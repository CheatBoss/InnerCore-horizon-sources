package org.spongycastle.asn1.cmp;

import org.spongycastle.asn1.*;

public class CertRepMessage extends ASN1Object
{
    private ASN1Sequence caPubs;
    private ASN1Sequence response;
    
    private CertRepMessage(final ASN1Sequence asn1Sequence) {
        final int size = asn1Sequence.size();
        int n = 0;
        if (size > 1) {
            this.caPubs = ASN1Sequence.getInstance((ASN1TaggedObject)asn1Sequence.getObjectAt(0), true);
            n = 1;
        }
        this.response = ASN1Sequence.getInstance(asn1Sequence.getObjectAt(n));
    }
    
    public CertRepMessage(final CMPCertificate[] array, final CertResponse[] array2) {
        if (array2 != null) {
            final int n = 0;
            if (array != null) {
                final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
                for (int i = 0; i < array.length; ++i) {
                    asn1EncodableVector.add(array[i]);
                }
                this.caPubs = new DERSequence(asn1EncodableVector);
            }
            final ASN1EncodableVector asn1EncodableVector2 = new ASN1EncodableVector();
            for (int j = n; j < array2.length; ++j) {
                asn1EncodableVector2.add(array2[j]);
            }
            this.response = new DERSequence(asn1EncodableVector2);
            return;
        }
        throw new IllegalArgumentException("'response' cannot be null");
    }
    
    public static CertRepMessage getInstance(final Object o) {
        if (o instanceof CertRepMessage) {
            return (CertRepMessage)o;
        }
        if (o != null) {
            return new CertRepMessage(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public CMPCertificate[] getCaPubs() {
        final ASN1Sequence caPubs = this.caPubs;
        if (caPubs == null) {
            return null;
        }
        final int size = caPubs.size();
        final CMPCertificate[] array = new CMPCertificate[size];
        for (int i = 0; i != size; ++i) {
            array[i] = CMPCertificate.getInstance(this.caPubs.getObjectAt(i));
        }
        return array;
    }
    
    public CertResponse[] getResponse() {
        final int size = this.response.size();
        final CertResponse[] array = new CertResponse[size];
        for (int i = 0; i != size; ++i) {
            array[i] = CertResponse.getInstance(this.response.getObjectAt(i));
        }
        return array;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        if (this.caPubs != null) {
            asn1EncodableVector.add(new DERTaggedObject(true, 1, this.caPubs));
        }
        asn1EncodableVector.add(this.response);
        return new DERSequence(asn1EncodableVector);
    }
}
