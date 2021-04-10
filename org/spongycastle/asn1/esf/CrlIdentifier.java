package org.spongycastle.asn1.esf;

import org.spongycastle.asn1.x500.*;
import java.math.*;
import org.spongycastle.asn1.*;

public class CrlIdentifier extends ASN1Object
{
    private ASN1UTCTime crlIssuedTime;
    private X500Name crlIssuer;
    private ASN1Integer crlNumber;
    
    private CrlIdentifier(final ASN1Sequence asn1Sequence) {
        if (asn1Sequence.size() >= 2 && asn1Sequence.size() <= 3) {
            this.crlIssuer = X500Name.getInstance(asn1Sequence.getObjectAt(0));
            this.crlIssuedTime = ASN1UTCTime.getInstance(asn1Sequence.getObjectAt(1));
            if (asn1Sequence.size() > 2) {
                this.crlNumber = ASN1Integer.getInstance(asn1Sequence.getObjectAt(2));
            }
            return;
        }
        throw new IllegalArgumentException();
    }
    
    public CrlIdentifier(final X500Name x500Name, final ASN1UTCTime asn1UTCTime) {
        this(x500Name, asn1UTCTime, null);
    }
    
    public CrlIdentifier(final X500Name crlIssuer, final ASN1UTCTime crlIssuedTime, final BigInteger bigInteger) {
        this.crlIssuer = crlIssuer;
        this.crlIssuedTime = crlIssuedTime;
        if (bigInteger != null) {
            this.crlNumber = new ASN1Integer(bigInteger);
        }
    }
    
    public static CrlIdentifier getInstance(final Object o) {
        if (o instanceof CrlIdentifier) {
            return (CrlIdentifier)o;
        }
        if (o != null) {
            return new CrlIdentifier(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public ASN1UTCTime getCrlIssuedTime() {
        return this.crlIssuedTime;
    }
    
    public X500Name getCrlIssuer() {
        return this.crlIssuer;
    }
    
    public BigInteger getCrlNumber() {
        final ASN1Integer crlNumber = this.crlNumber;
        if (crlNumber == null) {
            return null;
        }
        return crlNumber.getValue();
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.crlIssuer.toASN1Primitive());
        asn1EncodableVector.add(this.crlIssuedTime);
        final ASN1Integer crlNumber = this.crlNumber;
        if (crlNumber != null) {
            asn1EncodableVector.add(crlNumber);
        }
        return new DERSequence(asn1EncodableVector);
    }
}
