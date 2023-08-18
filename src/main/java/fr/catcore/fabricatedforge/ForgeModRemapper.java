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

        infos.registerMethodFieldIns(
                new VisitorInfos.MethodNamed("net/minecraft/class_9", "allowedBiomes"),
                new VisitorInfos.MethodNamed("fr/catcore/fabricatedforge/forged/ReflectionUtils", "StrongholdStructure_allowedBiomes")
        );
        infos.registerMethodFieldIns(
                new VisitorInfos.MethodNamed("net/minecraft/class_1175", "allowedBiomes"),
                new VisitorInfos.MethodNamed("fr/catcore/fabricatedforge/forged/ReflectionUtils", "LayeredBiomeSource_allowedBiomes")
        );

        infos.registerMethodFieldIns(
                new VisitorInfos.MethodNamed("net/minecraft/class_570", "NAME_TAG_RANGE"),
                new VisitorInfos.MethodNamed("fr/catcore/fabricatedforge/forged/ReflectionUtils", "NAME_TAG_RANGE")
        );
        infos.registerMethodFieldIns(
                new VisitorInfos.MethodNamed("net/minecraft/class_570", "NAME_TAG_RANGE_SNEAK"),
                new VisitorInfos.MethodNamed("fr/catcore/fabricatedforge/forged/ReflectionUtils", "NAME_TAG_RANGE_SNEAK")
        );


        infos.registerMethodFieldIns(
                new VisitorInfos.MethodNamed("net/minecraft/class_988", "PERSISTED_NBT_TAG"),
                new VisitorInfos.MethodNamed("fr/catcore/fabricatedforge/forged/ReflectionUtils", "PERSISTED_NBT_TAG")
        );

        // Mystcraft
        infos.registerMethodFieldIns(
                new VisitorInfos.MethodNamed("xcompwiz/mystcraft/Mystcraft", "registeredDims"),
                new VisitorInfos.MethodNamed("fr/catcore/fabricatedforge/compat/MystcraftCompat", "registeredDims")
        );

//        // CodeChickenCore
//        infos.registerMethodMethodIns(
//                new VisitorInfos.MethodNamed("codechicken/core/asm/ClassOverrider", "overrideBytes"),
//                new VisitorInfos.MethodNamed("fr/catcore/fabricatedforge/compat/CodeChickenCoreCompat", "overrideBytes")
//        );
//
//        // NEI
//        infos.registerMethodLdcIns(
//                new VisitorInfos.MethodValue("codechicken/nei/asm/NEITransformer", "aqh"),
//                new VisitorInfos.MethodValue("codechicken/nei/asm/NEITransformer", "net.minecraft.class_409")
//        );
//        infos.registerMethodLdcIns(
//                new VisitorInfos.MethodValue("codechicken/nei/asm/NEITransformer", "apn"),
//                new VisitorInfos.MethodValue("codechicken/nei/asm/NEITransformer", "net.minecraft.class_388")
//        );
//        infos.registerMethodLdcIns(
//                new VisitorInfos.MethodValue("codechicken/nei/asm/NEITransformer", "agu"),
//                new VisitorInfos.MethodValue("codechicken/nei/asm/NEITransformer", "net.minecraft.class_159")
//        );
//        infos.registerMethodLdcIns(
//                new VisitorInfos.MethodValue("codechicken/nei/asm/NEITransformer", "aig"),
//                new VisitorInfos.MethodValue("codechicken/nei/asm/NEITransformer", "net.minecraft.class_197")
//        );
//        infos.registerMethodLdcIns(
//                new VisitorInfos.MethodValue("codechicken/nei/asm/NEITransformer", "c"),
//                new VisitorInfos.MethodValue("codechicken/nei/asm/NEITransformer", "method_1033")
//        );
//        infos.registerMethodLdcIns(
//                new VisitorInfos.MethodValue("codechicken/nei/asm/NEITransformer", "a"),
//                new VisitorInfos.MethodValue("codechicken/nei/asm/NEITransformer", "method_419")
//        );
//        infos.registerMethodLdcIns(
//                new VisitorInfos.MethodValue("codechicken/nei/asm/NEITransformer", "(Lup;IIILjw;)V"),
//                new VisitorInfos.MethodValue("codechicken/nei/asm/NEITransformer", "(Lnet/minecraft/class_1150;IIILnet/minecraft/class_871;)V")
//        );
//        infos.registerMethodMethodIns(
//                new VisitorInfos.MethodNamed("codechicken/nei/TMIUninstaller", "getJarFile"),
//                new VisitorInfos.MethodNamed("fr/catcore/fabricatedforge/compat/CodeChickenCoreCompat", "getJarFile")
//        );
//        infos.registerMethodLdcIns(
//                new VisitorInfos.MethodValue("codechicken/nei/recipe/FurnaceRecipeHandler", "getItemBurnTime"),
//                new VisitorInfos.MethodValue("codechicken/nei/recipe/FurnaceRecipeHandler", "method_519")
//        );
    }

    @Override
    public Optional<String> getDefaultPackage() {
        return Optional.of("net/minecraft/");
    }

    @Override
    public void afterRemap() {

    }

    static {
        FORGE_EXCLUDED.put("a")
                .put("aam")
                .put("aaq")
                .put("aax")
                .put("abb")
                .put("abc")
                .put("abf")
                .put("abk")
                .put("abo")
                .put("abt")
                .put("abw")
                .put("abx")
                .put("aby")
                .put("abz")
                .put("acf")
                .put("acg")
                .put("ach")
                .put("aci")
                .put("acq")
                .put("adt")
                .put("adu")
                .put("ady")
                .put("aee")
                .put("aei")
                .put("aen")
                .put("afe")
                .put("afo")
                .put("afp")
                .put("afs")
                .put("agp")
                .put("agw")
                .put("ahn")
                .put("ahr")
                .put("ahv")
                .put("ahx")
                .put("aje")
                .put("ajf")
                .put("ajg")
                .put("ajk")
                .put("ajn")
                .put("ajq")
                .put("ajx")
                .put("akb")
                .put("akc")
                .put("akf")
                .put("akj")
                .put("aks")
                .put("akt")
                .put("akv")
                .put("akx")
                .put("ale")
                .put("alg")
                .put("alh")
                .put("alr")
                .put("als")
                .put("alv")
                .put("aly")
                .put("ama")
                .put("amc")
                .put("ami")
                .put("amm")
                .put("amq")
                .put("amq$1")
                .put("amw")
                .put("amx")
                .put("amz")
                .put("ana")
                .put("anb")
                .put("and")
                .put("ank")
                .put("anl")
                .put("anr")
                .put("any")
                .put("aoa")
                .put("aoh")
                .put("arn")
                .put("ast")
                .put("atg")
                .put("atj")
                .put("atp")
                .put("atr")
                .put("aun")
                .put("_aux")
                .put("avf")
                .put("avs")
                .put("axa")
                .put("axd")
                .put("axq")
                .put("aya")
                .put("ayf")
                .put("ayh")
                .put("ayk")
                .put("ayo")
                .put("ayp")
                .put("azi")
                .put("azr")
                .put("bac")
                .put("baj")
                .put("ban")
                .put("bau")
                .put("bav")
                .put("baz")
                .put("bba")
                .put("bbb")
                .put("bbu")
                .put("bcc")
                .put("bce")
                .put("bco")
                .put("bct")
                .put("bde")
                .put("bdf")
                .put("bdh")
                .put("bdi")
                .put("bdj")
                .put("bdk")
                .put("bdl")
                .put("bdm")
                .put("bds")
                .put("bdz")
                .put("bec")
                .put("bev")
                .put("bex")
                .put("bey")
                .put("bez")
                .put("bn")
                .put("ca")
                .put("cf")
                .put("cg")
                .put("dr")
                .put("dt")
                .put("dw")
                .put("ef")
                .put("eg")
                .put("fq")
                .put("gm")
                .put("ho")
                .put("hu")
                .put("i")
                .put("ia")
                .put("ii")
                .put("il")
                .put("im")
                .put("in")
                .put("iq")
                .put("ir")
                .put("is")
                .put("it")
                .put("iv")
                .put("iw")
                .put("jq")
                .put("jt")
                .put("kw")
                .put("lm")
                .put("lq")
                .put("lv")
                .put("md")
                .put("pb")
                .put("pc")
                .put("pe")
                .put("pl")
                .put("pp")
                .put("px")
                .put("py")
                .put("qo")
                .put("qu")
                .put("qx")
                .put("sd")
                .put("se")
                .put("sq")
                .put("sr")
                .put("sv")
                .put("sv$1")
                .put("tb")
                .put("td")
                .put("tj")
                .put("tw")
                .put("ty")
                .put("ua")
                .put("uo")
                .put("up")
                .put("uq")
                .put("uq$1")
                .put("ut")
                .put("uu")
                .put("vg")
                .put("vh")
                .put("vi")
                .put("vq")
                .put("wh")
                .put("wj")
                .put("wn")
                .put("x")
                .put("xc")
                .put("xe")
                .put("xx")
                .put("yc")
                .put("yn")
                .put("yp")
                .put("ys")
                .put("yy")
                .put("zc")
                .put("zd")
                .put("zz");
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
