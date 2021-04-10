package org.mozilla.javascript;

import java.io.*;
import org.mozilla.javascript.xml.*;

public class NativeGlobal implements Serializable, IdFunctionCall
{
    private static final Object FTAG;
    private static final int INVALID_UTF8 = Integer.MAX_VALUE;
    private static final int Id_decodeURI = 1;
    private static final int Id_decodeURIComponent = 2;
    private static final int Id_encodeURI = 3;
    private static final int Id_encodeURIComponent = 4;
    private static final int Id_escape = 5;
    private static final int Id_eval = 6;
    private static final int Id_isFinite = 7;
    private static final int Id_isNaN = 8;
    private static final int Id_isXMLName = 9;
    private static final int Id_new_CommonError = 14;
    private static final int Id_parseFloat = 10;
    private static final int Id_parseInt = 11;
    private static final int Id_unescape = 12;
    private static final int Id_uneval = 13;
    private static final int LAST_SCOPE_FUNCTION_ID = 13;
    private static final String URI_DECODE_RESERVED = ";/?:@&=+$,#";
    static final long serialVersionUID = 6080442165748707530L;
    
    static {
        FTAG = "Global";
    }
    
    @Deprecated
    public static EcmaError constructError(final Context context, final String s, final String s2, final Scriptable scriptable) {
        return ScriptRuntime.constructError(s, s2);
    }
    
    @Deprecated
    public static EcmaError constructError(final Context context, final String s, final String s2, final Scriptable scriptable, final String s3, final int n, final int n2, final String s4) {
        return ScriptRuntime.constructError(s, s2, s3, n, s4, n2);
    }
    
    private static String decode(final String s, final boolean b) {
        char[] array = null;
        int n = 0;
        int n2 = 0;
        final int length = s.length();
        while (true) {
            int i = 0;
            if (n2 != length) {
                final char char1 = s.charAt(n2);
                if (char1 != '%') {
                    int n3 = n;
                    if (array != null) {
                        array[n] = char1;
                        n3 = n + 1;
                    }
                    ++n2;
                    n = n3;
                }
                else {
                    char[] array2 = array;
                    int n4 = n;
                    if (array == null) {
                        array2 = new char[length];
                        s.getChars(0, n2, array2, 0);
                        n4 = n2;
                    }
                    if (n2 + 3 > length) {
                        throw uriError();
                    }
                    final int unHex = unHex(s.charAt(n2 + 1), s.charAt(n2 + 2));
                    if (unHex < 0) {
                        throw uriError();
                    }
                    final int n5 = n2 + 3;
                    char c;
                    int n6;
                    if ((unHex & 0x80) == 0x0) {
                        c = (char)unHex;
                        n6 = n5;
                    }
                    else {
                        if ((unHex & 0xC0) == 0x80) {
                            throw uriError();
                        }
                        int n7;
                        int n8;
                        int n9;
                        if ((unHex & 0x20) == 0x0) {
                            n7 = 1;
                            n8 = (unHex & 0x1F);
                            n9 = 128;
                        }
                        else if ((unHex & 0x10) == 0x0) {
                            n7 = 2;
                            n8 = (unHex & 0xF);
                            n9 = 2048;
                        }
                        else if ((unHex & 0x8) == 0x0) {
                            n7 = 3;
                            n8 = (unHex & 0x7);
                            n9 = 65536;
                        }
                        else if ((unHex & 0x4) == 0x0) {
                            n7 = 4;
                            n8 = (unHex & 0x3);
                            n9 = 2097152;
                        }
                        else {
                            if ((unHex & 0x2) != 0x0) {
                                throw uriError();
                            }
                            n7 = 5;
                            n8 = (unHex & 0x1);
                            n9 = 67108864;
                        }
                        if (n7 * 3 + n5 > length) {
                            throw uriError();
                        }
                        int n10 = n8;
                        n6 = n5;
                        while (i != n7) {
                            if (s.charAt(n6) != '%') {
                                throw uriError();
                            }
                            final int unHex2 = unHex(s.charAt(n6 + 1), s.charAt(n6 + 2));
                            if (unHex2 < 0 || (unHex2 & 0xC0) != 0x80) {
                                throw uriError();
                            }
                            n10 = (n10 << 6 | (unHex2 & 0x3F));
                            n6 += 3;
                            ++i;
                        }
                        int n11;
                        if (n10 >= n9 && (n10 < 55296 || n10 > 57343)) {
                            if (n10 == 65534 || (n11 = n10) == 65535) {
                                n11 = 65533;
                            }
                        }
                        else {
                            n11 = Integer.MAX_VALUE;
                        }
                        if (n11 >= 65536) {
                            final int n12 = n11 - 65536;
                            if (n12 > 1048575) {
                                throw uriError();
                            }
                            final char c2 = (char)((n12 >>> 10) + 55296);
                            c = (char)((n12 & 0x3FF) + 56320);
                            array2[n4] = c2;
                            ++n4;
                        }
                        else {
                            c = (char)n11;
                        }
                    }
                    int n14;
                    if (b && ";/?:@&=+$,#".indexOf(c) >= 0) {
                        int n13 = n4;
                        while (true) {
                            n14 = n13;
                            if (n2 == n6) {
                                break;
                            }
                            array2[n13] = s.charAt(n2);
                            ++n2;
                            ++n13;
                        }
                    }
                    else {
                        n14 = n4 + 1;
                        array2[n4] = c;
                    }
                    n2 = n6;
                    n = n14;
                    array = array2;
                }
            }
            else {
                if (array == null) {
                    return s;
                }
                return new String(array, 0, n);
            }
        }
    }
    
    private static String encode(final String s, final boolean b) {
        byte[] array = null;
        StringBuilder sb = null;
        byte[] array2;
        StringBuilder sb2;
        int n;
        for (int i = 0, length = s.length(); i != length; i = n + 1, array = array2, sb = sb2) {
            final char char1 = s.charAt(i);
            if (encodeUnescaped(char1, b)) {
                array2 = array;
                sb2 = sb;
                n = i;
                if (sb != null) {
                    sb.append(char1);
                    array2 = array;
                    sb2 = sb;
                    n = i;
                }
            }
            else {
                StringBuilder sb3;
                if ((sb3 = sb) == null) {
                    sb3 = new StringBuilder(length + 3);
                    sb3.append(s);
                    sb3.setLength(i);
                    array = new byte[6];
                }
                if ('\udc00' <= char1 && char1 <= '\udfff') {
                    throw uriError();
                }
                int n2;
                if (char1 >= '\ud800' && '\udbff' >= char1) {
                    ++i;
                    if (i == length) {
                        throw uriError();
                    }
                    final char char2 = s.charAt(i);
                    if ('\udc00' > char2 || char2 > '\udfff') {
                        throw uriError();
                    }
                    n2 = (char1 - '\ud800' << 10) + (char2 - '\udc00') + 65536;
                }
                else {
                    n2 = char1;
                }
                final int oneUcs4ToUtf8Char = oneUcs4ToUtf8Char(array, n2);
                int n3 = 0;
                while (true) {
                    array2 = array;
                    sb2 = sb3;
                    n = i;
                    if (n3 >= oneUcs4ToUtf8Char) {
                        break;
                    }
                    final int n4 = array[n3] & 0xFF;
                    sb3.append('%');
                    sb3.append(toHexChar(n4 >>> 4));
                    sb3.append(toHexChar(n4 & 0xF));
                    ++n3;
                }
            }
        }
        if (sb == null) {
            return s;
        }
        return sb.toString();
    }
    
    private static boolean encodeUnescaped(final char c, final boolean b) {
        return ('A' <= c && c <= 'Z') || ('a' <= c && c <= 'z') || ('0' <= c && c <= '9') || "-_.!~*'()".indexOf(c) >= 0 || (b && ";/?:@&=+$,#".indexOf(c) >= 0);
    }
    
    public static void init(final Context context, final Scriptable scriptable, final boolean b) {
        final NativeGlobal nativeGlobal = new NativeGlobal();
        for (int i = 1; i <= 13; ++i) {
            int n = 1;
            String s = null;
            switch (i) {
                default: {
                    throw Kit.codeBug();
                }
                case 13: {
                    s = "uneval";
                    break;
                }
                case 12: {
                    s = "unescape";
                    break;
                }
                case 11: {
                    s = "parseInt";
                    n = 2;
                    break;
                }
                case 10: {
                    s = "parseFloat";
                    break;
                }
                case 9: {
                    s = "isXMLName";
                    break;
                }
                case 8: {
                    s = "isNaN";
                    break;
                }
                case 7: {
                    s = "isFinite";
                    break;
                }
                case 6: {
                    s = "eval";
                    break;
                }
                case 5: {
                    s = "escape";
                    break;
                }
                case 4: {
                    s = "encodeURIComponent";
                    break;
                }
                case 3: {
                    s = "encodeURI";
                    break;
                }
                case 2: {
                    s = "decodeURIComponent";
                    break;
                }
                case 1: {
                    s = "decodeURI";
                    break;
                }
            }
            final IdFunctionObject idFunctionObject = new IdFunctionObject(nativeGlobal, NativeGlobal.FTAG, i, s, n, scriptable);
            if (b) {
                idFunctionObject.sealObject();
            }
            idFunctionObject.exportAsScopeProperty();
        }
        ScriptableObject.defineProperty(scriptable, "NaN", ScriptRuntime.NaNobj, 7);
        ScriptableObject.defineProperty(scriptable, "Infinity", ScriptRuntime.wrapNumber(Double.POSITIVE_INFINITY), 7);
        ScriptableObject.defineProperty(scriptable, "undefined", Undefined.instance, 7);
        final TopLevel.NativeErrors[] values = TopLevel.NativeErrors.values();
        for (int length = values.length, j = 0; j < length; ++j) {
            final TopLevel.NativeErrors nativeErrors = values[j];
            if (nativeErrors != TopLevel.NativeErrors.Error) {
                final String name = nativeErrors.name();
                final ScriptableObject scriptableObject = (ScriptableObject)ScriptRuntime.newBuiltinObject(context, scriptable, TopLevel.Builtins.Error, ScriptRuntime.emptyArgs);
                scriptableObject.put("name", scriptableObject, name);
                scriptableObject.put("message", scriptableObject, "");
                final IdFunctionObject idFunctionObject2 = new IdFunctionObject(nativeGlobal, NativeGlobal.FTAG, 14, name, 1, scriptable);
                idFunctionObject2.markAsConstructor(scriptableObject);
                scriptableObject.put("constructor", scriptableObject, idFunctionObject2);
                scriptableObject.setAttributes("constructor", 2);
                if (b) {
                    scriptableObject.sealObject();
                    idFunctionObject2.sealObject();
                }
                idFunctionObject2.exportAsScopeProperty();
            }
        }
    }
    
    static boolean isEvalFunction(final Object o) {
        if (o instanceof IdFunctionObject) {
            final IdFunctionObject idFunctionObject = (IdFunctionObject)o;
            if (idFunctionObject.hasTag(NativeGlobal.FTAG) && idFunctionObject.methodId() == 6) {
                return true;
            }
        }
        return false;
    }
    
    private Object js_escape(final Object[] array) {
        final String string = ScriptRuntime.toString(array, 0);
        int n = 7;
        Label_0065: {
            if (array.length > 1) {
                final double number = ScriptRuntime.toNumber(array[1]);
                final int n2;
                if (number == number && (n2 = (int)number) == number) {
                    n = n2;
                    if ((n2 & 0xFFFFFFF8) == 0x0) {
                        break Label_0065;
                    }
                }
                throw Context.reportRuntimeError0("msg.bad.esc.mask");
            }
        }
        StringBuilder sb = null;
        StringBuilder sb2;
        for (int i = 0, length = string.length(); i != length; ++i, sb = sb2) {
            final char char1 = string.charAt(i);
            if (n != 0 && ((char1 >= '0' && char1 <= '9') || (char1 >= 'A' && char1 <= 'Z') || (char1 >= 'a' && char1 <= 'z') || char1 == '@' || char1 == '*' || char1 == '_' || char1 == '-' || char1 == '.' || ((n & 0x4) != 0x0 && (char1 == '/' || char1 == '+')))) {
                if ((sb2 = sb) != null) {
                    sb.append(char1);
                    sb2 = sb;
                }
            }
            else {
                StringBuilder sb3;
                if ((sb3 = sb) == null) {
                    sb3 = new StringBuilder(length + 3);
                    sb3.append(string);
                    sb3.setLength(i);
                }
                int n3;
                if (char1 < '\u0100') {
                    if (char1 == ' ' && n == 2) {
                        sb3.append('+');
                        sb2 = sb3;
                        continue;
                    }
                    sb3.append('%');
                    n3 = 2;
                }
                else {
                    sb3.append('%');
                    sb3.append('u');
                    n3 = 4;
                }
                int n4 = (n3 - 1) * 4;
                while (true) {
                    sb2 = sb3;
                    if (n4 < 0) {
                        break;
                    }
                    final int n5 = char1 >> n4 & 0xF;
                    int n6;
                    if (n5 < 10) {
                        n6 = n5 + 48;
                    }
                    else {
                        n6 = n5 + 55;
                    }
                    sb3.append((char)n6);
                    n4 -= 4;
                }
            }
        }
        if (sb == null) {
            return string;
        }
        return sb.toString();
    }
    
    private Object js_eval(final Context context, Scriptable topLevelScope, final Object[] array) {
        topLevelScope = ScriptableObject.getTopLevelScope(topLevelScope);
        return ScriptRuntime.evalSpecial(context, topLevelScope, topLevelScope, array, "eval code", 1);
    }
    
    static Object js_parseFloat(final Object[] array) {
        if (array.length < 1) {
            return ScriptRuntime.NaNobj;
        }
        int n = 0;
        final String string = ScriptRuntime.toString(array[0]);
        for (int length = string.length(), i = 0; i != length; ++i) {
            final char char1 = string.charAt(i);
            if (!ScriptRuntime.isStrWhiteSpaceChar(char1)) {
                final int n2 = i;
                char char2 = '\0';
                int n3 = 0;
                Label_0103: {
                    if (char1 != '+') {
                        char2 = char1;
                        n3 = n2;
                        if (char1 != '-') {
                            break Label_0103;
                        }
                    }
                    n3 = n2 + 1;
                    if (n3 == length) {
                        return ScriptRuntime.NaNobj;
                    }
                    char2 = string.charAt(n3);
                }
                if (char2 == 'I') {
                    if (n3 + 8 <= length && string.regionMatches(n3, "Infinity", 0, 8)) {
                        double n4;
                        if (string.charAt(i) == '-') {
                            n4 = Double.NEGATIVE_INFINITY;
                        }
                        else {
                            n4 = Double.POSITIVE_INFINITY;
                        }
                        return ScriptRuntime.wrapNumber(n4);
                    }
                    return ScriptRuntime.NaNobj;
                }
                else {
                    int n5 = -1;
                    int n6 = -1;
                    int n7 = 0;
                Label_0453:
                    while (true) {
                        n7 = n3;
                        if (n3 >= length) {
                            break;
                        }
                        final char char3 = string.charAt(n3);
                        int n8 = 0;
                        int n9 = 0;
                        int n10 = 0;
                        Label_0434: {
                            if (char3 != '+') {
                                if (char3 != 'E' && char3 != 'e') {
                                    switch (char3) {
                                        default: {
                                            switch (char3) {
                                                default: {
                                                    n7 = n3;
                                                    break Label_0453;
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
                                                    n8 = n;
                                                    n9 = n5;
                                                    n10 = n6;
                                                    if (n6 != -1) {
                                                        n8 = 1;
                                                        n9 = n5;
                                                        n10 = n6;
                                                    }
                                                    break Label_0434;
                                                }
                                            }
                                            break;
                                        }
                                        case 46: {
                                            if (n5 != -1) {
                                                n7 = n3;
                                                break Label_0453;
                                            }
                                            n9 = n3;
                                            n8 = n;
                                            n10 = n6;
                                            break Label_0434;
                                        }
                                        case 45: {
                                            break;
                                        }
                                    }
                                }
                                else {
                                    if (n6 != -1) {
                                        n7 = n3;
                                        break;
                                    }
                                    if (n3 == length - 1) {
                                        n7 = n3;
                                        break;
                                    }
                                    n10 = n3;
                                    n8 = n;
                                    n9 = n5;
                                    break Label_0434;
                                }
                            }
                            if (n6 != n3 - 1) {
                                n7 = n3;
                                break;
                            }
                            n8 = n;
                            n9 = n5;
                            n10 = n6;
                            if (n3 == length - 1) {
                                n7 = n3 - 1;
                                break;
                            }
                        }
                        ++n3;
                        n = n8;
                        n5 = n9;
                        n6 = n10;
                    }
                    int n11 = n7;
                    if (n6 != -1) {
                        n11 = n7;
                        if (n == 0) {
                            n11 = n6;
                        }
                    }
                    final String substring = string.substring(i, n11);
                    try {
                        return Double.valueOf(substring);
                    }
                    catch (NumberFormatException ex) {
                        return ScriptRuntime.NaNobj;
                    }
                }
            }
        }
        return ScriptRuntime.NaNobj;
    }
    
    static Object js_parseInt(final Object[] array) {
        final boolean b = false;
        final String string = ScriptRuntime.toString(array, 0);
        final int int32 = ScriptRuntime.toInt32(array, 1);
        final int length = string.length();
        if (length == 0) {
            return ScriptRuntime.NaNobj;
        }
        final boolean b2 = false;
        int n = 0;
        while (true) {
            int n2;
            do {
                final char char1 = string.charAt(n);
                if (!ScriptRuntime.isStrWhiteSpaceChar(char1)) {
                    int n3 = b2 ? 1 : 0;
                    int n4 = 0;
                    int n5 = 0;
                    Label_0129: {
                        if (char1 != '+') {
                            boolean b3 = b;
                            if (char1 == '-') {
                                b3 = true;
                            }
                            final boolean b4 = (n4 = (b3 ? 1 : 0)) != 0;
                            n5 = n;
                            if (!b3) {
                                break Label_0129;
                            }
                            n3 = (b4 ? 1 : 0);
                        }
                        n5 = n + 1;
                        n4 = n3;
                    }
                    int n6 = 0;
                    int n7 = 0;
                    Label_0252: {
                        if (int32 == 0) {
                            n6 = -1;
                            n7 = n5;
                        }
                        else {
                            if (int32 < 2 || int32 > 36) {
                                return ScriptRuntime.NaNobj;
                            }
                            n6 = int32;
                            n7 = n5;
                            if (int32 == 16) {
                                n6 = int32;
                                n7 = n5;
                                if (length - n5 > 1) {
                                    n6 = int32;
                                    n7 = n5;
                                    if (string.charAt(n5) == '0') {
                                        final char char2 = string.charAt(n5 + 1);
                                        if (char2 != 'x') {
                                            n6 = int32;
                                            n7 = n5;
                                            if (char2 != 'X') {
                                                break Label_0252;
                                            }
                                        }
                                        n7 = n5 + 2;
                                        n6 = int32;
                                    }
                                }
                            }
                        }
                    }
                    int n8 = n6;
                    int n9 = n7;
                    if (n6 == -1) {
                        final int n10 = n8 = 10;
                        n9 = n7;
                        if (length - n7 > 1) {
                            n8 = n10;
                            n9 = n7;
                            if (string.charAt(n7) == '0') {
                                final char char3 = string.charAt(n7 + 1);
                                if (char3 != 'x' && char3 != 'X') {
                                    n8 = n10;
                                    n9 = n7;
                                    if ('0' <= char3) {
                                        n8 = n10;
                                        n9 = n7;
                                        if (char3 <= '9') {
                                            n8 = 8;
                                            n9 = n7 + 1;
                                        }
                                    }
                                }
                                else {
                                    n8 = 16;
                                    n9 = n7 + 2;
                                }
                            }
                        }
                    }
                    double stringToNumber = ScriptRuntime.stringToNumber(string, n9, n8);
                    if (n4 != 0) {
                        stringToNumber = -stringToNumber;
                    }
                    return ScriptRuntime.wrapNumber(stringToNumber);
                }
                n2 = n + 1;
            } while ((n = n2) < length);
            n = n2;
            continue;
        }
    }
    
    private Object js_unescape(final Object[] array) {
        final String string = ScriptRuntime.toString(array, 0);
        int i = string.indexOf(37);
        String s = string;
        if (i >= 0) {
            final int length = string.length();
            final char[] charArray = string.toCharArray();
            int n = i;
            while (i != length) {
                final char c = charArray[i];
                int n3;
                final int n2 = n3 = i + 1;
                char c2;
                if ((c2 = c) == '%') {
                    n3 = n2;
                    c2 = c;
                    if (n2 != length) {
                        int n4;
                        int n5;
                        if (charArray[n2] == 'u') {
                            n4 = n2 + 1;
                            n5 = n2 + 5;
                        }
                        else {
                            n4 = n2;
                            n5 = n2 + 2;
                        }
                        n3 = n2;
                        c2 = c;
                        if (n5 <= length) {
                            final int n6 = 0;
                            int j = n4;
                            int xDigitToInt = n6;
                            while (j != n5) {
                                xDigitToInt = Kit.xDigitToInt(charArray[j], xDigitToInt);
                                ++j;
                            }
                            n3 = n2;
                            c2 = c;
                            if (xDigitToInt >= 0) {
                                c2 = (char)xDigitToInt;
                                n3 = n5;
                            }
                        }
                    }
                }
                charArray[n] = c2;
                ++n;
                i = n3;
            }
            s = new String(charArray, 0, n);
        }
        return s;
    }
    
    private static int oneUcs4ToUtf8Char(final byte[] array, int n) {
        if ((n & 0xFFFFFF80) == 0x0) {
            array[0] = (byte)n;
            return 1;
        }
        int i;
        int n2;
        for (i = n >>> 11, n2 = 2; i != 0; i >>>= 5, ++n2) {}
        int n3 = n2;
        while (true) {
            --n3;
            if (n3 <= 0) {
                break;
            }
            array[n3] = (byte)((n & 0x3F) | 0x80);
            n >>>= 6;
        }
        array[0] = (byte)(256 - (1 << 8 - n2) + n);
        return n2;
    }
    
    private static char toHexChar(int n) {
        if (n >> 4 != 0) {
            Kit.codeBug();
        }
        if (n < 10) {
            n += 48;
        }
        else {
            n = n - 10 + 65;
        }
        return (char)n;
    }
    
    private static int unHex(final char c) {
        if ('A' <= c && c <= 'F') {
            return c - 'A' + 10;
        }
        if ('a' <= c && c <= 'f') {
            return c - 'a' + 10;
        }
        if ('0' <= c && c <= '9') {
            return c - '0';
        }
        return -1;
    }
    
    private static int unHex(final char c, final char c2) {
        final int unHex = unHex(c);
        final int unHex2 = unHex(c2);
        if (unHex >= 0 && unHex2 >= 0) {
            return unHex << 4 | unHex2;
        }
        return -1;
    }
    
    private static EcmaError uriError() {
        return ScriptRuntime.constructError("URIError", ScriptRuntime.getMessage0("msg.bad.uri"));
    }
    
    @Override
    public Object execIdCall(final IdFunctionObject idFunctionObject, final Context context, final Scriptable scriptable, final Scriptable scriptable2, final Object[] array) {
        if (idFunctionObject.hasTag(NativeGlobal.FTAG)) {
            final int methodId = idFunctionObject.methodId();
            final boolean b = true;
            final boolean b2 = true;
            boolean b3 = true;
            switch (methodId) {
                case 14: {
                    return NativeError.make(context, scriptable, idFunctionObject, array);
                }
                case 13: {
                    Object instance;
                    if (array.length != 0) {
                        instance = array[0];
                    }
                    else {
                        instance = Undefined.instance;
                    }
                    return ScriptRuntime.uneval(context, scriptable, instance);
                }
                case 12: {
                    return this.js_unescape(array);
                }
                case 11: {
                    return js_parseInt(array);
                }
                case 10: {
                    return js_parseFloat(array);
                }
                case 9: {
                    Object instance2;
                    if (array.length == 0) {
                        instance2 = Undefined.instance;
                    }
                    else {
                        instance2 = array[0];
                    }
                    return ScriptRuntime.wrapBoolean(XMLLib.extractFromScope(scriptable).isXMLName(context, instance2));
                }
                case 8: {
                    if (array.length < 1) {
                        b3 = true;
                    }
                    else {
                        final double number = ScriptRuntime.toNumber(array[0]);
                        if (number == number) {
                            b3 = false;
                        }
                    }
                    return ScriptRuntime.wrapBoolean(b3);
                }
                case 7: {
                    if (array.length < 1) {
                        return Boolean.FALSE;
                    }
                    return NativeNumber.isFinite(array[0]);
                }
                case 6: {
                    return this.js_eval(context, scriptable, array);
                }
                case 5: {
                    return this.js_escape(array);
                }
                case 3:
                case 4: {
                    return encode(ScriptRuntime.toString(array, 0), methodId == 3 && b);
                }
                case 1:
                case 2: {
                    return decode(ScriptRuntime.toString(array, 0), methodId == 1 && b2);
                }
            }
        }
        throw idFunctionObject.unknown();
    }
}
