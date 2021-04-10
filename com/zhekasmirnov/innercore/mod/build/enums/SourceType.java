package com.zhekasmirnov.innercore.mod.build.enums;

public enum SourceType
{
    CUSTOM("custom"), 
    LAUNCHER("launcher"), 
    LIBRARY("library"), 
    MOD("mod"), 
    PRELOADER("preloader");
    
    private final String name;
    
    private SourceType(final String name) {
        this.name = name;
    }
    
    public static SourceType fromString(final String s) {
        final int hashCode = s.hashCode();
        int n = 0;
        Label_0090: {
            if (hashCode != -1407250528) {
                if (hashCode != -1349088399) {
                    if (hashCode != -1113514890) {
                        if (hashCode == 166208699) {
                            if (s.equals("library")) {
                                n = 2;
                                break Label_0090;
                            }
                        }
                    }
                    else if (s.equals("preloader")) {
                        n = 0;
                        break Label_0090;
                    }
                }
                else if (s.equals("custom")) {
                    n = 3;
                    break Label_0090;
                }
            }
            else if (s.equals("launcher")) {
                n = 1;
                break Label_0090;
            }
            n = -1;
        }
        switch (n) {
            default: {
                return SourceType.MOD;
            }
            case 3: {
                return SourceType.CUSTOM;
            }
            case 2: {
                return SourceType.LIBRARY;
            }
            case 1: {
                return SourceType.LAUNCHER;
            }
            case 0: {
                return SourceType.PRELOADER;
            }
        }
    }
    
    @Override
    public String toString() {
        return this.name;
    }
}
