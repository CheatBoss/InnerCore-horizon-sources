package com.android.dx.dex.file;

import com.android.dx.rop.cst.*;
import com.android.dx.rop.annotation.*;
import com.android.dx.util.*;

public final class FieldAnnotationStruct implements ToHuman, Comparable<FieldAnnotationStruct>
{
    private AnnotationSetItem annotations;
    private final CstFieldRef field;
    
    public FieldAnnotationStruct(final CstFieldRef field, final AnnotationSetItem annotations) {
        if (field == null) {
            throw new NullPointerException("field == null");
        }
        if (annotations == null) {
            throw new NullPointerException("annotations == null");
        }
        this.field = field;
        this.annotations = annotations;
    }
    
    public void addContents(final DexFile dexFile) {
        final FieldIdsSection fieldIds = dexFile.getFieldIds();
        final MixedItemSection wordData = dexFile.getWordData();
        fieldIds.intern(this.field);
        this.annotations = wordData.intern(this.annotations);
    }
    
    @Override
    public int compareTo(final FieldAnnotationStruct fieldAnnotationStruct) {
        return this.field.compareTo((Constant)fieldAnnotationStruct.field);
    }
    
    @Override
    public boolean equals(final Object o) {
        return o instanceof FieldAnnotationStruct && this.field.equals(((FieldAnnotationStruct)o).field);
    }
    
    public Annotations getAnnotations() {
        return this.annotations.getAnnotations();
    }
    
    public CstFieldRef getField() {
        return this.field;
    }
    
    @Override
    public int hashCode() {
        return this.field.hashCode();
    }
    
    @Override
    public String toHuman() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.field.toHuman());
        sb.append(": ");
        sb.append(this.annotations);
        return sb.toString();
    }
    
    public void writeTo(final DexFile dexFile, final AnnotatedOutput annotatedOutput) {
        final int index = dexFile.getFieldIds().indexOf(this.field);
        final int absoluteOffset = this.annotations.getAbsoluteOffset();
        if (annotatedOutput.annotates()) {
            final StringBuilder sb = new StringBuilder();
            sb.append("    ");
            sb.append(this.field.toHuman());
            annotatedOutput.annotate(0, sb.toString());
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("      field_idx:       ");
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
