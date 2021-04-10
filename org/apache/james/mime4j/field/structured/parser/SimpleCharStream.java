package org.apache.james.mime4j.field.structured.parser;

import java.io.*;

public class SimpleCharStream
{
    public static final boolean staticFlag = false;
    int available;
    protected int[] bufcolumn;
    protected char[] buffer;
    protected int[] bufline;
    public int bufpos;
    int bufsize;
    protected int column;
    protected int inBuf;
    protected Reader inputStream;
    protected int line;
    protected int maxNextCharInd;
    protected boolean prevCharIsCR;
    protected boolean prevCharIsLF;
    protected int tabSize;
    int tokenBegin;
    
    public SimpleCharStream(final InputStream inputStream) {
        this(inputStream, 1, 1, 4096);
    }
    
    public SimpleCharStream(final InputStream inputStream, final int n, final int n2) {
        this(inputStream, n, n2, 4096);
    }
    
    public SimpleCharStream(final InputStream inputStream, final int n, final int n2, final int n3) {
        this(new InputStreamReader(inputStream), n, n2, n3);
    }
    
    public SimpleCharStream(final InputStream inputStream, final String s) throws UnsupportedEncodingException {
        this(inputStream, s, 1, 1, 4096);
    }
    
    public SimpleCharStream(final InputStream inputStream, final String s, final int n, final int n2) throws UnsupportedEncodingException {
        this(inputStream, s, n, n2, 4096);
    }
    
    public SimpleCharStream(final InputStream inputStream, final String s, final int n, final int n2, final int n3) throws UnsupportedEncodingException {
        InputStreamReader inputStreamReader;
        if (s == null) {
            inputStreamReader = new InputStreamReader(inputStream);
        }
        else {
            inputStreamReader = new InputStreamReader(inputStream, s);
        }
        this(inputStreamReader, n, n2, n3);
    }
    
    public SimpleCharStream(final Reader reader) {
        this(reader, 1, 1, 4096);
    }
    
    public SimpleCharStream(final Reader reader, final int n, final int n2) {
        this(reader, n, n2, 4096);
    }
    
    public SimpleCharStream(final Reader inputStream, final int line, final int n, final int n2) {
        this.bufpos = -1;
        this.column = 0;
        this.line = 1;
        this.prevCharIsCR = false;
        this.prevCharIsLF = false;
        this.maxNextCharInd = 0;
        this.inBuf = 0;
        this.tabSize = 8;
        this.inputStream = inputStream;
        this.line = line;
        this.column = n - 1;
        this.bufsize = n2;
        this.available = n2;
        this.buffer = new char[n2];
        this.bufline = new int[n2];
        this.bufcolumn = new int[n2];
    }
    
    public char BeginToken() throws IOException {
        this.tokenBegin = -1;
        final char char1 = this.readChar();
        this.tokenBegin = this.bufpos;
        return char1;
    }
    
    public void Done() {
        this.buffer = null;
        this.bufline = null;
        this.bufcolumn = null;
    }
    
    protected void ExpandBuff(final boolean b) {
        final int bufsize = this.bufsize;
        final int n = bufsize + 2048;
        final char[] array = new char[n];
        final int[] array2 = new int[n];
        final int[] array3 = new int[n];
        Label_0211: {
            if (!b) {
                break Label_0211;
            }
            try {
                System.arraycopy(this.buffer, this.tokenBegin, array, 0, bufsize - this.tokenBegin);
                System.arraycopy(this.buffer, 0, array, this.bufsize - this.tokenBegin, this.bufpos);
                this.buffer = array;
                System.arraycopy(this.bufline, this.tokenBegin, array2, 0, this.bufsize - this.tokenBegin);
                System.arraycopy(this.bufline, 0, array2, this.bufsize - this.tokenBegin, this.bufpos);
                this.bufline = array2;
                System.arraycopy(this.bufcolumn, this.tokenBegin, array3, 0, this.bufsize - this.tokenBegin);
                System.arraycopy(this.bufcolumn, 0, array3, this.bufsize - this.tokenBegin, this.bufpos);
                this.bufcolumn = array3;
                int bufpos = this.bufpos + (this.bufsize - this.tokenBegin);
                this.bufpos = bufpos;
                while (true) {
                    this.maxNextCharInd = bufpos;
                    final int n2 = this.bufsize + 2048;
                    this.bufsize = n2;
                    this.available = n2;
                    this.tokenBegin = 0;
                    return;
                    System.arraycopy(this.buffer, this.tokenBegin, array, 0, bufsize - this.tokenBegin);
                    this.buffer = array;
                    System.arraycopy(this.bufline, this.tokenBegin, array2, 0, this.bufsize - this.tokenBegin);
                    this.bufline = array2;
                    System.arraycopy(this.bufcolumn, this.tokenBegin, array3, 0, this.bufsize - this.tokenBegin);
                    this.bufcolumn = array3;
                    bufpos = this.bufpos - this.tokenBegin;
                    this.bufpos = bufpos;
                    continue;
                }
            }
            finally {
                final Throwable t;
                throw new Error(t.getMessage());
            }
        }
    }
    
    protected void FillBuff() throws IOException {
        final int maxNextCharInd = this.maxNextCharInd;
        final int available = this.available;
        Label_0115: {
            Block_7: {
                if (maxNextCharInd == available) {
                    int available2 = this.bufsize;
                    if (available == available2) {
                        available2 = this.tokenBegin;
                        if (available2 > 2048) {
                            this.maxNextCharInd = 0;
                            this.bufpos = 0;
                        }
                        else {
                            if (available2 < 0) {
                                this.maxNextCharInd = 0;
                                this.bufpos = 0;
                                break Label_0115;
                            }
                            this.ExpandBuff(false);
                            break Label_0115;
                        }
                    }
                    else {
                        final int tokenBegin = this.tokenBegin;
                        if (available <= tokenBegin) {
                            if (tokenBegin - available < 2048) {
                                this.ExpandBuff(true);
                                break Label_0115;
                            }
                            this.available = tokenBegin;
                            break Block_7;
                        }
                    }
                    this.available = available2;
                }
            }
            try {
                final int read = this.inputStream.read(this.buffer, this.maxNextCharInd, this.available - this.maxNextCharInd);
                if (read != -1) {
                    this.maxNextCharInd += read;
                    return;
                }
                this.inputStream.close();
                throw new IOException();
            }
            catch (IOException ex) {
                --this.bufpos;
                this.backup(0);
                if (this.tokenBegin == -1) {
                    this.tokenBegin = this.bufpos;
                }
                throw ex;
            }
        }
    }
    
    public String GetImage() {
        if (this.bufpos >= this.tokenBegin) {
            final char[] buffer = this.buffer;
            final int tokenBegin = this.tokenBegin;
            return new String(buffer, tokenBegin, this.bufpos - tokenBegin + 1);
        }
        final StringBuilder sb = new StringBuilder();
        final char[] buffer2 = this.buffer;
        final int tokenBegin2 = this.tokenBegin;
        sb.append(new String(buffer2, tokenBegin2, this.bufsize - tokenBegin2));
        sb.append(new String(this.buffer, 0, this.bufpos + 1));
        return sb.toString();
    }
    
    public char[] GetSuffix(final int n) {
        final char[] array = new char[n];
        final int bufpos = this.bufpos;
        if (bufpos + 1 >= n) {
            System.arraycopy(this.buffer, bufpos - n + 1, array, 0, n);
            return array;
        }
        final char[] buffer = this.buffer;
        final int bufsize = this.bufsize;
        final int n2 = n - bufpos - 1;
        System.arraycopy(buffer, bufsize - n2, array, 0, n2);
        final char[] buffer2 = this.buffer;
        final int bufpos2 = this.bufpos;
        System.arraycopy(buffer2, 0, array, n - bufpos2 - 1, bufpos2 + 1);
        return array;
    }
    
    public void ReInit(final InputStream inputStream) {
        this.ReInit(inputStream, 1, 1, 4096);
    }
    
    public void ReInit(final InputStream inputStream, final int n, final int n2) {
        this.ReInit(inputStream, n, n2, 4096);
    }
    
    public void ReInit(final InputStream inputStream, final int n, final int n2, final int n3) {
        this.ReInit(new InputStreamReader(inputStream), n, n2, n3);
    }
    
    public void ReInit(final InputStream inputStream, final String s) throws UnsupportedEncodingException {
        this.ReInit(inputStream, s, 1, 1, 4096);
    }
    
    public void ReInit(final InputStream inputStream, final String s, final int n, final int n2) throws UnsupportedEncodingException {
        this.ReInit(inputStream, s, n, n2, 4096);
    }
    
    public void ReInit(final InputStream inputStream, final String s, final int n, final int n2, final int n3) throws UnsupportedEncodingException {
        InputStreamReader inputStreamReader;
        if (s == null) {
            inputStreamReader = new InputStreamReader(inputStream);
        }
        else {
            inputStreamReader = new InputStreamReader(inputStream, s);
        }
        this.ReInit(inputStreamReader, n, n2, n3);
    }
    
    public void ReInit(final Reader reader) {
        this.ReInit(reader, 1, 1, 4096);
    }
    
    public void ReInit(final Reader reader, final int n, final int n2) {
        this.ReInit(reader, n, n2, 4096);
    }
    
    public void ReInit(final Reader inputStream, final int line, final int n, final int n2) {
        this.inputStream = inputStream;
        this.line = line;
        this.column = n - 1;
        final char[] buffer = this.buffer;
        if (buffer == null || n2 != buffer.length) {
            this.bufsize = n2;
            this.available = n2;
            this.buffer = new char[n2];
            this.bufline = new int[n2];
            this.bufcolumn = new int[n2];
        }
        this.prevCharIsCR = false;
        this.prevCharIsLF = false;
        this.maxNextCharInd = 0;
        this.inBuf = 0;
        this.tokenBegin = 0;
        this.bufpos = -1;
    }
    
    protected void UpdateLineColumn(final char c) {
        ++this.column;
        Label_0065: {
            if (this.prevCharIsLF) {
                this.prevCharIsLF = false;
            }
            else {
                if (!this.prevCharIsCR) {
                    break Label_0065;
                }
                this.prevCharIsCR = false;
                if (c == '\n') {
                    this.prevCharIsLF = true;
                    break Label_0065;
                }
            }
            final int line = this.line;
            this.column = 1;
            this.line = line + 1;
        }
        if (c != '\t') {
            if (c != '\n') {
                if (c == '\r') {
                    this.prevCharIsCR = true;
                }
            }
            else {
                this.prevCharIsLF = true;
            }
        }
        else {
            final int column = this.column - 1;
            this.column = column;
            final int tabSize = this.tabSize;
            this.column = column + (tabSize - column % tabSize);
        }
        final int[] bufline = this.bufline;
        final int bufpos = this.bufpos;
        bufline[bufpos] = this.line;
        this.bufcolumn[bufpos] = this.column;
    }
    
    public void adjustBeginLineColumn(int n, int bufsize) {
        int tokenBegin = this.tokenBegin;
        final int bufpos = this.bufpos;
        int n2;
        if (bufpos >= tokenBegin) {
            n2 = bufpos - tokenBegin + this.inBuf + 1;
        }
        else {
            n2 = this.inBuf + (this.bufsize - tokenBegin + bufpos + 1);
        }
        int n3 = 0;
        int n4 = 0;
        int n5 = 0;
        int n6;
        while (true) {
            n6 = tokenBegin;
            if (n3 >= n2) {
                break;
            }
            final int[] bufline = this.bufline;
            final int bufsize2 = this.bufsize;
            n5 = tokenBegin % bufsize2;
            final int n7 = bufline[n5];
            ++tokenBegin;
            final int n8 = tokenBegin % bufsize2;
            if (n7 != bufline[n8]) {
                n6 = tokenBegin;
                break;
            }
            bufline[n5] = n;
            final int[] bufcolumn = this.bufcolumn;
            final int n9 = bufcolumn[n8];
            final int n10 = bufcolumn[n5];
            bufcolumn[n5] = n4 + bufsize;
            ++n3;
            n4 = n9 + n4 - n10;
        }
        int n11 = n5;
        if (n3 < n2) {
            final int[] bufline2 = this.bufline;
            final int n12 = n + 1;
            bufline2[n5] = n;
            this.bufcolumn[n5] = bufsize + n4;
            n = n12;
            while (true) {
                n11 = n5;
                if (n3 >= n2) {
                    break;
                }
                final int[] bufline3 = this.bufline;
                bufsize = this.bufsize;
                n5 = n6 % bufsize;
                final int n13 = bufline3[n5];
                ++n6;
                if (n13 != bufline3[n6 % bufsize]) {
                    bufline3[n5] = n;
                    ++n;
                }
                else {
                    bufline3[n5] = n;
                }
                ++n3;
            }
        }
        this.line = this.bufline[n11];
        this.column = this.bufcolumn[n11];
    }
    
    public void backup(int bufpos) {
        this.inBuf += bufpos;
        bufpos = this.bufpos - bufpos;
        this.bufpos = bufpos;
        if (bufpos < 0) {
            this.bufpos = bufpos + this.bufsize;
        }
    }
    
    public int getBeginColumn() {
        return this.bufcolumn[this.tokenBegin];
    }
    
    public int getBeginLine() {
        return this.bufline[this.tokenBegin];
    }
    
    public int getColumn() {
        return this.bufcolumn[this.bufpos];
    }
    
    public int getEndColumn() {
        return this.bufcolumn[this.bufpos];
    }
    
    public int getEndLine() {
        return this.bufline[this.bufpos];
    }
    
    public int getLine() {
        return this.bufline[this.bufpos];
    }
    
    protected int getTabSize(final int n) {
        return this.tabSize;
    }
    
    public char readChar() throws IOException {
        final int inBuf = this.inBuf;
        if (inBuf > 0) {
            this.inBuf = inBuf - 1;
            if (++this.bufpos == this.bufsize) {
                this.bufpos = 0;
            }
            return this.buffer[this.bufpos];
        }
        if (++this.bufpos >= this.maxNextCharInd) {
            this.FillBuff();
        }
        final char c = this.buffer[this.bufpos];
        this.UpdateLineColumn(c);
        return c;
    }
    
    protected void setTabSize(final int tabSize) {
        this.tabSize = tabSize;
    }
}
