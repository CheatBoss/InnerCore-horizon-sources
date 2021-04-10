package org.spongycastle.asn1.pkcs;

import org.spongycastle.asn1.*;

public class AuthenticatedSafe extends ASN1Object
{
    private ContentInfo[] info;
    private boolean isBer;
    
    private AuthenticatedSafe(final ASN1Sequence asn1Sequence) {
        this.isBer = true;
        this.info = new ContentInfo[asn1Sequence.size()];
        int n = 0;
        while (true) {
            final ContentInfo[] info = this.info;
            if (n == info.length) {
                break;
            }
            info[n] = ContentInfo.getInstance(asn1Sequence.getObjectAt(n));
            ++n;
        }
        this.isBer = (asn1Sequence instanceof BERSequence);
    }
    
    public AuthenticatedSafe(final ContentInfo[] info) {
        this.isBer = true;
        this.info = info;
    }
    
    public static AuthenticatedSafe getInstance(final Object o) {
        if (o instanceof AuthenticatedSafe) {
            return (AuthenticatedSafe)o;
        }
        if (o != null) {
            return new AuthenticatedSafe(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public ContentInfo[] getContentInfo() {
        return this.info;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        int n = 0;
        while (true) {
            final ContentInfo[] info = this.info;
            if (n == info.length) {
                break;
            }
            asn1EncodableVector.add(info[n]);
            ++n;
        }
        if (this.isBer) {
            return new BERSequence(asn1EncodableVector);
        }
        return new DLSequence(asn1EncodableVector);
    }
}
