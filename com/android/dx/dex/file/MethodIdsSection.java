package com.android.dx.dex.file;

import com.android.dx.rop.cst.*;
import java.util.*;
import com.android.dx.util.*;

public final class MethodIdsSection extends MemberIdsSection
{
    private final TreeMap<CstBaseMethodRef, MethodIdItem> methodIds;
    
    public MethodIdsSection(final DexFile dexFile) {
        super("method_ids", dexFile);
        this.methodIds = new TreeMap<CstBaseMethodRef, MethodIdItem>();
    }
    
    @Override
    public IndexedItem get(final Constant constant) {
        if (constant == null) {
            throw new NullPointerException("cst == null");
        }
        this.throwIfNotPrepared();
        final MethodIdItem methodIdItem = this.methodIds.get(constant);
        if (methodIdItem == null) {
            throw new IllegalArgumentException("not found");
        }
        return methodIdItem;
    }
    
    public int indexOf(final CstBaseMethodRef cstBaseMethodRef) {
        if (cstBaseMethodRef == null) {
            throw new NullPointerException("ref == null");
        }
        this.throwIfNotPrepared();
        final MethodIdItem methodIdItem = this.methodIds.get(cstBaseMethodRef);
        if (methodIdItem == null) {
            throw new IllegalArgumentException("not found");
        }
        return methodIdItem.getIndex();
    }
    
    public MethodIdItem intern(final CstBaseMethodRef cstBaseMethodRef) {
        // monitorenter(this)
        Label_0016: {
            if (cstBaseMethodRef != null) {
                break Label_0016;
            }
            while (true) {
                try {
                    throw new NullPointerException("method == null");
                    // monitorexit(this)
                    throw;
                    while (true) {
                        return;
                        this.throwIfPrepared();
                        methodIdItem = new MethodIdItem(cstBaseMethodRef);
                        this.methodIds.put(cstBaseMethodRef, methodIdItem);
                        continue;
                    }
                }
                // monitorexit(this)
                // iftrue(Label_0057:, methodIdItem = (MethodIdItem)this.methodIds.get((Object)cstBaseMethodRef) != null)
                finally {
                    continue;
                }
                break;
            }
        }
    }
    
    @Override
    public Collection<? extends Item> items() {
        return this.methodIds.values();
    }
    
    public void writeHeaderPart(final AnnotatedOutput annotatedOutput) {
        this.throwIfNotPrepared();
        final int size = this.methodIds.size();
        int fileOffset;
        if (size == 0) {
            fileOffset = 0;
        }
        else {
            fileOffset = this.getFileOffset();
        }
        if (annotatedOutput.annotates()) {
            final StringBuilder sb = new StringBuilder();
            sb.append("method_ids_size: ");
            sb.append(Hex.u4(size));
            annotatedOutput.annotate(4, sb.toString());
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("method_ids_off:  ");
            sb2.append(Hex.u4(fileOffset));
            annotatedOutput.annotate(4, sb2.toString());
        }
        annotatedOutput.writeInt(size);
        annotatedOutput.writeInt(fileOffset);
    }
}
