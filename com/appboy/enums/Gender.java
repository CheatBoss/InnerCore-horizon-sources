package com.appboy.enums;

import com.appboy.models.*;

public enum Gender implements IPutIntoJson<String>
{
    FEMALE, 
    MALE, 
    NOT_APPLICABLE, 
    OTHER, 
    PREFER_NOT_TO_SAY, 
    UNKNOWN;
    
    @Override
    public String forJsonPut() {
        switch (Gender$1.a[this.ordinal()]) {
            default: {
                return null;
            }
            case 6: {
                return "p";
            }
            case 5: {
                return "n";
            }
            case 4: {
                return "u";
            }
            case 3: {
                return "o";
            }
            case 2: {
                return "f";
            }
            case 1: {
                return "m";
            }
        }
    }
}
