/*
 * The FML Forge Mod Loader suite.
 * Copyright (C) 2012 cpw
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this library; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 */
package cpw.mods.fml.common;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.cert.Certificate;

public class CertificateHelper {
    private static final String HEXES = "0123456789abcdef";

    public CertificateHelper() {
    }

    public static String getFingerprint(Certificate certificate) {
        if (certificate == null) {
            return "NO VALID CERTIFICATE FOUND";
        } else {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA-1");
                byte[] der = certificate.getEncoded();
                md.update(der);
                byte[] digest = md.digest();
                return hexify(digest);
            } catch (Exception var4) {
                return null;
            }
        }
    }

    public static String getFingerprint(ByteBuffer buffer) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(buffer);
            byte[] chksum = digest.digest();
            return hexify(chksum);
        } catch (Exception var3) {
            return null;
        }
    }

    private static String hexify(byte[] chksum) {
        StringBuilder hex = new StringBuilder(2 * chksum.length);

        for(byte b : chksum) {
            hex.append("0123456789abcdef".charAt((b & 240) >> 4)).append("0123456789abcdef".charAt(b & 15));
        }

        return hex.toString();
    }
}
