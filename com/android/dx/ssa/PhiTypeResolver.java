package com.android.dx.ssa;

import java.util.*;
import com.android.dx.cf.code.*;
import com.android.dx.rop.type.*;
import com.android.dx.rop.code.*;

public class PhiTypeResolver
{
    SsaMethod ssaMeth;
    private final BitSet worklist;
    
    private PhiTypeResolver(final SsaMethod ssaMeth) {
        this.ssaMeth = ssaMeth;
        this.worklist = new BitSet(ssaMeth.getRegCount());
    }
    
    private static boolean equalsHandlesNulls(final LocalItem localItem, final LocalItem localItem2) {
        return localItem == localItem2 || (localItem != null && localItem.equals(localItem2));
    }
    
    public static void process(final SsaMethod ssaMethod) {
        new PhiTypeResolver(ssaMethod).run();
    }
    
    private void run() {
        for (int regCount = this.ssaMeth.getRegCount(), i = 0; i < regCount; ++i) {
            final SsaInsn definitionForRegister = this.ssaMeth.getDefinitionForRegister(i);
            if (definitionForRegister != null && definitionForRegister.getResult().getBasicType() == 0) {
                this.worklist.set(i);
            }
        }
        while (true) {
            final int nextSetBit = this.worklist.nextSetBit(0);
            if (nextSetBit < 0) {
                break;
            }
            this.worklist.clear(nextSetBit);
            if (!this.resolveResultType((PhiInsn)this.ssaMeth.getDefinitionForRegister(nextSetBit))) {
                continue;
            }
            final List<SsaInsn> useListForRegister = this.ssaMeth.getUseListForRegister(nextSetBit);
            for (int size = useListForRegister.size(), j = 0; j < size; ++j) {
                final SsaInsn ssaInsn = useListForRegister.get(j);
                final RegisterSpec result = ssaInsn.getResult();
                if (result != null && ssaInsn instanceof PhiInsn) {
                    this.worklist.set(result.getReg());
                }
            }
        }
    }
    
    boolean resolveResultType(final PhiInsn phiInsn) {
        phiInsn.updateSourcesToDefinitions(this.ssaMeth);
        final RegisterSpecList sources = phiInsn.getSources();
        final int size = sources.size();
        final int n = 0;
        int n2 = -1;
        RegisterSpec registerSpec = null;
        for (int i = 0; i < size; ++i) {
            final RegisterSpec value = sources.get(i);
            if (value.getBasicType() != 0) {
                registerSpec = value;
                n2 = i;
            }
        }
        if (registerSpec == null) {
            return false;
        }
        LocalItem localItem = registerSpec.getLocalItem();
        TypeBearer typeBearer = registerSpec.getType();
        boolean b = true;
        int n3 = 0;
        while (true) {
            final boolean b2 = true;
            if (n3 >= size) {
                break;
            }
            if (n3 != n2) {
                final RegisterSpec value2 = sources.get(n3);
                if (value2.getBasicType() != 0) {
                    b = (b && equalsHandlesNulls(localItem, value2.getLocalItem()) && b2);
                    typeBearer = Merger.mergeType(typeBearer, value2.getType());
                }
            }
            ++n3;
        }
        if (typeBearer == null) {
            final StringBuilder sb = new StringBuilder();
            for (int j = n; j < size; ++j) {
                sb.append(sources.get(j).toString());
                sb.append(' ');
            }
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Couldn't map types in phi insn:");
            sb2.append((Object)sb);
            throw new RuntimeException(sb2.toString());
        }
        if (!b) {
            localItem = null;
        }
        final RegisterSpec result = phiInsn.getResult();
        if (result.getTypeBearer() == typeBearer && equalsHandlesNulls(localItem, result.getLocalItem())) {
            return false;
        }
        phiInsn.changeResultType(typeBearer, localItem);
        return true;
    }
}
