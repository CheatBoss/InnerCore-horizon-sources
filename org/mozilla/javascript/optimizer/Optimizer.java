package org.mozilla.javascript.optimizer;

import org.mozilla.javascript.*;
import org.mozilla.javascript.ast.*;

class Optimizer
{
    static final int AnyType = 3;
    static final int NoType = 0;
    static final int NumberType = 1;
    private boolean inDirectCallFunction;
    private boolean parameterUsedInNumberContext;
    OptFunctionNode theFunction;
    
    private static void buildStatementList_r(Node node, final ObjArray objArray) {
        final int type = node.getType();
        if (type != 129 && type != 141 && type != 132 && type != 109) {
            objArray.add(node);
            return;
        }
        for (node = node.getFirstChild(); node != null; node = node.getNext()) {
            buildStatementList_r(node, objArray);
        }
    }
    
    private boolean convertParameter(final Node node) {
        if (this.inDirectCallFunction && node.getType() == 55 && this.theFunction.isParameter(this.theFunction.getVarIndex(node))) {
            node.removeProp(8);
            return true;
        }
        return false;
    }
    
    private void markDCPNumberContext(final Node node) {
        if (this.inDirectCallFunction && node.getType() == 55 && this.theFunction.isParameter(this.theFunction.getVarIndex(node))) {
            this.parameterUsedInNumberContext = true;
        }
    }
    
    private void optimizeFunction(final OptFunctionNode theFunction) {
        if (theFunction.fnode.requiresActivation()) {
            return;
        }
        this.inDirectCallFunction = theFunction.isTargetOfDirectCall();
        this.theFunction = theFunction;
        final ObjArray objArray = new ObjArray();
        buildStatementList_r(theFunction.fnode, objArray);
        final Node[] array = new Node[objArray.size()];
        objArray.toArray(array);
        Block.runFlowAnalyzes(theFunction, array);
        if (!theFunction.fnode.requiresActivation()) {
            int i = 0;
            this.parameterUsedInNumberContext = false;
            while (i < array.length) {
                this.rewriteForNumberVariables(array[i], 1);
                ++i;
            }
            theFunction.setParameterNumberContext(this.parameterUsedInNumberContext);
        }
    }
    
    private void rewriteAsObjectChildren(final Node node, Node node2) {
        while (node2 != null) {
            final Node next = node2.getNext();
            if (this.rewriteForNumberVariables(node2, 0) == 1 && !this.convertParameter(node2)) {
                node.removeChild(node2);
                final Node node3 = new Node(149, node2);
                if (next == null) {
                    node.addChildToBack(node3);
                }
                else {
                    node.addChildBefore(node3, next);
                }
            }
            node2 = next;
        }
    }
    
    private int rewriteForNumberVariables(Node next, int n) {
        final int type = next.getType();
        if (type == 40) {
            next.putIntProp(8, 0);
            return 1;
        }
        if (type != 133) {
            Label_1178: {
                if (type != 140) {
                    Label_1010: {
                        if (type != 156) {
                            Label_0794: {
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
                                                                                this.rewriteAsObjectChildren(next, next.getFirstChild());
                                                                                return 0;
                                                                            }
                                                                            case 106:
                                                                            case 107: {
                                                                                final Node firstChild = next.getFirstChild();
                                                                                n = this.rewriteForNumberVariables(firstChild, 1);
                                                                                if (firstChild.getType() == 55) {
                                                                                    if (n == 1 && !this.convertParameter(firstChild)) {
                                                                                        next.putIntProp(8, 0);
                                                                                        this.markDCPNumberContext(firstChild);
                                                                                        return 1;
                                                                                    }
                                                                                    return 0;
                                                                                }
                                                                                else {
                                                                                    if (firstChild.getType() == 36) {
                                                                                        return n;
                                                                                    }
                                                                                    if (firstChild.getType() == 33) {
                                                                                        return n;
                                                                                    }
                                                                                    return 0;
                                                                                }
                                                                                break;
                                                                            }
                                                                        }
                                                                        break;
                                                                    }
                                                                    case 55: {
                                                                        final int varIndex = this.theFunction.getVarIndex(next);
                                                                        if (this.inDirectCallFunction && this.theFunction.isParameter(varIndex) && n == 1) {
                                                                            next.putIntProp(8, 0);
                                                                            return 1;
                                                                        }
                                                                        if (this.theFunction.isNumberVar(varIndex)) {
                                                                            next.putIntProp(8, 0);
                                                                            return 1;
                                                                        }
                                                                        return 0;
                                                                    }
                                                                    case 56: {
                                                                        break Label_1010;
                                                                    }
                                                                }
                                                                break;
                                                            }
                                                            case 38: {
                                                                final Node firstChild2 = next.getFirstChild();
                                                                this.rewriteAsObjectChildren(firstChild2, firstChild2.getFirstChild());
                                                                final Node next2 = firstChild2.getNext();
                                                                if (next.getProp(9) != null) {
                                                                    for (next = next2; next != null; next = next.getNext()) {
                                                                        if (this.rewriteForNumberVariables(next, 1) == 1) {
                                                                            this.markDCPNumberContext(next);
                                                                        }
                                                                    }
                                                                }
                                                                else {
                                                                    this.rewriteAsObjectChildren(next, next2);
                                                                }
                                                                return 0;
                                                            }
                                                            case 36: {
                                                                final Node firstChild3 = next.getFirstChild();
                                                                final Node next3 = firstChild3.getNext();
                                                                if (this.rewriteForNumberVariables(firstChild3, 1) == 1 && !this.convertParameter(firstChild3)) {
                                                                    next.removeChild(firstChild3);
                                                                    next.addChildToFront(new Node(149, firstChild3));
                                                                }
                                                                if (this.rewriteForNumberVariables(next3, 1) == 1 && !this.convertParameter(next3)) {
                                                                    next.putIntProp(8, 2);
                                                                }
                                                                return 0;
                                                            }
                                                            case 37: {
                                                                break Label_1178;
                                                            }
                                                        }
                                                        break;
                                                    }
                                                    case 21: {
                                                        final Node firstChild4 = next.getFirstChild();
                                                        final Node next4 = firstChild4.getNext();
                                                        n = this.rewriteForNumberVariables(firstChild4, 1);
                                                        final int rewriteForNumberVariables = this.rewriteForNumberVariables(next4, 1);
                                                        if (this.convertParameter(firstChild4)) {
                                                            if (this.convertParameter(next4)) {
                                                                return 0;
                                                            }
                                                            if (rewriteForNumberVariables == 1) {
                                                                next.putIntProp(8, 2);
                                                                return 0;
                                                            }
                                                        }
                                                        else if (this.convertParameter(next4)) {
                                                            if (n == 1) {
                                                                next.putIntProp(8, 1);
                                                                return 0;
                                                            }
                                                        }
                                                        else if (n == 1) {
                                                            if (rewriteForNumberVariables == 1) {
                                                                next.putIntProp(8, 0);
                                                                return 1;
                                                            }
                                                            next.putIntProp(8, 1);
                                                            return 0;
                                                        }
                                                        else if (rewriteForNumberVariables == 1) {
                                                            next.putIntProp(8, 2);
                                                        }
                                                        return 0;
                                                    }
                                                    case 22:
                                                    case 23:
                                                    case 24:
                                                    case 25: {
                                                        break Label_0794;
                                                    }
                                                }
                                                break;
                                            }
                                            case 14:
                                            case 15:
                                            case 16:
                                            case 17: {
                                                final Node firstChild5 = next.getFirstChild();
                                                final Node next5 = firstChild5.getNext();
                                                n = this.rewriteForNumberVariables(firstChild5, 1);
                                                final int rewriteForNumberVariables2 = this.rewriteForNumberVariables(next5, 1);
                                                this.markDCPNumberContext(firstChild5);
                                                this.markDCPNumberContext(next5);
                                                if (this.convertParameter(firstChild5)) {
                                                    if (this.convertParameter(next5)) {
                                                        return 0;
                                                    }
                                                    if (rewriteForNumberVariables2 == 1) {
                                                        next.putIntProp(8, 2);
                                                        return 0;
                                                    }
                                                }
                                                else if (this.convertParameter(next5)) {
                                                    if (n == 1) {
                                                        next.putIntProp(8, 1);
                                                        return 0;
                                                    }
                                                }
                                                else if (n == 1) {
                                                    if (rewriteForNumberVariables2 == 1) {
                                                        next.putIntProp(8, 0);
                                                        return 0;
                                                    }
                                                    next.putIntProp(8, 1);
                                                    return 0;
                                                }
                                                else if (rewriteForNumberVariables2 == 1) {
                                                    next.putIntProp(8, 2);
                                                }
                                                return 0;
                                            }
                                            case 18:
                                            case 19: {
                                                break Label_0794;
                                            }
                                        }
                                        break;
                                    }
                                    case 9:
                                    case 10:
                                    case 11: {
                                        final Node firstChild6 = next.getFirstChild();
                                        final Node next6 = firstChild6.getNext();
                                        n = this.rewriteForNumberVariables(firstChild6, 1);
                                        final int rewriteForNumberVariables3 = this.rewriteForNumberVariables(next6, 1);
                                        this.markDCPNumberContext(firstChild6);
                                        this.markDCPNumberContext(next6);
                                        if (n == 1) {
                                            if (rewriteForNumberVariables3 == 1) {
                                                next.putIntProp(8, 0);
                                                return 1;
                                            }
                                            if (!this.convertParameter(next6)) {
                                                next.removeChild(next6);
                                                next.addChildToBack(new Node(150, next6));
                                                next.putIntProp(8, 0);
                                            }
                                            return 1;
                                        }
                                        else {
                                            if (rewriteForNumberVariables3 == 1) {
                                                if (!this.convertParameter(firstChild6)) {
                                                    next.removeChild(firstChild6);
                                                    next.addChildToFront(new Node(150, firstChild6));
                                                    next.putIntProp(8, 0);
                                                }
                                                return 1;
                                            }
                                            if (!this.convertParameter(firstChild6)) {
                                                next.removeChild(firstChild6);
                                                next.addChildToFront(new Node(150, firstChild6));
                                            }
                                            if (!this.convertParameter(next6)) {
                                                next.removeChild(next6);
                                                next.addChildToBack(new Node(150, next6));
                                            }
                                            next.putIntProp(8, 0);
                                            return 1;
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    final Node next7 = next.getFirstChild().getNext();
                    n = this.rewriteForNumberVariables(next7, 1);
                    final int varIndex2 = this.theFunction.getVarIndex(next);
                    if (this.inDirectCallFunction && this.theFunction.isParameter(varIndex2)) {
                        if (n != 1) {
                            return n;
                        }
                        if (!this.convertParameter(next7)) {
                            next.putIntProp(8, 0);
                            return 1;
                        }
                        this.markDCPNumberContext(next7);
                        return 0;
                    }
                    else {
                        if (this.theFunction.isNumberVar(varIndex2)) {
                            if (n != 1) {
                                next.removeChild(next7);
                                next.addChildToBack(new Node(150, next7));
                            }
                            next.putIntProp(8, 0);
                            this.markDCPNumberContext(next7);
                            return 1;
                        }
                        if (n == 1 && !this.convertParameter(next7)) {
                            next.removeChild(next7);
                            next.addChildToBack(new Node(149, next7));
                        }
                        return 0;
                    }
                }
            }
            final Node firstChild7 = next.getFirstChild();
            final Node next8 = firstChild7.getNext();
            final Node next9 = next8.getNext();
            if (this.rewriteForNumberVariables(firstChild7, 1) == 1 && !this.convertParameter(firstChild7)) {
                next.removeChild(firstChild7);
                next.addChildToFront(new Node(149, firstChild7));
            }
            if (this.rewriteForNumberVariables(next8, 1) == 1 && !this.convertParameter(next8)) {
                next.putIntProp(8, 1);
            }
            if (this.rewriteForNumberVariables(next9, 1) == 1 && !this.convertParameter(next9)) {
                next.removeChild(next9);
                next.addChildToBack(new Node(149, next9));
            }
            return 0;
        }
        if (this.rewriteForNumberVariables(next.getFirstChild(), 1) == 1) {
            next.putIntProp(8, 0);
        }
        return 0;
    }
    
    void optimize(final ScriptNode scriptNode) {
        for (int functionCount = scriptNode.getFunctionCount(), i = 0; i != functionCount; ++i) {
            this.optimizeFunction(OptFunctionNode.get(scriptNode, i));
        }
    }
}
