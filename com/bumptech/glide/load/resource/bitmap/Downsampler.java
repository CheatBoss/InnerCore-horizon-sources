package com.bumptech.glide.load.resource.bitmap;

import java.util.*;
import com.bumptech.glide.load.engine.bitmap_recycle.*;
import com.bumptech.glide.load.*;
import com.bumptech.glide.util.*;
import android.util.*;
import java.io.*;
import android.os.*;
import android.graphics.*;
import android.annotation.*;

public abstract class Downsampler implements BitmapDecoder<InputStream>
{
    public static final Downsampler AT_LEAST;
    public static final Downsampler AT_MOST;
    private static final int MARK_POSITION = 5242880;
    public static final Downsampler NONE;
    private static final Queue<BitmapFactory$Options> OPTIONS_QUEUE;
    private static final String TAG = "Downsampler";
    private static final Set<ImageHeaderParser.ImageType> TYPES_THAT_USE_POOL;
    
    static {
        TYPES_THAT_USE_POOL = EnumSet.of(ImageHeaderParser.ImageType.JPEG, ImageHeaderParser.ImageType.PNG_A, ImageHeaderParser.ImageType.PNG);
        OPTIONS_QUEUE = Util.createQueue(0);
        AT_LEAST = new Downsampler() {
            @Override
            public String getId() {
                return "AT_LEAST.com.bumptech.glide.load.data.bitmap";
            }
            
            @Override
            protected int getSampleSize(final int n, final int n2, final int n3, final int n4) {
                return Math.min(n2 / n4, n / n3);
            }
        };
        AT_MOST = new Downsampler() {
            @Override
            public String getId() {
                return "AT_MOST.com.bumptech.glide.load.data.bitmap";
            }
            
            @Override
            protected int getSampleSize(int n, int n2, int n3, final int n4) {
                n3 = (int)Math.ceil(Math.max(n2 / (float)n4, n / (float)n3));
                n2 = Integer.highestOneBit(n3);
                n = 1;
                n2 = Math.max(1, n2);
                if (n2 >= n3) {
                    n = 0;
                }
                return n2 << n;
            }
        };
        NONE = new Downsampler() {
            @Override
            public String getId() {
                return "NONE.com.bumptech.glide.load.data.bitmap";
            }
            
            @Override
            protected int getSampleSize(final int n, final int n2, final int n3, final int n4) {
                return 0;
            }
        };
    }
    
    private static Bitmap decodeStream(final MarkEnforcingInputStream markEnforcingInputStream, RecyclableBufferedInputStream decodeStream, final BitmapFactory$Options bitmapFactory$Options) {
        if (bitmapFactory$Options.inJustDecodeBounds) {
            markEnforcingInputStream.mark(5242880);
        }
        else {
            decodeStream.fixMarkLimit();
        }
        decodeStream = (RecyclableBufferedInputStream)BitmapFactory.decodeStream((InputStream)markEnforcingInputStream, (Rect)null, bitmapFactory$Options);
        try {
            if (bitmapFactory$Options.inJustDecodeBounds) {
                markEnforcingInputStream.reset();
            }
            return (Bitmap)decodeStream;
        }
        catch (IOException ex) {
            if (Log.isLoggable("Downsampler", 6)) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Exception loading inDecodeBounds=");
                sb.append(bitmapFactory$Options.inJustDecodeBounds);
                sb.append(" sample=");
                sb.append(bitmapFactory$Options.inSampleSize);
                Log.e("Downsampler", sb.toString(), (Throwable)ex);
            }
            return (Bitmap)decodeStream;
        }
    }
    
    private Bitmap downsampleWithSize(final MarkEnforcingInputStream markEnforcingInputStream, final RecyclableBufferedInputStream recyclableBufferedInputStream, final BitmapFactory$Options bitmapFactory$Options, final BitmapPool bitmapPool, final int n, final int n2, final int inSampleSize, final DecodeFormat decodeFormat) {
        final Bitmap$Config config = getConfig(markEnforcingInputStream, decodeFormat);
        bitmapFactory$Options.inSampleSize = inSampleSize;
        bitmapFactory$Options.inPreferredConfig = config;
        if ((bitmapFactory$Options.inSampleSize == 1 || 19 <= Build$VERSION.SDK_INT) && shouldUsePool(markEnforcingInputStream)) {
            setInBitmap(bitmapFactory$Options, bitmapPool.getDirty((int)Math.ceil(n / (double)inSampleSize), (int)Math.ceil(n2 / (double)inSampleSize), config));
        }
        return decodeStream(markEnforcingInputStream, recyclableBufferedInputStream, bitmapFactory$Options);
    }
    
    private static Bitmap$Config getConfig(final InputStream p0, final DecodeFormat p1) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: getstatic       com/bumptech/glide/load/DecodeFormat.ALWAYS_ARGB_8888:Lcom/bumptech/glide/load/DecodeFormat;
        //     4: if_acmpeq       209
        //     7: aload_1        
        //     8: getstatic       com/bumptech/glide/load/DecodeFormat.PREFER_ARGB_8888:Lcom/bumptech/glide/load/DecodeFormat;
        //    11: if_acmpeq       209
        //    14: getstatic       android/os/Build$VERSION.SDK_INT:I
        //    17: bipush          16
        //    19: if_icmpne       25
        //    22: goto            209
        //    25: iconst_0       
        //    26: istore_3       
        //    27: iconst_0       
        //    28: istore          5
        //    30: iconst_0       
        //    31: istore_2       
        //    32: aload_0        
        //    33: sipush          1024
        //    36: invokevirtual   java/io/InputStream.mark:(I)V
        //    39: new             Lcom/bumptech/glide/load/resource/bitmap/ImageHeaderParser;
        //    42: dup            
        //    43: aload_0        
        //    44: invokespecial   com/bumptech/glide/load/resource/bitmap/ImageHeaderParser.<init>:(Ljava/io/InputStream;)V
        //    47: invokevirtual   com/bumptech/glide/load/resource/bitmap/ImageHeaderParser.hasAlpha:()Z
        //    50: istore          4
        //    52: iload           4
        //    54: istore_3       
        //    55: aload_0        
        //    56: invokevirtual   java/io/InputStream.reset:()V
        //    59: iload_3        
        //    60: istore_2       
        //    61: goto            169
        //    64: astore_0       
        //    65: iload_3        
        //    66: istore_2       
        //    67: ldc             "Downsampler"
        //    69: iconst_5       
        //    70: invokestatic    android/util/Log.isLoggable:(Ljava/lang/String;I)Z
        //    73: ifeq            87
        //    76: iload_3        
        //    77: istore_2       
        //    78: ldc             "Downsampler"
        //    80: ldc             "Cannot reset the input stream"
        //    82: aload_0        
        //    83: invokestatic    android/util/Log.w:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //    86: pop            
        //    87: goto            169
        //    90: astore_1       
        //    91: goto            181
        //    94: astore          6
        //    96: ldc             "Downsampler"
        //    98: iconst_5       
        //    99: invokestatic    android/util/Log.isLoggable:(Ljava/lang/String;I)Z
        //   102: ifeq            142
        //   105: new             Ljava/lang/StringBuilder;
        //   108: dup            
        //   109: invokespecial   java/lang/StringBuilder.<init>:()V
        //   112: astore          7
        //   114: aload           7
        //   116: ldc             "Cannot determine whether the image has alpha or not from header for format "
        //   118: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   121: pop            
        //   122: aload           7
        //   124: aload_1        
        //   125: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //   128: pop            
        //   129: ldc             "Downsampler"
        //   131: aload           7
        //   133: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   136: aload           6
        //   138: invokestatic    android/util/Log.w:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //   141: pop            
        //   142: aload_0        
        //   143: invokevirtual   java/io/InputStream.reset:()V
        //   146: iload_2        
        //   147: istore_3       
        //   148: goto            59
        //   151: astore_0       
        //   152: iload           5
        //   154: istore_2       
        //   155: ldc             "Downsampler"
        //   157: iconst_5       
        //   158: invokestatic    android/util/Log.isLoggable:(Ljava/lang/String;I)Z
        //   161: ifeq            87
        //   164: iload_3        
        //   165: istore_2       
        //   166: goto            78
        //   169: iload_2        
        //   170: ifeq            177
        //   173: getstatic       android/graphics/Bitmap$Config.ARGB_8888:Landroid/graphics/Bitmap$Config;
        //   176: areturn        
        //   177: getstatic       android/graphics/Bitmap$Config.RGB_565:Landroid/graphics/Bitmap$Config;
        //   180: areturn        
        //   181: aload_0        
        //   182: invokevirtual   java/io/InputStream.reset:()V
        //   185: goto            207
        //   188: astore_0       
        //   189: ldc             "Downsampler"
        //   191: iconst_5       
        //   192: invokestatic    android/util/Log.isLoggable:(Ljava/lang/String;I)Z
        //   195: ifeq            207
        //   198: ldc             "Downsampler"
        //   200: ldc             "Cannot reset the input stream"
        //   202: aload_0        
        //   203: invokestatic    android/util/Log.w:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //   206: pop            
        //   207: aload_1        
        //   208: athrow         
        //   209: getstatic       android/graphics/Bitmap$Config.ARGB_8888:Landroid/graphics/Bitmap$Config;
        //   212: areturn        
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  39     52     94     169    Ljava/io/IOException;
        //  39     52     90     209    Any
        //  55     59     64     78     Ljava/io/IOException;
        //  96     142    90     209    Any
        //  142    146    151    169    Ljava/io/IOException;
        //  181    185    188    207    Ljava/io/IOException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index: 110, Size: 110
        //     at java.util.ArrayList.rangeCheck(Unknown Source)
        //     at java.util.ArrayList.get(Unknown Source)
        //     at com.strobel.decompiler.ast.AstBuilder.convertToAst(AstBuilder.java:3321)
        //     at com.strobel.decompiler.ast.AstBuilder.convertToAst(AstBuilder.java:3569)
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
    
    @TargetApi(11)
    private static BitmapFactory$Options getDefaultOptions() {
        synchronized (Downsampler.class) {
            Object options_QUEUE = Downsampler.OPTIONS_QUEUE;
            synchronized (options_QUEUE) {
                final BitmapFactory$Options bitmapFactory$Options = Downsampler.OPTIONS_QUEUE.poll();
                // monitorexit(options_QUEUE)
                options_QUEUE = bitmapFactory$Options;
                if (bitmapFactory$Options == null) {
                    options_QUEUE = new BitmapFactory$Options();
                    resetOptions((BitmapFactory$Options)options_QUEUE);
                }
                return (BitmapFactory$Options)options_QUEUE;
            }
        }
    }
    
    private int getRoundedSampleSize(int n, final int n2, final int n3, int n4, int n5) {
        if (n5 == Integer.MIN_VALUE) {
            n5 = n3;
        }
        if (n4 == Integer.MIN_VALUE) {
            n4 = n2;
        }
        if (n != 90 && n != 270) {
            n = this.getSampleSize(n2, n3, n4, n5);
        }
        else {
            n = this.getSampleSize(n3, n2, n4, n5);
        }
        if (n == 0) {
            n = 0;
        }
        else {
            n = Integer.highestOneBit(n);
        }
        return Math.max(1, n);
    }
    
    private static void releaseOptions(final BitmapFactory$Options bitmapFactory$Options) {
        resetOptions(bitmapFactory$Options);
        synchronized (Downsampler.OPTIONS_QUEUE) {
            Downsampler.OPTIONS_QUEUE.offer(bitmapFactory$Options);
        }
    }
    
    @TargetApi(11)
    private static void resetOptions(final BitmapFactory$Options bitmapFactory$Options) {
        bitmapFactory$Options.inTempStorage = null;
        bitmapFactory$Options.inDither = false;
        bitmapFactory$Options.inScaled = false;
        bitmapFactory$Options.inSampleSize = 1;
        bitmapFactory$Options.inPreferredConfig = null;
        bitmapFactory$Options.inJustDecodeBounds = false;
        bitmapFactory$Options.outWidth = 0;
        bitmapFactory$Options.outHeight = 0;
        bitmapFactory$Options.outMimeType = null;
        if (11 <= Build$VERSION.SDK_INT) {
            bitmapFactory$Options.inBitmap = null;
            bitmapFactory$Options.inMutable = true;
        }
    }
    
    @TargetApi(11)
    private static void setInBitmap(final BitmapFactory$Options bitmapFactory$Options, final Bitmap inBitmap) {
        if (11 <= Build$VERSION.SDK_INT) {
            bitmapFactory$Options.inBitmap = inBitmap;
        }
    }
    
    private static boolean shouldUsePool(final InputStream p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     2: getstatic       android/os/Build$VERSION.SDK_INT:I
        //     5: if_icmpgt       10
        //     8: iconst_1       
        //     9: ireturn        
        //    10: aload_0        
        //    11: sipush          1024
        //    14: invokevirtual   java/io/InputStream.mark:(I)V
        //    17: new             Lcom/bumptech/glide/load/resource/bitmap/ImageHeaderParser;
        //    20: dup            
        //    21: aload_0        
        //    22: invokespecial   com/bumptech/glide/load/resource/bitmap/ImageHeaderParser.<init>:(Ljava/io/InputStream;)V
        //    25: invokevirtual   com/bumptech/glide/load/resource/bitmap/ImageHeaderParser.getType:()Lcom/bumptech/glide/load/resource/bitmap/ImageHeaderParser$ImageType;
        //    28: astore_2       
        //    29: getstatic       com/bumptech/glide/load/resource/bitmap/Downsampler.TYPES_THAT_USE_POOL:Ljava/util/Set;
        //    32: aload_2        
        //    33: invokeinterface java/util/Set.contains:(Ljava/lang/Object;)Z
        //    38: istore_1       
        //    39: aload_0        
        //    40: invokevirtual   java/io/InputStream.reset:()V
        //    43: iload_1        
        //    44: ireturn        
        //    45: astore_0       
        //    46: ldc             "Downsampler"
        //    48: iconst_5       
        //    49: invokestatic    android/util/Log.isLoggable:(Ljava/lang/String;I)Z
        //    52: ifeq            64
        //    55: ldc             "Downsampler"
        //    57: ldc             "Cannot reset the input stream"
        //    59: aload_0        
        //    60: invokestatic    android/util/Log.w:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //    63: pop            
        //    64: iload_1        
        //    65: ireturn        
        //    66: astore_2       
        //    67: goto            118
        //    70: astore_2       
        //    71: ldc             "Downsampler"
        //    73: iconst_5       
        //    74: invokestatic    android/util/Log.isLoggable:(Ljava/lang/String;I)Z
        //    77: ifeq            90
        //    80: ldc             "Downsampler"
        //    82: ldc_w           "Cannot determine the image type from header"
        //    85: aload_2        
        //    86: invokestatic    android/util/Log.w:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //    89: pop            
        //    90: aload_0        
        //    91: invokevirtual   java/io/InputStream.reset:()V
        //    94: goto            116
        //    97: astore_0       
        //    98: ldc             "Downsampler"
        //   100: iconst_5       
        //   101: invokestatic    android/util/Log.isLoggable:(Ljava/lang/String;I)Z
        //   104: ifeq            116
        //   107: ldc             "Downsampler"
        //   109: ldc             "Cannot reset the input stream"
        //   111: aload_0        
        //   112: invokestatic    android/util/Log.w:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //   115: pop            
        //   116: iconst_0       
        //   117: ireturn        
        //   118: aload_0        
        //   119: invokevirtual   java/io/InputStream.reset:()V
        //   122: goto            144
        //   125: astore_0       
        //   126: ldc             "Downsampler"
        //   128: iconst_5       
        //   129: invokestatic    android/util/Log.isLoggable:(Ljava/lang/String;I)Z
        //   132: ifeq            144
        //   135: ldc             "Downsampler"
        //   137: ldc             "Cannot reset the input stream"
        //   139: aload_0        
        //   140: invokestatic    android/util/Log.w:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //   143: pop            
        //   144: aload_2        
        //   145: athrow         
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  17     39     70     118    Ljava/io/IOException;
        //  17     39     66     146    Any
        //  39     43     45     66     Ljava/io/IOException;
        //  71     90     66     146    Any
        //  90     94     97     116    Ljava/io/IOException;
        //  118    122    125    144    Ljava/io/IOException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index: 76, Size: 76
        //     at java.util.ArrayList.rangeCheck(Unknown Source)
        //     at java.util.ArrayList.get(Unknown Source)
        //     at com.strobel.decompiler.ast.AstBuilder.convertToAst(AstBuilder.java:3321)
        //     at com.strobel.decompiler.ast.AstBuilder.convertToAst(AstBuilder.java:3569)
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
    
    @Override
    public Bitmap decode(final InputStream p0, final BitmapPool p1, final int p2, final int p3, final DecodeFormat p4) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: astore          12
        //     5: aload           12
        //     7: invokevirtual   com/bumptech/glide/util/ByteArrayPool.getBytes:()[B
        //    10: astore          13
        //    12: aload           12
        //    14: invokevirtual   com/bumptech/glide/util/ByteArrayPool.getBytes:()[B
        //    17: astore          14
        //    19: invokestatic    com/bumptech/glide/load/resource/bitmap/Downsampler.getDefaultOptions:()Landroid/graphics/BitmapFactory$Options;
        //    22: astore          15
        //    24: new             Lcom/bumptech/glide/load/resource/bitmap/RecyclableBufferedInputStream;
        //    27: dup            
        //    28: aload_1        
        //    29: aload           14
        //    31: invokespecial   com/bumptech/glide/load/resource/bitmap/RecyclableBufferedInputStream.<init>:(Ljava/io/InputStream;[B)V
        //    34: astore_1       
        //    35: aload_1        
        //    36: invokestatic    com/bumptech/glide/util/ExceptionCatchingInputStream.obtain:(Ljava/io/InputStream;)Lcom/bumptech/glide/util/ExceptionCatchingInputStream;
        //    39: astore          10
        //    41: new             Lcom/bumptech/glide/util/MarkEnforcingInputStream;
        //    44: dup            
        //    45: aload           10
        //    47: invokespecial   com/bumptech/glide/util/MarkEnforcingInputStream.<init>:(Ljava/io/InputStream;)V
        //    50: astore          16
        //    52: aload           10
        //    54: ldc             5242880
        //    56: invokevirtual   com/bumptech/glide/util/ExceptionCatchingInputStream.mark:(I)V
        //    59: iconst_0       
        //    60: istore          7
        //    62: iconst_0       
        //    63: istore          6
        //    65: new             Lcom/bumptech/glide/load/resource/bitmap/ImageHeaderParser;
        //    68: dup            
        //    69: aload           10
        //    71: invokespecial   com/bumptech/glide/load/resource/bitmap/ImageHeaderParser.<init>:(Ljava/io/InputStream;)V
        //    74: invokevirtual   com/bumptech/glide/load/resource/bitmap/ImageHeaderParser.getOrientation:()I
        //    77: istore          8
        //    79: iload           8
        //    81: istore          7
        //    83: aload           10
        //    85: invokevirtual   com/bumptech/glide/util/ExceptionCatchingInputStream.reset:()V
        //    88: iload           7
        //    90: istore          6
        //    92: goto            128
        //    95: astore_1       
        //    96: goto            417
        //    99: astore          11
        //   101: iload           7
        //   103: istore          6
        //   105: ldc             "Downsampler"
        //   107: iconst_5       
        //   108: invokestatic    android/util/Log.isLoggable:(Ljava/lang/String;I)Z
        //   111: ifeq            128
        //   114: ldc             "Downsampler"
        //   116: ldc             "Cannot reset the input stream"
        //   118: aload           11
        //   120: invokestatic    android/util/Log.w:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //   123: pop            
        //   124: iload           7
        //   126: istore          6
        //   128: goto            209
        //   131: astore_1       
        //   132: goto            383
        //   135: astore          11
        //   137: ldc             "Downsampler"
        //   139: iconst_5       
        //   140: invokestatic    android/util/Log.isLoggable:(Ljava/lang/String;I)Z
        //   143: istore          9
        //   145: iload           9
        //   147: ifeq            161
        //   150: ldc             "Downsampler"
        //   152: ldc_w           "Cannot determine the image orientation from header"
        //   155: aload           11
        //   157: invokestatic    android/util/Log.w:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //   160: pop            
        //   161: aload           10
        //   163: invokevirtual   com/bumptech/glide/util/ExceptionCatchingInputStream.reset:()V
        //   166: iload           6
        //   168: istore          7
        //   170: goto            88
        //   173: astore          11
        //   175: ldc             "Downsampler"
        //   177: iconst_5       
        //   178: invokestatic    android/util/Log.isLoggable:(Ljava/lang/String;I)Z
        //   181: istore          9
        //   183: iload           7
        //   185: istore          6
        //   187: iload           9
        //   189: ifeq            128
        //   192: ldc             "Downsampler"
        //   194: ldc             "Cannot reset the input stream"
        //   196: aload           11
        //   198: invokestatic    android/util/Log.w:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //   201: pop            
        //   202: iload           7
        //   204: istore          6
        //   206: goto            128
        //   209: aload           15
        //   211: aload           13
        //   213: putfield        android/graphics/BitmapFactory$Options.inTempStorage:[B
        //   216: aload_0        
        //   217: aload           16
        //   219: aload_1        
        //   220: aload           15
        //   222: invokevirtual   com/bumptech/glide/load/resource/bitmap/Downsampler.getDimensions:(Lcom/bumptech/glide/util/MarkEnforcingInputStream;Lcom/bumptech/glide/load/resource/bitmap/RecyclableBufferedInputStream;Landroid/graphics/BitmapFactory$Options;)[I
        //   225: astore          11
        //   227: aload           11
        //   229: iconst_0       
        //   230: iaload         
        //   231: istore          7
        //   233: aload           11
        //   235: iconst_1       
        //   236: iaload         
        //   237: istore          8
        //   239: aload_0        
        //   240: iload           6
        //   242: invokestatic    com/bumptech/glide/load/resource/bitmap/TransformationUtils.getExifOrientationDegrees:(I)I
        //   245: iload           7
        //   247: iload           8
        //   249: iload_3        
        //   250: iload           4
        //   252: invokespecial   com/bumptech/glide/load/resource/bitmap/Downsampler.getRoundedSampleSize:(IIIII)I
        //   255: istore_3       
        //   256: aload           10
        //   258: astore          11
        //   260: aload_0        
        //   261: aload           16
        //   263: aload_1        
        //   264: aload           15
        //   266: aload_2        
        //   267: iload           7
        //   269: iload           8
        //   271: iload_3        
        //   272: aload           5
        //   274: invokespecial   com/bumptech/glide/load/resource/bitmap/Downsampler.downsampleWithSize:(Lcom/bumptech/glide/util/MarkEnforcingInputStream;Lcom/bumptech/glide/load/resource/bitmap/RecyclableBufferedInputStream;Landroid/graphics/BitmapFactory$Options;Lcom/bumptech/glide/load/engine/bitmap_recycle/BitmapPool;IIILcom/bumptech/glide/load/DecodeFormat;)Landroid/graphics/Bitmap;
        //   277: astore          16
        //   279: aload           11
        //   281: invokevirtual   com/bumptech/glide/util/ExceptionCatchingInputStream.getException:()Ljava/io/IOException;
        //   284: astore_1       
        //   285: aload_1        
        //   286: ifnull          298
        //   289: new             Ljava/lang/RuntimeException;
        //   292: dup            
        //   293: aload_1        
        //   294: invokespecial   java/lang/RuntimeException.<init>:(Ljava/lang/Throwable;)V
        //   297: athrow         
        //   298: aconst_null    
        //   299: astore_1       
        //   300: aload           16
        //   302: ifnull          350
        //   305: aload           16
        //   307: aload_2        
        //   308: iload           6
        //   310: invokestatic    com/bumptech/glide/load/resource/bitmap/TransformationUtils.rotateImageExif:(Landroid/graphics/Bitmap;Lcom/bumptech/glide/load/engine/bitmap_recycle/BitmapPool;I)Landroid/graphics/Bitmap;
        //   313: astore          5
        //   315: aload           5
        //   317: astore_1       
        //   318: aload           16
        //   320: aload           5
        //   322: invokevirtual   java/lang/Object.equals:(Ljava/lang/Object;)Z
        //   325: ifne            350
        //   328: aload           5
        //   330: astore_1       
        //   331: aload_2        
        //   332: aload           16
        //   334: invokeinterface com/bumptech/glide/load/engine/bitmap_recycle/BitmapPool.put:(Landroid/graphics/Bitmap;)Z
        //   339: ifne            350
        //   342: aload           16
        //   344: invokevirtual   android/graphics/Bitmap.recycle:()V
        //   347: aload           5
        //   349: astore_1       
        //   350: aload           12
        //   352: aload           13
        //   354: invokevirtual   com/bumptech/glide/util/ByteArrayPool.releaseBytes:([B)Z
        //   357: pop            
        //   358: aload           12
        //   360: aload           14
        //   362: invokevirtual   com/bumptech/glide/util/ByteArrayPool.releaseBytes:([B)Z
        //   365: pop            
        //   366: aload           11
        //   368: invokevirtual   com/bumptech/glide/util/ExceptionCatchingInputStream.release:()V
        //   371: aload           15
        //   373: invokestatic    com/bumptech/glide/load/resource/bitmap/Downsampler.releaseOptions:(Landroid/graphics/BitmapFactory$Options;)V
        //   376: aload_1        
        //   377: areturn        
        //   378: astore_1       
        //   379: goto            417
        //   382: astore_1       
        //   383: aload           10
        //   385: invokevirtual   com/bumptech/glide/util/ExceptionCatchingInputStream.reset:()V
        //   388: goto            414
        //   391: astore_1       
        //   392: goto            417
        //   395: astore_2       
        //   396: ldc             "Downsampler"
        //   398: iconst_5       
        //   399: invokestatic    android/util/Log.isLoggable:(Ljava/lang/String;I)Z
        //   402: ifeq            414
        //   405: ldc             "Downsampler"
        //   407: ldc             "Cannot reset the input stream"
        //   409: aload_2        
        //   410: invokestatic    android/util/Log.w:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //   413: pop            
        //   414: aload_1        
        //   415: athrow         
        //   416: astore_1       
        //   417: aload           12
        //   419: aload           13
        //   421: invokevirtual   com/bumptech/glide/util/ByteArrayPool.releaseBytes:([B)Z
        //   424: pop            
        //   425: aload           12
        //   427: aload           14
        //   429: invokevirtual   com/bumptech/glide/util/ByteArrayPool.releaseBytes:([B)Z
        //   432: pop            
        //   433: aload           10
        //   435: invokevirtual   com/bumptech/glide/util/ExceptionCatchingInputStream.release:()V
        //   438: aload           15
        //   440: invokestatic    com/bumptech/glide/load/resource/bitmap/Downsampler.releaseOptions:(Landroid/graphics/BitmapFactory$Options;)V
        //   443: aload_1        
        //   444: athrow         
        //   445: astore_1       
        //   446: goto            417
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  52     59     416    417    Any
        //  65     79     135    209    Ljava/io/IOException;
        //  65     79     131    135    Any
        //  83     88     99     128    Ljava/io/IOException;
        //  83     88     95     99     Any
        //  105    124    95     99     Any
        //  137    145    382    383    Any
        //  150    161    131    135    Any
        //  161    166    173    209    Ljava/io/IOException;
        //  161    166    95     99     Any
        //  175    183    416    417    Any
        //  192    202    95     99     Any
        //  209    227    416    417    Any
        //  239    256    416    417    Any
        //  260    285    378    382    Any
        //  289    298    445    449    Any
        //  305    315    445    449    Any
        //  318    328    445    449    Any
        //  331    347    445    449    Any
        //  383    388    395    414    Ljava/io/IOException;
        //  383    388    391    395    Any
        //  396    414    391    395    Any
        //  414    416    391    395    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0161:
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
    
    public int[] getDimensions(final MarkEnforcingInputStream markEnforcingInputStream, final RecyclableBufferedInputStream recyclableBufferedInputStream, final BitmapFactory$Options bitmapFactory$Options) {
        bitmapFactory$Options.inJustDecodeBounds = true;
        decodeStream(markEnforcingInputStream, recyclableBufferedInputStream, bitmapFactory$Options);
        bitmapFactory$Options.inJustDecodeBounds = false;
        return new int[] { bitmapFactory$Options.outWidth, bitmapFactory$Options.outHeight };
    }
    
    protected abstract int getSampleSize(final int p0, final int p1, final int p2, final int p3);
}
