package org.spongycastle.crypto.params;

public class DESParameters extends KeyParameter
{
    public static final int DES_KEY_LENGTH = 8;
    private static byte[] DES_weak_keys;
    private static final int N_DES_WEAK_KEYS = 16;
    
    static {
        DESParameters.DES_weak_keys = new byte[] { 1, 1, 1, 1, 1, 1, 1, 1, 31, 31, 31, 31, 14, 14, 14, 14, -32, -32, -32, -32, -15, -15, -15, -15, -2, -2, -2, -2, -2, -2, -2, -2, 1, -2, 1, -2, 1, -2, 1, -2, 31, -32, 31, -32, 14, -15, 14, -15, 1, -32, 1, -32, 1, -15, 1, -15, 31, -2, 31, -2, 14, -2, 14, -2, 1, 31, 1, 31, 1, 14, 1, 14, -32, -2, -32, -2, -15, -2, -15, -2, -2, 1, -2, 1, -2, 1, -2, 1, -32, 31, -32, 31, -15, 14, -15, 14, -32, 1, -32, 1, -15, 1, -15, 1, -2, 31, -2, 31, -2, 14, -2, 14, 31, 1, 31, 1, 14, 1, 14, 1, -2, -32, -2, -32, -2, -15, -2, -15 };
    }
    
    public DESParameters(final byte[] array) {
        super(array);
        if (!isWeakKey(array, 0)) {
            return;
        }
        throw new IllegalArgumentException("attempt to create weak DES key");
    }
    
    public static boolean isWeakKey(final byte[] array, final int n) {
        if (array.length - n >= 8) {
            int i = 0;
        Label_0011:
            while (i < 16) {
                for (int j = 0; j < 8; ++j) {
                    if (array[j + n] != DESParameters.DES_weak_keys[i * 8 + j]) {
                        ++i;
                        continue Label_0011;
                    }
                }
                return true;
            }
            return false;
        }
        throw new IllegalArgumentException("key material too short.");
    }
    
    public static void setOddParity(final byte[] array) {
        for (int i = 0; i < array.length; ++i) {
            final byte b = array[i];
            array[i] = (byte)((b & 0xFE) | ((b >> 7 ^ (b >> 1 ^ b >> 2 ^ b >> 3 ^ b >> 4 ^ b >> 5 ^ b >> 6) ^ 0x1) & 0x1));
        }
    }
}
