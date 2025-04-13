package fr.catcore.fabricatedforge.compat;

import java.io.InputStream;

public class InvTweaksCompat {

    public static InputStream getLangFile(String path) {
        try {
            return Class.forName("net.minecraft.InvTweaksLocalization").getResourceAsStream("/" + path);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
