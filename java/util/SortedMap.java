package java.util;

public interface SortedMap<K, V> extends Map<K, V>
{
    Comparator<? super K> comparator();
    
    SortedMap<K, V> subMap(final K p0, final K p1);
    
    SortedMap<K, V> headMap(final K p0);
    
    SortedMap<K, V> tailMap(final K p0);
    
    K firstKey();
    
    K lastKey();
    
    Set<K> keySet();
    
    Collection<V> values();
    
    Set<Entry<K, V>> entrySet();
}
