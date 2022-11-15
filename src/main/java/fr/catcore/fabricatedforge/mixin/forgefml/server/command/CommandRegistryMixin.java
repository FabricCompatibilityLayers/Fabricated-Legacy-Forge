package fr.catcore.fabricatedforge.mixin.forgefml.server.command;

import net.minecraft.command.*;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.command.CommandRegistry;
import net.minecraft.util.PlayerSelector;
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

    @Shadow protected abstract int method_4642(Command command, String[] args);

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
        int var6 = this.method_4642(var5, var3);

        try {
            if (var5 == null) {
                throw new NotFoundException();
            }

            if (var5.isAccessible(par1ICommandSender)) {
                CommandEvent event = new CommandEvent(var5, par1ICommandSender, var3);
                if (MinecraftForge.EVENT_BUS.post(event)) {
                    if (event.exception != null) {
                        throw event.exception;
                    }

                    return;
                }

                if (var6 > -1) {
                    ServerPlayerEntity[] var7 = PlayerSelector.selectPlayers(par1ICommandSender, var3[var6]);
                    String var8 = var3[var6];

                    for(ServerPlayerEntity var12 : var7) {
                        var3[var6] = var12.getTranslationKey();

                        try {
                            var5.execute(par1ICommandSender, var3);
                        } catch (PlayerNotFoundException var15) {
                            par1ICommandSender.method_3331("§c" + par1ICommandSender.translate(var15.getMessage(), var15.getArgs()));
                        }
                    }

                    var3[var6] = var8;
                } else {
                    var5.execute(par1ICommandSender, var3);
                }
            } else {
                par1ICommandSender.method_3331("§cYou do not have permission to use this command.");
            }
        } catch (IncorrectUsageException var16) {
            par1ICommandSender.method_3331(
                    "§c" + par1ICommandSender.translate("commands.generic.usage", new Object[]{par1ICommandSender.translate(var16.getMessage(), var16.getArgs())})
            );
        } catch (CommandException var171) {
            par1ICommandSender.method_3331("§c" + par1ICommandSender.translate(var171.getMessage(), var171.getArgs()));
        } catch (Throwable var18) {
            par1ICommandSender.method_3331("§c" + par1ICommandSender.translate("commands.generic.exception", new Object[0]));
            var18.printStackTrace();
        }
    }
}
