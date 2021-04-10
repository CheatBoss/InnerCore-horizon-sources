package com.android.dx.rop.code;

import com.android.dx.util.*;
import java.util.*;
import com.android.dx.rop.cst.*;
import com.android.dx.rop.type.*;

public final class RegisterSpec implements TypeBearer, ToHuman, Comparable<RegisterSpec>
{
    public static final String PREFIX = "v";
    private static final ForComparison theInterningItem;
    private static final HashMap<Object, RegisterSpec> theInterns;
    private final LocalItem local;
    private final int reg;
    private final TypeBearer type;
    
    static {
        theInterns = new HashMap<Object, RegisterSpec>(1000);
        theInterningItem = new ForComparison();
    }
    
    private RegisterSpec(final int reg, final TypeBearer type, final LocalItem local) {
        if (reg < 0) {
            throw new IllegalArgumentException("reg < 0");
        }
        if (type == null) {
            throw new NullPointerException("type == null");
        }
        this.reg = reg;
        this.type = type;
        this.local = local;
    }
    
    private boolean equals(final int n, final TypeBearer typeBearer, final LocalItem localItem) {
        return this.reg == n && this.type.equals(typeBearer) && (this.local == localItem || (this.local != null && this.local.equals(localItem)));
    }
    
    private static int hashCodeOf(final int n, final TypeBearer typeBearer, final LocalItem localItem) {
        int hashCode;
        if (localItem != null) {
            hashCode = localItem.hashCode();
        }
        else {
            hashCode = 0;
        }
        return (hashCode * 31 + typeBearer.hashCode()) * 31 + n;
    }
    
    private static RegisterSpec intern(final int n, final TypeBearer typeBearer, final LocalItem localItem) {
        synchronized (RegisterSpec.theInterns) {
            RegisterSpec.theInterningItem.set(n, typeBearer, localItem);
            final RegisterSpec registerSpec = RegisterSpec.theInterns.get(RegisterSpec.theInterningItem);
            if (registerSpec != null) {
                return registerSpec;
            }
            final RegisterSpec registerSpec2 = RegisterSpec.theInterningItem.toRegisterSpec();
            RegisterSpec.theInterns.put(registerSpec2, registerSpec2);
            return registerSpec2;
        }
    }
    
    public static RegisterSpec make(final int n, final TypeBearer typeBearer) {
        return intern(n, typeBearer, null);
    }
    
    public static RegisterSpec make(final int n, final TypeBearer typeBearer, final LocalItem localItem) {
        if (localItem == null) {
            throw new NullPointerException("local  == null");
        }
        return intern(n, typeBearer, localItem);
    }
    
    public static RegisterSpec makeLocalOptional(final int n, final TypeBearer typeBearer, final LocalItem localItem) {
        return intern(n, typeBearer, localItem);
    }
    
    public static String regString(final int n) {
        final StringBuilder sb = new StringBuilder();
        sb.append("v");
        sb.append(n);
        return sb.toString();
    }
    
    private String toString0(final boolean b) {
        final StringBuffer sb = new StringBuffer(40);
        sb.append(this.regString());
        sb.append(":");
        if (this.local != null) {
            sb.append(this.local.toString());
        }
        final Type type = this.type.getType();
        sb.append(type);
        if (type != this.type) {
            sb.append("=");
            if (b && this.type instanceof CstString) {
                sb.append(((CstString)this.type).toQuoted());
            }
            else if (b && this.type instanceof Constant) {
                sb.append(this.type.toHuman());
            }
            else {
                sb.append(this.type);
            }
        }
        return sb.toString();
    }
    
    @Override
    public int compareTo(final RegisterSpec registerSpec) {
        final int reg = this.reg;
        final int reg2 = registerSpec.reg;
        int n = -1;
        if (reg < reg2) {
            return -1;
        }
        if (this.reg > registerSpec.reg) {
            return 1;
        }
        final int compareTo = this.type.getType().compareTo(registerSpec.type.getType());
        if (compareTo != 0) {
            return compareTo;
        }
        if (this.local == null) {
            if (registerSpec.local == null) {
                n = 0;
            }
            return n;
        }
        if (registerSpec.local == null) {
            return 1;
        }
        return this.local.compareTo(registerSpec.local);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o instanceof RegisterSpec) {
            final RegisterSpec registerSpec = (RegisterSpec)o;
            return this.equals(registerSpec.reg, registerSpec.type, registerSpec.local);
        }
        if (o instanceof ForComparison) {
            final ForComparison forComparison = (ForComparison)o;
            return this.equals(forComparison.reg, forComparison.type, forComparison.local);
        }
        return false;
    }
    
    public boolean equalsUsingSimpleType(final RegisterSpec registerSpec) {
        final boolean matchesVariable = this.matchesVariable(registerSpec);
        boolean b = false;
        if (!matchesVariable) {
            return false;
        }
        if (this.reg == registerSpec.reg) {
            b = true;
        }
        return b;
    }
    
    @Override
    public final int getBasicFrameType() {
        return this.type.getBasicFrameType();
    }
    
    @Override
    public final int getBasicType() {
        return this.type.getBasicType();
    }
    
    public int getCategory() {
        return this.type.getType().getCategory();
    }
    
    @Override
    public TypeBearer getFrameType() {
        return this.type.getFrameType();
    }
    
    public LocalItem getLocalItem() {
        return this.local;
    }
    
    public int getNextReg() {
        return this.reg + this.getCategory();
    }
    
    public int getReg() {
        return this.reg;
    }
    
    @Override
    public Type getType() {
        return this.type.getType();
    }
    
    public TypeBearer getTypeBearer() {
        return this.type;
    }
    
    @Override
    public int hashCode() {
        return hashCodeOf(this.reg, this.type, this.local);
    }
    
    public RegisterSpec intersect(final RegisterSpec registerSpec, final boolean b) {
        if (this == registerSpec) {
            return this;
        }
        if (registerSpec == null) {
            return null;
        }
        if (this.reg != registerSpec.getReg()) {
            return null;
        }
        LocalItem local;
        if (this.local != null && this.local.equals(registerSpec.getLocalItem())) {
            local = this.local;
        }
        else {
            local = null;
        }
        final boolean b2 = local == this.local;
        if (b && !b2) {
            return null;
        }
        final Type type = this.getType();
        if (type != registerSpec.getType()) {
            return null;
        }
        TypeBearer type2;
        if (this.type.equals(registerSpec.getTypeBearer())) {
            type2 = this.type;
        }
        else {
            type2 = type;
        }
        if (type2 == this.type && b2) {
            return this;
        }
        if (local == null) {
            return make(this.reg, type2);
        }
        return make(this.reg, type2, local);
    }
    
    public boolean isCategory1() {
        return this.type.getType().isCategory1();
    }
    
    public boolean isCategory2() {
        return this.type.getType().isCategory2();
    }
    
    @Override
    public final boolean isConstant() {
        return false;
    }
    
    public boolean isEvenRegister() {
        return (this.getReg() & 0x1) == 0x0;
    }
    
    public boolean matchesVariable(final RegisterSpec registerSpec) {
        final boolean b = false;
        if (registerSpec == null) {
            return false;
        }
        boolean b2 = b;
        if (this.type.getType().equals(registerSpec.type.getType())) {
            if (this.local != registerSpec.local) {
                b2 = b;
                if (this.local == null) {
                    return b2;
                }
                b2 = b;
                if (!this.local.equals(registerSpec.local)) {
                    return b2;
                }
            }
            b2 = true;
        }
        return b2;
    }
    
    public String regString() {
        return regString(this.reg);
    }
    
    @Override
    public String toHuman() {
        return this.toString0(true);
    }
    
    @Override
    public String toString() {
        return this.toString0(false);
    }
    
    public RegisterSpec withLocalItem(final LocalItem localItem) {
        if (this.local == localItem) {
            return this;
        }
        if (this.local != null && this.local.equals(localItem)) {
            return this;
        }
        return makeLocalOptional(this.reg, this.type, localItem);
    }
    
    public RegisterSpec withOffset(final int n) {
        if (n == 0) {
            return this;
        }
        return this.withReg(this.reg + n);
    }
    
    public RegisterSpec withReg(final int n) {
        if (this.reg == n) {
            return this;
        }
        return makeLocalOptional(n, this.type, this.local);
    }
    
    public RegisterSpec withSimpleType() {
        final TypeBearer type = this.type;
        Type type2;
        if (type instanceof Type) {
            type2 = (Type)type;
        }
        else {
            type2 = type.getType();
        }
        Type initializedType = type2;
        if (type2.isUninitialized()) {
            initializedType = type2.getInitializedType();
        }
        if (initializedType == type) {
            return this;
        }
        return makeLocalOptional(this.reg, initializedType, this.local);
    }
    
    public RegisterSpec withType(final TypeBearer typeBearer) {
        return makeLocalOptional(this.reg, typeBearer, this.local);
    }
    
    private static class ForComparison
    {
        private LocalItem local;
        private int reg;
        private TypeBearer type;
        
        @Override
        public boolean equals(final Object o) {
            return o instanceof RegisterSpec && ((RegisterSpec)o).equals(this.reg, this.type, this.local);
        }
        
        @Override
        public int hashCode() {
            return hashCodeOf(this.reg, this.type, this.local);
        }
        
        public void set(final int reg, final TypeBearer type, final LocalItem local) {
            this.reg = reg;
            this.type = type;
            this.local = local;
        }
        
        public RegisterSpec toRegisterSpec() {
            return new RegisterSpec(this.reg, this.type, this.local, null);
        }
    }
}
