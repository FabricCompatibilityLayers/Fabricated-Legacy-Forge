package cpw.mods.fml.relauncher;

import fr.catcore.wfvaio.FabricVariants;
import fr.catcore.wfvaio.WhichFabricVariantAmIOn;

public class CoreFMLLibraries implements ILibrarySet
{
    private static final boolean runningLegacyFabric = WhichFabricVariantAmIOn.getVariant() == FabricVariants.LEGACY_FABRIC_V1;
    private static String[] libraries = { "argo-2.25.jar","guava-12.0.1.jar","asm-all-4.0.jar" };
    private static String[] checksums = { "bb672829fde76cb163004752b86b0484bd0a7f4b", "b8e78b9af7bf45900e14c6f958486b6ca682195f", "98308890597acb64047f7e896638e0d98753ae82" };

    @Override
    public String[] getLibraries()
    {
        if (runningLegacyFabric) {
            return new String[] {
                    "guava-12.0.1.jar"
            };
        }

        return new String[0];
    }

    @Override
    public String[] getHashes()
    {
        if (runningLegacyFabric) {
            return new String[] {
                    "b8e78b9af7bf45900e14c6f958486b6ca682195f"
            };
        }

        return new String[0];
    }

    @Override
    public String getRootURL()
    {
//        return "http://files.minecraftforge.net/fmllibs/%s";
        return "https://repo.maven.apache.org/maven2/com/google/guava/guava/12.0.1/%s";
    }

}
