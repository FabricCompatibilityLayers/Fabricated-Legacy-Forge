package cpw.mods.fml.common.modloader;

import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import cpw.mods.fml.common.*;
import cpw.mods.fml.common.discovery.ASMDataTable;
import cpw.mods.fml.common.discovery.ContainerType;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.network.FMLNetworkHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.common.versioning.ArtifactVersion;
import cpw.mods.fml.common.versioning.DefaultArtifactVersion;
import cpw.mods.fml.common.versioning.VersionRange;
import net.minecraft.command.Command;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.logging.Level;

public class ModLoaderModContainer implements ModContainer {
    public BaseModProxy mod;
    private File modSource;
    public Set<ArtifactVersion> requirements = Sets.newHashSet();
    public ArrayList<ArtifactVersion> dependencies = Lists.newArrayList();
    public ArrayList<ArtifactVersion> dependants = Lists.newArrayList();
    private ContainerType sourceType;
    private ModMetadata metadata;
    private ProxyInjector sidedProxy;
    private BaseModTicker gameTickHandler;
    private BaseModTicker guiTickHandler;
    private String modClazzName;
    private String modId;
    private EventBus bus;
    private LoadController controller;
    private boolean enabled = true;
    private String sortingProperties;
    private ArtifactVersion processedVersion;
    private boolean isNetworkMod;
    private List<Command> serverCommands = Lists.newArrayList();

    public ModLoaderModContainer(String className, File modSource, String sortingProperties) {
        this.modClazzName = className;
        this.modSource = modSource;
        this.modId = className.contains(".") ? className.substring(className.lastIndexOf(46) + 1) : className;
        this.sortingProperties = Strings.isNullOrEmpty(sortingProperties) ? "" : sortingProperties;
    }

    ModLoaderModContainer(BaseModProxy instance) {
        this.mod = instance;
        this.gameTickHandler = new BaseModTicker(instance, false);
        this.guiTickHandler = new BaseModTicker(instance, true);
    }

    private void configureMod(Class<? extends BaseModProxy> modClazz, ASMDataTable asmData) {
        File configDir = Loader.instance().getConfigDir();
        File modConfig = new File(configDir, String.format("%s.cfg", getModId()));
        Properties props = new Properties();

        boolean existingConfigFound = false;
        boolean mlPropFound = false;

        if (modConfig.exists())
        {
            try
            {
                FMLLog.fine("Reading existing configuration file for %s : %s", getModId(), modConfig.getName());
                FileReader configReader = new FileReader(modConfig);
                props.load(configReader);
                configReader.close();
            }
            catch (Exception e)
            {
                FMLLog.log(Level.SEVERE, e, "Error occured reading mod configuration file %s", modConfig.getName());
                throw new LoaderException(e);
            }
            existingConfigFound = true;
        }

        StringBuffer comments = new StringBuffer();
        comments.append("MLProperties: name (type:default) min:max -- information\n");


        List<ModProperty> mlPropFields = Lists.newArrayList();
        try
        {
            for (ASMDataTable.ASMData dat : Sets.union(asmData.getAnnotationsFor(this).get("net.minecraft.src.MLProp"), asmData.getAnnotationsFor(this).get("MLProp")))
            {
                if (dat.getClassName().equals(modClazzName))
                {
                    try
                    {
                        mlPropFields.add(new ModProperty(modClazz.getDeclaredField(dat.getObjectName()), dat.getAnnotationInfo()));
                        FMLLog.finest("Found an MLProp field %s in %s", dat.getObjectName(), getModId());
                    }
                    catch (Exception e)
                    {
                        FMLLog.log(Level.WARNING, e, "An error occured trying to access field %s in mod %s", dat.getObjectName(), getModId());
                    }
                }
            }
            for (ModProperty property : mlPropFields)
            {
                if (!Modifier.isStatic(property.field().getModifiers()))
                {
                    FMLLog.info("The MLProp field %s in mod %s appears not to be static", property.field().getName(), getModId());
                    continue;
                }
                FMLLog.finest("Considering MLProp field %s", property.field().getName());
                Field f = property.field();
                String propertyName = !Strings.nullToEmpty(property.name()).isEmpty() ? property.name() : f.getName();
                String propertyValue = null;
                Object defaultValue = null;

                try
                {
                    defaultValue = f.get(null);
                    propertyValue = props.getProperty(propertyName, extractValue(defaultValue));
                    Object currentValue = parseValue(propertyValue, property, f.getType(), propertyName);
                    FMLLog.finest("Configuration for %s.%s found values default: %s, configured: %s, interpreted: %s", modClazzName, propertyName, defaultValue, propertyValue, currentValue);

                    if (currentValue != null && !currentValue.equals(defaultValue))
                    {
                        FMLLog.finest("Configuration for %s.%s value set to: %s", modClazzName, propertyName, currentValue);
                        f.set(null, currentValue);
                    }
                }
                catch (Exception e)
                {
                    FMLLog.log(Level.SEVERE, e, "Invalid configuration found for %s in %s", propertyName, modConfig.getName());
                    throw new LoaderException(e);
                }
                finally
                {
                    comments.append(String.format("MLProp : %s (%s:%s", propertyName, f.getType().getName(), defaultValue));

                    if (property.min() != Double.MIN_VALUE)
                    {
                        comments.append(",>=").append(String.format("%.1f", property.min()));
                    }

                    if (property.max() != Double.MAX_VALUE)
                    {
                        comments.append(",<=").append(String.format("%.1f", property.max()));
                    }

                    comments.append(")");

                    if (!Strings.nullToEmpty(property.info()).isEmpty())
                    {
                        comments.append(" -- ").append(property.info());
                    }

                    if (propertyValue != null)
                    {
                        props.setProperty(propertyName, extractValue(propertyValue));
                    }
                    comments.append("\n");
                }
                mlPropFound = true;
            }
        }
        finally
        {
            if (!mlPropFound && !existingConfigFound)
            {
                FMLLog.fine("No MLProp configuration for %s found or required. No file written", getModId());
                return;
            }

            if (!mlPropFound && existingConfigFound)
            {
                File mlPropBackup = new File(modConfig.getParent(),modConfig.getName()+".bak");
                FMLLog.fine("MLProp configuration file for %s found but not required. Attempting to rename file to %s", getModId(), mlPropBackup.getName());
                boolean renamed = modConfig.renameTo(mlPropBackup);
                if (renamed)
                {
                    FMLLog.fine("Unused MLProp configuration file for %s renamed successfully to %s", getModId(), mlPropBackup.getName());
                }
                else
                {
                    FMLLog.fine("Unused MLProp configuration file for %s renamed UNSUCCESSFULLY to %s", getModId(), mlPropBackup.getName());
                }

                return;
            }
            try
            {
                FileWriter configWriter = new FileWriter(modConfig);
                props.store(configWriter, comments.toString());
                configWriter.close();
                FMLLog.fine("Configuration for %s written to %s", getModId(), modConfig.getName());
            }
            catch (IOException e)
            {
                FMLLog.log(Level.SEVERE, e, "Error trying to write the config file %s", modConfig.getName());
                throw new LoaderException(e);
            }
        }
    }

    private Object parseValue(String val, ModProperty property, Class<?> type, String propertyName) {
        if (type.isAssignableFrom(String.class)) {
            return val;
        } else if (!type.isAssignableFrom(Boolean.TYPE) && !type.isAssignableFrom(Boolean.class)) {
            if (!Number.class.isAssignableFrom(type) && !type.isPrimitive()) {
                throw new IllegalArgumentException(String.format("MLProp declared on %s of type %s, an unsupported type", propertyName, type.getName()));
            } else {
                Number n = null;
                if (!type.isAssignableFrom(Double.TYPE) && !Double.class.isAssignableFrom(type)) {
                    if (!type.isAssignableFrom(Float.TYPE) && !Float.class.isAssignableFrom(type)) {
                        if (!type.isAssignableFrom(Long.TYPE) && !Long.class.isAssignableFrom(type)) {
                            if (!type.isAssignableFrom(Integer.TYPE) && !Integer.class.isAssignableFrom(type)) {
                                if (!type.isAssignableFrom(Short.TYPE) && !Short.class.isAssignableFrom(type)) {
                                    if (!type.isAssignableFrom(Byte.TYPE) && !Byte.class.isAssignableFrom(type)) {
                                        throw new IllegalArgumentException(String.format("MLProp declared on %s of type %s, an unsupported type", propertyName, type.getName()));
                                    }

                                    n = Byte.parseByte(val);
                                } else {
                                    n = Short.parseShort(val);
                                }
                            } else {
                                n = Integer.parseInt(val);
                            }
                        } else {
                            n = Long.parseLong(val);
                        }
                    } else {
                        n = Float.parseFloat(val);
                    }
                } else {
                    n = Double.parseDouble(val);
                }

                double dVal = ((Number)n).doubleValue();
                if (property.min() != Double.MIN_VALUE && dVal < property.min() || property.max() != Double.MAX_VALUE && dVal > property.max()) {
                    FMLLog.warning("Configuration for %s.%s found value %s outside acceptable range %s,%s", new Object[]{this.modClazzName, propertyName, n, property.min(), property.max()});
                    return null;
                } else {
                    return n;
                }
            }
        } else {
            return Boolean.parseBoolean(val);
        }
    }

    private String extractValue(Object value) {
        if (String.class.isInstance(value)) {
            return (String)value;
        } else if (!Number.class.isInstance(value) && !Boolean.class.isInstance(value)) {
            throw new IllegalArgumentException("MLProp declared on non-standard type");
        } else {
            return String.valueOf(value);
        }
    }

    public String getName() {
        return this.mod != null ? this.mod.getName() : this.modId;
    }

    /** @deprecated */
    @Deprecated
    public static ModContainer findContainerFor(BaseModProxy mod) {
        return FMLCommonHandler.instance().findContainerFor(mod);
    }

    public String getSortingRules() {
        return this.sortingProperties;
    }

    public boolean matches(Object mod) {
        return this.mod == mod;
    }

    public static <A extends BaseModProxy> List<A> findAll(Class<A> clazz) {
        ArrayList<A> modList = new ArrayList<A>();

        for (ModContainer mc : Loader.instance().getActiveModList())
        {
            if (mc instanceof ModLoaderModContainer && mc.getMod()!=null)
            {
                modList.add((A)((ModLoaderModContainer)mc).mod);
            }
        }

        return modList;
    }

    public File getSource() {
        return this.modSource;
    }

    public Object getMod() {
        return this.mod;
    }

    public Set<ArtifactVersion> getRequirements() {
        return this.requirements;
    }

    public List<ArtifactVersion> getDependants() {
        return this.dependants;
    }

    public List<ArtifactVersion> getDependencies() {
        return this.dependencies;
    }

    public String toString() {
        return this.modId;
    }

    public ModMetadata getMetadata() {
        return this.metadata;
    }

    public String getVersion() {
        return this.mod != null && this.mod.getVersion() != null ? this.mod.getVersion() : "Not available";
    }

    public BaseModTicker getGameTickHandler() {
        return this.gameTickHandler;
    }

    public BaseModTicker getGUITickHandler() {
        return this.guiTickHandler;
    }

    public String getModId() {
        return this.modId;
    }

    public void bindMetadata(MetadataCollection mc) {
        Map<String, Object> dummyMetadata = ImmutableMap.<String,Object>builder().put("name", modId).put("version", "1.0").build();
        this.metadata = mc.getMetadataForId(modId, dummyMetadata);
        Loader.instance().computeDependencies(sortingProperties, getRequirements(), getDependencies(), getDependants());
    }

    public void setEnabledState(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean registerBus(EventBus bus, LoadController controller) {
        if (this.enabled) {
            FMLLog.fine("Enabling mod %s", new Object[]{this.getModId()});
            this.bus = bus;
            this.controller = controller;
            bus.register(this);
            return true;
        } else {
            return false;
        }
    }

    @Subscribe
    public void constructMod(FMLConstructionEvent event) {
        try {
            ModClassLoader modClassLoader = event.getModClassLoader();
            modClassLoader.addFile(this.modSource);
            EnumSet<TickType> ticks = EnumSet.noneOf(TickType.class);
            this.gameTickHandler = new BaseModTicker(ticks, false);
            this.guiTickHandler = new BaseModTicker(ticks.clone(), true);
            Class<? extends BaseModProxy> modClazz = modClassLoader.loadBaseModClass(this.modClazzName);
            this.configureMod(modClazz, event.getASMHarvestedData());
            this.isNetworkMod = FMLNetworkHandler.instance().registerNetworkMod(this, modClazz, event.getASMHarvestedData());
            ModLoaderNetworkHandler dummyHandler = null;
            if (!this.isNetworkMod) {
                FMLLog.fine("Injecting dummy network mod handler for BaseMod %s", new Object[]{this.getModId()});
                dummyHandler = new ModLoaderNetworkHandler(this);
                FMLNetworkHandler.instance().registerNetworkMod(dummyHandler);
            }

            Constructor<? extends BaseModProxy> ctor = modClazz.getConstructor();
            ctor.setAccessible(true);
            this.mod = (BaseModProxy)modClazz.newInstance();
            if (dummyHandler != null) {
                dummyHandler.setBaseMod(this.mod);
            }

            ProxyInjector.inject(this, event.getASMHarvestedData(), FMLCommonHandler.instance().getSide());
        } catch (Exception var7) {
            this.controller.errorOccurred(this, var7);
            Throwables.propagateIfPossible(var7);
        }

    }

    @Subscribe
    public void preInit(FMLPreInitializationEvent event) {
        try {
            this.gameTickHandler.setMod(this.mod);
            this.guiTickHandler.setMod(this.mod);
            TickRegistry.registerTickHandler(this.gameTickHandler, Side.CLIENT);
            TickRegistry.registerTickHandler(this.guiTickHandler, Side.CLIENT);
            GameRegistry.registerWorldGenerator(ModLoaderHelper.buildWorldGenHelper(this.mod));
            GameRegistry.registerFuelHandler(ModLoaderHelper.buildFuelHelper(this.mod));
            GameRegistry.registerCraftingHandler(ModLoaderHelper.buildCraftingHelper(this.mod));
            GameRegistry.registerPickupHandler(ModLoaderHelper.buildPickupHelper(this.mod));
            GameRegistry.registerDispenserHandler(ModLoaderHelper.buildDispenseHelper(this.mod));
            NetworkRegistry.instance().registerChatListener(ModLoaderHelper.buildChatListener(this.mod));
            NetworkRegistry.instance().registerConnectionHandler(ModLoaderHelper.buildConnectionHelper(this.mod));
        } catch (Exception var3) {
            this.controller.errorOccurred(this, var3);
            Throwables.propagateIfPossible(var3);
        }

    }

    @Subscribe
    public void init(FMLInitializationEvent event) {
        try {
            this.mod.load();
        } catch (Throwable var3) {
            this.controller.errorOccurred(this, var3);
            Throwables.propagateIfPossible(var3);
        }

    }

    @Subscribe
    public void postInit(FMLPostInitializationEvent event) {
        try {
            this.mod.modsLoaded();
        } catch (Throwable var3) {
            this.controller.errorOccurred(this, var3);
            Throwables.propagateIfPossible(var3);
        }

    }

    @Subscribe
    public void loadComplete(FMLLoadCompleteEvent complete) {
        ModLoaderHelper.finishModLoading(this);
    }

    @Subscribe
    public void serverStarting(FMLServerStartingEvent evt) {
        for (Command cmd : serverCommands)
        {
            evt.registerServerCommand(cmd);
        }
    }

    public ArtifactVersion getProcessedVersion() {
        if (this.processedVersion == null) {
            this.processedVersion = new DefaultArtifactVersion(this.modId, this.getVersion());
        }

        return this.processedVersion;
    }

    public boolean isImmutable() {
        return false;
    }

    public boolean isNetworkMod() {
        return this.isNetworkMod;
    }

    public String getDisplayVersion() {
        return this.metadata != null ? this.metadata.version : this.getVersion();
    }

    public void addServerCommand(Command command) {
        this.serverCommands.add(command);
    }

    public VersionRange acceptableMinecraftVersionRange() {
        return Loader.instance().getMinecraftModContainer().getStaticVersionRange();
    }
}
