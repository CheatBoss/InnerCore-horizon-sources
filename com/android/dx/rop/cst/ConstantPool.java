package com.android.dx.rop.cst;

public interface ConstantPool
{
    Constant get(final int p0);
    
    Constant get0Ok(final int p0);
    
    Constant[] getEntries();
    
    Constant getOrNull(final int p0);
    
    int size();
}
