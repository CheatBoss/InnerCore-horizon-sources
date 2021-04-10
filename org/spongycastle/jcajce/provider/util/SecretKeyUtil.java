package org.spongycastle.jcajce.provider.util;

import java.util.*;
import org.spongycastle.asn1.pkcs.*;
import org.spongycastle.util.*;
import org.spongycastle.asn1.nist.*;
import org.spongycastle.asn1.ntt.*;
import org.spongycastle.asn1.*;

public class SecretKeyUtil
{
    private static Map keySizes;
    
    static {
        (SecretKeyUtil.keySizes = new HashMap()).put(PKCSObjectIdentifiers.des_EDE3_CBC.getId(), Integers.valueOf(192));
        SecretKeyUtil.keySizes.put(NISTObjectIdentifiers.id_aes128_CBC, Integers.valueOf(128));
        SecretKeyUtil.keySizes.put(NISTObjectIdentifiers.id_aes192_CBC, Integers.valueOf(192));
        SecretKeyUtil.keySizes.put(NISTObjectIdentifiers.id_aes256_CBC, Integers.valueOf(256));
        SecretKeyUtil.keySizes.put(NTTObjectIdentifiers.id_camellia128_cbc, Integers.valueOf(128));
        SecretKeyUtil.keySizes.put(NTTObjectIdentifiers.id_camellia192_cbc, Integers.valueOf(192));
        SecretKeyUtil.keySizes.put(NTTObjectIdentifiers.id_camellia256_cbc, Integers.valueOf(256));
    }
    
    public static int getKeySize(final ASN1ObjectIdentifier asn1ObjectIdentifier) {
        final Integer n = SecretKeyUtil.keySizes.get(asn1ObjectIdentifier);
        if (n != null) {
            return n;
        }
        return -1;
    }
}
