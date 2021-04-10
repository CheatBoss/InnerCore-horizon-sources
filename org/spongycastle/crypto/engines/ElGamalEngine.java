package org.spongycastle.crypto.engines;

import java.math.*;
import java.security.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.crypto.*;
import org.spongycastle.util.*;
import java.util.*;

public class ElGamalEngine implements AsymmetricBlockCipher
{
    private static final BigInteger ONE;
    private static final BigInteger TWO;
    private static final BigInteger ZERO;
    private int bitSize;
    private boolean forEncryption;
    private ElGamalKeyParameters key;
    private SecureRandom random;
    
    static {
        ZERO = BigInteger.valueOf(0L);
        ONE = BigInteger.valueOf(1L);
        TWO = BigInteger.valueOf(2L);
    }
    
    @Override
    public int getInputBlockSize() {
        if (this.forEncryption) {
            return (this.bitSize - 1) / 8;
        }
        return (this.bitSize + 7) / 8 * 2;
    }
    
    @Override
    public int getOutputBlockSize() {
        if (this.forEncryption) {
            return (this.bitSize + 7) / 8 * 2;
        }
        return (this.bitSize - 1) / 8;
    }
    
    @Override
    public void init(final boolean forEncryption, final CipherParameters cipherParameters) {
        SecureRandom random;
        if (cipherParameters instanceof ParametersWithRandom) {
            final ParametersWithRandom parametersWithRandom = (ParametersWithRandom)cipherParameters;
            this.key = (ElGamalKeyParameters)parametersWithRandom.getParameters();
            random = parametersWithRandom.getRandom();
        }
        else {
            this.key = (ElGamalKeyParameters)cipherParameters;
            random = new SecureRandom();
        }
        this.random = random;
        this.forEncryption = forEncryption;
        this.bitSize = this.key.getParameters().getP().bitLength();
        if (forEncryption) {
            if (this.key instanceof ElGamalPublicKeyParameters) {
                return;
            }
            throw new IllegalArgumentException("ElGamalPublicKeyParameters are required for encryption.");
        }
        else {
            if (this.key instanceof ElGamalPrivateKeyParameters) {
                return;
            }
            throw new IllegalArgumentException("ElGamalPrivateKeyParameters are required for decryption.");
        }
    }
    
    @Override
    public byte[] processBlock(byte[] byteArray, int n, int length) {
        if (this.key == null) {
            throw new IllegalStateException("ElGamal engine not initialised");
        }
        int inputBlockSize;
        if (this.forEncryption) {
            inputBlockSize = (this.bitSize - 1 + 7) / 8;
        }
        else {
            inputBlockSize = this.getInputBlockSize();
        }
        if (length > inputBlockSize) {
            throw new DataLengthException("input too large for ElGamal cipher.\n");
        }
        final BigInteger p3 = this.key.getParameters().getP();
        if (this.key instanceof ElGamalPrivateKeyParameters) {
            length /= 2;
            final byte[] array = new byte[length];
            final byte[] array2 = new byte[length];
            System.arraycopy(byteArray, n, array, 0, length);
            System.arraycopy(byteArray, n + length, array2, 0, length);
            return BigIntegers.asUnsignedByteArray(new BigInteger(1, array).modPow(p3.subtract(ElGamalEngine.ONE).subtract(((ElGamalPrivateKeyParameters)this.key).getX()), p3).multiply(new BigInteger(1, array2)).mod(p3));
        }
        byte[] array3 = null;
        Label_0194: {
            if (n == 0) {
                array3 = byteArray;
                if (length == byteArray.length) {
                    break Label_0194;
                }
            }
            array3 = new byte[length];
            System.arraycopy(byteArray, n, array3, 0, length);
        }
        final BigInteger bigInteger = new BigInteger(1, array3);
        if (bigInteger.compareTo(p3) >= 0) {
            throw new DataLengthException("input too large for ElGamal cipher.\n");
        }
        final ElGamalPublicKeyParameters elGamalPublicKeyParameters = (ElGamalPublicKeyParameters)this.key;
        BigInteger bigInteger2;
        for (n = p3.bitLength(), bigInteger2 = new BigInteger(n, this.random); bigInteger2.equals(ElGamalEngine.ZERO) || bigInteger2.compareTo(p3.subtract(ElGamalEngine.TWO)) > 0; bigInteger2 = new BigInteger(n, this.random)) {}
        final BigInteger modPow = this.key.getParameters().getG().modPow(bigInteger2, p3);
        final BigInteger mod = bigInteger.multiply(elGamalPublicKeyParameters.getY().modPow(bigInteger2, p3)).mod(p3);
        byteArray = modPow.toByteArray();
        final byte[] byteArray2 = mod.toByteArray();
        n = this.getOutputBlockSize();
        final byte[] array4 = new byte[n];
        length = byteArray.length;
        final int n2 = n / 2;
        if (length > n2) {
            System.arraycopy(byteArray, 1, array4, n2 - (byteArray.length - 1), byteArray.length - 1);
        }
        else {
            System.arraycopy(byteArray, 0, array4, n2 - byteArray.length, byteArray.length);
        }
        if (byteArray2.length > n2) {
            System.arraycopy(byteArray2, 1, array4, n - (byteArray2.length - 1), byteArray2.length - 1);
            return array4;
        }
        System.arraycopy(byteArray2, 0, array4, n - byteArray2.length, byteArray2.length);
        return array4;
    }
}
