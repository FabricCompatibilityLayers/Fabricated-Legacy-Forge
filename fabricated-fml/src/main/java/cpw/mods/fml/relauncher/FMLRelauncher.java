/*
 * The FML Forge Mod Loader suite. Copyright (C) 2012 cpw
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

import io.github.fabriccompatibilitylayers.fabricatedfml.extension.client.MinecraftAppletExtension;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.impl.game.minecraft.applet.AppletLauncher;

import java.applet.Applet;
import java.io.File;
import java.lang.reflect.Method;
import java.net.URLClassLoader;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

public class FMLRelauncher
{
    private static FMLRelauncher INSTANCE;
    public static String logFileNamePattern;
    private static String side;
    private RelaunchClassLoader classLoader;
    private Object newApplet;
    private Class<? super Object> appletClass;

    JDialog popupWindow;

    public static void handleClientRelaunch(ArgsWrapper wrap)
    {
        logFileNamePattern = "ForgeModLoader-client-%g.log";
        side = "CLIENT";
        instance().relaunchClient(wrap);
    }

    public static void handleServerRelaunch(ArgsWrapper wrap)
    {
        logFileNamePattern = "ForgeModLoader-server-%g.log";
        side = "SERVER";
        instance().relaunchServer(wrap);
    }

    public static void handleServerPreLaunch() {
        logFileNamePattern = "ForgeModLoader-server-%g.log";
        side = "SERVER";
        instance().preLaunchCommon(false);
    }

    static FMLRelauncher instance()
    {
        if (INSTANCE == null)
        {
            INSTANCE = new FMLRelauncher();
        }
        return INSTANCE;

    }

    private FMLRelauncher()
    {
        classLoader = new RelaunchClassLoader();
    }

    private void showWindow(boolean showIt)
    {
        if (RelaunchLibraryManager.downloadMonitor != null) { return; }
        try
        {
            if (showIt)
            {
                RelaunchLibraryManager.downloadMonitor = new Downloader();
                popupWindow = (JDialog) RelaunchLibraryManager.downloadMonitor.makeDialog();
            }
            else
            {
                RelaunchLibraryManager.downloadMonitor = new DummyDownloader();
            }
        }
        catch (Throwable e)
        {
            if (RelaunchLibraryManager.downloadMonitor == null)
            {
                RelaunchLibraryManager.downloadMonitor = new DummyDownloader();
                e.printStackTrace();
            }
            else
            {
                RelaunchLibraryManager.downloadMonitor.makeHeadless();
            }
            popupWindow = null;
        }
    }

    private void relaunchClient(ArgsWrapper wrap)
    {
        showWindow(true);
        // Now we re-inject the home into the "new" minecraft under our control
        Class<? super Object> client;
        try
        {
            File minecraftHome = computeExistingClientHome();
            setupHome(minecraftHome);

            client = setupNewClientHome(minecraftHome);
        }
        finally
        {
            if (popupWindow != null)
            {
                popupWindow.setVisible(false);
                popupWindow.dispose();
            }
        }

        if (RelaunchLibraryManager.downloadMonitor.shouldStopIt())
        {
            System.exit(1);
        }
        try
        {
            ReflectionHelper.findMethod(client, null, new String[] { "fmlReentry" }, ArgsWrapper.class).invoke(null, wrap);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            // Hmmm
        }
    }

    private Class<? super Object> setupNewClientHome(File minecraftHome)
    {
        Class<? super Object> client = ReflectionHelper.getClass(classLoader, "net.minecraft.client.Minecraft");
        ReflectionHelper.setPrivateValue(client, null, minecraftHome, "field_71463_am", "am", "minecraftDir");
        return client;
    }

    private void relaunchServer(ArgsWrapper wrap)
    {
//        showWindow(false);
//        // Now we re-inject the home into the "new" minecraft under our control
//        Class<? super Object> server;
//        File minecraftHome = new File(".");
//        setupHome(minecraftHome);

        Class<? super Object> server = ReflectionHelper.getClass(classLoader, "net.minecraft.server.MinecraftServer");
        try
        {
            ReflectionHelper.findMethod(server, null, new String[] { "fmlReentry" }, ArgsWrapper.class).invoke(null, wrap);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void setupHome(File minecraftHome)
    {
        FMLInjectionData.build(minecraftHome, classLoader);
        FMLRelaunchLog.minecraftHome = minecraftHome;
        FMLRelaunchLog.info("Forge Mod Loader version %s.%s.%s.%s for Minecraft client:%s, server:%s loading", FMLInjectionData.major, FMLInjectionData.minor,
                FMLInjectionData.rev, FMLInjectionData.build, FMLInjectionData.mccversion, FMLInjectionData.mcsversion);

        try
        {
            RelaunchLibraryManager.handleLaunch(minecraftHome, classLoader);
        }
        catch (Throwable t)
        {
            if (popupWindow != null)
            {
                try
                {
                    String logFile = new File(minecraftHome, "ForgeModLoader-client-0.log").getCanonicalPath();
                    JOptionPane.showMessageDialog(popupWindow, String.format(
                            "<html><div align=\"center\"><font size=\"+1\">There was a fatal error starting up minecraft and FML</font></div><br/>"
                                    + "Minecraft cannot launch in it's current configuration<br/>"
                                    + "Please consult the file <i><a href=\"file:///%s\">%s</a></i> for further information</html>", logFile, logFile),
                            "Fatal FML error", JOptionPane.ERROR_MESSAGE);
                }
                catch (Exception ex)
                {
                    // ah well, we tried
                }
            }
            throw new RuntimeException(t);
        }
    }

    /**
     * @return
     */
    private File computeExistingClientHome()
    {
        return FabricLoader.getInstance().getGameDir().toFile();

//        Class<? super Object> mcMaster = ReflectionHelper.getClass(getClass().getClassLoader(), "net.minecraft.client.Minecraft");
//        // If we get the system property we inject into the old MC, setup the
//        // dir, then pull the value
//        String str = System.getProperty("minecraft.applet.TargetDirectory");
//        if (str != null)
//        {
//            str = str.replace('/', File.separatorChar);
//            ReflectionHelper.setPrivateValue(mcMaster, null, new File(str), "minecraftDir", "am", "minecraftDir");
//        }
//        // We force minecraft to setup it's homedir very early on so we can
//        // inject stuff into it
//        Method setupHome = ReflectionHelper.findMethod(mcMaster, null, new String[] { "func_71380_b", "getMinecraftDir", "b" });
//        try
//        {
//            setupHome.invoke(null);
//        }
//        catch (Exception e)
//        {
//            // Hmmm
//        }
//        File minecraftHome = ReflectionHelper.getPrivateValue(mcMaster, null, "field_71463_am", "am", "minecraftDir");
//        return minecraftHome;
    }

    public static void appletEntry(Applet minecraftApplet)
    {
        side = "CLIENT";
        logFileNamePattern = "ForgeModLoader-client-%g.log";
        instance().relaunchApplet(minecraftApplet);
    }

    public static void preLaunchClientEntry() {
        side = "CLIENT";
        logFileNamePattern = "ForgeModLoader-client-%g.log";
        instance().preLaunchClient();
    }

    public File preLaunchCommon(boolean showWindow) {
        this.showWindow(showWindow);

        File mcDir = this.computeExistingClientHome();
        this.setupHome(mcDir);

        return mcDir;
    }

    public void preLaunchClient() {
        File mcDir = preLaunchCommon(true);
        this.setupNewClientHome(mcDir);

        if (this.popupWindow != null) {
            this.popupWindow.setVisible(false);
            this.popupWindow.dispose();
        }
    }

    private void relaunchApplet(Applet minecraftApplet)
    {
        showWindow(true);

        appletClass = ReflectionHelper.getClass(classLoader, "net.minecraft.client.MinecraftApplet");
        if (((MinecraftAppletExtension) minecraftApplet).isRelaunched())
        {
            if (popupWindow != null)
            {
                popupWindow.setVisible(false);
                popupWindow.dispose();
            }
            try
            {
                newApplet = minecraftApplet;
                ReflectionHelper.findMethod(appletClass, newApplet, new String[] { "fmlInitReentry" }).invoke(newApplet);
                return;
            }
            catch (Exception e)
            {
                System.out.println("FMLRelauncher.relaunchApplet");
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }

//        File mcDir = computeExistingClientHome();
//        setupHome(mcDir);
//        setupNewClientHome(mcDir);

        Class<? super Object> parentAppletClass = ReflectionHelper.getClass(getClass().getClassLoader(), "java.applet.Applet");

        try
        {
            newApplet = appletClass.newInstance();
            ((MinecraftAppletExtension) newApplet).setRelaunched(true);

//            Object appletContainer = ReflectionHelper.getPrivateValue(ReflectionHelper.getClass(getClass().getClassLoader(), "java.awt.Component"),
//                    minecraftApplet, "parent");
//
//            String launcherClassName = System.getProperty("minecraft.applet.WrapperClass", "net.minecraft.Launcher");
//            Class<? super Object> launcherClass = ReflectionHelper.getClass(getClass().getClassLoader(), launcherClassName);
//            if (launcherClass.isInstance(appletContainer))
//            {
//                ReflectionHelper.findMethod(ReflectionHelper.getClass(getClass().getClassLoader(), "java.awt.Container"), minecraftApplet,
//                        new String[] { "removeAll" }).invoke(appletContainer);
//                ReflectionHelper.findMethod(launcherClass, appletContainer, new String[] { "replace" }, parentAppletClass).invoke(appletContainer, newApplet);
//            }
//            else
            AppletLauncher appletContainer = (AppletLauncher) minecraftApplet.getParent();
            String launcherClassName = System.getProperty("minecraft.applet.WrapperClass", "net.minecraft.Launcher");
            Class<? super Object> launcherClass = ReflectionHelper.getClass(this.getClass().getClassLoader(), launcherClassName);
            if (!launcherClass.isInstance(appletContainer))
            {
                FMLRelaunchLog.severe("Found unknown applet parent %s, unable to inject!\n", appletContainer.getClass().getName());
                throw new RuntimeException();
            }

            appletContainer.removeAll();
            appletContainer.replace((Applet) this.newApplet);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
        finally
        {
            if (popupWindow != null)
            {
                popupWindow.setVisible(false);
                popupWindow.dispose();
            }
        }
    }

    public static void appletStart(Applet applet)
    {
        instance().startApplet(applet);
    }

    private void startApplet(Applet applet)
    {
        if (applet.getClass().getClassLoader() == classLoader)
        {
            if (popupWindow != null)
            {
                popupWindow.setVisible(false);
                popupWindow.dispose();
            }
            if (RelaunchLibraryManager.downloadMonitor.shouldStopIt())
            {
                System.exit(1);
            }
            try
            {
                ReflectionHelper.findMethod(appletClass, newApplet, new String[] { "fmlStartReentry" }).invoke(newApplet);
            }
            catch (Exception e)
            {
                System.out.println("FMLRelauncher.startApplet");
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
        return;
    }

    public static String side()
    {
        return side;
    }
}
