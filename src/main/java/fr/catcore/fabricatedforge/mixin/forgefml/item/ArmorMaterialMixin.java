package fr.catcore.fabricatedforge.mixin.forgefml.item;

import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(ArmorMaterial.class)
public class ArmorMaterialMixin {
    public Item customCraftingMaterial = null;

    /**
     * @author forge
     * @reason custom crafting material
     */
    @Overwrite
    public int getRepairMaterial() {
        switch((ArmorMaterial)(Object) this) {
            case CLOTH:
                return Item.LEATHER.id;
            case CHAIN:
                return Item.IRON_INGOT.id;
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
