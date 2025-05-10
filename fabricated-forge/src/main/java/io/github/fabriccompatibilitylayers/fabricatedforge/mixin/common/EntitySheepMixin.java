package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.common;

import io.github.fabriccompatibilitylayers.fabricatedforge.extension.common.EntityExtension;
import net.minecraft.src.*;
import net.minecraftforge.common.IShearable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.ArrayList;

@Mixin(EntitySheep.class)
public abstract class EntitySheepMixin extends EntityAnimal implements IShearable, EntityExtension {
    @Shadow public abstract boolean getSheared();

    @Shadow public abstract void setSheared(boolean par1);

    @Shadow public abstract int getFleeceColor();

    public EntitySheepMixin(World par1World) {
        super(par1World);
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public boolean interact(EntityPlayer par1EntityPlayer)
    {
        return super.interact(par1EntityPlayer);
    }

    @Override
    public boolean isShearable(ItemStack item, World world, int X, int Y, int Z)
    {
        return !getSheared() && !isChild();
    }

    @Override
    public ArrayList<ItemStack> onSheared(ItemStack item, World world, int X, int Y, int Z, int fortune)
    {
        ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
        setSheared(true);
        int i = 1 + rand.nextInt(3);
        for (int j = 0; j < i; j++)
        {
            ret.add(new ItemStack(Block.cloth.blockID, 1, getFleeceColor()));
        }
        return ret;
    }
}
