package fr.catcore.fabricatedforge.mixin.forgefml.server;

import cpw.mods.fml.common.network.FMLNetworkHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import fr.catcore.fabricatedforge.mixininterface.IPlayerManager;
import net.minecraft.entity.PortalTeleporter;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.Connection;
import net.minecraft.network.Packet;
import net.minecraft.network.class_690;
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
import net.minecraftforge.common.DimensionManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

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

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public void onPlayerConnect(Connection par1NetworkManager, ServerPlayerEntity par2EntityPlayerMP) {
        this.laodPlayer(par2EntityPlayerMP);
        par2EntityPlayerMP.setWorld(this.server.getWorld(par2EntityPlayerMP.dimension));
        par2EntityPlayerMP.interactionManager.setWorld((ServerWorld)par2EntityPlayerMP.world);
        String var3 = "local";
        if (par1NetworkManager.getAddress() != null) {
            var3 = par1NetworkManager.getAddress().toString();
        }

        LOGGER.info(par2EntityPlayerMP.username + "[" + var3 + "] logged in with entity id " + par2EntityPlayerMP.id + " at (" + par2EntityPlayerMP.x + ", " + par2EntityPlayerMP.y + ", " + par2EntityPlayerMP.z + ")");
        ServerWorld var4 = this.server.getWorld(par2EntityPlayerMP.dimension);
        BlockPos var5 = var4.getWorldSpawnPos();
        this.setGameMode(par2EntityPlayerMP, (ServerPlayerEntity)null, var4);
        ServerPacketListener var6 = new ServerPacketListener(this.server, par1NetworkManager, par2EntityPlayerMP);
        var6.sendPacket(new class_690(par2EntityPlayerMP.id, var4.getLevelProperties().getGeneratorType(), par2EntityPlayerMP.interactionManager.getGameMode(), var4.getLevelProperties().isHardcore(), var4.dimension.dimensionType, var4.difficulty, var4.getMaxBuildHeight(), this.getMaxPlayerCount()));
        var6.sendPacket(new PlayerSpawnPositionChangeS2CPacket(var5.x, var5.y, var5.z));
        var6.sendPacket(new PlayerAbilitiesS2CPacket(par2EntityPlayerMP.abilities));
        this.sendWorldInfo(par2EntityPlayerMP, var4);
        this.sendToAll(new ChatMessageS2CPacket("§e" + par2EntityPlayerMP.username + " joined the game."));
        this.sendPlayerList(par2EntityPlayerMP);
        var6.requestTeleport(par2EntityPlayerMP.x, par2EntityPlayerMP.y, par2EntityPlayerMP.z, par2EntityPlayerMP.yaw, par2EntityPlayerMP.pitch);
        this.server.method_3005().addListener(var6);
        var6.sendPacket(new WorldTimeUpdateS2CPacket(var4.getTimeOfDay()));
        if (this.server.getResourcePackUrl().length() > 0) {
            par2EntityPlayerMP.method_2151(this.server.getResourcePackUrl(), this.server.method_2982());
        }

        for (Object o : par2EntityPlayerMP.method_2644()) {
            StatusEffectInstance var8 = (StatusEffectInstance) o;
            var6.sendPacket(new EntityStatusEffectS2CPacket(par2EntityPlayerMP.id, var8));
        }

        par2EntityPlayerMP.listenToScreenHandler();
        FMLNetworkHandler.handlePlayerLogin(par2EntityPlayerMP, var6, par1NetworkManager);
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public void method_1986(ServerPlayerEntity par1EntityPlayerMP, ServerWorld par2WorldServer) {
        ServerWorld var3 = par1EntityPlayerMP.getServerWorld();
        if (par2WorldServer != null) {
            par2WorldServer.getPlayerWorldManager().method_2115(par1EntityPlayerMP);
        }

        var3.getPlayerWorldManager().method_2109(par1EntityPlayerMP);
        var3.chunkCache.getOrGenerateChunk((int)par1EntityPlayerMP.x >> 4, (int)par1EntityPlayerMP.z >> 4);
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
        par1EntityPlayerMP.dimension = par2;
        Object var5;
        if (this.server.isDemo()) {
            var5 = new DemoServerPlayerInteractionManager(this.server.getWorld(par1EntityPlayerMP.dimension));
        } else {
            var5 = new ServerPlayerInteractionManager(this.server.getWorld(par1EntityPlayerMP.dimension));
        }

        ServerPlayerEntity var6 = new ServerPlayerEntity(this.server, this.server.getWorld(par1EntityPlayerMP.dimension), par1EntityPlayerMP.username, (ServerPlayerInteractionManager)var5);
        var6.copyFrom(par1EntityPlayerMP, par3);
        var6.dimension = par2;
        var6.id = par1EntityPlayerMP.id;
        var6.field_2823 = par1EntityPlayerMP.field_2823;
        ServerWorld var7 = this.server.getWorld(par1EntityPlayerMP.dimension);
        this.setGameMode(var6, par1EntityPlayerMP, var7);
        BlockPos var8;
        if (var4 != null) {
            var8 = PlayerEntity.method_3169(this.server.getWorld(par1EntityPlayerMP.dimension), var4);
            if (var8 != null) {
                var6.refreshPositionAndAngles((double)((float)var8.x + 0.5F), (double)((float)var8.y + 0.1F), (double)((float)var8.z + 0.5F), 0.0F, 0.0F);
                var6.method_3161(var4);
            } else {
                var6.field_2823.sendPacket(new GameStateChangeS2CPacket(0, 0));
            }
        }

        var7.chunkCache.getOrGenerateChunk((int)var6.x >> 4, (int)var6.z >> 4);

        while(!var7.doesBoxCollide(var6, var6.boundingBox).isEmpty()) {
            var6.updatePosition(var6.x, var6.y + 1.0, var6.z);
        }

        var6.field_2823.sendPacket(new PlayerRespawnS2CPacket(var6.dimension, (byte)var6.world.difficulty, var6.world.getLevelProperties().getGeneratorType(), var6.world.getMaxBuildHeight(), var6.interactionManager.getGameMode()));
        var8 = var7.getWorldSpawnPos();
        var6.field_2823.requestTeleport(var6.x, var6.y, var6.z, var6.yaw, var6.pitch);
        var6.field_2823.sendPacket(new PlayerSpawnPositionChangeS2CPacket(var8.x, var8.y, var8.z));
        this.sendWorldInfo(var6, var7);
        var7.getPlayerWorldManager().method_2109(var6);
        var7.spawnEntity(var6);
        this.players.add(var6);
        var6.listenToScreenHandler();
        GameRegistry.onPlayerRespawn(var6);
        return var6;
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
        par1EntityPlayerMP.field_2823.sendPacket(new PlayerRespawnS2CPacket(par1EntityPlayerMP.dimension, (byte)par1EntityPlayerMP.world.difficulty, var5.getLevelProperties().getGeneratorType(), var5.getMaxBuildHeight(), par1EntityPlayerMP.interactionManager.getGameMode()));
        var4.method_3700(par1EntityPlayerMP);
        par1EntityPlayerMP.removed = false;
        Dimension pOld = DimensionManager.getProvider(var3);
        Dimension pNew = DimensionManager.getProvider(par2);
        double moveFactor = pOld.getMovementFactor() / pNew.getMovementFactor();
        double var6 = par1EntityPlayerMP.x * moveFactor;
        double var8 = par1EntityPlayerMP.z * moveFactor;
        if (par1EntityPlayerMP.dimension == 1) {
            BlockPos var12 = var5.getWorldSpawn();
            var6 = (double)var12.x;
            par1EntityPlayerMP.y = (double)var12.y;
            var8 = (double)var12.z;
            par1EntityPlayerMP.refreshPositionAndAngles(var6, par1EntityPlayerMP.y, var8, 90.0F, 0.0F);
            if (par1EntityPlayerMP.isAlive()) {
                var4.checkChunk(par1EntityPlayerMP, false);
            }
        }

        if (var3 != 1) {
            var6 = (double) MathHelper.clamp((int)var6, -29999872, 29999872);
            var8 = (double)MathHelper.clamp((int)var8, -29999872, 29999872);
            if (par1EntityPlayerMP.isAlive()) {
                var5.spawnEntity(par1EntityPlayerMP);
                par1EntityPlayerMP.refreshPositionAndAngles(var6, par1EntityPlayerMP.y, var8, par1EntityPlayerMP.yaw, par1EntityPlayerMP.pitch);
                var5.checkChunk(par1EntityPlayerMP, false);
                teleporter.method_3801(var5, par1EntityPlayerMP);
            }
        }

        par1EntityPlayerMP.setWorld(var5);
        this.method_1986(par1EntityPlayerMP, var4);
        par1EntityPlayerMP.field_2823.requestTeleport(par1EntityPlayerMP.x, par1EntityPlayerMP.y, par1EntityPlayerMP.z, par1EntityPlayerMP.yaw, par1EntityPlayerMP.pitch);
        par1EntityPlayerMP.interactionManager.setWorld(var5);
        this.sendWorldInfo(par1EntityPlayerMP, var5);
        this.method_2009(par1EntityPlayerMP);

        for (Object o : par1EntityPlayerMP.method_2644()) {
            StatusEffectInstance var13 = (StatusEffectInstance) o;
            par1EntityPlayerMP.field_2823.sendPacket(new EntityStatusEffectS2CPacket(par1EntityPlayerMP.id, var13));
        }

        GameRegistry.onPlayerChangedDimension(par1EntityPlayerMP);
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public ServerPlayerEntity getPlayer(String par1Str) {
        for (Object player : this.players) {
            ServerPlayerEntity var3 = (ServerPlayerEntity) player;
            if (var3.username.equalsIgnoreCase(par1Str)) {
                return var3;
            }
        }

        return null;
    }
}
