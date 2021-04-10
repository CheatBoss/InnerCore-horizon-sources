package org.spongycastle.asn1;

import org.spongycastle.util.*;
import org.spongycastle.util.io.*;
import java.io.*;

public abstract class ASN1BitString extends ASN1Primitive implements ASN1String
{
    private static final char[] table;
    protected final byte[] data;
    protected final int padBits;
    
    static {
        table = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
    }
    
    public ASN1BitString(final byte[] array, final int padBits) {
        if (array == null) {
            throw new NullPointerException("data cannot be null");
        }
        if (array.length == 0 && padBits != 0) {
            throw new IllegalArgumentException("zero length data with non-zero pad bits");
        }
        if (padBits <= 7 && padBits >= 0) {
            this.data = Arrays.clone(array);
            this.padBits = padBits;
            return;
        }
        throw new IllegalArgumentException("pad bits cannot be greater than 7 or less than 0");
    }
    
    protected static byte[] derForm(final byte[] array, final int n) {
        final byte[] clone = Arrays.clone(array);
        if (n > 0) {
            final int n2 = array.length - 1;
            clone[n2] &= (byte)(255 << n);
        }
        return clone;
    }
    
    static ASN1BitString fromInputStream(int n, final InputStream inputStream) throws IOException {
        if (n >= 1) {
            final int read = inputStream.read();
            --n;
            final byte[] array = new byte[n];
            if (n != 0) {
                if (Streams.readFully(inputStream, array) != n) {
                    throw new EOFException("EOF encountered in middle of BIT STRING");
                }
                if (read > 0 && read < 8) {
                    --n;
                    if (array[n] != (byte)(array[n] & 255 << read)) {
                        return new DLBitString(array, read);
                    }
                }
            }
            return new DERBitString(array, read);
        }
        throw new IllegalArgumentException("truncated BIT STRING detected");
    }
    
    protected static byte[] getBytes(final int n) {
        final int n2 = 0;
        if (n == 0) {
            return new byte[0];
        }
        int n3 = 4;
        for (int n4 = 3; n4 >= 1 && (255 << n4 * 8 & n) == 0x0; --n4) {
            --n3;
        }
        final byte[] array = new byte[n3];
        for (int i = n2; i < n3; ++i) {
            array[i] = (byte)(n >> i * 8 & 0xFF);
        }
        return array;
    }
    
    protected static int getPadBits(int n) {
        int i = 3;
    Label_0052:
        while (true) {
            while (i >= 0) {
                if (i != 0) {
                    final int n2 = n >> i * 8;
                    if (n2 != 0) {
                        n = (n2 & 0xFF);
                        break Label_0052;
                    }
                }
                else if (n != 0) {
                    n &= 0xFF;
                    break Label_0052;
                }
                --i;
                continue;
                if (n == 0) {
                    return 0;
                }
                final int n3 = 1;
                int n4 = n;
                n = n3;
                while (true) {
                    n4 <<= 1;
                    if ((n4 & 0xFF) == 0x0) {
                        break;
                    }
                    ++n;
                }
                return 8 - n;
            }
            n = 0;
            continue Label_0052;
        }
    }
    
    protected boolean asn1Equals(final ASN1Primitive asn1Primitive) {
        final boolean b = asn1Primitive instanceof ASN1BitString;
        final boolean b2 = false;
        if (!b) {
            return false;
        }
        final ASN1BitString asn1BitString = (ASN1BitString)asn1Primitive;
        boolean b3 = b2;
        if (this.padBits == asn1BitString.padBits) {
            b3 = b2;
            if (Arrays.areEqual(this.getBytes(), asn1BitString.getBytes())) {
                b3 = true;
            }
        }
        return b3;
    }
    
    @Override
    abstract void encode(final ASN1OutputStream p0) throws IOException;
    
    public byte[] getBytes() {
        return derForm(this.data, this.padBits);
    }
    
    public ASN1Primitive getLoadedObject() {
        return this.toASN1Primitive();
    }
    
    public byte[] getOctets() {
        if (this.padBits == 0) {
            return Arrays.clone(this.data);
        }
        throw new IllegalStateException("attempt to get non-octet aligned data from BIT STRING");
    }
    
    public int getPadBits() {
        return this.padBits;
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
                sb.append(ASN1BitString.table[byteArray[i] >>> 4 & 0xF]);
                sb.append(ASN1BitString.table[byteArray[i] & 0xF]);
            }
            return sb.toString();
        }
        catch (IOException ex) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Internal error encoding BitString: ");
            sb2.append(ex.getMessage());
            throw new ASN1ParsingException(sb2.toString(), ex);
        }
    }
    
    @Override
    public int hashCode() {
        return this.padBits ^ Arrays.hashCode(this.getBytes());
    }
    
    public int intValue() {
        final byte[] data = this.data;
        final int padBits = this.padBits;
        byte[] derForm = data;
        if (padBits > 0) {
            derForm = data;
            if (data.length <= 4) {
                derForm = derForm(data, padBits);
            }
        }
        int n = 0;
        int n2 = 0;
        while (n != derForm.length && n != 4) {
            n2 |= (derForm[n] & 0xFF) << n * 8;
            ++n;
        }
        return n2;
    }
    
    @Override
    ASN1Primitive toDERObject() {
        return new DERBitString(this.data, this.padBits);
    }
    
    @Override
    ASN1Primitive toDLObject() {
        return new DLBitString(this.data, this.padBits);
    }
    
    @Override
    public String toString() {
        return this.getString();
    }
}
