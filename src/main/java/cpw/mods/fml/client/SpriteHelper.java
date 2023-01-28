/*
 * The FML Forge Mod Loader suite.
 * Copyright (C) 2012 cpw
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this library; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 */
package cpw.mods.fml.client;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;

import java.util.BitSet;
import java.util.HashMap;
import java.util.logging.Level;

public class SpriteHelper {
    private static HashMap<String, BitSet> spriteInfo = new HashMap();

    public SpriteHelper() {
    }

    private static void initMCSpriteMaps() {
        BitSet slots = toBitSet(
                "0000000000000000000000000011000000000000001000000000000001100000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000111110000000000000000000000000111110000000000011110000000000000000000"
        );
        spriteInfo.put("/terrain.png", slots);
        slots = toBitSet(
                "0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000111110000000000111111101000000001111111100000000111111111111001111111111111111100000111111111110000000000000000"
        );
        spriteInfo.put("/gui/items.png", slots);
    }

    public static void registerSpriteMapForFile(String file, String spriteMap) {
        if (spriteInfo.size() == 0) {
            initMCSpriteMaps();
        }

        if (spriteInfo.containsKey(file)) {
            FMLCommonHandler.instance().getFMLLogger().finer(String.format("Duplicate attempt to register a sprite file %s for overriding -- ignoring", file));
        } else {
            spriteInfo.put(file, toBitSet(spriteMap));
        }
    }

    public static int getUniqueSpriteIndex(String path) {
        if (!spriteInfo.containsKey("/terrain.png")) {
            initMCSpriteMaps();
        }

        BitSet slots = (BitSet)spriteInfo.get(path);
        if (slots == null) {
            Exception ex = new Exception(String.format("Invalid getUniqueSpriteIndex call for texture: %s", path));
            FMLLog.log(Level.SEVERE, ex, "A critical error has been detected with sprite overrides", new Object[0]);
            FMLCommonHandler.instance().raiseException(ex, "Invalid request to getUniqueSpriteIndex", true);
        }

        int ret = getFreeSlot(slots);
        if (ret == -1) {
            Exception ex = new Exception(String.format("No more sprite indicies left for: %s", path));
            FMLLog.log(Level.SEVERE, ex, "There are no sprite indicies left for %s", new Object[]{path});
            FMLCommonHandler.instance().raiseException(ex, "No more sprite indicies left", true);
        }

        return ret;
    }

    public static BitSet toBitSet(String data) {
        BitSet ret = new BitSet(data.length());

        for(int x = 0; x < data.length(); ++x) {
            ret.set(x, data.charAt(x) == '1');
        }

        return ret;
    }

    public static int getFreeSlot(BitSet slots) {
        int next = slots.nextSetBit(0);
        slots.clear(next);
        return next;
    }

    public static int freeSlotCount(String textureToOverride) {
        return ((BitSet)spriteInfo.get(textureToOverride)).cardinality();
    }
}
