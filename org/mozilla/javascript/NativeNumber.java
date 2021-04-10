package org.mozilla.javascript;

final class NativeNumber extends IdScriptableObject
{
    private static final int ConstructorId_isFinite = -1;
    private static final int ConstructorId_isInteger = -3;
    private static final int ConstructorId_isNaN = -2;
    private static final int ConstructorId_isSafeInteger = -4;
    private static final int ConstructorId_parseFloat = -5;
    private static final int ConstructorId_parseInt = -6;
    private static final int Id_constructor = 1;
    private static final int Id_toExponential = 7;
    private static final int Id_toFixed = 6;
    private static final int Id_toLocaleString = 3;
    private static final int Id_toPrecision = 8;
    private static final int Id_toSource = 4;
    private static final int Id_toString = 2;
    private static final int Id_valueOf = 5;
    private static final int MAX_PRECISION = 100;
    private static final int MAX_PROTOTYPE_ID = 8;
    private static final double MAX_SAFE_INTEGER;
    private static final double MIN_SAFE_INTEGER;
    private static final Object NUMBER_TAG;
    static final long serialVersionUID = 3504516769741512101L;
    private double doubleValue;
    
    static {
        NUMBER_TAG = "Number";
        MAX_SAFE_INTEGER = Math.pow(2.0, 53.0) - 1.0;
        MIN_SAFE_INTEGER = -NativeNumber.MAX_SAFE_INTEGER;
    }
    
    NativeNumber(final double doubleValue) {
        this.doubleValue = doubleValue;
    }
    
    private Double doubleVal(final Number n) {
        if (n instanceof Double) {
            return (Double)n;
        }
        return n.doubleValue();
    }
    
    private Object execConstructorCall(final int n, final Object[] array) {
        switch (n) {
            default: {
                throw new IllegalArgumentException(String.valueOf(n));
            }
            case -1: {
                if (array.length == 0 || Undefined.instance == array[0]) {
                    return false;
                }
                if (array[0] instanceof Number) {
                    return isFinite(array[0]);
                }
                return false;
            }
            case -2: {
                if (array.length == 0 || Undefined.instance == array[0]) {
                    return false;
                }
                if (array[0] instanceof Number) {
                    return this.isNaN((Number)array[0]);
                }
                return false;
            }
            case -3: {
                if (array.length == 0 || Undefined.instance == array[0]) {
                    return false;
                }
                if (array[0] instanceof Number) {
                    return this.isInteger((Number)array[0]);
                }
                return false;
            }
            case -4: {
                if (array.length == 0 || Undefined.instance == array[0]) {
                    return false;
                }
                if (array[0] instanceof Number) {
                    return this.isSafeInteger((Number)array[0]);
                }
                return false;
            }
            case -5: {
                return NativeGlobal.js_parseFloat(array);
            }
            case -6: {
                return NativeGlobal.js_parseInt(array);
            }
        }
    }
    
    static void init(final Scriptable scriptable, final boolean b) {
        new NativeNumber(0.0).exportAsJSClass(8, scriptable, b);
    }
    
    private boolean isDoubleInteger(final Double n) {
        return !n.isInfinite() && !n.isNaN() && Math.floor(n) == n;
    }
    
    private boolean isDoubleNan(final Double n) {
        return n.isNaN();
    }
    
    private boolean isDoubleSafeInteger(final Double n) {
        return this.isDoubleInteger(n) && n <= NativeNumber.MAX_SAFE_INTEGER && n >= NativeNumber.MIN_SAFE_INTEGER;
    }
    
    static Object isFinite(final Object o) {
        final Double value = ScriptRuntime.toNumber(o);
        return ScriptRuntime.wrapBoolean(!value.isInfinite() && !value.isNaN());
    }
    
    private boolean isInteger(final Number n) {
        return ScriptRuntime.toBoolean(this.isDoubleInteger(this.doubleVal(n)));
    }
    
    private Object isNaN(final Number n) {
        return ScriptRuntime.toBoolean(this.isDoubleNan(this.doubleVal(n)));
    }
    
    private boolean isSafeInteger(final Number n) {
        return ScriptRuntime.toBoolean(this.isDoubleSafeInteger(this.doubleVal(n)));
    }
    
    private static String num_to(final double n, final Object[] array, int n2, int n3, int int32, final int n4) {
        if (array.length == 0) {
            n3 = 0;
        }
        else {
            final double integer = ScriptRuntime.toInteger(array[0]);
            if (integer < int32 || integer > 100.0) {
                throw ScriptRuntime.constructError("RangeError", ScriptRuntime.getMessage1("msg.bad.precision", ScriptRuntime.toString(array[0])));
            }
            int32 = ScriptRuntime.toInt32(integer);
            n2 = n3;
            n3 = int32;
        }
        final StringBuilder sb = new StringBuilder();
        DToA.JS_dtostr(sb, n2, n3 + n4, n);
        return sb.toString();
    }
    
    @Override
    public Object execIdCall(final IdFunctionObject idFunctionObject, final Context context, final Scriptable scriptable, final Scriptable scriptable2, final Object[] array) {
        if (!idFunctionObject.hasTag(NativeNumber.NUMBER_TAG)) {
            return super.execIdCall(idFunctionObject, context, scriptable, scriptable2, array);
        }
        final int methodId = idFunctionObject.methodId();
        double number = 0.0;
        if (methodId == 1) {
            if (array.length >= 1) {
                number = ScriptRuntime.toNumber(array[0]);
            }
            if (scriptable2 == null) {
                return new NativeNumber(number);
            }
            return ScriptRuntime.wrapNumber(number);
        }
        else {
            if (methodId < 1) {
                return this.execConstructorCall(methodId, array);
            }
            if (!(scriptable2 instanceof NativeNumber)) {
                throw IdScriptableObject.incompatibleCallError(idFunctionObject);
            }
            final double doubleValue = ((NativeNumber)scriptable2).doubleValue;
            final int n = 10;
            switch (methodId) {
                default: {
                    throw new IllegalArgumentException(String.valueOf(methodId));
                }
                case 8: {
                    if (array.length == 0 || array[0] == Undefined.instance) {
                        return ScriptRuntime.numberToString(doubleValue, 10);
                    }
                    if (Double.isNaN(doubleValue)) {
                        return "NaN";
                    }
                    if (!Double.isInfinite(doubleValue)) {
                        return num_to(doubleValue, array, 0, 4, 1, 0);
                    }
                    if (doubleValue >= 0.0) {
                        return "Infinity";
                    }
                    return "-Infinity";
                }
                case 7: {
                    if (Double.isNaN(doubleValue)) {
                        return "NaN";
                    }
                    if (!Double.isInfinite(doubleValue)) {
                        return num_to(doubleValue, array, 1, 3, 0, 1);
                    }
                    if (doubleValue >= 0.0) {
                        return "Infinity";
                    }
                    return "-Infinity";
                }
                case 6: {
                    return num_to(doubleValue, array, 2, 2, -20, 0);
                }
                case 5: {
                    return ScriptRuntime.wrapNumber(doubleValue);
                }
                case 4: {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("(new Number(");
                    sb.append(ScriptRuntime.toString(doubleValue));
                    sb.append("))");
                    return sb.toString();
                }
                case 2:
                case 3: {
                    int int32 = n;
                    if (array.length != 0) {
                        if (array[0] == Undefined.instance) {
                            int32 = n;
                        }
                        else {
                            int32 = ScriptRuntime.toInt32(array[0]);
                        }
                    }
                    return ScriptRuntime.numberToString(doubleValue, int32);
                }
            }
        }
    }
    
    @Override
    protected void fillConstructorProperties(final IdFunctionObject idFunctionObject) {
        idFunctionObject.defineProperty("NaN", ScriptRuntime.NaNobj, 7);
        idFunctionObject.defineProperty("POSITIVE_INFINITY", ScriptRuntime.wrapNumber(Double.POSITIVE_INFINITY), 7);
        idFunctionObject.defineProperty("NEGATIVE_INFINITY", ScriptRuntime.wrapNumber(Double.NEGATIVE_INFINITY), 7);
        idFunctionObject.defineProperty("MAX_VALUE", ScriptRuntime.wrapNumber(Double.MAX_VALUE), 7);
        idFunctionObject.defineProperty("MIN_VALUE", ScriptRuntime.wrapNumber(Double.MIN_VALUE), 7);
        idFunctionObject.defineProperty("MAX_SAFE_INTEGER", ScriptRuntime.wrapNumber(NativeNumber.MAX_SAFE_INTEGER), 7);
        idFunctionObject.defineProperty("MIN_SAFE_INTEGER", ScriptRuntime.wrapNumber(NativeNumber.MIN_SAFE_INTEGER), 7);
        this.addIdFunctionProperty(idFunctionObject, NativeNumber.NUMBER_TAG, -1, "isFinite", 1);
        this.addIdFunctionProperty(idFunctionObject, NativeNumber.NUMBER_TAG, -2, "isNaN", 1);
        this.addIdFunctionProperty(idFunctionObject, NativeNumber.NUMBER_TAG, -3, "isInteger", 1);
        this.addIdFunctionProperty(idFunctionObject, NativeNumber.NUMBER_TAG, -4, "isSafeInteger", 1);
        this.addIdFunctionProperty(idFunctionObject, NativeNumber.NUMBER_TAG, -5, "parseFloat", 1);
        this.addIdFunctionProperty(idFunctionObject, NativeNumber.NUMBER_TAG, -6, "parseInt", 1);
        super.fillConstructorProperties(idFunctionObject);
    }
    
    @Override
    protected int findPrototypeId(final String s) {
        int n = 0;
        String s2 = null;
        switch (s.length()) {
            case 14: {
                s2 = "toLocaleString";
                n = 3;
                break;
            }
            case 13: {
                s2 = "toExponential";
                n = 7;
                break;
            }
            case 11: {
                final char char1 = s.charAt(0);
                if (char1 == 'c') {
                    s2 = "constructor";
                    n = 1;
                    break;
                }
                if (char1 == 't') {
                    s2 = "toPrecision";
                    n = 8;
                    break;
                }
                break;
            }
            case 8: {
                final char char2 = s.charAt(3);
                if (char2 == 'o') {
                    s2 = "toSource";
                    n = 4;
                    break;
                }
                if (char2 == 't') {
                    s2 = "toString";
                    n = 2;
                    break;
                }
                break;
            }
            case 7: {
                final char char3 = s.charAt(0);
                if (char3 == 't') {
                    s2 = "toFixed";
                    n = 6;
                    break;
                }
                if (char3 == 'v') {
                    s2 = "valueOf";
                    n = 5;
                    break;
                }
                break;
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
        return "Number";
    }
    
    @Override
    protected void initPrototypeId(final int n) {
        int n2 = 0;
        String s = null;
        switch (n) {
            default: {
                throw new IllegalArgumentException(String.valueOf(n));
            }
            case 8: {
                n2 = 1;
                s = "toPrecision";
                break;
            }
            case 7: {
                n2 = 1;
                s = "toExponential";
                break;
            }
            case 6: {
                n2 = 1;
                s = "toFixed";
                break;
            }
            case 5: {
                n2 = 0;
                s = "valueOf";
                break;
            }
            case 4: {
                n2 = 0;
                s = "toSource";
                break;
            }
            case 3: {
                n2 = 1;
                s = "toLocaleString";
                break;
            }
            case 2: {
                n2 = 1;
                s = "toString";
                break;
            }
            case 1: {
                n2 = 1;
                s = "constructor";
                break;
            }
        }
        this.initPrototypeMethod(NativeNumber.NUMBER_TAG, n, s, n2);
    }
    
    @Override
    public String toString() {
        return ScriptRuntime.numberToString(this.doubleValue, 10);
    }
}
