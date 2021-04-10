package org.spongycastle.crypto.modes;

import org.spongycastle.crypto.modes.gcm.*;
import org.spongycastle.util.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.crypto.*;

public class GCMBlockCipher implements AEADBlockCipher
{
    private static final int BLOCK_SIZE = 16;
    private byte[] H;
    private byte[] J0;
    private byte[] S;
    private byte[] S_at;
    private byte[] S_atPre;
    private byte[] atBlock;
    private int atBlockPos;
    private long atLength;
    private long atLengthPre;
    private int blocksRemaining;
    private byte[] bufBlock;
    private int bufOff;
    private BlockCipher cipher;
    private byte[] counter;
    private GCMExponentiator exp;
    private boolean forEncryption;
    private byte[] initialAssociatedText;
    private boolean initialised;
    private byte[] lastKey;
    private byte[] macBlock;
    private int macSize;
    private GCMMultiplier multiplier;
    private byte[] nonce;
    private long totalLength;
    
    public GCMBlockCipher(final BlockCipher blockCipher) {
        this(blockCipher, null);
    }
    
    public GCMBlockCipher(final BlockCipher cipher, final GCMMultiplier gcmMultiplier) {
        if (cipher.getBlockSize() == 16) {
            GCMMultiplier multiplier;
            if ((multiplier = gcmMultiplier) == null) {
                multiplier = new Tables8kGCMMultiplier();
            }
            this.cipher = cipher;
            this.multiplier = multiplier;
            return;
        }
        throw new IllegalArgumentException("cipher required with a block size of 16.");
    }
    
    private void checkStatus() {
        if (this.initialised) {
            return;
        }
        if (this.forEncryption) {
            throw new IllegalStateException("GCM cipher cannot be reused for encryption");
        }
        throw new IllegalStateException("GCM cipher needs to be initialised");
    }
    
    private void gCTRBlock(byte[] array, byte[] s, final int n) {
        final byte[] nextCounterBlock = this.getNextCounterBlock();
        GCMUtil.xor(nextCounterBlock, array);
        System.arraycopy(nextCounterBlock, 0, s, n, 16);
        s = this.S;
        if (this.forEncryption) {
            array = nextCounterBlock;
        }
        this.gHASHBlock(s, array);
        this.totalLength += 16L;
    }
    
    private void gCTRPartial(byte[] array, final int n, final int n2, byte[] s, final int n3) {
        final byte[] nextCounterBlock = this.getNextCounterBlock();
        GCMUtil.xor(nextCounterBlock, array, n, n2);
        System.arraycopy(nextCounterBlock, 0, s, n3, n2);
        s = this.S;
        if (this.forEncryption) {
            array = nextCounterBlock;
        }
        this.gHASHPartial(s, array, 0, n2);
        this.totalLength += n2;
    }
    
    private void gHASH(final byte[] array, final byte[] array2, final int n) {
        for (int i = 0; i < n; i += 16) {
            this.gHASHPartial(array, array2, i, Math.min(n - i, 16));
        }
    }
    
    private void gHASHBlock(final byte[] array, final byte[] array2) {
        GCMUtil.xor(array, array2);
        this.multiplier.multiplyH(array);
    }
    
    private void gHASHPartial(final byte[] array, final byte[] array2, final int n, final int n2) {
        GCMUtil.xor(array, array2, n, n2);
        this.multiplier.multiplyH(array);
    }
    
    private byte[] getNextCounterBlock() {
        final int blocksRemaining = this.blocksRemaining;
        if (blocksRemaining != 0) {
            this.blocksRemaining = blocksRemaining - 1;
            final byte[] counter = this.counter;
            final int n = (counter[15] & 0xFF) + 1;
            counter[15] = (byte)n;
            final int n2 = (n >>> 8) + (counter[14] & 0xFF);
            counter[14] = (byte)n2;
            final int n3 = (n2 >>> 8) + (counter[13] & 0xFF);
            counter[13] = (byte)n3;
            counter[12] = (byte)((n3 >>> 8) + (counter[12] & 0xFF));
            final byte[] array = new byte[16];
            this.cipher.processBlock(counter, 0, array, 0);
            return array;
        }
        throw new IllegalStateException("Attempt to process too many blocks");
    }
    
    private void initCipher() {
        if (this.atLength > 0L) {
            System.arraycopy(this.S_at, 0, this.S_atPre, 0, 16);
            this.atLengthPre = this.atLength;
        }
        final int atBlockPos = this.atBlockPos;
        if (atBlockPos > 0) {
            this.gHASHPartial(this.S_atPre, this.atBlock, 0, atBlockPos);
            this.atLengthPre += this.atBlockPos;
        }
        if (this.atLengthPre > 0L) {
            System.arraycopy(this.S_atPre, 0, this.S, 0, 16);
        }
    }
    
    private void outputBlock(byte[] bufBlock, final int n) {
        if (bufBlock.length < n + 16) {
            throw new OutputLengthException("Output buffer too short");
        }
        if (this.totalLength == 0L) {
            this.initCipher();
        }
        this.gCTRBlock(this.bufBlock, bufBlock, n);
        if (this.forEncryption) {
            this.bufOff = 0;
            return;
        }
        bufBlock = this.bufBlock;
        System.arraycopy(bufBlock, 16, bufBlock, 0, this.macSize);
        this.bufOff = this.macSize;
    }
    
    private void reset(final boolean b) {
        this.cipher.reset();
        this.S = new byte[16];
        this.S_at = new byte[16];
        this.S_atPre = new byte[16];
        this.atBlock = new byte[16];
        this.atBlockPos = 0;
        this.atLength = 0L;
        this.atLengthPre = 0L;
        this.counter = Arrays.clone(this.J0);
        this.blocksRemaining = -2;
        this.bufOff = 0;
        this.totalLength = 0L;
        final byte[] bufBlock = this.bufBlock;
        if (bufBlock != null) {
            Arrays.fill(bufBlock, (byte)0);
        }
        if (b) {
            this.macBlock = null;
        }
        if (this.forEncryption) {
            this.initialised = false;
            return;
        }
        final byte[] initialAssociatedText = this.initialAssociatedText;
        if (initialAssociatedText != null) {
            this.processAADBytes(initialAssociatedText, 0, initialAssociatedText.length);
        }
    }
    
    @Override
    public int doFinal(byte[] array, int macSize) throws IllegalStateException, InvalidCipherTextException {
        this.checkStatus();
        if (this.totalLength == 0L) {
            this.initCipher();
        }
        int bufOff = this.bufOff;
        if (this.forEncryption) {
            if (array.length < macSize + bufOff + this.macSize) {
                throw new OutputLengthException("Output buffer too short");
            }
        }
        else {
            final int macSize2 = this.macSize;
            if (bufOff < macSize2) {
                throw new InvalidCipherTextException("data too short");
            }
            bufOff -= macSize2;
            if (array.length < macSize + bufOff) {
                throw new OutputLengthException("Output buffer too short");
            }
        }
        if (bufOff > 0) {
            this.gCTRPartial(this.bufBlock, 0, bufOff, array, macSize);
        }
        final long atLength = this.atLength;
        final int atBlockPos = this.atBlockPos;
        final long atLength2 = atLength + atBlockPos;
        this.atLength = atLength2;
        if (atLength2 > this.atLengthPre) {
            if (atBlockPos > 0) {
                this.gHASHPartial(this.S_at, this.atBlock, 0, atBlockPos);
            }
            if (this.atLengthPre > 0L) {
                GCMUtil.xor(this.S_at, this.S_atPre);
            }
            final long totalLength = this.totalLength;
            final byte[] array2 = new byte[16];
            if (this.exp == null) {
                (this.exp = new Tables1kGCMExponentiator()).init(this.H);
            }
            this.exp.exponentiateX(totalLength * 8L + 127L >>> 7, array2);
            GCMUtil.multiply(this.S_at, array2);
            GCMUtil.xor(this.S, this.S_at);
        }
        final byte[] array3 = new byte[16];
        Pack.longToBigEndian(this.atLength * 8L, array3, 0);
        Pack.longToBigEndian(this.totalLength * 8L, array3, 8);
        this.gHASHBlock(this.S, array3);
        final byte[] array4 = new byte[16];
        this.cipher.processBlock(this.J0, 0, array4, 0);
        GCMUtil.xor(array4, this.S);
        final int macSize3 = this.macSize;
        System.arraycopy(array4, 0, this.macBlock = new byte[macSize3], 0, macSize3);
        if (this.forEncryption) {
            System.arraycopy(this.macBlock, 0, array, macSize + this.bufOff, this.macSize);
            bufOff += this.macSize;
        }
        else {
            macSize = this.macSize;
            array = new byte[macSize];
            System.arraycopy(this.bufBlock, bufOff, array, 0, macSize);
            if (!Arrays.constantTimeAreEqual(this.macBlock, array)) {
                throw new InvalidCipherTextException("mac check in GCM failed");
            }
        }
        this.reset(false);
        return bufOff;
    }
    
    @Override
    public String getAlgorithmName() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.cipher.getAlgorithmName());
        sb.append("/GCM");
        return sb.toString();
    }
    
    @Override
    public byte[] getMac() {
        final byte[] macBlock = this.macBlock;
        if (macBlock == null) {
            return new byte[this.macSize];
        }
        return Arrays.clone(macBlock);
    }
    
    @Override
    public int getOutputSize(int n) {
        n += this.bufOff;
        if (this.forEncryption) {
            return n + this.macSize;
        }
        final int macSize = this.macSize;
        if (n < macSize) {
            return 0;
        }
        return n - macSize;
    }
    
    @Override
    public BlockCipher getUnderlyingCipher() {
        return this.cipher;
    }
    
    @Override
    public int getUpdateOutputSize(int macSize) {
        final int n = macSize += this.bufOff;
        if (!this.forEncryption) {
            macSize = this.macSize;
            if (n < macSize) {
                return 0;
            }
            macSize = n - macSize;
        }
        return macSize - macSize % 16;
    }
    
    @Override
    public void init(final boolean forEncryption, final CipherParameters cipherParameters) throws IllegalArgumentException {
        this.forEncryption = forEncryption;
        this.macBlock = null;
        this.initialised = true;
        byte[] nonce;
        KeyParameter key;
        if (cipherParameters instanceof AEADParameters) {
            final AEADParameters aeadParameters = (AEADParameters)cipherParameters;
            nonce = aeadParameters.getNonce();
            this.initialAssociatedText = aeadParameters.getAssociatedText();
            final int macSize = aeadParameters.getMacSize();
            if (macSize < 32 || macSize > 128 || macSize % 8 != 0) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Invalid value for MAC size: ");
                sb.append(macSize);
                throw new IllegalArgumentException(sb.toString());
            }
            this.macSize = macSize / 8;
            key = aeadParameters.getKey();
        }
        else {
            if (!(cipherParameters instanceof ParametersWithIV)) {
                throw new IllegalArgumentException("invalid parameters passed to GCM");
            }
            final ParametersWithIV parametersWithIV = (ParametersWithIV)cipherParameters;
            nonce = parametersWithIV.getIV();
            this.initialAssociatedText = null;
            this.macSize = 16;
            key = (KeyParameter)parametersWithIV.getParameters();
        }
        int n;
        if (forEncryption) {
            n = 16;
        }
        else {
            n = this.macSize + 16;
        }
        this.bufBlock = new byte[n];
        if (nonce != null && nonce.length >= 1) {
            if (forEncryption) {
                final byte[] nonce2 = this.nonce;
                if (nonce2 != null && Arrays.areEqual(nonce2, nonce)) {
                    if (key == null) {
                        throw new IllegalArgumentException("cannot reuse nonce for GCM encryption");
                    }
                    final byte[] lastKey = this.lastKey;
                    if (lastKey != null) {
                        if (Arrays.areEqual(lastKey, key.getKey())) {
                            throw new IllegalArgumentException("cannot reuse nonce for GCM encryption");
                        }
                    }
                }
            }
            this.nonce = nonce;
            if (key != null) {
                this.lastKey = key.getKey();
            }
            if (key != null) {
                this.cipher.init(true, key);
                final byte[] h = new byte[16];
                this.H = h;
                this.cipher.processBlock(h, 0, h, 0);
                this.multiplier.init(this.H);
                this.exp = null;
            }
            else if (this.H == null) {
                throw new IllegalArgumentException("Key must be specified in initial init");
            }
            final byte[] j0 = new byte[16];
            this.J0 = j0;
            final byte[] nonce3 = this.nonce;
            if (nonce3.length == 12) {
                System.arraycopy(nonce3, 0, j0, 0, nonce3.length);
                this.J0[15] = 1;
            }
            else {
                this.gHASH(j0, nonce3, nonce3.length);
                final byte[] array = new byte[16];
                Pack.longToBigEndian(this.nonce.length * 8L, array, 8);
                this.gHASHBlock(this.J0, array);
            }
            this.S = new byte[16];
            this.S_at = new byte[16];
            this.S_atPre = new byte[16];
            this.atBlock = new byte[16];
            this.atBlockPos = 0;
            this.atLength = 0L;
            this.atLengthPre = 0L;
            this.counter = Arrays.clone(this.J0);
            this.blocksRemaining = -2;
            this.bufOff = 0;
            this.totalLength = 0L;
            final byte[] initialAssociatedText = this.initialAssociatedText;
            if (initialAssociatedText != null) {
                this.processAADBytes(initialAssociatedText, 0, initialAssociatedText.length);
            }
            return;
        }
        throw new IllegalArgumentException("IV must be at least 1 byte");
    }
    
    @Override
    public void processAADByte(final byte b) {
        this.checkStatus();
        final byte[] atBlock = this.atBlock;
        final int atBlockPos = this.atBlockPos;
        atBlock[atBlockPos] = b;
        final int atBlockPos2 = atBlockPos + 1;
        this.atBlockPos = atBlockPos2;
        if (atBlockPos2 == 16) {
            this.gHASHBlock(this.S_at, atBlock);
            this.atBlockPos = 0;
            this.atLength += 16L;
        }
    }
    
    @Override
    public void processAADBytes(final byte[] array, final int n, final int n2) {
        this.checkStatus();
        for (int i = 0; i < n2; ++i) {
            final byte[] atBlock = this.atBlock;
            final int atBlockPos = this.atBlockPos;
            atBlock[atBlockPos] = array[n + i];
            if ((this.atBlockPos = atBlockPos + 1) == 16) {
                this.gHASHBlock(this.S_at, atBlock);
                this.atBlockPos = 0;
                this.atLength += 16L;
            }
        }
    }
    
    @Override
    public int processByte(final byte b, final byte[] array, final int n) throws DataLengthException {
        this.checkStatus();
        final byte[] bufBlock = this.bufBlock;
        final int bufOff = this.bufOff;
        bufBlock[bufOff] = b;
        final int bufOff2 = bufOff + 1;
        this.bufOff = bufOff2;
        if (bufOff2 == bufBlock.length) {
            this.outputBlock(array, n);
            return 16;
        }
        return 0;
    }
    
    @Override
    public int processBytes(final byte[] array, final int n, final int n2, final byte[] array2, final int n3) throws DataLengthException {
        this.checkStatus();
        if (array.length >= n + n2) {
            int i = 0;
            int n4 = 0;
            while (i < n2) {
                final byte[] bufBlock = this.bufBlock;
                final int bufOff = this.bufOff;
                bufBlock[bufOff] = array[n + i];
                final int bufOff2 = bufOff + 1;
                this.bufOff = bufOff2;
                int n5 = n4;
                if (bufOff2 == bufBlock.length) {
                    this.outputBlock(array2, n3 + n4);
                    n5 = n4 + 16;
                }
                ++i;
                n4 = n5;
            }
            return n4;
        }
        throw new DataLengthException("Input buffer too short");
    }
    
    @Override
    public void reset() {
        this.reset(true);
    }
}
