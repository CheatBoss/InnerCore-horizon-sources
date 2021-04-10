package org.spongycastle.crypto.modes;

import org.spongycastle.crypto.macs.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.crypto.*;
import org.spongycastle.util.*;
import java.io.*;

public class CCMBlockCipher implements AEADBlockCipher
{
    private ExposedByteArrayOutputStream associatedText;
    private int blockSize;
    private BlockCipher cipher;
    private ExposedByteArrayOutputStream data;
    private boolean forEncryption;
    private byte[] initialAssociatedText;
    private CipherParameters keyParam;
    private byte[] macBlock;
    private int macSize;
    private byte[] nonce;
    
    public CCMBlockCipher(final BlockCipher cipher) {
        this.associatedText = new ExposedByteArrayOutputStream();
        this.data = new ExposedByteArrayOutputStream();
        this.cipher = cipher;
        final int blockSize = cipher.getBlockSize();
        this.blockSize = blockSize;
        this.macBlock = new byte[blockSize];
        if (blockSize == 16) {
            return;
        }
        throw new IllegalArgumentException("cipher required with a block size of 16.");
    }
    
    private int calculateMac(final byte[] array, final int n, final int n2, final byte[] array2) {
        final CBCBlockCipherMac cbcBlockCipherMac = new CBCBlockCipherMac(this.cipher, this.macSize * 8);
        cbcBlockCipherMac.init(this.keyParam);
        final byte[] array3 = new byte[16];
        if (this.hasAssociatedText()) {
            array3[0] |= 0x40;
        }
        final byte b = array3[0];
        final int macSize = cbcBlockCipherMac.getMacSize();
        final int n3 = 2;
        array3[0] = (byte)(b | ((macSize - 2) / 2 & 0x7) << 3);
        final byte b2 = array3[0];
        final byte[] nonce = this.nonce;
        array3[0] = (byte)(b2 | (15 - nonce.length - 1 & 0x7));
        System.arraycopy(nonce, 0, array3, 1, nonce.length);
        for (int i = n2, n4 = 1; i > 0; i >>>= 8, ++n4) {
            array3[16 - n4] = (byte)(i & 0xFF);
        }
        cbcBlockCipherMac.update(array3, 0, 16);
        if (this.hasAssociatedText()) {
            final int associatedTextLength = this.getAssociatedTextLength();
            int n5;
            if (associatedTextLength < 65280) {
                cbcBlockCipherMac.update((byte)(associatedTextLength >> 8));
                cbcBlockCipherMac.update((byte)associatedTextLength);
                n5 = n3;
            }
            else {
                cbcBlockCipherMac.update((byte)(-1));
                cbcBlockCipherMac.update((byte)(-2));
                cbcBlockCipherMac.update((byte)(associatedTextLength >> 24));
                cbcBlockCipherMac.update((byte)(associatedTextLength >> 16));
                cbcBlockCipherMac.update((byte)(associatedTextLength >> 8));
                cbcBlockCipherMac.update((byte)associatedTextLength);
                n5 = 6;
            }
            final byte[] initialAssociatedText = this.initialAssociatedText;
            if (initialAssociatedText != null) {
                cbcBlockCipherMac.update(initialAssociatedText, 0, initialAssociatedText.length);
            }
            if (this.associatedText.size() > 0) {
                cbcBlockCipherMac.update(this.associatedText.getBuffer(), 0, this.associatedText.size());
            }
            int j = (n5 + associatedTextLength) % 16;
            if (j != 0) {
                while (j != 16) {
                    cbcBlockCipherMac.update((byte)0);
                    ++j;
                }
            }
        }
        cbcBlockCipherMac.update(array, n, n2);
        return cbcBlockCipherMac.doFinal(array2, 0);
    }
    
    private int getAssociatedTextLength() {
        final int size = this.associatedText.size();
        final byte[] initialAssociatedText = this.initialAssociatedText;
        int length;
        if (initialAssociatedText == null) {
            length = 0;
        }
        else {
            length = initialAssociatedText.length;
        }
        return size + length;
    }
    
    private boolean hasAssociatedText() {
        return this.getAssociatedTextLength() > 0;
    }
    
    @Override
    public int doFinal(final byte[] array, int processPacket) throws IllegalStateException, InvalidCipherTextException {
        processPacket = this.processPacket(this.data.getBuffer(), 0, this.data.size(), array, processPacket);
        this.reset();
        return processPacket;
    }
    
    @Override
    public String getAlgorithmName() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.cipher.getAlgorithmName());
        sb.append("/CCM");
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
    public int getOutputSize(int n) {
        n += this.data.size();
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
    public int getUpdateOutputSize(final int n) {
        return 0;
    }
    
    @Override
    public void init(final boolean forEncryption, CipherParameters keyParam) throws IllegalArgumentException {
        this.forEncryption = forEncryption;
        if (keyParam instanceof AEADParameters) {
            final AEADParameters aeadParameters = (AEADParameters)keyParam;
            this.nonce = aeadParameters.getNonce();
            this.initialAssociatedText = aeadParameters.getAssociatedText();
            this.macSize = aeadParameters.getMacSize() / 8;
            keyParam = aeadParameters.getKey();
        }
        else {
            if (!(keyParam instanceof ParametersWithIV)) {
                final StringBuilder sb = new StringBuilder();
                sb.append("invalid parameters passed to CCM: ");
                sb.append(keyParam.getClass().getName());
                throw new IllegalArgumentException(sb.toString());
            }
            final ParametersWithIV parametersWithIV = (ParametersWithIV)keyParam;
            this.nonce = parametersWithIV.getIV();
            this.initialAssociatedText = null;
            this.macSize = this.macBlock.length / 2;
            keyParam = parametersWithIV.getParameters();
        }
        if (keyParam != null) {
            this.keyParam = keyParam;
        }
        final byte[] nonce = this.nonce;
        if (nonce != null && nonce.length >= 7 && nonce.length <= 13) {
            this.reset();
            return;
        }
        throw new IllegalArgumentException("nonce must have length from 7 to 13 octets");
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
        throw new DataLengthException("Input buffer too short");
    }
    
    public int processPacket(byte[] array, int n, int macSize, final byte[] array2, final int n2) throws IllegalStateException, InvalidCipherTextException, DataLengthException {
        if (this.keyParam == null) {
            throw new IllegalStateException("CCM cipher unitialized.");
        }
        final int n3 = 15 - this.nonce.length;
        if (n3 < 4 && macSize >= 1 << n3 * 8) {
            throw new IllegalStateException("CCM packet too large for choice of q.");
        }
        final byte[] array3 = new byte[this.blockSize];
        array3[0] = (byte)(n3 - 1 & 0x7);
        final byte[] nonce = this.nonce;
        System.arraycopy(nonce, 0, array3, 1, nonce.length);
        final SICBlockCipher sicBlockCipher = new SICBlockCipher(this.cipher);
        sicBlockCipher.init(this.forEncryption, new ParametersWithIV(this.keyParam, array3));
        if (this.forEncryption) {
            final int n4 = this.macSize + macSize;
            if (array2.length >= n4 + n2) {
                this.calculateMac(array, n, macSize, this.macBlock);
                final byte[] array4 = new byte[this.blockSize];
                sicBlockCipher.processBlock(this.macBlock, 0, array4, 0);
                int n5 = n;
                int n6 = n2;
                int n7;
                int blockSize;
                while (true) {
                    n7 = n + macSize;
                    blockSize = this.blockSize;
                    if (n5 >= n7 - blockSize) {
                        break;
                    }
                    sicBlockCipher.processBlock(array, n5, array2, n6);
                    final int blockSize2 = this.blockSize;
                    n6 += blockSize2;
                    n5 += blockSize2;
                }
                final byte[] array5 = new byte[blockSize];
                n = n7 - n5;
                System.arraycopy(array, n5, array5, 0, n);
                sicBlockCipher.processBlock(array5, 0, array5, 0);
                System.arraycopy(array5, 0, array2, n6, n);
                System.arraycopy(array4, 0, array2, n2 + macSize, this.macSize);
                return n4;
            }
            throw new OutputLengthException("Output buffer too short.");
        }
        else {
            final int macSize2 = this.macSize;
            if (macSize < macSize2) {
                throw new InvalidCipherTextException("data too short");
            }
            final int n8 = macSize - macSize2;
            if (array2.length < n8 + n2) {
                throw new OutputLengthException("Output buffer too short.");
            }
            final int n9 = n + n8;
            System.arraycopy(array, n9, this.macBlock, 0, macSize2);
            final byte[] macBlock = this.macBlock;
            sicBlockCipher.processBlock(macBlock, 0, macBlock, 0);
            macSize = this.macSize;
            while (true) {
                final byte[] macBlock2 = this.macBlock;
                if (macSize == macBlock2.length) {
                    break;
                }
                macBlock2[macSize] = 0;
                ++macSize;
            }
            macSize = n;
            int n10 = n2;
            int blockSize3;
            while (true) {
                blockSize3 = this.blockSize;
                if (macSize >= n9 - blockSize3) {
                    break;
                }
                sicBlockCipher.processBlock(array, macSize, array2, n10);
                final int blockSize4 = this.blockSize;
                n10 += blockSize4;
                macSize += blockSize4;
            }
            final byte[] array6 = new byte[blockSize3];
            n = n8 - (macSize - n);
            System.arraycopy(array, macSize, array6, 0, n);
            sicBlockCipher.processBlock(array6, 0, array6, 0);
            System.arraycopy(array6, 0, array2, n10, n);
            array = new byte[this.blockSize];
            this.calculateMac(array2, n2, n8, array);
            if (Arrays.constantTimeAreEqual(this.macBlock, array)) {
                return n8;
            }
            throw new InvalidCipherTextException("mac check in CCM failed");
        }
    }
    
    public byte[] processPacket(final byte[] array, final int n, final int n2) throws IllegalStateException, InvalidCipherTextException {
        int n3;
        if (this.forEncryption) {
            n3 = this.macSize + n2;
        }
        else {
            final int macSize = this.macSize;
            if (n2 < macSize) {
                throw new InvalidCipherTextException("data too short");
            }
            n3 = n2 - macSize;
        }
        final byte[] array2 = new byte[n3];
        this.processPacket(array, n, n2, array2, 0);
        return array2;
    }
    
    @Override
    public void reset() {
        this.cipher.reset();
        this.associatedText.reset();
        this.data.reset();
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
