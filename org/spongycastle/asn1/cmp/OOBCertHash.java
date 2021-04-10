package org.spongycastle.asn1.cmp;

import org.spongycastle.asn1.crmf.*;
import org.spongycastle.asn1.x509.*;
import org.spongycastle.asn1.*;

public class OOBCertHash extends ASN1Object
{
    private CertId certId;
    private AlgorithmIdentifier hashAlg;
    private DERBitString hashVal;
    
    private OOBCertHash(final ASN1Sequence asn1Sequence) {
        final int n = asn1Sequence.size() - 1;
        int i = n - 1;
        this.hashVal = DERBitString.getInstance(asn1Sequence.getObjectAt(n));
        while (i >= 0) {
            final ASN1TaggedObject asn1TaggedObject = (ASN1TaggedObject)asn1Sequence.getObjectAt(i);
            if (asn1TaggedObject.getTagNo() == 0) {
                this.hashAlg = AlgorithmIdentifier.getInstance(asn1TaggedObject, true);
            }
            else {
                this.certId = CertId.getInstance(asn1TaggedObject, true);
            }
            --i;
        }
    }
    
    public OOBCertHash(final AlgorithmIdentifier hashAlg, final CertId certId, final DERBitString hashVal) {
        this.hashAlg = hashAlg;
        this.certId = certId;
        this.hashVal = hashVal;
    }
    
    public OOBCertHash(final AlgorithmIdentifier algorithmIdentifier, final CertId certId, final byte[] array) {
        this(algorithmIdentifier, certId, new DERBitString(array));
    }
    
    private void addOptional(final ASN1EncodableVector asn1EncodableVector, final int n, final ASN1Encodable asn1Encodable) {
        if (asn1Encodable != null) {
            asn1EncodableVector.add(new DERTaggedObject(true, n, asn1Encodable));
        }
    }
    
    public static OOBCertHash getInstance(final Object o) {
        if (o instanceof OOBCertHash) {
            return (OOBCertHash)o;
        }
        if (o != null) {
            return new OOBCertHash(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public CertId getCertId() {
        return this.certId;
    }
    
    public AlgorithmIdentifier getHashAlg() {
        return this.hashAlg;
    }
    
    public DERBitString getHashVal() {
        return this.hashVal;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        this.addOptional(asn1EncodableVector, 0, this.hashAlg);
        this.addOptional(asn1EncodableVector, 1, this.certId);
        asn1EncodableVector.add(this.hashVal);
        return new DERSequence(asn1EncodableVector);
    }
}
