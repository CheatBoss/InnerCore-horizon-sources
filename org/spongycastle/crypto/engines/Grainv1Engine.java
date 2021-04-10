package org.spongycastle.crypto.engines;

import org.spongycastle.crypto.params.*;
import org.spongycastle.crypto.*;

public class Grainv1Engine implements StreamCipher
{
    private static final int STATE_SIZE = 5;
    private int index;
    private boolean initialised;
    private int[] lfsr;
    private int[] nfsr;
    private byte[] out;
    private int output;
    private byte[] workingIV;
    private byte[] workingKey;
    
    public Grainv1Engine() {
        this.index = 2;
        this.initialised = false;
    }
    
    private byte getKeyStream() {
        if (this.index > 1) {
            this.oneRound();
            this.index = 0;
        }
        return this.out[this.index++];
    }
    
    private int getOutput() {
        final int[] nfsr = this.nfsr;
        final int n = nfsr[0];
        final int n2 = nfsr[1];
        final int n3 = nfsr[0];
        final int n4 = nfsr[1];
        final int n5 = nfsr[0];
        final int n6 = nfsr[1];
        final int n7 = nfsr[0];
        final int n8 = nfsr[1];
        final int n9 = nfsr[1];
        final int n10 = nfsr[2];
        final int n11 = nfsr[2];
        final int n12 = nfsr[3];
        final int n13 = nfsr[3];
        final int n14 = nfsr[4];
        final int n15 = nfsr[4] << 1 | nfsr[3] >>> 15;
        final int[] lfsr = this.lfsr;
        final int n16 = lfsr[0] >>> 3 | lfsr[1] << 13;
        final int n17 = lfsr[1] >>> 9 | lfsr[2] << 7;
        final int n18 = lfsr[3] << 2 | lfsr[2] >>> 14;
        final int n19 = lfsr[4];
        final int n20 = n18 & n19;
        final int n21 = n16 & n18;
        return ((n15 & n20) ^ ((n15 & (n17 & n18)) ^ ((n21 & n15) ^ ((n19 & n21) ^ ((n16 & n17 & n18) ^ (n17 ^ n15 ^ (n16 & n19) ^ n20 ^ (n19 & n15)))))) ^ (n >>> 1 | n2 << 15) ^ (n3 >>> 2 | n4 << 14) ^ (n5 >>> 4 | n6 << 12) ^ (n7 >>> 10 | n8 << 6) ^ (n9 >>> 15 | n10 << 1) ^ (n11 >>> 11 | n12 << 5) ^ (n13 >>> 8 | n14 << 8)) & 0xFFFF;
    }
    
    private int getOutputLFSR() {
        final int[] lfsr = this.lfsr;
        return ((lfsr[4] << 2 | lfsr[3] >>> 14) ^ ((lfsr[0] >>> 13 | lfsr[1] << 3) ^ lfsr[0] ^ (lfsr[1] >>> 7 | lfsr[2] << 9) ^ (lfsr[2] >>> 6 | lfsr[3] << 10) ^ (lfsr[3] >>> 3 | lfsr[4] << 13))) & 0xFFFF;
    }
    
    private int getOutputNFSR() {
        final int[] nfsr = this.nfsr;
        final int n = nfsr[0];
        final int n2 = nfsr[0] >>> 9 | nfsr[1] << 7;
        final int n3 = nfsr[0];
        final int n4 = nfsr[1];
        final int n5 = nfsr[0] >>> 15 | nfsr[1] << 1;
        final int n6 = nfsr[1] >>> 5 | nfsr[2] << 11;
        final int n7 = nfsr[1] >>> 12 | nfsr[2] << 4;
        final int n8 = nfsr[2] >>> 1 | nfsr[3] << 15;
        final int n9 = nfsr[2] >>> 5 | nfsr[3] << 11;
        final int n10 = nfsr[2] >>> 13 | nfsr[3] << 3;
        final int n11 = nfsr[3] >>> 4 | nfsr[4] << 12;
        final int n12 = nfsr[3] >>> 12 | nfsr[4] << 4;
        final int n13 = nfsr[3];
        final int n14 = nfsr[4];
        final int n15 = nfsr[4] << 1 | nfsr[3] >>> 15;
        final int n16 = n15 & n12;
        final int n17 = n12 & n11;
        final int n18 = n8 & n7 & n6;
        return ((n15 & n10 & n7 & n2) ^ (n ^ ((n13 >>> 14 | n14 << 2) ^ n12 ^ n11 ^ n10 ^ n9 ^ n8 ^ n7 ^ n6 ^ (n3 >>> 14 | n4 << 2) ^ n2) ^ n16 ^ (n9 & n8) ^ (n5 & n2) ^ (n17 & n10) ^ n18) ^ (n17 & n9 & n8) ^ (n16 & n6 & n5) ^ (n16 & n11 & n10 & n9) ^ (n5 & n18 & n2) ^ (n11 & n10 & n9 & n8 & n7 & n6)) & 0xFFFF;
    }
    
    private void initGrain() {
        for (int i = 0; i < 10; ++i) {
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
        this.nfsr = this.shift(this.nfsr, this.getOutputNFSR() ^ this.lfsr[0]);
        this.lfsr = this.shift(this.lfsr, this.getOutputLFSR());
    }
    
    private void setKey(final byte[] workingKey, byte[] workingIV) {
        workingIV[9] = (workingIV[8] = -1);
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
            final int n3 = n2 + 1;
            nfsr[n] = (((workingIV[n2] & 0xFF) | workingIV[n3] << 8) & 0xFFFF);
            final int[] lfsr = this.lfsr;
            workingIV = this.workingIV;
            lfsr[n] = (((workingIV[n2] & 0xFF) | workingIV[n3] << 8) & 0xFFFF);
            n2 += 2;
            ++n;
        }
    }
    
    private int[] shift(final int[] array, final int n) {
        array[0] = array[1];
        array[1] = array[2];
        array[2] = array[3];
        array[3] = array[4];
        array[4] = n;
        return array;
    }
    
    @Override
    public String getAlgorithmName() {
        return "Grain v1";
    }
    
    @Override
    public void init(final boolean b, final CipherParameters cipherParameters) throws IllegalArgumentException {
        if (!(cipherParameters instanceof ParametersWithIV)) {
            throw new IllegalArgumentException("Grain v1 Init parameters must include an IV");
        }
        final ParametersWithIV parametersWithIV = (ParametersWithIV)cipherParameters;
        final byte[] iv = parametersWithIV.getIV();
        if (iv == null || iv.length != 8) {
            throw new IllegalArgumentException("Grain v1 requires exactly 8 bytes of IV");
        }
        if (parametersWithIV.getParameters() instanceof KeyParameter) {
            final KeyParameter keyParameter = (KeyParameter)parametersWithIV.getParameters();
            this.workingIV = new byte[keyParameter.getKey().length];
            this.workingKey = new byte[keyParameter.getKey().length];
            this.lfsr = new int[5];
            this.nfsr = new int[5];
            this.out = new byte[2];
            System.arraycopy(iv, 0, this.workingIV, 0, iv.length);
            System.arraycopy(keyParameter.getKey(), 0, this.workingKey, 0, keyParameter.getKey().length);
            this.reset();
            return;
        }
        throw new IllegalArgumentException("Grain v1 Init parameters must include a key");
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
        this.index = 2;
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
