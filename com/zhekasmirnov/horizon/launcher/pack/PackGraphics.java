package com.zhekasmirnov.horizon.launcher.pack;

import android.graphics.*;
import com.zhekasmirnov.horizon.runtime.logger.*;
import java.util.zip.*;
import java.io.*;
import java.util.*;

public class PackGraphics
{
    private final List<Bitmap> allBitmaps;
    private final HashMap<String, HashMap<String, Bitmap>> loadedGraphics;
    
    public PackGraphics() {
        this.allBitmaps = new ArrayList<Bitmap>();
        this.loadedGraphics = new HashMap<String, HashMap<String, Bitmap>>();
    }
    
    public PackGraphics(final InputStream stream) throws IOException {
        this.allBitmaps = new ArrayList<Bitmap>();
        this.loadedGraphics = new HashMap<String, HashMap<String, Bitmap>>();
        final ZipInputStream zipStream = new ZipInputStream(stream);
        ZipEntry entry;
        while ((entry = zipStream.getNextEntry()) != null) {
            final String[] name = entry.getName().split("@", 2);
            final Bitmap bitmap = BitmapFactory.decodeStream((InputStream)zipStream);
            if (bitmap != null) {
                final String group = name[0];
                final String file = (name.length > 1) ? name[1] : UUID.randomUUID().toString();
                if (!this.loadedGraphics.containsKey(group)) {
                    this.loadedGraphics.put(group, new HashMap<String, Bitmap>());
                }
                this.loadedGraphics.get(group).put(file, bitmap);
                this.allBitmaps.add(bitmap);
            }
            else {
                Logger.error("pack graphics cannot decode archive entry: " + entry.getName());
            }
        }
    }
    
    public HashMap<String, Bitmap> getNamedGroup(final String name) {
        return this.loadedGraphics.get(name);
    }
    
    public Collection<Bitmap> getGroup(final String name) {
        return this.loadedGraphics.containsKey(name) ? this.loadedGraphics.get(name).values() : null;
    }
    
    public List<Bitmap> getAllBitmaps() {
        return this.allBitmaps;
    }
}
