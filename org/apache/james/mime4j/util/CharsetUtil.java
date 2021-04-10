package org.apache.james.mime4j.util;

import org.apache.commons.logging.*;
import java.io.*;
import java.util.*;
import java.nio.charset.*;

public class CharsetUtil
{
    public static final int CR = 13;
    public static final String CRLF = "\r\n";
    public static final java.nio.charset.Charset DEFAULT_CHARSET;
    public static final int HT = 9;
    public static final java.nio.charset.Charset ISO_8859_1;
    private static Charset[] JAVA_CHARSETS;
    public static final int LF = 10;
    public static final int SP = 32;
    public static final java.nio.charset.Charset US_ASCII;
    public static final java.nio.charset.Charset UTF_8;
    private static Map<String, Charset> charsetMap;
    private static SortedSet<String> decodingSupported;
    private static SortedSet<String> encodingSupported;
    private static Log log;
    
    static {
        CharsetUtil.log = LogFactory.getLog((Class)CharsetUtil.class);
        CharsetUtil.JAVA_CHARSETS = new Charset[] { new Charset("ISO8859_1", "ISO-8859-1", new String[] { "ISO_8859-1:1987", "iso-ir-100", "ISO_8859-1", "latin1", "l1", "IBM819", "CP819", "csISOLatin1", "8859_1", "819", "IBM-819", "ISO8859-1", "ISO_8859_1" }), new Charset("ISO8859_2", "ISO-8859-2", new String[] { "ISO_8859-2:1987", "iso-ir-101", "ISO_8859-2", "latin2", "l2", "csISOLatin2", "8859_2", "iso8859_2" }), new Charset("ISO8859_3", "ISO-8859-3", new String[] { "ISO_8859-3:1988", "iso-ir-109", "ISO_8859-3", "latin3", "l3", "csISOLatin3", "8859_3" }), new Charset("ISO8859_4", "ISO-8859-4", new String[] { "ISO_8859-4:1988", "iso-ir-110", "ISO_8859-4", "latin4", "l4", "csISOLatin4", "8859_4" }), new Charset("ISO8859_5", "ISO-8859-5", new String[] { "ISO_8859-5:1988", "iso-ir-144", "ISO_8859-5", "cyrillic", "csISOLatinCyrillic", "8859_5" }), new Charset("ISO8859_6", "ISO-8859-6", new String[] { "ISO_8859-6:1987", "iso-ir-127", "ISO_8859-6", "ECMA-114", "ASMO-708", "arabic", "csISOLatinArabic", "8859_6" }), new Charset("ISO8859_7", "ISO-8859-7", new String[] { "ISO_8859-7:1987", "iso-ir-126", "ISO_8859-7", "ELOT_928", "ECMA-118", "greek", "greek8", "csISOLatinGreek", "8859_7", "sun_eu_greek" }), new Charset("ISO8859_8", "ISO-8859-8", new String[] { "ISO_8859-8:1988", "iso-ir-138", "ISO_8859-8", "hebrew", "csISOLatinHebrew", "8859_8" }), new Charset("ISO8859_9", "ISO-8859-9", new String[] { "ISO_8859-9:1989", "iso-ir-148", "ISO_8859-9", "latin5", "l5", "csISOLatin5", "8859_9" }), new Charset("ISO8859_13", "ISO-8859-13", new String[0]), new Charset("ISO8859_15", "ISO-8859-15", new String[] { "ISO_8859-15", "Latin-9", "8859_15", "csISOlatin9", "IBM923", "cp923", "923", "L9", "IBM-923", "ISO8859-15", "LATIN9", "LATIN0", "csISOlatin0", "ISO8859_15_FDIS" }), new Charset("KOI8_R", "KOI8-R", new String[] { "csKOI8R", "koi8" }), new Charset("ASCII", "US-ASCII", new String[] { "ANSI_X3.4-1968", "iso-ir-6", "ANSI_X3.4-1986", "ISO_646.irv:1991", "ISO646-US", "us", "IBM367", "cp367", "csASCII", "ascii7", "646", "iso_646.irv:1983" }), new Charset("UTF8", "UTF-8", new String[0]), new Charset("UTF-16", "UTF-16", new String[] { "UTF_16" }), new Charset("UnicodeBigUnmarked", "UTF-16BE", new String[] { "X-UTF-16BE", "UTF_16BE", "ISO-10646-UCS-2" }), new Charset("UnicodeLittleUnmarked", "UTF-16LE", new String[] { "UTF_16LE", "X-UTF-16LE" }), new Charset("Big5", "Big5", new String[] { "csBig5", "CN-Big5", "BIG-FIVE", "BIGFIVE" }), new Charset("Big5_HKSCS", "Big5-HKSCS", new String[] { "big5hkscs" }), new Charset("EUC_JP", "EUC-JP", new String[] { "csEUCPkdFmtJapanese", "Extended_UNIX_Code_Packed_Format_for_Japanese", "eucjis", "x-eucjp", "eucjp", "x-euc-jp" }), new Charset("EUC_KR", "EUC-KR", new String[] { "csEUCKR", "ksc5601", "5601", "ksc5601_1987", "ksc_5601", "ksc5601-1987", "ks_c_5601-1987", "euckr" }), new Charset("GB18030", "GB18030", new String[] { "gb18030-2000" }), new Charset("EUC_CN", "GB2312", new String[] { "x-EUC-CN", "csGB2312", "euccn", "euc-cn", "gb2312-80", "gb2312-1980", "CN-GB", "CN-GB-ISOIR165" }), new Charset("GBK", "windows-936", new String[] { "CP936", "MS936", "ms_936", "x-mswin-936" }), new Charset("Cp037", "IBM037", new String[] { "ebcdic-cp-us", "ebcdic-cp-ca", "ebcdic-cp-wt", "ebcdic-cp-nl", "csIBM037" }), new Charset("Cp273", "IBM273", new String[] { "csIBM273" }), new Charset("Cp277", "IBM277", new String[] { "EBCDIC-CP-DK", "EBCDIC-CP-NO", "csIBM277" }), new Charset("Cp278", "IBM278", new String[] { "CP278", "ebcdic-cp-fi", "ebcdic-cp-se", "csIBM278" }), new Charset("Cp280", "IBM280", new String[] { "ebcdic-cp-it", "csIBM280" }), new Charset("Cp284", "IBM284", new String[] { "ebcdic-cp-es", "csIBM284" }), new Charset("Cp285", "IBM285", new String[] { "ebcdic-cp-gb", "csIBM285" }), new Charset("Cp297", "IBM297", new String[] { "ebcdic-cp-fr", "csIBM297" }), new Charset("Cp420", "IBM420", new String[] { "ebcdic-cp-ar1", "csIBM420" }), new Charset("Cp424", "IBM424", new String[] { "ebcdic-cp-he", "csIBM424" }), new Charset("Cp437", "IBM437", new String[] { "437", "csPC8CodePage437" }), new Charset("Cp500", "IBM500", new String[] { "ebcdic-cp-be", "ebcdic-cp-ch", "csIBM500" }), new Charset("Cp775", "IBM775", new String[] { "csPC775Baltic" }), new Charset("Cp838", "IBM-Thai", new String[0]), new Charset("Cp850", "IBM850", new String[] { "850", "csPC850Multilingual" }), new Charset("Cp852", "IBM852", new String[] { "852", "csPCp852" }), new Charset("Cp855", "IBM855", new String[] { "855", "csIBM855" }), new Charset("Cp857", "IBM857", new String[] { "857", "csIBM857" }), new Charset("Cp858", "IBM00858", new String[] { "CCSID00858", "CP00858", "PC-Multilingual-850+euro" }), new Charset("Cp860", "IBM860", new String[] { "860", "csIBM860" }), new Charset("Cp861", "IBM861", new String[] { "861", "cp-is", "csIBM861" }), new Charset("Cp862", "IBM862", new String[] { "862", "csPC862LatinHebrew" }), new Charset("Cp863", "IBM863", new String[] { "863", "csIBM863" }), new Charset("Cp864", "IBM864", new String[] { "cp864", "csIBM864" }), new Charset("Cp865", "IBM865", new String[] { "865", "csIBM865" }), new Charset("Cp866", "IBM866", new String[] { "866", "csIBM866" }), new Charset("Cp868", "IBM868", new String[] { "cp-ar", "csIBM868" }), new Charset("Cp869", "IBM869", new String[] { "cp-gr", "csIBM869" }), new Charset("Cp870", "IBM870", new String[] { "ebcdic-cp-roece", "ebcdic-cp-yu", "csIBM870" }), new Charset("Cp871", "IBM871", new String[] { "ebcdic-cp-is", "csIBM871" }), new Charset("Cp918", "IBM918", new String[] { "ebcdic-cp-ar2", "csIBM918" }), new Charset("Cp1026", "IBM1026", new String[] { "csIBM1026" }), new Charset("Cp1047", "IBM1047", new String[] { "IBM-1047" }), new Charset("Cp1140", "IBM01140", new String[] { "CCSID01140", "CP01140", "ebcdic-us-37+euro" }), new Charset("Cp1141", "IBM01141", new String[] { "CCSID01141", "CP01141", "ebcdic-de-273+euro" }), new Charset("Cp1142", "IBM01142", new String[] { "CCSID01142", "CP01142", "ebcdic-dk-277+euro", "ebcdic-no-277+euro" }), new Charset("Cp1143", "IBM01143", new String[] { "CCSID01143", "CP01143", "ebcdic-fi-278+euro", "ebcdic-se-278+euro" }), new Charset("Cp1144", "IBM01144", new String[] { "CCSID01144", "CP01144", "ebcdic-it-280+euro" }), new Charset("Cp1145", "IBM01145", new String[] { "CCSID01145", "CP01145", "ebcdic-es-284+euro" }), new Charset("Cp1146", "IBM01146", new String[] { "CCSID01146", "CP01146", "ebcdic-gb-285+euro" }), new Charset("Cp1147", "IBM01147", new String[] { "CCSID01147", "CP01147", "ebcdic-fr-297+euro" }), new Charset("Cp1148", "IBM01148", new String[] { "CCSID01148", "CP01148", "ebcdic-international-500+euro" }), new Charset("Cp1149", "IBM01149", new String[] { "CCSID01149", "CP01149", "ebcdic-is-871+euro" }), new Charset("Cp1250", "windows-1250", new String[0]), new Charset("Cp1251", "windows-1251", new String[0]), new Charset("Cp1252", "windows-1252", new String[0]), new Charset("Cp1253", "windows-1253", new String[0]), new Charset("Cp1254", "windows-1254", new String[0]), new Charset("Cp1255", "windows-1255", new String[0]), new Charset("Cp1256", "windows-1256", new String[0]), new Charset("Cp1257", "windows-1257", new String[0]), new Charset("Cp1258", "windows-1258", new String[0]), new Charset("ISO2022CN", "ISO-2022-CN", new String[0]), new Charset("ISO2022JP", "ISO-2022-JP", new String[] { "csISO2022JP", "JIS", "jis_encoding", "csjisencoding" }), new Charset("ISO2022KR", "ISO-2022-KR", new String[] { "csISO2022KR" }), new Charset("JIS_X0201", "JIS_X0201", new String[] { "X0201", "JIS0201", "csHalfWidthKatakana" }), new Charset("JIS_X0212-1990", "JIS_X0212-1990", new String[] { "iso-ir-159", "x0212", "JIS0212", "csISO159JISX02121990" }), new Charset("JIS_C6626-1983", "JIS_C6626-1983", new String[] { "x-JIS0208", "JIS0208", "csISO87JISX0208", "x0208", "JIS_X0208-1983", "iso-ir-87" }), new Charset("SJIS", "Shift_JIS", new String[] { "MS_Kanji", "csShiftJIS", "shift-jis", "x-sjis", "pck" }), new Charset("TIS620", "TIS-620", new String[0]), new Charset("MS932", "Windows-31J", new String[] { "windows-932", "csWindows31J", "x-ms-cp932" }), new Charset("EUC_TW", "EUC-TW", new String[] { "x-EUC-TW", "cns11643", "euctw" }), new Charset("x-Johab", "johab", new String[] { "johab", "cp1361", "ms1361", "ksc5601-1992", "ksc5601_1992" }), new Charset("MS950_HKSCS", "", new String[0]), new Charset("MS874", "windows-874", new String[] { "cp874" }), new Charset("MS949", "windows-949", new String[] { "windows949", "ms_949", "x-windows-949" }), new Charset("MS950", "windows-950", new String[] { "x-windows-950" }), new Charset("Cp737", (String)null, new String[0]), new Charset("Cp856", (String)null, new String[0]), new Charset("Cp875", (String)null, new String[0]), new Charset("Cp921", (String)null, new String[0]), new Charset("Cp922", (String)null, new String[0]), new Charset("Cp930", (String)null, new String[0]), new Charset("Cp933", (String)null, new String[0]), new Charset("Cp935", (String)null, new String[0]), new Charset("Cp937", (String)null, new String[0]), new Charset("Cp939", (String)null, new String[0]), new Charset("Cp942", (String)null, new String[0]), new Charset("Cp942C", (String)null, new String[0]), new Charset("Cp943", (String)null, new String[0]), new Charset("Cp943C", (String)null, new String[0]), new Charset("Cp948", (String)null, new String[0]), new Charset("Cp949", (String)null, new String[0]), new Charset("Cp949C", (String)null, new String[0]), new Charset("Cp950", (String)null, new String[0]), new Charset("Cp964", (String)null, new String[0]), new Charset("Cp970", (String)null, new String[0]), new Charset("Cp1006", (String)null, new String[0]), new Charset("Cp1025", (String)null, new String[0]), new Charset("Cp1046", (String)null, new String[0]), new Charset("Cp1097", (String)null, new String[0]), new Charset("Cp1098", (String)null, new String[0]), new Charset("Cp1112", (String)null, new String[0]), new Charset("Cp1122", (String)null, new String[0]), new Charset("Cp1123", (String)null, new String[0]), new Charset("Cp1124", (String)null, new String[0]), new Charset("Cp1381", (String)null, new String[0]), new Charset("Cp1383", (String)null, new String[0]), new Charset("Cp33722", (String)null, new String[0]), new Charset("Big5_Solaris", (String)null, new String[0]), new Charset("EUC_JP_LINUX", (String)null, new String[0]), new Charset("EUC_JP_Solaris", (String)null, new String[0]), new Charset("ISCII91", (String)null, new String[] { "x-ISCII91", "iscii" }), new Charset("ISO2022_CN_CNS", (String)null, new String[0]), new Charset("ISO2022_CN_GB", (String)null, new String[0]), new Charset("x-iso-8859-11", (String)null, new String[0]), new Charset("JISAutoDetect", (String)null, new String[0]), new Charset("MacArabic", (String)null, new String[0]), new Charset("MacCentralEurope", (String)null, new String[0]), new Charset("MacCroatian", (String)null, new String[0]), new Charset("MacCyrillic", (String)null, new String[0]), new Charset("MacDingbat", (String)null, new String[0]), new Charset("MacGreek", "MacGreek", new String[0]), new Charset("MacHebrew", (String)null, new String[0]), new Charset("MacIceland", (String)null, new String[0]), new Charset("MacRoman", "MacRoman", new String[] { "Macintosh", "MAC", "csMacintosh" }), new Charset("MacRomania", (String)null, new String[0]), new Charset("MacSymbol", (String)null, new String[0]), new Charset("MacThai", (String)null, new String[0]), new Charset("MacTurkish", (String)null, new String[0]), new Charset("MacUkraine", (String)null, new String[0]), new Charset("UnicodeBig", (String)null, new String[0]), new Charset("UnicodeLittle", (String)null, new String[0]) };
        CharsetUtil.decodingSupported = null;
        CharsetUtil.encodingSupported = null;
        CharsetUtil.charsetMap = null;
        CharsetUtil.decodingSupported = new TreeSet<String>();
        CharsetUtil.encodingSupported = new TreeSet<String>();
        final Charset[] java_CHARSETS = CharsetUtil.JAVA_CHARSETS;
        for (int length = java_CHARSETS.length, i = 0; i < length; ++i) {
            final Charset charset = java_CHARSETS[i];
            try {
                final String s = new String(new byte[] { 100, 117, 109, 109, 121 }, charset.canonical);
                CharsetUtil.decodingSupported.add(charset.canonical.toLowerCase());
            }
            catch (UnsupportedOperationException ex) {}
            catch (UnsupportedEncodingException ex2) {}
            try {
                "dummy".getBytes(charset.canonical);
                CharsetUtil.encodingSupported.add(charset.canonical.toLowerCase());
            }
            catch (UnsupportedOperationException ex3) {}
            catch (UnsupportedEncodingException ex4) {}
        }
        CharsetUtil.charsetMap = new HashMap<String, Charset>();
        final Charset[] java_CHARSETS2 = CharsetUtil.JAVA_CHARSETS;
        for (int length2 = java_CHARSETS2.length, j = 0; j < length2; ++j) {
            final Charset charset2 = java_CHARSETS2[j];
            CharsetUtil.charsetMap.put(charset2.canonical.toLowerCase(), charset2);
            if (charset2.mime != null) {
                CharsetUtil.charsetMap.put(charset2.mime.toLowerCase(), charset2);
            }
            if (charset2.aliases != null) {
                final String[] access$300 = charset2.aliases;
                for (int length3 = access$300.length, k = 0; k < length3; ++k) {
                    CharsetUtil.charsetMap.put(access$300[k].toLowerCase(), charset2);
                }
            }
        }
        if (CharsetUtil.log.isDebugEnabled()) {
            final Log log = CharsetUtil.log;
            final StringBuilder sb = new StringBuilder();
            sb.append("Character sets which support decoding: ");
            sb.append(CharsetUtil.decodingSupported);
            log.debug((Object)sb.toString());
            final Log log2 = CharsetUtil.log;
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Character sets which support encoding: ");
            sb2.append(CharsetUtil.encodingSupported);
            log2.debug((Object)sb2.toString());
        }
        US_ASCII = java.nio.charset.Charset.forName("US-ASCII");
        ISO_8859_1 = java.nio.charset.Charset.forName("ISO-8859-1");
        UTF_8 = java.nio.charset.Charset.forName("UTF-8");
        DEFAULT_CHARSET = CharsetUtil.US_ASCII;
    }
    
    public static java.nio.charset.Charset getCharset(final String s) {
        String s2 = s;
        if (s == null) {
            s2 = "ISO-8859-1";
        }
        try {
            return java.nio.charset.Charset.forName(s2);
        }
        catch (UnsupportedCharsetException ex) {
            final Log log = CharsetUtil.log;
            final StringBuilder sb = new StringBuilder();
        }
        catch (IllegalCharsetNameException ex) {
            final Log log = CharsetUtil.log;
            final StringBuilder sb = new StringBuilder();
            goto Label_0034;
        }
    }
    
    public static boolean isASCII(final char c) {
        return (c & '\uff80') == 0x0;
    }
    
    public static boolean isASCII(final String s) {
        if (s != null) {
            for (int length = s.length(), i = 0; i < length; ++i) {
                if (!isASCII(s.charAt(i))) {
                    return false;
                }
            }
            return true;
        }
        throw new IllegalArgumentException("String may not be null");
    }
    
    public static boolean isDecodingSupported(final String s) {
        return CharsetUtil.decodingSupported.contains(s.toLowerCase());
    }
    
    public static boolean isEncodingSupported(final String s) {
        return CharsetUtil.encodingSupported.contains(s.toLowerCase());
    }
    
    public static boolean isWhitespace(final char c) {
        return c == ' ' || c == '\t' || c == '\r' || c == '\n';
    }
    
    public static boolean isWhitespace(final String s) {
        if (s != null) {
            for (int length = s.length(), i = 0; i < length; ++i) {
                if (!isWhitespace(s.charAt(i))) {
                    return false;
                }
            }
            return true;
        }
        throw new IllegalArgumentException("String may not be null");
    }
    
    public static String toJavaCharset(final String s) {
        final Charset charset = CharsetUtil.charsetMap.get(s.toLowerCase());
        if (charset != null) {
            return charset.canonical;
        }
        return null;
    }
    
    public static String toMimeCharset(final String s) {
        final Charset charset = CharsetUtil.charsetMap.get(s.toLowerCase());
        if (charset != null) {
            return charset.mime;
        }
        return null;
    }
    
    private static class Charset implements Comparable<Charset>
    {
        private String[] aliases;
        private String canonical;
        private String mime;
        
        private Charset(final String canonical, final String mime, final String[] aliases) {
            this.canonical = null;
            this.mime = null;
            this.aliases = null;
            this.canonical = canonical;
            this.mime = mime;
            this.aliases = aliases;
        }
        
        @Override
        public int compareTo(final Charset charset) {
            return this.canonical.compareTo(charset.canonical);
        }
    }
}
