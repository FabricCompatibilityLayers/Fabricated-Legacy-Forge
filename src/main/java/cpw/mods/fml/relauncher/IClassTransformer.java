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
package cpw.mods.fml.relauncher;

import fr.catcore.fabricatedforge.util.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface IClassTransformer extends fr.catcore.modremapperapi.api.IClassTransformer {
    static final List<String> CLASS_NAMES = new ArrayList<>();

    Map<IClassTransformer, List<String>> transformed = new HashMap<>();

    byte[] transform(String string, byte[] bs);

    @Override
    default byte[] transformClass(String name, String transformedName, byte[] original) {
        if (original == null) return null;
        return this.transform(name, original);
    }

    @Override
    default boolean handlesClass(String s, String s1) {
        String className = this.getClass().getName();

        if (!CLASS_NAMES.contains(className)) {
            CLASS_NAMES.add(className);
            transformed.put(this, new ArrayList<>());
        }

        if (CLASS_NAMES.contains(s)) return false;

        for (String toExclude : Utils.TRANSFORMER_EXCLUSIONS) {
            if (s.startsWith(toExclude)) return false;
        }

        if (transformed.get(this).contains(s)) {
            throw new RuntimeException("Detected transformation loop for class " + s + " in ClassTransformer " + className);
        }

        transformed.get(this).add(s);

        if (s.equals("net.minecraft.class_415") && this.getClass().getName().equals("codechicken.nei.asm.NEITransformer")) return false;

        return true;
    }
}