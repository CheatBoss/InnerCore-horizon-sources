package com.bumptech.glide.signature;

import com.bumptech.glide.load.*;
import java.security.*;
import java.io.*;

public final class EmptySignature implements Key
{
    private static final EmptySignature EMPTY_KEY;
    
    static {
        EMPTY_KEY = new EmptySignature();
    }
    
    private EmptySignature() {
    }
    
    public static EmptySignature obtain() {
        return EmptySignature.EMPTY_KEY;
    }
    
    @Override
    public void updateDiskCacheKey(final MessageDigest messageDigest) throws UnsupportedEncodingException {
    }
}
