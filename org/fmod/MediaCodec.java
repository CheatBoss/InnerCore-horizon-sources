package org.fmod;

import java.nio.*;
import android.os.*;
import android.util.*;
import android.view.*;
import java.io.*;
import java.lang.reflect.*;
import android.media.*;

public class MediaCodec
{
    private int mChannelCount;
    private long mCodecPtr;
    private int mCurrentOutputBufferIndex;
    private Object mDataSourceProxy;
    private android.media.MediaCodec mDecoder;
    private MediaExtractor mExtractor;
    private ByteBuffer[] mInputBuffers;
    private boolean mInputFinished;
    private long mLength;
    private ByteBuffer[] mOutputBuffers;
    private boolean mOutputFinished;
    private int mSampleRate;
    
    public MediaCodec() {
        this.mCodecPtr = 0L;
        this.mLength = 0L;
        this.mSampleRate = 0;
        this.mChannelCount = 0;
        this.mInputFinished = false;
        this.mOutputFinished = false;
        this.mDecoder = null;
        this.mDataSourceProxy = null;
        this.mExtractor = null;
        this.mInputBuffers = null;
        this.mOutputBuffers = null;
        this.mCurrentOutputBufferIndex = -1;
    }
    
    private static native long fmodGetSize(final long p0);
    
    private static native int fmodReadAt(final long p0, final long p1, final byte[] p2, final int p3, final int p4);
    
    public int getChannelCount() {
        return this.mChannelCount;
    }
    
    public long getLength() {
        return this.mLength;
    }
    
    public int getSampleRate() {
        return this.mSampleRate;
    }
    
    public boolean init(long long1) {
        this.mCodecPtr = long1;
        final int sdk_INT = Build$VERSION.SDK_INT;
        int integer = 0;
        if (sdk_INT < 17) {
            Log.w("fmod", "MediaCodec::init : MediaCodec unavailable, ensure device is running at least 4.2 (JellyBean).\n");
            return false;
        }
        Label_0338: {
            if (Build$VERSION.SDK_INT < 23) {
                try {
                    final Class<?> forName = Class.forName("android.media.DataSource");
                    final Method method = Class.forName("android.media.MediaExtractor").getMethod("setDataSource", forName);
                    this.mExtractor = new MediaExtractor();
                    final Object proxyInstance = Proxy.newProxyInstance(forName.getClassLoader(), new Class[] { forName }, new MediaCodec.DataSource());
                    this.mDataSourceProxy = proxyInstance;
                    method.invoke(this.mExtractor, proxyInstance);
                    break Label_0338;
                }
                catch (InvocationTargetException ex) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("MediaCodec::init : ");
                    sb.append(ex.toString());
                    Log.e("fmod", sb.toString());
                    return false;
                }
                catch (IllegalAccessException ex2) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("MediaCodec::init : ");
                    sb2.append(ex2.toString());
                    Log.e("fmod", sb2.toString());
                    return false;
                }
                catch (NoSuchMethodException ex3) {
                    final StringBuilder sb3 = new StringBuilder();
                    sb3.append("MediaCodec::init : ");
                    sb3.append(ex3.toString());
                    Log.w("fmod", sb3.toString());
                    return false;
                }
                catch (ClassNotFoundException ex4) {
                    final StringBuilder sb4 = new StringBuilder();
                    sb4.append("MediaCodec::init : ");
                    sb4.append(ex4.toString());
                    Log.w("fmod", sb4.toString());
                    return false;
                }
            }
            try {
                (this.mExtractor = new MediaExtractor()).setDataSource((MediaDataSource)new MediaCodec.DataSource());
                for (int trackCount = this.mExtractor.getTrackCount(), i = 0; i < trackCount; ++i) {
                    final MediaFormat trackFormat = this.mExtractor.getTrackFormat(i);
                    final String string = trackFormat.getString("mime");
                    final StringBuilder sb5 = new StringBuilder();
                    sb5.append("MediaCodec::init : Format ");
                    sb5.append(i);
                    sb5.append(" / ");
                    sb5.append(trackCount);
                    sb5.append(" -- ");
                    sb5.append(trackFormat);
                    Log.d("fmod", sb5.toString());
                    if (string.equals("audio/mp4a-latm")) {
                        try {
                            this.mDecoder = android.media.MediaCodec.createDecoderByType(string);
                            this.mExtractor.selectTrack(i);
                            this.mDecoder.configure(trackFormat, (Surface)null, (MediaCrypto)null, 0);
                            this.mDecoder.start();
                            this.mInputBuffers = this.mDecoder.getInputBuffers();
                            this.mOutputBuffers = this.mDecoder.getOutputBuffers();
                            int integer2;
                            if (trackFormat.containsKey("encoder-delay")) {
                                integer2 = trackFormat.getInteger("encoder-delay");
                            }
                            else {
                                integer2 = 0;
                            }
                            if (trackFormat.containsKey("encoder-padding")) {
                                integer = trackFormat.getInteger("encoder-padding");
                            }
                            long1 = trackFormat.getLong("durationUs");
                            this.mChannelCount = trackFormat.getInteger("channel-count");
                            final int integer3 = trackFormat.getInteger("sample-rate");
                            this.mSampleRate = integer3;
                            this.mLength = (int)((long1 * integer3 + 999999L) / 1000000L) - integer2 - integer;
                            return true;
                        }
                        catch (IOException ex5) {
                            final StringBuilder sb6 = new StringBuilder();
                            sb6.append("MediaCodec::init : ");
                            sb6.append(ex5.toString());
                            Log.e("fmod", sb6.toString());
                            return false;
                        }
                    }
                }
                return false;
            }
            catch (IOException ex6) {
                final StringBuilder sb7 = new StringBuilder();
                sb7.append("MediaCodec::init : ");
                sb7.append(ex6.toString());
                Log.w("fmod", sb7.toString());
                return false;
            }
        }
    }
    
    public int read(final byte[] array, final int n) {
        int min;
        if (this.mInputFinished && this.mOutputFinished && this.mCurrentOutputBufferIndex == -1) {
            min = -1;
        }
        else {
            min = 0;
        }
        while (!this.mInputFinished) {
            final int dequeueInputBuffer = this.mDecoder.dequeueInputBuffer(0L);
            if (dequeueInputBuffer < 0) {
                break;
            }
            final int sampleData = this.mExtractor.readSampleData(this.mInputBuffers[dequeueInputBuffer], 0);
            if (sampleData >= 0) {
                this.mDecoder.queueInputBuffer(dequeueInputBuffer, 0, sampleData, this.mExtractor.getSampleTime(), 0);
                this.mExtractor.advance();
            }
            else {
                this.mDecoder.queueInputBuffer(dequeueInputBuffer, 0, 0, 0L, 4);
                this.mInputFinished = true;
            }
        }
        if (!this.mOutputFinished && this.mCurrentOutputBufferIndex == -1) {
            final MediaCodec$BufferInfo mediaCodec$BufferInfo = new MediaCodec$BufferInfo();
            final int dequeueOutputBuffer = this.mDecoder.dequeueOutputBuffer(mediaCodec$BufferInfo, 10000L);
            if (dequeueOutputBuffer >= 0) {
                this.mCurrentOutputBufferIndex = dequeueOutputBuffer;
                this.mOutputBuffers[dequeueOutputBuffer].limit(mediaCodec$BufferInfo.size);
                this.mOutputBuffers[dequeueOutputBuffer].position(mediaCodec$BufferInfo.offset);
            }
            else if (dequeueOutputBuffer == -3) {
                this.mOutputBuffers = this.mDecoder.getOutputBuffers();
            }
            else if (dequeueOutputBuffer == -2) {
                final StringBuilder sb = new StringBuilder();
                sb.append("MediaCodec::read : MediaCodec::dequeueOutputBuffer returned MediaCodec.INFO_OUTPUT_FORMAT_CHANGED ");
                sb.append(this.mDecoder.getOutputFormat());
                Log.d("fmod", sb.toString());
            }
            else if (dequeueOutputBuffer == -1) {
                Log.d("fmod", "MediaCodec::read : MediaCodec::dequeueOutputBuffer returned MediaCodec.INFO_TRY_AGAIN_LATER.");
            }
            else {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("MediaCodec::read : MediaCodec::dequeueOutputBuffer returned ");
                sb2.append(dequeueOutputBuffer);
                Log.w("fmod", sb2.toString());
            }
            if ((mediaCodec$BufferInfo.flags & 0x4) != 0x0) {
                this.mOutputFinished = true;
            }
        }
        final int mCurrentOutputBufferIndex = this.mCurrentOutputBufferIndex;
        if (mCurrentOutputBufferIndex != -1) {
            final ByteBuffer byteBuffer = this.mOutputBuffers[mCurrentOutputBufferIndex];
            min = Math.min(byteBuffer.remaining(), n);
            byteBuffer.get(array, 0, min);
            if (!byteBuffer.hasRemaining()) {
                byteBuffer.clear();
                this.mDecoder.releaseOutputBuffer(this.mCurrentOutputBufferIndex, false);
                this.mCurrentOutputBufferIndex = -1;
            }
        }
        return min;
    }
    
    public void release() {
        final android.media.MediaCodec mDecoder = this.mDecoder;
        if (mDecoder != null) {
            mDecoder.stop();
            this.mDecoder.release();
            this.mDecoder = null;
        }
        final MediaExtractor mExtractor = this.mExtractor;
        if (mExtractor != null) {
            mExtractor.release();
            this.mExtractor = null;
        }
    }
    
    public void seek(int i) {
        final int mCurrentOutputBufferIndex = this.mCurrentOutputBufferIndex;
        if (mCurrentOutputBufferIndex != -1) {
            this.mOutputBuffers[mCurrentOutputBufferIndex].clear();
            this.mCurrentOutputBufferIndex = -1;
        }
        this.mInputFinished = false;
        this.mOutputFinished = false;
        this.mDecoder.flush();
        final long n = i;
        this.mExtractor.seekTo(n * 1000000L / this.mSampleRate, 0);
        final long n2 = (this.mExtractor.getSampleTime() * this.mSampleRate + 999999L) / 1000000L;
        final int n3 = (int)((n - n2) * this.mChannelCount * 2L);
        if (n3 < 0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("MediaCodec::seek : Seek to ");
            sb.append(i);
            sb.append(" resulted in position ");
            sb.append(n2);
            Log.w("fmod", sb.toString());
            return;
        }
        byte[] array;
        for (array = new byte[1024], i = n3; i > 0; i -= this.read(array, Math.min(1024, i))) {}
    }
    
    class DataSource implements InvocationHandler
    {
        @Override
        public Object invoke(final Object o, final Method method, final Object[] array) {
            if (method.getName().equals("readAt")) {
                return fmodReadAt(MediaCodec.this.mCodecPtr, (long)array[0], (byte[])array[1], 0, (int)array[2]);
            }
            if (method.getName().equals("getSize")) {
                return fmodGetSize(MediaCodec.this.mCodecPtr);
            }
            if (method.getName().equals("close")) {
                return null;
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("MediaCodec::DataSource::invoke : Unrecognised method found: ");
            sb.append(method.getName());
            Log.w("fmod", sb.toString());
            return null;
        }
    }
    
    class DataSource extends MediaDataSource
    {
        public void close() {
        }
        
        public long getSize() {
            return fmodGetSize(MediaCodec.this.mCodecPtr);
        }
        
        public int readAt(final long n, final byte[] array, final int n2, final int n3) {
            return fmodReadAt(MediaCodec.this.mCodecPtr, n, array, n2, n3);
        }
    }
}
