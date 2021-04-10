package org.mozilla.classfile;

final class SuperBlock
{
    private int end;
    private int index;
    private boolean isInQueue;
    private boolean isInitialized;
    private int[] locals;
    private int[] stack;
    private int start;
    
    SuperBlock(final int index, final int start, final int end, final int[] array) {
        this.index = index;
        this.start = start;
        this.end = end;
        System.arraycopy(array, 0, this.locals = new int[array.length], 0, array.length);
        this.stack = new int[0];
        this.isInitialized = false;
        this.isInQueue = false;
    }
    
    private boolean mergeState(final int[] array, final int[] array2, final int n, final ConstantPool constantPool) {
        boolean b = false;
        for (int i = 0; i < n; ++i) {
            final int n2 = array[i];
            array[i] = TypeInfo.merge(array[i], array2[i], constantPool);
            if (n2 != array[i]) {
                b = true;
            }
        }
        return b;
    }
    
    int getEnd() {
        return this.end;
    }
    
    int getIndex() {
        return this.index;
    }
    
    int[] getLocals() {
        final int[] array = new int[this.locals.length];
        System.arraycopy(this.locals, 0, array, 0, this.locals.length);
        return array;
    }
    
    int[] getStack() {
        final int[] array = new int[this.stack.length];
        System.arraycopy(this.stack, 0, array, 0, this.stack.length);
        return array;
    }
    
    int getStart() {
        return this.start;
    }
    
    int[] getTrimmedLocals() {
        int n;
        for (n = this.locals.length - 1; n >= 0 && this.locals[n] == 0 && !TypeInfo.isTwoWords(this.locals[n - 1]); --n) {}
        final int n2 = n + 1;
        final int n3 = 0;
        int n4 = n2;
        int n5;
        for (int i = 0; i < n2; ++i, n4 = n5) {
            n5 = n4;
            if (TypeInfo.isTwoWords(this.locals[i])) {
                n5 = n4 - 1;
            }
        }
        final int[] array = new int[n4];
        int j = 0;
        int n6 = n3;
        while (j < n4) {
            array[j] = this.locals[n6];
            int n7 = n6;
            if (TypeInfo.isTwoWords(this.locals[n6])) {
                n7 = n6 + 1;
            }
            ++j;
            n6 = n7 + 1;
        }
        return array;
    }
    
    boolean isInQueue() {
        return this.isInQueue;
    }
    
    boolean isInitialized() {
        return this.isInitialized;
    }
    
    boolean merge(final int[] array, final int n, final int[] array2, final int n2, final ConstantPool constantPool) {
        final boolean isInitialized = this.isInitialized;
        boolean b = true;
        if (!isInitialized) {
            System.arraycopy(array, 0, this.locals, 0, n);
            System.arraycopy(array2, 0, this.stack = new int[n2], 0, n2);
            return this.isInitialized = true;
        }
        if (this.locals.length == n && this.stack.length == n2) {
            final boolean mergeState = this.mergeState(this.locals, array, n, constantPool);
            final boolean mergeState2 = this.mergeState(this.stack, array2, n2, constantPool);
            if (!mergeState) {
                if (mergeState2) {
                    return true;
                }
                b = false;
            }
            return b;
        }
        throw new IllegalArgumentException("bad merge attempt");
    }
    
    void setInQueue(final boolean isInQueue) {
        this.isInQueue = isInQueue;
    }
    
    void setInitialized(final boolean isInitialized) {
        this.isInitialized = isInitialized;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("sb ");
        sb.append(this.index);
        return sb.toString();
    }
}
