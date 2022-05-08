package net.minecraftforge.oredict;

import net.minecraft.block.Block;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeType;

import java.util.ArrayList;
import java.util.Iterator;

public class ShapelessOreRecipe implements RecipeType {
    private ItemStack output;
    private ArrayList input;

    public ShapelessOreRecipe(Block result, Object... recipe) {
        this(new ItemStack(result), recipe);
    }

    public ShapelessOreRecipe(Item result, Object... recipe) {
        this(new ItemStack(result), recipe);
    }

    public ShapelessOreRecipe(ItemStack result, Object... recipe) {
        this.output = null;
        this.input = new ArrayList();
        this.output = result.copy();
        Object[] arr$ = recipe;
        int len$ = recipe.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            Object in = arr$[i$];
            if (in instanceof ItemStack) {
                this.input.add(((ItemStack)in).copy());
            } else if (in instanceof Item) {
                this.input.add(new ItemStack((Item)in));
            } else if (in instanceof Block) {
                this.input.add(new ItemStack((Block)in));
            } else {
                if (!(in instanceof String)) {
                    String ret = "Invalid shapeless ore recipe: ";
                    Object[] arr$ = recipe;
                    int len$ = recipe.length;

                    for(int i$ = 0; i$ < len$; ++i$) {
                        Object tmp = arr$[i$];
                        ret = ret + tmp + ", ";
                    }

                    ret = ret + this.output;
                    throw new RuntimeException(ret);
                }

                this.input.add(OreDictionary.getOres((String)in));
            }
        }

    }

    public int getSize() {
        return this.input.size();
    }

    public ItemStack getOutput() {
        return this.output;
    }

    public ItemStack getResult(CraftingInventory var1) {
        return this.output.copy();
    }

    public boolean method_3500(CraftingInventory var1) {
        ArrayList required = new ArrayList(this.input);

        for(int x = 0; x < var1.getInvSize(); ++x) {
            ItemStack slot = var1.getInvStack(x);
            if (slot != null) {
                boolean inRecipe = false;
                Iterator req = required.iterator();

                while(req.hasNext()) {
                    boolean match = false;
                    Object next = req.next();
                    if (next instanceof ItemStack) {
                        match = this.checkItemEquals((ItemStack)next, slot);
                    } else {
                        ItemStack item;
                        if (next instanceof ArrayList) {
                            for(Iterator i$ = ((ArrayList)next).iterator(); i$.hasNext(); match = match || this.checkItemEquals(item, slot)) {
                                item = (ItemStack)i$.next();
                            }
                        }
                    }

                    if (match) {
                        inRecipe = true;
                        required.remove(next);
                        break;
                    }
                }

                if (!inRecipe) {
                    return false;
                }
            }
        }

        return required.isEmpty();
    }

    private boolean checkItemEquals(ItemStack target, ItemStack input) {
        return target.id == input.id && (target.getMeta() == -1 || target.getMeta() == input.getMeta());
    }
}
