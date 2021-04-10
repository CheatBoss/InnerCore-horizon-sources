package com.bumptech.glide.signature;

import com.bumptech.glide.load.*;
import java.security.*;
import java.io.*;

public class StringSignature implements Key
{
    private final String signature;
    
    public StringSignature(final String signature) {
        if (signature == null) {
            throw new NullPointerException("Signature cannot be null!");
        }
        this.signature = signature;
    }
    
    @Override
    public boolean equals(final Object o) {
        return this == o || (o != null && this.getClass() == o.getClass() && this.signature.equals(((StringSignature)o).signature));
    }
    
    @Override
    public int hashCode() {
        return this.signature.hashCode();
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("StringSignature{signature='");
        sb.append(this.signature);
        sb.append('\'');
        sb.append('}');
        return sb.toString();
    }
    
    @Override
    public void updateDiskCacheKey(final MessageDigest messageDigest) throws UnsupportedEncodingException {
        messageDigest.update(this.signature.getBytes("UTF-8"));
    }
}
