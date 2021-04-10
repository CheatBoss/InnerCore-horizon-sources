package org.spongycastle.crypto.engines;

import org.spongycastle.crypto.params.*;
import org.spongycastle.crypto.*;

public class DESEngine implements BlockCipher
{
    protected static final int BLOCK_SIZE = 8;
    private static final int[] SP1;
    private static final int[] SP2;
    private static final int[] SP3;
    private static final int[] SP4;
    private static final int[] SP5;
    private static final int[] SP6;
    private static final int[] SP7;
    private static final int[] SP8;
    private static final int[] bigbyte;
    private static final short[] bytebit;
    private static final byte[] pc1;
    private static final byte[] pc2;
    private static final byte[] totrot;
    private int[] workingKey;
    
    static {
        bytebit = new short[] { 128, 64, 32, 16, 8, 4, 2, 1 };
        bigbyte = new int[] { 8388608, 4194304, 2097152, 1048576, 524288, 262144, 131072, 65536, 32768, 16384, 8192, 4096, 2048, 1024, 512, 256, 128, 64, 32, 16, 8, 4, 2, 1 };
        pc1 = new byte[] { 56, 48, 40, 32, 24, 16, 8, 0, 57, 49, 41, 33, 25, 17, 9, 1, 58, 50, 42, 34, 26, 18, 10, 2, 59, 51, 43, 35, 62, 54, 46, 38, 30, 22, 14, 6, 61, 53, 45, 37, 29, 21, 13, 5, 60, 52, 44, 36, 28, 20, 12, 4, 27, 19, 11, 3 };
        totrot = new byte[] { 1, 2, 4, 6, 8, 10, 12, 14, 15, 17, 19, 21, 23, 25, 27, 28 };
        pc2 = new byte[] { 13, 16, 10, 23, 0, 4, 2, 27, 14, 5, 20, 9, 22, 18, 11, 3, 25, 7, 15, 6, 26, 19, 12, 1, 40, 51, 30, 36, 46, 54, 29, 39, 50, 44, 32, 47, 43, 48, 38, 55, 33, 52, 45, 41, 49, 35, 28, 31 };
        SP1 = new int[] { 16843776, 0, 65536, 16843780, 16842756, 66564, 4, 65536, 1024, 16843776, 16843780, 1024, 16778244, 16842756, 16777216, 4, 1028, 16778240, 16778240, 66560, 66560, 16842752, 16842752, 16778244, 65540, 16777220, 16777220, 65540, 0, 1028, 66564, 16777216, 65536, 16843780, 4, 16842752, 16843776, 16777216, 16777216, 1024, 16842756, 65536, 66560, 16777220, 1024, 4, 16778244, 66564, 16843780, 65540, 16842752, 16778244, 16777220, 1028, 66564, 16843776, 1028, 16778240, 16778240, 0, 65540, 66560, 0, 16842756 };
        SP2 = new int[] { -2146402272, -2147450880, 32768, 1081376, 1048576, 32, -2146435040, -2147450848, -2147483616, -2146402272, -2146402304, Integer.MIN_VALUE, -2147450880, 1048576, 32, -2146435040, 1081344, 1048608, -2147450848, 0, Integer.MIN_VALUE, 32768, 1081376, -2146435072, 1048608, -2147483616, 0, 1081344, 32800, -2146402304, -2146435072, 32800, 0, 1081376, -2146435040, 1048576, -2147450848, -2146435072, -2146402304, 32768, -2146435072, -2147450880, 32, -2146402272, 1081376, 32, 32768, Integer.MIN_VALUE, 32800, -2146402304, 1048576, -2147483616, 1048608, -2147450848, -2147483616, 1048608, 1081344, 0, -2147450880, 32800, Integer.MIN_VALUE, -2146435040, -2146402272, 1081344 };
        SP3 = new int[] { 520, 134349312, 0, 134348808, 134218240, 0, 131592, 134218240, 131080, 134217736, 134217736, 131072, 134349320, 131080, 134348800, 520, 134217728, 8, 134349312, 512, 131584, 134348800, 134348808, 131592, 134218248, 131584, 131072, 134218248, 8, 134349320, 512, 134217728, 134349312, 134217728, 131080, 520, 131072, 134349312, 134218240, 0, 512, 131080, 134349320, 134218240, 134217736, 512, 0, 134348808, 134218248, 131072, 134217728, 134349320, 8, 131592, 131584, 134217736, 134348800, 134218248, 520, 134348800, 131592, 8, 134348808, 131584 };
        SP4 = new int[] { 8396801, 8321, 8321, 128, 8396928, 8388737, 8388609, 8193, 0, 8396800, 8396800, 8396929, 129, 0, 8388736, 8388609, 1, 8192, 8388608, 8396801, 128, 8388608, 8193, 8320, 8388737, 1, 8320, 8388736, 8192, 8396928, 8396929, 129, 8388736, 8388609, 8396800, 8396929, 129, 0, 0, 8396800, 8320, 8388736, 8388737, 1, 8396801, 8321, 8321, 128, 8396929, 129, 1, 8192, 8388609, 8193, 8396928, 8388737, 8193, 8320, 8388608, 8396801, 128, 8388608, 8192, 8396928 };
        SP5 = new int[] { 256, 34078976, 34078720, 1107296512, 524288, 256, 1073741824, 34078720, 1074266368, 524288, 33554688, 1074266368, 1107296512, 1107820544, 524544, 1073741824, 33554432, 1074266112, 1074266112, 0, 1073742080, 1107820800, 1107820800, 33554688, 1107820544, 1073742080, 0, 1107296256, 34078976, 33554432, 1107296256, 524544, 524288, 1107296512, 256, 33554432, 1073741824, 34078720, 1107296512, 1074266368, 33554688, 1073741824, 1107820544, 34078976, 1074266368, 256, 33554432, 1107820544, 1107820800, 524544, 1107296256, 1107820800, 34078720, 0, 1074266112, 1107296256, 524544, 33554688, 1073742080, 524288, 0, 1074266112, 34078976, 1073742080 };
        SP6 = new int[] { 536870928, 541065216, 16384, 541081616, 541065216, 16, 541081616, 4194304, 536887296, 4210704, 4194304, 536870928, 4194320, 536887296, 536870912, 16400, 0, 4194320, 536887312, 16384, 4210688, 536887312, 16, 541065232, 541065232, 0, 4210704, 541081600, 16400, 4210688, 541081600, 536870912, 536887296, 16, 541065232, 4210688, 541081616, 4194304, 16400, 536870928, 4194304, 536887296, 536870912, 16400, 536870928, 541081616, 4210688, 541065216, 4210704, 541081600, 0, 541065232, 16, 16384, 541065216, 4210704, 16384, 4194320, 536887312, 0, 541081600, 536870912, 4194320, 536887312 };
        SP7 = new int[] { 2097152, 69206018, 67110914, 0, 2048, 67110914, 2099202, 69208064, 69208066, 2097152, 0, 67108866, 2, 67108864, 69206018, 2050, 67110912, 2099202, 2097154, 67110912, 67108866, 69206016, 69208064, 2097154, 69206016, 2048, 2050, 69208066, 2099200, 2, 67108864, 2099200, 67108864, 2099200, 2097152, 67110914, 67110914, 69206018, 69206018, 2, 2097154, 67108864, 67110912, 2097152, 69208064, 2050, 2099202, 69208064, 2050, 67108866, 69208066, 69206016, 2099200, 0, 2, 69208066, 0, 2099202, 69206016, 2048, 67108866, 67110912, 2048, 2097154 };
        SP8 = new int[] { 268439616, 4096, 262144, 268701760, 268435456, 268439616, 64, 268435456, 262208, 268697600, 268701760, 266240, 268701696, 266304, 4096, 64, 268697600, 268435520, 268439552, 4160, 266240, 262208, 268697664, 268701696, 4160, 0, 0, 268697664, 268435520, 268439552, 266304, 262144, 266304, 262144, 268701696, 4096, 64, 268697664, 4096, 266304, 268439552, 64, 268435520, 268697600, 268697664, 268435456, 262144, 268439616, 0, 268701760, 262208, 268435520, 268697600, 268439552, 268439616, 0, 268701760, 266240, 266240, 4160, 4160, 262208, 268435456, 268701696 };
    }
    
    public DESEngine() {
        this.workingKey = null;
    }
    
    protected void desFunc(final int[] array, final byte[] array2, int i, final byte[] array3, final int n) {
        final int n2 = (array2[i + 0] & 0xFF) << 24 | (array2[i + 1] & 0xFF) << 16 | (array2[i + 2] & 0xFF) << 8 | (array2[i + 3] & 0xFF);
        i = ((array2[i + 7] & 0xFF) | ((array2[i + 4] & 0xFF) << 24 | (array2[i + 5] & 0xFF) << 16 | (array2[i + 6] & 0xFF) << 8));
        final int n3 = (n2 >>> 4 ^ i) & 0xF0F0F0F;
        i ^= n3;
        final int n4 = n3 << 4 ^ n2;
        final int n5 = (n4 >>> 16 ^ i) & 0xFFFF;
        i ^= n5;
        final int n6 = n4 ^ n5 << 16;
        final int n7 = (i >>> 2 ^ n6) & 0x33333333;
        final int n8 = n6 ^ n7;
        final int n9 = i ^ n7 << 2;
        final int n10 = (n9 >>> 8 ^ n8) & 0xFF00FF;
        i = (n8 ^ n10);
        final int n11 = n9 ^ n10 << 8;
        final int n12 = (n11 << 1 | (n11 >>> 31 & 0x1)) & -1;
        final int n13 = (i ^ n12) & 0xAAAAAAAA;
        i ^= n13;
        int n14 = n12 ^ n13;
        int n15 = (i << 1 | (i >>> 31 & 0x1)) & -1;
        int n16;
        int n17;
        int[] sp7;
        int n18;
        int[] sp8;
        int n19;
        int[] sp9;
        int n20;
        int[] sp10;
        int n21;
        int n22;
        int[] sp11;
        int n23;
        int[] sp12;
        int n24;
        int[] sp13;
        int n25;
        int[] sp14;
        int n26;
        int n27;
        int n28;
        int n29;
        int n30;
        int n31;
        for (i = 0; i < 8; ++i) {
            n16 = i * 4;
            n17 = ((n14 << 28 | n14 >>> 4) ^ array[n16 + 0]);
            sp7 = DESEngine.SP7;
            n18 = sp7[n17 & 0x3F];
            sp8 = DESEngine.SP5;
            n19 = sp8[n17 >>> 8 & 0x3F];
            sp9 = DESEngine.SP3;
            n20 = sp9[n17 >>> 16 & 0x3F];
            sp10 = DESEngine.SP1;
            n21 = sp10[n17 >>> 24 & 0x3F];
            n22 = (array[n16 + 1] ^ n14);
            sp11 = DESEngine.SP8;
            n23 = sp11[n22 & 0x3F];
            sp12 = DESEngine.SP6;
            n24 = sp12[n22 >>> 8 & 0x3F];
            sp13 = DESEngine.SP4;
            n25 = sp13[n22 >>> 16 & 0x3F];
            sp14 = DESEngine.SP2;
            n15 ^= (n21 | (n18 | n19 | n20) | n23 | n24 | n25 | sp14[n22 >>> 24 & 0x3F]);
            n26 = ((n15 << 28 | n15 >>> 4) ^ array[n16 + 2]);
            n27 = sp7[n26 & 0x3F];
            n28 = sp8[n26 >>> 8 & 0x3F];
            n29 = sp9[n26 >>> 16 & 0x3F];
            n30 = sp10[n26 >>> 24 & 0x3F];
            n31 = (array[n16 + 3] ^ n15);
            n14 ^= (sp14[n31 >>> 24 & 0x3F] | (n30 | (n27 | n28 | n29) | sp11[n31 & 0x3F] | sp12[n31 >>> 8 & 0x3F] | sp13[n31 >>> 16 & 0x3F]));
        }
        i = (n14 >>> 1 | n14 << 31);
        final int n32 = (n15 ^ i) & 0xAAAAAAAA;
        final int n33 = n15 ^ n32;
        i ^= n32;
        final int n34 = n33 >>> 1 | n33 << 31;
        final int n35 = (n34 >>> 8 ^ i) & 0xFF00FF;
        i ^= n35;
        final int n36 = n34 ^ n35 << 8;
        final int n37 = (n36 >>> 2 ^ i) & 0x33333333;
        i ^= n37;
        final int n38 = n36 ^ n37 << 2;
        final int n39 = (i >>> 16 ^ n38) & 0xFFFF;
        final int n40 = n38 ^ n39;
        i ^= n39 << 16;
        final int n41 = (i >>> 4 ^ n40) & 0xF0F0F0F;
        final int n42 = n40 ^ n41;
        i ^= n41 << 4;
        array3[n + 0] = (byte)(i >>> 24 & 0xFF);
        array3[n + 1] = (byte)(i >>> 16 & 0xFF);
        array3[n + 2] = (byte)(i >>> 8 & 0xFF);
        array3[n + 3] = (byte)(i & 0xFF);
        array3[n + 4] = (byte)(n42 >>> 24 & 0xFF);
        array3[n + 5] = (byte)(n42 >>> 16 & 0xFF);
        array3[n + 6] = (byte)(n42 >>> 8 & 0xFF);
        array3[n + 7] = (byte)(n42 & 0xFF);
    }
    
    protected int[] generateWorkingKey(final boolean b, final byte[] array) {
        final int[] array2 = new int[32];
        final boolean[] array3 = new boolean[56];
        final boolean[] array4 = new boolean[56];
        final int n = 0;
        int n2 = 0;
        while (true) {
            boolean b2 = true;
            if (n2 >= 56) {
                break;
            }
            final byte b3 = DESEngine.pc1[n2];
            if ((DESEngine.bytebit[b3 & 0x7] & array[b3 >>> 3]) == 0x0) {
                b2 = false;
            }
            array3[n2] = b2;
            ++n2;
        }
        int n3 = 0;
        int i;
        while (true) {
            i = n;
            if (n3 >= 16) {
                break;
            }
            int n4;
            if (b) {
                n4 = n3 << 1;
            }
            else {
                n4 = 15 - n3 << 1;
            }
            final int n5 = n4 + 1;
            array2[n4] = (array2[n5] = 0);
            int n6 = 0;
            int j;
            while (true) {
                j = 28;
                if (n6 >= 28) {
                    break;
                }
                final int n7 = DESEngine.totrot[n3] + n6;
                if (n7 < 28) {
                    array4[n6] = array3[n7];
                }
                else {
                    array4[n6] = array3[n7 - 28];
                }
                ++n6;
            }
            while (j < 56) {
                final int n8 = DESEngine.totrot[n3] + j;
                if (n8 < 56) {
                    array4[j] = array3[n8];
                }
                else {
                    array4[j] = array3[n8 - 28];
                }
                ++j;
            }
            for (int k = 0; k < 24; ++k) {
                if (array4[DESEngine.pc2[k]]) {
                    array2[n4] |= DESEngine.bigbyte[k];
                }
                if (array4[DESEngine.pc2[k + 24]]) {
                    array2[n5] |= DESEngine.bigbyte[k];
                }
            }
            ++n3;
        }
        while (i != 32) {
            final int n9 = array2[i];
            final int n10 = i + 1;
            final int n11 = array2[n10];
            array2[i] = ((0xFC0000 & n9) << 6 | (n9 & 0xFC0) << 10 | (n11 & 0xFC0000) >>> 10 | (n11 & 0xFC0) >>> 6);
            array2[n10] = ((n9 & 0x3F000) << 12 | (n9 & 0x3F) << 16 | (n11 & 0x3F000) >>> 4 | (n11 & 0x3F));
            i += 2;
        }
        return array2;
    }
    
    @Override
    public String getAlgorithmName() {
        return "DES";
    }
    
    @Override
    public int getBlockSize() {
        return 8;
    }
    
    @Override
    public void init(final boolean b, final CipherParameters cipherParameters) {
        if (!(cipherParameters instanceof KeyParameter)) {
            final StringBuilder sb = new StringBuilder();
            sb.append("invalid parameter passed to DES init - ");
            sb.append(cipherParameters.getClass().getName());
            throw new IllegalArgumentException(sb.toString());
        }
        final KeyParameter keyParameter = (KeyParameter)cipherParameters;
        if (keyParameter.getKey().length <= 8) {
            this.workingKey = this.generateWorkingKey(b, keyParameter.getKey());
            return;
        }
        throw new IllegalArgumentException("DES key too long - should be 8 bytes");
    }
    
    @Override
    public int processBlock(final byte[] array, final int n, final byte[] array2, final int n2) {
        final int[] workingKey = this.workingKey;
        if (workingKey == null) {
            throw new IllegalStateException("DES engine not initialised");
        }
        if (n + 8 > array.length) {
            throw new DataLengthException("input buffer too short");
        }
        if (n2 + 8 <= array2.length) {
            this.desFunc(workingKey, array, n, array2, n2);
            return 8;
        }
        throw new OutputLengthException("output buffer too short");
    }
    
    @Override
    public void reset() {
    }
}
