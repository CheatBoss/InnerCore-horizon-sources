package org.apache.james.mime4j.util;

import org.apache.commons.logging.*;
import java.util.*;
import java.text.*;

public final class MimeUtil
{
    public static final String ENC_7BIT = "7bit";
    public static final String ENC_8BIT = "8bit";
    public static final String ENC_BASE64 = "base64";
    public static final String ENC_BINARY = "binary";
    public static final String ENC_QUOTED_PRINTABLE = "quoted-printable";
    public static final String MIME_HEADER_CONTENT_DESCRIPTION = "content-description";
    public static final String MIME_HEADER_CONTENT_DISPOSITION = "content-disposition";
    public static final String MIME_HEADER_CONTENT_ID = "content-id";
    public static final String MIME_HEADER_LANGAUGE = "content-language";
    public static final String MIME_HEADER_LOCATION = "content-location";
    public static final String MIME_HEADER_MD5 = "content-md5";
    public static final String MIME_HEADER_MIME_VERSION = "mime-version";
    public static final String PARAM_CREATION_DATE = "creation-date";
    public static final String PARAM_FILENAME = "filename";
    public static final String PARAM_MODIFICATION_DATE = "modification-date";
    public static final String PARAM_READ_DATE = "read-date";
    public static final String PARAM_SIZE = "size";
    private static final ThreadLocal<DateFormat> RFC822_DATE_FORMAT;
    private static int counter;
    private static final Log log;
    private static final Random random;
    
    static {
        log = LogFactory.getLog((Class)MimeUtil.class);
        random = new Random();
        MimeUtil.counter = 0;
        RFC822_DATE_FORMAT = new ThreadLocal<DateFormat>() {
            @Override
            protected DateFormat initialValue() {
                return new Rfc822DateFormat();
            }
        };
    }
    
    private MimeUtil() {
    }
    
    public static String createUniqueBoundary() {
        final StringBuilder sb = new StringBuilder();
        sb.append("-=Part.");
        sb.append(Integer.toHexString(nextCounterValue()));
        sb.append('.');
        sb.append(Long.toHexString(MimeUtil.random.nextLong()));
        sb.append('.');
        sb.append(Long.toHexString(System.currentTimeMillis()));
        sb.append('.');
        sb.append(Long.toHexString(MimeUtil.random.nextLong()));
        sb.append("=-");
        return sb.toString();
    }
    
    public static String createUniqueMessageId(final String s) {
        final StringBuilder sb = new StringBuilder("<Mime4j.");
        sb.append(Integer.toHexString(nextCounterValue()));
        sb.append('.');
        sb.append(Long.toHexString(MimeUtil.random.nextLong()));
        sb.append('.');
        sb.append(Long.toHexString(System.currentTimeMillis()));
        if (s != null) {
            sb.append('@');
            sb.append(s);
        }
        sb.append('>');
        return sb.toString();
    }
    
    public static String fold(final String s, int i) {
        final int length = s.length();
        if (i + length <= 76) {
            return s;
        }
        final StringBuilder sb = new StringBuilder();
        int n = -i;
        int indexOfWsp;
        int n2;
        for (i = indexOfWsp(s, 0); i != length; i = indexOfWsp, n = n2) {
            indexOfWsp = indexOfWsp(s, i + 1);
            n2 = n;
            if (indexOfWsp - n > 76) {
                sb.append(s.substring(Math.max(0, n), i));
                sb.append("\r\n");
                n2 = i;
            }
        }
        sb.append(s.substring(Math.max(0, n)));
        return sb.toString();
    }
    
    public static String formatDate(final Date date, final TimeZone timeZone) {
        final DateFormat dateFormat = MimeUtil.RFC822_DATE_FORMAT.get();
        TimeZone default1 = timeZone;
        if (timeZone == null) {
            default1 = TimeZone.getDefault();
        }
        dateFormat.setTimeZone(default1);
        return dateFormat.format(date);
    }
    
    public static Map<String, String> getHeaderParams(String unfold) {
        unfold = unfold(unfold.trim());
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        String s;
        if (unfold.indexOf(";") == -1) {
            s = null;
        }
        else {
            final String substring = unfold.substring(0, unfold.indexOf(";"));
            final String substring2 = unfold.substring(substring.length() + 1);
            unfold = substring;
            s = substring2;
        }
        hashMap.put("", unfold);
        if (s != null) {
            final char[] charArray = s.toCharArray();
            final StringBuilder sb = new StringBuilder(64);
            final StringBuilder sb2 = new StringBuilder(64);
            final int length = charArray.length;
            int i = 0;
            int n = 0;
            int n2 = 0;
            while (i < length) {
                final char c = charArray[i];
                Label_0530: {
                    Label_0512: {
                        Label_0498: {
                            Label_0481: {
                                if (n != 0) {
                                    if (n != 1) {
                                        int n4 = 0;
                                        Label_0456: {
                                            Label_0451: {
                                                while (true) {
                                                    int n3 = 0;
                                                    Label_0338: {
                                                        int n5;
                                                        if (n != 2) {
                                                            if (n == 3) {
                                                                n3 = n;
                                                                break Label_0338;
                                                            }
                                                            if (n == 4) {
                                                                if (c != '\"') {
                                                                    if (c == '\\') {
                                                                        if (n2 != 0) {
                                                                            sb2.append('\\');
                                                                        }
                                                                        n4 = (n2 ^ 0x1);
                                                                        break Label_0456;
                                                                    }
                                                                    if (n2 != 0) {
                                                                        sb2.append('\\');
                                                                    }
                                                                }
                                                                else if (n2 == 0) {
                                                                    hashMap.put(sb.toString().trim().toLowerCase(), sb2.toString());
                                                                    n = 5;
                                                                    break Label_0530;
                                                                }
                                                                sb2.append(c);
                                                                n2 = 0;
                                                                break Label_0530;
                                                            }
                                                            if ((n5 = n) != 5) {
                                                                if (n != 99) {
                                                                    n4 = n2;
                                                                    break Label_0456;
                                                                }
                                                                n4 = n2;
                                                                if (c == ';') {
                                                                    break Label_0451;
                                                                }
                                                                break Label_0456;
                                                            }
                                                        }
                                                        else {
                                                            boolean b = false;
                                                            Label_0324: {
                                                                if (c != '\t' && c != ' ') {
                                                                    if (c != '\"') {
                                                                        n = 3;
                                                                        b = true;
                                                                        break Label_0324;
                                                                    }
                                                                    n = 4;
                                                                }
                                                                b = false;
                                                            }
                                                            n3 = n;
                                                            if (!b) {
                                                                n4 = n2;
                                                                break Label_0456;
                                                            }
                                                            break Label_0338;
                                                        }
                                                        n = n5;
                                                        n4 = n2;
                                                        if (c == '\t') {
                                                            break Label_0456;
                                                        }
                                                        n = n5;
                                                        n4 = n2;
                                                        if (c == ' ') {
                                                            break Label_0456;
                                                        }
                                                        if (c != ';') {
                                                            break Label_0481;
                                                        }
                                                        break Label_0451;
                                                    }
                                                    boolean b2;
                                                    if (c != '\t' && c != ' ' && c != ';') {
                                                        sb2.append(c);
                                                        b2 = false;
                                                        n = n3;
                                                    }
                                                    else {
                                                        hashMap.put(sb.toString().trim().toLowerCase(), sb2.toString().trim());
                                                        b2 = true;
                                                        n = 5;
                                                    }
                                                    int n5 = n;
                                                    if (!b2) {
                                                        n4 = n2;
                                                        break Label_0456;
                                                    }
                                                    continue;
                                                }
                                            }
                                            n = 0;
                                            break Label_0530;
                                        }
                                        n2 = n4;
                                        break Label_0530;
                                    }
                                    break Label_0498;
                                }
                                else {
                                    if (c != '=') {
                                        sb.setLength(0);
                                        sb2.setLength(0);
                                        n = 1;
                                        break Label_0498;
                                    }
                                    MimeUtil.log.error((Object)"Expected header param name, got '='");
                                }
                            }
                            break Label_0512;
                        }
                        if (c != '=') {
                            sb.append(c);
                            break Label_0530;
                        }
                        if (sb.length() != 0) {
                            n = 2;
                            break Label_0530;
                        }
                    }
                    n = 99;
                }
                ++i;
            }
            if (n == 3) {
                hashMap.put(sb.toString().trim().toLowerCase(), sb2.toString().trim());
            }
        }
        return hashMap;
    }
    
    private static int indexOfWsp(final String s, int i) {
        int length;
        for (length = s.length(); i < length; ++i) {
            final char char1 = s.charAt(i);
            if (char1 == ' ') {
                return i;
            }
            if (char1 == '\t') {
                return i;
            }
        }
        return length;
    }
    
    public static boolean isBase64Encoding(final String s) {
        return "base64".equalsIgnoreCase(s);
    }
    
    public static boolean isMessage(final String s) {
        return s != null && s.equalsIgnoreCase("message/rfc822");
    }
    
    public static boolean isMultipart(final String s) {
        return s != null && s.toLowerCase().startsWith("multipart/");
    }
    
    public static boolean isQuotedPrintableEncoded(final String s) {
        return "quoted-printable".equalsIgnoreCase(s);
    }
    
    public static boolean isSameMimeType(final String s, final String s2) {
        return s != null && s2 != null && s.equalsIgnoreCase(s2);
    }
    
    private static int nextCounterValue() {
        synchronized (MimeUtil.class) {
            final int counter = MimeUtil.counter;
            MimeUtil.counter = counter + 1;
            return counter;
        }
    }
    
    public static String unfold(final String s) {
        final int length = s.length();
        int n = 0;
        String unfold0;
        while (true) {
            unfold0 = s;
            if (n >= length) {
                break;
            }
            final char char1 = s.charAt(n);
            if (char1 == '\r' || char1 == '\n') {
                unfold0 = unfold0(s, n);
                break;
            }
            ++n;
        }
        return unfold0;
    }
    
    private static String unfold0(final String s, int n) {
        final int length = s.length();
        final StringBuilder sb = new StringBuilder(length);
        int n2 = n;
        if (n > 0) {
            sb.append(s.substring(0, n));
            n2 = n;
        }
        while (true) {
            n = n2 + 1;
            if (n >= length) {
                break;
            }
            final char char1 = s.charAt(n);
            n2 = n;
            if (char1 == '\r') {
                continue;
            }
            n2 = n;
            if (char1 == '\n') {
                continue;
            }
            sb.append(char1);
            n2 = n;
        }
        return sb.toString();
    }
    
    private static final class Rfc822DateFormat extends SimpleDateFormat
    {
        private static final long serialVersionUID = 1L;
        
        public Rfc822DateFormat() {
            super("EEE, d MMM yyyy HH:mm:ss ", Locale.US);
        }
        
        @Override
        public StringBuffer format(final Date date, final StringBuffer sb, final FieldPosition fieldPosition) {
            final StringBuffer format = super.format(date, sb, fieldPosition);
            int n = (this.calendar.get(15) + this.calendar.get(16)) / 1000 / 60;
            if (n < 0) {
                format.append('-');
                n = -n;
            }
            else {
                format.append('+');
            }
            format.append(String.format("%02d%02d", n / 60, n % 60));
            return format;
        }
    }
}
