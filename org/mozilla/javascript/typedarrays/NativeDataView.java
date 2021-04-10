package org.mozilla.javascript.typedarrays;

import org.mozilla.javascript.*;

public class NativeDataView extends NativeArrayBufferView
{
    public static final String CLASS_NAME = "DataView";
    private static final int Id_constructor = 1;
    private static final int Id_getFloat32 = 8;
    private static final int Id_getFloat64 = 9;
    private static final int Id_getInt16 = 4;
    private static final int Id_getInt32 = 6;
    private static final int Id_getInt8 = 2;
    private static final int Id_getUint16 = 5;
    private static final int Id_getUint32 = 7;
    private static final int Id_getUint8 = 3;
    private static final int Id_setFloat32 = 16;
    private static final int Id_setFloat64 = 17;
    private static final int Id_setInt16 = 12;
    private static final int Id_setInt32 = 14;
    private static final int Id_setInt8 = 10;
    private static final int Id_setUint16 = 13;
    private static final int Id_setUint32 = 15;
    private static final int Id_setUint8 = 11;
    private static final int MAX_PROTOTYPE_ID = 17;
    private static final long serialVersionUID = 1427967607557438968L;
    
    public NativeDataView() {
    }
    
    public NativeDataView(final NativeArrayBuffer nativeArrayBuffer, final int n, final int n2) {
        super(nativeArrayBuffer, n, n2);
    }
    
    private void checkOffset(final Object[] array, final int n) {
        if (array.length <= n) {
            throw ScriptRuntime.constructError("TypeError", "missing required offset parameter");
        }
        if (Undefined.instance.equals(array[n])) {
            throw ScriptRuntime.constructError("RangeError", "invalid offset");
        }
    }
    
    private void checkValue(final Object[] array, final int n) {
        if (array.length <= n) {
            throw ScriptRuntime.constructError("TypeError", "missing required value parameter");
        }
        if (Undefined.instance.equals(array[n])) {
            throw ScriptRuntime.constructError("RangeError", "invalid value parameter");
        }
    }
    
    public static void init(final Context context, final Scriptable scriptable, final boolean b) {
        new NativeDataView().exportAsJSClass(17, scriptable, b);
    }
    
    private NativeDataView js_constructor(final NativeArrayBuffer nativeArrayBuffer, final int n, final int n2) {
        if (n2 < 0) {
            throw ScriptRuntime.constructError("RangeError", "length out of range");
        }
        if (n >= 0 && n + n2 <= nativeArrayBuffer.getLength()) {
            return new NativeDataView(nativeArrayBuffer, n, n2);
        }
        throw ScriptRuntime.constructError("RangeError", "offset out of range");
    }
    
    private Object js_getFloat(final int n, final Object[] array) {
        final boolean b = false;
        this.checkOffset(array, 0);
        final int int32 = ScriptRuntime.toInt32(array[0]);
        this.rangeCheck(int32, n);
        boolean b2 = b;
        if (NativeArrayBufferView.isArg(array, 1)) {
            b2 = b;
            if (n > 1) {
                b2 = b;
                if (ScriptRuntime.toBoolean(array[1])) {
                    b2 = true;
                }
            }
        }
        if (n == 4) {
            return ByteIo.readFloat32(this.arrayBuffer.buffer, int32, b2);
        }
        if (n != 8) {
            throw new AssertionError();
        }
        return ByteIo.readFloat64(this.arrayBuffer.buffer, int32, b2);
    }
    
    private Object js_getInt(final int n, final boolean b, final Object[] array) {
        final boolean b2 = false;
        this.checkOffset(array, 0);
        final int int32 = ScriptRuntime.toInt32(array[0]);
        this.rangeCheck(int32, n);
        boolean b3 = b2;
        if (NativeArrayBufferView.isArg(array, 1)) {
            b3 = b2;
            if (n > 1) {
                b3 = b2;
                if (ScriptRuntime.toBoolean(array[1])) {
                    b3 = true;
                }
            }
        }
        if (n != 4) {
            switch (n) {
                default: {
                    throw new AssertionError();
                }
                case 2: {
                    if (b) {
                        return ByteIo.readInt16(this.arrayBuffer.buffer, int32, b3);
                    }
                    return ByteIo.readUint16(this.arrayBuffer.buffer, int32, b3);
                }
                case 1: {
                    if (b) {
                        return ByteIo.readInt8(this.arrayBuffer.buffer, int32);
                    }
                    return ByteIo.readUint8(this.arrayBuffer.buffer, int32);
                }
            }
        }
        else {
            if (b) {
                return ByteIo.readInt32(this.arrayBuffer.buffer, int32, b3);
            }
            return ByteIo.readUint32(this.arrayBuffer.buffer, int32, b3);
        }
    }
    
    private void js_setFloat(final int n, final Object[] array) {
        final boolean b = false;
        this.checkOffset(array, 0);
        this.checkValue(array, 1);
        final int int32 = ScriptRuntime.toInt32(array[0]);
        this.rangeCheck(int32, n);
        boolean b2 = b;
        if (NativeArrayBufferView.isArg(array, 2)) {
            b2 = b;
            if (n > 1) {
                b2 = b;
                if (ScriptRuntime.toBoolean(array[2])) {
                    b2 = true;
                }
            }
        }
        final double number = ScriptRuntime.toNumber(array[1]);
        if (n == 4) {
            ByteIo.writeFloat32(this.arrayBuffer.buffer, int32, number, b2);
            return;
        }
        if (n != 8) {
            throw new AssertionError();
        }
        ByteIo.writeFloat64(this.arrayBuffer.buffer, int32, number, b2);
    }
    
    private void js_setInt(final int n, final boolean b, final Object[] array) {
        final boolean b2 = false;
        this.checkOffset(array, 0);
        this.checkValue(array, 1);
        final int int32 = ScriptRuntime.toInt32(array[0]);
        this.rangeCheck(int32, n);
        boolean b3 = b2;
        if (NativeArrayBufferView.isArg(array, 2)) {
            b3 = b2;
            if (n > 1) {
                b3 = b2;
                if (ScriptRuntime.toBoolean(array[2])) {
                    b3 = true;
                }
            }
        }
        if (n != 4) {
            switch (n) {
                default: {
                    throw new AssertionError();
                }
                case 2: {
                    if (b) {
                        ByteIo.writeInt16(this.arrayBuffer.buffer, int32, Conversions.toInt16(array[1]), b3);
                        return;
                    }
                    ByteIo.writeUint16(this.arrayBuffer.buffer, int32, Conversions.toUint16(array[1]), b3);
                }
                case 1: {
                    if (b) {
                        ByteIo.writeInt8(this.arrayBuffer.buffer, int32, Conversions.toInt8(array[1]));
                        return;
                    }
                    ByteIo.writeUint8(this.arrayBuffer.buffer, int32, Conversions.toUint8(array[1]));
                }
            }
        }
        else {
            if (b) {
                ByteIo.writeInt32(this.arrayBuffer.buffer, int32, Conversions.toInt32(array[1]), b3);
                return;
            }
            ByteIo.writeUint32(this.arrayBuffer.buffer, int32, Conversions.toUint32(array[1]), b3);
        }
    }
    
    private void rangeCheck(final int n, final int n2) {
        if (n >= 0 && n + n2 <= this.byteLength) {
            return;
        }
        throw ScriptRuntime.constructError("RangeError", "offset out of range");
    }
    
    private static NativeDataView realThis(final Scriptable scriptable, final IdFunctionObject idFunctionObject) {
        if (!(scriptable instanceof NativeDataView)) {
            throw IdScriptableObject.incompatibleCallError(idFunctionObject);
        }
        return (NativeDataView)scriptable;
    }
    
    @Override
    public Object execIdCall(final IdFunctionObject idFunctionObject, final Context context, final Scriptable scriptable, final Scriptable scriptable2, final Object[] array) {
        if (!idFunctionObject.hasTag(this.getClassName())) {
            return super.execIdCall(idFunctionObject, context, scriptable, scriptable2, array);
        }
        final int methodId = idFunctionObject.methodId();
        int int32 = 0;
        switch (methodId) {
            default: {
                throw new IllegalArgumentException(String.valueOf(methodId));
            }
            case 17: {
                realThis(scriptable2, idFunctionObject).js_setFloat(8, array);
                return Undefined.instance;
            }
            case 16: {
                realThis(scriptable2, idFunctionObject).js_setFloat(4, array);
                return Undefined.instance;
            }
            case 15: {
                realThis(scriptable2, idFunctionObject).js_setInt(4, false, array);
                return Undefined.instance;
            }
            case 14: {
                realThis(scriptable2, idFunctionObject).js_setInt(4, true, array);
                return Undefined.instance;
            }
            case 13: {
                realThis(scriptable2, idFunctionObject).js_setInt(2, false, array);
                return Undefined.instance;
            }
            case 12: {
                realThis(scriptable2, idFunctionObject).js_setInt(2, true, array);
                return Undefined.instance;
            }
            case 11: {
                realThis(scriptable2, idFunctionObject).js_setInt(1, false, array);
                return Undefined.instance;
            }
            case 10: {
                realThis(scriptable2, idFunctionObject).js_setInt(1, true, array);
                return Undefined.instance;
            }
            case 9: {
                return realThis(scriptable2, idFunctionObject).js_getFloat(8, array);
            }
            case 8: {
                return realThis(scriptable2, idFunctionObject).js_getFloat(4, array);
            }
            case 7: {
                return realThis(scriptable2, idFunctionObject).js_getInt(4, false, array);
            }
            case 6: {
                return realThis(scriptable2, idFunctionObject).js_getInt(4, true, array);
            }
            case 5: {
                return realThis(scriptable2, idFunctionObject).js_getInt(2, false, array);
            }
            case 4: {
                return realThis(scriptable2, idFunctionObject).js_getInt(2, true, array);
            }
            case 3: {
                return realThis(scriptable2, idFunctionObject).js_getInt(1, false, array);
            }
            case 2: {
                return realThis(scriptable2, idFunctionObject).js_getInt(1, true, array);
            }
            case 1: {
                if (NativeArrayBufferView.isArg(array, 0) && array[0] instanceof NativeArrayBuffer) {
                    final NativeArrayBuffer nativeArrayBuffer = (NativeArrayBuffer)array[0];
                    if (NativeArrayBufferView.isArg(array, 1)) {
                        int32 = ScriptRuntime.toInt32(array[1]);
                    }
                    int int33;
                    if (NativeArrayBufferView.isArg(array, 2)) {
                        int33 = ScriptRuntime.toInt32(array[2]);
                    }
                    else {
                        int33 = nativeArrayBuffer.getLength() - int32;
                    }
                    return this.js_constructor(nativeArrayBuffer, int32, int33);
                }
                throw ScriptRuntime.constructError("TypeError", "Missing parameters");
            }
        }
    }
    
    @Override
    protected int findPrototypeId(final String s) {
        final boolean b = false;
        final String s2 = null;
        int n = 0;
        String s3 = null;
        switch (s.length()) {
            default: {
                n = (b ? 1 : 0);
                s3 = s2;
                break;
            }
            case 11: {
                s3 = "constructor";
                n = 1;
                break;
            }
            case 10: {
                final char char1 = s.charAt(0);
                if (char1 == 'g') {
                    final char char2 = s.charAt(9);
                    if (char2 == '2') {
                        s3 = "getFloat32";
                        n = 8;
                        break;
                    }
                    n = (b ? 1 : 0);
                    s3 = s2;
                    if (char2 == '4') {
                        s3 = "getFloat64";
                        n = 9;
                        break;
                    }
                    break;
                }
                else {
                    n = (b ? 1 : 0);
                    s3 = s2;
                    if (char1 != 's') {
                        break;
                    }
                    final char char3 = s.charAt(9);
                    if (char3 == '2') {
                        s3 = "setFloat32";
                        n = 16;
                        break;
                    }
                    n = (b ? 1 : 0);
                    s3 = s2;
                    if (char3 == '4') {
                        s3 = "setFloat64";
                        n = 17;
                        break;
                    }
                    break;
                }
                break;
            }
            case 9: {
                final char char4 = s.charAt(0);
                if (char4 == 'g') {
                    final char char5 = s.charAt(8);
                    if (char5 == '2') {
                        s3 = "getUint32";
                        n = 7;
                        break;
                    }
                    n = (b ? 1 : 0);
                    s3 = s2;
                    if (char5 == '6') {
                        s3 = "getUint16";
                        n = 5;
                        break;
                    }
                    break;
                }
                else {
                    n = (b ? 1 : 0);
                    s3 = s2;
                    if (char4 != 's') {
                        break;
                    }
                    final char char6 = s.charAt(8);
                    if (char6 == '2') {
                        s3 = "setUint32";
                        n = 15;
                        break;
                    }
                    n = (b ? 1 : 0);
                    s3 = s2;
                    if (char6 == '6') {
                        s3 = "setUint16";
                        n = 13;
                        break;
                    }
                    break;
                }
                break;
            }
            case 8: {
                final char char7 = s.charAt(6);
                if (char7 == '1') {
                    final char char8 = s.charAt(0);
                    if (char8 == 'g') {
                        s3 = "getInt16";
                        n = 4;
                        break;
                    }
                    n = (b ? 1 : 0);
                    s3 = s2;
                    if (char8 == 's') {
                        s3 = "setInt16";
                        n = 12;
                        break;
                    }
                    break;
                }
                else if (char7 == '3') {
                    final char char9 = s.charAt(0);
                    if (char9 == 'g') {
                        s3 = "getInt32";
                        n = 6;
                        break;
                    }
                    n = (b ? 1 : 0);
                    s3 = s2;
                    if (char9 == 's') {
                        s3 = "setInt32";
                        n = 14;
                        break;
                    }
                    break;
                }
                else {
                    if (char7 != 't') {
                        n = (b ? 1 : 0);
                        s3 = s2;
                        break;
                    }
                    final char char10 = s.charAt(0);
                    if (char10 == 'g') {
                        s3 = "getUint8";
                        n = 3;
                        break;
                    }
                    n = (b ? 1 : 0);
                    s3 = s2;
                    if (char10 == 's') {
                        s3 = "setUint8";
                        n = 11;
                        break;
                    }
                    break;
                }
                break;
            }
            case 7: {
                final char char11 = s.charAt(0);
                if (char11 == 'g') {
                    s3 = "getInt8";
                    n = 2;
                    break;
                }
                n = (b ? 1 : 0);
                s3 = s2;
                if (char11 == 's') {
                    s3 = "setInt8";
                    n = 10;
                    break;
                }
                break;
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
        return "DataView";
    }
    
    @Override
    protected void initPrototypeId(final int n) {
        int n2 = 0;
        String s = null;
        switch (n) {
            default: {
                throw new IllegalArgumentException(String.valueOf(n));
            }
            case 17: {
                n2 = 2;
                s = "setFloat64";
                break;
            }
            case 16: {
                n2 = 2;
                s = "setFloat32";
                break;
            }
            case 15: {
                n2 = 2;
                s = "setUint32";
                break;
            }
            case 14: {
                n2 = 2;
                s = "setInt32";
                break;
            }
            case 13: {
                n2 = 2;
                s = "setUint16";
                break;
            }
            case 12: {
                n2 = 2;
                s = "setInt16";
                break;
            }
            case 11: {
                n2 = 2;
                s = "setUint8";
                break;
            }
            case 10: {
                n2 = 2;
                s = "setInt8";
                break;
            }
            case 9: {
                n2 = 1;
                s = "getFloat64";
                break;
            }
            case 8: {
                n2 = 1;
                s = "getFloat32";
                break;
            }
            case 7: {
                n2 = 1;
                s = "getUint32";
                break;
            }
            case 6: {
                n2 = 1;
                s = "getInt32";
                break;
            }
            case 5: {
                n2 = 1;
                s = "getUint16";
                break;
            }
            case 4: {
                n2 = 1;
                s = "getInt16";
                break;
            }
            case 3: {
                n2 = 1;
                s = "getUint8";
                break;
            }
            case 2: {
                n2 = 1;
                s = "getInt8";
                break;
            }
            case 1: {
                n2 = 1;
                s = "constructor";
                break;
            }
        }
        this.initPrototypeMethod(this.getClassName(), n, s, n2);
    }
}
