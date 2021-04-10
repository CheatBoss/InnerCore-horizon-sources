package org.mozilla.javascript.commonjs.module.provider;

import java.io.*;
import java.util.*;

public final class ParsedContentType implements Serializable
{
    private static final long serialVersionUID = 1L;
    private final String contentType;
    private final String encoding;
    
    public ParsedContentType(String substring) {
        final String s = null;
        final String s2 = null;
        String contentType = s;
        String substring2 = s2;
        if (substring != null) {
            final StringTokenizer stringTokenizer = new StringTokenizer(substring, ";");
            contentType = s;
            substring2 = s2;
            if (stringTokenizer.hasMoreTokens()) {
                final String trim = stringTokenizer.nextToken().trim();
                while (true) {
                    contentType = trim;
                    substring2 = s2;
                    if (!stringTokenizer.hasMoreTokens()) {
                        break;
                    }
                    final String trim2 = stringTokenizer.nextToken().trim();
                    if (!trim2.startsWith("charset=")) {
                        continue;
                    }
                    final String trim3 = trim2.substring(8).trim();
                    final int length = trim3.length();
                    contentType = trim;
                    substring2 = trim3;
                    if (length <= 0) {
                        break;
                    }
                    substring = trim3;
                    if (trim3.charAt(0) == '\"') {
                        substring = trim3.substring(1);
                    }
                    contentType = trim;
                    substring2 = substring;
                    if (substring.charAt(length - 1) == '\"') {
                        substring2 = substring.substring(0, length - 1);
                        contentType = trim;
                        break;
                    }
                    break;
                }
            }
        }
        this.contentType = contentType;
        this.encoding = substring2;
    }
    
    public String getContentType() {
        return this.contentType;
    }
    
    public String getEncoding() {
        return this.encoding;
    }
}
