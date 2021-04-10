package com.android.dx.dex.file;

import com.android.dx.rop.cst.*;
import com.android.dx.util.*;

public final class EncodedArrayItem extends OffsettedItem
{
    private static final int ALIGNMENT = 1;
    private final CstArray array;
    private byte[] encodedForm;
    
    public EncodedArrayItem(final CstArray array) {
        super(1, -1);
        if (array == null) {
            throw new NullPointerException("array == null");
        }
        this.array = array;
        this.encodedForm = null;
    }
    
    @Override
    public void addContents(final DexFile dexFile) {
        ValueEncoder.addContents(dexFile, this.array);
    }
    
    @Override
    protected int compareTo0(final OffsettedItem offsettedItem) {
        return this.array.compareTo((Constant)((EncodedArrayItem)offsettedItem).array);
    }
    
    @Override
    public int hashCode() {
        return this.array.hashCode();
    }
    
    @Override
    public ItemType itemType() {
        return ItemType.TYPE_ENCODED_ARRAY_ITEM;
    }
    
    @Override
    protected void place0(final Section section, final int n) {
        final ByteArrayAnnotatedOutput byteArrayAnnotatedOutput = new ByteArrayAnnotatedOutput();
        new ValueEncoder(section.getFile(), byteArrayAnnotatedOutput).writeArray(this.array, false);
        this.encodedForm = byteArrayAnnotatedOutput.toByteArray();
        this.setWriteSize(this.encodedForm.length);
    }
    
    @Override
    public String toHuman() {
        return this.array.toHuman();
    }
    
    @Override
    protected void writeTo0(final DexFile dexFile, final AnnotatedOutput annotatedOutput) {
        if (annotatedOutput.annotates()) {
            final StringBuilder sb = new StringBuilder();
            sb.append(this.offsetString());
            sb.append(" encoded array");
            annotatedOutput.annotate(0, sb.toString());
            new ValueEncoder(dexFile, annotatedOutput).writeArray(this.array, true);
            return;
        }
        annotatedOutput.write(this.encodedForm);
    }
}
