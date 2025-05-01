package io.github.fabriccompatibilitylayers.fabricatedfml.extension.client;

public interface MinecraftAppletExtension {
    void setRelaunched(boolean relaunched);

    boolean isRelaunched();

    void fmlInitReentry();

    void fmlStartReentry();
}
