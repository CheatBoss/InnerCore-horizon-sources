package com.android.dx.cf.code;

import com.android.dex.util.*;
import com.android.dx.rop.type.*;
import com.android.dx.util.*;
import com.android.dx.rop.code.*;
import com.android.dx.rop.cst.*;
import java.util.*;

public class Simulator
{
    private static final String LOCAL_MISMATCH_ERROR = "This is symptomatic of .class transformation tools that ignore local variable information.";
    private final BytecodeArray code;
    private final LocalVariableList localVariables;
    private final Machine machine;
    private final SimVisitor visitor;
    
    public Simulator(final Machine machine, final ConcreteMethod concreteMethod) {
        if (machine == null) {
            throw new NullPointerException("machine == null");
        }
        if (concreteMethod == null) {
            throw new NullPointerException("method == null");
        }
        this.machine = machine;
        this.code = concreteMethod.getCode();
        this.localVariables = concreteMethod.getLocalVariables();
        this.visitor = new SimVisitor();
    }
    
    private static SimException illegalTos() {
        return new SimException("stack mismatch: illegal top-of-stack for opcode");
    }
    
    private static Type requiredArrayTypeFor(final Type type, final Type type2) {
        if (type2 == Type.KNOWN_NULL) {
            return type.getArrayType();
        }
        if (type == Type.OBJECT && type2.isArray() && type2.getComponentType().isReference()) {
            return type2;
        }
        if (type == Type.BYTE && type2 == Type.BOOLEAN_ARRAY) {
            return Type.BOOLEAN_ARRAY;
        }
        return type.getArrayType();
    }
    
    public int simulate(final int n, final Frame frame) {
        this.visitor.setFrame(frame);
        return this.code.parseInstruction(n, (BytecodeArray.Visitor)this.visitor);
    }
    
    public void simulate(final ByteBlock byteBlock, final Frame frame) {
        final int end = byteBlock.getEnd();
        this.visitor.setFrame(frame);
        try {
            int instruction;
            for (int i = byteBlock.getStart(); i < end; i += instruction) {
                instruction = this.code.parseInstruction(i, (BytecodeArray.Visitor)this.visitor);
                this.visitor.setPreviousOffset(i);
            }
        }
        catch (SimException ex) {
            frame.annotate(ex);
            throw ex;
        }
    }
    
    private class SimVisitor implements Visitor
    {
        private Frame frame;
        private final Machine machine;
        private int previousOffset;
        
        public SimVisitor() {
            this.machine = Simulator.this.machine;
            this.frame = null;
        }
        
        private void checkReturnType(final Type type) {
            final Type returnType = this.machine.getPrototype().getReturnType();
            if (!Merger.isPossiblyAssignableFrom(returnType, type)) {
                final StringBuilder sb = new StringBuilder();
                sb.append("return type mismatch: prototype indicates ");
                sb.append(returnType.toHuman());
                sb.append(", but encountered type ");
                sb.append(type.toHuman());
                throw new SimException(sb.toString());
            }
        }
        
        @Override
        public int getPreviousOffset() {
            return this.previousOffset;
        }
        
        public void setFrame(final Frame frame) {
            if (frame == null) {
                throw new NullPointerException("frame == null");
            }
            this.frame = frame;
        }
        
        @Override
        public void setPreviousOffset(final int previousOffset) {
            this.previousOffset = previousOffset;
        }
        
        @Override
        public void visitBranch(final int n, final int n2, final int n3, final int n4) {
            Label_0139: {
                switch (n) {
                    default: {
                        switch (n) {
                            default: {
                                this.visitInvalid(n, n2, n3);
                                return;
                            }
                            case 198:
                            case 199: {
                                this.machine.popArgs(this.frame, Type.OBJECT);
                                break Label_0139;
                            }
                            case 200:
                            case 201: {
                                break Label_0139;
                            }
                        }
                        break;
                    }
                    case 167:
                    case 168: {
                        this.machine.clearArgs();
                        break;
                    }
                    case 165:
                    case 166: {
                        this.machine.popArgs(this.frame, Type.OBJECT, Type.OBJECT);
                        break;
                    }
                    case 159:
                    case 160:
                    case 161:
                    case 162:
                    case 163:
                    case 164: {
                        this.machine.popArgs(this.frame, Type.INT, Type.INT);
                        break;
                    }
                    case 153:
                    case 154:
                    case 155:
                    case 156:
                    case 157:
                    case 158: {
                        this.machine.popArgs(this.frame, Type.INT);
                        break;
                    }
                }
            }
            this.machine.auxTargetArg(n4);
            this.machine.run(this.frame, n2, n);
        }
        
        @Override
        public void visitConstant(final int n, final int n2, final int n3, Constant constant, final int n4) {
            Label_0293: {
                if (n != 189) {
                    if (n != 197) {
                        Constant methodRef = constant;
                        Label_0202: {
                            switch (n) {
                                default: {
                                    switch (n) {
                                        default: {
                                            this.machine.clearArgs();
                                            break Label_0293;
                                        }
                                        case 192:
                                        case 193: {
                                            break Label_0202;
                                        }
                                    }
                                    break;
                                }
                                case 184: {
                                    this.machine.popArgs(this.frame, ((CstMethodRef)constant).getPrototype(true));
                                    break;
                                }
                                case 185: {
                                    methodRef = ((CstInterfaceMethodRef)constant).toMethodRef();
                                }
                                case 182:
                                case 183: {
                                    this.machine.popArgs(this.frame, ((CstMethodRef)methodRef).getPrototype(false));
                                    constant = methodRef;
                                    break;
                                }
                                case 181: {
                                    this.machine.popArgs(this.frame, Type.OBJECT, ((CstFieldRef)constant).getType());
                                    break;
                                }
                                case 180: {
                                    this.machine.popArgs(this.frame, Type.OBJECT);
                                    break;
                                }
                                case 179: {
                                    this.machine.popArgs(this.frame, ((CstFieldRef)constant).getType());
                                    break;
                                }
                            }
                        }
                    }
                    else {
                        this.machine.popArgs(this.frame, Prototype.internInts(Type.VOID, n4));
                    }
                }
                else {
                    this.machine.popArgs(this.frame, Type.INT);
                }
            }
            this.machine.auxIntArg(n4);
            this.machine.auxCstArg(constant);
            this.machine.run(this.frame, n2, n);
        }
        
        @Override
        public void visitInvalid(final int n, final int n2, final int n3) {
            final StringBuilder sb = new StringBuilder();
            sb.append("invalid opcode ");
            sb.append(Hex.u1(n));
            throw new SimException(sb.toString());
        }
        
        @Override
        public void visitLocal(final int n, final int n2, final int n3, final int n4, final Type type, final int n5) {
            int n6;
            if (n == 54) {
                n6 = n2 + n3;
            }
            else {
                n6 = n2;
            }
            final LocalVariableList.Item pcAndIndexToLocal = Simulator.this.localVariables.pcAndIndexToLocal(n6, n4);
            Type type3;
            if (pcAndIndexToLocal != null) {
                final Type type2 = type3 = pcAndIndexToLocal.getType();
                if (type2.getBasicFrameType() != type.getBasicFrameType()) {
                    BaseMachine.throwLocalMismatch(type, type2);
                    return;
                }
            }
            else {
                type3 = type;
            }
            Label_0316: {
                if (n != 21) {
                    final LocalItem localItem = null;
                    LocalItem localItem2 = null;
                    if (n == 54) {
                        LocalItem localItem3;
                        if (pcAndIndexToLocal == null) {
                            localItem3 = localItem;
                        }
                        else {
                            localItem3 = pcAndIndexToLocal.getLocalItem();
                        }
                        this.machine.popArgs(this.frame, type);
                        this.machine.auxType(type);
                        this.machine.localTarget(n4, type3, localItem3);
                        break Label_0316;
                    }
                    if (n == 132) {
                        if (pcAndIndexToLocal != null) {
                            localItem2 = pcAndIndexToLocal.getLocalItem();
                        }
                        this.machine.localArg(this.frame, n4);
                        this.machine.localTarget(n4, type3, localItem2);
                        this.machine.auxType(type);
                        this.machine.auxIntArg(n5);
                        this.machine.auxCstArg(CstInteger.make(n5));
                        break Label_0316;
                    }
                    if (n != 169) {
                        this.visitInvalid(n, n2, n3);
                        return;
                    }
                }
                this.machine.localArg(this.frame, n4);
                this.machine.localInfo(pcAndIndexToLocal != null);
                this.machine.auxType(type);
            }
            this.machine.run(this.frame, n2, n);
        }
        
        @Override
        public void visitNewarray(final int n, final int n2, final CstType cstType, final ArrayList<Constant> list) {
            this.machine.popArgs(this.frame, Type.INT);
            this.machine.auxInitValues(list);
            this.machine.auxCstArg(cstType);
            this.machine.run(this.frame, n, 188);
        }
        
        @Override
        public void visitNoArgs(final int n, final int n2, int n3, Type type) {
            final int n4 = 3;
            Type componentType = null;
            Label_0902: {
                switch (n) {
                    default: {
                        switch (n) {
                            default: {
                                Label_0623: {
                                    switch (n) {
                                        default: {
                                            switch (n) {
                                                default: {
                                                    switch (n) {
                                                        default: {
                                                            this.visitInvalid(n, n2, n3);
                                                            return;
                                                        }
                                                        case 177: {
                                                            this.machine.clearArgs();
                                                            this.checkReturnType(Type.VOID);
                                                            componentType = type;
                                                            break Label_0902;
                                                        }
                                                        case 172: {
                                                            Type peekType = type;
                                                            if (type == Type.OBJECT) {
                                                                peekType = this.frame.getStack().peekType(0);
                                                            }
                                                            this.machine.popArgs(this.frame, type);
                                                            this.checkReturnType(peekType);
                                                            componentType = type;
                                                            break Label_0902;
                                                        }
                                                        case 120:
                                                        case 122:
                                                        case 124: {
                                                            this.machine.popArgs(this.frame, type, Type.INT);
                                                            componentType = type;
                                                            break Label_0902;
                                                        }
                                                        case 116: {
                                                            this.machine.popArgs(this.frame, type);
                                                            componentType = type;
                                                            break Label_0902;
                                                        }
                                                        case 79: {
                                                            final ExecutionStack stack = this.frame.getStack();
                                                            n3 = n4;
                                                            if (type.isCategory1()) {
                                                                n3 = 2;
                                                            }
                                                            final Type peekType2 = stack.peekType(n3);
                                                            final boolean peekLocal = stack.peekLocal(n3);
                                                            final Type access$200 = requiredArrayTypeFor(type, peekType2);
                                                            if (peekLocal) {
                                                                type = access$200.getComponentType();
                                                            }
                                                            this.machine.popArgs(this.frame, access$200, Type.INT, type);
                                                            componentType = type;
                                                            break Label_0902;
                                                        }
                                                        case 46: {
                                                            type = requiredArrayTypeFor(type, this.frame.getStack().peekType(1));
                                                            componentType = type.getComponentType();
                                                            this.machine.popArgs(this.frame, type, Type.INT);
                                                            break Label_0902;
                                                        }
                                                        case 0: {
                                                            this.machine.clearArgs();
                                                            componentType = type;
                                                            break Label_0902;
                                                        }
                                                        case 100:
                                                        case 104:
                                                        case 108:
                                                        case 112:
                                                        case 126:
                                                        case 128:
                                                        case 130: {
                                                            break Label_0902;
                                                        }
                                                    }
                                                    break;
                                                }
                                                case 194:
                                                case 195: {
                                                    break Label_0623;
                                                }
                                            }
                                            break;
                                        }
                                        case 191: {
                                            this.machine.popArgs(this.frame, Type.OBJECT);
                                            componentType = type;
                                            break Label_0902;
                                        }
                                        case 190: {
                                            final Type peekType3 = this.frame.getStack().peekType(0);
                                            if (!peekType3.isArrayOrKnownNull()) {
                                                final StringBuilder sb = new StringBuilder();
                                                sb.append("type mismatch: expected array type but encountered ");
                                                sb.append(peekType3.toHuman());
                                                throw new SimException(sb.toString());
                                            }
                                            this.machine.popArgs(this.frame, Type.OBJECT);
                                            componentType = type;
                                            break Label_0902;
                                        }
                                    }
                                }
                                break;
                            }
                            case 151:
                            case 152: {
                                this.machine.popArgs(this.frame, Type.DOUBLE, Type.DOUBLE);
                                componentType = type;
                                break Label_0902;
                            }
                            case 149:
                            case 150: {
                                this.machine.popArgs(this.frame, Type.FLOAT, Type.FLOAT);
                                componentType = type;
                                break Label_0902;
                            }
                            case 148: {
                                this.machine.popArgs(this.frame, Type.LONG, Type.LONG);
                                componentType = type;
                                break Label_0902;
                            }
                            case 142:
                            case 143:
                            case 144: {
                                this.machine.popArgs(this.frame, Type.DOUBLE);
                                componentType = type;
                                break Label_0902;
                            }
                            case 139:
                            case 140:
                            case 141: {
                                this.machine.popArgs(this.frame, Type.FLOAT);
                                componentType = type;
                                break Label_0902;
                            }
                            case 136:
                            case 137:
                            case 138: {
                                this.machine.popArgs(this.frame, Type.LONG);
                                componentType = type;
                                break Label_0902;
                            }
                            case 133:
                            case 134:
                            case 135:
                            case 145:
                            case 146:
                            case 147: {
                                this.machine.popArgs(this.frame, Type.INT);
                                componentType = type;
                                break Label_0902;
                            }
                        }
                        break;
                    }
                    case 96: {
                        this.machine.popArgs(this.frame, type, type);
                        componentType = type;
                        break;
                    }
                    case 95: {
                        final ExecutionStack stack2 = this.frame.getStack();
                        if (stack2.peekType(0).isCategory1() && stack2.peekType(1).isCategory1()) {
                            this.machine.popArgs(this.frame, 2);
                            this.machine.auxIntArg(18);
                            componentType = type;
                            break;
                        }
                        throw illegalTos();
                    }
                    case 94: {
                        final ExecutionStack stack3 = this.frame.getStack();
                        if (stack3.peekType(0).isCategory2()) {
                            if (stack3.peekType(2).isCategory2()) {
                                this.machine.popArgs(this.frame, 2);
                                this.machine.auxIntArg(530);
                                componentType = type;
                                break;
                            }
                            if (stack3.peekType(3).isCategory1()) {
                                this.machine.popArgs(this.frame, 3);
                                this.machine.auxIntArg(12819);
                                componentType = type;
                                break;
                            }
                            throw illegalTos();
                        }
                        else {
                            if (!stack3.peekType(1).isCategory1()) {
                                throw illegalTos();
                            }
                            if (stack3.peekType(2).isCategory2()) {
                                this.machine.popArgs(this.frame, 3);
                                this.machine.auxIntArg(205106);
                                componentType = type;
                                break;
                            }
                            if (stack3.peekType(3).isCategory1()) {
                                this.machine.popArgs(this.frame, 4);
                                this.machine.auxIntArg(4399427);
                                componentType = type;
                                break;
                            }
                            throw illegalTos();
                        }
                        break;
                    }
                    case 93: {
                        final ExecutionStack stack4 = this.frame.getStack();
                        if (stack4.peekType(0).isCategory2()) {
                            if (stack4.peekType(2).isCategory2()) {
                                throw illegalTos();
                            }
                            this.machine.popArgs(this.frame, 2);
                            this.machine.auxIntArg(530);
                            componentType = type;
                            break;
                        }
                        else {
                            if (!stack4.peekType(1).isCategory2() && !stack4.peekType(2).isCategory2()) {
                                this.machine.popArgs(this.frame, 3);
                                this.machine.auxIntArg(205106);
                                componentType = type;
                                break;
                            }
                            throw illegalTos();
                        }
                        break;
                    }
                    case 91: {
                        final ExecutionStack stack5 = this.frame.getStack();
                        if (stack5.peekType(0).isCategory2()) {
                            throw illegalTos();
                        }
                        if (stack5.peekType(1).isCategory2()) {
                            this.machine.popArgs(this.frame, 2);
                            this.machine.auxIntArg(530);
                            componentType = type;
                            break;
                        }
                        if (stack5.peekType(2).isCategory1()) {
                            this.machine.popArgs(this.frame, 3);
                            this.machine.auxIntArg(12819);
                            componentType = type;
                            break;
                        }
                        throw illegalTos();
                    }
                    case 90: {
                        final ExecutionStack stack6 = this.frame.getStack();
                        if (stack6.peekType(0).isCategory1() && stack6.peekType(1).isCategory1()) {
                            this.machine.popArgs(this.frame, 2);
                            this.machine.auxIntArg(530);
                            componentType = type;
                            break;
                        }
                        throw illegalTos();
                    }
                    case 89: {
                        if (this.frame.getStack().peekType(0).isCategory2()) {
                            throw illegalTos();
                        }
                        this.machine.popArgs(this.frame, 1);
                        this.machine.auxIntArg(17);
                        componentType = type;
                        break;
                    }
                    case 88:
                    case 92: {
                        final ExecutionStack stack7 = this.frame.getStack();
                        if (stack7.peekType(0).isCategory2()) {
                            this.machine.popArgs(this.frame, 1);
                            n3 = 17;
                        }
                        else {
                            if (!stack7.peekType(1).isCategory1()) {
                                throw illegalTos();
                            }
                            this.machine.popArgs(this.frame, 2);
                            n3 = 8481;
                        }
                        componentType = type;
                        if (n == 92) {
                            this.machine.auxIntArg(n3);
                            componentType = type;
                            break;
                        }
                        break;
                    }
                    case 87: {
                        if (this.frame.getStack().peekType(0).isCategory2()) {
                            throw illegalTos();
                        }
                        this.machine.popArgs(this.frame, 1);
                        componentType = type;
                        break;
                    }
                }
            }
            this.machine.auxType(componentType);
            this.machine.run(this.frame, n2, n);
        }
        
        @Override
        public void visitSwitch(final int n, final int n2, final int n3, final SwitchList list, final int n4) {
            this.machine.popArgs(this.frame, Type.INT);
            this.machine.auxIntArg(n4);
            this.machine.auxSwitchArg(list);
            this.machine.run(this.frame, n2, n);
        }
    }
}
