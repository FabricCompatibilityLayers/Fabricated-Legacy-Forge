package fr.catcore.fabricatedforge.mixin.forgefml.structure;

import cpw.mods.fml.common.registry.VillagerRegistry;
import net.minecraft.structure.*;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

@Mixin(VillagePieces.class)
public abstract class VillagePiecesMixin {

    @Shadow
    private static StructurePiece method_92(class_50 arg, List list, Random random, int i, int j, int k, int l, int m) {
        return null;
    }

    @Shadow
    private static StructurePiece method_93(class_50 arg, List list, Random random, int i, int j, int k, int l, int m) {
        return null;
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public static ArrayList method_89(Random par0Random, int par1) {
        ArrayList var2 = new ArrayList<>();
        var2.add(new class_44(class_46.class, 4, MathHelper.nextInt(par0Random, 2 + par1, 4 + par1 * 2)));
        var2.add(new class_44(class_48.class, 20, MathHelper.nextInt(par0Random, par1, 1 + par1)));
        var2.add(new class_44(class_40.class, 20, MathHelper.nextInt(par0Random, par1, 2 + par1)));
        var2.add(new class_44(class_47.class, 3, MathHelper.nextInt(par0Random, 2 + par1, 5 + par1 * 3)));
        var2.add(new class_44(class_45.class, 15, MathHelper.nextInt(par0Random, par1, 2 + par1)));
        var2.add(new class_44(class_41.class, 3, MathHelper.nextInt(par0Random, 1 + par1, 4 + par1)));
        var2.add(new class_44(class_42.class, 3, MathHelper.nextInt(par0Random, 2 + par1, 4 + par1 * 2)));
        var2.add(new class_44(class_49.class, 15, MathHelper.nextInt(par0Random, 0, 1 + par1)));
        var2.add(new class_44(class_52.class, 8, MathHelper.nextInt(par0Random, par1, 3 + par1 * 2)));
        VillagerRegistry.addExtraVillageComponents(var2, par0Random, par1);

        var2.removeIf(o -> ((class_44) o).field_81 == 0);

        return var2;
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    private static class_53 method_86(class_50 par0ComponentVillageStartPiece, class_44 par1StructureVillagePieceWeight, List par2List, Random par3Random, int par4, int par5, int par6, int par7, int par8) {
        Class<?> var9 = par1StructureVillagePieceWeight.field_78;
        Object var10;
        if (var9 == class_46.class) {
            var10 = class_46.method_101(par0ComponentVillageStartPiece, par2List, par3Random, par4, par5, par6, par7, par8);
        } else if (var9 == class_48.class) {
            var10 = class_48.method_103(par0ComponentVillageStartPiece, par2List, par3Random, par4, par5, par6, par7, par8);
        } else if (var9 == class_40.class) {
            var10 = class_40.method_94(par0ComponentVillageStartPiece, par2List, par3Random, par4, par5, par6, par7, par8);
        } else if (var9 == class_47.class) {
            var10 = class_47.method_102(par0ComponentVillageStartPiece, par2List, par3Random, par4, par5, par6, par7, par8);
        } else if (var9 == class_45.class) {
            var10 = class_45.method_100(par0ComponentVillageStartPiece, par2List, par3Random, par4, par5, par6, par7, par8);
        } else if (var9 == class_41.class) {
            var10 = class_41.method_95(par0ComponentVillageStartPiece, par2List, par3Random, par4, par5, par6, par7, par8);
        } else if (var9 == class_42.class) {
            var10 = class_42.method_96(par0ComponentVillageStartPiece, par2List, par3Random, par4, par5, par6, par7, par8);
        } else if (var9 == class_49.class) {
            var10 = class_49.method_104(par0ComponentVillageStartPiece, par2List, par3Random, par4, par5, par6, par7, par8);
        } else if (var9 == class_52.class) {
            var10 = class_52.method_107(par0ComponentVillageStartPiece, par2List, par3Random, par4, par5, par6, par7, par8);
        } else {
            var10 = VillagerRegistry.getVillageComponent(par1StructureVillagePieceWeight, par0ComponentVillageStartPiece, par2List, par3Random, par4, par5, par6, par7, par8);
        }

        return (class_53)var10;
    }
}
