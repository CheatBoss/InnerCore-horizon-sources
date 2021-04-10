package org.spongycastle.asn1.cmp;

import org.spongycastle.asn1.*;

public class CertConfirmContent extends ASN1Object
{
    private ASN1Sequence content;
    
    private CertConfirmContent(final ASN1Sequence content) {
        this.content = content;
    }
    
    public static CertConfirmContent getInstance(final Object o) {
        if (o instanceof CertConfirmContent) {
            return (CertConfirmContent)o;
        }
        if (o != null) {
            return new CertConfirmContent(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        return this.content;
    }
    
    public CertStatus[] toCertStatusArray() {
        final int size = this.content.size();
        final CertStatus[] array = new CertStatus[size];
        for (int i = 0; i != size; ++i) {
            array[i] = CertStatus.getInstance(this.content.getObjectAt(i));
        }
        return array;
    }
}
