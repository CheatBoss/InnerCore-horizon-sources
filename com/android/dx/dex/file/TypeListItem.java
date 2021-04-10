package com.android.dx.dex.file;

import com.android.dx.util.*;
import com.android.dx.rop.type.*;

public final class TypeListItem extends OffsettedItem
{
    private static final int ALIGNMENT = 4;
    private static final int ELEMENT_SIZE = 2;
    private static final int HEADER_SIZE = 4;
    private final TypeList list;
    
    public TypeListItem(final TypeList list) {
        super(4, list.size() * 2 + 4);
        this.list = list;
    }
    
    @Override
    public void addContents(final DexFile dexFile) {
        final TypeIdsSection typeIds = dexFile.getTypeIds();
        for (int size = this.list.size(), i = 0; i < size; ++i) {
            typeIds.intern(this.list.getType(i));
        }
    }
    
    @Override
    protected int compareTo0(final OffsettedItem offsettedItem) {
        return StdTypeList.compareContents(this.list, ((TypeListItem)offsettedItem).list);
    }
    
    public TypeList getList() {
        return this.list;
    }
    
    @Override
    public int hashCode() {
        return StdTypeList.hashContents(this.list);
    }
    
    @Override
    public ItemType itemType() {
        return ItemType.TYPE_TYPE_LIST;
    }
    
    @Override
    public String toHuman() {
        throw new RuntimeException("unsupported");
    }
    
    @Override
    protected void writeTo0(final DexFile dexFile, final AnnotatedOutput annotatedOutput) {
        final TypeIdsSection typeIds = dexFile.getTypeIds();
        final int size = this.list.size();
        final boolean annotates = annotatedOutput.annotates();
        final int n = 0;
        if (annotates) {
            final StringBuilder sb = new StringBuilder();
            sb.append(this.offsetString());
            sb.append(" type_list");
            annotatedOutput.annotate(0, sb.toString());
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("  size: ");
            sb2.append(Hex.u4(size));
            annotatedOutput.annotate(4, sb2.toString());
            for (int i = 0; i < size; ++i) {
                final Type type = this.list.getType(i);
                final int index = typeIds.indexOf(type);
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("  ");
                sb3.append(Hex.u2(index));
                sb3.append(" // ");
                sb3.append(type.toHuman());
                annotatedOutput.annotate(2, sb3.toString());
            }
        }
        annotatedOutput.writeInt(size);
        for (int j = n; j < size; ++j) {
            annotatedOutput.writeShort(typeIds.indexOf(this.list.getType(j)));
        }
    }
}
