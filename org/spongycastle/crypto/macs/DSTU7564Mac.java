package org.spongycastle.crypto.macs;

import org.spongycastle.crypto.digests.*;
import org.spongycastle.util.*;
import org.spongycastle.crypto.*;
import org.spongycastle.crypto.params.*;

public class DSTU7564Mac implements Mac
{
    private static final int BITS_IN_BYTE = 8;
    private DSTU7564Digest engine;
    private long inputLength;
    private byte[] invertedKey;
    private int macSize;
    private byte[] paddedKey;
    
    public DSTU7564Mac(final int n) {
        this.engine = new DSTU7564Digest(n);
        this.macSize = n / 8;
        this.paddedKey = null;
        this.invertedKey = null;
    }
    
    private void pad() {
        int n2;
        final int n = n2 = this.engine.getByteLength() - (int)(this.inputLength % this.engine.getByteLength());
        if (n < 13) {
            n2 = n + this.engine.getByteLength();
        }
        final byte[] array = new byte[n2];
        array[0] = -128;
        Pack.longToLittleEndian(this.inputLength * 8L, array, n2 - 12);
        this.engine.update(array, 0, n2);
    }
    
    private byte[] padKey(final byte[] array) {
        int n2;
        final int n = n2 = (array.length + this.engine.getByteLength() - 1) / this.engine.getByteLength() * this.engine.getByteLength();
        if (this.engine.getByteLength() - array.length % this.engine.getByteLength() < 13) {
            n2 = n + this.engine.getByteLength();
        }
        final byte[] array2 = new byte[n2];
        System.arraycopy(array, 0, array2, 0, array.length);
        array2[array.length] = -128;
        Pack.intToLittleEndian(array.length * 8, array2, n2 - 12);
        return array2;
    }
    
    @Override
    public int doFinal(final byte[] array, final int n) throws DataLengthException, IllegalStateException {
        if (this.paddedKey == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append(this.getAlgorithmName());
            sb.append(" not initialised");
            throw new IllegalStateException(sb.toString());
        }
        if (array.length - n >= this.macSize) {
            this.pad();
            final DSTU7564Digest engine = this.engine;
            final byte[] invertedKey = this.invertedKey;
            engine.update(invertedKey, 0, invertedKey.length);
            this.inputLength = 0L;
            return this.engine.doFinal(array, n);
        }
        throw new OutputLengthException("Output buffer too short");
    }
    
    @Override
    public String getAlgorithmName() {
        return "DSTU7564Mac";
    }
    
    @Override
    public int getMacSize() {
        return this.macSize;
    }
    
    @Override
    public void init(final CipherParameters cipherParameters) throws IllegalArgumentException {
        if (cipherParameters instanceof KeyParameter) {
            final byte[] key = ((KeyParameter)cipherParameters).getKey();
            this.invertedKey = new byte[key.length];
            this.paddedKey = this.padKey(key);
            int n = 0;
            while (true) {
                final byte[] invertedKey = this.invertedKey;
                if (n >= invertedKey.length) {
                    break;
                }
                invertedKey[n] = (byte)~key[n];
                ++n;
            }
            final DSTU7564Digest engine = this.engine;
            final byte[] paddedKey = this.paddedKey;
            engine.update(paddedKey, 0, paddedKey.length);
            return;
        }
        throw new IllegalArgumentException("Bad parameter passed");
    }
    
    @Override
    public void reset() {
        this.inputLength = 0L;
        this.engine.reset();
        final byte[] paddedKey = this.paddedKey;
        if (paddedKey != null) {
            this.engine.update(paddedKey, 0, paddedKey.length);
        }
    }
    
    @Override
    public void update(final byte b) throws IllegalStateException {
        this.engine.update(b);
        ++this.inputLength;
    }
    
    @Override
    public void update(final byte[] array, final int n, final int n2) throws DataLengthException, IllegalStateException {
        if (array.length - n < n2) {
            throw new DataLengthException("Input buffer too short");
        }
        if (this.paddedKey != null) {
            this.engine.update(array, n, n2);
            this.inputLength += n2;
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(this.getAlgorithmName());
        sb.append(" not initialised");
        throw new IllegalStateException(sb.toString());
    }
}
