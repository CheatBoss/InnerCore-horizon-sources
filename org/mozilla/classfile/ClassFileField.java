package org.mozilla.classfile;

final class ClassFileField
{
    private short itsAttr1;
    private short itsAttr2;
    private short itsAttr3;
    private short itsFlags;
    private boolean itsHasAttributes;
    private int itsIndex;
    private short itsNameIndex;
    private short itsTypeIndex;
    
    ClassFileField(final short itsNameIndex, final short itsTypeIndex, final short itsFlags) {
        this.itsNameIndex = itsNameIndex;
        this.itsTypeIndex = itsTypeIndex;
        this.itsFlags = itsFlags;
        this.itsHasAttributes = false;
    }
    
    int getWriteSize() {
        if (!this.itsHasAttributes) {
            return 6 + 2;
        }
        return 6 + 10;
    }
    
    void setAttributes(final short itsAttr1, final short itsAttr2, final short itsAttr3, final int itsIndex) {
        this.itsHasAttributes = true;
        this.itsAttr1 = itsAttr1;
        this.itsAttr2 = itsAttr2;
        this.itsAttr3 = itsAttr3;
        this.itsIndex = itsIndex;
    }
    
    int write(final byte[] array, int n) {
        n = ClassFileWriter.putInt16(this.itsFlags, array, n);
        n = ClassFileWriter.putInt16(this.itsNameIndex, array, n);
        n = ClassFileWriter.putInt16(this.itsTypeIndex, array, n);
        if (!this.itsHasAttributes) {
            return ClassFileWriter.putInt16(0, array, n);
        }
        n = ClassFileWriter.putInt16(1, array, n);
        n = ClassFileWriter.putInt16(this.itsAttr1, array, n);
        n = ClassFileWriter.putInt16(this.itsAttr2, array, n);
        n = ClassFileWriter.putInt16(this.itsAttr3, array, n);
        return ClassFileWriter.putInt16(this.itsIndex, array, n);
    }
}
