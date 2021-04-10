package org.mozilla.classfile;

final class ExceptionTableEntry
{
    short itsCatchType;
    int itsEndLabel;
    int itsHandlerLabel;
    int itsStartLabel;
    
    ExceptionTableEntry(final int itsStartLabel, final int itsEndLabel, final int itsHandlerLabel, final short itsCatchType) {
        this.itsStartLabel = itsStartLabel;
        this.itsEndLabel = itsEndLabel;
        this.itsHandlerLabel = itsHandlerLabel;
        this.itsCatchType = itsCatchType;
    }
}
