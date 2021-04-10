package org.spongycastle.pqc.math.ntru.util;

import java.security.*;
import org.spongycastle.pqc.math.ntru.polynomial.*;
import org.spongycastle.util.*;
import java.util.*;
import org.spongycastle.pqc.math.ntru.euclid.*;
import java.io.*;

public class Util
{
    private static volatile boolean IS_64_BITNESS_KNOWN;
    private static volatile boolean IS_64_BIT_JVM;
    
    public static TernaryPolynomial generateRandomTernary(final int n, final int n2, final int n3, final boolean b, final SecureRandom secureRandom) {
        if (b) {
            return SparseTernaryPolynomial.generateRandom(n, n2, n3, secureRandom);
        }
        return DenseTernaryPolynomial.generateRandom(n, n2, n3, secureRandom);
    }
    
    public static int[] generateRandomTernary(final int n, int i, final int n2, final SecureRandom secureRandom) {
        final Integer value = Integers.valueOf(1);
        final Integer value2 = Integers.valueOf(-1);
        final int n3 = 0;
        final Integer value3 = Integers.valueOf(0);
        final ArrayList<Integer> list = new ArrayList<Integer>();
        for (int j = 0; j < i; ++j) {
            list.add(value);
        }
        for (i = 0; i < n2; ++i) {
            list.add(value2);
        }
        while (list.size() < n) {
            list.add(value3);
        }
        Collections.shuffle(list, secureRandom);
        final int[] array = new int[n];
        for (i = n3; i < n; ++i) {
            array[i] = list.get(i);
        }
        return array;
    }
    
    public static int invert(int n, final int n2) {
        final int n3 = n %= n2;
        if (n3 < 0) {
            n = n3 + n2;
        }
        return IntEuclidean.calculate(n, n2).x;
    }
    
    public static boolean is64BitJVM() {
        if (!Util.IS_64_BITNESS_KNOWN) {
            final String property = System.getProperty("os.arch");
            final String property2 = System.getProperty("sun.arch.data.model");
            Util.IS_64_BIT_JVM = ("amd64".equals(property) || "x86_64".equals(property) || "ppc64".equals(property) || "64".equals(property2));
            Util.IS_64_BITNESS_KNOWN = true;
        }
        return Util.IS_64_BIT_JVM;
    }
    
    public static int pow(final int n, final int n2, final int n3) {
        int n4 = 1;
        for (int i = 0; i < n2; ++i) {
            n4 = n4 * n % n3;
        }
        return n4;
    }
    
    public static long pow(final long n, final int n2, final long n3) {
        long n4 = 1L;
        for (int i = 0; i < n2; ++i) {
            n4 = n4 * n % n3;
        }
        return n4;
    }
    
    public static byte[] readFullLength(final InputStream inputStream, final int n) throws IOException {
        final byte[] array = new byte[n];
        if (inputStream.read(array) == n) {
            return array;
        }
        throw new IOException("Not enough bytes to read.");
    }
}
