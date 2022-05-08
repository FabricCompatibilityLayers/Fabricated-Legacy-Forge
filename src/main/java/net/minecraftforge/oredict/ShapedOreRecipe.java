package net.minecraftforge.oredict;

import net.minecraft.block.Block;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class ShapedOreRecipe implements RecipeType {
    private static final int MAX_CRAFT_GRID_WIDTH = 3;
    private static final int MAX_CRAFT_GRID_HEIGHT = 3;
    private ItemStack output;
    private Object[] input;
    private int width;
    private int height;
    private boolean mirriored;

    public ShapedOreRecipe(Block result, Object... recipe) {
        this(result, true, recipe);
    }

    public ShapedOreRecipe(Item result, Object... recipe) {
        this(result, true, recipe);
    }

    public ShapedOreRecipe(ItemStack result, Object... recipe) {
        this(result, true, recipe);
    }

    public ShapedOreRecipe(Block result, boolean mirrior, Object... recipe) {
        this(new ItemStack(result), mirrior, recipe);
    }

    public ShapedOreRecipe(Item result, boolean mirrior, Object... recipe) {
        this(new ItemStack(result), mirrior, recipe);
    }

    public ShapedOreRecipe(ItemStack result, boolean mirrior, Object... recipe) {
        this.output = null;
        this.input = null;
        this.width = 0;
        this.height = 0;
        this.mirriored = true;
        this.output = result.copy();
        this.mirriored = mirrior;
        String shape = "";
        int idx = 0;
        String ret;
        int len$;
        int len$;
        String ret;
        if (recipe[idx] instanceof String[]) {
            String[] parts = (String[])((String[])recipe[idx++]);
            String[] arr$ = parts;
            len$ = parts.length;

            for(len$ = 0; len$ < len$; ++len$) {
                ret = arr$[len$];
                this.width = ret.length();
                shape = shape + ret;
            }

            this.height = parts.length;
        } else {
            while(recipe[idx] instanceof String) {
                ret = (String)recipe[idx++];
                shape = shape + ret;
                this.width = ret.length();
                ++this.height;
            }
        }

        if (this.width * this.height != shape.length()) {
            ret = "Invalid shaped ore recipe: ";
            Object[] arr$ = recipe;
            len$ = recipe.length;

            for(len$ = 0; len$ < len$; ++len$) {
                Object tmp = arr$[len$];
                ret = ret + tmp + ", ";
            }

            ret = ret + this.output;
            throw new RuntimeException(ret);
        } else {
            HashMap itemMap;
            for(itemMap = new HashMap(); idx < recipe.length; idx += 2) {
                Character chr = (Character)recipe[idx];
                Object in = recipe[idx + 1];
                Object val = null;
                if (in instanceof ItemStack) {
                    itemMap.put(chr, ((ItemStack)in).copy());
                } else if (in instanceof Item) {
                    itemMap.put(chr, new ItemStack((Item)in));
                } else if (in instanceof Block) {
                    itemMap.put(chr, new ItemStack((Block)in, 1, -1));
                } else {
                    if (!(in instanceof String)) {
                        ret = "Invalid shaped ore recipe: ";
                        Object[] arr$ = recipe;
                        int len$ = recipe.length;

                        for(int i$ = 0; i$ < len$; ++i$) {
                            Object tmp = arr$[i$];
                            ret = ret + tmp + ", ";
                        }

                        ret = ret + this.output;
                        throw new RuntimeException(ret);
                    }

                    itemMap.put(chr, OreDictionary.getOres((String)in));
                }
            }

            this.input = new Object[this.width * this.height];
            int x = 0;
            char[] arr$ = shape.toCharArray();
            len$ = arr$.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                char chr = arr$[i$];
                this.input[x++] = itemMap.get(chr);
            }

        }
    }

    public ItemStack getResult(CraftingInventory var1) {
        return this.output.copy();
    }

    public int getSize() {
        return this.input.length;
    }

    public ItemStack getOutput() {
        return this.output;
    }

    public boolean method_3500(CraftingInventory inv) {
        for(int x = 0; x <= 3 - this.width; ++x) {
            for(int y = 0; y <= 3 - this.height; ++y) {
                if (this.checkMatch(inv, x, y, true)) {
                    return true;
                }

                if (this.mirriored && this.checkMatch(inv, x, y, false)) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean checkMatch(CraftingInventory inv, int startX, int startY, boolean mirrior) {
        for(int x = 0; x < 3; ++x) {
            for(int y = 0; y < 3; ++y) {
                int subX = x - startX;
                int subY = y - startY;
                Object target = null;
                if (subX >= 0 && subY >= 0 && subX < this.width && subY < this.height) {
                    if (mirrior) {
                        target = this.input[this.width - subX - 1 + subY * this.width];
                    } else {
                        target = this.input[subX + subY * this.width];
                    }
                }

                ItemStack slot = inv.method_3280(x, y);
                if (target instanceof ItemStack) {
                    if (!this.checkItemEquals((ItemStack)target, slot)) {
                        return false;
                    }
                } else if (!(target instanceof ArrayList)) {
                    if (target == null && slot != null) {
                        return false;
                    }
                } else {
                    boolean matched = false;

                    ItemStack item;
                    for(Iterator i$ = ((ArrayList)target).iterator(); i$.hasNext(); matched = matched || this.checkItemEquals(item, slot)) {
                        item = (ItemStack)i$.next();
                    }

                    if (!matched) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    private boolean checkItemEquals(ItemStack target, ItemStack input) {
        if ((input != null || target == null) && (input == null || target != null)) {
            return target.id == input.id && (target.getMeta() == -1 || target.getMeta() == input.getMeta());
        } else {
            return false;
        }
    }

    public void setMirriored(boolean mirrior) {
        this.mirriored = mirrior;
    }
}
