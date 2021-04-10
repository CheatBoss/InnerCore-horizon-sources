package android.arch.core.internal;

import java.util.*;

public class FastSafeIterableMap<K, V> extends SafeIterableMap<K, V>
{
    private HashMap<K, Entry<K, V>> mHashMap;
    
    public FastSafeIterableMap() {
        this.mHashMap = new HashMap<K, Entry<K, V>>();
    }
    
    public boolean contains(final K k) {
        return this.mHashMap.containsKey(k);
    }
}
