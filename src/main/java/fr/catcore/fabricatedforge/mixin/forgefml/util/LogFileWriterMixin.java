package fr.catcore.fabricatedforge.mixin.forgefml.util;

import cpw.mods.fml.common.FMLLog;
import net.minecraft.util.LogFileWriter;
import net.minecraft.util.LogFormatter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

@Mixin(LogFileWriter.class)
public class LogFileWriterMixin {

    @Shadow public static Logger LOGGER;

    /**
     * @author Minecraft Forge
     */
    @Overwrite
    public static void method_1974() {
        LogFormatter var0 = LogFormatterAccessor.newInstance();
        LOGGER.setParent(FMLLog.getLogger());

        try {
            FileHandler var2 = new FileHandler("server.log", true);
            var2.setFormatter(var0);
            LOGGER.addHandler(var2);
        } catch (Exception var2) {
            LOGGER.log(Level.WARNING, "Failed to log to server.log", var2);
        }

    }
}
