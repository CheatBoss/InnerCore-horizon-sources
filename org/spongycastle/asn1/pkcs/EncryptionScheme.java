package org.spongycastle.asn1.pkcs;

import org.spongycastle.asn1.x509.*;
import org.spongycastle.asn1.*;

public class EncryptionScheme extends ASN1Object
{
    private AlgorithmIdentifier algId;
    
    public EncryptionScheme(final ASN1ObjectIdentifier asn1ObjectIdentifier, final ASN1Encodable asn1Encodable) {
        this.algId = new AlgorithmIdentifier(asn1ObjectIdentifier, asn1Encodable);
    }
    
    private EncryptionScheme(final ASN1Sequence asn1Sequence) {
        this.algId = AlgorithmIdentifier.getInstance(asn1Sequence);
    }
    
    public static EncryptionScheme getInstance(final Object o) {
        if (o instanceof EncryptionScheme) {
            return (EncryptionScheme)o;
        }
        if (o != null) {
            return new EncryptionScheme(ASN1Sequence.getInstance(o));
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
