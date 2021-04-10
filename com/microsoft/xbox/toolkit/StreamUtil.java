package com.microsoft.xbox.toolkit;

import java.io.*;

public class StreamUtil
{
    public static void CopyStream(final OutputStream outputStream, final InputStream inputStream) throws IOException {
        final byte[] array = new byte[16384];
        while (true) {
            final int read = inputStream.read(array);
            if (read <= 0) {
                break;
            }
            outputStream.write(array, 0, read);
        }
        outputStream.flush();
    }
    
    public static byte[] CreateByteArray(final InputStream inputStream) {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            CopyStream(byteArrayOutputStream, inputStream);
            return byteArrayOutputStream.toByteArray();
        }
        catch (IOException ex) {
            return null;
        }
    }
    
    public static byte[] HexStringToByteArray(final String s) {
        if (s != null) {
            String string = s;
            if (s.length() % 2 != 0) {
                final StringBuilder sb = new StringBuilder();
                sb.append("0");
                sb.append(s);
                string = sb.toString();
            }
            final int length = string.length();
            int i = 0;
            XLEAssert.assertTrue(length % 2 == 0);
            final byte[] array = new byte[string.length() / 2];
            while (i < string.length()) {
                final int n = i / 2;
                final int n2 = i + 2;
                array[n] = Byte.parseByte(string.substring(i, n2), 16);
                i = n2;
            }
            return array;
        }
        throw new IllegalArgumentException("hexString invalid");
    }
    
    public static String ReadAsString(final InputStream inputStream) {
        final StringBuilder sb = new StringBuilder();
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            while (true) {
                final String line = bufferedReader.readLine();
                if (line == null) {
                    break;
                }
                sb.append(line);
                sb.append('\n');
            }
            return sb.toString();
        }
        catch (IOException ex) {
            return null;
        }
    }
    
    public static void consumeAndClose(InputStream inputStream) throws IOException {
        inputStream = new BufferedInputStream(inputStream);
        try {
            while (inputStream.read() != -1) {}
        }
        finally {
            inputStream.close();
        }
    }
}
