package org.spongycastle.pqc.jcajce.provider.xmss;

import org.spongycastle.asn1.*;
import org.spongycastle.asn1.nist.*;
import org.spongycastle.crypto.digests.*;
import org.spongycastle.crypto.*;

class DigestUtil
{
    static Digest getDigest(final ASN1ObjectIdentifier asn1ObjectIdentifier) {
        if (asn1ObjectIdentifier.equals(NISTObjectIdentifiers.id_sha256)) {
            return new SHA256Digest();
        }
        if (asn1ObjectIdentifier.equals(NISTObjectIdentifiers.id_sha512)) {
            return new SHA512Digest();
        }
        if (asn1ObjectIdentifier.equals(NISTObjectIdentifiers.id_shake128)) {
            return new SHAKEDigest(128);
        }
        if (asn1ObjectIdentifier.equals(NISTObjectIdentifiers.id_shake256)) {
            return new SHAKEDigest(256);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("unrecognized digest OID: ");
        sb.append(asn1ObjectIdentifier);
        throw new IllegalArgumentException(sb.toString());
    }
    
    public static byte[] getDigestResult(final Digest digest) {
        final int digestSize = getDigestSize(digest);
        final byte[] array = new byte[digestSize];
        if (digest instanceof Xof) {
            ((Xof)digest).doFinal(array, 0, digestSize);
            return array;
        }
        digest.doFinal(array, 0);
        return array;
    }
    
    public static int getDigestSize(final Digest digest) {
        if (digest instanceof Xof) {
            return digest.getDigestSize() * 2;
        }
        return digest.getDigestSize();
    }
    
    public static String getXMSSDigestName(final ASN1ObjectIdentifier asn1ObjectIdentifier) {
        if (asn1ObjectIdentifier.equals(NISTObjectIdentifiers.id_sha256)) {
            return "SHA256";
        }
        if (asn1ObjectIdentifier.equals(NISTObjectIdentifiers.id_sha512)) {
            return "SHA512";
        }
        if (asn1ObjectIdentifier.equals(NISTObjectIdentifiers.id_shake128)) {
            return "SHAKE128";
        }
        if (asn1ObjectIdentifier.equals(NISTObjectIdentifiers.id_shake256)) {
            return "SHAKE256";
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("unrecognized digest OID: ");
        sb.append(asn1ObjectIdentifier);
        throw new IllegalArgumentException(sb.toString());
    }
}
