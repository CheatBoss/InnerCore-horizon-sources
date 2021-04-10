package org.mozilla.javascript;

import java.io.*;

public class ObjToIntMap implements Serializable
{
    private static final int A = -1640531527;
    private static final Object DELETED;
    private static final boolean check = false;
    static final long serialVersionUID = -1542220580748809402L;
    private int keyCount;
    private transient Object[] keys;
    private transient int occupiedCount;
    private int power;
    private transient int[] values;
    
    static {
        DELETED = new Object();
    }
    
    public ObjToIntMap() {
        this(4);
    }
    
    public ObjToIntMap(int power) {
        if (power < 0) {
            Kit.codeBug();
        }
        int n;
        for (n = power * 4 / 3, power = 2; 1 << power < n; ++power) {}
        this.power = power;
    }
    
    private int ensureIndex(final Object o) {
        final int hashCode = o.hashCode();
        int n = -1;
        int n3;
        final int n2 = n3 = -1;
        if (this.keys != null) {
            final int n4 = -1640531527 * hashCode;
            int n5 = n4 >>> 32 - this.power;
            final Object o2 = this.keys[n5];
            n = n5;
            n3 = n2;
            if (o2 != null) {
                final int n6 = 1 << this.power;
                if (o2 == o) {
                    return n5;
                }
                if (this.values[n6 + n5] == hashCode && o2.equals(o)) {
                    return n5;
                }
                n3 = n2;
                if (o2 == ObjToIntMap.DELETED) {
                    n3 = n5;
                }
                final int n7 = n6 - 1;
                final int tableLookupStep = tableLookupStep(n4, n7, this.power);
                while (true) {
                    n = (n5 + tableLookupStep & n7);
                    final Object o3 = this.keys[n];
                    if (o3 == null) {
                        break;
                    }
                    if (o3 == o) {
                        return n;
                    }
                    if (this.values[n6 + n] == hashCode && o3.equals(o)) {
                        return n;
                    }
                    n5 = n;
                    if (o3 != ObjToIntMap.DELETED) {
                        continue;
                    }
                    n5 = n;
                    if (n3 >= 0) {
                        continue;
                    }
                    n3 = n;
                    n5 = n;
                }
            }
        }
        if (n3 >= 0) {
            n = n3;
        }
        else {
            if (this.keys == null || this.occupiedCount * 4 >= (1 << this.power) * 3) {
                this.rehashTable();
                return this.insertNewKey(o, hashCode);
            }
            ++this.occupiedCount;
        }
        this.keys[n] = o;
        this.values[(1 << this.power) + n] = hashCode;
        ++this.keyCount;
        return n;
    }
    
    private int findIndex(final Object o) {
        if (this.keys != null) {
            final int hashCode = o.hashCode();
            final int n = -1640531527 * hashCode;
            int n2 = n >>> 32 - this.power;
            final Object o2 = this.keys[n2];
            if (o2 != null) {
                final int n3 = 1 << this.power;
                if (o2 == o) {
                    return n2;
                }
                if (this.values[n3 + n2] == hashCode && o2.equals(o)) {
                    return n2;
                }
                final int n4 = n3 - 1;
                final int tableLookupStep = tableLookupStep(n, n4, this.power);
                int n5;
                while (true) {
                    n5 = (n2 + tableLookupStep & n4);
                    final Object o3 = this.keys[n5];
                    if (o3 == null) {
                        return -1;
                    }
                    if (o3 == o) {
                        break;
                    }
                    n2 = n5;
                    if (this.values[n3 + n5] != hashCode) {
                        continue;
                    }
                    n2 = n5;
                    if (o3.equals(o)) {
                        break;
                    }
                }
                return n5;
            }
        }
        return -1;
    }
    
    private int insertNewKey(final Object o, final int n) {
        final int n2 = -1640531527 * n;
        int n3 = n2 >>> 32 - this.power;
        final int n4 = 1 << this.power;
        int n5 = n3;
        if (this.keys[n3] != null) {
            final int n6 = n4 - 1;
            final int tableLookupStep = tableLookupStep(n2, n6, this.power);
            do {
                n5 = (n3 = (n3 + tableLookupStep & n6));
            } while (this.keys[n5] != null);
        }
        this.keys[n5] = o;
        this.values[n4 + n5] = n;
        ++this.occupiedCount;
        ++this.keyCount;
        return n5;
    }
    
    private void readObject(final ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        final int keyCount = this.keyCount;
        if (keyCount != 0) {
            int i = 0;
            this.keyCount = 0;
            final int n = 1 << this.power;
            this.keys = new Object[n];
            this.values = new int[n * 2];
            while (i != keyCount) {
                final Object object = objectInputStream.readObject();
                this.values[this.insertNewKey(object, object.hashCode())] = objectInputStream.readInt();
                ++i;
            }
        }
    }
    
    private void rehashTable() {
        if (this.keys == null) {
            final int n = 1 << this.power;
            this.keys = new Object[n];
            this.values = new int[n * 2];
            return;
        }
        if (this.keyCount * 2 >= this.occupiedCount) {
            ++this.power;
        }
        final int n2 = 1 << this.power;
        final Object[] keys = this.keys;
        final int[] values = this.values;
        final int length = keys.length;
        this.keys = new Object[n2];
        this.values = new int[n2 * 2];
        int i = this.keyCount;
        int n3 = 0;
        this.keyCount = 0;
        this.occupiedCount = 0;
        while (i != 0) {
            final Object o = keys[n3];
            int n4 = i;
            if (o != null) {
                n4 = i;
                if (o != ObjToIntMap.DELETED) {
                    this.values[this.insertNewKey(o, values[length + n3])] = values[n3];
                    n4 = i - 1;
                }
            }
            ++n3;
            i = n4;
        }
    }
    
    private static int tableLookupStep(final int n, final int n2, int n3) {
        n3 = 32 - n3 * 2;
        if (n3 >= 0) {
            return (n >>> n3 & n2) | 0x1;
        }
        return (n2 >>> -n3 & n) | 0x1;
    }
    
    private void writeObject(final ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        int i = this.keyCount;
        int n = 0;
        while (i != 0) {
            final Object o = this.keys[n];
            int n2 = i;
            if (o != null) {
                n2 = i;
                if (o != ObjToIntMap.DELETED) {
                    n2 = i - 1;
                    objectOutputStream.writeObject(o);
                    objectOutputStream.writeInt(this.values[n]);
                }
            }
            ++n;
            i = n2;
        }
    }
    
    public void clear() {
        Object[] keys;
        for (int i = this.keys.length; i != 0; --i, keys[i] = null) {
            keys = this.keys;
        }
        this.keyCount = 0;
        this.occupiedCount = 0;
    }
    
    public int get(final Object o, final int n) {
        Object null_VALUE = o;
        if (o == null) {
            null_VALUE = UniqueTag.NULL_VALUE;
        }
        final int index = this.findIndex(null_VALUE);
        if (index >= 0) {
            return this.values[index];
        }
        return n;
    }
    
    public int getExisting(final Object o) {
        Object null_VALUE = o;
        if (o == null) {
            null_VALUE = UniqueTag.NULL_VALUE;
        }
        final int index = this.findIndex(null_VALUE);
        if (index >= 0) {
            return this.values[index];
        }
        Kit.codeBug();
        return 0;
    }
    
    public void getKeys(final Object[] array, int n) {
        int i = this.keyCount;
        final int n2 = 0;
        int n3 = n;
        n = n2;
        while (i != 0) {
            final Object o = this.keys[n];
            int n4 = i;
            int n5 = n3;
            if (o != null) {
                n4 = i;
                n5 = n3;
                if (o != ObjToIntMap.DELETED) {
                    Object o2;
                    if ((o2 = o) == UniqueTag.NULL_VALUE) {
                        o2 = null;
                    }
                    array[n3] = o2;
                    n5 = n3 + 1;
                    n4 = i - 1;
                }
            }
            ++n;
            i = n4;
            n3 = n5;
        }
    }
    
    public Object[] getKeys() {
        final Object[] array = new Object[this.keyCount];
        this.getKeys(array, 0);
        return array;
    }
    
    public boolean has(final Object o) {
        Object null_VALUE = o;
        if (o == null) {
            null_VALUE = UniqueTag.NULL_VALUE;
        }
        return this.findIndex(null_VALUE) >= 0;
    }
    
    final void initIterator(final Iterator iterator) {
        iterator.init(this.keys, this.values, this.keyCount);
    }
    
    public Object intern(final Object o) {
        boolean b = false;
        Object null_VALUE = o;
        if (o == null) {
            b = true;
            null_VALUE = UniqueTag.NULL_VALUE;
        }
        final int ensureIndex = this.ensureIndex(null_VALUE);
        this.values[ensureIndex] = 0;
        if (b) {
            return null;
        }
        return this.keys[ensureIndex];
    }
    
    public boolean isEmpty() {
        return this.keyCount == 0;
    }
    
    public Iterator newIterator() {
        return new Iterator(this);
    }
    
    public void put(final Object o, final int n) {
        Object null_VALUE = o;
        if (o == null) {
            null_VALUE = UniqueTag.NULL_VALUE;
        }
        this.values[this.ensureIndex(null_VALUE)] = n;
    }
    
    public void remove(final Object o) {
        Object null_VALUE = o;
        if (o == null) {
            null_VALUE = UniqueTag.NULL_VALUE;
        }
        final int index = this.findIndex(null_VALUE);
        if (index >= 0) {
            this.keys[index] = ObjToIntMap.DELETED;
            --this.keyCount;
        }
    }
    
    public int size() {
        return this.keyCount;
    }
    
    public static class Iterator
    {
        private int cursor;
        private Object[] keys;
        ObjToIntMap master;
        private int remaining;
        private int[] values;
        
        Iterator(final ObjToIntMap master) {
            this.master = master;
        }
        
        public boolean done() {
            return this.remaining < 0;
        }
        
        public Object getKey() {
            Object o;
            if ((o = this.keys[this.cursor]) == UniqueTag.NULL_VALUE) {
                o = null;
            }
            return o;
        }
        
        public int getValue() {
            return this.values[this.cursor];
        }
        
        final void init(final Object[] keys, final int[] values, final int remaining) {
            this.keys = keys;
            this.values = values;
            this.cursor = -1;
            this.remaining = remaining;
        }
        
        public void next() {
            if (this.remaining == -1) {
                Kit.codeBug();
            }
            if (this.remaining == 0) {
                this.remaining = -1;
                this.cursor = -1;
                return;
            }
            int n = this.cursor;
            while (true) {
                this.cursor = n + 1;
                final Object o = this.keys[this.cursor];
                if (o != null && o != ObjToIntMap.DELETED) {
                    break;
                }
                n = this.cursor;
            }
            --this.remaining;
        }
        
        public void setValue(final int n) {
            this.values[this.cursor] = n;
        }
        
        public void start() {
            this.master.initIterator(this);
            this.next();
        }
    }
}
