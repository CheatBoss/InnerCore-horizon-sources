package com.bumptech.glide.load.engine;

import com.bumptech.glide.load.*;
import java.security.*;
import java.io.*;

class OriginalKey implements Key
{
    private final String id;
    private final Key signature;
    
    public OriginalKey(final String id, final Key signature) {
        this.id = id;
        this.signature = signature;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (this.getClass() != o.getClass()) {
            return false;
        }
        final OriginalKey originalKey = (OriginalKey)o;
        return this.id.equals(originalKey.id) && this.signature.equals(originalKey.signature);
    }
    
    @Override
    public int hashCode() {
        return this.id.hashCode() * 31 + this.signature.hashCode();
    }
    
    @Override
    public void updateDiskCacheKey(final MessageDigest messageDigest) throws UnsupportedEncodingException {
        messageDigest.update(this.id.getBytes("UTF-8"));
        this.signature.updateDiskCacheKey(messageDigest);
    }
}
