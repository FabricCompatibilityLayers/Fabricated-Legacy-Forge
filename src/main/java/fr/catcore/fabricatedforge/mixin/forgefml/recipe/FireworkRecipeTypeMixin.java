package fr.catcore.fabricatedforge.mixin.forgefml.recipe;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.recipe.FireworkRecipeType;
import net.minecraft.recipe.RecipeType;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.ArrayList;

@Mixin(FireworkRecipeType.class)
public abstract class FireworkRecipeTypeMixin implements RecipeType {
    @Shadow private ItemStack ingredient;

    /**
     * @author forge
     * @reason idk
     */
    @Overwrite
    public boolean matches(CraftingInventory par1InventoryCrafting, World par2World) {
        this.ingredient = null;
        int var3 = 0;
        int var4 = 0;
        int var5 = 0;
        int var6 = 0;
        int var7 = 0;
        int var8 = 0;

        for(int var9 = 0; var9 < par1InventoryCrafting.getInvSize(); ++var9) {
            ItemStack var10 = par1InventoryCrafting.getInvStack(var9);
            if (var10 != null) {
                if (var10.id == Item.GUNPOWDER.id) {
                    ++var4;
                } else if (var10.id == Item.FIREWORKS_CHARGE.id) {
                    ++var6;
                } else if (var10.id == Item.DYES.id) {
                    ++var5;
                } else if (var10.id == Item.PAPER.id) {
                    ++var3;
                } else if (var10.id == Item.GLOWSTONE.id) {
                    ++var7;
                } else if (var10.id == Item.DIAMOND.id) {
                    ++var7;
                } else if (var10.id == Item.FIREBALL.id) {
                    ++var8;
                } else if (var10.id == Item.FEATHER.id) {
                    ++var8;
                } else if (var10.id == Item.GOLD_NUGGET.id) {
                    ++var8;
                } else {
                    if (var10.id != Item.SKULL.id) {
                        return false;
                    }

                    ++var8;
                }
            }
        }

        var7 += var5 + var8;
        if (var4 > 3 || var3 > 1) {
            return false;
        } else if (var4 >= 1 && var3 == 1 && var7 == 0) {
            this.ingredient = new ItemStack(Item.FIREWORKS);
            NbtCompound var15 = new NbtCompound();
            if (var6 > 0) {
                NbtCompound var18 = new NbtCompound("Fireworks");
                NbtList var25 = new NbtList("Explosions");

                for(int var22 = 0; var22 < par1InventoryCrafting.getInvSize(); ++var22) {
                    ItemStack var26 = par1InventoryCrafting.getInvStack(var22);
                    if (var26 != null && var26.id == Item.FIREWORKS_CHARGE.id && var26.hasNbt() && var26.getNbt().contains("Explosion")) {
                        var25.method_1217(var26.getNbt().getCompound("Explosion"));
                    }
                }

                var18.put("Explosions", var25);
                var18.putByte("Flight", (byte)var4);
                var15.put("Fireworks", var18);
            }

            this.ingredient.setNbt(var15);
            return true;
        } else if (var4 == 1 && var3 == 0 && var6 == 0 && var5 > 0 && var8 <= 1) {
            this.ingredient = new ItemStack(Item.FIREWORKS_CHARGE);
            NbtCompound var15 = new NbtCompound();
            NbtCompound var18 = new NbtCompound("Explosion");
            byte var21 = 0;
            ArrayList var12 = new ArrayList();

            for(int var13 = 0; var13 < par1InventoryCrafting.getInvSize(); ++var13) {
                ItemStack var14 = par1InventoryCrafting.getInvStack(var13);
                if (var14 != null) {
                    if (var14.id == Item.DYES.id) {
                        var12.add(DyeItem.COLORS[var14.getData()]);
                    } else if (var14.id == Item.GLOWSTONE.id) {
                        var18.putBoolean("Flicker", true);
                    } else if (var14.id == Item.DIAMOND.id) {
                        var18.putBoolean("Trail", true);
                    } else if (var14.id == Item.FIREBALL.id) {
                        var21 = 1;
                    } else if (var14.id == Item.FEATHER.id) {
                        var21 = 4;
                    } else if (var14.id == Item.GOLD_NUGGET.id) {
                        var21 = 2;
                    } else if (var14.id == Item.SKULL.id) {
                        var21 = 3;
                    }
                }
            }

            int[] var24 = new int[var12.size()];

            for(int var27 = 0; var27 < var24.length; ++var27) {
                var24[var27] = (int) var12.get(var27);
            }

            var18.putIntArray("Colors", var24);
            var18.putByte("Type", var21);
            var15.put("Explosion", var18);
            this.ingredient.setNbt(var15);
            return true;
        } else if (var4 == 0 && var3 == 0 && var6 == 1 && var5 > 0 && var5 == var7) {
            ArrayList var16 = new ArrayList();

            for(int var20 = 0; var20 < par1InventoryCrafting.getInvSize(); ++var20) {
                ItemStack var11 = par1InventoryCrafting.getInvStack(var20);
                if (var11 != null) {
                    if (var11.id == Item.DYES.id) {
                        var16.add(DyeItem.COLORS[var11.getData()]);
                    } else if (var11.id == Item.FIREWORKS_CHARGE.id) {
                        this.ingredient = var11.copy();
                        this.ingredient.count = 1;
                    }
                }
            }

            int[] var17 = new int[var16.size()];

            for(int var19 = 0; var19 < var17.length; ++var19) {
                var17[var19] = (int) var16.get(var19);
            }

            if (this.ingredient != null && this.ingredient.hasNbt()) {
                NbtCompound var23 = this.ingredient.getNbt().getCompound("Explosion");
                if (var23 == null) {
                    return false;
                } else {
                    var23.putIntArray("FadeColors", var17);
                    return true;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
