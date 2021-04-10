package org.spongycastle.asn1.pkcs;

import org.spongycastle.asn1.x509.*;
import org.spongycastle.asn1.oiw.*;
import java.math.*;
import org.spongycastle.asn1.*;

public class RSASSAPSSparams extends ASN1Object
{
    public static final AlgorithmIdentifier DEFAULT_HASH_ALGORITHM;
    public static final AlgorithmIdentifier DEFAULT_MASK_GEN_FUNCTION;
    public static final ASN1Integer DEFAULT_SALT_LENGTH;
    public static final ASN1Integer DEFAULT_TRAILER_FIELD;
    private AlgorithmIdentifier hashAlgorithm;
    private AlgorithmIdentifier maskGenAlgorithm;
    private ASN1Integer saltLength;
    private ASN1Integer trailerField;
    
    static {
        DEFAULT_HASH_ALGORITHM = new AlgorithmIdentifier(OIWObjectIdentifiers.idSHA1, DERNull.INSTANCE);
        DEFAULT_MASK_GEN_FUNCTION = new AlgorithmIdentifier(PKCSObjectIdentifiers.id_mgf1, RSASSAPSSparams.DEFAULT_HASH_ALGORITHM);
        DEFAULT_SALT_LENGTH = new ASN1Integer(20L);
        DEFAULT_TRAILER_FIELD = new ASN1Integer(1L);
    }
    
    public RSASSAPSSparams() {
        this.hashAlgorithm = RSASSAPSSparams.DEFAULT_HASH_ALGORITHM;
        this.maskGenAlgorithm = RSASSAPSSparams.DEFAULT_MASK_GEN_FUNCTION;
        this.saltLength = RSASSAPSSparams.DEFAULT_SALT_LENGTH;
        this.trailerField = RSASSAPSSparams.DEFAULT_TRAILER_FIELD;
    }
    
    private RSASSAPSSparams(final ASN1Sequence asn1Sequence) {
        this.hashAlgorithm = RSASSAPSSparams.DEFAULT_HASH_ALGORITHM;
        this.maskGenAlgorithm = RSASSAPSSparams.DEFAULT_MASK_GEN_FUNCTION;
        this.saltLength = RSASSAPSSparams.DEFAULT_SALT_LENGTH;
        this.trailerField = RSASSAPSSparams.DEFAULT_TRAILER_FIELD;
        for (int i = 0; i != asn1Sequence.size(); ++i) {
            final ASN1TaggedObject asn1TaggedObject = (ASN1TaggedObject)asn1Sequence.getObjectAt(i);
            final int tagNo = asn1TaggedObject.getTagNo();
            if (tagNo != 0) {
                if (tagNo != 1) {
                    if (tagNo != 2) {
                        if (tagNo != 3) {
                            throw new IllegalArgumentException("unknown tag");
                        }
                        this.trailerField = ASN1Integer.getInstance(asn1TaggedObject, true);
                    }
                    else {
                        this.saltLength = ASN1Integer.getInstance(asn1TaggedObject, true);
                    }
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
    
    public RSASSAPSSparams(final AlgorithmIdentifier hashAlgorithm, final AlgorithmIdentifier maskGenAlgorithm, final ASN1Integer saltLength, final ASN1Integer trailerField) {
        this.hashAlgorithm = hashAlgorithm;
        this.maskGenAlgorithm = maskGenAlgorithm;
        this.saltLength = saltLength;
        this.trailerField = trailerField;
    }
    
    public static RSASSAPSSparams getInstance(final Object o) {
        if (o instanceof RSASSAPSSparams) {
            return (RSASSAPSSparams)o;
        }
        if (o != null) {
            return new RSASSAPSSparams(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public AlgorithmIdentifier getHashAlgorithm() {
        return this.hashAlgorithm;
    }
    
    public AlgorithmIdentifier getMaskGenAlgorithm() {
        return this.maskGenAlgorithm;
    }
    
    public BigInteger getSaltLength() {
        return this.saltLength.getValue();
    }
    
    public BigInteger getTrailerField() {
        return this.trailerField.getValue();
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        if (!this.hashAlgorithm.equals(RSASSAPSSparams.DEFAULT_HASH_ALGORITHM)) {
            asn1EncodableVector.add(new DERTaggedObject(true, 0, this.hashAlgorithm));
        }
        if (!this.maskGenAlgorithm.equals(RSASSAPSSparams.DEFAULT_MASK_GEN_FUNCTION)) {
            asn1EncodableVector.add(new DERTaggedObject(true, 1, this.maskGenAlgorithm));
        }
        if (!this.saltLength.equals(RSASSAPSSparams.DEFAULT_SALT_LENGTH)) {
            asn1EncodableVector.add(new DERTaggedObject(true, 2, this.saltLength));
        }
        if (!this.trailerField.equals(RSASSAPSSparams.DEFAULT_TRAILER_FIELD)) {
            asn1EncodableVector.add(new DERTaggedObject(true, 3, this.trailerField));
        }
        return new DERSequence(asn1EncodableVector);
    }
}
