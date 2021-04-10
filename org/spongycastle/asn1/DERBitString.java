package org.spongycastle.asn1;

import java.io.*;

public class DERBitString extends ASN1BitString
{
    protected DERBitString(final byte b, final int n) {
        this(toByteArray(b), n);
    }
    
    public DERBitString(final int n) {
        super(ASN1BitString.getBytes(n), ASN1BitString.getPadBits(n));
    }
    
    public DERBitString(final ASN1Encodable asn1Encodable) throws IOException {
        super(asn1Encodable.toASN1Primitive().getEncoded("DER"), 0);
    }
    
    public DERBitString(final byte[] array) {
        this(array, 0);
    }
    
    public DERBitString(final byte[] array, final int n) {
        super(array, n);
    }
    
    static DERBitString fromOctetString(final byte[] array) {
        if (array.length >= 1) {
            final byte b = array[0];
            final int n = array.length - 1;
            final byte[] array2 = new byte[n];
            if (n != 0) {
                System.arraycopy(array, 1, array2, 0, array.length - 1);
            }
            return new DERBitString(array2, b);
        }
        throw new IllegalArgumentException("truncated BIT STRING detected");
    }
    
    public static DERBitString getInstance(final Object o) {
        if (o == null || o instanceof DERBitString) {
            return (DERBitString)o;
        }
        if (o instanceof DLBitString) {
            final DLBitString dlBitString = (DLBitString)o;
            return new DERBitString(dlBitString.data, dlBitString.padBits);
        }
        if (o instanceof byte[]) {
            try {
                return (DERBitString)ASN1Primitive.fromByteArray((byte[])o);
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
    
    public static DERBitString getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        final ASN1Primitive object = asn1TaggedObject.getObject();
        if (!b && !(object instanceof DERBitString)) {
            return fromOctetString(((ASN1OctetString)object).getOctets());
        }
        return getInstance(object);
    }
    
    private static byte[] toByteArray(final byte b) {
        return new byte[] { b };
    }
    
    @Override
    void encode(final ASN1OutputStream asn1OutputStream) throws IOException {
        final byte[] derForm = ASN1BitString.derForm(this.data, this.padBits);
        final int n = derForm.length + 1;
        final byte[] array = new byte[n];
        array[0] = (byte)this.getPadBits();
        System.arraycopy(derForm, 0, array, 1, n - 1);
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
