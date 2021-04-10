package com.android.dx.cf.attrib;

import com.android.dx.cf.iface.*;
import com.android.dx.cf.code.*;
import com.android.dx.util.*;

public final class AttCode extends BaseAttribute
{
    public static final String ATTRIBUTE_NAME = "Code";
    private final AttributeList attributes;
    private final ByteCatchList catches;
    private final BytecodeArray code;
    private final int maxLocals;
    private final int maxStack;
    
    public AttCode(final int maxStack, final int maxLocals, final BytecodeArray code, final ByteCatchList catches, final AttributeList attributes) {
        super("Code");
        if (maxStack < 0) {
            throw new IllegalArgumentException("maxStack < 0");
        }
        if (maxLocals < 0) {
            throw new IllegalArgumentException("maxLocals < 0");
        }
        if (code == null) {
            throw new NullPointerException("code == null");
        }
        try {
            if (catches.isMutable()) {
                throw new MutabilityException("catches.isMutable()");
            }
            try {
                if (attributes.isMutable()) {
                    throw new MutabilityException("attributes.isMutable()");
                }
                this.maxStack = maxStack;
                this.maxLocals = maxLocals;
                this.code = code;
                this.catches = catches;
                this.attributes = attributes;
            }
            catch (NullPointerException ex) {
                throw new NullPointerException("attributes == null");
            }
        }
        catch (NullPointerException ex2) {
            throw new NullPointerException("catches == null");
        }
    }
    
    @Override
    public int byteLength() {
        return this.code.byteLength() + 10 + this.catches.byteLength() + this.attributes.byteLength();
    }
    
    public AttributeList getAttributes() {
        return this.attributes;
    }
    
    public ByteCatchList getCatches() {
        return this.catches;
    }
    
    public BytecodeArray getCode() {
        return this.code;
    }
    
    public int getMaxLocals() {
        return this.maxLocals;
    }
    
    public int getMaxStack() {
        return this.maxStack;
    }
}
