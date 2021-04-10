package com.appsflyer.internal.referrer;

import com.appsflyer.internal.*;
import java.util.*;
import android.content.*;

public class HuaweiReferrer extends ContentFetcher<Map<String, Object>>
{
    public Map<String, Object> map;
    private Runnable \u0399;
    
    public HuaweiReferrer(final Runnable \u03b9, final Context context) {
        super(context, "com.huawei.appmarket.commondata", "ffe391e0ea186d0734ed601e4e70e3224b7309d48e2075bac46d8c667eae7212", 0L);
        this.\u0399 = \u03b9;
    }
    
    public Map<String, Object> query() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: dup            
        //     4: new             Lcom/appsflyer/internal/referrer/MandatoryFields;
        //     7: dup            
        //     8: invokespecial   com/appsflyer/internal/referrer/MandatoryFields.<init>:()V
        //    11: invokespecial   java/util/HashMap.<init>:(Ljava/util/Map;)V
        //    14: astore          4
        //    16: aload           4
        //    18: ldc             "source"
        //    20: ldc             "huawei"
        //    22: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //    27: pop            
        //    28: aload_0        
        //    29: getfield        com/appsflyer/internal/ContentFetcher.context:Landroid/content/Context;
        //    32: invokevirtual   android/content/Context.getPackageManager:()Landroid/content/pm/PackageManager;
        //    35: aload_0        
        //    36: getfield        com/appsflyer/internal/ContentFetcher.authority:Ljava/lang/String;
        //    39: sipush          128
        //    42: invokevirtual   android/content/pm/PackageManager.resolveContentProvider:(Ljava/lang/String;I)Landroid/content/pm/ProviderInfo;
        //    45: getfield        android/content/pm/PackageItemInfo.packageName:Ljava/lang/String;
        //    48: astore_1       
        //    49: aload           4
        //    51: ldc             "api_ver"
        //    53: aload_0        
        //    54: getfield        com/appsflyer/internal/ContentFetcher.context:Landroid/content/Context;
        //    57: aload_1        
        //    58: invokestatic    com/appsflyer/AndroidUtils.getVersionCode:(Landroid/content/Context;Ljava/lang/String;)J
        //    61: invokestatic    java/lang/Long.valueOf:(J)Ljava/lang/Long;
        //    64: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //    69: pop            
        //    70: aload           4
        //    72: ldc             "api_ver_name"
        //    74: aload_0        
        //    75: getfield        com/appsflyer/internal/ContentFetcher.context:Landroid/content/Context;
        //    78: aload_1        
        //    79: invokestatic    com/appsflyer/AndroidUtils.getVersionName:(Landroid/content/Context;Ljava/lang/String;)Ljava/lang/String;
        //    82: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //    87: pop            
        //    88: aload_0        
        //    89: getfield        com/appsflyer/internal/ContentFetcher.context:Landroid/content/Context;
        //    92: invokevirtual   android/content/Context.getContentResolver:()Landroid/content/ContentResolver;
        //    95: astore_1       
        //    96: new             Ljava/lang/StringBuilder;
        //    99: dup            
        //   100: ldc             "content://"
        //   102: invokespecial   java/lang/StringBuilder.<init>:(Ljava/lang/String;)V
        //   105: astore_2       
        //   106: aload_2        
        //   107: aload_0        
        //   108: getfield        com/appsflyer/internal/ContentFetcher.authority:Ljava/lang/String;
        //   111: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   114: pop            
        //   115: aload_2        
        //   116: ldc             "/item/5"
        //   118: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   121: pop            
        //   122: aload_1        
        //   123: aload_2        
        //   124: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
        //   127: invokestatic    android/net/Uri.parse:(Ljava/lang/String;)Landroid/net/Uri;
        //   130: aconst_null    
        //   131: aconst_null    
        //   132: iconst_1       
        //   133: anewarray       Ljava/lang/String;
        //   136: dup            
        //   137: iconst_0       
        //   138: aload_0        
        //   139: getfield        com/appsflyer/internal/ContentFetcher.context:Landroid/content/Context;
        //   142: invokevirtual   android/content/Context.getPackageName:()Ljava/lang/String;
        //   145: aastore        
        //   146: aconst_null    
        //   147: invokevirtual   android/content/ContentResolver.query:(Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
        //   150: astore_1       
        //   151: aload_1        
        //   152: ifnull          351
        //   155: aload_1        
        //   156: astore_2       
        //   157: aload_1        
        //   158: invokeinterface android/database/Cursor.moveToFirst:()Z
        //   163: ifeq            435
        //   166: aload_1        
        //   167: astore_2       
        //   168: aload           4
        //   170: ldc             "response"
        //   172: ldc             "OK"
        //   174: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   179: pop            
        //   180: aload_1        
        //   181: astore_2       
        //   182: aload           4
        //   184: ldc             "referrer"
        //   186: aload_1        
        //   187: iconst_0       
        //   188: invokeinterface android/database/Cursor.getString:(I)Ljava/lang/String;
        //   193: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   198: pop            
        //   199: aload_1        
        //   200: astore_2       
        //   201: aload           4
        //   203: ldc             "click_ts"
        //   205: aload_1        
        //   206: iconst_1       
        //   207: invokeinterface android/database/Cursor.getLong:(I)J
        //   212: invokestatic    java/lang/Long.valueOf:(J)Ljava/lang/Long;
        //   215: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   220: pop            
        //   221: aload_1        
        //   222: astore_2       
        //   223: aload           4
        //   225: ldc             "install_end_ts"
        //   227: aload_1        
        //   228: iconst_2       
        //   229: invokeinterface android/database/Cursor.getLong:(I)J
        //   234: invokestatic    java/lang/Long.valueOf:(J)Ljava/lang/Long;
        //   237: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   242: pop            
        //   243: aload_1        
        //   244: astore_2       
        //   245: aload_1        
        //   246: invokeinterface android/database/Cursor.getColumnCount:()I
        //   251: iconst_3       
        //   252: if_icmple       357
        //   255: aload_1        
        //   256: astore_2       
        //   257: aload           4
        //   259: ldc             "install_begin_ts"
        //   261: aload_1        
        //   262: iconst_3       
        //   263: invokeinterface android/database/Cursor.getLong:(I)J
        //   268: invokestatic    java/lang/Long.valueOf:(J)Ljava/lang/Long;
        //   271: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   276: pop            
        //   277: aload_1        
        //   278: astore_2       
        //   279: new             Ljava/util/HashMap;
        //   282: dup            
        //   283: invokespecial   java/util/HashMap.<init>:()V
        //   286: astore_3       
        //   287: aload_1        
        //   288: astore_2       
        //   289: aload_3        
        //   290: ldc             "track_id"
        //   292: aload_1        
        //   293: iconst_4       
        //   294: invokeinterface android/database/Cursor.getString:(I)Ljava/lang/String;
        //   299: invokevirtual   java/util/AbstractMap.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   302: pop            
        //   303: aload_1        
        //   304: astore_2       
        //   305: aload_3        
        //   306: ldc             "referrer_ex"
        //   308: aload_1        
        //   309: iconst_5       
        //   310: invokeinterface android/database/Cursor.getString:(I)Ljava/lang/String;
        //   315: invokevirtual   java/util/AbstractMap.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   318: pop            
        //   319: aload_1        
        //   320: astore_2       
        //   321: aload           4
        //   323: ldc             "huawei_custom"
        //   325: aload_3        
        //   326: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   331: pop            
        //   332: goto            357
        //   335: aload_1        
        //   336: astore_2       
        //   337: aload           4
        //   339: ldc             "response"
        //   341: aload_3        
        //   342: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   347: pop            
        //   348: goto            357
        //   351: ldc             "SERVICE_UNAVAILABLE"
        //   353: astore_3       
        //   354: goto            335
        //   357: aload_1        
        //   358: ifnull          404
        //   361: goto            398
        //   364: astore_1       
        //   365: aconst_null    
        //   366: astore_2       
        //   367: goto            423
        //   370: astore_3       
        //   371: aconst_null    
        //   372: astore_1       
        //   373: aload_1        
        //   374: astore_2       
        //   375: aload           4
        //   377: ldc             "response"
        //   379: ldc             "FEATURE_NOT_SUPPORTED"
        //   381: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   386: pop            
        //   387: aload_1        
        //   388: astore_2       
        //   389: aload_0        
        //   390: aload_3        
        //   391: invokevirtual   com/appsflyer/internal/referrer/HuaweiReferrer.onError:(Ljava/lang/Exception;)V
        //   394: aload_1        
        //   395: ifnull          404
        //   398: aload_1        
        //   399: invokeinterface android/database/Cursor.close:()V
        //   404: aload_0        
        //   405: aload           4
        //   407: putfield        com/appsflyer/internal/referrer/HuaweiReferrer.map:Ljava/util/Map;
        //   410: aload_0        
        //   411: getfield        com/appsflyer/internal/referrer/HuaweiReferrer.\u0399:Ljava/lang/Runnable;
        //   414: invokeinterface java/lang/Runnable.run:()V
        //   419: aload           4
        //   421: areturn        
        //   422: astore_1       
        //   423: aload_2        
        //   424: ifnull          433
        //   427: aload_2        
        //   428: invokeinterface android/database/Cursor.close:()V
        //   433: aload_1        
        //   434: athrow         
        //   435: ldc             "FEATURE_NOT_SUPPORTED"
        //   437: astore_3       
        //   438: goto            335
        //   441: astore_3       
        //   442: goto            373
        //    Signature:
        //  ()Ljava/util/Map<Ljava/lang/String;Ljava/lang/Object;>;
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  88     151    370    373    Ljava/lang/Exception;
        //  88     151    364    370    Any
        //  157    166    441    445    Ljava/lang/Exception;
        //  157    166    422    423    Any
        //  168    180    441    445    Ljava/lang/Exception;
        //  168    180    422    423    Any
        //  182    199    441    445    Ljava/lang/Exception;
        //  182    199    422    423    Any
        //  201    221    441    445    Ljava/lang/Exception;
        //  201    221    422    423    Any
        //  223    243    441    445    Ljava/lang/Exception;
        //  223    243    422    423    Any
        //  245    255    441    445    Ljava/lang/Exception;
        //  245    255    422    423    Any
        //  257    277    441    445    Ljava/lang/Exception;
        //  257    277    422    423    Any
        //  279    287    441    445    Ljava/lang/Exception;
        //  279    287    422    423    Any
        //  289    303    441    445    Ljava/lang/Exception;
        //  289    303    422    423    Any
        //  305    319    441    445    Ljava/lang/Exception;
        //  305    319    422    423    Any
        //  321    332    441    445    Ljava/lang/Exception;
        //  321    332    422    423    Any
        //  337    348    441    445    Ljava/lang/Exception;
        //  337    348    422    423    Any
        //  375    387    422    423    Any
        //  389    394    422    423    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0335:
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
}
