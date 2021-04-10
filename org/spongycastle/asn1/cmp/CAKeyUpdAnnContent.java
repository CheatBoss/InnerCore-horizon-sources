package org.spongycastle.asn1.cmp;

import org.spongycastle.asn1.*;

public class CAKeyUpdAnnContent extends ASN1Object
{
    private CMPCertificate newWithNew;
    private CMPCertificate newWithOld;
    private CMPCertificate oldWithNew;
    
    private CAKeyUpdAnnContent(final ASN1Sequence asn1Sequence) {
        this.oldWithNew = CMPCertificate.getInstance(asn1Sequence.getObjectAt(0));
        this.newWithOld = CMPCertificate.getInstance(asn1Sequence.getObjectAt(1));
        this.newWithNew = CMPCertificate.getInstance(asn1Sequence.getObjectAt(2));
    }
    
    public CAKeyUpdAnnContent(final CMPCertificate oldWithNew, final CMPCertificate newWithOld, final CMPCertificate newWithNew) {
        this.oldWithNew = oldWithNew;
        this.newWithOld = newWithOld;
        this.newWithNew = newWithNew;
    }
    
    public static CAKeyUpdAnnContent getInstance(final Object o) {
        if (o instanceof CAKeyUpdAnnContent) {
            return (CAKeyUpdAnnContent)o;
        }
        if (o != null) {
            return new CAKeyUpdAnnContent(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public CMPCertificate getNewWithNew() {
        return this.newWithNew;
    }
    
    public CMPCertificate getNewWithOld() {
        return this.newWithOld;
    }
    
    public CMPCertificate getOldWithNew() {
        return this.oldWithNew;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.oldWithNew);
        asn1EncodableVector.add(this.newWithOld);
        asn1EncodableVector.add(this.newWithNew);
        return new DERSequence(asn1EncodableVector);
    }
}
