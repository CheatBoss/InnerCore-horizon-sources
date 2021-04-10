package org.spongycastle.util.encoders;

import java.io.*;

public class UrlBase64
{
    private static final Encoder encoder;
    
    static {
        encoder = new UrlBase64Encoder();
    }
    
    public static int decode(final String s, final OutputStream outputStream) throws IOException {
        return UrlBase64.encoder.decode(s, outputStream);
    }
    
    public static int decode(final byte[] array, final OutputStream outputStream) throws IOException {
        return UrlBase64.encoder.decode(array, 0, array.length, outputStream);
    }
    
    public static byte[] decode(final String s) {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            UrlBase64.encoder.decode(s, byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        }
        catch (Exception ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("exception decoding URL safe base64 string: ");
            sb.append(ex.getMessage());
            throw new DecoderException(sb.toString(), ex);
        }
    }
    
    public static byte[] decode(final byte[] array) {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            UrlBase64.encoder.decode(array, 0, array.length, byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        }
        catch (Exception ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("exception decoding URL safe base64 string: ");
            sb.append(ex.getMessage());
            throw new DecoderException(sb.toString(), ex);
        }
    }
    
    public static int encode(final byte[] array, final OutputStream outputStream) throws IOException {
        return UrlBase64.encoder.encode(array, 0, array.length, outputStream);
    }
    
    public static byte[] encode(final byte[] array) {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            UrlBase64.encoder.encode(array, 0, array.length, byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        }
        catch (Exception ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("exception encoding URL safe base64 data: ");
            sb.append(ex.getMessage());
            throw new EncoderException(sb.toString(), ex);
        }
    }
}
