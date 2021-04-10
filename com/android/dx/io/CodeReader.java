package com.android.dx.io;

import com.android.dx.io.instructions.*;
import com.android.dex.*;

public final class CodeReader
{
    private Visitor fallbackVisitor;
    private Visitor fieldVisitor;
    private Visitor methodVisitor;
    private Visitor stringVisitor;
    private Visitor typeVisitor;
    
    public CodeReader() {
        this.fallbackVisitor = null;
        this.stringVisitor = null;
        this.typeVisitor = null;
        this.fieldVisitor = null;
        this.methodVisitor = null;
    }
    
    private void callVisit(final DecodedInstruction[] array, final DecodedInstruction decodedInstruction) {
        Visitor visitor = null;
        switch (OpcodeInfo.getIndexType(decodedInstruction.getOpcode())) {
            case METHOD_REF: {
                visitor = this.methodVisitor;
                break;
            }
            case FIELD_REF: {
                visitor = this.fieldVisitor;
                break;
            }
            case TYPE_REF: {
                visitor = this.typeVisitor;
                break;
            }
            case STRING_REF: {
                visitor = this.stringVisitor;
                break;
            }
        }
        Visitor fallbackVisitor = visitor;
        if (visitor == null) {
            fallbackVisitor = this.fallbackVisitor;
        }
        if (fallbackVisitor != null) {
            fallbackVisitor.visit(array, decodedInstruction);
        }
    }
    
    public void setAllVisitors(final Visitor methodVisitor) {
        this.fallbackVisitor = methodVisitor;
        this.stringVisitor = methodVisitor;
        this.typeVisitor = methodVisitor;
        this.fieldVisitor = methodVisitor;
        this.methodVisitor = methodVisitor;
    }
    
    public void setFallbackVisitor(final Visitor fallbackVisitor) {
        this.fallbackVisitor = fallbackVisitor;
    }
    
    public void setFieldVisitor(final Visitor fieldVisitor) {
        this.fieldVisitor = fieldVisitor;
    }
    
    public void setMethodVisitor(final Visitor methodVisitor) {
        this.methodVisitor = methodVisitor;
    }
    
    public void setStringVisitor(final Visitor stringVisitor) {
        this.stringVisitor = stringVisitor;
    }
    
    public void setTypeVisitor(final Visitor typeVisitor) {
        this.typeVisitor = typeVisitor;
    }
    
    public void visitAll(final DecodedInstruction[] array) throws DexException {
        for (int length = array.length, i = 0; i < length; ++i) {
            final DecodedInstruction decodedInstruction = array[i];
            if (decodedInstruction != null) {
                this.callVisit(array, decodedInstruction);
            }
        }
    }
    
    public void visitAll(final short[] array) throws DexException {
        this.visitAll(DecodedInstruction.decodeAll(array));
    }
    
    public interface Visitor
    {
        void visit(final DecodedInstruction[] p0, final DecodedInstruction p1);
    }
}
