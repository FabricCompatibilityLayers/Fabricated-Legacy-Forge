package cpw.mods.fml.common;

import cpw.mods.fml.common.discovery.ASMDataTable;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;

public class ProxyInjector {
    public ProxyInjector() {
    }

    public static void inject(ModContainer mod, ASMDataTable data, Side side) {
        FMLLog.fine("Attempting to inject @SidedProxy classes into %s", mod.getModId());
        Set<ASMDataTable.ASMData> targets = data.getAnnotationsFor(mod).get(SidedProxy.class.getName());
        ClassLoader mcl = Loader.instance().getModClassLoader();

        for (ASMDataTable.ASMData targ : targets)
        {
            try
            {
                Class<?> proxyTarget = Class.forName(targ.getClassName(), true, mcl);
                Field target = proxyTarget.getDeclaredField(targ.getObjectName());
                if (target == null)
                {
                    // Impossible?
                    FMLLog.severe("Attempted to load a proxy type into %s.%s but the field was not found", targ.getClassName(), targ.getObjectName());
                    throw new LoaderException();
                }

                String targetType = side.isClient() ? target.getAnnotation(SidedProxy.class).clientSide() : target.getAnnotation(SidedProxy.class).serverSide();
                Object proxy=Class.forName(targetType, true, mcl).newInstance();

                if ((target.getModifiers() & Modifier.STATIC) == 0 )
                {
                    FMLLog.severe("Attempted to load a proxy type %s into %s.%s, but the field is not static", targetType, targ.getClassName(), targ.getObjectName());
                    throw new LoaderException();
                }
                if (!target.getType().isAssignableFrom(proxy.getClass()))
                {
                    FMLLog.severe("Attempted to load a proxy type %s into %s.%s, but the types don't match", targetType, targ.getClassName(), targ.getObjectName());
                    throw new LoaderException();
                }
                target.set(null, proxy);
            }
            catch (Exception e)
            {
                FMLLog.log(Level.SEVERE, e, "An error occured trying to load a proxy into %s.%s", targ.getAnnotationInfo(), targ.getClassName(), targ.getObjectName());
                throw new LoaderException(e);
            }
        }
    }
}
