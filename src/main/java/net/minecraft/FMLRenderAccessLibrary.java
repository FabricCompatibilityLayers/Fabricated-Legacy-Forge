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
package net.minecraft;

import cpw.mods.fml.client.TextureFXManager;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLLog;
import net.minecraft.block.Block;
import net.minecraft.client.TextureManager;
import net.minecraft.client.BlockRenderer;
import net.minecraft.client.Sprite;
import net.minecraft.client.texture.ITexturePack;
import net.minecraft.world.BlockView;

import java.awt.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FMLRenderAccessLibrary {
    public FMLRenderAccessLibrary() {
    }

    public static Logger getLogger() {
        Logger l = Logger.getLogger("FMLRenderAccessLibrary");
        l.setParent(FMLLog.getLogger());
        return l;
    }

    public static void log(Level level, String message) {
        FMLLog.log("FMLRenderAccessLibrary", level, message, new Object[0]);
    }

    public static void log(Level level, String message, Throwable throwable) {
        FMLLog.log(level, throwable, message, new Object[0]);
    }

    public static void setTextureDimensions(int textureId, int width, int height, List<Sprite> textureFXList) {
        TextureFXManager.instance().setTextureDimensions(textureId, width, height, textureFXList);
    }

    public static void preRegisterEffect(Sprite textureFX) {
        TextureFXManager.instance().onPreRegisterEffect(textureFX);
    }

    public static boolean onUpdateTextureEffect(Sprite textureFX) {
        return TextureFXManager.instance().onUpdateTextureEffect(textureFX);
    }

    public static Dimension getTextureDimensions(Sprite textureFX) {
        return TextureFXManager.instance().getTextureDimensions(textureFX);
    }

    public static void onTexturePackChange(TextureManager engine, ITexturePack texturePack, List<Sprite> textureFXList) {
        TextureFXManager.instance().onTexturePackChange(engine, texturePack, textureFXList);
    }

    public static boolean renderWorldBlock(BlockRenderer renderer, BlockView world, int x, int y, int z, Block block, int modelId) {
        return RenderingRegistry.instance().renderWorldBlock(renderer, world, x, y, z, block, modelId);
    }

    public static void renderInventoryBlock(BlockRenderer renderer, Block block, int metadata, int modelID) {
        RenderingRegistry.instance().renderInventoryBlock(renderer, block, metadata, modelID);
    }

    public static boolean renderItemAsFull3DBlock(int modelId) {
        return RenderingRegistry.instance().renderItemAsFull3DBlock(modelId);
    }
}
