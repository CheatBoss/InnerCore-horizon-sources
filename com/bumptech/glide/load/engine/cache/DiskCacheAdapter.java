package com.bumptech.glide.load.engine.cache;

import com.bumptech.glide.load.*;
import java.io.*;

public class DiskCacheAdapter implements DiskCache
{
    @Override
    public void clear() {
    }
    
    @Override
    public void delete(final Key key) {
    }
    
    @Override
    public File get(final Key key) {
        return null;
    }
    
    @Override
    public void put(final Key key, final Writer writer) {
    }
}
