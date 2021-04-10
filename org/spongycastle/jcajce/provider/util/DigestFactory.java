package org.spongycastle.jcajce.provider.util;

import java.util.*;
import org.spongycastle.asn1.pkcs.*;
import org.spongycastle.asn1.oiw.*;
import org.spongycastle.asn1.nist.*;
import org.spongycastle.crypto.*;
import org.spongycastle.util.*;
import org.spongycastle.asn1.*;

public class DigestFactory
{
    private static Set md5;
    private static Map oids;
    private static Set sha1;
    private static Set sha224;
    private static Set sha256;
    private static Set sha384;
    private static Set sha3_224;
    private static Set sha3_256;
    private static Set sha3_384;
    private static Set sha3_512;
    private static Set sha512;
    private static Set sha512_224;
    private static Set sha512_256;
    
    static {
        DigestFactory.md5 = new HashSet();
        DigestFactory.sha1 = new HashSet();
        DigestFactory.sha224 = new HashSet();
        DigestFactory.sha256 = new HashSet();
        DigestFactory.sha384 = new HashSet();
        DigestFactory.sha512 = new HashSet();
        DigestFactory.sha512_224 = new HashSet();
        DigestFactory.sha512_256 = new HashSet();
        DigestFactory.sha3_224 = new HashSet();
        DigestFactory.sha3_256 = new HashSet();
        DigestFactory.sha3_384 = new HashSet();
        DigestFactory.sha3_512 = new HashSet();
        DigestFactory.oids = new HashMap();
        DigestFactory.md5.add("MD5");
        DigestFactory.md5.add(PKCSObjectIdentifiers.md5.getId());
        DigestFactory.sha1.add("SHA1");
        DigestFactory.sha1.add("SHA-1");
        DigestFactory.sha1.add(OIWObjectIdentifiers.idSHA1.getId());
        DigestFactory.sha224.add("SHA224");
        DigestFactory.sha224.add("SHA-224");
        DigestFactory.sha224.add(NISTObjectIdentifiers.id_sha224.getId());
        DigestFactory.sha256.add("SHA256");
        DigestFactory.sha256.add("SHA-256");
        DigestFactory.sha256.add(NISTObjectIdentifiers.id_sha256.getId());
        DigestFactory.sha384.add("SHA384");
        DigestFactory.sha384.add("SHA-384");
        DigestFactory.sha384.add(NISTObjectIdentifiers.id_sha384.getId());
        DigestFactory.sha512.add("SHA512");
        DigestFactory.sha512.add("SHA-512");
        DigestFactory.sha512.add(NISTObjectIdentifiers.id_sha512.getId());
        DigestFactory.sha512_224.add("SHA512(224)");
        DigestFactory.sha512_224.add("SHA-512(224)");
        DigestFactory.sha512_224.add(NISTObjectIdentifiers.id_sha512_224.getId());
        DigestFactory.sha512_256.add("SHA512(256)");
        DigestFactory.sha512_256.add("SHA-512(256)");
        DigestFactory.sha512_256.add(NISTObjectIdentifiers.id_sha512_256.getId());
        DigestFactory.sha3_224.add("SHA3-224");
        DigestFactory.sha3_224.add(NISTObjectIdentifiers.id_sha3_224.getId());
        DigestFactory.sha3_256.add("SHA3-256");
        DigestFactory.sha3_256.add(NISTObjectIdentifiers.id_sha3_256.getId());
        DigestFactory.sha3_384.add("SHA3-384");
        DigestFactory.sha3_384.add(NISTObjectIdentifiers.id_sha3_384.getId());
        DigestFactory.sha3_512.add("SHA3-512");
        DigestFactory.sha3_512.add(NISTObjectIdentifiers.id_sha3_512.getId());
        DigestFactory.oids.put("MD5", PKCSObjectIdentifiers.md5);
        DigestFactory.oids.put(PKCSObjectIdentifiers.md5.getId(), PKCSObjectIdentifiers.md5);
        DigestFactory.oids.put("SHA1", OIWObjectIdentifiers.idSHA1);
        DigestFactory.oids.put("SHA-1", OIWObjectIdentifiers.idSHA1);
        DigestFactory.oids.put(OIWObjectIdentifiers.idSHA1.getId(), OIWObjectIdentifiers.idSHA1);
        DigestFactory.oids.put("SHA224", NISTObjectIdentifiers.id_sha224);
        DigestFactory.oids.put("SHA-224", NISTObjectIdentifiers.id_sha224);
        DigestFactory.oids.put(NISTObjectIdentifiers.id_sha224.getId(), NISTObjectIdentifiers.id_sha224);
        DigestFactory.oids.put("SHA256", NISTObjectIdentifiers.id_sha256);
        DigestFactory.oids.put("SHA-256", NISTObjectIdentifiers.id_sha256);
        DigestFactory.oids.put(NISTObjectIdentifiers.id_sha256.getId(), NISTObjectIdentifiers.id_sha256);
        DigestFactory.oids.put("SHA384", NISTObjectIdentifiers.id_sha384);
        DigestFactory.oids.put("SHA-384", NISTObjectIdentifiers.id_sha384);
        DigestFactory.oids.put(NISTObjectIdentifiers.id_sha384.getId(), NISTObjectIdentifiers.id_sha384);
        DigestFactory.oids.put("SHA512", NISTObjectIdentifiers.id_sha512);
        DigestFactory.oids.put("SHA-512", NISTObjectIdentifiers.id_sha512);
        DigestFactory.oids.put(NISTObjectIdentifiers.id_sha512.getId(), NISTObjectIdentifiers.id_sha512);
        DigestFactory.oids.put("SHA512(224)", NISTObjectIdentifiers.id_sha512_224);
        DigestFactory.oids.put("SHA-512(224)", NISTObjectIdentifiers.id_sha512_224);
        DigestFactory.oids.put(NISTObjectIdentifiers.id_sha512_224.getId(), NISTObjectIdentifiers.id_sha512_224);
        DigestFactory.oids.put("SHA512(256)", NISTObjectIdentifiers.id_sha512_256);
        DigestFactory.oids.put("SHA-512(256)", NISTObjectIdentifiers.id_sha512_256);
        DigestFactory.oids.put(NISTObjectIdentifiers.id_sha512_256.getId(), NISTObjectIdentifiers.id_sha512_256);
        DigestFactory.oids.put("SHA3-224", NISTObjectIdentifiers.id_sha3_224);
        DigestFactory.oids.put(NISTObjectIdentifiers.id_sha3_224.getId(), NISTObjectIdentifiers.id_sha3_224);
        DigestFactory.oids.put("SHA3-256", NISTObjectIdentifiers.id_sha3_256);
        DigestFactory.oids.put(NISTObjectIdentifiers.id_sha3_256.getId(), NISTObjectIdentifiers.id_sha3_256);
        DigestFactory.oids.put("SHA3-384", NISTObjectIdentifiers.id_sha3_384);
        DigestFactory.oids.put(NISTObjectIdentifiers.id_sha3_384.getId(), NISTObjectIdentifiers.id_sha3_384);
        DigestFactory.oids.put("SHA3-512", NISTObjectIdentifiers.id_sha3_512);
        DigestFactory.oids.put(NISTObjectIdentifiers.id_sha3_512.getId(), NISTObjectIdentifiers.id_sha3_512);
    }
    
    public static Digest getDigest(String upperCase) {
        upperCase = Strings.toUpperCase(upperCase);
        if (DigestFactory.sha1.contains(upperCase)) {
            return org.spongycastle.crypto.util.DigestFactory.createSHA1();
        }
        if (DigestFactory.md5.contains(upperCase)) {
            return org.spongycastle.crypto.util.DigestFactory.createMD5();
        }
        if (DigestFactory.sha224.contains(upperCase)) {
            return org.spongycastle.crypto.util.DigestFactory.createSHA224();
        }
        if (DigestFactory.sha256.contains(upperCase)) {
            return org.spongycastle.crypto.util.DigestFactory.createSHA256();
        }
        if (DigestFactory.sha384.contains(upperCase)) {
            return org.spongycastle.crypto.util.DigestFactory.createSHA384();
        }
        if (DigestFactory.sha512.contains(upperCase)) {
            return org.spongycastle.crypto.util.DigestFactory.createSHA512();
        }
        if (DigestFactory.sha512_224.contains(upperCase)) {
            return org.spongycastle.crypto.util.DigestFactory.createSHA512_224();
        }
        if (DigestFactory.sha512_256.contains(upperCase)) {
            return org.spongycastle.crypto.util.DigestFactory.createSHA512_256();
        }
        if (DigestFactory.sha3_224.contains(upperCase)) {
            return org.spongycastle.crypto.util.DigestFactory.createSHA3_224();
        }
        if (DigestFactory.sha3_256.contains(upperCase)) {
            return org.spongycastle.crypto.util.DigestFactory.createSHA3_256();
        }
        if (DigestFactory.sha3_384.contains(upperCase)) {
            return org.spongycastle.crypto.util.DigestFactory.createSHA3_384();
        }
        if (DigestFactory.sha3_512.contains(upperCase)) {
            return org.spongycastle.crypto.util.DigestFactory.createSHA3_512();
        }
        return null;
    }
    
    public static ASN1ObjectIdentifier getOID(final String s) {
        return DigestFactory.oids.get(s);
    }
    
    public static boolean isSameDigest(final String s, final String s2) {
        return (DigestFactory.sha1.contains(s) && DigestFactory.sha1.contains(s2)) || (DigestFactory.sha224.contains(s) && DigestFactory.sha224.contains(s2)) || (DigestFactory.sha256.contains(s) && DigestFactory.sha256.contains(s2)) || (DigestFactory.sha384.contains(s) && DigestFactory.sha384.contains(s2)) || (DigestFactory.sha512.contains(s) && DigestFactory.sha512.contains(s2)) || (DigestFactory.sha512_224.contains(s) && DigestFactory.sha512_224.contains(s2)) || (DigestFactory.sha512_256.contains(s) && DigestFactory.sha512_256.contains(s2)) || (DigestFactory.sha3_224.contains(s) && DigestFactory.sha3_224.contains(s2)) || (DigestFactory.sha3_256.contains(s) && DigestFactory.sha3_256.contains(s2)) || (DigestFactory.sha3_384.contains(s) && DigestFactory.sha3_384.contains(s2)) || (DigestFactory.sha3_512.contains(s) && DigestFactory.sha3_512.contains(s2)) || (DigestFactory.md5.contains(s) && DigestFactory.md5.contains(s2));
    }
}
