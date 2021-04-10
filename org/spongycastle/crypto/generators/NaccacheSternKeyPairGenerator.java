package org.spongycastle.crypto.generators;

import java.math.*;
import java.security.*;
import java.util.*;
import org.spongycastle.crypto.params.*;
import java.io.*;
import org.spongycastle.crypto.*;

public class NaccacheSternKeyPairGenerator implements AsymmetricCipherKeyPairGenerator
{
    private static final BigInteger ONE;
    private static int[] smallPrimes;
    private NaccacheSternKeyGenerationParameters param;
    
    static {
        NaccacheSternKeyPairGenerator.smallPrimes = new int[] { 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97, 101, 103, 107, 109, 113, 127, 131, 137, 139, 149, 151, 157, 163, 167, 173, 179, 181, 191, 193, 197, 199, 211, 223, 227, 229, 233, 239, 241, 251, 257, 263, 269, 271, 277, 281, 283, 293, 307, 311, 313, 317, 331, 337, 347, 349, 353, 359, 367, 373, 379, 383, 389, 397, 401, 409, 419, 421, 431, 433, 439, 443, 449, 457, 461, 463, 467, 479, 487, 491, 499, 503, 509, 521, 523, 541, 547, 557 };
        ONE = BigInteger.valueOf(1L);
    }
    
    private static Vector findFirstPrimes(final int n) {
        final Vector<BigInteger> vector = new Vector<BigInteger>(n);
        for (int i = 0; i != n; ++i) {
            vector.addElement(BigInteger.valueOf(NaccacheSternKeyPairGenerator.smallPrimes[i]));
        }
        return vector;
    }
    
    private static BigInteger generatePrime(final int n, final int n2, final SecureRandom secureRandom) {
        BigInteger bigInteger;
        for (bigInteger = new BigInteger(n, n2, secureRandom); bigInteger.bitLength() != n; bigInteger = new BigInteger(n, n2, secureRandom)) {}
        return bigInteger;
    }
    
    private static int getInt(final SecureRandom secureRandom, final int n) {
        if ((-n & n) == n) {
            return (int)(n * (long)(secureRandom.nextInt() & Integer.MAX_VALUE) >> 31);
        }
        int n2;
        int n3;
        do {
            n2 = (secureRandom.nextInt() & Integer.MAX_VALUE);
            n3 = n2 % n;
        } while (n2 - n3 + (n - 1) < 0);
        return n3;
    }
    
    private static Vector permuteList(final Vector vector, final SecureRandom secureRandom) {
        final Vector<Object> vector2 = new Vector<Object>();
        final Vector<Object> vector3 = new Vector<Object>();
        for (int i = 0; i < vector.size(); ++i) {
            vector3.addElement(vector.elementAt(i));
        }
        vector2.addElement(vector3.elementAt(0));
        while (true) {
            vector3.removeElementAt(0);
            if (vector3.size() == 0) {
                break;
            }
            vector2.insertElementAt(vector3.elementAt(0), getInt(secureRandom, vector2.size() + 1));
        }
        return vector2;
    }
    
    @Override
    public AsymmetricCipherKeyPair generateKeyPair() {
        final int strength = this.param.getStrength();
        final SecureRandom random = this.param.getRandom();
        final int certainty = this.param.getCertainty();
        final boolean debug = this.param.isDebug();
        if (debug) {
            final PrintStream out = System.out;
            final StringBuilder sb = new StringBuilder();
            sb.append("Fetching first ");
            sb.append(this.param.getCntSmallPrimes());
            sb.append(" primes.");
            out.println(sb.toString());
        }
        final Vector permuteList = permuteList(findFirstPrimes(this.param.getCntSmallPrimes()), random);
        BigInteger bigInteger;
        BigInteger multiply = bigInteger = NaccacheSternKeyPairGenerator.ONE;
        for (int i = 0; i < permuteList.size() / 2; ++i) {
            bigInteger = bigInteger.multiply(permuteList.elementAt(i));
        }
        for (int j = permuteList.size() / 2; j < permuteList.size(); ++j) {
            multiply = multiply.multiply(permuteList.elementAt(j));
        }
        final BigInteger multiply2 = bigInteger.multiply(multiply);
        final int n = (strength - multiply2.bitLength() - 48) / 2 + 1;
        final BigInteger generatePrime = generatePrime(n, certainty, random);
        final BigInteger generatePrime2 = generatePrime(n, certainty, random);
        if (debug) {
            System.out.println("generating p and q");
        }
        final BigInteger shiftLeft = generatePrime.multiply(bigInteger).shiftLeft(1);
        BigInteger shiftLeft2 = generatePrime2.multiply(multiply).shiftLeft(1);
        long n2 = 0L;
        BigInteger generatePrime3;
        BigInteger add;
        BigInteger generatePrime4;
        BigInteger add2;
        while (true) {
            ++n2;
            generatePrime3 = generatePrime(24, certainty, random);
            add = generatePrime3.multiply(shiftLeft).add(NaccacheSternKeyPairGenerator.ONE);
            BigInteger bigInteger2 = shiftLeft2;
            BigInteger bigInteger3;
            if (!add.isProbablePrime(certainty)) {
                bigInteger3 = shiftLeft2;
            }
            else {
                BigInteger bigInteger4;
                while (true) {
                    generatePrime4 = generatePrime(24, certainty, random);
                    if (generatePrime3.equals(generatePrime4)) {
                        continue;
                    }
                    final BigInteger multiply3 = generatePrime4.multiply(bigInteger2);
                    bigInteger4 = bigInteger2;
                    add2 = multiply3.add(NaccacheSternKeyPairGenerator.ONE);
                    if (add2.isProbablePrime(certainty)) {
                        break;
                    }
                    bigInteger2 = bigInteger4;
                }
                if (!multiply2.gcd(generatePrime3.multiply(generatePrime4)).equals(NaccacheSternKeyPairGenerator.ONE)) {
                    bigInteger3 = bigInteger4;
                }
                else {
                    if (add.multiply(add2).bitLength() >= strength) {
                        break;
                    }
                    bigInteger3 = bigInteger4;
                    if (debug) {
                        final PrintStream out2 = System.out;
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append("key size too small. Should be ");
                        sb2.append(strength);
                        sb2.append(" but is actually ");
                        sb2.append(add.multiply(add2).bitLength());
                        out2.println(sb2.toString());
                        bigInteger3 = bigInteger4;
                    }
                }
            }
            shiftLeft2 = bigInteger3;
        }
        if (debug) {
            final PrintStream out3 = System.out;
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("needed ");
            sb3.append(n2);
            sb3.append(" tries to generate p and q.");
            out3.println(sb3.toString());
        }
        final BigInteger multiply4 = add.multiply(add2);
        final BigInteger multiply5 = add.subtract(NaccacheSternKeyPairGenerator.ONE).multiply(add2.subtract(NaccacheSternKeyPairGenerator.ONE));
        if (debug) {
            System.out.println("generating g");
        }
        long n3 = 0L;
        final int n4 = strength;
        BigInteger bigInteger6 = null;
        BigInteger bigInteger7 = null;
    Label_1197:
        while (true) {
            final Vector<BigInteger> vector = new Vector<BigInteger>();
            for (int k = 0; k != permuteList.size(); ++k) {
                final BigInteger divide = multiply5.divide(permuteList.elementAt(k));
                BigInteger bigInteger5;
                do {
                    ++n3;
                    bigInteger5 = new BigInteger(n4, certainty, random);
                } while (bigInteger5.modPow(divide, multiply4).equals(NaccacheSternKeyPairGenerator.ONE));
                vector.addElement(bigInteger5);
            }
            bigInteger6 = NaccacheSternKeyPairGenerator.ONE;
            for (int l = 0; l < permuteList.size(); ++l) {
                bigInteger6 = bigInteger6.multiply(vector.elementAt(l).modPow(multiply2.divide(permuteList.elementAt(l)), multiply4)).mod(multiply4);
            }
            while (true) {
                for (int n5 = 0; n5 < permuteList.size(); ++n5) {
                    if (bigInteger6.modPow(multiply5.divide(permuteList.elementAt(n5)), multiply4).equals(NaccacheSternKeyPairGenerator.ONE)) {
                        if (debug) {
                            final PrintStream out4 = System.out;
                            final StringBuilder sb4 = new StringBuilder();
                            sb4.append("g has order phi(n)/");
                            sb4.append(permuteList.elementAt(n5));
                            sb4.append("\n g: ");
                            sb4.append(bigInteger6);
                            out4.println(sb4.toString());
                        }
                        final boolean b = true;
                        if (!b) {
                            PrintStream printStream;
                            StringBuilder sb5;
                            String s;
                            if (bigInteger6.modPow(multiply5.divide(BigInteger.valueOf(4L)), multiply4).equals(NaccacheSternKeyPairGenerator.ONE)) {
                                if (!debug) {
                                    continue Label_1119;
                                }
                                printStream = System.out;
                                sb5 = new StringBuilder();
                                s = "g has order phi(n)/4\n g:";
                            }
                            else if (bigInteger6.modPow(multiply5.divide(generatePrime3), multiply4).equals(NaccacheSternKeyPairGenerator.ONE)) {
                                if (!debug) {
                                    continue Label_1119;
                                }
                                printStream = System.out;
                                sb5 = new StringBuilder();
                                s = "g has order phi(n)/p'\n g: ";
                            }
                            else if (bigInteger6.modPow(multiply5.divide(generatePrime4), multiply4).equals(NaccacheSternKeyPairGenerator.ONE)) {
                                if (!debug) {
                                    continue Label_1119;
                                }
                                printStream = System.out;
                                sb5 = new StringBuilder();
                                s = "g has order phi(n)/q'\n g: ";
                            }
                            else if (bigInteger6.modPow(multiply5.divide(generatePrime), multiply4).equals(NaccacheSternKeyPairGenerator.ONE)) {
                                if (!debug) {
                                    continue Label_1119;
                                }
                                printStream = System.out;
                                sb5 = new StringBuilder();
                                s = "g has order phi(n)/a\n g: ";
                            }
                            else {
                                bigInteger7 = generatePrime2;
                                if (!bigInteger6.modPow(multiply5.divide(bigInteger7), multiply4).equals(NaccacheSternKeyPairGenerator.ONE)) {
                                    break Label_1197;
                                }
                                if (debug) {
                                    final PrintStream out5 = System.out;
                                    final StringBuilder sb6 = new StringBuilder();
                                    sb6.append("g has order phi(n)/b\n g: ");
                                    sb6.append(bigInteger6);
                                    out5.println(sb6.toString());
                                    continue Label_1119;
                                }
                                continue Label_1119;
                            }
                            sb5.append(s);
                            sb5.append(bigInteger6);
                            printStream.println(sb5.toString());
                        }
                        Label_1119: {
                            continue Label_1197;
                        }
                    }
                }
                final boolean b = false;
                continue;
            }
        }
        if (debug) {
            final PrintStream out6 = System.out;
            final StringBuilder sb7 = new StringBuilder();
            sb7.append("needed ");
            sb7.append(n3);
            sb7.append(" tries to generate g");
            out6.println(sb7.toString());
            System.out.println();
            System.out.println("found new NaccacheStern cipher variables:");
            final PrintStream out7 = System.out;
            final StringBuilder sb8 = new StringBuilder();
            sb8.append("smallPrimes: ");
            sb8.append(permuteList);
            out7.println(sb8.toString());
            final PrintStream out8 = System.out;
            final StringBuilder sb9 = new StringBuilder();
            sb9.append("sigma:...... ");
            sb9.append(multiply2);
            sb9.append(" (");
            sb9.append(multiply2.bitLength());
            sb9.append(" bits)");
            out8.println(sb9.toString());
            final PrintStream out9 = System.out;
            final StringBuilder sb10 = new StringBuilder();
            sb10.append("a:.......... ");
            sb10.append(generatePrime);
            out9.println(sb10.toString());
            final PrintStream out10 = System.out;
            final StringBuilder sb11 = new StringBuilder();
            sb11.append("b:.......... ");
            sb11.append(bigInteger7);
            out10.println(sb11.toString());
            final PrintStream out11 = System.out;
            final StringBuilder sb12 = new StringBuilder();
            sb12.append("p':......... ");
            sb12.append(generatePrime3);
            out11.println(sb12.toString());
            final PrintStream out12 = System.out;
            final StringBuilder sb13 = new StringBuilder();
            sb13.append("q':......... ");
            sb13.append(generatePrime4);
            out12.println(sb13.toString());
            final PrintStream out13 = System.out;
            final StringBuilder sb14 = new StringBuilder();
            sb14.append("p:.......... ");
            sb14.append(add);
            out13.println(sb14.toString());
            final PrintStream out14 = System.out;
            final StringBuilder sb15 = new StringBuilder();
            sb15.append("q:.......... ");
            sb15.append(add2);
            out14.println(sb15.toString());
            final PrintStream out15 = System.out;
            final StringBuilder sb16 = new StringBuilder();
            sb16.append("n:.......... ");
            sb16.append(multiply4);
            out15.println(sb16.toString());
            final PrintStream out16 = System.out;
            final StringBuilder sb17 = new StringBuilder();
            sb17.append("phi(n):..... ");
            sb17.append(multiply5);
            out16.println(sb17.toString());
            final PrintStream out17 = System.out;
            final StringBuilder sb18 = new StringBuilder();
            sb18.append("g:.......... ");
            sb18.append(bigInteger6);
            out17.println(sb18.toString());
            System.out.println();
        }
        return new AsymmetricCipherKeyPair(new NaccacheSternKeyParameters(false, bigInteger6, multiply4, multiply2.bitLength()), new NaccacheSternPrivateKeyParameters(bigInteger6, multiply4, multiply2.bitLength(), permuteList, multiply5));
    }
    
    @Override
    public void init(final KeyGenerationParameters keyGenerationParameters) {
        this.param = (NaccacheSternKeyGenerationParameters)keyGenerationParameters;
    }
}
