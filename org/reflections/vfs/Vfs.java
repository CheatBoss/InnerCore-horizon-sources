package org.reflections.vfs;

import com.google.common.base.*;
import java.util.function.*;
import java.util.*;
import com.google.common.collect.*;
import org.slf4j.*;
import org.reflections.*;
import javax.annotation.*;
import java.util.jar.*;
import java.net.*;
import org.reflections.util.*;
import java.io.*;

public abstract class Vfs
{
    private static List<UrlType> defaultUrlTypes;
    
    static {
        Vfs.defaultUrlTypes = (List<UrlType>)Lists.newArrayList((Object[])DefaultUrlTypes.values());
    }
    
    public static void addDefaultURLTypes(final UrlType urlType) {
        Vfs.defaultUrlTypes.add(0, urlType);
    }
    
    public static Iterable<File> findFiles(Collection<URL> iterable, final Predicate<File> predicate) {
        final ArrayList<File> list = new ArrayList<File>();
        final Iterator<URL> iterator = ((Collection<URL>)iterable).iterator();
        iterable = list;
        while (iterator.hasNext()) {
            final URL url = iterator.next();
            Iterable<File> concat;
            try {
                concat = (Iterable<File>)Iterables.concat((Iterable)iterable, Iterables.filter((Iterable)new Iterable<File>() {
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
                        return Vfs.fromURL(url).getFiles().iterator();
                    }
                    
                    @Override
                    public Spliterator<Object> spliterator() {
                        return (Spliterator<Object>)Iterable-CC.$default$spliterator((Iterable)this);
                    }
                }, (Predicate)predicate));
            }
            catch (Throwable t) {
                concat = iterable;
                if (Reflections.log != null) {
                    final Logger log = Reflections.log;
                    final StringBuilder sb = new StringBuilder();
                    sb.append("could not findFiles for url. continuing. [");
                    sb.append(url);
                    sb.append("]");
                    log.error(sb.toString(), t);
                    concat = iterable;
                }
            }
            iterable = concat;
        }
        return iterable;
    }
    
    public static Iterable<File> findFiles(final Collection<URL> collection, final String s, final Predicate<String> predicate) {
        return findFiles(collection, (Predicate<File>)new Predicate<File>() {
            public boolean apply(final File file) {
                final String relativePath = file.getRelativePath();
                final boolean startsWith = relativePath.startsWith(s);
                final boolean b = false;
                if (startsWith) {
                    final String substring = relativePath.substring(relativePath.indexOf(s) + s.length());
                    boolean b2 = b;
                    if (!Utils.isEmpty(substring)) {
                        b2 = b;
                        if (predicate.apply((Object)substring.substring(1))) {
                            b2 = true;
                        }
                    }
                    return b2;
                }
                return false;
            }
        });
    }
    
    public static Dir fromURL(final URL url) {
        return fromURL(url, Vfs.defaultUrlTypes);
    }
    
    public static Dir fromURL(final URL url, List<UrlType> iterator) {
        iterator = ((List<UrlType>)iterator).iterator();
        while (iterator.hasNext()) {
            final UrlType urlType = iterator.next();
            try {
                if (!urlType.matches(url)) {
                    continue;
                }
                final Dir dir = urlType.createDir(url);
                if (dir != null) {
                    return dir;
                }
                continue;
            }
            catch (Throwable t) {
                if (Reflections.log == null) {
                    continue;
                }
                final Logger log = Reflections.log;
                final StringBuilder sb = new StringBuilder();
                sb.append("could not create Dir using ");
                sb.append(urlType);
                sb.append(" from url ");
                sb.append(url.toExternalForm());
                sb.append(". skipping.");
                log.warn(sb.toString(), t);
            }
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("could not create Vfs.Dir from url, no matching UrlType was found [");
        sb2.append(url.toExternalForm());
        sb2.append("]\n");
        sb2.append("either use fromURL(final URL url, final List<UrlType> urlTypes) or ");
        sb2.append("use the static setDefaultURLTypes(final List<UrlType> urlTypes) or addDefaultURLTypes(UrlType urlType) ");
        sb2.append("with your specialized UrlType.");
        throw new ReflectionsException(sb2.toString());
    }
    
    public static Dir fromURL(final URL url, final UrlType... array) {
        return fromURL(url, Lists.newArrayList((Object[])array));
    }
    
    public static List<UrlType> getDefaultUrlTypes() {
        return Vfs.defaultUrlTypes;
    }
    
    @Nullable
    public static java.io.File getFile(final URL url) {
        try {
            final java.io.File file = new java.io.File(url.toURI().getSchemeSpecificPart());
            if (file.exists()) {
                return file;
            }
        }
        catch (URISyntaxException ex) {}
        try {
            String s2;
            final String s = s2 = URLDecoder.decode(url.getPath(), "UTF-8");
            if (s.contains(".jar!")) {
                s2 = s.substring(0, s.lastIndexOf(".jar!") + ".jar".length());
            }
            final java.io.File file2 = new java.io.File(s2);
            if (file2.exists()) {
                return file2;
            }
        }
        catch (UnsupportedEncodingException ex2) {}
        try {
            String s4;
            final String s3 = s4 = url.toExternalForm();
            if (s3.startsWith("jar:")) {
                s4 = s3.substring("jar:".length());
            }
            String substring = s4;
            if (s4.startsWith("wsjar:")) {
                substring = s4.substring("wsjar:".length());
            }
            String substring2 = substring;
            if (substring.startsWith("file:")) {
                substring2 = substring.substring("file:".length());
            }
            String substring3 = substring2;
            if (substring2.contains(".jar!")) {
                substring3 = substring2.substring(0, substring2.indexOf(".jar!") + ".jar".length());
            }
            final java.io.File file3 = new java.io.File(substring3);
            if (file3.exists()) {
                return file3;
            }
            final java.io.File file4 = new java.io.File(substring3.replace("%20", " "));
            if (file4.exists()) {
                return file4;
            }
        }
        catch (Exception ex3) {}
        return null;
    }
    
    public static void setDefaultURLTypes(final List<UrlType> defaultUrlTypes) {
        Vfs.defaultUrlTypes = defaultUrlTypes;
    }
    
    public enum DefaultUrlTypes implements UrlType
    {
        bundle {
            @Override
            public Dir createDir(final URL url) throws Exception {
                return Vfs.fromURL((URL)ClasspathHelper.contextClassLoader().loadClass("org.eclipse.core.runtime.FileLocator").getMethod("resolve", URL.class).invoke(null, url));
            }
            
            @Override
            public boolean matches(final URL url) throws Exception {
                return url.getProtocol().startsWith("bundle");
            }
        }, 
        directory {
            @Override
            public Dir createDir(final URL url) throws Exception {
                return new SystemDir(Vfs.getFile(url));
            }
            
            @Override
            public boolean matches(final URL url) {
                return url.getProtocol().equals("file") && !url.toExternalForm().contains(".jar") && Vfs.getFile(url).isDirectory();
            }
        }, 
        jarFile {
            @Override
            public Dir createDir(final URL url) throws Exception {
                return new ZipDir(new JarFile(Vfs.getFile(url)));
            }
            
            @Override
            public boolean matches(final URL url) {
                return url.getProtocol().equals("file") && url.toExternalForm().contains(".jar");
            }
        }, 
        jarInputStream {
            @Override
            public Dir createDir(final URL url) throws Exception {
                return new JarInputDir(url);
            }
            
            @Override
            public boolean matches(final URL url) throws Exception {
                return url.toExternalForm().contains(".jar");
            }
        }, 
        jarUrl {
            @Override
            public Dir createDir(final URL url) throws Exception {
                try {
                    final URLConnection openConnection = url.openConnection();
                    if (openConnection instanceof JarURLConnection) {
                        return new ZipDir(((JarURLConnection)openConnection).getJarFile());
                    }
                }
                catch (Throwable t) {}
                final java.io.File file = Vfs.getFile(url);
                if (file != null) {
                    return new ZipDir(new JarFile(file));
                }
                return null;
            }
            
            @Override
            public boolean matches(final URL url) {
                return "jar".equals(url.getProtocol()) || "zip".equals(url.getProtocol()) || "wsjar".equals(url.getProtocol());
            }
        }, 
        jboss_vfs {
            @Override
            public Dir createDir(final URL url) throws Exception {
                final Object content = url.openConnection().getContent();
                final Class<?> loadClass = ClasspathHelper.contextClassLoader().loadClass("org.jboss.vfs.VirtualFile");
                final java.io.File file = (java.io.File)loadClass.getMethod("getPhysicalFile", (Class<?>[])new Class[0]).invoke(content, new Object[0]);
                final java.io.File file2 = new java.io.File(file.getParentFile(), (String)loadClass.getMethod("getName", (Class<?>[])new Class[0]).invoke(content, new Object[0]));
                java.io.File file3 = null;
                Label_0092: {
                    if (file2.exists()) {
                        file3 = file2;
                        if (file2.canRead()) {
                            break Label_0092;
                        }
                    }
                    file3 = file;
                }
                if (file3.isDirectory()) {
                    return new SystemDir(file3);
                }
                return new ZipDir(new JarFile(file3));
            }
            
            @Override
            public boolean matches(final URL url) {
                return url.getProtocol().equals("vfs");
            }
        }, 
        jboss_vfsfile {
            @Override
            public Dir createDir(final URL url) throws Exception {
                return new UrlTypeVFS().createDir(url);
            }
            
            @Override
            public boolean matches(final URL url) throws Exception {
                return "vfszip".equals(url.getProtocol()) || "vfsfile".equals(url.getProtocol());
            }
        };
    }
    
    public interface Dir
    {
        void close();
        
        Iterable<File> getFiles();
        
        String getPath();
    }
    
    public interface File
    {
        String getName();
        
        String getRelativePath();
        
        InputStream openInputStream() throws IOException;
    }
    
    public interface UrlType
    {
        Dir createDir(final URL p0) throws Exception;
        
        boolean matches(final URL p0) throws Exception;
    }
}
