package cpw.mods.fml.client;

import net.minecraft.client.Sprite;

class OverrideInfo {
    public String texture;
    public String override;
    public int index;
    public int imageIndex;
    public Sprite textureFX;
    public boolean added;

    OverrideInfo() {
    }

    public boolean equals(Object obj) {
        try {
            OverrideInfo inf = (OverrideInfo)obj;
            return this.index == inf.index && this.imageIndex == inf.imageIndex;
        } catch (Exception var3) {
            return false;
        }
    }

    public int hashCode() {
        return this.index + this.imageIndex;
    }
}
