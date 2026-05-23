package cpw.mods.fml.relauncher;

import fr.catcore.wfvaio.FabricVariants;
import fr.catcore.wfvaio.WhichFabricVariantAmIOn;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;

import java.util.ArrayList;
import java.util.List;

public class CoreFMLLibraries implements ILibrarySet
{
    private static final boolean runningLegacyFabric = WhichFabricVariantAmIOn.getVariant() == FabricVariants.LEGACY_FABRIC_V1;
    private static String[] libraries = { "argo-2.25.jar","guava-12.0.1.jar","asm-all-4.0.jar" };
    private static String[] checksums = { "bb672829fde76cb163004752b86b0484bd0a7f4b", "b8e78b9af7bf45900e14c6f958486b6ca682195f", "98308890597acb64047f7e896638e0d98753ae82" };

    @Override
    public String[] getLibraries()
    {
        List<String> libs = new ArrayList<>();

        if (runningLegacyFabric) {
            libs.add("guava-12.0.1.jar");
        }

        return libs.toArray(new String[0]);
    }

    @Override
    public String[] getHashes()
    {
        List<String> hashes = new ArrayList<>();

        if (runningLegacyFabric) {
            hashes.add("b8e78b9af7bf45900e14c6f958486b6ca682195f");
        }

        return hashes.toArray(new String[0]);
    }

    @Override
    public String getRootURL()
    {
//        return "http://files.minecraftforge.net/fmllibs/%s";
        return "https://maven.wagyourtail.xyz/releases/fmllibs/%s";
    }

}
