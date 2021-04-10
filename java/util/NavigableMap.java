package java.util;

public interface NavigableMap<K, V> extends SortedMap<K, V>
{
    Map.Entry<K, V> lowerEntry(final K p0);
    
    K lowerKey(final K p0);
    
    Map.Entry<K, V> floorEntry(final K p0);
    
    K floorKey(final K p0);
    
    Map.Entry<K, V> ceilingEntry(final K p0);
    
    K ceilingKey(final K p0);
    
    Map.Entry<K, V> higherEntry(final K p0);
    
    K higherKey(final K p0);
    
    Map.Entry<K, V> firstEntry();
    
    Map.Entry<K, V> lastEntry();
    
    Map.Entry<K, V> pollFirstEntry();
    
    Map.Entry<K, V> pollLastEntry();
    
    NavigableMap<K, V> descendingMap();
    
    NavigableSet<K> navigableKeySet();
    
    NavigableSet<K> descendingKeySet();
    
    NavigableMap<K, V> subMap(final K p0, final boolean p1, final K p2, final boolean p3);
    
    NavigableMap<K, V> headMap(final K p0, final boolean p1);
    
    NavigableMap<K, V> tailMap(final K p0, final boolean p1);
    
    SortedMap<K, V> subMap(final K p0, final K p1);
    
    SortedMap<K, V> headMap(final K p0);
    
    SortedMap<K, V> tailMap(final K p0);
}
