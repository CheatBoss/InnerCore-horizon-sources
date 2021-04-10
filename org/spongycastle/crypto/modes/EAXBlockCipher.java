package org.spongycastle.crypto.modes;

import org.spongycastle.crypto.macs.*;
import org.spongycastle.util.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.crypto.*;

public class EAXBlockCipher implements AEADBlockCipher
{
    private static final byte cTAG = 2;
    private static final byte hTAG = 1;
    private static final byte nTAG = 0;
    private byte[] associatedTextMac;
    private int blockSize;
    private byte[] bufBlock;
    private int bufOff;
    private SICBlockCipher cipher;
    private boolean cipherInitialized;
    private boolean forEncryption;
    private byte[] initialAssociatedText;
    private Mac mac;
    private byte[] macBlock;
    private int macSize;
    private byte[] nonceMac;
    
    public EAXBlockCipher(final BlockCipher blockCipher) {
        this.blockSize = blockCipher.getBlockSize();
        final CMac mac = new CMac(blockCipher);
        this.mac = mac;
        this.macBlock = new byte[this.blockSize];
        this.associatedTextMac = new byte[mac.getMacSize()];
        this.nonceMac = new byte[this.mac.getMacSize()];
        this.cipher = new SICBlockCipher(blockCipher);
    }
    
    private void calculateMac() {
        final byte[] array = new byte[this.blockSize];
        final Mac mac = this.mac;
        int n = 0;
        mac.doFinal(array, 0);
        while (true) {
            final byte[] macBlock = this.macBlock;
            if (n >= macBlock.length) {
                break;
            }
            macBlock[n] = (byte)(this.nonceMac[n] ^ this.associatedTextMac[n] ^ array[n]);
            ++n;
        }
    }
    
    private void initCipher() {
        if (this.cipherInitialized) {
            return;
        }
        this.cipherInitialized = true;
        this.mac.doFinal(this.associatedTextMac, 0);
        final int blockSize = this.blockSize;
        final byte[] array = new byte[blockSize];
        array[blockSize - 1] = 2;
        this.mac.update(array, 0, blockSize);
    }
    
    private int process(final byte b, byte[] bufBlock, int processBlock) {
        final byte[] bufBlock2 = this.bufBlock;
        final int bufOff = this.bufOff;
        final int bufOff2 = bufOff + 1;
        this.bufOff = bufOff2;
        bufBlock2[bufOff] = b;
        if (bufOff2 != bufBlock2.length) {
            return 0;
        }
        final int length = bufBlock.length;
        final int blockSize = this.blockSize;
        if (length >= processBlock + blockSize) {
            if (this.forEncryption) {
                final int processBlock2 = this.cipher.processBlock(bufBlock2, 0, bufBlock, processBlock);
                this.mac.update(bufBlock, processBlock, this.blockSize);
                processBlock = processBlock2;
            }
            else {
                this.mac.update(bufBlock2, 0, blockSize);
                processBlock = this.cipher.processBlock(this.bufBlock, 0, bufBlock, processBlock);
            }
            this.bufOff = 0;
            if (!this.forEncryption) {
                bufBlock = this.bufBlock;
                System.arraycopy(bufBlock, this.blockSize, bufBlock, 0, this.macSize);
                this.bufOff = this.macSize;
            }
            return processBlock;
        }
        throw new OutputLengthException("Output buffer is too short");
    }
    
    private void reset(final boolean b) {
        this.cipher.reset();
        this.mac.reset();
        this.bufOff = 0;
        Arrays.fill(this.bufBlock, (byte)0);
        if (b) {
            Arrays.fill(this.macBlock, (byte)0);
        }
        final int blockSize = this.blockSize;
        final byte[] array = new byte[blockSize];
        array[blockSize - 1] = 1;
        this.mac.update(array, 0, blockSize);
        this.cipherInitialized = false;
        final byte[] initialAssociatedText = this.initialAssociatedText;
        if (initialAssociatedText != null) {
            this.processAADBytes(initialAssociatedText, 0, initialAssociatedText.length);
        }
    }
    
    private boolean verifyMac(final byte[] array, final int n) {
        boolean b = false;
        int i = 0;
        int n2 = 0;
        while (i < this.macSize) {
            n2 |= (this.macBlock[i] ^ array[n + i]);
            ++i;
        }
        if (n2 == 0) {
            b = true;
        }
        return b;
    }
    
    @Override
    public int doFinal(final byte[] array, final int n) throws IllegalStateException, InvalidCipherTextException {
        this.initCipher();
        final int bufOff = this.bufOff;
        final byte[] bufBlock = this.bufBlock;
        final byte[] array2 = new byte[bufBlock.length];
        this.bufOff = 0;
        if (this.forEncryption) {
            final int length = array.length;
            final int n2 = n + bufOff;
            if (length >= this.macSize + n2) {
                this.cipher.processBlock(bufBlock, 0, array2, 0);
                System.arraycopy(array2, 0, array, n, bufOff);
                this.mac.update(array2, 0, bufOff);
                this.calculateMac();
                System.arraycopy(this.macBlock, 0, array, n2, this.macSize);
                this.reset(false);
                return bufOff + this.macSize;
            }
            throw new OutputLengthException("Output buffer too short");
        }
        else {
            final int macSize = this.macSize;
            if (bufOff < macSize) {
                throw new InvalidCipherTextException("data too short");
            }
            if (array.length < n + bufOff - macSize) {
                throw new OutputLengthException("Output buffer too short");
            }
            if (bufOff > macSize) {
                this.mac.update(bufBlock, 0, bufOff - macSize);
                this.cipher.processBlock(this.bufBlock, 0, array2, 0);
                System.arraycopy(array2, 0, array, n, bufOff - this.macSize);
            }
            this.calculateMac();
            if (this.verifyMac(this.bufBlock, bufOff - this.macSize)) {
                this.reset(false);
                return bufOff - this.macSize;
            }
            throw new InvalidCipherTextException("mac check in EAX failed");
        }
    }
    
    @Override
    public String getAlgorithmName() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.cipher.getUnderlyingCipher().getAlgorithmName());
        sb.append("/EAX");
        return sb.toString();
    }
    
    public int getBlockSize() {
        return this.cipher.getBlockSize();
    }
    
    @Override
    public byte[] getMac() {
        final int macSize = this.macSize;
        final byte[] array = new byte[macSize];
        System.arraycopy(this.macBlock, 0, array, 0, macSize);
        return array;
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
        return this.cipher.getUnderlyingCipher();
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
        return macSize - macSize % this.blockSize;
    }
    
    @Override
    public void init(final boolean forEncryption, final CipherParameters cipherParameters) throws IllegalArgumentException {
        this.forEncryption = forEncryption;
        byte[] array;
        CipherParameters cipherParameters2;
        if (cipherParameters instanceof AEADParameters) {
            final AEADParameters aeadParameters = (AEADParameters)cipherParameters;
            array = aeadParameters.getNonce();
            this.initialAssociatedText = aeadParameters.getAssociatedText();
            this.macSize = aeadParameters.getMacSize() / 8;
            cipherParameters2 = aeadParameters.getKey();
        }
        else {
            if (!(cipherParameters instanceof ParametersWithIV)) {
                throw new IllegalArgumentException("invalid parameters passed to EAX");
            }
            final ParametersWithIV parametersWithIV = (ParametersWithIV)cipherParameters;
            array = parametersWithIV.getIV();
            this.initialAssociatedText = null;
            this.macSize = this.mac.getMacSize() / 2;
            cipherParameters2 = parametersWithIV.getParameters();
        }
        int blockSize;
        if (forEncryption) {
            blockSize = this.blockSize;
        }
        else {
            blockSize = this.blockSize + this.macSize;
        }
        this.bufBlock = new byte[blockSize];
        final byte[] array2 = new byte[this.blockSize];
        this.mac.init(cipherParameters2);
        final int blockSize2 = this.blockSize;
        array2[blockSize2 - 1] = 0;
        this.mac.update(array2, 0, blockSize2);
        this.mac.update(array, 0, array.length);
        this.mac.doFinal(this.nonceMac, 0);
        this.cipher.init(true, new ParametersWithIV(null, this.nonceMac));
        this.reset();
    }
    
    @Override
    public void processAADByte(final byte b) {
        if (!this.cipherInitialized) {
            this.mac.update(b);
            return;
        }
        throw new IllegalStateException("AAD data cannot be added after encryption/decryption processing has begun.");
    }
    
    @Override
    public void processAADBytes(final byte[] array, final int n, final int n2) {
        if (!this.cipherInitialized) {
            this.mac.update(array, n, n2);
            return;
        }
        throw new IllegalStateException("AAD data cannot be added after encryption/decryption processing has begun.");
    }
    
    @Override
    public int processByte(final byte b, final byte[] array, final int n) throws DataLengthException {
        this.initCipher();
        return this.process(b, array, n);
    }
    
    @Override
    public int processBytes(final byte[] array, final int n, final int n2, final byte[] array2, final int n3) throws DataLengthException {
        this.initCipher();
        if (array.length >= n + n2) {
            int i = 0;
            int n4 = 0;
            while (i != n2) {
                n4 += this.process(array[n + i], array2, n3 + n4);
                ++i;
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
