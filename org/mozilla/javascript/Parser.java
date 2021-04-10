package org.mozilla.javascript;

import java.util.*;
import java.io.*;
import org.mozilla.javascript.ast.*;

public class Parser
{
    public static final int ARGC_LIMIT = 65536;
    static final int CLEAR_TI_MASK = 65535;
    private static final int GET_ENTRY = 2;
    private static final int METHOD_ENTRY = 8;
    private static final int PROP_ENTRY = 1;
    private static final int SET_ENTRY = 4;
    static final int TI_AFTER_EOL = 65536;
    static final int TI_CHECK_LABEL = 131072;
    boolean calledByCompileFunction;
    CompilerEnvirons compilerEnv;
    private int currentFlaggedToken;
    private Comment currentJsDocComment;
    private LabeledStatement currentLabel;
    Scope currentScope;
    ScriptNode currentScriptOrFn;
    private int currentToken;
    private int endFlags;
    private IdeErrorReporter errorCollector;
    private ErrorReporter errorReporter;
    private boolean inDestructuringAssignment;
    private boolean inForInit;
    protected boolean inUseStrictDirective;
    private Map<String, LabeledStatement> labelSet;
    private List<Jump> loopAndSwitchSet;
    private List<Loop> loopSet;
    protected int nestingOfFunction;
    private boolean parseFinished;
    private int prevNameTokenLineno;
    private int prevNameTokenStart;
    private String prevNameTokenString;
    private List<Comment> scannedComments;
    private char[] sourceChars;
    private String sourceURI;
    private int syntaxErrorCount;
    private TokenStream ts;
    
    public Parser() {
        this(new CompilerEnvirons());
    }
    
    public Parser(final CompilerEnvirons compilerEnvirons) {
        this(compilerEnvirons, compilerEnvirons.getErrorReporter());
    }
    
    public Parser(final CompilerEnvirons compilerEnv, final ErrorReporter errorReporter) {
        this.currentFlaggedToken = 0;
        this.prevNameTokenString = "";
        this.compilerEnv = compilerEnv;
        this.errorReporter = errorReporter;
        if (errorReporter instanceof IdeErrorReporter) {
            this.errorCollector = (IdeErrorReporter)errorReporter;
        }
    }
    
    private void addError(String lookupMessage, final String s, final int n, final int n2, final int n3, final String s2, final int n4) {
        ++this.syntaxErrorCount;
        lookupMessage = this.lookupMessage(lookupMessage, s);
        if (this.errorCollector != null) {
            this.errorCollector.error(lookupMessage, this.sourceURI, n, n2);
            return;
        }
        this.errorReporter.error(lookupMessage, this.sourceURI, n3, s2, n4);
    }
    
    private AstNode addExpr() throws IOException {
        AstNode mulExpr = this.mulExpr();
        while (true) {
            final int peekToken = this.peekToken();
            final int tokenBeg = this.ts.tokenBeg;
            if (peekToken != 21 && peekToken != 22) {
                break;
            }
            this.consumeToken();
            mulExpr = new InfixExpression(peekToken, mulExpr, this.mulExpr(), tokenBeg);
        }
        return mulExpr;
    }
    
    private void addStrictWarning(final String s, final String s2, final int n, final int n2, final int n3, final String s3, final int n4) {
        if (this.compilerEnv.isStrictMode()) {
            this.addWarning(s, s2, n, n2, n3, s3, n4);
        }
    }
    
    private void addWarning(final String s, final String s2, final int n, final int n2, final int n3, final String s3, final int n4) {
        final String lookupMessage = this.lookupMessage(s, s2);
        if (this.compilerEnv.reportWarningAsError()) {
            this.addError(s, s2, n, n2, n3, s3, n4);
            return;
        }
        if (this.errorCollector != null) {
            this.errorCollector.warning(lookupMessage, this.sourceURI, n, n2);
            return;
        }
        this.errorReporter.warning(lookupMessage, this.sourceURI, n3, s3, n4);
    }
    
    private AstNode andExpr() throws IOException {
        AstNode bitOrExpr = this.bitOrExpr();
        if (this.matchToken(105)) {
            bitOrExpr = new InfixExpression(105, bitOrExpr, this.andExpr(), this.ts.tokenBeg);
        }
        return bitOrExpr;
    }
    
    private List<AstNode> argumentList() throws IOException {
        if (this.matchToken(88)) {
            return null;
        }
        final ArrayList<AstNode> list = new ArrayList<AstNode>();
        final boolean inForInit = this.inForInit;
        this.inForInit = false;
        try {
            do {
                if (this.peekToken() == 72) {
                    this.reportError("msg.yield.parenthesized");
                }
                final AstNode assignExpr = this.assignExpr();
                if (this.peekToken() == 119) {
                    try {
                        list.add(this.generatorExpression(assignExpr, 0, true));
                    }
                    catch (IOException ex) {}
                }
                else {
                    list.add(assignExpr);
                }
            } while (this.matchToken(89));
            this.inForInit = inForInit;
            this.mustMatchToken(88, "msg.no.paren.arg");
            return list;
        }
        finally {
            this.inForInit = inForInit;
        }
    }
    
    private AstNode arrayComprehension(final AstNode result, final int n) throws IOException {
        final ArrayList<ArrayComprehensionLoop> loops = new ArrayList<ArrayComprehensionLoop>();
        while (this.peekToken() == 119) {
            loops.add(this.arrayComprehensionLoop());
        }
        int ifPosition = -1;
        ConditionData condition = null;
        if (this.peekToken() == 112) {
            this.consumeToken();
            ifPosition = this.ts.tokenBeg - n;
            condition = this.condition();
        }
        this.mustMatchToken(84, "msg.no.bracket.arg");
        final ArrayComprehension arrayComprehension = new ArrayComprehension(n, this.ts.tokenEnd - n);
        arrayComprehension.setResult(result);
        arrayComprehension.setLoops(loops);
        if (condition != null) {
            arrayComprehension.setIfPosition(ifPosition);
            arrayComprehension.setFilter(condition.condition);
            arrayComprehension.setFilterLp(condition.lp - n);
            arrayComprehension.setFilterRp(condition.rp - n);
        }
        return arrayComprehension;
    }
    
    private ArrayComprehensionLoop arrayComprehensionLoop() throws IOException {
        if (this.nextToken() != 119) {
            this.codeBug();
        }
        while (true) {
            final int tokenBeg = this.ts.tokenBeg;
            final int n = -1;
            int n2 = -1;
            int n3 = -1;
            int inPosition = -1;
            final ArrayComprehensionLoop arrayComprehensionLoop = new ArrayComprehensionLoop(tokenBeg);
            this.pushScope(arrayComprehensionLoop);
            int eachPosition = n;
            while (true) {
                try {
                    if (this.matchToken(39)) {
                        if (this.ts.getString().equals("each")) {
                            eachPosition = this.ts.tokenBeg - tokenBeg;
                        }
                        else {
                            this.reportError("msg.no.paren.for");
                            eachPosition = n;
                        }
                    }
                    if (this.mustMatchToken(87, "msg.no.paren.for")) {
                        n2 = this.ts.tokenBeg - tokenBeg;
                    }
                    AstNode iterator = null;
                    final int peekToken = this.peekToken();
                    if (peekToken != 39) {
                        if (peekToken != 83 && peekToken != 85) {
                            this.reportError("msg.bad.var");
                        }
                        else {
                            iterator = this.destructuringPrimaryExpr();
                            this.markDestructuring(iterator);
                        }
                    }
                    else {
                        this.consumeToken();
                        iterator = this.createNameNode();
                    }
                    final int type = iterator.getType();
                    final boolean isForEach = true;
                    if (type == 39) {
                        this.defineSymbol(153, this.ts.getString(), true);
                    }
                    if (this.mustMatchToken(52, "msg.in.after.for.name")) {
                        inPosition = this.ts.tokenBeg - tokenBeg;
                    }
                    final AstNode expr = this.expr();
                    if (this.mustMatchToken(88, "msg.no.paren.for.ctrl")) {
                        n3 = this.ts.tokenBeg - tokenBeg;
                    }
                    arrayComprehensionLoop.setLength(this.ts.tokenEnd - tokenBeg);
                    arrayComprehensionLoop.setIterator(iterator);
                    arrayComprehensionLoop.setIteratedObject(expr);
                    arrayComprehensionLoop.setInPosition(inPosition);
                    arrayComprehensionLoop.setEachPosition(eachPosition);
                    if (eachPosition != -1) {
                        arrayComprehensionLoop.setIsForEach(isForEach);
                        arrayComprehensionLoop.setParens(n2, n3);
                        return arrayComprehensionLoop;
                    }
                }
                finally {
                    this.popScope();
                }
                final boolean isForEach = false;
                continue;
            }
        }
    }
    
    private AstNode arrayLiteral() throws IOException {
        if (this.currentToken != 83) {
            this.codeBug();
        }
        final int tokenBeg = this.ts.tokenBeg;
        final int tokenEnd = this.ts.tokenEnd;
        final ArrayList<Object> list = new ArrayList<Object>();
        final ArrayLiteral arrayLiteral = new ArrayLiteral(tokenBeg);
        int tokenEnd2 = -1;
        int n = 0;
        int n2 = 1;
        int skipCount = 0;
        int n3;
        while (true) {
            final int peekToken = this.peekToken();
            if (peekToken == 89) {
                this.consumeToken();
                tokenEnd2 = this.ts.tokenEnd;
                if (n2 == 0) {
                    n2 = 1;
                }
                else {
                    list.add(new EmptyExpression(this.ts.tokenBeg, 1));
                    ++skipCount;
                }
            }
            else if (peekToken == 84) {
                this.consumeToken();
                final int tokenEnd3 = this.ts.tokenEnd;
                final int size = list.size();
                if (n2 != 0) {
                    n = 1;
                }
                arrayLiteral.setDestructuringLength(size + n);
                arrayLiteral.setSkipCount(skipCount);
                n3 = tokenEnd3;
                if (tokenEnd2 != -1) {
                    this.warnTrailingComma(tokenBeg, list, tokenEnd2);
                    n3 = tokenEnd3;
                    break;
                }
                break;
            }
            else {
                if (peekToken == 119 && n2 == 0 && list.size() == 1) {
                    return this.arrayComprehension((AstNode)list.get(0), tokenBeg);
                }
                if (peekToken == 0) {
                    this.reportError("msg.no.bracket.arg");
                    n3 = tokenEnd;
                    break;
                }
                if (n2 == 0) {
                    this.reportError("msg.no.bracket.arg");
                }
                list.add(this.assignExpr());
                n2 = 0;
                tokenEnd2 = -1;
            }
        }
        final Iterator<AstNode> iterator = list.iterator();
        while (iterator.hasNext()) {
            arrayLiteral.addElement(iterator.next());
        }
        arrayLiteral.setLength(n3 - tokenBeg);
        return arrayLiteral;
    }
    
    private AstNode assignExpr() throws IOException {
        final int peekToken = this.peekToken();
        if (peekToken == 72) {
            return this.returnOrYield(peekToken, true);
        }
        final AstNode condExpr = this.condExpr();
        final int peekToken2 = this.peekToken();
        if (90 <= peekToken2 && peekToken2 <= 101) {
            this.consumeToken();
            final Comment andResetJsDoc = this.getAndResetJsDoc();
            this.markDestructuring(condExpr);
            final Assignment assignment = new Assignment(peekToken2, condExpr, this.assignExpr(), this.ts.tokenBeg);
            if (andResetJsDoc != null) {
                assignment.setJsDocNode(andResetJsDoc);
            }
            return assignment;
        }
        if (peekToken2 == 82 && this.currentJsDocComment != null) {
            condExpr.setJsDocNode(this.getAndResetJsDoc());
        }
        return condExpr;
    }
    
    private AstNode attributeAccess() throws IOException {
        final int nextToken = this.nextToken();
        final int tokenBeg = this.ts.tokenBeg;
        if (nextToken == 23) {
            this.saveNameTokenData(this.ts.tokenBeg, "*", this.ts.lineno);
            return this.propertyName(tokenBeg, "*", 0);
        }
        if (nextToken == 39) {
            return this.propertyName(tokenBeg, this.ts.getString(), 0);
        }
        if (nextToken != 83) {
            this.reportError("msg.no.name.after.xmlAttr");
            return this.makeErrorNode();
        }
        return this.xmlElemRef(tokenBeg, null, -1);
    }
    
    private void autoInsertSemicolon(final AstNode astNode) throws IOException {
        final int peekFlaggedToken = this.peekFlaggedToken();
        final int position = astNode.getPosition();
        final int n = 0xFFFF & peekFlaggedToken;
        if (n != 82) {
            if (n != 86) {
                switch (n) {
                    default: {
                        if ((0x10000 & peekFlaggedToken) == 0x0) {
                            this.reportError("msg.no.semi.stmt");
                            return;
                        }
                        this.warnMissingSemi(position, this.nodeEnd(astNode));
                        return;
                    }
                    case -1:
                    case 0: {
                        break;
                    }
                }
            }
            this.warnMissingSemi(position, this.nodeEnd(astNode));
            return;
        }
        this.consumeToken();
        astNode.setLength(this.ts.tokenEnd - position);
    }
    
    private AstNode bitAndExpr() throws IOException {
        AstNode eqExpr = this.eqExpr();
        while (this.matchToken(11)) {
            eqExpr = new InfixExpression(11, eqExpr, this.eqExpr(), this.ts.tokenBeg);
        }
        return eqExpr;
    }
    
    private AstNode bitOrExpr() throws IOException {
        AstNode bitXorExpr = this.bitXorExpr();
        while (this.matchToken(9)) {
            bitXorExpr = new InfixExpression(9, bitXorExpr, this.bitXorExpr(), this.ts.tokenBeg);
        }
        return bitXorExpr;
    }
    
    private AstNode bitXorExpr() throws IOException {
        AstNode bitAndExpr = this.bitAndExpr();
        while (this.matchToken(10)) {
            bitAndExpr = new InfixExpression(10, bitAndExpr, this.bitAndExpr(), this.ts.tokenBeg);
        }
        return bitAndExpr;
    }
    
    private AstNode block() throws IOException {
        if (this.currentToken != 85) {
            this.codeBug();
        }
        this.consumeToken();
        final int tokenBeg = this.ts.tokenBeg;
        final Scope scope = new Scope(tokenBeg);
        scope.setLineno(this.ts.lineno);
        this.pushScope(scope);
        try {
            this.statements(scope);
            this.mustMatchToken(86, "msg.no.brace.block");
            scope.setLength(this.ts.tokenEnd - tokenBeg);
            return scope;
        }
        finally {
            this.popScope();
        }
    }
    
    private BreakStatement breakStatement() throws IOException {
        if (this.currentToken != 120) {
            this.codeBug();
        }
        this.consumeToken();
        final int lineno = this.ts.lineno;
        final int tokenBeg = this.ts.tokenBeg;
        int n = this.ts.tokenEnd;
        Name nameNode = null;
        if (this.peekTokenOrEOL() == 39) {
            nameNode = this.createNameNode();
            n = this.getNodeEnd(nameNode);
        }
        final LabeledStatement matchJumpLabelName = this.matchJumpLabelName();
        Label firstLabel;
        if (matchJumpLabelName == null) {
            firstLabel = null;
        }
        else {
            firstLabel = matchJumpLabelName.getFirstLabel();
        }
        Jump breakTarget = firstLabel;
        if (firstLabel == null) {
            breakTarget = firstLabel;
            if (nameNode == null) {
                if (this.loopAndSwitchSet != null && this.loopAndSwitchSet.size() != 0) {
                    breakTarget = this.loopAndSwitchSet.get(this.loopAndSwitchSet.size() - 1);
                }
                else {
                    breakTarget = firstLabel;
                    if (nameNode == null) {
                        this.reportError("msg.bad.break", tokenBeg, n - tokenBeg);
                        breakTarget = firstLabel;
                    }
                }
            }
        }
        final BreakStatement breakStatement = new BreakStatement(tokenBeg, n - tokenBeg);
        breakStatement.setBreakLabel(nameNode);
        if (breakTarget != null) {
            breakStatement.setBreakTarget(breakTarget);
        }
        breakStatement.setLineno(lineno);
        return breakStatement;
    }
    
    private void checkBadIncDec(final UnaryExpression unaryExpression) {
        final int type = this.removeParens(unaryExpression.getOperand()).getType();
        if (type != 39 && type != 33 && type != 36 && type != 67 && type != 38) {
            String s;
            if (unaryExpression.getType() == 106) {
                s = "msg.bad.incr";
            }
            else {
                s = "msg.bad.decr";
            }
            this.reportError(s);
        }
    }
    
    private void checkCallRequiresActivation(final AstNode astNode) {
        if ((astNode.getType() == 39 && "eval".equals(((Name)astNode).getIdentifier())) || (astNode.getType() == 33 && "eval".equals(((PropertyGet)astNode).getProperty().getIdentifier()))) {
            this.setRequiresActivation();
        }
    }
    
    private RuntimeException codeBug() throws RuntimeException {
        final StringBuilder sb = new StringBuilder();
        sb.append("ts.cursor=");
        sb.append(this.ts.cursor);
        sb.append(", ts.tokenBeg=");
        sb.append(this.ts.tokenBeg);
        sb.append(", currentToken=");
        sb.append(this.currentToken);
        throw Kit.codeBug(sb.toString());
    }
    
    private AstNode condExpr() throws IOException {
        final AstNode orExpr = this.orExpr();
        if (this.matchToken(102)) {
            final int lineno = this.ts.lineno;
            final int tokenBeg = this.ts.tokenBeg;
            int tokenBeg2 = -1;
            final boolean inForInit = this.inForInit;
            this.inForInit = false;
            try {
                final AstNode assignExpr = this.assignExpr();
                this.inForInit = inForInit;
                if (this.mustMatchToken(103, "msg.no.colon.cond")) {
                    tokenBeg2 = this.ts.tokenBeg;
                }
                final AstNode assignExpr2 = this.assignExpr();
                final int position = orExpr.getPosition();
                final ConditionalExpression conditionalExpression = new ConditionalExpression(position, this.getNodeEnd(assignExpr2) - position);
                conditionalExpression.setLineno(lineno);
                conditionalExpression.setTestExpression(orExpr);
                conditionalExpression.setTrueExpression(assignExpr);
                conditionalExpression.setFalseExpression(assignExpr2);
                conditionalExpression.setQuestionMarkPosition(tokenBeg - position);
                conditionalExpression.setColonPosition(tokenBeg2 - position);
                return conditionalExpression;
            }
            finally {
                this.inForInit = inForInit;
            }
        }
        return orExpr;
    }
    
    private ConditionData condition() throws IOException {
        final ConditionData conditionData = new ConditionData();
        if (this.mustMatchToken(87, "msg.no.paren.cond")) {
            conditionData.lp = this.ts.tokenBeg;
        }
        conditionData.condition = this.expr();
        if (this.mustMatchToken(88, "msg.no.paren.after.cond")) {
            conditionData.rp = this.ts.tokenBeg;
        }
        if (conditionData.condition instanceof Assignment) {
            this.addStrictWarning("msg.equal.as.assign", "", conditionData.condition.getPosition(), conditionData.condition.getLength());
        }
        return conditionData;
    }
    
    private void consumeToken() {
        this.currentFlaggedToken = 0;
    }
    
    private ContinueStatement continueStatement() throws IOException {
        if (this.currentToken != 121) {
            this.codeBug();
        }
        this.consumeToken();
        final int lineno = this.ts.lineno;
        final int tokenBeg = this.ts.tokenBeg;
        int n = this.ts.tokenEnd;
        Name nameNode = null;
        if (this.peekTokenOrEOL() == 39) {
            nameNode = this.createNameNode();
            n = this.getNodeEnd(nameNode);
        }
        final LabeledStatement matchJumpLabelName = this.matchJumpLabelName();
        Loop target = null;
        if (matchJumpLabelName == null && nameNode == null) {
            if (this.loopSet != null && this.loopSet.size() != 0) {
                target = this.loopSet.get(this.loopSet.size() - 1);
            }
            else {
                this.reportError("msg.continue.outside");
            }
        }
        else {
            if (matchJumpLabelName == null || !(matchJumpLabelName.getStatement() instanceof Loop)) {
                this.reportError("msg.continue.nonloop", tokenBeg, n - tokenBeg);
            }
            if (matchJumpLabelName == null) {
                target = null;
            }
            else {
                target = (Loop)matchJumpLabelName.getStatement();
            }
        }
        final ContinueStatement continueStatement = new ContinueStatement(tokenBeg, n - tokenBeg);
        if (target != null) {
            continueStatement.setTarget(target);
        }
        continueStatement.setLabel(nameNode);
        continueStatement.setLineno(lineno);
        return continueStatement;
    }
    
    private Name createNameNode() {
        return this.createNameNode(false, 39);
    }
    
    private Name createNameNode(final boolean b, final int n) {
        int n2 = this.ts.tokenBeg;
        String s = this.ts.getString();
        int lineno = this.ts.lineno;
        if (!"".equals(this.prevNameTokenString)) {
            n2 = this.prevNameTokenStart;
            s = this.prevNameTokenString;
            lineno = this.prevNameTokenLineno;
            this.prevNameTokenStart = 0;
            this.prevNameTokenString = "";
            this.prevNameTokenLineno = 0;
        }
        String s2;
        if ((s2 = s) == null) {
            if (this.compilerEnv.isIdeMode()) {
                s2 = "";
            }
            else {
                this.codeBug();
                s2 = s;
            }
        }
        final Name name = new Name(n2, s2);
        name.setLineno(lineno);
        if (b) {
            this.checkActivationName(s2, n);
        }
        return name;
    }
    
    private StringLiteral createStringLiteral() {
        final int tokenBeg = this.ts.tokenBeg;
        final StringLiteral stringLiteral = new StringLiteral(tokenBeg, this.ts.tokenEnd - tokenBeg);
        stringLiteral.setLineno(this.ts.lineno);
        stringLiteral.setValue(this.ts.getString());
        stringLiteral.setQuoteCharacter(this.ts.getQuoteChar());
        return stringLiteral;
    }
    
    private AstNode defaultXmlNamespace() throws IOException {
        if (this.currentToken != 116) {
            this.codeBug();
        }
        this.consumeToken();
        this.mustHaveXML();
        this.setRequiresActivation();
        final int lineno = this.ts.lineno;
        final int tokenBeg = this.ts.tokenBeg;
        if (!this.matchToken(39) || !"xml".equals(this.ts.getString())) {
            this.reportError("msg.bad.namespace");
        }
        if (!this.matchToken(39) || !"namespace".equals(this.ts.getString())) {
            this.reportError("msg.bad.namespace");
        }
        if (!this.matchToken(90)) {
            this.reportError("msg.bad.namespace");
        }
        final AstNode expr = this.expr();
        final UnaryExpression unaryExpression = new UnaryExpression(tokenBeg, this.getNodeEnd(expr) - tokenBeg);
        unaryExpression.setOperator(74);
        unaryExpression.setOperand(expr);
        unaryExpression.setLineno(lineno);
        return new ExpressionStatement(unaryExpression, true);
    }
    
    private AstNode destructuringPrimaryExpr() throws IOException, ParserException {
        try {
            this.inDestructuringAssignment = true;
            return this.primaryExpr();
        }
        finally {
            this.inDestructuringAssignment = false;
        }
    }
    
    private DoLoop doLoop() throws IOException {
        if (this.currentToken != 118) {
            this.codeBug();
        }
        this.consumeToken();
        final int tokenBeg = this.ts.tokenBeg;
        final DoLoop doLoop = new DoLoop(tokenBeg);
        doLoop.setLineno(this.ts.lineno);
        this.enterLoop(doLoop);
        try {
            final AstNode statement = this.statement();
            this.mustMatchToken(117, "msg.no.while.do");
            doLoop.setWhilePosition(this.ts.tokenBeg - tokenBeg);
            final ConditionData condition = this.condition();
            doLoop.setCondition(condition.condition);
            doLoop.setParens(condition.lp - tokenBeg, condition.rp - tokenBeg);
            int n = this.getNodeEnd(statement);
            doLoop.setBody(statement);
            this.exitLoop();
            if (this.matchToken(82)) {
                n = this.ts.tokenEnd;
            }
            doLoop.setLength(n - tokenBeg);
            return doLoop;
        }
        finally {
            this.exitLoop();
        }
    }
    
    private void enterLoop(final Loop loop) {
        if (this.loopSet == null) {
            this.loopSet = new ArrayList<Loop>();
        }
        this.loopSet.add(loop);
        if (this.loopAndSwitchSet == null) {
            this.loopAndSwitchSet = new ArrayList<Jump>();
        }
        this.loopAndSwitchSet.add(loop);
        this.pushScope(loop);
        if (this.currentLabel != null) {
            this.currentLabel.setStatement(loop);
            this.currentLabel.getFirstLabel().setLoop(loop);
            loop.setRelative(-this.currentLabel.getPosition());
        }
    }
    
    private void enterSwitch(final SwitchStatement switchStatement) {
        if (this.loopAndSwitchSet == null) {
            this.loopAndSwitchSet = new ArrayList<Jump>();
        }
        this.loopAndSwitchSet.add(switchStatement);
    }
    
    private AstNode eqExpr() throws IOException {
        AstNode relExpr = this.relExpr();
        while (true) {
            final int peekToken = this.peekToken();
            final int tokenBeg = this.ts.tokenBeg;
            switch (peekToken) {
                default: {
                    return relExpr;
                }
                case 12:
                case 13:
                case 46:
                case 47: {
                    this.consumeToken();
                    int n = peekToken;
                    if (this.compilerEnv.getLanguageVersion() == 120) {
                        if (peekToken == 12) {
                            n = 46;
                        }
                        else {
                            n = n;
                            if (peekToken == 13) {
                                n = 47;
                            }
                        }
                    }
                    relExpr = new InfixExpression(n, relExpr, this.relExpr(), tokenBeg);
                    continue;
                }
            }
        }
    }
    
    private void exitLoop() {
        final Loop loop = this.loopSet.remove(this.loopSet.size() - 1);
        this.loopAndSwitchSet.remove(this.loopAndSwitchSet.size() - 1);
        if (loop.getParent() != null) {
            loop.setRelative(loop.getParent().getPosition());
        }
        this.popScope();
    }
    
    private void exitSwitch() {
        this.loopAndSwitchSet.remove(this.loopAndSwitchSet.size() - 1);
    }
    
    private AstNode expr() throws IOException {
        AstNode assignExpr = this.assignExpr();
        final int position = assignExpr.getPosition();
        while (this.matchToken(89)) {
            final int tokenBeg = this.ts.tokenBeg;
            if (this.compilerEnv.isStrictMode() && !assignExpr.hasSideEffects()) {
                this.addStrictWarning("msg.no.side.effects", "", position, this.nodeEnd(assignExpr) - position);
            }
            if (this.peekToken() == 72) {
                this.reportError("msg.yield.parenthesized");
            }
            assignExpr = new InfixExpression(89, assignExpr, this.assignExpr(), tokenBeg);
        }
        return assignExpr;
    }
    
    private Loop forLoop() throws IOException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: getfield        org/mozilla/javascript/Parser.currentToken:I
        //     4: bipush          119
        //     6: if_icmpeq       14
        //     9: aload_0        
        //    10: invokespecial   org/mozilla/javascript/Parser.codeBug:()Ljava/lang/RuntimeException;
        //    13: pop            
        //    14: aload_0        
        //    15: invokespecial   org/mozilla/javascript/Parser.consumeToken:()V
        //    18: aload_0        
        //    19: getfield        org/mozilla/javascript/Parser.ts:Lorg/mozilla/javascript/TokenStream;
        //    22: getfield        org/mozilla/javascript/TokenStream.tokenBeg:I
        //    25: istore          6
        //    27: aload_0        
        //    28: getfield        org/mozilla/javascript/Parser.ts:Lorg/mozilla/javascript/TokenStream;
        //    31: getfield        org/mozilla/javascript/TokenStream.lineno:I
        //    34: istore          7
        //    36: iconst_m1      
        //    37: istore_1       
        //    38: iconst_m1      
        //    39: istore_2       
        //    40: aconst_null    
        //    41: astore          11
        //    43: new             Lorg/mozilla/javascript/ast/Scope;
        //    46: dup            
        //    47: invokespecial   org/mozilla/javascript/ast/Scope.<init>:()V
        //    50: astore          13
        //    52: aload_0        
        //    53: aload           13
        //    55: invokevirtual   org/mozilla/javascript/Parser.pushScope:(Lorg/mozilla/javascript/ast/Scope;)V
        //    58: aload_0        
        //    59: bipush          39
        //    61: invokespecial   org/mozilla/javascript/Parser.matchToken:(I)Z
        //    64: istore          8
        //    66: iload           8
        //    68: ifeq            130
        //    71: ldc_w           "each"
        //    74: aload_0        
        //    75: getfield        org/mozilla/javascript/Parser.ts:Lorg/mozilla/javascript/TokenStream;
        //    78: invokevirtual   org/mozilla/javascript/TokenStream.getString:()Ljava/lang/String;
        //    81: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //    84: istore          8
        //    86: iload           8
        //    88: ifeq            115
        //    91: iconst_1       
        //    92: istore          8
        //    94: aload_0        
        //    95: getfield        org/mozilla/javascript/Parser.ts:Lorg/mozilla/javascript/TokenStream;
        //    98: getfield        org/mozilla/javascript/TokenStream.tokenBeg:I
        //   101: istore_1       
        //   102: iload_1        
        //   103: iload           6
        //   105: isub           
        //   106: istore_1       
        //   107: goto            133
        //   110: astore          10
        //   112: goto            167
        //   115: aload_0        
        //   116: ldc_w           "msg.no.paren.for"
        //   119: invokevirtual   org/mozilla/javascript/Parser.reportError:(Ljava/lang/String;)V
        //   122: goto            130
        //   125: astore          10
        //   127: goto            651
        //   130: iconst_0       
        //   131: istore          8
        //   133: aload_0        
        //   134: bipush          87
        //   136: ldc_w           "msg.no.paren.for"
        //   139: invokespecial   org/mozilla/javascript/Parser.mustMatchToken:(ILjava/lang/String;)Z
        //   142: istore          9
        //   144: iload           9
        //   146: ifeq            170
        //   149: aload_0        
        //   150: getfield        org/mozilla/javascript/Parser.ts:Lorg/mozilla/javascript/TokenStream;
        //   153: getfield        org/mozilla/javascript/TokenStream.tokenBeg:I
        //   156: istore_2       
        //   157: iload_2        
        //   158: iload           6
        //   160: isub           
        //   161: istore_2       
        //   162: goto            170
        //   165: astore          10
        //   167: goto            651
        //   170: aload_0        
        //   171: aload_0        
        //   172: invokespecial   org/mozilla/javascript/Parser.peekToken:()I
        //   175: invokespecial   org/mozilla/javascript/Parser.forLoopInit:(I)Lorg/mozilla/javascript/ast/AstNode;
        //   178: astore          14
        //   180: aload_0        
        //   181: bipush          52
        //   183: invokespecial   org/mozilla/javascript/Parser.matchToken:(I)Z
        //   186: istore          9
        //   188: iload           9
        //   190: ifeq            221
        //   193: aload_0        
        //   194: getfield        org/mozilla/javascript/Parser.ts:Lorg/mozilla/javascript/TokenStream;
        //   197: getfield        org/mozilla/javascript/TokenStream.tokenBeg:I
        //   200: iload           6
        //   202: isub           
        //   203: istore_3       
        //   204: aload_0        
        //   205: invokespecial   org/mozilla/javascript/Parser.expr:()Lorg/mozilla/javascript/ast/AstNode;
        //   208: astore          10
        //   210: iconst_1       
        //   211: istore          4
        //   213: goto            360
        //   216: astore          10
        //   218: goto            167
        //   221: aload_0        
        //   222: bipush          82
        //   224: ldc_w           "msg.no.semi.for"
        //   227: invokespecial   org/mozilla/javascript/Parser.mustMatchToken:(ILjava/lang/String;)Z
        //   230: pop            
        //   231: aload_0        
        //   232: invokespecial   org/mozilla/javascript/Parser.peekToken:()I
        //   235: istore_3       
        //   236: iload_3        
        //   237: bipush          82
        //   239: if_icmpne       286
        //   242: aload_0        
        //   243: getfield        org/mozilla/javascript/Parser.ts:Lorg/mozilla/javascript/TokenStream;
        //   246: getfield        org/mozilla/javascript/TokenStream.tokenBeg:I
        //   249: istore_3       
        //   250: new             Lorg/mozilla/javascript/ast/EmptyExpression;
        //   253: dup            
        //   254: iload_3        
        //   255: iconst_1       
        //   256: invokespecial   org/mozilla/javascript/ast/EmptyExpression.<init>:(II)V
        //   259: astore          10
        //   261: aload           10
        //   263: aload_0        
        //   264: getfield        org/mozilla/javascript/Parser.ts:Lorg/mozilla/javascript/TokenStream;
        //   267: getfield        org/mozilla/javascript/TokenStream.lineno:I
        //   270: invokevirtual   org/mozilla/javascript/ast/AstNode.setLineno:(I)V
        //   273: goto            292
        //   276: astore          10
        //   278: goto            167
        //   281: astore          10
        //   283: goto            651
        //   286: aload_0        
        //   287: invokespecial   org/mozilla/javascript/Parser.expr:()Lorg/mozilla/javascript/ast/AstNode;
        //   290: astore          10
        //   292: aload_0        
        //   293: bipush          82
        //   295: ldc_w           "msg.no.semi.for.cond"
        //   298: invokespecial   org/mozilla/javascript/Parser.mustMatchToken:(ILjava/lang/String;)Z
        //   301: pop            
        //   302: aload_0        
        //   303: getfield        org/mozilla/javascript/Parser.ts:Lorg/mozilla/javascript/TokenStream;
        //   306: getfield        org/mozilla/javascript/TokenStream.tokenEnd:I
        //   309: istore_3       
        //   310: aload_0        
        //   311: invokespecial   org/mozilla/javascript/Parser.peekToken:()I
        //   314: istore          4
        //   316: iload           4
        //   318: bipush          88
        //   320: if_icmpne       349
        //   323: new             Lorg/mozilla/javascript/ast/EmptyExpression;
        //   326: dup            
        //   327: iload_3        
        //   328: iconst_1       
        //   329: invokespecial   org/mozilla/javascript/ast/EmptyExpression.<init>:(II)V
        //   332: astore          11
        //   334: aload           11
        //   336: aload_0        
        //   337: getfield        org/mozilla/javascript/Parser.ts:Lorg/mozilla/javascript/TokenStream;
        //   340: getfield        org/mozilla/javascript/TokenStream.lineno:I
        //   343: invokevirtual   org/mozilla/javascript/ast/AstNode.setLineno:(I)V
        //   346: goto            355
        //   349: aload_0        
        //   350: invokespecial   org/mozilla/javascript/Parser.expr:()Lorg/mozilla/javascript/ast/AstNode;
        //   353: astore          11
        //   355: iconst_0       
        //   356: istore          4
        //   358: iconst_m1      
        //   359: istore_3       
        //   360: aload_0        
        //   361: bipush          88
        //   363: ldc_w           "msg.no.paren.for.ctrl"
        //   366: invokespecial   org/mozilla/javascript/Parser.mustMatchToken:(ILjava/lang/String;)Z
        //   369: istore          9
        //   371: iload           9
        //   373: ifeq            400
        //   376: aload_0        
        //   377: getfield        org/mozilla/javascript/Parser.ts:Lorg/mozilla/javascript/TokenStream;
        //   380: getfield        org/mozilla/javascript/TokenStream.tokenBeg:I
        //   383: istore          5
        //   385: iload           5
        //   387: iload           6
        //   389: isub           
        //   390: istore          5
        //   392: goto            403
        //   395: astore          10
        //   397: goto            167
        //   400: iconst_m1      
        //   401: istore          5
        //   403: iload           4
        //   405: ifeq            498
        //   408: new             Lorg/mozilla/javascript/ast/ForInLoop;
        //   411: dup            
        //   412: iload           6
        //   414: invokespecial   org/mozilla/javascript/ast/ForInLoop.<init>:(I)V
        //   417: astore          11
        //   419: aload           14
        //   421: instanceof      Lorg/mozilla/javascript/ast/VariableDeclaration;
        //   424: ifeq            667
        //   427: aload           14
        //   429: checkcast       Lorg/mozilla/javascript/ast/VariableDeclaration;
        //   432: invokevirtual   org/mozilla/javascript/ast/VariableDeclaration.getVariables:()Ljava/util/List;
        //   435: invokeinterface java/util/List.size:()I
        //   440: istore          4
        //   442: iload           4
        //   444: iconst_1       
        //   445: if_icmple       458
        //   448: aload_0        
        //   449: ldc_w           "msg.mult.index"
        //   452: invokevirtual   org/mozilla/javascript/Parser.reportError:(Ljava/lang/String;)V
        //   455: goto            458
        //   458: aload           11
        //   460: aload           14
        //   462: invokevirtual   org/mozilla/javascript/ast/ForInLoop.setIterator:(Lorg/mozilla/javascript/ast/AstNode;)V
        //   465: aload           11
        //   467: aload           10
        //   469: invokevirtual   org/mozilla/javascript/ast/ForInLoop.setIteratedObject:(Lorg/mozilla/javascript/ast/AstNode;)V
        //   472: aload           11
        //   474: iload_3        
        //   475: invokevirtual   org/mozilla/javascript/ast/ForInLoop.setInPosition:(I)V
        //   478: aload           11
        //   480: iload           8
        //   482: invokevirtual   org/mozilla/javascript/ast/ForInLoop.setIsForEach:(Z)V
        //   485: aload           11
        //   487: iload_1        
        //   488: invokevirtual   org/mozilla/javascript/ast/ForInLoop.setEachPosition:(I)V
        //   491: aload           11
        //   493: astore          10
        //   495: goto            534
        //   498: new             Lorg/mozilla/javascript/ast/ForLoop;
        //   501: dup            
        //   502: iload           6
        //   504: invokespecial   org/mozilla/javascript/ast/ForLoop.<init>:(I)V
        //   507: astore          12
        //   509: aload           12
        //   511: aload           14
        //   513: invokevirtual   org/mozilla/javascript/ast/ForLoop.setInitializer:(Lorg/mozilla/javascript/ast/AstNode;)V
        //   516: aload           12
        //   518: aload           10
        //   520: invokevirtual   org/mozilla/javascript/ast/ForLoop.setCondition:(Lorg/mozilla/javascript/ast/AstNode;)V
        //   523: aload           12
        //   525: aload           11
        //   527: invokevirtual   org/mozilla/javascript/ast/ForLoop.setIncrement:(Lorg/mozilla/javascript/ast/AstNode;)V
        //   530: aload           12
        //   532: astore          10
        //   534: aload_0        
        //   535: getfield        org/mozilla/javascript/Parser.currentScope:Lorg/mozilla/javascript/ast/Scope;
        //   538: aload           10
        //   540: invokevirtual   org/mozilla/javascript/ast/Scope.replaceWith:(Lorg/mozilla/javascript/ast/Scope;)V
        //   543: aload_0        
        //   544: invokevirtual   org/mozilla/javascript/Parser.popScope:()V
        //   547: aload_0        
        //   548: aload           10
        //   550: invokespecial   org/mozilla/javascript/Parser.enterLoop:(Lorg/mozilla/javascript/ast/Loop;)V
        //   553: aload_0        
        //   554: invokespecial   org/mozilla/javascript/Parser.statement:()Lorg/mozilla/javascript/ast/AstNode;
        //   557: astore          11
        //   559: aload           10
        //   561: aload_0        
        //   562: aload           11
        //   564: invokespecial   org/mozilla/javascript/Parser.getNodeEnd:(Lorg/mozilla/javascript/ast/AstNode;)I
        //   567: iload           6
        //   569: isub           
        //   570: invokevirtual   org/mozilla/javascript/ast/Loop.setLength:(I)V
        //   573: aload           10
        //   575: aload           11
        //   577: invokevirtual   org/mozilla/javascript/ast/Loop.setBody:(Lorg/mozilla/javascript/ast/AstNode;)V
        //   580: aload_0        
        //   581: invokespecial   org/mozilla/javascript/Parser.exitLoop:()V
        //   584: aload_0        
        //   585: getfield        org/mozilla/javascript/Parser.currentScope:Lorg/mozilla/javascript/ast/Scope;
        //   588: aload           13
        //   590: if_acmpne       597
        //   593: aload_0        
        //   594: invokevirtual   org/mozilla/javascript/Parser.popScope:()V
        //   597: aload           10
        //   599: iload_2        
        //   600: iload           5
        //   602: invokevirtual   org/mozilla/javascript/ast/Loop.setParens:(II)V
        //   605: aload           10
        //   607: iload           7
        //   609: invokevirtual   org/mozilla/javascript/ast/Loop.setLineno:(I)V
        //   612: aload           10
        //   614: areturn        
        //   615: astore          10
        //   617: aload_0        
        //   618: invokespecial   org/mozilla/javascript/Parser.exitLoop:()V
        //   621: aload           10
        //   623: athrow         
        //   624: astore          10
        //   626: goto            631
        //   629: astore          10
        //   631: goto            167
        //   634: astore          10
        //   636: goto            651
        //   639: astore          10
        //   641: goto            651
        //   644: astore          10
        //   646: goto            651
        //   649: astore          10
        //   651: aload_0        
        //   652: getfield        org/mozilla/javascript/Parser.currentScope:Lorg/mozilla/javascript/ast/Scope;
        //   655: aload           13
        //   657: if_acmpne       664
        //   660: aload_0        
        //   661: invokevirtual   org/mozilla/javascript/Parser.popScope:()V
        //   664: aload           10
        //   666: athrow         
        //   667: goto            458
        //   670: astore          10
        //   672: goto            651
        //   675: astore          10
        //   677: goto            636
        //    Exceptions:
        //  throws java.io.IOException
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type
        //  -----  -----  -----  -----  ----
        //  58     66     649    651    Any
        //  71     86     125    130    Any
        //  94     102    110    115    Any
        //  115    122    125    130    Any
        //  133    144    644    649    Any
        //  149    157    165    167    Any
        //  170    188    644    649    Any
        //  193    210    216    221    Any
        //  221    236    644    649    Any
        //  242    250    281    286    Any
        //  250    273    276    281    Any
        //  286    292    639    644    Any
        //  292    316    639    644    Any
        //  323    346    276    281    Any
        //  349    355    639    644    Any
        //  360    371    634    636    Any
        //  376    385    395    400    Any
        //  408    419    675    680    Any
        //  419    442    670    675    Any
        //  448    455    629    631    Any
        //  458    491    629    631    Any
        //  498    530    629    631    Any
        //  534    553    624    629    Any
        //  553    580    615    624    Any
        //  580    584    624    629    Any
        //  617    624    624    629    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0458:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2596)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at us.deathmarine.luyten.FileSaver.doSaveJarDecompiled(FileSaver.java:192)
        //     at us.deathmarine.luyten.FileSaver.access$300(FileSaver.java:45)
        //     at us.deathmarine.luyten.FileSaver$4.run(FileSaver.java:112)
        //     at java.lang.Thread.run(Unknown Source)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    private AstNode forLoopInit(final int n) throws IOException {
        while (true) {
        Label_0041_Outer:
            while (true) {
                while (true) {
                    Label_0087: {
                        try {
                            this.inForInit = true;
                            if (n == 82) {
                                final AstNode astNode = new EmptyExpression(this.ts.tokenBeg, 1);
                                astNode.setLineno(this.ts.lineno);
                                return astNode;
                            }
                            break Label_0087;
                            this.consumeToken();
                            return this.variables(n, this.ts.tokenBeg, false);
                            astNode = this.expr();
                            this.markDestructuring(astNode);
                            return astNode;
                        }
                        finally {
                            this.inForInit = false;
                        }
                    }
                    if (n != 122 && n != 153) {
                        continue;
                    }
                    break;
                }
                continue Label_0041_Outer;
            }
        }
    }
    
    private FunctionNode function(final int functionType) throws IOException {
        int n = functionType;
        final int lineno = this.ts.lineno;
        final int tokenBeg = this.ts.tokenBeg;
        Object identifier = null;
        Object nameNode = null;
        AstNode memberExprNode = null;
        final AstNode astNode = null;
        if (this.matchToken(39)) {
            nameNode = this.createNameNode(true, 39);
            if (this.inUseStrictDirective) {
                final String identifier2 = ((Name)nameNode).getIdentifier();
                if ("eval".equals(identifier2) || "arguments".equals(identifier2)) {
                    this.reportError("msg.bad.id.strict", identifier2);
                }
            }
            identifier = nameNode;
            if (!this.matchToken(87)) {
                identifier = nameNode;
                memberExprNode = astNode;
                if (this.compilerEnv.isAllowMemberExprAsFunctionName()) {
                    identifier = null;
                    memberExprNode = this.memberExprTail(false, (AstNode)nameNode);
                }
                this.mustMatchToken(87, "msg.no.paren.parms");
            }
        }
        else if (!this.matchToken(87)) {
            memberExprNode = (AstNode)nameNode;
            if (this.compilerEnv.isAllowMemberExprAsFunctionName()) {
                memberExprNode = this.memberExpr(false);
            }
            this.mustMatchToken(87, "msg.no.paren.parms");
        }
        int tokenBeg2;
        if (this.currentToken == 87) {
            tokenBeg2 = this.ts.tokenBeg;
        }
        else {
            tokenBeg2 = -1;
        }
        if (memberExprNode != null) {
            n = 2;
        }
        if (n != 2 && identifier != null && ((Name)identifier).length() > 0) {
            this.defineSymbol(109, ((Name)identifier).getIdentifier());
        }
        final FunctionNode functionNode = new FunctionNode(tokenBeg, (Name)identifier);
        functionNode.setFunctionType(functionType);
        if (tokenBeg2 != -1) {
            functionNode.setLp(tokenBeg2 - tokenBeg);
        }
    Label_0392_Outer:
        while (true) {
            functionNode.setJsDocNode(this.getAndResetJsDoc());
            final PerFunctionVariables perFunctionVariables = new PerFunctionVariables(functionNode);
        Label_0399_Outer:
            while (true) {
                while (true) {
                    Label_0493: {
                        Label_0488: {
                            try {
                                this.parseFunctionParams(functionNode);
                                functionNode.setBody(this.parseFunctionBody());
                                functionNode.setEncodedSourceBounds(tokenBeg, this.ts.tokenEnd);
                                functionNode.setLength(this.ts.tokenEnd - tokenBeg);
                                if (this.compilerEnv.isStrictMode() && !functionNode.getBody().hasConsistentReturnUsage()) {
                                    if (identifier != null && ((Name)identifier).length() > 0) {
                                        nameNode = "msg.no.return.value";
                                        break Label_0493;
                                    }
                                    break Label_0488;
                                }
                                while (true) {
                                    perFunctionVariables.restore();
                                    if (memberExprNode != null) {
                                        Kit.codeBug();
                                        functionNode.setMemberExprNode(memberExprNode);
                                    }
                                    functionNode.setSourceName(this.sourceURI);
                                    functionNode.setBaseLineno(lineno);
                                    functionNode.setEndLineno(this.ts.lineno);
                                    if (this.compilerEnv.isIdeMode()) {
                                        functionNode.setParentScope(this.currentScope);
                                    }
                                    return functionNode;
                                    identifier = ((Name)identifier).getIdentifier();
                                    this.addStrictWarning((String)nameNode, (String)identifier);
                                    continue Label_0392_Outer;
                                }
                            }
                            finally {
                                perFunctionVariables.restore();
                            }
                        }
                        nameNode = "msg.anon.no.return.value";
                    }
                    if (identifier == null) {
                        identifier = "";
                        continue;
                    }
                    break;
                }
                continue Label_0399_Outer;
            }
        }
    }
    
    private AstNode generatorExpression(final AstNode astNode, final int n) throws IOException {
        return this.generatorExpression(astNode, n, false);
    }
    
    private AstNode generatorExpression(final AstNode result, final int n, final boolean b) throws IOException {
        final ArrayList<GeneratorExpressionLoop> loops = new ArrayList<GeneratorExpressionLoop>();
        while (this.peekToken() == 119) {
            loops.add(this.generatorExpressionLoop());
        }
        int ifPosition = -1;
        ConditionData condition = null;
        if (this.peekToken() == 112) {
            this.consumeToken();
            ifPosition = this.ts.tokenBeg - n;
            condition = this.condition();
        }
        if (!b) {
            this.mustMatchToken(88, "msg.no.paren.let");
        }
        final GeneratorExpression generatorExpression = new GeneratorExpression(n, this.ts.tokenEnd - n);
        generatorExpression.setResult(result);
        generatorExpression.setLoops(loops);
        if (condition != null) {
            generatorExpression.setIfPosition(ifPosition);
            generatorExpression.setFilter(condition.condition);
            generatorExpression.setFilterLp(condition.lp - n);
            generatorExpression.setFilterRp(condition.rp - n);
        }
        return generatorExpression;
    }
    
    private GeneratorExpressionLoop generatorExpressionLoop() throws IOException {
        if (this.nextToken() != 119) {
            this.codeBug();
        }
        final int tokenBeg = this.ts.tokenBeg;
        int n = -1;
        int n2 = -1;
        int inPosition = -1;
        final GeneratorExpressionLoop generatorExpressionLoop = new GeneratorExpressionLoop(tokenBeg);
        this.pushScope(generatorExpressionLoop);
        try {
            if (this.mustMatchToken(87, "msg.no.paren.for")) {
                n = this.ts.tokenBeg - tokenBeg;
            }
            AstNode iterator = null;
            final int peekToken = this.peekToken();
            if (peekToken != 39) {
                if (peekToken != 83 && peekToken != 85) {
                    this.reportError("msg.bad.var");
                }
                else {
                    iterator = this.destructuringPrimaryExpr();
                    this.markDestructuring(iterator);
                }
            }
            else {
                this.consumeToken();
                iterator = this.createNameNode();
            }
            if (iterator.getType() == 39) {
                this.defineSymbol(153, this.ts.getString(), true);
            }
            if (this.mustMatchToken(52, "msg.in.after.for.name")) {
                inPosition = this.ts.tokenBeg - tokenBeg;
            }
            final AstNode expr = this.expr();
            if (this.mustMatchToken(88, "msg.no.paren.for.ctrl")) {
                n2 = this.ts.tokenBeg - tokenBeg;
            }
            generatorExpressionLoop.setLength(this.ts.tokenEnd - tokenBeg);
            generatorExpressionLoop.setIterator(iterator);
            generatorExpressionLoop.setIteratedObject(expr);
            generatorExpressionLoop.setInPosition(inPosition);
            generatorExpressionLoop.setParens(n, n2);
            return generatorExpressionLoop;
        }
        finally {
            this.popScope();
        }
    }
    
    private Comment getAndResetJsDoc() {
        final Comment currentJsDocComment = this.currentJsDocComment;
        this.currentJsDocComment = null;
        return currentJsDocComment;
    }
    
    private String getDirective(AstNode expression) {
        if (expression instanceof ExpressionStatement) {
            expression = ((ExpressionStatement)expression).getExpression();
            if (expression instanceof StringLiteral) {
                return ((StringLiteral)expression).getValue();
            }
        }
        return null;
    }
    
    private int getNodeEnd(final AstNode astNode) {
        return astNode.getPosition() + astNode.getLength();
    }
    
    private int getNumberOfEols(final String s) {
        int n = 0;
        int n2;
        for (int i = s.length() - 1; i >= 0; --i, n = n2) {
            n2 = n;
            if (s.charAt(i) == '\n') {
                n2 = n + 1;
            }
        }
        return n;
    }
    
    private IfStatement ifStatement() throws IOException {
        if (this.currentToken != 112) {
            this.codeBug();
        }
        this.consumeToken();
        final int tokenBeg = this.ts.tokenBeg;
        final int lineno = this.ts.lineno;
        int elsePosition = -1;
        final ConditionData condition = this.condition();
        final AstNode statement = this.statement();
        AstNode statement2 = null;
        if (this.matchToken(113)) {
            elsePosition = this.ts.tokenBeg - tokenBeg;
            statement2 = this.statement();
        }
        AstNode astNode;
        if (statement2 != null) {
            astNode = statement2;
        }
        else {
            astNode = statement;
        }
        final IfStatement ifStatement = new IfStatement(tokenBeg, this.getNodeEnd(astNode) - tokenBeg);
        ifStatement.setCondition(condition.condition);
        ifStatement.setParens(condition.lp - tokenBeg, condition.rp - tokenBeg);
        ifStatement.setThenPart(statement);
        ifStatement.setElsePart(statement2);
        ifStatement.setElsePosition(elsePosition);
        ifStatement.setLineno(lineno);
        return ifStatement;
    }
    
    private AstNode let(final boolean b, final int n) throws IOException {
        final LetNode letNode = new LetNode(n);
        letNode.setLineno(this.ts.lineno);
        if (this.mustMatchToken(87, "msg.no.paren.after.let")) {
            letNode.setLp(this.ts.tokenBeg - n);
        }
        this.pushScope(letNode);
        try {
            letNode.setVariables(this.variables(153, this.ts.tokenBeg, b));
            if (this.mustMatchToken(88, "msg.no.paren.let")) {
                letNode.setRp(this.ts.tokenBeg - n);
            }
            if (b && this.peekToken() == 85) {
                this.consumeToken();
                final int tokenBeg = this.ts.tokenBeg;
                final AstNode statements = this.statements();
                this.mustMatchToken(86, "msg.no.curly.let");
                statements.setLength(this.ts.tokenEnd - tokenBeg);
                letNode.setLength(this.ts.tokenEnd - n);
                letNode.setBody(statements);
                letNode.setType(153);
            }
            else {
                final AstNode expr = this.expr();
                letNode.setLength(this.getNodeEnd(expr) - n);
                letNode.setBody(expr);
                if (b) {
                    final ExpressionStatement expressionStatement = new ExpressionStatement(letNode, this.insideFunction() ^ true);
                    expressionStatement.setLineno(letNode.getLineno());
                    return expressionStatement;
                }
            }
            return letNode;
        }
        finally {
            this.popScope();
        }
    }
    
    private AstNode letStatement() throws IOException {
        if (this.currentToken != 153) {
            this.codeBug();
        }
        this.consumeToken();
        final int lineno = this.ts.lineno;
        final int tokenBeg = this.ts.tokenBeg;
        AstNode astNode;
        if (this.peekToken() == 87) {
            astNode = this.let(true, tokenBeg);
        }
        else {
            astNode = this.variables(153, tokenBeg, true);
        }
        astNode.setLineno(lineno);
        return astNode;
    }
    
    private int lineBeginningFor(final int n) {
        if (this.sourceChars == null) {
            return -1;
        }
        if (n <= 0) {
            return 0;
        }
        final char[] sourceChars = this.sourceChars;
        int n2;
        if ((n2 = n) >= sourceChars.length) {
            n2 = sourceChars.length - 1;
        }
        while (true) {
            --n2;
            if (n2 < 0) {
                return 0;
            }
            if (ScriptRuntime.isJSLineTerminator(sourceChars[n2])) {
                return n2 + 1;
            }
        }
    }
    
    private ErrorNode makeErrorNode() {
        final ErrorNode errorNode = new ErrorNode(this.ts.tokenBeg, this.ts.tokenEnd - this.ts.tokenBeg);
        errorNode.setLineno(this.ts.lineno);
        return errorNode;
    }
    
    private LabeledStatement matchJumpLabelName() throws IOException {
        LabeledStatement labeledStatement = null;
        LabeledStatement labeledStatement2 = null;
        if (this.peekTokenOrEOL() == 39) {
            this.consumeToken();
            if (this.labelSet != null) {
                labeledStatement2 = this.labelSet.get(this.ts.getString());
            }
            if ((labeledStatement = labeledStatement2) == null) {
                this.reportError("msg.undef.label");
                labeledStatement = labeledStatement2;
            }
        }
        return labeledStatement;
    }
    
    private boolean matchToken(final int n) throws IOException {
        if (this.peekToken() != n) {
            return false;
        }
        this.consumeToken();
        return true;
    }
    
    private AstNode memberExpr(final boolean b) throws IOException {
        final int peekToken = this.peekToken();
        final int lineno = this.ts.lineno;
        AstNode primaryExpr;
        if (peekToken != 30) {
            primaryExpr = this.primaryExpr();
        }
        else {
            this.consumeToken();
            final int tokenBeg = this.ts.tokenBeg;
            primaryExpr = new NewExpression(tokenBeg);
            final AstNode memberExpr = this.memberExpr(false);
            int n = this.getNodeEnd(memberExpr);
            ((FunctionCall)primaryExpr).setTarget(memberExpr);
            if (this.matchToken(87)) {
                final int tokenBeg2 = this.ts.tokenBeg;
                final List<AstNode> argumentList = this.argumentList();
                if (argumentList != null && argumentList.size() > 65536) {
                    this.reportError("msg.too.many.constructor.args");
                }
                final int tokenBeg3 = this.ts.tokenBeg;
                n = this.ts.tokenEnd;
                if (argumentList != null) {
                    ((FunctionCall)primaryExpr).setArguments(argumentList);
                }
                ((FunctionCall)primaryExpr).setParens(tokenBeg2 - tokenBeg, tokenBeg3 - tokenBeg);
            }
            if (this.matchToken(85)) {
                final ObjectLiteral objectLiteral = this.objectLiteral();
                n = this.getNodeEnd(objectLiteral);
                ((NewExpression)primaryExpr).setInitializer(objectLiteral);
            }
            primaryExpr.setLength(n - tokenBeg);
        }
        primaryExpr.setLineno(lineno);
        return this.memberExprTail(b, primaryExpr);
    }
    
    private AstNode memberExprTail(final boolean b, final AstNode astNode) throws IOException {
        if (astNode == null) {
            this.codeBug();
        }
        final int position = astNode.getPosition();
        AstNode propertyAccess = astNode;
        while (true) {
            final int peekToken = this.peekToken();
            if (peekToken != 83) {
                if (peekToken != 87) {
                    if (peekToken != 108 && peekToken != 143) {
                        if (peekToken != 146) {
                            return propertyAccess;
                        }
                        this.consumeToken();
                        final int tokenBeg = this.ts.tokenBeg;
                        final int lineno = this.ts.lineno;
                        this.mustHaveXML();
                        this.setRequiresActivation();
                        final AstNode expr = this.expr();
                        int n = this.getNodeEnd(expr);
                        int tokenBeg2 = -1;
                        if (this.mustMatchToken(88, "msg.no.paren")) {
                            tokenBeg2 = this.ts.tokenBeg;
                            n = this.ts.tokenEnd;
                        }
                        final XmlDotQuery xmlDotQuery = new XmlDotQuery(position, n - position);
                        xmlDotQuery.setLeft(propertyAccess);
                        xmlDotQuery.setRight(expr);
                        xmlDotQuery.setOperatorPosition(tokenBeg);
                        xmlDotQuery.setRp(tokenBeg2 - position);
                        xmlDotQuery.setLineno(lineno);
                        propertyAccess = xmlDotQuery;
                    }
                    else {
                        final int lineno2 = this.ts.lineno;
                        propertyAccess = this.propertyAccess(peekToken, propertyAccess);
                        propertyAccess.setLineno(lineno2);
                    }
                }
                else {
                    if (!b) {
                        return propertyAccess;
                    }
                    final int lineno3 = this.ts.lineno;
                    this.consumeToken();
                    this.checkCallRequiresActivation(propertyAccess);
                    final FunctionCall functionCall = new FunctionCall(position);
                    functionCall.setTarget(propertyAccess);
                    functionCall.setLineno(lineno3);
                    functionCall.setLp(this.ts.tokenBeg - position);
                    final List<AstNode> argumentList = this.argumentList();
                    if (argumentList != null && argumentList.size() > 65536) {
                        this.reportError("msg.too.many.function.args");
                    }
                    functionCall.setArguments(argumentList);
                    functionCall.setRp(this.ts.tokenBeg - position);
                    functionCall.setLength(this.ts.tokenEnd - position);
                    propertyAccess = functionCall;
                }
            }
            else {
                this.consumeToken();
                final int tokenBeg3 = this.ts.tokenBeg;
                int tokenBeg4 = -1;
                final int lineno4 = this.ts.lineno;
                final AstNode expr2 = this.expr();
                int n2 = this.getNodeEnd(expr2);
                if (this.mustMatchToken(84, "msg.no.bracket.index")) {
                    tokenBeg4 = this.ts.tokenBeg;
                    n2 = this.ts.tokenEnd;
                }
                final ElementGet elementGet = new ElementGet(position, n2 - position);
                elementGet.setTarget(propertyAccess);
                elementGet.setElement(expr2);
                elementGet.setParens(tokenBeg3, tokenBeg4);
                elementGet.setLineno(lineno4);
                propertyAccess = elementGet;
            }
        }
    }
    
    private ObjectProperty methodDefinition(final int n, final AstNode left, int nodeEnd) throws IOException {
        final FunctionNode function = this.function(2);
        final Name functionName = function.getFunctionName();
        if (functionName != null && functionName.length() != 0) {
            this.reportError("msg.bad.prop");
        }
        final ObjectProperty objectProperty = new ObjectProperty(n);
        if (nodeEnd != 2) {
            if (nodeEnd != 4) {
                if (nodeEnd == 8) {
                    objectProperty.setIsNormalMethod();
                    function.setFunctionIsNormalMethod();
                }
            }
            else {
                objectProperty.setIsSetterMethod();
                function.setFunctionIsSetterMethod();
            }
        }
        else {
            objectProperty.setIsGetterMethod();
            function.setFunctionIsGetterMethod();
        }
        nodeEnd = this.getNodeEnd(function);
        objectProperty.setLeft(left);
        objectProperty.setRight(function);
        objectProperty.setLength(nodeEnd - n);
        return objectProperty;
    }
    
    private AstNode mulExpr() throws IOException {
        AstNode unaryExpr = this.unaryExpr();
        while (true) {
            final int peekToken = this.peekToken();
            final int tokenBeg = this.ts.tokenBeg;
            switch (peekToken) {
                default: {
                    return unaryExpr;
                }
                case 23:
                case 24:
                case 25: {
                    this.consumeToken();
                    unaryExpr = new InfixExpression(peekToken, unaryExpr, this.unaryExpr(), tokenBeg);
                    continue;
                }
            }
        }
    }
    
    private void mustHaveXML() {
        if (!this.compilerEnv.isXmlAvailable()) {
            this.reportError("msg.XML.not.available");
        }
    }
    
    private boolean mustMatchToken(final int n, final String s) throws IOException {
        return this.mustMatchToken(n, s, this.ts.tokenBeg, this.ts.tokenEnd - this.ts.tokenBeg);
    }
    
    private boolean mustMatchToken(final int n, final String s, final int n2, final int n3) throws IOException {
        if (this.matchToken(n)) {
            return true;
        }
        this.reportError(s, n2, n3);
        return false;
    }
    
    private AstNode name(final int n, int tokenBeg) throws IOException {
        final String string = this.ts.getString();
        tokenBeg = this.ts.tokenBeg;
        final int lineno = this.ts.lineno;
        if ((0x20000 & n) != 0x0 && this.peekToken() == 103) {
            final Label label = new Label(tokenBeg, this.ts.tokenEnd - tokenBeg);
            label.setName(string);
            label.setLineno(this.ts.lineno);
            return label;
        }
        this.saveNameTokenData(tokenBeg, string, lineno);
        if (this.compilerEnv.isXmlAvailable()) {
            return this.propertyName(-1, string, 0);
        }
        return this.createNameNode(true, 39);
    }
    
    private AstNode nameOrLabel() throws IOException {
        if (this.currentToken != 39) {
            throw this.codeBug();
        }
        final int tokenBeg = this.ts.tokenBeg;
        this.currentFlaggedToken |= 0x20000;
        final AstNode expr = this.expr();
        if (expr.getType() != 130) {
            final ExpressionStatement expressionStatement = new ExpressionStatement(expr, this.insideFunction() ^ true);
            expressionStatement.lineno = expr.lineno;
            return expressionStatement;
        }
        final LabeledStatement currentLabel = new LabeledStatement(tokenBeg);
        this.recordLabel((Label)expr, currentLabel);
        currentLabel.setLineno(this.ts.lineno);
        final ExpressionStatement expressionStatement2 = null;
        ExpressionStatement expressionStatement3;
        while (true) {
            expressionStatement3 = expressionStatement2;
            if (this.peekToken() != 39) {
                break;
            }
            this.currentFlaggedToken |= 0x20000;
            final AstNode expr2 = this.expr();
            if (expr2.getType() != 130) {
                expressionStatement3 = new ExpressionStatement(expr2, this.insideFunction() ^ true);
                this.autoInsertSemicolon(expressionStatement3);
                break;
            }
            this.recordLabel((Label)expr2, currentLabel);
        }
        try {
            this.currentLabel = currentLabel;
            AstNode statementHelper;
            if ((statementHelper = expressionStatement3) == null) {
                statementHelper = this.statementHelper();
            }
            this.currentLabel = null;
            final Iterator<Label> iterator = currentLabel.getLabels().iterator();
            while (iterator.hasNext()) {
                this.labelSet.remove(iterator.next().getName());
            }
            int nodeEnd;
            if (statementHelper.getParent() == null) {
                nodeEnd = this.getNodeEnd(statementHelper) - tokenBeg;
            }
            else {
                nodeEnd = this.getNodeEnd(statementHelper);
            }
            currentLabel.setLength(nodeEnd);
            currentLabel.setStatement(statementHelper);
            return currentLabel;
        }
        finally {
            this.currentLabel = null;
            final Iterator<Label> iterator2 = currentLabel.getLabels().iterator();
            while (iterator2.hasNext()) {
                this.labelSet.remove(iterator2.next().getName());
            }
        }
    }
    
    private int nextFlaggedToken() throws IOException {
        this.peekToken();
        final int currentFlaggedToken = this.currentFlaggedToken;
        this.consumeToken();
        return currentFlaggedToken;
    }
    
    private int nextToken() throws IOException {
        final int peekToken = this.peekToken();
        this.consumeToken();
        return peekToken;
    }
    
    private int nodeEnd(final AstNode astNode) {
        return astNode.getPosition() + astNode.getLength();
    }
    
    private static final boolean nowAllSet(final int n, final int n2, final int n3) {
        return (n & n3) != n3 && (n2 & n3) == n3;
    }
    
    private ObjectLiteral objectLiteral() throws IOException {
        final int tokenBeg = this.ts.tokenBeg;
        final int lineno = this.ts.lineno;
        int tokenEnd = -1;
        final ArrayList<ObjectProperty> elements = new ArrayList<ObjectProperty>();
        Set<String> set = null;
        Set<String> set2 = null;
        if (this.inUseStrictDirective) {
            set = new HashSet<String>();
            set2 = new HashSet<String>();
        }
        final Comment andResetJsDoc = this.getAndResetJsDoc();
        while (true) {
            final int n = 1;
            final int n2 = 1;
            final int peekToken = this.peekToken();
            final Comment andResetJsDoc2 = this.getAndResetJsDoc();
            if (peekToken == 86) {
                if (tokenEnd != -1) {
                    this.warnTrailingComma(tokenBeg, elements, tokenEnd);
                }
                break;
            }
            AstNode astNode = this.objliteralProperty();
            String s;
            int n3;
            if (astNode == null) {
                s = null;
                this.reportError("msg.bad.prop");
                n3 = n;
            }
            else {
                final String string = this.ts.getString();
                final int tokenBeg2 = this.ts.tokenBeg;
                this.consumeToken();
                final int peekToken2 = this.peekToken();
                if (peekToken2 != 89 && peekToken2 != 103 && peekToken2 != 86) {
                    if (peekToken2 == 87) {
                        n3 = 8;
                    }
                    else {
                        n3 = n2;
                        if (astNode.getType() == 39) {
                            if ("get".equals(string)) {
                                n3 = 2;
                            }
                            else {
                                n3 = n2;
                                if ("set".equals(string)) {
                                    n3 = 4;
                                }
                            }
                        }
                    }
                    if (n3 == 2 || n3 == 4) {
                        astNode = this.objliteralProperty();
                        if (astNode == null) {
                            this.reportError("msg.bad.prop");
                        }
                        this.consumeToken();
                    }
                    if (astNode == null) {
                        s = null;
                    }
                    else {
                        final String string2 = this.ts.getString();
                        final ObjectProperty methodDefinition = this.methodDefinition(tokenBeg2, astNode, n3);
                        astNode.setJsDocNode(andResetJsDoc2);
                        elements.add(methodDefinition);
                        s = string2;
                    }
                }
                else {
                    astNode.setJsDocNode(andResetJsDoc2);
                    elements.add(this.plainProperty(astNode, peekToken));
                    n3 = n;
                    s = string;
                }
            }
            Label_0520: {
                if (this.inUseStrictDirective && s != null) {
                    if (n3 != 4) {
                        if (n3 != 8) {
                            switch (n3) {
                                default: {
                                    break Label_0520;
                                }
                                case 2: {
                                    if (set.contains(s)) {
                                        this.addError("msg.dup.obj.lit.prop.strict", s);
                                    }
                                    set.add(s);
                                    break Label_0520;
                                }
                                case 1: {
                                    break;
                                }
                            }
                        }
                        if (set.contains(s) || set2.contains(s)) {
                            this.addError("msg.dup.obj.lit.prop.strict", s);
                        }
                        set.add(s);
                        set2.add(s);
                    }
                    else {
                        if (set2.contains(s)) {
                            this.addError("msg.dup.obj.lit.prop.strict", s);
                        }
                        set2.add(s);
                    }
                }
            }
            this.getAndResetJsDoc();
            if (!this.matchToken(89)) {
                break;
            }
            tokenEnd = this.ts.tokenEnd;
        }
        this.mustMatchToken(86, "msg.no.brace.prop");
        final ObjectLiteral objectLiteral = new ObjectLiteral(tokenBeg, this.ts.tokenEnd - tokenBeg);
        if (andResetJsDoc != null) {
            objectLiteral.setJsDocNode(andResetJsDoc);
        }
        objectLiteral.setElements(elements);
        objectLiteral.setLineno(lineno);
        return objectLiteral;
    }
    
    private AstNode objliteralProperty() throws IOException {
        switch (this.peekToken()) {
            default: {
                if (this.compilerEnv.isReservedKeywordAsIdentifier() && TokenStream.isKeyword(this.ts.getString())) {
                    return this.createNameNode();
                }
                return null;
            }
            case 41: {
                return this.createStringLiteral();
            }
            case 40: {
                return new NumberLiteral(this.ts.tokenBeg, this.ts.getString(), this.ts.getNumber());
            }
            case 39: {
                return this.createNameNode();
            }
        }
    }
    
    private AstNode orExpr() throws IOException {
        AstNode andExpr = this.andExpr();
        if (this.matchToken(104)) {
            andExpr = new InfixExpression(104, andExpr, this.orExpr(), this.ts.tokenBeg);
        }
        return andExpr;
    }
    
    private AstNode parenExpr() throws IOException {
        final boolean inForInit = this.inForInit;
        this.inForInit = false;
        try {
            final Comment andResetJsDoc = this.getAndResetJsDoc();
            final int lineno = this.ts.lineno;
            final int tokenBeg = this.ts.tokenBeg;
            final AstNode expr = this.expr();
            AstNode generatorExpression;
            if (this.peekToken() == 119) {
                generatorExpression = this.generatorExpression(expr, tokenBeg);
            }
            else {
                final ParenthesizedExpression parenthesizedExpression = new ParenthesizedExpression(expr);
                Comment andResetJsDoc2;
                if ((andResetJsDoc2 = andResetJsDoc) == null) {
                    andResetJsDoc2 = this.getAndResetJsDoc();
                }
                if (andResetJsDoc2 != null) {
                    parenthesizedExpression.setJsDocNode(andResetJsDoc2);
                }
                this.mustMatchToken(88, "msg.no.paren");
                parenthesizedExpression.setLength(this.ts.tokenEnd - parenthesizedExpression.getPosition());
                parenthesizedExpression.setLineno(lineno);
                generatorExpression = parenthesizedExpression;
            }
            return generatorExpression;
        }
        finally {
            this.inForInit = inForInit;
        }
    }
    
    private AstRoot parse() throws IOException {
        final AstRoot parent = new AstRoot(0);
        this.currentScriptOrFn = parent;
        this.currentScope = parent;
        final int lineno = this.ts.lineno;
        int n = 1;
        final boolean inUseStrictDirective = this.inUseStrictDirective;
        this.inUseStrictDirective = false;
        int nodeEnd = 0;
    Label_0099_Outer:
        while (true) {
            int n2 = n;
            int n3 = nodeEnd;
        Label_0325:
            while (true) {
                Label_0510: {
                    while (true) {
                        try {
                            try {
                                final int peekToken = this.peekToken();
                                if (peekToken > 0) {
                                    AstNode astNode = null;
                                    int n5 = 0;
                                    Label_0230: {
                                        if (peekToken == 109) {
                                            n2 = n;
                                            n3 = nodeEnd;
                                            this.consumeToken();
                                            n2 = n;
                                            n3 = nodeEnd;
                                            try {
                                                if (this.calledByCompileFunction) {
                                                    final int n4 = 2;
                                                    n2 = n;
                                                    n3 = nodeEnd;
                                                    astNode = this.function(n4);
                                                    n5 = n;
                                                    break Label_0230;
                                                }
                                                break Label_0510;
                                            }
                                            catch (ParserException ex) {}
                                            break Label_0120;
                                        }
                                        n2 = n;
                                        n3 = nodeEnd;
                                        final AstNode astNode2 = astNode = this.statement();
                                        if ((n5 = n) != 0) {
                                            n2 = n;
                                            n3 = nodeEnd;
                                            final String directive = this.getDirective(astNode2);
                                            if (directive == null) {
                                                n5 = 0;
                                                astNode = astNode2;
                                            }
                                            else {
                                                astNode = astNode2;
                                                n5 = n;
                                                n2 = n;
                                                n3 = nodeEnd;
                                                if (directive.equals("use strict")) {
                                                    n2 = n;
                                                    n3 = nodeEnd;
                                                    this.inUseStrictDirective = true;
                                                    n2 = n;
                                                    n3 = nodeEnd;
                                                    parent.setInStrictMode(true);
                                                    n5 = n;
                                                    astNode = astNode2;
                                                }
                                            }
                                        }
                                    }
                                    n2 = n5;
                                    n3 = nodeEnd;
                                    nodeEnd = this.getNodeEnd(astNode);
                                    n2 = n5;
                                    n3 = nodeEnd;
                                    parent.addChildToBack(astNode);
                                    n2 = n5;
                                    n3 = nodeEnd;
                                    astNode.setParent(parent);
                                    n = n5;
                                    continue Label_0099_Outer;
                                }
                                this.inUseStrictDirective = inUseStrictDirective;
                            }
                            finally {}
                        }
                        catch (StackOverflowError stackOverflowError) {
                            final String lookupMessage = this.lookupMessage("msg.too.deep.parser.recursion");
                            n = n2;
                            nodeEnd = n3;
                            if (!this.compilerEnv.isIdeMode()) {
                                throw Context.reportRuntimeError(lookupMessage, this.sourceURI, this.ts.lineno, null, 0);
                            }
                            continue;
                        }
                        break;
                    }
                    break Label_0325;
                }
                final int n4 = 1;
                continue;
            }
            if (this.syntaxErrorCount != 0) {
                final String lookupMessage2 = this.lookupMessage("msg.got.syntax.errors", String.valueOf(this.syntaxErrorCount));
                if (!this.compilerEnv.isIdeMode()) {
                    throw this.errorReporter.runtimeError(lookupMessage2, this.sourceURI, lineno, null, 0);
                }
            }
            int n6 = nodeEnd;
            if (this.scannedComments != null) {
                final int max = Math.max(nodeEnd, this.getNodeEnd(this.scannedComments.get(this.scannedComments.size() - 1)));
                final Iterator<Comment> iterator = this.scannedComments.iterator();
                while (true) {
                    n6 = max;
                    if (!iterator.hasNext()) {
                        break;
                    }
                    parent.addComment(iterator.next());
                }
            }
            parent.setLength(n6 - 0);
            parent.setSourceName(this.sourceURI);
            parent.setBaseLineno(lineno);
            parent.setEndLineno(this.ts.lineno);
            return parent;
            this.inUseStrictDirective = inUseStrictDirective;
            throw;
        }
    }
    
    private AstNode parseFunctionBody() throws IOException {
        boolean b2;
        final boolean b = b2 = false;
        if (!this.matchToken(85)) {
            if (this.compilerEnv.getLanguageVersion() < 180) {
                this.reportError("msg.no.brace.body");
                b2 = b;
            }
            else {
                b2 = true;
            }
        }
        ++this.nestingOfFunction;
        final int tokenBeg = this.ts.tokenBeg;
        final Block block = new Block(tokenBeg);
        int n = 1;
        final boolean inUseStrictDirective = this.inUseStrictDirective;
        block.setLineno(this.ts.lineno);
        while (true) {
            Label_0383: {
                if (!b2) {
                    break Label_0383;
                }
            Label_0188_Outer:
                while (true) {
                    while (true) {
                    Label_0270_Outer:
                        while (true) {
                            try {
                                final ReturnStatement returnStatement = new ReturnStatement(this.ts.lineno);
                                returnStatement.setReturnValue(this.assignExpr());
                                returnStatement.putProp(25, Boolean.TRUE);
                                block.putProp(25, Boolean.TRUE);
                                block.addStatement(returnStatement);
                                --this.nestingOfFunction;
                                this.inUseStrictDirective = inUseStrictDirective;
                                final int tokenEnd = this.ts.tokenEnd;
                                this.getAndResetJsDoc();
                                int tokenEnd2 = tokenEnd;
                                if (!b2) {
                                    tokenEnd2 = tokenEnd;
                                    if (this.mustMatchToken(86, "msg.no.brace.after.body")) {
                                        tokenEnd2 = this.ts.tokenEnd;
                                    }
                                }
                                block.setLength(tokenEnd2 - tokenBeg);
                                return block;
                                Label_0257: {
                                    this.consumeToken();
                                }
                                AstNode function = this.function(1);
                                int n2 = n;
                                // iftrue(Label_0226:, directive != null)
                                // iftrue(Label_0282:, peekToken == 86)
                                // iftrue(Label_0257:, peekToken == 109)
                                // iftrue(Label_0270:, n == 0)
                                // iftrue(Label_0270:, !directive.equals((Object)"use strict"))
                                Block_6: {
                                Block_9_Outer:
                                    while (true) {
                                        break Label_0270;
                                        AstNode statement = null;
                                        Block_8: {
                                        Block_7_Outer:
                                            while (true) {
                                                this.inUseStrictDirective = true;
                                                n2 = n;
                                                function = statement;
                                                break Label_0270;
                                                while (true) {
                                                    final String directive = this.getDirective(statement);
                                                    break Block_8;
                                                    block.addStatement(function);
                                                    n = n2;
                                                    final int peekToken = this.peekToken();
                                                    Block_5: {
                                                        break Block_5;
                                                        Label_0282:
                                                        continue Label_0270_Outer;
                                                    }
                                                    break Block_6;
                                                    statement = this.statement();
                                                    n2 = n;
                                                    function = statement;
                                                    continue Label_0188_Outer;
                                                }
                                                Label_0226:
                                                n2 = n;
                                                function = statement;
                                                continue Block_7_Outer;
                                            }
                                        }
                                        n2 = 0;
                                        function = statement;
                                        continue Block_9_Outer;
                                    }
                                }
                            }
                            // switch([Lcom.strobel.decompiler.ast.Label;@426de6ea, peekToken)
                            catch (ParserException ex) {
                                continue Label_0188_Outer;
                            }
                            break;
                        }
                        break Label_0383;
                        Label_0386: {
                            continue;
                        }
                    }
                }
            }
            continue;
        }
    }
    
    private void parseFunctionParams(final FunctionNode functionNode) throws IOException {
        if (this.matchToken(88)) {
            functionNode.setRp(this.ts.tokenBeg - functionNode.getPosition());
            return;
        }
        Map<String, AstNode> map = null;
        final HashSet<String> set = new HashSet<String>();
        Map<String, AstNode> map2;
        do {
            final int peekToken = this.peekToken();
            if (peekToken != 83 && peekToken != 85) {
                if (this.mustMatchToken(39, "msg.no.parm")) {
                    functionNode.addParam(this.createNameNode());
                    final String string = this.ts.getString();
                    this.defineSymbol(87, string);
                    if (this.inUseStrictDirective) {
                        if ("eval".equals(string) || "arguments".equals(string)) {
                            this.reportError("msg.bad.id.strict", string);
                        }
                        if (set.contains(string)) {
                            this.addError("msg.dup.param.strict", string);
                        }
                        set.add(string);
                    }
                    map2 = map;
                }
                else {
                    functionNode.addParam(this.makeErrorNode());
                    map2 = map;
                }
            }
            else {
                final AstNode destructuringPrimaryExpr = this.destructuringPrimaryExpr();
                this.markDestructuring(destructuringPrimaryExpr);
                functionNode.addParam(destructuringPrimaryExpr);
                if ((map2 = map) == null) {
                    map2 = new HashMap<String, AstNode>();
                }
                final String nextTempName = this.currentScriptOrFn.getNextTempName();
                this.defineSymbol(87, nextTempName, false);
                map2.put(nextTempName, destructuringPrimaryExpr);
            }
            map = map2;
        } while (this.matchToken(89));
        if (map2 != null) {
            final Node node = new Node(89);
            for (final Map.Entry<String, AstNode> entry : map2.entrySet()) {
                node.addChildToBack(this.createDestructuringAssignment(122, entry.getValue(), this.createName(entry.getKey())));
            }
            functionNode.putProp(23, node);
        }
        if (this.mustMatchToken(88, "msg.no.paren.after.parms")) {
            functionNode.setRp(this.ts.tokenBeg - functionNode.getPosition());
        }
    }
    
    private int peekFlaggedToken() throws IOException {
        this.peekToken();
        return this.currentFlaggedToken;
    }
    
    private int peekToken() throws IOException {
        if (this.currentFlaggedToken != 0) {
            return this.currentToken;
        }
        int lineno = this.ts.getLineno();
        int token = this.ts.getToken();
        final boolean b = false;
        int n = 0;
        while (token == 1 || token == 161) {
            int n2;
            int n3;
            if (token == 1) {
                n2 = lineno + 1;
                n3 = 1;
            }
            else {
                n3 = n;
                n2 = lineno;
                if (this.compilerEnv.isRecordingComments()) {
                    final String andResetCurrentComment = this.ts.getAndResetCurrentComment();
                    this.recordComment(lineno, andResetCurrentComment);
                    n2 = lineno + this.getNumberOfEols(andResetCurrentComment);
                    n3 = n;
                }
            }
            final int token2 = this.ts.getToken();
            n = n3;
            token = token2;
            lineno = n2;
        }
        this.currentToken = token;
        int n4 = b ? 1 : 0;
        if (n != 0) {
            n4 = 65536;
        }
        this.currentFlaggedToken = (n4 | token);
        return this.currentToken;
    }
    
    private int peekTokenOrEOL() throws IOException {
        int peekToken = this.peekToken();
        if ((this.currentFlaggedToken & 0x10000) != 0x0) {
            peekToken = 1;
        }
        return peekToken;
    }
    
    private ObjectProperty plainProperty(final AstNode astNode, final int n) throws IOException {
        final int peekToken = this.peekToken();
        if ((peekToken == 89 || peekToken == 86) && n == 39 && this.compilerEnv.getLanguageVersion() >= 180) {
            if (!this.inDestructuringAssignment) {
                this.reportError("msg.bad.object.init");
            }
            final Name name = new Name(astNode.getPosition(), astNode.getString());
            final ObjectProperty objectProperty = new ObjectProperty();
            objectProperty.putProp(26, Boolean.TRUE);
            objectProperty.setLeftAndRight(astNode, name);
            return objectProperty;
        }
        this.mustMatchToken(103, "msg.no.colon.prop");
        final ObjectProperty objectProperty2 = new ObjectProperty();
        objectProperty2.setOperatorPosition(this.ts.tokenBeg);
        objectProperty2.setLeftAndRight(astNode, this.assignExpr());
        return objectProperty2;
    }
    
    private AstNode primaryExpr() throws IOException {
        final int nextFlaggedToken = this.nextFlaggedToken();
        final int n = 0xFFFF & nextFlaggedToken;
        switch (n) {
            default: {
                switch (n) {
                    default: {
                        switch (n) {
                            default: {
                                this.reportError("msg.syntax");
                                return this.makeErrorNode();
                            }
                            case 153: {
                                return this.let(false, this.ts.tokenBeg);
                            }
                            case 147: {
                                this.mustHaveXML();
                                return this.attributeAccess();
                            }
                            case 127: {
                                this.reportError("msg.reserved.id");
                                return this.makeErrorNode();
                            }
                            case 109: {
                                return this.function(2);
                            }
                            case 87: {
                                return this.parenExpr();
                            }
                            case 85: {
                                return this.objectLiteral();
                            }
                            case 83: {
                                return this.arrayLiteral();
                            }
                            case 24:
                            case 100: {
                                this.ts.readRegExp(n);
                                final int tokenBeg = this.ts.tokenBeg;
                                final RegExpLiteral regExpLiteral = new RegExpLiteral(tokenBeg, this.ts.tokenEnd - tokenBeg);
                                regExpLiteral.setValue(this.ts.getString());
                                regExpLiteral.setFlags(this.ts.readAndClearRegExpFlags());
                                return regExpLiteral;
                            }
                        }
                        break;
                    }
                    case 42:
                    case 43:
                    case 44:
                    case 45: {
                        final int tokenBeg2 = this.ts.tokenBeg;
                        return new KeywordLiteral(tokenBeg2, this.ts.tokenEnd - tokenBeg2, n);
                    }
                    case 41: {
                        return this.createStringLiteral();
                    }
                    case 40: {
                        final String string = this.ts.getString();
                        if (this.inUseStrictDirective && this.ts.isNumberOctal()) {
                            this.reportError("msg.no.octal.strict");
                        }
                        String string2 = string;
                        if (this.ts.isNumberOctal()) {
                            final StringBuilder sb = new StringBuilder();
                            sb.append("0");
                            sb.append(string);
                            string2 = sb.toString();
                        }
                        String string3 = string2;
                        if (this.ts.isNumberHex()) {
                            final StringBuilder sb2 = new StringBuilder();
                            sb2.append("0x");
                            sb2.append(string2);
                            string3 = sb2.toString();
                        }
                        return new NumberLiteral(this.ts.tokenBeg, string3, this.ts.getNumber());
                    }
                    case 39: {
                        return this.name(nextFlaggedToken, n);
                    }
                }
                break;
            }
            case 0: {
                this.reportError("msg.unexpected.eof");
            }
            case -1: {
                return this.makeErrorNode();
            }
        }
    }
    
    private AstNode propertyAccess(int position, final AstNode left) throws IOException {
        if (left == null) {
            this.codeBug();
        }
        int n = 0;
        final int lineno = this.ts.lineno;
        final int tokenBeg = this.ts.tokenBeg;
        this.consumeToken();
        if (position == 143) {
            this.mustHaveXML();
            n = 4;
        }
        if (!this.compilerEnv.isXmlAvailable()) {
            if (this.nextToken() != 39 && (!this.compilerEnv.isReservedKeywordAsIdentifier() || !TokenStream.isKeyword(this.ts.getString()))) {
                this.reportError("msg.no.name.after.dot");
            }
            final PropertyGet propertyGet = new PropertyGet(left, this.createNameNode(true, 33), tokenBeg);
            propertyGet.setLineno(lineno);
            return propertyGet;
        }
        final int nextToken = this.nextToken();
        AstNode right = null;
        Label_0317: {
            if (nextToken != 23) {
                if (nextToken != 39) {
                    if (nextToken != 50) {
                        if (nextToken != 147) {
                            if (this.compilerEnv.isReservedKeywordAsIdentifier()) {
                                final String keywordToName = Token.keywordToName(nextToken);
                                if (keywordToName != null) {
                                    this.saveNameTokenData(this.ts.tokenBeg, keywordToName, this.ts.lineno);
                                    right = this.propertyName(-1, keywordToName, n);
                                    break Label_0317;
                                }
                            }
                            this.reportError("msg.no.name.after.dot");
                            return this.makeErrorNode();
                        }
                        right = this.attributeAccess();
                    }
                    else {
                        this.saveNameTokenData(this.ts.tokenBeg, "throw", this.ts.lineno);
                        right = this.propertyName(-1, "throw", n);
                    }
                }
                else {
                    right = this.propertyName(-1, this.ts.getString(), n);
                }
            }
            else {
                this.saveNameTokenData(this.ts.tokenBeg, "*", this.ts.lineno);
                right = this.propertyName(-1, "*", n);
            }
        }
        final boolean b = right instanceof XmlRef;
        InfixExpression infixExpression;
        if (b) {
            infixExpression = new XmlMemberGet();
        }
        else {
            infixExpression = new PropertyGet();
        }
        if (b && position == 108) {
            infixExpression.setType(108);
        }
        position = left.getPosition();
        infixExpression.setPosition(position);
        infixExpression.setLength(this.getNodeEnd(right) - position);
        infixExpression.setOperatorPosition(tokenBeg - position);
        infixExpression.setLineno(left.getLineno());
        infixExpression.setLeft(left);
        infixExpression.setRight(right);
        return infixExpression;
    }
    
    private AstNode propertyName(final int atPos, final String s, final int n) throws IOException {
        int tokenBeg;
        if (atPos != -1) {
            tokenBeg = atPos;
        }
        else {
            tokenBeg = this.ts.tokenBeg;
        }
        final int lineno = this.ts.lineno;
        int tokenBeg2 = -1;
        final Name nameNode = this.createNameNode(true, this.currentToken);
        Name namespace = null;
        Name propName = nameNode;
        if (this.matchToken(144)) {
            namespace = nameNode;
            tokenBeg2 = this.ts.tokenBeg;
            final int nextToken = this.nextToken();
            if (nextToken != 23) {
                if (nextToken != 39) {
                    if (nextToken != 83) {
                        this.reportError("msg.no.name.after.coloncolon");
                        return this.makeErrorNode();
                    }
                    return this.xmlElemRef(atPos, namespace, tokenBeg2);
                }
                else {
                    propName = this.createNameNode();
                }
            }
            else {
                this.saveNameTokenData(this.ts.tokenBeg, "*", this.ts.lineno);
                propName = this.createNameNode(false, -1);
            }
        }
        if (namespace == null && n == 0 && atPos == -1) {
            return propName;
        }
        final XmlPropRef xmlPropRef = new XmlPropRef(tokenBeg, this.getNodeEnd(propName) - tokenBeg);
        xmlPropRef.setAtPos(atPos);
        xmlPropRef.setNamespace(namespace);
        xmlPropRef.setColonPos(tokenBeg2);
        xmlPropRef.setPropName(propName);
        xmlPropRef.setLineno(lineno);
        return xmlPropRef;
    }
    
    private String readFully(Reader reader) throws IOException {
        reader = new BufferedReader(reader);
        try {
            final char[] array = new char[1024];
            final StringBuilder sb = new StringBuilder(1024);
            while (true) {
                final int read = ((BufferedReader)reader).read(array, 0, 1024);
                if (read == -1) {
                    break;
                }
                sb.append(array, 0, read);
            }
            return sb.toString();
        }
        finally {
            ((BufferedReader)reader).close();
        }
    }
    
    private void recordComment(final int lineno, final String s) {
        if (this.scannedComments == null) {
            this.scannedComments = new ArrayList<Comment>();
        }
        final Comment currentJsDocComment = new Comment(this.ts.tokenBeg, this.ts.getTokenLength(), this.ts.commentType, s);
        if (this.ts.commentType == Token.CommentType.JSDOC && this.compilerEnv.isRecordingLocalJsDocComments()) {
            this.currentJsDocComment = currentJsDocComment;
        }
        currentJsDocComment.setLineno(lineno);
        this.scannedComments.add(currentJsDocComment);
    }
    
    private void recordLabel(final Label label, final LabeledStatement labeledStatement) throws IOException {
        if (this.peekToken() != 103) {
            this.codeBug();
        }
        this.consumeToken();
        final String name = label.getName();
        if (this.labelSet == null) {
            this.labelSet = new HashMap<String, LabeledStatement>();
        }
        else {
            final LabeledStatement labeledStatement2 = this.labelSet.get(name);
            if (labeledStatement2 != null) {
                if (this.compilerEnv.isIdeMode()) {
                    final Label labelByName = labeledStatement2.getLabelByName(name);
                    this.reportError("msg.dup.label", labelByName.getAbsolutePosition(), labelByName.getLength());
                }
                this.reportError("msg.dup.label", label.getPosition(), label.getLength());
            }
        }
        labeledStatement.addLabel(label);
        this.labelSet.put(name, labeledStatement);
    }
    
    private AstNode relExpr() throws IOException {
        AstNode shiftExpr = this.shiftExpr();
        while (true) {
            final int peekToken = this.peekToken();
            final int tokenBeg = this.ts.tokenBeg;
            Label_0083: {
                switch (peekToken) {
                    default: {
                        switch (peekToken) {
                            default: {
                                return shiftExpr;
                            }
                            case 52: {
                                if (this.inForInit) {
                                    return shiftExpr;
                                }
                                break Label_0083;
                            }
                            case 53: {
                                break Label_0083;
                            }
                        }
                        break;
                    }
                    case 14:
                    case 15:
                    case 16:
                    case 17: {
                        this.consumeToken();
                        shiftExpr = new InfixExpression(peekToken, shiftExpr, this.shiftExpr(), tokenBeg);
                        continue;
                    }
                }
            }
        }
    }
    
    private AstNode returnOrYield(int n, final boolean b) throws IOException {
        final boolean insideFunction = this.insideFunction();
        final int n2 = 4;
        if (!insideFunction) {
            String s;
            if (n == 4) {
                s = "msg.bad.return";
            }
            else {
                s = "msg.bad.yield";
            }
            this.reportError(s);
        }
        this.consumeToken();
        final int lineno = this.ts.lineno;
        final int tokenBeg = this.ts.tokenBeg;
        int n3 = this.ts.tokenEnd;
        AstNode expr = null;
        final int peekTokenOrEOL = this.peekTokenOrEOL();
        if (peekTokenOrEOL != 72 && peekTokenOrEOL != 82 && peekTokenOrEOL != 84 && peekTokenOrEOL != 86 && peekTokenOrEOL != 88) {
            switch (peekTokenOrEOL) {
                default: {
                    expr = this.expr();
                    n3 = this.getNodeEnd(expr);
                    break;
                }
                case -1:
                case 0:
                case 1: {
                    break;
                }
            }
        }
        final int endFlags = this.endFlags;
        AstNode astNode;
        if (n == 4) {
            final int endFlags2 = this.endFlags;
            n = n2;
            if (expr == null) {
                n = 2;
            }
            this.endFlags = (n | endFlags2);
            final ReturnStatement returnStatement = (ReturnStatement)(astNode = new ReturnStatement(tokenBeg, n3 - tokenBeg, expr));
            if (nowAllSet(endFlags, this.endFlags, 6)) {
                this.addStrictWarning("msg.return.inconsistent", "", tokenBeg, n3 - tokenBeg);
                astNode = returnStatement;
            }
        }
        else {
            if (!this.insideFunction()) {
                this.reportError("msg.bad.yield");
            }
            this.endFlags |= 0x8;
            final Yield yield = new Yield(tokenBeg, n3 - tokenBeg, expr);
            this.setRequiresActivation();
            this.setIsGenerator();
            astNode = yield;
            if (!b) {
                astNode = new ExpressionStatement(yield);
            }
        }
        if (this.insideFunction() && nowAllSet(endFlags, this.endFlags, 12)) {
            final Name functionName = ((FunctionNode)this.currentScriptOrFn).getFunctionName();
            if (functionName != null && functionName.length() != 0) {
                this.addError("msg.generator.returns", functionName.getIdentifier());
            }
            else {
                this.addError("msg.anon.generator.returns", "");
            }
        }
        astNode.setLineno(lineno);
        return astNode;
    }
    
    private void saveNameTokenData(final int prevNameTokenStart, final String prevNameTokenString, final int prevNameTokenLineno) {
        this.prevNameTokenStart = prevNameTokenStart;
        this.prevNameTokenString = prevNameTokenString;
        this.prevNameTokenLineno = prevNameTokenLineno;
    }
    
    private AstNode shiftExpr() throws IOException {
        AstNode addExpr = this.addExpr();
        while (true) {
            final int peekToken = this.peekToken();
            final int tokenBeg = this.ts.tokenBeg;
            switch (peekToken) {
                default: {
                    return addExpr;
                }
                case 18:
                case 19:
                case 20: {
                    this.consumeToken();
                    addExpr = new InfixExpression(peekToken, addExpr, this.addExpr(), tokenBeg);
                    continue;
                }
            }
        }
    }
    
    private AstNode statement() throws IOException {
        int tokenBeg;
        while (true) {
            tokenBeg = this.ts.tokenBeg;
            while (true) {
                Label_0153: {
                    try {
                        final AstNode statementHelper = this.statementHelper();
                        if (statementHelper != null) {
                            if (this.compilerEnv.isStrictMode() && !statementHelper.hasSideEffects()) {
                                final int position = statementHelper.getPosition();
                                final int max = Math.max(position, this.lineBeginningFor(position));
                                if (!(statementHelper instanceof EmptyStatement)) {
                                    break Label_0153;
                                }
                                final String s = "msg.extra.trailing.semi";
                                this.addStrictWarning(s, "", max, this.nodeEnd(statementHelper) - max);
                            }
                            return statementHelper;
                        }
                    }
                    catch (ParserException ex) {}
                    break;
                }
                final String s = "msg.no.side.effects";
                continue;
            }
        }
    Label_0135:
        while (true) {
            final int peekTokenOrEOL = this.peekTokenOrEOL();
            this.consumeToken();
            if (peekTokenOrEOL == 82) {
                break;
            }
            switch (peekTokenOrEOL) {
                default: {
                    continue;
                }
                case -1:
                case 0:
                case 1: {
                    break Label_0135;
                }
            }
        }
        return new EmptyStatement(tokenBeg, this.ts.tokenBeg - tokenBeg);
    }
    
    private AstNode statementHelper() throws IOException {
        if (this.currentLabel != null && this.currentLabel.getStatement() != null) {
            this.currentLabel = null;
        }
        final int peekToken = this.peekToken();
        final int tokenBeg = this.ts.tokenBeg;
        switch (peekToken) {
            default: {
                AstNode astNode = null;
                Label_0421: {
                    switch (peekToken) {
                        default: {
                            switch (peekToken) {
                                default: {
                                    switch (peekToken) {
                                        default: {
                                            final int lineno = this.ts.lineno;
                                            astNode = new ExpressionStatement(this.expr(), true ^ this.insideFunction());
                                            astNode.setLineno(lineno);
                                            break Label_0421;
                                        }
                                        case 160: {
                                            this.consumeToken();
                                            astNode = new KeywordLiteral(this.ts.tokenBeg, this.ts.tokenEnd - this.ts.tokenBeg, peekToken);
                                            astNode.setLineno(this.ts.lineno);
                                            break Label_0421;
                                        }
                                        case 114: {
                                            return this.switchStatement();
                                        }
                                        case 112: {
                                            return this.ifStatement();
                                        }
                                        case 109: {
                                            this.consumeToken();
                                            return this.function(3);
                                        }
                                        case 85: {
                                            return this.block();
                                        }
                                        case 50: {
                                            astNode = this.throwStatement();
                                            break Label_0421;
                                        }
                                        case 39: {
                                            astNode = this.nameOrLabel();
                                            if (astNode instanceof ExpressionStatement) {
                                                break Label_0421;
                                            }
                                            return astNode;
                                        }
                                        case 4:
                                        case 72: {
                                            astNode = this.returnOrYield(peekToken, false);
                                            break Label_0421;
                                        }
                                        case -1: {
                                            this.consumeToken();
                                            return this.makeErrorNode();
                                        }
                                    }
                                    break;
                                }
                                case 153: {
                                    astNode = this.letStatement();
                                    if (astNode instanceof VariableDeclaration && this.peekToken() == 82) {
                                        break Label_0421;
                                    }
                                    return astNode;
                                }
                                case 154: {
                                    break Label_0421;
                                }
                            }
                            break;
                        }
                        case 123: {
                            if (this.inUseStrictDirective) {
                                this.reportError("msg.no.with.strict");
                            }
                            return this.withStatement();
                        }
                        case 122: {
                            this.consumeToken();
                            final int lineno2 = this.ts.lineno;
                            astNode = this.variables(this.currentToken, this.ts.tokenBeg, true);
                            astNode.setLineno(lineno2);
                            break;
                        }
                        case 121: {
                            astNode = this.continueStatement();
                            break;
                        }
                        case 120: {
                            astNode = this.breakStatement();
                            break;
                        }
                        case 119: {
                            return this.forLoop();
                        }
                        case 118: {
                            return this.doLoop();
                        }
                        case 117: {
                            return this.whileLoop();
                        }
                        case 116: {
                            astNode = this.defaultXmlNamespace();
                            break;
                        }
                    }
                }
                this.autoInsertSemicolon(astNode);
                return astNode;
            }
            case 82: {
                this.consumeToken();
                final int tokenBeg2 = this.ts.tokenBeg;
                final EmptyStatement emptyStatement = new EmptyStatement(tokenBeg2, this.ts.tokenEnd - tokenBeg2);
                emptyStatement.setLineno(this.ts.lineno);
                return emptyStatement;
            }
            case 81: {
                return this.tryStatement();
            }
        }
    }
    
    private AstNode statements() throws IOException {
        return this.statements(null);
    }
    
    private AstNode statements(AstNode astNode) throws IOException {
        if (this.currentToken != 85 && !this.compilerEnv.isIdeMode()) {
            this.codeBug();
        }
        final int tokenBeg = this.ts.tokenBeg;
        if (astNode == null) {
            astNode = new Block(tokenBeg);
        }
        astNode.setLineno(this.ts.lineno);
        while (true) {
            final int peekToken = this.peekToken();
            if (peekToken <= 0 || peekToken == 86) {
                break;
            }
            astNode.addChild(this.statement());
        }
        astNode.setLength(this.ts.tokenBeg - tokenBeg);
        return astNode;
    }
    
    private SwitchStatement switchStatement() throws IOException {
        if (this.currentToken != 114) {
            this.codeBug();
        }
        this.consumeToken();
        final int tokenBeg = this.ts.tokenBeg;
        final SwitchStatement switchStatement = new SwitchStatement(tokenBeg);
        if (this.mustMatchToken(87, "msg.no.paren.switch")) {
            switchStatement.setLp(this.ts.tokenBeg - tokenBeg);
        }
        while (true) {
            switchStatement.setLineno(this.ts.lineno);
            switchStatement.setExpression(this.expr());
            this.enterSwitch(switchStatement);
            while (true) {
                Label_0352: {
                    try {
                        if (this.mustMatchToken(88, "msg.no.paren.after.switch")) {
                            switchStatement.setRp(this.ts.tokenBeg - tokenBeg);
                        }
                        this.mustMatchToken(85, "msg.no.brace.switch");
                        int n = 0;
                        while (true) {
                            final int nextToken = this.nextToken();
                            final int tokenBeg2 = this.ts.tokenBeg;
                            final int lineno = this.ts.lineno;
                            if (nextToken == 86) {
                                break;
                            }
                            AstNode expr = null;
                            switch (nextToken) {
                                case 116: {
                                    if (n != 0) {
                                        this.reportError("msg.double.switch.default");
                                    }
                                    n = 1;
                                    expr = null;
                                    this.mustMatchToken(103, "msg.no.colon.case");
                                    break;
                                }
                                case 115: {
                                    expr = this.expr();
                                    this.mustMatchToken(103, "msg.no.colon.case");
                                    break;
                                }
                                default: {
                                    break Label_0352;
                                }
                            }
                            final SwitchCase switchCase = new SwitchCase(tokenBeg2);
                            switchCase.setExpression(expr);
                            switchCase.setLength(this.ts.tokenEnd - tokenBeg);
                            switchCase.setLineno(lineno);
                            while (true) {
                                final int peekToken = this.peekToken();
                                if (peekToken == 86 || peekToken == 115 || peekToken == 116 || peekToken == 0) {
                                    break;
                                }
                                switchCase.addStatement(this.statement());
                            }
                            switchStatement.addCase(switchCase);
                        }
                        switchStatement.setLength(this.ts.tokenEnd - tokenBeg);
                        return switchStatement;
                        this.reportError("msg.bad.switch");
                        return switchStatement;
                    }
                    finally {
                        this.exitSwitch();
                    }
                }
                continue;
            }
        }
    }
    
    private ThrowStatement throwStatement() throws IOException {
        if (this.currentToken != 50) {
            this.codeBug();
        }
        this.consumeToken();
        final int tokenBeg = this.ts.tokenBeg;
        final int lineno = this.ts.lineno;
        if (this.peekTokenOrEOL() == 1) {
            this.reportError("msg.bad.throw.eol");
        }
        final AstNode expr = this.expr();
        final ThrowStatement throwStatement = new ThrowStatement(tokenBeg, this.getNodeEnd(expr), expr);
        throwStatement.setLineno(lineno);
        return throwStatement;
    }
    
    private TryStatement tryStatement() throws IOException {
        if (this.currentToken != 81) {
            this.codeBug();
        }
        this.consumeToken();
        final Comment andResetJsDoc = this.getAndResetJsDoc();
        final int tokenBeg = this.ts.tokenBeg;
        final int lineno = this.ts.lineno;
        final int n = -1;
        if (this.peekToken() != 85) {
            this.reportError("msg.no.brace.try");
        }
        final AstNode statement = this.statement();
        int n2 = this.getNodeEnd(statement);
        final List<CatchClause> list = null;
        List<CatchClause> catchClauses = null;
        int n3 = 0;
        final int peekToken = this.peekToken();
        int tokenBeg6;
        int nodeEnd2;
        if (peekToken == 124) {
            while (this.matchToken(124)) {
                final int lineno2 = this.ts.lineno;
                if (n3 != 0) {
                    this.reportError("msg.catch.unreachable");
                }
                final int tokenBeg2 = this.ts.tokenBeg;
                int tokenBeg3;
                if (this.mustMatchToken(87, "msg.no.paren.catch")) {
                    tokenBeg3 = this.ts.tokenBeg;
                }
                else {
                    tokenBeg3 = -1;
                }
                this.mustMatchToken(39, "msg.bad.catchcond");
                final Name nameNode = this.createNameNode();
                final String identifier = nameNode.getIdentifier();
                if (this.inUseStrictDirective && ("eval".equals(identifier) || "arguments".equals(identifier))) {
                    this.reportError("msg.bad.id.strict", identifier);
                }
                AstNode expr = null;
                int tokenBeg4;
                if (this.matchToken(112)) {
                    tokenBeg4 = this.ts.tokenBeg;
                    expr = this.expr();
                }
                else {
                    n3 = 1;
                    tokenBeg4 = -1;
                }
                int tokenBeg5;
                if (this.mustMatchToken(88, "msg.bad.catchcond")) {
                    tokenBeg5 = this.ts.tokenBeg;
                }
                else {
                    tokenBeg5 = -1;
                }
                this.mustMatchToken(85, "msg.no.brace.catchblock");
                final Block body = (Block)this.statements();
                final int nodeEnd = this.getNodeEnd(body);
                final CatchClause catchClause = new CatchClause(tokenBeg2);
                catchClause.setVarName(nameNode);
                catchClause.setCatchCondition(expr);
                catchClause.setBody(body);
                if (tokenBeg4 != -1) {
                    catchClause.setIfPosition(tokenBeg4 - tokenBeg2);
                }
                catchClause.setParens(tokenBeg3, tokenBeg5);
                catchClause.setLineno(lineno2);
                n2 = nodeEnd;
                if (this.mustMatchToken(86, "msg.no.brace.after.body")) {
                    n2 = this.ts.tokenEnd;
                }
                catchClause.setLength(n2 - tokenBeg2);
                List<CatchClause> list2;
                if ((list2 = catchClauses) == null) {
                    list2 = new ArrayList<CatchClause>();
                }
                list2.add(catchClause);
                catchClauses = list2;
            }
            tokenBeg6 = n;
            nodeEnd2 = n2;
        }
        else {
            final int n4 = -1;
            nodeEnd2 = n2;
            catchClauses = list;
            tokenBeg6 = n4;
            if (peekToken != 125) {
                this.mustMatchToken(125, "msg.try.no.catchfinally");
                tokenBeg6 = n4;
                catchClauses = list;
                nodeEnd2 = n2;
            }
        }
        AstNode statement2 = null;
        if (this.matchToken(125)) {
            tokenBeg6 = this.ts.tokenBeg;
            statement2 = this.statement();
            nodeEnd2 = this.getNodeEnd(statement2);
        }
        final TryStatement tryStatement = new TryStatement(tokenBeg, nodeEnd2 - tokenBeg);
        tryStatement.setTryBlock(statement);
        tryStatement.setCatchClauses(catchClauses);
        tryStatement.setFinallyBlock(statement2);
        if (tokenBeg6 != -1) {
            tryStatement.setFinallyPosition(tokenBeg6 - tokenBeg);
        }
        tryStatement.setLineno(lineno);
        if (andResetJsDoc != null) {
            tryStatement.setJsDocNode(andResetJsDoc);
        }
        return tryStatement;
    }
    
    private AstNode unaryExpr() throws IOException {
        final int peekToken = this.peekToken();
        final int lineno = this.ts.lineno;
        switch (peekToken) {
            case 106:
            case 107: {
                this.consumeToken();
                final UnaryExpression unaryExpression = new UnaryExpression(peekToken, this.ts.tokenBeg, this.memberExpr(true));
                unaryExpression.setLineno(lineno);
                this.checkBadIncDec(unaryExpression);
                return unaryExpression;
            }
            case 31: {
                this.consumeToken();
                final UnaryExpression unaryExpression2 = new UnaryExpression(peekToken, this.ts.tokenBeg, this.unaryExpr());
                unaryExpression2.setLineno(lineno);
                return unaryExpression2;
            }
            case 26:
            case 27:
            case 32:
            case 126: {
                this.consumeToken();
                final UnaryExpression unaryExpression3 = new UnaryExpression(peekToken, this.ts.tokenBeg, this.unaryExpr());
                unaryExpression3.setLineno(lineno);
                return unaryExpression3;
            }
            case 22: {
                this.consumeToken();
                final UnaryExpression unaryExpression4 = new UnaryExpression(29, this.ts.tokenBeg, this.unaryExpr());
                unaryExpression4.setLineno(lineno);
                return unaryExpression4;
            }
            case 21: {
                this.consumeToken();
                final UnaryExpression unaryExpression5 = new UnaryExpression(28, this.ts.tokenBeg, this.unaryExpr());
                unaryExpression5.setLineno(lineno);
                return unaryExpression5;
            }
            case 14: {
                if (this.compilerEnv.isXmlAvailable()) {
                    this.consumeToken();
                    return this.memberExprTail(true, this.xmlInitializer());
                }
                break;
            }
            case -1: {
                this.consumeToken();
                return this.makeErrorNode();
            }
        }
        final AstNode memberExpr = this.memberExpr(true);
        final int peekTokenOrEOL = this.peekTokenOrEOL();
        if (peekTokenOrEOL != 106 && peekTokenOrEOL != 107) {
            return memberExpr;
        }
        this.consumeToken();
        final UnaryExpression unaryExpression6 = new UnaryExpression(peekTokenOrEOL, this.ts.tokenBeg, memberExpr, true);
        unaryExpression6.setLineno(lineno);
        this.checkBadIncDec(unaryExpression6);
        return unaryExpression6;
    }
    
    private VariableDeclaration variables(final int n, final int n2, final boolean isStatement) throws IOException {
        final VariableDeclaration variableDeclaration = new VariableDeclaration(n2);
        variableDeclaration.setType(n);
        variableDeclaration.setLineno(this.ts.lineno);
        final Comment andResetJsDoc = this.getAndResetJsDoc();
        if (andResetJsDoc != null) {
            variableDeclaration.setJsDocNode(andResetJsDoc);
        }
        int n3;
        do {
            AstNode destructuringPrimaryExpr = null;
            AstNode nameNode = null;
            final int peekToken = this.peekToken();
            final int tokenBeg = this.ts.tokenBeg;
            n3 = this.ts.tokenEnd;
            if (peekToken != 83 && peekToken != 85) {
                this.mustMatchToken(39, "msg.bad.var");
                nameNode = this.createNameNode();
                nameNode.setLineno(this.ts.getLineno());
                if (this.inUseStrictDirective) {
                    final String string = this.ts.getString();
                    if ("eval".equals(string) || "arguments".equals(this.ts.getString())) {
                        this.reportError("msg.bad.id.strict", string);
                    }
                }
                this.defineSymbol(n, this.ts.getString(), this.inForInit);
            }
            else {
                destructuringPrimaryExpr = this.destructuringPrimaryExpr();
                n3 = this.getNodeEnd(destructuringPrimaryExpr);
                if (!(destructuringPrimaryExpr instanceof DestructuringForm)) {
                    this.reportError("msg.bad.assign.left", tokenBeg, n3 - tokenBeg);
                }
                this.markDestructuring(destructuringPrimaryExpr);
            }
            final int lineno = this.ts.lineno;
            final Comment andResetJsDoc2 = this.getAndResetJsDoc();
            AstNode assignExpr = null;
            if (this.matchToken(90)) {
                assignExpr = this.assignExpr();
                n3 = this.getNodeEnd(assignExpr);
            }
            final VariableInitializer variableInitializer = new VariableInitializer(tokenBeg, n3 - tokenBeg);
            if (destructuringPrimaryExpr != null) {
                if (assignExpr == null && !this.inForInit) {
                    this.reportError("msg.destruct.assign.no.init");
                }
                variableInitializer.setTarget(destructuringPrimaryExpr);
            }
            else {
                variableInitializer.setTarget(nameNode);
            }
            variableInitializer.setInitializer(assignExpr);
            variableInitializer.setType(n);
            variableInitializer.setJsDocNode(andResetJsDoc2);
            variableInitializer.setLineno(lineno);
            variableDeclaration.addVariable(variableInitializer);
        } while (this.matchToken(89));
        variableDeclaration.setLength(n3 - n2);
        variableDeclaration.setIsStatement(isStatement);
        return variableDeclaration;
    }
    
    private void warnMissingSemi(int max, final int n) {
        if (this.compilerEnv.isStrictMode()) {
            final int[] array = new int[2];
            final String line = this.ts.getLine(n, array);
            if (this.compilerEnv.isIdeMode()) {
                max = Math.max(max, n - array[1]);
            }
            if (line != null) {
                this.addStrictWarning("msg.missing.semi", "", max, n - max, array[0], line, array[1]);
                return;
            }
            this.addStrictWarning("msg.missing.semi", "", max, n - max);
        }
    }
    
    private void warnTrailingComma(int n, final List<?> list, final int n2) {
        if (this.compilerEnv.getWarnTrailingComma()) {
            if (!list.isEmpty()) {
                n = ((AstNode)list.get(0)).getPosition();
            }
            n = Math.max(n, this.lineBeginningFor(n2));
            this.addWarning("msg.extra.trailing.comma", n, n2 - n);
        }
    }
    
    private WhileLoop whileLoop() throws IOException {
        if (this.currentToken != 117) {
            this.codeBug();
        }
        this.consumeToken();
        final int tokenBeg = this.ts.tokenBeg;
        final WhileLoop whileLoop = new WhileLoop(tokenBeg);
        whileLoop.setLineno(this.ts.lineno);
        this.enterLoop(whileLoop);
        try {
            final ConditionData condition = this.condition();
            whileLoop.setCondition(condition.condition);
            whileLoop.setParens(condition.lp - tokenBeg, condition.rp - tokenBeg);
            final AstNode statement = this.statement();
            whileLoop.setLength(this.getNodeEnd(statement) - tokenBeg);
            whileLoop.setBody(statement);
            return whileLoop;
        }
        finally {
            this.exitLoop();
        }
    }
    
    private WithStatement withStatement() throws IOException {
        if (this.currentToken != 123) {
            this.codeBug();
        }
        this.consumeToken();
        final Comment andResetJsDoc = this.getAndResetJsDoc();
        final int lineno = this.ts.lineno;
        final int tokenBeg = this.ts.tokenBeg;
        int tokenBeg2 = -1;
        int tokenBeg3 = -1;
        if (this.mustMatchToken(87, "msg.no.paren.with")) {
            tokenBeg2 = this.ts.tokenBeg;
        }
        final AstNode expr = this.expr();
        if (this.mustMatchToken(88, "msg.no.paren.after.with")) {
            tokenBeg3 = this.ts.tokenBeg;
        }
        final AstNode statement = this.statement();
        final WithStatement withStatement = new WithStatement(tokenBeg, this.getNodeEnd(statement) - tokenBeg);
        withStatement.setJsDocNode(andResetJsDoc);
        withStatement.setExpression(expr);
        withStatement.setStatement(statement);
        withStatement.setParens(tokenBeg2, tokenBeg3);
        withStatement.setLineno(lineno);
        return withStatement;
    }
    
    private XmlElemRef xmlElemRef(final int atPos, final Name namespace, final int colonPos) throws IOException {
        final int tokenBeg = this.ts.tokenBeg;
        int tokenBeg2 = -1;
        int n;
        if (atPos != -1) {
            n = atPos;
        }
        else {
            n = tokenBeg;
        }
        final AstNode expr = this.expr();
        int n2 = this.getNodeEnd(expr);
        if (this.mustMatchToken(84, "msg.no.bracket.index")) {
            tokenBeg2 = this.ts.tokenBeg;
            n2 = this.ts.tokenEnd;
        }
        final XmlElemRef xmlElemRef = new XmlElemRef(n, n2 - n);
        xmlElemRef.setNamespace(namespace);
        xmlElemRef.setColonPos(colonPos);
        xmlElemRef.setAtPos(atPos);
        xmlElemRef.setExpression(expr);
        xmlElemRef.setBrackets(tokenBeg, tokenBeg2);
        return xmlElemRef;
    }
    
    private AstNode xmlInitializer() throws IOException {
        if (this.currentToken != 14) {
            this.codeBug();
        }
        final int tokenBeg = this.ts.tokenBeg;
        int i = this.ts.getFirstXMLToken();
        if (i != 145 && i != 148) {
            this.reportError("msg.syntax");
            return this.makeErrorNode();
        }
        final XmlLiteral xmlLiteral = new XmlLiteral(tokenBeg);
        xmlLiteral.setLineno(this.ts.lineno);
        while (i == 145) {
            xmlLiteral.addFragment(new XmlString(this.ts.tokenBeg, this.ts.getString()));
            this.mustMatchToken(85, "msg.syntax");
            final int tokenBeg2 = this.ts.tokenBeg;
            AstNode expr;
            if (this.peekToken() == 86) {
                expr = new EmptyExpression(tokenBeg2, this.ts.tokenEnd - tokenBeg2);
            }
            else {
                expr = this.expr();
            }
            this.mustMatchToken(86, "msg.syntax");
            final XmlExpression xmlExpression = new XmlExpression(tokenBeg2, expr);
            xmlExpression.setIsXmlAttribute(this.ts.isXMLAttribute());
            xmlExpression.setLength(this.ts.tokenEnd - tokenBeg2);
            xmlLiteral.addFragment(xmlExpression);
            i = this.ts.getNextXMLToken();
        }
        if (i != 148) {
            this.reportError("msg.syntax");
            return this.makeErrorNode();
        }
        xmlLiteral.addFragment(new XmlString(this.ts.tokenBeg, this.ts.getString()));
        return xmlLiteral;
    }
    
    void addError(final String s) {
        this.addError(s, this.ts.tokenBeg, this.ts.tokenEnd - this.ts.tokenBeg);
    }
    
    void addError(final String s, final int n, final int n2) {
        this.addError(s, null, n, n2);
    }
    
    void addError(final String s, final String s2) {
        this.addError(s, s2, this.ts.tokenBeg, this.ts.tokenEnd - this.ts.tokenBeg);
    }
    
    void addError(String line, String lookupMessage, int lineno, int offset) {
        ++this.syntaxErrorCount;
        lookupMessage = this.lookupMessage(line, lookupMessage);
        if (this.errorCollector != null) {
            this.errorCollector.error(lookupMessage, this.sourceURI, lineno, offset);
            return;
        }
        lineno = 1;
        offset = 1;
        line = "";
        if (this.ts != null) {
            lineno = this.ts.getLineno();
            line = this.ts.getLine();
            offset = this.ts.getOffset();
        }
        this.errorReporter.error(lookupMessage, this.sourceURI, lineno, line, offset);
    }
    
    void addStrictWarning(final String s, final String s2) {
        int tokenBeg = -1;
        int n = -1;
        if (this.ts != null) {
            tokenBeg = this.ts.tokenBeg;
            n = this.ts.tokenEnd - this.ts.tokenBeg;
        }
        this.addStrictWarning(s, s2, tokenBeg, n);
    }
    
    void addStrictWarning(final String s, final String s2, final int n, final int n2) {
        if (this.compilerEnv.isStrictMode()) {
            this.addWarning(s, s2, n, n2);
        }
    }
    
    void addWarning(final String s, final int n, final int n2) {
        this.addWarning(s, null, n, n2);
    }
    
    void addWarning(final String s, final String s2) {
        int tokenBeg = -1;
        int n = -1;
        if (this.ts != null) {
            tokenBeg = this.ts.tokenBeg;
            n = this.ts.tokenEnd - this.ts.tokenBeg;
        }
        this.addWarning(s, s2, tokenBeg, n);
    }
    
    void addWarning(final String s, final String s2, final int n, final int n2) {
        final String lookupMessage = this.lookupMessage(s, s2);
        if (this.compilerEnv.reportWarningAsError()) {
            this.addError(s, s2, n, n2);
            return;
        }
        if (this.errorCollector != null) {
            this.errorCollector.warning(lookupMessage, this.sourceURI, n, n2);
            return;
        }
        this.errorReporter.warning(lookupMessage, this.sourceURI, this.ts.getLineno(), this.ts.getLine(), this.ts.getOffset());
    }
    
    protected void checkActivationName(final String s, final int n) {
        if (!this.insideFunction()) {
            return;
        }
        final boolean b = false;
        boolean b2;
        if (!"arguments".equals(s) && (this.compilerEnv.getActivationNames() == null || !this.compilerEnv.getActivationNames().contains(s))) {
            b2 = b;
            if ("length".equals(s)) {
                b2 = b;
                if (n == 33) {
                    b2 = b;
                    if (this.compilerEnv.getLanguageVersion() == 120) {
                        b2 = true;
                    }
                }
            }
        }
        else {
            b2 = true;
        }
        if (b2) {
            this.setRequiresActivation();
        }
    }
    
    protected void checkMutableReference(final Node node) {
        if ((node.getIntProp(16, 0) & 0x4) != 0x0) {
            this.reportError("msg.bad.assign.left");
        }
    }
    
    Node createDestructuringAssignment(final int n, Node destructuringAssignmentHelper, final Node node) {
        final String nextTempName = this.currentScriptOrFn.getNextTempName();
        destructuringAssignmentHelper = this.destructuringAssignmentHelper(n, destructuringAssignmentHelper, node, nextTempName);
        destructuringAssignmentHelper.getLastChild().addChildToBack(this.createName(nextTempName));
        return destructuringAssignmentHelper;
    }
    
    protected Node createName(final int type, final String s, final Node node) {
        final Node name = this.createName(s);
        name.setType(type);
        if (node != null) {
            name.addChildToBack(node);
        }
        return name;
    }
    
    protected Node createName(final String s) {
        this.checkActivationName(s, 39);
        return Node.newString(39, s);
    }
    
    protected Node createNumber(final double n) {
        return Node.newNumber(n);
    }
    
    protected Scope createScopeNode(final int type, final int lineno) {
        final Scope scope = new Scope();
        scope.setType(type);
        scope.setLineno(lineno);
        return scope;
    }
    
    void defineSymbol(final int n, final String s) {
        this.defineSymbol(n, s, false);
    }
    
    void defineSymbol(final int n, final String s, final boolean b) {
        if (s == null) {
            if (this.compilerEnv.isIdeMode()) {
                return;
            }
            this.codeBug();
        }
        final Scope definingScope = this.currentScope.getDefiningScope(s);
        Symbol symbol;
        if (definingScope != null) {
            symbol = definingScope.getSymbol(s);
        }
        else {
            symbol = null;
        }
        int declType;
        if (symbol != null) {
            declType = symbol.getDeclType();
        }
        else {
            declType = -1;
        }
        if (symbol != null && (declType == 154 || n == 154 || (definingScope == this.currentScope && declType == 153))) {
            String s2;
            if (declType == 154) {
                s2 = "msg.const.redecl";
            }
            else if (declType == 153) {
                s2 = "msg.let.redecl";
            }
            else if (declType == 122) {
                s2 = "msg.var.redecl";
            }
            else if (declType == 109) {
                s2 = "msg.fn.redecl";
            }
            else {
                s2 = "msg.parm.redecl";
            }
            this.addError(s2, s);
            return;
        }
        if (n != 87) {
            if (n != 109 && n != 122) {
                switch (n) {
                    default: {
                        throw this.codeBug();
                    }
                    case 153: {
                        if (!b && (this.currentScope.getType() == 112 || this.currentScope instanceof Loop)) {
                            this.addError("msg.let.decl.not.in.block");
                            return;
                        }
                        this.currentScope.putSymbol(new Symbol(n, s));
                        return;
                    }
                    case 154: {
                        break;
                    }
                }
            }
            if (symbol != null) {
                if (declType == 122) {
                    this.addStrictWarning("msg.var.redecl", s);
                    return;
                }
                if (declType == 87) {
                    this.addStrictWarning("msg.var.hides.arg", s);
                }
            }
            else {
                this.currentScriptOrFn.putSymbol(new Symbol(n, s));
            }
            return;
        }
        if (symbol != null) {
            this.addWarning("msg.dup.parms", s);
        }
        this.currentScriptOrFn.putSymbol(new Symbol(n, s));
    }
    
    boolean destructuringArray(final ArrayLiteral arrayLiteral, final int n, final String s, final Node node, final List<String> list) {
        boolean b = true;
        int n2;
        if (n == 154) {
            n2 = 155;
        }
        else {
            n2 = 8;
        }
        int n3 = 0;
        for (final AstNode astNode : arrayLiteral.getElements()) {
            if (astNode.getType() == 128) {
                ++n3;
            }
            else {
                final Node node2 = new Node(36, this.createName(s), this.createNumber(n3));
                if (astNode.getType() == 39) {
                    final String string = astNode.getString();
                    node.addChildToBack(new Node(n2, this.createName(49, string, null), node2));
                    if (n != -1) {
                        this.defineSymbol(n, string, true);
                        list.add(string);
                    }
                }
                else {
                    node.addChildToBack(this.destructuringAssignmentHelper(n, astNode, node2, this.currentScriptOrFn.getNextTempName()));
                }
                ++n3;
                b = false;
            }
        }
        return b;
    }
    
    Node destructuringAssignmentHelper(final int n, final Node node, Node node2, final String s) {
        final Scope scopeNode = this.createScopeNode(158, node.getLineno());
        scopeNode.addChildToFront(new Node(153, this.createName(39, s, node2)));
        try {
            this.pushScope(scopeNode);
            this.defineSymbol(153, s, true);
            this.popScope();
            node2 = new Node(89);
            scopeNode.addChildToBack(node2);
            final ArrayList<String> list = new ArrayList<String>();
            boolean b = true;
            final int type = node.getType();
            if (type != 33 && type != 36) {
                switch (type) {
                    default: {
                        this.reportError("msg.bad.assign.left");
                        break;
                    }
                    case 66: {
                        b = this.destructuringObject((ObjectLiteral)node, n, s, node2, list);
                        break;
                    }
                    case 65: {
                        b = this.destructuringArray((ArrayLiteral)node, n, s, node2, list);
                        break;
                    }
                }
            }
            else {
                Label_0214: {
                    if (n != 122) {
                        switch (n) {
                            default: {
                                break Label_0214;
                            }
                            case 153:
                            case 154: {
                                break;
                            }
                        }
                    }
                    this.reportError("msg.bad.assign.left");
                }
                node2.addChildToBack(this.simpleAssignment(node, this.createName(s)));
            }
            if (b) {
                node2.addChildToBack(this.createNumber(0.0));
            }
            scopeNode.putProp(22, list);
            return scopeNode;
        }
        finally {
            this.popScope();
        }
    }
    
    boolean destructuringObject(final ObjectLiteral objectLiteral, final int n, final String s, final Node node, final List<String> list) {
        boolean b = true;
        int n2;
        if (n == 154) {
            n2 = 155;
        }
        else {
            n2 = 8;
        }
        for (final ObjectProperty objectProperty : objectLiteral.getElements()) {
            int lineno = 0;
            if (this.ts != null) {
                lineno = this.ts.lineno;
            }
            final AstNode left = objectProperty.getLeft();
            Node node2;
            if (left instanceof Name) {
                node2 = new Node(33, this.createName(s), Node.newString(((Name)left).getIdentifier()));
            }
            else if (left instanceof StringLiteral) {
                node2 = new Node(33, this.createName(s), Node.newString(((StringLiteral)left).getValue()));
            }
            else {
                if (!(left instanceof NumberLiteral)) {
                    throw this.codeBug();
                }
                node2 = new Node(36, this.createName(s), this.createNumber((int)((NumberLiteral)left).getNumber()));
            }
            node2.setLineno(lineno);
            final AstNode right = objectProperty.getRight();
            if (right.getType() == 39) {
                final String identifier = ((Name)right).getIdentifier();
                node.addChildToBack(new Node(n2, this.createName(49, identifier, null), node2));
                if (n != -1) {
                    this.defineSymbol(n, identifier, true);
                    list.add(identifier);
                }
            }
            else {
                node.addChildToBack(this.destructuringAssignmentHelper(n, right, node2, this.currentScriptOrFn.getNextTempName()));
            }
            b = false;
        }
        return b;
    }
    
    public boolean eof() {
        return this.ts.eof();
    }
    
    boolean insideFunction() {
        return this.nestingOfFunction != 0;
    }
    
    String lookupMessage(final String s) {
        return this.lookupMessage(s, null);
    }
    
    String lookupMessage(final String s, final String s2) {
        if (s2 == null) {
            return ScriptRuntime.getMessage0(s);
        }
        return ScriptRuntime.getMessage1(s, s2);
    }
    
    void markDestructuring(final AstNode astNode) {
        if (astNode instanceof DestructuringForm) {
            ((DestructuringForm)astNode).setIsDestructuring(true);
            return;
        }
        if (astNode instanceof ParenthesizedExpression) {
            this.markDestructuring(((ParenthesizedExpression)astNode).getExpression());
        }
    }
    
    public AstRoot parse(final Reader reader, final String sourceURI, final int n) throws IOException {
        if (this.parseFinished) {
            throw new IllegalStateException("parser reused");
        }
        if (this.compilerEnv.isIdeMode()) {
            return this.parse(this.readFully(reader), sourceURI, n);
        }
        try {
            this.sourceURI = sourceURI;
            this.ts = new TokenStream(this, reader, null, n);
            return this.parse();
        }
        finally {
            this.parseFinished = true;
        }
    }
    
    public AstRoot parse(final String s, final String sourceURI, final int n) {
        if (this.parseFinished) {
            throw new IllegalStateException("parser reused");
        }
        this.sourceURI = sourceURI;
        if (this.compilerEnv.isIdeMode()) {
            this.sourceChars = s.toCharArray();
        }
        this.ts = new TokenStream(this, null, s, n);
        try {
            try {
                final AstRoot parse = this.parse();
                this.parseFinished = true;
                return parse;
            }
            finally {}
        }
        catch (IOException ex) {
            throw new IllegalStateException();
        }
        this.parseFinished = true;
    }
    
    void popScope() {
        this.currentScope = this.currentScope.getParentScope();
    }
    
    void pushScope(final Scope currentScope) {
        final Scope parentScope = currentScope.getParentScope();
        if (parentScope != null) {
            if (parentScope != this.currentScope) {
                this.codeBug();
            }
        }
        else {
            this.currentScope.addChildScope(currentScope);
        }
        this.currentScope = currentScope;
    }
    
    protected AstNode removeParens(AstNode expression) {
        while (expression instanceof ParenthesizedExpression) {
            expression = ((ParenthesizedExpression)expression).getExpression();
        }
        return expression;
    }
    
    void reportError(final String s) {
        this.reportError(s, null);
    }
    
    void reportError(final String s, final int n, final int n2) {
        this.reportError(s, null, n, n2);
    }
    
    void reportError(final String s, final String s2) {
        if (this.ts == null) {
            this.reportError(s, s2, 1, 1);
            return;
        }
        this.reportError(s, s2, this.ts.tokenBeg, this.ts.tokenEnd - this.ts.tokenBeg);
    }
    
    void reportError(final String s, final String s2, final int n, final int n2) {
        this.addError(s, n, n2);
        if (!this.compilerEnv.recoverFromErrors()) {
            throw new ParserException();
        }
    }
    
    protected void setIsGenerator() {
        if (this.insideFunction()) {
            ((FunctionNode)this.currentScriptOrFn).setIsGenerator();
        }
    }
    
    protected void setRequiresActivation() {
        if (this.insideFunction()) {
            ((FunctionNode)this.currentScriptOrFn).setRequiresActivation();
        }
    }
    
    protected Node simpleAssignment(Node node, final Node node2) {
        final int type = node.getType();
        if (type == 33 || type == 36) {
            Node node3;
            if (node instanceof PropertyGet) {
                node3 = ((PropertyGet)node).getTarget();
                node = ((PropertyGet)node).getProperty();
            }
            else if (node instanceof ElementGet) {
                node3 = ((ElementGet)node).getTarget();
                node = ((ElementGet)node).getElement();
            }
            else {
                node3 = node.getFirstChild();
                node = node.getLastChild();
            }
            int n;
            if (type == 33) {
                n = 35;
                node.setType(41);
            }
            else {
                n = 37;
            }
            return new Node(n, node3, node, node2);
        }
        if (type == 39) {
            if (this.inUseStrictDirective && "eval".equals(((Name)node).getIdentifier())) {
                this.reportError("msg.bad.id.strict", ((Name)node).getIdentifier());
            }
            node.setType(49);
            return new Node(8, node, node2);
        }
        if (type != 67) {
            throw this.codeBug();
        }
        node = node.getFirstChild();
        this.checkMutableReference(node);
        return new Node(68, node, node2);
    }
    
    private static class ParserException extends RuntimeException
    {
        static final long serialVersionUID = 5882582646773765630L;
    }
    
    private static class ConditionData
    {
        AstNode condition;
        int lp;
        int rp;
        
        private ConditionData() {
            this.lp = -1;
            this.rp = -1;
        }
    }
    
    protected class PerFunctionVariables
    {
        private Scope savedCurrentScope;
        private ScriptNode savedCurrentScriptOrFn;
        private int savedEndFlags;
        private boolean savedInForInit;
        private Map<String, LabeledStatement> savedLabelSet;
        private List<Jump> savedLoopAndSwitchSet;
        private List<Loop> savedLoopSet;
        
        PerFunctionVariables(final FunctionNode functionNode) {
            this.savedCurrentScriptOrFn = Parser.this.currentScriptOrFn;
            Parser.this.currentScriptOrFn = functionNode;
            this.savedCurrentScope = Parser.this.currentScope;
            Parser.this.currentScope = functionNode;
            this.savedLabelSet = Parser.this.labelSet;
            Parser.this.labelSet = null;
            this.savedLoopSet = Parser.this.loopSet;
            Parser.this.loopSet = null;
            this.savedLoopAndSwitchSet = Parser.this.loopAndSwitchSet;
            Parser.this.loopAndSwitchSet = null;
            this.savedEndFlags = Parser.this.endFlags;
            Parser.this.endFlags = 0;
            this.savedInForInit = Parser.this.inForInit;
            Parser.this.inForInit = false;
        }
        
        void restore() {
            Parser.this.currentScriptOrFn = this.savedCurrentScriptOrFn;
            Parser.this.currentScope = this.savedCurrentScope;
            Parser.this.labelSet = this.savedLabelSet;
            Parser.this.loopSet = this.savedLoopSet;
            Parser.this.loopAndSwitchSet = this.savedLoopAndSwitchSet;
            Parser.this.endFlags = this.savedEndFlags;
            Parser.this.inForInit = this.savedInForInit;
        }
    }
}
