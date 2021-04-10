package org.reflections.util;

import org.reflections.*;
import java.net.*;
import com.google.common.collect.*;
import java.io.*;
import java.util.jar.*;
import java.util.*;
import org.slf4j.*;
import javax.servlet.*;

public abstract class ClasspathHelper
{
    public static ClassLoader[] classLoaders(final ClassLoader... array) {
        if (array != null && array.length != 0) {
            return array;
        }
        final ClassLoader contextClassLoader = contextClassLoader();
        final ClassLoader staticClassLoader = staticClassLoader();
        if (contextClassLoader == null) {
            return new ClassLoader[0];
        }
        if (staticClassLoader != null && contextClassLoader != staticClassLoader) {
            return new ClassLoader[] { contextClassLoader, staticClassLoader };
        }
        return new ClassLoader[] { contextClassLoader };
    }
    
    public static String cleanPath(URL url) {
        url = (URL)url.getPath();
        try {
            url = (URL)URLDecoder.decode((String)url, "UTF-8");
        }
        catch (UnsupportedEncodingException ex) {}
        Serializable substring = url;
        if (((String)url).startsWith("jar:")) {
            substring = ((String)url).substring("jar:".length());
        }
        Serializable substring2 = substring;
        if (((String)substring).startsWith("file:")) {
            substring2 = ((String)substring).substring("file:".length());
        }
        String string = (String)substring2;
        if (((String)substring2).endsWith("!/")) {
            final StringBuilder sb = new StringBuilder();
            sb.append(((String)substring2).substring(0, ((String)substring2).lastIndexOf("!/")));
            sb.append("/");
            string = sb.toString();
        }
        return string;
    }
    
    public static ClassLoader contextClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }
    
    private static Collection<URL> distinctUrls(final Collection<URL> collection) {
        final HashMap<String, URL> hashMap = new HashMap<String, URL>(collection.size());
        for (final URL url : collection) {
            hashMap.put(url.toExternalForm(), url);
        }
        return hashMap.values();
    }
    
    public static URL forClass(final Class<?> clazz, ClassLoader... classLoaders) {
        classLoaders = classLoaders(classLoaders);
        final StringBuilder sb = new StringBuilder();
        sb.append(clazz.getName().replace(".", "/"));
        sb.append(".class");
        final String string = sb.toString();
        for (int length = classLoaders.length, i = 0; i < length; ++i) {
            final ClassLoader classLoader = classLoaders[i];
            try {
                final URL resource = classLoader.getResource(string);
                if (resource != null) {
                    return new URL(resource.toExternalForm().substring(0, resource.toExternalForm().lastIndexOf(clazz.getPackage().getName().replace(".", "/"))));
                }
            }
            catch (MalformedURLException ex) {
                if (Reflections.log != null) {
                    Reflections.log.warn("Could not get URL", (Throwable)ex);
                }
            }
        }
        return null;
    }
    
    public static Collection<URL> forClassLoader() {
        return forClassLoader(classLoaders(new ClassLoader[0]));
    }
    
    public static Collection<URL> forClassLoader(final ClassLoader... array) {
        final ArrayList<URL> list = new ArrayList<URL>();
        final ClassLoader[] classLoaders = classLoaders(array);
        for (int length = classLoaders.length, i = 0; i < length; ++i) {
            for (ClassLoader parent = classLoaders[i]; parent != null; parent = parent.getParent()) {
                if (parent instanceof URLClassLoader) {
                    final URL[] urLs = ((URLClassLoader)parent).getURLs();
                    if (urLs != null) {
                        list.addAll((Collection<?>)Sets.newHashSet((Object[])urLs));
                    }
                }
            }
        }
        return distinctUrls(list);
    }
    
    public static Collection<URL> forJavaClassPath() {
        final ArrayList<URL> list = new ArrayList<URL>();
        final String property = System.getProperty("java.class.path");
        if (property != null) {
            final String[] split = property.split(File.pathSeparator);
            for (int length = split.length, i = 0; i < length; ++i) {
                final String s = split[i];
                try {
                    list.add(new File(s).toURI().toURL());
                }
                catch (Exception ex) {
                    if (Reflections.log != null) {
                        Reflections.log.warn("Could not get URL", (Throwable)ex);
                    }
                }
            }
        }
        return distinctUrls(list);
    }
    
    public static Collection<URL> forManifest() {
        return forManifest(forClassLoader());
    }
    
    public static Collection<URL> forManifest(final Iterable<URL> iterable) {
        final ArrayList<URL> list = new ArrayList<URL>();
        final Iterator<URL> iterator = iterable.iterator();
        while (iterator.hasNext()) {
            list.addAll((Collection<?>)forManifest(iterator.next()));
        }
        return distinctUrls(list);
    }
    
    public static Collection<URL> forManifest(final URL url) {
        final ArrayList<URL> list = new ArrayList<URL>();
        list.add(url);
        try {
            final String cleanPath = cleanPath(url);
            final File file = new File(cleanPath);
            final JarFile jarFile = new JarFile(cleanPath);
            final URL tryToGetValidUrl = tryToGetValidUrl(file.getPath(), new File(cleanPath).getParent(), cleanPath);
            if (tryToGetValidUrl != null) {
                list.add(tryToGetValidUrl);
            }
            final Manifest manifest = jarFile.getManifest();
            if (manifest != null) {
                final String value = manifest.getMainAttributes().getValue(new Attributes.Name("Class-Path"));
                if (value != null) {
                    final String[] split = value.split(" ");
                    for (int length = split.length, i = 0; i < length; ++i) {
                        final URL tryToGetValidUrl2 = tryToGetValidUrl(file.getPath(), new File(cleanPath).getParent(), split[i]);
                        if (tryToGetValidUrl2 != null) {
                            list.add(tryToGetValidUrl2);
                        }
                    }
                }
            }
        }
        catch (IOException ex) {}
        return distinctUrls(list);
    }
    
    public static Collection<URL> forPackage(final String s, final ClassLoader... array) {
        return forResource(resourceName(s), array);
    }
    
    public static Collection<URL> forResource(final String s, ClassLoader... classLoaders) {
        final ArrayList<URL> list = new ArrayList<URL>();
        classLoaders = classLoaders(classLoaders);
        for (int length = classLoaders.length, i = 0; i < length; ++i) {
            final ClassLoader classLoader = classLoaders[i];
            try {
                final Enumeration<URL> resources = classLoader.getResources(s);
                while (resources.hasMoreElements()) {
                    final URL url = resources.nextElement();
                    final int lastIndex = url.toExternalForm().lastIndexOf(s);
                    if (lastIndex != -1) {
                        list.add(new URL(url.toExternalForm().substring(0, lastIndex)));
                    }
                    else {
                        list.add(url);
                    }
                }
            }
            catch (IOException ex) {
                if (Reflections.log != null) {
                    final Logger log = Reflections.log;
                    final StringBuilder sb = new StringBuilder();
                    sb.append("error getting resources for ");
                    sb.append(s);
                    log.error(sb.toString(), (Throwable)ex);
                }
            }
        }
        return distinctUrls(list);
    }
    
    public static URL forWebInfClasses(final ServletContext servletContext) {
        try {
            final String realPath = servletContext.getRealPath("/WEB-INF/classes");
            if (realPath == null) {
                return servletContext.getResource("/WEB-INF/classes");
            }
            final File file = new File(realPath);
            if (file.exists()) {
                return file.toURL();
            }
        }
        catch (MalformedURLException ex) {}
        return null;
    }
    
    public static Collection<URL> forWebInfLib(final ServletContext servletContext) {
        final ArrayList<URL> list = new ArrayList<URL>();
        for (final String next : servletContext.getResourcePaths("/WEB-INF/lib")) {
            try {
                list.add(servletContext.getResource((String)next));
            }
            catch (MalformedURLException ex) {}
        }
        return distinctUrls(list);
    }
    
    private static String resourceName(String s) {
        if (s != null) {
            final String s2 = s = s.replace(".", "/").replace("\\", "/");
            if (s2.startsWith("/")) {
                s = s2.substring(1);
            }
            return s;
        }
        return null;
    }
    
    public static ClassLoader staticClassLoader() {
        return Reflections.class.getClassLoader();
    }
    
    static URL tryToGetValidUrl(final String s, final String s2, final String s3) {
        try {
            if (new File(s3).exists()) {
                return new File(s3).toURI().toURL();
            }
            final StringBuilder sb = new StringBuilder();
            sb.append(s2);
            sb.append(File.separator);
            sb.append(s3);
            if (new File(sb.toString()).exists()) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append(s2);
                sb2.append(File.separator);
                sb2.append(s3);
                return new File(sb2.toString()).toURI().toURL();
            }
            final StringBuilder sb3 = new StringBuilder();
            sb3.append(s);
            sb3.append(File.separator);
            sb3.append(s3);
            if (new File(sb3.toString()).exists()) {
                final StringBuilder sb4 = new StringBuilder();
                sb4.append(s);
                sb4.append(File.separator);
                sb4.append(s3);
                return new File(sb4.toString()).toURI().toURL();
            }
            if (new File(new URL(s3).getFile()).exists()) {
                return new File(new URL(s3).getFile()).toURI().toURL();
            }
        }
        catch (MalformedURLException ex) {}
        return null;
    }
}
