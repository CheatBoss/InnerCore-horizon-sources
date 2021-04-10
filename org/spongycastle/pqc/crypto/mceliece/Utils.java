package org.spongycastle.pqc.crypto.mceliece;

import org.spongycastle.crypto.*;
import org.spongycastle.crypto.digests.*;

class Utils
{
    static Digest getDigest(final String s) {
        if (s.equals("SHA-1")) {
            return new SHA1Digest();
        }
        if (s.equals("SHA-224")) {
            return new SHA224Digest();
        }
        if (s.equals("SHA-256")) {
            return new SHA256Digest();
        }
        if (s.equals("SHA-384")) {
            return new SHA384Digest();
        }
        if (s.equals("SHA-512")) {
            return new SHA512Digest();
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("unrecognised digest algorithm: ");
        sb.append(s);
        throw new IllegalArgumentException(sb.toString());
    }
}
