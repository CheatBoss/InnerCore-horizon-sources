package com.android.dx.dex.file;

import java.util.*;
import com.android.dx.util.*;

public final class UniformListItem<T extends OffsettedItem> extends OffsettedItem
{
    private static final int HEADER_SIZE = 4;
    private final ItemType itemType;
    private final List<T> items;
    
    public UniformListItem(final ItemType itemType, final List<T> items) {
        super(getAlignment(items), writeSize(items));
        if (itemType == null) {
            throw new NullPointerException("itemType == null");
        }
        this.items = items;
        this.itemType = itemType;
    }
    
    private static int getAlignment(final List<? extends OffsettedItem> list) {
        try {
            return Math.max(4, ((OffsettedItem)list.get(0)).getAlignment());
        }
        catch (NullPointerException ex) {
            throw new NullPointerException("items == null");
        }
        catch (IndexOutOfBoundsException ex2) {
            throw new IllegalArgumentException("items.size() == 0");
        }
    }
    
    private int headerSize() {
        return this.getAlignment();
    }
    
    private static int writeSize(final List<? extends OffsettedItem> list) {
        return list.size() * ((OffsettedItem)list.get(0)).writeSize() + getAlignment(list);
    }
    
    @Override
    public void addContents(final DexFile dexFile) {
        final Iterator<T> iterator = this.items.iterator();
        while (iterator.hasNext()) {
            iterator.next().addContents(dexFile);
        }
    }
    
    public final List<T> getItems() {
        return this.items;
    }
    
    @Override
    public ItemType itemType() {
        return this.itemType;
    }
    
    @Override
    protected void place0(final Section section, int n) {
        n += this.headerSize();
        int n2 = 1;
        int n3 = -1;
        int n4 = -1;
        for (final OffsettedItem offsettedItem : this.items) {
            final int writeSize = offsettedItem.writeSize();
            int alignment;
            if (n2 != 0) {
                n3 = writeSize;
                alignment = offsettedItem.getAlignment();
                n2 = 0;
            }
            else {
                if (writeSize != n3) {
                    throw new UnsupportedOperationException("item size mismatch");
                }
                if (offsettedItem.getAlignment() != (alignment = n4)) {
                    throw new UnsupportedOperationException("item alignment mismatch");
                }
            }
            n = offsettedItem.place(section, n) + writeSize;
            n4 = alignment;
        }
    }
    
    @Override
    public final String toHuman() {
        final StringBuffer sb = new StringBuffer(100);
        int n = 1;
        sb.append("{");
        for (final OffsettedItem offsettedItem : this.items) {
            if (n != 0) {
                n = 0;
            }
            else {
                sb.append(", ");
            }
            sb.append(offsettedItem.toHuman());
        }
        sb.append("}");
        return sb.toString();
    }
    
    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer(100);
        sb.append(this.getClass().getName());
        sb.append(this.items);
        return sb.toString();
    }
    
    @Override
    protected void writeTo0(final DexFile dexFile, final AnnotatedOutput annotatedOutput) {
        final int size = this.items.size();
        if (annotatedOutput.annotates()) {
            final StringBuilder sb = new StringBuilder();
            sb.append(this.offsetString());
            sb.append(" ");
            sb.append(this.typeName());
            annotatedOutput.annotate(0, sb.toString());
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("  size: ");
            sb2.append(Hex.u4(size));
            annotatedOutput.annotate(4, sb2.toString());
        }
        annotatedOutput.writeInt(size);
        final Iterator<T> iterator = this.items.iterator();
        while (iterator.hasNext()) {
            iterator.next().writeTo(dexFile, annotatedOutput);
        }
    }
}
