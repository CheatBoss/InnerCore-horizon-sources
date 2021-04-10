package org.mozilla.javascript.optimizer;

import java.util.*;
import org.mozilla.javascript.ast.*;
import org.mozilla.javascript.*;

class OptTransformer extends NodeTransformer
{
    private ObjArray directCallTargets;
    private Map<String, OptFunctionNode> possibleDirectCalls;
    
    OptTransformer(final Map<String, OptFunctionNode> possibleDirectCalls, final ObjArray directCallTargets) {
        this.possibleDirectCalls = possibleDirectCalls;
        this.directCallTargets = directCallTargets;
    }
    
    private void detectDirectCall(final Node node, final ScriptNode scriptNode) {
        if (scriptNode.getType() == 109) {
            final Node firstChild = node.getFirstChild();
            int n = 0;
            for (Node node2 = firstChild.getNext(); node2 != null; node2 = node2.getNext(), ++n) {}
            if (n == 0) {
                OptFunctionNode.get(scriptNode).itsContainsCalls0 = true;
            }
            if (this.possibleDirectCalls != null) {
                Object o = null;
                if (firstChild.getType() == 39) {
                    o = firstChild.getString();
                }
                else if (firstChild.getType() == 33) {
                    o = firstChild.getFirstChild().getNext().getString();
                }
                else if (firstChild.getType() == 34) {
                    throw Kit.codeBug();
                }
                if (o != null) {
                    final OptFunctionNode optFunctionNode = this.possibleDirectCalls.get(o);
                    if (optFunctionNode != null && n == optFunctionNode.fnode.getParamCount() && !optFunctionNode.fnode.requiresActivation() && n <= 32) {
                        node.putProp(9, optFunctionNode);
                        if (!optFunctionNode.isTargetOfDirectCall()) {
                            final int size = this.directCallTargets.size();
                            this.directCallTargets.add(optFunctionNode);
                            optFunctionNode.setDirectTargetIndex(size);
                        }
                    }
                }
            }
        }
    }
    
    @Override
    protected void visitCall(final Node node, final ScriptNode scriptNode) {
        this.detectDirectCall(node, scriptNode);
        super.visitCall(node, scriptNode);
    }
    
    @Override
    protected void visitNew(final Node node, final ScriptNode scriptNode) {
        this.detectDirectCall(node, scriptNode);
        super.visitNew(node, scriptNode);
    }
}
