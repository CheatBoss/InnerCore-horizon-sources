package org.mozilla.javascript.ast;

import org.mozilla.javascript.*;
import java.io.*;
import java.util.*;
import java.util.function.*;

public abstract class AstNode extends Node implements Comparable<AstNode>
{
    private static Map<Integer, String> operatorNames;
    protected int length;
    protected AstNode parent;
    protected int position;
    
    static {
        (AstNode.operatorNames = new HashMap<Integer, String>()).put(52, "in");
        AstNode.operatorNames.put(32, "typeof");
        AstNode.operatorNames.put(53, "instanceof");
        AstNode.operatorNames.put(31, "delete");
        AstNode.operatorNames.put(89, ",");
        AstNode.operatorNames.put(103, ":");
        AstNode.operatorNames.put(104, "||");
        AstNode.operatorNames.put(105, "&&");
        AstNode.operatorNames.put(106, "++");
        AstNode.operatorNames.put(107, "--");
        AstNode.operatorNames.put(9, "|");
        AstNode.operatorNames.put(10, "^");
        AstNode.operatorNames.put(11, "&");
        AstNode.operatorNames.put(12, "==");
        AstNode.operatorNames.put(13, "!=");
        AstNode.operatorNames.put(14, "<");
        AstNode.operatorNames.put(16, ">");
        AstNode.operatorNames.put(15, "<=");
        AstNode.operatorNames.put(17, ">=");
        AstNode.operatorNames.put(18, "<<");
        AstNode.operatorNames.put(19, ">>");
        AstNode.operatorNames.put(20, ">>>");
        AstNode.operatorNames.put(21, "+");
        AstNode.operatorNames.put(22, "-");
        AstNode.operatorNames.put(23, "*");
        AstNode.operatorNames.put(24, "/");
        AstNode.operatorNames.put(25, "%");
        AstNode.operatorNames.put(26, "!");
        AstNode.operatorNames.put(27, "~");
        AstNode.operatorNames.put(28, "+");
        AstNode.operatorNames.put(29, "-");
        AstNode.operatorNames.put(46, "===");
        AstNode.operatorNames.put(47, "!==");
        AstNode.operatorNames.put(90, "=");
        AstNode.operatorNames.put(91, "|=");
        AstNode.operatorNames.put(93, "&=");
        AstNode.operatorNames.put(94, "<<=");
        AstNode.operatorNames.put(95, ">>=");
        AstNode.operatorNames.put(96, ">>>=");
        AstNode.operatorNames.put(97, "+=");
        AstNode.operatorNames.put(98, "-=");
        AstNode.operatorNames.put(99, "*=");
        AstNode.operatorNames.put(100, "/=");
        AstNode.operatorNames.put(101, "%=");
        AstNode.operatorNames.put(92, "^=");
        AstNode.operatorNames.put(126, "void");
    }
    
    public AstNode() {
        super(-1);
        this.position = -1;
        this.length = 1;
    }
    
    public AstNode(final int position) {
        this();
        this.position = position;
    }
    
    public AstNode(final int position, final int length) {
        this();
        this.position = position;
        this.length = length;
    }
    
    public static RuntimeException codeBug() throws RuntimeException {
        throw Kit.codeBug();
    }
    
    public static String operatorToString(final int n) {
        final String s = AstNode.operatorNames.get(n);
        if (s == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Invalid operator: ");
            sb.append(n);
            throw new IllegalArgumentException(sb.toString());
        }
        return s;
    }
    
    public void addChild(final AstNode astNode) {
        this.assertNotNull(astNode);
        this.setLength(astNode.getPosition() + astNode.getLength() - this.getPosition());
        this.addChildToBack(astNode);
        astNode.setParent(this);
    }
    
    protected void assertNotNull(final Object o) {
        if (o == null) {
            throw new IllegalArgumentException("arg cannot be null");
        }
    }
    
    @Override
    public int compareTo(final AstNode astNode) {
        if (this.equals(astNode)) {
            return 0;
        }
        final int absolutePosition = this.getAbsolutePosition();
        final int absolutePosition2 = astNode.getAbsolutePosition();
        if (absolutePosition < absolutePosition2) {
            return -1;
        }
        if (absolutePosition2 < absolutePosition) {
            return 1;
        }
        final int length = this.getLength();
        final int length2 = astNode.getLength();
        if (length < length2) {
            return -1;
        }
        if (length2 < length) {
            return 1;
        }
        return this.hashCode() - astNode.hashCode();
    }
    
    public String debugPrint() {
        final DebugPrintVisitor debugPrintVisitor = new DebugPrintVisitor(new StringBuilder(1000));
        this.visit(debugPrintVisitor);
        return debugPrintVisitor.toString();
    }
    
    public int depth() {
        if (this.parent == null) {
            return 0;
        }
        return this.parent.depth() + 1;
    }
    
    public int getAbsolutePosition() {
        int position = this.position;
        for (AstNode astNode = this.parent; astNode != null; astNode = astNode.getParent()) {
            position += astNode.getPosition();
        }
        return position;
    }
    
    public AstRoot getAstRoot() {
        AstNode parent;
        for (parent = this; parent != null && !(parent instanceof AstRoot); parent = parent.getParent()) {}
        return (AstRoot)parent;
    }
    
    public FunctionNode getEnclosingFunction() {
        AstNode astNode;
        for (astNode = this.getParent(); astNode != null && !(astNode instanceof FunctionNode); astNode = astNode.getParent()) {}
        return (FunctionNode)astNode;
    }
    
    public Scope getEnclosingScope() {
        AstNode astNode;
        for (astNode = this.getParent(); astNode != null && !(astNode instanceof Scope); astNode = astNode.getParent()) {}
        return (Scope)astNode;
    }
    
    public int getLength() {
        return this.length;
    }
    
    @Override
    public int getLineno() {
        if (this.lineno != -1) {
            return this.lineno;
        }
        if (this.parent != null) {
            return this.parent.getLineno();
        }
        return -1;
    }
    
    public AstNode getParent() {
        return this.parent;
    }
    
    public int getPosition() {
        return this.position;
    }
    
    @Override
    public boolean hasSideEffects() {
        final int type = this.getType();
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
                                                                                                            case 134:
                                                                                                            case 135: {
                                                                                                                return true;
                                                                                                            }
                                                                                                        }
                                                                                                        break;
                                                                                                    }
                                                                                                    case 129:
                                                                                                    case 130:
                                                                                                    case 131:
                                                                                                    case 132: {
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
                                                                                    case 109:
                                                                                    case 110:
                                                                                    case 111:
                                                                                    case 112:
                                                                                    case 113:
                                                                                    case 114: {
                                                                                        return true;
                                                                                    }
                                                                                }
                                                                                break;
                                                                            }
                                                                            case 106:
                                                                            case 107: {
                                                                                return true;
                                                                            }
                                                                        }
                                                                        break;
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
    
    public String makeIndent(final int n) {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; ++i) {
            sb.append("  ");
        }
        return sb.toString();
    }
    
    protected <T extends AstNode> void printList(final List<T> list, final StringBuilder sb) {
        final int size = list.size();
        int n = 0;
        for (final AstNode astNode : list) {
            sb.append(astNode.toSource(0));
            if (n < size - 1) {
                sb.append(", ");
            }
            else if (astNode instanceof EmptyExpression) {
                sb.append(",");
            }
            ++n;
        }
    }
    
    public void setBounds(final int position, final int n) {
        this.setPosition(position);
        this.setLength(n - position);
    }
    
    public void setLength(final int length) {
        this.length = length;
    }
    
    public void setParent(final AstNode parent) {
        if (parent == this.parent) {
            return;
        }
        if (this.parent != null) {
            this.setRelative(-this.parent.getPosition());
        }
        if ((this.parent = parent) != null) {
            this.setRelative(parent.getPosition());
        }
    }
    
    public void setPosition(final int position) {
        this.position = position;
    }
    
    public void setRelative(final int n) {
        this.position -= n;
    }
    
    public String shortName() {
        final String name = this.getClass().getName();
        return name.substring(name.lastIndexOf(".") + 1);
    }
    
    public String toSource() {
        return this.toSource(0);
    }
    
    public abstract String toSource(final int p0);
    
    public abstract void visit(final NodeVisitor p0);
    
    protected static class DebugPrintVisitor implements NodeVisitor
    {
        private static final int DEBUG_INDENT = 2;
        private StringBuilder buffer;
        
        public DebugPrintVisitor(final StringBuilder buffer) {
            this.buffer = buffer;
        }
        
        private String makeIndent(final int n) {
            final StringBuilder sb = new StringBuilder(n * 2);
            for (int i = 0; i < n * 2; ++i) {
                sb.append(" ");
            }
            return sb.toString();
        }
        
        @Override
        public String toString() {
            return this.buffer.toString();
        }
        
        @Override
        public boolean visit(final AstNode astNode) {
            final int type = astNode.getType();
            final String typeToName = Token.typeToName(type);
            final StringBuilder buffer = this.buffer;
            buffer.append(astNode.getAbsolutePosition());
            buffer.append("\t");
            this.buffer.append(this.makeIndent(astNode.depth()));
            final StringBuilder buffer2 = this.buffer;
            buffer2.append(typeToName);
            buffer2.append(" ");
            final StringBuilder buffer3 = this.buffer;
            buffer3.append(astNode.getPosition());
            buffer3.append(" ");
            this.buffer.append(astNode.getLength());
            if (type == 39) {
                final StringBuilder buffer4 = this.buffer;
                buffer4.append(" ");
                buffer4.append(((Name)astNode).getIdentifier());
            }
            this.buffer.append("\n");
            return true;
        }
    }
    
    public static class PositionComparator implements Comparator<AstNode>, Serializable
    {
        private static final long serialVersionUID = 1L;
        
        @Override
        public int compare(final AstNode astNode, final AstNode astNode2) {
            return astNode.position - astNode2.position;
        }
        
        @Override
        public Comparator<Object> reversed() {
            return (Comparator<Object>)Comparator-CC.$default$reversed((Comparator)this);
        }
        
        @Override
        public Comparator<Object> thenComparing(final Comparator<?> p0) {
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
        public <U extends Comparable<? super U>> Comparator<Object> thenComparing(final Function<?, ? extends U> p0) {
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
        public <U> Comparator<Object> thenComparing(final Function<?, ? extends U> p0, final Comparator<? super U> p1) {
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
        public Comparator<Object> thenComparingDouble(final ToDoubleFunction<?> p0) {
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
        public Comparator<Object> thenComparingInt(final ToIntFunction<?> p0) {
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
        public Comparator<Object> thenComparingLong(final ToLongFunction<?> p0) {
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
    }
}
