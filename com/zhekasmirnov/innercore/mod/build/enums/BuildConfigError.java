package com.zhekasmirnov.innercore.mod.build.enums;

public enum BuildConfigError
{
    FILE_ERROR, 
    NONE, 
    PARSE_ERROR;
    
    @Override
    public String toString() {
        switch (this) {
            default: {
                return "Unknown Error";
            }
            case PARSE_ERROR: {
                return "JSON Parse Error";
            }
            case FILE_ERROR: {
                return "File could not be loaded";
            }
            case NONE: {
                return "No Error";
            }
        }
    }
}
