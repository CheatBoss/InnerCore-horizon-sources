package org.spongycastle.asn1.icao;

import java.util.*;
import org.spongycastle.asn1.*;

public class DataGroupHash extends ASN1Object
{
    ASN1OctetString dataGroupHashValue;
    ASN1Integer dataGroupNumber;
    
    public DataGroupHash(final int n, final ASN1OctetString dataGroupHashValue) {
        this.dataGroupNumber = new ASN1Integer(n);
        this.dataGroupHashValue = dataGroupHashValue;
    }
    
    private DataGroupHash(final ASN1Sequence asn1Sequence) {
        final Enumeration objects = asn1Sequence.getObjects();
        this.dataGroupNumber = ASN1Integer.getInstance(objects.nextElement());
        this.dataGroupHashValue = ASN1OctetString.getInstance(objects.nextElement());
    }
    
    public static DataGroupHash getInstance(final Object o) {
        if (o instanceof DataGroupHash) {
            return (DataGroupHash)o;
        }
        if (o != null) {
            return new DataGroupHash(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public ASN1OctetString getDataGroupHashValue() {
        return this.dataGroupHashValue;
    }
    
    public int getDataGroupNumber() {
        return this.dataGroupNumber.getValue().intValue();
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.dataGroupNumber);
        asn1EncodableVector.add(this.dataGroupHashValue);
        return new DERSequence(asn1EncodableVector);
    }
}
