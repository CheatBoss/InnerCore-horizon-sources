package org.spongycastle.asn1.ess;

import org.spongycastle.asn1.x509.*;
import org.spongycastle.asn1.nist.*;
import org.spongycastle.util.*;
import org.spongycastle.asn1.*;

public class ESSCertIDv2 extends ASN1Object
{
    private static final AlgorithmIdentifier DEFAULT_ALG_ID;
    private byte[] certHash;
    private AlgorithmIdentifier hashAlgorithm;
    private IssuerSerial issuerSerial;
    
    static {
        DEFAULT_ALG_ID = new AlgorithmIdentifier(NISTObjectIdentifiers.id_sha256);
    }
    
    private ESSCertIDv2(final ASN1Sequence asn1Sequence) {
        if (asn1Sequence.size() <= 3) {
            int n = 0;
            if (asn1Sequence.getObjectAt(0) instanceof ASN1OctetString) {
                this.hashAlgorithm = ESSCertIDv2.DEFAULT_ALG_ID;
            }
            else {
                this.hashAlgorithm = AlgorithmIdentifier.getInstance(asn1Sequence.getObjectAt(0).toASN1Primitive());
                n = 1;
            }
            final int n2 = n + 1;
            this.certHash = ASN1OctetString.getInstance(asn1Sequence.getObjectAt(n).toASN1Primitive()).getOctets();
            if (asn1Sequence.size() > n2) {
                this.issuerSerial = IssuerSerial.getInstance(asn1Sequence.getObjectAt(n2));
            }
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Bad sequence size: ");
        sb.append(asn1Sequence.size());
        throw new IllegalArgumentException(sb.toString());
    }
    
    public ESSCertIDv2(final AlgorithmIdentifier algorithmIdentifier, final byte[] array) {
        this(algorithmIdentifier, array, null);
    }
    
    public ESSCertIDv2(final AlgorithmIdentifier algorithmIdentifier, final byte[] array, final IssuerSerial issuerSerial) {
        AlgorithmIdentifier default_ALG_ID = algorithmIdentifier;
        if (algorithmIdentifier == null) {
            default_ALG_ID = ESSCertIDv2.DEFAULT_ALG_ID;
        }
        this.hashAlgorithm = default_ALG_ID;
        this.certHash = Arrays.clone(array);
        this.issuerSerial = issuerSerial;
    }
    
    public ESSCertIDv2(final byte[] array) {
        this(null, array, null);
    }
    
    public ESSCertIDv2(final byte[] array, final IssuerSerial issuerSerial) {
        this(null, array, issuerSerial);
    }
    
    public static ESSCertIDv2 getInstance(final Object o) {
        if (o instanceof ESSCertIDv2) {
            return (ESSCertIDv2)o;
        }
        if (o != null) {
            return new ESSCertIDv2(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public byte[] getCertHash() {
        return Arrays.clone(this.certHash);
    }
    
    public AlgorithmIdentifier getHashAlgorithm() {
        return this.hashAlgorithm;
    }
    
    public IssuerSerial getIssuerSerial() {
        return this.issuerSerial;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        if (!this.hashAlgorithm.equals(ESSCertIDv2.DEFAULT_ALG_ID)) {
            asn1EncodableVector.add(this.hashAlgorithm);
        }
        asn1EncodableVector.add(new DEROctetString(this.certHash).toASN1Primitive());
        final IssuerSerial issuerSerial = this.issuerSerial;
        if (issuerSerial != null) {
            asn1EncodableVector.add(issuerSerial);
        }
        return new DERSequence(asn1EncodableVector);
    }
}
