package com.microsoft.xbox.toolkit.ui;

import android.widget.*;
import java.util.*;
import android.animation.*;
import java.net.*;
import com.microsoft.xboxtcui.*;
import android.graphics.*;
import com.microsoft.xbox.toolkit.*;
import java.io.*;
import org.apache.http.client.methods.*;
import com.microsoft.xbox.toolkit.network.*;

public class TextureManager
{
    private static final int ANIM_TIME = 100;
    private static final int BITMAP_CACHE_MAX_FILE_SIZE_IN_BYTES = 5242880;
    private static final String BMP_FILE_CACHE_DIR_NAME = "texture";
    private static final int BMP_FILE_CACHE_SIZE = 2000;
    private static final int DECODE_THREAD_WAIT_TIMEOUT_MS = 3000;
    private static final int TEXTURE_TIMEOUT_MS = 15000;
    private static final long TIME_TO_RETRY_MS = 300000L;
    public static TextureManager instance;
    private XLEMemoryCache<TextureManagerScaledNetworkBitmapRequest, XLEBitmap> bitmapCache;
    private XLEFileCache bitmapFileCache;
    private Thread decodeThread;
    private HashSet<TextureManagerScaledNetworkBitmapRequest> inProgress;
    private Object listLock;
    private HashMap<TextureManagerScaledResourceBitmapRequest, XLEBitmap> resourceBitmapCache;
    private TimeMonitor stopwatch;
    private HashMap<TextureManagerScaledNetworkBitmapRequest, RetryEntry> timeToRetryCache;
    private ThreadSafePriorityQueue<TextureManagerDownloadRequest> toDecode;
    private MultiMap<TextureManagerScaledNetworkBitmapRequest, ImageView> waitingForImage;
    
    static {
        TextureManager.instance = new TextureManager();
    }
    
    public TextureManager() {
        this.listLock = new Object();
        this.inProgress = new HashSet<TextureManagerScaledNetworkBitmapRequest>();
        this.waitingForImage = new MultiMap<TextureManagerScaledNetworkBitmapRequest, ImageView>();
        this.bitmapFileCache = XLEFileCacheManager.createCache("texture", 2000);
        this.timeToRetryCache = new HashMap<TextureManagerScaledNetworkBitmapRequest, RetryEntry>();
        this.resourceBitmapCache = new HashMap<TextureManagerScaledResourceBitmapRequest, XLEBitmap>();
        this.toDecode = new ThreadSafePriorityQueue<TextureManagerDownloadRequest>();
        this.decodeThread = null;
        this.stopwatch = new TimeMonitor();
        this.bitmapCache = new XLEMemoryCache<TextureManagerScaledNetworkBitmapRequest, XLEBitmap>(Math.min(this.getNetworkBitmapCacheSizeInMB(), 50) * 1048576, 5242880);
        this.stopwatch.start();
        (this.decodeThread = new XLEThread(new TextureManagerDecodeThread(), "XLETextureDecodeThread")).setDaemon(true);
        this.decodeThread.setPriority(4);
        this.decodeThread.start();
    }
    
    public static TextureManager Instance() {
        return TextureManager.instance;
    }
    
    private void bindToViewInternal(final String s, final ImageView imageView, final TextureBindingOption textureBindingOption) {
        TextureManagerScaledNetworkBitmapRequest textureManagerScaledNetworkBitmapRequest;
        boolean invalidUrl;
        boolean b;
        Object o = null;
        Object o2;
        final String test_loadingOrLoadedImageUrl;
        int n;
        Block_11_Outer:Label_0157_Outer:
        while (true) {
            textureManagerScaledNetworkBitmapRequest = new TextureManagerScaledNetworkBitmapRequest(s, textureBindingOption);
            while (true) {
                Label_0288: {
                    Label_0285: {
                        Label_0275: {
                            synchronized (this.listLock) {
                                if (this.waitingForImage.containsValue(imageView)) {
                                    this.waitingForImage.removeValue(imageView);
                                }
                                invalidUrl = invalidUrl(s);
                                b = false;
                                if (!invalidUrl) {
                                    o = this.bitmapCache.get(textureManagerScaledNetworkBitmapRequest);
                                    if ((o2 = o) != null) {
                                        break Label_0288;
                                    }
                                    o2 = this.timeToRetryCache.get(textureManagerScaledNetworkBitmapRequest);
                                    if (o2 == null) {
                                        break Label_0275;
                                    }
                                    if (((RetryEntry)o2).isExpired()) {
                                        break Label_0275;
                                    }
                                    o2 = o;
                                    if (textureBindingOption.resourceIdForError != -1) {
                                        o2 = this.loadResource(textureBindingOption.resourceIdForError);
                                    }
                                    break Label_0288;
                                }
                                else {
                                    if (textureBindingOption.resourceIdForError != -1) {
                                        o2 = this.loadResource(textureBindingOption.resourceIdForError);
                                        XLEAssert.assertNotNull(o2);
                                        break Label_0288;
                                    }
                                    break Label_0285;
                                }
                                // monitorexit(this.listLock)
                                // iftrue(Label_0268:, !imageView instanceof XLEImageView)
                                // iftrue(Label_0192:, o2 == null)
                                // iftrue(Label_0243:, n == 0)
                                while (true) {
                                    Block_10: {
                                        while (true) {
                                        Label_0197:
                                            while (true) {
                                                this.setImage(imageView, (XLEBitmap)o);
                                                while (true) {
                                                    Block_12: {
                                                        Block_14: {
                                                            break Block_14;
                                                            o2 = this.loadResource(textureBindingOption.resourceIdForLoading);
                                                            break Block_12;
                                                        }
                                                        ((XLEImageView)imageView).TEST_loadingOrLoadedImageUrl = test_loadingOrLoadedImageUrl;
                                                        Label_0268: {
                                                            return;
                                                        }
                                                        XLEAssert.assertTrue(b);
                                                        break Label_0197;
                                                    }
                                                    b = true;
                                                    continue Label_0157_Outer;
                                                }
                                                o = o2;
                                                break Block_10;
                                                this.inProgress.add(textureManagerScaledNetworkBitmapRequest);
                                                this.load(textureManagerScaledNetworkBitmapRequest);
                                                o = o2;
                                                continue Block_11_Outer;
                                            }
                                            this.waitingForImage.put(textureManagerScaledNetworkBitmapRequest, imageView);
                                            o = o2;
                                            continue;
                                        }
                                    }
                                    continue Label_0157_Outer;
                                }
                            }
                            // iftrue(Label_0243:, this.inProgress.contains((Object)textureManagerScaledNetworkBitmapRequest))
                            // iftrue(Label_0197:, textureBindingOption.resourceIdForLoading == -1)
                        }
                        n = 1;
                        o2 = o;
                        continue;
                    }
                    o2 = null;
                }
                n = 0;
                continue;
            }
        }
    }
    
    private BitmapFactory$Options computeInSampleSizeOptions(int inSampleSize, final int n, final BitmapFactory$Options bitmapFactory$Options) {
        final BitmapFactory$Options bitmapFactory$Options2 = new BitmapFactory$Options();
        final boolean validResizeDimention = validResizeDimention(inSampleSize, n);
        boolean b = true;
        if (validResizeDimention && bitmapFactory$Options.outWidth > inSampleSize && bitmapFactory$Options.outHeight > n) {
            inSampleSize = (int)Math.pow(2.0, Math.min((int)Math.floor(Math.log(bitmapFactory$Options.outWidth / (float)inSampleSize) / Math.log(2.0)), (int)Math.floor(Math.log(bitmapFactory$Options.outHeight / (float)n) / Math.log(2.0))));
            if (inSampleSize < 1) {
                b = false;
            }
            XLEAssert.assertTrue(b);
        }
        else {
            inSampleSize = 1;
        }
        bitmapFactory$Options2.inSampleSize = inSampleSize;
        return bitmapFactory$Options2;
    }
    
    private XLEBitmap createScaledBitmap(final XLEBitmap xleBitmap, int max, int max2) {
        XLEBitmap scaledBitmap8888 = xleBitmap;
        if (validResizeDimention(max, max2)) {
            scaledBitmap8888 = xleBitmap;
            if (xleBitmap.getBitmap() != null) {
                final float n = xleBitmap.getBitmap().getHeight() / (float)xleBitmap.getBitmap().getWidth();
                final float n2 = (float)max2;
                final float n3 = (float)max;
                if (n2 / n3 < n) {
                    max = Math.max(1, (int)(n2 / n));
                }
                else {
                    max2 = Math.max(1, (int)(n3 * n));
                }
                scaledBitmap8888 = XLEBitmap.createScaledBitmap8888(xleBitmap, max, max2, true);
            }
        }
        return scaledBitmap8888;
    }
    
    private void drainWaitingForImage(final TextureManagerScaledNetworkBitmapRequest textureManagerScaledNetworkBitmapRequest, final XLEBitmap xleBitmap) {
        if (this.waitingForImage.containsKey(textureManagerScaledNetworkBitmapRequest)) {
            for (final ImageView imageView : this.waitingForImage.get(textureManagerScaledNetworkBitmapRequest)) {
                if (imageView != null) {
                    if (imageView instanceof XLEImageView) {
                        this.setXLEImageView(textureManagerScaledNetworkBitmapRequest, (XLEImageView)imageView, xleBitmap);
                    }
                    else {
                        this.setView(textureManagerScaledNetworkBitmapRequest, imageView, xleBitmap);
                    }
                }
            }
        }
    }
    
    private int getNetworkBitmapCacheSizeInMB() {
        return Math.max(0, MemoryMonitor.instance().getMemoryClass() - 64) / 2 + 12;
    }
    
    private static boolean invalidUrl(final String s) {
        return s == null || s.length() == 0;
    }
    
    private void load(final TextureManagerScaledNetworkBitmapRequest textureManagerScaledNetworkBitmapRequest) {
        if (invalidUrl(textureManagerScaledNetworkBitmapRequest.url)) {
            return;
        }
        XLEThreadPool.textureThreadPool.run(new TextureManagerDownloadThreadWorker(new TextureManagerDownloadRequest(textureManagerScaledNetworkBitmapRequest)));
    }
    
    private void setImage(final ImageView imageView, final XLEBitmap xleBitmap) {
        Bitmap bitmap;
        if (xleBitmap == null) {
            bitmap = null;
        }
        else {
            bitmap = xleBitmap.getBitmap();
        }
        final OnBitmapSetListener onBitmapSetListener = (OnBitmapSetListener)imageView.getTag(R$id.image_callback);
        if (onBitmapSetListener != null) {
            onBitmapSetListener.onBeforeImageSet(imageView, bitmap);
        }
        imageView.setImageBitmap(bitmap);
        imageView.setTag(R$id.image_bound, (Object)true);
        if (onBitmapSetListener != null) {
            onBitmapSetListener.onAfterImageSet(imageView, bitmap);
        }
    }
    
    private void setView(final TextureManagerScaledNetworkBitmapRequest textureManagerScaledNetworkBitmapRequest, final ImageView imageView, final XLEBitmap xleBitmap) {
        ThreadManager.UIThreadPost(new Runnable() {
            @Override
            public void run() {
                XLEAssert.assertTrue(Thread.currentThread() == ThreadManager.UIThread);
                synchronized (TextureManager.this.listLock) {
                    final boolean keyValueMatches = TextureManager.this.waitingForImage.keyValueMatches(textureManagerScaledNetworkBitmapRequest, imageView);
                    // monitorexit(TextureManager.access$500(this.this$0))
                    if (keyValueMatches) {
                        TextureManager.this.setImage(imageView, xleBitmap);
                        TextureManager.this.listLock;
                        // monitorenter(TextureManager.access$500(this.this$0))
                        try {
                            TextureManager.this.waitingForImage.removeValue(imageView);
                        }
                        finally {}
                    }
                }
            }
        });
    }
    
    private void setXLEImageView(final TextureManagerScaledNetworkBitmapRequest textureManagerScaledNetworkBitmapRequest, final XLEImageView xleImageView, final XLEBitmap xleBitmap) {
        ThreadManager.UIThreadPost(new Runnable() {
            @Override
            public void run() {
                XLEAssert.assertTrue(Thread.currentThread() == ThreadManager.UIThread);
                synchronized (TextureManager.this.listLock) {
                    final boolean keyValueMatches = TextureManager.this.waitingForImage.keyValueMatches(textureManagerScaledNetworkBitmapRequest, xleImageView);
                    // monitorexit(TextureManager.access$500(this.this$0))
                    if (keyValueMatches) {
                        final float alpha = xleImageView.getAlpha();
                        if (xleImageView.getShouldAnimate()) {
                            xleImageView.animate().alpha(0.0f).setDuration(100L).setListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                                public void onAnimationEnd(final Animator animator) {
                                    xleImageView.setFinal(true);
                                    TextureManager.this.setImage(xleImageView, xleBitmap);
                                    xleImageView.animate().alpha(alpha).setDuration(100L).setListener((Animator$AnimatorListener)null);
                                }
                            });
                        }
                        else {
                            TextureManager.this.setImage(xleImageView, xleBitmap);
                        }
                        TextureManager.this.listLock;
                        // monitorenter(TextureManager.access$500(this.this$0))
                        try {
                            TextureManager.this.waitingForImage.removeValue(xleImageView);
                        }
                        finally {}
                    }
                }
            }
        });
    }
    
    private static boolean validResizeDimention(final int n, final int n2) {
        if (n != 0 && n2 != 0) {
            return n > 0 && n2 > 0;
        }
        throw new UnsupportedOperationException();
    }
    
    public void bindToView(final int n, final ImageView imageView, final int n2, final int n3) {
        this.bindToView(n, imageView, n2, n3, null);
    }
    
    public void bindToView(final int n, final ImageView imageView, final int n2, final int n3, final OnBitmapSetListener onBitmapSetListener) {
        final Thread currentThread = Thread.currentThread();
        final Thread uiThread = ThreadManager.UIThread;
        final boolean b = false;
        XLEAssert.assertTrue(currentThread == uiThread);
        final XLEBitmap loadResource = this.loadResource(n);
        boolean b2 = b;
        if (loadResource != null) {
            b2 = true;
        }
        XLEAssert.assertTrue(b2);
        if (imageView instanceof XLEImageView) {
            ((XLEImageView)imageView).TEST_loadingOrLoadedImageUrl = Integer.toString(n);
        }
        this.setImage(imageView, loadResource);
    }
    
    public void bindToView(final URI uri, final ImageView imageView, final int n, final int n2) {
        XLEAssert.assertTrue(Thread.currentThread() == ThreadManager.UIThread);
        if (n != 0 && n2 != 0) {
            String string;
            if (uri == null) {
                string = null;
            }
            else {
                string = uri.toString();
            }
            this.bindToViewInternal(string, imageView, new TextureBindingOption(n, n2));
            return;
        }
        throw new UnsupportedOperationException();
    }
    
    public void bindToView(final URI uri, final ImageView imageView, final TextureBindingOption textureBindingOption) {
        XLEAssert.assertTrue(Thread.currentThread() == ThreadManager.UIThread);
        String string;
        if (uri == null) {
            string = null;
        }
        else {
            string = uri.toString();
        }
        this.bindToViewInternal(string, imageView, textureBindingOption);
    }
    
    public void bindToViewFromFile(final String s, final ImageView imageView, final int n, final int n2) {
        XLEAssert.assertTrue(Thread.currentThread() == ThreadManager.UIThread);
        if (n != 0 && n2 != 0) {
            this.bindToViewInternal(s, imageView, new TextureBindingOption(n, n2));
            return;
        }
        throw new UnsupportedOperationException();
    }
    
    public void bindToViewFromFile(final String s, final ImageView imageView, final TextureBindingOption textureBindingOption) {
        XLEAssert.assertTrue(Thread.currentThread() == ThreadManager.UIThread);
        this.bindToViewInternal(s, imageView, textureBindingOption);
    }
    
    public boolean isBusy() {
        synchronized (this.listLock) {
            final boolean empty = this.inProgress.isEmpty();
            // monitorexit(this.listLock)
            return empty ^ true;
        }
    }
    
    public XLEBitmap loadResource(final int n) {
        final TextureManagerScaledResourceBitmapRequest textureManagerScaledResourceBitmapRequest = new TextureManagerScaledResourceBitmapRequest(n);
        XLEBitmap decodeResource;
        if ((decodeResource = this.resourceBitmapCache.get(textureManagerScaledResourceBitmapRequest)) == null) {
            final BitmapFactory$Options bitmapFactory$Options = new BitmapFactory$Options();
            bitmapFactory$Options.inJustDecodeBounds = true;
            BitmapFactory.decodeResource(XboxTcuiSdk.getResources(), textureManagerScaledResourceBitmapRequest.resourceId, bitmapFactory$Options);
            decodeResource = XLEBitmap.decodeResource(XboxTcuiSdk.getResources(), textureManagerScaledResourceBitmapRequest.resourceId);
            this.resourceBitmapCache.put(textureManagerScaledResourceBitmapRequest, decodeResource);
        }
        XLEAssert.assertNotNull(decodeResource);
        return decodeResource;
    }
    
    public XLEBitmap.XLEBitmapDrawable loadScaledResourceDrawable(final int n) {
        final XLEBitmap loadResource = this.loadResource(n);
        if (loadResource == null) {
            return null;
        }
        return loadResource.getDrawable();
    }
    
    public void logMemoryUsage() {
    }
    
    public void preload(final int n) {
    }
    
    public void preload(final URI uri) {
    }
    
    public void preloadFromFile(final String s) {
    }
    
    public void purgeResourceBitmapCache() {
        this.resourceBitmapCache.clear();
    }
    
    public void setCachingEnabled(final boolean b) {
        int networkBitmapCacheSizeInMB;
        if (b) {
            networkBitmapCacheSizeInMB = this.getNetworkBitmapCacheSizeInMB();
        }
        else {
            networkBitmapCacheSizeInMB = 0;
        }
        this.bitmapCache = new XLEMemoryCache<TextureManagerScaledNetworkBitmapRequest, XLEBitmap>(networkBitmapCacheSizeInMB, 5242880);
        this.bitmapFileCache = XLEFileCacheManager.createCache("texture", 2000, b);
        this.resourceBitmapCache = new HashMap<TextureManagerScaledResourceBitmapRequest, XLEBitmap>();
    }
    
    public void unsafeClearBitmapCache() {
    }
    
    private static class RetryEntry
    {
        private static final long SEC = 1000L;
        private static final long[] TIMES_MS;
        private int curIdx;
        private long currStart;
        
        static {
            TIMES_MS = new long[] { 5000L, 9000L, 19000L, 37000L, 75000L, 150000L, 300000L };
        }
        
        public RetryEntry() {
            this.curIdx = 0;
            this.currStart = System.currentTimeMillis();
        }
        
        public boolean isExpired() {
            return this.currStart + RetryEntry.TIMES_MS[this.curIdx] < System.currentTimeMillis();
        }
        
        public void startNext() {
            final int curIdx = this.curIdx;
            if (curIdx < RetryEntry.TIMES_MS.length - 1) {
                this.curIdx = curIdx + 1;
            }
            this.currStart = System.currentTimeMillis();
        }
    }
    
    private class TextureManagerDecodeThread implements Runnable
    {
        @Override
        public void run() {
            TextureManagerDownloadRequest textureManagerDownloadRequest;
            ByteArrayOutputStream byteArrayOutputStream;
            byte[] byteArray;
            BitmapFactory$Options bitmapFactory$Options;
            BitmapFactory$Options access$200;
            int n;
            int n2;
            XLEBitmap decodeStream;
            XLEBitmap xleBitmap = null;
            Object access$201;
            final Throwable t;
            RetryEntry retryEntry;
            Label_0388_Outer:Label_0421_Outer:
            while (true) {
                textureManagerDownloadRequest = TextureManager.this.toDecode.pop();
                Label_0239: {
                    if (textureManagerDownloadRequest.stream != null) {
                        BackgroundThreadWaitor.getInstance().waitForReady(3000);
                        try {
                            byteArrayOutputStream = new ByteArrayOutputStream();
                            StreamUtil.CopyStream(byteArrayOutputStream, textureManagerDownloadRequest.stream);
                            byteArray = byteArrayOutputStream.toByteArray();
                            bitmapFactory$Options = new BitmapFactory$Options();
                            bitmapFactory$Options.inJustDecodeBounds = true;
                            BitmapFactory.decodeStream((InputStream)new ByteArrayInputStream(byteArray), (Rect)null, bitmapFactory$Options);
                            access$200 = TextureManager.this.computeInSampleSizeOptions(textureManagerDownloadRequest.key.bindingOption.width, textureManagerDownloadRequest.key.bindingOption.height, bitmapFactory$Options);
                            n = bitmapFactory$Options.outWidth / access$200.inSampleSize;
                            n2 = bitmapFactory$Options.outHeight / access$200.inSampleSize;
                            decodeStream = XLEBitmap.decodeStream(new ByteArrayInputStream(byteArray), access$200);
                            if (textureManagerDownloadRequest.key.bindingOption.useFileCache && !TextureManager.this.bitmapFileCache.contains(textureManagerDownloadRequest.key)) {
                                TextureManager.this.bitmapFileCache.save(textureManagerDownloadRequest.key, new ByteArrayInputStream(byteArray));
                            }
                            xleBitmap = TextureManager.this.createScaledBitmap(decodeStream, textureManagerDownloadRequest.key.bindingOption.width, textureManagerDownloadRequest.key.bindingOption.height);
                            break Label_0239;
                        }
                        catch (Exception ex) {}
                    }
                    xleBitmap = null;
                }
                BackgroundThreadWaitor.getInstance().waitForReady(3000);
                access$201 = TextureManager.this.listLock;
                // monitorenter(access$201)
                Label_0302: {
                    if (xleBitmap == null) {
                        break Label_0302;
                    }
                Block_8_Outer:
                    while (true) {
                        try {
                            TextureManager.this.bitmapCache.add(textureManagerDownloadRequest.key, xleBitmap, xleBitmap.getByteCount());
                            TextureManager.this.timeToRetryCache.remove(textureManagerDownloadRequest.key);
                            // monitorexit(access$201)
                            // monitorexit(access$201)
                            // iftrue(Label_0366:, retryEntry == null)
                            while (true) {
                                while (true) {
                                    while (true) {
                                        TextureManager.this.drainWaitingForImage(textureManagerDownloadRequest.key, xleBitmap);
                                        TextureManager.this.inProgress.remove(textureManagerDownloadRequest.key);
                                        continue Label_0388_Outer;
                                        throw t;
                                        retryEntry.startNext();
                                        continue Label_0421_Outer;
                                        Label_0366: {
                                            TextureManager.this.timeToRetryCache.put(textureManagerDownloadRequest.key, new RetryEntry());
                                        }
                                        continue Label_0421_Outer;
                                    }
                                    xleBitmap = TextureManager.this.loadResource(textureManagerDownloadRequest.key.bindingOption.resourceIdForError);
                                    retryEntry = (RetryEntry)TextureManager.this.timeToRetryCache.get(textureManagerDownloadRequest.key);
                                    continue Block_8_Outer;
                                }
                                continue;
                            }
                        }
                        // iftrue(Label_0388:, textureManagerDownloadRequest.key.bindingOption.resourceIdForError == -1)
                        finally {
                            continue;
                        }
                        break;
                    }
                }
            }
        }
    }
    
    private class TextureManagerDownloadThreadWorker implements Runnable
    {
        private TextureManagerDownloadRequest request;
        
        public TextureManagerDownloadThreadWorker(final TextureManagerDownloadRequest request) {
            this.request = request;
        }
        
        private InputStream downloadFromAssets(final String s) {
            try {
                return XboxTcuiSdk.getAssetManager().open(s);
            }
            catch (IOException ex) {
                return null;
            }
        }
        
        private InputStream downloadFromWeb(final String s) {
            try {
                final XLEHttpStatusAndStream httpStatusAndStreamInternal = HttpClientFactory.textureFactory.getHttpClient(15000).getHttpStatusAndStreamInternal((HttpUriRequest)new HttpGet(URI.create(s)), false);
                if (httpStatusAndStreamInternal.statusCode == 200) {
                    return httpStatusAndStreamInternal.stream;
                }
                return null;
            }
            catch (Exception ex) {
                return null;
            }
        }
        
        @Override
        public void run() {
            XLEAssert.assertTrue(this.request.key != null && this.request.key.url != null);
            this.request.stream = null;
            Label_0185: {
                try {
                    TextureManagerDownloadRequest textureManagerDownloadRequest;
                    InputStream stream;
                    if (this.request.key.url.startsWith("http")) {
                        if (this.request.key.bindingOption.useFileCache) {
                            this.request.stream = TextureManager.this.bitmapFileCache.getInputStreamForRead(this.request.key);
                            if (this.request.stream != null) {
                                break Label_0185;
                            }
                            textureManagerDownloadRequest = this.request;
                            stream = this.downloadFromWeb(this.request.key.url);
                        }
                        else {
                            textureManagerDownloadRequest = this.request;
                            stream = this.downloadFromWeb(this.request.key.url);
                        }
                    }
                    else {
                        textureManagerDownloadRequest = this.request;
                        stream = this.downloadFromAssets(this.request.key.url);
                    }
                    textureManagerDownloadRequest.stream = stream;
                }
                catch (Exception ex) {}
            }
            synchronized (TextureManager.this.listLock) {
                TextureManager.this.toDecode.push(this.request);
            }
        }
    }
}
