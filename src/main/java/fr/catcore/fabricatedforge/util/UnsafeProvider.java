package fr.catcore.fabricatedforge.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

// Copyright (c) 2022 Modification Station
public class UnsafeProvider {
    private final Class<?> unsafeClass;
    private final Object unsafeInstance;

    private final Method getObject;
    private final Method staticFieldBase;
    private final Method staticFieldOffset;
    private final Method objectFieldOffset;
    private final Method putObject;

    protected UnsafeProvider() {
        try {
            this.unsafeClass = Class.forName("sun.misc.Unsafe");
            Field field = this.unsafeClass.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            this.unsafeInstance = field.get(null);

            this.getObject = this.unsafeClass.getMethod("getObject", Object.class, long.class);
            this.staticFieldBase = this.unsafeClass.getMethod("staticFieldBase", Field.class);
            this.staticFieldOffset = this.unsafeClass.getMethod("staticFieldOffset", Field.class);
            this.objectFieldOffset = this.unsafeClass.getMethod("objectFieldOffset", Field.class);
            this.putObject = this.unsafeClass.getMethod("putObject", Object.class, long.class, Object.class);
        } catch (NoSuchFieldException | IllegalAccessException | ClassNotFoundException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public Object getObject(Object o, long i) {
        try {
            return this.getObject.invoke(this.unsafeInstance, o, i);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public Object staticFieldBase(Field f) {
        try {
            return this.staticFieldBase.invoke(this.unsafeInstance, f);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public long staticFieldOffset(Field f) {
        try {
            return (long) this.staticFieldOffset.invoke(this.unsafeInstance, f);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public long objectFieldOffset(Field f) {
        try {
            return (long) this.objectFieldOffset.invoke(this.unsafeInstance, f);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public void putObject(Object o, long i, Object o2) {
        try {
            this.putObject.invoke(this.unsafeInstance, o, i, o2);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
