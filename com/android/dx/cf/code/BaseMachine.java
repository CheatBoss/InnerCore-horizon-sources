package com.android.dx.cf.code;

import com.android.dx.rop.cst.*;
import java.util.*;
import com.android.dx.rop.code.*;
import com.android.dx.rop.type.*;

public abstract class BaseMachine implements Machine
{
    private int argCount;
    private TypeBearer[] args;
    private SwitchList auxCases;
    private Constant auxCst;
    private ArrayList<Constant> auxInitValues;
    private int auxInt;
    private int auxTarget;
    private Type auxType;
    private int localIndex;
    private boolean localInfo;
    private RegisterSpec localTarget;
    private final Prototype prototype;
    private int resultCount;
    private TypeBearer[] results;
    
    public BaseMachine(final Prototype prototype) {
        if (prototype == null) {
            throw new NullPointerException("prototype == null");
        }
        this.prototype = prototype;
        this.args = new TypeBearer[10];
        this.results = new TypeBearer[6];
        this.clearArgs();
    }
    
    public static void throwLocalMismatch(final TypeBearer typeBearer, final TypeBearer typeBearer2) {
        final StringBuilder sb = new StringBuilder();
        sb.append("local variable type mismatch: attempt to set or access a value of type ");
        sb.append(typeBearer.toHuman());
        sb.append(" using a local variable of type ");
        sb.append(typeBearer2.toHuman());
        sb.append(". This is symptomatic of .class transformation tools ");
        sb.append("that ignore local variable information.");
        throw new SimException(sb.toString());
    }
    
    protected final void addResult(final TypeBearer typeBearer) {
        if (typeBearer == null) {
            throw new NullPointerException("result == null");
        }
        this.results[this.resultCount] = typeBearer;
        ++this.resultCount;
    }
    
    protected final TypeBearer arg(final int n) {
        if (n >= this.argCount) {
            throw new IllegalArgumentException("n >= argCount");
        }
        try {
            return this.args[n];
        }
        catch (ArrayIndexOutOfBoundsException ex) {
            throw new IllegalArgumentException("n < 0");
        }
    }
    
    protected final int argCount() {
        return this.argCount;
    }
    
    protected final int argWidth() {
        int n = 0;
        for (int i = 0; i < this.argCount; ++i) {
            n += this.args[i].getType().getCategory();
        }
        return n;
    }
    
    @Override
    public final void auxCstArg(final Constant auxCst) {
        if (auxCst == null) {
            throw new NullPointerException("cst == null");
        }
        this.auxCst = auxCst;
    }
    
    @Override
    public final void auxInitValues(final ArrayList<Constant> auxInitValues) {
        this.auxInitValues = auxInitValues;
    }
    
    @Override
    public final void auxIntArg(final int auxInt) {
        this.auxInt = auxInt;
    }
    
    @Override
    public final void auxSwitchArg(final SwitchList auxCases) {
        if (auxCases == null) {
            throw new NullPointerException("cases == null");
        }
        this.auxCases = auxCases;
    }
    
    @Override
    public final void auxTargetArg(final int auxTarget) {
        this.auxTarget = auxTarget;
    }
    
    @Override
    public final void auxType(final Type auxType) {
        this.auxType = auxType;
    }
    
    @Override
    public final void clearArgs() {
        this.argCount = 0;
        this.auxType = null;
        this.auxInt = 0;
        this.auxCst = null;
        this.auxTarget = 0;
        this.auxCases = null;
        this.auxInitValues = null;
        this.localIndex = -1;
        this.localInfo = false;
        this.localTarget = null;
        this.resultCount = -1;
    }
    
    protected final void clearResult() {
        this.resultCount = 0;
    }
    
    protected final SwitchList getAuxCases() {
        return this.auxCases;
    }
    
    protected final Constant getAuxCst() {
        return this.auxCst;
    }
    
    protected final int getAuxInt() {
        return this.auxInt;
    }
    
    protected final int getAuxTarget() {
        return this.auxTarget;
    }
    
    protected final Type getAuxType() {
        return this.auxType;
    }
    
    protected final ArrayList<Constant> getInitValues() {
        return this.auxInitValues;
    }
    
    protected final int getLocalIndex() {
        return this.localIndex;
    }
    
    protected final boolean getLocalInfo() {
        return this.localInfo;
    }
    
    protected final RegisterSpec getLocalTarget(final boolean b) {
        if (this.localTarget == null) {
            return null;
        }
        if (this.resultCount != 1) {
            final StringBuilder sb = new StringBuilder();
            sb.append("local target with ");
            String s;
            if (this.resultCount == 0) {
                s = "no";
            }
            else {
                s = "multiple";
            }
            sb.append(s);
            sb.append(" results");
            throw new SimException(sb.toString());
        }
        final TypeBearer typeBearer = this.results[0];
        final Type type = typeBearer.getType();
        final Type type2 = this.localTarget.getType();
        if (type == type2) {
            if (b) {
                return this.localTarget.withType(typeBearer);
            }
            return this.localTarget;
        }
        else {
            if (!Merger.isPossiblyAssignableFrom(type2, type)) {
                throwLocalMismatch(type, type2);
                return null;
            }
            if (type2 == Type.OBJECT) {
                this.localTarget = this.localTarget.withType(typeBearer);
            }
            return this.localTarget;
        }
    }
    
    @Override
    public Prototype getPrototype() {
        return this.prototype;
    }
    
    @Override
    public final void localArg(final Frame frame, final int localIndex) {
        this.clearArgs();
        this.args[0] = frame.getLocals().get(localIndex);
        this.argCount = 1;
        this.localIndex = localIndex;
    }
    
    @Override
    public final void localInfo(final boolean localInfo) {
        this.localInfo = localInfo;
    }
    
    @Override
    public final void localTarget(final int n, final Type type, final LocalItem localItem) {
        this.localTarget = RegisterSpec.makeLocalOptional(n, type, localItem);
    }
    
    @Override
    public final void popArgs(final Frame frame, final int argCount) {
        final ExecutionStack stack = frame.getStack();
        this.clearArgs();
        if (argCount > this.args.length) {
            this.args = new TypeBearer[argCount + 10];
        }
        for (int i = argCount - 1; i >= 0; --i) {
            this.args[i] = stack.pop();
        }
        this.argCount = argCount;
    }
    
    @Override
    public void popArgs(final Frame frame, final Prototype prototype) {
        final StdTypeList parameterTypes = prototype.getParameterTypes();
        final int size = parameterTypes.size();
        this.popArgs(frame, size);
        for (int i = 0; i < size; ++i) {
            if (!Merger.isPossiblyAssignableFrom(parameterTypes.getType(i), this.args[i])) {
                final StringBuilder sb = new StringBuilder();
                sb.append("at stack depth ");
                sb.append(size - 1 - i);
                sb.append(", expected type ");
                sb.append(parameterTypes.getType(i).toHuman());
                sb.append(" but found ");
                sb.append(this.args[i].getType().toHuman());
                throw new SimException(sb.toString());
            }
        }
    }
    
    @Override
    public final void popArgs(final Frame frame, final Type type) {
        this.popArgs(frame, 1);
        if (!Merger.isPossiblyAssignableFrom(type, this.args[0])) {
            final StringBuilder sb = new StringBuilder();
            sb.append("expected type ");
            sb.append(type.toHuman());
            sb.append(" but found ");
            sb.append(this.args[0].getType().toHuman());
            throw new SimException(sb.toString());
        }
    }
    
    @Override
    public final void popArgs(final Frame frame, final Type type, final Type type2) {
        this.popArgs(frame, 2);
        if (!Merger.isPossiblyAssignableFrom(type, this.args[0])) {
            final StringBuilder sb = new StringBuilder();
            sb.append("expected type ");
            sb.append(type.toHuman());
            sb.append(" but found ");
            sb.append(this.args[0].getType().toHuman());
            throw new SimException(sb.toString());
        }
        if (!Merger.isPossiblyAssignableFrom(type2, this.args[1])) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("expected type ");
            sb2.append(type2.toHuman());
            sb2.append(" but found ");
            sb2.append(this.args[1].getType().toHuman());
            throw new SimException(sb2.toString());
        }
    }
    
    @Override
    public final void popArgs(final Frame frame, final Type type, final Type type2, final Type type3) {
        this.popArgs(frame, 3);
        if (!Merger.isPossiblyAssignableFrom(type, this.args[0])) {
            final StringBuilder sb = new StringBuilder();
            sb.append("expected type ");
            sb.append(type.toHuman());
            sb.append(" but found ");
            sb.append(this.args[0].getType().toHuman());
            throw new SimException(sb.toString());
        }
        if (!Merger.isPossiblyAssignableFrom(type2, this.args[1])) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("expected type ");
            sb2.append(type2.toHuman());
            sb2.append(" but found ");
            sb2.append(this.args[1].getType().toHuman());
            throw new SimException(sb2.toString());
        }
        if (!Merger.isPossiblyAssignableFrom(type3, this.args[2])) {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("expected type ");
            sb3.append(type3.toHuman());
            sb3.append(" but found ");
            sb3.append(this.args[2].getType().toHuman());
            throw new SimException(sb3.toString());
        }
    }
    
    protected final TypeBearer result(final int n) {
        if (n >= this.resultCount) {
            throw new IllegalArgumentException("n >= resultCount");
        }
        try {
            return this.results[n];
        }
        catch (ArrayIndexOutOfBoundsException ex) {
            throw new IllegalArgumentException("n < 0");
        }
    }
    
    protected final int resultCount() {
        if (this.resultCount < 0) {
            throw new SimException("results never set");
        }
        return this.resultCount;
    }
    
    protected final int resultWidth() {
        int n = 0;
        for (int i = 0; i < this.resultCount; ++i) {
            n += this.results[i].getType().getCategory();
        }
        return n;
    }
    
    protected final void setResult(final TypeBearer typeBearer) {
        if (typeBearer == null) {
            throw new NullPointerException("result == null");
        }
        this.results[0] = typeBearer;
        this.resultCount = 1;
    }
    
    protected final void storeResults(final Frame frame) {
        if (this.resultCount < 0) {
            throw new SimException("results never set");
        }
        if (this.resultCount == 0) {
            return;
        }
        final RegisterSpec localTarget = this.localTarget;
        int i = 0;
        if (localTarget != null) {
            frame.getLocals().set(this.getLocalTarget(false));
            return;
        }
        final ExecutionStack stack = frame.getStack();
        while (i < this.resultCount) {
            if (this.localInfo) {
                stack.setLocal();
            }
            stack.push(this.results[i]);
            ++i;
        }
    }
}
