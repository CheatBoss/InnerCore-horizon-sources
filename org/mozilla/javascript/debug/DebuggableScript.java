package org.mozilla.javascript.debug;

public interface DebuggableScript
{
    DebuggableScript getFunction(final int p0);
    
    int getFunctionCount();
    
    String getFunctionName();
    
    int[] getLineNumbers();
    
    int getParamAndVarCount();
    
    int getParamCount();
    
    String getParamOrVarName(final int p0);
    
    DebuggableScript getParent();
    
    String getSourceName();
    
    boolean isFunction();
    
    boolean isGeneratedScript();
    
    boolean isTopLevel();
}
