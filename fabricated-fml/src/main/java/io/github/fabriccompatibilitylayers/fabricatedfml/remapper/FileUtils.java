/**
 * Copyright (C) 2025 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedfml.remapper;

import java.io.IOException;
import java.net.URI;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;

public class FileUtils {
    private static final Map<String, String> ZIP_PROPERTIES = new HashMap<>();

    static {
        ZIP_PROPERTIES.put("create", "false");
        ZIP_PROPERTIES.put("encoding", "UTF-8");
    }

    public static FileSystem getJarFileSystem(URI uri) throws IOException {
        try {
            return FileSystems.getFileSystem(uri);
        } catch (FileSystemNotFoundException ignore) {
            try {
                return FileSystems.newFileSystem(uri, ZIP_PROPERTIES);
            } catch (FileSystemAlreadyExistsException e) {
                return FileSystems.getFileSystem(uri);
            }
        }
    }

    public static FileSystem getJarFileSystem(Path path) throws IOException {
        URI uri = URI.create("jar:" + path.toUri());

        return getJarFileSystem(uri);
    }
}
