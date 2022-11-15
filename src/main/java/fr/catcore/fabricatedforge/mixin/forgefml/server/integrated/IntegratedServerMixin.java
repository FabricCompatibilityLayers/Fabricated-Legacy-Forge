package fr.catcore.fabricatedforge.mixin.forgefml.server.integrated;

import cpw.mods.fml.common.FMLCommonHandler;
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
    protected void setupWorld(String par1Str, String par2Str, long par3, LevelGeneratorType par5WorldType, String par6Str) {
        this.upgradeWorld(par1Str);
        SaveHandler var7 = this.getSaveStorage().createSaveHandler(par1Str, true);
        ServerWorld overWorld = (ServerWorld)(this.isDemo()
                ? new DemoServerWorld(this, var7, par2Str, 0, this.profiler)
                : new ServerWorld(this, var7, par2Str, 0, this.levelInfo, this.profiler));
        Integer[] arr$ = DimensionManager.getStaticDimensionIDs();
        int len$ = arr$.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            int dim = arr$[i$];
            ServerWorld world = (ServerWorld)(dim == 0 ? overWorld : new MultiServerWorld(this, var7, par2Str, dim, this.levelInfo, overWorld, this.profiler));
            world.addListener(new ServerWorldManager(this, world));
            if (!this.isSinglePlayer()) {
                world.getLevelProperties().method_207(this.method_3026());
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
    protected boolean setupServer() {
        field_3848.info("Starting integrated minecraft server version 1.4");
        this.setOnlineMode(false);
        this.setSpawnAnimals(true);
        this.setSpawnNpcs(true);
        this.setPvpEnabled(true);
        this.setFlightEnabled(true);
        field_3848.info("Generating keypair");
        this.setKeyPair(NetworkEncryptionUtils.generateServerKeyPair());
        this.setupWorld(
                this.getLevelName(), this.getServerName(), this.levelInfo.getSeed(), this.levelInfo.getGeneratorType(), this.levelInfo.getGeneratorOptions()
        );
        this.setMotd(this.getUserName() + " - " + this.worlds[0].getLevelProperties().getLevelName());
        FMLCommonHandler.instance().handleServerStarting(this);
        return true;
    }
}
