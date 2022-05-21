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
}
