package org.mozilla.javascript.typedarrays;

import org.mozilla.javascript.*;

public class NativeUint16Array extends NativeTypedArrayView<Integer>
{
    private static final int BYTES_PER_ELEMENT = 2;
    private static final String CLASS_NAME = "Uint16Array";
    private static final long serialVersionUID = 7700018949434240321L;
    
    public NativeUint16Array() {
    }
    
    public NativeUint16Array(final int n) {
        this(new NativeArrayBuffer(n * 2), 0, n);
    }
    
    public NativeUint16Array(final NativeArrayBuffer nativeArrayBuffer, final int n, final int n2) {
        super(nativeArrayBuffer, n, n2, n2 * 2);
    }
    
    public static void init(final Context context, final Scriptable scriptable, final boolean b) {
        new NativeUint16Array().exportAsJSClass(4, scriptable, b);
    }
    
    @Override
    protected NativeTypedArrayView construct(final NativeArrayBuffer nativeArrayBuffer, final int n, final int n2) {
        return new NativeUint16Array(nativeArrayBuffer, n, n2);
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
        return 2;
    }
    
    @Override
    public String getClassName() {
        return "Uint16Array";
    }
    
    @Override
    protected Object js_get(final int n) {
        if (this.checkIndex(n)) {
            return Undefined.instance;
        }
        return ByteIo.readUint16(this.arrayBuffer.buffer, n * 2 + this.offset, false);
    }
    
    @Override
    protected Object js_set(final int n, final Object o) {
        if (this.checkIndex(n)) {
            return Undefined.instance;
        }
        ByteIo.writeUint16(this.arrayBuffer.buffer, n * 2 + this.offset, Conversions.toUint16(o), false);
        return null;
    }
    
    @Override
    protected NativeTypedArrayView realThis(final Scriptable scriptable, final IdFunctionObject idFunctionObject) {
        if (!(scriptable instanceof NativeUint16Array)) {
            throw IdScriptableObject.incompatibleCallError(idFunctionObject);
        }
        return (NativeUint16Array)scriptable;
    }
    
    @Override
    public Integer set(final int n, final Integer n2) {
        if (this.checkIndex(n)) {
            throw new IndexOutOfBoundsException();
        }
        return (Integer)this.js_set(n, n2);
    }
}
