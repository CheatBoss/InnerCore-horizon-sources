package org.mozilla.javascript.typedarrays;

import org.mozilla.javascript.*;

public class NativeUint8Array extends NativeTypedArrayView<Integer>
{
    private static final String CLASS_NAME = "Uint8Array";
    private static final long serialVersionUID = -3349419704390398895L;
    
    public NativeUint8Array() {
    }
    
    public NativeUint8Array(final int n) {
        this(new NativeArrayBuffer(n), 0, n);
    }
    
    public NativeUint8Array(final NativeArrayBuffer nativeArrayBuffer, final int n, final int n2) {
        super(nativeArrayBuffer, n, n2, n2);
    }
    
    public static void init(final Context context, final Scriptable scriptable, final boolean b) {
        new NativeUint8Array().exportAsJSClass(4, scriptable, b);
    }
    
    @Override
    protected NativeTypedArrayView construct(final NativeArrayBuffer nativeArrayBuffer, final int n, final int n2) {
        return new NativeUint8Array(nativeArrayBuffer, n, n2);
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
        return 1;
    }
    
    @Override
    public String getClassName() {
        return "Uint8Array";
    }
    
    @Override
    protected Object js_get(final int n) {
        if (this.checkIndex(n)) {
            return Undefined.instance;
        }
        return ByteIo.readUint8(this.arrayBuffer.buffer, this.offset + n);
    }
    
    @Override
    protected Object js_set(final int n, final Object o) {
        if (this.checkIndex(n)) {
            return Undefined.instance;
        }
        ByteIo.writeUint8(this.arrayBuffer.buffer, this.offset + n, Conversions.toUint8(o));
        return null;
    }
    
    @Override
    protected NativeTypedArrayView realThis(final Scriptable scriptable, final IdFunctionObject idFunctionObject) {
        if (!(scriptable instanceof NativeUint8Array)) {
            throw IdScriptableObject.incompatibleCallError(idFunctionObject);
        }
        return (NativeUint8Array)scriptable;
    }
    
    @Override
    public Integer set(final int n, final Integer n2) {
        if (this.checkIndex(n)) {
            throw new IndexOutOfBoundsException();
        }
        return (Integer)this.js_set(n, n2);
    }
}
