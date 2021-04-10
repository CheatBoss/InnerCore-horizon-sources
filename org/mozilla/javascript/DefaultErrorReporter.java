package org.mozilla.javascript;

class DefaultErrorReporter implements ErrorReporter
{
    static final DefaultErrorReporter instance;
    private ErrorReporter chainedReporter;
    private boolean forEval;
    
    static {
        instance = new DefaultErrorReporter();
    }
    
    private DefaultErrorReporter() {
    }
    
    static ErrorReporter forEval(final ErrorReporter chainedReporter) {
        final DefaultErrorReporter defaultErrorReporter = new DefaultErrorReporter();
        defaultErrorReporter.forEval = true;
        defaultErrorReporter.chainedReporter = chainedReporter;
        return defaultErrorReporter;
    }
    
    @Override
    public void error(final String s, final String s2, final int n, final String s3, final int n2) {
        if (this.forEval) {
            String s4 = "SyntaxError";
            String substring = s;
            if (s.startsWith("TypeError: ")) {
                s4 = "TypeError";
                substring = s.substring("TypeError: ".length());
            }
            throw ScriptRuntime.constructError(s4, substring, s2, n, s3, n2);
        }
        if (this.chainedReporter != null) {
            this.chainedReporter.error(s, s2, n, s3, n2);
            return;
        }
        throw this.runtimeError(s, s2, n, s3, n2);
    }
    
    @Override
    public EvaluatorException runtimeError(final String s, final String s2, final int n, final String s3, final int n2) {
        if (this.chainedReporter != null) {
            return this.chainedReporter.runtimeError(s, s2, n, s3, n2);
        }
        return new EvaluatorException(s, s2, n, s3, n2);
    }
    
    @Override
    public void warning(final String s, final String s2, final int n, final String s3, final int n2) {
        if (this.chainedReporter != null) {
            this.chainedReporter.warning(s, s2, n, s3, n2);
        }
    }
}
