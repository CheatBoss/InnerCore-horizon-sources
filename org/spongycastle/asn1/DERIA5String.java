package org.spongycastle.asn1;

import org.spongycastle.util.*;
import java.io.*;

public class DERIA5String extends ASN1Primitive implements ASN1String
{
    private final byte[] string;
    
    public DERIA5String(final String s) {
        this(s, false);
    }
    
    public DERIA5String(final String s, final boolean b) {
        if (s == null) {
            throw new NullPointerException("string cannot be null");
        }
        if (b && !isIA5String(s)) {
            throw new IllegalArgumentException("string contains illegal characters");
        }
        this.string = Strings.toByteArray(s);
    }
    
    DERIA5String(final byte[] string) {
        this.string = string;
    }
    
    public static DERIA5String getInstance(final Object o) {
        if (o != null && !(o instanceof DERIA5String)) {
            if (o instanceof byte[]) {
                try {
                    return (DERIA5String)ASN1Primitive.fromByteArray((byte[])o);
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
        return (DERIA5String)o;
    }
    
    public static DERIA5String getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        final ASN1Primitive object = asn1TaggedObject.getObject();
        if (!b && !(object instanceof DERIA5String)) {
            return new DERIA5String(((ASN1OctetString)object).getOctets());
        }
        return getInstance(object);
    }
    
    public static boolean isIA5String(final String s) {
        for (int i = s.length() - 1; i >= 0; --i) {
            if (s.charAt(i) > '\u007f') {
                return false;
            }
        }
        return true;
    }
    
    @Override
    boolean asn1Equals(final ASN1Primitive asn1Primitive) {
        return asn1Primitive instanceof DERIA5String && Arrays.areEqual(this.string, ((DERIA5String)asn1Primitive).string);
    }
    
    @Override
    void encode(final ASN1OutputStream asn1OutputStream) throws IOException {
        asn1OutputStream.writeEncoded(22, this.string);
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
