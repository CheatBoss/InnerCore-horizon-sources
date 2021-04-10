package com.android.dx.rop.code;

import com.android.dx.rop.type.*;
import com.android.dx.rop.cst.*;

public final class DexTranslationAdvice implements TranslationAdvice
{
    private static final int MIN_INVOKE_IN_ORDER = 6;
    public static final DexTranslationAdvice NO_SOURCES_IN_ORDER;
    public static final DexTranslationAdvice THE_ONE;
    private final boolean disableSourcesInOrder;
    
    static {
        THE_ONE = new DexTranslationAdvice();
        NO_SOURCES_IN_ORDER = new DexTranslationAdvice(true);
    }
    
    private DexTranslationAdvice() {
        this.disableSourcesInOrder = false;
    }
    
    private DexTranslationAdvice(final boolean disableSourcesInOrder) {
        this.disableSourcesInOrder = disableSourcesInOrder;
    }
    
    private int totalRopWidth(final RegisterSpecList list) {
        final int size = list.size();
        int n = 0;
        for (int i = 0; i < size; ++i) {
            n += list.get(i).getCategory();
        }
        return n;
    }
    
    @Override
    public int getMaxOptimalRegisterCount() {
        return 16;
    }
    
    @Override
    public boolean hasConstantOperation(final Rop rop, final RegisterSpec registerSpec, final RegisterSpec registerSpec2) {
        if (registerSpec.getType() != Type.INT) {
            return false;
        }
        if (!(registerSpec2.getTypeBearer() instanceof CstInteger)) {
            return registerSpec.getTypeBearer() instanceof CstInteger && rop.getOpcode() == 15 && ((CstInteger)registerSpec.getTypeBearer()).fitsIn16Bits();
        }
        final CstInteger cstInteger = (CstInteger)registerSpec2.getTypeBearer();
        switch (rop.getOpcode()) {
            default: {
                return false;
            }
            case 23:
            case 24:
            case 25: {
                return cstInteger.fitsIn8Bits();
            }
            case 15: {
                return CstInteger.make(-cstInteger.getValue()).fitsIn16Bits();
            }
            case 14:
            case 16:
            case 17:
            case 18:
            case 20:
            case 21:
            case 22: {
                return cstInteger.fitsIn16Bits();
            }
        }
    }
    
    @Override
    public boolean requiresSourcesInOrder(final Rop rop, final RegisterSpecList list) {
        return !this.disableSourcesInOrder && rop.isCallLike() && this.totalRopWidth(list) >= 6;
    }
}
