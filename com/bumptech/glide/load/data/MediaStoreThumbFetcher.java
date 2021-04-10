package com.bumptech.glide.load.data;

import android.content.*;
import android.net.*;
import android.util.*;
import com.bumptech.glide.*;
import java.io.*;
import android.database.*;
import android.text.*;
import com.bumptech.glide.load.resource.bitmap.*;
import android.provider.*;

public class MediaStoreThumbFetcher implements DataFetcher<InputStream>
{
    private static final ThumbnailStreamOpenerFactory DEFAULT_FACTORY;
    private static final int MINI_HEIGHT = 384;
    private static final int MINI_WIDTH = 512;
    private static final String TAG = "MediaStoreThumbFetcher";
    private final Context context;
    private final DataFetcher<InputStream> defaultFetcher;
    private final ThumbnailStreamOpenerFactory factory;
    private final int height;
    private InputStream inputStream;
    private final Uri mediaStoreUri;
    private final int width;
    
    static {
        DEFAULT_FACTORY = new ThumbnailStreamOpenerFactory();
    }
    
    public MediaStoreThumbFetcher(final Context context, final Uri uri, final DataFetcher<InputStream> dataFetcher, final int n, final int n2) {
        this(context, uri, dataFetcher, n, n2, MediaStoreThumbFetcher.DEFAULT_FACTORY);
    }
    
    MediaStoreThumbFetcher(final Context context, final Uri mediaStoreUri, final DataFetcher<InputStream> defaultFetcher, final int width, final int height, final ThumbnailStreamOpenerFactory factory) {
        this.context = context;
        this.mediaStoreUri = mediaStoreUri;
        this.defaultFetcher = defaultFetcher;
        this.width = width;
        this.height = height;
        this.factory = factory;
    }
    
    private static boolean isMediaStoreUri(final Uri uri) {
        return uri != null && "content".equals(uri.getScheme()) && "media".equals(uri.getAuthority());
    }
    
    private static boolean isMediaStoreVideo(final Uri uri) {
        return isMediaStoreUri(uri) && uri.getPathSegments().contains("video");
    }
    
    private InputStream openThumbInputStream(final ThumbnailStreamOpener thumbnailStreamOpener) {
        final InputStream inputStream = null;
        InputStream open;
        try {
            open = thumbnailStreamOpener.open(this.context, this.mediaStoreUri);
        }
        catch (FileNotFoundException ex) {
            open = inputStream;
            if (Log.isLoggable("MediaStoreThumbFetcher", 3)) {
                Log.d("MediaStoreThumbFetcher", "Failed to find thumbnail file", (Throwable)ex);
                open = inputStream;
            }
        }
        int orientation = -1;
        if (open != null) {
            orientation = thumbnailStreamOpener.getOrientation(this.context, this.mediaStoreUri);
        }
        InputStream inputStream2 = open;
        if (orientation != -1) {
            inputStream2 = new ExifOrientationStream(open, orientation);
        }
        return inputStream2;
    }
    
    @Override
    public void cancel() {
    }
    
    @Override
    public void cleanup() {
        if (this.inputStream != null) {
            try {
                this.inputStream.close();
            }
            catch (IOException ex) {}
        }
        this.defaultFetcher.cleanup();
    }
    
    @Override
    public String getId() {
        return this.mediaStoreUri.toString();
    }
    
    @Override
    public InputStream loadData(final Priority priority) throws Exception {
        final ThumbnailStreamOpener build = this.factory.build(this.mediaStoreUri, this.width, this.height);
        if (build != null) {
            this.inputStream = this.openThumbInputStream(build);
        }
        if (this.inputStream == null) {
            this.inputStream = this.defaultFetcher.loadData(priority);
        }
        return this.inputStream;
    }
    
    static class FileService
    {
        public boolean exists(final File file) {
            return file.exists();
        }
        
        public File get(final String s) {
            return new File(s);
        }
        
        public long length(final File file) {
            return file.length();
        }
    }
    
    static class ImageThumbnailQuery implements ThumbnailQuery
    {
        private static final String[] PATH_PROJECTION;
        private static final String PATH_SELECTION = "kind = 1 AND image_id = ?";
        
        static {
            PATH_PROJECTION = new String[] { "_data" };
        }
        
        @Override
        public Cursor queryPath(final Context context, final Uri uri) {
            return context.getContentResolver().query(MediaStore$Images$Thumbnails.EXTERNAL_CONTENT_URI, ImageThumbnailQuery.PATH_PROJECTION, "kind = 1 AND image_id = ?", new String[] { uri.getLastPathSegment() }, (String)null);
        }
    }
    
    interface ThumbnailQuery
    {
        Cursor queryPath(final Context p0, final Uri p1);
    }
    
    static class ThumbnailStreamOpener
    {
        private static final FileService DEFAULT_SERVICE;
        private ThumbnailQuery query;
        private final FileService service;
        
        static {
            DEFAULT_SERVICE = new FileService();
        }
        
        public ThumbnailStreamOpener(final FileService service, final ThumbnailQuery query) {
            this.service = service;
            this.query = query;
        }
        
        public ThumbnailStreamOpener(final ThumbnailQuery thumbnailQuery) {
            this(ThumbnailStreamOpener.DEFAULT_SERVICE, thumbnailQuery);
        }
        
        private Uri parseThumbUri(final Cursor cursor) {
            final Uri uri = null;
            final String string = cursor.getString(0);
            Uri fromFile = uri;
            if (!TextUtils.isEmpty((CharSequence)string)) {
                final File value = this.service.get(string);
                fromFile = uri;
                if (this.service.exists(value)) {
                    fromFile = uri;
                    if (this.service.length(value) > 0L) {
                        fromFile = Uri.fromFile(value);
                    }
                }
            }
            return fromFile;
        }
        
        public int getOrientation(final Context context, final Uri uri) {
            int n = -1;
            InputStream inputStream = null;
            InputStream openInputStream = null;
            while (true) {
                try {
                    try {
                        final InputStream inputStream2 = inputStream = (openInputStream = context.getContentResolver().openInputStream(uri));
                        final int orientation;
                        n = (orientation = new ImageHeaderParser(inputStream2).getOrientation());
                        if (inputStream2 == null) {
                            return orientation;
                        }
                        final int n2 = n;
                        try {
                            inputStream2.close();
                            return n;
                        }
                        catch (IOException ex2) {
                            return n2;
                        }
                    }
                    finally {
                        if (openInputStream != null) {
                            final InputStream inputStream3 = openInputStream;
                            inputStream3.close();
                        }
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Failed to open uri: ");
                        sb.append(uri);
                        final IOException ex;
                        Log.d("MediaStoreThumbFetcher", sb.toString(), (Throwable)ex);
                        final int orientation = n;
                        // iftrue(Label_0147:, inputStream == null)
                        inputStream.close();
                        return -1;
                        Label_0147: {
                            return orientation;
                        }
                    }
                }
                catch (IOException ex3) {}
                try {
                    final InputStream inputStream3 = openInputStream;
                    inputStream3.close();
                    continue;
                }
                catch (IOException ex4) {}
                break;
            }
        }
        
        public InputStream open(final Context context, Uri thumbUri) throws FileNotFoundException {
            final Uri uri = null;
            InputStream openInputStream = null;
            final Cursor queryPath = this.query.queryPath(context, thumbUri);
            thumbUri = uri;
            if (queryPath != null) {
                thumbUri = uri;
                try {
                    if (queryPath.moveToFirst()) {
                        thumbUri = this.parseThumbUri(queryPath);
                    }
                }
                finally {
                    if (queryPath != null) {
                        queryPath.close();
                    }
                }
            }
            if (queryPath != null) {
                queryPath.close();
            }
            if (thumbUri != null) {
                openInputStream = context.getContentResolver().openInputStream(thumbUri);
            }
            return openInputStream;
        }
    }
    
    static class ThumbnailStreamOpenerFactory
    {
        public ThumbnailStreamOpener build(final Uri uri, final int n, final int n2) {
            if (!isMediaStoreUri(uri) || n > 512 || n2 > 384) {
                return null;
            }
            if (isMediaStoreVideo(uri)) {
                return new ThumbnailStreamOpener(new VideoThumbnailQuery());
            }
            return new ThumbnailStreamOpener(new ImageThumbnailQuery());
        }
    }
    
    static class VideoThumbnailQuery implements ThumbnailQuery
    {
        private static final String[] PATH_PROJECTION;
        private static final String PATH_SELECTION = "kind = 1 AND video_id = ?";
        
        static {
            PATH_PROJECTION = new String[] { "_data" };
        }
        
        @Override
        public Cursor queryPath(final Context context, final Uri uri) {
            return context.getContentResolver().query(MediaStore$Video$Thumbnails.EXTERNAL_CONTENT_URI, VideoThumbnailQuery.PATH_PROJECTION, "kind = 1 AND video_id = ?", new String[] { uri.getLastPathSegment() }, (String)null);
        }
    }
}
