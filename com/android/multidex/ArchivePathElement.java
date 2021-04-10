package com.android.multidex;

import java.util.function.*;
import java.util.zip.*;
import java.util.*;
import java.io.*;

class ArchivePathElement implements ClassPathElement
{
    private final ZipFile archive;
    
    public ArchivePathElement(final ZipFile archive) {
        this.archive = archive;
    }
    
    @Override
    public void close() throws IOException {
        this.archive.close();
    }
    
    @Override
    public Iterable<String> list() {
        return new Iterable<String>() {
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
            public Iterator<String> iterator() {
                return new Iterator<String>() {
                    Enumeration<? extends ZipEntry> delegate = ArchivePathElement.this.archive.entries();
                    ZipEntry next = null;
                    
                    @Override
                    public void forEachRemaining(final Consumer<?> p0) {
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
                    public boolean hasNext() {
                        while (this.next == null && this.delegate.hasMoreElements()) {
                            this.next = (ZipEntry)this.delegate.nextElement();
                            if (this.next.isDirectory()) {
                                this.next = null;
                            }
                        }
                        return this.next != null;
                    }
                    
                    @Override
                    public String next() {
                        if (this.hasNext()) {
                            final String name = this.next.getName();
                            this.next = null;
                            return name;
                        }
                        throw new NoSuchElementException();
                    }
                    
                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
            
            @Override
            public Spliterator<Object> spliterator() {
                return Iterable-CC.$default$spliterator();
            }
        };
    }
    
    @Override
    public InputStream open(final String s) throws IOException {
        final ZipEntry entry = this.archive.getEntry(s);
        if (entry == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("File \"");
            sb.append(s);
            sb.append("\" not found");
            throw new FileNotFoundException(sb.toString());
        }
        if (entry.isDirectory()) {
            throw new DirectoryEntryException();
        }
        return this.archive.getInputStream(entry);
    }
    
    static class DirectoryEntryException extends IOException
    {
    }
}
