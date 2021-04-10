package org.mozilla.javascript.typedarrays;

import org.mozilla.javascript.*;

public abstract class NativeArrayBufferView extends IdScriptableObject
{
    private static final int Id_buffer = 1;
    private static final int Id_byteLength = 3;
    private static final int Id_byteOffset = 2;
    private static final int MAX_INSTANCE_ID = 3;
    private static final long serialVersionUID = 6884475582973958419L;
    protected final NativeArrayBuffer arrayBuffer;
    protected final int byteLength;
    protected final int offset;
    
    public NativeArrayBufferView() {
        this.arrayBuffer = NativeArrayBuffer.EMPTY_BUFFER;
        this.offset = 0;
        this.byteLength = 0;
    }
    
    protected NativeArrayBufferView(final NativeArrayBuffer arrayBuffer, final int offset, final int byteLength) {
        this.offset = offset;
        this.byteLength = byteLength;
        this.arrayBuffer = arrayBuffer;
    }
    
    protected static boolean isArg(final Object[] array, final int n) {
        return array.length > n && !Undefined.instance.equals(array[n]);
    }
    
    @Override
    protected int findInstanceIdInfo(final String s) {
        final boolean b = false;
        final String s2 = null;
        final int length = s.length();
        String s3;
        int n;
        if (length == 6) {
            s3 = "buffer";
            n = 1;
        }
        else {
            n = (b ? 1 : 0);
            s3 = s2;
            if (length == 10) {
                final char char1 = s.charAt(4);
                if (char1 == 'L') {
                    s3 = "byteLength";
                    n = 3;
                }
                else {
                    n = (b ? 1 : 0);
                    s3 = s2;
                    if (char1 == 'O') {
                        s3 = "byteOffset";
                        n = 2;
                    }
                }
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
        if (n2 == 0) {
            return super.findInstanceIdInfo(s);
        }
        return IdScriptableObject.instanceIdInfo(5, n2);
    }
    
    public NativeArrayBuffer getBuffer() {
        return this.arrayBuffer;
    }
    
    public int getByteLength() {
        return this.byteLength;
    }
    
    public int getByteOffset() {
        return this.offset;
    }
    
    @Override
    protected String getInstanceIdName(final int n) {
        switch (n) {
            default: {
                return super.getInstanceIdName(n);
            }
            case 3: {
                return "byteLength";
            }
            case 2: {
                return "byteOffset";
            }
            case 1: {
                return "buffer";
            }
        }
    }
    
    @Override
    protected Object getInstanceIdValue(final int n) {
        switch (n) {
            default: {
                return super.getInstanceIdValue(n);
            }
            case 3: {
                return ScriptRuntime.wrapInt(this.byteLength);
            }
            case 2: {
                return ScriptRuntime.wrapInt(this.offset);
            }
            case 1: {
                return this.arrayBuffer;
            }
        }
    }
    
    @Override
    protected int getMaxInstanceId() {
        return 3;
    }
}
