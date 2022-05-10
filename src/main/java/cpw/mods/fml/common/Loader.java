package cpw.mods.fml.common;

import com.google.common.base.CharMatcher;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.*;
import cpw.mods.fml.common.discovery.ModDiscoverer;
import cpw.mods.fml.common.event.FMLLoadEvent;
import cpw.mods.fml.common.functions.ModIdFunction;
import cpw.mods.fml.common.toposort.ModSorter;
import cpw.mods.fml.common.toposort.ModSortingException;
import cpw.mods.fml.common.versioning.ArtifactVersion;
import cpw.mods.fml.common.versioning.VersionParser;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.crash.provider.MinecraftVersionProvider;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.*;
import java.util.logging.Level;

public class Loader {
    private static final Splitter DEPENDENCYPARTSPLITTER = Splitter.on(":").omitEmptyStrings().trimResults();
    private static final Splitter DEPENDENCYSPLITTER = Splitter.on(";").omitEmptyStrings().trimResults();
    private static Loader instance;
    private static String major;
    private static String minor;
    private static String rev;
    private static String build;
    private static String mccversion;
    private static String mcsversion;
    private ModClassLoader modClassLoader = new ModClassLoader(this.getClass().getClassLoader());
    private List<ModContainer> mods;
    private Map<String, ModContainer> namedMods;
    private File canonicalConfigDir;
    private File canonicalMinecraftDir;
    private Exception capturedError;
    private File canonicalModsDir;
    private LoadController modController;
    private MinecraftDummyContainer minecraft;
    private static File minecraftDir;
    private static List<String> injectedContainers;

    public static Loader instance() {
        if (instance == null) {
            instance = new Loader();
        }

        return instance;
    }

    public static void injectData(Object... data) {
        major = (String)data[0];
        minor = (String)data[1];
        rev = (String)data[2];
        build = (String)data[3];
        mccversion = (String)data[4];
        mcsversion = (String)data[5];
        minecraftDir = (File)data[6];
        injectedContainers = (List)data[7];
    }

    private Loader() {
        String actualMCVersion = (new MinecraftVersionProvider((CrashReport)null)).call();
        if (!mccversion.equals(actualMCVersion)) {
            FMLLog.severe("This version of FML is built for Minecraft %s, we have detected Minecraft %s in your minecraft jar file", new Object[]{mccversion, actualMCVersion});
            throw new LoaderException();
        } else {
            this.minecraft = new MinecraftDummyContainer(actualMCVersion);
        }
    }

    private void sortModList() {
        FMLLog.fine("Verifying mod requirements are satisfied");
        try
        {
            BiMap<String, ArtifactVersion> modVersions = HashBiMap.create();
            for (ModContainer mod : getActiveModList())
            {
                modVersions.put(mod.getModId(), mod.getProcessedVersion());
            }

            for (ModContainer mod : getActiveModList())
            {
                if (!mod.acceptableMinecraftVersionRange().containsVersion(minecraft.getProcessedVersion()))
                {
                    FMLLog.severe("The mod %s does not wish to run in Minecraft version %s. You will have to remove it to play.", mod.getModId(), getMCVersionString());
                    throw new WrongMinecraftVersionException(mod);
                }
                Map<String,ArtifactVersion> names = Maps.uniqueIndex(mod.getRequirements(), new Function<ArtifactVersion, String>()
                {
                    public String apply(ArtifactVersion v)
                    {
                        return v.getLabel();
                    }
                });
                Set<ArtifactVersion> versionMissingMods = Sets.newHashSet();
                Set<String> missingMods = Sets.difference(names.keySet(), modVersions.keySet());
                if (!missingMods.isEmpty())
                {
                    FMLLog.severe("The mod %s (%s) requires mods %s to be available", mod.getModId(), mod.getName(), missingMods);
                    for (String modid : missingMods)
                    {
                        versionMissingMods.add(names.get(modid));
                    }
                    throw new MissingModsException(versionMissingMods);
                }
                ImmutableList<ArtifactVersion> allDeps = ImmutableList.<ArtifactVersion>builder().addAll(mod.getDependants()).addAll(mod.getDependencies()).build();
                for (ArtifactVersion v : allDeps)
                {
                    if (modVersions.containsKey(v.getLabel()))
                    {
                        if (!v.containsVersion(modVersions.get(v.getLabel())))
                        {
                            versionMissingMods.add(v);
                        }
                    }
                }
                if (!versionMissingMods.isEmpty())
                {
                    FMLLog.severe("The mod %s (%s) requires mod versions %s to be available", mod.getModId(), mod.getName(), versionMissingMods);
                    throw new MissingModsException(versionMissingMods);
                }
            }

            FMLLog.fine("All mod requirements are satisfied");

            ModSorter sorter = new ModSorter(getActiveModList(), namedMods);

            try
            {
                FMLLog.fine("Sorting mods into an ordered list");
                List<ModContainer> sortedMods = sorter.sort();
                // Reset active list to the sorted list
                modController.getActiveModList().clear();
                modController.getActiveModList().addAll(sortedMods);
                // And inject the sorted list into the overall list
                mods.removeAll(sortedMods);
                sortedMods.addAll(mods);
                mods = sortedMods;
                FMLLog.fine("Mod sorting completed successfully");
            }
            catch (ModSortingException sortException)
            {
                FMLLog.severe("A dependency cycle was detected in the input mod set so an ordering cannot be determined");
                FMLLog.severe("The visited mod list is %s", sortException.getExceptionData().getVisitedNodes());
                FMLLog.severe("The first mod in the cycle is %s", sortException.getExceptionData().getFirstBadNode());
                FMLLog.log(Level.SEVERE, sortException, "The full error");
                throw new LoaderException(sortException);
            }
        }
        finally
        {
            FMLLog.fine("Mod sorting data:");
            for (ModContainer mod : getActiveModList())
            {
                if (!mod.isImmutable())
                {
                    FMLLog.fine("\t%s(%s:%s): %s (%s)", mod.getModId(), mod.getName(), mod.getVersion(), mod.getSource().getName(), mod.getSortingRules());
                }
            }
            if (mods.size()==0)
            {
                FMLLog.fine("No mods found to sort");
            }
        }
    }

    private ModDiscoverer identifyMods() {
        FMLLog.fine("Building injected Mod Containers %s", injectedContainers);
        File coremod = new File(minecraftDir,"coremods");
        for (String cont : injectedContainers)
        {
            ModContainer mc;
            try
            {
                mc = (ModContainer) Class.forName(cont,true,modClassLoader).newInstance();
            }
            catch (Exception e)
            {
                FMLLog.log(Level.SEVERE, e, "A problem occured instantiating the injected mod container %s", cont);
                throw new LoaderException(e);
            }
            mods.add(new InjectedModContainer(mc,coremod));
        }
        ModDiscoverer discoverer = new ModDiscoverer();
        FMLLog.fine("Attempting to load mods contained in the minecraft jar file and associated classes");
        discoverer.findClasspathMods(modClassLoader);
        FMLLog.fine("Minecraft jar mods loaded successfully");

        FMLLog.info("Searching %s for mods", canonicalModsDir.getAbsolutePath());
        discoverer.findModDirMods(canonicalModsDir);

        mods.addAll(discoverer.identifyMods());
        identifyDuplicates(mods);
        namedMods = Maps.uniqueIndex(mods, new ModIdFunction());
        FMLLog.info("Forge Mod Loader has identified %d mod%s to load", mods.size(), mods.size() != 1 ? "s" : "");
        return discoverer;
    }

    private void identifyDuplicates(List<ModContainer> mods) {
        boolean foundDupe = false;
        TreeMultimap<ModContainer, File> dupsearch = TreeMultimap.create(new ModIdComparator(), Ordering.arbitrary());
        for (ModContainer mc : mods)
        {
            if (mc.getSource() != null)
            {
                dupsearch.put(mc, mc.getSource());
            }
        }

        ImmutableMultiset<ModContainer> duplist = Multisets.copyHighestCountFirst(dupsearch.keys());
        for (Multiset.Entry<ModContainer> e : duplist.entrySet())
        {
            if (e.getCount() > 1)
            {
                FMLLog.severe("Found a duplicate mod %s at %s", e.getElement().getModId(), dupsearch.get(e.getElement()));
                foundDupe = true;
            }
        }
        if (foundDupe) { throw new LoaderException(); }
    }

    private void initializeLoader() {
        File modsDir = new File(minecraftDir, "mods");
        File configDir = new File(minecraftDir, "config");

        String canonicalModsPath;
        String canonicalConfigPath;
        try {
            this.canonicalMinecraftDir = minecraftDir.getCanonicalFile();
            canonicalModsPath = modsDir.getCanonicalPath();
            canonicalConfigPath = configDir.getCanonicalPath();
            this.canonicalConfigDir = configDir.getCanonicalFile();
            this.canonicalModsDir = modsDir.getCanonicalFile();
        } catch (IOException var6) {
            FMLLog.log(Level.SEVERE, var6, "Failed to resolve loader directories: mods : %s ; config %s", new Object[]{this.canonicalModsDir.getAbsolutePath(), configDir.getAbsolutePath()});
            throw new LoaderException(var6);
        }

        boolean dirMade;
        if (!this.canonicalModsDir.exists()) {
            FMLLog.info("No mod directory found, creating one: %s", new Object[]{canonicalModsPath});
            dirMade = this.canonicalModsDir.mkdir();
            if (!dirMade) {
                FMLLog.severe("Unable to create the mod directory %s", new Object[]{canonicalModsPath});
                throw new LoaderException();
            }

            FMLLog.info("Mod directory created successfully", new Object[0]);
        }

        if (!this.canonicalConfigDir.exists()) {
            FMLLog.fine("No config directory found, creating one: %s", new Object[]{canonicalConfigPath});
            dirMade = this.canonicalConfigDir.mkdir();
            if (!dirMade) {
                FMLLog.severe("Unable to create the config directory %s", new Object[]{canonicalConfigPath});
                throw new LoaderException();
            }

            FMLLog.info("Config directory created successfully", new Object[0]);
        }

        if (!this.canonicalModsDir.isDirectory()) {
            FMLLog.severe("Attempting to load mods from %s, which is not a directory", new Object[]{canonicalModsPath});
            throw new LoaderException();
        } else if (!configDir.isDirectory()) {
            FMLLog.severe("Attempting to load configuration from %s, which is not a directory", new Object[]{canonicalConfigPath});
            throw new LoaderException();
        }
    }

    public List<ModContainer> getModList() {
        return instance().mods != null ? ImmutableList.copyOf(instance().mods) : ImmutableList.of();
    }

    public void loadMods() {
        initializeLoader();
        mods = Lists.newArrayList();
        namedMods = Maps.newHashMap();
        modController = new LoadController(this);
        modController.transition(LoaderState.LOADING);
        ModDiscoverer disc = identifyMods();
        disableRequestedMods();
        modController.distributeStateMessage(FMLLoadEvent.class);
        sortModList();
        mods = ImmutableList.copyOf(mods);
        for (File nonMod : disc.getNonModLibs())
        {
            if (nonMod.isFile())
            {
                FMLLog.severe("FML has found a non-mod file %s in your mods directory. It will now be injected into your classpath. This could severe stability issues, it should be removed if possible.", nonMod.getName());
                try
                {
                    modClassLoader.addFile(nonMod);
                }
                catch (MalformedURLException e)
                {
                    FMLLog.log(Level.SEVERE, e, "Encountered a weird problem with non-mod file injection : %s", nonMod.getName());
                }
            }
        }
        modController.transition(LoaderState.CONSTRUCTING);
        modController.distributeStateMessage(LoaderState.CONSTRUCTING, modClassLoader, disc.getASMTable());
        modController.transition(LoaderState.PREINITIALIZATION);
        modController.distributeStateMessage(LoaderState.PREINITIALIZATION, disc.getASMTable(), canonicalConfigDir);
        modController.transition(LoaderState.INITIALIZATION);
    }

    private void disableRequestedMods() {
        String forcedModList = System.getProperty("fml.modStates", "");
        FMLLog.fine("Received a system property request \'%s\'",forcedModList);
        Map<String, String> sysPropertyStateList = Splitter.on(CharMatcher.anyOf(";:"))
                .omitEmptyStrings().trimResults().withKeyValueSeparator("=")
                .split(forcedModList);
        FMLLog.fine("System property request managing the state of %d mods", sysPropertyStateList.size());
        Map<String, String> modStates = Maps.newHashMap();

        File forcedModFile = new File(canonicalConfigDir, "fmlModState.properties");
        Properties forcedModListProperties = new Properties();
        if (forcedModFile.exists() && forcedModFile.isFile())
        {
            FMLLog.fine("Found a mod state file %s", forcedModFile.getName());
            try
            {
                forcedModListProperties.load(new FileReader(forcedModFile));
                FMLLog.fine("Loaded states for %d mods from file", forcedModListProperties.size());
            }
            catch (Exception e)
            {
                FMLLog.log(Level.INFO, e, "An error occurred reading the fmlModState.properties file");
            }
        }
        modStates.putAll(Maps.fromProperties(forcedModListProperties));
        modStates.putAll(sysPropertyStateList);
        FMLLog.fine("After merging, found state information for %d mods", modStates.size());

        Map<String, Boolean> isEnabled = Maps.transformValues(modStates, new Function<String, Boolean>()
        {
            public Boolean apply(String input)
            {
                return Boolean.parseBoolean(input);
            }
        });

        for (Map.Entry<String, Boolean> entry : isEnabled.entrySet())
        {
            if (namedMods.containsKey(entry.getKey()))
            {
                FMLLog.info("Setting mod %s to enabled state %b", entry.getKey(), entry.getValue());
                namedMods.get(entry.getKey()).setEnabledState(entry.getValue());
            }
        }
    }

    public static boolean isModLoaded(String modname) {
        return instance().namedMods.containsKey(modname) && instance().modController.getModState((ModContainer)instance.namedMods.get(modname)) != LoaderState.ModState.DISABLED;
    }

    public File getConfigDir() {
        return this.canonicalConfigDir;
    }

    public String getCrashInformation() {
        StringBuilder ret = new StringBuilder();
        List<String> branding = FMLCommonHandler.instance().getBrandings();
        Joiner.on(' ').skipNulls().appendTo(ret, branding.subList(1, branding.size()));
        if (this.modController != null) {
            this.modController.printModStates(ret);
        }

        return ret.toString();
    }

    public String getFMLVersionString() {
        return String.format("%s.%s.%s.%s", major, minor, rev, build);
    }

    public ClassLoader getModClassLoader() {
        return this.modClassLoader;
    }

    public void computeDependencies(String dependencyString, Set<ArtifactVersion> requirements, List<ArtifactVersion> dependencies, List<ArtifactVersion> dependants) {
        if (dependencyString == null || dependencyString.length() == 0)
        {
            return;
        }

        boolean parseFailure=false;

        for (String dep : DEPENDENCYSPLITTER.split(dependencyString))
        {
            List<String> depparts = Lists.newArrayList(DEPENDENCYPARTSPLITTER.split(dep));
            // Need two parts to the string
            if (depparts.size() != 2)
            {
                parseFailure=true;
                continue;
            }
            String instruction = depparts.get(0);
            String target = depparts.get(1);
            boolean targetIsAll = target.startsWith("*");

            // Cannot have an "all" relationship with anything except pure *
            if (targetIsAll && target.length()>1)
            {
                parseFailure = true;
                continue;
            }

            // If this is a required element, add it to the required list
            if ("required-before".equals(instruction) || "required-after".equals(instruction))
            {
                // You can't require everything
                if (!targetIsAll)
                {
                    requirements.add(VersionParser.parseVersionReference(target));
                }
                else
                {
                    parseFailure=true;
                    continue;
                }
            }

            // You cannot have a versioned dependency on everything
            if (targetIsAll && target.indexOf('@')>-1)
            {
                parseFailure = true;
                continue;
            }
            // before elements are things we are loaded before (so they are our dependants)
            if ("required-before".equals(instruction) || "before".equals(instruction))
            {
                dependants.add(VersionParser.parseVersionReference(target));
            }
            // after elements are things that load before we do (so they are out dependencies)
            else if ("required-after".equals(instruction) || "after".equals(instruction))
            {
                dependencies.add(VersionParser.parseVersionReference(target));
            }
            else
            {
                parseFailure=true;
            }
        }

        if (parseFailure)
        {
            FMLLog.log(Level.WARNING, "Unable to parse dependency string %s", dependencyString);
            throw new LoaderException();
        }
    }

    public Map<String, ModContainer> getIndexedModList() {
        return ImmutableMap.copyOf(this.namedMods);
    }

    public void initializeMods() {
        this.modController.distributeStateMessage(LoaderState.INITIALIZATION, new Object[0]);
        this.modController.transition(LoaderState.POSTINITIALIZATION);
        this.modController.distributeStateMessage(LoaderState.POSTINITIALIZATION, new Object[0]);
        this.modController.transition(LoaderState.AVAILABLE);
        this.modController.distributeStateMessage(LoaderState.AVAILABLE, new Object[0]);
        FMLLog.info("Forge Mod Loader has successfully loaded %d mod%s", new Object[]{this.mods.size(), this.mods.size() == 1 ? "" : "s"});
    }

    public ICrashCallable getCallableCrashInformation() {
        return new ICrashCallable() {
            public String call() throws Exception {
                return Loader.this.getCrashInformation();
            }

            public String getLabel() {
                return "FML";
            }
        };
    }

    public List<ModContainer> getActiveModList() {
        return (List)(this.modController != null ? this.modController.getActiveModList() : ImmutableList.of());
    }

    public LoaderState.ModState getModState(ModContainer selectedMod) {
        return this.modController.getModState(selectedMod);
    }

    public String getMCVersionString() {
        return "Minecraft " + mccversion;
    }

    public void serverStarting(Object server) {
        this.modController.distributeStateMessage(LoaderState.SERVER_STARTING, new Object[]{server});
        this.modController.transition(LoaderState.SERVER_STARTING);
    }

    public void serverStarted() {
        this.modController.distributeStateMessage(LoaderState.SERVER_STARTED, new Object[0]);
        this.modController.transition(LoaderState.SERVER_STARTED);
    }

    public void serverStopping() {
        this.modController.distributeStateMessage(LoaderState.SERVER_STOPPING, new Object[0]);
        this.modController.transition(LoaderState.SERVER_STOPPING);
        this.modController.transition(LoaderState.AVAILABLE);
    }

    public BiMap<ModContainer, Object> getModObjectList() {
        return this.modController.getModObjectList();
    }

    public BiMap<Object, ModContainer> getReversedModObjectList() {
        return this.getModObjectList().inverse();
    }

    public ModContainer activeModContainer() {
        return this.modController.activeContainer();
    }

    public boolean isInState(LoaderState state) {
        return this.modController.isInState(state);
    }

    public MinecraftDummyContainer getMinecraftModContainer() {
        return this.minecraft;
    }

    private class ModIdComparator implements Comparator<ModContainer> {
        private ModIdComparator() {
        }

        public int compare(ModContainer o1, ModContainer o2) {
            return o1.getModId().compareTo(o2.getModId());
        }
    }
}
