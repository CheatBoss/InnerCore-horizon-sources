package org.spongycastle.asn1;

import org.spongycastle.util.*;
import java.io.*;

public abstract class ASN1ApplicationSpecific extends ASN1Primitive
{
    protected final boolean isConstructed;
    protected final byte[] octets;
    protected final int tag;
    
    ASN1ApplicationSpecific(final boolean isConstructed, final int tag, final byte[] array) {
        this.isConstructed = isConstructed;
        this.tag = tag;
        this.octets = Arrays.clone(array);
    }
    
    public static ASN1ApplicationSpecific getInstance(final Object o) {
        if (o != null && !(o instanceof ASN1ApplicationSpecific)) {
            if (o instanceof byte[]) {
                try {
                    return getInstance(ASN1Primitive.fromByteArray((byte[])o));
                }
                catch (IOException ex) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Failed to construct object from byte[]: ");
                    sb.append(ex.getMessage());
                    throw new IllegalArgumentException(sb.toString());
                }
            }
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("unknown object in getInstance: ");
            sb2.append(o.getClass().getName());
            throw new IllegalArgumentException(sb2.toString());
        }
        return (ASN1ApplicationSpecific)o;
    }
    
    protected static int getLengthOfHeader(final byte[] array) {
        final int n = array[1] & 0xFF;
        if (n == 128) {
            return 2;
        }
        if (n <= 127) {
            return 2;
        }
        final int n2 = n & 0x7F;
        if (n2 <= 4) {
            return n2 + 2;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("DER length more than 4 bytes: ");
        sb.append(n2);
        throw new IllegalStateException(sb.toString());
    }
    
    private byte[] replaceTagNumber(final int n, final byte[] array) throws IOException {
        int n4;
        if ((array[0] & 0x1F) == 0x1F) {
            int n2 = 2;
            int n3 = array[1] & 0xFF;
            if ((n3 & 0x7F) == 0x0) {
                throw new ASN1ParsingException("corrupted stream - invalid high tag number found");
            }
            while (true) {
                n4 = n2;
                if (n3 < 0) {
                    break;
                }
                n4 = n2;
                if ((n3 & 0x80) == 0x0) {
                    break;
                }
                n3 = (array[n2] & 0xFF);
                ++n2;
            }
        }
        else {
            n4 = 1;
        }
        final int n5 = array.length - n4 + 1;
        final byte[] array2 = new byte[n5];
        System.arraycopy(array, n4, array2, 1, n5 - 1);
        array2[0] = (byte)n;
        return array2;
    }
    
    @Override
    boolean asn1Equals(final ASN1Primitive asn1Primitive) {
        final boolean b = asn1Primitive instanceof ASN1ApplicationSpecific;
        final boolean b2 = false;
        if (!b) {
            return false;
        }
        final ASN1ApplicationSpecific asn1ApplicationSpecific = (ASN1ApplicationSpecific)asn1Primitive;
        boolean b3 = b2;
        if (this.isConstructed == asn1ApplicationSpecific.isConstructed) {
            b3 = b2;
            if (this.tag == asn1ApplicationSpecific.tag) {
                b3 = b2;
                if (Arrays.areEqual(this.octets, asn1ApplicationSpecific.octets)) {
                    b3 = true;
                }
            }
        }
        return b3;
    }
    
    @Override
    void encode(final ASN1OutputStream asn1OutputStream) throws IOException {
        int n;
        if (this.isConstructed) {
            n = 96;
        }
        else {
            n = 64;
        }
        asn1OutputStream.writeEncoded(n, this.tag, this.octets);
    }
    
    @Override
    int encodedLength() throws IOException {
        return StreamUtil.calculateTagLength(this.tag) + StreamUtil.calculateBodyLength(this.octets.length) + this.octets.length;
    }
    
    public int getApplicationTag() {
        return this.tag;
    }
    
    public byte[] getContents() {
        return Arrays.clone(this.octets);
    }
    
    public ASN1Primitive getObject() throws IOException {
        return ASN1Primitive.fromByteArray(this.getContents());
    }
    
    public ASN1Primitive getObject(final int n) throws IOException {
        if (n < 31) {
            final byte[] encoded = this.getEncoded();
            final byte[] replaceTagNumber = this.replaceTagNumber(n, encoded);
            if ((encoded[0] & 0x20) != 0x0) {
                replaceTagNumber[0] |= 0x20;
            }
            return ASN1Primitive.fromByteArray(replaceTagNumber);
        }
        throw new IOException("unsupported tag number");
    }
    
    @Override
    public int hashCode() {
        return (this.isConstructed ? 1 : 0) ^ this.tag ^ Arrays.hashCode(this.octets);
    }
    
    public boolean isConstructed() {
        return this.isConstructed;
    }
}
