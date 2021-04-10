package com.android.dx.util;

import java.util.*;
import com.android.dex.util.*;
import java.io.*;
import com.android.dex.*;

public final class ByteArrayAnnotatedOutput implements AnnotatedOutput, ByteOutput
{
    private static final int DEFAULT_SIZE = 1000;
    private int annotationWidth;
    private ArrayList<Annotation> annotations;
    private int cursor;
    private byte[] data;
    private int hexCols;
    private final boolean stretchy;
    private boolean verbose;
    
    public ByteArrayAnnotatedOutput() {
        this(1000);
    }
    
    public ByteArrayAnnotatedOutput(final int n) {
        this(new byte[n], true);
    }
    
    public ByteArrayAnnotatedOutput(final byte[] array) {
        this(array, false);
    }
    
    private ByteArrayAnnotatedOutput(final byte[] data, final boolean stretchy) {
        if (data == null) {
            throw new NullPointerException("data == null");
        }
        this.stretchy = stretchy;
        this.data = data;
        this.cursor = 0;
        this.verbose = false;
        this.annotations = null;
        this.annotationWidth = 0;
        this.hexCols = 0;
    }
    
    private void ensureCapacity(final int n) {
        if (this.data.length < n) {
            final byte[] data = new byte[n * 2 + 1000];
            System.arraycopy(this.data, 0, data, 0, this.cursor);
            this.data = data;
        }
    }
    
    private static void throwBounds() {
        throw new IndexOutOfBoundsException("attempt to write past the end");
    }
    
    @Override
    public void alignTo(int cursor) {
        final int n = cursor - 1;
        if (cursor >= 0 && (n & cursor) == 0x0) {
            cursor = (this.cursor + n & ~n);
            if (this.stretchy) {
                this.ensureCapacity(cursor);
            }
            else if (cursor > this.data.length) {
                throwBounds();
                return;
            }
            this.cursor = cursor;
            return;
        }
        throw new IllegalArgumentException("bogus alignment");
    }
    
    @Override
    public void annotate(final int n, final String s) {
        if (this.annotations == null) {
            return;
        }
        this.endAnnotation();
        final int size = this.annotations.size();
        int n2;
        if (size == 0) {
            n2 = 0;
        }
        else {
            n2 = this.annotations.get(size - 1).getEnd();
        }
        if (n2 <= this.cursor) {
            n2 = this.cursor;
        }
        this.annotations.add(new Annotation(n2, n2 + n, s));
    }
    
    @Override
    public void annotate(final String s) {
        if (this.annotations == null) {
            return;
        }
        this.endAnnotation();
        this.annotations.add(new Annotation(this.cursor, s));
    }
    
    @Override
    public boolean annotates() {
        return this.annotations != null;
    }
    
    @Override
    public void assertCursor(final int n) {
        if (this.cursor != n) {
            final StringBuilder sb = new StringBuilder();
            sb.append("expected cursor ");
            sb.append(n);
            sb.append("; actual value: ");
            sb.append(this.cursor);
            throw new ExceptionWithContext(sb.toString());
        }
    }
    
    public void enableAnnotations(final int annotationWidth, final boolean verbose) {
        if (this.annotations != null || this.cursor != 0) {
            throw new RuntimeException("cannot enable annotations");
        }
        if (annotationWidth < 40) {
            throw new IllegalArgumentException("annotationWidth < 40");
        }
        final int n = (annotationWidth - 7) / 15 + 1 & 0xFFFFFFFE;
        int hexCols;
        if (n < 6) {
            hexCols = 6;
        }
        else if ((hexCols = n) > 10) {
            hexCols = 10;
        }
        this.annotations = new ArrayList<Annotation>(1000);
        this.annotationWidth = annotationWidth;
        this.hexCols = hexCols;
        this.verbose = verbose;
    }
    
    @Override
    public void endAnnotation() {
        if (this.annotations == null) {
            return;
        }
        final int size = this.annotations.size();
        if (size != 0) {
            this.annotations.get(size - 1).setEndIfUnset(this.cursor);
        }
    }
    
    public void finishAnnotating() {
        this.endAnnotation();
        if (this.annotations != null) {
            int i = this.annotations.size();
            while (i > 0) {
                final Annotation annotation = this.annotations.get(i - 1);
                if (annotation.getStart() > this.cursor) {
                    this.annotations.remove(i - 1);
                    --i;
                }
                else {
                    if (annotation.getEnd() > this.cursor) {
                        annotation.setEnd(this.cursor);
                        break;
                    }
                    break;
                }
            }
        }
    }
    
    @Override
    public int getAnnotationWidth() {
        return this.annotationWidth - (this.hexCols * 2 + 8 + this.hexCols / 2);
    }
    
    public byte[] getArray() {
        return this.data;
    }
    
    @Override
    public int getCursor() {
        return this.cursor;
    }
    
    @Override
    public boolean isVerbose() {
        return this.verbose;
    }
    
    public byte[] toByteArray() {
        final byte[] array = new byte[this.cursor];
        System.arraycopy(this.data, 0, array, 0, this.cursor);
        return array;
    }
    
    @Override
    public void write(final ByteArray byteArray) {
        final int size = byteArray.size();
        final int cursor = this.cursor;
        final int cursor2 = cursor + size;
        if (this.stretchy) {
            this.ensureCapacity(cursor2);
        }
        else if (cursor2 > this.data.length) {
            throwBounds();
            return;
        }
        byteArray.getBytes(this.data, cursor);
        this.cursor = cursor2;
    }
    
    @Override
    public void write(final byte[] array) {
        this.write(array, 0, array.length);
    }
    
    @Override
    public void write(final byte[] array, final int n, final int n2) {
        final int cursor = this.cursor;
        final int cursor2 = cursor + n2;
        if ((n | n2 | cursor2) >= 0 && n + n2 <= array.length) {
            if (this.stretchy) {
                this.ensureCapacity(cursor2);
            }
            else if (cursor2 > this.data.length) {
                throwBounds();
                return;
            }
            System.arraycopy(array, n, this.data, cursor, n2);
            this.cursor = cursor2;
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("bytes.length ");
        sb.append(array.length);
        sb.append("; ");
        sb.append(n);
        sb.append("..!");
        sb.append(cursor2);
        throw new IndexOutOfBoundsException(sb.toString());
    }
    
    public void writeAnnotationsTo(final Writer writer) throws IOException {
        final int annotationWidth = this.getAnnotationWidth();
        final TwoColumnOutput twoColumnOutput = new TwoColumnOutput(writer, this.annotationWidth - annotationWidth - 1, annotationWidth, "|");
        final Writer left = twoColumnOutput.getLeft();
        final Writer right = twoColumnOutput.getRight();
        int n;
        int n2;
        int size;
        int n4;
        int n5;
        for (n = 0, n2 = 0, size = this.annotations.size(); n < this.cursor && n2 < size; n = n5, n2 = n4) {
            final Annotation annotation = this.annotations.get(n2);
            final int start = annotation.getStart();
            String text;
            if (n < start) {
                final int n3 = start;
                text = "";
                n4 = n2;
                n5 = n3;
            }
            else {
                final int end = annotation.getEnd();
                text = annotation.getText();
                final int n6 = n2 + 1;
                n5 = end;
                n = start;
                n4 = n6;
            }
            left.write(Hex.dump(this.data, n, n5 - n, n, this.hexCols, 6));
            right.write(text);
            twoColumnOutput.flush();
        }
        int i = n2;
        if (n < this.cursor) {
            left.write(Hex.dump(this.data, n, this.cursor - n, n, this.hexCols, 6));
            i = n2;
        }
        while (i < size) {
            right.write(this.annotations.get(i).getText());
            ++i;
        }
        twoColumnOutput.flush();
    }
    
    @Override
    public void writeByte(final int n) {
        final int cursor = this.cursor;
        final int cursor2 = cursor + 1;
        if (this.stretchy) {
            this.ensureCapacity(cursor2);
        }
        else if (cursor2 > this.data.length) {
            throwBounds();
            return;
        }
        this.data[cursor] = (byte)n;
        this.cursor = cursor2;
    }
    
    @Override
    public void writeInt(final int n) {
        final int cursor = this.cursor;
        final int cursor2 = cursor + 4;
        if (this.stretchy) {
            this.ensureCapacity(cursor2);
        }
        else if (cursor2 > this.data.length) {
            throwBounds();
            return;
        }
        this.data[cursor] = (byte)n;
        this.data[cursor + 1] = (byte)(n >> 8);
        this.data[cursor + 2] = (byte)(n >> 16);
        this.data[cursor + 3] = (byte)(n >> 24);
        this.cursor = cursor2;
    }
    
    @Override
    public void writeLong(final long n) {
        final int cursor = this.cursor;
        final int cursor2 = cursor + 8;
        if (this.stretchy) {
            this.ensureCapacity(cursor2);
        }
        else if (cursor2 > this.data.length) {
            throwBounds();
            return;
        }
        final int n2 = (int)n;
        this.data[cursor] = (byte)n2;
        this.data[cursor + 1] = (byte)(n2 >> 8);
        this.data[cursor + 2] = (byte)(n2 >> 16);
        this.data[cursor + 3] = (byte)(n2 >> 24);
        final int n3 = (int)(n >> 32);
        this.data[cursor + 4] = (byte)n3;
        this.data[cursor + 5] = (byte)(n3 >> 8);
        this.data[cursor + 6] = (byte)(n3 >> 16);
        this.data[cursor + 7] = (byte)(n3 >> 24);
        this.cursor = cursor2;
    }
    
    @Override
    public void writeShort(final int n) {
        final int cursor = this.cursor;
        final int cursor2 = cursor + 2;
        if (this.stretchy) {
            this.ensureCapacity(cursor2);
        }
        else if (cursor2 > this.data.length) {
            throwBounds();
            return;
        }
        this.data[cursor] = (byte)n;
        this.data[cursor + 1] = (byte)(n >> 8);
        this.cursor = cursor2;
    }
    
    @Override
    public int writeSleb128(final int n) {
        if (this.stretchy) {
            this.ensureCapacity(this.cursor + 5);
        }
        final int cursor = this.cursor;
        Leb128.writeSignedLeb128(this, n);
        return this.cursor - cursor;
    }
    
    @Override
    public int writeUleb128(final int n) {
        if (this.stretchy) {
            this.ensureCapacity(this.cursor + 5);
        }
        final int cursor = this.cursor;
        Leb128.writeUnsignedLeb128(this, n);
        return this.cursor - cursor;
    }
    
    @Override
    public void writeZeroes(int cursor) {
        if (cursor < 0) {
            throw new IllegalArgumentException("count < 0");
        }
        cursor += this.cursor;
        if (this.stretchy) {
            this.ensureCapacity(cursor);
        }
        else if (cursor > this.data.length) {
            throwBounds();
            return;
        }
        this.cursor = cursor;
    }
    
    private static class Annotation
    {
        private int end;
        private final int start;
        private final String text;
        
        public Annotation(final int start, final int end, final String text) {
            this.start = start;
            this.end = end;
            this.text = text;
        }
        
        public Annotation(final int n, final String s) {
            this(n, Integer.MAX_VALUE, s);
        }
        
        public int getEnd() {
            return this.end;
        }
        
        public int getStart() {
            return this.start;
        }
        
        public String getText() {
            return this.text;
        }
        
        public void setEnd(final int end) {
            this.end = end;
        }
        
        public void setEndIfUnset(final int end) {
            if (this.end == Integer.MAX_VALUE) {
                this.end = end;
            }
        }
    }
}
