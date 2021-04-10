package com.android.dx.dex.file;

import java.util.*;
import com.android.dx.util.*;

public final class MapItem extends OffsettedItem
{
    private static final int ALIGNMENT = 4;
    private static final int WRITE_SIZE = 12;
    private final Item firstItem;
    private final int itemCount;
    private final Item lastItem;
    private final Section section;
    private final ItemType type;
    
    private MapItem(final ItemType type, final Section section, final Item firstItem, final Item lastItem, final int itemCount) {
        super(4, 12);
        if (type == null) {
            throw new NullPointerException("type == null");
        }
        if (section == null) {
            throw new NullPointerException("section == null");
        }
        if (firstItem == null) {
            throw new NullPointerException("firstItem == null");
        }
        if (lastItem == null) {
            throw new NullPointerException("lastItem == null");
        }
        if (itemCount <= 0) {
            throw new IllegalArgumentException("itemCount <= 0");
        }
        this.type = type;
        this.section = section;
        this.firstItem = firstItem;
        this.lastItem = lastItem;
        this.itemCount = itemCount;
    }
    
    private MapItem(final Section section) {
        super(4, 12);
        if (section == null) {
            throw new NullPointerException("section == null");
        }
        this.type = ItemType.TYPE_MAP_LIST;
        this.section = section;
        this.firstItem = null;
        this.lastItem = null;
        this.itemCount = 1;
    }
    
    public static void addMap(final Section[] array, final MixedItemSection mixedItemSection) {
        if (array == null) {
            throw new NullPointerException("sections == null");
        }
        if (mixedItemSection.items().size() != 0) {
            throw new IllegalArgumentException("mapSection.items().size() != 0");
        }
        final ArrayList<OffsettedItem> list = new ArrayList<OffsettedItem>(50);
        for (int length = array.length, i = 0; i < length; ++i) {
            final Section section = array[i];
            final Iterator<? extends Item> iterator = section.items().iterator();
            ItemType itemType = null;
            Item item = null;
            Item item2 = null;
            int n = 0;
            while (iterator.hasNext()) {
                final Item item3 = (Item)iterator.next();
                final ItemType itemType2 = item3.itemType();
                if (itemType2 != itemType) {
                    if (n != 0) {
                        list.add(new MapItem(itemType, section, item, item2, n));
                    }
                    itemType = itemType2;
                    item = item3;
                    n = 0;
                }
                ++n;
                item2 = item3;
            }
            if (n != 0) {
                list.add(new MapItem(itemType, section, item, item2, n));
            }
            else if (section == mixedItemSection) {
                list.add(new MapItem(mixedItemSection));
            }
        }
        mixedItemSection.add(new UniformListItem<Object>(ItemType.TYPE_MAP_LIST, list));
    }
    
    @Override
    public void addContents(final DexFile dexFile) {
    }
    
    @Override
    public ItemType itemType() {
        return ItemType.TYPE_MAP_ITEM;
    }
    
    @Override
    public final String toHuman() {
        return this.toString();
    }
    
    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer(100);
        sb.append(this.getClass().getName());
        sb.append('{');
        sb.append(this.section.toString());
        sb.append(' ');
        sb.append(this.type.toHuman());
        sb.append('}');
        return sb.toString();
    }
    
    @Override
    protected void writeTo0(final DexFile dexFile, final AnnotatedOutput annotatedOutput) {
        final int mapValue = this.type.getMapValue();
        int n;
        if (this.firstItem == null) {
            n = this.section.getFileOffset();
        }
        else {
            n = this.section.getAbsoluteItemOffset(this.firstItem);
        }
        if (annotatedOutput.annotates()) {
            final StringBuilder sb = new StringBuilder();
            sb.append(this.offsetString());
            sb.append(' ');
            sb.append(this.type.getTypeName());
            sb.append(" map");
            annotatedOutput.annotate(0, sb.toString());
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("  type:   ");
            sb2.append(Hex.u2(mapValue));
            sb2.append(" // ");
            sb2.append(this.type.toString());
            annotatedOutput.annotate(2, sb2.toString());
            annotatedOutput.annotate(2, "  unused: 0");
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("  size:   ");
            sb3.append(Hex.u4(this.itemCount));
            annotatedOutput.annotate(4, sb3.toString());
            final StringBuilder sb4 = new StringBuilder();
            sb4.append("  offset: ");
            sb4.append(Hex.u4(n));
            annotatedOutput.annotate(4, sb4.toString());
        }
        annotatedOutput.writeShort(mapValue);
        annotatedOutput.writeShort(0);
        annotatedOutput.writeInt(this.itemCount);
        annotatedOutput.writeInt(n);
    }
}
