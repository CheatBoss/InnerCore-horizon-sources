package org.spongycastle.pqc.jcajce.provider.newhope;

import org.spongycastle.pqc.jcajce.interfaces.*;
import org.spongycastle.pqc.crypto.newhope.*;
import org.spongycastle.asn1.pkcs.*;
import java.io.*;
import org.spongycastle.util.*;
import org.spongycastle.pqc.asn1.*;
import org.spongycastle.asn1.x509.*;
import org.spongycastle.asn1.*;
import org.spongycastle.crypto.*;

public class BCNHPrivateKey implements NHPrivateKey
{
    private static final long serialVersionUID = 1L;
    private final NHPrivateKeyParameters params;
    
    public BCNHPrivateKey(final PrivateKeyInfo privateKeyInfo) throws IOException {
        this.params = new NHPrivateKeyParameters(convert(ASN1OctetString.getInstance(privateKeyInfo.parsePrivateKey()).getOctets()));
    }
    
    public BCNHPrivateKey(final NHPrivateKeyParameters params) {
        this.params = params;
    }
    
    private static short[] convert(final byte[] array) {
        final int n = array.length / 2;
        final short[] array2 = new short[n];
        for (int i = 0; i != n; ++i) {
            array2[i] = Pack.littleEndianToShort(array, i * 2);
        }
        return array2;
    }
    
    @Override
    public boolean equals(final Object o) {
        return o != null && o instanceof BCNHPrivateKey && Arrays.areEqual(this.params.getSecData(), ((BCNHPrivateKey)o).params.getSecData());
    }
    
    @Override
    public final String getAlgorithm() {
        return "NH";
    }
    
    @Override
    public byte[] getEncoded() {
        try {
            final AlgorithmIdentifier algorithmIdentifier = new AlgorithmIdentifier(PQCObjectIdentifiers.newHope);
            final short[] secData = this.params.getSecData();
            final byte[] array = new byte[secData.length * 2];
            for (int i = 0; i != secData.length; ++i) {
                Pack.shortToLittleEndian(secData[i], array, i * 2);
            }
            return new PrivateKeyInfo(algorithmIdentifier, new DEROctetString(array)).getEncoded();
        }
        catch (IOException ex) {
            return null;
        }
    }
    
    @Override
    public String getFormat() {
        return "PKCS#8";
    }
    
    CipherParameters getKeyParams() {
        return this.params;
    }
    
    @Override
    public short[] getSecretData() {
        return this.params.getSecData();
    }
    
    @Override
    public int hashCode() {
        return Arrays.hashCode(this.params.getSecData());
    }
}
