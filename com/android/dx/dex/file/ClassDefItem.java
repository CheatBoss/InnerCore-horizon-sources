package com.android.dx.dex.file;

import com.android.dx.rop.annotation.*;
import com.android.dx.rop.cst.*;
import java.io.*;
import com.android.dx.rop.type.*;
import java.util.*;
import com.android.dx.util.*;
import com.android.dx.rop.code.*;

public final class ClassDefItem extends IndexedItem
{
    private final int accessFlags;
    private AnnotationsDirectoryItem annotationsDirectory;
    private final ClassDataItem classData;
    private TypeListItem interfaces;
    private final CstString sourceFile;
    private EncodedArrayItem staticValuesItem;
    private final CstType superclass;
    private final CstType thisClass;
    
    public ClassDefItem(final CstType thisClass, final int accessFlags, final CstType superclass, final TypeList list, final CstString sourceFile) {
        if (thisClass == null) {
            throw new NullPointerException("thisClass == null");
        }
        if (list == null) {
            throw new NullPointerException("interfaces == null");
        }
        this.thisClass = thisClass;
        this.accessFlags = accessFlags;
        this.superclass = superclass;
        TypeListItem interfaces;
        if (list.size() == 0) {
            interfaces = null;
        }
        else {
            interfaces = new TypeListItem(list);
        }
        this.interfaces = interfaces;
        this.sourceFile = sourceFile;
        this.classData = new ClassDataItem(thisClass);
        this.staticValuesItem = null;
        this.annotationsDirectory = new AnnotationsDirectoryItem();
    }
    
    @Override
    public void addContents(final DexFile dexFile) {
        final TypeIdsSection typeIds = dexFile.getTypeIds();
        final MixedItemSection byteData = dexFile.getByteData();
        final MixedItemSection wordData = dexFile.getWordData();
        final MixedItemSection typeLists = dexFile.getTypeLists();
        final StringIdsSection stringIds = dexFile.getStringIds();
        typeIds.intern(this.thisClass);
        if (!this.classData.isEmpty()) {
            dexFile.getClassData().add(this.classData);
            final CstArray staticValuesConstant = this.classData.getStaticValuesConstant();
            if (staticValuesConstant != null) {
                this.staticValuesItem = byteData.intern(new EncodedArrayItem(staticValuesConstant));
            }
        }
        if (this.superclass != null) {
            typeIds.intern(this.superclass);
        }
        if (this.interfaces != null) {
            this.interfaces = typeLists.intern(this.interfaces);
        }
        if (this.sourceFile != null) {
            stringIds.intern(this.sourceFile);
        }
        if (!this.annotationsDirectory.isEmpty()) {
            if (this.annotationsDirectory.isInternable()) {
                this.annotationsDirectory = wordData.intern(this.annotationsDirectory);
                return;
            }
            wordData.add(this.annotationsDirectory);
        }
    }
    
    public void addDirectMethod(final EncodedMethod encodedMethod) {
        this.classData.addDirectMethod(encodedMethod);
    }
    
    public void addFieldAnnotations(final CstFieldRef cstFieldRef, final Annotations annotations, final DexFile dexFile) {
        this.annotationsDirectory.addFieldAnnotations(cstFieldRef, annotations, dexFile);
    }
    
    public void addInstanceField(final EncodedField encodedField) {
        this.classData.addInstanceField(encodedField);
    }
    
    public void addMethodAnnotations(final CstMethodRef cstMethodRef, final Annotations annotations, final DexFile dexFile) {
        this.annotationsDirectory.addMethodAnnotations(cstMethodRef, annotations, dexFile);
    }
    
    public void addParameterAnnotations(final CstMethodRef cstMethodRef, final AnnotationsList list, final DexFile dexFile) {
        this.annotationsDirectory.addParameterAnnotations(cstMethodRef, list, dexFile);
    }
    
    public void addStaticField(final EncodedField encodedField, final Constant constant) {
        this.classData.addStaticField(encodedField, constant);
    }
    
    public void addVirtualMethod(final EncodedMethod encodedMethod) {
        this.classData.addVirtualMethod(encodedMethod);
    }
    
    public void debugPrint(final Writer writer, final boolean b) {
        final PrintWriter printWriter = Writers.printWriterFor(writer);
        final StringBuilder sb = new StringBuilder();
        sb.append(this.getClass().getName());
        sb.append(" {");
        printWriter.println(sb.toString());
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("  accessFlags: ");
        sb2.append(Hex.u2(this.accessFlags));
        printWriter.println(sb2.toString());
        final StringBuilder sb3 = new StringBuilder();
        sb3.append("  superclass: ");
        sb3.append(this.superclass);
        printWriter.println(sb3.toString());
        final StringBuilder sb4 = new StringBuilder();
        sb4.append("  interfaces: ");
        Object interfaces;
        if (this.interfaces == null) {
            interfaces = "<none>";
        }
        else {
            interfaces = this.interfaces;
        }
        sb4.append(interfaces);
        printWriter.println(sb4.toString());
        final StringBuilder sb5 = new StringBuilder();
        sb5.append("  sourceFile: ");
        String quoted;
        if (this.sourceFile == null) {
            quoted = "<none>";
        }
        else {
            quoted = this.sourceFile.toQuoted();
        }
        sb5.append(quoted);
        printWriter.println(sb5.toString());
        this.classData.debugPrint(writer, b);
        this.annotationsDirectory.debugPrint(printWriter);
        printWriter.println("}");
    }
    
    public int getAccessFlags() {
        return this.accessFlags;
    }
    
    public TypeList getInterfaces() {
        if (this.interfaces == null) {
            return StdTypeList.EMPTY;
        }
        return this.interfaces.getList();
    }
    
    public Annotations getMethodAnnotations(final CstMethodRef cstMethodRef) {
        return this.annotationsDirectory.getMethodAnnotations(cstMethodRef);
    }
    
    public ArrayList<EncodedMethod> getMethods() {
        return this.classData.getMethods();
    }
    
    public AnnotationsList getParameterAnnotations(final CstMethodRef cstMethodRef) {
        return this.annotationsDirectory.getParameterAnnotations(cstMethodRef);
    }
    
    public CstString getSourceFile() {
        return this.sourceFile;
    }
    
    public CstType getSuperclass() {
        return this.superclass;
    }
    
    public CstType getThisClass() {
        return this.thisClass;
    }
    
    @Override
    public ItemType itemType() {
        return ItemType.TYPE_CLASS_DEF_ITEM;
    }
    
    public void setClassAnnotations(final Annotations annotations, final DexFile dexFile) {
        this.annotationsDirectory.setClassAnnotations(annotations, dexFile);
    }
    
    @Override
    public int writeSize() {
        return 32;
    }
    
    @Override
    public void writeTo(final DexFile dexFile, final AnnotatedOutput annotatedOutput) {
        final boolean annotates = annotatedOutput.annotates();
        final TypeIdsSection typeIds = dexFile.getTypeIds();
        final int index = typeIds.indexOf(this.thisClass);
        final CstType superclass = this.superclass;
        int index2 = -1;
        int index3;
        if (superclass == null) {
            index3 = -1;
        }
        else {
            index3 = typeIds.indexOf(this.superclass);
        }
        final int absoluteOffsetOr0 = OffsettedItem.getAbsoluteOffsetOr0(this.interfaces);
        int absoluteOffset;
        if (this.annotationsDirectory.isEmpty()) {
            absoluteOffset = 0;
        }
        else {
            absoluteOffset = this.annotationsDirectory.getAbsoluteOffset();
        }
        if (this.sourceFile != null) {
            index2 = dexFile.getStringIds().indexOf(this.sourceFile);
        }
        int absoluteOffset2;
        if (this.classData.isEmpty()) {
            absoluteOffset2 = 0;
        }
        else {
            absoluteOffset2 = this.classData.getAbsoluteOffset();
        }
        final int absoluteOffsetOr2 = OffsettedItem.getAbsoluteOffsetOr0(this.staticValuesItem);
        if (annotates) {
            final StringBuilder sb = new StringBuilder();
            sb.append(this.indexString());
            sb.append(' ');
            sb.append(this.thisClass.toHuman());
            annotatedOutput.annotate(0, sb.toString());
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("  class_idx:           ");
            sb2.append(Hex.u4(index));
            annotatedOutput.annotate(4, sb2.toString());
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("  access_flags:        ");
            sb3.append(AccessFlags.classString(this.accessFlags));
            annotatedOutput.annotate(4, sb3.toString());
            final StringBuilder sb4 = new StringBuilder();
            sb4.append("  superclass_idx:      ");
            sb4.append(Hex.u4(index3));
            sb4.append(" // ");
            String human;
            if (this.superclass == null) {
                human = "<none>";
            }
            else {
                human = this.superclass.toHuman();
            }
            sb4.append(human);
            annotatedOutput.annotate(4, sb4.toString());
            final StringBuilder sb5 = new StringBuilder();
            sb5.append("  interfaces_off:      ");
            sb5.append(Hex.u4(absoluteOffsetOr0));
            annotatedOutput.annotate(4, sb5.toString());
            if (absoluteOffsetOr0 != 0) {
                final TypeList list = this.interfaces.getList();
                for (int size = list.size(), i = 0; i < size; ++i) {
                    final StringBuilder sb6 = new StringBuilder();
                    sb6.append("    ");
                    sb6.append(list.getType(i).toHuman());
                    annotatedOutput.annotate(0, sb6.toString());
                }
            }
            final StringBuilder sb7 = new StringBuilder();
            sb7.append("  source_file_idx:     ");
            sb7.append(Hex.u4(index2));
            sb7.append(" // ");
            String human2;
            if (this.sourceFile == null) {
                human2 = "<none>";
            }
            else {
                human2 = this.sourceFile.toHuman();
            }
            sb7.append(human2);
            annotatedOutput.annotate(4, sb7.toString());
            final StringBuilder sb8 = new StringBuilder();
            sb8.append("  annotations_off:     ");
            sb8.append(Hex.u4(absoluteOffset));
            annotatedOutput.annotate(4, sb8.toString());
            final StringBuilder sb9 = new StringBuilder();
            sb9.append("  class_data_off:      ");
            sb9.append(Hex.u4(absoluteOffset2));
            annotatedOutput.annotate(4, sb9.toString());
            final StringBuilder sb10 = new StringBuilder();
            sb10.append("  static_values_off:   ");
            sb10.append(Hex.u4(absoluteOffsetOr2));
            annotatedOutput.annotate(4, sb10.toString());
        }
        annotatedOutput.writeInt(index);
        annotatedOutput.writeInt(this.accessFlags);
        annotatedOutput.writeInt(index3);
        annotatedOutput.writeInt(absoluteOffsetOr0);
        annotatedOutput.writeInt(index2);
        annotatedOutput.writeInt(absoluteOffset);
        annotatedOutput.writeInt(absoluteOffset2);
        annotatedOutput.writeInt(absoluteOffsetOr2);
    }
}
