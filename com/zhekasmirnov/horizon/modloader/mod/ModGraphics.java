package com.zhekasmirnov.horizon.modloader.mod;

import java.io.*;
import android.graphics.*;
import com.zhekasmirnov.horizon.runtime.logger.*;
import java.util.*;

public class ModGraphics
{
    private final List<Bitmap> allBitmaps;
    private final HashMap<String, HashMap<String, Bitmap>> loadedGraphics;
    
    public ModGraphics() {
        this.allBitmaps = new ArrayList<Bitmap>();
        this.loadedGraphics = new HashMap<String, HashMap<String, Bitmap>>();
    }
    
    public ModGraphics(final File directory) {
        this.allBitmaps = new ArrayList<Bitmap>();
        this.loadedGraphics = new HashMap<String, HashMap<String, Bitmap>>();
        final File[] files = directory.listFiles();
        if (files != null) {
            for (final File file : files) {
                final String[] name = file.getName().split("@", 2);
                final Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                if (bitmap != null) {
                    final String group = name[0];
                    final String alias = (name.length > 1) ? name[1] : UUID.randomUUID().toString();
                    if (!this.loadedGraphics.containsKey(group)) {
                        this.loadedGraphics.put(group, new HashMap<String, Bitmap>());
                    }
                    this.loadedGraphics.get(group).put(alias, bitmap);
                    this.allBitmaps.add(bitmap);
                }
                else {
                    Logger.error("pack graphics cannot decode archive entry: " + file.getName());
                }
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
