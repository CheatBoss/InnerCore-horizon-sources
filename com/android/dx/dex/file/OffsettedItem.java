package com.android.dx.dex.file;

import com.android.dx.util.*;
import com.android.dex.util.*;

public abstract class OffsettedItem extends Item implements Comparable<OffsettedItem>
{
    private Section addedTo;
    private final int alignment;
    private int offset;
    private int writeSize;
    
    public OffsettedItem(final int alignment, final int writeSize) {
        Section.validateAlignment(alignment);
        if (writeSize < -1) {
            throw new IllegalArgumentException("writeSize < -1");
        }
        this.alignment = alignment;
        this.writeSize = writeSize;
        this.addedTo = null;
        this.offset = -1;
    }
    
    public static int getAbsoluteOffsetOr0(final OffsettedItem offsettedItem) {
        if (offsettedItem == null) {
            return 0;
        }
        return offsettedItem.getAbsoluteOffset();
    }
    
    @Override
    public final int compareTo(final OffsettedItem offsettedItem) {
        if (this == offsettedItem) {
            return 0;
        }
        final ItemType itemType = this.itemType();
        final ItemType itemType2 = offsettedItem.itemType();
        if (itemType != itemType2) {
            return itemType.compareTo(itemType2);
        }
        return this.compareTo0(offsettedItem);
    }
    
    protected int compareTo0(final OffsettedItem offsettedItem) {
        throw new UnsupportedOperationException("unsupported");
    }
    
    @Override
    public final boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        final OffsettedItem offsettedItem = (OffsettedItem)o;
        return this.itemType() == offsettedItem.itemType() && this.compareTo0(offsettedItem) == 0;
    }
    
    public final int getAbsoluteOffset() {
        if (this.offset < 0) {
            throw new RuntimeException("offset not yet known");
        }
        return this.addedTo.getAbsoluteOffset(this.offset);
    }
    
    public final int getAlignment() {
        return this.alignment;
    }
    
    public final int getRelativeOffset() {
        if (this.offset < 0) {
            throw new RuntimeException("offset not yet known");
        }
        return this.offset;
    }
    
    public final String offsetString() {
        final StringBuilder sb = new StringBuilder();
        sb.append('[');
        sb.append(Integer.toHexString(this.getAbsoluteOffset()));
        sb.append(']');
        return sb.toString();
    }
    
    public final int place(final Section addedTo, int offset) {
        if (addedTo == null) {
            throw new NullPointerException("addedTo == null");
        }
        if (offset < 0) {
            throw new IllegalArgumentException("offset < 0");
        }
        if (this.addedTo != null) {
            throw new RuntimeException("already written");
        }
        final int n = this.alignment - 1;
        offset = (offset + n & ~n);
        this.place0(this.addedTo = addedTo, this.offset = offset);
        return offset;
    }
    
    protected void place0(final Section section, final int n) {
    }
    
    public final void setWriteSize(final int writeSize) {
        if (writeSize < 0) {
            throw new IllegalArgumentException("writeSize < 0");
        }
        if (this.writeSize >= 0) {
            throw new UnsupportedOperationException("writeSize already set");
        }
        this.writeSize = writeSize;
    }
    
    public abstract String toHuman();
    
    @Override
    public final int writeSize() {
        if (this.writeSize < 0) {
            throw new UnsupportedOperationException("writeSize is unknown");
        }
        return this.writeSize;
    }
    
    @Override
    public final void writeTo(final DexFile dexFile, final AnnotatedOutput annotatedOutput) {
        annotatedOutput.alignTo(this.alignment);
        try {
            if (this.writeSize < 0) {
                throw new UnsupportedOperationException("writeSize is unknown");
            }
            annotatedOutput.assertCursor(this.getAbsoluteOffset());
            this.writeTo0(dexFile, annotatedOutput);
        }
        catch (RuntimeException ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("...while writing ");
            sb.append(this);
            throw ExceptionWithContext.withContext(ex, sb.toString());
        }
    }
    
    protected abstract void writeTo0(final DexFile p0, final AnnotatedOutput p1);
}
