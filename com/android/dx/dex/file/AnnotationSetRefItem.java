package com.android.dx.dex.file;

import com.android.dx.util.*;

public final class AnnotationSetRefItem extends OffsettedItem
{
    private static final int ALIGNMENT = 4;
    private static final int WRITE_SIZE = 4;
    private AnnotationSetItem annotations;
    
    public AnnotationSetRefItem(final AnnotationSetItem annotations) {
        super(4, 4);
        if (annotations == null) {
            throw new NullPointerException("annotations == null");
        }
        this.annotations = annotations;
    }
    
    @Override
    public void addContents(final DexFile dexFile) {
        this.annotations = dexFile.getWordData().intern(this.annotations);
    }
    
    @Override
    public ItemType itemType() {
        return ItemType.TYPE_ANNOTATION_SET_REF_ITEM;
    }
    
    @Override
    public String toHuman() {
        return this.annotations.toHuman();
    }
    
    @Override
    protected void writeTo0(final DexFile dexFile, final AnnotatedOutput annotatedOutput) {
        final int absoluteOffset = this.annotations.getAbsoluteOffset();
        if (annotatedOutput.annotates()) {
            final StringBuilder sb = new StringBuilder();
            sb.append("  annotations_off: ");
            sb.append(Hex.u4(absoluteOffset));
            annotatedOutput.annotate(4, sb.toString());
        }
        annotatedOutput.writeInt(absoluteOffset);
    }
}
