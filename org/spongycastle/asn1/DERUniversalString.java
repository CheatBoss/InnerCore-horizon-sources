package org.spongycastle.asn1;

import org.spongycastle.util.*;
import java.io.*;

public class DERUniversalString extends ASN1Primitive implements ASN1String
{
    private static final char[] table;
    private final byte[] string;
    
    static {
        table = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
    }
    
    public DERUniversalString(final byte[] array) {
        this.string = Arrays.clone(array);
    }
    
    public static DERUniversalString getInstance(final Object o) {
        if (o != null && !(o instanceof DERUniversalString)) {
            if (o instanceof byte[]) {
                try {
                    return (DERUniversalString)ASN1Primitive.fromByteArray((byte[])o);
                }
                catch (Exception ex) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("encoding error getInstance: ");
                    sb.append(ex.toString());
                    throw new IllegalArgumentException(sb.toString());
                }
            }
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("illegal object in getInstance: ");
            sb2.append(o.getClass().getName());
            throw new IllegalArgumentException(sb2.toString());
        }
        return (DERUniversalString)o;
    }
    
    public static DERUniversalString getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        final ASN1Primitive object = asn1TaggedObject.getObject();
        if (!b && !(object instanceof DERUniversalString)) {
            return new DERUniversalString(((ASN1OctetString)object).getOctets());
        }
        return getInstance(object);
    }
    
    @Override
    boolean asn1Equals(final ASN1Primitive asn1Primitive) {
        return asn1Primitive instanceof DERUniversalString && Arrays.areEqual(this.string, ((DERUniversalString)asn1Primitive).string);
    }
    
    @Override
    void encode(final ASN1OutputStream asn1OutputStream) throws IOException {
        asn1OutputStream.writeEncoded(28, this.getOctets());
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
        final StringBuffer sb = new StringBuffer("#");
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final ASN1OutputStream asn1OutputStream = new ASN1OutputStream(byteArrayOutputStream);
        try {
            asn1OutputStream.writeObject(this);
            final byte[] byteArray = byteArrayOutputStream.toByteArray();
            for (int i = 0; i != byteArray.length; ++i) {
                sb.append(DERUniversalString.table[byteArray[i] >>> 4 & 0xF]);
                sb.append(DERUniversalString.table[byteArray[i] & 0xF]);
            }
            return sb.toString();
        }
        catch (IOException ex) {
            throw new ASN1ParsingException("internal error encoding BitString");
        }
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
