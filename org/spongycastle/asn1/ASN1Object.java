package org.spongycastle.asn1;

import org.spongycastle.util.*;
import java.io.*;

public abstract class ASN1Object implements ASN1Encodable, Encodable
{
    protected static boolean hasEncodedTagValue(final Object o, final int n) {
        final boolean b = o instanceof byte[];
        boolean b2 = false;
        if (b) {
            b2 = b2;
            if (((byte[])o)[0] == n) {
                b2 = true;
            }
        }
        return b2;
    }
    
    @Override
    public boolean equals(final Object o) {
        return this == o || (o instanceof ASN1Encodable && this.toASN1Primitive().equals(((ASN1Encodable)o).toASN1Primitive()));
    }
    
    @Override
    public byte[] getEncoded() throws IOException {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        new ASN1OutputStream(byteArrayOutputStream).writeObject(this);
        return byteArrayOutputStream.toByteArray();
    }
    
    public byte[] getEncoded(final String s) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream;
        if (s.equals("DER")) {
            byteArrayOutputStream = new ByteArrayOutputStream();
            new DEROutputStream(byteArrayOutputStream).writeObject(this);
        }
        else {
            if (!s.equals("DL")) {
                return this.getEncoded();
            }
            byteArrayOutputStream = new ByteArrayOutputStream();
            new DLOutputStream(byteArrayOutputStream).writeObject(this);
        }
        return byteArrayOutputStream.toByteArray();
    }
    
    @Override
    public int hashCode() {
        return this.toASN1Primitive().hashCode();
    }
    
    public ASN1Primitive toASN1Object() {
        return this.toASN1Primitive();
    }
    
    @Override
    public abstract ASN1Primitive toASN1Primitive();
}
