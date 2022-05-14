package fr.catcore.fabricatedforge.mixin.forgefml.entity.player;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.ChatMessage_S2CPacket;
import net.minecraft.network.packet.s2c.play.DestroyEntityS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityEquipmentUpdate_S2CPacket;
import net.minecraft.network.packet.s2c.play.class_687;
import net.minecraft.screen.ScreenHandlerListener;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.ServerPacketListener;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
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

    @Shadow public abstract ItemStack method_2155(int i);

    @Shadow private ItemStack[] field_2835;

    @Shadow public abstract ServerWorld getServerWorld();

    @Shadow @Final public List loadedChunks;

    @Shadow public ServerPacketListener field_2823;

    @Shadow protected abstract void updateBlockEntity(BlockEntity blockEntity);

    @Shadow @Final public List removedEntities;

    public ServerPlayerEntityMixin(World world) {
        super(world);
    }

    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getWorldSpawnPos()Lnet/minecraft/util/BlockPos;"), cancellable = true)
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

        ci.cancel();
    }

    /**
     * @author Minecraft Forge
     */
    @Overwrite
    public void tick() {
        this.interactionManager.update();
        --this.spawnProtectionTicks;
        this.openScreenHandler.sendContentUpdates();

        int var1;
        for(var1 = 0; var1 < 5; ++var1) {
            ItemStack var2 = this.method_2155(var1);
            if (var2 != this.field_2835[var1]) {
                this.getServerWorld().getEntityTracker().sendToOtherTrackingEntities(this, new EntityEquipmentUpdate_S2CPacket(this.id, var1, var2));
                this.field_2835[var1] = var2;
            }
        }

        Iterator var9;
        if (!this.loadedChunks.isEmpty()) {
            ArrayList var6 = new ArrayList<>();
            var9 = this.loadedChunks.iterator();
            ArrayList var3 = new ArrayList<>();

            while(var9.hasNext() && var6.size() < 5) {
                ChunkPos var4 = (ChunkPos)var9.next();
                var9.remove();
                if (var4 != null && this.world.isPosLoaded(var4.x << 4, 0, var4.z << 4)) {
                    var6.add(this.world.getChunk(var4.x, var4.z));
                    var3.addAll(((ServerWorld)this.world).method_2134(var4.x * 16, 0, var4.z * 16, var4.x * 16 + 15, 256, var4.z * 16 + 15));
                }
            }

            if (!var6.isEmpty()) {
                this.field_2823.sendPacket(new class_687(var6));
                for (Object o : var3) {
                    BlockEntity var5 = (BlockEntity) o;
                    this.updateBlockEntity(var5);
                }
            }
        }

        if (!this.removedEntities.isEmpty()) {
            var1 = Math.min(this.removedEntities.size(), 127);
            int[] var8 = new int[var1];
            var9 = this.removedEntities.iterator();
            int var11 = 0;

            while(var9.hasNext() && var11 < var1) {
                var8[var11++] = (Integer)var9.next();
                var9.remove();
            }

            this.field_2823.sendPacket(new DestroyEntityS2CPacket(var8));
        }

    }

    /**
     * @author Minecraft Forge
     */
    @Overwrite
    public void dropInventory(DamageSource par1DamageSource) {
        if (!ForgeHooks.onLivingDeath(this, par1DamageSource)) {
            this.server.getPlayerManager().sendToAll(new ChatMessage_S2CPacket(par1DamageSource.method_2420(this)));
            this.server.getPlayerManager();
            PlayerManager._LOGGER.info(par1DamageSource.method_2420(this));
            this.captureDrops(true);
            this.getCapturedDrops().clear();
            this.inventory.dropAll();
            this.captureDrops(false);
            PlayerDropsEvent event = new PlayerDropsEvent(this, par1DamageSource, this.getCapturedDrops(), this.field_3332 > 0);
            if (!MinecraftForge.EVENT_BUS.post(event)) {
                for (ItemEntity item : this.getCapturedDrops()) {
                    this.spawnItemEntity(item);
                }
            }
        }
    }
}
