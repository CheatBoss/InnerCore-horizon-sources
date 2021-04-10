package org.spongycastle.asn1.crmf;

import org.spongycastle.asn1.x509.*;
import org.spongycastle.asn1.*;

public class POPOSigningKey extends ASN1Object
{
    private AlgorithmIdentifier algorithmIdentifier;
    private POPOSigningKeyInput poposkInput;
    private DERBitString signature;
    
    private POPOSigningKey(final ASN1Sequence asn1Sequence) {
        int n = 0;
        if (asn1Sequence.getObjectAt(0) instanceof ASN1TaggedObject) {
            final ASN1TaggedObject asn1TaggedObject = (ASN1TaggedObject)asn1Sequence.getObjectAt(0);
            if (asn1TaggedObject.getTagNo() != 0) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Unknown POPOSigningKeyInput tag: ");
                sb.append(asn1TaggedObject.getTagNo());
                throw new IllegalArgumentException(sb.toString());
            }
            this.poposkInput = POPOSigningKeyInput.getInstance(asn1TaggedObject.getObject());
            n = 1;
        }
        this.algorithmIdentifier = AlgorithmIdentifier.getInstance(asn1Sequence.getObjectAt(n));
        this.signature = DERBitString.getInstance(asn1Sequence.getObjectAt(n + 1));
    }
    
    public POPOSigningKey(final POPOSigningKeyInput poposkInput, final AlgorithmIdentifier algorithmIdentifier, final DERBitString signature) {
        this.poposkInput = poposkInput;
        this.algorithmIdentifier = algorithmIdentifier;
        this.signature = signature;
    }
    
    public static POPOSigningKey getInstance(final Object o) {
        if (o instanceof POPOSigningKey) {
            return (POPOSigningKey)o;
        }
        if (o != null) {
            return new POPOSigningKey(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public static POPOSigningKey getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public AlgorithmIdentifier getAlgorithmIdentifier() {
        return this.algorithmIdentifier;
    }
    
    public POPOSigningKeyInput getPoposkInput() {
        return this.poposkInput;
    }
    
    public DERBitString getSignature() {
        return this.signature;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        if (this.poposkInput != null) {
            asn1EncodableVector.add(new DERTaggedObject(false, 0, this.poposkInput));
        }
        asn1EncodableVector.add(this.algorithmIdentifier);
        asn1EncodableVector.add(this.signature);
        return new DERSequence(asn1EncodableVector);
    }
}
