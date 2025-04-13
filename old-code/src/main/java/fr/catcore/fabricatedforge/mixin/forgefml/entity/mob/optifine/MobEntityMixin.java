package fr.catcore.fabricatedforge.mixin.forgefml.entity.mob.optifine;

import fr.catcore.fabricatedforge.mixininterface.IMobEntity;
import net.minecraft.entity.mob.MobEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin implements IMobEntity {

    @Shadow
    float field_3344;

    @Override
    public float getField_3344() {
        return this.field_3344;
    }

    @Override
    public void setField_3344(float field_3344) {
        this.field_3344 = field_3344;
    }
}
