package com.android.dx.dex.file;

import com.android.dx.rop.cst.*;
import java.util.*;
import com.android.dx.util.*;

public final class FieldIdsSection extends MemberIdsSection
{
    private final TreeMap<CstFieldRef, FieldIdItem> fieldIds;
    
    public FieldIdsSection(final DexFile dexFile) {
        super("field_ids", dexFile);
        this.fieldIds = new TreeMap<CstFieldRef, FieldIdItem>();
    }
    
    @Override
    public IndexedItem get(final Constant constant) {
        if (constant == null) {
            throw new NullPointerException("cst == null");
        }
        this.throwIfNotPrepared();
        final FieldIdItem fieldIdItem = this.fieldIds.get(constant);
        if (fieldIdItem == null) {
            throw new IllegalArgumentException("not found");
        }
        return fieldIdItem;
    }
    
    public int indexOf(final CstFieldRef cstFieldRef) {
        if (cstFieldRef == null) {
            throw new NullPointerException("ref == null");
        }
        this.throwIfNotPrepared();
        final FieldIdItem fieldIdItem = this.fieldIds.get(cstFieldRef);
        if (fieldIdItem == null) {
            throw new IllegalArgumentException("not found");
        }
        return fieldIdItem.getIndex();
    }
    
    public FieldIdItem intern(final CstFieldRef cstFieldRef) {
        // monitorenter(this)
        Label_0016: {
            if (cstFieldRef != null) {
                break Label_0016;
            }
            while (true) {
                try {
                    throw new NullPointerException("field == null");
                    this.throwIfPrepared();
                    // iftrue(Label_0057:, fieldIdItem = (FieldIdItem)this.fieldIds.get((Object)cstFieldRef) != null)
                    // monitorexit(this)
                    Block_3: {
                        break Block_3;
                        throw;
                    }
                    final FieldIdItem fieldIdItem = new FieldIdItem(cstFieldRef);
                    this.fieldIds.put(cstFieldRef, fieldIdItem);
                    Label_0057: {
                        return fieldIdItem;
                    }
                }
                // monitorexit(this)
                finally {
                    continue;
                }
                break;
            }
        }
    }
    
    @Override
    public Collection<? extends Item> items() {
        return this.fieldIds.values();
    }
    
    public void writeHeaderPart(final AnnotatedOutput annotatedOutput) {
        this.throwIfNotPrepared();
        final int size = this.fieldIds.size();
        int fileOffset;
        if (size == 0) {
            fileOffset = 0;
        }
        else {
            fileOffset = this.getFileOffset();
        }
        if (annotatedOutput.annotates()) {
            final StringBuilder sb = new StringBuilder();
            sb.append("field_ids_size:  ");
            sb.append(Hex.u4(size));
            annotatedOutput.annotate(4, sb.toString());
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("field_ids_off:   ");
            sb2.append(Hex.u4(fileOffset));
            annotatedOutput.annotate(4, sb2.toString());
        }
        annotatedOutput.writeInt(size);
        annotatedOutput.writeInt(fileOffset);
    }
}
