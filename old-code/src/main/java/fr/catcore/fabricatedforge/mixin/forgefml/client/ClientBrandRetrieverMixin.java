package fr.catcore.fabricatedforge.mixin.forgefml.client;

import net.minecraft.client.ClientBrandRetriever;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(ClientBrandRetriever.class)
public class ClientBrandRetrieverMixin {

    /**
     * @author CatCore
     * @reason none
     */
    @Overwrite
    public static String getClientModName() {
        return "forge,fml on fabric";
    }
}
