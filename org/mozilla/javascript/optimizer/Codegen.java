package org.mozilla.javascript.optimizer;

import org.mozilla.classfile.*;
import org.mozilla.javascript.ast.*;
import java.util.*;
import org.mozilla.javascript.*;

public class Codegen implements Evaluator
{
    static final String DEFAULT_MAIN_METHOD_CLASS = "org.mozilla.javascript.optimizer.OptRuntime";
    static final String FUNCTION_CONSTRUCTOR_SIGNATURE = "(Lorg/mozilla/javascript/Scriptable;Lorg/mozilla/javascript/Context;I)V";
    static final String FUNCTION_INIT_SIGNATURE = "(Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;)V";
    static final String ID_FIELD_NAME = "_id";
    static final String REGEXP_INIT_METHOD_NAME = "_reInit";
    static final String REGEXP_INIT_METHOD_SIGNATURE = "(Lorg/mozilla/javascript/Context;)V";
    private static final String SUPER_CLASS_NAME = "org.mozilla.javascript.NativeFunction";
    private static final Object globalLock;
    private static int globalSerialClassCounter;
    private CompilerEnvirons compilerEnv;
    private ObjArray directCallTargets;
    private double[] itsConstantList;
    private int itsConstantListSize;
    String mainClassName;
    String mainClassSignature;
    private String mainMethodClass;
    private ObjToIntMap scriptOrFnIndexes;
    ScriptNode[] scriptOrFnNodes;
    
    static {
        globalLock = new Object();
    }
    
    public Codegen() {
        this.mainMethodClass = "org.mozilla.javascript.optimizer.OptRuntime";
    }
    
    private static void addDoubleWrap(final ClassFileWriter classFileWriter) {
        classFileWriter.addInvoke(184, "org/mozilla/javascript/optimizer/OptRuntime", "wrapDouble", "(D)Ljava/lang/Double;");
    }
    
    static RuntimeException badTree() {
        throw new RuntimeException("Bad tree in codegen");
    }
    
    private static void collectScriptNodes_r(final ScriptNode scriptNode, final ObjArray objArray) {
        objArray.add(scriptNode);
        for (int functionCount = scriptNode.getFunctionCount(), i = 0; i != functionCount; ++i) {
            collectScriptNodes_r(scriptNode.getFunctionNode(i), objArray);
        }
    }
    
    private Class<?> defineClass(Object defineClass, Object o) {
        final Object[] array = (Object)defineClass;
        defineClass = (IllegalArgumentException)array[0];
        final byte[] array2 = (byte[])array[1];
        final GeneratedClassLoader loader = SecurityController.createLoader(this.getClass().getClassLoader(), o);
        try {
            defineClass = (IllegalArgumentException)loader.defineClass((String)defineClass, array2);
            loader.linkClass((Class<?>)defineClass);
            return (Class<?>)defineClass;
        }
        catch (IllegalArgumentException defineClass) {}
        catch (SecurityException ex) {}
        o = new StringBuilder();
        ((StringBuilder)o).append("Malformed optimizer package ");
        ((StringBuilder)o).append(defineClass);
        throw new RuntimeException(((StringBuilder)o).toString());
    }
    
    private void emitConstantDudeInitializers(final ClassFileWriter classFileWriter) {
        final int itsConstantListSize = this.itsConstantListSize;
        if (itsConstantListSize == 0) {
            return;
        }
        classFileWriter.startMethod("<clinit>", "()V", (short)24);
        final double[] itsConstantList = this.itsConstantList;
        for (int i = 0; i != itsConstantListSize; ++i) {
            final double n = itsConstantList[i];
            final StringBuilder sb = new StringBuilder();
            sb.append("_k");
            sb.append(i);
            final String string = sb.toString();
            final String staticConstantWrapperType = getStaticConstantWrapperType(n);
            classFileWriter.addField(string, staticConstantWrapperType, (short)10);
            final int n2 = (int)n;
            if (n2 == n) {
                classFileWriter.addPush(n2);
                classFileWriter.addInvoke(184, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;");
            }
            else {
                classFileWriter.addPush(n);
                addDoubleWrap(classFileWriter);
            }
            classFileWriter.add(179, this.mainClassName, string, staticConstantWrapperType);
        }
        classFileWriter.add(177);
        classFileWriter.stopMethod((short)0);
    }
    
    private void emitDirectConstructor(final ClassFileWriter classFileWriter, final OptFunctionNode optFunctionNode) {
        classFileWriter.startMethod(this.getDirectCtorName(optFunctionNode.fnode), this.getBodyMethodSignature(optFunctionNode.fnode), (short)10);
        final int paramCount = optFunctionNode.fnode.getParamCount();
        final int n = paramCount * 3 + 4 + 1;
        int i = 0;
        classFileWriter.addALoad(0);
        classFileWriter.addALoad(1);
        classFileWriter.addALoad(2);
        classFileWriter.addInvoke(182, "org/mozilla/javascript/BaseFunction", "createObject", "(Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;)Lorg/mozilla/javascript/Scriptable;");
        classFileWriter.addAStore(n);
        classFileWriter.addALoad(0);
        classFileWriter.addALoad(1);
        classFileWriter.addALoad(2);
        classFileWriter.addALoad(n);
        while (i < paramCount) {
            classFileWriter.addALoad(i * 3 + 4);
            classFileWriter.addDLoad(i * 3 + 5);
            ++i;
        }
        classFileWriter.addALoad(paramCount * 3 + 4);
        classFileWriter.addInvoke(184, this.mainClassName, this.getBodyMethodName(optFunctionNode.fnode), this.getBodyMethodSignature(optFunctionNode.fnode));
        final int acquireLabel = classFileWriter.acquireLabel();
        classFileWriter.add(89);
        classFileWriter.add(193, "org/mozilla/javascript/Scriptable");
        classFileWriter.add(153, acquireLabel);
        classFileWriter.add(192, "org/mozilla/javascript/Scriptable");
        classFileWriter.add(176);
        classFileWriter.markLabel(acquireLabel);
        classFileWriter.addALoad(n);
        classFileWriter.add(176);
        classFileWriter.stopMethod((short)(n + 1));
    }
    
    private void emitRegExpInit(final ClassFileWriter classFileWriter) {
        int n = 0;
        for (int i = 0; i != this.scriptOrFnNodes.length; ++i) {
            n += this.scriptOrFnNodes[i].getRegexpCount();
        }
        if (n == 0) {
            return;
        }
        classFileWriter.startMethod("_reInit", "(Lorg/mozilla/javascript/Context;)V", (short)10);
        classFileWriter.addField("_reInitDone", "Z", (short)74);
        classFileWriter.add(178, this.mainClassName, "_reInitDone", "Z");
        final int acquireLabel = classFileWriter.acquireLabel();
        classFileWriter.add(153, acquireLabel);
        classFileWriter.add(177);
        classFileWriter.markLabel(acquireLabel);
        classFileWriter.addALoad(0);
        classFileWriter.addInvoke(184, "org/mozilla/javascript/ScriptRuntime", "checkRegExpProxy", "(Lorg/mozilla/javascript/Context;)Lorg/mozilla/javascript/RegExpProxy;");
        classFileWriter.addAStore(1);
        for (int j = 0; j != this.scriptOrFnNodes.length; ++j) {
            final ScriptNode scriptNode = this.scriptOrFnNodes[j];
            for (int regexpCount = scriptNode.getRegexpCount(), k = 0; k != regexpCount; ++k) {
                final String compiledRegexpName = this.getCompiledRegexpName(scriptNode, k);
                final String regexpString = scriptNode.getRegexpString(k);
                final String regexpFlags = scriptNode.getRegexpFlags(k);
                classFileWriter.addField(compiledRegexpName, "Ljava/lang/Object;", (short)10);
                classFileWriter.addALoad(1);
                classFileWriter.addALoad(0);
                classFileWriter.addPush(regexpString);
                if (regexpFlags == null) {
                    classFileWriter.add(1);
                }
                else {
                    classFileWriter.addPush(regexpFlags);
                }
                classFileWriter.addInvoke(185, "org/mozilla/javascript/RegExpProxy", "compileRegExp", "(Lorg/mozilla/javascript/Context;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/Object;");
                classFileWriter.add(179, this.mainClassName, compiledRegexpName, "Ljava/lang/Object;");
            }
        }
        classFileWriter.addPush(1);
        classFileWriter.add(179, this.mainClassName, "_reInitDone", "Z");
        classFileWriter.add(177);
        classFileWriter.stopMethod((short)2);
    }
    
    private void generateCallMethod(final ClassFileWriter classFileWriter) {
        classFileWriter.startMethod("call", "(Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;Lorg/mozilla/javascript/Scriptable;[Ljava/lang/Object;)Ljava/lang/Object;", (short)17);
        final int acquireLabel = classFileWriter.acquireLabel();
        classFileWriter.addALoad(1);
        classFileWriter.addInvoke(184, "org/mozilla/javascript/ScriptRuntime", "hasTopCall", "(Lorg/mozilla/javascript/Context;)Z");
        classFileWriter.add(154, acquireLabel);
        classFileWriter.addALoad(0);
        classFileWriter.addALoad(1);
        classFileWriter.addALoad(2);
        classFileWriter.addALoad(3);
        classFileWriter.addALoad(4);
        classFileWriter.addInvoke(184, "org/mozilla/javascript/ScriptRuntime", "doTopCall", "(Lorg/mozilla/javascript/Callable;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;Lorg/mozilla/javascript/Scriptable;[Ljava/lang/Object;)Ljava/lang/Object;");
        classFileWriter.add(176);
        classFileWriter.markLabel(acquireLabel);
        classFileWriter.addALoad(0);
        classFileWriter.addALoad(1);
        classFileWriter.addALoad(2);
        classFileWriter.addALoad(3);
        classFileWriter.addALoad(4);
        final int length = this.scriptOrFnNodes.length;
        final boolean b = 2 <= length;
        int addTableSwitch = 0;
        int n = 0;
        if (b) {
            classFileWriter.addLoadThis();
            classFileWriter.add(180, classFileWriter.getClassName(), "_id", "I");
            addTableSwitch = classFileWriter.addTableSwitch(1, length - 1);
        }
        int stackTop;
        for (int i = 0; i != length; ++i, n = stackTop) {
            final ScriptNode scriptNode = this.scriptOrFnNodes[i];
            stackTop = n;
            if (b) {
                if (i == 0) {
                    classFileWriter.markTableSwitchDefault(addTableSwitch);
                    stackTop = classFileWriter.getStackTop();
                }
                else {
                    classFileWriter.markTableSwitchCase(addTableSwitch, i - 1, n);
                    stackTop = n;
                }
            }
            if (scriptNode.getType() == 109) {
                final OptFunctionNode value = OptFunctionNode.get(scriptNode);
                if (value.isTargetOfDirectCall()) {
                    final int paramCount = value.fnode.getParamCount();
                    if (paramCount != 0) {
                        for (int j = 0; j != paramCount; ++j) {
                            classFileWriter.add(190);
                            classFileWriter.addPush(j);
                            final int acquireLabel2 = classFileWriter.acquireLabel();
                            final int acquireLabel3 = classFileWriter.acquireLabel();
                            classFileWriter.add(164, acquireLabel2);
                            classFileWriter.addALoad(4);
                            classFileWriter.addPush(j);
                            classFileWriter.add(50);
                            classFileWriter.add(167, acquireLabel3);
                            classFileWriter.markLabel(acquireLabel2);
                            pushUndefined(classFileWriter);
                            classFileWriter.markLabel(acquireLabel3);
                            classFileWriter.adjustStackTop(-1);
                            classFileWriter.addPush(0.0);
                            classFileWriter.addALoad(4);
                        }
                    }
                }
            }
            classFileWriter.addInvoke(184, this.mainClassName, this.getBodyMethodName(scriptNode), this.getBodyMethodSignature(scriptNode));
            classFileWriter.add(176);
        }
        classFileWriter.stopMethod((short)5);
    }
    
    private byte[] generateCode(String value) {
        final ScriptNode[] scriptOrFnNodes = this.scriptOrFnNodes;
        final int n = 0;
        final int type = scriptOrFnNodes[0].getType();
        final boolean b = true;
        final boolean b2 = type == 136;
        boolean b3 = b;
        if (this.scriptOrFnNodes.length <= 1) {
            b3 = (!b2 && b);
        }
        String sourceName = null;
        if (this.compilerEnv.isGenerateDebugInfo()) {
            sourceName = this.scriptOrFnNodes[0].getSourceName();
        }
        final ClassFileWriter cfw = new ClassFileWriter(this.mainClassName, "org.mozilla.javascript.NativeFunction", sourceName);
        cfw.addField("_id", "I", (short)2);
        if (b3) {
            this.generateFunctionConstructor(cfw);
        }
        if (b2) {
            cfw.addInterface("org/mozilla/javascript/Script");
            this.generateScriptCtor(cfw);
            this.generateMain(cfw);
            this.generateExecute(cfw);
        }
        this.generateCallMethod(cfw);
        this.generateResumeGenerator(cfw);
        this.generateNativeFunctionOverrides(cfw, value);
        final int length = this.scriptOrFnNodes.length;
        int i = n;
        while (i != length) {
            value = (String)this.scriptOrFnNodes[i];
            final BodyCodegen bodyCodegen = new BodyCodegen();
            bodyCodegen.cfw = cfw;
            bodyCodegen.codegen = this;
            bodyCodegen.compilerEnv = this.compilerEnv;
            bodyCodegen.scriptOrFn = (ScriptNode)value;
            bodyCodegen.scriptOrFnIndex = i;
            try {
                bodyCodegen.generateBodyCode();
                if (((Node)value).getType() == 109) {
                    value = (String)OptFunctionNode.get((ScriptNode)value);
                    this.generateFunctionInit(cfw, (OptFunctionNode)value);
                    if (((OptFunctionNode)value).isTargetOfDirectCall()) {
                        this.emitDirectConstructor(cfw, (OptFunctionNode)value);
                    }
                }
                ++i;
                continue;
            }
            catch (ClassFileWriter.ClassFileFormatException ex) {
                throw this.reportClassFileFormatException((ScriptNode)value, ex.getMessage());
            }
            break;
        }
        this.emitRegExpInit(cfw);
        this.emitConstantDudeInitializers(cfw);
        return cfw.toByteArray();
    }
    
    private void generateExecute(final ClassFileWriter classFileWriter) {
        classFileWriter.startMethod("exec", "(Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;)Ljava/lang/Object;", (short)17);
        classFileWriter.addLoadThis();
        classFileWriter.addALoad(1);
        classFileWriter.addALoad(2);
        classFileWriter.add(89);
        classFileWriter.add(1);
        classFileWriter.addInvoke(182, classFileWriter.getClassName(), "call", "(Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;Lorg/mozilla/javascript/Scriptable;[Ljava/lang/Object;)Ljava/lang/Object;");
        classFileWriter.add(176);
        classFileWriter.stopMethod((short)3);
    }
    
    private void generateFunctionConstructor(final ClassFileWriter classFileWriter) {
        classFileWriter.startMethod("<init>", "(Lorg/mozilla/javascript/Scriptable;Lorg/mozilla/javascript/Context;I)V", (short)1);
        boolean b = false;
        classFileWriter.addALoad(0);
        classFileWriter.addInvoke(183, "org.mozilla.javascript.NativeFunction", "<init>", "()V");
        classFileWriter.addLoadThis();
        classFileWriter.addILoad(3);
        classFileWriter.add(181, classFileWriter.getClassName(), "_id", "I");
        classFileWriter.addLoadThis();
        classFileWriter.addALoad(2);
        classFileWriter.addALoad(1);
        int n;
        if (this.scriptOrFnNodes[0].getType() == 136) {
            n = 1;
        }
        else {
            n = 0;
        }
        final int length = this.scriptOrFnNodes.length;
        if (n == length) {
            throw badTree();
        }
        if (2 <= length - n) {
            b = true;
        }
        int addTableSwitch = 0;
        int n2 = 0;
        if (b) {
            classFileWriter.addILoad(3);
            addTableSwitch = classFileWriter.addTableSwitch(n + 1, length - 1);
        }
        int stackTop;
        for (int i = n; i != length; ++i, n2 = stackTop) {
            stackTop = n2;
            if (b) {
                if (i == n) {
                    classFileWriter.markTableSwitchDefault(addTableSwitch);
                    stackTop = classFileWriter.getStackTop();
                }
                else {
                    classFileWriter.markTableSwitchCase(addTableSwitch, i - 1 - n, n2);
                    stackTop = n2;
                }
            }
            classFileWriter.addInvoke(183, this.mainClassName, this.getFunctionInitMethodName(OptFunctionNode.get(this.scriptOrFnNodes[i])), "(Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;)V");
            classFileWriter.add(177);
        }
        classFileWriter.stopMethod((short)4);
    }
    
    private void generateFunctionInit(final ClassFileWriter classFileWriter, final OptFunctionNode optFunctionNode) {
        classFileWriter.startMethod(this.getFunctionInitMethodName(optFunctionNode), "(Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;)V", (short)18);
        classFileWriter.addLoadThis();
        classFileWriter.addALoad(1);
        classFileWriter.addALoad(2);
        classFileWriter.addInvoke(182, "org/mozilla/javascript/NativeFunction", "initScriptFunction", "(Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;)V");
        if (optFunctionNode.fnode.getRegexpCount() != 0) {
            classFileWriter.addALoad(1);
            classFileWriter.addInvoke(184, this.mainClassName, "_reInit", "(Lorg/mozilla/javascript/Context;)V");
        }
        classFileWriter.add(177);
        classFileWriter.stopMethod((short)3);
    }
    
    private void generateMain(final ClassFileWriter classFileWriter) {
        classFileWriter.startMethod("main", "([Ljava/lang/String;)V", (short)9);
        classFileWriter.add(187, classFileWriter.getClassName());
        classFileWriter.add(89);
        classFileWriter.addInvoke(183, classFileWriter.getClassName(), "<init>", "()V");
        classFileWriter.add(42);
        classFileWriter.addInvoke(184, this.mainMethodClass, "main", "(Lorg/mozilla/javascript/Script;[Ljava/lang/String;)V");
        classFileWriter.add(177);
        classFileWriter.stopMethod((short)1);
    }
    
    private void generateNativeFunctionOverrides(final ClassFileWriter classFileWriter, final String s) {
        classFileWriter.startMethod("getLanguageVersion", "()I", (short)1);
        classFileWriter.addPush(this.compilerEnv.getLanguageVersion());
        classFileWriter.add(172);
        classFileWriter.stopMethod((short)1);
        for (int i = 0; i != 6; ++i) {
            if (i != 4 || s != null) {
                short n = 0;
                switch (i) {
                    default: {
                        throw Kit.codeBug();
                    }
                    case 5: {
                        n = 3;
                        classFileWriter.startMethod("getParamOrVarConst", "(I)Z", (short)1);
                        break;
                    }
                    case 4: {
                        n = 1;
                        classFileWriter.startMethod("getEncodedSource", "()Ljava/lang/String;", (short)1);
                        classFileWriter.addPush(s);
                        break;
                    }
                    case 3: {
                        n = 2;
                        classFileWriter.startMethod("getParamOrVarName", "(I)Ljava/lang/String;", (short)1);
                        break;
                    }
                    case 2: {
                        n = 1;
                        classFileWriter.startMethod("getParamAndVarCount", "()I", (short)1);
                        break;
                    }
                    case 1: {
                        n = 1;
                        classFileWriter.startMethod("getParamCount", "()I", (short)1);
                        break;
                    }
                    case 0: {
                        n = 1;
                        classFileWriter.startMethod("getFunctionName", "()Ljava/lang/String;", (short)1);
                        break;
                    }
                }
                final int length = this.scriptOrFnNodes.length;
                int addTableSwitch = 0;
                if (length > 1) {
                    classFileWriter.addLoadThis();
                    classFileWriter.add(180, classFileWriter.getClassName(), "_id", "I");
                    addTableSwitch = classFileWriter.addTableSwitch(1, length - 1);
                }
                int stackTop = 0;
                for (int j = 0; j != length; ++j) {
                    final ScriptNode scriptNode = this.scriptOrFnNodes[j];
                    if (j == 0) {
                        if (length > 1) {
                            classFileWriter.markTableSwitchDefault(addTableSwitch);
                            stackTop = classFileWriter.getStackTop();
                        }
                    }
                    else {
                        classFileWriter.markTableSwitchCase(addTableSwitch, j - 1, stackTop);
                    }
                    switch (i) {
                        default: {
                            throw Kit.codeBug();
                        }
                        case 5: {
                            final int paramAndVarCount = scriptNode.getParamAndVarCount();
                            final boolean[] paramAndVarConst = scriptNode.getParamAndVarConst();
                            if (paramAndVarCount == 0) {
                                classFileWriter.add(3);
                                classFileWriter.add(172);
                            }
                            else if (paramAndVarCount == 1) {
                                classFileWriter.addPush(paramAndVarConst[0]);
                                classFileWriter.add(172);
                            }
                            else {
                                classFileWriter.addILoad(1);
                                final int addTableSwitch2 = classFileWriter.addTableSwitch(1, paramAndVarCount - 1);
                                for (int k = 0; k != paramAndVarCount; ++k) {
                                    if (classFileWriter.getStackTop() != 0) {
                                        Kit.codeBug();
                                    }
                                    if (k == 0) {
                                        classFileWriter.markTableSwitchDefault(addTableSwitch2);
                                    }
                                    else {
                                        classFileWriter.markTableSwitchCase(addTableSwitch2, k - 1, 0);
                                    }
                                    classFileWriter.addPush(paramAndVarConst[k]);
                                    classFileWriter.add(172);
                                }
                            }
                            break;
                        }
                        case 4: {
                            classFileWriter.addPush(scriptNode.getEncodedSourceStart());
                            classFileWriter.addPush(scriptNode.getEncodedSourceEnd());
                            classFileWriter.addInvoke(182, "java/lang/String", "substring", "(II)Ljava/lang/String;");
                            classFileWriter.add(176);
                            break;
                        }
                        case 3: {
                            final int n2 = addTableSwitch;
                            final int paramAndVarCount2 = scriptNode.getParamAndVarCount();
                            if (paramAndVarCount2 == 0) {
                                classFileWriter.add(1);
                                classFileWriter.add(176);
                            }
                            else if (paramAndVarCount2 == 1) {
                                classFileWriter.addPush(scriptNode.getParamOrVarName(0));
                                classFileWriter.add(176);
                            }
                            else {
                                classFileWriter.addILoad(1);
                                final int addTableSwitch3 = classFileWriter.addTableSwitch(1, paramAndVarCount2 - 1);
                                for (int l = 0; l != paramAndVarCount2; ++l) {
                                    if (classFileWriter.getStackTop() != 0) {
                                        Kit.codeBug();
                                    }
                                    final String paramOrVarName = scriptNode.getParamOrVarName(l);
                                    if (l == 0) {
                                        classFileWriter.markTableSwitchDefault(addTableSwitch3);
                                    }
                                    else {
                                        classFileWriter.markTableSwitchCase(addTableSwitch3, l - 1, 0);
                                    }
                                    classFileWriter.addPush(paramOrVarName);
                                    classFileWriter.add(176);
                                }
                            }
                            addTableSwitch = n2;
                            break;
                        }
                        case 2: {
                            classFileWriter.addPush(scriptNode.getParamAndVarCount());
                            classFileWriter.add(172);
                            break;
                        }
                        case 1: {
                            classFileWriter.addPush(scriptNode.getParamCount());
                            classFileWriter.add(172);
                            break;
                        }
                        case 0: {
                            if (scriptNode.getType() == 136) {
                                classFileWriter.addPush("");
                            }
                            else {
                                classFileWriter.addPush(((FunctionNode)scriptNode).getName());
                            }
                            classFileWriter.add(176);
                            break;
                        }
                    }
                }
                classFileWriter.stopMethod(n);
            }
        }
    }
    
    private void generateResumeGenerator(final ClassFileWriter classFileWriter) {
        final int n = 0;
        boolean b = false;
        for (int i = 0; i < this.scriptOrFnNodes.length; ++i) {
            if (isGenerator(this.scriptOrFnNodes[i])) {
                b = true;
            }
        }
        if (!b) {
            return;
        }
        classFileWriter.startMethod("resumeGenerator", "(Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;ILjava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", (short)17);
        classFileWriter.addALoad(0);
        classFileWriter.addALoad(1);
        classFileWriter.addALoad(2);
        classFileWriter.addALoad(4);
        classFileWriter.addALoad(5);
        classFileWriter.addILoad(3);
        classFileWriter.addLoadThis();
        classFileWriter.add(180, classFileWriter.getClassName(), "_id", "I");
        final int addTableSwitch = classFileWriter.addTableSwitch(0, this.scriptOrFnNodes.length - 1);
        classFileWriter.markTableSwitchDefault(addTableSwitch);
        final int acquireLabel = classFileWriter.acquireLabel();
        for (int j = n; j < this.scriptOrFnNodes.length; ++j) {
            final ScriptNode scriptNode = this.scriptOrFnNodes[j];
            classFileWriter.markTableSwitchCase(addTableSwitch, j, 6);
            if (isGenerator(scriptNode)) {
                final StringBuilder sb = new StringBuilder();
                sb.append("(");
                sb.append(this.mainClassSignature);
                sb.append("Lorg/mozilla/javascript/Context;");
                sb.append("Lorg/mozilla/javascript/Scriptable;");
                sb.append("Ljava/lang/Object;");
                sb.append("Ljava/lang/Object;I)Ljava/lang/Object;");
                final String string = sb.toString();
                final String mainClassName = this.mainClassName;
                final StringBuilder sb2 = new StringBuilder();
                sb2.append(this.getBodyMethodName(scriptNode));
                sb2.append("_gen");
                classFileWriter.addInvoke(184, mainClassName, sb2.toString(), string);
                classFileWriter.add(176);
            }
            else {
                classFileWriter.add(167, acquireLabel);
            }
        }
        classFileWriter.markLabel(acquireLabel);
        pushUndefined(classFileWriter);
        classFileWriter.add(176);
        classFileWriter.stopMethod((short)6);
    }
    
    private void generateScriptCtor(final ClassFileWriter classFileWriter) {
        classFileWriter.startMethod("<init>", "()V", (short)1);
        classFileWriter.addLoadThis();
        classFileWriter.addInvoke(183, "org.mozilla.javascript.NativeFunction", "<init>", "()V");
        classFileWriter.addLoadThis();
        classFileWriter.addPush(0);
        classFileWriter.add(181, classFileWriter.getClassName(), "_id", "I");
        classFileWriter.add(177);
        classFileWriter.stopMethod((short)1);
    }
    
    private static String getStaticConstantWrapperType(final double n) {
        if ((int)n == n) {
            return "Ljava/lang/Integer;";
        }
        return "Ljava/lang/Double;";
    }
    
    private static void initOptFunctions_r(final ScriptNode scriptNode) {
        for (int i = 0; i != scriptNode.getFunctionCount(); ++i) {
            final FunctionNode functionNode = scriptNode.getFunctionNode(i);
            new OptFunctionNode(functionNode);
            initOptFunctions_r(functionNode);
        }
    }
    
    private void initScriptNodesData(final ScriptNode scriptNode) {
        final ObjArray objArray = new ObjArray();
        collectScriptNodes_r(scriptNode, objArray);
        final int size = objArray.size();
        objArray.toArray(this.scriptOrFnNodes = new ScriptNode[size]);
        this.scriptOrFnIndexes = new ObjToIntMap(size);
        for (int i = 0; i != size; ++i) {
            this.scriptOrFnIndexes.put(this.scriptOrFnNodes[i], i);
        }
    }
    
    static boolean isGenerator(final ScriptNode scriptNode) {
        return scriptNode.getType() == 109 && ((FunctionNode)scriptNode).isGenerator();
    }
    
    static void pushUndefined(final ClassFileWriter classFileWriter) {
        classFileWriter.add(178, "org/mozilla/javascript/Undefined", "instance", "Ljava/lang/Object;");
    }
    
    private RuntimeException reportClassFileFormatException(final ScriptNode scriptNode, String s) {
        if (scriptNode instanceof FunctionNode) {
            s = ScriptRuntime.getMessage2("msg.while.compiling.fn", ((FunctionNode)scriptNode).getFunctionName(), s);
        }
        else {
            s = ScriptRuntime.getMessage1("msg.while.compiling.script", s);
        }
        return Context.reportRuntimeError(s, scriptNode.getSourceName(), scriptNode.getLineno(), null, 0);
    }
    
    private void transform(final ScriptNode scriptNode) {
        initOptFunctions_r(scriptNode);
        final int optimizationLevel = this.compilerEnv.getOptimizationLevel();
        final Map<String, OptFunctionNode> map = null;
        Map<String, OptFunctionNode> map2 = null;
        Map<String, OptFunctionNode> map3 = map;
        if (optimizationLevel > 0) {
            map3 = map;
            if (scriptNode.getType() == 136) {
                final int functionCount = scriptNode.getFunctionCount();
                int n = 0;
                while (true) {
                    map3 = map2;
                    if (n == functionCount) {
                        break;
                    }
                    final OptFunctionNode value = OptFunctionNode.get(scriptNode, n);
                    Map<String, OptFunctionNode> map4 = map2;
                    if (value.fnode.getFunctionType() == 1) {
                        final String name = value.fnode.getName();
                        map4 = map2;
                        if (name.length() != 0) {
                            if ((map4 = map2) == null) {
                                map4 = new HashMap<String, OptFunctionNode>();
                            }
                            map4.put(name, value);
                        }
                    }
                    ++n;
                    map2 = map4;
                }
            }
        }
        if (map3 != null) {
            this.directCallTargets = new ObjArray();
        }
        new OptTransformer(map3, this.directCallTargets).transform(scriptNode);
        if (optimizationLevel > 0) {
            new Optimizer().optimize(scriptNode);
        }
    }
    
    @Override
    public void captureStackInfo(final RhinoException ex) {
        throw new UnsupportedOperationException();
    }
    
    String cleanName(final ScriptNode scriptNode) {
        if (scriptNode instanceof FunctionNode) {
            final Name functionName = ((FunctionNode)scriptNode).getFunctionName();
            String identifier;
            if (functionName == null) {
                identifier = "anonymous";
            }
            else {
                identifier = functionName.getIdentifier();
            }
            return identifier;
        }
        return "script";
    }
    
    @Override
    public Object compile(final CompilerEnvirons p0, final ScriptNode p1, final String p2, final boolean p3) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: astore          6
        //     5: aload           6
        //     7: monitorenter   
        //     8: getstatic       org/mozilla/javascript/optimizer/Codegen.globalSerialClassCounter:I
        //    11: iconst_1       
        //    12: iadd           
        //    13: istore          5
        //    15: iload           5
        //    17: putstatic       org/mozilla/javascript/optimizer/Codegen.globalSerialClassCounter:I
        //    20: aload           6
        //    22: monitorexit    
        //    23: ldc_w           "c"
        //    26: astore          6
        //    28: aload_2        
        //    29: invokevirtual   org/mozilla/javascript/ast/ScriptNode.getSourceName:()Ljava/lang/String;
        //    32: invokevirtual   java/lang/String.length:()I
        //    35: ifle            102
        //    38: aload_2        
        //    39: invokevirtual   org/mozilla/javascript/ast/ScriptNode.getSourceName:()Ljava/lang/String;
        //    42: ldc_w           "\\W"
        //    45: ldc_w           "_"
        //    48: invokevirtual   java/lang/String.replaceAll:(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
        //    51: astore          7
        //    53: aload           7
        //    55: astore          6
        //    57: aload           7
        //    59: iconst_0       
        //    60: invokevirtual   java/lang/String.charAt:(I)C
        //    63: invokestatic    java/lang/Character.isJavaIdentifierStart:(C)Z
        //    66: ifne            102
        //    69: new             Ljava/lang/StringBuilder;
        //    72: dup            
        //    73: invokespecial   java/lang/StringBuilder.<init>:()V
        //    76: astore          6
        //    78: aload           6
        //    80: ldc_w           "_"
        //    83: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    86: pop            
        //    87: aload           6
        //    89: aload           7
        //    91: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    94: pop            
        //    95: aload           6
        //    97: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   100: astore          6
        //   102: new             Ljava/lang/StringBuilder;
        //   105: dup            
        //   106: invokespecial   java/lang/StringBuilder.<init>:()V
        //   109: astore          7
        //   111: aload           7
        //   113: ldc_w           "org.mozilla.javascript.gen."
        //   116: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   119: pop            
        //   120: aload           7
        //   122: aload           6
        //   124: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   127: pop            
        //   128: aload           7
        //   130: ldc_w           "_"
        //   133: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   136: pop            
        //   137: aload           7
        //   139: iload           5
        //   141: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //   144: pop            
        //   145: aload           7
        //   147: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   150: astore          6
        //   152: iconst_2       
        //   153: anewarray       Ljava/lang/Object;
        //   156: dup            
        //   157: iconst_0       
        //   158: aload           6
        //   160: aastore        
        //   161: dup            
        //   162: iconst_1       
        //   163: aload_0        
        //   164: aload_1        
        //   165: aload           6
        //   167: aload_2        
        //   168: aload_3        
        //   169: iload           4
        //   171: invokevirtual   org/mozilla/javascript/optimizer/Codegen.compileToClassFile:(Lorg/mozilla/javascript/CompilerEnvirons;Ljava/lang/String;Lorg/mozilla/javascript/ast/ScriptNode;Ljava/lang/String;Z)[B
        //   174: aastore        
        //   175: areturn        
        //   176: astore_1       
        //   177: aload           6
        //   179: monitorexit    
        //   180: aload_1        
        //   181: athrow         
        //   182: astore_1       
        //   183: goto            177
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type
        //  -----  -----  -----  -----  ----
        //  8      20     176    177    Any
        //  20     23     182    186    Any
        //  177    180    182    186    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0102:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2596)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at us.deathmarine.luyten.FileSaver.doSaveJarDecompiled(FileSaver.java:192)
        //     at us.deathmarine.luyten.FileSaver.access$300(FileSaver.java:45)
        //     at us.deathmarine.luyten.FileSaver$4.run(FileSaver.java:112)
        //     at java.lang.Thread.run(Unknown Source)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public byte[] compileToClassFile(CompilerEnvirons functionNode, final String mainClassName, final ScriptNode scriptNode, final String s, final boolean b) {
        this.compilerEnv = (CompilerEnvirons)functionNode;
        this.transform(scriptNode);
        functionNode = scriptNode;
        if (b) {
            functionNode = scriptNode.getFunctionNode(0);
        }
        this.initScriptNodesData((ScriptNode)functionNode);
        this.mainClassName = mainClassName;
        this.mainClassSignature = ClassFileWriter.classNameToSignature(mainClassName);
        try {
            return this.generateCode(s);
        }
        catch (ClassFileWriter.ClassFileFormatException ex) {
            throw this.reportClassFileFormatException((ScriptNode)functionNode, ex.getMessage());
        }
    }
    
    @Override
    public Function createFunctionObject(final Context context, final Scriptable scriptable, final Object o, final Object o2) {
        final Class<?> defineClass = this.defineClass(o, o2);
        try {
            return (NativeFunction)defineClass.getConstructors()[0].newInstance(scriptable, context, 0);
        }
        catch (Exception ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Unable to instantiate compiled class:");
            sb.append(ex.toString());
            throw new RuntimeException(sb.toString());
        }
    }
    
    @Override
    public Script createScriptObject(final Object o, final Object o2) {
        final Class<?> defineClass = this.defineClass(o, o2);
        try {
            return (Script)defineClass.newInstance();
        }
        catch (Exception ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Unable to instantiate compiled class:");
            sb.append(ex.toString());
            throw new RuntimeException(sb.toString());
        }
    }
    
    String getBodyMethodName(final ScriptNode scriptNode) {
        final StringBuilder sb = new StringBuilder();
        sb.append("_c_");
        sb.append(this.cleanName(scriptNode));
        sb.append("_");
        sb.append(this.getIndex(scriptNode));
        return sb.toString();
    }
    
    String getBodyMethodSignature(final ScriptNode scriptNode) {
        final StringBuilder sb = new StringBuilder();
        sb.append('(');
        sb.append(this.mainClassSignature);
        sb.append("Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;Lorg/mozilla/javascript/Scriptable;");
        if (scriptNode.getType() == 109) {
            final OptFunctionNode value = OptFunctionNode.get(scriptNode);
            if (value.isTargetOfDirectCall()) {
                for (int paramCount = value.fnode.getParamCount(), i = 0; i != paramCount; ++i) {
                    sb.append("Ljava/lang/Object;D");
                }
            }
        }
        sb.append("[Ljava/lang/Object;)Ljava/lang/Object;");
        return sb.toString();
    }
    
    String getCompiledRegexpName(final ScriptNode scriptNode, final int n) {
        final StringBuilder sb = new StringBuilder();
        sb.append("_re");
        sb.append(this.getIndex(scriptNode));
        sb.append("_");
        sb.append(n);
        return sb.toString();
    }
    
    String getDirectCtorName(final ScriptNode scriptNode) {
        final StringBuilder sb = new StringBuilder();
        sb.append("_n");
        sb.append(this.getIndex(scriptNode));
        return sb.toString();
    }
    
    String getFunctionInitMethodName(final OptFunctionNode optFunctionNode) {
        final StringBuilder sb = new StringBuilder();
        sb.append("_i");
        sb.append(this.getIndex(optFunctionNode.fnode));
        return sb.toString();
    }
    
    int getIndex(final ScriptNode scriptNode) {
        return this.scriptOrFnIndexes.getExisting(scriptNode);
    }
    
    @Override
    public String getPatchedStack(final RhinoException ex, final String s) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public List<String> getScriptStack(final RhinoException ex) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public String getSourcePositionFromStack(final Context context, final int[] array) {
        throw new UnsupportedOperationException();
    }
    
    void pushNumberAsObject(final ClassFileWriter classFileWriter, final double n) {
        if (n == 0.0) {
            if (1.0 / n > 0.0) {
                classFileWriter.add(178, "org/mozilla/javascript/optimizer/OptRuntime", "zeroObj", "Ljava/lang/Double;");
                return;
            }
            classFileWriter.addPush(n);
            addDoubleWrap(classFileWriter);
        }
        else {
            if (n == 1.0) {
                classFileWriter.add(178, "org/mozilla/javascript/optimizer/OptRuntime", "oneObj", "Ljava/lang/Double;");
                return;
            }
            if (n == -1.0) {
                classFileWriter.add(178, "org/mozilla/javascript/optimizer/OptRuntime", "minusOneObj", "Ljava/lang/Double;");
                return;
            }
            if (n != n) {
                classFileWriter.add(178, "org/mozilla/javascript/ScriptRuntime", "NaNobj", "Ljava/lang/Double;");
                return;
            }
            if (this.itsConstantListSize >= 2000) {
                classFileWriter.addPush(n);
                addDoubleWrap(classFileWriter);
                return;
            }
            final int itsConstantListSize = this.itsConstantListSize;
            int n2 = 0;
            int n3 = 0;
            if (itsConstantListSize == 0) {
                this.itsConstantList = new double[64];
            }
            else {
                double[] itsConstantList;
                for (itsConstantList = this.itsConstantList; n3 != itsConstantListSize && itsConstantList[n3] != n; ++n3) {}
                n2 = n3;
                if (itsConstantListSize == itsConstantList.length) {
                    final double[] itsConstantList2 = new double[itsConstantListSize * 2];
                    System.arraycopy(this.itsConstantList, 0, itsConstantList2, 0, itsConstantListSize);
                    this.itsConstantList = itsConstantList2;
                    n2 = n3;
                }
            }
            if (n2 == itsConstantListSize) {
                this.itsConstantList[itsConstantListSize] = n;
                this.itsConstantListSize = itsConstantListSize + 1;
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("_k");
            sb.append(n2);
            classFileWriter.add(178, this.mainClassName, sb.toString(), getStaticConstantWrapperType(n));
        }
    }
    
    @Override
    public void setEvalScriptFlag(final Script script) {
        throw new UnsupportedOperationException();
    }
    
    public void setMainMethodClass(final String mainMethodClass) {
        this.mainMethodClass = mainMethodClass;
    }
}
