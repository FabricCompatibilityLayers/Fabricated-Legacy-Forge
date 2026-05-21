package net.minecraft.src;

import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class ModelRenderer {
    public float textureWidth;
    public float textureHeight;
    private int textureOffsetX;
    private int textureOffsetY;
    public float rotationPointX;
    public float rotationPointY;
    public float rotationPointZ;
    public float rotateAngleX;
    public float rotateAngleY;
    public float rotateAngleZ;
    private boolean compiled;
    private int displayList;
    public boolean mirror;
    public boolean showModel;
    public boolean isHidden;
    public List cubeList;
    public List childModels;
    public final String boxName;
    private ModelBase baseModel;

    public ModelRenderer(ModelBase par1ModelBase, String par2Str) {
        this.textureWidth = 64.0F;
        this.textureHeight = 32.0F;
        this.compiled = false;
        this.displayList = 0;
        this.mirror = false;
        this.showModel = true;
        this.isHidden = false;
        this.cubeList = new ArrayList();
        this.baseModel = par1ModelBase;
        par1ModelBase.boxList.add(this);
        this.boxName = par2Str;
        this.setTextureSize(par1ModelBase.textureWidth, par1ModelBase.textureHeight);
    }

    public ModelRenderer(ModelBase par1ModelBase) {
        this(par1ModelBase, (String)null);
    }

    public ModelRenderer(ModelBase par1ModelBase, int par2, int par3) {
        this(par1ModelBase);
        this.setTextureOffset(par2, par3);
    }

    public void addChild(ModelRenderer par1ModelRenderer) {
        if (this.childModels == null) {
            this.childModels = new ArrayList();
        }

        this.childModels.add(par1ModelRenderer);
    }

    public ModelRenderer setTextureOffset(int par1, int par2) {
        this.textureOffsetX = par1;
        this.textureOffsetY = par2;
        return this;
    }

    public ModelRenderer addBox(String par1Str, float par2, float par3, float par4, int par5, int par6, int par7) {
        par1Str = this.boxName + "." + par1Str;
        TextureOffset var8 = this.baseModel.getTextureOffset(par1Str);
        this.setTextureOffset(var8.textureOffsetX, var8.textureOffsetY);
        this.cubeList.add((new ModelBox(this, this.textureOffsetX, this.textureOffsetY, par2, par3, par4, par5, par6, par7, 0.0F)).func_78244_a(par1Str));
        return this;
    }

    public ModelRenderer addBox(float par1, float par2, float par3, int par4, int par5, int par6) {
        this.cubeList.add(new ModelBox(this, this.textureOffsetX, this.textureOffsetY, par1, par2, par3, par4, par5, par6, 0.0F));
        return this;
    }

    public void addBox(float par1, float par2, float par3, int par4, int par5, int par6, float par7) {
        this.cubeList.add(new ModelBox(this, this.textureOffsetX, this.textureOffsetY, par1, par2, par3, par4, par5, par6, par7));
    }

    public void setRotationPoint(float par1, float par2, float par3) {
        this.rotationPointX = par1;
        this.rotationPointY = par2;
        this.rotationPointZ = par3;
    }

    public ModelRenderer setTextureSize(int par1, int par2) {
        this.textureWidth = (float)par1;
        this.textureHeight = (float)par2;
        return this;
    }
}
