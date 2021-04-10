package com.google.android.gms.internal.measurement;

import java.util.*;

class zzxv extends AbstractSet<Map.Entry<Object, Object>>
{
    private final /* synthetic */ zzxm zzcch;
    
    private zzxv(final zzxm zzcch) {
        this.zzcch = zzcch;
    }
    
    @Override
    public void clear() {
        this.zzcch.clear();
    }
    
    @Override
    public boolean contains(Object value) {
        final Map.Entry entry = (Map.Entry)value;
        value = this.zzcch.get(entry.getKey());
        final Object value2 = entry.getValue();
        return value == value2 || (value != null && value.equals(value2));
    }
    
    @Override
    public Iterator<Map.Entry<Object, Object>> iterator() {
        return new zzxu(this.zzcch, null);
    }
    
    @Override
    public boolean remove(final Object o) {
        final Map.Entry entry = (Map.Entry)o;
        if (this.contains(entry)) {
            this.zzcch.remove(entry.getKey());
            return true;
        }
        return false;
    }
    
    @Override
    public int size() {
        return this.zzcch.size();
    }
}
