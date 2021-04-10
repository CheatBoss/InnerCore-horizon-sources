package com.appsflyer;

import javax.net.ssl.*;
import org.json.*;
import java.io.*;
import java.net.*;

public abstract class OneLinkHttpTask implements Runnable
{
    static final String BASE_URL = "https://%sonelink.%s/shortlink-sdk/v1";
    static final String NO_CONNECTION_ERROR_MSG = "Can't get one link data";
    private static final int WAIT_TIMEOUT = 3000;
    private AppsFlyerLibCore afLib;
    private HttpsUrlConnectionProvider connectionProvider;
    public String oneLinkId;
    
    public OneLinkHttpTask(final AppsFlyerLibCore afLib) {
        this.afLib = afLib;
    }
    
    private void doRequest() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     2: astore          4
        //     4: invokestatic    java/lang/System.currentTimeMillis:()J
        //     7: ldc2_w          1000
        //    10: ldiv           
        //    11: lstore_2       
        //    12: aload_0        
        //    13: invokevirtual   com/appsflyer/OneLinkHttpTask.getOneLinkUrl:()Ljava/lang/String;
        //    16: astore          7
        //    18: new             Ljava/lang/StringBuilder;
        //    21: dup            
        //    22: invokespecial   java/lang/StringBuilder.<init>:()V
        //    25: astore          5
        //    27: aload           5
        //    29: ldc             "oneLinkUrl: "
        //    31: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    34: pop            
        //    35: aload           5
        //    37: aload           7
        //    39: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    42: pop            
        //    43: aload           5
        //    45: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //    48: invokestatic    com/appsflyer/AFLogger.afRDLog:(Ljava/lang/String;)V
        //    51: aload_0        
        //    52: getfield        com/appsflyer/OneLinkHttpTask.connectionProvider:Lcom/appsflyer/OneLinkHttpTask$HttpsUrlConnectionProvider;
        //    55: aload           7
        //    57: invokevirtual   com/appsflyer/OneLinkHttpTask$HttpsUrlConnectionProvider.\u01c3:(Ljava/lang/String;)Ljavax/net/ssl/HttpsURLConnection;
        //    60: astore          5
        //    62: aload           5
        //    64: ldc             "content-type"
        //    66: ldc             "application/json"
        //    68: invokevirtual   javax/net/ssl/HttpsURLConnection.addRequestProperty:(Ljava/lang/String;Ljava/lang/String;)V
        //    71: aload           5
        //    73: ldc             "authorization"
        //    75: lload_2        
        //    76: invokestatic    com/appsflyer/internal/z.\u03b9:(J)Ljava/lang/String;
        //    79: invokevirtual   javax/net/ssl/HttpsURLConnection.addRequestProperty:(Ljava/lang/String;Ljava/lang/String;)V
        //    82: aload           5
        //    84: ldc             "af-timestamp"
        //    86: lload_2        
        //    87: invokestatic    java/lang/String.valueOf:(J)Ljava/lang/String;
        //    90: invokevirtual   javax/net/ssl/HttpsURLConnection.addRequestProperty:(Ljava/lang/String;Ljava/lang/String;)V
        //    93: aload           5
        //    95: sipush          3000
        //    98: invokevirtual   javax/net/ssl/HttpsURLConnection.setReadTimeout:(I)V
        //   101: aload           5
        //   103: sipush          3000
        //   106: invokevirtual   javax/net/ssl/HttpsURLConnection.setConnectTimeout:(I)V
        //   109: aload_0        
        //   110: aload           5
        //   112: invokevirtual   com/appsflyer/OneLinkHttpTask.initRequest:(Ljavax/net/ssl/HttpsURLConnection;)V
        //   115: aload           5
        //   117: invokevirtual   javax/net/ssl/HttpsURLConnection.getResponseCode:()I
        //   120: istore_1       
        //   121: aload_0        
        //   122: getfield        com/appsflyer/OneLinkHttpTask.afLib:Lcom/appsflyer/AppsFlyerLibCore;
        //   125: aload           5
        //   127: invokevirtual   com/appsflyer/AppsFlyerLibCore.\u01c3:(Ljava/net/HttpURLConnection;)Ljava/lang/String;
        //   130: astore          5
        //   132: iload_1        
        //   133: sipush          200
        //   136: if_icmpne       147
        //   139: ldc             "Status 200 ok"
        //   141: invokestatic    com/appsflyer/AFLogger.afInfoLog:(Ljava/lang/String;)V
        //   144: goto            297
        //   147: new             Ljava/lang/StringBuilder;
        //   150: dup            
        //   151: invokespecial   java/lang/StringBuilder.<init>:()V
        //   154: astore          4
        //   156: aload           4
        //   158: ldc             "Response code = "
        //   160: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   163: pop            
        //   164: aload           4
        //   166: iload_1        
        //   167: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //   170: pop            
        //   171: aload           4
        //   173: ldc             " content = "
        //   175: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   178: pop            
        //   179: aload           4
        //   181: aload           5
        //   183: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   186: pop            
        //   187: aload           4
        //   189: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   192: astore          4
        //   194: goto            297
        //   197: astore          6
        //   199: ldc             ""
        //   201: astore          4
        //   203: new             Ljava/lang/StringBuilder;
        //   206: dup            
        //   207: invokespecial   java/lang/StringBuilder.<init>:()V
        //   210: astore          5
        //   212: aload           5
        //   214: ldc             "Error while calling "
        //   216: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   219: pop            
        //   220: aload           5
        //   222: aload           7
        //   224: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   227: pop            
        //   228: aload           5
        //   230: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   233: aload           6
        //   235: invokestatic    com/appsflyer/AFLogger.afErrorLog:(Ljava/lang/String;Ljava/lang/Throwable;)V
        //   238: new             Ljava/lang/StringBuilder;
        //   241: dup            
        //   242: invokespecial   java/lang/StringBuilder.<init>:()V
        //   245: astore          5
        //   247: aload           5
        //   249: ldc             "Error while calling "
        //   251: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   254: pop            
        //   255: aload           5
        //   257: aload           7
        //   259: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   262: pop            
        //   263: aload           5
        //   265: ldc             " stacktrace: "
        //   267: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   270: pop            
        //   271: aload           5
        //   273: aload           6
        //   275: invokevirtual   java/lang/Throwable.toString:()Ljava/lang/String;
        //   278: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   281: pop            
        //   282: aload           5
        //   284: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   287: astore          6
        //   289: aload           4
        //   291: astore          5
        //   293: aload           6
        //   295: astore          4
        //   297: aload           4
        //   299: invokestatic    android/text/TextUtils.isEmpty:(Ljava/lang/CharSequence;)Z
        //   302: ifeq            345
        //   305: new             Ljava/lang/StringBuilder;
        //   308: dup            
        //   309: invokespecial   java/lang/StringBuilder.<init>:()V
        //   312: astore          4
        //   314: aload           4
        //   316: ldc             "Connection call succeeded: "
        //   318: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   321: pop            
        //   322: aload           4
        //   324: aload           5
        //   326: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   329: pop            
        //   330: aload           4
        //   332: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   335: invokestatic    com/appsflyer/AFLogger.afInfoLog:(Ljava/lang/String;)V
        //   338: aload_0        
        //   339: aload           5
        //   341: invokevirtual   com/appsflyer/OneLinkHttpTask.handleResponse:(Ljava/lang/String;)V
        //   344: return         
        //   345: new             Ljava/lang/StringBuilder;
        //   348: dup            
        //   349: invokespecial   java/lang/StringBuilder.<init>:()V
        //   352: astore          5
        //   354: aload           5
        //   356: ldc             "Connection error: "
        //   358: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   361: pop            
        //   362: aload           5
        //   364: aload           4
        //   366: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   369: pop            
        //   370: aload           5
        //   372: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   375: invokestatic    com/appsflyer/AFLogger.afWarnLog:(Ljava/lang/String;)V
        //   378: aload_0        
        //   379: invokevirtual   com/appsflyer/OneLinkHttpTask.onErrorResponse:()V
        //   382: return         
        //   383: astore          6
        //   385: aload           5
        //   387: astore          4
        //   389: goto            203
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type
        //  -----  -----  -----  -----  ----
        //  51     132    197    203    Any
        //  139    144    383    392    Any
        //  147    194    383    392    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0147:
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
    
    protected abstract String getOneLinkUrl();
    
    protected abstract void handleResponse(final String p0);
    
    protected abstract void initRequest(final HttpsURLConnection p0) throws JSONException, IOException;
    
    protected abstract void onErrorResponse();
    
    @Override
    public void run() {
        this.doRequest();
    }
    
    public void setConnProvider(final HttpsUrlConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }
    
    public static class HttpsUrlConnectionProvider
    {
        final HttpsURLConnection \u01c3(final String s) throws IOException {
            return (HttpsURLConnection)new URL(s).openConnection();
        }
    }
}
