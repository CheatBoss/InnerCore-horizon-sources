package org.spongycastle.asn1;

import org.spongycastle.util.*;
import java.io.*;

public class DERBMPString extends ASN1Primitive implements ASN1String
{
    private final char[] string;
    
    public DERBMPString(final String s) {
        this.string = s.toCharArray();
    }
    
    DERBMPString(final byte[] array) {
        final int n = array.length / 2;
        final char[] string = new char[n];
        for (int i = 0; i != n; ++i) {
            final int n2 = i * 2;
            string[i] = (char)((array[n2 + 1] & 0xFF) | array[n2] << 8);
        }
        this.string = string;
    }
    
    DERBMPString(final char[] string) {
        this.string = string;
    }
    
    public static DERBMPString getInstance(final Object o) {
        if (o != null && !(o instanceof DERBMPString)) {
            if (o instanceof byte[]) {
                try {
                    return (DERBMPString)ASN1Primitive.fromByteArray((byte[])o);
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
        return (DERBMPString)o;
    }
    
    public static DERBMPString getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        final ASN1Primitive object = asn1TaggedObject.getObject();
        if (!b && !(object instanceof DERBMPString)) {
            return new DERBMPString(ASN1OctetString.getInstance(object).getOctets());
        }
        return getInstance(object);
    }
    
    protected boolean asn1Equals(final ASN1Primitive asn1Primitive) {
        return asn1Primitive instanceof DERBMPString && Arrays.areEqual(this.string, ((DERBMPString)asn1Primitive).string);
    }
    
    @Override
    void encode(final ASN1OutputStream asn1OutputStream) throws IOException {
        asn1OutputStream.write(30);
        asn1OutputStream.writeLength(this.string.length * 2);
        int n = 0;
        while (true) {
            final char[] string = this.string;
            if (n == string.length) {
                break;
            }
            final char c = string[n];
            asn1OutputStream.write((byte)(c >> 8));
            asn1OutputStream.write((byte)c);
            ++n;
        }
    }
    
    @Override
    int encodedLength() {
        return StreamUtil.calculateBodyLength(this.string.length * 2) + 1 + this.string.length * 2;
    }
    
    @Override
    public String getString() {
        return new String(this.string);
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
