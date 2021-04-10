package org.mozilla.javascript;

import java.io.*;

class TokenStream
{
    private static final char BYTE_ORDER_MARK = '\ufeff';
    private static final int EOF_CHAR = -1;
    private ObjToIntMap allStrings;
    private int commentCursor;
    private String commentPrefix;
    Token.CommentType commentType;
    int cursor;
    private boolean dirtyLine;
    private boolean hitEOF;
    private boolean isHex;
    private boolean isOctal;
    private int lineEndChar;
    private int lineStart;
    int lineno;
    private double number;
    private Parser parser;
    private int quoteChar;
    String regExpFlags;
    private char[] sourceBuffer;
    int sourceCursor;
    private int sourceEnd;
    private Reader sourceReader;
    private String sourceString;
    private String string;
    private char[] stringBuffer;
    private int stringBufferTop;
    int tokenBeg;
    int tokenEnd;
    private final int[] ungetBuffer;
    private int ungetCursor;
    private boolean xmlIsAttribute;
    private boolean xmlIsTagContent;
    private int xmlOpenTagsCount;
    
    TokenStream(final Parser parser, final Reader sourceReader, final String sourceString, final int lineno) {
        this.string = "";
        this.stringBuffer = new char[128];
        this.allStrings = new ObjToIntMap(50);
        this.ungetBuffer = new int[3];
        this.hitEOF = false;
        this.lineStart = 0;
        this.lineEndChar = -1;
        this.commentPrefix = "";
        this.commentCursor = -1;
        this.parser = parser;
        this.lineno = lineno;
        if (sourceReader != null) {
            if (sourceString != null) {
                Kit.codeBug();
            }
            this.sourceReader = sourceReader;
            this.sourceBuffer = new char[512];
            this.sourceEnd = 0;
        }
        else {
            if (sourceString == null) {
                Kit.codeBug();
            }
            this.sourceString = sourceString;
            this.sourceEnd = sourceString.length();
        }
        this.cursor = 0;
        this.sourceCursor = 0;
    }
    
    private void addToString(final int n) {
        final int stringBufferTop = this.stringBufferTop;
        if (stringBufferTop == this.stringBuffer.length) {
            final char[] stringBuffer = new char[this.stringBuffer.length * 2];
            System.arraycopy(this.stringBuffer, 0, stringBuffer, 0, stringBufferTop);
            this.stringBuffer = stringBuffer;
        }
        this.stringBuffer[stringBufferTop] = (char)n;
        this.stringBufferTop = stringBufferTop + 1;
    }
    
    private boolean canUngetChar() {
        final int ungetCursor = this.ungetCursor;
        boolean b = true;
        if (ungetCursor != 0) {
            if (this.ungetBuffer[this.ungetCursor - 1] != 10) {
                return true;
            }
            b = false;
        }
        return b;
    }
    
    private final int charAt(final int n) {
        if (n < 0) {
            return -1;
        }
        if (this.sourceString == null) {
            int n2;
            if ((n2 = n) >= this.sourceEnd) {
                final int sourceCursor = this.sourceCursor;
                try {
                    if (!this.fillSourceBuffer()) {
                        return -1;
                    }
                    n2 = n - (sourceCursor - this.sourceCursor);
                }
                catch (IOException ex) {
                    return -1;
                }
            }
            return this.sourceBuffer[n2];
        }
        if (n >= this.sourceEnd) {
            return -1;
        }
        return this.sourceString.charAt(n);
    }
    
    private String convertLastCharToHex(String hexString) {
        final int n = hexString.length() - 1;
        int i = 0;
        final StringBuffer sb = new StringBuffer(hexString.substring(0, n));
        sb.append("\\u");
        for (hexString = Integer.toHexString(hexString.charAt(n)); i < 4 - hexString.length(); ++i) {
            sb.append('0');
        }
        sb.append(hexString);
        return sb.toString();
    }
    
    private boolean fillSourceBuffer() throws IOException {
        if (this.sourceString != null) {
            Kit.codeBug();
        }
        if (this.sourceEnd == this.sourceBuffer.length) {
            if (this.lineStart != 0 && !this.isMarkingComment()) {
                System.arraycopy(this.sourceBuffer, this.lineStart, this.sourceBuffer, 0, this.sourceEnd - this.lineStart);
                this.sourceEnd -= this.lineStart;
                this.sourceCursor -= this.lineStart;
                this.lineStart = 0;
            }
            else {
                final char[] sourceBuffer = new char[this.sourceBuffer.length * 2];
                System.arraycopy(this.sourceBuffer, 0, sourceBuffer, 0, this.sourceEnd);
                this.sourceBuffer = sourceBuffer;
            }
        }
        final int read = this.sourceReader.read(this.sourceBuffer, this.sourceEnd, this.sourceBuffer.length - this.sourceEnd);
        if (read < 0) {
            return false;
        }
        this.sourceEnd += read;
        return true;
    }
    
    private int getChar() throws IOException {
        return this.getChar(true);
    }
    
    private int getChar(final boolean b) throws IOException {
        if (this.ungetCursor != 0) {
            ++this.cursor;
            final int[] ungetBuffer = this.ungetBuffer;
            final int ungetCursor = this.ungetCursor - 1;
            this.ungetCursor = ungetCursor;
            return ungetBuffer[ungetCursor];
        }
        int n;
        while (true) {
            char char1;
            if (this.sourceString != null) {
                if (this.sourceCursor == this.sourceEnd) {
                    this.hitEOF = true;
                    return -1;
                }
                ++this.cursor;
                char1 = this.sourceString.charAt(this.sourceCursor++);
            }
            else {
                if (this.sourceCursor == this.sourceEnd && !this.fillSourceBuffer()) {
                    this.hitEOF = true;
                    return -1;
                }
                ++this.cursor;
                char1 = this.sourceBuffer[this.sourceCursor++];
            }
            if (this.lineEndChar >= 0) {
                if (this.lineEndChar == 13 && char1 == '\n') {
                    this.lineEndChar = 10;
                    continue;
                }
                this.lineEndChar = -1;
                this.lineStart = this.sourceCursor - 1;
                ++this.lineno;
            }
            if (char1 <= '\u007f') {
                if (char1 == '\n' || (n = char1) == 13) {
                    this.lineEndChar = char1;
                    return 10;
                }
                break;
            }
            else {
                if (char1 == '\ufeff') {
                    return char1;
                }
                if (b && isJSFormatChar(char1)) {
                    continue;
                }
                n = char1;
                if (ScriptRuntime.isJSLineTerminator(char1)) {
                    this.lineEndChar = char1;
                    n = 10;
                    break;
                }
                break;
            }
        }
        return n;
    }
    
    private int getCharIgnoreLineEnd() throws IOException {
        if (this.ungetCursor != 0) {
            ++this.cursor;
            final int[] ungetBuffer = this.ungetBuffer;
            final int ungetCursor = this.ungetCursor - 1;
            this.ungetCursor = ungetCursor;
            return ungetBuffer[ungetCursor];
        }
        int n;
        while (true) {
            char char1;
            if (this.sourceString != null) {
                if (this.sourceCursor == this.sourceEnd) {
                    this.hitEOF = true;
                    return -1;
                }
                ++this.cursor;
                char1 = this.sourceString.charAt(this.sourceCursor++);
            }
            else {
                if (this.sourceCursor == this.sourceEnd && !this.fillSourceBuffer()) {
                    this.hitEOF = true;
                    return -1;
                }
                ++this.cursor;
                char1 = this.sourceBuffer[this.sourceCursor++];
            }
            if (char1 <= '\u007f') {
                if (char1 == '\n' || (n = char1) == 13) {
                    this.lineEndChar = char1;
                    return 10;
                }
                break;
            }
            else {
                if (char1 == '\ufeff') {
                    return char1;
                }
                if (isJSFormatChar(char1)) {
                    continue;
                }
                n = char1;
                if (ScriptRuntime.isJSLineTerminator(char1)) {
                    this.lineEndChar = char1;
                    n = 10;
                    break;
                }
                break;
            }
        }
        return n;
    }
    
    private String getStringFromBuffer() {
        this.tokenEnd = this.cursor;
        return new String(this.stringBuffer, 0, this.stringBufferTop);
    }
    
    private static boolean isAlpha(final int n) {
        final boolean b = false;
        boolean b2 = false;
        if (n <= 90) {
            if (65 <= n) {
                b2 = true;
            }
            return b2;
        }
        boolean b3 = b;
        if (97 <= n) {
            b3 = b;
            if (n <= 122) {
                b3 = true;
            }
        }
        return b3;
    }
    
    static boolean isDigit(final int n) {
        return 48 <= n && n <= 57;
    }
    
    private static boolean isJSFormatChar(final int n) {
        return n > 127 && Character.getType((char)n) == 16;
    }
    
    static boolean isJSSpace(final int n) {
        final boolean b = true;
        final boolean b2 = true;
        if (n <= 127) {
            boolean b3 = b2;
            if (n != 32) {
                b3 = b2;
                if (n != 9) {
                    b3 = b2;
                    if (n != 12) {
                        if (n == 11) {
                            return true;
                        }
                        b3 = false;
                    }
                }
            }
            return b3;
        }
        boolean b4 = b;
        if (n != 160) {
            b4 = b;
            if (n != 65279) {
                if (Character.getType((char)n) == 12) {
                    return true;
                }
                b4 = false;
            }
        }
        return b4;
    }
    
    static boolean isKeyword(final String s) {
        return stringToKeyword(s) != 0;
    }
    
    private boolean isMarkingComment() {
        return this.commentCursor != -1;
    }
    
    private void markCommentStart() {
        this.markCommentStart("");
    }
    
    private void markCommentStart(final String commentPrefix) {
        if (this.parser.compilerEnv.isRecordingComments() && this.sourceReader != null) {
            this.commentPrefix = commentPrefix;
            this.commentCursor = this.sourceCursor - 1;
        }
    }
    
    private boolean matchChar(final int n) throws IOException {
        final int charIgnoreLineEnd = this.getCharIgnoreLineEnd();
        if (charIgnoreLineEnd == n) {
            this.tokenEnd = this.cursor;
            return true;
        }
        this.ungetCharIgnoreLineEnd(charIgnoreLineEnd);
        return false;
    }
    
    private int peekChar() throws IOException {
        final int char1 = this.getChar();
        this.ungetChar(char1);
        return char1;
    }
    
    private boolean readCDATA() throws IOException {
        int i = this.getChar();
        while (i != -1) {
            this.addToString(i);
            if (i == 93 && this.peekChar() == 93) {
                i = this.getChar();
                this.addToString(i);
                if (this.peekChar() == 62) {
                    this.addToString(this.getChar());
                    return true;
                }
                continue;
            }
            else {
                i = this.getChar();
            }
        }
        this.stringBufferTop = 0;
        this.string = null;
        this.parser.addError("msg.XML.bad.form");
        return false;
    }
    
    private boolean readEntity() throws IOException {
        int n = 1;
        while (true) {
            final int char1 = this.getChar();
            if (char1 == -1) {
                this.stringBufferTop = 0;
                this.string = null;
                this.parser.addError("msg.XML.bad.form");
                return false;
            }
            this.addToString(char1);
            if (char1 != 60) {
                if (char1 != 62) {
                    continue;
                }
                if (--n == 0) {
                    return true;
                }
                continue;
            }
            else {
                ++n;
            }
        }
    }
    
    private boolean readPI() throws IOException {
        int char1;
        do {
            char1 = this.getChar();
            if (char1 == -1) {
                this.stringBufferTop = 0;
                this.string = null;
                this.parser.addError("msg.XML.bad.form");
                return false;
            }
            this.addToString(char1);
        } while (char1 != 63 || this.peekChar() != 62);
        this.addToString(this.getChar());
        return true;
    }
    
    private boolean readQuotedString(final int n) throws IOException {
        int i;
        do {
            i = this.getChar();
            if (i == -1) {
                this.stringBufferTop = 0;
                this.string = null;
                this.parser.addError("msg.XML.bad.form");
                return false;
            }
            this.addToString(i);
        } while (i != n);
        return true;
    }
    
    private boolean readXmlComment() throws IOException {
        int i = this.getChar();
        while (i != -1) {
            this.addToString(i);
            if (i == 45 && this.peekChar() == 45) {
                i = this.getChar();
                this.addToString(i);
                if (this.peekChar() == 62) {
                    this.addToString(this.getChar());
                    return true;
                }
                continue;
            }
            else {
                i = this.getChar();
            }
        }
        this.stringBufferTop = 0;
        this.string = null;
        this.parser.addError("msg.XML.bad.form");
        return false;
    }
    
    private void skipLine() throws IOException {
        int char1;
        do {
            char1 = this.getChar();
        } while (char1 != -1 && char1 != 10);
        this.ungetChar(char1);
        this.tokenEnd = this.cursor;
    }
    
    private static int stringToKeyword(final String s) {
        final boolean b = false;
        int n = 0;
        final boolean b2 = false;
        final boolean b3 = false;
        final boolean b4 = false;
        final boolean b5 = false;
        String s2 = null;
        final String s3 = null;
        final String s4 = null;
        final String s5 = null;
        final String s6 = null;
        Label_1765: {
            Label_1740: {
            Label_1653:
                while (true) {
                    Label_0736: {
                        switch (s.length()) {
                            default: {
                                n = (b3 ? 1 : 0);
                                s2 = s5;
                                break;
                            }
                            case 12: {
                                s2 = "synchronized";
                                n = 127;
                                break;
                            }
                            case 10: {
                                final char char1 = s.charAt(1);
                                if (char1 == 'm') {
                                    s2 = "implements";
                                    n = 127;
                                }
                                else {
                                    n = (b ? 1 : 0);
                                    if (char1 == 'n') {
                                        s2 = "instanceof";
                                        n = 53;
                                    }
                                }
                                break;
                            }
                            case 9: {
                                final char char2 = s.charAt(0);
                                if (char2 == 'i') {
                                    s2 = "interface";
                                    n = 127;
                                    break;
                                }
                                if (char2 == 'p') {
                                    s2 = "protected";
                                    n = 127;
                                    break;
                                }
                                s2 = s3;
                                if (char2 == 't') {
                                    s2 = "transient";
                                    n = 127;
                                    break;
                                }
                                break;
                            }
                            case 8: {
                                final char char3 = s.charAt(0);
                                if (char3 == 'a') {
                                    s2 = "abstract";
                                    n = 127;
                                    break;
                                }
                                if (char3 == 'f') {
                                    s2 = "function";
                                    n = 109;
                                    break;
                                }
                                if (char3 == 'v') {
                                    s2 = "volatile";
                                    n = 127;
                                    break;
                                }
                                switch (char3) {
                                    default: {
                                        break Label_0736;
                                    }
                                    case 100: {
                                        s2 = "debugger";
                                        n = 160;
                                        break Label_0109;
                                    }
                                    case 99: {
                                        s2 = "continue";
                                        n = 121;
                                        break Label_0109;
                                    }
                                }
                                break;
                            }
                            case 7: {
                                final char char4 = s.charAt(1);
                                if (char4 == 'a') {
                                    s2 = "package";
                                    n = 127;
                                    break;
                                }
                                if (char4 == 'e') {
                                    s2 = "default";
                                    n = 116;
                                    break;
                                }
                                if (char4 == 'i') {
                                    s2 = "finally";
                                    n = 125;
                                    break;
                                }
                                if (char4 == 'o') {
                                    s2 = "boolean";
                                    n = 127;
                                    break;
                                }
                                if (char4 == 'r') {
                                    s2 = "private";
                                    n = 127;
                                    break;
                                }
                                if (char4 != 'x') {
                                    break Label_0736;
                                }
                                s2 = "extends";
                                n = 127;
                                break;
                            }
                            case 6: {
                                final char char5 = s.charAt(1);
                                if (char5 == 'a') {
                                    s2 = "native";
                                    n = 127;
                                    break;
                                }
                                if (char5 != 'e') {
                                    if (char5 == 'h') {
                                        s2 = "throws";
                                        n = 127;
                                        break;
                                    }
                                    if (char5 == 'm') {
                                        s2 = "import";
                                        n = 127;
                                        break;
                                    }
                                    if (char5 == 'o') {
                                        s2 = "double";
                                        n = 127;
                                        break;
                                    }
                                    switch (char5) {
                                        default: {
                                            switch (char5) {
                                                default: {
                                                    break Label_0736;
                                                }
                                                case 121: {
                                                    s2 = "typeof";
                                                    n = 32;
                                                    break Label_0109;
                                                }
                                                case 120: {
                                                    s2 = "export";
                                                    n = 127;
                                                    break Label_0109;
                                                }
                                                case 119: {
                                                    s2 = "switch";
                                                    n = 114;
                                                    break Label_0109;
                                                }
                                            }
                                            break;
                                        }
                                        case 117: {
                                            s2 = "public";
                                            n = 127;
                                            break Label_0109;
                                        }
                                        case 116: {
                                            s2 = "static";
                                            n = 127;
                                            break Label_0109;
                                        }
                                    }
                                }
                                else {
                                    final char char6 = s.charAt(0);
                                    if (char6 == 'd') {
                                        s2 = "delete";
                                        n = 31;
                                        break;
                                    }
                                    s2 = s3;
                                    if (char6 == 'r') {
                                        s2 = "return";
                                        n = 4;
                                        break;
                                    }
                                    break;
                                }
                                break;
                            }
                            case 5: {
                                final char char7 = s.charAt(2);
                                if (char7 == 'a') {
                                    s2 = "class";
                                    n = 127;
                                    break;
                                }
                                if (char7 != 'e') {
                                    if (char7 == 'i') {
                                        s2 = "while";
                                        n = 117;
                                        break;
                                    }
                                    if (char7 == 'l') {
                                        s2 = "false";
                                        n = 44;
                                        break;
                                    }
                                    if (char7 == 'r') {
                                        s2 = "throw";
                                        n = 50;
                                        break;
                                    }
                                    if (char7 == 't') {
                                        s2 = "catch";
                                        n = 124;
                                        break;
                                    }
                                    switch (char7) {
                                        default: {
                                            break Label_0736;
                                        }
                                        case 112: {
                                            s2 = "super";
                                            n = 127;
                                            break Label_0109;
                                        }
                                        case 111: {
                                            final char char8 = s.charAt(0);
                                            if (char8 == 'f') {
                                                s2 = "float";
                                                n = 127;
                                                break Label_0109;
                                            }
                                            s2 = s3;
                                            if (char8 == 's') {
                                                s2 = "short";
                                                n = 127;
                                                break Label_0109;
                                            }
                                            break Label_0109;
                                        }
                                        case 110: {
                                            final char char9 = s.charAt(0);
                                            if (char9 == 'c') {
                                                s2 = "const";
                                                n = 154;
                                                break Label_0109;
                                            }
                                            s2 = s3;
                                            if (char9 == 'f') {
                                                s2 = "final";
                                                n = 127;
                                                break Label_0109;
                                            }
                                            break Label_0109;
                                        }
                                    }
                                }
                                else {
                                    final char char10 = s.charAt(0);
                                    if (char10 == 'b') {
                                        s2 = "break";
                                        n = 120;
                                        break;
                                    }
                                    s2 = s3;
                                    if (char10 == 'y') {
                                        s2 = "yield";
                                        n = 72;
                                        break;
                                    }
                                    break;
                                }
                                break;
                            }
                            case 4: {
                                switch (s.charAt(0)) {
                                    default: {
                                        n = (b5 ? 1 : 0);
                                        s2 = s6;
                                        break Label_0109;
                                    }
                                    case 'w': {
                                        s2 = "with";
                                        n = 123;
                                        break Label_0109;
                                    }
                                    case 'v': {
                                        s2 = "void";
                                        n = 126;
                                        break Label_0109;
                                    }
                                    case 't': {
                                        final char char11 = s.charAt(3);
                                        if (char11 == 'e') {
                                            n = (b2 ? 1 : 0);
                                            s2 = s4;
                                            if (s.charAt(2) != 'u') {
                                                break Label_0109;
                                            }
                                            n = (b2 ? 1 : 0);
                                            s2 = s4;
                                            if (s.charAt(1) == 'r') {
                                                n = 45;
                                                break Label_1653;
                                            }
                                            break Label_0109;
                                        }
                                        else {
                                            n = (b2 ? 1 : 0);
                                            s2 = s4;
                                            if (char11 != 's') {
                                                break Label_0109;
                                            }
                                            n = (b2 ? 1 : 0);
                                            s2 = s4;
                                            if (s.charAt(2) != 'i') {
                                                break Label_0109;
                                            }
                                            n = (b2 ? 1 : 0);
                                            s2 = s4;
                                            if (s.charAt(1) == 'h') {
                                                n = 43;
                                                break Label_1653;
                                            }
                                            break Label_0109;
                                        }
                                        break;
                                    }
                                    case 'n': {
                                        s2 = "null";
                                        n = 42;
                                        break Label_0109;
                                    }
                                    case 'l': {
                                        s2 = "long";
                                        n = 127;
                                        break Label_0109;
                                    }
                                    case 'g': {
                                        s2 = "goto";
                                        n = 127;
                                        break Label_0109;
                                    }
                                    case 'e': {
                                        final char char12 = s.charAt(3);
                                        if (char12 == 'e') {
                                            if (s.charAt(2) == 's' && s.charAt(1) == 'l') {
                                                n = 113;
                                                break Label_1653;
                                            }
                                            break;
                                        }
                                        else {
                                            if (char12 == 'm' && s.charAt(2) == 'u' && s.charAt(1) == 'n') {
                                                n = 127;
                                                break Label_1653;
                                            }
                                            break;
                                        }
                                        break;
                                    }
                                    case 'c': {
                                        final char char13 = s.charAt(3);
                                        if (char13 == 'e') {
                                            if (s.charAt(2) == 's' && s.charAt(1) == 'a') {
                                                n = 115;
                                                break Label_1653;
                                            }
                                            break;
                                        }
                                        else {
                                            if (char13 == 'r' && s.charAt(2) == 'a' && s.charAt(1) == 'h') {
                                                n = 127;
                                                break Label_1653;
                                            }
                                            break;
                                        }
                                        break;
                                    }
                                    case 'b': {
                                        s2 = "byte";
                                        n = 127;
                                        break Label_0109;
                                    }
                                }
                                n = (b2 ? 1 : 0);
                                s2 = s4;
                                break;
                            }
                            case 3: {
                                final char char14 = s.charAt(0);
                                if (char14 != 'f') {
                                    if (char14 != 'i') {
                                        if (char14 != 'l') {
                                            if (char14 != 'n') {
                                                if (char14 != 't') {
                                                    if (char14 != 'v') {
                                                        n = (b5 ? 1 : 0);
                                                        s2 = s6;
                                                        break;
                                                    }
                                                    n = (b5 ? 1 : 0);
                                                    s2 = s6;
                                                    if (s.charAt(2) != 'r') {
                                                        break;
                                                    }
                                                    n = (b5 ? 1 : 0);
                                                    s2 = s6;
                                                    if (s.charAt(1) == 'a') {
                                                        n = 122;
                                                        break Label_1653;
                                                    }
                                                    break;
                                                }
                                                else {
                                                    n = (b5 ? 1 : 0);
                                                    s2 = s6;
                                                    if (s.charAt(2) != 'y') {
                                                        break;
                                                    }
                                                    n = (b5 ? 1 : 0);
                                                    s2 = s6;
                                                    if (s.charAt(1) == 'r') {
                                                        n = 81;
                                                        break Label_1653;
                                                    }
                                                    break;
                                                }
                                            }
                                            else {
                                                n = (b5 ? 1 : 0);
                                                s2 = s6;
                                                if (s.charAt(2) != 'w') {
                                                    break;
                                                }
                                                n = (b5 ? 1 : 0);
                                                s2 = s6;
                                                if (s.charAt(1) == 'e') {
                                                    n = 30;
                                                    break Label_1653;
                                                }
                                                break;
                                            }
                                        }
                                        else {
                                            n = (b5 ? 1 : 0);
                                            s2 = s6;
                                            if (s.charAt(2) != 't') {
                                                break;
                                            }
                                            n = (b5 ? 1 : 0);
                                            s2 = s6;
                                            if (s.charAt(1) == 'e') {
                                                n = 153;
                                                break Label_1653;
                                            }
                                            break;
                                        }
                                    }
                                    else {
                                        n = (b5 ? 1 : 0);
                                        s2 = s6;
                                        if (s.charAt(2) != 't') {
                                            break;
                                        }
                                        n = (b5 ? 1 : 0);
                                        s2 = s6;
                                        if (s.charAt(1) == 'n') {
                                            n = 127;
                                            break Label_1653;
                                        }
                                        break;
                                    }
                                }
                                else {
                                    n = (b5 ? 1 : 0);
                                    s2 = s6;
                                    if (s.charAt(2) != 'r') {
                                        break;
                                    }
                                    n = (b5 ? 1 : 0);
                                    s2 = s6;
                                    if (s.charAt(1) == 'o') {
                                        n = 119;
                                        break Label_1653;
                                    }
                                    break;
                                }
                                break;
                            }
                            case 2: {
                                final char char15 = s.charAt(1);
                                Label_1731: {
                                    if (char15 != 'f') {
                                        if (char15 == 'n') {
                                            if (s.charAt(0) != 'i') {
                                                break Label_1731;
                                            }
                                            n = 52;
                                        }
                                        else {
                                            if (char15 != 'o' || s.charAt(0) != 'd') {
                                                break Label_1731;
                                            }
                                            n = 118;
                                        }
                                        break Label_1765;
                                    }
                                    if (s.charAt(0) == 'i') {
                                        n = 112;
                                        break Label_1653;
                                    }
                                }
                                s2 = null;
                                n = (b4 ? 1 : 0);
                                break;
                            }
                        }
                        break Label_1740;
                    }
                    n = (b5 ? 1 : 0);
                    s2 = s6;
                    continue;
                }
                break Label_1765;
            }
            if (s2 != null && s2 != s && !s2.equals(s)) {
                n = 0;
            }
        }
        if (n == 0) {
            return 0;
        }
        return n & 0xFF;
    }
    
    private final String substring(final int n, final int n2) {
        if (this.sourceString != null) {
            return this.sourceString.substring(n, n2);
        }
        return new String(this.sourceBuffer, n, n2 - n);
    }
    
    private void ungetChar(final int n) {
        if (this.ungetCursor != 0 && this.ungetBuffer[this.ungetCursor - 1] == 10) {
            Kit.codeBug();
        }
        this.ungetBuffer[this.ungetCursor++] = n;
        --this.cursor;
    }
    
    private void ungetCharIgnoreLineEnd(final int n) {
        this.ungetBuffer[this.ungetCursor++] = n;
        --this.cursor;
    }
    
    final boolean eof() {
        return this.hitEOF;
    }
    
    final String getAndResetCurrentComment() {
        if (this.sourceString != null) {
            if (this.isMarkingComment()) {
                Kit.codeBug();
            }
            return this.sourceString.substring(this.tokenBeg, this.tokenEnd);
        }
        if (!this.isMarkingComment()) {
            Kit.codeBug();
        }
        final StringBuilder sb = new StringBuilder(this.commentPrefix);
        sb.append(this.sourceBuffer, this.commentCursor, this.getTokenLength() - this.commentPrefix.length());
        this.commentCursor = -1;
        return sb.toString();
    }
    
    public Token.CommentType getCommentType() {
        return this.commentType;
    }
    
    public int getCursor() {
        return this.cursor;
    }
    
    int getFirstXMLToken() throws IOException {
        this.xmlOpenTagsCount = 0;
        this.xmlIsAttribute = false;
        this.xmlIsTagContent = false;
        if (!this.canUngetChar()) {
            return -1;
        }
        this.ungetChar(60);
        return this.getNextXMLToken();
    }
    
    final String getLine() {
        final int sourceCursor = this.sourceCursor;
        int n2;
        if (this.lineEndChar >= 0) {
            final int n = n2 = sourceCursor - 1;
            if (this.lineEndChar == 10) {
                n2 = n;
                if (this.charAt(n - 1) == 13) {
                    n2 = n - 1;
                }
            }
        }
        else {
            int n3 = sourceCursor - this.lineStart;
            while (true) {
                final int char1 = this.charAt(this.lineStart + n3);
                if (char1 == -1 || ScriptRuntime.isJSLineTerminator(char1)) {
                    break;
                }
                ++n3;
            }
            n2 = this.lineStart + n3;
        }
        return this.substring(this.lineStart, n2);
    }
    
    final String getLine(int sourceCursor, final int[] array) {
        int i = this.cursor + this.ungetCursor - sourceCursor;
        sourceCursor = this.sourceCursor;
        if (i > sourceCursor) {
            return null;
        }
        int n = 0;
        int n2 = 0;
        while (i > 0) {
            final int char1 = this.charAt(sourceCursor - 1);
            int n3 = n2;
            int n4 = i;
            int n5 = sourceCursor;
            int n6 = n;
            if (ScriptRuntime.isJSLineTerminator(char1)) {
                int n7 = i;
                n5 = sourceCursor;
                if (char1 == 10) {
                    n7 = i;
                    n5 = sourceCursor;
                    if (this.charAt(sourceCursor - 2) == 13) {
                        n7 = i - 1;
                        n5 = sourceCursor - 1;
                    }
                }
                n3 = n2 + 1;
                n6 = n5 - 1;
                n4 = n7;
            }
            i = n4 - 1;
            sourceCursor = n5 - 1;
            n2 = n3;
            n = n6;
        }
        final int n8 = 0;
        int n9 = 0;
        int n10;
        while (true) {
            n10 = n8;
            if (sourceCursor <= 0) {
                break;
            }
            if (ScriptRuntime.isJSLineTerminator(this.charAt(sourceCursor - 1))) {
                n10 = sourceCursor;
                break;
            }
            --sourceCursor;
            ++n9;
        }
        final int lineno = this.lineno;
        if (this.lineEndChar >= 0) {
            sourceCursor = 1;
        }
        else {
            sourceCursor = 0;
        }
        array[0] = lineno - n2 + sourceCursor;
        array[1] = n9;
        if (n2 == 0) {
            return this.getLine();
        }
        return this.substring(n10, n);
    }
    
    final int getLineno() {
        return this.lineno;
    }
    
    int getNextXMLToken() throws IOException {
        this.tokenBeg = this.cursor;
        this.stringBufferTop = 0;
        while (true) {
            final int char1 = this.getChar();
            if (char1 == -1) {
                this.tokenEnd = this.cursor;
                this.stringBufferTop = 0;
                this.string = null;
                this.parser.addError("msg.XML.bad.form");
                return -1;
            }
            if (this.xmlIsTagContent) {
                switch (char1) {
                    default: {
                        this.addToString(char1);
                        this.xmlIsAttribute = false;
                        break;
                    }
                    case 123: {
                        this.ungetChar(char1);
                        this.string = this.getStringFromBuffer();
                        return 145;
                    }
                    case 62: {
                        this.addToString(char1);
                        this.xmlIsTagContent = false;
                        this.xmlIsAttribute = false;
                        break;
                    }
                    case 61: {
                        this.addToString(char1);
                        this.xmlIsAttribute = true;
                        break;
                    }
                    case 47: {
                        this.addToString(char1);
                        if (this.peekChar() == 62) {
                            this.addToString(this.getChar());
                            this.xmlIsTagContent = false;
                            --this.xmlOpenTagsCount;
                            break;
                        }
                        break;
                    }
                    case 34:
                    case 39: {
                        this.addToString(char1);
                        if (!this.readQuotedString(char1)) {
                            return -1;
                        }
                        break;
                    }
                    case 9:
                    case 10:
                    case 13:
                    case 32: {
                        this.addToString(char1);
                        break;
                    }
                }
                if (!this.xmlIsTagContent && this.xmlOpenTagsCount == 0) {
                    this.string = this.getStringFromBuffer();
                    return 148;
                }
                continue;
            }
            else if (char1 != 60) {
                if (char1 == 123) {
                    this.ungetChar(char1);
                    this.string = this.getStringFromBuffer();
                    return 145;
                }
                this.addToString(char1);
            }
            else {
                this.addToString(char1);
                final int peekChar = this.peekChar();
                if (peekChar != 33) {
                    if (peekChar != 47) {
                        if (peekChar != 63) {
                            this.xmlIsTagContent = true;
                            ++this.xmlOpenTagsCount;
                        }
                        else {
                            this.addToString(this.getChar());
                            if (!this.readPI()) {
                                return -1;
                            }
                            continue;
                        }
                    }
                    else {
                        this.addToString(this.getChar());
                        if (this.xmlOpenTagsCount == 0) {
                            this.stringBufferTop = 0;
                            this.string = null;
                            this.parser.addError("msg.XML.bad.form");
                            return -1;
                        }
                        this.xmlIsTagContent = true;
                        --this.xmlOpenTagsCount;
                    }
                }
                else {
                    this.addToString(this.getChar());
                    final int peekChar2 = this.peekChar();
                    if (peekChar2 != 45) {
                        if (peekChar2 != 91) {
                            if (!this.readEntity()) {
                                return -1;
                            }
                            continue;
                        }
                        else {
                            this.addToString(this.getChar());
                            if (this.getChar() != 67 || this.getChar() != 68 || this.getChar() != 65 || this.getChar() != 84 || this.getChar() != 65 || this.getChar() != 91) {
                                this.stringBufferTop = 0;
                                this.string = null;
                                this.parser.addError("msg.XML.bad.form");
                                return -1;
                            }
                            this.addToString(67);
                            this.addToString(68);
                            this.addToString(65);
                            this.addToString(84);
                            this.addToString(65);
                            this.addToString(91);
                            if (!this.readCDATA()) {
                                return -1;
                            }
                            continue;
                        }
                    }
                    else {
                        this.addToString(this.getChar());
                        final int char2 = this.getChar();
                        if (char2 != 45) {
                            this.stringBufferTop = 0;
                            this.string = null;
                            this.parser.addError("msg.XML.bad.form");
                            return -1;
                        }
                        this.addToString(char2);
                        if (!this.readXmlComment()) {
                            return -1;
                        }
                        continue;
                    }
                }
            }
        }
    }
    
    final double getNumber() {
        return this.number;
    }
    
    final int getOffset() {
        int n = this.sourceCursor - this.lineStart;
        if (this.lineEndChar >= 0) {
            --n;
        }
        return n;
    }
    
    final char getQuoteChar() {
        return (char)this.quoteChar;
    }
    
    final String getSourceString() {
        return this.sourceString;
    }
    
    final String getString() {
        return this.string;
    }
    
    final int getToken() throws IOException {
        int char1;
        do {
            char1 = this.getChar();
            if (char1 == -1) {
                this.tokenBeg = this.cursor - 1;
                this.tokenEnd = this.cursor;
                return 0;
            }
            if (char1 == 10) {
                this.dirtyLine = false;
                this.tokenBeg = this.cursor - 1;
                this.tokenEnd = this.cursor;
                return 1;
            }
        } while (isJSSpace(char1));
        if (char1 != 45) {
            this.dirtyLine = true;
        }
        this.tokenBeg = this.cursor - 1;
        this.tokenEnd = this.cursor;
        if (char1 == 64) {
            return 147;
        }
        final boolean b = false;
        int char2;
        int n;
        boolean b2;
        if (char1 == 92) {
            char2 = this.getChar();
            if (char2 == 117) {
                n = 1;
                b2 = true;
                this.stringBufferTop = 0;
            }
            else {
                n = 0;
                this.ungetChar(char2);
                char2 = 92;
                b2 = b;
            }
        }
        else {
            final boolean javaIdentifierStart = Character.isJavaIdentifierStart((char)char1);
            char2 = char1;
            b2 = b;
            n = (javaIdentifierStart ? 1 : 0);
            if (javaIdentifierStart) {
                this.stringBufferTop = 0;
                this.addToString(char1);
                n = (javaIdentifierStart ? 1 : 0);
                b2 = b;
                char2 = char1;
            }
        }
        if (n != 0) {
            final int n2 = char2;
            int n3 = b2 ? 1 : 0;
            boolean b3 = b2;
            int n4 = n2;
            while (true) {
                if (n3 != 0) {
                    final int n5 = 0;
                    int char3 = n4;
                    int n6 = 0;
                    int xDigitToInt = n5;
                    int n7;
                    while (true) {
                        n7 = xDigitToInt;
                        if (n6 == 4) {
                            break;
                        }
                        char3 = this.getChar();
                        xDigitToInt = Kit.xDigitToInt(char3, xDigitToInt);
                        if (xDigitToInt < 0) {
                            n7 = xDigitToInt;
                            break;
                        }
                        ++n6;
                    }
                    n4 = char3;
                    if (n7 < 0) {
                        this.parser.addError("msg.invalid.escape");
                        return -1;
                    }
                    this.addToString(n7);
                    n3 = 0;
                }
                else {
                    n4 = this.getChar();
                    if (n4 == 92) {
                        n4 = this.getChar();
                        if (n4 != 117) {
                            this.parser.addError("msg.illegal.character");
                            return -1;
                        }
                        n3 = 1;
                        b3 = true;
                    }
                    else {
                        if (n4 == -1 || n4 == 65279 || !Character.isJavaIdentifierPart((char)n4)) {
                            this.ungetChar(n4);
                            final String stringFromBuffer = this.getStringFromBuffer();
                            String convertLastCharToHex;
                            if (!b3) {
                                final int stringToKeyword = stringToKeyword(stringFromBuffer);
                                if (stringToKeyword != 0) {
                                    int n8;
                                    if (stringToKeyword == 153 || (n8 = stringToKeyword) == 72) {
                                        n8 = stringToKeyword;
                                        if (this.parser.compilerEnv.getLanguageVersion() < 170) {
                                            String string;
                                            if (stringToKeyword == 153) {
                                                string = "let";
                                            }
                                            else {
                                                string = "yield";
                                            }
                                            this.string = string;
                                            n8 = 39;
                                        }
                                    }
                                    this.string = (String)this.allStrings.intern(stringFromBuffer);
                                    if (n8 != 127) {
                                        return n8;
                                    }
                                    if (!this.parser.compilerEnv.isReservedKeywordAsIdentifier()) {
                                        return n8;
                                    }
                                }
                                convertLastCharToHex = stringFromBuffer;
                            }
                            else {
                                convertLastCharToHex = stringFromBuffer;
                                if (isKeyword(stringFromBuffer)) {
                                    convertLastCharToHex = this.convertLastCharToHex(stringFromBuffer);
                                }
                            }
                            this.string = (String)this.allStrings.intern(convertLastCharToHex);
                            return 39;
                        }
                        this.addToString(n4);
                    }
                }
            }
        }
        else {
            if (isDigit(char2) || (char2 == 46 && isDigit(this.peekChar()))) {
                this.isOctal = false;
                this.stringBufferTop = 0;
                final int n9 = 10;
                this.isOctal = false;
                this.isHex = false;
                int n10 = char2;
                int n11 = n9;
                if (char2 == 48) {
                    n10 = this.getChar();
                    if (n10 != 120 && n10 != 88) {
                        if (isDigit(n10)) {
                            n11 = 8;
                            this.isOctal = true;
                        }
                        else {
                            this.addToString(48);
                            n11 = n9;
                        }
                    }
                    else {
                        n11 = 16;
                        this.isHex = true;
                        n10 = this.getChar();
                    }
                }
                int char4 = n10;
                int n12;
                int char5;
                int n13;
                if ((n12 = n11) == 16) {
                    while (true) {
                        char5 = n10;
                        n13 = n11;
                        if (Kit.xDigitToInt(n10, 0) < 0) {
                            break;
                        }
                        this.addToString(n10);
                        n10 = this.getChar();
                    }
                }
                else {
                    while (true) {
                        char5 = char4;
                        n13 = n12;
                        if (48 > char4) {
                            break;
                        }
                        char5 = char4;
                        n13 = n12;
                        if (char4 > 57) {
                            break;
                        }
                        if (n12 == 8 && char4 >= 56) {
                            final Parser parser = this.parser;
                            String s;
                            if (char4 == 56) {
                                s = "8";
                            }
                            else {
                                s = "9";
                            }
                            parser.addWarning("msg.bad.octal.literal", s);
                            n12 = 10;
                        }
                        this.addToString(char4);
                        char4 = this.getChar();
                    }
                }
                boolean b5 = false;
                Label_2444: {
                    if (n13 == 10 && (char5 == 46 || char5 == 101 || char5 == 69)) {
                        final boolean b4 = false;
                        int n14;
                        if ((n14 = char5) == 46) {
                            do {
                                this.addToString(char5);
                                n14 = (char5 = this.getChar());
                            } while (isDigit(n14));
                        }
                        if (n14 != 101) {
                            char5 = n14;
                            b5 = b4;
                            if (n14 != 69) {
                                break Label_2444;
                            }
                        }
                        this.addToString(n14);
                        final int char6 = this.getChar();
                        int char7;
                        if (char6 == 43 || (char7 = char6) == 45) {
                            this.addToString(char6);
                            char7 = this.getChar();
                        }
                        int char8 = char7;
                        if (!isDigit(char7)) {
                            this.parser.addError("msg.missing.exponent");
                            return -1;
                        }
                        int n15;
                        do {
                            this.addToString(char8);
                            n15 = (char8 = this.getChar());
                        } while (isDigit(n15));
                        char5 = n15;
                        b5 = b4;
                    }
                    else {
                        b5 = true;
                    }
                }
                this.ungetChar(char5);
                final String stringFromBuffer2 = this.getStringFromBuffer();
                this.string = stringFromBuffer2;
                double number = 0.0;
                Label_2506: {
                    if (n13 == 10 && !b5) {
                        try {
                            number = Double.parseDouble(stringFromBuffer2);
                            break Label_2506;
                        }
                        catch (NumberFormatException ex) {
                            this.parser.addError("msg.caught.nfe");
                            return -1;
                        }
                    }
                    number = ScriptRuntime.stringToNumber(stringFromBuffer2, 0, n13);
                }
                this.number = number;
                return 40;
            }
            if (char2 == 34 || char2 == 39) {
                this.quoteChar = char2;
                this.stringBufferTop = 0;
                int i = this.getChar(false);
            Label_1518:
                while (i != this.quoteChar) {
                    if (i == 10 || i == -1) {
                        this.ungetChar(i);
                        this.tokenEnd = this.cursor;
                        this.parser.addError("msg.unterminated.string.lit");
                        return -1;
                    }
                    int xDigitToInt2 = 0;
                    Label_1952: {
                        int n16;
                        if ((n16 = i) == 92) {
                            final int char9 = this.getChar();
                            if (char9 == 10) {
                                i = this.getChar();
                                continue;
                            }
                            if (char9 == 98) {
                                xDigitToInt2 = 8;
                                break Label_1952;
                            }
                            if (char9 == 102) {
                                xDigitToInt2 = 12;
                                break Label_1952;
                            }
                            if (char9 == 110) {
                                xDigitToInt2 = 10;
                                break Label_1952;
                            }
                            if (char9 == 114) {
                                xDigitToInt2 = 13;
                                break Label_1952;
                            }
                            if (char9 != 120) {
                                switch (char9) {
                                    default: {
                                        n16 = char9;
                                        if (48 <= char9 && (n16 = char9) < 56) {
                                            final int n17 = char9 - 48;
                                            int n19;
                                            final int n18 = n19 = this.getChar();
                                            int n20 = n17;
                                            if (48 <= n18) {
                                                n19 = n18;
                                                n20 = n17;
                                                if (n18 < 56) {
                                                    final int n21 = n17 * 8 + n18 - 48;
                                                    final int n22 = n19 = this.getChar();
                                                    n20 = n21;
                                                    if (48 <= n22) {
                                                        n19 = n22;
                                                        n20 = n21;
                                                        if (n22 < 56) {
                                                            n19 = n22;
                                                            if ((n20 = n21) <= 31) {
                                                                n20 = n21 * 8 + n22 - 48;
                                                                n19 = this.getChar();
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                            this.ungetChar(n19);
                                            n16 = n20;
                                            break;
                                        }
                                        break;
                                    }
                                    case 118: {
                                        n16 = 11;
                                        break;
                                    }
                                    case 117: {
                                        final int stringBufferTop = this.stringBufferTop;
                                        this.addToString(117);
                                        xDigitToInt2 = 0;
                                        for (int j = 0; j != 4; ++j) {
                                            final int char10 = this.getChar();
                                            xDigitToInt2 = Kit.xDigitToInt(char10, xDigitToInt2);
                                            if (xDigitToInt2 < 0) {
                                                i = char10;
                                                continue Label_1518;
                                            }
                                            this.addToString(char10);
                                        }
                                        this.stringBufferTop = stringBufferTop;
                                        break Label_1952;
                                    }
                                    case 116: {
                                        xDigitToInt2 = 9;
                                        break Label_1952;
                                    }
                                }
                            }
                            else {
                                i = this.getChar();
                                final int xDigitToInt3 = Kit.xDigitToInt(i, 0);
                                if (xDigitToInt3 < 0) {
                                    this.addToString(120);
                                    continue;
                                }
                                final int char11 = this.getChar();
                                final int xDigitToInt4 = Kit.xDigitToInt(char11, xDigitToInt3);
                                if (xDigitToInt4 < 0) {
                                    this.addToString(120);
                                    this.addToString(i);
                                    i = char11;
                                    continue;
                                }
                                xDigitToInt2 = xDigitToInt4;
                                break Label_1952;
                            }
                        }
                        xDigitToInt2 = n16;
                    }
                    this.addToString(xDigitToInt2);
                    i = this.getChar(false);
                }
                this.string = (String)this.allStrings.intern(this.getStringFromBuffer());
                return 41;
            }
            if (char2 != 33) {
                if (char2 == 91) {
                    return 83;
                }
                switch (char2) {
                    default: {
                        switch (char2) {
                            default: {
                                switch (char2) {
                                    default: {
                                        switch (char2) {
                                            default: {
                                                switch (char2) {
                                                    default: {
                                                        this.parser.addError("msg.illegal.character");
                                                        return -1;
                                                    }
                                                    case 126: {
                                                        return 27;
                                                    }
                                                    case 125: {
                                                        return 86;
                                                    }
                                                    case 124: {
                                                        if (this.matchChar(124)) {
                                                            return 104;
                                                        }
                                                        if (this.matchChar(61)) {
                                                            return 91;
                                                        }
                                                        return 9;
                                                    }
                                                    case 123: {
                                                        return 85;
                                                    }
                                                }
                                                break;
                                            }
                                            case 94: {
                                                if (this.matchChar(61)) {
                                                    return 92;
                                                }
                                                return 10;
                                            }
                                            case 93: {
                                                return 84;
                                            }
                                        }
                                        break;
                                    }
                                    case 63: {
                                        return 102;
                                    }
                                    case 62: {
                                        if (this.matchChar(62)) {
                                            if (this.matchChar(62)) {
                                                if (this.matchChar(61)) {
                                                    return 96;
                                                }
                                                return 20;
                                            }
                                            else {
                                                if (this.matchChar(61)) {
                                                    return 95;
                                                }
                                                return 19;
                                            }
                                        }
                                        else {
                                            if (this.matchChar(61)) {
                                                return 17;
                                            }
                                            return 16;
                                        }
                                        break;
                                    }
                                    case 61: {
                                        if (!this.matchChar(61)) {
                                            return 90;
                                        }
                                        if (this.matchChar(61)) {
                                            return 46;
                                        }
                                        return 12;
                                    }
                                    case 60: {
                                        if (this.matchChar(33)) {
                                            if (this.matchChar(45)) {
                                                if (this.matchChar(45)) {
                                                    this.tokenBeg = this.cursor - 4;
                                                    this.skipLine();
                                                    this.commentType = Token.CommentType.HTML;
                                                    return 161;
                                                }
                                                this.ungetCharIgnoreLineEnd(45);
                                            }
                                            this.ungetCharIgnoreLineEnd(33);
                                        }
                                        if (this.matchChar(60)) {
                                            if (this.matchChar(61)) {
                                                return 94;
                                            }
                                            return 18;
                                        }
                                        else {
                                            if (this.matchChar(61)) {
                                                return 15;
                                            }
                                            return 14;
                                        }
                                        break;
                                    }
                                    case 59: {
                                        return 82;
                                    }
                                    case 58: {
                                        if (this.matchChar(58)) {
                                            return 144;
                                        }
                                        return 103;
                                    }
                                }
                                break;
                            }
                            case 47: {
                                this.markCommentStart();
                                if (this.matchChar(47)) {
                                    this.tokenBeg = this.cursor - 2;
                                    this.skipLine();
                                    this.commentType = Token.CommentType.LINE;
                                    return 161;
                                }
                                if (this.matchChar(42)) {
                                    int n23 = 0;
                                    this.tokenBeg = this.cursor - 2;
                                    if (this.matchChar(42)) {
                                        n23 = 1;
                                        this.commentType = Token.CommentType.JSDOC;
                                    }
                                    else {
                                        this.commentType = Token.CommentType.BLOCK_COMMENT;
                                    }
                                    while (true) {
                                        final int char12 = this.getChar();
                                        if (char12 == -1) {
                                            this.tokenEnd = this.cursor - 1;
                                            this.parser.addError("msg.unterminated.comment");
                                            return 161;
                                        }
                                        if (char12 == 42) {
                                            n23 = 1;
                                        }
                                        else if (char12 == 47) {
                                            if (n23 != 0) {
                                                this.tokenEnd = this.cursor;
                                                return 161;
                                            }
                                            continue;
                                        }
                                        else {
                                            n23 = 0;
                                            this.tokenEnd = this.cursor;
                                        }
                                    }
                                }
                                else {
                                    if (this.matchChar(61)) {
                                        return 100;
                                    }
                                    return 24;
                                }
                                break;
                            }
                            case 46: {
                                if (this.matchChar(46)) {
                                    return 143;
                                }
                                if (this.matchChar(40)) {
                                    return 146;
                                }
                                return 108;
                            }
                            case 45: {
                                int n24;
                                if (this.matchChar(61)) {
                                    n24 = 98;
                                }
                                else if (this.matchChar(45)) {
                                    if (!this.dirtyLine && this.matchChar(62)) {
                                        this.markCommentStart("--");
                                        this.skipLine();
                                        this.commentType = Token.CommentType.HTML;
                                        return 161;
                                    }
                                    n24 = 107;
                                }
                                else {
                                    n24 = 22;
                                }
                                this.dirtyLine = true;
                                return n24;
                            }
                            case 44: {
                                return 89;
                            }
                            case 43: {
                                if (this.matchChar(61)) {
                                    return 97;
                                }
                                if (this.matchChar(43)) {
                                    return 106;
                                }
                                return 21;
                            }
                            case 42: {
                                if (this.matchChar(61)) {
                                    return 99;
                                }
                                return 23;
                            }
                            case 41: {
                                return 88;
                            }
                            case 40: {
                                return 87;
                            }
                        }
                        break;
                    }
                    case 38: {
                        if (this.matchChar(38)) {
                            return 105;
                        }
                        if (this.matchChar(61)) {
                            return 93;
                        }
                        return 11;
                    }
                    case 37: {
                        if (this.matchChar(61)) {
                            return 101;
                        }
                        return 25;
                    }
                }
            }
            else {
                if (!this.matchChar(61)) {
                    return 26;
                }
                if (this.matchChar(61)) {
                    return 47;
                }
                return 13;
            }
        }
    }
    
    public int getTokenBeg() {
        return this.tokenBeg;
    }
    
    public int getTokenEnd() {
        return this.tokenEnd;
    }
    
    public int getTokenLength() {
        return this.tokenEnd - this.tokenBeg;
    }
    
    final boolean isNumberHex() {
        return this.isHex;
    }
    
    final boolean isNumberOctal() {
        return this.isOctal;
    }
    
    boolean isXMLAttribute() {
        return this.xmlIsAttribute;
    }
    
    String readAndClearRegExpFlags() {
        final String regExpFlags = this.regExpFlags;
        this.regExpFlags = null;
        return regExpFlags;
    }
    
    void readRegExp(int stringBufferTop) throws IOException {
        final int tokenBeg = this.tokenBeg;
        this.stringBufferTop = 0;
        if (stringBufferTop == 100) {
            this.addToString(61);
        }
        else if (stringBufferTop != 24) {
            Kit.codeBug();
        }
        stringBufferTop = 0;
        int char1;
        while ((char1 = this.getChar()) != 47 || stringBufferTop != 0) {
            if (char1 == 10 || char1 == -1) {
                this.ungetChar(char1);
                this.tokenEnd = this.cursor - 1;
                this.string = new String(this.stringBuffer, 0, this.stringBufferTop);
                this.parser.reportError("msg.unterminated.re.lit");
                return;
            }
            int char2;
            if (char1 == 92) {
                this.addToString(char1);
                char2 = this.getChar();
            }
            else if (char1 == 91) {
                stringBufferTop = 1;
                char2 = char1;
            }
            else if ((char2 = char1) == 93) {
                stringBufferTop = 0;
                char2 = char1;
            }
            this.addToString(char2);
        }
        stringBufferTop = this.stringBufferTop;
        while (true) {
            if (this.matchChar(103)) {
                this.addToString(103);
            }
            else if (this.matchChar(105)) {
                this.addToString(105);
            }
            else if (this.matchChar(109)) {
                this.addToString(109);
            }
            else {
                if (!this.matchChar(121)) {
                    break;
                }
                this.addToString(121);
            }
        }
        this.tokenEnd = this.stringBufferTop + tokenBeg + 2;
        if (isAlpha(this.peekChar())) {
            this.parser.reportError("msg.invalid.re.flag");
        }
        this.string = new String(this.stringBuffer, 0, stringBufferTop);
        this.regExpFlags = new String(this.stringBuffer, stringBufferTop, this.stringBufferTop - stringBufferTop);
    }
    
    String tokenToString(final int n) {
        return "";
    }
}
