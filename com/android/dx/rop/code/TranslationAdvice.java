package com.android.dx.rop.code;

public interface TranslationAdvice
{
    int getMaxOptimalRegisterCount();
    
    boolean hasConstantOperation(final Rop p0, final RegisterSpec p1, final RegisterSpec p2);
    
    boolean requiresSourcesInOrder(final Rop p0, final RegisterSpecList p1);
}
