package com.google.gson.stream;

import com.google.gson.internal.*;
import com.google.gson.internal.bind.*;
import java.io.*;

public class JsonReader implements Closeable
{
    private static final long MIN_INCOMPLETE_INTEGER = -922337203685477580L;
    private static final char[] NON_EXECUTE_PREFIX;
    private static final int NUMBER_CHAR_DECIMAL = 3;
    private static final int NUMBER_CHAR_DIGIT = 2;
    private static final int NUMBER_CHAR_EXP_DIGIT = 7;
    private static final int NUMBER_CHAR_EXP_E = 5;
    private static final int NUMBER_CHAR_EXP_SIGN = 6;
    private static final int NUMBER_CHAR_FRACTION_DIGIT = 4;
    private static final int NUMBER_CHAR_NONE = 0;
    private static final int NUMBER_CHAR_SIGN = 1;
    private static final int PEEKED_BEGIN_ARRAY = 3;
    private static final int PEEKED_BEGIN_OBJECT = 1;
    private static final int PEEKED_BUFFERED = 11;
    private static final int PEEKED_DOUBLE_QUOTED = 9;
    private static final int PEEKED_DOUBLE_QUOTED_NAME = 13;
    private static final int PEEKED_END_ARRAY = 4;
    private static final int PEEKED_END_OBJECT = 2;
    private static final int PEEKED_EOF = 17;
    private static final int PEEKED_FALSE = 6;
    private static final int PEEKED_LONG = 15;
    private static final int PEEKED_NONE = 0;
    private static final int PEEKED_NULL = 7;
    private static final int PEEKED_NUMBER = 16;
    private static final int PEEKED_SINGLE_QUOTED = 8;
    private static final int PEEKED_SINGLE_QUOTED_NAME = 12;
    private static final int PEEKED_TRUE = 5;
    private static final int PEEKED_UNQUOTED = 10;
    private static final int PEEKED_UNQUOTED_NAME = 14;
    private final char[] buffer;
    private final Reader in;
    private boolean lenient;
    private int limit;
    private int lineNumber;
    private int lineStart;
    private int[] pathIndices;
    private String[] pathNames;
    int peeked;
    private long peekedLong;
    private int peekedNumberLength;
    private String peekedString;
    private int pos;
    private int[] stack;
    private int stackSize;
    
    static {
        NON_EXECUTE_PREFIX = ")]}'\n".toCharArray();
        JsonReaderInternalAccess.INSTANCE = new JsonReaderInternalAccess() {
            @Override
            public void promoteNameToValue(final JsonReader jsonReader) throws IOException {
                if (jsonReader instanceof JsonTreeReader) {
                    ((JsonTreeReader)jsonReader).promoteNameToValue();
                    return;
                }
                int n;
                if ((n = jsonReader.peeked) == 0) {
                    n = jsonReader.doPeek();
                }
                if (n == 13) {
                    jsonReader.peeked = 9;
                    return;
                }
                if (n == 12) {
                    jsonReader.peeked = 8;
                    return;
                }
                if (n == 14) {
                    jsonReader.peeked = 10;
                    return;
                }
                final StringBuilder sb = new StringBuilder();
                sb.append("Expected a name but was ");
                sb.append(jsonReader.peek());
                sb.append(" ");
                sb.append(" at line ");
                sb.append(jsonReader.getLineNumber());
                sb.append(" column ");
                sb.append(jsonReader.getColumnNumber());
                sb.append(" path ");
                sb.append(jsonReader.getPath());
                throw new IllegalStateException(sb.toString());
            }
        };
    }
    
    public JsonReader(final Reader in) {
        this.lenient = false;
        this.buffer = new char[1024];
        this.pos = 0;
        this.limit = 0;
        this.lineNumber = 0;
        this.lineStart = 0;
        this.peeked = 0;
        this.stack = new int[32];
        this.stackSize = 0;
        this.stack[this.stackSize++] = 6;
        this.pathNames = new String[32];
        this.pathIndices = new int[32];
        if (in == null) {
            throw new NullPointerException("in == null");
        }
        this.in = in;
    }
    
    private void checkLenient() throws IOException {
        if (!this.lenient) {
            throw this.syntaxError("Use JsonReader.setLenient(true) to accept malformed JSON");
        }
    }
    
    private void consumeNonExecutePrefix() throws IOException {
        this.nextNonWhitespace(true);
        --this.pos;
        if (this.pos + JsonReader.NON_EXECUTE_PREFIX.length > this.limit && !this.fillBuffer(JsonReader.NON_EXECUTE_PREFIX.length)) {
            return;
        }
        for (int i = 0; i < JsonReader.NON_EXECUTE_PREFIX.length; ++i) {
            if (this.buffer[this.pos + i] != JsonReader.NON_EXECUTE_PREFIX[i]) {
                return;
            }
        }
        this.pos += JsonReader.NON_EXECUTE_PREFIX.length;
    }
    
    private boolean fillBuffer(int n) throws IOException {
        final char[] buffer = this.buffer;
        this.lineStart -= this.pos;
        if (this.limit != this.pos) {
            this.limit -= this.pos;
            System.arraycopy(buffer, this.pos, buffer, 0, this.limit);
        }
        else {
            this.limit = 0;
        }
        this.pos = 0;
        int n2;
        do {
            final int read = this.in.read(buffer, this.limit, buffer.length - this.limit);
            if (read == -1) {
                return false;
            }
            this.limit += read;
            n2 = n;
            if (this.lineNumber != 0) {
                continue;
            }
            n2 = n;
            if (this.lineStart != 0) {
                continue;
            }
            n2 = n;
            if (this.limit <= 0) {
                continue;
            }
            n2 = n;
            if (buffer[0] != '\ufeff') {
                continue;
            }
            ++this.pos;
            ++this.lineStart;
            n2 = n + 1;
        } while (this.limit < (n = n2));
        return true;
    }
    
    private boolean isLiteral(final char c) throws IOException {
        switch (c) {
            default: {
                return true;
            }
            case '#':
            case '/':
            case ';':
            case '=':
            case '\\': {
                this.checkLenient();
            }
            case '\t':
            case '\n':
            case '\f':
            case '\r':
            case ' ':
            case ',':
            case ':':
            case '[':
            case ']':
            case '{':
            case '}': {
                return false;
            }
        }
    }
    
    private int nextNonWhitespace(final boolean b) throws IOException {
        final char[] buffer = this.buffer;
        int pos = this.pos;
        int n = this.limit;
        while (true) {
            int pos2 = pos;
            int limit = n;
            if (pos == n) {
                this.pos = pos;
                if (!this.fillBuffer(1)) {
                    if (b) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("End of input at line ");
                        sb.append(this.getLineNumber());
                        sb.append(" column ");
                        sb.append(this.getColumnNumber());
                        throw new EOFException(sb.toString());
                    }
                    return -1;
                }
                else {
                    pos2 = this.pos;
                    limit = this.limit;
                }
            }
            pos = pos2 + 1;
            final char c = buffer[pos2];
            if (c == '\n') {
                ++this.lineNumber;
                this.lineStart = pos;
            }
            else if (c != ' ' && c != '\r') {
                if (c != '\t') {
                    if (c == '/') {
                        if ((this.pos = pos) == limit) {
                            --this.pos;
                            final boolean fillBuffer = this.fillBuffer(2);
                            ++this.pos;
                            if (!fillBuffer) {
                                return c;
                            }
                        }
                        this.checkLenient();
                        final char c2 = buffer[this.pos];
                        if (c2 != '*') {
                            if (c2 != '/') {
                                return c;
                            }
                            ++this.pos;
                            this.skipToEndOfLine();
                            pos = this.pos;
                            n = this.limit;
                        }
                        else {
                            ++this.pos;
                            if (!this.skipTo("*/")) {
                                throw this.syntaxError("Unterminated comment");
                            }
                            pos = this.pos + 2;
                            n = this.limit;
                        }
                    }
                    else {
                        if (c != '#') {
                            this.pos = pos;
                            return c;
                        }
                        this.pos = pos;
                        this.checkLenient();
                        this.skipToEndOfLine();
                        pos = this.pos;
                        n = this.limit;
                    }
                    continue;
                }
            }
            n = limit;
        }
    }
    
    private String nextQuotedValue(final char c) throws IOException {
        final char[] buffer = this.buffer;
        final StringBuilder sb = new StringBuilder();
        while (true) {
            int pos = this.pos;
            int limit;
            int i;
            int pos2;
            int limit2;
            for (limit = this.limit, i = pos; i < limit; i = pos2, limit = limit2) {
                final int lineStart = i + 1;
                final char c2 = buffer[i];
                if (c2 == c) {
                    sb.append(buffer, pos, (this.pos = lineStart) - pos - 1);
                    return sb.toString();
                }
                if (c2 == '\\') {
                    sb.append(buffer, pos, (this.pos = lineStart) - pos - 1);
                    sb.append(this.readEscapeCharacter());
                    pos2 = this.pos;
                    limit2 = this.limit;
                    pos = pos2;
                }
                else {
                    if (c2 == '\n') {
                        ++this.lineNumber;
                        this.lineStart = lineStart;
                    }
                    final int n = lineStart;
                    limit2 = limit;
                    pos2 = n;
                }
            }
            sb.append(buffer, pos, i - pos);
            this.pos = i;
            if (!this.fillBuffer(1)) {
                throw this.syntaxError("Unterminated string");
            }
        }
    }
    
    private String nextUnquotedValue() throws IOException {
        StringBuilder sb = null;
        int n = 0;
        StringBuilder sb2 = null;
        int n2 = 0;
        Label_0270: {
            Label_0179: {
            Label_0175:
                while (true) {
                    if (this.pos + n < this.limit) {
                        switch (this.buffer[this.pos + n]) {
                            default: {
                                ++n;
                                continue;
                            }
                            case '#':
                            case '/':
                            case ';':
                            case '=':
                            case '\\': {
                                break Label_0175;
                            }
                            case '\t':
                            case '\n':
                            case '\f':
                            case '\r':
                            case ' ':
                            case ',':
                            case ':':
                            case '[':
                            case ']':
                            case '{':
                            case '}': {
                                break Label_0179;
                            }
                        }
                    }
                    else if (n < this.buffer.length) {
                        sb2 = sb;
                        n2 = n;
                        if (this.fillBuffer(n + 1)) {
                            continue;
                        }
                        break Label_0270;
                    }
                    else {
                        if ((sb2 = sb) == null) {
                            sb2 = new StringBuilder();
                        }
                        sb2.append(this.buffer, this.pos, n);
                        this.pos += n;
                        n2 = 0;
                        n = 0;
                        sb = sb2;
                        if (!this.fillBuffer(1)) {
                            break Label_0270;
                        }
                        continue;
                    }
                }
                this.checkLenient();
            }
            sb2 = sb;
            n2 = n;
        }
        String string;
        if (sb2 == null) {
            string = new String(this.buffer, this.pos, n2);
        }
        else {
            sb2.append(this.buffer, this.pos, n2);
            string = sb2.toString();
        }
        this.pos += n2;
        return string;
    }
    
    private int peekKeyword() throws IOException {
        final char c = this.buffer[this.pos];
        String s;
        String s2;
        int peeked;
        if (c != 't' && c != 'T') {
            if (c != 'f' && c != 'F') {
                if (c != 'n' && c != 'N') {
                    return 0;
                }
                s = "null";
                s2 = "NULL";
                peeked = 7;
            }
            else {
                s = "false";
                s2 = "FALSE";
                peeked = 6;
            }
        }
        else {
            s = "true";
            s2 = "TRUE";
            peeked = 5;
        }
        final int length = s.length();
        for (int i = 1; i < length; ++i) {
            if (this.pos + i >= this.limit && !this.fillBuffer(i + 1)) {
                return 0;
            }
            final char c2 = this.buffer[this.pos + i];
            if (c2 != s.charAt(i) && c2 != s2.charAt(i)) {
                return 0;
            }
        }
        if ((this.pos + length < this.limit || this.fillBuffer(length + 1)) && this.isLiteral(this.buffer[this.pos + length])) {
            return 0;
        }
        this.pos += length;
        return this.peeked = peeked;
    }
    
    private int peekNumber() throws IOException {
        final char[] buffer = this.buffer;
        int pos = this.pos;
        int limit = this.limit;
        boolean b = false;
        boolean b2 = true;
        int n = 0;
        long peekedLong = 0L;
        int peekedNumberLength = 0;
    Label_0292:
        while (true) {
            int pos2 = pos;
            int limit2 = limit;
            if (pos + peekedNumberLength == limit) {
                if (peekedNumberLength == buffer.length) {
                    return 0;
                }
                if (!this.fillBuffer(peekedNumberLength + 1)) {
                    break;
                }
                pos2 = this.pos;
                limit2 = this.limit;
            }
            final char c = buffer[pos2 + peekedNumberLength];
            if (c != '+') {
                if (c != 'E' && c != 'e') {
                    switch (c) {
                        default: {
                            if (c >= '0' && c <= '9') {
                                final boolean b3 = true;
                                if (n == 1 || n == 0) {
                                    peekedLong = -(c - '0');
                                    n = 2;
                                    break;
                                }
                                if (n != 2) {
                                    int n2;
                                    if (n == 3) {
                                        n2 = 4;
                                    }
                                    else if (n == 5 || (n2 = n) == 6) {
                                        n2 = 7;
                                    }
                                    n = n2;
                                    break;
                                }
                                if (peekedLong == 0L) {
                                    return 0;
                                }
                                final long n3 = 10L * peekedLong - (c - '0');
                                boolean b4 = b3;
                                if (peekedLong <= -922337203685477580L) {
                                    b4 = (peekedLong == -922337203685477580L && n3 < peekedLong && b3);
                                }
                                b2 &= b4;
                                peekedLong = n3;
                                break;
                            }
                            else {
                                if (!this.isLiteral(c)) {
                                    break Label_0292;
                                }
                                return 0;
                            }
                            break;
                        }
                        case 46: {
                            if (n == 2) {
                                n = 3;
                                break;
                            }
                            return 0;
                        }
                        case 45: {
                            if (n == 0) {
                                b = true;
                                n = 1;
                                break;
                            }
                            if (n == 5) {
                                n = 6;
                                break;
                            }
                            return 0;
                        }
                    }
                }
                else {
                    if (n != 2 && n != 4) {
                        return 0;
                    }
                    n = 5;
                }
            }
            else {
                if (n != 5) {
                    return 0;
                }
                n = 6;
            }
            ++peekedNumberLength;
            pos = pos2;
            limit = limit2;
        }
        if (n == 2 && b2 && (peekedLong != Long.MIN_VALUE || b)) {
            if (!b) {
                peekedLong = -peekedLong;
            }
            this.peekedLong = peekedLong;
            this.pos += peekedNumberLength;
            return this.peeked = 15;
        }
        if (n != 2 && n != 4 && n != 7) {
            return 0;
        }
        this.peekedNumberLength = peekedNumberLength;
        return this.peeked = 16;
    }
    
    private void push(final int n) {
        if (this.stackSize == this.stack.length) {
            final int[] stack = new int[this.stackSize * 2];
            final int[] pathIndices = new int[this.stackSize * 2];
            final String[] pathNames = new String[this.stackSize * 2];
            System.arraycopy(this.stack, 0, stack, 0, this.stackSize);
            System.arraycopy(this.pathIndices, 0, pathIndices, 0, this.stackSize);
            System.arraycopy(this.pathNames, 0, pathNames, 0, this.stackSize);
            this.stack = stack;
            this.pathIndices = pathIndices;
            this.pathNames = pathNames;
        }
        this.stack[this.stackSize++] = n;
    }
    
    private char readEscapeCharacter() throws IOException {
        if (this.pos == this.limit && !this.fillBuffer(1)) {
            throw this.syntaxError("Unterminated escape sequence");
        }
        final char c = this.buffer[this.pos++];
        if (c == '\n') {
            ++this.lineNumber;
            this.lineStart = this.pos;
            return c;
        }
        if (c == 'b') {
            return '\b';
        }
        if (c == 'f') {
            return '\f';
        }
        if (c == 'n') {
            return '\n';
        }
        if (c == 'r') {
            return '\r';
        }
        switch (c) {
            default: {
                return c;
            }
            case 'u': {
                if (this.pos + 4 > this.limit && !this.fillBuffer(4)) {
                    throw this.syntaxError("Unterminated escape sequence");
                }
                char c2 = '\0';
                int i = 0;
                while (i < (i = this.pos) + 4) {
                    final char c3 = this.buffer[i];
                    final char c4 = (char)(c2 << 4);
                    if (c3 >= '0' && c3 <= '9') {
                        c2 = (char)(c3 - '0' + c4);
                    }
                    else if (c3 >= 'a' && c3 <= 'f') {
                        c2 = (char)(c3 - 'a' + 10 + c4);
                    }
                    else {
                        if (c3 < 'A' || c3 > 'F') {
                            final StringBuilder sb = new StringBuilder();
                            sb.append("\\u");
                            sb.append(new String(this.buffer, this.pos, 4));
                            throw new NumberFormatException(sb.toString());
                        }
                        c2 = (char)(c3 - 'A' + 10 + c4);
                    }
                    ++i;
                }
                this.pos += 4;
                return c2;
            }
            case 't': {
                return '\t';
            }
        }
    }
    
    private void skipQuotedValue(final char c) throws IOException {
        final char[] buffer = this.buffer;
        do {
            int i = this.pos;
            int pos;
            int limit2;
            for (int limit = this.limit; i < limit; i = pos, limit = limit2) {
                final int lineStart = i + 1;
                final char c2 = buffer[i];
                if (c2 == c) {
                    this.pos = lineStart;
                    return;
                }
                if (c2 == '\\') {
                    this.pos = lineStart;
                    this.readEscapeCharacter();
                    pos = this.pos;
                    limit2 = this.limit;
                }
                else {
                    if (c2 == '\n') {
                        ++this.lineNumber;
                        this.lineStart = lineStart;
                    }
                    final int n = lineStart;
                    limit2 = limit;
                    pos = n;
                }
            }
            this.pos = i;
        } while (this.fillBuffer(1));
        throw this.syntaxError("Unterminated string");
    }
    
    private boolean skipTo(final String s) throws IOException {
        while (true) {
            final int pos = this.pos;
            final int length = s.length();
            final int limit = this.limit;
            int i = 0;
            if (pos + length > limit && !this.fillBuffer(s.length())) {
                return false;
            }
            Label_0108: {
                if (this.buffer[this.pos] != '\n') {
                    while (i < s.length()) {
                        if (this.buffer[this.pos + i] != s.charAt(i)) {
                            break Label_0108;
                        }
                        ++i;
                    }
                    return true;
                }
                ++this.lineNumber;
                this.lineStart = this.pos + 1;
            }
            ++this.pos;
        }
    }
    
    private void skipToEndOfLine() throws IOException {
        while (this.pos < this.limit || this.fillBuffer(1)) {
            final char c = this.buffer[this.pos++];
            if (c == '\n') {
                ++this.lineNumber;
                this.lineStart = this.pos;
                return;
            }
            if (c == '\r') {
                break;
            }
        }
    }
    
    private void skipUnquotedValue() throws IOException {
        do {
            int n = 0;
            while (this.pos + n < this.limit) {
                switch (this.buffer[this.pos + n]) {
                    default: {
                        ++n;
                        continue;
                    }
                    case '#':
                    case '/':
                    case ';':
                    case '=':
                    case '\\': {
                        this.checkLenient();
                    }
                    case '\t':
                    case '\n':
                    case '\f':
                    case '\r':
                    case ' ':
                    case ',':
                    case ':':
                    case '[':
                    case ']':
                    case '{':
                    case '}': {
                        this.pos += n;
                        return;
                    }
                }
            }
            this.pos += n;
        } while (this.fillBuffer(1));
    }
    
    private IOException syntaxError(final String s) throws IOException {
        final StringBuilder sb = new StringBuilder();
        sb.append(s);
        sb.append(" at line ");
        sb.append(this.getLineNumber());
        sb.append(" column ");
        sb.append(this.getColumnNumber());
        sb.append(" path ");
        sb.append(this.getPath());
        throw new MalformedJsonException(sb.toString());
    }
    
    public void beginArray() throws IOException {
        int n;
        if ((n = this.peeked) == 0) {
            n = this.doPeek();
        }
        if (n == 3) {
            this.push(1);
            this.pathIndices[this.stackSize - 1] = 0;
            this.peeked = 0;
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Expected BEGIN_ARRAY but was ");
        sb.append(this.peek());
        sb.append(" at line ");
        sb.append(this.getLineNumber());
        sb.append(" column ");
        sb.append(this.getColumnNumber());
        sb.append(" path ");
        sb.append(this.getPath());
        throw new IllegalStateException(sb.toString());
    }
    
    public void beginObject() throws IOException {
        int n;
        if ((n = this.peeked) == 0) {
            n = this.doPeek();
        }
        if (n == 1) {
            this.push(3);
            this.peeked = 0;
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Expected BEGIN_OBJECT but was ");
        sb.append(this.peek());
        sb.append(" at line ");
        sb.append(this.getLineNumber());
        sb.append(" column ");
        sb.append(this.getColumnNumber());
        sb.append(" path ");
        sb.append(this.getPath());
        throw new IllegalStateException(sb.toString());
    }
    
    @Override
    public void close() throws IOException {
        this.peeked = 0;
        this.stack[0] = 8;
        this.stackSize = 1;
        this.in.close();
    }
    
    int doPeek() throws IOException {
        final int n = this.stack[this.stackSize - 1];
        if (n == 1) {
            this.stack[this.stackSize - 1] = 2;
        }
        else if (n == 2) {
            final int nextNonWhitespace = this.nextNonWhitespace(true);
            if (nextNonWhitespace != 44) {
                if (nextNonWhitespace != 59) {
                    if (nextNonWhitespace != 93) {
                        throw this.syntaxError("Unterminated array");
                    }
                    return this.peeked = 4;
                }
                else {
                    this.checkLenient();
                }
            }
        }
        else if (n != 3 && n != 5) {
            if (n == 4) {
                this.stack[this.stackSize - 1] = 5;
                final int nextNonWhitespace2 = this.nextNonWhitespace(true);
                if (nextNonWhitespace2 != 58) {
                    if (nextNonWhitespace2 != 61) {
                        throw this.syntaxError("Expected ':'");
                    }
                    this.checkLenient();
                    if ((this.pos < this.limit || this.fillBuffer(1)) && this.buffer[this.pos] == '>') {
                        ++this.pos;
                    }
                }
            }
            else if (n == 6) {
                if (this.lenient) {
                    this.consumeNonExecutePrefix();
                }
                this.stack[this.stackSize - 1] = 7;
            }
            else if (n == 7) {
                if (this.nextNonWhitespace(false) == -1) {
                    return this.peeked = 17;
                }
                this.checkLenient();
                --this.pos;
            }
            else if (n == 8) {
                throw new IllegalStateException("JsonReader is closed");
            }
        }
        else {
            this.stack[this.stackSize - 1] = 4;
            if (n == 5) {
                final int nextNonWhitespace3 = this.nextNonWhitespace(true);
                if (nextNonWhitespace3 != 44) {
                    if (nextNonWhitespace3 != 59) {
                        if (nextNonWhitespace3 != 125) {
                            throw this.syntaxError("Unterminated object");
                        }
                        return this.peeked = 2;
                    }
                    else {
                        this.checkLenient();
                    }
                }
            }
            final int nextNonWhitespace4 = this.nextNonWhitespace(true);
            if (nextNonWhitespace4 == 34) {
                return this.peeked = 13;
            }
            if (nextNonWhitespace4 == 39) {
                this.checkLenient();
                return this.peeked = 12;
            }
            if (nextNonWhitespace4 != 125) {
                this.checkLenient();
                --this.pos;
                if (this.isLiteral((char)nextNonWhitespace4)) {
                    return this.peeked = 14;
                }
                throw this.syntaxError("Expected name");
            }
            else {
                if (n != 5) {
                    return this.peeked = 2;
                }
                throw this.syntaxError("Expected name");
            }
        }
        final int nextNonWhitespace5 = this.nextNonWhitespace(true);
        if (nextNonWhitespace5 == 34) {
            return this.peeked = 9;
        }
        if (nextNonWhitespace5 == 39) {
            this.checkLenient();
            return this.peeked = 8;
        }
        if (nextNonWhitespace5 != 44 && nextNonWhitespace5 != 59) {
            if (nextNonWhitespace5 == 91) {
                return this.peeked = 3;
            }
            if (nextNonWhitespace5 != 93) {
                if (nextNonWhitespace5 == 123) {
                    return this.peeked = 1;
                }
                --this.pos;
                final int peekKeyword = this.peekKeyword();
                if (peekKeyword != 0) {
                    return peekKeyword;
                }
                final int peekNumber = this.peekNumber();
                if (peekNumber != 0) {
                    return peekNumber;
                }
                if (!this.isLiteral(this.buffer[this.pos])) {
                    throw this.syntaxError("Expected value");
                }
                this.checkLenient();
                return this.peeked = 10;
            }
            else if (n == 1) {
                return this.peeked = 4;
            }
        }
        if (n != 1 && n != 2) {
            throw this.syntaxError("Unexpected value");
        }
        this.checkLenient();
        --this.pos;
        return this.peeked = 7;
    }
    
    public void endArray() throws IOException {
        int n;
        if ((n = this.peeked) == 0) {
            n = this.doPeek();
        }
        if (n == 4) {
            --this.stackSize;
            final int[] pathIndices = this.pathIndices;
            final int n2 = this.stackSize - 1;
            ++pathIndices[n2];
            this.peeked = 0;
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Expected END_ARRAY but was ");
        sb.append(this.peek());
        sb.append(" at line ");
        sb.append(this.getLineNumber());
        sb.append(" column ");
        sb.append(this.getColumnNumber());
        sb.append(" path ");
        sb.append(this.getPath());
        throw new IllegalStateException(sb.toString());
    }
    
    public void endObject() throws IOException {
        int n;
        if ((n = this.peeked) == 0) {
            n = this.doPeek();
        }
        if (n == 2) {
            --this.stackSize;
            this.pathNames[this.stackSize] = null;
            final int[] pathIndices = this.pathIndices;
            final int n2 = this.stackSize - 1;
            ++pathIndices[n2];
            this.peeked = 0;
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Expected END_OBJECT but was ");
        sb.append(this.peek());
        sb.append(" at line ");
        sb.append(this.getLineNumber());
        sb.append(" column ");
        sb.append(this.getColumnNumber());
        sb.append(" path ");
        sb.append(this.getPath());
        throw new IllegalStateException(sb.toString());
    }
    
    int getColumnNumber() {
        return this.pos - this.lineStart + 1;
    }
    
    int getLineNumber() {
        return this.lineNumber + 1;
    }
    
    public String getPath() {
        final StringBuilder append = new StringBuilder().append('$');
        for (int i = 0; i < this.stackSize; ++i) {
            switch (this.stack[i]) {
                case 3:
                case 4:
                case 5: {
                    append.append('.');
                    if (this.pathNames[i] != null) {
                        append.append(this.pathNames[i]);
                        break;
                    }
                    break;
                }
                case 1:
                case 2: {
                    append.append('[');
                    append.append(this.pathIndices[i]);
                    append.append(']');
                    break;
                }
            }
        }
        return append.toString();
    }
    
    public boolean hasNext() throws IOException {
        int n;
        if ((n = this.peeked) == 0) {
            n = this.doPeek();
        }
        return n != 2 && n != 4;
    }
    
    public final boolean isLenient() {
        return this.lenient;
    }
    
    public boolean nextBoolean() throws IOException {
        int n;
        if ((n = this.peeked) == 0) {
            n = this.doPeek();
        }
        if (n == 5) {
            this.peeked = 0;
            final int[] pathIndices = this.pathIndices;
            final int n2 = this.stackSize - 1;
            ++pathIndices[n2];
            return true;
        }
        if (n == 6) {
            this.peeked = 0;
            final int[] pathIndices2 = this.pathIndices;
            final int n3 = this.stackSize - 1;
            ++pathIndices2[n3];
            return false;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Expected a boolean but was ");
        sb.append(this.peek());
        sb.append(" at line ");
        sb.append(this.getLineNumber());
        sb.append(" column ");
        sb.append(this.getColumnNumber());
        sb.append(" path ");
        sb.append(this.getPath());
        throw new IllegalStateException(sb.toString());
    }
    
    public double nextDouble() throws IOException {
        int n;
        if ((n = this.peeked) == 0) {
            n = this.doPeek();
        }
        if (n == 15) {
            this.peeked = 0;
            final int[] pathIndices = this.pathIndices;
            final int n2 = this.stackSize - 1;
            ++pathIndices[n2];
            return (double)this.peekedLong;
        }
        if (n == 16) {
            this.peekedString = new String(this.buffer, this.pos, this.peekedNumberLength);
            this.pos += this.peekedNumberLength;
        }
        else if (n != 8 && n != 9) {
            if (n == 10) {
                this.peekedString = this.nextUnquotedValue();
            }
            else if (n != 11) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Expected a double but was ");
                sb.append(this.peek());
                sb.append(" at line ");
                sb.append(this.getLineNumber());
                sb.append(" column ");
                sb.append(this.getColumnNumber());
                sb.append(" path ");
                sb.append(this.getPath());
                throw new IllegalStateException(sb.toString());
            }
        }
        else {
            char c;
            if (n == 8) {
                c = '\'';
            }
            else {
                c = '\"';
            }
            this.peekedString = this.nextQuotedValue(c);
        }
        this.peeked = 11;
        final double double1 = Double.parseDouble(this.peekedString);
        if (!this.lenient && (Double.isNaN(double1) || Double.isInfinite(double1))) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("JSON forbids NaN and infinities: ");
            sb2.append(double1);
            sb2.append(" at line ");
            sb2.append(this.getLineNumber());
            sb2.append(" column ");
            sb2.append(this.getColumnNumber());
            sb2.append(" path ");
            sb2.append(this.getPath());
            throw new MalformedJsonException(sb2.toString());
        }
        this.peekedString = null;
        this.peeked = 0;
        final int[] pathIndices2 = this.pathIndices;
        final int n3 = this.stackSize - 1;
        ++pathIndices2[n3];
        return double1;
    }
    
    public int nextInt() throws IOException {
        int n;
        if ((n = this.peeked) == 0) {
            n = this.doPeek();
        }
        if (n == 15) {
            final int n2 = (int)this.peekedLong;
            if (this.peekedLong != n2) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Expected an int but was ");
                sb.append(this.peekedLong);
                sb.append(" at line ");
                sb.append(this.getLineNumber());
                sb.append(" column ");
                sb.append(this.getColumnNumber());
                sb.append(" path ");
                sb.append(this.getPath());
                throw new NumberFormatException(sb.toString());
            }
            this.peeked = 0;
            final int[] pathIndices = this.pathIndices;
            final int n3 = this.stackSize - 1;
            ++pathIndices[n3];
            return n2;
        }
        else {
            if (n == 16) {
                this.peekedString = new String(this.buffer, this.pos, this.peekedNumberLength);
                this.pos += this.peekedNumberLength;
            }
            else {
                if (n != 8 && n != 9) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("Expected an int but was ");
                    sb2.append(this.peek());
                    sb2.append(" at line ");
                    sb2.append(this.getLineNumber());
                    sb2.append(" column ");
                    sb2.append(this.getColumnNumber());
                    sb2.append(" path ");
                    sb2.append(this.getPath());
                    throw new IllegalStateException(sb2.toString());
                }
                char c;
                if (n == 8) {
                    c = '\'';
                }
                else {
                    c = '\"';
                }
                this.peekedString = this.nextQuotedValue(c);
                try {
                    final int int1 = Integer.parseInt(this.peekedString);
                    this.peeked = 0;
                    final int[] pathIndices2 = this.pathIndices;
                    final int n4 = this.stackSize - 1;
                    ++pathIndices2[n4];
                    return int1;
                }
                catch (NumberFormatException ex) {}
            }
            this.peeked = 11;
            final double double1 = Double.parseDouble(this.peekedString);
            final int n5 = (int)double1;
            if (n5 != double1) {
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("Expected an int but was ");
                sb3.append(this.peekedString);
                sb3.append(" at line ");
                sb3.append(this.getLineNumber());
                sb3.append(" column ");
                sb3.append(this.getColumnNumber());
                sb3.append(" path ");
                sb3.append(this.getPath());
                throw new NumberFormatException(sb3.toString());
            }
            this.peekedString = null;
            this.peeked = 0;
            final int[] pathIndices3 = this.pathIndices;
            final int n6 = this.stackSize - 1;
            ++pathIndices3[n6];
            return n5;
        }
    }
    
    public long nextLong() throws IOException {
        int n;
        if ((n = this.peeked) == 0) {
            n = this.doPeek();
        }
        if (n == 15) {
            this.peeked = 0;
            final int[] pathIndices = this.pathIndices;
            final int n2 = this.stackSize - 1;
            ++pathIndices[n2];
            return this.peekedLong;
        }
        if (n == 16) {
            this.peekedString = new String(this.buffer, this.pos, this.peekedNumberLength);
            this.pos += this.peekedNumberLength;
        }
        else {
            if (n != 8 && n != 9) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Expected a long but was ");
                sb.append(this.peek());
                sb.append(" at line ");
                sb.append(this.getLineNumber());
                sb.append(" column ");
                sb.append(this.getColumnNumber());
                sb.append(" path ");
                sb.append(this.getPath());
                throw new IllegalStateException(sb.toString());
            }
            char c;
            if (n == 8) {
                c = '\'';
            }
            else {
                c = '\"';
            }
            this.peekedString = this.nextQuotedValue(c);
            try {
                final long long1 = Long.parseLong(this.peekedString);
                this.peeked = 0;
                final int[] pathIndices2 = this.pathIndices;
                final int n3 = this.stackSize - 1;
                ++pathIndices2[n3];
                return long1;
            }
            catch (NumberFormatException ex) {}
        }
        this.peeked = 11;
        final double double1 = Double.parseDouble(this.peekedString);
        final long n4 = (long)double1;
        if (n4 != double1) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Expected a long but was ");
            sb2.append(this.peekedString);
            sb2.append(" at line ");
            sb2.append(this.getLineNumber());
            sb2.append(" column ");
            sb2.append(this.getColumnNumber());
            sb2.append(" path ");
            sb2.append(this.getPath());
            throw new NumberFormatException(sb2.toString());
        }
        this.peekedString = null;
        this.peeked = 0;
        final int[] pathIndices3 = this.pathIndices;
        final int n5 = this.stackSize - 1;
        ++pathIndices3[n5];
        return n4;
    }
    
    public String nextName() throws IOException {
        int n;
        if ((n = this.peeked) == 0) {
            n = this.doPeek();
        }
        String s;
        if (n == 14) {
            s = this.nextUnquotedValue();
        }
        else if (n == 12) {
            s = this.nextQuotedValue('\'');
        }
        else {
            if (n != 13) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Expected a name but was ");
                sb.append(this.peek());
                sb.append(" at line ");
                sb.append(this.getLineNumber());
                sb.append(" column ");
                sb.append(this.getColumnNumber());
                sb.append(" path ");
                sb.append(this.getPath());
                throw new IllegalStateException(sb.toString());
            }
            s = this.nextQuotedValue('\"');
        }
        this.peeked = 0;
        return this.pathNames[this.stackSize - 1] = s;
    }
    
    public void nextNull() throws IOException {
        int n;
        if ((n = this.peeked) == 0) {
            n = this.doPeek();
        }
        if (n == 7) {
            this.peeked = 0;
            final int[] pathIndices = this.pathIndices;
            final int n2 = this.stackSize - 1;
            ++pathIndices[n2];
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Expected null but was ");
        sb.append(this.peek());
        sb.append(" at line ");
        sb.append(this.getLineNumber());
        sb.append(" column ");
        sb.append(this.getColumnNumber());
        sb.append(" path ");
        sb.append(this.getPath());
        throw new IllegalStateException(sb.toString());
    }
    
    public String nextString() throws IOException {
        int n;
        if ((n = this.peeked) == 0) {
            n = this.doPeek();
        }
        String s;
        if (n == 10) {
            s = this.nextUnquotedValue();
        }
        else if (n == 8) {
            s = this.nextQuotedValue('\'');
        }
        else if (n == 9) {
            s = this.nextQuotedValue('\"');
        }
        else if (n == 11) {
            s = this.peekedString;
            this.peekedString = null;
        }
        else if (n == 15) {
            s = Long.toString(this.peekedLong);
        }
        else {
            if (n != 16) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Expected a string but was ");
                sb.append(this.peek());
                sb.append(" at line ");
                sb.append(this.getLineNumber());
                sb.append(" column ");
                sb.append(this.getColumnNumber());
                sb.append(" path ");
                sb.append(this.getPath());
                throw new IllegalStateException(sb.toString());
            }
            s = new String(this.buffer, this.pos, this.peekedNumberLength);
            this.pos += this.peekedNumberLength;
        }
        this.peeked = 0;
        final int[] pathIndices = this.pathIndices;
        final int n2 = this.stackSize - 1;
        ++pathIndices[n2];
        return s;
    }
    
    public JsonToken peek() throws IOException {
        int n;
        if ((n = this.peeked) == 0) {
            n = this.doPeek();
        }
        switch (n) {
            default: {
                throw new AssertionError();
            }
            case 17: {
                return JsonToken.END_DOCUMENT;
            }
            case 15:
            case 16: {
                return JsonToken.NUMBER;
            }
            case 12:
            case 13:
            case 14: {
                return JsonToken.NAME;
            }
            case 8:
            case 9:
            case 10:
            case 11: {
                return JsonToken.STRING;
            }
            case 7: {
                return JsonToken.NULL;
            }
            case 5:
            case 6: {
                return JsonToken.BOOLEAN;
            }
            case 4: {
                return JsonToken.END_ARRAY;
            }
            case 3: {
                return JsonToken.BEGIN_ARRAY;
            }
            case 2: {
                return JsonToken.END_OBJECT;
            }
            case 1: {
                return JsonToken.BEGIN_OBJECT;
            }
        }
    }
    
    public final void setLenient(final boolean lenient) {
        this.lenient = lenient;
    }
    
    public void skipValue() throws IOException {
        int n = 0;
        int n2;
        do {
            int n3;
            if ((n3 = this.peeked) == 0) {
                n3 = this.doPeek();
            }
            if (n3 == 3) {
                this.push(1);
                n2 = n + 1;
            }
            else if (n3 == 1) {
                this.push(3);
                n2 = n + 1;
            }
            else if (n3 == 4) {
                --this.stackSize;
                n2 = n - 1;
            }
            else if (n3 == 2) {
                --this.stackSize;
                n2 = n - 1;
            }
            else if (n3 != 14 && n3 != 10) {
                if (n3 != 8 && n3 != 12) {
                    if (n3 != 9 && n3 != 13) {
                        n2 = n;
                        if (n3 == 16) {
                            this.pos += this.peekedNumberLength;
                            n2 = n;
                        }
                    }
                    else {
                        this.skipQuotedValue('\"');
                        n2 = n;
                    }
                }
                else {
                    this.skipQuotedValue('\'');
                    n2 = n;
                }
            }
            else {
                this.skipUnquotedValue();
                n2 = n;
            }
            this.peeked = 0;
        } while ((n = n2) != 0);
        final int[] pathIndices = this.pathIndices;
        final int n4 = this.stackSize - 1;
        ++pathIndices[n4];
        this.pathNames[this.stackSize - 1] = "null";
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.getClass().getSimpleName());
        sb.append(" at line ");
        sb.append(this.getLineNumber());
        sb.append(" column ");
        sb.append(this.getColumnNumber());
        return sb.toString();
    }
}
