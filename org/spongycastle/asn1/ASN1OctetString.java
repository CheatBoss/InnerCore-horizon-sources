package org.spongycastle.asn1;

import java.io.*;
import org.spongycastle.util.encoders.*;
import org.spongycastle.util.*;

public abstract class ASN1OctetString extends ASN1Primitive implements ASN1OctetStringParser
{
    byte[] string;
    
    public ASN1OctetString(final byte[] string) {
        if (string != null) {
            this.string = string;
            return;
        }
        throw new NullPointerException("string cannot be null");
    }
    
    public static ASN1OctetString getInstance(final Object o) {
        if (o != null && !(o instanceof ASN1OctetString)) {
            if (o instanceof byte[]) {
                try {
                    return getInstance(ASN1Primitive.fromByteArray((byte[])o));
                }
                catch (IOException ex) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("failed to construct OCTET STRING from byte[]: ");
                    sb.append(ex.getMessage());
                    throw new IllegalArgumentException(sb.toString());
                }
            }
            if (o instanceof ASN1Encodable) {
                final ASN1Primitive asn1Primitive = ((ASN1Encodable)o).toASN1Primitive();
                if (asn1Primitive instanceof ASN1OctetString) {
                    return (ASN1OctetString)asn1Primitive;
                }
            }
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("illegal object in getInstance: ");
            sb2.append(o.getClass().getName());
            throw new IllegalArgumentException(sb2.toString());
        }
        return (ASN1OctetString)o;
    }
    
    public static ASN1OctetString getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        final ASN1Primitive object = asn1TaggedObject.getObject();
        if (!b && !(object instanceof ASN1OctetString)) {
            return BEROctetString.fromSequence(ASN1Sequence.getInstance(object));
        }
        return getInstance(object);
    }
    
    @Override
    boolean asn1Equals(final ASN1Primitive asn1Primitive) {
        return asn1Primitive instanceof ASN1OctetString && Arrays.areEqual(this.string, ((ASN1OctetString)asn1Primitive).string);
    }
    
    @Override
    abstract void encode(final ASN1OutputStream p0) throws IOException;
    
    @Override
    public ASN1Primitive getLoadedObject() {
        return this.toASN1Primitive();
    }
    
    @Override
    public InputStream getOctetStream() {
        return new ByteArrayInputStream(this.string);
    }
    
    public byte[] getOctets() {
        return this.string;
    }
    
    @Override
    public int hashCode() {
        return Arrays.hashCode(this.getOctets());
    }
    
    public ASN1OctetStringParser parser() {
        return this;
    }
    
    @Override
    ASN1Primitive toDERObject() {
        return new DEROctetString(this.string);
    }
    
    @Override
    ASN1Primitive toDLObject() {
        return new DEROctetString(this.string);
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("#");
        sb.append(Strings.fromByteArray(Hex.encode(this.string)));
        return sb.toString();
    }
}
