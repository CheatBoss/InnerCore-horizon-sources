package org.spongycastle.asn1.mozilla;

import org.spongycastle.asn1.x509.*;
import org.spongycastle.asn1.*;

public class PublicKeyAndChallenge extends ASN1Object
{
    private DERIA5String challenge;
    private ASN1Sequence pkacSeq;
    private SubjectPublicKeyInfo spki;
    
    private PublicKeyAndChallenge(final ASN1Sequence pkacSeq) {
        this.pkacSeq = pkacSeq;
        this.spki = SubjectPublicKeyInfo.getInstance(pkacSeq.getObjectAt(0));
        this.challenge = DERIA5String.getInstance(pkacSeq.getObjectAt(1));
    }
    
    public static PublicKeyAndChallenge getInstance(final Object o) {
        if (o instanceof PublicKeyAndChallenge) {
            return (PublicKeyAndChallenge)o;
        }
        if (o != null) {
            return new PublicKeyAndChallenge(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public DERIA5String getChallenge() {
        return this.challenge;
    }
    
    public SubjectPublicKeyInfo getSubjectPublicKeyInfo() {
        return this.spki;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        return this.pkacSeq;
    }
}
