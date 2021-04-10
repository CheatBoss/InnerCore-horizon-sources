package com.google.android.gms.internal.measurement;

import java.util.stream.*;
import java.util.*;
import java.util.function.*;

abstract class zztz<E> extends AbstractList<E> implements zzvs<E>
{
    private boolean zzbtu;
    
    zztz() {
        this.zzbtu = true;
    }
    
    @Override
    public void add(final int n, final E e) {
        this.zztx();
        super.add(n, e);
    }
    
    @Override
    public boolean add(final E e) {
        this.zztx();
        return super.add(e);
    }
    
    @Override
    public boolean addAll(final int n, final Collection<? extends E> collection) {
        this.zztx();
        return super.addAll(n, collection);
    }
    
    @Override
    public boolean addAll(final Collection<? extends E> collection) {
        this.zztx();
        return super.addAll(collection);
    }
    
    @Override
    public void clear() {
        this.zztx();
        super.clear();
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof List)) {
            return false;
        }
        if (!(o instanceof RandomAccess)) {
            return super.equals(o);
        }
        final List list = (List)o;
        final int size = this.size();
        if (size != list.size()) {
            return false;
        }
        for (int i = 0; i < size; ++i) {
            if (!this.get(i).equals(list.get(i))) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public void forEach(final Consumer<? super E> p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: aload_1        
        //     2: invokestatic    java/util/Collection-CC.$default$forEach:(Ljava/util/Collection;Ljava/util/function/Consumer;)V
        //     5: return         
        //    Signature:
        //  (Ljava/util/function/Consumer<-TE;>;)V
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @Override
    public int hashCode() {
        final int size = this.size();
        int n = 1;
        for (int i = 0; i < size; ++i) {
            n = n * 31 + this.get(i).hashCode();
        }
        return n;
    }
    
    @Override
    public Stream<E> parallelStream() {
        return (Stream<E>)Collection-CC.$default$parallelStream();
    }
    
    @Override
    public E remove(final int n) {
        this.zztx();
        return super.remove(n);
    }
    
    @Override
    public boolean remove(final Object o) {
        this.zztx();
        return super.remove(o);
    }
    
    @Override
    public boolean removeAll(final Collection<?> collection) {
        this.zztx();
        return super.removeAll(collection);
    }
    
    @Override
    public boolean removeIf(final Predicate<? super E> p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: aload_1        
        //     2: invokestatic    java/util/Collection-CC.$default$removeIf:(Ljava/util/Collection;Ljava/util/function/Predicate;)Z
        //     5: ireturn        
        //    Signature:
        //  (Ljava/util/function/Predicate<-TE;>;)Z
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @Override
    public void replaceAll(final UnaryOperator<E> unaryOperator) {
        List-CC.$default$replaceAll((UnaryOperator<Object>)unaryOperator);
    }
    
    @Override
    public boolean retainAll(final Collection<?> collection) {
        this.zztx();
        return super.retainAll(collection);
    }
    
    @Override
    public E set(final int n, final E e) {
        this.zztx();
        return super.set(n, e);
    }
    
    @Override
    public void sort(final Comparator<? super E> p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: aload_1        
        //     2: invokestatic    java/util/List-CC.$default$sort:(Ljava/util/List;Ljava/util/Comparator;)V
        //     5: return         
        //    Signature:
        //  (Ljava/util/Comparator<-TE;>;)V
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @Override
    public Spliterator<E> spliterator() {
        return (Spliterator<E>)List-CC.$default$spliterator();
    }
    
    @Override
    public Stream<E> stream() {
        return (Stream<E>)Collection-CC.$default$stream();
    }
    
    public <T> T[] toArray(final IntFunction<T[]> intFunction) {
        return Collection-CC.$default$toArray(intFunction);
    }
    
    @Override
    public final void zzsm() {
        this.zzbtu = false;
    }
    
    @Override
    public boolean zztw() {
        return this.zzbtu;
    }
    
    protected final void zztx() {
        if (this.zzbtu) {
            return;
        }
        throw new UnsupportedOperationException();
    }
}
