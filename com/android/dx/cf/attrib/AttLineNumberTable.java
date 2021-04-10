package com.android.dx.cf.attrib;

import com.android.dx.cf.code.*;
import com.android.dx.util.*;

public final class AttLineNumberTable extends BaseAttribute
{
    public static final String ATTRIBUTE_NAME = "LineNumberTable";
    private final LineNumberList lineNumbers;
    
    public AttLineNumberTable(final LineNumberList lineNumbers) {
        super("LineNumberTable");
        try {
            if (lineNumbers.isMutable()) {
                throw new MutabilityException("lineNumbers.isMutable()");
            }
            this.lineNumbers = lineNumbers;
        }
        catch (NullPointerException ex) {
            throw new NullPointerException("lineNumbers == null");
        }
    }
    
    @Override
    public int byteLength() {
        return this.lineNumbers.size() * 4 + 8;
    }
    
    public LineNumberList getLineNumbers() {
        return this.lineNumbers;
    }
}
