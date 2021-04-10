package com.android.dx.ssa;

import java.util.*;
import java.io.*;
import com.android.dx.util.*;

public class DomFront
{
    private static boolean DEBUG;
    private final DomInfo[] domInfos;
    private final SsaMethod meth;
    private final ArrayList<SsaBasicBlock> nodes;
    
    static {
        DomFront.DEBUG = false;
    }
    
    public DomFront(final SsaMethod meth) {
        this.meth = meth;
        this.nodes = meth.getBlocks();
        final int size = this.nodes.size();
        this.domInfos = new DomInfo[size];
        for (int i = 0; i < size; ++i) {
            this.domInfos[i] = new DomInfo();
        }
    }
    
    private void buildDomTree() {
        for (int size = this.nodes.size(), i = 0; i < size; ++i) {
            final DomInfo domInfo = this.domInfos[i];
            if (domInfo.idom != -1) {
                this.nodes.get(domInfo.idom).addDomChild(this.nodes.get(i));
            }
        }
    }
    
    private void calcDomFronts() {
        for (int size = this.nodes.size(), i = 0; i < size; ++i) {
            final SsaBasicBlock ssaBasicBlock = this.nodes.get(i);
            final DomInfo domInfo = this.domInfos[i];
            final BitSet predecessors = ssaBasicBlock.getPredecessors();
            if (predecessors.cardinality() > 1) {
                for (int j = predecessors.nextSetBit(0); j >= 0; j = predecessors.nextSetBit(j + 1)) {
                    DomInfo domInfo2;
                    for (int k = j; k != domInfo.idom; k = domInfo2.idom) {
                        if (k == -1) {
                            break;
                        }
                        domInfo2 = this.domInfos[k];
                        if (domInfo2.dominanceFrontiers.has(i)) {
                            break;
                        }
                        domInfo2.dominanceFrontiers.add(i);
                    }
                }
            }
        }
    }
    
    private void debugPrintDomChildren() {
        for (int size = this.nodes.size(), i = 0; i < size; ++i) {
            final SsaBasicBlock ssaBasicBlock = this.nodes.get(i);
            final StringBuffer sb = new StringBuffer();
            sb.append('{');
            int n = 0;
            for (final SsaBasicBlock ssaBasicBlock2 : ssaBasicBlock.getDomChildren()) {
                if (n != 0) {
                    sb.append(',');
                }
                sb.append(ssaBasicBlock2);
                n = 1;
            }
            sb.append('}');
            final PrintStream out = System.out;
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("domChildren[");
            sb2.append(ssaBasicBlock);
            sb2.append("]: ");
            sb2.append((Object)sb);
            out.println(sb2.toString());
        }
    }
    
    public DomInfo[] run() {
        final int size = this.nodes.size();
        final boolean debug = DomFront.DEBUG;
        final int n = 0;
        if (debug) {
            for (int i = 0; i < size; ++i) {
                final SsaBasicBlock ssaBasicBlock = this.nodes.get(i);
                final PrintStream out = System.out;
                final StringBuilder sb = new StringBuilder();
                sb.append("pred[");
                sb.append(i);
                sb.append("]: ");
                sb.append(ssaBasicBlock.getPredecessors());
                out.println(sb.toString());
            }
        }
        Dominators.make(this.meth, this.domInfos, false);
        if (DomFront.DEBUG) {
            for (int j = 0; j < size; ++j) {
                final DomInfo domInfo = this.domInfos[j];
                final PrintStream out2 = System.out;
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("idom[");
                sb2.append(j);
                sb2.append("]: ");
                sb2.append(domInfo.idom);
                out2.println(sb2.toString());
            }
        }
        this.buildDomTree();
        if (DomFront.DEBUG) {
            this.debugPrintDomChildren();
        }
        for (int k = 0; k < size; ++k) {
            this.domInfos[k].dominanceFrontiers = SetFactory.makeDomFrontSet(size);
        }
        this.calcDomFronts();
        if (DomFront.DEBUG) {
            for (int l = n; l < size; ++l) {
                final PrintStream out3 = System.out;
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("df[");
                sb3.append(l);
                sb3.append("]: ");
                sb3.append(this.domInfos[l].dominanceFrontiers);
                out3.println(sb3.toString());
            }
        }
        return this.domInfos;
    }
    
    public static class DomInfo
    {
        public IntSet dominanceFrontiers;
        public int idom;
        
        public DomInfo() {
            this.idom = -1;
        }
    }
}
