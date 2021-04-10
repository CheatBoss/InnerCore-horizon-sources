package org.spongycastle.pqc.math.ntru.polynomial;

import java.math.*;
import java.io.*;
import org.spongycastle.pqc.math.ntru.util.*;
import org.spongycastle.util.*;
import org.spongycastle.pqc.math.ntru.euclid.*;
import java.util.*;
import java.util.concurrent.*;

public class IntegerPolynomial implements Polynomial
{
    private static final List BIGINT_PRIMES;
    private static final int NUM_EQUAL_RESULTANTS = 3;
    private static final int[] PRIMES;
    public int[] coeffs;
    
    static {
        PRIMES = new int[] { 4507, 4513, 4517, 4519, 4523, 4547, 4549, 4561, 4567, 4583, 4591, 4597, 4603, 4621, 4637, 4639, 4643, 4649, 4651, 4657, 4663, 4673, 4679, 4691, 4703, 4721, 4723, 4729, 4733, 4751, 4759, 4783, 4787, 4789, 4793, 4799, 4801, 4813, 4817, 4831, 4861, 4871, 4877, 4889, 4903, 4909, 4919, 4931, 4933, 4937, 4943, 4951, 4957, 4967, 4969, 4973, 4987, 4993, 4999, 5003, 5009, 5011, 5021, 5023, 5039, 5051, 5059, 5077, 5081, 5087, 5099, 5101, 5107, 5113, 5119, 5147, 5153, 5167, 5171, 5179, 5189, 5197, 5209, 5227, 5231, 5233, 5237, 5261, 5273, 5279, 5281, 5297, 5303, 5309, 5323, 5333, 5347, 5351, 5381, 5387, 5393, 5399, 5407, 5413, 5417, 5419, 5431, 5437, 5441, 5443, 5449, 5471, 5477, 5479, 5483, 5501, 5503, 5507, 5519, 5521, 5527, 5531, 5557, 5563, 5569, 5573, 5581, 5591, 5623, 5639, 5641, 5647, 5651, 5653, 5657, 5659, 5669, 5683, 5689, 5693, 5701, 5711, 5717, 5737, 5741, 5743, 5749, 5779, 5783, 5791, 5801, 5807, 5813, 5821, 5827, 5839, 5843, 5849, 5851, 5857, 5861, 5867, 5869, 5879, 5881, 5897, 5903, 5923, 5927, 5939, 5953, 5981, 5987, 6007, 6011, 6029, 6037, 6043, 6047, 6053, 6067, 6073, 6079, 6089, 6091, 6101, 6113, 6121, 6131, 6133, 6143, 6151, 6163, 6173, 6197, 6199, 6203, 6211, 6217, 6221, 6229, 6247, 6257, 6263, 6269, 6271, 6277, 6287, 6299, 6301, 6311, 6317, 6323, 6329, 6337, 6343, 6353, 6359, 6361, 6367, 6373, 6379, 6389, 6397, 6421, 6427, 6449, 6451, 6469, 6473, 6481, 6491, 6521, 6529, 6547, 6551, 6553, 6563, 6569, 6571, 6577, 6581, 6599, 6607, 6619, 6637, 6653, 6659, 6661, 6673, 6679, 6689, 6691, 6701, 6703, 6709, 6719, 6733, 6737, 6761, 6763, 6779, 6781, 6791, 6793, 6803, 6823, 6827, 6829, 6833, 6841, 6857, 6863, 6869, 6871, 6883, 6899, 6907, 6911, 6917, 6947, 6949, 6959, 6961, 6967, 6971, 6977, 6983, 6991, 6997, 7001, 7013, 7019, 7027, 7039, 7043, 7057, 7069, 7079, 7103, 7109, 7121, 7127, 7129, 7151, 7159, 7177, 7187, 7193, 7207, 7211, 7213, 7219, 7229, 7237, 7243, 7247, 7253, 7283, 7297, 7307, 7309, 7321, 7331, 7333, 7349, 7351, 7369, 7393, 7411, 7417, 7433, 7451, 7457, 7459, 7477, 7481, 7487, 7489, 7499, 7507, 7517, 7523, 7529, 7537, 7541, 7547, 7549, 7559, 7561, 7573, 7577, 7583, 7589, 7591, 7603, 7607, 7621, 7639, 7643, 7649, 7669, 7673, 7681, 7687, 7691, 7699, 7703, 7717, 7723, 7727, 7741, 7753, 7757, 7759, 7789, 7793, 7817, 7823, 7829, 7841, 7853, 7867, 7873, 7877, 7879, 7883, 7901, 7907, 7919, 7927, 7933, 7937, 7949, 7951, 7963, 7993, 8009, 8011, 8017, 8039, 8053, 8059, 8069, 8081, 8087, 8089, 8093, 8101, 8111, 8117, 8123, 8147, 8161, 8167, 8171, 8179, 8191, 8209, 8219, 8221, 8231, 8233, 8237, 8243, 8263, 8269, 8273, 8287, 8291, 8293, 8297, 8311, 8317, 8329, 8353, 8363, 8369, 8377, 8387, 8389, 8419, 8423, 8429, 8431, 8443, 8447, 8461, 8467, 8501, 8513, 8521, 8527, 8537, 8539, 8543, 8563, 8573, 8581, 8597, 8599, 8609, 8623, 8627, 8629, 8641, 8647, 8663, 8669, 8677, 8681, 8689, 8693, 8699, 8707, 8713, 8719, 8731, 8737, 8741, 8747, 8753, 8761, 8779, 8783, 8803, 8807, 8819, 8821, 8831, 8837, 8839, 8849, 8861, 8863, 8867, 8887, 8893, 8923, 8929, 8933, 8941, 8951, 8963, 8969, 8971, 8999, 9001, 9007, 9011, 9013, 9029, 9041, 9043, 9049, 9059, 9067, 9091, 9103, 9109, 9127, 9133, 9137, 9151, 9157, 9161, 9173, 9181, 9187, 9199, 9203, 9209, 9221, 9227, 9239, 9241, 9257, 9277, 9281, 9283, 9293, 9311, 9319, 9323, 9337, 9341, 9343, 9349, 9371, 9377, 9391, 9397, 9403, 9413, 9419, 9421, 9431, 9433, 9437, 9439, 9461, 9463, 9467, 9473, 9479, 9491, 9497, 9511, 9521, 9533, 9539, 9547, 9551, 9587, 9601, 9613, 9619, 9623, 9629, 9631, 9643, 9649, 9661, 9677, 9679, 9689, 9697, 9719, 9721, 9733, 9739, 9743, 9749, 9767, 9769, 9781, 9787, 9791, 9803, 9811, 9817, 9829, 9833, 9839, 9851, 9857, 9859, 9871, 9883, 9887, 9901, 9907, 9923, 9929, 9931, 9941, 9949, 9967, 9973 };
        BIGINT_PRIMES = new ArrayList();
        int n = 0;
        while (true) {
            final int[] primes = IntegerPolynomial.PRIMES;
            if (n == primes.length) {
                break;
            }
            IntegerPolynomial.BIGINT_PRIMES.add(BigInteger.valueOf(primes[n]));
            ++n;
        }
    }
    
    public IntegerPolynomial(final int n) {
        this.coeffs = new int[n];
    }
    
    public IntegerPolynomial(final BigIntPolynomial bigIntPolynomial) {
        this.coeffs = new int[bigIntPolynomial.coeffs.length];
        for (int i = 0; i < bigIntPolynomial.coeffs.length; ++i) {
            this.coeffs[i] = bigIntPolynomial.coeffs[i].intValue();
        }
    }
    
    public IntegerPolynomial(final int[] coeffs) {
        this.coeffs = coeffs;
    }
    
    private boolean equalsAbsOne() {
        int n = 1;
        while (true) {
            final int[] coeffs = this.coeffs;
            if (n >= coeffs.length) {
                return Math.abs(coeffs[0]) == 1;
            }
            if (coeffs[n] != 0) {
                return false;
            }
            ++n;
        }
    }
    
    private boolean equalsZero() {
        int n = 0;
        while (true) {
            final int[] coeffs = this.coeffs;
            if (n >= coeffs.length) {
                return true;
            }
            if (coeffs[n] != 0) {
                return false;
            }
            ++n;
        }
    }
    
    public static IntegerPolynomial fromBinary(final InputStream inputStream, final int n, final int n2) throws IOException {
        return new IntegerPolynomial(ArrayEncoder.decodeModQ(inputStream, n, n2));
    }
    
    public static IntegerPolynomial fromBinary(final byte[] array, final int n, final int n2) {
        return new IntegerPolynomial(ArrayEncoder.decodeModQ(array, n, n2));
    }
    
    public static IntegerPolynomial fromBinary3Sves(final byte[] array, final int n) {
        return new IntegerPolynomial(ArrayEncoder.decodeMod3Sves(array, n));
    }
    
    public static IntegerPolynomial fromBinary3Tight(final InputStream inputStream, final int n) throws IOException {
        return new IntegerPolynomial(ArrayEncoder.decodeMod3Tight(inputStream, n));
    }
    
    public static IntegerPolynomial fromBinary3Tight(final byte[] array, final int n) {
        return new IntegerPolynomial(ArrayEncoder.decodeMod3Tight(array, n));
    }
    
    private IntegerPolynomial mod2ToModq(final IntegerPolynomial integerPolynomial, final int n) {
        final boolean is64BitJVM = Util.is64BitJVM();
        int j;
        int i = j = 2;
        IntegerPolynomial integerPolynomial2 = integerPolynomial;
        if (is64BitJVM) {
            j = i;
            integerPolynomial2 = integerPolynomial;
            if (n == 2048) {
                final LongPolynomial2 longPolynomial2 = new LongPolynomial2(this);
                LongPolynomial2 longPolynomial3 = new LongPolynomial2(integerPolynomial);
                while (i < n) {
                    i *= 2;
                    final LongPolynomial2 longPolynomial4 = (LongPolynomial2)longPolynomial3.clone();
                    final int n2 = i - 1;
                    longPolynomial4.mult2And(n2);
                    longPolynomial4.subAnd(longPolynomial2.mult(longPolynomial3).mult(longPolynomial3), n2);
                    longPolynomial3 = longPolynomial4;
                }
                return longPolynomial3.toIntegerPolynomial();
            }
        }
        while (j < n) {
            j *= 2;
            final int[] coeffs = integerPolynomial2.coeffs;
            final IntegerPolynomial integerPolynomial3 = new IntegerPolynomial(Arrays.copyOf(coeffs, coeffs.length));
            integerPolynomial3.mult2(j);
            integerPolynomial3.sub(this.mult(integerPolynomial2, j).mult(integerPolynomial2, j), j);
            integerPolynomial2 = integerPolynomial3;
        }
        return integerPolynomial2;
    }
    
    private void mult2(final int n) {
        int n2 = 0;
        while (true) {
            final int[] coeffs = this.coeffs;
            if (n2 >= coeffs.length) {
                break;
            }
            coeffs[n2] *= 2;
            coeffs[n2] %= n;
            ++n2;
        }
    }
    
    private IntegerPolynomial multRecursive(IntegerPolynomial multRecursive) {
        final int[] coeffs = this.coeffs;
        final int[] coeffs2 = multRecursive.coeffs;
        final int length = coeffs2.length;
        final int n = 0;
        if (length <= 32) {
            final int n2 = length * 2 - 1;
            multRecursive = new IntegerPolynomial(new int[n2]);
            for (int i = 0; i < n2; ++i) {
                for (int j = Math.max(0, i - length + 1); j <= Math.min(i, length - 1); ++j) {
                    final int[] coeffs3 = multRecursive.coeffs;
                    coeffs3[i] += coeffs2[j] * coeffs[i - j];
                }
            }
            return multRecursive;
        }
        final int n3 = length / 2;
        multRecursive = new IntegerPolynomial(Arrays.copyOf(coeffs, n3));
        final IntegerPolynomial integerPolynomial = new IntegerPolynomial(Arrays.copyOfRange(coeffs, n3, length));
        final IntegerPolynomial integerPolynomial2 = new IntegerPolynomial(Arrays.copyOf(coeffs2, n3));
        final IntegerPolynomial integerPolynomial3 = new IntegerPolynomial(Arrays.copyOfRange(coeffs2, n3, length));
        final IntegerPolynomial integerPolynomial4 = (IntegerPolynomial)multRecursive.clone();
        integerPolynomial4.add(integerPolynomial);
        final IntegerPolynomial integerPolynomial5 = (IntegerPolynomial)integerPolynomial2.clone();
        integerPolynomial5.add(integerPolynomial3);
        final IntegerPolynomial multRecursive2 = multRecursive.multRecursive(integerPolynomial2);
        multRecursive = integerPolynomial.multRecursive(integerPolynomial3);
        final IntegerPolynomial multRecursive3 = integerPolynomial4.multRecursive(integerPolynomial5);
        multRecursive3.sub(multRecursive2);
        multRecursive3.sub(multRecursive);
        final IntegerPolynomial integerPolynomial6 = new IntegerPolynomial(length * 2 - 1);
        int n4 = 0;
        while (true) {
            final int[] coeffs4 = multRecursive2.coeffs;
            if (n4 >= coeffs4.length) {
                break;
            }
            integerPolynomial6.coeffs[n4] = coeffs4[n4];
            ++n4;
        }
        int n5 = 0;
        int n6;
        while (true) {
            final int[] coeffs5 = multRecursive3.coeffs;
            n6 = n;
            if (n5 >= coeffs5.length) {
                break;
            }
            final int[] coeffs6 = integerPolynomial6.coeffs;
            final int n7 = n3 + n5;
            coeffs6[n7] += coeffs5[n5];
            ++n5;
        }
        while (true) {
            final int[] coeffs7 = multRecursive.coeffs;
            if (n6 >= coeffs7.length) {
                break;
            }
            final int[] coeffs8 = integerPolynomial6.coeffs;
            final int n8 = n3 * 2 + n6;
            coeffs8[n8] += coeffs7[n6];
            ++n6;
        }
        return integerPolynomial6;
    }
    
    private void multShiftSub(final IntegerPolynomial integerPolynomial, final int n, final int n2, final int n3) {
        for (int length = this.coeffs.length, i = n2; i < length; ++i) {
            final int[] coeffs = this.coeffs;
            coeffs[i] = (coeffs[i] - integerPolynomial.coeffs[i - n2] * n) % n3;
        }
    }
    
    private void sort(final int[] array) {
        int i = 1;
        while (i != 0) {
            int j = 0;
            i = 0;
            while (j != array.length - 1) {
                final int n = array[j];
                final int n2 = j + 1;
                if (n > array[n2]) {
                    final int n3 = array[j];
                    array[j] = array[n2];
                    array[n2] = n3;
                    i = 1;
                }
                j = n2;
            }
        }
    }
    
    private BigInteger squareSum() {
        BigInteger bigInteger = Constants.BIGINT_ZERO;
        int n = 0;
        while (true) {
            final int[] coeffs = this.coeffs;
            if (n >= coeffs.length) {
                break;
            }
            bigInteger = bigInteger.add(BigInteger.valueOf(coeffs[n] * coeffs[n]));
            ++n;
        }
        return bigInteger;
    }
    
    public void add(final IntegerPolynomial integerPolynomial) {
        final int[] coeffs = integerPolynomial.coeffs;
        final int length = coeffs.length;
        final int[] coeffs2 = this.coeffs;
        if (length > coeffs2.length) {
            this.coeffs = Arrays.copyOf(coeffs2, coeffs.length);
        }
        int n = 0;
        while (true) {
            final int[] coeffs3 = integerPolynomial.coeffs;
            if (n >= coeffs3.length) {
                break;
            }
            final int[] coeffs4 = this.coeffs;
            coeffs4[n] += coeffs3[n];
            ++n;
        }
    }
    
    public void add(final IntegerPolynomial integerPolynomial, final int n) {
        this.add(integerPolynomial);
        this.mod(n);
    }
    
    public void center0(final int n) {
        for (int i = 0; i < this.coeffs.length; ++i) {
            while (true) {
                final int[] coeffs = this.coeffs;
                if (coeffs[i] >= -n / 2) {
                    break;
                }
                coeffs[i] += n;
            }
            while (true) {
                final int[] coeffs2 = this.coeffs;
                if (coeffs2[i] <= n / 2) {
                    break;
                }
                coeffs2[i] -= n;
            }
        }
    }
    
    public long centeredNormSq(int n) {
        final int length = this.coeffs.length;
        final IntegerPolynomial integerPolynomial = (IntegerPolynomial)this.clone();
        integerPolynomial.shiftGap(n);
        long n2 = 0L;
        n = 0;
        long n3 = 0L;
        while (true) {
            final int[] coeffs = integerPolynomial.coeffs;
            if (n == coeffs.length) {
                break;
            }
            final int n4 = coeffs[n];
            final long n5 = n4;
            final long n6 = n4 * n4;
            ++n;
            n3 += n6;
            n2 += n5;
        }
        return n3 - n2 * n2 / length;
    }
    
    public void clear() {
        int n = 0;
        while (true) {
            final int[] coeffs = this.coeffs;
            if (n >= coeffs.length) {
                break;
            }
            coeffs[n] = 0;
            ++n;
        }
    }
    
    public Object clone() {
        return new IntegerPolynomial(this.coeffs.clone());
    }
    
    public int count(final int n) {
        int n2 = 0;
        int n3 = 0;
        while (true) {
            final int[] coeffs = this.coeffs;
            if (n2 == coeffs.length) {
                break;
            }
            int n4 = n3;
            if (coeffs[n2] == n) {
                n4 = n3 + 1;
            }
            ++n2;
            n3 = n4;
        }
        return n3;
    }
    
    int degree() {
        int length = this.coeffs.length;
        do {
            --length;
        } while (length > 0 && this.coeffs[length] == 0);
        return length;
    }
    
    public void div(final int n) {
        final int n2 = (n + 1) / 2;
        int n3 = 0;
        while (true) {
            final int[] coeffs = this.coeffs;
            if (n3 >= coeffs.length) {
                break;
            }
            final int n4 = coeffs[n3];
            int n5;
            if (coeffs[n3] > 0) {
                n5 = n2;
            }
            else {
                n5 = -n2;
            }
            coeffs[n3] = n4 + n5;
            final int[] coeffs2 = this.coeffs;
            coeffs2[n3] /= n;
            ++n3;
        }
    }
    
    public void ensurePositive(final int n) {
        for (int i = 0; i < this.coeffs.length; ++i) {
            while (true) {
                final int[] coeffs = this.coeffs;
                if (coeffs[i] >= 0) {
                    break;
                }
                coeffs[i] += n;
            }
        }
    }
    
    @Override
    public boolean equals(final Object o) {
        return o instanceof IntegerPolynomial && Arrays.areEqual(this.coeffs, ((IntegerPolynomial)o).coeffs);
    }
    
    public boolean equalsOne() {
        int n = 1;
        while (true) {
            final int[] coeffs = this.coeffs;
            if (n >= coeffs.length) {
                return coeffs[0] == 1;
            }
            if (coeffs[n] != 0) {
                return false;
            }
            ++n;
        }
    }
    
    public IntegerPolynomial invertF3() {
        final int length = this.coeffs.length;
        final int n = length + 1;
        IntegerPolynomial integerPolynomial = new IntegerPolynomial(n);
        integerPolynomial.coeffs[0] = 1;
        IntegerPolynomial integerPolynomial2 = new IntegerPolynomial(n);
        IntegerPolynomial integerPolynomial3 = new IntegerPolynomial(n);
        integerPolynomial3.coeffs = Arrays.copyOf(this.coeffs, n);
        integerPolynomial3.modPositive(3);
        IntegerPolynomial integerPolynomial4 = new IntegerPolynomial(n);
        final int[] coeffs = integerPolynomial4.coeffs;
        coeffs[0] = -1;
        coeffs[length] = 1;
        int n2 = 0;
        while (true) {
            if (integerPolynomial3.coeffs[0] == 0) {
                for (int i = 1; i <= length; ++i) {
                    final int[] coeffs2 = integerPolynomial3.coeffs;
                    coeffs2[i - 1] = coeffs2[i];
                    final int[] coeffs3 = integerPolynomial2.coeffs;
                    coeffs3[n - i] = coeffs3[length - i];
                }
                integerPolynomial3.coeffs[length] = 0;
                integerPolynomial2.coeffs[0] = 0;
                ++n2;
                if (integerPolynomial3.equalsZero()) {
                    return null;
                }
                continue;
            }
            else if (integerPolynomial3.equalsAbsOne()) {
                if (integerPolynomial.coeffs[length] != 0) {
                    return null;
                }
                final IntegerPolynomial integerPolynomial5 = new IntegerPolynomial(length);
                for (int j = length - 1; j >= 0; --j) {
                    final int n3 = j - n2 % length;
                    int n4;
                    if ((n4 = n3) < 0) {
                        n4 = n3 + length;
                    }
                    integerPolynomial5.coeffs[n4] = integerPolynomial3.coeffs[0] * integerPolynomial.coeffs[j];
                }
                integerPolynomial5.ensurePositive(3);
                return integerPolynomial5;
            }
            else {
                IntegerPolynomial integerPolynomial6 = integerPolynomial;
                IntegerPolynomial integerPolynomial7 = integerPolynomial2;
                IntegerPolynomial integerPolynomial8 = integerPolynomial3;
                IntegerPolynomial integerPolynomial9 = integerPolynomial4;
                if (integerPolynomial3.degree() < integerPolynomial4.degree()) {
                    integerPolynomial9 = integerPolynomial3;
                    integerPolynomial8 = integerPolynomial4;
                    integerPolynomial7 = integerPolynomial;
                    integerPolynomial6 = integerPolynomial2;
                }
                if (integerPolynomial8.coeffs[0] == integerPolynomial9.coeffs[0]) {
                    integerPolynomial8.sub(integerPolynomial9, 3);
                    integerPolynomial6.sub(integerPolynomial7, 3);
                    integerPolynomial = integerPolynomial6;
                    integerPolynomial2 = integerPolynomial7;
                    integerPolynomial3 = integerPolynomial8;
                    integerPolynomial4 = integerPolynomial9;
                }
                else {
                    integerPolynomial8.add(integerPolynomial9, 3);
                    integerPolynomial6.add(integerPolynomial7, 3);
                    integerPolynomial = integerPolynomial6;
                    integerPolynomial2 = integerPolynomial7;
                    integerPolynomial3 = integerPolynomial8;
                    integerPolynomial4 = integerPolynomial9;
                }
            }
        }
    }
    
    public IntegerPolynomial invertFq(final int n) {
        final int length = this.coeffs.length;
        final int n2 = length + 1;
        IntegerPolynomial integerPolynomial = new IntegerPolynomial(n2);
        integerPolynomial.coeffs[0] = 1;
        IntegerPolynomial integerPolynomial2 = new IntegerPolynomial(n2);
        IntegerPolynomial integerPolynomial3 = new IntegerPolynomial(n2);
        integerPolynomial3.coeffs = Arrays.copyOf(this.coeffs, n2);
        integerPolynomial3.modPositive(2);
        IntegerPolynomial integerPolynomial4 = new IntegerPolynomial(n2);
        final int[] coeffs = integerPolynomial4.coeffs;
        coeffs[length] = (coeffs[0] = 1);
        int n3 = 0;
        while (true) {
            if (integerPolynomial3.coeffs[0] == 0) {
                for (int i = 1; i <= length; ++i) {
                    final int[] coeffs2 = integerPolynomial3.coeffs;
                    coeffs2[i - 1] = coeffs2[i];
                    final int[] coeffs3 = integerPolynomial2.coeffs;
                    coeffs3[n2 - i] = coeffs3[length - i];
                }
                integerPolynomial3.coeffs[length] = 0;
                integerPolynomial2.coeffs[0] = 0;
                ++n3;
                if (integerPolynomial3.equalsZero()) {
                    return null;
                }
                continue;
            }
            else if (integerPolynomial3.equalsOne()) {
                if (integerPolynomial.coeffs[length] != 0) {
                    return null;
                }
                final IntegerPolynomial integerPolynomial5 = new IntegerPolynomial(length);
                for (int j = length - 1; j >= 0; --j) {
                    final int n4 = j - n3 % length;
                    int n5;
                    if ((n5 = n4) < 0) {
                        n5 = n4 + length;
                    }
                    integerPolynomial5.coeffs[n5] = integerPolynomial.coeffs[j];
                }
                return this.mod2ToModq(integerPolynomial5, n);
            }
            else {
                IntegerPolynomial integerPolynomial6 = integerPolynomial;
                IntegerPolynomial integerPolynomial7 = integerPolynomial2;
                IntegerPolynomial integerPolynomial8 = integerPolynomial3;
                IntegerPolynomial integerPolynomial9 = integerPolynomial4;
                if (integerPolynomial3.degree() < integerPolynomial4.degree()) {
                    integerPolynomial9 = integerPolynomial3;
                    integerPolynomial8 = integerPolynomial4;
                    integerPolynomial7 = integerPolynomial;
                    integerPolynomial6 = integerPolynomial2;
                }
                integerPolynomial8.add(integerPolynomial9, 2);
                integerPolynomial6.add(integerPolynomial7, 2);
                integerPolynomial = integerPolynomial6;
                integerPolynomial2 = integerPolynomial7;
                integerPolynomial3 = integerPolynomial8;
                integerPolynomial4 = integerPolynomial9;
            }
        }
    }
    
    public void mod(final int n) {
        int n2 = 0;
        while (true) {
            final int[] coeffs = this.coeffs;
            if (n2 >= coeffs.length) {
                break;
            }
            coeffs[n2] %= n;
            ++n2;
        }
    }
    
    public void mod3() {
        int n = 0;
        while (true) {
            final int[] coeffs = this.coeffs;
            if (n >= coeffs.length) {
                break;
            }
            coeffs[n] %= 3;
            if (coeffs[n] > 1) {
                coeffs[n] -= 3;
            }
            final int[] coeffs2 = this.coeffs;
            if (coeffs2[n] < -1) {
                coeffs2[n] += 3;
            }
            ++n;
        }
    }
    
    void modCenter(final int n) {
        this.mod(n);
        for (int i = 0; i < this.coeffs.length; ++i) {
            int n3;
            while (true) {
                final int[] coeffs = this.coeffs;
                final int n2 = coeffs[i];
                n3 = n / 2;
                if (n2 >= n3) {
                    break;
                }
                coeffs[i] += n;
            }
            while (true) {
                final int[] coeffs2 = this.coeffs;
                if (coeffs2[i] < n3) {
                    break;
                }
                coeffs2[i] -= n;
            }
        }
    }
    
    public void modPositive(final int n) {
        this.mod(n);
        this.ensurePositive(n);
    }
    
    @Override
    public BigIntPolynomial mult(final BigIntPolynomial bigIntPolynomial) {
        return new BigIntPolynomial(this).mult(bigIntPolynomial);
    }
    
    @Override
    public IntegerPolynomial mult(IntegerPolynomial multRecursive) {
        final int length = this.coeffs.length;
        if (multRecursive.coeffs.length == length) {
            multRecursive = this.multRecursive(multRecursive);
            if (multRecursive.coeffs.length > length) {
                int n = length;
                int[] coeffs;
                while (true) {
                    coeffs = multRecursive.coeffs;
                    if (n >= coeffs.length) {
                        break;
                    }
                    final int n2 = n - length;
                    coeffs[n2] += coeffs[n];
                    ++n;
                }
                multRecursive.coeffs = Arrays.copyOf(coeffs, length);
            }
            return multRecursive;
        }
        throw new IllegalArgumentException("Number of coefficients must be the same");
    }
    
    @Override
    public IntegerPolynomial mult(IntegerPolynomial mult, final int n) {
        mult = this.mult(mult);
        mult.mod(n);
        return mult;
    }
    
    public void mult(final int n) {
        int n2 = 0;
        while (true) {
            final int[] coeffs = this.coeffs;
            if (n2 >= coeffs.length) {
                break;
            }
            coeffs[n2] *= n;
            ++n2;
        }
    }
    
    public void mult3(final int n) {
        int n2 = 0;
        while (true) {
            final int[] coeffs = this.coeffs;
            if (n2 >= coeffs.length) {
                break;
            }
            coeffs[n2] *= 3;
            coeffs[n2] %= n;
            ++n2;
        }
    }
    
    public ModularResultant resultant(final int n) {
        final int[] coeffs = this.coeffs;
        final int[] copy = Arrays.copyOf(coeffs, coeffs.length + 1);
        final IntegerPolynomial integerPolynomial = new IntegerPolynomial(copy);
        final int length = copy.length;
        IntegerPolynomial integerPolynomial2 = new IntegerPolynomial(length);
        final int[] coeffs2 = integerPolynomial2.coeffs;
        coeffs2[0] = -1;
        final int n2 = length - 1;
        coeffs2[n2] = 1;
        IntegerPolynomial integerPolynomial3 = new IntegerPolynomial(integerPolynomial.coeffs);
        IntegerPolynomial integerPolynomial4 = new IntegerPolynomial(length);
        IntegerPolynomial integerPolynomial5 = new IntegerPolynomial(length);
        integerPolynomial5.coeffs[0] = 1;
        final int degree = integerPolynomial3.degree();
        int n3 = 1;
        int degree2 = n2;
        int n4 = n2;
        int n5 = degree;
        IntegerPolynomial integerPolynomial7;
        while (true) {
            final IntegerPolynomial integerPolynomial6 = integerPolynomial4;
            integerPolynomial7 = integerPolynomial3;
            if (n5 <= 0) {
                break;
            }
            final int n6 = Util.invert(integerPolynomial7.coeffs[n5], n) * integerPolynomial2.coeffs[degree2] % n;
            final int n7 = degree2 - n5;
            integerPolynomial2.multShiftSub(integerPolynomial7, n6, n7, n);
            integerPolynomial6.multShiftSub(integerPolynomial5, n6, n7, n);
            final int n8 = degree2 = integerPolynomial2.degree();
            integerPolynomial3 = integerPolynomial7;
            integerPolynomial4 = integerPolynomial6;
            if (n8 >= n5) {
                continue;
            }
            final int n9 = n3 = n3 * Util.pow(integerPolynomial7.coeffs[n5], n4 - n8, n) % n;
            if (n4 % 2 == 1) {
                n3 = n9;
                if (n5 % 2 == 1) {
                    n3 = -n9 % n;
                }
            }
            n4 = n5;
            n5 = n8;
            degree2 = n4;
            integerPolynomial4 = integerPolynomial5;
            integerPolynomial5 = integerPolynomial6;
            integerPolynomial3 = integerPolynomial2;
            integerPolynomial2 = integerPolynomial7;
        }
        final int n10 = n3 * Util.pow(integerPolynomial7.coeffs[0], degree2, n) % n;
        integerPolynomial5.mult(Util.invert(integerPolynomial7.coeffs[0], n));
        integerPolynomial5.mod(n);
        integerPolynomial5.mult(n10);
        integerPolynomial5.mod(n);
        final int[] coeffs3 = integerPolynomial5.coeffs;
        integerPolynomial5.coeffs = Arrays.copyOf(coeffs3, coeffs3.length - 1);
        return new ModularResultant(new BigIntPolynomial(integerPolynomial5), BigInteger.valueOf(n10), BigInteger.valueOf(n));
    }
    
    public Resultant resultant() {
        final int length = this.coeffs.length;
        final LinkedList<ModularResultant> list = new LinkedList<ModularResultant>();
        BigInteger bigint_ONE = Constants.BIGINT_ONE;
        BigInteger bigint_ONE2 = Constants.BIGINT_ONE;
        final PrimeGenerator primeGenerator = new PrimeGenerator();
        int n = 1;
        BigInteger multiply;
        BigInteger bigInteger;
        while (true) {
            final BigInteger nextPrime = primeGenerator.nextPrime();
            final ModularResultant resultant = this.resultant(nextPrime.intValue());
            list.add(resultant);
            multiply = bigint_ONE.multiply(nextPrime);
            final BigIntEuclidean calculate = BigIntEuclidean.calculate(nextPrime, bigint_ONE);
            final BigInteger mod = bigint_ONE2.multiply(calculate.x.multiply(nextPrime)).add(resultant.res.multiply(calculate.y.multiply(bigint_ONE))).mod(multiply);
            final BigInteger divide = multiply.divide(BigInteger.valueOf(2L));
            final BigInteger negate = divide.negate();
            if (mod.compareTo(divide) > 0) {
                bigInteger = mod.subtract(multiply);
            }
            else {
                bigInteger = mod;
                if (mod.compareTo(negate) < 0) {
                    bigInteger = mod.add(multiply);
                }
            }
            if (bigInteger.equals(bigint_ONE2)) {
                if (++n >= 3) {
                    break;
                }
            }
            else {
                n = 1;
            }
            bigint_ONE2 = bigInteger;
            bigint_ONE = multiply;
        }
        while (list.size() > 1) {
            list.addLast(ModularResultant.combineRho(list.removeFirst(), list.removeFirst()));
        }
        final BigIntPolynomial rho = list.getFirst().rho;
        final BigInteger divide2 = multiply.divide(BigInteger.valueOf(2L));
        final BigInteger negate2 = divide2.negate();
        BigInteger subtract = bigInteger;
        if (bigInteger.compareTo(divide2) > 0) {
            subtract = bigInteger.subtract(multiply);
        }
        BigInteger add = subtract;
        if (subtract.compareTo(negate2) < 0) {
            add = subtract.add(multiply);
        }
        for (int i = 0; i < length; ++i) {
            final BigInteger bigInteger2 = rho.coeffs[i];
            if (bigInteger2.compareTo(divide2) > 0) {
                rho.coeffs[i] = bigInteger2.subtract(multiply);
            }
            if (bigInteger2.compareTo(negate2) < 0) {
                rho.coeffs[i] = bigInteger2.add(multiply);
            }
        }
        return new Resultant(rho, add);
    }
    
    public Resultant resultantMultiThread() {
        final int length = this.coeffs.length;
        final BigInteger multiply = this.squareSum().pow((length + 1) / 2).multiply(BigInteger.valueOf(2L).pow((this.degree() + 1) / 2)).multiply(BigInteger.valueOf(2L));
        BigInteger bigInteger = BigInteger.valueOf(10000L);
        BigInteger bigInteger2 = Constants.BIGINT_ONE;
        final LinkedBlockingQueue<Future<Object>> linkedBlockingQueue = new LinkedBlockingQueue<Future<Object>>();
        final Iterator<BigInteger> iterator = (Iterator<BigInteger>)IntegerPolynomial.BIGINT_PRIMES.iterator();
        final ExecutorService fixedThreadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        ModularResultant modularResultant;
        while (true) {
            final int compareTo = bigInteger2.compareTo(multiply);
            modularResultant = null;
            if (compareTo >= 0) {
                break;
            }
            if (iterator.hasNext()) {
                bigInteger = iterator.next();
            }
            else {
                bigInteger = bigInteger.nextProbablePrime();
            }
            linkedBlockingQueue.add(fixedThreadPool.submit((Callable<Object>)new ModResultantTask(bigInteger.intValue())));
            bigInteger2 = bigInteger2.multiply(bigInteger);
        }
        ModularResultant modularResultant2;
        while (true) {
            modularResultant2 = modularResultant;
            if (!linkedBlockingQueue.isEmpty()) {
                try {
                    final Future<Object> future = linkedBlockingQueue.take();
                    final Future<Object> future2 = linkedBlockingQueue.poll();
                    if (future2 != null) {
                        linkedBlockingQueue.add(fixedThreadPool.submit((Callable<Object>)new CombineTask((ModularResultant)future.get(), (ModularResultant)future2.get())));
                        continue;
                    }
                    modularResultant2 = future.get();
                }
                catch (Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
                break;
            }
            break;
        }
        fixedThreadPool.shutdown();
        final BigInteger res = modularResultant2.res;
        final BigIntPolynomial rho = modularResultant2.rho;
        final BigInteger divide = bigInteger2.divide(BigInteger.valueOf(2L));
        final BigInteger negate = divide.negate();
        BigInteger subtract = res;
        if (res.compareTo(divide) > 0) {
            subtract = res.subtract(bigInteger2);
        }
        BigInteger add = subtract;
        if (subtract.compareTo(negate) < 0) {
            add = subtract.add(bigInteger2);
        }
        for (int i = 0; i < length; ++i) {
            final BigInteger bigInteger3 = rho.coeffs[i];
            if (bigInteger3.compareTo(divide) > 0) {
                rho.coeffs[i] = bigInteger3.subtract(bigInteger2);
            }
            if (bigInteger3.compareTo(negate) < 0) {
                rho.coeffs[i] = bigInteger3.add(bigInteger2);
            }
        }
        return new Resultant(rho, add);
    }
    
    public void rotate1() {
        final int[] coeffs = this.coeffs;
        final int n = coeffs[coeffs.length - 1];
        int n2;
        for (int i = coeffs.length - 1; i > 0; i = n2) {
            final int[] coeffs2 = this.coeffs;
            n2 = i - 1;
            coeffs2[i] = coeffs2[n2];
        }
        this.coeffs[0] = n;
    }
    
    void shiftGap(int n) {
        this.modCenter(n);
        final int[] clone = Arrays.clone(this.coeffs);
        this.sort(clone);
        int i = 0;
        int n2 = 0;
        int n3 = 0;
        while (i < clone.length - 1) {
            final int n4 = i + 1;
            final int n5 = clone[n4] - clone[i];
            int n6;
            if (n5 > (n6 = n2)) {
                n3 = clone[i];
                n6 = n5;
            }
            i = n4;
            n2 = n6;
        }
        final int n7 = clone[0];
        final int n8 = clone[clone.length - 1];
        if (n - n8 + n7 > n2) {
            n = (n8 + n7) / 2;
        }
        else {
            n = n3 + n2 / 2 + n / 2;
        }
        this.sub(n);
    }
    
    void sub(final int n) {
        int n2 = 0;
        while (true) {
            final int[] coeffs = this.coeffs;
            if (n2 >= coeffs.length) {
                break;
            }
            coeffs[n2] -= n;
            ++n2;
        }
    }
    
    public void sub(final IntegerPolynomial integerPolynomial) {
        final int[] coeffs = integerPolynomial.coeffs;
        final int length = coeffs.length;
        final int[] coeffs2 = this.coeffs;
        if (length > coeffs2.length) {
            this.coeffs = Arrays.copyOf(coeffs2, coeffs.length);
        }
        int n = 0;
        while (true) {
            final int[] coeffs3 = integerPolynomial.coeffs;
            if (n >= coeffs3.length) {
                break;
            }
            final int[] coeffs4 = this.coeffs;
            coeffs4[n] -= coeffs3[n];
            ++n;
        }
    }
    
    public void sub(final IntegerPolynomial integerPolynomial, final int n) {
        this.sub(integerPolynomial);
        this.mod(n);
    }
    
    public int sumCoeffs() {
        int n = 0;
        int n2 = 0;
        while (true) {
            final int[] coeffs = this.coeffs;
            if (n >= coeffs.length) {
                break;
            }
            n2 += coeffs[n];
            ++n;
        }
        return n2;
    }
    
    public byte[] toBinary(final int n) {
        return ArrayEncoder.encodeModQ(this.coeffs, n);
    }
    
    public byte[] toBinary3Sves() {
        return ArrayEncoder.encodeMod3Sves(this.coeffs);
    }
    
    public byte[] toBinary3Tight() {
        BigInteger bigInteger = Constants.BIGINT_ZERO;
        for (int i = this.coeffs.length - 1; i >= 0; --i) {
            bigInteger = bigInteger.multiply(BigInteger.valueOf(3L)).add(BigInteger.valueOf(this.coeffs[i] + 1));
        }
        final int n = (BigInteger.valueOf(3L).pow(this.coeffs.length).bitLength() + 7) / 8;
        final byte[] byteArray = bigInteger.toByteArray();
        if (byteArray.length < n) {
            final byte[] array = new byte[n];
            System.arraycopy(byteArray, 0, array, n - byteArray.length, byteArray.length);
            return array;
        }
        byte[] copyOfRange = byteArray;
        if (byteArray.length > n) {
            copyOfRange = Arrays.copyOfRange(byteArray, 1, byteArray.length);
        }
        return copyOfRange;
    }
    
    @Override
    public IntegerPolynomial toIntegerPolynomial() {
        return (IntegerPolynomial)this.clone();
    }
    
    private class CombineTask implements Callable<ModularResultant>
    {
        private ModularResultant modRes1;
        private ModularResultant modRes2;
        
        private CombineTask(final ModularResultant modRes1, final ModularResultant modRes2) {
            this.modRes1 = modRes1;
            this.modRes2 = modRes2;
        }
        
        @Override
        public ModularResultant call() {
            return ModularResultant.combineRho(this.modRes1, this.modRes2);
        }
    }
    
    private class ModResultantTask implements Callable<ModularResultant>
    {
        private int modulus;
        
        private ModResultantTask(final int modulus) {
            this.modulus = modulus;
        }
        
        @Override
        public ModularResultant call() {
            return IntegerPolynomial.this.resultant(this.modulus);
        }
    }
    
    private class PrimeGenerator
    {
        private int index;
        private BigInteger prime;
        
        private PrimeGenerator() {
            this.index = 0;
        }
        
        public BigInteger nextPrime() {
            BigInteger nextProbablePrime;
            if (this.index < IntegerPolynomial.BIGINT_PRIMES.size()) {
                nextProbablePrime = IntegerPolynomial.BIGINT_PRIMES.get(this.index++);
            }
            else {
                nextProbablePrime = this.prime.nextProbablePrime();
            }
            return this.prime = nextProbablePrime;
        }
    }
}
