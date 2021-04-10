package org.spongycastle.crypto.engines;

import org.spongycastle.crypto.params.*;
import org.spongycastle.crypto.*;

public class Grain128Engine implements StreamCipher
{
    private static final int STATE_SIZE = 4;
    private int index;
    private boolean initialised;
    private int[] lfsr;
    private int[] nfsr;
    private byte[] out;
    private int output;
    private byte[] workingIV;
    private byte[] workingKey;
    
    public Grain128Engine() {
        this.index = 4;
        this.initialised = false;
    }
    
    private byte getKeyStream() {
        if (this.index > 3) {
            this.oneRound();
            this.index = 0;
        }
        return this.out[this.index++];
    }
    
    private int getOutput() {
        final int[] nfsr = this.nfsr;
        final int n = nfsr[0];
        final int n2 = nfsr[1];
        final int n3 = nfsr[0] >>> 12 | nfsr[1] << 20;
        final int n4 = nfsr[0];
        final int n5 = nfsr[1];
        final int n6 = nfsr[1];
        final int n7 = nfsr[2];
        final int n8 = nfsr[1];
        final int n9 = nfsr[2];
        final int n10 = nfsr[2];
        final int n11 = nfsr[2];
        final int n12 = nfsr[3];
        final int n13 = nfsr[2];
        final int n14 = nfsr[3];
        final int n15 = nfsr[3] << 1 | nfsr[2] >>> 31;
        final int[] lfsr = this.lfsr;
        return (n15 & (lfsr[1] >>> 10 | lfsr[2] << 22)) ^ (((lfsr[0] >>> 20 | lfsr[1] << 12) & (lfsr[0] >>> 13 | lfsr[1] << 19)) ^ (n3 & (lfsr[0] >>> 8 | lfsr[1] << 24))) ^ ((lfsr[1] >>> 28 | lfsr[2] << 4) & (lfsr[2] >>> 15 | lfsr[3] << 17)) ^ (n15 & n3 & (lfsr[2] >>> 31 | lfsr[3] << 1)) ^ (lfsr[2] >>> 29 | lfsr[3] << 3) ^ (n >>> 2 | n2 << 30) ^ (n4 >>> 15 | n5 << 17) ^ (n6 >>> 4 | n7 << 28) ^ (n8 >>> 13 | n9 << 19) ^ n10 ^ (n11 >>> 9 | n12 << 23) ^ (n13 >>> 25 | n14 << 7);
    }
    
    private int getOutputLFSR() {
        final int[] lfsr = this.lfsr;
        return lfsr[3] ^ ((lfsr[0] >>> 7 | lfsr[1] << 25) ^ lfsr[0] ^ (lfsr[1] >>> 6 | lfsr[2] << 26) ^ (lfsr[2] >>> 6 | lfsr[3] << 26) ^ (lfsr[2] >>> 17 | lfsr[3] << 15));
    }
    
    private int getOutputNFSR() {
        final int[] nfsr = this.nfsr;
        return nfsr[3] ^ (nfsr[0] ^ (nfsr[0] >>> 26 | nfsr[1] << 6) ^ (nfsr[1] >>> 24 | nfsr[2] << 8) ^ (nfsr[2] >>> 27 | nfsr[3] << 5)) ^ ((nfsr[0] >>> 3 | nfsr[1] << 29) & (nfsr[2] >>> 3 | nfsr[3] << 29)) ^ ((nfsr[0] >>> 11 | nfsr[1] << 21) & (nfsr[0] >>> 13 | nfsr[1] << 19)) ^ ((nfsr[0] >>> 17 | nfsr[1] << 15) & (nfsr[0] >>> 18 | nfsr[1] << 14)) ^ ((nfsr[0] >>> 27 | nfsr[1] << 5) & (nfsr[1] >>> 27 | nfsr[2] << 5)) ^ ((nfsr[1] >>> 8 | nfsr[2] << 24) & (nfsr[1] >>> 16 | nfsr[2] << 16)) ^ ((nfsr[1] >>> 29 | nfsr[2] << 3) & (nfsr[2] >>> 1 | nfsr[3] << 31)) ^ ((nfsr[2] >>> 4 | nfsr[3] << 28) & (nfsr[2] >>> 20 | nfsr[3] << 12));
    }
    
    private void initGrain() {
        for (int i = 0; i < 8; ++i) {
            this.output = this.getOutput();
            this.nfsr = this.shift(this.nfsr, this.getOutputNFSR() ^ this.lfsr[0] ^ this.output);
            this.lfsr = this.shift(this.lfsr, this.getOutputLFSR() ^ this.output);
        }
        this.initialised = true;
    }
    
    private void oneRound() {
        final int output = this.getOutput();
        this.output = output;
        final byte[] out = this.out;
        out[0] = (byte)output;
        out[1] = (byte)(output >> 8);
        out[2] = (byte)(output >> 16);
        out[3] = (byte)(output >> 24);
        this.nfsr = this.shift(this.nfsr, this.getOutputNFSR() ^ this.lfsr[0]);
        this.lfsr = this.shift(this.lfsr, this.getOutputLFSR());
    }
    
    private void setKey(final byte[] workingKey, byte[] workingIV) {
        workingIV[13] = (workingIV[12] = -1);
        workingIV[15] = (workingIV[14] = -1);
        this.workingKey = workingKey;
        this.workingIV = workingIV;
        int n = 0;
        int n2 = 0;
        while (true) {
            final int[] nfsr = this.nfsr;
            if (n >= nfsr.length) {
                break;
            }
            workingIV = this.workingKey;
            final int n3 = n2 + 3;
            final byte b = workingIV[n3];
            final int n4 = n2 + 2;
            final byte b2 = workingIV[n4];
            final int n5 = n2 + 1;
            nfsr[n] = ((workingIV[n2] & 0xFF) | (b << 24 | (b2 << 16 & 0xFF0000) | (workingIV[n5] << 8 & 0xFF00)));
            final int[] lfsr = this.lfsr;
            workingIV = this.workingIV;
            lfsr[n] = ((workingIV[n2] & 0xFF) | (workingIV[n3] << 24 | (workingIV[n4] << 16 & 0xFF0000) | (workingIV[n5] << 8 & 0xFF00)));
            n2 += 4;
            ++n;
        }
    }
    
    private int[] shift(final int[] array, final int n) {
        array[0] = array[1];
        array[1] = array[2];
        array[2] = array[3];
        array[3] = n;
        return array;
    }
    
    @Override
    public String getAlgorithmName() {
        return "Grain-128";
    }
    
    @Override
    public void init(final boolean b, final CipherParameters cipherParameters) throws IllegalArgumentException {
        if (!(cipherParameters instanceof ParametersWithIV)) {
            throw new IllegalArgumentException("Grain-128 Init parameters must include an IV");
        }
        final ParametersWithIV parametersWithIV = (ParametersWithIV)cipherParameters;
        final byte[] iv = parametersWithIV.getIV();
        if (iv == null || iv.length != 12) {
            throw new IllegalArgumentException("Grain-128  requires exactly 12 bytes of IV");
        }
        if (parametersWithIV.getParameters() instanceof KeyParameter) {
            final KeyParameter keyParameter = (KeyParameter)parametersWithIV.getParameters();
            this.workingIV = new byte[keyParameter.getKey().length];
            this.workingKey = new byte[keyParameter.getKey().length];
            this.lfsr = new int[4];
            this.nfsr = new int[4];
            this.out = new byte[4];
            System.arraycopy(iv, 0, this.workingIV, 0, iv.length);
            System.arraycopy(keyParameter.getKey(), 0, this.workingKey, 0, keyParameter.getKey().length);
            this.reset();
            return;
        }
        throw new IllegalArgumentException("Grain-128 Init parameters must include a key");
    }
    
    @Override
    public int processBytes(final byte[] array, final int n, final int n2, final byte[] array2, final int n3) throws DataLengthException {
        if (!this.initialised) {
            final StringBuilder sb = new StringBuilder();
            sb.append(this.getAlgorithmName());
            sb.append(" not initialised");
            throw new IllegalStateException(sb.toString());
        }
        if (n + n2 > array.length) {
            throw new DataLengthException("input buffer too short");
        }
        if (n3 + n2 <= array2.length) {
            for (int i = 0; i < n2; ++i) {
                array2[n3 + i] = (byte)(array[n + i] ^ this.getKeyStream());
            }
            return n2;
        }
        throw new OutputLengthException("output buffer too short");
    }
    
    @Override
    public void reset() {
        this.index = 4;
        this.setKey(this.workingKey, this.workingIV);
        this.initGrain();
    }
    
    @Override
    public byte returnByte(final byte b) {
        if (this.initialised) {
            return (byte)(b ^ this.getKeyStream());
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(this.getAlgorithmName());
        sb.append(" not initialised");
        throw new IllegalStateException(sb.toString());
    }
}
