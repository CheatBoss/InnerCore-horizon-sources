package com.bumptech.glide.load.engine.cache;

import com.bumptech.glide.load.*;
import com.bumptech.glide.util.*;
import java.security.*;
import java.io.*;

class SafeKeyGenerator
{
    private final LruCache<Key, String> loadIdToSafeHash;
    
    SafeKeyGenerator() {
        this.loadIdToSafeHash = new LruCache<Key, String>(1000);
    }
    
    public String getSafeKey(final Key key) {
        Object o = this.loadIdToSafeHash;
        synchronized (o) {
            String sha256BytesToHex = this.loadIdToSafeHash.get(key);
            // monitorexit(o)
            if (sha256BytesToHex == null) {
                try {
                    final MessageDigest instance = MessageDigest.getInstance("SHA-256");
                    key.updateDiskCacheKey(instance);
                    o = (sha256BytesToHex = Util.sha256BytesToHex(instance.digest()));
                }
                catch (NoSuchAlgorithmException o) {
                    ((Throwable)o).printStackTrace();
                }
                catch (UnsupportedEncodingException o) {
                    ((Throwable)o).printStackTrace();
                }
                o = this.loadIdToSafeHash;
                synchronized (o) {
                    this.loadIdToSafeHash.put(key, (String)o);
                    return (String)o;
                }
            }
            return (String)o;
        }
    }
}
