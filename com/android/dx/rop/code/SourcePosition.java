package com.android.dx.rop.code;

import com.android.dx.rop.cst.*;
import com.android.dx.util.*;

public final class SourcePosition
{
    public static final SourcePosition NO_INFO;
    private final int address;
    private final int line;
    private final CstString sourceFile;
    
    static {
        NO_INFO = new SourcePosition(null, -1, -1);
    }
    
    public SourcePosition(final CstString sourceFile, final int address, final int line) {
        if (address < -1) {
            throw new IllegalArgumentException("address < -1");
        }
        if (line < -1) {
            throw new IllegalArgumentException("line < -1");
        }
        this.sourceFile = sourceFile;
        this.address = address;
        this.line = line;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof SourcePosition)) {
            return false;
        }
        if (this == o) {
            return true;
        }
        final SourcePosition sourcePosition = (SourcePosition)o;
        return this.address == sourcePosition.address && this.sameLineAndFile(sourcePosition);
    }
    
    public int getAddress() {
        return this.address;
    }
    
    public int getLine() {
        return this.line;
    }
    
    public CstString getSourceFile() {
        return this.sourceFile;
    }
    
    @Override
    public int hashCode() {
        return this.sourceFile.hashCode() + this.address + this.line;
    }
    
    public boolean sameLine(final SourcePosition sourcePosition) {
        return this.line == sourcePosition.line;
    }
    
    public boolean sameLineAndFile(final SourcePosition sourcePosition) {
        return this.line == sourcePosition.line && (this.sourceFile == sourcePosition.sourceFile || (this.sourceFile != null && this.sourceFile.equals(sourcePosition.sourceFile)));
    }
    
    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer(50);
        if (this.sourceFile != null) {
            sb.append(this.sourceFile.toHuman());
            sb.append(":");
        }
        if (this.line >= 0) {
            sb.append(this.line);
        }
        sb.append('@');
        if (this.address < 0) {
            sb.append("????");
        }
        else {
            sb.append(Hex.u2(this.address));
        }
        return sb.toString();
    }
}
