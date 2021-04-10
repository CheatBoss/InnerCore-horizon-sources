package org.spongycastle.jcajce.provider.asymmetric.util;

import java.util.*;
import org.spongycastle.asn1.oiw.*;
import org.spongycastle.asn1.pkcs.*;
import org.spongycastle.util.*;

public class DESUtil
{
    private static final Set<String> des;
    
    static {
        (des = new HashSet<String>()).add("DES");
        DESUtil.des.add("DESEDE");
        DESUtil.des.add(OIWObjectIdentifiers.desCBC.getId());
        DESUtil.des.add(PKCSObjectIdentifiers.des_EDE3_CBC.getId());
        DESUtil.des.add(PKCSObjectIdentifiers.des_EDE3_CBC.getId());
        DESUtil.des.add(PKCSObjectIdentifiers.id_alg_CMS3DESwrap.getId());
    }
    
    public static boolean isDES(String upperCase) {
        upperCase = Strings.toUpperCase(upperCase);
        return DESUtil.des.contains(upperCase);
    }
    
    public static void setOddParity(final byte[] array) {
        for (int i = 0; i < array.length; ++i) {
            final byte b = array[i];
            array[i] = (byte)((b & 0xFE) | ((b >> 7 ^ (b >> 1 ^ b >> 2 ^ b >> 3 ^ b >> 4 ^ b >> 5 ^ b >> 6) ^ 0x1) & 0x1));
        }
    }
}
