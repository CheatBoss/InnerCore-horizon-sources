package org.mozilla.javascript;

import org.mozilla.javascript.typedarrays.*;

final class NativeMath extends IdScriptableObject
{
    private static final int Id_E = 30;
    private static final int Id_LN10 = 32;
    private static final int Id_LN2 = 33;
    private static final int Id_LOG10E = 35;
    private static final int Id_LOG2E = 34;
    private static final int Id_PI = 31;
    private static final int Id_SQRT1_2 = 36;
    private static final int Id_SQRT2 = 37;
    private static final int Id_abs = 2;
    private static final int Id_acos = 3;
    private static final int Id_asin = 4;
    private static final int Id_atan = 5;
    private static final int Id_atan2 = 6;
    private static final int Id_cbrt = 20;
    private static final int Id_ceil = 7;
    private static final int Id_cos = 8;
    private static final int Id_cosh = 21;
    private static final int Id_exp = 9;
    private static final int Id_expm1 = 22;
    private static final int Id_floor = 10;
    private static final int Id_hypot = 23;
    private static final int Id_imul = 28;
    private static final int Id_log = 11;
    private static final int Id_log10 = 25;
    private static final int Id_log1p = 24;
    private static final int Id_max = 12;
    private static final int Id_min = 13;
    private static final int Id_pow = 14;
    private static final int Id_random = 15;
    private static final int Id_round = 16;
    private static final int Id_sin = 17;
    private static final int Id_sinh = 26;
    private static final int Id_sqrt = 18;
    private static final int Id_tan = 19;
    private static final int Id_tanh = 27;
    private static final int Id_toSource = 1;
    private static final int Id_trunc = 29;
    private static final int LAST_METHOD_ID = 29;
    private static final Object MATH_TAG;
    private static final int MAX_ID = 37;
    static final long serialVersionUID = -8838847185801131569L;
    
    static {
        MATH_TAG = "Math";
    }
    
    private NativeMath() {
    }
    
    static void init(final Scriptable parentScope, final boolean b) {
        final NativeMath nativeMath = new NativeMath();
        nativeMath.activatePrototypeMap(37);
        nativeMath.setPrototype(ScriptableObject.getObjectPrototype(parentScope));
        nativeMath.setParentScope(parentScope);
        if (b) {
            nativeMath.sealObject();
        }
        ScriptableObject.defineProperty(parentScope, "Math", nativeMath, 2);
    }
    
    private double js_hypot(final Object[] array) {
        if (array == null) {
            return 0.0;
        }
        double n = 0.0;
        for (int length = array.length, i = 0; i < length; ++i) {
            final double number = ScriptRuntime.toNumber(array[i]);
            if (number == ScriptRuntime.NaN) {
                return number;
            }
            if (number == Double.POSITIVE_INFINITY) {
                return Double.POSITIVE_INFINITY;
            }
            if (number == Double.NEGATIVE_INFINITY) {
                return Double.POSITIVE_INFINITY;
            }
            n += number * number;
        }
        return Math.sqrt(n);
    }
    
    private Object js_imul(final Object[] array) {
        if (array != null && array.length >= 2) {
            long n = Conversions.toUint32(array[0]) * Conversions.toUint32(array[1]) % 4294967296L;
            if (n >= 2147483648L) {
                n -= 4294967296L;
            }
            return ScriptRuntime.toNumber(n);
        }
        return ScriptRuntime.wrapNumber(ScriptRuntime.NaN);
    }
    
    private double js_pow(double n, final double n2) {
        if (n2 != n2) {
            return n2;
        }
        final double n3 = 0.0;
        if (n2 == 0.0) {
            return 1.0;
        }
        double n4 = Double.POSITIVE_INFINITY;
        if (n != 0.0) {
            final double pow = Math.pow(n, n2);
            if (pow != pow) {
                if (n2 == Double.POSITIVE_INFINITY) {
                    if (n >= -1.0 && 1.0 >= n) {
                        if (-1.0 >= n || n >= 1.0) {
                            return pow;
                        }
                        n = 0.0;
                    }
                    else {
                        n = Double.POSITIVE_INFINITY;
                    }
                    return n;
                }
                if (n2 == Double.NEGATIVE_INFINITY) {
                    if (n < -1.0 || 1.0 < n) {
                        return 0.0;
                    }
                    if (-1.0 < n && n < 1.0) {
                        return Double.POSITIVE_INFINITY;
                    }
                }
                else {
                    if (n == Double.POSITIVE_INFINITY) {
                        if (n2 <= 0.0) {
                            n4 = 0.0;
                        }
                        return n4;
                    }
                    if (n == Double.NEGATIVE_INFINITY) {
                        final long n5 = (long)n2;
                        if (n5 == n2 && (n5 & 0x1L) != 0x0L) {
                            if (n2 > 0.0) {
                                n = Double.NEGATIVE_INFINITY;
                            }
                            else {
                                n = 0.0;
                            }
                            return n;
                        }
                        if (n2 <= 0.0) {
                            n4 = 0.0;
                        }
                        return n4;
                    }
                }
            }
            return pow;
        }
        if (1.0 / n > 0.0) {
            if (n2 > 0.0) {
                n4 = 0.0;
            }
            return n4;
        }
        final long n6 = (long)n2;
        if (n6 == n2 && (n6 & 0x1L) != 0x0L) {
            if (n2 > 0.0) {
                n = 0.0;
            }
            else {
                n = Double.NEGATIVE_INFINITY;
            }
        }
        else if (n2 > 0.0) {
            n = n3;
        }
        else {
            n = Double.POSITIVE_INFINITY;
        }
        return n;
    }
    
    private double js_trunc(final double n) {
        if (n < 0.0) {
            return Math.ceil(n);
        }
        return Math.floor(n);
    }
    
    @Override
    public Object execIdCall(final IdFunctionObject idFunctionObject, final Context context, final Scriptable scriptable, final Scriptable scriptable2, final Object[] array) {
        if (!idFunctionObject.hasTag(NativeMath.MATH_TAG)) {
            return super.execIdCall(idFunctionObject, context, scriptable, scriptable2, array);
        }
        final int methodId = idFunctionObject.methodId();
        double n = Double.NaN;
        final double n2 = Double.POSITIVE_INFINITY;
        final double n3 = 0.0;
        int i = 0;
        switch (methodId) {
            default: {
                throw new IllegalStateException(String.valueOf(methodId));
            }
            case 29: {
                n = this.js_trunc(ScriptRuntime.toNumber(array, 0));
                break;
            }
            case 28: {
                return this.js_imul(array);
            }
            case 27: {
                n = Math.tanh(ScriptRuntime.toNumber(array, 0));
                break;
            }
            case 26: {
                n = Math.sinh(ScriptRuntime.toNumber(array, 0));
                break;
            }
            case 25: {
                n = Math.log10(ScriptRuntime.toNumber(array, 0));
                break;
            }
            case 24: {
                n = Math.log1p(ScriptRuntime.toNumber(array, 0));
                break;
            }
            case 23: {
                n = this.js_hypot(array);
                break;
            }
            case 22: {
                n = Math.expm1(ScriptRuntime.toNumber(array, 0));
                break;
            }
            case 21: {
                n = Math.cosh(ScriptRuntime.toNumber(array, 0));
                break;
            }
            case 20: {
                n = Math.cbrt(ScriptRuntime.toNumber(array, 0));
                break;
            }
            case 19: {
                n = Math.tan(ScriptRuntime.toNumber(array, 0));
                break;
            }
            case 18: {
                n = Math.sqrt(ScriptRuntime.toNumber(array, 0));
                break;
            }
            case 17: {
                final double number = ScriptRuntime.toNumber(array, 0);
                double sin = n;
                if (number != Double.POSITIVE_INFINITY) {
                    if (number == Double.NEGATIVE_INFINITY) {
                        sin = n;
                    }
                    else {
                        sin = Math.sin(number);
                    }
                }
                n = sin;
                break;
            }
            case 16: {
                final double n4 = n = ScriptRuntime.toNumber(array, 0);
                if (n4 != n4) {
                    break;
                }
                n = n4;
                if (n4 == Double.POSITIVE_INFINITY) {
                    break;
                }
                n = n4;
                if (n4 != Double.NEGATIVE_INFINITY) {
                    final long round = Math.round(n4);
                    if (round != 0L) {
                        n = (double)round;
                    }
                    else if (n4 < 0.0) {
                        n = ScriptRuntime.negativeZero;
                    }
                    else {
                        n = n4;
                        if (n4 != 0.0) {
                            n = 0.0;
                        }
                    }
                    break;
                }
                break;
            }
            case 15: {
                n = Math.random();
                break;
            }
            case 14: {
                n = this.js_pow(ScriptRuntime.toNumber(array, 0), ScriptRuntime.toNumber(array, 1));
                break;
            }
            case 12:
            case 13: {
                n = n2;
                if (methodId == 12) {
                    n = Double.NEGATIVE_INFINITY;
                }
                while (i != array.length) {
                    final double number2 = ScriptRuntime.toNumber(array[i]);
                    if (number2 != number2) {
                        n = number2;
                        break;
                    }
                    if (methodId == 12) {
                        n = Math.max(n, number2);
                    }
                    else {
                        n = Math.min(n, number2);
                    }
                    ++i;
                }
                break;
            }
            case 11: {
                final double number3 = ScriptRuntime.toNumber(array, 0);
                if (number3 >= 0.0) {
                    n = Math.log(number3);
                }
                break;
            }
            case 10: {
                n = Math.floor(ScriptRuntime.toNumber(array, 0));
                break;
            }
            case 9: {
                n = ScriptRuntime.toNumber(array, 0);
                if (n != Double.POSITIVE_INFINITY) {
                    if (n == Double.NEGATIVE_INFINITY) {
                        n = n3;
                    }
                    else {
                        n = Math.exp(n);
                    }
                }
                break;
            }
            case 8: {
                final double number4 = ScriptRuntime.toNumber(array, 0);
                double cos = n;
                if (number4 != Double.POSITIVE_INFINITY) {
                    if (number4 == Double.NEGATIVE_INFINITY) {
                        cos = n;
                    }
                    else {
                        cos = Math.cos(number4);
                    }
                }
                n = cos;
                break;
            }
            case 7: {
                n = Math.ceil(ScriptRuntime.toNumber(array, 0));
                break;
            }
            case 6: {
                n = Math.atan2(ScriptRuntime.toNumber(array, 0), ScriptRuntime.toNumber(array, 1));
                break;
            }
            case 5: {
                n = Math.atan(ScriptRuntime.toNumber(array, 0));
                break;
            }
            case 3:
            case 4: {
                final double number5 = ScriptRuntime.toNumber(array, 0);
                if (number5 == number5 && -1.0 <= number5 && number5 <= 1.0) {
                    if (methodId == 3) {
                        n = Math.acos(number5);
                    }
                    else {
                        n = Math.asin(number5);
                    }
                    break;
                }
                n = Double.NaN;
                break;
            }
            case 2: {
                n = ScriptRuntime.toNumber(array, 0);
                if (n == 0.0) {
                    n = n3;
                    break;
                }
                if (n < 0.0) {
                    n = -n;
                    break;
                }
                break;
            }
            case 1: {
                return "Math";
            }
        }
        return ScriptRuntime.wrapNumber(n);
    }
    
    @Override
    protected int findPrototypeId(final String s) {
        final boolean b = false;
        final String s2 = null;
        int n = 0;
        String s3 = null;
        Label_1071: {
            switch (s.length()) {
                default: {
                    n = (b ? 1 : 0);
                    s3 = s2;
                    break;
                }
                case 8: {
                    s3 = "toSource";
                    n = 1;
                    break;
                }
                case 7: {
                    s3 = "SQRT1_2";
                    n = 36;
                    break;
                }
                case 6: {
                    final char char1 = s.charAt(0);
                    if (char1 == 'L') {
                        s3 = "LOG10E";
                        n = 35;
                        break;
                    }
                    n = (b ? 1 : 0);
                    s3 = s2;
                    if (char1 == 'r') {
                        s3 = "random";
                        n = 15;
                        break;
                    }
                    break;
                }
                case 5: {
                    final char char2 = s.charAt(0);
                    if (char2 == 'L') {
                        s3 = "LOG2E";
                        n = 34;
                        break;
                    }
                    if (char2 == 'S') {
                        s3 = "SQRT2";
                        n = 37;
                        break;
                    }
                    if (char2 == 'a') {
                        s3 = "atan2";
                        n = 6;
                        break;
                    }
                    if (char2 == 'h') {
                        s3 = "hypot";
                        n = 23;
                        break;
                    }
                    if (char2 != 'l') {
                        if (char2 == 'r') {
                            s3 = "round";
                            n = 16;
                            break;
                        }
                        if (char2 == 't') {
                            s3 = "trunc";
                            n = 29;
                            break;
                        }
                        switch (char2) {
                            default: {
                                n = (b ? 1 : 0);
                                s3 = s2;
                                break Label_1071;
                            }
                            case 102: {
                                s3 = "floor";
                                n = 10;
                                break Label_1071;
                            }
                            case 101: {
                                s3 = "expm1";
                                n = 22;
                                break Label_1071;
                            }
                        }
                    }
                    else {
                        final char char3 = s.charAt(4);
                        if (char3 == '0') {
                            s3 = "log10";
                            n = 25;
                            break;
                        }
                        n = (b ? 1 : 0);
                        s3 = s2;
                        if (char3 == 'p') {
                            s3 = "log1p";
                            n = 24;
                            break;
                        }
                        break;
                    }
                    break;
                }
                case 4: {
                    final char char4 = s.charAt(1);
                    if (char4 == 'N') {
                        s3 = "LN10";
                        n = 32;
                        break;
                    }
                    if (char4 == 'e') {
                        s3 = "ceil";
                        n = 7;
                        break;
                    }
                    if (char4 == 'i') {
                        s3 = "sinh";
                        n = 26;
                        break;
                    }
                    if (char4 == 'm') {
                        s3 = "imul";
                        n = 28;
                        break;
                    }
                    if (char4 == 'o') {
                        s3 = "cosh";
                        n = 21;
                        break;
                    }
                    if (char4 == 'q') {
                        s3 = "sqrt";
                        n = 18;
                        break;
                    }
                    switch (char4) {
                        default: {
                            switch (char4) {
                                default: {
                                    n = (b ? 1 : 0);
                                    s3 = s2;
                                    break Label_1071;
                                }
                                case 116: {
                                    s3 = "atan";
                                    n = 5;
                                    break Label_1071;
                                }
                                case 115: {
                                    s3 = "asin";
                                    n = 4;
                                    break Label_1071;
                                }
                            }
                            break;
                        }
                        case 99: {
                            s3 = "acos";
                            n = 3;
                            break Label_1071;
                        }
                        case 98: {
                            s3 = "cbrt";
                            n = 20;
                            break Label_1071;
                        }
                        case 97: {
                            s3 = "tanh";
                            n = 27;
                            break Label_1071;
                        }
                    }
                    break;
                }
                case 3: {
                    switch (s.charAt(0)) {
                        default: {
                            n = (b ? 1 : 0);
                            s3 = s2;
                            break Label_1071;
                        }
                        case 't': {
                            n = (b ? 1 : 0);
                            s3 = s2;
                            if (s.charAt(2) != 'n') {
                                break Label_1071;
                            }
                            n = (b ? 1 : 0);
                            s3 = s2;
                            if (s.charAt(1) == 'a') {
                                return 19;
                            }
                            break Label_1071;
                        }
                        case 's': {
                            n = (b ? 1 : 0);
                            s3 = s2;
                            if (s.charAt(2) != 'n') {
                                break Label_1071;
                            }
                            n = (b ? 1 : 0);
                            s3 = s2;
                            if (s.charAt(1) == 'i') {
                                return 17;
                            }
                            break Label_1071;
                        }
                        case 'p': {
                            n = (b ? 1 : 0);
                            s3 = s2;
                            if (s.charAt(2) != 'w') {
                                break Label_1071;
                            }
                            n = (b ? 1 : 0);
                            s3 = s2;
                            if (s.charAt(1) == 'o') {
                                return 14;
                            }
                            break Label_1071;
                        }
                        case 'm': {
                            final char char5 = s.charAt(2);
                            if (char5 == 'n') {
                                n = (b ? 1 : 0);
                                s3 = s2;
                                if (s.charAt(1) == 'i') {
                                    return 13;
                                }
                                break Label_1071;
                            }
                            else {
                                n = (b ? 1 : 0);
                                s3 = s2;
                                if (char5 != 'x') {
                                    break Label_1071;
                                }
                                n = (b ? 1 : 0);
                                s3 = s2;
                                if (s.charAt(1) == 'a') {
                                    return 12;
                                }
                                break Label_1071;
                            }
                            break;
                        }
                        case 'l': {
                            n = (b ? 1 : 0);
                            s3 = s2;
                            if (s.charAt(2) != 'g') {
                                break Label_1071;
                            }
                            n = (b ? 1 : 0);
                            s3 = s2;
                            if (s.charAt(1) == 'o') {
                                return 11;
                            }
                            break Label_1071;
                        }
                        case 'e': {
                            n = (b ? 1 : 0);
                            s3 = s2;
                            if (s.charAt(2) != 'p') {
                                break Label_1071;
                            }
                            n = (b ? 1 : 0);
                            s3 = s2;
                            if (s.charAt(1) == 'x') {
                                return 9;
                            }
                            break Label_1071;
                        }
                        case 'c': {
                            n = (b ? 1 : 0);
                            s3 = s2;
                            if (s.charAt(2) != 's') {
                                break Label_1071;
                            }
                            n = (b ? 1 : 0);
                            s3 = s2;
                            if (s.charAt(1) == 'o') {
                                return 8;
                            }
                            break Label_1071;
                        }
                        case 'a': {
                            n = (b ? 1 : 0);
                            s3 = s2;
                            if (s.charAt(2) != 's') {
                                break Label_1071;
                            }
                            n = (b ? 1 : 0);
                            s3 = s2;
                            if (s.charAt(1) == 'b') {
                                return 2;
                            }
                            break Label_1071;
                        }
                        case 'L': {
                            n = (b ? 1 : 0);
                            s3 = s2;
                            if (s.charAt(2) != '2') {
                                break Label_1071;
                            }
                            n = (b ? 1 : 0);
                            s3 = s2;
                            if (s.charAt(1) == 'N') {
                                return 33;
                            }
                            break Label_1071;
                        }
                    }
                    break;
                }
                case 2: {
                    n = (b ? 1 : 0);
                    s3 = s2;
                    if (s.charAt(0) != 'P') {
                        break;
                    }
                    n = (b ? 1 : 0);
                    s3 = s2;
                    if (s.charAt(1) == 'I') {
                        return 31;
                    }
                    break;
                }
                case 1: {
                    n = (b ? 1 : 0);
                    s3 = s2;
                    if (s.charAt(0) == 'E') {
                        return 30;
                    }
                    break;
                }
            }
        }
        int n2 = n;
        if (s3 != null) {
            n2 = n;
            if (s3 != s) {
                n2 = n;
                if (!s3.equals(s)) {
                    n2 = 0;
                }
            }
        }
        return n2;
    }
    
    @Override
    public String getClassName() {
        return "Math";
    }
    
    @Override
    protected void initPrototypeId(final int n) {
        if (n <= 29) {
            int n2 = 0;
            String s = null;
            switch (n) {
                default: {
                    throw new IllegalStateException(String.valueOf(n));
                }
                case 29: {
                    n2 = 1;
                    s = "trunc";
                    break;
                }
                case 28: {
                    n2 = 2;
                    s = "imul";
                    break;
                }
                case 27: {
                    n2 = 1;
                    s = "tanh";
                    break;
                }
                case 26: {
                    n2 = 1;
                    s = "sinh";
                    break;
                }
                case 25: {
                    n2 = 1;
                    s = "log10";
                    break;
                }
                case 24: {
                    n2 = 1;
                    s = "log1p";
                    break;
                }
                case 23: {
                    n2 = 2;
                    s = "hypot";
                    break;
                }
                case 22: {
                    n2 = 1;
                    s = "expm1";
                    break;
                }
                case 21: {
                    n2 = 1;
                    s = "cosh";
                    break;
                }
                case 20: {
                    n2 = 1;
                    s = "cbrt";
                    break;
                }
                case 19: {
                    n2 = 1;
                    s = "tan";
                    break;
                }
                case 18: {
                    n2 = 1;
                    s = "sqrt";
                    break;
                }
                case 17: {
                    n2 = 1;
                    s = "sin";
                    break;
                }
                case 16: {
                    n2 = 1;
                    s = "round";
                    break;
                }
                case 15: {
                    n2 = 0;
                    s = "random";
                    break;
                }
                case 14: {
                    n2 = 2;
                    s = "pow";
                    break;
                }
                case 13: {
                    n2 = 2;
                    s = "min";
                    break;
                }
                case 12: {
                    n2 = 2;
                    s = "max";
                    break;
                }
                case 11: {
                    n2 = 1;
                    s = "log";
                    break;
                }
                case 10: {
                    n2 = 1;
                    s = "floor";
                    break;
                }
                case 9: {
                    n2 = 1;
                    s = "exp";
                    break;
                }
                case 8: {
                    n2 = 1;
                    s = "cos";
                    break;
                }
                case 7: {
                    n2 = 1;
                    s = "ceil";
                    break;
                }
                case 6: {
                    n2 = 2;
                    s = "atan2";
                    break;
                }
                case 5: {
                    n2 = 1;
                    s = "atan";
                    break;
                }
                case 4: {
                    n2 = 1;
                    s = "asin";
                    break;
                }
                case 3: {
                    n2 = 1;
                    s = "acos";
                    break;
                }
                case 2: {
                    n2 = 1;
                    s = "abs";
                    break;
                }
                case 1: {
                    n2 = 0;
                    s = "toSource";
                    break;
                }
            }
            this.initPrototypeMethod(NativeMath.MATH_TAG, n, s, n2);
            return;
        }
        double n3 = 0.0;
        String s2 = null;
        switch (n) {
            default: {
                throw new IllegalStateException(String.valueOf(n));
            }
            case 37: {
                n3 = 1.4142135623730951;
                s2 = "SQRT2";
                break;
            }
            case 36: {
                n3 = 0.7071067811865476;
                s2 = "SQRT1_2";
                break;
            }
            case 35: {
                n3 = 0.4342944819032518;
                s2 = "LOG10E";
                break;
            }
            case 34: {
                n3 = 1.4426950408889634;
                s2 = "LOG2E";
                break;
            }
            case 33: {
                n3 = 0.6931471805599453;
                s2 = "LN2";
                break;
            }
            case 32: {
                n3 = 2.302585092994046;
                s2 = "LN10";
                break;
            }
            case 31: {
                n3 = 3.141592653589793;
                s2 = "PI";
                break;
            }
            case 30: {
                n3 = 2.718281828459045;
                s2 = "E";
                break;
            }
        }
        this.initPrototypeValue(n, s2, ScriptRuntime.wrapNumber(n3), 7);
    }
}
