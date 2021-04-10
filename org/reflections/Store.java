package org.reflections;

import com.google.common.base.*;
import java.util.concurrent.*;
import java.util.function.*;
import com.google.common.collect.*;
import java.util.*;

public class Store
{
    private transient boolean concurrent;
    private final Map<String, Multimap<String, String>> storeMap;
    
    protected Store() {
        this.storeMap = new HashMap<String, Multimap<String, String>>();
        this.concurrent = false;
    }
    
    public Store(final Configuration configuration) {
        this.storeMap = new HashMap<String, Multimap<String, String>>();
        this.concurrent = (configuration.getExecutorService() != null);
    }
    
    private Iterable<String> getAllIncluding(final String s, final Iterable<String> iterable, final IterableChain<String> iterableChain) {
        ((IterableChain<Object>)iterableChain).addAll(iterable);
        final Iterator<String> iterator = iterable.iterator();
        while (iterator.hasNext()) {
            final Iterable<String> value = this.get(s, iterator.next());
            if (value.iterator().hasNext()) {
                this.getAllIncluding(s, value, iterableChain);
            }
        }
        return iterableChain;
    }
    
    public Multimap<String, String> get(final String s) {
        final Multimap<String, String> multimap = this.storeMap.get(s);
        if (multimap == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Scanner ");
            sb.append(s);
            sb.append(" was not configured");
            throw new ReflectionsException(sb.toString());
        }
        return multimap;
    }
    
    public Iterable<String> get(final String s, final Iterable<String> iterable) {
        final Multimap<String, String> value = this.get(s);
        final IterableChain<Object> iterableChain = (IterableChain<Object>)new IterableChain<String>();
        final Iterator<String> iterator = iterable.iterator();
        while (iterator.hasNext()) {
            iterableChain.addAll(value.get((Object)iterator.next()));
        }
        return (Iterable<String>)iterableChain;
    }
    
    public Iterable<String> get(final String s, final String... array) {
        return this.get(s, Arrays.asList(array));
    }
    
    public Iterable<String> getAll(final String s, final Iterable<String> iterable) {
        return this.getAllIncluding(s, this.get(s, iterable), new IterableChain<String>());
    }
    
    public Iterable<String> getAll(final String s, final String s2) {
        return this.getAllIncluding(s, this.get(s, s2), new IterableChain<String>());
    }
    
    public Multimap<String, String> getOrCreate(final String s) {
        Object o;
        if ((o = this.storeMap.get(s)) == null) {
            o = Multimaps.newSetMultimap((Map)new HashMap(), (Supplier)new Supplier<Set<String>>() {
                public Set<String> get() {
                    return (Set<String>)Sets.newSetFromMap((Map)new ConcurrentHashMap());
                }
            });
            if (this.concurrent) {
                o = Multimaps.synchronizedSetMultimap((SetMultimap)o);
            }
            this.storeMap.put(s, (Multimap<String, String>)o);
        }
        return (Multimap<String, String>)o;
    }
    
    public Set<String> keySet() {
        return this.storeMap.keySet();
    }
    
    private static class IterableChain<T> implements Iterable<T>
    {
        private final List<Iterable<T>> chain;
        
        private IterableChain() {
            this.chain = (List<Iterable<T>>)Lists.newArrayList();
        }
        
        private void addAll(final Iterable<T> iterable) {
            this.chain.add(iterable);
        }
        
        @Override
        public void forEach(final Consumer<? super T> consumer) {
            Iterable-CC.$default$forEach((Iterable)this, (Consumer)consumer);
        }
        
        @Override
        public Iterator<T> iterator() {
            return Iterables.concat((Iterable)this.chain).iterator();
        }
        
        @Override
        public Spliterator<T> spliterator() {
            return (Spliterator<T>)Iterable-CC.$default$spliterator((Iterable)this);
        }
    }
}
