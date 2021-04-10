package com.android.dx.cf.code;

import com.android.dx.util.*;
import com.android.dex.util.*;
import com.android.dx.rop.type.*;
import com.android.dx.rop.code.*;

public class OneLocalsArray extends LocalsArray
{
    private final TypeBearer[] locals;
    
    public OneLocalsArray(final int n) {
        super(n != 0);
        this.locals = new TypeBearer[n];
    }
    
    private static TypeBearer throwSimException(final int n, final String s) {
        final StringBuilder sb = new StringBuilder();
        sb.append("local ");
        sb.append(Hex.u2(n));
        sb.append(": ");
        sb.append(s);
        throw new SimException(sb.toString());
    }
    
    @Override
    public void annotate(final ExceptionWithContext exceptionWithContext) {
        for (int i = 0; i < this.locals.length; ++i) {
            final TypeBearer typeBearer = this.locals[i];
            String string;
            if (typeBearer == null) {
                string = "<invalid>";
            }
            else {
                string = typeBearer.toString();
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("locals[");
            sb.append(Hex.u2(i));
            sb.append("]: ");
            sb.append(string);
            exceptionWithContext.addContext(sb.toString());
        }
    }
    
    @Override
    public OneLocalsArray copy() {
        final OneLocalsArray oneLocalsArray = new OneLocalsArray(this.locals.length);
        System.arraycopy(this.locals, 0, oneLocalsArray.locals, 0, this.locals.length);
        return oneLocalsArray;
    }
    
    @Override
    public TypeBearer get(final int n) {
        final TypeBearer typeBearer = this.locals[n];
        if (typeBearer == null) {
            return throwSimException(n, "invalid");
        }
        return typeBearer;
    }
    
    @Override
    public TypeBearer getCategory1(final int n) {
        final TypeBearer value = this.get(n);
        final Type type = value.getType();
        if (type.isUninitialized()) {
            return throwSimException(n, "uninitialized instance");
        }
        if (type.isCategory2()) {
            return throwSimException(n, "category-2");
        }
        return value;
    }
    
    @Override
    public TypeBearer getCategory2(final int n) {
        final TypeBearer value = this.get(n);
        if (value.getType().isCategory1()) {
            return throwSimException(n, "category-1");
        }
        return value;
    }
    
    @Override
    public int getMaxLocals() {
        return this.locals.length;
    }
    
    @Override
    public TypeBearer getOrNull(final int n) {
        return this.locals[n];
    }
    
    @Override
    protected OneLocalsArray getPrimary() {
        return this;
    }
    
    @Override
    public void invalidate(final int n) {
        this.throwIfImmutable();
        this.locals[n] = null;
    }
    
    @Override
    public void makeInitialized(final Type type) {
        final int length = this.locals.length;
        if (length == 0) {
            return;
        }
        this.throwIfImmutable();
        final Type initializedType = type.getInitializedType();
        for (int i = 0; i < length; ++i) {
            if (this.locals[i] == type) {
                this.locals[i] = initializedType;
            }
        }
    }
    
    @Override
    public LocalsArray merge(final LocalsArray localsArray) {
        if (localsArray instanceof OneLocalsArray) {
            return this.merge((OneLocalsArray)localsArray);
        }
        return localsArray.merge(this);
    }
    
    public OneLocalsArray merge(final OneLocalsArray oneLocalsArray) {
        try {
            return Merger.mergeLocals(this, oneLocalsArray);
        }
        catch (SimException ex) {
            ex.addContext("underlay locals:");
            this.annotate(ex);
            ex.addContext("overlay locals:");
            oneLocalsArray.annotate(ex);
            throw ex;
        }
    }
    
    @Override
    public LocalsArraySet mergeWithSubroutineCaller(final LocalsArray localsArray, final int n) {
        return new LocalsArraySet(this.getMaxLocals()).mergeWithSubroutineCaller(localsArray, n);
    }
    
    @Override
    public void set(final int n, TypeBearer frameType) {
        this.throwIfImmutable();
        try {
            frameType = frameType.getFrameType();
            if (n < 0) {
                throw new IndexOutOfBoundsException("idx < 0");
            }
            if (frameType.getType().isCategory2()) {
                this.locals[n + 1] = null;
            }
            this.locals[n] = frameType;
            if (n != 0) {
                frameType = this.locals[n - 1];
                if (frameType != null && frameType.getType().isCategory2()) {
                    this.locals[n - 1] = null;
                }
            }
        }
        catch (NullPointerException ex) {
            throw new NullPointerException("type == null");
        }
    }
    
    @Override
    public void set(final RegisterSpec registerSpec) {
        this.set(registerSpec.getReg(), registerSpec);
    }
    
    @Override
    public String toHuman() {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.locals.length; ++i) {
            final TypeBearer typeBearer = this.locals[i];
            String string;
            if (typeBearer == null) {
                string = "<invalid>";
            }
            else {
                string = typeBearer.toString();
            }
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("locals[");
            sb2.append(Hex.u2(i));
            sb2.append("]: ");
            sb2.append(string);
            sb2.append("\n");
            sb.append(sb2.toString());
        }
        return sb.toString();
    }
}
