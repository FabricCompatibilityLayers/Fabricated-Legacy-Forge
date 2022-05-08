package cpw.mods.fml.client.registry;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;

public class ClientRegistry {
    public ClientRegistry() {
    }

    public static void registerTileEntity(Class<? extends BlockEntity> tileEntityClass, String id, BlockEntityRenderer specialRenderer) {
        GameRegistry.registerTileEntity(tileEntityClass, id);
        bindTileEntitySpecialRenderer(tileEntityClass, specialRenderer);
    }

    public static void bindTileEntitySpecialRenderer(Class<? extends BlockEntity> tileEntityClass, BlockEntityRenderer specialRenderer) {
        BlockEntityRenderDispatcher.INSTANCE.renderers.put(tileEntityClass, specialRenderer);
        specialRenderer.setDispatcher(BlockEntityRenderDispatcher.INSTANCE);
    }
}
