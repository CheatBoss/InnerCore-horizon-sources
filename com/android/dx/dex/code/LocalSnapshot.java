package com.android.dx.dex.code;

import com.android.dx.ssa.*;
import com.android.dx.rop.code.*;

public final class LocalSnapshot extends ZeroSizeInsn
{
    private final RegisterSpecSet locals;
    
    public LocalSnapshot(final SourcePosition sourcePosition, final RegisterSpecSet locals) {
        super(sourcePosition);
        if (locals == null) {
            throw new NullPointerException("locals == null");
        }
        this.locals = locals;
    }
    
    @Override
    protected String argString() {
        return this.locals.toString();
    }
    
    public RegisterSpecSet getLocals() {
        return this.locals;
    }
    
    @Override
    protected String listingString0(final boolean b) {
        final int size = this.locals.size();
        final int maxSize = this.locals.getMaxSize();
        final StringBuffer sb = new StringBuffer(size * 40 + 100);
        sb.append("local-snapshot");
        for (int i = 0; i < maxSize; ++i) {
            final RegisterSpec value = this.locals.get(i);
            if (value != null) {
                sb.append("\n  ");
                sb.append(LocalStart.localString(value));
            }
        }
        return sb.toString();
    }
    
    @Override
    public DalvInsn withMapper(final RegisterMapper registerMapper) {
        return new LocalSnapshot(this.getPosition(), registerMapper.map(this.locals));
    }
    
    @Override
    public DalvInsn withRegisterOffset(final int n) {
        return new LocalSnapshot(this.getPosition(), this.locals.withOffset(n));
    }
    
    @Override
    public DalvInsn withRegisters(final RegisterSpecList list) {
        return new LocalSnapshot(this.getPosition(), this.locals);
    }
}
