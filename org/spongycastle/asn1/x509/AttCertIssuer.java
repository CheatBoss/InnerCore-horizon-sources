package org.spongycastle.asn1.x509;

import org.spongycastle.asn1.*;

public class AttCertIssuer extends ASN1Object implements ASN1Choice
{
    ASN1Primitive choiceObj;
    ASN1Encodable obj;
    
    public AttCertIssuer(final GeneralNames obj) {
        this.obj = obj;
        this.choiceObj = obj.toASN1Primitive();
    }
    
    public AttCertIssuer(final V2Form obj) {
        this.obj = obj;
        this.choiceObj = new DERTaggedObject(false, 0, this.obj);
    }
    
    public static AttCertIssuer getInstance(final Object o) {
        if (o == null || o instanceof AttCertIssuer) {
            return (AttCertIssuer)o;
        }
        if (o instanceof V2Form) {
            return new AttCertIssuer(V2Form.getInstance(o));
        }
        if (o instanceof GeneralNames) {
            return new AttCertIssuer((GeneralNames)o);
        }
        if (o instanceof ASN1TaggedObject) {
            return new AttCertIssuer(V2Form.getInstance((ASN1TaggedObject)o, false));
        }
        if (o instanceof ASN1Sequence) {
            return new AttCertIssuer(GeneralNames.getInstance(o));
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("unknown object in factory: ");
        sb.append(o.getClass().getName());
        throw new IllegalArgumentException(sb.toString());
    }
    
    public static AttCertIssuer getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(asn1TaggedObject.getObject());
    }
    
    public ASN1Encodable getIssuer() {
        return this.obj;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        return this.choiceObj;
    }
}
