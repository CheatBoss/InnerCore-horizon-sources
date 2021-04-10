package org.spongycastle.crypto.modes;

import org.spongycastle.util.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.crypto.*;
import java.io.*;

public class KCCMBlockCipher implements AEADBlockCipher
{
    private static final int BITS_IN_BYTE = 8;
    private static final int BYTES_IN_INT = 4;
    private static final int MAX_MAC_BIT_LENGTH = 512;
    private static final int MIN_MAC_BIT_LENGTH = 64;
    private byte[] G1;
    private int Nb_;
    private ExposedByteArrayOutputStream associatedText;
    private byte[] buffer;
    private byte[] counter;
    private ExposedByteArrayOutputStream data;
    private BlockCipher engine;
    private boolean forEncryption;
    private byte[] initialAssociatedText;
    private byte[] mac;
    private byte[] macBlock;
    private int macSize;
    private byte[] nonce;
    private byte[] s;
    
    public KCCMBlockCipher(final BlockCipher blockCipher) {
        this(blockCipher, 4);
    }
    
    public KCCMBlockCipher(final BlockCipher engine, final int nb) {
        this.associatedText = new ExposedByteArrayOutputStream();
        this.data = new ExposedByteArrayOutputStream();
        this.Nb_ = 4;
        this.engine = engine;
        this.macSize = engine.getBlockSize();
        this.nonce = new byte[engine.getBlockSize()];
        this.initialAssociatedText = new byte[engine.getBlockSize()];
        this.mac = new byte[engine.getBlockSize()];
        this.macBlock = new byte[engine.getBlockSize()];
        this.G1 = new byte[engine.getBlockSize()];
        this.buffer = new byte[engine.getBlockSize()];
        this.s = new byte[engine.getBlockSize()];
        this.counter = new byte[engine.getBlockSize()];
        this.setNb(nb);
    }
    
    private void CalculateMac(final byte[] array, int n, int i) {
        while (i > 0) {
            for (int j = 0; j < this.engine.getBlockSize(); ++j) {
                final byte[] macBlock = this.macBlock;
                macBlock[j] ^= array[n + j];
            }
            final BlockCipher engine = this.engine;
            final byte[] macBlock2 = this.macBlock;
            engine.processBlock(macBlock2, 0, macBlock2, 0);
            i -= this.engine.getBlockSize();
            n += this.engine.getBlockSize();
        }
    }
    
    private void ProcessBlock(final byte[] array, final int n, int i, final byte[] array2, final int n2) {
        final int n3 = 0;
        i = 0;
        while (true) {
            final byte[] counter = this.counter;
            if (i >= counter.length) {
                break;
            }
            final byte[] s = this.s;
            s[i] += counter[i];
            ++i;
        }
        this.engine.processBlock(this.s, 0, this.buffer, 0);
        for (i = n3; i < this.engine.getBlockSize(); ++i) {
            array2[n2 + i] = (byte)(this.buffer[i] ^ array[n + i]);
        }
    }
    
    private byte getFlag(final boolean b, final int n) {
        final StringBuffer sb = new StringBuffer();
        String s;
        if (b) {
            s = "1";
        }
        else {
            s = "0";
        }
        sb.append(s);
        Label_0099: {
            String s2;
            if (n != 8) {
                if (n != 16) {
                    if (n != 32) {
                        if (n != 48) {
                            if (n != 64) {
                                break Label_0099;
                            }
                            s2 = "110";
                        }
                        else {
                            s2 = "101";
                        }
                    }
                    else {
                        s2 = "100";
                    }
                }
                else {
                    s2 = "011";
                }
            }
            else {
                s2 = "010";
            }
            sb.append(s2);
        }
        String s3;
        for (s3 = Integer.toBinaryString(this.Nb_ - 1); s3.length() < 4; s3 = new StringBuffer(s3).insert(0, "0").toString()) {}
        sb.append(s3);
        return (byte)Integer.parseInt(sb.toString(), 2);
    }
    
    private void intToBytes(final int n, final byte[] array, final int n2) {
        array[n2 + 3] = (byte)(n >> 24);
        array[n2 + 2] = (byte)(n >> 16);
        array[n2 + 1] = (byte)(n >> 8);
        array[n2] = (byte)n;
    }
    
    private void processAAD(byte[] macBlock, int i, int j, int k) {
        if (j - i < this.engine.getBlockSize()) {
            throw new IllegalArgumentException("authText buffer too short");
        }
        if (j % this.engine.getBlockSize() != 0) {
            throw new IllegalArgumentException("padding not supported");
        }
        final byte[] nonce = this.nonce;
        System.arraycopy(nonce, 0, this.G1, 0, nonce.length - this.Nb_ - 1);
        this.intToBytes(k, this.buffer, 0);
        System.arraycopy(this.buffer, 0, this.G1, this.nonce.length - this.Nb_ - 1, 4);
        final byte[] g1 = this.G1;
        g1[g1.length - 1] = this.getFlag(true, this.macSize);
        this.engine.processBlock(this.G1, 0, this.macBlock, 0);
        this.intToBytes(j, this.buffer, 0);
        if (j <= this.engine.getBlockSize() - this.Nb_) {
            byte[] buffer;
            int n;
            for (k = 0; k < j; ++k) {
                buffer = this.buffer;
                n = this.Nb_ + k;
                buffer[n] ^= macBlock[i + k];
            }
            for (i = 0; i < this.engine.getBlockSize(); ++i) {
                macBlock = this.macBlock;
                macBlock[i] ^= this.buffer[i];
            }
            final BlockCipher engine = this.engine;
            final byte[] macBlock2 = this.macBlock;
            engine.processBlock(macBlock2, 0, macBlock2, 0);
            return;
        }
        byte[] macBlock3;
        for (k = 0; k < this.engine.getBlockSize(); ++k) {
            macBlock3 = this.macBlock;
            macBlock3[k] ^= this.buffer[k];
        }
        final BlockCipher engine2 = this.engine;
        final byte[] macBlock4 = this.macBlock;
        engine2.processBlock(macBlock4, 0, macBlock4, 0);
        while (j != 0) {
            byte[] macBlock5;
            for (k = 0; k < this.engine.getBlockSize(); ++k) {
                macBlock5 = this.macBlock;
                macBlock5[k] ^= macBlock[k + i];
            }
            final BlockCipher engine3 = this.engine;
            final byte[] macBlock6 = this.macBlock;
            engine3.processBlock(macBlock6, 0, macBlock6, 0);
            i += this.engine.getBlockSize();
            j -= this.engine.getBlockSize();
        }
    }
    
    private void setNb(final int nb_) {
        if (nb_ != 4 && nb_ != 6 && nb_ != 8) {
            throw new IllegalArgumentException("Nb = 4 is recommended by DSTU7624 but can be changed to only 6 or 8 in this implementation");
        }
        this.Nb_ = nb_;
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
        sb.append(this.engine.getAlgorithmName());
        sb.append("/KCCM");
        return sb.toString();
    }
    
    @Override
    public byte[] getMac() {
        return Arrays.clone(this.mac);
    }
    
    @Override
    public int getOutputSize(final int n) {
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
    public void init(final boolean forEncryption, CipherParameters cipherParameters) throws IllegalArgumentException {
        if (cipherParameters instanceof AEADParameters) {
            final AEADParameters aeadParameters = (AEADParameters)cipherParameters;
            if (aeadParameters.getMacSize() > 512 || aeadParameters.getMacSize() < 64 || aeadParameters.getMacSize() % 8 != 0) {
                throw new IllegalArgumentException("Invalid mac size specified");
            }
            this.nonce = aeadParameters.getNonce();
            this.macSize = aeadParameters.getMacSize() / 8;
            this.initialAssociatedText = aeadParameters.getAssociatedText();
            cipherParameters = aeadParameters.getKey();
        }
        else {
            if (!(cipherParameters instanceof ParametersWithIV)) {
                throw new IllegalArgumentException("Invalid parameters specified");
            }
            final ParametersWithIV parametersWithIV = (ParametersWithIV)cipherParameters;
            this.nonce = parametersWithIV.getIV();
            this.macSize = this.engine.getBlockSize();
            this.initialAssociatedText = null;
            cipherParameters = parametersWithIV.getParameters();
        }
        this.mac = new byte[this.macSize];
        this.forEncryption = forEncryption;
        this.engine.init(true, cipherParameters);
        this.counter[0] = 1;
        final byte[] initialAssociatedText = this.initialAssociatedText;
        if (initialAssociatedText != null) {
            this.processAADBytes(initialAssociatedText, 0, initialAssociatedText.length);
        }
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
    
    public int processPacket(byte[] array, int n, final int n2, final byte[] array2, int n3) throws IllegalStateException, InvalidCipherTextException {
        if (array.length - n < n2) {
            throw new DataLengthException("input buffer too short");
        }
        if (array2.length - n3 < n2) {
            throw new OutputLengthException("output buffer too short");
        }
        if (this.associatedText.size() > 0) {
            byte[] array3;
            int n4;
            int size;
            if (this.forEncryption) {
                array3 = this.associatedText.getBuffer();
                n4 = this.associatedText.size();
                size = this.data.size();
            }
            else {
                array3 = this.associatedText.getBuffer();
                n4 = this.associatedText.size();
                size = this.data.size() - this.macSize;
            }
            this.processAAD(array3, 0, n4, size);
        }
        if (this.forEncryption) {
            if (n2 % this.engine.getBlockSize() == 0) {
                this.CalculateMac(array, n, n2);
                this.engine.processBlock(this.nonce, 0, this.s, 0);
                int i;
                int n5;
                for (i = n2, n5 = n, n = n3; i > 0; i -= this.engine.getBlockSize(), n5 += this.engine.getBlockSize(), n += this.engine.getBlockSize()) {
                    this.ProcessBlock(array, n5, n2, array2, n);
                }
                n3 = 0;
                while (true) {
                    array = this.counter;
                    if (n3 >= array.length) {
                        break;
                    }
                    final byte[] s = this.s;
                    s[n3] += array[n3];
                    ++n3;
                }
                this.engine.processBlock(this.s, 0, this.buffer, 0);
                n3 = 0;
                int macSize;
                while (true) {
                    macSize = this.macSize;
                    if (n3 >= macSize) {
                        break;
                    }
                    array2[n + n3] = (byte)(this.buffer[n3] ^ this.macBlock[n3]);
                    ++n3;
                }
                System.arraycopy(this.macBlock, 0, this.mac, 0, macSize);
                this.reset();
                return n2 + this.macSize;
            }
            throw new DataLengthException("partial blocks not supported");
        }
        else {
            if ((n2 - this.macSize) % this.engine.getBlockSize() != 0) {
                throw new DataLengthException("partial blocks not supported");
            }
            this.engine.processBlock(this.nonce, 0, this.s, 0);
            final int n6 = n2 / this.engine.getBlockSize();
            final int n7 = n3;
            int j = 0;
            n3 = n;
            n = n7;
            while (j < n6) {
                this.ProcessBlock(array, n3, n2, array2, n);
                n3 += this.engine.getBlockSize();
                n += this.engine.getBlockSize();
                ++j;
            }
            int n8 = n;
            if (n2 > n3) {
                int n9 = 0;
                while (true) {
                    final byte[] counter = this.counter;
                    if (n9 >= counter.length) {
                        break;
                    }
                    final byte[] s2 = this.s;
                    s2[n9] += counter[n9];
                    ++n9;
                }
                this.engine.processBlock(this.s, 0, this.buffer, 0);
                int n10 = 0;
                int macSize2;
                while (true) {
                    macSize2 = this.macSize;
                    if (n10 >= macSize2) {
                        break;
                    }
                    array2[n + n10] = (byte)(this.buffer[n10] ^ array[n3 + n10]);
                    ++n10;
                }
                n8 = n + macSize2;
            }
            n = 0;
            while (true) {
                array = this.counter;
                if (n >= array.length) {
                    break;
                }
                final byte[] s3 = this.s;
                s3[n] += array[n];
                ++n;
            }
            this.engine.processBlock(this.s, 0, this.buffer, 0);
            n = this.macSize;
            System.arraycopy(array2, n8 - n, this.buffer, 0, n);
            this.CalculateMac(array2, 0, n8 - this.macSize);
            System.arraycopy(this.macBlock, 0, this.mac, 0, this.macSize);
            n = this.macSize;
            array = new byte[n];
            System.arraycopy(this.buffer, 0, array, 0, n);
            if (Arrays.constantTimeAreEqual(this.mac, array)) {
                this.reset();
                return n2 - this.macSize;
            }
            throw new InvalidCipherTextException("mac check failed");
        }
    }
    
    @Override
    public void reset() {
        Arrays.fill(this.G1, (byte)0);
        Arrays.fill(this.buffer, (byte)0);
        Arrays.fill(this.counter, (byte)0);
        Arrays.fill(this.macBlock, (byte)0);
        this.counter[0] = 1;
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
