package org.mozilla.javascript;

import org.mozilla.javascript.debug.*;

final class InterpretedFunction extends NativeFunction implements Script
{
    static final long serialVersionUID = 541475680333911468L;
    InterpreterData idata;
    SecurityController securityController;
    Object securityDomain;
    
    private InterpretedFunction(final InterpretedFunction interpretedFunction, final int n) {
        this.idata = interpretedFunction.idata.itsNestedFunctions[n];
        this.securityController = interpretedFunction.securityController;
        this.securityDomain = interpretedFunction.securityDomain;
    }
    
    private InterpretedFunction(final InterpreterData idata, final Object o) {
        this.idata = idata;
        final SecurityController securityController = Context.getContext().getSecurityController();
        Object dynamicSecurityDomain;
        if (securityController != null) {
            dynamicSecurityDomain = securityController.getDynamicSecurityDomain(o);
        }
        else {
            if (o != null) {
                throw new IllegalArgumentException();
            }
            dynamicSecurityDomain = null;
        }
        this.securityController = securityController;
        this.securityDomain = dynamicSecurityDomain;
    }
    
    static InterpretedFunction createFunction(final Context context, final Scriptable scriptable, InterpretedFunction interpretedFunction, final int n) {
        interpretedFunction = new InterpretedFunction(interpretedFunction, n);
        interpretedFunction.initScriptFunction(context, scriptable);
        return interpretedFunction;
    }
    
    static InterpretedFunction createFunction(final Context context, final Scriptable scriptable, final InterpreterData interpreterData, final Object o) {
        final InterpretedFunction interpretedFunction = new InterpretedFunction(interpreterData, o);
        interpretedFunction.initScriptFunction(context, scriptable);
        return interpretedFunction;
    }
    
    static InterpretedFunction createScript(final InterpreterData interpreterData, final Object o) {
        return new InterpretedFunction(interpreterData, o);
    }
    
    @Override
    public Object call(final Context context, final Scriptable scriptable, final Scriptable scriptable2, final Object[] array) {
        if (!ScriptRuntime.hasTopCall(context)) {
            return ScriptRuntime.doTopCall(this, context, scriptable, scriptable2, array);
        }
        return Interpreter.interpret(this, context, scriptable, scriptable2, array);
    }
    
    @Override
    public Object exec(final Context context, final Scriptable scriptable) {
        if (!this.isScript()) {
            throw new IllegalStateException();
        }
        if (!ScriptRuntime.hasTopCall(context)) {
            return ScriptRuntime.doTopCall(this, context, scriptable, scriptable, ScriptRuntime.emptyArgs);
        }
        return Interpreter.interpret(this, context, scriptable, scriptable, ScriptRuntime.emptyArgs);
    }
    
    @Override
    public DebuggableScript getDebuggableView() {
        return this.idata;
    }
    
    @Override
    public String getEncodedSource() {
        return Interpreter.getEncodedSource(this.idata);
    }
    
    @Override
    public String getFunctionName() {
        if (this.idata.itsName == null) {
            return "";
        }
        return this.idata.itsName;
    }
    
    @Override
    protected int getLanguageVersion() {
        return this.idata.languageVersion;
    }
    
    @Override
    protected int getParamAndVarCount() {
        return this.idata.argNames.length;
    }
    
    @Override
    protected int getParamCount() {
        return this.idata.argCount;
    }
    
    @Override
    protected boolean getParamOrVarConst(final int n) {
        return this.idata.argIsConst[n];
    }
    
    @Override
    protected String getParamOrVarName(final int n) {
        return this.idata.argNames[n];
    }
    
    public boolean isScript() {
        return this.idata.itsFunctionType == 0;
    }
    
    @Override
    public Object resumeGenerator(final Context context, final Scriptable scriptable, final int n, final Object o, final Object o2) {
        return Interpreter.resumeGenerator(context, scriptable, n, o, o2);
    }
}
