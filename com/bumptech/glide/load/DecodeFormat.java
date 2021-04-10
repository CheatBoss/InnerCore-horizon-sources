package com.bumptech.glide.load;

public enum DecodeFormat
{
    @Deprecated
    ALWAYS_ARGB_8888;
    
    public static final DecodeFormat DEFAULT;
    
    PREFER_ARGB_8888, 
    PREFER_RGB_565;
    
    static {
        DEFAULT = DecodeFormat.PREFER_RGB_565;
    }
}
