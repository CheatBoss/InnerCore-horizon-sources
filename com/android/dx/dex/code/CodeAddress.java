package com.android.dx.dex.code;

import com.android.dx.rop.code.*;

public final class CodeAddress extends ZeroSizeInsn
{
    private final boolean bindsClosely;
    
    public CodeAddress(final SourcePosition sourcePosition) {
        this(sourcePosition, false);
    }
    
    public CodeAddress(final SourcePosition sourcePosition, final boolean bindsClosely) {
        super(sourcePosition);
        this.bindsClosely = bindsClosely;
    }
    
    @Override
    protected String argString() {
        return null;
    }
    
    public boolean getBindsClosely() {
        return this.bindsClosely;
    }
    
    @Override
    protected String listingString0(final boolean b) {
        return "code-address";
    }
    
    @Override
    public final DalvInsn withRegisters(final RegisterSpecList list) {
        return new CodeAddress(this.getPosition());
    }
}
