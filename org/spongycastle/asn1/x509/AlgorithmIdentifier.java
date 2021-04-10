package org.spongycastle.asn1.x509;

import org.spongycastle.asn1.*;

public class AlgorithmIdentifier extends ASN1Object
{
    private ASN1ObjectIdentifier algorithm;
    private ASN1Encodable parameters;
    
    public AlgorithmIdentifier(final ASN1ObjectIdentifier algorithm) {
        this.algorithm = algorithm;
    }
    
    public AlgorithmIdentifier(final ASN1ObjectIdentifier algorithm, final ASN1Encodable parameters) {
        this.algorithm = algorithm;
        this.parameters = parameters;
    }
    
    private AlgorithmIdentifier(final ASN1Sequence asn1Sequence) {
        if (asn1Sequence.size() >= 1 && asn1Sequence.size() <= 2) {
            this.algorithm = ASN1ObjectIdentifier.getInstance(asn1Sequence.getObjectAt(0));
            ASN1Encodable object;
            if (asn1Sequence.size() == 2) {
                object = asn1Sequence.getObjectAt(1);
            }
            else {
                object = null;
            }
            this.parameters = object;
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Bad sequence size: ");
        sb.append(asn1Sequence.size());
        throw new IllegalArgumentException(sb.toString());
    }
    
    public static AlgorithmIdentifier getInstance(final Object o) {
        if (o instanceof AlgorithmIdentifier) {
            return (AlgorithmIdentifier)o;
        }
        if (o != null) {
            return new AlgorithmIdentifier(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public static AlgorithmIdentifier getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public ASN1ObjectIdentifier getAlgorithm() {
        return this.algorithm;
    }
    
    public ASN1Encodable getParameters() {
        return this.parameters;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.algorithm);
        final ASN1Encodable parameters = this.parameters;
        if (parameters != null) {
            asn1EncodableVector.add(parameters);
        }
        return new DERSequence(asn1EncodableVector);
    }
}
