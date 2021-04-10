package org.spongycastle.asn1;

import org.spongycastle.util.*;
import java.io.*;

public class DERUTF8String extends ASN1Primitive implements ASN1String
{
    private final byte[] string;
    
    public DERUTF8String(final String s) {
        this.string = Strings.toUTF8ByteArray(s);
    }
    
    DERUTF8String(final byte[] string) {
        this.string = string;
    }
    
    public static DERUTF8String getInstance(final Object o) {
        if (o != null && !(o instanceof DERUTF8String)) {
            if (o instanceof byte[]) {
                try {
                    return (DERUTF8String)ASN1Primitive.fromByteArray((byte[])o);
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
        return (DERUTF8String)o;
    }
    
    public static DERUTF8String getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        final ASN1Primitive object = asn1TaggedObject.getObject();
        if (!b && !(object instanceof DERUTF8String)) {
            return new DERUTF8String(ASN1OctetString.getInstance(object).getOctets());
        }
        return getInstance(object);
    }
    
    @Override
    boolean asn1Equals(final ASN1Primitive asn1Primitive) {
        return asn1Primitive instanceof DERUTF8String && Arrays.areEqual(this.string, ((DERUTF8String)asn1Primitive).string);
    }
    
    @Override
    void encode(final ASN1OutputStream asn1OutputStream) throws IOException {
        asn1OutputStream.writeEncoded(12, this.string);
    }
    
    @Override
    int encodedLength() throws IOException {
        return StreamUtil.calculateBodyLength(this.string.length) + 1 + this.string.length;
    }
    
    @Override
    public String getString() {
        return Strings.fromUTF8ByteArray(this.string);
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
