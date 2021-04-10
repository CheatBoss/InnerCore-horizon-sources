package org.spongycastle.asn1.isismtt.x509;

import org.spongycastle.asn1.x500.*;
import org.spongycastle.asn1.*;

public class Restriction extends ASN1Object
{
    private DirectoryString restriction;
    
    public Restriction(final String s) {
        this.restriction = new DirectoryString(s);
    }
    
    private Restriction(final DirectoryString restriction) {
        this.restriction = restriction;
    }
    
    public static Restriction getInstance(final Object o) {
        if (o instanceof Restriction) {
            return (Restriction)o;
        }
        if (o != null) {
            return new Restriction(DirectoryString.getInstance(o));
        }
        return null;
    }
    
    public DirectoryString getRestriction() {
        return this.restriction;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        return this.restriction.toASN1Primitive();
    }
}
