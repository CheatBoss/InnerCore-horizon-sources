package org.mozilla.javascript;

import org.mozilla.javascript.ast.*;

class CodeGenerator extends Icode
{
    private static final int ECF_TAIL = 1;
    private static final int MIN_FIXUP_TABLE_SIZE = 40;
    private static final int MIN_LABEL_TABLE_SIZE = 32;
    private CompilerEnvirons compilerEnv;
    private int doubleTableTop;
    private int exceptionTableTop;
    private long[] fixupTable;
    private int fixupTableTop;
    private int iCodeTop;
    private InterpreterData itsData;
    private boolean itsInFunctionFlag;
    private boolean itsInTryFlag;
    private int[] labelTable;
    private int labelTableTop;
    private int lineNumber;
    private ObjArray literalIds;
    private int localTop;
    private ScriptNode scriptOrFn;
    private int stackDepth;
    private ObjToIntMap strings;
    
    CodeGenerator() {
        this.strings = new ObjToIntMap(20);
        this.literalIds = new ObjArray();
    }
    
    private void addBackwardGoto(final int n, final int n2) {
        final int iCodeTop = this.iCodeTop;
        if (iCodeTop <= n2) {
            throw Kit.codeBug();
        }
        this.addGotoOp(n);
        this.resolveGoto(iCodeTop, n2);
    }
    
    private void addExceptionHandler(final int n, final int n2, final int n3, final boolean b, final int n4, final int n5) {
        throw new Runtime("d2j fail translate: java.lang.RuntimeException: can not merge Z and I\r\n\tat com.googlecode.dex2jar.ir.TypeClass.merge(TypeClass.java:100)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeRef.updateTypeClass(TypeTransformer.java:174)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.fixTypes(TypeTransformer.java:244)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.analyze(TypeTransformer.java:207)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer.transform(TypeTransformer.java:44)\r\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:162)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\r\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\r\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\r\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\r\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\r\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\r\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\r\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\r\n");
    }
    
    private void addGoto(final Node node, int fixupTableTop) {
        final int targetLabel = this.getTargetLabel(node);
        if (targetLabel >= this.labelTableTop) {
            Kit.codeBug();
        }
        final int n = this.labelTable[targetLabel];
        if (n != -1) {
            this.addBackwardGoto(fixupTableTop, n);
            return;
        }
        final int iCodeTop = this.iCodeTop;
        this.addGotoOp(fixupTableTop);
        fixupTableTop = this.fixupTableTop;
        if (this.fixupTable == null || fixupTableTop == this.fixupTable.length) {
            if (this.fixupTable == null) {
                this.fixupTable = new long[40];
            }
            else {
                final long[] fixupTable = new long[this.fixupTable.length * 2];
                System.arraycopy(this.fixupTable, 0, fixupTable, 0, fixupTableTop);
                this.fixupTable = fixupTable;
            }
        }
        this.fixupTableTop = fixupTableTop + 1;
        this.fixupTable[fixupTableTop] = ((long)targetLabel << 32 | (long)iCodeTop);
    }
    
    private void addGotoOp(final int n) {
        final byte[] itsICode = this.itsData.itsICode;
        final int iCodeTop = this.iCodeTop;
        byte[] increaseICodeCapacity = itsICode;
        if (iCodeTop + 3 > itsICode.length) {
            increaseICodeCapacity = this.increaseICodeCapacity(3);
        }
        increaseICodeCapacity[iCodeTop] = (byte)n;
        this.iCodeTop = iCodeTop + 1 + 2;
    }
    
    private void addIcode(final int n) {
        if (!Icode.validIcode(n)) {
            throw Kit.codeBug();
        }
        this.addUint8(n & 0xFF);
    }
    
    private void addIndexOp(final int n, final int n2) {
        this.addIndexPrefix(n2);
        if (Icode.validIcode(n)) {
            this.addIcode(n);
            return;
        }
        this.addToken(n);
    }
    
    private void addIndexPrefix(final int n) {
        if (n < 0) {
            Kit.codeBug();
        }
        if (n < 6) {
            this.addIcode(-32 - n);
            return;
        }
        if (n <= 255) {
            this.addIcode(-38);
            this.addUint8(n);
            return;
        }
        if (n <= 65535) {
            this.addIcode(-39);
            this.addUint16(n);
            return;
        }
        this.addIcode(-40);
        this.addInt(n);
    }
    
    private void addInt(final int n) {
        final byte[] itsICode = this.itsData.itsICode;
        final int iCodeTop = this.iCodeTop;
        byte[] increaseICodeCapacity = itsICode;
        if (iCodeTop + 4 > itsICode.length) {
            increaseICodeCapacity = this.increaseICodeCapacity(4);
        }
        increaseICodeCapacity[iCodeTop] = (byte)(n >>> 24);
        increaseICodeCapacity[iCodeTop + 1] = (byte)(n >>> 16);
        increaseICodeCapacity[iCodeTop + 2] = (byte)(n >>> 8);
        increaseICodeCapacity[iCodeTop + 3] = (byte)n;
        this.iCodeTop = iCodeTop + 4;
    }
    
    private void addStringOp(final int n, final String s) {
        this.addStringPrefix(s);
        if (Icode.validIcode(n)) {
            this.addIcode(n);
            return;
        }
        this.addToken(n);
    }
    
    private void addStringPrefix(final String s) {
        int n;
        if ((n = this.strings.get(s, -1)) == -1) {
            n = this.strings.size();
            this.strings.put(s, n);
        }
        if (n < 4) {
            this.addIcode(-41 - n);
            return;
        }
        if (n <= 255) {
            this.addIcode(-45);
            this.addUint8(n);
            return;
        }
        if (n <= 65535) {
            this.addIcode(-46);
            this.addUint16(n);
            return;
        }
        this.addIcode(-47);
        this.addInt(n);
    }
    
    private void addToken(final int n) {
        if (!Icode.validTokenCode(n)) {
            throw Kit.codeBug();
        }
        this.addUint8(n);
    }
    
    private void addUint16(final int n) {
        if ((0xFFFF0000 & n) != 0x0) {
            throw Kit.codeBug();
        }
        final byte[] itsICode = this.itsData.itsICode;
        final int iCodeTop = this.iCodeTop;
        byte[] increaseICodeCapacity = itsICode;
        if (iCodeTop + 2 > itsICode.length) {
            increaseICodeCapacity = this.increaseICodeCapacity(2);
        }
        increaseICodeCapacity[iCodeTop] = (byte)(n >>> 8);
        increaseICodeCapacity[iCodeTop + 1] = (byte)n;
        this.iCodeTop = iCodeTop + 2;
    }
    
    private void addUint8(final int n) {
        if ((n & 0xFFFFFF00) != 0x0) {
            throw Kit.codeBug();
        }
        final byte[] itsICode = this.itsData.itsICode;
        final int iCodeTop = this.iCodeTop;
        byte[] increaseICodeCapacity = itsICode;
        if (iCodeTop == itsICode.length) {
            increaseICodeCapacity = this.increaseICodeCapacity(1);
        }
        increaseICodeCapacity[iCodeTop] = (byte)n;
        this.iCodeTop = iCodeTop + 1;
    }
    
    private void addVarOp(int n, final int n2) {
        if (n != -7) {
            if (n != 156) {
                switch (n) {
                    default: {
                        throw Kit.codeBug();
                    }
                    case 55:
                    case 56: {
                        if (n2 < 128) {
                            if (n == 55) {
                                n = -48;
                            }
                            else {
                                n = -49;
                            }
                            this.addIcode(n);
                            this.addUint8(n2);
                            return;
                        }
                        break;
                    }
                }
            }
            else {
                if (n2 < 128) {
                    this.addIcode(-61);
                    this.addUint8(n2);
                    return;
                }
                this.addIndexOp(-60, n2);
                return;
            }
        }
        this.addIndexOp(n, n2);
    }
    
    private int allocLocal() {
        final int localTop = this.localTop;
        ++this.localTop;
        if (this.localTop > this.itsData.itsMaxLocals) {
            this.itsData.itsMaxLocals = this.localTop;
        }
        return localTop;
    }
    
    private RuntimeException badTree(final Node node) {
        throw new RuntimeException(node.toString());
    }
    
    private void fixLabelGotos() {
        for (int i = 0; i < this.fixupTableTop; ++i) {
            final long n = this.fixupTable[i];
            final int n2 = (int)(n >> 32);
            final int n3 = (int)n;
            final int n4 = this.labelTable[n2];
            if (n4 == -1) {
                throw Kit.codeBug();
            }
            this.resolveGoto(n3, n4);
        }
        this.fixupTableTop = 0;
    }
    
    private void generateCallFunAndThis(Node node) {
        final int type = node.getType();
        if (type != 33 && type != 36) {
            if (type != 39) {
                this.visitExpression(node, 0);
                this.addIcode(-18);
                this.stackChange(1);
                return;
            }
            this.addStringOp(-15, node.getString());
            this.stackChange(2);
        }
        else {
            node = node.getFirstChild();
            this.visitExpression(node, 0);
            node = node.getNext();
            if (type == 33) {
                this.addStringOp(-16, node.getString());
                this.stackChange(1);
                return;
            }
            this.visitExpression(node, 0);
            this.addIcode(-17);
        }
    }
    
    private void generateFunctionICode() {
        this.itsInFunctionFlag = true;
        final FunctionNode functionNode = (FunctionNode)this.scriptOrFn;
        this.itsData.itsFunctionType = functionNode.getFunctionType();
        this.itsData.itsNeedsActivation = functionNode.requiresActivation();
        if (functionNode.getFunctionName() != null) {
            this.itsData.itsName = functionNode.getName();
        }
        if (functionNode.isGenerator()) {
            this.addIcode(-62);
            this.addUint16(functionNode.getBaseLineno() & 0xFFFF);
        }
        this.generateICodeFromTree(functionNode.getLastChild());
    }
    
    private void generateICodeFromTree(final Node node) {
        this.generateNestedFunctions();
        this.generateRegExpLiterals();
        this.visitStatement(node, 0);
        this.fixLabelGotos();
        if (this.itsData.itsFunctionType == 0) {
            this.addToken(64);
        }
        if (this.itsData.itsICode.length != this.iCodeTop) {
            final byte[] itsICode = new byte[this.iCodeTop];
            System.arraycopy(this.itsData.itsICode, 0, itsICode, 0, this.iCodeTop);
            this.itsData.itsICode = itsICode;
        }
        if (this.strings.size() == 0) {
            this.itsData.itsStringTable = null;
        }
        else {
            this.itsData.itsStringTable = new String[this.strings.size()];
            final ObjToIntMap.Iterator iterator = this.strings.newIterator();
            iterator.start();
            while (!iterator.done()) {
                final String s = (String)iterator.getKey();
                final int value = iterator.getValue();
                if (this.itsData.itsStringTable[value] != null) {
                    Kit.codeBug();
                }
                this.itsData.itsStringTable[value] = s;
                iterator.next();
            }
        }
        if (this.doubleTableTop == 0) {
            this.itsData.itsDoubleTable = null;
        }
        else if (this.itsData.itsDoubleTable.length != this.doubleTableTop) {
            final double[] itsDoubleTable = new double[this.doubleTableTop];
            System.arraycopy(this.itsData.itsDoubleTable, 0, itsDoubleTable, 0, this.doubleTableTop);
            this.itsData.itsDoubleTable = itsDoubleTable;
        }
        if (this.exceptionTableTop != 0 && this.itsData.itsExceptionTable.length != this.exceptionTableTop) {
            final int[] itsExceptionTable = new int[this.exceptionTableTop];
            System.arraycopy(this.itsData.itsExceptionTable, 0, itsExceptionTable, 0, this.exceptionTableTop);
            this.itsData.itsExceptionTable = itsExceptionTable;
        }
        this.itsData.itsMaxVars = this.scriptOrFn.getParamAndVarCount();
        this.itsData.itsMaxFrameArray = this.itsData.itsMaxVars + this.itsData.itsMaxLocals + this.itsData.itsMaxStack;
        this.itsData.argNames = this.scriptOrFn.getParamAndVarNames();
        this.itsData.argIsConst = this.scriptOrFn.getParamAndVarConst();
        this.itsData.argCount = this.scriptOrFn.getParamCount();
        this.itsData.encodedSourceStart = this.scriptOrFn.getEncodedSourceStart();
        this.itsData.encodedSourceEnd = this.scriptOrFn.getEncodedSourceEnd();
        if (this.literalIds.size() != 0) {
            this.itsData.literalIds = this.literalIds.toArray();
        }
    }
    
    private void generateNestedFunctions() {
        final int functionCount = this.scriptOrFn.getFunctionCount();
        if (functionCount == 0) {
            return;
        }
        final InterpreterData[] itsNestedFunctions = new InterpreterData[functionCount];
        for (int i = 0; i != functionCount; ++i) {
            final FunctionNode functionNode = this.scriptOrFn.getFunctionNode(i);
            final CodeGenerator codeGenerator = new CodeGenerator();
            codeGenerator.compilerEnv = this.compilerEnv;
            codeGenerator.scriptOrFn = functionNode;
            codeGenerator.itsData = new InterpreterData(this.itsData);
            codeGenerator.generateFunctionICode();
            itsNestedFunctions[i] = codeGenerator.itsData;
        }
        this.itsData.itsNestedFunctions = itsNestedFunctions;
    }
    
    private void generateRegExpLiterals() {
        final int regexpCount = this.scriptOrFn.getRegexpCount();
        if (regexpCount == 0) {
            return;
        }
        final Context context = Context.getContext();
        final RegExpProxy checkRegExpProxy = ScriptRuntime.checkRegExpProxy(context);
        final Object[] itsRegExpLiterals = new Object[regexpCount];
        for (int i = 0; i != regexpCount; ++i) {
            itsRegExpLiterals[i] = checkRegExpProxy.compileRegExp(context, this.scriptOrFn.getRegexpString(i), this.scriptOrFn.getRegexpFlags(i));
        }
        this.itsData.itsRegExpLiterals = itsRegExpLiterals;
    }
    
    private int getDoubleIndex(final double n) {
        final int doubleTableTop = this.doubleTableTop;
        if (doubleTableTop == 0) {
            this.itsData.itsDoubleTable = new double[64];
        }
        else if (this.itsData.itsDoubleTable.length == doubleTableTop) {
            final double[] itsDoubleTable = new double[doubleTableTop * 2];
            System.arraycopy(this.itsData.itsDoubleTable, 0, itsDoubleTable, 0, doubleTableTop);
            this.itsData.itsDoubleTable = itsDoubleTable;
        }
        this.itsData.itsDoubleTable[doubleTableTop] = n;
        this.doubleTableTop = doubleTableTop + 1;
        return doubleTableTop;
    }
    
    private int getLocalBlockRef(final Node node) {
        return ((Node)node.getProp(3)).getExistingIntProp(2);
    }
    
    private int getTargetLabel(final Node node) {
        final int labelId = node.labelId();
        if (labelId != -1) {
            return labelId;
        }
        final int labelTableTop = this.labelTableTop;
        if (this.labelTable == null || labelTableTop == this.labelTable.length) {
            if (this.labelTable == null) {
                this.labelTable = new int[32];
            }
            else {
                final int[] labelTable = new int[this.labelTable.length * 2];
                System.arraycopy(this.labelTable, 0, labelTable, 0, labelTableTop);
                this.labelTable = labelTable;
            }
        }
        this.labelTableTop = labelTableTop + 1;
        this.labelTable[labelTableTop] = -1;
        node.labelId(labelTableTop);
        return labelTableTop;
    }
    
    private byte[] increaseICodeCapacity(final int n) {
        final int length = this.itsData.itsICode.length;
        final int iCodeTop = this.iCodeTop;
        if (iCodeTop + n <= length) {
            throw Kit.codeBug();
        }
        int n2;
        if (iCodeTop + n > (n2 = length * 2)) {
            n2 = iCodeTop + n;
        }
        final byte[] itsICode = new byte[n2];
        System.arraycopy(this.itsData.itsICode, 0, itsICode, 0, iCodeTop);
        return this.itsData.itsICode = itsICode;
    }
    
    private void markTargetLabel(final Node node) {
        final int targetLabel = this.getTargetLabel(node);
        if (this.labelTable[targetLabel] != -1) {
            Kit.codeBug();
        }
        this.labelTable[targetLabel] = this.iCodeTop;
    }
    
    private void releaseLocal(final int n) {
        --this.localTop;
        if (n != this.localTop) {
            Kit.codeBug();
        }
    }
    
    private void resolveForwardGoto(final int n) {
        if (this.iCodeTop < n + 3) {
            throw Kit.codeBug();
        }
        this.resolveGoto(n, this.iCodeTop);
    }
    
    private void resolveGoto(int n, final int n2) {
        final int n3 = n2 - n;
        if (n3 >= 0 && n3 <= 2) {
            throw Kit.codeBug();
        }
        final int n4 = n + 1;
        if ((n = n3) != (short)n3) {
            if (this.itsData.longJumps == null) {
                this.itsData.longJumps = new UintMap();
            }
            this.itsData.longJumps.put(n4, n2);
            n = 0;
        }
        final byte[] itsICode = this.itsData.itsICode;
        itsICode[n4] = (byte)(n >> 8);
        itsICode[n4 + 1] = (byte)n;
    }
    
    private void stackChange(int n) {
        if (n <= 0) {
            this.stackDepth += n;
            return;
        }
        n += this.stackDepth;
        if (n > this.itsData.itsMaxStack) {
            this.itsData.itsMaxStack = n;
        }
        this.stackDepth = n;
    }
    
    private void updateLineNumber(final Node node) {
        final int lineno = node.getLineno();
        if (lineno != this.lineNumber && lineno >= 0) {
            if (this.itsData.firstLinePC < 0) {
                this.itsData.firstLinePC = lineno;
            }
            this.lineNumber = lineno;
            this.addIcode(-26);
            this.addUint16(0xFFFF & lineno);
        }
    }
    
    private void visitArrayComprehension(final Node node, final Node node2, final Node node3) {
        this.visitStatement(node2, this.stackDepth);
        this.visitExpression(node3, 0);
    }
    
    private void visitExpression(Node node, int n) {
        final int type = node.getType();
        Node node2 = node.getFirstChild();
        final int stackDepth = this.stackDepth;
        final int n2 = 1;
        final int n3 = 1;
        Label_2106: {
            switch (type) {
                default: {
                    switch (type) {
                        default: {
                            switch (type) {
                                default: {
                                    Label_1217: {
                                        switch (type) {
                                            default: {
                                                int n4 = 7;
                                                switch (type) {
                                                    default: {
                                                        switch (type) {
                                                            default: {
                                                                switch (type) {
                                                                    default: {
                                                                        switch (type) {
                                                                            default: {
                                                                                throw this.badTree(node);
                                                                            }
                                                                            case 159: {
                                                                                node = node.getFirstChild();
                                                                                final Node next = node.getNext();
                                                                                this.visitExpression(node.getFirstChild(), 0);
                                                                                this.addToken(2);
                                                                                this.stackChange(-1);
                                                                                this.visitExpression(next.getFirstChild(), 0);
                                                                                this.addToken(3);
                                                                                break Label_2106;
                                                                            }
                                                                            case 146: {
                                                                                this.updateLineNumber(node);
                                                                                this.visitExpression(node2, 0);
                                                                                this.addIcode(-53);
                                                                                this.stackChange(-1);
                                                                                n = this.iCodeTop;
                                                                                this.visitExpression(node2.getNext(), 0);
                                                                                this.addBackwardGoto(-54, n);
                                                                                break Label_2106;
                                                                            }
                                                                            case 109: {
                                                                                n = node.getExistingIntProp(1);
                                                                                if (this.scriptOrFn.getFunctionNode(n).getFunctionType() != 2) {
                                                                                    throw Kit.codeBug();
                                                                                }
                                                                                this.addIndexOp(-19, n);
                                                                                this.stackChange(1);
                                                                                break Label_2106;
                                                                            }
                                                                            case 102: {
                                                                                node = node2.getNext();
                                                                                final Node next2 = node.getNext();
                                                                                this.visitExpression(node2, 0);
                                                                                final int iCodeTop = this.iCodeTop;
                                                                                this.addGotoOp(7);
                                                                                this.stackChange(-1);
                                                                                this.visitExpression(node, n & 0x1);
                                                                                final int iCodeTop2 = this.iCodeTop;
                                                                                this.addGotoOp(5);
                                                                                this.resolveForwardGoto(iCodeTop);
                                                                                this.stackDepth = stackDepth;
                                                                                this.visitExpression(next2, n & 0x1);
                                                                                this.resolveForwardGoto(iCodeTop2);
                                                                                break Label_2106;
                                                                            }
                                                                            case 89: {
                                                                                for (node = node.getLastChild(); node2 != node; node2 = node2.getNext()) {
                                                                                    this.visitExpression(node2, 0);
                                                                                    this.addIcode(-4);
                                                                                    this.stackChange(-1);
                                                                                }
                                                                                this.visitExpression(node2, n & 0x1);
                                                                                break Label_2106;
                                                                            }
                                                                            case 142: {
                                                                                break Label_1217;
                                                                            }
                                                                            case 126: {
                                                                                break Label_2106;
                                                                            }
                                                                        }
                                                                        break;
                                                                    }
                                                                    case 157: {
                                                                        this.visitArrayComprehension(node, node2, node2.getNext());
                                                                        break Label_2106;
                                                                    }
                                                                    case 156: {
                                                                        if (this.itsData.itsNeedsActivation) {
                                                                            Kit.codeBug();
                                                                        }
                                                                        n = this.scriptOrFn.getIndexForNameNode(node2);
                                                                        this.visitExpression(node2.getNext(), 0);
                                                                        this.addVarOp(156, n);
                                                                        break Label_2106;
                                                                    }
                                                                    case 155: {
                                                                        final String string = node2.getString();
                                                                        this.visitExpression(node2, 0);
                                                                        this.visitExpression(node2.getNext(), 0);
                                                                        this.addStringOp(-59, string);
                                                                        this.stackChange(-1);
                                                                        break Label_2106;
                                                                    }
                                                                }
                                                                break;
                                                            }
                                                            case 138: {
                                                                this.stackChange(1);
                                                                break Label_2106;
                                                            }
                                                            case 137: {
                                                                final int n5 = n = -1;
                                                                if (this.itsInFunctionFlag) {
                                                                    n = n5;
                                                                    if (!this.itsData.itsNeedsActivation) {
                                                                        n = this.scriptOrFn.getIndexForNameNode(node);
                                                                    }
                                                                }
                                                                if (n == -1) {
                                                                    this.addStringOp(-14, node.getString());
                                                                    this.stackChange(1);
                                                                }
                                                                else {
                                                                    this.addVarOp(55, n);
                                                                    this.stackChange(1);
                                                                    this.addToken(32);
                                                                }
                                                                break Label_2106;
                                                            }
                                                            case 140: {
                                                                break Label_2106;
                                                            }
                                                            case 139: {
                                                                break Label_2106;
                                                            }
                                                        }
                                                        break;
                                                    }
                                                    case 106:
                                                    case 107: {
                                                        this.visitIncDec(node, node2);
                                                        break Label_2106;
                                                    }
                                                    case 104:
                                                    case 105: {
                                                        this.visitExpression(node2, 0);
                                                        this.addIcode(-1);
                                                        this.stackChange(1);
                                                        final int iCodeTop3 = this.iCodeTop;
                                                        if (type != 105) {
                                                            n4 = 6;
                                                        }
                                                        this.addGotoOp(n4);
                                                        this.stackChange(-1);
                                                        this.addIcode(-4);
                                                        this.stackChange(-1);
                                                        this.visitExpression(node2.getNext(), n & 0x1);
                                                        this.resolveForwardGoto(iCodeTop3);
                                                        break Label_2106;
                                                    }
                                                }
                                                break;
                                            }
                                            case 77:
                                            case 78:
                                            case 79:
                                            case 80: {
                                                final int intProp = node.getIntProp(16, 0);
                                                node = node2;
                                                n = 0;
                                                Node next3;
                                                int n6;
                                                do {
                                                    this.visitExpression(node, 0);
                                                    n6 = n + 1;
                                                    next3 = node.getNext();
                                                    n = n6;
                                                } while ((node = next3) != null);
                                                this.addIndexOp(type, intProp);
                                                this.stackChange(1 - n6);
                                                break Label_2106;
                                            }
                                            case 74:
                                            case 75:
                                            case 76: {
                                                this.visitExpression(node2, 0);
                                                this.addToken(type);
                                                break Label_2106;
                                            }
                                            case 72: {
                                                if (node2 != null) {
                                                    this.visitExpression(node2, 0);
                                                }
                                                else {
                                                    this.addIcode(-50);
                                                    this.stackChange(1);
                                                }
                                                this.addToken(72);
                                                this.addUint16(node.getLineno() & 0xFFFF);
                                                break Label_2106;
                                            }
                                            case 71: {
                                                this.visitExpression(node2, 0);
                                                this.addStringOp(type, (String)node.getProp(17));
                                                break Label_2106;
                                            }
                                            case 68: {
                                                this.visitExpression(node2, 0);
                                                node = node2.getNext();
                                                if (type == 142) {
                                                    this.addIcode(-1);
                                                    this.stackChange(1);
                                                    this.addToken(67);
                                                    this.stackChange(-1);
                                                }
                                                this.visitExpression(node, 0);
                                                this.addToken(68);
                                                this.stackChange(-1);
                                                break Label_2106;
                                            }
                                            case 67:
                                            case 69: {
                                                this.visitExpression(node2, 0);
                                                this.addToken(type);
                                                break Label_2106;
                                            }
                                            case 65:
                                            case 66: {
                                                this.visitLiteral(node, node2);
                                                break Label_2106;
                                            }
                                            case 70: {
                                                break Label_2106;
                                            }
                                            case 73: {
                                                break Label_2106;
                                            }
                                        }
                                    }
                                    break;
                                }
                                case 61:
                                case 62: {
                                    this.addIndexOp(type, this.getLocalBlockRef(node));
                                    this.stackChange(1);
                                    break Label_2106;
                                }
                                case 63: {
                                    break Label_2106;
                                }
                            }
                            break;
                        }
                        case 56: {
                            if (this.itsData.itsNeedsActivation) {
                                Kit.codeBug();
                            }
                            n = this.scriptOrFn.getIndexForNameNode(node2);
                            this.visitExpression(node2.getNext(), 0);
                            this.addVarOp(56, n);
                            break Label_2106;
                        }
                        case 55: {
                            if (this.itsData.itsNeedsActivation) {
                                Kit.codeBug();
                            }
                            this.addVarOp(55, this.scriptOrFn.getIndexForNameNode(node));
                            this.stackChange(1);
                            break Label_2106;
                        }
                        case 54: {
                            this.addIndexOp(54, this.getLocalBlockRef(node));
                            this.stackChange(1);
                            break Label_2106;
                        }
                        case 52:
                        case 53: {
                            break Label_2106;
                        }
                    }
                    break;
                }
                case 48: {
                    this.addIndexOp(48, node.getExistingIntProp(4));
                    this.stackChange(1);
                    break;
                }
                case 42:
                case 43:
                case 44:
                case 45: {
                    this.addToken(type);
                    this.stackChange(1);
                    break;
                }
                case 40: {
                    final double double1 = node.getDouble();
                    n = (int)double1;
                    if (n == double1) {
                        if (n == 0) {
                            this.addIcode(-51);
                            if (1.0 / double1 < 0.0) {
                                this.addToken(29);
                            }
                        }
                        else if (n == 1) {
                            this.addIcode(-52);
                        }
                        else if ((short)n == n) {
                            this.addIcode(-27);
                            this.addUint16(0xFFFF & n);
                        }
                        else {
                            this.addIcode(-28);
                            this.addInt(n);
                        }
                    }
                    else {
                        this.addIndexOp(40, this.getDoubleIndex(double1));
                    }
                    this.stackChange(1);
                    break;
                }
                case 39:
                case 41:
                case 49: {
                    this.addStringOp(type, node.getString());
                    this.stackChange(1);
                    break;
                }
                case 37: {
                    this.visitExpression(node2, 0);
                    node = node2.getNext();
                    this.visitExpression(node, 0);
                    node = node.getNext();
                    if (type == 140) {
                        this.addIcode(-2);
                        this.stackChange(2);
                        this.addToken(36);
                        this.stackChange(-1);
                        this.stackChange(-1);
                    }
                    this.visitExpression(node, 0);
                    this.addToken(37);
                    this.stackChange(-2);
                    break;
                }
                case 35: {
                    this.visitExpression(node2, 0);
                    final Node next4 = node2.getNext();
                    final String string2 = next4.getString();
                    final Node next5 = next4.getNext();
                    if (type == 139) {
                        this.addIcode(-1);
                        this.stackChange(1);
                        this.addStringOp(33, string2);
                        this.stackChange(-1);
                    }
                    this.visitExpression(next5, 0);
                    this.addStringOp(35, string2);
                    this.stackChange(-1);
                    break;
                }
                case 33:
                case 34: {
                    this.visitExpression(node2, 0);
                    this.addStringOp(type, node2.getNext().getString());
                    break;
                }
                case 31: {
                    if (node2.getType() == 49) {
                        n = n3;
                    }
                    else {
                        n = 0;
                    }
                    this.visitExpression(node2, 0);
                    this.visitExpression(node2.getNext(), 0);
                    if (n != 0) {
                        this.addIcode(0);
                    }
                    else {
                        this.addToken(31);
                    }
                    this.stackChange(-1);
                    break;
                }
                case 30:
                case 38: {
                    if (type == 30) {
                        this.visitExpression(node2, 0);
                    }
                    else {
                        this.generateCallFunAndThis(node2);
                    }
                    int itsMaxCalleeArgs = 0;
                    while ((node2 = node2.getNext()) != null) {
                        this.visitExpression(node2, 0);
                        ++itsMaxCalleeArgs;
                    }
                    final int intProp2 = node.getIntProp(10, 0);
                    int n7;
                    if (type != 70 && intProp2 != 0) {
                        this.addIndexOp(-21, itsMaxCalleeArgs);
                        this.addUint8(intProp2);
                        if (type == 30) {
                            n = n2;
                        }
                        else {
                            n = 0;
                        }
                        this.addUint8(n);
                        this.addUint16(0xFFFF & this.lineNumber);
                        n7 = type;
                    }
                    else {
                        if ((n7 = type) == 38) {
                            n7 = type;
                            if ((n & 0x1) != 0x0) {
                                n7 = type;
                                if (!this.compilerEnv.isGenerateDebugInfo()) {
                                    n7 = type;
                                    if (!this.itsInTryFlag) {
                                        n7 = -55;
                                    }
                                }
                            }
                        }
                        this.addIndexOp(n7, itsMaxCalleeArgs);
                    }
                    if (n7 == 30) {
                        this.stackChange(-itsMaxCalleeArgs);
                    }
                    else {
                        this.stackChange(-1 - itsMaxCalleeArgs);
                    }
                    if (itsMaxCalleeArgs > this.itsData.itsMaxCalleeArgs) {
                        this.itsData.itsMaxCalleeArgs = itsMaxCalleeArgs;
                    }
                    break;
                }
                case 26:
                case 27:
                case 28:
                case 29:
                case 32: {
                    this.visitExpression(node2, 0);
                    if (type == 126) {
                        this.addIcode(-4);
                        this.addIcode(-50);
                        break;
                    }
                    this.addToken(type);
                    break;
                }
                case 9:
                case 10:
                case 11:
                case 12:
                case 13:
                case 14:
                case 15:
                case 16:
                case 17:
                case 18:
                case 19:
                case 20:
                case 21:
                case 22:
                case 23:
                case 24:
                case 25:
                case 36:
                case 46:
                case 47: {
                    this.visitExpression(node2, 0);
                    this.visitExpression(node2.getNext(), 0);
                    this.addToken(type);
                    this.stackChange(-1);
                    break;
                }
                case 8: {
                    final String string3 = node2.getString();
                    this.visitExpression(node2, 0);
                    this.visitExpression(node2.getNext(), 0);
                    this.addStringOp(type, string3);
                    this.stackChange(-1);
                    break;
                }
            }
        }
        if (stackDepth + 1 != this.stackDepth) {
            Kit.codeBug();
        }
    }
    
    private void visitIncDec(Node node, final Node node2) {
        final int existingIntProp = node.getExistingIntProp(13);
        final int type = node2.getType();
        if (type == 33) {
            node = node2.getFirstChild();
            this.visitExpression(node, 0);
            this.addStringOp(-9, node.getNext().getString());
            this.addUint8(existingIntProp);
            return;
        }
        if (type == 36) {
            node = node2.getFirstChild();
            this.visitExpression(node, 0);
            this.visitExpression(node.getNext(), 0);
            this.addIcode(-10);
            this.addUint8(existingIntProp);
            this.stackChange(-1);
            return;
        }
        if (type == 39) {
            this.addStringOp(-8, node2.getString());
            this.addUint8(existingIntProp);
            this.stackChange(1);
            return;
        }
        if (type == 55) {
            if (this.itsData.itsNeedsActivation) {
                Kit.codeBug();
            }
            this.addVarOp(-7, this.scriptOrFn.getIndexForNameNode(node2));
            this.addUint8(existingIntProp);
            this.stackChange(1);
            return;
        }
        if (type != 67) {
            throw this.badTree(node);
        }
        this.visitExpression(node2.getFirstChild(), 0);
        this.addIcode(-11);
        this.addUint8(existingIntProp);
    }
    
    private void visitLiteral(final Node node, Node next) {
        final int type = node.getType();
        final Object o = null;
        Object o2;
        int length;
        if (type == 65) {
            int n = 0;
            Node next2 = next;
            while (true) {
                o2 = o;
                length = n;
                if (next2 == null) {
                    break;
                }
                ++n;
                next2 = next2.getNext();
            }
        }
        else {
            if (type != 66) {
                throw this.badTree(node);
            }
            o2 = node.getProp(12);
            length = o2.length;
        }
        this.addIndexOp(-29, length);
        this.stackChange(2);
        while (next != null) {
            final int type2 = next.getType();
            if (type2 == 151) {
                this.visitExpression(next.getFirstChild(), 0);
                this.addIcode(-57);
            }
            else if (type2 == 152) {
                this.visitExpression(next.getFirstChild(), 0);
                this.addIcode(-58);
            }
            else if (type2 == 163) {
                this.visitExpression(next.getFirstChild(), 0);
                this.addIcode(-30);
            }
            else {
                this.visitExpression(next, 0);
                this.addIcode(-30);
            }
            this.stackChange(-1);
            next = next.getNext();
        }
        if (type == 65) {
            final int[] array = (int[])node.getProp(11);
            if (array == null) {
                this.addToken(65);
            }
            else {
                final int size = this.literalIds.size();
                this.literalIds.add(array);
                this.addIndexOp(-31, size);
            }
        }
        else {
            final int size2 = this.literalIds.size();
            this.literalIds.add(o2);
            this.addIndexOp(66, size2);
        }
        this.stackChange(-1);
    }
    
    private void visitStatement(Node node, final int n) {
        final int type = node.getType();
        Node node2 = node.getFirstChild();
        int n2 = 1;
        Label_1141: {
            switch (type) {
                default: {
                    switch (type) {
                        default: {
                            switch (type) {
                                default: {
                                    int n3 = -4;
                                    Node next = node2;
                                    Label_0817: {
                                        switch (type) {
                                            default: {
                                                switch (type) {
                                                    default: {
                                                        throw this.badTree(node);
                                                    }
                                                    case 160: {
                                                        this.addIcode(-64);
                                                        break Label_1141;
                                                    }
                                                    case 141: {
                                                        final int allocLocal = this.allocLocal();
                                                        node.putIntProp(2, allocLocal);
                                                        this.updateLineNumber(node);
                                                        while (node2 != null) {
                                                            this.visitStatement(node2, n);
                                                            node2 = node2.getNext();
                                                        }
                                                        this.addIndexOp(-56, allocLocal);
                                                        this.releaseLocal(allocLocal);
                                                        break Label_1141;
                                                    }
                                                    case 125: {
                                                        this.stackChange(1);
                                                        final int localBlockRef = this.getLocalBlockRef(node);
                                                        this.addIndexOp(-24, localBlockRef);
                                                        this.stackChange(-1);
                                                        while (node2 != null) {
                                                            this.visitStatement(node2, n);
                                                            node2 = node2.getNext();
                                                        }
                                                        this.addIndexOp(-25, localBlockRef);
                                                        break Label_1141;
                                                    }
                                                    case 114: {
                                                        this.updateLineNumber(node);
                                                        this.visitExpression(node2, 0);
                                                        for (Jump jump = (Jump)node2.getNext(); jump != null; jump = (Jump)jump.getNext()) {
                                                            if (jump.getType() != 115) {
                                                                throw this.badTree(jump);
                                                            }
                                                            final Node firstChild = jump.getFirstChild();
                                                            this.addIcode(-1);
                                                            this.stackChange(1);
                                                            this.visitExpression(firstChild, 0);
                                                            this.addToken(46);
                                                            this.stackChange(-1);
                                                            this.addGoto(jump.target, -6);
                                                            this.stackChange(-1);
                                                        }
                                                        this.addIcode(-4);
                                                        this.stackChange(-1);
                                                        break Label_1141;
                                                    }
                                                    case 109: {
                                                        final int existingIntProp = node.getExistingIntProp(1);
                                                        final int functionType = this.scriptOrFn.getFunctionNode(existingIntProp).getFunctionType();
                                                        if (functionType == 3) {
                                                            this.addIndexOp(-20, existingIntProp);
                                                        }
                                                        else if (functionType != 1) {
                                                            throw Kit.codeBug();
                                                        }
                                                        if (!this.itsInFunctionFlag) {
                                                            this.addIndexOp(-19, existingIntProp);
                                                            this.stackChange(1);
                                                            this.addIcode(-5);
                                                            this.stackChange(-1);
                                                        }
                                                        break Label_1141;
                                                    }
                                                    case 81: {
                                                        final Jump jump2 = (Jump)node;
                                                        final int localBlockRef2 = this.getLocalBlockRef(jump2);
                                                        final int allocLocal2 = this.allocLocal();
                                                        this.addIndexOp(-13, allocLocal2);
                                                        final int iCodeTop = this.iCodeTop;
                                                        final boolean itsInTryFlag = this.itsInTryFlag;
                                                        this.itsInTryFlag = true;
                                                        while (node2 != null) {
                                                            this.visitStatement(node2, n);
                                                            node2 = node2.getNext();
                                                        }
                                                        this.itsInTryFlag = itsInTryFlag;
                                                        final Node target = jump2.target;
                                                        if (target != null) {
                                                            final int n4 = this.labelTable[this.getTargetLabel(target)];
                                                            this.addExceptionHandler(iCodeTop, n4, n4, false, localBlockRef2, allocLocal2);
                                                        }
                                                        node = jump2.getFinally();
                                                        if (node != null) {
                                                            final int n5 = this.labelTable[this.getTargetLabel(node)];
                                                            this.addExceptionHandler(iCodeTop, n5, n5, true, localBlockRef2, allocLocal2);
                                                        }
                                                        this.addIndexOp(-56, allocLocal2);
                                                        this.releaseLocal(allocLocal2);
                                                        break Label_1141;
                                                    }
                                                    case 64: {
                                                        this.updateLineNumber(node);
                                                        this.addToken(64);
                                                        break Label_1141;
                                                    }
                                                    case -62: {
                                                        break Label_1141;
                                                    }
                                                    case 123: {
                                                        break Label_0817;
                                                    }
                                                }
                                                break;
                                            }
                                            case 135: {
                                                this.addGoto(((Jump)node).target, -23);
                                                break Label_1141;
                                            }
                                            case 133:
                                            case 134: {
                                                this.updateLineNumber(node);
                                                this.visitExpression(node2, 0);
                                                if (type != 133) {
                                                    n3 = -5;
                                                }
                                                this.addIcode(n3);
                                                this.stackChange(-1);
                                                break Label_1141;
                                            }
                                            case 131: {
                                                this.markTargetLabel(node);
                                                break Label_1141;
                                            }
                                            case 128:
                                            case 129:
                                            case 130:
                                            case 132: {
                                                this.updateLineNumber(node);
                                                next = node2;
                                            }
                                            case 136: {
                                                while (next != null) {
                                                    this.visitStatement(next, n);
                                                    next = next.getNext();
                                                }
                                                break Label_1141;
                                            }
                                        }
                                    }
                                    break;
                                }
                                case 58:
                                case 59:
                                case 60: {
                                    this.visitExpression(node2, 0);
                                    this.addIndexOp(type, this.getLocalBlockRef(node));
                                    this.stackChange(-1);
                                    break Label_1141;
                                }
                                case 57: {
                                    final int localBlockRef3 = this.getLocalBlockRef(node);
                                    final int existingIntProp2 = node.getExistingIntProp(14);
                                    final String string = node2.getString();
                                    this.visitExpression(node2.getNext(), 0);
                                    this.addStringPrefix(string);
                                    this.addIndexPrefix(localBlockRef3);
                                    this.addToken(57);
                                    if (existingIntProp2 == 0) {
                                        n2 = 0;
                                    }
                                    this.addUint8(n2);
                                    this.stackChange(-1);
                                    break Label_1141;
                                }
                            }
                            break;
                        }
                        case 51: {
                            this.updateLineNumber(node);
                            this.addIndexOp(51, this.getLocalBlockRef(node));
                            break Label_1141;
                        }
                        case 50: {
                            this.updateLineNumber(node);
                            this.visitExpression(node2, 0);
                            this.addToken(50);
                            this.addUint16(0xFFFF & this.lineNumber);
                            this.stackChange(-1);
                            break Label_1141;
                        }
                    }
                    break;
                }
                case 6:
                case 7: {
                    node = ((Jump)node).target;
                    this.visitExpression(node2, 0);
                    this.addGoto(node, type);
                    this.stackChange(-1);
                    break;
                }
                case 5: {
                    this.addGoto(((Jump)node).target, type);
                    break;
                }
                case 4: {
                    this.updateLineNumber(node);
                    if (node.getIntProp(20, 0) != 0) {
                        this.addIcode(-63);
                        this.addUint16(0xFFFF & this.lineNumber);
                        break;
                    }
                    if (node2 != null) {
                        this.visitExpression(node2, 1);
                        this.addToken(4);
                        this.stackChange(-1);
                        break;
                    }
                    this.addIcode(-22);
                    break;
                }
                case 3: {
                    this.addToken(3);
                    break;
                }
                case 2: {
                    this.visitExpression(node2, 0);
                    this.addToken(2);
                    this.stackChange(-1);
                    break;
                }
            }
        }
        if (this.stackDepth != n) {
            throw Kit.codeBug();
        }
    }
    
    public InterpreterData compile(final CompilerEnvirons compilerEnv, final ScriptNode scriptOrFn, final String s, final boolean b) {
        this.compilerEnv = compilerEnv;
        new NodeTransformer().transform(scriptOrFn);
        if (b) {
            this.scriptOrFn = scriptOrFn.getFunctionNode(0);
        }
        else {
            this.scriptOrFn = scriptOrFn;
        }
        this.itsData = new InterpreterData(compilerEnv.getLanguageVersion(), this.scriptOrFn.getSourceName(), s, ((AstRoot)scriptOrFn).isInStrictMode());
        this.itsData.topLevel = true;
        if (b) {
            this.generateFunctionICode();
        }
        else {
            this.generateICodeFromTree(this.scriptOrFn);
        }
        return this.itsData;
    }
}
