package org.spongycastle.asn1;

import java.io.*;
import org.spongycastle.util.encoders.*;

public class DERApplicationSpecific extends ASN1ApplicationSpecific
{
    public DERApplicationSpecific(final int n, final ASN1Encodable asn1Encodable) throws IOException {
        this(true, n, asn1Encodable);
    }
    
    public DERApplicationSpecific(final int n, final ASN1EncodableVector asn1EncodableVector) {
        super(true, n, getEncodedVector(asn1EncodableVector));
    }
    
    public DERApplicationSpecific(final int n, final byte[] array) {
        this(false, n, array);
    }
    
    public DERApplicationSpecific(final boolean b, final int n, final ASN1Encodable asn1Encodable) throws IOException {
        super(b || asn1Encodable.toASN1Primitive().isConstructed(), n, getEncoding(b, asn1Encodable));
    }
    
    DERApplicationSpecific(final boolean b, final int n, final byte[] array) {
        super(b, n, array);
    }
    
    private static byte[] getEncodedVector(final ASN1EncodableVector asn1EncodableVector) {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int i = 0;
        while (i != asn1EncodableVector.size()) {
            try {
                byteArrayOutputStream.write(((ASN1Object)asn1EncodableVector.get(i)).getEncoded("DER"));
                ++i;
                continue;
            }
            catch (IOException ex) {
                final StringBuilder sb = new StringBuilder();
                sb.append("malformed object: ");
                sb.append(ex);
                throw new ASN1ParsingException(sb.toString(), ex);
            }
            break;
        }
        return byteArrayOutputStream.toByteArray();
    }
    
    private static byte[] getEncoding(final boolean b, final ASN1Encodable asn1Encodable) throws IOException {
        final byte[] encoded = asn1Encodable.toASN1Primitive().getEncoded("DER");
        if (b) {
            return encoded;
        }
        final int lengthOfHeader = ASN1ApplicationSpecific.getLengthOfHeader(encoded);
        final int n = encoded.length - lengthOfHeader;
        final byte[] array = new byte[n];
        System.arraycopy(encoded, lengthOfHeader, array, 0, n);
        return array;
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
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append("[");
        if (this.isConstructed()) {
            sb.append("CONSTRUCTED ");
        }
        sb.append("APPLICATION ");
        sb.append(Integer.toString(this.getApplicationTag()));
        sb.append("]");
        String hexString;
        if (this.octets != null) {
            sb.append(" #");
            hexString = Hex.toHexString(this.octets);
        }
        else {
            hexString = " #null";
        }
        sb.append(hexString);
        sb.append(" ");
        return sb.toString();
    }
}
