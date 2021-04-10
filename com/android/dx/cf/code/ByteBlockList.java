package com.android.dx.cf.code;

import com.android.dx.util.*;

public final class ByteBlockList extends LabeledList
{
    public ByteBlockList(final int n) {
        super(n);
    }
    
    public ByteBlock get(final int n) {
        return (ByteBlock)this.get0(n);
    }
    
    public ByteBlock labelToBlock(final int n) {
        final int indexOfLabel = this.indexOfLabel(n);
        if (indexOfLabel < 0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("no such label: ");
            sb.append(Hex.u2(n));
            throw new IllegalArgumentException(sb.toString());
        }
        return this.get(indexOfLabel);
    }
    
    public void set(final int n, final ByteBlock byteBlock) {
        super.set(n, byteBlock);
    }
}
