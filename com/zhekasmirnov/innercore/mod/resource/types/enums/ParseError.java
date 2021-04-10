package com.zhekasmirnov.innercore.mod.resource.types.enums;

public enum ParseError
{
    ANIMATION_INVALID_DELAY, 
    ANIMATION_INVALID_FILE, 
    ANIMATION_INVALID_JSON, 
    ANIMATION_INVALID_NAME, 
    ANIMATION_NAME_MISSING, 
    ANIMATION_TILE_MISSING, 
    NONE;
    
    @Override
    public String toString() {
        switch (this) {
            default: {
                return "Unknown Error";
            }
            case ANIMATION_INVALID_DELAY: {
                return "Animation delay is not a number or less than 1";
            }
            case ANIMATION_TILE_MISSING: {
                return "Animation json missing tile texture name";
            }
            case ANIMATION_NAME_MISSING: {
                return "Animation json missing animation texture name";
            }
            case ANIMATION_INVALID_JSON: {
                return "Animation json file could not be parsed";
            }
            case ANIMATION_INVALID_NAME: {
                return "Animation file has invalid name (use <tile>.anim.png or <tile>.anim.<delay>.png)";
            }
            case ANIMATION_INVALID_FILE: {
                return "Animation file has invalid extension (.png or .json needed)";
            }
            case NONE: {
                return "No Error";
            }
        }
    }
}
