/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.0.
 */
package net.minecraftforge.common;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.*;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import fr.catcore.fabricatedforge.mixininterface.IServerWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;

public class ForgeChunkManager {
    private static int defaultMaxCount;
    private static int defaultMaxChunks;
    private static boolean overridesEnabled;
    private static Map<World, Multimap<String, ForgeChunkManager.Ticket>> tickets = (new MapMaker()).weakKeys().makeMap();
    private static Map<String, Integer> ticketConstraints = Maps.newHashMap();
    private static Map<String, Integer> chunkConstraints = Maps.newHashMap();
    private static SetMultimap<String, ForgeChunkManager.Ticket> playerTickets = HashMultimap.create();
    private static Map<String, ForgeChunkManager.LoadingCallback> callbacks = Maps.newHashMap();
    private static Map<World, ImmutableSetMultimap<ChunkPos, ForgeChunkManager.Ticket>> forcedChunks = (new MapMaker()).weakKeys().makeMap();
    private static BiMap<UUID, ForgeChunkManager.Ticket> pendingEntities = HashBiMap.create();
    private static Map<World, Cache<Long, Chunk>> dormantChunkCache = (new MapMaker()).weakKeys().makeMap();
    private static File cfgFile;
    private static Configuration config;
    private static int playerTicketLength;
    private static int dormantChunkCacheSize;

    public ForgeChunkManager() {
    }

    static void loadWorld(World world) {
        ArrayListMultimap<String, ForgeChunkManager.Ticket> newTickets = ArrayListMultimap.create();
        ForgeChunkManager.tickets.put(world, newTickets);
        forcedChunks.put(world, ImmutableSetMultimap.of());
        if (world instanceof ServerWorld) {
            dormantChunkCache.put(world, CacheBuilder.newBuilder().maximumSize((long)dormantChunkCacheSize).build());
            ServerWorld worldServer = (ServerWorld)world;
            File chunkDir = ((IServerWorld)worldServer).getChunkSaveLocation();
            File chunkLoaderData = new File(chunkDir, "forcedchunks.dat");
            if (chunkLoaderData.exists() && chunkLoaderData.isFile()) {
                ArrayListMultimap<String, ForgeChunkManager.Ticket> loadedTickets = ArrayListMultimap.create();
                ArrayListMultimap<String, ForgeChunkManager.Ticket> playerLoadedTickets = ArrayListMultimap.create();

                NbtCompound forcedChunkData;
                try {
                    forcedChunkData = NbtIo.method_1349(chunkLoaderData);
                } catch (Exception var20) {
                    FMLLog.log(Level.WARNING, var20, "Unable to read forced chunk data at %s - it will be ignored", chunkLoaderData.getAbsolutePath());
                    return;
                }

                NbtList ticketList = forcedChunkData.getList("TicketList");

                for(int i = 0; i < ticketList.size(); ++i) {
                    NbtCompound ticketHolder = (NbtCompound)ticketList.method_1218(i);
                    String modId = ticketHolder.getString("Owner");
                    boolean isPlayer = "Forge".equals(modId);
                    if (!isPlayer && !Loader.isModLoaded(modId)) {
                        FMLLog.warning("Found chunkloading data for mod %s which is currently not available or active - it will be removed from the world save", modId);
                    } else if (!isPlayer && !callbacks.containsKey(modId)) {
                        FMLLog.warning("The mod %s has registered persistent chunkloading data but doesn't seem to want to be called back with it - it will be removed from the world save", modId);
                    } else {
                        NbtList tickets = ticketHolder.getList("Tickets");

                        for(int j = 0; j < tickets.size(); ++j) {
                            NbtCompound ticket = (NbtCompound)tickets.method_1218(j);
                            modId = ticket.contains("ModId") ? ticket.getString("ModId") : modId;
                            ForgeChunkManager.Type type = ForgeChunkManager.Type.values()[ticket.getByte("Type")];
                            byte ticketChunkDepth = ticket.getByte("ChunkListDepth");
                            ForgeChunkManager.Ticket tick = new ForgeChunkManager.Ticket(modId, type, world);
                            if (ticket.contains("ModData")) {
                                tick.modData = ticket.getCompound("ModData");
                            }

                            if (ticket.contains("Player")) {
                                tick.player = ticket.getString("Player");
                                playerLoadedTickets.put(tick.modId, tick);
                                playerTickets.put(tick.player, tick);
                            } else {
                                loadedTickets.put(modId, tick);
                            }

                            if (type == ForgeChunkManager.Type.ENTITY) {
                                tick.entityChunkX = ticket.getInt("chunkX");
                                tick.entityChunkZ = ticket.getInt("chunkZ");
                                UUID uuid = new UUID(ticket.getLong("PersistentIDMSB"), ticket.getLong("PersistentIDLSB"));
                                pendingEntities.put(uuid, tick);
                            }
                        }
                    }
                }

                for (Ticket tick : ImmutableSet.copyOf(pendingEntities.values()))
                {
                    if (tick.ticketType == Type.ENTITY && tick.entity == null)
                    {
                        // force the world to load the entity's chunk
                        // the load will come back through the loadEntity method and attach the entity
                        // to the ticket
                        world.getChunk(tick.entityChunkX, tick.entityChunkZ);
                    }
                }
                for (Ticket tick : ImmutableSet.copyOf(pendingEntities.values()))
                {
                    if (tick.ticketType == Type.ENTITY && tick.entity == null)
                    {
                        FMLLog.warning("Failed to load persistent chunkloading entity %s from store.", pendingEntities.inverse().get(tick));
                        loadedTickets.remove(tick.modId, tick);
                    }
                }

                pendingEntities.clear();
                // send callbacks
                for (String modId : loadedTickets.keySet())
                {
                    LoadingCallback loadingCallback = callbacks.get(modId);
                    int maxTicketLength = getMaxTicketLengthFor(modId);
                    List<Ticket> tickets = loadedTickets.get(modId);
                    if (loadingCallback instanceof OrderedLoadingCallback)
                    {
                        OrderedLoadingCallback orderedLoadingCallback = (OrderedLoadingCallback) loadingCallback;
                        tickets = orderedLoadingCallback.ticketsLoaded(ImmutableList.copyOf(tickets), world, maxTicketLength);
                    }
                    if (tickets.size() > maxTicketLength)
                    {
                        FMLLog.warning("The mod %s has too many open chunkloading tickets %d. Excess will be dropped", modId, tickets.size());
                        tickets.subList(maxTicketLength, tickets.size()).clear();
                    }
                    ForgeChunkManager.tickets.get(world).putAll(modId, tickets);
                    loadingCallback.ticketsLoaded(ImmutableList.copyOf(tickets), world);
                }
                for (String modId : playerLoadedTickets.keySet())
                {
                    LoadingCallback loadingCallback = callbacks.get(modId);
                    List<Ticket> tickets = playerLoadedTickets.get(modId);
                    ForgeChunkManager.tickets.get(world).putAll("Forge", tickets);
                    loadingCallback.ticketsLoaded(ImmutableList.copyOf(tickets), world);
                }
            }

        }
    }

    public static void setForcedChunkLoadingCallback(Object mod, ForgeChunkManager.LoadingCallback callback) {
        ModContainer container = getContainer(mod);
        if (container == null) {
            FMLLog.warning("Unable to register a callback for an unknown mod %s (%s : %x)", mod, mod.getClass().getName(), System.identityHashCode(mod));
        } else {
            callbacks.put(container.getModId(), callback);
        }
    }

    public static int ticketCountAvailableFor(Object mod, World world) {
        ModContainer container = getContainer(mod);
        if (container != null) {
            String modId = container.getModId();
            int allowedCount = getMaxTicketLengthFor(modId);
            return allowedCount - ((Multimap)tickets.get(world)).get(modId).size();
        } else {
            return 0;
        }
    }

    private static ModContainer getContainer(Object mod) {
        return Loader.instance().getModObjectList().inverse().get(mod);
    }

    private static int getMaxTicketLengthFor(String modId) {
        return ticketConstraints.containsKey(modId) && overridesEnabled ? (Integer)ticketConstraints.get(modId) : defaultMaxCount;
    }

    private static int getMaxChunkDepthFor(String modId) {
        return chunkConstraints.containsKey(modId) && overridesEnabled ? (Integer)chunkConstraints.get(modId) : defaultMaxChunks;
    }

    public static ForgeChunkManager.Ticket requestPlayerTicket(Object mod, PlayerEntity player, World world, ForgeChunkManager.Type type) {
        ModContainer mc = getContainer(mod);
        if (mc == null) {
            FMLLog.log(Level.SEVERE, "Failed to locate the container for mod instance %s (%s : %x)", mod, mod.getClass().getName(), System.identityHashCode(mod));
            return null;
        } else if (playerTickets.get(player.getTranslationKey()).size() > playerTicketLength) {
            FMLLog.warning("Unable to assign further chunkloading tickets to player %s (on behalf of mod %s)", player.getTranslationKey(), mc.getModId());
            return null;
        } else {
            ForgeChunkManager.Ticket ticket = new ForgeChunkManager.Ticket(mc.getModId(), type, world, player);
            playerTickets.put(player.getTranslationKey(), ticket);
            tickets.get(world).put("Forge", ticket);
            return ticket;
        }
    }

    public static ForgeChunkManager.Ticket requestTicket(Object mod, World world, ForgeChunkManager.Type type) {
        ModContainer container = getContainer(mod);
        if (container == null) {
            FMLLog.log(Level.SEVERE, "Failed to locate the container for mod instance %s (%s : %x)", mod, mod.getClass().getName(), System.identityHashCode(mod));
            return null;
        } else {
            String modId = container.getModId();
            if (!callbacks.containsKey(modId)) {
                FMLLog.severe("The mod %s has attempted to request a ticket without a listener in place", modId);
                throw new RuntimeException("Invalid ticket request");
            } else {
                int allowedCount = ticketConstraints.containsKey(modId) ? ticketConstraints.get(modId) : defaultMaxCount;
                if (tickets.computeIfAbsent(world, (world1) -> ArrayListMultimap.create()).get(modId).size() >= allowedCount) {
                    FMLLog.info("The mod %s has attempted to allocate a chunkloading ticket beyond it's currently allocated maximum : %d", modId, allowedCount);
                    return null;
                } else {
                    ForgeChunkManager.Ticket ticket = new ForgeChunkManager.Ticket(modId, type, world);
                    tickets.get(world).put(modId, ticket);
                    return ticket;
                }
            }
        }
    }

    public static void releaseTicket(ForgeChunkManager.Ticket ticket) {
        if (ticket != null) {
            if (ticket.isPlayerTicket()) {
                if (!playerTickets.containsValue(ticket)) {
                    return;
                }
            } else if (!tickets.get(ticket.world).containsEntry(ticket.modId, ticket)) {
                return;
            }

            if (ticket.requestedChunks != null) {
                for (ChunkPos chunk : ImmutableSet.copyOf(ticket.requestedChunks))
                {
                    unforceChunk(ticket, chunk);
                }
            }

            if (ticket.isPlayerTicket()) {
                playerTickets.remove(ticket.player, ticket);
                tickets.get(ticket.world).remove("Forge", ticket);
            } else {
                tickets.get(ticket.world).remove(ticket.modId, ticket);
            }

        }
    }

    public static void forceChunk(ForgeChunkManager.Ticket ticket, ChunkPos chunk) {
        if (ticket == null || chunk == null)
        {
            return;
        }
        if (ticket.ticketType == Type.ENTITY && ticket.entity == null)
        {
            throw new RuntimeException("Attempted to use an entity ticket to force a chunk, without an entity");
        }
        if (ticket.isPlayerTicket() ? !playerTickets.containsValue(ticket) : !tickets.get(ticket.world).containsEntry(ticket.modId, ticket))
        {
            FMLLog.severe("The mod %s attempted to force load a chunk with an invalid ticket. This is not permitted.", ticket.modId);
            return;
        }
        ticket.requestedChunks.add(chunk);
        ImmutableSetMultimap<ChunkPos, Ticket> oldMap = forcedChunks.get(ticket.world);
        ImmutableSetMultimap<ChunkPos, Ticket> newMap = ImmutableSetMultimap.<ChunkPos,Ticket>builder().putAll(oldMap == null ? ImmutableSetMultimap.<ChunkPos,Ticket>builder().build() : oldMap).put(chunk, ticket).build();
        forcedChunks.put(ticket.world, newMap);
        if (ticket.maxDepth > 0 && ticket.requestedChunks.size() > ticket.maxDepth)
        {
            ChunkPos removed = ticket.requestedChunks.iterator().next();
            unforceChunk(ticket,removed);
        }
    }

    public static void reorderChunk(ForgeChunkManager.Ticket ticket, ChunkPos chunk) {
        if (ticket != null && chunk != null && ticket.requestedChunks.contains(chunk)) {
            ticket.requestedChunks.remove(chunk);
            ticket.requestedChunks.add(chunk);
        }
    }

    public static void unforceChunk(ForgeChunkManager.Ticket ticket, ChunkPos chunk) {
        if (ticket != null && chunk != null) {
            ticket.requestedChunks.remove(chunk);
            LinkedHashMultimap<ChunkPos, ForgeChunkManager.Ticket> copy = LinkedHashMultimap.create(forcedChunks.get(ticket.world));
            copy.remove(chunk, ticket);
            ImmutableSetMultimap<ChunkPos, ForgeChunkManager.Ticket> newMap = ImmutableSetMultimap.copyOf(copy);
            forcedChunks.put(ticket.world, newMap);
        }
    }

    static void loadConfiguration() {
        for (String mod : config.categories.keySet())
        {
            if (mod.equals("Forge") || mod.equals("defaults"))
            {
                continue;
            }
            Property modTC = config.get(mod, "maximumTicketCount", 200);
            Property modCPT = config.get(mod, "maximumChunksPerTicket", 25);
            ticketConstraints.put(mod, modTC.getInt(200));
            chunkConstraints.put(mod, modCPT.getInt(25));
        }
        config.save();
    }

    public static SetMultimap<ChunkPos, ForgeChunkManager.Ticket> getPersistentChunksFor(World world) {
        return forcedChunks.containsKey(world) ? forcedChunks.get(world) : ImmutableSetMultimap.of();
    }

    static void saveWorld(World world) {
        // only persist persistent worlds
        if (!(world instanceof ServerWorld)) { return; }
        ServerWorld worldServer = (ServerWorld) world;
        File chunkDir = ((IServerWorld)worldServer).getChunkSaveLocation();
        File chunkLoaderData = new File(chunkDir, "forcedchunks.dat");

        NbtCompound forcedChunkData = new NbtCompound();
        NbtList ticketList = new NbtList();
        forcedChunkData.put("TicketList", ticketList);

        Multimap<String, Ticket> ticketSet = tickets.get(worldServer);
        for (String modId : ticketSet.keySet())
        {
            NbtCompound ticketHolder = new NbtCompound();
            ticketList.method_1217(ticketHolder);

            ticketHolder.putString("Owner", modId);
            NbtList tickets = new NbtList();
            ticketHolder.put("Tickets", tickets);

            for (Ticket tick : ticketSet.get(modId))
            {
                NbtCompound ticket = new NbtCompound();
                ticket.putByte("Type", (byte) tick.ticketType.ordinal());
                ticket.putByte("ChunkListDepth", (byte) tick.maxDepth);
                if (tick.isPlayerTicket())
                {
                    ticket.putString("ModId", tick.modId);
                    ticket.putString("Player", tick.player);
                }
                if (tick.modData != null)
                {
                    ticket.put("ModData", tick.modData);
                }
                if (tick.ticketType == Type.ENTITY && tick.entity != null)
                {
                    ticket.putInt("chunkX", MathHelper.floor(tick.entity.chunkX));
                    ticket.putInt("chunkZ", MathHelper.floor(tick.entity.chunkZ));
                    ticket.putLong("PersistentIDMSB", tick.entity.getPersistentID().getMostSignificantBits());
                    ticket.putLong("PersistentIDLSB", tick.entity.getPersistentID().getLeastSignificantBits());
                    tickets.method_1217(ticket);
                }
                else if (tick.ticketType != Type.ENTITY)
                {
                    tickets.method_1217(ticket);
                }
            }
        }

        try
        {
            NbtIo.write(forcedChunkData, chunkLoaderData);
        }
        catch (Exception e)
        {
            FMLLog.log(Level.WARNING, e, "Unable to write forced chunk data to %s - chunkloading won't work", chunkLoaderData.getAbsolutePath());
        }
    }

    static void loadEntity(Entity entity) {
        UUID id = entity.getPersistentID();
        ForgeChunkManager.Ticket tick = pendingEntities.get(id);
        if (tick != null) {
            tick.bindEntity(entity);
            pendingEntities.remove(id);
        }

    }

    public static void putDormantChunk(long coords, Chunk chunk) {
        Cache<Long, Chunk> cache = dormantChunkCache.get(chunk.world);
        if (cache != null) {
            cache.put(coords, chunk);
        }

    }

    public static Chunk fetchDormantChunk(long coords, World world) {
        Cache<Long, Chunk> cache = dormantChunkCache.get(world);
        return cache == null ? null : cache.getIfPresent(coords);
    }

    static void captureConfig(File configDir) {
        cfgFile = new File(configDir, "forgeChunkLoading.cfg");
        config = new Configuration(cfgFile, true);
        config.categories.clear();

        try {
            config.load();
        } catch (Exception var11) {
            File dest = new File(cfgFile.getParentFile(), "forgeChunkLoading.cfg.bak");
            if (dest.exists()) {
                dest.delete();
            }

            cfgFile.renameTo(dest);
            FMLLog.log(Level.SEVERE, var11, "A critical error occured reading the forgeChunkLoading.cfg file, defaults will be used - the invalid file is backed up at forgeChunkLoading.cfg.bak");
        }

        config.addCustomCategoryComment("defaults", "Default configuration for forge chunk loading control");
        Property maxTicketCount = config.get("defaults", "maximumTicketCount", 200);
        maxTicketCount.comment = "The default maximum ticket count for a mod which does not have an override\nin this file. This is the number of chunk loading requests a mod is allowed to make.";
        defaultMaxCount = maxTicketCount.getInt(200);
        Property maxChunks = config.get("defaults", "maximumChunksPerTicket", 25);
        maxChunks.comment = "The default maximum number of chunks a mod can force, per ticket, \nfor a mod without an override. This is the maximum number of chunks a single ticket can force.";
        defaultMaxChunks = maxChunks.getInt(25);
        Property playerTicketCount = config.get("defaults", "playetTicketCount", 500);
        playerTicketCount.comment = "The number of tickets a player can be assigned instead of a mod. This is shared across all mods and it is up to the mods to use it.";
        playerTicketLength = playerTicketCount.getInt(500);
        Property dormantChunkCacheSizeProperty = config.get("defaults", "dormantChunkCacheSize", 0);
        dormantChunkCacheSizeProperty.comment = "Unloaded chunks can first be kept in a dormant cache for quicker\nloading times. Specify the size of that cache here";
        dormantChunkCacheSize = dormantChunkCacheSizeProperty.getInt(0);
        FMLLog.info("Configured a dormant chunk cache size of %d", dormantChunkCacheSizeProperty.getInt(0));
        Property modOverridesEnabled = config.get("defaults", "enabled", true);
        modOverridesEnabled.comment = "Are mod overrides enabled?";
        overridesEnabled = modOverridesEnabled.getBoolean(true);
        config.addCustomCategoryComment("Forge", "Sample mod specific control section.\nCopy this section and rename the with the modid for the mod you wish to override.\nA value of zero in either entry effectively disables any chunkloading capabilities\nfor that mod");
        Property sampleTC = config.get("Forge", "maximumTicketCount", 200);
        sampleTC.comment = "Maximum ticket count for the mod. Zero disables chunkloading capabilities.";
        sampleTC = config.get("Forge", "maximumChunksPerTicket", 25);
        sampleTC.comment = "Maximum chunks per ticket for the mod.";

        for (String mod : config.categories.keySet())
        {
            if (mod.equals("Forge") || mod.equals("defaults"))
            {
                continue;
            }
            Property modTC = config.get(mod, "maximumTicketCount", 200);
            Property modCPT = config.get(mod, "maximumChunksPerTicket", 25);
        }
    }

    public static Map<String, Property> getConfigMapFor(Object mod) {
        ModContainer container = getContainer(mod);
        if (container != null) {
            return config.categories.computeIfAbsent(container.getModId(), k -> Maps.newHashMap());
        } else {
            return null;
        }
    }

    public static void addConfigProperty(Object mod, String propertyName, String value, Property.Type type) {
        ModContainer container = getContainer(mod);
        if (container != null) {
            Map<String, Property> props = config.categories.get(container.getModId());
            props.put(propertyName, new Property(propertyName, value, type));
        }

    }

    public static class Ticket {
        private String modId;
        private ForgeChunkManager.Type ticketType;
        private LinkedHashSet<ChunkPos> requestedChunks;
        private NbtCompound modData;
        private World world;
        private int maxDepth;
        private String entityClazz;
        private int entityChunkX;
        private int entityChunkZ;
        private Entity entity;
        private String player;

        Ticket(String modId, ForgeChunkManager.Type type, World world) {
            this.modId = modId;
            this.ticketType = type;
            this.world = world;
            this.maxDepth = ForgeChunkManager.getMaxChunkDepthFor(modId);
            this.requestedChunks = Sets.newLinkedHashSet();
        }

        Ticket(String modId, ForgeChunkManager.Type type, World world, PlayerEntity player) {
            this(modId, type, world);
            if (player != null) {
                this.player = player.getTranslationKey();
            } else {
                FMLLog.log(Level.SEVERE, "Attempt to create a player ticket without a valid player");
                throw new RuntimeException();
            }
        }

        public void setChunkListDepth(int depth) {
            if (depth <= ForgeChunkManager.getMaxChunkDepthFor(this.modId) && (depth > 0 || ForgeChunkManager.getMaxChunkDepthFor(this.modId) <= 0)) {
                this.maxDepth = depth;
            } else {
                FMLLog.warning("The mod %s tried to modify the chunk ticket depth to: %d, its allowed maximum is: %d", this.modId, depth, ForgeChunkManager.getMaxChunkDepthFor(this.modId));
            }

        }

        public int getChunkListDepth() {
            return this.maxDepth;
        }

        public int getMaxChunkListDepth() {
            return ForgeChunkManager.getMaxChunkDepthFor(this.modId);
        }

        public void bindEntity(Entity entity) {
            if (this.ticketType != ForgeChunkManager.Type.ENTITY) {
                throw new RuntimeException("Cannot bind an entity to a non-entity ticket");
            } else {
                this.entity = entity;
            }
        }

        public NbtCompound getModData() {
            if (this.modData == null) {
                this.modData = new NbtCompound();
            }

            return this.modData;
        }

        public Entity getEntity() {
            return this.entity;
        }

        public boolean isPlayerTicket() {
            return this.player != null;
        }

        public String getPlayerName() {
            return this.player;
        }

        public String getModId() {
            return this.modId;
        }

        public ForgeChunkManager.Type getType() {
            return this.ticketType;
        }

        public ImmutableSet getChunkList() {
            return ImmutableSet.copyOf(this.requestedChunks);
        }
    }

    public static enum Type {
        NORMAL,
        ENTITY;

        private Type() {
        }
    }

    public interface OrderedLoadingCallback extends ForgeChunkManager.LoadingCallback {
        List<ForgeChunkManager.Ticket> ticketsLoaded(List<ForgeChunkManager.Ticket> list, World arg, int i);
    }

    public interface LoadingCallback {
        void ticketsLoaded(List<ForgeChunkManager.Ticket> list, World arg);
    }
}
