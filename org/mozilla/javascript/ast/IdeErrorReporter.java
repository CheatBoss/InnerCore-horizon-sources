package org.mozilla.javascript.ast;

import org.mozilla.javascript.*;

public interface IdeErrorReporter extends ErrorReporter
{
    void error(final String p0, final String p1, final int p2, final int p3);
    
    void warning(final String p0, final String p1, final int p2, final int p3);
}
