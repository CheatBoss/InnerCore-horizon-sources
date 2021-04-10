package okhttp3;

import okhttp3.internal.platform.*;
import java.io.*;
import okhttp3.internal.cache.*;
import java.util.*;
import okhttp3.internal.http.*;
import okhttp3.internal.connection.*;
import okhttp3.internal.*;

final class RealCall implements Call
{
    final OkHttpClient client;
    private EventListener eventListener;
    private boolean executed;
    final boolean forWebSocket;
    final Request originalRequest;
    final RetryAndFollowUpInterceptor retryAndFollowUpInterceptor;
    
    private RealCall(final OkHttpClient client, final Request originalRequest, final boolean forWebSocket) {
        this.client = client;
        this.originalRequest = originalRequest;
        this.forWebSocket = forWebSocket;
        this.retryAndFollowUpInterceptor = new RetryAndFollowUpInterceptor(client, forWebSocket);
    }
    
    private void captureCallStackTrace() {
        this.retryAndFollowUpInterceptor.setCallStackTrace(Platform.get().getStackTraceForCloseable("response.body().close()"));
    }
    
    static RealCall newRealCall(final OkHttpClient okHttpClient, final Request request, final boolean b) {
        final RealCall realCall = new RealCall(okHttpClient, request, b);
        realCall.eventListener = okHttpClient.eventListenerFactory().create(realCall);
        return realCall;
    }
    
    @Override
    public void cancel() {
        this.retryAndFollowUpInterceptor.cancel();
    }
    
    @Override
    public RealCall clone() {
        return newRealCall(this.client, this.originalRequest, this.forWebSocket);
    }
    
    @Override
    public void enqueue(final Callback callback) {
        synchronized (this) {
            if (!this.executed) {
                this.executed = true;
                // monitorexit(this)
                this.captureCallStackTrace();
                this.eventListener.callStart(this);
                this.client.dispatcher().enqueue(new AsyncCall(callback));
                return;
            }
            throw new IllegalStateException("Already Executed");
        }
    }
    
    @Override
    public Response execute() throws IOException {
        synchronized (this) {
            if (this.executed) {
                throw new IllegalStateException("Already Executed");
            }
            this.executed = true;
            // monitorexit(this)
            this.captureCallStackTrace();
            this.eventListener.callStart(this);
            try {
                this.client.dispatcher().executed(this);
                final Response responseWithInterceptorChain = this.getResponseWithInterceptorChain();
                if (responseWithInterceptorChain != null) {
                    this.client.dispatcher().finished(this);
                    return responseWithInterceptorChain;
                }
                throw new IOException("Canceled");
            }
            catch (IOException ex) {
                this.eventListener.callFailed(this, ex);
                throw ex;
            }
            this.client.dispatcher().finished(this);
        }
    }
    
    Response getResponseWithInterceptorChain() throws IOException {
        final ArrayList<Object> list = new ArrayList<Object>();
        list.addAll(this.client.interceptors());
        list.add(this.retryAndFollowUpInterceptor);
        list.add(new BridgeInterceptor(this.client.cookieJar()));
        list.add(new CacheInterceptor(this.client.internalCache()));
        list.add(new ConnectInterceptor(this.client));
        if (!this.forWebSocket) {
            list.addAll(this.client.networkInterceptors());
        }
        list.add(new CallServerInterceptor(this.forWebSocket));
        return ((Interceptor.Chain)new RealInterceptorChain((List<Interceptor>)list, null, null, null, 0, this.originalRequest, this, this.eventListener, this.client.connectTimeoutMillis(), this.client.readTimeoutMillis(), this.client.writeTimeoutMillis())).proceed(this.originalRequest);
    }
    
    @Override
    public boolean isCanceled() {
        return this.retryAndFollowUpInterceptor.isCanceled();
    }
    
    @Override
    public boolean isExecuted() {
        synchronized (this) {
            return this.executed;
        }
    }
    
    String redactedUrl() {
        return this.originalRequest.url().redact();
    }
    
    @Override
    public Request request() {
        return this.originalRequest;
    }
    
    StreamAllocation streamAllocation() {
        return this.retryAndFollowUpInterceptor.streamAllocation();
    }
    
    String toLoggableString() {
        final StringBuilder sb = new StringBuilder();
        String s;
        if (this.isCanceled()) {
            s = "canceled ";
        }
        else {
            s = "";
        }
        sb.append(s);
        String s2;
        if (this.forWebSocket) {
            s2 = "web socket";
        }
        else {
            s2 = "call";
        }
        sb.append(s2);
        sb.append(" to ");
        sb.append(this.redactedUrl());
        return sb.toString();
    }
    
    final class AsyncCall extends NamedRunnable
    {
        private final Callback responseCallback;
        
        AsyncCall(final Callback responseCallback) {
            super("OkHttp %s", new Object[] { RealCall.this.redactedUrl() });
            this.responseCallback = responseCallback;
        }
        
        @Override
        protected void execute() {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     1: getfield        okhttp3/RealCall$AsyncCall.this$0:Lokhttp3/RealCall;
            //     4: invokevirtual   okhttp3/RealCall.getResponseWithInterceptorChain:()Lokhttp3/Response;
            //     7: astore_3       
            //     8: aload_0        
            //     9: getfield        okhttp3/RealCall$AsyncCall.this$0:Lokhttp3/RealCall;
            //    12: getfield        okhttp3/RealCall.retryAndFollowUpInterceptor:Lokhttp3/internal/http/RetryAndFollowUpInterceptor;
            //    15: invokevirtual   okhttp3/internal/http/RetryAndFollowUpInterceptor.isCanceled:()Z
            //    18: istore_2       
            //    19: iload_2        
            //    20: ifeq            48
            //    23: aload_0        
            //    24: getfield        okhttp3/RealCall$AsyncCall.responseCallback:Lokhttp3/Callback;
            //    27: aload_0        
            //    28: getfield        okhttp3/RealCall$AsyncCall.this$0:Lokhttp3/RealCall;
            //    31: new             Ljava/io/IOException;
            //    34: dup            
            //    35: ldc             "Canceled"
            //    37: invokespecial   java/io/IOException.<init>:(Ljava/lang/String;)V
            //    40: invokeinterface okhttp3/Callback.onFailure:(Lokhttp3/Call;Ljava/io/IOException;)V
            //    45: goto            160
            //    48: aload_0        
            //    49: getfield        okhttp3/RealCall$AsyncCall.responseCallback:Lokhttp3/Callback;
            //    52: aload_0        
            //    53: getfield        okhttp3/RealCall$AsyncCall.this$0:Lokhttp3/RealCall;
            //    56: aload_3        
            //    57: invokeinterface okhttp3/Callback.onResponse:(Lokhttp3/Call;Lokhttp3/Response;)V
            //    62: goto            160
            //    65: iconst_1       
            //    66: istore_1       
            //    67: goto            77
            //    70: astore_3       
            //    71: goto            175
            //    74: astore_3       
            //    75: iconst_0       
            //    76: istore_1       
            //    77: iload_1        
            //    78: ifeq            131
            //    81: invokestatic    okhttp3/internal/platform/Platform.get:()Lokhttp3/internal/platform/Platform;
            //    84: astore          4
            //    86: new             Ljava/lang/StringBuilder;
            //    89: dup            
            //    90: invokespecial   java/lang/StringBuilder.<init>:()V
            //    93: astore          5
            //    95: aload           5
            //    97: ldc             "Callback failure for "
            //    99: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   102: pop            
            //   103: aload           5
            //   105: aload_0        
            //   106: getfield        okhttp3/RealCall$AsyncCall.this$0:Lokhttp3/RealCall;
            //   109: invokevirtual   okhttp3/RealCall.toLoggableString:()Ljava/lang/String;
            //   112: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   115: pop            
            //   116: aload           4
            //   118: iconst_4       
            //   119: aload           5
            //   121: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
            //   124: aload_3        
            //   125: invokevirtual   okhttp3/internal/platform/Platform.log:(ILjava/lang/String;Ljava/lang/Throwable;)V
            //   128: goto            160
            //   131: aload_0        
            //   132: getfield        okhttp3/RealCall$AsyncCall.this$0:Lokhttp3/RealCall;
            //   135: invokestatic    okhttp3/RealCall.access$000:(Lokhttp3/RealCall;)Lokhttp3/EventListener;
            //   138: aload_0        
            //   139: getfield        okhttp3/RealCall$AsyncCall.this$0:Lokhttp3/RealCall;
            //   142: aload_3        
            //   143: invokevirtual   okhttp3/EventListener.callFailed:(Lokhttp3/Call;Ljava/io/IOException;)V
            //   146: aload_0        
            //   147: getfield        okhttp3/RealCall$AsyncCall.responseCallback:Lokhttp3/Callback;
            //   150: aload_0        
            //   151: getfield        okhttp3/RealCall$AsyncCall.this$0:Lokhttp3/RealCall;
            //   154: aload_3        
            //   155: invokeinterface okhttp3/Callback.onFailure:(Lokhttp3/Call;Ljava/io/IOException;)V
            //   160: aload_0        
            //   161: getfield        okhttp3/RealCall$AsyncCall.this$0:Lokhttp3/RealCall;
            //   164: getfield        okhttp3/RealCall.client:Lokhttp3/OkHttpClient;
            //   167: invokevirtual   okhttp3/OkHttpClient.dispatcher:()Lokhttp3/Dispatcher;
            //   170: aload_0        
            //   171: invokevirtual   okhttp3/Dispatcher.finished:(Lokhttp3/RealCall$AsyncCall;)V
            //   174: return         
            //   175: aload_0        
            //   176: getfield        okhttp3/RealCall$AsyncCall.this$0:Lokhttp3/RealCall;
            //   179: getfield        okhttp3/RealCall.client:Lokhttp3/OkHttpClient;
            //   182: invokevirtual   okhttp3/OkHttpClient.dispatcher:()Lokhttp3/Dispatcher;
            //   185: aload_0        
            //   186: invokevirtual   okhttp3/Dispatcher.finished:(Lokhttp3/RealCall$AsyncCall;)V
            //   189: aload_3        
            //   190: athrow         
            //   191: astore_3       
            //   192: goto            65
            //    Exceptions:
            //  Try           Handler
            //  Start  End    Start  End    Type                 
            //  -----  -----  -----  -----  ---------------------
            //  0      19     74     77     Ljava/io/IOException;
            //  0      19     70     74     Any
            //  23     45     191    70     Ljava/io/IOException;
            //  23     45     70     74     Any
            //  48     62     191    70     Ljava/io/IOException;
            //  48     62     70     74     Any
            //  81     128    70     74     Any
            //  131    160    70     74     Any
            // 
            // The error that occurred was:
            // 
            // java.lang.IllegalStateException: Expression is linked from several locations: Label_0048:
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
        
        RealCall get() {
            return RealCall.this;
        }
        
        String host() {
            return RealCall.this.originalRequest.url().host();
        }
    }
}
