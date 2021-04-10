package org.apache.james.mime4j.codec;

import java.io.*;

public class CodecUtil
{
    static final int DEFAULT_ENCODING_BUFFER_SIZE = 1024;
    
    public static void copy(final InputStream inputStream, final OutputStream outputStream) throws IOException {
        final byte[] array = new byte[1024];
        while (true) {
            final int read = inputStream.read(array);
            if (-1 == read) {
                break;
            }
            outputStream.write(array, 0, read);
        }
    }
    
    public static void encodeBase64(final InputStream inputStream, final OutputStream outputStream) throws IOException {
        final Base64OutputStream base64OutputStream = new Base64OutputStream(outputStream);
        copy(inputStream, base64OutputStream);
        base64OutputStream.close();
    }
    
    public static void encodeQuotedPrintable(final InputStream inputStream, final OutputStream outputStream) throws IOException {
        new QuotedPrintableEncoder(1024, false).encode(inputStream, outputStream);
    }
    
    public static void encodeQuotedPrintableBinary(final InputStream inputStream, final OutputStream outputStream) throws IOException {
        new QuotedPrintableEncoder(1024, true).encode(inputStream, outputStream);
    }
    
    public static OutputStream wrapBase64(final OutputStream outputStream) throws IOException {
        return new Base64OutputStream(outputStream);
    }
    
    public static OutputStream wrapQuotedPrintable(final OutputStream outputStream, final boolean b) throws IOException {
        return new QuotedPrintableOutputStream(outputStream, b);
    }
}
