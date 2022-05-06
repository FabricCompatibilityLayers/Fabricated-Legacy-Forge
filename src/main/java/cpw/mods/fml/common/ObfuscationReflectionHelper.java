package cpw.mods.fml.common;

import cpw.mods.fml.relauncher.ReflectionHelper;

import java.util.Arrays;
import java.util.logging.Level;

public class ObfuscationReflectionHelper {
    public static boolean obfuscation;

    public ObfuscationReflectionHelper() {
    }

    public static <T, E> T getPrivateValue(Class<? super E> classToAccess, E instance, int fieldIndex) {
        try {
            return ReflectionHelper.getPrivateValue(classToAccess, instance, fieldIndex);
        } catch (ReflectionHelper.UnableToAccessFieldException var4) {
            FMLLog.log(Level.SEVERE, var4, "There was a problem getting field index %d from %s", new Object[]{fieldIndex, classToAccess.getName()});
            throw var4;
        }
    }

    public static <T, E> T getPrivateValue(Class<? super E> classToAccess, E instance, String... fieldNames) {
        try {
            return ReflectionHelper.getPrivateValue(classToAccess, instance, fieldNames);
        } catch (ReflectionHelper.UnableToFindFieldException var4) {
            FMLLog.log(Level.SEVERE, var4, "Unable to locate any field %s on type %s", new Object[]{Arrays.toString(fieldNames), classToAccess.getName()});
            throw var4;
        } catch (ReflectionHelper.UnableToAccessFieldException var5) {
            FMLLog.log(Level.SEVERE, var5, "Unable to access any field %s on type %s", new Object[]{Arrays.toString(fieldNames), classToAccess.getName()});
            throw var5;
        }
    }

    /** @deprecated */
    @Deprecated
    public static <T, E> void setPrivateValue(Class<? super T> classToAccess, T instance, int fieldIndex, E value) {
        setPrivateValue(classToAccess, instance, value, fieldIndex);
    }

    public static <T, E> void setPrivateValue(Class<? super T> classToAccess, T instance, E value, int fieldIndex) {
        try {
            ReflectionHelper.setPrivateValue(classToAccess, instance, value, fieldIndex);
        } catch (ReflectionHelper.UnableToAccessFieldException var5) {
            FMLLog.log(Level.SEVERE, var5, "There was a problem setting field index %d on type %s", new Object[]{fieldIndex, classToAccess.getName()});
            throw var5;
        }
    }

    /** @deprecated */
    @Deprecated
    public static <T, E> void setPrivateValue(Class<? super T> classToAccess, T instance, String fieldName, E value) {
        setPrivateValue(classToAccess, instance, value, fieldName);
    }

    public static <T, E> void setPrivateValue(Class<? super T> classToAccess, T instance, E value, String... fieldNames) {
        try {
            ReflectionHelper.setPrivateValue(classToAccess, instance, value, fieldNames);
        } catch (ReflectionHelper.UnableToFindFieldException var5) {
            FMLLog.log(Level.SEVERE, var5, "Unable to locate any field %s on type %s", new Object[]{Arrays.toString(fieldNames), classToAccess.getName()});
            throw var5;
        } catch (ReflectionHelper.UnableToAccessFieldException var6) {
            FMLLog.log(Level.SEVERE, var6, "Unable to set any field %s on type %s", new Object[]{Arrays.toString(fieldNames), classToAccess.getName()});
            throw var6;
        }
    }

    public static void detectObfuscation(Class<?> clazz) {
        obfuscation = !clazz.getSimpleName().equals("World");
    }
}
