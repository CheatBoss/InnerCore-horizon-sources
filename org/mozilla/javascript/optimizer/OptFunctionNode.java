package org.mozilla.javascript.optimizer;

import org.mozilla.javascript.ast.*;
import org.mozilla.javascript.*;

public final class OptFunctionNode
{
    private int directTargetIndex;
    public final FunctionNode fnode;
    boolean itsContainsCalls0;
    boolean itsContainsCalls1;
    private boolean itsParameterNumberContext;
    private boolean[] numberVarFlags;
    
    OptFunctionNode(final FunctionNode fnode) {
        this.directTargetIndex = -1;
        (this.fnode = fnode).setCompilerData(this);
    }
    
    public static OptFunctionNode get(final ScriptNode scriptNode) {
        return (OptFunctionNode)scriptNode.getCompilerData();
    }
    
    public static OptFunctionNode get(final ScriptNode scriptNode, final int n) {
        return (OptFunctionNode)scriptNode.getFunctionNode(n).getCompilerData();
    }
    
    public int getDirectTargetIndex() {
        return this.directTargetIndex;
    }
    
    public boolean getParameterNumberContext() {
        return this.itsParameterNumberContext;
    }
    
    public int getVarCount() {
        return this.fnode.getParamAndVarCount();
    }
    
    public int getVarIndex(final Node node) {
        int n;
        if ((n = node.getIntProp(7, -1)) == -1) {
            final int type = node.getType();
            Node firstChild;
            if (type == 55) {
                firstChild = node;
            }
            else {
                if (type != 56 && type != 156) {
                    throw Kit.codeBug();
                }
                firstChild = node.getFirstChild();
            }
            n = this.fnode.getIndexForNameNode(firstChild);
            if (n < 0) {
                throw Kit.codeBug();
            }
            node.putIntProp(7, n);
        }
        return n;
    }
    
    public boolean isNumberVar(int n) {
        n -= this.fnode.getParamCount();
        return n >= 0 && this.numberVarFlags != null && this.numberVarFlags[n];
    }
    
    public boolean isParameter(final int n) {
        return n < this.fnode.getParamCount();
    }
    
    public boolean isTargetOfDirectCall() {
        return this.directTargetIndex >= 0;
    }
    
    void setDirectTargetIndex(final int directTargetIndex) {
        if (directTargetIndex < 0 || this.directTargetIndex >= 0) {
            Kit.codeBug();
        }
        this.directTargetIndex = directTargetIndex;
    }
    
    void setIsNumberVar(int n) {
        n -= this.fnode.getParamCount();
        if (n < 0) {
            Kit.codeBug();
        }
        if (this.numberVarFlags == null) {
            this.numberVarFlags = new boolean[this.fnode.getParamAndVarCount() - this.fnode.getParamCount()];
        }
        this.numberVarFlags[n] = true;
    }
    
    void setParameterNumberContext(final boolean itsParameterNumberContext) {
        this.itsParameterNumberContext = itsParameterNumberContext;
    }
}
