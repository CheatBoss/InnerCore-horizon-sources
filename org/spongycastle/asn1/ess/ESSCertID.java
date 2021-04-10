package org.spongycastle.asn1.ess;

import org.spongycastle.asn1.x509.*;
import org.spongycastle.asn1.*;

public class ESSCertID extends ASN1Object
{
    private ASN1OctetString certHash;
    private IssuerSerial issuerSerial;
    
    private ESSCertID(final ASN1Sequence asn1Sequence) {
        if (asn1Sequence.size() >= 1 && asn1Sequence.size() <= 2) {
            this.certHash = ASN1OctetString.getInstance(asn1Sequence.getObjectAt(0));
            if (asn1Sequence.size() > 1) {
                this.issuerSerial = IssuerSerial.getInstance(asn1Sequence.getObjectAt(1));
            }
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Bad sequence size: ");
        sb.append(asn1Sequence.size());
        throw new IllegalArgumentException(sb.toString());
    }
    
    public ESSCertID(final byte[] array) {
        this.certHash = new DEROctetString(array);
    }
    
    public ESSCertID(final byte[] array, final IssuerSerial issuerSerial) {
        this.certHash = new DEROctetString(array);
        this.issuerSerial = issuerSerial;
    }
    
    public static ESSCertID getInstance(final Object o) {
        if (o instanceof ESSCertID) {
            return (ESSCertID)o;
        }
        if (o != null) {
            return new ESSCertID(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public byte[] getCertHash() {
        return this.certHash.getOctets();
    }
    
    public IssuerSerial getIssuerSerial() {
        return this.issuerSerial;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.certHash);
        final IssuerSerial issuerSerial = this.issuerSerial;
        if (issuerSerial != null) {
            asn1EncodableVector.add(issuerSerial);
        }
        return new DERSequence(asn1EncodableVector);
    }
}
