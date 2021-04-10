package org.spongycastle.asn1.mozilla;

import org.spongycastle.asn1.x509.*;
import org.spongycastle.asn1.*;

public class SignedPublicKeyAndChallenge extends ASN1Object
{
    private final ASN1Sequence pkacSeq;
    private final PublicKeyAndChallenge pubKeyAndChal;
    
    private SignedPublicKeyAndChallenge(final ASN1Sequence pkacSeq) {
        this.pkacSeq = pkacSeq;
        this.pubKeyAndChal = PublicKeyAndChallenge.getInstance(pkacSeq.getObjectAt(0));
    }
    
    public static SignedPublicKeyAndChallenge getInstance(final Object o) {
        if (o instanceof SignedPublicKeyAndChallenge) {
            return (SignedPublicKeyAndChallenge)o;
        }
        if (o != null) {
            return new SignedPublicKeyAndChallenge(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public PublicKeyAndChallenge getPublicKeyAndChallenge() {
        return this.pubKeyAndChal;
    }
    
    public DERBitString getSignature() {
        return DERBitString.getInstance(this.pkacSeq.getObjectAt(2));
    }
    
    public AlgorithmIdentifier getSignatureAlgorithm() {
        return AlgorithmIdentifier.getInstance(this.pkacSeq.getObjectAt(1));
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        return this.pkacSeq;
    }
}
