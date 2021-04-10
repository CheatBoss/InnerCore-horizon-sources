package org.mozilla.javascript.ast;

import java.util.*;
import org.mozilla.javascript.*;

public class Scope extends Jump
{
    private List<Scope> childScopes;
    protected Scope parentScope;
    protected Map<String, Symbol> symbolTable;
    protected ScriptNode top;
    
    public Scope() {
        this.type = 129;
    }
    
    public Scope(final int position) {
        this.type = 129;
        this.position = position;
    }
    
    public Scope(final int n, final int length) {
        this(n);
        this.length = length;
    }
    
    private Map<String, Symbol> ensureSymbolTable() {
        if (this.symbolTable == null) {
            this.symbolTable = new LinkedHashMap<String, Symbol>(5);
        }
        return this.symbolTable;
    }
    
    public static void joinScopes(final Scope scope, final Scope containingTable) {
        final Map<String, Symbol> ensureSymbolTable = scope.ensureSymbolTable();
        final Map<String, Symbol> ensureSymbolTable2 = containingTable.ensureSymbolTable();
        if (!Collections.disjoint(ensureSymbolTable.keySet(), ensureSymbolTable2.keySet())) {
            codeBug();
        }
        for (final Map.Entry<String, Symbol> entry : ensureSymbolTable.entrySet()) {
            final Symbol symbol = entry.getValue();
            symbol.setContainingTable(containingTable);
            ensureSymbolTable2.put(entry.getKey(), symbol);
        }
    }
    
    public static Scope splitScope(final Scope scope) {
        final Scope scope2 = new Scope(scope.getType());
        scope2.symbolTable = scope.symbolTable;
        scope.symbolTable = null;
        scope2.parent = scope.parent;
        scope2.setParentScope(scope.getParentScope());
        scope2.setParentScope(scope2);
        scope.parent = scope2;
        scope2.top = scope.top;
        return scope2;
    }
    
    public void addChildScope(final Scope scope) {
        if (this.childScopes == null) {
            this.childScopes = new ArrayList<Scope>();
        }
        this.childScopes.add(scope);
        scope.setParentScope(this);
    }
    
    public void clearParentScope() {
        this.parentScope = null;
    }
    
    public List<Scope> getChildScopes() {
        return this.childScopes;
    }
    
    public Scope getDefiningScope(final String s) {
        for (Scope parentScope = this; parentScope != null; parentScope = parentScope.parentScope) {
            final Map<String, Symbol> symbolTable = parentScope.getSymbolTable();
            if (symbolTable != null && symbolTable.containsKey(s)) {
                return parentScope;
            }
        }
        return null;
    }
    
    public Scope getParentScope() {
        return this.parentScope;
    }
    
    public List<AstNode> getStatements() {
        final ArrayList<AstNode> list = new ArrayList<AstNode>();
        for (Node node = this.getFirstChild(); node != null; node = node.getNext()) {
            list.add((AstNode)node);
        }
        return list;
    }
    
    public Symbol getSymbol(final String s) {
        if (this.symbolTable == null) {
            return null;
        }
        return this.symbolTable.get(s);
    }
    
    public Map<String, Symbol> getSymbolTable() {
        return this.symbolTable;
    }
    
    public ScriptNode getTop() {
        return this.top;
    }
    
    public void putSymbol(final Symbol symbol) {
        if (symbol.getName() == null) {
            throw new IllegalArgumentException("null symbol name");
        }
        this.ensureSymbolTable();
        this.symbolTable.put(symbol.getName(), symbol);
        symbol.setContainingTable(this);
        this.top.addSymbol(symbol);
    }
    
    public void replaceWith(final Scope scope) {
        if (this.childScopes != null) {
            final Iterator<Scope> iterator = this.childScopes.iterator();
            while (iterator.hasNext()) {
                scope.addChildScope(iterator.next());
            }
            this.childScopes.clear();
            this.childScopes = null;
        }
        if (this.symbolTable != null && !this.symbolTable.isEmpty()) {
            joinScopes(this, scope);
        }
    }
    
    public void setParentScope(final Scope parentScope) {
        this.parentScope = parentScope;
        ScriptNode top;
        if (parentScope == null) {
            top = (ScriptNode)this;
        }
        else {
            top = parentScope.top;
        }
        this.top = top;
    }
    
    public void setSymbolTable(final Map<String, Symbol> symbolTable) {
        this.symbolTable = symbolTable;
    }
    
    public void setTop(final ScriptNode top) {
        this.top = top;
    }
    
    @Override
    public String toSource(final int n) {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.makeIndent(n));
        sb.append("{\n");
        final Iterator<Node> iterator = this.iterator();
        while (iterator.hasNext()) {
            sb.append(((AstNode)iterator.next()).toSource(n + 1));
        }
        sb.append(this.makeIndent(n));
        sb.append("}\n");
        return sb.toString();
    }
    
    @Override
    public void visit(final NodeVisitor nodeVisitor) {
        if (nodeVisitor.visit(this)) {
            final Iterator<Node> iterator = this.iterator();
            while (iterator.hasNext()) {
                ((AstNode)iterator.next()).visit(nodeVisitor);
            }
        }
    }
}
