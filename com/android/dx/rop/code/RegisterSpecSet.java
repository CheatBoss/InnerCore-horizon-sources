package com.android.dx.rop.code;

import com.android.dx.util.*;

public final class RegisterSpecSet extends MutabilityControl
{
    public static final RegisterSpecSet EMPTY;
    private int size;
    private final RegisterSpec[] specs;
    
    static {
        EMPTY = new RegisterSpecSet(0);
    }
    
    public RegisterSpecSet(final int n) {
        super(n != 0);
        this.specs = new RegisterSpec[n];
        this.size = 0;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof RegisterSpecSet)) {
            return false;
        }
        final RegisterSpecSet set = (RegisterSpecSet)o;
        final RegisterSpec[] specs = set.specs;
        final int length = this.specs.length;
        if (length != specs.length) {
            return false;
        }
        if (this.size() != set.size()) {
            return false;
        }
        for (int i = 0; i < length; ++i) {
            final RegisterSpec registerSpec = this.specs[i];
            final RegisterSpec registerSpec2 = specs[i];
            if (registerSpec != registerSpec2) {
                if (registerSpec == null) {
                    return false;
                }
                if (!registerSpec.equals(registerSpec2)) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public RegisterSpec findMatchingLocal(final RegisterSpec registerSpec) {
        for (int length = this.specs.length, i = 0; i < length; ++i) {
            final RegisterSpec registerSpec2 = this.specs[i];
            if (registerSpec2 != null) {
                if (registerSpec.matchesVariable(registerSpec2)) {
                    return registerSpec2;
                }
            }
        }
        return null;
    }
    
    public RegisterSpec get(final int n) {
        try {
            return this.specs[n];
        }
        catch (ArrayIndexOutOfBoundsException ex) {
            throw new IllegalArgumentException("bogus reg");
        }
    }
    
    public RegisterSpec get(final RegisterSpec registerSpec) {
        return this.get(registerSpec.getReg());
    }
    
    public int getMaxSize() {
        return this.specs.length;
    }
    
    @Override
    public int hashCode() {
        final int length = this.specs.length;
        int n = 0;
        for (int i = 0; i < length; ++i) {
            final RegisterSpec registerSpec = this.specs[i];
            int hashCode;
            if (registerSpec == null) {
                hashCode = 0;
            }
            else {
                hashCode = registerSpec.hashCode();
            }
            n = n * 31 + hashCode;
        }
        return n;
    }
    
    public void intersect(final RegisterSpecSet set, final boolean b) {
        this.throwIfImmutable();
        final RegisterSpec[] specs = set.specs;
        final int length = this.specs.length;
        final int min = Math.min(length, specs.length);
        this.size = -1;
        for (int i = 0; i < min; ++i) {
            final RegisterSpec registerSpec = this.specs[i];
            if (registerSpec != null) {
                final RegisterSpec intersect = registerSpec.intersect(specs[i], b);
                if (intersect != registerSpec) {
                    this.specs[i] = intersect;
                }
            }
        }
        for (int j = min; j < length; ++j) {
            this.specs[j] = null;
        }
    }
    
    public RegisterSpec localItemToSpec(final LocalItem localItem) {
        for (int length = this.specs.length, i = 0; i < length; ++i) {
            final RegisterSpec registerSpec = this.specs[i];
            if (registerSpec != null && localItem.equals(registerSpec.getLocalItem())) {
                return registerSpec;
            }
        }
        return null;
    }
    
    public RegisterSpecSet mutableCopy() {
        final int length = this.specs.length;
        final RegisterSpecSet set = new RegisterSpecSet(length);
        for (int i = 0; i < length; ++i) {
            final RegisterSpec registerSpec = this.specs[i];
            if (registerSpec != null) {
                set.put(registerSpec);
            }
        }
        set.size = this.size;
        return set;
    }
    
    public void put(final RegisterSpec registerSpec) {
        this.throwIfImmutable();
        if (registerSpec == null) {
            throw new NullPointerException("spec == null");
        }
        this.size = -1;
        try {
            final int reg = registerSpec.getReg();
            this.specs[reg] = registerSpec;
            if (reg > 0) {
                final int n = reg - 1;
                final RegisterSpec registerSpec2 = this.specs[n];
                if (registerSpec2 != null && registerSpec2.getCategory() == 2) {
                    this.specs[n] = null;
                }
            }
            if (registerSpec.getCategory() == 2) {
                this.specs[reg + 1] = null;
            }
        }
        catch (ArrayIndexOutOfBoundsException ex) {
            throw new IllegalArgumentException("spec.getReg() out of range");
        }
    }
    
    public void putAll(final RegisterSpecSet set) {
        for (int maxSize = set.getMaxSize(), i = 0; i < maxSize; ++i) {
            final RegisterSpec value = set.get(i);
            if (value != null) {
                this.put(value);
            }
        }
    }
    
    public void remove(final RegisterSpec registerSpec) {
        try {
            this.specs[registerSpec.getReg()] = null;
            this.size = -1;
        }
        catch (ArrayIndexOutOfBoundsException ex) {
            throw new IllegalArgumentException("bogus reg");
        }
    }
    
    public int size() {
        int size;
        if ((size = this.size) < 0) {
            final int length = this.specs.length;
            size = 0;
            int n;
            for (int i = 0; i < length; ++i, size = n) {
                n = size;
                if (this.specs[i] != null) {
                    n = size + 1;
                }
            }
            this.size = size;
        }
        return size;
    }
    
    @Override
    public String toString() {
        final int length = this.specs.length;
        final StringBuffer sb = new StringBuffer(length * 25);
        sb.append('{');
        int n = 0;
        int n2;
        for (int i = 0; i < length; ++i, n = n2) {
            final RegisterSpec registerSpec = this.specs[i];
            n2 = n;
            if (registerSpec != null) {
                if (n != 0) {
                    sb.append(", ");
                }
                else {
                    n = 1;
                }
                sb.append(registerSpec);
                n2 = n;
            }
        }
        sb.append('}');
        return sb.toString();
    }
    
    public RegisterSpecSet withOffset(final int n) {
        final int length = this.specs.length;
        final RegisterSpecSet set = new RegisterSpecSet(length + n);
        for (int i = 0; i < length; ++i) {
            final RegisterSpec registerSpec = this.specs[i];
            if (registerSpec != null) {
                set.put(registerSpec.withOffset(n));
            }
        }
        set.size = this.size;
        if (this.isImmutable()) {
            set.setImmutable();
        }
        return set;
    }
}
