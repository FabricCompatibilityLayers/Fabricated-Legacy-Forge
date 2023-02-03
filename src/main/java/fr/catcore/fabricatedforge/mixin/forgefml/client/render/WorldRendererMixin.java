package fr.catcore.fabricatedforge.mixin.forgefml.client.render;

import fr.catcore.fabricatedforge.mixininterface.IParticleManager;
import fr.catcore.fabricatedforge.mixininterface.IWorldRenderer;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.material.Material;
import net.minecraft.client.*;
import net.minecraft.client.particle.*;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.IRenderHandler;
import net.minecraftforge.client.MinecraftForgeClient;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.*;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin implements IWorldRenderer {

    @Shadow public ClientWorld world;

    @Shadow public Minecraft client;

    @Shadow @Final public TextureManager textureManager;

    @Shadow private int lightSkyList;

    @Shadow private int starsList;

    @Shadow private int darkSkyList;

    @Shadow public BlockRenderer blockRenderer;

    @Shadow public Map<Integer, BlockBreakingInfo> blockBreakingInfos;

    @Shadow private int ticks;

    @Shadow public abstract void method_1382(float tickDelta);

    @Shadow private int totalEntityCount;

    @Shadow private int rendererdEntityCount;

    @Shadow private int hiddenEntityCount;

    @Shadow private int blockEntityCount;

    @Shadow public List blockEntities;

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public void renderSky(float par1) {
        IRenderHandler skyProvider = null;
        if ((skyProvider = this.client.world.dimension.getSkyRenderer()) != null) {
            skyProvider.render(par1, this.world, this.client);
        } else {
            if (this.client.world.dimension.dimensionType == 1) {
                GL11.glDisable(2912);
                GL11.glDisable(3008);
                GL11.glEnable(3042);
                GL11.glBlendFunc(770, 771);
                DiffuseLighting.disable();
                GL11.glDepthMask(false);
                this.textureManager.bindTexture(this.textureManager.getTextureFromPath("/misc/tunnel.png"));
                Tessellator var21 = Tessellator.INSTANCE;

                for(int var22 = 0; var22 < 6; ++var22) {
                    GL11.glPushMatrix();
                    if (var22 == 1) {
                        GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
                    }

                    if (var22 == 2) {
                        GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
                    }

                    if (var22 == 3) {
                        GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
                    }

                    if (var22 == 4) {
                        GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F);
                    }

                    if (var22 == 5) {
                        GL11.glRotatef(-90.0F, 0.0F, 0.0F, 1.0F);
                    }

                    var21.begin();
                    var21.color(2631720);
                    var21.vertex(-100.0, -100.0, -100.0, 0.0, 0.0);
                    var21.vertex(-100.0, -100.0, 100.0, 0.0, 16.0);
                    var21.vertex(100.0, -100.0, 100.0, 16.0, 16.0);
                    var21.vertex(100.0, -100.0, -100.0, 16.0, 0.0);
                    var21.end();
                    GL11.glPopMatrix();
                }

                GL11.glDepthMask(true);
                GL11.glEnable(3553);
                GL11.glEnable(3008);
            } else if (this.client.world.dimension.canPlayersSleep()) {
                GL11.glDisable(3553);
                Vec3d var2 = this.world.method_3631(this.client.cameraEntity, par1);
                float var3 = (float)var2.x;
                float var4 = (float)var2.y;
                float var5 = (float)var2.z;
                if (this.client.options.anaglyph3d) {
                    float var6 = (var3 * 30.0F + var4 * 59.0F + var5 * 11.0F) / 100.0F;
                    float var7 = (var3 * 30.0F + var4 * 70.0F) / 100.0F;
                    float var8 = (var3 * 30.0F + var5 * 70.0F) / 100.0F;
                    var3 = var6;
                    var4 = var7;
                    var5 = var8;
                }

                GL11.glColor3f(var3, var4, var5);
                Tessellator var23 = Tessellator.INSTANCE;
                GL11.glDepthMask(false);
                GL11.glEnable(2912);
                GL11.glColor3f(var3, var4, var5);
                GL11.glCallList(this.lightSkyList);
                GL11.glDisable(2912);
                GL11.glDisable(3008);
                GL11.glEnable(3042);
                GL11.glBlendFunc(770, 771);
                DiffuseLighting.disable();
                float[] var24 = this.world.dimension.getBackgroundColor(this.world.getSkyAngle(par1), par1);
                if (var24 != null) {
                    GL11.glDisable(3553);
                    GL11.glShadeModel(7425);
                    GL11.glPushMatrix();
                    GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
                    GL11.glRotatef(MathHelper.sin(this.world.getSkyAngleRadians(par1)) < 0.0F ? 180.0F : 0.0F, 0.0F, 0.0F, 1.0F);
                    GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F);
                    float var8 = var24[0];
                    float var9 = var24[1];
                    float var10 = var24[2];
                    if (this.client.options.anaglyph3d) {
                        float var11 = (var8 * 30.0F + var9 * 59.0F + var10 * 11.0F) / 100.0F;
                        float var12 = (var8 * 30.0F + var9 * 70.0F) / 100.0F;
                        float var13 = (var8 * 30.0F + var10 * 70.0F) / 100.0F;
                        var8 = var11;
                        var9 = var12;
                        var10 = var13;
                    }

                    var23.begin(6);
                    var23.color(var8, var9, var10, var24[3]);
                    var23.vertex(0.0, 100.0, 0.0);
                    byte var26 = 16;
                    var23.color(var24[0], var24[1], var24[2], 0.0F);

                    for(int var27 = 0; var27 <= var26; ++var27) {
                        float var13 = (float)var27 * (float) Math.PI * 2.0F / (float)var26;
                        float var14 = MathHelper.sin(var13);
                        float var15 = MathHelper.cos(var13);
                        var23.vertex((double)(var14 * 120.0F), (double)(var15 * 120.0F), (double)(-var15 * 40.0F * var24[3]));
                    }

                    var23.end();
                    GL11.glPopMatrix();
                    GL11.glShadeModel(7424);
                }

                GL11.glEnable(3553);
                GL11.glBlendFunc(770, 1);
                GL11.glPushMatrix();
                float var8 = 1.0F - this.world.getRainGradient(par1);
                float var9 = 0.0F;
                float var10 = 0.0F;
                float var11 = 0.0F;
                GL11.glColor4f(1.0F, 1.0F, 1.0F, var8);
                GL11.glTranslatef(var9, var10, var11);
                GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(this.world.getSkyAngle(par1) * 360.0F, 1.0F, 0.0F, 0.0F);
                float var12 = 30.0F;
                GL11.glBindTexture(3553, this.textureManager.getTextureFromPath("/terrain/sun.png"));
                var23.begin();
                var23.vertex((double)(-var12), 100.0, (double)(-var12), 0.0, 0.0);
                var23.vertex((double)var12, 100.0, (double)(-var12), 1.0, 0.0);
                var23.vertex((double)var12, 100.0, (double)var12, 1.0, 1.0);
                var23.vertex((double)(-var12), 100.0, (double)var12, 0.0, 1.0);
                var23.end();
                var12 = 20.0F;
                GL11.glBindTexture(3553, this.textureManager.getTextureFromPath("/terrain/moon_phases.png"));
                int var28 = this.world.method_3679(par1);
                int var30 = var28 % 4;
                int var29 = var28 / 4 % 2;
                float var16 = (float)(var30 + 0) / 4.0F;
                float var17 = (float)(var29 + 0) / 2.0F;
                float var18 = (float)(var30 + 1) / 4.0F;
                float var19 = (float)(var29 + 1) / 2.0F;
                var23.begin();
                var23.vertex((double)(-var12), -100.0, (double)var12, (double)var18, (double)var19);
                var23.vertex((double)var12, -100.0, (double)var12, (double)var16, (double)var19);
                var23.vertex((double)var12, -100.0, (double)(-var12), (double)var16, (double)var17);
                var23.vertex((double)(-var12), -100.0, (double)(-var12), (double)var18, (double)var17);
                var23.end();
                GL11.glDisable(3553);
                float var20 = this.world.method_3707(par1) * var8;
                if (var20 > 0.0F) {
                    GL11.glColor4f(var20, var20, var20, var20);
                    GL11.glCallList(this.starsList);
                }

                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                GL11.glDisable(3042);
                GL11.glEnable(3008);
                GL11.glEnable(2912);
                GL11.glPopMatrix();
                GL11.glDisable(3553);
                GL11.glColor3f(0.0F, 0.0F, 0.0F);
                double var25 = this.client.playerEntity.method_2663(par1).y - this.world.getHorizonHeight();
                if (var25 < 0.0) {
                    GL11.glPushMatrix();
                    GL11.glTranslatef(0.0F, 12.0F, 0.0F);
                    GL11.glCallList(this.darkSkyList);
                    GL11.glPopMatrix();
                    var10 = 1.0F;
                    var11 = -((float)(var25 + 65.0));
                    var12 = -var10;
                    var23.begin();
                    var23.color(0, 255);
                    var23.vertex((double)(-var10), (double)var11, (double)var10);
                    var23.vertex((double)var10, (double)var11, (double)var10);
                    var23.vertex((double)var10, (double)var12, (double)var10);
                    var23.vertex((double)(-var10), (double)var12, (double)var10);
                    var23.vertex((double)(-var10), (double)var12, (double)(-var10));
                    var23.vertex((double)var10, (double)var12, (double)(-var10));
                    var23.vertex((double)var10, (double)var11, (double)(-var10));
                    var23.vertex((double)(-var10), (double)var11, (double)(-var10));
                    var23.vertex((double)var10, (double)var12, (double)(-var10));
                    var23.vertex((double)var10, (double)var12, (double)var10);
                    var23.vertex((double)var10, (double)var11, (double)var10);
                    var23.vertex((double)var10, (double)var11, (double)(-var10));
                    var23.vertex((double)(-var10), (double)var11, (double)(-var10));
                    var23.vertex((double)(-var10), (double)var11, (double)var10);
                    var23.vertex((double)(-var10), (double)var12, (double)var10);
                    var23.vertex((double)(-var10), (double)var12, (double)(-var10));
                    var23.vertex((double)(-var10), (double)var12, (double)(-var10));
                    var23.vertex((double)(-var10), (double)var12, (double)var10);
                    var23.vertex((double)var10, (double)var12, (double)var10);
                    var23.vertex((double)var10, (double)var12, (double)(-var10));
                    var23.end();
                }

                if (this.world.dimension.hasGround()) {
                    GL11.glColor3f(var3 * 0.2F + 0.04F, var4 * 0.2F + 0.04F, var5 * 0.6F + 0.1F);
                } else {
                    GL11.glColor3f(var3, var4, var5);
                }

                GL11.glPushMatrix();
                GL11.glTranslatef(0.0F, -((float)(var25 - 16.0)), 0.0F);
                GL11.glCallList(this.darkSkyList);
                GL11.glPopMatrix();
                GL11.glEnable(3553);
                GL11.glDepthMask(true);
            }
        }
    }

    /**
     * @author forge
     * @reason hooks
     */
    @Overwrite
    public void method_1377(float par1) {
        IRenderHandler renderer = null;
        if ((renderer = this.world.dimension.getCloudRenderer()) != null) {
            renderer.render(par1, this.world, this.client);
        } else {
            if (this.client.world.dimension.canPlayersSleep()) {
                if (this.client.options.fancyGraphics) {
                    this.method_1382(par1);
                } else {
                    GL11.glDisable(2884);
                    float var2 = (float)(this.client.cameraEntity.prevTickY + (this.client.cameraEntity.y - this.client.cameraEntity.prevTickY) * (double)par1);
                    byte var3 = 32;
                    int var4 = 256 / var3;
                    Tessellator var5 = Tessellator.INSTANCE;
                    GL11.glBindTexture(3553, this.textureManager.getTextureFromPath("/environment/clouds.png"));
                    GL11.glEnable(3042);
                    GL11.glBlendFunc(770, 771);
                    Vec3d var6 = this.world.getCloudColor(par1);
                    float var7 = (float)var6.x;
                    float var8 = (float)var6.y;
                    float var9 = (float)var6.z;
                    if (this.client.options.anaglyph3d) {
                        float var10 = (var7 * 30.0F + var8 * 59.0F + var9 * 11.0F) / 100.0F;
                        float var11 = (var7 * 30.0F + var8 * 70.0F) / 100.0F;
                        float var12 = (var7 * 30.0F + var9 * 70.0F) / 100.0F;
                        var7 = var10;
                        var8 = var11;
                        var9 = var12;
                    }

                    float var10 = 4.8828125E-4F;
                    double var24 = (double)((float)this.ticks + par1);
                    double var13 = this.client.cameraEntity.prevX
                            + (this.client.cameraEntity.x - this.client.cameraEntity.prevX) * (double)par1
                            + var24 * 0.03F;
                    double var15 = this.client.cameraEntity.prevZ + (this.client.cameraEntity.z - this.client.cameraEntity.prevZ) * (double)par1;
                    int var17 = MathHelper.floor(var13 / 2048.0);
                    int var18 = MathHelper.floor(var15 / 2048.0);
                    var13 -= (double)(var17 * 2048);
                    var15 -= (double)(var18 * 2048);
                    float var19 = this.world.dimension.getCloudHeight() - var2 + 0.33F;
                    float var20 = (float)(var13 * (double)var10);
                    float var21 = (float)(var15 * (double)var10);
                    var5.begin();
                    var5.color(var7, var8, var9, 0.8F);

                    for(int var22 = -var3 * var4; var22 < var3 * var4; var22 += var3) {
                        for(int var23 = -var3 * var4; var23 < var3 * var4; var23 += var3) {
                            var5.vertex(
                                    (double)(var22 + 0),
                                    (double)var19,
                                    (double)(var23 + var3),
                                    (double)((float)(var22 + 0) * var10 + var20),
                                    (double)((float)(var23 + var3) * var10 + var21)
                            );
                            var5.vertex(
                                    (double)(var22 + var3),
                                    (double)var19,
                                    (double)(var23 + var3),
                                    (double)((float)(var22 + var3) * var10 + var20),
                                    (double)((float)(var23 + var3) * var10 + var21)
                            );
                            var5.vertex(
                                    (double)(var22 + var3),
                                    (double)var19,
                                    (double)(var23 + 0),
                                    (double)((float)(var22 + var3) * var10 + var20),
                                    (double)((float)(var23 + 0) * var10 + var21)
                            );
                            var5.vertex(
                                    (double)(var22 + 0),
                                    (double)var19,
                                    (double)(var23 + 0),
                                    (double)((float)(var22 + 0) * var10 + var20),
                                    (double)((float)(var23 + 0) * var10 + var21)
                            );
                        }
                    }

                    var5.end();
                    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                    GL11.glDisable(3042);
                    GL11.glEnable(2884);
                }
            }
        }
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public void method_1372(Tessellator par1Tessellator, PlayerEntity par2EntityPlayer, float par3) {
        this.drawBlockDamageTexture(par1Tessellator, par2EntityPlayer, par3);
    }

    @Override
    public void drawBlockDamageTexture(Tessellator par1Tessellator, MobEntity par2EntityPlayer, float par3) {
        double var4 = par2EntityPlayer.prevTickX + (par2EntityPlayer.x - par2EntityPlayer.prevTickX) * (double)par3;
        double var6 = par2EntityPlayer.prevTickY + (par2EntityPlayer.y - par2EntityPlayer.prevTickY) * (double)par3;
        double var8 = par2EntityPlayer.prevTickZ + (par2EntityPlayer.z - par2EntityPlayer.prevTickZ) * (double)par3;
        if (!this.blockBreakingInfos.isEmpty()) {
            GL11.glBlendFunc(774, 768);
            int var10 = this.textureManager.getTextureFromPath("/terrain.png");
            GL11.glBindTexture(3553, var10);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.5F);
            GL11.glPushMatrix();
            GL11.glDisable(3008);
            GL11.glPolygonOffset(-3.0F, -3.0F);
            GL11.glEnable(32823);
            GL11.glEnable(3008);
            par1Tessellator.begin();
            par1Tessellator.offset(-var4, -var6, -var8);
            par1Tessellator.setFixedColor();
            Iterator var11 = this.blockBreakingInfos.values().iterator();

            while(var11.hasNext()) {
                BlockBreakingInfo var12 = (BlockBreakingInfo)var11.next();
                double var13 = (double)var12.method_2090() - var4;
                double var15 = (double)var12.method_2091() - var6;
                double var17 = (double)var12.method_2092() - var8;
                if (var13 * var13 + var15 * var15 + var17 * var17 > 1024.0) {
                    var11.remove();
                } else {
                    int var19 = this.world.getBlock(var12.method_2090(), var12.method_2091(), var12.method_2092());
                    Block var20 = var19 > 0 ? Block.BLOCKS[var19] : null;
                    if (var20 == null) {
                        var20 = Block.STONE_BLOCK;
                    }

                    this.blockRenderer.render(var20, var12.method_2090(), var12.method_2091(), var12.method_2092(), 240 + var12.getStage());
                }
            }

            par1Tessellator.end();
            par1Tessellator.offset(0.0, 0.0, 0.0);
            GL11.glDisable(3008);
            GL11.glPolygonOffset(0.0F, 0.0F);
            GL11.glDisable(32823);
            GL11.glEnable(3008);
            GL11.glDepthMask(true);
            GL11.glPopMatrix();
        }
    }

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public Particle spawnParticleInternal(String par1Str, double par2, double par4, double par6, double par8, double par10, double par12) {
        if (this.client != null && this.client.cameraEntity != null && this.client.particleManager != null) {
            int var14 = this.client.options.particle;
            if (var14 == 1 && this.world.random.nextInt(3) == 0) {
                var14 = 2;
            }

            double var15 = this.client.cameraEntity.x - par2;
            double var17 = this.client.cameraEntity.y - par4;
            double var19 = this.client.cameraEntity.z - par6;
            Particle var21 = null;
            Object effectObject = null;
            if (par1Str.equals("hugeexplosion")) {
                this.client.particleManager.addParticle(var21 = new ExplosionEmitterParticle(this.world, par2, par4, par6, par8, par10, par12));
            } else if (par1Str.equals("largeexplode")) {
                this.client
                        .particleManager
                        .addParticle(var21 = new LargeExplosionParticle(this.textureManager, this.world, par2, par4, par6, par8, par10, par12));
            } else if (par1Str.equals("fireworksSpark")) {
                this.client.particleManager.addParticle(var21 = new class_1340(this.world, par2, par4, par6, par8, par10, par12, this.client.particleManager));
            }

            if (var21 != null) {
                return var21;
            } else {
                double var22 = 16.0;
                if (var15 * var15 + var17 * var17 + var19 * var19 > var22 * var22) {
                    return null;
                } else if (var14 > 1) {
                    return null;
                } else {
                    if (par1Str.equals("bubble")) {
                        var21 = new WaterBubbleParticle(this.world, par2, par4, par6, par8, par10, par12);
                    } else if (par1Str.equals("suspended")) {
                        var21 = new SuspendedParticle(this.world, par2, par4, par6, par8, par10, par12);
                    } else if (par1Str.equals("depthsuspend")) {
                        var21 = new VillageParticle(this.world, par2, par4, par6, par8, par10, par12);
                    } else if (par1Str.equals("townaura")) {
                        var21 = new VillageParticle(this.world, par2, par4, par6, par8, par10, par12);
                    } else if (par1Str.equals("crit")) {
                        var21 = new LargeFireSmokeParticle(this.world, par2, par4, par6, par8, par10, par12);
                    } else if (par1Str.equals("magicCrit")) {
                        var21 = new LargeFireSmokeParticle(this.world, par2, par4, par6, par8, par10, par12);
                        var21.setColor(var21.getRed() * 0.3F, var21.getGreen() * 0.8F, var21.getBlue());
                        var21.setMiscTexture(var21.method_1291() + 1);
                    } else if (par1Str.equals("smoke")) {
                        var21 = new FireSmokeParticle(this.world, par2, par4, par6, par8, par10, par12);
                    } else if (par1Str.equals("mobSpell")) {
                        var21 = new SpellParticle(this.world, par2, par4, par6, 0.0, 0.0, 0.0);
                        var21.setColor((float)par8, (float)par10, (float)par12);
                    } else if (par1Str.equals("mobSpellAmbient")) {
                        var21 = new SpellParticle(this.world, par2, par4, par6, 0.0, 0.0, 0.0);
                        var21.setColorAlpha(0.15F);
                        var21.setColor((float)par8, (float)par10, (float)par12);
                    } else if (par1Str.equals("spell")) {
                        var21 = new SpellParticle(this.world, par2, par4, par6, par8, par10, par12);
                    } else if (par1Str.equals("instantSpell")) {
                        var21 = new SpellParticle(this.world, par2, par4, par6, par8, par10, par12);
                        ((SpellParticle)var21).setTextureIndex(144);
                    } else if (par1Str.equals("witchMagic")) {
                        var21 = new SpellParticle(this.world, par2, par4, par6, par8, par10, par12);
                        ((SpellParticle)var21).setTextureIndex(144);
                        float var24 = this.world.random.nextFloat() * 0.5F + 0.35F;
                        var21.setColor(1.0F * var24, 0.0F * var24, 1.0F * var24);
                    } else if (par1Str.equals("note")) {
                        var21 = new NoteParticle(this.world, par2, par4, par6, par8, par10, par12);
                    } else if (par1Str.equals("portal")) {
                        var21 = new PortalParticle(this.world, par2, par4, par6, par8, par10, par12);
                    } else if (par1Str.equals("enchantmenttable")) {
                        var21 = new EnchantGlyphParticle(this.world, par2, par4, par6, par8, par10, par12);
                    } else if (par1Str.equals("explode")) {
                        var21 = new ExplosionSmokeParticle(this.world, par2, par4, par6, par8, par10, par12);
                    } else if (par1Str.equals("flame")) {
                        var21 = new FlameParticle(this.world, par2, par4, par6, par8, par10, par12);
                    } else if (par1Str.equals("lava")) {
                        var21 = new LavaEmberParticle(this.world, par2, par4, par6);
                    } else if (par1Str.equals("footstep")) {
                        var21 = new FootstepParticle(this.textureManager, this.world, par2, par4, par6);
                    } else if (par1Str.equals("splash")) {
                        var21 = new WaterSplashParticle(this.world, par2, par4, par6, par8, par10, par12);
                    } else if (par1Str.equals("largesmoke")) {
                        var21 = new FireSmokeParticle(this.world, par2, par4, par6, par8, par10, par12, 2.5F);
                    } else if (par1Str.equals("cloud")) {
                        var21 = new CloudParticle(this.world, par2, par4, par6, par8, par10, par12);
                    } else if (par1Str.equals("reddust")) {
                        var21 = new RedstoneParticle(this.world, par2, par4, par6, (float)par8, (float)par10, (float)par12);
                    } else if (par1Str.equals("snowballpoof")) {
                        var21 = new SnowballParticle(this.world, par2, par4, par6, Item.SNOWBALL);
                        effectObject = Item.SNOWBALL;
                    } else if (par1Str.equals("dripWater")) {
                        var21 = new BlockLeakParticle(this.world, par2, par4, par6, Material.WATER);
                    } else if (par1Str.equals("dripLava")) {
                        var21 = new BlockLeakParticle(this.world, par2, par4, par6, Material.LAVA);
                    } else if (par1Str.equals("snowshovel")) {
                        var21 = new SnowShovelParticle(this.world, par2, par4, par6, par8, par10, par12);
                    } else if (par1Str.equals("slime")) {
                        var21 = new SnowballParticle(this.world, par2, par4, par6, Item.SLIMEBALL);
                        effectObject = Item.SLIMEBALL;
                    } else if (par1Str.equals("heart")) {
                        var21 = new EmotionParticle(this.world, par2, par4, par6, par8, par10, par12);
                    } else if (par1Str.equals("angryVillager")) {
                        var21 = new EmotionParticle(this.world, par2, par4 + 0.5, par6, par8, par10, par12);
                        var21.setMiscTexture(81);
                        var21.setColor(1.0F, 1.0F, 1.0F);
                    } else if (par1Str.equals("happyVillager")) {
                        var21 = new VillageParticle(this.world, par2, par4, par6, par8, par10, par12);
                        var21.setMiscTexture(82);
                        var21.setColor(1.0F, 1.0F, 1.0F);
                    } else if (par1Str.startsWith("iconcrack_")) {
                        int var27 = Integer.parseInt(par1Str.substring(par1Str.indexOf("_") + 1));
                        var21 = new SnowballParticle(this.world, par2, par4, par6, par8, par10, par12, Item.ITEMS[var27]);
                        effectObject = Item.ITEMS[var27];
                    } else if (par1Str.startsWith("tilecrack_")) {
                        String[] var28 = par1Str.split("_", 3);
                        int var25 = Integer.parseInt(var28[1]);
                        int var26 = Integer.parseInt(var28[2]);
                        var21 = new BlockDustParticle(this.world, par2, par4, par6, par8, par10, par12, Block.BLOCKS[var25], 0, var26).method_4297(var26);
                        effectObject = Block.BLOCKS[var25];
                    }

                    if (var21 != null) {
                        ((IParticleManager)this.client.particleManager).addEffect(var21, effectObject);
                    }

                    return var21;
                }
            }
        } else {
            return null;
        }
    }

    /**
     * @author forge
     * @reason hook
     */
    @Overwrite
    public void method_1370(Vec3d par1Vec3, CameraView par2ICamera, float par3) {
        int pass = MinecraftForgeClient.getRenderPass();
        if (this.totalEntityCount > 0) {
            if (pass > 0) {
                return;
            }

            --this.totalEntityCount;
        } else {
            this.world.profiler.push("prepare");
            List var5 = this.world.getLoadedEntities();
            if (pass == 0) {
                BlockEntityRenderDispatcher.INSTANCE.initialize(this.world, this.textureManager, this.client.textRenderer, this.client.cameraEntity, par3);
                EntityRenderDispatcher.INSTANCE
                        .initialize(this.world, this.textureManager, this.client.textRenderer, this.client.cameraEntity, this.client.options, par3);
                this.rendererdEntityCount = 0;
                this.hiddenEntityCount = 0;
                this.blockEntityCount = 0;
                MobEntity var4 = this.client.cameraEntity;
                EntityRenderDispatcher.CAMERA_X = var4.prevTickX + (var4.x - var4.prevTickX) * (double)par3;
                EntityRenderDispatcher.CAMERA_Y = var4.prevTickY + (var4.y - var4.prevTickY) * (double)par3;
                EntityRenderDispatcher.CAMERA_Z = var4.prevTickZ + (var4.z - var4.prevTickZ) * (double)par3;
                BlockEntityRenderDispatcher.CAMERA_X = var4.prevTickX + (var4.x - var4.prevTickX) * (double)par3;
                BlockEntityRenderDispatcher.CAMERA_Y = var4.prevTickY + (var4.y - var4.prevTickY) * (double)par3;
                BlockEntityRenderDispatcher.CAMERA_Z = var4.prevTickZ + (var4.z - var4.prevTickZ) * (double)par3;
                this.rendererdEntityCount = var5.size();
            }

            this.client.gameRenderer.beforeWorldRender((double)par3);
            this.world.profiler.swap("global");

            for(int var6 = 0; var6 < this.world.entities.size(); ++var6) {
                Entity var7 = (Entity)this.world.entities.get(var6);
                if (var7.shouldRenderInPass(pass) && var7.shouldRender(par1Vec3)) {
                    ++this.hiddenEntityCount;
                    EntityRenderDispatcher.INSTANCE.render(var7, par3);
                }
            }

            this.world.profiler.swap("entities");

            for(int var10 = 0; var10 < var5.size(); ++var10) {
                Entity var7 = (Entity)var5.get(var10);
                if (var7.shouldRenderInPass(pass)
                        && var7.shouldRender(par1Vec3)
                        && (var7.ignoreCameraFrustum || par2ICamera.isBoxInFrustum(var7.boundingBox) || var7.rider == this.client.playerEntity)
                        && (var7 != this.client.cameraEntity || this.client.options.perspective != 0 || this.client.cameraEntity.method_2641())
                        && this.world.isPosLoaded(MathHelper.floor(var7.x), 0, MathHelper.floor(var7.z))) {
                    ++this.hiddenEntityCount;
                    EntityRenderDispatcher.INSTANCE.render(var7, par3);
                }
            }

            this.world.profiler.swap("tileentities");
            DiffuseLighting.enableNormally();

            for(int var11 = 0; var11 < this.blockEntities.size(); ++var11) {
                BlockEntity te = (BlockEntity)this.blockEntities.get(var11);
                if (par2ICamera.isBoxInFrustum(te.getRenderBoundingBox()) && te.shouldRenderInPass(pass)) {
                    BlockEntityRenderDispatcher.INSTANCE.render(te, par3);
                }
            }

            this.client.gameRenderer.afterWorldRender((double)par3);
            this.world.profiler.pop();
        }
    }
}
