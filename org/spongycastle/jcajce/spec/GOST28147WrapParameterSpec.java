package org.spongycastle.jcajce.spec;

import java.security.spec.*;
import java.util.*;
import org.spongycastle.asn1.cryptopro.*;
import org.spongycastle.crypto.engines.*;
import org.spongycastle.asn1.*;
import org.spongycastle.util.*;

public class GOST28147WrapParameterSpec implements AlgorithmParameterSpec
{
    private static Map oidMappings;
    private byte[] sBox;
    private byte[] ukm;
    
    static {
        (GOST28147WrapParameterSpec.oidMappings = new HashMap()).put(CryptoProObjectIdentifiers.id_Gost28147_89_CryptoPro_A_ParamSet, "E-A");
        GOST28147WrapParameterSpec.oidMappings.put(CryptoProObjectIdentifiers.id_Gost28147_89_CryptoPro_B_ParamSet, "E-B");
        GOST28147WrapParameterSpec.oidMappings.put(CryptoProObjectIdentifiers.id_Gost28147_89_CryptoPro_C_ParamSet, "E-C");
        GOST28147WrapParameterSpec.oidMappings.put(CryptoProObjectIdentifiers.id_Gost28147_89_CryptoPro_D_ParamSet, "E-D");
    }
    
    public GOST28147WrapParameterSpec(final String s) {
        this.ukm = null;
        this.sBox = null;
        this.sBox = GOST28147Engine.getSBox(s);
    }
    
    public GOST28147WrapParameterSpec(final String s, final byte[] array) {
        this(s);
        System.arraycopy(array, 0, this.ukm = new byte[array.length], 0, array.length);
    }
    
    public GOST28147WrapParameterSpec(final ASN1ObjectIdentifier asn1ObjectIdentifier, final byte[] array) {
        this(getName(asn1ObjectIdentifier));
        this.ukm = Arrays.clone(array);
    }
    
    public GOST28147WrapParameterSpec(final byte[] array) {
        this.ukm = null;
        this.sBox = null;
        System.arraycopy(array, 0, this.sBox = new byte[array.length], 0, array.length);
    }
    
    public GOST28147WrapParameterSpec(byte[] ukm, final byte[] array) {
        this(ukm);
        ukm = new byte[array.length];
        System.arraycopy(array, 0, this.ukm = ukm, 0, array.length);
    }
    
    private static String getName(final ASN1ObjectIdentifier asn1ObjectIdentifier) {
        final String s = GOST28147WrapParameterSpec.oidMappings.get(asn1ObjectIdentifier);
        if (s != null) {
            return s;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("unknown OID: ");
        sb.append(asn1ObjectIdentifier);
        throw new IllegalArgumentException(sb.toString());
    }
    
    public byte[] getSBox() {
        return Arrays.clone(this.sBox);
    }
    
    public byte[] getUKM() {
        return Arrays.clone(this.ukm);
    }
}
