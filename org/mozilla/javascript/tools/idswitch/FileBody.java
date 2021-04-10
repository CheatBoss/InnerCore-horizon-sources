package org.mozilla.javascript.tools.idswitch;

import java.io.*;

public class FileBody
{
    private char[] buffer;
    private int bufferEnd;
    ReplaceItem firstReplace;
    ReplaceItem lastReplace;
    private int lineBegin;
    private int lineEnd;
    private int lineNumber;
    private int nextLineStart;
    
    public FileBody() {
        this.buffer = new char[16384];
    }
    
    private static boolean equals(final String s, final char[] array, int n, final int n2) {
        if (s.length() == n2 - n) {
            final int n3 = 0;
            int i;
            for (i = n, n = n3; i != n2; ++i, ++n) {
                if (array[i] != s.charAt(n)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
    
    public char[] getBuffer() {
        return this.buffer;
    }
    
    public int getLineBegin() {
        return this.lineBegin;
    }
    
    public int getLineEnd() {
        return this.lineEnd;
    }
    
    public int getLineNumber() {
        return this.lineNumber;
    }
    
    public boolean nextLine() {
        if (this.nextLineStart == this.bufferEnd) {
            this.lineNumber = 0;
            return false;
        }
        char c = '\0';
        int nextLineStart = this.nextLineStart;
        char c2;
        while (true) {
            c2 = c;
            if (nextLineStart == this.bufferEnd) {
                break;
            }
            c = this.buffer[nextLineStart];
            if ((c2 = c) == '\n') {
                break;
            }
            if (c == '\r') {
                c2 = c;
                break;
            }
            ++nextLineStart;
        }
        this.lineBegin = this.nextLineStart;
        if ((this.lineEnd = nextLineStart) == this.bufferEnd) {
            this.nextLineStart = nextLineStart;
        }
        else if (c2 == '\r' && nextLineStart + 1 != this.bufferEnd && this.buffer[nextLineStart + 1] == '\n') {
            this.nextLineStart = nextLineStart + 2;
        }
        else {
            this.nextLineStart = nextLineStart + 1;
        }
        ++this.lineNumber;
        return true;
    }
    
    public void readData(final Reader reader) throws IOException {
        int length = this.buffer.length;
        int bufferEnd = 0;
        while (true) {
            final int read = reader.read(this.buffer, bufferEnd, length - bufferEnd);
            if (read < 0) {
                break;
            }
            bufferEnd += read;
            int n;
            if ((n = length) == bufferEnd) {
                n = length * 2;
                final char[] buffer = new char[n];
                System.arraycopy(this.buffer, 0, buffer, 0, bufferEnd);
                this.buffer = buffer;
            }
            length = n;
        }
        this.bufferEnd = bufferEnd;
    }
    
    public boolean setReplacement(final int n, final int n2, final String s) {
        if (equals(s, this.buffer, n, n2)) {
            return false;
        }
        final ReplaceItem next = new ReplaceItem(n, n2, s);
        if (this.firstReplace == null) {
            this.lastReplace = next;
            this.firstReplace = next;
        }
        else if (n < this.firstReplace.begin) {
            next.next = this.firstReplace;
            this.firstReplace = next;
        }
        else {
            ReplaceItem firstReplace = this.firstReplace;
            ReplaceItem next2;
            for (next2 = firstReplace.next; next2 != null; next2 = next2.next) {
                if (n < next2.begin) {
                    next.next = next2;
                    firstReplace.next = next;
                    break;
                }
                firstReplace = next2;
            }
            if (next2 == null) {
                this.lastReplace.next = next;
            }
        }
        return true;
    }
    
    public void startLineLoop() {
        this.lineNumber = 0;
        this.nextLineStart = 0;
        this.lineEnd = 0;
        this.lineBegin = 0;
    }
    
    public boolean wasModified() {
        return this.firstReplace != null;
    }
    
    public void writeData(final Writer writer) throws IOException {
        int end = 0;
        for (ReplaceItem replaceItem = this.firstReplace; replaceItem != null; replaceItem = replaceItem.next) {
            final int n = replaceItem.begin - end;
            if (n > 0) {
                writer.write(this.buffer, end, n);
            }
            writer.write(replaceItem.replacement);
            end = replaceItem.end;
        }
        final int n2 = this.bufferEnd - end;
        if (n2 != 0) {
            writer.write(this.buffer, end, n2);
        }
    }
    
    public void writeInitialData(final Writer writer) throws IOException {
        writer.write(this.buffer, 0, this.bufferEnd);
    }
    
    private static class ReplaceItem
    {
        int begin;
        int end;
        ReplaceItem next;
        String replacement;
        
        ReplaceItem(final int begin, final int end, final String replacement) {
            this.begin = begin;
            this.end = end;
            this.replacement = replacement;
        }
    }
}
