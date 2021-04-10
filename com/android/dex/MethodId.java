package com.android.dex;

import com.android.dex.util.*;

public final class MethodId implements Comparable<MethodId>
{
    private final int declaringClassIndex;
    private final Dex dex;
    private final int nameIndex;
    private final int protoIndex;
    
    public MethodId(final Dex dex, final int declaringClassIndex, final int protoIndex, final int nameIndex) {
        this.dex = dex;
        this.declaringClassIndex = declaringClassIndex;
        this.protoIndex = protoIndex;
        this.nameIndex = nameIndex;
    }
    
    @Override
    public int compareTo(final MethodId methodId) {
        if (this.declaringClassIndex != methodId.declaringClassIndex) {
            return Unsigned.compare(this.declaringClassIndex, methodId.declaringClassIndex);
        }
        if (this.nameIndex != methodId.nameIndex) {
            return Unsigned.compare(this.nameIndex, methodId.nameIndex);
        }
        return Unsigned.compare(this.protoIndex, methodId.protoIndex);
    }
    
    public int getDeclaringClassIndex() {
        return this.declaringClassIndex;
    }
    
    public int getNameIndex() {
        return this.nameIndex;
    }
    
    public int getProtoIndex() {
        return this.protoIndex;
    }
    
    @Override
    public String toString() {
        if (this.dex == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append(this.declaringClassIndex);
            sb.append(" ");
            sb.append(this.protoIndex);
            sb.append(" ");
            sb.append(this.nameIndex);
            return sb.toString();
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append(this.dex.typeNames().get(this.declaringClassIndex));
        sb2.append(".");
        sb2.append(this.dex.strings().get(this.nameIndex));
        sb2.append(this.dex.readTypeList(this.dex.protoIds().get(this.protoIndex).getParametersOffset()));
        return sb2.toString();
    }
    
    public void writeTo(final Dex.Section section) {
        section.writeUnsignedShort(this.declaringClassIndex);
        section.writeUnsignedShort(this.protoIndex);
        section.writeInt(this.nameIndex);
    }
}
