package org.spongycastle.asn1;

import java.io.*;

public class DLBitString extends ASN1BitString
{
    protected DLBitString(final byte b, final int n) {
        this(toByteArray(b), n);
    }
    
    public DLBitString(final int n) {
        super(ASN1BitString.getBytes(n), ASN1BitString.getPadBits(n));
    }
    
    public DLBitString(final ASN1Encodable asn1Encodable) throws IOException {
        super(asn1Encodable.toASN1Primitive().getEncoded("DER"), 0);
    }
    
    public DLBitString(final byte[] array) {
        this(array, 0);
    }
    
    public DLBitString(final byte[] array, final int n) {
        super(array, n);
    }
    
    static DLBitString fromOctetString(final byte[] array) {
        if (array.length >= 1) {
            final byte b = array[0];
            final int n = array.length - 1;
            final byte[] array2 = new byte[n];
            if (n != 0) {
                System.arraycopy(array, 1, array2, 0, array.length - 1);
            }
            return new DLBitString(array2, b);
        }
        throw new IllegalArgumentException("truncated BIT STRING detected");
    }
    
    public static ASN1BitString getInstance(final Object o) {
        if (o == null || o instanceof DLBitString) {
            return (DLBitString)o;
        }
        if (o instanceof DERBitString) {
            return (DERBitString)o;
        }
        if (o instanceof byte[]) {
            try {
                return (ASN1BitString)ASN1Primitive.fromByteArray((byte[])o);
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
    
    public static ASN1BitString getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        final ASN1Primitive object = asn1TaggedObject.getObject();
        if (!b && !(object instanceof DLBitString)) {
            return fromOctetString(((ASN1OctetString)object).getOctets());
        }
        return getInstance(object);
    }
    
    private static byte[] toByteArray(final byte b) {
        return new byte[] { b };
    }
    
    @Override
    void encode(final ASN1OutputStream asn1OutputStream) throws IOException {
        final byte[] data = this.data;
        final int n = data.length + 1;
        final byte[] array = new byte[n];
        array[0] = (byte)this.getPadBits();
        System.arraycopy(data, 0, array, 1, n - 1);
        asn1OutputStream.writeEncoded(3, array);
    }
    
    @Override
    int encodedLength() {
        return StreamUtil.calculateBodyLength(this.data.length + 1) + 1 + this.data.length + 1;
    }
    
    @Override
    boolean isConstructed() {
        return false;
    }
}
