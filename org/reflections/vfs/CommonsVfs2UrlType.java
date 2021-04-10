package org.reflections.vfs;

import java.net.*;
import org.reflections.*;
import org.apache.commons.vfs2.*;
import org.slf4j.*;
import java.util.function.*;
import com.google.common.collect.*;
import java.util.*;
import java.io.*;

public class CommonsVfs2UrlType implements UrlType
{
    @Override
    public Vfs.Dir createDir(final URL url) throws Exception {
        return new Dir(VFS.getManager().resolveFile(url.toExternalForm()));
    }
    
    @Override
    public boolean matches(final URL url) throws Exception {
        final boolean b = false;
        try {
            final FileObject resolveFile = VFS.getManager().resolveFile(url.toExternalForm());
            boolean b2 = b;
            if (resolveFile.exists()) {
                final FileType type = resolveFile.getType();
                final FileType folder = FileType.FOLDER;
                b2 = b;
                if (type == folder) {
                    b2 = true;
                }
            }
            return b2;
        }
        catch (FileSystemException ex) {
            final Logger log = Reflections.log;
            final StringBuilder sb = new StringBuilder();
            sb.append("Could not create CommonsVfs2UrlType from url ");
            sb.append(url.toExternalForm());
            log.warn(sb.toString(), (Throwable)ex);
            return false;
        }
    }
    
    public static class Dir implements Vfs.Dir
    {
        private final FileObject file;
        
        public Dir(final FileObject file) {
            this.file = file;
        }
        
        @Override
        public void close() {
            try {
                this.file.close();
            }
            catch (FileSystemException ex) {}
        }
        
        @Override
        public Iterable<Vfs.File> getFiles() {
            return new Iterable<Vfs.File>() {
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
                public Iterator<Vfs.File> iterator() {
                    return (Iterator<Vfs.File>)new FileAbstractIterator();
                }
                
                @Override
                public Spliterator<Object> spliterator() {
                    return (Spliterator<Object>)Iterable-CC.$default$spliterator((Iterable)this);
                }
            };
        }
        
        @Override
        public String getPath() {
            try {
                return this.file.getURL().getPath();
            }
            catch (FileSystemException ex) {
                throw new RuntimeException((Throwable)ex);
            }
        }
        
        private class FileAbstractIterator extends AbstractIterator<Vfs.File>
        {
            final Stack<FileObject> stack;
            
            private FileAbstractIterator() {
                this.stack = new Stack<FileObject>();
                this.listDir(Dir.this.file);
            }
            
            private CommonsVfs2UrlType.File getFile(final FileObject fileObject) {
                return new CommonsVfs2UrlType.File(Dir.this.file, fileObject);
            }
            
            private boolean isDir(final FileObject fileObject) throws FileSystemException {
                return fileObject.getType() == FileType.FOLDER;
            }
            
            private boolean listDir(final FileObject fileObject) {
                return this.stack.addAll((Collection<?>)this.listFiles(fileObject));
            }
            
            protected Vfs.File computeNext() {
                while (!this.stack.isEmpty()) {
                    final FileObject fileObject = this.stack.pop();
                    try {
                        if (this.isDir(fileObject)) {
                            this.listDir(fileObject);
                            continue;
                        }
                        return this.getFile(fileObject);
                    }
                    catch (FileSystemException ex) {
                        throw new RuntimeException((Throwable)ex);
                    }
                    break;
                }
                return (Vfs.File)this.endOfData();
            }
            
            protected List<FileObject> listFiles(final FileObject fileObject) {
                while (true) {
                    while (true) {
                        try {
                            if (fileObject.getType().hasChildren()) {
                                final FileObject[] children = fileObject.getChildren();
                                if (children != null) {
                                    return Arrays.asList(children);
                                }
                                return new ArrayList<FileObject>();
                            }
                        }
                        catch (FileSystemException ex) {
                            throw new RuntimeException((Throwable)ex);
                        }
                        final FileObject[] children = null;
                        continue;
                    }
                }
            }
        }
    }
    
    public static class File implements Vfs.File
    {
        private final FileObject file;
        private final FileObject root;
        
        public File(final FileObject root, final FileObject file) {
            this.root = root;
            this.file = file;
        }
        
        @Override
        public String getName() {
            return this.file.getName().getBaseName();
        }
        
        @Override
        public String getRelativePath() {
            final String replace = this.file.getName().getPath().replace("\\", "/");
            if (replace.startsWith(this.root.getName().getPath())) {
                return replace.substring(this.root.getName().getPath().length() + 1);
            }
            return null;
        }
        
        @Override
        public InputStream openInputStream() throws IOException {
            return this.file.getContent().getInputStream();
        }
    }
}
