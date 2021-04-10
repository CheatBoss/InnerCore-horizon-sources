package org.spongycastle.asn1;

import java.io.*;

public class BERApplicationSpecific extends ASN1ApplicationSpecific
{
    public BERApplicationSpecific(final int n, final ASN1Encodable asn1Encodable) throws IOException {
        this(true, n, asn1Encodable);
    }
    
    public BERApplicationSpecific(final int n, final ASN1EncodableVector asn1EncodableVector) {
        super(true, n, getEncodedVector(asn1EncodableVector));
    }
    
    public BERApplicationSpecific(final boolean b, final int n, final ASN1Encodable asn1Encodable) throws IOException {
        super(b || asn1Encodable.toASN1Primitive().isConstructed(), n, getEncoding(b, asn1Encodable));
    }
    
    BERApplicationSpecific(final boolean b, final int n, final byte[] array) {
        super(b, n, array);
    }
    
    private static byte[] getEncodedVector(final ASN1EncodableVector asn1EncodableVector) {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int i = 0;
        while (i != asn1EncodableVector.size()) {
            try {
                byteArrayOutputStream.write(((ASN1Object)asn1EncodableVector.get(i)).getEncoded("BER"));
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
        final byte[] encoded = asn1Encodable.toASN1Primitive().getEncoded("BER");
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
        asn1OutputStream.writeTag(n, this.tag);
        asn1OutputStream.write(128);
        asn1OutputStream.write(this.octets);
        asn1OutputStream.write(0);
        asn1OutputStream.write(0);
    }
}
