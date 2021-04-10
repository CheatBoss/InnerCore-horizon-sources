package org.spongycastle.asn1.x509;

import java.math.*;
import org.spongycastle.asn1.*;

public class CRLNumber extends ASN1Object
{
    private BigInteger number;
    
    public CRLNumber(final BigInteger number) {
        this.number = number;
    }
    
    public static CRLNumber getInstance(final Object o) {
        if (o instanceof CRLNumber) {
            return (CRLNumber)o;
        }
        if (o != null) {
            return new CRLNumber(ASN1Integer.getInstance(o).getValue());
        }
        return null;
    }
    
    public BigInteger getCRLNumber() {
        return this.number;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        return new ASN1Integer(this.number);
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("CRLNumber: ");
        sb.append(this.getCRLNumber());
        return sb.toString();
    }
}
