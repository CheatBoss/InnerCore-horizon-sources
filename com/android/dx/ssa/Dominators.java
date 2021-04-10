package com.android.dx.ssa;

import java.util.*;

public final class Dominators
{
    private final ArrayList<SsaBasicBlock> blocks;
    private final DomFront.DomInfo[] domInfos;
    private final DFSInfo[] info;
    private final SsaMethod meth;
    private final boolean postdom;
    private final ArrayList<SsaBasicBlock> vertex;
    
    private Dominators(final SsaMethod meth, final DomFront.DomInfo[] domInfos, final boolean postdom) {
        this.meth = meth;
        this.domInfos = domInfos;
        this.postdom = postdom;
        this.blocks = meth.getBlocks();
        this.info = new DFSInfo[this.blocks.size() + 2];
        this.vertex = new ArrayList<SsaBasicBlock>();
    }
    
    private void compress(SsaBasicBlock ssaBasicBlock) {
        if (this.info[this.info[ssaBasicBlock.getIndex()].ancestor.getIndex()].ancestor != null) {
            final ArrayList<SsaBasicBlock> list = new ArrayList<SsaBasicBlock>();
            final HashSet<SsaBasicBlock> set = new HashSet<SsaBasicBlock>();
            list.add(ssaBasicBlock);
            while (!list.isEmpty()) {
                final int size = list.size();
                ssaBasicBlock = list.get(size - 1);
                final DFSInfo dfsInfo = this.info[ssaBasicBlock.getIndex()];
                final SsaBasicBlock ancestor = dfsInfo.ancestor;
                final DFSInfo dfsInfo2 = this.info[ancestor.getIndex()];
                if (set.add(ancestor) && dfsInfo2.ancestor != null) {
                    list.add(ancestor);
                }
                else {
                    list.remove(size - 1);
                    if (dfsInfo2.ancestor == null) {
                        continue;
                    }
                    final SsaBasicBlock rep = dfsInfo2.rep;
                    if (this.info[rep.getIndex()].semidom < this.info[dfsInfo.rep.getIndex()].semidom) {
                        dfsInfo.rep = rep;
                    }
                    dfsInfo.ancestor = dfsInfo2.ancestor;
                }
            }
        }
    }
    
    private SsaBasicBlock eval(final SsaBasicBlock ssaBasicBlock) {
        final DFSInfo dfsInfo = this.info[ssaBasicBlock.getIndex()];
        if (dfsInfo.ancestor == null) {
            return ssaBasicBlock;
        }
        this.compress(ssaBasicBlock);
        return dfsInfo.rep;
    }
    
    private BitSet getPreds(final SsaBasicBlock ssaBasicBlock) {
        if (this.postdom) {
            return ssaBasicBlock.getSuccessors();
        }
        return ssaBasicBlock.getPredecessors();
    }
    
    private BitSet getSuccs(final SsaBasicBlock ssaBasicBlock) {
        if (this.postdom) {
            return ssaBasicBlock.getPredecessors();
        }
        return ssaBasicBlock.getSuccessors();
    }
    
    public static Dominators make(final SsaMethod ssaMethod, final DomFront.DomInfo[] array, final boolean b) {
        final Dominators dominators = new Dominators(ssaMethod, array, b);
        dominators.run();
        return dominators;
    }
    
    private void run() {
        SsaBasicBlock ssaBasicBlock;
        if (this.postdom) {
            ssaBasicBlock = this.meth.getExitBlock();
        }
        else {
            ssaBasicBlock = this.meth.getEntryBlock();
        }
        if (ssaBasicBlock != null) {
            this.vertex.add(ssaBasicBlock);
            this.domInfos[ssaBasicBlock.getIndex()].idom = ssaBasicBlock.getIndex();
        }
        this.meth.forEachBlockDepthFirst(this.postdom, new DfsWalker());
        int n2;
        final int n = n2 = this.vertex.size() - 1;
        int n3;
        while (true) {
            n3 = 2;
            if (n2 < 2) {
                break;
            }
            final SsaBasicBlock ssaBasicBlock2 = this.vertex.get(n2);
            final DFSInfo dfsInfo = this.info[ssaBasicBlock2.getIndex()];
            final BitSet preds = this.getPreds(ssaBasicBlock2);
            for (int i = preds.nextSetBit(0); i >= 0; i = preds.nextSetBit(i + 1)) {
                final SsaBasicBlock ssaBasicBlock3 = this.blocks.get(i);
                if (this.info[ssaBasicBlock3.getIndex()] != null) {
                    final int semidom = this.info[this.eval(ssaBasicBlock3).getIndex()].semidom;
                    if (semidom < dfsInfo.semidom) {
                        dfsInfo.semidom = semidom;
                    }
                }
            }
            this.info[this.vertex.get(dfsInfo.semidom).getIndex()].bucket.add(ssaBasicBlock2);
            dfsInfo.ancestor = dfsInfo.parent;
            final ArrayList<SsaBasicBlock> bucket = this.info[dfsInfo.parent.getIndex()].bucket;
            while (!bucket.isEmpty()) {
                final SsaBasicBlock ssaBasicBlock4 = bucket.remove(bucket.size() - 1);
                final SsaBasicBlock eval = this.eval(ssaBasicBlock4);
                if (this.info[eval.getIndex()].semidom < this.info[ssaBasicBlock4.getIndex()].semidom) {
                    this.domInfos[ssaBasicBlock4.getIndex()].idom = eval.getIndex();
                }
                else {
                    this.domInfos[ssaBasicBlock4.getIndex()].idom = dfsInfo.parent.getIndex();
                }
            }
            --n2;
        }
        for (int j = n3; j <= n; ++j) {
            final SsaBasicBlock ssaBasicBlock5 = this.vertex.get(j);
            if (this.domInfos[ssaBasicBlock5.getIndex()].idom != this.vertex.get(this.info[ssaBasicBlock5.getIndex()].semidom).getIndex()) {
                this.domInfos[ssaBasicBlock5.getIndex()].idom = this.domInfos[this.domInfos[ssaBasicBlock5.getIndex()].idom].idom;
            }
        }
    }
    
    private static final class DFSInfo
    {
        public SsaBasicBlock ancestor;
        public ArrayList<SsaBasicBlock> bucket;
        public SsaBasicBlock parent;
        public SsaBasicBlock rep;
        public int semidom;
        
        public DFSInfo() {
            this.bucket = new ArrayList<SsaBasicBlock>();
        }
    }
    
    private class DfsWalker implements Visitor
    {
        private int dfsNum;
        
        private DfsWalker() {
            this.dfsNum = 0;
        }
        
        @Override
        public void visitBlock(final SsaBasicBlock rep, final SsaBasicBlock parent) {
            final DFSInfo dfsInfo = new DFSInfo();
            final int n = this.dfsNum + 1;
            this.dfsNum = n;
            dfsInfo.semidom = n;
            dfsInfo.rep = rep;
            dfsInfo.parent = parent;
            Dominators.this.vertex.add(rep);
            Dominators.this.info[rep.getIndex()] = dfsInfo;
        }
    }
}
