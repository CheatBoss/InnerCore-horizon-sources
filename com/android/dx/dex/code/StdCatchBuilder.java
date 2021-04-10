package com.android.dx.dex.code;

import com.android.dx.rop.code.*;
import com.android.dx.rop.cst.*;
import com.android.dx.util.*;
import com.android.dx.rop.type.*;
import java.util.*;

public final class StdCatchBuilder implements CatchBuilder
{
    private static final int MAX_CATCH_RANGE = 65535;
    private final BlockAddresses addresses;
    private final RopMethod method;
    private final int[] order;
    
    public StdCatchBuilder(final RopMethod method, final int[] order, final BlockAddresses addresses) {
        if (method == null) {
            throw new NullPointerException("method == null");
        }
        if (order == null) {
            throw new NullPointerException("order == null");
        }
        if (addresses == null) {
            throw new NullPointerException("addresses == null");
        }
        this.method = method;
        this.order = order;
        this.addresses = addresses;
    }
    
    public static CatchTable build(final RopMethod ropMethod, final int[] array, final BlockAddresses blockAddresses) {
        final int length = array.length;
        final BasicBlockList blocks = ropMethod.getBlocks();
        final ArrayList list = new ArrayList<CatchTable.Entry>(length);
        CatchHandlerList empty = CatchHandlerList.EMPTY;
        final int n = 0;
        BasicBlock basicBlock = null;
        BasicBlock basicBlock2 = null;
        BasicBlock labelToBlock;
        CatchHandlerList handlers;
        for (int i = 0; i < length; ++i, empty = handlers, basicBlock = labelToBlock) {
            labelToBlock = blocks.labelToBlock(array[i]);
            if (!labelToBlock.canThrow()) {
                handlers = empty;
                labelToBlock = basicBlock;
            }
            else {
                handlers = handlersFor(labelToBlock, blockAddresses);
                if (empty.size() == 0) {
                    basicBlock2 = labelToBlock;
                }
                else if (empty.equals(handlers) && rangeIsValid(basicBlock2, labelToBlock, blockAddresses)) {
                    handlers = empty;
                }
                else {
                    if (empty.size() != 0) {
                        list.add(makeEntry(basicBlock2, basicBlock, empty, blockAddresses));
                    }
                    basicBlock2 = labelToBlock;
                }
            }
        }
        if (empty.size() != 0) {
            list.add(makeEntry(basicBlock2, basicBlock, empty, blockAddresses));
        }
        final int size = list.size();
        if (size == 0) {
            return CatchTable.EMPTY;
        }
        final CatchTable catchTable = new CatchTable(size);
        for (int j = n; j < size; ++j) {
            catchTable.set(j, (CatchTable.Entry)list.get(j));
        }
        catchTable.setImmutable();
        return catchTable;
    }
    
    private static CatchHandlerList handlersFor(final BasicBlock basicBlock, final BlockAddresses blockAddresses) {
        final IntList successors = basicBlock.getSuccessors();
        final int size = successors.size();
        final int primarySuccessor = basicBlock.getPrimarySuccessor();
        final TypeList catches = basicBlock.getLastInsn().getCatches();
        final int size2 = catches.size();
        if (size2 == 0) {
            return CatchHandlerList.EMPTY;
        }
        if ((primarySuccessor == -1 && size != size2) || (primarySuccessor != -1 && (size != size2 + 1 || primarySuccessor != successors.get(size2)))) {
            throw new RuntimeException("shouldn't happen: weird successors list");
        }
        final int n = 0;
        int n2 = 0;
        int n3;
        while (true) {
            n3 = size2;
            if (n2 >= size2) {
                break;
            }
            if (catches.getType(n2).equals(Type.OBJECT)) {
                n3 = n2 + 1;
                break;
            }
            ++n2;
        }
        final CatchHandlerList list = new CatchHandlerList(n3);
        for (int i = n; i < n3; ++i) {
            list.set(i, new CstType(catches.getType(i)), blockAddresses.getStart(successors.get(i)).getAddress());
        }
        list.setImmutable();
        return list;
    }
    
    private static CatchTable.Entry makeEntry(final BasicBlock basicBlock, final BasicBlock basicBlock2, final CatchHandlerList list, final BlockAddresses blockAddresses) {
        return new CatchTable.Entry(blockAddresses.getLast(basicBlock).getAddress(), blockAddresses.getEnd(basicBlock2).getAddress(), list);
    }
    
    private static boolean rangeIsValid(final BasicBlock basicBlock, final BasicBlock basicBlock2, final BlockAddresses blockAddresses) {
        if (basicBlock == null) {
            throw new NullPointerException("start == null");
        }
        if (basicBlock2 == null) {
            throw new NullPointerException("end == null");
        }
        return blockAddresses.getEnd(basicBlock2).getAddress() - blockAddresses.getLast(basicBlock).getAddress() <= 65535;
    }
    
    @Override
    public CatchTable build() {
        return build(this.method, this.order, this.addresses);
    }
    
    @Override
    public HashSet<Type> getCatchTypes() {
        final HashSet<Type> set = new HashSet<Type>(20);
        final BasicBlockList blocks = this.method.getBlocks();
        for (int size = blocks.size(), i = 0; i < size; ++i) {
            final TypeList catches = blocks.get(i).getLastInsn().getCatches();
            for (int size2 = catches.size(), j = 0; j < size2; ++j) {
                set.add(catches.getType(j));
            }
        }
        return set;
    }
    
    @Override
    public boolean hasAnyCatches() {
        final BasicBlockList blocks = this.method.getBlocks();
        for (int size = blocks.size(), i = 0; i < size; ++i) {
            if (blocks.get(i).getLastInsn().getCatches().size() != 0) {
                return true;
            }
        }
        return false;
    }
}
