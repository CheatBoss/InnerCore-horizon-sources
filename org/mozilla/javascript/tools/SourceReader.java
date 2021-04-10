package org.mozilla.javascript.tools;

import org.mozilla.javascript.commonjs.module.provider.*;
import org.mozilla.javascript.*;
import java.io.*;
import java.net.*;

public class SourceReader
{
    public static Object readFileOrUrl(String s, final boolean b, final String s2) throws IOException {
        final URL url = toUrl(s);
        Object o = null;
        final InputStream inputStream = null;
        final String s3 = null;
        Object o2 = null;
        Object o3 = null;
    Label_0561:
        while (true) {
            while (true) {
                Object o4 = null;
                Label_0111: {
                    if (url != null) {
                        break Label_0111;
                    }
                    o4 = o;
                    try {
                        final File file = new File(s);
                        o4 = null;
                        o2 = inputStream;
                        o = s3;
                        o = o4;
                        try {
                            final int n = (int)file.length();
                            o2 = inputStream;
                            o = s3;
                            o = o4;
                            o4 = new FileInputStream(file);
                        }
                        finally {}
                    }
                    finally {
                        s = (String)o4;
                        break Label_0561;
                    }
                }
                final URLConnection openConnection = url.openConnection();
                s = (String)openConnection.getInputStream();
                Serializable encoding = null;
                Label_0188: {
                    if (b) {
                        encoding = new ParsedContentType(openConnection.getContentType());
                        o4 = s;
                        final String contentType = ((ParsedContentType)encoding).getContentType();
                        try {
                            encoding = ((ParsedContentType)encoding).getEncoding();
                            o4 = contentType;
                            break Label_0188;
                        }
                        finally {
                            break Label_0561;
                        }
                    }
                    encoding = null;
                    o4 = o2;
                }
                final int contentLength = openConnection.getContentLength();
                o2 = s;
                o3 = o4;
                o = encoding;
                if (contentLength > 1048576) {
                    o2 = s;
                    o3 = o4;
                    o = encoding;
                    continue;
                }
                continue;
            }
            if (s != null) {
                ((InputStream)s).close();
            }
            final int n2 = 4096;
            try {
                final byte[] stream = Kit.readStream((InputStream)o2, n2);
                if (o2 != null) {
                    ((InputStream)o2).close();
                }
                if (!b) {
                    return stream;
                }
                if ((s = (String)o) == null) {
                    if (stream.length > 3 && stream[0] == -1 && stream[1] == -2 && stream[2] == 0 && stream[3] == 0) {
                        s = "UTF-32LE";
                    }
                    else if (stream.length > 3 && stream[0] == 0 && stream[1] == 0 && stream[2] == -2 && stream[3] == -1) {
                        s = "UTF-32BE";
                    }
                    else if (stream.length > 2 && stream[0] == -17 && stream[1] == -69 && stream[2] == -65) {
                        s = "UTF-8";
                    }
                    else if (stream.length > 1 && stream[0] == -1 && stream[1] == -2) {
                        s = "UTF-16LE";
                    }
                    else if (stream.length > 1 && stream[0] == -2 && stream[1] == -1) {
                        s = "UTF-16BE";
                    }
                    else {
                        final String s4;
                        if ((s = s4) == null) {
                            if (url == null) {
                                s = System.getProperty("file.encoding");
                            }
                            else if (o3 != null && ((String)o3).startsWith("application/")) {
                                s = "UTF-8";
                            }
                            else {
                                s = "US-ASCII";
                            }
                        }
                    }
                }
                final String s5 = new String(stream, s);
                if (s5.length() > 0 && s5.charAt(0) == '\ufeff') {
                    return s5.substring(1);
                }
                return s5;
            }
            finally {
                s = (String)o2;
            }
            continue Label_0561;
        }
    }
    
    public static URL toUrl(final String s) {
        if (s.indexOf(58) >= 2) {
            try {
                return new URL(s);
            }
            catch (MalformedURLException ex) {}
        }
        return null;
    }
}
