package com.zhekasmirnov.innercore.api.mod.adaptedscript;

import com.zhekasmirnov.innercore.api.log.*;
import org.mozilla.javascript.annotations.*;
import com.zhekasmirnov.innercore.api.annotations.*;
import java.net.*;
import com.zhekasmirnov.innercore.api.mod.ui.*;
import android.graphics.*;
import com.zhekasmirnov.innercore.utils.*;
import com.zhekasmirnov.innercore.mod.executable.*;
import com.zhekasmirnov.innercore.api.*;
import java.util.*;
import java.io.*;
import com.zhekasmirnov.innercore.mod.build.*;
import com.zhekasmirnov.apparatus.api.container.*;
import com.zhekasmirnov.innercore.api.mod.recipes.workbench.*;
import org.mozilla.javascript.*;

public class PreferencesWindowAPI extends AdaptedScriptAPI
{
    @JSStaticFunction
    public static void log(final String s) {
        ICLog.d("PREFS", s);
    }
    
    @Override
    public String getName() {
        return "PrefsWinAPI";
    }
    
    @APIStaticModule
    public static class Network
    {
        @JSStaticFunction
        public static String downloadFile(final String p0, final Object p1) {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     1: ldc             Lcom/zhekasmirnov/innercore/api/mod/adaptedscript/PreferencesWindowAPI$Network$IDownloadHandler;.class
            //     3: invokestatic    org/mozilla/javascript/Context.jsToJava:(Ljava/lang/Object;Ljava/lang/Class;)Ljava/lang/Object;
            //     6: checkcast       Lcom/zhekasmirnov/innercore/api/mod/adaptedscript/PreferencesWindowAPI$Network$IDownloadHandler;
            //     9: astore          21
            //    11: aconst_null    
            //    12: astore          17
            //    14: aconst_null    
            //    15: astore          8
            //    17: aconst_null    
            //    18: astore          16
            //    20: aconst_null    
            //    21: astore_1       
            //    22: aconst_null    
            //    23: astore          19
            //    25: aconst_null    
            //    26: astore          20
            //    28: getstatic       com/zhekasmirnov/innercore/utils/FileTools.DIR_WORK:Ljava/lang/String;
            //    31: astore          10
            //    33: new             Ljava/lang/StringBuilder;
            //    36: dup            
            //    37: invokespecial   java/lang/StringBuilder.<init>:()V
            //    40: astore          9
            //    42: aload           9
            //    44: ldc             "temp/download/"
            //    46: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //    49: pop            
            //    50: aload           9
            //    52: aload_0        
            //    53: ldc             "[/\\\\ :.]"
            //    55: ldc             "_"
            //    57: invokevirtual   java/lang/String.replaceAll:(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
            //    60: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //    63: pop            
            //    64: aload           9
            //    66: ldc             ".icmod"
            //    68: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //    71: pop            
            //    72: new             Ljava/io/File;
            //    75: dup            
            //    76: aload           10
            //    78: aload           9
            //    80: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
            //    83: invokespecial   java/io/File.<init>:(Ljava/lang/String;Ljava/lang/String;)V
            //    86: astore          15
            //    88: aload           15
            //    90: invokestatic    com/zhekasmirnov/innercore/utils/FileTools.assureFileDir:(Ljava/io/File;)Z
            //    93: pop            
            //    94: aload           15
            //    96: invokevirtual   java/io/File.exists:()Z
            //    99: ifeq            108
            //   102: aload           15
            //   104: invokevirtual   java/io/File.delete:()Z
            //   107: pop            
            //   108: aload           8
            //   110: astore          13
            //   112: aload_1        
            //   113: astore          11
            //   115: aload           20
            //   117: astore          9
            //   119: aload           17
            //   121: astore          14
            //   123: aload           16
            //   125: astore          12
            //   127: aload           19
            //   129: astore          10
            //   131: new             Ljava/net/URL;
            //   134: dup            
            //   135: aload_0        
            //   136: invokespecial   java/net/URL.<init>:(Ljava/lang/String;)V
            //   139: astore          18
            //   141: aload           8
            //   143: astore          13
            //   145: aload_1        
            //   146: astore          11
            //   148: aload           20
            //   150: astore          9
            //   152: aload           17
            //   154: astore          14
            //   156: aload           16
            //   158: astore          12
            //   160: aload           19
            //   162: astore          10
            //   164: aload           18
            //   166: invokevirtual   java/net/URL.openConnection:()Ljava/net/URLConnection;
            //   169: checkcast       Ljava/net/HttpURLConnection;
            //   172: astore_0       
            //   173: aload           8
            //   175: astore          13
            //   177: aload_1        
            //   178: astore          11
            //   180: aload_0        
            //   181: astore          9
            //   183: aload           17
            //   185: astore          14
            //   187: aload           16
            //   189: astore          12
            //   191: aload_0        
            //   192: astore          10
            //   194: aload_0        
            //   195: invokevirtual   java/net/HttpURLConnection.connect:()V
            //   198: aload           8
            //   200: astore          13
            //   202: aload_1        
            //   203: astore          11
            //   205: aload_0        
            //   206: astore          9
            //   208: aload           17
            //   210: astore          14
            //   212: aload           16
            //   214: astore          12
            //   216: aload_0        
            //   217: astore          10
            //   219: aload_0        
            //   220: invokevirtual   java/net/HttpURLConnection.getResponseCode:()I
            //   223: sipush          200
            //   226: if_icmpeq       449
            //   229: aload           8
            //   231: astore          13
            //   233: aload_1        
            //   234: astore          11
            //   236: aload_0        
            //   237: astore          9
            //   239: aload           17
            //   241: astore          14
            //   243: aload           16
            //   245: astore          12
            //   247: aload_0        
            //   248: astore          10
            //   250: new             Ljava/lang/StringBuilder;
            //   253: dup            
            //   254: invokespecial   java/lang/StringBuilder.<init>:()V
            //   257: astore          15
            //   259: aload           8
            //   261: astore          13
            //   263: aload_1        
            //   264: astore          11
            //   266: aload_0        
            //   267: astore          9
            //   269: aload           17
            //   271: astore          14
            //   273: aload           16
            //   275: astore          12
            //   277: aload_0        
            //   278: astore          10
            //   280: aload           15
            //   282: ldc             "Server returned HTTP "
            //   284: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   287: pop            
            //   288: aload           8
            //   290: astore          13
            //   292: aload_1        
            //   293: astore          11
            //   295: aload_0        
            //   296: astore          9
            //   298: aload           17
            //   300: astore          14
            //   302: aload           16
            //   304: astore          12
            //   306: aload_0        
            //   307: astore          10
            //   309: aload           15
            //   311: aload_0        
            //   312: invokevirtual   java/net/HttpURLConnection.getResponseCode:()I
            //   315: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
            //   318: pop            
            //   319: aload           8
            //   321: astore          13
            //   323: aload_1        
            //   324: astore          11
            //   326: aload_0        
            //   327: astore          9
            //   329: aload           17
            //   331: astore          14
            //   333: aload           16
            //   335: astore          12
            //   337: aload_0        
            //   338: astore          10
            //   340: aload           15
            //   342: ldc             " "
            //   344: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   347: pop            
            //   348: aload           8
            //   350: astore          13
            //   352: aload_1        
            //   353: astore          11
            //   355: aload_0        
            //   356: astore          9
            //   358: aload           17
            //   360: astore          14
            //   362: aload           16
            //   364: astore          12
            //   366: aload_0        
            //   367: astore          10
            //   369: aload           15
            //   371: aload_0        
            //   372: invokevirtual   java/net/HttpURLConnection.getResponseMessage:()Ljava/lang/String;
            //   375: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   378: pop            
            //   379: aload           8
            //   381: astore          13
            //   383: aload_1        
            //   384: astore          11
            //   386: aload_0        
            //   387: astore          9
            //   389: aload           17
            //   391: astore          14
            //   393: aload           16
            //   395: astore          12
            //   397: aload_0        
            //   398: astore          10
            //   400: aload           21
            //   402: aload           15
            //   404: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
            //   407: invokeinterface com/zhekasmirnov/innercore/api/mod/adaptedscript/PreferencesWindowAPI$Network$IDownloadHandler.message:(Ljava/lang/String;)V
            //   412: iconst_0       
            //   413: ifeq            424
            //   416: new             Ljava/lang/NullPointerException;
            //   419: dup            
            //   420: invokespecial   java/lang/NullPointerException.<init>:()V
            //   423: athrow         
            //   424: iconst_0       
            //   425: ifeq            439
            //   428: new             Ljava/lang/NullPointerException;
            //   431: dup            
            //   432: invokespecial   java/lang/NullPointerException.<init>:()V
            //   435: athrow         
            //   436: goto            439
            //   439: aload_0        
            //   440: ifnull          447
            //   443: aload_0        
            //   444: invokevirtual   java/net/HttpURLConnection.disconnect:()V
            //   447: aconst_null    
            //   448: areturn        
            //   449: aload           8
            //   451: astore          13
            //   453: aload_1        
            //   454: astore          11
            //   456: aload_0        
            //   457: astore          9
            //   459: aload           17
            //   461: astore          14
            //   463: aload           16
            //   465: astore          12
            //   467: aload_0        
            //   468: astore          10
            //   470: aload_0        
            //   471: invokevirtual   java/net/HttpURLConnection.getContentLength:()I
            //   474: istore          4
            //   476: aload           8
            //   478: astore          13
            //   480: aload_1        
            //   481: astore          11
            //   483: aload_0        
            //   484: astore          9
            //   486: aload           17
            //   488: astore          14
            //   490: aload           16
            //   492: astore          12
            //   494: aload_0        
            //   495: astore          10
            //   497: aload_0        
            //   498: invokevirtual   java/net/HttpURLConnection.getInputStream:()Ljava/io/InputStream;
            //   501: astore          8
            //   503: aload           8
            //   505: astore          13
            //   507: aload_1        
            //   508: astore          11
            //   510: aload_0        
            //   511: astore          9
            //   513: aload           8
            //   515: astore          14
            //   517: aload           16
            //   519: astore          12
            //   521: aload_0        
            //   522: astore          10
            //   524: new             Ljava/io/FileOutputStream;
            //   527: dup            
            //   528: aload           15
            //   530: invokespecial   java/io/FileOutputStream.<init>:(Ljava/io/File;)V
            //   533: astore_1       
            //   534: aload           8
            //   536: astore          13
            //   538: aload_1        
            //   539: astore          11
            //   541: aload_0        
            //   542: astore          9
            //   544: aload           8
            //   546: astore          14
            //   548: aload_1        
            //   549: astore          12
            //   551: aload_0        
            //   552: astore          10
            //   554: sipush          4096
            //   557: newarray        B
            //   559: astore          17
            //   561: lconst_0       
            //   562: lstore          6
            //   564: aload           18
            //   566: astore          16
            //   568: aload           8
            //   570: astore          13
            //   572: aload_1        
            //   573: astore          11
            //   575: aload_0        
            //   576: astore          9
            //   578: aload           8
            //   580: astore          14
            //   582: aload_1        
            //   583: astore          12
            //   585: aload_0        
            //   586: astore          10
            //   588: aload           8
            //   590: aload           17
            //   592: invokevirtual   java/io/InputStream.read:([B)I
            //   595: istore_3       
            //   596: iload_3        
            //   597: iconst_m1      
            //   598: if_icmpeq       743
            //   601: aload           21
            //   603: invokeinterface com/zhekasmirnov/innercore/api/mod/adaptedscript/PreferencesWindowAPI$Network$IDownloadHandler.isCancelled:()Z
            //   608: istore          5
            //   610: iload           5
            //   612: ifeq            677
            //   615: aload           8
            //   617: astore          13
            //   619: aload_1        
            //   620: astore          11
            //   622: aload_0        
            //   623: astore          9
            //   625: aload           8
            //   627: astore          14
            //   629: aload_1        
            //   630: astore          12
            //   632: aload_0        
            //   633: astore          10
            //   635: aload           8
            //   637: invokevirtual   java/io/InputStream.close:()V
            //   640: aload_1        
            //   641: ifnull          651
            //   644: aload_1        
            //   645: invokevirtual   java/io/OutputStream.close:()V
            //   648: goto            651
            //   651: aload           8
            //   653: ifnull          667
            //   656: aload           8
            //   658: invokevirtual   java/io/InputStream.close:()V
            //   661: goto            667
            //   664: goto            667
            //   667: aload_0        
            //   668: ifnull          675
            //   671: aload_0        
            //   672: invokevirtual   java/net/HttpURLConnection.disconnect:()V
            //   675: aconst_null    
            //   676: areturn        
            //   677: lload           6
            //   679: iload_3        
            //   680: i2l            
            //   681: ladd           
            //   682: lstore          6
            //   684: iload           4
            //   686: ifle            708
            //   689: lload           6
            //   691: l2f            
            //   692: iload           4
            //   694: i2f            
            //   695: fdiv           
            //   696: fstore_2       
            //   697: aload           21
            //   699: fload_2        
            //   700: invokeinterface com/zhekasmirnov/innercore/api/mod/adaptedscript/PreferencesWindowAPI$Network$IDownloadHandler.progress:(F)V
            //   705: goto            708
            //   708: aload_1        
            //   709: aload           17
            //   711: iconst_0       
            //   712: iload_3        
            //   713: invokevirtual   java/io/OutputStream.write:([BII)V
            //   716: goto            568
            //   719: astore          9
            //   721: aload           8
            //   723: astore          10
            //   725: goto            887
            //   728: astore          10
            //   730: aload           8
            //   732: astore          9
            //   734: aload_0        
            //   735: astore          8
            //   737: aload           9
            //   739: astore_0       
            //   740: goto            828
            //   743: aload_1        
            //   744: ifnull          754
            //   747: aload_1        
            //   748: invokevirtual   java/io/OutputStream.close:()V
            //   751: goto            754
            //   754: aload           8
            //   756: ifnull          770
            //   759: aload           8
            //   761: invokevirtual   java/io/InputStream.close:()V
            //   764: goto            770
            //   767: goto            770
            //   770: aload_0        
            //   771: ifnull          781
            //   774: aload_0        
            //   775: invokevirtual   java/net/HttpURLConnection.disconnect:()V
            //   778: goto            781
            //   781: aload           15
            //   783: invokevirtual   java/io/File.getAbsolutePath:()Ljava/lang/String;
            //   786: areturn        
            //   787: astore          10
            //   789: aload           9
            //   791: astore          8
            //   793: aload           13
            //   795: astore_0       
            //   796: aload           11
            //   798: astore_1       
            //   799: aload           10
            //   801: astore          9
            //   803: aload_0        
            //   804: astore          10
            //   806: aload           8
            //   808: astore_0       
            //   809: goto            887
            //   812: astore          9
            //   814: aload           10
            //   816: astore          8
            //   818: aload           14
            //   820: astore_0       
            //   821: aload           12
            //   823: astore_1       
            //   824: aload           9
            //   826: astore          10
            //   828: aload           21
            //   830: aload           10
            //   832: invokevirtual   java/lang/Exception.toString:()Ljava/lang/String;
            //   835: invokeinterface com/zhekasmirnov/innercore/api/mod/adaptedscript/PreferencesWindowAPI$Network$IDownloadHandler.message:(Ljava/lang/String;)V
            //   840: aload           10
            //   842: invokevirtual   java/lang/Exception.printStackTrace:()V
            //   845: aload_1        
            //   846: ifnull          856
            //   849: aload_1        
            //   850: invokevirtual   java/io/OutputStream.close:()V
            //   853: goto            856
            //   856: aload_0        
            //   857: ifnull          870
            //   860: aload_0        
            //   861: invokevirtual   java/io/InputStream.close:()V
            //   864: goto            870
            //   867: goto            870
            //   870: aload           8
            //   872: ifnull          880
            //   875: aload           8
            //   877: invokevirtual   java/net/HttpURLConnection.disconnect:()V
            //   880: aconst_null    
            //   881: areturn        
            //   882: astore          9
            //   884: goto            803
            //   887: aload_1        
            //   888: ifnull          898
            //   891: aload_1        
            //   892: invokevirtual   java/io/OutputStream.close:()V
            //   895: goto            898
            //   898: aload           10
            //   900: ifnull          914
            //   903: aload           10
            //   905: invokevirtual   java/io/InputStream.close:()V
            //   908: goto            914
            //   911: goto            914
            //   914: aload_0        
            //   915: ifnull          922
            //   918: aload_0        
            //   919: invokevirtual   java/net/HttpURLConnection.disconnect:()V
            //   922: aload           9
            //   924: athrow         
            //   925: astore_1       
            //   926: goto            436
            //   929: astore_1       
            //   930: goto            664
            //   933: astore          9
            //   935: aload           8
            //   937: astore          10
            //   939: aload_0        
            //   940: astore          8
            //   942: aload           10
            //   944: astore_0       
            //   945: goto            803
            //   948: astore          9
            //   950: aload_0        
            //   951: astore          10
            //   953: aload           8
            //   955: astore_0       
            //   956: aload           10
            //   958: astore          8
            //   960: goto            824
            //   963: astore_1       
            //   964: goto            767
            //   967: astore_0       
            //   968: goto            867
            //   971: astore_1       
            //   972: goto            911
            //    Exceptions:
            //  Try           Handler
            //  Start  End    Start  End    Type                 
            //  -----  -----  -----  -----  ---------------------
            //  131    141    812    824    Ljava/lang/Exception;
            //  131    141    787    803    Any
            //  164    173    812    824    Ljava/lang/Exception;
            //  164    173    787    803    Any
            //  194    198    812    824    Ljava/lang/Exception;
            //  194    198    787    803    Any
            //  219    229    812    824    Ljava/lang/Exception;
            //  219    229    787    803    Any
            //  250    259    812    824    Ljava/lang/Exception;
            //  250    259    787    803    Any
            //  280    288    812    824    Ljava/lang/Exception;
            //  280    288    787    803    Any
            //  309    319    812    824    Ljava/lang/Exception;
            //  309    319    787    803    Any
            //  340    348    812    824    Ljava/lang/Exception;
            //  340    348    787    803    Any
            //  369    379    812    824    Ljava/lang/Exception;
            //  369    379    787    803    Any
            //  400    412    812    824    Ljava/lang/Exception;
            //  400    412    787    803    Any
            //  416    424    925    439    Ljava/io/IOException;
            //  428    436    925    439    Ljava/io/IOException;
            //  470    476    812    824    Ljava/lang/Exception;
            //  470    476    787    803    Any
            //  497    503    812    824    Ljava/lang/Exception;
            //  497    503    787    803    Any
            //  524    534    812    824    Ljava/lang/Exception;
            //  524    534    787    803    Any
            //  554    561    812    824    Ljava/lang/Exception;
            //  554    561    787    803    Any
            //  588    596    812    824    Ljava/lang/Exception;
            //  588    596    787    803    Any
            //  601    610    728    743    Ljava/lang/Exception;
            //  601    610    719    728    Any
            //  635    640    812    824    Ljava/lang/Exception;
            //  635    640    787    803    Any
            //  644    648    929    667    Ljava/io/IOException;
            //  656    661    929    667    Ljava/io/IOException;
            //  697    705    948    963    Ljava/lang/Exception;
            //  697    705    933    948    Any
            //  708    716    948    963    Ljava/lang/Exception;
            //  708    716    933    948    Any
            //  747    751    963    770    Ljava/io/IOException;
            //  759    764    963    770    Ljava/io/IOException;
            //  828    845    882    887    Any
            //  849    853    967    870    Ljava/io/IOException;
            //  860    864    967    870    Ljava/io/IOException;
            //  891    895    971    914    Ljava/io/IOException;
            //  903    908    971    914    Ljava/io/IOException;
            // 
            // The error that occurred was:
            // 
            // java.lang.IndexOutOfBoundsException: Index: 500, Size: 500
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
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:576)
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
        
        @JSStaticFunction
        public static String downloadIcon(String string) {
            try {
                final URL url = new URL(string);
                new BitmapFactory$Options().inScaled = false;
                final Bitmap decodeStream = BitmapFactory.decodeStream(url.openStream());
                final StringBuilder sb = new StringBuilder();
                sb.append("web_icn_");
                sb.append(string);
                string = sb.toString();
                if (decodeStream != null) {
                    com.zhekasmirnov.innercore.api.mod.ui.TextureSource.instance.put(string, decodeStream);
                }
                return string;
            }
            catch (IOException | OutOfMemoryError ex) {
                final Throwable t;
                t.printStackTrace();
                return "missing_texture";
            }
        }
        
        @JSStaticFunction
        public static String getURLContents(String string) {
            try {
                final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new URL(string).openStream()));
                final StringBuilder sb = new StringBuilder();
                while (true) {
                    final String line = bufferedReader.readLine();
                    if (line == null) {
                        break;
                    }
                    sb.append(line);
                }
                bufferedReader.close();
                string = sb.toString();
                return string;
            }
            catch (IOException ex) {
                ex.printStackTrace();
                return null;
            }
        }
        
        interface IDownloadHandler
        {
            boolean isCancelled();
            
            void message(final String p0);
            
            void progress(final float p0);
        }
    }
    
    @APIStaticModule
    public static class Prefs
    {
        @JSStaticFunction
        public static boolean compileMod(final Object o, final Object o2) {
            return Compiler.compileMod((Mod)Context.jsToJava(o, (Class)Mod.class), (IMessageReceiver)Context.jsToJava(o2, (Class)IMessageReceiver.class));
        }
        
        @JSStaticFunction
        public static com.zhekasmirnov.innercore.mod.build.Config getGlobalConfig() {
            return InnerCoreConfig.config;
        }
        
        @JSStaticFunction
        public static ArrayList<Mod> getModList() {
            return ModLoader.instance.modsList;
        }
        
        @JSStaticFunction
        public static ArrayList<String> installModFile(final String s, final Object o) {
            return ExtractionHelper.extractICModFile(new File(s), (IMessageReceiver)Context.jsToJava(o, (Class)IMessageReceiver.class), null);
        }
    }
    
    public static class WorkbenchRecipeListBuilder extends com.zhekasmirnov.innercore.api.mod.recipes.workbench.WorkbenchRecipeListBuilder
    {
        public WorkbenchRecipeListBuilder(final long n, final com.zhekasmirnov.apparatus.api.container.ItemContainer itemContainer) {
            super(n, itemContainer);
        }
    }
    
    public static class WorkbenchRecipeListProcessor extends com.zhekasmirnov.innercore.api.mod.recipes.workbench.WorkbenchRecipeListProcessor
    {
        public WorkbenchRecipeListProcessor(final ScriptableObject scriptableObject) {
            super(scriptableObject);
        }
    }
}
