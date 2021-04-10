package com.android.dx.dex.code;

import java.util.*;
import com.android.dx.rop.type.*;
import com.android.dx.rop.cst.*;

public final class DalvCode
{
    private CatchTable catches;
    private DalvInsnList insns;
    private LocalList locals;
    private final int positionInfo;
    private PositionList positions;
    private CatchBuilder unprocessedCatches;
    private OutputFinisher unprocessedInsns;
    
    public DalvCode(final int positionInfo, final OutputFinisher unprocessedInsns, final CatchBuilder unprocessedCatches) {
        if (unprocessedInsns == null) {
            throw new NullPointerException("unprocessedInsns == null");
        }
        if (unprocessedCatches == null) {
            throw new NullPointerException("unprocessedCatches == null");
        }
        this.positionInfo = positionInfo;
        this.unprocessedInsns = unprocessedInsns;
        this.unprocessedCatches = unprocessedCatches;
        this.catches = null;
        this.positions = null;
        this.locals = null;
        this.insns = null;
    }
    
    private void finishProcessingIfNecessary() {
        if (this.insns != null) {
            return;
        }
        this.insns = this.unprocessedInsns.finishProcessingAndGetList();
        this.positions = PositionList.make(this.insns, this.positionInfo);
        this.locals = LocalList.make(this.insns);
        this.catches = this.unprocessedCatches.build();
        this.unprocessedInsns = null;
        this.unprocessedCatches = null;
    }
    
    public void assignIndices(final AssignIndicesCallback assignIndicesCallback) {
        this.unprocessedInsns.assignIndices(assignIndicesCallback);
    }
    
    public HashSet<Type> getCatchTypes() {
        return this.unprocessedCatches.getCatchTypes();
    }
    
    public CatchTable getCatches() {
        this.finishProcessingIfNecessary();
        return this.catches;
    }
    
    public HashSet<Constant> getInsnConstants() {
        return this.unprocessedInsns.getAllConstants();
    }
    
    public DalvInsnList getInsns() {
        this.finishProcessingIfNecessary();
        return this.insns;
    }
    
    public LocalList getLocals() {
        this.finishProcessingIfNecessary();
        return this.locals;
    }
    
    public PositionList getPositions() {
        this.finishProcessingIfNecessary();
        return this.positions;
    }
    
    public boolean hasAnyCatches() {
        return this.unprocessedCatches.hasAnyCatches();
    }
    
    public boolean hasLocals() {
        return this.unprocessedInsns.hasAnyLocalInfo();
    }
    
    public boolean hasPositions() {
        return this.positionInfo != 1 && this.unprocessedInsns.hasAnyPositionInfo();
    }
    
    public interface AssignIndicesCallback
    {
        int getIndex(final Constant p0);
    }
}
