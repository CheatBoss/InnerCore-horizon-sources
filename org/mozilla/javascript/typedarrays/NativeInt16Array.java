package org.mozilla.javascript.typedarrays;

import org.mozilla.javascript.*;

public class NativeInt16Array extends NativeTypedArrayView<Short>
{
    private static final int BYTES_PER_ELEMENT = 2;
    private static final String CLASS_NAME = "Int16Array";
    private static final long serialVersionUID = -8592870435287581398L;
    
    public NativeInt16Array() {
    }
    
    public NativeInt16Array(final int n) {
        this(new NativeArrayBuffer(n * 2), 0, n);
    }
    
    public NativeInt16Array(final NativeArrayBuffer nativeArrayBuffer, final int n, final int n2) {
        super(nativeArrayBuffer, n, n2, n2 * 2);
    }
    
    public static void init(final Context context, final Scriptable scriptable, final boolean b) {
        new NativeInt16Array().exportAsJSClass(4, scriptable, b);
    }
    
    @Override
    protected NativeTypedArrayView construct(final NativeArrayBuffer nativeArrayBuffer, final int n, final int n2) {
        return new NativeInt16Array(nativeArrayBuffer, n, n2);
    }
    
    @Override
    public Short get(final int n) {
        if (this.checkIndex(n)) {
            throw new IndexOutOfBoundsException();
        }
        return (Short)this.js_get(n);
    }
    
    @Override
    public int getBytesPerElement() {
        return 2;
    }
    
    @Override
    public String getClassName() {
        return "Int16Array";
    }
    
    @Override
    protected Object js_get(final int n) {
        if (this.checkIndex(n)) {
            return Undefined.instance;
        }
        return ByteIo.readInt16(this.arrayBuffer.buffer, n * 2 + this.offset, false);
    }
    
    @Override
    protected Object js_set(final int n, final Object o) {
        if (this.checkIndex(n)) {
            return Undefined.instance;
        }
        ByteIo.writeInt16(this.arrayBuffer.buffer, n * 2 + this.offset, Conversions.toInt16(o), false);
        return null;
    }
    
    @Override
    protected NativeTypedArrayView realThis(final Scriptable scriptable, final IdFunctionObject idFunctionObject) {
        if (!(scriptable instanceof NativeInt16Array)) {
            throw IdScriptableObject.incompatibleCallError(idFunctionObject);
        }
        return (NativeInt16Array)scriptable;
    }
    
    @Override
    public Short set(final int n, final Short n2) {
        if (this.checkIndex(n)) {
            throw new IndexOutOfBoundsException();
        }
        return (Short)this.js_set(n, n2);
    }
}
