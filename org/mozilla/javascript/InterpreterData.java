package org.mozilla.javascript;

import java.io.*;
import org.mozilla.javascript.debug.*;

final class InterpreterData implements Serializable, DebuggableScript
{
    static final int INITIAL_MAX_ICODE_LENGTH = 1024;
    static final int INITIAL_NUMBERTABLE_SIZE = 64;
    static final int INITIAL_STRINGTABLE_SIZE = 64;
    static final long serialVersionUID = 5067677351589230234L;
    int argCount;
    boolean[] argIsConst;
    String[] argNames;
    String encodedSource;
    int encodedSourceEnd;
    int encodedSourceStart;
    boolean evalScriptFlag;
    int firstLinePC;
    boolean isStrict;
    double[] itsDoubleTable;
    int[] itsExceptionTable;
    int itsFunctionType;
    byte[] itsICode;
    int itsMaxCalleeArgs;
    int itsMaxFrameArray;
    int itsMaxLocals;
    int itsMaxStack;
    int itsMaxVars;
    String itsName;
    boolean itsNeedsActivation;
    InterpreterData[] itsNestedFunctions;
    Object[] itsRegExpLiterals;
    String itsSourceFile;
    String[] itsStringTable;
    int languageVersion;
    Object[] literalIds;
    UintMap longJumps;
    InterpreterData parentData;
    boolean topLevel;
    
    InterpreterData(final int languageVersion, final String itsSourceFile, final String encodedSource, final boolean isStrict) {
        this.firstLinePC = -1;
        this.languageVersion = languageVersion;
        this.itsSourceFile = itsSourceFile;
        this.encodedSource = encodedSource;
        this.isStrict = isStrict;
        this.init();
    }
    
    InterpreterData(final InterpreterData parentData) {
        this.firstLinePC = -1;
        this.parentData = parentData;
        this.languageVersion = parentData.languageVersion;
        this.itsSourceFile = parentData.itsSourceFile;
        this.encodedSource = parentData.encodedSource;
        this.init();
    }
    
    private void init() {
        this.itsICode = new byte[1024];
        this.itsStringTable = new String[64];
    }
    
    @Override
    public DebuggableScript getFunction(final int n) {
        return this.itsNestedFunctions[n];
    }
    
    @Override
    public int getFunctionCount() {
        if (this.itsNestedFunctions == null) {
            return 0;
        }
        return this.itsNestedFunctions.length;
    }
    
    @Override
    public String getFunctionName() {
        return this.itsName;
    }
    
    @Override
    public int[] getLineNumbers() {
        return Interpreter.getLineNumbers(this);
    }
    
    @Override
    public int getParamAndVarCount() {
        return this.argNames.length;
    }
    
    @Override
    public int getParamCount() {
        return this.argCount;
    }
    
    public boolean getParamOrVarConst(final int n) {
        return this.argIsConst[n];
    }
    
    @Override
    public String getParamOrVarName(final int n) {
        return this.argNames[n];
    }
    
    @Override
    public DebuggableScript getParent() {
        return this.parentData;
    }
    
    @Override
    public String getSourceName() {
        return this.itsSourceFile;
    }
    
    @Override
    public boolean isFunction() {
        return this.itsFunctionType != 0;
    }
    
    @Override
    public boolean isGeneratedScript() {
        return ScriptRuntime.isGeneratedScript(this.itsSourceFile);
    }
    
    @Override
    public boolean isTopLevel() {
        return this.topLevel;
    }
}
