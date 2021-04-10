package com.android.dx.rop.code;

import com.android.dx.rop.type.*;
import com.android.dx.util.*;

public final class Rop
{
    public static final int BRANCH_GOTO = 3;
    public static final int BRANCH_IF = 4;
    public static final int BRANCH_MAX = 6;
    public static final int BRANCH_MIN = 1;
    public static final int BRANCH_NONE = 1;
    public static final int BRANCH_RETURN = 2;
    public static final int BRANCH_SWITCH = 5;
    public static final int BRANCH_THROW = 6;
    private final int branchingness;
    private final TypeList exceptions;
    private final boolean isCallLike;
    private final String nickname;
    private final int opcode;
    private final Type result;
    private final TypeList sources;
    
    public Rop(final int n, final Type type, final TypeList list, final int n2, final String s) {
        this(n, type, list, StdTypeList.EMPTY, n2, false, s);
    }
    
    public Rop(final int n, final Type type, final TypeList list, final TypeList list2, final int n2, final String s) {
        this(n, type, list, list2, n2, false, s);
    }
    
    public Rop(final int opcode, final Type result, final TypeList sources, final TypeList exceptions, final int branchingness, final boolean isCallLike, final String nickname) {
        if (result == null) {
            throw new NullPointerException("result == null");
        }
        if (sources == null) {
            throw new NullPointerException("sources == null");
        }
        if (exceptions == null) {
            throw new NullPointerException("exceptions == null");
        }
        if (branchingness < 1 || branchingness > 6) {
            throw new IllegalArgumentException("bogus branchingness");
        }
        if (exceptions.size() != 0 && branchingness != 6) {
            throw new IllegalArgumentException("exceptions / branchingness mismatch");
        }
        this.opcode = opcode;
        this.result = result;
        this.sources = sources;
        this.exceptions = exceptions;
        this.branchingness = branchingness;
        this.isCallLike = isCallLike;
        this.nickname = nickname;
    }
    
    public Rop(final int n, final Type type, final TypeList list, final TypeList list2, final String s) {
        this(n, type, list, list2, 6, false, s);
    }
    
    public Rop(final int n, final Type type, final TypeList list, final String s) {
        this(n, type, list, StdTypeList.EMPTY, 1, false, s);
    }
    
    public Rop(final int n, final TypeList list, final TypeList list2) {
        this(n, Type.VOID, list, list2, 6, true, null);
    }
    
    public final boolean canThrow() {
        return this.exceptions.size() != 0;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Rop)) {
            return false;
        }
        final Rop rop = (Rop)o;
        return this.opcode == rop.opcode && this.branchingness == rop.branchingness && this.result == rop.result && this.sources.equals(rop.sources) && this.exceptions.equals(rop.exceptions);
    }
    
    public int getBranchingness() {
        return this.branchingness;
    }
    
    public TypeList getExceptions() {
        return this.exceptions;
    }
    
    public String getNickname() {
        if (this.nickname != null) {
            return this.nickname;
        }
        return this.toString();
    }
    
    public int getOpcode() {
        return this.opcode;
    }
    
    public Type getResult() {
        return this.result;
    }
    
    public TypeList getSources() {
        return this.sources;
    }
    
    @Override
    public int hashCode() {
        return (((this.opcode * 31 + this.branchingness) * 31 + this.result.hashCode()) * 31 + this.sources.hashCode()) * 31 + this.exceptions.hashCode();
    }
    
    public boolean isCallLike() {
        return this.isCallLike;
    }
    
    public boolean isCommutative() {
        final int opcode = this.opcode;
        if (opcode != 14 && opcode != 16) {
            switch (opcode) {
                default: {
                    return false;
                }
                case 20:
                case 21:
                case 22: {
                    break;
                }
            }
        }
        return true;
    }
    
    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer(40);
        sb.append("Rop{");
        sb.append(RegOps.opName(this.opcode));
        if (this.result != Type.VOID) {
            sb.append(" ");
            sb.append(this.result);
        }
        else {
            sb.append(" .");
        }
        sb.append(" <-");
        final int size = this.sources.size();
        final int n = 0;
        if (size == 0) {
            sb.append(" .");
        }
        else {
            for (int i = 0; i < size; ++i) {
                sb.append(' ');
                sb.append(this.sources.getType(i));
            }
        }
        if (this.isCallLike) {
            sb.append(" call");
        }
        final int size2 = this.exceptions.size();
        if (size2 != 0) {
            sb.append(" throws");
            for (int j = n; j < size2; ++j) {
                sb.append(' ');
                if (this.exceptions.getType(j) == Type.THROWABLE) {
                    sb.append("<any>");
                }
                else {
                    sb.append(this.exceptions.getType(j));
                }
            }
        }
        else {
            switch (this.branchingness) {
                default: {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append(" ");
                    sb2.append(Hex.u1(this.branchingness));
                    sb.append(sb2.toString());
                    break;
                }
                case 5: {
                    sb.append(" switches");
                    break;
                }
                case 4: {
                    sb.append(" ifs");
                    break;
                }
                case 3: {
                    sb.append(" gotos");
                    break;
                }
                case 2: {
                    sb.append(" returns");
                    break;
                }
                case 1: {
                    sb.append(" flows");
                    break;
                }
            }
        }
        sb.append('}');
        return sb.toString();
    }
}
