package org.apache.james.mime4j.codec;

import java.util.*;
import java.io.*;

public class Base64OutputStream extends FilterOutputStream
{
    private static final Set<Byte> BASE64_CHARS;
    private static final byte BASE64_PAD = 61;
    static final byte[] BASE64_TABLE;
    private static final byte[] CRLF_SEPARATOR;
    private static final int DEFAULT_LINE_LENGTH = 76;
    private static final int ENCODED_BUFFER_SIZE = 2048;
    private static final int MASK_6BITS = 63;
    private boolean closed;
    private int data;
    private final byte[] encoded;
    private final int lineLength;
    private int linePosition;
    private final byte[] lineSeparator;
    private int modulus;
    private int position;
    private final byte[] singleByte;
    
    static {
        CRLF_SEPARATOR = new byte[] { 13, 10 };
        BASE64_TABLE = new byte[] { 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 43, 47 };
        BASE64_CHARS = new HashSet<Byte>();
        final byte[] base64_TABLE = Base64OutputStream.BASE64_TABLE;
        for (int length = base64_TABLE.length, i = 0; i < length; ++i) {
            Base64OutputStream.BASE64_CHARS.add(base64_TABLE[i]);
        }
        Base64OutputStream.BASE64_CHARS.add((Byte)61);
    }
    
    public Base64OutputStream(final OutputStream outputStream) {
        this(outputStream, 76, Base64OutputStream.CRLF_SEPARATOR);
    }
    
    public Base64OutputStream(final OutputStream outputStream, final int n) {
        this(outputStream, n, Base64OutputStream.CRLF_SEPARATOR);
    }
    
    public Base64OutputStream(final OutputStream outputStream, final int lineLength, final byte[] array) {
        super(outputStream);
        this.singleByte = new byte[1];
        this.closed = false;
        this.position = 0;
        this.data = 0;
        this.modulus = 0;
        this.linePosition = 0;
        if (outputStream == null) {
            throw new IllegalArgumentException();
        }
        if (lineLength >= 0) {
            this.checkLineSeparator(array);
            this.lineLength = lineLength;
            System.arraycopy(array, 0, this.lineSeparator = new byte[array.length], 0, array.length);
            this.encoded = new byte[2048];
            return;
        }
        throw new IllegalArgumentException();
    }
    
    private void checkLineSeparator(final byte[] array) {
        if (array.length <= 2048) {
            for (int length = array.length, i = 0; i < length; ++i) {
                final byte b = array[i];
                if (Base64OutputStream.BASE64_CHARS.contains(b)) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("line separator must not contain base64 character '");
                    sb.append((char)(b & 0xFF));
                    sb.append("'");
                    throw new IllegalArgumentException(sb.toString());
                }
            }
            return;
        }
        throw new IllegalArgumentException("line separator length exceeds 2048");
    }
    
    private void close0() throws IOException {
        if (this.modulus != 0) {
            this.writePad();
        }
        if (this.lineLength > 0 && this.linePosition > 0) {
            this.writeLineSeparator();
        }
        this.flush0();
    }
    
    private void flush0() throws IOException {
        if (this.position > 0) {
            this.out.write(this.encoded, 0, this.position);
            this.position = 0;
        }
    }
    
    private void write0(final byte[] array, int i, final int n) throws IOException {
        while (i < n) {
            this.data = (this.data << 8 | (array[i] & 0xFF));
            if (++this.modulus == 3) {
                int j = 0;
                this.modulus = 0;
                final int lineLength = this.lineLength;
                if (lineLength > 0 && this.linePosition >= lineLength) {
                    this.linePosition = 0;
                    if (this.encoded.length - this.position < this.lineSeparator.length) {
                        this.flush0();
                    }
                    for (byte[] lineSeparator = this.lineSeparator; j < lineSeparator.length; ++j) {
                        this.encoded[this.position++] = lineSeparator[j];
                    }
                }
                if (this.encoded.length - this.position < 4) {
                    this.flush0();
                }
                final byte[] encoded = this.encoded;
                final int position = this.position;
                final int position2 = position + 1;
                this.position = position2;
                final byte[] base64_TABLE = Base64OutputStream.BASE64_TABLE;
                final int data = this.data;
                encoded[position] = base64_TABLE[data >> 18 & 0x3F];
                final int position3 = position2 + 1;
                this.position = position3;
                encoded[position2] = base64_TABLE[data >> 12 & 0x3F];
                final int position4 = position3 + 1;
                this.position = position4;
                encoded[position3] = base64_TABLE[data >> 6 & 0x3F];
                this.position = position4 + 1;
                encoded[position4] = base64_TABLE[data & 0x3F];
                this.linePosition += 4;
            }
            ++i;
        }
    }
    
    private void writeLineSeparator() throws IOException {
        int i = 0;
        this.linePosition = 0;
        if (this.encoded.length - this.position < this.lineSeparator.length) {
            this.flush0();
        }
        for (byte[] lineSeparator = this.lineSeparator; i < lineSeparator.length; ++i) {
            this.encoded[this.position++] = lineSeparator[i];
        }
    }
    
    private void writePad() throws IOException {
        final int lineLength = this.lineLength;
        if (lineLength > 0 && this.linePosition >= lineLength) {
            this.writeLineSeparator();
        }
        if (this.encoded.length - this.position < 4) {
            this.flush0();
        }
        if (this.modulus == 1) {
            final byte[] encoded = this.encoded;
            final int position = this.position;
            final int position2 = position + 1;
            this.position = position2;
            final byte[] base64_TABLE = Base64OutputStream.BASE64_TABLE;
            final int data = this.data;
            encoded[position] = base64_TABLE[data >> 2 & 0x3F];
            final int position3 = position2 + 1;
            this.position = position3;
            encoded[position2] = base64_TABLE[data << 4 & 0x3F];
            final int position4 = position3 + 1;
            this.position = position4;
            encoded[position3] = 61;
            this.position = position4 + 1;
            encoded[position4] = 61;
        }
        else {
            final byte[] encoded2 = this.encoded;
            final int position5 = this.position;
            final int position6 = position5 + 1;
            this.position = position6;
            final byte[] base64_TABLE2 = Base64OutputStream.BASE64_TABLE;
            final int data2 = this.data;
            encoded2[position5] = base64_TABLE2[data2 >> 10 & 0x3F];
            final int position7 = position6 + 1;
            this.position = position7;
            encoded2[position6] = base64_TABLE2[data2 >> 4 & 0x3F];
            final int position8 = position7 + 1;
            this.position = position8;
            encoded2[position7] = base64_TABLE2[data2 << 2 & 0x3F];
            this.position = position8 + 1;
            encoded2[position8] = 61;
        }
        this.linePosition += 4;
    }
    
    @Override
    public void close() throws IOException {
        if (this.closed) {
            return;
        }
        this.closed = true;
        this.close0();
    }
    
    @Override
    public void flush() throws IOException {
        if (!this.closed) {
            this.flush0();
            return;
        }
        throw new IOException("Base64OutputStream has been closed");
    }
    
    @Override
    public final void write(final int n) throws IOException {
        if (!this.closed) {
            final byte[] singleByte = this.singleByte;
            singleByte[0] = (byte)n;
            this.write0(singleByte, 0, 1);
            return;
        }
        throw new IOException("Base64OutputStream has been closed");
    }
    
    @Override
    public final void write(final byte[] array) throws IOException {
        if (this.closed) {
            throw new IOException("Base64OutputStream has been closed");
        }
        if (array == null) {
            throw null;
        }
        if (array.length == 0) {
            return;
        }
        this.write0(array, 0, array.length);
    }
    
    @Override
    public final void write(final byte[] array, final int n, final int n2) throws IOException {
        if (this.closed) {
            throw new IOException("Base64OutputStream has been closed");
        }
        if (array != null) {
            if (n >= 0 && n2 >= 0) {
                final int n3 = n + n2;
                if (n3 <= array.length) {
                    if (n2 == 0) {
                        return;
                    }
                    this.write0(array, n, n3);
                    return;
                }
            }
            throw new IndexOutOfBoundsException();
        }
        throw null;
    }
}
