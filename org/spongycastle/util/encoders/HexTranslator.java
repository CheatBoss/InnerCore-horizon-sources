package org.spongycastle.util.encoders;

public class HexTranslator implements Translator
{
    private static final byte[] hexTable;
    
    static {
        hexTable = new byte[] { 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 97, 98, 99, 100, 101, 102 };
    }
    
    @Override
    public int decode(final byte[] array, final int n, int i, final byte[] array2, int n2) {
        int n3;
        int n4;
        byte b;
        byte b2;
        for (n3 = i / 2, i = 0; i < n3; ++i) {
            n4 = i * 2 + n;
            b = array[n4];
            b2 = array[n4 + 1];
            if (b < 97) {
                array2[n2] = (byte)(b - 48 << 4);
            }
            else {
                array2[n2] = (byte)(b - 97 + 10 << 4);
            }
            if (b2 < 97) {
                array2[n2] += (byte)(b2 - 48);
            }
            else {
                array2[n2] += (byte)(b2 - 97 + 10);
            }
            ++n2;
        }
        return n3;
    }
    
    @Override
    public int encode(final byte[] array, int n, final int n2, final byte[] array2, final int n3) {
        int i = 0;
        int n4 = n;
        int n5;
        byte[] hexTable;
        for (n = 0; i < n2; ++i, n += 2) {
            n5 = n3 + n;
            hexTable = HexTranslator.hexTable;
            array2[n5] = hexTable[array[n4] >> 4 & 0xF];
            array2[n5 + 1] = hexTable[array[n4] & 0xF];
            ++n4;
        }
        return n2 * 2;
    }
    
    @Override
    public int getDecodedBlockSize() {
        return 1;
    }
    
    @Override
    public int getEncodedBlockSize() {
        return 2;
    }
}
