package fr.catcore.fabricatedforge.compat.mixin.nei;

import codechicken.nei.asm.NEITransformer;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import fr.catcore.fabricatedforge.Constants;
import fr.catcore.fabricatedforge.compat.BetterClassWriter;
import io.github.fabriccompatibiltylayers.modremappingapi.api.MappingUtils;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(NEITransformer.class)
public class NEITransformerMixin {
    @WrapOperation(method = "transformer004", require = 0, at = @At(value = "NEW", target = "(ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;)Lorg/objectweb/asm/tree/FieldInsnNode;"), remap = false)
    private FieldInsnNode flf$remapField(int opcode, String owner, String name, String descriptor, Operation<FieldInsnNode> original) {
        String remappedOwner = Constants.mapClass(owner);
        MappingUtils.ClassMember member = Constants.mapFieldFromRemappedClass(remappedOwner, name, descriptor);

        return original.call(opcode, remappedOwner, member.name, Constants.mapTypeDescriptor(member.desc));
    }

    @WrapOperation(method = {"transformer001", "transformer002", "transformer003"}, at = @At(value = "NEW", target = "(I)Lorg/objectweb/asm/ClassWriter;"), remap = false)
    private ClassWriter flf$fixClassWriter(int flags, Operation<ClassWriter> original) {
        return new BetterClassWriter(flags);
    }
}
