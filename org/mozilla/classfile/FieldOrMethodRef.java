package org.mozilla.classfile;

final class FieldOrMethodRef
{
    private String className;
    private int hashCode;
    private String name;
    private String type;
    
    FieldOrMethodRef(final String className, final String name, final String type) {
        this.hashCode = -1;
        this.className = className;
        this.name = name;
        this.type = type;
    }
    
    @Override
    public boolean equals(final Object o) {
        final boolean b = o instanceof FieldOrMethodRef;
        final boolean b2 = false;
        if (!b) {
            return false;
        }
        final FieldOrMethodRef fieldOrMethodRef = (FieldOrMethodRef)o;
        boolean b3 = b2;
        if (this.className.equals(fieldOrMethodRef.className)) {
            b3 = b2;
            if (this.name.equals(fieldOrMethodRef.name)) {
                b3 = b2;
                if (this.type.equals(fieldOrMethodRef.type)) {
                    b3 = true;
                }
            }
        }
        return b3;
    }
    
    public String getClassName() {
        return this.className;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getType() {
        return this.type;
    }
    
    @Override
    public int hashCode() {
        if (this.hashCode == -1) {
            this.hashCode = (this.className.hashCode() ^ this.name.hashCode() ^ this.type.hashCode());
        }
        return this.hashCode;
    }
}
