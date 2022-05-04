package modloader;

import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.class_469;
import net.minecraft.client.class_535;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.class_481;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
import net.minecraft.network.packet.s2c.play.EntitySpawn_S2CPacket;
import net.minecraft.server.ServerPacketListener;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

import java.util.Map;
import java.util.Random;


@SuppressWarnings("unused")
public abstract class BaseMod {
    public BaseMod() {
    }

    public int addFuel(int i, int j) {
        return 0;
    }

    public void addRenderer(Map map) {
    }

    public void generateNether(World world, Random random, int i, int j) {
    }

    public void generateSurface(World world, Random random, int i, int j) {
    }

    public String getName() {
        return this.getClass().getSimpleName();
    }

    public String getPriorities() {
        return "";
    }

    public abstract String getVersion();

    public void keyboardEvent(KeyBinding keybinding) {
    }

    public abstract void load();

    public void modsLoaded() {
    }

    public void onItemPickup(PlayerEntity entityplayer, ItemStack itemstack) {
    }

    public boolean onTickInGame(float f, MinecraftClient minecraft) {
        return false;
    }

    public boolean onTickInGUI(float f, MinecraftClient minecraft, Screen guiscreen) {
        return false;
    }

    public void clientChat(String s) {
    }

    public void serverChat(ServerPacketListener netserverhandler, String s) {
    }

    public void clientCustomPayload(class_469 clientHandler, CustomPayloadC2SPacket packet250custompayload) {
    }

    public void serverCustomPayload(ServerPacketListener serverHandler, CustomPayloadC2SPacket packet250custompayload) {
    }

    public void registerAnimation(MinecraftClient minecraft) {
    }

    public void renderInvBlock(class_535 renderblocks, Block block, int i, int j) {
    }

    public boolean renderWorldBlock(class_535 renderblocks, WorldView iblockaccess, int i, int j, int k, Block block, int l) {
        return false;
    }

    public void clientConnect(class_469 netclienthandler) {
    }

    public void clientDisconnect(class_469 clientHandler) {
    }

    public void takenFromCrafting(PlayerEntity entityplayer, ItemStack itemstack, Inventory iinventory) {
    }

    public void takenFromFurnace(PlayerEntity entityplayer, ItemStack itemstack) {
    }

    public String toString() {
        return this.getName() + ' ' + this.getVersion();
    }

    public HandledScreen getContainerGUI(class_481 player, int inventoryType, int x, int y, int z) {
        return null;
    }

    public Entity spawnEntity(int id, World world, double x, double y, double z) {
        return null;
    }

    public EntitySpawn_S2CPacket getSpawnPacket(Entity entity, int id) {
        return null;
    }
}
