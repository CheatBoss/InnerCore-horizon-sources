package org.spongycastle.crypto.modes;

import org.spongycastle.crypto.params.*;
import org.spongycastle.util.*;
import org.spongycastle.crypto.*;

public class KCTRBlockCipher extends StreamBlockCipher
{
    private int byteCount;
    private BlockCipher engine;
    private boolean initialised;
    private byte[] iv;
    private byte[] ofbOutV;
    private byte[] ofbV;
    
    public KCTRBlockCipher(final BlockCipher engine) {
        super(engine);
        this.engine = engine;
        this.iv = new byte[engine.getBlockSize()];
        this.ofbV = new byte[engine.getBlockSize()];
        this.ofbOutV = new byte[engine.getBlockSize()];
    }
    
    private void checkCounter() {
    }
    
    private void incrementCounterAt(int n) {
        while (true) {
            final byte[] ofbV = this.ofbV;
            if (n >= ofbV.length) {
                return;
            }
            if (++ofbV[n] != 0) {
                return;
            }
            ++n;
        }
    }
    
    @Override
    protected byte calculateByte(final byte b) {
        final int byteCount = this.byteCount;
        if (byteCount == 0) {
            this.incrementCounterAt(0);
            this.checkCounter();
            this.engine.processBlock(this.ofbV, 0, this.ofbOutV, 0);
            return (byte)(b ^ this.ofbOutV[this.byteCount++]);
        }
        final byte[] ofbOutV = this.ofbOutV;
        final int byteCount2 = byteCount + 1;
        this.byteCount = byteCount2;
        final byte b2 = (byte)(b ^ ofbOutV[byteCount]);
        if (byteCount2 == this.ofbV.length) {
            this.byteCount = 0;
        }
        return b2;
    }
    
    @Override
    public String getAlgorithmName() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.engine.getAlgorithmName());
        sb.append("/KCTR");
        return sb.toString();
    }
    
    @Override
    public int getBlockSize() {
        return this.engine.getBlockSize();
    }
    
    @Override
    public void init(final boolean b, CipherParameters parameters) throws IllegalArgumentException {
        this.initialised = true;
        if (parameters instanceof ParametersWithIV) {
            final ParametersWithIV parametersWithIV = (ParametersWithIV)parameters;
            final byte[] iv = parametersWithIV.getIV();
            final byte[] iv2 = this.iv;
            final int length = iv2.length;
            final int length2 = iv.length;
            Arrays.fill(iv2, (byte)0);
            System.arraycopy(iv, 0, this.iv, length - length2, iv.length);
            parameters = parametersWithIV.getParameters();
            if (parameters != null) {
                this.engine.init(true, parameters);
            }
            this.reset();
            return;
        }
        throw new IllegalArgumentException("invalid parameter passed");
    }
    
    @Override
    public int processBlock(final byte[] array, final int n, final byte[] array2, final int n2) throws DataLengthException, IllegalStateException {
        if (array.length - n < this.getBlockSize()) {
            throw new DataLengthException("input buffer too short");
        }
        if (array2.length - n2 >= this.getBlockSize()) {
            this.processBytes(array, n, this.getBlockSize(), array2, n2);
            return this.getBlockSize();
        }
        throw new OutputLengthException("output buffer too short");
    }
    
    @Override
    public void reset() {
        if (this.initialised) {
            this.engine.processBlock(this.iv, 0, this.ofbV, 0);
        }
        this.engine.reset();
        this.byteCount = 0;
    }
}
