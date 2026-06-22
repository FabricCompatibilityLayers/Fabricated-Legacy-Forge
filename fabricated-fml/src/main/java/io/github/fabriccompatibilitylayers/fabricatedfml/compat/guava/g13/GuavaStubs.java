/**
 * Copyright (C) 2026 Fabric Compatibility Layer Team
 *
 * Licensed under the Open Software License version 3.0
 */
package io.github.fabriccompatibilitylayers.fabricatedfml.compat.guava.g13;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.google.common.io.ByteStreams;
import com.google.common.io.Files;
import com.google.common.io.InputSupplier;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.security.MessageDigest;

public class GuavaStubs {
    private static HashFunction messageDigestToHashFunction(MessageDigest messageDigest) {
        switch (messageDigest.getAlgorithm()) {
            case "MD5":
                return Hashing.md5();
            case "SHA-1": return Hashing.sha1();
            case "SHA-256": return Hashing.sha256();
            case "SHA-512": return Hashing.sha512();
            default:
                try {
                    Class mdhfClass = Class.forName("com.google.common.hash.MessageDigestHashFunction");
                    Constructor ctr = mdhfClass.getDeclaredConstructor(String.class, String.class);
                    ctr.setAccessible(true);
                    return (HashFunction) ctr.newInstance(messageDigest.getAlgorithm(), "");
                } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException |
                         IllegalAccessException | InvocationTargetException e) {
                    throw new IllegalArgumentException(messageDigest.getAlgorithm());
                }
        }
    }

    public static byte[] io_Files_getDigest(File file, MessageDigest messageDigest) throws IOException {
        return Files.hash(file, messageDigestToHashFunction(messageDigest)).asBytes();
    }

    public static byte[] io_ByteStreams_getDigest(InputSupplier<? extends InputStream> supplier, MessageDigest messageDigest) throws IOException {
        return ByteStreams.hash(supplier, messageDigestToHashFunction(messageDigest)).asBytes();
    }
}
