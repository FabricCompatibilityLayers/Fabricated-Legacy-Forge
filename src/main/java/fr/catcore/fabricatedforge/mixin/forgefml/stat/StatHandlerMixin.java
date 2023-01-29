package fr.catcore.fabricatedforge.mixin.forgefml.stat;

import argo.jdom.JdomParser;
import argo.jdom.JsonNode;
import argo.jdom.JsonRootNode;
import argo.jdom.JsonStringNode;
import argo.saj.InvalidSyntaxException;
import cpw.mods.fml.common.asm.ReobfuscationMarker;
import net.minecraft.client.class_827;
import net.minecraft.stat.Stat;
import net.minecraft.stat.StatHandler;
import net.minecraft.stat.Stats;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.HashMap;
import java.util.Map;

@Mixin(StatHandler.class)
@ReobfuscationMarker
public class StatHandlerMixin {
    /**
     * @author forge
     * @reason change lib
     */
    @Overwrite
    public static Map method_1736(String par0Str) {
        HashMap var1 = new HashMap();

        try {
            String var2 = "local";
            StringBuilder var3 = new StringBuilder();
            JsonRootNode var4 = new JdomParser().parse(par0Str);

            for(JsonNode var7 : var4.getArrayNode(new Object[]{"stats-change"})) {
                Map var8 = var7.getFields();
                Map.Entry var9 = (Map.Entry)var8.entrySet().iterator().next();
                int var10 = Integer.parseInt(((JsonStringNode)var9.getKey()).getText());
                int var11 = Integer.parseInt(((JsonNode)var9.getValue()).getText());
                Stat var12 = Stats.getStat(var10);
                if (var12 == null) {
                    System.out.println(var10 + " is not a valid stat");
                } else {
                    var3.append(Stats.getStat(var10).field_3013).append(",");
                    var3.append(var11).append(",");
                    var1.put(var12, var11);
                }
            }

            class_827 var14 = new class_827(var2);
            String var15 = var14.method_2293(var3.toString());
            if (!var15.equals(var4.getStringValue(new Object[]{"checksum"}))) {
                System.out.println("CHECKSUM MISMATCH");
                return null;
            }
        } catch (InvalidSyntaxException var131) {
            var131.printStackTrace();
        }

        return var1;
    }
}
