package com.android.dx.dex.file;

import com.android.dx.rop.annotation.*;
import com.android.dx.rop.cst.*;
import java.util.*;
import com.android.dx.util.*;

public final class ParameterAnnotationStruct implements ToHuman, Comparable<ParameterAnnotationStruct>
{
    private final UniformListItem<AnnotationSetRefItem> annotationsItem;
    private final AnnotationsList annotationsList;
    private final CstMethodRef method;
    
    public ParameterAnnotationStruct(final CstMethodRef method, final AnnotationsList annotationsList, final DexFile dexFile) {
        if (method == null) {
            throw new NullPointerException("method == null");
        }
        if (annotationsList == null) {
            throw new NullPointerException("annotationsList == null");
        }
        this.method = method;
        this.annotationsList = annotationsList;
        final int size = annotationsList.size();
        final ArrayList list = new ArrayList<OffsettedItem>(size);
        for (int i = 0; i < size; ++i) {
            list.add(new AnnotationSetRefItem(new AnnotationSetItem(annotationsList.get(i), dexFile)));
        }
        this.annotationsItem = new UniformListItem<AnnotationSetRefItem>(ItemType.TYPE_ANNOTATION_SET_REF_LIST, (List<OffsettedItem>)list);
    }
    
    public void addContents(final DexFile dexFile) {
        final MethodIdsSection methodIds = dexFile.getMethodIds();
        final MixedItemSection wordData = dexFile.getWordData();
        methodIds.intern(this.method);
        wordData.add(this.annotationsItem);
    }
    
    @Override
    public int compareTo(final ParameterAnnotationStruct parameterAnnotationStruct) {
        return this.method.compareTo((Constant)parameterAnnotationStruct.method);
    }
    
    @Override
    public boolean equals(final Object o) {
        return o instanceof ParameterAnnotationStruct && this.method.equals(((ParameterAnnotationStruct)o).method);
    }
    
    public AnnotationsList getAnnotationsList() {
        return this.annotationsList;
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
        int n = 1;
        for (final AnnotationSetRefItem annotationSetRefItem : this.annotationsItem.getItems()) {
            if (n != 0) {
                n = 0;
            }
            else {
                sb.append(", ");
            }
            sb.append(annotationSetRefItem.toHuman());
        }
        return sb.toString();
    }
    
    public void writeTo(final DexFile dexFile, final AnnotatedOutput annotatedOutput) {
        final int index = dexFile.getMethodIds().indexOf(this.method);
        final int absoluteOffset = this.annotationsItem.getAbsoluteOffset();
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
