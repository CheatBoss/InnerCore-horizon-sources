package org.mozilla.javascript.tools.debugger;

import org.mozilla.javascript.*;

public interface ScopeProvider
{
    Scriptable getScope();
}
