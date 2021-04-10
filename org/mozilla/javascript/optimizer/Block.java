package org.mozilla.javascript.optimizer;

import java.util.*;
import org.mozilla.javascript.ast.*;
import org.mozilla.javascript.*;

class Block
{
    static final boolean DEBUG = false;
    private static int debug_blockCount;
    private int itsBlockID;
    private int itsEndNodeIndex;
    private BitSet itsLiveOnEntrySet;
    private BitSet itsLiveOnExitSet;
    private BitSet itsNotDefSet;
    private Block[] itsPredecessors;
    private int itsStartNodeIndex;
    private Block[] itsSuccessors;
    private BitSet itsUseBeforeDefSet;
    
    Block(final int itsStartNodeIndex, final int itsEndNodeIndex) {
        this.itsStartNodeIndex = itsStartNodeIndex;
        this.itsEndNodeIndex = itsEndNodeIndex;
    }
    
    private static boolean assignType(final int[] array, final int n, int n2) {
        final int n3 = array[n];
        n2 |= array[n];
        array[n] = n2;
        return n3 != n2;
    }
    
    private static Block[] buildBlocks(final Node[] array) {
        final HashMap<Object, FatBlock> hashMap = new HashMap<Object, FatBlock>();
        final ObjArray objArray = new ObjArray();
        final int n = 0;
        int n2 = 0;
        int n3 = 0;
        for (int i = 0; i < array.length; ++i, n2 = n3) {
            final int type = array[i].getType();
            if (type != 131) {
                switch (type) {
                    default: {
                        n3 = n2;
                        break;
                    }
                    case 5:
                    case 6:
                    case 7: {
                        final FatBlock fatBlock = newFatBlock(n2, i);
                        if (array[n2].getType() == 131) {
                            hashMap.put(array[n2], fatBlock);
                        }
                        objArray.add(fatBlock);
                        n3 = i + 1;
                        break;
                    }
                }
            }
            else if (i != (n3 = n2)) {
                final FatBlock fatBlock2 = newFatBlock(n2, i - 1);
                if (array[n2].getType() == 131) {
                    hashMap.put(array[n2], fatBlock2);
                }
                objArray.add(fatBlock2);
                n3 = i;
            }
        }
        if (n2 != array.length) {
            final FatBlock fatBlock3 = newFatBlock(n2, array.length - 1);
            if (array[n2].getType() == 131) {
                hashMap.put(array[n2], fatBlock3);
            }
            objArray.add(fatBlock3);
        }
        for (int j = 0; j < objArray.size(); ++j) {
            final FatBlock fatBlock4 = (FatBlock)objArray.get(j);
            final Node node = array[fatBlock4.realBlock.itsEndNodeIndex];
            final int type2 = node.getType();
            if (type2 != 5 && j < objArray.size() - 1) {
                final FatBlock fatBlock5 = (FatBlock)objArray.get(j + 1);
                fatBlock4.addSuccessor(fatBlock5);
                fatBlock5.addPredecessor(fatBlock4);
            }
            if (type2 == 7 || type2 == 6 || type2 == 5) {
                final Node target = ((Jump)node).target;
                final FatBlock fatBlock6 = hashMap.get(target);
                target.putProp(6, fatBlock6.realBlock);
                fatBlock4.addSuccessor(fatBlock6);
                fatBlock6.addPredecessor(fatBlock4);
            }
        }
        final Block[] array2 = new Block[objArray.size()];
        for (int k = n; k < objArray.size(); ++k) {
            final FatBlock fatBlock7 = (FatBlock)objArray.get(k);
            final Block realBlock = fatBlock7.realBlock;
            realBlock.itsSuccessors = fatBlock7.getSuccessors();
            realBlock.itsPredecessors = fatBlock7.getPredecessors();
            array2[realBlock.itsBlockID = k] = realBlock;
        }
        return array2;
    }
    
    private boolean doReachedUseDataFlow() {
        this.itsLiveOnExitSet.clear();
        if (this.itsSuccessors != null) {
            for (int i = 0; i < this.itsSuccessors.length; ++i) {
                this.itsLiveOnExitSet.or(this.itsSuccessors[i].itsLiveOnEntrySet);
            }
        }
        return this.updateEntrySet(this.itsLiveOnEntrySet, this.itsLiveOnExitSet, this.itsUseBeforeDefSet, this.itsNotDefSet);
    }
    
    private boolean doTypeFlow(final OptFunctionNode optFunctionNode, final Node[] array, final int[] array2) {
        boolean b = false;
        boolean b2;
        for (int i = this.itsStartNodeIndex; i <= this.itsEndNodeIndex; ++i, b = b2) {
            final Node node = array[i];
            b2 = b;
            if (node != null) {
                b2 = (b | findDefPoints(optFunctionNode, node, array2));
            }
        }
        return b;
    }
    
    private static boolean findDefPoints(final OptFunctionNode optFunctionNode, final Node node, final int[] array) {
        final Node firstChild = node.getFirstChild();
        boolean b = false;
        for (Node next = firstChild; next != null; next = next.getNext()) {
            b |= findDefPoints(optFunctionNode, next, array);
        }
        final int type = node.getType();
        boolean b2 = false;
        if (type != 56 && type != 156) {
            switch (type) {
                default: {
                    return b;
                }
                case 106:
                case 107: {
                    b2 = b;
                    if (firstChild.getType() == 55) {
                        final int varIndex = optFunctionNode.getVarIndex(firstChild);
                        boolean b3 = b;
                        if (!optFunctionNode.fnode.getParamAndVarConst()[varIndex]) {
                            b3 = (b | assignType(array, varIndex, 1));
                        }
                        return b3;
                    }
                    break;
                }
            }
        }
        else {
            final int expressionType = findExpressionType(optFunctionNode, firstChild.getNext(), array);
            final int varIndex2 = optFunctionNode.getVarIndex(node);
            if (node.getType() == 56) {
                b2 = b;
                if (optFunctionNode.fnode.getParamAndVarConst()[varIndex2]) {
                    return b2;
                }
            }
            b2 = (b | assignType(array, varIndex2, expressionType));
        }
        return b2;
    }
    
    private static int findExpressionType(final OptFunctionNode optFunctionNode, Node node, final int[] array) {
        final int type = node.getType();
        switch (type) {
            default: {
                switch (type) {
                    default: {
                        switch (type) {
                            default: {
                                switch (type) {
                                    default: {
                                        switch (type) {
                                            default: {
                                                switch (type) {
                                                    default: {
                                                        switch (type) {
                                                            default: {
                                                                switch (type) {
                                                                    default: {
                                                                        switch (type) {
                                                                            default: {
                                                                                return 3;
                                                                            }
                                                                            case 126: {
                                                                                return 3;
                                                                            }
                                                                            case 102: {
                                                                                node = node.getFirstChild().getNext();
                                                                                return findExpressionType(optFunctionNode, node, array) | findExpressionType(optFunctionNode, node.getNext(), array);
                                                                            }
                                                                            case 137: {
                                                                                return 3;
                                                                            }
                                                                            case 89: {
                                                                                return findExpressionType(optFunctionNode, node.getLastChild(), array);
                                                                            }
                                                                        }
                                                                        break;
                                                                    }
                                                                    case 157: {
                                                                        return 3;
                                                                    }
                                                                    case 156: {
                                                                        return findExpressionType(optFunctionNode, node.getLastChild(), array);
                                                                    }
                                                                }
                                                                break;
                                                            }
                                                            case 104:
                                                            case 105: {
                                                                node = node.getFirstChild();
                                                                return findExpressionType(optFunctionNode, node, array) | findExpressionType(optFunctionNode, node.getNext(), array);
                                                            }
                                                            case 106:
                                                            case 107: {
                                                                return 1;
                                                            }
                                                        }
                                                        break;
                                                    }
                                                    case 70: {
                                                        return 3;
                                                    }
                                                    case 69: {
                                                        return 3;
                                                    }
                                                }
                                                break;
                                            }
                                            case 65:
                                            case 66: {
                                                return 3;
                                            }
                                        }
                                        break;
                                    }
                                    case 55: {
                                        return array[optFunctionNode.getVarIndex(node)];
                                    }
                                    case 56: {
                                        return findExpressionType(optFunctionNode, node.getLastChild(), array);
                                    }
                                }
                                break;
                            }
                            case 52:
                            case 53: {
                                return 3;
                            }
                        }
                        break;
                    }
                    case 42:
                    case 48: {
                        return 3;
                    }
                    case 40: {
                        return 1;
                    }
                    case 36:
                    case 39:
                    case 43: {
                        return 3;
                    }
                    case 41: {
                        return 3;
                    }
                    case 38: {
                        return 3;
                    }
                    case 44:
                    case 45:
                    case 46:
                    case 47: {
                        return 3;
                    }
                    case 35:
                    case 37: {
                        return findExpressionType(optFunctionNode, node.getLastChild(), array);
                    }
                }
                break;
            }
            case 33: {
                return 3;
            }
            case 32: {
                return 3;
            }
            case 30: {
                return 3;
            }
            case 21: {
                node = node.getFirstChild();
                return findExpressionType(optFunctionNode, node, array) | findExpressionType(optFunctionNode, node.getNext(), array);
            }
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 26:
            case 31: {
                return 3;
            }
            case 9:
            case 10:
            case 11:
            case 18:
            case 19:
            case 20:
            case 22:
            case 23:
            case 24:
            case 25:
            case 27:
            case 28:
            case 29: {
                return 1;
            }
            case 8: {
                return findExpressionType(optFunctionNode, node.getLastChild(), array);
            }
        }
    }
    
    private void initLiveOnEntrySets(final OptFunctionNode optFunctionNode, final Node[] array) {
        final int varCount = optFunctionNode.getVarCount();
        this.itsUseBeforeDefSet = new BitSet(varCount);
        this.itsNotDefSet = new BitSet(varCount);
        this.itsLiveOnEntrySet = new BitSet(varCount);
        this.itsLiveOnExitSet = new BitSet(varCount);
        for (int i = this.itsStartNodeIndex; i <= this.itsEndNodeIndex; ++i) {
            this.lookForVariableAccess(optFunctionNode, array[i]);
        }
        this.itsNotDefSet.flip(0, varCount);
    }
    
    private void lookForVariableAccess(final OptFunctionNode optFunctionNode, Node node) {
        switch (node.getType()) {
            default: {
                for (node = node.getFirstChild(); node != null; node = node.getNext()) {
                    this.lookForVariableAccess(optFunctionNode, node);
                }
            }
            case 137: {
                final int indexForNameNode = optFunctionNode.fnode.getIndexForNameNode(node);
                if (indexForNameNode > -1 && !this.itsNotDefSet.get(indexForNameNode)) {
                    this.itsUseBeforeDefSet.set(indexForNameNode);
                }
            }
            case 106:
            case 107: {
                node = node.getFirstChild();
                if (node.getType() == 55) {
                    final int varIndex = optFunctionNode.getVarIndex(node);
                    if (!this.itsNotDefSet.get(varIndex)) {
                        this.itsUseBeforeDefSet.set(varIndex);
                    }
                    this.itsNotDefSet.set(varIndex);
                }
                else {
                    this.lookForVariableAccess(optFunctionNode, node);
                }
            }
            case 56:
            case 156: {
                this.lookForVariableAccess(optFunctionNode, node.getFirstChild().getNext());
                this.itsNotDefSet.set(optFunctionNode.getVarIndex(node));
            }
            case 55: {
                final int varIndex2 = optFunctionNode.getVarIndex(node);
                if (!this.itsNotDefSet.get(varIndex2)) {
                    this.itsUseBeforeDefSet.set(varIndex2);
                }
            }
        }
    }
    
    private void markAnyTypeVariables(final int[] array) {
        for (int i = 0; i != array.length; ++i) {
            if (this.itsLiveOnEntrySet.get(i)) {
                assignType(array, i, 3);
            }
        }
    }
    
    private static FatBlock newFatBlock(final int n, final int n2) {
        final FatBlock fatBlock = new FatBlock();
        fatBlock.realBlock = new Block(n, n2);
        return fatBlock;
    }
    
    private void printLiveOnEntrySet(final OptFunctionNode optFunctionNode) {
    }
    
    private static void reachingDefDataFlow(final OptFunctionNode optFunctionNode, final Node[] array, final Block[] array2, final int[] array3) {
        for (int i = 0; i < array2.length; ++i) {
            array2[i].initLiveOnEntrySets(optFunctionNode, array);
        }
        final boolean[] array4 = new boolean[array2.length];
        final boolean[] array5 = new boolean[array2.length];
        int n = array2.length - 1;
        boolean b = false;
        array4[n] = true;
        while (true) {
            boolean b2 = false;
            Label_0174: {
                if (!array4[n]) {
                    b2 = b;
                    if (array5[n]) {
                        break Label_0174;
                    }
                }
                array5[n] = true;
                array4[n] = false;
                b2 = b;
                if (array2[n].doReachedUseDataFlow()) {
                    final Block[] itsPredecessors = array2[n].itsPredecessors;
                    b2 = b;
                    if (itsPredecessors != null) {
                        for (int j = 0; j < itsPredecessors.length; ++j) {
                            final int itsBlockID = itsPredecessors[j].itsBlockID;
                            array4[itsBlockID] = true;
                            b |= (itsBlockID > n);
                        }
                        b2 = b;
                    }
                }
            }
            if (n == 0) {
                if (!b2) {
                    break;
                }
                n = array2.length - 1;
                b = false;
            }
            else {
                --n;
                b = b2;
            }
        }
        array2[0].markAnyTypeVariables(array3);
    }
    
    static void runFlowAnalyzes(final OptFunctionNode optFunctionNode, final Node[] array) {
        int i = optFunctionNode.fnode.getParamCount();
        final int paramAndVarCount = optFunctionNode.fnode.getParamAndVarCount();
        final int[] array2 = new int[paramAndVarCount];
        for (int j = 0; j != i; ++j) {
            array2[j] = 3;
        }
        for (int k = i; k != paramAndVarCount; ++k) {
            array2[k] = 0;
        }
        final Block[] buildBlocks = buildBlocks(array);
        reachingDefDataFlow(optFunctionNode, array, buildBlocks, array2);
        typeFlow(optFunctionNode, array, buildBlocks, array2);
        while (i != paramAndVarCount) {
            if (array2[i] == 1) {
                optFunctionNode.setIsNumberVar(i);
            }
            ++i;
        }
    }
    
    private static String toString(final Block[] array, final Node[] array2) {
        return null;
    }
    
    private static void typeFlow(final OptFunctionNode optFunctionNode, final Node[] array, final Block[] array2, final int[] array3) {
        final boolean[] array4 = new boolean[array2.length];
        final boolean[] array5 = new boolean[array2.length];
        int n = 0;
        boolean b = false;
        array4[0] = true;
        while (true) {
            boolean b2 = false;
            Label_0153: {
                if (!array4[n]) {
                    b2 = b;
                    if (array5[n]) {
                        break Label_0153;
                    }
                }
                array5[n] = true;
                array4[n] = false;
                b2 = b;
                if (array2[n].doTypeFlow(optFunctionNode, array, array3)) {
                    final Block[] itsSuccessors = array2[n].itsSuccessors;
                    b2 = b;
                    if (itsSuccessors != null) {
                        for (int i = 0; i < itsSuccessors.length; ++i) {
                            final int itsBlockID = itsSuccessors[i].itsBlockID;
                            array4[itsBlockID] = true;
                            b |= (itsBlockID < n);
                        }
                        b2 = b;
                    }
                }
            }
            if (n == array2.length - 1) {
                if (!b2) {
                    break;
                }
                n = 0;
                b = false;
            }
            else {
                ++n;
                b = b2;
            }
        }
    }
    
    private boolean updateEntrySet(final BitSet set, final BitSet set2, final BitSet set3, final BitSet set4) {
        final int cardinality = set.cardinality();
        set.or(set2);
        set.and(set4);
        set.or(set3);
        return set.cardinality() != cardinality;
    }
    
    private static class FatBlock
    {
        private ObjToIntMap predecessors;
        Block realBlock;
        private ObjToIntMap successors;
        
        private FatBlock() {
            this.successors = new ObjToIntMap();
            this.predecessors = new ObjToIntMap();
        }
        
        private static Block[] reduceToArray(final ObjToIntMap objToIntMap) {
            Block[] array = null;
            if (!objToIntMap.isEmpty()) {
                final Block[] array2 = new Block[objToIntMap.size()];
                int n = 0;
                final ObjToIntMap.Iterator iterator = objToIntMap.newIterator();
                iterator.start();
                while (true) {
                    array = array2;
                    if (iterator.done()) {
                        break;
                    }
                    array2[n] = ((FatBlock)iterator.getKey()).realBlock;
                    iterator.next();
                    ++n;
                }
            }
            return array;
        }
        
        void addPredecessor(final FatBlock fatBlock) {
            this.predecessors.put(fatBlock, 0);
        }
        
        void addSuccessor(final FatBlock fatBlock) {
            this.successors.put(fatBlock, 0);
        }
        
        Block[] getPredecessors() {
            return reduceToArray(this.predecessors);
        }
        
        Block[] getSuccessors() {
            return reduceToArray(this.successors);
        }
    }
}
