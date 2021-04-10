package org.mozilla.javascript;

import java.io.*;

public class ObjArray implements Serializable
{
    private static final int FIELDS_STORE_SIZE = 5;
    static final long serialVersionUID = 4174889037736658296L;
    private transient Object[] data;
    private transient Object f0;
    private transient Object f1;
    private transient Object f2;
    private transient Object f3;
    private transient Object f4;
    private boolean sealed;
    private int size;
    
    private void ensureCapacity(int length) {
        final int n = length - 5;
        if (n <= 0) {
            throw new IllegalArgumentException();
        }
        if (this.data == null) {
            length = 10;
            if (10 < n) {
                length = n;
            }
            this.data = new Object[length];
            return;
        }
        length = this.data.length;
        if (length < n) {
            if (length <= 5) {
                length = 10;
            }
            else {
                length *= 2;
            }
            int n2 = length;
            if (length < n) {
                n2 = n;
            }
            final Object[] data = new Object[n2];
            if (this.size > 5) {
                System.arraycopy(this.data, 0, data, 0, this.size - 5);
            }
            this.data = data;
        }
    }
    
    private Object getImpl(final int n) {
        switch (n) {
            default: {
                return this.data[n - 5];
            }
            case 4: {
                return this.f4;
            }
            case 3: {
                return this.f3;
            }
            case 2: {
                return this.f2;
            }
            case 1: {
                return this.f1;
            }
            case 0: {
                return this.f0;
            }
        }
    }
    
    private static RuntimeException onEmptyStackTopRead() {
        throw new RuntimeException("Empty stack");
    }
    
    private static RuntimeException onInvalidIndex(final int n, final int n2) {
        final StringBuilder sb = new StringBuilder();
        sb.append(n);
        sb.append(" \u2209 [0, ");
        sb.append(n2);
        sb.append(')');
        throw new IndexOutOfBoundsException(sb.toString());
    }
    
    private static RuntimeException onSeledMutation() {
        throw new IllegalStateException("Attempt to modify sealed array");
    }
    
    private void readObject(final ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        final int size = this.size;
        if (size > 5) {
            this.data = new Object[size - 5];
        }
        for (int i = 0; i != size; ++i) {
            this.setImpl(i, objectInputStream.readObject());
        }
    }
    
    private void setImpl(final int n, final Object f0) {
        switch (n) {
            default: {
                this.data[n - 5] = f0;
            }
            case 4: {
                this.f4 = f0;
            }
            case 3: {
                this.f3 = f0;
            }
            case 2: {
                this.f2 = f0;
            }
            case 1: {
                this.f1 = f0;
            }
            case 0: {
                this.f0 = f0;
            }
        }
    }
    
    private void writeObject(final ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        for (int size = this.size, i = 0; i != size; ++i) {
            objectOutputStream.writeObject(this.getImpl(i));
        }
    }
    
    public final void add(int n, Object f4) {
        final int size = this.size;
        if (n < 0 || n > size) {
            throw onInvalidIndex(n, size + 1);
        }
        if (this.sealed) {
            throw onSeledMutation();
        }
        Object f5 = f4;
        Object f6 = f4;
        Object f7 = f4;
        Object f8 = f4;
        Label_0250: {
            switch (n) {
                case 0: {
                    if (size == 0) {
                        this.f0 = f4;
                        break Label_0250;
                    }
                    f5 = this.f0;
                    this.f0 = f4;
                }
                case 1: {
                    if (size == 1) {
                        this.f1 = f5;
                        break Label_0250;
                    }
                    f6 = this.f1;
                    this.f1 = f5;
                }
                case 2: {
                    if (size == 2) {
                        this.f2 = f6;
                        break Label_0250;
                    }
                    f7 = this.f2;
                    this.f2 = f6;
                }
                case 3: {
                    if (size == 3) {
                        this.f3 = f7;
                        break Label_0250;
                    }
                    f8 = this.f3;
                    this.f3 = f7;
                }
                case 4: {
                    if (size == 4) {
                        this.f4 = f8;
                        break Label_0250;
                    }
                    f4 = this.f4;
                    this.f4 = f8;
                    n = 5;
                    break;
                }
            }
            this.ensureCapacity(size + 1);
            if (n != size) {
                System.arraycopy(this.data, n - 5, this.data, n - 5 + 1, size - n);
            }
            this.data[n - 5] = f4;
        }
        this.size = size + 1;
    }
    
    public final void add(final Object o) {
        if (this.sealed) {
            throw onSeledMutation();
        }
        final int size = this.size;
        if (size >= 5) {
            this.ensureCapacity(size + 1);
        }
        this.size = size + 1;
        this.setImpl(size, o);
    }
    
    public final void clear() {
        if (this.sealed) {
            throw onSeledMutation();
        }
        for (int size = this.size, i = 0; i != size; ++i) {
            this.setImpl(i, null);
        }
        this.size = 0;
    }
    
    public final Object get(final int n) {
        if (n >= 0 && n < this.size) {
            return this.getImpl(n);
        }
        throw onInvalidIndex(n, this.size);
    }
    
    public int indexOf(final Object o) {
        for (int size = this.size, i = 0; i != size; ++i) {
            final Object impl = this.getImpl(i);
            if (impl == o) {
                return i;
            }
            if (impl != null && impl.equals(o)) {
                return i;
            }
        }
        return -1;
    }
    
    public final boolean isEmpty() {
        return this.size == 0;
    }
    
    public final boolean isSealed() {
        return this.sealed;
    }
    
    public int lastIndexOf(final Object o) {
        int i = this.size;
        while (i != 0) {
            --i;
            final Object impl = this.getImpl(i);
            if (impl == o) {
                return i;
            }
            if (impl != null && impl.equals(o)) {
                return i;
            }
        }
        return -1;
    }
    
    public final Object peek() {
        final int size = this.size;
        if (size == 0) {
            throw onEmptyStackTopRead();
        }
        return this.getImpl(size - 1);
    }
    
    public final Object pop() {
        if (this.sealed) {
            throw onSeledMutation();
        }
        final int size = this.size - 1;
        Object o = null;
        switch (size) {
            default: {
                o = this.data[size - 5];
                this.data[size - 5] = null;
                break;
            }
            case 4: {
                o = this.f4;
                this.f4 = null;
                break;
            }
            case 3: {
                o = this.f3;
                this.f3 = null;
                break;
            }
            case 2: {
                o = this.f2;
                this.f2 = null;
                break;
            }
            case 1: {
                o = this.f1;
                this.f1 = null;
                break;
            }
            case 0: {
                o = this.f0;
                this.f0 = null;
                break;
            }
            case -1: {
                throw onEmptyStackTopRead();
            }
        }
        this.size = size;
        return o;
    }
    
    public final void push(final Object o) {
        this.add(o);
    }
    
    public final void remove(int n) {
        final int size = this.size;
        if (n < 0 || n >= size) {
            throw onInvalidIndex(n, size);
        }
        if (this.sealed) {
            throw onSeledMutation();
        }
        final int size2 = size - 1;
        Label_0215: {
            switch (n) {
                case 0: {
                    if (size2 == 0) {
                        this.f0 = null;
                        break Label_0215;
                    }
                    this.f0 = this.f1;
                }
                case 1: {
                    if (size2 == 1) {
                        this.f1 = null;
                        break Label_0215;
                    }
                    this.f1 = this.f2;
                }
                case 2: {
                    if (size2 == 2) {
                        this.f2 = null;
                        break Label_0215;
                    }
                    this.f2 = this.f3;
                }
                case 3: {
                    if (size2 == 3) {
                        this.f3 = null;
                        break Label_0215;
                    }
                    this.f3 = this.f4;
                }
                case 4: {
                    if (size2 == 4) {
                        this.f4 = null;
                        break Label_0215;
                    }
                    this.f4 = this.data[0];
                    n = 5;
                    break;
                }
            }
            if (n != size2) {
                System.arraycopy(this.data, n - 5 + 1, this.data, n - 5, size2 - n);
            }
            this.data[size2 - 5] = null;
        }
        this.size = size2;
    }
    
    public final void seal() {
        this.sealed = true;
    }
    
    public final void set(final int n, final Object o) {
        if (n < 0 || n >= this.size) {
            throw onInvalidIndex(n, this.size);
        }
        if (this.sealed) {
            throw onSeledMutation();
        }
        this.setImpl(n, o);
    }
    
    public final void setSize(final int size) {
        if (size < 0) {
            throw new IllegalArgumentException();
        }
        if (this.sealed) {
            throw onSeledMutation();
        }
        final int size2 = this.size;
        if (size < size2) {
            for (int i = size; i != size2; ++i) {
                this.setImpl(i, null);
            }
        }
        else if (size > size2 && size > 5) {
            this.ensureCapacity(size);
        }
        this.size = size;
    }
    
    public final int size() {
        return this.size;
    }
    
    public final void toArray(final Object[] array) {
        this.toArray(array, 0);
    }
    
    public final void toArray(final Object[] array, final int n) {
        final int size = this.size;
        switch (size) {
            default: {
                System.arraycopy(this.data, 0, array, n + 5, size - 5);
            }
            case 5: {
                array[n + 4] = this.f4;
            }
            case 4: {
                array[n + 3] = this.f3;
            }
            case 3: {
                array[n + 2] = this.f2;
            }
            case 2: {
                array[n + 1] = this.f1;
            }
            case 1: {
                array[n + 0] = this.f0;
            }
            case 0: {}
        }
    }
    
    public final Object[] toArray() {
        final Object[] array = new Object[this.size];
        this.toArray(array, 0);
        return array;
    }
}
