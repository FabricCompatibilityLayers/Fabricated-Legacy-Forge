package fr.catcore.fabricatedmodloader.mixin.modloader.client;

import com.google.common.collect.Lists;
import modloader.ModLoader;
import net.minecraft.block.Block;
import net.minecraft.client.class_535;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(class_535.class)
public abstract class class_535Mixin {

    @Shadow
    public WorldView field_2017;

    private static final List<Integer> RENDER_BLOCKS = Lists.newArrayList(
            0, 31, 4, 13, 1, 19, 23, 6,
            2, 3, 5, 8, 7, 9, 10, 27, 11, 12, 29,
            30, 14, 15, 16, 17, 18, 20, 21, 24, 25,
            26, 28
    );

    @Inject(method = "method_1458", at = @At("RETURN"), cancellable = true)
    private void modLoaderRenderWorldBlock(Block block, int i, int j, int k, CallbackInfoReturnable<Boolean> cir) {
        int type = block.getBlockType();
        if (!cir.getReturnValue() && !RENDER_BLOCKS.contains(type)) {
            cir.setReturnValue(ModLoader.renderWorldBlock((class_535) (Object) this, this.field_2017, i, j, k, block, type));
        }
    }

    private static final List<Integer> RENDER_BLOCKS_INV = Lists.newArrayList(
            1, 19, 23, 13, 22, 6, 2, 10, 27, 11, 21
    );

    @Inject(method = "method_1447",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;getBlockType()I", ordinal = 0),
            cancellable = true
    )
    private void modLoaderRenderInvBlock(Block block, int i, float f, CallbackInfo ci) {
        int var6 = block.getBlockType();
        if (var6 != 0 && var6 != 31 && var6 != 16 && var6 != 26) {
            if (!RENDER_BLOCKS_INV.contains(var6)) {
                ModLoader.renderInvBlock((class_535) (Object) this, block, i, var6);
                ci.cancel();
            }
        }
    }

    @Inject(method = "method_1455", at = @At("RETURN"), cancellable = true)
    private static void modLoaderRenderBlockIsItemFull3D(int i, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValueZ()) {
            cir.setReturnValue(ModLoader.renderBlockIsItemFull3D(i));
        }
    }
}
