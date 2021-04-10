package com.bumptech.glide.load.resource.gif;

import com.bumptech.glide.load.engine.bitmap_recycle.*;
import android.content.*;
import com.bumptech.glide.*;
import com.bumptech.glide.load.resource.*;
import com.bumptech.glide.load.*;
import android.graphics.*;
import com.bumptech.glide.gifdecoder.*;
import android.util.*;
import java.io.*;
import com.bumptech.glide.load.engine.*;
import java.util.*;
import com.bumptech.glide.util.*;

public class GifResourceDecoder implements ResourceDecoder<InputStream, GifDrawable>
{
    private static final GifDecoderPool DECODER_POOL;
    private static final GifHeaderParserPool PARSER_POOL;
    private static final String TAG = "GifResourceDecoder";
    private final BitmapPool bitmapPool;
    private final Context context;
    private final GifDecoderPool decoderPool;
    private final GifHeaderParserPool parserPool;
    private final GifBitmapProvider provider;
    
    static {
        PARSER_POOL = new GifHeaderParserPool();
        DECODER_POOL = new GifDecoderPool();
    }
    
    public GifResourceDecoder(final Context context) {
        this(context, Glide.get(context).getBitmapPool());
    }
    
    public GifResourceDecoder(final Context context, final BitmapPool bitmapPool) {
        this(context, bitmapPool, GifResourceDecoder.PARSER_POOL, GifResourceDecoder.DECODER_POOL);
    }
    
    GifResourceDecoder(final Context context, final BitmapPool bitmapPool, final GifHeaderParserPool parserPool, final GifDecoderPool decoderPool) {
        this.context = context;
        this.bitmapPool = bitmapPool;
        this.decoderPool = decoderPool;
        this.provider = new GifBitmapProvider(bitmapPool);
        this.parserPool = parserPool;
    }
    
    private GifDrawableResource decode(final byte[] array, final int n, final int n2, final GifHeaderParser gifHeaderParser, final GifDecoder gifDecoder) {
        final GifHeader header = gifHeaderParser.parseHeader();
        if (header.getNumFrames() <= 0 || header.getStatus() != 0) {
            return null;
        }
        final Bitmap decodeFirstFrame = this.decodeFirstFrame(gifDecoder, header, array);
        if (decodeFirstFrame == null) {
            return null;
        }
        return new GifDrawableResource(new GifDrawable(this.context, this.provider, this.bitmapPool, (Transformation<Bitmap>)UnitTransformation.get(), n, n2, header, array, decodeFirstFrame));
    }
    
    private Bitmap decodeFirstFrame(final GifDecoder gifDecoder, final GifHeader gifHeader, final byte[] array) {
        gifDecoder.setData(gifHeader, array);
        gifDecoder.advance();
        return gifDecoder.getNextFrame();
    }
    
    private static byte[] inputStreamToBytes(final InputStream inputStream) {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(16384);
        try {
            final byte[] array = new byte[16384];
            while (true) {
                final int read = inputStream.read(array);
                if (read == -1) {
                    break;
                }
                byteArrayOutputStream.write(array, 0, read);
            }
            byteArrayOutputStream.flush();
        }
        catch (IOException ex) {
            Log.w("GifResourceDecoder", "Error reading data from stream", (Throwable)ex);
        }
        return byteArrayOutputStream.toByteArray();
    }
    
    @Override
    public GifDrawableResource decode(InputStream obtain, final int n, final int n2) {
        final byte[] inputStreamToBytes = inputStreamToBytes(obtain);
        obtain = (InputStream)this.parserPool.obtain(inputStreamToBytes);
        final GifDecoder obtain2 = this.decoderPool.obtain(this.provider);
        try {
            return this.decode(inputStreamToBytes, n, n2, (GifHeaderParser)obtain, obtain2);
        }
        finally {
            this.parserPool.release((GifHeaderParser)obtain);
            this.decoderPool.release(obtain2);
        }
    }
    
    @Override
    public String getId() {
        return "";
    }
    
    static class GifDecoderPool
    {
        private final Queue<GifDecoder> pool;
        
        GifDecoderPool() {
            this.pool = Util.createQueue(0);
        }
        
        public GifDecoder obtain(final GifDecoder.BitmapProvider bitmapProvider) {
            synchronized (this) {
                GifDecoder gifDecoder;
                if ((gifDecoder = this.pool.poll()) == null) {
                    gifDecoder = new GifDecoder(bitmapProvider);
                }
                return gifDecoder;
            }
        }
        
        public void release(final GifDecoder gifDecoder) {
            synchronized (this) {
                gifDecoder.clear();
                this.pool.offer(gifDecoder);
            }
        }
    }
    
    static class GifHeaderParserPool
    {
        private final Queue<GifHeaderParser> pool;
        
        GifHeaderParserPool() {
            this.pool = Util.createQueue(0);
        }
        
        public GifHeaderParser obtain(final byte[] data) {
            synchronized (this) {
                GifHeaderParser gifHeaderParser;
                if ((gifHeaderParser = this.pool.poll()) == null) {
                    gifHeaderParser = new GifHeaderParser();
                }
                return gifHeaderParser.setData(data);
            }
        }
        
        public void release(final GifHeaderParser gifHeaderParser) {
            synchronized (this) {
                gifHeaderParser.clear();
                this.pool.offer(gifHeaderParser);
            }
        }
    }
}
