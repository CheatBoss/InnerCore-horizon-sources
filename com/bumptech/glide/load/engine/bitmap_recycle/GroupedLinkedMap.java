package com.bumptech.glide.load.engine.bitmap_recycle;

import java.util.*;

class GroupedLinkedMap<K extends Poolable, V>
{
    private final LinkedEntry<K, V> head;
    private final Map<K, LinkedEntry<K, V>> keyToEntry;
    
    GroupedLinkedMap() {
        this.head = new LinkedEntry<K, V>();
        this.keyToEntry = new HashMap<K, LinkedEntry<K, V>>();
    }
    
    private void makeHead(final LinkedEntry<K, V> linkedEntry) {
        removeEntry(linkedEntry);
        linkedEntry.prev = this.head;
        linkedEntry.next = this.head.next;
        updateEntry(linkedEntry);
    }
    
    private void makeTail(final LinkedEntry<K, V> linkedEntry) {
        removeEntry(linkedEntry);
        linkedEntry.prev = this.head.prev;
        linkedEntry.next = this.head;
        updateEntry(linkedEntry);
    }
    
    private static <K, V> void removeEntry(final LinkedEntry<K, V> linkedEntry) {
        linkedEntry.prev.next = linkedEntry.next;
        linkedEntry.next.prev = linkedEntry.prev;
    }
    
    private static <K, V> void updateEntry(final LinkedEntry<K, V> linkedEntry) {
        linkedEntry.next.prev = linkedEntry;
        linkedEntry.prev.next = linkedEntry;
    }
    
    public V get(final K k) {
        final LinkedEntry<K, V> linkedEntry = this.keyToEntry.get(k);
        LinkedEntry<K, V> linkedEntry3;
        if (linkedEntry == null) {
            final LinkedEntry<K, V> linkedEntry2 = new LinkedEntry<K, V>(k);
            this.keyToEntry.put(k, linkedEntry2);
            linkedEntry3 = linkedEntry2;
        }
        else {
            k.offer();
            linkedEntry3 = linkedEntry;
        }
        this.makeHead(linkedEntry3);
        return linkedEntry3.removeLast();
    }
    
    public void put(final K k, final V v) {
        final LinkedEntry<K, V> linkedEntry = this.keyToEntry.get(k);
        LinkedEntry<K, V> linkedEntry3;
        if (linkedEntry == null) {
            final LinkedEntry<K, V> linkedEntry2 = new LinkedEntry<K, V>(k);
            this.makeTail(linkedEntry2);
            this.keyToEntry.put(k, linkedEntry2);
            linkedEntry3 = linkedEntry2;
        }
        else {
            k.offer();
            linkedEntry3 = linkedEntry;
        }
        linkedEntry3.add(v);
    }
    
    public V removeLast() {
        for (Object o = this.head.prev; !o.equals(this.head); o = ((LinkedEntry)o).prev) {
            final V removeLast = ((LinkedEntry<Object, V>)o).removeLast();
            if (removeLast != null) {
                return removeLast;
            }
            removeEntry((LinkedEntry)o);
            this.keyToEntry.remove(((LinkedEntry<Object, Object>)o).key);
            ((Poolable)((LinkedEntry<Object, Object>)o).key).offer();
        }
        return null;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("GroupedLinkedMap( ");
        Object o = this.head.next;
        boolean b = false;
        while (!o.equals(this.head)) {
            b = true;
            sb.append('{');
            sb.append(((LinkedEntry<Object, Object>)o).key);
            sb.append(':');
            sb.append(((LinkedEntry)o).size());
            sb.append("}, ");
            o = ((LinkedEntry)o).next;
        }
        if (b) {
            sb.delete(sb.length() - 2, sb.length());
        }
        sb.append(" )");
        return sb.toString();
    }
    
    private static class LinkedEntry<K, V>
    {
        private final K key;
        LinkedEntry<K, V> next;
        LinkedEntry<K, V> prev;
        private List<V> values;
        
        public LinkedEntry() {
            this(null);
        }
        
        public LinkedEntry(final K key) {
            this.prev = this;
            this.next = this;
            this.key = key;
        }
        
        public void add(final V v) {
            if (this.values == null) {
                this.values = new ArrayList<V>();
            }
            this.values.add(v);
        }
        
        public V removeLast() {
            final int size = this.size();
            if (size > 0) {
                return this.values.remove(size - 1);
            }
            return null;
        }
        
        public int size() {
            if (this.values != null) {
                return this.values.size();
            }
            return 0;
        }
    }
}
