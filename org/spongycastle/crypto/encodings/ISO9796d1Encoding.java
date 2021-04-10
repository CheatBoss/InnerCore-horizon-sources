package org.spongycastle.crypto.encodings;

import java.math.*;
import org.spongycastle.crypto.*;
import org.spongycastle.crypto.params.*;

public class ISO9796d1Encoding implements AsymmetricBlockCipher
{
    private static final BigInteger SIX;
    private static final BigInteger SIXTEEN;
    private static byte[] inverse;
    private static byte[] shadows;
    private int bitSize;
    private AsymmetricBlockCipher engine;
    private boolean forEncryption;
    private BigInteger modulus;
    private int padBits;
    
    static {
        SIXTEEN = BigInteger.valueOf(16L);
        SIX = BigInteger.valueOf(6L);
        ISO9796d1Encoding.shadows = new byte[] { 14, 3, 5, 8, 9, 4, 2, 15, 0, 13, 11, 6, 7, 10, 12, 1 };
        ISO9796d1Encoding.inverse = new byte[] { 8, 15, 6, 1, 5, 2, 11, 12, 3, 4, 13, 10, 14, 9, 0, 7 };
    }
    
    public ISO9796d1Encoding(final AsymmetricBlockCipher engine) {
        this.padBits = 0;
        this.engine = engine;
    }
    
    private static byte[] convertOutputDecryptOnly(final BigInteger bigInteger) {
        final byte[] byteArray = bigInteger.toByteArray();
        if (byteArray[0] == 0) {
            final int n = byteArray.length - 1;
            final byte[] array = new byte[n];
            System.arraycopy(byteArray, 1, array, 0, n);
            return array;
        }
        return byteArray;
    }
    
    private byte[] decodeBlock(byte[] array, int i, int n) throws InvalidCipherTextException {
        array = this.engine.processBlock(array, i, n);
        final int n2 = (this.bitSize + 13) / 16;
        BigInteger subtract = new BigInteger(1, array);
        if (!subtract.mod(ISO9796d1Encoding.SIXTEEN).equals(ISO9796d1Encoding.SIX)) {
            if (!this.modulus.subtract(subtract).mod(ISO9796d1Encoding.SIXTEEN).equals(ISO9796d1Encoding.SIX)) {
                throw new InvalidCipherTextException("resulting integer iS or (modulus - iS) is not congruent to 6 mod 16");
            }
            subtract = this.modulus.subtract(subtract);
        }
        array = convertOutputDecryptOnly(subtract);
        if ((array[array.length - 1] & 0xF) == 0x6) {
            array[array.length - 1] = (byte)((array[array.length - 1] & 0xFF) >>> 4 | ISO9796d1Encoding.inverse[(array[array.length - 2] & 0xFF) >> 4] << 4);
            final byte[] shadows = ISO9796d1Encoding.shadows;
            i = shadows[(array[1] & 0xFF) >>> 4];
            final byte b = (byte)(shadows[array[1] & 0xF] | i << 4);
            final int n3 = 0;
            array[0] = b;
            i = array.length - 1;
            int n4 = 0;
            int n5 = 0;
            n = 1;
            while (i >= array.length - n2 * 2) {
                final byte[] shadows2 = ISO9796d1Encoding.shadows;
                final int n6 = shadows2[array[i] & 0xF] | shadows2[(array[i] & 0xFF) >>> 4] << 4;
                final int n7 = i - 1;
                int n8 = n4;
                if (((array[n7] ^ n6) & 0xFF) != 0x0) {
                    if (n4 != 0) {
                        throw new InvalidCipherTextException("invalid tsums in block");
                    }
                    n = ((array[n7] ^ n6) & 0xFF);
                    n5 = n7;
                    n8 = 1;
                }
                i -= 2;
                n4 = n8;
            }
            array[n5] = 0;
            final int n9 = (array.length - n5) / 2;
            final byte[] array2 = new byte[n9];
            for (i = n3; i < n9; ++i) {
                array2[i] = array[i * 2 + n5 + 1];
            }
            this.padBits = n - 1;
            return array2;
        }
        throw new InvalidCipherTextException("invalid forcing byte in block");
    }
    
    private byte[] encodeBlock(byte[] shadows, int i, int n) throws InvalidCipherTextException {
        final int bitSize = this.bitSize;
        final int n2 = (bitSize + 7) / 8;
        final byte[] array = new byte[n2];
        final int padBits = this.padBits;
        final int n3 = (bitSize + 13) / 16;
        for (int j = 0; j < n3; j += n) {
            if (j > n3 - n) {
                final int n4 = n3 - j;
                System.arraycopy(shadows, i + n - n4, array, n2 - n3, n4);
            }
            else {
                System.arraycopy(shadows, i, array, n2 - (j + n), n);
            }
        }
        byte b;
        for (i = n2 - n3 * 2; i != n2; i += 2) {
            b = array[n2 - n3 + i / 2];
            shadows = ISO9796d1Encoding.shadows;
            array[i] = (byte)(shadows[b & 0xF] | shadows[(b & 0xFF) >>> 4] << 4);
            array[i + 1] = b;
        }
        n = n2 - n * 2;
        final byte b2 = array[n];
        i = 1;
        array[n] = (byte)(b2 ^ padBits + 1);
        n = n2 - 1;
        array[n] = (byte)(array[n] << 4 | 0x6);
        n = 8 - (this.bitSize - 1) % 8;
        if (n != 8) {
            array[0] &= (byte)(255 >>> n);
            array[0] |= (byte)(128 >>> n);
            i = 0;
        }
        else {
            array[0] = 0;
            array[1] |= (byte)128;
        }
        return this.engine.processBlock(array, i, n2 - i);
    }
    
    @Override
    public int getInputBlockSize() {
        int inputBlockSize = this.engine.getInputBlockSize();
        if (this.forEncryption) {
            inputBlockSize = (inputBlockSize + 1) / 2;
        }
        return inputBlockSize;
    }
    
    @Override
    public int getOutputBlockSize() {
        final int outputBlockSize = this.engine.getOutputBlockSize();
        if (this.forEncryption) {
            return outputBlockSize;
        }
        return (outputBlockSize + 1) / 2;
    }
    
    public int getPadBits() {
        return this.padBits;
    }
    
    public AsymmetricBlockCipher getUnderlyingCipher() {
        return this.engine;
    }
    
    @Override
    public void init(final boolean forEncryption, final CipherParameters cipherParameters) {
        RSAKeyParameters rsaKeyParameters;
        if (cipherParameters instanceof ParametersWithRandom) {
            rsaKeyParameters = (RSAKeyParameters)((ParametersWithRandom)cipherParameters).getParameters();
        }
        else {
            rsaKeyParameters = (RSAKeyParameters)cipherParameters;
        }
        this.engine.init(forEncryption, cipherParameters);
        final BigInteger modulus = rsaKeyParameters.getModulus();
        this.modulus = modulus;
        this.bitSize = modulus.bitLength();
        this.forEncryption = forEncryption;
    }
    
    @Override
    public byte[] processBlock(final byte[] array, final int n, final int n2) throws InvalidCipherTextException {
        if (this.forEncryption) {
            return this.encodeBlock(array, n, n2);
        }
        return this.decodeBlock(array, n, n2);
    }
    
    public void setPadBits(final int padBits) {
        if (padBits <= 7) {
            this.padBits = padBits;
            return;
        }
        throw new IllegalArgumentException("padBits > 7");
    }
}
