package fr.catcore.fabricatedforge.mixininterface;

import java.util.Hashtable;

public interface IMinecraftServer {
    int getSpawnProtectionSize();

    void setSpawnProtectionSize(int spawnProtectionSize);

    Hashtable<Integer, long[]> getWorldTickTimes();
}
