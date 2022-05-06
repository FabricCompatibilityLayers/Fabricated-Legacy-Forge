package cpw.mods.fml.common.asm;

import cpw.mods.fml.relauncher.IFMLCallHook;
import cpw.mods.fml.relauncher.RelaunchClassLoader;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class FMLSanityChecker implements IFMLCallHook {
    private RelaunchClassLoader cl;

    public FMLSanityChecker() {
    }

    public Void call() throws Exception {
        byte[] mlClass = this.cl.getClassBytes("ModLoader");
        if (mlClass == null) {
            return null;
        } else {
            FMLSanityChecker.MLDetectorClassVisitor mlTester = new FMLSanityChecker.MLDetectorClassVisitor();
            ClassReader cr = new ClassReader(mlClass);
            cr.accept(mlTester, 1);
            if (!mlTester.foundMarker) {
                JOptionPane.showMessageDialog((Component)null, "<html>CRITICAL ERROR<br/>ModLoader was detected in this environment<br/>ForgeModLoader cannot be installed alongside ModLoader<br/>All mods should work without ModLoader being installed<br/>Because ForgeModLoader is 100% compatible with ModLoader<br/>Re-install Minecraft Forge or Forge ModLoader into a clean<br/>jar and try again.", "ForgeModLoader critical error", 0);
                throw new RuntimeException("Invalid ModLoader class detected");
            } else {
                return null;
            }
        }
    }

    public void injectData(Map<String, Object> data) {
        this.cl = (RelaunchClassLoader)data.get("classLoader");
    }

    static class MLDetectorClassVisitor extends ClassVisitor {
        private boolean foundMarker;

        private MLDetectorClassVisitor() {
            super(262144);
            this.foundMarker = false;
        }

        public FieldVisitor visitField(int arg0, String arg1, String arg2, String arg3, Object arg4) {
            if ("fmlMarker".equals(arg1)) {
                this.foundMarker = true;
            }

            return null;
        }
    }
}
