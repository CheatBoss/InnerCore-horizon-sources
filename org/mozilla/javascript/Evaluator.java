package org.mozilla.javascript;

import org.mozilla.javascript.ast.*;
import java.util.*;

public interface Evaluator
{
    void captureStackInfo(final RhinoException p0);
    
    Object compile(final CompilerEnvirons p0, final ScriptNode p1, final String p2, final boolean p3);
    
    Function createFunctionObject(final Context p0, final Scriptable p1, final Object p2, final Object p3);
    
    Script createScriptObject(final Object p0, final Object p1);
    
    String getPatchedStack(final RhinoException p0, final String p1);
    
    List<String> getScriptStack(final RhinoException p0);
    
    String getSourcePositionFromStack(final Context p0, final int[] p1);
    
    void setEvalScriptFlag(final Script p0);
}
