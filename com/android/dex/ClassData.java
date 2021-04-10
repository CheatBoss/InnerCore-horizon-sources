package com.android.dex;

public final class ClassData
{
    private final Method[] directMethods;
    private final Field[] instanceFields;
    private final Field[] staticFields;
    private final Method[] virtualMethods;
    
    public ClassData(final Field[] staticFields, final Field[] instanceFields, final Method[] directMethods, final Method[] virtualMethods) {
        this.staticFields = staticFields;
        this.instanceFields = instanceFields;
        this.directMethods = directMethods;
        this.virtualMethods = virtualMethods;
    }
    
    public Field[] allFields() {
        final Field[] array = new Field[this.staticFields.length + this.instanceFields.length];
        System.arraycopy(this.staticFields, 0, array, 0, this.staticFields.length);
        System.arraycopy(this.instanceFields, 0, array, this.staticFields.length, this.instanceFields.length);
        return array;
    }
    
    public Method[] allMethods() {
        final Method[] array = new Method[this.directMethods.length + this.virtualMethods.length];
        System.arraycopy(this.directMethods, 0, array, 0, this.directMethods.length);
        System.arraycopy(this.virtualMethods, 0, array, this.directMethods.length, this.virtualMethods.length);
        return array;
    }
    
    public Method[] getDirectMethods() {
        return this.directMethods;
    }
    
    public Field[] getInstanceFields() {
        return this.instanceFields;
    }
    
    public Field[] getStaticFields() {
        return this.staticFields;
    }
    
    public Method[] getVirtualMethods() {
        return this.virtualMethods;
    }
    
    public static class Field
    {
        private final int accessFlags;
        private final int fieldIndex;
        
        public Field(final int fieldIndex, final int accessFlags) {
            this.fieldIndex = fieldIndex;
            this.accessFlags = accessFlags;
        }
        
        public int getAccessFlags() {
            return this.accessFlags;
        }
        
        public int getFieldIndex() {
            return this.fieldIndex;
        }
    }
    
    public static class Method
    {
        private final int accessFlags;
        private final int codeOffset;
        private final int methodIndex;
        
        public Method(final int methodIndex, final int accessFlags, final int codeOffset) {
            this.methodIndex = methodIndex;
            this.accessFlags = accessFlags;
            this.codeOffset = codeOffset;
        }
        
        public int getAccessFlags() {
            return this.accessFlags;
        }
        
        public int getCodeOffset() {
            return this.codeOffset;
        }
        
        public int getMethodIndex() {
            return this.methodIndex;
        }
    }
}
