package org.spongycastle.asn1.crmf;

import org.spongycastle.asn1.pkcs.*;
import org.spongycastle.asn1.x509.*;
import org.spongycastle.asn1.*;

public class EncKeyWithID extends ASN1Object
{
    private final ASN1Encodable identifier;
    private final PrivateKeyInfo privKeyInfo;
    
    private EncKeyWithID(final ASN1Sequence asn1Sequence) {
        this.privKeyInfo = PrivateKeyInfo.getInstance(asn1Sequence.getObjectAt(0));
        ASN1Encodable identifier;
        if (asn1Sequence.size() > 1) {
            if (!(asn1Sequence.getObjectAt(1) instanceof DERUTF8String)) {
                identifier = GeneralName.getInstance(asn1Sequence.getObjectAt(1));
            }
            else {
                identifier = asn1Sequence.getObjectAt(1);
            }
        }
        else {
            identifier = null;
        }
        this.identifier = identifier;
    }
    
    public EncKeyWithID(final PrivateKeyInfo privKeyInfo) {
        this.privKeyInfo = privKeyInfo;
        this.identifier = null;
    }
    
    public EncKeyWithID(final PrivateKeyInfo privKeyInfo, final DERUTF8String identifier) {
        this.privKeyInfo = privKeyInfo;
        this.identifier = identifier;
    }
    
    public EncKeyWithID(final PrivateKeyInfo privKeyInfo, final GeneralName identifier) {
        this.privKeyInfo = privKeyInfo;
        this.identifier = identifier;
    }
    
    public static EncKeyWithID getInstance(final Object o) {
        if (o instanceof EncKeyWithID) {
            return (EncKeyWithID)o;
        }
        if (o != null) {
            return new EncKeyWithID(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public ASN1Encodable getIdentifier() {
        return this.identifier;
    }
    
    public PrivateKeyInfo getPrivateKey() {
        return this.privKeyInfo;
    }
    
    public boolean hasIdentifier() {
        return this.identifier != null;
    }
    
    public boolean isIdentifierUTF8String() {
        return this.identifier instanceof DERUTF8String;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.privKeyInfo);
        final ASN1Encodable identifier = this.identifier;
        if (identifier != null) {
            asn1EncodableVector.add(identifier);
        }
        return new DERSequence(asn1EncodableVector);
    }
}
