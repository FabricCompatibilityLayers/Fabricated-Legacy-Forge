package fr.catcore.fabricatedforge.mixin.forgefml.client;

import net.minecraft.client.ClientBrandRetriever;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(ClientBrandRetriever.class)
public class ClientBrandRetrieverMixin {

    /**
     * @author CatCore
     */
    @Overwrite
    public static String getClientModName() {
        return "forge,fml on fabric";
    }
}
