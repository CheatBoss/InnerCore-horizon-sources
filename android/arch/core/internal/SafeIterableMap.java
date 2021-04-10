package android.arch.core.internal;

import java.util.function.*;
import java.util.*;

public class SafeIterableMap<K, V> implements Iterable<Map.Entry<K, V>>
{
    private Entry<K, V> mEnd;
    private WeakHashMap<Object<K, V>, Boolean> mIterators;
    private int mSize;
    private Entry<K, V> mStart;
    
    public SafeIterableMap() {
        this.mIterators = new WeakHashMap<Object<K, V>, Boolean>();
        this.mSize = 0;
    }
    
    public Iterator<Map.Entry<K, V>> descendingIterator() {
        final DescendingIterator descendingIterator = new DescendingIterator<K, V>(this.mEnd, this.mStart);
        this.mIterators.put((Object<K, V>)descendingIterator, false);
        return (Iterator<Map.Entry<K, V>>)descendingIterator;
    }
    
    public Map.Entry<K, V> eldest() {
        return this.mStart;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SafeIterableMap)) {
            return false;
        }
        final SafeIterableMap safeIterableMap = (SafeIterableMap)o;
        if (this.size() != safeIterableMap.size()) {
            return false;
        }
        final Iterator<Map.Entry<K, V>> iterator = this.iterator();
        final Iterator iterator2 = safeIterableMap.iterator();
        while (iterator.hasNext() && iterator2.hasNext()) {
            final Map.Entry entry = (Map.Entry)iterator.next();
            final Object next = iterator2.next();
            if ((entry == null && next != null) || (entry != null && !entry.equals(next))) {
                return false;
            }
        }
        return !iterator.hasNext() && !iterator2.hasNext();
    }
    
    @Override
    public void forEach(final Consumer<?> p0) {
        // 
        // This method could not be decompiled.
        // 
        // Could not show original bytecode, likely due to the same error.
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @Override
    public Iterator<Map.Entry<K, V>> iterator() {
        final AscendingIterator ascendingIterator = new AscendingIterator<K, V>(this.mStart, this.mEnd);
        this.mIterators.put((Object<K, V>)ascendingIterator, false);
        return (Iterator<Map.Entry<K, V>>)ascendingIterator;
    }
    
    public IteratorWithAdditions iteratorWithAdditions() {
        final IteratorWithAdditions iteratorWithAdditions = new IteratorWithAdditions();
        this.mIterators.put((Object<K, V>)iteratorWithAdditions, false);
        return iteratorWithAdditions;
    }
    
    public Map.Entry<K, V> newest() {
        return this.mEnd;
    }
    
    public int size() {
        return this.mSize;
    }
    
    @Override
    public Spliterator<Object> spliterator() {
        return Iterable-CC.$default$spliterator();
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("[");
        final Iterator<Map.Entry<?, ?>> iterator = (Iterator<Map.Entry<?, ?>>)this.iterator();
        while (iterator.hasNext()) {
            sb.append(((Map.Entry<?, ?>)iterator.next()).toString());
            if (iterator.hasNext()) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }
    
    static class AscendingIterator<K, V> extends ListIterator<K, V>
    {
        AscendingIterator(final Entry<K, V> entry, final Entry<K, V> entry2) {
            super(entry, entry2);
        }
        
        @Override
        Entry<K, V> forward(final Entry<K, V> entry) {
            return entry.mNext;
        }
    }
    
    private static class DescendingIterator<K, V> extends ListIterator<K, V>
    {
        DescendingIterator(final Entry<K, V> entry, final Entry<K, V> entry2) {
            super(entry, entry2);
        }
        
        @Override
        Entry<K, V> forward(final Entry<K, V> entry) {
            return entry.mPrevious;
        }
    }
    
    static class Entry<K, V> implements Map.Entry<K, V>
    {
        final K mKey;
        Entry<K, V> mNext;
        Entry<K, V> mPrevious;
        final V mValue;
        
        @Override
        public boolean equals(final Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof Entry)) {
                return false;
            }
            final Entry entry = (Entry)o;
            return this.mKey.equals(entry.mKey) && this.mValue.equals(entry.mValue);
        }
        
        @Override
        public K getKey() {
            return this.mKey;
        }
        
        @Override
        public V getValue() {
            return this.mValue;
        }
        
        @Override
        public V setValue(final V v) {
            throw new UnsupportedOperationException("An entry modification is not supported");
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append(this.mKey);
            sb.append("=");
            sb.append(this.mValue);
            return sb.toString();
        }
    }
    
    private class IteratorWithAdditions implements Iterator<Map.Entry<K, V>>
    {
        private boolean mBeforeStart;
        private Entry<K, V> mCurrent;
        
        private IteratorWithAdditions() {
            this.mBeforeStart = true;
        }
        
        @Override
        public void forEachRemaining(final Consumer<?> p0) {
            // 
            // This method could not be decompiled.
            // 
            // Could not show original bytecode, likely due to the same error.
            // 
            // The error that occurred was:
            // 
            // java.lang.NullPointerException
            // 
            throw new IllegalStateException("An error occurred while decompiling this method.");
        }
        
        @Override
        public boolean hasNext() {
            if (this.mBeforeStart) {
                return SafeIterableMap.this.mStart != null;
            }
            final Entry<K, V> mCurrent = this.mCurrent;
            return mCurrent != null && mCurrent.mNext != null;
        }
        
        @Override
        public Map.Entry<K, V> next() {
            Entry<K, V> mCurrent;
            if (this.mBeforeStart) {
                this.mBeforeStart = false;
                mCurrent = SafeIterableMap.this.mStart;
            }
            else {
                final Entry<K, V> mCurrent2 = this.mCurrent;
                if (mCurrent2 != null) {
                    mCurrent = mCurrent2.mNext;
                }
                else {
                    mCurrent = null;
                }
            }
            return this.mCurrent = mCurrent;
        }
        
        @Override
        public void remove() {
            Iterator-CC.$default$remove(this);
        }
    }
    
    private abstract static class ListIterator<K, V> implements Iterator<Map.Entry<K, V>>
    {
        Entry<K, V> mExpectedEnd;
        Entry<K, V> mNext;
        
        ListIterator(final Entry<K, V> mNext, final Entry<K, V> mExpectedEnd) {
            this.mExpectedEnd = mExpectedEnd;
            this.mNext = mNext;
        }
        
        private Entry<K, V> nextNode() {
            final Entry<K, V> mNext = this.mNext;
            final Entry<K, V> mExpectedEnd = this.mExpectedEnd;
            if (mNext != mExpectedEnd && mExpectedEnd != null) {
                return this.forward(mNext);
            }
            return null;
        }
        
        @Override
        public void forEachRemaining(final Consumer<?> p0) {
            // 
            // This method could not be decompiled.
            // 
            // Could not show original bytecode, likely due to the same error.
            // 
            // The error that occurred was:
            // 
            // java.lang.NullPointerException
            // 
            throw new IllegalStateException("An error occurred while decompiling this method.");
        }
        
        abstract Entry<K, V> forward(final Entry<K, V> p0);
        
        @Override
        public boolean hasNext() {
            return this.mNext != null;
        }
        
        @Override
        public Map.Entry<K, V> next() {
            final Entry<K, V> mNext = this.mNext;
            this.mNext = this.nextNode();
            return mNext;
        }
        
        @Override
        public void remove() {
            Iterator-CC.$default$remove(this);
        }
    }
}
