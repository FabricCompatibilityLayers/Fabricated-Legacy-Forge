/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedfml.compat.guava;

import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;

import java.nio.charset.StandardCharsets;

public class GuavaStubs {
    public static HashCode hash_HashFunction_hashString(HashFunction hashFunction, String input) {
        return hashFunction.hashString(input, StandardCharsets.UTF_8);
    }
}
