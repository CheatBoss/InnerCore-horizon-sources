package com.android.dx.cf.code;

import java.util.*;
import com.android.dx.cf.iface.*;
import com.android.dx.rop.type.*;
import com.android.dx.rop.cst.*;
import com.android.dx.rop.code.*;
import com.android.dx.util.*;

final class RopperMachine extends ValueAwareMachine
{
    private static final CstType ARRAY_REFLECT_TYPE;
    private static final CstMethodRef MULTIANEWARRAY_METHOD;
    private final TranslationAdvice advice;
    private boolean blockCanThrow;
    private TypeList catches;
    private boolean catchesUsed;
    private int extraBlockCount;
    private boolean hasJsr;
    private final ArrayList<Insn> insns;
    private final int maxLocals;
    private final ConcreteMethod method;
    private final MethodList methods;
    private int primarySuccessorIndex;
    private ReturnAddress returnAddress;
    private Rop returnOp;
    private SourcePosition returnPosition;
    private boolean returns;
    private final Ropper ropper;
    
    static {
        ARRAY_REFLECT_TYPE = new CstType(Type.internClassName("java/lang/reflect/Array"));
        MULTIANEWARRAY_METHOD = new CstMethodRef(RopperMachine.ARRAY_REFLECT_TYPE, new CstNat(new CstString("newInstance"), new CstString("(Ljava/lang/Class;[I)Ljava/lang/Object;")));
    }
    
    public RopperMachine(final Ropper ropper, final ConcreteMethod method, final TranslationAdvice advice, final MethodList methods) {
        super(method.getEffectiveDescriptor());
        if (methods == null) {
            throw new NullPointerException("methods == null");
        }
        if (ropper == null) {
            throw new NullPointerException("ropper == null");
        }
        if (advice == null) {
            throw new NullPointerException("advice == null");
        }
        this.ropper = ropper;
        this.method = method;
        this.methods = methods;
        this.advice = advice;
        this.maxLocals = method.getMaxLocals();
        this.insns = new ArrayList<Insn>(25);
        this.catches = null;
        this.catchesUsed = false;
        this.returns = false;
        this.primarySuccessorIndex = -1;
        this.extraBlockCount = 0;
        this.blockCanThrow = false;
        this.returnOp = null;
        this.returnPosition = null;
    }
    
    private RegisterSpecList getSources(final int n, int i) {
        final int argCount = this.argCount();
        if (argCount == 0) {
            return RegisterSpecList.EMPTY;
        }
        final int localIndex = this.getLocalIndex();
        RegisterSpecList list;
        if (localIndex >= 0) {
            list = new RegisterSpecList(1);
            list.set(0, RegisterSpec.make(localIndex, this.arg(0)));
        }
        else {
            list = new RegisterSpecList(argCount);
            final int n2 = 0;
            int n3 = i;
            RegisterSpec make;
            for (i = n2; i < argCount; ++i) {
                make = RegisterSpec.make(n3, this.arg(i));
                list.set(i, make);
                n3 += make.getCategory();
            }
            if (n != 79) {
                if (n == 181) {
                    if (argCount != 2) {
                        throw new RuntimeException("shouldn't happen");
                    }
                    final RegisterSpec value = list.get(0);
                    list.set(0, list.get(1));
                    list.set(1, value);
                }
            }
            else {
                if (argCount != 3) {
                    throw new RuntimeException("shouldn't happen");
                }
                final RegisterSpec value2 = list.get(0);
                final RegisterSpec value3 = list.get(1);
                list.set(0, list.get(2));
                list.set(1, value2);
                list.set(2, value3);
            }
        }
        list.setImmutable();
        return list;
    }
    
    private int jopToRopOpcode(int i, final Constant constant) {
        switch (i) {
            default: {
                switch (i) {
                    default: {
                        switch (i) {
                            default: {
                                switch (i) {
                                    default: {
                                        switch (i) {
                                            default: {
                                                switch (i) {
                                                    default: {
                                                        switch (i) {
                                                            default: {
                                                                switch (i) {
                                                                    default: {
                                                                        throw new RuntimeException("shouldn't happen");
                                                                    }
                                                                    case 130: {
                                                                        return 22;
                                                                    }
                                                                    case 128: {
                                                                        return 21;
                                                                    }
                                                                    case 126: {
                                                                        return 20;
                                                                    }
                                                                    case 124: {
                                                                        return 25;
                                                                    }
                                                                    case 122: {
                                                                        return 24;
                                                                    }
                                                                    case 120: {
                                                                        return 23;
                                                                    }
                                                                    case 116: {
                                                                        return 19;
                                                                    }
                                                                    case 112: {
                                                                        return 18;
                                                                    }
                                                                    case 108: {
                                                                        return 17;
                                                                    }
                                                                    case 104: {
                                                                        return 16;
                                                                    }
                                                                    case 100: {
                                                                        return 15;
                                                                    }
                                                                    case 79: {
                                                                        return 39;
                                                                    }
                                                                    case 46: {
                                                                        return 38;
                                                                    }
                                                                    case 0: {
                                                                        return 1;
                                                                    }
                                                                    case 54: {
                                                                        return 2;
                                                                    }
                                                                    case 18: {
                                                                        return 5;
                                                                    }
                                                                }
                                                                break;
                                                            }
                                                            case 199: {
                                                                return 8;
                                                            }
                                                            case 198: {
                                                                return 7;
                                                            }
                                                            case 197: {
                                                                throw new RuntimeException("shouldn't happen");
                                                            }
                                                        }
                                                        break;
                                                    }
                                                    case 195: {
                                                        return 37;
                                                    }
                                                    case 194: {
                                                        return 36;
                                                    }
                                                    case 193: {
                                                        return 44;
                                                    }
                                                    case 192: {
                                                        return 43;
                                                    }
                                                    case 191: {
                                                        return 35;
                                                    }
                                                    case 190: {
                                                        return 34;
                                                    }
                                                    case 188:
                                                    case 189: {
                                                        return 41;
                                                    }
                                                    case 187: {
                                                        return 40;
                                                    }
                                                }
                                                break;
                                            }
                                            case 185: {
                                                return 53;
                                            }
                                            case 184: {
                                                return 49;
                                            }
                                            case 183: {
                                                final CstMethodRef cstMethodRef = (CstMethodRef)constant;
                                                if (cstMethodRef.isInstanceInit() || cstMethodRef.getDefiningClass().equals(this.method.getDefiningClass())) {
                                                    return 52;
                                                }
                                                if (!this.method.getAccSuper()) {
                                                    return 52;
                                                }
                                                return 51;
                                            }
                                            case 182: {
                                                final CstMethodRef cstMethodRef2 = (CstMethodRef)constant;
                                                if (cstMethodRef2.getDefiningClass().equals(this.method.getDefiningClass())) {
                                                    Method value;
                                                    for (i = 0; i < this.methods.size(); ++i) {
                                                        value = this.methods.get(i);
                                                        if (AccessFlags.isPrivate(value.getAccessFlags()) && cstMethodRef2.getNat().equals(value.getNat())) {
                                                            return 52;
                                                        }
                                                    }
                                                }
                                                return 50;
                                            }
                                            case 181: {
                                                return 47;
                                            }
                                            case 180: {
                                                return 45;
                                            }
                                            case 179: {
                                                return 48;
                                            }
                                            case 178: {
                                                return 46;
                                            }
                                            case 177: {
                                                return 33;
                                            }
                                        }
                                        break;
                                    }
                                    case 172: {
                                        return 33;
                                    }
                                    case 171: {
                                        return 13;
                                    }
                                }
                                break;
                            }
                            case 167: {
                                return 6;
                            }
                            case 158:
                            case 164: {
                                return 11;
                            }
                            case 157:
                            case 163: {
                                return 12;
                            }
                            case 156:
                            case 162: {
                                return 10;
                            }
                            case 155:
                            case 161: {
                                return 9;
                            }
                            case 154:
                            case 160:
                            case 166: {
                                return 8;
                            }
                            case 153:
                            case 159:
                            case 165: {
                                return 7;
                            }
                            case 150:
                            case 152: {
                                return 28;
                            }
                            case 148:
                            case 149:
                            case 151: {
                                return 27;
                            }
                            case 147: {
                                return 32;
                            }
                            case 146: {
                                return 31;
                            }
                            case 145: {
                                return 30;
                            }
                            case 133:
                            case 134:
                            case 135:
                            case 136:
                            case 137:
                            case 138:
                            case 139:
                            case 140:
                            case 141:
                            case 142:
                            case 143:
                            case 144: {
                                return 29;
                            }
                            case 132: {
                                return 14;
                            }
                            case 168:
                            case 169: {
                                throw new RuntimeException("shouldn't happen");
                            }
                        }
                        break;
                    }
                    case 96: {
                        return 14;
                    }
                    case 87:
                    case 88:
                    case 89:
                    case 90:
                    case 91:
                    case 92:
                    case 93:
                    case 94:
                    case 95: {
                        throw new RuntimeException("shouldn't happen");
                    }
                }
                break;
            }
            case 21: {
                return 2;
            }
            case 20: {
                return 5;
            }
        }
    }
    
    private void updateReturnOp(final Rop returnOp, final SourcePosition sourcePosition) {
        if (returnOp == null) {
            throw new NullPointerException("op == null");
        }
        if (sourcePosition == null) {
            throw new NullPointerException("pos == null");
        }
        if (this.returnOp == null) {
            this.returnOp = returnOp;
            this.returnPosition = sourcePosition;
            return;
        }
        if (this.returnOp != returnOp) {
            final StringBuilder sb = new StringBuilder();
            sb.append("return op mismatch: ");
            sb.append(returnOp);
            sb.append(", ");
            sb.append(this.returnOp);
            throw new SimException(sb.toString());
        }
        if (sourcePosition.getLine() > this.returnPosition.getLine()) {
            this.returnPosition = sourcePosition;
        }
    }
    
    public boolean canThrow() {
        return this.blockCanThrow;
    }
    
    public int getExtraBlockCount() {
        return this.extraBlockCount;
    }
    
    public ArrayList<Insn> getInsns() {
        return this.insns;
    }
    
    public int getPrimarySuccessorIndex() {
        return this.primarySuccessorIndex;
    }
    
    public ReturnAddress getReturnAddress() {
        return this.returnAddress;
    }
    
    public Rop getReturnOp() {
        return this.returnOp;
    }
    
    public SourcePosition getReturnPosition() {
        return this.returnPosition;
    }
    
    public boolean hasJsr() {
        return this.hasJsr;
    }
    
    public boolean hasRet() {
        return this.returnAddress != null;
    }
    
    public boolean returns() {
        return this.returns;
    }
    
    @Override
    public void run(final Frame frame, int i, int firstTempStackReg) {
        final int n = firstTempStackReg;
        final int n2 = this.maxLocals + frame.getStack().size();
        final RegisterSpecList sources = this.getSources(n, n2);
        final int size = sources.size();
        super.run(frame, i, firstTempStackReg);
        SourcePosition sourcePosistion;
        final SourcePosition sourcePosition = sourcePosistion = this.method.makeSourcePosistion(i);
        RegisterSpec registerSpec = this.getLocalTarget(n == 54);
        i = this.resultCount();
        if (i == 0) {
            registerSpec = null;
            switch (n) {
                case 87:
                case 88: {
                    return;
                }
            }
        }
        else if (registerSpec == null) {
            if (i != 1) {
                i = 0;
                firstTempStackReg = this.ropper.getFirstTempStackReg();
                final RegisterSpec[] array = new RegisterSpec[size];
                while (i < size) {
                    final RegisterSpec value = sources.get(i);
                    final TypeBearer typeBearer = value.getTypeBearer();
                    final RegisterSpec withReg = value.withReg(firstTempStackReg);
                    this.insns.add(new PlainInsn(Rops.opMove(typeBearer), sourcePosistion, withReg, value));
                    array[i] = withReg;
                    firstTempStackReg += value.getCategory();
                    ++i;
                }
                i = this.getAuxInt();
                firstTempStackReg = n2;
                while (i != 0) {
                    final RegisterSpec registerSpec2 = array[(i & 0xF) - 1];
                    final TypeBearer typeBearer2 = registerSpec2.getTypeBearer();
                    this.insns.add(new PlainInsn(Rops.opMove(typeBearer2), sourcePosistion, registerSpec2.withReg(firstTempStackReg), registerSpec2));
                    firstTempStackReg += typeBearer2.getType().getCategory();
                    i >>= 4;
                }
                return;
            }
            registerSpec = RegisterSpec.make(n2, this.result(0));
        }
        TypeBearer void1;
        if (registerSpec != null) {
            void1 = registerSpec;
        }
        else {
            void1 = Type.VOID;
        }
        final Constant auxCst = this.getAuxCst();
        RegisterSpecList make4;
        if (n == 197) {
            this.blockCanThrow = true;
            this.extraBlockCount = 6;
            final RegisterSpec make = RegisterSpec.make(registerSpec.getNextReg(), Type.INT_ARRAY);
            final Rop opFilledNewArray = Rops.opFilledNewArray(Type.INT_ARRAY, size);
            final TypeList catches = this.catches;
            final CstType int_ARRAY = CstType.INT_ARRAY;
            final RegisterSpec registerSpec3 = registerSpec;
            this.insns.add(new ThrowingCstInsn(opFilledNewArray, sourcePosistion, sources, catches, int_ARRAY));
            this.insns.add(new PlainInsn(Rops.opMoveResult(Type.INT_ARRAY), sourcePosistion, make, RegisterSpecList.EMPTY));
            Type type = ((CstType)auxCst).getClassType();
            for (i = 0; i < size; ++i) {
                type = type.getComponentType();
            }
            final RegisterSpec make2 = RegisterSpec.make(registerSpec3.getReg(), Type.CLASS);
            ThrowingCstInsn throwingCstInsn;
            if (type.isPrimitive()) {
                throwingCstInsn = new ThrowingCstInsn(Rops.GET_STATIC_OBJECT, sourcePosistion, RegisterSpecList.EMPTY, this.catches, CstFieldRef.forPrimitiveType(type));
            }
            else {
                throwingCstInsn = new ThrowingCstInsn(Rops.CONST_OBJECT, sourcePosistion, RegisterSpecList.EMPTY, this.catches, new CstType(type));
            }
            this.insns.add(throwingCstInsn);
            final Rop opMoveResultPseudo = Rops.opMoveResultPseudo(make2.getType());
            final RegisterSpecList empty = RegisterSpecList.EMPTY;
            sourcePosistion = sourcePosition;
            this.insns.add(new PlainInsn(opMoveResultPseudo, sourcePosistion, make2, empty));
            final RegisterSpec make3 = RegisterSpec.make(registerSpec3.getReg(), Type.OBJECT);
            this.insns.add(new ThrowingCstInsn(Rops.opInvokeStatic(RopperMachine.MULTIANEWARRAY_METHOD.getPrototype()), sourcePosistion, RegisterSpecList.make(make2, make), this.catches, RopperMachine.MULTIANEWARRAY_METHOD));
            this.insns.add(new PlainInsn(Rops.opMoveResult(RopperMachine.MULTIANEWARRAY_METHOD.getPrototype().getReturnType()), sourcePosistion, make3, RegisterSpecList.EMPTY));
            firstTempStackReg = 192;
            make4 = RegisterSpecList.make(make3);
        }
        else {
            if (n == 168) {
                this.hasJsr = true;
                return;
            }
            if (n == 169) {
                try {
                    this.returnAddress = (ReturnAddress)this.arg(0);
                    return;
                }
                catch (ClassCastException ex) {
                    throw new RuntimeException("Argument to RET was not a ReturnAddress", ex);
                }
            }
            make4 = sources;
            firstTempStackReg = n;
        }
        final TypeBearer typeBearer3 = void1;
        Constant constant = auxCst;
        final int n3 = i = this.jopToRopOpcode(firstTempStackReg, constant);
        final Rop rop = Rops.ropFor(i, typeBearer3, make4, constant);
        Insn insn = null;
        RegisterSpec registerSpec4 = null;
        Label_0787: {
            PlainInsn plainInsn;
            if (registerSpec != null && rop.isCallLike()) {
                ++this.extraBlockCount;
                plainInsn = new PlainInsn(Rops.opMoveResult(((CstMethodRef)constant).getPrototype().getReturnType()), sourcePosistion, registerSpec, RegisterSpecList.EMPTY);
            }
            else {
                if (registerSpec == null || !rop.canThrow()) {
                    registerSpec4 = registerSpec;
                    break Label_0787;
                }
                ++this.extraBlockCount;
                plainInsn = new PlainInsn(Rops.opMoveResultPseudo(registerSpec.getTypeBearer()), sourcePosistion, registerSpec, RegisterSpecList.EMPTY);
            }
            registerSpec4 = null;
            insn = plainInsn;
        }
        Rop rop2 = null;
        Label_0997: {
            if (i == 41) {
                constant = CstType.intern(rop.getResult());
                rop2 = rop;
            }
            else {
                if (constant == null && size == 2) {
                    final TypeBearer typeBearer4 = make4.get(0).getTypeBearer();
                    final TypeBearer typeBearer5 = make4.get(1).getTypeBearer();
                    if (typeBearer5.isConstant() || typeBearer4.isConstant()) {
                        if (this.advice.hasConstantOperation(rop, make4.get(0), make4.get(1))) {
                            RegisterSpecList list2;
                            if (typeBearer5.isConstant()) {
                                constant = (CstInteger)typeBearer5;
                                final RegisterSpecList list = list2 = make4.withoutLast();
                                if (rop.getOpcode() == 15) {
                                    constant = CstInteger.make(-((CstInteger)typeBearer5).getValue());
                                    i = 14;
                                    list2 = list;
                                }
                            }
                            else {
                                constant = (Constant)typeBearer4;
                                list2 = make4.withoutFirst();
                            }
                            final Rop rop3 = Rops.ropFor(i, typeBearer3, list2, constant);
                            make4 = list2;
                            rop2 = rop3;
                            break Label_0997;
                        }
                    }
                }
                i = n3;
                rop2 = rop;
            }
        }
        final SwitchList auxCases = this.getAuxCases();
        final ArrayList<Constant> initValues = this.getInitValues();
        final boolean canThrow = rop2.canThrow();
        this.blockCanThrow |= canThrow;
        Insn insn2;
        if (auxCases != null) {
            if (auxCases.size() == 0) {
                insn2 = new PlainInsn(Rops.GOTO, sourcePosistion, null, RegisterSpecList.EMPTY);
                this.primarySuccessorIndex = 0;
            }
            else {
                final IntList values = auxCases.getValues();
                insn2 = new SwitchInsn(rop2, sourcePosistion, registerSpec4, make4, values);
                this.primarySuccessorIndex = values.size();
            }
        }
        else if (i == 33) {
            if (make4.size() != 0) {
                final RegisterSpec value2 = make4.get(0);
                final TypeBearer typeBearer6 = value2.getTypeBearer();
                if (value2.getReg() != 0) {
                    this.insns.add(new PlainInsn(Rops.opMove(typeBearer6), sourcePosistion, RegisterSpec.make(0, typeBearer6), value2));
                }
            }
            final SourcePosition sourcePosition2 = sourcePosistion;
            final PlainInsn plainInsn2 = new PlainInsn(Rops.GOTO, sourcePosition2, null, RegisterSpecList.EMPTY);
            this.primarySuccessorIndex = 0;
            this.updateReturnOp(rop2, sourcePosition2);
            this.returns = true;
            insn2 = plainInsn2;
        }
        else {
            final SourcePosition sourcePosition3 = sourcePosistion;
            if (constant != null) {
                if (canThrow) {
                    insn2 = new ThrowingCstInsn(rop2, sourcePosition3, make4, this.catches, constant);
                    this.catchesUsed = true;
                    this.primarySuccessorIndex = this.catches.size();
                }
                else {
                    insn2 = new PlainCstInsn(rop2, sourcePosition3, registerSpec4, make4, constant);
                }
            }
            else if (canThrow) {
                insn2 = new ThrowingInsn(rop2, sourcePosition3, make4, this.catches);
                this.catchesUsed = true;
                if (firstTempStackReg == 191) {
                    this.primarySuccessorIndex = -1;
                }
                else {
                    this.primarySuccessorIndex = this.catches.size();
                }
            }
            else {
                insn2 = new PlainInsn(rop2, sourcePosition3, registerSpec4, make4);
            }
        }
        this.insns.add(insn2);
        if (insn != null) {
            this.insns.add(insn);
        }
        if (initValues != null) {
            ++this.extraBlockCount;
            this.insns.add(new FillArrayDataInsn(Rops.FILL_ARRAY_DATA, sourcePosistion, RegisterSpecList.make(insn.getResult()), initValues, constant));
        }
    }
    
    public void startBlock(final TypeList catches) {
        this.catches = catches;
        this.insns.clear();
        this.catchesUsed = false;
        this.returns = false;
        this.primarySuccessorIndex = 0;
        this.extraBlockCount = 0;
        this.blockCanThrow = false;
        this.hasJsr = false;
        this.returnAddress = null;
    }
    
    public boolean wereCatchesUsed() {
        return this.catchesUsed;
    }
}
