package org.spongycastle.asn1.crmf;

import org.spongycastle.asn1.x509.*;
import org.spongycastle.asn1.*;

public class POPOSigningKeyInput extends ASN1Object
{
    private SubjectPublicKeyInfo publicKey;
    private PKMACValue publicKeyMAC;
    private GeneralName sender;
    
    private POPOSigningKeyInput(final ASN1Sequence asn1Sequence) {
        final ASN1Encodable object = asn1Sequence.getObjectAt(0);
        if (object instanceof ASN1TaggedObject) {
            final ASN1TaggedObject asn1TaggedObject = (ASN1TaggedObject)object;
            if (asn1TaggedObject.getTagNo() != 0) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Unknown authInfo tag: ");
                sb.append(asn1TaggedObject.getTagNo());
                throw new IllegalArgumentException(sb.toString());
            }
            this.sender = GeneralName.getInstance(asn1TaggedObject.getObject());
        }
        else {
            this.publicKeyMAC = PKMACValue.getInstance(object);
        }
        this.publicKey = SubjectPublicKeyInfo.getInstance(asn1Sequence.getObjectAt(1));
    }
    
    public POPOSigningKeyInput(final PKMACValue publicKeyMAC, final SubjectPublicKeyInfo publicKey) {
        this.publicKeyMAC = publicKeyMAC;
        this.publicKey = publicKey;
    }
    
    public POPOSigningKeyInput(final GeneralName sender, final SubjectPublicKeyInfo publicKey) {
        this.sender = sender;
        this.publicKey = publicKey;
    }
    
    public static POPOSigningKeyInput getInstance(final Object o) {
        if (o instanceof POPOSigningKeyInput) {
            return (POPOSigningKeyInput)o;
        }
        if (o != null) {
            return new POPOSigningKeyInput(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public SubjectPublicKeyInfo getPublicKey() {
        return this.publicKey;
    }
    
    public PKMACValue getPublicKeyMAC() {
        return this.publicKeyMAC;
    }
    
    public GeneralName getSender() {
        return this.sender;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        ASN1Object publicKeyMAC;
        if (this.sender != null) {
            publicKeyMAC = new DERTaggedObject(false, 0, this.sender);
        }
        else {
            publicKeyMAC = this.publicKeyMAC;
        }
        asn1EncodableVector.add(publicKeyMAC);
        asn1EncodableVector.add(this.publicKey);
        return new DERSequence(asn1EncodableVector);
    }
}
