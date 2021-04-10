package org.mozilla.javascript.json;

import java.util.*;
import org.mozilla.javascript.*;

public class JsonParser
{
    private Context cx;
    private int length;
    private int pos;
    private Scriptable scope;
    private String src;
    
    public JsonParser(final Context cx, final Scriptable scope) {
        this.cx = cx;
        this.scope = scope;
    }
    
    private void consume(final char c) throws ParseException {
        this.consumeWhitespace();
        if (this.pos >= this.length) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Expected ");
            sb.append(c);
            sb.append(" but reached end of stream");
            throw new ParseException(sb.toString());
        }
        final char char1 = this.src.charAt(this.pos++);
        if (char1 == c) {
            return;
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("Expected ");
        sb2.append(c);
        sb2.append(" found ");
        sb2.append(char1);
        throw new ParseException(sb2.toString());
    }
    
    private void consumeWhitespace() {
        while (this.pos < this.length) {
            final char char1 = this.src.charAt(this.pos);
            if (char1 != '\r' && char1 != ' ') {
                switch (char1) {
                    default: {
                        return;
                    }
                    case 9:
                    case 10: {
                        break;
                    }
                }
            }
            ++this.pos;
        }
    }
    
    private int fromHex(final char c) {
        if (c >= '0' && c <= '9') {
            return c - '0';
        }
        int n;
        if (c >= 'A' && c <= 'F') {
            n = c - 'A';
        }
        else {
            if (c < 'a' || c > 'f') {
                return -1;
            }
            n = c - 'a';
        }
        return n + 10;
    }
    
    private char nextOrNumberError(int n) throws ParseException {
        if (this.pos >= this.length) {
            throw this.numberError(n, this.length);
        }
        final String src = this.src;
        n = this.pos++;
        return src.charAt(n);
    }
    
    private ParseException numberError(final int n, final int n2) {
        final StringBuilder sb = new StringBuilder();
        sb.append("Unsupported number format: ");
        sb.append(this.src.substring(n, n2));
        return new ParseException(sb.toString());
    }
    
    private Object readArray() throws ParseException {
        this.consumeWhitespace();
        final int pos = this.pos;
        final int length = this.length;
        int n = 0;
        if (pos < length && this.src.charAt(this.pos) == ']') {
            ++this.pos;
            return this.cx.newArray(this.scope, 0);
        }
        final ArrayList<Object> list = new ArrayList<Object>();
        while (this.pos < this.length) {
            final char char1 = this.src.charAt(this.pos);
            if (char1 != ',') {
                if (char1 != ']') {
                    if (n != 0) {
                        throw new ParseException("Missing comma in array literal");
                    }
                    list.add(this.readValue());
                    n = 1;
                }
                else {
                    if (n == 0) {
                        throw new ParseException("Unexpected comma in array literal");
                    }
                    ++this.pos;
                    return this.cx.newArray(this.scope, list.toArray());
                }
            }
            else {
                if (n == 0) {
                    throw new ParseException("Unexpected comma in array literal");
                }
                n = 0;
                ++this.pos;
            }
            this.consumeWhitespace();
        }
        throw new ParseException("Unterminated array literal");
    }
    
    private void readDigits() {
        while (this.pos < this.length) {
            final char char1 = this.src.charAt(this.pos);
            if (char1 < '0') {
                break;
            }
            if (char1 > '9') {
                return;
            }
            ++this.pos;
        }
    }
    
    private Boolean readFalse() throws ParseException {
        if (this.length - this.pos >= 4 && this.src.charAt(this.pos) == 'a' && this.src.charAt(this.pos + 1) == 'l' && this.src.charAt(this.pos + 2) == 's' && this.src.charAt(this.pos + 3) == 'e') {
            this.pos += 4;
            return Boolean.FALSE;
        }
        throw new ParseException("Unexpected token: f");
    }
    
    private Object readNull() throws ParseException {
        if (this.length - this.pos >= 3 && this.src.charAt(this.pos) == 'u' && this.src.charAt(this.pos + 1) == 'l' && this.src.charAt(this.pos + 2) == 'l') {
            this.pos += 3;
            return null;
        }
        throw new ParseException("Unexpected token: n");
    }
    
    private Number readNumber(final char c) throws ParseException {
        final int n = this.pos - 1;
        int n2 = c;
        if (c == '-') {
            final char nextOrNumberError = this.nextOrNumberError(n);
            if (nextOrNumberError < '0' || (n2 = nextOrNumberError) > 57) {
                throw this.numberError(n, this.pos);
            }
        }
        if (n2 != 48) {
            this.readDigits();
        }
        if (this.pos < this.length && this.src.charAt(this.pos) == '.') {
            ++this.pos;
            final char nextOrNumberError2 = this.nextOrNumberError(n);
            if (nextOrNumberError2 < '0' || nextOrNumberError2 > '9') {
                throw this.numberError(n, this.pos);
            }
            this.readDigits();
        }
        if (this.pos < this.length) {
            final char char1 = this.src.charAt(this.pos);
            if (char1 == 'e' || char1 == 'E') {
                ++this.pos;
                final char nextOrNumberError3 = this.nextOrNumberError(n);
                char nextOrNumberError4;
                if (nextOrNumberError3 == '-' || (nextOrNumberError4 = nextOrNumberError3) == '+') {
                    nextOrNumberError4 = this.nextOrNumberError(n);
                }
                if (nextOrNumberError4 < '0' || nextOrNumberError4 > '9') {
                    throw this.numberError(n, this.pos);
                }
                this.readDigits();
            }
        }
        final double double1 = Double.parseDouble(this.src.substring(n, this.pos));
        final int n3 = (int)double1;
        if (n3 == double1) {
            return n3;
        }
        return double1;
    }
    
    private Object readObject() throws ParseException {
        this.consumeWhitespace();
        final Scriptable object = this.cx.newObject(this.scope);
        if (this.pos < this.length && this.src.charAt(this.pos) == '}') {
            ++this.pos;
            return object;
        }
        int n = 0;
        while (this.pos < this.length) {
            final char char1 = this.src.charAt(this.pos++);
            if (char1 != '\"') {
                if (char1 != ',') {
                    if (char1 != '}') {
                        throw new ParseException("Unexpected token in object literal");
                    }
                    if (n == 0) {
                        throw new ParseException("Unexpected comma in object literal");
                    }
                    return object;
                }
                else {
                    if (n == 0) {
                        throw new ParseException("Unexpected comma in object literal");
                    }
                    n = 0;
                }
            }
            else {
                if (n != 0) {
                    throw new ParseException("Missing comma in object literal");
                }
                final String string = this.readString();
                this.consume(':');
                final Object value = this.readValue();
                final long indexFromString = ScriptRuntime.indexFromString(string);
                if (indexFromString < 0L) {
                    object.put(string, object, value);
                }
                else {
                    object.put((int)indexFromString, object, value);
                }
                n = 1;
            }
            this.consumeWhitespace();
        }
        throw new ParseException("Unterminated object literal");
    }
    
    private String readString() throws ParseException {
        int n = this.pos;
        while (this.pos < this.length) {
            final char char1 = this.src.charAt(this.pos++);
            if (char1 <= '\u001f') {
                throw new ParseException("String contains control character");
            }
            if (char1 == '\\') {
                break;
            }
            if (char1 == '\"') {
                return this.src.substring(n, this.pos - 1);
            }
        }
        final StringBuilder sb = new StringBuilder();
        while (this.pos < this.length) {
            sb.append(this.src, n, this.pos - 1);
            if (this.pos >= this.length) {
                throw new ParseException("Unterminated string");
            }
            final char char2 = this.src.charAt(this.pos++);
            if (char2 != '\"') {
                if (char2 != '/') {
                    if (char2 != '\\') {
                        if (char2 != 'b') {
                            if (char2 != 'f') {
                                if (char2 != 'n') {
                                    if (char2 != 'r') {
                                        switch (char2) {
                                            default: {
                                                final StringBuilder sb2 = new StringBuilder();
                                                sb2.append("Unexpected character in string: '\\");
                                                sb2.append(char2);
                                                sb2.append("'");
                                                throw new ParseException(sb2.toString());
                                            }
                                            case 117: {
                                                if (this.length - this.pos < 5) {
                                                    final StringBuilder sb3 = new StringBuilder();
                                                    sb3.append("Invalid character code: \\u");
                                                    sb3.append(this.src.substring(this.pos));
                                                    throw new ParseException(sb3.toString());
                                                }
                                                final int n2 = this.fromHex(this.src.charAt(this.pos + 0)) << 12 | this.fromHex(this.src.charAt(this.pos + 1)) << 8 | this.fromHex(this.src.charAt(this.pos + 2)) << 4 | this.fromHex(this.src.charAt(this.pos + 3));
                                                if (n2 < 0) {
                                                    final StringBuilder sb4 = new StringBuilder();
                                                    sb4.append("Invalid character code: ");
                                                    sb4.append(this.src.substring(this.pos, this.pos + 4));
                                                    throw new ParseException(sb4.toString());
                                                }
                                                this.pos += 4;
                                                sb.append((char)n2);
                                                break;
                                            }
                                            case 116: {
                                                sb.append('\t');
                                                break;
                                            }
                                        }
                                    }
                                    else {
                                        sb.append('\r');
                                    }
                                }
                                else {
                                    sb.append('\n');
                                }
                            }
                            else {
                                sb.append('\f');
                            }
                        }
                        else {
                            sb.append('\b');
                        }
                    }
                    else {
                        sb.append('\\');
                    }
                }
                else {
                    sb.append('/');
                }
            }
            else {
                sb.append('\"');
            }
            n = this.pos;
            while (this.pos < this.length) {
                final char char3 = this.src.charAt(this.pos++);
                if (char3 <= '\u001f') {
                    throw new ParseException("String contains control character");
                }
                if (char3 == '\\') {
                    break;
                }
                if (char3 == '\"') {
                    sb.append(this.src, n, this.pos - 1);
                    return sb.toString();
                }
            }
        }
        throw new ParseException("Unterminated string literal");
    }
    
    private Boolean readTrue() throws ParseException {
        if (this.length - this.pos >= 3 && this.src.charAt(this.pos) == 'r' && this.src.charAt(this.pos + 1) == 'u' && this.src.charAt(this.pos + 2) == 'e') {
            this.pos += 3;
            return Boolean.TRUE;
        }
        throw new ParseException("Unexpected token: t");
    }
    
    private Object readValue() throws ParseException {
        this.consumeWhitespace();
        if (this.pos >= this.length) {
            throw new ParseException("Empty JSON string");
        }
        final char char1 = this.src.charAt(this.pos++);
        if (char1 != '\"') {
            if (char1 != '-') {
                if (char1 == '[') {
                    return this.readArray();
                }
                if (char1 == 'f') {
                    return this.readFalse();
                }
                if (char1 == 'n') {
                    return this.readNull();
                }
                if (char1 == 't') {
                    return this.readTrue();
                }
                if (char1 == '{') {
                    return this.readObject();
                }
                switch (char1) {
                    default: {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Unexpected token: ");
                        sb.append(char1);
                        throw new ParseException(sb.toString());
                    }
                    case 48:
                    case 49:
                    case 50:
                    case 51:
                    case 52:
                    case 53:
                    case 54:
                    case 55:
                    case 56:
                    case 57: {
                        break;
                    }
                }
            }
            return this.readNumber(char1);
        }
        return this.readString();
    }
    
    public Object parseValue(final String src) throws ParseException {
        // monitorenter(this)
        Label_0016: {
            if (src != null) {
                break Label_0016;
            }
            while (true) {
                try {
                    throw new ParseException("Input string may not be null");
                    // monitorexit(this)
                    throw;
                    Label_0090: {
                        return;
                    }
                    // monitorexit(this)
                    this.pos = 0;
                    this.length = src.length();
                    this.src = src;
                    final Object value = this.readValue();
                    this.consumeWhitespace();
                    // iftrue(Label_0090:, this.pos >= this.length)
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Expected end of stream at char ");
                    sb.append(this.pos);
                    throw new ParseException(sb.toString());
                }
                finally {
                    continue;
                }
                break;
            }
        }
    }
    
    public static class ParseException extends Exception
    {
        static final long serialVersionUID = 4804542791749920772L;
        
        ParseException(final Exception ex) {
            super(ex);
        }
        
        ParseException(final String s) {
            super(s);
        }
    }
}
