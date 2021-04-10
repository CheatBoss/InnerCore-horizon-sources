package com.android.dx.dex.code;

import com.android.dx.rop.cst.*;
import com.android.dx.rop.code.*;

public final class CstInsn extends FixedSizeInsn
{
    private int classIndex;
    private final Constant constant;
    private int index;
    
    public CstInsn(final Dop dop, final SourcePosition sourcePosition, final RegisterSpecList list, final Constant constant) {
        super(dop, sourcePosition, list);
        if (constant == null) {
            throw new NullPointerException("constant == null");
        }
        this.constant = constant;
        this.index = -1;
        this.classIndex = -1;
    }
    
    @Override
    protected String argString() {
        return this.constant.toHuman();
    }
    
    public int getClassIndex() {
        if (this.classIndex < 0) {
            throw new RuntimeException("class index not yet set");
        }
        return this.classIndex;
    }
    
    public Constant getConstant() {
        return this.constant;
    }
    
    public int getIndex() {
        if (this.index < 0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("index not yet set for ");
            sb.append(this.constant);
            throw new RuntimeException(sb.toString());
        }
        return this.index;
    }
    
    public boolean hasClassIndex() {
        return this.classIndex >= 0;
    }
    
    public boolean hasIndex() {
        return this.index >= 0;
    }
    
    public void setClassIndex(final int classIndex) {
        if (classIndex < 0) {
            throw new IllegalArgumentException("index < 0");
        }
        if (this.classIndex >= 0) {
            throw new RuntimeException("class index already set");
        }
        this.classIndex = classIndex;
    }
    
    public void setIndex(final int index) {
        if (index < 0) {
            throw new IllegalArgumentException("index < 0");
        }
        if (this.index >= 0) {
            throw new RuntimeException("index already set");
        }
        this.index = index;
    }
    
    @Override
    public DalvInsn withOpcode(final Dop dop) {
        final CstInsn cstInsn = new CstInsn(dop, this.getPosition(), this.getRegisters(), this.constant);
        if (this.index >= 0) {
            cstInsn.setIndex(this.index);
        }
        if (this.classIndex >= 0) {
            cstInsn.setClassIndex(this.classIndex);
        }
        return cstInsn;
    }
    
    @Override
    public DalvInsn withRegisters(final RegisterSpecList list) {
        final CstInsn cstInsn = new CstInsn(this.getOpcode(), this.getPosition(), list, this.constant);
        if (this.index >= 0) {
            cstInsn.setIndex(this.index);
        }
        if (this.classIndex >= 0) {
            cstInsn.setClassIndex(this.classIndex);
        }
        return cstInsn;
    }
}
