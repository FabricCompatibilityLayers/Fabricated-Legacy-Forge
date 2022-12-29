package fr.catcore.fabricatedforge.mixin.forgefml.server;

import cpw.mods.fml.common.network.FMLNetworkHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import fr.catcore.fabricatedforge.mixininterface.IPlayerManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.PortalTeleporter;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.Connection;
import net.minecraft.network.Packet;
import net.minecraft.network.class_690;
import net.minecraft.network.class_716;
import net.minecraft.network.packet.s2c.play.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.ServerPacketListener;
import net.minecraft.server.network.DemoServerPlayerInteractionManager;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.dimension.Dimension;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

@Mixin(PlayerManager.class)
public abstract class PlayerManagerMixin implements IPlayerManager {

    @Shadow public abstract void laodPlayer(ServerPlayerEntity player);

    @Shadow @Final private MinecraftServer server;

    @Shadow @Final public static Logger LOGGER;

    @Shadow protected abstract void setGameMode(ServerPlayerEntity player, ServerPlayerEntity oldPlayer, World world);

    @Shadow public abstract void sendWorldInfo(ServerPlayerEntity player, ServerWorld world);

    @Shadow public abstract void sendToAll(Packet packet);

    @Shadow public abstract void sendPlayerList(ServerPlayerEntity player);

    @Shadow public abstract int getMaxPlayerCount();

    @Shadow protected abstract void savePlayerData(ServerPlayerEntity player);

    @Shadow @Final public List players;

    @Shadow public abstract void method_2009(ServerPlayerEntity player);

    @Shadow public abstract void method_1986(ServerPlayerEntity player, ServerWorld world);

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public void onPlayerConnect(Connection par1INetworkManager, ServerPlayerEntity par2EntityPlayerMP) {
        this.laodPlayer(par2EntityPlayerMP);
        par2EntityPlayerMP.setWorld(this.server.getWorld(par2EntityPlayerMP.dimension));
        par2EntityPlayerMP.interactionManager.setWorld((ServerWorld)par2EntityPlayerMP.world);
        String var3 = "local";
        if (par1INetworkManager.getAddress() != null) {
            var3 = par1INetworkManager.getAddress().toString();
        }

        LOGGER.info(
                par2EntityPlayerMP.username
                        + "["
                        + var3
                        + "] logged in with entity id "
                        + par2EntityPlayerMP.id
                        + " at ("
                        + par2EntityPlayerMP.x
                        + ", "
                        + par2EntityPlayerMP.y
                        + ", "
                        + par2EntityPlayerMP.z
                        + ")"
        );
        ServerWorld var4 = this.server.getWorld(par2EntityPlayerMP.dimension);
        BlockPos var5 = var4.getWorldSpawnPos();
        this.setGameMode(par2EntityPlayerMP, (ServerPlayerEntity)null, var4);
        ServerPacketListener var6 = new ServerPacketListener(this.server, par1INetworkManager, par2EntityPlayerMP);
        var6.sendPacket(
                new class_690(
                        par2EntityPlayerMP.id,
                        var4.getLevelProperties().getGeneratorType(),
                        par2EntityPlayerMP.interactionManager.getGameMode(),
                        var4.getLevelProperties().isHardcore(),
                        var4.dimension.dimensionType,
                        var4.difficulty,
                        var4.getMaxBuildHeight(),
                        this.getMaxPlayerCount()
                )
        );
        var6.sendPacket(new PlayerSpawnPositionChangeS2CPacket(var5.x, var5.y, var5.z));
        var6.sendPacket(new PlayerAbilitiesS2CPacket(par2EntityPlayerMP.abilities));
        this.sendWorldInfo(par2EntityPlayerMP, var4);
        this.sendToAll(new ChatMessageS2CPacket("§e" + par2EntityPlayerMP.username + " joined the game."));
        this.sendPlayerList(par2EntityPlayerMP);
        var6.requestTeleport(par2EntityPlayerMP.x, par2EntityPlayerMP.y, par2EntityPlayerMP.z, par2EntityPlayerMP.yaw, par2EntityPlayerMP.pitch);
        this.server.method_3005().addListener(var6);
        var6.sendPacket(new WorldTimeUpdateS2CPacket(var4.getLastUpdateTime(), var4.getTimeOfDay()));
        if (this.server.getResourcePackUrl().length() > 0) {
            par2EntityPlayerMP.method_2151(this.server.getResourcePackUrl(), this.server.method_2982());
        }

        for(StatusEffectInstance var8 : (Collection<StatusEffectInstance>)par2EntityPlayerMP.method_2644()) {
            var6.sendPacket(new EntityStatusEffectS2CPacket(par2EntityPlayerMP.id, var8));
        }

        par2EntityPlayerMP.listenToScreenHandler();
        FMLNetworkHandler.handlePlayerLogin(par2EntityPlayerMP, var6, par1INetworkManager);
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public void remove(ServerPlayerEntity par1EntityPlayerMP) {
        GameRegistry.onPlayerLogout(par1EntityPlayerMP);
        this.savePlayerData(par1EntityPlayerMP);
        ServerWorld var2 = par1EntityPlayerMP.getServerWorld();
        var2.removeEntity(par1EntityPlayerMP);
        var2.getPlayerWorldManager().method_2115(par1EntityPlayerMP);
        this.players.remove(par1EntityPlayerMP);
        this.sendToAll(new PlayerListS2CPacket(par1EntityPlayerMP.username, false, 9999));
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public ServerPlayerEntity respawnPlayer(ServerPlayerEntity par1EntityPlayerMP, int par2, boolean par3) {
        World world = this.server.getWorld(par2);
        if (world == null || !world.dimension.containsWorldSpawn()) {
            par2 = 0;
        }

        par1EntityPlayerMP.getServerWorld().getEntityTracker().method_2096(par1EntityPlayerMP);
        par1EntityPlayerMP.getServerWorld().getEntityTracker().method_2101(par1EntityPlayerMP);
        par1EntityPlayerMP.getServerWorld().getPlayerWorldManager().method_2115(par1EntityPlayerMP);
        this.players.remove(par1EntityPlayerMP);
        this.server.getWorld(par1EntityPlayerMP.dimension).method_3700(par1EntityPlayerMP);
        BlockPos var4 = par1EntityPlayerMP.method_3186();
        boolean var5 = par1EntityPlayerMP.isSpawnForced();
        par1EntityPlayerMP.dimension = par2;
        Object var6;
        if (this.server.isDemo()) {
            var6 = new DemoServerPlayerInteractionManager(this.server.getWorld(par1EntityPlayerMP.dimension));
        } else {
            var6 = new ServerPlayerInteractionManager(this.server.getWorld(par1EntityPlayerMP.dimension));
        }

        ServerPlayerEntity var7 = new ServerPlayerEntity(
                this.server, this.server.getWorld(par1EntityPlayerMP.dimension), par1EntityPlayerMP.username, (ServerPlayerInteractionManager)var6
        );
        var7.field_2823 = par1EntityPlayerMP.field_2823;
        var7.copyFrom(par1EntityPlayerMP, par3);
        var7.dimension = par2;
        var7.id = par1EntityPlayerMP.id;
        ServerWorld var8 = this.server.getWorld(par1EntityPlayerMP.dimension);
        this.setGameMode(var7, par1EntityPlayerMP, var8);
        if (var4 != null) {
            BlockPos var9 = PlayerEntity.method_3169(this.server.getWorld(par1EntityPlayerMP.dimension), var4, var5);
            if (var9 != null) {
                var7.refreshPositionAndAngles((double)((float)var9.x + 0.5F), (double)((float)var9.y + 0.1F), (double)((float)var9.z + 0.5F), 0.0F, 0.0F);
                var7.method_4573(var4, var5);
            } else {
                var7.field_2823.sendPacket(new GameStateChangeS2CPacket(0, 0));
            }
        }

        var8.chunkCache.getOrGenerateChunk((int)var7.x >> 4, (int)var7.z >> 4);

        while(!var8.doesBoxCollide(var7, var7.boundingBox).isEmpty()) {
            var7.updatePosition(var7.x, var7.y + 1.0, var7.z);
        }

        var7.field_2823
                .sendPacket(
                        new PlayerRespawnS2CPacket(
                                var7.dimension,
                                (byte)var7.world.difficulty,
                                var7.world.getLevelProperties().getGeneratorType(),
                                var7.world.getMaxBuildHeight(),
                                var7.interactionManager.getGameMode()
                        )
                );
        BlockPos var9 = var8.getWorldSpawnPos();
        var7.field_2823.requestTeleport(var7.x, var7.y, var7.z, var7.yaw, var7.pitch);
        var7.field_2823.sendPacket(new PlayerSpawnPositionChangeS2CPacket(var9.x, var9.y, var9.z));
        var7.field_2823.sendPacket(new class_716(var7.experienceProgress, var7.totalExperience, var7.experienceLevel));
        this.sendWorldInfo(var7, var8);
        var8.getPlayerWorldManager().method_2109(var7);
        var8.spawnEntity(var7);
        this.players.add(var7);
        var7.listenToScreenHandler();
        GameRegistry.onPlayerRespawn(var7);
        return var7;
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public void teleportToDimension(ServerPlayerEntity par1EntityPlayerMP, int par2) {
        this.transferPlayerToDimension(par1EntityPlayerMP, par2, new PortalTeleporter());
    }

    @Override
    public void transferPlayerToDimension(ServerPlayerEntity par1EntityPlayerMP, int par2, PortalTeleporter teleporter) {
        int var3 = par1EntityPlayerMP.dimension;
        ServerWorld var4 = this.server.getWorld(par1EntityPlayerMP.dimension);
        par1EntityPlayerMP.dimension = par2;
        ServerWorld var5 = this.server.getWorld(par1EntityPlayerMP.dimension);
        par1EntityPlayerMP.field_2823
                .sendPacket(
                        new PlayerRespawnS2CPacket(
                                par1EntityPlayerMP.dimension,
                                (byte)par1EntityPlayerMP.world.difficulty,
                                var5.getLevelProperties().getGeneratorType(),
                                var5.getMaxBuildHeight(),
                                par1EntityPlayerMP.interactionManager.getGameMode()
                        )
                );
        var4.method_3700(par1EntityPlayerMP);
        par1EntityPlayerMP.removed = false;
        this.transferEntityToWorld(par1EntityPlayerMP, var3, var4, var5, teleporter);
        this.method_1986(par1EntityPlayerMP, var4);
        par1EntityPlayerMP.field_2823
                .requestTeleport(par1EntityPlayerMP.x, par1EntityPlayerMP.y, par1EntityPlayerMP.z, par1EntityPlayerMP.yaw, par1EntityPlayerMP.pitch);
        par1EntityPlayerMP.interactionManager.setWorld(var5);
        this.sendWorldInfo(par1EntityPlayerMP, var5);
        this.method_2009(par1EntityPlayerMP);

        for(StatusEffectInstance var7 : (Collection<StatusEffectInstance>)par1EntityPlayerMP.method_2644()) {
            par1EntityPlayerMP.field_2823.sendPacket(new EntityStatusEffectS2CPacket(par1EntityPlayerMP.id, var7));
        }

        GameRegistry.onPlayerChangedDimension(par1EntityPlayerMP);
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public void method_4399(Entity par1Entity, int par2, ServerWorld par3WorldServer, ServerWorld par4WorldServer) {
        this.transferEntityToWorld(par1Entity, par2, par3WorldServer, par4WorldServer, new PortalTeleporter());
    }

    @Override
    public void transferEntityToWorld(Entity par1Entity, int par2, ServerWorld par3WorldServer, ServerWorld par4WorldServer, PortalTeleporter teleporter) {
        Dimension pOld = par3WorldServer.dimension;
        Dimension pNew = par4WorldServer.dimension;
        double moveFactor = pOld.getMovementFactor() / pNew.getMovementFactor();
        double var5 = par1Entity.x * moveFactor;
        double var7 = par1Entity.z * moveFactor;
        double var11 = par1Entity.x;
        double var13 = par1Entity.y;
        double var15 = par1Entity.z;
        float var17 = par1Entity.yaw;
        if (par1Entity.dimension == 1) {
            BlockPos var18;
            if (par2 == 1) {
                var18 = par4WorldServer.getWorldSpawnPos();
            } else {
                var18 = par4WorldServer.getWorldSpawn();
            }

            var5 = (double)var18.x;
            par1Entity.y = (double)var18.y;
            var7 = (double)var18.z;
            par1Entity.refreshPositionAndAngles(var5, par1Entity.y, var7, 90.0F, 0.0F);
            if (par1Entity.isAlive()) {
                par3WorldServer.checkChunk(par1Entity, false);
            }
        }

        if (par2 != 1) {
            var5 = (double)MathHelper.clamp((int)var5, -29999872, 29999872);
            var7 = (double)MathHelper.clamp((int)var7, -29999872, 29999872);
            if (par1Entity.isAlive()) {
                par4WorldServer.spawnEntity(par1Entity);
                par1Entity.refreshPositionAndAngles(var5, par1Entity.y, var7, par1Entity.yaw, par1Entity.pitch);
                par4WorldServer.checkChunk(par1Entity, false);
                teleporter.method_4699(par4WorldServer, par1Entity, var11, var13, var15, var17);
            }
        }

        par1Entity.setWorld(par4WorldServer);
    }
}
