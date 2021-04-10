package org.spongycastle.crypto.prng;

import java.security.*;
import java.io.*;

public class FixedSecureRandom extends SecureRandom
{
    private byte[] _data;
    private int _index;
    private int _intPad;
    
    public FixedSecureRandom(final boolean b, final byte[] array) {
        this(b, new byte[][] { array });
    }
    
    public FixedSecureRandom(final boolean b, final byte[][] array) {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int i = 0;
        while (i != array.length) {
            try {
                byteArrayOutputStream.write(array[i]);
                ++i;
                continue;
            }
            catch (IOException ex) {
                throw new IllegalArgumentException("can't save value array.");
            }
            break;
        }
        final byte[] byteArray = byteArrayOutputStream.toByteArray();
        this._data = byteArray;
        if (b) {
            this._intPad = byteArray.length % 4;
        }
    }
    
    public FixedSecureRandom(final byte[] array) {
        this(false, new byte[][] { array });
    }
    
    public FixedSecureRandom(final byte[][] array) {
        this(false, array);
    }
    
    private int nextValue() {
        return this._data[this._index++] & 0xFF;
    }
    
    @Override
    public byte[] generateSeed(final int n) {
        final byte[] array = new byte[n];
        this.nextBytes(array);
        return array;
    }
    
    public boolean isExhausted() {
        return this._index == this._data.length;
    }
    
    @Override
    public void nextBytes(final byte[] array) {
        System.arraycopy(this._data, this._index, array, 0, array.length);
        this._index += array.length;
    }
    
    @Override
    public int nextInt() {
        int n = this.nextValue() << 24 | 0x0 | this.nextValue() << 16;
        final int intPad = this._intPad;
        if (intPad == 2) {
            this._intPad = intPad - 1;
        }
        else {
            n |= this.nextValue() << 8;
        }
        final int intPad2 = this._intPad;
        if (intPad2 == 1) {
            this._intPad = intPad2 - 1;
            return n;
        }
        return n | this.nextValue();
    }
    
    @Override
    public long nextLong() {
        return (long)this.nextValue() << 56 | 0x0L | (long)this.nextValue() << 48 | (long)this.nextValue() << 40 | (long)this.nextValue() << 32 | (long)this.nextValue() << 24 | (long)this.nextValue() << 16 | (long)this.nextValue() << 8 | (long)this.nextValue();
    }
}
