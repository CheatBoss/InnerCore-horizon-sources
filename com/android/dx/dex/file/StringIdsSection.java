package com.android.dx.dex.file;

import com.android.dx.rop.cst.*;
import java.util.*;
import com.android.dx.util.*;

public final class StringIdsSection extends UniformItemSection
{
    private final TreeMap<CstString, StringIdItem> strings;
    
    public StringIdsSection(final DexFile dexFile) {
        super("string_ids", dexFile, 4);
        this.strings = new TreeMap<CstString, StringIdItem>();
    }
    
    @Override
    public IndexedItem get(final Constant constant) {
        if (constant == null) {
            throw new NullPointerException("cst == null");
        }
        this.throwIfNotPrepared();
        final StringIdItem stringIdItem = this.strings.get(constant);
        if (stringIdItem == null) {
            throw new IllegalArgumentException("not found");
        }
        return stringIdItem;
    }
    
    public int indexOf(final CstString cstString) {
        if (cstString == null) {
            throw new NullPointerException("string == null");
        }
        this.throwIfNotPrepared();
        final StringIdItem stringIdItem = this.strings.get(cstString);
        if (stringIdItem == null) {
            throw new IllegalArgumentException("not found");
        }
        return stringIdItem.getIndex();
    }
    
    public StringIdItem intern(final StringIdItem stringIdItem) {
        // monitorenter(this)
        Label_0016: {
            if (stringIdItem != null) {
                break Label_0016;
            }
            while (true) {
                try {
                    throw new NullPointerException("string == null");
                    // monitorexit(this)
                    throw;
                    Label_0045: {
                        final CstString value;
                        this.strings.put(value, stringIdItem);
                    }
                    // monitorexit(this)
                    return stringIdItem;
                    while (true) {
                        return;
                        this.throwIfPrepared();
                        final CstString value = stringIdItem.getValue();
                        stringIdItem2 = this.strings.get(value);
                        continue;
                    }
                }
                // monitorexit(this)
                // iftrue(Label_0045:, stringIdItem2 == null)
                finally {
                    continue;
                }
                break;
            }
        }
    }
    
    public StringIdItem intern(final CstString cstString) {
        return this.intern(new StringIdItem(cstString));
    }
    
    public StringIdItem intern(final String s) {
        return this.intern(new StringIdItem(new CstString(s)));
    }
    
    public void intern(final CstNat cstNat) {
        synchronized (this) {
            this.intern(cstNat.getName());
            this.intern(cstNat.getDescriptor());
        }
    }
    
    @Override
    public Collection<? extends Item> items() {
        return this.strings.values();
    }
    
    @Override
    protected void orderItems() {
        int index = 0;
        final Iterator<StringIdItem> iterator = this.strings.values().iterator();
        while (iterator.hasNext()) {
            iterator.next().setIndex(index);
            ++index;
        }
    }
    
    public void writeHeaderPart(final AnnotatedOutput annotatedOutput) {
        this.throwIfNotPrepared();
        final int size = this.strings.size();
        int fileOffset;
        if (size == 0) {
            fileOffset = 0;
        }
        else {
            fileOffset = this.getFileOffset();
        }
        if (annotatedOutput.annotates()) {
            final StringBuilder sb = new StringBuilder();
            sb.append("string_ids_size: ");
            sb.append(Hex.u4(size));
            annotatedOutput.annotate(4, sb.toString());
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("string_ids_off:  ");
            sb2.append(Hex.u4(fileOffset));
            annotatedOutput.annotate(4, sb2.toString());
        }
        annotatedOutput.writeInt(size);
        annotatedOutput.writeInt(fileOffset);
    }
}
