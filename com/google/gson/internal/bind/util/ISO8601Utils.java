package com.google.gson.internal.bind.util;

import java.util.*;
import java.text.*;

public class ISO8601Utils
{
    private static final TimeZone TIMEZONE_UTC;
    private static final String UTC_ID = "UTC";
    
    static {
        TIMEZONE_UTC = TimeZone.getTimeZone("UTC");
    }
    
    private static boolean checkOffset(final String s, final int n, final char c) {
        return n < s.length() && s.charAt(n) == c;
    }
    
    public static String format(final Date date) {
        return format(date, false, ISO8601Utils.TIMEZONE_UTC);
    }
    
    public static String format(final Date date, final boolean b) {
        return format(date, b, ISO8601Utils.TIMEZONE_UTC);
    }
    
    public static String format(final Date time, final boolean b, final TimeZone timeZone) {
        final GregorianCalendar gregorianCalendar = new GregorianCalendar(timeZone, Locale.US);
        gregorianCalendar.setTime(time);
        final int length = "yyyy-MM-ddThh:mm:ss".length();
        int length2;
        if (b) {
            length2 = ".sss".length();
        }
        else {
            length2 = 0;
        }
        String s;
        if (timeZone.getRawOffset() == 0) {
            s = "Z";
        }
        else {
            s = "+hh:mm";
        }
        final StringBuilder sb = new StringBuilder(length + length2 + s.length());
        padInt(sb, gregorianCalendar.get(1), "yyyy".length());
        char c = '-';
        sb.append('-');
        padInt(sb, gregorianCalendar.get(2) + 1, "MM".length());
        sb.append('-');
        padInt(sb, gregorianCalendar.get(5), "dd".length());
        sb.append('T');
        padInt(sb, gregorianCalendar.get(11), "hh".length());
        sb.append(':');
        padInt(sb, gregorianCalendar.get(12), "mm".length());
        sb.append(':');
        padInt(sb, gregorianCalendar.get(13), "ss".length());
        if (b) {
            sb.append('.');
            padInt(sb, gregorianCalendar.get(14), "sss".length());
        }
        final int offset = timeZone.getOffset(gregorianCalendar.getTimeInMillis());
        if (offset != 0) {
            final int abs = Math.abs(offset / 60000 / 60);
            final int abs2 = Math.abs(offset / 60000 % 60);
            if (offset >= 0) {
                c = '+';
            }
            sb.append(c);
            padInt(sb, abs, "hh".length());
            sb.append(':');
            padInt(sb, abs2, "mm".length());
        }
        else {
            sb.append('Z');
        }
        return sb.toString();
    }
    
    private static int indexOfNonDigit(final String s, int i) {
        while (i < s.length()) {
            final char char1 = s.charAt(i);
            if (char1 < '0') {
                return i;
            }
            if (char1 > '9') {
                return i;
            }
            ++i;
        }
        return s.length();
    }
    
    private static void padInt(final StringBuilder sb, int i, final int n) {
        final String string = Integer.toString(i);
        for (i = n - string.length(); i > 0; --i) {
            sb.append('0');
        }
        sb.append(string);
    }
    
    public static Date parse(final String p0, final ParsePosition p1) throws ParseException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokevirtual   java/text/ParsePosition.getIndex:()I
        //     4: istore_3       
        //     5: iload_3        
        //     6: iconst_4       
        //     7: iadd           
        //     8: istore          4
        //    10: aload_0        
        //    11: iload_3        
        //    12: iload           4
        //    14: invokestatic    com/google/gson/internal/bind/util/ISO8601Utils.parseInt:(Ljava/lang/String;II)I
        //    17: istore          13
        //    19: iload           4
        //    21: istore_3       
        //    22: aload_0        
        //    23: iload           4
        //    25: bipush          45
        //    27: invokestatic    com/google/gson/internal/bind/util/ISO8601Utils.checkOffset:(Ljava/lang/String;IC)Z
        //    30: ifeq            38
        //    33: iload           4
        //    35: iconst_1       
        //    36: iadd           
        //    37: istore_3       
        //    38: iload_3        
        //    39: iconst_2       
        //    40: iadd           
        //    41: istore          4
        //    43: aload_0        
        //    44: iload_3        
        //    45: iload           4
        //    47: invokestatic    com/google/gson/internal/bind/util/ISO8601Utils.parseInt:(Ljava/lang/String;II)I
        //    50: istore          14
        //    52: iload           4
        //    54: istore_3       
        //    55: aload_0        
        //    56: iload           4
        //    58: bipush          45
        //    60: invokestatic    com/google/gson/internal/bind/util/ISO8601Utils.checkOffset:(Ljava/lang/String;IC)Z
        //    63: ifeq            71
        //    66: iload           4
        //    68: iconst_1       
        //    69: iadd           
        //    70: istore_3       
        //    71: iload_3        
        //    72: iconst_2       
        //    73: iadd           
        //    74: istore          8
        //    76: aload_0        
        //    77: iload_3        
        //    78: iload           8
        //    80: invokestatic    com/google/gson/internal/bind/util/ISO8601Utils.parseInt:(Ljava/lang/String;II)I
        //    83: istore          15
        //    85: iconst_0       
        //    86: istore          5
        //    88: iconst_0       
        //    89: istore          6
        //    91: iconst_0       
        //    92: istore          12
        //    94: iconst_0       
        //    95: istore          11
        //    97: aload_0        
        //    98: iload           8
        //   100: bipush          84
        //   102: invokestatic    com/google/gson/internal/bind/util/ISO8601Utils.checkOffset:(Ljava/lang/String;IC)Z
        //   105: istore          17
        //   107: iload           17
        //   109: ifne            150
        //   112: aload_0        
        //   113: invokevirtual   java/lang/String.length:()I
        //   116: iload           8
        //   118: if_icmpgt       150
        //   121: new             Ljava/util/GregorianCalendar;
        //   124: dup            
        //   125: iload           13
        //   127: iload           14
        //   129: iconst_1       
        //   130: isub           
        //   131: iload           15
        //   133: invokespecial   java/util/GregorianCalendar.<init>:(III)V
        //   136: astore          18
        //   138: aload_1        
        //   139: iload           8
        //   141: invokevirtual   java/text/ParsePosition.setIndex:(I)V
        //   144: aload           18
        //   146: invokevirtual   java/util/Calendar.getTime:()Ljava/util/Date;
        //   149: areturn        
        //   150: iload           8
        //   152: istore_3       
        //   153: iload           12
        //   155: istore          7
        //   157: iload           11
        //   159: istore          4
        //   161: iload           17
        //   163: ifeq            514
        //   166: iload           8
        //   168: iconst_1       
        //   169: iadd           
        //   170: istore_3       
        //   171: iload_3        
        //   172: iconst_2       
        //   173: iadd           
        //   174: istore          4
        //   176: aload_0        
        //   177: iload_3        
        //   178: iload           4
        //   180: invokestatic    com/google/gson/internal/bind/util/ISO8601Utils.parseInt:(Ljava/lang/String;II)I
        //   183: istore          9
        //   185: iload           4
        //   187: istore_3       
        //   188: aload_0        
        //   189: iload           4
        //   191: bipush          58
        //   193: invokestatic    com/google/gson/internal/bind/util/ISO8601Utils.checkOffset:(Ljava/lang/String;IC)Z
        //   196: ifeq            204
        //   199: iload           4
        //   201: iconst_1       
        //   202: iadd           
        //   203: istore_3       
        //   204: iload_3        
        //   205: iconst_2       
        //   206: iadd           
        //   207: istore          4
        //   209: aload_0        
        //   210: iload_3        
        //   211: iload           4
        //   213: invokestatic    com/google/gson/internal/bind/util/ISO8601Utils.parseInt:(Ljava/lang/String;II)I
        //   216: istore          10
        //   218: iload           4
        //   220: istore_3       
        //   221: aload_0        
        //   222: iload           4
        //   224: bipush          58
        //   226: invokestatic    com/google/gson/internal/bind/util/ISO8601Utils.checkOffset:(Ljava/lang/String;IC)Z
        //   229: ifeq            237
        //   232: iload           4
        //   234: iconst_1       
        //   235: iadd           
        //   236: istore_3       
        //   237: iload_3        
        //   238: istore          8
        //   240: iload           8
        //   242: istore_3       
        //   243: iload           9
        //   245: istore          5
        //   247: iload           10
        //   249: istore          6
        //   251: iload           12
        //   253: istore          7
        //   255: iload           11
        //   257: istore          4
        //   259: aload_0        
        //   260: invokevirtual   java/lang/String.length:()I
        //   263: iload           8
        //   265: if_icmple       514
        //   268: aload_0        
        //   269: iload           8
        //   271: invokevirtual   java/lang/String.charAt:(I)C
        //   274: istore          16
        //   276: iload           8
        //   278: istore_3       
        //   279: iload           9
        //   281: istore          5
        //   283: iload           10
        //   285: istore          6
        //   287: iload           12
        //   289: istore          7
        //   291: iload           11
        //   293: istore          4
        //   295: iload           16
        //   297: bipush          90
        //   299: if_icmpeq       514
        //   302: iload           8
        //   304: istore_3       
        //   305: iload           9
        //   307: istore          5
        //   309: iload           10
        //   311: istore          6
        //   313: iload           12
        //   315: istore          7
        //   317: iload           11
        //   319: istore          4
        //   321: iload           16
        //   323: bipush          43
        //   325: if_icmpeq       514
        //   328: iload           8
        //   330: istore_3       
        //   331: iload           9
        //   333: istore          5
        //   335: iload           10
        //   337: istore          6
        //   339: iload           12
        //   341: istore          7
        //   343: iload           11
        //   345: istore          4
        //   347: iload           16
        //   349: bipush          45
        //   351: if_icmpeq       514
        //   354: iload           8
        //   356: iconst_2       
        //   357: iadd           
        //   358: istore          4
        //   360: aload_0        
        //   361: iload           8
        //   363: iload           4
        //   365: invokestatic    com/google/gson/internal/bind/util/ISO8601Utils.parseInt:(Ljava/lang/String;II)I
        //   368: istore          5
        //   370: iload           5
        //   372: istore_3       
        //   373: iload           5
        //   375: bipush          59
        //   377: if_icmple       393
        //   380: iload           5
        //   382: istore_3       
        //   383: iload           5
        //   385: bipush          63
        //   387: if_icmpge       393
        //   390: bipush          59
        //   392: istore_3       
        //   393: iload_3        
        //   394: istore          7
        //   396: aload_0        
        //   397: iload           4
        //   399: bipush          46
        //   401: invokestatic    com/google/gson/internal/bind/util/ISO8601Utils.checkOffset:(Ljava/lang/String;IC)Z
        //   404: ifeq            499
        //   407: iload           4
        //   409: iconst_1       
        //   410: iadd           
        //   411: istore          5
        //   413: aload_0        
        //   414: iload           5
        //   416: iconst_1       
        //   417: iadd           
        //   418: invokestatic    com/google/gson/internal/bind/util/ISO8601Utils.indexOfNonDigit:(Ljava/lang/String;I)I
        //   421: istore_3       
        //   422: iload_3        
        //   423: iload           5
        //   425: iconst_3       
        //   426: iadd           
        //   427: invokestatic    java/lang/Math.min:(II)I
        //   430: istore          6
        //   432: aload_0        
        //   433: iload           5
        //   435: iload           6
        //   437: invokestatic    com/google/gson/internal/bind/util/ISO8601Utils.parseInt:(Ljava/lang/String;II)I
        //   440: istore          4
        //   442: iload           6
        //   444: iload           5
        //   446: isub           
        //   447: tableswitch {
        //                2: 481
        //                3: 471
        //          default: 468
        //        }
        //   468: goto            488
        //   471: iload           4
        //   473: bipush          10
        //   475: imul           
        //   476: istore          4
        //   478: goto            488
        //   481: iload           4
        //   483: bipush          100
        //   485: imul           
        //   486: istore          4
        //   488: iload           9
        //   490: istore          5
        //   492: iload           10
        //   494: istore          6
        //   496: goto            514
        //   499: iload           4
        //   501: istore_3       
        //   502: iload           11
        //   504: istore          4
        //   506: iload           10
        //   508: istore          6
        //   510: iload           9
        //   512: istore          5
        //   514: aload_0        
        //   515: invokevirtual   java/lang/String.length:()I
        //   518: istore          8
        //   520: iload           8
        //   522: iload_3        
        //   523: if_icmpgt       536
        //   526: new             Ljava/lang/IllegalArgumentException;
        //   529: dup            
        //   530: ldc             "No time zone indicator"
        //   532: invokespecial   java/lang/IllegalArgumentException.<init>:(Ljava/lang/String;)V
        //   535: athrow         
        //   536: aload_0        
        //   537: iload_3        
        //   538: invokevirtual   java/lang/String.charAt:(I)C
        //   541: istore_2       
        //   542: iload_2        
        //   543: bipush          90
        //   545: if_icmpne       560
        //   548: getstatic       com/google/gson/internal/bind/util/ISO8601Utils.TIMEZONE_UTC:Ljava/util/TimeZone;
        //   551: astore          18
        //   553: iload_3        
        //   554: iconst_1       
        //   555: iadd           
        //   556: istore_3       
        //   557: goto            840
        //   560: iload_2        
        //   561: bipush          43
        //   563: if_icmpeq       1159
        //   566: iload_2        
        //   567: bipush          45
        //   569: if_icmpne       575
        //   572: goto            620
        //   575: new             Ljava/lang/StringBuilder;
        //   578: dup            
        //   579: invokespecial   java/lang/StringBuilder.<init>:()V
        //   582: astore          18
        //   584: aload           18
        //   586: ldc             "Invalid time zone indicator '"
        //   588: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   591: pop            
        //   592: aload           18
        //   594: iload_2        
        //   595: invokevirtual   java/lang/StringBuilder.append:(C)Ljava/lang/StringBuilder;
        //   598: pop            
        //   599: aload           18
        //   601: ldc             "'"
        //   603: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   606: pop            
        //   607: new             Ljava/lang/IndexOutOfBoundsException;
        //   610: dup            
        //   611: aload           18
        //   613: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   616: invokespecial   java/lang/IndexOutOfBoundsException.<init>:(Ljava/lang/String;)V
        //   619: athrow         
        //   620: aload_0        
        //   621: iload_3        
        //   622: invokevirtual   java/lang/String.substring:(I)Ljava/lang/String;
        //   625: astore          18
        //   627: aload           18
        //   629: invokevirtual   java/lang/String.length:()I
        //   632: iconst_5       
        //   633: if_icmplt       639
        //   636: goto            671
        //   639: new             Ljava/lang/StringBuilder;
        //   642: dup            
        //   643: invokespecial   java/lang/StringBuilder.<init>:()V
        //   646: astore          19
        //   648: aload           19
        //   650: aload           18
        //   652: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   655: pop            
        //   656: aload           19
        //   658: ldc             "00"
        //   660: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   663: pop            
        //   664: aload           19
        //   666: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   669: astore          18
        //   671: iload_3        
        //   672: aload           18
        //   674: invokevirtual   java/lang/String.length:()I
        //   677: iadd           
        //   678: istore_3       
        //   679: ldc             "+0000"
        //   681: aload           18
        //   683: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   686: ifne            1165
        //   689: ldc             "+00:00"
        //   691: aload           18
        //   693: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   696: ifeq            702
        //   699: goto            832
        //   702: new             Ljava/lang/StringBuilder;
        //   705: dup            
        //   706: invokespecial   java/lang/StringBuilder.<init>:()V
        //   709: astore          19
        //   711: aload           19
        //   713: ldc             "GMT"
        //   715: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   718: pop            
        //   719: aload           19
        //   721: aload           18
        //   723: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   726: pop            
        //   727: aload           19
        //   729: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   732: astore          19
        //   734: aload           19
        //   736: invokestatic    java/util/TimeZone.getTimeZone:(Ljava/lang/String;)Ljava/util/TimeZone;
        //   739: astore          18
        //   741: aload           18
        //   743: invokevirtual   java/util/TimeZone.getID:()Ljava/lang/String;
        //   746: astore          20
        //   748: aload           20
        //   750: aload           19
        //   752: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   755: ifne            1162
        //   758: aload           20
        //   760: ldc             ":"
        //   762: ldc             ""
        //   764: invokevirtual   java/lang/String.replace:(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Ljava/lang/String;
        //   767: aload           19
        //   769: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   772: ifne            1162
        //   775: new             Ljava/lang/StringBuilder;
        //   778: dup            
        //   779: invokespecial   java/lang/StringBuilder.<init>:()V
        //   782: astore          20
        //   784: aload           20
        //   786: ldc             "Mismatching time zone indicator: "
        //   788: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   791: pop            
        //   792: aload           20
        //   794: aload           19
        //   796: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   799: pop            
        //   800: aload           20
        //   802: ldc             " given, resolves to "
        //   804: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   807: pop            
        //   808: aload           20
        //   810: aload           18
        //   812: invokevirtual   java/util/TimeZone.getID:()Ljava/lang/String;
        //   815: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   818: pop            
        //   819: new             Ljava/lang/IndexOutOfBoundsException;
        //   822: dup            
        //   823: aload           20
        //   825: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   828: invokespecial   java/lang/IndexOutOfBoundsException.<init>:(Ljava/lang/String;)V
        //   831: athrow         
        //   832: getstatic       com/google/gson/internal/bind/util/ISO8601Utils.TIMEZONE_UTC:Ljava/util/TimeZone;
        //   835: astore          18
        //   837: goto            1168
        //   840: new             Ljava/util/GregorianCalendar;
        //   843: dup            
        //   844: aload           18
        //   846: invokespecial   java/util/GregorianCalendar.<init>:(Ljava/util/TimeZone;)V
        //   849: astore          18
        //   851: aload           18
        //   853: iconst_0       
        //   854: invokevirtual   java/util/Calendar.setLenient:(Z)V
        //   857: aload           18
        //   859: iconst_1       
        //   860: iload           13
        //   862: invokevirtual   java/util/Calendar.set:(II)V
        //   865: aload           18
        //   867: iconst_2       
        //   868: iload           14
        //   870: iconst_1       
        //   871: isub           
        //   872: invokevirtual   java/util/Calendar.set:(II)V
        //   875: aload           18
        //   877: iconst_5       
        //   878: iload           15
        //   880: invokevirtual   java/util/Calendar.set:(II)V
        //   883: aload           18
        //   885: bipush          11
        //   887: iload           5
        //   889: invokevirtual   java/util/Calendar.set:(II)V
        //   892: aload           18
        //   894: bipush          12
        //   896: iload           6
        //   898: invokevirtual   java/util/Calendar.set:(II)V
        //   901: aload           18
        //   903: bipush          13
        //   905: iload           7
        //   907: invokevirtual   java/util/Calendar.set:(II)V
        //   910: aload           18
        //   912: bipush          14
        //   914: iload           4
        //   916: invokevirtual   java/util/Calendar.set:(II)V
        //   919: aload_1        
        //   920: iload_3        
        //   921: invokevirtual   java/text/ParsePosition.setIndex:(I)V
        //   924: aload           18
        //   926: invokevirtual   java/util/Calendar.getTime:()Ljava/util/Date;
        //   929: astore          18
        //   931: aload           18
        //   933: areturn        
        //   934: astore          18
        //   936: goto            951
        //   939: astore          18
        //   941: goto            956
        //   944: astore          18
        //   946: goto            961
        //   949: astore          18
        //   951: goto            961
        //   954: astore          18
        //   956: goto            961
        //   959: astore          18
        //   961: aload_0        
        //   962: ifnonnull       970
        //   965: aconst_null    
        //   966: astore_0       
        //   967: goto            1008
        //   970: new             Ljava/lang/StringBuilder;
        //   973: dup            
        //   974: invokespecial   java/lang/StringBuilder.<init>:()V
        //   977: astore          19
        //   979: aload           19
        //   981: bipush          34
        //   983: invokevirtual   java/lang/StringBuilder.append:(C)Ljava/lang/StringBuilder;
        //   986: pop            
        //   987: aload           19
        //   989: aload_0        
        //   990: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   993: pop            
        //   994: aload           19
        //   996: ldc             "'"
        //   998: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  1001: pop            
        //  1002: aload           19
        //  1004: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //  1007: astore_0       
        //  1008: aload           18
        //  1010: invokevirtual   java/lang/Exception.getMessage:()Ljava/lang/String;
        //  1013: astore          20
        //  1015: aload           20
        //  1017: ifnull          1032
        //  1020: aload           20
        //  1022: astore          19
        //  1024: aload           20
        //  1026: invokevirtual   java/lang/String.isEmpty:()Z
        //  1029: ifeq            1078
        //  1032: new             Ljava/lang/StringBuilder;
        //  1035: dup            
        //  1036: invokespecial   java/lang/StringBuilder.<init>:()V
        //  1039: astore          19
        //  1041: aload           19
        //  1043: ldc             "("
        //  1045: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  1048: pop            
        //  1049: aload           19
        //  1051: aload           18
        //  1053: invokevirtual   java/lang/Object.getClass:()Ljava/lang/Class;
        //  1056: invokevirtual   java/lang/Class.getName:()Ljava/lang/String;
        //  1059: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  1062: pop            
        //  1063: aload           19
        //  1065: ldc             ")"
        //  1067: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  1070: pop            
        //  1071: aload           19
        //  1073: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //  1076: astore          19
        //  1078: new             Ljava/lang/StringBuilder;
        //  1081: dup            
        //  1082: invokespecial   java/lang/StringBuilder.<init>:()V
        //  1085: astore          20
        //  1087: aload           20
        //  1089: ldc             "Failed to parse date ["
        //  1091: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  1094: pop            
        //  1095: aload           20
        //  1097: aload_0        
        //  1098: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  1101: pop            
        //  1102: aload           20
        //  1104: ldc             "]: "
        //  1106: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  1109: pop            
        //  1110: aload           20
        //  1112: aload           19
        //  1114: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  1117: pop            
        //  1118: new             Ljava/text/ParseException;
        //  1121: dup            
        //  1122: aload           20
        //  1124: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //  1127: aload_1        
        //  1128: invokevirtual   java/text/ParsePosition.getIndex:()I
        //  1131: invokespecial   java/text/ParseException.<init>:(Ljava/lang/String;I)V
        //  1134: astore_0       
        //  1135: aload_0        
        //  1136: aload           18
        //  1138: invokevirtual   java/text/ParseException.initCause:(Ljava/lang/Throwable;)Ljava/lang/Throwable;
        //  1141: pop            
        //  1142: aload_0        
        //  1143: athrow         
        //  1144: astore          18
        //  1146: goto            951
        //  1149: astore          18
        //  1151: goto            956
        //  1154: astore          18
        //  1156: goto            961
        //  1159: goto            620
        //  1162: goto            1168
        //  1165: goto            832
        //  1168: goto            840
        //    Exceptions:
        //  throws java.text.ParseException
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                 
        //  -----  -----  -----  -----  -------------------------------------
        //  0      5      959    961    Ljava/lang/IndexOutOfBoundsException;
        //  0      5      954    956    Ljava/lang/NumberFormatException;
        //  0      5      949    951    Ljava/lang/IllegalArgumentException;
        //  10     19     959    961    Ljava/lang/IndexOutOfBoundsException;
        //  10     19     954    956    Ljava/lang/NumberFormatException;
        //  10     19     949    951    Ljava/lang/IllegalArgumentException;
        //  22     33     959    961    Ljava/lang/IndexOutOfBoundsException;
        //  22     33     954    956    Ljava/lang/NumberFormatException;
        //  22     33     949    951    Ljava/lang/IllegalArgumentException;
        //  43     52     959    961    Ljava/lang/IndexOutOfBoundsException;
        //  43     52     954    956    Ljava/lang/NumberFormatException;
        //  43     52     949    951    Ljava/lang/IllegalArgumentException;
        //  55     66     959    961    Ljava/lang/IndexOutOfBoundsException;
        //  55     66     954    956    Ljava/lang/NumberFormatException;
        //  55     66     949    951    Ljava/lang/IllegalArgumentException;
        //  76     85     959    961    Ljava/lang/IndexOutOfBoundsException;
        //  76     85     954    956    Ljava/lang/NumberFormatException;
        //  76     85     949    951    Ljava/lang/IllegalArgumentException;
        //  97     107    959    961    Ljava/lang/IndexOutOfBoundsException;
        //  97     107    954    956    Ljava/lang/NumberFormatException;
        //  97     107    949    951    Ljava/lang/IllegalArgumentException;
        //  112    150    1154   1159   Ljava/lang/IndexOutOfBoundsException;
        //  112    150    1149   1154   Ljava/lang/NumberFormatException;
        //  112    150    1144   1149   Ljava/lang/IllegalArgumentException;
        //  176    185    1154   1159   Ljava/lang/IndexOutOfBoundsException;
        //  176    185    1149   1154   Ljava/lang/NumberFormatException;
        //  176    185    1144   1149   Ljava/lang/IllegalArgumentException;
        //  188    199    1154   1159   Ljava/lang/IndexOutOfBoundsException;
        //  188    199    1149   1154   Ljava/lang/NumberFormatException;
        //  188    199    1144   1149   Ljava/lang/IllegalArgumentException;
        //  209    218    1154   1159   Ljava/lang/IndexOutOfBoundsException;
        //  209    218    1149   1154   Ljava/lang/NumberFormatException;
        //  209    218    1144   1149   Ljava/lang/IllegalArgumentException;
        //  221    232    1154   1159   Ljava/lang/IndexOutOfBoundsException;
        //  221    232    1149   1154   Ljava/lang/NumberFormatException;
        //  221    232    1144   1149   Ljava/lang/IllegalArgumentException;
        //  259    276    1154   1159   Ljava/lang/IndexOutOfBoundsException;
        //  259    276    1149   1154   Ljava/lang/NumberFormatException;
        //  259    276    1144   1149   Ljava/lang/IllegalArgumentException;
        //  360    370    1154   1159   Ljava/lang/IndexOutOfBoundsException;
        //  360    370    1149   1154   Ljava/lang/NumberFormatException;
        //  360    370    1144   1149   Ljava/lang/IllegalArgumentException;
        //  396    407    1154   1159   Ljava/lang/IndexOutOfBoundsException;
        //  396    407    1149   1154   Ljava/lang/NumberFormatException;
        //  396    407    1144   1149   Ljava/lang/IllegalArgumentException;
        //  413    442    1154   1159   Ljava/lang/IndexOutOfBoundsException;
        //  413    442    1149   1154   Ljava/lang/NumberFormatException;
        //  413    442    1144   1149   Ljava/lang/IllegalArgumentException;
        //  514    520    959    961    Ljava/lang/IndexOutOfBoundsException;
        //  514    520    954    956    Ljava/lang/NumberFormatException;
        //  514    520    949    951    Ljava/lang/IllegalArgumentException;
        //  526    536    1154   1159   Ljava/lang/IndexOutOfBoundsException;
        //  526    536    1149   1154   Ljava/lang/NumberFormatException;
        //  526    536    1144   1149   Ljava/lang/IllegalArgumentException;
        //  536    542    959    961    Ljava/lang/IndexOutOfBoundsException;
        //  536    542    954    956    Ljava/lang/NumberFormatException;
        //  536    542    949    951    Ljava/lang/IllegalArgumentException;
        //  548    553    1154   1159   Ljava/lang/IndexOutOfBoundsException;
        //  548    553    1149   1154   Ljava/lang/NumberFormatException;
        //  548    553    1144   1149   Ljava/lang/IllegalArgumentException;
        //  575    584    959    961    Ljava/lang/IndexOutOfBoundsException;
        //  575    584    954    956    Ljava/lang/NumberFormatException;
        //  575    584    949    951    Ljava/lang/IllegalArgumentException;
        //  584    620    944    949    Ljava/lang/IndexOutOfBoundsException;
        //  584    620    939    944    Ljava/lang/NumberFormatException;
        //  584    620    934    939    Ljava/lang/IllegalArgumentException;
        //  620    636    944    949    Ljava/lang/IndexOutOfBoundsException;
        //  620    636    939    944    Ljava/lang/NumberFormatException;
        //  620    636    934    939    Ljava/lang/IllegalArgumentException;
        //  639    671    944    949    Ljava/lang/IndexOutOfBoundsException;
        //  639    671    939    944    Ljava/lang/NumberFormatException;
        //  639    671    934    939    Ljava/lang/IllegalArgumentException;
        //  671    699    944    949    Ljava/lang/IndexOutOfBoundsException;
        //  671    699    939    944    Ljava/lang/NumberFormatException;
        //  671    699    934    939    Ljava/lang/IllegalArgumentException;
        //  702    832    944    949    Ljava/lang/IndexOutOfBoundsException;
        //  702    832    939    944    Ljava/lang/NumberFormatException;
        //  702    832    934    939    Ljava/lang/IllegalArgumentException;
        //  832    837    944    949    Ljava/lang/IndexOutOfBoundsException;
        //  832    837    939    944    Ljava/lang/NumberFormatException;
        //  832    837    934    939    Ljava/lang/IllegalArgumentException;
        //  840    931    944    949    Ljava/lang/IndexOutOfBoundsException;
        //  840    931    939    944    Ljava/lang/NumberFormatException;
        //  840    931    934    939    Ljava/lang/IllegalArgumentException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0150:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2596)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at us.deathmarine.luyten.FileSaver.doSaveJarDecompiled(FileSaver.java:192)
        //     at us.deathmarine.luyten.FileSaver.access$300(FileSaver.java:45)
        //     at us.deathmarine.luyten.FileSaver$4.run(FileSaver.java:112)
        //     at java.lang.Thread.run(Unknown Source)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    private static int parseInt(final String s, final int n, final int n2) throws NumberFormatException {
        if (n < 0 || n2 > s.length() || n > n2) {
            throw new NumberFormatException(s);
        }
        int n3 = 0;
        while (true) {
            int n4 = 0;
            Label_0116: {
                if ((n4 = n) >= n2) {
                    break Label_0116;
                }
                final int n5 = n + 1;
                final int digit = Character.digit(s.charAt(n), 10);
                if (digit < 0) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Invalid number: ");
                    sb.append(s.substring(n, n2));
                    throw new NumberFormatException(sb.toString());
                }
                final int n6 = -digit;
                final int n7 = n5;
                n3 = n6;
                n4 = n7;
            }
            if (n4 >= n2) {
                return -n3;
            }
            final int n7 = n4 + 1;
            final int digit2 = Character.digit(s.charAt(n4), 10);
            if (digit2 < 0) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Invalid number: ");
                sb2.append(s.substring(n, n2));
                throw new NumberFormatException(sb2.toString());
            }
            n3 = n3 * 10 - digit2;
            continue;
        }
    }
}
