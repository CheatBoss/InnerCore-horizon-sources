package com.android.dx.dex.code;

import com.android.dx.util.*;
import com.android.dx.rop.code.*;

public final class PositionList extends FixedSizeList
{
    public static final PositionList EMPTY;
    public static final int IMPORTANT = 3;
    public static final int LINES = 2;
    public static final int NONE = 1;
    
    static {
        EMPTY = new PositionList(0);
    }
    
    public PositionList(final int n) {
        super(n);
    }
    
    public static PositionList make(final DalvInsnList list, int i) {
        switch (i) {
            default: {
                throw new IllegalArgumentException("bogus howMuch");
            }
            case 2:
            case 3: {
                final SourcePosition no_INFO = SourcePosition.NO_INFO;
                final int size = list.size();
                final Entry[] array = new Entry[size];
                int n = 0;
                int n2 = 0;
                final int n3 = 0;
                SourcePosition sourcePosition = no_INFO;
                int n4;
                int n5;
                SourcePosition sourcePosition2;
                for (int j = 0; j < size; ++j, n = n4, n2 = n5, sourcePosition = sourcePosition2) {
                    final DalvInsn value = list.get(j);
                    if (value instanceof CodeAddress) {
                        n4 = 1;
                        n5 = n2;
                        sourcePosition2 = sourcePosition;
                    }
                    else {
                        final SourcePosition position = value.getPosition();
                        n4 = n;
                        n5 = n2;
                        sourcePosition2 = sourcePosition;
                        if (!position.equals(no_INFO)) {
                            if (position.sameLine(sourcePosition)) {
                                n4 = n;
                                n5 = n2;
                                sourcePosition2 = sourcePosition;
                            }
                            else if (i == 3 && n == 0) {
                                n4 = n;
                                n5 = n2;
                                sourcePosition2 = sourcePosition;
                            }
                            else {
                                sourcePosition2 = position;
                                array[n2] = new Entry(value.getAddress(), position);
                                n5 = n2 + 1;
                                n4 = 0;
                            }
                        }
                    }
                }
                final PositionList list2 = new PositionList(n2);
                for (i = n3; i < n2; ++i) {
                    list2.set(i, array[i]);
                }
                list2.setImmutable();
                return list2;
            }
            case 1: {
                return PositionList.EMPTY;
            }
        }
    }
    
    public Entry get(final int n) {
        return (Entry)this.get0(n);
    }
    
    public void set(final int n, final Entry entry) {
        this.set0(n, entry);
    }
    
    public static class Entry
    {
        private final int address;
        private final SourcePosition position;
        
        public Entry(final int address, final SourcePosition position) {
            if (address < 0) {
                throw new IllegalArgumentException("address < 0");
            }
            if (position == null) {
                throw new NullPointerException("position == null");
            }
            this.address = address;
            this.position = position;
        }
        
        public int getAddress() {
            return this.address;
        }
        
        public SourcePosition getPosition() {
            return this.position;
        }
    }
}
