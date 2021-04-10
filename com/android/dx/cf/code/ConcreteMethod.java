package com.android.dx.cf.code;

import com.android.dx.cf.attrib.*;
import com.android.dx.cf.iface.*;
import com.android.dx.rop.type.*;
import com.android.dx.rop.cst.*;
import com.android.dx.rop.code.*;

public final class ConcreteMethod implements Method
{
    private final boolean accSuper;
    private final AttCode attCode;
    private final LineNumberList lineNumbers;
    private final LocalVariableList localVariables;
    private final Method method;
    private final CstString sourceFile;
    
    public ConcreteMethod(final Method method, final int n, final CstString sourceFile, final boolean b, final boolean b2) {
        this.method = method;
        this.accSuper = ((n & 0x20) != 0x0);
        this.sourceFile = sourceFile;
        this.attCode = (AttCode)method.getAttributes().findFirst("Code");
        final AttributeList attributes = this.attCode.getAttributes();
        LineNumberList empty;
        LineNumberList concat = empty = LineNumberList.EMPTY;
        if (b) {
            AttLineNumberTable attLineNumberTable = (AttLineNumberTable)attributes.findFirst("LineNumberTable");
            while (true) {
                empty = concat;
                if (attLineNumberTable == null) {
                    break;
                }
                concat = LineNumberList.concat(concat, attLineNumberTable.getLineNumbers());
                attLineNumberTable = (AttLineNumberTable)attributes.findNext(attLineNumberTable);
            }
        }
        this.lineNumbers = empty;
        LocalVariableList localVariables;
        LocalVariableList concat2 = localVariables = LocalVariableList.EMPTY;
        if (b2) {
            for (AttLocalVariableTable attLocalVariableTable = (AttLocalVariableTable)attributes.findFirst("LocalVariableTable"); attLocalVariableTable != null; attLocalVariableTable = (AttLocalVariableTable)attributes.findNext(attLocalVariableTable)) {
                concat2 = LocalVariableList.concat(concat2, attLocalVariableTable.getLocalVariables());
            }
            LocalVariableList list = LocalVariableList.EMPTY;
            for (AttLocalVariableTypeTable attLocalVariableTypeTable = (AttLocalVariableTypeTable)attributes.findFirst("LocalVariableTypeTable"); attLocalVariableTypeTable != null; attLocalVariableTypeTable = (AttLocalVariableTypeTable)attributes.findNext(attLocalVariableTypeTable)) {
                list = LocalVariableList.concat(list, attLocalVariableTypeTable.getLocalVariables());
            }
            localVariables = concat2;
            if (list.size() != 0) {
                localVariables = LocalVariableList.mergeDescriptorsAndSignatures(concat2, list);
            }
        }
        this.localVariables = localVariables;
    }
    
    public ConcreteMethod(final Method method, final ClassFile classFile, final boolean b, final boolean b2) {
        this(method, classFile.getAccessFlags(), classFile.getSourceFile(), b, b2);
    }
    
    public boolean getAccSuper() {
        return this.accSuper;
    }
    
    @Override
    public int getAccessFlags() {
        return this.method.getAccessFlags();
    }
    
    @Override
    public AttributeList getAttributes() {
        return this.method.getAttributes();
    }
    
    public ByteCatchList getCatches() {
        return this.attCode.getCatches();
    }
    
    public BytecodeArray getCode() {
        return this.attCode.getCode();
    }
    
    @Override
    public CstType getDefiningClass() {
        return this.method.getDefiningClass();
    }
    
    @Override
    public CstString getDescriptor() {
        return this.method.getDescriptor();
    }
    
    @Override
    public Prototype getEffectiveDescriptor() {
        return this.method.getEffectiveDescriptor();
    }
    
    public LineNumberList getLineNumbers() {
        return this.lineNumbers;
    }
    
    public LocalVariableList getLocalVariables() {
        return this.localVariables;
    }
    
    public int getMaxLocals() {
        return this.attCode.getMaxLocals();
    }
    
    public int getMaxStack() {
        return this.attCode.getMaxStack();
    }
    
    @Override
    public CstString getName() {
        return this.method.getName();
    }
    
    @Override
    public CstNat getNat() {
        return this.method.getNat();
    }
    
    public SourcePosition makeSourcePosistion(final int n) {
        return new SourcePosition(this.sourceFile, n, this.lineNumbers.pcToLine(n));
    }
}
