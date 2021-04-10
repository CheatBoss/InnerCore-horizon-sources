package com.android.dx.cf.code;

import com.android.dex.util.*;
import com.android.dx.util.*;
import com.android.dx.rop.type.*;

public final class ExecutionStack extends MutabilityControl
{
    private final boolean[] local;
    private final TypeBearer[] stack;
    private int stackPtr;
    
    public ExecutionStack(final int n) {
        super(n != 0);
        this.stack = new TypeBearer[n];
        this.local = new boolean[n];
        this.stackPtr = 0;
    }
    
    private static String stackElementString(final TypeBearer typeBearer) {
        if (typeBearer == null) {
            return "<invalid>";
        }
        return typeBearer.toString();
    }
    
    private static TypeBearer throwSimException(final String s) {
        final StringBuilder sb = new StringBuilder();
        sb.append("stack: ");
        sb.append(s);
        throw new SimException(sb.toString());
    }
    
    public void annotate(final ExceptionWithContext exceptionWithContext) {
        for (int n = this.stackPtr - 1, i = 0; i <= n; ++i) {
            String u2;
            if (i == n) {
                u2 = "top0";
            }
            else {
                u2 = Hex.u2(n - i);
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("stack[");
            sb.append(u2);
            sb.append("]: ");
            sb.append(stackElementString(this.stack[i]));
            exceptionWithContext.addContext(sb.toString());
        }
    }
    
    public void change(int n, TypeBearer frameType) {
        this.throwIfImmutable();
        try {
            frameType = frameType.getFrameType();
            n = this.stackPtr - n - 1;
            final TypeBearer typeBearer = this.stack[n];
            if (typeBearer == null || typeBearer.getType().getCategory() != frameType.getType().getCategory()) {
                final StringBuilder sb = new StringBuilder();
                sb.append("incompatible substitution: ");
                sb.append(stackElementString(typeBearer));
                sb.append(" -> ");
                sb.append(stackElementString(frameType));
                throwSimException(sb.toString());
            }
            this.stack[n] = frameType;
        }
        catch (NullPointerException ex) {
            throw new NullPointerException("type == null");
        }
    }
    
    public void clear() {
        this.throwIfImmutable();
        for (int i = 0; i < this.stackPtr; ++i) {
            this.stack[i] = null;
            this.local[i] = false;
        }
        this.stackPtr = 0;
    }
    
    public ExecutionStack copy() {
        final ExecutionStack executionStack = new ExecutionStack(this.stack.length);
        System.arraycopy(this.stack, 0, executionStack.stack, 0, this.stack.length);
        System.arraycopy(this.local, 0, executionStack.local, 0, this.local.length);
        executionStack.stackPtr = this.stackPtr;
        return executionStack;
    }
    
    public int getMaxStack() {
        return this.stack.length;
    }
    
    public void makeInitialized(final Type type) {
        if (this.stackPtr == 0) {
            return;
        }
        this.throwIfImmutable();
        final Type initializedType = type.getInitializedType();
        for (int i = 0; i < this.stackPtr; ++i) {
            if (this.stack[i] == type) {
                this.stack[i] = initializedType;
            }
        }
    }
    
    public ExecutionStack merge(final ExecutionStack executionStack) {
        try {
            return Merger.mergeStack(this, executionStack);
        }
        catch (SimException ex) {
            ex.addContext("underlay stack:");
            this.annotate(ex);
            ex.addContext("overlay stack:");
            executionStack.annotate(ex);
            throw ex;
        }
    }
    
    public TypeBearer peek(final int n) {
        if (n < 0) {
            throw new IllegalArgumentException("n < 0");
        }
        if (n >= this.stackPtr) {
            return throwSimException("underflow");
        }
        return this.stack[this.stackPtr - n - 1];
    }
    
    public boolean peekLocal(final int n) {
        if (n < 0) {
            throw new IllegalArgumentException("n < 0");
        }
        if (n >= this.stackPtr) {
            throw new SimException("stack: underflow");
        }
        return this.local[this.stackPtr - n - 1];
    }
    
    public Type peekType(final int n) {
        return this.peek(n).getType();
    }
    
    public TypeBearer pop() {
        this.throwIfImmutable();
        final TypeBearer peek = this.peek(0);
        this.stack[this.stackPtr - 1] = null;
        this.local[this.stackPtr - 1] = false;
        this.stackPtr -= peek.getType().getCategory();
        return peek;
    }
    
    public void push(TypeBearer frameType) {
        this.throwIfImmutable();
        try {
            frameType = frameType.getFrameType();
            final int category = frameType.getType().getCategory();
            if (this.stackPtr + category > this.stack.length) {
                throwSimException("overflow");
                return;
            }
            if (category == 2) {
                this.stack[this.stackPtr] = null;
                ++this.stackPtr;
            }
            this.stack[this.stackPtr] = frameType;
            ++this.stackPtr;
        }
        catch (NullPointerException ex) {
            throw new NullPointerException("type == null");
        }
    }
    
    public void setLocal() {
        this.throwIfImmutable();
        this.local[this.stackPtr] = true;
    }
    
    public int size() {
        return this.stackPtr;
    }
}
