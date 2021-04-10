package com.zhekasmirnov.innercore.api.log;

public enum LogPrefix
{
    DEBUG, 
    ERROR, 
    INFO, 
    LOADER, 
    MOD, 
    WARNING;
    
    private String name;
    
    public static LogPrefix fromString(final String s) {
        final int hashCode = s.hashCode();
        int n = 0;
        Label_0090: {
            if (hashCode != 2251950) {
                if (hashCode != 64921139) {
                    if (hashCode != 66247144) {
                        if (hashCode == 1842428796) {
                            if (s.equals("WARNING")) {
                                n = 3;
                                break Label_0090;
                            }
                        }
                    }
                    else if (s.equals("ERROR")) {
                        n = 1;
                        break Label_0090;
                    }
                }
                else if (s.equals("DEBUG")) {
                    n = 0;
                    break Label_0090;
                }
            }
            else if (s.equals("INFO")) {
                n = 2;
                break Label_0090;
            }
            n = -1;
        }
        switch (n) {
            default: {
                if (s.contains("MOD")) {
                    return LogPrefix.MOD;
                }
                return LogPrefix.LOADER;
            }
            case 3: {
                return LogPrefix.WARNING;
            }
            case 2: {
                return LogPrefix.INFO;
            }
            case 1: {
                return LogPrefix.ERROR;
            }
            case 0: {
                return LogPrefix.DEBUG;
            }
        }
    }
    
    public String toFontColor() {
        switch (this) {
            default: {
                return "#FFFFFF";
            }
            case MOD: {
                return "#FFFFFF";
            }
            case ERROR: {
                return "#FF0000";
            }
            case WARNING: {
                return "#FFFF00";
            }
            case INFO: {
                return "#0000FF";
            }
        }
    }
}
