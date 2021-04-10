package org.spongycastle.math.ec.tools;

import java.math.*;
import java.security.*;
import org.spongycastle.util.*;
import java.io.*;
import org.spongycastle.asn1.x9.*;
import org.spongycastle.crypto.ec.*;
import org.spongycastle.math.ec.*;
import java.util.*;

public class TraceOptimizer
{
    private static final BigInteger ONE;
    private static final SecureRandom R;
    
    static {
        ONE = BigInteger.valueOf(1L);
        R = new SecureRandom();
    }
    
    private static int calculateTrace(ECFieldElement square) {
        final int fieldSize = square.getFieldSize();
        ECFieldElement add = square;
        for (int i = 1; i < fieldSize; ++i) {
            square = square.square();
            add = add.add(square);
        }
        final BigInteger bigInteger = add.toBigInteger();
        if (bigInteger.bitLength() <= 1) {
            return bigInteger.intValue();
        }
        throw new IllegalStateException();
    }
    
    private static ArrayList enumToList(final Enumeration enumeration) {
        final ArrayList<Object> list = new ArrayList<Object>();
        while (enumeration.hasMoreElements()) {
            list.add(enumeration.nextElement());
        }
        return list;
    }
    
    public static void implPrintNonZeroTraceBits(final X9ECParameters x9ECParameters) {
        final ECCurve curve = x9ECParameters.getCurve();
        final int fieldSize = curve.getFieldSize();
        final ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < fieldSize; ++i) {
            if (calculateTrace(curve.fromBigInteger(TraceOptimizer.ONE.shiftLeft(i))) != 0) {
                list.add(Integers.valueOf(i));
                final PrintStream out = System.out;
                final StringBuilder sb = new StringBuilder();
                sb.append(" ");
                sb.append(i);
                out.print(sb.toString());
            }
        }
        System.out.println();
        for (int j = 0; j < 1000; ++j) {
            final BigInteger bigInteger = new BigInteger(fieldSize, TraceOptimizer.R);
            final int calculateTrace = calculateTrace(curve.fromBigInteger(bigInteger));
            int k = 0;
            int n = 0;
            while (k < list.size()) {
                int n2 = n;
                if (bigInteger.testBit(list.get(k))) {
                    n2 = (n ^ 0x1);
                }
                ++k;
                n = n2;
            }
            if (calculateTrace != n) {
                throw new IllegalStateException("Optimized-trace sanity check failed");
            }
        }
    }
    
    public static void main(final String[] array) {
        final TreeSet<String> set = new TreeSet<String>(enumToList(ECNamedCurveTable.getNames()));
        set.addAll((Collection<?>)enumToList(CustomNamedCurves.getNames()));
        for (final String s : set) {
            X9ECParameters x9ECParameters;
            if ((x9ECParameters = CustomNamedCurves.getByName(s)) == null) {
                x9ECParameters = ECNamedCurveTable.getByName(s);
            }
            if (x9ECParameters != null && ECAlgorithms.isF2mCurve(x9ECParameters.getCurve())) {
                final PrintStream out = System.out;
                final StringBuilder sb = new StringBuilder();
                sb.append(s);
                sb.append(":");
                out.print(sb.toString());
                implPrintNonZeroTraceBits(x9ECParameters);
            }
        }
    }
    
    public static void printNonZeroTraceBits(final X9ECParameters x9ECParameters) {
        if (ECAlgorithms.isF2mCurve(x9ECParameters.getCurve())) {
            implPrintNonZeroTraceBits(x9ECParameters);
            return;
        }
        throw new IllegalArgumentException("Trace only defined over characteristic-2 fields");
    }
}
