package org.spongycastle.crypto.engines;

import org.spongycastle.util.*;

public class ChaChaEngine extends Salsa20Engine
{
    public ChaChaEngine() {
    }
    
    public ChaChaEngine(final int n) {
        super(n);
    }
    
    public static void chachaCore(int i, final int[] array, final int[] array2) {
        if (array.length != 16) {
            throw new IllegalArgumentException();
        }
        if (array2.length != 16) {
            throw new IllegalArgumentException();
        }
        if (i % 2 == 0) {
            int n = array[0];
            int n2 = array[1];
            int n3 = array[2];
            int n4 = array[3];
            int rotl = array[4];
            int rotl2 = array[5];
            int rotl3 = array[6];
            int rotl4 = array[7];
            int n5 = array[8];
            int n6 = array[9];
            int n7 = array[10];
            int n8 = array[11];
            int rotl5 = array[12];
            int rotl6 = array[13];
            int rotl7 = array[14];
            int rotl8 = array[15];
            while (i > 0) {
                final int n9 = n + rotl;
                final int rotl9 = Salsa20Engine.rotl(rotl5 ^ n9, 16);
                final int n10 = n5 + rotl9;
                final int rotl10 = Salsa20Engine.rotl(rotl ^ n10, 12);
                final int n11 = n9 + rotl10;
                final int rotl11 = Salsa20Engine.rotl(rotl9 ^ n11, 8);
                final int n12 = n10 + rotl11;
                final int rotl12 = Salsa20Engine.rotl(rotl10 ^ n12, 7);
                final int n13 = n2 + rotl2;
                final int rotl13 = Salsa20Engine.rotl(rotl6 ^ n13, 16);
                final int n14 = n6 + rotl13;
                final int rotl14 = Salsa20Engine.rotl(rotl2 ^ n14, 12);
                final int n15 = n13 + rotl14;
                final int rotl15 = Salsa20Engine.rotl(rotl13 ^ n15, 8);
                final int n16 = n14 + rotl15;
                final int rotl16 = Salsa20Engine.rotl(rotl14 ^ n16, 7);
                final int n17 = n3 + rotl3;
                final int rotl17 = Salsa20Engine.rotl(rotl7 ^ n17, 16);
                final int n18 = n7 + rotl17;
                final int rotl18 = Salsa20Engine.rotl(rotl3 ^ n18, 12);
                final int n19 = n17 + rotl18;
                final int rotl19 = Salsa20Engine.rotl(rotl17 ^ n19, 8);
                final int n20 = n18 + rotl19;
                final int rotl20 = Salsa20Engine.rotl(rotl18 ^ n20, 7);
                final int n21 = n4 + rotl4;
                final int rotl21 = Salsa20Engine.rotl(rotl8 ^ n21, 16);
                final int n22 = n8 + rotl21;
                final int rotl22 = Salsa20Engine.rotl(rotl4 ^ n22, 12);
                final int n23 = n21 + rotl22;
                final int rotl23 = Salsa20Engine.rotl(rotl21 ^ n23, 8);
                final int n24 = n22 + rotl23;
                final int rotl24 = Salsa20Engine.rotl(rotl22 ^ n24, 7);
                final int n25 = n11 + rotl16;
                final int rotl25 = Salsa20Engine.rotl(rotl23 ^ n25, 16);
                final int n26 = n20 + rotl25;
                final int rotl26 = Salsa20Engine.rotl(rotl16 ^ n26, 12);
                n = n25 + rotl26;
                rotl8 = Salsa20Engine.rotl(rotl25 ^ n, 8);
                n7 = n26 + rotl8;
                rotl2 = Salsa20Engine.rotl(rotl26 ^ n7, 7);
                final int n27 = n15 + rotl20;
                final int rotl27 = Salsa20Engine.rotl(rotl11 ^ n27, 16);
                final int n28 = n24 + rotl27;
                final int rotl28 = Salsa20Engine.rotl(rotl20 ^ n28, 12);
                n2 = n27 + rotl28;
                rotl5 = Salsa20Engine.rotl(rotl27 ^ n2, 8);
                n8 = n28 + rotl5;
                rotl3 = Salsa20Engine.rotl(rotl28 ^ n8, 7);
                final int n29 = n19 + rotl24;
                final int rotl29 = Salsa20Engine.rotl(rotl15 ^ n29, 16);
                final int n30 = n12 + rotl29;
                final int rotl30 = Salsa20Engine.rotl(rotl24 ^ n30, 12);
                n3 = n29 + rotl30;
                rotl6 = Salsa20Engine.rotl(rotl29 ^ n3, 8);
                n5 = n30 + rotl6;
                rotl4 = Salsa20Engine.rotl(rotl30 ^ n5, 7);
                final int n31 = n23 + rotl12;
                final int rotl31 = Salsa20Engine.rotl(rotl19 ^ n31, 16);
                final int n32 = n16 + rotl31;
                final int rotl32 = Salsa20Engine.rotl(rotl12 ^ n32, 12);
                n4 = n31 + rotl32;
                rotl7 = Salsa20Engine.rotl(rotl31 ^ n4, 8);
                n6 = n32 + rotl7;
                rotl = Salsa20Engine.rotl(rotl32 ^ n6, 7);
                i -= 2;
            }
            array2[0] = n + array[0];
            array2[1] = n2 + array[1];
            array2[2] = n3 + array[2];
            array2[3] = n4 + array[3];
            array2[4] = rotl + array[4];
            array2[5] = rotl2 + array[5];
            array2[6] = rotl3 + array[6];
            array2[7] = rotl4 + array[7];
            array2[8] = n5 + array[8];
            array2[9] = n6 + array[9];
            array2[10] = n7 + array[10];
            array2[11] = n8 + array[11];
            array2[12] = rotl5 + array[12];
            array2[13] = rotl6 + array[13];
            array2[14] = rotl7 + array[14];
            array2[15] = rotl8 + array[15];
            return;
        }
        throw new IllegalArgumentException("Number of rounds must be even");
    }
    
    @Override
    protected void advanceCounter() {
        final int[] engineState = this.engineState;
        final int n = engineState[12] + 1;
        engineState[12] = n;
        if (n == 0) {
            final int[] engineState2 = this.engineState;
            ++engineState2[13];
        }
    }
    
    @Override
    protected void advanceCounter(final long n) {
        final int n2 = (int)(n >>> 32);
        final int n3 = (int)n;
        if (n2 > 0) {
            final int[] engineState = this.engineState;
            engineState[13] += n2;
        }
        final int n4 = this.engineState[12];
        final int[] engineState2 = this.engineState;
        engineState2[12] += n3;
        if (n4 != 0 && this.engineState[12] < n4) {
            final int[] engineState3 = this.engineState;
            ++engineState3[13];
        }
    }
    
    @Override
    protected void generateKeyStream(final byte[] array) {
        chachaCore(this.rounds, this.engineState, this.x);
        Pack.intToLittleEndian(this.x, array, 0);
    }
    
    @Override
    public String getAlgorithmName() {
        final StringBuilder sb = new StringBuilder();
        sb.append("ChaCha");
        sb.append(this.rounds);
        return sb.toString();
    }
    
    @Override
    protected long getCounter() {
        return (long)this.engineState[13] << 32 | ((long)this.engineState[12] & 0xFFFFFFFFL);
    }
    
    @Override
    protected void resetCounter() {
        this.engineState[12] = (this.engineState[13] = 0);
    }
    
    @Override
    protected void retreatCounter() {
        if (this.engineState[12] == 0 && this.engineState[13] == 0) {
            throw new IllegalStateException("attempt to reduce counter past zero.");
        }
        final int[] engineState = this.engineState;
        if (--engineState[12] == -1) {
            final int[] engineState2 = this.engineState;
            --engineState2[13];
        }
    }
    
    @Override
    protected void retreatCounter(final long n) {
        final int n2 = (int)(n >>> 32);
        final int n3 = (int)n;
        if (n2 != 0) {
            if (((long)this.engineState[13] & 0xFFFFFFFFL) < ((long)n2 & 0xFFFFFFFFL)) {
                throw new IllegalStateException("attempt to reduce counter past zero.");
            }
            final int[] engineState = this.engineState;
            engineState[13] -= n2;
        }
        if (((long)this.engineState[12] & 0xFFFFFFFFL) >= ((long)n3 & 0xFFFFFFFFL)) {
            final int[] engineState2 = this.engineState;
            engineState2[12] -= n3;
            return;
        }
        if (this.engineState[13] != 0) {
            final int[] engineState3 = this.engineState;
            --engineState3[13];
            final int[] engineState4 = this.engineState;
            engineState4[12] -= n3;
            return;
        }
        throw new IllegalStateException("attempt to reduce counter past zero.");
    }
    
    @Override
    protected void setKey(final byte[] array, final byte[] array2) {
        if (array != null) {
            if (array.length != 16 && array.length != 32) {
                final StringBuilder sb = new StringBuilder();
                sb.append(this.getAlgorithmName());
                sb.append(" requires 128 bit or 256 bit key");
                throw new IllegalArgumentException(sb.toString());
            }
            this.packTauOrSigma(array.length, this.engineState, 0);
            Pack.littleEndianToInt(array, 0, this.engineState, 4, 4);
            Pack.littleEndianToInt(array, array.length - 16, this.engineState, 8, 4);
        }
        Pack.littleEndianToInt(array2, 0, this.engineState, 14, 2);
    }
}
