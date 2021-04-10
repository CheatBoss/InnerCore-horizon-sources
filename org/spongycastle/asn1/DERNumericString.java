package org.spongycastle.asn1;

import org.spongycastle.util.*;
import java.io.*;

public class DERNumericString extends ASN1Primitive implements ASN1String
{
    private final byte[] string;
    
    public DERNumericString(final String s) {
        this(s, false);
    }
    
    public DERNumericString(final String s, final boolean b) {
        if (b && !isNumericString(s)) {
            throw new IllegalArgumentException("string contains illegal characters");
        }
        this.string = Strings.toByteArray(s);
    }
    
    DERNumericString(final byte[] string) {
        this.string = string;
    }
    
    public static DERNumericString getInstance(final Object o) {
        if (o != null && !(o instanceof DERNumericString)) {
            if (o instanceof byte[]) {
                try {
                    return (DERNumericString)ASN1Primitive.fromByteArray((byte[])o);
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
        return (DERNumericString)o;
    }
    
    public static DERNumericString getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        final ASN1Primitive object = asn1TaggedObject.getObject();
        if (!b && !(object instanceof DERNumericString)) {
            return new DERNumericString(ASN1OctetString.getInstance(object).getOctets());
        }
        return getInstance(object);
    }
    
    public static boolean isNumericString(final String s) {
        for (int i = s.length() - 1; i >= 0; --i) {
            final char char1 = s.charAt(i);
            if (char1 > '\u007f') {
                return false;
            }
            if (('0' > char1 || char1 > '9') && char1 != ' ') {
                return false;
            }
        }
        return true;
    }
    
    @Override
    boolean asn1Equals(final ASN1Primitive asn1Primitive) {
        return asn1Primitive instanceof DERNumericString && Arrays.areEqual(this.string, ((DERNumericString)asn1Primitive).string);
    }
    
    @Override
    void encode(final ASN1OutputStream asn1OutputStream) throws IOException {
        asn1OutputStream.writeEncoded(18, this.string);
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
