package fr.catcore.fabricatedmodloader.mixin.modloader.common;

import modloader.ModLoader;
import modloader.TradeEntry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.util.Pair;
import net.minecraft.village.TraderOfferList;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Mixin(VillagerEntity.class)
public abstract class VillagerEntityMixin extends Entity {

    @Shadow public abstract int profession();

    @Shadow
    private static void method_3107(TraderOfferList traderOfferList, int i, Random random, float f) {
    }

    @Shadow
    private static void method_3110(TraderOfferList traderOfferList, int i, Random random, float f) {
    }

    @Shadow @Final private static Map field_3947;

    public VillagerEntityMixin(World world) {
        super(world);
    }

    @Inject(method = "method_3111",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/village/TraderOfferList;isEmpty()Z", shift = At.Shift.BEFORE),
            locals = LocalCapture.CAPTURE_FAILEXCEPTION
    )
    private void modLoaderAddTrades(int par1, CallbackInfo ci, TraderOfferList var2) {
        this.addModTrades(var2);
    }

    private void addModTrades(TraderOfferList merchantrecipelist) {
        List list = ModLoader.getTrades(this.profession());
        if (list != null) {
            Iterator i$ = list.iterator();

            while(i$.hasNext()) {
                TradeEntry entry = (TradeEntry)i$.next();
                if (entry.buying) {
                    method_3107(merchantrecipelist, entry.id, this.random, entry.chance);
                } else {
                    method_3110(merchantrecipelist, entry.id, this.random, entry.chance);
                }
            }

        }
    }

    @Inject(method = "<clinit>", at = @At("RETURN"))
    private static void modLoaderAddTrades(CallbackInfo ci) {
        List list = ModLoader.getTrades(-1);
        Iterator i$ = list.iterator();

        while(i$.hasNext()) {
            TradeEntry entry = (TradeEntry)i$.next();
            if (entry.buying) {
                if (entry.min > 0 && entry.max > 0) {
                    field_3947.put(entry.id, new Pair(entry.min, entry.max));
                }
            } else if (entry.min > 0 && entry.max > 0) {
                field_3947.put(entry.id, new Pair(entry.min, entry.max));
            }
        }
    }
}
