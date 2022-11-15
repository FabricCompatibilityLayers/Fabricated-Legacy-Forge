package fr.catcore.fabricatedforge.mixininterface;

import java.util.Hashtable;

public interface IMinecraftServer {

    Hashtable<Integer, long[]> getWorldTickTimes();
}
