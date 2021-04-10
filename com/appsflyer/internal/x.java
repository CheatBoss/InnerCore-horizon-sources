package com.appsflyer.internal;

import java.lang.ref.*;
import java.util.*;
import android.content.*;
import com.appsflyer.internal.model.event.*;
import java.net.*;
import com.appsflyer.*;

public final class x implements Runnable
{
    private static String \u01c3;
    private static String \u03b9 = "https://%ssdk-services.%s/validate-android-signature";
    private WeakReference<Context> \u0131;
    private String \u0196;
    private Map<String, String> \u0237;
    private String \u0269;
    private String \u0279;
    private final Intent \u0399;
    private String \u0406;
    private String \u0456;
    private String \u04c0;
    
    static {
        final StringBuilder sb = new StringBuilder("https://%svalidate.%s/api/v");
        sb.append(AppsFlyerLibCore.\u0131);
        sb.append("/androidevent?buildnumber=5.4.1&app_id=");
        x.\u01c3 = sb.toString();
    }
    
    public x(final Context context, final String \u0269, final String \u0456, final String \u04562, final String \u04cf, final String \u02692, final String \u0279, final Map<String, String> \u0237, final Intent \u03b9) {
        this.\u0131 = new WeakReference<Context>(context);
        this.\u0269 = \u0269;
        this.\u0406 = \u0456;
        this.\u04c0 = \u04cf;
        this.\u0196 = \u02692;
        this.\u0279 = \u0279;
        this.\u0237 = \u0237;
        this.\u0456 = \u04562;
        this.\u0399 = \u03b9;
    }
    
    private static HttpURLConnection \u01c3(final Purchase purchase) {
        final StringBuilder sb = new StringBuilder("Calling ");
        sb.append(purchase.urlString());
        AFLogger.afDebugLog(sb.toString());
        return new ad(purchase.trackingStopped(AppsFlyerLib.getInstance().isTrackingStopped())).\u0269();
    }
    
    private static void \u03b9(final boolean b, String s, final String s2, final String s3, final String s4) {
        if (AppsFlyerLibCore.\u01c3 != null) {
            final StringBuilder sb = new StringBuilder("Validate callback parameters: ");
            sb.append(s);
            sb.append(" ");
            sb.append(s2);
            sb.append(" ");
            sb.append(s3);
            AFLogger.afDebugLog(sb.toString());
            if (b) {
                AFLogger.afDebugLog("Validate in app purchase success: ".concat(String.valueOf(s4)));
                AppsFlyerLibCore.\u01c3.onValidateInApp();
                return;
            }
            AFLogger.afDebugLog("Validate in app purchase failed: ".concat(String.valueOf(s4)));
            final AppsFlyerInAppPurchaseValidatorListener \u01c3 = AppsFlyerLibCore.\u01c3;
            if ((s = s4) == null) {
                s = "Failed validating";
            }
            \u01c3.onValidateInAppFailure(s);
        }
    }
    
    @Override
    public final void run() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: getfield        com/appsflyer/internal/x.\u0269:Ljava/lang/String;
        //     4: astore_2       
        //     5: aload_2        
        //     6: ifnull          518
        //     9: aload_2        
        //    10: invokevirtual   java/lang/String.length:()I
        //    13: ifne            17
        //    16: return         
        //    17: invokestatic    com/appsflyer/AppsFlyerLib.getInstance:()Lcom/appsflyer/AppsFlyerLib;
        //    20: invokevirtual   com/appsflyer/AppsFlyerLib.isTrackingStopped:()Z
        //    23: ifeq            27
        //    26: return         
        //    27: aconst_null    
        //    28: astore_2       
        //    29: aload_0        
        //    30: getfield        com/appsflyer/internal/x.\u0131:Ljava/lang/ref/WeakReference;
        //    33: invokevirtual   java/lang/ref/Reference.get:()Ljava/lang/Object;
        //    36: checkcast       Landroid/content/Context;
        //    39: astore_3       
        //    40: aload_3        
        //    41: ifnonnull       45
        //    44: return         
        //    45: new             Ljava/util/HashMap;
        //    48: dup            
        //    49: invokespecial   java/util/HashMap.<init>:()V
        //    52: astore          4
        //    54: aload           4
        //    56: ldc_w           "public-key"
        //    59: aload_0        
        //    60: getfield        com/appsflyer/internal/x.\u0406:Ljava/lang/String;
        //    63: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //    68: pop            
        //    69: aload           4
        //    71: ldc_w           "sig-data"
        //    74: aload_0        
        //    75: getfield        com/appsflyer/internal/x.\u04c0:Ljava/lang/String;
        //    78: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //    83: pop            
        //    84: aload           4
        //    86: ldc_w           "signature"
        //    89: aload_0        
        //    90: getfield        com/appsflyer/internal/x.\u0456:Ljava/lang/String;
        //    93: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //    98: pop            
        //    99: new             Ljava/lang/Thread;
        //   102: dup            
        //   103: new             Lcom/appsflyer/internal/x$3;
        //   106: dup            
        //   107: aload_0        
        //   108: new             Ljava/util/HashMap;
        //   111: dup            
        //   112: aload           4
        //   114: invokespecial   java/util/HashMap.<init>:(Ljava/util/Map;)V
        //   117: invokespecial   com/appsflyer/internal/x$3.<init>:(Lcom/appsflyer/internal/x;Ljava/util/Map;)V
        //   120: invokespecial   java/lang/Thread.<init>:(Ljava/lang/Runnable;)V
        //   123: invokevirtual   java/lang/Thread.start:()V
        //   126: aload           4
        //   128: ldc_w           "dev_key"
        //   131: aload_0        
        //   132: getfield        com/appsflyer/internal/x.\u0269:Ljava/lang/String;
        //   135: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   140: pop            
        //   141: aload           4
        //   143: ldc_w           "app_id"
        //   146: aload_3        
        //   147: invokevirtual   android/content/Context.getPackageName:()Ljava/lang/String;
        //   150: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   155: pop            
        //   156: aload           4
        //   158: ldc_w           "uid"
        //   161: invokestatic    com/appsflyer/AppsFlyerLib.getInstance:()Lcom/appsflyer/AppsFlyerLib;
        //   164: aload_3        
        //   165: invokevirtual   com/appsflyer/AppsFlyerLib.getAppsFlyerUID:(Landroid/content/Context;)Ljava/lang/String;
        //   168: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   173: pop            
        //   174: aload           4
        //   176: ldc_w           "advertiserId"
        //   179: invokestatic    com/appsflyer/AppsFlyerProperties.getInstance:()Lcom/appsflyer/AppsFlyerProperties;
        //   182: ldc_w           "advertiserId"
        //   185: invokevirtual   com/appsflyer/AppsFlyerProperties.getString:(Ljava/lang/String;)Ljava/lang/String;
        //   188: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   193: pop            
        //   194: new             Lorg/json/JSONObject;
        //   197: dup            
        //   198: aload           4
        //   200: invokespecial   org/json/JSONObject.<init>:(Ljava/util/Map;)V
        //   203: invokevirtual   org/json/JSONObject.toString:()Ljava/lang/String;
        //   206: astore          5
        //   208: getstatic       com/appsflyer/internal/x.\u03b9:Ljava/lang/String;
        //   211: invokestatic    com/appsflyer/ServerConfigHandler.getUrl:(Ljava/lang/String;)Ljava/lang/String;
        //   214: astore_3       
        //   215: getstatic       com/appsflyer/internal/ai.\u0269:Lcom/appsflyer/internal/ai;
        //   218: ifnonnull       231
        //   221: new             Lcom/appsflyer/internal/ai;
        //   224: dup            
        //   225: invokespecial   com/appsflyer/internal/ai.<init>:()V
        //   228: putstatic       com/appsflyer/internal/ai.\u0269:Lcom/appsflyer/internal/ai;
        //   231: getstatic       com/appsflyer/internal/ai.\u0269:Lcom/appsflyer/internal/ai;
        //   234: ldc             "server_request"
        //   236: aload_3        
        //   237: iconst_1       
        //   238: anewarray       Ljava/lang/String;
        //   241: dup            
        //   242: iconst_0       
        //   243: aload           5
        //   245: aastore        
        //   246: invokevirtual   com/appsflyer/internal/ai.\u0131:(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;)V
        //   249: new             Lcom/appsflyer/internal/model/event/SdkServices;
        //   252: dup            
        //   253: invokespecial   com/appsflyer/internal/model/event/SdkServices.<init>:()V
        //   256: aload           4
        //   258: invokevirtual   com/appsflyer/internal/model/event/SdkServices.params:(Ljava/util/Map;)Lcom/appsflyer/AFEvent;
        //   261: aload_3        
        //   262: invokevirtual   com/appsflyer/AFEvent.urlString:(Ljava/lang/String;)Lcom/appsflyer/AFEvent;
        //   265: checkcast       Lcom/appsflyer/internal/model/event/Purchase;
        //   268: invokestatic    com/appsflyer/internal/x.\u01c3:(Lcom/appsflyer/internal/model/event/Purchase;)Ljava/net/HttpURLConnection;
        //   271: astore          4
        //   273: iconst_m1      
        //   274: istore_1       
        //   275: aload           4
        //   277: ifnull          289
        //   280: aload           4
        //   282: invokevirtual   java/net/HttpURLConnection.getResponseCode:()I
        //   285: istore_1       
        //   286: goto            289
        //   289: invokestatic    com/appsflyer/AppsFlyerLibCore.getInstance:()Lcom/appsflyer/AppsFlyerLibCore;
        //   292: aload           4
        //   294: invokevirtual   com/appsflyer/AppsFlyerLibCore.\u01c3:(Ljava/net/HttpURLConnection;)Ljava/lang/String;
        //   297: astore_2       
        //   298: getstatic       com/appsflyer/internal/ai.\u0269:Lcom/appsflyer/internal/ai;
        //   301: ifnonnull       314
        //   304: new             Lcom/appsflyer/internal/ai;
        //   307: dup            
        //   308: invokespecial   com/appsflyer/internal/ai.<init>:()V
        //   311: putstatic       com/appsflyer/internal/ai.\u0269:Lcom/appsflyer/internal/ai;
        //   314: getstatic       com/appsflyer/internal/ai.\u0269:Lcom/appsflyer/internal/ai;
        //   317: ldc             "server_response"
        //   319: aload_3        
        //   320: iconst_2       
        //   321: anewarray       Ljava/lang/String;
        //   324: dup            
        //   325: iconst_0       
        //   326: iload_1        
        //   327: invokestatic    java/lang/String.valueOf:(I)Ljava/lang/String;
        //   330: aastore        
        //   331: dup            
        //   332: iconst_1       
        //   333: aload_2        
        //   334: aastore        
        //   335: invokevirtual   com/appsflyer/internal/ai.\u0131:(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;)V
        //   338: new             Lorg/json/JSONObject;
        //   341: dup            
        //   342: aload_2        
        //   343: invokespecial   org/json/JSONObject.<init>:(Ljava/lang/String;)V
        //   346: astore_2       
        //   347: aload_2        
        //   348: ldc_w           "code"
        //   351: iload_1        
        //   352: invokevirtual   org/json/JSONObject.put:(Ljava/lang/String;I)Lorg/json/JSONObject;
        //   355: pop            
        //   356: iload_1        
        //   357: sipush          200
        //   360: if_icmpne       419
        //   363: new             Ljava/lang/StringBuilder;
        //   366: dup            
        //   367: ldc_w           "Validate response 200 ok: "
        //   370: invokespecial   java/lang/StringBuilder.<init>:(Ljava/lang/String;)V
        //   373: astore_3       
        //   374: aload_3        
        //   375: aload_2        
        //   376: invokevirtual   org/json/JSONObject.toString:()Ljava/lang/String;
        //   379: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   382: pop            
        //   383: aload_3        
        //   384: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
        //   387: invokestatic    com/appsflyer/AFLogger.afInfoLog:(Ljava/lang/String;)V
        //   390: aload_2        
        //   391: ldc_w           "result"
        //   394: invokevirtual   org/json/JSONObject.optBoolean:(Ljava/lang/String;)Z
        //   397: aload_0        
        //   398: getfield        com/appsflyer/internal/x.\u04c0:Ljava/lang/String;
        //   401: aload_0        
        //   402: getfield        com/appsflyer/internal/x.\u0196:Ljava/lang/String;
        //   405: aload_0        
        //   406: getfield        com/appsflyer/internal/x.\u0279:Ljava/lang/String;
        //   409: aload_2        
        //   410: invokevirtual   org/json/JSONObject.toString:()Ljava/lang/String;
        //   413: invokestatic    com/appsflyer/internal/x.\u03b9:(ZLjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V
        //   416: goto            445
        //   419: ldc_w           "Failed Validate request"
        //   422: invokestatic    com/appsflyer/AFLogger.afInfoLog:(Ljava/lang/String;)V
        //   425: iconst_0       
        //   426: aload_0        
        //   427: getfield        com/appsflyer/internal/x.\u04c0:Ljava/lang/String;
        //   430: aload_0        
        //   431: getfield        com/appsflyer/internal/x.\u0196:Ljava/lang/String;
        //   434: aload_0        
        //   435: getfield        com/appsflyer/internal/x.\u0279:Ljava/lang/String;
        //   438: aload_2        
        //   439: invokevirtual   org/json/JSONObject.toString:()Ljava/lang/String;
        //   442: invokestatic    com/appsflyer/internal/x.\u03b9:(ZLjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V
        //   445: aload           4
        //   447: ifnull          506
        //   450: aload           4
        //   452: invokevirtual   java/net/HttpURLConnection.disconnect:()V
        //   455: return         
        //   456: astore_3       
        //   457: getstatic       com/appsflyer/AppsFlyerLibCore.\u01c3:Lcom/appsflyer/AppsFlyerInAppPurchaseValidatorListener;
        //   460: ifnull          490
        //   463: ldc_w           "Failed Validate request + ex"
        //   466: aload_3        
        //   467: invokestatic    com/appsflyer/AFLogger.afErrorLog:(Ljava/lang/String;Ljava/lang/Throwable;)V
        //   470: iconst_0       
        //   471: aload_0        
        //   472: getfield        com/appsflyer/internal/x.\u04c0:Ljava/lang/String;
        //   475: aload_0        
        //   476: getfield        com/appsflyer/internal/x.\u0196:Ljava/lang/String;
        //   479: aload_0        
        //   480: getfield        com/appsflyer/internal/x.\u0279:Ljava/lang/String;
        //   483: aload_3        
        //   484: invokevirtual   java/lang/Throwable.getMessage:()Ljava/lang/String;
        //   487: invokestatic    com/appsflyer/internal/x.\u03b9:(ZLjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V
        //   490: aload_3        
        //   491: invokevirtual   java/lang/Throwable.getMessage:()Ljava/lang/String;
        //   494: aload_3        
        //   495: invokestatic    com/appsflyer/AFLogger.afErrorLog:(Ljava/lang/String;Ljava/lang/Throwable;)V
        //   498: aload_2        
        //   499: ifnull          506
        //   502: aload_2        
        //   503: invokevirtual   java/net/HttpURLConnection.disconnect:()V
        //   506: return         
        //   507: astore_3       
        //   508: aload_2        
        //   509: ifnull          516
        //   512: aload_2        
        //   513: invokevirtual   java/net/HttpURLConnection.disconnect:()V
        //   516: aload_3        
        //   517: athrow         
        //   518: return         
        //   519: astore_3       
        //   520: aload           4
        //   522: astore_2       
        //   523: goto            457
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type
        //  -----  -----  -----  -----  ----
        //  29     40     456    457    Any
        //  45     231    456    457    Any
        //  231    273    456    457    Any
        //  280    286    519    526    Any
        //  289    314    519    526    Any
        //  314    356    519    526    Any
        //  363    416    519    526    Any
        //  419    445    519    526    Any
        //  457    490    507    518    Any
        //  490    498    507    518    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0289:
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
