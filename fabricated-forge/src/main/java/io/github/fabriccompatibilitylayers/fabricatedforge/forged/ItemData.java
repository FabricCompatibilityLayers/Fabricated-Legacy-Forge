/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.forged;

import io.github.fabriccompatibilitylayers.fabricatedforge.utils.FakeList;
import net.minecraft.src.Item;

import java.util.Objects;

public final class ItemData extends FakeList {
    public final Item item;
    public final int damage;

    public ItemData(int itemId, int damage) {
        this.item = Item.itemsList[itemId];
        this.damage = damage;
    }

    public ItemData(Item item, int damage) {
        this.item = item;
        this.damage = damage;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ItemData))
            return false;

        ItemData other = (ItemData) obj;
        return this.item == other.item && this.damage == other.damage;
    }

    @Override
    public int hashCode() {
        return Objects.hash(item, damage);
    }

    @Override
    public Object get(int index) {
        if (index == 0) return item.shiftedIndex;
        if (index == 1) return damage;

        return null;
    }
}
