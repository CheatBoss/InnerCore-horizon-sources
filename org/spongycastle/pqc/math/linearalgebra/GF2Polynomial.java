package org.spongycastle.pqc.math.linearalgebra;

import java.util.*;
import java.math.*;

public class GF2Polynomial
{
    private static final int[] bitMask;
    private static final boolean[] parity;
    private static Random rand;
    private static final int[] reverseRightMask;
    private static final short[] squaringTable;
    private int blocks;
    private int len;
    private int[] value;
    
    static {
        GF2Polynomial.rand = new Random();
        parity = new boolean[] { false, true, true, false, true, false, false, true, true, false, false, true, false, true, true, false, true, false, false, true, false, true, true, false, false, true, true, false, true, false, false, true, true, false, false, true, false, true, true, false, false, true, true, false, true, false, false, true, false, true, true, false, true, false, false, true, true, false, false, true, false, true, true, false, true, false, false, true, false, true, true, false, false, true, true, false, true, false, false, true, false, true, true, false, true, false, false, true, true, false, false, true, false, true, true, false, false, true, true, false, true, false, false, true, true, false, false, true, false, true, true, false, true, false, false, true, false, true, true, false, false, true, true, false, true, false, false, true, true, false, false, true, false, true, true, false, false, true, true, false, true, false, false, true, false, true, true, false, true, false, false, true, true, false, false, true, false, true, true, false, false, true, true, false, true, false, false, true, true, false, false, true, false, true, true, false, true, false, false, true, false, true, true, false, false, true, true, false, true, false, false, true, false, true, true, false, true, false, false, true, true, false, false, true, false, true, true, false, true, false, false, true, false, true, true, false, false, true, true, false, true, false, false, true, true, false, false, true, false, true, true, false, false, true, true, false, true, false, false, true, false, true, true, false, true, false, false, true, true, false, false, true, false, true, true, false };
        squaringTable = new short[] { 0, 1, 4, 5, 16, 17, 20, 21, 64, 65, 68, 69, 80, 81, 84, 85, 256, 257, 260, 261, 272, 273, 276, 277, 320, 321, 324, 325, 336, 337, 340, 341, 1024, 1025, 1028, 1029, 1040, 1041, 1044, 1045, 1088, 1089, 1092, 1093, 1104, 1105, 1108, 1109, 1280, 1281, 1284, 1285, 1296, 1297, 1300, 1301, 1344, 1345, 1348, 1349, 1360, 1361, 1364, 1365, 4096, 4097, 4100, 4101, 4112, 4113, 4116, 4117, 4160, 4161, 4164, 4165, 4176, 4177, 4180, 4181, 4352, 4353, 4356, 4357, 4368, 4369, 4372, 4373, 4416, 4417, 4420, 4421, 4432, 4433, 4436, 4437, 5120, 5121, 5124, 5125, 5136, 5137, 5140, 5141, 5184, 5185, 5188, 5189, 5200, 5201, 5204, 5205, 5376, 5377, 5380, 5381, 5392, 5393, 5396, 5397, 5440, 5441, 5444, 5445, 5456, 5457, 5460, 5461, 16384, 16385, 16388, 16389, 16400, 16401, 16404, 16405, 16448, 16449, 16452, 16453, 16464, 16465, 16468, 16469, 16640, 16641, 16644, 16645, 16656, 16657, 16660, 16661, 16704, 16705, 16708, 16709, 16720, 16721, 16724, 16725, 17408, 17409, 17412, 17413, 17424, 17425, 17428, 17429, 17472, 17473, 17476, 17477, 17488, 17489, 17492, 17493, 17664, 17665, 17668, 17669, 17680, 17681, 17684, 17685, 17728, 17729, 17732, 17733, 17744, 17745, 17748, 17749, 20480, 20481, 20484, 20485, 20496, 20497, 20500, 20501, 20544, 20545, 20548, 20549, 20560, 20561, 20564, 20565, 20736, 20737, 20740, 20741, 20752, 20753, 20756, 20757, 20800, 20801, 20804, 20805, 20816, 20817, 20820, 20821, 21504, 21505, 21508, 21509, 21520, 21521, 21524, 21525, 21568, 21569, 21572, 21573, 21584, 21585, 21588, 21589, 21760, 21761, 21764, 21765, 21776, 21777, 21780, 21781, 21824, 21825, 21828, 21829, 21840, 21841, 21844, 21845 };
        bitMask = new int[] { 1, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048, 4096, 8192, 16384, 32768, 65536, 131072, 262144, 524288, 1048576, 2097152, 4194304, 8388608, 16777216, 33554432, 67108864, 134217728, 268435456, 536870912, 1073741824, Integer.MIN_VALUE, 0 };
        reverseRightMask = new int[] { 0, 1, 3, 7, 15, 31, 63, 127, 255, 511, 1023, 2047, 4095, 8191, 16383, 32767, 65535, 131071, 262143, 524287, 1048575, 2097151, 4194303, 8388607, 16777215, 33554431, 67108863, 134217727, 268435455, 536870911, 1073741823, Integer.MAX_VALUE, -1 };
    }
    
    public GF2Polynomial(int blocks) {
        int len = blocks;
        if (blocks < 1) {
            len = 1;
        }
        blocks = (len - 1 >> 5) + 1;
        this.blocks = blocks;
        this.value = new int[blocks];
        this.len = len;
    }
    
    public GF2Polynomial(int blocks, final String s) {
        int len = blocks;
        if (blocks < 1) {
            len = 1;
        }
        blocks = (len - 1 >> 5) + 1;
        this.blocks = blocks;
        this.value = new int[blocks];
        this.len = len;
        if (s.equalsIgnoreCase("ZERO")) {
            this.assignZero();
            return;
        }
        if (s.equalsIgnoreCase("ONE")) {
            this.assignOne();
            return;
        }
        if (s.equalsIgnoreCase("RANDOM")) {
            this.randomize();
            return;
        }
        if (s.equalsIgnoreCase("X")) {
            this.assignX();
            return;
        }
        if (s.equalsIgnoreCase("ALL")) {
            this.assignAll();
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Error: GF2Polynomial was called using ");
        sb.append(s);
        sb.append(" as value!");
        throw new IllegalArgumentException(sb.toString());
    }
    
    public GF2Polynomial(int i, final BigInteger bigInteger) {
        int len = i;
        if (i < 1) {
            len = 1;
        }
        i = (len - 1 >> 5) + 1;
        this.blocks = i;
        this.value = new int[i];
        this.len = len;
        final byte[] byteArray = bigInteger.toByteArray();
        final int n = 0;
        byte[] array = byteArray;
        if (byteArray[0] == 0) {
            i = byteArray.length - 1;
            array = new byte[i];
            System.arraycopy(byteArray, 1, array, 0, i);
        }
        final int n2 = array.length & 0x3;
        final int length = array.length;
        int n3 = 0;
        while (true) {
            i = n;
            if (n3 >= n2) {
                break;
            }
            final int[] value = this.value;
            i = (length - 1 >> 2) + 1 - 1;
            value[i] |= (array[n3] & 0xFF) << (n2 - 1 - n3 << 3);
            ++n3;
        }
        while (i <= array.length - 4 >> 2) {
            final int n4 = array.length - 1 - (i << 2);
            final int[] value2 = this.value;
            value2[i] = (array[n4] & 0xFF);
            value2[i] |= (array[n4 - 1] << 8 & 0xFF00);
            value2[i] |= (array[n4 - 2] << 16 & 0xFF0000);
            value2[i] |= (array[n4 - 3] << 24 & 0xFF000000);
            ++i;
        }
        i = (this.len & 0x1F);
        if (i != 0) {
            final int[] value3 = this.value;
            final int n5 = this.blocks - 1;
            value3[n5] &= GF2Polynomial.reverseRightMask[i];
        }
        this.reduceN();
    }
    
    public GF2Polynomial(int blocks, final Random random) {
        int len = blocks;
        if (blocks < 1) {
            len = 1;
        }
        blocks = (len - 1 >> 5) + 1;
        this.blocks = blocks;
        this.value = new int[blocks];
        this.len = len;
        this.randomize(random);
    }
    
    public GF2Polynomial(int blocks, final byte[] array) {
        int len = blocks;
        if (blocks < 1) {
            len = 1;
        }
        blocks = (len - 1 >> 5) + 1;
        this.blocks = blocks;
        this.value = new int[blocks];
        this.len = len;
        final int min = Math.min((array.length - 1 >> 2) + 1, blocks);
        blocks = 0;
        int n;
        while (true) {
            n = min - 1;
            if (blocks >= n) {
                break;
            }
            final int n2 = array.length - (blocks << 2) - 1;
            final int[] value = this.value;
            value[blocks] = (array[n2] & 0xFF);
            value[blocks] |= (0xFF00 & array[n2 - 1] << 8);
            value[blocks] |= (0xFF0000 & array[n2 - 2] << 16);
            value[blocks] |= (array[n2 - 3] << 24 & 0xFF000000);
            ++blocks;
        }
        blocks = array.length - (n << 2) - 1;
        final int[] value2 = this.value;
        value2[n] = (array[blocks] & 0xFF);
        if (blocks > 0) {
            value2[n] |= (0xFF00 & array[blocks - 1] << 8);
        }
        if (blocks > 1) {
            final int[] value3 = this.value;
            value3[n] |= (0xFF0000 & array[blocks - 2] << 16);
        }
        if (blocks > 2) {
            final int[] value4 = this.value;
            value4[n] |= (array[blocks - 3] << 24 & 0xFF000000);
        }
        this.zeroUnusedBits();
        this.reduceN();
    }
    
    public GF2Polynomial(int min, final int[] array) {
        int len = min;
        if (min < 1) {
            len = 1;
        }
        min = (len - 1 >> 5) + 1;
        this.blocks = min;
        this.value = new int[min];
        this.len = len;
        min = Math.min(min, array.length);
        System.arraycopy(array, 0, this.value, 0, min);
        this.zeroUnusedBits();
    }
    
    public GF2Polynomial(final GF2Polynomial gf2Polynomial) {
        this.len = gf2Polynomial.len;
        this.blocks = gf2Polynomial.blocks;
        this.value = IntUtils.clone(gf2Polynomial.value);
    }
    
    private void doShiftBlocksLeft(final int n) {
        int blocks = this.blocks;
        final int[] value = this.value;
        if (blocks <= value.length) {
            while (true) {
                --blocks;
                if (blocks < n) {
                    break;
                }
                final int[] value2 = this.value;
                value2[blocks] = value2[blocks - n];
            }
            for (int i = 0; i < n; ++i) {
                this.value[i] = 0;
            }
        }
        else {
            final int[] value3 = new int[blocks];
            System.arraycopy(value, 0, value3, n, blocks - n);
            this.value = null;
            this.value = value3;
        }
    }
    
    private GF2Polynomial karaMult(GF2Polynomial karaMult) {
        final GF2Polynomial gf2Polynomial = new GF2Polynomial(this.len << 1);
        final int len = this.len;
        if (len <= 32) {
            gf2Polynomial.value = mult32(this.value[0], karaMult.value[0]);
            return gf2Polynomial;
        }
        if (len <= 64) {
            gf2Polynomial.value = mult64(this.value, karaMult.value);
            return gf2Polynomial;
        }
        if (len <= 128) {
            gf2Polynomial.value = mult128(this.value, karaMult.value);
            return gf2Polynomial;
        }
        if (len <= 256) {
            gf2Polynomial.value = mult256(this.value, karaMult.value);
            return gf2Polynomial;
        }
        if (len <= 512) {
            gf2Polynomial.value = mult512(this.value, karaMult.value);
            return gf2Polynomial;
        }
        final int n = GF2Polynomial.bitMask[IntegerFunctions.floorLog(len - 1)];
        final int n2 = (n - 1 >> 5) + 1;
        final GF2Polynomial lower = this.lower(n2);
        final GF2Polynomial upper = this.upper(n2);
        final GF2Polynomial lower2 = karaMult.lower(n2);
        final GF2Polynomial upper2 = karaMult.upper(n2);
        karaMult = upper.karaMult(upper2);
        final GF2Polynomial karaMult2 = lower.karaMult(lower2);
        lower.addToThis(upper);
        lower2.addToThis(upper2);
        final GF2Polynomial karaMult3 = lower.karaMult(lower2);
        gf2Polynomial.shiftLeftAddThis(karaMult, n << 1);
        gf2Polynomial.shiftLeftAddThis(karaMult, n);
        gf2Polynomial.shiftLeftAddThis(karaMult3, n);
        gf2Polynomial.shiftLeftAddThis(karaMult2, n);
        gf2Polynomial.addToThis(karaMult2);
        return gf2Polynomial;
    }
    
    private GF2Polynomial lower(final int n) {
        final GF2Polynomial gf2Polynomial = new GF2Polynomial(n << 5);
        System.arraycopy(this.value, 0, gf2Polynomial.value, 0, Math.min(n, this.blocks));
        return gf2Polynomial;
    }
    
    private static int[] mult128(int[] array, int[] array2) {
        final int[] array3 = new int[8];
        final int[] array4 = new int[2];
        System.arraycopy(array, 0, array4, 0, Math.min(2, array.length));
        final int[] array5 = new int[2];
        if (array.length > 2) {
            System.arraycopy(array, 2, array5, 0, Math.min(2, array.length - 2));
        }
        array = new int[2];
        System.arraycopy(array2, 0, array, 0, Math.min(2, array2.length));
        final int[] array6 = new int[2];
        if (array2.length > 2) {
            System.arraycopy(array2, 2, array6, 0, Math.min(2, array2.length - 2));
        }
        if (array5[1] == 0 && array6[1] == 0) {
            if (array5[0] != 0 || array6[0] != 0) {
                array2 = mult32(array5[0], array6[0]);
                array3[5] ^= array2[1];
                array3[4] ^= array2[0];
                array3[3] ^= array2[1];
                array3[2] ^= array2[0];
            }
        }
        else {
            array2 = mult64(array5, array6);
            array3[7] ^= array2[3];
            array3[6] ^= array2[2];
            array3[5] ^= (array2[1] ^ array2[3]);
            array3[4] ^= (array2[0] ^ array2[2]);
            array3[3] ^= array2[1];
            array3[2] ^= array2[0];
        }
        array5[0] ^= array4[0];
        array5[1] ^= array4[1];
        array6[0] ^= array[0];
        array6[1] ^= array[1];
        if (array5[1] == 0 && array6[1] == 0) {
            array2 = mult32(array5[0], array6[0]);
            array3[3] ^= array2[1];
            array3[2] ^= array2[0];
        }
        else {
            array2 = mult64(array5, array6);
            array3[5] ^= array2[3];
            array3[4] ^= array2[2];
            array3[3] ^= array2[1];
            array3[2] ^= array2[0];
        }
        if (array4[1] == 0 && array[1] == 0) {
            array = mult32(array4[0], array[0]);
            array3[3] ^= array[1];
            array3[2] ^= array[0];
            array3[1] ^= array[1];
            array3[0] ^= array[0];
            return array3;
        }
        array = mult64(array4, array);
        array3[5] ^= array[3];
        array3[4] ^= array[2];
        array3[3] ^= (array[1] ^ array[3]);
        array3[2] ^= (array[0] ^ array[2]);
        array3[1] ^= array[1];
        array3[0] ^= array[0];
        return array3;
    }
    
    private static int[] mult256(int[] mult128, int[] array) {
        final int[] array2 = new int[16];
        final int[] array3 = new int[4];
        System.arraycopy(mult128, 0, array3, 0, Math.min(4, mult128.length));
        final int[] array4 = new int[4];
        if (mult128.length > 4) {
            System.arraycopy(mult128, 4, array4, 0, Math.min(4, mult128.length - 4));
        }
        mult128 = new int[4];
        System.arraycopy(array, 0, mult128, 0, Math.min(4, array.length));
        final int[] array5 = new int[4];
        if (array.length > 4) {
            System.arraycopy(array, 4, array5, 0, Math.min(4, array.length - 4));
        }
        if (array4[3] == 0 && array4[2] == 0 && array5[3] == 0 && array5[2] == 0) {
            if (array4[1] == 0 && array5[1] == 0) {
                if (array4[0] != 0 || array5[0] != 0) {
                    array = mult32(array4[0], array5[0]);
                    array2[9] ^= array[1];
                    array2[8] ^= array[0];
                    array2[5] ^= array[1];
                    array2[4] ^= array[0];
                }
            }
            else {
                array = mult64(array4, array5);
                array2[11] ^= array[3];
                array2[10] ^= array[2];
                array2[9] ^= array[1];
                array2[8] ^= array[0];
                array2[7] ^= array[3];
                array2[6] ^= array[2];
                array2[5] ^= array[1];
                array2[4] ^= array[0];
            }
        }
        else {
            array = mult128(array4, array5);
            array2[15] ^= array[7];
            array2[14] ^= array[6];
            array2[13] ^= array[5];
            array2[12] ^= array[4];
            array2[11] ^= (array[3] ^ array[7]);
            array2[10] ^= (array[2] ^ array[6]);
            array2[9] ^= (array[1] ^ array[5]);
            array2[8] ^= (array[0] ^ array[4]);
            array2[7] ^= array[3];
            array2[6] ^= array[2];
            array2[5] ^= array[1];
            array2[4] ^= array[0];
        }
        array4[0] ^= array3[0];
        array4[1] ^= array3[1];
        array4[2] ^= array3[2];
        array4[3] ^= array3[3];
        array5[0] ^= mult128[0];
        array5[1] ^= mult128[1];
        array5[2] ^= mult128[2];
        array5[3] ^= mult128[3];
        array = mult128(array4, array5);
        array2[11] ^= array[7];
        array2[10] ^= array[6];
        array2[9] ^= array[5];
        array2[8] ^= array[4];
        array2[7] ^= array[3];
        array2[6] ^= array[2];
        array2[5] ^= array[1];
        array2[4] ^= array[0];
        mult128 = mult128(array3, mult128);
        array2[11] ^= mult128[7];
        array2[10] ^= mult128[6];
        array2[9] ^= mult128[5];
        array2[8] ^= mult128[4];
        array2[7] ^= (mult128[3] ^ mult128[7]);
        array2[6] ^= (mult128[2] ^ mult128[6]);
        array2[5] ^= (mult128[1] ^ mult128[5]);
        array2[4] ^= (mult128[0] ^ mult128[4]);
        array2[3] ^= mult128[3];
        array2[2] ^= mult128[2];
        array2[1] ^= mult128[1];
        array2[0] ^= mult128[0];
        return array2;
    }
    
    private static int[] mult32(final int n, int i) {
        final int[] array = new int[2];
        if (n != 0) {
            if (i == 0) {
                return array;
            }
            long n2 = (long)i & 0xFFFFFFFFL;
            long n3 = 0L;
            long n4;
            for (i = 1; i <= 32; ++i, n3 = n4) {
                n4 = n3;
                if ((GF2Polynomial.bitMask[i - 1] & n) != 0x0) {
                    n4 = (n3 ^ n2);
                }
                n2 <<= 1;
            }
            array[1] = (int)(n3 >>> 32);
            array[0] = (int)(n3 & 0xFFFFFFFFL);
        }
        return array;
    }
    
    private static int[] mult512(int[] mult256, int[] array) {
        final int[] array2 = new int[32];
        final int[] array3 = new int[8];
        System.arraycopy(mult256, 0, array3, 0, Math.min(8, mult256.length));
        final int[] array4 = new int[8];
        if (mult256.length > 8) {
            System.arraycopy(mult256, 8, array4, 0, Math.min(8, mult256.length - 8));
        }
        mult256 = new int[8];
        System.arraycopy(array, 0, mult256, 0, Math.min(8, array.length));
        final int[] array5 = new int[8];
        if (array.length > 8) {
            System.arraycopy(array, 8, array5, 0, Math.min(8, array.length - 8));
        }
        array = mult256(array4, array5);
        array2[31] ^= array[15];
        array2[30] ^= array[14];
        array2[29] ^= array[13];
        array2[28] ^= array[12];
        array2[27] ^= array[11];
        array2[26] ^= array[10];
        array2[25] ^= array[9];
        array2[24] ^= array[8];
        array2[23] ^= (array[7] ^ array[15]);
        array2[22] ^= (array[6] ^ array[14]);
        array2[21] ^= (array[5] ^ array[13]);
        array2[20] ^= (array[4] ^ array[12]);
        array2[19] ^= (array[3] ^ array[11]);
        array2[18] ^= (array[2] ^ array[10]);
        array2[17] ^= (array[1] ^ array[9]);
        array2[16] ^= (array[0] ^ array[8]);
        array2[15] ^= array[7];
        array2[14] ^= array[6];
        array2[13] ^= array[5];
        array2[12] ^= array[4];
        array2[11] ^= array[3];
        array2[10] ^= array[2];
        array2[9] ^= array[1];
        array2[8] ^= array[0];
        array4[0] ^= array3[0];
        array4[1] ^= array3[1];
        array4[2] ^= array3[2];
        array4[3] ^= array3[3];
        array4[4] ^= array3[4];
        array4[5] ^= array3[5];
        array4[6] ^= array3[6];
        array4[7] ^= array3[7];
        array5[0] ^= mult256[0];
        array5[1] ^= mult256[1];
        array5[2] ^= mult256[2];
        array5[3] ^= mult256[3];
        array5[4] ^= mult256[4];
        array5[5] ^= mult256[5];
        array5[6] ^= mult256[6];
        array5[7] ^= mult256[7];
        array = mult256(array4, array5);
        array2[23] ^= array[15];
        array2[22] ^= array[14];
        array2[21] ^= array[13];
        array2[20] ^= array[12];
        array2[19] ^= array[11];
        array2[18] ^= array[10];
        array2[17] ^= array[9];
        array2[16] ^= array[8];
        array2[15] ^= array[7];
        array2[14] ^= array[6];
        array2[13] ^= array[5];
        array2[12] ^= array[4];
        array2[11] ^= array[3];
        array2[10] ^= array[2];
        array2[9] ^= array[1];
        array2[8] ^= array[0];
        mult256 = mult256(array3, mult256);
        array2[23] ^= mult256[15];
        array2[22] ^= mult256[14];
        array2[21] ^= mult256[13];
        array2[20] ^= mult256[12];
        array2[19] ^= mult256[11];
        array2[18] ^= mult256[10];
        array2[17] ^= mult256[9];
        array2[16] ^= mult256[8];
        array2[15] ^= (mult256[7] ^ mult256[15]);
        array2[14] ^= (mult256[6] ^ mult256[14]);
        array2[13] ^= (mult256[5] ^ mult256[13]);
        array2[12] ^= (mult256[4] ^ mult256[12]);
        array2[11] ^= (mult256[3] ^ mult256[11]);
        array2[10] ^= (mult256[2] ^ mult256[10]);
        array2[9] ^= (mult256[1] ^ mult256[9]);
        array2[8] ^= (mult256[0] ^ mult256[8]);
        array2[7] ^= mult256[7];
        array2[6] ^= mult256[6];
        array2[5] ^= mult256[5];
        array2[4] ^= mult256[4];
        array2[3] ^= mult256[3];
        array2[2] ^= mult256[2];
        array2[1] ^= mult256[1];
        array2[0] ^= mult256[0];
        return array2;
    }
    
    private static int[] mult64(int[] array, final int[] array2) {
        final int[] array3 = new int[4];
        final int n = array[0];
        int n2;
        if (array.length > 1) {
            n2 = array[1];
        }
        else {
            n2 = 0;
        }
        final int n3 = array2[0];
        int n4;
        if (array2.length > 1) {
            n4 = array2[1];
        }
        else {
            n4 = 0;
        }
        if (n2 != 0 || n4 != 0) {
            array = mult32(n2, n4);
            array3[3] ^= array[1];
            array3[2] ^= (array[0] ^ array[1]);
            array3[1] ^= array[0];
        }
        array = mult32(n2 ^ n, n4 ^ n3);
        array3[2] ^= array[1];
        array3[1] ^= array[0];
        array = mult32(n, n3);
        array3[2] ^= array[1];
        array3[1] ^= (array[0] ^ array[1]);
        array3[0] ^= array[0];
        return array3;
    }
    
    private GF2Polynomial upper(final int n) {
        final int min = Math.min(n, this.blocks - n);
        final GF2Polynomial gf2Polynomial = new GF2Polynomial(min << 5);
        if (this.blocks >= n) {
            System.arraycopy(this.value, n, gf2Polynomial.value, 0, min);
        }
        return gf2Polynomial;
    }
    
    private void zeroUnusedBits() {
        final int n = this.len & 0x1F;
        if (n != 0) {
            final int[] value = this.value;
            final int n2 = this.blocks - 1;
            value[n2] &= GF2Polynomial.reverseRightMask[n];
        }
    }
    
    public GF2Polynomial add(final GF2Polynomial gf2Polynomial) {
        return this.xor(gf2Polynomial);
    }
    
    public void addToThis(final GF2Polynomial gf2Polynomial) {
        this.expandN(gf2Polynomial.len);
        this.xorThisBy(gf2Polynomial);
    }
    
    public void assignAll() {
        for (int i = 0; i < this.blocks; ++i) {
            this.value[i] = -1;
        }
        this.zeroUnusedBits();
    }
    
    public void assignOne() {
        for (int i = 1; i < this.blocks; ++i) {
            this.value[i] = 0;
        }
        this.value[0] = 1;
    }
    
    public void assignX() {
        for (int i = 1; i < this.blocks; ++i) {
            this.value[i] = 0;
        }
        this.value[0] = 2;
    }
    
    public void assignZero() {
        for (int i = 0; i < this.blocks; ++i) {
            this.value[i] = 0;
        }
    }
    
    public Object clone() {
        return new GF2Polynomial(this);
    }
    
    public GF2Polynomial[] divide(GF2Polynomial gf2Polynomial) throws RuntimeException {
        final GF2Polynomial[] array = new GF2Polynomial[2];
        final GF2Polynomial gf2Polynomial2 = new GF2Polynomial(this.len);
        final GF2Polynomial gf2Polynomial3 = new GF2Polynomial(this);
        gf2Polynomial = new GF2Polynomial(gf2Polynomial);
        if (gf2Polynomial.isZero()) {
            throw new RuntimeException();
        }
        gf2Polynomial3.reduceN();
        gf2Polynomial.reduceN();
        final int len = gf2Polynomial3.len;
        final int len2 = gf2Polynomial.len;
        if (len < len2) {
            array[0] = new GF2Polynomial(0);
            array[1] = gf2Polynomial3;
            return array;
        }
        int i = len - len2;
        gf2Polynomial2.expandN(i + 1);
        while (i >= 0) {
            gf2Polynomial3.subtractFromThis(gf2Polynomial.shiftLeft(i));
            gf2Polynomial3.reduceN();
            gf2Polynomial2.xorBit(i);
            i = gf2Polynomial3.len - gf2Polynomial.len;
        }
        array[0] = gf2Polynomial2;
        array[1] = gf2Polynomial3;
        return array;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == null) {
            return false;
        }
        if (!(o instanceof GF2Polynomial)) {
            return false;
        }
        final GF2Polynomial gf2Polynomial = (GF2Polynomial)o;
        if (this.len != gf2Polynomial.len) {
            return false;
        }
        for (int i = 0; i < this.blocks; ++i) {
            if (this.value[i] != gf2Polynomial.value[i]) {
                return false;
            }
        }
        return true;
    }
    
    public void expandN(int i) {
        if (this.len >= i) {
            return;
        }
        this.len = i;
        final int n = (i - 1 >>> 5) + 1;
        i = this.blocks;
        if (i >= n) {
            return;
        }
        final int[] value = this.value;
        if (value.length >= n) {
            while (i < n) {
                this.value[i] = 0;
                ++i;
            }
            this.blocks = n;
            return;
        }
        final int[] value2 = new int[n];
        System.arraycopy(value, 0, value2, 0, i);
        this.blocks = n;
        this.value = null;
        this.value = value2;
    }
    
    public GF2Polynomial gcd(GF2Polynomial gf2Polynomial) throws RuntimeException {
        if (this.isZero() && gf2Polynomial.isZero()) {
            throw new ArithmeticException("Both operands of gcd equal zero.");
        }
        if (this.isZero()) {
            return new GF2Polynomial(gf2Polynomial);
        }
        if (gf2Polynomial.isZero()) {
            return new GF2Polynomial(this);
        }
        GF2Polynomial gf2Polynomial2 = new GF2Polynomial(this);
        GF2Polynomial remainder;
        for (gf2Polynomial = new GF2Polynomial(gf2Polynomial); !gf2Polynomial.isZero(); gf2Polynomial = remainder) {
            remainder = gf2Polynomial2.remainder(gf2Polynomial);
            gf2Polynomial2 = gf2Polynomial;
        }
        return gf2Polynomial2;
    }
    
    public int getBit(final int n) {
        if (n < 0) {
            throw new RuntimeException();
        }
        if (n > this.len - 1) {
            return 0;
        }
        if ((GF2Polynomial.bitMask[n & 0x1F] & this.value[n >>> 5]) != 0x0) {
            return 1;
        }
        return 0;
    }
    
    public int getLength() {
        return this.len;
    }
    
    @Override
    public int hashCode() {
        return this.len + this.value.hashCode();
    }
    
    public GF2Polynomial increase() {
        final GF2Polynomial gf2Polynomial = new GF2Polynomial(this);
        gf2Polynomial.increaseThis();
        return gf2Polynomial;
    }
    
    public void increaseThis() {
        this.xorBit(0);
    }
    
    public boolean isIrreducible() {
        if (this.isZero()) {
            return false;
        }
        final GF2Polynomial gf2Polynomial = new GF2Polynomial(this);
        gf2Polynomial.reduceN();
        final int len = gf2Polynomial.len;
        GF2Polynomial remainder = new GF2Polynomial(gf2Polynomial.len, "X");
        for (int i = 1; i <= len - 1 >> 1; ++i) {
            remainder.squareThisPreCalc();
            remainder = remainder.remainder(gf2Polynomial);
            final GF2Polynomial add = remainder.add(new GF2Polynomial(32, "X"));
            if (add.isZero()) {
                return false;
            }
            if (!gf2Polynomial.gcd(add).isOne()) {
                return false;
            }
        }
        return true;
    }
    
    public boolean isOne() {
        for (int i = 1; i < this.blocks; ++i) {
            if (this.value[i] != 0) {
                return false;
            }
        }
        return this.value[0] == 1;
    }
    
    public boolean isZero() {
        if (this.len == 0) {
            return true;
        }
        for (int i = 0; i < this.blocks; ++i) {
            if (this.value[i] != 0) {
                return false;
            }
        }
        return true;
    }
    
    public GF2Polynomial multiply(final GF2Polynomial gf2Polynomial) {
        final int max = Math.max(this.len, gf2Polynomial.len);
        this.expandN(max);
        gf2Polynomial.expandN(max);
        return this.karaMult(gf2Polynomial);
    }
    
    public GF2Polynomial multiplyClassic(final GF2Polynomial gf2Polynomial) {
        final int max = Math.max(this.len, gf2Polynomial.len);
        int i = 1;
        final GF2Polynomial gf2Polynomial2 = new GF2Polynomial(max << 1);
        final GF2Polynomial[] array = new GF2Polynomial[32];
        array[0] = new GF2Polynomial(this);
        while (i <= 31) {
            array[i] = array[i - 1].shiftLeft();
            ++i;
        }
        for (int j = 0; j < gf2Polynomial.blocks; ++j) {
            for (int k = 0; k <= 31; ++k) {
                if ((gf2Polynomial.value[j] & GF2Polynomial.bitMask[k]) != 0x0) {
                    gf2Polynomial2.xorThisBy(array[k]);
                }
            }
            for (int l = 0; l <= 31; ++l) {
                array[l].shiftBlocksLeft();
            }
        }
        return gf2Polynomial2;
    }
    
    public GF2Polynomial quotient(GF2Polynomial gf2Polynomial) throws RuntimeException {
        final GF2Polynomial gf2Polynomial2 = new GF2Polynomial(this.len);
        final GF2Polynomial gf2Polynomial3 = new GF2Polynomial(this);
        gf2Polynomial = new GF2Polynomial(gf2Polynomial);
        if (gf2Polynomial.isZero()) {
            throw new RuntimeException();
        }
        gf2Polynomial3.reduceN();
        gf2Polynomial.reduceN();
        final int len = gf2Polynomial3.len;
        final int len2 = gf2Polynomial.len;
        if (len < len2) {
            return new GF2Polynomial(0);
        }
        int i = len - len2;
        gf2Polynomial2.expandN(i + 1);
        while (i >= 0) {
            gf2Polynomial3.subtractFromThis(gf2Polynomial.shiftLeft(i));
            gf2Polynomial3.reduceN();
            gf2Polynomial2.xorBit(i);
            i = gf2Polynomial3.len - gf2Polynomial.len;
        }
        return gf2Polynomial2;
    }
    
    public void randomize() {
        for (int i = 0; i < this.blocks; ++i) {
            this.value[i] = GF2Polynomial.rand.nextInt();
        }
        this.zeroUnusedBits();
    }
    
    public void randomize(final Random random) {
        for (int i = 0; i < this.blocks; ++i) {
            this.value[i] = random.nextInt();
        }
        this.zeroUnusedBits();
    }
    
    public void reduceN() {
        int blocks = this.blocks;
        int n;
        while (true) {
            n = blocks - 1;
            if (this.value[n] != 0 || n <= 0) {
                break;
            }
            blocks = n;
        }
        int i;
        int n2;
        for (i = this.value[n], n2 = 0; i != 0; i >>>= 1, ++n2) {}
        this.len = (n << 5) + n2;
        this.blocks = n + 1;
    }
    
    void reducePentanomial(final int len, int[] value) {
        final int n = len >>> 5;
        final int n2 = len & 0x1F;
        final int n3 = 32 - n2;
        final int n4 = len - value[0] >>> 5;
        final int n5 = 32 - (len - value[0] & 0x1F);
        final int n6 = len - value[1] >>> 5;
        final int n7 = 32 - (len - value[1] & 0x1F);
        final int n8 = len - value[2] >>> 5;
        final int n9 = 32 - (len - value[2] & 0x1F);
        for (int i = (len << 1) - 2 >>> 5; i > n; --i) {
            value = this.value;
            final long n10 = (long)value[i] & 0xFFFFFFFFL;
            final int n11 = i - n;
            final int n12 = n11 - 1;
            value[n12] ^= (int)(n10 << n3);
            value[n11] = (int)((long)value[n11] ^ n10 >>> 32 - n3);
            final int n13 = i - n4;
            final int n14 = n13 - 1;
            value[n14] ^= (int)(n10 << n5);
            value[n13] = (int)((long)value[n13] ^ n10 >>> 32 - n5);
            final int n15 = i - n6;
            final int n16 = n15 - 1;
            value[n16] ^= (int)(n10 << n7);
            value[n15] = (int)((long)value[n15] ^ n10 >>> 32 - n7);
            final int n17 = i - n8;
            final int n18 = n17 - 1;
            value[n18] ^= (int)(n10 << n9);
            value[n17] = (int)(n10 >>> 32 - n9 ^ (long)value[n17]);
            value[i] = 0;
        }
        final int[] value2 = this.value;
        final long n19 = (long)value2[n] & 0xFFFFFFFFL & 4294967295L << n2;
        value2[0] = (int)(n19 >>> 32 - n3 ^ (long)value2[0]);
        final int n20 = n - n4;
        final int n21 = n20 - 1;
        if (n21 >= 0) {
            value2[n21] ^= (int)(n19 << n5);
        }
        final int[] value3 = this.value;
        value3[n20] = (int)((long)value3[n20] ^ n19 >>> 32 - n5);
        final int n22 = n - n6;
        final int n23 = n22 - 1;
        if (n23 >= 0) {
            value3[n23] ^= (int)(n19 << n7);
        }
        final int[] value4 = this.value;
        value4[n22] = (int)((long)value4[n22] ^ n19 >>> 32 - n7);
        final int n24 = n - n8;
        final int n25 = n24 - 1;
        if (n25 >= 0) {
            value4[n25] ^= (int)(n19 << n9);
        }
        final int[] value5 = this.value;
        value5[n24] = (int)(n19 >>> 32 - n9 ^ (long)value5[n24]);
        value5[n] &= GF2Polynomial.reverseRightMask[n2];
        this.blocks = (len - 1 >>> 5) + 1;
        this.len = len;
    }
    
    void reduceTrinomial(final int len, int n) {
        final int n2 = len >>> 5;
        final int n3 = len & 0x1F;
        final int n4 = 32 - n3;
        n = len - n;
        final int n5 = n >>> 5;
        final int n6 = 32 - (n & 0x1F);
        int i;
        int[] value;
        long n7;
        int n8;
        int n9;
        int n10;
        int n11;
        for (i = (len << 1) - 2 >>> 5, n = n2; i > n; --i) {
            value = this.value;
            n7 = ((long)value[i] & 0xFFFFFFFFL);
            n8 = i - n;
            n9 = n8 - 1;
            value[n9] ^= (int)(n7 << n4);
            value[n8] = (int)((long)value[n8] ^ n7 >>> 32 - n4);
            n10 = i - n5;
            n11 = n10 - 1;
            value[n11] ^= (int)(n7 << n6);
            value[n10] = (int)(n7 >>> 32 - n6 ^ (long)value[n10]);
            value[i] = 0;
        }
        final int[] value2 = this.value;
        final long n12 = 4294967295L << n3 & ((long)value2[n] & 0xFFFFFFFFL);
        value2[0] = (int)((long)value2[0] ^ n12 >>> 32 - n4);
        final int n13 = n - n5;
        final int n14 = n13 - 1;
        if (n14 >= 0) {
            value2[n14] ^= (int)(n12 << n6);
        }
        final int[] value3 = this.value;
        value3[n13] = (int)(n12 >>> 32 - n6 ^ (long)value3[n13]);
        value3[n] &= GF2Polynomial.reverseRightMask[n3];
        this.blocks = (len - 1 >>> 5) + 1;
        this.len = len;
    }
    
    public GF2Polynomial remainder(GF2Polynomial gf2Polynomial) throws RuntimeException {
        final GF2Polynomial gf2Polynomial2 = new GF2Polynomial(this);
        gf2Polynomial = new GF2Polynomial(gf2Polynomial);
        if (gf2Polynomial.isZero()) {
            throw new RuntimeException();
        }
        gf2Polynomial2.reduceN();
        gf2Polynomial.reduceN();
        int n;
        int n2;
        if ((n = gf2Polynomial2.len) < (n2 = gf2Polynomial.len)) {
            return gf2Polynomial2;
        }
        while (true) {
            final int n3 = n - n2;
            if (n3 < 0) {
                break;
            }
            gf2Polynomial2.subtractFromThis(gf2Polynomial.shiftLeft(n3));
            gf2Polynomial2.reduceN();
            n = gf2Polynomial2.len;
            n2 = gf2Polynomial.len;
        }
        return gf2Polynomial2;
    }
    
    public void resetBit(final int n) throws RuntimeException {
        if (n < 0) {
            throw new RuntimeException();
        }
        if (n > this.len - 1) {
            return;
        }
        final int[] value = this.value;
        final int n2 = n >>> 5;
        value[n2] &= ~GF2Polynomial.bitMask[n & 0x1F];
    }
    
    public void setBit(final int n) throws RuntimeException {
        if (n >= 0 && n <= this.len - 1) {
            final int[] value = this.value;
            final int n2 = n >>> 5;
            value[n2] |= GF2Polynomial.bitMask[n & 0x1F];
            return;
        }
        throw new RuntimeException();
    }
    
    void shiftBlocksLeft() {
        final int blocks = this.blocks + 1;
        this.blocks = blocks;
        this.len += 32;
        final int[] value = this.value;
        if (blocks <= value.length) {
            int n;
            for (int i = blocks - 1; i >= 1; i = n) {
                final int[] value2 = this.value;
                n = i - 1;
                value2[i] = value2[n];
            }
            this.value[0] = 0;
            return;
        }
        final int[] value3 = new int[blocks];
        System.arraycopy(value, 0, value3, 1, blocks - 1);
        this.value = null;
        this.value = value3;
    }
    
    public GF2Polynomial shiftLeft() {
        final GF2Polynomial gf2Polynomial = new GF2Polynomial(this.len + 1, this.value);
        int n2;
        for (int i = gf2Polynomial.blocks - 1; i >= 1; i = n2) {
            final int[] value = gf2Polynomial.value;
            value[i] <<= 1;
            final int n = value[i];
            n2 = i - 1;
            value[i] = (n | value[n2] >>> 31);
        }
        final int[] value2 = gf2Polynomial.value;
        value2[0] <<= 1;
        return gf2Polynomial;
    }
    
    public GF2Polynomial shiftLeft(int i) {
        final GF2Polynomial gf2Polynomial = new GF2Polynomial(this.len + i, this.value);
        if (i >= 32) {
            gf2Polynomial.doShiftBlocksLeft(i >>> 5);
        }
        final int n = i & 0x1F;
        if (n != 0) {
            int[] value;
            int n2;
            int n3;
            for (i = gf2Polynomial.blocks - 1; i >= 1; i = n3) {
                value = gf2Polynomial.value;
                value[i] <<= n;
                n2 = value[i];
                n3 = i - 1;
                value[i] = (n2 | value[n3] >>> 32 - n);
            }
            final int[] value2 = gf2Polynomial.value;
            value2[0] <<= n;
        }
        return gf2Polynomial;
    }
    
    public void shiftLeftAddThis(final GF2Polynomial gf2Polynomial, final int n) {
        if (n == 0) {
            this.addToThis(gf2Polynomial);
            return;
        }
        this.expandN(gf2Polynomial.len + n);
        int blocks = gf2Polynomial.blocks;
        while (true) {
            --blocks;
            if (blocks < 0) {
                break;
            }
            final int n2 = (n >>> 5) + blocks;
            final int n3 = n2 + 1;
            if (n3 < this.blocks) {
                final int n4 = n & 0x1F;
                if (n4 != 0) {
                    final int[] value = this.value;
                    value[n3] ^= gf2Polynomial.value[blocks] >>> 32 - n4;
                }
            }
            final int[] value2 = this.value;
            value2[n2] ^= gf2Polynomial.value[blocks] << (n & 0x1F);
        }
    }
    
    public void shiftLeftThis() {
        final int len = this.len;
        if ((len & 0x1F) == 0x0) {
            this.len = len + 1;
            final int blocks = this.blocks + 1;
            this.blocks = blocks;
            final int[] value = this.value;
            if (blocks > value.length) {
                final int[] value2 = new int[blocks];
                System.arraycopy(value, 0, value2, 0, value.length);
                this.value = null;
                this.value = value2;
            }
            int n2;
            for (int i = this.blocks - 1; i >= 1; i = n2) {
                final int[] value3 = this.value;
                final int n = value3[i];
                n2 = i - 1;
                value3[i] = (n | value3[n2] >>> 31);
                value3[n2] <<= 1;
            }
        }
        else {
            this.len = len + 1;
            int n4;
            for (int j = this.blocks - 1; j >= 1; j = n4) {
                final int[] value4 = this.value;
                value4[j] <<= 1;
                final int n3 = value4[j];
                n4 = j - 1;
                value4[j] = (n3 | value4[n4] >>> 31);
            }
            final int[] value5 = this.value;
            value5[0] <<= 1;
        }
    }
    
    public GF2Polynomial shiftRight() {
        final GF2Polynomial gf2Polynomial = new GF2Polynomial(this.len - 1);
        final int[] value = this.value;
        final int[] value2 = gf2Polynomial.value;
        final int blocks = gf2Polynomial.blocks;
        int n = 0;
        System.arraycopy(value, 0, value2, 0, blocks);
        int blocks2;
        while (true) {
            blocks2 = gf2Polynomial.blocks;
            if (n > blocks2 - 2) {
                break;
            }
            final int[] value3 = gf2Polynomial.value;
            value3[n] >>>= 1;
            final int n2 = value3[n];
            final int n3 = n + 1;
            value3[n] = (n2 | value3[n3] << 31);
            n = n3;
        }
        final int[] value4 = gf2Polynomial.value;
        final int n4 = blocks2 - 1;
        value4[n4] >>>= 1;
        if (blocks2 < this.blocks) {
            value4[n4] |= this.value[blocks2] << 31;
        }
        return gf2Polynomial;
    }
    
    public void shiftRightThis() {
        final int len = this.len - 1;
        this.len = len;
        this.blocks = (len - 1 >>> 5) + 1;
        int n = 0;
        int blocks;
        while (true) {
            blocks = this.blocks;
            if (n > blocks - 2) {
                break;
            }
            final int[] value = this.value;
            value[n] >>>= 1;
            final int n2 = value[n];
            final int n3 = n + 1;
            value[n] = (n2 | value[n3] << 31);
            n = n3;
        }
        final int[] value2 = this.value;
        final int n4 = blocks - 1;
        value2[n4] >>>= 1;
        if ((this.len & 0x1F) == 0x0) {
            value2[n4] |= value2[blocks] << 31;
        }
    }
    
    public void squareThisBitwise() {
        if (this.isZero()) {
            return;
        }
        final int blocks = this.blocks;
        final int blocks2 = blocks << 1;
        final int[] value = new int[blocks2];
        for (int i = blocks - 1; i >= 0; --i) {
            int n = this.value[i];
            int j = 0;
            int n2 = 1;
            while (j < 16) {
                if ((n & 0x1) != 0x0) {
                    final int n3 = i << 1;
                    value[n3] |= n2;
                }
                if ((0x10000 & n) != 0x0) {
                    final int n4 = (i << 1) + 1;
                    value[n4] |= n2;
                }
                n2 <<= 2;
                n >>>= 1;
                ++j;
            }
        }
        this.value = null;
        this.value = value;
        this.blocks = blocks2;
        this.len = (this.len << 1) - 1;
    }
    
    public void squareThisPreCalc() {
        if (this.isZero()) {
            return;
        }
        final int length = this.value.length;
        int blocks = this.blocks;
        final int n = blocks << 1;
        int blocks2;
        if (length >= n) {
            while (true) {
                --blocks;
                if (blocks < 0) {
                    break;
                }
                final int[] value = this.value;
                final int n2 = blocks << 1;
                final short[] squaringTable = GF2Polynomial.squaringTable;
                value[n2 + 1] = (squaringTable[(value[blocks] & 0xFF0000) >>> 16] | squaringTable[(value[blocks] & 0xFF000000) >>> 24] << 16);
                value[n2] = (squaringTable[(value[blocks] & 0xFF00) >>> 8] << 16 | squaringTable[value[blocks] & 0xFF]);
            }
            blocks2 = this.blocks << 1;
        }
        else {
            final int[] value2 = new int[n];
            int n3 = 0;
            int blocks3;
            while (true) {
                blocks3 = this.blocks;
                if (n3 >= blocks3) {
                    break;
                }
                final int n4 = n3 << 1;
                final short[] squaringTable2 = GF2Polynomial.squaringTable;
                final int[] value3 = this.value;
                value2[n4] = (squaringTable2[value3[n3] & 0xFF] | squaringTable2[(value3[n3] & 0xFF00) >>> 8] << 16);
                value2[n4 + 1] = (squaringTable2[(value3[n3] & 0xFF000000) >>> 24] << 16 | squaringTable2[(value3[n3] & 0xFF0000) >>> 16]);
                ++n3;
            }
            this.value = null;
            this.value = value2;
            blocks2 = blocks3 << 1;
        }
        this.blocks = blocks2;
        this.len = (this.len << 1) - 1;
    }
    
    public GF2Polynomial subtract(final GF2Polynomial gf2Polynomial) {
        return this.xor(gf2Polynomial);
    }
    
    public void subtractFromThis(final GF2Polynomial gf2Polynomial) {
        this.expandN(gf2Polynomial.len);
        this.xorThisBy(gf2Polynomial);
    }
    
    public boolean testBit(final int n) {
        if (n >= 0) {
            return n <= this.len - 1 && (GF2Polynomial.bitMask[n & 0x1F] & this.value[n >>> 5]) != 0x0;
        }
        throw new RuntimeException();
    }
    
    public byte[] toByteArray() {
        final int n = (this.len - 1 >> 3) + 1;
        final int n2 = n & 0x3;
        final byte[] array = new byte[n];
        final int n3 = 0;
        int n4 = 0;
        int i;
        while (true) {
            i = n3;
            if (n4 >= n >> 2) {
                break;
            }
            final int n5 = n - (n4 << 2) - 1;
            final int[] value = this.value;
            array[n5] = (byte)(0xFF & value[n4]);
            array[n5 - 1] = (byte)((value[n4] & 0xFF00) >>> 8);
            array[n5 - 2] = (byte)((value[n4] & 0xFF0000) >>> 16);
            array[n5 - 3] = (byte)((value[n4] & 0xFF000000) >>> 24);
            ++n4;
        }
        while (i < n2) {
            final int n6 = n2 - i - 1 << 3;
            array[i] = (byte)((this.value[this.blocks - 1] & 255 << n6) >>> n6);
            ++i;
        }
        return array;
    }
    
    public BigInteger toFlexiBigInt() {
        if (this.len != 0 && !this.isZero()) {
            return new BigInteger(1, this.toByteArray());
        }
        return new BigInteger(0, new byte[0]);
    }
    
    public int[] toIntegerArray() {
        final int blocks = this.blocks;
        final int[] array = new int[blocks];
        System.arraycopy(this.value, 0, array, 0, blocks);
        return array;
    }
    
    public String toString(int n) {
        final char[] array2;
        final char[] array = array2 = new char[16];
        array2[0] = '0';
        array2[1] = '1';
        array2[2] = '2';
        array2[3] = '3';
        array2[4] = '4';
        array2[5] = '5';
        array2[6] = '6';
        array2[7] = '7';
        array2[8] = '8';
        array2[9] = '9';
        array2[10] = 'a';
        array2[11] = 'b';
        array2[12] = 'c';
        array2[13] = 'd';
        array2[14] = 'e';
        array2[15] = 'f';
        final String[] array3 = { "0000", "0001", "0010", "0011", "0100", "0101", "0110", "0111", "1000", "1001", "1010", "1011", "1100", "1101", "1110", "1111" };
        String s = new String();
        String s2;
        if (n == 16) {
            n = this.blocks - 1;
            while (true) {
                s2 = s;
                if (n < 0) {
                    break;
                }
                final StringBuilder sb = new StringBuilder();
                sb.append(s);
                sb.append(array[this.value[n] >>> 28 & 0xF]);
                final String string = sb.toString();
                final StringBuilder sb2 = new StringBuilder();
                sb2.append(string);
                sb2.append(array[this.value[n] >>> 24 & 0xF]);
                final String string2 = sb2.toString();
                final StringBuilder sb3 = new StringBuilder();
                sb3.append(string2);
                sb3.append(array[this.value[n] >>> 20 & 0xF]);
                final String string3 = sb3.toString();
                final StringBuilder sb4 = new StringBuilder();
                sb4.append(string3);
                sb4.append(array[this.value[n] >>> 16 & 0xF]);
                final String string4 = sb4.toString();
                final StringBuilder sb5 = new StringBuilder();
                sb5.append(string4);
                sb5.append(array[this.value[n] >>> 12 & 0xF]);
                final String string5 = sb5.toString();
                final StringBuilder sb6 = new StringBuilder();
                sb6.append(string5);
                sb6.append(array[this.value[n] >>> 8 & 0xF]);
                final String string6 = sb6.toString();
                final StringBuilder sb7 = new StringBuilder();
                sb7.append(string6);
                sb7.append(array[this.value[n] >>> 4 & 0xF]);
                final String string7 = sb7.toString();
                final StringBuilder sb8 = new StringBuilder();
                sb8.append(string7);
                sb8.append(array[this.value[n] & 0xF]);
                final String string8 = sb8.toString();
                final StringBuilder sb9 = new StringBuilder();
                sb9.append(string8);
                sb9.append(" ");
                s = sb9.toString();
                --n;
            }
        }
        else {
            n = this.blocks - 1;
            while (true) {
                s2 = s;
                if (n < 0) {
                    break;
                }
                final StringBuilder sb10 = new StringBuilder();
                sb10.append(s);
                sb10.append(array3[this.value[n] >>> 28 & 0xF]);
                final String string9 = sb10.toString();
                final StringBuilder sb11 = new StringBuilder();
                sb11.append(string9);
                sb11.append(array3[this.value[n] >>> 24 & 0xF]);
                final String string10 = sb11.toString();
                final StringBuilder sb12 = new StringBuilder();
                sb12.append(string10);
                sb12.append(array3[this.value[n] >>> 20 & 0xF]);
                final String string11 = sb12.toString();
                final StringBuilder sb13 = new StringBuilder();
                sb13.append(string11);
                sb13.append(array3[this.value[n] >>> 16 & 0xF]);
                final String string12 = sb13.toString();
                final StringBuilder sb14 = new StringBuilder();
                sb14.append(string12);
                sb14.append(array3[this.value[n] >>> 12 & 0xF]);
                final String string13 = sb14.toString();
                final StringBuilder sb15 = new StringBuilder();
                sb15.append(string13);
                sb15.append(array3[this.value[n] >>> 8 & 0xF]);
                final String string14 = sb15.toString();
                final StringBuilder sb16 = new StringBuilder();
                sb16.append(string14);
                sb16.append(array3[this.value[n] >>> 4 & 0xF]);
                final String string15 = sb16.toString();
                final StringBuilder sb17 = new StringBuilder();
                sb17.append(string15);
                sb17.append(array3[this.value[n] & 0xF]);
                final String string16 = sb17.toString();
                final StringBuilder sb18 = new StringBuilder();
                sb18.append(string16);
                sb18.append(" ");
                s = sb18.toString();
                --n;
            }
        }
        return s2;
    }
    
    public boolean vectorMult(final GF2Polynomial gf2Polynomial) throws RuntimeException {
        if (this.len == gf2Polynomial.len) {
            int i = 0;
            boolean b = false;
            while (i < this.blocks) {
                final int n = this.value[i] & gf2Polynomial.value[i];
                final boolean[] parity = GF2Polynomial.parity;
                b = (b ^ parity[n & 0xFF] ^ parity[n >>> 8 & 0xFF] ^ parity[n >>> 16 & 0xFF] ^ parity[n >>> 24 & 0xFF]);
                ++i;
            }
            return b;
        }
        throw new RuntimeException();
    }
    
    public GF2Polynomial xor(GF2Polynomial gf2Polynomial) {
        final int min = Math.min(this.blocks, gf2Polynomial.blocks);
        final int len = this.len;
        final int len2 = gf2Polynomial.len;
        final int n = 0;
        int n2 = 0;
        GF2Polynomial gf2Polynomial3;
        if (len >= len2) {
            final GF2Polynomial gf2Polynomial2 = new GF2Polynomial(this);
            while (true) {
                gf2Polynomial3 = gf2Polynomial2;
                if (n2 >= min) {
                    break;
                }
                final int[] value = gf2Polynomial2.value;
                value[n2] ^= gf2Polynomial.value[n2];
                ++n2;
            }
        }
        else {
            gf2Polynomial = new GF2Polynomial(gf2Polynomial);
            int n3 = n;
            while (true) {
                gf2Polynomial3 = gf2Polynomial;
                if (n3 >= min) {
                    break;
                }
                final int[] value2 = gf2Polynomial.value;
                value2[n3] ^= this.value[n3];
                ++n3;
            }
        }
        gf2Polynomial3.zeroUnusedBits();
        return gf2Polynomial3;
    }
    
    public void xorBit(final int n) throws RuntimeException {
        if (n >= 0 && n <= this.len - 1) {
            final int[] value = this.value;
            final int n2 = n >>> 5;
            value[n2] ^= GF2Polynomial.bitMask[n & 0x1F];
            return;
        }
        throw new RuntimeException();
    }
    
    public void xorThisBy(final GF2Polynomial gf2Polynomial) {
        for (int i = 0; i < Math.min(this.blocks, gf2Polynomial.blocks); ++i) {
            final int[] value = this.value;
            value[i] ^= gf2Polynomial.value[i];
        }
        this.zeroUnusedBits();
    }
}
