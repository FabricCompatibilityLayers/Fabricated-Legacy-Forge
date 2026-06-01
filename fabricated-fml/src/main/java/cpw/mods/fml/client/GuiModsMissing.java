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

import cpw.mods.fml.common.MissingModsException;
import cpw.mods.fml.common.versioning.ArtifactVersion;
import io.github.fabriccompatibilitylayers.fabricatedfml.forged.ForgedGuiErrorScreen;
import net.minecraft.src.GuiErrorScreen;

public class GuiModsMissing extends ForgedGuiErrorScreen
{

    private MissingModsException modsMissing;

    public GuiModsMissing(MissingModsException modsMissing)
    {
        this.modsMissing = modsMissing;
    }

    @Override
    public void func_73866_w_()
    {
        super.func_73866_w_();
    }
    @Override
    public void func_73863_a(int p_73863_1_, int p_73863_2_, float p_73863_3_)
    {
        this.func_73873_v_();
        int offset = Math.max(85 - modsMissing.missingMods.size() * 10, 10);
        this.func_73732_a(this.field_73886_k, "Forge Mod Loader has found a problem with your minecraft installation", this.field_73880_f / 2, offset, 0xFFFFFF);
        offset+=10;
        this.func_73732_a(this.field_73886_k, "The mods and versions listed below could not be found", this.field_73880_f / 2, offset, 0xFFFFFF);
        offset+=5;
        for (ArtifactVersion v : modsMissing.missingMods)
        {
            offset+=10;
            this.func_73732_a(this.field_73886_k, String.format("%s : %s", v.getLabel(), v.getRangeString()), this.field_73880_f / 2, offset, 0xEEEEEE);
        }
        offset+=20;
        this.func_73732_a(this.field_73886_k, "The file 'ForgeModLoader-client-0.log' contains more information", this.field_73880_f / 2, offset, 0xFFFFFF);
    }
}
