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
package cpw.mods.fml.relauncher;

import io.github.fabriccompatibiltylayers.modremappingapi.api.MappingUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectionHelper {
    public ReflectionHelper() {
    }

    public static Field findField(Class<?> clazz, String... fieldNames) {
        Exception failed = null;
        for (String fieldName : fieldNames)
        {
            try
            {
                fieldName = MappingUtils.mapField(clazz, fieldName).name;
                Field f = clazz.getDeclaredField(fieldName);
                f.setAccessible(true);
                return f;
            }
            catch (Exception e)
            {
                failed = e;
            }
        }
        throw new UnableToFindFieldException(fieldNames, failed);
    }

    public static <T, E> T getPrivateValue(Class<? super E> classToAccess, E instance, int fieldIndex) {
        try {
            Field f = classToAccess.getDeclaredFields()[fieldIndex];
            f.setAccessible(true);
            return (T) f.get(instance);
        } catch (Exception var4) {
            throw new ReflectionHelper.UnableToAccessFieldException(new String[0], var4);
        }
    }

    public static <T, E> T getPrivateValue(Class<? super E> classToAccess, E instance, String... fieldNames) {
        try {
            return (T) findField(classToAccess, fieldNames).get(instance);
        } catch (Exception var4) {
            throw new ReflectionHelper.UnableToAccessFieldException(fieldNames, var4);
        }
    }

    public static <T, E> void setPrivateValue(Class<? super T> classToAccess, T instance, E value, int fieldIndex) {
        try {
            Field f = classToAccess.getDeclaredFields()[fieldIndex];
            f.setAccessible(true);
            f.set(instance, value);
        } catch (Exception var5) {
            throw new ReflectionHelper.UnableToAccessFieldException(new String[0], var5);
        }
    }

    public static <T, E> void setPrivateValue(Class<? super T> classToAccess, T instance, E value, String... fieldNames) {
        try {
            findField(classToAccess, fieldNames).set(instance, value);
        } catch (Exception var5) {
            throw new ReflectionHelper.UnableToAccessFieldException(fieldNames, var5);
        }
    }

    public static Class<? super Object> getClass(ClassLoader loader, String... classNames) {
        Exception err = null;
        for (String className : classNames)
        {
            try
            {
                className = MappingUtils.mapClass(className);
                return (Class<? super Object>) Class.forName(className, false, loader);
            }
            catch (Exception e)
            {
                err = e;
            }
        }

        throw new UnableToFindClassException(classNames, err);
    }

    public static <E> Method findMethod(Class<? super E> clazz, E instance, String[] methodNames, Class<?>... methodTypes) {
        Exception failed = null;
        for (String methodName : methodNames)
        {
            try
            {
                methodName = MappingUtils.mapMethod(clazz, methodName, methodTypes).name;
                Method m = clazz.getDeclaredMethod(methodName, methodTypes);
                m.setAccessible(true);
                return m;
            }
            catch (Exception e)
            {
                failed = e;
            }
        }
        throw new UnableToFindMethodException(methodNames, failed);
    }

    public static class UnableToFindFieldException extends RuntimeException {
        private String[] fieldNameList;

        public UnableToFindFieldException(String[] fieldNameList, Exception e) {
            super(e);
            this.fieldNameList = fieldNameList;
        }
    }

    public static class UnableToAccessFieldException extends RuntimeException {
        private String[] fieldNameList;

        public UnableToAccessFieldException(String[] fieldNames, Exception e) {
            super(e);
            this.fieldNameList = fieldNames;
        }
    }

    public static class UnableToFindClassException extends RuntimeException {
        private String[] classNames;

        public UnableToFindClassException(String[] classNames, Exception err) {
            super(err);
            this.classNames = classNames;
        }
    }

    public static class UnableToFindMethodException extends RuntimeException {
        private String[] methodNames;

        public UnableToFindMethodException(String[] methodNames, Exception failed) {
            super(failed);
            this.methodNames = methodNames;
        }
    }
}
