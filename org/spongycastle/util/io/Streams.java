package org.spongycastle.util.io;

import java.io.*;

public final class Streams
{
    private static int BUFFER_SIZE = 4096;
    
    public static void drain(final InputStream inputStream) throws IOException {
        final int buffer_SIZE = Streams.BUFFER_SIZE;
        while (inputStream.read(new byte[buffer_SIZE], 0, buffer_SIZE) >= 0) {}
    }
    
    public static void pipeAll(final InputStream inputStream, final OutputStream outputStream) throws IOException {
        final int buffer_SIZE = Streams.BUFFER_SIZE;
        final byte[] array = new byte[buffer_SIZE];
        while (true) {
            final int read = inputStream.read(array, 0, buffer_SIZE);
            if (read < 0) {
                break;
            }
            outputStream.write(array, 0, read);
        }
    }
    
    public static long pipeAllLimited(final InputStream inputStream, final long n, final OutputStream outputStream) throws IOException {
        final int buffer_SIZE = Streams.BUFFER_SIZE;
        final byte[] array = new byte[buffer_SIZE];
        long n2 = 0L;
        while (true) {
            final int read = inputStream.read(array, 0, buffer_SIZE);
            if (read < 0) {
                return n2;
            }
            final long n3 = read;
            if (n - n2 < n3) {
                throw new StreamOverflowException("Data Overflow");
            }
            outputStream.write(array, 0, read);
            n2 += n3;
        }
    }
    
    public static byte[] readAll(final InputStream inputStream) throws IOException {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        pipeAll(inputStream, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
    
    public static byte[] readAllLimited(final InputStream inputStream, final int n) throws IOException {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        pipeAllLimited(inputStream, n, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
    
    public static int readFully(final InputStream inputStream, final byte[] array) throws IOException {
        return readFully(inputStream, array, 0, array.length);
    }
    
    public static int readFully(final InputStream inputStream, final byte[] array, final int n, final int n2) throws IOException {
        int i;
        int read;
        for (i = 0; i < n2; i += read) {
            read = inputStream.read(array, n + i, n2 - i);
            if (read < 0) {
                return i;
            }
        }
        return i;
    }
    
    public static void writeBufTo(final ByteArrayOutputStream byteArrayOutputStream, final OutputStream outputStream) throws IOException {
        byteArrayOutputStream.writeTo(outputStream);
    }
}
