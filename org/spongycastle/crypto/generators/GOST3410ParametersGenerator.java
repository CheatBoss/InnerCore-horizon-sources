package org.spongycastle.crypto.generators;

import java.math.*;
import java.security.*;
import java.util.*;
import org.spongycastle.crypto.params.*;

public class GOST3410ParametersGenerator
{
    private static final BigInteger ONE;
    private static final BigInteger TWO;
    private SecureRandom init_random;
    private int size;
    private int typeproc;
    
    static {
        ONE = BigInteger.valueOf(1L);
        TWO = BigInteger.valueOf(2L);
    }
    
    private int procedure_A(int n, int n2, final BigInteger[] array, int i) {
        while (n < 0 || n > 65536) {
            n = this.init_random.nextInt() / 32768;
        }
        while (n2 < 0 || n2 > 65536 || n2 / 2 == 0) {
            n2 = this.init_random.nextInt() / 32768 + 1;
        }
        final BigInteger bigInteger = new BigInteger(Integer.toString(n2));
        final BigInteger bigInteger2 = new BigInteger("19381");
        final BigInteger bigInteger3 = new BigInteger(Integer.toString(n));
        int[] array2;
        int[] array3;
        for (array2 = new int[] { i }, n = 0, n2 = 0; array2[n] >= 17; n = n2) {
            n2 = array2.length + 1;
            array3 = new int[n2];
            System.arraycopy(array2, 0, array3, 0, array2.length);
            array2 = new int[n2];
            System.arraycopy(array3, 0, array2, 0, n2);
            n2 = n + 1;
            array2[n2] = array2[n] / 2;
        }
        final BigInteger[] array4 = new BigInteger[n2 + 1];
        array4[n2] = new BigInteger("8003", 16);
        int n3 = n2 - 1;
        BigInteger[] array5 = { bigInteger3 };
        int length;
        BigInteger[] array6;
        int j;
        int n4;
        BigInteger add;
        int k;
        BigInteger pow;
        int n5;
        BigInteger bigInteger5;
        BigInteger bigInteger4;
        int n6;
        BigInteger bigInteger6;
        long n7;
        BigInteger bigInteger7;
        for (i = 0; i < n2; ++i) {
            n = array2[n3] / 16;
        Block_12:
            while (true) {
                length = array5.length;
                array6 = new BigInteger[length];
                System.arraycopy(array5, 0, array6, 0, array5.length);
                array5 = new BigInteger[n + 1];
                System.arraycopy(array6, 0, array5, 0, length);
                for (j = 0; j < n; j = n4) {
                    n4 = j + 1;
                    array5[n4] = array5[j].multiply(bigInteger2).add(bigInteger).mod(GOST3410ParametersGenerator.TWO.pow(16));
                }
                add = new BigInteger("0");
                for (k = 0; k < n; ++k) {
                    add = add.add(array5[k].multiply(GOST3410ParametersGenerator.TWO.pow(k * 16)));
                }
                array5[0] = array5[n];
                pow = GOST3410ParametersGenerator.TWO.pow(array2[n3] - 1);
                n5 = n3 + 1;
                bigInteger4 = (bigInteger5 = pow.divide(array4[n5]).add(GOST3410ParametersGenerator.TWO.pow(array2[n3] - 1).multiply(add).divide(array4[n5].multiply(GOST3410ParametersGenerator.TWO.pow(n * 16)))));
                if (bigInteger4.mod(GOST3410ParametersGenerator.TWO).compareTo(GOST3410ParametersGenerator.ONE) == 0) {
                    bigInteger5 = bigInteger4.add(GOST3410ParametersGenerator.ONE);
                }
                n6 = 0;
                while (true) {
                    bigInteger6 = array4[n5];
                    n7 = n6;
                    array4[n3] = bigInteger6.multiply(bigInteger5.add(BigInteger.valueOf(n7))).add(GOST3410ParametersGenerator.ONE);
                    if (array4[n3].compareTo(GOST3410ParametersGenerator.TWO.pow(array2[n3])) == 1) {
                        break;
                    }
                    if (GOST3410ParametersGenerator.TWO.modPow(array4[n5].multiply(bigInteger5.add(BigInteger.valueOf(n7))), array4[n3]).compareTo(GOST3410ParametersGenerator.ONE) == 0 && GOST3410ParametersGenerator.TWO.modPow(bigInteger5.add(BigInteger.valueOf(n7)), array4[n3]).compareTo(GOST3410ParametersGenerator.ONE) != 0) {
                        break Block_12;
                    }
                    n6 += 2;
                }
            }
            --n3;
            if (n3 < 0) {
                array[0] = array4[0];
                array[1] = array4[1];
                bigInteger7 = array5[0];
                return bigInteger7.intValue();
            }
        }
        bigInteger7 = array5[0];
        return bigInteger7.intValue();
    }
    
    private long procedure_Aa(long n, long n2, final BigInteger[] array, int n3) {
        while (n < 0L || n > 4294967296L) {
            n = this.init_random.nextInt() * 2;
        }
        while (n2 < 0L || n2 > 4294967296L || n2 / 2L == 0L) {
            n2 = this.init_random.nextInt() * 2 + 1;
        }
        final BigInteger bigInteger = new BigInteger(Long.toString(n2));
        final BigInteger bigInteger2 = new BigInteger("97781173");
        final BigInteger bigInteger3 = new BigInteger(Long.toString(n));
        int[] array2;
        int n4;
        int[] array3;
        for (array2 = new int[] { n3 }, n4 = 0, n3 = 0; array2[n4] >= 33; n4 = n3) {
            n3 = array2.length + 1;
            array3 = new int[n3];
            System.arraycopy(array2, 0, array3, 0, array2.length);
            array2 = new int[n3];
            System.arraycopy(array3, 0, array2, 0, n3);
            n3 = n4 + 1;
            array2[n3] = array2[n4] / 2;
        }
        final BigInteger[] array4 = new BigInteger[n3 + 1];
        array4[n3] = new BigInteger("8000000B", 16);
        int n5 = n3 - 1;
        BigInteger[] array5 = { bigInteger3 };
        for (int i = 0; i < n3; ++i) {
            final int n6 = array2[n5] / 32;
        Block_12:
            while (true) {
                final int length = array5.length;
                final BigInteger[] array6 = new BigInteger[length];
                System.arraycopy(array5, 0, array6, 0, array5.length);
                array5 = new BigInteger[n6 + 1];
                System.arraycopy(array6, 0, array5, 0, length);
                int n7;
                for (int j = 0; j < n6; j = n7) {
                    n7 = j + 1;
                    array5[n7] = array5[j].multiply(bigInteger2).add(bigInteger).mod(GOST3410ParametersGenerator.TWO.pow(32));
                }
                BigInteger add = new BigInteger("0");
                for (int k = 0; k < n6; ++k) {
                    add = add.add(array5[k].multiply(GOST3410ParametersGenerator.TWO.pow(k * 32)));
                }
                array5[0] = array5[n6];
                final BigInteger pow = GOST3410ParametersGenerator.TWO.pow(array2[n5] - 1);
                final int n8 = n5 + 1;
                BigInteger bigInteger5;
                final BigInteger bigInteger4 = bigInteger5 = pow.divide(array4[n8]).add(GOST3410ParametersGenerator.TWO.pow(array2[n5] - 1).multiply(add).divide(array4[n8].multiply(GOST3410ParametersGenerator.TWO.pow(n6 * 32))));
                if (bigInteger4.mod(GOST3410ParametersGenerator.TWO).compareTo(GOST3410ParametersGenerator.ONE) == 0) {
                    bigInteger5 = bigInteger4.add(GOST3410ParametersGenerator.ONE);
                }
                int n9 = 0;
                while (true) {
                    final BigInteger bigInteger6 = array4[n8];
                    n = n9;
                    array4[n5] = bigInteger6.multiply(bigInteger5.add(BigInteger.valueOf(n))).add(GOST3410ParametersGenerator.ONE);
                    if (array4[n5].compareTo(GOST3410ParametersGenerator.TWO.pow(array2[n5])) == 1) {
                        break;
                    }
                    if (GOST3410ParametersGenerator.TWO.modPow(array4[n8].multiply(bigInteger5.add(BigInteger.valueOf(n))), array4[n5]).compareTo(GOST3410ParametersGenerator.ONE) == 0 && GOST3410ParametersGenerator.TWO.modPow(bigInteger5.add(BigInteger.valueOf(n)), array4[n5]).compareTo(GOST3410ParametersGenerator.ONE) != 0) {
                        break Block_12;
                    }
                    n9 += 2;
                }
            }
            --n5;
            if (n5 < 0) {
                array[0] = array4[0];
                array[1] = array4[1];
                final BigInteger bigInteger7 = array5[0];
                return bigInteger7.longValue();
            }
        }
        final BigInteger bigInteger7 = array5[0];
        return bigInteger7.longValue();
    }
    
    private void procedure_B(int n, int i, final BigInteger[] array) {
        while (n < 0 || n > 65536) {
            n = this.init_random.nextInt() / 32768;
        }
        int n2;
        while (true) {
            n2 = 1;
            if (i >= 0 && i <= 65536 && i / 2 != 0) {
                break;
            }
            i = this.init_random.nextInt() / 32768 + 1;
        }
        final BigInteger[] array2 = new BigInteger[2];
        final BigInteger bigInteger = new BigInteger(Integer.toString(i));
        final BigInteger bigInteger2 = new BigInteger("19381");
        n = this.procedure_A(n, i, array2, 256);
        final BigInteger bigInteger3 = array2[0];
        n = this.procedure_A(n, i, array2, 512);
        final BigInteger bigInteger4 = array2[0];
        final BigInteger[] array3 = new BigInteger[65];
        array3[0] = new BigInteger(Integer.toString(n));
        n = n2;
        BigInteger add2 = null;
    Block_10:
        while (true) {
            int n3;
            for (i = 0; i < 64; i = n3) {
                n3 = i + 1;
                array3[n3] = array3[i].multiply(bigInteger2).add(bigInteger).mod(GOST3410ParametersGenerator.TWO.pow(16));
            }
            BigInteger add = new BigInteger("0");
            for (i = 0; i < 64; ++i) {
                add = add.add(array3[i].multiply(GOST3410ParametersGenerator.TWO.pow(i * 16)));
            }
            array3[0] = array3[64];
            BigInteger bigInteger6;
            final BigInteger bigInteger5 = bigInteger6 = GOST3410ParametersGenerator.TWO.pow(1023).divide(bigInteger3.multiply(bigInteger4)).add(GOST3410ParametersGenerator.TWO.pow(1023).multiply(add).divide(bigInteger3.multiply(bigInteger4).multiply(GOST3410ParametersGenerator.TWO.pow(1024))));
            if (bigInteger5.mod(GOST3410ParametersGenerator.TWO).compareTo(GOST3410ParametersGenerator.ONE) == 0) {
                bigInteger6 = bigInteger5.add(GOST3410ParametersGenerator.ONE);
            }
            final int n4 = 0;
            i = n;
            n = n4;
            while (true) {
                final BigInteger multiply = bigInteger3.multiply(bigInteger4);
                final long n5 = n;
                add2 = multiply.multiply(bigInteger6.add(BigInteger.valueOf(n5))).add(GOST3410ParametersGenerator.ONE);
                if (add2.compareTo(GOST3410ParametersGenerator.TWO.pow(1024)) == i) {
                    n = i;
                    break;
                }
                if (GOST3410ParametersGenerator.TWO.modPow(bigInteger3.multiply(bigInteger4).multiply(bigInteger6.add(BigInteger.valueOf(n5))), add2).compareTo(GOST3410ParametersGenerator.ONE) == 0 && GOST3410ParametersGenerator.TWO.modPow(bigInteger3.multiply(bigInteger6.add(BigInteger.valueOf(n5))), add2).compareTo(GOST3410ParametersGenerator.ONE) != 0) {
                    break Block_10;
                }
                n += 2;
                i = 1;
            }
        }
        array[0] = add2;
        array[1] = bigInteger3;
    }
    
    private void procedure_Bb(long n, long n2, final BigInteger[] array) {
        while (n < 0L || n > 4294967296L) {
            n = this.init_random.nextInt() * 2;
        }
        while (n2 < 0L || n2 > 4294967296L || n2 / 2L == 0L) {
            n2 = this.init_random.nextInt() * 2 + 1;
        }
        final BigInteger[] array2 = new BigInteger[2];
        final BigInteger bigInteger = new BigInteger(Long.toString(n2));
        final BigInteger bigInteger2 = new BigInteger("97781173");
        n = this.procedure_Aa(n, n2, array2, 256);
        final BigInteger bigInteger3 = array2[0];
        n = this.procedure_Aa(n, n2, array2, 512);
        final BigInteger bigInteger4 = array2[0];
        final BigInteger[] array3 = new BigInteger[33];
        array3[0] = new BigInteger(Long.toString(n));
        BigInteger add2 = null;
    Block_10:
        while (true) {
            int n3;
            for (int i = 0; i < 32; i = n3) {
                n3 = i + 1;
                array3[n3] = array3[i].multiply(bigInteger2).add(bigInteger).mod(GOST3410ParametersGenerator.TWO.pow(32));
            }
            BigInteger add = new BigInteger("0");
            for (int j = 0; j < 32; ++j) {
                add = add.add(array3[j].multiply(GOST3410ParametersGenerator.TWO.pow(j * 32)));
            }
            array3[0] = array3[32];
            BigInteger bigInteger6;
            final BigInteger bigInteger5 = bigInteger6 = GOST3410ParametersGenerator.TWO.pow(1023).divide(bigInteger3.multiply(bigInteger4)).add(GOST3410ParametersGenerator.TWO.pow(1023).multiply(add).divide(bigInteger3.multiply(bigInteger4).multiply(GOST3410ParametersGenerator.TWO.pow(1024))));
            if (bigInteger5.mod(GOST3410ParametersGenerator.TWO).compareTo(GOST3410ParametersGenerator.ONE) == 0) {
                bigInteger6 = bigInteger5.add(GOST3410ParametersGenerator.ONE);
            }
            int n4 = 0;
            while (true) {
                final BigInteger multiply = bigInteger3.multiply(bigInteger4);
                n = n4;
                add2 = multiply.multiply(bigInteger6.add(BigInteger.valueOf(n))).add(GOST3410ParametersGenerator.ONE);
                if (add2.compareTo(GOST3410ParametersGenerator.TWO.pow(1024)) == 1) {
                    break;
                }
                if (GOST3410ParametersGenerator.TWO.modPow(bigInteger3.multiply(bigInteger4).multiply(bigInteger6.add(BigInteger.valueOf(n))), add2).compareTo(GOST3410ParametersGenerator.ONE) == 0 && GOST3410ParametersGenerator.TWO.modPow(bigInteger3.multiply(bigInteger6.add(BigInteger.valueOf(n))), add2).compareTo(GOST3410ParametersGenerator.ONE) != 0) {
                    break Block_10;
                }
                n4 += 2;
            }
        }
        array[0] = add2;
        array[1] = bigInteger3;
    }
    
    private BigInteger procedure_C(final BigInteger bigInteger, BigInteger divide) {
        final BigInteger subtract = bigInteger.subtract(GOST3410ParametersGenerator.ONE);
        divide = subtract.divide(divide);
        final int bitLength = bigInteger.bitLength();
        BigInteger modPow;
        while (true) {
            final BigInteger bigInteger2 = new BigInteger(bitLength, this.init_random);
            if (bigInteger2.compareTo(GOST3410ParametersGenerator.ONE) > 0 && bigInteger2.compareTo(subtract) < 0) {
                modPow = bigInteger2.modPow(divide, bigInteger);
                if (modPow.compareTo(GOST3410ParametersGenerator.ONE) != 0) {
                    break;
                }
                continue;
            }
        }
        return modPow;
    }
    
    public GOST3410Parameters generateParameters() {
        final BigInteger[] array = new BigInteger[2];
        if (this.typeproc == 1) {
            final int nextInt = this.init_random.nextInt();
            final int nextInt2 = this.init_random.nextInt();
            final int size = this.size;
            if (size != 512) {
                if (size != 1024) {
                    throw new IllegalArgumentException("Ooops! key size 512 or 1024 bit.");
                }
                this.procedure_B(nextInt, nextInt2, array);
            }
            else {
                this.procedure_A(nextInt, nextInt2, array, 512);
            }
            final BigInteger bigInteger = array[0];
            final BigInteger bigInteger2 = array[1];
            return new GOST3410Parameters(bigInteger, bigInteger2, this.procedure_C(bigInteger, bigInteger2), new GOST3410ValidationParameters(nextInt, nextInt2));
        }
        final long nextLong = this.init_random.nextLong();
        final long nextLong2 = this.init_random.nextLong();
        final int size2 = this.size;
        if (size2 != 512) {
            if (size2 != 1024) {
                throw new IllegalStateException("Ooops! key size 512 or 1024 bit.");
            }
            this.procedure_Bb(nextLong, nextLong2, array);
        }
        else {
            this.procedure_Aa(nextLong, nextLong2, array, 512);
        }
        final BigInteger bigInteger3 = array[0];
        final BigInteger bigInteger4 = array[1];
        return new GOST3410Parameters(bigInteger3, bigInteger4, this.procedure_C(bigInteger3, bigInteger4), new GOST3410ValidationParameters(nextLong, nextLong2));
    }
    
    public void init(final int size, final int typeproc, final SecureRandom init_random) {
        this.size = size;
        this.typeproc = typeproc;
        this.init_random = init_random;
    }
}
