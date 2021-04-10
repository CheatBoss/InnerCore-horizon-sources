package java.util.concurrent;

import java.io.*;
import java.util.concurrent.atomic.*;
import java.util.function.*;
import java.lang.reflect.*;
import sun.misc.*;
import java.util.*;
import java.util.concurrent.locks.*;

public class ConcurrentHashMap<K, V> extends AbstractMap<K, V> implements ConcurrentMap<K, V>, Serializable
{
    private static final long serialVersionUID = 7249069246763182397L;
    private static final int MAXIMUM_CAPACITY = 1073741824;
    private static final int DEFAULT_CAPACITY = 16;
    static final int MAX_ARRAY_SIZE = 2147483639;
    private static final int DEFAULT_CONCURRENCY_LEVEL = 16;
    private static final float LOAD_FACTOR = 0.75f;
    static final int TREEIFY_THRESHOLD = 8;
    static final int UNTREEIFY_THRESHOLD = 6;
    static final int MIN_TREEIFY_CAPACITY = 64;
    private static final int MIN_TRANSFER_STRIDE = 16;
    private static int RESIZE_STAMP_BITS;
    private static final int MAX_RESIZERS;
    private static final int RESIZE_STAMP_SHIFT;
    static final int MOVED = -1;
    static final int TREEBIN = -2;
    static final int RESERVED = -3;
    static final int HASH_BITS = Integer.MAX_VALUE;
    static final int NCPU;
    private static final ObjectStreamField[] serialPersistentFields;
    transient volatile Node<K, V>[] table;
    private transient volatile Node<K, V>[] nextTable;
    private transient volatile long baseCount;
    private transient volatile int sizeCtl;
    private transient volatile int transferIndex;
    private transient volatile int cellsBusy;
    private transient volatile CounterCell[] counterCells;
    private transient KeySetView<K, V> keySet;
    private transient ValuesView<K, V> values;
    private transient EntrySetView<K, V> entrySet;
    private static final Unsafe U;
    private static final long SIZECTL;
    private static final long TRANSFERINDEX;
    private static final long BASECOUNT;
    private static final long CELLSBUSY;
    private static final long CELLVALUE;
    private static final long ABASE;
    private static final int ASHIFT;
    
    static final int spread(final int n) {
        return (n ^ n >>> 16) & Integer.MAX_VALUE;
    }
    
    private static final int tableSizeFor(final int n) {
        final int n2 = n - 1;
        final int n3 = n2 | n2 >>> 1;
        final int n4 = n3 | n3 >>> 2;
        final int n5 = n4 | n4 >>> 4;
        final int n6 = n5 | n5 >>> 8;
        final int n7 = n6 | n6 >>> 16;
        return (n7 < 0) ? 1 : ((n7 >= 1073741824) ? 1073741824 : (n7 + 1));
    }
    
    static Class<?> comparableClassFor(final Object o) {
        if (o instanceof Comparable) {
            final Class<?> class1;
            if ((class1 = o.getClass()) == String.class) {
                return class1;
            }
            final Type[] genericInterfaces;
            if ((genericInterfaces = class1.getGenericInterfaces()) != null) {
                for (int i = 0; i < genericInterfaces.length; ++i) {
                    final Type type;
                    final ParameterizedType parameterizedType;
                    final Type[] actualTypeArguments;
                    if ((type = genericInterfaces[i]) instanceof ParameterizedType && (parameterizedType = (ParameterizedType)type).getRawType() == Comparable.class && (actualTypeArguments = parameterizedType.getActualTypeArguments()) != null && actualTypeArguments.length == 1 && actualTypeArguments[0] == class1) {
                        return class1;
                    }
                }
            }
        }
        return null;
    }
    
    static int compareComparables(final Class<?> clazz, final Object o, final Object o2) {
        return (o2 == null || o2.getClass() != clazz) ? 0 : ((Comparable)o).compareTo(o2);
    }
    
    static final <K, V> Node<K, V> tabAt(final Node<K, V>[] array, final int n) {
        return (Node<K, V>)ConcurrentHashMap.U.getObjectVolatile(array, ((long)n << ConcurrentHashMap.ASHIFT) + ConcurrentHashMap.ABASE);
    }
    
    static final <K, V> boolean casTabAt(final Node<K, V>[] array, final int n, final Node<K, V> node, final Node<K, V> node2) {
        return ConcurrentHashMap.U.compareAndSwapObject(array, ((long)n << ConcurrentHashMap.ASHIFT) + ConcurrentHashMap.ABASE, node, node2);
    }
    
    static final <K, V> void setTabAt(final Node<K, V>[] array, final int n, final Node<K, V> node) {
        ConcurrentHashMap.U.putObjectVolatile(array, ((long)n << ConcurrentHashMap.ASHIFT) + ConcurrentHashMap.ABASE, node);
    }
    
    public ConcurrentHashMap() {
    }
    
    public ConcurrentHashMap(final int n) {
        if (n < 0) {
            throw new IllegalArgumentException();
        }
        this.sizeCtl = ((n >= 536870912) ? 1073741824 : tableSizeFor(n + (n >>> 1) + 1));
    }
    
    public ConcurrentHashMap(final Map<? extends K, ? extends V> map) {
        this.sizeCtl = 16;
        this.putAll(map);
    }
    
    public ConcurrentHashMap(final int n, final float n2) {
        this(n, n2, 1);
    }
    
    public ConcurrentHashMap(int n, final float n2, final int n3) {
        if (n2 <= 0.0f || n < 0 || n3 <= 0) {
            throw new IllegalArgumentException();
        }
        if (n < n3) {
            n = n3;
        }
        final long n4 = (long)(1.0 + n / n2);
        this.sizeCtl = ((n4 >= 1073741824L) ? 1073741824 : tableSizeFor((int)n4));
    }
    
    @Override
    public int size() {
        final long sumCount = this.sumCount();
        return (sumCount < 0L) ? 0 : ((sumCount > 2147483647L) ? Integer.MAX_VALUE : ((int)sumCount));
    }
    
    @Override
    public boolean isEmpty() {
        return this.sumCount() <= 0L;
    }
    
    @Override
    public V get(final Object o) {
        final int spread = spread(o.hashCode());
        final Node<K, V>[] table;
        final int length;
        Node<K, V> node;
        if ((table = this.table) != null && (length = table.length) > 0 && (node = tabAt(table, length - 1 & spread)) != null) {
            final int hash;
            if ((hash = node.hash) == spread) {
                final K key;
                if ((key = node.key) == o || (key != null && o.equals(key))) {
                    return node.val;
                }
            }
            else if (hash < 0) {
                final Node<K, V> find;
                return ((find = node.find(spread, o)) != null) ? find.val : null;
            }
            while ((node = node.next) != null) {
                final K key2;
                if (node.hash == spread && ((key2 = node.key) == o || (key2 != null && o.equals(key2)))) {
                    return node.val;
                }
            }
        }
        return null;
    }
    
    @Override
    public boolean containsKey(final Object o) {
        return this.get(o) != null;
    }
    
    @Override
    public boolean containsValue(final Object o) {
        if (o == null) {
            throw new NullPointerException();
        }
        final Node<K, V>[] table;
        if ((table = this.table) != null) {
            Node<K, V> advance;
            while ((advance = new Traverser<K, V>(table, table.length, 0, table.length).advance()) != null) {
                final V val;
                if ((val = advance.val) == o || (val != null && o.equals(val))) {
                    return true;
                }
            }
        }
        return false;
    }
    
    @Override
    public V put(final K k, final V v) {
        return this.putVal(k, v, false);
    }
    
    final V putVal(final K k, final V v, final boolean b) {
        if (k == null || v == null) {
            throw new NullPointerException();
        }
        final int spread = spread(k.hashCode());
        int n = 0;
        Object[] array = this.table;
        while (true) {
            final int length;
            if (array == null || (length = ((Node<K, V>[])array).length) == 0) {
                array = this.initTable();
            }
            else {
                final int n2;
                final Node<K, V> tab;
                if ((tab = tabAt((Node<K, V>[])array, n2 = (length - 1 & spread))) == null) {
                    if (casTabAt((Node<K, V>[])array, n2, null, new Node<K, V>(spread, k, v, null))) {
                        break;
                    }
                    continue;
                }
                else {
                    final int hash;
                    if ((hash = tab.hash) == -1) {
                        array = this.helpTransfer((Node<K, V>[])array, (Node<K, V>)tab);
                    }
                    else {
                        Object o = null;
                        synchronized (tab) {
                            Label_0308: {
                                if (tabAt((Node<K, V>[])array, n2) == tab) {
                                    if (hash >= 0) {
                                        n = 1;
                                        Node<K, V> next = tab;
                                        K key;
                                        while (next.hash != spread || ((key = next.key) != k && (key == null || !k.equals(key)))) {
                                            final Node<K, V> node = next;
                                            if ((next = next.next) == null) {
                                                node.next = (Node<K, V>)new Node<Object, Object>(spread, (K)k, (V)v, null);
                                                break Label_0308;
                                            }
                                            ++n;
                                        }
                                        o = next.val;
                                        if (!b) {
                                            next.val = v;
                                        }
                                    }
                                    else if (tab instanceof TreeBin) {
                                        n = 2;
                                        final TreeNode<K, V> putTreeVal;
                                        if ((putTreeVal = ((TreeBin<K, V>)tab).putTreeVal(spread, k, v)) != null) {
                                            o = putTreeVal.val;
                                            if (!b) {
                                                putTreeVal.val = v;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if (n == 0) {
                            continue;
                        }
                        if (n >= 8) {
                            this.treeifyBin((Node<K, V>[])array, n2);
                        }
                        if (o != null) {
                            return (V)o;
                        }
                        break;
                    }
                }
            }
        }
        this.addCount(1L, n);
        return null;
    }
    
    @Override
    public void putAll(final Map<? extends K, ? extends V> map) {
        this.tryPresize(map.size());
        for (final Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
            this.putVal(entry.getKey(), (V)entry.getValue(), false);
        }
    }
    
    @Override
    public V remove(final Object o) {
        return this.replaceNode(o, null, null);
    }
    
    final V replaceNode(final Object o, final V v, final Object o2) {
        final int spread = spread(o.hashCode());
        Map.Entry<K, V>[] array = (Map.Entry<K, V>[])this.table;
        int length;
        while (array != null && (length = ((Node<K, V>[])array).length) != 0) {
            final int n;
            final Node<K, V> tab;
            if ((tab = tabAt((Node<K, V>[])array, n = (length - 1 & spread))) == null) {
                break;
            }
            final int hash;
            if ((hash = tab.hash) == -1) {
                array = (Map.Entry<K, V>[])this.helpTransfer((Node<K, V>[])array, (Node<K, V>)tab);
            }
            else {
                Object o3 = null;
                boolean b = false;
                synchronized (tab) {
                    Label_0372: {
                        if (tabAt((Node<K, V>[])array, n) == tab) {
                            if (hash >= 0) {
                                b = true;
                                Node<K, V> next = tab;
                                Node<K, V> node = null;
                                K key;
                                while (next.hash != spread || ((key = next.key) != o && (key == null || !o.equals(key)))) {
                                    node = next;
                                    if ((next = next.next) == null) {
                                        break Label_0372;
                                    }
                                }
                                final V val = next.val;
                                if (o2 == null || o2 == val || (val != null && o2.equals(val))) {
                                    o3 = val;
                                    if (v != null) {
                                        next.val = v;
                                    }
                                    else if (node != null) {
                                        node.next = next.next;
                                    }
                                    else {
                                        setTabAt((Node<K, V>[])array, n, next.next);
                                    }
                                }
                            }
                            else if (tab instanceof TreeBin) {
                                b = true;
                                final TreeBin treeBin = (TreeBin<K, V>)tab;
                                final Object root;
                                final Object treeNode;
                                if ((root = treeBin.root) != null && (treeNode = ((TreeNode<K, V>)root).findTreeNode(spread, o, null)) != null) {
                                    final V val2 = ((TreeNode)treeNode).val;
                                    if (o2 == null || o2 == val2 || (val2 != null && o2.equals(val2))) {
                                        o3 = val2;
                                        if (v != null) {
                                            ((TreeNode)treeNode).val = (V)v;
                                        }
                                        else if (treeBin.removeTreeNode((TreeNode)treeNode)) {
                                            setTabAt((Node<K, V>[])array, n, (Node<K, V>)untreeify((Node<K, V>)treeBin.first));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if (!b) {
                    continue;
                }
                if (o3 != null) {
                    if (v == null) {
                        this.addCount(-1L, -1);
                    }
                    return (V)o3;
                }
                break;
            }
        }
        return null;
    }
    
    @Override
    public void clear() {
        long n = 0L;
        int n2 = 0;
        Map.Entry<K, V>[] array = (Map.Entry<K, V>[])this.table;
        while (array != null && n2 < ((Node<K, V>[])array).length) {
            final Node<K, V> tab = tabAt((Node<K, V>[])array, n2);
            if (tab == null) {
                ++n2;
            }
            else {
                final int hash;
                if ((hash = tab.hash) == -1) {
                    array = (Map.Entry<K, V>[])this.helpTransfer((Node<K, V>[])array, (Node<K, V>)tab);
                    n2 = 0;
                }
                else {
                    synchronized (tab) {
                        if (tabAt((Node<K, V>[])array, n2) != tab) {
                            continue;
                        }
                        final TreeNode<K, V> treeNode = (TreeNode<K, V>)((hash >= 0) ? tab : ((tab instanceof TreeBin) ? ((TreeBin)tab).first : null));
                        while (tab != null) {
                            --n;
                            final Node<K, V> next = (Node<K, V>)tab.next;
                        }
                        setTabAt((Node<K, V>[])array, n2++, null);
                    }
                }
            }
        }
        if (n != 0L) {
            this.addCount(n, -1);
        }
    }
    
    @Override
    public KeySetView<K, V> keySet() {
        final KeySetView<K, V> keySet;
        return ((keySet = this.keySet) != null) ? keySet : (this.keySet = new KeySetView<K, V>(this, null));
    }
    
    @Override
    public Collection<V> values() {
        final ValuesView<K, V> values;
        return (Collection<V>)(((values = this.values) != null) ? values : (this.values = new ValuesView<K, V>(this)));
    }
    
    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        final EntrySetView<K, V> entrySet;
        return ((entrySet = this.entrySet) != null) ? entrySet : (this.entrySet = new EntrySetView<K, V>(this));
    }
    
    @Override
    public int hashCode() {
        int n = 0;
        final Node<K, V>[] table;
        if ((table = this.table) != null) {
            Node<K, V> advance;
            while ((advance = new Traverser<K, V>(table, table.length, 0, table.length).advance()) != null) {
                n += (advance.key.hashCode() ^ advance.val.hashCode());
            }
        }
        return n;
    }
    
    @Override
    public String toString() {
        final Node<K, V>[] table;
        final int n = ((table = this.table) == null) ? 0 : ((Node<Object, Object>[])table).length;
        final Traverser traverser = new Traverser<K, V>((Node<Object, Object>[])table, n, 0, n);
        final StringBuilder sb = new StringBuilder();
        sb.append('{');
        Node<K, V> node;
        if ((node = traverser.advance()) != null) {
            while (true) {
                final K key = node.key;
                final V val = node.val;
                sb.append((key == this) ? "(this Map)" : key);
                sb.append('=');
                sb.append((val == this) ? "(this Map)" : val);
                if ((node = traverser.advance()) == null) {
                    break;
                }
                sb.append(',').append(' ');
            }
        }
        return sb.append('}').toString();
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o != this) {
            if (!(o instanceof Map)) {
                return false;
            }
            final Map map = (Map)o;
            final Node<K, V>[] table;
            final int n = ((table = this.table) == null) ? 0 : ((Node<Object, Object>[])table).length;
            Node<K, V> advance;
            while ((advance = new Traverser<K, V>(table, n, 0, n).advance()) != null) {
                final V val = advance.val;
                final Object value = map.get(advance.key);
                if (value == null || (value != val && !value.equals(val))) {
                    return false;
                }
            }
            for (final Map.Entry<K, Object> entry : map.entrySet()) {
                final K key;
                final Object value2;
                final Object value3;
                if ((key = entry.getKey()) == null || (value2 = entry.getValue()) == null || (value3 = this.get(key)) == null || (value2 != value3 && !value2.equals(value3))) {
                    return false;
                }
            }
        }
        return true;
    }
    
    private void writeObject(final ObjectOutputStream objectOutputStream) throws IOException {
        int n = 0;
        int i;
        for (i = 1; i < 16; i <<= 1) {
            ++n;
        }
        final int n2 = 32 - n;
        final int n3 = i - 1;
        final Segment[] array = new Segment[16];
        for (int j = 0; j < array.length; ++j) {
            array[j] = new Segment(0.75f);
        }
        objectOutputStream.putFields().put("segments", array);
        objectOutputStream.putFields().put("segmentShift", n2);
        objectOutputStream.putFields().put("segmentMask", n3);
        objectOutputStream.writeFields();
        final Node<K, V>[] table;
        if ((table = this.table) != null) {
            Node<K, V> advance;
            while ((advance = new Traverser<K, V>(table, table.length, 0, table.length).advance()) != null) {
                objectOutputStream.writeObject(advance.key);
                objectOutputStream.writeObject(advance.val);
            }
        }
        objectOutputStream.writeObject(null);
        objectOutputStream.writeObject(null);
    }
    
    private void readObject(final ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        this.sizeCtl = -1;
        objectInputStream.defaultReadObject();
        long n = 0L;
        Node<Object, Object> node = null;
        while (true) {
            final Object object = objectInputStream.readObject();
            final Object object2 = objectInputStream.readObject();
            if (object == null || object2 == null) {
                break;
            }
            node = new Node<Object, Object>(spread(object.hashCode()), object, object2, node);
            ++n;
        }
        if (n == 0L) {
            this.sizeCtl = 0;
        }
        else {
            int tableSize;
            if (n >= 536870912L) {
                tableSize = 1073741824;
            }
            else {
                final int n2 = (int)n;
                tableSize = tableSizeFor(n2 + (n2 >>> 1) + 1);
            }
            final Node[] table = new Node[tableSize];
            final int n3 = tableSize - 1;
            long baseCount = 0L;
            while (node != null) {
                final Node<Object, Object> next = node.next;
                final int hash = node.hash;
                final int n4 = hash & n3;
                final Node<K, V> tab;
                int n5;
                if ((tab = tabAt((Node<K, V>[])table, n4)) == null) {
                    n5 = 1;
                }
                else {
                    final Object key = node.key;
                    if (tab.hash < 0) {
                        if (((TreeBin<Object, Object>)tab).putTreeVal(hash, key, node.val) == null) {
                            ++baseCount;
                        }
                        n5 = 0;
                    }
                    else {
                        int n6 = 0;
                        n5 = 1;
                        for (Node<K, V> next2 = tab; next2 != null; next2 = next2.next) {
                            final K key2;
                            if (next2.hash == hash && ((key2 = next2.key) == key || (key2 != null && key.equals(key2)))) {
                                n5 = 0;
                                break;
                            }
                            ++n6;
                        }
                        if (n5 != 0 && n6 >= 8) {
                            n5 = 0;
                            ++baseCount;
                            node.next = (Node<Object, Object>)tab;
                            TreeNode<Object, Object> treeNode = null;
                            TreeNode<Object, Object> prev = null;
                            for (Node<Object, Object> next3 = node; next3 != null; next3 = next3.next) {
                                final TreeNode next4 = new TreeNode<Object, Object>(next3.hash, next3.key, next3.val, null, null);
                                if ((next4.prev = (TreeNode<K, V>)prev) == null) {
                                    treeNode = (TreeNode<Object, Object>)next4;
                                }
                                else {
                                    prev.next = (Node<Object, Object>)next4;
                                }
                                prev = (TreeNode<Object, Object>)next4;
                            }
                            setTabAt(table, n4, new TreeBin<K, V>((TreeNode<K, V>)treeNode));
                        }
                    }
                }
                if (n5 != 0) {
                    ++baseCount;
                    node.next = (Node<Object, Object>)tab;
                    setTabAt(table, n4, (Node<K, V>)node);
                }
                node = next;
            }
            this.table = (Node<K, V>[])table;
            this.sizeCtl = tableSize - (tableSize >>> 2);
            this.baseCount = baseCount;
        }
    }
    
    @Override
    public V putIfAbsent(final K k, final V v) {
        return this.putVal(k, v, true);
    }
    
    @Override
    public boolean remove(final Object o, final Object o2) {
        if (o == null) {
            throw new NullPointerException();
        }
        return o2 != null && this.replaceNode(o, null, o2) != null;
    }
    
    @Override
    public boolean replace(final K k, final V v, final V v2) {
        if (k == null || v == null || v2 == null) {
            throw new NullPointerException();
        }
        return this.replaceNode(k, v2, v) != null;
    }
    
    @Override
    public V replace(final K k, final V v) {
        if (k == null || v == null) {
            throw new NullPointerException();
        }
        return this.replaceNode(k, v, null);
    }
    
    @Override
    public V getOrDefault(final Object o, final V v) {
        final V value;
        return ((value = this.get(o)) == null) ? v : value;
    }
    
    @Override
    public void forEach(final BiConsumer<? super K, ? super V> biConsumer) {
        if (biConsumer == null) {
            throw new NullPointerException();
        }
        final Node<K, V>[] table;
        if ((table = this.table) != null) {
            Node<K, V> advance;
            while ((advance = new Traverser<K, V>(table, table.length, 0, table.length).advance()) != null) {
                biConsumer.accept(advance.key, advance.val);
            }
        }
    }
    
    @Override
    public void replaceAll(final BiFunction<? super K, ? super V, ? extends V> biFunction) {
        if (biFunction == null) {
            throw new NullPointerException();
        }
        final Node<K, V>[] table;
        if ((table = this.table) != null) {
            Node<K, V> advance;
            while ((advance = new Traverser<K, V>(table, table.length, 0, table.length).advance()) != null) {
                V v = advance.val;
                final K key = advance.key;
                V apply;
                do {
                    apply = (V)biFunction.apply(key, v);
                    if (apply == null) {
                        throw new NullPointerException();
                    }
                } while (this.replaceNode(key, apply, v) == null && (v = this.get(key)) != null);
            }
        }
    }
    
    @Override
    public V computeIfAbsent(final K k, final Function<? super K, ? extends V> function) {
        if (k == null || function == null) {
            throw new NullPointerException();
        }
        final int spread = spread(k.hashCode());
        Object o = null;
        int n = 0;
        Object[] array = this.table;
        while (true) {
            final int length;
            if (array == null || (length = ((Node<K, V>[])array).length) == 0) {
                array = this.initTable();
            }
            else {
                final int n2;
                final Map.Entry<K, V> tab;
                if ((tab = (Map.Entry<K, V>)tabAt((Node<K, V>[])array, n2 = (length - 1 & spread))) == null) {
                    final ReservationNode<Object, Object> reservationNode = new ReservationNode<Object, Object>();
                    synchronized (reservationNode) {
                        if (casTabAt((Node<K, V>[])array, n2, null, (Node<K, V>)reservationNode)) {
                            n = 1;
                            Node<Object, Object> node = null;
                            try {
                                if ((o = function.apply(k)) != null) {
                                    node = new Node<Object, Object>(spread, k, o, null);
                                }
                            }
                            finally {
                                setTabAt((Node<K, V>[])array, n2, (Node<K, V>)node);
                            }
                        }
                    }
                    if (n != 0) {
                        break;
                    }
                    continue;
                }
                else {
                    final int hash;
                    if ((hash = ((Node)tab).hash) == -1) {
                        array = this.helpTransfer((Node<K, V>[])array, (Node<K, V>)tab);
                    }
                    else {
                        boolean b = false;
                        synchronized (tab) {
                            Label_0433: {
                                if (tabAt((Node<K, V>[])array, n2) == tab) {
                                    if (hash >= 0) {
                                        n = 1;
                                        Node<K, V> next = (Node<K, V>)tab;
                                        K key;
                                        while (next.hash != spread || ((key = next.key) != k && (key == null || !k.equals(key)))) {
                                            final Node<K, V> node2 = next;
                                            if ((next = next.next) == null) {
                                                if ((o = function.apply(k)) != null) {
                                                    b = true;
                                                    node2.next = (Node<K, V>)new Node<Object, Object>(spread, (K)k, (V)o, null);
                                                }
                                                break Label_0433;
                                            }
                                            ++n;
                                        }
                                        o = next.val;
                                    }
                                    else if (tab instanceof TreeBin) {
                                        n = 2;
                                        final TreeBin treeBin = (TreeBin)tab;
                                        final Object root;
                                        final TreeNode<K, Object> treeNode;
                                        if ((root = treeBin.root) != null && (treeNode = ((TreeNode<K, Object>)root).findTreeNode(spread, k, null)) != null) {
                                            o = treeNode.val;
                                        }
                                        else if ((o = function.apply(k)) != null) {
                                            b = true;
                                            treeBin.putTreeVal(spread, k, o);
                                        }
                                    }
                                }
                            }
                        }
                        if (n == 0) {
                            continue;
                        }
                        if (n >= 8) {
                            this.treeifyBin((Node<K, V>[])array, n2);
                        }
                        if (!b) {
                            return (V)o;
                        }
                        break;
                    }
                }
            }
        }
        if (o != null) {
            this.addCount(1L, n);
        }
        return (V)o;
    }
    
    @Override
    public V computeIfPresent(final K k, final BiFunction<? super K, ? super V, ? extends V> biFunction) {
        if (k == null || biFunction == null) {
            throw new NullPointerException();
        }
        final int spread = spread(k.hashCode());
        V v = null;
        int n = 0;
        int n2 = 0;
        Object[] array = this.table;
        while (true) {
            final int length;
            if (array == null || (length = ((Node<K, V>[])array).length) == 0) {
                array = this.initTable();
            }
            else {
                final int n3;
                final Node<K, V> tab;
                if ((tab = tabAt((Node<K, V>[])array, n3 = (length - 1 & spread))) == null) {
                    break;
                }
                final int hash;
                if ((hash = tab.hash) == -1) {
                    array = this.helpTransfer((Node<K, V>[])array, (Node<K, V>)tab);
                }
                else {
                    synchronized (tab) {
                        Label_0371: {
                            if (tabAt((Node<K, V>[])array, n3) == tab) {
                                if (hash >= 0) {
                                    n2 = 1;
                                    Node<K, V> node = null;
                                    K key;
                                    while (tab.hash != spread || ((key = tab.key) != k && (key == null || !k.equals(key)))) {
                                        node = tab;
                                        final Node<K, V> next;
                                        if ((next = tab.next) == null) {
                                            break Label_0371;
                                        }
                                        ++n2;
                                    }
                                    v = (V)biFunction.apply(k, tab.val);
                                    if (v != null) {
                                        tab.val = v;
                                    }
                                    else {
                                        n = -1;
                                        final Node<K, V> next2 = tab.next;
                                        if (node != null) {
                                            node.next = next2;
                                        }
                                        else {
                                            setTabAt((Node<K, V>[])array, n3, next2);
                                        }
                                    }
                                }
                                else if (tab instanceof TreeBin) {
                                    n2 = 2;
                                    final TreeBin treeBin = (TreeBin<K, V>)tab;
                                    final Object root;
                                    final Object treeNode;
                                    if ((root = treeBin.root) != null && (treeNode = ((TreeNode<K, V>)root).findTreeNode(spread, k, null)) != null) {
                                        v = (V)biFunction.apply(k, (V)((TreeNode)treeNode).val);
                                        if (v != null) {
                                            ((TreeNode)treeNode).val = (V)v;
                                        }
                                        else {
                                            n = -1;
                                            if (treeBin.removeTreeNode((TreeNode)treeNode)) {
                                                setTabAt((Node<K, V>[])array, n3, (Node<K, V>)untreeify((Node<K, V>)treeBin.first));
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (n2 != 0) {
                        break;
                    }
                    continue;
                }
            }
        }
        if (n != 0) {
            this.addCount(n, n2);
        }
        return v;
    }
    
    @Override
    public V compute(final K k, final BiFunction<? super K, ? super V, ? extends V> biFunction) {
        if (k == null || biFunction == null) {
            throw new NullPointerException();
        }
        final int spread = spread(k.hashCode());
        V v = null;
        int n = 0;
        int n2 = 0;
        Object[] array = this.table;
        while (true) {
            final int length;
            if (array == null || (length = ((Node<K, V>[])array).length) == 0) {
                array = this.initTable();
            }
            else {
                final int n3;
                final Map.Entry<K, V> tab;
                if ((tab = (Map.Entry<K, V>)tabAt((Node<K, V>[])array, n3 = (length - 1 & spread))) == null) {
                    final ReservationNode<Object, Object> reservationNode = new ReservationNode<Object, Object>();
                    synchronized (reservationNode) {
                        if (casTabAt((Node<K, V>[])array, n3, null, (Node<K, V>)reservationNode)) {
                            n2 = 1;
                            Node<Object, Object> node = null;
                            try {
                                if ((v = (V)biFunction.apply(k, null)) != null) {
                                    n = 1;
                                    node = new Node<Object, Object>(spread, k, v, null);
                                }
                            }
                            finally {
                                setTabAt((Node<K, V>[])array, n3, (Node<K, V>)node);
                            }
                        }
                    }
                    if (n2 != 0) {
                        break;
                    }
                    continue;
                }
                else {
                    final int hash;
                    if ((hash = ((Node)tab).hash) == -1) {
                        array = this.helpTransfer((Node<K, V>[])array, (Node<K, V>)tab);
                    }
                    else {
                        synchronized (tab) {
                            Label_0560: {
                                if (tabAt((Node<K, V>[])array, n3) == tab) {
                                    if (hash >= 0) {
                                        n2 = 1;
                                        Node<K, V> node2 = null;
                                        K key;
                                        while (((Node)tab).hash != spread || ((key = ((Node)tab).key) != k && (key == null || !k.equals(key)))) {
                                            node2 = (Node<K, V>)tab;
                                            final Map.Entry<K, V> next;
                                            if ((next = ((Node)tab).next) == null) {
                                                v = (V)biFunction.apply(k, null);
                                                if (v != null) {
                                                    n = 1;
                                                    ((Node)tab).next = (Node<K, V>)new Node<Object, Object>(spread, (K)k, (V)v, null);
                                                }
                                                break Label_0560;
                                            }
                                            ++n2;
                                        }
                                        v = (V)biFunction.apply(k, (V)((Node)tab).val);
                                        if (v != null) {
                                            ((Node)tab).val = (V)v;
                                        }
                                        else {
                                            n = -1;
                                            final Node<K, V> next2 = ((Node)tab).next;
                                            if (node2 != null) {
                                                node2.next = (Node<K, V>)next2;
                                            }
                                            else {
                                                setTabAt((Node<K, V>[])array, n3, (Node<K, V>)next2);
                                            }
                                        }
                                    }
                                    else if (tab instanceof TreeBin) {
                                        n2 = 1;
                                        final TreeBin treeBin = (TreeBin)tab;
                                        final Node<K, V> root;
                                        TreeNode<K, V> treeNode;
                                        if ((root = (Node<K, V>)treeBin.root) != null) {
                                            treeNode = ((TreeNode<K, V>)root).findTreeNode(spread, k, null);
                                        }
                                        else {
                                            treeNode = null;
                                        }
                                        v = (V)biFunction.apply(k, (treeNode == null) ? null : treeNode.val);
                                        if (v != null) {
                                            if (treeNode != null) {
                                                treeNode.val = v;
                                            }
                                            else {
                                                n = 1;
                                                treeBin.putTreeVal(spread, k, v);
                                            }
                                        }
                                        else if (treeNode != null) {
                                            n = -1;
                                            if (treeBin.removeTreeNode(treeNode)) {
                                                setTabAt((Node<K, V>[])array, n3, (Node<K, V>)untreeify((Node<K, V>)treeBin.first));
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if (n2 == 0) {
                            continue;
                        }
                        if (n2 >= 8) {
                            this.treeifyBin((Node<K, V>[])array, n3);
                            break;
                        }
                        break;
                    }
                }
            }
        }
        if (n != 0) {
            this.addCount(n, n2);
        }
        return v;
    }
    
    @Override
    public V merge(final K k, final V v, final BiFunction<? super V, ? super V, ? extends V> biFunction) {
        if (k == null || v == null || biFunction == null) {
            throw new NullPointerException();
        }
        final int spread = spread(k.hashCode());
        V apply = null;
        int n = 0;
        int n2 = 0;
        Object[] array = this.table;
        while (true) {
            final int length;
            if (array == null || (length = ((Node<K, V>[])array).length) == 0) {
                array = this.initTable();
            }
            else {
                final int n3;
                final Map.Entry<K, V> tab;
                if ((tab = (Map.Entry<K, V>)tabAt((Node<K, V>[])array, n3 = (length - 1 & spread))) == null) {
                    if (casTabAt((Node<K, V>[])array, n3, null, new Node<K, V>(spread, k, v, null))) {
                        n = 1;
                        apply = v;
                        break;
                    }
                    continue;
                }
                else {
                    final int hash;
                    if ((hash = ((Node)tab).hash) == -1) {
                        array = this.helpTransfer((Node<K, V>[])array, (Node<K, V>)tab);
                    }
                    else {
                        synchronized (tab) {
                            Label_0469: {
                                if (tabAt((Node<K, V>[])array, n3) == tab) {
                                    if (hash >= 0) {
                                        n2 = 1;
                                        Node<K, V> node = null;
                                        K key;
                                        while (((Node)tab).hash != spread || ((key = ((Node)tab).key) != k && (key == null || !k.equals(key)))) {
                                            node = (Node<K, V>)tab;
                                            final Map.Entry<K, V> next;
                                            if ((next = ((Node)tab).next) == null) {
                                                n = 1;
                                                apply = v;
                                                ((Node)tab).next = (Node<K, V>)new Node<Object, Object>(spread, (K)k, (V)apply, null);
                                                break Label_0469;
                                            }
                                            ++n2;
                                        }
                                        apply = (V)biFunction.apply((V)((Node)tab).val, v);
                                        if (apply != null) {
                                            ((Node)tab).val = (V)apply;
                                        }
                                        else {
                                            n = -1;
                                            final Node<K, V> next2 = ((Node)tab).next;
                                            if (node != null) {
                                                node.next = (Node<K, V>)next2;
                                            }
                                            else {
                                                setTabAt((Node<K, V>[])array, n3, (Node<K, V>)next2);
                                            }
                                        }
                                    }
                                    else if (tab instanceof TreeBin) {
                                        n2 = 2;
                                        final TreeBin treeBin = (TreeBin)tab;
                                        final TreeNode<K, V> root = treeBin.root;
                                        final TreeNode<K, V> treeNode = (root == null) ? null : root.findTreeNode(spread, k, null);
                                        apply = ((treeNode == null) ? v : biFunction.apply((V)treeNode.val, v));
                                        if (apply != null) {
                                            if (treeNode != null) {
                                                treeNode.val = (V)apply;
                                            }
                                            else {
                                                n = 1;
                                                treeBin.putTreeVal(spread, k, apply);
                                            }
                                        }
                                        else if (treeNode != null) {
                                            n = -1;
                                            if (treeBin.removeTreeNode(treeNode)) {
                                                setTabAt((Node<K, V>[])array, n3, (Node<K, V>)untreeify((Node<K, V>)treeBin.first));
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if (n2 == 0) {
                            continue;
                        }
                        if (n2 >= 8) {
                            this.treeifyBin((Node<K, V>[])array, n3);
                            break;
                        }
                        break;
                    }
                }
            }
        }
        if (n != 0) {
            this.addCount(n, n2);
        }
        return apply;
    }
    
    public boolean contains(final Object o) {
        return this.containsValue(o);
    }
    
    public Enumeration<K> keys() {
        final Node<K, V>[] table;
        final int n = ((table = this.table) == null) ? 0 : ((Node<Object, Object>[])table).length;
        return new KeyIterator<K, Object>((Node<Object, Object>[])table, n, 0, n, (ConcurrentHashMap<Object, Object>)this);
    }
    
    public Enumeration<V> elements() {
        final Node<K, V>[] table;
        final int n = ((table = this.table) == null) ? 0 : ((Node<Object, Object>[])table).length;
        return new ValueIterator<Object, V>((Node<Object, Object>[])table, n, 0, n, (ConcurrentHashMap<Object, Object>)this);
    }
    
    public long mappingCount() {
        final long sumCount = this.sumCount();
        return (sumCount < 0L) ? 0L : sumCount;
    }
    
    public static <K> KeySetView<K, Boolean> newKeySet() {
        return new KeySetView<K, Boolean>(new ConcurrentHashMap<K, Boolean>(), Boolean.TRUE);
    }
    
    public static <K> KeySetView<K, Boolean> newKeySet(final int n) {
        return new KeySetView<K, Boolean>(new ConcurrentHashMap<K, Boolean>(n), Boolean.TRUE);
    }
    
    public KeySetView<K, V> keySet(final V v) {
        if (v == null) {
            throw new NullPointerException();
        }
        return new KeySetView<K, V>(this, v);
    }
    
    static final int resizeStamp(final int n) {
        return Integer.numberOfLeadingZeros(n) | 1 << ConcurrentHashMap.RESIZE_STAMP_BITS - 1;
    }
    
    private final Node<K, V>[] initTable() {
        Node<K, V>[] array;
        while ((array = this.table) == null || array.length == 0) {
            int sizeCtl;
            if ((sizeCtl = this.sizeCtl) < 0) {
                Thread.yield();
            }
            else {
                if (ConcurrentHashMap.U.compareAndSwapInt(this, ConcurrentHashMap.SIZECTL, sizeCtl, -1)) {
                    try {
                        if ((array = this.table) == null || array.length == 0) {
                            final int n = (sizeCtl > 0) ? sizeCtl : 16;
                            array = (this.table = (Node<K, V>[])new Node[n]);
                            sizeCtl = n - (n >>> 2);
                        }
                    }
                    finally {
                        this.sizeCtl = sizeCtl;
                    }
                    break;
                }
                continue;
            }
        }
        return array;
    }
    
    private final void addCount(final long n, final int n2) {
        long n3 = 0L;
        Label_0120: {
            final CounterCell[] counterCells;
            if ((counterCells = this.counterCells) == null) {
                final Unsafe u = ConcurrentHashMap.U;
                final long basecount = ConcurrentHashMap.BASECOUNT;
                final long baseCount = this.baseCount;
                if (u.compareAndSwapLong(this, basecount, baseCount, n3 = baseCount + n)) {
                    break Label_0120;
                }
            }
            boolean compareAndSwapLong = true;
            final int n4;
            final CounterCell counterCell;
            if (counterCells != null && (n4 = counterCells.length - 1) >= 0 && (counterCell = counterCells[ThreadLocalRandom.getProbe() & n4]) != null) {
                final Unsafe u2 = ConcurrentHashMap.U;
                final CounterCell counterCell2 = counterCell;
                final long cellvalue = ConcurrentHashMap.CELLVALUE;
                final long value = counterCell.value;
                if (compareAndSwapLong = u2.compareAndSwapLong(counterCell2, cellvalue, value, value + n)) {
                    if (n2 <= 1) {
                        return;
                    }
                    n3 = this.sumCount();
                    break Label_0120;
                }
            }
            this.fullAddCount(n, compareAndSwapLong);
            return;
        }
        if (n2 >= 0) {
            int sizeCtl;
            Node<K, V>[] table;
            int length;
            while (n3 >= (sizeCtl = this.sizeCtl) && (table = this.table) != null && (length = table.length) < 1073741824) {
                final int resizeStamp = resizeStamp(length);
                if (sizeCtl < 0) {
                    final Node<K, V>[] nextTable;
                    if (sizeCtl >>> ConcurrentHashMap.RESIZE_STAMP_SHIFT != resizeStamp || sizeCtl == resizeStamp + 1 || sizeCtl == resizeStamp + ConcurrentHashMap.MAX_RESIZERS || (nextTable = this.nextTable) == null) {
                        break;
                    }
                    if (this.transferIndex <= 0) {
                        break;
                    }
                    if (ConcurrentHashMap.U.compareAndSwapInt(this, ConcurrentHashMap.SIZECTL, sizeCtl, sizeCtl + 1)) {
                        this.transfer(table, nextTable);
                    }
                }
                else if (ConcurrentHashMap.U.compareAndSwapInt(this, ConcurrentHashMap.SIZECTL, sizeCtl, (resizeStamp << ConcurrentHashMap.RESIZE_STAMP_SHIFT) + 2)) {
                    this.transfer(table, null);
                }
                n3 = this.sumCount();
            }
        }
    }
    
    final Node<K, V>[] helpTransfer(final Node<K, V>[] array, final Node<K, V> node) {
        final Node<K, V>[] nextTable;
        if (array != null && node instanceof ForwardingNode && (nextTable = ((ForwardingNode)node).nextTable) != null) {
            final int resizeStamp = resizeStamp(array.length);
            int sizeCtl;
            while (nextTable == this.nextTable && this.table == array && (sizeCtl = this.sizeCtl) < 0 && sizeCtl >>> ConcurrentHashMap.RESIZE_STAMP_SHIFT == resizeStamp && sizeCtl != resizeStamp + 1 && sizeCtl != resizeStamp + ConcurrentHashMap.MAX_RESIZERS) {
                if (this.transferIndex <= 0) {
                    break;
                }
                if (ConcurrentHashMap.U.compareAndSwapInt(this, ConcurrentHashMap.SIZECTL, sizeCtl, sizeCtl + 1)) {
                    this.transfer(array, (Node<K, V>[])nextTable);
                    break;
                }
            }
            return (Node<K, V>[])nextTable;
        }
        return this.table;
    }
    
    private final void tryPresize(final int n) {
        final int n2 = (n >= 536870912) ? 1073741824 : tableSizeFor(n + (n >>> 1) + 1);
        int sizeCtl;
        while ((sizeCtl = this.sizeCtl) >= 0) {
            final Node<K, V>[] table = this.table;
            final int length;
            if (table == null || (length = table.length) == 0) {
                final int n3 = (sizeCtl > n2) ? sizeCtl : n2;
                if (!ConcurrentHashMap.U.compareAndSwapInt(this, ConcurrentHashMap.SIZECTL, sizeCtl, -1)) {
                    continue;
                }
                try {
                    if (this.table != table) {
                        continue;
                    }
                    this.table = (Node<K, V>[])new Node[n3];
                    sizeCtl = n3 - (n3 >>> 2);
                }
                finally {
                    this.sizeCtl = sizeCtl;
                }
            }
            else {
                if (n2 <= sizeCtl) {
                    break;
                }
                if (length >= 1073741824) {
                    break;
                }
                if (table != this.table) {
                    continue;
                }
                final int resizeStamp = resizeStamp(length);
                if (sizeCtl < 0) {
                    final Node<K, V>[] nextTable;
                    if (sizeCtl >>> ConcurrentHashMap.RESIZE_STAMP_SHIFT != resizeStamp || sizeCtl == resizeStamp + 1 || sizeCtl == resizeStamp + ConcurrentHashMap.MAX_RESIZERS || (nextTable = this.nextTable) == null) {
                        break;
                    }
                    if (this.transferIndex <= 0) {
                        break;
                    }
                    if (!ConcurrentHashMap.U.compareAndSwapInt(this, ConcurrentHashMap.SIZECTL, sizeCtl, sizeCtl + 1)) {
                        continue;
                    }
                    this.transfer(table, nextTable);
                }
                else {
                    if (!ConcurrentHashMap.U.compareAndSwapInt(this, ConcurrentHashMap.SIZECTL, sizeCtl, (resizeStamp << ConcurrentHashMap.RESIZE_STAMP_SHIFT) + 2)) {
                        continue;
                    }
                    this.transfer(table, null);
                }
            }
        }
    }
    
    private final void transfer(final Node<K, V>[] array, Node<K, V>[] array2) {
        final int length = array.length;
        int n;
        if ((n = ((ConcurrentHashMap.NCPU > 1) ? ((length >>> 3) / ConcurrentHashMap.NCPU) : length)) < 16) {
            n = 16;
        }
        if (array2 == null) {
            try {
                array2 = new Node[length << 1];
            }
            catch (Throwable t) {
                this.sizeCtl = Integer.MAX_VALUE;
                return;
            }
            this.nextTable = (Node<K, V>[])array2;
            this.transferIndex = length;
        }
        final int length2 = array2.length;
        final ForwardingNode forwardingNode = new ForwardingNode<K, V>(array2);
        int casTab = 1;
        boolean b = false;
        int n2 = 0;
        int n3 = 0;
        while (true) {
            if (casTab != 0) {
                if (--n2 >= n3 || b) {
                    casTab = 0;
                }
                else {
                    final int transferIndex;
                    if ((transferIndex = this.transferIndex) <= 0) {
                        n2 = -1;
                        casTab = 0;
                    }
                    else {
                        final int n4;
                        final int n5;
                        if (!ConcurrentHashMap.U.compareAndSwapInt(this, ConcurrentHashMap.TRANSFERINDEX, n4, n5 = (((n4 = transferIndex) > n) ? (transferIndex - n) : 0))) {
                            continue;
                        }
                        n3 = n5;
                        n2 = transferIndex - 1;
                        casTab = 0;
                    }
                }
            }
            else if (n2 < 0 || n2 >= length || n2 + length >= length2) {
                if (b) {
                    this.nextTable = null;
                    this.table = (Node<K, V>[])array2;
                    this.sizeCtl = (length << 1) - (length >>> 1);
                    return;
                }
                final int sizeCtl;
                if (!ConcurrentHashMap.U.compareAndSwapInt(this, ConcurrentHashMap.SIZECTL, sizeCtl = this.sizeCtl, sizeCtl - 1)) {
                    continue;
                }
                if (sizeCtl - 2 != resizeStamp(length) << ConcurrentHashMap.RESIZE_STAMP_SHIFT) {
                    return;
                }
                casTab = ((b = true) ? 1 : 0);
                n2 = length;
            }
            else {
                final Node<K, V> tab;
                if ((tab = tabAt(array, n2)) == null) {
                    casTab = (casTabAt(array, n2, null, (Node<K, V>)forwardingNode) ? 1 : 0);
                }
                else {
                    final int hash;
                    if ((hash = tab.hash) == -1) {
                        casTab = 1;
                    }
                    else {
                        synchronized (tab) {
                            if (tabAt(array, n2) != tab) {
                                continue;
                            }
                            if (hash >= 0) {
                                int n6 = hash & length;
                                for (Node<K, V> node = tab.next; node != null; node = node.next) {
                                    final int n7 = node.hash & length;
                                    if (n7 != n6) {
                                        n6 = n7;
                                    }
                                }
                                if (n6 == 0) {}
                                while (tab != tab) {
                                    final int hash2 = tab.hash;
                                    final K key = tab.key;
                                    final V val = tab.val;
                                    if ((hash2 & length) == 0x0) {
                                        final Node node2 = new Node(hash2, key, val, (Node<Object, Object>)tab);
                                    }
                                    else {
                                        final Node node3 = new Node(hash2, key, val, (Node<Object, Object>)tab);
                                    }
                                    final Node<K, V> next = (Node<K, V>)tab.next;
                                }
                                setTabAt(array2, n2, tab);
                                setTabAt(array2, n2 + length, tab);
                                setTabAt(array, n2, (Node<K, V>)forwardingNode);
                                casTab = 1;
                            }
                            else {
                                if (!(tab instanceof TreeBin)) {
                                    continue;
                                }
                                final TreeBin<Object, Object> treeBin = (TreeBin<Object, Object>)tab;
                                TreeNode<K, V> treeNode = null;
                                TreeNode<Object, Object> prev = null;
                                Object o = null;
                                TreeNode<Object, Object> prev2 = null;
                                int n8 = 0;
                                int n9 = 0;
                                for (Object o2 = treeBin.first; o2 != null; o2 = ((Node)o2).next) {
                                    final int hash3 = ((Node)o2).hash;
                                    final TreeNode treeNode2 = new TreeNode<K, V>(hash3, ((Node)o2).key, ((Node)o2).val, null, null);
                                    if ((hash3 & length) == 0x0) {
                                        if ((treeNode2.prev = (TreeNode<K, V>)prev) == null) {
                                            treeNode = (TreeNode<K, V>)treeNode2;
                                        }
                                        else {
                                            prev.next = (Node<Object, Object>)treeNode2;
                                        }
                                        prev = (TreeNode<Object, Object>)treeNode2;
                                        ++n8;
                                    }
                                    else {
                                        if ((treeNode2.prev = (TreeNode<K, V>)prev2) == null) {
                                            o = treeNode2;
                                        }
                                        else {
                                            prev2.next = (Node<Object, Object>)treeNode2;
                                        }
                                        prev2 = (TreeNode<Object, Object>)treeNode2;
                                        ++n9;
                                    }
                                }
                                final Node<K, V> node4 = (Node<K, V>)((n8 <= 6) ? untreeify((Node<K, V>)treeNode) : ((n9 != 0) ? new TreeBin<K, V>((TreeNode<K, V>)treeNode) : treeBin));
                                final Node<K, V> node5 = (Node<K, V>)((n9 <= 6) ? untreeify((Node<K, V>)o) : ((n8 != 0) ? new TreeBin<K, V>((TreeNode<K, V>)o) : treeBin));
                                setTabAt(array2, n2, node4);
                                setTabAt(array2, n2 + length, node5);
                                setTabAt(array, n2, (Node<K, V>)forwardingNode);
                                casTab = 1;
                            }
                        }
                    }
                }
            }
        }
    }
    
    final long sumCount() {
        final CounterCell[] counterCells = this.counterCells;
        long baseCount = this.baseCount;
        if (counterCells != null) {
            for (int i = 0; i < counterCells.length; ++i) {
                final CounterCell counterCell;
                if ((counterCell = counterCells[i]) != null) {
                    baseCount += counterCell.value;
                }
            }
        }
        return baseCount;
    }
    
    private final void fullAddCount(final long n, boolean b) {
        int n2;
        if ((n2 = ThreadLocalRandom.getProbe()) == 0) {
            ThreadLocalRandom.localInit();
            n2 = ThreadLocalRandom.getProbe();
            b = true;
        }
        int n3 = 0;
        while (true) {
            final CounterCell[] counterCells;
            final int length;
            if ((counterCells = this.counterCells) != null && (length = counterCells.length) > 0) {
                final CounterCell counterCell;
                if ((counterCell = counterCells[length - 1 & n2]) == null) {
                    if (this.cellsBusy == 0) {
                        final CounterCell counterCell2 = new CounterCell(n);
                        if (this.cellsBusy == 0 && ConcurrentHashMap.U.compareAndSwapInt(this, ConcurrentHashMap.CELLSBUSY, 0, 1)) {
                            boolean b2 = false;
                            try {
                                final CounterCell[] counterCells2;
                                final int length2;
                                final int n4;
                                if ((counterCells2 = this.counterCells) != null && (length2 = counterCells2.length) > 0 && counterCells2[n4 = (length2 - 1 & n2)] == null) {
                                    counterCells2[n4] = counterCell2;
                                    b2 = true;
                                }
                            }
                            finally {
                                this.cellsBusy = 0;
                            }
                            if (b2) {
                                break;
                            }
                            continue;
                        }
                    }
                    n3 = 0;
                }
                else if (!b) {
                    b = true;
                }
                else {
                    final Unsafe u = ConcurrentHashMap.U;
                    final CounterCell counterCell3 = counterCell;
                    final long cellvalue = ConcurrentHashMap.CELLVALUE;
                    final long value = counterCell.value;
                    if (u.compareAndSwapLong(counterCell3, cellvalue, value, value + n)) {
                        break;
                    }
                    if (this.counterCells != counterCells || length >= ConcurrentHashMap.NCPU) {
                        n3 = 0;
                    }
                    else if (n3 == 0) {
                        n3 = 1;
                    }
                    else if (this.cellsBusy == 0 && ConcurrentHashMap.U.compareAndSwapInt(this, ConcurrentHashMap.CELLSBUSY, 0, 1)) {
                        try {
                            if (this.counterCells == counterCells) {
                                final CounterCell[] counterCells3 = new CounterCell[length << 1];
                                for (int i = 0; i < length; ++i) {
                                    counterCells3[i] = counterCells[i];
                                }
                                this.counterCells = counterCells3;
                            }
                        }
                        finally {
                            this.cellsBusy = 0;
                        }
                        n3 = 0;
                        continue;
                    }
                }
                n2 = ThreadLocalRandom.advanceProbe(n2);
            }
            else if (this.cellsBusy == 0 && this.counterCells == counterCells && ConcurrentHashMap.U.compareAndSwapInt(this, ConcurrentHashMap.CELLSBUSY, 0, 1)) {
                boolean b3 = false;
                try {
                    if (this.counterCells == counterCells) {
                        final CounterCell[] counterCells4 = new CounterCell[2];
                        counterCells4[n2 & 0x1] = new CounterCell(n);
                        this.counterCells = counterCells4;
                        b3 = true;
                    }
                }
                finally {
                    this.cellsBusy = 0;
                }
                if (b3) {
                    break;
                }
                continue;
            }
            else {
                final Unsafe u2 = ConcurrentHashMap.U;
                final long basecount = ConcurrentHashMap.BASECOUNT;
                final long baseCount = this.baseCount;
                if (u2.compareAndSwapLong(this, basecount, baseCount, baseCount + n)) {
                    break;
                }
                continue;
            }
        }
    }
    
    private final void treeifyBin(final Node<K, V>[] array, final int n) {
        if (array != null) {
            final int length;
            if ((length = array.length) < 64) {
                this.tryPresize(length << 1);
            }
            else {
                final Node<K, V> tab;
                if ((tab = tabAt(array, n)) != null && tab.hash >= 0) {
                    synchronized (tab) {
                        if (tabAt(array, n) == tab) {
                            TreeNode<K, V> treeNode = null;
                            TreeNode<Object, Object> prev = null;
                            for (Node<K, V> next = tab; next != null; next = next.next) {
                                final TreeNode next2 = new TreeNode<Object, Object>(next.hash, next.key, next.val, null, null);
                                if ((next2.prev = (TreeNode<K, V>)prev) == null) {
                                    treeNode = (TreeNode<K, V>)next2;
                                }
                                else {
                                    prev.next = (Node<Object, Object>)next2;
                                }
                                prev = (TreeNode<Object, Object>)next2;
                            }
                            setTabAt(array, n, new TreeBin<K, V>((TreeNode<Object, Object>)treeNode));
                        }
                    }
                }
            }
        }
    }
    
    static <K, V> Node<K, V> untreeify(final Node<K, V> node) {
        Node<K, V> node2 = null;
        Node<K, V> node3 = null;
        for (Node<K, V> next = node; next != null; next = next.next) {
            final Node next2 = new Node<K, V>(next.hash, next.key, next.val, null);
            if (node3 == null) {
                node2 = (Node<K, V>)next2;
            }
            else {
                node3.next = (Node<K, V>)next2;
            }
            node3 = (Node<K, V>)next2;
        }
        return node2;
    }
    
    final int batchFor(final long n) {
        final long sumCount;
        if (n == Long.MAX_VALUE || (sumCount = this.sumCount()) <= 1L || sumCount < n) {
            return 0;
        }
        final int n2 = ForkJoinPool.getCommonPoolParallelism() << 2;
        final long n3;
        return (n <= 0L || (n3 = sumCount / n) >= n2) ? n2 : ((int)n3);
    }
    
    public void forEach(final long n, final BiConsumer<? super K, ? super V> biConsumer) {
        if (biConsumer == null) {
            throw new NullPointerException();
        }
        new ForEachMappingTask(null, this.batchFor(n), 0, 0, (Node<K, V>[])this.table, (BiConsumer<? super K, ? super V>)biConsumer).invoke();
    }
    
    public <U> void forEach(final long n, final BiFunction<? super K, ? super V, ? extends U> biFunction, final Consumer<? super U> consumer) {
        if (biFunction == null || consumer == null) {
            throw new NullPointerException();
        }
        new ForEachTransformedMappingTask(null, this.batchFor(n), 0, 0, (Node<K, V>[])this.table, (BiFunction<? super K, ? super V, ? extends U>)biFunction, (Consumer<? super U>)consumer).invoke();
    }
    
    public <U> U search(final long n, final BiFunction<? super K, ? super V, ? extends U> biFunction) {
        if (biFunction == null) {
            throw new NullPointerException();
        }
        return (U)new SearchMappingsTask(null, this.batchFor(n), 0, 0, (Node<Object, Object>[])this.table, (BiFunction<? super Object, ? super Object, ?>)biFunction, new AtomicReference<Object>()).invoke();
    }
    
    public <U> U reduce(final long n, final BiFunction<? super K, ? super V, ? extends U> biFunction, final BiFunction<? super U, ? super U, ? extends U> biFunction2) {
        if (biFunction == null || biFunction2 == null) {
            throw new NullPointerException();
        }
        return (U)new MapReduceMappingsTask(null, this.batchFor(n), 0, 0, (Node<Object, Object>[])this.table, null, (BiFunction<? super Object, ? super Object, ?>)biFunction, (BiFunction<? super Object, ? super Object, ?>)biFunction2).invoke();
    }
    
    public double reduceToDouble(final long n, final ToDoubleBiFunction<? super K, ? super V> toDoubleBiFunction, final double n2, final DoubleBinaryOperator doubleBinaryOperator) {
        if (toDoubleBiFunction == null || doubleBinaryOperator == null) {
            throw new NullPointerException();
        }
        return new MapReduceMappingsToDoubleTask(null, this.batchFor(n), 0, 0, (Node<Object, Object>[])this.table, null, (ToDoubleBiFunction<? super Object, ? super Object>)toDoubleBiFunction, n2, doubleBinaryOperator).invoke();
    }
    
    public long reduceToLong(final long n, final ToLongBiFunction<? super K, ? super V> toLongBiFunction, final long n2, final LongBinaryOperator longBinaryOperator) {
        if (toLongBiFunction == null || longBinaryOperator == null) {
            throw new NullPointerException();
        }
        return new MapReduceMappingsToLongTask(null, this.batchFor(n), 0, 0, (Node<Object, Object>[])this.table, null, (ToLongBiFunction<? super Object, ? super Object>)toLongBiFunction, n2, longBinaryOperator).invoke();
    }
    
    public int reduceToInt(final long n, final ToIntBiFunction<? super K, ? super V> toIntBiFunction, final int n2, final IntBinaryOperator intBinaryOperator) {
        if (toIntBiFunction == null || intBinaryOperator == null) {
            throw new NullPointerException();
        }
        return new MapReduceMappingsToIntTask(null, this.batchFor(n), 0, 0, (Node<Object, Object>[])this.table, null, (ToIntBiFunction<? super Object, ? super Object>)toIntBiFunction, n2, intBinaryOperator).invoke();
    }
    
    public void forEachKey(final long n, final Consumer<? super K> consumer) {
        if (consumer == null) {
            throw new NullPointerException();
        }
        new ForEachKeyTask(null, this.batchFor(n), 0, 0, (Node<K, V>[])this.table, (Consumer<? super K>)consumer).invoke();
    }
    
    public <U> void forEachKey(final long n, final Function<? super K, ? extends U> function, final Consumer<? super U> consumer) {
        if (function == null || consumer == null) {
            throw new NullPointerException();
        }
        new ForEachTransformedKeyTask(null, this.batchFor(n), 0, 0, (Node<K, V>[])this.table, (Function<? super K, ? extends U>)function, (Consumer<? super U>)consumer).invoke();
    }
    
    public <U> U searchKeys(final long n, final Function<? super K, ? extends U> function) {
        if (function == null) {
            throw new NullPointerException();
        }
        return (U)new SearchKeysTask(null, this.batchFor(n), 0, 0, (Node<Object, Object>[])this.table, (Function<? super Object, ?>)function, new AtomicReference<Object>()).invoke();
    }
    
    public K reduceKeys(final long n, final BiFunction<? super K, ? super K, ? extends K> biFunction) {
        if (biFunction == null) {
            throw new NullPointerException();
        }
        return (K)new ReduceKeysTask(null, this.batchFor(n), 0, 0, (Node<Object, Object>[])this.table, null, (BiFunction<? super Object, ? super Object, ?>)biFunction).invoke();
    }
    
    public <U> U reduceKeys(final long n, final Function<? super K, ? extends U> function, final BiFunction<? super U, ? super U, ? extends U> biFunction) {
        if (function == null || biFunction == null) {
            throw new NullPointerException();
        }
        return (U)new MapReduceKeysTask(null, this.batchFor(n), 0, 0, (Node<Object, Object>[])this.table, null, (Function<? super Object, ?>)function, (BiFunction<? super Object, ? super Object, ?>)biFunction).invoke();
    }
    
    public double reduceKeysToDouble(final long n, final ToDoubleFunction<? super K> toDoubleFunction, final double n2, final DoubleBinaryOperator doubleBinaryOperator) {
        if (toDoubleFunction == null || doubleBinaryOperator == null) {
            throw new NullPointerException();
        }
        return new MapReduceKeysToDoubleTask(null, this.batchFor(n), 0, 0, (Node<Object, Object>[])this.table, null, (ToDoubleFunction<? super Object>)toDoubleFunction, n2, doubleBinaryOperator).invoke();
    }
    
    public long reduceKeysToLong(final long n, final ToLongFunction<? super K> toLongFunction, final long n2, final LongBinaryOperator longBinaryOperator) {
        if (toLongFunction == null || longBinaryOperator == null) {
            throw new NullPointerException();
        }
        return new MapReduceKeysToLongTask(null, this.batchFor(n), 0, 0, (Node<Object, Object>[])this.table, null, (ToLongFunction<? super Object>)toLongFunction, n2, longBinaryOperator).invoke();
    }
    
    public int reduceKeysToInt(final long n, final ToIntFunction<? super K> toIntFunction, final int n2, final IntBinaryOperator intBinaryOperator) {
        if (toIntFunction == null || intBinaryOperator == null) {
            throw new NullPointerException();
        }
        return new MapReduceKeysToIntTask(null, this.batchFor(n), 0, 0, (Node<Object, Object>[])this.table, null, (ToIntFunction<? super Object>)toIntFunction, n2, intBinaryOperator).invoke();
    }
    
    public void forEachValue(final long n, final Consumer<? super V> consumer) {
        if (consumer == null) {
            throw new NullPointerException();
        }
        new ForEachValueTask(null, this.batchFor(n), 0, 0, (Node<K, V>[])this.table, (Consumer<? super V>)consumer).invoke();
    }
    
    public <U> void forEachValue(final long n, final Function<? super V, ? extends U> function, final Consumer<? super U> consumer) {
        if (function == null || consumer == null) {
            throw new NullPointerException();
        }
        new ForEachTransformedValueTask(null, this.batchFor(n), 0, 0, (Node<K, V>[])this.table, (Function<? super V, ? extends U>)function, (Consumer<? super U>)consumer).invoke();
    }
    
    public <U> U searchValues(final long n, final Function<? super V, ? extends U> function) {
        if (function == null) {
            throw new NullPointerException();
        }
        return (U)new SearchValuesTask(null, this.batchFor(n), 0, 0, (Node<Object, Object>[])this.table, (Function<? super Object, ?>)function, new AtomicReference<Object>()).invoke();
    }
    
    public V reduceValues(final long n, final BiFunction<? super V, ? super V, ? extends V> biFunction) {
        if (biFunction == null) {
            throw new NullPointerException();
        }
        return (V)new ReduceValuesTask(null, this.batchFor(n), 0, 0, (Node<Object, Object>[])this.table, null, (BiFunction<? super Object, ? super Object, ?>)biFunction).invoke();
    }
    
    public <U> U reduceValues(final long n, final Function<? super V, ? extends U> function, final BiFunction<? super U, ? super U, ? extends U> biFunction) {
        if (function == null || biFunction == null) {
            throw new NullPointerException();
        }
        return (U)new MapReduceValuesTask(null, this.batchFor(n), 0, 0, (Node<Object, Object>[])this.table, null, (Function<? super Object, ?>)function, (BiFunction<? super Object, ? super Object, ?>)biFunction).invoke();
    }
    
    public double reduceValuesToDouble(final long n, final ToDoubleFunction<? super V> toDoubleFunction, final double n2, final DoubleBinaryOperator doubleBinaryOperator) {
        if (toDoubleFunction == null || doubleBinaryOperator == null) {
            throw new NullPointerException();
        }
        return new MapReduceValuesToDoubleTask(null, this.batchFor(n), 0, 0, (Node<Object, Object>[])this.table, null, (ToDoubleFunction<? super Object>)toDoubleFunction, n2, doubleBinaryOperator).invoke();
    }
    
    public long reduceValuesToLong(final long n, final ToLongFunction<? super V> toLongFunction, final long n2, final LongBinaryOperator longBinaryOperator) {
        if (toLongFunction == null || longBinaryOperator == null) {
            throw new NullPointerException();
        }
        return new MapReduceValuesToLongTask(null, this.batchFor(n), 0, 0, (Node<Object, Object>[])this.table, null, (ToLongFunction<? super Object>)toLongFunction, n2, longBinaryOperator).invoke();
    }
    
    public int reduceValuesToInt(final long n, final ToIntFunction<? super V> toIntFunction, final int n2, final IntBinaryOperator intBinaryOperator) {
        if (toIntFunction == null || intBinaryOperator == null) {
            throw new NullPointerException();
        }
        return new MapReduceValuesToIntTask(null, this.batchFor(n), 0, 0, (Node<Object, Object>[])this.table, null, (ToIntFunction<? super Object>)toIntFunction, n2, intBinaryOperator).invoke();
    }
    
    public void forEachEntry(final long n, final Consumer<? super Map.Entry<K, V>> consumer) {
        if (consumer == null) {
            throw new NullPointerException();
        }
        new ForEachEntryTask(null, this.batchFor(n), 0, 0, (Node<K, V>[])this.table, (Consumer<? super Map.Entry<K, V>>)consumer).invoke();
    }
    
    public <U> void forEachEntry(final long n, final Function<Map.Entry<K, V>, ? extends U> function, final Consumer<? super U> consumer) {
        if (function == null || consumer == null) {
            throw new NullPointerException();
        }
        new ForEachTransformedEntryTask(null, this.batchFor(n), 0, 0, (Node<K, V>[])this.table, (Function<Map.Entry<K, V>, ? extends U>)function, (Consumer<? super U>)consumer).invoke();
    }
    
    public <U> U searchEntries(final long n, final Function<Map.Entry<K, V>, ? extends U> function) {
        if (function == null) {
            throw new NullPointerException();
        }
        return (U)new SearchEntriesTask(null, this.batchFor(n), 0, 0, (Node<Object, Object>[])this.table, (Function<Map.Entry<Object, Object>, ?>)function, new AtomicReference<Object>()).invoke();
    }
    
    public Map.Entry<K, V> reduceEntries(final long n, final BiFunction<Map.Entry<K, V>, Map.Entry<K, V>, ? extends Map.Entry<K, V>> biFunction) {
        if (biFunction == null) {
            throw new NullPointerException();
        }
        return new ReduceEntriesTask(null, this.batchFor(n), 0, 0, (Node<Object, Object>[])this.table, null, (BiFunction<Map.Entry<Object, Object>, Map.Entry<Object, Object>, ? extends Map.Entry<Object, Object>>)biFunction).invoke();
    }
    
    public <U> U reduceEntries(final long n, final Function<Map.Entry<K, V>, ? extends U> function, final BiFunction<? super U, ? super U, ? extends U> biFunction) {
        if (function == null || biFunction == null) {
            throw new NullPointerException();
        }
        return (U)new MapReduceEntriesTask(null, this.batchFor(n), 0, 0, (Node<Object, Object>[])this.table, null, (Function<Map.Entry<Object, Object>, ?>)function, (BiFunction<? super Object, ? super Object, ?>)biFunction).invoke();
    }
    
    public double reduceEntriesToDouble(final long n, final ToDoubleFunction<Map.Entry<K, V>> toDoubleFunction, final double n2, final DoubleBinaryOperator doubleBinaryOperator) {
        if (toDoubleFunction == null || doubleBinaryOperator == null) {
            throw new NullPointerException();
        }
        return new MapReduceEntriesToDoubleTask(null, this.batchFor(n), 0, 0, (Node<Object, Object>[])this.table, null, (ToDoubleFunction<Map.Entry<Object, Object>>)toDoubleFunction, n2, doubleBinaryOperator).invoke();
    }
    
    public long reduceEntriesToLong(final long n, final ToLongFunction<Map.Entry<K, V>> toLongFunction, final long n2, final LongBinaryOperator longBinaryOperator) {
        if (toLongFunction == null || longBinaryOperator == null) {
            throw new NullPointerException();
        }
        return new MapReduceEntriesToLongTask(null, this.batchFor(n), 0, 0, (Node<Object, Object>[])this.table, null, (ToLongFunction<Map.Entry<Object, Object>>)toLongFunction, n2, longBinaryOperator).invoke();
    }
    
    public int reduceEntriesToInt(final long n, final ToIntFunction<Map.Entry<K, V>> toIntFunction, final int n2, final IntBinaryOperator intBinaryOperator) {
        if (toIntFunction == null || intBinaryOperator == null) {
            throw new NullPointerException();
        }
        return new MapReduceEntriesToIntTask(null, this.batchFor(n), 0, 0, (Node<Object, Object>[])this.table, null, (ToIntFunction<Map.Entry<Object, Object>>)toIntFunction, n2, intBinaryOperator).invoke();
    }
    
    static {
        ConcurrentHashMap.RESIZE_STAMP_BITS = 16;
        MAX_RESIZERS = (1 << 32 - ConcurrentHashMap.RESIZE_STAMP_BITS) - 1;
        RESIZE_STAMP_SHIFT = 32 - ConcurrentHashMap.RESIZE_STAMP_BITS;
        NCPU = Runtime.getRuntime().availableProcessors();
        serialPersistentFields = new ObjectStreamField[] { new ObjectStreamField("segments", Segment[].class), new ObjectStreamField("segmentMask", Integer.TYPE), new ObjectStreamField("segmentShift", Integer.TYPE) };
        try {
            U = Unsafe.getUnsafe();
            final Class<ConcurrentHashMap> clazz = ConcurrentHashMap.class;
            SIZECTL = ConcurrentHashMap.U.objectFieldOffset(clazz.getDeclaredField("sizeCtl"));
            TRANSFERINDEX = ConcurrentHashMap.U.objectFieldOffset(clazz.getDeclaredField("transferIndex"));
            BASECOUNT = ConcurrentHashMap.U.objectFieldOffset(clazz.getDeclaredField("baseCount"));
            CELLSBUSY = ConcurrentHashMap.U.objectFieldOffset(clazz.getDeclaredField("cellsBusy"));
            CELLVALUE = ConcurrentHashMap.U.objectFieldOffset(CounterCell.class.getDeclaredField("value"));
            final Class<Node[]> clazz2 = Node[].class;
            ABASE = ConcurrentHashMap.U.arrayBaseOffset(clazz2);
            final int arrayIndexScale = ConcurrentHashMap.U.arrayIndexScale(clazz2);
            if ((arrayIndexScale & arrayIndexScale - 1) != 0x0) {
                throw new Error("data type scale not a power of two");
            }
            ASHIFT = 31 - Integer.numberOfLeadingZeros(arrayIndexScale);
        }
        catch (Exception ex) {
            throw new Error(ex);
        }
    }
    
    static class BaseIterator<K, V> extends Traverser<K, V>
    {
        final ConcurrentHashMap<K, V> map;
        Node<K, V> lastReturned;
        
        BaseIterator(final Node<K, V>[] array, final int n, final int n2, final int n3, final ConcurrentHashMap<K, V> map) {
            super(array, n, n2, n3);
            this.map = map;
            this.advance();
        }
        
        public final boolean hasNext() {
            return this.next != null;
        }
        
        public final boolean hasMoreElements() {
            return this.next != null;
        }
        
        public final void remove() {
            final Node<K, V> lastReturned;
            if ((lastReturned = this.lastReturned) == null) {
                throw new IllegalStateException();
            }
            this.lastReturned = null;
            this.map.replaceNode(lastReturned.key, null, null);
        }
    }
    
    static class Node<K, V> implements Map.Entry<K, V>
    {
        final int hash;
        final K key;
        volatile V val;
        volatile Node<K, V> next;
        
        Node(final int hash, final K key, final V val, final Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.val = val;
            this.next = next;
        }
        
        @Override
        public final K getKey() {
            return this.key;
        }
        
        @Override
        public final V getValue() {
            return this.val;
        }
        
        @Override
        public final int hashCode() {
            return this.key.hashCode() ^ this.val.hashCode();
        }
        
        @Override
        public final String toString() {
            return this.key + "=" + this.val;
        }
        
        @Override
        public final V setValue(final V v) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public final boolean equals(final Object o) {
            final Map.Entry entry;
            final K key;
            final Object value;
            final V val;
            return o instanceof Map.Entry && (key = (entry = (Map.Entry)o).getKey()) != null && (value = entry.getValue()) != null && (key == this.key || key.equals(this.key)) && (value == (val = this.val) || value.equals(val));
        }
        
        Node<K, V> find(final int n, final Object o) {
            Node next = this;
            if (o != null) {
                K key;
                while (next.hash != n || ((key = next.key) != o && (key == null || !o.equals(key)))) {
                    if ((next = next.next) == null) {
                        return null;
                    }
                }
                return next;
            }
            return null;
        }
    }
    
    static class Traverser<K, V>
    {
        Node<K, V>[] tab;
        Node<K, V> next;
        TableStack<K, V> stack;
        TableStack<K, V> spare;
        int index;
        int baseIndex;
        int baseLimit;
        final int baseSize;
        
        Traverser(final Node<K, V>[] tab, final int baseSize, final int n, final int baseLimit) {
            this.tab = tab;
            this.baseSize = baseSize;
            this.index = n;
            this.baseIndex = n;
            this.baseLimit = baseLimit;
            this.next = null;
        }
        
        final Node<K, V> advance() {
            Object next;
            if ((next = this.next) != null) {
                next = ((Node)next).next;
            }
            while (next == null) {
                final Node<K, V>[] tab;
                final int length;
                final int index;
                if (this.baseIndex >= this.baseLimit || (tab = this.tab) == null || (length = tab.length) <= (index = this.index) || index < 0) {
                    return this.next = null;
                }
                if ((next = ConcurrentHashMap.tabAt(tab, index)) != null && ((Node)next).hash < 0) {
                    if (next instanceof ForwardingNode) {
                        this.tab = (Node<K, V>[])((ForwardingNode)next).nextTable;
                        next = null;
                        this.pushState(tab, index, length);
                        continue;
                    }
                    if (next instanceof TreeBin) {
                        next = ((TreeBin)next).first;
                    }
                    else {
                        next = null;
                    }
                }
                if (this.stack != null) {
                    this.recoverState(length);
                }
                else {
                    if ((this.index = index + this.baseSize) < length) {
                        continue;
                    }
                    this.index = ++this.baseIndex;
                }
            }
            return this.next = (Node<K, V>)next;
        }
        
        private void pushState(final Node<K, V>[] tab, final int index, final int length) {
            TableStack<K, V> spare = this.spare;
            if (spare != null) {
                this.spare = spare.next;
            }
            else {
                spare = new TableStack<K, V>();
            }
            spare.tab = tab;
            spare.length = length;
            spare.index = index;
            spare.next = this.stack;
            this.stack = spare;
        }
        
        private void recoverState(int n) {
            TableStack<K, V> stack;
            int length;
            while ((stack = this.stack) != null && (this.index += (length = stack.length)) >= n) {
                n = length;
                this.index = stack.index;
                this.tab = stack.tab;
                stack.tab = null;
                final TableStack<K, V> next = stack.next;
                stack.next = this.spare;
                this.stack = next;
                this.spare = stack;
            }
            if (stack == null && (this.index += this.baseSize) >= n) {
                this.index = ++this.baseIndex;
            }
        }
    }
    
    static final class ForwardingNode<K, V> extends Node<K, V>
    {
        final Node<K, V>[] nextTable;
        
        ForwardingNode(final Node<K, V>[] nextTable) {
            super(-1, null, null, null);
            this.nextTable = nextTable;
        }
        
        @Override
        Node<K, V> find(final int n, final Object o) {
            Node<K, V>[] array = this.nextTable;
            int length;
            Node<K, V> node;
        Label_0005:
            while (o != null && array != null && (length = array.length) != 0 && (node = ConcurrentHashMap.tabAt(array, length - 1 & n)) != null) {
                int hash;
                K key;
                while ((hash = node.hash) != n || ((key = node.key) != o && (key == null || !o.equals(key)))) {
                    if (hash < 0) {
                        if (node instanceof ForwardingNode) {
                            array = ((ForwardingNode)node).nextTable;
                            continue Label_0005;
                        }
                        return node.find(n, o);
                    }
                    else {
                        if ((node = node.next) == null) {
                            return null;
                        }
                        continue;
                    }
                }
                return node;
            }
            return null;
        }
    }
    
    static final class TableStack<K, V>
    {
        int length;
        int index;
        Node<K, V>[] tab;
        TableStack<K, V> next;
    }
    
    static final class TreeBin<K, V> extends Node<K, V>
    {
        TreeNode<K, V> root;
        volatile TreeNode<K, V> first;
        volatile Thread waiter;
        volatile int lockState;
        static final int WRITER = 1;
        static final int WAITER = 2;
        static final int READER = 4;
        private static final Unsafe U;
        private static final long LOCKSTATE;
        
        static int tieBreakOrder(final Object o, final Object o2) {
            int compareTo;
            if (o == null || o2 == null || (compareTo = o.getClass().getName().compareTo(o2.getClass().getName())) == 0) {
                compareTo = ((System.identityHashCode(o) <= System.identityHashCode(o2)) ? -1 : 1);
            }
            return compareTo;
        }
        
        TreeBin(final TreeNode<K, V> first) {
            super(-2, null, null, null);
            this.first = first;
            TreeNode<K, V> balanceInsertion = null;
            TreeNode treeNode2;
            for (TreeNode<K, V> treeNode = first; treeNode != null; treeNode = (TreeNode<K, V>)treeNode2) {
                treeNode2 = (TreeNode)treeNode.next;
                final TreeNode<K, V> treeNode3 = treeNode;
                final TreeNode<K, V> treeNode4 = treeNode;
                final TreeNode<K, V> treeNode5 = null;
                treeNode4.right = (TreeNode<K, V>)treeNode5;
                treeNode3.left = (TreeNode<K, V>)treeNode5;
                if (balanceInsertion == null) {
                    treeNode.parent = null;
                    treeNode.red = false;
                    balanceInsertion = treeNode;
                }
                else {
                    final K key = treeNode.key;
                    final int hash = treeNode.hash;
                    Class<?> comparableClass = null;
                    TreeNode<K, V> treeNode6 = balanceInsertion;
                    int n;
                    TreeNode<K, V> parent;
                    do {
                        final K key2 = treeNode6.key;
                        final int hash2;
                        if ((hash2 = treeNode6.hash) > hash) {
                            n = -1;
                        }
                        else if (hash2 < hash) {
                            n = 1;
                        }
                        else if ((comparableClass == null && (comparableClass = ConcurrentHashMap.comparableClassFor(key)) == null) || (n = ConcurrentHashMap.compareComparables(comparableClass, key, key2)) == 0) {
                            n = tieBreakOrder(key, key2);
                        }
                        parent = treeNode6;
                    } while ((treeNode6 = ((n <= 0) ? treeNode6.left : treeNode6.right)) != null);
                    treeNode.parent = parent;
                    if (n <= 0) {
                        parent.left = treeNode;
                    }
                    else {
                        parent.right = treeNode;
                    }
                    balanceInsertion = balanceInsertion(balanceInsertion, treeNode);
                }
            }
            this.root = balanceInsertion;
            assert checkInvariants(this.root);
        }
        
        private final void lockRoot() {
            if (!TreeBin.U.compareAndSwapInt(this, TreeBin.LOCKSTATE, 0, 1)) {
                this.contendedLock();
            }
        }
        
        private final void unlockRoot() {
            this.lockState = 0;
        }
        
        private final void contendedLock() {
            boolean b = false;
            while (true) {
                final int lockState;
                if (((lockState = this.lockState) & 0xFFFFFFFD) == 0x0) {
                    if (TreeBin.U.compareAndSwapInt(this, TreeBin.LOCKSTATE, lockState, 1)) {
                        break;
                    }
                    continue;
                }
                else if ((lockState & 0x2) == 0x0) {
                    if (!TreeBin.U.compareAndSwapInt(this, TreeBin.LOCKSTATE, lockState, lockState | 0x2)) {
                        continue;
                    }
                    b = true;
                    this.waiter = Thread.currentThread();
                }
                else {
                    if (!b) {
                        continue;
                    }
                    LockSupport.park(this);
                }
            }
            if (b) {
                this.waiter = null;
            }
        }
        
        @Override
        final Node<K, V> find(final int n, final Object o) {
            if (o != null) {
                Node<K, V> node = this.first;
                while (node != null) {
                    final int lockState;
                    if (((lockState = this.lockState) & 0x3) != 0x0) {
                        final K key;
                        if (node.hash == n && ((key = node.key) == o || (key != null && o.equals(key)))) {
                            return node;
                        }
                        node = node.next;
                    }
                    else {
                        if (TreeBin.U.compareAndSwapInt(this, TreeBin.LOCKSTATE, lockState, lockState + 4)) {
                            TreeNode<K, V> treeNode;
                            try {
                                final TreeNode<K, V> root;
                                treeNode = (((root = this.root) == null) ? null : root.findTreeNode(n, o, null));
                            }
                            finally {
                                final Thread waiter;
                                if (TreeBin.U.getAndAddInt(this, TreeBin.LOCKSTATE, -4) == 6 && (waiter = this.waiter) != null) {
                                    LockSupport.unpark(waiter);
                                }
                            }
                            return treeNode;
                        }
                        continue;
                    }
                }
            }
            return null;
        }
        
        final TreeNode<K, V> putTreeVal(final int n, final K k, final V v) {
            Class<?> comparableClass = null;
            int n2 = 0;
            Object root = this.root;
            while (true) {
                while (root != null) {
                    final int hash;
                    int n3;
                    if ((hash = ((TreeNode)root).hash) > n) {
                        n3 = -1;
                    }
                    else if (hash < n) {
                        n3 = 1;
                    }
                    else {
                        final K key;
                        if ((key = ((TreeNode)root).key) == k || (key != null && k.equals(key))) {
                            return (TreeNode<K, V>)root;
                        }
                        if ((comparableClass == null && (comparableClass = ConcurrentHashMap.comparableClassFor(k)) == null) || (n3 = ConcurrentHashMap.compareComparables(comparableClass, k, key)) == 0) {
                            if (n2 == 0) {
                                n2 = 1;
                                final Node<K, V> left;
                                TreeNode<K, V> treeNode;
                                final Node<K, V> right;
                                if (((left = ((TreeNode)root).left) != null && (treeNode = ((TreeNode<K, V>)left).findTreeNode(n, k, comparableClass)) != null) || ((right = ((TreeNode)root).right) != null && (treeNode = ((TreeNode<K, V>)right).findTreeNode(n, k, comparableClass)) != null)) {
                                    return treeNode;
                                }
                            }
                            n3 = tieBreakOrder(k, key);
                        }
                    }
                    final TreeNode treeNode2 = (TreeNode)root;
                    if ((root = ((n3 <= 0) ? ((TreeNode)root).left : ((TreeNode)root).right)) == null) {
                        final TreeNode<K, V> first = this.first;
                        final TreeNode<K, V> right2 = this.first = new TreeNode<K, V>(n, k, v, first, treeNode2);
                        if (first != null) {
                            first.prev = right2;
                        }
                        if (n3 <= 0) {
                            treeNode2.left = (TreeNode<K, V>)right2;
                        }
                        else {
                            treeNode2.right = (TreeNode<K, V>)right2;
                        }
                        if (!treeNode2.red) {
                            right2.red = true;
                        }
                        else {
                            this.lockRoot();
                            try {
                                this.root = balanceInsertion(this.root, right2);
                            }
                            finally {
                                this.unlockRoot();
                            }
                        }
                        assert checkInvariants(this.root);
                        return null;
                    }
                }
                final TreeNode<K, V> treeNode3 = new TreeNode<K, V>(n, k, v, null, null);
                this.root = treeNode3;
                this.first = treeNode3;
                continue;
            }
        }
        
        final boolean removeTreeNode(final TreeNode<K, V> treeNode) {
            final TreeNode treeNode2 = (TreeNode)treeNode.next;
            final TreeNode<K, V> prev = treeNode.prev;
            if (prev == null) {
                this.first = (TreeNode<K, V>)treeNode2;
            }
            else {
                prev.next = (Node<K, V>)treeNode2;
            }
            if (treeNode2 != null) {
                treeNode2.prev = (TreeNode<K, V>)prev;
            }
            if (this.first == null) {
                this.root = null;
                return true;
            }
            TreeNode<K, V> root;
            final TreeNode<K, V> left;
            if ((root = this.root) == null || root.right == null || (left = root.left) == null || left.left == null) {
                return true;
            }
            this.lockRoot();
            try {
                final TreeNode<K, V> left2 = treeNode.left;
                final TreeNode<K, V> right = treeNode.right;
                TreeNode<K, V> treeNode3;
                if (left2 != null && right != null) {
                    TreeNode<K, V> right2;
                    TreeNode<K, V> left3;
                    for (right2 = right; (left3 = right2.left) != null; right2 = left3) {}
                    final boolean red = right2.red;
                    right2.red = treeNode.red;
                    treeNode.red = red;
                    final TreeNode<K, V> right3 = right2.right;
                    final TreeNode<K, V> parent = treeNode.parent;
                    if (right2 == right) {
                        treeNode.parent = right2;
                        right2.right = treeNode;
                    }
                    else {
                        final TreeNode<K, V> parent2 = right2.parent;
                        if ((treeNode.parent = parent2) != null) {
                            if (right2 == parent2.left) {
                                parent2.left = treeNode;
                            }
                            else {
                                parent2.right = treeNode;
                            }
                        }
                        if ((right2.right = right) != null) {
                            right.parent = right2;
                        }
                    }
                    treeNode.left = null;
                    if ((treeNode.right = right3) != null) {
                        right3.parent = treeNode;
                    }
                    if ((right2.left = left2) != null) {
                        left2.parent = right2;
                    }
                    if ((right2.parent = parent) == null) {
                        root = right2;
                    }
                    else if (treeNode == parent.left) {
                        parent.left = right2;
                    }
                    else {
                        parent.right = right2;
                    }
                    if (right3 != null) {
                        treeNode3 = right3;
                    }
                    else {
                        treeNode3 = treeNode;
                    }
                }
                else if (left2 != null) {
                    treeNode3 = left2;
                }
                else if (right != null) {
                    treeNode3 = right;
                }
                else {
                    treeNode3 = treeNode;
                }
                if (treeNode3 != treeNode) {
                    final TreeNode<K, V> treeNode4 = treeNode3;
                    final TreeNode<K, V> parent3 = treeNode.parent;
                    treeNode4.parent = parent3;
                    final TreeNode<K, V> treeNode5 = parent3;
                    if (treeNode5 == null) {
                        root = treeNode3;
                    }
                    else if (treeNode == treeNode5.left) {
                        treeNode5.left = treeNode3;
                    }
                    else {
                        treeNode5.right = treeNode3;
                    }
                    final TreeNode<K, V> left4 = null;
                    treeNode.parent = (TreeNode<K, V>)left4;
                    treeNode.right = (TreeNode<K, V>)left4;
                    treeNode.left = (TreeNode<K, V>)left4;
                }
                this.root = (treeNode.red ? root : balanceDeletion(root, treeNode3));
                final TreeNode<K, V> parent4;
                if (treeNode == treeNode3 && (parent4 = treeNode.parent) != null) {
                    if (treeNode == parent4.left) {
                        parent4.left = null;
                    }
                    else if (treeNode == parent4.right) {
                        parent4.right = null;
                    }
                    treeNode.parent = null;
                }
            }
            finally {
                this.unlockRoot();
            }
            assert checkInvariants(this.root);
            return false;
        }
        
        static <K, V> TreeNode<K, V> rotateLeft(TreeNode<K, V> treeNode, final TreeNode<K, V> treeNode2) {
            final TreeNode<K, V> right;
            if (treeNode2 != null && (right = treeNode2.right) != null) {
                final TreeNode<K, V> left = right.left;
                treeNode2.right = left;
                final TreeNode<K, V> treeNode3;
                if ((treeNode3 = left) != null) {
                    treeNode3.parent = treeNode2;
                }
                final TreeNode<K, V> treeNode4 = right;
                final TreeNode<K, V> parent = treeNode2.parent;
                treeNode4.parent = parent;
                final TreeNode<K, V> treeNode5;
                if ((treeNode5 = parent) == null) {
                    (treeNode = right).red = false;
                }
                else if (treeNode5.left == treeNode2) {
                    treeNode5.left = right;
                }
                else {
                    treeNode5.right = right;
                }
                right.left = treeNode2;
                treeNode2.parent = right;
            }
            return treeNode;
        }
        
        static <K, V> TreeNode<K, V> rotateRight(TreeNode<K, V> treeNode, final TreeNode<K, V> treeNode2) {
            final TreeNode<K, V> left;
            if (treeNode2 != null && (left = treeNode2.left) != null) {
                final TreeNode<K, V> right = left.right;
                treeNode2.left = right;
                final TreeNode<K, V> treeNode3;
                if ((treeNode3 = right) != null) {
                    treeNode3.parent = treeNode2;
                }
                final TreeNode<K, V> treeNode4 = left;
                final TreeNode<K, V> parent = treeNode2.parent;
                treeNode4.parent = parent;
                final TreeNode<K, V> treeNode5;
                if ((treeNode5 = parent) == null) {
                    (treeNode = left).red = false;
                }
                else if (treeNode5.right == treeNode2) {
                    treeNode5.right = left;
                }
                else {
                    treeNode5.left = left;
                }
                left.right = treeNode2;
                treeNode2.parent = left;
            }
            return treeNode;
        }
        
        static <K, V> TreeNode<K, V> balanceInsertion(TreeNode<K, V> treeNode, TreeNode<K, V> treeNode2) {
            treeNode2.red = true;
            TreeNode<K, V> treeNode3;
            while ((treeNode3 = treeNode2.parent) != null) {
                TreeNode<K, V> parent;
                if (!treeNode3.red || (parent = treeNode3.parent) == null) {
                    return (TreeNode<K, V>)treeNode;
                }
                final TreeNode<K, V> left;
                if (treeNode3 == (left = parent.left)) {
                    final TreeNode<K, V> right;
                    if ((right = parent.right) != null && right.red) {
                        right.red = false;
                        treeNode3.red = false;
                        parent.red = true;
                        treeNode2 = parent;
                    }
                    else {
                        if (treeNode2 == treeNode3.right) {
                            treeNode = rotateLeft(treeNode, (TreeNode<Object, Object>)(treeNode2 = treeNode3));
                            parent = (((treeNode3 = treeNode2.parent) == null) ? null : treeNode3.parent);
                        }
                        if (treeNode3 == null) {
                            continue;
                        }
                        treeNode3.red = false;
                        if (parent == null) {
                            continue;
                        }
                        parent.red = true;
                        treeNode = rotateRight(treeNode, (TreeNode<Object, Object>)parent);
                    }
                }
                else if (left != null && left.red) {
                    left.red = false;
                    treeNode3.red = false;
                    parent.red = true;
                    treeNode2 = parent;
                }
                else {
                    if (treeNode2 == treeNode3.left) {
                        treeNode = rotateRight(treeNode, (TreeNode<Object, Object>)(treeNode2 = treeNode3));
                        parent = (((treeNode3 = treeNode2.parent) == null) ? null : treeNode3.parent);
                    }
                    if (treeNode3 == null) {
                        continue;
                    }
                    treeNode3.red = false;
                    if (parent == null) {
                        continue;
                    }
                    parent.red = true;
                    treeNode = rotateLeft(treeNode, (TreeNode<Object, Object>)parent);
                }
            }
            treeNode2.red = false;
            return treeNode2;
        }
        
        static <K, V> TreeNode<K, V> balanceDeletion(TreeNode<K, V> node, TreeNode<K, V> o) {
            while (o != null && o != node) {
                Node<K, V> node2;
                if ((node2 = ((TreeNode)o).parent) == null) {
                    ((TreeNode)o).red = false;
                    return (TreeNode<K, V>)o;
                }
                if (((TreeNode)o).red) {
                    ((TreeNode)o).red = false;
                    return (TreeNode<K, V>)node;
                }
                Object left;
                if ((left = ((TreeNode)node2).left) == o) {
                    Object right;
                    if ((right = ((TreeNode)node2).right) != null && ((TreeNode)right).red) {
                        ((TreeNode)right).red = false;
                        ((TreeNode)node2).red = true;
                        node = rotateLeft((TreeNode<K, V>)node, (TreeNode<K, V>)node2);
                        right = (((node2 = ((TreeNode)o).parent) == null) ? null : ((TreeNode)node2).right);
                    }
                    if (right == null) {
                        o = node2;
                    }
                    else {
                        final TreeNode<K, V> left2 = ((TreeNode)right).left;
                        final TreeNode<K, V> right2 = ((TreeNode)right).right;
                        if ((right2 == null || !right2.red) && (left2 == null || !left2.red)) {
                            ((TreeNode)right).red = true;
                            o = node2;
                        }
                        else {
                            if (right2 == null || !right2.red) {
                                if (left2 != null) {
                                    left2.red = false;
                                }
                                ((TreeNode)right).red = true;
                                node = rotateRight((TreeNode<K, V>)node, (TreeNode<K, V>)right);
                                right = (((node2 = ((TreeNode)o).parent) == null) ? null : ((TreeNode)node2).right);
                            }
                            if (right != null) {
                                ((TreeNode)right).red = (node2 != null && ((TreeNode)node2).red);
                                final Node<K, V> right3;
                                if ((right3 = ((TreeNode)right).right) != null) {
                                    ((TreeNode)right3).red = false;
                                }
                            }
                            if (node2 != null) {
                                ((TreeNode)node2).red = false;
                                node = rotateLeft((TreeNode<K, V>)node, (TreeNode<K, V>)node2);
                            }
                            o = node;
                        }
                    }
                }
                else {
                    if (left != null && ((TreeNode)left).red) {
                        ((TreeNode)left).red = false;
                        ((TreeNode)node2).red = true;
                        node = rotateRight((TreeNode<K, V>)node, (TreeNode<K, V>)node2);
                        left = (((node2 = ((TreeNode)o).parent) == null) ? null : ((TreeNode)node2).left);
                    }
                    if (left == null) {
                        o = node2;
                    }
                    else {
                        final TreeNode<K, V> left3 = ((TreeNode)left).left;
                        final TreeNode<K, V> right4 = ((TreeNode)left).right;
                        if ((left3 == null || !left3.red) && (right4 == null || !right4.red)) {
                            ((TreeNode)left).red = true;
                            o = node2;
                        }
                        else {
                            if (left3 == null || !left3.red) {
                                if (right4 != null) {
                                    right4.red = false;
                                }
                                ((TreeNode)left).red = true;
                                node = rotateLeft((TreeNode<K, V>)node, (TreeNode<K, V>)left);
                                left = (((node2 = ((TreeNode)o).parent) == null) ? null : ((TreeNode)node2).left);
                            }
                            if (left != null) {
                                ((TreeNode)left).red = (node2 != null && ((TreeNode)node2).red);
                                final Node<K, V> left4;
                                if ((left4 = ((TreeNode)left).left) != null) {
                                    ((TreeNode)left4).red = false;
                                }
                            }
                            if (node2 != null) {
                                ((TreeNode)node2).red = false;
                                node = rotateRight((TreeNode<K, V>)node, (TreeNode<K, V>)node2);
                            }
                            o = node;
                        }
                    }
                }
            }
            return (TreeNode<K, V>)node;
        }
        
        static <K, V> boolean checkInvariants(final TreeNode<K, V> treeNode) {
            final TreeNode<K, V> parent = treeNode.parent;
            final TreeNode<K, V> left = treeNode.left;
            final TreeNode<K, V> right = treeNode.right;
            final TreeNode<K, V> prev = treeNode.prev;
            final TreeNode treeNode2 = (TreeNode)treeNode.next;
            return (prev == null || prev.next == treeNode) && (treeNode2 == null || treeNode2.prev == treeNode) && (parent == null || treeNode == parent.left || treeNode == parent.right) && (left == null || (left.parent == treeNode && left.hash <= treeNode.hash)) && (right == null || (right.parent == treeNode && right.hash >= treeNode.hash)) && (!treeNode.red || left == null || !left.red || right == null || !right.red) && (left == null || checkInvariants((TreeNode<Object, Object>)left)) && (right == null || checkInvariants((TreeNode<Object, Object>)right));
        }
        
        static {
            try {
                U = Unsafe.getUnsafe();
                LOCKSTATE = TreeBin.U.objectFieldOffset(TreeBin.class.getDeclaredField("lockState"));
            }
            catch (Exception ex) {
                throw new Error(ex);
            }
        }
    }
    
    static final class TreeNode<K, V> extends Node<K, V>
    {
        TreeNode<K, V> parent;
        TreeNode<K, V> left;
        TreeNode<K, V> right;
        TreeNode<K, V> prev;
        boolean red;
        
        TreeNode(final int n, final K k, final V v, final Node<K, V> node, final TreeNode<K, V> parent) {
            super(n, k, v, node);
            this.parent = parent;
        }
        
        @Override
        Node<K, V> find(final int n, final Object o) {
            return this.findTreeNode(n, o, null);
        }
        
        final TreeNode<K, V> findTreeNode(final int n, final Object o, Class<?> comparableClass) {
            if (o != null) {
                TreeNode treeNode = this;
                do {
                    final TreeNode<K, V> left = treeNode.left;
                    final TreeNode<K, V> right = treeNode.right;
                    final int hash;
                    if ((hash = treeNode.hash) > n) {
                        treeNode = left;
                    }
                    else if (hash < n) {
                        treeNode = right;
                    }
                    else {
                        final K key;
                        if ((key = treeNode.key) == o || (key != null && o.equals(key))) {
                            return (TreeNode<K, V>)treeNode;
                        }
                        if (left == null) {
                            treeNode = right;
                        }
                        else if (right == null) {
                            treeNode = left;
                        }
                        else {
                            final int compareComparables;
                            if ((comparableClass != null || (comparableClass = ConcurrentHashMap.comparableClassFor(o)) != null) && (compareComparables = ConcurrentHashMap.compareComparables(comparableClass, o, key)) != 0) {
                                treeNode = ((compareComparables < 0) ? left : right);
                            }
                            else {
                                final TreeNode treeNode2;
                                if ((treeNode2 = right.findTreeNode(n, o, comparableClass)) != null) {
                                    return (TreeNode<K, V>)treeNode2;
                                }
                                treeNode = left;
                            }
                        }
                    }
                } while (treeNode != null);
            }
            return null;
        }
    }
    
    abstract static class BulkTask<K, V, R> extends CountedCompleter<R>
    {
        Node<K, V>[] tab;
        Node<K, V> next;
        TableStack<K, V> stack;
        TableStack<K, V> spare;
        int index;
        int baseIndex;
        int baseLimit;
        final int baseSize;
        int batch;
        
        BulkTask(final BulkTask<K, V, ?> bulkTask, final int batch, final int n, final int baseLimit, final Node<K, V>[] tab) {
            super(bulkTask);
            this.batch = batch;
            this.baseIndex = n;
            this.index = n;
            this.tab = tab;
            if (tab == null) {
                final boolean b = false;
                this.baseLimit = (b ? 1 : 0);
                this.baseSize = (b ? 1 : 0);
            }
            else if (bulkTask == null) {
                final int length = tab.length;
                this.baseLimit = length;
                this.baseSize = length;
            }
            else {
                this.baseLimit = baseLimit;
                this.baseSize = bulkTask.baseSize;
            }
        }
        
        final Node<K, V> advance() {
            Object next;
            if ((next = this.next) != null) {
                next = ((Node)next).next;
            }
            while (next == null) {
                final Node<K, V>[] tab;
                final int length;
                final int index;
                if (this.baseIndex >= this.baseLimit || (tab = this.tab) == null || (length = tab.length) <= (index = this.index) || index < 0) {
                    return this.next = null;
                }
                if ((next = ConcurrentHashMap.tabAt(tab, index)) != null && ((Node)next).hash < 0) {
                    if (next instanceof ForwardingNode) {
                        this.tab = (Node<K, V>[])((ForwardingNode)next).nextTable;
                        next = null;
                        this.pushState(tab, index, length);
                        continue;
                    }
                    if (next instanceof TreeBin) {
                        next = ((TreeBin)next).first;
                    }
                    else {
                        next = null;
                    }
                }
                if (this.stack != null) {
                    this.recoverState(length);
                }
                else {
                    if ((this.index = index + this.baseSize) < length) {
                        continue;
                    }
                    this.index = ++this.baseIndex;
                }
            }
            return this.next = (Node<K, V>)next;
        }
        
        private void pushState(final Node<K, V>[] tab, final int index, final int length) {
            TableStack<K, V> spare = this.spare;
            if (spare != null) {
                this.spare = spare.next;
            }
            else {
                spare = new TableStack<K, V>();
            }
            spare.tab = tab;
            spare.length = length;
            spare.index = index;
            spare.next = this.stack;
            this.stack = spare;
        }
        
        private void recoverState(int n) {
            TableStack<K, V> stack;
            int length;
            while ((stack = this.stack) != null && (this.index += (length = stack.length)) >= n) {
                n = length;
                this.index = stack.index;
                this.tab = stack.tab;
                stack.tab = null;
                final TableStack<K, V> next = stack.next;
                stack.next = this.spare;
                this.stack = next;
                this.spare = stack;
            }
            if (stack == null && (this.index += this.baseSize) >= n) {
                this.index = ++this.baseIndex;
            }
        }
    }
    
    abstract static class CollectionView<K, V, E> implements Collection<E>, Serializable
    {
        private static final long serialVersionUID = 7249069246763182397L;
        final ConcurrentHashMap<K, V> map;
        private static final String oomeMsg = "Required array size too large";
        
        CollectionView(final ConcurrentHashMap<K, V> map) {
            this.map = map;
        }
        
        public ConcurrentHashMap<K, V> getMap() {
            return this.map;
        }
        
        @Override
        public final void clear() {
            this.map.clear();
        }
        
        @Override
        public final int size() {
            return this.map.size();
        }
        
        @Override
        public final boolean isEmpty() {
            return this.map.isEmpty();
        }
        
        @Override
        public abstract Iterator<E> iterator();
        
        @Override
        public abstract boolean contains(final Object p0);
        
        @Override
        public abstract boolean remove(final Object p0);
        
        @Override
        public final Object[] toArray() {
            final long mappingCount = this.map.mappingCount();
            if (mappingCount > 2147483639L) {
                throw new OutOfMemoryError("Required array size too large");
            }
            int n = (int)mappingCount;
            Object[] copy = new Object[n];
            int n2 = 0;
            for (final E next : this) {
                if (n2 == n) {
                    if (n >= 2147483639) {
                        throw new OutOfMemoryError("Required array size too large");
                    }
                    if (n >= 1073741819) {
                        n = 2147483639;
                    }
                    else {
                        n += (n >>> 1) + 1;
                    }
                    copy = Arrays.copyOf(copy, n);
                }
                copy[n2++] = next;
            }
            return (n2 == n) ? copy : Arrays.copyOf(copy, n2);
        }
        
        @Override
        public final <T> T[] toArray(final T[] array) {
            final long mappingCount = this.map.mappingCount();
            if (mappingCount > 2147483639L) {
                throw new OutOfMemoryError("Required array size too large");
            }
            final int n = (int)mappingCount;
            Object[] copy = (array.length >= n) ? array : ((Object[])Array.newInstance(array.getClass().getComponentType(), n));
            int length = ((T[])copy).length;
            int n2 = 0;
            for (final T next : this) {
                if (n2 == length) {
                    if (length >= 2147483639) {
                        throw new OutOfMemoryError("Required array size too large");
                    }
                    if (length >= 1073741819) {
                        length = 2147483639;
                    }
                    else {
                        length += (length >>> 1) + 1;
                    }
                    copy = Arrays.copyOf(copy, length);
                }
                copy[n2++] = next;
            }
            if (array == copy && n2 < length) {
                copy[n2] = null;
                return (T[])copy;
            }
            return (T[])((n2 == length) ? copy : Arrays.copyOf(copy, n2));
        }
        
        @Override
        public final String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append('[');
            final Iterator<CollectionView<K, V, CollectionView<K, V, CollectionView<K, V, CollectionView>>>> iterator = (Iterator<CollectionView<K, V, CollectionView<K, V, CollectionView<K, V, CollectionView>>>>)this.iterator();
            if (iterator.hasNext()) {
                while (true) {
                    final CollectionView<K, V, CollectionView<K, V, CollectionView<K, V, CollectionView>>> next = iterator.next();
                    sb.append((next == this) ? "(this Collection)" : next);
                    if (!iterator.hasNext()) {
                        break;
                    }
                    sb.append(',').append(' ');
                }
            }
            return sb.append(']').toString();
        }
        
        @Override
        public final boolean containsAll(final Collection<?> collection) {
            if (collection != this) {
                for (final Object next : collection) {
                    if (next == null || !this.contains(next)) {
                        return false;
                    }
                }
            }
            return true;
        }
        
        @Override
        public final boolean removeAll(final Collection<?> collection) {
            if (collection == null) {
                throw new NullPointerException();
            }
            boolean b = false;
            final Iterator<E> iterator = this.iterator();
            while (iterator.hasNext()) {
                if (collection.contains(iterator.next())) {
                    iterator.remove();
                    b = true;
                }
            }
            return b;
        }
        
        @Override
        public final boolean retainAll(final Collection<?> collection) {
            if (collection == null) {
                throw new NullPointerException();
            }
            boolean b = false;
            final Iterator<E> iterator = this.iterator();
            while (iterator.hasNext()) {
                if (!collection.contains(iterator.next())) {
                    iterator.remove();
                    b = true;
                }
            }
            return b;
        }
    }
    
    @Contended
    static final class CounterCell
    {
        volatile long value;
        
        CounterCell(final long value) {
            this.value = value;
        }
    }
    
    static final class EntryIterator<K, V> extends BaseIterator<K, V> implements Iterator<Map.Entry<K, V>>
    {
        EntryIterator(final Node<K, V>[] array, final int n, final int n2, final int n3, final ConcurrentHashMap<K, V> concurrentHashMap) {
            super(array, n, n2, n3, concurrentHashMap);
        }
        
        @Override
        public final Map.Entry<K, V> next() {
            final Node<K, V> next;
            if ((next = this.next) == null) {
                throw new NoSuchElementException();
            }
            final K key = next.key;
            final V val = next.val;
            this.lastReturned = next;
            this.advance();
            return new MapEntry<K, V>(key, val, (ConcurrentHashMap<Object, Object>)this.map);
        }
    }
    
    static final class MapEntry<K, V> implements Map.Entry<K, V>
    {
        final K key;
        V val;
        final ConcurrentHashMap<K, V> map;
        
        MapEntry(final K key, final V val, final ConcurrentHashMap<K, V> map) {
            this.key = key;
            this.val = val;
            this.map = map;
        }
        
        @Override
        public K getKey() {
            return this.key;
        }
        
        @Override
        public V getValue() {
            return this.val;
        }
        
        @Override
        public int hashCode() {
            return this.key.hashCode() ^ this.val.hashCode();
        }
        
        @Override
        public String toString() {
            return this.key + "=" + this.val;
        }
        
        @Override
        public boolean equals(final Object o) {
            final Map.Entry entry;
            final K key;
            final Object value;
            return o instanceof Map.Entry && (key = (entry = (Map.Entry)o).getKey()) != null && (value = entry.getValue()) != null && (key == this.key || key.equals(this.key)) && (value == this.val || value.equals(this.val));
        }
        
        @Override
        public V setValue(final V val) {
            if (val == null) {
                throw new NullPointerException();
            }
            final V val2 = this.val;
            this.val = val;
            this.map.put(this.key, val);
            return val2;
        }
    }
    
    static final class EntrySetView<K, V> extends CollectionView<K, V, Map.Entry<K, V>> implements Set<Map.Entry<K, V>>, Serializable
    {
        private static final long serialVersionUID = 2249069246763182397L;
        
        EntrySetView(final ConcurrentHashMap<K, V> concurrentHashMap) {
            super(concurrentHashMap);
        }
        
        @Override
        public boolean contains(final Object o) {
            final Map.Entry entry;
            final Object key;
            final V value;
            final Object value2;
            return o instanceof Map.Entry && (key = (entry = (Map.Entry)o).getKey()) != null && (value = this.map.get(key)) != null && (value2 = entry.getValue()) != null && (value2 == value || value2.equals(value));
        }
        
        @Override
        public boolean remove(final Object o) {
            final Map.Entry entry;
            final Object key;
            final Object value;
            return o instanceof Map.Entry && (key = (entry = (Map.Entry)o).getKey()) != null && (value = entry.getValue()) != null && this.map.remove(key, value);
        }
        
        @Override
        public Iterator<Map.Entry<K, V>> iterator() {
            final ConcurrentHashMap<K, V> map = this.map;
            final Node<Object, Object>[] table;
            final int n = ((table = (Node<Object, Object>[])map.table) == null) ? 0 : table.length;
            return new EntryIterator<K, V>(table, n, 0, n, (ConcurrentHashMap<Object, Object>)map);
        }
        
        @Override
        public boolean add(final Map.Entry<K, V> entry) {
            return this.map.putVal((K)entry.getKey(), (V)entry.getValue(), false) == null;
        }
        
        @Override
        public boolean addAll(final Collection<? extends Map.Entry<K, V>> collection) {
            boolean b = false;
            final Iterator<? extends Map.Entry<K, V>> iterator = collection.iterator();
            while (iterator.hasNext()) {
                if (this.add((Map.Entry<K, V>)iterator.next())) {
                    b = true;
                }
            }
            return b;
        }
        
        @Override
        public final int hashCode() {
            int n = 0;
            final Node<K, V>[] table;
            if ((table = this.map.table) != null) {
                Node<Object, Object> advance;
                while ((advance = new Traverser<Object, Object>((Node<Object, Object>[])table, ((Node<Object, Object>[])table).length, 0, ((Node<Object, Object>[])table).length).advance()) != null) {
                    n += advance.hashCode();
                }
            }
            return n;
        }
        
        @Override
        public final boolean equals(final Object o) {
            final Set set;
            return o instanceof Set && ((set = (Set)o) == this || (this.containsAll(set) && set.containsAll(this)));
        }
        
        @Override
        public Spliterator<Map.Entry<K, V>> spliterator() {
            final ConcurrentHashMap<K, V> map = this.map;
            final long sumCount = map.sumCount();
            final Node<K, V>[] table;
            final int n = ((table = (Node<K, V>[])map.table) == null) ? 0 : ((Node<Object, Object>[])table).length;
            return new EntrySpliterator<K, V>((Node<Object, Object>[])table, n, 0, n, (sumCount < 0L) ? 0L : sumCount, (ConcurrentHashMap<Object, Object>)map);
        }
        
        @Override
        public void forEach(final Consumer<? super Map.Entry<K, V>> consumer) {
            if (consumer == null) {
                throw new NullPointerException();
            }
            final Node<K, V>[] table;
            if ((table = this.map.table) != null) {
                Node<Object, Object> advance;
                while ((advance = new Traverser<Object, Object>((Node<Object, Object>[])table, ((Node<Object, Object>[])table).length, 0, ((Node<Object, Object>[])table).length).advance()) != null) {
                    consumer.accept((Object)new MapEntry(advance.key, advance.val, (ConcurrentHashMap<Object, Object>)this.map));
                }
            }
        }
    }
    
    static final class EntrySpliterator<K, V> extends Traverser<K, V> implements Spliterator<Map.Entry<K, V>>
    {
        final ConcurrentHashMap<K, V> map;
        long est;
        
        EntrySpliterator(final Node<K, V>[] array, final int n, final int n2, final int n3, final long est, final ConcurrentHashMap<K, V> map) {
            super(array, n, n2, n3);
            this.map = map;
            this.est = est;
        }
        
        @Override
        public Spliterator<Map.Entry<K, V>> trySplit() {
            final int baseIndex = this.baseIndex;
            final int baseLimit;
            final int n;
            Spliterator<Map.Entry<K, V>> spliterator;
            if ((n = baseIndex + (baseLimit = this.baseLimit) >>> 1) <= baseIndex) {
                spliterator = null;
            }
            else {
                final Node<K, V>[] tab;
                final int baseSize;
                final int baseLimit2;
                spliterator = (Spliterator<Map.Entry<K, V>>)new EntrySpliterator<Object, Object>((Node<Object, Object>[])tab, baseSize, baseLimit2, baseLimit, this.est >>>= 1, (ConcurrentHashMap<Object, Object>)this.map);
                tab = this.tab;
                baseSize = this.baseSize;
                baseLimit2 = n;
                this.baseLimit = baseLimit2;
            }
            return spliterator;
        }
        
        @Override
        public void forEachRemaining(final Consumer<? super Map.Entry<K, V>> consumer) {
            if (consumer == null) {
                throw new NullPointerException();
            }
            Node<K, V> advance;
            while ((advance = this.advance()) != null) {
                consumer.accept(new MapEntry<K, V>(advance.key, advance.val, (ConcurrentHashMap<Object, Object>)this.map));
            }
        }
        
        @Override
        public boolean tryAdvance(final Consumer<? super Map.Entry<K, V>> consumer) {
            if (consumer == null) {
                throw new NullPointerException();
            }
            final Node<K, V> advance;
            if ((advance = this.advance()) == null) {
                return false;
            }
            consumer.accept(new MapEntry<K, V>(advance.key, advance.val, (ConcurrentHashMap<Object, Object>)this.map));
            return true;
        }
        
        @Override
        public long estimateSize() {
            return this.est;
        }
        
        @Override
        public int characteristics() {
            return 4353;
        }
    }
    
    static final class ForEachEntryTask<K, V> extends BulkTask<K, V, Void>
    {
        final Consumer<? super Map.Entry<K, V>> action;
        
        ForEachEntryTask(final BulkTask<K, V, ?> bulkTask, final int n, final int n2, final int n3, final Node<K, V>[] array, final Consumer<? super Map.Entry<K, V>> action) {
            super(bulkTask, n, n2, n3, array);
            this.action = action;
        }
        
        @Override
        public final void compute() {
            final Consumer<? super Map.Entry<K, V>> action;
            if ((action = this.action) != null) {
                final int baseIndex = this.baseIndex;
                int baseLimit2;
                int baseLimit;
                while (this.batch > 0 && (baseLimit = (baseLimit2 = this.baseLimit) + baseIndex >>> 1) > baseIndex) {
                    this.addToPendingCount(1);
                    final int batch = this.batch >>> 1;
                    this.batch = batch;
                    new ForEachEntryTask((BulkTask<Object, Object, ?>)this, batch, this.baseLimit = baseLimit, baseLimit2, (Node<Object, Object>[])this.tab, (Consumer<? super Map.Entry<Object, Object>>)action).fork();
                }
                Node<K, V> advance;
                while ((advance = this.advance()) != null) {
                    action.accept(advance);
                }
                this.propagateCompletion();
            }
        }
    }
    
    static final class ForEachKeyTask<K, V> extends BulkTask<K, V, Void>
    {
        final Consumer<? super K> action;
        
        ForEachKeyTask(final BulkTask<K, V, ?> bulkTask, final int n, final int n2, final int n3, final Node<K, V>[] array, final Consumer<? super K> action) {
            super(bulkTask, n, n2, n3, array);
            this.action = action;
        }
        
        @Override
        public final void compute() {
            final Consumer<? super K> action;
            if ((action = this.action) != null) {
                final int baseIndex = this.baseIndex;
                int baseLimit2;
                int baseLimit;
                while (this.batch > 0 && (baseLimit = (baseLimit2 = this.baseLimit) + baseIndex >>> 1) > baseIndex) {
                    this.addToPendingCount(1);
                    final int batch = this.batch >>> 1;
                    this.batch = batch;
                    new ForEachKeyTask((BulkTask<Object, Object, ?>)this, batch, this.baseLimit = baseLimit, baseLimit2, (Node<Object, Object>[])this.tab, (Consumer<? super Object>)action).fork();
                }
                Node<K, V> advance;
                while ((advance = this.advance()) != null) {
                    action.accept(advance.key);
                }
                this.propagateCompletion();
            }
        }
    }
    
    static final class ForEachMappingTask<K, V> extends BulkTask<K, V, Void>
    {
        final BiConsumer<? super K, ? super V> action;
        
        ForEachMappingTask(final BulkTask<K, V, ?> bulkTask, final int n, final int n2, final int n3, final Node<K, V>[] array, final BiConsumer<? super K, ? super V> action) {
            super(bulkTask, n, n2, n3, array);
            this.action = action;
        }
        
        @Override
        public final void compute() {
            final BiConsumer<? super K, ? super V> action;
            if ((action = this.action) != null) {
                final int baseIndex = this.baseIndex;
                int baseLimit2;
                int baseLimit;
                while (this.batch > 0 && (baseLimit = (baseLimit2 = this.baseLimit) + baseIndex >>> 1) > baseIndex) {
                    this.addToPendingCount(1);
                    final int batch = this.batch >>> 1;
                    this.batch = batch;
                    new ForEachMappingTask((BulkTask<Object, Object, ?>)this, batch, this.baseLimit = baseLimit, baseLimit2, (Node<Object, Object>[])this.tab, (BiConsumer<? super Object, ? super Object>)action).fork();
                }
                Node<K, V> advance;
                while ((advance = this.advance()) != null) {
                    action.accept(advance.key, advance.val);
                }
                this.propagateCompletion();
            }
        }
    }
    
    static final class ForEachTransformedEntryTask<K, V, U> extends BulkTask<K, V, Void>
    {
        final Function<Map.Entry<K, V>, ? extends U> transformer;
        final Consumer<? super U> action;
        
        ForEachTransformedEntryTask(final BulkTask<K, V, ?> bulkTask, final int n, final int n2, final int n3, final Node<K, V>[] array, final Function<Map.Entry<K, V>, ? extends U> transformer, final Consumer<? super U> action) {
            super(bulkTask, n, n2, n3, array);
            this.transformer = transformer;
            this.action = action;
        }
        
        @Override
        public final void compute() {
            final Function<Map.Entry<K, V>, ? extends U> transformer;
            final Consumer<? super U> action;
            if ((transformer = this.transformer) != null && (action = this.action) != null) {
                final int baseIndex = this.baseIndex;
                int baseLimit2;
                int baseLimit;
                while (this.batch > 0 && (baseLimit = (baseLimit2 = this.baseLimit) + baseIndex >>> 1) > baseIndex) {
                    this.addToPendingCount(1);
                    final int batch = this.batch >>> 1;
                    this.batch = batch;
                    new ForEachTransformedEntryTask((BulkTask<Object, Object, ?>)this, batch, this.baseLimit = baseLimit, baseLimit2, (Node<Object, Object>[])this.tab, (Function<Map.Entry<Object, Object>, ?>)transformer, (Consumer<? super Object>)action).fork();
                }
                Node<K, V> advance;
                while ((advance = this.advance()) != null) {
                    final U apply;
                    if ((apply = (U)transformer.apply(advance)) != null) {
                        action.accept(apply);
                    }
                }
                this.propagateCompletion();
            }
        }
    }
    
    static final class ForEachTransformedKeyTask<K, V, U> extends BulkTask<K, V, Void>
    {
        final Function<? super K, ? extends U> transformer;
        final Consumer<? super U> action;
        
        ForEachTransformedKeyTask(final BulkTask<K, V, ?> bulkTask, final int n, final int n2, final int n3, final Node<K, V>[] array, final Function<? super K, ? extends U> transformer, final Consumer<? super U> action) {
            super(bulkTask, n, n2, n3, array);
            this.transformer = transformer;
            this.action = action;
        }
        
        @Override
        public final void compute() {
            final Function<? super K, ? extends U> transformer;
            final Consumer<? super U> action;
            if ((transformer = this.transformer) != null && (action = this.action) != null) {
                final int baseIndex = this.baseIndex;
                int baseLimit2;
                int baseLimit;
                while (this.batch > 0 && (baseLimit = (baseLimit2 = this.baseLimit) + baseIndex >>> 1) > baseIndex) {
                    this.addToPendingCount(1);
                    final int batch = this.batch >>> 1;
                    this.batch = batch;
                    new ForEachTransformedKeyTask((BulkTask<Object, Object, ?>)this, batch, this.baseLimit = baseLimit, baseLimit2, (Node<Object, Object>[])this.tab, (Function<? super Object, ?>)transformer, (Consumer<? super Object>)action).fork();
                }
                Node<K, V> advance;
                while ((advance = this.advance()) != null) {
                    final U apply;
                    if ((apply = (U)transformer.apply(advance.key)) != null) {
                        action.accept(apply);
                    }
                }
                this.propagateCompletion();
            }
        }
    }
    
    static final class ForEachTransformedMappingTask<K, V, U> extends BulkTask<K, V, Void>
    {
        final BiFunction<? super K, ? super V, ? extends U> transformer;
        final Consumer<? super U> action;
        
        ForEachTransformedMappingTask(final BulkTask<K, V, ?> bulkTask, final int n, final int n2, final int n3, final Node<K, V>[] array, final BiFunction<? super K, ? super V, ? extends U> transformer, final Consumer<? super U> action) {
            super(bulkTask, n, n2, n3, array);
            this.transformer = transformer;
            this.action = action;
        }
        
        @Override
        public final void compute() {
            final BiFunction<? super K, ? super V, ? extends U> transformer;
            final Consumer<? super U> action;
            if ((transformer = this.transformer) != null && (action = this.action) != null) {
                final int baseIndex = this.baseIndex;
                int baseLimit2;
                int baseLimit;
                while (this.batch > 0 && (baseLimit = (baseLimit2 = this.baseLimit) + baseIndex >>> 1) > baseIndex) {
                    this.addToPendingCount(1);
                    final int batch = this.batch >>> 1;
                    this.batch = batch;
                    new ForEachTransformedMappingTask((BulkTask<Object, Object, ?>)this, batch, this.baseLimit = baseLimit, baseLimit2, (Node<Object, Object>[])this.tab, (BiFunction<? super Object, ? super Object, ?>)transformer, (Consumer<? super Object>)action).fork();
                }
                Node<K, V> advance;
                while ((advance = this.advance()) != null) {
                    final U apply;
                    if ((apply = (U)transformer.apply(advance.key, advance.val)) != null) {
                        action.accept(apply);
                    }
                }
                this.propagateCompletion();
            }
        }
    }
    
    static final class ForEachTransformedValueTask<K, V, U> extends BulkTask<K, V, Void>
    {
        final Function<? super V, ? extends U> transformer;
        final Consumer<? super U> action;
        
        ForEachTransformedValueTask(final BulkTask<K, V, ?> bulkTask, final int n, final int n2, final int n3, final Node<K, V>[] array, final Function<? super V, ? extends U> transformer, final Consumer<? super U> action) {
            super(bulkTask, n, n2, n3, array);
            this.transformer = transformer;
            this.action = action;
        }
        
        @Override
        public final void compute() {
            final Function<? super V, ? extends U> transformer;
            final Consumer<? super U> action;
            if ((transformer = this.transformer) != null && (action = this.action) != null) {
                final int baseIndex = this.baseIndex;
                int baseLimit2;
                int baseLimit;
                while (this.batch > 0 && (baseLimit = (baseLimit2 = this.baseLimit) + baseIndex >>> 1) > baseIndex) {
                    this.addToPendingCount(1);
                    final int batch = this.batch >>> 1;
                    this.batch = batch;
                    new ForEachTransformedValueTask((BulkTask<Object, Object, ?>)this, batch, this.baseLimit = baseLimit, baseLimit2, (Node<Object, Object>[])this.tab, (Function<? super Object, ?>)transformer, (Consumer<? super Object>)action).fork();
                }
                Node<K, V> advance;
                while ((advance = this.advance()) != null) {
                    final U apply;
                    if ((apply = (U)transformer.apply(advance.val)) != null) {
                        action.accept(apply);
                    }
                }
                this.propagateCompletion();
            }
        }
    }
    
    static final class ForEachValueTask<K, V> extends BulkTask<K, V, Void>
    {
        final Consumer<? super V> action;
        
        ForEachValueTask(final BulkTask<K, V, ?> bulkTask, final int n, final int n2, final int n3, final Node<K, V>[] array, final Consumer<? super V> action) {
            super(bulkTask, n, n2, n3, array);
            this.action = action;
        }
        
        @Override
        public final void compute() {
            final Consumer<? super V> action;
            if ((action = this.action) != null) {
                final int baseIndex = this.baseIndex;
                int baseLimit2;
                int baseLimit;
                while (this.batch > 0 && (baseLimit = (baseLimit2 = this.baseLimit) + baseIndex >>> 1) > baseIndex) {
                    this.addToPendingCount(1);
                    final int batch = this.batch >>> 1;
                    this.batch = batch;
                    new ForEachValueTask((BulkTask<Object, Object, ?>)this, batch, this.baseLimit = baseLimit, baseLimit2, (Node<Object, Object>[])this.tab, (Consumer<? super Object>)action).fork();
                }
                Node<K, V> advance;
                while ((advance = this.advance()) != null) {
                    action.accept(advance.val);
                }
                this.propagateCompletion();
            }
        }
    }
    
    static final class KeyIterator<K, V> extends BaseIterator<K, V> implements Iterator<K>, Enumeration<K>
    {
        KeyIterator(final Node<K, V>[] array, final int n, final int n2, final int n3, final ConcurrentHashMap<K, V> concurrentHashMap) {
            super(array, n, n2, n3, concurrentHashMap);
        }
        
        @Override
        public final K next() {
            final Node<K, V> next;
            if ((next = this.next) == null) {
                throw new NoSuchElementException();
            }
            final K key = next.key;
            this.lastReturned = next;
            this.advance();
            return key;
        }
        
        @Override
        public final K nextElement() {
            return this.next();
        }
    }
    
    public static class KeySetView<K, V> extends CollectionView<K, V, K> implements Set<K>, Serializable
    {
        private static final long serialVersionUID = 7249069246763182397L;
        private final V value;
        
        KeySetView(final ConcurrentHashMap<K, V> concurrentHashMap, final V value) {
            super(concurrentHashMap);
            this.value = value;
        }
        
        public V getMappedValue() {
            return this.value;
        }
        
        @Override
        public boolean contains(final Object o) {
            return this.map.containsKey(o);
        }
        
        @Override
        public boolean remove(final Object o) {
            return this.map.remove(o) != null;
        }
        
        @Override
        public Iterator<K> iterator() {
            final ConcurrentHashMap<K, V> map = this.map;
            final Node<Object, Object>[] table;
            final int n = ((table = (Node<Object, Object>[])map.table) == null) ? 0 : table.length;
            return new KeyIterator<K, Object>(table, n, 0, n, (ConcurrentHashMap<Object, Object>)map);
        }
        
        @Override
        public boolean add(final K k) {
            final V value;
            if ((value = this.value) == null) {
                throw new UnsupportedOperationException();
            }
            return this.map.putVal((K)k, (V)value, true) == null;
        }
        
        @Override
        public boolean addAll(final Collection<? extends K> collection) {
            boolean b = false;
            final V value;
            if ((value = this.value) == null) {
                throw new UnsupportedOperationException();
            }
            final Iterator<? extends K> iterator = collection.iterator();
            while (iterator.hasNext()) {
                if (this.map.putVal((K)iterator.next(), (V)value, true) == null) {
                    b = true;
                }
            }
            return b;
        }
        
        @Override
        public int hashCode() {
            int n = 0;
            final Iterator<Object> iterator = this.iterator();
            while (iterator.hasNext()) {
                n += iterator.next().hashCode();
            }
            return n;
        }
        
        @Override
        public boolean equals(final Object o) {
            final Set set;
            return o instanceof Set && ((set = (Set)o) == this || (this.containsAll(set) && set.containsAll(this)));
        }
        
        @Override
        public Spliterator<K> spliterator() {
            final ConcurrentHashMap<K, V> map = this.map;
            final long sumCount = map.sumCount();
            final Node<K, V>[] table;
            final int n = ((table = map.table) == null) ? 0 : ((Node<Object, Object>[])table).length;
            return new KeySpliterator<K, Object>((Node<Object, Object>[])table, n, 0, n, (sumCount < 0L) ? 0L : sumCount);
        }
        
        @Override
        public void forEach(final Consumer<? super K> consumer) {
            if (consumer == null) {
                throw new NullPointerException();
            }
            final Node<K, V>[] table;
            if ((table = this.map.table) != null) {
                Node<Object, Object> advance;
                while ((advance = new Traverser<Object, Object>((Node<Object, Object>[])table, ((Node<Object, Object>[])table).length, 0, ((Node<Object, Object>[])table).length).advance()) != null) {
                    consumer.accept((Object)advance.key);
                }
            }
        }
    }
    
    static final class KeySpliterator<K, V> extends Traverser<K, V> implements Spliterator<K>
    {
        long est;
        
        KeySpliterator(final Node<K, V>[] array, final int n, final int n2, final int n3, final long est) {
            super(array, n, n2, n3);
            this.est = est;
        }
        
        @Override
        public Spliterator<K> trySplit() {
            final int baseIndex = this.baseIndex;
            final int baseLimit;
            final int n;
            Spliterator<K> spliterator;
            if ((n = baseIndex + (baseLimit = this.baseLimit) >>> 1) <= baseIndex) {
                spliterator = null;
            }
            else {
                final Node<K, V>[] tab;
                final int baseSize;
                final int baseLimit2;
                spliterator = new KeySpliterator<K, Object>((Node<Object, Object>[])tab, baseSize, baseLimit2, baseLimit, this.est >>>= 1);
                tab = this.tab;
                baseSize = this.baseSize;
                baseLimit2 = n;
                this.baseLimit = baseLimit2;
            }
            return spliterator;
        }
        
        @Override
        public void forEachRemaining(final Consumer<? super K> consumer) {
            if (consumer == null) {
                throw new NullPointerException();
            }
            Node<K, V> advance;
            while ((advance = this.advance()) != null) {
                consumer.accept(advance.key);
            }
        }
        
        @Override
        public boolean tryAdvance(final Consumer<? super K> consumer) {
            if (consumer == null) {
                throw new NullPointerException();
            }
            final Node<K, V> advance;
            if ((advance = this.advance()) == null) {
                return false;
            }
            consumer.accept(advance.key);
            return true;
        }
        
        @Override
        public long estimateSize() {
            return this.est;
        }
        
        @Override
        public int characteristics() {
            return 4353;
        }
    }
    
    static final class MapReduceEntriesTask<K, V, U> extends BulkTask<K, V, U>
    {
        final Function<Map.Entry<K, V>, ? extends U> transformer;
        final BiFunction<? super U, ? super U, ? extends U> reducer;
        U result;
        MapReduceEntriesTask<K, V, U> rights;
        MapReduceEntriesTask<K, V, U> nextRight;
        
        MapReduceEntriesTask(final BulkTask<K, V, ?> bulkTask, final int n, final int n2, final int n3, final Node<K, V>[] array, final MapReduceEntriesTask<K, V, U> nextRight, final Function<Map.Entry<K, V>, ? extends U> transformer, final BiFunction<? super U, ? super U, ? extends U> reducer) {
            super(bulkTask, n, n2, n3, array);
            this.nextRight = nextRight;
            this.transformer = transformer;
            this.reducer = reducer;
        }
        
        @Override
        public final U getRawResult() {
            return this.result;
        }
        
        @Override
        public final void compute() {
            final Function<Map.Entry<K, V>, ? extends U> transformer;
            final BiFunction<? super U, ? super U, ? extends U> reducer;
            if ((transformer = this.transformer) != null && (reducer = this.reducer) != null) {
                final int baseIndex = this.baseIndex;
                int baseLimit2;
                int baseLimit;
                while (this.batch > 0 && (baseLimit = (baseLimit2 = this.baseLimit) + baseIndex >>> 1) > baseIndex) {
                    this.addToPendingCount(1);
                    final int batch = this.batch >>> 1;
                    this.batch = batch;
                    (this.rights = new MapReduceEntriesTask<K, V, U>((BulkTask<Object, Object, ?>)this, batch, this.baseLimit = baseLimit, baseLimit2, (Node<Object, Object>[])this.tab, (MapReduceEntriesTask<Object, Object, Object>)this.rights, (Function<Map.Entry<Object, Object>, ?>)transformer, (BiFunction<? super Object, ? super Object, ?>)reducer)).fork();
                }
                U result = null;
                Node<K, V> advance;
                while ((advance = this.advance()) != null) {
                    final U apply;
                    if ((apply = (U)transformer.apply(advance)) != null) {
                        result = ((result == null) ? apply : reducer.apply(result, apply));
                    }
                }
                this.result = result;
                for (CountedCompleter<?> countedCompleter = this.firstComplete(); countedCompleter != null; countedCompleter = countedCompleter.nextComplete()) {
                    final MapReduceEntriesTask<?, ?, ?> mapReduceEntriesTask = (MapReduceEntriesTask<?, ?, ?>)countedCompleter;
                    MapReduceEntriesTask<?, ?, ?> nextRight;
                    for (MapReduceEntriesTask<?, ?, ?> rights = mapReduceEntriesTask.rights; rights != null; rights = nextRight) {
                        final Object result2;
                        if ((result2 = rights.result) != null) {
                            final Object result3;
                            mapReduceEntriesTask.result = (((result3 = mapReduceEntriesTask.result) == null) ? result2 : reducer.apply((U)result3, (U)result2));
                        }
                        final MapReduceEntriesTask<?, ?, ?> mapReduceEntriesTask2 = mapReduceEntriesTask;
                        nextRight = rights.nextRight;
                        mapReduceEntriesTask2.rights = nextRight;
                    }
                }
            }
        }
    }
    
    static final class MapReduceEntriesToDoubleTask<K, V> extends BulkTask<K, V, Double>
    {
        final ToDoubleFunction<Map.Entry<K, V>> transformer;
        final DoubleBinaryOperator reducer;
        final double basis;
        double result;
        MapReduceEntriesToDoubleTask<K, V> rights;
        MapReduceEntriesToDoubleTask<K, V> nextRight;
        
        MapReduceEntriesToDoubleTask(final BulkTask<K, V, ?> bulkTask, final int n, final int n2, final int n3, final Node<K, V>[] array, final MapReduceEntriesToDoubleTask<K, V> nextRight, final ToDoubleFunction<Map.Entry<K, V>> transformer, final double basis, final DoubleBinaryOperator reducer) {
            super(bulkTask, n, n2, n3, array);
            this.nextRight = nextRight;
            this.transformer = transformer;
            this.basis = basis;
            this.reducer = reducer;
        }
        
        @Override
        public final Double getRawResult() {
            return this.result;
        }
        
        @Override
        public final void compute() {
            final ToDoubleFunction<Map.Entry<K, V>> transformer;
            final DoubleBinaryOperator reducer;
            if ((transformer = this.transformer) != null && (reducer = this.reducer) != null) {
                double result = this.basis;
                final int baseIndex = this.baseIndex;
                int baseLimit2;
                int baseLimit;
                while (this.batch > 0 && (baseLimit = (baseLimit2 = this.baseLimit) + baseIndex >>> 1) > baseIndex) {
                    this.addToPendingCount(1);
                    final int batch = this.batch >>> 1;
                    this.batch = batch;
                    (this.rights = new MapReduceEntriesToDoubleTask<K, V>((BulkTask<Object, Object, ?>)this, batch, this.baseLimit = baseLimit, baseLimit2, (Node<Object, Object>[])this.tab, (MapReduceEntriesToDoubleTask<Object, Object>)this.rights, (ToDoubleFunction<Map.Entry<Object, Object>>)transformer, result, reducer)).fork();
                }
                Node<K, V> advance;
                while ((advance = this.advance()) != null) {
                    result = reducer.applyAsDouble(result, transformer.applyAsDouble(advance));
                }
                this.result = result;
                for (CountedCompleter<?> countedCompleter = this.firstComplete(); countedCompleter != null; countedCompleter = countedCompleter.nextComplete()) {
                    final MapReduceEntriesToDoubleTask<?, ?> mapReduceEntriesToDoubleTask = (MapReduceEntriesToDoubleTask<?, ?>)countedCompleter;
                    MapReduceEntriesToDoubleTask<?, ?> nextRight;
                    for (MapReduceEntriesToDoubleTask<?, ?> rights = mapReduceEntriesToDoubleTask.rights; rights != null; rights = nextRight) {
                        mapReduceEntriesToDoubleTask.result = reducer.applyAsDouble(mapReduceEntriesToDoubleTask.result, rights.result);
                        final MapReduceEntriesToDoubleTask<?, ?> mapReduceEntriesToDoubleTask2 = mapReduceEntriesToDoubleTask;
                        nextRight = rights.nextRight;
                        mapReduceEntriesToDoubleTask2.rights = nextRight;
                    }
                }
            }
        }
    }
    
    static final class MapReduceEntriesToIntTask<K, V> extends BulkTask<K, V, Integer>
    {
        final ToIntFunction<Map.Entry<K, V>> transformer;
        final IntBinaryOperator reducer;
        final int basis;
        int result;
        MapReduceEntriesToIntTask<K, V> rights;
        MapReduceEntriesToIntTask<K, V> nextRight;
        
        MapReduceEntriesToIntTask(final BulkTask<K, V, ?> bulkTask, final int n, final int n2, final int n3, final Node<K, V>[] array, final MapReduceEntriesToIntTask<K, V> nextRight, final ToIntFunction<Map.Entry<K, V>> transformer, final int basis, final IntBinaryOperator reducer) {
            super(bulkTask, n, n2, n3, array);
            this.nextRight = nextRight;
            this.transformer = transformer;
            this.basis = basis;
            this.reducer = reducer;
        }
        
        @Override
        public final Integer getRawResult() {
            return this.result;
        }
        
        @Override
        public final void compute() {
            final ToIntFunction<Map.Entry<K, V>> transformer;
            final IntBinaryOperator reducer;
            if ((transformer = this.transformer) != null && (reducer = this.reducer) != null) {
                int result = this.basis;
                final int baseIndex = this.baseIndex;
                int baseLimit2;
                int baseLimit;
                while (this.batch > 0 && (baseLimit = (baseLimit2 = this.baseLimit) + baseIndex >>> 1) > baseIndex) {
                    this.addToPendingCount(1);
                    final int batch = this.batch >>> 1;
                    this.batch = batch;
                    (this.rights = new MapReduceEntriesToIntTask<K, V>((BulkTask<Object, Object, ?>)this, batch, this.baseLimit = baseLimit, baseLimit2, (Node<Object, Object>[])this.tab, (MapReduceEntriesToIntTask<Object, Object>)this.rights, (ToIntFunction<Map.Entry<Object, Object>>)transformer, result, reducer)).fork();
                }
                Node<K, V> advance;
                while ((advance = this.advance()) != null) {
                    result = reducer.applyAsInt(result, transformer.applyAsInt(advance));
                }
                this.result = result;
                for (CountedCompleter<?> countedCompleter = this.firstComplete(); countedCompleter != null; countedCompleter = countedCompleter.nextComplete()) {
                    final MapReduceEntriesToIntTask<?, ?> mapReduceEntriesToIntTask = (MapReduceEntriesToIntTask<?, ?>)countedCompleter;
                    MapReduceEntriesToIntTask<?, ?> nextRight;
                    for (MapReduceEntriesToIntTask<?, ?> rights = mapReduceEntriesToIntTask.rights; rights != null; rights = nextRight) {
                        mapReduceEntriesToIntTask.result = reducer.applyAsInt(mapReduceEntriesToIntTask.result, rights.result);
                        final MapReduceEntriesToIntTask<?, ?> mapReduceEntriesToIntTask2 = mapReduceEntriesToIntTask;
                        nextRight = rights.nextRight;
                        mapReduceEntriesToIntTask2.rights = nextRight;
                    }
                }
            }
        }
    }
    
    static final class MapReduceEntriesToLongTask<K, V> extends BulkTask<K, V, Long>
    {
        final ToLongFunction<Map.Entry<K, V>> transformer;
        final LongBinaryOperator reducer;
        final long basis;
        long result;
        MapReduceEntriesToLongTask<K, V> rights;
        MapReduceEntriesToLongTask<K, V> nextRight;
        
        MapReduceEntriesToLongTask(final BulkTask<K, V, ?> bulkTask, final int n, final int n2, final int n3, final Node<K, V>[] array, final MapReduceEntriesToLongTask<K, V> nextRight, final ToLongFunction<Map.Entry<K, V>> transformer, final long basis, final LongBinaryOperator reducer) {
            super(bulkTask, n, n2, n3, array);
            this.nextRight = nextRight;
            this.transformer = transformer;
            this.basis = basis;
            this.reducer = reducer;
        }
        
        @Override
        public final Long getRawResult() {
            return this.result;
        }
        
        @Override
        public final void compute() {
            final ToLongFunction<Map.Entry<K, V>> transformer;
            final LongBinaryOperator reducer;
            if ((transformer = this.transformer) != null && (reducer = this.reducer) != null) {
                long result = this.basis;
                final int baseIndex = this.baseIndex;
                int baseLimit2;
                int baseLimit;
                while (this.batch > 0 && (baseLimit = (baseLimit2 = this.baseLimit) + baseIndex >>> 1) > baseIndex) {
                    this.addToPendingCount(1);
                    final int batch = this.batch >>> 1;
                    this.batch = batch;
                    (this.rights = new MapReduceEntriesToLongTask<K, V>((BulkTask<Object, Object, ?>)this, batch, this.baseLimit = baseLimit, baseLimit2, (Node<Object, Object>[])this.tab, (MapReduceEntriesToLongTask<Object, Object>)this.rights, (ToLongFunction<Map.Entry<Object, Object>>)transformer, result, reducer)).fork();
                }
                Node<K, V> advance;
                while ((advance = this.advance()) != null) {
                    result = reducer.applyAsLong(result, transformer.applyAsLong(advance));
                }
                this.result = result;
                for (CountedCompleter<?> countedCompleter = this.firstComplete(); countedCompleter != null; countedCompleter = countedCompleter.nextComplete()) {
                    final MapReduceEntriesToLongTask<?, ?> mapReduceEntriesToLongTask = (MapReduceEntriesToLongTask<?, ?>)countedCompleter;
                    MapReduceEntriesToLongTask<?, ?> nextRight;
                    for (MapReduceEntriesToLongTask<?, ?> rights = mapReduceEntriesToLongTask.rights; rights != null; rights = nextRight) {
                        mapReduceEntriesToLongTask.result = reducer.applyAsLong(mapReduceEntriesToLongTask.result, rights.result);
                        final MapReduceEntriesToLongTask<?, ?> mapReduceEntriesToLongTask2 = mapReduceEntriesToLongTask;
                        nextRight = rights.nextRight;
                        mapReduceEntriesToLongTask2.rights = nextRight;
                    }
                }
            }
        }
    }
    
    static final class MapReduceKeysTask<K, V, U> extends BulkTask<K, V, U>
    {
        final Function<? super K, ? extends U> transformer;
        final BiFunction<? super U, ? super U, ? extends U> reducer;
        U result;
        MapReduceKeysTask<K, V, U> rights;
        MapReduceKeysTask<K, V, U> nextRight;
        
        MapReduceKeysTask(final BulkTask<K, V, ?> bulkTask, final int n, final int n2, final int n3, final Node<K, V>[] array, final MapReduceKeysTask<K, V, U> nextRight, final Function<? super K, ? extends U> transformer, final BiFunction<? super U, ? super U, ? extends U> reducer) {
            super(bulkTask, n, n2, n3, array);
            this.nextRight = nextRight;
            this.transformer = transformer;
            this.reducer = reducer;
        }
        
        @Override
        public final U getRawResult() {
            return this.result;
        }
        
        @Override
        public final void compute() {
            final Function<? super K, ? extends U> transformer;
            final BiFunction<? super U, ? super U, ? extends U> reducer;
            if ((transformer = this.transformer) != null && (reducer = this.reducer) != null) {
                final int baseIndex = this.baseIndex;
                int baseLimit2;
                int baseLimit;
                while (this.batch > 0 && (baseLimit = (baseLimit2 = this.baseLimit) + baseIndex >>> 1) > baseIndex) {
                    this.addToPendingCount(1);
                    final int batch = this.batch >>> 1;
                    this.batch = batch;
                    (this.rights = new MapReduceKeysTask<K, V, U>((BulkTask<Object, Object, ?>)this, batch, this.baseLimit = baseLimit, baseLimit2, (Node<Object, Object>[])this.tab, (MapReduceKeysTask<Object, Object, Object>)this.rights, (Function<? super Object, ?>)transformer, (BiFunction<? super Object, ? super Object, ?>)reducer)).fork();
                }
                U result = null;
                Node<K, V> advance;
                while ((advance = this.advance()) != null) {
                    final U apply;
                    if ((apply = (U)transformer.apply(advance.key)) != null) {
                        result = ((result == null) ? apply : reducer.apply(result, apply));
                    }
                }
                this.result = result;
                for (CountedCompleter<?> countedCompleter = this.firstComplete(); countedCompleter != null; countedCompleter = countedCompleter.nextComplete()) {
                    final MapReduceKeysTask<?, ?, ?> mapReduceKeysTask = (MapReduceKeysTask<?, ?, ?>)countedCompleter;
                    MapReduceKeysTask<?, ?, ?> nextRight;
                    for (MapReduceKeysTask<?, ?, ?> rights = mapReduceKeysTask.rights; rights != null; rights = nextRight) {
                        final Object result2;
                        if ((result2 = rights.result) != null) {
                            final Object result3;
                            mapReduceKeysTask.result = (((result3 = mapReduceKeysTask.result) == null) ? result2 : reducer.apply((U)result3, (U)result2));
                        }
                        final MapReduceKeysTask<?, ?, ?> mapReduceKeysTask2 = mapReduceKeysTask;
                        nextRight = rights.nextRight;
                        mapReduceKeysTask2.rights = nextRight;
                    }
                }
            }
        }
    }
    
    static final class MapReduceKeysToDoubleTask<K, V> extends BulkTask<K, V, Double>
    {
        final ToDoubleFunction<? super K> transformer;
        final DoubleBinaryOperator reducer;
        final double basis;
        double result;
        MapReduceKeysToDoubleTask<K, V> rights;
        MapReduceKeysToDoubleTask<K, V> nextRight;
        
        MapReduceKeysToDoubleTask(final BulkTask<K, V, ?> bulkTask, final int n, final int n2, final int n3, final Node<K, V>[] array, final MapReduceKeysToDoubleTask<K, V> nextRight, final ToDoubleFunction<? super K> transformer, final double basis, final DoubleBinaryOperator reducer) {
            super(bulkTask, n, n2, n3, array);
            this.nextRight = nextRight;
            this.transformer = transformer;
            this.basis = basis;
            this.reducer = reducer;
        }
        
        @Override
        public final Double getRawResult() {
            return this.result;
        }
        
        @Override
        public final void compute() {
            final ToDoubleFunction<? super K> transformer;
            final DoubleBinaryOperator reducer;
            if ((transformer = this.transformer) != null && (reducer = this.reducer) != null) {
                double result = this.basis;
                final int baseIndex = this.baseIndex;
                int baseLimit2;
                int baseLimit;
                while (this.batch > 0 && (baseLimit = (baseLimit2 = this.baseLimit) + baseIndex >>> 1) > baseIndex) {
                    this.addToPendingCount(1);
                    final int batch = this.batch >>> 1;
                    this.batch = batch;
                    (this.rights = new MapReduceKeysToDoubleTask<K, V>((BulkTask<Object, Object, ?>)this, batch, this.baseLimit = baseLimit, baseLimit2, (Node<Object, Object>[])this.tab, (MapReduceKeysToDoubleTask<Object, Object>)this.rights, (ToDoubleFunction<? super Object>)transformer, result, reducer)).fork();
                }
                Node<K, V> advance;
                while ((advance = this.advance()) != null) {
                    result = reducer.applyAsDouble(result, transformer.applyAsDouble(advance.key));
                }
                this.result = result;
                for (CountedCompleter<?> countedCompleter = this.firstComplete(); countedCompleter != null; countedCompleter = countedCompleter.nextComplete()) {
                    final MapReduceKeysToDoubleTask<?, ?> mapReduceKeysToDoubleTask = (MapReduceKeysToDoubleTask<?, ?>)countedCompleter;
                    MapReduceKeysToDoubleTask<?, ?> nextRight;
                    for (MapReduceKeysToDoubleTask<?, ?> rights = mapReduceKeysToDoubleTask.rights; rights != null; rights = nextRight) {
                        mapReduceKeysToDoubleTask.result = reducer.applyAsDouble(mapReduceKeysToDoubleTask.result, rights.result);
                        final MapReduceKeysToDoubleTask<?, ?> mapReduceKeysToDoubleTask2 = mapReduceKeysToDoubleTask;
                        nextRight = rights.nextRight;
                        mapReduceKeysToDoubleTask2.rights = nextRight;
                    }
                }
            }
        }
    }
    
    static final class MapReduceKeysToIntTask<K, V> extends BulkTask<K, V, Integer>
    {
        final ToIntFunction<? super K> transformer;
        final IntBinaryOperator reducer;
        final int basis;
        int result;
        MapReduceKeysToIntTask<K, V> rights;
        MapReduceKeysToIntTask<K, V> nextRight;
        
        MapReduceKeysToIntTask(final BulkTask<K, V, ?> bulkTask, final int n, final int n2, final int n3, final Node<K, V>[] array, final MapReduceKeysToIntTask<K, V> nextRight, final ToIntFunction<? super K> transformer, final int basis, final IntBinaryOperator reducer) {
            super(bulkTask, n, n2, n3, array);
            this.nextRight = nextRight;
            this.transformer = transformer;
            this.basis = basis;
            this.reducer = reducer;
        }
        
        @Override
        public final Integer getRawResult() {
            return this.result;
        }
        
        @Override
        public final void compute() {
            final ToIntFunction<? super K> transformer;
            final IntBinaryOperator reducer;
            if ((transformer = this.transformer) != null && (reducer = this.reducer) != null) {
                int result = this.basis;
                final int baseIndex = this.baseIndex;
                int baseLimit2;
                int baseLimit;
                while (this.batch > 0 && (baseLimit = (baseLimit2 = this.baseLimit) + baseIndex >>> 1) > baseIndex) {
                    this.addToPendingCount(1);
                    final int batch = this.batch >>> 1;
                    this.batch = batch;
                    (this.rights = new MapReduceKeysToIntTask<K, V>((BulkTask<Object, Object, ?>)this, batch, this.baseLimit = baseLimit, baseLimit2, (Node<Object, Object>[])this.tab, (MapReduceKeysToIntTask<Object, Object>)this.rights, (ToIntFunction<? super Object>)transformer, result, reducer)).fork();
                }
                Node<K, V> advance;
                while ((advance = this.advance()) != null) {
                    result = reducer.applyAsInt(result, transformer.applyAsInt(advance.key));
                }
                this.result = result;
                for (CountedCompleter<?> countedCompleter = this.firstComplete(); countedCompleter != null; countedCompleter = countedCompleter.nextComplete()) {
                    final MapReduceKeysToIntTask<?, ?> mapReduceKeysToIntTask = (MapReduceKeysToIntTask<?, ?>)countedCompleter;
                    MapReduceKeysToIntTask<?, ?> nextRight;
                    for (MapReduceKeysToIntTask<?, ?> rights = mapReduceKeysToIntTask.rights; rights != null; rights = nextRight) {
                        mapReduceKeysToIntTask.result = reducer.applyAsInt(mapReduceKeysToIntTask.result, rights.result);
                        final MapReduceKeysToIntTask<?, ?> mapReduceKeysToIntTask2 = mapReduceKeysToIntTask;
                        nextRight = rights.nextRight;
                        mapReduceKeysToIntTask2.rights = nextRight;
                    }
                }
            }
        }
    }
    
    static final class MapReduceKeysToLongTask<K, V> extends BulkTask<K, V, Long>
    {
        final ToLongFunction<? super K> transformer;
        final LongBinaryOperator reducer;
        final long basis;
        long result;
        MapReduceKeysToLongTask<K, V> rights;
        MapReduceKeysToLongTask<K, V> nextRight;
        
        MapReduceKeysToLongTask(final BulkTask<K, V, ?> bulkTask, final int n, final int n2, final int n3, final Node<K, V>[] array, final MapReduceKeysToLongTask<K, V> nextRight, final ToLongFunction<? super K> transformer, final long basis, final LongBinaryOperator reducer) {
            super(bulkTask, n, n2, n3, array);
            this.nextRight = nextRight;
            this.transformer = transformer;
            this.basis = basis;
            this.reducer = reducer;
        }
        
        @Override
        public final Long getRawResult() {
            return this.result;
        }
        
        @Override
        public final void compute() {
            final ToLongFunction<? super K> transformer;
            final LongBinaryOperator reducer;
            if ((transformer = this.transformer) != null && (reducer = this.reducer) != null) {
                long result = this.basis;
                final int baseIndex = this.baseIndex;
                int baseLimit2;
                int baseLimit;
                while (this.batch > 0 && (baseLimit = (baseLimit2 = this.baseLimit) + baseIndex >>> 1) > baseIndex) {
                    this.addToPendingCount(1);
                    final int batch = this.batch >>> 1;
                    this.batch = batch;
                    (this.rights = new MapReduceKeysToLongTask<K, V>((BulkTask<Object, Object, ?>)this, batch, this.baseLimit = baseLimit, baseLimit2, (Node<Object, Object>[])this.tab, (MapReduceKeysToLongTask<Object, Object>)this.rights, (ToLongFunction<? super Object>)transformer, result, reducer)).fork();
                }
                Node<K, V> advance;
                while ((advance = this.advance()) != null) {
                    result = reducer.applyAsLong(result, transformer.applyAsLong(advance.key));
                }
                this.result = result;
                for (CountedCompleter<?> countedCompleter = this.firstComplete(); countedCompleter != null; countedCompleter = countedCompleter.nextComplete()) {
                    final MapReduceKeysToLongTask<?, ?> mapReduceKeysToLongTask = (MapReduceKeysToLongTask<?, ?>)countedCompleter;
                    MapReduceKeysToLongTask<?, ?> nextRight;
                    for (MapReduceKeysToLongTask<?, ?> rights = mapReduceKeysToLongTask.rights; rights != null; rights = nextRight) {
                        mapReduceKeysToLongTask.result = reducer.applyAsLong(mapReduceKeysToLongTask.result, rights.result);
                        final MapReduceKeysToLongTask<?, ?> mapReduceKeysToLongTask2 = mapReduceKeysToLongTask;
                        nextRight = rights.nextRight;
                        mapReduceKeysToLongTask2.rights = nextRight;
                    }
                }
            }
        }
    }
    
    static final class MapReduceMappingsTask<K, V, U> extends BulkTask<K, V, U>
    {
        final BiFunction<? super K, ? super V, ? extends U> transformer;
        final BiFunction<? super U, ? super U, ? extends U> reducer;
        U result;
        MapReduceMappingsTask<K, V, U> rights;
        MapReduceMappingsTask<K, V, U> nextRight;
        
        MapReduceMappingsTask(final BulkTask<K, V, ?> bulkTask, final int n, final int n2, final int n3, final Node<K, V>[] array, final MapReduceMappingsTask<K, V, U> nextRight, final BiFunction<? super K, ? super V, ? extends U> transformer, final BiFunction<? super U, ? super U, ? extends U> reducer) {
            super(bulkTask, n, n2, n3, array);
            this.nextRight = nextRight;
            this.transformer = transformer;
            this.reducer = reducer;
        }
        
        @Override
        public final U getRawResult() {
            return this.result;
        }
        
        @Override
        public final void compute() {
            final BiFunction<? super K, ? super V, ? extends U> transformer;
            final BiFunction<? super U, ? super U, ? extends U> reducer;
            if ((transformer = this.transformer) != null && (reducer = this.reducer) != null) {
                final int baseIndex = this.baseIndex;
                int baseLimit2;
                int baseLimit;
                while (this.batch > 0 && (baseLimit = (baseLimit2 = this.baseLimit) + baseIndex >>> 1) > baseIndex) {
                    this.addToPendingCount(1);
                    final int batch = this.batch >>> 1;
                    this.batch = batch;
                    (this.rights = new MapReduceMappingsTask<K, V, U>((BulkTask<Object, Object, ?>)this, batch, this.baseLimit = baseLimit, baseLimit2, (Node<Object, Object>[])this.tab, (MapReduceMappingsTask<Object, Object, Object>)this.rights, (BiFunction<? super Object, ? super Object, ?>)transformer, (BiFunction<? super Object, ? super Object, ?>)reducer)).fork();
                }
                U result = null;
                Node<K, V> advance;
                while ((advance = this.advance()) != null) {
                    final U apply;
                    if ((apply = (U)transformer.apply(advance.key, advance.val)) != null) {
                        result = ((result == null) ? apply : reducer.apply(result, apply));
                    }
                }
                this.result = result;
                for (CountedCompleter<?> countedCompleter = this.firstComplete(); countedCompleter != null; countedCompleter = countedCompleter.nextComplete()) {
                    final MapReduceMappingsTask<?, ?, ?> mapReduceMappingsTask = (MapReduceMappingsTask<?, ?, ?>)countedCompleter;
                    MapReduceMappingsTask<?, ?, ?> nextRight;
                    for (MapReduceMappingsTask<?, ?, ?> rights = mapReduceMappingsTask.rights; rights != null; rights = nextRight) {
                        final Object result2;
                        if ((result2 = rights.result) != null) {
                            final Object result3;
                            mapReduceMappingsTask.result = (((result3 = mapReduceMappingsTask.result) == null) ? result2 : reducer.apply((U)result3, (U)result2));
                        }
                        final MapReduceMappingsTask<?, ?, ?> mapReduceMappingsTask2 = mapReduceMappingsTask;
                        nextRight = rights.nextRight;
                        mapReduceMappingsTask2.rights = nextRight;
                    }
                }
            }
        }
    }
    
    static final class MapReduceMappingsToDoubleTask<K, V> extends BulkTask<K, V, Double>
    {
        final ToDoubleBiFunction<? super K, ? super V> transformer;
        final DoubleBinaryOperator reducer;
        final double basis;
        double result;
        MapReduceMappingsToDoubleTask<K, V> rights;
        MapReduceMappingsToDoubleTask<K, V> nextRight;
        
        MapReduceMappingsToDoubleTask(final BulkTask<K, V, ?> bulkTask, final int n, final int n2, final int n3, final Node<K, V>[] array, final MapReduceMappingsToDoubleTask<K, V> nextRight, final ToDoubleBiFunction<? super K, ? super V> transformer, final double basis, final DoubleBinaryOperator reducer) {
            super(bulkTask, n, n2, n3, array);
            this.nextRight = nextRight;
            this.transformer = transformer;
            this.basis = basis;
            this.reducer = reducer;
        }
        
        @Override
        public final Double getRawResult() {
            return this.result;
        }
        
        @Override
        public final void compute() {
            final ToDoubleBiFunction<? super K, ? super V> transformer;
            final DoubleBinaryOperator reducer;
            if ((transformer = this.transformer) != null && (reducer = this.reducer) != null) {
                double result = this.basis;
                final int baseIndex = this.baseIndex;
                int baseLimit2;
                int baseLimit;
                while (this.batch > 0 && (baseLimit = (baseLimit2 = this.baseLimit) + baseIndex >>> 1) > baseIndex) {
                    this.addToPendingCount(1);
                    final int batch = this.batch >>> 1;
                    this.batch = batch;
                    (this.rights = new MapReduceMappingsToDoubleTask<K, V>((BulkTask<Object, Object, ?>)this, batch, this.baseLimit = baseLimit, baseLimit2, (Node<Object, Object>[])this.tab, (MapReduceMappingsToDoubleTask<Object, Object>)this.rights, (ToDoubleBiFunction<? super Object, ? super Object>)transformer, result, reducer)).fork();
                }
                Node<K, V> advance;
                while ((advance = this.advance()) != null) {
                    result = reducer.applyAsDouble(result, transformer.applyAsDouble(advance.key, advance.val));
                }
                this.result = result;
                for (CountedCompleter<?> countedCompleter = this.firstComplete(); countedCompleter != null; countedCompleter = countedCompleter.nextComplete()) {
                    final MapReduceMappingsToDoubleTask<?, ?> mapReduceMappingsToDoubleTask = (MapReduceMappingsToDoubleTask<?, ?>)countedCompleter;
                    MapReduceMappingsToDoubleTask<?, ?> nextRight;
                    for (MapReduceMappingsToDoubleTask<?, ?> rights = mapReduceMappingsToDoubleTask.rights; rights != null; rights = nextRight) {
                        mapReduceMappingsToDoubleTask.result = reducer.applyAsDouble(mapReduceMappingsToDoubleTask.result, rights.result);
                        final MapReduceMappingsToDoubleTask<?, ?> mapReduceMappingsToDoubleTask2 = mapReduceMappingsToDoubleTask;
                        nextRight = rights.nextRight;
                        mapReduceMappingsToDoubleTask2.rights = nextRight;
                    }
                }
            }
        }
    }
    
    static final class MapReduceMappingsToIntTask<K, V> extends BulkTask<K, V, Integer>
    {
        final ToIntBiFunction<? super K, ? super V> transformer;
        final IntBinaryOperator reducer;
        final int basis;
        int result;
        MapReduceMappingsToIntTask<K, V> rights;
        MapReduceMappingsToIntTask<K, V> nextRight;
        
        MapReduceMappingsToIntTask(final BulkTask<K, V, ?> bulkTask, final int n, final int n2, final int n3, final Node<K, V>[] array, final MapReduceMappingsToIntTask<K, V> nextRight, final ToIntBiFunction<? super K, ? super V> transformer, final int basis, final IntBinaryOperator reducer) {
            super(bulkTask, n, n2, n3, array);
            this.nextRight = nextRight;
            this.transformer = transformer;
            this.basis = basis;
            this.reducer = reducer;
        }
        
        @Override
        public final Integer getRawResult() {
            return this.result;
        }
        
        @Override
        public final void compute() {
            final ToIntBiFunction<? super K, ? super V> transformer;
            final IntBinaryOperator reducer;
            if ((transformer = this.transformer) != null && (reducer = this.reducer) != null) {
                int result = this.basis;
                final int baseIndex = this.baseIndex;
                int baseLimit2;
                int baseLimit;
                while (this.batch > 0 && (baseLimit = (baseLimit2 = this.baseLimit) + baseIndex >>> 1) > baseIndex) {
                    this.addToPendingCount(1);
                    final int batch = this.batch >>> 1;
                    this.batch = batch;
                    (this.rights = new MapReduceMappingsToIntTask<K, V>((BulkTask<Object, Object, ?>)this, batch, this.baseLimit = baseLimit, baseLimit2, (Node<Object, Object>[])this.tab, (MapReduceMappingsToIntTask<Object, Object>)this.rights, (ToIntBiFunction<? super Object, ? super Object>)transformer, result, reducer)).fork();
                }
                Node<K, V> advance;
                while ((advance = this.advance()) != null) {
                    result = reducer.applyAsInt(result, transformer.applyAsInt(advance.key, advance.val));
                }
                this.result = result;
                for (CountedCompleter<?> countedCompleter = this.firstComplete(); countedCompleter != null; countedCompleter = countedCompleter.nextComplete()) {
                    final MapReduceMappingsToIntTask<?, ?> mapReduceMappingsToIntTask = (MapReduceMappingsToIntTask<?, ?>)countedCompleter;
                    MapReduceMappingsToIntTask<?, ?> nextRight;
                    for (MapReduceMappingsToIntTask<?, ?> rights = mapReduceMappingsToIntTask.rights; rights != null; rights = nextRight) {
                        mapReduceMappingsToIntTask.result = reducer.applyAsInt(mapReduceMappingsToIntTask.result, rights.result);
                        final MapReduceMappingsToIntTask<?, ?> mapReduceMappingsToIntTask2 = mapReduceMappingsToIntTask;
                        nextRight = rights.nextRight;
                        mapReduceMappingsToIntTask2.rights = nextRight;
                    }
                }
            }
        }
    }
    
    static final class MapReduceMappingsToLongTask<K, V> extends BulkTask<K, V, Long>
    {
        final ToLongBiFunction<? super K, ? super V> transformer;
        final LongBinaryOperator reducer;
        final long basis;
        long result;
        MapReduceMappingsToLongTask<K, V> rights;
        MapReduceMappingsToLongTask<K, V> nextRight;
        
        MapReduceMappingsToLongTask(final BulkTask<K, V, ?> bulkTask, final int n, final int n2, final int n3, final Node<K, V>[] array, final MapReduceMappingsToLongTask<K, V> nextRight, final ToLongBiFunction<? super K, ? super V> transformer, final long basis, final LongBinaryOperator reducer) {
            super(bulkTask, n, n2, n3, array);
            this.nextRight = nextRight;
            this.transformer = transformer;
            this.basis = basis;
            this.reducer = reducer;
        }
        
        @Override
        public final Long getRawResult() {
            return this.result;
        }
        
        @Override
        public final void compute() {
            final ToLongBiFunction<? super K, ? super V> transformer;
            final LongBinaryOperator reducer;
            if ((transformer = this.transformer) != null && (reducer = this.reducer) != null) {
                long result = this.basis;
                final int baseIndex = this.baseIndex;
                int baseLimit2;
                int baseLimit;
                while (this.batch > 0 && (baseLimit = (baseLimit2 = this.baseLimit) + baseIndex >>> 1) > baseIndex) {
                    this.addToPendingCount(1);
                    final int batch = this.batch >>> 1;
                    this.batch = batch;
                    (this.rights = new MapReduceMappingsToLongTask<K, V>((BulkTask<Object, Object, ?>)this, batch, this.baseLimit = baseLimit, baseLimit2, (Node<Object, Object>[])this.tab, (MapReduceMappingsToLongTask<Object, Object>)this.rights, (ToLongBiFunction<? super Object, ? super Object>)transformer, result, reducer)).fork();
                }
                Node<K, V> advance;
                while ((advance = this.advance()) != null) {
                    result = reducer.applyAsLong(result, transformer.applyAsLong(advance.key, advance.val));
                }
                this.result = result;
                for (CountedCompleter<?> countedCompleter = this.firstComplete(); countedCompleter != null; countedCompleter = countedCompleter.nextComplete()) {
                    final MapReduceMappingsToLongTask<?, ?> mapReduceMappingsToLongTask = (MapReduceMappingsToLongTask<?, ?>)countedCompleter;
                    MapReduceMappingsToLongTask<?, ?> nextRight;
                    for (MapReduceMappingsToLongTask<?, ?> rights = mapReduceMappingsToLongTask.rights; rights != null; rights = nextRight) {
                        mapReduceMappingsToLongTask.result = reducer.applyAsLong(mapReduceMappingsToLongTask.result, rights.result);
                        final MapReduceMappingsToLongTask<?, ?> mapReduceMappingsToLongTask2 = mapReduceMappingsToLongTask;
                        nextRight = rights.nextRight;
                        mapReduceMappingsToLongTask2.rights = nextRight;
                    }
                }
            }
        }
    }
    
    static final class MapReduceValuesTask<K, V, U> extends BulkTask<K, V, U>
    {
        final Function<? super V, ? extends U> transformer;
        final BiFunction<? super U, ? super U, ? extends U> reducer;
        U result;
        MapReduceValuesTask<K, V, U> rights;
        MapReduceValuesTask<K, V, U> nextRight;
        
        MapReduceValuesTask(final BulkTask<K, V, ?> bulkTask, final int n, final int n2, final int n3, final Node<K, V>[] array, final MapReduceValuesTask<K, V, U> nextRight, final Function<? super V, ? extends U> transformer, final BiFunction<? super U, ? super U, ? extends U> reducer) {
            super(bulkTask, n, n2, n3, array);
            this.nextRight = nextRight;
            this.transformer = transformer;
            this.reducer = reducer;
        }
        
        @Override
        public final U getRawResult() {
            return this.result;
        }
        
        @Override
        public final void compute() {
            final Function<? super V, ? extends U> transformer;
            final BiFunction<? super U, ? super U, ? extends U> reducer;
            if ((transformer = this.transformer) != null && (reducer = this.reducer) != null) {
                final int baseIndex = this.baseIndex;
                int baseLimit2;
                int baseLimit;
                while (this.batch > 0 && (baseLimit = (baseLimit2 = this.baseLimit) + baseIndex >>> 1) > baseIndex) {
                    this.addToPendingCount(1);
                    final int batch = this.batch >>> 1;
                    this.batch = batch;
                    (this.rights = new MapReduceValuesTask<K, V, U>((BulkTask<Object, Object, ?>)this, batch, this.baseLimit = baseLimit, baseLimit2, (Node<Object, Object>[])this.tab, (MapReduceValuesTask<Object, Object, Object>)this.rights, (Function<? super Object, ?>)transformer, (BiFunction<? super Object, ? super Object, ?>)reducer)).fork();
                }
                U result = null;
                Node<K, V> advance;
                while ((advance = this.advance()) != null) {
                    final U apply;
                    if ((apply = (U)transformer.apply(advance.val)) != null) {
                        result = ((result == null) ? apply : reducer.apply(result, apply));
                    }
                }
                this.result = result;
                for (CountedCompleter<?> countedCompleter = this.firstComplete(); countedCompleter != null; countedCompleter = countedCompleter.nextComplete()) {
                    final MapReduceValuesTask<?, ?, ?> mapReduceValuesTask = (MapReduceValuesTask<?, ?, ?>)countedCompleter;
                    MapReduceValuesTask<?, ?, ?> nextRight;
                    for (MapReduceValuesTask<?, ?, ?> rights = mapReduceValuesTask.rights; rights != null; rights = nextRight) {
                        final Object result2;
                        if ((result2 = rights.result) != null) {
                            final Object result3;
                            mapReduceValuesTask.result = (((result3 = mapReduceValuesTask.result) == null) ? result2 : reducer.apply((U)result3, (U)result2));
                        }
                        final MapReduceValuesTask<?, ?, ?> mapReduceValuesTask2 = mapReduceValuesTask;
                        nextRight = rights.nextRight;
                        mapReduceValuesTask2.rights = nextRight;
                    }
                }
            }
        }
    }
    
    static final class MapReduceValuesToDoubleTask<K, V> extends BulkTask<K, V, Double>
    {
        final ToDoubleFunction<? super V> transformer;
        final DoubleBinaryOperator reducer;
        final double basis;
        double result;
        MapReduceValuesToDoubleTask<K, V> rights;
        MapReduceValuesToDoubleTask<K, V> nextRight;
        
        MapReduceValuesToDoubleTask(final BulkTask<K, V, ?> bulkTask, final int n, final int n2, final int n3, final Node<K, V>[] array, final MapReduceValuesToDoubleTask<K, V> nextRight, final ToDoubleFunction<? super V> transformer, final double basis, final DoubleBinaryOperator reducer) {
            super(bulkTask, n, n2, n3, array);
            this.nextRight = nextRight;
            this.transformer = transformer;
            this.basis = basis;
            this.reducer = reducer;
        }
        
        @Override
        public final Double getRawResult() {
            return this.result;
        }
        
        @Override
        public final void compute() {
            final ToDoubleFunction<? super V> transformer;
            final DoubleBinaryOperator reducer;
            if ((transformer = this.transformer) != null && (reducer = this.reducer) != null) {
                double result = this.basis;
                final int baseIndex = this.baseIndex;
                int baseLimit2;
                int baseLimit;
                while (this.batch > 0 && (baseLimit = (baseLimit2 = this.baseLimit) + baseIndex >>> 1) > baseIndex) {
                    this.addToPendingCount(1);
                    final int batch = this.batch >>> 1;
                    this.batch = batch;
                    (this.rights = new MapReduceValuesToDoubleTask<K, V>((BulkTask<Object, Object, ?>)this, batch, this.baseLimit = baseLimit, baseLimit2, (Node<Object, Object>[])this.tab, (MapReduceValuesToDoubleTask<Object, Object>)this.rights, (ToDoubleFunction<? super Object>)transformer, result, reducer)).fork();
                }
                Node<K, V> advance;
                while ((advance = this.advance()) != null) {
                    result = reducer.applyAsDouble(result, transformer.applyAsDouble(advance.val));
                }
                this.result = result;
                for (CountedCompleter<?> countedCompleter = this.firstComplete(); countedCompleter != null; countedCompleter = countedCompleter.nextComplete()) {
                    final MapReduceValuesToDoubleTask<?, ?> mapReduceValuesToDoubleTask = (MapReduceValuesToDoubleTask<?, ?>)countedCompleter;
                    MapReduceValuesToDoubleTask<?, ?> nextRight;
                    for (MapReduceValuesToDoubleTask<?, ?> rights = mapReduceValuesToDoubleTask.rights; rights != null; rights = nextRight) {
                        mapReduceValuesToDoubleTask.result = reducer.applyAsDouble(mapReduceValuesToDoubleTask.result, rights.result);
                        final MapReduceValuesToDoubleTask<?, ?> mapReduceValuesToDoubleTask2 = mapReduceValuesToDoubleTask;
                        nextRight = rights.nextRight;
                        mapReduceValuesToDoubleTask2.rights = nextRight;
                    }
                }
            }
        }
    }
    
    static final class MapReduceValuesToIntTask<K, V> extends BulkTask<K, V, Integer>
    {
        final ToIntFunction<? super V> transformer;
        final IntBinaryOperator reducer;
        final int basis;
        int result;
        MapReduceValuesToIntTask<K, V> rights;
        MapReduceValuesToIntTask<K, V> nextRight;
        
        MapReduceValuesToIntTask(final BulkTask<K, V, ?> bulkTask, final int n, final int n2, final int n3, final Node<K, V>[] array, final MapReduceValuesToIntTask<K, V> nextRight, final ToIntFunction<? super V> transformer, final int basis, final IntBinaryOperator reducer) {
            super(bulkTask, n, n2, n3, array);
            this.nextRight = nextRight;
            this.transformer = transformer;
            this.basis = basis;
            this.reducer = reducer;
        }
        
        @Override
        public final Integer getRawResult() {
            return this.result;
        }
        
        @Override
        public final void compute() {
            final ToIntFunction<? super V> transformer;
            final IntBinaryOperator reducer;
            if ((transformer = this.transformer) != null && (reducer = this.reducer) != null) {
                int result = this.basis;
                final int baseIndex = this.baseIndex;
                int baseLimit2;
                int baseLimit;
                while (this.batch > 0 && (baseLimit = (baseLimit2 = this.baseLimit) + baseIndex >>> 1) > baseIndex) {
                    this.addToPendingCount(1);
                    final int batch = this.batch >>> 1;
                    this.batch = batch;
                    (this.rights = new MapReduceValuesToIntTask<K, V>((BulkTask<Object, Object, ?>)this, batch, this.baseLimit = baseLimit, baseLimit2, (Node<Object, Object>[])this.tab, (MapReduceValuesToIntTask<Object, Object>)this.rights, (ToIntFunction<? super Object>)transformer, result, reducer)).fork();
                }
                Node<K, V> advance;
                while ((advance = this.advance()) != null) {
                    result = reducer.applyAsInt(result, transformer.applyAsInt(advance.val));
                }
                this.result = result;
                for (CountedCompleter<?> countedCompleter = this.firstComplete(); countedCompleter != null; countedCompleter = countedCompleter.nextComplete()) {
                    final MapReduceValuesToIntTask<?, ?> mapReduceValuesToIntTask = (MapReduceValuesToIntTask<?, ?>)countedCompleter;
                    MapReduceValuesToIntTask<?, ?> nextRight;
                    for (MapReduceValuesToIntTask<?, ?> rights = mapReduceValuesToIntTask.rights; rights != null; rights = nextRight) {
                        mapReduceValuesToIntTask.result = reducer.applyAsInt(mapReduceValuesToIntTask.result, rights.result);
                        final MapReduceValuesToIntTask<?, ?> mapReduceValuesToIntTask2 = mapReduceValuesToIntTask;
                        nextRight = rights.nextRight;
                        mapReduceValuesToIntTask2.rights = nextRight;
                    }
                }
            }
        }
    }
    
    static final class MapReduceValuesToLongTask<K, V> extends BulkTask<K, V, Long>
    {
        final ToLongFunction<? super V> transformer;
        final LongBinaryOperator reducer;
        final long basis;
        long result;
        MapReduceValuesToLongTask<K, V> rights;
        MapReduceValuesToLongTask<K, V> nextRight;
        
        MapReduceValuesToLongTask(final BulkTask<K, V, ?> bulkTask, final int n, final int n2, final int n3, final Node<K, V>[] array, final MapReduceValuesToLongTask<K, V> nextRight, final ToLongFunction<? super V> transformer, final long basis, final LongBinaryOperator reducer) {
            super(bulkTask, n, n2, n3, array);
            this.nextRight = nextRight;
            this.transformer = transformer;
            this.basis = basis;
            this.reducer = reducer;
        }
        
        @Override
        public final Long getRawResult() {
            return this.result;
        }
        
        @Override
        public final void compute() {
            final ToLongFunction<? super V> transformer;
            final LongBinaryOperator reducer;
            if ((transformer = this.transformer) != null && (reducer = this.reducer) != null) {
                long result = this.basis;
                final int baseIndex = this.baseIndex;
                int baseLimit2;
                int baseLimit;
                while (this.batch > 0 && (baseLimit = (baseLimit2 = this.baseLimit) + baseIndex >>> 1) > baseIndex) {
                    this.addToPendingCount(1);
                    final int batch = this.batch >>> 1;
                    this.batch = batch;
                    (this.rights = new MapReduceValuesToLongTask<K, V>((BulkTask<Object, Object, ?>)this, batch, this.baseLimit = baseLimit, baseLimit2, (Node<Object, Object>[])this.tab, (MapReduceValuesToLongTask<Object, Object>)this.rights, (ToLongFunction<? super Object>)transformer, result, reducer)).fork();
                }
                Node<K, V> advance;
                while ((advance = this.advance()) != null) {
                    result = reducer.applyAsLong(result, transformer.applyAsLong(advance.val));
                }
                this.result = result;
                for (CountedCompleter<?> countedCompleter = this.firstComplete(); countedCompleter != null; countedCompleter = countedCompleter.nextComplete()) {
                    final MapReduceValuesToLongTask<?, ?> mapReduceValuesToLongTask = (MapReduceValuesToLongTask<?, ?>)countedCompleter;
                    MapReduceValuesToLongTask<?, ?> nextRight;
                    for (MapReduceValuesToLongTask<?, ?> rights = mapReduceValuesToLongTask.rights; rights != null; rights = nextRight) {
                        mapReduceValuesToLongTask.result = reducer.applyAsLong(mapReduceValuesToLongTask.result, rights.result);
                        final MapReduceValuesToLongTask<?, ?> mapReduceValuesToLongTask2 = mapReduceValuesToLongTask;
                        nextRight = rights.nextRight;
                        mapReduceValuesToLongTask2.rights = nextRight;
                    }
                }
            }
        }
    }
    
    static final class ReduceEntriesTask<K, V> extends BulkTask<K, V, Map.Entry<K, V>>
    {
        final BiFunction<Map.Entry<K, V>, Map.Entry<K, V>, ? extends Map.Entry<K, V>> reducer;
        Map.Entry<K, V> result;
        ReduceEntriesTask<K, V> rights;
        ReduceEntriesTask<K, V> nextRight;
        
        ReduceEntriesTask(final BulkTask<K, V, ?> bulkTask, final int n, final int n2, final int n3, final Node<K, V>[] array, final ReduceEntriesTask<K, V> nextRight, final BiFunction<Map.Entry<K, V>, Map.Entry<K, V>, ? extends Map.Entry<K, V>> reducer) {
            super(bulkTask, n, n2, n3, array);
            this.nextRight = nextRight;
            this.reducer = reducer;
        }
        
        @Override
        public final Map.Entry<K, V> getRawResult() {
            return this.result;
        }
        
        @Override
        public final void compute() {
            final BiFunction<Map.Entry<K, V>, Map.Entry<K, V>, ? extends Map.Entry<K, V>> reducer;
            if ((reducer = this.reducer) != null) {
                final int baseIndex = this.baseIndex;
                int baseLimit2;
                int baseLimit;
                while (this.batch > 0 && (baseLimit = (baseLimit2 = this.baseLimit) + baseIndex >>> 1) > baseIndex) {
                    this.addToPendingCount(1);
                    final int batch = this.batch >>> 1;
                    this.batch = batch;
                    (this.rights = new ReduceEntriesTask<K, V>((BulkTask<Object, Object, ?>)this, batch, this.baseLimit = baseLimit, baseLimit2, (Node<Object, Object>[])this.tab, (ReduceEntriesTask<Object, Object>)this.rights, (BiFunction<Map.Entry<Object, Object>, Map.Entry<Object, Object>, ? extends Map.Entry<Object, Object>>)reducer)).fork();
                }
                Map.Entry<K, V> result = null;
                Node<K, V> advance;
                while ((advance = this.advance()) != null) {
                    result = ((result == null) ? advance : ((Map.Entry<K, V>)reducer.apply(result, advance)));
                }
                this.result = result;
                for (CountedCompleter<?> countedCompleter = this.firstComplete(); countedCompleter != null; countedCompleter = countedCompleter.nextComplete()) {
                    final ReduceEntriesTask<?, ?> reduceEntriesTask = (ReduceEntriesTask<?, ?>)countedCompleter;
                    ReduceEntriesTask<?, ?> nextRight;
                    for (ReduceEntriesTask<?, ?> rights = reduceEntriesTask.rights; rights != null; rights = nextRight) {
                        final Map.Entry<?, ?> result2;
                        if ((result2 = rights.result) != null) {
                            final Map.Entry<?, ?> result3;
                            reduceEntriesTask.result = (((result3 = reduceEntriesTask.result) == null) ? result2 : reducer.apply((Map.Entry<K, V>)result3, (Node<K, V>)result2));
                        }
                        final ReduceEntriesTask<?, ?> reduceEntriesTask2 = reduceEntriesTask;
                        nextRight = rights.nextRight;
                        reduceEntriesTask2.rights = nextRight;
                    }
                }
            }
        }
    }
    
    static final class ReduceKeysTask<K, V> extends BulkTask<K, V, K>
    {
        final BiFunction<? super K, ? super K, ? extends K> reducer;
        K result;
        ReduceKeysTask<K, V> rights;
        ReduceKeysTask<K, V> nextRight;
        
        ReduceKeysTask(final BulkTask<K, V, ?> bulkTask, final int n, final int n2, final int n3, final Node<K, V>[] array, final ReduceKeysTask<K, V> nextRight, final BiFunction<? super K, ? super K, ? extends K> reducer) {
            super(bulkTask, n, n2, n3, array);
            this.nextRight = nextRight;
            this.reducer = reducer;
        }
        
        @Override
        public final K getRawResult() {
            return this.result;
        }
        
        @Override
        public final void compute() {
            final BiFunction<? super K, ? super K, ? extends K> reducer;
            if ((reducer = this.reducer) != null) {
                final int baseIndex = this.baseIndex;
                int baseLimit2;
                int baseLimit;
                while (this.batch > 0 && (baseLimit = (baseLimit2 = this.baseLimit) + baseIndex >>> 1) > baseIndex) {
                    this.addToPendingCount(1);
                    final int batch = this.batch >>> 1;
                    this.batch = batch;
                    (this.rights = new ReduceKeysTask<K, V>((BulkTask<Object, Object, ?>)this, batch, this.baseLimit = baseLimit, baseLimit2, (Node<Object, Object>[])this.tab, (ReduceKeysTask<Object, Object>)this.rights, (BiFunction<? super Object, ? super Object, ?>)reducer)).fork();
                }
                Object result = null;
                Node<K, V> advance;
                while ((advance = this.advance()) != null) {
                    final K key = advance.key;
                    result = ((result == null) ? key : ((key == null) ? result : reducer.apply((K)result, key)));
                }
                this.result = (K)result;
                for (CountedCompleter<?> countedCompleter = this.firstComplete(); countedCompleter != null; countedCompleter = countedCompleter.nextComplete()) {
                    final ReduceKeysTask<?, ?> reduceKeysTask = (ReduceKeysTask<?, ?>)countedCompleter;
                    ReduceKeysTask<?, ?> nextRight;
                    for (ReduceKeysTask<?, ?> rights = reduceKeysTask.rights; rights != null; rights = nextRight) {
                        final Object result2;
                        if ((result2 = rights.result) != null) {
                            final Object result3;
                            reduceKeysTask.result = (((result3 = reduceKeysTask.result) == null) ? result2 : reducer.apply((K)result3, (K)result2));
                        }
                        final ReduceKeysTask<?, ?> reduceKeysTask2 = reduceKeysTask;
                        nextRight = rights.nextRight;
                        reduceKeysTask2.rights = nextRight;
                    }
                }
            }
        }
    }
    
    static final class ReduceValuesTask<K, V> extends BulkTask<K, V, V>
    {
        final BiFunction<? super V, ? super V, ? extends V> reducer;
        V result;
        ReduceValuesTask<K, V> rights;
        ReduceValuesTask<K, V> nextRight;
        
        ReduceValuesTask(final BulkTask<K, V, ?> bulkTask, final int n, final int n2, final int n3, final Node<K, V>[] array, final ReduceValuesTask<K, V> nextRight, final BiFunction<? super V, ? super V, ? extends V> reducer) {
            super(bulkTask, n, n2, n3, array);
            this.nextRight = nextRight;
            this.reducer = reducer;
        }
        
        @Override
        public final V getRawResult() {
            return this.result;
        }
        
        @Override
        public final void compute() {
            final BiFunction<? super V, ? super V, ? extends V> reducer;
            if ((reducer = this.reducer) != null) {
                final int baseIndex = this.baseIndex;
                int baseLimit2;
                int baseLimit;
                while (this.batch > 0 && (baseLimit = (baseLimit2 = this.baseLimit) + baseIndex >>> 1) > baseIndex) {
                    this.addToPendingCount(1);
                    final int batch = this.batch >>> 1;
                    this.batch = batch;
                    (this.rights = new ReduceValuesTask<K, V>((BulkTask<Object, Object, ?>)this, batch, this.baseLimit = baseLimit, baseLimit2, (Node<Object, Object>[])this.tab, (ReduceValuesTask<Object, Object>)this.rights, (BiFunction<? super Object, ? super Object, ?>)reducer)).fork();
                }
                V result = null;
                Node<K, V> advance;
                while ((advance = this.advance()) != null) {
                    final V val = advance.val;
                    result = ((result == null) ? val : reducer.apply(result, val));
                }
                this.result = result;
                for (CountedCompleter<?> countedCompleter = this.firstComplete(); countedCompleter != null; countedCompleter = countedCompleter.nextComplete()) {
                    final ReduceValuesTask<?, ?> reduceValuesTask = (ReduceValuesTask<?, ?>)countedCompleter;
                    ReduceValuesTask<?, ?> nextRight;
                    for (ReduceValuesTask<?, ?> rights = reduceValuesTask.rights; rights != null; rights = nextRight) {
                        final Object result2;
                        if ((result2 = rights.result) != null) {
                            final Object result3;
                            reduceValuesTask.result = (((result3 = reduceValuesTask.result) == null) ? result2 : reducer.apply((V)result3, (V)result2));
                        }
                        final ReduceValuesTask<?, ?> reduceValuesTask2 = reduceValuesTask;
                        nextRight = rights.nextRight;
                        reduceValuesTask2.rights = nextRight;
                    }
                }
            }
        }
    }
    
    static final class ReservationNode<K, V> extends Node<K, V>
    {
        ReservationNode() {
            super(-3, null, null, null);
        }
        
        @Override
        Node<K, V> find(final int n, final Object o) {
            return null;
        }
    }
    
    static final class SearchEntriesTask<K, V, U> extends BulkTask<K, V, U>
    {
        final Function<Map.Entry<K, V>, ? extends U> searchFunction;
        final AtomicReference<U> result;
        
        SearchEntriesTask(final BulkTask<K, V, ?> bulkTask, final int n, final int n2, final int n3, final Node<K, V>[] array, final Function<Map.Entry<K, V>, ? extends U> searchFunction, final AtomicReference<U> result) {
            super(bulkTask, n, n2, n3, array);
            this.searchFunction = searchFunction;
            this.result = result;
        }
        
        @Override
        public final U getRawResult() {
            return this.result.get();
        }
        
        @Override
        public final void compute() {
            final Function<Map.Entry<K, V>, ? extends U> searchFunction;
            final AtomicReference<U> result;
            if ((searchFunction = this.searchFunction) != null && (result = this.result) != null) {
                final int baseIndex = this.baseIndex;
                int baseLimit2;
                int baseLimit;
                while (this.batch > 0 && (baseLimit = (baseLimit2 = this.baseLimit) + baseIndex >>> 1) > baseIndex) {
                    if (result.get() != null) {
                        return;
                    }
                    this.addToPendingCount(1);
                    final int batch = this.batch >>> 1;
                    this.batch = batch;
                    new SearchEntriesTask((BulkTask<Object, Object, ?>)this, batch, this.baseLimit = baseLimit, baseLimit2, (Node<Object, Object>[])this.tab, (Function<Map.Entry<Object, Object>, ?>)searchFunction, (AtomicReference<Object>)result).fork();
                }
                while (result.get() == null) {
                    final Node<K, V> advance;
                    if ((advance = this.advance()) == null) {
                        this.propagateCompletion();
                        break;
                    }
                    final U apply;
                    if ((apply = (U)searchFunction.apply(advance)) != null) {
                        if (result.compareAndSet(null, apply)) {
                            this.quietlyCompleteRoot();
                        }
                    }
                }
            }
        }
    }
    
    static final class SearchKeysTask<K, V, U> extends BulkTask<K, V, U>
    {
        final Function<? super K, ? extends U> searchFunction;
        final AtomicReference<U> result;
        
        SearchKeysTask(final BulkTask<K, V, ?> bulkTask, final int n, final int n2, final int n3, final Node<K, V>[] array, final Function<? super K, ? extends U> searchFunction, final AtomicReference<U> result) {
            super(bulkTask, n, n2, n3, array);
            this.searchFunction = searchFunction;
            this.result = result;
        }
        
        @Override
        public final U getRawResult() {
            return this.result.get();
        }
        
        @Override
        public final void compute() {
            final Function<? super K, ? extends U> searchFunction;
            final AtomicReference<U> result;
            if ((searchFunction = this.searchFunction) != null && (result = this.result) != null) {
                final int baseIndex = this.baseIndex;
                int baseLimit2;
                int baseLimit;
                while (this.batch > 0 && (baseLimit = (baseLimit2 = this.baseLimit) + baseIndex >>> 1) > baseIndex) {
                    if (result.get() != null) {
                        return;
                    }
                    this.addToPendingCount(1);
                    final int batch = this.batch >>> 1;
                    this.batch = batch;
                    new SearchKeysTask((BulkTask<Object, Object, ?>)this, batch, this.baseLimit = baseLimit, baseLimit2, (Node<Object, Object>[])this.tab, (Function<? super Object, ?>)searchFunction, (AtomicReference<Object>)result).fork();
                }
                while (result.get() == null) {
                    final Node<K, V> advance;
                    if ((advance = this.advance()) == null) {
                        this.propagateCompletion();
                        break;
                    }
                    final U apply;
                    if ((apply = (U)searchFunction.apply((K)advance.key)) == null) {
                        continue;
                    }
                    if (result.compareAndSet(null, apply)) {
                        this.quietlyCompleteRoot();
                        break;
                    }
                    break;
                }
            }
        }
    }
    
    static final class SearchMappingsTask<K, V, U> extends BulkTask<K, V, U>
    {
        final BiFunction<? super K, ? super V, ? extends U> searchFunction;
        final AtomicReference<U> result;
        
        SearchMappingsTask(final BulkTask<K, V, ?> bulkTask, final int n, final int n2, final int n3, final Node<K, V>[] array, final BiFunction<? super K, ? super V, ? extends U> searchFunction, final AtomicReference<U> result) {
            super(bulkTask, n, n2, n3, array);
            this.searchFunction = searchFunction;
            this.result = result;
        }
        
        @Override
        public final U getRawResult() {
            return this.result.get();
        }
        
        @Override
        public final void compute() {
            final BiFunction<? super K, ? super V, ? extends U> searchFunction;
            final AtomicReference<U> result;
            if ((searchFunction = this.searchFunction) != null && (result = this.result) != null) {
                final int baseIndex = this.baseIndex;
                int baseLimit2;
                int baseLimit;
                while (this.batch > 0 && (baseLimit = (baseLimit2 = this.baseLimit) + baseIndex >>> 1) > baseIndex) {
                    if (result.get() != null) {
                        return;
                    }
                    this.addToPendingCount(1);
                    final int batch = this.batch >>> 1;
                    this.batch = batch;
                    new SearchMappingsTask((BulkTask<Object, Object, ?>)this, batch, this.baseLimit = baseLimit, baseLimit2, (Node<Object, Object>[])this.tab, (BiFunction<? super Object, ? super Object, ?>)searchFunction, (AtomicReference<Object>)result).fork();
                }
                while (result.get() == null) {
                    final Node<K, V> advance;
                    if ((advance = this.advance()) == null) {
                        this.propagateCompletion();
                        break;
                    }
                    final U apply;
                    if ((apply = (U)searchFunction.apply((K)advance.key, (V)advance.val)) == null) {
                        continue;
                    }
                    if (result.compareAndSet(null, apply)) {
                        this.quietlyCompleteRoot();
                        break;
                    }
                    break;
                }
            }
        }
    }
    
    static final class SearchValuesTask<K, V, U> extends BulkTask<K, V, U>
    {
        final Function<? super V, ? extends U> searchFunction;
        final AtomicReference<U> result;
        
        SearchValuesTask(final BulkTask<K, V, ?> bulkTask, final int n, final int n2, final int n3, final Node<K, V>[] array, final Function<? super V, ? extends U> searchFunction, final AtomicReference<U> result) {
            super(bulkTask, n, n2, n3, array);
            this.searchFunction = searchFunction;
            this.result = result;
        }
        
        @Override
        public final U getRawResult() {
            return this.result.get();
        }
        
        @Override
        public final void compute() {
            final Function<? super V, ? extends U> searchFunction;
            final AtomicReference<U> result;
            if ((searchFunction = this.searchFunction) != null && (result = this.result) != null) {
                final int baseIndex = this.baseIndex;
                int baseLimit2;
                int baseLimit;
                while (this.batch > 0 && (baseLimit = (baseLimit2 = this.baseLimit) + baseIndex >>> 1) > baseIndex) {
                    if (result.get() != null) {
                        return;
                    }
                    this.addToPendingCount(1);
                    final int batch = this.batch >>> 1;
                    this.batch = batch;
                    new SearchValuesTask((BulkTask<Object, Object, ?>)this, batch, this.baseLimit = baseLimit, baseLimit2, (Node<Object, Object>[])this.tab, (Function<? super Object, ?>)searchFunction, (AtomicReference<Object>)result).fork();
                }
                while (result.get() == null) {
                    final Node<K, V> advance;
                    if ((advance = this.advance()) == null) {
                        this.propagateCompletion();
                        break;
                    }
                    final U apply;
                    if ((apply = (U)searchFunction.apply((V)advance.val)) == null) {
                        continue;
                    }
                    if (result.compareAndSet(null, apply)) {
                        this.quietlyCompleteRoot();
                        break;
                    }
                    break;
                }
            }
        }
    }
    
    static class Segment<K, V> extends ReentrantLock implements Serializable
    {
        private static final long serialVersionUID = 2249069246763182397L;
        final float loadFactor;
        
        Segment(final float loadFactor) {
            this.loadFactor = loadFactor;
        }
    }
    
    static final class ValueIterator<K, V> extends BaseIterator<K, V> implements Iterator<V>, Enumeration<V>
    {
        ValueIterator(final Node<K, V>[] array, final int n, final int n2, final int n3, final ConcurrentHashMap<K, V> concurrentHashMap) {
            super(array, n, n2, n3, concurrentHashMap);
        }
        
        @Override
        public final V next() {
            final Node<K, V> next;
            if ((next = this.next) == null) {
                throw new NoSuchElementException();
            }
            final V val = next.val;
            this.lastReturned = next;
            this.advance();
            return val;
        }
        
        @Override
        public final V nextElement() {
            return this.next();
        }
    }
    
    static final class ValueSpliterator<K, V> extends Traverser<K, V> implements Spliterator<V>
    {
        long est;
        
        ValueSpliterator(final Node<K, V>[] array, final int n, final int n2, final int n3, final long est) {
            super(array, n, n2, n3);
            this.est = est;
        }
        
        @Override
        public Spliterator<V> trySplit() {
            final int baseIndex = this.baseIndex;
            final int baseLimit;
            final int n;
            Spliterator<V> spliterator;
            if ((n = baseIndex + (baseLimit = this.baseLimit) >>> 1) <= baseIndex) {
                spliterator = null;
            }
            else {
                final Node<K, V>[] tab;
                final int baseSize;
                final int baseLimit2;
                spliterator = new ValueSpliterator<Object, V>((Node<Object, Object>[])tab, baseSize, baseLimit2, baseLimit, this.est >>>= 1);
                tab = this.tab;
                baseSize = this.baseSize;
                baseLimit2 = n;
                this.baseLimit = baseLimit2;
            }
            return spliterator;
        }
        
        @Override
        public void forEachRemaining(final Consumer<? super V> consumer) {
            if (consumer == null) {
                throw new NullPointerException();
            }
            Node<K, V> advance;
            while ((advance = this.advance()) != null) {
                consumer.accept(advance.val);
            }
        }
        
        @Override
        public boolean tryAdvance(final Consumer<? super V> consumer) {
            if (consumer == null) {
                throw new NullPointerException();
            }
            final Node<K, V> advance;
            if ((advance = this.advance()) == null) {
                return false;
            }
            consumer.accept(advance.val);
            return true;
        }
        
        @Override
        public long estimateSize() {
            return this.est;
        }
        
        @Override
        public int characteristics() {
            return 4352;
        }
    }
    
    static final class ValuesView<K, V> extends CollectionView<K, V, V> implements Collection<V>, Serializable
    {
        private static final long serialVersionUID = 2249069246763182397L;
        
        ValuesView(final ConcurrentHashMap<K, V> concurrentHashMap) {
            super(concurrentHashMap);
        }
        
        @Override
        public final boolean contains(final Object o) {
            return this.map.containsValue(o);
        }
        
        @Override
        public final boolean remove(final Object o) {
            if (o != null) {
                final Iterator<Object> iterator = (Iterator<Object>)this.iterator();
                while (iterator.hasNext()) {
                    if (o.equals(iterator.next())) {
                        iterator.remove();
                        return true;
                    }
                }
            }
            return false;
        }
        
        @Override
        public final Iterator<V> iterator() {
            final ConcurrentHashMap<K, V> map = this.map;
            final Node<Object, Object>[] table;
            final int n = ((table = (Node<Object, Object>[])map.table) == null) ? 0 : table.length;
            return new ValueIterator<Object, V>(table, n, 0, n, (ConcurrentHashMap<Object, Object>)map);
        }
        
        @Override
        public final boolean add(final V v) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public final boolean addAll(final Collection<? extends V> collection) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public Spliterator<V> spliterator() {
            final ConcurrentHashMap<K, V> map = this.map;
            final long sumCount = map.sumCount();
            final Node<K, V>[] table;
            final int n = ((table = map.table) == null) ? 0 : ((Node<Object, Object>[])table).length;
            return new ValueSpliterator<Object, V>((Node<Object, Object>[])table, n, 0, n, (sumCount < 0L) ? 0L : sumCount);
        }
        
        @Override
        public void forEach(final Consumer<? super V> consumer) {
            if (consumer == null) {
                throw new NullPointerException();
            }
            final Node<K, V>[] table;
            if ((table = this.map.table) != null) {
                Node<Object, Object> advance;
                while ((advance = new Traverser<Object, Object>((Node<Object, Object>[])table, ((Node<Object, Object>[])table).length, 0, ((Node<Object, Object>[])table).length).advance()) != null) {
                    consumer.accept((Object)advance.val);
                }
            }
        }
    }
}
