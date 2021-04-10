package org.spongycastle.crypto.engines;

import org.spongycastle.util.*;

public final class SerpentEngine extends SerpentEngineBase
{
    @Override
    protected void decryptBlock(final byte[] array, final int n, final byte[] array2, final int n2) {
        this.X0 = (this.wKey[128] ^ Pack.littleEndianToInt(array, n));
        this.X1 = (this.wKey[129] ^ Pack.littleEndianToInt(array, n + 4));
        this.X2 = (this.wKey[130] ^ Pack.littleEndianToInt(array, n + 8));
        this.X3 = (Pack.littleEndianToInt(array, n + 12) ^ this.wKey[131]);
        this.ib7(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[124];
        this.X1 ^= this.wKey[125];
        this.X2 ^= this.wKey[126];
        this.X3 ^= this.wKey[127];
        this.inverseLT();
        this.ib6(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[120];
        this.X1 ^= this.wKey[121];
        this.X2 ^= this.wKey[122];
        this.X3 ^= this.wKey[123];
        this.inverseLT();
        this.ib5(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[116];
        this.X1 ^= this.wKey[117];
        this.X2 ^= this.wKey[118];
        this.X3 ^= this.wKey[119];
        this.inverseLT();
        this.ib4(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[112];
        this.X1 ^= this.wKey[113];
        this.X2 ^= this.wKey[114];
        this.X3 ^= this.wKey[115];
        this.inverseLT();
        this.ib3(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[108];
        this.X1 ^= this.wKey[109];
        this.X2 ^= this.wKey[110];
        this.X3 ^= this.wKey[111];
        this.inverseLT();
        this.ib2(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[104];
        this.X1 ^= this.wKey[105];
        this.X2 ^= this.wKey[106];
        this.X3 ^= this.wKey[107];
        this.inverseLT();
        this.ib1(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[100];
        this.X1 ^= this.wKey[101];
        this.X2 ^= this.wKey[102];
        this.X3 ^= this.wKey[103];
        this.inverseLT();
        this.ib0(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[96];
        this.X1 ^= this.wKey[97];
        this.X2 ^= this.wKey[98];
        this.X3 ^= this.wKey[99];
        this.inverseLT();
        this.ib7(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[92];
        this.X1 ^= this.wKey[93];
        this.X2 ^= this.wKey[94];
        this.X3 ^= this.wKey[95];
        this.inverseLT();
        this.ib6(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[88];
        this.X1 ^= this.wKey[89];
        this.X2 ^= this.wKey[90];
        this.X3 ^= this.wKey[91];
        this.inverseLT();
        this.ib5(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[84];
        this.X1 ^= this.wKey[85];
        this.X2 ^= this.wKey[86];
        this.X3 ^= this.wKey[87];
        this.inverseLT();
        this.ib4(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[80];
        this.X1 ^= this.wKey[81];
        this.X2 ^= this.wKey[82];
        this.X3 ^= this.wKey[83];
        this.inverseLT();
        this.ib3(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[76];
        this.X1 ^= this.wKey[77];
        this.X2 ^= this.wKey[78];
        this.X3 ^= this.wKey[79];
        this.inverseLT();
        this.ib2(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[72];
        this.X1 ^= this.wKey[73];
        this.X2 ^= this.wKey[74];
        this.X3 ^= this.wKey[75];
        this.inverseLT();
        this.ib1(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[68];
        this.X1 ^= this.wKey[69];
        this.X2 ^= this.wKey[70];
        this.X3 ^= this.wKey[71];
        this.inverseLT();
        this.ib0(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[64];
        this.X1 ^= this.wKey[65];
        this.X2 ^= this.wKey[66];
        this.X3 ^= this.wKey[67];
        this.inverseLT();
        this.ib7(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[60];
        this.X1 ^= this.wKey[61];
        this.X2 ^= this.wKey[62];
        this.X3 ^= this.wKey[63];
        this.inverseLT();
        this.ib6(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[56];
        this.X1 ^= this.wKey[57];
        this.X2 ^= this.wKey[58];
        this.X3 ^= this.wKey[59];
        this.inverseLT();
        this.ib5(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[52];
        this.X1 ^= this.wKey[53];
        this.X2 ^= this.wKey[54];
        this.X3 ^= this.wKey[55];
        this.inverseLT();
        this.ib4(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[48];
        this.X1 ^= this.wKey[49];
        this.X2 ^= this.wKey[50];
        this.X3 ^= this.wKey[51];
        this.inverseLT();
        this.ib3(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[44];
        this.X1 ^= this.wKey[45];
        this.X2 ^= this.wKey[46];
        this.X3 ^= this.wKey[47];
        this.inverseLT();
        this.ib2(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[40];
        this.X1 ^= this.wKey[41];
        this.X2 ^= this.wKey[42];
        this.X3 ^= this.wKey[43];
        this.inverseLT();
        this.ib1(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[36];
        this.X1 ^= this.wKey[37];
        this.X2 ^= this.wKey[38];
        this.X3 ^= this.wKey[39];
        this.inverseLT();
        this.ib0(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[32];
        this.X1 ^= this.wKey[33];
        this.X2 ^= this.wKey[34];
        this.X3 ^= this.wKey[35];
        this.inverseLT();
        this.ib7(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[28];
        this.X1 ^= this.wKey[29];
        this.X2 ^= this.wKey[30];
        this.X3 ^= this.wKey[31];
        this.inverseLT();
        this.ib6(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[24];
        this.X1 ^= this.wKey[25];
        this.X2 ^= this.wKey[26];
        this.X3 ^= this.wKey[27];
        this.inverseLT();
        this.ib5(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[20];
        this.X1 ^= this.wKey[21];
        this.X2 ^= this.wKey[22];
        this.X3 ^= this.wKey[23];
        this.inverseLT();
        this.ib4(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[16];
        this.X1 ^= this.wKey[17];
        this.X2 ^= this.wKey[18];
        this.X3 ^= this.wKey[19];
        this.inverseLT();
        this.ib3(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[12];
        this.X1 ^= this.wKey[13];
        this.X2 ^= this.wKey[14];
        this.X3 ^= this.wKey[15];
        this.inverseLT();
        this.ib2(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[8];
        this.X1 ^= this.wKey[9];
        this.X2 ^= this.wKey[10];
        this.X3 ^= this.wKey[11];
        this.inverseLT();
        this.ib1(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[4];
        this.X1 ^= this.wKey[5];
        this.X2 ^= this.wKey[6];
        this.X3 ^= this.wKey[7];
        this.inverseLT();
        this.ib0(this.X0, this.X1, this.X2, this.X3);
        Pack.intToLittleEndian(this.X0 ^ this.wKey[0], array2, n2);
        Pack.intToLittleEndian(this.X1 ^ this.wKey[1], array2, n2 + 4);
        Pack.intToLittleEndian(this.X2 ^ this.wKey[2], array2, n2 + 8);
        Pack.intToLittleEndian(this.X3 ^ this.wKey[3], array2, n2 + 12);
    }
    
    @Override
    protected void encryptBlock(final byte[] array, final int n, final byte[] array2, final int n2) {
        this.X0 = Pack.littleEndianToInt(array, n);
        this.X1 = Pack.littleEndianToInt(array, n + 4);
        this.X2 = Pack.littleEndianToInt(array, n + 8);
        this.X3 = Pack.littleEndianToInt(array, n + 12);
        this.sb0(this.wKey[0] ^ this.X0, this.wKey[1] ^ this.X1, this.wKey[2] ^ this.X2, this.wKey[3] ^ this.X3);
        this.LT();
        this.sb1(this.wKey[4] ^ this.X0, this.wKey[5] ^ this.X1, this.wKey[6] ^ this.X2, this.wKey[7] ^ this.X3);
        this.LT();
        this.sb2(this.wKey[8] ^ this.X0, this.wKey[9] ^ this.X1, this.wKey[10] ^ this.X2, this.wKey[11] ^ this.X3);
        this.LT();
        this.sb3(this.wKey[12] ^ this.X0, this.wKey[13] ^ this.X1, this.wKey[14] ^ this.X2, this.wKey[15] ^ this.X3);
        this.LT();
        this.sb4(this.wKey[16] ^ this.X0, this.wKey[17] ^ this.X1, this.wKey[18] ^ this.X2, this.wKey[19] ^ this.X3);
        this.LT();
        this.sb5(this.wKey[20] ^ this.X0, this.wKey[21] ^ this.X1, this.wKey[22] ^ this.X2, this.wKey[23] ^ this.X3);
        this.LT();
        this.sb6(this.wKey[24] ^ this.X0, this.wKey[25] ^ this.X1, this.wKey[26] ^ this.X2, this.wKey[27] ^ this.X3);
        this.LT();
        this.sb7(this.wKey[28] ^ this.X0, this.wKey[29] ^ this.X1, this.wKey[30] ^ this.X2, this.wKey[31] ^ this.X3);
        this.LT();
        this.sb0(this.wKey[32] ^ this.X0, this.wKey[33] ^ this.X1, this.wKey[34] ^ this.X2, this.wKey[35] ^ this.X3);
        this.LT();
        this.sb1(this.wKey[36] ^ this.X0, this.wKey[37] ^ this.X1, this.wKey[38] ^ this.X2, this.wKey[39] ^ this.X3);
        this.LT();
        this.sb2(this.wKey[40] ^ this.X0, this.wKey[41] ^ this.X1, this.wKey[42] ^ this.X2, this.wKey[43] ^ this.X3);
        this.LT();
        this.sb3(this.wKey[44] ^ this.X0, this.wKey[45] ^ this.X1, this.wKey[46] ^ this.X2, this.wKey[47] ^ this.X3);
        this.LT();
        this.sb4(this.wKey[48] ^ this.X0, this.wKey[49] ^ this.X1, this.wKey[50] ^ this.X2, this.wKey[51] ^ this.X3);
        this.LT();
        this.sb5(this.wKey[52] ^ this.X0, this.wKey[53] ^ this.X1, this.wKey[54] ^ this.X2, this.wKey[55] ^ this.X3);
        this.LT();
        this.sb6(this.wKey[56] ^ this.X0, this.wKey[57] ^ this.X1, this.wKey[58] ^ this.X2, this.wKey[59] ^ this.X3);
        this.LT();
        this.sb7(this.wKey[60] ^ this.X0, this.wKey[61] ^ this.X1, this.wKey[62] ^ this.X2, this.wKey[63] ^ this.X3);
        this.LT();
        this.sb0(this.wKey[64] ^ this.X0, this.wKey[65] ^ this.X1, this.wKey[66] ^ this.X2, this.wKey[67] ^ this.X3);
        this.LT();
        this.sb1(this.wKey[68] ^ this.X0, this.wKey[69] ^ this.X1, this.wKey[70] ^ this.X2, this.wKey[71] ^ this.X3);
        this.LT();
        this.sb2(this.wKey[72] ^ this.X0, this.wKey[73] ^ this.X1, this.wKey[74] ^ this.X2, this.wKey[75] ^ this.X3);
        this.LT();
        this.sb3(this.wKey[76] ^ this.X0, this.wKey[77] ^ this.X1, this.wKey[78] ^ this.X2, this.wKey[79] ^ this.X3);
        this.LT();
        this.sb4(this.wKey[80] ^ this.X0, this.wKey[81] ^ this.X1, this.wKey[82] ^ this.X2, this.wKey[83] ^ this.X3);
        this.LT();
        this.sb5(this.wKey[84] ^ this.X0, this.wKey[85] ^ this.X1, this.wKey[86] ^ this.X2, this.wKey[87] ^ this.X3);
        this.LT();
        this.sb6(this.wKey[88] ^ this.X0, this.wKey[89] ^ this.X1, this.wKey[90] ^ this.X2, this.wKey[91] ^ this.X3);
        this.LT();
        this.sb7(this.wKey[92] ^ this.X0, this.wKey[93] ^ this.X1, this.wKey[94] ^ this.X2, this.wKey[95] ^ this.X3);
        this.LT();
        this.sb0(this.wKey[96] ^ this.X0, this.wKey[97] ^ this.X1, this.wKey[98] ^ this.X2, this.wKey[99] ^ this.X3);
        this.LT();
        this.sb1(this.wKey[100] ^ this.X0, this.wKey[101] ^ this.X1, this.wKey[102] ^ this.X2, this.wKey[103] ^ this.X3);
        this.LT();
        this.sb2(this.wKey[104] ^ this.X0, this.wKey[105] ^ this.X1, this.wKey[106] ^ this.X2, this.wKey[107] ^ this.X3);
        this.LT();
        this.sb3(this.wKey[108] ^ this.X0, this.wKey[109] ^ this.X1, this.wKey[110] ^ this.X2, this.wKey[111] ^ this.X3);
        this.LT();
        this.sb4(this.wKey[112] ^ this.X0, this.wKey[113] ^ this.X1, this.wKey[114] ^ this.X2, this.wKey[115] ^ this.X3);
        this.LT();
        this.sb5(this.wKey[116] ^ this.X0, this.wKey[117] ^ this.X1, this.wKey[118] ^ this.X2, this.wKey[119] ^ this.X3);
        this.LT();
        this.sb6(this.wKey[120] ^ this.X0, this.wKey[121] ^ this.X1, this.wKey[122] ^ this.X2, this.wKey[123] ^ this.X3);
        this.LT();
        this.sb7(this.wKey[124] ^ this.X0, this.wKey[125] ^ this.X1, this.wKey[126] ^ this.X2, this.wKey[127] ^ this.X3);
        Pack.intToLittleEndian(this.wKey[128] ^ this.X0, array2, n2);
        Pack.intToLittleEndian(this.wKey[129] ^ this.X1, array2, n2 + 4);
        Pack.intToLittleEndian(this.wKey[130] ^ this.X2, array2, n2 + 8);
        Pack.intToLittleEndian(this.wKey[131] ^ this.X3, array2, n2 + 12);
    }
    
    @Override
    protected int[] makeWorkingKey(final byte[] array) throws IllegalArgumentException {
        final int[] array2 = new int[16];
        int n = 0;
        int n2 = 0;
        while (true) {
            final int n3 = n + 4;
            if (n3 >= array.length) {
                break;
            }
            array2[n2] = Pack.littleEndianToInt(array, n);
            ++n2;
            n = n3;
        }
        if (n % 4 == 0) {
            final int n4 = n2 + 1;
            array2[n2] = Pack.littleEndianToInt(array, n);
            if (n4 < 8) {
                array2[n4] = 1;
            }
            final int[] array3 = new int[132];
            for (int i = 8; i < 16; ++i) {
                final int n5 = i - 8;
                array2[i] = SerpentEngineBase.rotateLeft(0x9E3779B9 ^ (array2[n5] ^ array2[i - 5] ^ array2[i - 3] ^ array2[i - 1]) ^ n5, 11);
            }
            System.arraycopy(array2, 8, array3, 0, 8);
            for (int j = 8; j < 132; ++j) {
                array3[j] = SerpentEngineBase.rotateLeft(array3[j - 8] ^ array3[j - 5] ^ array3[j - 3] ^ array3[j - 1] ^ 0x9E3779B9 ^ j, 11);
            }
            this.sb3(array3[0], array3[1], array3[2], array3[3]);
            array3[0] = this.X0;
            array3[1] = this.X1;
            array3[2] = this.X2;
            array3[3] = this.X3;
            this.sb2(array3[4], array3[5], array3[6], array3[7]);
            array3[4] = this.X0;
            array3[5] = this.X1;
            array3[6] = this.X2;
            array3[7] = this.X3;
            this.sb1(array3[8], array3[9], array3[10], array3[11]);
            array3[8] = this.X0;
            array3[9] = this.X1;
            array3[10] = this.X2;
            array3[11] = this.X3;
            this.sb0(array3[12], array3[13], array3[14], array3[15]);
            array3[12] = this.X0;
            array3[13] = this.X1;
            array3[14] = this.X2;
            array3[15] = this.X3;
            this.sb7(array3[16], array3[17], array3[18], array3[19]);
            array3[16] = this.X0;
            array3[17] = this.X1;
            array3[18] = this.X2;
            array3[19] = this.X3;
            this.sb6(array3[20], array3[21], array3[22], array3[23]);
            array3[20] = this.X0;
            array3[21] = this.X1;
            array3[22] = this.X2;
            array3[23] = this.X3;
            this.sb5(array3[24], array3[25], array3[26], array3[27]);
            array3[24] = this.X0;
            array3[25] = this.X1;
            array3[26] = this.X2;
            array3[27] = this.X3;
            this.sb4(array3[28], array3[29], array3[30], array3[31]);
            array3[28] = this.X0;
            array3[29] = this.X1;
            array3[30] = this.X2;
            array3[31] = this.X3;
            this.sb3(array3[32], array3[33], array3[34], array3[35]);
            array3[32] = this.X0;
            array3[33] = this.X1;
            array3[34] = this.X2;
            array3[35] = this.X3;
            this.sb2(array3[36], array3[37], array3[38], array3[39]);
            array3[36] = this.X0;
            array3[37] = this.X1;
            array3[38] = this.X2;
            array3[39] = this.X3;
            this.sb1(array3[40], array3[41], array3[42], array3[43]);
            array3[40] = this.X0;
            array3[41] = this.X1;
            array3[42] = this.X2;
            array3[43] = this.X3;
            this.sb0(array3[44], array3[45], array3[46], array3[47]);
            array3[44] = this.X0;
            array3[45] = this.X1;
            array3[46] = this.X2;
            array3[47] = this.X3;
            this.sb7(array3[48], array3[49], array3[50], array3[51]);
            array3[48] = this.X0;
            array3[49] = this.X1;
            array3[50] = this.X2;
            array3[51] = this.X3;
            this.sb6(array3[52], array3[53], array3[54], array3[55]);
            array3[52] = this.X0;
            array3[53] = this.X1;
            array3[54] = this.X2;
            array3[55] = this.X3;
            this.sb5(array3[56], array3[57], array3[58], array3[59]);
            array3[56] = this.X0;
            array3[57] = this.X1;
            array3[58] = this.X2;
            array3[59] = this.X3;
            this.sb4(array3[60], array3[61], array3[62], array3[63]);
            array3[60] = this.X0;
            array3[61] = this.X1;
            array3[62] = this.X2;
            array3[63] = this.X3;
            this.sb3(array3[64], array3[65], array3[66], array3[67]);
            array3[64] = this.X0;
            array3[65] = this.X1;
            array3[66] = this.X2;
            array3[67] = this.X3;
            this.sb2(array3[68], array3[69], array3[70], array3[71]);
            array3[68] = this.X0;
            array3[69] = this.X1;
            array3[70] = this.X2;
            array3[71] = this.X3;
            this.sb1(array3[72], array3[73], array3[74], array3[75]);
            array3[72] = this.X0;
            array3[73] = this.X1;
            array3[74] = this.X2;
            array3[75] = this.X3;
            this.sb0(array3[76], array3[77], array3[78], array3[79]);
            array3[76] = this.X0;
            array3[77] = this.X1;
            array3[78] = this.X2;
            array3[79] = this.X3;
            this.sb7(array3[80], array3[81], array3[82], array3[83]);
            array3[80] = this.X0;
            array3[81] = this.X1;
            array3[82] = this.X2;
            array3[83] = this.X3;
            this.sb6(array3[84], array3[85], array3[86], array3[87]);
            array3[84] = this.X0;
            array3[85] = this.X1;
            array3[86] = this.X2;
            array3[87] = this.X3;
            this.sb5(array3[88], array3[89], array3[90], array3[91]);
            array3[88] = this.X0;
            array3[89] = this.X1;
            array3[90] = this.X2;
            array3[91] = this.X3;
            this.sb4(array3[92], array3[93], array3[94], array3[95]);
            array3[92] = this.X0;
            array3[93] = this.X1;
            array3[94] = this.X2;
            array3[95] = this.X3;
            this.sb3(array3[96], array3[97], array3[98], array3[99]);
            array3[96] = this.X0;
            array3[97] = this.X1;
            array3[98] = this.X2;
            array3[99] = this.X3;
            this.sb2(array3[100], array3[101], array3[102], array3[103]);
            array3[100] = this.X0;
            array3[101] = this.X1;
            array3[102] = this.X2;
            array3[103] = this.X3;
            this.sb1(array3[104], array3[105], array3[106], array3[107]);
            array3[104] = this.X0;
            array3[105] = this.X1;
            array3[106] = this.X2;
            array3[107] = this.X3;
            this.sb0(array3[108], array3[109], array3[110], array3[111]);
            array3[108] = this.X0;
            array3[109] = this.X1;
            array3[110] = this.X2;
            array3[111] = this.X3;
            this.sb7(array3[112], array3[113], array3[114], array3[115]);
            array3[112] = this.X0;
            array3[113] = this.X1;
            array3[114] = this.X2;
            array3[115] = this.X3;
            this.sb6(array3[116], array3[117], array3[118], array3[119]);
            array3[116] = this.X0;
            array3[117] = this.X1;
            array3[118] = this.X2;
            array3[119] = this.X3;
            this.sb5(array3[120], array3[121], array3[122], array3[123]);
            array3[120] = this.X0;
            array3[121] = this.X1;
            array3[122] = this.X2;
            array3[123] = this.X3;
            this.sb4(array3[124], array3[125], array3[126], array3[127]);
            array3[124] = this.X0;
            array3[125] = this.X1;
            array3[126] = this.X2;
            array3[127] = this.X3;
            this.sb3(array3[128], array3[129], array3[130], array3[131]);
            array3[128] = this.X0;
            array3[129] = this.X1;
            array3[130] = this.X2;
            array3[131] = this.X3;
            return array3;
        }
        throw new IllegalArgumentException("key must be a multiple of 4 bytes");
    }
}
