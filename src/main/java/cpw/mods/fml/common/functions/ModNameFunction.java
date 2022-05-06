package cpw.mods.fml.common.functions;

import com.google.common.base.Function;
import cpw.mods.fml.common.ModContainer;

public class ModNameFunction implements Function<ModContainer, String> {
    public ModNameFunction() {
    }

    public String apply(ModContainer input) {
        return input.getName();
    }
}
