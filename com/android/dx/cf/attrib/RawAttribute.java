package com.android.dx.cf.attrib;

import com.android.dx.util.*;
import com.android.dx.rop.cst.*;

public final class RawAttribute extends BaseAttribute
{
    private final ByteArray data;
    private final ConstantPool pool;
    
    public RawAttribute(final String s, final ByteArray byteArray, final int n, final int n2, final ConstantPool constantPool) {
        this(s, byteArray.slice(n, n + n2), constantPool);
    }
    
    public RawAttribute(final String s, final ByteArray data, final ConstantPool pool) {
        super(s);
        if (data == null) {
            throw new NullPointerException("data == null");
        }
        this.data = data;
        this.pool = pool;
    }
    
    @Override
    public int byteLength() {
        return this.data.size() + 6;
    }
    
    public ByteArray getData() {
        return this.data;
    }
    
    public ConstantPool getPool() {
        return this.pool;
    }
}
