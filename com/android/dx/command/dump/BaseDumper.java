package com.android.dx.command.dump;

import com.android.dx.cf.code.*;
import com.android.dx.cf.iface.*;
import com.android.dx.util.*;
import java.io.*;

public abstract class BaseDumper implements ParseObserver
{
    protected Args args;
    private int at;
    private final byte[] bytes;
    private final String filePath;
    private final int hexCols;
    private int indent;
    private final PrintStream out;
    private final boolean rawBytes;
    private String separator;
    private final boolean strictParse;
    private final int width;
    
    public BaseDumper(final byte[] bytes, final PrintStream out, final String filePath, final Args args) {
        this.bytes = bytes;
        this.rawBytes = args.rawBytes;
        this.out = out;
        int width;
        if (args.width <= 0) {
            width = 79;
        }
        else {
            width = args.width;
        }
        this.width = width;
        this.filePath = filePath;
        this.strictParse = args.strictParse;
        this.indent = 0;
        String separator;
        if (this.rawBytes) {
            separator = "|";
        }
        else {
            separator = "";
        }
        this.separator = separator;
        this.at = 0;
        this.args = args;
        final int n = (this.width - 5) / 15 + 1 & 0xFFFFFFFE;
        int hexCols;
        if (n < 6) {
            hexCols = 6;
        }
        else if ((hexCols = n) > 10) {
            hexCols = 10;
        }
        this.hexCols = hexCols;
    }
    
    static int computeParamWidth(final ConcreteMethod concreteMethod, final boolean b) {
        return concreteMethod.getEffectiveDescriptor().getParameterTypes().getWordCount();
    }
    
    @Override
    public void changeIndent(int i) {
        this.indent += i;
        String separator;
        if (this.rawBytes) {
            separator = "|";
        }
        else {
            separator = "";
        }
        this.separator = separator;
        StringBuilder sb;
        for (i = 0; i < this.indent; ++i) {
            sb = new StringBuilder();
            sb.append(this.separator);
            sb.append("  ");
            this.separator = sb.toString();
        }
    }
    
    @Override
    public void endParsingMember(final ByteArray byteArray, final int n, final String s, final String s2, final Member member) {
    }
    
    protected final int getAt() {
        return this.at;
    }
    
    protected final byte[] getBytes() {
        return this.bytes;
    }
    
    protected final String getFilePath() {
        return this.filePath;
    }
    
    protected final boolean getRawBytes() {
        return this.rawBytes;
    }
    
    protected final boolean getStrictParse() {
        return this.strictParse;
    }
    
    protected final int getWidth1() {
        if (this.rawBytes) {
            return this.hexCols * 2 + 5 + this.hexCols / 2;
        }
        return 0;
    }
    
    protected final int getWidth2() {
        int n;
        if (this.rawBytes) {
            n = this.getWidth1() + 1;
        }
        else {
            n = 0;
        }
        return this.width - n - this.indent * 2;
    }
    
    protected final String hexDump(final int n, final int n2) {
        return Hex.dump(this.bytes, n, n2, n, this.hexCols, 4);
    }
    
    @Override
    public void parsed(final ByteArray byteArray, int underlyingOffset, final int n, final String s) {
        underlyingOffset = byteArray.underlyingOffset(underlyingOffset, this.getBytes());
        final boolean rawBytes = this.getRawBytes();
        if (underlyingOffset < this.at) {
            final StringBuilder sb = new StringBuilder();
            sb.append("<dump skipped backwards to ");
            sb.append(Hex.u4(underlyingOffset));
            sb.append(">");
            this.println(sb.toString());
            this.at = underlyingOffset;
        }
        else if (underlyingOffset > this.at) {
            String hexDump;
            if (rawBytes) {
                hexDump = this.hexDump(this.at, underlyingOffset - this.at);
            }
            else {
                hexDump = "";
            }
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("<skipped to ");
            sb2.append(Hex.u4(underlyingOffset));
            sb2.append(">");
            this.print(this.twoColumns(hexDump, sb2.toString()));
            this.at = underlyingOffset;
        }
        String hexDump2;
        if (rawBytes) {
            hexDump2 = this.hexDump(underlyingOffset, n);
        }
        else {
            hexDump2 = "";
        }
        this.print(this.twoColumns(hexDump2, s));
        this.at += n;
    }
    
    protected final void print(final String s) {
        this.out.print(s);
    }
    
    protected final void println(final String s) {
        this.out.println(s);
    }
    
    protected final void setAt(final ByteArray byteArray, final int n) {
        this.at = byteArray.underlyingOffset(n, this.bytes);
    }
    
    @Override
    public void startParsingMember(final ByteArray byteArray, final int n, final String s, final String s2) {
    }
    
    protected final String twoColumns(String string, final String s) {
        final int width1 = this.getWidth1();
        final int width2 = this.getWidth2();
        Label_0090: {
            if (width1 != 0) {
                break Label_0090;
            }
            try {
                final int length = s.length();
                final StringWriter stringWriter = new StringWriter(length * 2);
                final IndentingWriter indentingWriter = new IndentingWriter(stringWriter, width2, this.separator);
                indentingWriter.write(s);
                if (length == 0 || s.charAt(length - 1) != '\n') {
                    indentingWriter.write(10);
                }
                indentingWriter.flush();
                return stringWriter.toString();
                string = TwoColumnOutput.toString(string, width1, this.separator, s, width2);
                return string;
            }
            catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
