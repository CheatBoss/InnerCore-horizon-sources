package org.mozilla.javascript.ast;

import org.mozilla.javascript.*;
import java.util.*;

public class ScriptNode extends Scope
{
    private List<FunctionNode> EMPTY_LIST;
    private Object compilerData;
    private String encodedSource;
    private int encodedSourceEnd;
    private int encodedSourceStart;
    private int endLineno;
    private List<FunctionNode> functions;
    private boolean[] isConsts;
    private int paramCount;
    private List<RegExpLiteral> regexps;
    private String sourceName;
    private List<Symbol> symbols;
    private int tempNumber;
    private String[] variableNames;
    
    public ScriptNode() {
        this.encodedSourceStart = -1;
        this.encodedSourceEnd = -1;
        this.endLineno = -1;
        this.EMPTY_LIST = Collections.emptyList();
        this.symbols = new ArrayList<Symbol>(4);
        this.paramCount = 0;
        this.tempNumber = 0;
        this.top = this;
        this.type = 136;
    }
    
    public ScriptNode(final int n) {
        super(n);
        this.encodedSourceStart = -1;
        this.encodedSourceEnd = -1;
        this.endLineno = -1;
        this.EMPTY_LIST = Collections.emptyList();
        this.symbols = new ArrayList<Symbol>(4);
        this.paramCount = 0;
        this.tempNumber = 0;
        this.top = this;
        this.type = 136;
    }
    
    public int addFunction(final FunctionNode functionNode) {
        if (functionNode == null) {
            codeBug();
        }
        if (this.functions == null) {
            this.functions = new ArrayList<FunctionNode>();
        }
        this.functions.add(functionNode);
        return this.functions.size() - 1;
    }
    
    public void addRegExp(final RegExpLiteral regExpLiteral) {
        if (regExpLiteral == null) {
            codeBug();
        }
        if (this.regexps == null) {
            this.regexps = new ArrayList<RegExpLiteral>();
        }
        this.regexps.add(regExpLiteral);
        regExpLiteral.putIntProp(4, this.regexps.size() - 1);
    }
    
    void addSymbol(final Symbol symbol) {
        if (this.variableNames != null) {
            codeBug();
        }
        if (symbol.getDeclType() == 87) {
            ++this.paramCount;
        }
        this.symbols.add(symbol);
    }
    
    public void flattenSymbolTable(final boolean b) {
        if (!b) {
            final ArrayList<Symbol> symbols = new ArrayList<Symbol>();
            if (this.symbolTable != null) {
                for (int i = 0; i < this.symbols.size(); ++i) {
                    final Symbol symbol = this.symbols.get(i);
                    if (symbol.getContainingTable() == this) {
                        symbols.add(symbol);
                    }
                }
            }
            this.symbols = symbols;
        }
        this.variableNames = new String[this.symbols.size()];
        this.isConsts = new boolean[this.symbols.size()];
        for (int j = 0; j < this.symbols.size(); ++j) {
            final Symbol symbol2 = this.symbols.get(j);
            this.variableNames[j] = symbol2.getName();
            this.isConsts[j] = (symbol2.getDeclType() == 154);
            symbol2.setIndex(j);
        }
    }
    
    public int getBaseLineno() {
        return this.lineno;
    }
    
    public Object getCompilerData() {
        return this.compilerData;
    }
    
    public String getEncodedSource() {
        return this.encodedSource;
    }
    
    public int getEncodedSourceEnd() {
        return this.encodedSourceEnd;
    }
    
    public int getEncodedSourceStart() {
        return this.encodedSourceStart;
    }
    
    public int getEndLineno() {
        return this.endLineno;
    }
    
    public int getFunctionCount() {
        if (this.functions == null) {
            return 0;
        }
        return this.functions.size();
    }
    
    public FunctionNode getFunctionNode(final int n) {
        return this.functions.get(n);
    }
    
    public List<FunctionNode> getFunctions() {
        if (this.functions == null) {
            return this.EMPTY_LIST;
        }
        return this.functions;
    }
    
    public int getIndexForNameNode(final Node node) {
        if (this.variableNames == null) {
            codeBug();
        }
        final Scope scope = node.getScope();
        Symbol symbol;
        if (scope == null) {
            symbol = null;
        }
        else {
            symbol = scope.getSymbol(((Name)node).getIdentifier());
        }
        if (symbol == null) {
            return -1;
        }
        return symbol.getIndex();
    }
    
    public String getNextTempName() {
        final StringBuilder sb = new StringBuilder();
        sb.append("$");
        sb.append(this.tempNumber++);
        return sb.toString();
    }
    
    public boolean[] getParamAndVarConst() {
        if (this.variableNames == null) {
            codeBug();
        }
        return this.isConsts;
    }
    
    public int getParamAndVarCount() {
        if (this.variableNames == null) {
            codeBug();
        }
        return this.symbols.size();
    }
    
    public String[] getParamAndVarNames() {
        if (this.variableNames == null) {
            codeBug();
        }
        return this.variableNames;
    }
    
    public int getParamCount() {
        return this.paramCount;
    }
    
    public String getParamOrVarName(final int n) {
        if (this.variableNames == null) {
            codeBug();
        }
        return this.variableNames[n];
    }
    
    public int getRegexpCount() {
        if (this.regexps == null) {
            return 0;
        }
        return this.regexps.size();
    }
    
    public String getRegexpFlags(final int n) {
        return this.regexps.get(n).getFlags();
    }
    
    public String getRegexpString(final int n) {
        return this.regexps.get(n).getValue();
    }
    
    public String getSourceName() {
        return this.sourceName;
    }
    
    public List<Symbol> getSymbols() {
        return this.symbols;
    }
    
    public void setBaseLineno(final int lineno) {
        if (lineno < 0 || this.lineno >= 0) {
            codeBug();
        }
        this.lineno = lineno;
    }
    
    public void setCompilerData(final Object compilerData) {
        this.assertNotNull(compilerData);
        if (this.compilerData != null) {
            throw new IllegalStateException();
        }
        this.compilerData = compilerData;
    }
    
    public void setEncodedSource(final String encodedSource) {
        this.encodedSource = encodedSource;
    }
    
    public void setEncodedSourceBounds(final int encodedSourceStart, final int encodedSourceEnd) {
        this.encodedSourceStart = encodedSourceStart;
        this.encodedSourceEnd = encodedSourceEnd;
    }
    
    public void setEncodedSourceEnd(final int encodedSourceEnd) {
        this.encodedSourceEnd = encodedSourceEnd;
    }
    
    public void setEncodedSourceStart(final int encodedSourceStart) {
        this.encodedSourceStart = encodedSourceStart;
    }
    
    public void setEndLineno(final int endLineno) {
        if (endLineno < 0 || this.endLineno >= 0) {
            codeBug();
        }
        this.endLineno = endLineno;
    }
    
    public void setSourceName(final String sourceName) {
        this.sourceName = sourceName;
    }
    
    public void setSymbols(final List<Symbol> symbols) {
        this.symbols = symbols;
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
