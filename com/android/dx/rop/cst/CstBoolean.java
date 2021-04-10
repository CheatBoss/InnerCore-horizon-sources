package com.android.dx.rop.cst;

import com.android.dx.rop.type.*;

public final class CstBoolean extends CstLiteral32
{
    public static final CstBoolean VALUE_FALSE;
    public static final CstBoolean VALUE_TRUE;
    
    static {
        VALUE_FALSE = new CstBoolean(false);
        VALUE_TRUE = new CstBoolean(true);
    }
    
    private CstBoolean(final boolean b) {
        throw new Runtime("d2j fail translate: java.lang.RuntimeException: can not merge I and Z\r\n\tat com.googlecode.dex2jar.ir.TypeClass.merge(TypeClass.java:100)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeRef.updateTypeClass(TypeTransformer.java:174)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.copyTypes(TypeTransformer.java:311)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.fixTypes(TypeTransformer.java:226)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.analyze(TypeTransformer.java:207)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer.transform(TypeTransformer.java:44)\r\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:162)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\r\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\r\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\r\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\r\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\r\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\r\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\r\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\r\n");
    }
    
    public static CstBoolean make(final int n) {
        if (n == 0) {
            return CstBoolean.VALUE_FALSE;
        }
        if (n == 1) {
            return CstBoolean.VALUE_TRUE;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("bogus value: ");
        sb.append(n);
        throw new IllegalArgumentException(sb.toString());
    }
    
    public static CstBoolean make(final boolean b) {
        if (b) {
            return CstBoolean.VALUE_TRUE;
        }
        return CstBoolean.VALUE_FALSE;
    }
    
    @Override
    public Type getType() {
        return Type.BOOLEAN;
    }
    
    public boolean getValue() {
        return this.getIntBits() != 0;
    }
    
    @Override
    public String toHuman() {
        if (this.getValue()) {
            return "true";
        }
        return "false";
    }
    
    @Override
    public String toString() {
        if (this.getValue()) {
            return "boolean{true}";
        }
        return "boolean{false}";
    }
    
    @Override
    public String typeName() {
        return "boolean";
    }
}
