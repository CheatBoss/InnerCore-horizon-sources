package com.android.dx.dex.file;

import com.android.dx.util.*;
import com.android.dx.rop.cst.*;

public abstract class MemberIdItem extends IdItem
{
    private final CstMemberRef cst;
    
    public MemberIdItem(final CstMemberRef cst) {
        super(cst.getDefiningClass());
        this.cst = cst;
    }
    
    @Override
    public void addContents(final DexFile dexFile) {
        super.addContents(dexFile);
        dexFile.getStringIds().intern(this.getRef().getNat().getName());
    }
    
    public final CstMemberRef getRef() {
        return this.cst;
    }
    
    protected abstract int getTypoidIdx(final DexFile p0);
    
    protected abstract String getTypoidName();
    
    @Override
    public int writeSize() {
        return 8;
    }
    
    @Override
    public final void writeTo(final DexFile dexFile, final AnnotatedOutput annotatedOutput) {
        final TypeIdsSection typeIds = dexFile.getTypeIds();
        final StringIdsSection stringIds = dexFile.getStringIds();
        final CstNat nat = this.cst.getNat();
        final int index = typeIds.indexOf(this.getDefiningClass());
        final int index2 = stringIds.indexOf(nat.getName());
        final int typoidIdx = this.getTypoidIdx(dexFile);
        if (annotatedOutput.annotates()) {
            final StringBuilder sb = new StringBuilder();
            sb.append(this.indexString());
            sb.append(' ');
            sb.append(this.cst.toHuman());
            annotatedOutput.annotate(0, sb.toString());
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("  class_idx: ");
            sb2.append(Hex.u2(index));
            annotatedOutput.annotate(2, sb2.toString());
            final StringBuilder sb3 = new StringBuilder();
            sb3.append(this.getTypoidName());
            sb3.append(':');
            annotatedOutput.annotate(2, String.format("  %-10s %s", sb3.toString(), Hex.u2(typoidIdx)));
            final StringBuilder sb4 = new StringBuilder();
            sb4.append("  name_idx:  ");
            sb4.append(Hex.u4(index2));
            annotatedOutput.annotate(4, sb4.toString());
        }
        annotatedOutput.writeShort(index);
        annotatedOutput.writeShort(typoidIdx);
        annotatedOutput.writeInt(index2);
    }
}
