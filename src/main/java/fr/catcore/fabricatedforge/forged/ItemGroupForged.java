package fr.catcore.fabricatedforge.forged;

import net.minecraft.item.itemgroup.ItemGroup;

public class ItemGroupForged extends ItemGroup {
    public ItemGroupForged(int index, String id) {
        super(index, id);
    }

    public ItemGroupForged(String id) {
        this(ItemGroup.itemGroups.length, id);
    }
}
