package com.bumptech.glide.util;

public class MultiClassKey
{
    private Class<?> first;
    private Class<?> second;
    
    public MultiClassKey() {
    }
    
    public MultiClassKey(final Class<?> clazz, final Class<?> clazz2) {
        this.set(clazz, clazz2);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (this.getClass() != o.getClass()) {
            return false;
        }
        final MultiClassKey multiClassKey = (MultiClassKey)o;
        return this.first.equals(multiClassKey.first) && this.second.equals(multiClassKey.second);
    }
    
    @Override
    public int hashCode() {
        return this.first.hashCode() * 31 + this.second.hashCode();
    }
    
    public void set(final Class<?> first, final Class<?> second) {
        this.first = first;
        this.second = second;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("MultiClassKey{first=");
        sb.append(this.first);
        sb.append(", second=");
        sb.append(this.second);
        sb.append('}');
        return sb.toString();
    }
}
