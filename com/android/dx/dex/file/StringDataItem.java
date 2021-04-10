package com.android.dx.dex.file;

import com.android.dex.*;
import com.android.dx.rop.cst.*;
import com.android.dx.util.*;

public final class StringDataItem extends OffsettedItem
{
    private final CstString value;
    
    public StringDataItem(final CstString value) {
        super(1, writeSize(value));
        this.value = value;
    }
    
    private static int writeSize(final CstString cstString) {
        return Leb128.unsignedLeb128Size(cstString.getUtf16Size()) + cstString.getUtf8Size() + 1;
    }
    
    @Override
    public void addContents(final DexFile dexFile) {
    }
    
    @Override
    protected int compareTo0(final OffsettedItem offsettedItem) {
        return this.value.compareTo((Constant)((StringDataItem)offsettedItem).value);
    }
    
    @Override
    public ItemType itemType() {
        return ItemType.TYPE_STRING_DATA_ITEM;
    }
    
    @Override
    public String toHuman() {
        return this.value.toQuoted();
    }
    
    public void writeTo0(final DexFile dexFile, final AnnotatedOutput annotatedOutput) {
        final ByteArray bytes = this.value.getBytes();
        final int utf16Size = this.value.getUtf16Size();
        if (annotatedOutput.annotates()) {
            final int unsignedLeb128Size = Leb128.unsignedLeb128Size(utf16Size);
            final StringBuilder sb = new StringBuilder();
            sb.append("utf16_size: ");
            sb.append(Hex.u4(utf16Size));
            annotatedOutput.annotate(unsignedLeb128Size, sb.toString());
            annotatedOutput.annotate(bytes.size() + 1, this.value.toQuoted());
        }
        annotatedOutput.writeUleb128(utf16Size);
        annotatedOutput.write(bytes);
        annotatedOutput.writeByte(0);
    }
}
