package org.mozilla.javascript.typedarrays;

import org.mozilla.javascript.*;

public class NativeFloat64Array extends NativeTypedArrayView<Double>
{
    private static final int BYTES_PER_ELEMENT = 8;
    private static final String CLASS_NAME = "Float64Array";
    private static final long serialVersionUID = -1255405650050639335L;
    
    public NativeFloat64Array() {
    }
    
    public NativeFloat64Array(final int n) {
        this(new NativeArrayBuffer(n * 8), 0, n);
    }
    
    public NativeFloat64Array(final NativeArrayBuffer nativeArrayBuffer, final int n, final int n2) {
        super(nativeArrayBuffer, n, n2, n2 * 8);
    }
    
    public static void init(final Context context, final Scriptable scriptable, final boolean b) {
        new NativeFloat64Array().exportAsJSClass(4, scriptable, b);
    }
    
    @Override
    protected NativeTypedArrayView construct(final NativeArrayBuffer nativeArrayBuffer, final int n, final int n2) {
        return new NativeFloat64Array(nativeArrayBuffer, n, n2);
    }
    
    @Override
    public Double get(final int n) {
        if (this.checkIndex(n)) {
            throw new IndexOutOfBoundsException();
        }
        return (Double)this.js_get(n);
    }
    
    @Override
    public int getBytesPerElement() {
        return 8;
    }
    
    @Override
    public String getClassName() {
        return "Float64Array";
    }
    
    @Override
    protected Object js_get(final int n) {
        if (this.checkIndex(n)) {
            return Undefined.instance;
        }
        return Double.longBitsToDouble(ByteIo.readUint64Primitive(this.arrayBuffer.buffer, n * 8 + this.offset, false));
    }
    
    @Override
    protected Object js_set(final int n, final Object o) {
        if (this.checkIndex(n)) {
            return Undefined.instance;
        }
        ByteIo.writeUint64(this.arrayBuffer.buffer, n * 8 + this.offset, Double.doubleToLongBits(ScriptRuntime.toNumber(o)), false);
        return null;
    }
    
    @Override
    protected NativeTypedArrayView realThis(final Scriptable scriptable, final IdFunctionObject idFunctionObject) {
        if (!(scriptable instanceof NativeFloat64Array)) {
            throw IdScriptableObject.incompatibleCallError(idFunctionObject);
        }
        return (NativeFloat64Array)scriptable;
    }
    
    @Override
    public Double set(final int n, final Double n2) {
        if (this.checkIndex(n)) {
            throw new IndexOutOfBoundsException();
        }
        return (Double)this.js_set(n, n2);
    }
}
