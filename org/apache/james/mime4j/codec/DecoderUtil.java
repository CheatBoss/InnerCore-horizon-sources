package org.apache.james.mime4j.codec;

import org.apache.commons.logging.*;
import java.io.*;
import org.apache.james.mime4j.util.*;

public class DecoderUtil
{
    private static Log log;
    
    static {
        DecoderUtil.log = LogFactory.getLog((Class)DecoderUtil.class);
    }
    
    public static String decodeB(final String s, final String s2) throws UnsupportedEncodingException {
        return new String(decodeBase64(s), s2);
    }
    
    public static byte[] decodeBase64(final String s) {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            final Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(s.getBytes("US-ASCII")));
            while (true) {
                final int read = base64InputStream.read();
                if (read == -1) {
                    break;
                }
                byteArrayOutputStream.write(read);
            }
        }
        catch (IOException ex) {
            DecoderUtil.log.error((Object)ex);
        }
        return byteArrayOutputStream.toByteArray();
    }
    
    public static byte[] decodeBaseQuotedPrintable(final String s) {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            final QuotedPrintableInputStream quotedPrintableInputStream = new QuotedPrintableInputStream(new ByteArrayInputStream(s.getBytes("US-ASCII")));
            while (true) {
                final int read = quotedPrintableInputStream.read();
                if (read == -1) {
                    break;
                }
                byteArrayOutputStream.write(read);
            }
        }
        catch (IOException ex) {
            DecoderUtil.log.error((Object)ex);
        }
        return byteArrayOutputStream.toByteArray();
    }
    
    private static String decodeEncodedWord(final String s, final int n, final int n2) {
        final int n3 = n + 2;
        final int index = s.indexOf(63, n3);
        final int n4 = n2 - 2;
        if (index == n4) {
            return null;
        }
        final int n5 = index + 1;
        final int index2 = s.indexOf(63, n5);
        if (index2 == n4) {
            return null;
        }
        final String substring = s.substring(n3, index);
        final String substring2 = s.substring(n5, index2);
        final String substring3 = s.substring(index2 + 1, n4);
        final String javaCharset = CharsetUtil.toJavaCharset(substring);
        if (javaCharset == null) {
            if (DecoderUtil.log.isWarnEnabled()) {
                final Log log = DecoderUtil.log;
                final StringBuilder sb = new StringBuilder();
                sb.append("MIME charset '");
                sb.append(substring);
                sb.append("' in encoded word '");
                sb.append(s.substring(n, n2));
                sb.append("' doesn't have a ");
                sb.append("corresponding Java charset");
                log.warn((Object)sb.toString());
            }
            return null;
        }
        if (!CharsetUtil.isDecodingSupported(javaCharset)) {
            if (DecoderUtil.log.isWarnEnabled()) {
                final Log log2 = DecoderUtil.log;
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Current JDK doesn't support decoding of charset '");
                sb2.append(javaCharset);
                sb2.append("' (MIME charset '");
                sb2.append(substring);
                sb2.append("' in encoded word '");
                sb2.append(s.substring(n, n2));
                sb2.append("')");
                log2.warn((Object)sb2.toString());
            }
            return null;
        }
        if (substring3.length() == 0) {
            if (DecoderUtil.log.isWarnEnabled()) {
                final Log log3 = DecoderUtil.log;
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("Missing encoded text in encoded word: '");
                sb3.append(s.substring(n, n2));
                sb3.append("'");
                log3.warn((Object)sb3.toString());
            }
            return null;
        }
        try {
            if (substring2.equalsIgnoreCase("Q")) {
                return decodeQ(substring3, javaCharset);
            }
            if (substring2.equalsIgnoreCase("B")) {
                return decodeB(substring3, javaCharset);
            }
            if (DecoderUtil.log.isWarnEnabled()) {
                final Log log4 = DecoderUtil.log;
                final StringBuilder sb4 = new StringBuilder();
                sb4.append("Warning: Unknown encoding in encoded word '");
                sb4.append(s.substring(n, n2));
                sb4.append("'");
                log4.warn((Object)sb4.toString());
            }
            return null;
        }
        catch (RuntimeException ex) {
            if (DecoderUtil.log.isWarnEnabled()) {
                final Log log5 = DecoderUtil.log;
                final StringBuilder sb5 = new StringBuilder();
                sb5.append("Could not decode encoded word '");
                sb5.append(s.substring(n, n2));
                sb5.append("'");
                log5.warn((Object)sb5.toString(), (Throwable)ex);
            }
            return null;
        }
        catch (UnsupportedEncodingException ex2) {
            if (DecoderUtil.log.isWarnEnabled()) {
                final Log log6 = DecoderUtil.log;
                final StringBuilder sb6 = new StringBuilder();
                sb6.append("Unsupported encoding in encoded word '");
                sb6.append(s.substring(n, n2));
                sb6.append("'");
                log6.warn((Object)sb6.toString(), (Throwable)ex2);
            }
            return null;
        }
    }
    
    public static String decodeEncodedWords(final String s) {
        final StringBuilder sb = new StringBuilder();
        int n = 0;
        int n2 = 0;
        while (true) {
            final int index = s.indexOf("=?", n);
            int index2;
            if (index == -1) {
                index2 = -1;
            }
            else {
                index2 = s.indexOf("?=", index + 2);
            }
            if (index2 == -1) {
                break;
            }
            final int n3 = index2 + 2;
            final String substring = s.substring(n, index);
            final String decodeEncodedWord = decodeEncodedWord(s, index, n3);
            if (decodeEncodedWord == null) {
                sb.append(substring);
                sb.append(s.substring(index, n3));
            }
            else {
                if (n2 == 0 || !CharsetUtil.isWhitespace(substring)) {
                    sb.append(substring);
                }
                sb.append(decodeEncodedWord);
            }
            if (decodeEncodedWord != null) {
                n2 = 1;
            }
            else {
                n2 = 0;
            }
            n = n3;
        }
        if (n == 0) {
            return s;
        }
        sb.append(s.substring(n));
        return sb.toString();
    }
    
    public static String decodeQ(final String s, final String s2) throws UnsupportedEncodingException {
        final StringBuilder sb = new StringBuilder(128);
        for (int i = 0; i < s.length(); ++i) {
            final char char1 = s.charAt(i);
            if (char1 == '_') {
                sb.append("=20");
            }
            else {
                sb.append(char1);
            }
        }
        return new String(decodeBaseQuotedPrintable(sb.toString()), s2);
    }
}
