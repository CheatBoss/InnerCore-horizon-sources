package org.spongycastle.asn1.cmp;

import org.spongycastle.asn1.*;

public class POPODecKeyChallContent extends ASN1Object
{
    private ASN1Sequence content;
    
    private POPODecKeyChallContent(final ASN1Sequence content) {
        this.content = content;
    }
    
    public static POPODecKeyChallContent getInstance(final Object o) {
        if (o instanceof POPODecKeyChallContent) {
            return (POPODecKeyChallContent)o;
        }
        if (o != null) {
            return new POPODecKeyChallContent(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        return this.content;
    }
    
    public Challenge[] toChallengeArray() {
        final int size = this.content.size();
        final Challenge[] array = new Challenge[size];
        for (int i = 0; i != size; ++i) {
            array[i] = Challenge.getInstance(this.content.getObjectAt(i));
        }
        return array;
    }
}
