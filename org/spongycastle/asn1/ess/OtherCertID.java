package org.spongycastle.asn1.ess;

import org.spongycastle.asn1.x509.*;
import org.spongycastle.asn1.oiw.*;
import org.spongycastle.asn1.*;

public class OtherCertID extends ASN1Object
{
    private IssuerSerial issuerSerial;
    private ASN1Encodable otherCertHash;
    
    private OtherCertID(final ASN1Sequence asn1Sequence) {
        if (asn1Sequence.size() >= 1 && asn1Sequence.size() <= 2) {
            ASN1Object otherCertHash;
            if (asn1Sequence.getObjectAt(0).toASN1Primitive() instanceof ASN1OctetString) {
                otherCertHash = ASN1OctetString.getInstance(asn1Sequence.getObjectAt(0));
            }
            else {
                otherCertHash = DigestInfo.getInstance(asn1Sequence.getObjectAt(0));
            }
            this.otherCertHash = otherCertHash;
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
    
    public OtherCertID(final AlgorithmIdentifier algorithmIdentifier, final byte[] array) {
        this.otherCertHash = new DigestInfo(algorithmIdentifier, array);
    }
    
    public OtherCertID(final AlgorithmIdentifier algorithmIdentifier, final byte[] array, final IssuerSerial issuerSerial) {
        this.otherCertHash = new DigestInfo(algorithmIdentifier, array);
        this.issuerSerial = issuerSerial;
    }
    
    public static OtherCertID getInstance(final Object o) {
        if (o instanceof OtherCertID) {
            return (OtherCertID)o;
        }
        if (o != null) {
            return new OtherCertID(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public AlgorithmIdentifier getAlgorithmHash() {
        if (this.otherCertHash.toASN1Primitive() instanceof ASN1OctetString) {
            return new AlgorithmIdentifier(OIWObjectIdentifiers.idSHA1);
        }
        return DigestInfo.getInstance(this.otherCertHash).getAlgorithmId();
    }
    
    public byte[] getCertHash() {
        if (this.otherCertHash.toASN1Primitive() instanceof ASN1OctetString) {
            return ((ASN1OctetString)this.otherCertHash.toASN1Primitive()).getOctets();
        }
        return DigestInfo.getInstance(this.otherCertHash).getDigest();
    }
    
    public IssuerSerial getIssuerSerial() {
        return this.issuerSerial;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.otherCertHash);
        final IssuerSerial issuerSerial = this.issuerSerial;
        if (issuerSerial != null) {
            asn1EncodableVector.add(issuerSerial);
        }
        return new DERSequence(asn1EncodableVector);
    }
}
