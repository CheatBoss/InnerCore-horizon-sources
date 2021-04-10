package com.android.dex;

import com.android.dex.util.*;

public final class FieldId implements Comparable<FieldId>
{
    private final int declaringClassIndex;
    private final Dex dex;
    private final int nameIndex;
    private final int typeIndex;
    
    public FieldId(final Dex dex, final int declaringClassIndex, final int typeIndex, final int nameIndex) {
        this.dex = dex;
        this.declaringClassIndex = declaringClassIndex;
        this.typeIndex = typeIndex;
        this.nameIndex = nameIndex;
    }
    
    @Override
    public int compareTo(final FieldId fieldId) {
        if (this.declaringClassIndex != fieldId.declaringClassIndex) {
            return Unsigned.compare(this.declaringClassIndex, fieldId.declaringClassIndex);
        }
        if (this.nameIndex != fieldId.nameIndex) {
            return Unsigned.compare(this.nameIndex, fieldId.nameIndex);
        }
        return Unsigned.compare(this.typeIndex, fieldId.typeIndex);
    }
    
    public int getDeclaringClassIndex() {
        return this.declaringClassIndex;
    }
    
    public int getNameIndex() {
        return this.nameIndex;
    }
    
    public int getTypeIndex() {
        return this.typeIndex;
    }
    
    @Override
    public String toString() {
        if (this.dex == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append(this.declaringClassIndex);
            sb.append(" ");
            sb.append(this.typeIndex);
            sb.append(" ");
            sb.append(this.nameIndex);
            return sb.toString();
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append(this.dex.typeNames().get(this.typeIndex));
        sb2.append(".");
        sb2.append(this.dex.strings().get(this.nameIndex));
        return sb2.toString();
    }
    
    public void writeTo(final Dex.Section section) {
        section.writeUnsignedShort(this.declaringClassIndex);
        section.writeUnsignedShort(this.typeIndex);
        section.writeInt(this.nameIndex);
    }
}
