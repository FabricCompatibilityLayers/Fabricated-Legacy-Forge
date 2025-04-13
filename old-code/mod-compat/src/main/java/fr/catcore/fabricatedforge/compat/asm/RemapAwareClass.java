package fr.catcore.fabricatedforge.compat.asm;

import io.github.fabriccompatibiltylayers.modremappingapi.api.MappingUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class RemapAwareClass {
    public static Class<?> forName(String className)
            throws ClassNotFoundException {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            className = MappingUtils.mapClass(className);

            return Class.forName(className);
        }
    }
    public static Class<?> forName(String className, boolean initialize,
                                   ClassLoader loader)
            throws ClassNotFoundException {
        try {
            return Class.forName(className, initialize, loader);
        } catch (ClassNotFoundException e) {
            className = MappingUtils.mapClass(className);

            return Class.forName(className, initialize, loader);
        }
    }

    public static Method getDeclaredMethod(Class<?> clazz, String name, Class<?>... parameterTypes) throws NoSuchMethodException {
        try {
            return clazz.getDeclaredMethod(name, parameterTypes);
        } catch (NoSuchMethodException e) {
            name = MappingUtils.mapMethod(clazz, name, parameterTypes).name;

            return clazz.getDeclaredMethod(name, parameterTypes);
        }
    }

    public static Field getDeclaredField(Class<?> clazz, String name) throws NoSuchFieldException {
        try {
            return clazz.getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            name = MappingUtils.mapField(clazz, name).name;

            return clazz.getDeclaredField(name);
        }
    }

    public static Method getMethod(Class<?> clazz, String name, Class<?>... parameterTypes) throws NoSuchMethodException, SecurityException {
        final String originalName = name;
        try {
            return clazz.getMethod(name, parameterTypes);
        } catch (NoSuchMethodException e) {
            name = MappingUtils.mapMethod(clazz, name, parameterTypes).name;

            try {
                return clazz.getMethod(name, parameterTypes);
            } catch (NoSuchMethodException e1) {
                return getDeclaredMethod(clazz, originalName, parameterTypes);
            }
        }
    }

    public static Field getField(Class<?> clazz, String name) throws NoSuchFieldException, SecurityException {
        final String originalName = name;
        try {
            return clazz.getField(name);
        } catch (NoSuchFieldException e) {
            name = MappingUtils.mapField(clazz, name).name;

            try {
                return clazz.getField(name);
            } catch (NoSuchFieldException e1) {
                return getDeclaredField(clazz, originalName);
            }
        }
    }
}