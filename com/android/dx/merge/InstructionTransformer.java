package com.android.dx.merge;

import com.android.dx.io.*;
import com.android.dx.io.instructions.*;
import com.android.dex.*;

final class InstructionTransformer
{
    private IndexMap indexMap;
    private int mappedAt;
    private DecodedInstruction[] mappedInstructions;
    private final CodeReader reader;
    
    public InstructionTransformer() {
        (this.reader = new CodeReader()).setAllVisitors((CodeReader.Visitor)new GenericVisitor());
        this.reader.setStringVisitor((CodeReader.Visitor)new StringVisitor());
        this.reader.setTypeVisitor((CodeReader.Visitor)new TypeVisitor());
        this.reader.setFieldVisitor((CodeReader.Visitor)new FieldVisitor());
        this.reader.setMethodVisitor((CodeReader.Visitor)new MethodVisitor());
    }
    
    private static void jumboCheck(final boolean b, final int n) {
        if (!b && n > 65535) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Cannot merge new index ");
            sb.append(n);
            sb.append(" into a non-jumbo instruction!");
            throw new DexIndexOverflowException(sb.toString());
        }
    }
    
    public short[] transform(final IndexMap indexMap, final short[] array) throws DexException {
        final DecodedInstruction[] decodeAll = DecodedInstruction.decodeAll(array);
        final int length = decodeAll.length;
        this.indexMap = indexMap;
        this.mappedInstructions = new DecodedInstruction[length];
        int i = 0;
        this.mappedAt = 0;
        this.reader.visitAll(decodeAll);
        final ShortArrayCodeOutput shortArrayCodeOutput = new ShortArrayCodeOutput(length);
        for (DecodedInstruction[] mappedInstructions = this.mappedInstructions; i < mappedInstructions.length; ++i) {
            final DecodedInstruction decodedInstruction = mappedInstructions[i];
            if (decodedInstruction != null) {
                decodedInstruction.encode(shortArrayCodeOutput);
            }
        }
        this.indexMap = null;
        return shortArrayCodeOutput.getArray();
    }
    
    private class FieldVisitor implements Visitor
    {
        @Override
        public void visit(final DecodedInstruction[] array, final DecodedInstruction decodedInstruction) {
            final int adjustField = InstructionTransformer.this.indexMap.adjustField(decodedInstruction.getIndex());
            jumboCheck(decodedInstruction.getOpcode() == 27, adjustField);
            InstructionTransformer.this.mappedInstructions[InstructionTransformer.this.mappedAt++] = decodedInstruction.withIndex(adjustField);
        }
    }
    
    private class GenericVisitor implements Visitor
    {
        @Override
        public void visit(final DecodedInstruction[] array, final DecodedInstruction decodedInstruction) {
            InstructionTransformer.this.mappedInstructions[InstructionTransformer.this.mappedAt++] = decodedInstruction;
        }
    }
    
    private class MethodVisitor implements Visitor
    {
        @Override
        public void visit(final DecodedInstruction[] array, final DecodedInstruction decodedInstruction) {
            final int adjustMethod = InstructionTransformer.this.indexMap.adjustMethod(decodedInstruction.getIndex());
            jumboCheck(decodedInstruction.getOpcode() == 27, adjustMethod);
            InstructionTransformer.this.mappedInstructions[InstructionTransformer.this.mappedAt++] = decodedInstruction.withIndex(adjustMethod);
        }
    }
    
    private class StringVisitor implements Visitor
    {
        @Override
        public void visit(final DecodedInstruction[] array, final DecodedInstruction decodedInstruction) {
            final int adjustString = InstructionTransformer.this.indexMap.adjustString(decodedInstruction.getIndex());
            jumboCheck(decodedInstruction.getOpcode() == 27, adjustString);
            InstructionTransformer.this.mappedInstructions[InstructionTransformer.this.mappedAt++] = decodedInstruction.withIndex(adjustString);
        }
    }
    
    private class TypeVisitor implements Visitor
    {
        @Override
        public void visit(final DecodedInstruction[] array, final DecodedInstruction decodedInstruction) {
            final int adjustType = InstructionTransformer.this.indexMap.adjustType(decodedInstruction.getIndex());
            jumboCheck(decodedInstruction.getOpcode() == 27, adjustType);
            InstructionTransformer.this.mappedInstructions[InstructionTransformer.this.mappedAt++] = decodedInstruction.withIndex(adjustType);
        }
    }
}
