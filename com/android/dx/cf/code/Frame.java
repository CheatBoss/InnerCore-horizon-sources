package com.android.dx.cf.code;

import com.android.dx.util.*;
import com.android.dex.util.*;
import com.android.dx.rop.type.*;
import com.android.dx.rop.cst.*;

public final class Frame
{
    private final LocalsArray locals;
    private final ExecutionStack stack;
    private final IntList subroutines;
    
    public Frame(final int n, final int n2) {
        this(new OneLocalsArray(n), new ExecutionStack(n2));
    }
    
    private Frame(final LocalsArray localsArray, final ExecutionStack executionStack) {
        this(localsArray, executionStack, IntList.EMPTY);
    }
    
    private Frame(final LocalsArray locals, final ExecutionStack stack, final IntList subroutines) {
        if (locals == null) {
            throw new NullPointerException("locals == null");
        }
        if (stack == null) {
            throw new NullPointerException("stack == null");
        }
        subroutines.throwIfMutable();
        this.locals = locals;
        this.stack = stack;
        this.subroutines = subroutines;
    }
    
    private static LocalsArray adjustLocalsForSubroutines(final LocalsArray localsArray, final IntList list) {
        if (!(localsArray instanceof LocalsArraySet)) {
            return localsArray;
        }
        final LocalsArraySet set = (LocalsArraySet)localsArray;
        if (list.size() == 0) {
            return set.getPrimary();
        }
        return set;
    }
    
    private IntList mergeSubroutineLists(final IntList list) {
        if (this.subroutines.equals(list)) {
            return this.subroutines;
        }
        final IntList list2 = new IntList();
        for (int size = this.subroutines.size(), size2 = list.size(), n = 0; n < size && n < size2 && this.subroutines.get(n) == list.get(n); ++n) {
            list2.add(n);
        }
        list2.setImmutable();
        return list2;
    }
    
    public void annotate(final ExceptionWithContext exceptionWithContext) {
        this.locals.annotate(exceptionWithContext);
        this.stack.annotate(exceptionWithContext);
    }
    
    public Frame copy() {
        return new Frame(this.locals.copy(), this.stack.copy(), this.subroutines);
    }
    
    public LocalsArray getLocals() {
        return this.locals;
    }
    
    public ExecutionStack getStack() {
        return this.stack;
    }
    
    public IntList getSubroutines() {
        return this.subroutines;
    }
    
    public void initializeWithParameters(final StdTypeList list) {
        int n = 0;
        for (int size = list.size(), i = 0; i < size; ++i) {
            final Type value = list.get(i);
            this.locals.set(n, value);
            n += value.getCategory();
        }
    }
    
    public Frame makeExceptionHandlerStartFrame(final CstType cstType) {
        final ExecutionStack copy = this.getStack().copy();
        copy.clear();
        copy.push(cstType);
        return new Frame(this.getLocals(), copy, this.subroutines);
    }
    
    public void makeInitialized(final Type type) {
        this.locals.makeInitialized(type);
        this.stack.makeInitialized(type);
    }
    
    public Frame makeNewSubroutineStartFrame(final int n, final int n2) {
        this.subroutines.mutableCopy().add(n);
        return new Frame(this.locals.getPrimary(), this.stack, IntList.makeImmutable(n)).mergeWithSubroutineCaller(this, n, n2);
    }
    
    public Frame mergeWith(final Frame frame) {
        final LocalsArray merge = this.getLocals().merge(frame.getLocals());
        final ExecutionStack merge2 = this.getStack().merge(frame.getStack());
        final IntList mergeSubroutineLists = this.mergeSubroutineLists(frame.subroutines);
        final LocalsArray adjustLocalsForSubroutines = adjustLocalsForSubroutines(merge, mergeSubroutineLists);
        if (adjustLocalsForSubroutines == this.getLocals() && merge2 == this.getStack() && this.subroutines == mergeSubroutineLists) {
            return this;
        }
        return new Frame(adjustLocalsForSubroutines, merge2, mergeSubroutineLists);
    }
    
    public Frame mergeWithSubroutineCaller(final Frame frame, int n, int size) {
        final LocalsArraySet mergeWithSubroutineCaller = this.getLocals().mergeWithSubroutineCaller(frame.getLocals(), size);
        final ExecutionStack merge = this.getStack().merge(frame.getStack());
        IntList mutableCopy = frame.subroutines.mutableCopy();
        mutableCopy.add(n);
        mutableCopy.setImmutable();
        if (mergeWithSubroutineCaller == this.getLocals() && merge == this.getStack() && this.subroutines.equals(mutableCopy)) {
            return this;
        }
        IntList subroutines;
        if (this.subroutines.equals(mutableCopy)) {
            subroutines = this.subroutines;
        }
        else {
            IntList subroutines3;
            if (this.subroutines.size() > mutableCopy.size()) {
                final IntList subroutines2 = this.subroutines;
                subroutines3 = mutableCopy;
                mutableCopy = subroutines2;
            }
            else {
                subroutines3 = this.subroutines;
            }
            size = mutableCopy.size();
            final int size2 = subroutines3.size();
            n = size2 - 1;
            while (true) {
                subroutines = mutableCopy;
                if (n < 0) {
                    break;
                }
                if (subroutines3.get(n) != mutableCopy.get(size - size2 + n)) {
                    throw new RuntimeException("Incompatible merged subroutines");
                }
                --n;
            }
        }
        return new Frame(mergeWithSubroutineCaller, merge, subroutines);
    }
    
    public void setImmutable() {
        this.locals.setImmutable();
        this.stack.setImmutable();
    }
    
    public Frame subFrameForLabel(final int n, final int n2) {
        LocalsArray subArrayForLabel = null;
        if (this.locals instanceof LocalsArraySet) {
            subArrayForLabel = ((LocalsArraySet)this.locals).subArrayForLabel(n2);
        }
        try {
            final IntList mutableCopy = this.subroutines.mutableCopy();
            if (mutableCopy.pop() != n) {
                throw new RuntimeException("returning from invalid subroutine");
            }
            mutableCopy.setImmutable();
            if (subArrayForLabel == null) {
                return null;
            }
            return new Frame(subArrayForLabel, this.stack, mutableCopy);
        }
        catch (NullPointerException ex) {
            throw new NullPointerException("can't return from non-subroutine");
        }
        catch (IndexOutOfBoundsException ex2) {
            throw new RuntimeException("returning from invalid subroutine");
        }
    }
}
