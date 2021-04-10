package net.lingala.zip4j.crypto.engine;

import net.lingala.zip4j.exception.*;
import java.lang.reflect.*;

public class AESEngine
{
    private static final byte[] S;
    private static final int[] T0;
    private static final int[] rcon;
    private int C0;
    private int C1;
    private int C2;
    private int C3;
    private int rounds;
    private int[][] workingKey;
    
    static {
        S = new byte[] { 99, 124, 119, 123, -14, 107, 111, -59, 48, 1, 103, 43, -2, -41, -85, 118, -54, -126, -55, 125, -6, 89, 71, -16, -83, -44, -94, -81, -100, -92, 114, -64, -73, -3, -109, 38, 54, 63, -9, -52, 52, -91, -27, -15, 113, -40, 49, 21, 4, -57, 35, -61, 24, -106, 5, -102, 7, 18, -128, -30, -21, 39, -78, 117, 9, -125, 44, 26, 27, 110, 90, -96, 82, 59, -42, -77, 41, -29, 47, -124, 83, -47, 0, -19, 32, -4, -79, 91, 106, -53, -66, 57, 74, 76, 88, -49, -48, -17, -86, -5, 67, 77, 51, -123, 69, -7, 2, 127, 80, 60, -97, -88, 81, -93, 64, -113, -110, -99, 56, -11, -68, -74, -38, 33, 16, -1, -13, -46, -51, 12, 19, -20, 95, -105, 68, 23, -60, -89, 126, 61, 100, 93, 25, 115, 96, -127, 79, -36, 34, 42, -112, -120, 70, -18, -72, 20, -34, 94, 11, -37, -32, 50, 58, 10, 73, 6, 36, 92, -62, -45, -84, 98, -111, -107, -28, 121, -25, -56, 55, 109, -115, -43, 78, -87, 108, 86, -12, -22, 101, 122, -82, 8, -70, 120, 37, 46, 28, -90, -76, -58, -24, -35, 116, 31, 75, -67, -117, -118, 112, 62, -75, 102, 72, 3, -10, 14, 97, 53, 87, -71, -122, -63, 29, -98, -31, -8, -104, 17, 105, -39, -114, -108, -101, 30, -121, -23, -50, 85, 40, -33, -116, -95, -119, 13, -65, -26, 66, 104, 65, -103, 45, 15, -80, 84, -69, 22 };
        rcon = new int[] { 1, 2, 4, 8, 16, 32, 64, 128, 27, 54, 108, 216, 171, 77, 154, 47, 94, 188, 99, 198, 151, 53, 106, 212, 179, 125, 250, 239, 197, 145 };
        T0 = new int[] { -1520213050, -2072216328, -1720223762, -1921287178, 234025727, -1117033514, -1318096930, 1422247313, 1345335392, 50397442, -1452841010, 2099981142, 436141799, 1658312629, -424957107, -1703512340, 1170918031, -1652391393, 1086966153, -2021818886, 368769775, -346465870, -918075506, 200339707, -324162239, 1742001331, -39673249, -357585083, -1080255453, -140204973, -1770884380, 1539358875, -1028147339, 486407649, -1366060227, 1780885068, 1513502316, 1094664062, 49805301, 1338821763, 1546925160, -190470831, 887481809, 150073849, -1821281822, 1943591083, 1395732834, 1058346282, 201589768, 1388824469, 1696801606, 1589887901, 672667696, -1583966665, 251987210, -1248159185, 151455502, 907153956, -1686077413, 1038279391, 652995533, 1764173646, -843926913, -1619692054, 453576978, -1635548387, 1949051992, 773462580, 756751158, -1301385508, -296068428, -73359269, -162377052, 1295727478, 1641469623, -827083907, 2066295122, 1055122397, 1898917726, -1752923117, -179088474, 1758581177, 0, 753790401, 1612718144, 536673507, -927878791, -312779850, -1100322092, 1187761037, -641810841, 1262041458, -565556588, -733197160, -396863312, 1255133061, 1808847035, 720367557, -441800113, 385612781, -985447546, -682799718, 1429418854, -1803188975, -817543798, 284817897, 100794884, -2122350594, -263171936, 1144798328, -1163944155, -475486133, -212774494, -22830243, -1069531008, -1970303227, -1382903233, -1130521311, 1211644016, 83228145, -541279133, -1044990345, 1977277103, 1663115586, 806359072, 452984805, 250868733, 1842533055, 1288555905, 336333848, 890442534, 804056259, -513843266, -1567123659, -867941240, 957814574, 1472513171, -223893675, -2105639172, 1195195770, -1402706744, -413311558, 723065138, -1787595802, -1604296512, -1736343271, -783331426, 2145180835, 1713513028, 2116692564, -1416589253, -2088204277, -901364084, 703524551, -742868885, 1007948840, 2044649127, -497131844, 487262998, 1994120109, 1004593371, 1446130276, 1312438900, 503974420, -615954030, 168166924, 1814307912, -463709000, 1573044895, 1859376061, -273896381, -1503501628, -1466855111, -1533700815, 937747667, -1954973198, 854058965, 1137232011, 1496790894, -1217565222, -1936880383, 1691735473, -766620004, -525751991, -1267962664, -95005012, 133494003, 636152527, -1352309302, -1904575756, -374428089, 403179536, -709182865, -2005370640, 1864705354, 1915629148, 605822008, -240736681, -944458637, 1371981463, 602466507, 2094914977, -1670089496, 555687742, -582268010, -591544991, -2037675251, -2054518257, -1871679264, 1111375484, -994724495, -1436129588, -666351472, 84083462, 32962295, 302911004, -1553899070, 1597322602, -111716434, -793134743, -1853454825, 1489093017, 656219450, -1180787161, 954327513, 335083755, -1281845205, 856756514, -1150719534, 1893325225, -1987146233, -1483434957, -1231316179, 572399164, -1836611819, 552200649, 1238290055, -11184726, 2015897680, 2061492133, -1886614525, -123625127, -2138470135, 386731290, -624967835, 837215959, -968736124, -1201116976, -1019133566, -1332111063, 1999449434, 286199582, -877612933, -61582168, -692339859, 974525996 };
    }
    
    public AESEngine(final byte[] array) throws ZipException {
        this.workingKey = null;
        this.init(array);
    }
    
    private final void encryptBlock(final int[][] array) {
        this.C0 ^= array[0][0];
        this.C1 ^= array[0][1];
        this.C2 ^= array[0][2];
        this.C3 ^= array[0][3];
        int i;
        int n5;
        for (i = 1; i < this.rounds - 1; i = n5 + 1) {
            final int n = AESEngine.T0[this.C0 & 0xFF] ^ this.shift(AESEngine.T0[this.C1 >> 8 & 0xFF], 24) ^ this.shift(AESEngine.T0[this.C2 >> 16 & 0xFF], 16) ^ this.shift(AESEngine.T0[this.C3 >> 24 & 0xFF], 8) ^ array[i][0];
            final int n2 = AESEngine.T0[this.C1 & 0xFF] ^ this.shift(AESEngine.T0[this.C2 >> 8 & 0xFF], 24) ^ this.shift(AESEngine.T0[this.C3 >> 16 & 0xFF], 16) ^ this.shift(AESEngine.T0[this.C0 >> 24 & 0xFF], 8) ^ array[i][1];
            final int n3 = AESEngine.T0[this.C2 & 0xFF] ^ this.shift(AESEngine.T0[this.C3 >> 8 & 0xFF], 24) ^ this.shift(AESEngine.T0[this.C0 >> 16 & 0xFF], 16) ^ this.shift(AESEngine.T0[this.C1 >> 24 & 0xFF], 8) ^ array[i][2];
            final int n4 = AESEngine.T0[this.C3 & 0xFF];
            final int shift = this.shift(AESEngine.T0[this.C0 >> 8 & 0xFF], 24);
            final int shift2 = this.shift(AESEngine.T0[this.C1 >> 16 & 0xFF], 16);
            final int shift3 = this.shift(AESEngine.T0[this.C2 >> 24 & 0xFF], 8);
            n5 = i + 1;
            final int n6 = array[i][3] ^ (n4 ^ shift ^ shift2 ^ shift3);
            this.C0 = (this.shift(AESEngine.T0[n2 >> 8 & 0xFF], 24) ^ AESEngine.T0[n & 0xFF] ^ this.shift(AESEngine.T0[n3 >> 16 & 0xFF], 16) ^ this.shift(AESEngine.T0[n6 >> 24 & 0xFF], 8) ^ array[n5][0]);
            this.C1 = (AESEngine.T0[n2 & 0xFF] ^ this.shift(AESEngine.T0[n3 >> 8 & 0xFF], 24) ^ this.shift(AESEngine.T0[n6 >> 16 & 0xFF], 16) ^ this.shift(AESEngine.T0[n >> 24 & 0xFF], 8) ^ array[n5][1]);
            this.C2 = (AESEngine.T0[n3 & 0xFF] ^ this.shift(AESEngine.T0[n6 >> 8 & 0xFF], 24) ^ this.shift(AESEngine.T0[n >> 16 & 0xFF], 16) ^ this.shift(AESEngine.T0[n2 >> 24 & 0xFF], 8) ^ array[n5][2]);
            this.C3 = (AESEngine.T0[n6 & 0xFF] ^ this.shift(AESEngine.T0[n >> 8 & 0xFF], 24) ^ this.shift(AESEngine.T0[n2 >> 16 & 0xFF], 16) ^ this.shift(AESEngine.T0[n3 >> 24 & 0xFF], 8) ^ array[n5][3]);
        }
        final int n7 = AESEngine.T0[this.C0 & 0xFF] ^ this.shift(AESEngine.T0[this.C1 >> 8 & 0xFF], 24) ^ this.shift(AESEngine.T0[this.C2 >> 16 & 0xFF], 16) ^ this.shift(AESEngine.T0[this.C3 >> 24 & 0xFF], 8) ^ array[i][0];
        final int n8 = AESEngine.T0[this.C1 & 0xFF] ^ this.shift(AESEngine.T0[this.C2 >> 8 & 0xFF], 24) ^ this.shift(AESEngine.T0[this.C3 >> 16 & 0xFF], 16) ^ this.shift(AESEngine.T0[this.C0 >> 24 & 0xFF], 8) ^ array[i][1];
        final int n9 = AESEngine.T0[this.C2 & 0xFF] ^ this.shift(AESEngine.T0[this.C3 >> 8 & 0xFF], 24) ^ this.shift(AESEngine.T0[this.C0 >> 16 & 0xFF], 16) ^ this.shift(AESEngine.T0[this.C1 >> 24 & 0xFF], 8) ^ array[i][2];
        final int n10 = AESEngine.T0[this.C3 & 0xFF];
        final int shift4 = this.shift(AESEngine.T0[this.C0 >> 8 & 0xFF], 24);
        final int shift5 = this.shift(AESEngine.T0[this.C1 >> 16 & 0xFF], 16);
        final int shift6 = this.shift(AESEngine.T0[this.C2 >> 24 & 0xFF], 8);
        final int n11 = i + 1;
        final int n12 = array[i][3] ^ (n10 ^ shift4 ^ shift5 ^ shift6);
        this.C0 = (array[n11][0] ^ ((AESEngine.S[n7 & 0xFF] & 0xFF) ^ (AESEngine.S[n8 >> 8 & 0xFF] & 0xFF) << 8 ^ (AESEngine.S[n9 >> 16 & 0xFF] & 0xFF) << 16 ^ AESEngine.S[n12 >> 24 & 0xFF] << 24));
        this.C1 = ((AESEngine.S[n8 & 0xFF] & 0xFF) ^ (AESEngine.S[n9 >> 8 & 0xFF] & 0xFF) << 8 ^ (AESEngine.S[n12 >> 16 & 0xFF] & 0xFF) << 16 ^ AESEngine.S[n7 >> 24 & 0xFF] << 24 ^ array[n11][1]);
        this.C2 = ((AESEngine.S[n9 & 0xFF] & 0xFF) ^ (AESEngine.S[n12 >> 8 & 0xFF] & 0xFF) << 8 ^ (AESEngine.S[n7 >> 16 & 0xFF] & 0xFF) << 16 ^ AESEngine.S[n8 >> 24 & 0xFF] << 24 ^ array[n11][2]);
        this.C3 = ((AESEngine.S[n12 & 0xFF] & 0xFF) ^ (AESEngine.S[n7 >> 8 & 0xFF] & 0xFF) << 8 ^ (AESEngine.S[n8 >> 16 & 0xFF] & 0xFF) << 16 ^ AESEngine.S[n9 >> 24 & 0xFF] << 24 ^ array[n11][3]);
    }
    
    private int[][] generateWorkingKey(final byte[] array) throws ZipException {
        final int n = array.length / 4;
        if ((n != 4 && n != 6 && n != 8) || n * 4 != array.length) {
            throw new ZipException("invalid key length (not 128/192/256)");
        }
        this.rounds = n + 6;
        final int[][] array2 = (int[][])Array.newInstance(Integer.TYPE, this.rounds + 1, 4);
        for (int n2 = 0, i = 0; i < array.length; i += 4, ++n2) {
            array2[n2 >> 2][n2 & 0x3] = ((array[i] & 0xFF) | (array[i + 1] & 0xFF) << 8 | (array[i + 2] & 0xFF) << 16 | array[i + 3] << 24);
        }
        for (int rounds = this.rounds, j = n; j < rounds + 1 << 2; ++j) {
            final int n3 = array2[j - 1 >> 2][j - 1 & 0x3];
            int subWord;
            if (j % n == 0) {
                subWord = (this.subWord(this.shift(n3, 8)) ^ AESEngine.rcon[j / n - 1]);
            }
            else {
                subWord = n3;
                if (n > 6) {
                    subWord = n3;
                    if (j % n == 4) {
                        subWord = this.subWord(n3);
                    }
                }
            }
            array2[j >> 2][j & 0x3] = (array2[j - n >> 2][j - n & 0x3] ^ subWord);
        }
        return array2;
    }
    
    private int shift(final int n, final int n2) {
        return n >>> n2 | n << -n2;
    }
    
    private final void stateIn(final byte[] array, int n) {
        final int n2 = n + 1;
        this.C0 = (array[n] & 0xFF);
        final int c0 = this.C0;
        n = n2 + 1;
        this.C0 = (c0 | (array[n2] & 0xFF) << 8);
        final int c2 = this.C0;
        final int n3 = n + 1;
        this.C0 = (c2 | (array[n] & 0xFF) << 16);
        final int c3 = this.C0;
        n = n3 + 1;
        this.C0 = (c3 | array[n3] << 24);
        final int n4 = n + 1;
        this.C1 = (array[n] & 0xFF);
        final int c4 = this.C1;
        n = n4 + 1;
        this.C1 = ((array[n4] & 0xFF) << 8 | c4);
        final int c5 = this.C1;
        final int n5 = n + 1;
        this.C1 = (c5 | (array[n] & 0xFF) << 16);
        final int c6 = this.C1;
        n = n5 + 1;
        this.C1 = (c6 | array[n5] << 24);
        final int n6 = n + 1;
        this.C2 = (array[n] & 0xFF);
        final int c7 = this.C2;
        n = n6 + 1;
        this.C2 = ((array[n6] & 0xFF) << 8 | c7);
        final int c8 = this.C2;
        final int n7 = n + 1;
        this.C2 = (c8 | (array[n] & 0xFF) << 16);
        final int c9 = this.C2;
        n = n7 + 1;
        this.C2 = (c9 | array[n7] << 24);
        final int n8 = n + 1;
        this.C3 = (array[n] & 0xFF);
        final int c10 = this.C3;
        n = n8 + 1;
        this.C3 = ((array[n8] & 0xFF) << 8 | c10);
        final int c11 = this.C3;
        final int n9 = n + 1;
        this.C3 = (c11 | (array[n] & 0xFF) << 16);
        this.C3 |= array[n9] << 24;
    }
    
    private final void stateOut(final byte[] array, int n) {
        final int n2 = n + 1;
        array[n] = (byte)this.C0;
        n = n2 + 1;
        array[n2] = (byte)(this.C0 >> 8);
        final int n3 = n + 1;
        array[n] = (byte)(this.C0 >> 16);
        n = n3 + 1;
        array[n3] = (byte)(this.C0 >> 24);
        final int n4 = n + 1;
        array[n] = (byte)this.C1;
        n = n4 + 1;
        array[n4] = (byte)(this.C1 >> 8);
        final int n5 = n + 1;
        array[n] = (byte)(this.C1 >> 16);
        n = n5 + 1;
        array[n5] = (byte)(this.C1 >> 24);
        final int n6 = n + 1;
        array[n] = (byte)this.C2;
        n = n6 + 1;
        array[n6] = (byte)(this.C2 >> 8);
        final int n7 = n + 1;
        array[n] = (byte)(this.C2 >> 16);
        n = n7 + 1;
        array[n7] = (byte)(this.C2 >> 24);
        final int n8 = n + 1;
        array[n] = (byte)this.C3;
        n = n8 + 1;
        array[n8] = (byte)(this.C3 >> 8);
        final int n9 = n + 1;
        array[n] = (byte)(this.C3 >> 16);
        array[n9] = (byte)(this.C3 >> 24);
    }
    
    private int subWord(final int n) {
        return (AESEngine.S[n & 0xFF] & 0xFF) | (AESEngine.S[n >> 8 & 0xFF] & 0xFF) << 8 | (AESEngine.S[n >> 16 & 0xFF] & 0xFF) << 16 | AESEngine.S[n >> 24 & 0xFF] << 24;
    }
    
    public void init(final byte[] array) throws ZipException {
        this.workingKey = this.generateWorkingKey(array);
    }
    
    public int processBlock(final byte[] array, final int n, final byte[] array2, final int n2) throws ZipException {
        if (this.workingKey == null) {
            throw new ZipException("AES engine not initialised");
        }
        if (n + 16 > array.length) {
            throw new ZipException("input buffer too short");
        }
        if (n2 + 16 > array2.length) {
            throw new ZipException("output buffer too short");
        }
        this.stateIn(array, n);
        this.encryptBlock(this.workingKey);
        this.stateOut(array2, n2);
        return 16;
    }
    
    public int processBlock(final byte[] array, final byte[] array2) throws ZipException {
        return this.processBlock(array, 0, array2, 0);
    }
}
