package org.mozilla.javascript.tools.idswitch;

class CodePrinter
{
    private static final int LITERAL_CHAR_MAX_SIZE = 6;
    private char[] buffer;
    private int indentStep;
    private int indentTabSize;
    private String lineTerminator;
    private int offset;
    
    CodePrinter() {
        this.lineTerminator = "\n";
        this.indentStep = 4;
        this.indentTabSize = 8;
        this.buffer = new char[4096];
    }
    
    private int add_area(final int n) {
        final int ensure_area = this.ensure_area(n);
        this.offset = ensure_area + n;
        return ensure_area;
    }
    
    private static char digit_to_hex_letter(int n) {
        if (n < 10) {
            n += 48;
        }
        else {
            n += 55;
        }
        return (char)n;
    }
    
    private int ensure_area(int n) {
        final int offset = this.offset;
        final int n2 = offset + n;
        if (n2 > this.buffer.length) {
            if (n2 > (n = this.buffer.length * 2)) {
                n = n2;
            }
            final char[] buffer = new char[n];
            System.arraycopy(this.buffer, 0, buffer, 0, offset);
            this.buffer = buffer;
        }
        return offset;
    }
    
    private int put_string_literal_char(final int n, int n2, boolean b) {
        final boolean b2 = true;
        Label_0125: {
            if (n2 != 34) {
                if (n2 != 39) {
                    switch (n2) {
                        default: {
                            switch (n2) {
                                default: {
                                    b = false;
                                    break Label_0125;
                                }
                                case 13: {
                                    n2 = 114;
                                    b = b2;
                                    break Label_0125;
                                }
                                case 12: {
                                    n2 = 102;
                                    b = b2;
                                    break Label_0125;
                                }
                            }
                            break;
                        }
                        case 10: {
                            n2 = 110;
                            b = b2;
                            break;
                        }
                        case 9: {
                            n2 = 116;
                            b = b2;
                            break;
                        }
                        case 8: {
                            n2 = 98;
                            b = b2;
                            break;
                        }
                    }
                }
                else {
                    b ^= true;
                }
            }
        }
        if (b) {
            this.buffer[n] = '\\';
            this.buffer[n + 1] = (char)n2;
            return n + 2;
        }
        if (32 <= n2 && n2 <= 126) {
            this.buffer[n] = (char)n2;
            return n + 1;
        }
        this.buffer[n] = '\\';
        this.buffer[n + 1] = 'u';
        this.buffer[n + 2] = digit_to_hex_letter(n2 >> 12 & 0xF);
        this.buffer[n + 3] = digit_to_hex_letter(n2 >> 8 & 0xF);
        this.buffer[n + 4] = digit_to_hex_letter(n2 >> 4 & 0xF);
        this.buffer[n + 5] = digit_to_hex_letter(n2 & 0xF);
        return n + 6;
    }
    
    public void clear() {
        this.offset = 0;
    }
    
    public void erase(final int n, final int n2) {
        System.arraycopy(this.buffer, n2, this.buffer, n, this.offset - n2);
        this.offset -= n2 - n;
    }
    
    public int getIndentStep() {
        return this.indentStep;
    }
    
    public int getIndentTabSize() {
        return this.indentTabSize;
    }
    
    public int getLastChar() {
        if (this.offset == 0) {
            return -1;
        }
        return this.buffer[this.offset - 1];
    }
    
    public String getLineTerminator() {
        return this.lineTerminator;
    }
    
    public int getOffset() {
        return this.offset;
    }
    
    public void indent(int add_area) {
        int n = this.indentStep * add_area;
        int n2;
        if (this.indentTabSize <= 0) {
            n2 = 0;
        }
        else {
            n2 = n / this.indentTabSize;
            n = n % this.indentTabSize + n2;
        }
        final int n3 = add_area = this.add_area(n);
        int i;
        while (true) {
            i = add_area;
            if (add_area == n3 + n2) {
                break;
            }
            this.buffer[add_area] = '\t';
            ++add_area;
        }
        while (i != n3 + n) {
            this.buffer[i] = ' ';
            ++i;
        }
    }
    
    public void line(final int n, final String s) {
        this.indent(n);
        this.p(s);
        this.nl();
    }
    
    public void nl() {
        this.p('\n');
    }
    
    public void p(final char c) {
        this.buffer[this.add_area(1)] = c;
    }
    
    public void p(final int n) {
        this.p(Integer.toString(n));
    }
    
    public void p(final String s) {
        final int length = s.length();
        s.getChars(0, length, this.buffer, this.add_area(length));
    }
    
    public final void p(final char[] array) {
        this.p(array, 0, array.length);
    }
    
    public void p(final char[] array, final int n, int n2) {
        n2 -= n;
        System.arraycopy(array, n, this.buffer, this.add_area(n2), n2);
    }
    
    public void qchar(int put_string_literal_char) {
        final int ensure_area = this.ensure_area(8);
        this.buffer[ensure_area] = '\'';
        put_string_literal_char = this.put_string_literal_char(ensure_area + 1, put_string_literal_char, false);
        this.buffer[put_string_literal_char] = '\'';
        this.offset = put_string_literal_char + 1;
    }
    
    public void qstring(final String s) {
        final int length = s.length();
        final int ensure_area = this.ensure_area(length * 6 + 2);
        this.buffer[ensure_area] = '\"';
        int put_string_literal_char = ensure_area + 1;
        for (int i = 0; i != length; ++i) {
            put_string_literal_char = this.put_string_literal_char(put_string_literal_char, s.charAt(i), true);
        }
        this.buffer[put_string_literal_char] = '\"';
        this.offset = put_string_literal_char + 1;
    }
    
    public void setIndentStep(final int indentStep) {
        this.indentStep = indentStep;
    }
    
    public void setIndentTabSize(final int indentTabSize) {
        this.indentTabSize = indentTabSize;
    }
    
    public void setLineTerminator(final String lineTerminator) {
        this.lineTerminator = lineTerminator;
    }
    
    @Override
    public String toString() {
        return new String(this.buffer, 0, this.offset);
    }
}
