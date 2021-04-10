package org.spongycastle.asn1.isismtt.x509;

import java.util.*;
import java.math.*;
import org.spongycastle.asn1.*;

public class MonetaryLimit extends ASN1Object
{
    ASN1Integer amount;
    DERPrintableString currency;
    ASN1Integer exponent;
    
    public MonetaryLimit(final String s, final int n, final int n2) {
        this.currency = new DERPrintableString(s, true);
        this.amount = new ASN1Integer(n);
        this.exponent = new ASN1Integer(n2);
    }
    
    private MonetaryLimit(final ASN1Sequence asn1Sequence) {
        if (asn1Sequence.size() == 3) {
            final Enumeration objects = asn1Sequence.getObjects();
            this.currency = DERPrintableString.getInstance(objects.nextElement());
            this.amount = ASN1Integer.getInstance(objects.nextElement());
            this.exponent = ASN1Integer.getInstance(objects.nextElement());
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Bad sequence size: ");
        sb.append(asn1Sequence.size());
        throw new IllegalArgumentException(sb.toString());
    }
    
    public static MonetaryLimit getInstance(final Object o) {
        if (o == null || o instanceof MonetaryLimit) {
            return (MonetaryLimit)o;
        }
        if (o instanceof ASN1Sequence) {
            return new MonetaryLimit(ASN1Sequence.getInstance(o));
        }
        throw new IllegalArgumentException("unknown object in getInstance");
    }
    
    public BigInteger getAmount() {
        return this.amount.getValue();
    }
    
    public String getCurrency() {
        return this.currency.getString();
    }
    
    public BigInteger getExponent() {
        return this.exponent.getValue();
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.currency);
        asn1EncodableVector.add(this.amount);
        asn1EncodableVector.add(this.exponent);
        return new DERSequence(asn1EncodableVector);
    }
}
