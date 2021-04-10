package org.spongycastle.asn1;

import org.spongycastle.util.*;
import java.io.*;

public class ASN1Boolean extends ASN1Primitive
{
    public static final ASN1Boolean FALSE;
    private static final byte[] FALSE_VALUE;
    public static final ASN1Boolean TRUE;
    private static final byte[] TRUE_VALUE;
    private final byte[] value;
    
    static {
        TRUE_VALUE = new byte[] { -1 };
        FALSE_VALUE = new byte[] { 0 };
        FALSE = new ASN1Boolean(false);
        TRUE = new ASN1Boolean(true);
    }
    
    public ASN1Boolean(final boolean b) {
        byte[] value;
        if (b) {
            value = ASN1Boolean.TRUE_VALUE;
        }
        else {
            value = ASN1Boolean.FALSE_VALUE;
        }
        this.value = value;
    }
    
    ASN1Boolean(byte[] value) {
        if (value.length == 1) {
            if (value[0] == 0) {
                value = ASN1Boolean.FALSE_VALUE;
            }
            else if ((value[0] & 0xFF) == 0xFF) {
                value = ASN1Boolean.TRUE_VALUE;
            }
            else {
                value = Arrays.clone(value);
            }
            this.value = value;
            return;
        }
        throw new IllegalArgumentException("byte value should have 1 byte in it");
    }
    
    static ASN1Boolean fromOctetString(final byte[] array) {
        if (array.length != 1) {
            throw new IllegalArgumentException("BOOLEAN value should have 1 byte in it");
        }
        if (array[0] == 0) {
            return ASN1Boolean.FALSE;
        }
        if ((array[0] & 0xFF) == 0xFF) {
            return ASN1Boolean.TRUE;
        }
        return new ASN1Boolean(array);
    }
    
    public static ASN1Boolean getInstance(final int n) {
        if (n != 0) {
            return ASN1Boolean.TRUE;
        }
        return ASN1Boolean.FALSE;
    }
    
    public static ASN1Boolean getInstance(final Object o) {
        if (o != null && !(o instanceof ASN1Boolean)) {
            if (o instanceof byte[]) {
                final byte[] array = (byte[])o;
                try {
                    return (ASN1Boolean)ASN1Primitive.fromByteArray(array);
                }
                catch (IOException ex) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("failed to construct boolean from byte[]: ");
                    sb.append(ex.getMessage());
                    throw new IllegalArgumentException(sb.toString());
                }
            }
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("illegal object in getInstance: ");
            sb2.append(o.getClass().getName());
            throw new IllegalArgumentException(sb2.toString());
        }
        return (ASN1Boolean)o;
    }
    
    public static ASN1Boolean getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        final ASN1Primitive object = asn1TaggedObject.getObject();
        if (!b && !(object instanceof ASN1Boolean)) {
            return fromOctetString(((ASN1OctetString)object).getOctets());
        }
        return getInstance(object);
    }
    
    public static ASN1Boolean getInstance(final boolean b) {
        if (b) {
            return ASN1Boolean.TRUE;
        }
        return ASN1Boolean.FALSE;
    }
    
    protected boolean asn1Equals(final ASN1Primitive asn1Primitive) {
        final boolean b = asn1Primitive instanceof ASN1Boolean;
        boolean b2 = false;
        if (b) {
            b2 = b2;
            if (this.value[0] == ((ASN1Boolean)asn1Primitive).value[0]) {
                b2 = true;
            }
        }
        return b2;
    }
    
    @Override
    void encode(final ASN1OutputStream asn1OutputStream) throws IOException {
        asn1OutputStream.writeEncoded(1, this.value);
    }
    
    @Override
    int encodedLength() {
        return 3;
    }
    
    @Override
    public int hashCode() {
        return this.value[0];
    }
    
    @Override
    boolean isConstructed() {
        return false;
    }
    
    public boolean isTrue() {
        final byte[] value = this.value;
        boolean b = false;
        if (value[0] != 0) {
            b = true;
        }
        return b;
    }
    
    @Override
    public String toString() {
        if (this.value[0] != 0) {
            return "TRUE";
        }
        return "FALSE";
    }
}
