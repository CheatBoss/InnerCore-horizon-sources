package org.spongycastle.util.encoders;

import java.io.*;

public class Base64Encoder implements Encoder
{
    protected final byte[] decodingTable;
    protected final byte[] encodingTable;
    protected byte padding;
    
    public Base64Encoder() {
        this.encodingTable = new byte[] { 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 43, 47 };
        this.padding = 61;
        this.decodingTable = new byte[128];
        this.initialiseDecodingTable();
    }
    
    private int decodeLastBlock(final OutputStream outputStream, final char c, final char c2, final char c3, final char c4) throws IOException {
        final byte padding = this.padding;
        if (c3 == padding) {
            if (c4 != padding) {
                throw new IOException("invalid characters encountered at end of base64 data");
            }
            final byte[] decodingTable = this.decodingTable;
            final byte b = decodingTable[c];
            final byte b2 = decodingTable[c2];
            if ((b | b2) >= 0) {
                outputStream.write(b << 2 | b2 >> 4);
                return 1;
            }
            throw new IOException("invalid characters encountered at end of base64 data");
        }
        else if (c4 == padding) {
            final byte[] decodingTable2 = this.decodingTable;
            final byte b3 = decodingTable2[c];
            final byte b4 = decodingTable2[c2];
            final byte b5 = decodingTable2[c3];
            if ((b3 | b4 | b5) >= 0) {
                outputStream.write(b3 << 2 | b4 >> 4);
                outputStream.write(b4 << 4 | b5 >> 2);
                return 2;
            }
            throw new IOException("invalid characters encountered at end of base64 data");
        }
        else {
            final byte[] decodingTable3 = this.decodingTable;
            final byte b6 = decodingTable3[c];
            final byte b7 = decodingTable3[c2];
            final byte b8 = decodingTable3[c3];
            final byte b9 = decodingTable3[c4];
            if ((b6 | b7 | b8 | b9) >= 0) {
                outputStream.write(b6 << 2 | b7 >> 4);
                outputStream.write(b7 << 4 | b8 >> 2);
                outputStream.write(b8 << 6 | b9);
                return 3;
            }
            throw new IOException("invalid characters encountered at end of base64 data");
        }
    }
    
    private boolean ignore(final char c) {
        return c == '\n' || c == '\r' || c == '\t' || c == ' ';
    }
    
    private int nextI(final String s, int n, final int n2) {
        while (n < n2 && this.ignore(s.charAt(n))) {
            ++n;
        }
        return n;
    }
    
    private int nextI(final byte[] array, int n, final int n2) {
        while (n < n2 && this.ignore((char)array[n])) {
            ++n;
        }
        return n;
    }
    
    @Override
    public int decode(final String s, final OutputStream outputStream) throws IOException {
        int i;
        int n;
        for (i = s.length(); i > 0; i = n) {
            n = i - 1;
            if (!this.ignore(s.charAt(n))) {
                break;
            }
        }
        final int n2 = i - 4;
        int n3 = 0;
        int nextI3;
        for (int j = this.nextI(s, 0, n2); j < n2; j = this.nextI(s, nextI3 + 1, n2)) {
            final byte b = this.decodingTable[s.charAt(j)];
            final int nextI = this.nextI(s, j + 1, n2);
            final byte b2 = this.decodingTable[s.charAt(nextI)];
            final int nextI2 = this.nextI(s, nextI + 1, n2);
            final byte b3 = this.decodingTable[s.charAt(nextI2)];
            nextI3 = this.nextI(s, nextI2 + 1, n2);
            final byte b4 = this.decodingTable[s.charAt(nextI3)];
            if ((b | b2 | b3 | b4) < 0) {
                throw new IOException("invalid characters encountered in base64 data");
            }
            outputStream.write(b << 2 | b2 >> 4);
            outputStream.write(b2 << 4 | b3 >> 2);
            outputStream.write(b3 << 6 | b4);
            n3 += 3;
        }
        return n3 + this.decodeLastBlock(outputStream, s.charAt(n2), s.charAt(i - 3), s.charAt(i - 2), s.charAt(i - 1));
    }
    
    @Override
    public int decode(final byte[] array, int n, int i, final OutputStream outputStream) throws IOException {
        int n2;
        for (i += n; i > n; i = n2) {
            n2 = i - 1;
            if (!this.ignore((char)array[n2])) {
                break;
            }
        }
        final int n3 = i - 4;
        int j = this.nextI(array, n, n3);
        n = 0;
        while (j < n3) {
            final byte b = this.decodingTable[array[j]];
            final int nextI = this.nextI(array, j + 1, n3);
            final byte b2 = this.decodingTable[array[nextI]];
            final int nextI2 = this.nextI(array, nextI + 1, n3);
            final byte b3 = this.decodingTable[array[nextI2]];
            final int nextI3 = this.nextI(array, nextI2 + 1, n3);
            final byte b4 = this.decodingTable[array[nextI3]];
            if ((b | b2 | b3 | b4) < 0) {
                throw new IOException("invalid characters encountered in base64 data");
            }
            outputStream.write(b << 2 | b2 >> 4);
            outputStream.write(b2 << 4 | b3 >> 2);
            outputStream.write(b3 << 6 | b4);
            n += 3;
            j = this.nextI(array, nextI3 + 1, n3);
        }
        return n + this.decodeLastBlock(outputStream, (char)array[n3], (char)array[i - 3], (char)array[i - 2], (char)array[i - 1]);
    }
    
    @Override
    public int encode(final byte[] array, int padding, int n, final OutputStream outputStream) throws IOException {
        final int n2 = n % 3;
        final int n3 = n - n2;
        n = padding;
        int n4;
        while (true) {
            n4 = padding + n3;
            if (n >= n4) {
                break;
            }
            final int n5 = array[n] & 0xFF;
            final int n6 = array[n + 1] & 0xFF;
            final int n7 = array[n + 2] & 0xFF;
            outputStream.write(this.encodingTable[n5 >>> 2 & 0x3F]);
            outputStream.write(this.encodingTable[(n5 << 4 | n6 >>> 4) & 0x3F]);
            outputStream.write(this.encodingTable[(n6 << 2 | n7 >>> 6) & 0x3F]);
            outputStream.write(this.encodingTable[n7 & 0x3F]);
            n += 3;
        }
        Label_0287: {
            if (n2 != 1) {
                if (n2 != 2) {
                    break Label_0287;
                }
                padding = (array[n4] & 0xFF);
                n = (array[n4 + 1] & 0xFF);
                outputStream.write(this.encodingTable[padding >>> 2 & 0x3F]);
                outputStream.write(this.encodingTable[(padding << 4 | n >>> 4) & 0x3F]);
                padding = this.encodingTable[n << 2 & 0x3F];
            }
            else {
                padding = (array[n4] & 0xFF);
                outputStream.write(this.encodingTable[padding >>> 2 & 0x3F]);
                outputStream.write(this.encodingTable[padding << 4 & 0x3F]);
                padding = this.padding;
            }
            outputStream.write(padding);
            outputStream.write(this.padding);
        }
        n = n3 / 3;
        if (n2 == 0) {
            padding = 0;
        }
        else {
            padding = 4;
        }
        return n * 4 + padding;
    }
    
    protected void initialiseDecodingTable() {
        final int n = 0;
        int n2 = 0;
        int n3;
        while (true) {
            final byte[] decodingTable = this.decodingTable;
            n3 = n;
            if (n2 >= decodingTable.length) {
                break;
            }
            decodingTable[n2] = -1;
            ++n2;
        }
        while (true) {
            final byte[] encodingTable = this.encodingTable;
            if (n3 >= encodingTable.length) {
                break;
            }
            this.decodingTable[encodingTable[n3]] = (byte)n3;
            ++n3;
        }
    }
}
