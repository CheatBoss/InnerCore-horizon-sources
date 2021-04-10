package org.mozilla.javascript.tools.shell;

import org.mozilla.javascript.*;

public abstract class SecurityProxy extends SecurityController
{
    protected abstract void callProcessFileSecure(final Context p0, final Scriptable p1, final String p2);
}
