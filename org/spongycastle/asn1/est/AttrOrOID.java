package org.spongycastle.asn1.est;

import org.spongycastle.asn1.pkcs.*;
import org.spongycastle.asn1.*;
import java.io.*;

public class AttrOrOID extends ASN1Object implements ASN1Choice
{
    private final Attribute attribute;
    private final ASN1ObjectIdentifier oid;
    
    public AttrOrOID(final ASN1ObjectIdentifier oid) {
        this.oid = oid;
        this.attribute = null;
    }
    
    public AttrOrOID(final Attribute attribute) {
        this.oid = null;
        this.attribute = attribute;
    }
    
    public static AttrOrOID getInstance(final Object o) {
        if (o instanceof AttrOrOID) {
            return (AttrOrOID)o;
        }
        if (o != null) {
            if (o instanceof ASN1Encodable) {
                final ASN1Primitive asn1Primitive = ((ASN1Encodable)o).toASN1Primitive();
                if (asn1Primitive instanceof ASN1ObjectIdentifier) {
                    return new AttrOrOID(ASN1ObjectIdentifier.getInstance(asn1Primitive));
                }
                if (asn1Primitive instanceof ASN1Sequence) {
                    return new AttrOrOID(Attribute.getInstance(asn1Primitive));
                }
            }
            if (o instanceof byte[]) {
                try {
                    return getInstance(ASN1Primitive.fromByteArray((byte[])o));
                }
                catch (IOException ex) {
                    throw new IllegalArgumentException("unknown encoding in getInstance()");
                }
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("unknown object in getInstance(): ");
            sb.append(o.getClass().getName());
            throw new IllegalArgumentException(sb.toString());
        }
        return null;
    }
    
    public Attribute getAttribute() {
        return this.attribute;
    }
    
    public ASN1ObjectIdentifier getOid() {
        return this.oid;
    }
    
    public boolean isOid() {
        return this.oid != null;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1ObjectIdentifier oid = this.oid;
        if (oid != null) {
            return oid;
        }
        return this.attribute.toASN1Primitive();
    }
}
