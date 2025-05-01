package cpw.mods.fml.relauncher;

import io.github.fabriccompatibilitylayers.fabricatedfml.forged.ClassLoaderUtils;
import io.github.fabriccompatibiltylayers.modremappingapi.api.v1.ClassTransformer;
import net.legacyfabric.fabric.api.logger.v1.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface IClassTransformer extends ClassTransformer
{
    public byte[] transform(String name, byte[] bytes);

    // Fabricated FML
    static final List<String> CLASS_NAMES = new ArrayList<>();
    static final Logger LOGGER = Logger.get("Fabricated-FML", "ClassTransformers");

    Map<IClassTransformer, List<String>> transformed = new HashMap<>();

    @Override
    default byte[] transformClass(String name, String transformedName, byte[] original) {
        if (original == null) return null;
        byte[] transformed = this.transform(name, original);

        if (original != transformed && !this.toString().startsWith("cpw.mods.fml.common.asm.transformers.SideTransformer")) {
            LOGGER.debug(name + " transformed by " + this);
        }

        return transformed;
    }

    @Override
    default boolean handlesClass(String s, String s1) {
        String className = this.toString();

        if (!CLASS_NAMES.contains(className)) {
            CLASS_NAMES.add(className);
            transformed.put(this, new ArrayList<>());
        }

        for (String toExclude : ClassLoaderUtils.TRANSFORMER_EXCLUSIONS) {
            if (s.startsWith(toExclude)) {
                return false;
            }
        }

        if (transformed.get(this).contains(s)) {
            LOGGER.warn("Detected transformation loop for class " + s + " in ClassTransformer " + className);
            return false;
        }

        transformed.get(this).add(s);

        return true;
    }
}
