package org.mozilla.javascript;

import java.util.function.*;
import org.mozilla.javascript.ast.*;
import java.util.*;

public class Node implements Iterable<Node>
{
    public static final int ATTRIBUTE_FLAG = 2;
    public static final int BOTH = 0;
    public static final int CASEARRAY_PROP = 5;
    public static final int CATCH_SCOPE_PROP = 14;
    public static final int CONTROL_BLOCK_PROP = 18;
    public static final int DECR_FLAG = 1;
    public static final int DESCENDANTS_FLAG = 4;
    public static final int DESTRUCTURING_ARRAY_LENGTH = 21;
    public static final int DESTRUCTURING_NAMES = 22;
    public static final int DESTRUCTURING_PARAMS = 23;
    public static final int DESTRUCTURING_SHORTHAND = 26;
    public static final int DIRECTCALL_PROP = 9;
    public static final int END_DROPS_OFF = 1;
    public static final int END_RETURNS = 2;
    public static final int END_RETURNS_VALUE = 4;
    public static final int END_UNREACHED = 0;
    public static final int END_YIELDS = 8;
    public static final int EXPRESSION_CLOSURE_PROP = 25;
    public static final int FUNCTION_PROP = 1;
    public static final int GENERATOR_END_PROP = 20;
    public static final int INCRDECR_PROP = 13;
    public static final int ISNUMBER_PROP = 8;
    public static final int JSDOC_PROP = 24;
    public static final int LABEL_ID_PROP = 15;
    public static final int LAST_PROP = 26;
    public static final int LEFT = 1;
    public static final int LOCAL_BLOCK_PROP = 3;
    public static final int LOCAL_PROP = 2;
    public static final int MEMBER_TYPE_PROP = 16;
    public static final int NAME_PROP = 17;
    public static final int NON_SPECIALCALL = 0;
    private static final Node NOT_SET;
    public static final int OBJECT_IDS_PROP = 12;
    public static final int PARENTHESIZED_PROP = 19;
    public static final int POST_FLAG = 2;
    public static final int PROPERTY_FLAG = 1;
    public static final int REGEXP_PROP = 4;
    public static final int RIGHT = 2;
    public static final int SKIP_INDEXES_PROP = 11;
    public static final int SPECIALCALL_EVAL = 1;
    public static final int SPECIALCALL_PROP = 10;
    public static final int SPECIALCALL_WITH = 2;
    public static final int TARGETBLOCK_PROP = 6;
    public static final int VARIABLE_PROP = 7;
    protected Node first;
    protected Node last;
    protected int lineno;
    protected Node next;
    protected PropListItem propListHead;
    protected int type;
    
    static {
        NOT_SET = new Node(-1);
    }
    
    public Node(final int type) {
        this.type = -1;
        this.lineno = -1;
        this.type = type;
    }
    
    public Node(final int type, final int lineno) {
        this.type = -1;
        this.lineno = -1;
        this.type = type;
        this.lineno = lineno;
    }
    
    public Node(final int type, final Node node) {
        this.type = -1;
        this.lineno = -1;
        this.type = type;
        this.last = node;
        this.first = node;
        node.next = null;
    }
    
    public Node(final int n, final Node node, final int lineno) {
        this(n, node);
        this.lineno = lineno;
    }
    
    public Node(final int type, final Node first, final Node node) {
        this.type = -1;
        this.lineno = -1;
        this.type = type;
        this.first = first;
        this.last = node;
        first.next = node;
        node.next = null;
    }
    
    public Node(final int n, final Node node, final Node node2, final int lineno) {
        this(n, node, node2);
        this.lineno = lineno;
    }
    
    public Node(final int type, final Node first, final Node next, final Node node) {
        this.type = -1;
        this.lineno = -1;
        this.type = type;
        this.first = first;
        this.last = node;
        first.next = next;
        next.next = node;
        node.next = null;
    }
    
    public Node(final int n, final Node node, final Node node2, final Node node3, final int lineno) {
        this(n, node, node2, node3);
        this.lineno = lineno;
    }
    
    private static void appendPrintId(final Node node, final ObjToIntMap objToIntMap, final StringBuilder sb) {
    }
    
    private int endCheck() {
        final int type = this.type;
        if (type != 4) {
            if (type != 50) {
                if (type == 72) {
                    return 8;
                }
                if (type != 129 && type != 141) {
                    switch (type) {
                        default: {
                            switch (type) {
                                default: {
                                    return 1;
                                }
                                case 133: {
                                    if (this.first != null) {
                                        return this.first.endCheck();
                                    }
                                    return 1;
                                }
                                case 132: {
                                    return this.endCheckLoop();
                                }
                                case 131: {
                                    if (this.next != null) {
                                        return this.next.endCheck();
                                    }
                                    return 1;
                                }
                            }
                            break;
                        }
                        case 120: {
                            return this.endCheckBreak();
                        }
                        case 121: {
                            break;
                        }
                    }
                }
                else {
                    if (this.first == null) {
                        return 1;
                    }
                    final int type2 = this.first.type;
                    if (type2 == 7) {
                        return this.first.endCheckIf();
                    }
                    if (type2 == 81) {
                        return this.first.endCheckTry();
                    }
                    if (type2 == 114) {
                        return this.first.endCheckSwitch();
                    }
                    if (type2 != 130) {
                        return this.endCheckBlock();
                    }
                    return this.first.endCheckLabel();
                }
            }
            return 0;
        }
        if (this.first != null) {
            return 4;
        }
        return 2;
    }
    
    private int endCheckBlock() {
        int n = 1;
        for (Node node = this.first; (n & 0x1) != 0x0 && node != null; n = ((n & 0xFFFFFFFE) | node.endCheck()), node = node.next) {}
        return n;
    }
    
    private int endCheckBreak() {
        ((Jump)this).getJumpStatement().putIntProp(18, 1);
        return 0;
    }
    
    private int endCheckIf() {
        final Node next = this.next;
        final Node target = ((Jump)this).target;
        final int endCheck = next.endCheck();
        if (target != null) {
            return endCheck | target.endCheck();
        }
        return endCheck | 0x1;
    }
    
    private int endCheckLabel() {
        return this.next.endCheck() | this.getIntProp(18, 0);
    }
    
    private int endCheckLoop() {
        Node node;
        for (node = this.first; node.next != this.last; node = node.next) {}
        if (node.type != 6) {
            return 1;
        }
        int endCheck = ((Jump)node).target.next.endCheck();
        if (node.first.type == 45) {
            endCheck &= 0xFFFFFFFE;
        }
        return endCheck | this.getIntProp(18, 0);
    }
    
    private int endCheckSwitch() {
        return 0;
    }
    
    private int endCheckTry() {
        return 0;
    }
    
    private PropListItem ensureProperty(final int type) {
        PropListItem lookupProperty;
        if ((lookupProperty = this.lookupProperty(type)) == null) {
            lookupProperty = new PropListItem();
            lookupProperty.type = type;
            lookupProperty.next = this.propListHead;
            this.propListHead = lookupProperty;
        }
        return lookupProperty;
    }
    
    private static void generatePrintIds(final Node node, final ObjToIntMap objToIntMap) {
    }
    
    private PropListItem lookupProperty(final int n) {
        PropListItem propListItem;
        for (propListItem = this.propListHead; propListItem != null && n != propListItem.type; propListItem = propListItem.next) {}
        return propListItem;
    }
    
    public static Node newNumber(final double number) {
        final NumberLiteral numberLiteral = new NumberLiteral();
        numberLiteral.setNumber(number);
        return numberLiteral;
    }
    
    public static Node newString(final int type, final String identifier) {
        final Name name = new Name();
        name.setIdentifier(identifier);
        name.setType(type);
        return name;
    }
    
    public static Node newString(final String s) {
        return newString(41, s);
    }
    
    public static Node newTarget() {
        return new Node(131);
    }
    
    private static final String propToString(final int n) {
        return null;
    }
    
    private void resetTargets_r() {
        if (this.type == 131 || this.type == 72) {
            this.labelId(-1);
        }
        for (Node node = this.first; node != null; node = node.next) {
            node.resetTargets_r();
        }
    }
    
    private void toString(final ObjToIntMap objToIntMap, final StringBuilder sb) {
    }
    
    private static void toStringTreeHelper(final ScriptNode scriptNode, final Node node, final ObjToIntMap objToIntMap, final int n, final StringBuilder sb) {
    }
    
    public void addChildAfter(final Node node, final Node node2) {
        if (node.next != null) {
            throw new RuntimeException("newChild had siblings in addChildAfter");
        }
        node.next = node2.next;
        node2.next = node;
        if (this.last == node2) {
            this.last = node;
        }
    }
    
    public void addChildBefore(final Node first, final Node node) {
        if (first.next != null) {
            throw new RuntimeException("newChild had siblings in addChildBefore");
        }
        if (this.first == node) {
            first.next = this.first;
            this.first = first;
            return;
        }
        this.addChildAfter(first, this.getChildBefore(node));
    }
    
    public void addChildToBack(final Node node) {
        node.next = null;
        if (this.last == null) {
            this.last = node;
            this.first = node;
            return;
        }
        this.last.next = node;
        this.last = node;
    }
    
    public void addChildToFront(final Node node) {
        node.next = this.first;
        this.first = node;
        if (this.last == null) {
            this.last = node;
        }
    }
    
    public void addChildrenToBack(final Node node) {
        if (this.last != null) {
            this.last.next = node;
        }
        this.last = node.getLastSibling();
        if (this.first == null) {
            this.first = node;
        }
    }
    
    public void addChildrenToFront(final Node first) {
        final Node lastSibling = first.getLastSibling();
        lastSibling.next = this.first;
        this.first = first;
        if (this.last == null) {
            this.last = lastSibling;
        }
    }
    
    @Override
    public void forEach(final Consumer<?> p0) {
        // 
        // This method could not be decompiled.
        // 
        // Could not show original bytecode, likely due to the same error.
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public Node getChildBefore(final Node node) {
        if (node == this.first) {
            return null;
        }
        Node node2 = this.first;
        while (node2.next != node) {
            if ((node2 = node2.next) == null) {
                throw new RuntimeException("node is not a child");
            }
        }
        return node2;
    }
    
    public final double getDouble() {
        return ((NumberLiteral)this).getNumber();
    }
    
    public int getExistingIntProp(final int n) {
        final PropListItem lookupProperty = this.lookupProperty(n);
        if (lookupProperty == null) {
            Kit.codeBug();
        }
        return lookupProperty.intValue;
    }
    
    public Node getFirstChild() {
        return this.first;
    }
    
    public int getIntProp(final int n, final int n2) {
        final PropListItem lookupProperty = this.lookupProperty(n);
        if (lookupProperty == null) {
            return n2;
        }
        return lookupProperty.intValue;
    }
    
    public String getJsDoc() {
        final Comment jsDocNode = this.getJsDocNode();
        if (jsDocNode != null) {
            return jsDocNode.getValue();
        }
        return null;
    }
    
    public Comment getJsDocNode() {
        return (Comment)this.getProp(24);
    }
    
    public Node getLastChild() {
        return this.last;
    }
    
    public Node getLastSibling() {
        Node next;
        for (next = this; next.next != null; next = next.next) {}
        return next;
    }
    
    public int getLineno() {
        return this.lineno;
    }
    
    public Node getNext() {
        return this.next;
    }
    
    public Object getProp(final int n) {
        final PropListItem lookupProperty = this.lookupProperty(n);
        if (lookupProperty == null) {
            return null;
        }
        return lookupProperty.objectValue;
    }
    
    public Scope getScope() {
        return ((Name)this).getScope();
    }
    
    public final String getString() {
        return ((Name)this).getIdentifier();
    }
    
    public int getType() {
        return this.type;
    }
    
    public boolean hasChildren() {
        return this.first != null;
    }
    
    public boolean hasConsistentReturnUsage() {
        final int endCheck = this.endCheck();
        return (endCheck & 0x4) == 0x0 || (endCheck & 0xB) == 0x0;
    }
    
    public boolean hasSideEffects() {
        final int type = this.type;
        boolean b = true;
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
                                                                                                                                        return false;
                                                                                                                                    }
                                                                                                                                    case -1:
                                                                                                                                    case 35:
                                                                                                                                    case 64:
                                                                                                                                    case 72: {
                                                                                                                                        return true;
                                                                                                                                    }
                                                                                                                                }
                                                                                                                                break;
                                                                                                                            }
                                                                                                                            case 158:
                                                                                                                            case 159: {
                                                                                                                                return true;
                                                                                                                            }
                                                                                                                        }
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    case 153:
                                                                                                                    case 154: {
                                                                                                                        return true;
                                                                                                                    }
                                                                                                                }
                                                                                                                break;
                                                                                                            }
                                                                                                            case 139:
                                                                                                            case 140:
                                                                                                            case 141:
                                                                                                            case 142: {
                                                                                                                return true;
                                                                                                            }
                                                                                                        }
                                                                                                        break;
                                                                                                    }
                                                                                                    case 133: {
                                                                                                        return this.last == null || this.last.hasSideEffects();
                                                                                                    }
                                                                                                    case 129:
                                                                                                    case 130:
                                                                                                    case 131:
                                                                                                    case 132:
                                                                                                    case 134:
                                                                                                    case 135: {
                                                                                                        return true;
                                                                                                    }
                                                                                                }
                                                                                                break;
                                                                                            }
                                                                                            case 117:
                                                                                            case 118:
                                                                                            case 119:
                                                                                            case 120:
                                                                                            case 121:
                                                                                            case 122:
                                                                                            case 123:
                                                                                            case 124:
                                                                                            case 125: {
                                                                                                return true;
                                                                                            }
                                                                                        }
                                                                                        break;
                                                                                    }
                                                                                    case 112:
                                                                                    case 113:
                                                                                    case 114: {
                                                                                        return true;
                                                                                    }
                                                                                }
                                                                                break;
                                                                            }
                                                                            case 104:
                                                                            case 105: {
                                                                                if (this.first == null || this.last == null) {
                                                                                    Kit.codeBug();
                                                                                }
                                                                                if (!this.first.hasSideEffects()) {
                                                                                    if (this.last.hasSideEffects()) {
                                                                                        return true;
                                                                                    }
                                                                                    b = false;
                                                                                }
                                                                                return b;
                                                                            }
                                                                            case 106:
                                                                            case 107: {
                                                                                return true;
                                                                            }
                                                                        }
                                                                        break;
                                                                    }
                                                                    case 102: {
                                                                        if (this.first == null || this.first.next == null || this.first.next.next == null) {
                                                                            Kit.codeBug();
                                                                        }
                                                                        return this.first.next.hasSideEffects() && this.first.next.next.hasSideEffects();
                                                                    }
                                                                    case 89: {
                                                                        return this.last == null || this.last.hasSideEffects();
                                                                    }
                                                                    case 90:
                                                                    case 91:
                                                                    case 92:
                                                                    case 93:
                                                                    case 94:
                                                                    case 95:
                                                                    case 96:
                                                                    case 97:
                                                                    case 98:
                                                                    case 99:
                                                                    case 100:
                                                                    case 101: {
                                                                        return true;
                                                                    }
                                                                }
                                                                break;
                                                            }
                                                            case 81:
                                                            case 82: {
                                                                return true;
                                                            }
                                                        }
                                                        break;
                                                    }
                                                    case 68:
                                                    case 69:
                                                    case 70: {
                                                        return true;
                                                    }
                                                }
                                                break;
                                            }
                                            case 56:
                                            case 57: {
                                                return true;
                                            }
                                        }
                                        break;
                                    }
                                    case 50:
                                    case 51: {
                                        return true;
                                    }
                                }
                                break;
                            }
                            case 37:
                            case 38: {
                                return true;
                            }
                        }
                        break;
                    }
                    case 30:
                    case 31: {
                        return true;
                    }
                }
                break;
            }
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8: {
                return true;
            }
        }
    }
    
    @Override
    public Iterator<Node> iterator() {
        return new NodeIterator();
    }
    
    public final int labelId() {
        if (this.type != 131 && this.type != 72) {
            Kit.codeBug();
        }
        return this.getIntProp(15, -1);
    }
    
    public void labelId(final int n) {
        if (this.type != 131 && this.type != 72) {
            Kit.codeBug();
        }
        this.putIntProp(15, n);
    }
    
    public void putIntProp(final int n, final int intValue) {
        this.ensureProperty(n).intValue = intValue;
    }
    
    public void putProp(final int n, final Object objectValue) {
        if (objectValue == null) {
            this.removeProp(n);
            return;
        }
        this.ensureProperty(n).objectValue = objectValue;
    }
    
    public void removeChild(final Node node) {
        final Node childBefore = this.getChildBefore(node);
        if (childBefore == null) {
            this.first = this.first.next;
        }
        else {
            childBefore.next = node.next;
        }
        if (node == this.last) {
            this.last = childBefore;
        }
        node.next = null;
    }
    
    public void removeChildren() {
        this.last = null;
        this.first = null;
    }
    
    public void removeProp(final int n) {
        PropListItem propListItem = this.propListHead;
        if (propListItem != null) {
            PropListItem propListItem2 = null;
            while (propListItem.type != n) {
                propListItem2 = propListItem;
                if ((propListItem = propListItem.next) == null) {
                    return;
                }
            }
            if (propListItem2 == null) {
                this.propListHead = propListItem.next;
                return;
            }
            propListItem2.next = propListItem.next;
        }
    }
    
    public void replaceChild(final Node node, final Node last) {
        last.next = node.next;
        if (node == this.first) {
            this.first = last;
        }
        else {
            this.getChildBefore(node).next = last;
        }
        if (node == this.last) {
            this.last = last;
        }
        node.next = null;
    }
    
    public void replaceChildAfter(final Node node, final Node node2) {
        final Node next = node.next;
        node2.next = next.next;
        node.next = node2;
        if (next == this.last) {
            this.last = node2;
        }
        next.next = null;
    }
    
    public void resetTargets() {
        if (this.type == 125) {
            this.resetTargets_r();
            return;
        }
        Kit.codeBug();
    }
    
    public final void setDouble(final double number) {
        ((NumberLiteral)this).setNumber(number);
    }
    
    public void setJsDocNode(final Comment comment) {
        this.putProp(24, comment);
    }
    
    public void setLineno(final int lineno) {
        this.lineno = lineno;
    }
    
    public void setScope(final Scope scope) {
        if (scope == null) {
            Kit.codeBug();
        }
        if (!(this instanceof Name)) {
            throw Kit.codeBug();
        }
        ((Name)this).setScope(scope);
    }
    
    public final void setString(final String identifier) {
        if (identifier == null) {
            Kit.codeBug();
        }
        ((Name)this).setIdentifier(identifier);
    }
    
    public Node setType(final int type) {
        this.type = type;
        return this;
    }
    
    @Override
    public Spliterator<Object> spliterator() {
        return (Spliterator<Object>)Iterable-CC.$default$spliterator((Iterable)this);
    }
    
    @Override
    public String toString() {
        return String.valueOf(this.type);
    }
    
    public String toStringTree(final ScriptNode scriptNode) {
        return null;
    }
    
    public class NodeIterator implements Iterator<Node>
    {
        private Node cursor;
        private Node prev;
        private Node prev2;
        private boolean removed;
        
        public NodeIterator() {
            this.prev = Node.NOT_SET;
            this.removed = false;
            this.cursor = Node.this.first;
        }
        
        @Override
        public void forEachRemaining(final Consumer<?> p0) {
            // 
            // This method could not be decompiled.
            // 
            // Could not show original bytecode, likely due to the same error.
            // 
            // The error that occurred was:
            // 
            // java.lang.NullPointerException
            // 
            throw new IllegalStateException("An error occurred while decompiling this method.");
        }
        
        @Override
        public boolean hasNext() {
            return this.cursor != null;
        }
        
        @Override
        public Node next() {
            if (this.cursor == null) {
                throw new NoSuchElementException();
            }
            this.removed = false;
            this.prev2 = this.prev;
            this.prev = this.cursor;
            this.cursor = this.cursor.next;
            return this.prev;
        }
        
        @Override
        public void remove() {
            if (this.prev == Node.NOT_SET) {
                throw new IllegalStateException("next() has not been called");
            }
            if (this.removed) {
                throw new IllegalStateException("remove() already called for current element");
            }
            if (this.prev == Node.this.first) {
                Node.this.first = this.prev.next;
                return;
            }
            if (this.prev == Node.this.last) {
                this.prev2.next = null;
                Node.this.last = this.prev2;
                return;
            }
            this.prev2.next = this.cursor;
        }
    }
    
    private static class PropListItem
    {
        int intValue;
        PropListItem next;
        Object objectValue;
        int type;
    }
}
