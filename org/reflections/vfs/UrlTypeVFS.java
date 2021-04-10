package org.reflections.vfs;

import com.google.common.base.*;
import java.net.*;
import java.util.jar.*;
import java.io.*;
import java.util.regex.*;
import org.reflections.*;

public class UrlTypeVFS implements UrlType
{
    public static final String[] REPLACE_EXTENSION;
    final String VFSFILE;
    final String VFSZIP;
    Predicate<java.io.File> realFile;
    
    static {
        REPLACE_EXTENSION = new String[] { ".ear/", ".jar/", ".war/", ".sar/", ".har/", ".par/" };
    }
    
    public UrlTypeVFS() {
        this.VFSZIP = "vfszip";
        this.VFSFILE = "vfsfile";
        this.realFile = (Predicate<java.io.File>)new Predicate<java.io.File>() {
            public boolean apply(final java.io.File file) {
                return file.exists() && file.isFile();
            }
        };
    }
    
    public URL adaptURL(final URL url) throws MalformedURLException {
        if ("vfszip".equals(url.getProtocol())) {
            return this.replaceZipSeparators(url.getPath(), this.realFile);
        }
        if ("vfsfile".equals(url.getProtocol())) {
            return new URL(url.toString().replace("vfsfile", "file"));
        }
        return url;
    }
    
    @Override
    public Dir createDir(final URL url) {
        try {
            return new ZipDir(new JarFile(this.adaptURL(url).getFile()));
        }
        catch (Exception ex) {
            if (Reflections.log != null) {
                Reflections.log.warn("Could not get URL", (Throwable)ex);
            }
            try {
                return new ZipDir(new JarFile(url.getFile()));
            }
            catch (IOException ex2) {
                if (Reflections.log != null) {
                    Reflections.log.warn("Could not get URL", (Throwable)ex2);
                }
                return null;
            }
        }
    }
    
    int findFirstMatchOfDeployableExtention(final String s, final int n) {
        final Matcher matcher = Pattern.compile("\\.[ejprw]ar/").matcher(s);
        if (matcher.find(n)) {
            return matcher.end();
        }
        return -1;
    }
    
    @Override
    public boolean matches(final URL url) {
        return "vfszip".equals(url.getProtocol()) || "vfsfile".equals(url.getProtocol());
    }
    
    URL replaceZipSeparatorStartingFrom(String s, int n) throws MalformedURLException {
        final int n2 = 0;
        final String substring = s.substring(0, n - 1);
        s = s.substring(n);
        final String[] replace_EXTENSION = UrlTypeVFS.REPLACE_EXTENSION;
        final int length = replace_EXTENSION.length;
        n = 1;
        for (int i = 0; i < length; ++i) {
            StringBuilder sb;
            for (String s2 = replace_EXTENSION[i]; s.contains(s2); s = s.replace(s2, sb.toString()), ++n) {
                sb = new StringBuilder();
                sb.append(s2.substring(0, 4));
                sb.append("!");
            }
        }
        String string = "";
        for (int j = n2; j < n; ++j) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(string);
            sb2.append("zip:");
            string = sb2.toString();
        }
        if (s.trim().length() == 0) {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append(string);
            sb3.append("/");
            sb3.append(substring);
            return new URL(sb3.toString());
        }
        final StringBuilder sb4 = new StringBuilder();
        sb4.append(string);
        sb4.append("/");
        sb4.append(substring);
        sb4.append("!");
        sb4.append(s);
        return new URL(sb4.toString());
    }
    
    URL replaceZipSeparators(final String s, final Predicate<java.io.File> predicate) throws MalformedURLException {
        int firstMatchOfDeployableExtention;
        for (int i = 0; i != -1; i = firstMatchOfDeployableExtention) {
            firstMatchOfDeployableExtention = this.findFirstMatchOfDeployableExtention(s, i);
            if ((i = firstMatchOfDeployableExtention) > 0) {
                if (predicate.apply((Object)new java.io.File(s.substring(0, firstMatchOfDeployableExtention - 1)))) {
                    return this.replaceZipSeparatorStartingFrom(s, firstMatchOfDeployableExtention);
                }
            }
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Unable to identify the real zip file in path '");
        sb.append(s);
        sb.append("'.");
        throw new ReflectionsException(sb.toString());
    }
}
