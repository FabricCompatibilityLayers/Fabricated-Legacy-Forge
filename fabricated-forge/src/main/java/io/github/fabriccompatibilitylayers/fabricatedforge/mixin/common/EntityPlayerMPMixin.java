package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.EntityExtension;
import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.WorldProviderExtension;
import net.minecraft.src.*;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(EntityPlayerMP.class)
public abstract class EntityPlayerMPMixin extends EntityPlayer implements ICrafting, EntityExtension {
    public EntityPlayerMPMixin(World par1World) {
        super(par1World);
    }

    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/World;getSpawnPoint()Lnet/minecraft/src/ChunkCoordinates;"))
    private ChunkCoordinates forge$getRandomizedSpawnPoint(World instance) {
        return ((WorldProviderExtension) instance.provider).getRandomizedSpawnPoint();
    }

    @Redirect(method = "<init>", at = @At(value = "FIELD", target = "Lnet/minecraft/src/WorldProvider;hasNoSky:Z"))
    private boolean forge$cancelIf(WorldProvider instance) {
        return true;
    }

    @WrapOperation(method = "onUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/WorldServer;getAllTileEntityInBox(IIIIII)Ljava/util/List;"))
    private List forge$bugfix(WorldServer instance, int par2, int par3, int par4, int par5, int par6, int i, Operation<List> original) {
        //BugFix: 16 makes it load an extra chunk, which isn't associated with a player, which makes it not unload unless a player walks near it.
        //To_Do: Find a way to efficiently clean abandoned chunks.
        return original.call(instance, par2, par3, par4, par5 - 1, par6, i - 1);
    }

    @Inject(method = "onDeath", at = @At("HEAD"), cancellable = true)
    private void forge$onLivingDeath(DamageSource par1DamageSource, CallbackInfo ci) {
        if (ForgeHooks.onLivingDeath(this, par1DamageSource))
        {
            ci.cancel();
        }
    }

    @Inject(method = "onDeath", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/InventoryPlayer;dropAllItems()V"))
    private void forge$captureDrops$setup(DamageSource par1, CallbackInfo ci) {
        this.setCaptureDrops(true);
        this.getCapturedDrops().clear();
    }

    @Inject(method = "onDeath", at = @At("RETURN"))
    private void forge$captureDrops$collect(DamageSource par1DamageSource, CallbackInfo ci) {
        this.setCaptureDrops(false);
        PlayerDropsEvent event = new PlayerDropsEvent(this, par1DamageSource, this.getCapturedDrops(), recentlyHit > 0);
        if (!MinecraftForge.EVENT_BUS.post(event))
        {
            for (EntityItem item : this.getCapturedDrops())
            {
                joinEntityItemWithWorld(item);
            }
        }
    }
}
