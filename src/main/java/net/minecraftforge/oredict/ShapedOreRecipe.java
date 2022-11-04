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
        this.output = result.copy();
        this.mirriored = mirrior;

        String shape = "";
        int idx = 0;

        if (recipe[idx] instanceof String[])
        {
            String[] parts = ((String[])recipe[idx++]);

            for (String s : parts)
            {
                this.width = s.length();
                shape += s;
            }

            this.height = parts.length;
        }
        else
        {
            while (recipe[idx] instanceof String)
            {
                String s = (String)recipe[idx++];
                shape += s;
                this.width = s.length();
                this.height++;
            }
        }

        if (this.width * this.height != shape.length())
        {
            String ret = "Invalid shaped ore recipe: ";
            for (Object tmp :  recipe)
            {
                ret += tmp + ", ";
            }
            ret += this.output;
            throw new RuntimeException(ret);
        }

        HashMap<Character, Object> itemMap = new HashMap<Character, Object>();

        for (; idx < recipe.length; idx += 2)
        {
            Character chr = (Character)recipe[idx];
            Object in = recipe[idx + 1];
            Object val = null;

            if (in instanceof ItemStack)
            {
                itemMap.put(chr, ((ItemStack)in).copy());
            }
            else if (in instanceof Item)
            {
                itemMap.put(chr, new ItemStack((Item)in));
            }
            else if (in instanceof Block)
            {
                itemMap.put(chr, new ItemStack((Block)in, 1, -1));
            }
            else if (in instanceof String)
            {
                itemMap.put(chr, OreDictionary.getOres((String)in));
            }
            else
            {
                String ret = "Invalid shaped ore recipe: ";
                for (Object tmp :  recipe)
                {
                    ret += tmp + ", ";
                }
                ret += this.output;
                throw new RuntimeException(ret);
            }
        }

        this.input = new Object[this.width * this.height];
        int x = 0;
        for (char chr : shape.toCharArray())
        {
            this.input[x++] = itemMap.get(chr);
        }
    }

    @Override
    public ItemStack getResult(CraftingInventory var1) {
        return this.output.copy();
    }

    @Override
    public int getSize() {
        return this.input.length;
    }

    @Override
    public ItemStack getOutput() {
        return this.output;
    }

    @Override
    public boolean method_3500(CraftingInventory inv) {
        for (int x = 0; x <= MAX_CRAFT_GRID_WIDTH - width; x++)
        {
            for (int y = 0; y <= MAX_CRAFT_GRID_HEIGHT - height; ++y)
            {
                if (this.checkMatch(inv, x, y, true))
                {
                    return true;
                }

                if (this.mirriored && this.checkMatch(inv, x, y, false))
                {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean checkMatch(CraftingInventory inv, int startX, int startY, boolean mirrior) {
        for (int x = 0; x < MAX_CRAFT_GRID_WIDTH; x++)
        {
            for (int y = 0; y < MAX_CRAFT_GRID_HEIGHT; y++)
            {
                int subX = x - startX;
                int subY = y - startY;
                Object target = null;

                if (subX >= 0 && subY >= 0 && subX < this.width && subY < this.height)
                {
                    if (mirrior)
                    {
                        target = this.input[this.width - subX - 1 + subY * this.width];
                    }
                    else
                    {
                        target = this.input[subX + subY * this.width];
                    }
                }

                ItemStack slot = inv.getStackAt(x, y);

                if (target instanceof ItemStack)
                {
                    if (!this.checkItemEquals((ItemStack)target, slot))
                    {
                        return false;
                    }
                }
                else if (target instanceof ArrayList)
                {
                    boolean matched = false;

                    for (ItemStack item : (ArrayList<ItemStack>)target)
                    {
                        matched = matched || this.checkItemEquals(item, slot);
                    }

                    if (!matched)
                    {
                        return false;
                    }
                }
                else if (target == null && slot != null)
                {
                    return false;
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
