package org.mozilla.javascript.typedarrays;

import org.mozilla.javascript.*;

public class NativeArrayBuffer extends IdScriptableObject
{
    public static final String CLASS_NAME = "ArrayBuffer";
    private static final int ConstructorId_isView = -3;
    private static final byte[] EMPTY_BUF;
    public static final NativeArrayBuffer EMPTY_BUFFER;
    private static final int Id_byteLength = 1;
    private static final int Id_constructor = 1;
    private static final int Id_isView = 3;
    private static final int Id_slice = 2;
    private static final int MAX_INSTANCE_ID = 1;
    private static final int MAX_PROTOTYPE_ID = 3;
    private static final long serialVersionUID = 3110411773054879549L;
    final byte[] buffer;
    
    static {
        EMPTY_BUF = new byte[0];
        EMPTY_BUFFER = new NativeArrayBuffer();
    }
    
    public NativeArrayBuffer() {
        this.buffer = NativeArrayBuffer.EMPTY_BUF;
    }
    
    public NativeArrayBuffer(final int n) {
        if (n < 0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Negative array length ");
            sb.append(n);
            throw ScriptRuntime.constructError("RangeError", sb.toString());
        }
        if (n == 0) {
            this.buffer = NativeArrayBuffer.EMPTY_BUF;
            return;
        }
        this.buffer = new byte[n];
    }
    
    public static void init(final Context context, final Scriptable scriptable, final boolean b) {
        new NativeArrayBuffer().exportAsJSClass(3, scriptable, b);
    }
    
    private static boolean isArg(final Object[] array, final int n) {
        return array.length > n && !Undefined.instance.equals(array[n]);
    }
    
    private static NativeArrayBuffer realThis(final Scriptable scriptable, final IdFunctionObject idFunctionObject) {
        if (!(scriptable instanceof NativeArrayBuffer)) {
            throw IdScriptableObject.incompatibleCallError(idFunctionObject);
        }
        return (NativeArrayBuffer)scriptable;
    }
    
    @Override
    public Object execIdCall(final IdFunctionObject idFunctionObject, final Context context, final Scriptable scriptable, final Scriptable scriptable2, final Object[] array) {
        if (!idFunctionObject.hasTag("ArrayBuffer")) {
            return super.execIdCall(idFunctionObject, context, scriptable, scriptable2, array);
        }
        final int methodId = idFunctionObject.methodId();
        boolean b = true;
        final boolean b2 = false;
        int int32 = 0;
        if (methodId == -3) {
            if (!isArg(array, 0) || !(array[0] instanceof NativeArrayBufferView)) {
                b = false;
            }
            return b;
        }
        switch (methodId) {
            default: {
                throw new IllegalArgumentException(String.valueOf(methodId));
            }
            case 2: {
                final NativeArrayBuffer realThis = realThis(scriptable2, idFunctionObject);
                if (isArg(array, 0)) {
                    int32 = ScriptRuntime.toInt32(array[0]);
                }
                int n;
                if (isArg(array, 1)) {
                    n = ScriptRuntime.toInt32(array[1]);
                }
                else {
                    n = realThis.buffer.length;
                }
                return realThis.slice(int32, n);
            }
            case 1: {
                int int33 = b2 ? 1 : 0;
                if (isArg(array, 0)) {
                    int33 = ScriptRuntime.toInt32(array[0]);
                }
                return new NativeArrayBuffer(int33);
            }
        }
    }
    
    @Override
    protected void fillConstructorProperties(final IdFunctionObject idFunctionObject) {
        this.addIdFunctionProperty(idFunctionObject, "ArrayBuffer", -3, "isView", 1);
    }
    
    @Override
    protected int findInstanceIdInfo(final String s) {
        if ("byteLength".equals(s)) {
            return IdScriptableObject.instanceIdInfo(5, 1);
        }
        return super.findInstanceIdInfo(s);
    }
    
    @Override
    protected int findPrototypeId(final String s) {
        int n = 0;
        String s2 = null;
        final int length = s.length();
        if (length == 5) {
            s2 = "slice";
            n = 2;
        }
        else if (length == 6) {
            s2 = "isView";
            n = 3;
        }
        else if (length == 11) {
            s2 = "constructor";
            n = 1;
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
    
    public byte[] getBuffer() {
        return this.buffer;
    }
    
    @Override
    public String getClassName() {
        return "ArrayBuffer";
    }
    
    @Override
    protected String getInstanceIdName(final int n) {
        if (n == 1) {
            return "byteLength";
        }
        return super.getInstanceIdName(n);
    }
    
    @Override
    protected Object getInstanceIdValue(final int n) {
        if (n == 1) {
            return ScriptRuntime.wrapInt(this.buffer.length);
        }
        return super.getInstanceIdValue(n);
    }
    
    public int getLength() {
        return this.buffer.length;
    }
    
    @Override
    protected int getMaxInstanceId() {
        return 1;
    }
    
    @Override
    protected void initPrototypeId(final int n) {
        String s = null;
        switch (n) {
            default: {
                throw new IllegalArgumentException(String.valueOf(n));
            }
            case 2: {
                s = "slice";
                break;
            }
            case 1: {
                s = "constructor";
                break;
            }
        }
        this.initPrototypeMethod("ArrayBuffer", n, s, 1);
    }
    
    public NativeArrayBuffer slice(int min, int max) {
        final int length = this.buffer.length;
        if (max < 0) {
            max += this.buffer.length;
        }
        max = Math.max(0, Math.min(length, max));
        if (min < 0) {
            min += this.buffer.length;
        }
        min = Math.min(max, Math.max(0, min));
        max -= min;
        final NativeArrayBuffer nativeArrayBuffer = new NativeArrayBuffer(max);
        System.arraycopy(this.buffer, min, nativeArrayBuffer.buffer, 0, max);
        return nativeArrayBuffer;
    }
}
