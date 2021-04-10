package org.spongycastle.util;

import java.util.function.*;
import java.util.*;

public class CollectionStore<T> implements Iterable<T>, Store<T>
{
    private Collection<T> _local;
    
    public CollectionStore(final Collection<T> collection) {
        this._local = new ArrayList<T>((Collection<? extends T>)collection);
    }
    
    @Override
    public void forEach(final Consumer<? super T> consumer) {
        Iterable-CC.$default$forEach((java.lang.Iterable)this, (Consumer)consumer);
    }
    
    @Override
    public Collection<T> getMatches(final Selector<T> selector) {
        if (selector == null) {
            return new ArrayList<T>((Collection<? extends T>)this._local);
        }
        final ArrayList<T> list = new ArrayList<T>();
        for (final T next : this._local) {
            if (selector.match(next)) {
                list.add(next);
            }
        }
        return list;
    }
    
    @Override
    public Iterator<T> iterator() {
        return this.getMatches(null).iterator();
    }
    
    @Override
    public Spliterator<T> spliterator() {
        return (Spliterator<T>)Iterable-CC.$default$spliterator((java.lang.Iterable)this);
    }
}
