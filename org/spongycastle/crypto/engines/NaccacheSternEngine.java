package org.spongycastle.crypto.engines;

import java.math.*;
import java.util.*;
import org.spongycastle.util.*;
import java.io.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.crypto.*;

public class NaccacheSternEngine implements AsymmetricBlockCipher
{
    private static BigInteger ONE;
    private static BigInteger ZERO;
    private boolean debug;
    private boolean forEncryption;
    private NaccacheSternKeyParameters key;
    private Vector[] lookup;
    
    static {
        NaccacheSternEngine.ZERO = BigInteger.valueOf(0L);
        NaccacheSternEngine.ONE = BigInteger.valueOf(1L);
    }
    
    public NaccacheSternEngine() {
        this.lookup = null;
        this.debug = false;
    }
    
    private static BigInteger chineseRemainder(final Vector vector, final Vector vector2) {
        final BigInteger zero = NaccacheSternEngine.ZERO;
        BigInteger bigInteger = NaccacheSternEngine.ONE;
        final int n = 0;
        int n2 = 0;
        BigInteger add;
        int i;
        while (true) {
            add = zero;
            i = n;
            if (n2 >= vector2.size()) {
                break;
            }
            bigInteger = bigInteger.multiply(vector2.elementAt(n2));
            ++n2;
        }
        while (i < vector2.size()) {
            final BigInteger bigInteger2 = vector2.elementAt(i);
            final BigInteger divide = bigInteger.divide(bigInteger2);
            add = add.add(divide.multiply(divide.modInverse(bigInteger2)).multiply(vector.elementAt(i)));
            ++i;
        }
        return add.mod(bigInteger);
    }
    
    public byte[] addCryptedBlocks(final byte[] array, byte[] byteArray) throws InvalidCipherTextException {
        if (this.forEncryption) {
            if (array.length > this.getOutputBlockSize() || byteArray.length > this.getOutputBlockSize()) {
                throw new InvalidCipherTextException("BlockLength too large for simple addition.\n");
            }
        }
        else if (array.length > this.getInputBlockSize() || byteArray.length > this.getInputBlockSize()) {
            throw new InvalidCipherTextException("BlockLength too large for simple addition.\n");
        }
        final BigInteger bigInteger = new BigInteger(1, array);
        final BigInteger bigInteger2 = new BigInteger(1, byteArray);
        final BigInteger mod = bigInteger.multiply(bigInteger2).mod(this.key.getModulus());
        if (this.debug) {
            final PrintStream out = System.out;
            final StringBuilder sb = new StringBuilder();
            sb.append("c(m1) as BigInteger:....... ");
            sb.append(bigInteger);
            out.println(sb.toString());
            final PrintStream out2 = System.out;
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("c(m2) as BigInteger:....... ");
            sb2.append(bigInteger2);
            out2.println(sb2.toString());
            final PrintStream out3 = System.out;
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("c(m1)*c(m2)%n = c(m1+m2)%n: ");
            sb3.append(mod);
            out3.println(sb3.toString());
        }
        byteArray = this.key.getModulus().toByteArray();
        Arrays.fill(byteArray, (byte)0);
        System.arraycopy(mod.toByteArray(), 0, byteArray, byteArray.length - mod.toByteArray().length, mod.toByteArray().length);
        return byteArray;
    }
    
    public byte[] encrypt(final BigInteger bigInteger) {
        final byte[] byteArray = this.key.getModulus().toByteArray();
        Arrays.fill(byteArray, (byte)0);
        final byte[] byteArray2 = this.key.getG().modPow(bigInteger, this.key.getModulus()).toByteArray();
        System.arraycopy(byteArray2, 0, byteArray, byteArray.length - byteArray2.length, byteArray2.length);
        if (this.debug) {
            final PrintStream out = System.out;
            final StringBuilder sb = new StringBuilder();
            sb.append("Encrypted value is:  ");
            sb.append(new BigInteger(byteArray));
            out.println(sb.toString());
        }
        return byteArray;
    }
    
    @Override
    public int getInputBlockSize() {
        if (this.forEncryption) {
            return (this.key.getLowerSigmaBound() + 7) / 8 - 1;
        }
        return this.key.getModulus().toByteArray().length;
    }
    
    @Override
    public int getOutputBlockSize() {
        if (this.forEncryption) {
            return this.key.getModulus().toByteArray().length;
        }
        return (this.key.getLowerSigmaBound() + 7) / 8 - 1;
    }
    
    @Override
    public void init(final boolean forEncryption, final CipherParameters cipherParameters) {
        this.forEncryption = forEncryption;
        CipherParameters parameters = cipherParameters;
        if (cipherParameters instanceof ParametersWithRandom) {
            parameters = ((ParametersWithRandom)cipherParameters).getParameters();
        }
        this.key = (NaccacheSternKeyParameters)parameters;
        if (!this.forEncryption) {
            if (this.debug) {
                System.out.println("Constructing lookup Array");
            }
            final NaccacheSternPrivateKeyParameters naccacheSternPrivateKeyParameters = (NaccacheSternPrivateKeyParameters)this.key;
            final Vector smallPrimes = naccacheSternPrivateKeyParameters.getSmallPrimes();
            this.lookup = new Vector[smallPrimes.size()];
            for (int i = 0; i < smallPrimes.size(); ++i) {
                final BigInteger bigInteger = smallPrimes.elementAt(i);
                final int intValue = bigInteger.intValue();
                (this.lookup[i] = new Vector()).addElement(NaccacheSternEngine.ONE);
                if (this.debug) {
                    final PrintStream out = System.out;
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Constructing lookup ArrayList for ");
                    sb.append(intValue);
                    out.println(sb.toString());
                }
                BigInteger bigInteger2 = NaccacheSternEngine.ZERO;
                for (int j = 1; j < intValue; ++j) {
                    bigInteger2 = bigInteger2.add(naccacheSternPrivateKeyParameters.getPhi_n());
                    this.lookup[i].addElement(naccacheSternPrivateKeyParameters.getG().modPow(bigInteger2.divide(bigInteger), naccacheSternPrivateKeyParameters.getModulus()));
                }
            }
        }
    }
    
    @Override
    public byte[] processBlock(final byte[] array, int i, int j) throws InvalidCipherTextException {
        if (this.key == null) {
            throw new IllegalStateException("NaccacheStern engine not initialised");
        }
        if (j > this.getInputBlockSize() + 1) {
            throw new DataLengthException("input too large for Naccache-Stern cipher.\n");
        }
        if (!this.forEncryption && j < this.getInputBlockSize()) {
            throw new InvalidCipherTextException("BlockLength does not match modulus for Naccache-Stern cipher.\n");
        }
        final int n = 0;
        byte[] array2 = null;
        Label_0075: {
            if (i == 0) {
                array2 = array;
                if (j == array.length) {
                    break Label_0075;
                }
            }
            array2 = new byte[j];
            System.arraycopy(array, i, array2, 0, j);
        }
        final BigInteger bigInteger = new BigInteger(1, array2);
        if (this.debug) {
            final PrintStream out = System.out;
            final StringBuilder sb = new StringBuilder();
            sb.append("input as BigInteger: ");
            sb.append(bigInteger);
            out.println(sb.toString());
        }
        if (this.forEncryption) {
            return this.encrypt(bigInteger);
        }
        final Vector<BigInteger> vector = new Vector<BigInteger>();
        final NaccacheSternPrivateKeyParameters naccacheSternPrivateKeyParameters = (NaccacheSternPrivateKeyParameters)this.key;
        Vector smallPrimes;
        BigInteger modPow;
        Vector[] lookup;
        Vector vector2;
        PrintStream out2;
        StringBuilder sb2;
        StringBuilder sb3;
        PrintStream out3;
        StringBuilder sb4;
        PrintStream out4;
        StringBuilder sb5;
        PrintStream out5;
        StringBuilder sb6;
        for (smallPrimes = naccacheSternPrivateKeyParameters.getSmallPrimes(), i = 0; i < smallPrimes.size(); ++i) {
            modPow = bigInteger.modPow(naccacheSternPrivateKeyParameters.getPhi_n().divide(smallPrimes.elementAt(i)), naccacheSternPrivateKeyParameters.getModulus());
            lookup = this.lookup;
            vector2 = lookup[i];
            if (lookup[i].size() != smallPrimes.elementAt(i).intValue()) {
                if (this.debug) {
                    out2 = System.out;
                    sb2 = new StringBuilder();
                    sb2.append("Prime is ");
                    sb2.append(smallPrimes.elementAt(i));
                    sb2.append(", lookup table has size ");
                    sb2.append(vector2.size());
                    out2.println(sb2.toString());
                }
                sb3 = new StringBuilder();
                sb3.append("Error in lookup Array for ");
                sb3.append(smallPrimes.elementAt(i).intValue());
                sb3.append(": Size mismatch. Expected ArrayList with length ");
                sb3.append(smallPrimes.elementAt(i).intValue());
                sb3.append(" but found ArrayList of length ");
                sb3.append(this.lookup[i].size());
                throw new InvalidCipherTextException(sb3.toString());
            }
            j = vector2.indexOf(modPow);
            if (j == -1) {
                if (this.debug) {
                    out3 = System.out;
                    sb4 = new StringBuilder();
                    sb4.append("Actual prime is ");
                    sb4.append(smallPrimes.elementAt(i));
                    out3.println(sb4.toString());
                    out4 = System.out;
                    sb5 = new StringBuilder();
                    sb5.append("Decrypted value is ");
                    sb5.append(modPow);
                    out4.println(sb5.toString());
                    out5 = System.out;
                    sb6 = new StringBuilder();
                    sb6.append("LookupList for ");
                    sb6.append(smallPrimes.elementAt(i));
                    sb6.append(" with size ");
                    sb6.append(this.lookup[i].size());
                    sb6.append(" is: ");
                    out5.println(sb6.toString());
                    for (j = n; j < this.lookup[i].size(); ++j) {
                        System.out.println(this.lookup[i].elementAt(j));
                    }
                }
                throw new InvalidCipherTextException("Lookup failed");
            }
            vector.addElement(BigInteger.valueOf(j));
        }
        return chineseRemainder(vector, smallPrimes).toByteArray();
    }
    
    public byte[] processData(byte[] array) throws InvalidCipherTextException {
        if (this.debug) {
            System.out.println();
        }
        if (array.length > this.getInputBlockSize()) {
            final int inputBlockSize = this.getInputBlockSize();
            final int outputBlockSize = this.getOutputBlockSize();
            if (this.debug) {
                final PrintStream out = System.out;
                final StringBuilder sb = new StringBuilder();
                sb.append("Input blocksize is:  ");
                sb.append(inputBlockSize);
                sb.append(" bytes");
                out.println(sb.toString());
                final PrintStream out2 = System.out;
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Output blocksize is: ");
                sb2.append(outputBlockSize);
                sb2.append(" bytes");
                out2.println(sb2.toString());
                final PrintStream out3 = System.out;
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("Data has length:.... ");
                sb3.append(array.length);
                sb3.append(" bytes");
                out3.println(sb3.toString());
            }
            final byte[] array2 = new byte[(array.length / inputBlockSize + 1) * outputBlockSize];
            int i = 0;
            int n = 0;
            while (i < array.length) {
                final int n2 = i + inputBlockSize;
                byte[] array3;
                if (n2 < array.length) {
                    array3 = this.processBlock(array, i, inputBlockSize);
                    i = n2;
                }
                else {
                    array3 = this.processBlock(array, i, array.length - i);
                    i += array.length - i;
                }
                if (this.debug) {
                    final PrintStream out4 = System.out;
                    final StringBuilder sb4 = new StringBuilder();
                    sb4.append("new datapos is ");
                    sb4.append(i);
                    out4.println(sb4.toString());
                }
                if (array3 == null) {
                    if (this.debug) {
                        System.out.println("cipher returned null");
                    }
                    throw new InvalidCipherTextException("cipher returned null");
                }
                System.arraycopy(array3, 0, array2, n, array3.length);
                n += array3.length;
            }
            array = new byte[n];
            System.arraycopy(array2, 0, array, 0, n);
            if (this.debug) {
                final PrintStream out5 = System.out;
                final StringBuilder sb5 = new StringBuilder();
                sb5.append("returning ");
                sb5.append(n);
                sb5.append(" bytes");
                out5.println(sb5.toString());
            }
            return array;
        }
        if (this.debug) {
            System.out.println("data size is less then input block size, processing directly");
        }
        return this.processBlock(array, 0, array.length);
    }
    
    public void setDebug(final boolean debug) {
        this.debug = debug;
    }
}
