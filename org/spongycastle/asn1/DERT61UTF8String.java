package org.spongycastle.asn1;

import org.spongycastle.util.*;
import java.io.*;

public class DERT61UTF8String extends ASN1Primitive implements ASN1String
{
    private byte[] string;
    
    public DERT61UTF8String(final String s) {
        this(Strings.toUTF8ByteArray(s));
    }
    
    public DERT61UTF8String(final byte[] string) {
        this.string = string;
    }
    
    public static DERT61UTF8String getInstance(final Object o) {
        if (o instanceof DERT61String) {
            return new DERT61UTF8String(((DERT61String)o).getOctets());
        }
        if (o != null && !(o instanceof DERT61UTF8String)) {
            if (o instanceof byte[]) {
                try {
                    return new DERT61UTF8String(((DERT61String)ASN1Primitive.fromByteArray((byte[])o)).getOctets());
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
        return (DERT61UTF8String)o;
    }
    
    public static DERT61UTF8String getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        final ASN1Primitive object = asn1TaggedObject.getObject();
        if (!b && !(object instanceof DERT61String) && !(object instanceof DERT61UTF8String)) {
            return new DERT61UTF8String(ASN1OctetString.getInstance(object).getOctets());
        }
        return getInstance(object);
    }
    
    @Override
    boolean asn1Equals(final ASN1Primitive asn1Primitive) {
        return asn1Primitive instanceof DERT61UTF8String && Arrays.areEqual(this.string, ((DERT61UTF8String)asn1Primitive).string);
    }
    
    @Override
    void encode(final ASN1OutputStream asn1OutputStream) throws IOException {
        asn1OutputStream.writeEncoded(20, this.string);
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
