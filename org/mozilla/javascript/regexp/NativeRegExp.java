package org.mozilla.javascript.regexp;

import org.mozilla.javascript.*;

public class NativeRegExp extends IdScriptableObject implements Function
{
    private static final int ANCHOR_BOL = -2;
    private static final int INDEX_LEN = 2;
    private static final int Id_compile = 1;
    private static final int Id_exec = 4;
    private static final int Id_global = 3;
    private static final int Id_ignoreCase = 4;
    private static final int Id_lastIndex = 1;
    private static final int Id_multiline = 5;
    private static final int Id_prefix = 6;
    private static final int Id_source = 2;
    private static final int Id_test = 5;
    private static final int Id_toSource = 3;
    private static final int Id_toString = 2;
    public static final int JSREG_FOLD = 2;
    public static final int JSREG_GLOB = 1;
    public static final int JSREG_MULTILINE = 4;
    public static final int MATCH = 1;
    private static final int MAX_INSTANCE_ID = 5;
    private static final int MAX_PROTOTYPE_ID = 6;
    public static final int PREFIX = 2;
    private static final Object REGEXP_TAG;
    private static final byte REOP_ALNUM = 9;
    private static final byte REOP_ALT = 31;
    private static final byte REOP_ALTPREREQ = 53;
    private static final byte REOP_ALTPREREQ2 = 55;
    private static final byte REOP_ALTPREREQi = 54;
    private static final byte REOP_ASSERT = 41;
    private static final byte REOP_ASSERTNOTTEST = 44;
    private static final byte REOP_ASSERTTEST = 43;
    private static final byte REOP_ASSERT_NOT = 42;
    private static final byte REOP_BACKREF = 13;
    private static final byte REOP_BOL = 2;
    private static final byte REOP_CLASS = 22;
    private static final byte REOP_DIGIT = 7;
    private static final byte REOP_DOT = 6;
    private static final byte REOP_EMPTY = 1;
    private static final byte REOP_END = 57;
    private static final byte REOP_ENDCHILD = 49;
    private static final byte REOP_EOL = 3;
    private static final byte REOP_FLAT = 14;
    private static final byte REOP_FLAT1 = 15;
    private static final byte REOP_FLAT1i = 17;
    private static final byte REOP_FLATi = 16;
    private static final byte REOP_JUMP = 32;
    private static final byte REOP_LPAREN = 29;
    private static final byte REOP_MINIMALOPT = 47;
    private static final byte REOP_MINIMALPLUS = 46;
    private static final byte REOP_MINIMALQUANT = 48;
    private static final byte REOP_MINIMALREPEAT = 52;
    private static final byte REOP_MINIMALSTAR = 45;
    private static final byte REOP_NCLASS = 23;
    private static final byte REOP_NONALNUM = 10;
    private static final byte REOP_NONDIGIT = 8;
    private static final byte REOP_NONSPACE = 12;
    private static final byte REOP_OPT = 28;
    private static final byte REOP_PLUS = 27;
    private static final byte REOP_QUANT = 25;
    private static final byte REOP_REPEAT = 51;
    private static final byte REOP_RPAREN = 30;
    private static final byte REOP_SIMPLE_END = 23;
    private static final byte REOP_SIMPLE_START = 1;
    private static final byte REOP_SPACE = 11;
    private static final byte REOP_STAR = 26;
    private static final byte REOP_UCFLAT1 = 18;
    private static final byte REOP_UCFLAT1i = 19;
    private static final byte REOP_WBDRY = 4;
    private static final byte REOP_WNONBDRY = 5;
    public static final int TEST = 0;
    private static final boolean debug = false;
    static final long serialVersionUID = 4965263491464903264L;
    Object lastIndex;
    private int lastIndexAttr;
    private RECompiled re;
    
    static {
        REGEXP_TAG = new Object();
    }
    
    NativeRegExp() {
        this.lastIndex = 0.0;
        this.lastIndexAttr = 6;
    }
    
    NativeRegExp(final Scriptable scriptable, final RECompiled re) {
        this.lastIndex = 0.0;
        this.lastIndexAttr = 6;
        this.re = re;
        this.lastIndex = 0.0;
        ScriptRuntime.setBuiltinProtoAndParent(this, scriptable, TopLevel.Builtins.RegExp);
    }
    
    private static void addCharacterRangeToCharSet(final RECharSet set, final char c, final char c2) {
        final int n = c / '\b';
        final int n2 = c2 / '\b';
        if (c2 >= set.length || c > c2) {
            throw ScriptRuntime.constructError("SyntaxError", "invalid range in character class");
        }
        final char c3 = (char)(c & '\u0007');
        final char c4 = (char)(c2 & '\u0007');
        if (n == n2) {
            final byte[] bits = set.bits;
            bits[n] |= (byte)(255 >> 7 - (c4 - c3) << c3);
            return;
        }
        final byte[] bits2 = set.bits;
        bits2[n] |= (byte)(255 << c3);
        for (int i = n + 1; i < n2; ++i) {
            set.bits[i] = -1;
        }
        final byte[] bits3 = set.bits;
        bits3[n2] |= (byte)(255 >> '\u0007' - c4);
    }
    
    private static void addCharacterToCharSet(final RECharSet set, final char c) {
        final int n = c / '\b';
        if (c >= set.length) {
            throw ScriptRuntime.constructError("SyntaxError", "invalid range in character class");
        }
        final byte[] bits = set.bits;
        bits[n] |= (byte)(1 << (c & '\u0007'));
    }
    
    private static int addIndex(final byte[] array, final int n, final int n2) {
        if (n2 < 0) {
            throw Kit.codeBug();
        }
        if (n2 > 65535) {
            throw Context.reportRuntimeError("Too complex regexp");
        }
        array[n] = (byte)(n2 >> 8);
        array[n + 1] = (byte)n2;
        return n + 2;
    }
    
    private static boolean backrefMatcher(final REGlobalData reGlobalData, int i, final String s, final int n) {
        if (reGlobalData.parens == null) {
            return false;
        }
        if (i >= reGlobalData.parens.length) {
            return false;
        }
        final int parensIndex = reGlobalData.parensIndex(i);
        if (parensIndex == -1) {
            return true;
        }
        final int parensLength = reGlobalData.parensLength(i);
        if (reGlobalData.cp + parensLength > n) {
            return false;
        }
        if ((reGlobalData.regexp.flags & 0x2) != 0x0) {
            char char1;
            char char2;
            for (i = 0; i < parensLength; ++i) {
                char1 = s.charAt(parensIndex + i);
                char2 = s.charAt(reGlobalData.cp + i);
                if (char1 != char2 && upcase(char1) != upcase(char2)) {
                    return false;
                }
            }
        }
        else if (!s.regionMatches(parensIndex, s, reGlobalData.cp, parensLength)) {
            return false;
        }
        reGlobalData.cp += parensLength;
        return true;
    }
    
    private static boolean calculateBitmapSize(final CompilerState compilerState, final RENode reNode, final char[] array, int n, final int n2) {
        int n3 = 0;
        int n4 = 0;
        reNode.bmsize = 0;
        reNode.sense = true;
        if (n == n2) {
            return true;
        }
        int i = n;
        if (array[n] == '^') {
            i = n + 1;
            reNode.sense = false;
        }
        int n5 = 0;
        while (i != n2) {
            int n6 = 2;
            Label_0084: {
                if (array[i] != '\\') {
                    n = i + 1;
                    final int n7 = array[i];
                    i = n;
                    n = n7;
                }
                else {
                    n = i + 1;
                    int n8 = n + 1;
                    int n9 = array[n];
                    Label_0392: {
                        if (n9 != 68 && n9 != 83 && n9 != 87) {
                            Label_0259: {
                                if (n9 != 102) {
                                    if (n9 != 110) {
                                        switch (n9) {
                                            default: {
                                                switch (n9) {
                                                    default: {
                                                        switch (n9) {
                                                            default: {
                                                                n = n9;
                                                                break Label_0259;
                                                            }
                                                            case 118: {
                                                                n = 11;
                                                                break Label_0259;
                                                            }
                                                            case 117: {
                                                                n6 = 2 + 2;
                                                            }
                                                            case 120: {
                                                                n = 0;
                                                                int n10 = 0;
                                                                int n11;
                                                                int n12;
                                                                while (true) {
                                                                    n11 = n8;
                                                                    n12 = n;
                                                                    if (n10 >= n6) {
                                                                        break;
                                                                    }
                                                                    n11 = n8;
                                                                    n12 = n;
                                                                    if (n8 >= n2) {
                                                                        break;
                                                                    }
                                                                    final int n13 = n8 + 1;
                                                                    n9 = array[n8];
                                                                    n = Kit.xDigitToInt(n9, n);
                                                                    if (n < 0) {
                                                                        n11 = n13 - (n10 + 1);
                                                                        n = (n12 = 92);
                                                                        break;
                                                                    }
                                                                    ++n10;
                                                                    n8 = n13;
                                                                }
                                                                n = n12;
                                                                i = n11;
                                                                break Label_0084;
                                                            }
                                                            case 116: {
                                                                n = 9;
                                                                break Label_0259;
                                                            }
                                                            case 115:
                                                            case 119: {
                                                                break Label_0392;
                                                            }
                                                            case 114: {
                                                                n = 13;
                                                                break Label_0259;
                                                            }
                                                        }
                                                        break;
                                                    }
                                                    case 100: {
                                                        if (n5 != 0) {
                                                            reportError("msg.bad.range", "");
                                                            return false;
                                                        }
                                                        n = 57;
                                                        break Label_0259;
                                                    }
                                                    case 99: {
                                                        if (n8 < n2 && isControlLetter(array[n8])) {
                                                            i = n8 + 1;
                                                            n = (char)(array[n8] & '\u001f');
                                                        }
                                                        else {
                                                            i = n8 - 1;
                                                        }
                                                        n = 92;
                                                        break Label_0084;
                                                    }
                                                    case 98: {
                                                        n = 8;
                                                        break Label_0259;
                                                    }
                                                }
                                                break;
                                            }
                                            case 48:
                                            case 49:
                                            case 50:
                                            case 51:
                                            case 52:
                                            case 53:
                                            case 54:
                                            case 55: {
                                                final int n14 = n9 - 48;
                                                char c2;
                                                final char c = c2 = array[n8];
                                                n = n8;
                                                int n15 = n14;
                                                if ('0' <= c) {
                                                    n = n8;
                                                    n15 = n14;
                                                    if (c <= '7') {
                                                        final int n16 = n8 + 1;
                                                        final int n17 = n14 * 8 + (c - '0');
                                                        final char c3 = c2 = array[n16];
                                                        n = n16;
                                                        n15 = n17;
                                                        if ('0' <= c3) {
                                                            n = n16;
                                                            n15 = n17;
                                                            if (c3 <= '7') {
                                                                n = n16 + 1;
                                                                n15 = n17 * 8 + (c3 - '0');
                                                                if (n15 > 255) {
                                                                    --n;
                                                                    n15 = n17;
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                                final int n18 = n15;
                                                n8 = n;
                                                n = n18;
                                                break;
                                            }
                                        }
                                    }
                                    else {
                                        n = 10;
                                    }
                                }
                                else {
                                    n = 12;
                                }
                            }
                            i = n8;
                            break Label_0084;
                        }
                    }
                    if (n5 != 0) {
                        reportError("msg.bad.range", "");
                        return false;
                    }
                    reNode.bmsize = 65536;
                    return true;
                }
            }
            int n19;
            if (n5 != 0) {
                if (n3 > n) {
                    reportError("msg.bad.range", "");
                    return false;
                }
                n19 = 0;
            }
            else {
                n19 = n5;
                if (i < n2 - 1) {
                    n19 = n5;
                    if (array[i] == '-') {
                        ++i;
                        n5 = 1;
                        n3 = (char)n;
                        continue;
                    }
                }
            }
            int n20 = n;
            if ((compilerState.flags & 0x2) != 0x0) {
                final int upcase = upcase((char)n);
                n = downcase((char)n);
                if (upcase >= n) {
                    n = upcase;
                }
                n20 = n;
            }
            if (n20 > (n = n4)) {
                n = n20;
            }
            n4 = n;
            n5 = n19;
        }
        reNode.bmsize = n4 + 1;
        return true;
    }
    
    private static boolean classMatcher(final REGlobalData reGlobalData, final RECharSet set, final char c) {
        if (!set.converted) {
            processCharSet(reGlobalData, set);
        }
        final int length = set.length;
        boolean b2;
        final boolean b = b2 = true;
        if (length != 0) {
            b2 = b;
            if (c < set.length) {
                b2 = ((set.bits[c >> 3] & 1 << (c & '\u0007')) == 0x0 && b);
            }
        }
        return set.sense ^ b2;
    }
    
    static RECompiled compileRE(final Context context, final String s, final String s2, final boolean b) {
        final RECompiled reCompiled = new RECompiled(s);
        final int length = s.length();
        int flags = 0;
        if (s2 != null) {
            flags = 0;
            for (int i = 0; i < s2.length(); ++i) {
                final char char1 = s2.charAt(i);
                int n = 0;
                if (char1 == 'g') {
                    n = 1;
                }
                else if (char1 == 'i') {
                    n = 2;
                }
                else if (char1 == 'm') {
                    n = 4;
                }
                else {
                    reportError("msg.invalid.re.flag", String.valueOf(char1));
                }
                if ((flags & n) != 0x0) {
                    reportError("msg.invalid.re.flag", String.valueOf(char1));
                }
                flags |= n;
            }
        }
        reCompiled.flags = flags;
        final CompilerState compilerState = new CompilerState(context, reCompiled.source, length, flags);
        CompilerState compilerState2;
        if (b && length > 0) {
            compilerState.result = new RENode((byte)14);
            compilerState.result.chr = compilerState.cpbegin[0];
            compilerState.result.length = length;
            compilerState.result.flatIndex = 0;
            compilerState.progLength += 5;
            compilerState2 = compilerState;
        }
        else {
            if (!parseDisjunction(compilerState)) {
                return null;
            }
            compilerState2 = compilerState;
            if (compilerState.maxBackReference > compilerState.parenCount) {
                final CompilerState compilerState3 = new CompilerState(context, reCompiled.source, length, flags);
                compilerState3.backReferenceLimit = compilerState3.parenCount;
                compilerState2 = compilerState3;
                if (!parseDisjunction(compilerState3)) {
                    return null;
                }
            }
        }
        reCompiled.program = new byte[compilerState2.progLength + 1];
        if (compilerState2.classCount != 0) {
            reCompiled.classList = new RECharSet[compilerState2.classCount];
            reCompiled.classCount = compilerState2.classCount;
        }
        reCompiled.program[emitREBytecode(compilerState2, reCompiled, 0, compilerState2.result)] = 57;
        reCompiled.parenCount = compilerState2.parenCount;
        final byte b2 = reCompiled.program[0];
        if (b2 != 2) {
            if (b2 != 31) {
                switch (b2) {
                    default: {
                        return reCompiled;
                    }
                    case 18:
                    case 19: {
                        reCompiled.anchorCh = (char)getIndex(reCompiled.program, 1);
                        return reCompiled;
                    }
                    case 15:
                    case 17: {
                        reCompiled.anchorCh = (char)(reCompiled.program[1] & 0xFF);
                        return reCompiled;
                    }
                    case 14:
                    case 16: {
                        reCompiled.anchorCh = reCompiled.source[getIndex(reCompiled.program, 1)];
                        return reCompiled;
                    }
                }
            }
            else {
                final RENode result = compilerState2.result;
                if (result.kid.op == 2 && result.kid2.op == 2) {
                    reCompiled.anchorCh = -2;
                    return reCompiled;
                }
            }
        }
        else {
            reCompiled.anchorCh = -2;
        }
        return reCompiled;
    }
    
    private static void doFlat(final CompilerState compilerState, final char chr) {
        compilerState.result = new RENode((byte)14);
        compilerState.result.chr = chr;
        compilerState.result.length = 1;
        compilerState.result.flatIndex = -1;
        compilerState.progLength += 3;
    }
    
    private static char downcase(final char c) {
        if (c < '\u0080') {
            if ('A' <= c && c <= 'Z') {
                return (char)(c + ' ');
            }
            return c;
        }
        else {
            final char lowerCase = Character.toLowerCase(c);
            if (lowerCase < '\u0080') {
                return c;
            }
            return lowerCase;
        }
    }
    
    private static int emitREBytecode(final CompilerState compilerState, final RECompiled reCompiled, int n, RENode next) {
        final byte[] program = reCompiled.program;
        int n2 = n;
        while (next != null) {
            n = n2 + 1;
            program[n2] = next.op;
            final byte op = next.op;
            final boolean b = true;
            int n3 = n;
            switch (op) {
                case 42: {
                    final int emitREBytecode = emitREBytecode(compilerState, reCompiled, n + 2, next.kid);
                    final int n4 = emitREBytecode + 1;
                    program[emitREBytecode] = 44;
                    resolveForwardJump(program, n, n4);
                    n = n4;
                    break;
                }
                case 41: {
                    final int emitREBytecode2 = emitREBytecode(compilerState, reCompiled, n + 2, next.kid);
                    final int n5 = emitREBytecode2 + 1;
                    program[emitREBytecode2] = 43;
                    resolveForwardJump(program, n, n5);
                    n = n5;
                    break;
                }
                case 53:
                case 54:
                case 55: {
                    final boolean b2 = next.op == 54 && b;
                    char c;
                    if (b2) {
                        c = upcase(next.chr);
                    }
                    else {
                        c = next.chr;
                    }
                    addIndex(program, n, c);
                    final int n8 = n + 2;
                    if (b2) {
                        n = upcase((char)next.index);
                    }
                    else {
                        n = next.index;
                    }
                    addIndex(program, n8, n);
                    n3 = n8 + 2;
                }
                case 31: {
                    final RENode kid2 = next.kid2;
                    n = emitREBytecode(compilerState, reCompiled, n3 + 2, next.kid);
                    final int n6 = n + 1;
                    program[n] = 32;
                    n = n6 + 2;
                    resolveForwardJump(program, n3, n);
                    n = emitREBytecode(compilerState, reCompiled, n, kid2);
                    final int n7 = n + 1;
                    program[n] = 32;
                    n = n7 + 2;
                    resolveForwardJump(program, n6, n);
                    resolveForwardJump(program, n7, n);
                    break;
                }
                case 29: {
                    n = emitREBytecode(compilerState, reCompiled, addIndex(program, n, next.parenIndex), next.kid);
                    program[n] = 30;
                    n = addIndex(program, n + 1, next.parenIndex);
                    break;
                }
                case 25: {
                    if (next.min == 0 && next.max == -1) {
                        byte b3;
                        if (next.greedy) {
                            b3 = 26;
                        }
                        else {
                            b3 = 45;
                        }
                        program[n - 1] = b3;
                    }
                    else if (next.min == 0 && next.max == 1) {
                        byte b4;
                        if (next.greedy) {
                            b4 = 28;
                        }
                        else {
                            b4 = 47;
                        }
                        program[n - 1] = b4;
                    }
                    else if (next.min == 1 && next.max == -1) {
                        byte b5;
                        if (next.greedy) {
                            b5 = 27;
                        }
                        else {
                            b5 = 46;
                        }
                        program[n - 1] = b5;
                    }
                    else {
                        if (!next.greedy) {
                            program[n - 1] = 48;
                        }
                        n = addIndex(program, addIndex(program, n, next.min), next.max + 1);
                    }
                    final int addIndex = addIndex(program, addIndex(program, n, next.parenCount), next.parenIndex);
                    final int emitREBytecode3 = emitREBytecode(compilerState, reCompiled, addIndex + 2, next.kid);
                    n = emitREBytecode3 + 1;
                    program[emitREBytecode3] = 49;
                    resolveForwardJump(program, addIndex, n);
                    break;
                }
                case 22: {
                    if (!next.sense) {
                        program[n - 1] = 23;
                    }
                    n = addIndex(program, n, next.index);
                    reCompiled.classList[next.index] = new RECharSet(next.bmsize, next.startIndex, next.kidlen, next.sense);
                    break;
                }
                case 14: {
                    if (next.flatIndex != -1) {
                        while (next.next != null && next.next.op == 14 && next.flatIndex + next.length == next.next.flatIndex) {
                            next.length += next.next.length;
                            next.next = next.next.next;
                        }
                    }
                    if (next.flatIndex != -1 && next.length > 1) {
                        if ((compilerState.flags & 0x2) != 0x0) {
                            program[n - 1] = 16;
                        }
                        else {
                            program[n - 1] = 14;
                        }
                        n = addIndex(program, addIndex(program, n, next.flatIndex), next.length);
                        break;
                    }
                    if (next.chr < '\u0100') {
                        if ((compilerState.flags & 0x2) != 0x0) {
                            program[n - 1] = 17;
                        }
                        else {
                            program[n - 1] = 15;
                        }
                        final int n9 = n + 1;
                        program[n] = (byte)next.chr;
                        n = n9;
                        break;
                    }
                    if ((compilerState.flags & 0x2) != 0x0) {
                        program[n - 1] = 19;
                    }
                    else {
                        program[n - 1] = 18;
                    }
                    n = addIndex(program, n, next.chr);
                    break;
                }
                case 13: {
                    n = addIndex(program, n, next.parenIndex);
                    break;
                }
                case 1: {
                    --n;
                    break;
                }
            }
            next = next.next;
            n2 = n;
        }
        return n2;
    }
    
    private static String escapeRegExp(Object o) {
        final String string = ScriptRuntime.toString(o);
        o = null;
        int n = 0;
        Object o2;
        int n2;
        for (int i = string.indexOf(47); i > -1; i = string.indexOf(47, i + 1), o = o2, n = n2) {
            if (i != n) {
                o2 = o;
                n2 = n;
                if (string.charAt(i - 1) == '\\') {
                    continue;
                }
            }
            if ((o2 = o) == null) {
                o2 = new StringBuilder();
            }
            ((StringBuilder)o2).append(string, n, i);
            ((StringBuilder)o2).append("\\/");
            n2 = i + 1;
        }
        String string2 = string;
        if (o != null) {
            ((StringBuilder)o).append(string, n, string.length());
            string2 = ((StringBuilder)o).toString();
        }
        return string2;
    }
    
    private Object execSub(final Context context, final Scriptable scriptable, final Object[] array, final int n) {
        final RegExpImpl impl = getImpl(context);
        String s;
        if (array.length == 0) {
            if ((s = impl.input) == null) {
                s = ScriptRuntime.toString(Undefined.instance);
            }
        }
        else {
            s = ScriptRuntime.toString(array[0]);
        }
        double integer = 0.0;
        if ((this.re.flags & 0x1) != 0x0) {
            integer = ScriptRuntime.toInteger(this.lastIndex);
        }
        Object executeRegExp;
        if (integer >= 0.0 && s.length() >= integer) {
            final int[] array2 = { (int)integer };
            final Object o = executeRegExp = this.executeRegExp(context, scriptable, impl, s, array2, n);
            if ((this.re.flags & 0x1) != 0x0) {
                double n2;
                if (o != null && o != Undefined.instance) {
                    n2 = array2[0];
                }
                else {
                    n2 = 0.0;
                }
                this.lastIndex = n2;
                return o;
            }
        }
        else {
            this.lastIndex = 0.0;
            executeRegExp = null;
        }
        return executeRegExp;
    }
    
    private static boolean executeREBytecode(final REGlobalData reGlobalData, final String s, final int n) {
        final byte[] program = reGlobalData.regexp.program;
        final int n2 = 57;
        final int n3 = 0;
        final boolean b = false;
        final int n4 = 0 + 1;
        int op = program[0];
        int pc = 0;
        int n5 = 0;
        int n6 = 0;
        int n7 = 0;
        Label_0170: {
            if (reGlobalData.regexp.anchorCh < 0 && reopIsSimple(op)) {
                final boolean b2 = false;
                while (true) {
                    while (reGlobalData.cp <= n) {
                        final int simpleMatch = simpleMatch(reGlobalData, s, op, program, n4, n, true);
                        if (simpleMatch >= 0) {
                            final boolean b3 = true;
                            pc = simpleMatch + 1;
                            op = program[simpleMatch];
                            if (!b3) {
                                return false;
                            }
                            n5 = n2;
                            n6 = n3;
                            n7 = (b ? 1 : 0);
                            break Label_0170;
                        }
                        else {
                            ++reGlobalData.skipped;
                            ++reGlobalData.cp;
                        }
                    }
                    final boolean b3 = b2;
                    pc = n4;
                    continue;
                }
            }
            pc = n4;
            n7 = (b ? 1 : 0);
            n6 = n3;
            n5 = n2;
        }
        int simpleMatch2;
        char c;
        int n8;
        char c2;
        char char1;
        char upcase;
        REProgState popProgState;
        int index;
        int n9;
        int index2;
        int n10;
        int n11 = 0;
        int n12 = 0;
        int min;
        int max;
        int n13;
        int n14;
        int index3;
        int n15;
        int index4;
        int n16;
        int n17;
        int n18;
        REProgState popProgState2;
        int continuationOp = 0;
        int n19;
        int n20;
        int min2;
        int max2;
        int n21;
        int n22;
        int n23;
        int simpleMatch3;
        byte b4;
        int cp;
        int n24;
        int n25;
        int n26;
        int n27;
        int index5;
        int index6;
        byte b5;
        REProgState popProgState3;
        int n28;
        int n29;
        int n30 = 0;
        byte b6 = 0;
        int simpleMatch4;
        int n31;
        int n32;
        byte b7;
        int n33;
        int n34;
        int n35;
        int cp2;
        int simpleMatch5;
        int index7;
        int n36;
        int parensIndex;
        int index8;
        int n37;
        int n38;
        int n39;
        int n40;
        int n41;
        int n42;
        int n43;
        int n44;
        int offset;
        int n45;
        int n46;
        int n47;
        int n48;
        int n49;
        int n50;
        REBackTrackData backTrackStackTop;
        Label_0972_Outer:Label_1996:Label_2173_Outer:
        while (true) {
            Label_1653: {
                if (reopIsSimple(op)) {
                    simpleMatch2 = simpleMatch(reGlobalData, s, op, program, pc, n, true);
                    if (simpleMatch2 >= 0) {
                        n7 = 1;
                    }
                    else {
                        n7 = 0;
                    }
                    if (n7 != 0) {
                        pc = simpleMatch2;
                    }
                }
                else {
                    if (op != 57) {
                        Label_2041: {
                            switch (op) {
                                default: {
                                    Label_1780: {
                                        switch (op) {
                                            default: {
                                            Label_0972:
                                                while (true) {
                                                    Label_1034: {
                                                        switch (op) {
                                                            default: {
                                                                throw Kit.codeBug("invalid bytecode");
                                                            }
                                                            case 53:
                                                            case 54:
                                                            case 55: {
                                                                c = (char)getIndex(program, pc);
                                                                n8 = pc + 2;
                                                                c2 = (char)getIndex(program, n8);
                                                                pc = n8 + 2;
                                                                if (reGlobalData.cp == n) {
                                                                    n7 = 0;
                                                                    break Label_1653;
                                                                }
                                                                char1 = s.charAt(reGlobalData.cp);
                                                                Label_0496: {
                                                                    if (op == 55) {
                                                                        if (char1 == c || classMatcher(reGlobalData, reGlobalData.regexp.classList[c2], char1)) {
                                                                            break Label_0496;
                                                                        }
                                                                    }
                                                                    else {
                                                                        upcase = char1;
                                                                        if (op == 54) {
                                                                            upcase = upcase(char1);
                                                                        }
                                                                        if (upcase == c || upcase == c2) {
                                                                            break Label_0496;
                                                                        }
                                                                    }
                                                                    n7 = 0;
                                                                    break Label_1653;
                                                                }
                                                                break Label_2041;
                                                            }
                                                            case 52: {
                                                                popProgState = popProgState(reGlobalData);
                                                                if (n7 == 0) {
                                                                    if (popProgState.max != -1 && popProgState.max <= 0) {
                                                                        n6 = popProgState.continuationPc;
                                                                        n5 = popProgState.continuationOp;
                                                                        break Label_1653;
                                                                    }
                                                                    pushProgState(reGlobalData, popProgState.min, popProgState.max, reGlobalData.cp, null, popProgState.continuationOp, popProgState.continuationPc);
                                                                    n5 = 52;
                                                                    n6 = pc;
                                                                    index = getIndex(program, pc);
                                                                    n9 = pc + 2;
                                                                    index2 = getIndex(program, n9);
                                                                    n10 = n9 + 4;
                                                                    for (int i = 0; i < index; ++i) {
                                                                        reGlobalData.setParens(index2 + i, -1, 0);
                                                                    }
                                                                    pc = n10 + 1;
                                                                    op = program[n10];
                                                                    continue Label_0972_Outer;
                                                                }
                                                                else {
                                                                    if (popProgState.min == 0 && reGlobalData.cp == popProgState.index) {
                                                                        n7 = 0;
                                                                        n11 = popProgState.continuationPc;
                                                                        n12 = popProgState.continuationOp;
                                                                        break Label_1034;
                                                                    }
                                                                    min = popProgState.min;
                                                                    max = popProgState.max;
                                                                    if ((n13 = min) != 0) {
                                                                        n13 = min - 1;
                                                                    }
                                                                    if ((n14 = max) != -1) {
                                                                        n14 = max - 1;
                                                                    }
                                                                    pushProgState(reGlobalData, n13, n14, reGlobalData.cp, null, popProgState.continuationOp, popProgState.continuationPc);
                                                                    if (n13 != 0) {
                                                                        n5 = 52;
                                                                        n6 = pc;
                                                                        index3 = getIndex(program, pc);
                                                                        n15 = pc + 2;
                                                                        index4 = getIndex(program, n15);
                                                                        n16 = n15 + 4;
                                                                        for (int j = 0; j < index3; ++j) {
                                                                            reGlobalData.setParens(index4 + j, -1, 0);
                                                                        }
                                                                        pc = n16 + 1;
                                                                        op = program[n16];
                                                                        continue Label_0972_Outer;
                                                                    }
                                                                    n6 = popProgState.continuationPc;
                                                                    n5 = popProgState.continuationOp;
                                                                    pushBackTrackState(reGlobalData, (byte)52, pc);
                                                                    popProgState(reGlobalData);
                                                                    n17 = pc + 4;
                                                                    n18 = n17 + getOffset(program, n17);
                                                                    pc = n18 + 1;
                                                                    op = program[n18];
                                                                    continue Label_0972_Outer;
                                                                }
                                                                break;
                                                            }
                                                            case 51: {
                                                                while (true) {
                                                                    popProgState2 = popProgState(reGlobalData);
                                                                    if (n7 == 0) {
                                                                        if (popProgState2.min == 0) {
                                                                            n7 = 1;
                                                                        }
                                                                        n6 = popProgState2.continuationPc;
                                                                        continuationOp = popProgState2.continuationOp;
                                                                        n19 = pc + 4;
                                                                        pc = n19 + getOffset(program, n19);
                                                                        break Label_0972;
                                                                    }
                                                                    if (popProgState2.min == 0 && reGlobalData.cp == popProgState2.index) {
                                                                        n7 = 0;
                                                                        n11 = popProgState2.continuationPc;
                                                                        n12 = popProgState2.continuationOp;
                                                                        n20 = pc + 4;
                                                                        pc = n20 + getOffset(program, n20);
                                                                        break Label_1034;
                                                                    }
                                                                    min2 = popProgState2.min;
                                                                    max2 = popProgState2.max;
                                                                    if ((n21 = min2) != 0) {
                                                                        n21 = min2 - 1;
                                                                    }
                                                                    if ((n22 = max2) != -1) {
                                                                        n22 = max2 - 1;
                                                                    }
                                                                    if (n22 == 0) {
                                                                        n7 = 1;
                                                                        n11 = popProgState2.continuationPc;
                                                                        n12 = popProgState2.continuationOp;
                                                                        n23 = pc + 4;
                                                                        pc = n23 + getOffset(program, n23);
                                                                        break Label_1034;
                                                                    }
                                                                    simpleMatch3 = pc + 6;
                                                                    b4 = program[simpleMatch3];
                                                                    cp = reGlobalData.cp;
                                                                    if (reopIsSimple(b4)) {
                                                                        simpleMatch3 = simpleMatch(reGlobalData, s, b4, program, simpleMatch3 + 1, n, true);
                                                                        if (simpleMatch3 < 0) {
                                                                            if (n21 == 0) {
                                                                                n7 = 1;
                                                                            }
                                                                            else {
                                                                                n7 = 0;
                                                                            }
                                                                            n6 = popProgState2.continuationPc;
                                                                            n5 = popProgState2.continuationOp;
                                                                            n24 = pc + 4;
                                                                            pc = n24 + getOffset(program, n24);
                                                                            break Label_1653;
                                                                        }
                                                                        n7 = 1;
                                                                    }
                                                                    n25 = cp;
                                                                    n26 = 51;
                                                                    n27 = pc;
                                                                    pushProgState(reGlobalData, n21, n22, n25, null, popProgState2.continuationOp, popProgState2.continuationPc);
                                                                    if (n21 == 0) {
                                                                        pushBackTrackState(reGlobalData, (byte)51, pc, n25, popProgState2.continuationOp, popProgState2.continuationPc);
                                                                        index5 = getIndex(program, pc);
                                                                        index6 = getIndex(program, pc + 2);
                                                                        for (int k = 0; k < index5; ++k) {
                                                                            reGlobalData.setParens(index6 + k, -1, 0);
                                                                        }
                                                                    }
                                                                    if (program[simpleMatch3] != 49) {
                                                                        pc = simpleMatch3 + 1;
                                                                        b5 = program[simpleMatch3];
                                                                        n5 = n26;
                                                                        n6 = n27;
                                                                        op = b5;
                                                                        continue Label_1996;
                                                                    }
                                                                }
                                                                break;
                                                            }
                                                        }
                                                        n5 = continuationOp;
                                                        break;
                                                    }
                                                    continuationOp = n12;
                                                    n6 = n11;
                                                    continue Label_0972;
                                                }
                                            }
                                            case 49: {
                                                n7 = 1;
                                                pc = n6;
                                                op = n5;
                                                continue;
                                            }
                                            case 45:
                                            case 46:
                                            case 47:
                                            case 48: {
                                                break Label_2041;
                                            }
                                            case 43:
                                            case 44: {
                                                popProgState3 = popProgState(reGlobalData);
                                                reGlobalData.cp = popProgState3.index;
                                                reGlobalData.backTrackStackTop = popProgState3.backTrack;
                                                n6 = popProgState3.continuationPc;
                                                n5 = popProgState3.continuationOp;
                                                if (op == 44) {
                                                    if (n7 == 0) {
                                                        n7 = 1;
                                                    }
                                                    else {
                                                        n7 = 0;
                                                    }
                                                    break;
                                                }
                                                break;
                                            }
                                            case 42: {
                                                n28 = pc + getIndex(program, pc);
                                                n29 = pc + 2;
                                                n30 = n29 + 1;
                                                b6 = program[n29];
                                                if (reopIsSimple(b6)) {
                                                    simpleMatch4 = simpleMatch(reGlobalData, s, b6, program, n30, n, false);
                                                    if (simpleMatch4 >= 0 && program[simpleMatch4] == 44) {
                                                        n7 = 0;
                                                        pc = n30;
                                                        break;
                                                    }
                                                }
                                                pushProgState(reGlobalData, 0, 0, reGlobalData.cp, reGlobalData.backTrackStackTop, n5, n6);
                                                pushBackTrackState(reGlobalData, (byte)44, n28);
                                                break Label_1780;
                                            }
                                            case 41: {
                                                n31 = pc + getIndex(program, pc);
                                                n32 = pc + 2;
                                                n30 = n32 + 1;
                                                b6 = program[n32];
                                                if (reopIsSimple(b6) && simpleMatch(reGlobalData, s, b6, program, n30, n, false) < 0) {
                                                    n7 = 0;
                                                    pc = n30;
                                                    break;
                                                }
                                                pushProgState(reGlobalData, 0, 0, reGlobalData.cp, reGlobalData.backTrackStackTop, n5, n6);
                                                pushBackTrackState(reGlobalData, (byte)43, n31);
                                                break Label_1780;
                                            }
                                        }
                                        break Label_1653;
                                    }
                                    b7 = b6;
                                    pc = n30;
                                    op = b7;
                                    continue;
                                }
                                case 32: {
                                    n33 = pc + getOffset(program, pc);
                                    pc = n33 + 1;
                                    op = program[n33];
                                    continue;
                                }
                                case 31: {
                                    n34 = pc + getOffset(program, pc);
                                    n35 = pc + 2;
                                    pc = n35 + 1;
                                    op = program[n35];
                                    cp2 = reGlobalData.cp;
                                    if (reopIsSimple(op)) {
                                        simpleMatch5 = simpleMatch(reGlobalData, s, op, program, pc, n, true);
                                        if (simpleMatch5 < 0) {
                                            op = program[n34];
                                            pc = n34 + 1;
                                            continue;
                                        }
                                        op = program[simpleMatch5];
                                        n7 = 1;
                                        pc = simpleMatch5 + 1;
                                    }
                                    pushBackTrackState(reGlobalData, program[n34], n34 + 1, cp2, n5, n6);
                                    continue;
                                }
                                case 30: {
                                    index7 = getIndex(program, pc);
                                    n36 = pc + 2;
                                    parensIndex = reGlobalData.parensIndex(index7);
                                    reGlobalData.setParens(index7, parensIndex, reGlobalData.cp - parensIndex);
                                    pc = n36 + 1;
                                    op = program[n36];
                                    continue;
                                }
                                case 29: {
                                    index8 = getIndex(program, pc);
                                    n37 = pc + 2;
                                    reGlobalData.setParens(index8, reGlobalData.cp, 0);
                                    op = program[n37];
                                    pc = n37 + 1;
                                    continue;
                                }
                                case 25:
                                case 26:
                                case 27:
                                case 28: {
                                    n38 = op;
                                    n39 = 0;
                                    n40 = 0;
                                    n41 = 0;
                                    n42 = 0;
                                    while (true) {
                                        Label_2179: {
                                            Label_2167: {
                                                Label_2147: {
                                                    Label_2127: {
                                                        switch (n38) {
                                                            default: {
                                                                switch (n38) {
                                                                    default: {
                                                                        throw Kit.codeBug();
                                                                    }
                                                                    case 47: {
                                                                        break Label_2127;
                                                                    }
                                                                    case 46: {
                                                                        break Label_2147;
                                                                    }
                                                                    case 45: {
                                                                        break Label_2167;
                                                                    }
                                                                    case 48: {
                                                                        break Label_2179;
                                                                    }
                                                                }
                                                                break;
                                                            }
                                                            case 28: {
                                                                n42 = 1;
                                                                break;
                                                            }
                                                            case 27: {
                                                                n39 = 1;
                                                                break Label_2147;
                                                            }
                                                            case 26: {
                                                                n40 = 1;
                                                                break Label_2167;
                                                            }
                                                            case 25: {
                                                                n41 = 1;
                                                                break Label_2179;
                                                            }
                                                        }
                                                    }
                                                    n43 = 0;
                                                    n44 = 1;
                                                    n40 = n42;
                                                    offset = n43;
                                                    break Label_2173;
                                                }
                                                offset = 1;
                                                n45 = -1;
                                                n40 = n39;
                                                n44 = n45;
                                                break Label_2173;
                                            }
                                            offset = 0;
                                            n44 = -1;
                                            pushProgState(reGlobalData, offset, n44, reGlobalData.cp, null, n5, n6);
                                            if (n40 != 0) {
                                                pushBackTrackState(reGlobalData, (byte)51, pc);
                                                n5 = 51;
                                                n6 = pc;
                                                n46 = pc + 6;
                                                pc = n46 + 1;
                                                op = program[n46];
                                            }
                                            else if (offset != 0) {
                                                n5 = 52;
                                                n6 = pc;
                                                n47 = pc + 6;
                                                pc = n47 + 1;
                                                op = program[n47];
                                            }
                                            else {
                                                pushBackTrackState(reGlobalData, (byte)52, pc);
                                                popProgState(reGlobalData);
                                                n48 = pc + 4;
                                                n49 = n48 + getOffset(program, n48);
                                                pc = n49 + 1;
                                                op = program[n49];
                                            }
                                            continue Label_2173_Outer;
                                        }
                                        offset = getOffset(program, pc);
                                        n50 = pc + 2;
                                        n44 = getOffset(program, n50) - 1;
                                        pc = n50 + 2;
                                        n40 = n41;
                                        continue;
                                    }
                                }
                            }
                        }
                        continue;
                    }
                    return true;
                }
            }
            if (n7 == 0) {
                backTrackStackTop = reGlobalData.backTrackStackTop;
                if (backTrackStackTop == null) {
                    return false;
                }
                reGlobalData.backTrackStackTop = backTrackStackTop.previous;
                reGlobalData.parens = backTrackStackTop.parens;
                reGlobalData.cp = backTrackStackTop.cp;
                reGlobalData.stateStackTop = backTrackStackTop.stateStackTop;
                n5 = backTrackStackTop.continuationOp;
                n6 = backTrackStackTop.continuationPc;
                pc = backTrackStackTop.pc;
                op = backTrackStackTop.op;
            }
            else {
                op = program[pc];
                ++pc;
            }
        }
    }
    
    private static boolean flatNIMatcher(final REGlobalData reGlobalData, final int n, final int n2, final String s, int i) {
        if (reGlobalData.cp + n2 > i) {
            return false;
        }
        final char[] source = reGlobalData.regexp.source;
        char c;
        char char1;
        for (i = 0; i < n2; ++i) {
            c = source[n + i];
            char1 = s.charAt(reGlobalData.cp + i);
            if (c != char1 && upcase(c) != upcase(char1)) {
                return false;
            }
        }
        reGlobalData.cp += n2;
        return true;
    }
    
    private static boolean flatNMatcher(final REGlobalData reGlobalData, final int n, final int n2, final String s, int i) {
        if (reGlobalData.cp + n2 > i) {
            return false;
        }
        for (i = 0; i < n2; ++i) {
            if (reGlobalData.regexp.source[n + i] != s.charAt(reGlobalData.cp + i)) {
                return false;
            }
        }
        reGlobalData.cp += n2;
        return true;
    }
    
    private static int getDecimalValue(final char c, final CompilerState compilerState, final int n, final String s) {
        int n2 = 0;
        final int cp = compilerState.cp;
        final char[] cpbegin = compilerState.cpbegin;
        int n3 = c - '0';
        while (compilerState.cp != compilerState.cpend) {
            final char c2 = cpbegin[compilerState.cp];
            if (!isDigit(c2)) {
                break;
            }
            int n4 = n2;
            int n5 = n3;
            if (n2 == 0) {
                n5 = n3 * 10 + (c2 - '0');
                if (n5 < n) {
                    n4 = n2;
                }
                else {
                    n4 = 1;
                    n5 = n;
                }
            }
            ++compilerState.cp;
            n2 = n4;
            n3 = n5;
        }
        if (n2 != 0) {
            reportError(s, String.valueOf(cpbegin, cp, compilerState.cp - cp));
        }
        return n3;
    }
    
    private static RegExpImpl getImpl(final Context context) {
        return (RegExpImpl)ScriptRuntime.getRegExpProxy(context);
    }
    
    private static int getIndex(final byte[] array, final int n) {
        return (array[n] & 0xFF) << 8 | (array[n + 1] & 0xFF);
    }
    
    private static int getOffset(final byte[] array, final int n) {
        return getIndex(array, n);
    }
    
    public static void init(final Context context, final Scriptable parentScope, final boolean b) {
        final NativeRegExp immunePrototypeProperty = new NativeRegExp();
        immunePrototypeProperty.re = compileRE(context, "", null, false);
        immunePrototypeProperty.activatePrototypeMap(6);
        immunePrototypeProperty.setParentScope(parentScope);
        immunePrototypeProperty.setPrototype(ScriptableObject.getObjectPrototype(parentScope));
        final NativeRegExpCtor nativeRegExpCtor = new NativeRegExpCtor();
        immunePrototypeProperty.defineProperty("constructor", nativeRegExpCtor, 2);
        ScriptRuntime.setFunctionProtoAndParent(nativeRegExpCtor, parentScope);
        nativeRegExpCtor.setImmunePrototypeProperty(immunePrototypeProperty);
        if (b) {
            immunePrototypeProperty.sealObject();
            nativeRegExpCtor.sealObject();
        }
        ScriptableObject.defineProperty(parentScope, "RegExp", nativeRegExpCtor, 2);
    }
    
    private static boolean isControlLetter(final char c) {
        return ('a' <= c && c <= 'z') || ('A' <= c && c <= 'Z');
    }
    
    static boolean isDigit(final char c) {
        return '0' <= c && c <= '9';
    }
    
    private static boolean isLineTerm(final char c) {
        return ScriptRuntime.isJSLineTerminator(c);
    }
    
    private static boolean isREWhiteSpace(final int n) {
        return ScriptRuntime.isJSWhitespaceOrLineTerminator(n);
    }
    
    private static boolean isWord(final char c) {
        return ('a' <= c && c <= 'z') || ('A' <= c && c <= 'Z') || isDigit(c) || c == '_';
    }
    
    private static boolean matchRegExp(final REGlobalData reGlobalData, final RECompiled regexp, final String s, final int n, final int skipped, final boolean b) {
        if (regexp.parenCount != 0) {
            reGlobalData.parens = new long[regexp.parenCount];
        }
        else {
            reGlobalData.parens = null;
        }
        reGlobalData.backTrackStackTop = null;
        reGlobalData.stateStackTop = null;
        reGlobalData.multiline = (b || (regexp.flags & 0x4) != 0x0);
        reGlobalData.regexp = regexp;
        final int anchorCh = reGlobalData.regexp.anchorCh;
        for (int i = n; i <= skipped; i = reGlobalData.skipped + n + 1) {
            int cp = i;
            Label_0170: {
                if (anchorCh >= 0) {
                    while (i != skipped) {
                        final char char1 = s.charAt(i);
                        cp = i;
                        if (char1 == anchorCh) {
                            break Label_0170;
                        }
                        if ((reGlobalData.regexp.flags & 0x2) != 0x0 && upcase(char1) == upcase((char)anchorCh)) {
                            cp = i;
                            break Label_0170;
                        }
                        ++i;
                    }
                    return false;
                }
            }
            reGlobalData.cp = cp;
            reGlobalData.skipped = cp - n;
            for (int j = 0; j < regexp.parenCount; ++j) {
                reGlobalData.parens[j] = -1L;
            }
            final boolean executeREBytecode = executeREBytecode(reGlobalData, s, skipped);
            reGlobalData.backTrackStackTop = null;
            reGlobalData.stateStackTop = null;
            if (executeREBytecode) {
                return true;
            }
            if (anchorCh == -2 && !reGlobalData.multiline) {
                reGlobalData.skipped = skipped;
                return false;
            }
        }
        return false;
    }
    
    private static boolean parseAlternative(final CompilerState compilerState) {
        RENode result = null;
        RENode reNode = null;
        final char[] cpbegin = compilerState.cpbegin;
        while (compilerState.cp != compilerState.cpend && cpbegin[compilerState.cp] != '|' && (compilerState.parenNesting == 0 || cpbegin[compilerState.cp] != ')')) {
            if (!parseTerm(compilerState)) {
                return false;
            }
            RENode reNode3;
            RENode reNode2;
            if (result == null) {
                reNode2 = (reNode3 = compilerState.result);
            }
            else {
                reNode.next = compilerState.result;
                reNode3 = reNode;
                reNode2 = result;
            }
            while (true) {
                result = reNode2;
                reNode = reNode3;
                if (reNode3.next == null) {
                    break;
                }
                reNode3 = reNode3.next;
            }
        }
        if (result == null) {
            compilerState.result = new RENode((byte)1);
            return true;
        }
        compilerState.result = result;
        return true;
    }
    
    private static boolean parseDisjunction(final CompilerState compilerState) {
        if (!parseAlternative(compilerState)) {
            return false;
        }
        final char[] cpbegin = compilerState.cpbegin;
        final int cp = compilerState.cp;
        if (cp != cpbegin.length && cpbegin[cp] == '|') {
            ++compilerState.cp;
            final RENode result = new RENode((byte)31);
            result.kid = compilerState.result;
            if (!parseDisjunction(compilerState)) {
                return false;
            }
            result.kid2 = compilerState.result;
            compilerState.result = result;
            if (result.kid.op == 14 && result.kid2.op == 14) {
                byte op;
                if ((compilerState.flags & 0x2) == 0x0) {
                    op = 53;
                }
                else {
                    op = 54;
                }
                result.op = op;
                result.chr = result.kid.chr;
                result.index = result.kid2.chr;
                compilerState.progLength += 13;
                return true;
            }
            if (result.kid.op == 22 && result.kid.index < 256 && result.kid2.op == 14 && (compilerState.flags & 0x2) == 0x0) {
                result.op = 55;
                result.chr = result.kid2.chr;
                result.index = result.kid.index;
                compilerState.progLength += 13;
                return true;
            }
            if (result.kid.op == 14 && result.kid2.op == 22 && result.kid2.index < 256 && (compilerState.flags & 0x2) == 0x0) {
                result.op = 55;
                result.chr = result.kid.chr;
                result.index = result.kid2.index;
                compilerState.progLength += 13;
                return true;
            }
            compilerState.progLength += 9;
        }
        return true;
    }
    
    private static boolean parseTerm(final CompilerState compilerState) {
        final char[] cpbegin = compilerState.cpbegin;
        final char chr = cpbegin[compilerState.cp++];
        int n = 2;
        final int parenCount = compilerState.parenCount;
        if (chr == '$') {
            compilerState.result = new RENode((byte)3);
            ++compilerState.progLength;
            return true;
        }
        Label_1674: {
            if (chr != '.') {
                Label_1631: {
                    if (chr != '?') {
                        if (chr != '^') {
                            Label_1593: {
                                switch (chr) {
                                    default: {
                                        switch (chr) {
                                            default: {
                                                compilerState.result = new RENode((byte)14);
                                                compilerState.result.chr = chr;
                                                compilerState.result.length = 1;
                                                compilerState.result.flatIndex = compilerState.cp - 1;
                                                compilerState.progLength += 3;
                                                break Label_1674;
                                            }
                                            case 92: {
                                                if (compilerState.cp >= compilerState.cpend) {
                                                    reportError("msg.trail.backslash", "");
                                                    return false;
                                                }
                                                final char chr2 = cpbegin[compilerState.cp++];
                                                if (chr2 == 'B') {
                                                    compilerState.result = new RENode((byte)5);
                                                    ++compilerState.progLength;
                                                    return true;
                                                }
                                                if (chr2 == 'D') {
                                                    compilerState.result = new RENode((byte)8);
                                                    ++compilerState.progLength;
                                                    break Label_1674;
                                                }
                                                if (chr2 == 'S') {
                                                    compilerState.result = new RENode((byte)12);
                                                    ++compilerState.progLength;
                                                    break Label_1674;
                                                }
                                                if (chr2 == 'W') {
                                                    compilerState.result = new RENode((byte)10);
                                                    ++compilerState.progLength;
                                                    break Label_1674;
                                                }
                                                if (chr2 == 'f') {
                                                    doFlat(compilerState, '\f');
                                                    break Label_1674;
                                                }
                                                if (chr2 == 'n') {
                                                    doFlat(compilerState, '\n');
                                                    break Label_1674;
                                                }
                                                switch (chr2) {
                                                    default: {
                                                        switch (chr2) {
                                                            default: {
                                                                switch (chr2) {
                                                                    default: {
                                                                        compilerState.result = new RENode((byte)14);
                                                                        compilerState.result.chr = chr2;
                                                                        compilerState.result.length = 1;
                                                                        compilerState.result.flatIndex = compilerState.cp - 1;
                                                                        compilerState.progLength += 3;
                                                                        break Label_1674;
                                                                    }
                                                                    case 119: {
                                                                        compilerState.result = new RENode((byte)9);
                                                                        ++compilerState.progLength;
                                                                        break Label_1674;
                                                                    }
                                                                    case 118: {
                                                                        doFlat(compilerState, '\u000b');
                                                                        break Label_1674;
                                                                    }
                                                                    case 117: {
                                                                        n = 2 + 2;
                                                                    }
                                                                    case 120: {
                                                                        int xDigitToInt = 0;
                                                                        int n2 = 0;
                                                                        int n3;
                                                                        while (true) {
                                                                            n3 = xDigitToInt;
                                                                            if (n2 >= n) {
                                                                                break;
                                                                            }
                                                                            n3 = xDigitToInt;
                                                                            if (compilerState.cp >= compilerState.cpend) {
                                                                                break;
                                                                            }
                                                                            xDigitToInt = Kit.xDigitToInt(cpbegin[compilerState.cp++], xDigitToInt);
                                                                            if (xDigitToInt < 0) {
                                                                                compilerState.cp -= n2 + 2;
                                                                                n3 = cpbegin[compilerState.cp++];
                                                                                break;
                                                                            }
                                                                            ++n2;
                                                                        }
                                                                        doFlat(compilerState, (char)n3);
                                                                        break Label_1674;
                                                                    }
                                                                    case 116: {
                                                                        doFlat(compilerState, '\t');
                                                                        break Label_1674;
                                                                    }
                                                                    case 115: {
                                                                        compilerState.result = new RENode((byte)11);
                                                                        ++compilerState.progLength;
                                                                        break Label_1674;
                                                                    }
                                                                    case 114: {
                                                                        doFlat(compilerState, '\r');
                                                                        break Label_1674;
                                                                    }
                                                                }
                                                                break;
                                                            }
                                                            case 100: {
                                                                compilerState.result = new RENode((byte)7);
                                                                ++compilerState.progLength;
                                                                break Label_1674;
                                                            }
                                                            case 99: {
                                                                char c;
                                                                if (compilerState.cp < compilerState.cpend && isControlLetter(cpbegin[compilerState.cp])) {
                                                                    c = (char)(cpbegin[compilerState.cp++] & '\u001f');
                                                                }
                                                                else {
                                                                    --compilerState.cp;
                                                                    c = '\\';
                                                                }
                                                                doFlat(compilerState, c);
                                                                break Label_1674;
                                                            }
                                                            case 98: {
                                                                compilerState.result = new RENode((byte)4);
                                                                ++compilerState.progLength;
                                                                return true;
                                                            }
                                                        }
                                                        break;
                                                    }
                                                    case 49:
                                                    case 50:
                                                    case 51:
                                                    case 52:
                                                    case 53:
                                                    case 54:
                                                    case 55:
                                                    case 56:
                                                    case 57: {
                                                        final int cp = compilerState.cp;
                                                        final int decimalValue = getDecimalValue(chr2, compilerState, 65535, "msg.overlarge.backref");
                                                        if (decimalValue > compilerState.backReferenceLimit) {
                                                            reportWarning(compilerState.cx, "msg.bad.backref", "");
                                                        }
                                                        if (decimalValue > compilerState.backReferenceLimit) {
                                                            compilerState.cp = cp - 1;
                                                            if (chr2 >= '8') {
                                                                doFlat(compilerState, '\\');
                                                            }
                                                            else {
                                                                ++compilerState.cp;
                                                                int n4;
                                                                char c2;
                                                                for (n4 = chr2 - '0'; n4 < 32 && compilerState.cp < compilerState.cpend; n4 = n4 * 8 + (c2 - '0')) {
                                                                    c2 = cpbegin[compilerState.cp];
                                                                    if (c2 < '0' || c2 > '7') {
                                                                        break;
                                                                    }
                                                                    ++compilerState.cp;
                                                                }
                                                                doFlat(compilerState, (char)n4);
                                                            }
                                                        }
                                                        else {
                                                            compilerState.result = new RENode((byte)13);
                                                            compilerState.result.parenIndex = decimalValue - 1;
                                                            compilerState.progLength += 3;
                                                            if (compilerState.maxBackReference < decimalValue) {
                                                                compilerState.maxBackReference = decimalValue;
                                                            }
                                                        }
                                                        break Label_1674;
                                                    }
                                                    case 48: {
                                                        reportWarning(compilerState.cx, "msg.bad.backref", "");
                                                        int n5;
                                                        char c3;
                                                        for (n5 = 0; n5 < 32 && compilerState.cp < compilerState.cpend; n5 = n5 * 8 + (c3 - '0')) {
                                                            c3 = cpbegin[compilerState.cp];
                                                            if (c3 < '0' || c3 > '7') {
                                                                break;
                                                            }
                                                            ++compilerState.cp;
                                                        }
                                                        doFlat(compilerState, (char)n5);
                                                        break Label_1674;
                                                    }
                                                }
                                                break;
                                            }
                                            case 91: {
                                                compilerState.result = new RENode((byte)22);
                                                final int cp2 = compilerState.cp;
                                                compilerState.result.startIndex = cp2;
                                                while (compilerState.cp != compilerState.cpend) {
                                                    if (cpbegin[compilerState.cp] == '\\') {
                                                        ++compilerState.cp;
                                                    }
                                                    else if (cpbegin[compilerState.cp] == ']') {
                                                        compilerState.result.kidlen = compilerState.cp - cp2;
                                                        compilerState.result.index = compilerState.classCount++;
                                                        if (!calculateBitmapSize(compilerState, compilerState.result, cpbegin, cp2, compilerState.cp++)) {
                                                            return false;
                                                        }
                                                        compilerState.progLength += 3;
                                                        break Label_1593;
                                                    }
                                                    ++compilerState.cp;
                                                }
                                                reportError("msg.unterm.class", "");
                                                return false;
                                            }
                                        }
                                        break;
                                    }
                                    case 41: {
                                        reportError("msg.re.unmatched.right.paren", "");
                                        return false;
                                    }
                                    case 40: {
                                        RENode result = null;
                                        final int cp3 = compilerState.cp;
                                        Label_1504: {
                                            if (compilerState.cp + 1 < compilerState.cpend && cpbegin[compilerState.cp] == '?') {
                                                final char c4 = cpbegin[compilerState.cp + 1];
                                                if (c4 == '=' || c4 == '!' || c4 == ':') {
                                                    compilerState.cp += 2;
                                                    if (c4 == '=') {
                                                        result = new RENode((byte)41);
                                                        compilerState.progLength += 4;
                                                        break Label_1504;
                                                    }
                                                    if (c4 == '!') {
                                                        result = new RENode((byte)42);
                                                        compilerState.progLength += 4;
                                                    }
                                                    break Label_1504;
                                                }
                                            }
                                            result = new RENode((byte)29);
                                            compilerState.progLength += 6;
                                            result.parenIndex = compilerState.parenCount++;
                                        }
                                        ++compilerState.parenNesting;
                                        if (!parseDisjunction(compilerState)) {
                                            return false;
                                        }
                                        if (compilerState.cp == compilerState.cpend || cpbegin[compilerState.cp] != ')') {
                                            reportError("msg.unterm.paren", "");
                                            return false;
                                        }
                                        ++compilerState.cp;
                                        --compilerState.parenNesting;
                                        if (result != null) {
                                            result.kid = compilerState.result;
                                            compilerState.result = result;
                                            break;
                                        }
                                        break;
                                    }
                                    case 42:
                                    case 43: {
                                        break Label_1631;
                                    }
                                }
                            }
                            break Label_1674;
                        }
                        compilerState.result = new RENode((byte)2);
                        ++compilerState.progLength;
                        return true;
                    }
                }
                reportError("msg.bad.quant", String.valueOf(cpbegin[compilerState.cp - 1]));
                return false;
            }
            compilerState.result = new RENode((byte)6);
            ++compilerState.progLength;
        }
        final RENode result2 = compilerState.result;
        if (compilerState.cp == compilerState.cpend) {
            return true;
        }
        int n6 = 0;
        final boolean b = false;
        final char c5 = cpbegin[compilerState.cp];
        if (c5 != '?') {
            if (c5 != '{') {
                switch (c5) {
                    case 43: {
                        compilerState.result = new RENode((byte)25);
                        compilerState.result.min = 1;
                        compilerState.result.max = -1;
                        compilerState.progLength += 8;
                        n6 = 1;
                        break;
                    }
                    case 42: {
                        compilerState.result = new RENode((byte)25);
                        compilerState.result.min = 0;
                        compilerState.result.max = -1;
                        compilerState.progLength += 8;
                        n6 = 1;
                        break;
                    }
                }
            }
            else {
                int max = -1;
                final int cp4 = compilerState.cp;
                final int cp5 = compilerState.cp + 1;
                compilerState.cp = cp5;
                boolean b2 = b;
                if (cp5 < cpbegin.length) {
                    final char c6 = cpbegin[compilerState.cp];
                    b2 = b;
                    if (isDigit(c6)) {
                        ++compilerState.cp;
                        final int decimalValue2 = getDecimalValue(c6, compilerState, 65535, "msg.overlarge.min");
                        char c7 = cpbegin[compilerState.cp];
                        if (c7 == ',') {
                            final int cp6 = compilerState.cp + 1;
                            compilerState.cp = cp6;
                            final char c8 = c7 = cpbegin[cp6];
                            if (isDigit(c8)) {
                                ++compilerState.cp;
                                final int decimalValue3 = getDecimalValue(c8, compilerState, 65535, "msg.overlarge.max");
                                c7 = cpbegin[compilerState.cp];
                                if (decimalValue2 > (max = decimalValue3)) {
                                    reportError("msg.max.lt.min", String.valueOf(cpbegin[compilerState.cp]));
                                    return false;
                                }
                            }
                        }
                        else {
                            max = decimalValue2;
                        }
                        b2 = b;
                        if (c7 == '}') {
                            compilerState.result = new RENode((byte)25);
                            compilerState.result.min = decimalValue2;
                            compilerState.result.max = max;
                            compilerState.progLength += 12;
                            b2 = true;
                        }
                    }
                }
                if ((n6 = (b2 ? 1 : 0)) == 0) {
                    compilerState.cp = cp4;
                    n6 = (b2 ? 1 : 0);
                }
            }
        }
        else {
            compilerState.result = new RENode((byte)25);
            compilerState.result.min = 0;
            compilerState.result.max = 1;
            compilerState.progLength += 8;
            n6 = 1;
        }
        if (n6 == 0) {
            return true;
        }
        ++compilerState.cp;
        compilerState.result.kid = result2;
        compilerState.result.parenIndex = parenCount;
        compilerState.result.parenCount = compilerState.parenCount - parenCount;
        if (compilerState.cp < compilerState.cpend && cpbegin[compilerState.cp] == '?') {
            ++compilerState.cp;
            compilerState.result.greedy = false;
            return true;
        }
        return compilerState.result.greedy = true;
    }
    
    private static REProgState popProgState(final REGlobalData reGlobalData) {
        final REProgState stateStackTop = reGlobalData.stateStackTop;
        reGlobalData.stateStackTop = stateStackTop.previous;
        return stateStackTop;
    }
    
    private static void processCharSet(final REGlobalData reGlobalData, final RECharSet set) {
        synchronized (set) {
            if (!set.converted) {
                processCharSetImpl(reGlobalData, set);
                set.converted = true;
            }
        }
    }
    
    private static void processCharSetImpl(final REGlobalData reGlobalData, final RECharSet set) {
        int i = set.startIndex;
        final int n = set.strlength + i;
        char c = '\0';
        set.bits = new byte[(set.length + 7) / 8];
        if (i == n) {
            return;
        }
        if (reGlobalData.regexp.source[i] == '^') {
            ++i;
        }
        int n2 = 0;
        while (i != n) {
            int n3 = 2;
            char c2 = '\0';
            int n17 = 0;
            Label_0796: {
                int n12 = 0;
                Label_0118: {
                    if (reGlobalData.regexp.source[i] == '\\') {
                        final int n4 = i + 1;
                        final char[] source = reGlobalData.regexp.source;
                        int n5 = n4 + 1;
                        c2 = source[n4];
                        Label_1079: {
                            if (c2 != 'D') {
                                if (c2 != 'S') {
                                    if (c2 != 'W') {
                                        int n6 = 0;
                                        Label_0304: {
                                            if (c2 != 'f') {
                                                if (c2 != 'n') {
                                                    switch (c2) {
                                                        default: {
                                                            switch (c2) {
                                                                default: {
                                                                    switch (c2) {
                                                                        default: {
                                                                            n6 = n5;
                                                                            break Label_0304;
                                                                        }
                                                                        case 119: {
                                                                            for (int j = set.length - 1; j >= 0; --j) {
                                                                                if (isWord((char)j)) {
                                                                                    addCharacterToCharSet(set, (char)j);
                                                                                }
                                                                            }
                                                                            break Label_1079;
                                                                        }
                                                                        case 118: {
                                                                            c2 = '\u000b';
                                                                            n6 = n5;
                                                                            break Label_0304;
                                                                        }
                                                                        case 117: {
                                                                            n3 = 2 + 2;
                                                                        }
                                                                        case 120: {
                                                                            int n7 = 0;
                                                                            int n8 = 0;
                                                                            int n9;
                                                                            int n10;
                                                                            while (true) {
                                                                                n9 = n7;
                                                                                n10 = n5;
                                                                                if (n8 >= n3) {
                                                                                    break;
                                                                                }
                                                                                n9 = n7;
                                                                                if ((n10 = n5) >= n) {
                                                                                    break;
                                                                                }
                                                                                final char[] source2 = reGlobalData.regexp.source;
                                                                                final int n11 = n5 + 1;
                                                                                final int asciiHexDigit = toASCIIHexDigit(source2[n5]);
                                                                                if (asciiHexDigit < 0) {
                                                                                    n10 = n11 - (n8 + 1);
                                                                                    n9 = 92;
                                                                                    break;
                                                                                }
                                                                                n7 = (n7 << 4 | asciiHexDigit);
                                                                                ++n8;
                                                                                n5 = n11;
                                                                            }
                                                                            c2 = (char)n9;
                                                                            n6 = n10;
                                                                            break Label_0304;
                                                                        }
                                                                        case 116: {
                                                                            c2 = '\t';
                                                                            n6 = n5;
                                                                            break Label_0304;
                                                                        }
                                                                        case 115: {
                                                                            for (int k = set.length - 1; k >= 0; --k) {
                                                                                if (isREWhiteSpace(k)) {
                                                                                    addCharacterToCharSet(set, (char)k);
                                                                                }
                                                                            }
                                                                            break Label_1079;
                                                                        }
                                                                        case 114: {
                                                                            c2 = '\r';
                                                                            n6 = n5;
                                                                            break Label_0304;
                                                                        }
                                                                    }
                                                                    break;
                                                                }
                                                                case 100: {
                                                                    addCharacterRangeToCharSet(set, '0', '9');
                                                                    break Label_1079;
                                                                }
                                                                case 99: {
                                                                    if (n5 < n && isControlLetter(reGlobalData.regexp.source[n5])) {
                                                                        final char[] source3 = reGlobalData.regexp.source;
                                                                        n12 = n5 + 1;
                                                                        c2 = (char)(source3[n5] & '\u001f');
                                                                        break Label_0118;
                                                                    }
                                                                    n6 = n5 - 1;
                                                                    c2 = '\\';
                                                                    break Label_0304;
                                                                }
                                                                case 98: {
                                                                    c2 = '\b';
                                                                    n6 = n5;
                                                                    break Label_0304;
                                                                }
                                                            }
                                                            break;
                                                        }
                                                        case 48:
                                                        case 49:
                                                        case 50:
                                                        case 51:
                                                        case 52:
                                                        case 53:
                                                        case 54:
                                                        case 55: {
                                                            final int n13 = c2 - '0';
                                                            final char c3 = reGlobalData.regexp.source[n5];
                                                            int n14 = n13;
                                                            n6 = n5;
                                                            if ('0' <= c3) {
                                                                n14 = n13;
                                                                n6 = n5;
                                                                if (c3 <= '7') {
                                                                    final int n15 = n5 + 1;
                                                                    final int n16 = n13 * 8 + (c3 - '0');
                                                                    final char c4 = reGlobalData.regexp.source[n15];
                                                                    n14 = n16;
                                                                    n6 = n15;
                                                                    if ('0' <= c4) {
                                                                        n14 = n16;
                                                                        n6 = n15;
                                                                        if (c4 <= '7') {
                                                                            n6 = n15 + 1;
                                                                            n14 = n16 * 8 + (c4 - '0');
                                                                            if (n14 > 255) {
                                                                                --n6;
                                                                                n14 = n16;
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                            c2 = (char)n14;
                                                            break;
                                                        }
                                                    }
                                                }
                                                else {
                                                    c2 = '\n';
                                                    n6 = n5;
                                                }
                                            }
                                            else {
                                                c2 = '\f';
                                                n6 = n5;
                                            }
                                        }
                                        n17 = n6;
                                        break Label_0796;
                                    }
                                    for (int l = set.length - 1; l >= 0; --l) {
                                        if (!isWord((char)l)) {
                                            addCharacterToCharSet(set, (char)l);
                                        }
                                    }
                                }
                                else {
                                    for (int n18 = set.length - 1; n18 >= 0; --n18) {
                                        if (!isREWhiteSpace(n18)) {
                                            addCharacterToCharSet(set, (char)n18);
                                        }
                                    }
                                }
                            }
                            else {
                                addCharacterRangeToCharSet(set, '\0', '/');
                                addCharacterRangeToCharSet(set, ':', (char)(set.length - 1));
                            }
                        }
                        i = n5;
                        continue;
                    }
                    final char[] source4 = reGlobalData.regexp.source;
                    final int n19 = i + 1;
                    c2 = source4[i];
                    n12 = n19;
                }
                n17 = n12;
            }
            if (n2 != 0) {
                if ((reGlobalData.regexp.flags & 0x2) != 0x0) {
                    char c5 = c;
                    while (c5 <= c2) {
                        addCharacterToCharSet(set, c5);
                        final char upcase = upcase(c5);
                        final char downcase = downcase(c5);
                        if (c5 != upcase) {
                            addCharacterToCharSet(set, upcase);
                        }
                        if (c5 != downcase) {
                            addCharacterToCharSet(set, downcase);
                        }
                        ++c5;
                        if (c5 == '\0') {
                            break;
                        }
                    }
                }
                else {
                    addCharacterRangeToCharSet(set, c, c2);
                }
                n2 = 0;
                i = n17;
            }
            else {
                if ((reGlobalData.regexp.flags & 0x2) != 0x0) {
                    addCharacterToCharSet(set, upcase(c2));
                    addCharacterToCharSet(set, downcase(c2));
                }
                else {
                    addCharacterToCharSet(set, c2);
                }
                i = n17;
                if (n17 >= n - 1) {
                    continue;
                }
                i = n17;
                if (reGlobalData.regexp.source[n17] != '-') {
                    continue;
                }
                i = n17 + 1;
                n2 = 1;
                c = c2;
            }
        }
    }
    
    private static void pushBackTrackState(final REGlobalData reGlobalData, final byte b, final int n) {
        final REProgState stateStackTop = reGlobalData.stateStackTop;
        reGlobalData.backTrackStackTop = new REBackTrackData(reGlobalData, b, n, reGlobalData.cp, stateStackTop.continuationOp, stateStackTop.continuationPc);
    }
    
    private static void pushBackTrackState(final REGlobalData reGlobalData, final byte b, final int n, final int n2, final int n3, final int n4) {
        reGlobalData.backTrackStackTop = new REBackTrackData(reGlobalData, b, n, n2, n3, n4);
    }
    
    private static void pushProgState(final REGlobalData reGlobalData, final int n, final int n2, final int n3, final REBackTrackData reBackTrackData, final int n4, final int n5) {
        reGlobalData.stateStackTop = new REProgState(reGlobalData.stateStackTop, n, n2, n3, reBackTrackData, n4, n5);
    }
    
    private static NativeRegExp realThis(final Scriptable scriptable, final IdFunctionObject idFunctionObject) {
        if (!(scriptable instanceof NativeRegExp)) {
            throw IdScriptableObject.incompatibleCallError(idFunctionObject);
        }
        return (NativeRegExp)scriptable;
    }
    
    private static boolean reopIsSimple(final int n) {
        return n >= 1 && n <= 23;
    }
    
    private static void reportError(final String s, final String s2) {
        throw ScriptRuntime.constructError("SyntaxError", ScriptRuntime.getMessage1(s, s2));
    }
    
    private static void reportWarning(final Context context, final String s, final String s2) {
        if (context.hasFeature(11)) {
            Context.reportWarning(ScriptRuntime.getMessage1(s, s2));
        }
    }
    
    private static void resolveForwardJump(final byte[] array, final int n, final int n2) {
        if (n > n2) {
            throw Kit.codeBug();
        }
        addIndex(array, n, n2 - n);
    }
    
    private static int simpleMatch(final REGlobalData reGlobalData, final String s, int index, final byte[] array, int index2, int n, final boolean b) {
        final boolean b2 = false;
        final boolean b3 = false;
        final boolean b4 = false;
        final boolean b5 = false;
        final boolean b6 = false;
        final boolean b7 = false;
        final int cp = reGlobalData.cp;
        final boolean b8 = true;
        final int n2 = 1;
        int n3 = 0;
        switch (index) {
            default: {
                throw Kit.codeBug();
            }
            case 22:
            case 23: {
                final int index3 = getIndex(array, index2);
                index = index2 + 2;
                n3 = (b7 ? 1 : 0);
                if (reGlobalData.cp != n) {
                    n3 = (b7 ? 1 : 0);
                    if (classMatcher(reGlobalData, reGlobalData.regexp.classList[index3], s.charAt(reGlobalData.cp))) {
                        ++reGlobalData.cp;
                        n3 = 1;
                    }
                }
                break;
            }
            case 19: {
                final char c = (char)getIndex(array, index2);
                final int n4 = index2 + 2;
                n3 = (b3 ? 1 : 0);
                index2 = c;
                index = n4;
                if (reGlobalData.cp != n) {
                    final char char1 = s.charAt(reGlobalData.cp);
                    Label_0293: {
                        if (c != char1) {
                            n3 = (b2 ? 1 : 0);
                            if (upcase(c) != upcase(char1)) {
                                break Label_0293;
                            }
                        }
                        n3 = 1;
                        ++reGlobalData.cp;
                    }
                    index2 = c;
                    index = n4;
                    break;
                }
                break;
            }
            case 18: {
                final char c2 = (char)getIndex(array, index2);
                final int n5 = index2 + 2;
                n3 = (b3 ? 1 : 0);
                index2 = c2;
                index = n5;
                if (reGlobalData.cp == n) {
                    break;
                }
                n3 = (b3 ? 1 : 0);
                index2 = c2;
                index = n5;
                if (s.charAt(reGlobalData.cp) == c2) {
                    n3 = 1;
                    ++reGlobalData.cp;
                    index = n5;
                    index2 = c2;
                    break;
                }
                break;
            }
            case 17: {
                final int n6 = index2 + 1;
                final char c3 = (char)(array[index2] & 0xFF);
                n3 = (b5 ? 1 : 0);
                index = n6;
                index2 = c3;
                if (reGlobalData.cp != n) {
                    final char char2 = s.charAt(reGlobalData.cp);
                    Label_0469: {
                        if (c3 != char2) {
                            n3 = (b4 ? 1 : 0);
                            if (upcase(c3) != upcase(char2)) {
                                break Label_0469;
                            }
                        }
                        n3 = 1;
                        ++reGlobalData.cp;
                    }
                    index = n6;
                    index2 = c3;
                    break;
                }
                break;
            }
            case 16: {
                index = getIndex(array, index2);
                index2 += 2;
                final int index4 = getIndex(array, index2);
                final int n7 = index2 + 2;
                n3 = (flatNIMatcher(reGlobalData, index, index4, s, n) ? 1 : 0);
                index2 = index;
                index2 = index4;
                index = n7;
                break;
            }
            case 15: {
                final int n8 = index2 + 1;
                final int n9 = (char)(array[index2] & 0xFF);
                n3 = (b5 ? 1 : 0);
                index = n8;
                index2 = n9;
                if (reGlobalData.cp == n) {
                    break;
                }
                n3 = (b5 ? 1 : 0);
                index = n8;
                if (s.charAt(reGlobalData.cp) == (index2 = n9)) {
                    n3 = 1;
                    ++reGlobalData.cp;
                    index2 = n9;
                    index = n8;
                    break;
                }
                break;
            }
            case 14: {
                final int index5 = getIndex(array, index2);
                index = index2 + 2;
                index2 = getIndex(array, index);
                index += 2;
                n3 = (flatNMatcher(reGlobalData, index5, index2, s, n) ? 1 : 0);
                index2 = index5;
                break;
            }
            case 13: {
                final int index6 = getIndex(array, index2);
                index = index2 + 2;
                n3 = (backrefMatcher(reGlobalData, index6, s, n) ? 1 : 0);
                break;
            }
            case 12: {
                n3 = (b6 ? 1 : 0);
                index = index2;
                if (reGlobalData.cp == n) {
                    break;
                }
                n3 = (b6 ? 1 : 0);
                index = index2;
                if (!isREWhiteSpace(s.charAt(reGlobalData.cp))) {
                    n3 = 1;
                    ++reGlobalData.cp;
                    index = index2;
                    break;
                }
                break;
            }
            case 11: {
                n3 = (b6 ? 1 : 0);
                index = index2;
                if (reGlobalData.cp == n) {
                    break;
                }
                n3 = (b6 ? 1 : 0);
                index = index2;
                if (isREWhiteSpace(s.charAt(reGlobalData.cp))) {
                    n3 = 1;
                    ++reGlobalData.cp;
                    index = index2;
                    break;
                }
                break;
            }
            case 10: {
                n3 = (b6 ? 1 : 0);
                index = index2;
                if (reGlobalData.cp == n) {
                    break;
                }
                n3 = (b6 ? 1 : 0);
                index = index2;
                if (!isWord(s.charAt(reGlobalData.cp))) {
                    n3 = 1;
                    ++reGlobalData.cp;
                    index = index2;
                    break;
                }
                break;
            }
            case 9: {
                n3 = (b6 ? 1 : 0);
                index = index2;
                if (reGlobalData.cp == n) {
                    break;
                }
                n3 = (b6 ? 1 : 0);
                index = index2;
                if (isWord(s.charAt(reGlobalData.cp))) {
                    n3 = 1;
                    ++reGlobalData.cp;
                    index = index2;
                    break;
                }
                break;
            }
            case 8: {
                n3 = (b6 ? 1 : 0);
                index = index2;
                if (reGlobalData.cp == n) {
                    break;
                }
                n3 = (b6 ? 1 : 0);
                index = index2;
                if (!isDigit(s.charAt(reGlobalData.cp))) {
                    n3 = 1;
                    ++reGlobalData.cp;
                    index = index2;
                    break;
                }
                break;
            }
            case 7: {
                n3 = (b6 ? 1 : 0);
                index = index2;
                if (reGlobalData.cp == n) {
                    break;
                }
                n3 = (b6 ? 1 : 0);
                index = index2;
                if (isDigit(s.charAt(reGlobalData.cp))) {
                    n3 = 1;
                    ++reGlobalData.cp;
                    index = index2;
                    break;
                }
                break;
            }
            case 6: {
                n3 = (b6 ? 1 : 0);
                index = index2;
                if (reGlobalData.cp == n) {
                    break;
                }
                n3 = (b6 ? 1 : 0);
                index = index2;
                if (!isLineTerm(s.charAt(reGlobalData.cp))) {
                    n3 = 1;
                    ++reGlobalData.cp;
                    index = index2;
                    break;
                }
                break;
            }
            case 5: {
                if (reGlobalData.cp != 0 && isWord(s.charAt(reGlobalData.cp - 1))) {
                    index = 0;
                }
                else {
                    index = 1;
                }
                if (reGlobalData.cp < n && isWord(s.charAt(reGlobalData.cp))) {
                    n = n2;
                }
                else {
                    n = 0;
                }
                n3 = (index ^ n);
                index = index2;
                break;
            }
            case 4: {
                if (reGlobalData.cp != 0 && isWord(s.charAt(reGlobalData.cp - 1))) {
                    index = 0;
                }
                else {
                    index = 1;
                }
                boolean b9 = b8;
                if (reGlobalData.cp < n) {
                    b9 = (!isWord(s.charAt(reGlobalData.cp)) && b8);
                }
                n3 = (index ^ (b9 ? 1 : 0));
                index = index2;
                break;
            }
            case 3: {
                if (reGlobalData.cp != n) {
                    n3 = (b6 ? 1 : 0);
                    index = index2;
                    if (!reGlobalData.multiline) {
                        break;
                    }
                    if (!isLineTerm(s.charAt(reGlobalData.cp))) {
                        n3 = (b6 ? 1 : 0);
                        index = index2;
                        break;
                    }
                }
                n3 = 1;
                index = index2;
                break;
            }
            case 2: {
                if (reGlobalData.cp != 0) {
                    n3 = (b6 ? 1 : 0);
                    index = index2;
                    if (!reGlobalData.multiline) {
                        break;
                    }
                    if (!isLineTerm(s.charAt(reGlobalData.cp - 1))) {
                        n3 = (b6 ? 1 : 0);
                        index = index2;
                        break;
                    }
                }
                n3 = 1;
                index = index2;
                break;
            }
            case 1: {
                n3 = 1;
                index = index2;
                break;
            }
        }
        if (n3 != 0) {
            if (!b) {
                reGlobalData.cp = cp;
            }
            return index;
        }
        reGlobalData.cp = cp;
        return -1;
    }
    
    private static int toASCIIHexDigit(int n) {
        if (n < 48) {
            return -1;
        }
        if (n <= 57) {
            return n - 48;
        }
        n |= 0x20;
        if (97 <= n && n <= 102) {
            return n - 97 + 10;
        }
        return -1;
    }
    
    private static char upcase(final char c) {
        if (c < '\u0080') {
            if ('a' <= c && c <= 'z') {
                return (char)(c - ' ');
            }
            return c;
        }
        else {
            final char upperCase = Character.toUpperCase(c);
            if (upperCase < '\u0080') {
                return c;
            }
            return upperCase;
        }
    }
    
    @Override
    public Object call(final Context context, final Scriptable scriptable, final Scriptable scriptable2, final Object[] array) {
        return this.execSub(context, scriptable, array, 1);
    }
    
    Scriptable compile(final Context context, final Scriptable scriptable, final Object[] array) {
        if (array.length <= 0 || !(array[0] instanceof NativeRegExp)) {
            String escapeRegExp;
            if (array.length != 0 && !(array[0] instanceof Undefined)) {
                escapeRegExp = escapeRegExp(array[0]);
            }
            else {
                escapeRegExp = "";
            }
            String string;
            if (array.length > 1 && array[1] != Undefined.instance) {
                string = ScriptRuntime.toString(array[1]);
            }
            else {
                string = null;
            }
            this.re = compileRE(context, escapeRegExp, string, false);
            this.lastIndex = 0.0;
            return this;
        }
        if (array.length > 1 && array[1] != Undefined.instance) {
            throw ScriptRuntime.typeError0("msg.bad.regexp.compile");
        }
        final NativeRegExp nativeRegExp = (NativeRegExp)array[0];
        this.re = nativeRegExp.re;
        this.lastIndex = nativeRegExp.lastIndex;
        return this;
    }
    
    @Override
    public Scriptable construct(final Context context, final Scriptable scriptable, final Object[] array) {
        return (Scriptable)this.execSub(context, scriptable, array, 1);
    }
    
    @Override
    public Object execIdCall(final IdFunctionObject idFunctionObject, final Context context, final Scriptable scriptable, final Scriptable scriptable2, final Object[] array) {
        if (!idFunctionObject.hasTag(NativeRegExp.REGEXP_TAG)) {
            return super.execIdCall(idFunctionObject, context, scriptable, scriptable2, array);
        }
        final int methodId = idFunctionObject.methodId();
        switch (methodId) {
            default: {
                throw new IllegalArgumentException(String.valueOf(methodId));
            }
            case 6: {
                return realThis(scriptable2, idFunctionObject).execSub(context, scriptable, array, 2);
            }
            case 5: {
                if (Boolean.TRUE.equals(realThis(scriptable2, idFunctionObject).execSub(context, scriptable, array, 0))) {
                    return Boolean.TRUE;
                }
                return Boolean.FALSE;
            }
            case 4: {
                return realThis(scriptable2, idFunctionObject).execSub(context, scriptable, array, 1);
            }
            case 2:
            case 3: {
                return realThis(scriptable2, idFunctionObject).toString();
            }
            case 1: {
                return realThis(scriptable2, idFunctionObject).compile(context, scriptable, array);
            }
        }
    }
    
    Object executeRegExp(final Context context, Scriptable scriptable, final RegExpImpl regExpImpl, final String str, final int[] array, final int n) {
        final REGlobalData reGlobalData = new REGlobalData();
        final int n2 = array[0];
        final int length = str.length();
        int index = n2;
        if (n2 > length) {
            index = length;
        }
        if (matchRegExp(reGlobalData, this.re, str, index, length, regExpImpl.multiline)) {
            final int cp = reGlobalData.cp;
            array[0] = cp;
            final int length2 = cp - (reGlobalData.skipped + index);
            final int index2 = cp - length2;
            Object o;
            if (n == 0) {
                o = Boolean.TRUE;
                scriptable = null;
            }
            else {
                o = context.newArray(scriptable, 0);
                scriptable = (Scriptable)o;
                scriptable.put(0, scriptable, str.substring(index2, index2 + length2));
            }
            if (this.re.parenCount == 0) {
                regExpImpl.parens = null;
                regExpImpl.lastParen = SubString.emptySubString;
            }
            else {
                regExpImpl.parens = new SubString[this.re.parenCount];
                SubString lastParen = null;
                SubString subString;
                for (int i = 0; i < this.re.parenCount; ++i, lastParen = subString) {
                    final int parensIndex = reGlobalData.parensIndex(i);
                    if (parensIndex != -1) {
                        subString = new SubString(str, parensIndex, reGlobalData.parensLength(i));
                        regExpImpl.parens[i] = subString;
                        if (n != 0) {
                            scriptable.put(i + 1, scriptable, subString.toString());
                        }
                    }
                    else {
                        subString = lastParen;
                        if (n != 0) {
                            scriptable.put(i + 1, scriptable, Undefined.instance);
                            subString = lastParen;
                        }
                    }
                }
                regExpImpl.lastParen = lastParen;
            }
            if (n != 0) {
                scriptable.put("index", scriptable, reGlobalData.skipped + index);
                scriptable.put("input", scriptable, str);
            }
            if (regExpImpl.lastMatch == null) {
                regExpImpl.lastMatch = new SubString();
                regExpImpl.leftContext = new SubString();
                regExpImpl.rightContext = new SubString();
            }
            regExpImpl.lastMatch.str = str;
            regExpImpl.lastMatch.index = index2;
            regExpImpl.lastMatch.length = length2;
            regExpImpl.leftContext.str = str;
            if (context.getLanguageVersion() == 120) {
                regExpImpl.leftContext.index = index;
                regExpImpl.leftContext.length = reGlobalData.skipped;
            }
            else {
                regExpImpl.leftContext.index = 0;
                regExpImpl.leftContext.length = reGlobalData.skipped + index;
            }
            regExpImpl.rightContext.str = str;
            regExpImpl.rightContext.index = cp;
            regExpImpl.rightContext.length = length - cp;
            return o;
        }
        if (n != 2) {
            return null;
        }
        return Undefined.instance;
    }
    
    @Override
    protected int findInstanceIdInfo(final String s) {
        int n = 0;
        String s2 = null;
        final int length = s.length();
        if (length == 6) {
            final char char1 = s.charAt(0);
            if (char1 == 'g') {
                s2 = "global";
                n = 3;
            }
            else if (char1 == 's') {
                s2 = "source";
                n = 2;
            }
        }
        else if (length == 9) {
            final char char2 = s.charAt(0);
            if (char2 == 'l') {
                s2 = "lastIndex";
                n = 1;
            }
            else if (char2 == 'm') {
                s2 = "multiline";
                n = 5;
            }
        }
        else if (length == 10) {
            s2 = "ignoreCase";
            n = 4;
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
        if (n2 == 0) {
            return super.findInstanceIdInfo(s);
        }
        int lastIndexAttr = 0;
        switch (n2) {
            default: {
                throw new IllegalStateException();
            }
            case 2:
            case 3:
            case 4:
            case 5: {
                lastIndexAttr = 7;
                break;
            }
            case 1: {
                lastIndexAttr = this.lastIndexAttr;
                break;
            }
        }
        return IdScriptableObject.instanceIdInfo(lastIndexAttr, n2);
    }
    
    @Override
    protected int findPrototypeId(final String s) {
        int n = 0;
        String s2 = null;
        final int length = s.length();
        if (length != 4) {
            switch (length) {
                case 8: {
                    final char char1 = s.charAt(3);
                    if (char1 == 'o') {
                        s2 = "toSource";
                        n = 3;
                        break;
                    }
                    if (char1 == 't') {
                        s2 = "toString";
                        n = 2;
                        break;
                    }
                    break;
                }
                case 7: {
                    s2 = "compile";
                    n = 1;
                    break;
                }
                case 6: {
                    s2 = "prefix";
                    n = 6;
                    break;
                }
            }
        }
        else {
            final char char2 = s.charAt(0);
            if (char2 == 'e') {
                s2 = "exec";
                n = 4;
            }
            else if (char2 == 't') {
                s2 = "test";
                n = 5;
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
    public String getClassName() {
        return "RegExp";
    }
    
    int getFlags() {
        return this.re.flags;
    }
    
    @Override
    protected String getInstanceIdName(final int n) {
        switch (n) {
            default: {
                return super.getInstanceIdName(n);
            }
            case 5: {
                return "multiline";
            }
            case 4: {
                return "ignoreCase";
            }
            case 3: {
                return "global";
            }
            case 2: {
                return "source";
            }
            case 1: {
                return "lastIndex";
            }
        }
    }
    
    @Override
    protected Object getInstanceIdValue(final int n) {
        final boolean b = false;
        final boolean b2 = false;
        boolean b3 = false;
        switch (n) {
            default: {
                return super.getInstanceIdValue(n);
            }
            case 5: {
                if ((this.re.flags & 0x4) != 0x0) {
                    b3 = true;
                }
                return ScriptRuntime.wrapBoolean(b3);
            }
            case 4: {
                boolean b4 = b;
                if ((this.re.flags & 0x2) != 0x0) {
                    b4 = true;
                }
                return ScriptRuntime.wrapBoolean(b4);
            }
            case 3: {
                boolean b5 = b2;
                if ((this.re.flags & 0x1) != 0x0) {
                    b5 = true;
                }
                return ScriptRuntime.wrapBoolean(b5);
            }
            case 2: {
                return new String(this.re.source);
            }
            case 1: {
                return this.lastIndex;
            }
        }
    }
    
    @Override
    protected int getMaxInstanceId() {
        return 5;
    }
    
    @Override
    public String getTypeOf() {
        return "object";
    }
    
    @Override
    protected void initPrototypeId(final int n) {
        int n2 = 0;
        String s = null;
        switch (n) {
            default: {
                throw new IllegalArgumentException(String.valueOf(n));
            }
            case 6: {
                n2 = 1;
                s = "prefix";
                break;
            }
            case 5: {
                n2 = 1;
                s = "test";
                break;
            }
            case 4: {
                n2 = 1;
                s = "exec";
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
                n2 = 2;
                s = "compile";
                break;
            }
        }
        this.initPrototypeMethod(NativeRegExp.REGEXP_TAG, n, s, n2);
    }
    
    @Override
    protected void setInstanceIdAttributes(final int n, final int lastIndexAttr) {
        if (n != 1) {
            super.setInstanceIdAttributes(n, lastIndexAttr);
            return;
        }
        this.lastIndexAttr = lastIndexAttr;
    }
    
    @Override
    protected void setInstanceIdValue(final int n, final Object lastIndex) {
        switch (n) {
            default: {
                super.setInstanceIdValue(n, lastIndex);
            }
            case 2:
            case 3:
            case 4:
            case 5: {}
            case 1: {
                this.lastIndex = lastIndex;
            }
        }
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append('/');
        if (this.re.source.length != 0) {
            sb.append(this.re.source);
        }
        else {
            sb.append("(?:)");
        }
        sb.append('/');
        if ((this.re.flags & 0x1) != 0x0) {
            sb.append('g');
        }
        if ((this.re.flags & 0x2) != 0x0) {
            sb.append('i');
        }
        if ((this.re.flags & 0x4) != 0x0) {
            sb.append('m');
        }
        return sb.toString();
    }
}
