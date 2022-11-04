package fr.catcore.fabricatedforge.mixin.forgefml.item;

import net.minecraft.block.material.Material;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResultType;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(BucketItem.class)
public abstract class BucketItemMixin extends Item {
    @Shadow private int field_4170;

    @Shadow public abstract boolean method_3312(World world, double d, double e, double f, int i, int j, int k);

    protected BucketItemMixin(int id) {
        super(id);
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public ItemStack onStartUse(ItemStack par1ItemStack, World par2World, PlayerEntity par3EntityPlayer) {
        float var4 = 1.0F;
        double var5 = par3EntityPlayer.prevX + (par3EntityPlayer.x - par3EntityPlayer.prevX) * (double)var4;
        double var7 = par3EntityPlayer.prevY + (par3EntityPlayer.y - par3EntityPlayer.prevY) * (double)var4 + 1.62 - (double)par3EntityPlayer.heightOffset;
        double var9 = par3EntityPlayer.prevZ + (par3EntityPlayer.z - par3EntityPlayer.prevZ) * (double)var4;
        boolean var11 = this.field_4170 == 0;
        BlockHitResult var12 = this.onHit(par2World, par3EntityPlayer, var11);
        if (var12 == null) {
            return par1ItemStack;
        } else {
            FillBucketEvent event = new FillBucketEvent(par3EntityPlayer, par1ItemStack, par2World, var12);
            if (MinecraftForge.EVENT_BUS.post(event)) {
                return par1ItemStack;
            } else if (event.isHandeled()) {
                if (par3EntityPlayer.abilities.creativeMode) {
                    return par1ItemStack;
                } else if (--par1ItemStack.count <= 0) {
                    return event.result;
                } else {
                    if (!par3EntityPlayer.inventory.insertStack(event.result)) {
                        par3EntityPlayer.dropStack(event.result);
                    }

                    return par1ItemStack;
                }
            } else {
                if (var12.field_595 == HitResultType.TILE) {
                    int var13 = var12.x;
                    int var14 = var12.y;
                    int var15 = var12.z;
                    if (!par2World.method_3637(par3EntityPlayer, var13, var14, var15)) {
                        return par1ItemStack;
                    }

                    if (this.field_4170 == 0) {
                        if (!par3EntityPlayer.method_3204(var13, var14, var15)) {
                            return par1ItemStack;
                        }

                        if (par2World.getMaterial(var13, var14, var15) == Material.WATER && par2World.getBlockData(var13, var14, var15) == 0) {
                            par2World.method_3690(var13, var14, var15, 0);
                            if (par3EntityPlayer.abilities.creativeMode) {
                                return par1ItemStack;
                            }

                            if (--par1ItemStack.count <= 0) {
                                return new ItemStack(Item.WATER_BUCKET);
                            }

                            if (!par3EntityPlayer.inventory.insertStack(new ItemStack(Item.WATER_BUCKET))) {
                                par3EntityPlayer.dropStack(new ItemStack(Item.WATER_BUCKET.id, 1, 0));
                            }

                            return par1ItemStack;
                        }

                        if (par2World.getMaterial(var13, var14, var15) == Material.LAVA && par2World.getBlockData(var13, var14, var15) == 0) {
                            par2World.method_3690(var13, var14, var15, 0);
                            if (par3EntityPlayer.abilities.creativeMode) {
                                return par1ItemStack;
                            }

                            if (--par1ItemStack.count <= 0) {
                                return new ItemStack(Item.LAVA_BUCKET);
                            }

                            if (!par3EntityPlayer.inventory.insertStack(new ItemStack(Item.LAVA_BUCKET))) {
                                par3EntityPlayer.dropStack(new ItemStack(Item.LAVA_BUCKET.id, 1, 0));
                            }

                            return par1ItemStack;
                        }
                    } else {
                        if (this.field_4170 < 0) {
                            return new ItemStack(Item.BUCKET);
                        }

                        if (var12.side == 0) {
                            --var14;
                        }

                        if (var12.side == 1) {
                            ++var14;
                        }

                        if (var12.side == 2) {
                            --var15;
                        }

                        if (var12.side == 3) {
                            ++var15;
                        }

                        if (var12.side == 4) {
                            --var13;
                        }

                        if (var12.side == 5) {
                            ++var13;
                        }

                        if (!par3EntityPlayer.method_3204(var13, var14, var15)) {
                            return par1ItemStack;
                        }

                        if (this.method_3312(par2World, var5, var7, var9, var13, var14, var15) && !par3EntityPlayer.abilities.creativeMode) {
                            return new ItemStack(Item.BUCKET);
                        }
                    }
                } else if (this.field_4170 == 0 && var12.entity instanceof CowEntity) {
                    return new ItemStack(Item.MILK_BUCKET);
                }

                return par1ItemStack;
            }
        }
    }
}
