package com.android.dx.io.instructions;

import java.io.*;

public final class ShortArrayCodeInput extends BaseCodeCursor implements CodeInput
{
    private final short[] array;
    
    public ShortArrayCodeInput(final short[] array) {
        if (array == null) {
            throw new NullPointerException("array == null");
        }
        this.array = array;
    }
    
    @Override
    public boolean hasMore() {
        return this.cursor() < this.array.length;
    }
    
    @Override
    public int read() throws EOFException {
        try {
            final short n = this.array[this.cursor()];
            this.advance(1);
            return 0xFFFF & n;
        }
        catch (ArrayIndexOutOfBoundsException ex) {
            throw new EOFException();
        }
    }
    
    @Override
    public int readInt() throws EOFException {
        return this.read() << 16 | this.read();
    }
    
    @Override
    public long readLong() throws EOFException {
        return (long)this.read() | (long)this.read() << 16 | (long)this.read() << 32 | (long)this.read() << 48;
    }
}
