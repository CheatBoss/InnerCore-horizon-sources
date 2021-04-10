package org.spongycastle.crypto.engines;

import java.lang.reflect.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.util.*;
import org.spongycastle.crypto.*;

public class AESEngine implements BlockCipher
{
    private static final int BLOCK_SIZE = 16;
    private static final byte[] S;
    private static final byte[] Si;
    private static final int[] T0;
    private static final int[] Tinv0;
    private static final int m1 = -2139062144;
    private static final int m2 = 2139062143;
    private static final int m3 = 27;
    private static final int m4 = -1061109568;
    private static final int m5 = 1061109567;
    private static final int[] rcon;
    private int C0;
    private int C1;
    private int C2;
    private int C3;
    private int ROUNDS;
    private int[][] WorkingKey;
    private boolean forEncryption;
    private byte[] s;
    
    static {
        S = new byte[] { 99, 124, 119, 123, -14, 107, 111, -59, 48, 1, 103, 43, -2, -41, -85, 118, -54, -126, -55, 125, -6, 89, 71, -16, -83, -44, -94, -81, -100, -92, 114, -64, -73, -3, -109, 38, 54, 63, -9, -52, 52, -91, -27, -15, 113, -40, 49, 21, 4, -57, 35, -61, 24, -106, 5, -102, 7, 18, -128, -30, -21, 39, -78, 117, 9, -125, 44, 26, 27, 110, 90, -96, 82, 59, -42, -77, 41, -29, 47, -124, 83, -47, 0, -19, 32, -4, -79, 91, 106, -53, -66, 57, 74, 76, 88, -49, -48, -17, -86, -5, 67, 77, 51, -123, 69, -7, 2, 127, 80, 60, -97, -88, 81, -93, 64, -113, -110, -99, 56, -11, -68, -74, -38, 33, 16, -1, -13, -46, -51, 12, 19, -20, 95, -105, 68, 23, -60, -89, 126, 61, 100, 93, 25, 115, 96, -127, 79, -36, 34, 42, -112, -120, 70, -18, -72, 20, -34, 94, 11, -37, -32, 50, 58, 10, 73, 6, 36, 92, -62, -45, -84, 98, -111, -107, -28, 121, -25, -56, 55, 109, -115, -43, 78, -87, 108, 86, -12, -22, 101, 122, -82, 8, -70, 120, 37, 46, 28, -90, -76, -58, -24, -35, 116, 31, 75, -67, -117, -118, 112, 62, -75, 102, 72, 3, -10, 14, 97, 53, 87, -71, -122, -63, 29, -98, -31, -8, -104, 17, 105, -39, -114, -108, -101, 30, -121, -23, -50, 85, 40, -33, -116, -95, -119, 13, -65, -26, 66, 104, 65, -103, 45, 15, -80, 84, -69, 22 };
        Si = new byte[] { 82, 9, 106, -43, 48, 54, -91, 56, -65, 64, -93, -98, -127, -13, -41, -5, 124, -29, 57, -126, -101, 47, -1, -121, 52, -114, 67, 68, -60, -34, -23, -53, 84, 123, -108, 50, -90, -62, 35, 61, -18, 76, -107, 11, 66, -6, -61, 78, 8, 46, -95, 102, 40, -39, 36, -78, 118, 91, -94, 73, 109, -117, -47, 37, 114, -8, -10, 100, -122, 104, -104, 22, -44, -92, 92, -52, 93, 101, -74, -110, 108, 112, 72, 80, -3, -19, -71, -38, 94, 21, 70, 87, -89, -115, -99, -124, -112, -40, -85, 0, -116, -68, -45, 10, -9, -28, 88, 5, -72, -77, 69, 6, -48, 44, 30, -113, -54, 63, 15, 2, -63, -81, -67, 3, 1, 19, -118, 107, 58, -111, 17, 65, 79, 103, -36, -22, -105, -14, -49, -50, -16, -76, -26, 115, -106, -84, 116, 34, -25, -83, 53, -123, -30, -7, 55, -24, 28, 117, -33, 110, 71, -15, 26, 113, 29, 41, -59, -119, 111, -73, 98, 14, -86, 24, -66, 27, -4, 86, 62, 75, -58, -46, 121, 32, -102, -37, -64, -2, 120, -51, 90, -12, 31, -35, -88, 51, -120, 7, -57, 49, -79, 18, 16, 89, 39, -128, -20, 95, 96, 81, 127, -87, 25, -75, 74, 13, 45, -27, 122, -97, -109, -55, -100, -17, -96, -32, 59, 77, -82, 42, -11, -80, -56, -21, -69, 60, -125, 83, -103, 97, 23, 43, 4, 126, -70, 119, -42, 38, -31, 105, 20, 99, 85, 33, 12, 125 };
        rcon = new int[] { 1, 2, 4, 8, 16, 32, 64, 128, 27, 54, 108, 216, 171, 77, 154, 47, 94, 188, 99, 198, 151, 53, 106, 212, 179, 125, 250, 239, 197, 145 };
        T0 = new int[] { -1520213050, -2072216328, -1720223762, -1921287178, 234025727, -1117033514, -1318096930, 1422247313, 1345335392, 50397442, -1452841010, 2099981142, 436141799, 1658312629, -424957107, -1703512340, 1170918031, -1652391393, 1086966153, -2021818886, 368769775, -346465870, -918075506, 200339707, -324162239, 1742001331, -39673249, -357585083, -1080255453, -140204973, -1770884380, 1539358875, -1028147339, 486407649, -1366060227, 1780885068, 1513502316, 1094664062, 49805301, 1338821763, 1546925160, -190470831, 887481809, 150073849, -1821281822, 1943591083, 1395732834, 1058346282, 201589768, 1388824469, 1696801606, 1589887901, 672667696, -1583966665, 251987210, -1248159185, 151455502, 907153956, -1686077413, 1038279391, 652995533, 1764173646, -843926913, -1619692054, 453576978, -1635548387, 1949051992, 773462580, 756751158, -1301385508, -296068428, -73359269, -162377052, 1295727478, 1641469623, -827083907, 2066295122, 1055122397, 1898917726, -1752923117, -179088474, 1758581177, 0, 753790401, 1612718144, 536673507, -927878791, -312779850, -1100322092, 1187761037, -641810841, 1262041458, -565556588, -733197160, -396863312, 1255133061, 1808847035, 720367557, -441800113, 385612781, -985447546, -682799718, 1429418854, -1803188975, -817543798, 284817897, 100794884, -2122350594, -263171936, 1144798328, -1163944155, -475486133, -212774494, -22830243, -1069531008, -1970303227, -1382903233, -1130521311, 1211644016, 83228145, -541279133, -1044990345, 1977277103, 1663115586, 806359072, 452984805, 250868733, 1842533055, 1288555905, 336333848, 890442534, 804056259, -513843266, -1567123659, -867941240, 957814574, 1472513171, -223893675, -2105639172, 1195195770, -1402706744, -413311558, 723065138, -1787595802, -1604296512, -1736343271, -783331426, 2145180835, 1713513028, 2116692564, -1416589253, -2088204277, -901364084, 703524551, -742868885, 1007948840, 2044649127, -497131844, 487262998, 1994120109, 1004593371, 1446130276, 1312438900, 503974420, -615954030, 168166924, 1814307912, -463709000, 1573044895, 1859376061, -273896381, -1503501628, -1466855111, -1533700815, 937747667, -1954973198, 854058965, 1137232011, 1496790894, -1217565222, -1936880383, 1691735473, -766620004, -525751991, -1267962664, -95005012, 133494003, 636152527, -1352309302, -1904575756, -374428089, 403179536, -709182865, -2005370640, 1864705354, 1915629148, 605822008, -240736681, -944458637, 1371981463, 602466507, 2094914977, -1670089496, 555687742, -582268010, -591544991, -2037675251, -2054518257, -1871679264, 1111375484, -994724495, -1436129588, -666351472, 84083462, 32962295, 302911004, -1553899070, 1597322602, -111716434, -793134743, -1853454825, 1489093017, 656219450, -1180787161, 954327513, 335083755, -1281845205, 856756514, -1150719534, 1893325225, -1987146233, -1483434957, -1231316179, 572399164, -1836611819, 552200649, 1238290055, -11184726, 2015897680, 2061492133, -1886614525, -123625127, -2138470135, 386731290, -624967835, 837215959, -968736124, -1201116976, -1019133566, -1332111063, 1999449434, 286199582, -877612933, -61582168, -692339859, 974525996 };
        Tinv0 = new int[] { 1353184337, 1399144830, -1012656358, -1772214470, -882136261, -247096033, -1420232020, -1828461749, 1442459680, -160598355, -1854485368, 625738485, -52959921, -674551099, -2143013594, -1885117771, 1230680542, 1729870373, -1743852987, -507445667, 41234371, 317738113, -1550367091, -956705941, -413167869, -1784901099, -344298049, -631680363, 763608788, -752782248, 694804553, 1154009486, 1787413109, 2021232372, 1799248025, -579749593, -1236278850, 397248752, 1722556617, -1271214467, 407560035, -2110711067, 1613975959, 1165972322, -529046351, -2068943941, 480281086, -1809118983, 1483229296, 436028815, -2022908268, -1208452270, 601060267, -503166094, 1468997603, 715871590, 120122290, 63092015, -1703164538, -1526188077, -226023376, -1297760477, -1167457534, 1552029421, 723308426, -1833666137, -252573709, -1578997426, -839591323, -708967162, 526529745, -1963022652, -1655493068, -1604979806, 853641733, 1978398372, 971801355, -1427152832, 111112542, 1360031421, -108388034, 1023860118, -1375387939, 1186850381, -1249028975, 90031217, 1876166148, -15380384, 620468249, -1746289194, -868007799, 2006899047, -1119688528, -2004121337, 945494503, -605108103, 1191869601, -384875908, -920746760, 0, -2088337399, 1223502642, -1401941730, 1316117100, -67170563, 1446544655, 517320253, 658058550, 1691946762, 564550760, -783000677, 976107044, -1318647284, 266819475, -761860428, -1634624741, 1338359936, -1574904735, 1766553434, 370807324, 179999714, -450191168, 1138762300, 488053522, 185403662, -1379431438, -1180125651, -928440812, -2061897385, 1275557295, -1143105042, -44007517, -1624899081, -1124765092, -985962940, 880737115, 1982415755, -590994485, 1761406390, 1676797112, -891538985, 277177154, 1076008723, 538035844, 2099530373, -130171950, 288553390, 1839278535, 1261411869, -214912292, -330136051, -790380169, 1813426987, -1715900247, -95906799, 577038663, -997393240, 440397984, -668172970, -275762398, -951170681, -1043253031, -22885748, 906744984, -813566554, 685669029, 646887386, -1530942145, -459458004, 227702864, -1681105046, 1648787028, -1038905866, -390539120, 1593260334, -173030526, -1098883681, 2090061929, -1456614033, -1290656305, 999926984, -1484974064, 1852021992, 2075868123, 158869197, -199730834, 28809964, -1466282109, 1701746150, 2129067946, 147831841, -420997649, -644094022, -835293366, -737566742, -696471511, -1347247055, 824393514, 815048134, -1067015627, 935087732, -1496677636, -1328508704, 366520115, 1251476721, -136647615, 240176511, 804688151, -1915335306, 1303441219, 1414376140, -553347356, -474623586, 461924940, -1205916479, 2136040774, 82468509, 1563790337, 1937016826, 776014843, 1511876531, 1389550482, 861278441, 323475053, -1939744870, 2047648055, -1911228327, -1992551445, -299390514, 902390199, -303751967, 1018251130, 1507840668, 1064563285, 2043548696, -1086863501, -355600557, 1537932639, 342834655, -2032450440, -2114736182, 1053059257, 741614648, 1598071746, 1925389590, 203809468, -1958134744, 1100287487, 1895934009, -558691320, -1662733096, -1866377628, 1636092795, 1890988757, 1952214088, 1113045200 };
    }
    
    public AESEngine() {
        this.WorkingKey = null;
    }
    
    private static int FFmulX(final int n) {
        return (n & 0x7F7F7F7F) << 1 ^ ((0x80808080 & n) >>> 7) * 27;
    }
    
    private static int FFmulX2(final int n) {
        final int n2 = 0xC0C0C0C0 & n;
        final int n3 = n2 ^ n2 >>> 1;
        return (n & 0x3F3F3F3F) << 2 ^ n3 >>> 2 ^ n3 >>> 5;
    }
    
    private void decryptBlock(final int[][] array) {
        final int c0 = this.C0;
        final int rounds = this.ROUNDS;
        int n = c0 ^ array[rounds][0];
        int n2 = this.C1 ^ array[rounds][1];
        int n3 = this.C2 ^ array[rounds][2];
        int i = rounds - 1;
        int n4 = array[rounds][3] ^ this.C3;
        while (i > 1) {
            final int[] tinv0 = AESEngine.Tinv0;
            final int n5 = shift(tinv0[n4 >> 8 & 0xFF], 24) ^ tinv0[n & 0xFF] ^ shift(AESEngine.Tinv0[n3 >> 16 & 0xFF], 16) ^ shift(AESEngine.Tinv0[n2 >> 24 & 0xFF], 8) ^ array[i][0];
            final int[] tinv2 = AESEngine.Tinv0;
            final int n6 = shift(tinv2[n >> 8 & 0xFF], 24) ^ tinv2[n2 & 0xFF] ^ shift(AESEngine.Tinv0[n4 >> 16 & 0xFF], 16) ^ shift(AESEngine.Tinv0[n3 >> 24 & 0xFF], 8) ^ array[i][1];
            final int[] tinv3 = AESEngine.Tinv0;
            final int n7 = shift(tinv3[n2 >> 8 & 0xFF], 24) ^ tinv3[n3 & 0xFF] ^ shift(AESEngine.Tinv0[n >> 16 & 0xFF], 16) ^ shift(AESEngine.Tinv0[n4 >> 24 & 0xFF], 8) ^ array[i][2];
            final int[] tinv4 = AESEngine.Tinv0;
            final int n8 = tinv4[n4 & 0xFF];
            final int shift = shift(tinv4[n3 >> 8 & 0xFF], 24);
            final int shift2 = shift(AESEngine.Tinv0[n2 >> 16 & 0xFF], 16);
            final int shift3 = shift(AESEngine.Tinv0[n >> 24 & 0xFF], 8);
            final int n9 = i - 1;
            final int n10 = shift3 ^ (n8 ^ shift ^ shift2) ^ array[i][3];
            final int[] tinv5 = AESEngine.Tinv0;
            final int n11 = tinv5[n5 & 0xFF];
            final int shift4 = shift(tinv5[n10 >> 8 & 0xFF], 24);
            final int shift5 = shift(AESEngine.Tinv0[n7 >> 16 & 0xFF], 16);
            final int shift6 = shift(AESEngine.Tinv0[n6 >> 24 & 0xFF], 8);
            final int n12 = array[n9][0];
            final int[] tinv6 = AESEngine.Tinv0;
            final int n13 = tinv6[n6 & 0xFF];
            final int shift7 = shift(tinv6[n5 >> 8 & 0xFF], 24);
            final int shift8 = shift(AESEngine.Tinv0[n10 >> 16 & 0xFF], 16);
            final int shift9 = shift(AESEngine.Tinv0[n7 >> 24 & 0xFF], 8);
            final int n14 = array[n9][1];
            final int[] tinv7 = AESEngine.Tinv0;
            final int n15 = tinv7[n7 & 0xFF];
            final int shift10 = shift(tinv7[n6 >> 8 & 0xFF], 24);
            final int shift11 = shift(AESEngine.Tinv0[n5 >> 16 & 0xFF], 16);
            final int shift12 = shift(AESEngine.Tinv0[n10 >> 24 & 0xFF], 8);
            final int n16 = array[n9][2];
            final int[] tinv8 = AESEngine.Tinv0;
            final int n17 = tinv8[n10 & 0xFF];
            final int shift13 = shift(tinv8[n7 >> 8 & 0xFF], 24);
            final int shift14 = shift(AESEngine.Tinv0[n6 >> 16 & 0xFF], 16);
            final int shift15 = shift(AESEngine.Tinv0[n5 >> 24 & 0xFF], 8);
            final int n18 = array[n9][3];
            n2 = (shift7 ^ n13 ^ shift8 ^ shift9 ^ n14);
            n3 = (shift12 ^ (shift10 ^ n15 ^ shift11) ^ n16);
            i = n9 - 1;
            n4 = (n17 ^ shift13 ^ shift14 ^ shift15 ^ n18);
            n = (shift4 ^ n11 ^ shift5 ^ shift6 ^ n12);
        }
        final int[] tinv9 = AESEngine.Tinv0;
        final int n19 = shift(tinv9[n4 >> 8 & 0xFF], 24) ^ tinv9[n & 0xFF] ^ shift(AESEngine.Tinv0[n3 >> 16 & 0xFF], 16) ^ shift(AESEngine.Tinv0[n2 >> 24 & 0xFF], 8) ^ array[i][0];
        final int[] tinv10 = AESEngine.Tinv0;
        final int n20 = shift(tinv10[n >> 8 & 0xFF], 24) ^ tinv10[n2 & 0xFF] ^ shift(AESEngine.Tinv0[n4 >> 16 & 0xFF], 16) ^ shift(AESEngine.Tinv0[n3 >> 24 & 0xFF], 8) ^ array[i][1];
        final int[] tinv11 = AESEngine.Tinv0;
        final int n21 = shift(tinv11[n2 >> 8 & 0xFF], 24) ^ tinv11[n3 & 0xFF] ^ shift(AESEngine.Tinv0[n >> 16 & 0xFF], 16) ^ shift(AESEngine.Tinv0[n4 >> 24 & 0xFF], 8) ^ array[i][2];
        final int[] tinv12 = AESEngine.Tinv0;
        final int n22 = shift(AESEngine.Tinv0[n >> 24 & 0xFF], 8) ^ (tinv12[n4 & 0xFF] ^ shift(tinv12[n3 >> 8 & 0xFF], 24) ^ shift(AESEngine.Tinv0[n2 >> 16 & 0xFF], 16)) ^ array[i][3];
        final byte[] si = AESEngine.Si;
        final byte b = si[n19 & 0xFF];
        final byte[] s = this.s;
        this.C0 = ((b & 0xFF) ^ (s[n22 >> 8 & 0xFF] & 0xFF) << 8 ^ (s[n21 >> 16 & 0xFF] & 0xFF) << 16 ^ si[n20 >> 24 & 0xFF] << 24 ^ array[0][0]);
        this.C1 = ((s[n20 & 0xFF] & 0xFF) ^ (s[n19 >> 8 & 0xFF] & 0xFF) << 8 ^ (si[n22 >> 16 & 0xFF] & 0xFF) << 16 ^ s[n21 >> 24 & 0xFF] << 24 ^ array[0][1]);
        this.C2 = ((s[n21 & 0xFF] & 0xFF) ^ (si[n20 >> 8 & 0xFF] & 0xFF) << 8 ^ (si[n19 >> 16 & 0xFF] & 0xFF) << 16 ^ s[n22 >> 24 & 0xFF] << 24 ^ array[0][2]);
        this.C3 = (s[n19 >> 24 & 0xFF] << 24 ^ ((si[n22 & 0xFF] & 0xFF) ^ (s[n21 >> 8 & 0xFF] & 0xFF) << 8 ^ (s[n20 >> 16 & 0xFF] & 0xFF) << 16) ^ array[0][3]);
    }
    
    private void encryptBlock(final int[][] array) {
        final int c0 = this.C0;
        final int n = array[0][0];
        final int c2 = this.C1;
        final int n2 = array[0][1];
        final int c3 = this.C2;
        final int n3 = array[0][2];
        int n4 = this.C3 ^ array[0][3];
        int n5 = c3 ^ n3;
        int n6 = c2 ^ n2;
        int n7 = c0 ^ n;
        int i;
        int n12;
        int n16;
        int shift7;
        int shift8;
        int shift9;
        int n17;
        for (i = 1; i < this.ROUNDS - 1; i = n12 + 1, n6 = (n17 ^ (n16 ^ shift7 ^ shift8 ^ shift9))) {
            final int[] t0 = AESEngine.T0;
            final int n8 = shift(t0[n6 >> 8 & 0xFF], 24) ^ t0[n7 & 0xFF] ^ shift(AESEngine.T0[n5 >> 16 & 0xFF], 16) ^ shift(AESEngine.T0[n4 >> 24 & 0xFF], 8) ^ array[i][0];
            final int[] t2 = AESEngine.T0;
            final int n9 = shift(t2[n5 >> 8 & 0xFF], 24) ^ t2[n6 & 0xFF] ^ shift(AESEngine.T0[n4 >> 16 & 0xFF], 16) ^ shift(AESEngine.T0[n7 >> 24 & 0xFF], 8) ^ array[i][1];
            final int[] t3 = AESEngine.T0;
            final int n10 = shift(t3[n4 >> 8 & 0xFF], 24) ^ t3[n5 & 0xFF] ^ shift(AESEngine.T0[n7 >> 16 & 0xFF], 16) ^ shift(AESEngine.T0[n6 >> 24 & 0xFF], 8) ^ array[i][2];
            final int[] t4 = AESEngine.T0;
            final int n11 = t4[n4 & 0xFF];
            final int shift = shift(t4[n7 >> 8 & 0xFF], 24);
            final int shift2 = shift(AESEngine.T0[n6 >> 16 & 0xFF], 16);
            final int shift3 = shift(AESEngine.T0[n5 >> 24 & 0xFF], 8);
            n12 = i + 1;
            final int n13 = array[i][3] ^ (shift ^ n11 ^ shift2 ^ shift3);
            final int[] t5 = AESEngine.T0;
            final int n14 = t5[n8 & 0xFF];
            final int shift4 = shift(t5[n9 >> 8 & 0xFF], 24);
            final int shift5 = shift(AESEngine.T0[n10 >> 16 & 0xFF], 16);
            final int shift6 = shift(AESEngine.T0[n13 >> 24 & 0xFF], 8);
            final int n15 = array[n12][0];
            final int[] t6 = AESEngine.T0;
            n16 = t6[n9 & 0xFF];
            shift7 = shift(t6[n10 >> 8 & 0xFF], 24);
            shift8 = shift(AESEngine.T0[n13 >> 16 & 0xFF], 16);
            shift9 = shift(AESEngine.T0[n8 >> 24 & 0xFF], 8);
            n17 = array[n12][1];
            final int[] t7 = AESEngine.T0;
            final int n18 = t7[n10 & 0xFF];
            final int shift10 = shift(t7[n13 >> 8 & 0xFF], 24);
            final int shift11 = shift(AESEngine.T0[n8 >> 16 & 0xFF], 16);
            final int shift12 = shift(AESEngine.T0[n9 >> 24 & 0xFF], 8);
            final int n19 = array[n12][2];
            final int[] t8 = AESEngine.T0;
            final int n20 = t8[n13 & 0xFF];
            final int shift13 = shift(t8[n8 >> 8 & 0xFF], 24);
            final int shift14 = shift(AESEngine.T0[n9 >> 16 & 0xFF], 16);
            final int shift15 = shift(AESEngine.T0[n10 >> 24 & 0xFF], 8);
            final int n21 = array[n12][3];
            n7 = (shift4 ^ n14 ^ shift5 ^ shift6 ^ n15);
            n5 = (shift10 ^ n18 ^ shift11 ^ shift12 ^ n19);
            n4 = (n20 ^ shift13 ^ shift14 ^ shift15 ^ n21);
        }
        final int[] t9 = AESEngine.T0;
        final int n22 = shift(t9[n6 >> 8 & 0xFF], 24) ^ t9[n7 & 0xFF] ^ shift(AESEngine.T0[n5 >> 16 & 0xFF], 16) ^ shift(AESEngine.T0[n4 >> 24 & 0xFF], 8) ^ array[i][0];
        final int[] t10 = AESEngine.T0;
        final int n23 = shift(t10[n5 >> 8 & 0xFF], 24) ^ t10[n6 & 0xFF] ^ shift(AESEngine.T0[n4 >> 16 & 0xFF], 16) ^ shift(AESEngine.T0[n7 >> 24 & 0xFF], 8) ^ array[i][1];
        final int[] t11 = AESEngine.T0;
        final int n24 = shift(t11[n4 >> 8 & 0xFF], 24) ^ t11[n5 & 0xFF] ^ shift(AESEngine.T0[n7 >> 16 & 0xFF], 16) ^ shift(AESEngine.T0[n6 >> 24 & 0xFF], 8) ^ array[i][2];
        final int[] t12 = AESEngine.T0;
        final int n25 = t12[n4 & 0xFF];
        final int shift16 = shift(t12[n7 >> 8 & 0xFF], 24);
        final int shift17 = shift(AESEngine.T0[n6 >> 16 & 0xFF], 16);
        final int shift18 = shift(AESEngine.T0[n5 >> 24 & 0xFF], 8);
        final int n26 = i + 1;
        final int n27 = array[i][3] ^ (shift16 ^ n25 ^ shift17 ^ shift18);
        final byte[] s = AESEngine.S;
        final byte b = s[n22 & 0xFF];
        final byte b2 = s[n23 >> 8 & 0xFF];
        final byte[] s2 = this.s;
        this.C0 = ((b & 0xFF) ^ (b2 & 0xFF) << 8 ^ (s2[n24 >> 16 & 0xFF] & 0xFF) << 16 ^ s2[n27 >> 24 & 0xFF] << 24 ^ array[n26][0]);
        this.C1 = ((s2[n23 & 0xFF] & 0xFF) ^ (s[n24 >> 8 & 0xFF] & 0xFF) << 8 ^ (s[n27 >> 16 & 0xFF] & 0xFF) << 16 ^ s2[n22 >> 24 & 0xFF] << 24 ^ array[n26][1]);
        this.C2 = ((s2[n24 & 0xFF] & 0xFF) ^ (s[n27 >> 8 & 0xFF] & 0xFF) << 8 ^ (s[n22 >> 16 & 0xFF] & 0xFF) << 16 ^ s[n23 >> 24 & 0xFF] << 24 ^ array[n26][2]);
        this.C3 = ((s2[n22 >> 8 & 0xFF] & 0xFF) << 8 ^ (s2[n27 & 0xFF] & 0xFF) ^ (s2[n23 >> 16 & 0xFF] & 0xFF) << 16 ^ s[n24 >> 24 & 0xFF] << 24 ^ array[n26][3]);
    }
    
    private int[][] generateWorkingKey(final byte[] array, final boolean b) {
        final int length = array.length;
        if (length >= 16 && length <= 32 && (length & 0x7) == 0x0) {
            final int n = length >>> 2;
            final int rounds = n + 6;
            this.ROUNDS = rounds;
            final Class<Integer> type = Integer.TYPE;
            final int n2 = 1;
            final int[][] array2 = (int[][])Array.newInstance(type, rounds + 1, 4);
            if (n != 4) {
                if (n != 6) {
                    if (n != 8) {
                        throw new IllegalStateException("Should never get here");
                    }
                    final int littleEndianToInt = Pack.littleEndianToInt(array, 0);
                    array2[0][0] = littleEndianToInt;
                    int littleEndianToInt2 = Pack.littleEndianToInt(array, 4);
                    array2[0][1] = littleEndianToInt2;
                    int littleEndianToInt3 = Pack.littleEndianToInt(array, 8);
                    array2[0][2] = littleEndianToInt3;
                    int littleEndianToInt4 = Pack.littleEndianToInt(array, 12);
                    array2[0][3] = littleEndianToInt4;
                    int littleEndianToInt5 = Pack.littleEndianToInt(array, 16);
                    array2[1][0] = littleEndianToInt5;
                    int littleEndianToInt6 = Pack.littleEndianToInt(array, 20);
                    array2[1][1] = littleEndianToInt6;
                    int littleEndianToInt7 = Pack.littleEndianToInt(array, 24);
                    array2[1][2] = littleEndianToInt7;
                    int littleEndianToInt8 = Pack.littleEndianToInt(array, 28);
                    array2[1][3] = littleEndianToInt8;
                    int i = 2;
                    final int n3 = 1;
                    int n4 = littleEndianToInt;
                    int n5;
                    for (n5 = n3; i < 14; i += 2, n5 <<= 1) {
                        n4 ^= (n5 ^ subWord(shift(littleEndianToInt8, 8)));
                        array2[i][0] = n4;
                        littleEndianToInt2 ^= n4;
                        array2[i][1] = littleEndianToInt2;
                        littleEndianToInt3 ^= littleEndianToInt2;
                        array2[i][2] = littleEndianToInt3;
                        littleEndianToInt4 ^= littleEndianToInt3;
                        array2[i][3] = littleEndianToInt4;
                        littleEndianToInt5 ^= subWord(littleEndianToInt4);
                        final int n6 = i + 1;
                        array2[n6][0] = littleEndianToInt5;
                        littleEndianToInt6 ^= littleEndianToInt5;
                        array2[n6][1] = littleEndianToInt6;
                        littleEndianToInt7 ^= littleEndianToInt6;
                        array2[n6][2] = littleEndianToInt7;
                        littleEndianToInt8 ^= littleEndianToInt7;
                        array2[n6][3] = littleEndianToInt8;
                    }
                    final int n7 = n5 ^ subWord(shift(littleEndianToInt8, 8)) ^ n4;
                    array2[14][0] = n7;
                    final int n8 = n7 ^ littleEndianToInt2;
                    array2[14][1] = n8;
                    final int n9 = n8 ^ littleEndianToInt3;
                    array2[14][2] = n9;
                    array2[14][3] = (n9 ^ littleEndianToInt4);
                }
                else {
                    final int littleEndianToInt9 = Pack.littleEndianToInt(array, 0);
                    array2[0][0] = littleEndianToInt9;
                    final int littleEndianToInt10 = Pack.littleEndianToInt(array, 4);
                    array2[0][1] = littleEndianToInt10;
                    final int littleEndianToInt11 = Pack.littleEndianToInt(array, 8);
                    array2[0][2] = littleEndianToInt11;
                    final int littleEndianToInt12 = Pack.littleEndianToInt(array, 12);
                    array2[0][3] = littleEndianToInt12;
                    final int littleEndianToInt13 = Pack.littleEndianToInt(array, 16);
                    array2[1][0] = littleEndianToInt13;
                    final int littleEndianToInt14 = Pack.littleEndianToInt(array, 20);
                    array2[1][1] = littleEndianToInt14;
                    int n10 = littleEndianToInt9 ^ (subWord(shift(littleEndianToInt14, 8)) ^ 0x1);
                    array2[1][2] = n10;
                    int n11 = littleEndianToInt10 ^ n10;
                    array2[1][3] = n11;
                    int n12 = littleEndianToInt11 ^ n11;
                    array2[2][0] = n12;
                    int n13 = littleEndianToInt12 ^ n12;
                    array2[2][1] = n13;
                    int n14 = littleEndianToInt13 ^ n13;
                    array2[2][2] = n14;
                    int n15 = littleEndianToInt14 ^ n14;
                    array2[2][3] = n15;
                    int n16 = 2;
                    int n17;
                    for (int j = 3; j < 12; j += 3, n16 = n17 << 1) {
                        final int subWord = subWord(shift(n15, 8));
                        n17 = n16 << 1;
                        final int n18 = n16 ^ subWord ^ n10;
                        array2[j][0] = n18;
                        final int n19 = n11 ^ n18;
                        array2[j][1] = n19;
                        final int n20 = n12 ^ n19;
                        array2[j][2] = n20;
                        final int n21 = n13 ^ n20;
                        array2[j][3] = n21;
                        final int n22 = n14 ^ n21;
                        final int n23 = j + 1;
                        array2[n23][0] = n22;
                        final int n24 = n15 ^ n22;
                        array2[n23][1] = n24;
                        n10 = (n18 ^ (subWord(shift(n24, 8)) ^ n17));
                        array2[n23][2] = n10;
                        n11 = (n19 ^ n10);
                        array2[n23][3] = n11;
                        n12 = (n20 ^ n11);
                        final int n25 = j + 2;
                        array2[n25][0] = n12;
                        n13 = (n21 ^ n12);
                        array2[n25][1] = n13;
                        n14 = (n22 ^ n13);
                        array2[n25][2] = n14;
                        n15 = (n24 ^ n14);
                        array2[n25][3] = n15;
                    }
                    final int n26 = subWord(shift(n15, 8)) ^ n16 ^ n10;
                    array2[12][0] = n26;
                    final int n27 = n26 ^ n11;
                    array2[12][1] = n27;
                    final int n28 = n27 ^ n12;
                    array2[12][2] = n28;
                    array2[12][3] = (n28 ^ n13);
                }
            }
            else {
                int littleEndianToInt15 = Pack.littleEndianToInt(array, 0);
                array2[0][0] = littleEndianToInt15;
                int littleEndianToInt16 = Pack.littleEndianToInt(array, 4);
                array2[0][1] = littleEndianToInt16;
                int littleEndianToInt17 = Pack.littleEndianToInt(array, 8);
                array2[0][2] = littleEndianToInt17;
                int littleEndianToInt18 = Pack.littleEndianToInt(array, 12);
                array2[0][3] = littleEndianToInt18;
                for (int k = 1; k <= 10; ++k) {
                    littleEndianToInt15 ^= (subWord(shift(littleEndianToInt18, 8)) ^ AESEngine.rcon[k - 1]);
                    array2[k][0] = littleEndianToInt15;
                    littleEndianToInt16 ^= littleEndianToInt15;
                    array2[k][1] = littleEndianToInt16;
                    littleEndianToInt17 ^= littleEndianToInt16;
                    array2[k][2] = littleEndianToInt17;
                    littleEndianToInt18 ^= littleEndianToInt17;
                    array2[k][3] = littleEndianToInt18;
                }
            }
            if (!b) {
                for (int l = n2; l < this.ROUNDS; ++l) {
                    for (int n29 = 0; n29 < 4; ++n29) {
                        array2[l][n29] = inv_mcol(array2[l][n29]);
                    }
                }
            }
            return array2;
        }
        throw new IllegalArgumentException("Key length not 128/192/256 bits.");
    }
    
    private static int inv_mcol(int n) {
        final int n2 = shift(n, 8) ^ n;
        n ^= FFmulX(n2);
        final int n3 = n2 ^ FFmulX2(n);
        return n ^ (n3 ^ shift(n3, 16));
    }
    
    private void packBlock(final byte[] array, int n) {
        final int n2 = n + 1;
        final int c0 = this.C0;
        array[n] = (byte)c0;
        n = n2 + 1;
        array[n2] = (byte)(c0 >> 8);
        final int n3 = n + 1;
        array[n] = (byte)(c0 >> 16);
        final int n4 = n3 + 1;
        array[n3] = (byte)(c0 >> 24);
        final int n5 = n4 + 1;
        n = this.C1;
        array[n4] = (byte)n;
        final int n6 = n5 + 1;
        array[n5] = (byte)(n >> 8);
        final int n7 = n6 + 1;
        array[n6] = (byte)(n >> 16);
        final int n8 = n7 + 1;
        array[n7] = (byte)(n >> 24);
        final int n9 = n8 + 1;
        n = this.C2;
        array[n8] = (byte)n;
        final int n10 = n9 + 1;
        array[n9] = (byte)(n >> 8);
        final int n11 = n10 + 1;
        array[n10] = (byte)(n >> 16);
        final int n12 = n11 + 1;
        array[n11] = (byte)(n >> 24);
        n = n12 + 1;
        final int c2 = this.C3;
        array[n12] = (byte)c2;
        final int n13 = n + 1;
        array[n] = (byte)(c2 >> 8);
        array[n13] = (byte)(c2 >> 16);
        array[n13 + 1] = (byte)(c2 >> 24);
    }
    
    private static int shift(final int n, final int n2) {
        return n >>> n2 | n << -n2;
    }
    
    private static int subWord(final int n) {
        final byte[] s = AESEngine.S;
        return s[n >> 24 & 0xFF] << 24 | ((s[n & 0xFF] & 0xFF) | (s[n >> 8 & 0xFF] & 0xFF) << 8 | (s[n >> 16 & 0xFF] & 0xFF) << 16);
    }
    
    private void unpackBlock(final byte[] array, int n) {
        final int n2 = n + 1;
        final int c0 = array[n] & 0xFF;
        this.C0 = c0;
        n = n2 + 1;
        final int c2 = c0 | (array[n2] & 0xFF) << 8;
        this.C0 = c2;
        final int n3 = n + 1;
        final int c3 = c2 | (array[n] & 0xFF) << 16;
        this.C0 = c3;
        n = n3 + 1;
        this.C0 = (c3 | array[n3] << 24);
        final int n4 = n + 1;
        final int c4 = array[n] & 0xFF;
        this.C1 = c4;
        n = n4 + 1;
        final int c5 = (array[n4] & 0xFF) << 8 | c4;
        this.C1 = c5;
        final int n5 = n + 1;
        final int c6 = c5 | (array[n] & 0xFF) << 16;
        this.C1 = c6;
        n = n5 + 1;
        this.C1 = (c6 | array[n5] << 24);
        final int n6 = n + 1;
        final int c7 = array[n] & 0xFF;
        this.C2 = c7;
        n = n6 + 1;
        final int c8 = (array[n6] & 0xFF) << 8 | c7;
        this.C2 = c8;
        final int n7 = n + 1;
        final int c9 = c8 | (array[n] & 0xFF) << 16;
        this.C2 = c9;
        n = n7 + 1;
        this.C2 = (c9 | array[n7] << 24);
        final int n8 = n + 1;
        final int c10 = array[n] & 0xFF;
        this.C3 = c10;
        n = n8 + 1;
        final int c11 = (array[n8] & 0xFF) << 8 | c10;
        this.C3 = c11;
        final int c12 = c11 | (array[n] & 0xFF) << 16;
        this.C3 = c12;
        this.C3 = (array[n + 1] << 24 | c12);
    }
    
    @Override
    public String getAlgorithmName() {
        return "AES";
    }
    
    @Override
    public int getBlockSize() {
        return 16;
    }
    
    @Override
    public void init(final boolean forEncryption, final CipherParameters cipherParameters) {
        if (cipherParameters instanceof KeyParameter) {
            this.WorkingKey = this.generateWorkingKey(((KeyParameter)cipherParameters).getKey(), forEncryption);
            this.forEncryption = forEncryption;
            byte[] array;
            if (forEncryption) {
                array = AESEngine.S;
            }
            else {
                array = AESEngine.Si;
            }
            this.s = Arrays.clone(array);
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("invalid parameter passed to AES init - ");
        sb.append(cipherParameters.getClass().getName());
        throw new IllegalArgumentException(sb.toString());
    }
    
    @Override
    public int processBlock(final byte[] array, final int n, final byte[] array2, final int n2) {
        if (this.WorkingKey == null) {
            throw new IllegalStateException("AES engine not initialised");
        }
        if (n + 16 > array.length) {
            throw new DataLengthException("input buffer too short");
        }
        if (n2 + 16 <= array2.length) {
            if (this.forEncryption) {
                this.unpackBlock(array, n);
                this.encryptBlock(this.WorkingKey);
            }
            else {
                this.unpackBlock(array, n);
                this.decryptBlock(this.WorkingKey);
            }
            this.packBlock(array2, n2);
            return 16;
        }
        throw new OutputLengthException("output buffer too short");
    }
    
    @Override
    public void reset() {
    }
}
