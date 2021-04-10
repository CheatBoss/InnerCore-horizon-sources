package org.mozilla.javascript;

import java.io.*;

public class UintMap implements Serializable
{
    private static final int A = -1640531527;
    private static final int DELETED = -2;
    private static final int EMPTY = -1;
    private static final boolean check = false;
    static final long serialVersionUID = 4242698212885848444L;
    private transient int ivaluesShift;
    private int keyCount;
    private transient int[] keys;
    private transient int occupiedCount;
    private int power;
    private transient Object[] values;
    
    public UintMap() {
        this(4);
    }
    
    public UintMap(int power) {
        if (power < 0) {
            Kit.codeBug();
        }
        int n;
        for (n = power * 4 / 3, power = 2; 1 << power < n; ++power) {}
        this.power = power;
    }
    
    private int ensureIndex(final int n, final boolean b) {
        int n2 = -1;
        final int n3 = -1;
        final int[] keys = this.keys;
        int n4 = n3;
        if (keys != null) {
            final int n5 = -1640531527 * n;
            final int n6 = n5 >>> 32 - this.power;
            final int n7 = keys[n6];
            if (n7 == n) {
                return n6;
            }
            n2 = n6;
            n4 = n3;
            if (n7 != -1) {
                int n8 = n3;
                if (n7 == -2) {
                    n8 = n6;
                }
                final int n9 = (1 << this.power) - 1;
                final int tableLookupStep = tableLookupStep(n5, n9, this.power);
                int n10 = n8;
                int n11 = n6;
                int i;
                int n12;
                do {
                    n2 = (n11 + tableLookupStep & n9);
                    i = keys[n2];
                    if (i == n) {
                        return n2;
                    }
                    n12 = n10;
                    if (i == -2 && (n12 = n10) < 0) {
                        n12 = n2;
                    }
                    n11 = n2;
                    n10 = n12;
                } while (i != -1);
                n4 = n12;
            }
        }
        if (n4 >= 0) {
            n2 = n4;
        }
        else {
            if (keys == null || this.occupiedCount * 4 >= (1 << this.power) * 3) {
                this.rehashTable(b);
                return this.insertNewKey(n);
            }
            ++this.occupiedCount;
        }
        keys[n2] = n;
        ++this.keyCount;
        return n2;
    }
    
    private int findIndex(final int n) {
        final int[] keys = this.keys;
        if (keys != null) {
            final int n2 = -1640531527 * n;
            int n3 = n2 >>> 32 - this.power;
            final int n4 = keys[n3];
            if (n4 == n) {
                return n3;
            }
            if (n4 != -1) {
                final int n5 = (1 << this.power) - 1;
                final int tableLookupStep = tableLookupStep(n2, n5, this.power);
                int i;
                do {
                    n3 = (n3 + tableLookupStep & n5);
                    i = keys[n3];
                    if (i == n) {
                        return n3;
                    }
                } while (i != -1);
            }
        }
        return -1;
    }
    
    private int insertNewKey(final int n) {
        final int[] keys = this.keys;
        final int n2 = -1640531527 * n;
        int n4;
        int n3 = n4 = n2 >>> 32 - this.power;
        if (keys[n3] != -1) {
            final int n5 = (1 << this.power) - 1;
            final int tableLookupStep = tableLookupStep(n2, n5, this.power);
            do {
                n4 = (n3 = (n3 + tableLookupStep & n5));
            } while (keys[n4] != -1);
        }
        keys[n4] = n;
        ++this.occupiedCount;
        ++this.keyCount;
        return n4;
    }
    
    private void readObject(final ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        final int keyCount = this.keyCount;
        if (keyCount != 0) {
            final int n = 0;
            this.keyCount = 0;
            final boolean boolean1 = objectInputStream.readBoolean();
            final boolean boolean2 = objectInputStream.readBoolean();
            final int ivaluesShift = 1 << this.power;
            if (boolean1) {
                this.keys = new int[ivaluesShift * 2];
                this.ivaluesShift = ivaluesShift;
            }
            else {
                this.keys = new int[ivaluesShift];
            }
            for (int i = 0; i != ivaluesShift; ++i) {
                this.keys[i] = -1;
            }
            if (boolean2) {
                this.values = new Object[ivaluesShift];
            }
            for (int j = n; j != keyCount; ++j) {
                final int insertNewKey = this.insertNewKey(objectInputStream.readInt());
                if (boolean1) {
                    this.keys[this.ivaluesShift + insertNewKey] = objectInputStream.readInt();
                }
                if (boolean2) {
                    this.values[insertNewKey] = objectInputStream.readObject();
                }
            }
        }
    }
    
    private void rehashTable(final boolean b) {
        if (this.keys != null && this.keyCount * 2 >= this.occupiedCount) {
            ++this.power;
        }
        final int ivaluesShift = 1 << this.power;
        final int[] keys = this.keys;
        final int ivaluesShift2 = this.ivaluesShift;
        if (ivaluesShift2 == 0 && !b) {
            this.keys = new int[ivaluesShift];
        }
        else {
            this.ivaluesShift = ivaluesShift;
            this.keys = new int[ivaluesShift * 2];
        }
        for (int i = 0; i != ivaluesShift; ++i) {
            this.keys[i] = -1;
        }
        final Object[] values = this.values;
        if (values != null) {
            this.values = new Object[ivaluesShift];
        }
        int j = this.keyCount;
        this.occupiedCount = 0;
        if (j != 0) {
            this.keyCount = 0;
            int n = 0;
            while (j != 0) {
                final int n2 = keys[n];
                int n3 = j;
                if (n2 != -1) {
                    n3 = j;
                    if (n2 != -2) {
                        final int insertNewKey = this.insertNewKey(n2);
                        if (values != null) {
                            this.values[insertNewKey] = values[n];
                        }
                        if (ivaluesShift2 != 0) {
                            this.keys[this.ivaluesShift + insertNewKey] = keys[ivaluesShift2 + n];
                        }
                        n3 = j - 1;
                    }
                }
                ++n;
                j = n3;
            }
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
        if (i != 0) {
            final int ivaluesShift = this.ivaluesShift;
            int n = 0;
            boolean b = true;
            final boolean b2 = ivaluesShift != 0;
            if (this.values == null) {
                b = false;
            }
            objectOutputStream.writeBoolean(b2);
            objectOutputStream.writeBoolean(b);
            while (i != 0) {
                final int n2 = this.keys[n];
                int n3 = i;
                if (n2 != -1) {
                    n3 = i;
                    if (n2 != -2) {
                        final int n4 = i - 1;
                        objectOutputStream.writeInt(n2);
                        if (b2) {
                            objectOutputStream.writeInt(this.keys[this.ivaluesShift + n]);
                        }
                        n3 = n4;
                        if (b) {
                            objectOutputStream.writeObject(this.values[n]);
                            n3 = n4;
                        }
                    }
                }
                ++n;
                i = n3;
            }
        }
    }
    
    public void clear() {
        final int n = 1 << this.power;
        if (this.keys != null) {
            for (int i = 0; i != n; ++i) {
                this.keys[i] = -1;
            }
            if (this.values != null) {
                for (int j = 0; j != n; ++j) {
                    this.values[j] = null;
                }
            }
        }
        this.ivaluesShift = 0;
        this.keyCount = 0;
        this.occupiedCount = 0;
    }
    
    public int getExistingInt(int index) {
        if (index < 0) {
            Kit.codeBug();
        }
        index = this.findIndex(index);
        if (index < 0) {
            Kit.codeBug();
            return 0;
        }
        if (this.ivaluesShift != 0) {
            return this.keys[this.ivaluesShift + index];
        }
        return 0;
    }
    
    public int getInt(int index, final int n) {
        if (index < 0) {
            Kit.codeBug();
        }
        index = this.findIndex(index);
        if (index < 0) {
            return n;
        }
        if (this.ivaluesShift != 0) {
            return this.keys[this.ivaluesShift + index];
        }
        return 0;
    }
    
    public int[] getKeys() {
        final int[] keys = this.keys;
        int i = this.keyCount;
        final int[] array = new int[i];
        int n = 0;
        while (i != 0) {
            final int n2 = keys[n];
            int n3 = i;
            if (n2 != -1) {
                n3 = i;
                if (n2 != -2) {
                    n3 = i - 1;
                    array[n3] = n2;
                }
            }
            ++n;
            i = n3;
        }
        return array;
    }
    
    public Object getObject(int index) {
        if (index < 0) {
            Kit.codeBug();
        }
        if (this.values != null) {
            index = this.findIndex(index);
            if (index >= 0) {
                return this.values[index];
            }
        }
        return null;
    }
    
    public boolean has(final int n) {
        if (n < 0) {
            Kit.codeBug();
        }
        return this.findIndex(n) >= 0;
    }
    
    public boolean isEmpty() {
        return this.keyCount == 0;
    }
    
    public void put(int ensureIndex, final int n) {
        if (ensureIndex < 0) {
            Kit.codeBug();
        }
        ensureIndex = this.ensureIndex(ensureIndex, true);
        if (this.ivaluesShift == 0) {
            final int ivaluesShift = 1 << this.power;
            if (this.keys.length != ivaluesShift * 2) {
                final int[] keys = new int[ivaluesShift * 2];
                System.arraycopy(this.keys, 0, keys, 0, ivaluesShift);
                this.keys = keys;
            }
            this.ivaluesShift = ivaluesShift;
        }
        this.keys[this.ivaluesShift + ensureIndex] = n;
    }
    
    public void put(int ensureIndex, final Object o) {
        if (ensureIndex < 0) {
            Kit.codeBug();
        }
        ensureIndex = this.ensureIndex(ensureIndex, false);
        if (this.values == null) {
            this.values = new Object[1 << this.power];
        }
        this.values[ensureIndex] = o;
    }
    
    public void remove(int index) {
        if (index < 0) {
            Kit.codeBug();
        }
        index = this.findIndex(index);
        if (index >= 0) {
            this.keys[index] = -2;
            --this.keyCount;
            if (this.values != null) {
                this.values[index] = null;
            }
            if (this.ivaluesShift != 0) {
                this.keys[this.ivaluesShift + index] = 0;
            }
        }
    }
    
    public int size() {
        return this.keyCount;
    }
}
