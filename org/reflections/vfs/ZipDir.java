package org.reflections.vfs;

import java.util.jar.*;
import org.reflections.*;
import java.io.*;
import java.util.function.*;
import com.google.common.collect.*;
import java.util.zip.*;
import java.util.*;

public class ZipDir implements Dir
{
    final ZipFile jarFile;
    
    public ZipDir(final JarFile jarFile) {
        this.jarFile = jarFile;
    }
    
    @Override
    public void close() {
        try {
            this.jarFile.close();
        }
        catch (IOException ex) {
            if (Reflections.log != null) {
                Reflections.log.warn("Could not close JarFile", (Throwable)ex);
            }
        }
    }
    
    @Override
    public Iterable<File> getFiles() {
        return new Iterable<File>() {
            @Override
            public void forEach(final Consumer<?> p0) {
                // 
                // This method could not be decompiled.
                // 
                // Could not show original bytecode, likely due to the same error.
                // 
                // The error that occurred was:
                // 
                // java.lang.NullPointerException
                // 
                throw new IllegalStateException("An error occurred while decompiling this method.");
            }
            
            @Override
            public Iterator<File> iterator() {
                return (Iterator<File>)new AbstractIterator<File>() {
                    final Enumeration<? extends ZipEntry> entries = ZipDir.this.jarFile.entries();
                    
                    protected File computeNext() {
                        while (this.entries.hasMoreElements()) {
                            final ZipEntry zipEntry = (ZipEntry)this.entries.nextElement();
                            if (!zipEntry.isDirectory()) {
                                return new org.reflections.vfs.ZipFile(ZipDir.this, zipEntry);
                            }
                        }
                        return (File)this.endOfData();
                    }
                };
            }
            
            @Override
            public Spliterator<Object> spliterator() {
                return (Spliterator<Object>)Iterable-CC.$default$spliterator((Iterable)this);
            }
        };
    }
    
    @Override
    public String getPath() {
        return this.jarFile.getName();
    }
    
    @Override
    public String toString() {
        return this.jarFile.getName();
    }
}
