package org.spongycastle.crypto.engines;

import org.spongycastle.util.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.crypto.*;

public class ISAACEngine implements StreamCipher
{
    private int a;
    private int b;
    private int c;
    private int[] engineState;
    private int index;
    private boolean initialised;
    private byte[] keyStream;
    private int[] results;
    private final int sizeL;
    private final int stateArraySize;
    private byte[] workingKey;
    
    public ISAACEngine() {
        this.sizeL = 8;
        this.stateArraySize = 256;
        this.engineState = null;
        this.results = null;
        this.a = 0;
        this.b = 0;
        this.c = 0;
        this.index = 0;
        this.keyStream = new byte[1024];
        this.workingKey = null;
        this.initialised = false;
    }
    
    private void isaac() {
        final int b = this.b;
        final int c = this.c + 1;
        this.c = c;
        this.b = b + c;
        for (int i = 0; i < 256; ++i) {
            final int n = this.engineState[i];
            final int n2 = i & 0x3;
            Label_0122: {
                int n3;
                int n4;
                if (n2 != 0) {
                    if (n2 != 1) {
                        if (n2 != 2) {
                            if (n2 != 3) {
                                break Label_0122;
                            }
                            n3 = this.a;
                            n4 = n3 >>> 16;
                        }
                        else {
                            n3 = this.a;
                            n4 = n3 << 2;
                        }
                    }
                    else {
                        n3 = this.a;
                        n4 = n3 >>> 6;
                    }
                }
                else {
                    n3 = this.a;
                    n4 = n3 << 13;
                }
                this.a = (n3 ^ n4);
            }
            final int a = this.a;
            final int[] engineState = this.engineState;
            final int a2 = a + engineState[i + 128 & 0xFF];
            this.a = a2;
            final int n5 = engineState[n >>> 2 & 0xFF] + a2 + this.b;
            engineState[i] = n5;
            this.results[i] = (this.b = engineState[n5 >>> 10 & 0xFF] + n);
        }
    }
    
    private void mix(final int[] array) {
        array[0] ^= array[1] << 11;
        array[3] += array[0];
        array[1] += array[2];
        array[1] ^= array[2] >>> 2;
        array[4] += array[1];
        array[2] += array[3];
        array[2] ^= array[3] << 8;
        array[5] += array[2];
        array[3] += array[4];
        array[3] ^= array[4] >>> 16;
        array[6] += array[3];
        array[4] += array[5];
        array[4] ^= array[5] << 10;
        array[7] += array[4];
        array[5] += array[6];
        array[5] ^= array[6] >>> 4;
        array[0] += array[5];
        array[6] += array[7];
        array[6] ^= array[7] << 8;
        array[1] += array[6];
        array[7] += array[0];
        array[7] ^= array[0] >>> 9;
        array[2] += array[7];
        array[0] += array[1];
    }
    
    private void setKey(final byte[] workingKey) {
        this.workingKey = workingKey;
        if (this.engineState == null) {
            this.engineState = new int[256];
        }
        if (this.results == null) {
            this.results = new int[256];
        }
        for (int i = 0; i < 256; ++i) {
            this.engineState[i] = (this.results[i] = 0);
        }
        this.c = 0;
        this.b = 0;
        this.a = 0;
        this.index = 0;
        final int n = workingKey.length + (workingKey.length & 0x3);
        final byte[] array = new byte[n];
        System.arraycopy(workingKey, 0, array, 0, workingKey.length);
        for (int j = 0; j < n; j += 4) {
            this.results[j >>> 2] = Pack.littleEndianToInt(array, j);
        }
        final int[] array2 = new int[8];
        for (int k = 0; k < 8; ++k) {
            array2[k] = -1640531527;
        }
        for (int l = 0; l < 4; ++l) {
            this.mix(array2);
        }
        for (int n2 = 0; n2 < 2; ++n2) {
            for (int n3 = 0; n3 < 256; n3 += 8) {
                for (int n4 = 0; n4 < 8; ++n4) {
                    final int n5 = array2[n4];
                    int n6;
                    if (n2 < 1) {
                        n6 = this.results[n3 + n4];
                    }
                    else {
                        n6 = this.engineState[n3 + n4];
                    }
                    array2[n4] = n5 + n6;
                }
                this.mix(array2);
                for (int n7 = 0; n7 < 8; ++n7) {
                    this.engineState[n3 + n7] = array2[n7];
                }
            }
        }
        this.isaac();
        this.initialised = true;
    }
    
    @Override
    public String getAlgorithmName() {
        return "ISAAC";
    }
    
    @Override
    public void init(final boolean b, final CipherParameters cipherParameters) {
        if (cipherParameters instanceof KeyParameter) {
            this.setKey(((KeyParameter)cipherParameters).getKey());
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("invalid parameter passed to ISAAC init - ");
        sb.append(cipherParameters.getClass().getName());
        throw new IllegalArgumentException(sb.toString());
    }
    
    @Override
    public int processBytes(final byte[] array, final int n, final int n2, final byte[] array2, final int n3) {
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
                if (this.index == 0) {
                    this.isaac();
                    this.keyStream = Pack.intToBigEndian(this.results);
                }
                final byte[] keyStream = this.keyStream;
                final int index = this.index;
                array2[i + n3] = (byte)(keyStream[index] ^ array[i + n]);
                this.index = (index + 1 & 0x3FF);
            }
            return n2;
        }
        throw new OutputLengthException("output buffer too short");
    }
    
    @Override
    public void reset() {
        this.setKey(this.workingKey);
    }
    
    @Override
    public byte returnByte(final byte b) {
        if (this.index == 0) {
            this.isaac();
            this.keyStream = Pack.intToBigEndian(this.results);
        }
        final byte[] keyStream = this.keyStream;
        final int index = this.index;
        final byte b2 = (byte)(b ^ keyStream[index]);
        this.index = (index + 1 & 0x3FF);
        return b2;
    }
}
