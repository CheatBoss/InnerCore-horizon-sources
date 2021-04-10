package org.spongycastle.crypto.engines;

import org.spongycastle.util.*;

public class ChaCha7539Engine extends Salsa20Engine
{
    @Override
    protected void advanceCounter() {
        final int[] engineState = this.engineState;
        final int n = engineState[12] + 1;
        engineState[12] = n;
        if (n != 0) {
            return;
        }
        throw new IllegalStateException("attempt to increase counter past 2^32.");
    }
    
    @Override
    protected void advanceCounter(final long n) {
        final int n2 = (int)(n >>> 32);
        final int n3 = (int)n;
        if (n2 > 0) {
            throw new IllegalStateException("attempt to increase counter past 2^32.");
        }
        final int n4 = this.engineState[12];
        final int[] engineState = this.engineState;
        engineState[12] += n3;
        if (n4 == 0) {
            return;
        }
        if (this.engineState[12] >= n4) {
            return;
        }
        throw new IllegalStateException("attempt to increase counter past 2^32.");
    }
    
    @Override
    protected void generateKeyStream(final byte[] array) {
        ChaChaEngine.chachaCore(this.rounds, this.engineState, this.x);
        Pack.intToLittleEndian(this.x, array, 0);
    }
    
    @Override
    public String getAlgorithmName() {
        final StringBuilder sb = new StringBuilder();
        sb.append("ChaCha7539-");
        sb.append(this.rounds);
        return sb.toString();
    }
    
    @Override
    protected long getCounter() {
        return (long)this.engineState[12] & 0xFFFFFFFFL;
    }
    
    @Override
    protected int getNonceSize() {
        return 12;
    }
    
    @Override
    protected void resetCounter() {
        this.engineState[12] = 0;
    }
    
    @Override
    protected void retreatCounter() {
        if (this.engineState[12] != 0) {
            final int[] engineState = this.engineState;
            --engineState[12];
            return;
        }
        throw new IllegalStateException("attempt to reduce counter past zero.");
    }
    
    @Override
    protected void retreatCounter(final long n) {
        final int n2 = (int)(n >>> 32);
        final int n3 = (int)n;
        if (n2 != 0) {
            throw new IllegalStateException("attempt to reduce counter past zero.");
        }
        if (((long)this.engineState[12] & 0xFFFFFFFFL) >= ((long)n3 & 0xFFFFFFFFL)) {
            final int[] engineState = this.engineState;
            engineState[12] -= n3;
            return;
        }
        throw new IllegalStateException("attempt to reduce counter past zero.");
    }
    
    @Override
    protected void setKey(final byte[] array, final byte[] array2) {
        if (array != null) {
            if (array.length != 32) {
                final StringBuilder sb = new StringBuilder();
                sb.append(this.getAlgorithmName());
                sb.append(" requires 256 bit key");
                throw new IllegalArgumentException(sb.toString());
            }
            this.packTauOrSigma(array.length, this.engineState, 0);
            Pack.littleEndianToInt(array, 0, this.engineState, 4, 8);
        }
        Pack.littleEndianToInt(array2, 0, this.engineState, 13, 3);
    }
}
