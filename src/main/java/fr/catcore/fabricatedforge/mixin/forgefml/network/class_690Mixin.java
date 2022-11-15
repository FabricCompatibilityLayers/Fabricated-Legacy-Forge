package fr.catcore.fabricatedforge.mixin.forgefml.network;

import cpw.mods.fml.common.network.FMLNetworkHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.class_690;
import net.minecraft.world.GameMode;
import net.minecraft.world.level.LevelGeneratorType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

@Mixin(class_690.class)
public abstract class class_690Mixin extends Packet {
    @Shadow public int entityId;
    @Shadow public LevelGeneratorType levelType;
    @Shadow public boolean hardcore;
    @Shadow public GameMode gameMode;
    @Shadow public int dimension;
    @Shadow public byte difficulty;
    @Shadow public byte worldHeight;
    @Shadow public byte maxPlayers;

    private boolean vanillaCompatible;

    @Inject(method = "<init>()V", at = @At("RETURN"))
    private void fmlCtr(CallbackInfo ci) {
        this.vanillaCompatible = FMLNetworkHandler.vanillaLoginPacketCompatibility();
    }

    @Inject(method = "<init>(ILnet/minecraft/world/level/LevelGeneratorType;Lnet/minecraft/world/GameMode;ZIIII)V", at = @At("RETURN"))
    private void fmlCtr(int levelType, LevelGeneratorType gameMode, GameMode handcore, boolean dimension, int difficulty, int worldHeight, int maxPlayers, int par8, CallbackInfo ci) {
        this.vanillaCompatible = false;
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public void readFrom(DataInputStream par1DataInputStream) {
        try {
            this.entityId = par1DataInputStream.readInt();
            String var2 = getString(par1DataInputStream, 16);
            this.levelType = LevelGeneratorType.getTypeFromName(var2);
            if (this.levelType == null) {
                this.levelType = LevelGeneratorType.DEFAULT;
            }

            byte var3 = par1DataInputStream.readByte();
            this.hardcore = (var3 & 8) == 8;
            int var4 = var3 & -9;
            this.gameMode = GameMode.setGameModeWithId(var4);
            if (this.vanillaCompatible) {
                this.dimension = par1DataInputStream.readByte();
            } else {
                this.dimension = par1DataInputStream.readInt();
            }

            this.difficulty = par1DataInputStream.readByte();
            this.worldHeight = par1DataInputStream.readByte();
            this.maxPlayers = par1DataInputStream.readByte();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public void writeTo(DataOutputStream par1DataOutputStream) {
        try {
            par1DataOutputStream.writeInt(this.entityId);
            putString(this.levelType == null ? "" : this.levelType.getName(), par1DataOutputStream);
            int var2 = this.gameMode.getGameModeId();
            if (this.hardcore) {
                var2 |= 8;
            }

            par1DataOutputStream.writeByte(var2);
            if (this.vanillaCompatible) {
                par1DataOutputStream.writeByte(this.dimension);
            } else {
                par1DataOutputStream.writeInt(this.dimension);
            }

            par1DataOutputStream.writeByte(this.difficulty);
            par1DataOutputStream.writeByte(this.worldHeight);
            par1DataOutputStream.writeByte(this.maxPlayers);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public int getSize() {
        int var1 = 0;
        if (this.levelType != null) {
            var1 = this.levelType.getName().length();
        }

        return 6 + 2 * var1 + 4 + 4 + 1 + 1 + 1 + (this.vanillaCompatible ? 0 : 3);
    }
}
