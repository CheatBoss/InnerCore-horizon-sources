package com.android.dx.dex.code;

import com.android.dx.dex.*;
import com.android.dx.rop.type.*;
import com.android.dx.rop.code.*;
import com.android.dx.rop.cst.*;
import com.android.dex.*;
import java.util.*;
import com.android.dx.ssa.*;

public final class OutputFinisher
{
    private final DexOptions dexOptions;
    private boolean hasAnyLocalInfo;
    private boolean hasAnyPositionInfo;
    private ArrayList<DalvInsn> insns;
    private final int paramSize;
    private int reservedCount;
    private int reservedParameterCount;
    private final int unreservedRegCount;
    
    public OutputFinisher(final DexOptions dexOptions, final int n, final int unreservedRegCount, final int paramSize) {
        this.dexOptions = dexOptions;
        this.unreservedRegCount = unreservedRegCount;
        this.insns = new ArrayList<DalvInsn>(n);
        this.reservedCount = -1;
        this.hasAnyPositionInfo = false;
        this.hasAnyLocalInfo = false;
        this.paramSize = paramSize;
    }
    
    private static void addConstants(final HashSet<Constant> set, final DalvInsn dalvInsn) {
        if (dalvInsn instanceof CstInsn) {
            set.add(((CstInsn)dalvInsn).getConstant());
            return;
        }
        if (dalvInsn instanceof LocalSnapshot) {
            final RegisterSpecSet locals = ((LocalSnapshot)dalvInsn).getLocals();
            for (int size = locals.size(), i = 0; i < size; ++i) {
                addConstants(set, locals.get(i));
            }
            return;
        }
        if (dalvInsn instanceof LocalStart) {
            addConstants(set, ((LocalStart)dalvInsn).getLocal());
        }
    }
    
    private static void addConstants(final HashSet<Constant> set, final RegisterSpec registerSpec) {
        if (registerSpec == null) {
            return;
        }
        final LocalItem localItem = registerSpec.getLocalItem();
        final CstString name = localItem.getName();
        final CstString signature = localItem.getSignature();
        final Type type = registerSpec.getType();
        if (type != Type.KNOWN_NULL) {
            set.add(CstType.intern(type));
        }
        if (name != null) {
            set.add(name);
        }
        if (signature != null) {
            set.add(signature);
        }
    }
    
    private void addReservedParameters(final int n) {
        this.shiftParameters(n);
        this.reservedParameterCount += n;
    }
    
    private void addReservedRegisters(final int n) {
        this.shiftAllRegisters(n);
        this.reservedCount += n;
    }
    
    private void align64bits(final Dop[] array) {
        while (true) {
            int n = 0;
            int n2 = 0;
            int n3 = 0;
            int n4 = 0;
            final int unreservedRegCount = this.unreservedRegCount;
            final int reservedCount = this.reservedCount;
            final int reservedParameterCount = this.reservedParameterCount;
            final int paramSize = this.paramSize;
            final Iterator<DalvInsn> iterator = this.insns.iterator();
            while (iterator.hasNext()) {
                final RegisterSpecList registers = iterator.next().getRegisters();
                final int n5 = n2;
                final int n6 = n;
                final int n7 = 0;
                int n8 = n4;
                int n9 = n3;
                int n10 = n5;
                int n11 = n6;
                int n12;
                int n13;
                int n14;
                int n15;
                for (int i = n7; i < registers.size(); ++i, n11 = n12, n10 = n13, n9 = n14, n8 = n15) {
                    final RegisterSpec value = registers.get(i);
                    n12 = n11;
                    n13 = n10;
                    n14 = n9;
                    n15 = n8;
                    if (value.isCategory2()) {
                        final boolean b = value.getReg() >= unreservedRegCount + reservedCount + reservedParameterCount - paramSize;
                        if (value.isEvenRegister()) {
                            if (b) {
                                n15 = n8 + 1;
                                n12 = n11;
                                n13 = n10;
                                n14 = n9;
                            }
                            else {
                                n13 = n10 + 1;
                                n12 = n11;
                                n14 = n9;
                                n15 = n8;
                            }
                        }
                        else if (b) {
                            n14 = n9 + 1;
                            n12 = n11;
                            n13 = n10;
                            n15 = n8;
                        }
                        else {
                            n12 = n11 + 1;
                            n15 = n8;
                            n14 = n9;
                            n13 = n10;
                        }
                    }
                }
                final int n16 = n9;
                final int n17 = n8;
                n = n11;
                n2 = n10;
                n3 = n16;
                n4 = n17;
            }
            if (n3 > n4 && n > n2) {
                this.addReservedRegisters(1);
            }
            else if (n3 > n4) {
                this.addReservedParameters(1);
            }
            else {
                if (n <= n2) {
                    return;
                }
                this.addReservedRegisters(1);
                if (this.paramSize != 0 && n4 > n3) {
                    this.addReservedParameters(1);
                }
            }
            if (!this.reserveRegisters(array)) {
                return;
            }
        }
    }
    
    private void assignAddresses() {
        int address = 0;
        for (int size = this.insns.size(), i = 0; i < size; ++i) {
            final DalvInsn dalvInsn = this.insns.get(i);
            dalvInsn.setAddress(address);
            address += dalvInsn.codeSize();
        }
    }
    
    private void assignAddressesAndFixBranches() {
        do {
            this.assignAddresses();
        } while (this.fixBranches());
    }
    
    private static void assignIndices(final CstInsn cstInsn, final DalvCode.AssignIndicesCallback assignIndicesCallback) {
        final Constant constant = cstInsn.getConstant();
        final int index = assignIndicesCallback.getIndex(constant);
        if (index >= 0) {
            cstInsn.setIndex(index);
        }
        if (constant instanceof CstMemberRef) {
            final int index2 = assignIndicesCallback.getIndex(((CstMemberRef)constant).getDefiningClass());
            if (index2 >= 0) {
                cstInsn.setClassIndex(index2);
            }
        }
    }
    
    private int calculateReservedCount(final Dop[] array) {
        final int size = this.insns.size();
        int reservedCount = this.reservedCount;
        for (int i = 0; i < size; ++i) {
            final DalvInsn dalvInsn = this.insns.get(i);
            final Dop dop = array[i];
            final Dop opcodeForInsn = this.findOpcodeForInsn(dalvInsn, dop);
            int n;
            if (opcodeForInsn == null) {
                final int minimumRegisterRequirement = dalvInsn.getMinimumRegisterRequirement(this.findExpandedOpcodeForInsn(dalvInsn).getFormat().compatibleRegs(dalvInsn));
                if (minimumRegisterRequirement > (n = reservedCount)) {
                    n = minimumRegisterRequirement;
                }
            }
            else {
                n = reservedCount;
                if (dop == opcodeForInsn) {
                    continue;
                }
            }
            array[i] = opcodeForInsn;
            reservedCount = n;
        }
        return reservedCount;
    }
    
    private Dop findExpandedOpcodeForInsn(final DalvInsn dalvInsn) {
        final Dop opcodeForInsn = this.findOpcodeForInsn(dalvInsn.getLowRegVersion(), dalvInsn.getOpcode());
        if (opcodeForInsn == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("No expanded opcode for ");
            sb.append(dalvInsn);
            throw new DexException(sb.toString());
        }
        return opcodeForInsn;
    }
    
    private Dop findOpcodeForInsn(final DalvInsn dalvInsn, Dop nextOrNull) {
        while (nextOrNull != null) {
            if (nextOrNull.getFormat().isCompatible(dalvInsn)) {
                if (!this.dexOptions.forceJumbo) {
                    break;
                }
                if (nextOrNull.getOpcode() != 26) {
                    return nextOrNull;
                }
            }
            nextOrNull = Dops.getNextOrNull(nextOrNull, this.dexOptions);
        }
        return nextOrNull;
    }
    
    private boolean fixBranches() {
        int size = this.insns.size();
        boolean b = false;
        int i = 0;
        while (i < size) {
            final DalvInsn dalvInsn = this.insns.get(i);
            Label_0202: {
                if (!(dalvInsn instanceof TargetInsn)) {
                    break Label_0202;
                }
                final Dop opcode = dalvInsn.getOpcode();
                final TargetInsn targetInsn = (TargetInsn)dalvInsn;
                if (opcode.getFormat().branchFits(targetInsn)) {
                    break Label_0202;
                }
                Label_0200: {
                    if (opcode.getFamily() == 40) {
                        final Dop opcodeForInsn = this.findOpcodeForInsn(dalvInsn, opcode);
                        if (opcodeForInsn == null) {
                            throw new UnsupportedOperationException("method too long");
                        }
                        this.insns.set(i, dalvInsn.withOpcode(opcodeForInsn));
                        break Label_0200;
                    }
                    try {
                        final CodeAddress codeAddress = this.insns.get(i + 1);
                        this.insns.set(i, new TargetInsn(Dops.GOTO, targetInsn.getPosition(), RegisterSpecList.EMPTY, targetInsn.getTarget()));
                        this.insns.add(i, targetInsn.withNewTargetAndReversed(codeAddress));
                        ++size;
                        ++i;
                        b = true;
                        ++i;
                        continue;
                    }
                    catch (ClassCastException ex) {
                        throw new IllegalStateException("unpaired TargetInsn");
                    }
                    catch (IndexOutOfBoundsException ex2) {
                        throw new IllegalStateException("unpaired TargetInsn (dangling)");
                    }
                }
            }
            break;
        }
        return b;
    }
    
    private static boolean hasLocalInfo(final DalvInsn dalvInsn) {
        if (dalvInsn instanceof LocalSnapshot) {
            final RegisterSpecSet locals = ((LocalSnapshot)dalvInsn).getLocals();
            for (int size = locals.size(), i = 0; i < size; ++i) {
                if (hasLocalInfo(locals.get(i))) {
                    return true;
                }
            }
            return false;
        }
        return dalvInsn instanceof LocalStart && hasLocalInfo(((LocalStart)dalvInsn).getLocal());
    }
    
    private static boolean hasLocalInfo(final RegisterSpec registerSpec) {
        return registerSpec != null && registerSpec.getLocalItem().getName() != null;
    }
    
    private Dop[] makeOpcodesArray() {
        final int size = this.insns.size();
        final Dop[] array = new Dop[size];
        for (int i = 0; i < size; ++i) {
            array[i] = this.insns.get(i).getOpcode();
        }
        return array;
    }
    
    private void massageInstructions(final Dop[] array) {
        if (this.reservedCount == 0) {
            for (int size = this.insns.size(), i = 0; i < size; ++i) {
                final DalvInsn dalvInsn = this.insns.get(i);
                final Dop opcode = dalvInsn.getOpcode();
                final Dop dop = array[i];
                if (opcode != dop) {
                    this.insns.set(i, dalvInsn.withOpcode(dop));
                }
            }
            return;
        }
        this.insns = this.performExpansion(array);
    }
    
    private ArrayList<DalvInsn> performExpansion(final Dop[] array) {
        final int size = this.insns.size();
        final ArrayList list = new ArrayList<CodeAddress>(size * 2);
        final ArrayList<CodeAddress> list2 = new ArrayList<CodeAddress>();
        for (int i = 0; i < size; ++i) {
            DalvInsn expandedVersion = this.insns.get(i);
            final Dop opcode = expandedVersion.getOpcode();
            Dop expandedOpcodeForInsn = array[i];
            DalvInsn expandedPrefix;
            DalvInsn expandedSuffix;
            if (expandedOpcodeForInsn != null) {
                expandedPrefix = null;
                expandedSuffix = null;
            }
            else {
                expandedOpcodeForInsn = this.findExpandedOpcodeForInsn(expandedVersion);
                final BitSet compatibleRegs = expandedOpcodeForInsn.getFormat().compatibleRegs(expandedVersion);
                expandedPrefix = expandedVersion.expandedPrefix(compatibleRegs);
                expandedSuffix = expandedVersion.expandedSuffix(compatibleRegs);
                expandedVersion = expandedVersion.expandedVersion(compatibleRegs);
            }
            if (expandedVersion instanceof CodeAddress && ((CodeAddress)expandedVersion).getBindsClosely()) {
                list2.add((CodeAddress)expandedVersion);
            }
            else {
                if (expandedPrefix != null) {
                    list.add(expandedPrefix);
                }
                if (!(expandedVersion instanceof ZeroSizeInsn) && list2.size() > 0) {
                    final Iterator<CodeAddress> iterator = list2.iterator();
                    while (iterator.hasNext()) {
                        list.add(iterator.next());
                    }
                    list2.clear();
                }
                DalvInsn withOpcode = expandedVersion;
                if (expandedOpcodeForInsn != opcode) {
                    withOpcode = expandedVersion.withOpcode(expandedOpcodeForInsn);
                }
                list.add((CodeAddress)withOpcode);
                if (expandedSuffix != null) {
                    list.add((CodeAddress)expandedSuffix);
                }
            }
        }
        return (ArrayList<DalvInsn>)list;
    }
    
    private boolean reserveRegisters(final Dop[] array) {
        boolean b = false;
        int reservedCount;
        if (this.reservedCount < 0) {
            reservedCount = 0;
        }
        else {
            reservedCount = this.reservedCount;
        }
        while (true) {
            final int calculateReservedCount = this.calculateReservedCount(array);
            if (reservedCount >= calculateReservedCount) {
                break;
            }
            b = true;
            for (int size = this.insns.size(), i = 0; i < size; ++i) {
                final DalvInsn dalvInsn = this.insns.get(i);
                if (!(dalvInsn instanceof CodeAddress)) {
                    this.insns.set(i, dalvInsn.withRegisterOffset(calculateReservedCount - reservedCount));
                }
            }
            reservedCount = calculateReservedCount;
        }
        this.reservedCount = reservedCount;
        return b;
    }
    
    private void shiftAllRegisters(final int n) {
        for (int size = this.insns.size(), i = 0; i < size; ++i) {
            final DalvInsn dalvInsn = this.insns.get(i);
            if (!(dalvInsn instanceof CodeAddress)) {
                this.insns.set(i, dalvInsn.withRegisterOffset(n));
            }
        }
    }
    
    private void shiftParameters(int i) {
        final int size = this.insns.size();
        final int n = this.unreservedRegCount + this.reservedCount + this.reservedParameterCount;
        final int paramSize = this.paramSize;
        final BasicRegisterMapper basicRegisterMapper = new BasicRegisterMapper(n);
        final int n2 = 0;
        for (int j = 0; j < n; ++j) {
            if (j >= n - paramSize) {
                basicRegisterMapper.addMapping(j, j + i, 1);
            }
            else {
                basicRegisterMapper.addMapping(j, j, 1);
            }
        }
        DalvInsn dalvInsn;
        for (i = n2; i < size; ++i) {
            dalvInsn = this.insns.get(i);
            if (!(dalvInsn instanceof CodeAddress)) {
                this.insns.set(i, dalvInsn.withMapper(basicRegisterMapper));
            }
        }
    }
    
    private void updateInfo(final DalvInsn dalvInsn) {
        if (!this.hasAnyPositionInfo && dalvInsn.getPosition().getLine() >= 0) {
            this.hasAnyPositionInfo = true;
        }
        if (!this.hasAnyLocalInfo && hasLocalInfo(dalvInsn)) {
            this.hasAnyLocalInfo = true;
        }
    }
    
    public void add(final DalvInsn dalvInsn) {
        this.insns.add(dalvInsn);
        this.updateInfo(dalvInsn);
    }
    
    public void assignIndices(final DalvCode.AssignIndicesCallback assignIndicesCallback) {
        for (final DalvInsn dalvInsn : this.insns) {
            if (dalvInsn instanceof CstInsn) {
                assignIndices((CstInsn)dalvInsn, assignIndicesCallback);
            }
        }
    }
    
    public DalvInsnList finishProcessingAndGetList() {
        if (this.reservedCount >= 0) {
            throw new UnsupportedOperationException("already processed");
        }
        final Dop[] opcodesArray = this.makeOpcodesArray();
        this.reserveRegisters(opcodesArray);
        if (this.dexOptions.ALIGN_64BIT_REGS_IN_OUTPUT_FINISHER) {
            this.align64bits(opcodesArray);
        }
        this.massageInstructions(opcodesArray);
        this.assignAddressesAndFixBranches();
        return DalvInsnList.makeImmutable(this.insns, this.reservedCount + this.unreservedRegCount + this.reservedParameterCount);
    }
    
    public HashSet<Constant> getAllConstants() {
        final HashSet<Constant> set = new HashSet<Constant>(20);
        final Iterator<DalvInsn> iterator = this.insns.iterator();
        while (iterator.hasNext()) {
            addConstants(set, iterator.next());
        }
        return set;
    }
    
    public boolean hasAnyLocalInfo() {
        return this.hasAnyLocalInfo;
    }
    
    public boolean hasAnyPositionInfo() {
        return this.hasAnyPositionInfo;
    }
    
    public void insert(final int n, final DalvInsn dalvInsn) {
        this.insns.add(n, dalvInsn);
        this.updateInfo(dalvInsn);
    }
    
    public void reverseBranch(int n, final CodeAddress codeAddress) {
        n = this.insns.size() - n - 1;
        try {
            this.insns.set(n, this.insns.get(n).withNewTargetAndReversed(codeAddress));
        }
        catch (ClassCastException ex) {
            throw new IllegalArgumentException("non-reversible instruction");
        }
        catch (IndexOutOfBoundsException ex2) {
            throw new IllegalArgumentException("too few instructions");
        }
    }
}
