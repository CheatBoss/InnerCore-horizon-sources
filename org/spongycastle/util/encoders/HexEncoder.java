package org.spongycastle.util.encoders;

import java.io.*;

public class HexEncoder implements Encoder
{
    protected final byte[] decodingTable;
    protected final byte[] encodingTable;
    
    public HexEncoder() {
        this.encodingTable = new byte[] { 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 97, 98, 99, 100, 101, 102 };
        this.decodingTable = new byte[128];
        this.initialiseDecodingTable();
    }
    
    private static boolean ignore(final char c) {
        return c == '\n' || c == '\r' || c == '\t' || c == ' ';
    }
    
    @Override
    public int decode(final String s, final OutputStream outputStream) throws IOException {
        int i;
        int n;
        for (i = s.length(); i > 0; i = n) {
            n = i - 1;
            if (!ignore(s.charAt(n))) {
                break;
            }
        }
        int j = 0;
        int n2 = 0;
        while (j < i) {
            while (j < i && ignore(s.charAt(j))) {
                ++j;
            }
            final byte[] decodingTable = this.decodingTable;
            final int n3 = j + 1;
            final byte b = decodingTable[s.charAt(j)];
            int n4;
            for (n4 = n3; n4 < i && ignore(s.charAt(n4)); ++n4) {}
            final byte b2 = this.decodingTable[s.charAt(n4)];
            if ((b | b2) < 0) {
                throw new IOException("invalid characters encountered in Hex string");
            }
            outputStream.write(b << 4 | b2);
            ++n2;
            j = n4 + 1;
        }
        return n2;
    }
    
    @Override
    public int decode(final byte[] array, int i, int j, final OutputStream outputStream) throws IOException {
        int n;
        for (j += i; j > i; j = n) {
            n = j - 1;
            if (!ignore((char)array[n])) {
                break;
            }
        }
        int n2 = 0;
        while (i < j) {
            while (i < j && ignore((char)array[i])) {
                ++i;
            }
            final byte[] decodingTable = this.decodingTable;
            final int n3 = i + 1;
            final byte b = decodingTable[array[i]];
            for (i = n3; i < j && ignore((char)array[i]); ++i) {}
            final byte b2 = this.decodingTable[array[i]];
            if ((b | b2) < 0) {
                throw new IOException("invalid characters encountered in Hex data");
            }
            outputStream.write(b << 4 | b2);
            ++n2;
            ++i;
        }
        return n2;
    }
    
    @Override
    public int encode(final byte[] array, final int n, final int n2, final OutputStream outputStream) throws IOException {
        for (int i = n; i < n + n2; ++i) {
            final int n3 = array[i] & 0xFF;
            outputStream.write(this.encodingTable[n3 >>> 4]);
            outputStream.write(this.encodingTable[n3 & 0xF]);
        }
        return n2 * 2;
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
        final byte[] decodingTable2 = this.decodingTable;
        decodingTable2[65] = decodingTable2[97];
        decodingTable2[66] = decodingTable2[98];
        decodingTable2[67] = decodingTable2[99];
        decodingTable2[68] = decodingTable2[100];
        decodingTable2[69] = decodingTable2[101];
        decodingTable2[70] = decodingTable2[102];
    }
}
