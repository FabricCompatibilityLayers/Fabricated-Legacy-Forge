/*
 * The FML Forge Mod Loader suite. Copyright (C) 2012 cpw
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
package cpw.mods.fml.client;

import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.GuiSmallButton;
import net.minecraft.src.StringTranslate;
import cpw.mods.fml.common.network.ModMissingPacket;
import cpw.mods.fml.common.versioning.ArtifactVersion;

public class GuiModsMissingForServer extends GuiScreen
{
    private ModMissingPacket modsMissing;

    public GuiModsMissingForServer(ModMissingPacket modsMissing)
    {
        this.modsMissing = modsMissing;
    }

    @Override
    public void func_73866_w_()
    {
        StringTranslate translations = StringTranslate.func_74808_a();
        this.field_73887_h.add(new GuiSmallButton(1, this.field_73880_f / 2 - 75, this.field_73881_g - 38, translations.func_74805_b("gui.done")));
    }

    @Override
    protected void func_73875_a(GuiButton p_73875_1_)
    {
        if (p_73875_1_.field_73742_g && p_73875_1_.field_73741_f == 1)
        {
            FMLClientHandler.instance().getClient().func_71373_a(null);
        }
    }
    @Override
    public void func_73863_a(int p_73863_1_, int p_73863_2_, float p_73863_3_)
    {
        this.func_73873_v_();
        int offset = Math.max(85 - modsMissing.getModList().size() * 10, 10);
        this.func_73732_a(this.field_73886_k, "Forge Mod Loader could not connect to this server", this.field_73880_f / 2, offset, 0xFFFFFF);
        offset += 10;
        this.func_73732_a(this.field_73886_k, "The mods and versions listed below could not be found", this.field_73880_f / 2, offset, 0xFFFFFF);
        offset += 10;
        this.func_73732_a(this.field_73886_k, "They are required to play on this server", this.field_73880_f / 2, offset, 0xFFFFFF);
        offset += 5;
        for (ArtifactVersion v : modsMissing.getModList())
        {
            offset += 10;
            this.func_73732_a(this.field_73886_k, String.format("%s : %s", v.getLabel(), v.getRangeString()), this.field_73880_f / 2, offset, 0xEEEEEE);
        }
        super.func_73863_a(p_73863_1_, p_73863_2_, p_73863_3_);
    }
}
