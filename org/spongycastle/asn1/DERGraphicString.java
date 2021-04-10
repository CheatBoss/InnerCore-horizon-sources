package org.spongycastle.asn1;

import java.io.*;
import org.spongycastle.util.*;

public class DERGraphicString extends ASN1Primitive implements ASN1String
{
    private final byte[] string;
    
    public DERGraphicString(final byte[] array) {
        this.string = Arrays.clone(array);
    }
    
    public static DERGraphicString getInstance(final Object o) {
        if (o != null && !(o instanceof DERGraphicString)) {
            if (o instanceof byte[]) {
                try {
                    return (DERGraphicString)ASN1Primitive.fromByteArray((byte[])o);
                }
                catch (Exception ex) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("encoding error in getInstance: ");
                    sb.append(ex.toString());
                    throw new IllegalArgumentException(sb.toString());
                }
            }
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("illegal object in getInstance: ");
            sb2.append(o.getClass().getName());
            throw new IllegalArgumentException(sb2.toString());
        }
        return (DERGraphicString)o;
    }
    
    public static DERGraphicString getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        final ASN1Primitive object = asn1TaggedObject.getObject();
        if (!b && !(object instanceof DERGraphicString)) {
            return new DERGraphicString(((ASN1OctetString)object).getOctets());
        }
        return getInstance(object);
    }
    
    @Override
    boolean asn1Equals(final ASN1Primitive asn1Primitive) {
        return asn1Primitive instanceof DERGraphicString && Arrays.areEqual(this.string, ((DERGraphicString)asn1Primitive).string);
    }
    
    @Override
    void encode(final ASN1OutputStream asn1OutputStream) throws IOException {
        asn1OutputStream.writeEncoded(25, this.string);
    }
    
    @Override
    int encodedLength() {
        return StreamUtil.calculateBodyLength(this.string.length) + 1 + this.string.length;
    }
    
    public byte[] getOctets() {
        return Arrays.clone(this.string);
    }
    
    @Override
    public String getString() {
        return Strings.fromByteArray(this.string);
    }
    
    @Override
    public int hashCode() {
        return Arrays.hashCode(this.string);
    }
    
    @Override
    boolean isConstructed() {
        return false;
    }
}
