package org.spongycastle.asn1.pkcs;

import org.spongycastle.asn1.x509.*;
import org.spongycastle.asn1.*;

public class KeyDerivationFunc extends ASN1Object
{
    private AlgorithmIdentifier algId;
    
    public KeyDerivationFunc(final ASN1ObjectIdentifier asn1ObjectIdentifier, final ASN1Encodable asn1Encodable) {
        this.algId = new AlgorithmIdentifier(asn1ObjectIdentifier, asn1Encodable);
    }
    
    private KeyDerivationFunc(final ASN1Sequence asn1Sequence) {
        this.algId = AlgorithmIdentifier.getInstance(asn1Sequence);
    }
    
    public static KeyDerivationFunc getInstance(final Object o) {
        if (o instanceof KeyDerivationFunc) {
            return (KeyDerivationFunc)o;
        }
        if (o != null) {
            return new KeyDerivationFunc(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public ASN1ObjectIdentifier getAlgorithm() {
        return this.algId.getAlgorithm();
    }
    
    public ASN1Encodable getParameters() {
        return this.algId.getParameters();
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        return this.algId.toASN1Primitive();
    }
}
