package org.mozilla.javascript.commonjs.module;

import java.net.*;
import org.mozilla.javascript.*;

public interface ModuleScriptProvider
{
    ModuleScript getModuleScript(final Context p0, final String p1, final URI p2, final URI p3, final Scriptable p4) throws Exception;
}
