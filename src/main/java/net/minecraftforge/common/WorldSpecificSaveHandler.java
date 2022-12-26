package net.minecraftforge.common;

import fr.catcore.fabricatedforge.mixininterface.IServerWorld;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.PlayerDataHandler;
import net.minecraft.world.SaveHandler;
import net.minecraft.world.chunk.ChunkStorage;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.level.LevelProperties;
import net.minecraft.world.level.storage.WorldSaveException;

import java.io.File;

public class WorldSpecificSaveHandler implements SaveHandler {
    private ServerWorld world;
    private SaveHandler parent;
    private File dataDir;

    public WorldSpecificSaveHandler(ServerWorld world, SaveHandler parent) {
        this.world = world;
        this.parent = parent;
        this.dataDir = new File(((IServerWorld) world).getChunkSaveLocation(), "data");
        this.dataDir.mkdirs();
    }

    public LevelProperties getLevelProperties() {
        return this.parent.getLevelProperties();
    }

    public void readSessionLock() {
        this.parent.readSessionLock();
    }

    public ChunkStorage getChunkWriter(Dimension var1) {
        return this.parent.getChunkWriter(var1);
    }

    public void saveWorld(LevelProperties var1, NbtCompound var2) {
        this.parent.saveWorld(var1, var2);
    }

    public void saveWorld(LevelProperties var1) {
        this.parent.saveWorld(var1);
    }

    public PlayerDataHandler getInstance() {
        return this.parent.getInstance();
    }

    public void clear() {
        this.parent.clear();
    }

    public String getWorldName() {
        return this.parent.getWorldName();
    }

    public File getDataFile(String name) {
        return new File(this.dataDir, name + ".dat");
    }
}
