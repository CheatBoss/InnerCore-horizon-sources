package org.mozilla.javascript;

import java.math.*;

class DToA
{
    private static final int Bias = 1023;
    private static final int Bletch = 16;
    private static final int Bndry_mask = 1048575;
    static final int DTOSTR_EXPONENTIAL = 3;
    static final int DTOSTR_FIXED = 2;
    static final int DTOSTR_PRECISION = 4;
    static final int DTOSTR_STANDARD = 0;
    static final int DTOSTR_STANDARD_EXPONENTIAL = 1;
    private static final int Exp_11 = 1072693248;
    private static final int Exp_mask = 2146435072;
    private static final int Exp_mask_shifted = 2047;
    private static final int Exp_msk1 = 1048576;
    private static final long Exp_msk1L = 4503599627370496L;
    private static final int Exp_shift = 20;
    private static final int Exp_shift1 = 20;
    private static final int Exp_shiftL = 52;
    private static final int Frac_mask = 1048575;
    private static final int Frac_mask1 = 1048575;
    private static final long Frac_maskL = 4503599627370495L;
    private static final int Int_max = 14;
    private static final int Log2P = 1;
    private static final int P = 53;
    private static final int Quick_max = 14;
    private static final int Sign_bit = Integer.MIN_VALUE;
    private static final int Ten_pmax = 22;
    private static final double[] bigtens;
    private static final int[] dtoaModes;
    private static final int n_bigtens = 5;
    private static final double[] tens;
    
    static {
        tens = new double[] { 1.0, 10.0, 100.0, 1000.0, 10000.0, 100000.0, 1000000.0, 1.0E7, 1.0E8, 1.0E9, 1.0E10, 1.0E11, 1.0E12, 1.0E13, 1.0E14, 1.0E15, 1.0E16, 1.0E17, 1.0E18, 1.0E19, 1.0E20, 1.0E21, 1.0E22 };
        bigtens = new double[] { 1.0E16, 1.0E32, 1.0E64, 1.0E128, 1.0E256 };
        dtoaModes = new int[] { 0, 0, 3, 2, 2 };
    }
    
    private static char BASEDIGIT(int n) {
        if (n >= 10) {
            n += 87;
        }
        else {
            n += 48;
        }
        return (char)n;
    }
    
    static int JS_dtoa(double setWord0, int n, final boolean b, int n2, final boolean[] array, final StringBuilder sb) {
        final int n3 = n;
        final int[] array2 = { 0 };
        final int[] array3 = { 0 };
        if ((word0(setWord0) & Integer.MIN_VALUE) != 0x0) {
            array[0] = true;
            setWord0 = setWord0(setWord0, word0(setWord0) & Integer.MAX_VALUE);
        }
        else {
            array[0] = false;
        }
        if ((word0(setWord0) & 0x7FF00000) == 0x7FF00000) {
            String s;
            if (word1(setWord0) == 0 && (word0(setWord0) & 0xFFFFF) == 0x0) {
                s = "Infinity";
            }
            else {
                s = "NaN";
            }
            sb.append(s);
            return 9999;
        }
        if (setWord0 == 0.0) {
            sb.setLength(0);
            sb.append('0');
            return 1;
        }
        BigInteger bigInteger = d2b(setWord0, array2, array3);
        n = (word0(setWord0) >>> 20 & 0x7FF);
        double n4;
        int n5;
        boolean b2;
        if (n != 0) {
            n4 = setWord0(setWord0, (word0(setWord0) & 0xFFFFF) | 0x3FF00000);
            n5 = n - 1023;
            b2 = false;
        }
        else {
            n = array3[0] + array2[0] + 1074;
            long n6;
            if (n > 32) {
                n6 = ((long)word0(setWord0) << 64 - n | (long)(word1(setWord0) >>> n - 32));
            }
            else {
                n6 = (long)word1(setWord0) << 32 - n;
            }
            n4 = setWord0((double)n6, word0((double)n6) - 32505856);
            n5 = n - 1075;
            b2 = true;
        }
        final double n7 = (n4 - 1.5) * 0.289529654602168 + 0.1760912590558 + n5 * 0.301029995663981;
        final int n8 = n = (int)n7;
        if (n7 < 0.0) {
            n = n8;
            if (n7 != n8) {
                n = n8 - 1;
            }
        }
        final boolean b3 = true;
        int n9 = n;
        boolean b4 = b3;
        if (n >= 0) {
            n9 = n;
            b4 = b3;
            if (n <= 22) {
                n9 = n;
                if (setWord0 < DToA.tens[n]) {
                    n9 = n - 1;
                }
                b4 = false;
            }
        }
        final int n10 = array3[0] - n5 - 1;
        int n11;
        int n12;
        if (n10 >= 0) {
            n11 = 0;
            n12 = n10;
        }
        else {
            n11 = -n10;
            n12 = 0;
        }
        int n13;
        if (n9 >= 0) {
            n = 0;
            n13 = n9;
            n12 += n9;
        }
        else {
            n11 -= n9;
            n = -n9;
            n13 = 0;
        }
        int n14;
        if (n3 < 0 || (n14 = n3) > 9) {
            n14 = 0;
        }
        boolean b5 = true;
        int n15;
        if ((n15 = n14) > 5) {
            n15 = n14 - 4;
            b5 = false;
        }
        int n16 = 1;
        boolean b6 = true;
        int n17 = 1;
        final boolean b7 = true;
        int n18 = 0;
        final boolean b8 = false;
        int n23 = 0;
        int n24 = 0;
        Label_0673: {
            int n19 = 0;
            int n25 = 0;
            Label_0552: {
                switch (n15) {
                    default: {
                        n19 = (b8 ? 1 : 0);
                        n16 = (b7 ? 1 : 0);
                        break;
                    }
                    case 3: {
                        b6 = false;
                    }
                    case 5: {
                        final int n21;
                        final int n20 = n21 = n2 + n9 + 1;
                        final int n22 = n20 - 1;
                        n16 = (b6 ? 1 : 0);
                        n5 = n20;
                        n18 = n22;
                        n19 = n21;
                        if (n20 <= 0) {
                            n5 = 1;
                            n16 = (b6 ? 1 : 0);
                            n18 = n22;
                            n19 = n21;
                            break;
                        }
                        break;
                    }
                    case 2: {
                        n17 = 0;
                    }
                    case 4: {
                        if (n2 <= 0) {
                            n2 = 1;
                        }
                        n5 = n2;
                        n23 = n2;
                        n24 = n2;
                        break Label_0673;
                    }
                    case 0:
                    case 1: {
                        n23 = -1;
                        n19 = -1;
                        n5 = 18;
                        n25 = 0;
                        break Label_0552;
                    }
                }
                n23 = n18;
                n25 = n2;
            }
            n2 = n19;
            n17 = n16;
            n24 = n25;
        }
        final boolean b9 = false;
        int n63;
        int n70;
        int n73;
        if (n2 >= 0 && n2 <= 14 && b5) {
            final int n26 = 0;
            int n27 = 0;
            final int n28 = 0;
            int n29 = 2;
            double n36;
            int n37;
            if (n9 > 0) {
                final double n30 = DToA.tens[n9 & 0xF];
                final int n31 = n9 >> 4;
                n27 = n28;
                int i = n31;
                double n32 = setWord0;
                double n33 = n30;
                if ((n31 & 0x10) != 0x0) {
                    i = (n31 & 0xF);
                    n32 = setWord0 / DToA.bigtens[4];
                    n29 = 2 + 1;
                    n33 = n30;
                    n27 = n28;
                }
                while (i != 0) {
                    double n34 = n33;
                    int n35 = n29;
                    if ((i & 0x1) != 0x0) {
                        n35 = n29 + 1;
                        n34 = n33 * DToA.bigtens[n27];
                    }
                    i >>= 1;
                    ++n27;
                    n33 = n34;
                    n29 = n35;
                }
                n36 = n32 / n33;
                n37 = n29;
            }
            else {
                final int n38 = -n9;
                n36 = setWord0;
                n37 = n29;
                if (n38 != 0) {
                    double n39 = setWord0 * DToA.tens[n38 & 0xF];
                    int n40 = n38 >> 4;
                    int n41 = n29;
                    int n42 = n26;
                    while (true) {
                        n27 = n42;
                        n36 = n39;
                        n37 = n41;
                        if (n40 == 0) {
                            break;
                        }
                        double n43 = n39;
                        int n44 = n41;
                        if ((n40 & 0x1) != 0x0) {
                            n44 = n41 + 1;
                            n43 = n39 * DToA.bigtens[n42];
                        }
                        n40 >>= 1;
                        ++n42;
                        n39 = n43;
                        n41 = n44;
                    }
                }
            }
            final double n45 = n36;
            int n46 = n2;
            int n47 = n9;
            double n48 = n45;
            boolean b10 = b9;
            int n49 = n37;
            if (b4) {
                n46 = n2;
                n47 = n9;
                n48 = n45;
                b10 = b9;
                n49 = n37;
                if (n45 < 1.0) {
                    n46 = n2;
                    n47 = n9;
                    n48 = n45;
                    b10 = b9;
                    n49 = n37;
                    if (n2 > 0) {
                        if (n23 <= 0) {
                            b10 = true;
                            n46 = n2;
                            n47 = n9;
                            n48 = n45;
                            n49 = n37;
                        }
                        else {
                            n46 = n23;
                            n47 = n9 - 1;
                            n48 = n45 * 10.0;
                            n49 = n37 + 1;
                            b10 = b9;
                        }
                    }
                }
            }
            int n50 = n47;
            final double n51 = n49 * n48 + 7.0;
            final double setWord2 = setWord0(n51, word0(n51) - 54525952);
            if (n46 == 0) {
                n48 -= 5.0;
                if (n48 > setWord2) {
                    sb.append('1');
                    return n50 + 1 + 1;
                }
                if (n48 < -setWord2) {
                    sb.setLength(0);
                    sb.append('0');
                    return 1;
                }
                b10 = true;
            }
            int n64;
            int n69;
            if (!b10) {
                int n52 = 1;
                int n57;
                int n59 = 0;
                if (n17 != 0) {
                    final double n53 = 0.5 / DToA.tens[n46 - 1] - setWord2;
                    int n54 = 0;
                    double n55 = n48;
                    double n56 = n53;
                    n57 = n13;
                    final int n58 = n15;
                    double n61 = 0.0;
                    Block_41: {
                        while (true) {
                            n59 = n52;
                            final long n60 = (long)n55;
                            n61 = n55 - n60;
                            sb.append((char)(n60 + 48L));
                            if (n61 < n56) {
                                return n50 + 1;
                            }
                            if (1.0 - n61 < n56) {
                                break;
                            }
                            n27 = n54 + 1;
                            if (n27 >= n46) {
                                break Block_41;
                            }
                            n56 *= 10.0;
                            n55 = n61 * 10.0;
                            n54 = n27;
                            n52 = n59;
                        }
                        while (true) {
                            do {
                                n = sb.charAt(sb.length() - 1);
                                sb.setLength(sb.length() - 1);
                                if (n != 57) {
                                    sb.append((char)(n + 1));
                                    return n50 + 1;
                                }
                            } while (sb.length() != 0);
                            ++n50;
                            n = 48;
                            continue;
                        }
                    }
                    final int n62 = n50;
                    n48 = n61;
                    n63 = n58;
                    n64 = n62;
                }
                else {
                    final int n65 = n15;
                    final boolean b11 = true;
                    n57 = n13;
                    final double n66 = setWord2 * DToA.tens[n46 - 1];
                    n27 = 1;
                    int n67 = n50;
                    while (true) {
                        final long n68 = (long)n48;
                        n48 -= n68;
                        sb.append((char)(n68 + 48L));
                        if (n27 == n46) {
                            break;
                        }
                        ++n27;
                        n48 *= 10.0;
                    }
                    if (n48 > 0.5 + n66) {
                        while (true) {
                            do {
                                n = sb.charAt(sb.length() - 1);
                                sb.setLength(sb.length() - 1);
                                if (n != 57) {
                                    sb.append((char)(n + 1));
                                    return n67 + 1;
                                }
                            } while (sb.length() != 0);
                            ++n67;
                            n = 48;
                            continue;
                        }
                    }
                    if (n48 < 0.5 - n66) {
                        stripTrailingZeroes(sb);
                        return n67 + 1;
                    }
                    n64 = n67;
                    n63 = n65;
                    n59 = (b11 ? 1 : 0);
                }
                n69 = n59;
                n70 = n57;
            }
            else {
                final int n71 = n13;
                n64 = n50;
                n63 = n15;
                n70 = n71;
                n69 = (b10 ? 1 : 0);
            }
            if (n69 != 0) {
                sb.setLength(0);
                final int n72 = n2;
                n2 = n9;
                n73 = n72;
            }
            else {
                n73 = n46;
                setWord0 = n48;
                n2 = n64;
            }
        }
        else {
            final int n74 = n13;
            final int n75 = n9;
            final int n76 = n5;
            n63 = n15;
            n70 = n74;
            final int n27 = n76;
            n73 = n2;
            n2 = n75;
        }
        final int n77 = n12;
        if (array2[0] >= 0 && n2 <= 14) {
            final double n78 = DToA.tens[n2];
            if (n24 >= 0 || n73 > 0) {
                n = 1;
            Label_1994:
                while (true) {
                    final long n79 = (long)(setWord0 / n78);
                    setWord0 -= n79 * n78;
                    sb.append((char)(n79 + 48L));
                    if (n == n73) {
                        setWord0 += setWord0;
                        if (setWord0 <= n78) {
                            n = n2;
                            if (setWord0 != n78) {
                                break;
                            }
                            if ((n79 & 0x1L) == 0x0L) {
                                n = n2;
                                if (!b) {
                                    break;
                                }
                            }
                        }
                        while (true) {
                            do {
                                n = sb.charAt(sb.length() - 1);
                                sb.setLength(sb.length() - 1);
                                if (n != 57) {
                                    sb.append((char)(n + 1));
                                    n = n2;
                                    break Label_1994;
                                }
                            } while (sb.length() != 0);
                            ++n2;
                            n = 48;
                            continue;
                        }
                    }
                    setWord0 *= 10.0;
                    if (setWord0 == 0.0) {
                        n = n2;
                        break;
                    }
                    ++n;
                }
                return n + 1;
            }
            if (n73 >= 0 && setWord0 >= n78 * 5.0 && (b || setWord0 != 5.0 * n78)) {
                sb.append('1');
                return n2 + 1 + 1;
            }
            sb.setLength(0);
            sb.append('0');
            return 1;
        }
        else {
            int n80 = n11;
            int n81 = n;
            final BigInteger bigInteger2 = null;
            BigInteger bigInteger3;
            int n88;
            int n89;
            int n90;
            int n91;
            if (n17 != 0) {
                int n27;
                if (n63 < 2) {
                    if (b2) {
                        n27 = array2[0] + 1075;
                    }
                    else {
                        n27 = 54 - array3[0];
                    }
                }
                else {
                    final int n82 = n73 - 1;
                    if (n81 >= n82) {
                        n81 -= n82;
                    }
                    else {
                        final int n83 = n82 - n81;
                        n += n83;
                        n81 = 0;
                        n70 += n83;
                    }
                    final int n84 = n73;
                    int n85 = n80;
                    n27 = n84;
                    if (n73 < 0) {
                        n85 = n80 - n84;
                        n27 = 0;
                    }
                    n80 = n85;
                }
                final int n86 = n11 + n27;
                final int n87 = n77 + n27;
                bigInteger3 = BigInteger.valueOf(1L);
                n88 = n70;
                n89 = n;
                n90 = n81;
                n91 = n86;
                n = n87;
            }
            else {
                n89 = n;
                n = n77;
                n88 = n70;
                n91 = n11;
                bigInteger3 = bigInteger2;
                n90 = n81;
            }
            final int n92 = n63;
            int n93 = n80;
            int n94 = n91;
            int n95 = n;
            if (n80 > 0) {
                n93 = n80;
                n94 = n91;
                if ((n95 = n) > 0) {
                    int n96;
                    if (n80 < n) {
                        n96 = n80;
                    }
                    else {
                        n96 = n;
                    }
                    n94 = n91 - n96;
                    n93 = n80 - n96;
                    n95 = n - n96;
                }
            }
            if (n89 > 0) {
                if (n17 != 0) {
                    if (n90 > 0) {
                        bigInteger3 = pow5mult(bigInteger3, n90);
                        bigInteger = bigInteger3.multiply(bigInteger);
                    }
                    n = n89 - n90;
                    if (n != 0) {
                        bigInteger = pow5mult(bigInteger, n);
                    }
                }
                else {
                    bigInteger = pow5mult(bigInteger, n89);
                }
            }
            BigInteger bigInteger5;
            final BigInteger bigInteger4 = bigInteger5 = BigInteger.valueOf(1L);
            if (n88 > 0) {
                bigInteger5 = pow5mult(bigInteger4, n88);
            }
            boolean b13;
            final boolean b12 = b13 = false;
            int n97 = n94;
            n = n95;
            if (n92 < 2) {
                b13 = b12;
                n97 = n94;
                n = n95;
                if (word1(setWord0) == 0) {
                    b13 = b12;
                    n97 = n94;
                    n = n95;
                    if ((word0(setWord0) & 0xFFFFF) == 0x0) {
                        b13 = b12;
                        n97 = n94;
                        n = n95;
                        if ((word0(setWord0) & 0x7FE00000) != 0x0) {
                            n97 = n94 + 1;
                            n = n95 + 1;
                            b13 = true;
                        }
                    }
                }
            }
            final byte[] byteArray = bigInteger5.toByteArray();
            int n98 = 0;
            int n99;
            for (int j = 0; j < 4; ++j, n98 = n99) {
                n99 = n98 << 8;
                if (j < byteArray.length) {
                    n99 |= (byteArray[j] & 0xFF);
                }
            }
            int n100;
            if (n88 != 0) {
                n100 = 32 - hi0bits(n98);
            }
            else {
                n100 = 1;
            }
            int n101;
            if ((n101 = (n100 + n & 0x1F)) != 0) {
                n101 = 32 - n101;
            }
            final int n102 = n101;
            int n104;
            int n105;
            int n106;
            if (n102 > 4) {
                final int n103 = n102 - 4;
                n104 = n97 + n103;
                n105 = n93 + n103;
                n106 = n + n103;
            }
            else {
                n105 = n93;
                n104 = n97;
                n106 = n;
                if (n102 < 4) {
                    final int n107 = n102 + 28;
                    n104 = n97 + n107;
                    n105 = n93 + n107;
                    n106 = n + n107;
                }
            }
            BigInteger shiftLeft = bigInteger;
            if (n104 > 0) {
                shiftLeft = bigInteger.shiftLeft(n104);
            }
            BigInteger shiftLeft2 = bigInteger5;
            if (n106 > 0) {
                shiftLeft2 = bigInteger5.shiftLeft(n106);
            }
            BigInteger bigInteger6;
            BigInteger multiply;
            if (b4 && shiftLeft.compareTo(shiftLeft2) < 0) {
                n = n2 - 1;
                bigInteger6 = shiftLeft.multiply(BigInteger.valueOf(10L));
                multiply = bigInteger3;
                if (n17 != 0) {
                    multiply = bigInteger3.multiply(BigInteger.valueOf(10L));
                }
                n2 = n23;
            }
            else {
                n = n2;
                n2 = n73;
                multiply = bigInteger3;
                bigInteger6 = shiftLeft;
            }
            if (n2 <= 0 && n92 > 2) {
                if (n2 >= 0) {
                    n2 = bigInteger6.compareTo(shiftLeft2.multiply(BigInteger.valueOf(5L)));
                    if (n2 >= 0) {
                        if (n2 != 0 || b) {
                            sb.append('1');
                            return n + 1 + 1;
                        }
                    }
                }
                sb.setLength(0);
                sb.append('0');
                return 1;
            }
            BigInteger bigInteger9;
            if (n17 != 0) {
                BigInteger bigInteger7 = multiply;
                if (n105 > 0) {
                    bigInteger7 = multiply.shiftLeft(n105);
                }
                BigInteger bigInteger8 = bigInteger7;
                if (b13) {
                    bigInteger7 = bigInteger8.shiftLeft(1);
                }
                int n108 = 1;
                BigInteger multiply2 = bigInteger6;
                while (true) {
                    final BigInteger[] divideAndRemainder = multiply2.divideAndRemainder(shiftLeft2);
                    bigInteger9 = divideAndRemainder[1];
                    final char c = (char)(divideAndRemainder[0].intValue() + 48);
                    final int compareTo = bigInteger9.compareTo(bigInteger8);
                    final BigInteger subtract = shiftLeft2.subtract(bigInteger7);
                    int compareTo2;
                    if (subtract.signum() <= 0) {
                        compareTo2 = 1;
                    }
                    else {
                        compareTo2 = bigInteger9.compareTo(subtract);
                    }
                    if (compareTo2 == 0 && n92 == 0 && (word1(setWord0) & 0x1) == 0x0) {
                        if (c == '9') {
                            sb.append('9');
                            n2 = n;
                            if (roundOff(sb)) {
                                n2 = n + 1;
                                sb.append('1');
                            }
                            return n2 + 1;
                        }
                        char c2 = c;
                        if (compareTo > 0) {
                            c2 = (char)(c + '\u0001');
                        }
                        sb.append(c2);
                        return n + 1;
                    }
                    else {
                        if (compareTo < 0 || (compareTo == 0 && n92 == 0 && (word1(setWord0) & 0x1) == 0x0)) {
                            char c3 = c;
                            Label_3381: {
                                if (compareTo2 > 0) {
                                    n2 = bigInteger9.shiftLeft(1).compareTo(shiftLeft2);
                                    if (n2 <= 0) {
                                        c3 = c;
                                        if (n2 != 0) {
                                            break Label_3381;
                                        }
                                        if ((c & '\u0001') != 0x1) {
                                            c3 = c;
                                            if (!b) {
                                                break Label_3381;
                                            }
                                        }
                                    }
                                    c3 = (char)(c + '\u0001');
                                    if (c == '9') {
                                        sb.append('9');
                                        n2 = n;
                                        if (roundOff(sb)) {
                                            n2 = n + 1;
                                            sb.append('1');
                                        }
                                        return n2 + 1;
                                    }
                                }
                            }
                            sb.append(c3);
                            return n + 1;
                        }
                        if (compareTo2 > 0) {
                            if (c == '9') {
                                sb.append('9');
                                n2 = n;
                                if (roundOff(sb)) {
                                    n2 = n + 1;
                                    sb.append('1');
                                }
                                return n2 + 1;
                            }
                            sb.append((char)(c + '\u0001'));
                            return n + 1;
                        }
                        else {
                            sb.append(c);
                            if (n108 == n2) {
                                n2 = c;
                                break;
                            }
                            multiply2 = bigInteger9.multiply(BigInteger.valueOf(10L));
                            BigInteger bigInteger11;
                            BigInteger bigInteger10;
                            if (bigInteger8 == bigInteger7) {
                                bigInteger10 = (bigInteger11 = bigInteger7.multiply(BigInteger.valueOf(10L)));
                            }
                            else {
                                final BigInteger multiply3 = bigInteger8.multiply(BigInteger.valueOf(10L));
                                bigInteger11 = bigInteger7.multiply(BigInteger.valueOf(10L));
                                bigInteger10 = multiply3;
                            }
                            ++n108;
                            bigInteger8 = bigInteger10;
                            bigInteger7 = bigInteger11;
                        }
                    }
                }
            }
            else {
                int n109 = 1;
                char c4;
                while (true) {
                    final BigInteger[] divideAndRemainder2 = bigInteger6.divideAndRemainder(shiftLeft2);
                    bigInteger9 = divideAndRemainder2[1];
                    c4 = (char)(divideAndRemainder2[0].intValue() + 48);
                    sb.append(c4);
                    if (n109 >= n2) {
                        break;
                    }
                    bigInteger6 = bigInteger9.multiply(BigInteger.valueOf(10L));
                    ++n109;
                }
                n2 = c4;
            }
            final int compareTo3 = bigInteger9.shiftLeft(1).compareTo(shiftLeft2);
            Label_3489: {
                if (compareTo3 <= 0) {
                    if (compareTo3 == 0) {
                        if ((n2 & 0x1) == 0x1) {
                            break Label_3489;
                        }
                        if (b) {
                            break Label_3489;
                        }
                    }
                    stripTrailingZeroes(sb);
                    return n + 1;
                }
            }
            if (roundOff(sb)) {
                sb.append('1');
                return n + 1 + 1;
            }
            return n + 1;
        }
    }
    
    static String JS_dtobasestr(int n, double floor) {
        double n2 = floor;
        if (2 > n || n > 36) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Bad base: ");
            sb.append(n);
            throw new IllegalArgumentException(sb.toString());
        }
        if (Double.isNaN(floor)) {
            return "NaN";
        }
        if (Double.isInfinite(floor)) {
            if (n2 > 0.0) {
                return "Infinity";
            }
            return "-Infinity";
        }
        else {
            if (n2 == 0.0) {
                return "0";
            }
            boolean b;
            if (n2 >= 0.0) {
                b = false;
            }
            else {
                b = true;
                n2 = -n2;
            }
            floor = Math.floor(n2);
            long n3 = (long)floor;
            String s;
            if (n3 == floor) {
                if (b) {
                    n3 = -n3;
                }
                s = Long.toString(n3, n);
            }
            else {
                final long doubleToLongBits = Double.doubleToLongBits(floor);
                final int n4 = (int)(doubleToLongBits >> 52) & 0x7FF;
                long n5;
                if (n4 == 0) {
                    n5 = (doubleToLongBits & 0xFFFFFFFFFFFFFL) << 1;
                }
                else {
                    n5 = ((doubleToLongBits & 0xFFFFFFFFFFFFFL) | 0x10000000000000L);
                }
                long n6 = n5;
                if (b) {
                    n6 = -n5;
                }
                final int n7 = n4 - 1075;
                final BigInteger value = BigInteger.valueOf(n6);
                BigInteger bigInteger;
                if (n7 > 0) {
                    bigInteger = value.shiftLeft(n7);
                }
                else {
                    bigInteger = value;
                    if (n7 < 0) {
                        bigInteger = value.shiftRight(-n7);
                    }
                }
                s = bigInteger.toString(n);
            }
            if (n2 == floor) {
                return s;
            }
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(s);
            sb2.append('.');
            final long doubleToLongBits2 = Double.doubleToLongBits(n2);
            final int n8 = (int)(doubleToLongBits2 >> 32);
            final int n9 = (int)doubleToLongBits2;
            final int[] array = { 0 };
            final BigInteger d2b = d2b(n2 - floor, array, new int[1]);
            int n10;
            if ((n10 = -(n8 >>> 20 & 0x7FF)) == 0) {
                n10 = -1;
            }
            final int n11 = n10 + 1076;
            final BigInteger value2;
            final BigInteger bigInteger2 = value2 = BigInteger.valueOf(1L);
            int n12 = n11;
            BigInteger value3 = value2;
            if (n9 == 0) {
                n12 = n11;
                value3 = value2;
                if ((n8 & 0xFFFFF) == 0x0) {
                    n12 = n11;
                    value3 = value2;
                    if ((n8 & 0x7FE00000) != 0x0) {
                        n12 = n11 + 1;
                        value3 = BigInteger.valueOf(2L);
                    }
                }
            }
            final BigInteger shiftLeft = d2b.shiftLeft(array[0] + n12);
            final BigInteger shiftLeft2 = BigInteger.valueOf(1L).shiftLeft(n12);
            final BigInteger value4 = BigInteger.valueOf(n);
            final BigInteger bigInteger3 = value3;
            int n13 = 0;
            BigInteger multiply = bigInteger2;
            BigInteger bigInteger4 = bigInteger3;
            BigInteger shiftLeft3 = shiftLeft;
            final BigInteger bigInteger5 = value4;
            do {
                final BigInteger[] divideAndRemainder = shiftLeft3.multiply(bigInteger5).divideAndRemainder(shiftLeft2);
                shiftLeft3 = divideAndRemainder[1];
                n = (char)divideAndRemainder[0].intValue();
                if (multiply == bigInteger4) {
                    multiply = (bigInteger4 = multiply.multiply(bigInteger5));
                }
                else {
                    multiply = multiply.multiply(bigInteger5);
                    bigInteger4 = bigInteger4.multiply(bigInteger5);
                }
                final int compareTo = shiftLeft3.compareTo(multiply);
                final BigInteger subtract = shiftLeft2.subtract(bigInteger4);
                int compareTo2;
                if (subtract.signum() <= 0) {
                    compareTo2 = 1;
                }
                else {
                    compareTo2 = shiftLeft3.compareTo(subtract);
                }
                int n16 = 0;
                Label_0702: {
                    int n14 = 0;
                    Label_0603: {
                        if (compareTo2 == 0 && (n9 & 0x1) == 0x0) {
                            n14 = n;
                            if (compareTo > 0) {
                                n14 = n + 1;
                            }
                        }
                        else {
                            if (compareTo < 0 || (compareTo == 0 && (n9 & 0x1) == 0x0)) {
                                if (compareTo2 > 0) {
                                    shiftLeft3 = shiftLeft3.shiftLeft(1);
                                    int n15 = n;
                                    if (shiftLeft3.compareTo(shiftLeft2) > 0) {
                                        n15 = n + 1;
                                    }
                                    n = n15;
                                }
                                n13 = 1;
                                n16 = n;
                                break Label_0702;
                            }
                            if (compareTo2 <= 0) {
                                final int n17 = n13;
                                n14 = n;
                                n = n17;
                                break Label_0603;
                            }
                            n14 = n + 1;
                        }
                        n = 1;
                    }
                    n16 = n14;
                    n13 = n;
                }
                sb2.append(BASEDIGIT(n16));
            } while (n13 == 0);
            return sb2.toString();
        }
    }
    
    static void JS_dtostr(final StringBuilder sb, int i, int n, final double n2) {
        final boolean[] array = { false };
        int n3 = i;
        Label_0035: {
            if (i == 2) {
                if (n2 < 1.0E21) {
                    n3 = i;
                    if (n2 > -1.0E21) {
                        break Label_0035;
                    }
                }
                n3 = 0;
            }
        }
        i = DToA.dtoaModes[n3];
        final int js_dtoa = JS_dtoa(n2, i, n3 >= 2, n, array, sb);
        final int length = sb.length();
        if (js_dtoa != 9999) {
            final boolean b = false;
            final int n4 = 0;
            i = 0;
            boolean b2 = false;
            switch (n3) {
                default: {
                    b2 = b;
                    i = n4;
                    break;
                }
                case 4: {
                    final int n5 = n;
                    if (js_dtoa >= -5) {
                        b2 = b;
                        i = n5;
                        if (js_dtoa <= n) {
                            break;
                        }
                    }
                    b2 = true;
                    i = n5;
                    break;
                }
                case 2: {
                    if (n >= 0) {
                        i = js_dtoa + n;
                        b2 = b;
                        break;
                    }
                    i = js_dtoa;
                    b2 = b;
                    break;
                }
                case 3: {
                    i = n;
                }
                case 1: {
                    b2 = true;
                    break;
                }
                case 0: {
                    if (js_dtoa >= -5 && js_dtoa <= 21) {
                        i = js_dtoa;
                        b2 = b;
                        break;
                    }
                    b2 = true;
                    i = n4;
                    break;
                }
            }
            if ((n = length) < i) {
                do {
                    sb.append('0');
                } while (sb.length() != i);
                n = i;
            }
            if (b2) {
                if (n != 1) {
                    sb.insert(1, '.');
                }
                sb.append('e');
                if (js_dtoa - 1 >= 0) {
                    sb.append('+');
                }
                sb.append(js_dtoa - 1);
            }
            else if (js_dtoa != n) {
                if (js_dtoa > 0) {
                    sb.insert(js_dtoa, '.');
                }
                else {
                    for (i = 0; i < 1 - js_dtoa; ++i) {
                        sb.insert(0, '0');
                    }
                    sb.insert(1, '.');
                }
            }
        }
        if (array[0] && (word0(n2) != Integer.MIN_VALUE || word1(n2) != 0) && ((word0(n2) & 0x7FF00000) != 0x7FF00000 || (word1(n2) == 0 && (word0(n2) & 0xFFFFF) == 0x0))) {
            sb.insert(0, '-');
        }
    }
    
    private static BigInteger d2b(final double n, final int[] array, final int[] array2) {
        final long doubleToLongBits = Double.doubleToLongBits(n);
        final int n2 = (int)(doubleToLongBits >>> 32);
        final int n3 = (int)doubleToLongBits;
        final int n4 = 0xFFFFF & n2;
        final int n5 = (n2 & Integer.MAX_VALUE) >>> 20;
        int n6 = n4;
        if (n5 != 0) {
            n6 = (n4 | 0x100000);
        }
        byte[] array3;
        int lo0bits;
        int n8;
        if (n3 != 0) {
            array3 = new byte[8];
            lo0bits = lo0bits(n3);
            final int n7 = n3 >>> lo0bits;
            if (lo0bits != 0) {
                stuffBits(array3, 4, n6 << 32 - lo0bits | n7);
                n6 >>= lo0bits;
            }
            else {
                stuffBits(array3, 4, n7);
            }
            stuffBits(array3, 0, n6);
            if (n6 != 0) {
                n8 = 2;
            }
            else {
                n8 = 1;
            }
        }
        else {
            array3 = new byte[4];
            final int lo0bits2 = lo0bits(n6);
            n6 >>>= lo0bits2;
            stuffBits(array3, 0, n6);
            lo0bits = lo0bits2 + 32;
            n8 = 1;
        }
        if (n5 != 0) {
            array[0] = n5 - 1023 - 52 + lo0bits;
            array2[0] = 53 - lo0bits;
        }
        else {
            array[0] = n5 - 1023 - 52 + 1 + lo0bits;
            array2[0] = n8 * 32 - hi0bits(n6);
        }
        return new BigInteger(array3);
    }
    
    private static int hi0bits(int n) {
        int n2 = 0;
        int n3 = n;
        if ((0xFFFF0000 & n) == 0x0) {
            n2 = 16;
            n3 = n << 16;
        }
        int n4 = n2;
        n = n3;
        if ((0xFF000000 & n3) == 0x0) {
            n4 = n2 + 8;
            n = n3 << 8;
        }
        int n5 = n4;
        int n6 = n;
        if ((0xF0000000 & n) == 0x0) {
            n5 = n4 + 4;
            n6 = n << 4;
        }
        n = n5;
        int n7 = n6;
        if ((0xC0000000 & n6) == 0x0) {
            n = n5 + 2;
            n7 = n6 << 2;
        }
        int n8 = n;
        if ((Integer.MIN_VALUE & n7) == 0x0) {
            n8 = n + 1;
            if ((0x40000000 & n7) == 0x0) {
                return 32;
            }
        }
        return n8;
    }
    
    private static int lo0bits(int n) {
        final int n2 = n;
        if ((n2 & 0x7) == 0x0) {
            int n3 = 0;
            n = n2;
            if ((0xFFFF & n2) == 0x0) {
                n3 = 16;
                n = n2 >>> 16;
            }
            int n4 = n;
            int n5 = n3;
            if ((n & 0xFF) == 0x0) {
                n5 = n3 + 8;
                n4 = n >>> 8;
            }
            n = n4;
            int n6 = n5;
            if ((n4 & 0xF) == 0x0) {
                n6 = n5 + 4;
                n = n4 >>> 4;
            }
            int n7 = n;
            int n8 = n6;
            if ((n & 0x3) == 0x0) {
                n8 = n6 + 2;
                n7 = n >>> 2;
            }
            n = n8;
            if ((n7 & 0x1) == 0x0) {
                n = n8 + 1;
                if ((n7 >>> 1 & 0x1) == 0x0) {
                    return 32;
                }
            }
            return n;
        }
        if ((n2 & 0x1) != 0x0) {
            return 0;
        }
        if ((n2 & 0x2) != 0x0) {
            return 1;
        }
        return 2;
    }
    
    static BigInteger pow5mult(final BigInteger bigInteger, final int n) {
        return bigInteger.multiply(BigInteger.valueOf(5L).pow(n));
    }
    
    static boolean roundOff(final StringBuilder sb) {
        int i = sb.length();
        while (i != 0) {
            --i;
            final char char1 = sb.charAt(i);
            if (char1 != '9') {
                sb.setCharAt(i, (char)(char1 + '\u0001'));
                sb.setLength(i + 1);
                return false;
            }
        }
        sb.setLength(0);
        return true;
    }
    
    static double setWord0(final double n, final int n2) {
        return Double.longBitsToDouble((long)n2 << 32 | (Double.doubleToLongBits(n) & 0xFFFFFFFFL));
    }
    
    private static void stripTrailingZeroes(final StringBuilder sb) {
        int length = sb.length();
        int n;
        while (true) {
            n = length - 1;
            if (length <= 0 || sb.charAt(n) != '0') {
                break;
            }
            length = n;
        }
        sb.setLength(n + 1);
    }
    
    private static void stuffBits(final byte[] array, final int n, final int n2) {
        array[n] = (byte)(n2 >> 24);
        array[n + 1] = (byte)(n2 >> 16);
        array[n + 2] = (byte)(n2 >> 8);
        array[n + 3] = (byte)n2;
    }
    
    static int word0(final double n) {
        return (int)(Double.doubleToLongBits(n) >> 32);
    }
    
    static int word1(final double n) {
        return (int)Double.doubleToLongBits(n);
    }
}
