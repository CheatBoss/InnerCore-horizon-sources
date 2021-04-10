package com.android.dx.io.instructions;

public interface CodeCursor
{
    int baseAddressForCursor();
    
    int cursor();
    
    void setBaseAddress(final int p0, final int p1);
}
