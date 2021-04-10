package com.android.dx.dex.file;

import com.android.dx.util.*;
import java.util.*;

public abstract class Section
{
    private final int alignment;
    private final DexFile file;
    private int fileOffset;
    private final String name;
    private boolean prepared;
    
    public Section(final String name, final DexFile file, final int alignment) {
        if (file == null) {
            throw new NullPointerException("file == null");
        }
        validateAlignment(alignment);
        this.name = name;
        this.file = file;
        this.alignment = alignment;
        this.fileOffset = -1;
        this.prepared = false;
    }
    
    public static void validateAlignment(final int n) {
        if (n > 0 && (n - 1 & n) == 0x0) {
            return;
        }
        throw new IllegalArgumentException("invalid alignment");
    }
    
    protected final void align(final AnnotatedOutput annotatedOutput) {
        annotatedOutput.alignTo(this.alignment);
    }
    
    public abstract int getAbsoluteItemOffset(final Item p0);
    
    public final int getAbsoluteOffset(final int n) {
        if (n < 0) {
            throw new IllegalArgumentException("relative < 0");
        }
        if (this.fileOffset < 0) {
            throw new RuntimeException("fileOffset not yet set");
        }
        return this.fileOffset + n;
    }
    
    public final int getAlignment() {
        return this.alignment;
    }
    
    public final DexFile getFile() {
        return this.file;
    }
    
    public final int getFileOffset() {
        if (this.fileOffset < 0) {
            throw new RuntimeException("fileOffset not set");
        }
        return this.fileOffset;
    }
    
    protected final String getName() {
        return this.name;
    }
    
    public abstract Collection<? extends Item> items();
    
    public final void prepare() {
        this.throwIfPrepared();
        this.prepare0();
        this.prepared = true;
    }
    
    protected abstract void prepare0();
    
    public final int setFileOffset(int fileOffset) {
        if (fileOffset < 0) {
            throw new IllegalArgumentException("fileOffset < 0");
        }
        if (this.fileOffset >= 0) {
            throw new RuntimeException("fileOffset already set");
        }
        final int n = this.alignment - 1;
        fileOffset = (fileOffset + n & ~n);
        return this.fileOffset = fileOffset;
    }
    
    protected final void throwIfNotPrepared() {
        if (!this.prepared) {
            throw new RuntimeException("not prepared");
        }
    }
    
    protected final void throwIfPrepared() {
        if (this.prepared) {
            throw new RuntimeException("already prepared");
        }
    }
    
    public abstract int writeSize();
    
    public final void writeTo(final AnnotatedOutput annotatedOutput) {
        this.throwIfNotPrepared();
        this.align(annotatedOutput);
        final int cursor = annotatedOutput.getCursor();
        if (this.fileOffset < 0) {
            this.fileOffset = cursor;
        }
        else if (this.fileOffset != cursor) {
            final StringBuilder sb = new StringBuilder();
            sb.append("alignment mismatch: for ");
            sb.append(this);
            sb.append(", at ");
            sb.append(cursor);
            sb.append(", but expected ");
            sb.append(this.fileOffset);
            throw new RuntimeException(sb.toString());
        }
        if (annotatedOutput.annotates()) {
            if (this.name != null) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("\n");
                sb2.append(this.name);
                sb2.append(":");
                annotatedOutput.annotate(0, sb2.toString());
            }
            else if (cursor != 0) {
                annotatedOutput.annotate(0, "\n");
            }
        }
        this.writeTo0(annotatedOutput);
    }
    
    protected abstract void writeTo0(final AnnotatedOutput p0);
}
