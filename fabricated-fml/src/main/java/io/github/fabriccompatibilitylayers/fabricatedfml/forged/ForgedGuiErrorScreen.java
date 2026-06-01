/**
 * Copyright (C) 2025 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedfml.forged;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.GuiScreen;

@Environment(EnvType.CLIENT)
public class ForgedGuiErrorScreen  extends GuiScreen {
    private String field_74001_a;
    private String field_74000_b;

    public ForgedGuiErrorScreen() {

    }

    @Override
    public void func_73863_a(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
        this.func_73733_a(0, 0, this.field_73880_f, this.field_73881_g, -12574688, -11530224);
        this.func_73732_a(this.field_73886_k, this.field_74001_a, this.field_73880_f / 2, 90, 16777215);
        this.func_73732_a(this.field_73886_k, this.field_74000_b, this.field_73880_f / 2, 110, 16777215);
        super.func_73863_a(p_73863_1_, p_73863_2_, p_73863_3_);
    }

    @Override
    protected void func_73869_a(char p_73869_1_, int p_73869_2_) {
    }
}
