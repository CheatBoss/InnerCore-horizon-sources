package org.spongycastle.asn1;

import org.spongycastle.util.*;
import java.io.*;

public class DERVisibleString extends ASN1Primitive implements ASN1String
{
    private final byte[] string;
    
    public DERVisibleString(final String s) {
        this.string = Strings.toByteArray(s);
    }
    
    DERVisibleString(final byte[] string) {
        this.string = string;
    }
    
    public static DERVisibleString getInstance(final Object o) {
        if (o != null && !(o instanceof DERVisibleString)) {
            if (o instanceof byte[]) {
                try {
                    return (DERVisibleString)ASN1Primitive.fromByteArray((byte[])o);
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
        return (DERVisibleString)o;
    }
    
    public static DERVisibleString getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        final ASN1Primitive object = asn1TaggedObject.getObject();
        if (!b && !(object instanceof DERVisibleString)) {
            return new DERVisibleString(ASN1OctetString.getInstance(object).getOctets());
        }
        return getInstance(object);
    }
    
    @Override
    boolean asn1Equals(final ASN1Primitive asn1Primitive) {
        return asn1Primitive instanceof DERVisibleString && Arrays.areEqual(this.string, ((DERVisibleString)asn1Primitive).string);
    }
    
    @Override
    void encode(final ASN1OutputStream asn1OutputStream) throws IOException {
        asn1OutputStream.writeEncoded(26, this.string);
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
    
    @Override
    public String toString() {
        return this.getString();
    }
}
