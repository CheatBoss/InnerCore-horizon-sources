package com.android.dx.dex.file;

import com.android.dx.rop.type.*;
import com.android.dx.rop.cst.*;
import java.util.*;
import com.android.dx.command.dexer.*;
import com.android.dex.*;
import com.android.dx.util.*;

public final class TypeIdsSection extends UniformItemSection
{
    private final TreeMap<Type, TypeIdItem> typeIds;
    
    public TypeIdsSection(final DexFile dexFile) {
        super("type_ids", dexFile, 4);
        this.typeIds = new TreeMap<Type, TypeIdItem>();
    }
    
    @Override
    public IndexedItem get(final Constant constant) {
        if (constant == null) {
            throw new NullPointerException("cst == null");
        }
        this.throwIfNotPrepared();
        final TypeIdItem typeIdItem = this.typeIds.get(((CstType)constant).getClassType());
        if (typeIdItem == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("not found: ");
            sb.append(constant);
            throw new IllegalArgumentException(sb.toString());
        }
        return typeIdItem;
    }
    
    public int indexOf(final CstType cstType) {
        if (cstType == null) {
            throw new NullPointerException("type == null");
        }
        return this.indexOf(cstType.getClassType());
    }
    
    public int indexOf(final Type type) {
        if (type == null) {
            throw new NullPointerException("type == null");
        }
        this.throwIfNotPrepared();
        final TypeIdItem typeIdItem = this.typeIds.get(type);
        if (typeIdItem == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("not found: ");
            sb.append(type);
            throw new IllegalArgumentException(sb.toString());
        }
        return typeIdItem.getIndex();
    }
    
    public TypeIdItem intern(final CstType cstType) {
        // monitorenter(this)
        Label_0016: {
            if (cstType != null) {
                break Label_0016;
            }
            while (true) {
                try {
                    throw new NullPointerException("type == null");
                    // iftrue(Label_0065:, typeIdItem = (TypeIdItem)this.typeIds.get((Object)classType) != null)
                    TypeIdItem typeIdItem = null;
                Label_0065:
                    while (true) {
                        typeIdItem = new TypeIdItem(cstType);
                        final Type classType;
                        this.typeIds.put(classType, typeIdItem);
                        break Label_0065;
                        this.throwIfPrepared();
                        classType = cstType.getClassType();
                        continue;
                    }
                    // monitorexit(this)
                    return typeIdItem;
                    // monitorexit(this)
                    throw;
                }
                finally {
                    continue;
                }
                break;
            }
        }
    }
    
    public TypeIdItem intern(final Type type) {
        // monitorenter(this)
        Label_0016: {
            if (type != null) {
                break Label_0016;
            }
            while (true) {
                try {
                    throw new NullPointerException("type == null");
                    this.throwIfPrepared();
                    // iftrue(Label_0064:, typeIdItem = (TypeIdItem)this.typeIds.get((Object)type) != null)
                    // monitorexit(this)
                    // monitorexit(this)
                    while (true) {
                        Block_3: {
                            break Block_3;
                            return;
                            throw;
                        }
                        final TypeIdItem typeIdItem = new TypeIdItem(new CstType(type));
                        this.typeIds.put(type, typeIdItem);
                        continue;
                    }
                }
                finally {
                    continue;
                }
                break;
            }
        }
    }
    
    @Override
    public Collection<? extends Item> items() {
        return this.typeIds.values();
    }
    
    @Override
    protected void orderItems() {
        int index = 0;
        final Iterator<? extends Item> iterator = this.items().iterator();
        while (iterator.hasNext()) {
            ((TypeIdItem)iterator.next()).setIndex(index);
            ++index;
        }
    }
    
    public void writeHeaderPart(final AnnotatedOutput annotatedOutput) {
        this.throwIfNotPrepared();
        final int size = this.typeIds.size();
        int fileOffset;
        if (size == 0) {
            fileOffset = 0;
        }
        else {
            fileOffset = this.getFileOffset();
        }
        if (size > 65536) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Too many type references: ");
            sb.append(size);
            sb.append("; max is ");
            sb.append(65536);
            sb.append(".\n");
            sb.append(Main.getTooManyIdsErrorMessage());
            throw new DexIndexOverflowException(sb.toString());
        }
        if (annotatedOutput.annotates()) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("type_ids_size:   ");
            sb2.append(Hex.u4(size));
            annotatedOutput.annotate(4, sb2.toString());
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("type_ids_off:    ");
            sb3.append(Hex.u4(fileOffset));
            annotatedOutput.annotate(4, sb3.toString());
        }
        annotatedOutput.writeInt(size);
        annotatedOutput.writeInt(fileOffset);
    }
}
