package org.spongycastle.util.encoders;

import java.io.*;
import org.spongycastle.util.*;

public class Base64
{
    private static final Encoder encoder;
    
    static {
        encoder = new Base64Encoder();
    }
    
    public static int decode(final String s, final OutputStream outputStream) throws IOException {
        return Base64.encoder.decode(s, outputStream);
    }
    
    public static int decode(final byte[] array, int decode, final int n, final OutputStream outputStream) {
        try {
            decode = Base64.encoder.decode(array, decode, n, outputStream);
            return decode;
        }
        catch (Exception ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("unable to decode base64 data: ");
            sb.append(ex.getMessage());
            throw new DecoderException(sb.toString(), ex);
        }
    }
    
    public static byte[] decode(final String s) {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(s.length() / 4 * 3);
        try {
            Base64.encoder.decode(s, byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        }
        catch (Exception ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("unable to decode base64 string: ");
            sb.append(ex.getMessage());
            throw new DecoderException(sb.toString(), ex);
        }
    }
    
    public static byte[] decode(final byte[] array) {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(array.length / 4 * 3);
        try {
            Base64.encoder.decode(array, 0, array.length, byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        }
        catch (Exception ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("unable to decode base64 data: ");
            sb.append(ex.getMessage());
            throw new DecoderException(sb.toString(), ex);
        }
    }
    
    public static int encode(final byte[] array, final int n, final int n2, final OutputStream outputStream) throws IOException {
        return Base64.encoder.encode(array, n, n2, outputStream);
    }
    
    public static int encode(final byte[] array, final OutputStream outputStream) throws IOException {
        return Base64.encoder.encode(array, 0, array.length, outputStream);
    }
    
    public static byte[] encode(final byte[] array) {
        return encode(array, 0, array.length);
    }
    
    public static byte[] encode(final byte[] array, final int n, final int n2) {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream((n2 + 2) / 3 * 4);
        try {
            Base64.encoder.encode(array, n, n2, byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        }
        catch (Exception ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("exception encoding base64 string: ");
            sb.append(ex.getMessage());
            throw new EncoderException(sb.toString(), ex);
        }
    }
    
    public static String toBase64String(final byte[] array) {
        return toBase64String(array, 0, array.length);
    }
    
    public static String toBase64String(final byte[] array, final int n, final int n2) {
        return Strings.fromByteArray(encode(array, n, n2));
    }
}
