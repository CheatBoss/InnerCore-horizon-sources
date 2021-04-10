package org.spongycastle.asn1.x509;

import org.spongycastle.asn1.x500.*;
import java.math.*;
import org.spongycastle.asn1.*;

public class IssuerSerial extends ASN1Object
{
    GeneralNames issuer;
    DERBitString issuerUID;
    ASN1Integer serial;
    
    private IssuerSerial(final ASN1Sequence asn1Sequence) {
        if (asn1Sequence.size() != 2 && asn1Sequence.size() != 3) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Bad sequence size: ");
            sb.append(asn1Sequence.size());
            throw new IllegalArgumentException(sb.toString());
        }
        this.issuer = GeneralNames.getInstance(asn1Sequence.getObjectAt(0));
        this.serial = ASN1Integer.getInstance(asn1Sequence.getObjectAt(1));
        if (asn1Sequence.size() == 3) {
            this.issuerUID = DERBitString.getInstance(asn1Sequence.getObjectAt(2));
        }
    }
    
    public IssuerSerial(final X500Name x500Name, final BigInteger bigInteger) {
        this(new GeneralNames(new GeneralName(x500Name)), new ASN1Integer(bigInteger));
    }
    
    public IssuerSerial(final GeneralNames generalNames, final BigInteger bigInteger) {
        this(generalNames, new ASN1Integer(bigInteger));
    }
    
    public IssuerSerial(final GeneralNames issuer, final ASN1Integer serial) {
        this.issuer = issuer;
        this.serial = serial;
    }
    
    public static IssuerSerial getInstance(final Object o) {
        if (o instanceof IssuerSerial) {
            return (IssuerSerial)o;
        }
        if (o != null) {
            return new IssuerSerial(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public static IssuerSerial getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public GeneralNames getIssuer() {
        return this.issuer;
    }
    
    public DERBitString getIssuerUID() {
        return this.issuerUID;
    }
    
    public ASN1Integer getSerial() {
        return this.serial;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.issuer);
        asn1EncodableVector.add(this.serial);
        final DERBitString issuerUID = this.issuerUID;
        if (issuerUID != null) {
            asn1EncodableVector.add(issuerUID);
        }
        return new DERSequence(asn1EncodableVector);
    }
}
