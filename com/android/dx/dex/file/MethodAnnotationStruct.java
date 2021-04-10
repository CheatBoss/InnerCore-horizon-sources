package com.android.dx.dex.file;

import com.android.dx.rop.cst.*;
import com.android.dx.rop.annotation.*;
import com.android.dx.util.*;

public final class MethodAnnotationStruct implements ToHuman, Comparable<MethodAnnotationStruct>
{
    private AnnotationSetItem annotations;
    private final CstMethodRef method;
    
    public MethodAnnotationStruct(final CstMethodRef method, final AnnotationSetItem annotations) {
        if (method == null) {
            throw new NullPointerException("method == null");
        }
        if (annotations == null) {
            throw new NullPointerException("annotations == null");
        }
        this.method = method;
        this.annotations = annotations;
    }
    
    public void addContents(final DexFile dexFile) {
        final MethodIdsSection methodIds = dexFile.getMethodIds();
        final MixedItemSection wordData = dexFile.getWordData();
        methodIds.intern(this.method);
        this.annotations = wordData.intern(this.annotations);
    }
    
    @Override
    public int compareTo(final MethodAnnotationStruct methodAnnotationStruct) {
        return this.method.compareTo((Constant)methodAnnotationStruct.method);
    }
    
    @Override
    public boolean equals(final Object o) {
        return o instanceof MethodAnnotationStruct && this.method.equals(((MethodAnnotationStruct)o).method);
    }
    
    public Annotations getAnnotations() {
        return this.annotations.getAnnotations();
    }
    
    public CstMethodRef getMethod() {
        return this.method;
    }
    
    @Override
    public int hashCode() {
        return this.method.hashCode();
    }
    
    @Override
    public String toHuman() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.method.toHuman());
        sb.append(": ");
        sb.append(this.annotations);
        return sb.toString();
    }
    
    public void writeTo(final DexFile dexFile, final AnnotatedOutput annotatedOutput) {
        final int index = dexFile.getMethodIds().indexOf(this.method);
        final int absoluteOffset = this.annotations.getAbsoluteOffset();
        if (annotatedOutput.annotates()) {
            final StringBuilder sb = new StringBuilder();
            sb.append("    ");
            sb.append(this.method.toHuman());
            annotatedOutput.annotate(0, sb.toString());
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("      method_idx:      ");
            sb2.append(Hex.u4(index));
            annotatedOutput.annotate(4, sb2.toString());
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("      annotations_off: ");
            sb3.append(Hex.u4(absoluteOffset));
            annotatedOutput.annotate(4, sb3.toString());
        }
        annotatedOutput.writeInt(index);
        annotatedOutput.writeInt(absoluteOffset);
    }
}
