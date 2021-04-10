package org.spongycastle.asn1.x509.qualified;

import org.spongycastle.asn1.*;

public class TypeOfBiometricData extends ASN1Object implements ASN1Choice
{
    public static final int HANDWRITTEN_SIGNATURE = 1;
    public static final int PICTURE = 0;
    ASN1Encodable obj;
    
    public TypeOfBiometricData(final int n) {
        if (n != 0 && n != 1) {
            final StringBuilder sb = new StringBuilder();
            sb.append("unknow PredefinedBiometricType : ");
            sb.append(n);
            throw new IllegalArgumentException(sb.toString());
        }
        this.obj = new ASN1Integer(n);
    }
    
    public TypeOfBiometricData(final ASN1ObjectIdentifier obj) {
        this.obj = obj;
    }
    
    public static TypeOfBiometricData getInstance(final Object o) {
        if (o == null || o instanceof TypeOfBiometricData) {
            return (TypeOfBiometricData)o;
        }
        if (o instanceof ASN1Integer) {
            return new TypeOfBiometricData(ASN1Integer.getInstance(o).getValue().intValue());
        }
        if (o instanceof ASN1ObjectIdentifier) {
            return new TypeOfBiometricData(ASN1ObjectIdentifier.getInstance(o));
        }
        throw new IllegalArgumentException("unknown object in getInstance");
    }
    
    public ASN1ObjectIdentifier getBiometricDataOid() {
        return (ASN1ObjectIdentifier)this.obj;
    }
    
    public int getPredefinedBiometricType() {
        return ((ASN1Integer)this.obj).getValue().intValue();
    }
    
    public boolean isPredefined() {
        return this.obj instanceof ASN1Integer;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        return this.obj.toASN1Primitive();
    }
}
