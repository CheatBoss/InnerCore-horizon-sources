package org.mozilla.javascript.typedarrays;

import org.mozilla.javascript.*;

public class NativeInt8Array extends NativeTypedArrayView<Byte>
{
    private static final String CLASS_NAME = "Int8Array";
    private static final long serialVersionUID = -3349419704390398895L;
    
    public NativeInt8Array() {
    }
    
    public NativeInt8Array(final int n) {
        this(new NativeArrayBuffer(n), 0, n);
    }
    
    public NativeInt8Array(final NativeArrayBuffer nativeArrayBuffer, final int n, final int n2) {
        super(nativeArrayBuffer, n, n2, n2);
    }
    
    public static void init(final Context context, final Scriptable scriptable, final boolean b) {
        new NativeInt8Array().exportAsJSClass(4, scriptable, b);
    }
    
    @Override
    protected NativeTypedArrayView construct(final NativeArrayBuffer nativeArrayBuffer, final int n, final int n2) {
        return new NativeInt8Array(nativeArrayBuffer, n, n2);
    }
    
    @Override
    public Byte get(final int n) {
        if (this.checkIndex(n)) {
            throw new IndexOutOfBoundsException();
        }
        return (Byte)this.js_get(n);
    }
    
    @Override
    public int getBytesPerElement() {
        return 1;
    }
    
    @Override
    public String getClassName() {
        return "Int8Array";
    }
    
    @Override
    protected Object js_get(final int n) {
        if (this.checkIndex(n)) {
            return Undefined.instance;
        }
        return ByteIo.readInt8(this.arrayBuffer.buffer, this.offset + n);
    }
    
    @Override
    protected Object js_set(final int n, final Object o) {
        if (this.checkIndex(n)) {
            return Undefined.instance;
        }
        ByteIo.writeInt8(this.arrayBuffer.buffer, this.offset + n, Conversions.toInt8(o));
        return null;
    }
    
    @Override
    protected NativeTypedArrayView realThis(final Scriptable scriptable, final IdFunctionObject idFunctionObject) {
        if (!(scriptable instanceof NativeInt8Array)) {
            throw IdScriptableObject.incompatibleCallError(idFunctionObject);
        }
        return (NativeInt8Array)scriptable;
    }
    
    @Override
    public Byte set(final int n, final Byte b) {
        if (this.checkIndex(n)) {
            throw new IndexOutOfBoundsException();
        }
        return (Byte)this.js_set(n, b);
    }
}
