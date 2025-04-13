package fr.catcore.fabricatedforge.mixin.forgefml.client.gui.hud;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.class_469;
import net.minecraft.client.class_482;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.util.Window;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatUtil;
import net.minecraft.util.CommonI18n;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.LightType;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.ForgeHooks;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.awt.*;
import java.util.List;
import java.util.Random;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin extends DrawableHelper {

    @Shadow @Final private Minecraft field_1166;

    @Shadow protected abstract void method_978(float f, int i, int j);

    @Shadow protected abstract void method_980(int i, int j);

    @Shadow protected abstract void method_984(float f, int i, int j);

    @Shadow @Final private Random random;

    @Shadow private int ticks;

    @Shadow protected abstract void renderBossBar();

    @Shadow protected abstract void method_981(int i, int j, int k, float f);

    @Shadow private int overlayRemaining;

    @Shadow private boolean overlayTinted;

    @Shadow private String overlayMessage;

    @Shadow @Final private ChatHud chatHud;

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public void method_979(float par1, boolean par2, int par3, int par4) {
        Window var5 = new Window(this.field_1166.options, this.field_1166.width, this.field_1166.height);
        int var6 = var5.getWidth();
        int var7 = var5.getHeight();
        TextRenderer var8 = this.field_1166.textRenderer;
        this.field_1166.gameRenderer.setupHudMatrixMode();
        GL11.glEnable(3042);
        if (Minecraft.isFancyGraphicsEnabled()) {
            this.method_978(this.field_1166.playerEntity.getBrightnessAtEyes(par1), var6, var7);
        } else {
            GL11.glBlendFunc(770, 771);
        }

        ItemStack var9 = this.field_1166.playerEntity.inventory.getArmor(3);
        if (this.field_1166.options.perspective == 0 && var9 != null && var9.id == Block.PUMPKIN.id) {
            this.method_980(var6, var7);
        }

        if (!this.field_1166.playerEntity.method_2581(StatusEffect.NAUSEA)) {
            float var10 = this.field_1166.playerEntity.field_4010 + (this.field_1166.playerEntity.field_3997 - this.field_1166.playerEntity.field_4010) * par1;
            if (var10 > 0.0F) {
                this.method_984(var10, var6, var7);
            }
        }

        int var12;
        int var13;
        int var17;
        int var16;
        int var19;
        int var20;
        int var23;
        int var22;
        int var47;
        boolean var11;
        if (!this.field_1166.interactionManager.isSpectator()) {
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glBindTexture(3553, this.field_1166.textureManager.getTextureFromPath("/gui/gui.png"));
            PlayerInventory var31 = this.field_1166.playerEntity.inventory;
            this.zOffset = -90.0F;
            this.drawTexture(var6 / 2 - 91, var7 - 22, 0, 0, 182, 22);
            this.drawTexture(var6 / 2 - 91 - 1 + var31.selectedSlot * 20, var7 - 22 - 1, 0, 22, 24, 22);
            GL11.glBindTexture(3553, this.field_1166.textureManager.getTextureFromPath("/gui/icons.png"));
            GL11.glEnable(3042);
            GL11.glBlendFunc(775, 769);
            this.drawTexture(var6 / 2 - 7, var7 / 2 - 7, 0, 0, 16, 16);
            GL11.glDisable(3042);
            var11 = this.field_1166.playerEntity.timeUntilRegen / 3 % 2 == 1;
            if (this.field_1166.playerEntity.timeUntilRegen < 10) {
                var11 = false;
            }

            var12 = this.field_1166.playerEntity.method_2600();
            var13 = this.field_1166.playerEntity.field_3295;
            this.random.setSeed((long)(this.ticks * 312871));
            boolean var14 = false;
            HungerManager var15 = this.field_1166.playerEntity.getHungerManager();
            var16 = var15.getFoodLevel();
            var17 = var15.getPrevFoodLevel();
            this.field_1166.profiler.push("bossHealth");
            this.renderBossBar();
            this.field_1166.profiler.pop();
            int var18;
            if (this.field_1166.interactionManager.hasStatusBars()) {
                var18 = var6 / 2 - 91;
                var19 = var6 / 2 + 91;
                this.field_1166.profiler.push("expBar");
                var20 = this.field_1166.playerEntity.getNextLevelExperience();
                int var24;
                if (var20 > 0) {
                    var24 = 182;
                    var22 = (int)(this.field_1166.playerEntity.experienceProgress * (float)(var24 + 1));
                    var23 = var7 - 32 + 3;
                    this.drawTexture(var18, var23, 0, 64, var24, 5);
                    if (var22 > 0) {
                        this.drawTexture(var18, var23, 0, 69, var22, 5);
                    }
                }

                var47 = var7 - 39;
                var22 = var47 - 10;
                var23 = ForgeHooks.getTotalArmorValue(this.field_1166.playerEntity);
                var24 = -1;
                if (this.field_1166.playerEntity.method_2581(StatusEffect.REGENERATION)) {
                    var24 = this.ticks % 25;
                }

                this.field_1166.profiler.swap("healthArmor");

                int var25;
                int var26;
                int var29;
                int var28;
                int var52;
                byte var30;
                for(var25 = 0; var25 < 10; ++var25) {
                    if (var23 > 0) {
                        var26 = var18 + var25 * 8;
                        if (var25 * 2 + 1 < var23) {
                            this.drawTexture(var26, var22, 34, 9, 9, 9);
                        }

                        if (var25 * 2 + 1 == var23) {
                            this.drawTexture(var26, var22, 25, 9, 9, 9);
                        }

                        if (var25 * 2 + 1 > var23) {
                            this.drawTexture(var26, var22, 16, 9, 9, 9);
                        }
                    }

                    var26 = 16;
                    if (this.field_1166.playerEntity.method_2581(StatusEffect.POISON)) {
                        var26 += 36;
                    }

                    var52 = 0;
                    if (var11) {
                        var52 = 1;
                    }

                    var28 = var18 + var25 * 8;
                    var29 = var47;
                    if (var12 <= 4) {
                        var29 = var47 + this.random.nextInt(2);
                    }

                    if (var25 == var24) {
                        var29 -= 2;
                    }

                    var30 = 0;
                    if (this.field_1166.world.getLevelProperties().isHardcore()) {
                        var30 = 5;
                    }

                    this.drawTexture(var28, var29, 16 + var52 * 9, 9 * var30, 9, 9);
                    if (var11) {
                        if (var25 * 2 + 1 < var13) {
                            this.drawTexture(var28, var29, var26 + 54, 9 * var30, 9, 9);
                        }

                        if (var25 * 2 + 1 == var13) {
                            this.drawTexture(var28, var29, var26 + 63, 9 * var30, 9, 9);
                        }
                    }

                    if (var25 * 2 + 1 < var12) {
                        this.drawTexture(var28, var29, var26 + 36, 9 * var30, 9, 9);
                    }

                    if (var25 * 2 + 1 == var12) {
                        this.drawTexture(var28, var29, var26 + 45, 9 * var30, 9, 9);
                    }
                }

                this.field_1166.profiler.swap("food");

                for(var25 = 0; var25 < 10; ++var25) {
                    var26 = var47;
                    var52 = 16;
                    var30 = 0;
                    if (this.field_1166.playerEntity.method_2581(StatusEffect.HUNGER)) {
                        var52 += 36;
                        var30 = 13;
                    }

                    if (this.field_1166.playerEntity.getHungerManager().getSaturationLevel() <= 0.0F && this.ticks % (var16 * 3 + 1) == 0) {
                        var26 = var47 + (this.random.nextInt(3) - 1);
                    }

                    if (var14) {
                        var30 = 1;
                    }

                    var29 = var19 - var25 * 8 - 9;
                    this.drawTexture(var29, var26, 16 + var30 * 9, 27, 9, 9);
                    if (var14) {
                        if (var25 * 2 + 1 < var17) {
                            this.drawTexture(var29, var26, var52 + 54, 27, 9, 9);
                        }

                        if (var25 * 2 + 1 == var17) {
                            this.drawTexture(var29, var26, var52 + 63, 27, 9, 9);
                        }
                    }

                    if (var25 * 2 + 1 < var16) {
                        this.drawTexture(var29, var26, var52 + 36, 27, 9, 9);
                    }

                    if (var25 * 2 + 1 == var16) {
                        this.drawTexture(var29, var26, var52 + 45, 27, 9, 9);
                    }
                }

                this.field_1166.profiler.swap("air");
                if (this.field_1166.playerEntity.isSubmergedIn(Material.WATER)) {
                    var25 = this.field_1166.playerEntity.getAir();
                    var26 = MathHelper.ceil((double)(var25 - 2) * 10.0 / 300.0);
                    var52 = MathHelper.ceil((double)var25 * 10.0 / 300.0) - var26;

                    for(var28 = 0; var28 < var26 + var52; ++var28) {
                        if (var28 < var26) {
                            this.drawTexture(var19 - var28 * 8 - 9, var22, 16, 18, 9, 9);
                        } else {
                            this.drawTexture(var19 - var28 * 8 - 9, var22, 25, 18, 9, 9);
                        }
                    }
                }

                this.field_1166.profiler.pop();
            }

            GL11.glDisable(3042);
            this.field_1166.profiler.push("actionBar");
            GL11.glEnable(32826);
            DiffuseLighting.enable();

            for(var18 = 0; var18 < 9; ++var18) {
                var19 = var6 / 2 - 90 + var18 * 20 + 2;
                var20 = var7 - 16 - 3;
                this.method_981(var18, var19, var20, par1);
            }

            DiffuseLighting.disable();
            GL11.glDisable(32826);
            this.field_1166.profiler.pop();
        }

        float var33;
        int var38;
        if (this.field_1166.playerEntity.getSleepTimer() > 0) {
            this.field_1166.profiler.push("sleep");
            GL11.glDisable(2929);
            GL11.glDisable(3008);
            var38 = this.field_1166.playerEntity.getSleepTimer();
            var33 = (float)var38 / 100.0F;
            if (var33 > 1.0F) {
                var33 = 1.0F - (float)(var38 - 100) / 10.0F;
            }

            var12 = (int)(220.0F * var33) << 24 | 1052704;
            fill(0, 0, var6, var7, var12);
            GL11.glEnable(3008);
            GL11.glEnable(2929);
            this.field_1166.profiler.pop();
        }

        int var40;
        String var36;
        if (this.field_1166.interactionManager.hasExperienceBar() && this.field_1166.playerEntity.experienceLevel > 0) {
            this.field_1166.profiler.push("expLevel");
            var11 = false;
            var12 = var11 ? 16777215 : 8453920;
            var36 = "" + this.field_1166.playerEntity.experienceLevel;
            var40 = (var6 - var8.getStringWidth(var36)) / 2;
            var38 = var7 - 31 - 4;
            var8.method_964(var36, var40 + 1, var38, 0);
            var8.method_964(var36, var40 - 1, var38, 0);
            var8.method_964(var36, var40, var38 + 1, 0);
            var8.method_964(var36, var40, var38 - 1, 0);
            var8.method_964(var36, var40, var38, var12);
            this.field_1166.profiler.pop();
        }

        if (this.field_1166.isDemo()) {
            this.field_1166.profiler.push("demo");
            if (this.field_1166.world.getTimeOfDay() >= 120500L) {
                var36 = CommonI18n.translate("demo.demoExpired");
            } else {
                var36 = String.format(CommonI18n.translate("demo.remainingTime"), ChatUtil.ticksToString((int)(120500L - this.field_1166.world.getTimeOfDay())));
            }

            var12 = var8.getStringWidth(var36);
            var8.method_956(var36, var6 - var12 - 10, 5, 16777215);
            this.field_1166.profiler.pop();
        }

        if (this.field_1166.options.debugEnabled) {
            this.field_1166.profiler.push("debug");
            GL11.glPushMatrix();
            var8.method_956("Minecraft 1.3.2 (" + this.field_1166.fpsDebugString + ")", 2, 2, 16777215);
            var8.method_956(this.field_1166.getChunkDebugString(), 2, 12, 16777215);
            var8.method_956(this.field_1166.getEntitiesDebugString(), 2, 22, 16777215);
            var8.method_956(this.field_1166.method_2958(), 2, 32, 16777215);
            var8.method_956(this.field_1166.getWorldDebugString(), 2, 42, 16777215);
            long var41 = Runtime.getRuntime().maxMemory();
            long var34 = Runtime.getRuntime().totalMemory();
            long var42 = Runtime.getRuntime().freeMemory();
            long var43 = var34 - var42;
            String var45 = "Used memory: " + var43 * 100L / var41 + "% (" + var43 / 1024L / 1024L + "MB) of " + var41 / 1024L / 1024L + "MB";
            this.drawWithShadow(var8, var45, var6 - var8.getStringWidth(var45) - 2, 2, 14737632);
            var45 = "Allocated memory: " + var34 * 100L / var41 + "% (" + var34 / 1024L / 1024L + "MB)";
            this.drawWithShadow(var8, var45, var6 - var8.getStringWidth(var45) - 2, 12, 14737632);
            this.drawWithShadow(var8, String.format("x: %.5f", this.field_1166.playerEntity.x), 2, 64, 14737632);
            this.drawWithShadow(var8, String.format("y: %.3f (feet pos, %.3f eyes pos)", this.field_1166.playerEntity.boundingBox.minY, this.field_1166.playerEntity.y), 2, 72, 14737632);
            this.drawWithShadow(var8, String.format("z: %.5f", this.field_1166.playerEntity.z), 2, 80, 14737632);
            this.drawWithShadow(var8, "f: " + (MathHelper.floor((double)(this.field_1166.playerEntity.yaw * 4.0F / 360.0F) + 0.5) & 3), 2, 88, 14737632);
            var47 = MathHelper.floor(this.field_1166.playerEntity.x);
            var22 = MathHelper.floor(this.field_1166.playerEntity.y);
            var23 = MathHelper.floor(this.field_1166.playerEntity.z);
            if (this.field_1166.world != null && this.field_1166.world.isPosLoaded(var47, var22, var23)) {
                Chunk var48 = this.field_1166.world.getChunkFromPos(var47, var23);
                this.drawWithShadow(var8, "lc: " + (var48.getHighestNonEmptySectionYOffset() + 15) + " b: " + var48.getBiome(var47 & 15, var23 & 15, this.field_1166.world.getBiomeSource()).name + " bl: " + var48.method_3890(LightType.BLOCK, var47 & 15, var22, var23 & 15) + " sl: " + var48.method_3890(LightType.SKY, var47 & 15, var22, var23 & 15) + " rl: " + var48.method_3905(var47 & 15, var22, var23 & 15, 0), 2, 96, 14737632);
            }

            this.drawWithShadow(var8, String.format("ws: %.3f, fs: %.3f, g: %b", this.field_1166.playerEntity.abilities.getWalkSpeed(), this.field_1166.playerEntity.abilities.getFlySpeed(), this.field_1166.playerEntity.onGround), 2, 104, 14737632);
            GL11.glPopMatrix();
            this.field_1166.profiler.pop();
        }

        if (this.overlayRemaining > 0) {
            this.field_1166.profiler.push("overlayMessage");
            var33 = (float)this.overlayRemaining - par1;
            var12 = (int)(var33 * 256.0F / 20.0F);
            if (var12 > 255) {
                var12 = 255;
            }

            if (var12 > 0) {
                GL11.glPushMatrix();
                GL11.glTranslatef((float)(var6 / 2), (float)(var7 - 48), 0.0F);
                GL11.glEnable(3042);
                GL11.glBlendFunc(770, 771);
                var13 = 16777215;
                if (this.overlayTinted) {
                    var13 = Color.HSBtoRGB(var33 / 50.0F, 0.7F, 0.6F) & 16777215;
                }

                var8.method_964(this.overlayMessage, -var8.getStringWidth(this.overlayMessage) / 2, -4, var13 + (var12 << 24));
                GL11.glDisable(3042);
                GL11.glPopMatrix();
            }

            this.field_1166.profiler.pop();
        }

        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3008);
        GL11.glPushMatrix();
        GL11.glTranslatef(0.0F, (float)(var7 - 48), 0.0F);
        this.field_1166.profiler.push("chat");
        this.chatHud.render(this.ticks);
        this.field_1166.profiler.pop();
        GL11.glPopMatrix();
        if (this.field_1166.options.keyPlayerList.pressed && (!this.field_1166.isIntegratedServerRunning() || this.field_1166.playerEntity.field_1667.field_1618.size() > 1)) {
            this.field_1166.profiler.push("playerList");
            class_469 var37 = this.field_1166.playerEntity.field_1667;
            List var39 = var37.field_1618;
            var13 = var37.field_1619;
            var40 = var13;

            for(var38 = 1; var40 > 20; var40 = (var13 + var38 - 1) / var38) {
                ++var38;
            }

            var16 = 300 / var38;
            if (var16 > 150) {
                var16 = 150;
            }

            var17 = (var6 - var38 * var16) / 2;
            byte var44 = 10;
            fill(var17 - 1, var44 - 1, var17 + var16 * var38, var44 + 9 * var40, Integer.MIN_VALUE);

            for(var19 = 0; var19 < var13; ++var19) {
                var20 = var17 + var19 % var38 * var16;
                var47 = var44 + var19 / var38 * 9;
                fill(var20, var47, var20 + var16 - 1, var47 + 8, 553648127);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                GL11.glEnable(3008);
                if (var19 < var39.size()) {
                    class_482 var46 = (class_482)var39.get(var19);
                    var8.method_956(var46.field_1679, var20, var47, 16777215);
                    this.field_1166.textureManager.bindTexture(this.field_1166.textureManager.getTextureFromPath("/gui/icons.png"));
                    byte var51 = 0;
                    byte var50;
                    if (var46.field_1680 < 0) {
                        var50 = 5;
                    } else if (var46.field_1680 < 150) {
                        var50 = 0;
                    } else if (var46.field_1680 < 300) {
                        var50 = 1;
                    } else if (var46.field_1680 < 600) {
                        var50 = 2;
                    } else if (var46.field_1680 < 1000) {
                        var50 = 3;
                    } else {
                        var50 = 4;
                    }

                    this.zOffset += 100.0F;
                    this.drawTexture(var20 + var16 - 12, var47, 0 + var51 * 10, 176 + var50 * 8, 10, 8);
                    this.zOffset -= 100.0F;
                }
            }
        }

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(2896);
        GL11.glEnable(3008);
    }
}
