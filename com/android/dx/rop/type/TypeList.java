package com.android.dx.rop.type;

public interface TypeList
{
    Type getType(final int p0);
    
    int getWordCount();
    
    boolean isMutable();
    
    int size();
    
    TypeList withAddedType(final Type p0);
}
