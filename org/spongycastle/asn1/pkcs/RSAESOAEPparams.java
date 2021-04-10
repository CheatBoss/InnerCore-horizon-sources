package org.spongycastle.asn1.pkcs;

import org.spongycastle.asn1.x509.*;
import org.spongycastle.asn1.oiw.*;
import org.spongycastle.asn1.*;

public class RSAESOAEPparams extends ASN1Object
{
    public static final AlgorithmIdentifier DEFAULT_HASH_ALGORITHM;
    public static final AlgorithmIdentifier DEFAULT_MASK_GEN_FUNCTION;
    public static final AlgorithmIdentifier DEFAULT_P_SOURCE_ALGORITHM;
    private AlgorithmIdentifier hashAlgorithm;
    private AlgorithmIdentifier maskGenAlgorithm;
    private AlgorithmIdentifier pSourceAlgorithm;
    
    static {
        DEFAULT_HASH_ALGORITHM = new AlgorithmIdentifier(OIWObjectIdentifiers.idSHA1, DERNull.INSTANCE);
        DEFAULT_MASK_GEN_FUNCTION = new AlgorithmIdentifier(PKCSObjectIdentifiers.id_mgf1, RSAESOAEPparams.DEFAULT_HASH_ALGORITHM);
        DEFAULT_P_SOURCE_ALGORITHM = new AlgorithmIdentifier(PKCSObjectIdentifiers.id_pSpecified, new DEROctetString(new byte[0]));
    }
    
    public RSAESOAEPparams() {
        this.hashAlgorithm = RSAESOAEPparams.DEFAULT_HASH_ALGORITHM;
        this.maskGenAlgorithm = RSAESOAEPparams.DEFAULT_MASK_GEN_FUNCTION;
        this.pSourceAlgorithm = RSAESOAEPparams.DEFAULT_P_SOURCE_ALGORITHM;
    }
    
    public RSAESOAEPparams(final ASN1Sequence asn1Sequence) {
        this.hashAlgorithm = RSAESOAEPparams.DEFAULT_HASH_ALGORITHM;
        this.maskGenAlgorithm = RSAESOAEPparams.DEFAULT_MASK_GEN_FUNCTION;
        this.pSourceAlgorithm = RSAESOAEPparams.DEFAULT_P_SOURCE_ALGORITHM;
        for (int i = 0; i != asn1Sequence.size(); ++i) {
            final ASN1TaggedObject asn1TaggedObject = (ASN1TaggedObject)asn1Sequence.getObjectAt(i);
            final int tagNo = asn1TaggedObject.getTagNo();
            if (tagNo != 0) {
                if (tagNo != 1) {
                    if (tagNo != 2) {
                        throw new IllegalArgumentException("unknown tag");
                    }
                    this.pSourceAlgorithm = AlgorithmIdentifier.getInstance(asn1TaggedObject, true);
                }
                else {
                    this.maskGenAlgorithm = AlgorithmIdentifier.getInstance(asn1TaggedObject, true);
                }
            }
            else {
                this.hashAlgorithm = AlgorithmIdentifier.getInstance(asn1TaggedObject, true);
            }
        }
    }
    
    public RSAESOAEPparams(final AlgorithmIdentifier hashAlgorithm, final AlgorithmIdentifier maskGenAlgorithm, final AlgorithmIdentifier pSourceAlgorithm) {
        this.hashAlgorithm = hashAlgorithm;
        this.maskGenAlgorithm = maskGenAlgorithm;
        this.pSourceAlgorithm = pSourceAlgorithm;
    }
    
    public static RSAESOAEPparams getInstance(final Object o) {
        if (o instanceof RSAESOAEPparams) {
            return (RSAESOAEPparams)o;
        }
        if (o != null) {
            return new RSAESOAEPparams(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public AlgorithmIdentifier getHashAlgorithm() {
        return this.hashAlgorithm;
    }
    
    public AlgorithmIdentifier getMaskGenAlgorithm() {
        return this.maskGenAlgorithm;
    }
    
    public AlgorithmIdentifier getPSourceAlgorithm() {
        return this.pSourceAlgorithm;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        if (!this.hashAlgorithm.equals(RSAESOAEPparams.DEFAULT_HASH_ALGORITHM)) {
            asn1EncodableVector.add(new DERTaggedObject(true, 0, this.hashAlgorithm));
        }
        if (!this.maskGenAlgorithm.equals(RSAESOAEPparams.DEFAULT_MASK_GEN_FUNCTION)) {
            asn1EncodableVector.add(new DERTaggedObject(true, 1, this.maskGenAlgorithm));
        }
        if (!this.pSourceAlgorithm.equals(RSAESOAEPparams.DEFAULT_P_SOURCE_ALGORITHM)) {
            asn1EncodableVector.add(new DERTaggedObject(true, 2, this.pSourceAlgorithm));
        }
        return new DERSequence(asn1EncodableVector);
    }
}
