package com.microsoft.xbox.toolkit;

import java.util.*;

public class ThreadSafeFixedSizeHashtable<K, V>
{
    private int count;
    private PriorityQueue<KeyTuple> fifo;
    private Hashtable<K, V> hashtable;
    private final int maxSize;
    private Object syncObject;
    
    public ThreadSafeFixedSizeHashtable(final int maxSize) {
        this.fifo = new PriorityQueue<KeyTuple>();
        this.hashtable = new Hashtable<K, V>();
        this.syncObject = new Object();
        this.count = 0;
        this.maxSize = maxSize;
        if (maxSize > 0) {
            return;
        }
        throw new IllegalArgumentException();
    }
    
    private void cleanupIfNecessary() {
    Label_0017_Outer:
        while (true) {
            if (this.hashtable.size() != this.fifo.size()) {
                break Label_0022;
            }
            while (true) {
                boolean b = true;
                Label_0024: {
                    break Label_0024;
                    b = false;
                }
                XLEAssert.assertTrue(b);
                if (this.hashtable.size() <= this.maxSize) {
                    return;
                }
                this.hashtable.remove(this.fifo.remove().getKey());
                if (this.hashtable.size() == this.fifo.size()) {
                    continue;
                }
                break;
            }
            continue Label_0017_Outer;
        }
    }
    
    public Enumeration<V> elements() {
        return this.hashtable.elements();
    }
    
    public V get(final K k) {
        if (k == null) {
            return null;
        }
        synchronized (this.syncObject) {
            return this.hashtable.get(k);
        }
    }
    
    public Enumeration<K> keys() {
        return this.hashtable.keys();
    }
    
    public void put(final K k, final V v) {
        if (k != null) {
            if (v == null) {
                return;
            }
            synchronized (this.syncObject) {
                if (this.hashtable.containsKey(k)) {
                    return;
                }
                ++this.count;
                this.fifo.add(new KeyTuple(k, this.count));
                this.hashtable.put(k, v);
                this.cleanupIfNecessary();
            }
        }
    }
    
    public void remove(final K k) {
        if (k == null) {
            return;
        }
        synchronized (this.syncObject) {
            if (!this.hashtable.containsKey(k)) {
                return;
            }
            this.hashtable.remove(k);
            final KeyTuple keyTuple = null;
            final Iterator<KeyTuple> iterator = this.fifo.iterator();
            KeyTuple keyTuple2;
            do {
                keyTuple2 = keyTuple;
                if (!iterator.hasNext()) {
                    break;
                }
                keyTuple2 = iterator.next();
            } while (!keyTuple2.key.equals(k));
            if (keyTuple2 != null) {
                this.fifo.remove(keyTuple2);
            }
        }
    }
    
    private class KeyTuple implements Comparable<KeyTuple>
    {
        private int index;
        private K key;
        
        public KeyTuple(final K key, final int index) {
            this.index = 0;
            this.key = key;
            this.index = index;
        }
        
        @Override
        public int compareTo(final KeyTuple keyTuple) {
            return this.index - keyTuple.index;
        }
        
        public K getKey() {
            return this.key;
        }
    }
}
