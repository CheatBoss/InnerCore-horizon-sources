package org.spongycastle.asn1.cmc;

import org.spongycastle.asn1.x509.*;
import org.spongycastle.asn1.*;

public class ExtensionReq extends ASN1Object
{
    private final Extension[] extensions;
    
    private ExtensionReq(final ASN1Sequence asn1Sequence) {
        this.extensions = new Extension[asn1Sequence.size()];
        for (int i = 0; i != asn1Sequence.size(); ++i) {
            this.extensions[i] = Extension.getInstance(asn1Sequence.getObjectAt(i));
        }
    }
    
    public ExtensionReq(final Extension extension) {
        this.extensions = new Extension[] { extension };
    }
    
    public ExtensionReq(final Extension[] array) {
        this.extensions = Utils.clone(array);
    }
    
    public static ExtensionReq getInstance(final Object o) {
        if (o instanceof ExtensionReq) {
            return (ExtensionReq)o;
        }
        if (o != null) {
            return new ExtensionReq(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public static ExtensionReq getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public Extension[] getExtensions() {
        return Utils.clone(this.extensions);
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        return new DERSequence(this.extensions);
    }
}
