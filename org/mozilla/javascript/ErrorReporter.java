package org.mozilla.javascript;

public interface ErrorReporter
{
    void error(final String p0, final String p1, final int p2, final String p3, final int p4);
    
    EvaluatorException runtimeError(final String p0, final String p1, final int p2, final String p3, final int p4);
    
    void warning(final String p0, final String p1, final int p2, final String p3, final int p4);
}
