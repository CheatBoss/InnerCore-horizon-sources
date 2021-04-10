package com.cedarsoftware.util.io;

import java.util.*;
import java.io.*;

class JsonParser
{
    private static final String EMPTY_ARRAY = "~!a~";
    public static final String EMPTY_OBJECT = "~!o~";
    private static final int HEX_DIGITS = 2;
    private static final int STATE_READ_FIELD = 1;
    private static final int STATE_READ_POST_VALUE = 3;
    private static final int STATE_READ_START_OBJECT = 0;
    private static final int STATE_READ_VALUE = 2;
    private static final int STRING_SLASH = 1;
    private static final int STRING_START = 0;
    private static final Map<String, String> stringCache;
    private final StringBuilder hexBuf;
    private final FastPushbackReader input;
    private final StringBuilder numBuf;
    private final Map<Long, JsonObject> objsRead;
    private final StringBuilder strBuf;
    private final Map<String, String> typeNameMap;
    private final boolean useMaps;
    
    static {
        (stringCache = new HashMap<String, String>()).put("", "");
        JsonParser.stringCache.put("true", "true");
        JsonParser.stringCache.put("True", "True");
        JsonParser.stringCache.put("TRUE", "TRUE");
        JsonParser.stringCache.put("false", "false");
        JsonParser.stringCache.put("False", "False");
        JsonParser.stringCache.put("FALSE", "FALSE");
        JsonParser.stringCache.put("null", "null");
        JsonParser.stringCache.put("yes", "yes");
        JsonParser.stringCache.put("Yes", "Yes");
        JsonParser.stringCache.put("YES", "YES");
        JsonParser.stringCache.put("no", "no");
        JsonParser.stringCache.put("No", "No");
        JsonParser.stringCache.put("NO", "NO");
        JsonParser.stringCache.put("on", "on");
        JsonParser.stringCache.put("On", "On");
        JsonParser.stringCache.put("ON", "ON");
        JsonParser.stringCache.put("off", "off");
        JsonParser.stringCache.put("Off", "Off");
        JsonParser.stringCache.put("OFF", "OFF");
        JsonParser.stringCache.put("@id", "@id");
        JsonParser.stringCache.put("@ref", "@ref");
        JsonParser.stringCache.put("@items", "@items");
        JsonParser.stringCache.put("@type", "@type");
        JsonParser.stringCache.put("@keys", "@keys");
        JsonParser.stringCache.put("0", "0");
        JsonParser.stringCache.put("1", "1");
        JsonParser.stringCache.put("2", "2");
        JsonParser.stringCache.put("3", "3");
        JsonParser.stringCache.put("4", "4");
        JsonParser.stringCache.put("5", "5");
        JsonParser.stringCache.put("6", "6");
        JsonParser.stringCache.put("7", "7");
        JsonParser.stringCache.put("8", "8");
        JsonParser.stringCache.put("9", "9");
    }
    
    JsonParser(final FastPushbackReader input, final Map<Long, JsonObject> objsRead, final Map<String, Object> map) {
        this.strBuf = new StringBuilder(256);
        this.hexBuf = new StringBuilder();
        this.numBuf = new StringBuilder();
        this.input = input;
        this.useMaps = Boolean.TRUE.equals(map.get("USE_MAPS"));
        this.objsRead = objsRead;
        this.typeNameMap = map.get("TYPE_NAME_MAP_REVERSE");
    }
    
    private Object readArray(final JsonObject jsonObject) throws IOException {
        final ArrayList<String> list = new ArrayList<String>();
        while (true) {
            final Object value = this.readValue(jsonObject);
            if (value != "~!a~") {
                list.add((String)value);
            }
            final int skipWhitespaceRead = this.skipWhitespaceRead();
            if (skipWhitespaceRead == 93) {
                break;
            }
            if (skipWhitespaceRead == 44) {
                continue;
            }
            this.error("Expected ',' or ']' inside array");
        }
        return list.toArray();
    }
    
    private Object readJsonObject() throws IOException {
        int i = 0;
        String s = null;
        final JsonObject<String, Long> jsonObject = new JsonObject<String, Long>();
        int n = 0;
        final FastPushbackReader input = this.input;
        while (i == 0) {
            switch (n) {
                default: {
                    continue;
                }
                case 3: {
                    final int skipWhitespaceRead = this.skipWhitespaceRead();
                    if (skipWhitespaceRead == -1) {
                        this.error("EOF reached before closing '}'");
                    }
                    if (skipWhitespaceRead == 125) {
                        i = 1;
                        continue;
                    }
                    if (skipWhitespaceRead == 44) {
                        n = 1;
                        continue;
                    }
                    this.error("Object not ended with '}'");
                    continue;
                }
                case 2: {
                    String s2 = s;
                    if (s == null) {
                        s2 = "@items";
                    }
                    Object value;
                    final Object o = value = this.readValue(jsonObject);
                    if ("@type".equals(s2)) {
                        value = o;
                        if (this.typeNameMap != null) {
                            final String s3 = this.typeNameMap.get(o);
                            value = o;
                            if (s3 != null) {
                                value = s3;
                            }
                        }
                    }
                    jsonObject.put(s2, (Long)value);
                    if ("@id".equals(s2)) {
                        this.objsRead.put((Long)value, jsonObject);
                    }
                    n = 3;
                    s = s2;
                    continue;
                }
                case 1: {
                    if (this.skipWhitespaceRead() == 34) {
                        final String string = this.readString();
                        if (this.skipWhitespaceRead() != 58) {
                            this.error("Expected ':' between string field and value");
                        }
                        s = string;
                        if (string.startsWith("@")) {
                            if (string.equals("@t")) {
                                s = JsonParser.stringCache.get("@type");
                            }
                            else if (string.equals("@i")) {
                                s = JsonParser.stringCache.get("@id");
                            }
                            else if (string.equals("@r")) {
                                s = JsonParser.stringCache.get("@ref");
                            }
                            else if (string.equals("@k")) {
                                s = JsonParser.stringCache.get("@keys");
                            }
                            else {
                                s = string;
                                if (string.equals("@e")) {
                                    s = JsonParser.stringCache.get("@items");
                                }
                            }
                        }
                        n = 2;
                        continue;
                    }
                    this.error("Expected quote");
                    continue;
                }
                case 0: {
                    final int skipWhitespaceRead2 = this.skipWhitespaceRead();
                    if (skipWhitespaceRead2 != 123) {
                        this.objsRead.size();
                        final StringBuilder sb = new StringBuilder("Input is invalid JSON; object does not start with '{', c=");
                        sb.append(skipWhitespaceRead2);
                        this.error(sb.toString());
                        continue;
                    }
                    jsonObject.line = input.getLine();
                    jsonObject.col = input.getCol();
                    final int skipWhitespaceRead3 = this.skipWhitespaceRead();
                    if (skipWhitespaceRead3 == 125) {
                        return "~!o~";
                    }
                    input.unread(skipWhitespaceRead3);
                    n = 1;
                    continue;
                }
            }
        }
        if (this.useMaps && jsonObject.isPrimitive()) {
            return jsonObject.getPrimitiveValue();
        }
        return jsonObject;
    }
    
    private Number readNumber(int n) throws IOException {
        final FastPushbackReader input = this.input;
        final StringBuilder numBuf = this.numBuf;
        final int n2 = 0;
        numBuf.setLength(0);
        numBuf.appendCodePoint(n);
        n = n2;
    Label_0131_Outer:
        while (true) {
            final int read = input.read();
            while (true) {
                Label_0178: {
                    if ((read >= 48 && read <= 57) || read == 45 || read == 43) {
                        break Label_0178;
                    }
                    Label_0166: {
                        if (read == 46 || read == 101 || read == 69) {
                            break Label_0166;
                        }
                        if (read != -1) {
                            input.unread(read);
                        }
                        Label_0117: {
                            if (n == 0) {
                                break Label_0117;
                            }
                            try {
                                return Double.parseDouble(numBuf.toString());
                                numBuf.appendCodePoint(read);
                                continue Label_0131_Outer;
                                numBuf.appendCodePoint(read);
                                n = 1;
                                continue Label_0131_Outer;
                                return Long.parseLong(numBuf.toString());
                                final StringBuilder sb = new StringBuilder("Invalid number: ");
                                sb.append((Object)numBuf);
                                final Exception ex;
                                return (Number)this.error(sb.toString(), ex);
                            }
                            catch (Exception ex2) {}
                        }
                    }
                }
                final Exception ex2;
                final Exception ex = ex2;
                continue;
            }
        }
    }
    
    private String readString() throws IOException {
        final StringBuilder strBuf = this.strBuf;
        final StringBuilder hexBuf = this.hexBuf;
        strBuf.setLength(0);
        int n = 0;
        final FastPushbackReader input = this.input;
        while (true) {
            final int read = input.read();
            if (read == -1) {
                this.error("EOF reached while reading JSON string");
            }
            if (n == 0) {
                if (read == 34) {
                    break;
                }
                if (read == 92) {
                    n = 1;
                }
                else {
                    strBuf.appendCodePoint(read);
                }
            }
            else if (n == 1) {
                if (read != 34) {
                    if (read != 39) {
                        if (read != 47) {
                            if (read != 92) {
                                if (read != 98) {
                                    if (read != 102) {
                                        if (read != 110) {
                                            if (read != 114) {
                                                switch (read) {
                                                    default: {
                                                        final StringBuilder sb = new StringBuilder("Invalid character escape sequence specified: ");
                                                        sb.append(read);
                                                        this.error(sb.toString());
                                                        break;
                                                    }
                                                    case 117: {
                                                        hexBuf.setLength(0);
                                                        n = 2;
                                                        break;
                                                    }
                                                    case 116: {
                                                        strBuf.appendCodePoint(9);
                                                        break;
                                                    }
                                                }
                                            }
                                            else {
                                                strBuf.appendCodePoint(13);
                                            }
                                        }
                                        else {
                                            strBuf.appendCodePoint(10);
                                        }
                                    }
                                    else {
                                        strBuf.appendCodePoint(12);
                                    }
                                }
                                else {
                                    strBuf.appendCodePoint(8);
                                }
                            }
                            else {
                                strBuf.appendCodePoint(92);
                            }
                        }
                        else {
                            strBuf.appendCodePoint(47);
                        }
                    }
                    else {
                        strBuf.appendCodePoint(39);
                    }
                }
                else {
                    strBuf.appendCodePoint(34);
                }
                if (read == 117) {
                    continue;
                }
                n = 0;
            }
            else if ((read >= 48 && read <= 57) || (read >= 65 && read <= 70) || (read >= 97 && read <= 102)) {
                hexBuf.appendCodePoint((char)read);
                if (hexBuf.length() != 4) {
                    continue;
                }
                strBuf.appendCodePoint(Integer.parseInt(hexBuf.toString(), 16));
                n = 0;
            }
            else {
                this.error("Expected hexadecimal digits");
            }
        }
        final String string = strBuf.toString();
        final String s = JsonParser.stringCache.get(string);
        if (s == null) {
            return string;
        }
        return s;
    }
    
    private void readToken(final String s) throws IOException {
        for (int length = s.length(), i = 1; i < length; ++i) {
            final int read = this.input.read();
            if (read == -1) {
                final StringBuilder sb = new StringBuilder("EOF reached while reading token: ");
                sb.append(s);
                this.error(sb.toString());
            }
            if (s.charAt(i) != Character.toLowerCase((char)read)) {
                final StringBuilder sb2 = new StringBuilder("Expected token: ");
                sb2.append(s);
                this.error(sb2.toString());
            }
        }
    }
    
    private int skipWhitespaceRead() throws IOException {
        final FastPushbackReader input = this.input;
        int read;
        do {
            read = input.read();
        } while (read == 32 || read == 10 || read == 13 || read == 9);
        return read;
    }
    
    Object error(final String s) {
        throw new JsonIoException(this.getMessage(s));
    }
    
    Object error(final String s, final Exception ex) {
        throw new JsonIoException(this.getMessage(s), ex);
    }
    
    String getMessage(final String s) {
        final StringBuilder sb = new StringBuilder(String.valueOf(s));
        sb.append("\nline: ");
        sb.append(this.input.getLine());
        sb.append(", col: ");
        sb.append(this.input.getCol());
        sb.append("\n");
        sb.append(this.input.getLastSnippet());
        return sb.toString();
    }
    
    Object readValue(final JsonObject jsonObject) throws IOException {
        final int skipWhitespaceRead = this.skipWhitespaceRead();
        if (skipWhitespaceRead == 34) {
            return this.readString();
        }
        if ((skipWhitespaceRead >= 48 && skipWhitespaceRead <= 57) || skipWhitespaceRead == 45) {
            return this.readNumber(skipWhitespaceRead);
        }
        if (skipWhitespaceRead != -1) {
            Label_0156: {
                if (skipWhitespaceRead != 70) {
                    Label_0148: {
                        if (skipWhitespaceRead != 78) {
                            if (skipWhitespaceRead != 84) {
                                if (skipWhitespaceRead == 91) {
                                    return this.readArray(jsonObject);
                                }
                                if (skipWhitespaceRead == 93) {
                                    this.input.unread(93);
                                    return "~!a~";
                                }
                                if (skipWhitespaceRead == 102) {
                                    break Label_0156;
                                }
                                if (skipWhitespaceRead == 110) {
                                    break Label_0148;
                                }
                                if (skipWhitespaceRead != 116) {
                                    if (skipWhitespaceRead != 123) {
                                        return this.error("Unknown JSON value type");
                                    }
                                    this.input.unread(123);
                                    return this.readJsonObject();
                                }
                            }
                            this.readToken("true");
                            return Boolean.TRUE;
                        }
                    }
                    this.readToken("null");
                    return null;
                }
            }
            this.readToken("false");
            return Boolean.FALSE;
        }
        this.error("EOF reached prematurely");
        return this.error("Unknown JSON value type");
    }
}
