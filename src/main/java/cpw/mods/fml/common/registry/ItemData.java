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
package cpw.mods.fml.common.registry;

import com.google.common.base.Objects;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Maps;
import com.google.common.collect.Multiset;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.LoaderException;
import cpw.mods.fml.common.ModContainer;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;

import java.util.Map;

public class ItemData {
    private static Map<String, Multiset<String>> modOrdinals = Maps.newHashMap();
    private final String modId;
    private final String itemType;
    private final int itemId;
    private final int ordinal;
    private String forcedModId;
    private String forcedName;

    public ItemData(Item item, ModContainer mc) {
        this.itemId = item.id;
        if (item.getClass().equals(BlockItem.class)) {
            this.itemType = Block.BLOCKS[this.getItemId()].getClass().getName();
        } else {
            this.itemType = item.getClass().getName();
        }

        this.modId = mc.getModId();
        if (!modOrdinals.containsKey(mc.getModId())) {
            modOrdinals.put(mc.getModId(), HashMultiset.create());
        }

        this.ordinal = ((Multiset)modOrdinals.get(mc.getModId())).add(this.itemType, 1);
    }

    public ItemData(NbtCompound tag) {
        this.modId = tag.getString("ModId");
        this.itemType = tag.getString("ItemType");
        this.itemId = tag.getInt("ItemId");
        this.ordinal = tag.getInt("ordinal");
        this.forcedModId = tag.contains("ForcedModId") ? tag.getString("ForcedModId") : null;
        this.forcedName = tag.contains("ForcedName") ? tag.getString("ForcedName") : null;
    }

    public String getItemType() {
        return this.forcedName != null ? this.forcedName : this.itemType;
    }

    public String getModId() {
        return this.forcedModId != null ? this.forcedModId : this.modId;
    }

    public int getOrdinal() {
        return this.ordinal;
    }

    public int getItemId() {
        return this.itemId;
    }

    public NbtCompound toNBT() {
        NbtCompound tag = new NbtCompound();
        tag.putString("ModId", this.modId);
        tag.putString("ItemType", this.itemType);
        tag.putInt("ItemId", this.itemId);
        tag.putInt("ordinal", this.ordinal);
        if (this.forcedModId != null) {
            tag.putString("ForcedModId", this.forcedModId);
        }

        if (this.forcedName != null) {
            tag.putString("ForcedName", this.forcedName);
        }

        return tag;
    }

    public int hashCode() {
        return Objects.hashCode(new Object[]{this.itemId, this.ordinal});
    }

    public boolean equals(Object obj) {
        try {
            ItemData other = (ItemData)obj;
            return Objects.equal(this.getModId(), other.getModId())
                    && Objects.equal(this.getItemType(), other.getItemType())
                    && Objects.equal(this.itemId, other.itemId)
                    && (this.isOveridden() || Objects.equal(this.ordinal, other.ordinal));
        } catch (ClassCastException var3) {
            return false;
        }
    }

    public String toString() {
        return String.format(
                "Item %d, Type %s, owned by %s, ordinal %d, name %s, claimedModId %s",
                this.itemId,
                this.itemType,
                this.modId,
                this.ordinal,
                this.forcedName,
                this.forcedModId
        );
    }

    public boolean mayDifferByOrdinal(ItemData rightValue) {
        return Objects.equal(this.getItemType(), rightValue.getItemType()) && Objects.equal(this.getModId(), rightValue.getModId());
    }

    public boolean isOveridden() {
        return this.forcedName != null;
    }

    public void setName(String name, String modId) {
        if (name == null) {
            this.forcedName = null;
            this.forcedModId = null;
        } else {
            String localModId = modId;
            if (modId == null) {
                localModId = Loader.instance().activeModContainer().getModId();
            }

            if (((Multiset)modOrdinals.get(localModId)).count(name) > 0) {
                FMLLog.severe(
                        "The mod %s is attempting to redefine the item at id %d with a non-unique name (%s.%s)",
                        new Object[]{Loader.instance().activeModContainer(), this.itemId, localModId, name}
                );
                throw new LoaderException();
            } else {
                ((Multiset)modOrdinals.get(localModId)).add(name);
                this.forcedModId = modId;
                this.forcedName = name;
            }
        }
    }
}
