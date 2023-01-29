package fr.catcore.fabricatedforge.mixin.forgefml.network;

import cpw.mods.fml.common.asm.ReobfuscationMarker;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.NetworkEncryptionUtils;
import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.CipherKeyGenerator;
import org.bouncycastle.crypto.KeyGenerationParameters;
import org.bouncycastle.crypto.engines.AESFastEngine;
import org.bouncycastle.crypto.io.CipherInputStream;
import org.bouncycastle.crypto.io.CipherOutputStream;
import org.bouncycastle.crypto.modes.CFBBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Key;
import java.security.SecureRandom;
import java.security.Security;

@Mixin(NetworkEncryptionUtils.class)
@ReobfuscationMarker
public class NetworkEncryptionUtilsMixin {
    /**
     * @author forge
     * @reason bouncycastle
     */
    @Environment(EnvType.CLIENT)
    @Overwrite
    public static SecretKey generateKey() {
        CipherKeyGenerator var0 = new CipherKeyGenerator();
        var0.init(new KeyGenerationParameters(new SecureRandom(), 128));
        return new SecretKeySpec(var0.generateKey(), "AES");
    }

    private static BufferedBlockCipher a(boolean par0, Key par1Key) {
        BufferedBlockCipher var2 = new BufferedBlockCipher(new CFBBlockCipher(new AESFastEngine(), 8));
        var2.init(par0, new ParametersWithIV(new KeyParameter(par1Key.getEncoded()), par1Key.getEncoded(), 0, 16));
        return var2;
    }

    /**
     * @author forge
     * @reason bouncycastle
     */
    @Overwrite
    public static OutputStream method_2288(SecretKey par0SecretKey, OutputStream par1OutputStream) {
        return new CipherOutputStream(par1OutputStream, a(true, par0SecretKey));
    }

    /**
     * @author forge
     * @reason bouncycastle
     */
    @Overwrite
    public static InputStream method_2287(SecretKey par0SecretKey, InputStream par1InputStream) {
        return new CipherInputStream(par1InputStream, a(false, par0SecretKey));
    }

    @Inject(method = "<clinit>", at = @At("RETURN"))
    private static void addProvider(CallbackInfo ci) {
        Security.addProvider(new BouncyCastleProvider());
    }
}
