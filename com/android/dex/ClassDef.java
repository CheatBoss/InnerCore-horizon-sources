package com.android.dex;

public final class ClassDef
{
    public static final int NO_INDEX = -1;
    private final int accessFlags;
    private final int annotationsOffset;
    private final Dex buffer;
    private final int classDataOffset;
    private final int interfacesOffset;
    private final int offset;
    private final int sourceFileIndex;
    private final int staticValuesOffset;
    private final int supertypeIndex;
    private final int typeIndex;
    
    public ClassDef(final Dex buffer, final int offset, final int typeIndex, final int accessFlags, final int supertypeIndex, final int interfacesOffset, final int sourceFileIndex, final int annotationsOffset, final int classDataOffset, final int staticValuesOffset) {
        this.buffer = buffer;
        this.offset = offset;
        this.typeIndex = typeIndex;
        this.accessFlags = accessFlags;
        this.supertypeIndex = supertypeIndex;
        this.interfacesOffset = interfacesOffset;
        this.sourceFileIndex = sourceFileIndex;
        this.annotationsOffset = annotationsOffset;
        this.classDataOffset = classDataOffset;
        this.staticValuesOffset = staticValuesOffset;
    }
    
    public int getAccessFlags() {
        return this.accessFlags;
    }
    
    public int getAnnotationsOffset() {
        return this.annotationsOffset;
    }
    
    public int getClassDataOffset() {
        return this.classDataOffset;
    }
    
    public short[] getInterfaces() {
        return this.buffer.readTypeList(this.interfacesOffset).getTypes();
    }
    
    public int getInterfacesOffset() {
        return this.interfacesOffset;
    }
    
    public int getOffset() {
        return this.offset;
    }
    
    public int getSourceFileIndex() {
        return this.sourceFileIndex;
    }
    
    public int getStaticValuesOffset() {
        return this.staticValuesOffset;
    }
    
    public int getSupertypeIndex() {
        return this.supertypeIndex;
    }
    
    public int getTypeIndex() {
        return this.typeIndex;
    }
    
    @Override
    public String toString() {
        if (this.buffer == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append(this.typeIndex);
            sb.append(" ");
            sb.append(this.supertypeIndex);
            return sb.toString();
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append(this.buffer.typeNames().get(this.typeIndex));
        if (this.supertypeIndex != -1) {
            sb2.append(" extends ");
            sb2.append(this.buffer.typeNames().get(this.supertypeIndex));
        }
        return sb2.toString();
    }
}
