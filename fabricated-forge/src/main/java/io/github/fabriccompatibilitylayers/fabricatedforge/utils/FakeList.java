/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.utils;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public abstract class FakeList implements List {
    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public @NotNull Iterator iterator() {
        return null;
    }

    @Override
    public @NotNull Object[] toArray() {
        return new Object[0];
    }

    @Override
    public boolean add(Object o) {
        return false;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean addAll(@NotNull Collection c) {
        return false;
    }

    @Override
    public boolean addAll(int index, @NotNull Collection c) {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public Object set(int index, Object element) {
        return null;
    }

    @Override
    public void add(int index, Object element) {

    }

    @Override
    public Object remove(int index) {
        return null;
    }

    @Override
    public int indexOf(Object o) {
        return 0;
    }

    @Override
    public int lastIndexOf(Object o) {
        return 0;
    }

    @Override
    public @NotNull ListIterator listIterator() {
        return null;
    }

    @Override
    public @NotNull ListIterator listIterator(int index) {
        return null;
    }

    @Override
    public @NotNull List subList(int fromIndex, int toIndex) {
        return List.of();
    }

    @Override
    public boolean retainAll(@NotNull Collection c) {
        return false;
    }

    @Override
    public boolean removeAll(@NotNull Collection c) {
        return false;
    }

    @Override
    public boolean containsAll(@NotNull Collection c) {
        return false;
    }

    @Override
    public @NotNull Object[] toArray(@NotNull Object[] a) {
        return new Object[0];
    }
}
