package com.android.dx.rop.code;

public final class ConservativeTranslationAdvice implements TranslationAdvice
{
    public static final ConservativeTranslationAdvice THE_ONE;
    
    static {
        THE_ONE = new ConservativeTranslationAdvice();
    }
    
    private ConservativeTranslationAdvice() {
    }
    
    @Override
    public int getMaxOptimalRegisterCount() {
        return Integer.MAX_VALUE;
    }
    
    @Override
    public boolean hasConstantOperation(final Rop rop, final RegisterSpec registerSpec, final RegisterSpec registerSpec2) {
        return false;
    }
    
    @Override
    public boolean requiresSourcesInOrder(final Rop rop, final RegisterSpecList list) {
        return false;
    }
}
