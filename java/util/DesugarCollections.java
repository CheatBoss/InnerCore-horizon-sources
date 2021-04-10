package java.util;

import java.lang.reflect.*;
import java.io.*;
import java.util.function.*;

public class DesugarCollections
{
    private static final Field COLLECTION_FIELD;
    private static final Field MUTEX_FIELD;
    public static final Class<? extends Collection> SYNCHRONIZED_COLLECTION;
    private static final Constructor<? extends Collection> SYNCHRONIZED_COLLECTION_CONSTRUCTOR;
    static final Class<? extends List> SYNCHRONIZED_LIST;
    private static final Constructor<? extends Set> SYNCHRONIZED_SET_CONSTRUCTOR;
    
    static {
        SYNCHRONIZED_COLLECTION = Collections.synchronizedCollection(new ArrayList<Object>()).getClass();
        SYNCHRONIZED_LIST = Collections.synchronizedList(new LinkedList<Object>()).getClass();
        MUTEX_FIELD = getField(DesugarCollections.SYNCHRONIZED_COLLECTION, "mutex");
        if (DesugarCollections.MUTEX_FIELD != null) {
            DesugarCollections.MUTEX_FIELD.setAccessible(true);
        }
        COLLECTION_FIELD = getField(DesugarCollections.SYNCHRONIZED_COLLECTION, "c");
        if (DesugarCollections.COLLECTION_FIELD != null) {
            DesugarCollections.COLLECTION_FIELD.setAccessible(true);
        }
        SYNCHRONIZED_SET_CONSTRUCTOR = getConstructor((Class<?>)Collections.synchronizedSet(new HashSet<Object>()).getClass(), (Class<?>[])new Class[] { Set.class, Object.class });
        if (DesugarCollections.SYNCHRONIZED_SET_CONSTRUCTOR != null) {
            DesugarCollections.SYNCHRONIZED_SET_CONSTRUCTOR.setAccessible(true);
        }
        SYNCHRONIZED_COLLECTION_CONSTRUCTOR = getConstructor((Class<?>)DesugarCollections.SYNCHRONIZED_COLLECTION, (Class<?>[])new Class[] { Collection.class, Object.class });
        if (DesugarCollections.SYNCHRONIZED_COLLECTION_CONSTRUCTOR != null) {
            DesugarCollections.SYNCHRONIZED_COLLECTION_CONSTRUCTOR.setAccessible(true);
        }
    }
    
    private DesugarCollections() {
    }
    
    public static <E> void forEach(final Iterable<E> iterable, final Consumer<? super E> consumer) {
        if (DesugarCollections.MUTEX_FIELD == null) {
            try {
                ((Collection)DesugarCollections.COLLECTION_FIELD.get(iterable)).forEach(consumer);
                return;
            }
            catch (IllegalAccessException ex) {
                throw new Error("Runtime illegal access in synchronized collection forEach fall-back.", ex);
            }
        }
        try {
            synchronized (DesugarCollections.MUTEX_FIELD.get(iterable)) {
                ((Collection)DesugarCollections.COLLECTION_FIELD.get(iterable)).forEach(consumer);
            }
        }
        catch (IllegalAccessException ex2) {
            throw new Error("Runtime illegal access in synchronized collection forEach.", ex2);
        }
    }
    
    private static <E> Constructor<? extends E> getConstructor(final Class<? extends E> clazz, final Class<?>... array) {
        try {
            return clazz.getDeclaredConstructor(array);
        }
        catch (NoSuchMethodException ex) {
            return null;
        }
    }
    
    private static Field getField(final Class<?> clazz, final String s) {
        try {
            return clazz.getDeclaredField(s);
        }
        catch (NoSuchFieldException ex) {
            return null;
        }
    }
    
    static <E> boolean removeIf(final Collection<E> collection, final Predicate<? super E> predicate) {
        if (DesugarCollections.MUTEX_FIELD == null) {
            try {
                return ((Collection)DesugarCollections.COLLECTION_FIELD.get(collection)).removeIf(predicate);
            }
            catch (IllegalAccessException ex) {
                throw new Error("Runtime illegal access in synchronized collection removeIf fall-back.", ex);
            }
        }
        try {
            synchronized (DesugarCollections.MUTEX_FIELD.get(collection)) {
                return ((Collection)DesugarCollections.COLLECTION_FIELD.get(collection)).removeIf(predicate);
            }
        }
        catch (IllegalAccessException ex2) {
            throw new Error("Runtime illegal access in synchronized collection removeIf.", ex2);
        }
    }
    
    static <E> void replaceAll(final List<E> list, final UnaryOperator<E> unaryOperator) {
        if (DesugarCollections.MUTEX_FIELD == null) {
            try {
                ((List)DesugarCollections.COLLECTION_FIELD.get(list)).replaceAll(unaryOperator);
                return;
            }
            catch (IllegalAccessException ex) {
                throw new Error("Runtime illegal access in synchronized list replaceAll fall-back.", ex);
            }
        }
        try {
            synchronized (DesugarCollections.MUTEX_FIELD.get(list)) {
                ((List)DesugarCollections.COLLECTION_FIELD.get(list)).replaceAll(unaryOperator);
            }
        }
        catch (IllegalAccessException ex2) {
            throw new Error("Runtime illegal access in synchronized list replaceAll.", ex2);
        }
    }
    
    static <E> void sort(final List<E> list, final Comparator<? super E> comparator) {
        if (DesugarCollections.MUTEX_FIELD == null) {
            try {
                ((List)DesugarCollections.COLLECTION_FIELD.get(list)).sort(comparator);
                return;
            }
            catch (IllegalAccessException ex) {
                throw new Error("Runtime illegal access in synchronized collection sort fall-back.", ex);
            }
        }
        try {
            synchronized (DesugarCollections.MUTEX_FIELD.get(list)) {
                ((List)DesugarCollections.COLLECTION_FIELD.get(list)).sort(comparator);
            }
        }
        catch (IllegalAccessException ex2) {
            throw new Error("Runtime illegal access in synchronized list sort.", ex2);
        }
    }
    
    public static <K, V> Map<K, V> synchronizedMap(final Map<K, V> map) {
        return new SynchronizedMap<K, V>(map);
    }
    
    public static <K, V> SortedMap<K, V> synchronizedSortedMap(final SortedMap<K, V> sortedMap) {
        return new SynchronizedSortedMap<K, V>(sortedMap);
    }
    
    private static class SynchronizedMap<K, V> implements Map<K, V>, Serializable
    {
        private static final long serialVersionUID = 1978198479659022715L;
        private transient Set<Entry<K, V>> entrySet;
        private transient Set<K> keySet;
        private final Map<K, V> m;
        final Object mutex;
        private transient Collection<V> values;
        
        SynchronizedMap(final Map<K, V> map) {
            map.getClass();
            this.m = map;
            this.mutex = this;
        }
        
        SynchronizedMap(final Map<K, V> m, final Object mutex) {
            this.m = m;
            this.mutex = mutex;
        }
        
        private <T> Collection<T> instantiateCollection(final Collection<T> collection, final Object o) {
            if (DesugarCollections.SYNCHRONIZED_COLLECTION_CONSTRUCTOR == null) {
                return Collections.synchronizedCollection(collection);
            }
            try {
                return DesugarCollections.SYNCHRONIZED_COLLECTION_CONSTRUCTOR.newInstance(collection, o);
            }
            catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
                final Object o2;
                throw new Error("Unable to instantiate a synchronized list.", (Throwable)o2);
            }
        }
        
        private <T> Set<T> instantiateSet(final Set<T> set, final Object o) {
            if (DesugarCollections.SYNCHRONIZED_SET_CONSTRUCTOR == null) {
                return Collections.synchronizedSet(set);
            }
            try {
                return DesugarCollections.SYNCHRONIZED_SET_CONSTRUCTOR.newInstance(set, o);
            }
            catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
                final Object o2;
                throw new Error("Unable to instantiate a synchronized list.", (Throwable)o2);
            }
        }
        
        private void writeObject(final ObjectOutputStream objectOutputStream) throws IOException {
            synchronized (this.mutex) {
                objectOutputStream.defaultWriteObject();
            }
        }
        
        @Override
        public void clear() {
            synchronized (this.mutex) {
                this.m.clear();
            }
        }
        
        @Override
        public V compute(final K k, final BiFunction<? super K, ? super V, ? extends V> biFunction) {
            synchronized (this.mutex) {
                return this.m.compute(k, biFunction);
            }
        }
        
        @Override
        public V computeIfAbsent(final K k, final Function<? super K, ? extends V> function) {
            synchronized (this.mutex) {
                return this.m.computeIfAbsent(k, function);
            }
        }
        
        @Override
        public V computeIfPresent(final K k, final BiFunction<? super K, ? super V, ? extends V> biFunction) {
            synchronized (this.mutex) {
                return this.m.computeIfPresent(k, biFunction);
            }
        }
        
        @Override
        public boolean containsKey(final Object o) {
            synchronized (this.mutex) {
                return this.m.containsKey(o);
            }
        }
        
        @Override
        public boolean containsValue(final Object o) {
            synchronized (this.mutex) {
                return this.m.containsValue(o);
            }
        }
        
        @Override
        public Set<Entry<K, V>> entrySet() {
            synchronized (this.mutex) {
                if (this.entrySet == null) {
                    this.entrySet = this.instantiateSet(this.m.entrySet(), this.mutex);
                }
                return this.entrySet;
            }
        }
        
        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            synchronized (this.mutex) {
                return this.m.equals(o);
            }
        }
        
        @Override
        public void forEach(final BiConsumer<? super K, ? super V> biConsumer) {
            synchronized (this.mutex) {
                this.m.forEach(biConsumer);
            }
        }
        
        @Override
        public V get(Object value) {
            synchronized (this.mutex) {
                value = this.m.get(value);
                return (V)value;
            }
        }
        
        @Override
        public V getOrDefault(Object orDefault, final V v) {
            synchronized (this.mutex) {
                orDefault = this.m.getOrDefault(orDefault, v);
                return (V)orDefault;
            }
        }
        
        @Override
        public int hashCode() {
            synchronized (this.mutex) {
                return this.m.hashCode();
            }
        }
        
        @Override
        public boolean isEmpty() {
            synchronized (this.mutex) {
                return this.m.isEmpty();
            }
        }
        
        @Override
        public Set<K> keySet() {
            synchronized (this.mutex) {
                if (this.keySet == null) {
                    this.keySet = this.instantiateSet(this.m.keySet(), this.mutex);
                }
                return this.keySet;
            }
        }
        
        @Override
        public V merge(final K k, final V v, final BiFunction<? super V, ? super V, ? extends V> biFunction) {
            synchronized (this.mutex) {
                return this.m.merge(k, v, biFunction);
            }
        }
        
        @Override
        public V put(final K k, final V v) {
            synchronized (this.mutex) {
                return this.m.put(k, v);
            }
        }
        
        @Override
        public void putAll(final Map<? extends K, ? extends V> map) {
            synchronized (this.mutex) {
                this.m.putAll(map);
            }
        }
        
        @Override
        public V putIfAbsent(final K k, final V v) {
            synchronized (this.mutex) {
                return this.m.putIfAbsent(k, v);
            }
        }
        
        @Override
        public V remove(Object remove) {
            synchronized (this.mutex) {
                remove = this.m.remove(remove);
                return (V)remove;
            }
        }
        
        @Override
        public boolean remove(final Object o, final Object o2) {
            synchronized (this.mutex) {
                return this.m.remove(o, o2);
            }
        }
        
        @Override
        public V replace(final K k, final V v) {
            synchronized (this.mutex) {
                return this.m.replace(k, v);
            }
        }
        
        @Override
        public boolean replace(final K k, final V v, final V v2) {
            synchronized (this.mutex) {
                return this.m.replace(k, v, v2);
            }
        }
        
        @Override
        public void replaceAll(final BiFunction<? super K, ? super V, ? extends V> biFunction) {
            synchronized (this.mutex) {
                this.m.replaceAll(biFunction);
            }
        }
        
        @Override
        public int size() {
            synchronized (this.mutex) {
                return this.m.size();
            }
        }
        
        @Override
        public String toString() {
            synchronized (this.mutex) {
                return this.m.toString();
            }
        }
        
        @Override
        public Collection<V> values() {
            synchronized (this.mutex) {
                if (this.values == null) {
                    this.values = this.instantiateCollection(this.m.values(), this.mutex);
                }
                return this.values;
            }
        }
    }
    
    static class SynchronizedSortedMap<K, V> extends SynchronizedMap<K, V> implements SortedMap<K, V>
    {
        private static final long serialVersionUID = -8798146769416483793L;
        private final SortedMap<K, V> sm;
        
        SynchronizedSortedMap(final SortedMap<K, V> sm) {
            super(sm);
            this.sm = sm;
        }
        
        SynchronizedSortedMap(final SortedMap<K, V> sm, final Object o) {
            super(sm, o);
            this.sm = sm;
        }
        
        @Override
        public Comparator<? super K> comparator() {
            synchronized (this.mutex) {
                return this.sm.comparator();
            }
        }
        
        @Override
        public K firstKey() {
            synchronized (this.mutex) {
                return this.sm.firstKey();
            }
        }
        
        @Override
        public SortedMap<K, V> headMap(final K k) {
            synchronized (this.mutex) {
                return new SynchronizedSortedMap((SortedMap<Object, Object>)this.sm.headMap(k), this.mutex);
            }
        }
        
        @Override
        public K lastKey() {
            synchronized (this.mutex) {
                return this.sm.lastKey();
            }
        }
        
        @Override
        public SortedMap<K, V> subMap(final K k, final K i) {
            synchronized (this.mutex) {
                return new SynchronizedSortedMap((SortedMap<Object, Object>)this.sm.subMap(k, i), this.mutex);
            }
        }
        
        @Override
        public SortedMap<K, V> tailMap(final K k) {
            synchronized (this.mutex) {
                return new SynchronizedSortedMap((SortedMap<Object, Object>)this.sm.tailMap(k), this.mutex);
            }
        }
    }
}
