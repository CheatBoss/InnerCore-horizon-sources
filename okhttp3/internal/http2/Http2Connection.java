package okhttp3.internal.http2;

import java.net.*;
import java.io.*;
import java.util.*;
import okhttp3.internal.*;
import java.util.concurrent.*;
import okio.*;
import okhttp3.internal.platform.*;

public final class Http2Connection implements Closeable
{
    private static final ExecutorService listenerExecutor;
    private boolean awaitingPong;
    long bytesLeftInWriteWindow;
    final boolean client;
    final Set<Integer> currentPushRequests;
    final String hostname;
    int lastGoodStreamId;
    final Listener listener;
    int nextStreamId;
    Settings okHttpSettings;
    final Settings peerSettings;
    private final ExecutorService pushExecutor;
    final PushObserver pushObserver;
    final ReaderRunnable readerRunnable;
    boolean receivedInitialPeerSettings;
    boolean shutdown;
    final Socket socket;
    final Map<Integer, Http2Stream> streams;
    long unacknowledgedBytesRead;
    final Http2Writer writer;
    private final ScheduledExecutorService writerExecutor;
    
    static {
        listenerExecutor = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>(), Util.threadFactory("OkHttp Http2Connection", true));
    }
    
    Http2Connection(final Builder builder) {
        this.streams = new LinkedHashMap<Integer, Http2Stream>();
        this.unacknowledgedBytesRead = 0L;
        this.okHttpSettings = new Settings();
        this.peerSettings = new Settings();
        this.receivedInitialPeerSettings = false;
        this.currentPushRequests = new LinkedHashSet<Integer>();
        this.pushObserver = builder.pushObserver;
        this.client = builder.client;
        this.listener = builder.listener;
        int nextStreamId;
        if (builder.client) {
            nextStreamId = 1;
        }
        else {
            nextStreamId = 2;
        }
        this.nextStreamId = nextStreamId;
        if (builder.client) {
            this.nextStreamId += 2;
        }
        if (builder.client) {
            this.okHttpSettings.set(7, 16777216);
        }
        this.hostname = builder.hostname;
        this.writerExecutor = new ScheduledThreadPoolExecutor(1, Util.threadFactory(Util.format("OkHttp %s Writer", this.hostname), false));
        if (builder.pingIntervalMillis != 0) {
            this.writerExecutor.scheduleAtFixedRate(new PingRunnable(false, 0, 0), builder.pingIntervalMillis, builder.pingIntervalMillis, TimeUnit.MILLISECONDS);
        }
        this.pushExecutor = new ThreadPoolExecutor(0, 1, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), Util.threadFactory(Util.format("OkHttp %s Push Observer", this.hostname), true));
        this.peerSettings.set(7, 65535);
        this.peerSettings.set(5, 16384);
        this.bytesLeftInWriteWindow = this.peerSettings.getInitialWindowSize();
        this.socket = builder.socket;
        this.writer = new Http2Writer(builder.sink, this.client);
        this.readerRunnable = new ReaderRunnable(new Http2Reader(builder.source, this.client));
    }
    
    private void failConnection() {
        try {
            this.close(ErrorCode.PROTOCOL_ERROR, ErrorCode.PROTOCOL_ERROR);
        }
        catch (IOException ex) {}
    }
    
    private Http2Stream newStream(final int n, final List<Header> list, final boolean b) throws IOException {
        while (true) {
            final boolean b2 = b ^ true;
        Block_13_Outer:
            while (true) {
                Label_0216: {
                    Label_0210: {
                        synchronized (this.writer) {
                            synchronized (this) {
                                if (this.nextStreamId > 1073741823) {
                                    this.shutdown(ErrorCode.REFUSED_STREAM);
                                }
                                if (this.shutdown) {
                                    throw new ConnectionShutdownException();
                                }
                                final int nextStreamId = this.nextStreamId;
                                this.nextStreamId += 2;
                                final Http2Stream http2Stream = new Http2Stream(nextStreamId, this, b2, false, list);
                                if (b && this.bytesLeftInWriteWindow != 0L && http2Stream.bytesLeftInWriteWindow != 0L) {
                                    break Label_0210;
                                }
                                break Label_0216;
                                Label_0177: {
                                    return http2Stream;
                                }
                                // iftrue(Label_0122:, !http2Stream.isOpen())
                                // iftrue(Label_0180:, this.client)
                                // monitorexit(this)
                                // iftrue(Label_0144:, n != 0)
                                // monitorexit(this.writer)
                                // iftrue(Label_0177:, n2 == 0)
                                while (true) {
                                    Label_0162: {
                                        while (true) {
                                            Block_10: {
                                                break Block_10;
                                                final List<Header> list2;
                                                this.writer.synStream(b2, nextStreamId, n, list2);
                                                break Label_0162;
                                                Label_0144:
                                                this.writer.pushPromise(n, nextStreamId, list2);
                                                break Label_0162;
                                            }
                                            this.streams.put(nextStreamId, http2Stream);
                                            Label_0122:
                                            continue Block_13_Outer;
                                        }
                                        this.writer.flush();
                                        return http2Stream;
                                    }
                                    continue;
                                }
                                Label_0180:
                                throw new IllegalArgumentException("client streams shouldn't have associated stream IDs");
                            }
                        }
                    }
                    final int n2 = 0;
                    continue;
                }
                final int n2 = 1;
                continue;
            }
        }
    }
    
    void addBytesToWriteWindow(final long n) {
        this.bytesLeftInWriteWindow += n;
        if (n > 0L) {
            this.notifyAll();
        }
    }
    
    @Override
    public void close() throws IOException {
        this.close(ErrorCode.NO_ERROR, ErrorCode.CANCEL);
    }
    
    void close(ErrorCode errorCode, final ErrorCode errorCode2) throws IOException {
        Http2Stream[] array = null;
        try {
            this.shutdown(errorCode);
            errorCode = null;
        }
        catch (IOException ex2) {}
        synchronized (this) {
            if (!this.streams.isEmpty()) {
                array = this.streams.values().toArray(new Http2Stream[this.streams.size()]);
                this.streams.clear();
            }
            // monitorexit(this)
            ErrorCode errorCode3 = errorCode;
            if (array != null) {
                final int length = array.length;
                int n = 0;
                while (true) {
                    errorCode3 = errorCode;
                    if (n >= length) {
                        break;
                    }
                    final Http2Stream http2Stream = array[n];
                    ErrorCode errorCode4;
                    try {
                        http2Stream.close(errorCode2);
                        errorCode4 = errorCode;
                    }
                    catch (IOException ex) {
                        errorCode4 = errorCode;
                        if (errorCode != null) {
                            errorCode4 = (ErrorCode)ex;
                        }
                    }
                    ++n;
                    errorCode = errorCode4;
                }
            }
            try {
                this.writer.close();
                errorCode = errorCode3;
            }
            catch (IOException errorCode5) {
                errorCode = errorCode3;
                if (errorCode3 == null) {
                    errorCode = errorCode5;
                }
            }
            try {
                this.socket.close();
            }
            catch (IOException ex3) {}
            this.writerExecutor.shutdown();
            this.pushExecutor.shutdown();
            if (errorCode == null) {
                return;
            }
            throw errorCode;
        }
    }
    
    public void flush() throws IOException {
        this.writer.flush();
    }
    
    Http2Stream getStream(final int n) {
        synchronized (this) {
            return this.streams.get(n);
        }
    }
    
    public boolean isShutdown() {
        synchronized (this) {
            return this.shutdown;
        }
    }
    
    public int maxConcurrentStreams() {
        synchronized (this) {
            return this.peerSettings.getMaxConcurrentStreams(Integer.MAX_VALUE);
        }
    }
    
    public Http2Stream newStream(final List<Header> list, final boolean b) throws IOException {
        return this.newStream(0, list, b);
    }
    
    void pushDataLater(final int n, final BufferedSource bufferedSource, final int n2, final boolean b) throws IOException {
        final Buffer buffer = new Buffer();
        final long n3 = n2;
        bufferedSource.require(n3);
        bufferedSource.read(buffer, n3);
        if (buffer.size() == n3) {
            this.pushExecutor.execute(new NamedRunnable("OkHttp %s Push Data[%s]", new Object[] { this.hostname, n }) {
                public void execute() {
                    try {
                        final boolean onData = Http2Connection.this.pushObserver.onData(n, buffer, n2, b);
                        if (onData) {
                            Http2Connection.this.writer.rstStream(n, ErrorCode.CANCEL);
                        }
                        if (onData || b) {
                            synchronized (Http2Connection.this) {
                                Http2Connection.this.currentPushRequests.remove(n);
                            }
                        }
                    }
                    catch (IOException ex) {}
                }
            });
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(buffer.size());
        sb.append(" != ");
        sb.append(n2);
        throw new IOException(sb.toString());
    }
    
    void pushHeadersLater(final int n, final List<Header> list, final boolean b) {
        try {
            this.pushExecutor.execute(new NamedRunnable("OkHttp %s Push Headers[%s]", new Object[] { this.hostname, n }) {
                public void execute() {
                    final boolean onHeaders = Http2Connection.this.pushObserver.onHeaders(n, list, b);
                    while (true) {
                        if (onHeaders) {
                            try {
                                Http2Connection.this.writer.rstStream(n, ErrorCode.CANCEL);
                                if (onHeaders || b) {
                                    synchronized (Http2Connection.this) {
                                        Http2Connection.this.currentPushRequests.remove(n);
                                    }
                                }
                            }
                            catch (IOException ex) {}
                            return;
                        }
                        continue;
                    }
                }
            });
        }
        catch (RejectedExecutionException ex) {}
    }
    
    void pushRequestLater(final int n, final List<Header> list) {
        synchronized (this) {
            if (this.currentPushRequests.contains(n)) {
                this.writeSynResetLater(n, ErrorCode.PROTOCOL_ERROR);
                return;
            }
            this.currentPushRequests.add(n);
            // monitorexit(this)
            try {
                this.pushExecutor.execute(new NamedRunnable("OkHttp %s Push Request[%s]", new Object[] { this.hostname, n }) {
                    public void execute() {
                        if (Http2Connection.this.pushObserver.onRequest(n, list)) {
                            try {
                                Http2Connection.this.writer.rstStream(n, ErrorCode.CANCEL);
                                synchronized (Http2Connection.this) {
                                    Http2Connection.this.currentPushRequests.remove(n);
                                }
                            }
                            catch (IOException ex) {}
                        }
                    }
                });
            }
            catch (RejectedExecutionException ex) {}
        }
    }
    
    void pushResetLater(final int n, final ErrorCode errorCode) {
        this.pushExecutor.execute(new NamedRunnable("OkHttp %s Push Reset[%s]", new Object[] { this.hostname, n }) {
            public void execute() {
                Http2Connection.this.pushObserver.onReset(n, errorCode);
                synchronized (Http2Connection.this) {
                    Http2Connection.this.currentPushRequests.remove(n);
                }
            }
        });
    }
    
    boolean pushedStream(final int n) {
        return n != 0 && (n & 0x1) == 0x0;
    }
    
    Http2Stream removeStream(final int n) {
        synchronized (this) {
            final Http2Stream http2Stream = this.streams.remove(n);
            this.notifyAll();
            return http2Stream;
        }
    }
    
    public void shutdown(final ErrorCode errorCode) throws IOException {
        synchronized (this.writer) {
            synchronized (this) {
                if (this.shutdown) {
                    return;
                }
                this.shutdown = true;
                final int lastGoodStreamId = this.lastGoodStreamId;
                // monitorexit(this)
                this.writer.goAway(lastGoodStreamId, errorCode, Util.EMPTY_BYTE_ARRAY);
            }
        }
    }
    
    public void start() throws IOException {
        this.start(true);
    }
    
    void start(final boolean b) throws IOException {
        if (b) {
            this.writer.connectionPreface();
            this.writer.settings(this.okHttpSettings);
            final int initialWindowSize = this.okHttpSettings.getInitialWindowSize();
            if (initialWindowSize != 65535) {
                this.writer.windowUpdate(0, initialWindowSize - 65535);
            }
        }
        new Thread(this.readerRunnable).start();
    }
    
    public void writeData(final int n, final boolean b, final Buffer buffer, long bytesLeftInWriteWindow) throws IOException {
        long n2 = bytesLeftInWriteWindow;
        if (bytesLeftInWriteWindow == 0L) {
            this.writer.data(b, n, buffer, 0);
            return;
        }
        while (n2 > 0L) {
            // monitorenter(this)
            try {
                try {
                    while (this.bytesLeftInWriteWindow <= 0L) {
                        if (!this.streams.containsKey(n)) {
                            throw new IOException("stream closed");
                        }
                        this.wait();
                    }
                    final int min = Math.min((int)Math.min(n2, this.bytesLeftInWriteWindow), this.writer.maxDataLength());
                    bytesLeftInWriteWindow = this.bytesLeftInWriteWindow;
                    final long n3 = min;
                    this.bytesLeftInWriteWindow = bytesLeftInWriteWindow - n3;
                    // monitorexit(this)
                    n2 -= n3;
                    this.writer.data(b && n2 == 0L, n, buffer, min);
                }
                finally {
                }
                // monitorexit(this)
            }
            catch (InterruptedException ex) {}
            break;
        }
    }
    
    void writePing(final boolean b, final int n, final int n2) {
        if (!b) {
            synchronized (this) {
                final boolean awaitingPong = this.awaitingPong;
                this.awaitingPong = true;
                // monitorexit(this)
                if (awaitingPong) {
                    this.failConnection();
                    return;
                }
            }
        }
        try {
            this.writer.ping(b, n, n2);
        }
        catch (IOException ex) {
            this.failConnection();
        }
    }
    
    void writeSynReset(final int n, final ErrorCode errorCode) throws IOException {
        this.writer.rstStream(n, errorCode);
    }
    
    void writeSynResetLater(final int n, final ErrorCode errorCode) {
        try {
            this.writerExecutor.execute(new NamedRunnable("OkHttp %s stream %d", new Object[] { this.hostname, n }) {
                public void execute() {
                    try {
                        Http2Connection.this.writeSynReset(n, errorCode);
                    }
                    catch (IOException ex) {
                        Http2Connection.this.failConnection();
                    }
                }
            });
        }
        catch (RejectedExecutionException ex) {}
    }
    
    void writeWindowUpdateLater(final int n, final long n2) {
        try {
            this.writerExecutor.execute(new NamedRunnable("OkHttp Window Update %s stream %d", new Object[] { this.hostname, n }) {
                public void execute() {
                    try {
                        Http2Connection.this.writer.windowUpdate(n, n2);
                    }
                    catch (IOException ex) {
                        Http2Connection.this.failConnection();
                    }
                }
            });
        }
        catch (RejectedExecutionException ex) {}
    }
    
    public static class Builder
    {
        boolean client;
        String hostname;
        Listener listener;
        int pingIntervalMillis;
        PushObserver pushObserver;
        BufferedSink sink;
        Socket socket;
        BufferedSource source;
        
        public Builder(final boolean client) {
            this.listener = Listener.REFUSE_INCOMING_STREAMS;
            this.pushObserver = PushObserver.CANCEL;
            this.client = client;
        }
        
        public Http2Connection build() {
            return new Http2Connection(this);
        }
        
        public Builder listener(final Listener listener) {
            this.listener = listener;
            return this;
        }
        
        public Builder pingIntervalMillis(final int pingIntervalMillis) {
            this.pingIntervalMillis = pingIntervalMillis;
            return this;
        }
        
        public Builder socket(final Socket socket, final String hostname, final BufferedSource source, final BufferedSink sink) {
            this.socket = socket;
            this.hostname = hostname;
            this.source = source;
            this.sink = sink;
            return this;
        }
    }
    
    public abstract static class Listener
    {
        public static final Listener REFUSE_INCOMING_STREAMS;
        
        static {
            REFUSE_INCOMING_STREAMS = new Listener() {
                @Override
                public void onStream(final Http2Stream http2Stream) throws IOException {
                    http2Stream.close(ErrorCode.REFUSED_STREAM);
                }
            };
        }
        
        public void onSettings(final Http2Connection http2Connection) {
        }
        
        public abstract void onStream(final Http2Stream p0) throws IOException;
    }
    
    final class PingRunnable extends NamedRunnable
    {
        final int payload1;
        final int payload2;
        final boolean reply;
        
        PingRunnable(final boolean reply, final int payload1, final int payload2) {
            super("OkHttp %s ping %08x%08x", new Object[] { Http2Connection.this.hostname, payload1, payload2 });
            this.reply = reply;
            this.payload1 = payload1;
            this.payload2 = payload2;
        }
        
        public void execute() {
            Http2Connection.this.writePing(this.reply, this.payload1, this.payload2);
        }
    }
    
    class ReaderRunnable extends NamedRunnable implements Handler
    {
        final Http2Reader reader;
        
        ReaderRunnable(final Http2Reader reader) {
            super("OkHttp %s", new Object[] { Http2Connection.this.hostname });
            this.reader = reader;
        }
        
        private void applyAndAckSettings(final Settings settings) {
            try {
                Http2Connection.this.writerExecutor.execute(new NamedRunnable("OkHttp %s ACK Settings", new Object[] { Http2Connection.this.hostname }) {
                    public void execute() {
                        try {
                            Http2Connection.this.writer.applyAndAckSettings(settings);
                        }
                        catch (IOException ex) {
                            Http2Connection.this.failConnection();
                        }
                    }
                });
            }
            catch (RejectedExecutionException ex) {}
        }
        
        @Override
        public void ackSettings() {
        }
        
        @Override
        public void data(final boolean b, final int n, final BufferedSource bufferedSource, final int n2) throws IOException {
            if (Http2Connection.this.pushedStream(n)) {
                Http2Connection.this.pushDataLater(n, bufferedSource, n2, b);
                return;
            }
            final Http2Stream stream = Http2Connection.this.getStream(n);
            if (stream == null) {
                Http2Connection.this.writeSynResetLater(n, ErrorCode.PROTOCOL_ERROR);
                bufferedSource.skip(n2);
                return;
            }
            stream.receiveData(bufferedSource, n2);
            if (b) {
                stream.receiveFin();
            }
        }
        
        @Override
        protected void execute() {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     3: astore_3       
            //     4: getstatic       okhttp3/internal/http2/ErrorCode.INTERNAL_ERROR:Lokhttp3/internal/http2/ErrorCode;
            //     7: astore          4
            //     9: aload_3        
            //    10: astore_1       
            //    11: aload_0        
            //    12: getfield        okhttp3/internal/http2/Http2Connection$ReaderRunnable.reader:Lokhttp3/internal/http2/Http2Reader;
            //    15: aload_0        
            //    16: invokevirtual   okhttp3/internal/http2/Http2Reader.readConnectionPreface:(Lokhttp3/internal/http2/Http2Reader$Handler;)V
            //    19: aload_3        
            //    20: astore_1       
            //    21: aload_0        
            //    22: getfield        okhttp3/internal/http2/Http2Connection$ReaderRunnable.reader:Lokhttp3/internal/http2/Http2Reader;
            //    25: iconst_0       
            //    26: aload_0        
            //    27: invokevirtual   okhttp3/internal/http2/Http2Reader.nextFrame:(ZLokhttp3/internal/http2/Http2Reader$Handler;)Z
            //    30: ifeq            36
            //    33: goto            19
            //    36: aload_3        
            //    37: astore_1       
            //    38: getstatic       okhttp3/internal/http2/ErrorCode.NO_ERROR:Lokhttp3/internal/http2/ErrorCode;
            //    41: astore_2       
            //    42: aload_2        
            //    43: astore_1       
            //    44: getstatic       okhttp3/internal/http2/ErrorCode.CANCEL:Lokhttp3/internal/http2/ErrorCode;
            //    47: astore_3       
            //    48: aload_3        
            //    49: astore_1       
            //    50: aload_0        
            //    51: getfield        okhttp3/internal/http2/Http2Connection$ReaderRunnable.this$0:Lokhttp3/internal/http2/Http2Connection;
            //    54: astore_3       
            //    55: goto            90
            //    58: astore_1       
            //    59: aload_2        
            //    60: astore_1       
            //    61: goto            75
            //    64: astore_2       
            //    65: aload_1        
            //    66: astore_3       
            //    67: aload_2        
            //    68: astore_1       
            //    69: goto            113
            //    72: astore_1       
            //    73: aload_3        
            //    74: astore_1       
            //    75: getstatic       okhttp3/internal/http2/ErrorCode.PROTOCOL_ERROR:Lokhttp3/internal/http2/ErrorCode;
            //    78: astore_2       
            //    79: aload_2        
            //    80: astore_1       
            //    81: getstatic       okhttp3/internal/http2/ErrorCode.PROTOCOL_ERROR:Lokhttp3/internal/http2/ErrorCode;
            //    84: astore_3       
            //    85: aload_3        
            //    86: astore_1       
            //    87: goto            50
            //    90: aload_3        
            //    91: aload_2        
            //    92: aload_1        
            //    93: invokevirtual   okhttp3/internal/http2/Http2Connection.close:(Lokhttp3/internal/http2/ErrorCode;Lokhttp3/internal/http2/ErrorCode;)V
            //    96: goto            100
            //    99: astore_1       
            //   100: aload_0        
            //   101: getfield        okhttp3/internal/http2/Http2Connection$ReaderRunnable.reader:Lokhttp3/internal/http2/Http2Reader;
            //   104: invokestatic    okhttp3/internal/Util.closeQuietly:(Ljava/io/Closeable;)V
            //   107: return         
            //   108: astore_2       
            //   109: aload_1        
            //   110: astore_3       
            //   111: aload_2        
            //   112: astore_1       
            //   113: aload_0        
            //   114: getfield        okhttp3/internal/http2/Http2Connection$ReaderRunnable.this$0:Lokhttp3/internal/http2/Http2Connection;
            //   117: aload_3        
            //   118: aload           4
            //   120: invokevirtual   okhttp3/internal/http2/Http2Connection.close:(Lokhttp3/internal/http2/ErrorCode;Lokhttp3/internal/http2/ErrorCode;)V
            //   123: goto            127
            //   126: astore_2       
            //   127: aload_0        
            //   128: getfield        okhttp3/internal/http2/Http2Connection$ReaderRunnable.reader:Lokhttp3/internal/http2/Http2Reader;
            //   131: invokestatic    okhttp3/internal/Util.closeQuietly:(Ljava/io/Closeable;)V
            //   134: aload_1        
            //   135: athrow         
            //    Exceptions:
            //  Try           Handler
            //  Start  End    Start  End    Type                 
            //  -----  -----  -----  -----  ---------------------
            //  11     19     72     75     Ljava/io/IOException;
            //  11     19     64     72     Any
            //  21     33     72     75     Ljava/io/IOException;
            //  21     33     64     72     Any
            //  38     42     72     75     Ljava/io/IOException;
            //  38     42     64     72     Any
            //  44     48     58     64     Ljava/io/IOException;
            //  44     48     108    113    Any
            //  50     55     99     100    Ljava/io/IOException;
            //  75     79     64     72     Any
            //  81     85     108    113    Any
            //  90     96     99     100    Ljava/io/IOException;
            //  113    123    126    127    Ljava/io/IOException;
            // 
            // The error that occurred was:
            // 
            // java.util.ConcurrentModificationException
            //     at java.util.ArrayList$Itr.checkForComodification(Unknown Source)
            //     at java.util.ArrayList$Itr.next(Unknown Source)
            //     at com.strobel.decompiler.ast.AstBuilder.convertLocalVariables(AstBuilder.java:2863)
            //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2445)
            //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:108)
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
        
        @Override
        public void goAway(final int n, ErrorCode this$0, final ByteString byteString) {
            byteString.size();
            this$0 = (ErrorCode)Http2Connection.this;
            synchronized (this$0) {
                final Http2Stream[] array = Http2Connection.this.streams.values().toArray(new Http2Stream[Http2Connection.this.streams.size()]);
                Http2Connection.this.shutdown = true;
                // monitorexit(this$0)
                for (int length = array.length, i = 0; i < length; ++i) {
                    this$0 = (ErrorCode)array[i];
                    if (((Http2Stream)this$0).getId() > n && ((Http2Stream)this$0).isLocallyInitiated()) {
                        ((Http2Stream)this$0).receiveRstStream(ErrorCode.REFUSED_STREAM);
                        Http2Connection.this.removeStream(((Http2Stream)this$0).getId());
                    }
                }
            }
        }
        
        @Override
        public void headers(final boolean b, final int lastGoodStreamId, final int n, final List<Header> list) {
            if (Http2Connection.this.pushedStream(lastGoodStreamId)) {
                Http2Connection.this.pushHeadersLater(lastGoodStreamId, list, b);
                return;
            }
            synchronized (Http2Connection.this) {
                final Http2Stream stream = Http2Connection.this.getStream(lastGoodStreamId);
                if (stream != null) {
                    // monitorexit(this.this$0)
                    stream.receiveHeaders(list);
                    if (b) {
                        stream.receiveFin();
                    }
                    return;
                }
                if (Http2Connection.this.shutdown) {
                    return;
                }
                if (lastGoodStreamId <= Http2Connection.this.lastGoodStreamId) {
                    return;
                }
                if (lastGoodStreamId % 2 == Http2Connection.this.nextStreamId % 2) {
                    return;
                }
                final Http2Stream http2Stream = new Http2Stream(lastGoodStreamId, Http2Connection.this, false, b, list);
                Http2Connection.this.lastGoodStreamId = lastGoodStreamId;
                Http2Connection.this.streams.put(lastGoodStreamId, http2Stream);
                Http2Connection.listenerExecutor.execute(new NamedRunnable("OkHttp %s stream %d", new Object[] { Http2Connection.this.hostname, lastGoodStreamId }) {
                    public void execute() {
                        try {
                            Http2Connection.this.listener.onStream(http2Stream);
                        }
                        catch (IOException ex) {
                            final Platform value = Platform.get();
                            final StringBuilder sb = new StringBuilder();
                            sb.append("Http2Connection.Listener failure for ");
                            sb.append(Http2Connection.this.hostname);
                            value.log(4, sb.toString(), ex);
                            try {
                                http2Stream.close(ErrorCode.PROTOCOL_ERROR);
                            }
                            catch (IOException ex2) {}
                        }
                    }
                });
            }
        }
        
        @Override
        public void ping(final boolean b, final int n, final int n2) {
            if (b) {
                synchronized (Http2Connection.this) {
                    Http2Connection.this.awaitingPong = false;
                    Http2Connection.this.notifyAll();
                    return;
                }
            }
            try {
                Http2Connection.this.writerExecutor.execute(new PingRunnable(true, n, n2));
            }
            catch (RejectedExecutionException ex) {}
        }
        
        @Override
        public void priority(final int n, final int n2, final int n3, final boolean b) {
        }
        
        @Override
        public void pushPromise(final int n, final int n2, final List<Header> list) {
            Http2Connection.this.pushRequestLater(n2, list);
        }
        
        @Override
        public void rstStream(final int n, final ErrorCode errorCode) {
            if (Http2Connection.this.pushedStream(n)) {
                Http2Connection.this.pushResetLater(n, errorCode);
                return;
            }
            final Http2Stream removeStream = Http2Connection.this.removeStream(n);
            if (removeStream != null) {
                removeStream.receiveRstStream(errorCode);
            }
        }
        
        @Override
        public void settings(final boolean p0, final Settings p1) {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     1: getfield        okhttp3/internal/http2/Http2Connection$ReaderRunnable.this$0:Lokhttp3/internal/http2/Http2Connection;
            //     4: astore          9
            //     6: aload           9
            //     8: monitorenter   
            //     9: aload_0        
            //    10: getfield        okhttp3/internal/http2/Http2Connection$ReaderRunnable.this$0:Lokhttp3/internal/http2/Http2Connection;
            //    13: getfield        okhttp3/internal/http2/Http2Connection.peerSettings:Lokhttp3/internal/http2/Settings;
            //    16: invokevirtual   okhttp3/internal/http2/Settings.getInitialWindowSize:()I
            //    19: istore_3       
            //    20: iload_1        
            //    21: ifeq            34
            //    24: aload_0        
            //    25: getfield        okhttp3/internal/http2/Http2Connection$ReaderRunnable.this$0:Lokhttp3/internal/http2/Http2Connection;
            //    28: getfield        okhttp3/internal/http2/Http2Connection.peerSettings:Lokhttp3/internal/http2/Settings;
            //    31: invokevirtual   okhttp3/internal/http2/Settings.clear:()V
            //    34: aload_0        
            //    35: getfield        okhttp3/internal/http2/Http2Connection$ReaderRunnable.this$0:Lokhttp3/internal/http2/Http2Connection;
            //    38: getfield        okhttp3/internal/http2/Http2Connection.peerSettings:Lokhttp3/internal/http2/Settings;
            //    41: aload_2        
            //    42: invokevirtual   okhttp3/internal/http2/Settings.merge:(Lokhttp3/internal/http2/Settings;)V
            //    45: aload_0        
            //    46: aload_2        
            //    47: invokespecial   okhttp3/internal/http2/Http2Connection$ReaderRunnable.applyAndAckSettings:(Lokhttp3/internal/http2/Settings;)V
            //    50: aload_0        
            //    51: getfield        okhttp3/internal/http2/Http2Connection$ReaderRunnable.this$0:Lokhttp3/internal/http2/Http2Connection;
            //    54: getfield        okhttp3/internal/http2/Http2Connection.peerSettings:Lokhttp3/internal/http2/Settings;
            //    57: invokevirtual   okhttp3/internal/http2/Settings.getInitialWindowSize:()I
            //    60: istore          4
            //    62: aconst_null    
            //    63: astore_2       
            //    64: iload           4
            //    66: iconst_m1      
            //    67: if_icmpeq       277
            //    70: iload           4
            //    72: iload_3        
            //    73: if_icmpeq       277
            //    76: iload           4
            //    78: iload_3        
            //    79: isub           
            //    80: i2l            
            //    81: lstore          7
            //    83: aload_0        
            //    84: getfield        okhttp3/internal/http2/Http2Connection$ReaderRunnable.this$0:Lokhttp3/internal/http2/Http2Connection;
            //    87: getfield        okhttp3/internal/http2/Http2Connection.receivedInitialPeerSettings:Z
            //    90: ifne            110
            //    93: aload_0        
            //    94: getfield        okhttp3/internal/http2/Http2Connection$ReaderRunnable.this$0:Lokhttp3/internal/http2/Http2Connection;
            //    97: lload           7
            //    99: invokevirtual   okhttp3/internal/http2/Http2Connection.addBytesToWriteWindow:(J)V
            //   102: aload_0        
            //   103: getfield        okhttp3/internal/http2/Http2Connection$ReaderRunnable.this$0:Lokhttp3/internal/http2/Http2Connection;
            //   106: iconst_1       
            //   107: putfield        okhttp3/internal/http2/Http2Connection.receivedInitialPeerSettings:Z
            //   110: lload           7
            //   112: lstore          5
            //   114: aload_0        
            //   115: getfield        okhttp3/internal/http2/Http2Connection$ReaderRunnable.this$0:Lokhttp3/internal/http2/Http2Connection;
            //   118: getfield        okhttp3/internal/http2/Http2Connection.streams:Ljava/util/Map;
            //   121: invokeinterface java/util/Map.isEmpty:()Z
            //   126: ifne            172
            //   129: aload_0        
            //   130: getfield        okhttp3/internal/http2/Http2Connection$ReaderRunnable.this$0:Lokhttp3/internal/http2/Http2Connection;
            //   133: getfield        okhttp3/internal/http2/Http2Connection.streams:Ljava/util/Map;
            //   136: invokeinterface java/util/Map.values:()Ljava/util/Collection;
            //   141: aload_0        
            //   142: getfield        okhttp3/internal/http2/Http2Connection$ReaderRunnable.this$0:Lokhttp3/internal/http2/Http2Connection;
            //   145: getfield        okhttp3/internal/http2/Http2Connection.streams:Ljava/util/Map;
            //   148: invokeinterface java/util/Map.size:()I
            //   153: anewarray       Lokhttp3/internal/http2/Http2Stream;
            //   156: invokeinterface java/util/Collection.toArray:([Ljava/lang/Object;)[Ljava/lang/Object;
            //   161: checkcast       [Lokhttp3/internal/http2/Http2Stream;
            //   164: astore_2       
            //   165: lload           7
            //   167: lstore          5
            //   169: goto            172
            //   172: invokestatic    okhttp3/internal/http2/Http2Connection.access$100:()Ljava/util/concurrent/ExecutorService;
            //   175: astore          10
            //   177: aload_0        
            //   178: getfield        okhttp3/internal/http2/Http2Connection$ReaderRunnable.this$0:Lokhttp3/internal/http2/Http2Connection;
            //   181: getfield        okhttp3/internal/http2/Http2Connection.hostname:Ljava/lang/String;
            //   184: astore          11
            //   186: iconst_0       
            //   187: istore_3       
            //   188: aload           10
            //   190: new             Lokhttp3/internal/http2/Http2Connection$ReaderRunnable$2;
            //   193: dup            
            //   194: aload_0        
            //   195: ldc_w           "OkHttp %s settings"
            //   198: iconst_1       
            //   199: anewarray       Ljava/lang/Object;
            //   202: dup            
            //   203: iconst_0       
            //   204: aload           11
            //   206: aastore        
            //   207: invokespecial   okhttp3/internal/http2/Http2Connection$ReaderRunnable$2.<init>:(Lokhttp3/internal/http2/Http2Connection$ReaderRunnable;Ljava/lang/String;[Ljava/lang/Object;)V
            //   210: invokeinterface java/util/concurrent/ExecutorService.execute:(Ljava/lang/Runnable;)V
            //   215: aload           9
            //   217: monitorexit    
            //   218: aload_2        
            //   219: ifnull          270
            //   222: lload           5
            //   224: lconst_0       
            //   225: lcmp           
            //   226: ifeq            270
            //   229: aload_2        
            //   230: arraylength    
            //   231: istore          4
            //   233: iload_3        
            //   234: iload           4
            //   236: if_icmpge       270
            //   239: aload_2        
            //   240: iload_3        
            //   241: aaload         
            //   242: astore          9
            //   244: aload           9
            //   246: monitorenter   
            //   247: aload           9
            //   249: lload           5
            //   251: invokevirtual   okhttp3/internal/http2/Http2Stream.addBytesToWriteWindow:(J)V
            //   254: aload           9
            //   256: monitorexit    
            //   257: iload_3        
            //   258: iconst_1       
            //   259: iadd           
            //   260: istore_3       
            //   261: goto            233
            //   264: astore_2       
            //   265: aload           9
            //   267: monitorexit    
            //   268: aload_2        
            //   269: athrow         
            //   270: return         
            //   271: astore_2       
            //   272: aload           9
            //   274: monitorexit    
            //   275: aload_2        
            //   276: athrow         
            //   277: lconst_0       
            //   278: lstore          5
            //   280: goto            172
            //    Exceptions:
            //  Try           Handler
            //  Start  End    Start  End    Type
            //  -----  -----  -----  -----  ----
            //  9      20     271    277    Any
            //  24     34     271    277    Any
            //  34     62     271    277    Any
            //  83     110    271    277    Any
            //  114    165    271    277    Any
            //  172    186    271    277    Any
            //  188    218    271    277    Any
            //  247    257    264    270    Any
            //  265    268    264    270    Any
            //  272    275    271    277    Any
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
        
        @Override
        public void windowUpdate(final int n, final long n2) {
            if (n == 0) {
                synchronized (Http2Connection.this) {
                    final Http2Connection this$0 = Http2Connection.this;
                    this$0.bytesLeftInWriteWindow += n2;
                    Http2Connection.this.notifyAll();
                    return;
                }
            }
            final Http2Stream stream = Http2Connection.this.getStream(n);
            if (stream != null) {
                synchronized (stream) {
                    stream.addBytesToWriteWindow(n2);
                }
            }
        }
    }
}
