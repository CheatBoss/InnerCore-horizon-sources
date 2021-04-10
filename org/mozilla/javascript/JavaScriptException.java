package org.mozilla.javascript;

public class JavaScriptException extends RhinoException
{
    static final long serialVersionUID = -7666130513694669293L;
    private Object value;
    
    @Deprecated
    public JavaScriptException(final Object o) {
        this(o, "", 0);
    }
    
    public JavaScriptException(final Object value, final String s, final int n) {
        this.recordErrorOrigin(s, n, null, 0);
        this.value = value;
        if (value instanceof NativeError && Context.getContext().hasFeature(10)) {
            final NativeError nativeError = (NativeError)value;
            if (!nativeError.has("fileName", nativeError)) {
                nativeError.put("fileName", nativeError, s);
            }
            if (!nativeError.has("lineNumber", nativeError)) {
                nativeError.put("lineNumber", nativeError, n);
            }
            nativeError.setStackProvider(this);
        }
    }
    
    @Override
    public String details() {
        if (this.value == null) {
            return "null";
        }
        if (this.value instanceof NativeError) {
            return this.value.toString();
        }
        try {
            return ScriptRuntime.toString(this.value);
        }
        catch (RuntimeException ex) {
            if (this.value instanceof Scriptable) {
                return ScriptRuntime.defaultObjectToString((Scriptable)this.value);
            }
            return this.value.toString();
        }
    }
    
    @Deprecated
    public int getLineNumber() {
        return this.lineNumber();
    }
    
    @Deprecated
    public String getSourceName() {
        return this.sourceName();
    }
    
    public Object getValue() {
        return this.value;
    }
}
