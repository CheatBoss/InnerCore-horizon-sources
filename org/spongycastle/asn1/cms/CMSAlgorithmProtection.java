package org.spongycastle.asn1.cms;

import org.spongycastle.asn1.x509.*;
import org.spongycastle.asn1.*;

public class CMSAlgorithmProtection extends ASN1Object
{
    public static final int MAC = 2;
    public static final int SIGNATURE = 1;
    private final AlgorithmIdentifier digestAlgorithm;
    private final AlgorithmIdentifier macAlgorithm;
    private final AlgorithmIdentifier signatureAlgorithm;
    
    private CMSAlgorithmProtection(final ASN1Sequence asn1Sequence) {
        if (asn1Sequence.size() != 2) {
            throw new IllegalArgumentException("Sequence wrong size: One of signatureAlgorithm or macAlgorithm must be present");
        }
        this.digestAlgorithm = AlgorithmIdentifier.getInstance(asn1Sequence.getObjectAt(0));
        final ASN1TaggedObject instance = ASN1TaggedObject.getInstance(asn1Sequence.getObjectAt(1));
        if (instance.getTagNo() == 1) {
            this.signatureAlgorithm = AlgorithmIdentifier.getInstance(instance, false);
            this.macAlgorithm = null;
            return;
        }
        if (instance.getTagNo() == 2) {
            this.signatureAlgorithm = null;
            this.macAlgorithm = AlgorithmIdentifier.getInstance(instance, false);
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Unknown tag found: ");
        sb.append(instance.getTagNo());
        throw new IllegalArgumentException(sb.toString());
    }
    
    public CMSAlgorithmProtection(final AlgorithmIdentifier digestAlgorithm, final int n, final AlgorithmIdentifier algorithmIdentifier) {
        if (digestAlgorithm == null || algorithmIdentifier == null) {
            throw new NullPointerException("AlgorithmIdentifiers cannot be null");
        }
        this.digestAlgorithm = digestAlgorithm;
        if (n == 1) {
            this.signatureAlgorithm = algorithmIdentifier;
            this.macAlgorithm = null;
            return;
        }
        if (n == 2) {
            this.signatureAlgorithm = null;
            this.macAlgorithm = algorithmIdentifier;
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Unknown type: ");
        sb.append(n);
        throw new IllegalArgumentException(sb.toString());
    }
    
    public static CMSAlgorithmProtection getInstance(final Object o) {
        if (o instanceof CMSAlgorithmProtection) {
            return (CMSAlgorithmProtection)o;
        }
        if (o != null) {
            return new CMSAlgorithmProtection(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public AlgorithmIdentifier getDigestAlgorithm() {
        return this.digestAlgorithm;
    }
    
    public AlgorithmIdentifier getMacAlgorithm() {
        return this.macAlgorithm;
    }
    
    public AlgorithmIdentifier getSignatureAlgorithm() {
        return this.signatureAlgorithm;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.digestAlgorithm);
        if (this.signatureAlgorithm != null) {
            asn1EncodableVector.add(new DERTaggedObject(false, 1, this.signatureAlgorithm));
        }
        if (this.macAlgorithm != null) {
            asn1EncodableVector.add(new DERTaggedObject(false, 2, this.macAlgorithm));
        }
        return new DERSequence(asn1EncodableVector);
    }
}
