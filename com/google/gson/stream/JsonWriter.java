package com.google.gson.stream;

import java.io.*;

public class JsonWriter implements Closeable, Flushable
{
    private static final String[] HTML_SAFE_REPLACEMENT_CHARS;
    private static final String[] REPLACEMENT_CHARS;
    private String deferredName;
    private boolean htmlSafe;
    private String indent;
    private boolean lenient;
    private final Writer out;
    private String separator;
    private boolean serializeNulls;
    private int[] stack;
    private int stackSize;
    
    static {
        REPLACEMENT_CHARS = new String[128];
        for (int i = 0; i <= 31; ++i) {
            JsonWriter.REPLACEMENT_CHARS[i] = String.format("\\u%04x", i);
        }
        final String[] replacement_CHARS = JsonWriter.REPLACEMENT_CHARS;
        replacement_CHARS[34] = "\\\"";
        replacement_CHARS[92] = "\\\\";
        replacement_CHARS[9] = "\\t";
        replacement_CHARS[8] = "\\b";
        replacement_CHARS[10] = "\\n";
        replacement_CHARS[13] = "\\r";
        replacement_CHARS[12] = "\\f";
        final String[] html_SAFE_REPLACEMENT_CHARS = replacement_CHARS.clone();
        (HTML_SAFE_REPLACEMENT_CHARS = html_SAFE_REPLACEMENT_CHARS)[60] = "\\u003c";
        html_SAFE_REPLACEMENT_CHARS[62] = "\\u003e";
        html_SAFE_REPLACEMENT_CHARS[38] = "\\u0026";
        html_SAFE_REPLACEMENT_CHARS[61] = "\\u003d";
        html_SAFE_REPLACEMENT_CHARS[39] = "\\u0027";
    }
    
    public JsonWriter(final Writer out) {
        this.stack = new int[32];
        this.stackSize = 0;
        this.push(6);
        this.separator = ":";
        this.serializeNulls = true;
        if (out != null) {
            this.out = out;
            return;
        }
        throw new NullPointerException("out == null");
    }
    
    private void beforeName() throws IOException {
        final int peek = this.peek();
        if (peek == 5) {
            this.out.write(44);
        }
        else if (peek != 3) {
            throw new IllegalStateException("Nesting problem.");
        }
        this.newline();
        this.replaceTop(4);
    }
    
    private void beforeValue(final boolean b) throws IOException {
        final int peek = this.peek();
        if (peek == 1) {
            this.replaceTop(2);
            this.newline();
            return;
        }
        if (peek == 2) {
            this.out.append(',');
            this.newline();
            return;
        }
        if (peek == 4) {
            this.out.append((CharSequence)this.separator);
            this.replaceTop(5);
            return;
        }
        if (peek != 6) {
            if (peek != 7) {
                throw new IllegalStateException("Nesting problem.");
            }
            if (!this.lenient) {
                throw new IllegalStateException("JSON must have only one top-level value.");
            }
        }
        if (!this.lenient && !b) {
            throw new IllegalStateException("JSON must start with an array or an object.");
        }
        this.replaceTop(7);
    }
    
    private JsonWriter close(final int n, final int n2, final String s) throws IOException {
        final int peek = this.peek();
        if (peek != n2 && peek != n) {
            throw new IllegalStateException("Nesting problem.");
        }
        if (this.deferredName == null) {
            --this.stackSize;
            if (peek == n2) {
                this.newline();
            }
            this.out.write(s);
            return this;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Dangling name: ");
        sb.append(this.deferredName);
        throw new IllegalStateException(sb.toString());
    }
    
    private void newline() throws IOException {
        if (this.indent == null) {
            return;
        }
        this.out.write("\n");
        for (int stackSize = this.stackSize, i = 1; i < stackSize; ++i) {
            this.out.write(this.indent);
        }
    }
    
    private JsonWriter open(final int n, final String s) throws IOException {
        this.beforeValue(true);
        this.push(n);
        this.out.write(s);
        return this;
    }
    
    private int peek() {
        final int stackSize = this.stackSize;
        if (stackSize != 0) {
            return this.stack[stackSize - 1];
        }
        throw new IllegalStateException("JsonWriter is closed.");
    }
    
    private void push(final int n) {
        final int stackSize = this.stackSize;
        final int[] stack = this.stack;
        if (stackSize == stack.length) {
            final int[] stack2 = new int[stackSize * 2];
            System.arraycopy(stack, 0, stack2, 0, stackSize);
            this.stack = stack2;
        }
        this.stack[this.stackSize++] = n;
    }
    
    private void replaceTop(final int n) {
        this.stack[this.stackSize - 1] = n;
    }
    
    private void string(final String s) throws IOException {
        String[] array;
        if (this.htmlSafe) {
            array = JsonWriter.HTML_SAFE_REPLACEMENT_CHARS;
        }
        else {
            array = JsonWriter.REPLACEMENT_CHARS;
        }
        this.out.write("\"");
        final int length = s.length();
        int i = 0;
        int n = 0;
        while (i < length) {
            final char char1 = s.charAt(i);
            int n2 = 0;
            Label_0143: {
                String s2;
                if (char1 < '\u0080') {
                    if ((s2 = array[char1]) == null) {
                        n2 = n;
                        break Label_0143;
                    }
                }
                else if (char1 == '\u2028') {
                    s2 = "\\u2028";
                }
                else {
                    n2 = n;
                    if (char1 != '\u2029') {
                        break Label_0143;
                    }
                    s2 = "\\u2029";
                }
                if (n < i) {
                    this.out.write(s, n, i - n);
                }
                this.out.write(s2);
                n2 = i + 1;
            }
            ++i;
            n = n2;
        }
        if (n < length) {
            this.out.write(s, n, length - n);
        }
        this.out.write("\"");
    }
    
    private void writeDeferredName() throws IOException {
        if (this.deferredName != null) {
            this.beforeName();
            this.string(this.deferredName);
            this.deferredName = null;
        }
    }
    
    public JsonWriter beginArray() throws IOException {
        this.writeDeferredName();
        return this.open(1, "[");
    }
    
    public JsonWriter beginObject() throws IOException {
        this.writeDeferredName();
        return this.open(3, "{");
    }
    
    @Override
    public void close() throws IOException {
        this.out.close();
        final int stackSize = this.stackSize;
        if (stackSize <= 1 && (stackSize != 1 || this.stack[stackSize - 1] == 7)) {
            this.stackSize = 0;
            return;
        }
        throw new IOException("Incomplete document");
    }
    
    public JsonWriter endArray() throws IOException {
        return this.close(1, 2, "]");
    }
    
    public JsonWriter endObject() throws IOException {
        return this.close(3, 5, "}");
    }
    
    @Override
    public void flush() throws IOException {
        if (this.stackSize != 0) {
            this.out.flush();
            return;
        }
        throw new IllegalStateException("JsonWriter is closed.");
    }
    
    public final boolean getSerializeNulls() {
        return this.serializeNulls;
    }
    
    public boolean isLenient() {
        return this.lenient;
    }
    
    public JsonWriter name(final String deferredName) throws IOException {
        if (deferredName == null) {
            throw new NullPointerException("name == null");
        }
        if (this.deferredName != null) {
            throw new IllegalStateException();
        }
        if (this.stackSize != 0) {
            this.deferredName = deferredName;
            return this;
        }
        throw new IllegalStateException("JsonWriter is closed.");
    }
    
    public JsonWriter nullValue() throws IOException {
        if (this.deferredName != null) {
            if (!this.serializeNulls) {
                this.deferredName = null;
                return this;
            }
            this.writeDeferredName();
        }
        this.beforeValue(false);
        this.out.write("null");
        return this;
    }
    
    public final void setLenient(final boolean lenient) {
        this.lenient = lenient;
    }
    
    public JsonWriter value(final long n) throws IOException {
        this.writeDeferredName();
        this.beforeValue(false);
        this.out.write(Long.toString(n));
        return this;
    }
    
    public JsonWriter value(final Number n) throws IOException {
        if (n == null) {
            return this.nullValue();
        }
        this.writeDeferredName();
        final String string = n.toString();
        if (!this.lenient && (string.equals("-Infinity") || string.equals("Infinity") || string.equals("NaN"))) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Numeric values must be finite, but was ");
            sb.append(n);
            throw new IllegalArgumentException(sb.toString());
        }
        this.beforeValue(false);
        this.out.append((CharSequence)string);
        return this;
    }
    
    public JsonWriter value(final String s) throws IOException {
        if (s == null) {
            return this.nullValue();
        }
        this.writeDeferredName();
        this.beforeValue(false);
        this.string(s);
        return this;
    }
    
    public JsonWriter value(final boolean b) throws IOException {
        this.writeDeferredName();
        this.beforeValue(false);
        final Writer out = this.out;
        String s;
        if (b) {
            s = "true";
        }
        else {
            s = "false";
        }
        out.write(s);
        return this;
    }
}
