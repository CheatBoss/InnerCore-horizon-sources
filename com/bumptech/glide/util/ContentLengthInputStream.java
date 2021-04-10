package com.bumptech.glide.util;

import java.io.*;
import android.text.*;
import android.util.*;

public final class ContentLengthInputStream extends FilterInputStream
{
    private static final String TAG = "ContentLengthStream";
    private static final int UNKNOWN = -1;
    private final long contentLength;
    private int readSoFar;
    
    ContentLengthInputStream(final InputStream inputStream, final long contentLength) {
        super(inputStream);
        this.contentLength = contentLength;
    }
    
    private int checkReadSoFarOrThrow(final int n) throws IOException {
        if (n >= 0) {
            this.readSoFar += n;
            return n;
        }
        if (this.contentLength - this.readSoFar > 0L) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Failed to read all expected data, expected: ");
            sb.append(this.contentLength);
            sb.append(", but read: ");
            sb.append(this.readSoFar);
            throw new IOException(sb.toString());
        }
        return n;
    }
    
    public static InputStream obtain(final InputStream inputStream, final long n) {
        return new ContentLengthInputStream(inputStream, n);
    }
    
    public static InputStream obtain(final InputStream inputStream, final String s) {
        return obtain(inputStream, parseContentLength(s));
    }
    
    private static int parseContentLength(final String s) {
        if (!TextUtils.isEmpty((CharSequence)s)) {
            try {
                return Integer.parseInt(s);
            }
            catch (NumberFormatException ex) {
                if (Log.isLoggable("ContentLengthStream", 3)) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("failed to parse content length header: ");
                    sb.append(s);
                    Log.d("ContentLengthStream", sb.toString(), (Throwable)ex);
                }
            }
        }
        return -1;
    }
    
    @Override
    public int available() throws IOException {
        synchronized (this) {
            return (int)Math.max(this.contentLength - this.readSoFar, this.in.available());
        }
    }
    
    @Override
    public int read() throws IOException {
        synchronized (this) {
            return this.checkReadSoFarOrThrow(super.read());
        }
    }
    
    @Override
    public int read(final byte[] array) throws IOException {
        return this.read(array, 0, array.length);
    }
    
    @Override
    public int read(final byte[] array, int checkReadSoFarOrThrow, final int n) throws IOException {
        synchronized (this) {
            checkReadSoFarOrThrow = this.checkReadSoFarOrThrow(super.read(array, checkReadSoFarOrThrow, n));
            return checkReadSoFarOrThrow;
        }
    }
}
