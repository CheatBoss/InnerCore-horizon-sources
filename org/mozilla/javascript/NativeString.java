package org.mozilla.javascript;

import org.mozilla.javascript.regexp.*;
import java.text.*;

final class NativeString extends IdScriptableObject
{
    private static final int ConstructorId_charAt = -5;
    private static final int ConstructorId_charCodeAt = -6;
    private static final int ConstructorId_concat = -14;
    private static final int ConstructorId_equalsIgnoreCase = -30;
    private static final int ConstructorId_fromCharCode = -1;
    private static final int ConstructorId_indexOf = -7;
    private static final int ConstructorId_lastIndexOf = -8;
    private static final int ConstructorId_localeCompare = -34;
    private static final int ConstructorId_match = -31;
    private static final int ConstructorId_replace = -33;
    private static final int ConstructorId_search = -32;
    private static final int ConstructorId_slice = -15;
    private static final int ConstructorId_split = -9;
    private static final int ConstructorId_substr = -13;
    private static final int ConstructorId_substring = -10;
    private static final int ConstructorId_toLocaleLowerCase = -35;
    private static final int ConstructorId_toLowerCase = -11;
    private static final int ConstructorId_toUpperCase = -12;
    private static final int Id_anchor = 28;
    private static final int Id_big = 21;
    private static final int Id_blink = 22;
    private static final int Id_bold = 16;
    private static final int Id_charAt = 5;
    private static final int Id_charCodeAt = 6;
    private static final int Id_codePointAt = 45;
    private static final int Id_concat = 14;
    private static final int Id_constructor = 1;
    private static final int Id_endsWith = 42;
    private static final int Id_equals = 29;
    private static final int Id_equalsIgnoreCase = 30;
    private static final int Id_fixed = 18;
    private static final int Id_fontcolor = 26;
    private static final int Id_fontsize = 25;
    private static final int Id_includes = 40;
    private static final int Id_indexOf = 7;
    private static final int Id_italics = 17;
    private static final int Id_lastIndexOf = 8;
    private static final int Id_length = 1;
    private static final int Id_link = 27;
    private static final int Id_localeCompare = 34;
    private static final int Id_match = 31;
    private static final int Id_normalize = 43;
    private static final int Id_repeat = 44;
    private static final int Id_replace = 33;
    private static final int Id_search = 32;
    private static final int Id_slice = 15;
    private static final int Id_small = 20;
    private static final int Id_split = 9;
    private static final int Id_startsWith = 41;
    private static final int Id_strike = 19;
    private static final int Id_sub = 24;
    private static final int Id_substr = 13;
    private static final int Id_substring = 10;
    private static final int Id_sup = 23;
    private static final int Id_toLocaleLowerCase = 35;
    private static final int Id_toLocaleUpperCase = 36;
    private static final int Id_toLowerCase = 11;
    private static final int Id_toSource = 3;
    private static final int Id_toString = 2;
    private static final int Id_toUpperCase = 12;
    private static final int Id_trim = 37;
    private static final int Id_trimLeft = 38;
    private static final int Id_trimRight = 39;
    private static final int Id_valueOf = 4;
    private static final int MAX_INSTANCE_ID = 1;
    private static final int MAX_PROTOTYPE_ID = 45;
    private static final Object STRING_TAG;
    static final long serialVersionUID = 920268368584188687L;
    private CharSequence string;
    
    static {
        STRING_TAG = "String";
    }
    
    NativeString(final CharSequence string) {
        this.string = string;
    }
    
    static void init(final Scriptable scriptable, final boolean b) {
        new NativeString("").exportAsJSClass(45, scriptable, b);
    }
    
    private static String js_concat(final String s, final Object[] array) {
        final int length = array.length;
        if (length == 0) {
            return s;
        }
        final int n = 0;
        if (length == 1) {
            return s.concat(ScriptRuntime.toString(array[0]));
        }
        int length2 = s.length();
        final String[] array2 = new String[length];
        for (int i = 0; i != length; ++i) {
            final String string = ScriptRuntime.toString(array[i]);
            array2[i] = string;
            length2 += string.length();
        }
        final StringBuilder sb = new StringBuilder(length2);
        sb.append(s);
        for (int j = n; j != length; ++j) {
            sb.append(array2[j]);
        }
        return sb.toString();
    }
    
    private static int js_indexOf(final int n, final String s, final Object[] array) {
        final String string = ScriptRuntime.toString(array, 0);
        final double integer = ScriptRuntime.toInteger(array, 1);
        if (integer > s.length() && n != 41 && n != 42) {
            return -1;
        }
        double n2 = 0.0;
        Label_0108: {
            if (integer < 0.0) {
                n2 = 0.0;
            }
            else if (integer > s.length()) {
                n2 = s.length();
            }
            else {
                n2 = integer;
                if (n == 42) {
                    if (integer == integer) {
                        n2 = integer;
                        if (integer <= s.length()) {
                            break Label_0108;
                        }
                    }
                    n2 = s.length();
                }
            }
        }
        if (42 == n) {
            double n3 = 0.0;
            Label_0153: {
                if (array.length != 0 && array.length != 1) {
                    n3 = n2;
                    if (array.length != 2) {
                        break Label_0153;
                    }
                    n3 = n2;
                    if (array[1] != Undefined.instance) {
                        break Label_0153;
                    }
                }
                n3 = s.length();
            }
            if (s.substring(0, (int)n3).endsWith(string)) {
                return 0;
            }
            return -1;
        }
        else {
            if (n != 41) {
                return s.indexOf(string, (int)n2);
            }
            if (s.startsWith(string, (int)n2)) {
                return 0;
            }
            return -1;
        }
    }
    
    private static int js_lastIndexOf(final String s, final Object[] array) {
        final String string = ScriptRuntime.toString(array, 0);
        final double number = ScriptRuntime.toNumber(array, 1);
        double n;
        if (number == number && number <= s.length()) {
            n = number;
            if (number < 0.0) {
                n = 0.0;
            }
        }
        else {
            n = s.length();
        }
        return s.lastIndexOf(string, (int)n);
    }
    
    private static CharSequence js_slice(final CharSequence charSequence, final Object[] array) {
        double integer;
        if (array.length < 1) {
            integer = 0.0;
        }
        else {
            integer = ScriptRuntime.toInteger(array[0]);
        }
        final int length = charSequence.length();
        double n;
        if (integer < 0.0) {
            if ((n = integer + length) < 0.0) {
                n = 0.0;
            }
        }
        else {
            n = integer;
            if (integer > length) {
                n = length;
            }
        }
        double n3;
        if (array.length >= 2 && array[1] != Undefined.instance) {
            final double integer2 = ScriptRuntime.toInteger(array[1]);
            double n2;
            if (integer2 < 0.0) {
                if ((n2 = integer2 + length) < 0.0) {
                    n2 = 0.0;
                }
            }
            else {
                n2 = integer2;
                if (integer2 > length) {
                    n2 = length;
                }
            }
            n3 = n2;
            if (n2 < n) {
                n3 = n;
            }
        }
        else {
            n3 = length;
        }
        return charSequence.subSequence((int)n, (int)n3);
    }
    
    private static CharSequence js_substr(final CharSequence charSequence, final Object[] array) {
        if (array.length < 1) {
            return charSequence;
        }
        final double integer = ScriptRuntime.toInteger(array[0]);
        final int length = charSequence.length();
        double n;
        if (integer < 0.0) {
            if ((n = integer + length) < 0.0) {
                n = 0.0;
            }
        }
        else {
            n = integer;
            if (integer > length) {
                n = length;
            }
        }
        double n2;
        if (array.length == 1) {
            n2 = length;
        }
        else {
            double integer2;
            if ((integer2 = ScriptRuntime.toInteger(array[1])) < 0.0) {
                integer2 = 0.0;
            }
            if ((n2 = integer2 + n) > length) {
                n2 = length;
            }
        }
        return charSequence.subSequence((int)n, (int)n2);
    }
    
    private static CharSequence js_substring(final Context context, final CharSequence charSequence, final Object[] array) {
        final int length = charSequence.length();
        final double integer = ScriptRuntime.toInteger(array, 0);
        double n;
        if (integer < 0.0) {
            n = 0.0;
        }
        else {
            n = integer;
            if (integer > length) {
                n = length;
            }
        }
        double n3;
        double n4;
        if (array.length > 1 && array[1] != Undefined.instance) {
            final double integer2 = ScriptRuntime.toInteger(array[1]);
            double n2;
            if (integer2 < 0.0) {
                n2 = 0.0;
            }
            else {
                n2 = integer2;
                if (integer2 > length) {
                    n2 = length;
                }
            }
            n3 = n;
            n4 = n2;
            if (n2 < n) {
                if (context.getLanguageVersion() != 120) {
                    n3 = n2;
                    n4 = n;
                }
                else {
                    n4 = n;
                    n3 = n;
                }
            }
        }
        else {
            n4 = length;
            n3 = n;
        }
        return charSequence.subSequence((int)n3, (int)n4);
    }
    
    private static NativeString realThis(final Scriptable scriptable, final IdFunctionObject idFunctionObject) {
        if (!(scriptable instanceof NativeString)) {
            throw IdScriptableObject.incompatibleCallError(idFunctionObject);
        }
        return (NativeString)scriptable;
    }
    
    private static String tagify(final Object o, final String s, final String s2, final Object[] array) {
        final String string = ScriptRuntime.toString(o);
        final StringBuilder sb = new StringBuilder();
        sb.append('<');
        sb.append(s);
        if (s2 != null) {
            sb.append(' ');
            sb.append(s2);
            sb.append("=\"");
            sb.append(ScriptRuntime.toString(array, 0));
            sb.append('\"');
        }
        sb.append('>');
        sb.append(string);
        sb.append("</");
        sb.append(s);
        sb.append('>');
        return sb.toString();
    }
    
    @Override
    public Object execIdCall(final IdFunctionObject idFunctionObject, final Context context, final Scriptable scriptable, Scriptable scriptable2, Object[] array) {
        if (!idFunctionObject.hasTag(NativeString.STRING_TAG)) {
            return super.execIdCall(idFunctionObject, context, scriptable, scriptable2, array);
        }
        int methodId = idFunctionObject.methodId();
        while (true) {
            final int n = 0;
            final int n2 = 0;
            final int n3 = 0;
            final int n4 = 0;
            if (methodId != -1) {
                Label_1677: {
                    switch (methodId) {
                        default: {
                            switch (methodId) {
                                default: {
                                    switch (methodId) {
                                        default: {
                                            final StringBuilder sb = new StringBuilder();
                                            sb.append("String.prototype has no method: ");
                                            sb.append(idFunctionObject.getFunctionName());
                                            throw new IllegalArgumentException(sb.toString());
                                        }
                                        case 45: {
                                            final String string = ScriptRuntime.toString(ScriptRuntime.requireObjectCoercible(scriptable2, idFunctionObject));
                                            final double integer = ScriptRuntime.toInteger(array, 0);
                                            if (integer >= 0.0 && integer < string.length()) {
                                                return string.codePointAt((int)integer);
                                            }
                                            return Undefined.instance;
                                        }
                                        case 44: {
                                            final String string2 = ScriptRuntime.toString(ScriptRuntime.requireObjectCoercible(scriptable2, idFunctionObject));
                                            final double integer2 = ScriptRuntime.toInteger(array, 0);
                                            if (integer2 == 0.0) {
                                                return "";
                                            }
                                            if (integer2 >= 0.0 && integer2 != Double.POSITIVE_INFINITY) {
                                                final long n5 = string2.length() * (long)integer2;
                                                long n6 = 0L;
                                                Label_0520: {
                                                    if (integer2 < 2.147483647E9) {
                                                        n6 = n5;
                                                        if (n5 < 2147483647L) {
                                                            break Label_0520;
                                                        }
                                                    }
                                                    n6 = 2147483647L;
                                                }
                                                final StringBuilder sb2 = new StringBuilder((int)n6);
                                                sb2.append(string2);
                                                int n7 = 1;
                                                int n8;
                                                while (true) {
                                                    n8 = n7;
                                                    if (n7 > integer2 / 2.0) {
                                                        break;
                                                    }
                                                    sb2.append((CharSequence)sb2);
                                                    n7 *= 2;
                                                }
                                                while (n8 < integer2) {
                                                    sb2.append(string2);
                                                    ++n8;
                                                }
                                                return sb2.toString();
                                            }
                                            throw ScriptRuntime.rangeError("Invalid count value");
                                        }
                                        case 43: {
                                            final String string3 = ScriptRuntime.toString(array, 0);
                                            Normalizer.Form form;
                                            if (Normalizer.Form.NFD.name().equals(string3)) {
                                                form = Normalizer.Form.NFD;
                                            }
                                            else if (Normalizer.Form.NFKC.name().equals(string3)) {
                                                form = Normalizer.Form.NFKC;
                                            }
                                            else if (Normalizer.Form.NFKD.name().equals(string3)) {
                                                form = Normalizer.Form.NFKD;
                                            }
                                            else {
                                                if (!Normalizer.Form.NFC.name().equals(string3) && array.length != 0) {
                                                    throw ScriptRuntime.rangeError("The normalization form should be one of NFC, NFD, NFKC, NFKD");
                                                }
                                                form = Normalizer.Form.NFC;
                                            }
                                            return Normalizer.normalize(ScriptRuntime.toString(ScriptRuntime.requireObjectCoercible(scriptable2, idFunctionObject)), form);
                                        }
                                        case 40:
                                        case 41:
                                        case 42: {
                                            final String string4 = ScriptRuntime.toString(ScriptRuntime.requireObjectCoercible(scriptable2, idFunctionObject));
                                            if (array.length > 0 && array[0] instanceof NativeRegExp) {
                                                throw ScriptRuntime.typeError2("msg.first.arg.not.regexp", String.class.getSimpleName(), idFunctionObject.getFunctionName());
                                            }
                                            final int js_index = js_indexOf(methodId, string4, array);
                                            if (methodId == 40) {
                                                return js_index != -1;
                                            }
                                            if (methodId == 41) {
                                                return js_index == 0;
                                            }
                                            if (methodId == 42) {
                                                return js_index != -1;
                                            }
                                            return ScriptRuntime.wrapInt(js_lastIndexOf(ScriptRuntime.toString(scriptable2), array));
                                        }
                                        case 39: {
                                            final String string5 = ScriptRuntime.toString(scriptable2);
                                            char[] charArray;
                                            int length;
                                            for (charArray = string5.toCharArray(), length = charArray.length; length > 0 && ScriptRuntime.isJSWhitespaceOrLineTerminator(charArray[length - 1]); --length) {}
                                            return string5.substring(0, length);
                                        }
                                        case 38: {
                                            final String string6 = ScriptRuntime.toString(scriptable2);
                                            char[] charArray2;
                                            int n9;
                                            for (charArray2 = string6.toCharArray(), n9 = n4; n9 < charArray2.length && ScriptRuntime.isJSWhitespaceOrLineTerminator(charArray2[n9]); ++n9) {}
                                            return string6.substring(n9, charArray2.length);
                                        }
                                        case 37: {
                                            final String string7 = ScriptRuntime.toString(scriptable2);
                                            char[] charArray3;
                                            int n10;
                                            for (charArray3 = string7.toCharArray(), n10 = n; n10 < charArray3.length && ScriptRuntime.isJSWhitespaceOrLineTerminator(charArray3[n10]); ++n10) {}
                                            int length2;
                                            for (length2 = charArray3.length; length2 > n10 && ScriptRuntime.isJSWhitespaceOrLineTerminator(charArray3[length2 - 1]); --length2) {}
                                            return string7.substring(n10, length2);
                                        }
                                        case 36: {
                                            return ScriptRuntime.toString(scriptable2).toUpperCase(context.getLocale());
                                        }
                                        case 35: {
                                            return ScriptRuntime.toString(scriptable2).toLowerCase(context.getLocale());
                                        }
                                        case 34: {
                                            final Collator instance = Collator.getInstance(context.getLocale());
                                            instance.setStrength(3);
                                            instance.setDecomposition(1);
                                            return ScriptRuntime.wrapNumber(instance.compare(ScriptRuntime.toString(scriptable2), ScriptRuntime.toString(array, 0)));
                                        }
                                        case 31:
                                        case 32:
                                        case 33: {
                                            int n11;
                                            if (methodId == 31) {
                                                n11 = 1;
                                            }
                                            else if (methodId == 32) {
                                                n11 = 3;
                                            }
                                            else {
                                                n11 = 2;
                                            }
                                            return ScriptRuntime.checkRegExpProxy(context).action(context, scriptable, scriptable2, array, n11);
                                        }
                                        case 29:
                                        case 30: {
                                            final String string8 = ScriptRuntime.toString(scriptable2);
                                            final String string9 = ScriptRuntime.toString(array, 0);
                                            boolean b;
                                            if (methodId == 29) {
                                                b = string8.equals(string9);
                                            }
                                            else {
                                                b = string8.equalsIgnoreCase(string9);
                                            }
                                            return ScriptRuntime.wrapBoolean(b);
                                        }
                                        case 28: {
                                            return tagify(scriptable2, "a", "name", array);
                                        }
                                        case 27: {
                                            return tagify(scriptable2, "a", "href", array);
                                        }
                                        case 26: {
                                            return tagify(scriptable2, "font", "color", array);
                                        }
                                        case 25: {
                                            return tagify(scriptable2, "font", "size", array);
                                        }
                                        case 24: {
                                            return tagify(scriptable2, "sub", null, null);
                                        }
                                        case 23: {
                                            return tagify(scriptable2, "sup", null, null);
                                        }
                                        case 22: {
                                            return tagify(scriptable2, "blink", null, null);
                                        }
                                        case 21: {
                                            return tagify(scriptable2, "big", null, null);
                                        }
                                        case 20: {
                                            return tagify(scriptable2, "small", null, null);
                                        }
                                        case 19: {
                                            return tagify(scriptable2, "strike", null, null);
                                        }
                                        case 18: {
                                            return tagify(scriptable2, "tt", null, null);
                                        }
                                        case 17: {
                                            return tagify(scriptable2, "i", null, null);
                                        }
                                        case 16: {
                                            return tagify(scriptable2, "b", null, null);
                                        }
                                        case 15: {
                                            return js_slice(ScriptRuntime.toCharSequence(scriptable2), array);
                                        }
                                        case 14: {
                                            return js_concat(ScriptRuntime.toString(scriptable2), array);
                                        }
                                        case 13: {
                                            return js_substr(ScriptRuntime.toCharSequence(scriptable2), array);
                                        }
                                        case 12: {
                                            return ScriptRuntime.toString(scriptable2).toUpperCase(ScriptRuntime.ROOT_LOCALE);
                                        }
                                        case 11: {
                                            return ScriptRuntime.toString(scriptable2).toLowerCase(ScriptRuntime.ROOT_LOCALE);
                                        }
                                        case 10: {
                                            return js_substring(context, ScriptRuntime.toCharSequence(scriptable2), array);
                                        }
                                        case 9: {
                                            return ScriptRuntime.checkRegExpProxy(context).js_split(context, scriptable, ScriptRuntime.toString(scriptable2), array);
                                        }
                                        case 8: {
                                            return ScriptRuntime.wrapInt(js_lastIndexOf(ScriptRuntime.toString(scriptable2), array));
                                        }
                                        case 7: {
                                            return ScriptRuntime.wrapInt(js_indexOf(7, ScriptRuntime.toString(scriptable2), array));
                                        }
                                        case 5:
                                        case 6: {
                                            final CharSequence charSequence = ScriptRuntime.toCharSequence(scriptable2);
                                            final double integer3 = ScriptRuntime.toInteger(array, 0);
                                            if (integer3 >= 0.0 && integer3 < charSequence.length()) {
                                                final char char1 = charSequence.charAt((int)integer3);
                                                if (methodId == 5) {
                                                    return String.valueOf(char1);
                                                }
                                                return ScriptRuntime.wrapInt(char1);
                                            }
                                            else {
                                                if (methodId == 5) {
                                                    return "";
                                                }
                                                return ScriptRuntime.NaNobj;
                                            }
                                            break;
                                        }
                                        case 3: {
                                            final CharSequence string10 = realThis(scriptable2, idFunctionObject).string;
                                            final StringBuilder sb3 = new StringBuilder();
                                            sb3.append("(new String(\"");
                                            sb3.append(ScriptRuntime.escapeString(string10.toString()));
                                            sb3.append("\"))");
                                            return sb3.toString();
                                        }
                                        case 2:
                                        case 4: {
                                            final CharSequence string11 = realThis(scriptable2, idFunctionObject).string;
                                            if (string11 instanceof String) {
                                                return string11;
                                            }
                                            return string11.toString();
                                        }
                                        case 1: {
                                            CharSequence charSequence2;
                                            if (array.length >= 1) {
                                                charSequence2 = ScriptRuntime.toCharSequence(array[0]);
                                            }
                                            else {
                                                charSequence2 = "";
                                            }
                                            if (scriptable2 == null) {
                                                return new NativeString(charSequence2);
                                            }
                                            if (charSequence2 instanceof String) {
                                                return charSequence2;
                                            }
                                            return charSequence2.toString();
                                        }
                                    }
                                    break;
                                }
                                case -15:
                                case -14:
                                case -13:
                                case -12:
                                case -11:
                                case -10:
                                case -9:
                                case -8:
                                case -7:
                                case -6:
                                case -5: {
                                    break Label_1677;
                                }
                            }
                            break;
                        }
                        case -35:
                        case -34:
                        case -33:
                        case -32:
                        case -31:
                        case -30: {
                            if (array.length > 0) {
                                scriptable2 = ScriptRuntime.toObject(context, scriptable, ScriptRuntime.toCharSequence(array[0]));
                                final Object[] array2 = new Object[array.length - 1];
                                for (int i = n2; i < array2.length; ++i) {
                                    array2[i] = array[i + 1];
                                }
                                array = array2;
                            }
                            else {
                                scriptable2 = ScriptRuntime.toObject(context, scriptable, ScriptRuntime.toCharSequence(scriptable2));
                            }
                            methodId = -methodId;
                            continue;
                        }
                    }
                }
            }
            else {
                final int length3 = array.length;
                if (length3 < 1) {
                    return "";
                }
                final StringBuilder sb4 = new StringBuilder(length3);
                for (int j = n3; j != length3; ++j) {
                    sb4.append(ScriptRuntime.toUint16(array[j]));
                }
                return sb4.toString();
            }
        }
    }
    
    @Override
    protected void fillConstructorProperties(final IdFunctionObject idFunctionObject) {
        this.addIdFunctionProperty(idFunctionObject, NativeString.STRING_TAG, -1, "fromCharCode", 1);
        this.addIdFunctionProperty(idFunctionObject, NativeString.STRING_TAG, -5, "charAt", 2);
        this.addIdFunctionProperty(idFunctionObject, NativeString.STRING_TAG, -6, "charCodeAt", 2);
        this.addIdFunctionProperty(idFunctionObject, NativeString.STRING_TAG, -7, "indexOf", 2);
        this.addIdFunctionProperty(idFunctionObject, NativeString.STRING_TAG, -8, "lastIndexOf", 2);
        this.addIdFunctionProperty(idFunctionObject, NativeString.STRING_TAG, -9, "split", 3);
        this.addIdFunctionProperty(idFunctionObject, NativeString.STRING_TAG, -10, "substring", 3);
        this.addIdFunctionProperty(idFunctionObject, NativeString.STRING_TAG, -11, "toLowerCase", 1);
        this.addIdFunctionProperty(idFunctionObject, NativeString.STRING_TAG, -12, "toUpperCase", 1);
        this.addIdFunctionProperty(idFunctionObject, NativeString.STRING_TAG, -13, "substr", 3);
        this.addIdFunctionProperty(idFunctionObject, NativeString.STRING_TAG, -14, "concat", 2);
        this.addIdFunctionProperty(idFunctionObject, NativeString.STRING_TAG, -15, "slice", 3);
        this.addIdFunctionProperty(idFunctionObject, NativeString.STRING_TAG, -30, "equalsIgnoreCase", 2);
        this.addIdFunctionProperty(idFunctionObject, NativeString.STRING_TAG, -31, "match", 2);
        this.addIdFunctionProperty(idFunctionObject, NativeString.STRING_TAG, -32, "search", 2);
        this.addIdFunctionProperty(idFunctionObject, NativeString.STRING_TAG, -33, "replace", 2);
        this.addIdFunctionProperty(idFunctionObject, NativeString.STRING_TAG, -34, "localeCompare", 2);
        this.addIdFunctionProperty(idFunctionObject, NativeString.STRING_TAG, -35, "toLocaleLowerCase", 1);
        super.fillConstructorProperties(idFunctionObject);
    }
    
    @Override
    protected int findInstanceIdInfo(final String s) {
        if (s.equals("length")) {
            return IdScriptableObject.instanceIdInfo(7, 1);
        }
        return super.findInstanceIdInfo(s);
    }
    
    @Override
    protected int findPrototypeId(final String s) {
        int n = 0;
        String s2 = null;
        Label_1031: {
            switch (s.length()) {
                case 17: {
                    final char char1 = s.charAt(8);
                    if (char1 == 'L') {
                        s2 = "toLocaleLowerCase";
                        n = 35;
                        break;
                    }
                    if (char1 == 'U') {
                        s2 = "toLocaleUpperCase";
                        n = 36;
                        break;
                    }
                    break;
                }
                case 16: {
                    s2 = "equalsIgnoreCase";
                    n = 30;
                    break;
                }
                case 13: {
                    s2 = "localeCompare";
                    n = 34;
                    break;
                }
                case 11: {
                    final char char2 = s.charAt(2);
                    if (char2 == 'L') {
                        s2 = "toLowerCase";
                        n = 11;
                        break;
                    }
                    if (char2 == 'U') {
                        s2 = "toUpperCase";
                        n = 12;
                        break;
                    }
                    if (char2 == 'd') {
                        s2 = "codePointAt";
                        n = 45;
                        break;
                    }
                    if (char2 == 'n') {
                        s2 = "constructor";
                        n = 1;
                        break;
                    }
                    if (char2 != 's') {
                        break;
                    }
                    s2 = "lastIndexOf";
                    n = 8;
                    break;
                }
                case 10: {
                    final char char3 = s.charAt(0);
                    if (char3 == 'c') {
                        s2 = "charCodeAt";
                        n = 6;
                        break;
                    }
                    if (char3 == 's') {
                        s2 = "startsWith";
                        n = 41;
                        break;
                    }
                    break;
                }
                case 9: {
                    final char char4 = s.charAt(0);
                    if (char4 == 'f') {
                        s2 = "fontcolor";
                        n = 26;
                        break;
                    }
                    if (char4 == 'n') {
                        s2 = "normalize";
                        n = 43;
                        break;
                    }
                    switch (char4) {
                        default: {
                            break Label_1031;
                        }
                        case 116: {
                            s2 = "trimRight";
                            n = 39;
                            break Label_1031;
                        }
                        case 115: {
                            s2 = "substring";
                            n = 10;
                            break Label_1031;
                        }
                    }
                    break;
                }
                case 8: {
                    final char char5 = s.charAt(6);
                    if (char5 == 'c') {
                        s2 = "toSource";
                        n = 3;
                        break;
                    }
                    if (char5 == 'n') {
                        s2 = "toString";
                        n = 2;
                        break;
                    }
                    if (char5 == 't') {
                        s2 = "endsWith";
                        n = 42;
                        break;
                    }
                    if (char5 == 'z') {
                        s2 = "fontsize";
                        n = 25;
                        break;
                    }
                    switch (char5) {
                        default: {
                            break Label_1031;
                        }
                        case 102: {
                            s2 = "trimLeft";
                            n = 38;
                            break Label_1031;
                        }
                        case 101: {
                            s2 = "includes";
                            n = 40;
                            break Label_1031;
                        }
                    }
                    break;
                }
                case 7: {
                    final char char6 = s.charAt(1);
                    if (char6 == 'a') {
                        s2 = "valueOf";
                        n = 4;
                        break;
                    }
                    if (char6 == 'e') {
                        s2 = "replace";
                        n = 33;
                        break;
                    }
                    if (char6 == 'n') {
                        s2 = "indexOf";
                        n = 7;
                        break;
                    }
                    if (char6 != 't') {
                        break;
                    }
                    s2 = "italics";
                    n = 17;
                    break;
                }
                case 6: {
                    switch (s.charAt(1)) {
                        default: {
                            break Label_1031;
                        }
                        case 'u': {
                            s2 = "substr";
                            n = 13;
                            break Label_1031;
                        }
                        case 't': {
                            s2 = "strike";
                            n = 19;
                            break Label_1031;
                        }
                        case 'q': {
                            s2 = "equals";
                            n = 29;
                            break Label_1031;
                        }
                        case 'o': {
                            s2 = "concat";
                            n = 14;
                            break Label_1031;
                        }
                        case 'n': {
                            s2 = "anchor";
                            n = 28;
                            break Label_1031;
                        }
                        case 'h': {
                            s2 = "charAt";
                            n = 5;
                            break Label_1031;
                        }
                        case 'e': {
                            final char char7 = s.charAt(0);
                            if (char7 == 'r') {
                                s2 = "repeat";
                                n = 44;
                                break Label_1031;
                            }
                            if (char7 == 's') {
                                s2 = "search";
                                n = 32;
                                break Label_1031;
                            }
                            break Label_1031;
                        }
                    }
                    break;
                }
                case 5: {
                    switch (s.charAt(4)) {
                        default: {
                            break Label_1031;
                        }
                        case 't': {
                            s2 = "split";
                            n = 9;
                            break Label_1031;
                        }
                        case 'l': {
                            s2 = "small";
                            n = 20;
                            break Label_1031;
                        }
                        case 'k': {
                            s2 = "blink";
                            n = 22;
                            break Label_1031;
                        }
                        case 'h': {
                            s2 = "match";
                            n = 31;
                            break Label_1031;
                        }
                        case 'e': {
                            s2 = "slice";
                            n = 15;
                            break Label_1031;
                        }
                        case 'd': {
                            s2 = "fixed";
                            n = 18;
                            break Label_1031;
                        }
                    }
                    break;
                }
                case 4: {
                    final char char8 = s.charAt(0);
                    if (char8 == 'b') {
                        s2 = "bold";
                        n = 16;
                        break;
                    }
                    if (char8 == 'l') {
                        s2 = "link";
                        n = 27;
                        break;
                    }
                    if (char8 == 't') {
                        s2 = "trim";
                        n = 37;
                        break;
                    }
                    break;
                }
                case 3: {
                    final char char9 = s.charAt(2);
                    if (char9 == 'b') {
                        if (s.charAt(0) == 's' && s.charAt(1) == 'u') {
                            return 24;
                        }
                        break;
                    }
                    else if (char9 == 'g') {
                        if (s.charAt(0) == 'b' && s.charAt(1) == 'i') {
                            return 21;
                        }
                        break;
                    }
                    else {
                        if (char9 == 'p' && s.charAt(0) == 's' && s.charAt(1) == 'u') {
                            return 23;
                        }
                        break;
                    }
                    break;
                }
            }
        }
        int n2 = n;
        if (s2 != null) {
            n2 = n;
            if (s2 != s) {
                n2 = n;
                if (!s2.equals(s)) {
                    n2 = 0;
                }
            }
        }
        return n2;
    }
    
    @Override
    public Object get(final int n, final Scriptable scriptable) {
        if (n >= 0 && n < this.string.length()) {
            return String.valueOf(this.string.charAt(n));
        }
        return super.get(n, scriptable);
    }
    
    @Override
    public String getClassName() {
        return "String";
    }
    
    @Override
    protected String getInstanceIdName(final int n) {
        if (n == 1) {
            return "length";
        }
        return super.getInstanceIdName(n);
    }
    
    @Override
    protected Object getInstanceIdValue(final int n) {
        if (n == 1) {
            return ScriptRuntime.wrapInt(this.string.length());
        }
        return super.getInstanceIdValue(n);
    }
    
    int getLength() {
        return this.string.length();
    }
    
    @Override
    protected int getMaxInstanceId() {
        return 1;
    }
    
    @Override
    protected void initPrototypeId(final int n) {
        int n2 = 0;
        String s = null;
        switch (n) {
            default: {
                throw new IllegalArgumentException(String.valueOf(n));
            }
            case 45: {
                n2 = 1;
                s = "codePointAt";
                break;
            }
            case 44: {
                n2 = 1;
                s = "repeat";
                break;
            }
            case 43: {
                n2 = 0;
                s = "normalize";
                break;
            }
            case 42: {
                n2 = 1;
                s = "endsWith";
                break;
            }
            case 41: {
                n2 = 1;
                s = "startsWith";
                break;
            }
            case 40: {
                n2 = 1;
                s = "includes";
                break;
            }
            case 39: {
                n2 = 0;
                s = "trimRight";
                break;
            }
            case 38: {
                n2 = 0;
                s = "trimLeft";
                break;
            }
            case 37: {
                n2 = 0;
                s = "trim";
                break;
            }
            case 36: {
                n2 = 0;
                s = "toLocaleUpperCase";
                break;
            }
            case 35: {
                n2 = 0;
                s = "toLocaleLowerCase";
                break;
            }
            case 34: {
                n2 = 1;
                s = "localeCompare";
                break;
            }
            case 33: {
                n2 = 2;
                s = "replace";
                break;
            }
            case 32: {
                n2 = 1;
                s = "search";
                break;
            }
            case 31: {
                n2 = 1;
                s = "match";
                break;
            }
            case 30: {
                n2 = 1;
                s = "equalsIgnoreCase";
                break;
            }
            case 29: {
                n2 = 1;
                s = "equals";
                break;
            }
            case 28: {
                n2 = 0;
                s = "anchor";
                break;
            }
            case 27: {
                n2 = 0;
                s = "link";
                break;
            }
            case 26: {
                n2 = 0;
                s = "fontcolor";
                break;
            }
            case 25: {
                n2 = 0;
                s = "fontsize";
                break;
            }
            case 24: {
                n2 = 0;
                s = "sub";
                break;
            }
            case 23: {
                n2 = 0;
                s = "sup";
                break;
            }
            case 22: {
                n2 = 0;
                s = "blink";
                break;
            }
            case 21: {
                n2 = 0;
                s = "big";
                break;
            }
            case 20: {
                n2 = 0;
                s = "small";
                break;
            }
            case 19: {
                n2 = 0;
                s = "strike";
                break;
            }
            case 18: {
                n2 = 0;
                s = "fixed";
                break;
            }
            case 17: {
                n2 = 0;
                s = "italics";
                break;
            }
            case 16: {
                n2 = 0;
                s = "bold";
                break;
            }
            case 15: {
                n2 = 2;
                s = "slice";
                break;
            }
            case 14: {
                n2 = 1;
                s = "concat";
                break;
            }
            case 13: {
                n2 = 2;
                s = "substr";
                break;
            }
            case 12: {
                n2 = 0;
                s = "toUpperCase";
                break;
            }
            case 11: {
                n2 = 0;
                s = "toLowerCase";
                break;
            }
            case 10: {
                n2 = 2;
                s = "substring";
                break;
            }
            case 9: {
                n2 = 2;
                s = "split";
                break;
            }
            case 8: {
                n2 = 1;
                s = "lastIndexOf";
                break;
            }
            case 7: {
                n2 = 1;
                s = "indexOf";
                break;
            }
            case 6: {
                n2 = 1;
                s = "charCodeAt";
                break;
            }
            case 5: {
                n2 = 1;
                s = "charAt";
                break;
            }
            case 4: {
                n2 = 0;
                s = "valueOf";
                break;
            }
            case 3: {
                n2 = 0;
                s = "toSource";
                break;
            }
            case 2: {
                n2 = 0;
                s = "toString";
                break;
            }
            case 1: {
                n2 = 1;
                s = "constructor";
                break;
            }
        }
        this.initPrototypeMethod(NativeString.STRING_TAG, n, s, n2);
    }
    
    @Override
    public void put(final int n, final Scriptable scriptable, final Object o) {
        if (n >= 0 && n < this.string.length()) {
            return;
        }
        super.put(n, scriptable, o);
    }
    
    public CharSequence toCharSequence() {
        return this.string;
    }
    
    @Override
    public String toString() {
        if (this.string instanceof String) {
            return (String)this.string;
        }
        return this.string.toString();
    }
}
