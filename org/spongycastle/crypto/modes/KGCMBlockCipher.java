package org.spongycastle.crypto.modes;

import java.math.*;
import org.spongycastle.util.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.crypto.*;
import java.io.*;

public class KGCMBlockCipher implements AEADBlockCipher
{
    private static final int BITS_IN_BYTE = 8;
    private static final BigInteger MASK_1_128;
    private static final BigInteger MASK_1_256;
    private static final BigInteger MASK_1_512;
    private static final BigInteger MASK_2_128;
    private static final BigInteger MASK_2_256;
    private static final BigInteger MASK_2_512;
    private static final int MIN_MAC_BITS = 64;
    private static final BigInteger ONE;
    private static final BigInteger POLYRED_128;
    private static final BigInteger POLYRED_256;
    private static final BigInteger POLYRED_512;
    private static final BigInteger ZERO;
    private byte[] H;
    private ExposedByteArrayOutputStream associatedText;
    private byte[] b;
    private BufferedBlockCipher ctrEngine;
    private ExposedByteArrayOutputStream data;
    private BlockCipher engine;
    private boolean forEncryption;
    private byte[] initialAssociatedText;
    private byte[] iv;
    private int lambda_c;
    private int lambda_o;
    private byte[] macBlock;
    private int macSize;
    private byte[] temp;
    
    static {
        ZERO = BigInteger.valueOf(0L);
        ONE = BigInteger.valueOf(1L);
        MASK_1_128 = new BigInteger("340282366920938463463374607431768211456", 10);
        MASK_2_128 = new BigInteger("340282366920938463463374607431768211455", 10);
        POLYRED_128 = new BigInteger("135", 10);
        MASK_1_256 = new BigInteger("115792089237316195423570985008687907853269984665640564039457584007913129639936", 10);
        MASK_2_256 = new BigInteger("115792089237316195423570985008687907853269984665640564039457584007913129639935", 10);
        POLYRED_256 = new BigInteger("1061", 10);
        MASK_1_512 = new BigInteger("13407807929942597099574024998205846127479365820592393377723561443721764030073546976801874298166903427690031858186486050853753882811946569946433649006084096", 10);
        MASK_2_512 = new BigInteger("13407807929942597099574024998205846127479365820592393377723561443721764030073546976801874298166903427690031858186486050853753882811946569946433649006084095", 10);
        POLYRED_512 = new BigInteger("293", 10);
    }
    
    public KGCMBlockCipher(final BlockCipher engine) {
        this.associatedText = new ExposedByteArrayOutputStream();
        this.data = new ExposedByteArrayOutputStream();
        this.engine = engine;
        this.ctrEngine = new BufferedBlockCipher(new KCTRBlockCipher(this.engine));
        this.macSize = 0;
        this.initialAssociatedText = new byte[this.engine.getBlockSize()];
        this.iv = new byte[this.engine.getBlockSize()];
        this.H = new byte[this.engine.getBlockSize()];
        this.b = new byte[this.engine.getBlockSize()];
        this.temp = new byte[this.engine.getBlockSize()];
        this.lambda_c = 0;
        this.lambda_o = 0;
        this.macBlock = null;
    }
    
    private void calculateMac(byte[] b, int i, int j) {
        this.macBlock = new byte[this.engine.getBlockSize()];
        while (j > 0) {
            for (int k = 0; k < this.engine.getBlockSize(); ++k) {
                final byte[] b2 = this.b;
                b2[k] ^= b[k + i];
            }
            this.multiplyOverField(this.engine.getBlockSize() * 8, this.b, this.H, this.temp);
            System.arraycopy(this.temp = Arrays.reverse(this.temp), 0, this.b, 0, this.engine.getBlockSize());
            j -= this.engine.getBlockSize();
            i += this.engine.getBlockSize();
        }
        Arrays.fill(this.temp, (byte)0);
        this.intToBytes(this.lambda_o, this.temp, 0);
        this.intToBytes(this.lambda_c, this.temp, this.engine.getBlockSize() / 2);
        for (i = 0; i < this.engine.getBlockSize(); ++i) {
            b = this.b;
            b[i] ^= this.temp[i];
        }
        this.engine.processBlock(this.b, 0, this.macBlock, 0);
    }
    
    private void intToBytes(final int n, final byte[] array, final int n2) {
        array[n2 + 3] = (byte)(n >> 24);
        array[n2 + 2] = (byte)(n >> 16);
        array[n2 + 1] = (byte)(n >> 8);
        array[n2] = (byte)n;
    }
    
    private void multiplyOverField(final int n, byte[] unsignedByteArray, final byte[] array, final byte[] array2) {
        final byte[] array3 = new byte[this.engine.getBlockSize()];
        final byte[] array4 = new byte[this.engine.getBlockSize()];
        System.arraycopy(unsignedByteArray, 0, array3, 0, this.engine.getBlockSize());
        System.arraycopy(array, 0, array4, 0, this.engine.getBlockSize());
        final byte[] reverse = Arrays.reverse(array3);
        final byte[] reverse2 = Arrays.reverse(array4);
        BigInteger bigInteger = null;
        BigInteger bigInteger2 = null;
        BigInteger bigInteger3 = null;
        Label_0143: {
            if (n != 128) {
                if (n == 256) {
                    bigInteger = KGCMBlockCipher.MASK_1_256;
                    bigInteger2 = KGCMBlockCipher.MASK_2_256;
                    bigInteger3 = KGCMBlockCipher.POLYRED_256;
                    break Label_0143;
                }
                if (n == 512) {
                    bigInteger = KGCMBlockCipher.MASK_1_512;
                    bigInteger2 = KGCMBlockCipher.MASK_2_512;
                    bigInteger3 = KGCMBlockCipher.POLYRED_512;
                    break Label_0143;
                }
            }
            bigInteger = KGCMBlockCipher.MASK_1_128;
            bigInteger2 = KGCMBlockCipher.MASK_2_128;
            bigInteger3 = KGCMBlockCipher.POLYRED_128;
        }
        BigInteger zero = KGCMBlockCipher.ZERO;
        BigInteger bigInteger4 = new BigInteger(1, reverse);
        BigInteger xor;
        for (BigInteger shiftRight = new BigInteger(1, reverse2); !shiftRight.equals(KGCMBlockCipher.ZERO); shiftRight = shiftRight.shiftRight(1), zero = xor) {
            xor = zero;
            if (shiftRight.and(KGCMBlockCipher.ONE).equals(KGCMBlockCipher.ONE)) {
                xor = zero.xor(bigInteger4);
            }
            final BigInteger bigInteger5 = bigInteger4 = bigInteger4.shiftLeft(1);
            if (!bigInteger5.and(bigInteger).equals(KGCMBlockCipher.ZERO)) {
                bigInteger4 = bigInteger5.xor(bigInteger3);
            }
        }
        unsignedByteArray = BigIntegers.asUnsignedByteArray(zero.and(bigInteger2));
        Arrays.fill(array2, (byte)0);
        System.arraycopy(unsignedByteArray, 0, array2, 0, unsignedByteArray.length);
    }
    
    private void processAAD(final byte[] array, int n, int i) {
        this.lambda_o = i * 8;
        final BlockCipher engine = this.engine;
        final byte[] h = this.H;
        engine.processBlock(h, 0, h, 0);
        while (i > 0) {
            for (int j = 0; j < this.engine.getBlockSize(); ++j) {
                final byte[] b = this.b;
                b[j] ^= array[n + j];
            }
            this.multiplyOverField(this.engine.getBlockSize() * 8, this.b, this.H, this.temp);
            System.arraycopy(this.temp = Arrays.reverse(this.temp), 0, this.b, 0, this.engine.getBlockSize());
            i -= this.engine.getBlockSize();
            n += this.engine.getBlockSize();
        }
    }
    
    @Override
    public int doFinal(byte[] array, int macSize) throws IllegalStateException, InvalidCipherTextException {
        final int size = this.data.size();
        if (this.associatedText.size() > 0) {
            this.processAAD(this.associatedText.getBuffer(), 0, this.associatedText.size());
        }
        int n;
        if (this.forEncryption) {
            if (array.length - macSize < this.macSize + size) {
                throw new OutputLengthException("Output buffer too short");
            }
            this.lambda_c = size * 8;
            final int processBytes = this.ctrEngine.processBytes(this.data.getBuffer(), 0, size, array, macSize);
            n = processBytes + this.ctrEngine.doFinal(array, macSize + processBytes);
            this.calculateMac(array, macSize, size);
        }
        else {
            this.lambda_c = (size - this.macSize) * 8;
            this.calculateMac(this.data.getBuffer(), 0, size - this.macSize);
            final int processBytes2 = this.ctrEngine.processBytes(this.data.getBuffer(), 0, size - this.macSize, array, macSize);
            n = processBytes2 + this.ctrEngine.doFinal(array, macSize + processBytes2);
        }
        final byte[] macBlock = this.macBlock;
        if (macBlock == null) {
            throw new IllegalStateException("mac is not calculated");
        }
        if (this.forEncryption) {
            System.arraycopy(macBlock, 0, array, macSize + n, this.macSize);
            this.reset();
            return n + this.macSize;
        }
        array = new byte[this.macSize];
        System.arraycopy(this.data.getBuffer(), n, array, 0, this.macSize);
        macSize = this.macSize;
        final byte[] array2 = new byte[macSize];
        System.arraycopy(this.macBlock, 0, array2, 0, macSize);
        if (Arrays.constantTimeAreEqual(array, array2)) {
            this.reset();
            return n;
        }
        throw new InvalidCipherTextException("mac verification failed");
    }
    
    @Override
    public String getAlgorithmName() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.engine.getAlgorithmName());
        sb.append("/KGCM");
        return sb.toString();
    }
    
    @Override
    public byte[] getMac() {
        final int macSize = this.macSize;
        final byte[] array = new byte[macSize];
        System.arraycopy(this.macBlock, 0, array, 0, macSize);
        return array;
    }
    
    @Override
    public int getOutputSize(final int n) {
        if (this.forEncryption) {
            return n;
        }
        return n + this.macSize;
    }
    
    @Override
    public BlockCipher getUnderlyingCipher() {
        return this.engine;
    }
    
    @Override
    public int getUpdateOutputSize(final int n) {
        return n;
    }
    
    @Override
    public void init(final boolean forEncryption, final CipherParameters cipherParameters) throws IllegalArgumentException {
        this.forEncryption = forEncryption;
        KeyParameter keyParameter;
        if (cipherParameters instanceof AEADParameters) {
            final AEADParameters aeadParameters = (AEADParameters)cipherParameters;
            final byte[] nonce = aeadParameters.getNonce();
            final byte[] iv = this.iv;
            final int length = iv.length;
            final int length2 = nonce.length;
            Arrays.fill(iv, (byte)0);
            System.arraycopy(nonce, 0, this.iv, length - length2, nonce.length);
            this.initialAssociatedText = aeadParameters.getAssociatedText();
            final int macSize = aeadParameters.getMacSize();
            if (macSize < 64 || macSize > this.engine.getBlockSize() * 8 || macSize % 8 != 0) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Invalid value for MAC size: ");
                sb.append(macSize);
                throw new IllegalArgumentException(sb.toString());
            }
            this.macSize = macSize / 8;
            final KeyParameter key = aeadParameters.getKey();
            final byte[] initialAssociatedText = this.initialAssociatedText;
            keyParameter = key;
            if (initialAssociatedText != null) {
                this.processAADBytes(initialAssociatedText, 0, initialAssociatedText.length);
                keyParameter = key;
            }
        }
        else {
            if (!(cipherParameters instanceof ParametersWithIV)) {
                throw new IllegalArgumentException("Invalid parameter passed");
            }
            final ParametersWithIV parametersWithIV = (ParametersWithIV)cipherParameters;
            final byte[] iv2 = parametersWithIV.getIV();
            final byte[] iv3 = this.iv;
            final int length3 = iv3.length;
            final int length4 = iv2.length;
            Arrays.fill(iv3, (byte)0);
            System.arraycopy(iv2, 0, this.iv, length3 - length4, iv2.length);
            this.initialAssociatedText = null;
            this.macSize = this.engine.getBlockSize();
            keyParameter = (KeyParameter)parametersWithIV.getParameters();
        }
        this.macBlock = new byte[this.engine.getBlockSize()];
        this.ctrEngine.init(true, new ParametersWithIV(keyParameter, this.iv));
        this.engine.init(true, keyParameter);
    }
    
    @Override
    public void processAADByte(final byte b) {
        this.associatedText.write(b);
    }
    
    @Override
    public void processAADBytes(final byte[] array, final int n, final int n2) {
        this.associatedText.write(array, n, n2);
    }
    
    @Override
    public int processByte(final byte b, final byte[] array, final int n) throws DataLengthException, IllegalStateException {
        this.data.write(b);
        return 0;
    }
    
    @Override
    public int processBytes(final byte[] array, final int n, final int n2, final byte[] array2, final int n3) throws DataLengthException, IllegalStateException {
        if (array.length >= n + n2) {
            this.data.write(array, n, n2);
            return 0;
        }
        throw new DataLengthException("input buffer too short");
    }
    
    @Override
    public void reset() {
        this.H = new byte[this.engine.getBlockSize()];
        this.b = new byte[this.engine.getBlockSize()];
        this.temp = new byte[this.engine.getBlockSize()];
        this.lambda_c = 0;
        this.lambda_o = 0;
        this.engine.reset();
        this.data.reset();
        this.associatedText.reset();
        final byte[] initialAssociatedText = this.initialAssociatedText;
        if (initialAssociatedText != null) {
            this.processAADBytes(initialAssociatedText, 0, initialAssociatedText.length);
        }
    }
    
    private class ExposedByteArrayOutputStream extends ByteArrayOutputStream
    {
        public ExposedByteArrayOutputStream() {
        }
        
        public byte[] getBuffer() {
            return this.buf;
        }
    }
}
