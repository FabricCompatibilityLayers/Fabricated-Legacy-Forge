package modloader;

import fr.catcore.fabricatedmodloader.mixin.modloader.common.BlockEntityAccessor;
import fr.catcore.fabricatedmodloader.mixin.modloader.common.EntityTypeAccessor;
import fr.catcore.fabricatedmodloader.mixin.modloader.client.PlayerEntityRendererAccessor;
import fr.catcore.fabricatedmodloader.remapping.RemapUtil;
import fr.catcore.fabricatedmodloader.utils.Constants;
import fr.catcore.fabricatedmodloader.utils.SetBaseBiomesLayerData;
import fr.catcore.fabricatedmodloader.utils.class_535Data;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.impl.util.log.Log;
import net.minecraft.advancement.Achievement;
import net.minecraft.block.Block;
import net.minecraft.client.*;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.texture.TexturePackManager;
import net.minecraft.command.Command;
import net.minecraft.entity.*;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.player.class_481;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.class_690;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
import net.minecraft.network.packet.s2c.play.OpenScreen_S2CPacket;
import net.minecraft.recipe.RecipeDispatcher;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SmeltingRecipeRegistry;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerPacketListener;
import net.minecraft.server.command.CommandRegistry;
import net.minecraft.server.command.CommandRegistryProvider;
import net.minecraft.stat.CraftingStat;
import net.minecraft.stat.Stat;
import net.minecraft.stat.Stats;
import net.minecraft.util.CommonI18n;
import net.minecraft.util.Language;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.EndBiome;
import net.minecraft.world.biome.NetherBiome;
import net.minecraft.world.chunk.ChunkProvider;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.DigestException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public final class ModLoader {
    private static final List animList = new LinkedList();
    private static final Map blockModels = new HashMap();
    private static final Map blockSpecialInv = new HashMap();
    private static final File cfgdir = new File(FabricLoader.getInstance().getGameDir().toFile(), "/config/");
    private static final File cfgfile;
    public static Level cfgLoggingLevel;
    private static Map classMap = null;
    private static long clock = 0L;
    private static Field field_animList = null;
    private static Field field_modifiers = null;
    private static Field field_TileEntityRenderers = null;
    private static boolean hasInit = false;
    private static int highestEntityId = 3000;
    private static final Map inGameHooks = new HashMap();
    private static final Map inGUIHooks = new HashMap();
    private static Minecraft instance = null;
    private static int itemSpriteIndex = 0;
    private static int itemSpritesLeft = 0;
    private static final Map keyList = new HashMap();
    private static String langPack = null;
    private static Map localizedStrings = new HashMap();
    private static final File logfile = new File(FabricLoader.getInstance().getGameDir().toFile(), "ModLoader.txt");
    private static final Logger logger = Logger.getLogger("ModLoader");
    private static FileHandler logHandler = null;
    private static final File modDir = new File(FabricLoader.getInstance().getGameDir().toFile(), "/mods/");
    private static final LinkedList modList = new LinkedList();
    private static int nextBlockModelID = 1000;
    private static final Map overrides = new HashMap();
    private static final Map packetChannels = new HashMap();
    public static final Properties props = new Properties();
    private static Biome[] standardBiomes;
    private static int terrainSpriteIndex = 0;
    private static int terrainSpritesLeft = 0;
    private static String texPack = null;
    private static boolean texturesAdded = false;
    private static final boolean[] usedItemSprites = new boolean[256];
    private static final boolean[] usedTerrainSprites = new boolean[256];
    public static final String VERSION = "ModLoader 1.3.2";
    private static class_469 clientHandler = null;
    private static final List commandList = new LinkedList();
    private static final Map tradeItems = new HashMap();
    private static final Map containerGUIs = new HashMap();
    private static final Map trackers = new HashMap();

    public static void addAchievementDesc(Achievement achievement, String s, String s1) {
        try {
            if (achievement.getStringId().contains(".")) {
                String[] as = achievement.getStringId().split("\\.");
                if (as.length == 2) {
                    String s2 = as[1];
                    addLocalization("achievement." + s2, s);
                    addLocalization("achievement." + s2 + ".desc", s1);
                    setPrivateValue(Stat.class, achievement, 1, CommonI18n.translate("achievement." + s2));
                    setPrivateValue(Achievement.class, achievement, 3, CommonI18n.translate("achievement." + s2 + ".desc"));
                } else {
                    setPrivateValue(Stat.class, achievement, 1, s);
                    setPrivateValue(Achievement.class, achievement, 3, s1);
                }
            } else {
                setPrivateValue(Stat.class, achievement, 1, s);
                setPrivateValue(Achievement.class, achievement, 3, s1);
            }
        } catch (IllegalArgumentException var5) {
            logger.throwing("ModLoader", "AddAchievementDesc", var5);
            throwException(var5);
        } catch (SecurityException var6) {
            logger.throwing("ModLoader", "AddAchievementDesc", var6);
            throwException(var6);
        } catch (NoSuchFieldException var7) {
            logger.throwing("ModLoader", "AddAchievementDesc", var7);
            throwException(var7);
        }

    }

    public static void addEntityTracker(BaseMod mod, Class entityClass, int id, int viewDistance, int updateFrequency, boolean trackMotion) {
        if (entityClass == null) {
            throw new IllegalArgumentException();
        } else {
            if (!Entity.class.isAssignableFrom(entityClass)) {
                Exception exception = new Exception(entityClass.getCanonicalName() + " is not an entity.");
                logger.throwing("ModLoader", "addEntityTracker", exception);
                throwException(exception);
            }

            trackers.put(entityClass, new EntityTrackerNonliving(mod, entityClass, id, viewDistance, updateFrequency, trackMotion));
        }
    }

    public static Map getTrackers() {
        return Collections.unmodifiableMap(trackers);
    }

    public static int addAllFuel(int id, int metadata) {
        logger.finest("Finding fuel for " + id);
        int result = 0;

        for(Iterator iter = modList.iterator(); iter.hasNext() && result == 0; result = ((BaseMod)iter.next()).addFuel(id, metadata)) {
        }

        if (result != 0) {
            logger.finest("Returned " + result);
        }

        return result;
    }

    public static void addAllRenderers(Map renderers) {
        if (!hasInit) {
            init();
            logger.fine("Initialized");
        }

        Iterator i$ = modList.iterator();

        while(i$.hasNext()) {
            BaseMod mod = (BaseMod)i$.next();
            mod.addRenderer(renderers);
        }

    }

    public static void addAnimation(class_584 anim) {
        logger.finest("Adding animation " + anim.toString());
        Iterator i$ = animList.iterator();

        while(i$.hasNext()) {
            class_584 oldAnim = (class_584)i$.next();
            if (oldAnim.field_2153 == anim.field_2153 && oldAnim.field_2157 == anim.field_2157) {
                animList.remove(anim);
                break;
            }
        }

        animList.add(anim);
    }

    public static int addArmor(String s) {
        try {
            String[] as = PlayerEntityRendererAccessor.getArmor();
            List list = Arrays.asList(as);
            ArrayList arraylist = new ArrayList();
            arraylist.addAll(list);
            if (!arraylist.contains(s)) {
                arraylist.add(s);
            }

            int i = arraylist.indexOf(s);
            PlayerEntityRendererAccessor.setArmor((String[]) arraylist.toArray(new String[0]));
            return i;
        } catch (IllegalArgumentException var5) {
            logger.throwing("ModLoader", "AddArmor", var5);
            throwException("An impossible error has occured!", var5);
        }

        return -1;
    }

    public static void addBiome(Biome biomegenbase) {
        Biome[] abiomegenbase = SetBaseBiomesLayerData.biomeArray;
        List list = Arrays.asList(abiomegenbase);
        ArrayList arraylist = new ArrayList();
        arraylist.addAll(list);
        if (!arraylist.contains(biomegenbase)) {
            arraylist.add(biomegenbase);
        }

        SetBaseBiomesLayerData.biomeArray = (Biome[])((Biome[])arraylist.toArray(new Biome[0]));
    }

    public static void addCommand(Command cmd) {
        commandList.add(cmd);
    }

    public static void registerCommands(MinecraftServer server) {
        CommandRegistryProvider manager = server.getCommandManager();
        if (manager instanceof CommandRegistry) {
            CommandRegistry handler = (CommandRegistry)manager;
            Iterator i$ = commandList.iterator();

            while(i$.hasNext()) {
                Command cmd = (Command) i$.next();
                handler.registerCommand(cmd);
            }

        }
    }

    public static void addLocalization(String s, String s1) {
        addLocalization(s, "en_US", s1);
    }

    public static void addLocalization(String s, String s1, String s2) {
        Object obj;
        if (localizedStrings.containsKey(s1)) {
            obj = (Map)localizedStrings.get(s1);
        } else {
            obj = new HashMap();
            localizedStrings.put(s1, obj);
        }

        ((Map)obj).put(s, s2);
    }

    public static void addTrade(int profession, TradeEntry entry) {
        List list = null;
        if (tradeItems.containsKey(profession)) {
            list = (List)tradeItems.get(profession);
        } else {
            list = new LinkedList();
            tradeItems.put(profession, list);
        }

        ((List)list).add(entry);
    }

    public static List getTrades(int profession) {
        if (profession != -1) {
            return tradeItems.containsKey(profession) ? Collections.unmodifiableList((List)tradeItems.get(profession)) : null;
        } else {
            List list = new LinkedList();
            Iterator i$ = tradeItems.values().iterator();

            while(i$.hasNext()) {
                List entry = (List)i$.next();
                list.addAll(entry);
            }

            return list;
        }
    }

    private static void addMod(ClassLoader classloader, String s) {
        try {
            String s1 = s.split("\\.")[0];
            if (s1.contains("$")) {
                return;
            }

            if (props.containsKey(s1) && (props.getProperty(s1).equalsIgnoreCase("no") || props.getProperty(s1).equalsIgnoreCase("off"))) {
                return;
            }

            Class class1 = classloader.loadClass(s1.replace("/", "."));
            if (!BaseMod.class.isAssignableFrom(class1)) {
                return;
            }

            setupProperties(class1);
            BaseMod basemod = (BaseMod)class1.newInstance();
            if (basemod != null) {
                modList.add(basemod);
                logger.fine("Mod Initialized: \"" + basemod.toString() + "\" from " + s);
                System.out.println("Mod Initialized: " + basemod.toString());
            }
        } catch (Throwable var6) {
            logger.fine("Failed to load mod from \"" + s + "\"");
            System.out.println("Failed to load mod from \"" + s + "\"");
            var6.printStackTrace();
            logger.throwing("ModLoader", "addMod", var6);
            throwException(var6);
        }

    }

    public static void addName(Object obj, String s) {
        addName(obj, "en_US", s);
    }

    public static void addName(Object obj, String s, String s1) {
        String s2 = null;
        Exception exception1;
        if (obj instanceof Item) {
            Item item = (Item) obj;
            if (item.getTranslationKey() != null) {
                s2 = item.getTranslationKey() + ".name";
            }
        } else if (obj instanceof Block) {
            Block block = (Block) obj;
            if (block.getTranslationKey() != null) {
                s2 = block.getTranslationKey() + ".name";
            }
        } else if (obj instanceof ItemStack) {
            ItemStack itemstack = (ItemStack)obj;
            String s3 = Item.ITEMS[itemstack.id].getTranslationKey(itemstack);
            if (s3 != null) {
                s2 = s3 + ".name";
            }
        } else {
            exception1 = new Exception(obj.getClass().getName() + " cannot have name attached to it!");
            logger.throwing("ModLoader", "AddName", exception1);
            throwException(exception1);
        }

        if (s2 != null) {
            addLocalization(s2, s, s1);
        } else {
            exception1 = new Exception(obj + " is missing name tag!");
            logger.throwing("ModLoader", "AddName", exception1);
            throwException(exception1);
        }

    }

    public static int addOverride(String s, String s1) {
        try {
            int i = getUniqueSpriteIndex(s);
            addOverride(s, s1, i);
            return i;
        } catch (Throwable var3) {
            logger.throwing("ModLoader", "addOverride", var3);
            throwException(var3);
            throw new RuntimeException(var3);
        }
    }

    public static void addOverride(String s, String s1, int i) {
        byte j;
        int k;
        if (s.equals("/terrain.png")) {
            j = 0;
            k = terrainSpritesLeft;
        } else {
            if (!s.equals("/gui/items.png")) {
                return;
            }

            j = 1;
            k = itemSpritesLeft;
        }

        System.out.println("Overriding " + s + " with " + s1 + " @ " + i + ". " + k + " left.");
        logger.finer("addOverride(" + s + "," + s1 + "," + i + "). " + k + " left.");
        Map obj = (Map)overrides.get(Integer.valueOf(j));
        if (obj == null) {
            obj = new HashMap();
            overrides.put(Integer.valueOf(j), obj);
        }

        ((Map)obj).put(s1, i);
    }

    public static void addRecipe(ItemStack itemstack, Object... aobj) {
        RecipeDispatcher.getInstance().method_3495(itemstack, aobj);
    }

    public static void addShapelessRecipe(ItemStack itemstack, Object... aobj) {
        RecipeDispatcher.getInstance().registerShapelessRecipe(itemstack, aobj);
    }

    public static void addSmelting(int i, ItemStack itemstack, float xp) {
        SmeltingRecipeRegistry.getInstance().method_3488(i, itemstack, xp);
    }

    public static void addSpawn(Class class1, int i, int j, int k, EntityCategory enumcreaturetype) {
        addSpawn((Class)class1, i, j, k, enumcreaturetype, (Biome[])null);
    }

    public static void addSpawn(Class class1, int i, int j, int k, EntityCategory enumcreaturetype, Biome[] abiomegenbase) {
        if (class1 == null) {
            throw new IllegalArgumentException("entityClass cannot be null");
        } else if (enumcreaturetype == null) {
            throw new IllegalArgumentException("spawnList cannot be null");
        } else {
            if (abiomegenbase == null) {
                abiomegenbase = standardBiomes;
            }

            for(int l = 0; l < abiomegenbase.length; ++l) {
                List list = abiomegenbase[l].getSpawnEntries(enumcreaturetype);
                if (list != null) {
                    boolean flag = false;
                    Iterator iterator = list.iterator();

                    while(iterator.hasNext()) {
                        SpawnEntry spawnlistentry = (SpawnEntry)iterator.next();
                        if (spawnlistentry.type == class1) {
                            spawnlistentry.weight = i;
                            spawnlistentry.minGroupSize = j;
                            spawnlistentry.maxGroupSize = k;
                            flag = true;
                            break;
                        }
                    }

                    if (!flag) {
                        list.add(new SpawnEntry(class1, i, j, k));
                    }
                }
            }

        }
    }

    public static void addSpawn(String s, int i, int j, int k, EntityCategory enumcreaturetype) {
        addSpawn((String)s, i, j, k, enumcreaturetype, (Biome[])null);
    }

    public static void addSpawn(String s, int i, int j, int k, EntityCategory enumcreaturetype, Biome[] abiomegenbase) {
        Class class1 = (Class)classMap.get(s);
        if (class1 != null && MobEntity.class.isAssignableFrom(class1)) {
            addSpawn(class1, i, j, k, enumcreaturetype, abiomegenbase);
        }

    }

    public static int dispenseEntity(World world, ItemStack itemstack, Random rnd, int blockX, int blockY, int blockZ, int offsetX, int offsetZ, double entityX, double entityY, double entityZ) {
        int type = 0;

        for(Iterator iterator = modList.iterator(); type == 0 && iterator.hasNext(); type = ((BaseMod)iterator.next()).dispenseEntity(world, itemstack, rnd, blockX, blockY, blockZ, offsetX, offsetZ, entityX, entityY, entityZ)) {
        }

        return type;
    }

    public static void genericContainerRemoval(World world, int i, int j, int k) {
        Inventory iinventory = (Inventory)world.method_3781(i, j, k);
        if (iinventory != null) {
            for(int l = 0; l < iinventory.getInvSize(); ++l) {
                ItemStack itemstack = iinventory.getInvStack(l);
                if (itemstack != null) {
                    double d = world.random.nextDouble() * 0.8D + 0.1D;
                    double d1 = world.random.nextDouble() * 0.8D + 0.1D;

                    ItemEntity entityitem;
                    for(double d2 = world.random.nextDouble() * 0.8D + 0.1D; itemstack.count > 0; world.spawnEntity(entityitem)) {
                        int i1 = world.random.nextInt(21) + 10;
                        if (i1 > itemstack.count) {
                            i1 = itemstack.count;
                        }

                        itemstack.count -= i1;
                        entityitem = new ItemEntity(world, (double)i + d, (double)j + d1, (double)k + d2, new ItemStack(itemstack.id, i1, itemstack.getMeta()));
                        double d3 = 0.05D;
                        entityitem.velocityX = world.random.nextGaussian() * d3;
                        entityitem.velocityY = world.random.nextGaussian() * d3 + 0.2D;
                        entityitem.velocityZ = world.random.nextGaussian() * d3;
                        if (itemstack.hasNbt()) {
                            entityitem.stack.setNbt((NbtCompound)itemstack.getNbt().copy());
                        }
                    }

                    iinventory.setInvStack(l, (ItemStack)null);
                }
            }
        }

    }

    public static List getLoadedMods() {
        return Collections.unmodifiableList(modList);
    }

    public static Logger getLogger() {
        return logger;
    }

    public static Minecraft getMinecraftInstance() {
        if (instance == null) {
            try {
                ThreadGroup threadgroup = Thread.currentThread().getThreadGroup();
                int i = threadgroup.activeCount();
                Thread[] athread = new Thread[i];
                threadgroup.enumerate(athread);

                int k;
                for(k = 0; k < athread.length; ++k) {
                    System.out.println(athread[k].getName());
                }

                for(k = 0; k < athread.length; ++k) {
                    if (athread[k].getName().equals("Minecraft main thread")) {
                        instance = (Minecraft)getPrivateValue(Thread.class, athread[k], "target");
                        break;
                    }
                }
            } catch (SecurityException var4) {
                logger.throwing("ModLoader", "getMinecraftInstance", var4);
                throw new RuntimeException(var4);
            } catch (NoSuchFieldException var5) {
                logger.throwing("ModLoader", "getMinecraftInstance", var5);
                throw new RuntimeException(var5);
            }
        }

        return instance;
    }

    public static Object getPrivateValue(Class class1, Object obj, int i) throws IllegalArgumentException, SecurityException, NoSuchFieldException {
        try {
            Field field = class1.getDeclaredFields()[i];
            field.setAccessible(true);
            return field.get(obj);
        } catch (IllegalAccessException var4) {
            logger.throwing("ModLoader", "getPrivateValue", var4);
            throwException("An impossible error has occured!", var4);
            return null;
        }
    }

    public static Object getPrivateValue(Class class1, Object obj, String s) throws IllegalArgumentException, SecurityException, NoSuchFieldException {
        try {
            Field field = class1.getDeclaredField(s);
            field.setAccessible(true);
            return field.get(obj);
        } catch (IllegalAccessException var4) {
            logger.throwing("ModLoader", "getPrivateValue", var4);
            throwException("An impossible error has occured!", var4);
            return null;
        }
    }

    public static int getUniqueBlockModelID(BaseMod basemod, boolean flag) {
        int i = nextBlockModelID++;
        blockModels.put(i, basemod);
        blockSpecialInv.put(i, flag);
        return i;
    }

    public static int getUniqueEntityId() {
        return highestEntityId++;
    }

    private static int getUniqueItemSpriteIndex() {
        while(itemSpriteIndex < usedItemSprites.length) {
            if (!usedItemSprites[itemSpriteIndex]) {
                usedItemSprites[itemSpriteIndex] = true;
                --itemSpritesLeft;
                return itemSpriteIndex++;
            }

            ++itemSpriteIndex;
        }

        Exception exception = new Exception("No more empty item sprite indices left!");
        logger.throwing("ModLoader", "getUniqueItemSpriteIndex", exception);
        throwException(exception);
        return 0;
    }

    public static int getUniqueSpriteIndex(String s) {
        if (s.equals("/gui/items.png")) {
            return getUniqueItemSpriteIndex();
        } else if (s.equals("/terrain.png")) {
            return getUniqueTerrainSpriteIndex();
        } else {
            Exception exception = new Exception("No registry for this texture: " + s);
            logger.throwing("ModLoader", "getUniqueItemSpriteIndex", exception);
            throwException(exception);
            return 0;
        }
    }

    private static int getUniqueTerrainSpriteIndex() {
        while(terrainSpriteIndex < usedTerrainSprites.length) {
            if (!usedTerrainSprites[terrainSpriteIndex]) {
                usedTerrainSprites[terrainSpriteIndex] = true;
                --terrainSpritesLeft;
                return terrainSpriteIndex++;
            }

            ++terrainSpriteIndex;
        }

        Exception exception = new Exception("No more empty terrain sprite indices left!");
        logger.throwing("ModLoader", "getUniqueItemSpriteIndex", exception);
        throwException(exception);
        return 0;
    }

    private static void init() {
        hasInit = true;
        RemapUtil.init();
        String usedItemSpritesString = "1111111111111111111111111111111111111101111111111111111111111111111111111111111111111111111111111111110111111111111111000111111111111101111111110000000101111111000000010111111100000000001110110000000000000000000000000000000000000000000000001111111111111111";
        String usedTerrainSpritesString = "1111111111111111111111111100111111111111100111111111111110011111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111000000001111111100000111111111100000001111111110000001111111111111111111";

        for(int i = 0; i < 256; ++i) {
            usedItemSprites[i] = usedItemSpritesString.charAt(i) == '1';
            if (!usedItemSprites[i]) {
                ++itemSpritesLeft;
            }

            usedTerrainSprites[i] = usedTerrainSpritesString.charAt(i) == '1';
            if (!usedTerrainSprites[i]) {
                ++terrainSpritesLeft;
            }
        }

        try {
            instance = Minecraft.getMinecraft();
            instance.gameRenderer = new EntityRendererProxy(instance);
            classMap = (Map)getPrivateValue(EntityType.class, (Object)null, 0);
            field_modifiers = Field.class.getDeclaredField("modifiers");
            field_modifiers.setAccessible(true);
            field_TileEntityRenderers = BlockEntityRenderDispatcher.class.getDeclaredFields()[0];
            field_TileEntityRenderers.setAccessible(true);
            field_animList = class_534.class.getDeclaredFields()[6];
            field_animList.setAccessible(true);
            Field[] afield = Biome.class.getDeclaredFields();
            LinkedList linkedlist = new LinkedList();

            for(int j = 0; j < afield.length; ++j) {
                Class class1 = afield[j].getType();
                if ((afield[j].getModifiers() & 8) != 0 && class1.isAssignableFrom(Biome.class)) {
                    Biome biomegenbase = (Biome)afield[j].get((Object)null);
                    if (!(biomegenbase instanceof NetherBiome) && !(biomegenbase instanceof EndBiome)) {
                        linkedlist.add(biomegenbase);
                    }
                }
            }

            standardBiomes = (Biome[])((Biome[])linkedlist.toArray(new Biome[0]));
        } catch (SecurityException var10) {
            logger.throwing("ModLoader", "init", var10);
            throwException(var10);
            throw new RuntimeException(var10);
        } catch (NoSuchFieldException var11) {
            logger.throwing("ModLoader", "init", var11);
            throwException(var11);
            throw new RuntimeException(var11);
        } catch (IllegalArgumentException var13) {
            logger.throwing("ModLoader", "init", var13);
            throwException(var13);
            throw new RuntimeException(var13);
        } catch (IllegalAccessException var14) {
            logger.throwing("ModLoader", "init", var14);
            throwException(var14);
            throw new RuntimeException(var14);
        }

        try {
            loadConfig();
            if (props.containsKey("loggingLevel")) {
                cfgLoggingLevel = Level.parse(props.getProperty("loggingLevel"));
            }

            if (props.containsKey("grassFix")) {
                class_535Data.cfgGrassFix = Boolean.parseBoolean(props.getProperty("grassFix"));
            }

            logger.setLevel(cfgLoggingLevel);
            if ((logfile.exists() || logfile.createNewFile()) && logfile.canWrite() && logHandler == null) {
                logHandler = new FileHandler(logfile.getPath());
                logHandler.setFormatter(new SimpleFormatter());
                logger.addHandler(logHandler);
            }

            logger.fine("ModLoader 1.3.2 Initializing...");
            System.out.println("ModLoader 1.3.2 Initializing...");
            File file = new File(ModLoader.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            modDir.mkdirs();
            readFromClassPath(file);
            readFromModFolder(modDir);
            sortModList();
            Iterator iterator = modList.iterator();

            while(iterator.hasNext()) {
                BaseMod basemod = (BaseMod)iterator.next();
                basemod.load();
                logger.fine("Mod Loaded: \"" + basemod.toString() + "\"");
                System.out.println("Mod Loaded: " + basemod.toString());
                if (!props.containsKey(basemod.getClass().getSimpleName())) {
                    props.setProperty(basemod.getClass().getSimpleName(), "on");
                }
            }

            Iterator iterator1 = modList.iterator();

            while(iterator1.hasNext()) {
                BaseMod basemod1 = (BaseMod)iterator1.next();
                basemod1.modsLoaded();
            }

            System.out.println("Done.");
            props.setProperty("loggingLevel", cfgLoggingLevel.getName());
            props.setProperty("grassFix", Boolean.toString(class_535Data.cfgGrassFix));
            instance.options.keysAll = registerAllKeys(instance.options.keysAll);
            instance.options.load();
            initStats();
            saveConfig();
        } catch (Throwable var9) {
            logger.throwing("ModLoader", "init", var9);
            throwException("ModLoader has failed to initialize.", var9);
            if (logHandler != null) {
                logHandler.close();
            }

            throw new RuntimeException(var9);
        }
    }

    private static void initStats() {
        int j;
        String s2;
        for(j = 0; j < Block.BLOCKS.length; ++j) {
            if (!Stats.ID_TO_STAT.containsKey(16777216 + j) && Block.BLOCKS[j] != null && Block.BLOCKS[j].hasStats()) {
                s2 = CommonI18n.translate("stat.mineBlock", new Object[]{Block.BLOCKS[j].getTranslatedName()});
                Stats.BLOCK_STATS[j] = (new CraftingStat(16777216 + j, s2, j)).addStat();
                Stats.MINE.add(Stats.BLOCK_STATS[j]);
            }
        }

        for(j = 0; j < Item.ITEMS.length; ++j) {
            if (!Stats.ID_TO_STAT.containsKey(16908288 + j) && Item.ITEMS[j] != null) {
                s2 = CommonI18n.translate("stat.useItem", new Object[]{Item.ITEMS[j].getName()});
                Stats.USED[j] = (new CraftingStat(16908288 + j, s2, j)).addStat();
                if (j >= Block.BLOCKS.length) {
                    Stats.ITEM.add(Stats.USED[j]);
                }
            }

            if (!Stats.ID_TO_STAT.containsKey(16973824 + j) && Item.ITEMS[j] != null && Item.ITEMS[j].isDamageable()) {
                s2 = CommonI18n.translate("stat.breakItem", new Object[]{Item.ITEMS[j].getName()});
                Stats.BROKEN[j] = (new CraftingStat(16973824 + j, s2, j)).addStat();
            }
        }

        HashSet hashset = new HashSet();
        Iterator iterator = RecipeDispatcher.getInstance().getAllRecipes().iterator();

        while(iterator.hasNext()) {
            Object obj = iterator.next();
            hashset.add(((RecipeType)obj).getOutput().id);
        }

        Iterator iterator2 = SmeltingRecipeRegistry.getInstance().getRecipeMap().values().iterator();

        while(iterator2.hasNext()) {
            Object obj1 = iterator2.next();
            hashset.add(((ItemStack)obj1).id);
        }

        iterator2 = hashset.iterator();

        while(iterator2.hasNext()) {
            int k = (Integer)iterator2.next();
            if (!Stats.ID_TO_STAT.containsKey(16842752 + k) && Item.ITEMS[k] != null) {
                String s3 = CommonI18n.translate("stat.craftItem", new Object[]{Item.ITEMS[k].getName()});
                Stats.CRAFTING_STATS[k] = (new CraftingStat(16842752 + k, s3, k)).addStat();
            }
        }

    }

    public static boolean isGUIOpen(Class class1) {
        Minecraft minecraft = getMinecraftInstance();
        if (class1 == null) {
            return minecraft.currentScreen == null;
        } else {
            return minecraft.currentScreen == null && class1 != null ? false : class1.isInstance(minecraft.currentScreen);
        }
    }

    public static boolean isModLoaded(String s) {
        Iterator iterator = modList.iterator();

        BaseMod basemod;
        do {
            if (!iterator.hasNext()) {
                return false;
            }

            basemod = (BaseMod)iterator.next();
        } while(!s.contentEquals(basemod.getName()));

        return true;
    }

    public static void loadConfig() throws IOException {
        cfgdir.mkdir();
        if (cfgfile.exists() || cfgfile.createNewFile()) {
            if (cfgfile.canRead()) {
                FileInputStream fileinputstream = new FileInputStream(cfgfile);
                props.load(fileinputstream);
                fileinputstream.close();
            }

        }
    }

    public static BufferedImage loadImage(class_534 renderengine, String s) throws Exception {
        TexturePackManager texturepacklist = (TexturePackManager)getPrivateValue(class_534.class, renderengine, 10);
        InputStream inputstream = texturepacklist.getCurrentTexturePack().openStream(s);
        if (inputstream == null) {
            throw new Exception("Image not found: " + s);
        } else {
            BufferedImage bufferedimage = ImageIO.read(inputstream);
            if (bufferedimage == null) {
                throw new Exception("Image corrupted: " + s);
            } else {
                return bufferedimage;
            }
        }
    }

    public static void onItemPickup(PlayerEntity entityplayer, ItemStack itemstack) {
        Iterator iterator = modList.iterator();

        while(iterator.hasNext()) {
            BaseMod basemod = (BaseMod)iterator.next();
            basemod.onItemPickup(entityplayer, itemstack);
        }

    }

    public static void onTick(float f, Minecraft minecraft) {
        minecraft.profiler.pop();
        minecraft.profiler.pop();
        minecraft.profiler.push("modtick");
        if (!hasInit) {
            init();
            logger.fine("Initialized");
        }

        if (texPack == null || minecraft.options.currentTexturePackName != texPack) {
            texturesAdded = false;
            texPack = minecraft.options.currentTexturePackName;
        }

        if (langPack == null || Language.getInstance().getCode() != langPack) {
            Properties properties = null;

            try {
                properties = (Properties)getPrivateValue(Language.class, Language.getInstance(), 1);
            } catch (SecurityException var12) {
                logger.throwing("ModLoader", "AddLocalization", var12);
                throwException(var12);
            } catch (NoSuchFieldException var13) {
                logger.throwing("ModLoader", "AddLocalization", var13);
                throwException(var13);
            }

            langPack = Language.getInstance().getCode();
            if (properties != null) {
                if (localizedStrings.containsKey("en_US")) {
                    properties.putAll((Map)localizedStrings.get("en_US"));
                }

                if (!langPack.contentEquals("en_US") && localizedStrings.containsKey(langPack)) {
                    properties.putAll((Map)localizedStrings.get(langPack));
                }
            }
        }

        if (!texturesAdded && minecraft.field_3813 != null) {
            registerAllTextureOverrides(minecraft.field_3813);
            texturesAdded = true;
        }

        long l = 0L;
        Iterator iterator1;
        Map.Entry entry;
        if (minecraft.playerEntity != null && minecraft.playerEntity.world != null) {
            l = minecraft.playerEntity.world.getTimeOfDay();
            iterator1 = inGameHooks.entrySet().iterator();

            label126:
            while(true) {
                do {
                    if (!iterator1.hasNext()) {
                        break label126;
                    }

                    entry = (Map.Entry)iterator1.next();
                } while(clock == l && (Boolean)entry.getValue());

                if (!((BaseMod)entry.getKey()).onTickInGame(f, minecraft)) {
                    iterator1.remove();
                }
            }
        }

        if (minecraft.playerEntity != null && minecraft.currentScreen != null) {
            iterator1 = inGUIHooks.entrySet().iterator();

            label112:
            while(true) {
                do {
                    if (!iterator1.hasNext()) {
                        break label112;
                    }

                    entry = (Map.Entry)iterator1.next();
                } while(clock == l && (Boolean)entry.getValue() & minecraft.playerEntity.world != null);

                if (!((BaseMod)entry.getKey()).onTickInGUI(f, minecraft, minecraft.currentScreen)) {
                    iterator1.remove();
                }
            }
        }

        if (clock != l) {
            iterator1 = keyList.entrySet().iterator();

            label98:
            while(iterator1.hasNext()) {
                entry = (Map.Entry)iterator1.next();
                Iterator iterator3 = ((Map)entry.getValue()).entrySet().iterator();

                while(true) {
                    Map.Entry entry3;
                    boolean flag;
                    boolean[] aflag;
                    boolean flag1;
                    do {
                        do {
                            if (!iterator3.hasNext()) {
                                continue label98;
                            }

                            entry3 = (Map.Entry)iterator3.next();
                            int i = ((KeyBinding)entry3.getKey()).code;
                            if (i < 0) {
                                i += 100;
                                flag = Mouse.isButtonDown(i);
                            } else {
                                flag = Keyboard.isKeyDown(i);
                            }

                            aflag = (boolean[])((boolean[])entry3.getValue());
                            flag1 = aflag[1];
                            aflag[1] = flag;
                        } while(!flag);
                    } while(flag1 && !aflag[0]);

                    ((BaseMod)entry.getKey()).keyboardEvent((KeyBinding)entry3.getKey());
                }
            }
        }

        clock = l;
        minecraft.profiler.pop();
        minecraft.profiler.push("render");
        minecraft.profiler.push("gameRenderer");
    }

    public static void openGUI(PlayerEntity entityplayer, Screen guiscreen) {
        if (!hasInit) {
            init();
            logger.fine("Initialized");
        }

        Minecraft minecraft = getMinecraftInstance();
        if (minecraft.playerEntity == entityplayer) {
            if (guiscreen != null) {
                minecraft.openScreen(guiscreen);
            }

        }
    }

    public static void populateChunk(ChunkProvider ichunkprovider, int i, int j, World world) {
        if (!hasInit) {
            init();
            logger.fine("Initialized");
        }

        Random random = new Random(world.getSeed());
        long l = random.nextLong() / 2L * 2L + 1L;
        long l1 = random.nextLong() / 2L * 2L + 1L;
        random.setSeed((long)i * l + (long)j * l1 ^ world.getSeed());
        Iterator iterator = modList.iterator();

        while(iterator.hasNext()) {
            BaseMod basemod = (BaseMod)iterator.next();
            if (world.dimension.canPlayersSleep()) {
                basemod.generateSurface(world, random, i << 4, j << 4);
            } else if (world.dimension.waterVaporizes) {
                basemod.generateNether(world, random, i << 4, j << 4);
            }
        }

    }

    private static void readFromClassPath(File file) throws FileNotFoundException, IOException {
        logger.finer("Adding mods from " + file.getCanonicalPath());
        ClassLoader classloader = ModLoader.class.getClassLoader();
        if (!file.isFile() || !file.getName().endsWith(".jar") && !file.getName().endsWith(".zip")) {
            if (file.isDirectory()) {
                Package package1 = ModLoader.class.getPackage();
                if (package1 != null) {
                    String s = package1.getName().replace('.', File.separatorChar);
                    file = new File(file, s);
                }

                logger.finer("Directory found.");
                File[] afile = file.listFiles();
                if (afile != null) {
                    for(int i = 0; i < afile.length; ++i) {
                        String s2 = afile[i].getName();
                        if (afile[i].isFile() && s2.startsWith("mod_") && s2.endsWith(".class")) {
                            addMod(classloader, s2);
                        }
                    }
                }
            }
        } else {
            logger.finer("Zip found.");
            FileInputStream fileinputstream = new FileInputStream(file);
            ZipInputStream zipinputstream = new ZipInputStream(fileinputstream);
            Object obj = null;

            while(true) {
                ZipEntry zipentry = zipinputstream.getNextEntry();
                if (zipentry == null) {
                    fileinputstream.close();
                    break;
                }

                String s1 = zipentry.getName();
                if (!zipentry.isDirectory() && s1.startsWith("mod_") && s1.endsWith(".class")) {
                    addMod(classloader, s1);
                }
            }
        }

    }

    private static void readFromModFolder(File file) throws IOException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, SecurityException, NoSuchMethodException {
        ClassLoader classloader = Minecraft.class.getClassLoader();
        Method method = classloader.getClass().getDeclaredMethod("addURL", URL.class);
        method.setAccessible(true);
        if (!file.isDirectory()) {
            throw new IllegalArgumentException("folder must be a Directory.");
        } else {
            File[] afile = file.listFiles();
            Arrays.sort(afile);
            int j;
            File file2;

            for (j = 0; j < afile.length; j++) {
                file2 = afile[j];
                if (file.isDirectory() || file2.isFile() && (file2.getName().endsWith(".jar") || file2.getName().endsWith(".zip"))) {
                    File remappedFile = new File(Constants.REMAPPED_FOLDER, file2.getName());
                    if (file2.isFile() && (file2.getName().endsWith(".jar") || file2.getName().endsWith(".zip"))) {
                        FileInputStream fileinputstream = new FileInputStream(file2);
                        ZipInputStream zipinputstream = new ZipInputStream(fileinputstream);
                        boolean fabric = false;
                        while(true) {
                            ZipEntry zipentry = zipinputstream.getNextEntry();
                            if (zipentry == null) {
                                zipinputstream.close();
                                fileinputstream.close();
                                break;
                            }

                            String s1 = zipentry.getName();
                            if (s1.equals("fabric.mod.json")) {
                                fabric = true;
                                break;
                            }
                        }

                        if (fabric) {
                            afile[j] = null;
                            continue;
                        }
                    } else if (file2.isDirectory()) {
                        boolean isMod = false;

                        for (String fl : file2.list()) {
                            if (fl.startsWith("mod_") && fl.endsWith(".class")) {
                                isMod = true;
                                break;
                            }
                        }

                        if (!isMod) {
                            afile[j] = null;
                            continue;
                        } else {
                            remappedFile = new File(Constants.REMAPPED_FOLDER, file2.getName() + ".zip");
                        }
                    } else if (file2.isFile()) {
                        afile[j] = null;
                        continue;
                    }
                    Log.info(Constants.LOG_CATEGORY, "Found potential ModLoader mod " + file2.getName());
                    if (!remappedFile.exists()) RemapUtil.remapMod(file2.toPath(), remappedFile.toPath());
                    afile[j] = remappedFile;
                }
            }

            for(j = 0; j < afile.length; ++j) {
                file2 = afile[j];
                if (file2 != null && (file2.isDirectory() || file2.isFile() && (file2.getName().endsWith(".jar") || file2.getName().endsWith(".zip")))) {
                    method.invoke(classloader, file2.toURI().toURL());
                }
            }

            for(j = 0; j < afile.length; ++j) {
                file2 = afile[j];
                if (file2 != null && (file2.isDirectory() || file2.isFile() && (file2.getName().endsWith(".jar") || file2.getName().endsWith(".zip")))) {
                    logger.finer("Adding mods from " + file2.getCanonicalPath());
                    if (!file2.isFile()) {
                        if (file2.isDirectory()) {
                            logger.finer("Directory found.");
                            File[] afile1 = file2.listFiles();
                            if (afile1 != null) {
                                for(int k = 0; k < afile1.length; ++k) {
                                    String s2 = afile1[k].getName();
                                    if (afile1[k].isFile() && s2.startsWith("net/minecraft/mod_") && s2.endsWith(".class")) {
                                        addMod(classloader, s2);
                                    }
                                }
                            }
                        }
                    } else {
                        logger.finer("Zip found.");
                        FileInputStream fileinputstream = new FileInputStream(file2);
                        ZipInputStream zipinputstream = new ZipInputStream(fileinputstream);
                        Object obj = null;

                        while(true) {
                            ZipEntry zipentry = zipinputstream.getNextEntry();
                            if (zipentry == null) {
                                zipinputstream.close();
                                fileinputstream.close();
                                break;
                            }

                            String s1 = zipentry.getName();
                            if (!zipentry.isDirectory() && s1.startsWith("net/minecraft/mod_") && s1.endsWith(".class")) {
                                addMod(classloader, s1);
                            }
                        }
                    }
                }
            }

        }
    }

    public static void clientCustomPayload(CustomPayloadC2SPacket packet) {
        if (packet.channel.equals("ML|OpenTE")) {
            try {
                DataInputStream stream = new DataInputStream(new ByteArrayInputStream(packet.field_2455));
                int guiID = stream.read();
                int contID = stream.readInt();
                int x = stream.readInt();
                int y = stream.readInt();
                int z = stream.readInt();
                int dim = (byte)stream.read();
                class_481 player = instance.playerEntity;
                if (player._dimension != dim) {
                    return;
                }

                if (containerGUIs.containsKey(contID)) {
                    BaseMod basemod = (BaseMod)containerGUIs.get(contID);
                    if (basemod != null) {
                        HandledScreen gui = basemod.getContainerGUI(player, contID, x, y, z);
                        if (gui == null) {
                            return;
                        }

                        instance.openScreen(gui);
                        player.openScreenHandler.syncId = guiID;
                    }
                }
            } catch (IOException var11) {
                var11.printStackTrace();
            }
        } else if (packetChannels.containsKey(packet.channel)) {
            BaseMod basemod = (BaseMod)packetChannels.get(packet.channel);
            if (basemod != null) {
                basemod.clientCustomPayload(clientHandler, packet);
            }
        }

    }

    public static void serverCustomPayload(ServerPacketListener serverHandler, CustomPayloadC2SPacket packet250custompayload) {
        if (packetChannels.containsKey(packet250custompayload.channel)) {
            BaseMod basemod = (BaseMod)packetChannels.get(packet250custompayload.channel);
            if (basemod != null) {
                basemod.serverCustomPayload(serverHandler, packet250custompayload);
            }
        }

    }

    public static void registerContainerID(BaseMod mod, int id) {
        containerGUIs.put(id, mod);
    }

    public static void clientOpenWindow(OpenScreen_S2CPacket par1Packet100OpenWindow) {
    }

    public static void serverOpenWindow(ServerPlayerEntity player, ScreenHandler container, int id, int x, int y, int z) {
        try {
            Field winIDField = ServerPlayerEntity.class.getDeclaredFields()[17];
            winIDField.setAccessible(true);
            int winID = winIDField.getInt(player);
            winID = winID % 100 + 1;
            winIDField.setInt(player, winID);
            ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
            DataOutputStream stream = new DataOutputStream(bytestream);
            stream.write(winID);
            stream.writeInt(id);
            stream.writeInt(x);
            stream.writeInt(y);
            stream.writeInt(z);
            stream.write(player._dimension);
            player.field_2823.sendPacket(new CustomPayloadC2SPacket("ML|OpenTE", bytestream.toByteArray()));
            player.openScreenHandler = container;
            player.openScreenHandler.syncId = winID;
            player.openScreenHandler.close(player);
        } catch (Exception var10) {
            var10.printStackTrace();
        }

    }

    public static KeyBinding[] registerAllKeys(KeyBinding[] akeybinding) {
        LinkedList linkedlist = new LinkedList();
        linkedlist.addAll(Arrays.asList(akeybinding));
        Iterator iterator = keyList.values().iterator();

        while(iterator.hasNext()) {
            Map map = (Map)iterator.next();
            linkedlist.addAll(map.keySet());
        }

        return (KeyBinding[])((KeyBinding[])linkedlist.toArray(new KeyBinding[0]));
    }

    public static void registerAllTextureOverrides(class_534 renderengine) {
        animList.clear();
        Minecraft minecraft = getMinecraftInstance();
        Iterator iterator = modList.iterator();

        while(iterator.hasNext()) {
            BaseMod basemod = (BaseMod)iterator.next();
            basemod.registerAnimation(minecraft);
        }

        Iterator iterator2 = animList.iterator();

        while(iterator2.hasNext()) {
            class_584 texturefx = (class_584)iterator2.next();
            renderengine.method_1416(texturefx);
        }

        iterator2 = overrides.entrySet().iterator();

        while(iterator2.hasNext()) {
            Map.Entry entry = (Map.Entry)iterator2.next();
            Iterator iterator3 = ((Map)entry.getValue()).entrySet().iterator();

            while(iterator3.hasNext()) {
                Map.Entry entry1 = (Map.Entry)iterator3.next();
                String s = (String)entry1.getKey();
                int i = (Integer)entry1.getValue();
                int j = (Integer)entry.getKey();

                try {
                    BufferedImage bufferedimage = loadImage(renderengine, s);
                    ModTextureStatic modtexturestatic = new ModTextureStatic(i, j, bufferedimage);
                    renderengine.method_1416(modtexturestatic);
                } catch (Exception var13) {
                    logger.throwing("ModLoader", "RegisterAllTextureOverrides", var13);
                    throwException(var13);
                    throw new RuntimeException(var13);
                }
            }
        }

    }

    public static void registerBlock(Block block) {
        registerBlock(block, (Class)null);
    }

    public static void registerBlock(Block block, Class class1) {
        try {
            if (block == null) {
                throw new IllegalArgumentException("block parameter cannot be null.");
            }

            int i = block.id;
            BlockItem itemblock = null;
            if (class1 != null) {
                itemblock = (BlockItem)class1.getConstructor(Integer.TYPE).newInstance(i - 256);
            } else {
                itemblock = new BlockItem(i - 256);
            }

            if (Block.BLOCKS[i] != null && Item.ITEMS[i] == null) {
                Item.ITEMS[i] = itemblock;
            }
        } catch (IllegalArgumentException var4) {
            logger.throwing("ModLoader", "RegisterBlock", var4);
            throwException(var4);
        } catch (IllegalAccessException var5) {
            logger.throwing("ModLoader", "RegisterBlock", var5);
            throwException(var5);
        } catch (SecurityException var6) {
            logger.throwing("ModLoader", "RegisterBlock", var6);
            throwException(var6);
        } catch (InstantiationException var7) {
            logger.throwing("ModLoader", "RegisterBlock", var7);
            throwException(var7);
        } catch (InvocationTargetException var8) {
            logger.throwing("ModLoader", "RegisterBlock", var8);
            throwException(var8);
        } catch (NoSuchMethodException var9) {
            logger.throwing("ModLoader", "RegisterBlock", var9);
            throwException(var9);
        }

    }

    public static void registerEntityID(Class class1, String s, int i) {
        try {
            EntityTypeAccessor.callRegister(class1, s, i);
        } catch (IllegalArgumentException var4) {
            logger.throwing("ModLoader", "RegisterEntityID", var4);
            throwException(var4);
        }

    }

    public static void registerEntityID(Class class1, String s, int i, int j, int k) {
        registerEntityID(class1, s, i);
        EntityType.field_3267.put(i, new class_868(i, j, k));
    }

    public static void registerKey(BaseMod basemod, KeyBinding keybinding, boolean flag) {
        Map obj = (Map)keyList.get(basemod);
        if (obj == null) {
            obj = new HashMap();
        }

        boolean[] aflag = new boolean[]{flag, false};
        ((Map)obj).put(keybinding, aflag);
        keyList.put(basemod, obj);
    }

    public static void registerPacketChannel(BaseMod basemod, String s) {
        if (s.length() < 16) {
            packetChannels.put(s, basemod);
        } else {
            throw new RuntimeException(String.format("Invalid channel name: %s. Must be less than 16 characters.", s));
        }
    }

    public static void registerTileEntity(Class class1, String s) {
        registerTileEntity(class1, s, (BlockEntityRenderer)null);
    }

    public static void registerTileEntity(Class class1, String s, BlockEntityRenderer tileentityspecialrenderer) {
        try {
            BlockEntityAccessor.callRegister(class1, s);
            if (tileentityspecialrenderer != null) {
                BlockEntityRenderDispatcher tileentityrenderer = BlockEntityRenderDispatcher.INSTANCE;
                Map map = (Map)field_TileEntityRenderers.get(tileentityrenderer);
                map.put(class1, tileentityspecialrenderer);
                tileentityspecialrenderer.setDispatcher(tileentityrenderer);
            }
        } catch (IllegalArgumentException var5) {
            logger.throwing("ModLoader", "RegisterTileEntity", var5);
            throwException(var5);
        } catch (IllegalAccessException var6) {
            logger.throwing("ModLoader", "RegisterTileEntity", var6);
            throwException(var6);
        }

    }

    public static void removeBiome(Biome biomegenbase) {
        Biome[] abiomegenbase = SetBaseBiomesLayerData.biomeArray;
        List list = Arrays.asList(abiomegenbase);
        ArrayList arraylist = new ArrayList();
        arraylist.addAll(list);
        if (arraylist.contains(biomegenbase)) {
            arraylist.remove(biomegenbase);
        }

        SetBaseBiomesLayerData.biomeArray = (Biome[])((Biome[])arraylist.toArray(new Biome[0]));
    }

    public static void removeSpawn(Class class1, EntityCategory enumcreaturetype) {
        removeSpawn((Class)class1, enumcreaturetype, (Biome[])null);
    }

    public static void removeSpawn(Class class1, EntityCategory enumcreaturetype, Biome[] abiomegenbase) {
        if (class1 == null) {
            throw new IllegalArgumentException("entityClass cannot be null");
        } else if (enumcreaturetype == null) {
            throw new IllegalArgumentException("spawnList cannot be null");
        } else {
            if (abiomegenbase == null) {
                abiomegenbase = standardBiomes;
            }

            for(int i = 0; i < abiomegenbase.length; ++i) {
                List list = abiomegenbase[i].getSpawnEntries(enumcreaturetype);
                if (list != null) {
                    Iterator iterator = list.iterator();

                    while(iterator.hasNext()) {
                        SpawnEntry spawnlistentry = (SpawnEntry)iterator.next();
                        if (spawnlistentry.type == class1) {
                            iterator.remove();
                        }
                    }
                }
            }

        }
    }

    public static void removeSpawn(String s, EntityCategory enumcreaturetype) {
        removeSpawn((String)s, enumcreaturetype, (Biome[])null);
    }

    public static void removeSpawn(String s, EntityCategory enumcreaturetype, Biome[] abiomegenbase) {
        Class class1 = (Class)classMap.get(s);
        if (class1 != null && MobEntity.class.isAssignableFrom(class1)) {
            removeSpawn(class1, enumcreaturetype, abiomegenbase);
        }

    }

    public static boolean renderBlockIsItemFull3D(int i) {
        if (!blockSpecialInv.containsKey(i)) {
            return i == 26;
        } else {
            return (Boolean)blockSpecialInv.get(i);
        }
    }

    public static void renderInvBlock(class_535 renderblocks, Block block, int i, int j) {
        BaseMod basemod = (BaseMod)blockModels.get(j);
        if (basemod != null) {
            basemod.renderInvBlock(renderblocks, block, i, j);
        }
    }

    public static boolean renderWorldBlock(class_535 renderblocks, WorldView iblockaccess, int i, int j, int k, Block block, int l) {
        BaseMod basemod = (BaseMod)blockModels.get(l);
        return basemod == null ? false : basemod.renderWorldBlock(renderblocks, iblockaccess, i, j, k, block, l);
    }

    public static void saveConfig() throws IOException {
        cfgdir.mkdir();
        if (cfgfile.exists() || cfgfile.createNewFile()) {
            if (cfgfile.canWrite()) {
                FileOutputStream fileoutputstream = new FileOutputStream(cfgfile);
                props.store(fileoutputstream, "ModLoader Config");
                fileoutputstream.close();
            }

        }
    }

    public static void clientChat(String s) {
        Iterator i$ = modList.iterator();

        while(i$.hasNext()) {
            BaseMod mod = (BaseMod)i$.next();
            mod.clientChat(s);
        }

    }

    public static void serverChat(ServerPacketListener netserverhandler, String s) {
        Iterator i$ = modList.iterator();

        while(i$.hasNext()) {
            BaseMod mod = (BaseMod)i$.next();
            mod.serverChat(netserverhandler, s);
        }

    }

    public static void clientConnect(class_469 netclienthandler, class_690 packet1login) {
        clientHandler = netclienthandler;
        if (packetChannels.size() > 0) {
            CustomPayloadC2SPacket packet250custompayload = new CustomPayloadC2SPacket();
            packet250custompayload.channel = "REGISTER";
            StringBuilder stringbuilder = new StringBuilder();
            Iterator iterator1 = packetChannels.keySet().iterator();
            stringbuilder.append((String)iterator1.next());

            while(iterator1.hasNext()) {
                stringbuilder.append("\u0000");
                stringbuilder.append((String)iterator1.next());
            }

            packet250custompayload.field_2455 = stringbuilder.toString().getBytes(Charset.forName("UTF8"));
            packet250custompayload.field_2454 = packet250custompayload.field_2455.length;
            clientSendPacket(packet250custompayload);
        }

        Iterator i$ = modList.iterator();

        while(i$.hasNext()) {
            BaseMod mod = (BaseMod)i$.next();
            mod.clientConnect(netclienthandler);
        }

    }

    public static void clientDisconnect() {
        Iterator i$ = modList.iterator();

        while(i$.hasNext()) {
            BaseMod mod = (BaseMod)i$.next();
            mod.clientDisconnect(clientHandler);
        }

        clientHandler = null;
    }

    public static void clientSendPacket(Packet packet) {
        if (clientHandler != null) {
            clientHandler.sendPacket(packet);
        }

    }

    public static void serverSendPacket(ServerPacketListener serverHandler, Packet packet) {
        if (serverHandler != null) {
            serverHandler.sendPacket(packet);
        }

    }

    public static void setInGameHook(BaseMod basemod, boolean flag, boolean flag1) {
        if (flag) {
            inGameHooks.put(basemod, flag1);
        } else {
            inGameHooks.remove(basemod);
        }

    }

    public static void setInGUIHook(BaseMod basemod, boolean flag, boolean flag1) {
        if (flag) {
            inGUIHooks.put(basemod, flag1);
        } else {
            inGUIHooks.remove(basemod);
        }

    }

    public static void setPrivateValue(Class class1, Object obj, int i, Object obj1) throws IllegalArgumentException, SecurityException, NoSuchFieldException {
        try {
            Field field = class1.getDeclaredFields()[i];
            field.setAccessible(true);
            int j = field_modifiers.getInt(field);
            if ((j & 16) != 0) {
                field_modifiers.setInt(field, j & -17);
            }

            field.set(obj, obj1);
        } catch (IllegalAccessException var6) {
            logger.throwing("ModLoader", "setPrivateValue", var6);
            throwException("An impossible error has occured!", var6);
        }

    }

    public static void setPrivateValue(Class class1, Object obj, String s, Object obj1) throws IllegalArgumentException, SecurityException, NoSuchFieldException {
        try {
            Field field = class1.getDeclaredField(s);
            int i = field_modifiers.getInt(field);
            if ((i & 16) != 0) {
                field_modifiers.setInt(field, i & -17);
            }

            field.setAccessible(true);
            field.set(obj, obj1);
        } catch (IllegalAccessException var6) {
            logger.throwing("ModLoader", "setPrivateValue", var6);
            throwException("An impossible error has occured!", var6);
        }

    }

    private static void setupProperties(Class class1) throws IllegalArgumentException, IllegalAccessException, IOException, SecurityException, NoSuchFieldException, NoSuchAlgorithmException, DigestException {
        LinkedList linkedlist = new LinkedList();
        Properties properties = new Properties();
        int i = 0;
        int j = 0;
        File file = new File(cfgdir, class1.getSimpleName() + ".cfg");
        if (file.exists() && file.canRead()) {
            properties.load(new FileInputStream(file));
        }

        if (properties.containsKey("checksum")) {
            j = Integer.parseInt(properties.getProperty("checksum"), 36);
        }

        Field[] afield;
        int l = (afield = class1.getDeclaredFields()).length;

        for(int k = 0; k < l; ++k) {
            Field field = afield[k];
            if ((field.getModifiers() & 8) != 0 && field.isAnnotationPresent(MLProp.class)) {
                linkedlist.add(field);
                Object obj = field.get((Object)null);
                i += obj.hashCode();
            }
        }

        StringBuilder stringbuilder = new StringBuilder();
        Iterator iterator = linkedlist.iterator();

        while(true) {
            MLProp mlprop;
            String s;
            Object obj1;
            Object obj2;
            double d;
            Field field1;
            do {
                do {
                    while(true) {
                        do {
                            do {
                                if (!iterator.hasNext()) {
                                    properties.put("checksum", Integer.toString(i, 36));
                                    if (!properties.isEmpty() && (file.exists() || file.createNewFile()) && file.canWrite()) {
                                        properties.store(new FileOutputStream(file), stringbuilder.toString());
                                    }

                                    return;
                                }

                                field1 = (Field)iterator.next();
                            } while((field1.getModifiers() & 8) == 0);
                        } while(!field1.isAnnotationPresent(MLProp.class));

                        Class class2 = field1.getType();
                        mlprop = (MLProp)field1.getAnnotation(MLProp.class);
                        s = mlprop.name().length() != 0 ? mlprop.name() : field1.getName();
                        obj1 = field1.get((Object)null);
                        StringBuilder stringbuilder1 = new StringBuilder();
                        if (mlprop.min() != Double.NEGATIVE_INFINITY) {
                            stringbuilder1.append(String.format(",>=%.1f", mlprop.min()));
                        }

                        if (mlprop.max() != Double.POSITIVE_INFINITY) {
                            stringbuilder1.append(String.format(",<=%.1f", mlprop.max()));
                        }

                        StringBuilder stringbuilder2 = new StringBuilder();
                        if (mlprop.info().length() > 0) {
                            stringbuilder2.append(" -- ");
                            stringbuilder2.append(mlprop.info());
                        }

                        stringbuilder.append(String.format("%s (%s:%s%s)%s\n", s, class2.getName(), obj1, stringbuilder1, stringbuilder2));
                        if (j == i && properties.containsKey(s)) {
                            String s1 = properties.getProperty(s);
                            obj2 = null;
                            if (class2.isAssignableFrom(String.class)) {
                                obj2 = s1;
                            } else if (class2.isAssignableFrom(Integer.TYPE)) {
                                obj2 = Integer.parseInt(s1);
                            } else if (class2.isAssignableFrom(Short.TYPE)) {
                                obj2 = Short.parseShort(s1);
                            } else if (class2.isAssignableFrom(Byte.TYPE)) {
                                obj2 = Byte.parseByte(s1);
                            } else if (class2.isAssignableFrom(Boolean.TYPE)) {
                                obj2 = Boolean.parseBoolean(s1);
                            } else if (class2.isAssignableFrom(Float.TYPE)) {
                                obj2 = Float.parseFloat(s1);
                            } else if (class2.isAssignableFrom(Double.TYPE)) {
                                obj2 = Double.parseDouble(s1);
                            }
                            break;
                        }

                        logger.finer(s + " not in config, using default: " + obj1);
                        properties.setProperty(s, obj1.toString());
                    }
                } while(obj2 == null);

                if (!(obj2 instanceof Number)) {
                    break;
                }

                d = ((Number)obj2).doubleValue();
            } while(mlprop.min() != Double.NEGATIVE_INFINITY && d < mlprop.min() || mlprop.max() != Double.POSITIVE_INFINITY && d > mlprop.max());

            logger.finer(s + " set to " + obj2);
            if (!obj2.equals(obj1)) {
                field1.set((Object)null, obj2);
            }
        }
    }

    private static void sortModList() throws Exception {
        HashMap hashmap = new HashMap();
        Iterator iterator = getLoadedMods().iterator();

        while(iterator.hasNext()) {
            BaseMod basemod = (BaseMod)iterator.next();
            hashmap.put(basemod.getClass().getSimpleName(), basemod);
        }

        LinkedList linkedlist = new LinkedList();
        int i = 0;

        label129:
        while(linkedlist.size() != modList.size() && i <= 10) {
            Iterator iterator1 = modList.iterator();

            while(true) {
                BaseMod basemod1;
                int j;
                label125:
                while(true) {
                    String s;
                    do {
                        while(true) {
                            do {
                                if (!iterator1.hasNext()) {
                                    ++i;
                                    continue label129;
                                }

                                basemod1 = (BaseMod)iterator1.next();
                            } while(linkedlist.contains(basemod1));

                            s = basemod1.getPriorities();
                            if (s != null && s.length() != 0 && s.indexOf(58) != -1) {
                                break;
                            }

                            linkedlist.add(basemod1);
                        }
                    } while(i <= 0);

                    j = -1;
                    int k = Integer.MIN_VALUE;
                    int l = Integer.MAX_VALUE;
                    String[] as;
                    if (s.indexOf(59) > 0) {
                        as = s.split(";");
                    } else {
                        as = new String[]{s};
                    }

                    int i1 = 0;

                    while(true) {
                        if (i1 >= as.length) {
                            break label125;
                        }

                        String s1 = as[i1];
                        if (s1.indexOf(58) != -1) {
                            String[] as1 = s1.split(":");
                            String s2 = as1[0];
                            String s3 = as1[1];
                            if (s2.contentEquals("required-before") || s2.contentEquals("before") || s2.contentEquals("after") || s2.contentEquals("required-after")) {
                                if (s3.contentEquals("*")) {
                                    if (!s2.contentEquals("required-before") && !s2.contentEquals("before")) {
                                        if (s2.contentEquals("required-after") || s2.contentEquals("after")) {
                                            j = linkedlist.size();
                                        }
                                        break label125;
                                    }

                                    j = 0;
                                    break label125;
                                }

                                if ((s2.contentEquals("required-before") || s2.contentEquals("required-after")) && !hashmap.containsKey(s3)) {
                                    throw new Exception(String.format("%s is missing dependency: %s", basemod1, s3));
                                }

                                BaseMod basemod2 = (BaseMod)hashmap.get(s3);
                                if (!linkedlist.contains(basemod2)) {
                                    break;
                                }

                                int j1 = linkedlist.indexOf(basemod2);
                                if (!s2.contentEquals("required-before") && !s2.contentEquals("before")) {
                                    if (s2.contentEquals("required-after") || s2.contentEquals("after")) {
                                        j = j1 + 1;
                                        if (j > k) {
                                            k = j;
                                        } else {
                                            j = k;
                                        }
                                    }
                                } else {
                                    j = j1;
                                    if (j1 < l) {
                                        l = j1;
                                    } else {
                                        j = l;
                                    }
                                }
                            }
                        }

                        ++i1;
                    }
                }

                if (j != -1) {
                    linkedlist.add(j, basemod1);
                }
            }
        }

        modList.clear();
        modList.addAll(linkedlist);
    }

    public static void takenFromCrafting(PlayerEntity entityplayer, ItemStack itemstack, Inventory iinventory) {
        Iterator iterator = modList.iterator();

        while(iterator.hasNext()) {
            BaseMod basemod = (BaseMod)iterator.next();
            basemod.takenFromCrafting(entityplayer, itemstack, iinventory);
        }

    }

    public static void takenFromFurnace(PlayerEntity entityplayer, ItemStack itemstack) {
        Iterator iterator = modList.iterator();

        while(iterator.hasNext()) {
            BaseMod basemod = (BaseMod)iterator.next();
            basemod.takenFromFurnace(entityplayer, itemstack);
        }

    }

    public static void throwException(String s, Throwable throwable) {
        Minecraft minecraft = getMinecraftInstance();
        if (minecraft != null) {
            minecraft.printCrashReport(new CrashReport(s, throwable));
        } else {
            throw new RuntimeException(throwable);
        }
    }

    private static void throwException(Throwable throwable) {
        throwException("Exception occured in ModLoader", throwable);
    }

    public static String getCrashReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("Mods loaded: ");
        sb.append(getLoadedMods().size() + 1);
        sb.append('\n');
        sb.append("ModLoader 1.3.2");
        sb.append('\n');
        Iterator i$ = getLoadedMods().iterator();

        while(i$.hasNext()) {
            BaseMod mod = (BaseMod)i$.next();
            sb.append(mod.getName());
            sb.append(' ');
            sb.append(mod.getVersion());
            sb.append('\n');
        }

        return sb.toString();
    }

    private ModLoader() {
    }

    static {
        cfgfile = new File(cfgdir, "ModLoader.cfg");
        cfgLoggingLevel = Level.FINER;
    }
}
