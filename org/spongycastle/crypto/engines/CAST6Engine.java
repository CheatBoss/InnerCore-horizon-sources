package org.spongycastle.crypto.engines;

public final class CAST6Engine extends CAST5Engine
{
    protected static final int BLOCK_SIZE = 16;
    protected static final int ROUNDS = 12;
    protected int[] _Km;
    protected int[] _Kr;
    protected int[] _Tm;
    protected int[] _Tr;
    private int[] _workingKey;
    
    public CAST6Engine() {
        this._Kr = new int[48];
        this._Km = new int[48];
        this._Tr = new int[192];
        this._Tm = new int[192];
        this._workingKey = new int[8];
    }
    
    protected final void CAST_Decipher(int n, int n2, int n3, int n4, final int[] array) {
        int n5 = 0;
        int i;
        int n6;
        int n7;
        int n8;
        int n9;
        while (true) {
            i = 6;
            n6 = n;
            n7 = n2;
            n8 = n3;
            n9 = n4;
            if (n5 >= 6) {
                break;
            }
            final int n10 = (11 - n5) * 4;
            n3 ^= this.F1(n4, this._Km[n10], this._Kr[n10]);
            final int[] km = this._Km;
            final int n11 = n10 + 1;
            n2 ^= this.F2(n3, km[n11], this._Kr[n11]);
            final int[] km2 = this._Km;
            final int n12 = n10 + 2;
            n ^= this.F3(n2, km2[n12], this._Kr[n12]);
            final int[] km3 = this._Km;
            final int n13 = n10 + 3;
            n4 ^= this.F1(n, km3[n13], this._Kr[n13]);
            ++n5;
        }
        while (i < 12) {
            n = (11 - i) * 4;
            final int[] km4 = this._Km;
            n2 = n + 3;
            n9 ^= this.F1(n6, km4[n2], this._Kr[n2]);
            final int[] km5 = this._Km;
            n2 = n + 2;
            n6 ^= this.F3(n7, km5[n2], this._Kr[n2]);
            final int[] km6 = this._Km;
            n2 = n + 1;
            n7 ^= this.F2(n8, km6[n2], this._Kr[n2]);
            n8 ^= this.F1(n9, this._Km[n], this._Kr[n]);
            ++i;
        }
        array[0] = n6;
        array[1] = n7;
        array[2] = n8;
        array[3] = n9;
    }
    
    protected final void CAST_Encipher(int n, int n2, int n3, int n4, final int[] array) {
        int n5 = 0;
        int i;
        int n6;
        int n7;
        int n8;
        int n9;
        while (true) {
            i = 6;
            n6 = n;
            n7 = n2;
            n8 = n3;
            n9 = n4;
            if (n5 >= 6) {
                break;
            }
            final int n10 = n5 * 4;
            n3 ^= this.F1(n4, this._Km[n10], this._Kr[n10]);
            final int[] km = this._Km;
            final int n11 = n10 + 1;
            n2 ^= this.F2(n3, km[n11], this._Kr[n11]);
            final int[] km2 = this._Km;
            final int n12 = n10 + 2;
            n ^= this.F3(n2, km2[n12], this._Kr[n12]);
            final int[] km3 = this._Km;
            final int n13 = n10 + 3;
            n4 ^= this.F1(n, km3[n13], this._Kr[n13]);
            ++n5;
        }
        while (i < 12) {
            n = i * 4;
            final int[] km4 = this._Km;
            n2 = n + 3;
            n9 ^= this.F1(n6, km4[n2], this._Kr[n2]);
            final int[] km5 = this._Km;
            n2 = n + 2;
            n6 ^= this.F3(n7, km5[n2], this._Kr[n2]);
            final int[] km6 = this._Km;
            n2 = n + 1;
            n7 ^= this.F2(n8, km6[n2], this._Kr[n2]);
            n8 ^= this.F1(n9, this._Km[n], this._Kr[n]);
            ++i;
        }
        array[0] = n6;
        array[1] = n7;
        array[2] = n8;
        array[3] = n9;
    }
    
    @Override
    protected int decryptBlock(final byte[] array, final int n, final byte[] array2, final int n2) {
        final int[] array3 = new int[4];
        this.CAST_Decipher(this.BytesTo32bits(array, n), this.BytesTo32bits(array, n + 4), this.BytesTo32bits(array, n + 8), this.BytesTo32bits(array, n + 12), array3);
        this.Bits32ToBytes(array3[0], array2, n2);
        this.Bits32ToBytes(array3[1], array2, n2 + 4);
        this.Bits32ToBytes(array3[2], array2, n2 + 8);
        this.Bits32ToBytes(array3[3], array2, n2 + 12);
        return 16;
    }
    
    @Override
    protected int encryptBlock(final byte[] array, final int n, final byte[] array2, final int n2) {
        final int[] array3 = new int[4];
        this.CAST_Encipher(this.BytesTo32bits(array, n), this.BytesTo32bits(array, n + 4), this.BytesTo32bits(array, n + 8), this.BytesTo32bits(array, n + 12), array3);
        this.Bits32ToBytes(array3[0], array2, n2);
        this.Bits32ToBytes(array3[1], array2, n2 + 4);
        this.Bits32ToBytes(array3[2], array2, n2 + 8);
        this.Bits32ToBytes(array3[3], array2, n2 + 12);
        return 16;
    }
    
    @Override
    public String getAlgorithmName() {
        return "CAST6";
    }
    
    @Override
    public int getBlockSize() {
        return 16;
    }
    
    @Override
    public void reset() {
    }
    
    @Override
    protected void setKey(final byte[] array) {
        int n = 19;
        int i = 0;
        int n2 = 1518500249;
        while (i < 24) {
            for (int j = 0; j < 8; ++j) {
                final int[] tm = this._Tm;
                final int n3 = i * 8 + j;
                tm[n3] = n2;
                n2 += 1859775393;
                this._Tr[n3] = n;
                n = (n + 17 & 0x1F);
            }
            ++i;
        }
        final byte[] array2 = new byte[64];
        System.arraycopy(array, 0, array2, 0, array.length);
        for (int k = 0; k < 8; ++k) {
            this._workingKey[k] = this.BytesTo32bits(array2, k * 4);
        }
        for (int l = 0; l < 12; ++l) {
            final int n4 = l * 2;
            final int n5 = n4 * 8;
            final int[] workingKey = this._workingKey;
            workingKey[6] ^= this.F1(workingKey[7], this._Tm[n5], this._Tr[n5]);
            final int[] workingKey2 = this._workingKey;
            final int n6 = workingKey2[5];
            final int n7 = workingKey2[6];
            final int[] tm2 = this._Tm;
            final int n8 = n5 + 1;
            workingKey2[5] = (n6 ^ this.F2(n7, tm2[n8], this._Tr[n8]));
            final int[] workingKey3 = this._workingKey;
            final int n9 = workingKey3[4];
            final int n10 = workingKey3[5];
            final int[] tm3 = this._Tm;
            final int n11 = n5 + 2;
            workingKey3[4] = (n9 ^ this.F3(n10, tm3[n11], this._Tr[n11]));
            final int[] workingKey4 = this._workingKey;
            final int n12 = workingKey4[3];
            final int n13 = workingKey4[4];
            final int[] tm4 = this._Tm;
            final int n14 = n5 + 3;
            workingKey4[3] = (this.F1(n13, tm4[n14], this._Tr[n14]) ^ n12);
            final int[] workingKey5 = this._workingKey;
            final int n15 = workingKey5[2];
            final int n16 = workingKey5[3];
            final int[] tm5 = this._Tm;
            final int n17 = n5 + 4;
            workingKey5[2] = (this.F2(n16, tm5[n17], this._Tr[n17]) ^ n15);
            final int[] workingKey6 = this._workingKey;
            final int n18 = workingKey6[1];
            final int n19 = workingKey6[2];
            final int[] tm6 = this._Tm;
            final int n20 = n5 + 5;
            workingKey6[1] = (this.F3(n19, tm6[n20], this._Tr[n20]) ^ n18);
            final int[] workingKey7 = this._workingKey;
            final int n21 = workingKey7[0];
            final int n22 = workingKey7[1];
            final int[] tm7 = this._Tm;
            final int n23 = n5 + 6;
            workingKey7[0] = (n21 ^ this.F1(n22, tm7[n23], this._Tr[n23]));
            final int[] workingKey8 = this._workingKey;
            final int n24 = workingKey8[7];
            final int n25 = workingKey8[0];
            final int[] tm8 = this._Tm;
            final int n26 = n5 + 7;
            workingKey8[7] = (this.F2(n25, tm8[n26], this._Tr[n26]) ^ n24);
            final int n27 = (n4 + 1) * 8;
            final int[] workingKey9 = this._workingKey;
            workingKey9[6] ^= this.F1(workingKey9[7], this._Tm[n27], this._Tr[n27]);
            final int[] workingKey10 = this._workingKey;
            final int n28 = workingKey10[5];
            final int n29 = workingKey10[6];
            final int[] tm9 = this._Tm;
            final int n30 = n27 + 1;
            workingKey10[5] = (n28 ^ this.F2(n29, tm9[n30], this._Tr[n30]));
            final int[] workingKey11 = this._workingKey;
            final int n31 = workingKey11[4];
            final int n32 = workingKey11[5];
            final int[] tm10 = this._Tm;
            final int n33 = n27 + 2;
            workingKey11[4] = (n31 ^ this.F3(n32, tm10[n33], this._Tr[n33]));
            final int[] workingKey12 = this._workingKey;
            final int n34 = workingKey12[3];
            final int n35 = workingKey12[4];
            final int[] tm11 = this._Tm;
            final int n36 = n27 + 3;
            workingKey12[3] = (this.F1(n35, tm11[n36], this._Tr[n36]) ^ n34);
            final int[] workingKey13 = this._workingKey;
            final int n37 = workingKey13[2];
            final int n38 = workingKey13[3];
            final int[] tm12 = this._Tm;
            final int n39 = n27 + 4;
            workingKey13[2] = (this.F2(n38, tm12[n39], this._Tr[n39]) ^ n37);
            final int[] workingKey14 = this._workingKey;
            final int n40 = workingKey14[1];
            final int n41 = workingKey14[2];
            final int[] tm13 = this._Tm;
            final int n42 = n27 + 5;
            workingKey14[1] = (this.F3(n41, tm13[n42], this._Tr[n42]) ^ n40);
            final int[] workingKey15 = this._workingKey;
            final int n43 = workingKey15[0];
            final int n44 = workingKey15[1];
            final int[] tm14 = this._Tm;
            final int n45 = n27 + 6;
            workingKey15[0] = (n43 ^ this.F1(n44, tm14[n45], this._Tr[n45]));
            final int[] workingKey16 = this._workingKey;
            final int n46 = workingKey16[7];
            final int n47 = workingKey16[0];
            final int[] tm15 = this._Tm;
            final int n48 = n27 + 7;
            workingKey16[7] = (this.F2(n47, tm15[n48], this._Tr[n48]) ^ n46);
            final int[] kr = this._Kr;
            final int n49 = l * 4;
            final int[] workingKey17 = this._workingKey;
            kr[n49] = (workingKey17[0] & 0x1F);
            final int n50 = n49 + 1;
            kr[n50] = (workingKey17[2] & 0x1F);
            final int n51 = n49 + 2;
            kr[n51] = (workingKey17[4] & 0x1F);
            final int n52 = n49 + 3;
            kr[n52] = (workingKey17[6] & 0x1F);
            final int[] km = this._Km;
            km[n49] = workingKey17[7];
            km[n50] = workingKey17[5];
            km[n51] = workingKey17[3];
            km[n52] = workingKey17[1];
        }
    }
}
