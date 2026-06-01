/**
 * Copyright (C) 2025 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedfml.compat.osl;

import net.fabricmc.loader.api.FabricLoader;
import net.ornithemc.osl.entrypoints.api.ModInitializer;
import net.ornithemc.osl.entrypoints.api.client.ClientLaunchEvents;
import net.ornithemc.osl.entrypoints.api.client.ClientModInitializer;
import net.ornithemc.osl.entrypoints.api.server.ServerLaunchEvents;
import net.ornithemc.osl.entrypoints.api.server.ServerModInitializer;
import net.ornithemc.osl.entrypoints.impl.LaunchUtils;

import java.util.Arrays;
import java.util.function.Function;

public class OSLEntrypointsHelper {
    public static void clientEntrypoint(Function<String, String> getParameter) {
        System.out.println("Client entrypoint running");
        FabricLoader.getInstance().invokeEntrypoints("client-init", ClientModInitializer.class, ClientModInitializer::initClient);
        FabricLoader.getInstance().invokeEntrypoints("init", ModInitializer.class, ModInitializer::init);
        ClientLaunchEvents.PARSE_RUN_ARGS.invoker().accept(LaunchUtils.wrapFabricRunArgs(getParameter::apply));
    }

    public static void serverEntrypoint(String[] args) {
        FabricLoader.getInstance().invokeEntrypoints("server-init", ServerModInitializer.class, ServerModInitializer::initServer);
        FabricLoader.getInstance().invokeEntrypoints("init", ModInitializer.class, ModInitializer::init);
        ServerLaunchEvents.PARSE_RUN_ARGS.invoker().accept(Arrays.copyOf(args, args.length));
    }
}
