package org.mozilla.javascript;

import org.mozilla.javascript.debug.*;
import org.mozilla.javascript.ast.*;
import java.util.*;
import java.io.*;

public final class Interpreter extends Icode implements Evaluator
{
    static final int EXCEPTION_HANDLER_SLOT = 2;
    static final int EXCEPTION_LOCAL_SLOT = 4;
    static final int EXCEPTION_SCOPE_SLOT = 5;
    static final int EXCEPTION_SLOT_SIZE = 6;
    static final int EXCEPTION_TRY_END_SLOT = 1;
    static final int EXCEPTION_TRY_START_SLOT = 0;
    static final int EXCEPTION_TYPE_SLOT = 3;
    InterpreterData itsData;
    
    private static void addInstructionCount(final Context context, final CallFrame callFrame, final int n) {
        context.instructionCount += callFrame.pc - callFrame.pcPrevBranch + n;
        if (context.instructionCount > context.instructionThreshold) {
            context.observeInstructionCount(context.instructionCount);
            context.instructionCount = 0;
        }
    }
    
    private static int bytecodeSpan(final int n) {
        switch (n) {
            default: {
                switch (n) {
                    default: {
                        switch (n) {
                            default: {
                                switch (n) {
                                    default: {
                                        switch (n) {
                                            default: {
                                                switch (n) {
                                                    default: {
                                                        switch (n) {
                                                            default: {
                                                                if (!Icode.validBytecode(n)) {
                                                                    throw Kit.codeBug();
                                                                }
                                                                return 1;
                                                            }
                                                            case 57: {
                                                                return 2;
                                                            }
                                                            case -21: {
                                                                return 5;
                                                            }
                                                            case -54:
                                                            case -23: {
                                                                return 3;
                                                            }
                                                            case 50:
                                                            case 72: {
                                                                return 3;
                                                            }
                                                        }
                                                        break;
                                                    }
                                                    case 5:
                                                    case 6:
                                                    case 7: {
                                                        return 3;
                                                    }
                                                }
                                                break;
                                            }
                                            case -6: {
                                                return 3;
                                            }
                                            case -11:
                                            case -10:
                                            case -9:
                                            case -8:
                                            case -7: {
                                                return 2;
                                            }
                                        }
                                        break;
                                    }
                                    case -26: {
                                        return 3;
                                    }
                                    case -27: {
                                        return 3;
                                    }
                                    case -28: {
                                        return 5;
                                    }
                                }
                                break;
                            }
                            case -38: {
                                return 2;
                            }
                            case -39: {
                                return 3;
                            }
                            case -40: {
                                return 5;
                            }
                        }
                        break;
                    }
                    case -45: {
                        return 2;
                    }
                    case -46: {
                        return 3;
                    }
                    case -47: {
                        return 5;
                    }
                    case -49:
                    case -48: {
                        return 2;
                    }
                }
                break;
            }
            case -61: {
                return 2;
            }
            case -63:
            case -62: {
                return 3;
            }
        }
    }
    
    public static NativeContinuation captureContinuation(final Context context) {
        if (context.lastInterpreterFrame != null && context.lastInterpreterFrame instanceof CallFrame) {
            return captureContinuation(context, (CallFrame)context.lastInterpreterFrame, true);
        }
        throw new IllegalStateException("Interpreter frames not found");
    }
    
    private static NativeContinuation captureContinuation(final Context context, final CallFrame callFrame, final boolean b) {
        final NativeContinuation nativeContinuation = new NativeContinuation();
        ScriptRuntime.setObjectProtoAndParent(nativeContinuation, ScriptRuntime.getTopCallScope(context));
        CallFrame parentFrame2;
        CallFrame parentFrame;
        for (parentFrame = (parentFrame2 = callFrame); parentFrame2 != null && !parentFrame2.frozen; parentFrame2 = parentFrame2.parentFrame) {
            parentFrame2.frozen = true;
            for (int i = parentFrame2.savedStackTop + 1; i != parentFrame2.stack.length; ++i) {
                parentFrame2.stack[i] = null;
                parentFrame2.stackAttributes[i] = 0;
            }
            if (parentFrame2.savedCallOp == 38) {
                parentFrame2.stack[parentFrame2.savedStackTop] = null;
            }
            else if (parentFrame2.savedCallOp != 30) {
                Kit.codeBug();
            }
            parentFrame = parentFrame2;
        }
        if (b) {
            while (parentFrame.parentFrame != null) {
                parentFrame = parentFrame.parentFrame;
            }
            if (!parentFrame.isContinuationsTopFrame) {
                throw new IllegalStateException("Cannot capture continuation from JavaScript code not called directly by executeScriptWithContinuations or callFunctionWithContinuations");
            }
        }
        nativeContinuation.initImplementation(callFrame);
        return nativeContinuation;
    }
    
    private static CallFrame captureFrameForGenerator(final CallFrame callFrame) {
        callFrame.frozen = true;
        final CallFrame cloneFrozen = callFrame.cloneFrozen();
        callFrame.frozen = false;
        cloneFrozen.parentFrame = null;
        cloneFrozen.frameIndex = 0;
        return cloneFrozen;
    }
    
    private static void doAdd(final Object[] array, final double[] array2, final int n, final Context context) {
        Object o = array[n + 1];
        final Object o2 = array[n];
        double n2;
        boolean b;
        if (o == UniqueTag.DOUBLE_MARK) {
            n2 = array2[n + 1];
            if (o2 == UniqueTag.DOUBLE_MARK) {
                array2[n] += n2;
                return;
            }
            b = true;
            o = o2;
        }
        else if (o2 == UniqueTag.DOUBLE_MARK) {
            n2 = array2[n];
            b = false;
        }
        else {
            if (o2 instanceof Scriptable || o instanceof Scriptable) {
                array[n] = ScriptRuntime.add(o2, o, context);
                return;
            }
            if (!(o2 instanceof CharSequence) && !(o instanceof CharSequence)) {
                double n3;
                if (o2 instanceof Number) {
                    n3 = ((Number)o2).doubleValue();
                }
                else {
                    n3 = ScriptRuntime.toNumber(o2);
                }
                double n4;
                if (o instanceof Number) {
                    n4 = ((Number)o).doubleValue();
                }
                else {
                    n4 = ScriptRuntime.toNumber(o);
                }
                array[n] = UniqueTag.DOUBLE_MARK;
                array2[n] = n3 + n4;
                return;
            }
            array[n] = new ConsString(ScriptRuntime.toCharSequence(o2), ScriptRuntime.toCharSequence(o));
            return;
        }
        if (o instanceof Scriptable) {
            Number wrapNumber;
            final Number n5 = wrapNumber = ScriptRuntime.wrapNumber(n2);
            Number n6 = (Number)o;
            if (!b) {
                n6 = n5;
                wrapNumber = (Number)o;
            }
            array[n] = ScriptRuntime.add(n6, wrapNumber, context);
            return;
        }
        if (o instanceof CharSequence) {
            final CharSequence charSequence = (CharSequence)o;
            final CharSequence charSequence2 = ScriptRuntime.toCharSequence(n2);
            if (b) {
                array[n] = new ConsString(charSequence, charSequence2);
            }
            else {
                array[n] = new ConsString(charSequence2, charSequence);
            }
            return;
        }
        double n7;
        if (o instanceof Number) {
            n7 = ((Number)o).doubleValue();
        }
        else {
            n7 = ScriptRuntime.toNumber(o);
        }
        array[n] = UniqueTag.DOUBLE_MARK;
        array2[n] = n7 + n2;
    }
    
    private static int doArithmetic(final CallFrame callFrame, final int n, final Object[] array, final double[] array2, int n2) {
        final double stack_double = stack_double(callFrame, n2);
        --n2;
        double stack_double2 = stack_double(callFrame, n2);
        array[n2] = UniqueTag.DOUBLE_MARK;
        switch (n) {
            case 25: {
                stack_double2 %= stack_double;
                break;
            }
            case 24: {
                stack_double2 /= stack_double;
                break;
            }
            case 23: {
                stack_double2 *= stack_double;
                break;
            }
            case 22: {
                stack_double2 -= stack_double;
                break;
            }
        }
        array2[n2] = stack_double2;
        return n2;
    }
    
    private static int doBitOp(final CallFrame callFrame, int n, final Object[] array, final double[] array2, int n2) {
        final int stack_int32 = stack_int32(callFrame, n2 - 1);
        final int stack_int33 = stack_int32(callFrame, n2);
        --n2;
        array[n2] = UniqueTag.DOUBLE_MARK;
        Label_0132: {
            switch (n) {
                default: {
                    switch (n) {
                        default: {
                            n = stack_int32;
                            break Label_0132;
                        }
                        case 19: {
                            n = stack_int32 >> stack_int33;
                            break Label_0132;
                        }
                        case 18: {
                            n = stack_int32 << stack_int33;
                            break Label_0132;
                        }
                    }
                    break;
                }
                case 11: {
                    n = (stack_int32 & stack_int33);
                    break;
                }
                case 10: {
                    n = (stack_int32 ^ stack_int33);
                    break;
                }
                case 9: {
                    n = (stack_int32 | stack_int33);
                    break;
                }
            }
        }
        array2[n2] = n;
        return n2;
    }
    
    private static int doCallSpecial(final Context context, final CallFrame callFrame, final Object[] array, final double[] array2, int n, final byte[] array3, final int n2) {
        final int n3 = array3[callFrame.pc] & 0xFF;
        final int pc = callFrame.pc;
        boolean b = true;
        if (array3[pc + 1] == 0) {
            b = false;
        }
        final int index = getIndex(array3, callFrame.pc + 2);
        if (b) {
            n -= n2;
            Object wrapNumber;
            if ((wrapNumber = array[n]) == UniqueTag.DOUBLE_MARK) {
                wrapNumber = ScriptRuntime.wrapNumber(array2[n]);
            }
            array[n] = ScriptRuntime.newSpecial(context, wrapNumber, getArgsArray(array, array2, n + 1, n2), callFrame.scope, n3);
        }
        else {
            n -= n2 + 1;
            array[n] = ScriptRuntime.callSpecial(context, (Callable)array[n], (Scriptable)array[n + 1], getArgsArray(array, array2, n + 2, n2), callFrame.scope, callFrame.thisObj, n3, callFrame.idata.itsSourceFile, index);
        }
        callFrame.pc += 4;
        return n;
    }
    
    private static int doCompare(final CallFrame callFrame, final int n, final Object[] array, final double[] array2, int n2) {
        --n2;
        final Object o = array[n2 + 1];
        final Object o2 = array[n2];
        boolean b = false;
        Label_0265: {
            double number;
            double stack_double;
            if (o == UniqueTag.DOUBLE_MARK) {
                number = array2[n2 + 1];
                stack_double = stack_double(callFrame, n2);
            }
            else if (o2 == UniqueTag.DOUBLE_MARK) {
                number = ScriptRuntime.toNumber(o);
                stack_double = array2[n2];
            }
            else {
                switch (n) {
                    default: {
                        throw Kit.codeBug();
                    }
                    case 17: {
                        b = ScriptRuntime.cmp_LE(o, o2);
                        break Label_0265;
                    }
                    case 16: {
                        b = ScriptRuntime.cmp_LT(o, o2);
                        break Label_0265;
                    }
                    case 15: {
                        b = ScriptRuntime.cmp_LE(o2, o);
                        break Label_0265;
                    }
                    case 14: {
                        b = ScriptRuntime.cmp_LT(o2, o);
                        break Label_0265;
                    }
                }
            }
            final boolean b2 = false;
            final boolean b3 = false;
            final boolean b4 = false;
            b = false;
            switch (n) {
                default: {
                    throw Kit.codeBug();
                }
                case 17: {
                    if (stack_double >= number) {
                        b = true;
                    }
                    break;
                }
                case 16: {
                    b = b2;
                    if (stack_double > number) {
                        b = true;
                    }
                    break;
                }
                case 15: {
                    b = b3;
                    if (stack_double <= number) {
                        b = true;
                    }
                    break;
                }
                case 14: {
                    b = b4;
                    if (stack_double < number) {
                        b = true;
                    }
                    break;
                }
            }
        }
        array[n2] = ScriptRuntime.wrapBoolean(b);
        return n2;
    }
    
    private static int doDelName(final Context context, final CallFrame callFrame, final int n, final Object[] array, final double[] array2, int n2) {
        Object wrapNumber;
        if ((wrapNumber = array[n2]) == UniqueTag.DOUBLE_MARK) {
            wrapNumber = ScriptRuntime.wrapNumber(array2[n2]);
        }
        --n2;
        Object wrapNumber2;
        if ((wrapNumber2 = array[n2]) == UniqueTag.DOUBLE_MARK) {
            wrapNumber2 = ScriptRuntime.wrapNumber(array2[n2]);
        }
        array[n2] = ScriptRuntime.delete(wrapNumber2, wrapNumber, context, callFrame.scope, n == 0);
        return n2;
    }
    
    private static int doElemIncDec(final Context context, final CallFrame callFrame, final byte[] array, final Object[] array2, final double[] array3, int n) {
        Object wrapNumber;
        if ((wrapNumber = array2[n]) == UniqueTag.DOUBLE_MARK) {
            wrapNumber = ScriptRuntime.wrapNumber(array3[n]);
        }
        --n;
        Object wrapNumber2;
        if ((wrapNumber2 = array2[n]) == UniqueTag.DOUBLE_MARK) {
            wrapNumber2 = ScriptRuntime.wrapNumber(array3[n]);
        }
        array2[n] = ScriptRuntime.elemIncrDecr(wrapNumber2, wrapNumber, context, callFrame.scope, array[callFrame.pc]);
        ++callFrame.pc;
        return n;
    }
    
    private static boolean doEquals(final Object[] array, final double[] array2, final int n) {
        final Object o = array[n + 1];
        final Object o2 = array[n];
        if (o == UniqueTag.DOUBLE_MARK) {
            if (o2 == UniqueTag.DOUBLE_MARK) {
                return array2[n] == array2[n + 1];
            }
            return ScriptRuntime.eqNumber(array2[n + 1], o2);
        }
        else {
            if (o2 == UniqueTag.DOUBLE_MARK) {
                return ScriptRuntime.eqNumber(array2[n], o);
            }
            return ScriptRuntime.eq(o2, o);
        }
    }
    
    private static int doGetElem(final Context context, final CallFrame callFrame, final Object[] array, final double[] array2, int n) {
        --n;
        Object wrapNumber;
        if ((wrapNumber = array[n]) == UniqueTag.DOUBLE_MARK) {
            wrapNumber = ScriptRuntime.wrapNumber(array2[n]);
        }
        final Object o = array[n + 1];
        Object o2;
        if (o != UniqueTag.DOUBLE_MARK) {
            o2 = ScriptRuntime.getObjectElem(wrapNumber, o, context, callFrame.scope);
        }
        else {
            o2 = ScriptRuntime.getObjectIndex(wrapNumber, array2[n + 1], context, callFrame.scope);
        }
        array[n] = o2;
        return n;
    }
    
    private static int doGetVar(final CallFrame callFrame, final Object[] array, final double[] array2, int n, final Object[] array3, final double[] array4, final int n2) {
        ++n;
        if (!callFrame.useActivation) {
            array[n] = array3[n2];
            array2[n] = array4[n2];
            return n;
        }
        array[n] = callFrame.scope.get(callFrame.idata.argNames[n2], callFrame.scope);
        return n;
    }
    
    private static int doInOrInstanceof(final Context context, final int n, final Object[] array, final double[] array2, int n2) {
        Object wrapNumber;
        if ((wrapNumber = array[n2]) == UniqueTag.DOUBLE_MARK) {
            wrapNumber = ScriptRuntime.wrapNumber(array2[n2]);
        }
        --n2;
        Object wrapNumber2;
        if ((wrapNumber2 = array[n2]) == UniqueTag.DOUBLE_MARK) {
            wrapNumber2 = ScriptRuntime.wrapNumber(array2[n2]);
        }
        boolean b;
        if (n == 52) {
            b = ScriptRuntime.in(wrapNumber2, wrapNumber, context);
        }
        else {
            b = ScriptRuntime.instanceOf(wrapNumber2, wrapNumber, context);
        }
        array[n2] = ScriptRuntime.wrapBoolean(b);
        return n2;
    }
    
    private static int doRefMember(final Context context, final Object[] array, final double[] array2, int n, final int n2) {
        Object wrapNumber;
        if ((wrapNumber = array[n]) == UniqueTag.DOUBLE_MARK) {
            wrapNumber = ScriptRuntime.wrapNumber(array2[n]);
        }
        --n;
        Object wrapNumber2;
        if ((wrapNumber2 = array[n]) == UniqueTag.DOUBLE_MARK) {
            wrapNumber2 = ScriptRuntime.wrapNumber(array2[n]);
        }
        array[n] = ScriptRuntime.memberRef(wrapNumber2, wrapNumber, context, n2);
        return n;
    }
    
    private static int doRefNsMember(final Context context, final Object[] array, final double[] array2, int n, final int n2) {
        Object wrapNumber;
        if ((wrapNumber = array[n]) == UniqueTag.DOUBLE_MARK) {
            wrapNumber = ScriptRuntime.wrapNumber(array2[n]);
        }
        --n;
        Object wrapNumber2;
        if ((wrapNumber2 = array[n]) == UniqueTag.DOUBLE_MARK) {
            wrapNumber2 = ScriptRuntime.wrapNumber(array2[n]);
        }
        --n;
        Object wrapNumber3;
        if ((wrapNumber3 = array[n]) == UniqueTag.DOUBLE_MARK) {
            wrapNumber3 = ScriptRuntime.wrapNumber(array2[n]);
        }
        array[n] = ScriptRuntime.memberRef(wrapNumber3, wrapNumber2, wrapNumber, context, n2);
        return n;
    }
    
    private static int doRefNsName(final Context context, final CallFrame callFrame, final Object[] array, final double[] array2, int n, final int n2) {
        Object wrapNumber;
        if ((wrapNumber = array[n]) == UniqueTag.DOUBLE_MARK) {
            wrapNumber = ScriptRuntime.wrapNumber(array2[n]);
        }
        --n;
        Object wrapNumber2;
        if ((wrapNumber2 = array[n]) == UniqueTag.DOUBLE_MARK) {
            wrapNumber2 = ScriptRuntime.wrapNumber(array2[n]);
        }
        array[n] = ScriptRuntime.nameRef(wrapNumber2, wrapNumber, context, callFrame.scope, n2);
        return n;
    }
    
    private static int doSetConstVar(final CallFrame callFrame, final Object[] array, final double[] array2, final int n, final Object[] array3, final double[] array4, final int[] array5, final int n2) {
        if (!callFrame.useActivation) {
            if ((array5[n2] & 0x1) == 0x0) {
                throw Context.reportRuntimeError1("msg.var.redecl", callFrame.idata.argNames[n2]);
            }
            if ((array5[n2] & 0x8) != 0x0) {
                array3[n2] = array[n];
                array5[n2] &= 0xFFFFFFF7;
                array4[n2] = array2[n];
                return n;
            }
        }
        else {
            Object wrapNumber;
            if ((wrapNumber = array[n]) == UniqueTag.DOUBLE_MARK) {
                wrapNumber = ScriptRuntime.wrapNumber(array2[n]);
            }
            final String s = callFrame.idata.argNames[n2];
            if (!(callFrame.scope instanceof ConstProperties)) {
                throw Kit.codeBug();
            }
            ((ConstProperties)callFrame.scope).putConst(s, callFrame.scope, wrapNumber);
        }
        return n;
    }
    
    private static int doSetElem(final Context context, final CallFrame callFrame, final Object[] array, final double[] array2, int n) {
        n -= 2;
        Object wrapNumber;
        if ((wrapNumber = array[n + 2]) == UniqueTag.DOUBLE_MARK) {
            wrapNumber = ScriptRuntime.wrapNumber(array2[n + 2]);
        }
        Object wrapNumber2;
        if ((wrapNumber2 = array[n]) == UniqueTag.DOUBLE_MARK) {
            wrapNumber2 = ScriptRuntime.wrapNumber(array2[n]);
        }
        final Object o = array[n + 1];
        Object o2;
        if (o != UniqueTag.DOUBLE_MARK) {
            o2 = ScriptRuntime.setObjectElem(wrapNumber2, o, wrapNumber, context, callFrame.scope);
        }
        else {
            o2 = ScriptRuntime.setObjectIndex(wrapNumber2, array2[n + 1], wrapNumber, context, callFrame.scope);
        }
        array[n] = o2;
        return n;
    }
    
    private static int doSetVar(final CallFrame callFrame, final Object[] array, final double[] array2, final int n, final Object[] array3, final double[] array4, final int[] array5, final int n2) {
        if (!callFrame.useActivation) {
            if ((array5[n2] & 0x1) == 0x0) {
                array3[n2] = array[n];
                array4[n2] = array2[n];
                return n;
            }
        }
        else {
            Object wrapNumber;
            if ((wrapNumber = array[n]) == UniqueTag.DOUBLE_MARK) {
                wrapNumber = ScriptRuntime.wrapNumber(array2[n]);
            }
            callFrame.scope.put(callFrame.idata.argNames[n2], callFrame.scope, wrapNumber);
        }
        return n;
    }
    
    private static boolean doShallowEquals(final Object[] array, final double[] array2, final int n) {
        final Object o = array[n + 1];
        final Object o2 = array[n];
        final UniqueTag double_MARK = UniqueTag.DOUBLE_MARK;
        boolean b = false;
        double doubleValue;
        double doubleValue2;
        if (o == double_MARK) {
            doubleValue = array2[n + 1];
            if (o2 == double_MARK) {
                doubleValue2 = array2[n];
            }
            else {
                if (!(o2 instanceof Number)) {
                    return false;
                }
                doubleValue2 = ((Number)o2).doubleValue();
            }
        }
        else {
            if (o2 != double_MARK) {
                return ScriptRuntime.shallowEq(o2, o);
            }
            doubleValue2 = array2[n];
            if (!(o instanceof Number)) {
                return false;
            }
            doubleValue = ((Number)o).doubleValue();
        }
        if (doubleValue2 == doubleValue) {
            b = true;
        }
        return b;
    }
    
    private static int doVarIncDec(final Context context, final CallFrame callFrame, final Object[] array, final double[] array2, int n, final Object[] array3, final double[] array4, final int[] array5, final int n2) {
        final int n3 = n + 1;
        n = callFrame.idata.itsICode[callFrame.pc];
        if (!callFrame.useActivation) {
            final Object o = array3[n2];
            double number;
            if (o == UniqueTag.DOUBLE_MARK) {
                number = array4[n2];
            }
            else {
                number = ScriptRuntime.toNumber(o);
            }
            double n4;
            if ((n & 0x1) == 0x0) {
                n4 = 1.0 + number;
            }
            else {
                n4 = number - 1.0;
            }
            if ((n & 0x2) != 0x0) {
                n = 1;
            }
            else {
                n = 0;
            }
            if ((array5[n2] & 0x1) == 0x0) {
                if (o != UniqueTag.DOUBLE_MARK) {
                    array3[n2] = UniqueTag.DOUBLE_MARK;
                }
                array4[n2] = n4;
                array[n3] = UniqueTag.DOUBLE_MARK;
                if (n == 0) {
                    number = n4;
                }
                array2[n3] = number;
            }
            else if (n != 0 && o != UniqueTag.DOUBLE_MARK) {
                array[n3] = o;
            }
            else {
                array[n3] = UniqueTag.DOUBLE_MARK;
                if (n == 0) {
                    number = n4;
                }
                array2[n3] = number;
            }
        }
        else {
            array[n3] = ScriptRuntime.nameIncrDecr(callFrame.scope, callFrame.idata.argNames[n2], context, n);
        }
        ++callFrame.pc;
        return n3;
    }
    
    static void dumpICode(final InterpreterData interpreterData) {
    }
    
    private static void enterFrame(final Context context, final CallFrame callFrame, final Object[] array, final boolean b) {
        final boolean itsNeedsActivation = callFrame.idata.itsNeedsActivation;
        final boolean b2 = callFrame.debuggerFrame != null;
        if (itsNeedsActivation || b2) {
            Scriptable scope = callFrame.scope;
            Scriptable parentScope = null;
            Label_0122: {
                if (scope == null) {
                    Kit.codeBug();
                    parentScope = scope;
                }
                else {
                    parentScope = scope;
                    if (b) {
                        do {
                            parentScope = scope;
                            if (!(scope instanceof NativeWith)) {
                                break Label_0122;
                            }
                            parentScope = scope.getParentScope();
                            if (parentScope == null) {
                                break;
                            }
                            scope = parentScope;
                        } while (callFrame.parentFrame == null || callFrame.parentFrame.scope != (scope = parentScope));
                        Kit.codeBug();
                    }
                }
            }
            if (b2) {
                callFrame.debuggerFrame.onEnter(context, parentScope, callFrame.thisObj, array);
            }
            if (itsNeedsActivation) {
                ScriptRuntime.enterActivationFunction(context, parentScope);
            }
        }
    }
    
    private static void exitFrame(final Context context, final CallFrame callFrame, Object o) {
        if (callFrame.idata.itsNeedsActivation) {
            ScriptRuntime.exitActivationFunction(context);
        }
        if (callFrame.debuggerFrame != null) {
            try {
                if (o instanceof Throwable) {
                    callFrame.debuggerFrame.onExit(context, true, o);
                }
                else {
                    final ContinuationJump continuationJump = (ContinuationJump)o;
                    if (continuationJump == null) {
                        o = callFrame.result;
                    }
                    else {
                        o = continuationJump.result;
                    }
                    Object wrapNumber = o;
                    if (o == UniqueTag.DOUBLE_MARK) {
                        double n;
                        if (continuationJump == null) {
                            n = callFrame.resultDbl;
                        }
                        else {
                            n = continuationJump.resultDbl;
                        }
                        wrapNumber = ScriptRuntime.wrapNumber(n);
                    }
                    callFrame.debuggerFrame.onExit(context, false, wrapNumber);
                }
            }
            catch (Throwable t) {
                System.err.println("RHINO USAGE WARNING: onExit terminated with exception");
                t.printStackTrace(System.err);
            }
        }
    }
    
    private static Object freezeGenerator(final Context context, final CallFrame callFrame, final int savedStackTop, final GeneratorState generatorState) {
        if (generatorState.operation == 2) {
            throw ScriptRuntime.typeError0("msg.yield.closing");
        }
        callFrame.frozen = true;
        callFrame.result = callFrame.stack[savedStackTop];
        callFrame.resultDbl = callFrame.sDbl[savedStackTop];
        callFrame.savedStackTop = savedStackTop;
        --callFrame.pc;
        ScriptRuntime.exitActivationFunction(context);
        if (callFrame.result != UniqueTag.DOUBLE_MARK) {
            return callFrame.result;
        }
        return ScriptRuntime.wrapNumber(callFrame.resultDbl);
    }
    
    private static Object[] getArgsArray(final Object[] array, final double[] array2, int n, final int n2) {
        if (n2 == 0) {
            return ScriptRuntime.emptyArgs;
        }
        final Object[] array3 = new Object[n2];
        for (int i = 0; i != n2; ++i, ++n) {
            Object wrapNumber;
            if ((wrapNumber = array[n]) == UniqueTag.DOUBLE_MARK) {
                wrapNumber = ScriptRuntime.wrapNumber(array2[n]);
            }
            array3[i] = wrapNumber;
        }
        return array3;
    }
    
    static String getEncodedSource(final InterpreterData interpreterData) {
        if (interpreterData.encodedSource == null) {
            return null;
        }
        return interpreterData.encodedSource.substring(interpreterData.encodedSourceStart, interpreterData.encodedSourceEnd);
    }
    
    private static int getExceptionHandler(final CallFrame callFrame, final boolean b) {
        final int[] itsExceptionTable = callFrame.idata.itsExceptionTable;
        if (itsExceptionTable == null) {
            return -1;
        }
        final int n = callFrame.pc - 1;
        int n2 = -1;
        int n3 = 0;
        int n4 = 0;
        int n7;
        int n8;
        int n9;
        for (int i = 0; i != itsExceptionTable.length; i += 6, n2 = n7, n3 = n8, n4 = n9) {
            final int n5 = itsExceptionTable[i + 0];
            final int n6 = itsExceptionTable[i + 1];
            n7 = n2;
            n8 = n3;
            n9 = n4;
            if (n5 <= n) {
                if (n >= n6) {
                    n7 = n2;
                    n8 = n3;
                    n9 = n4;
                }
                else if (b && itsExceptionTable[i + 3] != 1) {
                    n7 = n2;
                    n8 = n3;
                    n9 = n4;
                }
                else {
                    if (n2 >= 0) {
                        if (n4 < n6) {
                            n7 = n2;
                            n8 = n3;
                            n9 = n4;
                            continue;
                        }
                        if (n3 > n5) {
                            Kit.codeBug();
                        }
                        if (n4 == n6) {
                            Kit.codeBug();
                        }
                    }
                    n7 = i;
                    n8 = n5;
                    n9 = n6;
                }
            }
        }
        return n2;
    }
    
    private static int getIndex(final byte[] array, final int n) {
        return (array[n] & 0xFF) << 8 | (array[n + 1] & 0xFF);
    }
    
    private static int getInt(final byte[] array, final int n) {
        return array[n] << 24 | (array[n + 1] & 0xFF) << 16 | (array[n + 2] & 0xFF) << 8 | (array[n + 3] & 0xFF);
    }
    
    static int[] getLineNumbers(final InterpreterData interpreterData) {
        final UintMap uintMap = new UintMap();
        final byte[] itsICode = interpreterData.itsICode;
        int bytecodeSpan;
        for (int length = itsICode.length, i = 0; i != length; i += bytecodeSpan) {
            final byte b = itsICode[i];
            bytecodeSpan = bytecodeSpan(b);
            if (b == -26) {
                if (bytecodeSpan != 3) {
                    Kit.codeBug();
                }
                uintMap.put(getIndex(itsICode, i + 1), 0);
            }
        }
        return uintMap.getKeys();
    }
    
    private static int getShort(final byte[] array, final int n) {
        return array[n] << 8 | (array[n + 1] & 0xFF);
    }
    
    private static void initFrame(final Context context, Scriptable scope, final Scriptable thisObj, final Object[] array, final double[] array2, int i, int frameIndex, final InterpretedFunction fnOrScript, final CallFrame parentFrame, final CallFrame varSource) {
        final InterpreterData idata = fnOrScript.idata;
        final boolean itsNeedsActivation = idata.itsNeedsActivation;
        DebugFrame debuggerFrame = null;
        boolean useActivation = itsNeedsActivation;
        if (context.debugger != null) {
            final DebugFrame frame = context.debugger.getFrame(context, idata);
            useActivation = itsNeedsActivation;
            if ((debuggerFrame = frame) != null) {
                useActivation = true;
                debuggerFrame = frame;
            }
        }
        Object[] argsArray;
        int n;
        Object o;
        if (useActivation) {
            if (array2 != null) {
                argsArray = getArgsArray(array, array2, i, frameIndex);
            }
            else {
                argsArray = array;
            }
            n = 0;
            o = null;
        }
        else {
            n = i;
            argsArray = array;
            o = array2;
        }
        if (idata.itsFunctionType != 0) {
            final Scriptable scriptable = scope = fnOrScript.getParentScope();
            if (useActivation) {
                scope = ScriptRuntime.createFunctionActivation(fnOrScript, scriptable, argsArray);
            }
        }
        else {
            ScriptRuntime.initScript(fnOrScript, thisObj, context, scope, fnOrScript.idata.evalScriptFlag);
        }
        if (idata.itsNestedFunctions != null) {
            if (idata.itsFunctionType != 0 && !idata.itsNeedsActivation) {
                Kit.codeBug();
            }
            for (i = 0; i < idata.itsNestedFunctions.length; ++i) {
                if (idata.itsNestedFunctions[i].itsFunctionType == 1) {
                    initFunction(context, scope, fnOrScript, i);
                }
            }
        }
        final int n2 = idata.itsMaxVars + idata.itsMaxLocals - 1;
        i = idata.itsMaxFrameArray;
        if (i != idata.itsMaxStack + n2 + 1) {
            Kit.codeBug();
        }
        Object[] stack;
        int[] stackAttributes;
        double[] sDbl;
        boolean b;
        if (varSource.stack != null && i <= varSource.stack.length) {
            stack = varSource.stack;
            stackAttributes = varSource.stackAttributes;
            sDbl = varSource.sDbl;
            b = true;
        }
        else {
            stack = new Object[i];
            stackAttributes = new int[i];
            sDbl = new double[i];
            b = false;
        }
        int paramAndVarCount;
        for (paramAndVarCount = idata.getParamAndVarCount(), i = 0; i < paramAndVarCount; ++i) {
            if (idata.getParamOrVarConst(i)) {
                stackAttributes[i] = 13;
            }
        }
        if ((i = idata.argCount) > frameIndex) {
            i = frameIndex;
        }
        if ((varSource.parentFrame = parentFrame) == null) {
            frameIndex = 0;
        }
        else {
            frameIndex = parentFrame.frameIndex + 1;
        }
        varSource.frameIndex = frameIndex;
        if (varSource.frameIndex > context.getMaximumInterpreterStackDepth()) {
            throw Context.reportRuntimeError("Exceeded maximum stack depth");
        }
        varSource.frozen = false;
        varSource.fnOrScript = fnOrScript;
        varSource.idata = idata;
        varSource.stack = stack;
        varSource.stackAttributes = stackAttributes;
        varSource.sDbl = sDbl;
        varSource.varSource = varSource;
        varSource.localShift = idata.itsMaxVars;
        varSource.emptyStackTop = n2;
        varSource.debuggerFrame = debuggerFrame;
        varSource.useActivation = useActivation;
        varSource.thisObj = thisObj;
        varSource.result = Undefined.instance;
        varSource.pc = 0;
        varSource.pcPrevBranch = 0;
        varSource.pcSourceLineStart = idata.firstLinePC;
        varSource.scope = scope;
        varSource.savedStackTop = n2;
        System.arraycopy(argsArray, n, stack, varSource.savedCallOp = 0, i);
        if (o != null) {
            System.arraycopy(o, n, sDbl, 0, i);
        }
        while (i != idata.itsMaxVars) {
            stack[i] = Undefined.instance;
            ++i;
        }
        if (b) {
            for (i = n2 + 1; i != stack.length; ++i) {
                stack[i] = null;
            }
        }
        enterFrame(context, varSource, argsArray, false);
    }
    
    private static CallFrame initFrameForApplyOrCall(final Context context, final CallFrame callFrame, int n, final Object[] array, final double[] array2, final int savedStackTop, int i, final Scriptable scriptable, final IdFunctionObject idFunctionObject, final InterpretedFunction interpretedFunction) {
        CallFrame parentFrame = callFrame;
        Scriptable objectOrNull;
        if (n != 0) {
            Object wrapNumber;
            if ((wrapNumber = array[savedStackTop + 2]) == UniqueTag.DOUBLE_MARK) {
                wrapNumber = ScriptRuntime.wrapNumber(array2[savedStackTop + 2]);
            }
            objectOrNull = ScriptRuntime.toObjectOrNull(context, wrapNumber, parentFrame.scope);
        }
        else {
            objectOrNull = null;
        }
        Scriptable topCallScope = objectOrNull;
        if (objectOrNull == null) {
            topCallScope = ScriptRuntime.getTopCallScope(context);
        }
        if (i == -55) {
            exitFrame(context, parentFrame, null);
            parentFrame = parentFrame.parentFrame;
        }
        else {
            parentFrame.savedStackTop = savedStackTop;
            parentFrame.savedCallOp = i;
        }
        final CallFrame callFrame2 = new CallFrame();
        if (BaseFunction.isApply(idFunctionObject)) {
            Object[] array3;
            if (n < 2) {
                array3 = ScriptRuntime.emptyArgs;
            }
            else {
                array3 = ScriptRuntime.getApplyArguments(context, array[savedStackTop + 3]);
            }
            initFrame(context, scriptable, topCallScope, array3, null, 0, array3.length, interpretedFunction, parentFrame, callFrame2);
        }
        else {
            for (i = 1; i < n; ++i) {
                array[savedStackTop + 1 + i] = array[savedStackTop + 2 + i];
                array2[savedStackTop + 1 + i] = array2[savedStackTop + 2 + i];
            }
            if (n < 2) {
                n = 0;
            }
            else {
                --n;
            }
            initFrame(context, scriptable, topCallScope, array, array2, savedStackTop + 2, n, interpretedFunction, parentFrame, callFrame2);
        }
        return callFrame2;
    }
    
    private static CallFrame initFrameForNoSuchMethod(final Context context, final CallFrame callFrame, final int n, final Object[] array, final double[] array2, final int savedStackTop, final int savedCallOp, final Scriptable scriptable, final Scriptable scriptable2, final ScriptRuntime.NoSuchMethodShim noSuchMethodShim, final InterpretedFunction interpretedFunction) {
        final Object[] array3 = new Object[n];
        for (int n2 = savedStackTop + 2, i = 0; i < n; ++i, ++n2) {
            Object wrapNumber;
            if ((wrapNumber = array[n2]) == UniqueTag.DOUBLE_MARK) {
                wrapNumber = ScriptRuntime.wrapNumber(array2[n2]);
            }
            array3[i] = wrapNumber;
        }
        final String methodName = noSuchMethodShim.methodName;
        final Scriptable array4 = context.newArray(scriptable2, array3);
        CallFrame parentFrame = callFrame;
        final CallFrame callFrame2 = new CallFrame();
        if (savedCallOp == -55) {
            parentFrame = callFrame.parentFrame;
            exitFrame(context, callFrame, null);
        }
        initFrame(context, scriptable2, scriptable, new Object[] { methodName, array4 }, null, 0, 2, interpretedFunction, parentFrame, callFrame2);
        if (savedCallOp != -55) {
            callFrame.savedStackTop = savedStackTop;
            callFrame.savedCallOp = savedCallOp;
        }
        return callFrame2;
    }
    
    private static void initFunction(final Context context, final Scriptable scriptable, final InterpretedFunction interpretedFunction, final int n) {
        final InterpretedFunction function = InterpretedFunction.createFunction(context, scriptable, interpretedFunction, n);
        ScriptRuntime.initFunction(context, scriptable, function, function.idata.itsFunctionType, interpretedFunction.idata.evalScriptFlag);
    }
    
    static Object interpret(final InterpretedFunction interpretedFunction, final Context context, final Scriptable scriptable, final Scriptable scriptable2, final Object[] array) {
        if (!ScriptRuntime.hasTopCall(context)) {
            Kit.codeBug();
        }
        if (context.interpreterSecurityDomain != interpretedFunction.securityDomain) {
            final Object interpreterSecurityDomain = context.interpreterSecurityDomain;
            context.interpreterSecurityDomain = interpretedFunction.securityDomain;
            try {
                return interpretedFunction.securityController.callWithDomain(interpretedFunction.securityDomain, context, interpretedFunction, scriptable, scriptable2, array);
            }
            finally {
                context.interpreterSecurityDomain = interpreterSecurityDomain;
            }
        }
        final CallFrame callFrame = new CallFrame();
        initFrame(context, scriptable, scriptable2, array, null, 0, array.length, interpretedFunction, null, callFrame);
        callFrame.isContinuationsTopFrame = context.isContinuationsTopCall;
        context.isContinuationsTopCall = false;
        return interpretLoop(context, callFrame, null);
    }
    
    private static Object interpretLoop(final Context p0, final CallFrame p1, final Object p2) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: astore          21
        //     3: aload_2        
        //     4: astore          20
        //     6: getstatic       org/mozilla/javascript/UniqueTag.DOUBLE_MARK:Lorg/mozilla/javascript/UniqueTag;
        //     9: astore          24
        //    11: getstatic       org/mozilla/javascript/Undefined.instance:Ljava/lang/Object;
        //    14: astore          22
        //    16: aload           21
        //    18: getfield        org/mozilla/javascript/Context.instructionThreshold:I
        //    21: ifeq            30
        //    24: iconst_1       
        //    25: istore          16
        //    27: goto            33
        //    30: iconst_0       
        //    31: istore          16
        //    33: aload           21
        //    35: getfield        org/mozilla/javascript/Context.lastInterpreterFrame:Ljava/lang/Object;
        //    38: ifnull          74
        //    41: aload           21
        //    43: getfield        org/mozilla/javascript/Context.previousInterpreterInvocations:Lorg/mozilla/javascript/ObjArray;
        //    46: ifnonnull       61
        //    49: aload           21
        //    51: new             Lorg/mozilla/javascript/ObjArray;
        //    54: dup            
        //    55: invokespecial   org/mozilla/javascript/ObjArray.<init>:()V
        //    58: putfield        org/mozilla/javascript/Context.previousInterpreterInvocations:Lorg/mozilla/javascript/ObjArray;
        //    61: aload           21
        //    63: getfield        org/mozilla/javascript/Context.previousInterpreterInvocations:Lorg/mozilla/javascript/ObjArray;
        //    66: aload           21
        //    68: getfield        org/mozilla/javascript/Context.lastInterpreterFrame:Ljava/lang/Object;
        //    71: invokevirtual   org/mozilla/javascript/ObjArray.push:(Ljava/lang/Object;)V
        //    74: aconst_null    
        //    75: astore          23
        //    77: aload           20
        //    79: ifnull          141
        //    82: aload           20
        //    84: instanceof      Lorg/mozilla/javascript/Interpreter$GeneratorState;
        //    87: ifeq            112
        //    90: aload           20
        //    92: checkcast       Lorg/mozilla/javascript/Interpreter$GeneratorState;
        //    95: astore          19
        //    97: aload           21
        //    99: aload_1        
        //   100: getstatic       org/mozilla/javascript/ScriptRuntime.emptyArgs:[Ljava/lang/Object;
        //   103: iconst_1       
        //   104: invokestatic    org/mozilla/javascript/Interpreter.enterFrame:(Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Interpreter$CallFrame;[Ljava/lang/Object;Z)V
        //   107: aconst_null    
        //   108: astore_2       
        //   109: goto            148
        //   112: aload           20
        //   114: astore_2       
        //   115: aload           23
        //   117: astore          19
        //   119: aload           20
        //   121: instanceof      Lorg/mozilla/javascript/Interpreter$ContinuationJump;
        //   124: ifne            148
        //   127: invokestatic    org/mozilla/javascript/Kit.codeBug:()Ljava/lang/RuntimeException;
        //   130: pop            
        //   131: aload           20
        //   133: astore_2       
        //   134: aload           23
        //   136: astore          19
        //   138: goto            148
        //   141: aload           23
        //   143: astore          19
        //   145: aload           20
        //   147: astore_2       
        //   148: aload_1        
        //   149: astore          23
        //   151: aload           19
        //   153: astore          25
        //   155: aconst_null    
        //   156: astore          26
        //   158: aconst_null    
        //   159: astore          34
        //   161: aconst_null    
        //   162: astore_1       
        //   163: iconst_m1      
        //   164: istore          11
        //   166: aload_2        
        //   167: astore          19
        //   169: dconst_0       
        //   170: dstore_3       
        //   171: aload           24
        //   173: astore_2       
        //   174: aload           22
        //   176: astore          20
        //   178: aload           26
        //   180: astore          22
        //   182: dload_3        
        //   183: dstore          5
        //   185: aload           19
        //   187: ifnull          245
        //   190: aload           19
        //   192: astore          27
        //   194: aload           23
        //   196: astore          26
        //   198: aload           21
        //   200: aload           19
        //   202: aload           23
        //   204: iload           11
        //   206: iload           16
        //   208: invokestatic    org/mozilla/javascript/Interpreter.processThrowable:(Lorg/mozilla/javascript/Context;Ljava/lang/Object;Lorg/mozilla/javascript/Interpreter$CallFrame;IZ)Lorg/mozilla/javascript/Interpreter$CallFrame;
        //   211: astore          24
        //   213: aload           19
        //   215: astore          27
        //   217: aload           24
        //   219: astore          26
        //   221: aload           24
        //   223: getfield        org/mozilla/javascript/Interpreter$CallFrame.throwable:Ljava/lang/Object;
        //   226: astore          35
        //   228: aload           35
        //   230: astore          27
        //   232: aload           24
        //   234: astore          26
        //   236: aload           24
        //   238: aconst_null    
        //   239: putfield        org/mozilla/javascript/Interpreter$CallFrame.throwable:Ljava/lang/Object;
        //   242: goto            302
        //   245: aload           19
        //   247: astore          35
        //   249: aload           23
        //   251: astore          24
        //   253: aload           25
        //   255: ifnonnull       302
        //   258: aload           19
        //   260: astore          35
        //   262: aload           23
        //   264: astore          24
        //   266: aload           19
        //   268: astore          27
        //   270: aload           23
        //   272: astore          26
        //   274: aload           23
        //   276: getfield        org/mozilla/javascript/Interpreter$CallFrame.frozen:Z
        //   279: ifeq            302
        //   282: aload           19
        //   284: astore          27
        //   286: aload           23
        //   288: astore          26
        //   290: invokestatic    org/mozilla/javascript/Kit.codeBug:()Ljava/lang/RuntimeException;
        //   293: pop            
        //   294: aload           23
        //   296: astore          24
        //   298: aload           19
        //   300: astore          35
        //   302: aload           35
        //   304: astore          32
        //   306: aload           24
        //   308: getfield        org/mozilla/javascript/Interpreter$CallFrame.stack:[Ljava/lang/Object;
        //   311: astore          19
        //   313: aload           24
        //   315: getfield        org/mozilla/javascript/Interpreter$CallFrame.sDbl:[D
        //   318: astore          23
        //   320: aload           24
        //   322: getfield        org/mozilla/javascript/Interpreter$CallFrame.varSource:Lorg/mozilla/javascript/Interpreter$CallFrame;
        //   325: getfield        org/mozilla/javascript/Interpreter$CallFrame.stack:[Ljava/lang/Object;
        //   328: astore          46
        //   330: aload           24
        //   332: getfield        org/mozilla/javascript/Interpreter$CallFrame.varSource:Lorg/mozilla/javascript/Interpreter$CallFrame;
        //   335: getfield        org/mozilla/javascript/Interpreter$CallFrame.sDbl:[D
        //   338: astore          47
        //   340: aload           24
        //   342: getfield        org/mozilla/javascript/Interpreter$CallFrame.varSource:Lorg/mozilla/javascript/Interpreter$CallFrame;
        //   345: getfield        org/mozilla/javascript/Interpreter$CallFrame.stackAttributes:[I
        //   348: astore          48
        //   350: aload           24
        //   352: getfield        org/mozilla/javascript/Interpreter$CallFrame.idata:Lorg/mozilla/javascript/InterpreterData;
        //   355: getfield        org/mozilla/javascript/InterpreterData.itsICode:[B
        //   358: astore          27
        //   360: aload           24
        //   362: getfield        org/mozilla/javascript/Interpreter$CallFrame.idata:Lorg/mozilla/javascript/InterpreterData;
        //   365: getfield        org/mozilla/javascript/InterpreterData.itsStringTable:[Ljava/lang/String;
        //   368: astore          49
        //   370: aload           24
        //   372: getfield        org/mozilla/javascript/Interpreter$CallFrame.savedStackTop:I
        //   375: istore          12
        //   377: aload           21
        //   379: aload           24
        //   381: putfield        org/mozilla/javascript/Context.lastInterpreterFrame:Ljava/lang/Object;
        //   384: aload_1        
        //   385: astore          26
        //   387: aload           24
        //   389: astore_1       
        //   390: aload           27
        //   392: astore          24
        //   394: aload_1        
        //   395: getfield        org/mozilla/javascript/Interpreter$CallFrame.pc:I
        //   398: istore          13
        //   400: aload_1        
        //   401: iload           13
        //   403: iconst_1       
        //   404: iadd           
        //   405: putfield        org/mozilla/javascript/Interpreter$CallFrame.pc:I
        //   408: aload           24
        //   410: iload           13
        //   412: baload         
        //   413: istore          14
        //   415: iload           14
        //   417: sipush          156
        //   420: if_icmpeq       12503
        //   423: iload           14
        //   425: tableswitch {
        //             -128: 10150
        //             -127: 9829
        //             -126: 9511
        //             -125: 9451
        //          default: 456
        //        }
        //   456: iload           14
        //   458: tableswitch {
        //             -118: 9244
        //             -117: 9124
        //             -116: 9055
        //             -115: 9033
        //             -114: 12383
        //             -113: 7101
        //             -112: 6991
        //             -111: 12286
        //             -110: 12234
        //             -109: 6945
        //             -108: 6853
        //             -107: 6702
        //             -106: 6627
        //             -105: 6575
        //             -104: 6521
        //             -103: 12158
        //             -102: 12149
        //             -101: 12140
        //             -100: 6512
        //              -99: 6463
        //              -98: 6414
        //              -97: 6359
        //              -96: 12064
        //              -95: 12058
        //              -94: 12052
        //              -93: 12046
        //              -92: 12040
        //              -91: 12034
        //              -90: 12009
        //              -89: 6049
        //              -88: 5947
        //              -87: 5846
        //              -86: 5745
        //              -85: 5612
        //              -84: 5454
        //              -83: 5312
        //              -82: 5262
        //              -81: 5238
        //              -80: 5168
        //              -79: 5107
        //              -78: 5073
        //              -77: 4997
        //              -76: 4878
        //              -75: 4799
        //              -74: 4755
        //              -73: 4732
        //              -72: 4638
        //              -71: 4597
        //              -70: 4528
        //              -69: 4509
        //              -68: 4386
        //              -67: 4329
        //              -66: 4293
        //              -65: 4169
        //              -64: 4116
        //              -63: 11815
        //              -62: 11738
        //              -61: 11663
        //              -60: 11616
        //              -59: 4083
        //          default: 712
        //        }
        //   712: iload           14
        //   714: tableswitch {
        //                4: 3981
        //                5: 3928
        //                6: 3899
        //                7: 11514
        //                8: 3857
        //                9: 3815
        //               10: 3597
        //               11: 3515
        //               12: 3515
        //               13: 3515
        //               14: 3461
        //               15: 3461
        //               16: 3444
        //               17: 3444
        //               18: 3444
        //               19: 3444
        //               20: 3515
        //               21: 3515
        //               22: 3389
        //               23: 3369
        //               24: 3352
        //               25: 3352
        //               26: 3352
        //               27: 3352
        //               28: 3333
        //               29: 3306
        //               30: 3273
        //               31: 3273
        //               32: 2703
        //               33: 2700
        //               34: 2614
        //               35: 2554
        //               36: 2494
        //               37: 2391
        //               38: 2374
        //               39: 2357
        //               40: 11349
        //               41: 2332
        //               42: 2302
        //               43: 11333
        //               44: 11318
        //               45: 2284
        //               46: 2267
        //               47: 2250
        //               48: 2196
        //               49: 2196
        //               50: 2146
        //               51: 2121
        //               52: 2039
        //               53: 1979
        //               54: 1961
        //               55: 1961
        //               56: 1915
        //               57: 11305
        //               58: 11298
        //               59: 1796
        //               60: 1695
        //               61: 1695
        //               62: 1695
        //               63: 1613
        //               64: 1613
        //               65: 1595
        //               66: 1592
        //               67: 1589
        //               68: 1589
        //               69: 1552
        //               70: 1480
        //               71: 1443
        //               72: 11349
        //               73: 1383
        //               74: 11270
        //               75: 3597
        //               76: 1337
        //               77: 1305
        //               78: 1273
        //               79: 1255
        //               80: 1237
        //               81: 1177
        //               82: 1128
        //          default: 1044
        //        }
        //  1044: aload_1        
        //  1045: getfield        org/mozilla/javascript/Interpreter$CallFrame.idata:Lorg/mozilla/javascript/InterpreterData;
        //  1048: invokestatic    org/mozilla/javascript/Interpreter.dumpICode:(Lorg/mozilla/javascript/InterpreterData;)V
        //  1051: new             Ljava/lang/StringBuilder;
        //  1054: dup            
        //  1055: invokespecial   java/lang/StringBuilder.<init>:()V
        //  1058: astore          19
        //  1060: aload           19
        //  1062: ldc_w           "Unknown icode : "
        //  1065: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  1068: pop            
        //  1069: aload           19
        //  1071: iload           14
        //  1073: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //  1076: pop            
        //  1077: aload           19
        //  1079: ldc_w           " @ pc : "
        //  1082: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  1085: pop            
        //  1086: aload           19
        //  1088: aload_1        
        //  1089: getfield        org/mozilla/javascript/Interpreter$CallFrame.pc:I
        //  1092: iconst_1       
        //  1093: isub           
        //  1094: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //  1097: pop            
        //  1098: new             Ljava/lang/RuntimeException;
        //  1101: dup            
        //  1102: aload           19
        //  1104: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //  1107: invokespecial   java/lang/RuntimeException.<init>:(Ljava/lang/String;)V
        //  1110: athrow         
        //  1111: astore          24
        //  1113: aload           22
        //  1115: astore          23
        //  1117: aload           25
        //  1119: astore          19
        //  1121: aload           26
        //  1123: astore          22
        //  1125: goto            11187
        //  1128: aload           21
        //  1130: aload_1        
        //  1131: aload           19
        //  1133: aload           23
        //  1135: iload           12
        //  1137: iload           11
        //  1139: invokestatic    org/mozilla/javascript/Interpreter.doRefNsName:(Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Interpreter$CallFrame;[Ljava/lang/Object;[DII)I
        //  1142: istore          12
        //  1144: aload_1        
        //  1145: astore          27
        //  1147: aload           23
        //  1149: astore_1       
        //  1150: aload           24
        //  1152: astore          23
        //  1154: aload           20
        //  1156: astore          24
        //  1158: aload           19
        //  1160: astore          20
        //  1162: aload           23
        //  1164: astore          19
        //  1166: aload           27
        //  1168: astore          23
        //  1170: aload           24
        //  1172: astore          27
        //  1174: goto            11374
        //  1177: aload           19
        //  1179: astore          28
        //  1181: aload           28
        //  1183: iload           12
        //  1185: aaload         
        //  1186: astore          26
        //  1188: aload           26
        //  1190: astore          27
        //  1192: aload           26
        //  1194: aload_2        
        //  1195: if_acmpne       1208
        //  1198: aload           23
        //  1200: iload           12
        //  1202: daload         
        //  1203: invokestatic    org/mozilla/javascript/ScriptRuntime.wrapNumber:(D)Ljava/lang/Number;
        //  1206: astore          27
        //  1208: aload           27
        //  1210: astore          26
        //  1212: aload           28
        //  1214: iload           12
        //  1216: aload           27
        //  1218: aload           21
        //  1220: aload_1        
        //  1221: getfield        org/mozilla/javascript/Interpreter$CallFrame.scope:Lorg/mozilla/javascript/Scriptable;
        //  1224: iload           11
        //  1226: invokestatic    org/mozilla/javascript/ScriptRuntime.nameRef:(Ljava/lang/Object;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;I)Lorg/mozilla/javascript/Ref;
        //  1229: aastore        
        //  1230: aload           27
        //  1232: astore          26
        //  1234: goto            2666
        //  1237: aload           21
        //  1239: aload           19
        //  1241: aload           23
        //  1243: iload           12
        //  1245: iload           11
        //  1247: invokestatic    org/mozilla/javascript/Interpreter.doRefNsMember:(Lorg/mozilla/javascript/Context;[Ljava/lang/Object;[DII)I
        //  1250: istore          12
        //  1252: goto            11352
        //  1255: aload           21
        //  1257: aload           19
        //  1259: aload           23
        //  1261: iload           12
        //  1263: iload           11
        //  1265: invokestatic    org/mozilla/javascript/Interpreter.doRefMember:(Lorg/mozilla/javascript/Context;[Ljava/lang/Object;[DII)I
        //  1268: istore          12
        //  1270: goto            11352
        //  1273: aload           19
        //  1275: astore          27
        //  1277: aload           27
        //  1279: iload           12
        //  1281: aaload         
        //  1282: astore          28
        //  1284: aload           28
        //  1286: aload_2        
        //  1287: if_acmpeq       11212
        //  1290: aload           27
        //  1292: iload           12
        //  1294: aload           28
        //  1296: aload           21
        //  1298: invokestatic    org/mozilla/javascript/ScriptRuntime.escapeTextValue:(Ljava/lang/Object;Lorg/mozilla/javascript/Context;)Ljava/lang/String;
        //  1301: aastore        
        //  1302: goto            11212
        //  1305: aload           19
        //  1307: astore          27
        //  1309: aload           27
        //  1311: iload           12
        //  1313: aaload         
        //  1314: astore          28
        //  1316: aload           28
        //  1318: aload_2        
        //  1319: if_acmpeq       11212
        //  1322: aload           27
        //  1324: iload           12
        //  1326: aload           28
        //  1328: aload           21
        //  1330: invokestatic    org/mozilla/javascript/ScriptRuntime.escapeAttributeValue:(Ljava/lang/Object;Lorg/mozilla/javascript/Context;)Ljava/lang/String;
        //  1333: aastore        
        //  1334: goto            11212
        //  1337: aload           19
        //  1339: astore          28
        //  1341: aload           28
        //  1343: iload           12
        //  1345: aaload         
        //  1346: astore          29
        //  1348: aload           29
        //  1350: astore          27
        //  1352: aload           29
        //  1354: aload_2        
        //  1355: if_acmpne       1368
        //  1358: aload           23
        //  1360: iload           12
        //  1362: daload         
        //  1363: invokestatic    org/mozilla/javascript/ScriptRuntime.wrapNumber:(D)Ljava/lang/Number;
        //  1366: astore          27
        //  1368: aload           28
        //  1370: iload           12
        //  1372: aload           27
        //  1374: aload           21
        //  1376: invokestatic    org/mozilla/javascript/ScriptRuntime.setDefaultNamespace:(Ljava/lang/Object;Lorg/mozilla/javascript/Context;)Ljava/lang/Object;
        //  1379: aastore        
        //  1380: goto            11212
        //  1383: aload           19
        //  1385: astore          28
        //  1387: aload           28
        //  1389: iload           12
        //  1391: aaload         
        //  1392: astore          26
        //  1394: aload           26
        //  1396: astore          27
        //  1398: aload           26
        //  1400: aload_2        
        //  1401: if_acmpne       1414
        //  1404: aload           23
        //  1406: iload           12
        //  1408: daload         
        //  1409: invokestatic    org/mozilla/javascript/ScriptRuntime.wrapNumber:(D)Ljava/lang/Number;
        //  1412: astore          27
        //  1414: aload           27
        //  1416: astore          26
        //  1418: aload           28
        //  1420: iload           12
        //  1422: aload           27
        //  1424: aload           22
        //  1426: aload           21
        //  1428: aload_1        
        //  1429: getfield        org/mozilla/javascript/Interpreter$CallFrame.scope:Lorg/mozilla/javascript/Scriptable;
        //  1432: invokestatic    org/mozilla/javascript/ScriptRuntime.specialRef:(Ljava/lang/Object;Ljava/lang/String;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;)Lorg/mozilla/javascript/Ref;
        //  1435: aastore        
        //  1436: aload           27
        //  1438: astore          26
        //  1440: goto            2666
        //  1443: aload           19
        //  1445: astore          28
        //  1447: aload           28
        //  1449: iload           12
        //  1451: aaload         
        //  1452: checkcast       Lorg/mozilla/javascript/Ref;
        //  1455: astore          27
        //  1457: aload           27
        //  1459: astore          26
        //  1461: aload           28
        //  1463: iload           12
        //  1465: aload           27
        //  1467: aload           21
        //  1469: invokestatic    org/mozilla/javascript/ScriptRuntime.refDel:(Lorg/mozilla/javascript/Ref;Lorg/mozilla/javascript/Context;)Ljava/lang/Object;
        //  1472: aastore        
        //  1473: aload           27
        //  1475: astore          26
        //  1477: goto            2666
        //  1480: aload           19
        //  1482: astore          28
        //  1484: aload           28
        //  1486: iload           12
        //  1488: aaload         
        //  1489: astore          26
        //  1491: aload           26
        //  1493: astore          27
        //  1495: aload           26
        //  1497: aload_2        
        //  1498: if_acmpne       1511
        //  1501: aload           23
        //  1503: iload           12
        //  1505: daload         
        //  1506: invokestatic    org/mozilla/javascript/ScriptRuntime.wrapNumber:(D)Ljava/lang/Number;
        //  1509: astore          27
        //  1511: iload           12
        //  1513: iconst_1       
        //  1514: isub           
        //  1515: istore          12
        //  1517: aload           27
        //  1519: astore          26
        //  1521: aload           28
        //  1523: iload           12
        //  1525: aload           28
        //  1527: iload           12
        //  1529: aaload         
        //  1530: checkcast       Lorg/mozilla/javascript/Ref;
        //  1533: aload           27
        //  1535: aload           21
        //  1537: aload_1        
        //  1538: getfield        org/mozilla/javascript/Interpreter$CallFrame.scope:Lorg/mozilla/javascript/Scriptable;
        //  1541: invokestatic    org/mozilla/javascript/ScriptRuntime.refSet:(Lorg/mozilla/javascript/Ref;Ljava/lang/Object;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;)Ljava/lang/Object;
        //  1544: aastore        
        //  1545: aload           27
        //  1547: astore          26
        //  1549: goto            2666
        //  1552: aload           19
        //  1554: astore          28
        //  1556: aload           28
        //  1558: iload           12
        //  1560: aaload         
        //  1561: checkcast       Lorg/mozilla/javascript/Ref;
        //  1564: astore          27
        //  1566: aload           27
        //  1568: astore          26
        //  1570: aload           28
        //  1572: iload           12
        //  1574: aload           27
        //  1576: aload           21
        //  1578: invokestatic    org/mozilla/javascript/ScriptRuntime.refGet:(Lorg/mozilla/javascript/Ref;Lorg/mozilla/javascript/Context;)Ljava/lang/Object;
        //  1581: aastore        
        //  1582: aload           27
        //  1584: astore          26
        //  1586: goto            2666
        //  1589: goto            6172
        //  1592: goto            9926
        //  1595: iload           12
        //  1597: iconst_1       
        //  1598: iadd           
        //  1599: istore          12
        //  1601: aload           19
        //  1603: iload           12
        //  1605: aload_1        
        //  1606: getfield        org/mozilla/javascript/Interpreter$CallFrame.fnOrScript:Lorg/mozilla/javascript/InterpretedFunction;
        //  1609: aastore        
        //  1610: goto            11352
        //  1613: aload           19
        //  1615: astore          28
        //  1617: iload           11
        //  1619: aload_1        
        //  1620: getfield        org/mozilla/javascript/Interpreter$CallFrame.localShift:I
        //  1623: iadd           
        //  1624: istore          11
        //  1626: aload           28
        //  1628: iload           11
        //  1630: aaload         
        //  1631: astore          27
        //  1633: iload           12
        //  1635: iconst_1       
        //  1636: iadd           
        //  1637: istore          12
        //  1639: iload           14
        //  1641: bipush          61
        //  1643: if_icmpne       1664
        //  1646: aload           27
        //  1648: astore          26
        //  1650: aload           27
        //  1652: invokestatic    org/mozilla/javascript/ScriptRuntime.enumNext:(Ljava/lang/Object;)Ljava/lang/Boolean;
        //  1655: astore          29
        //  1657: aload           29
        //  1659: astore          26
        //  1661: goto            1681
        //  1664: aload           27
        //  1666: astore          26
        //  1668: aload           27
        //  1670: aload           21
        //  1672: invokestatic    org/mozilla/javascript/ScriptRuntime.enumId:(Ljava/lang/Object;Lorg/mozilla/javascript/Context;)Ljava/lang/Object;
        //  1675: astore          29
        //  1677: aload           29
        //  1679: astore          26
        //  1681: aload           28
        //  1683: iload           12
        //  1685: aload           26
        //  1687: aastore        
        //  1688: aload           27
        //  1690: astore          26
        //  1692: goto            2666
        //  1695: aload_1        
        //  1696: astore          28
        //  1698: aload           19
        //  1700: astore          29
        //  1702: aload           29
        //  1704: iload           12
        //  1706: aaload         
        //  1707: astore          26
        //  1709: aload           26
        //  1711: astore          27
        //  1713: aload           26
        //  1715: aload_2        
        //  1716: if_acmpne       1729
        //  1719: aload           23
        //  1721: iload           12
        //  1723: daload         
        //  1724: invokestatic    org/mozilla/javascript/ScriptRuntime.wrapNumber:(D)Ljava/lang/Number;
        //  1727: astore          27
        //  1729: iload           12
        //  1731: iconst_1       
        //  1732: isub           
        //  1733: istore          12
        //  1735: aload           27
        //  1737: astore          26
        //  1739: iload           11
        //  1741: aload           28
        //  1743: getfield        org/mozilla/javascript/Interpreter$CallFrame.localShift:I
        //  1746: iadd           
        //  1747: istore          13
        //  1749: iload           14
        //  1751: bipush          58
        //  1753: if_icmpne       11273
        //  1756: iconst_0       
        //  1757: istore          11
        //  1759: goto            1762
        //  1762: aload           27
        //  1764: astore          26
        //  1766: aload           29
        //  1768: iload           13
        //  1770: aload           27
        //  1772: aload           21
        //  1774: aload           28
        //  1776: getfield        org/mozilla/javascript/Interpreter$CallFrame.scope:Lorg/mozilla/javascript/Scriptable;
        //  1779: iload           11
        //  1781: invokestatic    org/mozilla/javascript/ScriptRuntime.enumInit:(Ljava/lang/Object;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;I)Ljava/lang/Object;
        //  1784: aastore        
        //  1785: aload           27
        //  1787: astore          26
        //  1789: iload           13
        //  1791: istore          11
        //  1793: goto            2666
        //  1796: aload_1        
        //  1797: astore          28
        //  1799: aload           19
        //  1801: astore          29
        //  1803: iload           12
        //  1805: iconst_1       
        //  1806: isub           
        //  1807: istore          12
        //  1809: iload           11
        //  1811: aload           28
        //  1813: getfield        org/mozilla/javascript/Interpreter$CallFrame.localShift:I
        //  1816: iadd           
        //  1817: istore          13
        //  1819: aload           28
        //  1821: getfield        org/mozilla/javascript/Interpreter$CallFrame.idata:Lorg/mozilla/javascript/InterpreterData;
        //  1824: getfield        org/mozilla/javascript/InterpreterData.itsICode:[B
        //  1827: aload           28
        //  1829: getfield        org/mozilla/javascript/Interpreter$CallFrame.pc:I
        //  1832: baload         
        //  1833: ifeq            11292
        //  1836: iconst_1       
        //  1837: istore          11
        //  1839: goto            1842
        //  1842: aload           29
        //  1844: iload           12
        //  1846: iconst_1       
        //  1847: iadd           
        //  1848: aaload         
        //  1849: checkcast       Ljava/lang/Throwable;
        //  1852: astore          30
        //  1854: iload           11
        //  1856: ifne            1865
        //  1859: aconst_null    
        //  1860: astore          27
        //  1862: goto            1875
        //  1865: aload           29
        //  1867: iload           13
        //  1869: aaload         
        //  1870: checkcast       Lorg/mozilla/javascript/Scriptable;
        //  1873: astore          27
        //  1875: aload           29
        //  1877: iload           13
        //  1879: aload           30
        //  1881: aload           27
        //  1883: aload           22
        //  1885: aload           21
        //  1887: aload           28
        //  1889: getfield        org/mozilla/javascript/Interpreter$CallFrame.scope:Lorg/mozilla/javascript/Scriptable;
        //  1892: invokestatic    org/mozilla/javascript/ScriptRuntime.newCatchScope:(Ljava/lang/Throwable;Lorg/mozilla/javascript/Scriptable;Ljava/lang/String;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;)Lorg/mozilla/javascript/Scriptable;
        //  1895: aastore        
        //  1896: aload           28
        //  1898: aload           28
        //  1900: getfield        org/mozilla/javascript/Interpreter$CallFrame.pc:I
        //  1903: iconst_1       
        //  1904: iadd           
        //  1905: putfield        org/mozilla/javascript/Interpreter$CallFrame.pc:I
        //  1908: iload           13
        //  1910: istore          11
        //  1912: goto            11352
        //  1915: aload           23
        //  1917: astore          27
        //  1919: aload           19
        //  1921: astore          28
        //  1923: iload           12
        //  1925: iconst_1       
        //  1926: iadd           
        //  1927: istore          12
        //  1929: iload           11
        //  1931: aload_1        
        //  1932: getfield        org/mozilla/javascript/Interpreter$CallFrame.localShift:I
        //  1935: iadd           
        //  1936: istore          11
        //  1938: aload           28
        //  1940: iload           12
        //  1942: aload           28
        //  1944: iload           11
        //  1946: aaload         
        //  1947: aastore        
        //  1948: aload           27
        //  1950: iload           12
        //  1952: aload           27
        //  1954: iload           11
        //  1956: daload         
        //  1957: dastore        
        //  1958: goto            11352
        //  1961: aload           21
        //  1963: iload           14
        //  1965: aload           19
        //  1967: aload           23
        //  1969: iload           12
        //  1971: invokestatic    org/mozilla/javascript/Interpreter.doInOrInstanceof:(Lorg/mozilla/javascript/Context;I[Ljava/lang/Object;[DI)I
        //  1974: istore          12
        //  1976: goto            11352
        //  1979: aload_1        
        //  1980: getfield        org/mozilla/javascript/Interpreter$CallFrame.localShift:I
        //  1983: istore          12
        //  1985: aload           19
        //  1987: iload           11
        //  1989: iload           12
        //  1991: iadd           
        //  1992: aaload         
        //  1993: astore          19
        //  1995: aload           26
        //  1997: astore          24
        //  1999: aload           19
        //  2001: astore          26
        //  2003: aload           21
        //  2005: astore          19
        //  2007: aload           22
        //  2009: astore          23
        //  2011: aload           25
        //  2013: astore          27
        //  2015: aload           20
        //  2017: astore          21
        //  2019: aload           19
        //  2021: astore          22
        //  2023: aload_1        
        //  2024: astore          25
        //  2026: aload_2        
        //  2027: astore          20
        //  2029: aload           27
        //  2031: astore          19
        //  2033: aload           26
        //  2035: astore_2       
        //  2036: goto            10525
        //  2039: aload_1        
        //  2040: astore          27
        //  2042: aload           19
        //  2044: iload           12
        //  2046: aaload         
        //  2047: astore          26
        //  2049: aload           26
        //  2051: astore          19
        //  2053: aload           26
        //  2055: aload_2        
        //  2056: if_acmpne       2069
        //  2059: aload           23
        //  2061: iload           12
        //  2063: daload         
        //  2064: invokestatic    org/mozilla/javascript/ScriptRuntime.wrapNumber:(D)Ljava/lang/Number;
        //  2067: astore          19
        //  2069: aload           19
        //  2071: astore          26
        //  2073: aload           24
        //  2075: aload           27
        //  2077: getfield        org/mozilla/javascript/Interpreter$CallFrame.pc:I
        //  2080: invokestatic    org/mozilla/javascript/Interpreter.getIndex:([BI)I
        //  2083: istore          11
        //  2085: aload           19
        //  2087: astore          26
        //  2089: new             Lorg/mozilla/javascript/JavaScriptException;
        //  2092: dup            
        //  2093: aload           19
        //  2095: aload           27
        //  2097: getfield        org/mozilla/javascript/Interpreter$CallFrame.idata:Lorg/mozilla/javascript/InterpreterData;
        //  2100: getfield        org/mozilla/javascript/InterpreterData.itsSourceFile:Ljava/lang/String;
        //  2103: iload           11
        //  2105: invokespecial   org/mozilla/javascript/JavaScriptException.<init>:(Ljava/lang/Object;Ljava/lang/String;I)V
        //  2108: astore          23
        //  2110: aload           23
        //  2112: astore          26
        //  2114: aload           19
        //  2116: astore          24
        //  2118: goto            2003
        //  2121: iload           12
        //  2123: iconst_1       
        //  2124: iadd           
        //  2125: istore          12
        //  2127: aload           19
        //  2129: iload           12
        //  2131: aload           21
        //  2133: aload_1        
        //  2134: getfield        org/mozilla/javascript/Interpreter$CallFrame.scope:Lorg/mozilla/javascript/Scriptable;
        //  2137: aload           22
        //  2139: invokestatic    org/mozilla/javascript/ScriptRuntime.bind:(Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;Ljava/lang/String;)Lorg/mozilla/javascript/Scriptable;
        //  2142: aastore        
        //  2143: goto            11352
        //  2146: aload_1        
        //  2147: astore          28
        //  2149: aload           28
        //  2151: getfield        org/mozilla/javascript/Interpreter$CallFrame.idata:Lorg/mozilla/javascript/InterpreterData;
        //  2154: getfield        org/mozilla/javascript/InterpreterData.itsRegExpLiterals:[Ljava/lang/Object;
        //  2157: iload           11
        //  2159: aaload         
        //  2160: astore          27
        //  2162: iload           12
        //  2164: iconst_1       
        //  2165: iadd           
        //  2166: istore          12
        //  2168: aload           27
        //  2170: astore          26
        //  2172: aload           19
        //  2174: iload           12
        //  2176: aload           21
        //  2178: aload           28
        //  2180: getfield        org/mozilla/javascript/Interpreter$CallFrame.scope:Lorg/mozilla/javascript/Scriptable;
        //  2183: aload           27
        //  2185: invokestatic    org/mozilla/javascript/ScriptRuntime.wrapRegExp:(Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;Ljava/lang/Object;)Lorg/mozilla/javascript/Scriptable;
        //  2188: aastore        
        //  2189: aload           27
        //  2191: astore          26
        //  2193: goto            2666
        //  2196: aload           19
        //  2198: astore          27
        //  2200: iload           12
        //  2202: iconst_1       
        //  2203: isub           
        //  2204: istore          13
        //  2206: aload           27
        //  2208: aload           23
        //  2210: iload           13
        //  2212: invokestatic    org/mozilla/javascript/Interpreter.doShallowEquals:([Ljava/lang/Object;[DI)Z
        //  2215: istore          17
        //  2217: iload           14
        //  2219: bipush          47
        //  2221: if_icmpne       11312
        //  2224: iconst_1       
        //  2225: istore          12
        //  2227: goto            2230
        //  2230: aload           27
        //  2232: iload           13
        //  2234: iload           17
        //  2236: iload           12
        //  2238: ixor           
        //  2239: invokestatic    org/mozilla/javascript/ScriptRuntime.wrapBoolean:(Z)Ljava/lang/Boolean;
        //  2242: aastore        
        //  2243: iload           13
        //  2245: istore          12
        //  2247: goto            11352
        //  2250: iload           12
        //  2252: iconst_1       
        //  2253: iadd           
        //  2254: istore          12
        //  2256: aload           19
        //  2258: iload           12
        //  2260: getstatic       java/lang/Boolean.TRUE:Ljava/lang/Boolean;
        //  2263: aastore        
        //  2264: goto            11352
        //  2267: iload           12
        //  2269: iconst_1       
        //  2270: iadd           
        //  2271: istore          12
        //  2273: aload           19
        //  2275: iload           12
        //  2277: getstatic       java/lang/Boolean.FALSE:Ljava/lang/Boolean;
        //  2280: aastore        
        //  2281: goto            11352
        //  2284: iload           12
        //  2286: iconst_1       
        //  2287: iadd           
        //  2288: istore          12
        //  2290: aload           19
        //  2292: iload           12
        //  2294: aload_1        
        //  2295: getfield        org/mozilla/javascript/Interpreter$CallFrame.thisObj:Lorg/mozilla/javascript/Scriptable;
        //  2298: aastore        
        //  2299: goto            11352
        //  2302: iload           12
        //  2304: iconst_1       
        //  2305: iadd           
        //  2306: istore          12
        //  2308: aload           19
        //  2310: iload           12
        //  2312: aload_2        
        //  2313: aastore        
        //  2314: aload           23
        //  2316: iload           12
        //  2318: aload_1        
        //  2319: getfield        org/mozilla/javascript/Interpreter$CallFrame.idata:Lorg/mozilla/javascript/InterpreterData;
        //  2322: getfield        org/mozilla/javascript/InterpreterData.itsDoubleTable:[D
        //  2325: iload           11
        //  2327: daload         
        //  2328: dastore        
        //  2329: goto            11352
        //  2332: iload           12
        //  2334: iconst_1       
        //  2335: iadd           
        //  2336: istore          12
        //  2338: aload           19
        //  2340: iload           12
        //  2342: aload           21
        //  2344: aload_1        
        //  2345: getfield        org/mozilla/javascript/Interpreter$CallFrame.scope:Lorg/mozilla/javascript/Scriptable;
        //  2348: aload           22
        //  2350: invokestatic    org/mozilla/javascript/ScriptRuntime.name:(Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;Ljava/lang/String;)Ljava/lang/Object;
        //  2353: aastore        
        //  2354: goto            11352
        //  2357: aload           21
        //  2359: aload_1        
        //  2360: aload           19
        //  2362: aload           23
        //  2364: iload           12
        //  2366: invokestatic    org/mozilla/javascript/Interpreter.doSetElem:(Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Interpreter$CallFrame;[Ljava/lang/Object;[DI)I
        //  2369: istore          12
        //  2371: goto            11352
        //  2374: aload           21
        //  2376: aload_1        
        //  2377: aload           19
        //  2379: aload           23
        //  2381: iload           12
        //  2383: invokestatic    org/mozilla/javascript/Interpreter.doGetElem:(Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Interpreter$CallFrame;[Ljava/lang/Object;[DI)I
        //  2386: istore          12
        //  2388: goto            11352
        //  2391: aload           23
        //  2393: astore          30
        //  2395: aload           19
        //  2397: astore          29
        //  2399: aload           29
        //  2401: iload           12
        //  2403: aaload         
        //  2404: astore          26
        //  2406: aload           26
        //  2408: astore          27
        //  2410: aload           26
        //  2412: aload_2        
        //  2413: if_acmpne       2426
        //  2416: aload           30
        //  2418: iload           12
        //  2420: daload         
        //  2421: invokestatic    org/mozilla/javascript/ScriptRuntime.wrapNumber:(D)Ljava/lang/Number;
        //  2424: astore          27
        //  2426: iload           12
        //  2428: iconst_1       
        //  2429: isub           
        //  2430: istore          12
        //  2432: aload           29
        //  2434: iload           12
        //  2436: aaload         
        //  2437: astore          26
        //  2439: aload           26
        //  2441: astore          28
        //  2443: aload           26
        //  2445: aload_2        
        //  2446: if_acmpne       2463
        //  2449: aload           27
        //  2451: astore          26
        //  2453: aload           30
        //  2455: iload           12
        //  2457: daload         
        //  2458: invokestatic    org/mozilla/javascript/ScriptRuntime.wrapNumber:(D)Ljava/lang/Number;
        //  2461: astore          28
        //  2463: aload           27
        //  2465: astore          26
        //  2467: aload           29
        //  2469: iload           12
        //  2471: aload           28
        //  2473: aload           22
        //  2475: aload           27
        //  2477: aload           21
        //  2479: aload_1        
        //  2480: getfield        org/mozilla/javascript/Interpreter$CallFrame.scope:Lorg/mozilla/javascript/Scriptable;
        //  2483: invokestatic    org/mozilla/javascript/ScriptRuntime.setObjectProp:(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Object;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;)Ljava/lang/Object;
        //  2486: aastore        
        //  2487: aload           27
        //  2489: astore          26
        //  2491: goto            2666
        //  2494: aload           19
        //  2496: astore          28
        //  2498: aload           28
        //  2500: iload           12
        //  2502: aaload         
        //  2503: astore          26
        //  2505: aload           26
        //  2507: astore          27
        //  2509: aload           26
        //  2511: aload_2        
        //  2512: if_acmpne       2525
        //  2515: aload           23
        //  2517: iload           12
        //  2519: daload         
        //  2520: invokestatic    org/mozilla/javascript/ScriptRuntime.wrapNumber:(D)Ljava/lang/Number;
        //  2523: astore          27
        //  2525: aload           27
        //  2527: astore          26
        //  2529: aload           28
        //  2531: iload           12
        //  2533: aload           27
        //  2535: aload           22
        //  2537: aload           21
        //  2539: aload_1        
        //  2540: getfield        org/mozilla/javascript/Interpreter$CallFrame.scope:Lorg/mozilla/javascript/Scriptable;
        //  2543: invokestatic    org/mozilla/javascript/ScriptRuntime.getObjectPropNoWarn:(Ljava/lang/Object;Ljava/lang/String;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;)Ljava/lang/Object;
        //  2546: aastore        
        //  2547: aload           27
        //  2549: astore          26
        //  2551: goto            2666
        //  2554: aload           19
        //  2556: astore          28
        //  2558: aload           28
        //  2560: iload           12
        //  2562: aaload         
        //  2563: astore          26
        //  2565: aload           26
        //  2567: astore          27
        //  2569: aload           26
        //  2571: aload_2        
        //  2572: if_acmpne       2585
        //  2575: aload           23
        //  2577: iload           12
        //  2579: daload         
        //  2580: invokestatic    org/mozilla/javascript/ScriptRuntime.wrapNumber:(D)Ljava/lang/Number;
        //  2583: astore          27
        //  2585: aload           27
        //  2587: astore          26
        //  2589: aload           28
        //  2591: iload           12
        //  2593: aload           27
        //  2595: aload           22
        //  2597: aload           21
        //  2599: aload_1        
        //  2600: getfield        org/mozilla/javascript/Interpreter$CallFrame.scope:Lorg/mozilla/javascript/Scriptable;
        //  2603: invokestatic    org/mozilla/javascript/ScriptRuntime.getObjectProp:(Ljava/lang/Object;Ljava/lang/String;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;)Ljava/lang/Object;
        //  2606: aastore        
        //  2607: aload           27
        //  2609: astore          26
        //  2611: goto            2666
        //  2614: aload           19
        //  2616: astore          28
        //  2618: aload           28
        //  2620: iload           12
        //  2622: aaload         
        //  2623: astore          26
        //  2625: aload           26
        //  2627: astore          27
        //  2629: aload           26
        //  2631: aload_2        
        //  2632: if_acmpne       2648
        //  2635: aload           23
        //  2637: iload           12
        //  2639: daload         
        //  2640: invokestatic    org/mozilla/javascript/ScriptRuntime.wrapNumber:(D)Ljava/lang/Number;
        //  2643: astore          27
        //  2645: goto            2648
        //  2648: aload           27
        //  2650: astore          26
        //  2652: aload           28
        //  2654: iload           12
        //  2656: aload           27
        //  2658: invokestatic    org/mozilla/javascript/ScriptRuntime.typeof:(Ljava/lang/Object;)Ljava/lang/String;
        //  2661: aastore        
        //  2662: aload           27
        //  2664: astore          26
        //  2666: goto            11352
        //  2669: astore          19
        //  2671: aload           22
        //  2673: astore          23
        //  2675: aload_2        
        //  2676: astore          24
        //  2678: aload           21
        //  2680: astore          22
        //  2682: aload           20
        //  2684: astore          21
        //  2686: aload           19
        //  2688: astore_2       
        //  2689: aload           25
        //  2691: astore          19
        //  2693: aload           24
        //  2695: astore          20
        //  2697: goto            11209
        //  2700: goto            4083
        //  2703: aload           25
        //  2705: astore          29
        //  2707: iload           16
        //  2709: istore          17
        //  2711: aload           20
        //  2713: astore          28
        //  2715: aload           22
        //  2717: astore          30
        //  2719: aload_1        
        //  2720: astore          27
        //  2722: iload           17
        //  2724: ifeq            2743
        //  2727: aload           21
        //  2729: aload           21
        //  2731: getfield        org/mozilla/javascript/Context.instructionCount:I
        //  2734: bipush          100
        //  2736: iadd           
        //  2737: putfield        org/mozilla/javascript/Context.instructionCount:I
        //  2740: goto            2743
        //  2743: iload           12
        //  2745: iload           11
        //  2747: isub           
        //  2748: istore          12
        //  2750: aload           19
        //  2752: iload           12
        //  2754: aaload         
        //  2755: astore          26
        //  2757: aload           26
        //  2759: instanceof      Lorg/mozilla/javascript/InterpretedFunction;
        //  2762: istore          18
        //  2764: iload           18
        //  2766: ifeq            2984
        //  2769: aload           26
        //  2771: checkcast       Lorg/mozilla/javascript/InterpretedFunction;
        //  2774: astore          31
        //  2776: aload           27
        //  2778: getfield        org/mozilla/javascript/Interpreter$CallFrame.fnOrScript:Lorg/mozilla/javascript/InterpretedFunction;
        //  2781: getfield        org/mozilla/javascript/InterpretedFunction.securityDomain:Ljava/lang/Object;
        //  2784: aload           31
        //  2786: getfield        org/mozilla/javascript/InterpretedFunction.securityDomain:Ljava/lang/Object;
        //  2789: if_acmpne       2984
        //  2792: aload           31
        //  2794: aload           21
        //  2796: aload           27
        //  2798: getfield        org/mozilla/javascript/Interpreter$CallFrame.scope:Lorg/mozilla/javascript/Scriptable;
        //  2801: invokevirtual   org/mozilla/javascript/InterpretedFunction.createObject:(Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;)Lorg/mozilla/javascript/Scriptable;
        //  2804: astore          33
        //  2806: new             Lorg/mozilla/javascript/Interpreter$CallFrame;
        //  2809: dup            
        //  2810: aconst_null    
        //  2811: invokespecial   org/mozilla/javascript/Interpreter$CallFrame.<init>:(Lorg/mozilla/javascript/Interpreter$1;)V
        //  2814: astore          24
        //  2816: aload           27
        //  2818: getfield        org/mozilla/javascript/Interpreter$CallFrame.scope:Lorg/mozilla/javascript/Scriptable;
        //  2821: astore          36
        //  2823: aload           21
        //  2825: aload           36
        //  2827: aload           33
        //  2829: aload           19
        //  2831: aload           23
        //  2833: iload           12
        //  2835: iconst_1       
        //  2836: iadd           
        //  2837: iload           11
        //  2839: aload           31
        //  2841: aload           27
        //  2843: aload           24
        //  2845: invokestatic    org/mozilla/javascript/Interpreter.initFrame:(Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;Lorg/mozilla/javascript/Scriptable;[Ljava/lang/Object;[DIILorg/mozilla/javascript/InterpretedFunction;Lorg/mozilla/javascript/Interpreter$CallFrame;Lorg/mozilla/javascript/Interpreter$CallFrame;)V
        //  2848: aload           19
        //  2850: iload           12
        //  2852: aload           33
        //  2854: aastore        
        //  2855: aload           27
        //  2857: iload           12
        //  2859: putfield        org/mozilla/javascript/Interpreter$CallFrame.savedStackTop:I
        //  2862: aload           27
        //  2864: iload           14
        //  2866: putfield        org/mozilla/javascript/Interpreter$CallFrame.savedCallOp:I
        //  2869: aload           24
        //  2871: astore          23
        //  2873: aload           26
        //  2875: astore_1       
        //  2876: iload           17
        //  2878: istore          16
        //  2880: aload           28
        //  2882: astore          20
        //  2884: aload           30
        //  2886: astore          22
        //  2888: aload           29
        //  2890: astore          25
        //  2892: aload           35
        //  2894: astore          19
        //  2896: aload_0        
        //  2897: astore          21
        //  2899: goto            182
        //  2902: astore          19
        //  2904: aload_2        
        //  2905: astore          21
        //  2907: aload           19
        //  2909: astore_2       
        //  2910: aload           21
        //  2912: astore          19
        //  2914: goto            3174
        //  2917: astore          19
        //  2919: aload           27
        //  2921: astore_1       
        //  2922: aload           28
        //  2924: astore          21
        //  2926: aload_2        
        //  2927: astore          20
        //  2929: aload           30
        //  2931: astore          23
        //  2933: aload_0        
        //  2934: astore          22
        //  2936: aload           19
        //  2938: astore_2       
        //  2939: aload           29
        //  2941: astore          19
        //  2943: iload           17
        //  2945: istore          16
        //  2947: goto            10498
        //  2950: astore          19
        //  2952: iload           17
        //  2954: istore          16
        //  2956: aload           27
        //  2958: astore_1       
        //  2959: aload_2        
        //  2960: astore          20
        //  2962: aload           21
        //  2964: astore          22
        //  2966: aload           28
        //  2968: astore          21
        //  2970: aload           30
        //  2972: astore          23
        //  2974: aload           19
        //  2976: astore_2       
        //  2977: aload           29
        //  2979: astore          19
        //  2981: goto            10498
        //  2984: aload           26
        //  2986: instanceof      Lorg/mozilla/javascript/Function;
        //  2989: istore          18
        //  2991: iload           18
        //  2993: ifne            3041
        //  2996: aload_2        
        //  2997: astore          19
        //  2999: aload           26
        //  3001: aload           19
        //  3003: if_acmpne       3022
        //  3006: aload           23
        //  3008: iload           12
        //  3010: daload         
        //  3011: invokestatic    org/mozilla/javascript/ScriptRuntime.wrapNumber:(D)Ljava/lang/Number;
        //  3014: astore          21
        //  3016: aload           21
        //  3018: astore_2       
        //  3019: goto            3025
        //  3022: aload           26
        //  3024: astore_2       
        //  3025: aload_2        
        //  3026: invokestatic    org/mozilla/javascript/ScriptRuntime.notFunctionError:(Ljava/lang/Object;)Ljava/lang/RuntimeException;
        //  3029: athrow         
        //  3030: astore          21
        //  3032: aload_2        
        //  3033: astore          26
        //  3035: aload           21
        //  3037: astore_2       
        //  3038: goto            3174
        //  3041: aload_2        
        //  3042: astore          21
        //  3044: aload           26
        //  3046: checkcast       Lorg/mozilla/javascript/Function;
        //  3049: astore          28
        //  3051: aload           28
        //  3053: instanceof      Lorg/mozilla/javascript/IdFunctionObject;
        //  3056: ifeq            3099
        //  3059: aload           28
        //  3061: checkcast       Lorg/mozilla/javascript/IdFunctionObject;
        //  3064: invokestatic    org/mozilla/javascript/NativeContinuation.isContinuationConstructor:(Lorg/mozilla/javascript/IdFunctionObject;)Z
        //  3067: ifeq            3099
        //  3070: aload           27
        //  3072: getfield        org/mozilla/javascript/Interpreter$CallFrame.stack:[Ljava/lang/Object;
        //  3075: astore          28
        //  3077: aload           27
        //  3079: getfield        org/mozilla/javascript/Interpreter$CallFrame.parentFrame:Lorg/mozilla/javascript/Interpreter$CallFrame;
        //  3082: astore          29
        //  3084: aload           28
        //  3086: iload           12
        //  3088: aload_0        
        //  3089: aload           29
        //  3091: iconst_0       
        //  3092: invokestatic    org/mozilla/javascript/Interpreter.captureContinuation:(Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Interpreter$CallFrame;Z)Lorg/mozilla/javascript/NativeContinuation;
        //  3095: aastore        
        //  3096: goto            3136
        //  3099: iload           12
        //  3101: istore          13
        //  3103: aload           19
        //  3105: aload           23
        //  3107: iload           13
        //  3109: iconst_1       
        //  3110: iadd           
        //  3111: iload           11
        //  3113: invokestatic    org/mozilla/javascript/Interpreter.getArgsArray:([Ljava/lang/Object;[DII)[Ljava/lang/Object;
        //  3116: astore_1       
        //  3117: aload           19
        //  3119: iload           13
        //  3121: aload           28
        //  3123: aload_0        
        //  3124: aload           27
        //  3126: getfield        org/mozilla/javascript/Interpreter$CallFrame.scope:Lorg/mozilla/javascript/Scriptable;
        //  3129: aload_1        
        //  3130: invokeinterface org/mozilla/javascript/Function.construct:(Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;[Ljava/lang/Object;)Lorg/mozilla/javascript/Scriptable;
        //  3135: aastore        
        //  3136: aload           23
        //  3138: astore_1       
        //  3139: aload           21
        //  3141: astore_2       
        //  3142: aload_0        
        //  3143: astore          21
        //  3145: aload           27
        //  3147: astore          23
        //  3149: aload           19
        //  3151: astore          27
        //  3153: goto            3543
        //  3156: astore_2       
        //  3157: aload_0        
        //  3158: astore          19
        //  3160: aload           27
        //  3162: astore_1       
        //  3163: goto            3574
        //  3166: astore          21
        //  3168: aload_2        
        //  3169: astore          19
        //  3171: aload           21
        //  3173: astore_2       
        //  3174: aload           22
        //  3176: astore          23
        //  3178: aload           20
        //  3180: astore          21
        //  3182: aload           19
        //  3184: astore          20
        //  3186: aload           25
        //  3188: astore          19
        //  3190: aload_2        
        //  3191: astore          22
        //  3193: aload_0        
        //  3194: astore          24
        //  3196: aload           22
        //  3198: astore_2       
        //  3199: aload           24
        //  3201: astore          22
        //  3203: goto            11209
        //  3206: astore          19
        //  3208: aload           27
        //  3210: astore_1       
        //  3211: aload           28
        //  3213: astore          21
        //  3215: aload_2        
        //  3216: astore          20
        //  3218: aload           30
        //  3220: astore          23
        //  3222: aload_0        
        //  3223: astore          22
        //  3225: aload           19
        //  3227: astore_2       
        //  3228: aload           29
        //  3230: astore          19
        //  3232: iload           17
        //  3234: istore          16
        //  3236: goto            10498
        //  3239: astore          19
        //  3241: iload           17
        //  3243: istore          16
        //  3245: aload           27
        //  3247: astore_1       
        //  3248: aload_2        
        //  3249: astore          20
        //  3251: aload           30
        //  3253: astore          23
        //  3255: aload           19
        //  3257: astore_2       
        //  3258: aload           29
        //  3260: astore          19
        //  3262: aload           21
        //  3264: astore          22
        //  3266: aload           28
        //  3268: astore          21
        //  3270: goto            10498
        //  3273: aload_1        
        //  3274: iload           12
        //  3276: invokestatic    org/mozilla/javascript/Interpreter.stack_double:(Lorg/mozilla/javascript/Interpreter$CallFrame;I)D
        //  3279: dstore          9
        //  3281: aload           19
        //  3283: iload           12
        //  3285: aload_2        
        //  3286: aastore        
        //  3287: dload           9
        //  3289: dstore          7
        //  3291: iload           14
        //  3293: bipush          29
        //  3295: if_icmpne       11421
        //  3298: dload           9
        //  3300: dneg           
        //  3301: dstore          7
        //  3303: goto            11421
        //  3306: aload_1        
        //  3307: iload           12
        //  3309: invokestatic    org/mozilla/javascript/Interpreter.stack_int32:(Lorg/mozilla/javascript/Interpreter$CallFrame;I)I
        //  3312: istore          13
        //  3314: aload           19
        //  3316: iload           12
        //  3318: aload_2        
        //  3319: aastore        
        //  3320: aload           23
        //  3322: iload           12
        //  3324: iload           13
        //  3326: iconst_m1      
        //  3327: ixor           
        //  3328: i2d            
        //  3329: dastore        
        //  3330: goto            11428
        //  3333: aload           19
        //  3335: iload           12
        //  3337: aload_1        
        //  3338: iload           12
        //  3340: invokestatic    org/mozilla/javascript/Interpreter.stack_boolean:(Lorg/mozilla/javascript/Interpreter$CallFrame;I)Z
        //  3343: iconst_1       
        //  3344: ixor           
        //  3345: invokestatic    org/mozilla/javascript/ScriptRuntime.wrapBoolean:(Z)Ljava/lang/Boolean;
        //  3348: aastore        
        //  3349: goto            11428
        //  3352: aload_1        
        //  3353: iload           14
        //  3355: aload           19
        //  3357: aload           23
        //  3359: iload           12
        //  3361: invokestatic    org/mozilla/javascript/Interpreter.doArithmetic:(Lorg/mozilla/javascript/Interpreter$CallFrame;I[Ljava/lang/Object;[DI)I
        //  3364: istore          12
        //  3366: goto            3529
        //  3369: iload           12
        //  3371: iconst_1       
        //  3372: isub           
        //  3373: istore          12
        //  3375: aload           19
        //  3377: aload           23
        //  3379: iload           12
        //  3381: aload           21
        //  3383: invokestatic    org/mozilla/javascript/Interpreter.doAdd:([Ljava/lang/Object;[DILorg/mozilla/javascript/Context;)V
        //  3386: goto            3529
        //  3389: aload_1        
        //  3390: astore          27
        //  3392: aload           27
        //  3394: iload           12
        //  3396: iconst_1       
        //  3397: isub           
        //  3398: invokestatic    org/mozilla/javascript/Interpreter.stack_double:(Lorg/mozilla/javascript/Interpreter$CallFrame;I)D
        //  3401: dstore          7
        //  3403: aload           27
        //  3405: iload           12
        //  3407: invokestatic    org/mozilla/javascript/Interpreter.stack_int32:(Lorg/mozilla/javascript/Interpreter$CallFrame;I)I
        //  3410: istore          13
        //  3412: iload           12
        //  3414: iconst_1       
        //  3415: isub           
        //  3416: istore          12
        //  3418: aload           19
        //  3420: iload           12
        //  3422: aload_2        
        //  3423: aastore        
        //  3424: aload           23
        //  3426: iload           12
        //  3428: dload           7
        //  3430: invokestatic    org/mozilla/javascript/ScriptRuntime.toUint32:(D)J
        //  3433: iload           13
        //  3435: bipush          31
        //  3437: iand           
        //  3438: lushr          
        //  3439: l2d            
        //  3440: dastore        
        //  3441: goto            3529
        //  3444: aload_1        
        //  3445: iload           14
        //  3447: aload           19
        //  3449: aload           23
        //  3451: iload           12
        //  3453: invokestatic    org/mozilla/javascript/Interpreter.doCompare:(Lorg/mozilla/javascript/Interpreter$CallFrame;I[Ljava/lang/Object;[DI)I
        //  3456: istore          12
        //  3458: goto            3529
        //  3461: aload           19
        //  3463: astore          27
        //  3465: iload           12
        //  3467: iconst_1       
        //  3468: isub           
        //  3469: istore          13
        //  3471: aload           27
        //  3473: aload           23
        //  3475: iload           13
        //  3477: invokestatic    org/mozilla/javascript/Interpreter.doEquals:([Ljava/lang/Object;[DI)Z
        //  3480: istore          17
        //  3482: iload           14
        //  3484: bipush          13
        //  3486: if_icmpne       11468
        //  3489: iconst_1       
        //  3490: istore          12
        //  3492: goto            3495
        //  3495: aload           27
        //  3497: iload           13
        //  3499: iload           17
        //  3501: iload           12
        //  3503: ixor           
        //  3504: invokestatic    org/mozilla/javascript/ScriptRuntime.wrapBoolean:(Z)Ljava/lang/Boolean;
        //  3507: aastore        
        //  3508: iload           13
        //  3510: istore          12
        //  3512: goto            3529
        //  3515: aload_1        
        //  3516: iload           14
        //  3518: aload           19
        //  3520: aload           23
        //  3522: iload           12
        //  3524: invokestatic    org/mozilla/javascript/Interpreter.doBitOp:(Lorg/mozilla/javascript/Interpreter$CallFrame;I[Ljava/lang/Object;[DI)I
        //  3527: istore          12
        //  3529: aload           23
        //  3531: astore          28
        //  3533: aload_1        
        //  3534: astore          23
        //  3536: aload           19
        //  3538: astore          27
        //  3540: aload           28
        //  3542: astore_1       
        //  3543: aload           20
        //  3545: astore          28
        //  3547: aload           24
        //  3549: astore          19
        //  3551: aload           27
        //  3553: astore          20
        //  3555: aload           28
        //  3557: astore          24
        //  3559: goto            12095
        //  3562: astore          23
        //  3564: aload           21
        //  3566: astore          19
        //  3568: aload_2        
        //  3569: astore          21
        //  3571: aload           23
        //  3573: astore_2       
        //  3574: aload           20
        //  3576: astore          24
        //  3578: aload           22
        //  3580: astore          23
        //  3582: aload           21
        //  3584: astore          20
        //  3586: aload           19
        //  3588: astore          22
        //  3590: aload           24
        //  3592: astore          21
        //  3594: goto            12376
        //  3597: aload           19
        //  3599: astore          33
        //  3601: iload           16
        //  3603: istore          17
        //  3605: aload           20
        //  3607: astore          27
        //  3609: aload           21
        //  3611: astore          30
        //  3613: aload_1        
        //  3614: astore          29
        //  3616: aload_2        
        //  3617: astore          28
        //  3619: aload           33
        //  3621: iload           12
        //  3623: aaload         
        //  3624: astore          31
        //  3626: aload           31
        //  3628: astore          26
        //  3630: aload           31
        //  3632: aload           28
        //  3634: if_acmpne       3673
        //  3637: aload           23
        //  3639: iload           12
        //  3641: daload         
        //  3642: invokestatic    org/mozilla/javascript/ScriptRuntime.wrapNumber:(D)Ljava/lang/Number;
        //  3645: astore          26
        //  3647: goto            3673
        //  3650: astore_1       
        //  3651: aload           31
        //  3653: astore          26
        //  3655: aload           28
        //  3657: astore_2       
        //  3658: aload           30
        //  3660: astore          20
        //  3662: aload           29
        //  3664: astore          19
        //  3666: aload           27
        //  3668: astore          21
        //  3670: goto            6822
        //  3673: iload           12
        //  3675: iconst_1       
        //  3676: isub           
        //  3677: istore          12
        //  3679: aload           33
        //  3681: iload           12
        //  3683: aaload         
        //  3684: checkcast       Lorg/mozilla/javascript/Scriptable;
        //  3687: astore          36
        //  3689: iload           14
        //  3691: bipush          8
        //  3693: if_icmpne       3737
        //  3696: aload           29
        //  3698: getfield        org/mozilla/javascript/Interpreter$CallFrame.scope:Lorg/mozilla/javascript/Scriptable;
        //  3701: astore          31
        //  3703: aload           22
        //  3705: astore          29
        //  3707: aload           26
        //  3709: astore          28
        //  3711: aload           29
        //  3713: astore          27
        //  3715: aload           36
        //  3717: aload           26
        //  3719: aload           30
        //  3721: aload           31
        //  3723: aload           29
        //  3725: invokestatic    org/mozilla/javascript/ScriptRuntime.setName:(Lorg/mozilla/javascript/Scriptable;Ljava/lang/Object;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;Ljava/lang/String;)Ljava/lang/Object;
        //  3728: astore          29
        //  3730: aload           29
        //  3732: astore          27
        //  3734: goto            3771
        //  3737: aload           22
        //  3739: astore          31
        //  3741: aload           26
        //  3743: astore          28
        //  3745: aload           31
        //  3747: astore          27
        //  3749: aload           36
        //  3751: aload           26
        //  3753: aload           30
        //  3755: aload           29
        //  3757: getfield        org/mozilla/javascript/Interpreter$CallFrame.scope:Lorg/mozilla/javascript/Scriptable;
        //  3760: aload           31
        //  3762: invokestatic    org/mozilla/javascript/ScriptRuntime.strictSetName:(Lorg/mozilla/javascript/Scriptable;Ljava/lang/Object;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;Ljava/lang/String;)Ljava/lang/Object;
        //  3765: astore          29
        //  3767: aload           29
        //  3769: astore          27
        //  3771: aload           33
        //  3773: iload           12
        //  3775: aload           27
        //  3777: aastore        
        //  3778: goto            4049
        //  3781: astore_2       
        //  3782: aload           22
        //  3784: astore          23
        //  3786: aload           28
        //  3788: astore          20
        //  3790: aload           30
        //  3792: astore          22
        //  3794: aload           29
        //  3796: astore_1       
        //  3797: aload           27
        //  3799: astore          21
        //  3801: aload           25
        //  3803: astore          19
        //  3805: goto            3808
        //  3808: iload           17
        //  3810: istore          16
        //  3812: goto            10498
        //  3815: aload_1        
        //  3816: astore          27
        //  3818: iload           12
        //  3820: iconst_1       
        //  3821: isub           
        //  3822: istore          14
        //  3824: iload           14
        //  3826: istore          13
        //  3828: aload           27
        //  3830: iload           12
        //  3832: invokestatic    org/mozilla/javascript/Interpreter.stack_boolean:(Lorg/mozilla/javascript/Interpreter$CallFrame;I)Z
        //  3835: ifeq            11507
        //  3838: aload           27
        //  3840: aload           27
        //  3842: getfield        org/mozilla/javascript/Interpreter$CallFrame.pc:I
        //  3845: iconst_2       
        //  3846: iadd           
        //  3847: putfield        org/mozilla/javascript/Interpreter$CallFrame.pc:I
        //  3850: iload           14
        //  3852: istore          12
        //  3854: goto            11474
        //  3857: aload_1        
        //  3858: astore          27
        //  3860: iload           12
        //  3862: iconst_1       
        //  3863: isub           
        //  3864: istore          14
        //  3866: iload           14
        //  3868: istore          13
        //  3870: aload           27
        //  3872: iload           12
        //  3874: invokestatic    org/mozilla/javascript/Interpreter.stack_boolean:(Lorg/mozilla/javascript/Interpreter$CallFrame;I)Z
        //  3877: ifne            11507
        //  3880: aload           27
        //  3882: aload           27
        //  3884: getfield        org/mozilla/javascript/Interpreter$CallFrame.pc:I
        //  3887: iconst_2       
        //  3888: iadd           
        //  3889: putfield        org/mozilla/javascript/Interpreter$CallFrame.pc:I
        //  3892: iload           14
        //  3894: istore          12
        //  3896: goto            11474
        //  3899: aload_1        
        //  3900: astore          24
        //  3902: aload           24
        //  3904: aload           19
        //  3906: iload           12
        //  3908: aaload         
        //  3909: putfield        org/mozilla/javascript/Interpreter$CallFrame.result:Ljava/lang/Object;
        //  3912: aload           24
        //  3914: aload           23
        //  3916: iload           12
        //  3918: daload         
        //  3919: putfield        org/mozilla/javascript/Interpreter$CallFrame.resultDbl:D
        //  3922: aload           24
        //  3924: astore_1       
        //  3925: goto            9926
        //  3928: iload           16
        //  3930: istore          17
        //  3932: aload_1        
        //  3933: astore          29
        //  3935: aload           29
        //  3937: aload           29
        //  3939: getfield        org/mozilla/javascript/Interpreter$CallFrame.scope:Lorg/mozilla/javascript/Scriptable;
        //  3942: invokestatic    org/mozilla/javascript/ScriptRuntime.leaveWith:(Lorg/mozilla/javascript/Scriptable;)Lorg/mozilla/javascript/Scriptable;
        //  3945: putfield        org/mozilla/javascript/Interpreter$CallFrame.scope:Lorg/mozilla/javascript/Scriptable;
        //  3948: aload           23
        //  3950: astore          30
        //  3952: aload           19
        //  3954: astore_1       
        //  3955: aload           25
        //  3957: astore          23
        //  3959: aload_2        
        //  3960: astore          19
        //  3962: aload           22
        //  3964: astore          25
        //  3966: iload           12
        //  3968: istore          14
        //  3970: aload           21
        //  3972: astore          28
        //  3974: aload           20
        //  3976: astore          31
        //  3978: goto            12415
        //  3981: aload_1        
        //  3982: astore          29
        //  3984: aload           19
        //  3986: iload           12
        //  3988: aaload         
        //  3989: astore          28
        //  3991: aload           28
        //  3993: astore          26
        //  3995: aload           28
        //  3997: aload_2        
        //  3998: if_acmpne       4018
        //  4001: aload           22
        //  4003: astore          27
        //  4005: aload           23
        //  4007: iload           12
        //  4009: daload         
        //  4010: invokestatic    org/mozilla/javascript/ScriptRuntime.wrapNumber:(D)Ljava/lang/Number;
        //  4013: astore          26
        //  4015: goto            4018
        //  4018: iload           12
        //  4020: iconst_1       
        //  4021: isub           
        //  4022: istore          12
        //  4024: aload           26
        //  4026: astore          28
        //  4028: aload           22
        //  4030: astore          27
        //  4032: aload           29
        //  4034: aload           26
        //  4036: aload           21
        //  4038: aload           29
        //  4040: getfield        org/mozilla/javascript/Interpreter$CallFrame.scope:Lorg/mozilla/javascript/Scriptable;
        //  4043: invokestatic    org/mozilla/javascript/ScriptRuntime.enterWith:(Ljava/lang/Object;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;)Lorg/mozilla/javascript/Scriptable;
        //  4046: putfield        org/mozilla/javascript/Interpreter$CallFrame.scope:Lorg/mozilla/javascript/Scriptable;
        //  4049: goto            11474
        //  4052: astore          19
        //  4054: aload           22
        //  4056: astore          23
        //  4058: aload_2        
        //  4059: astore          22
        //  4061: aload           20
        //  4063: astore          24
        //  4065: aload           19
        //  4067: astore_2       
        //  4068: aload           22
        //  4070: astore          20
        //  4072: aload           21
        //  4074: astore          22
        //  4076: aload           24
        //  4078: astore          21
        //  4080: goto            12376
        //  4083: aload           23
        //  4085: astore          28
        //  4087: aload_2        
        //  4088: astore          23
        //  4090: aload           28
        //  4092: astore_2       
        //  4093: aload           23
        //  4095: astore          27
        //  4097: aload           21
        //  4099: aload_1        
        //  4100: iload           14
        //  4102: aload           19
        //  4104: aload           28
        //  4106: iload           12
        //  4108: invokestatic    org/mozilla/javascript/Interpreter.doDelName:(Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Interpreter$CallFrame;I[Ljava/lang/Object;[DI)I
        //  4111: istore          12
        //  4113: goto            11575
        //  4116: aload           19
        //  4118: astore          29
        //  4120: aload_2        
        //  4121: astore          28
        //  4123: aload_1        
        //  4124: astore          30
        //  4126: aload           28
        //  4128: astore          27
        //  4130: aload           30
        //  4132: aload           29
        //  4134: iload           12
        //  4136: aaload         
        //  4137: putfield        org/mozilla/javascript/Interpreter$CallFrame.result:Ljava/lang/Object;
        //  4140: aload           28
        //  4142: astore          27
        //  4144: aload           30
        //  4146: aload           23
        //  4148: iload           12
        //  4150: daload         
        //  4151: putfield        org/mozilla/javascript/Interpreter$CallFrame.resultDbl:D
        //  4154: aload           29
        //  4156: iload           12
        //  4158: aconst_null    
        //  4159: aastore        
        //  4160: iload           12
        //  4162: iconst_1       
        //  4163: isub           
        //  4164: istore          12
        //  4166: goto            11827
        //  4169: aload           19
        //  4171: astore          29
        //  4173: aload_2        
        //  4174: astore          28
        //  4176: aload_1        
        //  4177: astore          30
        //  4179: iload           12
        //  4181: iconst_1       
        //  4182: isub           
        //  4183: istore          13
        //  4185: aload           28
        //  4187: astore          27
        //  4189: aload           30
        //  4191: iload           12
        //  4193: invokestatic    org/mozilla/javascript/Interpreter.stack_boolean:(Lorg/mozilla/javascript/Interpreter$CallFrame;I)Z
        //  4196: ifne            4222
        //  4199: aload           28
        //  4201: astore          27
        //  4203: aload           30
        //  4205: aload           30
        //  4207: getfield        org/mozilla/javascript/Interpreter$CallFrame.pc:I
        //  4210: iconst_2       
        //  4211: iadd           
        //  4212: putfield        org/mozilla/javascript/Interpreter$CallFrame.pc:I
        //  4215: iload           13
        //  4217: istore          12
        //  4219: goto            11827
        //  4222: iload           13
        //  4224: iconst_1       
        //  4225: isub           
        //  4226: istore          12
        //  4228: aload           29
        //  4230: iload           13
        //  4232: aconst_null    
        //  4233: aastore        
        //  4234: aload           20
        //  4236: astore          30
        //  4238: aload           28
        //  4240: astore          31
        //  4242: iload           12
        //  4244: istore          13
        //  4246: aload           23
        //  4248: astore          36
        //  4250: aload           29
        //  4252: astore          37
        //  4254: aload           24
        //  4256: astore          38
        //  4258: aload           21
        //  4260: astore          33
        //  4262: goto            7220
        //  4265: astore_2       
        //  4266: aload           22
        //  4268: astore          23
        //  4270: aload           20
        //  4272: astore          24
        //  4274: aload           25
        //  4276: astore          19
        //  4278: aload           27
        //  4280: astore          20
        //  4282: aload           21
        //  4284: astore          22
        //  4286: aload           24
        //  4288: astore          21
        //  4290: goto            11209
        //  4293: aload_0        
        //  4294: astore          28
        //  4296: aload           28
        //  4298: astore          27
        //  4300: aload           21
        //  4302: aload_1        
        //  4303: aload           19
        //  4305: aload           23
        //  4307: iload           12
        //  4309: aload           46
        //  4311: aload           47
        //  4313: aload           48
        //  4315: iload           11
        //  4317: invokestatic    org/mozilla/javascript/Interpreter.doVarIncDec:(Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Interpreter$CallFrame;[Ljava/lang/Object;[DI[Ljava/lang/Object;[D[II)I
        //  4320: istore          12
        //  4322: aload           28
        //  4324: astore          21
        //  4326: goto            11830
        //  4329: aload_1        
        //  4330: astore          28
        //  4332: iload           12
        //  4334: iconst_1       
        //  4335: iadd           
        //  4336: istore          12
        //  4338: aload           21
        //  4340: astore          27
        //  4342: aload           19
        //  4344: iload           12
        //  4346: aload           28
        //  4348: getfield        org/mozilla/javascript/Interpreter$CallFrame.scope:Lorg/mozilla/javascript/Scriptable;
        //  4351: aload           22
        //  4353: aload           21
        //  4355: aload           24
        //  4357: aload           28
        //  4359: getfield        org/mozilla/javascript/Interpreter$CallFrame.pc:I
        //  4362: baload         
        //  4363: invokestatic    org/mozilla/javascript/ScriptRuntime.nameIncrDecr:(Lorg/mozilla/javascript/Scriptable;Ljava/lang/String;Lorg/mozilla/javascript/Context;I)Ljava/lang/Object;
        //  4366: aastore        
        //  4367: aload           21
        //  4369: astore          27
        //  4371: aload           28
        //  4373: aload           28
        //  4375: getfield        org/mozilla/javascript/Interpreter$CallFrame.pc:I
        //  4378: iconst_1       
        //  4379: iadd           
        //  4380: putfield        org/mozilla/javascript/Interpreter$CallFrame.pc:I
        //  4383: goto            11830
        //  4386: aload           19
        //  4388: iload           12
        //  4390: aaload         
        //  4391: astore          27
        //  4393: aload           27
        //  4395: astore          26
        //  4397: aload           27
        //  4399: aload_2        
        //  4400: if_acmpne       4416
        //  4403: aload           23
        //  4405: iload           12
        //  4407: daload         
        //  4408: invokestatic    org/mozilla/javascript/ScriptRuntime.wrapNumber:(D)Ljava/lang/Number;
        //  4411: astore          26
        //  4413: goto            4416
        //  4416: aload           26
        //  4418: astore          27
        //  4420: aload           19
        //  4422: iload           12
        //  4424: aload           26
        //  4426: aload           22
        //  4428: aload           21
        //  4430: aload_1        
        //  4431: getfield        org/mozilla/javascript/Interpreter$CallFrame.scope:Lorg/mozilla/javascript/Scriptable;
        //  4434: aload           24
        //  4436: aload_1        
        //  4437: getfield        org/mozilla/javascript/Interpreter$CallFrame.pc:I
        //  4440: baload         
        //  4441: invokestatic    org/mozilla/javascript/ScriptRuntime.propIncrDecr:(Ljava/lang/Object;Ljava/lang/String;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;I)Ljava/lang/Object;
        //  4444: aastore        
        //  4445: aload           26
        //  4447: astore          27
        //  4449: aload_1        
        //  4450: aload_1        
        //  4451: getfield        org/mozilla/javascript/Interpreter$CallFrame.pc:I
        //  4454: iconst_1       
        //  4455: iadd           
        //  4456: putfield        org/mozilla/javascript/Interpreter$CallFrame.pc:I
        //  4459: aload           19
        //  4461: astore          27
        //  4463: aload_1        
        //  4464: astore          28
        //  4466: aload           23
        //  4468: astore_1       
        //  4469: aload           24
        //  4471: astore          19
        //  4473: aload           28
        //  4475: astore          23
        //  4477: goto            11848
        //  4480: astore          19
        //  4482: aload           22
        //  4484: astore          23
        //  4486: aload_2        
        //  4487: astore          21
        //  4489: aload_1        
        //  4490: astore          22
        //  4492: aload           19
        //  4494: astore_2       
        //  4495: aload           21
        //  4497: astore_1       
        //  4498: aload           22
        //  4500: astore          21
        //  4502: aload           27
        //  4504: astore          22
        //  4506: goto            11956
        //  4509: aload           21
        //  4511: aload_1        
        //  4512: aload           24
        //  4514: aload           19
        //  4516: aload           23
        //  4518: iload           12
        //  4520: invokestatic    org/mozilla/javascript/Interpreter.doElemIncDec:(Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Interpreter$CallFrame;[B[Ljava/lang/Object;[DI)I
        //  4523: istore          12
        //  4525: goto            11900
        //  4528: aload_1        
        //  4529: astore          28
        //  4531: aload           19
        //  4533: astore          29
        //  4535: aload           29
        //  4537: iload           12
        //  4539: aaload         
        //  4540: checkcast       Lorg/mozilla/javascript/Ref;
        //  4543: astore          27
        //  4545: aload           27
        //  4547: astore          26
        //  4549: aload           29
        //  4551: iload           12
        //  4553: aload           27
        //  4555: aload           21
        //  4557: aload           28
        //  4559: getfield        org/mozilla/javascript/Interpreter$CallFrame.scope:Lorg/mozilla/javascript/Scriptable;
        //  4562: aload           24
        //  4564: aload           28
        //  4566: getfield        org/mozilla/javascript/Interpreter$CallFrame.pc:I
        //  4569: baload         
        //  4570: invokestatic    org/mozilla/javascript/ScriptRuntime.refIncrDecr:(Lorg/mozilla/javascript/Ref;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;I)Ljava/lang/Object;
        //  4573: aastore        
        //  4574: aload           27
        //  4576: astore          26
        //  4578: aload           28
        //  4580: aload           28
        //  4582: getfield        org/mozilla/javascript/Interpreter$CallFrame.pc:I
        //  4585: iconst_1       
        //  4586: iadd           
        //  4587: putfield        org/mozilla/javascript/Interpreter$CallFrame.pc:I
        //  4590: aload           27
        //  4592: astore          26
        //  4594: goto            5070
        //  4597: aload_1        
        //  4598: astore          27
        //  4600: aload           27
        //  4602: getfield        org/mozilla/javascript/Interpreter$CallFrame.localShift:I
        //  4605: istore          13
        //  4607: iload           13
        //  4609: iload           11
        //  4611: iadd           
        //  4612: istore          13
        //  4614: iload           13
        //  4616: istore          11
        //  4618: aload           27
        //  4620: aload           19
        //  4622: iload           13
        //  4624: aaload         
        //  4625: checkcast       Lorg/mozilla/javascript/Scriptable;
        //  4628: putfield        org/mozilla/javascript/Interpreter$CallFrame.scope:Lorg/mozilla/javascript/Scriptable;
        //  4631: iload           13
        //  4633: istore          11
        //  4635: goto            4673
        //  4638: aload_1        
        //  4639: astore          27
        //  4641: aload           27
        //  4643: getfield        org/mozilla/javascript/Interpreter$CallFrame.localShift:I
        //  4646: istore          13
        //  4648: iload           13
        //  4650: iload           11
        //  4652: iadd           
        //  4653: istore          13
        //  4655: iload           13
        //  4657: istore          11
        //  4659: aload           19
        //  4661: iload           13
        //  4663: aload           27
        //  4665: getfield        org/mozilla/javascript/Interpreter$CallFrame.scope:Lorg/mozilla/javascript/Scriptable;
        //  4668: aastore        
        //  4669: iload           13
        //  4671: istore          11
        //  4673: aload_1        
        //  4674: astore          28
        //  4676: aload           19
        //  4678: astore          29
        //  4680: aload           24
        //  4682: astore          19
        //  4684: aload           20
        //  4686: astore          27
        //  4688: aload           23
        //  4690: astore_1       
        //  4691: aload           29
        //  4693: astore          20
        //  4695: goto            12119
        //  4698: astore          19
        //  4700: aload           22
        //  4702: astore          23
        //  4704: aload_1        
        //  4705: astore          22
        //  4707: aload           19
        //  4709: astore_1       
        //  4710: aload           25
        //  4712: astore          19
        //  4714: aload           20
        //  4716: astore          25
        //  4718: aload_2        
        //  4719: astore          20
        //  4721: aload           21
        //  4723: astore          24
        //  4725: aload           25
        //  4727: astore          21
        //  4729: goto            6841
        //  4732: iload           12
        //  4734: iconst_1       
        //  4735: iadd           
        //  4736: istore          12
        //  4738: aload           19
        //  4740: iload           12
        //  4742: aload_1        
        //  4743: getfield        org/mozilla/javascript/Interpreter$CallFrame.scope:Lorg/mozilla/javascript/Scriptable;
        //  4746: aload           22
        //  4748: invokestatic    org/mozilla/javascript/ScriptRuntime.typeofName:(Lorg/mozilla/javascript/Scriptable;Ljava/lang/String;)Ljava/lang/String;
        //  4751: aastore        
        //  4752: goto            11900
        //  4755: aload           19
        //  4757: astore          27
        //  4759: iload           12
        //  4761: iconst_1       
        //  4762: iadd           
        //  4763: istore          12
        //  4765: aload           27
        //  4767: iload           12
        //  4769: aload           22
        //  4771: aload           21
        //  4773: aload_1        
        //  4774: getfield        org/mozilla/javascript/Interpreter$CallFrame.scope:Lorg/mozilla/javascript/Scriptable;
        //  4777: invokestatic    org/mozilla/javascript/ScriptRuntime.getNameFunctionAndThis:(Ljava/lang/String;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;)Lorg/mozilla/javascript/Callable;
        //  4780: aastore        
        //  4781: iload           12
        //  4783: iconst_1       
        //  4784: iadd           
        //  4785: istore          12
        //  4787: aload           27
        //  4789: iload           12
        //  4791: aload_0        
        //  4792: invokestatic    org/mozilla/javascript/ScriptRuntime.lastStoredScriptable:(Lorg/mozilla/javascript/Context;)Lorg/mozilla/javascript/Scriptable;
        //  4795: aastore        
        //  4796: goto            11900
        //  4799: aload           19
        //  4801: astore          28
        //  4803: aload           28
        //  4805: iload           12
        //  4807: aaload         
        //  4808: astore          26
        //  4810: aload           26
        //  4812: astore          27
        //  4814: aload           26
        //  4816: aload_2        
        //  4817: if_acmpne       4830
        //  4820: aload           23
        //  4822: iload           12
        //  4824: daload         
        //  4825: invokestatic    org/mozilla/javascript/ScriptRuntime.wrapNumber:(D)Ljava/lang/Number;
        //  4828: astore          27
        //  4830: aload           27
        //  4832: astore          26
        //  4834: aload           28
        //  4836: iload           12
        //  4838: aload           27
        //  4840: aload           22
        //  4842: aload           21
        //  4844: aload_1        
        //  4845: getfield        org/mozilla/javascript/Interpreter$CallFrame.scope:Lorg/mozilla/javascript/Scriptable;
        //  4848: invokestatic    org/mozilla/javascript/ScriptRuntime.getPropFunctionAndThis:(Ljava/lang/Object;Ljava/lang/String;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;)Lorg/mozilla/javascript/Callable;
        //  4851: aastore        
        //  4852: iload           12
        //  4854: iconst_1       
        //  4855: iadd           
        //  4856: istore          12
        //  4858: aload           27
        //  4860: astore          26
        //  4862: aload           28
        //  4864: iload           12
        //  4866: aload_0        
        //  4867: invokestatic    org/mozilla/javascript/ScriptRuntime.lastStoredScriptable:(Lorg/mozilla/javascript/Context;)Lorg/mozilla/javascript/Scriptable;
        //  4870: aastore        
        //  4871: aload           27
        //  4873: astore          26
        //  4875: goto            5070
        //  4878: aload           23
        //  4880: astore          30
        //  4882: aload_2        
        //  4883: astore          31
        //  4885: aload           19
        //  4887: astore          29
        //  4889: aload           29
        //  4891: iload           12
        //  4893: iconst_1       
        //  4894: isub           
        //  4895: aaload         
        //  4896: astore          26
        //  4898: aload           26
        //  4900: astore          27
        //  4902: aload           26
        //  4904: aload           31
        //  4906: if_acmpne       4921
        //  4909: aload           30
        //  4911: iload           12
        //  4913: iconst_1       
        //  4914: isub           
        //  4915: daload         
        //  4916: invokestatic    org/mozilla/javascript/ScriptRuntime.wrapNumber:(D)Ljava/lang/Number;
        //  4919: astore          27
        //  4921: aload           29
        //  4923: iload           12
        //  4925: aaload         
        //  4926: astore          26
        //  4928: aload           26
        //  4930: astore          28
        //  4932: aload           26
        //  4934: aload           31
        //  4936: if_acmpne       4953
        //  4939: aload           27
        //  4941: astore          26
        //  4943: aload           30
        //  4945: iload           12
        //  4947: daload         
        //  4948: invokestatic    org/mozilla/javascript/ScriptRuntime.wrapNumber:(D)Ljava/lang/Number;
        //  4951: astore          28
        //  4953: aload           27
        //  4955: astore          26
        //  4957: aload           29
        //  4959: iload           12
        //  4961: iconst_1       
        //  4962: isub           
        //  4963: aload           27
        //  4965: aload           28
        //  4967: aload           21
        //  4969: aload_1        
        //  4970: getfield        org/mozilla/javascript/Interpreter$CallFrame.scope:Lorg/mozilla/javascript/Scriptable;
        //  4973: invokestatic    org/mozilla/javascript/ScriptRuntime.getElemFunctionAndThis:(Ljava/lang/Object;Ljava/lang/Object;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;)Lorg/mozilla/javascript/Callable;
        //  4976: aastore        
        //  4977: aload           27
        //  4979: astore          26
        //  4981: aload           29
        //  4983: iload           12
        //  4985: aload_0        
        //  4986: invokestatic    org/mozilla/javascript/ScriptRuntime.lastStoredScriptable:(Lorg/mozilla/javascript/Context;)Lorg/mozilla/javascript/Scriptable;
        //  4989: aastore        
        //  4990: aload           27
        //  4992: astore          26
        //  4994: goto            5070
        //  4997: aload           19
        //  4999: astore          28
        //  5001: aload           28
        //  5003: iload           12
        //  5005: aaload         
        //  5006: astore          26
        //  5008: aload           26
        //  5010: astore          27
        //  5012: aload           26
        //  5014: aload_2        
        //  5015: if_acmpne       5031
        //  5018: aload           23
        //  5020: iload           12
        //  5022: daload         
        //  5023: invokestatic    org/mozilla/javascript/ScriptRuntime.wrapNumber:(D)Ljava/lang/Number;
        //  5026: astore          27
        //  5028: goto            5031
        //  5031: aload           27
        //  5033: astore          26
        //  5035: aload           28
        //  5037: iload           12
        //  5039: aload           27
        //  5041: aload           21
        //  5043: invokestatic    org/mozilla/javascript/ScriptRuntime.getValueFunctionAndThis:(Ljava/lang/Object;Lorg/mozilla/javascript/Context;)Lorg/mozilla/javascript/Callable;
        //  5046: aastore        
        //  5047: iload           12
        //  5049: iconst_1       
        //  5050: iadd           
        //  5051: istore          12
        //  5053: aload           27
        //  5055: astore          26
        //  5057: aload           28
        //  5059: iload           12
        //  5061: aload_0        
        //  5062: invokestatic    org/mozilla/javascript/ScriptRuntime.lastStoredScriptable:(Lorg/mozilla/javascript/Context;)Lorg/mozilla/javascript/Scriptable;
        //  5065: aastore        
        //  5066: aload           27
        //  5068: astore          26
        //  5070: goto            11900
        //  5073: aload_1        
        //  5074: astore          27
        //  5076: iload           12
        //  5078: iconst_1       
        //  5079: iadd           
        //  5080: istore          12
        //  5082: aload           19
        //  5084: iload           12
        //  5086: aload           21
        //  5088: aload           27
        //  5090: getfield        org/mozilla/javascript/Interpreter$CallFrame.scope:Lorg/mozilla/javascript/Scriptable;
        //  5093: aload           27
        //  5095: getfield        org/mozilla/javascript/Interpreter$CallFrame.fnOrScript:Lorg/mozilla/javascript/InterpretedFunction;
        //  5098: iload           11
        //  5100: invokestatic    org/mozilla/javascript/InterpretedFunction.createFunction:(Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;Lorg/mozilla/javascript/InterpretedFunction;I)Lorg/mozilla/javascript/InterpretedFunction;
        //  5103: aastore        
        //  5104: goto            11900
        //  5107: iload           16
        //  5109: istore          17
        //  5111: aload           21
        //  5113: astore          28
        //  5115: aload_1        
        //  5116: astore          27
        //  5118: aload           28
        //  5120: aload           27
        //  5122: getfield        org/mozilla/javascript/Interpreter$CallFrame.scope:Lorg/mozilla/javascript/Scriptable;
        //  5125: aload           27
        //  5127: getfield        org/mozilla/javascript/Interpreter$CallFrame.fnOrScript:Lorg/mozilla/javascript/InterpretedFunction;
        //  5130: iload           11
        //  5132: invokestatic    org/mozilla/javascript/Interpreter.initFunction:(Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;Lorg/mozilla/javascript/InterpretedFunction;I)V
        //  5135: aload           19
        //  5137: astore_1       
        //  5138: aload           23
        //  5140: astore          30
        //  5142: aload           25
        //  5144: astore          23
        //  5146: aload_2        
        //  5147: astore          19
        //  5149: aload           22
        //  5151: astore          25
        //  5153: iload           12
        //  5155: istore          14
        //  5157: aload           27
        //  5159: astore          29
        //  5161: aload           20
        //  5163: astore          31
        //  5165: goto            12415
        //  5168: iload           16
        //  5170: istore          17
        //  5172: aload           21
        //  5174: astore          28
        //  5176: aload_1        
        //  5177: astore          27
        //  5179: iload           17
        //  5181: ifeq            5200
        //  5184: aload           28
        //  5186: aload           28
        //  5188: getfield        org/mozilla/javascript/Context.instructionCount:I
        //  5191: bipush          100
        //  5193: iadd           
        //  5194: putfield        org/mozilla/javascript/Context.instructionCount:I
        //  5197: goto            5200
        //  5200: aload           28
        //  5202: aload           27
        //  5204: aload           19
        //  5206: aload           23
        //  5208: iload           12
        //  5210: aload           24
        //  5212: iload           11
        //  5214: invokestatic    org/mozilla/javascript/Interpreter.doCallSpecial:(Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Interpreter$CallFrame;[Ljava/lang/Object;[DI[BI)I
        //  5217: istore          12
        //  5219: aload           23
        //  5221: astore_1       
        //  5222: aload           24
        //  5224: astore          23
        //  5226: iload           17
        //  5228: istore          16
        //  5230: goto            11910
        //  5233: astore          19
        //  5235: goto            11940
        //  5238: aload_2        
        //  5239: astore          28
        //  5241: aload           21
        //  5243: astore          29
        //  5245: aload           20
        //  5247: astore          19
        //  5249: aload           19
        //  5251: astore          27
        //  5253: aload_1        
        //  5254: aload           19
        //  5256: putfield        org/mozilla/javascript/Interpreter$CallFrame.result:Ljava/lang/Object;
        //  5259: goto            1592
        //  5262: iload           12
        //  5264: iconst_1       
        //  5265: iadd           
        //  5266: istore          12
        //  5268: aload           19
        //  5270: iload           12
        //  5272: aload_2        
        //  5273: aastore        
        //  5274: aload           20
        //  5276: astore          27
        //  5278: aload_2        
        //  5279: astore          28
        //  5281: aload           21
        //  5283: astore          29
        //  5285: aload           23
        //  5287: iload           12
        //  5289: aload_1        
        //  5290: getfield        org/mozilla/javascript/Interpreter$CallFrame.pc:I
        //  5293: iconst_2       
        //  5294: iadd           
        //  5295: i2d            
        //  5296: dastore        
        //  5297: aload           23
        //  5299: astore          36
        //  5301: aload           19
        //  5303: astore          37
        //  5305: aload           24
        //  5307: astore          38
        //  5309: goto            11526
        //  5312: aload           23
        //  5314: astore          36
        //  5316: aload           20
        //  5318: astore          30
        //  5320: aload_2        
        //  5321: astore          31
        //  5323: aload           21
        //  5325: astore          33
        //  5327: aload_1        
        //  5328: astore          37
        //  5330: aload           19
        //  5332: astore          38
        //  5334: aload           30
        //  5336: astore          27
        //  5338: aload           31
        //  5340: astore          28
        //  5342: aload           33
        //  5344: astore          29
        //  5346: iload           12
        //  5348: aload           37
        //  5350: getfield        org/mozilla/javascript/Interpreter$CallFrame.emptyStackTop:I
        //  5353: iconst_1       
        //  5354: iadd           
        //  5355: if_icmpne       5413
        //  5358: aload           30
        //  5360: astore          27
        //  5362: aload           31
        //  5364: astore          28
        //  5366: aload           33
        //  5368: astore          29
        //  5370: aload           37
        //  5372: getfield        org/mozilla/javascript/Interpreter$CallFrame.localShift:I
        //  5375: istore          13
        //  5377: iload           13
        //  5379: iload           11
        //  5381: iadd           
        //  5382: istore          11
        //  5384: aload           38
        //  5386: iload           11
        //  5388: aload           38
        //  5390: iload           12
        //  5392: aaload         
        //  5393: aastore        
        //  5394: aload           36
        //  5396: iload           11
        //  5398: aload           36
        //  5400: iload           12
        //  5402: daload         
        //  5403: dastore        
        //  5404: iload           12
        //  5406: iconst_1       
        //  5407: isub           
        //  5408: istore          12
        //  5410: goto            6789
        //  5413: aload           30
        //  5415: astore          27
        //  5417: aload           31
        //  5419: astore          28
        //  5421: aload           33
        //  5423: astore          29
        //  5425: iload           12
        //  5427: aload           37
        //  5429: getfield        org/mozilla/javascript/Interpreter$CallFrame.emptyStackTop:I
        //  5432: if_icmpeq       11977
        //  5435: aload           30
        //  5437: astore          27
        //  5439: aload           31
        //  5441: astore          28
        //  5443: aload           33
        //  5445: astore          29
        //  5447: invokestatic    org/mozilla/javascript/Kit.codeBug:()Ljava/lang/RuntimeException;
        //  5450: pop            
        //  5451: goto            11977
        //  5454: iload           16
        //  5456: istore          17
        //  5458: aload           20
        //  5460: astore          33
        //  5462: aload_2        
        //  5463: astore          31
        //  5465: aload           21
        //  5467: astore          30
        //  5469: aload_1        
        //  5470: astore          36
        //  5472: iload           17
        //  5474: ifeq            12006
        //  5477: aload           33
        //  5479: astore          27
        //  5481: aload           31
        //  5483: astore          28
        //  5485: aload           30
        //  5487: astore          29
        //  5489: aload           30
        //  5491: aload           36
        //  5493: iconst_0       
        //  5494: invokestatic    org/mozilla/javascript/Interpreter.addInstructionCount:(Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Interpreter$CallFrame;I)V
        //  5497: goto            5500
        //  5500: aload           33
        //  5502: astore          27
        //  5504: aload           31
        //  5506: astore          28
        //  5508: aload           30
        //  5510: astore          29
        //  5512: aload           36
        //  5514: getfield        org/mozilla/javascript/Interpreter$CallFrame.localShift:I
        //  5517: istore          13
        //  5519: iload           13
        //  5521: iload           11
        //  5523: iadd           
        //  5524: istore          11
        //  5526: aload           19
        //  5528: iload           11
        //  5530: aaload         
        //  5531: astore          26
        //  5533: aload           26
        //  5535: aload           31
        //  5537: if_acmpeq       5578
        //  5540: aload           26
        //  5542: astore          24
        //  5544: aload           26
        //  5546: astore_2       
        //  5547: aload           33
        //  5549: astore          21
        //  5551: aload           31
        //  5553: astore          20
        //  5555: aload           25
        //  5557: astore          19
        //  5559: aload           22
        //  5561: astore          23
        //  5563: aload           36
        //  5565: astore          25
        //  5567: aload           30
        //  5569: astore          22
        //  5571: iload           17
        //  5573: istore          16
        //  5575: goto            2036
        //  5578: aload           36
        //  5580: aload           23
        //  5582: iload           11
        //  5584: daload         
        //  5585: d2i            
        //  5586: putfield        org/mozilla/javascript/Interpreter$CallFrame.pc:I
        //  5589: iload           17
        //  5591: ifeq            5604
        //  5594: aload           36
        //  5596: aload           36
        //  5598: getfield        org/mozilla/javascript/Interpreter$CallFrame.pc:I
        //  5601: putfield        org/mozilla/javascript/Interpreter$CallFrame.pcPrevBranch:I
        //  5604: goto            6789
        //  5607: astore          23
        //  5609: goto            6804
        //  5612: aload           20
        //  5614: astore          30
        //  5616: aload_2        
        //  5617: astore          31
        //  5619: aload           21
        //  5621: astore          33
        //  5623: aload_1        
        //  5624: astore          36
        //  5626: aload           30
        //  5628: astore          27
        //  5630: aload           31
        //  5632: astore          28
        //  5634: aload           33
        //  5636: astore          29
        //  5638: aload           36
        //  5640: aload           36
        //  5642: getfield        org/mozilla/javascript/Interpreter$CallFrame.pc:I
        //  5645: putfield        org/mozilla/javascript/Interpreter$CallFrame.pcSourceLineStart:I
        //  5648: aload           30
        //  5650: astore          27
        //  5652: aload           31
        //  5654: astore          28
        //  5656: aload           33
        //  5658: astore          29
        //  5660: aload           36
        //  5662: getfield        org/mozilla/javascript/Interpreter$CallFrame.debuggerFrame:Lorg/mozilla/javascript/debug/DebugFrame;
        //  5665: ifnull          5718
        //  5668: aload           30
        //  5670: astore          27
        //  5672: aload           31
        //  5674: astore          28
        //  5676: aload           33
        //  5678: astore          29
        //  5680: aload           24
        //  5682: aload           36
        //  5684: getfield        org/mozilla/javascript/Interpreter$CallFrame.pc:I
        //  5687: invokestatic    org/mozilla/javascript/Interpreter.getIndex:([BI)I
        //  5690: istore          13
        //  5692: aload           30
        //  5694: astore          27
        //  5696: aload           31
        //  5698: astore          28
        //  5700: aload           33
        //  5702: astore          29
        //  5704: aload           36
        //  5706: getfield        org/mozilla/javascript/Interpreter$CallFrame.debuggerFrame:Lorg/mozilla/javascript/debug/DebugFrame;
        //  5709: aload           33
        //  5711: iload           13
        //  5713: invokeinterface org/mozilla/javascript/debug/DebugFrame.onLineChange:(Lorg/mozilla/javascript/Context;I)V
        //  5718: aload           30
        //  5720: astore          27
        //  5722: aload           31
        //  5724: astore          28
        //  5726: aload           33
        //  5728: astore          29
        //  5730: aload           36
        //  5732: aload           36
        //  5734: getfield        org/mozilla/javascript/Interpreter$CallFrame.pc:I
        //  5737: iconst_2       
        //  5738: iadd           
        //  5739: putfield        org/mozilla/javascript/Interpreter$CallFrame.pc:I
        //  5742: goto            11977
        //  5745: aload           23
        //  5747: astore          31
        //  5749: aload_2        
        //  5750: astore          23
        //  5752: aload           21
        //  5754: astore          30
        //  5756: aload_1        
        //  5757: astore_2       
        //  5758: aload           19
        //  5760: astore          21
        //  5762: iload           12
        //  5764: iconst_1       
        //  5765: iadd           
        //  5766: istore          12
        //  5768: aload           21
        //  5770: iload           12
        //  5772: aload           23
        //  5774: aastore        
        //  5775: aload           20
        //  5777: astore          27
        //  5779: aload           23
        //  5781: astore          28
        //  5783: aload           30
        //  5785: astore          29
        //  5787: aload           31
        //  5789: iload           12
        //  5791: aload           24
        //  5793: aload_2        
        //  5794: getfield        org/mozilla/javascript/Interpreter$CallFrame.pc:I
        //  5797: invokestatic    org/mozilla/javascript/Interpreter.getShort:([BI)I
        //  5800: i2d            
        //  5801: dastore        
        //  5802: aload           20
        //  5804: astore          27
        //  5806: aload           23
        //  5808: astore          28
        //  5810: aload           30
        //  5812: astore          29
        //  5814: aload_2        
        //  5815: aload_2        
        //  5816: getfield        org/mozilla/javascript/Interpreter$CallFrame.pc:I
        //  5819: iconst_2       
        //  5820: iadd           
        //  5821: putfield        org/mozilla/javascript/Interpreter$CallFrame.pc:I
        //  5824: aload           20
        //  5826: astore_2       
        //  5827: aload           23
        //  5829: astore          19
        //  5831: aload           31
        //  5833: astore          20
        //  5835: aload           24
        //  5837: astore          23
        //  5839: aload           30
        //  5841: astore          24
        //  5843: goto            12187
        //  5846: aload           23
        //  5848: astore          31
        //  5850: aload_2        
        //  5851: astore          23
        //  5853: aload           21
        //  5855: astore          30
        //  5857: aload_1        
        //  5858: astore_2       
        //  5859: aload           19
        //  5861: astore          21
        //  5863: iload           12
        //  5865: iconst_1       
        //  5866: iadd           
        //  5867: istore          12
        //  5869: aload           21
        //  5871: iload           12
        //  5873: aload           23
        //  5875: aastore        
        //  5876: aload           20
        //  5878: astore          27
        //  5880: aload           23
        //  5882: astore          28
        //  5884: aload           30
        //  5886: astore          29
        //  5888: aload           31
        //  5890: iload           12
        //  5892: aload           24
        //  5894: aload_2        
        //  5895: getfield        org/mozilla/javascript/Interpreter$CallFrame.pc:I
        //  5898: invokestatic    org/mozilla/javascript/Interpreter.getInt:([BI)I
        //  5901: i2d            
        //  5902: dastore        
        //  5903: aload           20
        //  5905: astore          27
        //  5907: aload           23
        //  5909: astore          28
        //  5911: aload           30
        //  5913: astore          29
        //  5915: aload_2        
        //  5916: aload_2        
        //  5917: getfield        org/mozilla/javascript/Interpreter$CallFrame.pc:I
        //  5920: iconst_4       
        //  5921: iadd           
        //  5922: putfield        org/mozilla/javascript/Interpreter$CallFrame.pc:I
        //  5925: aload           20
        //  5927: astore_2       
        //  5928: aload           23
        //  5930: astore          19
        //  5932: aload           31
        //  5934: astore          20
        //  5936: aload           24
        //  5938: astore          23
        //  5940: aload           30
        //  5942: astore          24
        //  5944: goto            12187
        //  5947: aload           23
        //  5949: astore          31
        //  5951: aload_2        
        //  5952: astore          23
        //  5954: aload           21
        //  5956: astore          30
        //  5958: iload           11
        //  5960: istore          13
        //  5962: aload           19
        //  5964: astore          21
        //  5966: iload           12
        //  5968: iconst_1       
        //  5969: iadd           
        //  5970: istore          12
        //  5972: aload           20
        //  5974: astore          27
        //  5976: aload           23
        //  5978: astore          28
        //  5980: aload           30
        //  5982: astore          29
        //  5984: aload           21
        //  5986: iload           12
        //  5988: iload           13
        //  5990: newarray        I
        //  5992: aastore        
        //  5993: iload           12
        //  5995: iconst_1       
        //  5996: iadd           
        //  5997: istore          12
        //  5999: aload           20
        //  6001: astore          27
        //  6003: aload           23
        //  6005: astore          28
        //  6007: aload           30
        //  6009: astore          29
        //  6011: aload           21
        //  6013: iload           12
        //  6015: iload           13
        //  6017: anewarray       Ljava/lang/Object;
        //  6020: aastore        
        //  6021: aload           31
        //  6023: iload           12
        //  6025: dconst_0       
        //  6026: dastore        
        //  6027: aload           20
        //  6029: astore_2       
        //  6030: aload           23
        //  6032: astore          19
        //  6034: aload           31
        //  6036: astore          20
        //  6038: aload           24
        //  6040: astore          23
        //  6042: aload           30
        //  6044: astore          24
        //  6046: goto            12187
        //  6049: aload           23
        //  6051: astore          31
        //  6053: aload_2        
        //  6054: astore          23
        //  6056: aload           21
        //  6058: astore          30
        //  6060: aload           19
        //  6062: astore          21
        //  6064: aload           21
        //  6066: iload           12
        //  6068: aaload         
        //  6069: astore          19
        //  6071: aload           19
        //  6073: astore_2       
        //  6074: aload           19
        //  6076: aload           23
        //  6078: if_acmpne       6102
        //  6081: aload           20
        //  6083: astore          27
        //  6085: aload           23
        //  6087: astore          28
        //  6089: aload           30
        //  6091: astore          29
        //  6093: aload           31
        //  6095: iload           12
        //  6097: daload         
        //  6098: invokestatic    org/mozilla/javascript/ScriptRuntime.wrapNumber:(D)Ljava/lang/Number;
        //  6101: astore_2       
        //  6102: iload           12
        //  6104: iconst_1       
        //  6105: isub           
        //  6106: istore          12
        //  6108: aload           31
        //  6110: iload           12
        //  6112: daload         
        //  6113: d2i            
        //  6114: istore          13
        //  6116: aload           20
        //  6118: astore          27
        //  6120: aload           23
        //  6122: astore          28
        //  6124: aload           30
        //  6126: astore          29
        //  6128: aload           21
        //  6130: iload           12
        //  6132: aaload         
        //  6133: checkcast       [Ljava/lang/Object;
        //  6136: iload           13
        //  6138: aload_2        
        //  6139: aastore        
        //  6140: aload           31
        //  6142: iload           12
        //  6144: iload           13
        //  6146: iconst_1       
        //  6147: iadd           
        //  6148: i2d            
        //  6149: dastore        
        //  6150: aload           20
        //  6152: astore_2       
        //  6153: aload           23
        //  6155: astore          19
        //  6157: aload           31
        //  6159: astore          20
        //  6161: aload           24
        //  6163: astore          23
        //  6165: aload           30
        //  6167: astore          24
        //  6169: goto            12187
        //  6172: iload           11
        //  6174: istore          13
        //  6176: aload           21
        //  6178: astore          30
        //  6180: aload           19
        //  6182: astore          21
        //  6184: aload_1        
        //  6185: astore          31
        //  6187: aload_2        
        //  6188: astore          19
        //  6190: aload           20
        //  6192: astore_2       
        //  6193: aload_2        
        //  6194: astore          27
        //  6196: aload           19
        //  6198: astore          28
        //  6200: aload           30
        //  6202: astore          29
        //  6204: aload           21
        //  6206: iload           12
        //  6208: aaload         
        //  6209: checkcast       [Ljava/lang/Object;
        //  6212: astore          33
        //  6214: iload           12
        //  6216: iconst_1       
        //  6217: isub           
        //  6218: istore          12
        //  6220: aload_2        
        //  6221: astore          27
        //  6223: aload           19
        //  6225: astore          28
        //  6227: aload           30
        //  6229: astore          29
        //  6231: aload           21
        //  6233: iload           12
        //  6235: aaload         
        //  6236: checkcast       [I
        //  6239: astore          20
        //  6241: iload           14
        //  6243: bipush          66
        //  6245: if_icmpne       6292
        //  6248: aload_2        
        //  6249: astore          27
        //  6251: aload           19
        //  6253: astore          28
        //  6255: aload           30
        //  6257: astore          29
        //  6259: aload           31
        //  6261: getfield        org/mozilla/javascript/Interpreter$CallFrame.idata:Lorg/mozilla/javascript/InterpreterData;
        //  6264: getfield        org/mozilla/javascript/InterpreterData.literalIds:[Ljava/lang/Object;
        //  6267: iload           13
        //  6269: aaload         
        //  6270: checkcast       [Ljava/lang/Object;
        //  6273: aload           33
        //  6275: aload           20
        //  6277: aload           30
        //  6279: aload           31
        //  6281: getfield        org/mozilla/javascript/Interpreter$CallFrame.scope:Lorg/mozilla/javascript/Scriptable;
        //  6284: invokestatic    org/mozilla/javascript/ScriptRuntime.newObjectLiteral:([Ljava/lang/Object;[Ljava/lang/Object;[ILorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;)Lorg/mozilla/javascript/Scriptable;
        //  6287: astore          20
        //  6289: goto            12012
        //  6292: aconst_null    
        //  6293: astore          20
        //  6295: iload           14
        //  6297: bipush          -31
        //  6299: if_icmpne       6329
        //  6302: aload_2        
        //  6303: astore          27
        //  6305: aload           19
        //  6307: astore          28
        //  6309: aload           30
        //  6311: astore          29
        //  6313: aload           31
        //  6315: getfield        org/mozilla/javascript/Interpreter$CallFrame.idata:Lorg/mozilla/javascript/InterpreterData;
        //  6318: getfield        org/mozilla/javascript/InterpreterData.literalIds:[Ljava/lang/Object;
        //  6321: iload           13
        //  6323: aaload         
        //  6324: checkcast       [I
        //  6327: astore          20
        //  6329: aload_2        
        //  6330: astore          27
        //  6332: aload           19
        //  6334: astore          28
        //  6336: aload           30
        //  6338: astore          29
        //  6340: aload           33
        //  6342: aload           20
        //  6344: aload           30
        //  6346: aload           31
        //  6348: getfield        org/mozilla/javascript/Interpreter$CallFrame.scope:Lorg/mozilla/javascript/Scriptable;
        //  6351: invokestatic    org/mozilla/javascript/ScriptRuntime.newArrayLiteral:([Ljava/lang/Object;[ILorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;)Lorg/mozilla/javascript/Scriptable;
        //  6354: astore          20
        //  6356: goto            12012
        //  6359: aload           20
        //  6361: astore          27
        //  6363: aload_2        
        //  6364: astore          28
        //  6366: aload           21
        //  6368: astore          29
        //  6370: aload_1        
        //  6371: astore          30
        //  6373: aload           24
        //  6375: aload           30
        //  6377: getfield        org/mozilla/javascript/Interpreter$CallFrame.pc:I
        //  6380: baload         
        //  6381: istore          11
        //  6383: iload           11
        //  6385: sipush          255
        //  6388: iand           
        //  6389: istore          13
        //  6391: iload           13
        //  6393: istore          11
        //  6395: aload           30
        //  6397: aload           30
        //  6399: getfield        org/mozilla/javascript/Interpreter$CallFrame.pc:I
        //  6402: iconst_1       
        //  6403: iadd           
        //  6404: putfield        org/mozilla/javascript/Interpreter$CallFrame.pc:I
        //  6407: iload           13
        //  6409: istore          11
        //  6411: goto            6789
        //  6414: aload           20
        //  6416: astore          27
        //  6418: aload_2        
        //  6419: astore          28
        //  6421: aload           21
        //  6423: astore          29
        //  6425: aload_1        
        //  6426: astore          30
        //  6428: aload           24
        //  6430: aload           30
        //  6432: getfield        org/mozilla/javascript/Interpreter$CallFrame.pc:I
        //  6435: invokestatic    org/mozilla/javascript/Interpreter.getIndex:([BI)I
        //  6438: istore          13
        //  6440: iload           13
        //  6442: istore          11
        //  6444: aload           30
        //  6446: aload           30
        //  6448: getfield        org/mozilla/javascript/Interpreter$CallFrame.pc:I
        //  6451: iconst_2       
        //  6452: iadd           
        //  6453: putfield        org/mozilla/javascript/Interpreter$CallFrame.pc:I
        //  6456: iload           13
        //  6458: istore          11
        //  6460: goto            6789
        //  6463: aload           20
        //  6465: astore          27
        //  6467: aload_2        
        //  6468: astore          28
        //  6470: aload           21
        //  6472: astore          29
        //  6474: aload_1        
        //  6475: astore          30
        //  6477: aload           24
        //  6479: aload           30
        //  6481: getfield        org/mozilla/javascript/Interpreter$CallFrame.pc:I
        //  6484: invokestatic    org/mozilla/javascript/Interpreter.getInt:([BI)I
        //  6487: istore          13
        //  6489: iload           13
        //  6491: istore          11
        //  6493: aload           30
        //  6495: aload           30
        //  6497: getfield        org/mozilla/javascript/Interpreter$CallFrame.pc:I
        //  6500: iconst_4       
        //  6501: iadd           
        //  6502: putfield        org/mozilla/javascript/Interpreter$CallFrame.pc:I
        //  6505: iload           13
        //  6507: istore          11
        //  6509: goto            6789
        //  6512: aload           49
        //  6514: iconst_0       
        //  6515: aaload         
        //  6516: astore          22
        //  6518: goto            12164
        //  6521: aload           20
        //  6523: astore          27
        //  6525: aload_2        
        //  6526: astore          28
        //  6528: aload           21
        //  6530: astore          29
        //  6532: aload_1        
        //  6533: astore          31
        //  6535: aload           49
        //  6537: aload           24
        //  6539: aload           31
        //  6541: getfield        org/mozilla/javascript/Interpreter$CallFrame.pc:I
        //  6544: baload         
        //  6545: sipush          255
        //  6548: iand           
        //  6549: aaload         
        //  6550: astore          30
        //  6552: aload           30
        //  6554: astore          22
        //  6556: aload           31
        //  6558: aload           31
        //  6560: getfield        org/mozilla/javascript/Interpreter$CallFrame.pc:I
        //  6563: iconst_1       
        //  6564: iadd           
        //  6565: putfield        org/mozilla/javascript/Interpreter$CallFrame.pc:I
        //  6568: aload           30
        //  6570: astore          22
        //  6572: goto            12164
        //  6575: aload           20
        //  6577: astore          27
        //  6579: aload_2        
        //  6580: astore          28
        //  6582: aload           21
        //  6584: astore          29
        //  6586: aload_1        
        //  6587: astore          31
        //  6589: aload           49
        //  6591: aload           24
        //  6593: aload           31
        //  6595: getfield        org/mozilla/javascript/Interpreter$CallFrame.pc:I
        //  6598: invokestatic    org/mozilla/javascript/Interpreter.getIndex:([BI)I
        //  6601: aaload         
        //  6602: astore          30
        //  6604: aload           30
        //  6606: astore          22
        //  6608: aload           31
        //  6610: aload           31
        //  6612: getfield        org/mozilla/javascript/Interpreter$CallFrame.pc:I
        //  6615: iconst_2       
        //  6616: iadd           
        //  6617: putfield        org/mozilla/javascript/Interpreter$CallFrame.pc:I
        //  6620: aload           30
        //  6622: astore          22
        //  6624: goto            12164
        //  6627: aload           20
        //  6629: astore          27
        //  6631: aload_2        
        //  6632: astore          28
        //  6634: aload           21
        //  6636: astore          29
        //  6638: aload_1        
        //  6639: astore          31
        //  6641: aload           49
        //  6643: aload           24
        //  6645: aload           31
        //  6647: getfield        org/mozilla/javascript/Interpreter$CallFrame.pc:I
        //  6650: invokestatic    org/mozilla/javascript/Interpreter.getInt:([BI)I
        //  6653: aaload         
        //  6654: astore          30
        //  6656: aload           30
        //  6658: astore          22
        //  6660: aload           31
        //  6662: aload           31
        //  6664: getfield        org/mozilla/javascript/Interpreter$CallFrame.pc:I
        //  6667: iconst_4       
        //  6668: iadd           
        //  6669: putfield        org/mozilla/javascript/Interpreter$CallFrame.pc:I
        //  6672: aload           30
        //  6674: astore          22
        //  6676: goto            12164
        //  6679: astore          24
        //  6681: aload           20
        //  6683: astore          23
        //  6685: aload_1        
        //  6686: astore          19
        //  6688: aload           24
        //  6690: astore_1       
        //  6691: aload           21
        //  6693: astore          20
        //  6695: aload           23
        //  6697: astore          21
        //  6699: goto            6822
        //  6702: aload           20
        //  6704: astore          30
        //  6706: aload_2        
        //  6707: astore          31
        //  6709: aload           21
        //  6711: astore          33
        //  6713: aload_1        
        //  6714: astore          36
        //  6716: aload           30
        //  6718: astore          27
        //  6720: aload           31
        //  6722: astore          28
        //  6724: aload           33
        //  6726: astore          29
        //  6728: aload           36
        //  6730: getfield        org/mozilla/javascript/Interpreter$CallFrame.pc:I
        //  6733: istore          11
        //  6735: aload           30
        //  6737: astore          27
        //  6739: aload           31
        //  6741: astore          28
        //  6743: aload           33
        //  6745: astore          29
        //  6747: aload           36
        //  6749: iload           11
        //  6751: iconst_1       
        //  6752: iadd           
        //  6753: putfield        org/mozilla/javascript/Interpreter$CallFrame.pc:I
        //  6756: aload           24
        //  6758: iload           11
        //  6760: baload         
        //  6761: istore          13
        //  6763: iload           13
        //  6765: istore          11
        //  6767: aload_1        
        //  6768: aload           19
        //  6770: aload           23
        //  6772: iload           12
        //  6774: aload           46
        //  6776: aload           47
        //  6778: iload           13
        //  6780: invokestatic    org/mozilla/javascript/Interpreter.doGetVar:(Lorg/mozilla/javascript/Interpreter$CallFrame;[Ljava/lang/Object;[DI[Ljava/lang/Object;[DI)I
        //  6783: istore          12
        //  6785: iload           13
        //  6787: istore          11
        //  6789: aload_1        
        //  6790: astore          27
        //  6792: aload           19
        //  6794: astore_1       
        //  6795: aload           24
        //  6797: astore          19
        //  6799: goto            12077
        //  6802: astore          23
        //  6804: aload           21
        //  6806: astore          19
        //  6808: aload           20
        //  6810: astore          21
        //  6812: aload           19
        //  6814: astore          20
        //  6816: aload_1        
        //  6817: astore          19
        //  6819: aload           23
        //  6821: astore_1       
        //  6822: aload           22
        //  6824: astore          23
        //  6826: aload           20
        //  6828: astore          24
        //  6830: aload           19
        //  6832: astore          22
        //  6834: aload_2        
        //  6835: astore          20
        //  6837: aload           25
        //  6839: astore          19
        //  6841: aload_1        
        //  6842: astore_2       
        //  6843: aload           22
        //  6845: astore_1       
        //  6846: aload           24
        //  6848: astore          22
        //  6850: goto            10498
        //  6853: aload           20
        //  6855: astore          30
        //  6857: aload_2        
        //  6858: astore          31
        //  6860: aload           21
        //  6862: astore          33
        //  6864: aload_1        
        //  6865: astore          36
        //  6867: aload           30
        //  6869: astore          27
        //  6871: aload           31
        //  6873: astore          28
        //  6875: aload           33
        //  6877: astore          29
        //  6879: aload           36
        //  6881: getfield        org/mozilla/javascript/Interpreter$CallFrame.pc:I
        //  6884: istore          11
        //  6886: aload           30
        //  6888: astore          27
        //  6890: aload           31
        //  6892: astore          28
        //  6894: aload           33
        //  6896: astore          29
        //  6898: aload           36
        //  6900: iload           11
        //  6902: iconst_1       
        //  6903: iadd           
        //  6904: putfield        org/mozilla/javascript/Interpreter$CallFrame.pc:I
        //  6907: aload           24
        //  6909: iload           11
        //  6911: baload         
        //  6912: istore          13
        //  6914: iload           13
        //  6916: istore          11
        //  6918: aload_1        
        //  6919: aload           19
        //  6921: aload           23
        //  6923: iload           12
        //  6925: aload           46
        //  6927: aload           47
        //  6929: aload           48
        //  6931: iload           13
        //  6933: invokestatic    org/mozilla/javascript/Interpreter.doSetVar:(Lorg/mozilla/javascript/Interpreter$CallFrame;[Ljava/lang/Object;[DI[Ljava/lang/Object;[D[II)I
        //  6936: istore          12
        //  6938: iload           13
        //  6940: istore          11
        //  6942: goto            6789
        //  6945: aload_2        
        //  6946: astore          28
        //  6948: aload           21
        //  6950: astore          27
        //  6952: aload           19
        //  6954: astore          21
        //  6956: iload           12
        //  6958: iconst_1       
        //  6959: iadd           
        //  6960: istore          12
        //  6962: aload           21
        //  6964: iload           12
        //  6966: aload           20
        //  6968: aastore        
        //  6969: aload           20
        //  6971: astore_2       
        //  6972: aload           28
        //  6974: astore          19
        //  6976: aload           23
        //  6978: astore          20
        //  6980: aload           24
        //  6982: astore          23
        //  6984: aload           27
        //  6986: astore          24
        //  6988: goto            12187
        //  6991: aload           23
        //  6993: astore          31
        //  6995: aload_2        
        //  6996: astore          23
        //  6998: aload           21
        //  7000: astore          30
        //  7002: aload_1        
        //  7003: astore          33
        //  7005: aload           19
        //  7007: astore          21
        //  7009: aload           21
        //  7011: iload           12
        //  7013: aaload         
        //  7014: astore          19
        //  7016: aload           19
        //  7018: astore_2       
        //  7019: aload           19
        //  7021: aload           23
        //  7023: if_acmpne       7047
        //  7026: aload           20
        //  7028: astore          27
        //  7030: aload           23
        //  7032: astore          28
        //  7034: aload           30
        //  7036: astore          29
        //  7038: aload           31
        //  7040: iload           12
        //  7042: daload         
        //  7043: invokestatic    org/mozilla/javascript/ScriptRuntime.wrapNumber:(D)Ljava/lang/Number;
        //  7046: astore_2       
        //  7047: iload           12
        //  7049: iconst_1       
        //  7050: isub           
        //  7051: istore          12
        //  7053: aload           20
        //  7055: astore          27
        //  7057: aload           23
        //  7059: astore          28
        //  7061: aload           30
        //  7063: astore          29
        //  7065: aload           33
        //  7067: aload_2        
        //  7068: aload           33
        //  7070: getfield        org/mozilla/javascript/Interpreter$CallFrame.scope:Lorg/mozilla/javascript/Scriptable;
        //  7073: invokestatic    org/mozilla/javascript/ScriptRuntime.enterDotQuery:(Ljava/lang/Object;Lorg/mozilla/javascript/Scriptable;)Lorg/mozilla/javascript/Scriptable;
        //  7076: putfield        org/mozilla/javascript/Interpreter$CallFrame.scope:Lorg/mozilla/javascript/Scriptable;
        //  7079: aload           20
        //  7081: astore_2       
        //  7082: aload           23
        //  7084: astore          19
        //  7086: aload           31
        //  7088: astore          20
        //  7090: aload           24
        //  7092: astore          23
        //  7094: aload           30
        //  7096: astore          24
        //  7098: goto            12187
        //  7101: aload           23
        //  7103: astore          36
        //  7105: aload           20
        //  7107: astore          30
        //  7109: aload_2        
        //  7110: astore          31
        //  7112: aload           21
        //  7114: astore          33
        //  7116: aload           24
        //  7118: astore          38
        //  7120: aload_1        
        //  7121: astore          39
        //  7123: aload           19
        //  7125: astore          37
        //  7127: aload           30
        //  7129: astore          27
        //  7131: aload           31
        //  7133: astore          28
        //  7135: aload           33
        //  7137: astore          29
        //  7139: aload           39
        //  7141: iload           12
        //  7143: invokestatic    org/mozilla/javascript/Interpreter.stack_boolean:(Lorg/mozilla/javascript/Interpreter$CallFrame;I)Z
        //  7146: aload           39
        //  7148: getfield        org/mozilla/javascript/Interpreter$CallFrame.scope:Lorg/mozilla/javascript/Scriptable;
        //  7151: invokestatic    org/mozilla/javascript/ScriptRuntime.updateDotQuery:(ZLorg/mozilla/javascript/Scriptable;)Ljava/lang/Object;
        //  7154: astore          40
        //  7156: aload           40
        //  7158: ifnull          12338
        //  7161: aload           37
        //  7163: iload           12
        //  7165: aload           40
        //  7167: aastore        
        //  7168: aload           30
        //  7170: astore          27
        //  7172: aload           31
        //  7174: astore          28
        //  7176: aload           33
        //  7178: astore          29
        //  7180: aload           39
        //  7182: aload           39
        //  7184: getfield        org/mozilla/javascript/Interpreter$CallFrame.scope:Lorg/mozilla/javascript/Scriptable;
        //  7187: invokestatic    org/mozilla/javascript/ScriptRuntime.leaveDotQuery:(Lorg/mozilla/javascript/Scriptable;)Lorg/mozilla/javascript/Scriptable;
        //  7190: putfield        org/mozilla/javascript/Interpreter$CallFrame.scope:Lorg/mozilla/javascript/Scriptable;
        //  7193: aload           30
        //  7195: astore          27
        //  7197: aload           31
        //  7199: astore          28
        //  7201: aload           33
        //  7203: astore          29
        //  7205: aload           39
        //  7207: aload           39
        //  7209: getfield        org/mozilla/javascript/Interpreter$CallFrame.pc:I
        //  7212: iconst_2       
        //  7213: iadd           
        //  7214: putfield        org/mozilla/javascript/Interpreter$CallFrame.pc:I
        //  7217: goto            11977
        //  7220: iload           16
        //  7222: istore          17
        //  7224: aload_1        
        //  7225: astore          39
        //  7227: iload           17
        //  7229: ifeq            7252
        //  7232: aload           30
        //  7234: astore          27
        //  7236: aload           31
        //  7238: astore          28
        //  7240: aload           33
        //  7242: astore          29
        //  7244: aload           33
        //  7246: aload           39
        //  7248: iconst_2       
        //  7249: invokestatic    org/mozilla/javascript/Interpreter.addInstructionCount:(Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Interpreter$CallFrame;I)V
        //  7252: aload           30
        //  7254: astore          27
        //  7256: aload           31
        //  7258: astore          28
        //  7260: aload           33
        //  7262: astore          29
        //  7264: aload           38
        //  7266: aload           39
        //  7268: getfield        org/mozilla/javascript/Interpreter$CallFrame.pc:I
        //  7271: invokestatic    org/mozilla/javascript/Interpreter.getShort:([BI)I
        //  7274: istore          12
        //  7276: iload           12
        //  7278: ifeq            7311
        //  7281: aload           30
        //  7283: astore          27
        //  7285: aload           31
        //  7287: astore          28
        //  7289: aload           33
        //  7291: astore          29
        //  7293: aload           39
        //  7295: aload           39
        //  7297: getfield        org/mozilla/javascript/Interpreter$CallFrame.pc:I
        //  7300: iload           12
        //  7302: iconst_1       
        //  7303: isub           
        //  7304: iadd           
        //  7305: putfield        org/mozilla/javascript/Interpreter$CallFrame.pc:I
        //  7308: goto            7344
        //  7311: aload           30
        //  7313: astore          27
        //  7315: aload           31
        //  7317: astore          28
        //  7319: aload           33
        //  7321: astore          29
        //  7323: aload           39
        //  7325: aload           39
        //  7327: getfield        org/mozilla/javascript/Interpreter$CallFrame.idata:Lorg/mozilla/javascript/InterpreterData;
        //  7330: getfield        org/mozilla/javascript/InterpreterData.longJumps:Lorg/mozilla/javascript/UintMap;
        //  7333: aload           39
        //  7335: getfield        org/mozilla/javascript/Interpreter$CallFrame.pc:I
        //  7338: invokevirtual   org/mozilla/javascript/UintMap.getExistingInt:(I)I
        //  7341: putfield        org/mozilla/javascript/Interpreter$CallFrame.pc:I
        //  7344: aload           30
        //  7346: astore_2       
        //  7347: aload           31
        //  7349: astore          19
        //  7351: iload           13
        //  7353: istore          12
        //  7355: aload           36
        //  7357: astore          20
        //  7359: aload           37
        //  7361: astore          21
        //  7363: aload           38
        //  7365: astore          23
        //  7367: aload           33
        //  7369: astore          24
        //  7371: iload           17
        //  7373: ifeq            12187
        //  7376: aload           30
        //  7378: astore          27
        //  7380: aload           31
        //  7382: astore          28
        //  7384: aload           33
        //  7386: astore          29
        //  7388: aload           39
        //  7390: aload           39
        //  7392: getfield        org/mozilla/javascript/Interpreter$CallFrame.pc:I
        //  7395: putfield        org/mozilla/javascript/Interpreter$CallFrame.pcPrevBranch:I
        //  7398: aload           30
        //  7400: astore_2       
        //  7401: aload           31
        //  7403: astore          19
        //  7405: iload           13
        //  7407: istore          12
        //  7409: aload           36
        //  7411: astore          20
        //  7413: aload           37
        //  7415: astore          21
        //  7417: aload           38
        //  7419: astore          23
        //  7421: aload           33
        //  7423: astore          24
        //  7425: goto            12187
        //  7428: aload           22
        //  7430: astore          31
        //  7432: aload           25
        //  7434: astore          30
        //  7436: iload           16
        //  7438: istore          17
        //  7440: aload           21
        //  7442: astore          33
        //  7444: aload           24
        //  7446: astore          37
        //  7448: aload           19
        //  7450: astore          36
        //  7452: aload           23
        //  7454: astore          24
        //  7456: aload_1        
        //  7457: astore          19
        //  7459: aload_2        
        //  7460: astore          21
        //  7462: iload           17
        //  7464: ifeq            7495
        //  7467: aload           20
        //  7469: astore          27
        //  7471: aload           21
        //  7473: astore          28
        //  7475: aload           33
        //  7477: astore          29
        //  7479: aload           33
        //  7481: aload           33
        //  7483: getfield        org/mozilla/javascript/Context.instructionCount:I
        //  7486: bipush          100
        //  7488: iadd           
        //  7489: putfield        org/mozilla/javascript/Context.instructionCount:I
        //  7492: goto            7495
        //  7495: iload           12
        //  7497: iload           11
        //  7499: iconst_1       
        //  7500: iadd           
        //  7501: isub           
        //  7502: istore          12
        //  7504: aload           36
        //  7506: iload           12
        //  7508: aaload         
        //  7509: checkcast       Lorg/mozilla/javascript/Callable;
        //  7512: astore          23
        //  7514: aload           36
        //  7516: iload           12
        //  7518: iconst_1       
        //  7519: iadd           
        //  7520: aaload         
        //  7521: checkcast       Lorg/mozilla/javascript/Scriptable;
        //  7524: astore          29
        //  7526: iload           14
        //  7528: bipush          70
        //  7530: if_icmpne       7601
        //  7533: aload           36
        //  7535: iload           12
        //  7537: aload           23
        //  7539: aload           29
        //  7541: aload           36
        //  7543: aload           24
        //  7545: iload           12
        //  7547: iconst_2       
        //  7548: iadd           
        //  7549: iload           11
        //  7551: invokestatic    org/mozilla/javascript/Interpreter.getArgsArray:([Ljava/lang/Object;[DII)[Ljava/lang/Object;
        //  7554: aload           33
        //  7556: invokestatic    org/mozilla/javascript/ScriptRuntime.callRef:(Lorg/mozilla/javascript/Callable;Lorg/mozilla/javascript/Scriptable;[Ljava/lang/Object;Lorg/mozilla/javascript/Context;)Lorg/mozilla/javascript/Ref;
        //  7559: aastore        
        //  7560: aload           20
        //  7562: astore          22
        //  7564: aload           36
        //  7566: astore_1       
        //  7567: aload           19
        //  7569: astore          20
        //  7571: aload           24
        //  7573: astore_2       
        //  7574: aload           23
        //  7576: astore          19
        //  7578: aload           33
        //  7580: astore          24
        //  7582: aload           22
        //  7584: astore          23
        //  7586: goto            8678
        //  7589: astore_2       
        //  7590: aload           33
        //  7592: astore          29
        //  7594: aload           23
        //  7596: astore          26
        //  7598: goto            12356
        //  7601: aload           19
        //  7603: getfield        org/mozilla/javascript/Interpreter$CallFrame.scope:Lorg/mozilla/javascript/Scriptable;
        //  7606: astore          28
        //  7608: aload           19
        //  7610: getfield        org/mozilla/javascript/Interpreter$CallFrame.useActivation:Z
        //  7613: istore          18
        //  7615: iload           18
        //  7617: ifeq            7633
        //  7620: aload           19
        //  7622: getfield        org/mozilla/javascript/Interpreter$CallFrame.scope:Lorg/mozilla/javascript/Scriptable;
        //  7625: invokestatic    org/mozilla/javascript/ScriptableObject.getTopLevelScope:(Lorg/mozilla/javascript/Scriptable;)Lorg/mozilla/javascript/Scriptable;
        //  7628: astore          28
        //  7630: goto            7633
        //  7633: aload           23
        //  7635: instanceof      Lorg/mozilla/javascript/InterpretedFunction;
        //  7638: istore          18
        //  7640: iload           18
        //  7642: ifeq            7885
        //  7645: aload           23
        //  7647: checkcast       Lorg/mozilla/javascript/InterpretedFunction;
        //  7650: astore          27
        //  7652: aload           19
        //  7654: getfield        org/mozilla/javascript/Interpreter$CallFrame.fnOrScript:Lorg/mozilla/javascript/InterpretedFunction;
        //  7657: getfield        org/mozilla/javascript/InterpretedFunction.securityDomain:Ljava/lang/Object;
        //  7660: aload           27
        //  7662: getfield        org/mozilla/javascript/InterpretedFunction.securityDomain:Ljava/lang/Object;
        //  7665: if_acmpne       7843
        //  7668: aload           19
        //  7670: astore_2       
        //  7671: new             Lorg/mozilla/javascript/Interpreter$CallFrame;
        //  7674: dup            
        //  7675: aconst_null    
        //  7676: invokespecial   org/mozilla/javascript/Interpreter$CallFrame.<init>:(Lorg/mozilla/javascript/Interpreter$1;)V
        //  7679: astore          26
        //  7681: iload           14
        //  7683: bipush          -55
        //  7685: if_icmpne       7702
        //  7688: aload           19
        //  7690: getfield        org/mozilla/javascript/Interpreter$CallFrame.parentFrame:Lorg/mozilla/javascript/Interpreter$CallFrame;
        //  7693: astore_2       
        //  7694: aload           33
        //  7696: aload           19
        //  7698: aconst_null    
        //  7699: invokestatic    org/mozilla/javascript/Interpreter.exitFrame:(Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Interpreter$CallFrame;Ljava/lang/Object;)V
        //  7702: aload           21
        //  7704: astore_1       
        //  7705: aload           33
        //  7707: aload           28
        //  7709: aload           29
        //  7711: aload           36
        //  7713: aload           24
        //  7715: iload           12
        //  7717: iconst_2       
        //  7718: iadd           
        //  7719: iload           11
        //  7721: aload           27
        //  7723: aload_2        
        //  7724: aload           26
        //  7726: invokestatic    org/mozilla/javascript/Interpreter.initFrame:(Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;Lorg/mozilla/javascript/Scriptable;[Ljava/lang/Object;[DIILorg/mozilla/javascript/InterpretedFunction;Lorg/mozilla/javascript/Interpreter$CallFrame;Lorg/mozilla/javascript/Interpreter$CallFrame;)V
        //  7729: iload           14
        //  7731: bipush          -55
        //  7733: if_icmpeq       7768
        //  7736: aload           19
        //  7738: iload           12
        //  7740: putfield        org/mozilla/javascript/Interpreter$CallFrame.savedStackTop:I
        //  7743: aload           19
        //  7745: iload           14
        //  7747: putfield        org/mozilla/javascript/Interpreter$CallFrame.savedCallOp:I
        //  7750: goto            7768
        //  7753: astore          22
        //  7755: aload           19
        //  7757: astore_1       
        //  7758: aload           20
        //  7760: astore_2       
        //  7761: aload           23
        //  7763: astore          26
        //  7765: goto            12397
        //  7768: aload           26
        //  7770: astore          19
        //  7772: aload           23
        //  7774: astore_2       
        //  7775: iload           17
        //  7777: istore          16
        //  7779: aload           30
        //  7781: astore          25
        //  7783: aload           31
        //  7785: astore          22
        //  7787: aload_1        
        //  7788: astore          21
        //  7790: aload           19
        //  7792: astore          23
        //  7794: aload_2        
        //  7795: astore_1       
        //  7796: aload           21
        //  7798: astore_2       
        //  7799: goto            2892
        //  7802: astore_2       
        //  7803: aload           19
        //  7805: astore          22
        //  7807: aload           30
        //  7809: astore          19
        //  7811: aload           20
        //  7813: astore          21
        //  7815: aload_0        
        //  7816: astore          24
        //  7818: aload_1        
        //  7819: astore          20
        //  7821: aload           22
        //  7823: astore_1       
        //  7824: aload           24
        //  7826: astore          22
        //  7828: iload           17
        //  7830: istore          16
        //  7832: aload           23
        //  7834: astore          26
        //  7836: aload           31
        //  7838: astore          23
        //  7840: goto            10498
        //  7843: goto            7885
        //  7846: astore_2       
        //  7847: aload           20
        //  7849: astore          24
        //  7851: aload           21
        //  7853: astore          20
        //  7855: aload           19
        //  7857: astore_1       
        //  7858: aload           30
        //  7860: astore          19
        //  7862: aload           33
        //  7864: astore          22
        //  7866: iload           17
        //  7868: istore          16
        //  7870: aload           24
        //  7872: astore          21
        //  7874: aload           23
        //  7876: astore          26
        //  7878: aload           31
        //  7880: astore          23
        //  7882: goto            10498
        //  7885: aload           21
        //  7887: astore_2       
        //  7888: aload           24
        //  7890: astore          27
        //  7892: aload           36
        //  7894: astore          26
        //  7896: aload           29
        //  7898: astore          24
        //  7900: aload           23
        //  7902: astore_1       
        //  7903: aload_1        
        //  7904: instanceof      Lorg/mozilla/javascript/NativeContinuation;
        //  7907: istore          18
        //  7909: iload           18
        //  7911: ifeq            8047
        //  7914: new             Lorg/mozilla/javascript/Interpreter$ContinuationJump;
        //  7917: dup            
        //  7918: aload_1        
        //  7919: checkcast       Lorg/mozilla/javascript/NativeContinuation;
        //  7922: aload           19
        //  7924: invokespecial   org/mozilla/javascript/Interpreter$ContinuationJump.<init>:(Lorg/mozilla/javascript/NativeContinuation;Lorg/mozilla/javascript/Interpreter$CallFrame;)V
        //  7927: astore          24
        //  7929: iload           11
        //  7931: ifne            7944
        //  7934: aload           24
        //  7936: aload           20
        //  7938: putfield        org/mozilla/javascript/Interpreter$ContinuationJump.result:Ljava/lang/Object;
        //  7941: goto            7968
        //  7944: aload           24
        //  7946: aload           26
        //  7948: iload           12
        //  7950: iconst_2       
        //  7951: iadd           
        //  7952: aaload         
        //  7953: putfield        org/mozilla/javascript/Interpreter$ContinuationJump.result:Ljava/lang/Object;
        //  7956: aload           24
        //  7958: aload           27
        //  7960: iload           12
        //  7962: iconst_2       
        //  7963: iadd           
        //  7964: daload         
        //  7965: putfield        org/mozilla/javascript/Interpreter$ContinuationJump.resultDbl:D
        //  7968: aload           20
        //  7970: astore          21
        //  7972: aload           19
        //  7974: astore          25
        //  7976: aload           30
        //  7978: astore          19
        //  7980: aload           31
        //  7982: astore          23
        //  7984: aload_2        
        //  7985: astore          20
        //  7987: aload_0        
        //  7988: astore          22
        //  7990: aload           24
        //  7992: astore_2       
        //  7993: aload_1        
        //  7994: astore          24
        //  7996: iload           17
        //  7998: istore          16
        //  8000: goto            2036
        //  8003: astore          23
        //  8005: aload           19
        //  8007: astore          22
        //  8009: aload_1        
        //  8010: astore          26
        //  8012: aload           30
        //  8014: astore          19
        //  8016: aload           20
        //  8018: astore          21
        //  8020: aload_2        
        //  8021: astore          20
        //  8023: aload_0        
        //  8024: astore          24
        //  8026: aload           23
        //  8028: astore_2       
        //  8029: aload           22
        //  8031: astore_1       
        //  8032: aload           24
        //  8034: astore          22
        //  8036: iload           17
        //  8038: istore          16
        //  8040: aload           31
        //  8042: astore          23
        //  8044: goto            10498
        //  8047: aload_1        
        //  8048: instanceof      Lorg/mozilla/javascript/IdFunctionObject;
        //  8051: istore          18
        //  8053: iload           18
        //  8055: ifeq            8379
        //  8058: aload_1        
        //  8059: checkcast       Lorg/mozilla/javascript/IdFunctionObject;
        //  8062: astore          29
        //  8064: aload           29
        //  8066: invokestatic    org/mozilla/javascript/NativeContinuation.isContinuationConstructor:(Lorg/mozilla/javascript/IdFunctionObject;)Z
        //  8069: istore          18
        //  8071: iload           18
        //  8073: ifeq            8183
        //  8076: aload           19
        //  8078: getfield        org/mozilla/javascript/Interpreter$CallFrame.stack:[Ljava/lang/Object;
        //  8081: astore          22
        //  8083: aload           19
        //  8085: getfield        org/mozilla/javascript/Interpreter$CallFrame.parentFrame:Lorg/mozilla/javascript/Interpreter$CallFrame;
        //  8088: astore          23
        //  8090: aload_0        
        //  8091: astore          24
        //  8093: aload           22
        //  8095: iload           12
        //  8097: aload           24
        //  8099: aload           23
        //  8101: iconst_0       
        //  8102: invokestatic    org/mozilla/javascript/Interpreter.captureContinuation:(Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Interpreter$CallFrame;Z)Lorg/mozilla/javascript/NativeContinuation;
        //  8105: aastore        
        //  8106: aload           20
        //  8108: astore          23
        //  8110: aload_1        
        //  8111: astore          20
        //  8113: aload           26
        //  8115: astore_1       
        //  8116: aload           27
        //  8118: astore_2       
        //  8119: aload           19
        //  8121: astore          22
        //  8123: aload           20
        //  8125: astore          19
        //  8127: aload           22
        //  8129: astore          20
        //  8131: goto            8678
        //  8134: astore          21
        //  8136: goto            8141
        //  8139: astore          21
        //  8141: aload_0        
        //  8142: astore          22
        //  8144: aload           21
        //  8146: astore          24
        //  8148: aload           20
        //  8150: astore          21
        //  8152: aload           19
        //  8154: astore          23
        //  8156: aload_1        
        //  8157: astore          26
        //  8159: aload           30
        //  8161: astore          19
        //  8163: aload_2        
        //  8164: astore          20
        //  8166: aload           24
        //  8168: astore_2       
        //  8169: aload           23
        //  8171: astore_1       
        //  8172: iload           17
        //  8174: istore          16
        //  8176: aload           31
        //  8178: astore          23
        //  8180: goto            11209
        //  8183: aload_0        
        //  8184: astore          23
        //  8186: aload           29
        //  8188: invokestatic    org/mozilla/javascript/BaseFunction.isApplyOrCall:(Lorg/mozilla/javascript/IdFunctionObject;)Z
        //  8191: ifeq            8321
        //  8194: aload           24
        //  8196: invokestatic    org/mozilla/javascript/ScriptRuntime.getCallable:(Lorg/mozilla/javascript/Scriptable;)Lorg/mozilla/javascript/Callable;
        //  8199: astore          33
        //  8201: aload           33
        //  8203: instanceof      Lorg/mozilla/javascript/InterpretedFunction;
        //  8206: ifeq            8318
        //  8209: aload           33
        //  8211: checkcast       Lorg/mozilla/javascript/InterpretedFunction;
        //  8214: astore          33
        //  8216: aload           19
        //  8218: getfield        org/mozilla/javascript/Interpreter$CallFrame.fnOrScript:Lorg/mozilla/javascript/InterpretedFunction;
        //  8221: getfield        org/mozilla/javascript/InterpretedFunction.securityDomain:Ljava/lang/Object;
        //  8224: astore          36
        //  8226: aload           33
        //  8228: getfield        org/mozilla/javascript/InterpretedFunction.securityDomain:Ljava/lang/Object;
        //  8231: astore          38
        //  8233: aload           36
        //  8235: aload           38
        //  8237: if_acmpne       8318
        //  8240: aload           23
        //  8242: aload           19
        //  8244: iload           11
        //  8246: aload           26
        //  8248: aload           27
        //  8250: iload           12
        //  8252: iload           14
        //  8254: aload           28
        //  8256: aload           29
        //  8258: aload           33
        //  8260: invokestatic    org/mozilla/javascript/Interpreter.initFrameForApplyOrCall:(Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Interpreter$CallFrame;I[Ljava/lang/Object;[DIILorg/mozilla/javascript/Scriptable;Lorg/mozilla/javascript/IdFunctionObject;Lorg/mozilla/javascript/InterpretedFunction;)Lorg/mozilla/javascript/Interpreter$CallFrame;
        //  8263: astore          21
        //  8265: dload           5
        //  8267: dstore_3       
        //  8268: aload           32
        //  8270: astore          19
        //  8272: iload           17
        //  8274: istore          16
        //  8276: aload           30
        //  8278: astore          25
        //  8280: aload           31
        //  8282: astore          22
        //  8284: aload           23
        //  8286: astore          24
        //  8288: aload           21
        //  8290: astore          23
        //  8292: aload           24
        //  8294: astore          21
        //  8296: goto            182
        //  8299: astore          21
        //  8301: aload_1        
        //  8302: astore          26
        //  8304: aload           21
        //  8306: astore_1       
        //  8307: aload           19
        //  8309: astore          24
        //  8311: aload           20
        //  8313: astore          21
        //  8315: goto            9217
        //  8318: goto            8379
        //  8321: goto            8379
        //  8324: astore          21
        //  8326: goto            8331
        //  8329: astore          21
        //  8331: aload           20
        //  8333: astore          23
        //  8335: aload_0        
        //  8336: astore          22
        //  8338: aload           21
        //  8340: astore          20
        //  8342: aload           19
        //  8344: astore          21
        //  8346: aload_1        
        //  8347: astore          26
        //  8349: aload           30
        //  8351: astore          19
        //  8353: aload_2        
        //  8354: astore_1       
        //  8355: aload           20
        //  8357: astore_2       
        //  8358: aload_1        
        //  8359: astore          20
        //  8361: aload           21
        //  8363: astore_1       
        //  8364: iload           17
        //  8366: istore          16
        //  8368: aload           23
        //  8370: astore          21
        //  8372: aload           31
        //  8374: astore          23
        //  8376: goto            10498
        //  8379: aload           24
        //  8381: astore          29
        //  8383: aload           20
        //  8385: astore          23
        //  8387: aload_0        
        //  8388: astore          24
        //  8390: aload_1        
        //  8391: instanceof      Lorg/mozilla/javascript/ScriptRuntime$NoSuchMethodShim;
        //  8394: istore          18
        //  8396: iload           18
        //  8398: ifeq            8606
        //  8401: aload_1        
        //  8402: checkcast       Lorg/mozilla/javascript/ScriptRuntime$NoSuchMethodShim;
        //  8405: astore          20
        //  8407: aload           20
        //  8409: getfield        org/mozilla/javascript/ScriptRuntime$NoSuchMethodShim.noSuchMethodMethod:Lorg/mozilla/javascript/Callable;
        //  8412: astore          33
        //  8414: aload           33
        //  8416: instanceof      Lorg/mozilla/javascript/InterpretedFunction;
        //  8419: ifeq            8606
        //  8422: aload           33
        //  8424: checkcast       Lorg/mozilla/javascript/InterpretedFunction;
        //  8427: astore          33
        //  8429: aload           19
        //  8431: getfield        org/mozilla/javascript/Interpreter$CallFrame.fnOrScript:Lorg/mozilla/javascript/InterpretedFunction;
        //  8434: getfield        org/mozilla/javascript/InterpretedFunction.securityDomain:Ljava/lang/Object;
        //  8437: astore          36
        //  8439: aload           33
        //  8441: getfield        org/mozilla/javascript/InterpretedFunction.securityDomain:Ljava/lang/Object;
        //  8444: astore          38
        //  8446: aload           36
        //  8448: aload           38
        //  8450: if_acmpne       8606
        //  8453: aload           24
        //  8455: aload           19
        //  8457: iload           11
        //  8459: aload           26
        //  8461: aload           27
        //  8463: iload           12
        //  8465: iload           14
        //  8467: aload           29
        //  8469: aload           28
        //  8471: aload           20
        //  8473: aload           33
        //  8475: invokestatic    org/mozilla/javascript/Interpreter.initFrameForNoSuchMethod:(Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Interpreter$CallFrame;I[Ljava/lang/Object;[DIILorg/mozilla/javascript/Scriptable;Lorg/mozilla/javascript/Scriptable;Lorg/mozilla/javascript/ScriptRuntime$NoSuchMethodShim;Lorg/mozilla/javascript/InterpretedFunction;)Lorg/mozilla/javascript/Interpreter$CallFrame;
        //  8478: astore          20
        //  8480: aload           20
        //  8482: astore          19
        //  8484: aload           24
        //  8486: astore          21
        //  8488: dload           5
        //  8490: dstore_3       
        //  8491: iload           17
        //  8493: istore          16
        //  8495: aload           23
        //  8497: astore          20
        //  8499: aload           30
        //  8501: astore          25
        //  8503: aload           31
        //  8505: astore          22
        //  8507: aload_1        
        //  8508: astore          24
        //  8510: aload           19
        //  8512: astore_1       
        //  8513: goto            10072
        //  8516: astore          20
        //  8518: aload           19
        //  8520: astore          21
        //  8522: aload           30
        //  8524: astore          19
        //  8526: aload_2        
        //  8527: astore          22
        //  8529: aload_1        
        //  8530: astore          26
        //  8532: aload           20
        //  8534: astore_2       
        //  8535: aload           22
        //  8537: astore          20
        //  8539: aload           21
        //  8541: astore_1       
        //  8542: aload           24
        //  8544: astore          22
        //  8546: iload           17
        //  8548: istore          16
        //  8550: aload           23
        //  8552: astore          21
        //  8554: aload           31
        //  8556: astore          23
        //  8558: goto            10498
        //  8561: astore          20
        //  8563: aload           19
        //  8565: astore          21
        //  8567: aload           30
        //  8569: astore          19
        //  8571: aload_2        
        //  8572: astore          22
        //  8574: aload_1        
        //  8575: astore          26
        //  8577: aload           20
        //  8579: astore_2       
        //  8580: aload           22
        //  8582: astore          20
        //  8584: aload           21
        //  8586: astore_1       
        //  8587: aload           24
        //  8589: astore          22
        //  8591: iload           17
        //  8593: istore          16
        //  8595: aload           23
        //  8597: astore          21
        //  8599: aload           31
        //  8601: astore          23
        //  8603: goto            10498
        //  8606: aload           24
        //  8608: aload           19
        //  8610: putfield        org/mozilla/javascript/Context.lastInterpreterFrame:Ljava/lang/Object;
        //  8613: aload           19
        //  8615: iload           14
        //  8617: putfield        org/mozilla/javascript/Interpreter$CallFrame.savedCallOp:I
        //  8620: aload           19
        //  8622: iload           12
        //  8624: putfield        org/mozilla/javascript/Interpreter$CallFrame.savedStackTop:I
        //  8627: iload           11
        //  8629: istore          13
        //  8631: aload           26
        //  8633: aload           27
        //  8635: iload           12
        //  8637: iconst_2       
        //  8638: iadd           
        //  8639: iload           13
        //  8641: invokestatic    org/mozilla/javascript/Interpreter.getArgsArray:([Ljava/lang/Object;[DII)[Ljava/lang/Object;
        //  8644: astore          20
        //  8646: aload           26
        //  8648: iload           12
        //  8650: aload_1        
        //  8651: aload           24
        //  8653: aload           28
        //  8655: aload           29
        //  8657: aload           20
        //  8659: invokeinterface org/mozilla/javascript/Callable.call:(Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;Lorg/mozilla/javascript/Scriptable;[Ljava/lang/Object;)Ljava/lang/Object;
        //  8664: aastore        
        //  8665: aload           27
        //  8667: astore_2       
        //  8668: aload           19
        //  8670: astore          20
        //  8672: aload_1        
        //  8673: astore          19
        //  8675: aload           26
        //  8677: astore_1       
        //  8678: aload           19
        //  8680: astore          26
        //  8682: aload           20
        //  8684: astore          25
        //  8686: iload           17
        //  8688: istore          16
        //  8690: aload           23
        //  8692: astore          20
        //  8694: aload           31
        //  8696: astore          22
        //  8698: aload_2        
        //  8699: astore          23
        //  8701: aload           24
        //  8703: astore          27
        //  8705: aload           21
        //  8707: astore_2       
        //  8708: aload           37
        //  8710: astore          24
        //  8712: aload_1        
        //  8713: astore          19
        //  8715: aload           25
        //  8717: astore_1       
        //  8718: aload           30
        //  8720: astore          25
        //  8722: aload           27
        //  8724: astore          21
        //  8726: goto            394
        //  8729: astore          20
        //  8731: aload_1        
        //  8732: astore          26
        //  8734: aload           20
        //  8736: astore_1       
        //  8737: iload           13
        //  8739: istore          11
        //  8741: aload           23
        //  8743: astore          21
        //  8745: goto            9209
        //  8748: astore          20
        //  8750: aload_1        
        //  8751: astore          26
        //  8753: aload           19
        //  8755: astore_1       
        //  8756: aload           30
        //  8758: astore          19
        //  8760: aload_2        
        //  8761: astore          21
        //  8763: aload           20
        //  8765: astore_2       
        //  8766: aload           21
        //  8768: astore          20
        //  8770: aload           24
        //  8772: astore          22
        //  8774: iload           17
        //  8776: istore          16
        //  8778: aload           23
        //  8780: astore          21
        //  8782: aload           31
        //  8784: astore          23
        //  8786: goto            10498
        //  8789: astore          20
        //  8791: aload_1        
        //  8792: astore          26
        //  8794: aload           19
        //  8796: astore_1       
        //  8797: aload           30
        //  8799: astore          19
        //  8801: aload_2        
        //  8802: astore          21
        //  8804: aload           20
        //  8806: astore_2       
        //  8807: aload           21
        //  8809: astore          20
        //  8811: aload           24
        //  8813: astore          22
        //  8815: iload           17
        //  8817: istore          16
        //  8819: aload           23
        //  8821: astore          21
        //  8823: aload           31
        //  8825: astore          23
        //  8827: goto            10498
        //  8830: astore          20
        //  8832: aload_1        
        //  8833: astore          26
        //  8835: aload           19
        //  8837: astore_1       
        //  8838: aload           30
        //  8840: astore          19
        //  8842: aload_2        
        //  8843: astore          21
        //  8845: aload           20
        //  8847: astore_2       
        //  8848: aload           21
        //  8850: astore          20
        //  8852: aload           24
        //  8854: astore          22
        //  8856: iload           17
        //  8858: istore          16
        //  8860: aload           23
        //  8862: astore          21
        //  8864: aload           31
        //  8866: astore          23
        //  8868: goto            10498
        //  8871: astore          23
        //  8873: aload           20
        //  8875: astore          21
        //  8877: aload_0        
        //  8878: astore          22
        //  8880: aload           23
        //  8882: astore          20
        //  8884: aload_1        
        //  8885: astore          26
        //  8887: aload           19
        //  8889: astore_1       
        //  8890: aload           30
        //  8892: astore          19
        //  8894: aload_2        
        //  8895: astore          23
        //  8897: aload           20
        //  8899: astore_2       
        //  8900: aload           23
        //  8902: astore          20
        //  8904: iload           17
        //  8906: istore          16
        //  8908: aload           31
        //  8910: astore          23
        //  8912: goto            10498
        //  8915: astore          23
        //  8917: aload           20
        //  8919: astore          21
        //  8921: aload_0        
        //  8922: astore          22
        //  8924: aload           23
        //  8926: astore          20
        //  8928: aload_1        
        //  8929: astore          26
        //  8931: aload           19
        //  8933: astore_1       
        //  8934: aload           30
        //  8936: astore          19
        //  8938: aload_2        
        //  8939: astore          23
        //  8941: aload           20
        //  8943: astore_2       
        //  8944: aload           23
        //  8946: astore          20
        //  8948: iload           17
        //  8950: istore          16
        //  8952: aload           31
        //  8954: astore          23
        //  8956: goto            10498
        //  8959: astore_2       
        //  8960: aload           20
        //  8962: astore          24
        //  8964: aload           21
        //  8966: astore          20
        //  8968: aload           19
        //  8970: astore_1       
        //  8971: aload           30
        //  8973: astore          19
        //  8975: aload           33
        //  8977: astore          22
        //  8979: iload           17
        //  8981: istore          16
        //  8983: aload           24
        //  8985: astore          21
        //  8987: aload           23
        //  8989: astore          26
        //  8991: aload           31
        //  8993: astore          23
        //  8995: goto            10498
        //  8998: astore_2       
        //  8999: aload           20
        //  9001: astore          23
        //  9003: aload           21
        //  9005: astore          20
        //  9007: aload           19
        //  9009: astore_1       
        //  9010: aload           30
        //  9012: astore          19
        //  9014: aload           33
        //  9016: astore          22
        //  9018: iload           17
        //  9020: istore          16
        //  9022: aload           23
        //  9024: astore          21
        //  9026: aload           31
        //  9028: astore          23
        //  9030: goto            10498
        //  9033: aload_1        
        //  9034: getfield        org/mozilla/javascript/Interpreter$CallFrame.localShift:I
        //  9037: istore          13
        //  9039: iload           13
        //  9041: iload           11
        //  9043: iadd           
        //  9044: istore          11
        //  9046: aload           19
        //  9048: iload           11
        //  9050: aconst_null    
        //  9051: aastore        
        //  9052: goto            394
        //  9055: aload           23
        //  9057: astore          27
        //  9059: aload           19
        //  9061: astore          28
        //  9063: aload           28
        //  9065: iload           12
        //  9067: aaload         
        //  9068: astore          29
        //  9070: iload           12
        //  9072: iconst_1       
        //  9073: isub           
        //  9074: istore          12
        //  9076: aload           27
        //  9078: iload           12
        //  9080: daload         
        //  9081: d2i            
        //  9082: istore          13
        //  9084: aload           28
        //  9086: iload           12
        //  9088: aaload         
        //  9089: checkcast       [Ljava/lang/Object;
        //  9092: iload           13
        //  9094: aload           29
        //  9096: aastore        
        //  9097: aload           28
        //  9099: iload           12
        //  9101: iconst_1       
        //  9102: isub           
        //  9103: aaload         
        //  9104: checkcast       [I
        //  9107: iload           13
        //  9109: iconst_m1      
        //  9110: iastore        
        //  9111: aload           27
        //  9113: iload           12
        //  9115: iload           13
        //  9117: iconst_1       
        //  9118: iadd           
        //  9119: i2d            
        //  9120: dastore        
        //  9121: goto            9190
        //  9124: aload           23
        //  9126: astore          27
        //  9128: aload           19
        //  9130: astore          28
        //  9132: aload           28
        //  9134: iload           12
        //  9136: aaload         
        //  9137: astore          29
        //  9139: iload           12
        //  9141: iconst_1       
        //  9142: isub           
        //  9143: istore          12
        //  9145: aload           27
        //  9147: iload           12
        //  9149: daload         
        //  9150: d2i            
        //  9151: istore          13
        //  9153: aload           28
        //  9155: iload           12
        //  9157: aaload         
        //  9158: checkcast       [Ljava/lang/Object;
        //  9161: iload           13
        //  9163: aload           29
        //  9165: aastore        
        //  9166: aload           28
        //  9168: iload           12
        //  9170: iconst_1       
        //  9171: isub           
        //  9172: aaload         
        //  9173: checkcast       [I
        //  9176: iload           13
        //  9178: iconst_1       
        //  9179: iastore        
        //  9180: aload           27
        //  9182: iload           12
        //  9184: iload           13
        //  9186: iconst_1       
        //  9187: iadd           
        //  9188: i2d            
        //  9189: dastore        
        //  9190: goto            9052
        //  9193: aload           21
        //  9195: astore          24
        //  9197: astore          23
        //  9199: aload           20
        //  9201: astore          21
        //  9203: aload_1        
        //  9204: astore          19
        //  9206: aload           23
        //  9208: astore_1       
        //  9209: aload           24
        //  9211: astore          23
        //  9213: aload           19
        //  9215: astore          24
        //  9217: aload           22
        //  9219: astore          27
        //  9221: aload           25
        //  9223: astore          19
        //  9225: aload_2        
        //  9226: astore          20
        //  9228: aload_1        
        //  9229: astore_2       
        //  9230: aload           24
        //  9232: astore_1       
        //  9233: aload           23
        //  9235: astore          22
        //  9237: aload           27
        //  9239: astore          23
        //  9241: goto            10498
        //  9244: aload           22
        //  9246: astore          27
        //  9248: aload           25
        //  9250: astore          29
        //  9252: iload           16
        //  9254: istore          17
        //  9256: aload           20
        //  9258: astore          26
        //  9260: aload           21
        //  9262: astore          28
        //  9264: aload           23
        //  9266: astore          31
        //  9268: aload           19
        //  9270: astore          33
        //  9272: aload_1        
        //  9273: astore          30
        //  9275: aload           33
        //  9277: iload           12
        //  9279: aaload         
        //  9280: astore          36
        //  9282: aload_2        
        //  9283: astore          23
        //  9285: aload           36
        //  9287: astore          19
        //  9289: aload           36
        //  9291: aload           23
        //  9293: if_acmpne       9322
        //  9296: aload           31
        //  9298: iload           12
        //  9300: daload         
        //  9301: invokestatic    org/mozilla/javascript/ScriptRuntime.wrapNumber:(D)Ljava/lang/Number;
        //  9304: astore          19
        //  9306: goto            9322
        //  9309: astore          19
        //  9311: aload           36
        //  9313: astore          26
        //  9315: aload           27
        //  9317: astore          23
        //  9319: goto            9486
        //  9322: iload           12
        //  9324: iconst_1       
        //  9325: isub           
        //  9326: istore          12
        //  9328: aload           33
        //  9330: iload           12
        //  9332: aaload         
        //  9333: checkcast       Lorg/mozilla/javascript/Scriptable;
        //  9336: astore          37
        //  9338: aload           27
        //  9340: astore          36
        //  9342: aload           19
        //  9344: astore          27
        //  9346: aload           33
        //  9348: iload           12
        //  9350: aload           37
        //  9352: aload           19
        //  9354: aload           28
        //  9356: aload           36
        //  9358: invokestatic    org/mozilla/javascript/ScriptRuntime.setConst:(Lorg/mozilla/javascript/Scriptable;Ljava/lang/Object;Lorg/mozilla/javascript/Context;Ljava/lang/String;)Ljava/lang/Object;
        //  9361: aastore        
        //  9362: aload           19
        //  9364: astore          21
        //  9366: aload           36
        //  9368: astore          22
        //  9370: aload           30
        //  9372: astore_2       
        //  9373: iload           17
        //  9375: istore          16
        //  9377: aload           26
        //  9379: astore          20
        //  9381: aload           29
        //  9383: astore          25
        //  9385: aload           33
        //  9387: astore_1       
        //  9388: aload           23
        //  9390: astore          19
        //  9392: aload           31
        //  9394: astore          30
        //  9396: aload           21
        //  9398: astore          26
        //  9400: goto            12438
        //  9403: astore          19
        //  9405: aload           27
        //  9407: astore          26
        //  9409: goto            9482
        //  9412: astore_2       
        //  9413: aload           19
        //  9415: astore          24
        //  9417: aload           30
        //  9419: astore_1       
        //  9420: aload           29
        //  9422: astore          19
        //  9424: aload           23
        //  9426: astore          20
        //  9428: aload           28
        //  9430: astore          22
        //  9432: iload           17
        //  9434: istore          16
        //  9436: aload           26
        //  9438: astore          21
        //  9440: aload           24
        //  9442: astore          26
        //  9444: aload           27
        //  9446: astore          23
        //  9448: goto            10498
        //  9451: aload_1        
        //  9452: astore          27
        //  9454: aload           27
        //  9456: getfield        org/mozilla/javascript/Interpreter$CallFrame.pc:I
        //  9459: istore          11
        //  9461: aload           27
        //  9463: iload           11
        //  9465: iconst_1       
        //  9466: iadd           
        //  9467: putfield        org/mozilla/javascript/Interpreter$CallFrame.pc:I
        //  9470: aload           24
        //  9472: iload           11
        //  9474: baload         
        //  9475: istore          11
        //  9477: goto            10341
        //  9480: astore          19
        //  9482: aload           22
        //  9484: astore          23
        //  9486: aload           20
        //  9488: astore          24
        //  9490: aload_2        
        //  9491: astore          20
        //  9493: aload           19
        //  9495: astore_2       
        //  9496: aload           25
        //  9498: astore          19
        //  9500: aload           21
        //  9502: astore          22
        //  9504: aload           24
        //  9506: astore          21
        //  9508: goto            10498
        //  9511: aload_1        
        //  9512: astore          28
        //  9514: aload           28
        //  9516: getfield        org/mozilla/javascript/Interpreter$CallFrame.frozen:Z
        //  9519: istore          17
        //  9521: iload           17
        //  9523: ifne            9593
        //  9526: aload           28
        //  9528: aload           28
        //  9530: getfield        org/mozilla/javascript/Interpreter$CallFrame.pc:I
        //  9533: iconst_1       
        //  9534: isub           
        //  9535: putfield        org/mozilla/javascript/Interpreter$CallFrame.pc:I
        //  9538: aload           28
        //  9540: invokestatic    org/mozilla/javascript/Interpreter.captureFrameForGenerator:(Lorg/mozilla/javascript/Interpreter$CallFrame;)Lorg/mozilla/javascript/Interpreter$CallFrame;
        //  9543: astore          19
        //  9545: aload           19
        //  9547: astore          27
        //  9549: aload           19
        //  9551: iconst_1       
        //  9552: putfield        org/mozilla/javascript/Interpreter$CallFrame.frozen:Z
        //  9555: aload           19
        //  9557: astore          27
        //  9559: aload           28
        //  9561: new             Lorg/mozilla/javascript/NativeGenerator;
        //  9564: dup            
        //  9565: aload           28
        //  9567: getfield        org/mozilla/javascript/Interpreter$CallFrame.scope:Lorg/mozilla/javascript/Scriptable;
        //  9570: aload           19
        //  9572: getfield        org/mozilla/javascript/Interpreter$CallFrame.fnOrScript:Lorg/mozilla/javascript/InterpretedFunction;
        //  9575: aload           19
        //  9577: invokespecial   org/mozilla/javascript/NativeGenerator.<init>:(Lorg/mozilla/javascript/Scriptable;Lorg/mozilla/javascript/NativeFunction;Ljava/lang/Object;)V
        //  9580: putfield        org/mozilla/javascript/Interpreter$CallFrame.result:Ljava/lang/Object;
        //  9583: aload           28
        //  9585: astore_1       
        //  9586: aload           19
        //  9588: astore          26
        //  9590: goto            9926
        //  9593: aload           25
        //  9595: astore          27
        //  9597: aload           20
        //  9599: astore          29
        //  9601: iload           16
        //  9603: istore          17
        //  9605: aload           23
        //  9607: astore          37
        //  9609: aload           21
        //  9611: astore          28
        //  9613: aload_1        
        //  9614: astore          23
        //  9616: aload           22
        //  9618: astore          31
        //  9620: aload_2        
        //  9621: astore          30
        //  9623: aload           19
        //  9625: astore          38
        //  9627: aload           23
        //  9629: getfield        org/mozilla/javascript/Interpreter$CallFrame.frozen:Z
        //  9632: istore          18
        //  9634: iload           18
        //  9636: ifne            9668
        //  9639: aload           27
        //  9641: astore_1       
        //  9642: aload_1        
        //  9643: astore          27
        //  9645: iload           11
        //  9647: istore          13
        //  9649: aload           23
        //  9651: astore          33
        //  9653: aload           26
        //  9655: astore          36
        //  9657: aload           28
        //  9659: aload           23
        //  9661: iload           12
        //  9663: aload_1        
        //  9664: invokestatic    org/mozilla/javascript/Interpreter.freezeGenerator:(Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Interpreter$CallFrame;ILorg/mozilla/javascript/Interpreter$GeneratorState;)Ljava/lang/Object;
        //  9667: areturn        
        //  9668: aload           27
        //  9670: astore_1       
        //  9671: aload_1        
        //  9672: astore          27
        //  9674: iload           11
        //  9676: istore          13
        //  9678: aload           23
        //  9680: astore          33
        //  9682: aload           26
        //  9684: astore          36
        //  9686: aload           23
        //  9688: iload           12
        //  9690: aload_1        
        //  9691: iload           14
        //  9693: invokestatic    org/mozilla/javascript/Interpreter.thawGenerator:(Lorg/mozilla/javascript/Interpreter$CallFrame;ILorg/mozilla/javascript/Interpreter$GeneratorState;I)Ljava/lang/Object;
        //  9696: astore          19
        //  9698: getstatic       org/mozilla/javascript/Scriptable.NOT_FOUND:Ljava/lang/Object;
        //  9701: astore          25
        //  9703: aload           19
        //  9705: aload           25
        //  9707: if_acmpeq       9747
        //  9710: aload           19
        //  9712: astore          24
        //  9714: aload           19
        //  9716: astore_2       
        //  9717: aload           23
        //  9719: astore          25
        //  9721: aload_1        
        //  9722: astore          19
        //  9724: aload           30
        //  9726: astore          20
        //  9728: aload           31
        //  9730: astore          23
        //  9732: aload           28
        //  9734: astore          22
        //  9736: iload           17
        //  9738: istore          16
        //  9740: aload           29
        //  9742: astore          21
        //  9744: goto            10525
        //  9747: aload           19
        //  9749: astore          26
        //  9751: aload           31
        //  9753: astore          22
        //  9755: aload           23
        //  9757: astore_2       
        //  9758: iload           17
        //  9760: istore          16
        //  9762: aload           29
        //  9764: astore          20
        //  9766: aload_1        
        //  9767: astore          25
        //  9769: aload           38
        //  9771: astore_1       
        //  9772: aload           30
        //  9774: astore          19
        //  9776: aload           37
        //  9778: astore          30
        //  9780: goto            12438
        //  9783: astore          24
        //  9785: aload           19
        //  9787: astore          26
        //  9789: aload_1        
        //  9790: astore          19
        //  9792: aload           23
        //  9794: astore_1       
        //  9795: goto            12474
        //  9798: aload           20
        //  9800: astore          23
        //  9802: aload_2        
        //  9803: astore          20
        //  9805: astore_2       
        //  9806: aload           25
        //  9808: astore          19
        //  9810: aload           22
        //  9812: astore          24
        //  9814: aload           21
        //  9816: astore          22
        //  9818: aload           23
        //  9820: astore          21
        //  9822: aload           24
        //  9824: astore          23
        //  9826: goto            10498
        //  9829: aload           25
        //  9831: astore          19
        //  9833: iload           11
        //  9835: istore          12
        //  9837: aload           19
        //  9839: astore          27
        //  9841: iload           12
        //  9843: istore          13
        //  9845: aload_1        
        //  9846: astore          33
        //  9848: aload           26
        //  9850: astore          36
        //  9852: aload_1        
        //  9853: iconst_1       
        //  9854: putfield        org/mozilla/javascript/Interpreter$CallFrame.frozen:Z
        //  9857: aload           19
        //  9859: astore          27
        //  9861: iload           12
        //  9863: istore          13
        //  9865: aload_1        
        //  9866: astore          33
        //  9868: aload           26
        //  9870: astore          36
        //  9872: aload           24
        //  9874: aload_1        
        //  9875: getfield        org/mozilla/javascript/Interpreter$CallFrame.pc:I
        //  9878: invokestatic    org/mozilla/javascript/Interpreter.getIndex:([BI)I
        //  9881: istore          14
        //  9883: aload           19
        //  9885: astore          27
        //  9887: iload           12
        //  9889: istore          13
        //  9891: aload_1        
        //  9892: astore          33
        //  9894: aload           26
        //  9896: astore          36
        //  9898: aload           19
        //  9900: new             Lorg/mozilla/javascript/JavaScriptException;
        //  9903: dup            
        //  9904: aload_1        
        //  9905: getfield        org/mozilla/javascript/Interpreter$CallFrame.scope:Lorg/mozilla/javascript/Scriptable;
        //  9908: invokestatic    org/mozilla/javascript/NativeIterator.getStopIterationObject:(Lorg/mozilla/javascript/Scriptable;)Ljava/lang/Object;
        //  9911: aload_1        
        //  9912: getfield        org/mozilla/javascript/Interpreter$CallFrame.idata:Lorg/mozilla/javascript/InterpreterData;
        //  9915: getfield        org/mozilla/javascript/InterpreterData.itsSourceFile:Ljava/lang/String;
        //  9918: iload           14
        //  9920: invokespecial   org/mozilla/javascript/JavaScriptException.<init>:(Ljava/lang/Object;Ljava/lang/String;I)V
        //  9923: putfield        org/mozilla/javascript/Interpreter$GeneratorState.returnedException:Ljava/lang/RuntimeException;
        //  9926: aload           20
        //  9928: astore          28
        //  9930: iload           16
        //  9932: istore          17
        //  9934: aload           21
        //  9936: astore          24
        //  9938: aload           22
        //  9940: astore          29
        //  9942: aload_2        
        //  9943: astore          23
        //  9945: aload           25
        //  9947: astore          19
        //  9949: aload           19
        //  9951: astore          27
        //  9953: iload           11
        //  9955: istore          13
        //  9957: aload_1        
        //  9958: astore          33
        //  9960: aload           26
        //  9962: astore          36
        //  9964: aload           24
        //  9966: aload_1        
        //  9967: aconst_null    
        //  9968: invokestatic    org/mozilla/javascript/Interpreter.exitFrame:(Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Interpreter$CallFrame;Ljava/lang/Object;)V
        //  9971: aload           19
        //  9973: astore          27
        //  9975: iload           11
        //  9977: istore          13
        //  9979: aload_1        
        //  9980: astore          33
        //  9982: aload           26
        //  9984: astore          36
        //  9986: aload_1        
        //  9987: getfield        org/mozilla/javascript/Interpreter$CallFrame.result:Ljava/lang/Object;
        //  9990: astore          25
        //  9992: aload_1        
        //  9993: getfield        org/mozilla/javascript/Interpreter$CallFrame.resultDbl:D
        //  9996: dstore_3       
        //  9997: aload_1        
        //  9998: getfield        org/mozilla/javascript/Interpreter$CallFrame.parentFrame:Lorg/mozilla/javascript/Interpreter$CallFrame;
        // 10001: ifnull          10092
        // 10004: aload_1        
        // 10005: getfield        org/mozilla/javascript/Interpreter$CallFrame.parentFrame:Lorg/mozilla/javascript/Interpreter$CallFrame;
        // 10008: astore          20
        // 10010: aload           20
        // 10012: astore_1       
        // 10013: aload           20
        // 10015: astore_2       
        // 10016: aload           20
        // 10018: getfield        org/mozilla/javascript/Interpreter$CallFrame.frozen:Z
        // 10021: ifeq            10033
        // 10024: aload           20
        // 10026: astore_2       
        // 10027: aload           20
        // 10029: invokevirtual   org/mozilla/javascript/Interpreter$CallFrame.cloneFrozen:()Lorg/mozilla/javascript/Interpreter$CallFrame;
        // 10032: astore_1       
        // 10033: aload_1        
        // 10034: astore_2       
        // 10035: aload_1        
        // 10036: aload           25
        // 10038: dload_3        
        // 10039: invokestatic    org/mozilla/javascript/Interpreter.setCallResult:(Lorg/mozilla/javascript/Interpreter$CallFrame;Ljava/lang/Object;D)V
        // 10042: aconst_null    
        // 10043: astore          34
        // 10045: aload           29
        // 10047: astore          22
        // 10049: aload           24
        // 10051: astore          21
        // 10053: iload           17
        // 10055: istore          16
        // 10057: aload           28
        // 10059: astore          20
        // 10061: aload           26
        // 10063: astore          24
        // 10065: aload           23
        // 10067: astore_2       
        // 10068: aload           19
        // 10070: astore          25
        // 10072: aload           35
        // 10074: astore          19
        // 10076: aload_1        
        // 10077: astore          23
        // 10079: aload           24
        // 10081: astore_1       
        // 10082: goto            182
        // 10085: astore          20
        // 10087: aload_2        
        // 10088: astore_1       
        // 10089: goto            10108
        // 10092: aload           32
        // 10094: astore_1       
        // 10095: aload           23
        // 10097: astore          20
        // 10099: aload           24
        // 10101: astore          22
        // 10103: goto            11025
        // 10106: astore          20
        // 10108: aload           20
        // 10110: astore_2       
        // 10111: aload           23
        // 10113: astore          20
        // 10115: aload           24
        // 10117: astore          22
        // 10119: aload           25
        // 10121: astore          34
        // 10123: dload_3        
        // 10124: dstore          5
        // 10126: iload           17
        // 10128: istore          16
        // 10130: aload           28
        // 10132: astore          21
        // 10134: aload           29
        // 10136: astore          23
        // 10138: goto            10498
        // 10141: astore          24
        // 10143: aload           25
        // 10145: astore          34
        // 10147: goto            12474
        // 10150: iload           16
        // 10152: istore          18
        // 10154: iload           11
        // 10156: istore          15
        // 10158: aload           20
        // 10160: astore          39
        // 10162: aload_1        
        // 10163: astore          37
        // 10165: aload           24
        // 10167: astore          40
        // 10169: aload           25
        // 10171: astore          38
        // 10173: aload           22
        // 10175: astore          41
        // 10177: aload           23
        // 10179: astore          42
        // 10181: aload           19
        // 10183: astore          43
        // 10185: aload_2        
        // 10186: astore          44
        // 10188: aload           21
        // 10190: astore          45
        // 10192: aload           43
        // 10194: astore_1       
        // 10195: aload           38
        // 10197: astore          23
        // 10199: aload           44
        // 10201: astore          19
        // 10203: iload           15
        // 10205: istore          11
        // 10207: aload           40
        // 10209: astore          24
        // 10211: aload           41
        // 10213: astore          25
        // 10215: iload           12
        // 10217: istore          14
        // 10219: aload           37
        // 10221: astore          29
        // 10223: aload           45
        // 10225: astore          28
        // 10227: aload           42
        // 10229: astore          30
        // 10231: iload           18
        // 10233: istore          17
        // 10235: aload           39
        // 10237: astore          31
        // 10239: aload           38
        // 10241: astore          27
        // 10243: iload           15
        // 10245: istore          13
        // 10247: aload           37
        // 10249: astore          33
        // 10251: aload           26
        // 10253: astore          36
        // 10255: aload           37
        // 10257: getfield        org/mozilla/javascript/Interpreter$CallFrame.debuggerFrame:Lorg/mozilla/javascript/debug/DebugFrame;
        // 10260: ifnull          12415
        // 10263: aload           38
        // 10265: astore          27
        // 10267: iload           15
        // 10269: istore          13
        // 10271: aload           37
        // 10273: astore          33
        // 10275: aload           26
        // 10277: astore          36
        // 10279: aload           37
        // 10281: getfield        org/mozilla/javascript/Interpreter$CallFrame.debuggerFrame:Lorg/mozilla/javascript/debug/DebugFrame;
        // 10284: aload           45
        // 10286: invokeinterface org/mozilla/javascript/debug/DebugFrame.onDebuggerStatement:(Lorg/mozilla/javascript/Context;)V
        // 10291: aload           43
        // 10293: astore_1       
        // 10294: aload           38
        // 10296: astore          23
        // 10298: aload           44
        // 10300: astore          19
        // 10302: iload           15
        // 10304: istore          11
        // 10306: aload           40
        // 10308: astore          24
        // 10310: aload           41
        // 10312: astore          25
        // 10314: iload           12
        // 10316: istore          14
        // 10318: aload           37
        // 10320: astore          29
        // 10322: aload           45
        // 10324: astore          28
        // 10326: aload           42
        // 10328: astore          30
        // 10330: iload           18
        // 10332: istore          17
        // 10334: aload           39
        // 10336: astore          31
        // 10338: goto            12415
        // 10341: aload           20
        // 10343: astore          31
        // 10345: iload           16
        // 10347: istore          17
        // 10349: aload           23
        // 10351: astore          30
        // 10353: aload           21
        // 10355: astore          28
        // 10357: aload_1        
        // 10358: astore          29
        // 10360: aload           22
        // 10362: astore          37
        // 10364: aload_2        
        // 10365: astore          38
        // 10367: aload           25
        // 10369: astore          23
        // 10371: aload           19
        // 10373: astore_1       
        // 10374: aload           23
        // 10376: astore          27
        // 10378: iload           11
        // 10380: istore          13
        // 10382: aload           29
        // 10384: astore          33
        // 10386: aload           26
        // 10388: astore          36
        // 10390: aload           29
        // 10392: aload_1        
        // 10393: aload           30
        // 10395: iload           12
        // 10397: aload           46
        // 10399: aload           47
        // 10401: aload           48
        // 10403: iload           11
        // 10405: invokestatic    org/mozilla/javascript/Interpreter.doSetConstVar:(Lorg/mozilla/javascript/Interpreter$CallFrame;[Ljava/lang/Object;[DI[Ljava/lang/Object;[D[II)I
        // 10408: istore          14
        // 10410: aload           38
        // 10412: astore          19
        // 10414: aload           37
        // 10416: astore          25
        // 10418: goto            12415
        // 10421: astore          24
        // 10423: aload           25
        // 10425: astore          19
        // 10427: aload           20
        // 10429: astore          23
        // 10431: aload_2        
        // 10432: astore          20
        // 10434: aload           24
        // 10436: astore_2       
        // 10437: aload           22
        // 10439: astore          24
        // 10441: aload           21
        // 10443: astore          22
        // 10445: aload           23
        // 10447: astore          21
        // 10449: aload           24
        // 10451: astore          23
        // 10453: goto            10498
        // 10456: astore          23
        // 10458: aload           25
        // 10460: astore          19
        // 10462: aload           20
        // 10464: astore          27
        // 10466: aload           21
        // 10468: astore          20
        // 10470: aload           23
        // 10472: astore          25
        // 10474: aload           22
        // 10476: astore          23
        // 10478: aload_1        
        // 10479: astore          26
        // 10481: aload           27
        // 10483: astore          21
        // 10485: aload           20
        // 10487: astore          22
        // 10489: aload           24
        // 10491: astore_1       
        // 10492: aload_2        
        // 10493: astore          20
        // 10495: aload           25
        // 10497: astore_2       
        // 10498: aload           32
        // 10500: ifnull          10518
        // 10503: aload_2        
        // 10504: getstatic       java/lang/System.err:Ljava/io/PrintStream;
        // 10507: invokevirtual   java/lang/Throwable.printStackTrace:(Ljava/io/PrintStream;)V
        // 10510: new             Ljava/lang/IllegalStateException;
        // 10513: dup            
        // 10514: invokespecial   java/lang/IllegalStateException.<init>:()V
        // 10517: athrow         
        // 10518: aload           26
        // 10520: astore          24
        // 10522: aload_1        
        // 10523: astore          25
        // 10525: aload_2        
        // 10526: ifnonnull       10533
        // 10529: invokestatic    org/mozilla/javascript/Kit.codeBug:()Ljava/lang/RuntimeException;
        // 10532: pop            
        // 10533: aconst_null    
        // 10534: astore_1       
        // 10535: aload           19
        // 10537: ifnull          10564
        // 10540: aload           19
        // 10542: getfield        org/mozilla/javascript/Interpreter$GeneratorState.operation:I
        // 10545: iconst_2       
        // 10546: if_icmpne       10564
        // 10549: aload_2        
        // 10550: aload           19
        // 10552: getfield        org/mozilla/javascript/Interpreter$GeneratorState.value:Ljava/lang/Object;
        // 10555: if_acmpne       10564
        // 10558: iconst_1       
        // 10559: istore          11
        // 10561: goto            10574
        // 10564: aload_2        
        // 10565: instanceof      Lorg/mozilla/javascript/JavaScriptException;
        // 10568: ifeq            10577
        // 10571: iconst_2       
        // 10572: istore          11
        // 10574: goto            10711
        // 10577: aload_2        
        // 10578: instanceof      Lorg/mozilla/javascript/EcmaError;
        // 10581: ifeq            10590
        // 10584: iconst_2       
        // 10585: istore          11
        // 10587: goto            10574
        // 10590: aload_2        
        // 10591: instanceof      Lorg/mozilla/javascript/EvaluatorException;
        // 10594: ifeq            10603
        // 10597: iconst_2       
        // 10598: istore          11
        // 10600: goto            10574
        // 10603: aload_2        
        // 10604: instanceof      Lorg/mozilla/javascript/ContinuationPending;
        // 10607: ifeq            10616
        // 10610: iconst_0       
        // 10611: istore          11
        // 10613: goto            10574
        // 10616: aload_2        
        // 10617: instanceof      Ljava/lang/RuntimeException;
        // 10620: ifeq            10645
        // 10623: aload           22
        // 10625: bipush          13
        // 10627: invokevirtual   org/mozilla/javascript/Context.hasFeature:(I)Z
        // 10630: ifeq            10639
        // 10633: iconst_2       
        // 10634: istore          11
        // 10636: goto            10642
        // 10639: iconst_1       
        // 10640: istore          11
        // 10642: goto            10574
        // 10645: aload_2        
        // 10646: instanceof      Ljava/lang/Error;
        // 10649: ifeq            10674
        // 10652: aload           22
        // 10654: bipush          13
        // 10656: invokevirtual   org/mozilla/javascript/Context.hasFeature:(I)Z
        // 10659: ifeq            10668
        // 10662: iconst_2       
        // 10663: istore          11
        // 10665: goto            10671
        // 10668: iconst_0       
        // 10669: istore          11
        // 10671: goto            10574
        // 10674: aload_2        
        // 10675: instanceof      Lorg/mozilla/javascript/Interpreter$ContinuationJump;
        // 10678: ifeq            10692
        // 10681: iconst_1       
        // 10682: istore          11
        // 10684: aload_2        
        // 10685: checkcast       Lorg/mozilla/javascript/Interpreter$ContinuationJump;
        // 10688: astore_1       
        // 10689: goto            10711
        // 10692: aload           22
        // 10694: bipush          13
        // 10696: invokevirtual   org/mozilla/javascript/Context.hasFeature:(I)Z
        // 10699: ifeq            10708
        // 10702: iconst_2       
        // 10703: istore          11
        // 10705: goto            10711
        // 10708: iconst_1       
        // 10709: istore          11
        // 10711: aload_2        
        // 10712: astore          26
        // 10714: aload_1        
        // 10715: astore          27
        // 10717: iload           11
        // 10719: istore          12
        // 10721: iload           16
        // 10723: ifeq            10763
        // 10726: aload           22
        // 10728: aload           25
        // 10730: bipush          100
        // 10732: invokestatic    org/mozilla/javascript/Interpreter.addInstructionCount:(Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Interpreter$CallFrame;I)V
        // 10735: goto            10753
        // 10738: astore          26
        // 10740: aconst_null    
        // 10741: astore          27
        // 10743: iconst_0       
        // 10744: istore          12
        // 10746: goto            10763
        // 10749: astore_2       
        // 10750: iconst_1       
        // 10751: istore          11
        // 10753: iload           11
        // 10755: istore          12
        // 10757: aload_1        
        // 10758: astore          27
        // 10760: aload_2        
        // 10761: astore          26
        // 10763: aload           26
        // 10765: astore_1       
        // 10766: aload           19
        // 10768: astore_2       
        // 10769: aload           27
        // 10771: astore          29
        // 10773: aload           25
        // 10775: astore          28
        // 10777: iload           12
        // 10779: istore          11
        // 10781: aload           25
        // 10783: getfield        org/mozilla/javascript/Interpreter$CallFrame.debuggerFrame:Lorg/mozilla/javascript/debug/DebugFrame;
        // 10786: ifnull          10869
        // 10789: aload           26
        // 10791: astore_1       
        // 10792: aload           19
        // 10794: astore_2       
        // 10795: aload           27
        // 10797: astore          29
        // 10799: aload           25
        // 10801: astore          28
        // 10803: iload           12
        // 10805: istore          11
        // 10807: aload           26
        // 10809: instanceof      Ljava/lang/RuntimeException;
        // 10812: ifeq            10869
        // 10815: aload           26
        // 10817: checkcast       Ljava/lang/RuntimeException;
        // 10820: astore_1       
        // 10821: aload           25
        // 10823: getfield        org/mozilla/javascript/Interpreter$CallFrame.debuggerFrame:Lorg/mozilla/javascript/debug/DebugFrame;
        // 10826: aload           22
        // 10828: aload_1        
        // 10829: invokeinterface org/mozilla/javascript/debug/DebugFrame.onExceptionThrown:(Lorg/mozilla/javascript/Context;Ljava/lang/Throwable;)V
        // 10834: aload           26
        // 10836: astore_1       
        // 10837: aload           19
        // 10839: astore_2       
        // 10840: aload           27
        // 10842: astore          29
        // 10844: aload           25
        // 10846: astore          28
        // 10848: iload           12
        // 10850: istore          11
        // 10852: goto            10869
        // 10855: astore_1       
        // 10856: aconst_null    
        // 10857: astore          29
        // 10859: iconst_0       
        // 10860: istore          11
        // 10862: aload           25
        // 10864: astore          28
        // 10866: aload           19
        // 10868: astore_2       
        // 10869: iload           11
        // 10871: ifeq            10949
        // 10874: iload           11
        // 10876: iconst_2       
        // 10877: if_icmpeq       10886
        // 10880: iconst_1       
        // 10881: istore          17
        // 10883: goto            10889
        // 10886: iconst_0       
        // 10887: istore          17
        // 10889: aload           28
        // 10891: iload           17
        // 10893: invokestatic    org/mozilla/javascript/Interpreter.getExceptionHandler:(Lorg/mozilla/javascript/Interpreter$CallFrame;Z)I
        // 10896: istore          12
        // 10898: iload           12
        // 10900: iflt            10949
        // 10903: iload           12
        // 10905: istore          11
        // 10907: aload           22
        // 10909: astore          26
        // 10911: aload           20
        // 10913: astore          27
        // 10915: dload           5
        // 10917: dstore_3       
        // 10918: aload_1        
        // 10919: astore          19
        // 10921: aload           23
        // 10923: astore          22
        // 10925: aload           28
        // 10927: astore          23
        // 10929: aload_2        
        // 10930: astore          25
        // 10932: aload           24
        // 10934: astore_1       
        // 10935: aload           21
        // 10937: astore          20
        // 10939: aload           27
        // 10941: astore_2       
        // 10942: aload           26
        // 10944: astore          21
        // 10946: goto            182
        // 10949: aload           22
        // 10951: aload           28
        // 10953: aload_1        
        // 10954: invokestatic    org/mozilla/javascript/Interpreter.exitFrame:(Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Interpreter$CallFrame;Ljava/lang/Object;)V
        // 10957: aload           28
        // 10959: getfield        org/mozilla/javascript/Interpreter$CallFrame.parentFrame:Lorg/mozilla/javascript/Interpreter$CallFrame;
        // 10962: astore          28
        // 10964: aload           28
        // 10966: ifnonnull       11108
        // 10969: aload           29
        // 10971: ifnull          11018
        // 10974: aload           29
        // 10976: getfield        org/mozilla/javascript/Interpreter$ContinuationJump.branchFrame:Lorg/mozilla/javascript/Interpreter$CallFrame;
        // 10979: ifnull          10986
        // 10982: invokestatic    org/mozilla/javascript/Kit.codeBug:()Ljava/lang/RuntimeException;
        // 10985: pop            
        // 10986: aload           29
        // 10988: getfield        org/mozilla/javascript/Interpreter$ContinuationJump.capturedFrame:Lorg/mozilla/javascript/Interpreter$CallFrame;
        // 10991: ifnull          11000
        // 10994: iconst_m1      
        // 10995: istore          11
        // 10997: goto            10907
        // 11000: aload           29
        // 11002: getfield        org/mozilla/javascript/Interpreter$ContinuationJump.result:Ljava/lang/Object;
        // 11005: astore          25
        // 11007: aload           29
        // 11009: getfield        org/mozilla/javascript/Interpreter$ContinuationJump.resultDbl:D
        // 11012: dstore_3       
        // 11013: aconst_null    
        // 11014: astore_1       
        // 11015: goto            11025
        // 11018: aload           34
        // 11020: astore          25
        // 11022: dload           5
        // 11024: dstore_3       
        // 11025: aload           22
        // 11027: getfield        org/mozilla/javascript/Context.previousInterpreterInvocations:Lorg/mozilla/javascript/ObjArray;
        // 11030: ifnull          11060
        // 11033: aload           22
        // 11035: getfield        org/mozilla/javascript/Context.previousInterpreterInvocations:Lorg/mozilla/javascript/ObjArray;
        // 11038: invokevirtual   org/mozilla/javascript/ObjArray.size:()I
        // 11041: ifeq            11060
        // 11044: aload           22
        // 11046: aload           22
        // 11048: getfield        org/mozilla/javascript/Context.previousInterpreterInvocations:Lorg/mozilla/javascript/ObjArray;
        // 11051: invokevirtual   org/mozilla/javascript/ObjArray.pop:()Ljava/lang/Object;
        // 11054: putfield        org/mozilla/javascript/Context.lastInterpreterFrame:Ljava/lang/Object;
        // 11057: goto            11072
        // 11060: aload           22
        // 11062: aconst_null    
        // 11063: putfield        org/mozilla/javascript/Context.lastInterpreterFrame:Ljava/lang/Object;
        // 11066: aload           22
        // 11068: aconst_null    
        // 11069: putfield        org/mozilla/javascript/Context.previousInterpreterInvocations:Lorg/mozilla/javascript/ObjArray;
        // 11072: aload_1        
        // 11073: ifnull          11093
        // 11076: aload_1        
        // 11077: instanceof      Ljava/lang/RuntimeException;
        // 11080: ifeq            11088
        // 11083: aload_1        
        // 11084: checkcast       Ljava/lang/RuntimeException;
        // 11087: athrow         
        // 11088: aload_1        
        // 11089: checkcast       Ljava/lang/Error;
        // 11092: athrow         
        // 11093: aload           25
        // 11095: aload           20
        // 11097: if_acmpeq       11103
        // 11100: aload           25
        // 11102: areturn        
        // 11103: dload_3        
        // 11104: invokestatic    org/mozilla/javascript/ScriptRuntime.wrapNumber:(D)Ljava/lang/Number;
        // 11107: areturn        
        // 11108: aload           29
        // 11110: ifnull          11164
        // 11113: aload           29
        // 11115: getfield        org/mozilla/javascript/Interpreter$ContinuationJump.branchFrame:Lorg/mozilla/javascript/Interpreter$CallFrame;
        // 11118: aload           28
        // 11120: if_acmpne       11164
        // 11123: iconst_m1      
        // 11124: istore          11
        // 11126: aload           22
        // 11128: astore          26
        // 11130: dload           5
        // 11132: dstore_3       
        // 11133: aload_2        
        // 11134: astore          25
        // 11136: aload           20
        // 11138: astore_2       
        // 11139: aload_1        
        // 11140: astore          19
        // 11142: aload           23
        // 11144: astore          22
        // 11146: aload           28
        // 11148: astore          23
        // 11150: aload           24
        // 11152: astore_1       
        // 11153: aload           21
        // 11155: astore          20
        // 11157: aload           26
        // 11159: astore          21
        // 11161: goto            182
        // 11164: goto            10869
        // 11167: astore          24
        // 11169: aload           25
        // 11171: astore          19
        // 11173: aload           22
        // 11175: astore          23
        // 11177: aload_1        
        // 11178: astore          22
        // 11180: aload           27
        // 11182: astore          32
        // 11184: aload           26
        // 11186: astore_1       
        // 11187: aload           21
        // 11189: astore          25
        // 11191: aload           22
        // 11193: astore          26
        // 11195: aload           20
        // 11197: astore          21
        // 11199: aload           25
        // 11201: astore          22
        // 11203: aload_2        
        // 11204: astore          20
        // 11206: aload           24
        // 11208: astore_2       
        // 11209: goto            10498
        // 11212: aload           22
        // 11214: astore          27
        // 11216: aload           19
        // 11218: astore          22
        // 11220: aload           20
        // 11222: astore          31
        // 11224: aload           25
        // 11226: astore          20
        // 11228: iload           16
        // 11230: istore          17
        // 11232: aload           27
        // 11234: astore          25
        // 11236: aload_2        
        // 11237: astore          19
        // 11239: aload           20
        // 11241: astore_2       
        // 11242: aload           22
        // 11244: astore          20
        // 11246: aload_1        
        // 11247: astore          29
        // 11249: aload           23
        // 11251: astore          30
        // 11253: aload           20
        // 11255: astore_1       
        // 11256: aload_2        
        // 11257: astore          23
        // 11259: iload           12
        // 11261: istore          14
        // 11263: aload           21
        // 11265: astore          28
        // 11267: goto            12415
        // 11270: goto            9593
        // 11273: iload           14
        // 11275: bipush          59
        // 11277: if_icmpne       11286
        // 11280: iconst_1       
        // 11281: istore          11
        // 11283: goto            1762
        // 11286: iconst_2       
        // 11287: istore          11
        // 11289: goto            1762
        // 11292: iconst_0       
        // 11293: istore          11
        // 11295: goto            1842
        // 11298: iload           11
        // 11300: istore          13
        // 11302: goto            6914
        // 11305: iload           11
        // 11307: istore          13
        // 11309: goto            6763
        // 11312: iconst_0       
        // 11313: istore          12
        // 11315: goto            2230
        // 11318: iload           12
        // 11320: iconst_1       
        // 11321: iadd           
        // 11322: istore          12
        // 11324: aload           19
        // 11326: iload           12
        // 11328: aconst_null    
        // 11329: aastore        
        // 11330: goto            11352
        // 11333: iload           12
        // 11335: iconst_1       
        // 11336: iadd           
        // 11337: istore          12
        // 11339: aload           19
        // 11341: iload           12
        // 11343: aload           22
        // 11345: aastore        
        // 11346: goto            11352
        // 11349: goto            7428
        // 11352: aload_1        
        // 11353: astore          28
        // 11355: aload           23
        // 11357: astore_1       
        // 11358: aload           20
        // 11360: astore          27
        // 11362: aload           28
        // 11364: astore          23
        // 11366: aload           19
        // 11368: astore          20
        // 11370: aload           24
        // 11372: astore          19
        // 11374: aload           25
        // 11376: astore          24
        // 11378: aload           27
        // 11380: astore          25
        // 11382: goto            12107
        // 11385: astore          27
        // 11387: aload           22
        // 11389: astore          23
        // 11391: aload_1        
        // 11392: astore          22
        // 11394: aload_2        
        // 11395: astore          24
        // 11397: aload           20
        // 11399: astore_2       
        // 11400: aload           25
        // 11402: astore          19
        // 11404: aload           27
        // 11406: astore_1       
        // 11407: aload           24
        // 11409: astore          20
        // 11411: aload           21
        // 11413: astore          24
        // 11415: aload_2        
        // 11416: astore          21
        // 11418: goto            6841
        // 11421: aload           23
        // 11423: iload           12
        // 11425: dload           7
        // 11427: dastore        
        // 11428: aload           23
        // 11430: astore          30
        // 11432: aload_1        
        // 11433: astore          29
        // 11435: aload           19
        // 11437: astore_1       
        // 11438: aload           25
        // 11440: astore          23
        // 11442: aload_2        
        // 11443: astore          19
        // 11445: aload           22
        // 11447: astore          25
        // 11449: iload           12
        // 11451: istore          14
        // 11453: aload           21
        // 11455: astore          28
        // 11457: iload           16
        // 11459: istore          17
        // 11461: aload           20
        // 11463: astore          31
        // 11465: goto            12415
        // 11468: iconst_0       
        // 11469: istore          12
        // 11471: goto            3495
        // 11474: aload           23
        // 11476: astore          27
        // 11478: aload_1        
        // 11479: astore          23
        // 11481: aload           19
        // 11483: astore          29
        // 11485: aload           20
        // 11487: astore          28
        // 11489: aload           24
        // 11491: astore          19
        // 11493: aload           27
        // 11495: astore_1       
        // 11496: aload           29
        // 11498: astore          20
        // 11500: aload           28
        // 11502: astore          24
        // 11504: goto            12095
        // 11507: iload           13
        // 11509: istore          12
        // 11511: goto            11514
        // 11514: aload           24
        // 11516: astore          38
        // 11518: aload           19
        // 11520: astore          37
        // 11522: aload           23
        // 11524: astore          36
        // 11526: aload           20
        // 11528: astore          30
        // 11530: aload_2        
        // 11531: astore          31
        // 11533: iload           12
        // 11535: istore          13
        // 11537: aload           21
        // 11539: astore          33
        // 11541: goto            7220
        // 11544: astore          24
        // 11546: aload           28
        // 11548: astore          26
        // 11550: aload           27
        // 11552: astore          22
        // 11554: aload_1        
        // 11555: astore          19
        // 11557: aload           20
        // 11559: astore          23
        // 11561: aload           24
        // 11563: astore_1       
        // 11564: aload           21
        // 11566: astore          20
        // 11568: aload           23
        // 11570: astore          21
        // 11572: goto            6822
        // 11575: aload_1        
        // 11576: astore          27
        // 11578: aload           19
        // 11580: astore          28
        // 11582: aload_2        
        // 11583: astore_1       
        // 11584: aload           20
        // 11586: astore_2       
        // 11587: aload           24
        // 11589: astore          19
        // 11591: aload           25
        // 11593: astore          24
        // 11595: aload           23
        // 11597: astore          29
        // 11599: aload           28
        // 11601: astore          20
        // 11603: aload           27
        // 11605: astore          23
        // 11607: aload_2        
        // 11608: astore          25
        // 11610: aload           29
        // 11612: astore_2       
        // 11613: goto            12107
        // 11616: aload           23
        // 11618: astore          27
        // 11620: aload           19
        // 11622: astore          28
        // 11624: aload_2        
        // 11625: astore          23
        // 11627: aload           28
        // 11629: iload           12
        // 11631: iconst_1       
        // 11632: iadd           
        // 11633: aload           28
        // 11635: iload           12
        // 11637: aaload         
        // 11638: aastore        
        // 11639: aload           27
        // 11641: iload           12
        // 11643: iconst_1       
        // 11644: iadd           
        // 11645: aload           27
        // 11647: iload           12
        // 11649: daload         
        // 11650: dastore        
        // 11651: iload           12
        // 11653: iconst_1       
        // 11654: iadd           
        // 11655: istore          12
        // 11657: aload           27
        // 11659: astore_2       
        // 11660: goto            11575
        // 11663: aload           23
        // 11665: astore          27
        // 11667: aload           19
        // 11669: astore          28
        // 11671: aload_2        
        // 11672: astore          23
        // 11674: aload           28
        // 11676: iload           12
        // 11678: iconst_1       
        // 11679: iadd           
        // 11680: aload           28
        // 11682: iload           12
        // 11684: iconst_1       
        // 11685: isub           
        // 11686: aaload         
        // 11687: aastore        
        // 11688: aload           27
        // 11690: iload           12
        // 11692: iconst_1       
        // 11693: iadd           
        // 11694: aload           27
        // 11696: iload           12
        // 11698: iconst_1       
        // 11699: isub           
        // 11700: daload         
        // 11701: dastore        
        // 11702: aload           28
        // 11704: iload           12
        // 11706: iconst_2       
        // 11707: iadd           
        // 11708: aload           28
        // 11710: iload           12
        // 11712: aaload         
        // 11713: aastore        
        // 11714: aload           27
        // 11716: iload           12
        // 11718: iconst_2       
        // 11719: iadd           
        // 11720: aload           27
        // 11722: iload           12
        // 11724: daload         
        // 11725: dastore        
        // 11726: iload           12
        // 11728: iconst_2       
        // 11729: iadd           
        // 11730: istore          12
        // 11732: aload           27
        // 11734: astore_2       
        // 11735: goto            11575
        // 11738: aload           23
        // 11740: astore          26
        // 11742: aload           19
        // 11744: astore          27
        // 11746: aload_2        
        // 11747: astore          23
        // 11749: aload           27
        // 11751: iload           12
        // 11753: aaload         
        // 11754: astore          28
        // 11756: aload           27
        // 11758: iload           12
        // 11760: aload           27
        // 11762: iload           12
        // 11764: iconst_1       
        // 11765: isub           
        // 11766: aaload         
        // 11767: aastore        
        // 11768: aload           27
        // 11770: iload           12
        // 11772: iconst_1       
        // 11773: isub           
        // 11774: aload           28
        // 11776: aastore        
        // 11777: aload           26
        // 11779: iload           12
        // 11781: daload         
        // 11782: dstore          7
        // 11784: aload           26
        // 11786: iload           12
        // 11788: aload           26
        // 11790: iload           12
        // 11792: iconst_1       
        // 11793: isub           
        // 11794: daload         
        // 11795: dastore        
        // 11796: aload           26
        // 11798: iload           12
        // 11800: iconst_1       
        // 11801: isub           
        // 11802: dload           7
        // 11804: dastore        
        // 11805: aload           26
        // 11807: astore_2       
        // 11808: aload           28
        // 11810: astore          26
        // 11812: goto            11575
        // 11815: aload           19
        // 11817: iload           12
        // 11819: aconst_null    
        // 11820: aastore        
        // 11821: iload           12
        // 11823: iconst_1       
        // 11824: isub           
        // 11825: istore          12
        // 11827: goto            394
        // 11830: aload           23
        // 11832: astore          28
        // 11834: aload_1        
        // 11835: astore          23
        // 11837: aload           19
        // 11839: astore          27
        // 11841: aload           24
        // 11843: astore          19
        // 11845: aload           28
        // 11847: astore_1       
        // 11848: aload           25
        // 11850: astore          24
        // 11852: aload           20
        // 11854: astore          25
        // 11856: aload           27
        // 11858: astore          20
        // 11860: goto            12107
        // 11863: astore          24
        // 11865: aload           21
        // 11867: astore          19
        // 11869: aload           22
        // 11871: astore          23
        // 11873: aload_2        
        // 11874: astore          21
        // 11876: aload_1        
        // 11877: astore          22
        // 11879: aload           27
        // 11881: astore          26
        // 11883: aload           24
        // 11885: astore_2       
        // 11886: aload           21
        // 11888: astore_1       
        // 11889: aload           22
        // 11891: astore          21
        // 11893: aload           19
        // 11895: astore          22
        // 11897: goto            11956
        // 11900: aload_1        
        // 11901: astore          27
        // 11903: aload           23
        // 11905: astore_1       
        // 11906: aload           24
        // 11908: astore          23
        // 11910: aload_2        
        // 11911: astore          24
        // 11913: aload           23
        // 11915: astore_2       
        // 11916: aload           24
        // 11918: astore          23
        // 11920: goto            12215
        // 11923: astore          19
        // 11925: aload           22
        // 11927: astore          23
        // 11929: aload_1        
        // 11930: astore          22
        // 11932: aload           19
        // 11934: astore_1       
        // 11935: goto            4710
        // 11938: astore          19
        // 11940: aload           22
        // 11942: astore          23
        // 11944: aload           21
        // 11946: astore          22
        // 11948: aload_1        
        // 11949: astore          21
        // 11951: aload_2        
        // 11952: astore_1       
        // 11953: aload           19
        // 11955: astore_2       
        // 11956: aload           25
        // 11958: astore          19
        // 11960: aload           20
        // 11962: astore          24
        // 11964: aload_1        
        // 11965: astore          20
        // 11967: aload           21
        // 11969: astore_1       
        // 11970: aload           24
        // 11972: astore          21
        // 11974: goto            11209
        // 11977: aload           20
        // 11979: astore          31
        // 11981: aload_2        
        // 11982: astore          27
        // 11984: aload           25
        // 11986: astore_2       
        // 11987: aload           19
        // 11989: astore          20
        // 11991: aload           27
        // 11993: astore          19
        // 11995: aload           22
        // 11997: astore          25
        // 11999: iload           16
        // 12001: istore          17
        // 12003: goto            11246
        // 12006: goto            5500
        // 12009: goto            6172
        // 12012: aload           21
        // 12014: iload           12
        // 12016: aload           20
        // 12018: aastore        
        // 12019: aload           23
        // 12021: astore          20
        // 12023: aload           24
        // 12025: astore          23
        // 12027: aload           30
        // 12029: astore          24
        // 12031: goto            12187
        // 12034: iconst_0       
        // 12035: istore          11
        // 12037: goto            12067
        // 12040: iconst_1       
        // 12041: istore          11
        // 12043: goto            12067
        // 12046: iconst_2       
        // 12047: istore          11
        // 12049: goto            12067
        // 12052: iconst_3       
        // 12053: istore          11
        // 12055: goto            12067
        // 12058: iconst_4       
        // 12059: istore          11
        // 12061: goto            12067
        // 12064: iconst_5       
        // 12065: istore          11
        // 12067: aload_1        
        // 12068: astore          27
        // 12070: aload           19
        // 12072: astore_1       
        // 12073: aload           24
        // 12075: astore          19
        // 12077: aload           23
        // 12079: astore          28
        // 12081: aload           20
        // 12083: astore          24
        // 12085: aload           27
        // 12087: astore          23
        // 12089: aload_1        
        // 12090: astore          20
        // 12092: aload           28
        // 12094: astore_1       
        // 12095: aload           25
        // 12097: astore          27
        // 12099: aload           24
        // 12101: astore          25
        // 12103: aload           27
        // 12105: astore          24
        // 12107: aload           25
        // 12109: astore          27
        // 12111: aload           24
        // 12113: astore          25
        // 12115: aload           23
        // 12117: astore          28
        // 12119: aload_1        
        // 12120: astore          23
        // 12122: aload           19
        // 12124: astore          24
        // 12126: aload           20
        // 12128: astore          19
        // 12130: aload           28
        // 12132: astore_1       
        // 12133: aload           27
        // 12135: astore          20
        // 12137: goto            394
        // 12140: aload           49
        // 12142: iconst_1       
        // 12143: aaload         
        // 12144: astore          22
        // 12146: goto            12164
        // 12149: aload           49
        // 12151: iconst_2       
        // 12152: aaload         
        // 12153: astore          22
        // 12155: goto            12164
        // 12158: aload           49
        // 12160: iconst_3       
        // 12161: aaload         
        // 12162: astore          22
        // 12164: aload_2        
        // 12165: astore          28
        // 12167: aload_1        
        // 12168: astore          27
        // 12170: aload           23
        // 12172: astore_1       
        // 12173: aload           24
        // 12175: astore_2       
        // 12176: aload           28
        // 12178: astore          23
        // 12180: aload           21
        // 12182: astore          24
        // 12184: goto            12211
        // 12187: aload_1        
        // 12188: astore          27
        // 12190: aload           20
        // 12192: astore_1       
        // 12193: aload           23
        // 12195: astore          28
        // 12197: aload           19
        // 12199: astore          23
        // 12201: aload_2        
        // 12202: astore          20
        // 12204: aload           21
        // 12206: astore          19
        // 12208: aload           28
        // 12210: astore_2       
        // 12211: aload           24
        // 12213: astore          21
        // 12215: aload           23
        // 12217: astore          28
        // 12219: aload_1        
        // 12220: astore          23
        // 12222: aload_2        
        // 12223: astore          24
        // 12225: aload           27
        // 12227: astore_1       
        // 12228: aload           28
        // 12230: astore_2       
        // 12231: goto            394
        // 12234: aload_2        
        // 12235: astore          28
        // 12237: aload           21
        // 12239: astore          27
        // 12241: aload           19
        // 12243: astore          21
        // 12245: iload           12
        // 12247: iconst_1       
        // 12248: iadd           
        // 12249: istore          12
        // 12251: aload           21
        // 12253: iload           12
        // 12255: aload           28
        // 12257: aastore        
        // 12258: aload           23
        // 12260: iload           12
        // 12262: dconst_0       
        // 12263: dastore        
        // 12264: aload           20
        // 12266: astore_2       
        // 12267: aload           28
        // 12269: astore          19
        // 12271: aload           23
        // 12273: astore          20
        // 12275: aload           24
        // 12277: astore          23
        // 12279: aload           27
        // 12281: astore          24
        // 12283: goto            12187
        // 12286: aload_2        
        // 12287: astore          28
        // 12289: aload           21
        // 12291: astore          27
        // 12293: aload           19
        // 12295: astore          21
        // 12297: iload           12
        // 12299: iconst_1       
        // 12300: iadd           
        // 12301: istore          12
        // 12303: aload           21
        // 12305: iload           12
        // 12307: aload           28
        // 12309: aastore        
        // 12310: aload           23
        // 12312: iload           12
        // 12314: dconst_1       
        // 12315: dastore        
        // 12316: aload           20
        // 12318: astore_2       
        // 12319: aload           28
        // 12321: astore          19
        // 12323: aload           23
        // 12325: astore          20
        // 12327: aload           24
        // 12329: astore          23
        // 12331: aload           27
        // 12333: astore          24
        // 12335: goto            12187
        // 12338: iload           12
        // 12340: iconst_1       
        // 12341: isub           
        // 12342: istore          13
        // 12344: goto            7220
        // 12347: astore_2       
        // 12348: aload           28
        // 12350: astore          21
        // 12352: aload           27
        // 12354: astore          20
        // 12356: aload           20
        // 12358: astore          19
        // 12360: aload           21
        // 12362: astore          20
        // 12364: aload           22
        // 12366: astore          23
        // 12368: aload           19
        // 12370: astore          21
        // 12372: aload           29
        // 12374: astore          22
        // 12376: aload           25
        // 12378: astore          19
        // 12380: goto            11209
        // 12383: goto            7428
        // 12386: astore          22
        // 12388: aload           20
        // 12390: astore_2       
        // 12391: aload_1        
        // 12392: astore          26
        // 12394: aload           19
        // 12396: astore_1       
        // 12397: aload           30
        // 12399: astore          19
        // 12401: aload           21
        // 12403: astore          20
        // 12405: aload_2        
        // 12406: astore          21
        // 12408: aload           31
        // 12410: astore          23
        // 12412: goto            3193
        // 12415: aload           25
        // 12417: astore          22
        // 12419: aload           29
        // 12421: astore_2       
        // 12422: iload           17
        // 12424: istore          16
        // 12426: aload           31
        // 12428: astore          20
        // 12430: iload           14
        // 12432: istore          12
        // 12434: aload           23
        // 12436: astore          25
        // 12438: aload           30
        // 12440: astore          23
        // 12442: aload           28
        // 12444: astore          21
        // 12446: aload           19
        // 12448: astore          27
        // 12450: aload_1        
        // 12451: astore          19
        // 12453: aload_2        
        // 12454: astore_1       
        // 12455: aload           27
        // 12457: astore_2       
        // 12458: goto            394
        // 12461: astore          24
        // 12463: aload           36
        // 12465: astore          26
        // 12467: aload           33
        // 12469: astore_1       
        // 12470: aload           27
        // 12472: astore          19
        // 12474: aload           20
        // 12476: astore          23
        // 12478: aload_2        
        // 12479: astore          20
        // 12481: aload           22
        // 12483: astore          25
        // 12485: aload           24
        // 12487: astore_2       
        // 12488: aload           21
        // 12490: astore          22
        // 12492: aload           23
        // 12494: astore          21
        // 12496: aload           25
        // 12498: astore          23
        // 12500: goto            10498
        // 12503: goto            10341
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                        
        //  -----  -----  -----  -----  ----------------------------
        //  198    213    11167  11187  Ljava/lang/Throwable;
        //  221    228    11167  11187  Ljava/lang/Throwable;
        //  236    242    11167  11187  Ljava/lang/Throwable;
        //  274    282    11167  11187  Ljava/lang/Throwable;
        //  290    294    11167  11187  Ljava/lang/Throwable;
        //  306    384    10456  10498  Ljava/lang/Throwable;
        //  394    408    10421  10456  Ljava/lang/Throwable;
        //  1044   1111   1111   1128   Ljava/lang/Throwable;
        //  1128   1144   2669   2700   Ljava/lang/Throwable;
        //  1198   1208   11385  11421  Ljava/lang/Throwable;
        //  1212   1230   11385  11421  Ljava/lang/Throwable;
        //  1237   1252   2669   2700   Ljava/lang/Throwable;
        //  1255   1270   2669   2700   Ljava/lang/Throwable;
        //  1290   1302   2669   2700   Ljava/lang/Throwable;
        //  1322   1334   2669   2700   Ljava/lang/Throwable;
        //  1358   1368   2669   2700   Ljava/lang/Throwable;
        //  1368   1380   2669   2700   Ljava/lang/Throwable;
        //  1404   1414   11385  11421  Ljava/lang/Throwable;
        //  1418   1436   11385  11421  Ljava/lang/Throwable;
        //  1447   1457   2669   2700   Ljava/lang/Throwable;
        //  1461   1473   11385  11421  Ljava/lang/Throwable;
        //  1501   1511   11385  11421  Ljava/lang/Throwable;
        //  1521   1545   11385  11421  Ljava/lang/Throwable;
        //  1556   1566   2669   2700   Ljava/lang/Throwable;
        //  1570   1582   11385  11421  Ljava/lang/Throwable;
        //  1601   1610   2669   2700   Ljava/lang/Throwable;
        //  1617   1626   2669   2700   Ljava/lang/Throwable;
        //  1650   1657   11385  11421  Ljava/lang/Throwable;
        //  1668   1677   11385  11421  Ljava/lang/Throwable;
        //  1719   1729   11385  11421  Ljava/lang/Throwable;
        //  1739   1749   11385  11421  Ljava/lang/Throwable;
        //  1766   1785   11385  11421  Ljava/lang/Throwable;
        //  1809   1836   2669   2700   Ljava/lang/Throwable;
        //  1842   1854   2669   2700   Ljava/lang/Throwable;
        //  1865   1875   2669   2700   Ljava/lang/Throwable;
        //  1875   1908   2669   2700   Ljava/lang/Throwable;
        //  1929   1938   2669   2700   Ljava/lang/Throwable;
        //  1961   1976   2669   2700   Ljava/lang/Throwable;
        //  1979   1985   2669   2700   Ljava/lang/Throwable;
        //  2059   2069   11385  11421  Ljava/lang/Throwable;
        //  2073   2085   11385  11421  Ljava/lang/Throwable;
        //  2089   2110   11385  11421  Ljava/lang/Throwable;
        //  2127   2143   2669   2700   Ljava/lang/Throwable;
        //  2149   2162   2669   2700   Ljava/lang/Throwable;
        //  2172   2189   11385  11421  Ljava/lang/Throwable;
        //  2206   2217   2669   2700   Ljava/lang/Throwable;
        //  2230   2243   2669   2700   Ljava/lang/Throwable;
        //  2256   2264   2669   2700   Ljava/lang/Throwable;
        //  2273   2281   2669   2700   Ljava/lang/Throwable;
        //  2290   2299   2669   2700   Ljava/lang/Throwable;
        //  2314   2329   2669   2700   Ljava/lang/Throwable;
        //  2338   2354   2669   2700   Ljava/lang/Throwable;
        //  2357   2371   2669   2700   Ljava/lang/Throwable;
        //  2374   2388   2669   2700   Ljava/lang/Throwable;
        //  2416   2426   11385  11421  Ljava/lang/Throwable;
        //  2453   2463   11385  11421  Ljava/lang/Throwable;
        //  2467   2487   11385  11421  Ljava/lang/Throwable;
        //  2515   2525   11385  11421  Ljava/lang/Throwable;
        //  2529   2547   11385  11421  Ljava/lang/Throwable;
        //  2575   2585   11385  11421  Ljava/lang/Throwable;
        //  2589   2607   11385  11421  Ljava/lang/Throwable;
        //  2635   2645   11385  11421  Ljava/lang/Throwable;
        //  2652   2662   11385  11421  Ljava/lang/Throwable;
        //  2727   2740   2669   2700   Ljava/lang/Throwable;
        //  2757   2764   3239   3273   Ljava/lang/Throwable;
        //  2769   2823   2950   2984   Ljava/lang/Throwable;
        //  2823   2848   2917   2950   Ljava/lang/Throwable;
        //  2855   2869   2902   2917   Ljava/lang/Throwable;
        //  2984   2991   3206   3239   Ljava/lang/Throwable;
        //  3006   3016   3166   3174   Ljava/lang/Throwable;
        //  3025   3030   3030   3041   Ljava/lang/Throwable;
        //  3044   3084   3166   3174   Ljava/lang/Throwable;
        //  3084   3096   3156   3166   Ljava/lang/Throwable;
        //  3103   3136   3156   3166   Ljava/lang/Throwable;
        //  3273   3281   3562   3574   Ljava/lang/Throwable;
        //  3306   3314   3562   3574   Ljava/lang/Throwable;
        //  3333   3349   3562   3574   Ljava/lang/Throwable;
        //  3352   3366   3562   3574   Ljava/lang/Throwable;
        //  3375   3386   3562   3574   Ljava/lang/Throwable;
        //  3392   3412   3562   3574   Ljava/lang/Throwable;
        //  3424   3441   3562   3574   Ljava/lang/Throwable;
        //  3444   3458   3562   3574   Ljava/lang/Throwable;
        //  3471   3482   3562   3574   Ljava/lang/Throwable;
        //  3495   3508   3562   3574   Ljava/lang/Throwable;
        //  3515   3529   3562   3574   Ljava/lang/Throwable;
        //  3637   3647   3650   3673   Ljava/lang/Throwable;
        //  3679   3689   3781   3815   Ljava/lang/Throwable;
        //  3696   3703   3781   3815   Ljava/lang/Throwable;
        //  3715   3730   11544  11575  Ljava/lang/Throwable;
        //  3749   3767   11544  11575  Ljava/lang/Throwable;
        //  3828   3850   4052   4083   Ljava/lang/Throwable;
        //  3870   3892   4052   4083   Ljava/lang/Throwable;
        //  3902   3922   4052   4083   Ljava/lang/Throwable;
        //  3935   3948   4052   4083   Ljava/lang/Throwable;
        //  4005   4015   11544  11575  Ljava/lang/Throwable;
        //  4032   4049   11544  11575  Ljava/lang/Throwable;
        //  4097   4113   4265   4293   Ljava/lang/Throwable;
        //  4130   4140   4265   4293   Ljava/lang/Throwable;
        //  4144   4154   4265   4293   Ljava/lang/Throwable;
        //  4189   4199   4265   4293   Ljava/lang/Throwable;
        //  4203   4215   4265   4293   Ljava/lang/Throwable;
        //  4300   4322   4480   4509   Ljava/lang/Throwable;
        //  4342   4367   4480   4509   Ljava/lang/Throwable;
        //  4371   4383   4480   4509   Ljava/lang/Throwable;
        //  4403   4413   11863  11900  Ljava/lang/Throwable;
        //  4420   4445   11863  11900  Ljava/lang/Throwable;
        //  4449   4459   11863  11900  Ljava/lang/Throwable;
        //  4509   4525   11938  11940  Ljava/lang/Throwable;
        //  4535   4545   11938  11940  Ljava/lang/Throwable;
        //  4549   4574   11923  11938  Ljava/lang/Throwable;
        //  4578   4590   11923  11938  Ljava/lang/Throwable;
        //  4600   4607   11938  11940  Ljava/lang/Throwable;
        //  4618   4631   4698   4710   Ljava/lang/Throwable;
        //  4641   4648   11938  11940  Ljava/lang/Throwable;
        //  4659   4669   4698   4710   Ljava/lang/Throwable;
        //  4738   4752   11938  11940  Ljava/lang/Throwable;
        //  4765   4781   11938  11940  Ljava/lang/Throwable;
        //  4787   4796   11938  11940  Ljava/lang/Throwable;
        //  4820   4830   11923  11938  Ljava/lang/Throwable;
        //  4834   4852   11923  11938  Ljava/lang/Throwable;
        //  4862   4871   11923  11938  Ljava/lang/Throwable;
        //  4909   4921   11923  11938  Ljava/lang/Throwable;
        //  4943   4953   11923  11938  Ljava/lang/Throwable;
        //  4957   4977   11923  11938  Ljava/lang/Throwable;
        //  4981   4990   11923  11938  Ljava/lang/Throwable;
        //  5018   5028   11923  11938  Ljava/lang/Throwable;
        //  5035   5047   11923  11938  Ljava/lang/Throwable;
        //  5057   5066   11923  11938  Ljava/lang/Throwable;
        //  5082   5104   11938  11940  Ljava/lang/Throwable;
        //  5118   5135   11938  11940  Ljava/lang/Throwable;
        //  5184   5197   11938  11940  Ljava/lang/Throwable;
        //  5200   5219   5233   5238   Ljava/lang/Throwable;
        //  5253   5259   12347  12356  Ljava/lang/Throwable;
        //  5285   5297   12347  12356  Ljava/lang/Throwable;
        //  5346   5358   12347  12356  Ljava/lang/Throwable;
        //  5370   5377   12347  12356  Ljava/lang/Throwable;
        //  5425   5435   12347  12356  Ljava/lang/Throwable;
        //  5447   5451   12347  12356  Ljava/lang/Throwable;
        //  5489   5497   12347  12356  Ljava/lang/Throwable;
        //  5512   5519   12347  12356  Ljava/lang/Throwable;
        //  5578   5589   5607   5612   Ljava/lang/Throwable;
        //  5594   5604   5607   5612   Ljava/lang/Throwable;
        //  5638   5648   12347  12356  Ljava/lang/Throwable;
        //  5660   5668   12347  12356  Ljava/lang/Throwable;
        //  5680   5692   12347  12356  Ljava/lang/Throwable;
        //  5704   5718   12347  12356  Ljava/lang/Throwable;
        //  5730   5742   12347  12356  Ljava/lang/Throwable;
        //  5787   5802   12347  12356  Ljava/lang/Throwable;
        //  5814   5824   12347  12356  Ljava/lang/Throwable;
        //  5888   5903   12347  12356  Ljava/lang/Throwable;
        //  5915   5925   12347  12356  Ljava/lang/Throwable;
        //  5984   5993   12347  12356  Ljava/lang/Throwable;
        //  6011   6021   12347  12356  Ljava/lang/Throwable;
        //  6093   6102   12347  12356  Ljava/lang/Throwable;
        //  6128   6140   12347  12356  Ljava/lang/Throwable;
        //  6204   6214   12347  12356  Ljava/lang/Throwable;
        //  6231   6241   12347  12356  Ljava/lang/Throwable;
        //  6259   6289   12347  12356  Ljava/lang/Throwable;
        //  6313   6329   12347  12356  Ljava/lang/Throwable;
        //  6340   6356   12347  12356  Ljava/lang/Throwable;
        //  6373   6383   12347  12356  Ljava/lang/Throwable;
        //  6395   6407   6802   6804   Ljava/lang/Throwable;
        //  6428   6440   12347  12356  Ljava/lang/Throwable;
        //  6444   6456   6802   6804   Ljava/lang/Throwable;
        //  6477   6489   12347  12356  Ljava/lang/Throwable;
        //  6493   6505   6802   6804   Ljava/lang/Throwable;
        //  6535   6552   12347  12356  Ljava/lang/Throwable;
        //  6556   6568   6679   6702   Ljava/lang/Throwable;
        //  6589   6604   12347  12356  Ljava/lang/Throwable;
        //  6608   6620   6679   6702   Ljava/lang/Throwable;
        //  6641   6656   12347  12356  Ljava/lang/Throwable;
        //  6660   6672   6679   6702   Ljava/lang/Throwable;
        //  6728   6735   12347  12356  Ljava/lang/Throwable;
        //  6747   6756   12347  12356  Ljava/lang/Throwable;
        //  6767   6785   6802   6804   Ljava/lang/Throwable;
        //  6879   6886   12347  12356  Ljava/lang/Throwable;
        //  6898   6907   12347  12356  Ljava/lang/Throwable;
        //  6918   6938   6802   6804   Ljava/lang/Throwable;
        //  7038   7047   12347  12356  Ljava/lang/Throwable;
        //  7065   7079   12347  12356  Ljava/lang/Throwable;
        //  7139   7156   12347  12356  Ljava/lang/Throwable;
        //  7180   7193   12347  12356  Ljava/lang/Throwable;
        //  7205   7217   12347  12356  Ljava/lang/Throwable;
        //  7244   7252   12347  12356  Ljava/lang/Throwable;
        //  7264   7276   12347  12356  Ljava/lang/Throwable;
        //  7293   7308   12347  12356  Ljava/lang/Throwable;
        //  7323   7344   12347  12356  Ljava/lang/Throwable;
        //  7388   7398   12347  12356  Ljava/lang/Throwable;
        //  7479   7492   12347  12356  Ljava/lang/Throwable;
        //  7504   7514   8998   9033   Ljava/lang/Throwable;
        //  7514   7526   8959   8998   Ljava/lang/Throwable;
        //  7533   7560   7589   7601   Ljava/lang/Throwable;
        //  7601   7615   8959   8998   Ljava/lang/Throwable;
        //  7620   7630   7589   7601   Ljava/lang/Throwable;
        //  7633   7640   8959   8998   Ljava/lang/Throwable;
        //  7645   7668   7846   7885   Ljava/lang/Throwable;
        //  7671   7681   7846   7885   Ljava/lang/Throwable;
        //  7688   7702   7589   7601   Ljava/lang/Throwable;
        //  7705   7729   7802   7843   Ljava/lang/Throwable;
        //  7736   7750   7753   7768   Ljava/lang/Throwable;
        //  7903   7909   8915   8959   Ljava/lang/Throwable;
        //  7914   7929   8003   8047   Ljava/lang/Throwable;
        //  7934   7941   12386  12397  Ljava/lang/Throwable;
        //  7944   7968   12386  12397  Ljava/lang/Throwable;
        //  8047   8053   8871   8915   Ljava/lang/Throwable;
        //  8058   8071   8329   8331   Ljava/lang/Throwable;
        //  8076   8090   8139   8141   Ljava/lang/Throwable;
        //  8093   8106   8134   8139   Ljava/lang/Throwable;
        //  8186   8233   8324   8329   Ljava/lang/Throwable;
        //  8240   8265   8299   8318   Ljava/lang/Throwable;
        //  8390   8396   8830   8871   Ljava/lang/Throwable;
        //  8401   8446   8561   8606   Ljava/lang/Throwable;
        //  8453   8480   8516   8561   Ljava/lang/Throwable;
        //  8606   8620   8789   8830   Ljava/lang/Throwable;
        //  8620   8627   8789   8830   Ljava/lang/Throwable;
        //  8631   8646   8748   8789   Ljava/lang/Throwable;
        //  8646   8665   8729   8748   Ljava/lang/Throwable;
        //  9033   9039   9193   9209   Ljava/lang/Throwable;
        //  9084   9111   9193   9209   Ljava/lang/Throwable;
        //  9153   9180   9193   9209   Ljava/lang/Throwable;
        //  9296   9306   9309   9322   Ljava/lang/Throwable;
        //  9328   9338   9412   9451   Ljava/lang/Throwable;
        //  9346   9362   9403   9412   Ljava/lang/Throwable;
        //  9454   9470   9480   9482   Ljava/lang/Throwable;
        //  9514   9521   9798   9829   Ljava/lang/Throwable;
        //  9526   9545   9480   9482   Ljava/lang/Throwable;
        //  9549   9555   9403   9412   Ljava/lang/Throwable;
        //  9559   9583   9403   9412   Ljava/lang/Throwable;
        //  9627   9634   9798   9829   Ljava/lang/Throwable;
        //  9657   9668   12461  12474  Ljava/lang/Throwable;
        //  9686   9698   12461  12474  Ljava/lang/Throwable;
        //  9698   9703   9783   9798   Ljava/lang/Throwable;
        //  9852   9857   12461  12474  Ljava/lang/Throwable;
        //  9872   9883   12461  12474  Ljava/lang/Throwable;
        //  9898   9926   12461  12474  Ljava/lang/Throwable;
        //  9964   9971   12461  12474  Ljava/lang/Throwable;
        //  9986   9992   12461  12474  Ljava/lang/Throwable;
        //  9992   9997   10141  10150  Ljava/lang/Throwable;
        //  9997   10010  10106  10108  Ljava/lang/Throwable;
        //  10016  10024  10085  10092  Ljava/lang/Throwable;
        //  10027  10033  10085  10092  Ljava/lang/Throwable;
        //  10035  10042  10085  10092  Ljava/lang/Throwable;
        //  10255  10263  12461  12474  Ljava/lang/Throwable;
        //  10279  10291  12461  12474  Ljava/lang/Throwable;
        //  10390  10410  12461  12474  Ljava/lang/Throwable;
        //  10726  10735  10749  10753  Ljava/lang/RuntimeException;
        //  10726  10735  10738  10749  Ljava/lang/Error;
        //  10821  10834  10855  10869  Ljava/lang/Throwable;
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index: 6084, Size: 6084
        //     at java.util.ArrayList.rangeCheck(Unknown Source)
        //     at java.util.ArrayList.get(Unknown Source)
        //     at com.strobel.decompiler.ast.AstBuilder.convertToAst(AstBuilder.java:3321)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:113)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:210)
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
    
    private static boolean isFrameEnterExitRequired(final CallFrame callFrame) {
        return callFrame.debuggerFrame != null || callFrame.idata.itsNeedsActivation;
    }
    
    private static CallFrame processThrowable(final Context context, final Object o, final CallFrame callFrame, int n, final boolean b) {
        CallFrame callFrame2;
        if (n >= 0) {
            callFrame2 = callFrame;
            if (callFrame.frozen) {
                callFrame2 = callFrame.cloneFrozen();
            }
            final int[] itsExceptionTable = callFrame2.idata.itsExceptionTable;
            callFrame2.pc = itsExceptionTable[n + 2];
            if (b) {
                callFrame2.pcPrevBranch = callFrame2.pc;
            }
            callFrame2.savedStackTop = callFrame2.emptyStackTop;
            final int localShift = callFrame2.localShift;
            final int n2 = itsExceptionTable[n + 5];
            final int localShift2 = callFrame2.localShift;
            n = itsExceptionTable[n + 4];
            callFrame2.scope = (Scriptable)callFrame2.stack[localShift + n2];
            callFrame2.stack[localShift2 + n] = o;
        }
        else {
            final ContinuationJump continuationJump = (ContinuationJump)o;
            if (continuationJump.branchFrame != callFrame) {
                Kit.codeBug();
            }
            if (continuationJump.capturedFrame == null) {
                Kit.codeBug();
            }
            int n3;
            n = (n3 = continuationJump.capturedFrame.frameIndex + 1);
            if (continuationJump.branchFrame != null) {
                n3 = n - continuationJump.branchFrame.frameIndex;
            }
            n = 0;
            CallFrame[] array = null;
            Cloneable cloneable = continuationJump.capturedFrame;
            int n4 = 0;
            int i;
            while (true) {
                i = n;
                if (n4 == n3) {
                    break;
                }
                if (!((CallFrame)cloneable).frozen) {
                    Kit.codeBug();
                }
                int n5 = n;
                CallFrame[] array2 = array;
                if (isFrameEnterExitRequired((CallFrame)cloneable)) {
                    if ((array2 = array) == null) {
                        array2 = new CallFrame[n3 - n4];
                    }
                    array2[n] = (CallFrame)cloneable;
                    n5 = n + 1;
                }
                cloneable = ((CallFrame)cloneable).parentFrame;
                ++n4;
                n = n5;
                array = array2;
            }
            while (i != 0) {
                --i;
                enterFrame(context, array[i], ScriptRuntime.emptyArgs, true);
            }
            callFrame2 = continuationJump.capturedFrame.cloneFrozen();
            setCallResult(callFrame2, continuationJump.result, continuationJump.resultDbl);
        }
        callFrame2.throwable = null;
        return callFrame2;
    }
    
    public static Object restartContinuation(final NativeContinuation nativeContinuation, final Context context, final Scriptable scriptable, final Object[] array) {
        if (!ScriptRuntime.hasTopCall(context)) {
            return ScriptRuntime.doTopCall(nativeContinuation, context, scriptable, null, array);
        }
        Object instance;
        if (array.length == 0) {
            instance = Undefined.instance;
        }
        else {
            instance = array[0];
        }
        if (nativeContinuation.getImplementation() == null) {
            return instance;
        }
        final ContinuationJump continuationJump = new ContinuationJump(nativeContinuation, null);
        continuationJump.result = instance;
        return interpretLoop(context, null, continuationJump);
    }
    
    public static Object resumeGenerator(final Context context, final Scriptable scriptable, final int n, final Object o, final Object o2) {
        final CallFrame callFrame = (CallFrame)o;
        final GeneratorState generatorState = new GeneratorState(n, o2);
        if (n == 2) {
            try {
                return interpretLoop(context, callFrame, generatorState);
            }
            catch (RuntimeException ex) {
                if (ex != o2) {
                    throw ex;
                }
                return Undefined.instance;
            }
        }
        final Object interpretLoop = interpretLoop(context, callFrame, generatorState);
        if (generatorState.returnedException != null) {
            throw generatorState.returnedException;
        }
        return interpretLoop;
    }
    
    private static void setCallResult(final CallFrame callFrame, final Object o, final double n) {
        if (callFrame.savedCallOp == 38) {
            callFrame.stack[callFrame.savedStackTop] = o;
            callFrame.sDbl[callFrame.savedStackTop] = n;
        }
        else if (callFrame.savedCallOp == 30) {
            if (o instanceof Scriptable) {
                callFrame.stack[callFrame.savedStackTop] = o;
            }
        }
        else {
            Kit.codeBug();
        }
        callFrame.savedCallOp = 0;
    }
    
    private static boolean stack_boolean(final CallFrame callFrame, final int n) {
        final Object o = callFrame.stack[n];
        if (o == Boolean.TRUE) {
            return true;
        }
        if (o == Boolean.FALSE) {
            return false;
        }
        if (o == UniqueTag.DOUBLE_MARK) {
            final double n2 = callFrame.sDbl[n];
            return n2 == n2 && n2 != 0.0;
        }
        if (o == null) {
            return false;
        }
        if (o == Undefined.instance) {
            return false;
        }
        if (o instanceof Number) {
            final double doubleValue = ((Number)o).doubleValue();
            return doubleValue == doubleValue && doubleValue != 0.0;
        }
        if (o instanceof Boolean) {
            return (boolean)o;
        }
        return ScriptRuntime.toBoolean(o);
    }
    
    private static double stack_double(final CallFrame callFrame, final int n) {
        final Object o = callFrame.stack[n];
        if (o != UniqueTag.DOUBLE_MARK) {
            return ScriptRuntime.toNumber(o);
        }
        return callFrame.sDbl[n];
    }
    
    private static int stack_int32(final CallFrame callFrame, final int n) {
        final Object o = callFrame.stack[n];
        if (o == UniqueTag.DOUBLE_MARK) {
            return ScriptRuntime.toInt32(callFrame.sDbl[n]);
        }
        return ScriptRuntime.toInt32(o);
    }
    
    private static Object thawGenerator(final CallFrame callFrame, final int n, final GeneratorState generatorState, final int n2) {
        callFrame.frozen = false;
        final int index = getIndex(callFrame.idata.itsICode, callFrame.pc);
        callFrame.pc += 2;
        if (generatorState.operation == 1) {
            return new JavaScriptException(generatorState.value, callFrame.idata.itsSourceFile, index);
        }
        if (generatorState.operation == 2) {
            return generatorState.value;
        }
        if (generatorState.operation != 0) {
            throw Kit.codeBug();
        }
        if (n2 == 72) {
            callFrame.stack[n] = generatorState.value;
        }
        return Scriptable.NOT_FOUND;
    }
    
    @Override
    public void captureStackInfo(final RhinoException ex) {
        final Context currentContext = Context.getCurrentContext();
        if (currentContext != null && currentContext.lastInterpreterFrame != null) {
            CallFrame[] interpreterStackInfo;
            if (currentContext.previousInterpreterInvocations != null && currentContext.previousInterpreterInvocations.size() != 0) {
                int size = currentContext.previousInterpreterInvocations.size();
                if (currentContext.previousInterpreterInvocations.peek() == currentContext.lastInterpreterFrame) {
                    --size;
                }
                interpreterStackInfo = new CallFrame[size + 1];
                currentContext.previousInterpreterInvocations.toArray(interpreterStackInfo);
            }
            else {
                interpreterStackInfo = new CallFrame[] { null };
            }
            interpreterStackInfo[interpreterStackInfo.length - 1] = (CallFrame)currentContext.lastInterpreterFrame;
            int n = 0;
            for (int i = 0; i != interpreterStackInfo.length; ++i) {
                n += interpreterStackInfo[i].frameIndex + 1;
            }
            final int[] interpreterLineData = new int[n];
            int j = interpreterStackInfo.length;
            while (j != 0) {
                --j;
                for (CallFrame parentFrame = interpreterStackInfo[j]; parentFrame != null; parentFrame = parentFrame.parentFrame) {
                    --n;
                    interpreterLineData[n] = parentFrame.pcSourceLineStart;
                }
            }
            if (n != 0) {
                Kit.codeBug();
            }
            ex.interpreterStackInfo = interpreterStackInfo;
            ex.interpreterLineData = interpreterLineData;
            return;
        }
        ex.interpreterStackInfo = null;
        ex.interpreterLineData = null;
    }
    
    @Override
    public Object compile(final CompilerEnvirons compilerEnvirons, final ScriptNode scriptNode, final String s, final boolean b) {
        return this.itsData = new CodeGenerator().compile(compilerEnvirons, scriptNode, s, b);
    }
    
    @Override
    public Function createFunctionObject(final Context context, final Scriptable scriptable, final Object o, final Object o2) {
        if (o != this.itsData) {
            Kit.codeBug();
        }
        return InterpretedFunction.createFunction(context, scriptable, this.itsData, o2);
    }
    
    @Override
    public Script createScriptObject(final Object o, final Object o2) {
        if (o != this.itsData) {
            Kit.codeBug();
        }
        return InterpretedFunction.createScript(this.itsData, o2);
    }
    
    @Override
    public String getPatchedStack(final RhinoException ex, final String s) {
        final StringBuilder sb = new StringBuilder(s.length() + 1000);
        final String systemProperty = SecurityUtilities.getSystemProperty("line.separator");
        final CallFrame[] array = (CallFrame[])ex.interpreterStackInfo;
        final int[] interpreterLineData = ex.interpreterLineData;
        int i = array.length;
        int length = interpreterLineData.length;
        int n = 0;
        while (i != 0) {
            --i;
            final int index = s.indexOf("org.mozilla.javascript.Interpreter.interpretLoop", n);
            if (index < 0) {
                break;
            }
            int j;
            for (j = index + "org.mozilla.javascript.Interpreter.interpretLoop".length(); j != s.length(); ++j) {
                final char char1 = s.charAt(j);
                if (char1 == '\n') {
                    break;
                }
                if (char1 == '\r') {
                    break;
                }
            }
            sb.append(s.substring(n, j));
            for (CallFrame parentFrame = array[i]; parentFrame != null; parentFrame = parentFrame.parentFrame) {
                if (length == 0) {
                    Kit.codeBug();
                }
                --length;
                final InterpreterData idata = parentFrame.idata;
                sb.append(systemProperty);
                sb.append("\tat script");
                if (idata.itsName != null && idata.itsName.length() != 0) {
                    sb.append('.');
                    sb.append(idata.itsName);
                }
                sb.append('(');
                sb.append(idata.itsSourceFile);
                final int n2 = interpreterLineData[length];
                if (n2 >= 0) {
                    sb.append(':');
                    sb.append(getIndex(idata.itsICode, n2));
                }
                sb.append(')');
            }
            n = j;
        }
        sb.append(s.substring(n));
        return sb.toString();
    }
    
    @Override
    public List<String> getScriptStack(final RhinoException ex) {
        final ScriptStackElement[][] scriptStackElements = this.getScriptStackElements(ex);
        final ArrayList list = new ArrayList<String>(scriptStackElements.length);
        final String systemProperty = SecurityUtilities.getSystemProperty("line.separator");
        for (int length = scriptStackElements.length, i = 0; i < length; ++i) {
            final ScriptStackElement[] array = scriptStackElements[i];
            final StringBuilder sb = new StringBuilder();
            for (int length2 = array.length, j = 0; j < length2; ++j) {
                array[j].renderJavaStyle(sb);
                sb.append(systemProperty);
            }
            list.add(sb.toString());
        }
        return (List<String>)list;
    }
    
    public ScriptStackElement[][] getScriptStackElements(final RhinoException ex) {
        if (ex.interpreterStackInfo == null) {
            return null;
        }
        final ArrayList<ScriptStackElement[]> list = new ArrayList<ScriptStackElement[]>();
        final CallFrame[] array = (CallFrame[])ex.interpreterStackInfo;
        final int[] interpreterLineData = ex.interpreterLineData;
        int i = array.length;
        int length = interpreterLineData.length;
        while (i != 0) {
            --i;
            CallFrame parentFrame = array[i];
            final ArrayList<ScriptStackElement> list2 = new ArrayList<ScriptStackElement>();
            while (parentFrame != null) {
                if (length == 0) {
                    Kit.codeBug();
                }
                final int n = length - 1;
                final InterpreterData idata = parentFrame.idata;
                final String itsSourceFile = idata.itsSourceFile;
                final String s = null;
                int index = -1;
                final int n2 = interpreterLineData[n];
                if (n2 >= 0) {
                    index = getIndex(idata.itsICode, n2);
                }
                String itsName = s;
                if (idata.itsName != null) {
                    itsName = s;
                    if (idata.itsName.length() != 0) {
                        itsName = idata.itsName;
                    }
                }
                parentFrame = parentFrame.parentFrame;
                list2.add(new ScriptStackElement(itsSourceFile, itsName, index));
                length = n;
            }
            list.add(list2.toArray(new ScriptStackElement[list2.size()]));
        }
        return list.toArray(new ScriptStackElement[list.size()][]);
    }
    
    @Override
    public String getSourcePositionFromStack(final Context context, final int[] array) {
        final CallFrame callFrame = (CallFrame)context.lastInterpreterFrame;
        final InterpreterData idata = callFrame.idata;
        if (callFrame.pcSourceLineStart >= 0) {
            array[0] = getIndex(idata.itsICode, callFrame.pcSourceLineStart);
        }
        else {
            array[0] = 0;
        }
        return idata.itsSourceFile;
    }
    
    @Override
    public void setEvalScriptFlag(final Script script) {
        ((InterpretedFunction)script).idata.evalScriptFlag = true;
    }
    
    private static class CallFrame implements Cloneable, Serializable
    {
        static final long serialVersionUID = -2843792508994958978L;
        DebugFrame debuggerFrame;
        int emptyStackTop;
        InterpretedFunction fnOrScript;
        int frameIndex;
        boolean frozen;
        InterpreterData idata;
        boolean isContinuationsTopFrame;
        int localShift;
        CallFrame parentFrame;
        int pc;
        int pcPrevBranch;
        int pcSourceLineStart;
        Object result;
        double resultDbl;
        double[] sDbl;
        int savedCallOp;
        int savedStackTop;
        Scriptable scope;
        Object[] stack;
        int[] stackAttributes;
        Scriptable thisObj;
        Object throwable;
        boolean useActivation;
        CallFrame varSource;
        
        CallFrame cloneFrozen() {
            if (!this.frozen) {
                Kit.codeBug();
            }
            try {
                final CallFrame callFrame = (CallFrame)this.clone();
                callFrame.stack = this.stack.clone();
                callFrame.stackAttributes = this.stackAttributes.clone();
                callFrame.sDbl = this.sDbl.clone();
                callFrame.frozen = false;
                return callFrame;
            }
            catch (CloneNotSupportedException ex) {
                throw new IllegalStateException();
            }
        }
    }
    
    private static final class ContinuationJump implements Serializable
    {
        static final long serialVersionUID = 7687739156004308247L;
        CallFrame branchFrame;
        CallFrame capturedFrame;
        Object result;
        double resultDbl;
        
        ContinuationJump(final NativeContinuation nativeContinuation, CallFrame parentFrame) {
            this.capturedFrame = (CallFrame)nativeContinuation.getImplementation();
            if (this.capturedFrame != null && parentFrame != null) {
                final CallFrame capturedFrame = this.capturedFrame;
                CallFrame capturedFrame2 = parentFrame;
                final int n = capturedFrame.frameIndex - capturedFrame2.frameIndex;
                Cloneable parentFrame2 = capturedFrame;
                Cloneable parentFrame3 = capturedFrame2;
                if (n != 0) {
                    CallFrame callFrame = capturedFrame;
                    int n2;
                    if ((n2 = n) < 0) {
                        callFrame = parentFrame;
                        capturedFrame2 = this.capturedFrame;
                        n2 = -n;
                    }
                    int n3;
                    do {
                        parentFrame = callFrame.parentFrame;
                        n3 = n2 - 1;
                        callFrame = parentFrame;
                    } while ((n2 = n3) != 0);
                    parentFrame2 = parentFrame;
                    parentFrame3 = capturedFrame2;
                    if (parentFrame.frameIndex != capturedFrame2.frameIndex) {
                        Kit.codeBug();
                        parentFrame3 = capturedFrame2;
                        parentFrame2 = parentFrame;
                    }
                }
                while (parentFrame2 != parentFrame3 && parentFrame2 != null) {
                    parentFrame2 = ((CallFrame)parentFrame2).parentFrame;
                    parentFrame3 = ((CallFrame)parentFrame3).parentFrame;
                }
                this.branchFrame = (CallFrame)parentFrame2;
                if (this.branchFrame != null && !this.branchFrame.frozen) {
                    Kit.codeBug();
                }
            }
            else {
                this.branchFrame = null;
            }
        }
    }
    
    static class GeneratorState
    {
        int operation;
        RuntimeException returnedException;
        Object value;
        
        GeneratorState(final int operation, final Object value) {
            this.operation = operation;
            this.value = value;
        }
    }
}
