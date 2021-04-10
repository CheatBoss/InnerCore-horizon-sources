package com.android.dx.io.instructions;

public final class ShortArrayCodeOutput extends BaseCodeCursor implements CodeOutput
{
    private final short[] array;
    
    public ShortArrayCodeOutput(final int n) {
        if (n < 0) {
            throw new IllegalArgumentException("maxSize < 0");
        }
        this.array = new short[n];
    }
    
    public short[] getArray() {
        final int cursor = this.cursor();
        if (cursor == this.array.length) {
            return this.array;
        }
        final short[] array = new short[cursor];
        System.arraycopy(this.array, 0, array, 0, cursor);
        return array;
    }
    
    @Override
    public void write(final short n) {
        this.array[this.cursor()] = n;
        this.advance(1);
    }
    
    @Override
    public void write(final short n, final short n2) {
        this.write(n);
        this.write(n2);
    }
    
    @Override
    public void write(final short n, final short n2, final short n3) {
        this.write(n);
        this.write(n2);
        this.write(n3);
    }
    
    @Override
    public void write(final short n, final short n2, final short n3, final short n4) {
        this.write(n);
        this.write(n2);
        this.write(n3);
        this.write(n4);
    }
    
    @Override
    public void write(final short n, final short n2, final short n3, final short n4, final short n5) {
        this.write(n);
        this.write(n2);
        this.write(n3);
        this.write(n4);
        this.write(n5);
    }
    
    @Override
    public void write(final byte[] array) {
        int n = 0;
        int n2 = 1;
        for (int length = array.length, i = 0; i < length; ++i) {
            final byte b = array[i];
            if (n2 != 0) {
                n = (b & 0xFF);
                n2 = 0;
            }
            else {
                n |= b << 8;
                this.write((short)n);
                n2 = 1;
            }
        }
        if (n2 == 0) {
            this.write((short)n);
        }
    }
    
    @Override
    public void write(final int[] array) {
        for (int length = array.length, i = 0; i < length; ++i) {
            this.writeInt(array[i]);
        }
    }
    
    @Override
    public void write(final long[] array) {
        for (int length = array.length, i = 0; i < length; ++i) {
            this.writeLong(array[i]);
        }
    }
    
    @Override
    public void write(final short[] array) {
        for (int length = array.length, i = 0; i < length; ++i) {
            this.write(array[i]);
        }
    }
    
    @Override
    public void writeInt(final int n) {
        this.write((short)n);
        this.write((short)(n >> 16));
    }
    
    @Override
    public void writeLong(final long n) {
        this.write((short)n);
        this.write((short)(n >> 16));
        this.write((short)(n >> 32));
        this.write((short)(n >> 48));
    }
}
