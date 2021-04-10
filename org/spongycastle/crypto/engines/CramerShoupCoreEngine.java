package org.spongycastle.crypto.engines;

import java.math.*;
import java.security.*;
import org.spongycastle.util.*;
import org.spongycastle.crypto.*;
import org.spongycastle.crypto.params.*;

public class CramerShoupCoreEngine
{
    private static final BigInteger ONE;
    private boolean forEncryption;
    private CramerShoupKeyParameters key;
    private String label;
    private SecureRandom random;
    
    static {
        ONE = BigInteger.valueOf(1L);
    }
    
    public CramerShoupCoreEngine() {
        this.label = null;
    }
    
    private BigInteger generateRandomElement(final BigInteger bigInteger, final SecureRandom secureRandom) {
        final BigInteger one = CramerShoupCoreEngine.ONE;
        return BigIntegers.createRandomInRange(one, bigInteger.subtract(one), secureRandom);
    }
    
    private boolean isValidMessage(final BigInteger bigInteger, final BigInteger bigInteger2) {
        return bigInteger.compareTo(bigInteger2) < 0;
    }
    
    public BigInteger convertInput(final byte[] array, final int n, final int n2) {
        if (n2 > this.getInputBlockSize() + 1) {
            throw new DataLengthException("input too large for Cramer Shoup cipher.");
        }
        if (n2 == this.getInputBlockSize() + 1 && this.forEncryption) {
            throw new DataLengthException("input too large for Cramer Shoup cipher.");
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
        if (bigInteger.compareTo(this.key.getParameters().getP()) < 0) {
            return bigInteger;
        }
        throw new DataLengthException("input too large for Cramer Shoup cipher.");
    }
    
    public byte[] convertOutput(final BigInteger bigInteger) {
        final byte[] byteArray = bigInteger.toByteArray();
        if (!this.forEncryption) {
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
    
    public BigInteger decryptBlock(final CramerShoupCiphertext cramerShoupCiphertext) throws CramerShoupCiphertextException {
        if (this.key.isPrivate() && !this.forEncryption) {
            final CramerShoupKeyParameters key = this.key;
            if (key instanceof CramerShoupPrivateKeyParameters) {
                final CramerShoupPrivateKeyParameters cramerShoupPrivateKeyParameters = (CramerShoupPrivateKeyParameters)key;
                final BigInteger p = cramerShoupPrivateKeyParameters.getParameters().getP();
                final Digest h = cramerShoupPrivateKeyParameters.getParameters().getH();
                final byte[] byteArray = cramerShoupCiphertext.getU1().toByteArray();
                h.update(byteArray, 0, byteArray.length);
                final byte[] byteArray2 = cramerShoupCiphertext.getU2().toByteArray();
                h.update(byteArray2, 0, byteArray2.length);
                final byte[] byteArray3 = cramerShoupCiphertext.getE().toByteArray();
                h.update(byteArray3, 0, byteArray3.length);
                final String label = this.label;
                if (label != null) {
                    final byte[] bytes = label.getBytes();
                    h.update(bytes, 0, bytes.length);
                }
                final byte[] array = new byte[h.getDigestSize()];
                h.doFinal(array, 0);
                final BigInteger bigInteger = new BigInteger(1, array);
                if (cramerShoupCiphertext.v.equals(cramerShoupCiphertext.u1.modPow(cramerShoupPrivateKeyParameters.getX1().add(cramerShoupPrivateKeyParameters.getY1().multiply(bigInteger)), p).multiply(cramerShoupCiphertext.u2.modPow(cramerShoupPrivateKeyParameters.getX2().add(cramerShoupPrivateKeyParameters.getY2().multiply(bigInteger)), p)).mod(p))) {
                    return cramerShoupCiphertext.e.multiply(cramerShoupCiphertext.u1.modPow(cramerShoupPrivateKeyParameters.getZ(), p).modInverse(p)).mod(p);
                }
                throw new CramerShoupCiphertextException("Sorry, that ciphertext is not correct");
            }
        }
        return null;
    }
    
    public CramerShoupCiphertext encryptBlock(BigInteger mod) {
        final boolean private1 = this.key.isPrivate();
        CramerShoupCiphertext cramerShoupCiphertext2;
        final CramerShoupCiphertext cramerShoupCiphertext = cramerShoupCiphertext2 = null;
        if (!private1) {
            cramerShoupCiphertext2 = cramerShoupCiphertext;
            if (this.forEncryption) {
                final CramerShoupKeyParameters key = this.key;
                cramerShoupCiphertext2 = cramerShoupCiphertext;
                if (key instanceof CramerShoupPublicKeyParameters) {
                    final CramerShoupPublicKeyParameters cramerShoupPublicKeyParameters = (CramerShoupPublicKeyParameters)key;
                    final BigInteger p = cramerShoupPublicKeyParameters.getParameters().getP();
                    final BigInteger g1 = cramerShoupPublicKeyParameters.getParameters().getG1();
                    final BigInteger g2 = cramerShoupPublicKeyParameters.getParameters().getG2();
                    final BigInteger h = cramerShoupPublicKeyParameters.getH();
                    if (!this.isValidMessage(mod, p)) {
                        return null;
                    }
                    final BigInteger generateRandomElement = this.generateRandomElement(p, this.random);
                    final BigInteger modPow = g1.modPow(generateRandomElement, p);
                    final BigInteger modPow2 = g2.modPow(generateRandomElement, p);
                    mod = h.modPow(generateRandomElement, p).multiply(mod).mod(p);
                    final Digest h2 = cramerShoupPublicKeyParameters.getParameters().getH();
                    final byte[] byteArray = modPow.toByteArray();
                    h2.update(byteArray, 0, byteArray.length);
                    final byte[] byteArray2 = modPow2.toByteArray();
                    h2.update(byteArray2, 0, byteArray2.length);
                    final byte[] byteArray3 = mod.toByteArray();
                    h2.update(byteArray3, 0, byteArray3.length);
                    final String label = this.label;
                    if (label != null) {
                        final byte[] bytes = label.getBytes();
                        h2.update(bytes, 0, bytes.length);
                    }
                    final byte[] array = new byte[h2.getDigestSize()];
                    h2.doFinal(array, 0);
                    cramerShoupCiphertext2 = new CramerShoupCiphertext(modPow, modPow2, mod, cramerShoupPublicKeyParameters.getC().modPow(generateRandomElement, p).multiply(cramerShoupPublicKeyParameters.getD().modPow(generateRandomElement.multiply(new BigInteger(1, array)), p)).mod(p));
                }
            }
        }
        return cramerShoupCiphertext2;
    }
    
    public int getInputBlockSize() {
        final int bitLength = this.key.getParameters().getP().bitLength();
        if (this.forEncryption) {
            return (bitLength + 7) / 8 - 1;
        }
        return (bitLength + 7) / 8;
    }
    
    public int getOutputBlockSize() {
        final int bitLength = this.key.getParameters().getP().bitLength();
        if (this.forEncryption) {
            return (bitLength + 7) / 8;
        }
        return (bitLength + 7) / 8 - 1;
    }
    
    public void init(final boolean forEncryption, final CipherParameters cipherParameters) {
        SecureRandom random;
        if (cipherParameters instanceof ParametersWithRandom) {
            final ParametersWithRandom parametersWithRandom = (ParametersWithRandom)cipherParameters;
            this.key = (CramerShoupKeyParameters)parametersWithRandom.getParameters();
            random = parametersWithRandom.getRandom();
        }
        else {
            this.key = (CramerShoupKeyParameters)cipherParameters;
            random = null;
        }
        this.random = this.initSecureRandom(forEncryption, random);
        this.forEncryption = forEncryption;
    }
    
    public void init(final boolean b, final CipherParameters cipherParameters, final String label) {
        this.init(b, cipherParameters);
        this.label = label;
    }
    
    protected SecureRandom initSecureRandom(final boolean b, final SecureRandom secureRandom) {
        if (!b) {
            return null;
        }
        if (secureRandom != null) {
            return secureRandom;
        }
        return new SecureRandom();
    }
    
    public static class CramerShoupCiphertextException extends Exception
    {
        private static final long serialVersionUID = -6360977166495345076L;
        
        public CramerShoupCiphertextException(final String s) {
            super(s);
        }
    }
}
