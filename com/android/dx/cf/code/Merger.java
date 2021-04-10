package com.android.dx.cf.code;

import com.android.dx.rop.type.*;
import com.android.dx.util.*;

public final class Merger
{
    private Merger() {
    }
    
    public static boolean isPossiblyAssignableFrom(final TypeBearer typeBearer, final TypeBearer typeBearer2) {
        Type type = typeBearer.getType();
        Type type2 = typeBearer2.getType();
        final boolean equals = type.equals(type2);
        boolean b = true;
        if (equals) {
            return true;
        }
        final int basicType = type.getBasicType();
        final int basicType2 = type2.getBasicType();
        int n;
        if ((n = basicType) == 10) {
            type = Type.OBJECT;
            n = 9;
        }
        int n2;
        if ((n2 = basicType2) == 10) {
            type2 = Type.OBJECT;
            n2 = 9;
        }
        if (n != 9 || n2 != 9) {
            return type.isIntlike() && type2.isIntlike();
        }
        if (type == Type.KNOWN_NULL) {
            return false;
        }
        if (type2 == Type.KNOWN_NULL) {
            return true;
        }
        if (type == Type.OBJECT) {
            return true;
        }
        if (type.isArray()) {
            Type type3 = type2;
            if (!type2.isArray()) {
                return false;
            }
            Type componentType;
            Type componentType2;
            do {
                componentType2 = type.getComponentType();
                componentType = type3.getComponentType();
                if (!componentType2.isArray()) {
                    break;
                }
                type = componentType2;
                type3 = componentType;
            } while (componentType.isArray());
            return isPossiblyAssignableFrom(componentType2, componentType);
        }
        else {
            if (type2.isArray()) {
                if (type != Type.SERIALIZABLE) {
                    if (type == Type.CLONEABLE) {
                        return true;
                    }
                    b = false;
                }
                return b;
            }
            return true;
        }
    }
    
    public static OneLocalsArray mergeLocals(final OneLocalsArray oneLocalsArray, final OneLocalsArray oneLocalsArray2) {
        if (oneLocalsArray == oneLocalsArray2) {
            return oneLocalsArray;
        }
        final int maxLocals = oneLocalsArray.getMaxLocals();
        OneLocalsArray oneLocalsArray3 = null;
        if (oneLocalsArray2.getMaxLocals() != maxLocals) {
            throw new SimException("mismatched maxLocals values");
        }
        OneLocalsArray copy;
        for (int i = 0; i < maxLocals; ++i, oneLocalsArray3 = copy) {
            final TypeBearer orNull = oneLocalsArray.getOrNull(i);
            final TypeBearer mergeType = mergeType(orNull, oneLocalsArray2.getOrNull(i));
            copy = oneLocalsArray3;
            if (mergeType != orNull) {
                if ((copy = oneLocalsArray3) == null) {
                    copy = oneLocalsArray.copy();
                }
                if (mergeType == null) {
                    copy.invalidate(i);
                }
                else {
                    copy.set(i, mergeType);
                }
            }
        }
        if (oneLocalsArray3 == null) {
            return oneLocalsArray;
        }
        oneLocalsArray3.setImmutable();
        return oneLocalsArray3;
    }
    
    public static ExecutionStack mergeStack(final ExecutionStack executionStack, final ExecutionStack executionStack2) {
        if (executionStack == executionStack2) {
            return executionStack;
        }
        final int size = executionStack.size();
        ExecutionStack executionStack3 = null;
        if (executionStack2.size() != size) {
            throw new SimException("mismatched stack depths");
        }
        int n = 0;
    Label_0153_Outer:
        while (true) {
            while (true) {
                Label_0205: {
                    if (n >= size) {
                        break Label_0205;
                    }
                    final TypeBearer peek = executionStack.peek(n);
                    final TypeBearer peek2 = executionStack2.peek(n);
                    final TypeBearer mergeType = mergeType(peek, peek2);
                    ExecutionStack copy = executionStack3;
                    Label_0194: {
                        if (mergeType == peek) {
                            break Label_0194;
                        }
                        if ((copy = executionStack3) == null) {
                            copy = executionStack.copy();
                        }
                        Label_0142: {
                            if (mergeType != null) {
                                break Label_0142;
                            }
                            try {
                                final StringBuilder sb = new StringBuilder();
                                sb.append("incompatible: ");
                                sb.append(peek);
                                sb.append(", ");
                                sb.append(peek2);
                                throw new SimException(sb.toString());
                                // iftrue(Label_0212:, executionStack3 != null)
                                return executionStack;
                                copy.change(n, mergeType);
                                break Label_0194;
                                final StringBuilder sb2 = new StringBuilder();
                                sb2.append("...while merging stack[");
                                sb2.append(Hex.u2(n));
                                sb2.append("]");
                                final SimException ex;
                                ex.addContext(sb2.toString());
                                throw ex;
                                Label_0212: {
                                    executionStack3.setImmutable();
                                }
                                return executionStack3;
                                ++n;
                                executionStack3 = copy;
                                continue Label_0153_Outer;
                            }
                            catch (SimException ex2) {}
                        }
                    }
                }
                final SimException ex2;
                final SimException ex = ex2;
                continue;
            }
        }
    }
    
    public static TypeBearer mergeType(TypeBearer mergeType, final TypeBearer typeBearer) {
        if (mergeType == null) {
            return mergeType;
        }
        if (mergeType.equals(typeBearer)) {
            return mergeType;
        }
        if (typeBearer == null) {
            return null;
        }
        final Type type = mergeType.getType();
        final Type type2 = typeBearer.getType();
        if (type == type2) {
            return type;
        }
        if (type.isReference() && type2.isReference()) {
            if (type == Type.KNOWN_NULL) {
                return type2;
            }
            if (type2 == Type.KNOWN_NULL) {
                return type;
            }
            if (!type.isArray() || !type2.isArray()) {
                return Type.OBJECT;
            }
            mergeType = mergeType(type.getComponentType(), type2.getComponentType());
            if (mergeType == null) {
                return Type.OBJECT;
            }
            return ((Type)mergeType).getArrayType();
        }
        else {
            if (type.isIntlike() && type2.isIntlike()) {
                return Type.INT;
            }
            return null;
        }
    }
}
