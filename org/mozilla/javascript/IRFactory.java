package org.mozilla.javascript;

import java.util.*;
import org.mozilla.javascript.ast.*;

public final class IRFactory extends Parser
{
    private static final int ALWAYS_FALSE_BOOLEAN = -1;
    private static final int ALWAYS_TRUE_BOOLEAN = 1;
    private static final int LOOP_DO_WHILE = 0;
    private static final int LOOP_FOR = 2;
    private static final int LOOP_WHILE = 1;
    private Decompiler decompiler;
    
    public IRFactory() {
        this.decompiler = new Decompiler();
    }
    
    public IRFactory(final CompilerEnvirons compilerEnvirons) {
        this(compilerEnvirons, compilerEnvirons.getErrorReporter());
    }
    
    public IRFactory(final CompilerEnvirons compilerEnvirons, final ErrorReporter errorReporter) {
        super(compilerEnvirons, errorReporter);
        this.decompiler = new Decompiler();
    }
    
    private void addSwitchCase(final Node node, final Node node2, final Node node3) {
        if (node.getType() != 129) {
            throw Kit.codeBug();
        }
        final Jump jump = (Jump)node.getFirstChild();
        if (jump.getType() != 114) {
            throw Kit.codeBug();
        }
        final Node target = Node.newTarget();
        if (node2 != null) {
            final Jump jump2 = new Jump(115, node2);
            jump2.target = target;
            jump.addChildToBack(jump2);
        }
        else {
            jump.setDefault(target);
        }
        node.addChildToBack(target);
        node.addChildToBack(node3);
    }
    
    private Node arrayCompTransformHelper(final ArrayComprehension arrayComprehension, String s) {
        this.decompiler.addToken(83);
        final int lineno = arrayComprehension.getLineno();
        Node node = this.transform(arrayComprehension.getResult());
        final List<ArrayComprehensionLoop> loops = arrayComprehension.getLoops();
        final int size = loops.size();
        final Node[] array = new Node[size];
        final Node[] array2 = new Node[size];
        for (int i = 0; i < size; ++i) {
            final ArrayComprehensionLoop arrayComprehensionLoop = loops.get(i);
            this.decompiler.addName(" ");
            this.decompiler.addToken(119);
            if (arrayComprehensionLoop.isForEach()) {
                this.decompiler.addName("each ");
            }
            this.decompiler.addToken(87);
            final AstNode iterator = arrayComprehensionLoop.getIterator();
            String s2;
            if (iterator.getType() == 39) {
                s2 = iterator.getString();
                this.decompiler.addName(s2);
            }
            else {
                this.decompile(iterator);
                s2 = this.currentScriptOrFn.getNextTempName();
                this.defineSymbol(87, s2, false);
                node = this.createBinary(89, this.createAssignment(90, iterator, this.createName(s2)), node);
            }
            final Node name = this.createName(s2);
            this.defineSymbol(153, s2, false);
            array[i] = name;
            this.decompiler.addToken(52);
            array2[i] = this.transform(arrayComprehensionLoop.getIteratedObject());
            this.decompiler.addToken(88);
        }
        final Node name2 = this.createName(s);
        final Node node2 = null;
        final Node callOrNew = this.createCallOrNew(38, this.createPropertyGet(name2, null, "push", 0));
        Node if1 = new Node(133, callOrNew, lineno);
        if (arrayComprehension.getFilter() != null) {
            this.decompiler.addName(" ");
            this.decompiler.addToken(112);
            this.decompiler.addToken(87);
            if1 = this.createIf(this.transform(arrayComprehension.getFilter()), if1, null, lineno);
            this.decompiler.addToken(88);
        }
        int n = 0;
        int j = size - 1;
        Node forIn = if1;
        final Node node3 = node2;
        s = (String)callOrNew;
        while (j >= 0) {
            try {
                final ArrayComprehensionLoop arrayComprehensionLoop2 = loops.get(j);
                final Scope loopNode = this.createLoopNode(node3, arrayComprehensionLoop2.getLineno());
                this.pushScope(loopNode);
                ++n;
                final Node node4 = array[j];
                final Node node5 = array2[j];
                try {
                    final boolean forEach = arrayComprehensionLoop2.isForEach();
                    try {
                        forIn = this.createForIn(153, loopNode, node4, node5, forIn, forEach);
                        --j;
                    }
                    finally {}
                }
                finally {}
            }
            finally {}
            for (int k = 0; k < n; ++k) {
                this.popScope();
            }
        }
        for (int l = 0; l < n; ++l) {
            this.popScope();
        }
        this.decompiler.addToken(84);
        ((Node)s).addChildToBack(node);
        return forIn;
    }
    
    private void closeSwitch(final Node node) {
        if (node.getType() != 129) {
            throw Kit.codeBug();
        }
        final Jump jump = (Jump)node.getFirstChild();
        if (jump.getType() != 114) {
            throw Kit.codeBug();
        }
        final Node target = Node.newTarget();
        jump.target = target;
        Node default1;
        if ((default1 = jump.getDefault()) == null) {
            default1 = target;
        }
        node.addChildAfter(this.makeJump(5, default1), jump);
        node.addChildToBack(target);
    }
    
    private Node createAssignment(int n, Node node, final Node node2) {
        final Node reference = this.makeReference(node);
        if (reference == null) {
            if (node.getType() != 65 && node.getType() != 66) {
                this.reportError("msg.bad.assign.left");
                return node2;
            }
            if (n != 90) {
                this.reportError("msg.bad.destruct.op");
                return node2;
            }
            return this.createDestructuringAssignment(-1, node, node2);
        }
        else {
            switch (n) {
                default: {
                    throw Kit.codeBug();
                }
                case 101: {
                    n = 25;
                    break;
                }
                case 100: {
                    n = 24;
                    break;
                }
                case 99: {
                    n = 23;
                    break;
                }
                case 98: {
                    n = 22;
                    break;
                }
                case 97: {
                    n = 21;
                    break;
                }
                case 96: {
                    n = 20;
                    break;
                }
                case 95: {
                    n = 19;
                    break;
                }
                case 94: {
                    n = 18;
                    break;
                }
                case 93: {
                    n = 11;
                    break;
                }
                case 92: {
                    n = 10;
                    break;
                }
                case 91: {
                    n = 9;
                    break;
                }
                case 90: {
                    return this.simpleAssignment(reference, node2);
                }
            }
            final int type = reference.getType();
            if (type == 33 || type == 36) {
                node = reference.getFirstChild();
                final Node lastChild = reference.getLastChild();
                int n2;
                if (type == 33) {
                    n2 = 139;
                }
                else {
                    n2 = 140;
                }
                return new Node(n2, node, lastChild, new Node(n, new Node(138), node2));
            }
            if (type == 39) {
                return new Node(8, Node.newString(49, reference.getString()), new Node(n, reference, node2));
            }
            if (type != 67) {
                throw Kit.codeBug();
            }
            node = reference.getFirstChild();
            this.checkMutableReference(node);
            return new Node(142, node, new Node(n, new Node(138), node2));
        }
    }
    
    private Node createBinary(final int n, final Node node, final Node node2) {
        Label_0458: {
            switch (n) {
                default: {
                    switch (n) {
                        default: {
                            break Label_0458;
                        }
                        case 105: {
                            final int alwaysDefinedBoolean = isAlwaysDefinedBoolean(node);
                            if (alwaysDefinedBoolean == -1) {
                                return node;
                            }
                            if (alwaysDefinedBoolean == 1) {
                                return node2;
                            }
                            break Label_0458;
                        }
                        case 104: {
                            final int alwaysDefinedBoolean2 = isAlwaysDefinedBoolean(node);
                            if (alwaysDefinedBoolean2 == 1) {
                                return node;
                            }
                            if (alwaysDefinedBoolean2 == -1) {
                                return node2;
                            }
                            break Label_0458;
                        }
                    }
                    break;
                }
                case 24: {
                    if (node2.type != 40) {
                        break;
                    }
                    final double double1 = node2.getDouble();
                    if (node.type == 40) {
                        node.setDouble(node.getDouble() / double1);
                        return node;
                    }
                    if (double1 == 1.0) {
                        return new Node(28, node);
                    }
                    break;
                }
                case 23: {
                    if (node.type == 40) {
                        final double double2 = node.getDouble();
                        if (node2.type == 40) {
                            node.setDouble(node2.getDouble() * double2);
                            return node;
                        }
                        if (double2 == 1.0) {
                            return new Node(28, node2);
                        }
                        break;
                    }
                    else {
                        if (node2.type == 40 && node2.getDouble() == 1.0) {
                            return new Node(28, node);
                        }
                        break;
                    }
                    break;
                }
                case 22: {
                    if (node.type == 40) {
                        final double double3 = node.getDouble();
                        if (node2.type == 40) {
                            node.setDouble(double3 - node2.getDouble());
                            return node;
                        }
                        if (double3 == 0.0) {
                            return new Node(29, node2);
                        }
                        break;
                    }
                    else {
                        if (node2.type == 40 && node2.getDouble() == 0.0) {
                            return new Node(28, node);
                        }
                        break;
                    }
                    break;
                }
                case 21: {
                    if (node.type == 41) {
                        String s;
                        if (node2.type == 41) {
                            s = node2.getString();
                        }
                        else {
                            if (node2.type != 40) {
                                break;
                            }
                            s = ScriptRuntime.numberToString(node2.getDouble(), 10);
                        }
                        node.setString(node.getString().concat(s));
                        return node;
                    }
                    if (node.type != 40) {
                        break;
                    }
                    if (node2.type == 40) {
                        node.setDouble(node.getDouble() + node2.getDouble());
                        return node;
                    }
                    if (node2.type == 41) {
                        node2.setString(ScriptRuntime.numberToString(node.getDouble(), 10).concat(node2.getString()));
                        return node2;
                    }
                    break;
                }
            }
        }
        return new Node(n, node, node2);
    }
    
    private Node createCallOrNew(final int n, final Node node) {
        final boolean b = false;
        int n2 = 0;
        if (node.getType() == 39) {
            final String string = node.getString();
            if (string.equals("eval")) {
                n2 = 1;
            }
            else if (string.equals("With")) {
                n2 = 2;
            }
        }
        else {
            n2 = (b ? 1 : 0);
            if (node.getType() == 33) {
                n2 = (b ? 1 : 0);
                if (node.getLastChild().getString().equals("eval")) {
                    n2 = 1;
                }
            }
        }
        final Node node2 = new Node(n, node);
        if (n2 != 0) {
            this.setRequiresActivation();
            node2.putIntProp(10, n2);
        }
        return node2;
    }
    
    private Node createCatch(final String s, final Node node, final Node node2, final int n) {
        Node node3 = node;
        if (node == null) {
            node3 = new Node(128);
        }
        return new Node(124, this.createName(s), node3, node2, n);
    }
    
    private Node createCondExpr(final Node node, final Node node2, final Node node3) {
        final int alwaysDefinedBoolean = isAlwaysDefinedBoolean(node);
        if (alwaysDefinedBoolean == 1) {
            return node2;
        }
        if (alwaysDefinedBoolean == -1) {
            return node3;
        }
        return new Node(102, node, node2, node3);
    }
    
    private Node createElementGet(final Node node, final String s, final Node node2, final int n) {
        if (s != null || n != 0) {
            return this.createMemberRefGet(node, s, node2, n);
        }
        if (node == null) {
            throw Kit.codeBug();
        }
        return new Node(36, node, node2);
    }
    
    private Node createExprStatementNoReturn(final Node node, final int n) {
        return new Node(133, node, n);
    }
    
    private Node createFor(final Scope scope, final Node node, final Node node2, final Node node3, final Node node4) {
        if (node.getType() == 153) {
            final Scope splitScope = Scope.splitScope(scope);
            splitScope.setType(153);
            splitScope.addChildrenToBack(node);
            splitScope.addChildToBack(this.createLoop(scope, 2, node4, node2, new Node(128), node3));
            return splitScope;
        }
        return this.createLoop(scope, 2, node4, node2, node, node3);
    }
    
    private Node createForIn(final int n, Node loop, final Node node, Node node2, final Node node3, final boolean b) {
        int n2 = -1;
        int n3 = 0;
        final boolean b2 = false;
        int type = node.getType();
        int n4;
        Node string;
        if (type != 122 && type != 153) {
            if (type != 65 && type != 66) {
                final Node reference = this.makeReference(node);
                n3 = (b2 ? 1 : 0);
                n4 = type;
                if ((string = reference) == null) {
                    this.reportError("msg.bad.for.in.lhs");
                    return null;
                }
            }
            else {
                final int n5 = type;
                n3 = 0;
                n2 = n5;
                n4 = type;
                string = node;
                if (node instanceof ArrayLiteral) {
                    n3 = ((ArrayLiteral)node).getDestructuringLength();
                    string = node;
                    n4 = type;
                    n2 = n5;
                }
            }
        }
        else {
            final Node lastChild = node.getLastChild();
            final int type2 = lastChild.getType();
            if (type2 != 65 && type2 != 66) {
                if (type2 != 39) {
                    this.reportError("msg.bad.for.in.lhs");
                    return null;
                }
                string = Node.newString(39, lastChild.getString());
            }
            else {
                final int n6 = type2;
                final Node node4 = lastChild;
                n3 = 0;
                n2 = n6;
                type = type2;
                string = node4;
                if (lastChild instanceof ArrayLiteral) {
                    n3 = ((ArrayLiteral)lastChild).getDestructuringLength();
                    string = node4;
                    type = type2;
                    n2 = n6;
                }
            }
            n4 = type;
        }
        final Node node5 = new Node(141);
        int n7;
        if (b) {
            n7 = 59;
        }
        else if (n2 != -1) {
            n7 = 60;
        }
        else {
            n7 = 58;
        }
        final Node node6 = new Node(n7, node2);
        node6.putProp(3, node5);
        final Node node7 = new Node(61);
        node7.putProp(3, node5);
        final Node node8 = new Node(62);
        node8.putProp(3, node5);
        final Node node9 = new Node(129);
        if (n2 != -1) {
            node2 = this.createDestructuringAssignment(n, string, node8);
            if (!b && (n2 == 66 || n3 != 2)) {
                this.reportError("msg.bad.for.in.destruct");
            }
        }
        else {
            node2 = this.simpleAssignment(string, node8);
        }
        node9.addChildToBack(new Node(133, node2));
        node9.addChildToBack(node3);
        loop = this.createLoop((Jump)loop, 1, node9, node7, null, null);
        loop.addChildToFront(node6);
        if (n4 == 122 || n4 == 153) {
            loop.addChildToFront(node);
        }
        node5.addChildToBack(loop);
        return node5;
    }
    
    private Node createIf(Node target, final Node node, final Node node2, final int n) {
        final int alwaysDefinedBoolean = isAlwaysDefinedBoolean(target);
        if (alwaysDefinedBoolean == 1) {
            return node;
        }
        if (alwaysDefinedBoolean == -1) {
            if (node2 != null) {
                return node2;
            }
            return new Node(129, n);
        }
        else {
            final Node node3 = new Node(129, n);
            final Node target2 = Node.newTarget();
            final Jump jump = new Jump(7, target);
            jump.target = target2;
            node3.addChildToBack(jump);
            node3.addChildrenToBack(node);
            if (node2 != null) {
                target = Node.newTarget();
                node3.addChildToBack(this.makeJump(5, target));
                node3.addChildToBack(target2);
                node3.addChildrenToBack(node2);
                node3.addChildToBack(target);
                return node3;
            }
            node3.addChildToBack(target2);
            return node3;
        }
    }
    
    private Node createIncDec(int n, final boolean b, Node reference) {
        reference = this.makeReference(reference);
        final int type = reference.getType();
        if (type != 33 && type != 36 && type != 39 && type != 67) {
            throw Kit.codeBug();
        }
        reference = new Node(n, reference);
        int n2 = 0;
        if (n == 107) {
            n2 = ((false | true) ? 1 : 0);
        }
        n = n2;
        if (b) {
            n = (n2 | 0x2);
        }
        reference.putIntProp(13, n);
        return reference;
    }
    
    private Node createLoop(final Jump jump, int type, Node continue1, Node target, Node node, final Node node2) {
        final Node target2 = Node.newTarget();
        final Node target3 = Node.newTarget();
        if (type == 2 && target.getType() == 128) {
            target = new Node(45);
        }
        final Jump jump2 = new Jump(6, target);
        jump2.target = target2;
        final Node target4 = Node.newTarget();
        jump.addChildToBack(target2);
        jump.addChildrenToBack(continue1);
        if (type == 1 || type == 2) {
            jump.addChildrenToBack(new Node(128, jump.getLineno()));
        }
        jump.addChildToBack(target3);
        jump.addChildToBack(jump2);
        jump.addChildToBack(target4);
        jump.target = target4;
        target = target3;
        Label_0269: {
            if (type == 1 || type == 2) {
                jump.addChildToFront(this.makeJump(5, target3));
                if (type == 2) {
                    type = node.getType();
                    if (type != 128) {
                        if (type != 122 && type != 153) {
                            node = new Node(133, node);
                        }
                        jump.addChildToFront(node);
                    }
                    target = Node.newTarget();
                    jump.addChildAfter(target, continue1);
                    if (node2.getType() != 128) {
                        jump.addChildAfter(new Node(133, node2), target);
                    }
                    continue1 = target;
                    break Label_0269;
                }
            }
            continue1 = target;
        }
        jump.setContinue(continue1);
        return jump;
    }
    
    private Scope createLoopNode(final Node node, final int n) {
        final Scope scopeNode = this.createScopeNode(132, n);
        if (node != null) {
            ((Jump)node).setLoop(scopeNode);
        }
        return scopeNode;
    }
    
    private Node createMemberRefGet(final Node node, final String s, final Node node2, final int n) {
        Node name = null;
        if (s != null) {
            if (s.equals("*")) {
                name = new Node(42);
            }
            else {
                name = this.createName(s);
            }
        }
        Node node3;
        if (node == null) {
            if (s == null) {
                node3 = new Node(79, node2);
            }
            else {
                node3 = new Node(80, name, node2);
            }
        }
        else if (s == null) {
            node3 = new Node(77, node, node2);
        }
        else {
            node3 = new Node(78, node, name, node2);
        }
        if (n != 0) {
            node3.putIntProp(16, n);
        }
        return new Node(67, node3);
    }
    
    private Node createPropertyGet(Node node, final String s, final String s2, final int n) {
        if (s != null || n != 0) {
            return this.createMemberRefGet(node, s, Node.newString(s2), n | 0x1);
        }
        if (node == null) {
            return this.createName(s2);
        }
        this.checkActivationName(s2, 33);
        if (ScriptRuntime.isSpecialProperty(s2)) {
            node = new Node(71, node);
            node.putProp(17, s2);
            return new Node(67, node);
        }
        return new Node(33, node, Node.newString(s2));
    }
    
    private Node createString(final String s) {
        return Node.newString(s);
    }
    
    private Node createTryCatchFinally(Node target, Node finally1, final Node node, int n) {
        final int n2 = 0;
        final boolean b = node != null && (node.getType() != 129 || node.hasChildren());
        if (target.getType() == 129 && !target.hasChildren() && !b) {
            return target;
        }
        final boolean hasChildren = finally1.hasChildren();
        if (!b && !hasChildren) {
            return target;
        }
        final Node node2 = new Node(141);
        final Jump jump = new Jump(81, target, n);
        jump.putProp(3, node2);
        if (hasChildren) {
            final Node target2 = Node.newTarget();
            jump.addChildToBack(this.makeJump(5, target2));
            target = Node.newTarget();
            jump.addChildToBack(jump.target = target);
            final Node node3 = new Node(141);
            finally1 = finally1.getFirstChild();
            boolean b2 = false;
            int lineno;
            Node firstChild;
            Node next;
            Node node4;
            Node node5;
            for (n = n2; finally1 != null; finally1 = finally1.getNext(), ++n) {
                lineno = finally1.getLineno();
                firstChild = finally1.getFirstChild();
                next = firstChild.getNext();
                node4 = next.getNext();
                finally1.removeChild(firstChild);
                finally1.removeChild(next);
                finally1.removeChild(node4);
                node4.addChildToBack(new Node(3));
                node4.addChildToBack(this.makeJump(5, target2));
                if (next.getType() == 128) {
                    b2 = true;
                }
                else {
                    node4 = this.createIf(next, node4, null, lineno);
                }
                node5 = new Node(57, firstChild, this.createUseLocal(node2));
                node5.putProp(3, node3);
                node5.putIntProp(14, n);
                node3.addChildToBack(node5);
                node3.addChildToBack(this.createWith(this.createUseLocal(node3), node4, lineno));
            }
            jump.addChildToBack(node3);
            if (!b2) {
                final Node node6 = new Node(51);
                node6.putProp(3, node2);
                jump.addChildToBack(node6);
            }
            jump.addChildToBack(target2);
        }
        if (b) {
            finally1 = Node.newTarget();
            jump.setFinally(finally1);
            jump.addChildToBack(this.makeJump(135, finally1));
            target = Node.newTarget();
            jump.addChildToBack(this.makeJump(5, target));
            jump.addChildToBack(finally1);
            final Node node7 = new Node(125, node);
            node7.putProp(3, node2);
            jump.addChildToBack(node7);
            jump.addChildToBack(target);
        }
        node2.addChildToBack(jump);
        return node2;
    }
    
    private Node createUnary(int type, final Node node) {
        final int type2 = node.getType();
        switch (type) {
            case 32: {
                if (type2 == 39) {
                    node.setType(137);
                    return node;
                }
                break;
            }
            case 31: {
                if (type2 == 39) {
                    node.setType(49);
                    return new Node(type, node, Node.newString(node.getString()));
                }
                if (type2 == 33 || type2 == 36) {
                    final Node firstChild = node.getFirstChild();
                    final Node lastChild = node.getLastChild();
                    node.removeChild(firstChild);
                    node.removeChild(lastChild);
                    return new Node(type, firstChild, lastChild);
                }
                if (type2 == 67) {
                    final Node firstChild2 = node.getFirstChild();
                    node.removeChild(firstChild2);
                    return new Node(69, firstChild2);
                }
                return new Node(type, new Node(45), node);
            }
            case 29: {
                if (type2 == 40) {
                    node.setDouble(-node.getDouble());
                    return node;
                }
                break;
            }
            case 27: {
                if (type2 == 40) {
                    node.setDouble(~ScriptRuntime.toInt32(node.getDouble()));
                    return node;
                }
                break;
            }
            case 26: {
                final int alwaysDefinedBoolean = isAlwaysDefinedBoolean(node);
                if (alwaysDefinedBoolean == 0) {
                    break;
                }
                if (alwaysDefinedBoolean == 1) {
                    type = 44;
                }
                else {
                    type = 45;
                }
                if (type2 != 45 && type2 != 44) {
                    return new Node(type);
                }
                node.setType(type);
                return node;
            }
        }
        return new Node(type, node);
    }
    
    private Node createUseLocal(final Node node) {
        if (141 != node.getType()) {
            throw Kit.codeBug();
        }
        final Node node2 = new Node(54);
        node2.putProp(3, node);
        return node2;
    }
    
    private Node createWith(final Node node, final Node node2, final int n) {
        this.setRequiresActivation();
        final Node node3 = new Node(129, n);
        node3.addChildToBack(new Node(2, node));
        node3.addChildrenToBack(new Node(123, node2, n));
        node3.addChildToBack(new Node(3));
        return node3;
    }
    
    private Node genExprTransformHelper(final GeneratorExpression generatorExpression) {
        this.decompiler.addToken(87);
        final int lineno = generatorExpression.getLineno();
        Node node = this.transform(generatorExpression.getResult());
        final List<GeneratorExpressionLoop> loops = generatorExpression.getLoops();
        final int size = loops.size();
        final Node[] array = new Node[size];
        final Node[] array2 = new Node[size];
        for (int i = 0; i < size; ++i) {
            final GeneratorExpressionLoop generatorExpressionLoop = loops.get(i);
            this.decompiler.addName(" ");
            this.decompiler.addToken(119);
            this.decompiler.addToken(87);
            final AstNode iterator = generatorExpressionLoop.getIterator();
            String s;
            if (iterator.getType() == 39) {
                s = iterator.getString();
                this.decompiler.addName(s);
            }
            else {
                this.decompile(iterator);
                s = this.currentScriptOrFn.getNextTempName();
                this.defineSymbol(87, s, false);
                node = this.createBinary(89, this.createAssignment(90, iterator, this.createName(s)), node);
            }
            final Node name = this.createName(s);
            this.defineSymbol(153, s, false);
            array[i] = name;
            this.decompiler.addToken(52);
            array2[i] = this.transform(generatorExpressionLoop.getIteratedObject());
            this.decompiler.addToken(88);
        }
        final Node node2 = new Node(133, new Node(72, node, generatorExpression.getLineno()), lineno);
        final AstNode filter = generatorExpression.getFilter();
        final Node node3 = null;
        Node if1;
        if (filter != null) {
            this.decompiler.addName(" ");
            this.decompiler.addToken(112);
            this.decompiler.addToken(87);
            if1 = this.createIf(this.transform(generatorExpression.getFilter()), node2, null, lineno);
            this.decompiler.addToken(88);
        }
        else {
            if1 = node2;
        }
        int n = 0;
        int j = size - 1;
        Node forIn = if1;
        final Node node4 = node3;
        while (j >= 0) {
            try {
                final GeneratorExpressionLoop generatorExpressionLoop2 = loops.get(j);
                final Scope loopNode = this.createLoopNode(node4, generatorExpressionLoop2.getLineno());
                this.pushScope(loopNode);
                ++n;
                final Node node5 = array[j];
                final Node node6 = array2[j];
                try {
                    final boolean forEach = generatorExpressionLoop2.isForEach();
                    try {
                        forIn = this.createForIn(153, loopNode, node5, node6, forIn, forEach);
                        --j;
                    }
                    finally {}
                }
                finally {}
            }
            finally {}
            for (int k = 0; k < n; ++k) {
                this.popScope();
            }
        }
        for (int l = 0; l < n; ++l) {
            this.popScope();
        }
        this.decompiler.addToken(88);
        return forIn;
    }
    
    private Object getPropKey(final Node node) {
        if (node instanceof Name) {
            final String identifier = ((Name)node).getIdentifier();
            this.decompiler.addName(identifier);
            return ScriptRuntime.getIndexObject(identifier);
        }
        if (node instanceof StringLiteral) {
            final String value = ((StringLiteral)node).getValue();
            this.decompiler.addString(value);
            return ScriptRuntime.getIndexObject(value);
        }
        if (node instanceof NumberLiteral) {
            final double number = ((NumberLiteral)node).getNumber();
            this.decompiler.addNumber(number);
            return ScriptRuntime.getIndexObject(number);
        }
        throw Kit.codeBug();
    }
    
    private Node initFunction(final FunctionNode functionNode, final int n, final Node node, final int functionType) {
        functionNode.setFunctionType(functionType);
        functionNode.addChildToBack(node);
        if (functionNode.getFunctionCount() != 0) {
            functionNode.setRequiresActivation();
        }
        if (functionType == 2) {
            final Name functionName = functionNode.getFunctionName();
            if (functionName != null && functionName.length() != 0 && functionNode.getSymbol(functionName.getIdentifier()) == null) {
                functionNode.putSymbol(new Symbol(109, functionName.getIdentifier()));
                node.addChildrenToFront(new Node(133, new Node(8, Node.newString(49, functionName.getIdentifier()), new Node(63))));
            }
        }
        final Node lastChild = node.getLastChild();
        if (lastChild == null || lastChild.getType() != 4) {
            node.addChildToBack(new Node(4));
        }
        final Node string = Node.newString(109, functionNode.getName());
        string.putIntProp(1, n);
        return string;
    }
    
    private static int isAlwaysDefinedBoolean(final Node node) {
        switch (node.getType()) {
            default: {
                return 0;
            }
            case 45: {
                return 1;
            }
            case 42:
            case 44: {
                return -1;
            }
            case 40: {
                final double double1 = node.getDouble();
                if (double1 == double1 && double1 != 0.0) {
                    return 1;
                }
                return -1;
            }
        }
    }
    
    private Jump makeJump(final int n, final Node target) {
        final Jump jump = new Jump(n);
        jump.target = target;
        return jump;
    }
    
    private Node makeReference(final Node node) {
        final int type = node.getType();
        if (type != 33 && type != 36 && type != 67) {
            switch (type) {
                default: {
                    return null;
                }
                case 38: {
                    node.setType(70);
                    return new Node(67, node);
                }
                case 39: {
                    break;
                }
            }
        }
        return node;
    }
    
    private Node transformArrayComp(final ArrayComprehension arrayComprehension) {
        final int lineno = arrayComprehension.getLineno();
        final Scope scopeNode = this.createScopeNode(157, lineno);
        final String nextTempName = this.currentScriptOrFn.getNextTempName();
        this.pushScope(scopeNode);
        try {
            this.defineSymbol(153, nextTempName, false);
            final Node node = new Node(129, lineno);
            node.addChildToBack(new Node(133, this.createAssignment(90, this.createName(nextTempName), this.createCallOrNew(30, this.createName("Array"))), lineno));
            node.addChildToBack(this.arrayCompTransformHelper(arrayComprehension, nextTempName));
            scopeNode.addChildToBack(node);
            scopeNode.addChildToBack(this.createName(nextTempName));
            return scopeNode;
        }
        finally {
            this.popScope();
        }
    }
    
    private Node transformArrayLiteral(final ArrayLiteral arrayLiteral) {
        if (arrayLiteral.isDestructuring()) {
            return arrayLiteral;
        }
        this.decompiler.addToken(83);
        final List<AstNode> elements = arrayLiteral.getElements();
        final Node node = new Node(65);
        final int n = 0;
        List<Integer> list = null;
        for (int i = 0; i < elements.size(); ++i) {
            final AstNode astNode = elements.get(i);
            if (astNode.getType() != 128) {
                node.addChildToBack(this.transform(astNode));
            }
            else {
                List<Integer> list2;
                if ((list2 = list) == null) {
                    list2 = new ArrayList<Integer>();
                }
                list2.add(i);
                list = list2;
            }
            if (i < elements.size() - 1) {
                this.decompiler.addToken(89);
            }
        }
        this.decompiler.addToken(84);
        node.putIntProp(21, arrayLiteral.getDestructuringLength());
        if (list != null) {
            final int[] array = new int[list.size()];
            for (int j = n; j < list.size(); ++j) {
                array[j] = list.get(j);
            }
            node.putProp(11, array);
        }
        return node;
    }
    
    private Node transformAssignment(final Assignment assignment) {
        Node node = this.removeParens(assignment.getLeft());
        if (this.isDestructuring(node)) {
            this.decompile((AstNode)node);
        }
        else {
            node = this.transform((AstNode)node);
        }
        this.decompiler.addToken(assignment.getType());
        return this.createAssignment(assignment.getType(), node, this.transform(assignment.getRight()));
    }
    
    private Node transformBlock(final AstNode astNode) {
        if (astNode instanceof Scope) {
            this.pushScope((Scope)astNode);
        }
        try {
            final ArrayList<Node> list = new ArrayList<Node>();
            final Iterator<Node> iterator = astNode.iterator();
            while (iterator.hasNext()) {
                list.add(this.transform((AstNode)iterator.next()));
            }
            astNode.removeChildren();
            final Iterator<Object> iterator2 = list.iterator();
            while (iterator2.hasNext()) {
                astNode.addChildToBack(iterator2.next());
            }
            return astNode;
        }
        finally {
            if (astNode instanceof Scope) {
                this.popScope();
            }
        }
    }
    
    private Node transformBreak(final BreakStatement breakStatement) {
        this.decompiler.addToken(120);
        if (breakStatement.getBreakLabel() != null) {
            this.decompiler.addName(breakStatement.getBreakLabel().getIdentifier());
        }
        this.decompiler.addEOL(82);
        return breakStatement;
    }
    
    private Node transformCondExpr(final ConditionalExpression conditionalExpression) {
        final Node transform = this.transform(conditionalExpression.getTestExpression());
        this.decompiler.addToken(102);
        final Node transform2 = this.transform(conditionalExpression.getTrueExpression());
        this.decompiler.addToken(103);
        return this.createCondExpr(transform, transform2, this.transform(conditionalExpression.getFalseExpression()));
    }
    
    private Node transformContinue(final ContinueStatement continueStatement) {
        this.decompiler.addToken(121);
        if (continueStatement.getLabel() != null) {
            this.decompiler.addName(continueStatement.getLabel().getIdentifier());
        }
        this.decompiler.addEOL(82);
        return continueStatement;
    }
    
    private Node transformDefaultXmlNamepace(final UnaryExpression unaryExpression) {
        this.decompiler.addToken(116);
        this.decompiler.addName(" xml");
        this.decompiler.addName(" namespace");
        this.decompiler.addToken(90);
        return this.createUnary(74, this.transform(unaryExpression.getOperand()));
    }
    
    private Node transformDoLoop(final DoLoop doLoop) {
        doLoop.setType(132);
        this.pushScope(doLoop);
        try {
            this.decompiler.addToken(118);
            this.decompiler.addEOL(85);
            final Node transform = this.transform(doLoop.getBody());
            this.decompiler.addToken(86);
            this.decompiler.addToken(117);
            this.decompiler.addToken(87);
            final Node transform2 = this.transform(doLoop.getCondition());
            this.decompiler.addToken(88);
            this.decompiler.addEOL(82);
            return this.createLoop(doLoop, 0, transform, transform2, null, null);
        }
        finally {
            this.popScope();
        }
    }
    
    private Node transformElementGet(final ElementGet elementGet) {
        final Node transform = this.transform(elementGet.getTarget());
        this.decompiler.addToken(83);
        final Node transform2 = this.transform(elementGet.getElement());
        this.decompiler.addToken(84);
        return new Node(36, transform, transform2);
    }
    
    private Node transformExprStmt(final ExpressionStatement expressionStatement) {
        final Node transform = this.transform(expressionStatement.getExpression());
        this.decompiler.addEOL(82);
        return new Node(expressionStatement.getType(), transform, expressionStatement.getLineno());
    }
    
    private Node transformForInLoop(final ForInLoop forInLoop) {
        this.decompiler.addToken(119);
        if (forInLoop.isForEach()) {
            this.decompiler.addName("each ");
        }
        this.decompiler.addToken(87);
        forInLoop.setType(132);
        this.pushScope(forInLoop);
        int type = -1;
        try {
            final AstNode iterator = forInLoop.getIterator();
            if (iterator instanceof VariableDeclaration) {
                type = ((VariableDeclaration)iterator).getType();
            }
            final Node transform = this.transform(iterator);
            this.decompiler.addToken(52);
            final Node transform2 = this.transform(forInLoop.getIteratedObject());
            this.decompiler.addToken(88);
            this.decompiler.addEOL(85);
            final Node transform3 = this.transform(forInLoop.getBody());
            this.decompiler.addEOL(86);
            return this.createForIn(type, forInLoop, transform, transform2, transform3, forInLoop.isForEach());
        }
        finally {
            this.popScope();
        }
    }
    
    private Node transformForLoop(final ForLoop currentScope) {
        this.decompiler.addToken(119);
        this.decompiler.addToken(87);
        currentScope.setType(132);
        final Scope currentScope2 = this.currentScope;
        this.currentScope = currentScope;
        try {
            final Node transform = this.transform(currentScope.getInitializer());
            this.decompiler.addToken(82);
            final Node transform2 = this.transform(currentScope.getCondition());
            this.decompiler.addToken(82);
            final Node transform3 = this.transform(currentScope.getIncrement());
            this.decompiler.addToken(88);
            this.decompiler.addEOL(85);
            final Node transform4 = this.transform(currentScope.getBody());
            this.decompiler.addEOL(86);
            return this.createFor(currentScope, transform, transform2, transform3, transform4);
        }
        finally {
            this.currentScope = currentScope2;
        }
    }
    
    private Node transformFunction(final FunctionNode functionNode) {
        final int functionType = functionNode.getFunctionType();
        final int markFunctionStart = this.decompiler.markFunctionStart(functionType);
        final Node decompileFunctionHeader = this.decompileFunctionHeader(functionNode);
        final int addFunction = this.currentScriptOrFn.addFunction(functionNode);
        final PerFunctionVariables perFunctionVariables = new PerFunctionVariables(functionNode);
        try {
            final Node node = (Node)functionNode.getProp(23);
            functionNode.removeProp(23);
            final int lineno = functionNode.getBody().getLineno();
            ++this.nestingOfFunction;
            final Node transform = this.transform(functionNode.getBody());
            if (!functionNode.isExpressionClosure()) {
                this.decompiler.addToken(86);
            }
            functionNode.setEncodedSourceBounds(markFunctionStart, this.decompiler.markFunctionEnd(markFunctionStart));
            if (functionType != 2 && !functionNode.isExpressionClosure()) {
                this.decompiler.addToken(1);
            }
            if (node != null) {
                transform.addChildToFront(new Node(133, node, lineno));
            }
            final int functionType2 = functionNode.getFunctionType();
            Node node2 = this.initFunction(functionNode, addFunction, transform, functionType2);
            if (decompileFunctionHeader != null) {
                final Node node3 = node2 = this.createAssignment(90, decompileFunctionHeader, node2);
                if (functionType2 != 2) {
                    node2 = this.createExprStatementNoReturn(node3, functionNode.getLineno());
                }
            }
            return node2;
        }
        finally {
            --this.nestingOfFunction;
            perFunctionVariables.restore();
        }
    }
    
    private Node transformFunctionCall(final FunctionCall functionCall) {
        final Node callOrNew = this.createCallOrNew(38, this.transform(functionCall.getTarget()));
        callOrNew.setLineno(functionCall.getLineno());
        this.decompiler.addToken(87);
        final List<AstNode> arguments = functionCall.getArguments();
        for (int i = 0; i < arguments.size(); ++i) {
            callOrNew.addChildToBack(this.transform(arguments.get(i)));
            if (i < arguments.size() - 1) {
                this.decompiler.addToken(89);
            }
        }
        this.decompiler.addToken(88);
        return callOrNew;
    }
    
    private Node transformGenExpr(final GeneratorExpression generatorExpression) {
        final FunctionNode functionNode = new FunctionNode();
        functionNode.setSourceName(this.currentScriptOrFn.getNextTempName());
        functionNode.setIsGenerator();
        functionNode.setFunctionType(2);
        functionNode.setRequiresActivation();
        final int functionType = functionNode.getFunctionType();
        final int markFunctionStart = this.decompiler.markFunctionStart(functionType);
        final Node decompileFunctionHeader = this.decompileFunctionHeader(functionNode);
        final int addFunction = this.currentScriptOrFn.addFunction(functionNode);
        final PerFunctionVariables perFunctionVariables = new PerFunctionVariables(functionNode);
        try {
            final Node node = (Node)functionNode.getProp(23);
            functionNode.removeProp(23);
            final int lineno = generatorExpression.lineno;
            ++this.nestingOfFunction;
            final Node genExprTransformHelper = this.genExprTransformHelper(generatorExpression);
            if (!functionNode.isExpressionClosure()) {
                this.decompiler.addToken(86);
            }
            functionNode.setEncodedSourceBounds(markFunctionStart, this.decompiler.markFunctionEnd(markFunctionStart));
            if (functionType != 2 && !functionNode.isExpressionClosure()) {
                this.decompiler.addToken(1);
            }
            if (node != null) {
                genExprTransformHelper.addChildToFront(new Node(133, node, lineno));
            }
            final int functionType2 = functionNode.getFunctionType();
            Node node2 = this.initFunction(functionNode, addFunction, genExprTransformHelper, functionType2);
            if (decompileFunctionHeader != null) {
                final Node node3 = node2 = this.createAssignment(90, decompileFunctionHeader, node2);
                if (functionType2 != 2) {
                    node2 = this.createExprStatementNoReturn(node3, functionNode.getLineno());
                }
            }
            --this.nestingOfFunction;
            perFunctionVariables.restore();
            final Node callOrNew = this.createCallOrNew(38, node2);
            callOrNew.setLineno(generatorExpression.getLineno());
            this.decompiler.addToken(87);
            this.decompiler.addToken(88);
            return callOrNew;
        }
        finally {
            --this.nestingOfFunction;
            perFunctionVariables.restore();
        }
    }
    
    private Node transformIf(final IfStatement ifStatement) {
        this.decompiler.addToken(112);
        this.decompiler.addToken(87);
        final Node transform = this.transform(ifStatement.getCondition());
        this.decompiler.addToken(88);
        this.decompiler.addEOL(85);
        final Node transform2 = this.transform(ifStatement.getThenPart());
        Node transform3 = null;
        if (ifStatement.getElsePart() != null) {
            this.decompiler.addToken(86);
            this.decompiler.addToken(113);
            this.decompiler.addEOL(85);
            transform3 = this.transform(ifStatement.getElsePart());
        }
        this.decompiler.addEOL(86);
        return this.createIf(transform, transform2, transform3, ifStatement.getLineno());
    }
    
    private Node transformInfix(final InfixExpression infixExpression) {
        final Node transform = this.transform(infixExpression.getLeft());
        this.decompiler.addToken(infixExpression.getType());
        final Node transform2 = this.transform(infixExpression.getRight());
        if (infixExpression instanceof XmlDotQuery) {
            this.decompiler.addToken(88);
        }
        return this.createBinary(infixExpression.getType(), transform, transform2);
    }
    
    private Node transformLabeledStatement(final LabeledStatement labeledStatement) {
        final Label firstLabel = labeledStatement.getFirstLabel();
        final List<Label> labels = labeledStatement.getLabels();
        this.decompiler.addName(firstLabel.getName());
        if (labels.size() > 1) {
            for (final Label label : labels.subList(1, labels.size())) {
                this.decompiler.addEOL(103);
                this.decompiler.addName(label.getName());
            }
        }
        if (labeledStatement.getStatement().getType() == 129) {
            this.decompiler.addToken(66);
            this.decompiler.addEOL(85);
        }
        else {
            this.decompiler.addEOL(103);
        }
        final Node transform = this.transform(labeledStatement.getStatement());
        if (labeledStatement.getStatement().getType() == 129) {
            this.decompiler.addEOL(86);
        }
        final Node target = Node.newTarget();
        final Node node = new Node(129, firstLabel, transform, target);
        firstLabel.target = target;
        return node;
    }
    
    private Node transformLetNode(final LetNode letNode) {
        while (true) {
            this.pushScope(letNode);
            while (true) {
                try {
                    this.decompiler.addToken(153);
                    this.decompiler.addToken(87);
                    final Node transformVariableInitializers = this.transformVariableInitializers(letNode.getVariables());
                    this.decompiler.addToken(88);
                    letNode.addChildToBack(transformVariableInitializers);
                    if (letNode.getType() == 158) {
                        final int n = 1;
                        if (letNode.getBody() != null) {
                            if (n != 0) {
                                this.decompiler.addName(" ");
                            }
                            else {
                                this.decompiler.addEOL(85);
                            }
                            letNode.addChildToBack(this.transform(letNode.getBody()));
                            if (n == 0) {
                                this.decompiler.addEOL(86);
                            }
                        }
                        return letNode;
                    }
                }
                finally {
                    this.popScope();
                }
                final int n = 0;
                continue;
            }
        }
    }
    
    private Node transformLiteral(final AstNode astNode) {
        this.decompiler.addToken(astNode.getType());
        return astNode;
    }
    
    private Node transformName(final Name name) {
        this.decompiler.addName(name.getIdentifier());
        return name;
    }
    
    private Node transformNewExpr(final NewExpression newExpression) {
        this.decompiler.addToken(30);
        final Node callOrNew = this.createCallOrNew(30, this.transform(newExpression.getTarget()));
        callOrNew.setLineno(newExpression.getLineno());
        final List<AstNode> arguments = newExpression.getArguments();
        this.decompiler.addToken(87);
        for (int i = 0; i < arguments.size(); ++i) {
            callOrNew.addChildToBack(this.transform(arguments.get(i)));
            if (i < arguments.size() - 1) {
                this.decompiler.addToken(89);
            }
        }
        this.decompiler.addToken(88);
        if (newExpression.getInitializer() != null) {
            callOrNew.addChildToBack(this.transformObjectLiteral(newExpression.getInitializer()));
        }
        return callOrNew;
    }
    
    private Node transformNumber(final NumberLiteral numberLiteral) {
        this.decompiler.addNumber(numberLiteral.getNumber());
        return numberLiteral;
    }
    
    private Node transformObjectLiteral(final ObjectLiteral objectLiteral) {
        if (objectLiteral.isDestructuring()) {
            return objectLiteral;
        }
        this.decompiler.addToken(85);
        final List<ObjectProperty> elements = objectLiteral.getElements();
        final Node node = new Node(66);
        Object[] emptyArgs;
        if (elements.isEmpty()) {
            emptyArgs = ScriptRuntime.emptyArgs;
        }
        else {
            final int size = elements.size();
            int n = 0;
            final Object[] array = new Object[size];
            for (final ObjectProperty objectProperty : elements) {
                if (objectProperty.isGetterMethod()) {
                    this.decompiler.addToken(151);
                }
                else if (objectProperty.isSetterMethod()) {
                    this.decompiler.addToken(152);
                }
                else if (objectProperty.isNormalMethod()) {
                    this.decompiler.addToken(163);
                }
                final int n2 = n + 1;
                array[n] = this.getPropKey(objectProperty.getLeft());
                if (!objectProperty.isMethod()) {
                    this.decompiler.addToken(66);
                }
                final Node transform = this.transform(objectProperty.getRight());
                Node node2;
                if (objectProperty.isGetterMethod()) {
                    node2 = this.createUnary(151, transform);
                }
                else if (objectProperty.isSetterMethod()) {
                    node2 = this.createUnary(152, transform);
                }
                else {
                    node2 = transform;
                    if (objectProperty.isNormalMethod()) {
                        node2 = this.createUnary(163, transform);
                    }
                }
                node.addChildToBack(node2);
                if (n2 < size) {
                    this.decompiler.addToken(89);
                }
                n = n2;
            }
            emptyArgs = array;
        }
        this.decompiler.addToken(86);
        node.putProp(12, emptyArgs);
        return node;
    }
    
    private Node transformParenExpr(final ParenthesizedExpression parenthesizedExpression) {
        AstNode astNode = parenthesizedExpression.getExpression();
        this.decompiler.addToken(87);
        int n = 1;
        while (astNode instanceof ParenthesizedExpression) {
            this.decompiler.addToken(87);
            ++n;
            astNode = ((ParenthesizedExpression)astNode).getExpression();
        }
        final Node transform = this.transform(astNode);
        for (int i = 0; i < n; ++i) {
            this.decompiler.addToken(88);
        }
        transform.putProp(19, Boolean.TRUE);
        return transform;
    }
    
    private Node transformPropertyGet(final PropertyGet propertyGet) {
        final Node transform = this.transform(propertyGet.getTarget());
        final String identifier = propertyGet.getProperty().getIdentifier();
        this.decompiler.addToken(108);
        this.decompiler.addName(identifier);
        return this.createPropertyGet(transform, null, identifier, 0);
    }
    
    private Node transformRegExp(final RegExpLiteral regExpLiteral) {
        this.decompiler.addRegexp(regExpLiteral.getValue(), regExpLiteral.getFlags());
        this.currentScriptOrFn.addRegExp(regExpLiteral);
        return regExpLiteral;
    }
    
    private Node transformReturn(final ReturnStatement returnStatement) {
        final boolean equals = Boolean.TRUE.equals(returnStatement.getProp(25));
        if (equals) {
            this.decompiler.addName(" ");
        }
        else {
            this.decompiler.addToken(4);
        }
        final AstNode returnValue = returnStatement.getReturnValue();
        Node transform;
        if (returnValue == null) {
            transform = null;
        }
        else {
            transform = this.transform(returnValue);
        }
        if (!equals) {
            this.decompiler.addEOL(82);
        }
        if (returnValue == null) {
            return new Node(4, returnStatement.getLineno());
        }
        return new Node(4, transform, returnStatement.getLineno());
    }
    
    private Node transformScript(final ScriptNode currentScope) {
        this.decompiler.addToken(136);
        if (this.currentScope != null) {
            Kit.codeBug();
        }
        this.currentScope = currentScope;
        final Node node = new Node(129);
        final Iterator<Node> iterator = currentScope.iterator();
        while (iterator.hasNext()) {
            node.addChildToBack(this.transform((AstNode)iterator.next()));
        }
        currentScope.removeChildren();
        final Node firstChild = node.getFirstChild();
        if (firstChild != null) {
            currentScope.addChildrenToBack(firstChild);
        }
        return currentScope;
    }
    
    private Node transformString(final StringLiteral stringLiteral) {
        this.decompiler.addString(stringLiteral.getValue());
        return Node.newString(stringLiteral.getValue());
    }
    
    private Node transformSwitch(final SwitchStatement switchStatement) {
        this.decompiler.addToken(114);
        this.decompiler.addToken(87);
        final Node transform = this.transform(switchStatement.getExpression());
        this.decompiler.addToken(88);
        switchStatement.addChildToBack(transform);
        final Node node = new Node(129, switchStatement, switchStatement.getLineno());
        this.decompiler.addEOL(85);
        for (final SwitchCase switchCase : switchStatement.getCases()) {
            final AstNode expression = switchCase.getExpression();
            Node transform2 = null;
            if (expression != null) {
                this.decompiler.addToken(115);
                transform2 = this.transform(expression);
            }
            else {
                this.decompiler.addToken(116);
            }
            this.decompiler.addEOL(103);
            final List<AstNode> statements = switchCase.getStatements();
            final Block block = new Block();
            if (statements != null) {
                final Iterator<AstNode> iterator2 = statements.iterator();
                while (iterator2.hasNext()) {
                    block.addChildToBack(this.transform(iterator2.next()));
                }
            }
            this.addSwitchCase(node, transform2, block);
        }
        this.decompiler.addEOL(86);
        this.closeSwitch(node);
        return node;
    }
    
    private Node transformThrow(final ThrowStatement throwStatement) {
        this.decompiler.addToken(50);
        final Node transform = this.transform(throwStatement.getExpression());
        this.decompiler.addEOL(82);
        return new Node(50, transform, throwStatement.getLineno());
    }
    
    private Node transformTry(final TryStatement tryStatement) {
        this.decompiler.addToken(81);
        this.decompiler.addEOL(85);
        final Node transform = this.transform(tryStatement.getTryBlock());
        this.decompiler.addEOL(86);
        final Block block = new Block();
        for (final CatchClause catchClause : tryStatement.getCatchClauses()) {
            this.decompiler.addToken(124);
            this.decompiler.addToken(87);
            final String identifier = catchClause.getVarName().getIdentifier();
            this.decompiler.addName(identifier);
            final AstNode catchCondition = catchClause.getCatchCondition();
            Node transform2;
            if (catchCondition != null) {
                this.decompiler.addName(" ");
                this.decompiler.addToken(112);
                transform2 = this.transform(catchCondition);
            }
            else {
                transform2 = new EmptyExpression();
            }
            this.decompiler.addToken(88);
            this.decompiler.addEOL(85);
            final Node transform3 = this.transform(catchClause.getBody());
            this.decompiler.addEOL(86);
            block.addChildToBack(this.createCatch(identifier, transform2, transform3, catchClause.getLineno()));
        }
        Node transform4 = null;
        if (tryStatement.getFinallyBlock() != null) {
            this.decompiler.addToken(125);
            this.decompiler.addEOL(85);
            transform4 = this.transform(tryStatement.getFinallyBlock());
            this.decompiler.addEOL(86);
        }
        return this.createTryCatchFinally(transform, block, transform4, tryStatement.getLineno());
    }
    
    private Node transformUnary(final UnaryExpression unaryExpression) {
        final int type = unaryExpression.getType();
        if (type == 74) {
            return this.transformDefaultXmlNamepace(unaryExpression);
        }
        if (unaryExpression.isPrefix()) {
            this.decompiler.addToken(type);
        }
        final Node transform = this.transform(unaryExpression.getOperand());
        if (unaryExpression.isPostfix()) {
            this.decompiler.addToken(type);
        }
        if (type != 106 && type != 107) {
            return this.createUnary(type, transform);
        }
        return this.createIncDec(type, unaryExpression.isPostfix(), transform);
    }
    
    private Node transformVariableInitializers(final VariableDeclaration variableDeclaration) {
        final List<VariableInitializer> variables = variableDeclaration.getVariables();
        final int size = variables.size();
        int n = 0;
        for (final VariableInitializer variableInitializer : variables) {
            Node node = variableInitializer.getTarget();
            final AstNode initializer = variableInitializer.getInitializer();
            if (variableInitializer.isDestructuring()) {
                this.decompile((AstNode)node);
            }
            else {
                node = this.transform((AstNode)node);
            }
            Node transform = null;
            if (initializer != null) {
                this.decompiler.addToken(90);
                transform = this.transform(initializer);
            }
            if (variableInitializer.isDestructuring()) {
                if (transform == null) {
                    variableDeclaration.addChildToBack(node);
                }
                else {
                    variableDeclaration.addChildToBack(this.createDestructuringAssignment(variableDeclaration.getType(), node, transform));
                }
            }
            else {
                if (transform != null) {
                    node.addChildToBack(transform);
                }
                variableDeclaration.addChildToBack(node);
            }
            if (n < size - 1) {
                this.decompiler.addToken(89);
            }
            ++n;
        }
        return variableDeclaration;
    }
    
    private Node transformVariables(final VariableDeclaration variableDeclaration) {
        this.decompiler.addToken(variableDeclaration.getType());
        this.transformVariableInitializers(variableDeclaration);
        final AstNode parent = variableDeclaration.getParent();
        if (!(parent instanceof Loop) && !(parent instanceof LetNode)) {
            this.decompiler.addEOL(82);
        }
        return variableDeclaration;
    }
    
    private Node transformWhileLoop(final WhileLoop whileLoop) {
        this.decompiler.addToken(117);
        whileLoop.setType(132);
        this.pushScope(whileLoop);
        try {
            this.decompiler.addToken(87);
            final Node transform = this.transform(whileLoop.getCondition());
            this.decompiler.addToken(88);
            this.decompiler.addEOL(85);
            final Node transform2 = this.transform(whileLoop.getBody());
            this.decompiler.addEOL(86);
            return this.createLoop(whileLoop, 1, transform2, transform, null, null);
        }
        finally {
            this.popScope();
        }
    }
    
    private Node transformWith(final WithStatement withStatement) {
        this.decompiler.addToken(123);
        this.decompiler.addToken(87);
        final Node transform = this.transform(withStatement.getExpression());
        this.decompiler.addToken(88);
        this.decompiler.addEOL(85);
        final Node transform2 = this.transform(withStatement.getStatement());
        this.decompiler.addEOL(86);
        return this.createWith(transform, transform2, withStatement.getLineno());
    }
    
    private Node transformXmlLiteral(final XmlLiteral xmlLiteral) {
        final Node node = new Node(30, xmlLiteral.getLineno());
        final List<XmlFragment> fragments = xmlLiteral.getFragments();
        String s;
        if (((XmlString)fragments.get(0)).getXml().trim().startsWith("<>")) {
            s = "XMLList";
        }
        else {
            s = "XML";
        }
        node.addChildToBack(this.createName(s));
        Node node2 = null;
        for (final XmlFragment xmlFragment : fragments) {
            if (xmlFragment instanceof XmlString) {
                final String xml = ((XmlString)xmlFragment).getXml();
                this.decompiler.addName(xml);
                if (node2 == null) {
                    node2 = this.createString(xml);
                }
                else {
                    node2 = this.createBinary(21, node2, this.createString(xml));
                }
            }
            else {
                final XmlExpression xmlExpression = (XmlExpression)xmlFragment;
                final boolean xmlAttribute = xmlExpression.isXmlAttribute();
                this.decompiler.addToken(85);
                Node node3;
                if (xmlExpression.getExpression() instanceof EmptyExpression) {
                    node3 = this.createString("");
                }
                else {
                    node3 = this.transform(xmlExpression.getExpression());
                }
                this.decompiler.addToken(86);
                Node node4;
                if (xmlAttribute) {
                    node4 = this.createBinary(21, this.createBinary(21, this.createString("\""), this.createUnary(75, node3)), this.createString("\""));
                }
                else {
                    node4 = this.createUnary(76, node3);
                }
                node2 = this.createBinary(21, node2, node4);
            }
        }
        node.addChildToBack(node2);
        return node;
    }
    
    private Node transformXmlMemberGet(final XmlMemberGet xmlMemberGet) {
        final XmlRef memberRef = xmlMemberGet.getMemberRef();
        final Node transform = this.transform(xmlMemberGet.getLeft());
        int n;
        if (memberRef.isAttributeAccess()) {
            n = 2;
        }
        else {
            n = 0;
        }
        if (xmlMemberGet.getType() == 143) {
            n |= 0x4;
            this.decompiler.addToken(143);
        }
        else {
            this.decompiler.addToken(108);
        }
        return this.transformXmlRef(transform, memberRef, n);
    }
    
    private Node transformXmlRef(final Node node, final XmlRef xmlRef, final int n) {
        if ((n & 0x2) != 0x0) {
            this.decompiler.addToken(147);
        }
        final Name namespace = xmlRef.getNamespace();
        String identifier;
        if (namespace != null) {
            identifier = namespace.getIdentifier();
        }
        else {
            identifier = null;
        }
        if (identifier != null) {
            this.decompiler.addName(identifier);
            this.decompiler.addToken(144);
        }
        if (xmlRef instanceof XmlPropRef) {
            final String identifier2 = ((XmlPropRef)xmlRef).getPropName().getIdentifier();
            this.decompiler.addName(identifier2);
            return this.createPropertyGet(node, identifier, identifier2, n);
        }
        this.decompiler.addToken(83);
        final Node transform = this.transform(((XmlElemRef)xmlRef).getExpression());
        this.decompiler.addToken(84);
        return this.createElementGet(node, identifier, transform, n);
    }
    
    private Node transformXmlRef(final XmlRef xmlRef) {
        int n;
        if (xmlRef.isAttributeAccess()) {
            n = 2;
        }
        else {
            n = 0;
        }
        return this.transformXmlRef(null, xmlRef, n);
    }
    
    private Node transformYield(final Yield yield) {
        this.decompiler.addToken(72);
        Node transform;
        if (yield.getValue() == null) {
            transform = null;
        }
        else {
            transform = this.transform(yield.getValue());
        }
        if (transform != null) {
            return new Node(72, transform, yield.getLineno());
        }
        return new Node(72, yield.getLineno());
    }
    
    void decompile(final AstNode astNode) {
        final int type = astNode.getType();
        if (type == 33) {
            this.decompilePropertyGet((PropertyGet)astNode);
            return;
        }
        if (type == 36) {
            this.decompileElementGet((ElementGet)astNode);
            return;
        }
        if (type == 43) {
            this.decompiler.addToken(astNode.getType());
            return;
        }
        if (type == 128) {
            return;
        }
        switch (type) {
            default: {
                switch (type) {
                    default: {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("unexpected token: ");
                        sb.append(Token.typeToName(astNode.getType()));
                        Kit.codeBug(sb.toString());
                        return;
                    }
                    case 66: {
                        this.decompileObjectLiteral((ObjectLiteral)astNode);
                        return;
                    }
                    case 65: {
                        this.decompileArrayLiteral((ArrayLiteral)astNode);
                        return;
                    }
                }
                break;
            }
            case 41: {
                this.decompiler.addString(((StringLiteral)astNode).getValue());
            }
            case 40: {
                this.decompiler.addNumber(((NumberLiteral)astNode).getNumber());
            }
            case 39: {
                this.decompiler.addName(((Name)astNode).getIdentifier());
            }
        }
    }
    
    void decompileArrayLiteral(final ArrayLiteral arrayLiteral) {
        this.decompiler.addToken(83);
        final List<AstNode> elements = arrayLiteral.getElements();
        for (int size = elements.size(), i = 0; i < size; ++i) {
            this.decompile(elements.get(i));
            if (i < size - 1) {
                this.decompiler.addToken(89);
            }
        }
        this.decompiler.addToken(84);
    }
    
    void decompileElementGet(final ElementGet elementGet) {
        this.decompile(elementGet.getTarget());
        this.decompiler.addToken(83);
        this.decompile(elementGet.getElement());
        this.decompiler.addToken(84);
    }
    
    Node decompileFunctionHeader(final FunctionNode functionNode) {
        Node transform = null;
        if (functionNode.getFunctionName() != null) {
            this.decompiler.addName(functionNode.getName());
        }
        else if (functionNode.getMemberExprNode() != null) {
            transform = this.transform(functionNode.getMemberExprNode());
        }
        this.decompiler.addToken(87);
        final List<AstNode> params = functionNode.getParams();
        for (int i = 0; i < params.size(); ++i) {
            this.decompile(params.get(i));
            if (i < params.size() - 1) {
                this.decompiler.addToken(89);
            }
        }
        this.decompiler.addToken(88);
        if (!functionNode.isExpressionClosure()) {
            this.decompiler.addEOL(85);
        }
        return transform;
    }
    
    void decompileObjectLiteral(final ObjectLiteral objectLiteral) {
        this.decompiler.addToken(85);
        final List<ObjectProperty> elements = objectLiteral.getElements();
        for (int size = elements.size(), i = 0; i < size; ++i) {
            final ObjectProperty objectProperty = elements.get(i);
            final boolean equals = Boolean.TRUE.equals(objectProperty.getProp(26));
            this.decompile(objectProperty.getLeft());
            if (!equals) {
                this.decompiler.addToken(103);
                this.decompile(objectProperty.getRight());
            }
            if (i < size - 1) {
                this.decompiler.addToken(89);
            }
        }
        this.decompiler.addToken(86);
    }
    
    void decompilePropertyGet(final PropertyGet propertyGet) {
        this.decompile(propertyGet.getTarget());
        this.decompiler.addToken(108);
        this.decompile(propertyGet.getProperty());
    }
    
    boolean isDestructuring(final Node node) {
        return node instanceof DestructuringForm && ((DestructuringForm)node).isDestructuring();
    }
    
    public Node transform(final AstNode astNode) {
        final int type = astNode.getType();
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
                                                if (astNode instanceof ExpressionStatement) {
                                                    return this.transformExprStmt((ExpressionStatement)astNode);
                                                }
                                                if (astNode instanceof Assignment) {
                                                    return this.transformAssignment((Assignment)astNode);
                                                }
                                                if (astNode instanceof UnaryExpression) {
                                                    return this.transformUnary((UnaryExpression)astNode);
                                                }
                                                if (astNode instanceof XmlMemberGet) {
                                                    return this.transformXmlMemberGet((XmlMemberGet)astNode);
                                                }
                                                if (astNode instanceof InfixExpression) {
                                                    return this.transformInfix((InfixExpression)astNode);
                                                }
                                                if (astNode instanceof VariableDeclaration) {
                                                    return this.transformVariables((VariableDeclaration)astNode);
                                                }
                                                if (astNode instanceof ParenthesizedExpression) {
                                                    return this.transformParenExpr((ParenthesizedExpression)astNode);
                                                }
                                                if (astNode instanceof LabeledStatement) {
                                                    return this.transformLabeledStatement((LabeledStatement)astNode);
                                                }
                                                if (astNode instanceof LetNode) {
                                                    return this.transformLetNode((LetNode)astNode);
                                                }
                                                if (astNode instanceof XmlRef) {
                                                    return this.transformXmlRef((XmlRef)astNode);
                                                }
                                                if (astNode instanceof XmlLiteral) {
                                                    return this.transformXmlLiteral((XmlLiteral)astNode);
                                                }
                                                final StringBuilder sb = new StringBuilder();
                                                sb.append("Can't transform: ");
                                                sb.append(astNode);
                                                throw new IllegalArgumentException(sb.toString());
                                            }
                                            case 162: {
                                                return this.transformGenExpr((GeneratorExpression)astNode);
                                            }
                                            case 157: {
                                                return this.transformArrayComp((ArrayComprehension)astNode);
                                            }
                                            case 136: {
                                                return this.transformScript((ScriptNode)astNode);
                                            }
                                            case 123: {
                                                return this.transformWith((WithStatement)astNode);
                                            }
                                            case 114: {
                                                return this.transformSwitch((SwitchStatement)astNode);
                                            }
                                            case 112: {
                                                return this.transformIf((IfStatement)astNode);
                                            }
                                            case 109: {
                                                return this.transformFunction((FunctionNode)astNode);
                                            }
                                            case 102: {
                                                return this.transformCondExpr((ConditionalExpression)astNode);
                                            }
                                            case 81: {
                                                return this.transformTry((TryStatement)astNode);
                                            }
                                            case 72: {
                                                return this.transformYield((Yield)astNode);
                                            }
                                            case 50: {
                                                return this.transformThrow((ThrowStatement)astNode);
                                            }
                                            case 48: {
                                                return this.transformRegExp((RegExpLiteral)astNode);
                                            }
                                            case 36: {
                                                return this.transformElementGet((ElementGet)astNode);
                                            }
                                            case 33: {
                                                return this.transformPropertyGet((PropertyGet)astNode);
                                            }
                                            case 30: {
                                                return this.transformNewExpr((NewExpression)astNode);
                                            }
                                            case 4: {
                                                return this.transformReturn((ReturnStatement)astNode);
                                            }
                                            case 160: {
                                                return this.transformLiteral(astNode);
                                            }
                                        }
                                        break;
                                    }
                                    case 129: {
                                        return this.transformBlock(astNode);
                                    }
                                    case 128: {
                                        return astNode;
                                    }
                                }
                                break;
                            }
                            case 121: {
                                return this.transformContinue((ContinueStatement)astNode);
                            }
                            case 120: {
                                return this.transformBreak((BreakStatement)astNode);
                            }
                            case 119: {
                                if (astNode instanceof ForInLoop) {
                                    return this.transformForInLoop((ForInLoop)astNode);
                                }
                                return this.transformForLoop((ForLoop)astNode);
                            }
                            case 118: {
                                return this.transformDoLoop((DoLoop)astNode);
                            }
                            case 117: {
                                return this.transformWhileLoop((WhileLoop)astNode);
                            }
                        }
                        break;
                    }
                    case 66: {
                        return this.transformObjectLiteral((ObjectLiteral)astNode);
                    }
                    case 65: {
                        return this.transformArrayLiteral((ArrayLiteral)astNode);
                    }
                }
                break;
            }
            case 42:
            case 43:
            case 44:
            case 45: {
                return this.transformLiteral(astNode);
            }
            case 41: {
                return this.transformString((StringLiteral)astNode);
            }
            case 40: {
                return this.transformNumber((NumberLiteral)astNode);
            }
            case 39: {
                return this.transformName((Name)astNode);
            }
            case 38: {
                return this.transformFunctionCall((FunctionCall)astNode);
            }
        }
    }
    
    public ScriptNode transformTree(final AstRoot currentScriptOrFn) {
        this.currentScriptOrFn = currentScriptOrFn;
        this.inUseStrictDirective = currentScriptOrFn.isInStrictMode();
        final int currentOffset = this.decompiler.getCurrentOffset();
        final ScriptNode scriptNode = (ScriptNode)this.transform(currentScriptOrFn);
        scriptNode.setEncodedSourceBounds(currentOffset, this.decompiler.getCurrentOffset());
        if (this.compilerEnv.isGeneratingSource()) {
            scriptNode.setEncodedSource(this.decompiler.getEncodedSource());
        }
        this.decompiler = null;
        return scriptNode;
    }
}
