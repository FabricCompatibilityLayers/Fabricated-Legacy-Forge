package fr.catcore.fabricatedforge.mixin.forgefml.server;

import cpw.mods.fml.common.network.FMLNetworkHandler;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.slot.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.WritableBookItem;
import net.minecraft.item.WrittenBookItem;
import net.minecraft.network.Connection;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.c2s.play.*;
import net.minecraft.network.packet.s2c.play.BlockUpdate_S2CPacket;
import net.minecraft.network.packet.s2c.play.ChatMessage_S2CPacket;
import net.minecraft.network.packet.s2c.play.MapUpdate_S2CPacket;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdate_S2CPacket;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.VillagerScreenHandler;
import net.minecraft.server.BanEntry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerPacketListener;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.BlockPos;
import net.minecraft.util.SharedConstants;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.Event;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.logging.Logger;

@Mixin(ServerPacketListener.class)
public abstract class ServerPacketListenerMixin extends PacketListener {

    @Shadow private MinecraftServer server;

    @Shadow private ServerPlayerEntity player;

    @Shadow private boolean hasMoved;

    @Shadow private boolean field_2910;

    @Shadow private double lastTickY;

    @Shadow private double lastTickX;

    @Shadow private double lastTickZ;

    @Shadow public abstract void disconnect(String reason);

    @Shadow public static Logger LOGGER;

    @Shadow public abstract void requestTeleport(double x, double y, double z, float yaw, float pitch);

    @Shadow public int floatingTicks;

    @Shadow public abstract void sendPacket(Packet packet);

    @Shadow protected abstract void executeCommand(String input);

    @Shadow private int messageCooldown;

    @Shadow public Connection connection;

    /**
     * @author Minecraft Forge
     */
    @Overwrite
    public void onPlayerMove(PlayerMove_C2SPacket par1Packet10Flying) {
        ServerWorld var2 = this.server.getWorld(this.player._dimension);
        this.hasMoved = true;
        if (!this.player.killedEnderdragon) {
            double var3;
            if (!this.field_2910) {
                var3 = par1Packet10Flying.collisionY - this.lastTickY;
                if (par1Packet10Flying.x == this.lastTickX && var3 * var3 < 0.01 && par1Packet10Flying.z == this.lastTickZ) {
                    this.field_2910 = true;
                }
            }

            if (this.field_2910) {
                double var5;
                double var7;
                double var9;
                double var13;
                float var11;
                float var12;
                double var15;
                if (this.player.vehicle != null) {
                    var11 = this.player.yaw;
                    var12 = this.player.pitch;
                    this.player.vehicle.updatePassengerPosition();
                    var5 = this.player.x;
                    var7 = this.player.y;
                    var9 = this.player.z;
                    var15 = 0.0;
                    var13 = 0.0;
                    if (par1Packet10Flying.changeLook) {
                        var11 = par1Packet10Flying.yaw;
                        var12 = par1Packet10Flying.pitch;
                    }

                    if (par1Packet10Flying.changePosition && par1Packet10Flying.collisionY == -999.0 && par1Packet10Flying.y == -999.0) {
                        if (Math.abs(par1Packet10Flying.x) > 1.0 || Math.abs(par1Packet10Flying.z) > 1.0) {
                            System.err.println(this.player.username + " was caught trying to crash the server with an invalid position.");
                            this.disconnect("Nope!");
                            return;
                        }

                        var15 = par1Packet10Flying.x;
                        var13 = par1Packet10Flying.z;
                    }

                    this.player.onGround = par1Packet10Flying.onGround;
                    this.player.tickPlayer();
                    this.player.move(var15, 0.0, var13);
                    this.player.updatePositionAndAngles(var5, var7, var9, var11, var12);
                    this.player.velocityX = var15;
                    this.player.velocityZ = var13;
                    if (this.player.vehicle != null) {
                        var2.method_2139(this.player.vehicle, true);
                    }

                    if (this.player.vehicle != null) {
                        this.player.vehicle.updatePassengerPosition();
                    }

                    if (!this.field_2910) {
                        return;
                    }

                    this.server.getPlayerManager().method_2003(this.player);
                    this.lastTickX = this.player.x;
                    this.lastTickY = this.player.y;
                    this.lastTickZ = this.player.z;
                    var2.method_3705(this.player);
                    return;
                }

                if (this.player.method_2641()) {
                    this.player.tickPlayer();
                    this.player.updatePositionAndAngles(this.lastTickX, this.lastTickY, this.lastTickZ, this.player.yaw, this.player.pitch);
                    var2.method_3705(this.player);
                    return;
                }

                var3 = this.player.y;
                this.lastTickX = this.player.x;
                this.lastTickY = this.player.y;
                this.lastTickZ = this.player.z;
                var5 = this.player.x;
                var7 = this.player.y;
                var9 = this.player.z;
                var11 = this.player.yaw;
                var12 = this.player.pitch;
                if (par1Packet10Flying.changePosition && par1Packet10Flying.collisionY == -999.0 && par1Packet10Flying.y == -999.0) {
                    par1Packet10Flying.changePosition = false;
                }

                if (par1Packet10Flying.changePosition) {
                    var5 = par1Packet10Flying.x;
                    var7 = par1Packet10Flying.collisionY;
                    var9 = par1Packet10Flying.z;
                    var13 = par1Packet10Flying.y - par1Packet10Flying.collisionY;
                    if (!this.player.method_2641() && (var13 > 1.65 || var13 < 0.1)) {
                        this.disconnect("Illegal stance");
                        LOGGER.warning(this.player.username + " had an illegal stance: " + var13);
                        return;
                    }

                    if (Math.abs(par1Packet10Flying.x) > 3.2E7 || Math.abs(par1Packet10Flying.z) > 3.2E7) {
                        this.disconnect("Illegal position");
                        return;
                    }
                }

                if (par1Packet10Flying.changeLook) {
                    var11 = par1Packet10Flying.yaw;
                    var12 = par1Packet10Flying.pitch;
                }

                this.player.tickPlayer();
                this.player.field_3214 = 0.0F;
                this.player.updatePositionAndAngles(this.lastTickX, this.lastTickY, this.lastTickZ, var11, var12);
                if (!this.field_2910) {
                    return;
                }

                var13 = var5 - this.player.x;
                var15 = var7 - this.player.y;
                double var17 = var9 - this.player.z;
                double var19 = Math.min(Math.abs(var13), Math.abs(this.player.velocityX));
                double var21 = Math.min(Math.abs(var15), Math.abs(this.player.velocityY));
                double var23 = Math.min(Math.abs(var17), Math.abs(this.player.velocityZ));
                double var25 = var19 * var19 + var21 * var21 + var23 * var23;
                if (var25 > 100.0 && (!this.server.isSinglePlayer() || !this.server.getUserName().equals(this.player.username))) {
                    LOGGER.warning(this.player.username + " moved too quickly! " + var13 + "," + var15 + "," + var17 + " (" + var19 + ", " + var21 + ", " + var23 + ")");
                    this.requestTeleport(this.lastTickX, this.lastTickY, this.lastTickZ, this.player.yaw, this.player.pitch);
                    return;
                }

                float var27 = 0.0625F;
                boolean var28 = var2.doesBoxCollide(this.player, this.player.boundingBox.method_591().increment((double)var27, (double)var27, (double)var27)).isEmpty();
                if (this.player.onGround && !par1Packet10Flying.onGround && var15 > 0.0) {
                    this.player.addExhaustion(0.2F);
                }

                if (!this.field_2910) {
                    return;
                }

                this.player.move(var13, var15, var17);
                this.player.onGround = par1Packet10Flying.onGround;
                this.player.method_3209(var13, var15, var17);
                double var29 = var15;
                var13 = var5 - this.player.x;
                var15 = var7 - this.player.y;
                if (var15 > -0.5 || var15 < 0.5) {
                    var15 = 0.0;
                }

                var17 = var9 - this.player.z;
                var25 = var13 * var13 + var15 * var15 + var17 * var17;
                boolean var31 = false;
                if (var25 > 0.0625 && !this.player.method_2641() && !this.player.interactionManager.isCreative()) {
                    var31 = true;
                    LOGGER.warning(this.player.username + " moved wrongly!");
                }

                if (!this.field_2910) {
                    return;
                }

                this.player.updatePositionAndAngles(var5, var7, var9, var11, var12);
                boolean var32 = var2.doesBoxCollide(this.player, this.player.boundingBox.method_591().increment((double)var27, (double)var27, (double)var27)).isEmpty();
                if (var28 && (var31 || !var32) && !this.player.method_2641() && !this.player.noClip) {
                    this.requestTeleport(this.lastTickX, this.lastTickY, this.lastTickZ, var11, var12);
                    return;
                }

                Box var33 = this.player.boundingBox.method_591().expand((double)var27, (double)var27, (double)var27).stretch(0.0, -0.55, 0.0);
                if (!this.server.isFlightEnabled() && !this.player.interactionManager.isCreative() && !var2.isBoxNotEmpty(var33) && !this.player.abilities.allowFlying) {
                    if (var29 >= -0.03125) {
                        ++this.floatingTicks;
                        if (this.floatingTicks > 80) {
                            LOGGER.warning(this.player.username + " was kicked for floating too long!");
                            this.disconnect("Flying is not enabled on this server");
                            return;
                        }
                    }
                } else {
                    this.floatingTicks = 0;
                }

                if (!this.field_2910) {
                    return;
                }

                this.player.onGround = par1Packet10Flying.onGround;
                this.server.getPlayerManager().method_2003(this.player);
                this.player.handleFall(this.player.y - var3, par1Packet10Flying.onGround);
            }
        }

    }

    /**
     * @author Minecraft Forge
     */
    @Overwrite
    public void onPlayerAction(PlayerAction_C2SPacket par1Packet14BlockDig) {
        ServerWorld var2 = this.server.getWorld(this.player._dimension);
        if (par1Packet14BlockDig.action == 4) {
            this.player.dropSelectedStack();
        } else if (par1Packet14BlockDig.action == 5) {
            this.player.stopUsingItem();
        } else {
            boolean var3 = var2.field_2821 = var2.dimension.dimensionType != 0 || this.server.getPlayerManager().canCheat(this.player.username) || this.server.isSinglePlayer();
            boolean var4 = false;
            if (par1Packet14BlockDig.action == 0) {
                var4 = true;
            }

            if (par1Packet14BlockDig.action == 2) {
                var4 = true;
            }

            int var5 = par1Packet14BlockDig.x;
            int var6 = par1Packet14BlockDig.y;
            int var7 = par1Packet14BlockDig.z;
            if (var4) {
                double var8 = this.player.x - ((double)var5 + 0.5);
                double var10 = this.player.y - ((double)var6 + 0.5) + 1.5;
                double var12 = this.player.z - ((double)var7 + 0.5);
                double var14 = var8 * var8 + var10 * var10 + var12 * var12;
                double dist = this.player.interactionManager.getBlockReachDistance() + 1.0;
                dist *= dist;
                if (var14 > dist) {
                    return;
                }

                if (var6 >= this.server.getWorldHeight()) {
                    return;
                }
            }

            BlockPos var19 = var2.getWorldSpawnPos();
            int var9 = MathHelper.abs(var5 - var19.x);
            int var20 = MathHelper.abs(var7 - var19.z);
            if (var9 > var20) {
                var20 = var9;
            }

            if (par1Packet14BlockDig.action == 0) {
                if (var20 <= this.server.spawnProtectionSize && !var3) {
                    ForgeEventFactory.onPlayerInteract(this.player, PlayerInteractEvent.Action.LEFT_CLICK_BLOCK, var5, var6, var7, 0);
                    this.player.field_2823.sendPacket(new BlockUpdate_S2CPacket(var5, var6, var7, var2));
                } else {
                    this.player.interactionManager.method_2167(var5, var6, var7, par1Packet14BlockDig.side);
                }
            } else if (par1Packet14BlockDig.action == 2) {
                this.player.interactionManager.method_2166(var5, var6, var7);
                if (var2.getBlock(var5, var6, var7) != 0) {
                    this.player.field_2823.sendPacket(new BlockUpdate_S2CPacket(var5, var6, var7, var2));
                }
            } else if (par1Packet14BlockDig.action == 1) {
                this.player.interactionManager.method_2175(var5, var6, var7);
                if (var2.getBlock(var5, var6, var7) != 0) {
                    this.player.field_2823.sendPacket(new BlockUpdate_S2CPacket(var5, var6, var7, var2));
                }
            } else if (par1Packet14BlockDig.action == 3) {
                double var11 = this.player.x - ((double)var5 + 0.5);
                double var13 = this.player.y - ((double)var6 + 0.5);
                double var15 = this.player.z - ((double)var7 + 0.5);
                double var17 = var11 * var11 + var13 * var13 + var15 * var15;
                if (var17 < 256.0) {
                    this.player.field_2823.sendPacket(new BlockUpdate_S2CPacket(var5, var6, var7, var2));
                }
            }

            var2.field_2821 = false;
        }

    }

    /**
     * @author Minecraft Forge
     */
    @Overwrite
    public void onPlayerInteractBlock(PlayerInteractBlockC2SPacket par1Packet15Place) {
        ServerWorld var2 = this.server.getWorld(this.player._dimension);
        ItemStack var3 = this.player.inventory.getMainHandStack();
        boolean var4 = false;
        int var5 = par1Packet15Place.getX();
        int var6 = par1Packet15Place.getY();
        int var7 = par1Packet15Place.getZ();
        int var8 = par1Packet15Place.getSide();
        boolean var9 = var2.field_2821 = var2.dimension.dimensionType != 0 || this.server.getPlayerManager().canCheat(this.player.username) || this.server.isSinglePlayer();
        if (par1Packet15Place.getSide() == 255) {
            if (var3 == null) {
                return;
            }

            PlayerInteractEvent event = ForgeEventFactory.onPlayerInteract(this.player, PlayerInteractEvent.Action.RIGHT_CLICK_AIR, 0, 0, 0, -1);
            if (event.useItem != Event.Result.DENY) {
                this.player.interactionManager.interactItem(this.player, var2, var3);
            }
        } else if (par1Packet15Place.getY() >= this.server.getWorldHeight() - 1 && (par1Packet15Place.getSide() == 1 || par1Packet15Place.getY() >= this.server.getWorldHeight())) {
            this.player.field_2823.sendPacket(new ChatMessage_S2CPacket("§7Height limit for building is " + this.server.getWorldHeight()));
            var4 = true;
        } else {
            BlockPos var10 = var2.getWorldSpawnPos();
            int var11 = MathHelper.abs(var5 - var10.x);
            int var12 = MathHelper.abs(var7 - var10.z);
            if (var11 > var12) {
                var12 = var11;
            }

            double dist = this.player.interactionManager.getBlockReachDistance() + 1.0;
            dist *= dist;
            if (this.field_2910 && this.player.squaredDistanceTo((double)var5 + 0.5, (double)var6 + 0.5, (double)var7 + 0.5) < dist && (var12 > this.server.spawnProtectionSize || var9)) {
                this.player.interactionManager.method_2170(this.player, var2, var3, var5, var6, var7, var8, par1Packet15Place.method_1949(), par1Packet15Place.method_1950(), par1Packet15Place.method_1951());
            }

            var4 = true;
        }

        if (var4) {
            this.player.field_2823.sendPacket(new BlockUpdate_S2CPacket(var5, var6, var7, var2));
            if (var8 == 0) {
                --var6;
            }

            if (var8 == 1) {
                ++var6;
            }

            if (var8 == 2) {
                --var7;
            }

            if (var8 == 3) {
                ++var7;
            }

            if (var8 == 4) {
                --var5;
            }

            if (var8 == 5) {
                ++var5;
            }

            this.player.field_2823.sendPacket(new BlockUpdate_S2CPacket(var5, var6, var7, var2));
        }

        var3 = this.player.inventory.getMainHandStack();
        if (var3 != null && var3.count == 0) {
            this.player.inventory.main[this.player.inventory.selectedSlot] = null;
            var3 = null;
        }

        if (var3 == null || var3.getMaxUseTime() == 0) {
            this.player.skipPacketSlotUpdates = true;
            this.player.inventory.main[this.player.inventory.selectedSlot] = ItemStack.copyOf(this.player.inventory.main[this.player.inventory.selectedSlot]);
            Slot var13 = this.player.openScreenHandler.method_3255(this.player.inventory, this.player.inventory.selectedSlot);
            this.player.openScreenHandler.sendContentUpdates();
            this.player.skipPacketSlotUpdates = false;
            if (!ItemStack.equalsAll(this.player.inventory.getMainHandStack(), par1Packet15Place.getStack())) {
                this.sendPacket(new ScreenHandlerSlotUpdate_S2CPacket(this.player.openScreenHandler.syncId, var13.id, this.player.inventory.getMainHandStack()));
            }
        }

        var2.field_2821 = false;
    }

    /**
     * @author Minecraft Forge
     */
    @Overwrite
    public void onChatMessage(ChatMessage_S2CPacket par1Packet3Chat) {
        par1Packet3Chat = FMLNetworkHandler.handleChatMessage(this, par1Packet3Chat);
        if (this.player.getChatFilterLevel() == 2) {
            this.sendPacket(new ChatMessage_S2CPacket("Cannot send chat message."));
        } else {
            String var2 = par1Packet3Chat.message;
            if (var2.length() > 100) {
                this.disconnect("Chat message too long");
            } else {
                var2 = var2.trim();

                for(int var3 = 0; var3 < var2.length(); ++var3) {
                    if (!SharedConstants.isValidChar(var2.charAt(var3))) {
                        this.disconnect("Illegal characters in chat");
                        return;
                    }
                }

                if (var2.startsWith("/")) {
                    this.executeCommand(var2);
                } else {
                    if (this.player.getChatFilterLevel() == 1) {
                        this.sendPacket(new ChatMessage_S2CPacket("Cannot send chat message."));
                        return;
                    }

                    var2 = "<" + this.player.username + "> " + var2;
                    LOGGER.info(var2);
                    this.server.getPlayerManager().sendToAll(new ChatMessage_S2CPacket(var2, false));
                }

                this.messageCooldown += 20;
                if (this.messageCooldown > 200 && !this.server.getPlayerManager().canCheat(this.player.username)) {
                    this.disconnect("disconnect.spam");
                }
            }
        }

    }

    /**
     * @author Minecraft Forge
     */
    @Overwrite
    public void onClientStatus(ClientStatus_C2SPacket par1Packet205ClientCommand) {
        if (par1Packet205ClientCommand.mode == 1) {
            if (this.player.killedEnderdragon) {
                this.player = this.server.getPlayerManager().respawnPlayer(this.player, 0, true);
            } else if (this.player.getServerWorld().getLevelProperties().isHardcore()) {
                if (this.server.isSinglePlayer() && this.player.username.equals(this.server.getUserName())) {
                    this.player.field_2823.disconnect("You have died. Game over, man, it's game over!");
                    this.server.method_2980();
                } else {
                    BanEntry var2 = new BanEntry(this.player.username);
                    var2.setReason("Death in Hardcore");
                    this.server.getPlayerManager().getBannedIps().add(var2);
                    this.player.field_2823.disconnect("You have died. Game over, man, it's game over!");
                }
            } else {
                if (this.player.method_2600() > 0) {
                    return;
                }

                this.player = this.server.getPlayerManager().respawnPlayer(this.player, this.player._dimension, false);
            }
        }

    }

    @Override
    public void onCustomPayload(CustomPayloadC2SPacket par1Packet250CustomPayload) {
        FMLNetworkHandler.handlePacket250Packet(par1Packet250CustomPayload, this.connection, this);
    }

    @Override
    public void handleVanilla250Packet(CustomPayloadC2SPacket par1Packet250CustomPayload) {
        DataInputStream var2;
        ItemStack var3;
        ItemStack var4;
        if ("MC|BEdit".equals(par1Packet250CustomPayload.channel)) {
            try {
                var2 = new DataInputStream(new ByteArrayInputStream(par1Packet250CustomPayload.field_2455));
                var3 = Packet.readItemStack(var2);
                if (!WritableBookItem.method_3466(var3.getNbt())) {
                    throw new IOException("Invalid book tag!");
                }

                var4 = this.player.inventory.getMainHandStack();
                if (var3 != null && var3.id == Item.WRITABLE_BOOK.id && var3.id == var4.id) {
                    var4.setNbt(var3.getNbt());
                }
            } catch (Exception var9) {
                var9.printStackTrace();
            }
        } else if ("MC|BSign".equals(par1Packet250CustomPayload.channel)) {
            try {
                var2 = new DataInputStream(new ByteArrayInputStream(par1Packet250CustomPayload.field_2455));
                var3 = Packet.readItemStack(var2);
                if (!WrittenBookItem.isValid(var3.getNbt())) {
                    throw new IOException("Invalid book tag!");
                }

                var4 = this.player.inventory.getMainHandStack();
                if (var3 != null && var3.id == Item.WRITTEN_BOOK.id && var4.id == Item.WRITABLE_BOOK.id) {
                    var4.setNbt(var3.getNbt());
                    var4.id = Item.WRITTEN_BOOK.id;
                }
            } catch (Exception var8) {
                var8.printStackTrace();
            }
        } else if ("MC|TrSel".equals(par1Packet250CustomPayload.channel)) {
            try {
                var2 = new DataInputStream(new ByteArrayInputStream(par1Packet250CustomPayload.field_2455));
                int var8 = var2.readInt();
                ScreenHandler var9 = this.player.openScreenHandler;
                if (var9 instanceof VillagerScreenHandler) {
                    ((VillagerScreenHandler)var9).setRecipeIndex(var8);
                }
            } catch (Exception var7) {
                var7.printStackTrace();
            }
        }

    }

    @Override
    public void onMapUpdate(MapUpdate_S2CPacket par1Packet131MapData) {
        FMLNetworkHandler.handlePacket131Packet(this, par1Packet131MapData);
    }

    @Override
    public ServerPlayerEntity getPlayer() {
        return this.player;
    }
}
