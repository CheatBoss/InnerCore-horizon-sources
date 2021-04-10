package com.android.dx.dex.file;

import com.android.dx.rop.cst.*;
import com.android.dx.rop.type.*;
import com.android.dx.util.*;

public final class ProtoIdItem extends IndexedItem
{
    private TypeListItem parameterTypes;
    private final Prototype prototype;
    private final CstString shortForm;
    
    public ProtoIdItem(final Prototype prototype) {
        if (prototype == null) {
            throw new NullPointerException("prototype == null");
        }
        this.prototype = prototype;
        this.shortForm = makeShortForm(prototype);
        final StdTypeList parameterTypes = prototype.getParameterTypes();
        TypeListItem parameterTypes2;
        if (parameterTypes.size() == 0) {
            parameterTypes2 = null;
        }
        else {
            parameterTypes2 = new TypeListItem(parameterTypes);
        }
        this.parameterTypes = parameterTypes2;
    }
    
    private static CstString makeShortForm(final Prototype prototype) {
        final StdTypeList parameterTypes = prototype.getParameterTypes();
        final int size = parameterTypes.size();
        final StringBuilder sb = new StringBuilder(size + 1);
        sb.append(shortFormCharFor(prototype.getReturnType()));
        for (int i = 0; i < size; ++i) {
            sb.append(shortFormCharFor(parameterTypes.getType(i)));
        }
        return new CstString(sb.toString());
    }
    
    private static char shortFormCharFor(final Type type) {
        final char char1 = type.getDescriptor().charAt(0);
        if (char1 == '[') {
            return 'L';
        }
        return char1;
    }
    
    @Override
    public void addContents(final DexFile dexFile) {
        final StringIdsSection stringIds = dexFile.getStringIds();
        final TypeIdsSection typeIds = dexFile.getTypeIds();
        final MixedItemSection typeLists = dexFile.getTypeLists();
        typeIds.intern(this.prototype.getReturnType());
        stringIds.intern(this.shortForm);
        if (this.parameterTypes != null) {
            this.parameterTypes = typeLists.intern(this.parameterTypes);
        }
    }
    
    @Override
    public ItemType itemType() {
        return ItemType.TYPE_PROTO_ID_ITEM;
    }
    
    @Override
    public int writeSize() {
        return 12;
    }
    
    @Override
    public void writeTo(final DexFile dexFile, final AnnotatedOutput annotatedOutput) {
        final int index = dexFile.getStringIds().indexOf(this.shortForm);
        final int index2 = dexFile.getTypeIds().indexOf(this.prototype.getReturnType());
        final int absoluteOffsetOr0 = OffsettedItem.getAbsoluteOffsetOr0(this.parameterTypes);
        if (annotatedOutput.annotates()) {
            final StringBuilder sb = new StringBuilder();
            sb.append(this.prototype.getReturnType().toHuman());
            sb.append(" proto(");
            final StdTypeList parameterTypes = this.prototype.getParameterTypes();
            for (int size = parameterTypes.size(), i = 0; i < size; ++i) {
                if (i != 0) {
                    sb.append(", ");
                }
                sb.append(parameterTypes.getType(i).toHuman());
            }
            sb.append(")");
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(this.indexString());
            sb2.append(' ');
            sb2.append(sb.toString());
            annotatedOutput.annotate(0, sb2.toString());
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("  shorty_idx:      ");
            sb3.append(Hex.u4(index));
            sb3.append(" // ");
            sb3.append(this.shortForm.toQuoted());
            annotatedOutput.annotate(4, sb3.toString());
            final StringBuilder sb4 = new StringBuilder();
            sb4.append("  return_type_idx: ");
            sb4.append(Hex.u4(index2));
            sb4.append(" // ");
            sb4.append(this.prototype.getReturnType().toHuman());
            annotatedOutput.annotate(4, sb4.toString());
            final StringBuilder sb5 = new StringBuilder();
            sb5.append("  parameters_off:  ");
            sb5.append(Hex.u4(absoluteOffsetOr0));
            annotatedOutput.annotate(4, sb5.toString());
        }
        annotatedOutput.writeInt(index);
        annotatedOutput.writeInt(index2);
        annotatedOutput.writeInt(absoluteOffsetOr0);
    }
}
