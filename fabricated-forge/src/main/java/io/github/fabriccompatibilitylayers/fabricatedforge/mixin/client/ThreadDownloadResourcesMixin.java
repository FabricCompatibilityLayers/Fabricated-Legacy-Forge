/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedforge.mixin.client;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import net.minecraft.src.ThreadDownloadResources;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ThreadDownloadResources.class)
public class ThreadDownloadResourcesMixin {

    // Pattern N (@Redirect on URL.openStream()): replaces the call-site entirely — no original
    // call needed since we're substituting the stream source with a timeout-configured URLConnection.
    @Redirect(
        method = "run",
        at = @At(value = "INVOKE", target = "Ljava/net/URL;openStream()Ljava/io/InputStream;")
    )
    private InputStream forge$openXmlStreamWithTimeout(URL url) throws IOException {
        URLConnection con = url.openConnection();
        con.setConnectTimeout(60000);
        con.setReadTimeout(60000);
        return con.getInputStream();
    }

    // Pattern N (@Redirect on URL.openStream()): same substitution as forge$openXmlStreamWithTimeout
    // but scoped to downloadResource — adds connect/read timeouts to individual file downloads.
    @Redirect(
        method = "downloadResource(Ljava/net/URL;Ljava/io/File;J)V",
        at = @At(value = "INVOKE", target = "Ljava/net/URL;openStream()Ljava/io/InputStream;")
    )
    private InputStream forge$openResourceStreamWithTimeout(URL url) throws IOException {
        URLConnection con = url.openConnection();
        con.setConnectTimeout(60000);
        con.setReadTimeout(60000);
        return con.getInputStream();
    }

}