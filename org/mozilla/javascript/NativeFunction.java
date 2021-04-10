package org.mozilla.javascript;

import org.mozilla.javascript.debug.*;

public abstract class NativeFunction extends BaseFunction
{
    static final long serialVersionUID = 8713897114082216401L;
    
    @Override
    final String decompile(final int n, final int n2) {
        final String encodedSource = this.getEncodedSource();
        if (encodedSource == null) {
            return super.decompile(n, n2);
        }
        final UintMap uintMap = new UintMap(1);
        uintMap.put(1, n);
        return Decompiler.decompile(encodedSource, n2, uintMap);
    }
    
    @Override
    public int getArity() {
        return this.getParamCount();
    }
    
    public DebuggableScript getDebuggableView() {
        return null;
    }
    
    public String getEncodedSource() {
        return null;
    }
    
    protected abstract int getLanguageVersion();
    
    @Override
    public int getLength() {
        final int paramCount = this.getParamCount();
        if (this.getLanguageVersion() != 120) {
            return paramCount;
        }
        final NativeCall functionActivation = ScriptRuntime.findFunctionActivation(Context.getContext(), this);
        if (functionActivation == null) {
            return paramCount;
        }
        return functionActivation.originalArgs.length;
    }
    
    protected abstract int getParamAndVarCount();
    
    protected abstract int getParamCount();
    
    protected boolean getParamOrVarConst(final int n) {
        return false;
    }
    
    protected abstract String getParamOrVarName(final int p0);
    
    public final void initScriptFunction(final Context context, final Scriptable scriptable) {
        ScriptRuntime.setFunctionProtoAndParent(this, scriptable);
    }
    
    @Deprecated
    public String jsGet_name() {
        return this.getFunctionName();
    }
    
    public Object resumeGenerator(final Context context, final Scriptable scriptable, final int n, final Object o, final Object o2) {
        throw new EvaluatorException("resumeGenerator() not implemented");
    }
}
