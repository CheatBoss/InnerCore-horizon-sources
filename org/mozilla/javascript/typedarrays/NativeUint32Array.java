package org.mozilla.javascript.typedarrays;

import org.mozilla.javascript.*;

public class NativeUint32Array extends NativeTypedArrayView<Long>
{
    private static final int BYTES_PER_ELEMENT = 4;
    private static final String CLASS_NAME = "Uint32Array";
    private static final long serialVersionUID = -7987831421954144244L;
    
    public NativeUint32Array() {
    }
    
    public NativeUint32Array(final int n) {
        this(new NativeArrayBuffer(n * 4), 0, n);
    }
    
    public NativeUint32Array(final NativeArrayBuffer nativeArrayBuffer, final int n, final int n2) {
        super(nativeArrayBuffer, n, n2, n2 * 4);
    }
    
    public static void init(final Context context, final Scriptable scriptable, final boolean b) {
        new NativeUint32Array().exportAsJSClass(4, scriptable, b);
    }
    
    @Override
    protected NativeTypedArrayView construct(final NativeArrayBuffer nativeArrayBuffer, final int n, final int n2) {
        return new NativeUint32Array(nativeArrayBuffer, n, n2);
    }
    
    @Override
    public Long get(final int n) {
        if (this.checkIndex(n)) {
            throw new IndexOutOfBoundsException();
        }
        return (Long)this.js_get(n);
    }
    
    @Override
    public int getBytesPerElement() {
        return 4;
    }
    
    @Override
    public String getClassName() {
        return "Uint32Array";
    }
    
    @Override
    protected Object js_get(final int n) {
        if (this.checkIndex(n)) {
            return Undefined.instance;
        }
        return ByteIo.readUint32(this.arrayBuffer.buffer, n * 4 + this.offset, false);
    }
    
    @Override
    protected Object js_set(final int n, final Object o) {
        if (this.checkIndex(n)) {
            return Undefined.instance;
        }
        ByteIo.writeUint32(this.arrayBuffer.buffer, n * 4 + this.offset, Conversions.toUint32(o), false);
        return null;
    }
    
    @Override
    protected NativeTypedArrayView realThis(final Scriptable scriptable, final IdFunctionObject idFunctionObject) {
        if (!(scriptable instanceof NativeUint32Array)) {
            throw IdScriptableObject.incompatibleCallError(idFunctionObject);
        }
        return (NativeUint32Array)scriptable;
    }
    
    @Override
    public Long set(final int n, final Long n2) {
        if (this.checkIndex(n)) {
            throw new IndexOutOfBoundsException();
        }
        return (Long)this.js_set(n, n2);
    }
}
