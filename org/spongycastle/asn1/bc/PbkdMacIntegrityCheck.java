package org.spongycastle.asn1.bc;

import org.spongycastle.asn1.x509.*;
import org.spongycastle.asn1.pkcs.*;
import org.spongycastle.util.*;
import org.spongycastle.asn1.*;

public class PbkdMacIntegrityCheck extends ASN1Object
{
    private final ASN1OctetString mac;
    private final AlgorithmIdentifier macAlgorithm;
    private final KeyDerivationFunc pbkdAlgorithm;
    
    private PbkdMacIntegrityCheck(final ASN1Sequence asn1Sequence) {
        this.macAlgorithm = AlgorithmIdentifier.getInstance(asn1Sequence.getObjectAt(0));
        this.pbkdAlgorithm = KeyDerivationFunc.getInstance(asn1Sequence.getObjectAt(1));
        this.mac = ASN1OctetString.getInstance(asn1Sequence.getObjectAt(2));
    }
    
    public PbkdMacIntegrityCheck(final AlgorithmIdentifier macAlgorithm, final KeyDerivationFunc pbkdAlgorithm, final byte[] array) {
        this.macAlgorithm = macAlgorithm;
        this.pbkdAlgorithm = pbkdAlgorithm;
        this.mac = new DEROctetString(Arrays.clone(array));
    }
    
    public static PbkdMacIntegrityCheck getInstance(final Object o) {
        if (o instanceof PbkdMacIntegrityCheck) {
            return (PbkdMacIntegrityCheck)o;
        }
        if (o != null) {
            return new PbkdMacIntegrityCheck(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public byte[] getMac() {
        return Arrays.clone(this.mac.getOctets());
    }
    
    public AlgorithmIdentifier getMacAlgorithm() {
        return this.macAlgorithm;
    }
    
    public KeyDerivationFunc getPbkdAlgorithm() {
        return this.pbkdAlgorithm;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.macAlgorithm);
        asn1EncodableVector.add(this.pbkdAlgorithm);
        asn1EncodableVector.add(this.mac);
        return new DERSequence(asn1EncodableVector);
    }
}
