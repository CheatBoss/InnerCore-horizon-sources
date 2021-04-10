package org.spongycastle.asn1.crmf;

import org.spongycastle.asn1.x509.*;
import org.spongycastle.asn1.*;

public class EncryptedValue extends ASN1Object
{
    private DERBitString encSymmKey;
    private DERBitString encValue;
    private AlgorithmIdentifier intendedAlg;
    private AlgorithmIdentifier keyAlg;
    private AlgorithmIdentifier symmAlg;
    private ASN1OctetString valueHint;
    
    private EncryptedValue(final ASN1Sequence asn1Sequence) {
        int n;
        for (n = 0; asn1Sequence.getObjectAt(n) instanceof ASN1TaggedObject; ++n) {
            final ASN1TaggedObject asn1TaggedObject = (ASN1TaggedObject)asn1Sequence.getObjectAt(n);
            final int tagNo = asn1TaggedObject.getTagNo();
            if (tagNo != 0) {
                if (tagNo != 1) {
                    if (tagNo != 2) {
                        if (tagNo != 3) {
                            if (tagNo != 4) {
                                final StringBuilder sb = new StringBuilder();
                                sb.append("Unknown tag encountered: ");
                                sb.append(asn1TaggedObject.getTagNo());
                                throw new IllegalArgumentException(sb.toString());
                            }
                            this.valueHint = ASN1OctetString.getInstance(asn1TaggedObject, false);
                        }
                        else {
                            this.keyAlg = AlgorithmIdentifier.getInstance(asn1TaggedObject, false);
                        }
                    }
                    else {
                        this.encSymmKey = DERBitString.getInstance(asn1TaggedObject, false);
                    }
                }
                else {
                    this.symmAlg = AlgorithmIdentifier.getInstance(asn1TaggedObject, false);
                }
            }
            else {
                this.intendedAlg = AlgorithmIdentifier.getInstance(asn1TaggedObject, false);
            }
        }
        this.encValue = DERBitString.getInstance(asn1Sequence.getObjectAt(n));
    }
    
    public EncryptedValue(final AlgorithmIdentifier intendedAlg, final AlgorithmIdentifier symmAlg, final DERBitString encSymmKey, final AlgorithmIdentifier keyAlg, final ASN1OctetString valueHint, final DERBitString encValue) {
        if (encValue != null) {
            this.intendedAlg = intendedAlg;
            this.symmAlg = symmAlg;
            this.encSymmKey = encSymmKey;
            this.keyAlg = keyAlg;
            this.valueHint = valueHint;
            this.encValue = encValue;
            return;
        }
        throw new IllegalArgumentException("'encValue' cannot be null");
    }
    
    private void addOptional(final ASN1EncodableVector asn1EncodableVector, final int n, final ASN1Encodable asn1Encodable) {
        if (asn1Encodable != null) {
            asn1EncodableVector.add(new DERTaggedObject(false, n, asn1Encodable));
        }
    }
    
    public static EncryptedValue getInstance(final Object o) {
        if (o instanceof EncryptedValue) {
            return (EncryptedValue)o;
        }
        if (o != null) {
            return new EncryptedValue(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public DERBitString getEncSymmKey() {
        return this.encSymmKey;
    }
    
    public DERBitString getEncValue() {
        return this.encValue;
    }
    
    public AlgorithmIdentifier getIntendedAlg() {
        return this.intendedAlg;
    }
    
    public AlgorithmIdentifier getKeyAlg() {
        return this.keyAlg;
    }
    
    public AlgorithmIdentifier getSymmAlg() {
        return this.symmAlg;
    }
    
    public ASN1OctetString getValueHint() {
        return this.valueHint;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        this.addOptional(asn1EncodableVector, 0, this.intendedAlg);
        this.addOptional(asn1EncodableVector, 1, this.symmAlg);
        this.addOptional(asn1EncodableVector, 2, this.encSymmKey);
        this.addOptional(asn1EncodableVector, 3, this.keyAlg);
        this.addOptional(asn1EncodableVector, 4, this.valueHint);
        asn1EncodableVector.add(this.encValue);
        return new DERSequence(asn1EncodableVector);
    }
}
