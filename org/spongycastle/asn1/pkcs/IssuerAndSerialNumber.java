package org.spongycastle.asn1.pkcs;

import org.spongycastle.asn1.x500.*;
import java.math.*;
import org.spongycastle.asn1.x509.*;
import org.spongycastle.asn1.*;

public class IssuerAndSerialNumber extends ASN1Object
{
    ASN1Integer certSerialNumber;
    X500Name name;
    
    private IssuerAndSerialNumber(final ASN1Sequence asn1Sequence) {
        this.name = X500Name.getInstance(asn1Sequence.getObjectAt(0));
        this.certSerialNumber = (ASN1Integer)asn1Sequence.getObjectAt(1);
    }
    
    public IssuerAndSerialNumber(final X500Name name, final BigInteger bigInteger) {
        this.name = name;
        this.certSerialNumber = new ASN1Integer(bigInteger);
    }
    
    public IssuerAndSerialNumber(final X509Name x509Name, final BigInteger bigInteger) {
        this.name = X500Name.getInstance(x509Name.toASN1Primitive());
        this.certSerialNumber = new ASN1Integer(bigInteger);
    }
    
    public IssuerAndSerialNumber(final X509Name x509Name, final ASN1Integer certSerialNumber) {
        this.name = X500Name.getInstance(x509Name.toASN1Primitive());
        this.certSerialNumber = certSerialNumber;
    }
    
    public static IssuerAndSerialNumber getInstance(final Object o) {
        if (o instanceof IssuerAndSerialNumber) {
            return (IssuerAndSerialNumber)o;
        }
        if (o != null) {
            return new IssuerAndSerialNumber(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public ASN1Integer getCertificateSerialNumber() {
        return this.certSerialNumber;
    }
    
    public X500Name getName() {
        return this.name;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.name);
        asn1EncodableVector.add(this.certSerialNumber);
        return new DERSequence(asn1EncodableVector);
    }
}
