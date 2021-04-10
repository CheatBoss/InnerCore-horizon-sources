package com.android.dx.cf.iface;

import com.android.dx.util.*;

public final class StdAttributeList extends FixedSizeList implements AttributeList
{
    public StdAttributeList(final int n) {
        super(n);
    }
    
    @Override
    public int byteLength() {
        final int size = this.size();
        int n = 2;
        for (int i = 0; i < size; ++i) {
            n += this.get(i).byteLength();
        }
        return n;
    }
    
    @Override
    public Attribute findFirst(final String s) {
        for (int size = this.size(), i = 0; i < size; ++i) {
            final Attribute value = this.get(i);
            if (value.getName().equals(s)) {
                return value;
            }
        }
        return null;
    }
    
    @Override
    public Attribute findNext(final Attribute attribute) {
        for (int size = this.size(), i = 0; i < size; ++i) {
            if (this.get(i) == attribute) {
                Attribute value;
                do {
                    ++i;
                    if (i >= size) {
                        return null;
                    }
                    value = this.get(i);
                } while (!value.getName().equals(attribute.getName()));
                return value;
            }
        }
        return null;
    }
    
    @Override
    public Attribute get(final int n) {
        return (Attribute)this.get0(n);
    }
    
    public void set(final int n, final Attribute attribute) {
        this.set0(n, attribute);
    }
}
