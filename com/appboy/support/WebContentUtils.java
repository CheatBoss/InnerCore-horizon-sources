package com.appboy.support;

import java.io.*;
import android.content.*;

public class WebContentUtils
{
    private static final String a;
    
    static {
        a = AppboyLogger.getAppboyLogTag(WebContentUtils.class);
    }
    
    static boolean a(final String p0, final File p1) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokestatic    com/appboy/support/StringUtils.isNullOrBlank:(Ljava/lang/String;)Z
        //     4: ifeq            22
        //     7: getstatic       com/appboy/support/WebContentUtils.a:Ljava/lang/String;
        //    10: astore_0       
        //    11: ldc             "Unpack directory null or blank. Zip file not unpacked."
        //    13: astore_1       
        //    14: aload_0        
        //    15: aload_1        
        //    16: invokestatic    com/appboy/support/AppboyLogger.i:(Ljava/lang/String;Ljava/lang/String;)I
        //    19: pop            
        //    20: iconst_0       
        //    21: ireturn        
        //    22: aload_1        
        //    23: ifnonnull       36
        //    26: getstatic       com/appboy/support/WebContentUtils.a:Ljava/lang/String;
        //    29: astore_0       
        //    30: ldc             "Zip file is null. Zip file not unpacked."
        //    32: astore_1       
        //    33: goto            14
        //    36: new             Ljava/io/File;
        //    39: dup            
        //    40: aload_0        
        //    41: invokespecial   java/io/File.<init>:(Ljava/lang/String;)V
        //    44: invokevirtual   java/io/File.mkdirs:()Z
        //    47: pop            
        //    48: aconst_null    
        //    49: astore          12
        //    51: aconst_null    
        //    52: astore          5
        //    54: aconst_null    
        //    55: astore          11
        //    57: aconst_null    
        //    58: astore          10
        //    60: aconst_null    
        //    61: astore          6
        //    63: aconst_null    
        //    64: astore          7
        //    66: aconst_null    
        //    67: astore          9
        //    69: aconst_null    
        //    70: astore          4
        //    72: aconst_null    
        //    73: astore          8
        //    75: new             Ljava/util/zip/ZipInputStream;
        //    78: dup            
        //    79: new             Ljava/io/BufferedInputStream;
        //    82: dup            
        //    83: new             Ljava/io/FileInputStream;
        //    86: dup            
        //    87: aload_1        
        //    88: invokespecial   java/io/FileInputStream.<init>:(Ljava/io/File;)V
        //    91: invokespecial   java/io/BufferedInputStream.<init>:(Ljava/io/InputStream;)V
        //    94: invokespecial   java/util/zip/ZipInputStream.<init>:(Ljava/io/InputStream;)V
        //    97: astore_3       
        //    98: aload           12
        //   100: astore          4
        //   102: aload           11
        //   104: astore          6
        //   106: aload           10
        //   108: astore          7
        //   110: sipush          8192
        //   113: newarray        B
        //   115: astore          9
        //   117: aload           8
        //   119: astore_1       
        //   120: aload_1        
        //   121: astore          4
        //   123: aload_1        
        //   124: astore          5
        //   126: aload_1        
        //   127: astore          6
        //   129: aload_1        
        //   130: astore          7
        //   132: aload_3        
        //   133: invokevirtual   java/util/zip/ZipInputStream.getNextEntry:()Ljava/util/zip/ZipEntry;
        //   136: astore          8
        //   138: aload           8
        //   140: ifnull          434
        //   143: aload_1        
        //   144: astore          4
        //   146: aload_1        
        //   147: astore          5
        //   149: aload_1        
        //   150: astore          6
        //   152: aload_1        
        //   153: astore          7
        //   155: aload           8
        //   157: invokevirtual   java/util/zip/ZipEntry.getName:()Ljava/lang/String;
        //   160: astore          10
        //   162: aload_1        
        //   163: astore          4
        //   165: aload_1        
        //   166: astore          5
        //   168: aload_1        
        //   169: astore          6
        //   171: aload_1        
        //   172: astore          7
        //   174: aload           10
        //   176: getstatic       java/util/Locale.US:Ljava/util/Locale;
        //   179: invokevirtual   java/lang/String.toLowerCase:(Ljava/util/Locale;)Ljava/lang/String;
        //   182: ldc             "__macosx"
        //   184: invokevirtual   java/lang/String.startsWith:(Ljava/lang/String;)Z
        //   187: ifeq            193
        //   190: goto            120
        //   193: aload_1        
        //   194: astore          4
        //   196: aload_1        
        //   197: astore          5
        //   199: aload_1        
        //   200: astore          6
        //   202: aload_1        
        //   203: astore          7
        //   205: new             Ljava/lang/StringBuilder;
        //   208: dup            
        //   209: invokespecial   java/lang/StringBuilder.<init>:()V
        //   212: astore          11
        //   214: aload_1        
        //   215: astore          4
        //   217: aload_1        
        //   218: astore          5
        //   220: aload_1        
        //   221: astore          6
        //   223: aload_1        
        //   224: astore          7
        //   226: aload           11
        //   228: aload_0        
        //   229: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   232: pop            
        //   233: aload_1        
        //   234: astore          4
        //   236: aload_1        
        //   237: astore          5
        //   239: aload_1        
        //   240: astore          6
        //   242: aload_1        
        //   243: astore          7
        //   245: aload           11
        //   247: ldc             "/"
        //   249: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   252: pop            
        //   253: aload_1        
        //   254: astore          4
        //   256: aload_1        
        //   257: astore          5
        //   259: aload_1        
        //   260: astore          6
        //   262: aload_1        
        //   263: astore          7
        //   265: aload           11
        //   267: aload           10
        //   269: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   272: pop            
        //   273: aload_1        
        //   274: astore          4
        //   276: aload_1        
        //   277: astore          5
        //   279: aload_1        
        //   280: astore          6
        //   282: aload_1        
        //   283: astore          7
        //   285: aload           11
        //   287: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   290: astore          10
        //   292: aload_1        
        //   293: astore          4
        //   295: aload_1        
        //   296: astore          5
        //   298: aload_1        
        //   299: astore          6
        //   301: aload_1        
        //   302: astore          7
        //   304: aload           8
        //   306: invokevirtual   java/util/zip/ZipEntry.isDirectory:()Z
        //   309: ifeq            340
        //   312: aload_1        
        //   313: astore          4
        //   315: aload_1        
        //   316: astore          5
        //   318: aload_1        
        //   319: astore          6
        //   321: aload_1        
        //   322: astore          7
        //   324: new             Ljava/io/File;
        //   327: dup            
        //   328: aload           10
        //   330: invokespecial   java/io/File.<init>:(Ljava/lang/String;)V
        //   333: invokevirtual   java/io/File.mkdirs:()Z
        //   336: pop            
        //   337: goto            120
        //   340: aload_1        
        //   341: astore          4
        //   343: aload_1        
        //   344: astore          5
        //   346: aload_1        
        //   347: astore          6
        //   349: aload_1        
        //   350: astore          7
        //   352: new             Ljava/io/BufferedOutputStream;
        //   355: dup            
        //   356: new             Ljava/io/FileOutputStream;
        //   359: dup            
        //   360: aload           10
        //   362: invokespecial   java/io/FileOutputStream.<init>:(Ljava/lang/String;)V
        //   365: invokespecial   java/io/BufferedOutputStream.<init>:(Ljava/io/OutputStream;)V
        //   368: astore_1       
        //   369: aload_3        
        //   370: aload           9
        //   372: invokevirtual   java/util/zip/ZipInputStream.read:([B)I
        //   375: istore_2       
        //   376: iload_2        
        //   377: iconst_m1      
        //   378: if_icmpeq       392
        //   381: aload_1        
        //   382: aload           9
        //   384: iconst_0       
        //   385: iload_2        
        //   386: invokevirtual   java/io/BufferedOutputStream.write:([BII)V
        //   389: goto            369
        //   392: aload_1        
        //   393: invokevirtual   java/io/BufferedOutputStream.close:()V
        //   396: aload_3        
        //   397: invokevirtual   java/util/zip/ZipInputStream.closeEntry:()V
        //   400: goto            120
        //   403: astore          4
        //   405: aload_1        
        //   406: astore_0       
        //   407: aload           4
        //   409: astore_1       
        //   410: goto            481
        //   413: astore_0       
        //   414: aload_1        
        //   415: astore          5
        //   417: goto            485
        //   420: astore_0       
        //   421: aload_1        
        //   422: astore          6
        //   424: goto            496
        //   427: astore_0       
        //   428: aload_1        
        //   429: astore          7
        //   431: goto            507
        //   434: aload_1        
        //   435: astore          4
        //   437: aload_1        
        //   438: astore          5
        //   440: aload_1        
        //   441: astore          6
        //   443: aload_1        
        //   444: astore          7
        //   446: aload_3        
        //   447: invokevirtual   java/util/zip/ZipInputStream.close:()V
        //   450: aload_3        
        //   451: invokevirtual   java/util/zip/ZipInputStream.close:()V
        //   454: aload_1        
        //   455: ifnull          475
        //   458: aload_1        
        //   459: invokevirtual   java/io/BufferedOutputStream.close:()V
        //   462: iconst_1       
        //   463: ireturn        
        //   464: astore_0       
        //   465: getstatic       com/appboy/support/WebContentUtils.a:Ljava/lang/String;
        //   468: ldc             "IOException during closing of zip file unpacking streams."
        //   470: aload_0        
        //   471: invokestatic    com/appboy/support/AppboyLogger.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //   474: pop            
        //   475: iconst_1       
        //   476: ireturn        
        //   477: astore_1       
        //   478: aload           4
        //   480: astore_0       
        //   481: goto            714
        //   484: astore_0       
        //   485: aload           5
        //   487: astore          4
        //   489: aload_0        
        //   490: astore          5
        //   492: goto            534
        //   495: astore_0       
        //   496: aload           6
        //   498: astore          4
        //   500: aload_0        
        //   501: astore          5
        //   503: goto            593
        //   506: astore_0       
        //   507: aload           7
        //   509: astore          4
        //   511: aload_0        
        //   512: astore          5
        //   514: goto            652
        //   517: astore_1       
        //   518: aconst_null    
        //   519: astore_3       
        //   520: aload           4
        //   522: astore_0       
        //   523: goto            714
        //   526: astore          5
        //   528: aconst_null    
        //   529: astore          4
        //   531: aload           6
        //   533: astore_3       
        //   534: aload_3        
        //   535: astore_0       
        //   536: aload           4
        //   538: astore_1       
        //   539: getstatic       com/appboy/support/WebContentUtils.a:Ljava/lang/String;
        //   542: ldc             "Exception during unpack of zip file."
        //   544: aload           5
        //   546: invokestatic    com/appboy/support/AppboyLogger.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //   549: pop            
        //   550: aload_3        
        //   551: ifnull          561
        //   554: aload_3        
        //   555: invokevirtual   java/util/zip/ZipInputStream.close:()V
        //   558: goto            561
        //   561: aload           4
        //   563: ifnull          583
        //   566: aload           4
        //   568: invokevirtual   java/io/BufferedOutputStream.close:()V
        //   571: iconst_0       
        //   572: ireturn        
        //   573: getstatic       com/appboy/support/WebContentUtils.a:Ljava/lang/String;
        //   576: ldc             "IOException during closing of zip file unpacking streams."
        //   578: aload_0        
        //   579: invokestatic    com/appboy/support/AppboyLogger.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //   582: pop            
        //   583: iconst_0       
        //   584: ireturn        
        //   585: astore          5
        //   587: aconst_null    
        //   588: astore          4
        //   590: aload           7
        //   592: astore_3       
        //   593: aload_3        
        //   594: astore_0       
        //   595: aload           4
        //   597: astore_1       
        //   598: getstatic       com/appboy/support/WebContentUtils.a:Ljava/lang/String;
        //   601: ldc             "IOException during unpack of zip file."
        //   603: aload           5
        //   605: invokestatic    com/appboy/support/AppboyLogger.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //   608: pop            
        //   609: aload_3        
        //   610: ifnull          620
        //   613: aload_3        
        //   614: invokevirtual   java/util/zip/ZipInputStream.close:()V
        //   617: goto            620
        //   620: aload           4
        //   622: ifnull          642
        //   625: aload           4
        //   627: invokevirtual   java/io/BufferedOutputStream.close:()V
        //   630: iconst_0       
        //   631: ireturn        
        //   632: getstatic       com/appboy/support/WebContentUtils.a:Ljava/lang/String;
        //   635: ldc             "IOException during closing of zip file unpacking streams."
        //   637: aload_0        
        //   638: invokestatic    com/appboy/support/AppboyLogger.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //   641: pop            
        //   642: iconst_0       
        //   643: ireturn        
        //   644: astore          5
        //   646: aconst_null    
        //   647: astore          4
        //   649: aload           9
        //   651: astore_3       
        //   652: aload_3        
        //   653: astore_0       
        //   654: aload           4
        //   656: astore_1       
        //   657: getstatic       com/appboy/support/WebContentUtils.a:Ljava/lang/String;
        //   660: ldc             "FileNotFoundException during unpack of zip file."
        //   662: aload           5
        //   664: invokestatic    com/appboy/support/AppboyLogger.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //   667: pop            
        //   668: aload_3        
        //   669: ifnull          679
        //   672: aload_3        
        //   673: invokevirtual   java/util/zip/ZipInputStream.close:()V
        //   676: goto            679
        //   679: aload           4
        //   681: ifnull          701
        //   684: aload           4
        //   686: invokevirtual   java/io/BufferedOutputStream.close:()V
        //   689: iconst_0       
        //   690: ireturn        
        //   691: getstatic       com/appboy/support/WebContentUtils.a:Ljava/lang/String;
        //   694: ldc             "IOException during closing of zip file unpacking streams."
        //   696: aload_0        
        //   697: invokestatic    com/appboy/support/AppboyLogger.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //   700: pop            
        //   701: iconst_0       
        //   702: ireturn        
        //   703: astore_3       
        //   704: aload_1        
        //   705: astore          4
        //   707: aload_3        
        //   708: astore_1       
        //   709: aload_0        
        //   710: astore_3       
        //   711: aload           4
        //   713: astore_0       
        //   714: aload_3        
        //   715: ifnull          725
        //   718: aload_3        
        //   719: invokevirtual   java/util/zip/ZipInputStream.close:()V
        //   722: goto            725
        //   725: aload_0        
        //   726: ifnull          746
        //   729: aload_0        
        //   730: invokevirtual   java/io/BufferedOutputStream.close:()V
        //   733: goto            746
        //   736: getstatic       com/appboy/support/WebContentUtils.a:Ljava/lang/String;
        //   739: ldc             "IOException during closing of zip file unpacking streams."
        //   741: aload_0        
        //   742: invokestatic    com/appboy/support/AppboyLogger.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //   745: pop            
        //   746: aload_1        
        //   747: athrow         
        //   748: astore_0       
        //   749: goto            573
        //   752: astore_0       
        //   753: goto            632
        //   756: astore_0       
        //   757: goto            691
        //   760: astore_0       
        //   761: goto            736
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                           
        //  -----  -----  -----  -----  -------------------------------
        //  75     98     644    652    Ljava/io/FileNotFoundException;
        //  75     98     585    593    Ljava/io/IOException;
        //  75     98     526    534    Ljava/lang/Exception;
        //  75     98     517    526    Any
        //  110    117    506    507    Ljava/io/FileNotFoundException;
        //  110    117    495    496    Ljava/io/IOException;
        //  110    117    484    485    Ljava/lang/Exception;
        //  110    117    477    481    Any
        //  132    138    506    507    Ljava/io/FileNotFoundException;
        //  132    138    495    496    Ljava/io/IOException;
        //  132    138    484    485    Ljava/lang/Exception;
        //  132    138    477    481    Any
        //  155    162    506    507    Ljava/io/FileNotFoundException;
        //  155    162    495    496    Ljava/io/IOException;
        //  155    162    484    485    Ljava/lang/Exception;
        //  155    162    477    481    Any
        //  174    190    506    507    Ljava/io/FileNotFoundException;
        //  174    190    495    496    Ljava/io/IOException;
        //  174    190    484    485    Ljava/lang/Exception;
        //  174    190    477    481    Any
        //  205    214    506    507    Ljava/io/FileNotFoundException;
        //  205    214    495    496    Ljava/io/IOException;
        //  205    214    484    485    Ljava/lang/Exception;
        //  205    214    477    481    Any
        //  226    233    506    507    Ljava/io/FileNotFoundException;
        //  226    233    495    496    Ljava/io/IOException;
        //  226    233    484    485    Ljava/lang/Exception;
        //  226    233    477    481    Any
        //  245    253    506    507    Ljava/io/FileNotFoundException;
        //  245    253    495    496    Ljava/io/IOException;
        //  245    253    484    485    Ljava/lang/Exception;
        //  245    253    477    481    Any
        //  265    273    506    507    Ljava/io/FileNotFoundException;
        //  265    273    495    496    Ljava/io/IOException;
        //  265    273    484    485    Ljava/lang/Exception;
        //  265    273    477    481    Any
        //  285    292    506    507    Ljava/io/FileNotFoundException;
        //  285    292    495    496    Ljava/io/IOException;
        //  285    292    484    485    Ljava/lang/Exception;
        //  285    292    477    481    Any
        //  304    312    506    507    Ljava/io/FileNotFoundException;
        //  304    312    495    496    Ljava/io/IOException;
        //  304    312    484    485    Ljava/lang/Exception;
        //  304    312    477    481    Any
        //  324    337    506    507    Ljava/io/FileNotFoundException;
        //  324    337    495    496    Ljava/io/IOException;
        //  324    337    484    485    Ljava/lang/Exception;
        //  324    337    477    481    Any
        //  352    369    506    507    Ljava/io/FileNotFoundException;
        //  352    369    495    496    Ljava/io/IOException;
        //  352    369    484    485    Ljava/lang/Exception;
        //  352    369    477    481    Any
        //  369    376    427    434    Ljava/io/FileNotFoundException;
        //  369    376    420    427    Ljava/io/IOException;
        //  369    376    413    420    Ljava/lang/Exception;
        //  369    376    403    413    Any
        //  381    389    427    434    Ljava/io/FileNotFoundException;
        //  381    389    420    427    Ljava/io/IOException;
        //  381    389    413    420    Ljava/lang/Exception;
        //  381    389    403    413    Any
        //  392    400    427    434    Ljava/io/FileNotFoundException;
        //  392    400    420    427    Ljava/io/IOException;
        //  392    400    413    420    Ljava/lang/Exception;
        //  392    400    403    413    Any
        //  446    450    506    507    Ljava/io/FileNotFoundException;
        //  446    450    495    496    Ljava/io/IOException;
        //  446    450    484    485    Ljava/lang/Exception;
        //  446    450    477    481    Any
        //  450    454    464    475    Ljava/io/IOException;
        //  458    462    464    475    Ljava/io/IOException;
        //  539    550    703    714    Any
        //  554    558    748    583    Ljava/io/IOException;
        //  566    571    748    583    Ljava/io/IOException;
        //  598    609    703    714    Any
        //  613    617    752    642    Ljava/io/IOException;
        //  625    630    752    642    Ljava/io/IOException;
        //  657    668    703    714    Any
        //  672    676    756    701    Ljava/io/IOException;
        //  684    689    756    701    Ljava/io/IOException;
        //  718    722    760    746    Ljava/io/IOException;
        //  729    733    760    746    Ljava/io/IOException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index: 426, Size: 426
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
    
    public static File getHtmlInAppMessageAssetCacheDirectory(final Context context) {
        final StringBuilder sb = new StringBuilder();
        sb.append(context.getCacheDir().getPath());
        sb.append("/");
        sb.append("appboy-html-inapp-messages");
        return new File(sb.toString());
    }
    
    public static String getLocalHtmlUrlFromRemoteUrl(File downloadFileToPath, final String s) {
        if (downloadFileToPath == null) {
            AppboyLogger.w(WebContentUtils.a, "Internal cache directory is null. No local URL will be created.");
            return null;
        }
        if (StringUtils.isNullOrBlank(s)) {
            AppboyLogger.w(WebContentUtils.a, "Remote zip url is null or empty. No local URL will be created.");
            return null;
        }
        final String absolutePath = downloadFileToPath.getAbsolutePath();
        final String value = String.valueOf(IntentUtils.getRequestCode());
        final StringBuilder sb = new StringBuilder();
        sb.append(absolutePath);
        sb.append("/");
        sb.append(value);
        final String string = sb.toString();
        final String a = WebContentUtils.a;
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("Starting download of url: ");
        sb2.append(s);
        AppboyLogger.d(a, sb2.toString());
        downloadFileToPath = AppboyFileUtils.downloadFileToPath(string, s, value, ".zip");
        if (downloadFileToPath == null) {
            AppboyLogger.d(WebContentUtils.a, "Could not download zip file to local storage.");
            AppboyFileUtils.deleteFileOrDirectory(new File(string));
            return null;
        }
        AppboyLogger.d(WebContentUtils.a, "Html content zip downloaded.");
        if (!a(string, downloadFileToPath)) {
            AppboyLogger.w(WebContentUtils.a, "Error during the zip unpack.");
            AppboyFileUtils.deleteFileOrDirectory(new File(string));
            return null;
        }
        AppboyLogger.d(WebContentUtils.a, "Html content zip unpacked.");
        return string;
    }
}
