package fr.catcore.fabricatedmodloader.utils;

import java.util.ArrayList;

public class BArrayList<T> extends ArrayList<T> {

    public BArrayList<T> put(T entry) {
        this.add(entry);
        return this;
    }
}
