package com.appsflyer.internal;

import android.content.*;
import android.net.*;

public final class s
{
    s() {
    }
    
    public static a \u01c3(final Context p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     2: astore          5
        //     4: aconst_null    
        //     5: astore          6
        //     7: aload           5
        //     9: astore          4
        //    11: aload_0        
        //    12: ldc             "connectivity"
        //    14: invokevirtual   android/content/Context.getSystemService:(Ljava/lang/String;)Ljava/lang/Object;
        //    17: checkcast       Landroid/net/ConnectivityManager;
        //    20: astore          7
        //    22: aload           5
        //    24: astore_3       
        //    25: aload           7
        //    27: ifnull          239
        //    30: aload           5
        //    32: astore          4
        //    34: getstatic       android/os/Build$VERSION.SDK_INT:I
        //    37: istore_2       
        //    38: iconst_0       
        //    39: istore_1       
        //    40: bipush          21
        //    42: iload_2        
        //    43: if_icmpgt       134
        //    46: aload           5
        //    48: astore          4
        //    50: aload           7
        //    52: invokevirtual   android/net/ConnectivityManager.getAllNetworks:()[Landroid/net/Network;
        //    55: astore          8
        //    57: aload           5
        //    59: astore          4
        //    61: aload           8
        //    63: arraylength    
        //    64: istore_2       
        //    65: aload           5
        //    67: astore_3       
        //    68: iload_1        
        //    69: iload_2        
        //    70: if_icmpge       239
        //    73: aload           5
        //    75: astore          4
        //    77: aload           7
        //    79: aload           8
        //    81: iload_1        
        //    82: aaload         
        //    83: invokevirtual   android/net/ConnectivityManager.getNetworkInfo:(Landroid/net/Network;)Landroid/net/NetworkInfo;
        //    86: astore          9
        //    88: aload           5
        //    90: astore          4
        //    92: aload           9
        //    94: invokestatic    com/appsflyer/internal/s.\u0269:(Landroid/net/NetworkInfo;)Z
        //    97: ifeq            382
        //   100: aload           5
        //   102: astore          4
        //   104: iconst_1       
        //   105: aload           9
        //   107: invokevirtual   android/net/NetworkInfo.getType:()I
        //   110: if_icmpne       116
        //   113: goto            213
        //   116: aload           5
        //   118: astore_3       
        //   119: aload           5
        //   121: astore          4
        //   123: aload           9
        //   125: invokevirtual   android/net/NetworkInfo.getType:()I
        //   128: ifne            239
        //   131: goto            236
        //   134: aload           5
        //   136: astore          4
        //   138: aload           7
        //   140: iconst_1       
        //   141: invokevirtual   android/net/ConnectivityManager.getNetworkInfo:(I)Landroid/net/NetworkInfo;
        //   144: invokestatic    com/appsflyer/internal/s.\u0269:(Landroid/net/NetworkInfo;)Z
        //   147: ifeq            153
        //   150: goto            213
        //   153: aload           5
        //   155: astore          4
        //   157: aload           7
        //   159: iconst_0       
        //   160: invokevirtual   android/net/ConnectivityManager.getNetworkInfo:(I)Landroid/net/NetworkInfo;
        //   163: invokestatic    com/appsflyer/internal/s.\u0269:(Landroid/net/NetworkInfo;)Z
        //   166: ifeq            172
        //   169: goto            236
        //   172: aload           5
        //   174: astore          4
        //   176: aload           7
        //   178: invokevirtual   android/net/ConnectivityManager.getActiveNetworkInfo:()Landroid/net/NetworkInfo;
        //   181: astore          7
        //   183: aload           5
        //   185: astore_3       
        //   186: aload           5
        //   188: astore          4
        //   190: aload           7
        //   192: invokestatic    com/appsflyer/internal/s.\u0269:(Landroid/net/NetworkInfo;)Z
        //   195: ifeq            239
        //   198: aload           5
        //   200: astore          4
        //   202: aload           7
        //   204: invokevirtual   android/net/NetworkInfo.getType:()I
        //   207: istore_1       
        //   208: iconst_1       
        //   209: iload_1        
        //   210: if_icmpne       219
        //   213: ldc             "WIFI"
        //   215: astore_3       
        //   216: goto            239
        //   219: aload           5
        //   221: astore          4
        //   223: aload           7
        //   225: invokevirtual   android/net/NetworkInfo.getType:()I
        //   228: istore_1       
        //   229: aload           5
        //   231: astore_3       
        //   232: iload_1        
        //   233: ifne            239
        //   236: ldc             "MOBILE"
        //   238: astore_3       
        //   239: aload_3        
        //   240: astore          4
        //   242: aload_0        
        //   243: ldc             "phone"
        //   245: invokevirtual   android/content/Context.getSystemService:(Ljava/lang/String;)Ljava/lang/Object;
        //   248: checkcast       Landroid/telephony/TelephonyManager;
        //   251: astore          8
        //   253: aload_3        
        //   254: astore          4
        //   256: aload           8
        //   258: invokevirtual   android/telephony/TelephonyManager.getSimOperatorName:()Ljava/lang/String;
        //   261: astore          5
        //   263: aload           8
        //   265: invokevirtual   android/telephony/TelephonyManager.getNetworkOperatorName:()Ljava/lang/String;
        //   268: astore          4
        //   270: aload           4
        //   272: ifnull          296
        //   275: aload_3        
        //   276: astore          7
        //   278: aload           5
        //   280: astore          6
        //   282: aload           4
        //   284: astore_0       
        //   285: aload           4
        //   287: invokevirtual   java/lang/String.isEmpty:()Z
        //   290: ifeq            369
        //   293: goto            296
        //   296: aload           8
        //   298: invokevirtual   android/telephony/TelephonyManager.getPhoneType:()I
        //   301: istore_1       
        //   302: aload_3        
        //   303: astore          7
        //   305: aload           5
        //   307: astore          6
        //   309: aload           4
        //   311: astore_0       
        //   312: iconst_2       
        //   313: iload_1        
        //   314: if_icmpne       369
        //   317: ldc             "CDMA"
        //   319: astore_0       
        //   320: aload_3        
        //   321: astore          7
        //   323: aload           5
        //   325: astore          6
        //   327: goto            369
        //   330: astore_0       
        //   331: aload           6
        //   333: astore          4
        //   335: aload           4
        //   337: astore          6
        //   339: goto            353
        //   342: astore_0       
        //   343: aconst_null    
        //   344: astore          5
        //   346: aload           5
        //   348: astore          6
        //   350: aload           4
        //   352: astore_3       
        //   353: ldc             "Exception while collecting network info. "
        //   355: aload_0        
        //   356: invokestatic    com/appsflyer/AFLogger.afErrorLog:(Ljava/lang/String;Ljava/lang/Throwable;)V
        //   359: aload           6
        //   361: astore_0       
        //   362: aload           5
        //   364: astore          6
        //   366: aload_3        
        //   367: astore          7
        //   369: new             Lcom/appsflyer/internal/s$a;
        //   372: dup            
        //   373: aload           7
        //   375: aload_0        
        //   376: aload           6
        //   378: invokespecial   com/appsflyer/internal/s$a.<init>:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V
        //   381: areturn        
        //   382: iload_1        
        //   383: iconst_1       
        //   384: iadd           
        //   385: istore_1       
        //   386: goto            65
        //   389: astore_0       
        //   390: goto            335
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type
        //  -----  -----  -----  -----  ----
        //  11     22     342    353    Any
        //  34     38     342    353    Any
        //  50     57     342    353    Any
        //  61     65     342    353    Any
        //  77     88     342    353    Any
        //  92     100    342    353    Any
        //  104    113    342    353    Any
        //  123    131    342    353    Any
        //  138    150    342    353    Any
        //  157    169    342    353    Any
        //  176    183    342    353    Any
        //  190    198    342    353    Any
        //  202    208    342    353    Any
        //  223    229    342    353    Any
        //  242    253    342    353    Any
        //  256    263    342    353    Any
        //  263    270    330    335    Any
        //  285    293    389    393    Any
        //  296    302    389    393    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        //     at com.strobel.assembler.ir.StackMappingVisitor.push(StackMappingVisitor.java:290)
        //     at com.strobel.assembler.ir.StackMappingVisitor$InstructionAnalyzer.execute(StackMappingVisitor.java:833)
        //     at com.strobel.assembler.ir.StackMappingVisitor$InstructionAnalyzer.visit(StackMappingVisitor.java:398)
        //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2030)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:108)
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
    
    private static boolean \u0269(final NetworkInfo networkInfo) {
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }
    
    public static final class a
    {
        public final String \u0131;
        public final String \u0269;
        public final String \u03b9;
        
        a(final String \u0131, final String \u0269, final String \u03b9) {
            this.\u0131 = \u0131;
            this.\u0269 = \u0269;
            this.\u03b9 = \u03b9;
        }
    }
    
    public static final class d
    {
        public static final s \u03b9;
        
        static {
            \u03b9 = new s();
        }
    }
}
