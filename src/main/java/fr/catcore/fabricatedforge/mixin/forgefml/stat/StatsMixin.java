package fr.catcore.fabricatedforge.mixin.forgefml.stat;

import net.minecraft.block.Block;
import net.minecraft.stat.CraftingStat;
import net.minecraft.stat.Stat;
import net.minecraft.stat.Stats;
import net.minecraft.util.CommonI18n;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(Stats.class)
public abstract class StatsMixin {

    @Shadow public static List MINE;

    @Shadow
    private static void method_2272(Stat[] stats) {
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public static Stat[] method_2271(String par0Str, int par1) {
        Stat[] var2 = new Stat[Block.BLOCKS.length];

        for(int var3 = 0; var3 < Block.BLOCKS.length; ++var3) {
            if (Block.BLOCKS[var3] != null && Block.BLOCKS[var3].hasStats()) {
                String var4 = CommonI18n.translate(par0Str, new Object[]{Block.BLOCKS[var3].getTranslatedName()});
                var2[var3] = new CraftingStat(par1 + var3, var4, var3).addStat();
                MINE.add((CraftingStat)var2[var3]);
            }
        }

        method_2272(var2);
        return var2;
    }
}
