package org.mozilla.javascript;

public class EvaluatorException extends RhinoException
{
    static final long serialVersionUID = -8743165779676009808L;
    
    public EvaluatorException(final String s) {
        super(s);
    }
    
    public EvaluatorException(final String s, final String s2, final int n) {
        this(s, s2, n, null, 0);
    }
    
    public EvaluatorException(final String s, final String s2, final int n, final String s3, final int n2) {
        super(s);
        this.recordErrorOrigin(s2, n, s3, n2);
    }
    
    @Deprecated
    public int getColumnNumber() {
        return this.columnNumber();
    }
    
    @Deprecated
    public int getLineNumber() {
        return this.lineNumber();
    }
    
    @Deprecated
    public String getLineSource() {
        return this.lineSource();
    }
    
    @Deprecated
    public String getSourceName() {
        return this.sourceName();
    }
}
