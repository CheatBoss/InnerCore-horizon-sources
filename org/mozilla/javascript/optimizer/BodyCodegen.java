package org.mozilla.javascript.optimizer;

import org.mozilla.classfile.*;
import org.mozilla.javascript.ast.*;
import org.mozilla.javascript.*;
import java.util.*;

class BodyCodegen
{
    private static final int ECMAERROR_EXCEPTION = 2;
    private static final int EVALUATOR_EXCEPTION = 1;
    private static final int EXCEPTION_MAX = 5;
    private static final int FINALLY_EXCEPTION = 4;
    static final int GENERATOR_START = 0;
    static final int GENERATOR_TERMINATE = -1;
    static final int GENERATOR_YIELD_START = 1;
    private static final int JAVASCRIPT_EXCEPTION = 0;
    private static final int MAX_LOCALS = 1024;
    private static final int THROWABLE_EXCEPTION = 3;
    private short argsLocal;
    ClassFileWriter cfw;
    Codegen codegen;
    CompilerEnvirons compilerEnv;
    private short contextLocal;
    private int enterAreaStartLabel;
    private int epilogueLabel;
    private ExceptionManager exceptionManager;
    private Map<Node, FinallyReturnPoint> finallys;
    private short firstFreeLocal;
    private OptFunctionNode fnCurrent;
    private short funObjLocal;
    private short generatorStateLocal;
    private int generatorSwitch;
    private boolean hasVarsInRegs;
    private boolean inDirectCallFunction;
    private boolean inLocalBlock;
    private boolean isGenerator;
    private boolean itsForcedObjectParameters;
    private int itsLineNumber;
    private short itsOneArgArray;
    private short itsZeroArgArray;
    private List<Node> literals;
    private int[] locals;
    private short localsMax;
    private int maxLocals;
    private int maxStack;
    private short operationLocal;
    private short popvLocal;
    private int savedCodeOffset;
    ScriptNode scriptOrFn;
    public int scriptOrFnIndex;
    private short thisObjLocal;
    private short[] varRegisters;
    private short variableObjectLocal;
    
    BodyCodegen() {
        this.exceptionManager = new ExceptionManager();
        this.maxLocals = 0;
        this.maxStack = 0;
    }
    
    private void addDoubleWrap() {
        this.addOptRuntimeInvoke("wrapDouble", "(D)Ljava/lang/Double;");
    }
    
    private void addGoto(final Node node, final int n) {
        this.cfw.add(n, this.getTargetLabel(node));
    }
    
    private void addGotoWithReturn(final Node node) {
        final FinallyReturnPoint finallyReturnPoint = this.finallys.get(node);
        this.cfw.addLoadConstant(finallyReturnPoint.jsrPoints.size());
        this.addGoto(node, 167);
        final int acquireLabel = this.cfw.acquireLabel();
        this.cfw.markLabel(acquireLabel);
        finallyReturnPoint.jsrPoints.add(acquireLabel);
    }
    
    private void addInstructionCount() {
        this.addInstructionCount(Math.max(this.cfw.getCurrentCodeOffset() - this.savedCodeOffset, 1));
    }
    
    private void addInstructionCount(final int n) {
        this.cfw.addALoad(this.contextLocal);
        this.cfw.addPush(n);
        this.addScriptRuntimeInvoke("addInstructionCount", "(Lorg/mozilla/javascript/Context;I)V");
    }
    
    private void addJumpedBooleanWrap(final int n, int acquireLabel) {
        this.cfw.markLabel(acquireLabel);
        acquireLabel = this.cfw.acquireLabel();
        this.cfw.add(178, "java/lang/Boolean", "FALSE", "Ljava/lang/Boolean;");
        this.cfw.add(167, acquireLabel);
        this.cfw.markLabel(n);
        this.cfw.add(178, "java/lang/Boolean", "TRUE", "Ljava/lang/Boolean;");
        this.cfw.markLabel(acquireLabel);
        this.cfw.adjustStackTop(-1);
    }
    
    private void addLoadPropertyIds(final Object[] array, final int n) {
        this.addNewObjectArray(n);
        for (int i = 0; i != n; ++i) {
            this.cfw.add(89);
            this.cfw.addPush(i);
            final Object o = array[i];
            if (o instanceof String) {
                this.cfw.addPush((String)o);
            }
            else {
                this.cfw.addPush((int)o);
                this.addScriptRuntimeInvoke("wrapInt", "(I)Ljava/lang/Integer;");
            }
            this.cfw.add(83);
        }
    }
    
    private void addLoadPropertyValues(final Node node, Node node2, final int n) {
        final boolean isGenerator = this.isGenerator;
        int i = 0;
        final int n2 = 0;
        if (isGenerator) {
            for (int j = 0; j != n; ++j) {
                final int type = node2.getType();
                if (type != 151 && type != 152 && type != 163) {
                    this.generateExpression(node2, node);
                }
                else {
                    this.generateExpression(node2.getFirstChild(), node);
                }
                node2 = node2.getNext();
            }
            this.addNewObjectArray(n);
            for (int k = n2; k != n; ++k) {
                this.cfw.add(90);
                this.cfw.add(95);
                this.cfw.addPush(n - k - 1);
                this.cfw.add(95);
                this.cfw.add(83);
            }
            return;
        }
        this.addNewObjectArray(n);
        while (i != n) {
            this.cfw.add(89);
            this.cfw.addPush(i);
            final int type2 = node2.getType();
            if (type2 != 151 && type2 != 152 && type2 != 163) {
                this.generateExpression(node2, node);
            }
            else {
                this.generateExpression(node2.getFirstChild(), node);
            }
            this.cfw.add(83);
            node2 = node2.getNext();
            ++i;
        }
    }
    
    private void addNewObjectArray(final int n) {
        if (n != 0) {
            this.cfw.addPush(n);
            this.cfw.add(189, "java/lang/Object");
            return;
        }
        if (this.itsZeroArgArray >= 0) {
            this.cfw.addALoad(this.itsZeroArgArray);
            return;
        }
        this.cfw.add(178, "org/mozilla/javascript/ScriptRuntime", "emptyArgs", "[Ljava/lang/Object;");
    }
    
    private void addObjectToDouble() {
        this.addScriptRuntimeInvoke("toNumber", "(Ljava/lang/Object;)D");
    }
    
    private void addOptRuntimeInvoke(final String s, final String s2) {
        this.cfw.addInvoke(184, "org/mozilla/javascript/optimizer/OptRuntime", s, s2);
    }
    
    private void addScriptRuntimeInvoke(final String s, final String s2) {
        this.cfw.addInvoke(184, "org.mozilla.javascript.ScriptRuntime", s, s2);
    }
    
    private void dcpLoadAsNumber(final int n) {
        this.cfw.addALoad(n);
        this.cfw.add(178, "java/lang/Void", "TYPE", "Ljava/lang/Class;");
        final int acquireLabel = this.cfw.acquireLabel();
        this.cfw.add(165, acquireLabel);
        final short stackTop = this.cfw.getStackTop();
        this.cfw.addALoad(n);
        this.addObjectToDouble();
        final int acquireLabel2 = this.cfw.acquireLabel();
        this.cfw.add(167, acquireLabel2);
        this.cfw.markLabel(acquireLabel, stackTop);
        this.cfw.addDLoad(n + 1);
        this.cfw.markLabel(acquireLabel2);
    }
    
    private void dcpLoadAsObject(final int n) {
        this.cfw.addALoad(n);
        this.cfw.add(178, "java/lang/Void", "TYPE", "Ljava/lang/Class;");
        final int acquireLabel = this.cfw.acquireLabel();
        this.cfw.add(165, acquireLabel);
        final short stackTop = this.cfw.getStackTop();
        this.cfw.addALoad(n);
        final int acquireLabel2 = this.cfw.acquireLabel();
        this.cfw.add(167, acquireLabel2);
        this.cfw.markLabel(acquireLabel, stackTop);
        this.cfw.addDLoad(n + 1);
        this.addDoubleWrap();
        this.cfw.markLabel(acquireLabel2);
    }
    
    private void decReferenceWordLocal(final short n) {
        final int[] locals = this.locals;
        --locals[n];
    }
    
    private String exceptionTypeToName(final int n) {
        if (n == 0) {
            return "org/mozilla/javascript/JavaScriptException";
        }
        if (n == 1) {
            return "org/mozilla/javascript/EvaluatorException";
        }
        if (n == 2) {
            return "org/mozilla/javascript/EcmaError";
        }
        if (n == 3) {
            return "java/lang/Throwable";
        }
        if (n == 4) {
            return null;
        }
        throw Kit.codeBug();
    }
    
    private void genSimpleCompare(final int n, final int n2, final int n3) {
        if (n2 == -1) {
            throw Codegen.badTree();
        }
        switch (n) {
            default: {
                throw Codegen.badTree();
            }
            case 17: {
                this.cfw.add(151);
                this.cfw.add(156, n2);
                break;
            }
            case 16: {
                this.cfw.add(151);
                this.cfw.add(157, n2);
                break;
            }
            case 15: {
                this.cfw.add(152);
                this.cfw.add(158, n2);
                break;
            }
            case 14: {
                this.cfw.add(152);
                this.cfw.add(155, n2);
                break;
            }
        }
        if (n3 != -1) {
            this.cfw.add(167, n3);
        }
    }
    
    private void generateActivationExit() {
        if (this.fnCurrent != null && !this.hasVarsInRegs) {
            this.cfw.addALoad(this.contextLocal);
            this.addScriptRuntimeInvoke("exitActivationFunction", "(Lorg/mozilla/javascript/Context;)V");
            return;
        }
        throw Kit.codeBug();
    }
    
    private void generateArrayLiteralFactory(final Node node, final int n) {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.codegen.getBodyMethodName(this.scriptOrFn));
        sb.append("_literal");
        sb.append(n);
        final String string = sb.toString();
        this.initBodyGeneration();
        final short firstFreeLocal = this.firstFreeLocal;
        this.firstFreeLocal = (short)(firstFreeLocal + 1);
        this.argsLocal = firstFreeLocal;
        this.localsMax = this.firstFreeLocal;
        this.cfw.startMethod(string, "(Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;Lorg/mozilla/javascript/Scriptable;[Ljava/lang/Object;)Lorg/mozilla/javascript/Scriptable;", (short)2);
        this.visitArrayLiteral(node, node.getFirstChild(), true);
        this.cfw.add(176);
        this.cfw.stopMethod((short)(this.localsMax + 1));
    }
    
    private void generateCallArgArray(final Node node, Node next, final boolean b) {
        int n = 0;
        for (Node next2 = next; next2 != null; next2 = next2.getNext()) {
            ++n;
        }
        if (n == 1 && this.itsOneArgArray >= 0) {
            this.cfw.addALoad(this.itsOneArgArray);
        }
        else {
            this.addNewObjectArray(n);
        }
        for (int i = 0; i != n; ++i) {
            if (!this.isGenerator) {
                this.cfw.add(89);
                this.cfw.addPush(i);
            }
            if (!b) {
                this.generateExpression(next, node);
            }
            else {
                final int nodeIsDirectCallParameter = this.nodeIsDirectCallParameter(next);
                if (nodeIsDirectCallParameter >= 0) {
                    this.dcpLoadAsObject(nodeIsDirectCallParameter);
                }
                else {
                    this.generateExpression(next, node);
                    if (next.getIntProp(8, -1) == 0) {
                        this.addDoubleWrap();
                    }
                }
            }
            if (this.isGenerator) {
                final short newWordLocal = this.getNewWordLocal();
                this.cfw.addAStore(newWordLocal);
                this.cfw.add(192, "[Ljava/lang/Object;");
                this.cfw.add(89);
                this.cfw.addPush(i);
                this.cfw.addALoad(newWordLocal);
                this.releaseWordLocal(newWordLocal);
            }
            this.cfw.add(83);
            next = next.getNext();
        }
    }
    
    private void generateCatchBlock(final int n, final short n2, final int n3, final int n4, final int n5) {
        int acquireLabel = n5;
        if (n5 == 0) {
            acquireLabel = this.cfw.acquireLabel();
        }
        this.cfw.markHandler(acquireLabel);
        this.cfw.addAStore(n4);
        this.cfw.addALoad(n2);
        this.cfw.addAStore(this.variableObjectLocal);
        this.exceptionTypeToName(n);
        this.cfw.add(167, n3);
    }
    
    private void generateCheckForThrowOrClose(final int n, final boolean b, final int n2) {
        final int acquireLabel = this.cfw.acquireLabel();
        final int acquireLabel2 = this.cfw.acquireLabel();
        this.cfw.markLabel(acquireLabel);
        this.cfw.addALoad(this.argsLocal);
        this.generateThrowJavaScriptException();
        this.cfw.markLabel(acquireLabel2);
        this.cfw.addALoad(this.argsLocal);
        this.cfw.add(192, "java/lang/Throwable");
        this.cfw.add(191);
        if (n != -1) {
            this.cfw.markLabel(n);
        }
        if (!b) {
            this.cfw.markTableSwitchCase(this.generatorSwitch, n2);
        }
        this.cfw.addILoad(this.operationLocal);
        this.cfw.addLoadConstant(2);
        this.cfw.add(159, acquireLabel2);
        this.cfw.addILoad(this.operationLocal);
        this.cfw.addLoadConstant(1);
        this.cfw.add(159, acquireLabel);
    }
    
    private void generateEpilogue() {
        if (this.compilerEnv.isGenerateObserverCount()) {
            this.addInstructionCount();
        }
        if (this.isGenerator) {
            final Map<Node, int[]> liveLocals = ((FunctionNode)this.scriptOrFn).getLiveLocals();
            if (liveLocals != null) {
                final List<Node> resumptionPoints = ((FunctionNode)this.scriptOrFn).getResumptionPoints();
                for (int i = 0; i < resumptionPoints.size(); ++i) {
                    final Node node = resumptionPoints.get(i);
                    final int[] array = liveLocals.get(node);
                    if (array != null) {
                        this.cfw.markTableSwitchCase(this.generatorSwitch, this.getNextGeneratorState(node));
                        this.generateGetGeneratorLocalsState();
                        for (int j = 0; j < array.length; ++j) {
                            this.cfw.add(89);
                            this.cfw.addLoadConstant(j);
                            this.cfw.add(50);
                            this.cfw.addAStore(array[j]);
                        }
                        this.cfw.add(87);
                        this.cfw.add(167, this.getTargetLabel(node));
                    }
                }
            }
            if (this.finallys != null) {
                for (final Node node2 : this.finallys.keySet()) {
                    if (node2.getType() == 125) {
                        final FinallyReturnPoint finallyReturnPoint = this.finallys.get(node2);
                        this.cfw.markLabel(finallyReturnPoint.tableLabel, (short)1);
                        final int addTableSwitch = this.cfw.addTableSwitch(0, finallyReturnPoint.jsrPoints.size() - 1);
                        this.cfw.markTableSwitchDefault(addTableSwitch);
                        int n = 0;
                        for (int k = 0; k < finallyReturnPoint.jsrPoints.size(); ++k) {
                            this.cfw.markTableSwitchCase(addTableSwitch, n);
                            this.cfw.add(167, finallyReturnPoint.jsrPoints.get(k));
                            ++n;
                        }
                    }
                }
            }
        }
        if (this.epilogueLabel != -1) {
            this.cfw.markLabel(this.epilogueLabel);
        }
        if (this.hasVarsInRegs) {
            this.cfw.add(176);
            return;
        }
        if (this.isGenerator) {
            if (((FunctionNode)this.scriptOrFn).getResumptionPoints() != null) {
                this.cfw.markTableSwitchDefault(this.generatorSwitch);
            }
            this.generateSetGeneratorResumptionPoint(-1);
            this.cfw.addALoad(this.variableObjectLocal);
            this.addOptRuntimeInvoke("throwStopIteration", "(Ljava/lang/Object;)V");
            Codegen.pushUndefined(this.cfw);
            this.cfw.add(176);
            return;
        }
        if (this.fnCurrent == null) {
            this.cfw.addALoad(this.popvLocal);
            this.cfw.add(176);
            return;
        }
        this.generateActivationExit();
        this.cfw.add(176);
        final int acquireLabel = this.cfw.acquireLabel();
        this.cfw.markHandler(acquireLabel);
        final short newWordLocal = this.getNewWordLocal();
        this.cfw.addAStore(newWordLocal);
        this.generateActivationExit();
        this.cfw.addALoad(newWordLocal);
        this.releaseWordLocal(newWordLocal);
        this.cfw.add(191);
        this.cfw.addExceptionHandler(this.enterAreaStartLabel, this.epilogueLabel, acquireLabel, null);
    }
    
    private void generateExpression(Node next, Node node) {
        final int type = next.getType();
        Node firstChild = next.getFirstChild();
        boolean b = false;
        Node next2 = firstChild;
        Label_2647: {
            switch (type) {
                default: {
                    switch (type) {
                        default: {
                            switch (type) {
                                default: {
                                    Label_1456: {
                                        switch (type) {
                                            default: {
                                                switch (type) {
                                                    default: {
                                                        switch (type) {
                                                            default: {
                                                                switch (type) {
                                                                    default: {
                                                                        switch (type) {
                                                                            default: {
                                                                                switch (type) {
                                                                                    default: {
                                                                                        final StringBuilder sb = new StringBuilder();
                                                                                        sb.append("Unexpected node type ");
                                                                                        sb.append(type);
                                                                                        throw new RuntimeException(sb.toString());
                                                                                    }
                                                                                    case 159: {
                                                                                        next = firstChild.getNext();
                                                                                        node = next.getNext();
                                                                                        this.generateStatement(firstChild);
                                                                                        this.generateExpression(next.getFirstChild(), next);
                                                                                        this.generateStatement(node);
                                                                                        return;
                                                                                    }
                                                                                    case 146: {
                                                                                        this.visitDotQuery(next, firstChild);
                                                                                        return;
                                                                                    }
                                                                                    case 126: {
                                                                                        this.generateExpression(firstChild, next);
                                                                                        this.cfw.add(87);
                                                                                        Codegen.pushUndefined(this.cfw);
                                                                                        return;
                                                                                    }
                                                                                    case 109: {
                                                                                        if (this.fnCurrent == null && node.getType() == 136) {
                                                                                            break Label_2647;
                                                                                        }
                                                                                        final OptFunctionNode value = OptFunctionNode.get(this.scriptOrFn, next.getExistingIntProp(1));
                                                                                        final int functionType = value.fnode.getFunctionType();
                                                                                        if (functionType != 2) {
                                                                                            throw Codegen.badTree();
                                                                                        }
                                                                                        this.visitFunction(value, functionType);
                                                                                        return;
                                                                                    }
                                                                                    case 102: {
                                                                                        node = firstChild.getNext();
                                                                                        final Node next3 = node.getNext();
                                                                                        this.generateExpression(firstChild, next);
                                                                                        this.addScriptRuntimeInvoke("toBoolean", "(Ljava/lang/Object;)Z");
                                                                                        final int acquireLabel = this.cfw.acquireLabel();
                                                                                        this.cfw.add(153, acquireLabel);
                                                                                        final short stackTop = this.cfw.getStackTop();
                                                                                        this.generateExpression(node, next);
                                                                                        final int acquireLabel2 = this.cfw.acquireLabel();
                                                                                        this.cfw.add(167, acquireLabel2);
                                                                                        this.cfw.markLabel(acquireLabel, stackTop);
                                                                                        this.generateExpression(next3, next);
                                                                                        this.cfw.markLabel(acquireLabel2);
                                                                                        return;
                                                                                    }
                                                                                    case 89: {
                                                                                        for (node = firstChild.getNext(); node != null; node = node.getNext()) {
                                                                                            this.generateExpression(firstChild, next);
                                                                                            this.cfw.add(87);
                                                                                            firstChild = node;
                                                                                        }
                                                                                        this.generateExpression(firstChild, next);
                                                                                        return;
                                                                                    }
                                                                                    case 142: {
                                                                                        break Label_1456;
                                                                                    }
                                                                                }
                                                                                break;
                                                                            }
                                                                            case 157: {
                                                                                node = firstChild.getNext();
                                                                                this.generateStatement(firstChild);
                                                                                this.generateExpression(node, next);
                                                                                return;
                                                                            }
                                                                            case 156: {
                                                                                this.visitSetConstVar(next, firstChild, true);
                                                                                return;
                                                                            }
                                                                            case 155: {
                                                                                this.visitSetConst(next, firstChild);
                                                                                return;
                                                                            }
                                                                        }
                                                                        break;
                                                                    }
                                                                    case 150: {
                                                                        this.generateExpression(firstChild, next);
                                                                        this.addObjectToDouble();
                                                                        return;
                                                                    }
                                                                    case 149: {
                                                                        int intProp = -1;
                                                                        if (firstChild.getType() == 40) {
                                                                            intProp = firstChild.getIntProp(8, -1);
                                                                        }
                                                                        if (intProp != -1) {
                                                                            firstChild.removeProp(8);
                                                                            this.generateExpression(firstChild, next);
                                                                            firstChild.putIntProp(8, intProp);
                                                                            return;
                                                                        }
                                                                        this.generateExpression(firstChild, next);
                                                                        this.addDoubleWrap();
                                                                        return;
                                                                    }
                                                                }
                                                                break;
                                                            }
                                                            case 138: {
                                                                return;
                                                            }
                                                            case 137: {
                                                                this.visitTypeofname(next);
                                                                return;
                                                            }
                                                            case 140: {
                                                                break Label_2647;
                                                            }
                                                            case 139: {
                                                                break Label_2647;
                                                            }
                                                        }
                                                        break;
                                                    }
                                                    case 106:
                                                    case 107: {
                                                        this.visitIncDec(next);
                                                        return;
                                                    }
                                                    case 104:
                                                    case 105: {
                                                        this.generateExpression(firstChild, next);
                                                        this.cfw.add(89);
                                                        this.addScriptRuntimeInvoke("toBoolean", "(Ljava/lang/Object;)Z");
                                                        final int acquireLabel3 = this.cfw.acquireLabel();
                                                        if (type == 105) {
                                                            this.cfw.add(153, acquireLabel3);
                                                        }
                                                        else {
                                                            this.cfw.add(154, acquireLabel3);
                                                        }
                                                        this.cfw.add(87);
                                                        this.generateExpression(firstChild.getNext(), next);
                                                        this.cfw.markLabel(acquireLabel3);
                                                        return;
                                                    }
                                                }
                                                break;
                                            }
                                            case 77:
                                            case 78:
                                            case 79:
                                            case 80: {
                                                final int intProp2 = next.getIntProp(16, 0);
                                                do {
                                                    this.generateExpression(firstChild, next);
                                                    node = firstChild.getNext();
                                                } while ((firstChild = node) != null);
                                                this.cfw.addALoad(this.contextLocal);
                                                String s = null;
                                                String s2 = null;
                                                switch (type) {
                                                    default: {
                                                        throw Kit.codeBug();
                                                    }
                                                    case 80: {
                                                        s = "nameRef";
                                                        s2 = "(Ljava/lang/Object;Ljava/lang/Object;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;I)Lorg/mozilla/javascript/Ref;";
                                                        this.cfw.addALoad(this.variableObjectLocal);
                                                        break;
                                                    }
                                                    case 79: {
                                                        s = "nameRef";
                                                        s2 = "(Ljava/lang/Object;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;I)Lorg/mozilla/javascript/Ref;";
                                                        this.cfw.addALoad(this.variableObjectLocal);
                                                        break;
                                                    }
                                                    case 78: {
                                                        s = "memberRef";
                                                        s2 = "(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Lorg/mozilla/javascript/Context;I)Lorg/mozilla/javascript/Ref;";
                                                        break;
                                                    }
                                                    case 77: {
                                                        s = "memberRef";
                                                        s2 = "(Ljava/lang/Object;Ljava/lang/Object;Lorg/mozilla/javascript/Context;I)Lorg/mozilla/javascript/Ref;";
                                                        break;
                                                    }
                                                }
                                                this.cfw.addPush(intProp2);
                                                this.addScriptRuntimeInvoke(s, s2);
                                                return;
                                            }
                                            case 76: {
                                                this.generateExpression(firstChild, next);
                                                this.cfw.addALoad(this.contextLocal);
                                                this.addScriptRuntimeInvoke("escapeTextValue", "(Ljava/lang/Object;Lorg/mozilla/javascript/Context;)Ljava/lang/String;");
                                                return;
                                            }
                                            case 75: {
                                                this.generateExpression(firstChild, next);
                                                this.cfw.addALoad(this.contextLocal);
                                                this.addScriptRuntimeInvoke("escapeAttributeValue", "(Ljava/lang/Object;Lorg/mozilla/javascript/Context;)Ljava/lang/String;");
                                                return;
                                            }
                                            case 74: {
                                                this.generateExpression(firstChild, next);
                                                this.cfw.addALoad(this.contextLocal);
                                                this.addScriptRuntimeInvoke("setDefaultNamespace", "(Ljava/lang/Object;Lorg/mozilla/javascript/Context;)Ljava/lang/Object;");
                                                return;
                                            }
                                            case 73: {
                                                this.visitStrictSetName(next, firstChild);
                                                return;
                                            }
                                            case 72: {
                                                this.generateYieldPoint(next, true);
                                                return;
                                            }
                                            case 71: {
                                                final String s3 = (String)next.getProp(17);
                                                this.generateExpression(firstChild, next);
                                                this.cfw.addPush(s3);
                                                this.cfw.addALoad(this.contextLocal);
                                                this.cfw.addALoad(this.variableObjectLocal);
                                                this.addScriptRuntimeInvoke("specialRef", "(Ljava/lang/Object;Ljava/lang/String;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;)Lorg/mozilla/javascript/Ref;");
                                                return;
                                            }
                                            case 70: {
                                                this.generateFunctionAndThisObj(firstChild, next);
                                                this.generateCallArgArray(next, firstChild.getNext(), false);
                                                this.cfw.addALoad(this.contextLocal);
                                                this.addScriptRuntimeInvoke("callRef", "(Lorg/mozilla/javascript/Callable;Lorg/mozilla/javascript/Scriptable;[Ljava/lang/Object;Lorg/mozilla/javascript/Context;)Lorg/mozilla/javascript/Ref;");
                                                return;
                                            }
                                            case 69: {
                                                this.generateExpression(firstChild, next);
                                                this.cfw.addALoad(this.contextLocal);
                                                this.addScriptRuntimeInvoke("refDel", "(Lorg/mozilla/javascript/Ref;Lorg/mozilla/javascript/Context;)Ljava/lang/Object;");
                                                return;
                                            }
                                            case 68: {
                                                this.generateExpression(firstChild, next);
                                                node = firstChild.getNext();
                                                if (type == 142) {
                                                    this.cfw.add(89);
                                                    this.cfw.addALoad(this.contextLocal);
                                                    this.addScriptRuntimeInvoke("refGet", "(Lorg/mozilla/javascript/Ref;Lorg/mozilla/javascript/Context;)Ljava/lang/Object;");
                                                }
                                                this.generateExpression(node, next);
                                                this.cfw.addALoad(this.contextLocal);
                                                this.cfw.addALoad(this.variableObjectLocal);
                                                this.addScriptRuntimeInvoke("refSet", "(Lorg/mozilla/javascript/Ref;Ljava/lang/Object;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;)Ljava/lang/Object;");
                                                return;
                                            }
                                            case 67: {
                                                this.generateExpression(firstChild, next);
                                                this.cfw.addALoad(this.contextLocal);
                                                this.addScriptRuntimeInvoke("refGet", "(Lorg/mozilla/javascript/Ref;Lorg/mozilla/javascript/Context;)Ljava/lang/Object;");
                                                return;
                                            }
                                            case 66: {
                                                this.visitObjectLiteral(next, firstChild, false);
                                                return;
                                            }
                                            case 65: {
                                                this.visitArrayLiteral(next, firstChild, false);
                                                return;
                                            }
                                        }
                                    }
                                    break;
                                }
                                case 63: {
                                    this.cfw.add(42);
                                    return;
                                }
                                case 61:
                                case 62: {
                                    this.cfw.addALoad(this.getLocalBlockRegister(next));
                                    if (type == 61) {
                                        this.addScriptRuntimeInvoke("enumNext", "(Ljava/lang/Object;)Ljava/lang/Boolean;");
                                        return;
                                    }
                                    this.cfw.addALoad(this.contextLocal);
                                    this.addScriptRuntimeInvoke("enumId", "(Ljava/lang/Object;Lorg/mozilla/javascript/Context;)Ljava/lang/Object;");
                                    return;
                                }
                            }
                            break;
                        }
                        case 56: {
                            this.visitSetVar(next, firstChild, true);
                            return;
                        }
                        case 55: {
                            this.visitGetVar(next);
                            return;
                        }
                        case 54: {
                            this.cfw.addALoad(this.getLocalBlockRegister(next));
                            return;
                        }
                        case 52:
                        case 53: {
                            break Label_2647;
                        }
                    }
                    break;
                }
                case 49: {
                    while (next2 != null) {
                        this.generateExpression(next2, next);
                        next2 = next2.getNext();
                    }
                    this.cfw.addALoad(this.contextLocal);
                    this.cfw.addALoad(this.variableObjectLocal);
                    this.cfw.addPush(next.getString());
                    this.addScriptRuntimeInvoke("bind", "(Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;Ljava/lang/String;)Lorg/mozilla/javascript/Scriptable;");
                    return;
                }
                case 48: {
                    this.cfw.addALoad(this.contextLocal);
                    this.cfw.addALoad(this.variableObjectLocal);
                    this.cfw.add(178, this.codegen.mainClassName, this.codegen.getCompiledRegexpName(this.scriptOrFn, next.getExistingIntProp(4)), "Ljava/lang/Object;");
                    this.cfw.addInvoke(184, "org/mozilla/javascript/ScriptRuntime", "wrapRegExp", "(Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;Ljava/lang/Object;)Lorg/mozilla/javascript/Scriptable;");
                    return;
                }
                case 45: {
                    this.cfw.add(178, "java/lang/Boolean", "TRUE", "Ljava/lang/Boolean;");
                    return;
                }
                case 44: {
                    this.cfw.add(178, "java/lang/Boolean", "FALSE", "Ljava/lang/Boolean;");
                    return;
                }
                case 43: {
                    this.cfw.addALoad(this.thisObjLocal);
                    return;
                }
                case 42: {
                    this.cfw.add(1);
                    return;
                }
                case 41: {
                    this.cfw.addPush(next.getString());
                    return;
                }
                case 40: {
                    final double double1 = next.getDouble();
                    if (next.getIntProp(8, -1) != -1) {
                        this.cfw.addPush(double1);
                    }
                    else {
                        this.codegen.pushNumberAsObject(this.cfw, double1);
                    }
                    return;
                }
                case 39: {
                    this.cfw.addALoad(this.contextLocal);
                    this.cfw.addALoad(this.variableObjectLocal);
                    this.cfw.addPush(next.getString());
                    this.addScriptRuntimeInvoke("name", "(Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;Ljava/lang/String;)Ljava/lang/Object;");
                    return;
                }
                case 37: {
                    this.visitSetElem(type, next, firstChild);
                    return;
                }
                case 36: {
                    this.generateExpression(firstChild, next);
                    this.generateExpression(firstChild.getNext(), next);
                    this.cfw.addALoad(this.contextLocal);
                    if (next.getIntProp(8, -1) != -1) {
                        this.addScriptRuntimeInvoke("getObjectIndex", "(Ljava/lang/Object;DLorg/mozilla/javascript/Context;)Ljava/lang/Object;");
                        return;
                    }
                    this.cfw.addALoad(this.variableObjectLocal);
                    this.addScriptRuntimeInvoke("getObjectElem", "(Ljava/lang/Object;Ljava/lang/Object;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;)Ljava/lang/Object;");
                    return;
                }
                case 35: {
                    this.visitSetProp(type, next, firstChild);
                    return;
                }
                case 33:
                case 34: {
                    this.visitGetProp(next, firstChild);
                    return;
                }
                case 32: {
                    this.generateExpression(firstChild, next);
                    this.addScriptRuntimeInvoke("typeof", "(Ljava/lang/Object;)Ljava/lang/String;");
                    return;
                }
                case 31: {
                    if (firstChild.getType() == 49) {
                        b = true;
                    }
                    this.generateExpression(firstChild, next);
                    this.generateExpression(firstChild.getNext(), next);
                    this.cfw.addALoad(this.contextLocal);
                    this.cfw.addPush(b);
                    this.addScriptRuntimeInvoke("delete", "(Ljava/lang/Object;Ljava/lang/Object;Lorg/mozilla/javascript/Context;Z)Ljava/lang/Object;");
                    return;
                }
                case 30:
                case 38: {
                    final int intProp3 = next.getIntProp(10, 0);
                    if (intProp3 == 0) {
                        final OptFunctionNode optFunctionNode = (OptFunctionNode)next.getProp(9);
                        if (optFunctionNode != null) {
                            this.visitOptimizedCall(next, optFunctionNode, type, firstChild);
                        }
                        else if (type == 38) {
                            this.visitStandardCall(next, firstChild);
                        }
                        else {
                            this.visitStandardNew(next, firstChild);
                        }
                    }
                    else {
                        this.visitSpecialCall(next, type, intProp3, firstChild);
                    }
                    return;
                }
                case 28:
                case 29: {
                    this.generateExpression(firstChild, next);
                    this.addObjectToDouble();
                    if (type == 29) {
                        this.cfw.add(119);
                    }
                    this.addDoubleWrap();
                    return;
                }
                case 27: {
                    this.generateExpression(firstChild, next);
                    this.addScriptRuntimeInvoke("toInt32", "(Ljava/lang/Object;)I");
                    this.cfw.addPush(-1);
                    this.cfw.add(130);
                    this.cfw.add(135);
                    this.addDoubleWrap();
                    return;
                }
                case 26: {
                    final int acquireLabel4 = this.cfw.acquireLabel();
                    final int acquireLabel5 = this.cfw.acquireLabel();
                    final int acquireLabel6 = this.cfw.acquireLabel();
                    this.generateIfJump(firstChild, next, acquireLabel4, acquireLabel5);
                    this.cfw.markLabel(acquireLabel4);
                    this.cfw.add(178, "java/lang/Boolean", "FALSE", "Ljava/lang/Boolean;");
                    this.cfw.add(167, acquireLabel6);
                    this.cfw.markLabel(acquireLabel5);
                    this.cfw.add(178, "java/lang/Boolean", "TRUE", "Ljava/lang/Boolean;");
                    this.cfw.markLabel(acquireLabel6);
                    this.cfw.adjustStackTop(-1);
                    return;
                }
                case 24:
                case 25: {
                    int n;
                    if (type == 24) {
                        n = 111;
                    }
                    else {
                        n = 115;
                    }
                    this.visitArithmetic(next, n, firstChild, node);
                    return;
                }
                case 23: {
                    this.visitArithmetic(next, 107, firstChild, node);
                    return;
                }
                case 22: {
                    this.visitArithmetic(next, 103, firstChild, node);
                    return;
                }
                case 21: {
                    this.generateExpression(firstChild, next);
                    this.generateExpression(firstChild.getNext(), next);
                    switch (next.getIntProp(8, -1)) {
                        default: {
                            if (firstChild.getType() == 41) {
                                this.addScriptRuntimeInvoke("add", "(Ljava/lang/CharSequence;Ljava/lang/Object;)Ljava/lang/CharSequence;");
                                return;
                            }
                            if (firstChild.getNext().getType() == 41) {
                                this.addScriptRuntimeInvoke("add", "(Ljava/lang/Object;Ljava/lang/CharSequence;)Ljava/lang/CharSequence;");
                                return;
                            }
                            this.cfw.addALoad(this.contextLocal);
                            this.addScriptRuntimeInvoke("add", "(Ljava/lang/Object;Ljava/lang/Object;Lorg/mozilla/javascript/Context;)Ljava/lang/Object;");
                            return;
                        }
                        case 2: {
                            this.addOptRuntimeInvoke("add", "(Ljava/lang/Object;D)Ljava/lang/Object;");
                            return;
                        }
                        case 1: {
                            this.addOptRuntimeInvoke("add", "(DLjava/lang/Object;)Ljava/lang/Object;");
                            return;
                        }
                        case 0: {
                            this.cfw.add(99);
                            return;
                        }
                    }
                    break;
                }
                case 14:
                case 15:
                case 16:
                case 17: {
                    final int acquireLabel7 = this.cfw.acquireLabel();
                    final int acquireLabel8 = this.cfw.acquireLabel();
                    this.visitIfJumpRelOp(next, firstChild, acquireLabel7, acquireLabel8);
                    this.addJumpedBooleanWrap(acquireLabel7, acquireLabel8);
                    return;
                }
                case 12:
                case 13:
                case 46:
                case 47: {
                    final int acquireLabel9 = this.cfw.acquireLabel();
                    final int acquireLabel10 = this.cfw.acquireLabel();
                    this.visitIfJumpEqOp(next, firstChild, acquireLabel9, acquireLabel10);
                    this.addJumpedBooleanWrap(acquireLabel9, acquireLabel10);
                    return;
                }
                case 9:
                case 10:
                case 11:
                case 18:
                case 19:
                case 20: {
                    this.visitBitOp(next, type, firstChild);
                    return;
                }
                case 8: {
                    this.visitSetName(next, firstChild);
                    break;
                }
            }
        }
    }
    
    private void generateFunctionAndThisObj(final Node node, Node node2) {
        final int type = node.getType();
        final int type2 = node.getType();
        Label_0253: {
            if (type2 != 36) {
                if (type2 == 39) {
                    this.cfw.addPush(node.getString());
                    this.cfw.addALoad(this.contextLocal);
                    this.cfw.addALoad(this.variableObjectLocal);
                    this.addScriptRuntimeInvoke("getNameFunctionAndThis", "(Ljava/lang/String;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;)Lorg/mozilla/javascript/Callable;");
                    break Label_0253;
                }
                switch (type2) {
                    default: {
                        this.generateExpression(node, node2);
                        this.cfw.addALoad(this.contextLocal);
                        this.addScriptRuntimeInvoke("getValueFunctionAndThis", "(Ljava/lang/Object;Lorg/mozilla/javascript/Context;)Lorg/mozilla/javascript/Callable;");
                        break Label_0253;
                    }
                    case 34: {
                        throw Kit.codeBug();
                    }
                    case 33: {
                        break;
                    }
                }
            }
            node2 = node.getFirstChild();
            this.generateExpression(node2, node);
            node2 = node2.getNext();
            if (type == 33) {
                this.cfw.addPush(node2.getString());
                this.cfw.addALoad(this.contextLocal);
                this.cfw.addALoad(this.variableObjectLocal);
                this.addScriptRuntimeInvoke("getPropFunctionAndThis", "(Ljava/lang/Object;Ljava/lang/String;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;)Lorg/mozilla/javascript/Callable;");
            }
            else {
                this.generateExpression(node2, node);
                if (node.getIntProp(8, -1) != -1) {
                    this.addDoubleWrap();
                }
                this.cfw.addALoad(this.contextLocal);
                this.cfw.addALoad(this.variableObjectLocal);
                this.addScriptRuntimeInvoke("getElemFunctionAndThis", "(Ljava/lang/Object;Ljava/lang/Object;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;)Lorg/mozilla/javascript/Callable;");
            }
        }
        this.cfw.addALoad(this.contextLocal);
        this.addScriptRuntimeInvoke("lastStoredScriptable", "(Lorg/mozilla/javascript/Context;)Lorg/mozilla/javascript/Scriptable;");
    }
    
    private void generateGenerator() {
        this.cfw.startMethod(this.codegen.getBodyMethodName(this.scriptOrFn), this.codegen.getBodyMethodSignature(this.scriptOrFn), (short)10);
        this.initBodyGeneration();
        final short firstFreeLocal = this.firstFreeLocal;
        this.firstFreeLocal = (short)(firstFreeLocal + 1);
        this.argsLocal = firstFreeLocal;
        this.localsMax = this.firstFreeLocal;
        if (this.fnCurrent != null) {
            this.cfw.addALoad(this.funObjLocal);
            this.cfw.addInvoke(185, "org/mozilla/javascript/Scriptable", "getParentScope", "()Lorg/mozilla/javascript/Scriptable;");
            this.cfw.addAStore(this.variableObjectLocal);
        }
        this.cfw.addALoad(this.funObjLocal);
        this.cfw.addALoad(this.variableObjectLocal);
        this.cfw.addALoad(this.argsLocal);
        this.addScriptRuntimeInvoke("createFunctionActivation", "(Lorg/mozilla/javascript/NativeFunction;Lorg/mozilla/javascript/Scriptable;[Ljava/lang/Object;)Lorg/mozilla/javascript/Scriptable;");
        this.cfw.addAStore(this.variableObjectLocal);
        this.cfw.add(187, this.codegen.mainClassName);
        this.cfw.add(89);
        this.cfw.addALoad(this.variableObjectLocal);
        this.cfw.addALoad(this.contextLocal);
        this.cfw.addPush(this.scriptOrFnIndex);
        this.cfw.addInvoke(183, this.codegen.mainClassName, "<init>", "(Lorg/mozilla/javascript/Scriptable;Lorg/mozilla/javascript/Context;I)V");
        this.generateNestedFunctionInits();
        this.cfw.addALoad(this.variableObjectLocal);
        this.cfw.addALoad(this.thisObjLocal);
        this.cfw.addLoadConstant(this.maxLocals);
        this.cfw.addLoadConstant(this.maxStack);
        this.addOptRuntimeInvoke("createNativeGenerator", "(Lorg/mozilla/javascript/NativeFunction;Lorg/mozilla/javascript/Scriptable;Lorg/mozilla/javascript/Scriptable;II)Lorg/mozilla/javascript/Scriptable;");
        this.cfw.add(176);
        this.cfw.stopMethod((short)(this.localsMax + 1));
    }
    
    private void generateGetGeneratorLocalsState() {
        this.cfw.addALoad(this.generatorStateLocal);
        this.addOptRuntimeInvoke("getGeneratorLocalsState", "(Ljava/lang/Object;)[Ljava/lang/Object;");
    }
    
    private void generateGetGeneratorResumptionPoint() {
        this.cfw.addALoad(this.generatorStateLocal);
        this.cfw.add(180, "org/mozilla/javascript/optimizer/OptRuntime$GeneratorState", "resumptionPoint", "I");
    }
    
    private void generateGetGeneratorStackState() {
        this.cfw.addALoad(this.generatorStateLocal);
        this.addOptRuntimeInvoke("getGeneratorStackState", "(Ljava/lang/Object;)[Ljava/lang/Object;");
    }
    
    private void generateIfJump(final Node node, final Node node2, final int n, final int n2) {
        final int type = node.getType();
        final Node firstChild = node.getFirstChild();
        if (type == 26) {
            this.generateIfJump(firstChild, node, n2, n);
            return;
        }
        Label_0246: {
            switch (type) {
                default: {
                    switch (type) {
                        default: {
                            switch (type) {
                                default: {
                                    switch (type) {
                                        default: {
                                            this.generateExpression(node, node2);
                                            this.addScriptRuntimeInvoke("toBoolean", "(Ljava/lang/Object;)Z");
                                            this.cfw.add(154, n);
                                            this.cfw.add(167, n2);
                                            return;
                                        }
                                        case 104:
                                        case 105: {
                                            final int acquireLabel = this.cfw.acquireLabel();
                                            if (type == 105) {
                                                this.generateIfJump(firstChild, node, acquireLabel, n2);
                                            }
                                            else {
                                                this.generateIfJump(firstChild, node, n, acquireLabel);
                                            }
                                            this.cfw.markLabel(acquireLabel);
                                            this.generateIfJump(firstChild.getNext(), node, n, n2);
                                            return;
                                        }
                                    }
                                    break;
                                }
                                case 52:
                                case 53: {
                                    break Label_0246;
                                }
                            }
                            break;
                        }
                        case 46:
                        case 47: {
                            break Label_0246;
                        }
                    }
                    break;
                }
                case 14:
                case 15:
                case 16:
                case 17: {
                    this.visitIfJumpRelOp(node, firstChild, n, n2);
                }
                case 12:
                case 13: {
                    this.visitIfJumpEqOp(node, firstChild, n, n2);
                }
            }
        }
    }
    
    private void generateIntegerUnwrap() {
        this.cfw.addInvoke(182, "java/lang/Integer", "intValue", "()I");
    }
    
    private void generateIntegerWrap() {
        this.cfw.addInvoke(184, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;");
    }
    
    private void generateNestedFunctionInits() {
        for (int functionCount = this.scriptOrFn.getFunctionCount(), i = 0; i != functionCount; ++i) {
            final OptFunctionNode value = OptFunctionNode.get(this.scriptOrFn, i);
            if (value.fnode.getFunctionType() == 1) {
                this.visitFunction(value, 1);
            }
        }
    }
    
    private void generateObjectLiteralFactory(final Node node, final int n) {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.codegen.getBodyMethodName(this.scriptOrFn));
        sb.append("_literal");
        sb.append(n);
        final String string = sb.toString();
        this.initBodyGeneration();
        final short firstFreeLocal = this.firstFreeLocal;
        this.firstFreeLocal = (short)(firstFreeLocal + 1);
        this.argsLocal = firstFreeLocal;
        this.localsMax = this.firstFreeLocal;
        this.cfw.startMethod(string, "(Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;Lorg/mozilla/javascript/Scriptable;[Ljava/lang/Object;)Lorg/mozilla/javascript/Scriptable;", (short)2);
        this.visitObjectLiteral(node, node.getFirstChild(), true);
        this.cfw.add(176);
        this.cfw.stopMethod((short)(this.localsMax + 1));
    }
    
    private void generatePrologue() {
        if (this.inDirectCallFunction) {
            final int paramCount = this.scriptOrFn.getParamCount();
            if (this.firstFreeLocal != 4) {
                Kit.codeBug();
            }
            for (int i = 0; i != paramCount; ++i) {
                this.varRegisters[i] = this.firstFreeLocal;
                this.firstFreeLocal += 3;
            }
            if (!this.fnCurrent.getParameterNumberContext()) {
                this.itsForcedObjectParameters = true;
                for (int j = 0; j != paramCount; ++j) {
                    final short n = this.varRegisters[j];
                    this.cfw.addALoad(n);
                    this.cfw.add(178, "java/lang/Void", "TYPE", "Ljava/lang/Class;");
                    final int acquireLabel = this.cfw.acquireLabel();
                    this.cfw.add(166, acquireLabel);
                    this.cfw.addDLoad(n + 1);
                    this.addDoubleWrap();
                    this.cfw.addAStore(n);
                    this.cfw.markLabel(acquireLabel);
                }
            }
        }
        if (this.fnCurrent != null) {
            this.cfw.addALoad(this.funObjLocal);
            this.cfw.addInvoke(185, "org/mozilla/javascript/Scriptable", "getParentScope", "()Lorg/mozilla/javascript/Scriptable;");
            this.cfw.addAStore(this.variableObjectLocal);
        }
        final short firstFreeLocal = this.firstFreeLocal;
        this.firstFreeLocal = (short)(firstFreeLocal + 1);
        this.argsLocal = firstFreeLocal;
        this.localsMax = this.firstFreeLocal;
        if (this.isGenerator) {
            final short firstFreeLocal2 = this.firstFreeLocal;
            this.firstFreeLocal = (short)(firstFreeLocal2 + 1);
            this.operationLocal = firstFreeLocal2;
            this.localsMax = this.firstFreeLocal;
            this.cfw.addALoad(this.thisObjLocal);
            final short firstFreeLocal3 = this.firstFreeLocal;
            this.firstFreeLocal = (short)(firstFreeLocal3 + 1);
            this.generatorStateLocal = firstFreeLocal3;
            this.localsMax = this.firstFreeLocal;
            this.cfw.add(192, "org/mozilla/javascript/optimizer/OptRuntime$GeneratorState");
            this.cfw.add(89);
            this.cfw.addAStore(this.generatorStateLocal);
            this.cfw.add(180, "org/mozilla/javascript/optimizer/OptRuntime$GeneratorState", "thisObj", "Lorg/mozilla/javascript/Scriptable;");
            this.cfw.addAStore(this.thisObjLocal);
            if (this.epilogueLabel == -1) {
                this.epilogueLabel = this.cfw.acquireLabel();
            }
            final List<Node> resumptionPoints = ((FunctionNode)this.scriptOrFn).getResumptionPoints();
            if (resumptionPoints != null) {
                this.generateGetGeneratorResumptionPoint();
                this.generatorSwitch = this.cfw.addTableSwitch(0, resumptionPoints.size() + 0);
                this.generateCheckForThrowOrClose(-1, false, 0);
            }
        }
        if (this.fnCurrent == null && this.scriptOrFn.getRegexpCount() != 0) {
            this.cfw.addALoad(this.contextLocal);
            this.cfw.addInvoke(184, this.codegen.mainClassName, "_reInit", "(Lorg/mozilla/javascript/Context;)V");
        }
        if (this.compilerEnv.isGenerateObserverCount()) {
            this.saveCurrentCodeOffset();
        }
        if (this.hasVarsInRegs) {
            final int paramCount2 = this.scriptOrFn.getParamCount();
            if (paramCount2 > 0 && !this.inDirectCallFunction) {
                this.cfw.addALoad(this.argsLocal);
                this.cfw.add(190);
                this.cfw.addPush(paramCount2);
                final int acquireLabel2 = this.cfw.acquireLabel();
                this.cfw.add(162, acquireLabel2);
                this.cfw.addALoad(this.argsLocal);
                this.cfw.addPush(paramCount2);
                this.addScriptRuntimeInvoke("padArguments", "([Ljava/lang/Object;I)[Ljava/lang/Object;");
                this.cfw.addAStore(this.argsLocal);
                this.cfw.markLabel(acquireLabel2);
            }
            final int paramCount3 = this.fnCurrent.fnode.getParamCount();
            final int paramAndVarCount = this.fnCurrent.fnode.getParamAndVarCount();
            final boolean[] paramAndVarConst = this.fnCurrent.fnode.getParamAndVarConst();
            int n2 = -1;
            int n4;
            for (int k = 0; k != paramAndVarCount; ++k, n2 = n4) {
                short n3 = -1;
                if (k < paramCount3) {
                    n4 = n2;
                    if (!this.inDirectCallFunction) {
                        n3 = this.getNewWordLocal();
                        this.cfw.addALoad(this.argsLocal);
                        this.cfw.addPush(k);
                        this.cfw.add(50);
                        this.cfw.addAStore(n3);
                        n4 = n2;
                    }
                }
                else if (this.fnCurrent.isNumberVar(k)) {
                    n3 = this.getNewWordPairLocal(paramAndVarConst[k]);
                    this.cfw.addPush(0.0);
                    this.cfw.addDStore(n3);
                    n4 = n2;
                }
                else {
                    n3 = this.getNewWordLocal(paramAndVarConst[k]);
                    if (n2 == -1) {
                        Codegen.pushUndefined(this.cfw);
                        n2 = n3;
                    }
                    else {
                        this.cfw.addALoad(n2);
                    }
                    this.cfw.addAStore(n3);
                    n4 = n2;
                }
                if (n3 >= 0) {
                    if (paramAndVarConst[k]) {
                        this.cfw.addPush(0);
                        final ClassFileWriter cfw = this.cfw;
                        short n5;
                        if (this.fnCurrent.isNumberVar(k)) {
                            n5 = 2;
                        }
                        else {
                            n5 = 1;
                        }
                        cfw.addIStore(n5 + n3);
                    }
                    this.varRegisters[k] = n3;
                }
                if (this.compilerEnv.isGenerateDebugInfo()) {
                    final String paramOrVarName = this.fnCurrent.fnode.getParamOrVarName(k);
                    String s;
                    if (this.fnCurrent.isNumberVar(k)) {
                        s = "D";
                    }
                    else {
                        s = "Ljava/lang/Object;";
                    }
                    final int currentCodeOffset = this.cfw.getCurrentCodeOffset();
                    short n6 = n3;
                    if (n3 < 0) {
                        n6 = this.varRegisters[k];
                    }
                    this.cfw.addVariableDescriptor(paramOrVarName, s, currentCodeOffset, n6);
                }
            }
            return;
        }
        if (this.isGenerator) {
            return;
        }
        String s2;
        if (this.fnCurrent != null) {
            s2 = "activation";
            this.cfw.addALoad(this.funObjLocal);
            this.cfw.addALoad(this.variableObjectLocal);
            this.cfw.addALoad(this.argsLocal);
            this.addScriptRuntimeInvoke("createFunctionActivation", "(Lorg/mozilla/javascript/NativeFunction;Lorg/mozilla/javascript/Scriptable;[Ljava/lang/Object;)Lorg/mozilla/javascript/Scriptable;");
            this.cfw.addAStore(this.variableObjectLocal);
            this.cfw.addALoad(this.contextLocal);
            this.cfw.addALoad(this.variableObjectLocal);
            this.addScriptRuntimeInvoke("enterActivationFunction", "(Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;)V");
        }
        else {
            s2 = "global";
            this.cfw.addALoad(this.funObjLocal);
            this.cfw.addALoad(this.thisObjLocal);
            this.cfw.addALoad(this.contextLocal);
            this.cfw.addALoad(this.variableObjectLocal);
            this.cfw.addPush(0);
            this.addScriptRuntimeInvoke("initScript", "(Lorg/mozilla/javascript/NativeFunction;Lorg/mozilla/javascript/Scriptable;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;Z)V");
        }
        this.enterAreaStartLabel = this.cfw.acquireLabel();
        this.epilogueLabel = this.cfw.acquireLabel();
        this.cfw.markLabel(this.enterAreaStartLabel);
        this.generateNestedFunctionInits();
        if (this.compilerEnv.isGenerateDebugInfo()) {
            this.cfw.addVariableDescriptor(s2, "Lorg/mozilla/javascript/Scriptable;", this.cfw.getCurrentCodeOffset(), this.variableObjectLocal);
        }
        if (this.fnCurrent == null) {
            this.popvLocal = this.getNewWordLocal();
            Codegen.pushUndefined(this.cfw);
            this.cfw.addAStore(this.popvLocal);
            final int endLineno = this.scriptOrFn.getEndLineno();
            if (endLineno != -1) {
                this.cfw.addLineNumberEntry((short)endLineno);
            }
            return;
        }
        if (this.fnCurrent.itsContainsCalls0) {
            this.itsZeroArgArray = this.getNewWordLocal();
            this.cfw.add(178, "org/mozilla/javascript/ScriptRuntime", "emptyArgs", "[Ljava/lang/Object;");
            this.cfw.addAStore(this.itsZeroArgArray);
        }
        if (this.fnCurrent.itsContainsCalls1) {
            this.itsOneArgArray = this.getNewWordLocal();
            this.cfw.addPush(1);
            this.cfw.add(189, "java/lang/Object");
            this.cfw.addAStore(this.itsOneArgArray);
        }
    }
    
    private boolean generateSaveLocals(final Node node) {
        final int n = 0;
        int n2 = 0;
        int n3;
        for (int i = 0; i < this.firstFreeLocal; ++i, n2 = n3) {
            n3 = n2;
            if (this.locals[i] != 0) {
                n3 = n2 + 1;
            }
        }
        if (n2 == 0) {
            ((FunctionNode)this.scriptOrFn).addLiveLocals(node, null);
            return false;
        }
        int maxLocals;
        if (this.maxLocals > n2) {
            maxLocals = this.maxLocals;
        }
        else {
            maxLocals = n2;
        }
        this.maxLocals = maxLocals;
        final int[] array = new int[n2];
        int n4 = 0;
        int n5;
        for (int j = 0; j < this.firstFreeLocal; ++j, n4 = n5) {
            n5 = n4;
            if (this.locals[j] != 0) {
                array[n4] = j;
                n5 = n4 + 1;
            }
        }
        ((FunctionNode)this.scriptOrFn).addLiveLocals(node, array);
        this.generateGetGeneratorLocalsState();
        for (int k = n; k < n2; ++k) {
            this.cfw.add(89);
            this.cfw.addLoadConstant(k);
            this.cfw.addALoad(array[k]);
            this.cfw.add(83);
        }
        this.cfw.add(87);
        return true;
    }
    
    private void generateSetGeneratorResumptionPoint(final int n) {
        this.cfw.addALoad(this.generatorStateLocal);
        this.cfw.addLoadConstant(n);
        this.cfw.add(181, "org/mozilla/javascript/optimizer/OptRuntime$GeneratorState", "resumptionPoint", "I");
    }
    
    private void generateStatement(Node next) {
        this.updateLineNumber(next);
        final int type = next.getType();
        Node node = next.getFirstChild();
        Label_1053: {
            switch (type) {
                default: {
                    switch (type) {
                        default: {
                            int n = 2;
                            switch (type) {
                                default: {
                                    Label_0762: {
                                        switch (type) {
                                            default: {
                                                switch (type) {
                                                    default: {
                                                        throw Codegen.badTree();
                                                    }
                                                    case 160: {
                                                        return;
                                                    }
                                                    case 141: {
                                                        final boolean inLocalBlock = this.inLocalBlock;
                                                        this.inLocalBlock = true;
                                                        final short newWordLocal = this.getNewWordLocal();
                                                        if (this.isGenerator) {
                                                            this.cfw.add(1);
                                                            this.cfw.addAStore(newWordLocal);
                                                        }
                                                        next.putIntProp(2, newWordLocal);
                                                        while (node != null) {
                                                            this.generateStatement(node);
                                                            node = node.getNext();
                                                        }
                                                        this.releaseWordLocal(newWordLocal);
                                                        next.removeProp(2);
                                                        this.inLocalBlock = inLocalBlock;
                                                        return;
                                                    }
                                                    case 125: {
                                                        if (!this.isGenerator) {
                                                            return;
                                                        }
                                                        if (this.compilerEnv.isGenerateObserverCount()) {
                                                            this.saveCurrentCodeOffset();
                                                        }
                                                        this.cfw.setStackTop((short)1);
                                                        final short newWordLocal2 = this.getNewWordLocal();
                                                        final int acquireLabel = this.cfw.acquireLabel();
                                                        final int acquireLabel2 = this.cfw.acquireLabel();
                                                        this.cfw.markLabel(acquireLabel);
                                                        this.generateIntegerWrap();
                                                        this.cfw.addAStore(newWordLocal2);
                                                        while (node != null) {
                                                            this.generateStatement(node);
                                                            node = node.getNext();
                                                        }
                                                        this.cfw.addALoad(newWordLocal2);
                                                        this.cfw.add(192, "java/lang/Integer");
                                                        this.generateIntegerUnwrap();
                                                        final FinallyReturnPoint finallyReturnPoint = this.finallys.get(next);
                                                        finallyReturnPoint.tableLabel = this.cfw.acquireLabel();
                                                        this.cfw.add(167, finallyReturnPoint.tableLabel);
                                                        this.releaseWordLocal(newWordLocal2);
                                                        this.cfw.markLabel(acquireLabel2);
                                                        return;
                                                    }
                                                    case 114: {
                                                        if (this.compilerEnv.isGenerateObserverCount()) {
                                                            this.addInstructionCount();
                                                        }
                                                        this.visitSwitch((Jump)next, node);
                                                        return;
                                                    }
                                                    case 109: {
                                                        final OptFunctionNode value = OptFunctionNode.get(this.scriptOrFn, next.getExistingIntProp(1));
                                                        final int functionType = value.fnode.getFunctionType();
                                                        if (functionType == 3) {
                                                            this.visitFunction(value, functionType);
                                                            return;
                                                        }
                                                        if (functionType != 1) {
                                                            throw Codegen.badTree();
                                                        }
                                                        break Label_1053;
                                                    }
                                                    case 81: {
                                                        this.visitTryCatchFinally((Jump)next, node);
                                                        return;
                                                    }
                                                    case 123: {
                                                        break Label_0762;
                                                    }
                                                    case 64: {
                                                        break Label_1053;
                                                    }
                                                }
                                                break;
                                            }
                                            case 134: {
                                                this.generateExpression(node, next);
                                                if (this.popvLocal < 0) {
                                                    this.popvLocal = this.getNewWordLocal();
                                                }
                                                this.cfw.addAStore(this.popvLocal);
                                                return;
                                            }
                                            case 133: {
                                                if (node.getType() == 56) {
                                                    this.visitSetVar(node, node.getFirstChild(), false);
                                                    return;
                                                }
                                                if (node.getType() == 156) {
                                                    this.visitSetConstVar(node, node.getFirstChild(), false);
                                                    return;
                                                }
                                                if (node.getType() == 72) {
                                                    this.generateYieldPoint(node, false);
                                                    return;
                                                }
                                                this.generateExpression(node, next);
                                                if (next.getIntProp(8, -1) != -1) {
                                                    this.cfw.add(88);
                                                    return;
                                                }
                                                this.cfw.add(87);
                                                return;
                                            }
                                            case 131: {
                                                if (this.compilerEnv.isGenerateObserverCount()) {
                                                    this.addInstructionCount();
                                                }
                                                this.cfw.markLabel(this.getTargetLabel(next));
                                                if (this.compilerEnv.isGenerateObserverCount()) {
                                                    this.saveCurrentCodeOffset();
                                                }
                                                return;
                                            }
                                            case 128:
                                            case 129:
                                            case 130:
                                            case 132:
                                            case 136: {
                                                next = node;
                                                if (this.compilerEnv.isGenerateObserverCount()) {
                                                    this.addInstructionCount(1);
                                                    next = node;
                                                }
                                                while (next != null) {
                                                    this.generateStatement(next);
                                                    next = next.getNext();
                                                }
                                                break Label_1053;
                                            }
                                            case 135: {
                                                break Label_1053;
                                            }
                                        }
                                    }
                                    break;
                                }
                                case 58:
                                case 59:
                                case 60: {
                                    this.generateExpression(node, next);
                                    this.cfw.addALoad(this.contextLocal);
                                    this.cfw.addALoad(this.variableObjectLocal);
                                    if (type == 58) {
                                        n = 0;
                                    }
                                    else if (type == 59) {
                                        n = 1;
                                    }
                                    this.cfw.addPush(n);
                                    this.addScriptRuntimeInvoke("enumInit", "(Ljava/lang/Object;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;I)Ljava/lang/Object;");
                                    this.cfw.addAStore(this.getLocalBlockRegister(next));
                                    return;
                                }
                                case 57: {
                                    this.cfw.setStackTop((short)0);
                                    final int localBlockRegister = this.getLocalBlockRegister(next);
                                    final int existingIntProp = next.getExistingIntProp(14);
                                    final String string = node.getString();
                                    this.generateExpression(node.getNext(), next);
                                    if (existingIntProp == 0) {
                                        this.cfw.add(1);
                                    }
                                    else {
                                        this.cfw.addALoad(localBlockRegister);
                                    }
                                    this.cfw.addPush(string);
                                    this.cfw.addALoad(this.contextLocal);
                                    this.cfw.addALoad(this.variableObjectLocal);
                                    this.addScriptRuntimeInvoke("newCatchScope", "(Ljava/lang/Throwable;Lorg/mozilla/javascript/Scriptable;Ljava/lang/String;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;)Lorg/mozilla/javascript/Scriptable;");
                                    this.cfw.addAStore(localBlockRegister);
                                    return;
                                }
                            }
                            break;
                        }
                        case 51: {
                            if (this.compilerEnv.isGenerateObserverCount()) {
                                this.addInstructionCount();
                            }
                            this.cfw.addALoad(this.getLocalBlockRegister(next));
                            this.cfw.add(191);
                            return;
                        }
                        case 50: {
                            this.generateExpression(node, next);
                            if (this.compilerEnv.isGenerateObserverCount()) {
                                this.addInstructionCount();
                            }
                            this.generateThrowJavaScriptException();
                            return;
                        }
                    }
                    break;
                }
                case 5:
                case 6:
                case 7: {
                    if (this.compilerEnv.isGenerateObserverCount()) {
                        this.addInstructionCount();
                    }
                    this.visitGoto((Jump)next, type, node);
                    return;
                }
                case 4: {
                    if (!this.isGenerator) {
                        if (node != null) {
                            this.generateExpression(node, next);
                        }
                        else if (type == 4) {
                            Codegen.pushUndefined(this.cfw);
                        }
                        else {
                            if (this.popvLocal < 0) {
                                throw Codegen.badTree();
                            }
                            this.cfw.addALoad(this.popvLocal);
                        }
                    }
                    if (this.compilerEnv.isGenerateObserverCount()) {
                        this.addInstructionCount();
                    }
                    if (this.epilogueLabel == -1) {
                        if (!this.hasVarsInRegs) {
                            throw Codegen.badTree();
                        }
                        this.epilogueLabel = this.cfw.acquireLabel();
                    }
                    this.cfw.add(167, this.epilogueLabel);
                    return;
                }
                case 3: {
                    this.cfw.addALoad(this.variableObjectLocal);
                    this.addScriptRuntimeInvoke("leaveWith", "(Lorg/mozilla/javascript/Scriptable;)Lorg/mozilla/javascript/Scriptable;");
                    this.cfw.addAStore(this.variableObjectLocal);
                    this.decReferenceWordLocal(this.variableObjectLocal);
                    return;
                }
                case 2: {
                    this.generateExpression(node, next);
                    this.cfw.addALoad(this.contextLocal);
                    this.cfw.addALoad(this.variableObjectLocal);
                    this.addScriptRuntimeInvoke("enterWith", "(Ljava/lang/Object;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;)Lorg/mozilla/javascript/Scriptable;");
                    this.cfw.addAStore(this.variableObjectLocal);
                    this.incReferenceWordLocal(this.variableObjectLocal);
                    break;
                }
            }
        }
    }
    
    private void generateThrowJavaScriptException() {
        this.cfw.add(187, "org/mozilla/javascript/JavaScriptException");
        this.cfw.add(90);
        this.cfw.add(95);
        this.cfw.addPush(this.scriptOrFn.getSourceName());
        this.cfw.addPush(this.itsLineNumber);
        this.cfw.addInvoke(183, "org/mozilla/javascript/JavaScriptException", "<init>", "(Ljava/lang/Object;Ljava/lang/String;I)V");
        this.cfw.add(191);
    }
    
    private void generateYieldPoint(final Node node, final boolean b) {
        final short stackTop = this.cfw.getStackTop();
        int maxStack;
        if (this.maxStack > stackTop) {
            maxStack = this.maxStack;
        }
        else {
            maxStack = stackTop;
        }
        this.maxStack = maxStack;
        final short stackTop2 = this.cfw.getStackTop();
        final short n = 0;
        if (stackTop2 != 0) {
            this.generateGetGeneratorStackState();
            for (int i = 0; i < stackTop; ++i) {
                this.cfw.add(90);
                this.cfw.add(95);
                this.cfw.addLoadConstant(i);
                this.cfw.add(95);
                this.cfw.add(83);
            }
            this.cfw.add(87);
        }
        final Node firstChild = node.getFirstChild();
        if (firstChild != null) {
            this.generateExpression(firstChild, node);
        }
        else {
            Codegen.pushUndefined(this.cfw);
        }
        final int nextGeneratorState = this.getNextGeneratorState(node);
        this.generateSetGeneratorResumptionPoint(nextGeneratorState);
        final boolean generateSaveLocals = this.generateSaveLocals(node);
        this.cfw.add(176);
        this.generateCheckForThrowOrClose(this.getTargetLabel(node), generateSaveLocals, nextGeneratorState);
        if (stackTop != 0) {
            this.generateGetGeneratorStackState();
            for (int j = n; j < stackTop; ++j) {
                this.cfw.add(89);
                this.cfw.addLoadConstant(stackTop - j - 1);
                this.cfw.add(50);
                this.cfw.add(95);
            }
            this.cfw.add(87);
        }
        if (b) {
            this.cfw.addALoad(this.argsLocal);
        }
    }
    
    private Node getFinallyAtTarget(Node next) {
        if (next == null) {
            return null;
        }
        if (next.getType() == 125) {
            return next;
        }
        if (next != null && next.getType() == 131) {
            next = next.getNext();
            if (next != null && next.getType() == 125) {
                return next;
            }
        }
        throw Kit.codeBug("bad finally target");
    }
    
    private int getLocalBlockRegister(final Node node) {
        return ((Node)node.getProp(3)).getExistingIntProp(2);
    }
    
    private short getNewWordIntern(int i) {
        final int[] locals = this.locals;
        final short n = -1;
        short firstFreeLocal2 = 0;
        if (i > 1) {
            int firstFreeLocal = this.firstFreeLocal;
        Label_0019:
            while (true) {
                firstFreeLocal2 = n;
                if (firstFreeLocal + i <= 1024) {
                    for (int j = 0; j < i; ++j) {
                        if (locals[firstFreeLocal + j] != 0) {
                            firstFreeLocal += j + 1;
                            continue Label_0019;
                        }
                    }
                    firstFreeLocal2 = (short)firstFreeLocal;
                    break;
                }
                break;
            }
        }
        else {
            firstFreeLocal2 = this.firstFreeLocal;
        }
        if (firstFreeLocal2 != -1) {
            locals[firstFreeLocal2] = 1;
            if (i > 1) {
                locals[firstFreeLocal2 + 1] = 1;
            }
            if (i > 2) {
                locals[firstFreeLocal2 + 2] = 1;
            }
            if (firstFreeLocal2 != this.firstFreeLocal) {
                return firstFreeLocal2;
            }
            for (i += firstFreeLocal2; i < 1024; ++i) {
                if (locals[i] == 0) {
                    this.firstFreeLocal = (short)i;
                    if (this.localsMax < this.firstFreeLocal) {
                        this.localsMax = this.firstFreeLocal;
                    }
                    return firstFreeLocal2;
                }
            }
        }
        throw Context.reportRuntimeError("Program too complex (out of locals)");
    }
    
    private short getNewWordLocal() {
        return this.getNewWordIntern(1);
    }
    
    private short getNewWordLocal(final boolean b) {
        int n;
        if (b) {
            n = 2;
        }
        else {
            n = 1;
        }
        return this.getNewWordIntern(n);
    }
    
    private short getNewWordPairLocal(final boolean b) {
        int n;
        if (b) {
            n = 3;
        }
        else {
            n = 2;
        }
        return this.getNewWordIntern(n);
    }
    
    private int getNextGeneratorState(final Node node) {
        return ((FunctionNode)this.scriptOrFn).getResumptionPoints().indexOf(node) + 1;
    }
    
    private int getTargetLabel(final Node node) {
        int n;
        if ((n = node.labelId()) == -1) {
            n = this.cfw.acquireLabel();
            node.labelId(n);
        }
        return n;
    }
    
    private void incReferenceWordLocal(final short n) {
        final int[] locals = this.locals;
        ++locals[n];
    }
    
    private void initBodyGeneration() {
        this.varRegisters = null;
        if (this.scriptOrFn.getType() == 109) {
            this.fnCurrent = OptFunctionNode.get(this.scriptOrFn);
            this.hasVarsInRegs = (this.fnCurrent.fnode.requiresActivation() ^ true);
            if (this.hasVarsInRegs) {
                final int paramAndVarCount = this.fnCurrent.fnode.getParamAndVarCount();
                if (paramAndVarCount != 0) {
                    this.varRegisters = new short[paramAndVarCount];
                }
            }
            this.inDirectCallFunction = this.fnCurrent.isTargetOfDirectCall();
            if (this.inDirectCallFunction && !this.hasVarsInRegs) {
                Codegen.badTree();
            }
        }
        else {
            this.fnCurrent = null;
            this.hasVarsInRegs = false;
            this.inDirectCallFunction = false;
        }
        this.locals = new int[1024];
        this.funObjLocal = 0;
        this.contextLocal = 1;
        this.variableObjectLocal = 2;
        this.thisObjLocal = 3;
        this.localsMax = 4;
        this.firstFreeLocal = 4;
        this.popvLocal = -1;
        this.argsLocal = -1;
        this.itsZeroArgArray = -1;
        this.itsOneArgArray = -1;
        this.epilogueLabel = -1;
        this.enterAreaStartLabel = -1;
        this.generatorStateLocal = -1;
    }
    
    private void inlineFinally(final Node node) {
        final int acquireLabel = this.cfw.acquireLabel();
        final int acquireLabel2 = this.cfw.acquireLabel();
        this.cfw.markLabel(acquireLabel);
        this.inlineFinally(node, acquireLabel, acquireLabel2);
        this.cfw.markLabel(acquireLabel2);
    }
    
    private void inlineFinally(Node node, final int n, final int n2) {
        final Node finallyAtTarget = this.getFinallyAtTarget(node);
        finallyAtTarget.resetTargets();
        node = finallyAtTarget.getFirstChild();
        this.exceptionManager.markInlineFinallyStart(finallyAtTarget, n);
        while (node != null) {
            this.generateStatement(node);
            node = node.getNext();
        }
        this.exceptionManager.markInlineFinallyEnd(finallyAtTarget, n2);
    }
    
    private static boolean isArithmeticNode(final Node node) {
        final int type = node.getType();
        return type == 22 || type == 25 || type == 24 || type == 23;
    }
    
    private int nodeIsDirectCallParameter(final Node node) {
        if (node.getType() == 55 && this.inDirectCallFunction && !this.itsForcedObjectParameters) {
            final int varIndex = this.fnCurrent.getVarIndex(node);
            if (this.fnCurrent.isParameter(varIndex)) {
                return this.varRegisters[varIndex];
            }
        }
        return -1;
    }
    
    private void releaseWordLocal(final short firstFreeLocal) {
        if (firstFreeLocal < this.firstFreeLocal) {
            this.firstFreeLocal = firstFreeLocal;
        }
        this.locals[firstFreeLocal] = 0;
    }
    
    private void saveCurrentCodeOffset() {
        this.savedCodeOffset = this.cfw.getCurrentCodeOffset();
    }
    
    private void updateLineNumber(final Node node) {
        this.itsLineNumber = node.getLineno();
        if (this.itsLineNumber == -1) {
            return;
        }
        this.cfw.addLineNumberEntry((short)this.itsLineNumber);
    }
    
    private boolean varIsDirectCallParameter(final int n) {
        return this.fnCurrent.isParameter(n) && this.inDirectCallFunction && !this.itsForcedObjectParameters;
    }
    
    private void visitArithmetic(final Node node, final int n, final Node node2, final Node node3) {
        if (node.getIntProp(8, -1) != -1) {
            this.generateExpression(node2, node);
            this.generateExpression(node2.getNext(), node);
            this.cfw.add(n);
            return;
        }
        final boolean arithmeticNode = isArithmeticNode(node3);
        this.generateExpression(node2, node);
        if (!isArithmeticNode(node2)) {
            this.addObjectToDouble();
        }
        this.generateExpression(node2.getNext(), node);
        if (!isArithmeticNode(node2.getNext())) {
            this.addObjectToDouble();
        }
        this.cfw.add(n);
        if (!arithmeticNode) {
            this.addDoubleWrap();
        }
    }
    
    private void visitArrayLiteral(final Node node, Node node2, final boolean b) {
        int n = 0;
        for (Node next = node2; next != null; next = next.getNext()) {
            ++n;
        }
        if (!b && (n > 10 || this.cfw.getCurrentCodeOffset() > 30000) && !this.hasVarsInRegs && !this.isGenerator && !this.inLocalBlock) {
            if (this.literals == null) {
                this.literals = new LinkedList<Node>();
            }
            this.literals.add(node);
            final StringBuilder sb = new StringBuilder();
            sb.append(this.codegen.getBodyMethodName(this.scriptOrFn));
            sb.append("_literal");
            sb.append(this.literals.size());
            final String string = sb.toString();
            this.cfw.addALoad(this.funObjLocal);
            this.cfw.addALoad(this.contextLocal);
            this.cfw.addALoad(this.variableObjectLocal);
            this.cfw.addALoad(this.thisObjLocal);
            this.cfw.addALoad(this.argsLocal);
            this.cfw.addInvoke(182, this.codegen.mainClassName, string, "(Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;Lorg/mozilla/javascript/Scriptable;[Ljava/lang/Object;)Lorg/mozilla/javascript/Scriptable;");
            return;
        }
        final boolean isGenerator = this.isGenerator;
        int i = 0;
        final int n2 = 0;
        if (isGenerator) {
            for (int j = 0; j != n; ++j) {
                this.generateExpression(node2, node);
                node2 = node2.getNext();
            }
            this.addNewObjectArray(n);
            for (int k = n2; k != n; ++k) {
                this.cfw.add(90);
                this.cfw.add(95);
                this.cfw.addPush(n - k - 1);
                this.cfw.add(95);
                this.cfw.add(83);
            }
        }
        else {
            this.addNewObjectArray(n);
            while (i != n) {
                this.cfw.add(89);
                this.cfw.addPush(i);
                this.generateExpression(node2, node);
                this.cfw.add(83);
                node2 = node2.getNext();
                ++i;
            }
        }
        final int[] array = (int[])node.getProp(11);
        if (array == null) {
            this.cfw.add(1);
            this.cfw.add(3);
        }
        else {
            this.cfw.addPush(OptRuntime.encodeIntArray(array));
            this.cfw.addPush(array.length);
        }
        this.cfw.addALoad(this.contextLocal);
        this.cfw.addALoad(this.variableObjectLocal);
        this.addOptRuntimeInvoke("newArrayLiteral", "([Ljava/lang/Object;Ljava/lang/String;ILorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;)Lorg/mozilla/javascript/Scriptable;");
    }
    
    private void visitBitOp(final Node node, final int n, final Node node2) {
        final int intProp = node.getIntProp(8, -1);
        this.generateExpression(node2, node);
        if (n == 20) {
            this.addScriptRuntimeInvoke("toUint32", "(Ljava/lang/Object;)J");
            this.generateExpression(node2.getNext(), node);
            this.addScriptRuntimeInvoke("toInt32", "(Ljava/lang/Object;)I");
            this.cfw.addPush(31);
            this.cfw.add(126);
            this.cfw.add(125);
            this.cfw.add(138);
            this.addDoubleWrap();
            return;
        }
        if (intProp == -1) {
            this.addScriptRuntimeInvoke("toInt32", "(Ljava/lang/Object;)I");
            this.generateExpression(node2.getNext(), node);
            this.addScriptRuntimeInvoke("toInt32", "(Ljava/lang/Object;)I");
        }
        else {
            this.addScriptRuntimeInvoke("toInt32", "(D)I");
            this.generateExpression(node2.getNext(), node);
            this.addScriptRuntimeInvoke("toInt32", "(D)I");
        }
        Label_0275: {
            switch (n) {
                default: {
                    switch (n) {
                        default: {
                            throw Codegen.badTree();
                        }
                        case 19: {
                            this.cfw.add(122);
                            break Label_0275;
                        }
                        case 18: {
                            this.cfw.add(120);
                            break Label_0275;
                        }
                    }
                    break;
                }
                case 11: {
                    this.cfw.add(126);
                    break;
                }
                case 10: {
                    this.cfw.add(130);
                    break;
                }
                case 9: {
                    this.cfw.add(128);
                    break;
                }
            }
        }
        this.cfw.add(135);
        if (intProp == -1) {
            this.addDoubleWrap();
        }
    }
    
    private void visitDotQuery(final Node node, final Node node2) {
        this.updateLineNumber(node);
        this.generateExpression(node2, node);
        this.cfw.addALoad(this.variableObjectLocal);
        this.addScriptRuntimeInvoke("enterDotQuery", "(Ljava/lang/Object;Lorg/mozilla/javascript/Scriptable;)Lorg/mozilla/javascript/Scriptable;");
        this.cfw.addAStore(this.variableObjectLocal);
        this.cfw.add(1);
        final int acquireLabel = this.cfw.acquireLabel();
        this.cfw.markLabel(acquireLabel);
        this.cfw.add(87);
        this.generateExpression(node2.getNext(), node);
        this.addScriptRuntimeInvoke("toBoolean", "(Ljava/lang/Object;)Z");
        this.cfw.addALoad(this.variableObjectLocal);
        this.addScriptRuntimeInvoke("updateDotQuery", "(ZLorg/mozilla/javascript/Scriptable;)Ljava/lang/Object;");
        this.cfw.add(89);
        this.cfw.add(198, acquireLabel);
        this.cfw.addALoad(this.variableObjectLocal);
        this.addScriptRuntimeInvoke("leaveDotQuery", "(Lorg/mozilla/javascript/Scriptable;)Lorg/mozilla/javascript/Scriptable;");
        this.cfw.addAStore(this.variableObjectLocal);
    }
    
    private void visitFunction(final OptFunctionNode optFunctionNode, final int n) {
        final int index = this.codegen.getIndex(optFunctionNode.fnode);
        this.cfw.add(187, this.codegen.mainClassName);
        this.cfw.add(89);
        this.cfw.addALoad(this.variableObjectLocal);
        this.cfw.addALoad(this.contextLocal);
        this.cfw.addPush(index);
        this.cfw.addInvoke(183, this.codegen.mainClassName, "<init>", "(Lorg/mozilla/javascript/Scriptable;Lorg/mozilla/javascript/Context;I)V");
        if (n == 2) {
            return;
        }
        this.cfw.addPush(n);
        this.cfw.addALoad(this.variableObjectLocal);
        this.cfw.addALoad(this.contextLocal);
        this.addOptRuntimeInvoke("initFunction", "(Lorg/mozilla/javascript/NativeFunction;ILorg/mozilla/javascript/Scriptable;Lorg/mozilla/javascript/Context;)V");
    }
    
    private void visitGetProp(final Node node, final Node node2) {
        this.generateExpression(node2, node);
        final Node next = node2.getNext();
        this.generateExpression(next, node);
        if (node.getType() == 34) {
            this.cfw.addALoad(this.contextLocal);
            this.cfw.addALoad(this.variableObjectLocal);
            this.addScriptRuntimeInvoke("getObjectPropNoWarn", "(Ljava/lang/Object;Ljava/lang/String;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;)Ljava/lang/Object;");
            return;
        }
        if (node2.getType() == 43 && next.getType() == 41) {
            this.cfw.addALoad(this.contextLocal);
            this.addScriptRuntimeInvoke("getObjectProp", "(Lorg/mozilla/javascript/Scriptable;Ljava/lang/String;Lorg/mozilla/javascript/Context;)Ljava/lang/Object;");
            return;
        }
        this.cfw.addALoad(this.contextLocal);
        this.cfw.addALoad(this.variableObjectLocal);
        this.addScriptRuntimeInvoke("getObjectProp", "(Ljava/lang/Object;Ljava/lang/String;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;)Ljava/lang/Object;");
    }
    
    private void visitGetVar(final Node node) {
        if (!this.hasVarsInRegs) {
            Kit.codeBug();
        }
        final int varIndex = this.fnCurrent.getVarIndex(node);
        final short n = this.varRegisters[varIndex];
        if (this.varIsDirectCallParameter(varIndex)) {
            if (node.getIntProp(8, -1) != -1) {
                this.dcpLoadAsNumber(n);
                return;
            }
            this.dcpLoadAsObject(n);
        }
        else {
            if (this.fnCurrent.isNumberVar(varIndex)) {
                this.cfw.addDLoad(n);
                return;
            }
            this.cfw.addALoad(n);
        }
    }
    
    private void visitGoto(final Jump jump, final int n, final Node node) {
        final Node target = jump.target;
        if (n != 6 && n != 7) {
            if (n != 135) {
                this.addGoto(target, 167);
                return;
            }
            if (this.isGenerator) {
                this.addGotoWithReturn(target);
                return;
            }
            this.inlineFinally(target);
        }
        else {
            if (node == null) {
                throw Codegen.badTree();
            }
            final int targetLabel = this.getTargetLabel(target);
            final int acquireLabel = this.cfw.acquireLabel();
            if (n == 6) {
                this.generateIfJump(node, jump, targetLabel, acquireLabel);
            }
            else {
                this.generateIfJump(node, jump, acquireLabel, targetLabel);
            }
            this.cfw.markLabel(acquireLabel);
        }
    }
    
    private void visitIfJumpEqOp(final Node node, Node firstChild, int nodeIsDirectCallParameter, int acquireLabel) {
        Node node2 = firstChild;
        final int n = nodeIsDirectCallParameter;
        if (n == -1 || acquireLabel == -1) {
            throw Codegen.badTree();
        }
        final short stackTop = this.cfw.getStackTop();
        final int type = node.getType();
        final Node next = firstChild.getNext();
        if (firstChild.getType() != 42 && next.getType() != 42) {
            nodeIsDirectCallParameter = this.nodeIsDirectCallParameter(node2);
            if (nodeIsDirectCallParameter != -1 && next.getType() == 149) {
                firstChild = next.getFirstChild();
                if (firstChild.getType() == 40) {
                    this.cfw.addALoad(nodeIsDirectCallParameter);
                    this.cfw.add(178, "java/lang/Void", "TYPE", "Ljava/lang/Class;");
                    final int acquireLabel2 = this.cfw.acquireLabel();
                    this.cfw.add(166, acquireLabel2);
                    this.cfw.addDLoad(nodeIsDirectCallParameter + 1);
                    this.cfw.addPush(firstChild.getDouble());
                    this.cfw.add(151);
                    if (type == 12) {
                        this.cfw.add(153, n);
                    }
                    else {
                        this.cfw.add(154, n);
                    }
                    this.cfw.add(167, acquireLabel);
                    this.cfw.markLabel(acquireLabel2);
                }
            }
            this.generateExpression(node2, node);
            this.generateExpression(next, node);
            String s = null;
            switch (type) {
                default: {
                    throw Codegen.badTree();
                }
                case 47: {
                    s = "shallowEq";
                    nodeIsDirectCallParameter = 153;
                    break;
                }
                case 46: {
                    s = "shallowEq";
                    nodeIsDirectCallParameter = 154;
                    break;
                }
                case 13: {
                    s = "eq";
                    nodeIsDirectCallParameter = 153;
                    break;
                }
                case 12: {
                    s = "eq";
                    nodeIsDirectCallParameter = 154;
                    break;
                }
            }
            this.addScriptRuntimeInvoke(s, "(Ljava/lang/Object;Ljava/lang/Object;)Z");
            this.cfw.add(nodeIsDirectCallParameter, n);
            this.cfw.add(167, acquireLabel);
        }
        else {
            if (firstChild.getType() == 42) {
                node2 = next;
            }
            this.generateExpression(node2, node);
            nodeIsDirectCallParameter = 199;
            if (type != 46 && type != 47) {
                int n2 = n;
                nodeIsDirectCallParameter = acquireLabel;
                if (type != 12) {
                    if (type != 13) {
                        throw Codegen.badTree();
                    }
                    nodeIsDirectCallParameter = n;
                    n2 = acquireLabel;
                }
                this.cfw.add(89);
                acquireLabel = this.cfw.acquireLabel();
                this.cfw.add(199, acquireLabel);
                final short stackTop2 = this.cfw.getStackTop();
                this.cfw.add(87);
                this.cfw.add(167, n2);
                this.cfw.markLabel(acquireLabel, stackTop2);
                Codegen.pushUndefined(this.cfw);
                this.cfw.add(165, n2);
            }
            else {
                if (type == 46) {
                    nodeIsDirectCallParameter = 198;
                }
                this.cfw.add(nodeIsDirectCallParameter, n);
                nodeIsDirectCallParameter = acquireLabel;
            }
            this.cfw.add(167, nodeIsDirectCallParameter);
        }
        if (stackTop != this.cfw.getStackTop()) {
            throw Codegen.badTree();
        }
    }
    
    private void visitIfJumpRelOp(final Node node, final Node node2, final int n, final int n2) {
        if (n == -1 || n2 == -1) {
            throw Codegen.badTree();
        }
        final int type = node.getType();
        final Node next = node2.getNext();
        if (type == 53 || type == 52) {
            this.generateExpression(node2, node);
            this.generateExpression(next, node);
            this.cfw.addALoad(this.contextLocal);
            String s;
            if (type == 53) {
                s = "instanceOf";
            }
            else {
                s = "in";
            }
            this.addScriptRuntimeInvoke(s, "(Ljava/lang/Object;Ljava/lang/Object;Lorg/mozilla/javascript/Context;)Z");
            this.cfw.add(154, n);
            this.cfw.add(167, n2);
            return;
        }
        final int intProp = node.getIntProp(8, -1);
        final int nodeIsDirectCallParameter = this.nodeIsDirectCallParameter(node2);
        final int nodeIsDirectCallParameter2 = this.nodeIsDirectCallParameter(next);
        if (intProp != -1) {
            if (intProp != 2) {
                this.generateExpression(node2, node);
            }
            else if (nodeIsDirectCallParameter != -1) {
                this.dcpLoadAsNumber(nodeIsDirectCallParameter);
            }
            else {
                this.generateExpression(node2, node);
                this.addObjectToDouble();
            }
            if (intProp != 1) {
                this.generateExpression(next, node);
            }
            else if (nodeIsDirectCallParameter2 != -1) {
                this.dcpLoadAsNumber(nodeIsDirectCallParameter2);
            }
            else {
                this.generateExpression(next, node);
                this.addObjectToDouble();
            }
            this.genSimpleCompare(type, n, n2);
            return;
        }
        if (nodeIsDirectCallParameter != -1 && nodeIsDirectCallParameter2 != -1) {
            final short stackTop = this.cfw.getStackTop();
            final int acquireLabel = this.cfw.acquireLabel();
            this.cfw.addALoad(nodeIsDirectCallParameter);
            this.cfw.add(178, "java/lang/Void", "TYPE", "Ljava/lang/Class;");
            this.cfw.add(166, acquireLabel);
            this.cfw.addDLoad(nodeIsDirectCallParameter + 1);
            this.dcpLoadAsNumber(nodeIsDirectCallParameter2);
            this.genSimpleCompare(type, n, n2);
            if (stackTop != this.cfw.getStackTop()) {
                throw Codegen.badTree();
            }
            this.cfw.markLabel(acquireLabel);
            final int acquireLabel2 = this.cfw.acquireLabel();
            this.cfw.addALoad(nodeIsDirectCallParameter2);
            this.cfw.add(178, "java/lang/Void", "TYPE", "Ljava/lang/Class;");
            this.cfw.add(166, acquireLabel2);
            this.cfw.addALoad(nodeIsDirectCallParameter);
            this.addObjectToDouble();
            this.cfw.addDLoad(nodeIsDirectCallParameter2 + 1);
            this.genSimpleCompare(type, n, n2);
            if (stackTop != this.cfw.getStackTop()) {
                throw Codegen.badTree();
            }
            this.cfw.markLabel(acquireLabel2);
            this.cfw.addALoad(nodeIsDirectCallParameter);
            this.cfw.addALoad(nodeIsDirectCallParameter2);
        }
        else {
            this.generateExpression(node2, node);
            this.generateExpression(next, node);
        }
        if (type == 17 || type == 16) {
            this.cfw.add(95);
        }
        String s2;
        if (type != 14 && type != 16) {
            s2 = "cmp_LE";
        }
        else {
            s2 = "cmp_LT";
        }
        this.addScriptRuntimeInvoke(s2, "(Ljava/lang/Object;Ljava/lang/Object;)Z");
        this.cfw.add(154, n);
        this.cfw.add(167, n2);
    }
    
    private void visitIncDec(final Node node) {
        throw new Runtime("d2j fail translate: java.lang.RuntimeException: can not merge I and Z\r\n\tat com.googlecode.dex2jar.ir.TypeClass.merge(TypeClass.java:100)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeRef.updateTypeClass(TypeTransformer.java:174)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.copyTypes(TypeTransformer.java:311)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.fixTypes(TypeTransformer.java:226)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.analyze(TypeTransformer.java:207)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer.transform(TypeTransformer.java:44)\r\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:162)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\r\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\r\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\r\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\r\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\r\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\r\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\r\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\r\n");
    }
    
    private void visitObjectLiteral(Node next, Node next2, final boolean b) {
        final Object[] array = (Object[])next.getProp(12);
        final int length = array.length;
        if (!b && (length > 10 || this.cfw.getCurrentCodeOffset() > 30000) && !this.hasVarsInRegs && !this.isGenerator && !this.inLocalBlock) {
            if (this.literals == null) {
                this.literals = new LinkedList<Node>();
            }
            this.literals.add(next);
            final StringBuilder sb = new StringBuilder();
            sb.append(this.codegen.getBodyMethodName(this.scriptOrFn));
            sb.append("_literal");
            sb.append(this.literals.size());
            final String string = sb.toString();
            this.cfw.addALoad(this.funObjLocal);
            this.cfw.addALoad(this.contextLocal);
            this.cfw.addALoad(this.variableObjectLocal);
            this.cfw.addALoad(this.thisObjLocal);
            this.cfw.addALoad(this.argsLocal);
            this.cfw.addInvoke(182, this.codegen.mainClassName, string, "(Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;Lorg/mozilla/javascript/Scriptable;[Ljava/lang/Object;)Lorg/mozilla/javascript/Scriptable;");
            return;
        }
        if (this.isGenerator) {
            this.addLoadPropertyValues(next, next2, length);
            this.addLoadPropertyIds(array, length);
            this.cfw.add(95);
        }
        else {
            this.addLoadPropertyIds(array, length);
            this.addLoadPropertyValues(next, next2, length);
        }
        final boolean b2 = false;
        final int n = 0;
        next = next2;
        int n2 = 0;
        boolean b3;
        while (true) {
            b3 = b2;
            if (n2 == length) {
                break;
            }
            final int type = next.getType();
            if (type == 151 || type == 152) {
                b3 = true;
                break;
            }
            next = next.getNext();
            ++n2;
        }
        if (b3) {
            this.cfw.addPush(length);
            this.cfw.add(188, 10);
            for (int i = n; i != length; ++i) {
                this.cfw.add(89);
                this.cfw.addPush(i);
                final int type2 = next2.getType();
                if (type2 == 151) {
                    this.cfw.add(2);
                }
                else if (type2 == 152) {
                    this.cfw.add(4);
                }
                else {
                    this.cfw.add(3);
                }
                this.cfw.add(79);
                next2 = next2.getNext();
            }
        }
        else {
            this.cfw.add(1);
        }
        this.cfw.addALoad(this.contextLocal);
        this.cfw.addALoad(this.variableObjectLocal);
        this.addScriptRuntimeInvoke("newObjectLiteral", "([Ljava/lang/Object;[Ljava/lang/Object;[ILorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;)Lorg/mozilla/javascript/Scriptable;");
    }
    
    private void visitOptimizedCall(final Node node, final OptFunctionNode optFunctionNode, final int n, Node next) {
        final Node next2 = next.getNext();
        final String mainClassName = this.codegen.mainClassName;
        short newWordLocal = 0;
        if (n == 30) {
            this.generateExpression(next, node);
        }
        else {
            this.generateFunctionAndThisObj(next, node);
            newWordLocal = this.getNewWordLocal();
            this.cfw.addAStore(newWordLocal);
        }
        final int acquireLabel = this.cfw.acquireLabel();
        final int acquireLabel2 = this.cfw.acquireLabel();
        this.cfw.add(89);
        this.cfw.add(193, mainClassName);
        this.cfw.add(153, acquireLabel2);
        this.cfw.add(192, mainClassName);
        this.cfw.add(89);
        this.cfw.add(180, mainClassName, "_id", "I");
        this.cfw.addPush(this.codegen.getIndex(optFunctionNode.fnode));
        this.cfw.add(160, acquireLabel2);
        this.cfw.addALoad(this.contextLocal);
        this.cfw.addALoad(this.variableObjectLocal);
        if (n == 30) {
            this.cfw.add(1);
        }
        else {
            this.cfw.addALoad(newWordLocal);
        }
        int nodeIsDirectCallParameter;
        for (next = next2; next != null; next = next.getNext()) {
            nodeIsDirectCallParameter = this.nodeIsDirectCallParameter(next);
            if (nodeIsDirectCallParameter >= 0) {
                this.cfw.addALoad(nodeIsDirectCallParameter);
                this.cfw.addDLoad(nodeIsDirectCallParameter + 1);
            }
            else if (next.getIntProp(8, -1) == 0) {
                this.cfw.add(178, "java/lang/Void", "TYPE", "Ljava/lang/Class;");
                this.generateExpression(next, node);
            }
            else {
                this.generateExpression(next, node);
                this.cfw.addPush(0.0);
            }
        }
        this.cfw.add(178, "org/mozilla/javascript/ScriptRuntime", "emptyArgs", "[Ljava/lang/Object;");
        final ClassFileWriter cfw = this.cfw;
        final String mainClassName2 = this.codegen.mainClassName;
        String s;
        if (n == 30) {
            s = this.codegen.getDirectCtorName(optFunctionNode.fnode);
        }
        else {
            s = this.codegen.getBodyMethodName(optFunctionNode.fnode);
        }
        cfw.addInvoke(184, mainClassName2, s, this.codegen.getBodyMethodSignature(optFunctionNode.fnode));
        this.cfw.add(167, acquireLabel);
        this.cfw.markLabel(acquireLabel2);
        this.cfw.addALoad(this.contextLocal);
        this.cfw.addALoad(this.variableObjectLocal);
        if (n != 30) {
            this.cfw.addALoad(newWordLocal);
            this.releaseWordLocal(newWordLocal);
        }
        this.generateCallArgArray(node, next2, true);
        if (n == 30) {
            this.addScriptRuntimeInvoke("newObject", "(Ljava/lang/Object;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;[Ljava/lang/Object;)Lorg/mozilla/javascript/Scriptable;");
        }
        else {
            this.cfw.addInvoke(185, "org/mozilla/javascript/Callable", "call", "(Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;Lorg/mozilla/javascript/Scriptable;[Ljava/lang/Object;)Ljava/lang/Object;");
        }
        this.cfw.markLabel(acquireLabel);
    }
    
    private void visitSetConst(final Node node, Node next) {
        final String string = node.getFirstChild().getString();
        while (next != null) {
            this.generateExpression(next, node);
            next = next.getNext();
        }
        this.cfw.addALoad(this.contextLocal);
        this.cfw.addPush(string);
        this.addScriptRuntimeInvoke("setConst", "(Lorg/mozilla/javascript/Scriptable;Ljava/lang/Object;Lorg/mozilla/javascript/Context;Ljava/lang/String;)Ljava/lang/Object;");
    }
    
    private void visitSetConstVar(final Node node, final Node node2, final boolean b) {
        if (!this.hasVarsInRegs) {
            Kit.codeBug();
        }
        final int varIndex = this.fnCurrent.getVarIndex(node);
        this.generateExpression(node2.getNext(), node);
        final boolean b2 = node.getIntProp(8, -1) != -1;
        final short n = this.varRegisters[varIndex];
        final int acquireLabel = this.cfw.acquireLabel();
        final int acquireLabel2 = this.cfw.acquireLabel();
        if (b2) {
            this.cfw.addILoad(n + 2);
            this.cfw.add(154, acquireLabel2);
            final short stackTop = this.cfw.getStackTop();
            this.cfw.addPush(1);
            this.cfw.addIStore(n + 2);
            this.cfw.addDStore(n);
            if (b) {
                this.cfw.addDLoad(n);
                this.cfw.markLabel(acquireLabel2, stackTop);
            }
            else {
                this.cfw.add(167, acquireLabel);
                this.cfw.markLabel(acquireLabel2, stackTop);
                this.cfw.add(88);
            }
        }
        else {
            this.cfw.addILoad(n + 1);
            this.cfw.add(154, acquireLabel2);
            final short stackTop2 = this.cfw.getStackTop();
            this.cfw.addPush(1);
            this.cfw.addIStore(n + 1);
            this.cfw.addAStore(n);
            if (b) {
                this.cfw.addALoad(n);
                this.cfw.markLabel(acquireLabel2, stackTop2);
            }
            else {
                this.cfw.add(167, acquireLabel);
                this.cfw.markLabel(acquireLabel2, stackTop2);
                this.cfw.add(87);
            }
        }
        this.cfw.markLabel(acquireLabel);
    }
    
    private void visitSetElem(final int n, final Node node, Node node2) {
        this.generateExpression(node2, node);
        node2 = node2.getNext();
        if (n == 140) {
            this.cfw.add(89);
        }
        this.generateExpression(node2, node);
        node2 = node2.getNext();
        final boolean b = node.getIntProp(8, -1) != -1;
        if (n == 140) {
            if (b) {
                this.cfw.add(93);
                this.cfw.addALoad(this.contextLocal);
                this.cfw.addALoad(this.variableObjectLocal);
                this.addScriptRuntimeInvoke("getObjectIndex", "(Ljava/lang/Object;DLorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;)Ljava/lang/Object;");
            }
            else {
                this.cfw.add(90);
                this.cfw.addALoad(this.contextLocal);
                this.cfw.addALoad(this.variableObjectLocal);
                this.addScriptRuntimeInvoke("getObjectElem", "(Ljava/lang/Object;Ljava/lang/Object;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;)Ljava/lang/Object;");
            }
        }
        this.generateExpression(node2, node);
        this.cfw.addALoad(this.contextLocal);
        this.cfw.addALoad(this.variableObjectLocal);
        if (b) {
            this.addScriptRuntimeInvoke("setObjectIndex", "(Ljava/lang/Object;DLjava/lang/Object;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;)Ljava/lang/Object;");
            return;
        }
        this.addScriptRuntimeInvoke("setObjectElem", "(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;)Ljava/lang/Object;");
    }
    
    private void visitSetName(final Node node, Node next) {
        final String string = node.getFirstChild().getString();
        while (next != null) {
            this.generateExpression(next, node);
            next = next.getNext();
        }
        this.cfw.addALoad(this.contextLocal);
        this.cfw.addALoad(this.variableObjectLocal);
        this.cfw.addPush(string);
        this.addScriptRuntimeInvoke("setName", "(Lorg/mozilla/javascript/Scriptable;Ljava/lang/Object;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;Ljava/lang/String;)Ljava/lang/Object;");
    }
    
    private void visitSetProp(final int n, final Node node, final Node node2) {
        this.generateExpression(node2, node);
        final Node next = node2.getNext();
        if (n == 139) {
            this.cfw.add(89);
        }
        this.generateExpression(next, node);
        final Node next2 = next.getNext();
        if (n == 139) {
            this.cfw.add(90);
            if (node2.getType() == 43 && next.getType() == 41) {
                this.cfw.addALoad(this.contextLocal);
                this.addScriptRuntimeInvoke("getObjectProp", "(Lorg/mozilla/javascript/Scriptable;Ljava/lang/String;Lorg/mozilla/javascript/Context;)Ljava/lang/Object;");
            }
            else {
                this.cfw.addALoad(this.contextLocal);
                this.cfw.addALoad(this.variableObjectLocal);
                this.addScriptRuntimeInvoke("getObjectProp", "(Ljava/lang/Object;Ljava/lang/String;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;)Ljava/lang/Object;");
            }
        }
        this.generateExpression(next2, node);
        this.cfw.addALoad(this.contextLocal);
        this.cfw.addALoad(this.variableObjectLocal);
        this.addScriptRuntimeInvoke("setObjectProp", "(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Object;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;)Ljava/lang/Object;");
    }
    
    private void visitSetVar(final Node node, final Node node2, final boolean b) {
        if (!this.hasVarsInRegs) {
            Kit.codeBug();
        }
        final int varIndex = this.fnCurrent.getVarIndex(node);
        this.generateExpression(node2.getNext(), node);
        final boolean b2 = node.getIntProp(8, -1) != -1;
        final short n = this.varRegisters[varIndex];
        if (this.fnCurrent.fnode.getParamAndVarConst()[varIndex]) {
            if (!b) {
                if (b2) {
                    this.cfw.add(88);
                    return;
                }
                this.cfw.add(87);
            }
        }
        else if (this.varIsDirectCallParameter(varIndex)) {
            if (b2) {
                if (b) {
                    this.cfw.add(92);
                }
                this.cfw.addALoad(n);
                this.cfw.add(178, "java/lang/Void", "TYPE", "Ljava/lang/Class;");
                final int acquireLabel = this.cfw.acquireLabel();
                final int acquireLabel2 = this.cfw.acquireLabel();
                this.cfw.add(165, acquireLabel);
                final short stackTop = this.cfw.getStackTop();
                this.addDoubleWrap();
                this.cfw.addAStore(n);
                this.cfw.add(167, acquireLabel2);
                this.cfw.markLabel(acquireLabel, stackTop);
                this.cfw.addDStore(n + 1);
                this.cfw.markLabel(acquireLabel2);
                return;
            }
            if (b) {
                this.cfw.add(89);
            }
            this.cfw.addAStore(n);
        }
        else {
            final boolean numberVar = this.fnCurrent.isNumberVar(varIndex);
            if (b2) {
                if (!numberVar) {
                    if (b) {
                        this.cfw.add(92);
                    }
                    this.addDoubleWrap();
                    this.cfw.addAStore(n);
                    return;
                }
                this.cfw.addDStore(n);
                if (b) {
                    this.cfw.addDLoad(n);
                }
            }
            else {
                if (numberVar) {
                    Kit.codeBug();
                }
                this.cfw.addAStore(n);
                if (b) {
                    this.cfw.addALoad(n);
                }
            }
        }
    }
    
    private void visitSpecialCall(final Node node, final int n, final int n2, final Node node2) {
        this.cfw.addALoad(this.contextLocal);
        if (n == 30) {
            this.generateExpression(node2, node);
        }
        else {
            this.generateFunctionAndThisObj(node2, node);
        }
        this.generateCallArgArray(node, node2.getNext(), false);
        String s;
        String s2;
        if (n == 30) {
            s = "newObjectSpecial";
            s2 = "(Lorg/mozilla/javascript/Context;Ljava/lang/Object;[Ljava/lang/Object;Lorg/mozilla/javascript/Scriptable;Lorg/mozilla/javascript/Scriptable;I)Ljava/lang/Object;";
            this.cfw.addALoad(this.variableObjectLocal);
            this.cfw.addALoad(this.thisObjLocal);
            this.cfw.addPush(n2);
        }
        else {
            final String s3 = "callSpecial";
            s2 = "(Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Callable;Lorg/mozilla/javascript/Scriptable;[Ljava/lang/Object;Lorg/mozilla/javascript/Scriptable;Lorg/mozilla/javascript/Scriptable;ILjava/lang/String;I)Ljava/lang/Object;";
            this.cfw.addALoad(this.variableObjectLocal);
            this.cfw.addALoad(this.thisObjLocal);
            this.cfw.addPush(n2);
            String sourceName = this.scriptOrFn.getSourceName();
            final ClassFileWriter cfw = this.cfw;
            if (sourceName == null) {
                sourceName = "";
            }
            cfw.addPush(sourceName);
            this.cfw.addPush(this.itsLineNumber);
            s = s3;
        }
        this.addOptRuntimeInvoke(s, s2);
    }
    
    private void visitStandardCall(final Node node, Node firstChild) {
        if (node.getType() != 38) {
            throw Codegen.badTree();
        }
        final Node next = firstChild.getNext();
        final int type = firstChild.getType();
        String s;
        String s2;
        if (next == null) {
            if (type == 39) {
                this.cfw.addPush(firstChild.getString());
                s = "callName0";
                s2 = "(Ljava/lang/String;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;)Ljava/lang/Object;";
            }
            else if (type == 33) {
                firstChild = firstChild.getFirstChild();
                this.generateExpression(firstChild, node);
                this.cfw.addPush(firstChild.getNext().getString());
                s2 = "(Ljava/lang/Object;Ljava/lang/String;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;)Ljava/lang/Object;";
                s = "callProp0";
            }
            else {
                if (type == 34) {
                    throw Kit.codeBug();
                }
                this.generateFunctionAndThisObj(firstChild, node);
                s = "call0";
                s2 = "(Lorg/mozilla/javascript/Callable;Lorg/mozilla/javascript/Scriptable;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;)Ljava/lang/Object;";
            }
        }
        else if (type == 39) {
            final String string = firstChild.getString();
            this.generateCallArgArray(node, next, false);
            this.cfw.addPush(string);
            s = "callName";
            s2 = "([Ljava/lang/Object;Ljava/lang/String;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;)Ljava/lang/Object;";
        }
        else {
            int n = 0;
            for (Node next2 = next; next2 != null; next2 = next2.getNext()) {
                ++n;
            }
            this.generateFunctionAndThisObj(firstChild, node);
            if (n == 1) {
                this.generateExpression(next, node);
                s = "call1";
                s2 = "(Lorg/mozilla/javascript/Callable;Lorg/mozilla/javascript/Scriptable;Ljava/lang/Object;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;)Ljava/lang/Object;";
            }
            else if (n == 2) {
                this.generateExpression(next, node);
                this.generateExpression(next.getNext(), node);
                s = "call2";
                s2 = "(Lorg/mozilla/javascript/Callable;Lorg/mozilla/javascript/Scriptable;Ljava/lang/Object;Ljava/lang/Object;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;)Ljava/lang/Object;";
            }
            else {
                this.generateCallArgArray(node, next, false);
                s = "callN";
                s2 = "(Lorg/mozilla/javascript/Callable;Lorg/mozilla/javascript/Scriptable;[Ljava/lang/Object;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;)Ljava/lang/Object;";
            }
        }
        this.cfw.addALoad(this.contextLocal);
        this.cfw.addALoad(this.variableObjectLocal);
        this.addOptRuntimeInvoke(s, s2);
    }
    
    private void visitStandardNew(final Node node, final Node node2) {
        if (node.getType() != 30) {
            throw Codegen.badTree();
        }
        final Node next = node2.getNext();
        this.generateExpression(node2, node);
        this.cfw.addALoad(this.contextLocal);
        this.cfw.addALoad(this.variableObjectLocal);
        this.generateCallArgArray(node, next, false);
        this.addScriptRuntimeInvoke("newObject", "(Ljava/lang/Object;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;[Ljava/lang/Object;)Lorg/mozilla/javascript/Scriptable;");
    }
    
    private void visitStrictSetName(final Node node, Node next) {
        final String string = node.getFirstChild().getString();
        while (next != null) {
            this.generateExpression(next, node);
            next = next.getNext();
        }
        this.cfw.addALoad(this.contextLocal);
        this.cfw.addALoad(this.variableObjectLocal);
        this.cfw.addPush(string);
        this.addScriptRuntimeInvoke("strictSetName", "(Lorg/mozilla/javascript/Scriptable;Ljava/lang/Object;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;Ljava/lang/String;)Ljava/lang/Object;");
    }
    
    private void visitSwitch(Jump jump, final Node node) {
        this.generateExpression(node, jump);
        final short newWordLocal = this.getNewWordLocal();
        this.cfw.addAStore(newWordLocal);
        for (jump = (Jump)node.getNext(); jump != null; jump = (Jump)jump.getNext()) {
            if (jump.getType() != 115) {
                throw Codegen.badTree();
            }
            this.generateExpression(jump.getFirstChild(), jump);
            this.cfw.addALoad(newWordLocal);
            this.addScriptRuntimeInvoke("shallowEq", "(Ljava/lang/Object;Ljava/lang/Object;)Z");
            this.addGoto(jump.target, 154);
        }
        this.releaseWordLocal(newWordLocal);
    }
    
    private void visitTryCatchFinally(final Jump jump, Node next) {
        final short newWordLocal = this.getNewWordLocal();
        this.cfw.addALoad(this.variableObjectLocal);
        this.cfw.addAStore(newWordLocal);
        final int acquireLabel = this.cfw.acquireLabel();
        this.cfw.markLabel(acquireLabel, (short)0);
        final Node target = jump.target;
        final Node finally1 = jump.getFinally();
        final int[] array = new int[5];
        this.exceptionManager.pushExceptionInfo(jump);
        if (target != null) {
            array[0] = this.cfw.acquireLabel();
            array[1] = this.cfw.acquireLabel();
            array[2] = this.cfw.acquireLabel();
            final Context currentContext = Context.getCurrentContext();
            if (currentContext != null && currentContext.hasFeature(13)) {
                array[3] = this.cfw.acquireLabel();
            }
        }
        if (finally1 != null) {
            array[4] = this.cfw.acquireLabel();
        }
        this.exceptionManager.setHandlers(array, acquireLabel);
        if (this.isGenerator && finally1 != null) {
            final FinallyReturnPoint finallyReturnPoint = new FinallyReturnPoint();
            if (this.finallys == null) {
                this.finallys = new HashMap<Node, FinallyReturnPoint>();
            }
            this.finallys.put(finally1, finallyReturnPoint);
            this.finallys.put(finally1.getNext(), finallyReturnPoint);
        }
        while (next != null) {
            if (next == target) {
                final int targetLabel = this.getTargetLabel(target);
                this.exceptionManager.removeHandler(0, targetLabel);
                this.exceptionManager.removeHandler(1, targetLabel);
                this.exceptionManager.removeHandler(2, targetLabel);
                this.exceptionManager.removeHandler(3, targetLabel);
            }
            this.generateStatement(next);
            next = next.getNext();
        }
        final int acquireLabel2 = this.cfw.acquireLabel();
        this.cfw.add(167, acquireLabel2);
        final int localBlockRegister = this.getLocalBlockRegister(jump);
        if (target != null) {
            final int labelId = target.labelId();
            final int n = array[0];
            final int n2 = localBlockRegister;
            this.generateCatchBlock(0, newWordLocal, labelId, n2, n);
            this.generateCatchBlock(1, newWordLocal, labelId, n2, array[1]);
            this.generateCatchBlock(2, newWordLocal, labelId, n2, array[2]);
            final Context currentContext2 = Context.getCurrentContext();
            if (currentContext2 != null && currentContext2.hasFeature(13)) {
                this.generateCatchBlock(3, newWordLocal, labelId, n2, array[3]);
            }
        }
        if (finally1 != null) {
            final int acquireLabel3 = this.cfw.acquireLabel();
            final int acquireLabel4 = this.cfw.acquireLabel();
            this.cfw.markHandler(acquireLabel3);
            if (!this.isGenerator) {
                this.cfw.markLabel(array[4]);
            }
            this.cfw.addAStore(localBlockRegister);
            this.cfw.addALoad(newWordLocal);
            this.cfw.addAStore(this.variableObjectLocal);
            final int labelId2 = finally1.labelId();
            if (this.isGenerator) {
                this.addGotoWithReturn(finally1);
            }
            else {
                this.inlineFinally(finally1, array[4], acquireLabel4);
            }
            this.cfw.addALoad(localBlockRegister);
            if (this.isGenerator) {
                this.cfw.add(192, "java/lang/Throwable");
            }
            this.cfw.add(191);
            this.cfw.markLabel(acquireLabel4);
            if (this.isGenerator) {
                this.cfw.addExceptionHandler(acquireLabel, labelId2, acquireLabel3, null);
            }
        }
        this.releaseWordLocal(newWordLocal);
        this.cfw.markLabel(acquireLabel2);
        if (!this.isGenerator) {
            this.exceptionManager.popExceptionInfo();
        }
    }
    
    private void visitTypeofname(final Node node) {
        if (this.hasVarsInRegs) {
            final int indexForNameNode = this.fnCurrent.fnode.getIndexForNameNode(node);
            if (indexForNameNode >= 0) {
                if (this.fnCurrent.isNumberVar(indexForNameNode)) {
                    this.cfw.addPush("number");
                    return;
                }
                if (this.varIsDirectCallParameter(indexForNameNode)) {
                    final short n = this.varRegisters[indexForNameNode];
                    this.cfw.addALoad(n);
                    this.cfw.add(178, "java/lang/Void", "TYPE", "Ljava/lang/Class;");
                    final int acquireLabel = this.cfw.acquireLabel();
                    this.cfw.add(165, acquireLabel);
                    final short stackTop = this.cfw.getStackTop();
                    this.cfw.addALoad(n);
                    this.addScriptRuntimeInvoke("typeof", "(Ljava/lang/Object;)Ljava/lang/String;");
                    final int acquireLabel2 = this.cfw.acquireLabel();
                    this.cfw.add(167, acquireLabel2);
                    this.cfw.markLabel(acquireLabel, stackTop);
                    this.cfw.addPush("number");
                    this.cfw.markLabel(acquireLabel2);
                    return;
                }
                this.cfw.addALoad(this.varRegisters[indexForNameNode]);
                this.addScriptRuntimeInvoke("typeof", "(Ljava/lang/Object;)Ljava/lang/String;");
                return;
            }
        }
        this.cfw.addALoad(this.variableObjectLocal);
        this.cfw.addPush(node.getString());
        this.addScriptRuntimeInvoke("typeofName", "(Lorg/mozilla/javascript/Scriptable;Ljava/lang/String;)Ljava/lang/String;");
    }
    
    void generateBodyCode() {
        this.isGenerator = Codegen.isGenerator(this.scriptOrFn);
        this.initBodyGeneration();
        if (this.isGenerator) {
            final StringBuilder sb = new StringBuilder();
            sb.append("(");
            sb.append(this.codegen.mainClassSignature);
            sb.append("Lorg/mozilla/javascript/Context;");
            sb.append("Lorg/mozilla/javascript/Scriptable;");
            sb.append("Ljava/lang/Object;");
            sb.append("Ljava/lang/Object;I)Ljava/lang/Object;");
            final String string = sb.toString();
            final ClassFileWriter cfw = this.cfw;
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(this.codegen.getBodyMethodName(this.scriptOrFn));
            sb2.append("_gen");
            cfw.startMethod(sb2.toString(), string, (short)10);
        }
        else {
            this.cfw.startMethod(this.codegen.getBodyMethodName(this.scriptOrFn), this.codegen.getBodyMethodSignature(this.scriptOrFn), (short)10);
        }
        this.generatePrologue();
        Node node;
        if (this.fnCurrent != null) {
            node = this.scriptOrFn.getLastChild();
        }
        else {
            node = this.scriptOrFn;
        }
        this.generateStatement(node);
        this.generateEpilogue();
        this.cfw.stopMethod((short)(this.localsMax + 1));
        if (this.isGenerator) {
            this.generateGenerator();
        }
        if (this.literals != null) {
            for (int i = 0; i < this.literals.size(); ++i) {
                final Node node2 = this.literals.get(i);
                final int type = node2.getType();
                switch (type) {
                    default: {
                        Kit.codeBug(Token.typeToName(type));
                        break;
                    }
                    case 66: {
                        this.generateObjectLiteralFactory(node2, i + 1);
                        break;
                    }
                    case 65: {
                        this.generateArrayLiteralFactory(node2, i + 1);
                        break;
                    }
                }
            }
        }
    }
    
    private class ExceptionManager
    {
        private LinkedList<ExceptionInfo> exceptionInfo;
        
        ExceptionManager() {
            this.exceptionInfo = new LinkedList<ExceptionInfo>();
        }
        
        private void endCatch(final ExceptionInfo exceptionInfo, final int n, final int n2) {
            if (exceptionInfo.exceptionStarts[n] == 0) {
                throw new IllegalStateException("bad exception start");
            }
            if (BodyCodegen.this.cfw.getLabelPC(exceptionInfo.exceptionStarts[n]) != BodyCodegen.this.cfw.getLabelPC(n2)) {
                BodyCodegen.this.cfw.addExceptionHandler(exceptionInfo.exceptionStarts[n], n2, exceptionInfo.handlerLabels[n], BodyCodegen.this.exceptionTypeToName(n));
            }
        }
        
        private ExceptionInfo getTop() {
            return this.exceptionInfo.getLast();
        }
        
        void addHandler(final int n, final int n2, final int n3) {
            final ExceptionInfo top = this.getTop();
            top.handlerLabels[n] = n2;
            top.exceptionStarts[n] = n3;
        }
        
        void markInlineFinallyEnd(final Node node, final int n) {
            final ListIterator<ExceptionInfo> listIterator = this.exceptionInfo.listIterator(this.exceptionInfo.size());
            while (listIterator.hasPrevious()) {
                final ExceptionInfo exceptionInfo = listIterator.previous();
                for (int i = 0; i < 5; ++i) {
                    if (exceptionInfo.handlerLabels[i] != 0 && exceptionInfo.currentFinally == node) {
                        exceptionInfo.exceptionStarts[i] = n;
                        exceptionInfo.currentFinally = null;
                    }
                }
                if (exceptionInfo.finallyBlock == node) {
                    return;
                }
            }
        }
        
        void markInlineFinallyStart(final Node currentFinally, final int n) {
            final ListIterator<ExceptionInfo> listIterator = this.exceptionInfo.listIterator(this.exceptionInfo.size());
            while (listIterator.hasPrevious()) {
                final ExceptionInfo exceptionInfo = listIterator.previous();
                for (int i = 0; i < 5; ++i) {
                    if (exceptionInfo.handlerLabels[i] != 0 && exceptionInfo.currentFinally == null) {
                        this.endCatch(exceptionInfo, i, n);
                        exceptionInfo.exceptionStarts[i] = 0;
                        exceptionInfo.currentFinally = currentFinally;
                    }
                }
                if (exceptionInfo.finallyBlock == currentFinally) {
                    return;
                }
            }
        }
        
        void popExceptionInfo() {
            this.exceptionInfo.removeLast();
        }
        
        void pushExceptionInfo(final Jump jump) {
            this.exceptionInfo.add(new ExceptionInfo(jump, BodyCodegen.this.getFinallyAtTarget(jump.getFinally())));
        }
        
        int removeHandler(final int n, final int n2) {
            final ExceptionInfo top = this.getTop();
            if (top.handlerLabels[n] != 0) {
                final int n3 = top.handlerLabels[n];
                this.endCatch(top, n, n2);
                top.handlerLabels[n] = 0;
                return n3;
            }
            return 0;
        }
        
        void setHandlers(final int[] array, final int n) {
            this.getTop();
            for (int i = 0; i < array.length; ++i) {
                if (array[i] != 0) {
                    this.addHandler(i, array[i], n);
                }
            }
        }
        
        private class ExceptionInfo
        {
            Node currentFinally;
            int[] exceptionStarts;
            Node finallyBlock;
            int[] handlerLabels;
            Jump node;
            
            ExceptionInfo(final Jump node, final Node finallyBlock) {
                this.node = node;
                this.finallyBlock = finallyBlock;
                this.handlerLabels = new int[5];
                this.exceptionStarts = new int[5];
                this.currentFinally = null;
            }
        }
    }
    
    static class FinallyReturnPoint
    {
        public List<Integer> jsrPoints;
        public int tableLabel;
        
        FinallyReturnPoint() {
            this.jsrPoints = new ArrayList<Integer>();
            this.tableLabel = 0;
        }
    }
}
