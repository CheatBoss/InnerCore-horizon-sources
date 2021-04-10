package com.android.dx.dex.file;

import com.android.dx.rop.annotation.*;
import java.util.*;
import com.android.dx.util.*;

public final class AnnotationSetItem extends OffsettedItem
{
    private static final int ALIGNMENT = 4;
    private static final int ENTRY_WRITE_SIZE = 4;
    private final Annotations annotations;
    private final AnnotationItem[] items;
    
    public AnnotationSetItem(final Annotations annotations, final DexFile dexFile) {
        super(4, writeSize(annotations));
        this.annotations = annotations;
        this.items = new AnnotationItem[annotations.size()];
        int n = 0;
        final Iterator<Annotation> iterator = annotations.getAnnotations().iterator();
        while (iterator.hasNext()) {
            this.items[n] = new AnnotationItem(iterator.next(), dexFile);
            ++n;
        }
    }
    
    private static int writeSize(final Annotations annotations) {
        try {
            return annotations.size() * 4 + 4;
        }
        catch (NullPointerException ex) {
            throw new NullPointerException("list == null");
        }
    }
    
    @Override
    public void addContents(final DexFile dexFile) {
        final MixedItemSection byteData = dexFile.getByteData();
        for (int length = this.items.length, i = 0; i < length; ++i) {
            this.items[i] = byteData.intern(this.items[i]);
        }
    }
    
    @Override
    protected int compareTo0(final OffsettedItem offsettedItem) {
        return this.annotations.compareTo(((AnnotationSetItem)offsettedItem).annotations);
    }
    
    public Annotations getAnnotations() {
        return this.annotations;
    }
    
    @Override
    public int hashCode() {
        return this.annotations.hashCode();
    }
    
    @Override
    public ItemType itemType() {
        return ItemType.TYPE_ANNOTATION_SET_ITEM;
    }
    
    @Override
    protected void place0(final Section section, final int n) {
        AnnotationItem.sortByTypeIdIndex(this.items);
    }
    
    @Override
    public String toHuman() {
        return this.annotations.toString();
    }
    
    @Override
    protected void writeTo0(final DexFile dexFile, final AnnotatedOutput annotatedOutput) {
        final boolean annotates = annotatedOutput.annotates();
        final int length = this.items.length;
        int i = 0;
        if (annotates) {
            final StringBuilder sb = new StringBuilder();
            sb.append(this.offsetString());
            sb.append(" annotation set");
            annotatedOutput.annotate(0, sb.toString());
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("  size: ");
            sb2.append(Hex.u4(length));
            annotatedOutput.annotate(4, sb2.toString());
        }
        annotatedOutput.writeInt(length);
        while (i < length) {
            final int absoluteOffset = this.items[i].getAbsoluteOffset();
            if (annotates) {
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("  entries[");
                sb3.append(Integer.toHexString(i));
                sb3.append("]: ");
                sb3.append(Hex.u4(absoluteOffset));
                annotatedOutput.annotate(4, sb3.toString());
                this.items[i].annotateTo(annotatedOutput, "    ");
            }
            annotatedOutput.writeInt(absoluteOffset);
            ++i;
        }
    }
}
