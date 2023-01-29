package fr.catcore.fabricatedforge.mixin.forgefml.entity.player;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.packet.s2c.play.ChatMessageS2CPacket;
import net.minecraft.network.packet.s2c.play.DestroyEntityS2CPacket;
import net.minecraft.network.packet.s2c.play.class_687;
import net.minecraft.screen.ScreenHandlerListener;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerPacketListener;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.event.world.ChunkWatchEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity implements ScreenHandlerListener {
    @Shadow public MinecraftServer server;

    @Shadow public ServerPlayerInteractionManager interactionManager;

    @Shadow private int spawnProtectionTicks;

    @Shadow @Final public List<ChunkPos> loadedChunks;

    @Shadow public ServerPacketListener field_2823;

    @Shadow protected abstract void updateBlockEntity(BlockEntity blockEntity);

    @Shadow @Final public List<Integer> removedEntities;

    @Shadow public abstract ServerWorld getServerWorld();

    public ServerPlayerEntityMixin(World world) {
        super(world);
    }

    @Inject(method = "<init>", at = @At(value = "RETURN"))
    private void fmlCtr(MinecraftServer par1MinecraftServer, World par2World, String par3Str, ServerPlayerInteractionManager par4ItemInWorldManager, CallbackInfo ci) {
        BlockPos var5 = par2World.dimension.getRandomizedSpawnPoint();
        int var6 = var5.x;
        int var7 = var5.z;
        int var8 = var5.y;
        this.refreshPositionAndAngles((double)var6 + 0.5, (double)var8, (double)var7 + 0.5, 0.0F, 0.0F);
        this.server = par1MinecraftServer;
        this.stepHeight = 0.0F;
        this.username = par3Str;
        this.heightOffset = 0.0F;
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public void tick() {
        this.interactionManager.tick();
        --this.spawnProtectionTicks;
        this.openScreenHandler.sendContentUpdates();

        while(!this.removedEntities.isEmpty()) {
            int var1 = Math.min(this.removedEntities.size(), 127);
            int[] var2 = new int[var1];
            Iterator<Integer> var3 = this.removedEntities.iterator();
            int var4 = 0;

            while(var3.hasNext() && var4 < var1) {
                var2[var4++] = var3.next();
                var3.remove();
            }

            this.field_2823.sendPacket(new DestroyEntityS2CPacket(var2));
        }

        if (!this.loadedChunks.isEmpty()) {
            ArrayList<Chunk> var6 = new ArrayList();
            Iterator var7 = this.loadedChunks.iterator();
            ArrayList<BlockEntity> var8 = new ArrayList();

            while(var7.hasNext() && var6.size() < 5) {
                ChunkPos var9 = (ChunkPos)var7.next();
                var7.remove();
                if (var9 != null && this.world.isPosLoaded(var9.x << 4, 0, var9.z << 4)) {
                    var6.add(this.world.getChunk(var9.x, var9.z));
                    var8.addAll(((ServerWorld)this.world).method_2134(var9.x * 16, 0, var9.z * 16, var9.x * 16 + 15, 256, var9.z * 16 + 15));
                }
            }

            if (!var6.isEmpty()) {
                this.field_2823.sendPacket(new class_687(var6));

                for(BlockEntity var5 : var8) {
                    this.updateBlockEntity(var5);
                }

                for(Chunk var10 : var6) {
                    this.getServerWorld().getEntityTracker().method_4410((ServerPlayerEntity)(Object) this, var10);
                    MinecraftForge.EVENT_BUS.post(new ChunkWatchEvent.Watch(var10.getChunkPos(), (ServerPlayerEntity)(Object) this));
                }
            }
        }
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public void dropInventory(DamageSource par1DamageSource) {
        if (!ForgeHooks.onLivingDeath(this, par1DamageSource)) {
            this.server.getPlayerManager().sendToAll(new ChatMessageS2CPacket(par1DamageSource.method_2420(this)));
            if (!this.world.getGameRules().getBoolean("keepInventory")) {
                this.captureDrops(true);
                this.getCapturedDrops().clear();
                this.inventory.dropAll();
                this.captureDrops(false);
                PlayerDropsEvent event = new PlayerDropsEvent(this, par1DamageSource, this.getCapturedDrops(), this.field_3332 > 0);
                if (!MinecraftForge.EVENT_BUS.post(event)) {
                    for(ItemEntity item : this.getCapturedDrops()) {
                        this.spawnItemEntity(item);
                    }
                }
            }
        }
    }
}
