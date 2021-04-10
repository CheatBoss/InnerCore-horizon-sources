package org.spongycastle.util.encoders;

import java.io.*;
import org.spongycastle.util.*;

public class Hex
{
    private static final Encoder encoder;
    
    static {
        encoder = new HexEncoder();
    }
    
    public static int decode(final String s, final OutputStream outputStream) throws IOException {
        return Hex.encoder.decode(s, outputStream);
    }
    
    public static byte[] decode(final String s) {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            Hex.encoder.decode(s, byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        }
        catch (Exception ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("exception decoding Hex string: ");
            sb.append(ex.getMessage());
            throw new DecoderException(sb.toString(), ex);
        }
    }
    
    public static byte[] decode(final byte[] array) {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            Hex.encoder.decode(array, 0, array.length, byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        }
        catch (Exception ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("exception decoding Hex data: ");
            sb.append(ex.getMessage());
            throw new DecoderException(sb.toString(), ex);
        }
    }
    
    public static int encode(final byte[] array, final int n, final int n2, final OutputStream outputStream) throws IOException {
        return Hex.encoder.encode(array, n, n2, outputStream);
    }
    
    public static int encode(final byte[] array, final OutputStream outputStream) throws IOException {
        return Hex.encoder.encode(array, 0, array.length, outputStream);
    }
    
    public static byte[] encode(final byte[] array) {
        return encode(array, 0, array.length);
    }
    
    public static byte[] encode(final byte[] array, final int n, final int n2) {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            Hex.encoder.encode(array, n, n2, byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        }
        catch (Exception ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("exception encoding Hex string: ");
            sb.append(ex.getMessage());
            throw new EncoderException(sb.toString(), ex);
        }
    }
    
    public static String toHexString(final byte[] array) {
        return toHexString(array, 0, array.length);
    }
    
    public static String toHexString(final byte[] array, final int n, final int n2) {
        return Strings.fromByteArray(encode(array, n, n2));
    }
}
