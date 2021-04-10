package com.bumptech.glide.signature;

import com.bumptech.glide.load.*;
import java.security.*;
import java.nio.*;
import java.io.*;

public class MediaStoreSignature implements Key
{
    private final long dateModified;
    private final String mimeType;
    private final int orientation;
    
    public MediaStoreSignature(final String mimeType, final long dateModified, final int orientation) {
        this.mimeType = mimeType;
        this.dateModified = dateModified;
        this.orientation = orientation;
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
        final MediaStoreSignature mediaStoreSignature = (MediaStoreSignature)o;
        if (this.dateModified != mediaStoreSignature.dateModified) {
            return false;
        }
        if (this.orientation != mediaStoreSignature.orientation) {
            return false;
        }
        if (this.mimeType != null) {
            if (!this.mimeType.equals(mediaStoreSignature.mimeType)) {
                return false;
            }
        }
        else if (mediaStoreSignature.mimeType != null) {
            return false;
        }
        return true;
    }
    
    @Override
    public int hashCode() {
        int hashCode;
        if (this.mimeType != null) {
            hashCode = this.mimeType.hashCode();
        }
        else {
            hashCode = 0;
        }
        return (hashCode * 31 + (int)(this.dateModified ^ this.dateModified >>> 32)) * 31 + this.orientation;
    }
    
    @Override
    public void updateDiskCacheKey(final MessageDigest messageDigest) throws UnsupportedEncodingException {
        messageDigest.update(ByteBuffer.allocate(12).putLong(this.dateModified).putInt(this.orientation).array());
        messageDigest.update(this.mimeType.getBytes("UTF-8"));
    }
}
