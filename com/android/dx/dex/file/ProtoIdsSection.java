package com.android.dx.dex.file;

import com.android.dx.rop.type.*;
import com.android.dx.rop.cst.*;
import java.util.*;
import com.android.dx.util.*;

public final class ProtoIdsSection extends UniformItemSection
{
    private final TreeMap<Prototype, ProtoIdItem> protoIds;
    
    public ProtoIdsSection(final DexFile dexFile) {
        super("proto_ids", dexFile, 4);
        this.protoIds = new TreeMap<Prototype, ProtoIdItem>();
    }
    
    @Override
    public IndexedItem get(final Constant constant) {
        throw new UnsupportedOperationException("unsupported");
    }
    
    public int indexOf(final Prototype prototype) {
        if (prototype == null) {
            throw new NullPointerException("prototype == null");
        }
        this.throwIfNotPrepared();
        final ProtoIdItem protoIdItem = this.protoIds.get(prototype);
        if (protoIdItem == null) {
            throw new IllegalArgumentException("not found");
        }
        return protoIdItem.getIndex();
    }
    
    public ProtoIdItem intern(final Prototype prototype) {
        // monitorenter(this)
        Label_0016: {
            if (prototype != null) {
                break Label_0016;
            }
            while (true) {
                try {
                    throw new NullPointerException("prototype == null");
                    // monitorexit(this)
                    while (true) {
                        while (true) {
                            return;
                            protoIdItem = new ProtoIdItem(prototype);
                            this.protoIds.put(prototype, protoIdItem);
                            continue;
                        }
                        throw;
                        this.throwIfPrepared();
                        continue;
                    }
                }
                // monitorexit(this)
                // iftrue(Label_0057:, protoIdItem = (ProtoIdItem)this.protoIds.get((Object)prototype) != null)
                finally {
                    continue;
                }
                break;
            }
        }
    }
    
    @Override
    public Collection<? extends Item> items() {
        return this.protoIds.values();
    }
    
    @Override
    protected void orderItems() {
        int index = 0;
        final Iterator<? extends Item> iterator = this.items().iterator();
        while (iterator.hasNext()) {
            ((ProtoIdItem)iterator.next()).setIndex(index);
            ++index;
        }
    }
    
    public void writeHeaderPart(final AnnotatedOutput annotatedOutput) {
        this.throwIfNotPrepared();
        final int size = this.protoIds.size();
        int fileOffset;
        if (size == 0) {
            fileOffset = 0;
        }
        else {
            fileOffset = this.getFileOffset();
        }
        if (size > 65536) {
            throw new UnsupportedOperationException("too many proto ids");
        }
        if (annotatedOutput.annotates()) {
            final StringBuilder sb = new StringBuilder();
            sb.append("proto_ids_size:  ");
            sb.append(Hex.u4(size));
            annotatedOutput.annotate(4, sb.toString());
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("proto_ids_off:   ");
            sb2.append(Hex.u4(fileOffset));
            annotatedOutput.annotate(4, sb2.toString());
        }
        annotatedOutput.writeInt(size);
        annotatedOutput.writeInt(fileOffset);
    }
}
