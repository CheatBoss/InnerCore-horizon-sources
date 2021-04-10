package com.android.dx.cf.code;

import com.android.dx.rop.type.*;
import com.android.dx.util.*;

public final class ReturnAddress implements TypeBearer
{
    private final int subroutineAddress;
    
    public ReturnAddress(final int subroutineAddress) {
        if (subroutineAddress < 0) {
            throw new IllegalArgumentException("subroutineAddress < 0");
        }
        this.subroutineAddress = subroutineAddress;
    }
    
    @Override
    public boolean equals(final Object o) {
        final boolean b = o instanceof ReturnAddress;
        boolean b2 = false;
        if (!b) {
            return false;
        }
        if (this.subroutineAddress == ((ReturnAddress)o).subroutineAddress) {
            b2 = true;
        }
        return b2;
    }
    
    @Override
    public int getBasicFrameType() {
        return Type.RETURN_ADDRESS.getBasicFrameType();
    }
    
    @Override
    public int getBasicType() {
        return Type.RETURN_ADDRESS.getBasicType();
    }
    
    @Override
    public TypeBearer getFrameType() {
        return this;
    }
    
    public int getSubroutineAddress() {
        return this.subroutineAddress;
    }
    
    @Override
    public Type getType() {
        return Type.RETURN_ADDRESS;
    }
    
    @Override
    public int hashCode() {
        return this.subroutineAddress;
    }
    
    @Override
    public boolean isConstant() {
        return false;
    }
    
    @Override
    public String toHuman() {
        return this.toString();
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("<addr:");
        sb.append(Hex.u2(this.subroutineAddress));
        sb.append(">");
        return sb.toString();
    }
}
