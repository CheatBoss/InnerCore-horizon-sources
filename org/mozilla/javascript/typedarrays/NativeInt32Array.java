package org.mozilla.javascript.typedarrays;

import org.mozilla.javascript.*;

public class NativeInt32Array extends NativeTypedArrayView<Integer>
{
    private static final int BYTES_PER_ELEMENT = 4;
    private static final String CLASS_NAME = "Int32Array";
    private static final long serialVersionUID = -8963461831950499340L;
    
    public NativeInt32Array() {
    }
    
    public NativeInt32Array(final int n) {
        this(new NativeArrayBuffer(n * 4), 0, n);
    }
    
    public NativeInt32Array(final NativeArrayBuffer nativeArrayBuffer, final int n, final int n2) {
        super(nativeArrayBuffer, n, n2, n2 * 4);
    }
    
    public static void init(final Context context, final Scriptable scriptable, final boolean b) {
        new NativeInt32Array().exportAsJSClass(4, scriptable, b);
    }
    
    @Override
    protected NativeTypedArrayView construct(final NativeArrayBuffer nativeArrayBuffer, final int n, final int n2) {
        return new NativeInt32Array(nativeArrayBuffer, n, n2);
    }
    
    @Override
    public Integer get(final int n) {
        if (this.checkIndex(n)) {
            throw new IndexOutOfBoundsException();
        }
        return (Integer)this.js_get(n);
    }
    
    @Override
    public int getBytesPerElement() {
        return 4;
    }
    
    @Override
    public String getClassName() {
        return "Int32Array";
    }
    
    @Override
    protected Object js_get(final int n) {
        if (this.checkIndex(n)) {
            return Undefined.instance;
        }
        return ByteIo.readInt32(this.arrayBuffer.buffer, n * 4 + this.offset, false);
    }
    
    @Override
    protected Object js_set(final int n, final Object o) {
        if (this.checkIndex(n)) {
            return Undefined.instance;
        }
        ByteIo.writeInt32(this.arrayBuffer.buffer, n * 4 + this.offset, ScriptRuntime.toInt32(o), false);
        return null;
    }
    
    @Override
    protected NativeTypedArrayView realThis(final Scriptable scriptable, final IdFunctionObject idFunctionObject) {
        if (!(scriptable instanceof NativeInt32Array)) {
            throw IdScriptableObject.incompatibleCallError(idFunctionObject);
        }
        return (NativeInt32Array)scriptable;
    }
    
    @Override
    public Integer set(final int n, final Integer n2) {
        if (this.checkIndex(n)) {
            throw new IndexOutOfBoundsException();
        }
        return (Integer)this.js_set(n, n2);
    }
}
