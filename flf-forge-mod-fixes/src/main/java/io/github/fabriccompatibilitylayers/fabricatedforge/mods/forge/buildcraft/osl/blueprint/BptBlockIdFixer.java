/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mods.forge.buildcraft.osl.blueprint;

import buildcraft.api.blueprints.BlueprintManager;
import buildcraft.api.blueprints.BptBlock;
import io.github.fabriccompatibilitylayers.fabricatedforge.mods.forge.mixin.buildcraft.osl.blueprint.BptBlockAccessor;
import net.ornithemc.osl.registries.api.registry.sync.IdFixer;

public class BptBlockIdFixer implements IdFixer {
    @Override
    public void apply() {
        for (int i = 0; i < BlueprintManager.blockBptProps.length; i++) {
            BptBlock bptBlock = BlueprintManager.blockBptProps[i];

            if (bptBlock != null) {
                ((BptBlockAccessor) bptBlock).setBlockId(i);
            }
        }
    }
}
