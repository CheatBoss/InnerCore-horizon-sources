package org.spongycastle.crypto.agreement.kdf;

import org.spongycastle.crypto.*;
import org.spongycastle.asn1.*;

public class DHKDFParameters implements DerivationParameters
{
    private ASN1ObjectIdentifier algorithm;
    private byte[] extraInfo;
    private int keySize;
    private byte[] z;
    
    public DHKDFParameters(final ASN1ObjectIdentifier asn1ObjectIdentifier, final int n, final byte[] array) {
        this(asn1ObjectIdentifier, n, array, null);
    }
    
    public DHKDFParameters(final ASN1ObjectIdentifier algorithm, final int keySize, final byte[] z, final byte[] extraInfo) {
        this.algorithm = algorithm;
        this.keySize = keySize;
        this.z = z;
        this.extraInfo = extraInfo;
    }
    
    public ASN1ObjectIdentifier getAlgorithm() {
        return this.algorithm;
    }
    
    public byte[] getExtraInfo() {
        return this.extraInfo;
    }
    
    public int getKeySize() {
        return this.keySize;
    }
    
    public byte[] getZ() {
        return this.z;
    }
}
