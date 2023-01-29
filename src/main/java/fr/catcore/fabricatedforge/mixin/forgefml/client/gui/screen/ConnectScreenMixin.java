package fr.catcore.fabricatedforge.mixin.forgefml.client.gui.screen;

import fr.catcore.fabricatedforge.mixininterface.IConnectScreen;
import net.minecraft.client.class_469;
import net.minecraft.client.gui.screen.ConnectScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ConnectScreen.class)
public class ConnectScreenMixin implements IConnectScreen {
    @Shadow private boolean connectingCancelled;

    @Shadow private class_469 field_1631;

    @Override
    public void forceTermination() {
        this.connectingCancelled = true;
        this.field_1631 = null;
    }
}
