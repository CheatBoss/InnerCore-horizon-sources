package com.android.dx.dex.file;

import com.android.dx.rop.cst.*;
import com.android.dx.rop.annotation.*;
import java.io.*;
import com.android.dx.util.*;
import java.util.*;

public final class AnnotationsDirectoryItem extends OffsettedItem
{
    private static final int ALIGNMENT = 4;
    private static final int ELEMENT_SIZE = 8;
    private static final int HEADER_SIZE = 16;
    private AnnotationSetItem classAnnotations;
    private ArrayList<FieldAnnotationStruct> fieldAnnotations;
    private ArrayList<MethodAnnotationStruct> methodAnnotations;
    private ArrayList<ParameterAnnotationStruct> parameterAnnotations;
    
    public AnnotationsDirectoryItem() {
        super(4, -1);
        this.classAnnotations = null;
        this.fieldAnnotations = null;
        this.methodAnnotations = null;
        this.parameterAnnotations = null;
    }
    
    private static int listSize(final ArrayList<?> list) {
        if (list == null) {
            return 0;
        }
        return list.size();
    }
    
    @Override
    public void addContents(final DexFile dexFile) {
        final MixedItemSection wordData = dexFile.getWordData();
        if (this.classAnnotations != null) {
            this.classAnnotations = wordData.intern(this.classAnnotations);
        }
        if (this.fieldAnnotations != null) {
            final Iterator<FieldAnnotationStruct> iterator = this.fieldAnnotations.iterator();
            while (iterator.hasNext()) {
                iterator.next().addContents(dexFile);
            }
        }
        if (this.methodAnnotations != null) {
            final Iterator<MethodAnnotationStruct> iterator2 = this.methodAnnotations.iterator();
            while (iterator2.hasNext()) {
                iterator2.next().addContents(dexFile);
            }
        }
        if (this.parameterAnnotations != null) {
            final Iterator<ParameterAnnotationStruct> iterator3 = this.parameterAnnotations.iterator();
            while (iterator3.hasNext()) {
                iterator3.next().addContents(dexFile);
            }
        }
    }
    
    public void addFieldAnnotations(final CstFieldRef cstFieldRef, final Annotations annotations, final DexFile dexFile) {
        if (this.fieldAnnotations == null) {
            this.fieldAnnotations = new ArrayList<FieldAnnotationStruct>();
        }
        this.fieldAnnotations.add(new FieldAnnotationStruct(cstFieldRef, new AnnotationSetItem(annotations, dexFile)));
    }
    
    public void addMethodAnnotations(final CstMethodRef cstMethodRef, final Annotations annotations, final DexFile dexFile) {
        if (this.methodAnnotations == null) {
            this.methodAnnotations = new ArrayList<MethodAnnotationStruct>();
        }
        this.methodAnnotations.add(new MethodAnnotationStruct(cstMethodRef, new AnnotationSetItem(annotations, dexFile)));
    }
    
    public void addParameterAnnotations(final CstMethodRef cstMethodRef, final AnnotationsList list, final DexFile dexFile) {
        if (this.parameterAnnotations == null) {
            this.parameterAnnotations = new ArrayList<ParameterAnnotationStruct>();
        }
        this.parameterAnnotations.add(new ParameterAnnotationStruct(cstMethodRef, list, dexFile));
    }
    
    public int compareTo0(final OffsettedItem offsettedItem) {
        if (!this.isInternable()) {
            throw new UnsupportedOperationException("uninternable instance");
        }
        return this.classAnnotations.compareTo(((AnnotationsDirectoryItem)offsettedItem).classAnnotations);
    }
    
    void debugPrint(final PrintWriter printWriter) {
        if (this.classAnnotations != null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("  class annotations: ");
            sb.append(this.classAnnotations);
            printWriter.println(sb.toString());
        }
        if (this.fieldAnnotations != null) {
            printWriter.println("  field annotations:");
            for (final FieldAnnotationStruct fieldAnnotationStruct : this.fieldAnnotations) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("    ");
                sb2.append(fieldAnnotationStruct.toHuman());
                printWriter.println(sb2.toString());
            }
        }
        if (this.methodAnnotations != null) {
            printWriter.println("  method annotations:");
            for (final MethodAnnotationStruct methodAnnotationStruct : this.methodAnnotations) {
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("    ");
                sb3.append(methodAnnotationStruct.toHuman());
                printWriter.println(sb3.toString());
            }
        }
        if (this.parameterAnnotations != null) {
            printWriter.println("  parameter annotations:");
            for (final ParameterAnnotationStruct parameterAnnotationStruct : this.parameterAnnotations) {
                final StringBuilder sb4 = new StringBuilder();
                sb4.append("    ");
                sb4.append(parameterAnnotationStruct.toHuman());
                printWriter.println(sb4.toString());
            }
        }
    }
    
    public Annotations getMethodAnnotations(final CstMethodRef cstMethodRef) {
        if (this.methodAnnotations == null) {
            return null;
        }
        for (final MethodAnnotationStruct methodAnnotationStruct : this.methodAnnotations) {
            if (methodAnnotationStruct.getMethod().equals(cstMethodRef)) {
                return methodAnnotationStruct.getAnnotations();
            }
        }
        return null;
    }
    
    public AnnotationsList getParameterAnnotations(final CstMethodRef cstMethodRef) {
        if (this.parameterAnnotations == null) {
            return null;
        }
        for (final ParameterAnnotationStruct parameterAnnotationStruct : this.parameterAnnotations) {
            if (parameterAnnotationStruct.getMethod().equals(cstMethodRef)) {
                return parameterAnnotationStruct.getAnnotationsList();
            }
        }
        return null;
    }
    
    @Override
    public int hashCode() {
        if (this.classAnnotations == null) {
            return 0;
        }
        return this.classAnnotations.hashCode();
    }
    
    public boolean isEmpty() {
        return this.classAnnotations == null && this.fieldAnnotations == null && this.methodAnnotations == null && this.parameterAnnotations == null;
    }
    
    public boolean isInternable() {
        return this.classAnnotations != null && this.fieldAnnotations == null && this.methodAnnotations == null && this.parameterAnnotations == null;
    }
    
    @Override
    public ItemType itemType() {
        return ItemType.TYPE_ANNOTATIONS_DIRECTORY_ITEM;
    }
    
    @Override
    protected void place0(final Section section, final int n) {
        this.setWriteSize((listSize(this.fieldAnnotations) + listSize(this.methodAnnotations) + listSize(this.parameterAnnotations)) * 8 + 16);
    }
    
    public void setClassAnnotations(final Annotations annotations, final DexFile dexFile) {
        if (annotations == null) {
            throw new NullPointerException("annotations == null");
        }
        if (this.classAnnotations != null) {
            throw new UnsupportedOperationException("class annotations already set");
        }
        this.classAnnotations = new AnnotationSetItem(annotations, dexFile);
    }
    
    @Override
    public String toHuman() {
        throw new RuntimeException("unsupported");
    }
    
    @Override
    protected void writeTo0(final DexFile dexFile, final AnnotatedOutput annotatedOutput) {
        final boolean annotates = annotatedOutput.annotates();
        final int absoluteOffsetOr0 = OffsettedItem.getAbsoluteOffsetOr0(this.classAnnotations);
        final int listSize = listSize(this.fieldAnnotations);
        final int listSize2 = listSize(this.methodAnnotations);
        final int listSize3 = listSize(this.parameterAnnotations);
        if (annotates) {
            final StringBuilder sb = new StringBuilder();
            sb.append(this.offsetString());
            sb.append(" annotations directory");
            annotatedOutput.annotate(0, sb.toString());
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("  class_annotations_off: ");
            sb2.append(Hex.u4(absoluteOffsetOr0));
            annotatedOutput.annotate(4, sb2.toString());
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("  fields_size:           ");
            sb3.append(Hex.u4(listSize));
            annotatedOutput.annotate(4, sb3.toString());
            final StringBuilder sb4 = new StringBuilder();
            sb4.append("  methods_size:          ");
            sb4.append(Hex.u4(listSize2));
            annotatedOutput.annotate(4, sb4.toString());
            final StringBuilder sb5 = new StringBuilder();
            sb5.append("  parameters_size:       ");
            sb5.append(Hex.u4(listSize3));
            annotatedOutput.annotate(4, sb5.toString());
        }
        annotatedOutput.writeInt(absoluteOffsetOr0);
        annotatedOutput.writeInt(listSize);
        annotatedOutput.writeInt(listSize2);
        annotatedOutput.writeInt(listSize3);
        if (listSize != 0) {
            Collections.sort(this.fieldAnnotations);
            if (annotates) {
                annotatedOutput.annotate(0, "  fields:");
            }
            final Iterator<FieldAnnotationStruct> iterator = this.fieldAnnotations.iterator();
            while (iterator.hasNext()) {
                iterator.next().writeTo(dexFile, annotatedOutput);
            }
        }
        if (listSize2 != 0) {
            Collections.sort(this.methodAnnotations);
            if (annotates) {
                annotatedOutput.annotate(0, "  methods:");
            }
            final Iterator<MethodAnnotationStruct> iterator2 = this.methodAnnotations.iterator();
            while (iterator2.hasNext()) {
                iterator2.next().writeTo(dexFile, annotatedOutput);
            }
        }
        if (listSize3 != 0) {
            Collections.sort(this.parameterAnnotations);
            if (annotates) {
                annotatedOutput.annotate(0, "  parameters:");
            }
            final Iterator<ParameterAnnotationStruct> iterator3 = this.parameterAnnotations.iterator();
            while (iterator3.hasNext()) {
                iterator3.next().writeTo(dexFile, annotatedOutput);
            }
        }
    }
}
