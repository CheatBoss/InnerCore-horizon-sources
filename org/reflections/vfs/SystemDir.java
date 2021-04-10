package org.reflections.vfs;

import java.io.*;
import java.util.function.*;
import com.google.common.collect.*;
import java.util.*;

public class SystemDir implements Dir
{
    private final java.io.File file;
    
    public SystemDir(final java.io.File file) {
        if (file != null && (!file.isDirectory() || !file.canRead())) {
            final StringBuilder sb = new StringBuilder();
            sb.append("cannot use dir ");
            sb.append(file);
            throw new RuntimeException(sb.toString());
        }
        this.file = file;
    }
    
    private static List<java.io.File> listFiles(final java.io.File file) {
        final java.io.File[] listFiles = file.listFiles();
        if (listFiles != null) {
            return (List<java.io.File>)Lists.newArrayList((Object[])listFiles);
        }
        return (List<java.io.File>)Lists.newArrayList();
    }
    
    @Override
    public void close() {
    }
    
    @Override
    public Iterable<File> getFiles() {
        if (this.file != null && this.file.exists()) {
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
                        final Stack<java.io.File> stack;
                        
                        {
                            (this.stack = new Stack<java.io.File>()).addAll((Collection<?>)listFiles(SystemDir.this.file));
                        }
                        
                        protected File computeNext() {
                            while (!this.stack.isEmpty()) {
                                final java.io.File file = this.stack.pop();
                                if (!file.isDirectory()) {
                                    return new SystemFile(SystemDir.this, file);
                                }
                                this.stack.addAll((Collection<?>)listFiles(file));
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
        return (Iterable<File>)Collections.emptyList();
    }
    
    @Override
    public String getPath() {
        if (this.file == null) {
            return "/NO-SUCH-DIRECTORY/";
        }
        return this.file.getPath().replace("\\", "/");
    }
    
    @Override
    public String toString() {
        return this.getPath();
    }
}
