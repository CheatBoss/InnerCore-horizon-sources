package com.zhekasmirnov.innercore.mod.build.enums;

public enum ResourceDirType
{
    GUI("gui"), 
    RESOURCE("resource");
    
    private final String name;
    
    private ResourceDirType(final String name) {
        this.name = name;
    }
    
    public static ResourceDirType fromString(final String s) {
        int n = 0;
        Label_0028: {
            if (s.hashCode() == 102715) {
                if (s.equals("gui")) {
                    n = 0;
                    break Label_0028;
                }
            }
            n = -1;
        }
        if (n != 0) {
            return ResourceDirType.RESOURCE;
        }
        return ResourceDirType.GUI;
    }
    
    @Override
    public String toString() {
        return this.name;
    }
}
