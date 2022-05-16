package fr.catcore.fabricatedforge.mixin.forgefml.server.dedicated;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.network.NetworkEncryptionUtils;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.*;
import net.minecraft.server.network.PacketListenerManager;
import net.minecraft.server.network.class_774;
import net.minecraft.server.rcon.QueryResponseHandler;
import net.minecraft.server.rcon.RconServer;
import net.minecraft.util.LogFileWriter;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.GameMode;
import net.minecraft.world.level.LevelGeneratorType;
import net.minecraft.world.level.LevelInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Random;
import java.util.logging.Level;

@Mixin(MinecraftDedicatedServer.class)
public abstract class MinecraftDedicatedServerMixin extends MinecraftServer implements DedicatedServer {
    @Shadow private AbstractPropertiesHandler abstractPropertiesHandler;

    @Shadow private boolean shouldGenerateStructures;

    @Shadow private GameMode field_2736;

    @Shadow private PacketListenerManager field_2737;

    @Shadow private QueryResponseHandler queryResponseHandler;

    @Shadow private RconServer rconServer;

    public MinecraftDedicatedServerMixin(File file) {
        super(file);
    }

    /**
     * @author Minecraft Forge
     */
    @Overwrite
    protected boolean setupServer() {
        try {
            class_772 var1 = class_772Accessor.newInstance((MinecraftDedicatedServer) (Object) this);
            var1.setDaemon(true);
            var1.start();
            LogFileWriter.method_1974();
            field_3848.info("Starting minecraft server version 1.3.2");
            if (Runtime.getRuntime().maxMemory() / 1024L / 1024L < 512L) {
                field_3848.warning("To start the server with more ram, launch it as \"java -Xmx1024M -Xms1024M -jar minecraft_server.jar\"");
            }

            FMLCommonHandler.instance().onServerStart((MinecraftDedicatedServer) (Object) this);
            field_3848.info("Loading properties");
            this.abstractPropertiesHandler = new AbstractPropertiesHandler(new File("server.properties"));
            if (this.isSinglePlayer()) {
                this.setServerIp("127.0.0.1");
            } else {
                this.setOnlineMode(this.abstractPropertiesHandler.getBooleanOrDefault("online-mode", true));
                this.setServerIp(this.abstractPropertiesHandler.getOrDefault("server-ip", ""));
            }

            this.setSpawnAnimals(this.abstractPropertiesHandler.getBooleanOrDefault("spawn-animals", true));
            this.setSpawnNpcs(this.abstractPropertiesHandler.getBooleanOrDefault("spawn-npcs", true));
            this.setPvpEnabled(this.abstractPropertiesHandler.getBooleanOrDefault("pvp", true));
            this.setFlightEnabled(this.abstractPropertiesHandler.getBooleanOrDefault("allow-flight", false));
            this.method_3043(this.abstractPropertiesHandler.getOrDefault("texture-pack", ""));
            this.setMotd(this.abstractPropertiesHandler.getOrDefault("motd", "A Minecraft Server"));
            this.spawnProtectionSize = this.abstractPropertiesHandler.getIntOrDefault("spawn-protection-size", 16);
            this.shouldGenerateStructures = this.abstractPropertiesHandler.getBooleanOrDefault("generate-structures", true);
            int var2 = this.abstractPropertiesHandler.getIntOrDefault("gamemode", GameMode.SURVIVAL.getGameModeId());
            this.field_2736 = LevelInfo.method_3754(var2);
            field_3848.info("Default game type: " + this.field_2736);
            InetAddress var3 = null;
            if (this.getServerIp().length() > 0) {
                var3 = InetAddress.getByName(this.getServerIp());
            }

            if (this.getServerPort() < 0) {
                this.setServerPort(this.abstractPropertiesHandler.getIntOrDefault("server-port", 25565));
            }

            field_3848.info("Generating keypair");
            this.setKeyPair(NetworkEncryptionUtils.generateServerKeyPair());
            field_3848.info("Starting Minecraft server on " + (this.getServerIp().length() == 0 ? "*" : this.getServerIp()) + ":" + this.getServerPort());

            try {
                this.field_2737 = new class_774(this, var3, this.getServerPort());
            } catch (Exception var15) {
                field_3848.warning("**** FAILED TO BIND TO PORT!");
                field_3848.log(Level.WARNING, "The exception was: " + var15.toString());
                field_3848.warning("Perhaps a server is already running on that port?");
                return false;
            }

            if (!this.isOnlineMode()) {
                field_3848.warning("**** SERVER IS RUNNING IN OFFLINE/INSECURE MODE!");
                field_3848.warning("The server will make no attempt to authenticate usernames. Beware.");
                field_3848.warning("While this makes the game possible to play without internet access, it also opens up the ability for hackers to connect with any username they choose.");
                field_3848.warning("To change this, set \"online-mode\" to \"true\" in the server.properties file.");
            }

            FMLCommonHandler.instance().onServerStarted();
            this.setPlayerManager(new DedicatedPlayerManager((MinecraftDedicatedServer) (Object) this));
            long var4 = System.nanoTime();
            if (this.getLevelName() == null) {
                this.setLevelName(this.abstractPropertiesHandler.getOrDefault("level-name", "world"));
            }

            String var6 = this.abstractPropertiesHandler.getOrDefault("level-seed", "");
            String var7 = this.abstractPropertiesHandler.getOrDefault("level-type", "DEFAULT");
            long var8 = (new Random()).nextLong();
            if (var6.length() > 0) {
                try {
                    long var10 = Long.parseLong(var6);
                    if (var10 != 0L) {
                        var8 = var10;
                    }
                } catch (NumberFormatException var14) {
                    var8 = (long) var6.hashCode();
                }
            }

            LevelGeneratorType var16 = LevelGeneratorType.getTypeFromName(var7);
            if (var16 == null) {
                var16 = LevelGeneratorType.DEFAULT;
            }

            this.setWorldHeight(this.abstractPropertiesHandler.getIntOrDefault("max-build-height", 256));
            this.setWorldHeight((this.getWorldHeight() + 8) / 16 * 16);
            this.setWorldHeight(MathHelper.clamp(this.getWorldHeight(), 64, 256));
            this.abstractPropertiesHandler.set("max-build-height", this.getWorldHeight());
            field_3848.info("Preparing level \"" + this.getLevelName() + "\"");
            this.method_2995(this.getLevelName(), this.getLevelName(), var8, var16);
            long var11 = System.nanoTime() - var4;
            String var13 = String.format("%.3fs", (double) var11 / 1.0E9);
            field_3848.info("Done (" + var13 + ")! For help, type \"help\" or \"?\"");
            if (this.abstractPropertiesHandler.getBooleanOrDefault("enable-query", false)) {
                field_3848.info("Starting GS4 status listener");
                this.queryResponseHandler = new QueryResponseHandler(this);
                this.queryResponseHandler.start();
            }

            if (this.abstractPropertiesHandler.getBooleanOrDefault("enable-rcon", false)) {
                field_3848.info("Starting remote control listener");
                this.rconServer = new RconServer(this);
                this.rconServer.start();
            }

            FMLCommonHandler.instance().handleServerStarting(this);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
