package org.spongycastle.math.ec.tools;

import java.math.*;
import org.spongycastle.asn1.x9.*;
import org.spongycastle.crypto.ec.*;
import org.spongycastle.math.ec.*;
import java.util.*;
import java.io.*;

public class F2mSqrtOptimizer
{
    private static ArrayList enumToList(final Enumeration enumeration) {
        final ArrayList<Object> list = new ArrayList<Object>();
        while (enumeration.hasMoreElements()) {
            list.add(enumeration.nextElement());
        }
        return list;
    }
    
    private static void implPrintRootZ(final X9ECParameters x9ECParameters) {
        final ECFieldElement fromBigInteger = x9ECParameters.getCurve().fromBigInteger(BigInteger.valueOf(2L));
        final ECFieldElement sqrt = fromBigInteger.sqrt();
        System.out.println(sqrt.toBigInteger().toString(16).toUpperCase());
        if (sqrt.square().equals(fromBigInteger)) {
            return;
        }
        throw new IllegalStateException("Optimized-sqrt sanity check failed");
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
                implPrintRootZ(x9ECParameters);
            }
        }
    }
    
    public static void printRootZ(final X9ECParameters x9ECParameters) {
        if (ECAlgorithms.isF2mCurve(x9ECParameters.getCurve())) {
            implPrintRootZ(x9ECParameters);
            return;
        }
        throw new IllegalArgumentException("Sqrt optimization only defined over characteristic-2 fields");
    }
}
