package org.spongycastle.crypto.modes;

import org.spongycastle.crypto.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.util.*;

public class CBCBlockCipher implements BlockCipher
{
    private byte[] IV;
    private int blockSize;
    private byte[] cbcNextV;
    private byte[] cbcV;
    private BlockCipher cipher;
    private boolean encrypting;
    
    public CBCBlockCipher(final BlockCipher cipher) {
        this.cipher = null;
        this.cipher = cipher;
        final int blockSize = cipher.getBlockSize();
        this.blockSize = blockSize;
        this.IV = new byte[blockSize];
        this.cbcV = new byte[blockSize];
        this.cbcNextV = new byte[blockSize];
    }
    
    private int decryptBlock(byte[] cbcV, int i, final byte[] array, final int n) throws DataLengthException, IllegalStateException {
        final int blockSize = this.blockSize;
        if (i + blockSize <= cbcV.length) {
            final byte[] cbcNextV = this.cbcNextV;
            final int n2 = 0;
            System.arraycopy(cbcV, i, cbcNextV, 0, blockSize);
            final int processBlock = this.cipher.processBlock(cbcV, i, array, n);
            int n3;
            for (i = n2; i < this.blockSize; ++i) {
                n3 = n + i;
                array[n3] ^= this.cbcV[i];
            }
            cbcV = this.cbcV;
            this.cbcV = this.cbcNextV;
            this.cbcNextV = cbcV;
            return processBlock;
        }
        throw new DataLengthException("input buffer too short");
    }
    
    private int encryptBlock(byte[] cbcV, int processBlock, final byte[] array, final int n) throws DataLengthException, IllegalStateException {
        if (this.blockSize + processBlock <= cbcV.length) {
            for (int i = 0; i < this.blockSize; ++i) {
                final byte[] cbcV2 = this.cbcV;
                cbcV2[i] ^= cbcV[processBlock + i];
            }
            processBlock = this.cipher.processBlock(this.cbcV, 0, array, n);
            cbcV = this.cbcV;
            System.arraycopy(array, n, cbcV, 0, cbcV.length);
            return processBlock;
        }
        throw new DataLengthException("input buffer too short");
    }
    
    @Override
    public String getAlgorithmName() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.cipher.getAlgorithmName());
        sb.append("/CBC");
        return sb.toString();
    }
    
    @Override
    public int getBlockSize() {
        return this.cipher.getBlockSize();
    }
    
    public BlockCipher getUnderlyingCipher() {
        return this.cipher;
    }
    
    @Override
    public void init(final boolean encrypting, CipherParameters parameters) throws IllegalArgumentException {
        final boolean encrypting2 = this.encrypting;
        this.encrypting = encrypting;
        BlockCipher blockCipher;
        if (parameters instanceof ParametersWithIV) {
            final ParametersWithIV parametersWithIV = (ParametersWithIV)parameters;
            final byte[] iv = parametersWithIV.getIV();
            if (iv.length != this.blockSize) {
                throw new IllegalArgumentException("initialisation vector must be the same length as block size");
            }
            System.arraycopy(iv, 0, this.IV, 0, iv.length);
            this.reset();
            if (parametersWithIV.getParameters() != null) {
                blockCipher = this.cipher;
                parameters = parametersWithIV.getParameters();
            }
            else {
                if (encrypting2 == encrypting) {
                    return;
                }
                throw new IllegalArgumentException("cannot change encrypting state without providing key.");
            }
        }
        else {
            this.reset();
            if (parameters != null) {
                blockCipher = this.cipher;
            }
            else {
                if (encrypting2 == encrypting) {
                    return;
                }
                throw new IllegalArgumentException("cannot change encrypting state without providing key.");
            }
        }
        blockCipher.init(encrypting, parameters);
    }
    
    @Override
    public int processBlock(final byte[] array, final int n, final byte[] array2, final int n2) throws DataLengthException, IllegalStateException {
        if (this.encrypting) {
            return this.encryptBlock(array, n, array2, n2);
        }
        return this.decryptBlock(array, n, array2, n2);
    }
    
    @Override
    public void reset() {
        final byte[] iv = this.IV;
        System.arraycopy(iv, 0, this.cbcV, 0, iv.length);
        Arrays.fill(this.cbcNextV, (byte)0);
        this.cipher.reset();
    }
}
