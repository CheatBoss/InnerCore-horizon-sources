package com.android.dx.command.dump;

import com.android.dx.cf.direct.*;
import com.android.dx.cf.iface.*;
import com.android.dx.cf.code.*;
import com.android.dx.ssa.*;
import java.io.*;
import com.android.dx.rop.code.*;
import com.android.dx.util.*;

public class DotDumper implements ParseObserver
{
    private final Args args;
    private final byte[] bytes;
    private DirectClassFile classFile;
    private final String filePath;
    private final boolean optimize;
    private final boolean strictParse;
    
    DotDumper(final byte[] bytes, final String filePath, final Args args) {
        this.bytes = bytes;
        this.filePath = filePath;
        this.strictParse = args.strictParse;
        this.optimize = args.optimize;
        this.args = args;
    }
    
    static void dump(final byte[] array, final String s, final Args args) {
        new DotDumper(array, s, args).run();
    }
    
    private void run() {
        final ByteArray byteArray = new ByteArray(this.bytes);
        (this.classFile = new DirectClassFile(byteArray, this.filePath, this.strictParse)).setAttributeFactory(StdAttributeFactory.THE_ONE);
        this.classFile.getMagic();
        final DirectClassFile directClassFile = new DirectClassFile(byteArray, this.filePath, this.strictParse);
        directClassFile.setAttributeFactory(StdAttributeFactory.THE_ONE);
        directClassFile.setObserver(this);
        directClassFile.getMagic();
    }
    
    @Override
    public void changeIndent(final int n) {
    }
    
    @Override
    public void endParsingMember(final ByteArray byteArray, int i, final String s, final String s2, final Member member) {
        if (!(member instanceof Method)) {
            return;
        }
        if (!this.shouldDumpMethod(s)) {
            return;
        }
        final ConcreteMethod concreteMethod = new ConcreteMethod((Method)member, this.classFile, true, true);
        final DexTranslationAdvice the_ONE = DexTranslationAdvice.THE_ONE;
        RopMethod ropMethod2;
        final RopMethod ropMethod = ropMethod2 = Ropper.convert(concreteMethod, the_ONE, this.classFile.getMethods());
        if (this.optimize) {
            final boolean static1 = AccessFlags.isStatic(concreteMethod.getAccessFlags());
            ropMethod2 = Optimizer.optimize(ropMethod, BaseDumper.computeParamWidth(concreteMethod, static1), static1, true, the_ONE);
        }
        final PrintStream out = System.out;
        final StringBuilder sb = new StringBuilder();
        sb.append("digraph ");
        sb.append(s);
        sb.append("{");
        out.println(sb.toString());
        final PrintStream out2 = System.out;
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("\tfirst -> n");
        sb2.append(Hex.u2(ropMethod2.getFirstLabel()));
        sb2.append(";");
        out2.println(sb2.toString());
        final BasicBlockList blocks = ropMethod2.getBlocks();
        int size;
        BasicBlock value;
        int label;
        IntList successors;
        PrintStream out3;
        StringBuilder sb3;
        PrintStream out4;
        StringBuilder sb4;
        PrintStream out5;
        StringBuilder sb5;
        int j;
        int value2;
        PrintStream out6;
        StringBuilder sb6;
        PrintStream out7;
        StringBuilder sb7;
        for (size = blocks.size(), i = 0; i < size; ++i) {
            value = blocks.get(i);
            label = value.getLabel();
            successors = value.getSuccessors();
            if (successors.size() == 0) {
                out3 = System.out;
                sb3 = new StringBuilder();
                sb3.append("\tn");
                sb3.append(Hex.u2(label));
                sb3.append(" -> returns;");
                out3.println(sb3.toString());
            }
            else if (successors.size() == 1) {
                out4 = System.out;
                sb4 = new StringBuilder();
                sb4.append("\tn");
                sb4.append(Hex.u2(label));
                sb4.append(" -> n");
                sb4.append(Hex.u2(successors.get(0)));
                sb4.append(";");
                out4.println(sb4.toString());
            }
            else {
                out5 = System.out;
                sb5 = new StringBuilder();
                sb5.append("\tn");
                sb5.append(Hex.u2(label));
                sb5.append(" -> {");
                out5.print(sb5.toString());
                for (j = 0; j < successors.size(); ++j) {
                    value2 = successors.get(j);
                    if (value2 != value.getPrimarySuccessor()) {
                        out6 = System.out;
                        sb6 = new StringBuilder();
                        sb6.append(" n");
                        sb6.append(Hex.u2(value2));
                        sb6.append(" ");
                        out6.print(sb6.toString());
                    }
                }
                System.out.println("};");
                out7 = System.out;
                sb7 = new StringBuilder();
                sb7.append("\tn");
                sb7.append(Hex.u2(label));
                sb7.append(" -> n");
                sb7.append(Hex.u2(value.getPrimarySuccessor()));
                sb7.append(" [label=\"primary\"];");
                out7.println(sb7.toString());
            }
        }
        System.out.println("}");
    }
    
    @Override
    public void parsed(final ByteArray byteArray, final int n, final int n2, final String s) {
    }
    
    protected boolean shouldDumpMethod(final String s) {
        return this.args.method == null || this.args.method.equals(s);
    }
    
    @Override
    public void startParsingMember(final ByteArray byteArray, final int n, final String s, final String s2) {
    }
}
