package org.mozilla.javascript;

public class Decompiler
{
    public static final int CASE_GAP_PROP = 3;
    private static final int FUNCTION_END = 165;
    public static final int INDENT_GAP_PROP = 2;
    public static final int INITIAL_INDENT_PROP = 1;
    public static final int ONLY_BODY_FLAG = 1;
    public static final int TO_SOURCE_FLAG = 2;
    private static final boolean printSource = false;
    private char[] sourceBuffer;
    private int sourceTop;
    
    public Decompiler() {
        this.sourceBuffer = new char[128];
    }
    
    private void append(final char c) {
        if (this.sourceTop == this.sourceBuffer.length) {
            this.increaseSourceCapacity(this.sourceTop + 1);
        }
        this.sourceBuffer[this.sourceTop] = c;
        ++this.sourceTop;
    }
    
    private void appendString(final String s) {
        final int length = s.length();
        int n = 1;
        if (length >= 32768) {
            n = 2;
        }
        final int sourceTop = this.sourceTop + n + length;
        if (sourceTop > this.sourceBuffer.length) {
            this.increaseSourceCapacity(sourceTop);
        }
        if (length >= 32768) {
            this.sourceBuffer[this.sourceTop] = (char)(0x8000 | length >>> 16);
            ++this.sourceTop;
        }
        this.sourceBuffer[this.sourceTop] = (char)length;
        ++this.sourceTop;
        s.getChars(0, length, this.sourceBuffer, this.sourceTop);
        this.sourceTop = sourceTop;
    }
    
    public static String decompile(final String s, int i, final UintMap uintMap) {
        final int length = s.length();
        if (length == 0) {
            return "";
        }
        final int int1 = uintMap.getInt(1, 0);
        if (int1 < 0) {
            throw new IllegalArgumentException();
        }
        final int int2 = uintMap.getInt(2, 4);
        if (int2 < 0) {
            throw new IllegalArgumentException();
        }
        final int int3 = uintMap.getInt(3, 2);
        if (int3 < 0) {
            throw new IllegalArgumentException();
        }
        final StringBuilder sb = new StringBuilder();
        final boolean b = (i & 0x1) != 0x0;
        final boolean b2 = (i & 0x2) != 0x0;
        final int n = 0;
        final boolean b3 = false;
        int n2 = 0;
        int char1;
        if (s.charAt(0) == '\u0088') {
            n2 = 0 + 1;
            char1 = -1;
        }
        else {
            char1 = s.charAt(0 + 1);
        }
        int n4;
        int n5;
        int j;
        if (!b2) {
            sb.append('\n');
            int n3 = 0;
            while (true) {
                i = int1;
                n4 = n;
                n5 = (b3 ? 1 : 0);
                j = n2;
                if (n3 >= int1) {
                    break;
                }
                sb.append(' ');
                ++n3;
            }
        }
        else {
            i = int1;
            n4 = n;
            n5 = (b3 ? 1 : 0);
            j = n2;
            if (char1 == 2) {
                sb.append('(');
                j = n2;
                n5 = (b3 ? 1 : 0);
                n4 = n;
                i = int1;
            }
        }
    Label_2699_Outer:
        while (j < length) {
            final char char2 = s.charAt(j);
            while (true) {
                Label_2943: {
                    switch (char2) {
                        default: {
                            Label_1814: {
                                switch (char2) {
                                    default: {
                                        switch (char2) {
                                            default: {
                                                int n7 = 0;
                                                int n12 = 0;
                                                int n13 = 0;
                                                int n14 = 0;
                                                Label_1800: {
                                                    int n6 = 0;
                                                    int n8 = 0;
                                                    Label_1788: {
                                                        switch (char2) {
                                                            default: {
                                                                switch (char2) {
                                                                    default: {
                                                                        switch (char2) {
                                                                            default: {
                                                                                switch (char2) {
                                                                                    default: {
                                                                                        Label_1263: {
                                                                                            switch (char2) {
                                                                                                default: {
                                                                                                    switch (char2) {
                                                                                                        default: {
                                                                                                            final StringBuilder sb2 = new StringBuilder();
                                                                                                            sb2.append("Token: ");
                                                                                                            sb2.append(Token.name(s.charAt(j)));
                                                                                                            throw new RuntimeException(sb2.toString());
                                                                                                        }
                                                                                                        case 165: {
                                                                                                            n6 = i;
                                                                                                            n7 = n5;
                                                                                                            n8 = j;
                                                                                                            break Label_1788;
                                                                                                        }
                                                                                                        case 160: {
                                                                                                            sb.append("debugger;\n");
                                                                                                            n6 = i;
                                                                                                            n7 = n5;
                                                                                                            n8 = j;
                                                                                                            break Label_1788;
                                                                                                        }
                                                                                                        case 72: {
                                                                                                            sb.append("yield ");
                                                                                                            n6 = i;
                                                                                                            n7 = n5;
                                                                                                            n8 = j;
                                                                                                            break Label_1788;
                                                                                                        }
                                                                                                        case 66: {
                                                                                                            sb.append(": ");
                                                                                                            n6 = i;
                                                                                                            n7 = n5;
                                                                                                            n8 = j;
                                                                                                            break Label_1788;
                                                                                                        }
                                                                                                        case 50: {
                                                                                                            sb.append("throw ");
                                                                                                            n6 = i;
                                                                                                            n7 = n5;
                                                                                                            n8 = j;
                                                                                                            break Label_1788;
                                                                                                        }
                                                                                                        case 4: {
                                                                                                            sb.append("return");
                                                                                                            n6 = i;
                                                                                                            n7 = n5;
                                                                                                            n8 = j;
                                                                                                            if (82 != getNext(s, length, j)) {
                                                                                                                sb.append(' ');
                                                                                                                n6 = i;
                                                                                                                n7 = n5;
                                                                                                                n8 = j;
                                                                                                                break Label_1788;
                                                                                                            }
                                                                                                            break Label_1788;
                                                                                                        }
                                                                                                        case 1: {
                                                                                                            if (b2) {
                                                                                                                n6 = i;
                                                                                                                n7 = n5;
                                                                                                                n8 = j;
                                                                                                                break Label_1788;
                                                                                                            }
                                                                                                            boolean b4 = true;
                                                                                                            int n9 = i;
                                                                                                            int n10;
                                                                                                            if ((n10 = n5) == 0) {
                                                                                                                final boolean b5 = true;
                                                                                                                b4 = b4;
                                                                                                                n9 = i;
                                                                                                                n10 = (b5 ? 1 : 0);
                                                                                                                if (b) {
                                                                                                                    sb.setLength(0);
                                                                                                                    n9 = i - int2;
                                                                                                                    b4 = false;
                                                                                                                    n10 = (b5 ? 1 : 0);
                                                                                                                }
                                                                                                            }
                                                                                                            if (b4) {
                                                                                                                sb.append('\n');
                                                                                                            }
                                                                                                            n6 = n9;
                                                                                                            n7 = n10;
                                                                                                            n8 = j;
                                                                                                            if (j + 1 < length) {
                                                                                                                final int n11 = 0;
                                                                                                                final char char3 = s.charAt(j + 1);
                                                                                                                if (char3 != 's' && char3 != 't') {
                                                                                                                    if (char3 == 'V') {
                                                                                                                        i = int2;
                                                                                                                    }
                                                                                                                    else {
                                                                                                                        i = n11;
                                                                                                                        if (char3 == '\'') {
                                                                                                                            i = n11;
                                                                                                                            if (s.charAt(getSourceStringEnd(s, j + 2)) == 'g') {
                                                                                                                                i = int2;
                                                                                                                            }
                                                                                                                        }
                                                                                                                    }
                                                                                                                }
                                                                                                                else {
                                                                                                                    i = int2 - int3;
                                                                                                                }
                                                                                                                while (i < n9) {
                                                                                                                    sb.append(' ');
                                                                                                                    ++i;
                                                                                                                }
                                                                                                                n6 = n9;
                                                                                                                n7 = n10;
                                                                                                                n8 = j;
                                                                                                                break Label_1788;
                                                                                                            }
                                                                                                            break Label_1788;
                                                                                                        }
                                                                                                        case 163: {
                                                                                                            break Label_1263;
                                                                                                        }
                                                                                                    }
                                                                                                    break;
                                                                                                }
                                                                                                case 154: {
                                                                                                    sb.append("const ");
                                                                                                    n6 = i;
                                                                                                    n7 = n5;
                                                                                                    n8 = j;
                                                                                                    break Label_1788;
                                                                                                }
                                                                                                case 153: {
                                                                                                    sb.append("let ");
                                                                                                    n6 = i;
                                                                                                    n7 = n5;
                                                                                                    n8 = j;
                                                                                                    break Label_1788;
                                                                                                }
                                                                                                case 151:
                                                                                                case 152: {
                                                                                                    if (s.charAt(j) == '\u0097') {
                                                                                                        sb.append("get ");
                                                                                                    }
                                                                                                    else if (s.charAt(j) == '\u0098') {
                                                                                                        sb.append("set ");
                                                                                                    }
                                                                                                    n8 = printSourceString(s, j + 1 + 1, false, sb) + 1;
                                                                                                    n6 = i;
                                                                                                    n7 = n5;
                                                                                                    break Label_1788;
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                        break;
                                                                                    }
                                                                                    case 147: {
                                                                                        sb.append('@');
                                                                                        n6 = i;
                                                                                        n7 = n5;
                                                                                        n8 = j;
                                                                                        break Label_1788;
                                                                                    }
                                                                                    case 146: {
                                                                                        sb.append(".(");
                                                                                        n6 = i;
                                                                                        n7 = n5;
                                                                                        n8 = j;
                                                                                        break Label_1788;
                                                                                    }
                                                                                }
                                                                                break;
                                                                            }
                                                                            case 144: {
                                                                                sb.append("::");
                                                                                n6 = i;
                                                                                n7 = n5;
                                                                                n8 = j;
                                                                                break Label_1788;
                                                                            }
                                                                            case 143: {
                                                                                sb.append("..");
                                                                                n6 = i;
                                                                                n7 = n5;
                                                                                n8 = j;
                                                                                break Label_1788;
                                                                            }
                                                                        }
                                                                        break;
                                                                    }
                                                                    case 126: {
                                                                        sb.append("void ");
                                                                        n6 = i;
                                                                        n7 = n5;
                                                                        n8 = j;
                                                                        break Label_1788;
                                                                    }
                                                                    case 125: {
                                                                        sb.append("finally ");
                                                                        n6 = i;
                                                                        n7 = n5;
                                                                        n8 = j;
                                                                        break Label_1788;
                                                                    }
                                                                    case 124: {
                                                                        sb.append("catch ");
                                                                        n6 = i;
                                                                        n7 = n5;
                                                                        n8 = j;
                                                                        break Label_1788;
                                                                    }
                                                                    case 123: {
                                                                        sb.append("with ");
                                                                        n6 = i;
                                                                        n7 = n5;
                                                                        n8 = j;
                                                                        break Label_1788;
                                                                    }
                                                                    case 122: {
                                                                        sb.append("var ");
                                                                        n6 = i;
                                                                        n7 = n5;
                                                                        n8 = j;
                                                                        break Label_1788;
                                                                    }
                                                                    case 121: {
                                                                        sb.append("continue");
                                                                        n6 = i;
                                                                        n7 = n5;
                                                                        n8 = j;
                                                                        if (39 == getNext(s, length, j)) {
                                                                            sb.append(' ');
                                                                            n6 = i;
                                                                            n7 = n5;
                                                                            n8 = j;
                                                                            break Label_1788;
                                                                        }
                                                                        break Label_1788;
                                                                    }
                                                                    case 120: {
                                                                        sb.append("break");
                                                                        n6 = i;
                                                                        n7 = n5;
                                                                        n8 = j;
                                                                        if (39 == getNext(s, length, j)) {
                                                                            sb.append(' ');
                                                                            n6 = i;
                                                                            n7 = n5;
                                                                            n8 = j;
                                                                            break Label_1788;
                                                                        }
                                                                        break Label_1788;
                                                                    }
                                                                    case 119: {
                                                                        sb.append("for ");
                                                                        n6 = i;
                                                                        n7 = n5;
                                                                        n8 = j;
                                                                        break Label_1788;
                                                                    }
                                                                    case 118: {
                                                                        sb.append("do ");
                                                                        n6 = i;
                                                                        n7 = n5;
                                                                        n8 = j;
                                                                        break Label_1788;
                                                                    }
                                                                    case 117: {
                                                                        sb.append("while ");
                                                                        n6 = i;
                                                                        n7 = n5;
                                                                        n8 = j;
                                                                        break Label_1788;
                                                                    }
                                                                    case 116: {
                                                                        sb.append("default");
                                                                        n6 = i;
                                                                        n7 = n5;
                                                                        n8 = j;
                                                                        break Label_1788;
                                                                    }
                                                                    case 115: {
                                                                        sb.append("case ");
                                                                        n6 = i;
                                                                        n7 = n5;
                                                                        n8 = j;
                                                                        break Label_1788;
                                                                    }
                                                                    case 114: {
                                                                        sb.append("switch ");
                                                                        n6 = i;
                                                                        n7 = n5;
                                                                        n8 = j;
                                                                        break Label_1788;
                                                                    }
                                                                    case 113: {
                                                                        sb.append("else ");
                                                                        n6 = i;
                                                                        n7 = n5;
                                                                        n8 = j;
                                                                        break Label_1788;
                                                                    }
                                                                    case 112: {
                                                                        sb.append("if ");
                                                                        n6 = i;
                                                                        n7 = n5;
                                                                        n8 = j;
                                                                        break Label_1788;
                                                                    }
                                                                }
                                                                break;
                                                            }
                                                            case 109: {
                                                                n8 = j + 1;
                                                                sb.append("function ");
                                                                n7 = n5;
                                                                n6 = i;
                                                                break;
                                                            }
                                                            case 108: {
                                                                sb.append('.');
                                                                n6 = i;
                                                                n7 = n5;
                                                                n8 = j;
                                                                break;
                                                            }
                                                            case 107: {
                                                                sb.append("--");
                                                                n6 = i;
                                                                n7 = n5;
                                                                n8 = j;
                                                                break;
                                                            }
                                                            case 106: {
                                                                sb.append("++");
                                                                n6 = i;
                                                                n7 = n5;
                                                                n8 = j;
                                                                break;
                                                            }
                                                            case 105: {
                                                                sb.append(" && ");
                                                                n6 = i;
                                                                n7 = n5;
                                                                n8 = j;
                                                                break;
                                                            }
                                                            case 104: {
                                                                sb.append(" || ");
                                                                n6 = i;
                                                                n7 = n5;
                                                                n8 = j;
                                                                break;
                                                            }
                                                            case 103: {
                                                                if (1 == getNext(s, length, j)) {
                                                                    sb.append(':');
                                                                    n6 = i;
                                                                    n7 = n5;
                                                                    n8 = j;
                                                                    break;
                                                                }
                                                                sb.append(" : ");
                                                                n6 = i;
                                                                n7 = n5;
                                                                n8 = j;
                                                                break;
                                                            }
                                                            case 102: {
                                                                sb.append(" ? ");
                                                                n6 = i;
                                                                n7 = n5;
                                                                n8 = j;
                                                                break;
                                                            }
                                                            case 101: {
                                                                sb.append(" %= ");
                                                                n6 = i;
                                                                n7 = n5;
                                                                n8 = j;
                                                                break;
                                                            }
                                                            case 100: {
                                                                sb.append(" /= ");
                                                                n6 = i;
                                                                n7 = n5;
                                                                n8 = j;
                                                                break;
                                                            }
                                                            case 99: {
                                                                sb.append(" *= ");
                                                                n6 = i;
                                                                n7 = n5;
                                                                n8 = j;
                                                                break;
                                                            }
                                                            case 98: {
                                                                sb.append(" -= ");
                                                                n6 = i;
                                                                n7 = n5;
                                                                n8 = j;
                                                                break;
                                                            }
                                                            case 97: {
                                                                sb.append(" += ");
                                                                n6 = i;
                                                                n7 = n5;
                                                                n8 = j;
                                                                break;
                                                            }
                                                            case 96: {
                                                                sb.append(" >>>= ");
                                                                n6 = i;
                                                                n7 = n5;
                                                                n8 = j;
                                                                break;
                                                            }
                                                            case 95: {
                                                                sb.append(" >>= ");
                                                                n6 = i;
                                                                n7 = n5;
                                                                n8 = j;
                                                                break;
                                                            }
                                                            case 94: {
                                                                sb.append(" <<= ");
                                                                n6 = i;
                                                                n7 = n5;
                                                                n8 = j;
                                                                break;
                                                            }
                                                            case 93: {
                                                                sb.append(" &= ");
                                                                n6 = i;
                                                                n7 = n5;
                                                                n8 = j;
                                                                break;
                                                            }
                                                            case 92: {
                                                                sb.append(" ^= ");
                                                                n6 = i;
                                                                n7 = n5;
                                                                n8 = j;
                                                                break;
                                                            }
                                                            case 91: {
                                                                sb.append(" |= ");
                                                                n6 = i;
                                                                n7 = n5;
                                                                n8 = j;
                                                                break;
                                                            }
                                                            case 90: {
                                                                sb.append(" = ");
                                                                n6 = i;
                                                                n7 = n5;
                                                                n8 = j;
                                                                break;
                                                            }
                                                            case 89: {
                                                                sb.append(", ");
                                                                n6 = i;
                                                                n7 = n5;
                                                                n8 = j;
                                                                break;
                                                            }
                                                            case 88: {
                                                                sb.append(')');
                                                                n6 = i;
                                                                n7 = n5;
                                                                n8 = j;
                                                                if (85 == getNext(s, length, j)) {
                                                                    sb.append(' ');
                                                                    n6 = i;
                                                                    n7 = n5;
                                                                    n8 = j;
                                                                    break;
                                                                }
                                                                break;
                                                            }
                                                            case 87: {
                                                                sb.append('(');
                                                                n12 = i;
                                                                n13 = n4;
                                                                n7 = n5;
                                                                n14 = j;
                                                                break Label_1800;
                                                            }
                                                            case 86: {
                                                                n13 = n4 - 1;
                                                                if (b && n13 == 0) {
                                                                    n12 = i;
                                                                    n7 = n5;
                                                                    n14 = j;
                                                                    break Label_1800;
                                                                }
                                                                sb.append('}');
                                                                final int next = getNext(s, length, j);
                                                                Label_2420: {
                                                                    if (next != 1) {
                                                                        if (next == 113 || next == 117) {
                                                                            i -= int2;
                                                                            sb.append(' ');
                                                                            break Label_2420;
                                                                        }
                                                                        if (next != 165) {
                                                                            break Label_2420;
                                                                        }
                                                                    }
                                                                    i -= int2;
                                                                }
                                                                n12 = i;
                                                                n7 = n5;
                                                                n14 = j;
                                                                break Label_1800;
                                                            }
                                                            case 85: {
                                                                n13 = n4 + 1;
                                                                n12 = i;
                                                                if (1 == getNext(s, length, j)) {
                                                                    n12 = i + int2;
                                                                }
                                                                sb.append('{');
                                                                n7 = n5;
                                                                n14 = j;
                                                                break Label_1800;
                                                            }
                                                            case 84: {
                                                                sb.append(']');
                                                                n12 = i;
                                                                n13 = n4;
                                                                n7 = n5;
                                                                n14 = j;
                                                                break Label_1800;
                                                            }
                                                            case 83: {
                                                                sb.append('[');
                                                                n12 = i;
                                                                n13 = n4;
                                                                n7 = n5;
                                                                n14 = j;
                                                                break Label_1800;
                                                            }
                                                            case 82: {
                                                                sb.append(';');
                                                                n12 = i;
                                                                n13 = n4;
                                                                n7 = n5;
                                                                n14 = j;
                                                                if (1 != getNext(s, length, j)) {
                                                                    sb.append(' ');
                                                                    break Label_1814;
                                                                }
                                                                break Label_1800;
                                                            }
                                                            case 81: {
                                                                sb.append("try ");
                                                                break Label_1814;
                                                            }
                                                        }
                                                    }
                                                    n14 = n8;
                                                    n13 = n4;
                                                    n12 = n6;
                                                }
                                                j = n14;
                                                n5 = n7;
                                                n4 = n13;
                                                i = n12;
                                                break Label_1814;
                                            }
                                            case 53: {
                                                sb.append(" instanceof ");
                                                break Label_1814;
                                            }
                                            case 52: {
                                                sb.append(" in ");
                                                break Label_1814;
                                            }
                                        }
                                        break;
                                    }
                                    case 47: {
                                        sb.append(" !== ");
                                        break;
                                    }
                                    case 46: {
                                        sb.append(" === ");
                                        break;
                                    }
                                    case 45: {
                                        sb.append("true");
                                        break;
                                    }
                                    case 44: {
                                        sb.append("false");
                                        break;
                                    }
                                    case 43: {
                                        sb.append("this");
                                        break;
                                    }
                                    case 42: {
                                        sb.append("null");
                                        break;
                                    }
                                    case 41: {
                                        j = printSourceString(s, j + 1, true, sb);
                                        continue Label_2699_Outer;
                                    }
                                    case 40: {
                                        j = printSourceNumber(s, j + 1, sb);
                                        continue Label_2699_Outer;
                                    }
                                    case 39:
                                    case 48: {
                                        j = printSourceString(s, j + 1, false, sb);
                                        continue Label_2699_Outer;
                                    }
                                }
                            }
                            break Label_2943;
                        }
                        case 32: {
                            sb.append("typeof ");
                            break Label_2943;
                        }
                        case 31: {
                            sb.append("delete ");
                            break Label_2943;
                        }
                        case 30: {
                            sb.append("new ");
                            break Label_2943;
                        }
                        case 29: {
                            sb.append('-');
                            break Label_2943;
                        }
                        case 28: {
                            sb.append('+');
                            break Label_2943;
                        }
                        case 27: {
                            sb.append('~');
                            break Label_2943;
                        }
                        case 26: {
                            sb.append('!');
                            break Label_2943;
                        }
                        case 25: {
                            sb.append(" % ");
                            break Label_2943;
                        }
                        case 24: {
                            sb.append(" / ");
                            break Label_2943;
                        }
                        case 23: {
                            sb.append(" * ");
                            break Label_2943;
                        }
                        case 22: {
                            sb.append(" - ");
                            break Label_2943;
                        }
                        case 21: {
                            sb.append(" + ");
                            break Label_2943;
                        }
                        case 20: {
                            sb.append(" >>> ");
                            break Label_2943;
                        }
                        case 19: {
                            sb.append(" >> ");
                            break Label_2943;
                        }
                        case 18: {
                            sb.append(" << ");
                            break Label_2943;
                        }
                        case 17: {
                            sb.append(" >= ");
                            break Label_2943;
                        }
                        case 16: {
                            sb.append(" > ");
                            break Label_2943;
                        }
                        case 15: {
                            sb.append(" <= ");
                            break Label_2943;
                        }
                        case 14: {
                            sb.append(" < ");
                            break Label_2943;
                        }
                        case 13: {
                            sb.append(" != ");
                            break Label_2943;
                        }
                        case 12: {
                            sb.append(" == ");
                            break Label_2943;
                        }
                        case 11: {
                            sb.append(" & ");
                            break Label_2943;
                        }
                        case 10: {
                            sb.append(" ^ ");
                            break Label_2943;
                        }
                        case 9: {
                            sb.append(" | ");
                            break Label_2943;
                        }
                    }
                    continue Label_2699_Outer;
                }
                ++j;
                continue;
            }
        }
        if (!b2) {
            if (!b) {
                sb.append('\n');
            }
        }
        else if (char1 == 2) {
            sb.append(')');
        }
        return sb.toString();
    }
    
    private static int getNext(final String s, final int n, final int n2) {
        if (n2 + 1 < n) {
            return s.charAt(n2 + 1);
        }
        return 0;
    }
    
    private static int getSourceStringEnd(final String s, final int n) {
        return printSourceString(s, n, false, null);
    }
    
    private void increaseSourceCapacity(final int n) {
        if (n <= this.sourceBuffer.length) {
            Kit.codeBug();
        }
        int n2;
        if ((n2 = this.sourceBuffer.length * 2) < n) {
            n2 = n;
        }
        final char[] sourceBuffer = new char[n2];
        System.arraycopy(this.sourceBuffer, 0, sourceBuffer, 0, this.sourceTop);
        this.sourceBuffer = sourceBuffer;
    }
    
    private static int printSourceNumber(final String s, int n, final StringBuilder sb) {
        double longBitsToDouble = 0.0;
        final char char1 = s.charAt(n);
        ++n;
        if (char1 == 'S') {
            if (sb != null) {
                longBitsToDouble = s.charAt(n);
            }
            ++n;
        }
        else {
            if (char1 != 'J' && char1 != 'D') {
                throw new RuntimeException();
            }
            if (sb != null) {
                final long n2 = (long)s.charAt(n) << 48 | (long)s.charAt(n + 1) << 32 | (long)s.charAt(n + 2) << 16 | (long)s.charAt(n + 3);
                if (char1 == 'J') {
                    longBitsToDouble = (double)n2;
                }
                else {
                    longBitsToDouble = Double.longBitsToDouble(n2);
                }
            }
            n += 4;
        }
        if (sb != null) {
            sb.append(ScriptRuntime.numberToString(longBitsToDouble, 10));
        }
        return n;
    }
    
    private static int printSourceString(String substring, int n, final boolean b, final StringBuilder sb) {
        final char char1 = substring.charAt(n);
        final int n2 = n + 1;
        int n3 = char1;
        n = n2;
        if (('\u8000' & char1) != 0x0) {
            n3 = ((char1 & '\u7fff') << 16 | substring.charAt(n2));
            n = n2 + 1;
        }
        if (sb != null) {
            substring = substring.substring(n, n + n3);
            if (!b) {
                sb.append(substring);
            }
            else {
                sb.append('\"');
                sb.append(ScriptRuntime.escapeString(substring));
                sb.append('\"');
            }
        }
        return n + n3;
    }
    
    private String sourceToString(final int n) {
        if (n < 0 || this.sourceTop < n) {
            Kit.codeBug();
        }
        return new String(this.sourceBuffer, n, this.sourceTop - n);
    }
    
    void addEOL(final int n) {
        if (n >= 0 && n <= 164) {
            this.append((char)n);
            this.append('\u0001');
            return;
        }
        throw new IllegalArgumentException();
    }
    
    void addName(final String s) {
        this.addToken(39);
        this.appendString(s);
    }
    
    void addNumber(final double n) {
        this.addToken(40);
        final long n2 = (long)n;
        if (n2 != n) {
            final long doubleToLongBits = Double.doubleToLongBits(n);
            this.append('D');
            this.append((char)(doubleToLongBits >> 48));
            this.append((char)(doubleToLongBits >> 32));
            this.append((char)(doubleToLongBits >> 16));
            this.append((char)doubleToLongBits);
            return;
        }
        if (n2 < 0L) {
            Kit.codeBug();
        }
        if (n2 <= 65535L) {
            this.append('S');
            this.append((char)n2);
            return;
        }
        this.append('J');
        this.append((char)(n2 >> 48));
        this.append((char)(n2 >> 32));
        this.append((char)(n2 >> 16));
        this.append((char)n2);
    }
    
    void addRegexp(final String s, final String s2) {
        this.addToken(48);
        final StringBuilder sb = new StringBuilder();
        sb.append('/');
        sb.append(s);
        sb.append('/');
        sb.append(s2);
        this.appendString(sb.toString());
    }
    
    void addString(final String s) {
        this.addToken(41);
        this.appendString(s);
    }
    
    void addToken(final int n) {
        if (n >= 0 && n <= 164) {
            this.append((char)n);
            return;
        }
        throw new IllegalArgumentException();
    }
    
    int getCurrentOffset() {
        return this.sourceTop;
    }
    
    String getEncodedSource() {
        return this.sourceToString(0);
    }
    
    int markFunctionEnd(int currentOffset) {
        currentOffset = this.getCurrentOffset();
        this.append('¥');
        return currentOffset;
    }
    
    int markFunctionStart(final int n) {
        final int currentOffset = this.getCurrentOffset();
        this.addToken(109);
        this.append((char)n);
        return currentOffset;
    }
}
