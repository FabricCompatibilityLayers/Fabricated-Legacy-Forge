package io.github.fabriccompatibilitylayers.fabricatedfml.remapper.discoverer;

import io.github.fabriccompatibilitylayers.fabricatedfml.remapper.candidate.CoremodCandidate;
import io.github.fabriccompatibilitylayers.modremappingapi.api.v2.ModCandidate;
import io.github.fabriccompatibilitylayers.modremappingapi.api.v2.ModDiscovererConfig;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarFile;

public class CoremodsDiscoverer implements ModDiscovererConfig.Collector {
    @Override
    public List<ModCandidate> collect(ModDiscovererConfig modDiscovererConfig, Path path, List<String> list) {
        try (JarFile jarFile = new JarFile(path.toFile())) {
            Attributes attributes = jarFile.getManifest().getMainAttributes();

            if (attributes.getValue("FMLCorePlugin") != null) {
                return Collections.singletonList(new CoremodCandidate(
                        path,
                        modDiscovererConfig
                ));
            }
        } catch (IOException e) {
            return Collections.emptyList();
        }

        return Collections.emptyList();
    }
}
