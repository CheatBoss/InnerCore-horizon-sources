package com.android.dx.dex.file;

import com.android.dx.util.*;
import com.android.dx.rop.cst.*;

public final class TypeIdItem extends IdItem
{
    public TypeIdItem(final CstType cstType) {
        super(cstType);
    }
    
    @Override
    public void addContents(final DexFile dexFile) {
        dexFile.getStringIds().intern(this.getDefiningClass().getDescriptor());
    }
    
    @Override
    public ItemType itemType() {
        return ItemType.TYPE_TYPE_ID_ITEM;
    }
    
    @Override
    public int writeSize() {
        return 4;
    }
    
    @Override
    public void writeTo(final DexFile dexFile, final AnnotatedOutput annotatedOutput) {
        final CstString descriptor = this.getDefiningClass().getDescriptor();
        final int index = dexFile.getStringIds().indexOf(descriptor);
        if (annotatedOutput.annotates()) {
            final StringBuilder sb = new StringBuilder();
            sb.append(this.indexString());
            sb.append(' ');
            sb.append(descriptor.toHuman());
            annotatedOutput.annotate(0, sb.toString());
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("  descriptor_idx: ");
            sb2.append(Hex.u4(index));
            annotatedOutput.annotate(4, sb2.toString());
        }
        annotatedOutput.writeInt(index);
    }
}
