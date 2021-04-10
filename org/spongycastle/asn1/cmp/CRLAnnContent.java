package org.spongycastle.asn1.cmp;

import org.spongycastle.asn1.x509.*;
import org.spongycastle.asn1.*;

public class CRLAnnContent extends ASN1Object
{
    private ASN1Sequence content;
    
    private CRLAnnContent(final ASN1Sequence content) {
        this.content = content;
    }
    
    public CRLAnnContent(final CertificateList list) {
        this.content = new DERSequence(list);
    }
    
    public static CRLAnnContent getInstance(final Object o) {
        if (o instanceof CRLAnnContent) {
            return (CRLAnnContent)o;
        }
        if (o != null) {
            return new CRLAnnContent(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public CertificateList[] getCertificateLists() {
        final int size = this.content.size();
        final CertificateList[] array = new CertificateList[size];
        for (int i = 0; i != size; ++i) {
            array[i] = CertificateList.getInstance(this.content.getObjectAt(i));
        }
        return array;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        return this.content;
    }
}
