package org.spongycastle.asn1.crmf;

import org.spongycastle.asn1.x509.*;
import java.math.*;
import org.spongycastle.asn1.*;

public class CertId extends ASN1Object
{
    private GeneralName issuer;
    private ASN1Integer serialNumber;
    
    private CertId(final ASN1Sequence asn1Sequence) {
        this.issuer = GeneralName.getInstance(asn1Sequence.getObjectAt(0));
        this.serialNumber = ASN1Integer.getInstance(asn1Sequence.getObjectAt(1));
    }
    
    public CertId(final GeneralName generalName, final BigInteger bigInteger) {
        this(generalName, new ASN1Integer(bigInteger));
    }
    
    public CertId(final GeneralName issuer, final ASN1Integer serialNumber) {
        this.issuer = issuer;
        this.serialNumber = serialNumber;
    }
    
    public static CertId getInstance(final Object o) {
        if (o instanceof CertId) {
            return (CertId)o;
        }
        if (o != null) {
            return new CertId(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public static CertId getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public GeneralName getIssuer() {
        return this.issuer;
    }
    
    public ASN1Integer getSerialNumber() {
        return this.serialNumber;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.issuer);
        asn1EncodableVector.add(this.serialNumber);
        return new DERSequence(asn1EncodableVector);
    }
}
