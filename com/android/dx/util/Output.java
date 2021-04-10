package com.android.dx.util;

import com.android.dex.util.*;

public interface Output extends ByteOutput
{
    void alignTo(final int p0);
    
    void assertCursor(final int p0);
    
    int getCursor();
    
    void write(final ByteArray p0);
    
    void write(final byte[] p0);
    
    void write(final byte[] p0, final int p1, final int p2);
    
    void writeByte(final int p0);
    
    void writeInt(final int p0);
    
    void writeLong(final long p0);
    
    void writeShort(final int p0);
    
    int writeSleb128(final int p0);
    
    int writeUleb128(final int p0);
    
    void writeZeroes(final int p0);
}
