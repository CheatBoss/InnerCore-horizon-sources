package org.mozilla.javascript;

import org.mozilla.javascript.ast.*;
import java.util.*;

public class NodeTransformer
{
    private boolean hasFinally;
    private ObjArray loopEnds;
    private ObjArray loops;
    
    private static Node addBeforeCurrent(final Node node, final Node node2, final Node node3, final Node node4) {
        if (node2 == null) {
            if (node3 != node.getFirstChild()) {
                Kit.codeBug();
            }
            node.addChildToFront(node4);
            return node4;
        }
        if (node3 != node2.getNext()) {
            Kit.codeBug();
        }
        node.addChildAfter(node4, node2);
        return node4;
    }
    
    private static Node replaceCurrent(final Node node, final Node node2, final Node node3, final Node node4) {
        if (node2 == null) {
            if (node3 != node.getFirstChild()) {
                Kit.codeBug();
            }
            node.replaceChild(node3, node4);
            return node4;
        }
        if (node2.next == node3) {
            node.replaceChildAfter(node2, node4);
            return node4;
        }
        node.replaceChild(node3, node4);
        return node4;
    }
    
    private void transformCompilationUnit(final ScriptNode scriptNode) {
        this.loops = new ObjArray();
        this.loopEnds = new ObjArray();
        this.hasFinally = false;
        final boolean b = scriptNode.getType() != 109 || ((FunctionNode)scriptNode).requiresActivation();
        scriptNode.flattenSymbolTable(!b);
        this.transformCompilationUnit_r(scriptNode, scriptNode, scriptNode, b, scriptNode instanceof AstRoot && ((AstRoot)scriptNode).isInStrictMode());
    }
    
    private void transformCompilationUnit_r(final ScriptNode scriptNode, final Node node, final Scope scope, final boolean b, final boolean b2) {
        Node node2 = null;
        while (true) {
            Node node3 = null;
            Node node4;
            if (node2 == null) {
                node4 = node.getFirstChild();
            }
            else {
                node3 = node2;
                node4 = node2.getNext();
            }
            Node node5 = node3;
            if (node4 == null) {
                return;
            }
            final int type = node4.getType();
            Node replaceCurrent = node4;
            int type2 = type;
            Label_0268: {
                if (b) {
                    if (type != 129 && type != 132) {
                        replaceCurrent = node4;
                        if ((type2 = type) != 157) {
                            break Label_0268;
                        }
                    }
                    replaceCurrent = node4;
                    type2 = type;
                    if (node4 instanceof Scope) {
                        final Scope scope2 = (Scope)node4;
                        replaceCurrent = node4;
                        type2 = type;
                        if (scope2.getSymbolTable() != null) {
                            int n;
                            if (type == 157) {
                                n = 158;
                            }
                            else {
                                n = 153;
                            }
                            final Node node6 = new Node(n);
                            final Node node7 = new Node(153);
                            node6.addChildToBack(node7);
                            final Iterator<String> iterator = scope2.getSymbolTable().keySet().iterator();
                            while (iterator.hasNext()) {
                                node7.addChildToBack(Node.newString(39, iterator.next()));
                            }
                            scope2.setSymbolTable(null);
                            replaceCurrent = replaceCurrent(node, node5, node4, node6);
                            type2 = replaceCurrent.getType();
                            node6.addChildToBack(node4);
                        }
                    }
                }
            }
            final boolean b3 = true;
            Label_1903: {
                switch (type2) {
                    default: {
                        node2 = replaceCurrent;
                        break;
                    }
                    case 137: {
                        final Scope definingScope = scope.getDefiningScope(replaceCurrent.getString());
                        if (definingScope != null) {
                            replaceCurrent.setScope(definingScope);
                        }
                        node2 = replaceCurrent;
                        break;
                    }
                    case 123: {
                        this.loops.push(replaceCurrent);
                        final Node next = replaceCurrent.getNext();
                        if (next.getType() != 3) {
                            Kit.codeBug();
                        }
                        this.loopEnds.push(next);
                        node2 = replaceCurrent;
                        break;
                    }
                    case 153:
                    case 158: {
                        if (replaceCurrent.getFirstChild().getType() == 153) {
                            boolean b4 = b3;
                            if (scriptNode.getType() == 109) {
                                b4 = (((FunctionNode)scriptNode).requiresActivation() && b3);
                            }
                            node2 = this.visitLet(b4, node, node5, replaceCurrent);
                            break;
                        }
                    }
                    case 122:
                    case 154: {
                        final Node node8 = new Node(129);
                        Node firstChild = replaceCurrent.getFirstChild();
                        while (firstChild != null) {
                            final Node node9 = firstChild;
                            final Node next2 = firstChild.getNext();
                            Node node10;
                            if (node9.getType() == 39) {
                                if (!node9.hasChildren()) {
                                    firstChild = next2;
                                    continue;
                                }
                                final Node firstChild2 = node9.getFirstChild();
                                node9.removeChild(firstChild2);
                                node9.setType(49);
                                int n2;
                                if (type2 == 154) {
                                    n2 = 155;
                                }
                                else {
                                    n2 = 8;
                                }
                                node10 = new Node(n2, node9, firstChild2);
                            }
                            else {
                                node10 = node9;
                                if (node9.getType() != 158) {
                                    throw Kit.codeBug();
                                }
                            }
                            node8.addChildToBack(new Node(133, node10, replaceCurrent.getLineno()));
                            firstChild = next2;
                        }
                        node2 = replaceCurrent(node, node5, replaceCurrent, node8);
                        break;
                    }
                    case 120:
                    case 121: {
                        final Scope scope3 = (Scope)replaceCurrent;
                        final Jump jumpStatement = scope3.getJumpStatement();
                        if (jumpStatement == null) {
                            Kit.codeBug();
                        }
                        int i = this.loops.size();
                        while (i != 0) {
                            --i;
                            final Node node11 = (Node)this.loops.get(i);
                            if (node11 == jumpStatement) {
                                if (type2 == 120) {
                                    scope3.target = jumpStatement.target;
                                }
                                else {
                                    scope3.target = jumpStatement.getContinue();
                                }
                                scope3.setType(5);
                                node2 = replaceCurrent;
                                break Label_1903;
                            }
                            final int type3 = node11.getType();
                            Node node12;
                            if (type3 == 123) {
                                node12 = addBeforeCurrent(node, node5, replaceCurrent, new Node(3));
                            }
                            else {
                                node12 = node5;
                                if (type3 == 81) {
                                    final Jump jump = (Jump)node11;
                                    final Jump jump2 = new Jump(135);
                                    jump2.target = jump.getFinally();
                                    node12 = addBeforeCurrent(node, node5, replaceCurrent, jump2);
                                }
                            }
                            node5 = node12;
                        }
                        throw Kit.codeBug();
                    }
                    case 114:
                    case 130:
                    case 132: {
                        this.loops.push(replaceCurrent);
                        this.loopEnds.push(((Scope)replaceCurrent).target);
                        node2 = replaceCurrent;
                        break;
                    }
                    case 81: {
                        final Node finally1 = ((Scope)replaceCurrent).getFinally();
                        node2 = replaceCurrent;
                        if (finally1 != null) {
                            this.hasFinally = true;
                            this.loops.push(replaceCurrent);
                            this.loopEnds.push(finally1);
                            node2 = replaceCurrent;
                            break;
                        }
                        break;
                    }
                    case 72: {
                        ((FunctionNode)scriptNode).addResumptionPoint(replaceCurrent);
                        node2 = replaceCurrent;
                        break;
                    }
                    case 38: {
                        this.visitCall(replaceCurrent, scriptNode);
                        node2 = replaceCurrent;
                        break;
                    }
                    case 30: {
                        this.visitNew(replaceCurrent, scriptNode);
                        node2 = replaceCurrent;
                        break;
                    }
                    case 8: {
                        if (b2) {
                            replaceCurrent.setType(73);
                        }
                    }
                    case 31:
                    case 39:
                    case 155: {
                        if (b) {
                            node2 = replaceCurrent;
                            break;
                        }
                        Node firstChild3;
                        if (type2 == 39) {
                            firstChild3 = replaceCurrent;
                        }
                        else if ((firstChild3 = replaceCurrent.getFirstChild()).getType() != 49) {
                            if (type2 == 31) {
                                node2 = replaceCurrent;
                                break;
                            }
                            throw Kit.codeBug();
                        }
                        if (firstChild3.getScope() != null) {
                            node2 = replaceCurrent;
                            break;
                        }
                        final Scope definingScope2 = scope.getDefiningScope(firstChild3.getString());
                        node2 = replaceCurrent;
                        if (definingScope2 == null) {
                            break;
                        }
                        firstChild3.setScope(definingScope2);
                        if (type2 == 39) {
                            replaceCurrent.setType(55);
                            node2 = replaceCurrent;
                            break;
                        }
                        if (type2 == 8 || type2 == 73) {
                            replaceCurrent.setType(56);
                            firstChild3.setType(41);
                            node2 = replaceCurrent;
                            break;
                        }
                        if (type2 == 155) {
                            replaceCurrent.setType(156);
                            firstChild3.setType(41);
                            node2 = replaceCurrent;
                            break;
                        }
                        if (type2 == 31) {
                            node2 = replaceCurrent(node, node5, replaceCurrent, new Node(44));
                            break;
                        }
                        throw Kit.codeBug();
                    }
                    case 7:
                    case 32: {
                        Node firstChild5;
                        Node firstChild4 = firstChild5 = replaceCurrent.getFirstChild();
                        Label_1522: {
                            if (type2 == 7) {
                                while (firstChild4.getType() == 26) {
                                    firstChild4 = firstChild4.getFirstChild();
                                }
                                if (firstChild4.getType() != 12) {
                                    firstChild5 = firstChild4;
                                    if (firstChild4.getType() != 13) {
                                        break Label_1522;
                                    }
                                }
                                final Node firstChild6 = firstChild4.getFirstChild();
                                final Node lastChild = firstChild4.getLastChild();
                                if (firstChild6.getType() == 39 && firstChild6.getString().equals("undefined")) {
                                    firstChild5 = lastChild;
                                }
                                else {
                                    firstChild5 = firstChild4;
                                    if (lastChild.getType() == 39) {
                                        firstChild5 = firstChild4;
                                        if (lastChild.getString().equals("undefined")) {
                                            firstChild5 = firstChild6;
                                        }
                                    }
                                }
                            }
                        }
                        node2 = replaceCurrent;
                        if (firstChild5.getType() == 33) {
                            firstChild5.setType(34);
                            node2 = replaceCurrent;
                            break;
                        }
                        break;
                    }
                    case 4: {
                        final boolean b5 = scriptNode.getType() == 109 && ((FunctionNode)scriptNode).isGenerator();
                        if (b5) {
                            replaceCurrent.putIntProp(20, 1);
                        }
                        if (!this.hasFinally) {
                            node2 = replaceCurrent;
                            break;
                        }
                        int j = this.loops.size() - 1;
                        Node node13 = null;
                        while (j >= 0) {
                            final Node node14 = (Node)this.loops.get(j);
                            final int type4 = node14.getType();
                            if (type4 == 81 || type4 == 123) {
                                Node node15;
                                if (type4 == 81) {
                                    node15 = new Jump(135);
                                    ((Jump)node15).target = ((Jump)node14).getFinally();
                                }
                                else {
                                    node15 = new Node(3);
                                }
                                if (node13 == null) {
                                    node13 = new Node(129, replaceCurrent.getLineno());
                                }
                                node13.addChildToBack(node15);
                            }
                            --j;
                        }
                        node2 = replaceCurrent;
                        if (node13 != null) {
                            final Node firstChild7 = replaceCurrent.getFirstChild();
                            node2 = replaceCurrent(node, node5, replaceCurrent, node13);
                            if (firstChild7 != null && !b5) {
                                final Node node16 = new Node(134, firstChild7);
                                node13.addChildToFront(node16);
                                node13.addChildToBack(new Node(64));
                                this.transformCompilationUnit_r(scriptNode, node16, scope, b, b2);
                            }
                            else {
                                node13.addChildToBack(replaceCurrent);
                            }
                            continue;
                        }
                        break;
                    }
                    case 3:
                    case 131: {
                        node2 = replaceCurrent;
                        if (!this.loopEnds.isEmpty() && this.loopEnds.peek() == (node2 = replaceCurrent)) {
                            this.loopEnds.pop();
                            this.loops.pop();
                            node2 = replaceCurrent;
                            break;
                        }
                        break;
                    }
                }
            }
            Scope scope4;
            if (node2 instanceof Scope) {
                scope4 = (Scope)node2;
            }
            else {
                scope4 = scope;
            }
            this.transformCompilationUnit_r(scriptNode, node2, scope4, b, b2);
        }
    }
    
    public final void transform(final ScriptNode scriptNode) {
        this.transformCompilationUnit(scriptNode);
        for (int i = 0; i != scriptNode.getFunctionCount(); ++i) {
            this.transform(scriptNode.getFunctionNode(i));
        }
    }
    
    protected void visitCall(final Node node, final ScriptNode scriptNode) {
    }
    
    protected Node visitLet(final boolean b, Node o, Node node, Node firstChild) {
        final Node firstChild2 = firstChild.getFirstChild();
        final Node next = firstChild2.getNext();
        firstChild.removeChild(firstChild2);
        firstChild.removeChild(next);
        final boolean b2 = firstChild.getType() == 158;
        if (b) {
            int n;
            if (b2) {
                n = 159;
            }
            else {
                n = 129;
            }
            final Node replaceCurrent = replaceCurrent((Node)o, node, firstChild, new Node(n));
            final ArrayList<Object> list = new ArrayList<Object>();
            final Node node2 = new Node(66);
            node = firstChild2.getFirstChild();
            o = next;
            while (node != null) {
                firstChild = node;
                if (firstChild.getType() == 158) {
                    final List list2 = (List)firstChild.getProp(22);
                    final Node firstChild3 = firstChild.getFirstChild();
                    if (firstChild3.getType() != 153) {
                        throw Kit.codeBug();
                    }
                    Node node3;
                    if (b2) {
                        node3 = new Node(89, firstChild3.getNext(), (Node)o);
                    }
                    else {
                        node3 = new Node(129, new Node(133, firstChild3.getNext()), (Node)o);
                    }
                    Object o2 = node3;
                    if (list2 != null) {
                        list.addAll(list2);
                        int n2 = 0;
                        while (true) {
                            o2 = node3;
                            if (n2 >= list2.size()) {
                                break;
                            }
                            node2.addChildToBack(new Node(126, Node.newNumber(0.0)));
                            ++n2;
                        }
                    }
                    final Node firstChild4 = firstChild3.getFirstChild();
                    o = o2;
                    firstChild = firstChild4;
                }
                if (firstChild.getType() != 39) {
                    throw Kit.codeBug();
                }
                list.add(ScriptRuntime.getIndexObject(firstChild.getString()));
                if ((firstChild = firstChild.getFirstChild()) == null) {
                    firstChild = new Node(126, Node.newNumber(0.0));
                }
                node2.addChildToBack(firstChild);
                node = node.getNext();
            }
            node2.putProp(12, list.toArray());
            replaceCurrent.addChildToBack(new Node(2, node2));
            replaceCurrent.addChildToBack(new Node(123, (Node)o));
            replaceCurrent.addChildToBack(new Node(3));
            return replaceCurrent;
        }
        int n3;
        if (b2) {
            n3 = 89;
        }
        else {
            n3 = 129;
        }
        final Node replaceCurrent2 = replaceCurrent((Node)o, node, firstChild, new Node(n3));
        final Node node4 = new Node(89);
        o = firstChild2.getFirstChild();
        node = next;
        while (o != null) {
            Object firstChild5 = o;
            if (((Node)firstChild5).getType() == 158) {
                final Node firstChild6 = ((Node)firstChild5).getFirstChild();
                if (firstChild6.getType() != 153) {
                    throw Kit.codeBug();
                }
                if (b2) {
                    node = new Node(89, firstChild6.getNext(), node);
                }
                else {
                    node = new Node(129, new Node(133, firstChild6.getNext()), node);
                }
                Scope.joinScopes((Scope)firstChild5, (Scope)firstChild);
                firstChild5 = firstChild6.getFirstChild();
            }
            if (((Node)firstChild5).getType() != 39) {
                throw Kit.codeBug();
            }
            final Node string = Node.newString(((Node)firstChild5).getString());
            string.setScope((Scope)firstChild);
            Node firstChild7 = ((Node)firstChild5).getFirstChild();
            if (firstChild7 == null) {
                firstChild7 = new Node(126, Node.newNumber(0.0));
            }
            node4.addChildToBack(new Node(56, string, firstChild7));
            o = ((Node)o).getNext();
        }
        if (b2) {
            replaceCurrent2.addChildToBack(node4);
            firstChild.setType(89);
            replaceCurrent2.addChildToBack(firstChild);
            firstChild.addChildToBack(node);
            if (node instanceof Scope) {
                final Scope parentScope = ((Scope)node).getParentScope();
                ((Scope)node).setParentScope((Scope)firstChild);
                ((Scope)firstChild).setParentScope(parentScope);
                return replaceCurrent2;
            }
        }
        else {
            replaceCurrent2.addChildToBack(new Node(133, node4));
            firstChild.setType(129);
            replaceCurrent2.addChildToBack(firstChild);
            firstChild.addChildrenToBack(node);
            if (node instanceof Scope) {
                final Scope parentScope2 = ((Scope)node).getParentScope();
                ((Scope)node).setParentScope((Scope)firstChild);
                ((Scope)firstChild).setParentScope(parentScope2);
            }
        }
        return replaceCurrent2;
    }
    
    protected void visitNew(final Node node, final ScriptNode scriptNode) {
    }
}
