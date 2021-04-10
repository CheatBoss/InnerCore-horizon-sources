package org.mozilla.javascript.optimizer;

import org.mozilla.javascript.*;
import org.mozilla.javascript.ast.*;

public class ClassCompiler
{
    private CompilerEnvirons compilerEnv;
    private String mainMethodClassName;
    private Class<?> targetExtends;
    private Class<?>[] targetImplements;
    
    public ClassCompiler(final CompilerEnvirons compilerEnv) {
        if (compilerEnv == null) {
            throw new IllegalArgumentException();
        }
        this.compilerEnv = compilerEnv;
        this.mainMethodClassName = "org.mozilla.javascript.optimizer.OptRuntime";
    }
    
    public Object[] compileToClassFiles(String auxiliaryClassName, final String s, int i, final String s2) {
        final ScriptNode transformTree = new IRFactory(this.compilerEnv).transformTree(new Parser(this.compilerEnv).parse(auxiliaryClassName, s, i));
        final Class<?> targetExtends = this.getTargetExtends();
        final Class<?>[] targetImplements = this.getTargetImplements();
        if (targetImplements == null && targetExtends == null) {
            i = 1;
        }
        else {
            i = 0;
        }
        if (i != 0) {
            auxiliaryClassName = s2;
        }
        else {
            auxiliaryClassName = this.makeAuxiliaryClassName(s2, "1");
        }
        final Codegen codegen = new Codegen();
        codegen.setMainMethodClass(this.mainMethodClassName);
        final byte[] compileToClassFile = codegen.compileToClassFile(this.compilerEnv, auxiliaryClassName, transformTree, transformTree.getEncodedSource(), false);
        if (i != 0) {
            return new Object[] { auxiliaryClassName, compileToClassFile };
        }
        final int functionCount = transformTree.getFunctionCount();
        final ObjToIntMap objToIntMap = new ObjToIntMap(functionCount);
        FunctionNode functionNode;
        String name;
        for (i = 0; i != functionCount; ++i) {
            functionNode = transformTree.getFunctionNode(i);
            name = functionNode.getName();
            if (name != null && name.length() != 0) {
                objToIntMap.put(name, functionNode.getParamCount());
            }
        }
        Class<?> objectClass;
        if ((objectClass = targetExtends) == null) {
            objectClass = ScriptRuntime.ObjectClass;
        }
        return new Object[] { s2, JavaAdapter.createAdapterCode(objToIntMap, s2, objectClass, targetImplements, auxiliaryClassName), auxiliaryClassName, compileToClassFile };
    }
    
    public CompilerEnvirons getCompilerEnv() {
        return this.compilerEnv;
    }
    
    public String getMainMethodClass() {
        return this.mainMethodClassName;
    }
    
    public Class<?> getTargetExtends() {
        return this.targetExtends;
    }
    
    public Class<?>[] getTargetImplements() {
        if (this.targetImplements == null) {
            return null;
        }
        return (Class<?>[])this.targetImplements.clone();
    }
    
    protected String makeAuxiliaryClassName(final String s, final String s2) {
        final StringBuilder sb = new StringBuilder();
        sb.append(s);
        sb.append(s2);
        return sb.toString();
    }
    
    public void setMainMethodClass(final String mainMethodClassName) {
        this.mainMethodClassName = mainMethodClassName;
    }
    
    public void setTargetExtends(final Class<?> targetExtends) {
        this.targetExtends = targetExtends;
    }
    
    public void setTargetImplements(final Class<?>[] array) {
        Class<?>[] targetImplements;
        if (array == null) {
            targetImplements = null;
        }
        else {
            targetImplements = (Class<?>[])array.clone();
        }
        this.targetImplements = targetImplements;
    }
}
