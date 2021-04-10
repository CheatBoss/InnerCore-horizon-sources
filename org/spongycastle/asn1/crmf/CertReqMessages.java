package org.spongycastle.asn1.crmf;

import org.spongycastle.asn1.*;

public class CertReqMessages extends ASN1Object
{
    private ASN1Sequence content;
    
    private CertReqMessages(final ASN1Sequence content) {
        this.content = content;
    }
    
    public CertReqMessages(final CertReqMsg certReqMsg) {
        this.content = new DERSequence(certReqMsg);
    }
    
    public CertReqMessages(final CertReqMsg[] array) {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        for (int i = 0; i < array.length; ++i) {
            asn1EncodableVector.add(array[i]);
        }
        this.content = new DERSequence(asn1EncodableVector);
    }
    
    public static CertReqMessages getInstance(final Object o) {
        if (o instanceof CertReqMessages) {
            return (CertReqMessages)o;
        }
        if (o != null) {
            return new CertReqMessages(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        return this.content;
    }
    
    public CertReqMsg[] toCertReqMsgArray() {
        final int size = this.content.size();
        final CertReqMsg[] array = new CertReqMsg[size];
        for (int i = 0; i != size; ++i) {
            array[i] = CertReqMsg.getInstance(this.content.getObjectAt(i));
        }
        return array;
    }
}
