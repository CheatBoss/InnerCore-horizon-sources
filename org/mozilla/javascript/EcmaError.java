package org.mozilla.javascript;

public class EcmaError extends RhinoException
{
    static final long serialVersionUID = -6261226256957286699L;
    private String errorMessage;
    private String errorName;
    
    EcmaError(final String errorName, final String errorMessage, final String s, final int n, final String s2, final int n2) {
        this.recordErrorOrigin(s, n, s2, n2);
        this.errorName = errorName;
        this.errorMessage = errorMessage;
    }
    
    @Deprecated
    public EcmaError(final Scriptable scriptable, final String s, final int n, final int n2, final String s2) {
        this("InternalError", ScriptRuntime.toString(scriptable), s, n, s2, n2);
    }
    
    @Override
    public String details() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.errorName);
        sb.append(": ");
        sb.append(this.errorMessage);
        return sb.toString();
    }
    
    @Deprecated
    public int getColumnNumber() {
        return this.columnNumber();
    }
    
    public String getErrorMessage() {
        return this.errorMessage;
    }
    
    @Deprecated
    public Scriptable getErrorObject() {
        return null;
    }
    
    @Deprecated
    public int getLineNumber() {
        return this.lineNumber();
    }
    
    @Deprecated
    public String getLineSource() {
        return this.lineSource();
    }
    
    public String getName() {
        return this.errorName;
    }
    
    @Deprecated
    public String getSourceName() {
        return this.sourceName();
    }
}
