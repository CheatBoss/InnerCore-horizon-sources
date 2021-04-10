package org.spongycastle.crypto.engines;

import java.math.*;
import org.spongycastle.crypto.*;
import org.spongycastle.crypto.params.*;

class RSACoreEngine
{
    private boolean forEncryption;
    private RSAKeyParameters key;
    
    public BigInteger convertInput(final byte[] array, final int n, final int n2) {
        if (n2 > this.getInputBlockSize() + 1) {
            throw new DataLengthException("input too large for RSA cipher.");
        }
        if (n2 == this.getInputBlockSize() + 1 && !this.forEncryption) {
            throw new DataLengthException("input too large for RSA cipher.");
        }
        byte[] array2 = null;
        Label_0067: {
            if (n == 0) {
                array2 = array;
                if (n2 == array.length) {
                    break Label_0067;
                }
            }
            array2 = new byte[n2];
            System.arraycopy(array, n, array2, 0, n2);
        }
        final BigInteger bigInteger = new BigInteger(1, array2);
        if (bigInteger.compareTo(this.key.getModulus()) < 0) {
            return bigInteger;
        }
        throw new DataLengthException("input too large for RSA cipher.");
    }
    
    public byte[] convertOutput(final BigInteger bigInteger) {
        final byte[] byteArray = bigInteger.toByteArray();
        if (this.forEncryption) {
            if (byteArray[0] != 0 || byteArray.length <= this.getOutputBlockSize()) {
                if (byteArray.length < this.getOutputBlockSize()) {
                    final int outputBlockSize = this.getOutputBlockSize();
                    final byte[] array = new byte[outputBlockSize];
                    System.arraycopy(byteArray, 0, array, outputBlockSize - byteArray.length, byteArray.length);
                    return array;
                }
                return byteArray;
            }
        }
        else if (byteArray[0] != 0) {
            return byteArray;
        }
        final int n = byteArray.length - 1;
        final byte[] array2 = new byte[n];
        System.arraycopy(byteArray, 1, array2, 0, n);
        return array2;
    }
    
    public int getInputBlockSize() {
        final int bitLength = this.key.getModulus().bitLength();
        if (this.forEncryption) {
            return (bitLength + 7) / 8 - 1;
        }
        return (bitLength + 7) / 8;
    }
    
    public int getOutputBlockSize() {
        final int bitLength = this.key.getModulus().bitLength();
        if (this.forEncryption) {
            return (bitLength + 7) / 8;
        }
        return (bitLength + 7) / 8 - 1;
    }
    
    public void init(final boolean forEncryption, final CipherParameters cipherParameters) {
        CipherParameters parameters = cipherParameters;
        if (cipherParameters instanceof ParametersWithRandom) {
            parameters = ((ParametersWithRandom)cipherParameters).getParameters();
        }
        this.key = (RSAKeyParameters)parameters;
        this.forEncryption = forEncryption;
    }
    
    public BigInteger processBlock(BigInteger modPow) {
        final RSAKeyParameters key = this.key;
        if (key instanceof RSAPrivateCrtKeyParameters) {
            final RSAPrivateCrtKeyParameters rsaPrivateCrtKeyParameters = (RSAPrivateCrtKeyParameters)key;
            final BigInteger p = rsaPrivateCrtKeyParameters.getP();
            final BigInteger q = rsaPrivateCrtKeyParameters.getQ();
            final BigInteger dp = rsaPrivateCrtKeyParameters.getDP();
            final BigInteger dq = rsaPrivateCrtKeyParameters.getDQ();
            final BigInteger qInv = rsaPrivateCrtKeyParameters.getQInv();
            final BigInteger modPow2 = modPow.remainder(p).modPow(dp, p);
            modPow = modPow.remainder(q).modPow(dq, q);
            return modPow2.subtract(modPow).multiply(qInv).mod(p).multiply(q).add(modPow);
        }
        return modPow.modPow(key.getExponent(), this.key.getModulus());
    }
}
