package org.spongycastle.asn1.x500;

import org.spongycastle.asn1.*;

public class DirectoryString extends ASN1Object implements ASN1Choice, ASN1String
{
    private ASN1String string;
    
    public DirectoryString(final String s) {
        this.string = new DERUTF8String(s);
    }
    
    private DirectoryString(final DERBMPString string) {
        this.string = string;
    }
    
    private DirectoryString(final DERPrintableString string) {
        this.string = string;
    }
    
    private DirectoryString(final DERT61String string) {
        this.string = string;
    }
    
    private DirectoryString(final DERUTF8String string) {
        this.string = string;
    }
    
    private DirectoryString(final DERUniversalString string) {
        this.string = string;
    }
    
    public static DirectoryString getInstance(final Object o) {
        if (o == null || o instanceof DirectoryString) {
            return (DirectoryString)o;
        }
        if (o instanceof DERT61String) {
            return new DirectoryString((DERT61String)o);
        }
        if (o instanceof DERPrintableString) {
            return new DirectoryString((DERPrintableString)o);
        }
        if (o instanceof DERUniversalString) {
            return new DirectoryString((DERUniversalString)o);
        }
        if (o instanceof DERUTF8String) {
            return new DirectoryString((DERUTF8String)o);
        }
        if (o instanceof DERBMPString) {
            return new DirectoryString((DERBMPString)o);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("illegal object in getInstance: ");
        sb.append(o.getClass().getName());
        throw new IllegalArgumentException(sb.toString());
    }
    
    public static DirectoryString getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        if (b) {
            return getInstance(asn1TaggedObject.getObject());
        }
        throw new IllegalArgumentException("choice item must be explicitly tagged");
    }
    
    @Override
    public String getString() {
        return this.string.getString();
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        return ((ASN1Encodable)this.string).toASN1Primitive();
    }
    
    @Override
    public String toString() {
        return this.string.getString();
    }
}
