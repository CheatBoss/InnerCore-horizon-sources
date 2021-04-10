package org.mozilla.javascript.typedarrays;

import org.mozilla.javascript.*;

public class NativeFloat32Array extends NativeTypedArrayView<Float>
{
    private static final int BYTES_PER_ELEMENT = 4;
    private static final String CLASS_NAME = "Float32Array";
    private static final long serialVersionUID = -8963461831950499340L;
    
    public NativeFloat32Array() {
    }
    
    public NativeFloat32Array(final int n) {
        this(new NativeArrayBuffer(n * 4), 0, n);
    }
    
    public NativeFloat32Array(final NativeArrayBuffer nativeArrayBuffer, final int n, final int n2) {
        super(nativeArrayBuffer, n, n2, n2 * 4);
    }
    
    public static void init(final Context context, final Scriptable scriptable, final boolean b) {
        new NativeFloat32Array().exportAsJSClass(4, scriptable, b);
    }
    
    @Override
    protected NativeTypedArrayView construct(final NativeArrayBuffer nativeArrayBuffer, final int n, final int n2) {
        return new NativeFloat32Array(nativeArrayBuffer, n, n2);
    }
    
    @Override
    public Float get(final int n) {
        if (this.checkIndex(n)) {
            throw new IndexOutOfBoundsException();
        }
        return (Float)this.js_get(n);
    }
    
    @Override
    public int getBytesPerElement() {
        return 4;
    }
    
    @Override
    public String getClassName() {
        return "Float32Array";
    }
    
    @Override
    protected Object js_get(final int n) {
        if (this.checkIndex(n)) {
            return Undefined.instance;
        }
        return ByteIo.readFloat32(this.arrayBuffer.buffer, n * 4 + this.offset, false);
    }
    
    @Override
    protected Object js_set(final int n, final Object o) {
        if (this.checkIndex(n)) {
            return Undefined.instance;
        }
        ByteIo.writeFloat32(this.arrayBuffer.buffer, n * 4 + this.offset, ScriptRuntime.toNumber(o), false);
        return null;
    }
    
    @Override
    protected NativeTypedArrayView realThis(final Scriptable scriptable, final IdFunctionObject idFunctionObject) {
        if (!(scriptable instanceof NativeFloat32Array)) {
            throw IdScriptableObject.incompatibleCallError(idFunctionObject);
        }
        return (NativeFloat32Array)scriptable;
    }
    
    @Override
    public Float set(final int n, final Float n2) {
        if (this.checkIndex(n)) {
            throw new IndexOutOfBoundsException();
        }
        return (Float)this.js_set(n, n2);
    }
}
