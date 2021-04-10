package java.util;

public interface Deque<E> extends Queue<E>
{
    void addFirst(final E p0);
    
    void addLast(final E p0);
    
    boolean offerFirst(final E p0);
    
    boolean offerLast(final E p0);
    
    E removeFirst();
    
    E removeLast();
    
    E pollFirst();
    
    E pollLast();
    
    E getFirst();
    
    E getLast();
    
    E peekFirst();
    
    E peekLast();
    
    boolean removeFirstOccurrence(final Object p0);
    
    boolean removeLastOccurrence(final Object p0);
    
    boolean add(final E p0);
    
    boolean offer(final E p0);
    
    E remove();
    
    E poll();
    
    E element();
    
    E peek();
    
    void push(final E p0);
    
    E pop();
    
    boolean remove(final Object p0);
    
    boolean contains(final Object p0);
    
    int size();
    
    Iterator<E> iterator();
    
    Iterator<E> descendingIterator();
}
