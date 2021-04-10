package com.android.dx.dex.file;

import com.android.dx.rop.cst.*;
import com.android.dx.util.*;

public final class StringIdItem extends IndexedItem implements Comparable
{
    private StringDataItem data;
    private final CstString value;
    
    public StringIdItem(final CstString value) {
        if (value == null) {
            throw new NullPointerException("value == null");
        }
        this.value = value;
        this.data = null;
    }
    
    @Override
    public void addContents(final DexFile dexFile) {
        if (this.data == null) {
            dexFile.getStringData().add(this.data = new StringDataItem(this.value));
        }
    }
    
    @Override
    public int compareTo(final Object o) {
        return this.value.compareTo((Constant)((StringIdItem)o).value);
    }
    
    @Override
    public boolean equals(final Object o) {
        return o instanceof StringIdItem && this.value.equals(((StringIdItem)o).value);
    }
    
    public StringDataItem getData() {
        return this.data;
    }
    
    public CstString getValue() {
        return this.value;
    }
    
    @Override
    public int hashCode() {
        return this.value.hashCode();
    }
    
    @Override
    public ItemType itemType() {
        return ItemType.TYPE_STRING_ID_ITEM;
    }
    
    @Override
    public int writeSize() {
        return 4;
    }
    
    @Override
    public void writeTo(final DexFile dexFile, final AnnotatedOutput annotatedOutput) {
        final int absoluteOffset = this.data.getAbsoluteOffset();
        if (annotatedOutput.annotates()) {
            final StringBuilder sb = new StringBuilder();
            sb.append(this.indexString());
            sb.append(' ');
            sb.append(this.value.toQuoted(100));
            annotatedOutput.annotate(0, sb.toString());
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("  string_data_off: ");
            sb2.append(Hex.u4(absoluteOffset));
            annotatedOutput.annotate(4, sb2.toString());
        }
        annotatedOutput.writeInt(absoluteOffset);
    }
}
