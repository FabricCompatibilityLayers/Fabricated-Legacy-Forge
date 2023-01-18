/*
 * The FML Forge Mod Loader suite.
 * Copyright (C) 2012 cpw
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this library; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 */
package cpw.mods.fml.common;

import com.google.common.base.Function;
import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import com.google.common.collect.*;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import cpw.mods.fml.common.discovery.ASMDataTable;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.network.FMLNetworkHandler;
import cpw.mods.fml.common.versioning.ArtifactVersion;
import cpw.mods.fml.common.versioning.DefaultArtifactVersion;
import cpw.mods.fml.common.versioning.VersionParser;
import cpw.mods.fml.common.versioning.VersionRange;

import java.io.File;
import java.io.FileInputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.logging.Level;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class FMLModContainer implements ModContainer {
    private Mod modDescriptor;
    private Object modInstance;
    private File source;
    private ModMetadata modMetadata;
    private String className;
    private Map<String, Object> descriptor;
    private boolean enabled = true;
    private String internalVersion;
    private boolean overridesMetadata;
    private EventBus eventBus;
    private LoadController controller;
    private Multimap<Class<? extends Annotation>, Object> annotations;
    private DefaultArtifactVersion processedVersion;
    private boolean isNetworkMod;
    private static final BiMap<Class<? extends FMLStateEvent>, Class<? extends Annotation>> modAnnotationTypes = ImmutableBiMap.<Class<? extends FMLStateEvent>, Class<? extends Annotation>>builder().put(FMLPreInitializationEvent.class, Mod.PreInit.class).put(FMLInitializationEvent.class, Mod.Init.class).put(FMLPostInitializationEvent.class, Mod.PostInit.class).put(FMLServerStartingEvent.class, Mod.ServerStarting.class).put(FMLServerStartedEvent.class, Mod.ServerStarted.class).put(FMLServerStoppingEvent.class, Mod.ServerStopping.class).build();
    private static final BiMap<Class<? extends Annotation>, Class<? extends FMLStateEvent>> modTypeAnnotations;
    private String annotationDependencies;
    private VersionRange minecraftAccepted;

    public FMLModContainer(String className, File modSource, Map<String, Object> modDescriptor) {
        this.className = className;
        this.source = modSource;
        this.descriptor = modDescriptor;
    }

    public String getModId() {
        return (String)this.descriptor.get("modid");
    }

    public String getName() {
        return this.modMetadata.name;
    }

    public String getVersion() {
        return this.internalVersion;
    }

    public File getSource() {
        return this.source;
    }

    public ModMetadata getMetadata() {
        return this.modMetadata;
    }

    public void bindMetadata(MetadataCollection mc) {
        this.modMetadata = mc.getMetadataForId(this.getModId(), this.descriptor);
        if (this.descriptor.containsKey("useMetadata")) {
            this.overridesMetadata = !(Boolean)this.descriptor.get("useMetadata");
        }

        if (!this.overridesMetadata && this.modMetadata.useDependencyInformation) {
            FMLLog.finest("Using mcmod dependency info : %s %s %s", this.modMetadata.requiredMods, this.modMetadata.dependencies, this.modMetadata.dependants);
        } else {
            Set<ArtifactVersion> requirements = Sets.newHashSet();
            List<ArtifactVersion> dependencies = Lists.newArrayList();
            List<ArtifactVersion> dependants = Lists.newArrayList();
            this.annotationDependencies = (String)this.descriptor.get("dependencies");
            Loader.instance().computeDependencies(this.annotationDependencies, requirements, dependencies, dependants);
            this.modMetadata.requiredMods = requirements;
            this.modMetadata.dependencies = dependencies;
            this.modMetadata.dependants = dependants;
            FMLLog.finest("Parsed dependency info : %s %s %s", requirements, dependencies, dependants);
        }

        if (Strings.isNullOrEmpty(this.modMetadata.name)) {
            FMLLog.info("Mod %s is missing the required element 'name'. Substituting %s", this.getModId(), this.getModId());
            this.modMetadata.name = this.getModId();
        }

        this.internalVersion = (String)this.descriptor.get("version");
        if (Strings.isNullOrEmpty(this.internalVersion)) {
            Properties versionProps = this.searchForVersionProperties();
            if (versionProps != null) {
                this.internalVersion = versionProps.getProperty(this.getModId() + ".version");
                FMLLog.fine("Found version %s for mod %s in version.properties, using", this.internalVersion, this.getModId());
            }
        }

        if (Strings.isNullOrEmpty(this.internalVersion) && !Strings.isNullOrEmpty(this.modMetadata.version)) {
            FMLLog.warning("Mod %s is missing the required element 'version' and a version.properties file could not be found. Falling back to metadata version %s", this.getModId(), this.modMetadata.version);
            this.internalVersion = this.modMetadata.version;
        }

        if (Strings.isNullOrEmpty(this.internalVersion)) {
            FMLLog.warning("Mod %s is missing the required element 'version' and no fallback can be found. Substituting '1.0'.", this.getModId());
            this.modMetadata.version = this.internalVersion = "1.0";
        }

        String mcVersionString = (String)this.descriptor.get("acceptedMinecraftVersions");
        if (!Strings.isNullOrEmpty(mcVersionString)) {
            this.minecraftAccepted = VersionParser.parseRange(mcVersionString);
        } else {
            this.minecraftAccepted = Loader.instance().getMinecraftModContainer().getStaticVersionRange();
        }

    }

    public Properties searchForVersionProperties() {
        try {
            FMLLog.fine("Attempting to load the file version.properties from %s to locate a version number for %s", this.getSource().getName(), this.getModId());
            Properties version = null;
            if (this.getSource().isFile()) {
                ZipFile source = new ZipFile(this.getSource());
                ZipEntry versionFile = source.getEntry("version.properties");
                if (versionFile != null) {
                    version = new Properties();
                    version.load(source.getInputStream(versionFile));
                }

                source.close();
            } else if (this.getSource().isDirectory()) {
                File propsFile = new File(this.getSource(), "version.properties");
                if (propsFile.exists() && propsFile.isFile()) {
                    version = new Properties();
                    FileInputStream fis = new FileInputStream(propsFile);
                    version.load(fis);
                    fis.close();
                }
            }

            return version;
        } catch (Exception var4) {
            Throwables.propagateIfPossible(var4);
            FMLLog.fine("Failed to find a usable version.properties file");
            return null;
        }
    }

    public void setEnabledState(boolean enabled) {
        this.enabled = enabled;
    }

    public Set<ArtifactVersion> getRequirements() {
        return this.modMetadata.requiredMods;
    }

    public List<ArtifactVersion> getDependencies() {
        return this.modMetadata.dependencies;
    }

    public List<ArtifactVersion> getDependants() {
        return this.modMetadata.dependants;
    }

    public String getSortingRules() {
        return !this.overridesMetadata && this.modMetadata.useDependencyInformation ? this.modMetadata.printableSortingRules() : Strings.nullToEmpty(this.annotationDependencies);
    }

    public boolean matches(Object mod) {
        return mod == this.modInstance;
    }

    public Object getMod() {
        return this.modInstance;
    }

    public boolean registerBus(EventBus bus, LoadController controller) {
        if (this.enabled) {
            FMLLog.fine("Enabling mod %s", this.getModId());
            this.eventBus = bus;
            this.controller = controller;
            this.eventBus.register(this);
            return true;
        } else {
            return false;
        }
    }

    private Multimap<Class<? extends Annotation>, Object> gatherAnnotations(Class<?> clazz) throws Exception {
        Multimap<Class<? extends Annotation>,Object> anns = ArrayListMultimap.create();

        for (Method m : clazz.getDeclaredMethods())
        {
            for (Annotation a : m.getAnnotations())
            {
                if (modTypeAnnotations.containsKey(a.annotationType()))
                {
                    Class<?>[] paramTypes = new Class[] { modTypeAnnotations.get(a.annotationType()) };

                    if (Arrays.equals(m.getParameterTypes(), paramTypes))
                    {
                        m.setAccessible(true);
                        anns.put(a.annotationType(), m);
                    }
                    else
                    {
                        FMLLog.severe("The mod %s appears to have an invalid method annotation %s. This annotation can only apply to methods with argument types %s -it will not be called", getModId(), a.annotationType().getSimpleName(), Arrays.toString(paramTypes));
                    }
                }
            }
        }
        return anns;
    }

    private void processFieldAnnotations(ASMDataTable asmDataTable) throws Exception {
        SetMultimap<String, ASMDataTable.ASMData> annotations = asmDataTable.getAnnotationsFor(this);
        this.parseSimpleFieldAnnotation(annotations, Mod.Instance.class.getName(), ModContainer::getMod);
        this.parseSimpleFieldAnnotation(annotations, Mod.Metadata.class.getName(), ModContainer::getMetadata);
    }

    private void parseSimpleFieldAnnotation(SetMultimap<String, ASMDataTable.ASMData> annotations, String annotationClassName, Function<ModContainer, Object> retreiver) throws IllegalAccessException {
        String[] annName = annotationClassName.split("\\.");
        String annotationName = annName[annName.length - 1];
        for (ASMDataTable.ASMData targets : annotations.get(annotationClassName))
        {
            String targetMod = (String) targets.getAnnotationInfo().get("value");
            Field f = null;
            Object injectedMod = null;
            ModContainer mc = this;
            boolean isStatic = false;
            Class<?> clz = modInstance.getClass();
            if (!Strings.isNullOrEmpty(targetMod))
            {
                if (Loader.isModLoaded(targetMod))
                {
                    mc = Loader.instance().getIndexedModList().get(targetMod);
                }
                else
                {
                    mc = null;
                }
            }
            if (mc != null)
            {
                try
                {
                    clz = Class.forName(targets.getClassName(), true, Loader.instance().getModClassLoader());
                    f = clz.getDeclaredField(targets.getObjectName());
                    f.setAccessible(true);
                    isStatic = Modifier.isStatic(f.getModifiers());
                    injectedMod = retreiver.apply(mc);
                }
                catch (Exception e)
                {
                    Throwables.propagateIfPossible(e);
                    FMLLog.log(Level.WARNING, e, "Attempting to load @%s in class %s for %s and failing", annotationName, targets.getClassName(), mc.getModId());
                }
            }
            if (f != null)
            {
                Object target = null;
                if (!isStatic)
                {
                    target = modInstance;
                    if (!modInstance.getClass().equals(clz))
                    {
                        FMLLog.warning("Unable to inject @%s in non-static field %s.%s for %s as it is NOT the primary mod instance", annotationName, targets.getClassName(), targets.getObjectName(), mc.getModId());
                        continue;
                    }
                }
                f.set(target, injectedMod);
            }
        }
    }

    @Subscribe
    public void constructMod(FMLConstructionEvent event) {
        try {
            ModClassLoader modClassLoader = event.getModClassLoader();
            modClassLoader.addFile(this.source);
            Class<?> clazz = Class.forName(this.className, true, modClassLoader);
            ASMDataTable asmHarvestedAnnotations = event.getASMHarvestedData();
            asmHarvestedAnnotations.getAnnotationsFor(this);
            this.annotations = this.gatherAnnotations(clazz);
            this.isNetworkMod = FMLNetworkHandler.instance().registerNetworkMod(this, clazz, event.getASMHarvestedData());
            this.modInstance = clazz.newInstance();
            ProxyInjector.inject(this, event.getASMHarvestedData(), FMLCommonHandler.instance().getSide());
            this.processFieldAnnotations(event.getASMHarvestedData());
        } catch (Throwable var5) {
            this.controller.errorOccurred(this, var5);
            Throwables.propagateIfPossible(var5);
        }

    }

    @Subscribe
    public void handleModStateEvent(FMLStateEvent event) {
        Class<? extends Annotation> annotation = modAnnotationTypes.get(event.getClass());
        if (annotation == null)
        {
            return;
        }
        try
        {
            for (Object o : annotations.get(annotation))
            {
                Method m = (Method) o;
                m.invoke(modInstance, event);
            }
        }
        catch (Throwable t)
        {
            controller.errorOccurred(this, t);
            Throwables.propagateIfPossible(t);
        }
    }

    public ArtifactVersion getProcessedVersion() {
        if (this.processedVersion == null) {
            this.processedVersion = new DefaultArtifactVersion(this.getModId(), this.getVersion());
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
        return this.modMetadata.version;
    }

    public VersionRange acceptableMinecraftVersionRange() {
        return this.minecraftAccepted;
    }

    static {
        modTypeAnnotations = modAnnotationTypes.inverse();
    }
}
