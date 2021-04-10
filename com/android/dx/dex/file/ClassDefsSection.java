package com.android.dx.dex.file;

import com.android.dx.rop.type.*;
import com.android.dx.rop.cst.*;
import java.util.*;
import com.android.dx.util.*;

public final class ClassDefsSection extends UniformItemSection
{
    private final TreeMap<Type, ClassDefItem> classDefs;
    private ArrayList<ClassDefItem> orderedDefs;
    
    public ClassDefsSection(final DexFile dexFile) {
        super("class_defs", dexFile, 4);
        this.classDefs = new TreeMap<Type, ClassDefItem>();
        this.orderedDefs = null;
    }
    
    private int orderItems0(final Type type, int i, int index) {
        final ClassDefItem classDefItem = this.classDefs.get(type);
        if (classDefItem == null) {
            return i;
        }
        if (classDefItem.hasIndex()) {
            return i;
        }
        if (index < 0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("class circularity with ");
            sb.append(type);
            throw new RuntimeException(sb.toString());
        }
        final int n = index - 1;
        final CstType superclass = classDefItem.getSuperclass();
        index = i;
        if (superclass != null) {
            index = this.orderItems0(superclass.getClassType(), i, n);
        }
        final TypeList interfaces = classDefItem.getInterfaces();
        int size;
        for (size = interfaces.size(), i = 0; i < size; ++i) {
            index = this.orderItems0(interfaces.getType(i), index, n);
        }
        classDefItem.setIndex(index);
        this.orderedDefs.add(classDefItem);
        return index + 1;
    }
    
    public void add(final ClassDefItem classDefItem) {
        try {
            final Type classType = classDefItem.getThisClass().getClassType();
            this.throwIfPrepared();
            if (this.classDefs.get(classType) != null) {
                final StringBuilder sb = new StringBuilder();
                sb.append("already added: ");
                sb.append(classType);
                throw new IllegalArgumentException(sb.toString());
            }
            this.classDefs.put(classType, classDefItem);
        }
        catch (NullPointerException ex) {
            throw new NullPointerException("clazz == null");
        }
    }
    
    @Override
    public IndexedItem get(final Constant constant) {
        if (constant == null) {
            throw new NullPointerException("cst == null");
        }
        this.throwIfNotPrepared();
        final ClassDefItem classDefItem = this.classDefs.get(((CstType)constant).getClassType());
        if (classDefItem == null) {
            throw new IllegalArgumentException("not found");
        }
        return classDefItem;
    }
    
    @Override
    public Collection<? extends Item> items() {
        if (this.orderedDefs != null) {
            return this.orderedDefs;
        }
        return this.classDefs.values();
    }
    
    @Override
    protected void orderItems() {
        final int size = this.classDefs.size();
        int orderItems0 = 0;
        this.orderedDefs = new ArrayList<ClassDefItem>(size);
        final Iterator<Type> iterator = this.classDefs.keySet().iterator();
        while (iterator.hasNext()) {
            orderItems0 = this.orderItems0(iterator.next(), orderItems0, size - orderItems0);
        }
    }
    
    public void writeHeaderPart(final AnnotatedOutput annotatedOutput) {
        this.throwIfNotPrepared();
        final int size = this.classDefs.size();
        int fileOffset;
        if (size == 0) {
            fileOffset = 0;
        }
        else {
            fileOffset = this.getFileOffset();
        }
        if (annotatedOutput.annotates()) {
            final StringBuilder sb = new StringBuilder();
            sb.append("class_defs_size: ");
            sb.append(Hex.u4(size));
            annotatedOutput.annotate(4, sb.toString());
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("class_defs_off:  ");
            sb2.append(Hex.u4(fileOffset));
            annotatedOutput.annotate(4, sb2.toString());
        }
        annotatedOutput.writeInt(size);
        annotatedOutput.writeInt(fileOffset);
    }
}
