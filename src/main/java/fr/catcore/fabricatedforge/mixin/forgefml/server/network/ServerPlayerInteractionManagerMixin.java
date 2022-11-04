package fr.catcore.fabricatedforge.mixin.forgefml.server.network;

import fr.catcore.fabricatedforge.mixininterface.IBlock;
import fr.catcore.fabricatedforge.mixininterface.IItem;
import fr.catcore.fabricatedforge.mixininterface.IServerPlayerInteractionManager;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.Event;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ServerPlayerInteractionManager.class)
public abstract class ServerPlayerInteractionManagerMixin implements IServerPlayerInteractionManager {

    @Shadow private GameMode gameMode;

    @Shadow public ServerPlayerEntity player;

    @Shadow public World world;

    @Shadow public abstract boolean isCreative();

    @Shadow private int tickCounter;

    @Shadow private int field_2848;

    @Shadow private boolean mining;

    @Shadow private int field_2849;

    @Shadow private int field_2850;

    @Shadow private int field_2851;

    @Shadow private int field_2858;

    @Unique
    private double blockReachDistance = 5.0;

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public void method_2167(int par1, int par2, int par3, int par4) {
        if (!this.gameMode.isAdventure()) {
            PlayerInteractEvent event = ForgeEventFactory.onPlayerInteract(this.player, PlayerInteractEvent.Action.LEFT_CLICK_BLOCK, par1, par2, par3, par4);
            if (event.isCanceled()) {
                this.player.field_2823.sendPacket(new BlockUpdateS2CPacket(par1, par2, par3, this.world));
                return;
            }

            if (this.isCreative()) {
                if (!this.world.method_3638((PlayerEntity)null, par1, par2, par3, par4)) {
                    this.method_2173(par1, par2, par3);
                }
            } else {
                this.field_2848 = this.tickCounter;
                float var5 = 1.0F;
                int var6 = this.world.getBlock(par1, par2, par3);
                Block block = Block.BLOCKS[var6];
                if (block != null) {
                    if (event.useBlock != Event.Result.DENY) {
                        block.method_420(this.world, par1, par2, par3, this.player);
                        this.world.method_3638(this.player, par1, par2, par3, par4);
                    } else {
                        this.player.field_2823.sendPacket(new BlockUpdateS2CPacket(par1, par2, par3, this.world));
                    }

                    var5 = block.method_405(this.player, this.player.world, par1, par2, par3);
                }

                if (event.useItem == Event.Result.DENY) {
                    if (var5 >= 1.0F) {
                        this.player.field_2823.sendPacket(new BlockUpdateS2CPacket(par1, par2, par3, this.world));
                    }

                    return;
                }

                if (var6 > 0 && var5 >= 1.0F) {
                    this.method_2173(par1, par2, par3);
                } else {
                    this.mining = true;
                    this.field_2849 = par1;
                    this.field_2850 = par2;
                    this.field_2851 = par3;
                    int var7 = (int)(var5 * 10.0F);
                    this.world.method_3698(this.player.id, par1, par2, par3, var7);
                    this.field_2858 = var7;
                }
            }
        }

    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    private boolean method_2177(int par1, int par2, int par3) {
        Block var4 = Block.BLOCKS[this.world.getBlock(par1, par2, par3)];
        int var5 = this.world.getBlockData(par1, par2, par3);
        if (var4 != null) {
            var4.method_412(this.world, par1, par2, par3, var5, this.player);
        }

        boolean var6 = var4 != null && ((IBlock)var4).removeBlockByPlayer(this.world, this.player, par1, par2, par3);
        if (var4 != null && var6) {
            var4.onDestroyed(this.world, par1, par2, par3, var5);
        }

        return var6;
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public boolean method_2173(int par1, int par2, int par3) {
        if (this.gameMode.isAdventure()) {
            return false;
        } else {
            ItemStack stack = this.player.getMainHandStack();
            if (stack != null && ((IItem)stack.getItem()).onBlockStartBreak(stack, par1, par2, par3, this.player)) {
                return false;
            } else {
                int var4 = this.world.getBlock(par1, par2, par3);
                int var5 = this.world.getBlockData(par1, par2, par3);
                this.world.dispatchEvent(this.player, 2001, par1, par2, par3, var4 + (this.world.getBlockData(par1, par2, par3) << 12));
                boolean var6 = false;
                if (this.isCreative()) {
                    var6 = this.method_2177(par1, par2, par3);
                    this.player.field_2823.sendPacket(new BlockUpdateS2CPacket(par1, par2, par3, this.world));
                } else {
                    ItemStack var7 = this.player.getMainHandStack();
                    boolean var8 = false;
                    Block block = Block.BLOCKS[var4];
                    if (block != null) {
                        var8 = ((IBlock)block).canHarvestBlock(this.player, var5);
                    }

                    if (var7 != null) {
                        var7.method_3417(this.world, var4, par1, par2, par3, this.player);
                        if (var7.count == 0) {
                            this.player.removeSelectedSlotItem();
                            MinecraftForge.EVENT_BUS.post(new PlayerDestroyItemEvent(this.player, var7));
                        }
                    }

                    var6 = this.method_2177(par1, par2, par3);
                    if (var6 && var8) {
                        Block.BLOCKS[var4].method_424(this.world, this.player, par1, par2, par3, var5);
                    }
                }

                return var6;
            }
        }
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public boolean interactItem(PlayerEntity par1EntityPlayer, World par2World, ItemStack par3ItemStack) {
        int var4 = par3ItemStack.count;
        int var5 = par3ItemStack.getMeta();
        ItemStack var6 = par3ItemStack.onStartUse(par2World, par1EntityPlayer);
        if (var6 == par3ItemStack && (var6 == null || var6.count == var4) && (var6 == null || var6.getMaxUseTime() <= 0)) {
            return false;
        } else {
            par1EntityPlayer.inventory.main[par1EntityPlayer.inventory.selectedSlot] = var6;
            if (this.isCreative()) {
                var6.count = var4;
                var6.setDamage(var5);
            }

            if (var6.count == 0) {
                par1EntityPlayer.inventory.main[par1EntityPlayer.inventory.selectedSlot] = null;
                MinecraftForge.EVENT_BUS.post(new PlayerDestroyItemEvent(this.player, var6));
            }

            return true;
        }
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public boolean method_2170(PlayerEntity par1EntityPlayer, World par2World, ItemStack par3ItemStack, int par4, int par5, int par6, int par7, float par8, float par9, float par10) {
        PlayerInteractEvent event = ForgeEventFactory.onPlayerInteract(par1EntityPlayer, PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK, par4, par5, par6, par7);
        if (event.isCanceled()) {
            this.player.field_2823.sendPacket(new BlockUpdateS2CPacket(par4, par5, par6, this.world));
            return false;
        } else {
            Item item = par3ItemStack != null ? par3ItemStack.getItem() : null;
            if (item != null && ((IItem)item).onItemUseFirst(par3ItemStack, par1EntityPlayer, par2World, par4, par5, par6, par7, par8, par9, par10)) {
                if (par3ItemStack.count <= 0) {
                    ForgeEventFactory.onPlayerDestroyItem(this.player, par3ItemStack);
                }

                return true;
            } else {
                int var11 = par2World.getBlock(par4, par5, par6);
                Block block = Block.BLOCKS[var11];
                boolean result = false;
                if (block != null) {
                    if (event.useBlock != Event.Result.DENY) {
                        result = block.onActivated(par2World, par4, par5, par6, par1EntityPlayer, par7, par8, par9, par10);
                    } else {
                        this.player.field_2823.sendPacket(new BlockUpdateS2CPacket(par4, par5, par6, this.world));
                        result = event.useItem != Event.Result.ALLOW;
                    }
                }

                if (par3ItemStack != null && !result) {
                    int meta = par3ItemStack.getMeta();
                    int size = par3ItemStack.count;
                    result = par3ItemStack.method_3413(par1EntityPlayer, par2World, par4, par5, par6, par7, par8, par9, par10);
                    if (this.isCreative()) {
                        par3ItemStack.setDamage(meta);
                        par3ItemStack.count = size;
                    }

                    if (par3ItemStack.count <= 0) {
                        ForgeEventFactory.onPlayerDestroyItem(this.player, par3ItemStack);
                    }
                }

                return result;
            }
        }
    }

    @Override
    public double getBlockReachDistance() {
        return this.blockReachDistance;
    }

    @Override
    public void setBlockReachDistance(double distance) {
        this.blockReachDistance = distance;
    }
}
