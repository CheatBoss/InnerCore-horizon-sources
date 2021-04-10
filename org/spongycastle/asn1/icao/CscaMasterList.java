package org.spongycastle.asn1.icao;

import org.spongycastle.asn1.x509.*;
import org.spongycastle.asn1.*;

public class CscaMasterList extends ASN1Object
{
    private Certificate[] certList;
    private ASN1Integer version;
    
    private CscaMasterList(final ASN1Sequence asn1Sequence) {
        this.version = new ASN1Integer(0L);
        if (asn1Sequence == null || asn1Sequence.size() == 0) {
            throw new IllegalArgumentException("null or empty sequence passed.");
        }
        if (asn1Sequence.size() == 2) {
            int n = 0;
            this.version = ASN1Integer.getInstance(asn1Sequence.getObjectAt(0));
            final ASN1Set instance = ASN1Set.getInstance(asn1Sequence.getObjectAt(1));
            this.certList = new Certificate[instance.size()];
            while (true) {
                final Certificate[] certList = this.certList;
                if (n >= certList.length) {
                    break;
                }
                certList[n] = Certificate.getInstance(instance.getObjectAt(n));
                ++n;
            }
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Incorrect sequence size: ");
        sb.append(asn1Sequence.size());
        throw new IllegalArgumentException(sb.toString());
    }
    
    public CscaMasterList(final Certificate[] array) {
        this.version = new ASN1Integer(0L);
        this.certList = this.copyCertList(array);
    }
    
    private Certificate[] copyCertList(final Certificate[] array) {
        final int length = array.length;
        final Certificate[] array2 = new Certificate[length];
        for (int i = 0; i != length; ++i) {
            array2[i] = array[i];
        }
        return array2;
    }
    
    public static CscaMasterList getInstance(final Object o) {
        if (o instanceof CscaMasterList) {
            return (CscaMasterList)o;
        }
        if (o != null) {
            return new CscaMasterList(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public Certificate[] getCertStructs() {
        return this.copyCertList(this.certList);
    }
    
    public int getVersion() {
        return this.version.getValue().intValue();
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.version);
        final ASN1EncodableVector asn1EncodableVector2 = new ASN1EncodableVector();
        int n = 0;
        while (true) {
            final Certificate[] certList = this.certList;
            if (n >= certList.length) {
                break;
            }
            asn1EncodableVector2.add(certList[n]);
            ++n;
        }
        asn1EncodableVector.add(new DERSet(asn1EncodableVector2));
        return new DERSequence(asn1EncodableVector);
    }
}
