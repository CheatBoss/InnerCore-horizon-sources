package org.spongycastle.asn1.dvcs;

import java.math.*;
import org.spongycastle.asn1.*;

public class ServiceType extends ASN1Object
{
    public static final ServiceType CCPD;
    public static final ServiceType CPD;
    public static final ServiceType VPKC;
    public static final ServiceType VSD;
    private ASN1Enumerated value;
    
    static {
        CPD = new ServiceType(1);
        VSD = new ServiceType(2);
        VPKC = new ServiceType(3);
        CCPD = new ServiceType(4);
    }
    
    public ServiceType(final int n) {
        this.value = new ASN1Enumerated(n);
    }
    
    private ServiceType(final ASN1Enumerated value) {
        this.value = value;
    }
    
    public static ServiceType getInstance(final Object o) {
        if (o instanceof ServiceType) {
            return (ServiceType)o;
        }
        if (o != null) {
            return new ServiceType(ASN1Enumerated.getInstance(o));
        }
        return null;
    }
    
    public static ServiceType getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Enumerated.getInstance(asn1TaggedObject, b));
    }
    
    public BigInteger getValue() {
        return this.value.getValue();
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        return this.value;
    }
    
    @Override
    public String toString() {
        final int intValue = this.value.getValue().intValue();
        final StringBuilder sb = new StringBuilder();
        sb.append("");
        sb.append(intValue);
        String s;
        if (intValue == ServiceType.CPD.getValue().intValue()) {
            s = "(CPD)";
        }
        else if (intValue == ServiceType.VSD.getValue().intValue()) {
            s = "(VSD)";
        }
        else if (intValue == ServiceType.VPKC.getValue().intValue()) {
            s = "(VPKC)";
        }
        else if (intValue == ServiceType.CCPD.getValue().intValue()) {
            s = "(CCPD)";
        }
        else {
            s = "?";
        }
        sb.append(s);
        return sb.toString();
    }
}
