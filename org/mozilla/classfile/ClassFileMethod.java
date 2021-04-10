package org.mozilla.classfile;

final class ClassFileMethod
{
    private byte[] itsCodeAttribute;
    private short itsFlags;
    private String itsName;
    private short itsNameIndex;
    private String itsType;
    private short itsTypeIndex;
    
    ClassFileMethod(final String itsName, final short itsNameIndex, final String itsType, final short itsTypeIndex, final short itsFlags) {
        this.itsName = itsName;
        this.itsNameIndex = itsNameIndex;
        this.itsType = itsType;
        this.itsTypeIndex = itsTypeIndex;
        this.itsFlags = itsFlags;
    }
    
    short getFlags() {
        return this.itsFlags;
    }
    
    String getName() {
        return this.itsName;
    }
    
    String getType() {
        return this.itsType;
    }
    
    int getWriteSize() {
        return this.itsCodeAttribute.length + 8;
    }
    
    void setCodeAttribute(final byte[] itsCodeAttribute) {
        this.itsCodeAttribute = itsCodeAttribute;
    }
    
    int write(final byte[] array, int n) {
        n = ClassFileWriter.putInt16(this.itsFlags, array, n);
        n = ClassFileWriter.putInt16(this.itsNameIndex, array, n);
        n = ClassFileWriter.putInt16(1, array, ClassFileWriter.putInt16(this.itsTypeIndex, array, n));
        System.arraycopy(this.itsCodeAttribute, 0, array, n, this.itsCodeAttribute.length);
        return n + this.itsCodeAttribute.length;
    }
}
