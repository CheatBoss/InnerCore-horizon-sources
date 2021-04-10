package com.android.dex;

import com.android.dex.util.*;

public final class ProtoId implements Comparable<ProtoId>
{
    private final Dex dex;
    private final int parametersOffset;
    private final int returnTypeIndex;
    private final int shortyIndex;
    
    public ProtoId(final Dex dex, final int shortyIndex, final int returnTypeIndex, final int parametersOffset) {
        this.dex = dex;
        this.shortyIndex = shortyIndex;
        this.returnTypeIndex = returnTypeIndex;
        this.parametersOffset = parametersOffset;
    }
    
    @Override
    public int compareTo(final ProtoId protoId) {
        if (this.returnTypeIndex != protoId.returnTypeIndex) {
            return Unsigned.compare(this.returnTypeIndex, protoId.returnTypeIndex);
        }
        return Unsigned.compare(this.parametersOffset, protoId.parametersOffset);
    }
    
    public int getParametersOffset() {
        return this.parametersOffset;
    }
    
    public int getReturnTypeIndex() {
        return this.returnTypeIndex;
    }
    
    public int getShortyIndex() {
        return this.shortyIndex;
    }
    
    @Override
    public String toString() {
        if (this.dex == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append(this.shortyIndex);
            sb.append(" ");
            sb.append(this.returnTypeIndex);
            sb.append(" ");
            sb.append(this.parametersOffset);
            return sb.toString();
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append(this.dex.strings().get(this.shortyIndex));
        sb2.append(": ");
        sb2.append(this.dex.typeNames().get(this.returnTypeIndex));
        sb2.append(" ");
        sb2.append(this.dex.readTypeList(this.parametersOffset));
        return sb2.toString();
    }
    
    public void writeTo(final Dex.Section section) {
        section.writeInt(this.shortyIndex);
        section.writeInt(this.returnTypeIndex);
        section.writeInt(this.parametersOffset);
    }
}
