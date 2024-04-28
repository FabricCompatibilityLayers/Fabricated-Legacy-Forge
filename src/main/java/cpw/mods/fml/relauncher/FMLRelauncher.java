/*
 * The FML Forge Mod Loader suite.
 * Copyright (C) 2012 cpw
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this library; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 */
package cpw.mods.fml.relauncher;

import fr.catcore.fabricatedforge.mixininterface.IMinecraftApplet;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.impl.game.minecraft.applet.AppletLauncher;

import javax.swing.*;
import java.applet.Applet;
import java.io.File;
import java.lang.reflect.Method;

public class FMLRelauncher {
    private static FMLRelauncher INSTANCE;
    public static String logFileNamePattern;
    private static String side;
    private RelaunchClassLoader classLoader;
    private Object newApplet;
    private Class<? super Object> appletClass;
    JDialog popupWindow;

    public static void handleClientRelaunch(ArgsWrapper wrap) {
        logFileNamePattern = "ForgeModLoader-client-%g.log";
        side = "CLIENT";
        instance().relaunchClient(wrap);
    }

    public static void handleServerRelaunch(ArgsWrapper wrap) {
        logFileNamePattern = "ForgeModLoader-server-%g.log";
        side = "SERVER";
        instance().relaunchServer(wrap);
    }

    static FMLRelauncher instance() {
        if (INSTANCE == null) {
            INSTANCE = new FMLRelauncher();
        }

        return INSTANCE;
    }

    private FMLRelauncher() {
//        URLClassLoader ucl = (URLClassLoader) this.getClass().getClassLoader();
//        this.classLoader = new RelaunchClassLoader(ucl.getURLs());
//        this.classLoader = FabricLauncherBase.getLauncher().getTargetClassLoader();
        this.classLoader = new RelaunchClassLoader();
    }

    private void showWindow(boolean showIt) {
        if (RelaunchLibraryManager.downloadMonitor == null) {
            try {
                if (showIt) {
                    RelaunchLibraryManager.downloadMonitor = new Downloader();
                    this.popupWindow = (JDialog)RelaunchLibraryManager.downloadMonitor.makeDialog();
                } else {
                    RelaunchLibraryManager.downloadMonitor = new DummyDownloader();
                }
            } catch (Throwable var3) {
                if (RelaunchLibraryManager.downloadMonitor == null) {
                    RelaunchLibraryManager.downloadMonitor = new DummyDownloader();
                    var3.printStackTrace();
                } else {
                    RelaunchLibraryManager.downloadMonitor.makeHeadless();
                }

                this.popupWindow = null;
            }
        }
    }

    private void relaunchClient(ArgsWrapper wrap) {
        this.showWindow(true);

        Class<? super Object> client;
        try {
            File minecraftHome = this.computeExistingClientHome();
            this.setupHome(minecraftHome);
            client = this.setupNewClientHome(minecraftHome);
        } finally {
            if (this.popupWindow != null) {
                this.popupWindow.setVisible(false);
                this.popupWindow.dispose();
            }
        }

        if (RelaunchLibraryManager.downloadMonitor.shouldStopIt()) {
            System.exit(1);
        }

        try {
            Method clientMethod = ReflectionHelper.findMethod(client, (Object) null, new String[]{"fmlReentry"}, ArgsWrapper.class);
            clientMethod.setAccessible(true);
            clientMethod.invoke((Object) null, wrap);
        } catch (Exception var7) {
            var7.printStackTrace();
        }
    }

    private Class<? super Object> setupNewClientHome(File minecraftHome) {
        Class<? super Object> client = ReflectionHelper.getClass(this.classLoader, new String[]{"net.minecraft.client.Minecraft"});
        ReflectionHelper.setPrivateValue(client, null, minecraftHome, new String[]{"minecraftDir", "am"});
        return client;
    }

    private void relaunchServer(ArgsWrapper wrap) {
        this.showWindow(false);
        File minecraftHome = new File(".");
        this.setupHome(minecraftHome);
        Class<? super Object> server = ReflectionHelper.getClass(this.classLoader, "net.minecraft.server.MinecraftServer");

        try {
            Method method = ReflectionHelper.findMethod(server, (Object) null, new String[]{"fmlReentry"}, ArgsWrapper.class);
            method.setAccessible(true);
            method.invoke((Object) null, wrap);
        } catch (Exception var5) {
            var5.printStackTrace();
        }
    }

    private void setupHome(File minecraftHome) {
        FMLInjectionData.build(minecraftHome, this.classLoader);
        FMLRelaunchLog.minecraftHome = minecraftHome;
        FMLRelaunchLog.info(
                "Forge Mod Loader version %s.%s.%s.%s for Minecraft client:%s, server:%s loading",
                FMLInjectionData.major,
                FMLInjectionData.minor,
                FMLInjectionData.rev,
                FMLInjectionData.build,
                FMLInjectionData.mccversion,
                FMLInjectionData.mcsversion);

        try {
            RelaunchLibraryManager.handleLaunch(minecraftHome, this.classLoader);
        } catch (Throwable var5) {
            if (this.popupWindow != null) {
                try {
                    String logFile = new File(minecraftHome, "ForgeModLoader-client-0.log").getCanonicalPath();
                    JOptionPane.showMessageDialog(
                            this.popupWindow,
                            String.format(
                                    "<html><div align=\"center\"><font size=\"+1\">There was a fatal error starting up minecraft and FML</font></div><br/>Minecraft cannot launch in it's current configuration<br/>Please consult the file <i><a href=\"file:///%s\">%s</a></i> for further information</html>",
                                    logFile,
                                    logFile
                            ),
                            "Fatal FML error",
                            0
                    );
                } catch (Exception var4) {
                }
            }

            throw new RuntimeException(var5);
        }
    }

    private File computeExistingClientHome() {
        return FabricLoader.getInstance().getGameDir().toFile();
    }

    public static void appletEntry(Applet minecraftApplet) {
        side = "CLIENT";
        logFileNamePattern = "ForgeModLoader-client-%g.log";
        instance().relaunchApplet(minecraftApplet);
    }

    private void relaunchApplet(Applet minecraftApplet) {
        this.showWindow(true);
        this.appletClass = ReflectionHelper.getClass(this.classLoader, "net.minecraft.client.MinecraftApplet");
        if (((IMinecraftApplet)minecraftApplet).isRelaunched()
                /*minecraftApplet.getClass().getClassLoader() == this.classLoader*/
        ) {
            if (this.popupWindow != null) {
                this.popupWindow.setVisible(false);
                this.popupWindow.dispose();
            }

            try {
                this.newApplet = minecraftApplet;
                Method method = ReflectionHelper.findMethod(this.appletClass, this.newApplet, new String[]{"fmlInitReentry"});
                method.setAccessible(true);
                method.invoke(this.newApplet);
            } catch (Exception var11) {
                System.out.println("FMLRelauncher.relaunchApplet");
                var11.printStackTrace();
                throw new RuntimeException(var11);
            }
        } else {
            File mcDir = this.computeExistingClientHome();
            this.setupHome(mcDir);
            this.setupNewClientHome(mcDir);
            Class<? super Object> parentAppletClass = ReflectionHelper.getClass(this.getClass().getClassLoader(), "java.applet.Applet");

            try {
                this.newApplet = this.classLoader.loadClass(this.appletClass.getName())
                        .getDeclaredConstructor()
                        .newInstance();
                ((IMinecraftApplet)this.newApplet).setRelaunched(true);
                AppletLauncher appletContainer = (AppletLauncher) minecraftApplet.getParent();
                String launcherClassName = System.getProperty("minecraft.applet.WrapperClass", "net.minecraft.Launcher");
                Class<? super Object> launcherClass = ReflectionHelper.getClass(this.getClass().getClassLoader(), launcherClassName);
                if (!launcherClass.isInstance(appletContainer)) {
                    FMLRelaunchLog.severe("Found unknown applet parent %s, unable to inject!\n", appletContainer.getClass().getName());
                    throw new RuntimeException();
                }

                appletContainer.removeAll();
                appletContainer.replace((Applet) this.newApplet);
            } catch (Exception var12) {
                throw new RuntimeException(var12);
            } finally {
                if (this.popupWindow != null) {
                    this.popupWindow.setVisible(false);
                    this.popupWindow.dispose();
                }
            }
        }
    }

    public static void appletStart(Applet applet) {
        instance().startApplet(applet);
    }

    private void startApplet(Applet applet) {
        if (applet.getClass().getClassLoader() == this.classLoader) {
            if (this.popupWindow != null) {
                this.popupWindow.setVisible(false);
                this.popupWindow.dispose();
            }

            if (RelaunchLibraryManager.downloadMonitor.shouldStopIt()) {
                System.exit(1);
            }

            try {
                ReflectionHelper.findMethod(this.appletClass, this.newApplet, new String[]{"fmlStartReentry"}, new Class[0]).invoke(this.newApplet);
            } catch (Exception var3) {
                System.out.println("FMLRelauncher.startApplet");
                var3.printStackTrace();
                throw new RuntimeException(var3);
            }
        }
    }

    public static String side() {
        return side;
    }
}
