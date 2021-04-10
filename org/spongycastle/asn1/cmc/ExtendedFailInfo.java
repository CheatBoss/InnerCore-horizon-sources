package org.spongycastle.asn1.cmc;

import org.spongycastle.asn1.*;

public class ExtendedFailInfo extends ASN1Object
{
    private final ASN1ObjectIdentifier failInfoOID;
    private final ASN1Encodable failInfoValue;
    
    public ExtendedFailInfo(final ASN1ObjectIdentifier failInfoOID, final ASN1Encodable failInfoValue) {
        this.failInfoOID = failInfoOID;
        this.failInfoValue = failInfoValue;
    }
    
    private ExtendedFailInfo(final ASN1Sequence asn1Sequence) {
        if (asn1Sequence.size() == 2) {
            this.failInfoOID = ASN1ObjectIdentifier.getInstance(asn1Sequence.getObjectAt(0));
            this.failInfoValue = asn1Sequence.getObjectAt(1);
            return;
        }
        throw new IllegalArgumentException("Sequence must be 2 elements.");
    }
    
    public static ExtendedFailInfo getInstance(final Object o) {
        if (o instanceof ExtendedFailInfo) {
            return (ExtendedFailInfo)o;
        }
        if (o instanceof ASN1Encodable) {
            final ASN1Primitive asn1Primitive = ((ASN1Encodable)o).toASN1Primitive();
            if (asn1Primitive instanceof ASN1Sequence) {
                return new ExtendedFailInfo((ASN1Sequence)asn1Primitive);
            }
        }
        else if (o instanceof byte[]) {
            return getInstance(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public ASN1ObjectIdentifier getFailInfoOID() {
        return this.failInfoOID;
    }
    
    public ASN1Encodable getFailInfoValue() {
        return this.failInfoValue;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        return new DERSequence(new ASN1Encodable[] { this.failInfoOID, this.failInfoValue });
    }
}
