package org.spongycastle.crypto.engines;

import org.spongycastle.crypto.*;

public class NullEngine implements BlockCipher
{
    protected static final int DEFAULT_BLOCK_SIZE = 1;
    private final int blockSize;
    private boolean initialised;
    
    public NullEngine() {
        this(1);
    }
    
    public NullEngine(final int blockSize) {
        this.blockSize = blockSize;
    }
    
    @Override
    public String getAlgorithmName() {
        return "Null";
    }
    
    @Override
    public int getBlockSize() {
        return this.blockSize;
    }
    
    @Override
    public void init(final boolean b, final CipherParameters cipherParameters) throws IllegalArgumentException {
        this.initialised = true;
    }
    
    @Override
    public int processBlock(final byte[] array, final int n, final byte[] array2, final int n2) throws DataLengthException, IllegalStateException {
        if (!this.initialised) {
            throw new IllegalStateException("Null engine not initialised");
        }
        final int blockSize = this.blockSize;
        if (n + blockSize > array.length) {
            throw new DataLengthException("input buffer too short");
        }
        if (blockSize + n2 <= array2.length) {
            int n3 = 0;
            int blockSize2;
            while (true) {
                blockSize2 = this.blockSize;
                if (n3 >= blockSize2) {
                    break;
                }
                array2[n2 + n3] = array[n + n3];
                ++n3;
            }
            return blockSize2;
        }
        throw new OutputLengthException("output buffer too short");
    }
    
    @Override
    public void reset() {
    }
}
