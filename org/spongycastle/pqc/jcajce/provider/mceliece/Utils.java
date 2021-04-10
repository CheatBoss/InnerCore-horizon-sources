package org.spongycastle.pqc.jcajce.provider.mceliece;

import org.spongycastle.asn1.x509.*;
import org.spongycastle.asn1.oiw.*;
import org.spongycastle.asn1.*;
import org.spongycastle.asn1.nist.*;
import org.spongycastle.crypto.*;
import org.spongycastle.crypto.util.*;

class Utils
{
    static AlgorithmIdentifier getDigAlgId(final String s) {
        if (s.equals("SHA-1")) {
            return new AlgorithmIdentifier(OIWObjectIdentifiers.idSHA1, DERNull.INSTANCE);
        }
        if (s.equals("SHA-224")) {
            return new AlgorithmIdentifier(NISTObjectIdentifiers.id_sha224, DERNull.INSTANCE);
        }
        if (s.equals("SHA-256")) {
            return new AlgorithmIdentifier(NISTObjectIdentifiers.id_sha256, DERNull.INSTANCE);
        }
        if (s.equals("SHA-384")) {
            return new AlgorithmIdentifier(NISTObjectIdentifiers.id_sha384, DERNull.INSTANCE);
        }
        if (s.equals("SHA-512")) {
            return new AlgorithmIdentifier(NISTObjectIdentifiers.id_sha512, DERNull.INSTANCE);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("unrecognised digest algorithm: ");
        sb.append(s);
        throw new IllegalArgumentException(sb.toString());
    }
    
    static Digest getDigest(final AlgorithmIdentifier algorithmIdentifier) {
        if (algorithmIdentifier.getAlgorithm().equals(OIWObjectIdentifiers.idSHA1)) {
            return DigestFactory.createSHA1();
        }
        if (algorithmIdentifier.getAlgorithm().equals(NISTObjectIdentifiers.id_sha224)) {
            return DigestFactory.createSHA224();
        }
        if (algorithmIdentifier.getAlgorithm().equals(NISTObjectIdentifiers.id_sha256)) {
            return DigestFactory.createSHA256();
        }
        if (algorithmIdentifier.getAlgorithm().equals(NISTObjectIdentifiers.id_sha384)) {
            return DigestFactory.createSHA384();
        }
        if (algorithmIdentifier.getAlgorithm().equals(NISTObjectIdentifiers.id_sha512)) {
            return DigestFactory.createSHA512();
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("unrecognised OID in digest algorithm identifier: ");
        sb.append(algorithmIdentifier.getAlgorithm());
        throw new IllegalArgumentException(sb.toString());
    }
}
