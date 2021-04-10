package org.spongycastle.util.io.pem;

public class PemHeader
{
    private String name;
    private String value;
    
    public PemHeader(final String name, final String value) {
        this.name = name;
        this.value = value;
    }
    
    private int getHashCode(final String s) {
        if (s == null) {
            return 1;
        }
        return s.hashCode();
    }
    
    private boolean isEqual(final String s, final String s2) {
        return s == s2 || (s != null && s2 != null && s.equals(s2));
    }
    
    @Override
    public boolean equals(final Object o) {
        final boolean b = o instanceof PemHeader;
        final boolean b2 = false;
        if (!b) {
            return false;
        }
        final PemHeader pemHeader = (PemHeader)o;
        if (pemHeader != this) {
            boolean b3 = b2;
            if (!this.isEqual(this.name, pemHeader.name)) {
                return b3;
            }
            b3 = b2;
            if (!this.isEqual(this.value, pemHeader.value)) {
                return b3;
            }
        }
        return true;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getValue() {
        return this.value;
    }
    
    @Override
    public int hashCode() {
        return this.getHashCode(this.name) + this.getHashCode(this.value) * 31;
    }
}
