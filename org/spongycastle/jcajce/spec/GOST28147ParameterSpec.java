package org.spongycastle.jcajce.spec;

import java.security.spec.*;
import java.util.*;
import org.spongycastle.asn1.cryptopro.*;
import org.spongycastle.crypto.engines.*;
import org.spongycastle.asn1.*;
import org.spongycastle.util.*;

public class GOST28147ParameterSpec implements AlgorithmParameterSpec
{
    private static Map oidMappings;
    private byte[] iv;
    private byte[] sBox;
    
    static {
        (GOST28147ParameterSpec.oidMappings = new HashMap()).put(CryptoProObjectIdentifiers.id_Gost28147_89_CryptoPro_A_ParamSet, "E-A");
        GOST28147ParameterSpec.oidMappings.put(CryptoProObjectIdentifiers.id_Gost28147_89_CryptoPro_B_ParamSet, "E-B");
        GOST28147ParameterSpec.oidMappings.put(CryptoProObjectIdentifiers.id_Gost28147_89_CryptoPro_C_ParamSet, "E-C");
        GOST28147ParameterSpec.oidMappings.put(CryptoProObjectIdentifiers.id_Gost28147_89_CryptoPro_D_ParamSet, "E-D");
    }
    
    public GOST28147ParameterSpec(final String s) {
        this.iv = null;
        this.sBox = null;
        this.sBox = GOST28147Engine.getSBox(s);
    }
    
    public GOST28147ParameterSpec(final String s, final byte[] array) {
        this(s);
        System.arraycopy(array, 0, this.iv = new byte[array.length], 0, array.length);
    }
    
    public GOST28147ParameterSpec(final ASN1ObjectIdentifier asn1ObjectIdentifier, final byte[] array) {
        this(getName(asn1ObjectIdentifier));
        this.iv = Arrays.clone(array);
    }
    
    public GOST28147ParameterSpec(final byte[] array) {
        this.iv = null;
        this.sBox = null;
        System.arraycopy(array, 0, this.sBox = new byte[array.length], 0, array.length);
    }
    
    public GOST28147ParameterSpec(byte[] iv, final byte[] array) {
        this(iv);
        iv = new byte[array.length];
        System.arraycopy(array, 0, this.iv = iv, 0, array.length);
    }
    
    private static String getName(final ASN1ObjectIdentifier asn1ObjectIdentifier) {
        final String s = GOST28147ParameterSpec.oidMappings.get(asn1ObjectIdentifier);
        if (s != null) {
            return s;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("unknown OID: ");
        sb.append(asn1ObjectIdentifier);
        throw new IllegalArgumentException(sb.toString());
    }
    
    public byte[] getIV() {
        return Arrays.clone(this.iv);
    }
    
    public byte[] getSBox() {
        return Arrays.clone(this.sBox);
    }
    
    public byte[] getSbox() {
        return Arrays.clone(this.sBox);
    }
}
