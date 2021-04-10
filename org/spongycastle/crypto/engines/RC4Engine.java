package org.spongycastle.crypto.engines;

import org.spongycastle.crypto.params.*;
import org.spongycastle.crypto.*;

public class RC4Engine implements StreamCipher
{
    private static final int STATE_LENGTH = 256;
    private byte[] engineState;
    private byte[] workingKey;
    private int x;
    private int y;
    
    public RC4Engine() {
        this.engineState = null;
        this.x = 0;
        this.y = 0;
        this.workingKey = null;
    }
    
    private void setKey(final byte[] workingKey) {
        this.workingKey = workingKey;
        final int n = 0;
        this.x = 0;
        this.y = 0;
        if (this.engineState == null) {
            this.engineState = new byte[256];
        }
        for (int i = 0; i < 256; ++i) {
            this.engineState[i] = (byte)i;
        }
        int n2 = 0;
        int n3 = 0;
        for (int j = n; j < 256; ++j) {
            final byte b = workingKey[n2];
            final byte[] engineState = this.engineState;
            n3 = ((b & 0xFF) + engineState[j] + n3 & 0xFF);
            final byte b2 = engineState[j];
            engineState[j] = engineState[n3];
            engineState[n3] = b2;
            n2 = (n2 + 1) % workingKey.length;
        }
    }
    
    @Override
    public String getAlgorithmName() {
        return "RC4";
    }
    
    @Override
    public void init(final boolean b, final CipherParameters cipherParameters) {
        if (cipherParameters instanceof KeyParameter) {
            this.setKey(this.workingKey = ((KeyParameter)cipherParameters).getKey());
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("invalid parameter passed to RC4 init - ");
        sb.append(cipherParameters.getClass().getName());
        throw new IllegalArgumentException(sb.toString());
    }
    
    @Override
    public int processBytes(final byte[] array, final int n, final int n2, final byte[] array2, final int n3) {
        if (n + n2 > array.length) {
            throw new DataLengthException("input buffer too short");
        }
        if (n3 + n2 <= array2.length) {
            for (int i = 0; i < n2; ++i) {
                final int x = this.x + 1 & 0xFF;
                this.x = x;
                final byte[] engineState = this.engineState;
                final int y = engineState[x] + this.y & 0xFF;
                this.y = y;
                final byte b = engineState[x];
                engineState[x] = engineState[y];
                engineState[y] = b;
                array2[i + n3] = (byte)(engineState[engineState[x] + engineState[y] & 0xFF] ^ array[i + n]);
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
        final int x = this.x + 1 & 0xFF;
        this.x = x;
        final byte[] engineState = this.engineState;
        final int y = engineState[x] + this.y & 0xFF;
        this.y = y;
        final byte b2 = engineState[x];
        engineState[x] = engineState[y];
        engineState[y] = b2;
        return (byte)(b ^ engineState[engineState[x] + engineState[y] & 0xFF]);
    }
}
