package fr.catcore.fabricatedforge.mixin.forgefml.item;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterial;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(ToolMaterial.class)
public class ToolMaterialMixin {
    public Item customCraftingMaterial = null;

    /**
     * @author forge
     * @reason custom crafting material
     */
    @Overwrite
    public int method_4617() {
        switch((ToolMaterial)(Object) this) {
            case WOOD:
                return Block.PLANKS.id;
            case STONE:
                return Block.STONE_BRICKS.id;
            case GOLD:
                return Item.GOLD_INGOT.id;
            case IRON:
                return Item.IRON_INGOT.id;
            case DIAMOND:
                return Item.DIAMOND.id;
            default:
                return this.customCraftingMaterial == null ? 0 : this.customCraftingMaterial.id;
        }
    }
}
