package fr.catcore.fabricatedforge.mixin.forgefml.client.gui.screen.ingame;

import fr.catcore.fabricatedforge.mixininterface.ISlot;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.inventory.slot.Slot;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(HandledScreen.class)
public class HandledScreenMixin extends Screen {
    @Shadow private Slot touchDragSlotStart;

    @Shadow private ItemStack touchDragStack;

    @Shadow private boolean touchIsRightClickDrag;

    @Shadow protected static ItemRenderer field_1346;

    /**
     * @author forge
     * @reason slot background icon texture overwrite.
     */
    @Overwrite
    public final void drawSlot(Slot par1Slot) {
        int var2 = par1Slot.x;
        int var3 = par1Slot.y;
        ItemStack var4 = par1Slot.getStack();
        boolean var5 = par1Slot == this.touchDragSlotStart && this.touchDragStack != null && !this.touchIsRightClickDrag;
        if (par1Slot == this.touchDragSlotStart && this.touchDragStack != null && this.touchIsRightClickDrag && var4 != null) {
            var4 = var4.copy();
            var4.count /= 2;
        }

        this.zOffset = 100.0F;
        field_1346.zOffset = 100.0F;
        if (var4 == null) {
            int var6 = par1Slot.method_3297();
            if (var6 >= 0) {
                GL11.glDisable(2896);
                this.field_1229.textureManager.bindTexture(this.field_1229.textureManager.getTextureFromPath(((ISlot)par1Slot).getBackgroundIconTexture()));
                this.drawTexture(var2, var3, var6 % 16 * 16, var6 / 16 * 16, 16, 16);
                GL11.glEnable(2896);
                var5 = true;
            }
        }

        if (!var5) {
            GL11.glEnable(2929);
            field_1346.method_4336(this.textRenderer, this.field_1229.textureManager, var4, var2, var3);
            field_1346.method_1549(this.textRenderer, this.field_1229.textureManager, var4, var2, var3);
        }

        field_1346.zOffset = 0.0F;
        this.zOffset = 0.0F;
    }
}
