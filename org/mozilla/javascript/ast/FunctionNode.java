package org.mozilla.javascript.ast;

import org.mozilla.javascript.*;
import java.util.*;

public class FunctionNode extends ScriptNode
{
    public static final int FUNCTION_EXPRESSION = 2;
    public static final int FUNCTION_EXPRESSION_STATEMENT = 3;
    public static final int FUNCTION_STATEMENT = 1;
    private static final List<AstNode> NO_PARAMS;
    private AstNode body;
    private Form functionForm;
    private Name functionName;
    private int functionType;
    private List<Node> generatorResumePoints;
    private boolean isExpressionClosure;
    private boolean isGenerator;
    private Map<Node, int[]> liveLocals;
    private int lp;
    private AstNode memberExprNode;
    private boolean needsActivation;
    private List<AstNode> params;
    private int rp;
    
    static {
        NO_PARAMS = Collections.unmodifiableList((List<? extends AstNode>)new ArrayList<AstNode>());
    }
    
    public FunctionNode() {
        this.functionForm = Form.FUNCTION;
        this.lp = -1;
        this.rp = -1;
        this.type = 109;
    }
    
    public FunctionNode(final int n) {
        super(n);
        this.functionForm = Form.FUNCTION;
        this.lp = -1;
        this.rp = -1;
        this.type = 109;
    }
    
    public FunctionNode(final int n, final Name functionName) {
        super(n);
        this.functionForm = Form.FUNCTION;
        this.lp = -1;
        this.rp = -1;
        this.type = 109;
        this.setFunctionName(functionName);
    }
    
    @Override
    public int addFunction(final FunctionNode functionNode) {
        final int addFunction = super.addFunction(functionNode);
        if (this.getFunctionCount() > 0) {
            this.needsActivation = true;
        }
        return addFunction;
    }
    
    public void addLiveLocals(final Node node, final int[] array) {
        if (this.liveLocals == null) {
            this.liveLocals = new HashMap<Node, int[]>();
        }
        this.liveLocals.put(node, array);
    }
    
    public void addParam(final AstNode astNode) {
        this.assertNotNull(astNode);
        if (this.params == null) {
            this.params = new ArrayList<AstNode>();
        }
        this.params.add(astNode);
        astNode.setParent(this);
    }
    
    public void addResumptionPoint(final Node node) {
        if (this.generatorResumePoints == null) {
            this.generatorResumePoints = new ArrayList<Node>();
        }
        this.generatorResumePoints.add(node);
    }
    
    public AstNode getBody() {
        return this.body;
    }
    
    public Name getFunctionName() {
        return this.functionName;
    }
    
    public int getFunctionType() {
        return this.functionType;
    }
    
    public Map<Node, int[]> getLiveLocals() {
        return this.liveLocals;
    }
    
    public int getLp() {
        return this.lp;
    }
    
    public AstNode getMemberExprNode() {
        return this.memberExprNode;
    }
    
    public String getName() {
        if (this.functionName != null) {
            return this.functionName.getIdentifier();
        }
        return "";
    }
    
    public List<AstNode> getParams() {
        if (this.params != null) {
            return this.params;
        }
        return FunctionNode.NO_PARAMS;
    }
    
    public List<Node> getResumptionPoints() {
        return this.generatorResumePoints;
    }
    
    public int getRp() {
        return this.rp;
    }
    
    public boolean isExpressionClosure() {
        return this.isExpressionClosure;
    }
    
    public boolean isGenerator() {
        return this.isGenerator;
    }
    
    public boolean isGetterMethod() {
        return this.functionForm == Form.GETTER;
    }
    
    public boolean isMethod() {
        return this.functionForm == Form.GETTER || this.functionForm == Form.SETTER || this.functionForm == Form.METHOD;
    }
    
    public boolean isNormalMethod() {
        return this.functionForm == Form.METHOD;
    }
    
    public boolean isParam(final AstNode astNode) {
        return this.params != null && this.params.contains(astNode);
    }
    
    public boolean isSetterMethod() {
        return this.functionForm == Form.SETTER;
    }
    
    public boolean requiresActivation() {
        return this.needsActivation;
    }
    
    public void setBody(final AstNode body) {
        this.assertNotNull(body);
        this.body = body;
        if (Boolean.TRUE.equals(body.getProp(25))) {
            this.setIsExpressionClosure(true);
        }
        final int n = body.getPosition() + body.getLength();
        body.setParent(this);
        this.setLength(n - this.position);
        this.setEncodedSourceBounds(this.position, n);
    }
    
    public void setFunctionIsGetterMethod() {
        this.functionForm = Form.GETTER;
    }
    
    public void setFunctionIsNormalMethod() {
        this.functionForm = Form.METHOD;
    }
    
    public void setFunctionIsSetterMethod() {
        this.functionForm = Form.SETTER;
    }
    
    public void setFunctionName(final Name functionName) {
        this.functionName = functionName;
        if (functionName != null) {
            functionName.setParent(this);
        }
    }
    
    public void setFunctionType(final int functionType) {
        this.functionType = functionType;
    }
    
    public void setIsExpressionClosure(final boolean isExpressionClosure) {
        this.isExpressionClosure = isExpressionClosure;
    }
    
    public void setIsGenerator() {
        this.isGenerator = true;
    }
    
    public void setLp(final int lp) {
        this.lp = lp;
    }
    
    public void setMemberExprNode(final AstNode memberExprNode) {
        this.memberExprNode = memberExprNode;
        if (memberExprNode != null) {
            memberExprNode.setParent(this);
        }
    }
    
    public void setParams(final List<AstNode> list) {
        if (list == null) {
            this.params = null;
            return;
        }
        if (this.params != null) {
            this.params.clear();
        }
        final Iterator<AstNode> iterator = list.iterator();
        while (iterator.hasNext()) {
            this.addParam(iterator.next());
        }
    }
    
    public void setParens(final int lp, final int rp) {
        this.lp = lp;
        this.rp = rp;
    }
    
    public void setRequiresActivation() {
        this.needsActivation = true;
    }
    
    public void setRp(final int rp) {
        this.rp = rp;
    }
    
    @Override
    public String toSource(final int n) {
        final StringBuilder sb = new StringBuilder();
        if (!this.isMethod()) {
            sb.append(this.makeIndent(n));
            sb.append("function");
        }
        if (this.functionName != null) {
            sb.append(" ");
            sb.append(this.functionName.toSource(0));
        }
        if (this.params == null) {
            sb.append("() ");
        }
        else {
            sb.append("(");
            this.printList(this.params, sb);
            sb.append(") ");
        }
        if (this.isExpressionClosure) {
            final AstNode body = this.getBody();
            if (body.getLastChild() instanceof ReturnStatement) {
                sb.append(((ReturnStatement)body.getLastChild()).getReturnValue().toSource(0));
                if (this.functionType == 1) {
                    sb.append(";");
                }
            }
            else {
                sb.append(" ");
                sb.append(body.toSource(0));
            }
        }
        else {
            sb.append(this.getBody().toSource(n).trim());
        }
        if (this.functionType == 1 || this.isMethod()) {
            sb.append("\n");
        }
        return sb.toString();
    }
    
    @Override
    public void visit(final NodeVisitor nodeVisitor) {
        if (nodeVisitor.visit(this)) {
            if (this.functionName != null) {
                this.functionName.visit(nodeVisitor);
            }
            final Iterator<AstNode> iterator = this.getParams().iterator();
            while (iterator.hasNext()) {
                iterator.next().visit(nodeVisitor);
            }
            this.getBody().visit(nodeVisitor);
            if (!this.isExpressionClosure && this.memberExprNode != null) {
                this.memberExprNode.visit(nodeVisitor);
            }
        }
    }
    
    public enum Form
    {
        FUNCTION, 
        GETTER, 
        METHOD, 
        SETTER;
    }
}
