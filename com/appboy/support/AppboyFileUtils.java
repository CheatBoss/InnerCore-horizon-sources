package com.appboy.support;

import java.util.*;
import java.io.*;
import android.content.res.*;
import android.net.*;

public class AppboyFileUtils
{
    public static final List<String> REMOTE_SCHEMES;
    private static final String a;
    
    static {
        a = AppboyLogger.getAppboyLogTag(AppboyFileUtils.class);
        REMOTE_SCHEMES = Collections.unmodifiableList((List<? extends String>)Arrays.asList("http", "https", "ftp", "ftps", "about", "javascript"));
    }
    
    public static void deleteFileOrDirectory(final File file) {
        if (file != null) {
            try {
                if (!file.exists()) {
                    return;
                }
                if (file.isDirectory()) {
                    final String[] list = file.list();
                    for (int length = list.length, i = 0; i < length; ++i) {
                        deleteFileOrDirectory(new File(file, list[i]));
                    }
                }
                file.delete();
            }
            catch (Exception ex) {
                final String a = AppboyFileUtils.a;
                final StringBuilder sb = new StringBuilder();
                sb.append("Caught exception while trying to delete file or directory ");
                sb.append(file.getName());
                AppboyLogger.e(a, sb.toString(), ex);
            }
        }
    }
    
    public static File downloadFileToPath(final String p0, final String p1, final String p2, final String p3) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: ifeq            44
        //     6: getstatic       com/appboy/support/AppboyFileUtils.a:Ljava/lang/String;
        //     9: astore_0       
        //    10: new             Ljava/lang/StringBuilder;
        //    13: dup            
        //    14: invokespecial   java/lang/StringBuilder.<init>:()V
        //    17: astore_2       
        //    18: aload_2        
        //    19: ldc             "SDK is offline. File not downloaded for url: "
        //    21: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    24: pop            
        //    25: aload_2        
        //    26: aload_1        
        //    27: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    30: pop            
        //    31: aload_2        
        //    32: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //    35: astore_1       
        //    36: aload_0        
        //    37: aload_1        
        //    38: invokestatic    com/appboy/support/AppboyLogger.i:(Ljava/lang/String;Ljava/lang/String;)I
        //    41: pop            
        //    42: aconst_null    
        //    43: areturn        
        //    44: aload_0        
        //    45: invokestatic    com/appboy/support/StringUtils.isNullOrBlank:(Ljava/lang/String;)Z
        //    48: ifeq            61
        //    51: getstatic       com/appboy/support/AppboyFileUtils.a:Ljava/lang/String;
        //    54: astore_0       
        //    55: ldc             "Download directory null or blank. File not downloaded."
        //    57: astore_1       
        //    58: goto            36
        //    61: aload_1        
        //    62: invokestatic    com/appboy/support/StringUtils.isNullOrBlank:(Ljava/lang/String;)Z
        //    65: ifeq            78
        //    68: getstatic       com/appboy/support/AppboyFileUtils.a:Ljava/lang/String;
        //    71: astore_0       
        //    72: ldc             "Zip file url null or blank. File not downloaded."
        //    74: astore_1       
        //    75: goto            36
        //    78: aload_2        
        //    79: invokestatic    com/appboy/support/StringUtils.isNullOrBlank:(Ljava/lang/String;)Z
        //    82: ifeq            95
        //    85: getstatic       com/appboy/support/AppboyFileUtils.a:Ljava/lang/String;
        //    88: astore_0       
        //    89: ldc             "Output filename null or blank. File not downloaded."
        //    91: astore_1       
        //    92: goto            36
        //    95: new             Ljava/io/File;
        //    98: dup            
        //    99: aload_0        
        //   100: invokespecial   java/io/File.<init>:(Ljava/lang/String;)V
        //   103: invokevirtual   java/io/File.mkdirs:()Z
        //   106: pop            
        //   107: aload_2        
        //   108: astore          5
        //   110: aload_3        
        //   111: invokestatic    com/appboy/support/StringUtils.isNullOrBlank:(Ljava/lang/String;)Z
        //   114: ifne            147
        //   117: new             Ljava/lang/StringBuilder;
        //   120: dup            
        //   121: invokespecial   java/lang/StringBuilder.<init>:()V
        //   124: astore          5
        //   126: aload           5
        //   128: aload_2        
        //   129: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   132: pop            
        //   133: aload           5
        //   135: aload_3        
        //   136: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   139: pop            
        //   140: aload           5
        //   142: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   145: astore          5
        //   147: new             Ljava/io/File;
        //   150: dup            
        //   151: aload_0        
        //   152: aload           5
        //   154: invokespecial   java/io/File.<init>:(Ljava/lang/String;Ljava/lang/String;)V
        //   157: astore          5
        //   159: new             Ljava/net/URL;
        //   162: dup            
        //   163: aload_1        
        //   164: invokespecial   java/net/URL.<init>:(Ljava/lang/String;)V
        //   167: invokestatic    bo/app/k.a:(Ljava/net/URL;)Ljava/net/URLConnection;
        //   170: checkcast       Ljava/net/HttpURLConnection;
        //   173: astore_0       
        //   174: aload_0        
        //   175: invokevirtual   java/net/HttpURLConnection.getResponseCode:()I
        //   178: istore          4
        //   180: iload           4
        //   182: sipush          200
        //   185: if_icmpeq       253
        //   188: getstatic       com/appboy/support/AppboyFileUtils.a:Ljava/lang/String;
        //   191: astore_2       
        //   192: new             Ljava/lang/StringBuilder;
        //   195: dup            
        //   196: invokespecial   java/lang/StringBuilder.<init>:()V
        //   199: astore_3       
        //   200: aload_3        
        //   201: ldc             "HTTP response code was "
        //   203: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   206: pop            
        //   207: aload_3        
        //   208: iload           4
        //   210: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //   213: pop            
        //   214: aload_3        
        //   215: ldc             ". File with url "
        //   217: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   220: pop            
        //   221: aload_3        
        //   222: aload_1        
        //   223: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   226: pop            
        //   227: aload_3        
        //   228: ldc             " could not be downloaded."
        //   230: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   233: pop            
        //   234: aload_2        
        //   235: aload_3        
        //   236: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   239: invokestatic    com/appboy/support/AppboyLogger.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   242: pop            
        //   243: aload_0        
        //   244: ifnull          251
        //   247: aload_0        
        //   248: invokevirtual   java/net/HttpURLConnection.disconnect:()V
        //   251: aconst_null    
        //   252: areturn        
        //   253: sipush          8192
        //   256: newarray        B
        //   258: astore          6
        //   260: new             Ljava/io/DataInputStream;
        //   263: dup            
        //   264: aload_0        
        //   265: invokevirtual   java/net/HttpURLConnection.getInputStream:()Ljava/io/InputStream;
        //   268: invokespecial   java/io/DataInputStream.<init>:(Ljava/io/InputStream;)V
        //   271: astore_3       
        //   272: new             Ljava/io/BufferedOutputStream;
        //   275: dup            
        //   276: new             Ljava/io/FileOutputStream;
        //   279: dup            
        //   280: aload           5
        //   282: invokespecial   java/io/FileOutputStream.<init>:(Ljava/io/File;)V
        //   285: invokespecial   java/io/BufferedOutputStream.<init>:(Ljava/io/OutputStream;)V
        //   288: astore_2       
        //   289: aload_3        
        //   290: aload           6
        //   292: invokevirtual   java/io/DataInputStream.read:([B)I
        //   295: istore          4
        //   297: iload           4
        //   299: iconst_m1      
        //   300: if_icmpeq       315
        //   303: aload_2        
        //   304: aload           6
        //   306: iconst_0       
        //   307: iload           4
        //   309: invokevirtual   java/io/BufferedOutputStream.write:([BII)V
        //   312: goto            289
        //   315: aload_3        
        //   316: invokevirtual   java/io/DataInputStream.close:()V
        //   319: aload_0        
        //   320: invokevirtual   java/net/HttpURLConnection.disconnect:()V
        //   323: aload_2        
        //   324: invokevirtual   java/io/BufferedOutputStream.close:()V
        //   327: aload_0        
        //   328: ifnull          335
        //   331: aload_0        
        //   332: invokevirtual   java/net/HttpURLConnection.disconnect:()V
        //   335: aload_3        
        //   336: invokevirtual   java/io/DataInputStream.close:()V
        //   339: aload_2        
        //   340: invokevirtual   java/io/BufferedOutputStream.close:()V
        //   343: aload           5
        //   345: areturn        
        //   346: astore_0       
        //   347: getstatic       com/appboy/support/AppboyFileUtils.a:Ljava/lang/String;
        //   350: ldc             "Exception during closing of file download streams."
        //   352: aload_0        
        //   353: invokestatic    com/appboy/support/AppboyLogger.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //   356: pop            
        //   357: aload           5
        //   359: areturn        
        //   360: astore          5
        //   362: goto            409
        //   365: astore          5
        //   367: goto            544
        //   370: astore          5
        //   372: aconst_null    
        //   373: astore_2       
        //   374: goto            409
        //   377: astore          5
        //   379: aconst_null    
        //   380: astore_2       
        //   381: goto            544
        //   384: astore          5
        //   386: aconst_null    
        //   387: astore_3       
        //   388: aconst_null    
        //   389: astore_2       
        //   390: goto            409
        //   393: astore          5
        //   395: aconst_null    
        //   396: astore_3       
        //   397: aconst_null    
        //   398: astore_2       
        //   399: goto            544
        //   402: astore          5
        //   404: aconst_null    
        //   405: astore_0       
        //   406: goto            386
        //   409: aload_3        
        //   410: astore          6
        //   412: aload_2        
        //   413: astore          7
        //   415: aload_0        
        //   416: astore          8
        //   418: getstatic       com/appboy/support/AppboyFileUtils.a:Ljava/lang/String;
        //   421: astore          9
        //   423: aload_3        
        //   424: astore          6
        //   426: aload_2        
        //   427: astore          7
        //   429: aload_0        
        //   430: astore          8
        //   432: new             Ljava/lang/StringBuilder;
        //   435: dup            
        //   436: invokespecial   java/lang/StringBuilder.<init>:()V
        //   439: astore          10
        //   441: aload_3        
        //   442: astore          6
        //   444: aload_2        
        //   445: astore          7
        //   447: aload_0        
        //   448: astore          8
        //   450: aload           10
        //   452: ldc             "Throwable during download of file from url : "
        //   454: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   457: pop            
        //   458: aload_3        
        //   459: astore          6
        //   461: aload_2        
        //   462: astore          7
        //   464: aload_0        
        //   465: astore          8
        //   467: aload           10
        //   469: aload_1        
        //   470: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   473: pop            
        //   474: aload_3        
        //   475: astore          6
        //   477: aload_2        
        //   478: astore          7
        //   480: aload_0        
        //   481: astore          8
        //   483: aload           9
        //   485: aload           10
        //   487: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   490: aload           5
        //   492: invokestatic    com/appboy/support/AppboyLogger.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //   495: pop            
        //   496: aload_0        
        //   497: ifnull          504
        //   500: aload_0        
        //   501: invokevirtual   java/net/HttpURLConnection.disconnect:()V
        //   504: aload_3        
        //   505: ifnull          515
        //   508: aload_3        
        //   509: invokevirtual   java/io/DataInputStream.close:()V
        //   512: goto            515
        //   515: aload_2        
        //   516: ifnull          535
        //   519: aload_2        
        //   520: invokevirtual   java/io/BufferedOutputStream.close:()V
        //   523: aconst_null    
        //   524: areturn        
        //   525: getstatic       com/appboy/support/AppboyFileUtils.a:Ljava/lang/String;
        //   528: ldc             "Exception during closing of file download streams."
        //   530: aload_0        
        //   531: invokestatic    com/appboy/support/AppboyLogger.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //   534: pop            
        //   535: aconst_null    
        //   536: areturn        
        //   537: astore          5
        //   539: aconst_null    
        //   540: astore_0       
        //   541: goto            395
        //   544: aload_3        
        //   545: astore          6
        //   547: aload_2        
        //   548: astore          7
        //   550: aload_0        
        //   551: astore          8
        //   553: getstatic       com/appboy/support/AppboyFileUtils.a:Ljava/lang/String;
        //   556: astore          9
        //   558: aload_3        
        //   559: astore          6
        //   561: aload_2        
        //   562: astore          7
        //   564: aload_0        
        //   565: astore          8
        //   567: new             Ljava/lang/StringBuilder;
        //   570: dup            
        //   571: invokespecial   java/lang/StringBuilder.<init>:()V
        //   574: astore          10
        //   576: aload_3        
        //   577: astore          6
        //   579: aload_2        
        //   580: astore          7
        //   582: aload_0        
        //   583: astore          8
        //   585: aload           10
        //   587: ldc             "Exception during download of file from url : "
        //   589: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   592: pop            
        //   593: aload_3        
        //   594: astore          6
        //   596: aload_2        
        //   597: astore          7
        //   599: aload_0        
        //   600: astore          8
        //   602: aload           10
        //   604: aload_1        
        //   605: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   608: pop            
        //   609: aload_3        
        //   610: astore          6
        //   612: aload_2        
        //   613: astore          7
        //   615: aload_0        
        //   616: astore          8
        //   618: aload           9
        //   620: aload           10
        //   622: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   625: aload           5
        //   627: invokestatic    com/appboy/support/AppboyLogger.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //   630: pop            
        //   631: aload_0        
        //   632: ifnull          639
        //   635: aload_0        
        //   636: invokevirtual   java/net/HttpURLConnection.disconnect:()V
        //   639: aload_3        
        //   640: ifnull          650
        //   643: aload_3        
        //   644: invokevirtual   java/io/DataInputStream.close:()V
        //   647: goto            650
        //   650: aload_2        
        //   651: ifnull          670
        //   654: aload_2        
        //   655: invokevirtual   java/io/BufferedOutputStream.close:()V
        //   658: aconst_null    
        //   659: areturn        
        //   660: getstatic       com/appboy/support/AppboyFileUtils.a:Ljava/lang/String;
        //   663: ldc             "Exception during closing of file download streams."
        //   665: aload_0        
        //   666: invokestatic    com/appboy/support/AppboyLogger.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //   669: pop            
        //   670: aconst_null    
        //   671: areturn        
        //   672: astore_0       
        //   673: aload           8
        //   675: ifnull          683
        //   678: aload           8
        //   680: invokevirtual   java/net/HttpURLConnection.disconnect:()V
        //   683: aload           6
        //   685: ifnull          696
        //   688: aload           6
        //   690: invokevirtual   java/io/DataInputStream.close:()V
        //   693: goto            696
        //   696: aload           7
        //   698: ifnull          719
        //   701: aload           7
        //   703: invokevirtual   java/io/BufferedOutputStream.close:()V
        //   706: goto            719
        //   709: getstatic       com/appboy/support/AppboyFileUtils.a:Ljava/lang/String;
        //   712: ldc             "Exception during closing of file download streams."
        //   714: aload_1        
        //   715: invokestatic    com/appboy/support/AppboyLogger.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //   718: pop            
        //   719: aload_0        
        //   720: athrow         
        //   721: astore_0       
        //   722: goto            525
        //   725: astore_0       
        //   726: goto            660
        //   729: astore_1       
        //   730: goto            709
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  95     107    537    544    Ljava/lang/Exception;
        //  95     107    402    409    Any
        //  110    147    537    544    Ljava/lang/Exception;
        //  110    147    402    409    Any
        //  147    174    537    544    Ljava/lang/Exception;
        //  147    174    402    409    Any
        //  174    180    393    395    Ljava/lang/Exception;
        //  174    180    384    386    Any
        //  188    243    393    395    Ljava/lang/Exception;
        //  188    243    384    386    Any
        //  253    272    393    395    Ljava/lang/Exception;
        //  253    272    384    386    Any
        //  272    289    377    384    Ljava/lang/Exception;
        //  272    289    370    377    Any
        //  289    297    365    370    Ljava/lang/Exception;
        //  289    297    360    365    Any
        //  303    312    365    370    Ljava/lang/Exception;
        //  303    312    360    365    Any
        //  315    327    365    370    Ljava/lang/Exception;
        //  315    327    360    365    Any
        //  335    343    346    360    Ljava/lang/Exception;
        //  418    423    672    721    Any
        //  432    441    672    721    Any
        //  450    458    672    721    Any
        //  467    474    672    721    Any
        //  483    496    672    721    Any
        //  508    512    721    535    Ljava/lang/Exception;
        //  519    523    721    535    Ljava/lang/Exception;
        //  553    558    672    721    Any
        //  567    576    672    721    Any
        //  585    593    672    721    Any
        //  602    609    672    721    Any
        //  618    631    672    721    Any
        //  643    647    725    670    Ljava/lang/Exception;
        //  654    658    725    670    Ljava/lang/Exception;
        //  688    693    729    719    Ljava/lang/Exception;
        //  701    706    729    719    Ljava/lang/Exception;
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index: 389, Size: 389
        //     at java.util.ArrayList.rangeCheck(Unknown Source)
        //     at java.util.ArrayList.get(Unknown Source)
        //     at com.strobel.decompiler.ast.AstBuilder.convertToAst(AstBuilder.java:3321)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:113)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:210)
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
    
    public static String getAssetFileStringContents(final AssetManager p0, final String p1) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: astore_3       
        //     2: aload_0        
        //     3: aload_1        
        //     4: invokevirtual   android/content/res/AssetManager.open:(Ljava/lang/String;)Ljava/io/InputStream;
        //     7: astore_2       
        //     8: new             Ljava/io/BufferedReader;
        //    11: dup            
        //    12: new             Ljava/io/InputStreamReader;
        //    15: dup            
        //    16: aload_2        
        //    17: ldc             "UTF-8"
        //    19: invokespecial   java/io/InputStreamReader.<init>:(Ljava/io/InputStream;Ljava/lang/String;)V
        //    22: invokespecial   java/io/BufferedReader.<init>:(Ljava/io/Reader;)V
        //    25: astore_0       
        //    26: aload_0        
        //    27: astore_3       
        //    28: aload_2        
        //    29: astore          4
        //    31: new             Ljava/lang/StringBuilder;
        //    34: dup            
        //    35: invokespecial   java/lang/StringBuilder.<init>:()V
        //    38: astore          5
        //    40: aload_0        
        //    41: astore_3       
        //    42: aload_2        
        //    43: astore          4
        //    45: aload_0        
        //    46: invokevirtual   java/io/BufferedReader.readLine:()Ljava/lang/String;
        //    49: astore          6
        //    51: aload           6
        //    53: ifnull          85
        //    56: aload_0        
        //    57: astore_3       
        //    58: aload_2        
        //    59: astore          4
        //    61: aload           5
        //    63: aload           6
        //    65: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    68: pop            
        //    69: aload_0        
        //    70: astore_3       
        //    71: aload_2        
        //    72: astore          4
        //    74: aload           5
        //    76: bipush          10
        //    78: invokevirtual   java/lang/StringBuilder.append:(C)Ljava/lang/StringBuilder;
        //    81: pop            
        //    82: goto            40
        //    85: aload_0        
        //    86: astore_3       
        //    87: aload_2        
        //    88: astore          4
        //    90: aload           5
        //    92: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //    95: astore          5
        //    97: aload_2        
        //    98: ifnull          108
        //   101: aload_2        
        //   102: invokevirtual   java/io/InputStream.close:()V
        //   105: goto            108
        //   108: aload_0        
        //   109: invokevirtual   java/io/BufferedReader.close:()V
        //   112: aload           5
        //   114: areturn        
        //   115: getstatic       com/appboy/support/AppboyFileUtils.a:Ljava/lang/String;
        //   118: astore_0       
        //   119: new             Ljava/lang/StringBuilder;
        //   122: dup            
        //   123: invokespecial   java/lang/StringBuilder.<init>:()V
        //   126: astore_2       
        //   127: aload_2        
        //   128: ldc             "Exception attempting to close file download streams for path:"
        //   130: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   133: pop            
        //   134: aload_2        
        //   135: aload_1        
        //   136: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   139: pop            
        //   140: aload_0        
        //   141: aload_2        
        //   142: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   145: aload_3        
        //   146: invokestatic    com/appboy/support/AppboyLogger.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //   149: pop            
        //   150: aload           5
        //   152: areturn        
        //   153: astore          5
        //   155: goto            189
        //   158: astore_0       
        //   159: aconst_null    
        //   160: astore_3       
        //   161: goto            318
        //   164: astore          5
        //   166: aconst_null    
        //   167: astore_0       
        //   168: goto            189
        //   171: astore_0       
        //   172: aconst_null    
        //   173: astore          4
        //   175: aload_3        
        //   176: astore_2       
        //   177: aload           4
        //   179: astore_3       
        //   180: goto            318
        //   183: astore          5
        //   185: aconst_null    
        //   186: astore_2       
        //   187: aload_2        
        //   188: astore_0       
        //   189: aload_0        
        //   190: astore_3       
        //   191: aload_2        
        //   192: astore          4
        //   194: getstatic       com/appboy/support/AppboyFileUtils.a:Ljava/lang/String;
        //   197: astore          6
        //   199: aload_0        
        //   200: astore_3       
        //   201: aload_2        
        //   202: astore          4
        //   204: new             Ljava/lang/StringBuilder;
        //   207: dup            
        //   208: invokespecial   java/lang/StringBuilder.<init>:()V
        //   211: astore          7
        //   213: aload_0        
        //   214: astore_3       
        //   215: aload_2        
        //   216: astore          4
        //   218: aload           7
        //   220: ldc             "Exception attempting to get asset content for "
        //   222: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   225: pop            
        //   226: aload_0        
        //   227: astore_3       
        //   228: aload_2        
        //   229: astore          4
        //   231: aload           7
        //   233: aload_1        
        //   234: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   237: pop            
        //   238: aload_0        
        //   239: astore_3       
        //   240: aload_2        
        //   241: astore          4
        //   243: aload           6
        //   245: aload           7
        //   247: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   250: aload           5
        //   252: invokestatic    com/appboy/support/AppboyLogger.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //   255: pop            
        //   256: aload_2        
        //   257: ifnull          267
        //   260: aload_2        
        //   261: invokevirtual   java/io/InputStream.close:()V
        //   264: goto            267
        //   267: aload_0        
        //   268: ifnull          312
        //   271: aload_0        
        //   272: invokevirtual   java/io/BufferedReader.close:()V
        //   275: aconst_null    
        //   276: areturn        
        //   277: getstatic       com/appboy/support/AppboyFileUtils.a:Ljava/lang/String;
        //   280: astore_0       
        //   281: new             Ljava/lang/StringBuilder;
        //   284: dup            
        //   285: invokespecial   java/lang/StringBuilder.<init>:()V
        //   288: astore_2       
        //   289: aload_2        
        //   290: ldc             "Exception attempting to close file download streams for path:"
        //   292: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   295: pop            
        //   296: aload_2        
        //   297: aload_1        
        //   298: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   301: pop            
        //   302: aload_0        
        //   303: aload_2        
        //   304: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   307: aload_3        
        //   308: invokestatic    com/appboy/support/AppboyLogger.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //   311: pop            
        //   312: aconst_null    
        //   313: areturn        
        //   314: astore_0       
        //   315: aload           4
        //   317: astore_2       
        //   318: aload_2        
        //   319: ifnull          329
        //   322: aload_2        
        //   323: invokevirtual   java/io/InputStream.close:()V
        //   326: goto            329
        //   329: aload_3        
        //   330: ifnull          376
        //   333: aload_3        
        //   334: invokevirtual   java/io/BufferedReader.close:()V
        //   337: goto            376
        //   340: getstatic       com/appboy/support/AppboyFileUtils.a:Ljava/lang/String;
        //   343: astore_2       
        //   344: new             Ljava/lang/StringBuilder;
        //   347: dup            
        //   348: invokespecial   java/lang/StringBuilder.<init>:()V
        //   351: astore_3       
        //   352: aload_3        
        //   353: ldc             "Exception attempting to close file download streams for path:"
        //   355: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   358: pop            
        //   359: aload_3        
        //   360: aload_1        
        //   361: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   364: pop            
        //   365: aload_2        
        //   366: aload_3        
        //   367: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   370: aload           4
        //   372: invokestatic    com/appboy/support/AppboyLogger.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //   375: pop            
        //   376: aload_0        
        //   377: athrow         
        //   378: astore_3       
        //   379: goto            115
        //   382: astore_3       
        //   383: goto            277
        //   386: astore          4
        //   388: goto            340
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  2      8      183    189    Ljava/lang/Exception;
        //  2      8      171    183    Any
        //  8      26     164    171    Ljava/lang/Exception;
        //  8      26     158    164    Any
        //  31     40     153    158    Ljava/lang/Exception;
        //  31     40     314    318    Any
        //  45     51     153    158    Ljava/lang/Exception;
        //  45     51     314    318    Any
        //  61     69     153    158    Ljava/lang/Exception;
        //  61     69     314    318    Any
        //  74     82     153    158    Ljava/lang/Exception;
        //  74     82     314    318    Any
        //  90     97     153    158    Ljava/lang/Exception;
        //  90     97     314    318    Any
        //  101    105    378    153    Ljava/lang/Exception;
        //  108    112    378    153    Ljava/lang/Exception;
        //  194    199    314    318    Any
        //  204    213    314    318    Any
        //  218    226    314    318    Any
        //  231    238    314    318    Any
        //  243    256    314    318    Any
        //  260    264    382    312    Ljava/lang/Exception;
        //  271    275    382    312    Ljava/lang/Exception;
        //  322    326    386    376    Ljava/lang/Exception;
        //  333    337    386    376    Ljava/lang/Exception;
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index: 224, Size: 224
        //     at java.util.ArrayList.rangeCheck(Unknown Source)
        //     at java.util.ArrayList.get(Unknown Source)
        //     at com.strobel.decompiler.ast.AstBuilder.convertToAst(AstBuilder.java:3321)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:113)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:210)
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
    
    public static boolean isLocalUri(final Uri uri) {
        boolean b = false;
        if (uri == null) {
            AppboyLogger.i(AppboyFileUtils.a, "Null Uri received.");
            return false;
        }
        final String scheme = uri.getScheme();
        if (StringUtils.isNullOrBlank(scheme) || scheme.equals("file")) {
            b = true;
        }
        return b;
    }
    
    public static boolean isRemoteUri(final Uri uri) {
        String s;
        String s2;
        if (uri == null) {
            s = AppboyFileUtils.a;
            s2 = "Null Uri received.";
        }
        else {
            final String scheme = uri.getScheme();
            if (!StringUtils.isNullOrBlank(scheme)) {
                return AppboyFileUtils.REMOTE_SCHEMES.contains(scheme);
            }
            s = AppboyFileUtils.a;
            s2 = "Null or blank Uri scheme.";
        }
        AppboyLogger.i(s, s2);
        return false;
    }
}
