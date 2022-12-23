package fr.catcore.fabricatedforge.mixin.forgefml.server.integrated;

import cpw.mods.fml.common.FMLCommonHandler;
import fr.catcore.fabricatedforge.mixininterface.IMinecraftServer;
import net.minecraft.network.NetworkEncryptionUtils;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.server.world.ServerWorldManager;
import net.minecraft.world.DemoServerWorld;
import net.minecraft.world.MultiServerWorld;
import net.minecraft.world.SaveHandler;
import net.minecraft.world.level.LevelGeneratorType;
import net.minecraft.world.level.LevelInfo;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.io.File;

@Mixin(IntegratedServer.class)
public abstract class IntegratedServerMixin extends MinecraftServer {
    @Shadow @Final private LevelInfo levelInfo;

    public IntegratedServerMixin(File file) {
        super(file);
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public void method_2995(String par1Str, String par2Str, long par3, LevelGeneratorType par5WorldType) {
        this.upgradeWorld(par1Str);
        SaveHandler var6 = this.getSaveStorage().createSaveHandler(par1Str, true);
        ServerWorld overWorld = this.isDemo() ? new DemoServerWorld(this, var6, par2Str, 0, this.profiler) : new ServerWorld(this, var6, par2Str, 0, this.levelInfo, this.profiler);

        for (int dim : DimensionManager.getStaticDimensionIDs()) {
            ServerWorld world = dim == 0 ? overWorld : new MultiServerWorld(this, var6, par2Str, dim, this.levelInfo, overWorld, this.profiler);
            world.addListener(new ServerWorldManager(this, world));
            if (!this.isSinglePlayer()) {
                world.getLevelProperties().getGameMode(this.method_3026());
            }

            MinecraftForge.EVENT_BUS.post(new WorldEvent.Load(world));
        }

        this.getPlayerManager().setMainWorld(new ServerWorld[]{overWorld});
        this.method_3016(this.method_3029());
        this.prepareWorlds();
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public boolean setupServer() {
        field_3848.info("Starting integrated minecraft server version 1.3.2");
        this.setOnlineMode(false);
        this.setSpawnAnimals(true);
        this.setSpawnNpcs(true);
        this.setPvpEnabled(true);
        this.setFlightEnabled(true);
        field_3848.info("Generating keypair");
        this.setKeyPair(NetworkEncryptionUtils.generateServerKeyPair());
        this.method_2995(this.getLevelName(), this.getServerName(), this.levelInfo.getSeed(), this.levelInfo.getGeneratorType());
        this.setMotd(this.getUserName() + " - " + this.worlds[0].getLevelProperties().getLevelName());
        FMLCommonHandler.instance().handleServerStarting(this);
        ((IMinecraftServer)this).setSpawnProtectionSize(0);
        return true;
    }
}
