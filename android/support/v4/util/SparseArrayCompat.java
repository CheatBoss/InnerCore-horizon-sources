package android.support.v4.util;

public class SparseArrayCompat<E> implements Cloneable
{
    private static final Object DELETED;
    private boolean mGarbage;
    private int[] mKeys;
    private int mSize;
    private Object[] mValues;
    
    static {
        DELETED = new Object();
    }
    
    public SparseArrayCompat() {
        this(10);
    }
    
    public SparseArrayCompat(int idealIntArraySize) {
        this.mGarbage = false;
        Object[] empty_OBJECTS;
        if (idealIntArraySize == 0) {
            this.mKeys = ContainerHelpers.EMPTY_INTS;
            empty_OBJECTS = ContainerHelpers.EMPTY_OBJECTS;
        }
        else {
            idealIntArraySize = ContainerHelpers.idealIntArraySize(idealIntArraySize);
            this.mKeys = new int[idealIntArraySize];
            empty_OBJECTS = new Object[idealIntArraySize];
        }
        this.mValues = empty_OBJECTS;
        this.mSize = 0;
    }
    
    private void gc() {
        final int mSize = this.mSize;
        final int[] mKeys = this.mKeys;
        final Object[] mValues = this.mValues;
        int i = 0;
        int mSize2 = 0;
        while (i < mSize) {
            final Object o = mValues[i];
            int n = mSize2;
            if (o != SparseArrayCompat.DELETED) {
                if (i != mSize2) {
                    mKeys[mSize2] = mKeys[i];
                    mValues[mSize2] = o;
                    mValues[i] = null;
                }
                n = mSize2 + 1;
            }
            ++i;
            mSize2 = n;
        }
        this.mGarbage = false;
        this.mSize = mSize2;
    }
    
    public void append(final int n, final E e) {
        if (this.mSize != 0 && n <= this.mKeys[this.mSize - 1]) {
            this.put(n, e);
            return;
        }
        if (this.mGarbage && this.mSize >= this.mKeys.length) {
            this.gc();
        }
        final int mSize = this.mSize;
        if (mSize >= this.mKeys.length) {
            final int idealIntArraySize = ContainerHelpers.idealIntArraySize(mSize + 1);
            final int[] mKeys = new int[idealIntArraySize];
            final Object[] mValues = new Object[idealIntArraySize];
            System.arraycopy(this.mKeys, 0, mKeys, 0, this.mKeys.length);
            System.arraycopy(this.mValues, 0, mValues, 0, this.mValues.length);
            this.mKeys = mKeys;
            this.mValues = mValues;
        }
        this.mKeys[mSize] = n;
        this.mValues[mSize] = e;
        this.mSize = mSize + 1;
    }
    
    public void clear() {
        final int mSize = this.mSize;
        final Object[] mValues = this.mValues;
        for (int i = 0; i < mSize; ++i) {
            mValues[i] = null;
        }
        this.mSize = 0;
        this.mGarbage = false;
    }
    
    public SparseArrayCompat<E> clone() {
        try {
            final SparseArrayCompat sparseArrayCompat = (SparseArrayCompat)super.clone();
            try {
                sparseArrayCompat.mKeys = this.mKeys.clone();
                sparseArrayCompat.mValues = this.mValues.clone();
                return sparseArrayCompat;
            }
            catch (CloneNotSupportedException ex) {
                return sparseArrayCompat;
            }
        }
        catch (CloneNotSupportedException ex2) {
            return null;
        }
    }
    
    public void delete(int binarySearch) {
        binarySearch = ContainerHelpers.binarySearch(this.mKeys, this.mSize, binarySearch);
        if (binarySearch >= 0 && this.mValues[binarySearch] != SparseArrayCompat.DELETED) {
            this.mValues[binarySearch] = SparseArrayCompat.DELETED;
            this.mGarbage = true;
        }
    }
    
    public E get(final int n) {
        return this.get(n, null);
    }
    
    public E get(int binarySearch, final E e) {
        binarySearch = ContainerHelpers.binarySearch(this.mKeys, this.mSize, binarySearch);
        if (binarySearch < 0) {
            return e;
        }
        if (this.mValues[binarySearch] == SparseArrayCompat.DELETED) {
            return e;
        }
        return (E)this.mValues[binarySearch];
    }
    
    public int indexOfKey(final int n) {
        if (this.mGarbage) {
            this.gc();
        }
        return ContainerHelpers.binarySearch(this.mKeys, this.mSize, n);
    }
    
    public int indexOfValue(final E e) {
        if (this.mGarbage) {
            this.gc();
        }
        for (int i = 0; i < this.mSize; ++i) {
            if (this.mValues[i] == e) {
                return i;
            }
        }
        return -1;
    }
    
    public int keyAt(final int n) {
        if (this.mGarbage) {
            this.gc();
        }
        return this.mKeys[n];
    }
    
    public void put(final int n, final E e) {
        final int binarySearch = ContainerHelpers.binarySearch(this.mKeys, this.mSize, n);
        if (binarySearch >= 0) {
            this.mValues[binarySearch] = e;
            return;
        }
        final int n2 = ~binarySearch;
        if (n2 < this.mSize && this.mValues[n2] == SparseArrayCompat.DELETED) {
            this.mKeys[n2] = n;
            this.mValues[n2] = e;
            return;
        }
        int n3 = n2;
        if (this.mGarbage) {
            n3 = n2;
            if (this.mSize >= this.mKeys.length) {
                this.gc();
                n3 = ~ContainerHelpers.binarySearch(this.mKeys, this.mSize, n);
            }
        }
        if (this.mSize >= this.mKeys.length) {
            final int idealIntArraySize = ContainerHelpers.idealIntArraySize(this.mSize + 1);
            final int[] mKeys = new int[idealIntArraySize];
            final Object[] mValues = new Object[idealIntArraySize];
            System.arraycopy(this.mKeys, 0, mKeys, 0, this.mKeys.length);
            System.arraycopy(this.mValues, 0, mValues, 0, this.mValues.length);
            this.mKeys = mKeys;
            this.mValues = mValues;
        }
        if (this.mSize - n3 != 0) {
            final int[] mKeys2 = this.mKeys;
            final int[] mKeys3 = this.mKeys;
            final int n4 = n3 + 1;
            System.arraycopy(mKeys2, n3, mKeys3, n4, this.mSize - n3);
            System.arraycopy(this.mValues, n3, this.mValues, n4, this.mSize - n3);
        }
        this.mKeys[n3] = n;
        this.mValues[n3] = e;
        ++this.mSize;
    }
    
    public void remove(final int n) {
        this.delete(n);
    }
    
    public void removeAt(final int n) {
        if (this.mValues[n] != SparseArrayCompat.DELETED) {
            this.mValues[n] = SparseArrayCompat.DELETED;
            this.mGarbage = true;
        }
    }
    
    public void removeAtRange(int i, int min) {
        for (min = Math.min(this.mSize, min + i); i < min; ++i) {
            this.removeAt(i);
        }
    }
    
    public void setValueAt(final int n, final E e) {
        if (this.mGarbage) {
            this.gc();
        }
        this.mValues[n] = e;
    }
    
    public int size() {
        if (this.mGarbage) {
            this.gc();
        }
        return this.mSize;
    }
    
    @Override
    public String toString() {
        if (this.size() <= 0) {
            return "{}";
        }
        final StringBuilder sb = new StringBuilder(this.mSize * 28);
        sb.append('{');
        for (int i = 0; i < this.mSize; ++i) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(this.keyAt(i));
            sb.append('=');
            final E value = this.valueAt(i);
            if (value != this) {
                sb.append(value);
            }
            else {
                sb.append("(this Map)");
            }
        }
        sb.append('}');
        return sb.toString();
    }
    
    public E valueAt(final int n) {
        if (this.mGarbage) {
            this.gc();
        }
        return (E)this.mValues[n];
    }
}
