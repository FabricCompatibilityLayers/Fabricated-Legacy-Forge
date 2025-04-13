package fr.catcore.fabricatedforge.mixin.forgefml.server.command;

import net.minecraft.command.*;
import net.minecraft.server.command.CommandRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.CommandEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

@Mixin(CommandRegistry.class)
public abstract class CommandRegistryMixin {

    @Shadow
    private static String[] method_3104(String[] strings) {
        return new String[0];
    }

    @Shadow @Final private Map commandMap;

    /**
     * @author Minecraft Forge
     * @reason none
     */
    @Overwrite
    public void executeCommand(CommandSource par1ICommandSender, String par2Str) {
        if (par2Str.startsWith("/")) {
            par2Str = par2Str.substring(1);
        }

        String[] var3 = par2Str.split(" ");
        String var4 = var3[0];
        var3 = method_3104(var3);
        Command var5 = (Command)this.commandMap.get(var4);

        try {
            if (var5 == null) {
                throw new NotFoundException();
            }

            if (var5.isAccessible(par1ICommandSender)) {
                CommandEvent event = new CommandEvent(var5, par1ICommandSender, var3);
                if (!MinecraftForge.EVENT_BUS.post(event)) {
                    var5.execute(par1ICommandSender, event.parameters);
                } else if (event.exception != null) {
                    throw event.exception;
                }
            } else {
                par1ICommandSender.method_3331("§cYou do not have permission to use this command.");
            }
        } catch (IncorrectUsageException var7) {
            par1ICommandSender.method_3331("§c" + par1ICommandSender.translate("commands.generic.usage", par1ICommandSender.translate(var7.getMessage(), var7.getArgs())));
        } catch (CommandException var8) {
            par1ICommandSender.method_3331("§c" + par1ICommandSender.translate(var8.getMessage(), var8.getArgs()));
        } catch (Throwable var9) {
            par1ICommandSender.method_3331("§c" + par1ICommandSender.translate("commands.generic.exception"));
            var9.printStackTrace();
        }

    }
}
