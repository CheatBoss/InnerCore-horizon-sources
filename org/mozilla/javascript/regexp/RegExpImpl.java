package org.mozilla.javascript.regexp;

import org.mozilla.javascript.*;

public class RegExpImpl implements RegExpProxy
{
    protected String input;
    protected SubString lastMatch;
    protected SubString lastParen;
    protected SubString leftContext;
    protected boolean multiline;
    protected SubString[] parens;
    protected SubString rightContext;
    
    private static NativeRegExp createRegExp(final Context context, final Scriptable scriptable, final Object[] array, final int n, final boolean b) {
        final Scriptable topLevelScope = ScriptableObject.getTopLevelScope(scriptable);
        if (array.length == 0 || array[0] == Undefined.instance) {
            return new NativeRegExp(topLevelScope, NativeRegExp.compileRE(context, "", "", false));
        }
        if (array[0] instanceof NativeRegExp) {
            return (NativeRegExp)array[0];
        }
        final String string = ScriptRuntime.toString(array[0]);
        String string2;
        if (n < array.length) {
            array[0] = string;
            string2 = ScriptRuntime.toString(array[n]);
        }
        else {
            string2 = null;
        }
        return new NativeRegExp(topLevelScope, NativeRegExp.compileRE(context, string, string2, b));
    }
    
    private static void do_replace(final GlobData globData, final Context context, final RegExpImpl regExpImpl) {
        final StringBuilder charBuf = globData.charBuf;
        int n = 0;
        int n2 = 0;
        final String repstr = globData.repstr;
        int dollar = globData.dollar;
        if (dollar != -1) {
            final int[] array = { 0 };
            int i;
            do {
                charBuf.append(repstr.substring(n2, dollar));
                n = dollar;
                final SubString interpretDollar = interpretDollar(context, regExpImpl, repstr, dollar, array);
                int n3;
                if (interpretDollar != null) {
                    final int length = interpretDollar.length;
                    if (length > 0) {
                        charBuf.append(interpretDollar.str, interpretDollar.index, interpretDollar.index + length);
                    }
                    n += array[0];
                    n3 = dollar + array[0];
                }
                else {
                    n3 = dollar + 1;
                }
                i = repstr.indexOf(36, n3);
                n2 = n;
                dollar = i;
            } while (i >= 0);
        }
        final int length2 = repstr.length();
        if (length2 > n) {
            charBuf.append(repstr.substring(n, length2));
        }
    }
    
    private static int find_split(final Context context, final Scriptable scriptable, final String s, final String s2, int index, final RegExpProxy regExpProxy, final Scriptable scriptable2, final int[] array, final int[] array2, final boolean[] array3, final String[][] array4) {
        final int n = array[0];
        final int length = s.length();
        if (index == 120 && scriptable2 == null && s2.length() == 1 && s2.charAt(0) == ' ') {
            int n2;
            if ((n2 = n) == 0) {
                for (n2 = n; n2 < length && Character.isWhitespace(s.charAt(n2)); ++n2) {}
                array[0] = n2;
            }
            if ((index = n2) == length) {
                return -1;
            }
            while (index < length && !Character.isWhitespace(s.charAt(index))) {
                ++index;
            }
            int n3;
            for (n3 = index; n3 < length && Character.isWhitespace(s.charAt(n3)); ++n3) {}
            array2[0] = n3 - index;
            return index;
        }
        else {
            if (n > length) {
                return -1;
            }
            if (scriptable2 != null) {
                return regExpProxy.find_split(context, scriptable, s, s2, scriptable2, array, array2, array3, array4);
            }
            if (index != 0 && index < 130 && length == 0) {
                return -1;
            }
            if (s2.length() == 0) {
                if (index == 120) {
                    if (n == length) {
                        array2[0] = 1;
                        return n;
                    }
                    return n + 1;
                }
                else {
                    if (n == length) {
                        return -1;
                    }
                    return n + 1;
                }
            }
            else {
                if (array[0] >= length) {
                    return length;
                }
                index = s.indexOf(s2, array[0]);
                if (index != -1) {
                    return index;
                }
                return length;
            }
        }
    }
    
    private static SubString interpretDollar(final Context context, final RegExpImpl regExpImpl, final String s, final int n, final int[] array) {
        if (s.charAt(n) != '$') {
            Kit.codeBug();
        }
        final int languageVersion = context.getLanguageVersion();
        if (languageVersion != 0 && languageVersion <= 140 && n > 0 && s.charAt(n - 1) == '\\') {
            return null;
        }
        final int length = s.length();
        if (n + 1 >= length) {
            return null;
        }
        final char char1 = s.charAt(n + 1);
        if (NativeRegExp.isDigit(char1)) {
            int n4;
            int n5;
            if (languageVersion != 0 && languageVersion <= 140) {
                if (char1 == '0') {
                    return null;
                }
                int n2 = 0;
                int n3 = n;
                while (true) {
                    n3 = (n4 = n3 + 1);
                    n5 = n2;
                    if (n3 >= length) {
                        break;
                    }
                    final char char2 = s.charAt(n3);
                    n4 = n3;
                    n5 = n2;
                    if (!NativeRegExp.isDigit(char2)) {
                        break;
                    }
                    final int n6 = n2 * 10 + (char2 - '0');
                    if (n6 < n2) {
                        n4 = n3;
                        n5 = n2;
                        break;
                    }
                    n2 = n6;
                }
            }
            else {
                int length2;
                if (regExpImpl.parens == null) {
                    length2 = 0;
                }
                else {
                    length2 = regExpImpl.parens.length;
                }
                final int n7 = char1 - '0';
                if (n7 > length2) {
                    return null;
                }
                final int n8 = n + 2;
                int n9 = n7;
                int n10 = n8;
                if (n + 2 < length) {
                    final char char3 = s.charAt(n + 2);
                    n9 = n7;
                    n10 = n8;
                    if (NativeRegExp.isDigit(char3)) {
                        final int n11 = n7 * 10 + (char3 - '0');
                        n9 = n7;
                        n10 = n8;
                        if (n11 <= length2) {
                            n10 = n8 + 1;
                            n9 = n11;
                        }
                    }
                }
                if (n9 == 0) {
                    return null;
                }
                n5 = n9;
                n4 = n10;
            }
            array[0] = n4 - n;
            return regExpImpl.getParenSubString(n5 - 1);
        }
        array[0] = 2;
        if (char1 == '$') {
            return new SubString("$");
        }
        if (char1 == '+') {
            return regExpImpl.lastParen;
        }
        if (char1 == '`') {
            if (languageVersion == 120) {
                regExpImpl.leftContext.index = 0;
                regExpImpl.leftContext.length = regExpImpl.lastMatch.index;
            }
            return regExpImpl.leftContext;
        }
        switch (char1) {
            default: {
                return null;
            }
            case '\'': {
                return regExpImpl.rightContext;
            }
            case '&': {
                return regExpImpl.lastMatch;
            }
        }
    }
    
    private static Object matchOrReplace(final Context context, final Scriptable scriptable, final Scriptable scriptable2, final Object[] array, final RegExpImpl regExpImpl, final GlobData globData, final NativeRegExp nativeRegExp) {
        final String str = globData.str;
        final int flags = nativeRegExp.getFlags();
        int n = 1;
        globData.global = ((flags & 0x1) != 0x0);
        final int[] array2 = { 0 };
        Object executeRegExp = null;
        if (globData.mode == 3) {
            final Object executeRegExp2 = nativeRegExp.executeRegExp(context, scriptable, regExpImpl, str, array2, 0);
            Integer n2;
            if (executeRegExp2 != null && executeRegExp2.equals(Boolean.TRUE)) {
                n2 = regExpImpl.leftContext.length;
            }
            else {
                n2 = -1;
            }
            return n2;
        }
        if (globData.global) {
            nativeRegExp.lastIndex = 0.0;
            int n3 = 0;
            Object o;
            while (true) {
                o = executeRegExp;
                if (array2[0] > str.length()) {
                    break;
                }
                executeRegExp = nativeRegExp.executeRegExp(context, scriptable, regExpImpl, str, array2, 0);
                if ((o = executeRegExp) == null) {
                    break;
                }
                if (!executeRegExp.equals(Boolean.TRUE)) {
                    o = executeRegExp;
                    break;
                }
                if (globData.mode == 1) {
                    match_glob(globData, context, scriptable, n3, regExpImpl);
                }
                else {
                    if (globData.mode != 2) {
                        Kit.codeBug();
                    }
                    final SubString lastMatch = regExpImpl.lastMatch;
                    final int leftIndex = globData.leftIndex;
                    final int index = lastMatch.index;
                    globData.leftIndex = lastMatch.index + lastMatch.length;
                    replace_glob(globData, context, scriptable, regExpImpl, leftIndex, index - leftIndex);
                }
                if (regExpImpl.lastMatch.length == 0) {
                    if (array2[0] == str.length()) {
                        return executeRegExp;
                    }
                    ++array2[0];
                }
                ++n3;
            }
            executeRegExp = o;
            return executeRegExp;
        }
        if (globData.mode == 2) {
            n = 0;
        }
        return nativeRegExp.executeRegExp(context, scriptable, regExpImpl, str, array2, n);
    }
    
    private static void match_glob(final GlobData globData, final Context context, final Scriptable scriptable, final int n, final RegExpImpl regExpImpl) {
        if (globData.arrayobj == null) {
            globData.arrayobj = context.newArray(scriptable, 0);
        }
        globData.arrayobj.put(n, globData.arrayobj, regExpImpl.lastMatch.toString());
    }
    
    private static void replace_glob(final GlobData globData, final Context context, Scriptable o, final RegExpImpl regExpImpl, final int n, final int n2) {
        final Function lambda = globData.lambda;
        int i = 0;
        int n3 = 0;
        Label_0346: {
            if (lambda != null) {
                final SubString[] parens = regExpImpl.parens;
                if (parens == null) {
                    n3 = 0;
                }
                else {
                    n3 = parens.length;
                }
                final Object[] array = new Object[n3 + 3];
                array[0] = regExpImpl.lastMatch.toString();
                while (i < n3) {
                    final SubString subString = parens[i];
                    if (subString != null) {
                        array[i + 1] = subString.toString();
                    }
                    else {
                        array[i + 1] = Undefined.instance;
                    }
                    ++i;
                }
                array[n3 + 1] = regExpImpl.leftContext.length;
                array[n3 + 2] = globData.str;
                if (regExpImpl != ScriptRuntime.getRegExpProxy(context)) {
                    Kit.codeBug();
                }
                final RegExpImpl regExpImpl2 = new RegExpImpl();
                regExpImpl2.multiline = regExpImpl.multiline;
                regExpImpl2.input = regExpImpl.input;
                ScriptRuntime.setRegExpProxy(context, regExpImpl2);
                try {
                    o = ScriptableObject.getTopLevelScope((Scriptable)o);
                    o = ScriptRuntime.toString(globData.lambda.call(context, (Scriptable)o, (Scriptable)o, array));
                    ScriptRuntime.setRegExpProxy(context, regExpImpl);
                    n3 = ((String)o).length();
                    break Label_0346;
                }
                finally {
                    ScriptRuntime.setRegExpProxy(context, regExpImpl);
                }
            }
            final Object o2 = null;
            int n4 = n3 = globData.repstr.length();
            o = o2;
            if (globData.dollar >= 0) {
                final int[] array2 = { 0 };
                int dollar = globData.dollar;
                int j;
                do {
                    final SubString interpretDollar = interpretDollar(context, regExpImpl, globData.repstr, dollar, array2);
                    int n5;
                    if (interpretDollar != null) {
                        n3 = n4 + (interpretDollar.length - array2[0]);
                        n5 = dollar + array2[0];
                    }
                    else {
                        n5 = dollar + 1;
                        n3 = n4;
                    }
                    j = globData.repstr.indexOf(36, n5);
                    n4 = n3;
                    dollar = j;
                } while (j >= 0);
                o = o2;
            }
        }
        final int n6 = n2 + n3 + regExpImpl.rightContext.length;
        StringBuilder charBuf = globData.charBuf;
        if (charBuf == null) {
            charBuf = new StringBuilder(n6);
            globData.charBuf = charBuf;
        }
        else {
            charBuf.ensureCapacity(globData.charBuf.length() + n6);
        }
        charBuf.append(regExpImpl.leftContext.str, n, n + n2);
        if (globData.lambda != null) {
            charBuf.append((String)o);
            return;
        }
        do_replace(globData, context, regExpImpl);
    }
    
    @Override
    public Object action(final Context context, final Scriptable scriptable, final Scriptable scriptable2, final Object[] array, int index) {
        final GlobData globData = new GlobData();
        globData.mode = index;
        globData.str = ScriptRuntime.toString(scriptable2);
        switch (index) {
            default: {
                throw Kit.codeBug();
            }
            case 3: {
                return matchOrReplace(context, scriptable, scriptable2, array, this, globData, createRegExp(context, scriptable, array, 1, false));
            }
            case 2: {
                if ((array.length > 0 && array[0] instanceof NativeRegExp) || array.length > 2) {
                    index = 1;
                }
                else {
                    index = 0;
                }
                NativeRegExp regExp = null;
                String string = null;
                if (index != 0) {
                    regExp = createRegExp(context, scriptable, array, 2, true);
                }
                else {
                    Object instance;
                    if (array.length < 1) {
                        instance = Undefined.instance;
                    }
                    else {
                        instance = array[0];
                    }
                    string = ScriptRuntime.toString(instance);
                }
                Object instance2;
                if (array.length < 2) {
                    instance2 = Undefined.instance;
                }
                else {
                    instance2 = array[1];
                }
                final String s = null;
                Function lambda = null;
                String string2;
                if (instance2 instanceof Function) {
                    lambda = (Function)instance2;
                    string2 = s;
                }
                else {
                    string2 = ScriptRuntime.toString(instance2);
                }
                globData.lambda = lambda;
                globData.repstr = string2;
                int index2;
                if (string2 == null) {
                    index2 = -1;
                }
                else {
                    index2 = string2.indexOf(36);
                }
                globData.dollar = index2;
                globData.charBuf = null;
                globData.leftIndex = 0;
                Object o;
                if (index != 0) {
                    o = matchOrReplace(context, scriptable, scriptable2, array, this, globData, regExp);
                }
                else {
                    final String str = globData.str;
                    index = str.indexOf(string);
                    if (index >= 0) {
                        final int length = string.length();
                        this.lastParen = null;
                        this.leftContext = new SubString(str, 0, index);
                        this.lastMatch = new SubString(str, index, length);
                        this.rightContext = new SubString(str, index + length, str.length() - index - length);
                        o = Boolean.TRUE;
                    }
                    else {
                        o = Boolean.FALSE;
                    }
                }
                if (globData.charBuf == null) {
                    if (globData.global || o == null || !o.equals(Boolean.TRUE)) {
                        return globData.str;
                    }
                    final SubString leftContext = this.leftContext;
                    replace_glob(globData, context, scriptable, this, leftContext.index, leftContext.length);
                }
                final SubString rightContext = this.rightContext;
                globData.charBuf.append(rightContext.str, rightContext.index, rightContext.index + rightContext.length);
                return globData.charBuf.toString();
            }
            case 1: {
                final Object matchOrReplace = matchOrReplace(context, scriptable, scriptable2, array, this, globData, createRegExp(context, scriptable, array, 1, false));
                if (globData.arrayobj == null) {
                    return matchOrReplace;
                }
                return globData.arrayobj;
            }
        }
    }
    
    @Override
    public Object compileRegExp(final Context context, final String s, final String s2) {
        return NativeRegExp.compileRE(context, s, s2, false);
    }
    
    @Override
    public int find_split(final Context context, final Scriptable scriptable, final String s, final String s2, final Scriptable scriptable2, final int[] array, final int[] array2, final boolean[] array3, final String[][] array4) {
        int n = array[0];
        final int length = s.length();
        final int languageVersion = context.getLanguageVersion();
        final NativeRegExp nativeRegExp = (NativeRegExp)scriptable2;
        int n3;
        while (true) {
            final int n2 = array[0];
            array[0] = n;
            if (nativeRegExp.executeRegExp(context, scriptable, this, s, array, 0) != Boolean.TRUE) {
                array[0] = n2;
                array2[0] = 1;
                array3[0] = false;
                return length;
            }
            n3 = array[0];
            array[0] = n2;
            array3[0] = true;
            array2[0] = this.lastMatch.length;
            if (array2[0] != 0 || n3 != array[0]) {
                n3 -= array2[0];
                break;
            }
            if (n3 == length) {
                if (languageVersion == 120) {
                    array2[0] = 1;
                    break;
                }
                n3 = -1;
                break;
            }
            else {
                n = n3 + 1;
            }
        }
        int length2;
        if (this.parens == null) {
            length2 = 0;
        }
        else {
            length2 = this.parens.length;
        }
        array4[0] = new String[length2];
        for (int i = 0; i < length2; ++i) {
            array4[0][i] = this.getParenSubString(i).toString();
        }
        return n3;
    }
    
    SubString getParenSubString(final int n) {
        if (this.parens != null && n < this.parens.length) {
            final SubString subString = this.parens[n];
            if (subString != null) {
                return subString;
            }
        }
        return SubString.emptySubString;
    }
    
    @Override
    public boolean isRegExp(final Scriptable scriptable) {
        return scriptable instanceof NativeRegExp;
    }
    
    @Override
    public Object js_split(final Context context, final Scriptable scriptable, final String s, final Object[] array) {
        final Scriptable array2;
        final Scriptable scriptable2 = array2 = context.newArray(scriptable, 0);
        final boolean b = array.length > 1 && array[1] != Undefined.instance;
        long uint32 = 0L;
        if (b && (uint32 = ScriptRuntime.toUint32(array[1])) > s.length()) {
            uint32 = s.length() + 1;
        }
        if (array.length < 1 || array[0] == Undefined.instance) {
            scriptable2.put(0, scriptable2, s);
            return scriptable2;
        }
        String string = null;
        final int[] array3 = { 0 };
        final Scriptable scriptable3 = null;
        RegExpProxy regExpProxy = null;
        Scriptable scriptable4 = scriptable3;
        if (array[0] instanceof Scriptable) {
            final RegExpProxy regExpProxy2 = ScriptRuntime.getRegExpProxy(context);
            scriptable4 = scriptable3;
            if ((regExpProxy = regExpProxy2) != null) {
                final Scriptable scriptable5 = (Scriptable)array[0];
                scriptable4 = scriptable3;
                regExpProxy = regExpProxy2;
                if (regExpProxy2.isRegExp(scriptable5)) {
                    scriptable4 = scriptable5;
                    regExpProxy = regExpProxy2;
                }
            }
        }
        if (scriptable4 == null) {
            string = ScriptRuntime.toString(array[0]);
            array3[0] = string.length();
        }
        final int[] array4 = { 0 };
        final boolean[] array5 = { false };
        final String[][] array6 = { null };
        final int languageVersion = context.getLanguageVersion();
        int n = 0;
        final int[] array7 = array3;
        while (true) {
            final int find_split = find_split(context, scriptable, s, string, languageVersion, regExpProxy, scriptable4, array4, array7, array5, array6);
            if (find_split < 0) {
                return array2;
            }
            if ((b && n >= uint32) || find_split > s.length()) {
                return array2;
            }
            String substring;
            if (s.length() == 0) {
                substring = s;
            }
            else {
                substring = s.substring(array4[0], find_split);
            }
            array2.put(n, array2, substring);
            ++n;
            if (scriptable4 != null) {
                if (array5[0]) {
                    for (int length = array6[0].length, n2 = 0; n2 < length && (!b || n < uint32); ++n, ++n2) {
                        array2.put(n, array2, array6[0][n2]);
                    }
                    array5[0] = false;
                }
            }
            array4[0] = array7[0] + find_split;
            if (languageVersion < 130 && languageVersion != 0 && !b && array4[0] == s.length()) {
                return array2;
            }
        }
    }
    
    @Override
    public Scriptable wrapRegExp(final Context context, final Scriptable scriptable, final Object o) {
        return new NativeRegExp(scriptable, (RECompiled)o);
    }
}
