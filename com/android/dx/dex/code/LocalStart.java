package com.android.dx.dex.code;

import com.android.dx.ssa.*;
import com.android.dx.rop.code.*;

public final class LocalStart extends ZeroSizeInsn
{
    private final RegisterSpec local;
    
    public LocalStart(final SourcePosition sourcePosition, final RegisterSpec local) {
        super(sourcePosition);
        if (local == null) {
            throw new NullPointerException("local == null");
        }
        this.local = local;
    }
    
    public static String localString(final RegisterSpec registerSpec) {
        final StringBuilder sb = new StringBuilder();
        sb.append(registerSpec.regString());
        sb.append(' ');
        sb.append(registerSpec.getLocalItem().toString());
        sb.append(": ");
        sb.append(registerSpec.getTypeBearer().toHuman());
        return sb.toString();
    }
    
    @Override
    protected String argString() {
        return this.local.toString();
    }
    
    public RegisterSpec getLocal() {
        return this.local;
    }
    
    @Override
    protected String listingString0(final boolean b) {
        final StringBuilder sb = new StringBuilder();
        sb.append("local-start ");
        sb.append(localString(this.local));
        return sb.toString();
    }
    
    @Override
    public DalvInsn withMapper(final RegisterMapper registerMapper) {
        return new LocalStart(this.getPosition(), registerMapper.map(this.local));
    }
    
    @Override
    public DalvInsn withRegisterOffset(final int n) {
        return new LocalStart(this.getPosition(), this.local.withOffset(n));
    }
    
    @Override
    public DalvInsn withRegisters(final RegisterSpecList list) {
        return new LocalStart(this.getPosition(), this.local);
    }
}
