package fr.catcore.fabricatedforge.compat;

import fr.catcore.fabricatedforge.compat.asm.ASMRemapperTransformer;
import fr.catcore.fabricatedforge.util.Utils;
import fr.catcore.modremapperapi.ClassTransformer;
import fr.catcore.modremapperapi.api.ModRemapper;
import fr.catcore.modremapperapi.remapping.RemapUtil;
import fr.catcore.modremapperapi.remapping.VisitorInfos;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExtraRemapper implements ModRemapper {
    @Override
    public String[] getJarFolders() {
        return new String[0];
    }

    @Override
    public Map<String, List<String>> getExclusions() {
        return new HashMap<>();
    }

    @Override
    public void getMappingList(RemapUtil.MappingList mappingList) {

    }

    @Override
    public void registerVisitors(VisitorInfos visitorInfos) {

    }

    @Override
    public void afterRemap() {
        Mixins.addConfiguration("fabricated-forge.mods.mixins.json");
        Utils.TRANSFORMER_EXCLUSIONS.add("fr.catcore.fabricatedforge.compat.asm.BetterClassWriter");
        ClassTransformer.registerPostTransformer(new ASMRemapperTransformer());
    }
}
