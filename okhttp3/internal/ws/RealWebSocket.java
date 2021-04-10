package okhttp3.internal.ws;

import java.util.*;
import okhttp3.*;
import okhttp3.internal.*;
import java.io.*;
import okhttp3.internal.connection.*;
import javax.annotation.*;
import java.util.concurrent.*;
import java.net.*;
import okio.*;

public final class RealWebSocket implements WebSocket, FrameCallback
{
    private static final List<Protocol> ONLY_HTTP1;
    private boolean awaitingPong;
    private Call call;
    private ScheduledFuture<?> cancelFuture;
    private boolean enqueuedClose;
    private ScheduledExecutorService executor;
    private boolean failed;
    private final String key;
    final WebSocketListener listener;
    private final ArrayDeque<Object> messageAndCloseQueue;
    private final Request originalRequest;
    private final long pingIntervalMillis;
    private final ArrayDeque<ByteString> pongQueue;
    private long queueSize;
    private final Random random;
    private WebSocketReader reader;
    private int receivedCloseCode;
    private String receivedCloseReason;
    private int receivedPingCount;
    private int receivedPongCount;
    private int sentPingCount;
    private Streams streams;
    private WebSocketWriter writer;
    private final Runnable writerRunnable;
    
    static {
        ONLY_HTTP1 = Collections.singletonList(Protocol.HTTP_1_1);
    }
    
    public RealWebSocket(final Request originalRequest, final WebSocketListener listener, final Random random, final long pingIntervalMillis) {
        this.pongQueue = new ArrayDeque<ByteString>();
        this.messageAndCloseQueue = new ArrayDeque<Object>();
        this.receivedCloseCode = -1;
        if ("GET".equals(originalRequest.method())) {
            this.originalRequest = originalRequest;
            this.listener = listener;
            this.random = random;
            this.pingIntervalMillis = pingIntervalMillis;
            final byte[] array = new byte[16];
            random.nextBytes(array);
            this.key = ByteString.of(array).base64();
            this.writerRunnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        while (RealWebSocket.this.writeOneFrame()) {}
                    }
                    catch (IOException ex) {
                        RealWebSocket.this.failWebSocket(ex, null);
                    }
                }
            };
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Request must be GET: ");
        sb.append(originalRequest.method());
        throw new IllegalArgumentException(sb.toString());
    }
    
    private void runWriter() {
        final ScheduledExecutorService executor = this.executor;
        if (executor != null) {
            executor.execute(this.writerRunnable);
        }
    }
    
    public void cancel() {
        this.call.cancel();
    }
    
    void checkResponse(final Response response) throws ProtocolException {
        if (response.code() != 101) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Expected HTTP 101 response but was '");
            sb.append(response.code());
            sb.append(" ");
            sb.append(response.message());
            sb.append("'");
            throw new ProtocolException(sb.toString());
        }
        final String header = response.header("Connection");
        if (!"Upgrade".equalsIgnoreCase(header)) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Expected 'Connection' header value 'Upgrade' but was '");
            sb2.append(header);
            sb2.append("'");
            throw new ProtocolException(sb2.toString());
        }
        final String header2 = response.header("Upgrade");
        if (!"websocket".equalsIgnoreCase(header2)) {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("Expected 'Upgrade' header value 'websocket' but was '");
            sb3.append(header2);
            sb3.append("'");
            throw new ProtocolException(sb3.toString());
        }
        final String header3 = response.header("Sec-WebSocket-Accept");
        final StringBuilder sb4 = new StringBuilder();
        sb4.append(this.key);
        sb4.append("258EAFA5-E914-47DA-95CA-C5AB0DC85B11");
        final String base64 = ByteString.encodeUtf8(sb4.toString()).sha1().base64();
        if (base64.equals(header3)) {
            return;
        }
        final StringBuilder sb5 = new StringBuilder();
        sb5.append("Expected 'Sec-WebSocket-Accept' header value '");
        sb5.append(base64);
        sb5.append("' but was '");
        sb5.append(header3);
        sb5.append("'");
        throw new ProtocolException(sb5.toString());
    }
    
    public void connect(final OkHttpClient okHttpClient) {
        final OkHttpClient build = okHttpClient.newBuilder().eventListener(EventListener.NONE).protocols(RealWebSocket.ONLY_HTTP1).build();
        final Request build2 = this.originalRequest.newBuilder().header("Upgrade", "websocket").header("Connection", "Upgrade").header("Sec-WebSocket-Key", this.key).header("Sec-WebSocket-Version", "13").build();
        (this.call = Internal.instance.newWebSocketCall(build, build2)).enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, final IOException ex) {
                RealWebSocket.this.failWebSocket(ex, null);
            }
            
            @Override
            public void onResponse(final Call call, Response string) {
                try {
                    RealWebSocket.this.checkResponse(string);
                    final StreamAllocation streamAllocation = Internal.instance.streamAllocation(call);
                    streamAllocation.noNewStreams();
                    final Streams webSocketStreams = streamAllocation.connection().newWebSocketStreams(streamAllocation);
                    try {
                        RealWebSocket.this.listener.onOpen(RealWebSocket.this, string);
                        string = (Response)new StringBuilder();
                        ((StringBuilder)string).append("OkHttp WebSocket ");
                        ((StringBuilder)string).append(build2.url().redact());
                        string = (Response)((StringBuilder)string).toString();
                        RealWebSocket.this.initReaderAndWriter((String)string, webSocketStreams);
                        streamAllocation.connection().socket().setSoTimeout(0);
                        RealWebSocket.this.loopReader();
                    }
                    catch (Exception ex) {
                        RealWebSocket.this.failWebSocket(ex, null);
                    }
                }
                catch (ProtocolException ex2) {
                    RealWebSocket.this.failWebSocket(ex2, string);
                    Util.closeQuietly(string);
                }
            }
        });
    }
    
    public void failWebSocket(final Exception ex, @Nullable final Response response) {
        synchronized (this) {
            if (this.failed) {
                return;
            }
            this.failed = true;
            final Streams streams = this.streams;
            this.streams = null;
            if (this.cancelFuture != null) {
                this.cancelFuture.cancel(false);
            }
            if (this.executor != null) {
                this.executor.shutdown();
            }
            // monitorexit(this)
            try {
                this.listener.onFailure(this, ex, response);
            }
            finally {
                Util.closeQuietly(streams);
            }
        }
    }
    
    public void initReaderAndWriter(final String s, final Streams streams) throws IOException {
        synchronized (this) {
            this.streams = streams;
            this.writer = new WebSocketWriter(streams.client, streams.sink, this.random);
            final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1, Util.threadFactory(s, false));
            this.executor = executor;
            if (this.pingIntervalMillis != 0L) {
                executor.scheduleAtFixedRate(new PingRunnable(), this.pingIntervalMillis, this.pingIntervalMillis, TimeUnit.MILLISECONDS);
            }
            if (!this.messageAndCloseQueue.isEmpty()) {
                this.runWriter();
            }
            // monitorexit(this)
            this.reader = new WebSocketReader(streams.client, streams.source, (WebSocketReader.FrameCallback)this);
        }
    }
    
    public void loopReader() throws IOException {
        while (this.receivedCloseCode == -1) {
            this.reader.processNextFrame();
        }
    }
    
    @Override
    public void onReadClose(final int receivedCloseCode, final String receivedCloseReason) {
        if (receivedCloseCode != -1) {
            while (true) {
                while (true) {
                    Label_0144: {
                        synchronized (this) {
                            if (this.receivedCloseCode == -1) {
                                this.receivedCloseCode = receivedCloseCode;
                                this.receivedCloseReason = receivedCloseReason;
                                if (!this.enqueuedClose || !this.messageAndCloseQueue.isEmpty()) {
                                    break Label_0144;
                                }
                                final Streams streams = this.streams;
                                this.streams = null;
                                if (this.cancelFuture != null) {
                                    this.cancelFuture.cancel(false);
                                }
                                this.executor.shutdown();
                                // monitorexit(this)
                                try {
                                    this.listener.onClosing(this, receivedCloseCode, receivedCloseReason);
                                    if (streams != null) {
                                        this.listener.onClosed(this, receivedCloseCode, receivedCloseReason);
                                    }
                                    return;
                                }
                                finally {
                                    Util.closeQuietly(streams);
                                }
                            }
                            throw new IllegalStateException("already closed");
                        }
                        break;
                    }
                    final Streams streams = null;
                    continue;
                }
            }
        }
        throw new IllegalArgumentException();
    }
    
    @Override
    public void onReadMessage(final String s) throws IOException {
        this.listener.onMessage(this, s);
    }
    
    @Override
    public void onReadMessage(final ByteString byteString) throws IOException {
        this.listener.onMessage(this, byteString);
    }
    
    @Override
    public void onReadPing(final ByteString byteString) {
        synchronized (this) {
            if (!this.failed && (!this.enqueuedClose || !this.messageAndCloseQueue.isEmpty())) {
                this.pongQueue.add(byteString);
                this.runWriter();
                ++this.receivedPingCount;
            }
        }
    }
    
    @Override
    public void onReadPong(final ByteString byteString) {
        synchronized (this) {
            ++this.receivedPongCount;
            this.awaitingPong = false;
        }
    }
    
    boolean writeOneFrame() throws IOException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     2: aload_0        
        //     3: getfield        okhttp3/internal/ws/RealWebSocket.failed:Z
        //     6: ifeq            13
        //     9: aload_0        
        //    10: monitorexit    
        //    11: iconst_0       
        //    12: ireturn        
        //    13: aload_0        
        //    14: getfield        okhttp3/internal/ws/RealWebSocket.writer:Lokhttp3/internal/ws/WebSocketWriter;
        //    17: astore          6
        //    19: aload_0        
        //    20: getfield        okhttp3/internal/ws/RealWebSocket.pongQueue:Ljava/util/ArrayDeque;
        //    23: invokevirtual   java/util/ArrayDeque.poll:()Ljava/lang/Object;
        //    26: checkcast       Lokio/ByteString;
        //    29: astore          7
        //    31: iconst_m1      
        //    32: istore_1       
        //    33: aconst_null    
        //    34: astore_2       
        //    35: aconst_null    
        //    36: astore_3       
        //    37: aload           7
        //    39: ifnonnull       322
        //    42: aload_0        
        //    43: getfield        okhttp3/internal/ws/RealWebSocket.messageAndCloseQueue:Ljava/util/ArrayDeque;
        //    46: invokevirtual   java/util/ArrayDeque.poll:()Ljava/lang/Object;
        //    49: astore          4
        //    51: aload           4
        //    53: instanceof      Lokhttp3/internal/ws/RealWebSocket$Close;
        //    56: ifeq            132
        //    59: aload_0        
        //    60: getfield        okhttp3/internal/ws/RealWebSocket.receivedCloseCode:I
        //    63: istore_1       
        //    64: aload_0        
        //    65: getfield        okhttp3/internal/ws/RealWebSocket.receivedCloseReason:Ljava/lang/String;
        //    68: astore          5
        //    70: iload_1        
        //    71: iconst_m1      
        //    72: if_icmpeq       97
        //    75: aload_0        
        //    76: getfield        okhttp3/internal/ws/RealWebSocket.streams:Lokhttp3/internal/ws/RealWebSocket$Streams;
        //    79: astore_2       
        //    80: aload_0        
        //    81: aconst_null    
        //    82: putfield        okhttp3/internal/ws/RealWebSocket.streams:Lokhttp3/internal/ws/RealWebSocket$Streams;
        //    85: aload_0        
        //    86: getfield        okhttp3/internal/ws/RealWebSocket.executor:Ljava/util/concurrent/ScheduledExecutorService;
        //    89: invokeinterface java/util/concurrent/ScheduledExecutorService.shutdown:()V
        //    94: goto            309
        //    97: aload_0        
        //    98: aload_0        
        //    99: getfield        okhttp3/internal/ws/RealWebSocket.executor:Ljava/util/concurrent/ScheduledExecutorService;
        //   102: new             Lokhttp3/internal/ws/RealWebSocket$CancelRunnable;
        //   105: dup            
        //   106: aload_0        
        //   107: invokespecial   okhttp3/internal/ws/RealWebSocket$CancelRunnable.<init>:(Lokhttp3/internal/ws/RealWebSocket;)V
        //   110: aload           4
        //   112: checkcast       Lokhttp3/internal/ws/RealWebSocket$Close;
        //   115: getfield        okhttp3/internal/ws/RealWebSocket$Close.cancelAfterCloseMillis:J
        //   118: getstatic       java/util/concurrent/TimeUnit.MILLISECONDS:Ljava/util/concurrent/TimeUnit;
        //   121: invokeinterface java/util/concurrent/ScheduledExecutorService.schedule:(Ljava/lang/Runnable;JLjava/util/concurrent/TimeUnit;)Ljava/util/concurrent/ScheduledFuture;
        //   126: putfield        okhttp3/internal/ws/RealWebSocket.cancelFuture:Ljava/util/concurrent/ScheduledFuture;
        //   129: goto            309
        //   132: aload           4
        //   134: ifnonnull       317
        //   137: aload_0        
        //   138: monitorexit    
        //   139: iconst_0       
        //   140: ireturn        
        //   141: aload_0        
        //   142: monitorexit    
        //   143: aload           7
        //   145: ifnull          158
        //   148: aload           6
        //   150: aload           7
        //   152: invokevirtual   okhttp3/internal/ws/WebSocketWriter.writePong:(Lokio/ByteString;)V
        //   155: goto            284
        //   158: aload           4
        //   160: instanceof      Lokhttp3/internal/ws/RealWebSocket$Message;
        //   163: ifeq            240
        //   166: aload           4
        //   168: checkcast       Lokhttp3/internal/ws/RealWebSocket$Message;
        //   171: getfield        okhttp3/internal/ws/RealWebSocket$Message.data:Lokio/ByteString;
        //   174: astore_2       
        //   175: aload           6
        //   177: aload           4
        //   179: checkcast       Lokhttp3/internal/ws/RealWebSocket$Message;
        //   182: getfield        okhttp3/internal/ws/RealWebSocket$Message.formatOpcode:I
        //   185: aload_2        
        //   186: invokevirtual   okio/ByteString.size:()I
        //   189: i2l            
        //   190: invokevirtual   okhttp3/internal/ws/WebSocketWriter.newMessageSink:(IJ)Lokio/Sink;
        //   193: invokestatic    okio/Okio.buffer:(Lokio/Sink;)Lokio/BufferedSink;
        //   196: astore          4
        //   198: aload           4
        //   200: aload_2        
        //   201: invokeinterface okio/BufferedSink.write:(Lokio/ByteString;)Lokio/BufferedSink;
        //   206: pop            
        //   207: aload           4
        //   209: invokeinterface okio/BufferedSink.close:()V
        //   214: aload_0        
        //   215: monitorenter   
        //   216: aload_0        
        //   217: aload_0        
        //   218: getfield        okhttp3/internal/ws/RealWebSocket.queueSize:J
        //   221: aload_2        
        //   222: invokevirtual   okio/ByteString.size:()I
        //   225: i2l            
        //   226: lsub           
        //   227: putfield        okhttp3/internal/ws/RealWebSocket.queueSize:J
        //   230: aload_0        
        //   231: monitorexit    
        //   232: goto            284
        //   235: astore_2       
        //   236: aload_0        
        //   237: monitorexit    
        //   238: aload_2        
        //   239: athrow         
        //   240: aload           4
        //   242: instanceof      Lokhttp3/internal/ws/RealWebSocket$Close;
        //   245: ifeq            290
        //   248: aload           4
        //   250: checkcast       Lokhttp3/internal/ws/RealWebSocket$Close;
        //   253: astore          4
        //   255: aload           6
        //   257: aload           4
        //   259: getfield        okhttp3/internal/ws/RealWebSocket$Close.code:I
        //   262: aload           4
        //   264: getfield        okhttp3/internal/ws/RealWebSocket$Close.reason:Lokio/ByteString;
        //   267: invokevirtual   okhttp3/internal/ws/WebSocketWriter.writeClose:(ILokio/ByteString;)V
        //   270: aload_3        
        //   271: ifnull          284
        //   274: aload_0        
        //   275: getfield        okhttp3/internal/ws/RealWebSocket.listener:Lokhttp3/WebSocketListener;
        //   278: aload_0        
        //   279: iload_1        
        //   280: aload_2        
        //   281: invokevirtual   okhttp3/WebSocketListener.onClosed:(Lokhttp3/WebSocket;ILjava/lang/String;)V
        //   284: aload_3        
        //   285: invokestatic    okhttp3/internal/Util.closeQuietly:(Ljava/io/Closeable;)V
        //   288: iconst_1       
        //   289: ireturn        
        //   290: new             Ljava/lang/AssertionError;
        //   293: dup            
        //   294: invokespecial   java/lang/AssertionError.<init>:()V
        //   297: athrow         
        //   298: aload_3        
        //   299: invokestatic    okhttp3/internal/Util.closeQuietly:(Ljava/io/Closeable;)V
        //   302: aload_2        
        //   303: athrow         
        //   304: astore_2       
        //   305: aload_0        
        //   306: monitorexit    
        //   307: aload_2        
        //   308: athrow         
        //   309: aload_2        
        //   310: astore_3       
        //   311: aload           5
        //   313: astore_2       
        //   314: goto            141
        //   317: aconst_null    
        //   318: astore_2       
        //   319: goto            141
        //   322: aconst_null    
        //   323: astore          4
        //   325: aload           4
        //   327: astore_2       
        //   328: goto            141
        //   331: astore_2       
        //   332: goto            298
        //    Exceptions:
        //  throws java.io.IOException
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type
        //  -----  -----  -----  -----  ----
        //  2      11     304    309    Any
        //  13     31     304    309    Any
        //  42     70     304    309    Any
        //  75     94     304    309    Any
        //  97     129    304    309    Any
        //  137    139    304    309    Any
        //  141    143    304    309    Any
        //  148    155    331    304    Any
        //  158    216    331    304    Any
        //  216    232    235    240    Any
        //  236    238    235    240    Any
        //  238    240    331    304    Any
        //  240    270    331    304    Any
        //  274    284    331    304    Any
        //  290    298    331    304    Any
        //  305    307    304    309    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0158:
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
    
    void writePingFrame() {
        while (true) {
            while (true) {
                synchronized (this) {
                    if (this.failed) {
                        return;
                    }
                    final WebSocketWriter writer = this.writer;
                    if (this.awaitingPong) {
                        final int sentPingCount = this.sentPingCount;
                        ++this.sentPingCount;
                        this.awaitingPong = true;
                        // monitorexit(this)
                        if (sentPingCount != -1) {
                            final StringBuilder sb = new StringBuilder();
                            sb.append("sent ping but didn't receive pong within ");
                            sb.append(this.pingIntervalMillis);
                            sb.append("ms (after ");
                            sb.append(sentPingCount - 1);
                            sb.append(" successful ping/pongs)");
                            this.failWebSocket(new SocketTimeoutException(sb.toString()), null);
                            return;
                        }
                        try {
                            writer.writePing(ByteString.EMPTY);
                            return;
                        }
                        catch (IOException ex) {
                            this.failWebSocket(ex, null);
                            return;
                        }
                    }
                }
                final int sentPingCount = -1;
                continue;
            }
        }
    }
    
    final class CancelRunnable implements Runnable
    {
        @Override
        public void run() {
            RealWebSocket.this.cancel();
        }
    }
    
    static final class Close
    {
        final long cancelAfterCloseMillis;
        final int code;
        final ByteString reason;
    }
    
    static final class Message
    {
        final ByteString data;
        final int formatOpcode;
    }
    
    private final class PingRunnable implements Runnable
    {
        PingRunnable() {
        }
        
        @Override
        public void run() {
            RealWebSocket.this.writePingFrame();
        }
    }
    
    public abstract static class Streams implements Closeable
    {
        public final boolean client;
        public final BufferedSink sink;
        public final BufferedSource source;
        
        public Streams(final boolean client, final BufferedSource source, final BufferedSink sink) {
            this.client = client;
            this.source = source;
            this.sink = sink;
        }
    }
}
