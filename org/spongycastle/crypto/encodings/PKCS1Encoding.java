package org.spongycastle.crypto.encodings;

import org.spongycastle.util.*;
import java.security.*;
import org.spongycastle.crypto.*;
import org.spongycastle.crypto.params.*;

public class PKCS1Encoding implements AsymmetricBlockCipher
{
    private static final int HEADER_LENGTH = 10;
    public static final String NOT_STRICT_LENGTH_ENABLED_PROPERTY = "org.spongycastle.pkcs1.not_strict";
    public static final String STRICT_LENGTH_ENABLED_PROPERTY = "org.spongycastle.pkcs1.strict";
    private byte[] blockBuffer;
    private AsymmetricBlockCipher engine;
    private byte[] fallback;
    private boolean forEncryption;
    private boolean forPrivateKey;
    private int pLen;
    private SecureRandom random;
    private boolean useStrictLength;
    
    public PKCS1Encoding(final AsymmetricBlockCipher engine) {
        this.pLen = -1;
        this.fallback = null;
        this.engine = engine;
        this.useStrictLength = this.useStrict();
    }
    
    public PKCS1Encoding(final AsymmetricBlockCipher engine, final int pLen) {
        this.pLen = -1;
        this.fallback = null;
        this.engine = engine;
        this.useStrictLength = this.useStrict();
        this.pLen = pLen;
    }
    
    public PKCS1Encoding(final AsymmetricBlockCipher engine, final byte[] fallback) {
        this.pLen = -1;
        this.fallback = null;
        this.engine = engine;
        this.useStrictLength = this.useStrict();
        this.fallback = fallback;
        this.pLen = fallback.length;
    }
    
    private static int checkPkcs1Encoding(final byte[] array, int i) {
        final byte b = array[0];
        final int length = array.length;
        final int n = i + 1;
        int n2 = 0x0 | (b ^ 0x2);
        byte b2;
        int n3;
        int n4;
        for (i = 1; i < length - n; ++i) {
            b2 = array[i];
            n3 = (b2 | b2 >> 1);
            n4 = (n3 | n3 >> 2);
            n2 |= ((n4 | n4 >> 4) & 0x1) - 1;
        }
        i = (array[array.length - n] | n2);
        i |= i >> 1;
        i |= i >> 2;
        return ~(((i | i >> 4) & 0x1) - 1);
    }
    
    private byte[] decodeBlock(byte[] blockBuffer, int length, int outputBlockSize) throws InvalidCipherTextException {
        if (this.pLen != -1) {
            return this.decodeBlockOrRandom(blockBuffer, length, outputBlockSize);
        }
        final byte[] processBlock = this.engine.processBlock(blockBuffer, length, outputBlockSize);
        final boolean useStrictLength = this.useStrictLength;
        length = processBlock.length;
        outputBlockSize = this.engine.getOutputBlockSize();
        boolean b = true;
        if (length != outputBlockSize) {
            length = 1;
        }
        else {
            length = 0;
        }
        blockBuffer = processBlock;
        if (processBlock.length < this.getOutputBlockSize()) {
            blockBuffer = this.blockBuffer;
        }
        final byte b2 = blockBuffer[0];
        outputBlockSize = ((this.forPrivateKey ? (b2 != 2) : (b2 != 1)) ? 1 : 0);
        final int n = this.findStart(b2, blockBuffer) + 1;
        if (n >= 10) {
            b = false;
        }
        if (((b ? 1 : 0) | outputBlockSize) != 0x0) {
            Arrays.fill(blockBuffer, (byte)0);
            throw new InvalidCipherTextException("block incorrect");
        }
        if (((useStrictLength ? 1 : 0) & length) == 0x0) {
            length = blockBuffer.length - n;
            final byte[] array = new byte[length];
            System.arraycopy(blockBuffer, n, array, 0, length);
            return array;
        }
        Arrays.fill(blockBuffer, (byte)0);
        throw new InvalidCipherTextException("block incorrect size");
    }
    
    private byte[] decodeBlockOrRandom(byte[] fallback, int n, int checkPkcs1Encoding) throws InvalidCipherTextException {
        if (this.forPrivateKey) {
            byte[] array = this.engine.processBlock(fallback, n, checkPkcs1Encoding);
            if ((fallback = this.fallback) == null) {
                fallback = new byte[this.pLen];
                this.random.nextBytes(fallback);
            }
            final boolean useStrictLength = this.useStrictLength;
            if (array.length != this.engine.getOutputBlockSize()) {
                n = 1;
            }
            else {
                n = 0;
            }
            if (((useStrictLength ? 1 : 0) & n) != 0x0) {
                array = this.blockBuffer;
            }
            checkPkcs1Encoding = checkPkcs1Encoding(array, this.pLen);
            final byte[] array2 = new byte[this.pLen];
            n = 0;
            while (true) {
                final int pLen = this.pLen;
                if (n >= pLen) {
                    break;
                }
                array2[n] = (byte)((array[array.length - pLen + n] & ~checkPkcs1Encoding) | (fallback[n] & checkPkcs1Encoding));
                ++n;
            }
            Arrays.fill(array, (byte)0);
            return array2;
        }
        throw new InvalidCipherTextException("sorry, this method is only for decryption, not for signing");
    }
    
    private byte[] encodeBlock(final byte[] array, final int n, final int n2) throws InvalidCipherTextException {
        if (n2 <= this.getInputBlockSize()) {
            final int inputBlockSize = this.engine.getInputBlockSize();
            final byte[] array2 = new byte[inputBlockSize];
            if (this.forPrivateKey) {
                array2[0] = 1;
                for (int i = 1; i != inputBlockSize - n2 - 1; ++i) {
                    array2[i] = -1;
                }
            }
            else {
                this.random.nextBytes(array2);
                array2[0] = 2;
                for (int j = 1; j != inputBlockSize - n2 - 1; ++j) {
                    while (array2[j] == 0) {
                        array2[j] = (byte)this.random.nextInt();
                    }
                }
            }
            final int n3 = inputBlockSize - n2;
            array2[n3 - 1] = 0;
            System.arraycopy(array, n, array2, n3, n2);
            return this.engine.processBlock(array2, 0, inputBlockSize);
        }
        throw new IllegalArgumentException("input data too large");
    }
    
    private int findStart(final byte b, final byte[] array) throws InvalidCipherTextException {
        int i = 1;
        boolean b2 = false;
        int n = -1;
        while (i != array.length) {
            final byte b3 = array[i];
            if (b3 == 0 & n < 0) {
                n = i;
            }
            b2 |= (b3 != -1 & (b == 1 & n < 0));
            ++i;
        }
        if (b2) {
            return -1;
        }
        return n;
    }
    
    private boolean useStrict() {
        final String s = AccessController.doPrivileged((PrivilegedAction<String>)new PrivilegedAction() {
            @Override
            public Object run() {
                return System.getProperty("org.spongycastle.pkcs1.strict");
            }
        });
        final String s2 = AccessController.doPrivileged((PrivilegedAction<String>)new PrivilegedAction() {
            @Override
            public Object run() {
                return System.getProperty("org.spongycastle.pkcs1.not_strict");
            }
        });
        boolean b = true;
        if (s2 != null) {
            return s2.equals("true") ^ true;
        }
        if (s != null) {
            if (s.equals("true")) {
                return true;
            }
            b = false;
        }
        return b;
    }
    
    @Override
    public int getInputBlockSize() {
        int inputBlockSize = this.engine.getInputBlockSize();
        if (this.forEncryption) {
            inputBlockSize -= 10;
        }
        return inputBlockSize;
    }
    
    @Override
    public int getOutputBlockSize() {
        final int outputBlockSize = this.engine.getOutputBlockSize();
        if (this.forEncryption) {
            return outputBlockSize;
        }
        return outputBlockSize - 10;
    }
    
    public AsymmetricBlockCipher getUnderlyingCipher() {
        return this.engine;
    }
    
    @Override
    public void init(final boolean forEncryption, final CipherParameters cipherParameters) {
        AsymmetricKeyParameter asymmetricKeyParameter;
        if (cipherParameters instanceof ParametersWithRandom) {
            final ParametersWithRandom parametersWithRandom = (ParametersWithRandom)cipherParameters;
            this.random = parametersWithRandom.getRandom();
            asymmetricKeyParameter = (AsymmetricKeyParameter)parametersWithRandom.getParameters();
        }
        else {
            final AsymmetricKeyParameter asymmetricKeyParameter2 = asymmetricKeyParameter = (AsymmetricKeyParameter)cipherParameters;
            if (!asymmetricKeyParameter2.isPrivate()) {
                asymmetricKeyParameter = asymmetricKeyParameter2;
                if (forEncryption) {
                    this.random = new SecureRandom();
                    asymmetricKeyParameter = asymmetricKeyParameter2;
                }
            }
        }
        this.engine.init(forEncryption, cipherParameters);
        this.forPrivateKey = asymmetricKeyParameter.isPrivate();
        this.forEncryption = forEncryption;
        this.blockBuffer = new byte[this.engine.getOutputBlockSize()];
        if (this.pLen <= 0 || this.fallback != null) {
            return;
        }
        if (this.random != null) {
            return;
        }
        throw new IllegalArgumentException("encoder requires random");
    }
    
    @Override
    public byte[] processBlock(final byte[] array, final int n, final int n2) throws InvalidCipherTextException {
        if (this.forEncryption) {
            return this.encodeBlock(array, n, n2);
        }
        return this.decodeBlock(array, n, n2);
    }
}
