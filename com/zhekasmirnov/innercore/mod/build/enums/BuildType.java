package com.zhekasmirnov.innercore.mod.build.enums;

public enum BuildType
{
    DEVELOP("develop"), 
    RELEASE("release");
    
    private final String name;
    
    private BuildType(final String name) {
        this.name = name;
    }
    
    public static BuildType fromString(final String s) {
        int n = 0;
        Label_0028: {
            if (s.hashCode() == 1090594823) {
                if (s.equals("release")) {
                    n = 0;
                    break Label_0028;
                }
            }
            n = -1;
        }
        if (n != 0) {
            return BuildType.DEVELOP;
        }
        return BuildType.RELEASE;
    }
    
    @Override
    public String toString() {
        return this.name;
    }
}
