package fr.catcore.fabricatedforge.compat;

import fr.catcore.fabricatedforge.compat.asm.ASMRemapperTransformer;
import fr.catcore.modremapperapi.ClassTransformer;
import io.github.fabriccompatibiltylayers.modremappingapi.api.v1.MappingBuilder;
import io.github.fabriccompatibiltylayers.modremappingapi.api.v1.ModRemapper;
import io.github.fabriccompatibiltylayers.modremappingapi.api.v1.RemapLibrary;
import io.github.fabriccompatibiltylayers.modremappingapi.api.v1.VisitorInfos;
import net.fabricmc.api.EnvType;
import org.spongepowered.asm.mixin.Mixins;

import java.util.List;

public class ExtraRemapper implements ModRemapper {
    @Override
    public String[] getJarFolders() {
        return new String[0];
    }

    @Override
    public void addRemapLibraries(List<RemapLibrary> list, EnvType envType) {

    }

    @Override
    public void registerMappings(MappingBuilder mappingBuilder) {

    }

    @Override
    public void registerPreVisitors(VisitorInfos visitorInfos) {

    }

    @Override
    public void registerPostVisitors(VisitorInfos visitorInfos) {

    }

    @Override
    public void afterRemap() {
        Mixins.addConfiguration("fabricated-forge.mods.mixins.json");

        try {
            Class.forName("fr.catcore.fabricatedforge.compat.asm.RemapAwareClass");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        ClassTransformer.registerPostTransformer(new ASMRemapperTransformer());
    }
}
