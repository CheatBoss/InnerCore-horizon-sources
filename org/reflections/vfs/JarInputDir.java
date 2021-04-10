package org.reflections.vfs;

import java.net.*;
import org.reflections.util.*;
import java.util.function.*;
import com.google.common.collect.*;
import org.reflections.*;
import java.util.zip.*;
import java.io.*;
import java.util.jar.*;
import java.util.*;

public class JarInputDir implements Dir
{
    long cursor;
    JarInputStream jarInputStream;
    long nextCursor;
    private final URL url;
    
    public JarInputDir(final URL url) {
        this.cursor = 0L;
        this.nextCursor = 0L;
        this.url = url;
    }
    
    @Override
    public void close() {
        Utils.close(this.jarInputStream);
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
                    {
                        try {
                            JarInputDir.this.jarInputStream = new JarInputStream(JarInputDir.this.url.openConnection().getInputStream());
                        }
                        catch (Exception ex) {
                            throw new ReflectionsException("Could not open url connection", ex);
                        }
                    }
                    
                    protected File computeNext() {
                        while (true) {
                            while (true) {
                                Label_0127: {
                                    try {
                                        while (true) {
                                            final JarEntry nextJarEntry = JarInputDir.this.jarInputStream.getNextJarEntry();
                                            if (nextJarEntry == null) {
                                                return (File)this.endOfData();
                                            }
                                            long size = nextJarEntry.getSize();
                                            if (size >= 0L) {
                                                break Label_0127;
                                            }
                                            size += 4294967295L;
                                            final JarInputDir this$0 = JarInputDir.this;
                                            this$0.nextCursor += size;
                                            if (!nextJarEntry.isDirectory()) {
                                                return new JarInputFile(nextJarEntry, JarInputDir.this, JarInputDir.this.cursor, JarInputDir.this.nextCursor);
                                            }
                                        }
                                    }
                                    catch (IOException ex) {
                                        throw new ReflectionsException("could not get next zip entry", ex);
                                    }
                                }
                                continue;
                            }
                        }
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
        return this.url.getPath();
    }
}
