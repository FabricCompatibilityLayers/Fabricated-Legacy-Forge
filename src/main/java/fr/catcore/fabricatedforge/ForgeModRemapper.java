package fr.catcore.fabricatedforge;

import fr.catcore.modremapperapi.api.ModRemapper;
import fr.catcore.modremapperapi.api.RemapLibrary;
import fr.catcore.modremapperapi.remapping.RemapUtil;
import fr.catcore.modremapperapi.remapping.VisitorInfos;
import net.fabricmc.loader.api.FabricLoader;
import org.spongepowered.asm.mixin.Mixins;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ForgeModRemapper implements ModRemapper {

    private static final BArrayList FORGE_EXCLUDED = new BArrayList();
    private static final Map<String, fr.catcore.modremapperapi.utils.BArrayList<String>> EXCLUDED = new HashMap<>();
    
    @Override
    public String[] getJarFolders() {
        return new String[] {"coremods"};
    }

    @Override
    public RemapLibrary[] getRemapLibraries() {
        return new RemapLibrary[] {
                new RemapLibrary(
                        Constants.FORGE_URL,
                        FORGE_EXCLUDED,
                        "forge.zip"
                )
        };
    }

    @Override
    public Map<String, List<String>> getExclusions() {
        return new HashMap<>(EXCLUDED);
    }

    @Override
    public void getMappingList(RemapUtil.MappingList mappings) {
        // ModLoader mappings
        mappings.add("BaseMod", "net/minecraft/BaseMod");
        mappings.add("EntityRendererProxy", "net/minecraft/EntityRendererProxy");
        mappings.add("FMLRendererAccessLibrary", "net/minecraft/FMLRendererAccessLibrary");
        mappings.add("MLProp", "net/minecraft/MLProp");
        mappings.add("ModLoader", "net/minecraft/ModLoader");
        mappings.add("ModTextureAnimation", "net/minecraft/ModTextureAnimation");
        mappings.add("ModTextureStatic", "net/minecraft/ModTextureStatic");
        mappings.add("TradeEntry", "net/minecraft/TradeEntry");
    }

    private String getOfficialClassName(String className) {
        className = className.replace("/", ".");
        className = FabricLoader.getInstance().getMappingResolver().unmapClassName("official", className);
        return className.replace(".", "/");
    }

    @Override
    public void registerVisitors(VisitorInfos infos) {
        infos.registerMethodMethodIns(
                new VisitorInfos.MethodNamed("net/minecraft/class_197", "setBurnProperties"),
                new VisitorInfos.MethodNamed("fr/catcore/fabricatedforge/forged/ReflectionUtils", "Block_setBurnProperties")
        );

        // Mystcraft
        infos.registerMethodFieldIns(
                new VisitorInfos.MethodNamed("xcompwiz/mystcraft/Mystcraft", "registeredDims"),
                new VisitorInfos.MethodNamed("fr/catcore/fabricatedforge/compat/MystcraftCompat", "registeredDims")
        );

        // CodeChickenCore
        infos.registerMethodMethodIns(
                new VisitorInfos.MethodNamed("codechicken/core/asm/ClassOverrider", "overrideBytes"),
                new VisitorInfos.MethodNamed("fr/catcore/fabricatedforge/compat/CodeChickenCoreCompat", "overrideBytes")
        );

        // NEI
        infos.registerMethodLdcIns(
                new VisitorInfos.MethodValue("codechicken/nei/asm/NEITransformer", "aqh"),
                new VisitorInfos.MethodValue("codechicken/nei/asm/NEITransformer", "net.minecraft.class_409")
        );
        infos.registerMethodLdcIns(
                new VisitorInfos.MethodValue("codechicken/nei/asm/NEITransformer", "apn"),
                new VisitorInfos.MethodValue("codechicken/nei/asm/NEITransformer", "net.minecraft.class_388")
        );
        infos.registerMethodLdcIns(
                new VisitorInfos.MethodValue("codechicken/nei/asm/NEITransformer", "agu"),
                new VisitorInfos.MethodValue("codechicken/nei/asm/NEITransformer", "net.minecraft.class_159")
        );
        infos.registerMethodLdcIns(
                new VisitorInfos.MethodValue("codechicken/nei/asm/NEITransformer", "aig"),
                new VisitorInfos.MethodValue("codechicken/nei/asm/NEITransformer", "net.minecraft.class_197")
        );
        infos.registerMethodLdcIns(
                new VisitorInfos.MethodValue("codechicken/nei/asm/NEITransformer", "c"),
                new VisitorInfos.MethodValue("codechicken/nei/asm/NEITransformer", "method_1033")
        );
        infos.registerMethodLdcIns(
                new VisitorInfos.MethodValue("codechicken/nei/asm/NEITransformer", "a"),
                new VisitorInfos.MethodValue("codechicken/nei/asm/NEITransformer", "method_419")
        );
        infos.registerMethodLdcIns(
                new VisitorInfos.MethodValue("codechicken/nei/asm/NEITransformer", "(Lup;IIILjw;)V"),
                new VisitorInfos.MethodValue("codechicken/nei/asm/NEITransformer", "(Lnet/minecraft/class_1150;IIILnet/minecraft/class_871;)V")
        );
        infos.registerMethodMethodIns(
                new VisitorInfos.MethodNamed("codechicken/nei/TMIUninstaller", "getJarFile"),
                new VisitorInfos.MethodNamed("fr/catcore/fabricatedforge/compat/CodeChickenCoreCompat", "getJarFile")
        );
        infos.registerMethodLdcIns(
                new VisitorInfos.MethodValue("codechicken/nei/recipe/FurnaceRecipeHandler", "getItemBurnTime"),
                new VisitorInfos.MethodValue("codechicken/nei/recipe/FurnaceRecipeHandler", "method_519")
        );
    }

    @Override
    public Optional<String> getDefaultPackage() {
        return Optional.of("net/minecraft/");
    }

    @Override
    public void afterRemap() {
        Mixins.addConfiguration("fabricated-forge.mods.mixins.json");
    }

    static {
        FORGE_EXCLUDED.put("a")
                .put("aag")
                .put("aal")
                .put("aap")
                .put("aau")
                .put("aax")
                .put("aay")
                .put("aaz")
                .put("aba")
                .put("abg")
                .put("abh")
                .put("abi")
                .put("abj")
                .put("abr")
                .put("acu")
                .put("acv")
                .put("adf")
                .put("adj")
                .put("ado")
                .put("aec")
                .put("aem")
                .put("afn")
                .put("agl")
                .put("agp")
                .put("agt")
                .put("agv")
                .put("aht")
                .put("ahu")
                .put("ahv")
                .put("ahz")
                .put("aif")
                .put("ail")
                .put("aim")
                .put("aiq")
                .put("air")
                .put("aiu")
                .put("aiy")
                .put("ajh")
                .put("aji")
                .put("ajk")
                .put("ajt")
                .put("ajv")
                .put("ajw")
                .put("akg")
                .put("akh")
                .put("akk")
                .put("akn")
                .put("akx")
                .put("alb")
                .put("alf")
                .put("alf$1")
                .put("all")
                .put("alm")
                .put("alo")
                .put("alp")
                .put("alq")
                .put("als")
                .put("ama")
                .put("amg")
                .put("amm")
                .put("amn")
                .put("amu")
                .put("aqa")
                .put("ard")
                .put("arr")
                .put("aru")
                .put("asa")
                .put("asc")
                .put("asy")
                .put("ati")
                .put("aud")
                .put("avl")
                .put("avo")
                .put("awa")
                .put("awk")
                .put("awp")
                .put("awr")
                .put("awy")
                .put("awz")
                .put("axx")
                .put("ayi")
                .put("ayp")
                .put("ayt")
                .put("ayx")
                .put("ayy")
                .put("azb")
                .put("azc")
                .put("azd")
                .put("azw")
                .put("bae")
                .put("bag")
                .put("baq")
                .put("bav")
                .put("bbg")
                .put("bbh")
                .put("bbj")
                .put("bbk")
                .put("bbl")
                .put("bbm")
                .put("bbn")
                .put("bbo")
                .put("bbu")
                .put("bcb")
                .put("bce")
                .put("bcx")
                .put("bcy")
                .put("bcz")
                .put("be")
                .put("br")
                .put("bw")
                .put("bx")
                .put("dj")
                .put("dl")
                .put("do")
                .put("dx")
                .put("dy")
                .put("fi")
                .put("ge")
                .put("hg")
                .put("hl")
                .put("hr")
                .put("hz")
                .put("ic")
                .put("id")
                .put("ie")
                .put("ih")
                .put("ii")
                .put("ij")
                .put("ik")
                .put("im")
                .put("in")
                .put("jh")
                .put("kh")
                .put("kx")
                .put("lb")
                .put("ln")
                .put("mt")
                .put("ol")
                .put("om")
                .put("oo")
                .put("ou")
                .put("oy")
                .put("p")
                .put("pg")
                .put("ph")
                .put("px")
                .put("qb")
                .put("qg")
                .put("rl")
                .put("rm")
                .put("ry")
                .put("si")
                .put("sk")
                .put("sq")
                .put("td")
                .put("tf")
                .put("th")
                .put("ts")
                .put("tt")
                .put("tx")
                .put("ty")
                .put("ul")
                .put("um")
                .put("uu")
                .put("vm")
                .put("vq")
                .put("wf")
                .put("wh")
                .put("wz")
                .put("xe")
                .put("xp")
                .put("xr")
                .put("xt")
                .put("za")
                .put("zn")
                .put("zr");
    }

    public static class Entry {
        public final String name;
        public final String descriptor;
        public final String owner;

        public Entry(String name, String descriptor, String owner) {
            this.name = name;
            this.descriptor = descriptor;
            this.owner = owner;
        }
    }

    public static class BArrayList extends fr.catcore.modremapperapi.utils.BArrayList<String> {

        @Override
        public BArrayList put(String entry) {
            return (BArrayList) super.put(entry + ".class");
        }
    }
}
