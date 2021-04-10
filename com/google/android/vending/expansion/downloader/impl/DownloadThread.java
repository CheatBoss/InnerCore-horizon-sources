package com.google.android.vending.expansion.downloader.impl;

import android.content.*;
import java.util.*;
import com.google.android.vending.expansion.downloader.*;
import android.util.*;
import java.io.*;
import android.os.*;
import java.net.*;

public class DownloadThread
{
    private Context mContext;
    private final DownloadsDB mDB;
    private DownloadInfo mInfo;
    private final DownloadNotification mNotification;
    private DownloaderService mService;
    private String mUserAgent;
    
    public DownloadThread(final DownloadInfo mInfo, final DownloaderService downloaderService, final DownloadNotification mNotification) {
        this.mContext = (Context)downloaderService;
        this.mInfo = mInfo;
        this.mService = downloaderService;
        this.mNotification = mNotification;
        this.mDB = DownloadsDB.getDB((Context)downloaderService);
        final StringBuilder sb = new StringBuilder();
        sb.append("APKXDL (Linux; U; Android ");
        sb.append(Build$VERSION.RELEASE);
        sb.append(";");
        sb.append(Locale.getDefault().toString());
        sb.append("; ");
        sb.append(Build.DEVICE);
        sb.append("/");
        sb.append(Build.ID);
        sb.append(")");
        sb.append(downloaderService.getPackageName());
        this.mUserAgent = sb.toString();
    }
    
    private void addRequestHeaders(final InnerState innerState, final HttpURLConnection httpURLConnection) {
        if (innerState.mContinuingDownload) {
            if (innerState.mHeaderETag != null) {
                httpURLConnection.setRequestProperty("If-Match", innerState.mHeaderETag);
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("bytes=");
            sb.append(innerState.mBytesSoFar);
            sb.append("-");
            httpURLConnection.setRequestProperty("Range", sb.toString());
        }
    }
    
    private boolean cannotResume(final InnerState innerState) {
        return innerState.mBytesSoFar > 0 && innerState.mHeaderETag == null;
    }
    
    private void checkConnectivity(final State state) throws StopRequest {
        final int networkAvailabilityState = this.mService.getNetworkAvailabilityState(this.mDB);
        if (networkAvailabilityState == 2) {
            throw new StopRequest(195, "waiting for network to return");
        }
        if (networkAvailabilityState == 3) {
            throw new StopRequest(197, "waiting for wifi");
        }
        if (networkAvailabilityState == 5) {
            throw new StopRequest(195, "roaming is not allowed");
        }
        if (networkAvailabilityState != 6) {
            return;
        }
        throw new StopRequest(196, "waiting for wifi or for download over cellular to be authorized");
    }
    
    private void checkPausedOrCanceled(final State state) throws StopRequest {
        if (this.mService.getControl() != 1) {
            return;
        }
        if (this.mService.getStatus() != 193) {
            return;
        }
        throw new StopRequest(this.mService.getStatus(), "download paused");
    }
    
    private void cleanupDestination(final State state, final int n) {
        this.closeDestination(state);
        if (state.mFilename != null && DownloaderService.isStatusError(n)) {
            new File(state.mFilename).delete();
            state.mFilename = null;
        }
    }
    
    private void closeDestination(final State state) {
        try {
            if (state.mStream != null) {
                state.mStream.close();
                state.mStream = null;
            }
        }
        catch (IOException ex) {}
    }
    
    private void executeDownload(final State state, final HttpURLConnection httpURLConnection) throws StopRequest, RetryDownload {
        final InnerState innerState = new InnerState();
        final byte[] array = new byte[4096];
        this.checkPausedOrCanceled(state);
        this.setupDestinationFile(state, innerState);
        this.addRequestHeaders(innerState, httpURLConnection);
        this.checkConnectivity(state);
        this.mNotification.onDownloadStateChanged(3);
        this.handleExceptionalStatus(state, innerState, httpURLConnection, this.sendRequest(state, httpURLConnection));
        this.processResponseHeaders(state, innerState, httpURLConnection);
        final InputStream openResponseEntity = this.openResponseEntity(state, httpURLConnection);
        this.mNotification.onDownloadStateChanged(4);
        this.transferData(state, innerState, array, openResponseEntity);
    }
    
    private void finalizeDestinationFile(final State state) throws StopRequest {
        this.syncDestination(state);
        final String mFilename = state.mFilename;
        final String generateSaveFileName = Helpers.generateSaveFileName((Context)this.mService, this.mInfo.mFileName);
        if (state.mFilename.equals(generateSaveFileName)) {
            return;
        }
        final File file = new File(mFilename);
        final File file2 = new File(generateSaveFileName);
        if (this.mInfo.mTotalBytes == -1L || this.mInfo.mCurrentBytes != this.mInfo.mTotalBytes) {
            throw new StopRequest(487, "file delivered with incorrect size. probably due to network not browser configured");
        }
        if (file.renameTo(file2)) {
            return;
        }
        throw new StopRequest(492, "unable to finalize destination file");
    }
    
    private int getFinalStatusForHttpError(final State state) {
        if (this.mService.getNetworkAvailabilityState(this.mDB) != 1) {
            return 195;
        }
        if (this.mInfo.mNumFailed < 5) {
            state.mCountRetry = true;
            return 194;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("reached max retries for ");
        sb.append(this.mInfo.mNumFailed);
        Log.w("LVLDL", sb.toString());
        return 495;
    }
    
    private void handleEndOfStream(final State state, final InnerState innerState) throws StopRequest {
        this.mInfo.mCurrentBytes = innerState.mBytesSoFar;
        this.mDB.updateDownload(this.mInfo);
        if (innerState.mHeaderContentLength == null || innerState.mBytesSoFar == Integer.parseInt(innerState.mHeaderContentLength)) {
            return;
        }
        if (this.cannotResume(innerState)) {
            throw new StopRequest(489, "mismatched content length");
        }
        throw new StopRequest(this.getFinalStatusForHttpError(state), "closed socket before end of file");
    }
    
    private void handleExceptionalStatus(final State state, final InnerState innerState, final HttpURLConnection httpURLConnection, final int n) throws StopRequest, RetryDownload {
        if (n == 503 && this.mInfo.mNumFailed < 5) {
            this.handleServiceUnavailable(state, httpURLConnection);
        }
        int n2;
        if (innerState.mContinuingDownload) {
            n2 = 206;
        }
        else {
            n2 = 200;
        }
        if (n != n2) {
            this.handleOtherStatus(state, innerState, n);
            return;
        }
        state.mRedirectCount = 0;
    }
    
    private void handleOtherStatus(final State state, final InnerState innerState, final int n) throws StopRequest {
        int n2;
        if (!DownloaderService.isStatusError(n)) {
            if (n >= 300 && n < 400) {
                n2 = 493;
            }
            else if (innerState.mContinuingDownload && n == 200) {
                n2 = 489;
            }
            else {
                n2 = 494;
            }
        }
        else {
            n2 = n;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("http error ");
        sb.append(n);
        throw new StopRequest(n2, sb.toString());
    }
    
    private void handleServiceUnavailable(final State state, final HttpURLConnection httpURLConnection) throws StopRequest {
        state.mCountRetry = true;
        final String headerField = httpURLConnection.getHeaderField("Retry-After");
        if (headerField != null) {
            try {
                state.mRetryAfter = Integer.parseInt(headerField);
                if (state.mRetryAfter >= 0) {
                    final int mRetryAfter = state.mRetryAfter;
                    int mRetryAfter2 = 30;
                    Label_0071: {
                        if (mRetryAfter >= 30) {
                            final int mRetryAfter3 = state.mRetryAfter;
                            mRetryAfter2 = 86400;
                            if (mRetryAfter3 <= 86400) {
                                break Label_0071;
                            }
                        }
                        state.mRetryAfter = mRetryAfter2;
                    }
                    state.mRetryAfter += Helpers.sRandom.nextInt(31);
                    state.mRetryAfter *= 1000;
                }
                else {
                    state.mRetryAfter = 0;
                }
            }
            catch (NumberFormatException ex) {}
        }
        throw new StopRequest(194, "got 503 Service Unavailable, will retry later");
    }
    
    private void logNetworkState() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Net ");
        String s;
        if (this.mService.getNetworkAvailabilityState(this.mDB) == 1) {
            s = "Up";
        }
        else {
            s = "Down";
        }
        sb.append(s);
        Log.i("LVLDL", sb.toString());
    }
    
    private void notifyDownloadCompleted(final int n, final boolean b, final int n2, final int n3, final boolean b2, final String s) {
        this.updateDownloadDatabase(n, b, n2, n3, b2, s);
        DownloaderService.isStatusCompleted(n);
    }
    
    private InputStream openResponseEntity(final State state, final HttpURLConnection httpURLConnection) throws StopRequest {
        try {
            return httpURLConnection.getInputStream();
        }
        catch (IOException ex) {
            this.logNetworkState();
            final int finalStatusForHttpError = this.getFinalStatusForHttpError(state);
            final StringBuilder sb = new StringBuilder();
            sb.append("while getting entity: ");
            sb.append(ex.toString());
            throw new StopRequest(finalStatusForHttpError, sb.toString(), ex);
        }
    }
    
    private void processResponseHeaders(final State state, final InnerState innerState, final HttpURLConnection ex) throws StopRequest {
        if (innerState.mContinuingDownload) {
            return;
        }
        this.readResponseHeaders(state, innerState, (HttpURLConnection)ex);
        try {
            state.mFilename = this.mService.generateSaveFile(this.mInfo.mFileName, this.mInfo.mTotalBytes);
            Label_0098: {
                File file;
                try {
                    state.mStream = new FileOutputStream(state.mFilename);
                    break Label_0098;
                }
                catch (FileNotFoundException ex) {
                    final File file2;
                    file = (file2 = new File(Helpers.getSaveFilePath((Context)this.mService)));
                    final boolean b = file2.mkdirs();
                    if (!b) {
                        break Label_0098;
                    }
                    final State state2 = state;
                    final State state3 = state;
                    final String s = state3.mFilename;
                    final FileOutputStream fileOutputStream = new FileOutputStream(s);
                    state2.mStream = fileOutputStream;
                }
                try {
                    final File file2 = file;
                    final boolean b = file2.mkdirs();
                    if (b) {
                        final State state2 = state;
                        final State state3 = state;
                        final String s = state3.mFilename;
                        final FileOutputStream fileOutputStream = new FileOutputStream(s);
                        state2.mStream = fileOutputStream;
                    }
                    this.updateDatabaseFromHeaders(state, innerState);
                    this.checkConnectivity(state);
                }
                catch (Exception ex2) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("while opening destination file: ");
                    sb.append(ex.toString());
                    throw new StopRequest(492, sb.toString(), ex);
                }
            }
        }
        catch (DownloaderService.GenerateSaveFileError generateSaveFileError) {
            throw new StopRequest(generateSaveFileError.mStatus, generateSaveFileError.mMessage);
        }
    }
    
    private int readFromResponse(final State state, final InnerState innerState, final byte[] array, final InputStream inputStream) throws StopRequest {
        try {
            return inputStream.read(array);
        }
        catch (IOException ex) {
            this.logNetworkState();
            this.mInfo.mCurrentBytes = innerState.mBytesSoFar;
            this.mDB.updateDownload(this.mInfo);
            if (this.cannotResume(innerState)) {
                final StringBuilder sb = new StringBuilder();
                sb.append("while reading response: ");
                sb.append(ex.toString());
                sb.append(", can't resume interrupted download with no ETag");
                throw new StopRequest(489, sb.toString(), ex);
            }
            final int finalStatusForHttpError = this.getFinalStatusForHttpError(state);
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("while reading response: ");
            sb2.append(ex.toString());
            throw new StopRequest(finalStatusForHttpError, sb2.toString(), ex);
        }
    }
    
    private void readResponseHeaders(final State state, final InnerState innerState, final HttpURLConnection httpURLConnection) throws StopRequest {
        final String headerField = httpURLConnection.getHeaderField("Content-Disposition");
        if (headerField != null) {
            innerState.mHeaderContentDisposition = headerField;
        }
        final String headerField2 = httpURLConnection.getHeaderField("Content-Location");
        if (headerField2 != null) {
            innerState.mHeaderContentLocation = headerField2;
        }
        final String headerField3 = httpURLConnection.getHeaderField("ETag");
        if (headerField3 != null) {
            innerState.mHeaderETag = headerField3;
        }
        String s = null;
        final String headerField4 = httpURLConnection.getHeaderField("Transfer-Encoding");
        if (headerField4 != null) {
            s = headerField4;
        }
        final String headerField5 = httpURLConnection.getHeaderField("Content-Type");
        if (headerField5 != null && !headerField5.equals("application/vnd.android.obb")) {
            throw new StopRequest(487, "file delivered with incorrect Mime type");
        }
        if (s == null) {
            final long n = httpURLConnection.getContentLength();
            if (headerField5 != null) {
                if (n != -1L && n != this.mInfo.mTotalBytes) {
                    Log.e("LVLDL", "Incorrect file size delivered.");
                }
                else {
                    innerState.mHeaderContentLength = Long.toString(n);
                }
            }
        }
        if (innerState.mHeaderContentLength != null || (s != null && s.equalsIgnoreCase("chunked"))) {
            return;
        }
        throw new StopRequest(495, "can't know size of download, giving up");
    }
    
    private void reportProgress(final State state, final InnerState innerState) {
        final long currentTimeMillis = System.currentTimeMillis();
        if (innerState.mBytesSoFar - innerState.mBytesNotified > 4096 && currentTimeMillis - innerState.mTimeLastNotification > 1000L) {
            this.mInfo.mCurrentBytes = innerState.mBytesSoFar;
            this.mDB.updateDownloadCurrentBytes(this.mInfo);
            innerState.mBytesNotified = innerState.mBytesSoFar;
            innerState.mTimeLastNotification = currentTimeMillis;
            this.mService.notifyUpdateBytes(innerState.mBytesThisSession + this.mService.mBytesSoFar);
        }
    }
    
    private int sendRequest(final State state, final HttpURLConnection httpURLConnection) throws StopRequest {
        try {
            return httpURLConnection.getResponseCode();
        }
        catch (IOException ex) {
            this.logNetworkState();
            final int finalStatusForHttpError = this.getFinalStatusForHttpError(state);
            final StringBuilder sb = new StringBuilder();
            sb.append("while trying to execute request: ");
            sb.append(ex.toString());
            throw new StopRequest(finalStatusForHttpError, sb.toString(), ex);
        }
        catch (IllegalArgumentException ex2) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("while trying to execute request: ");
            sb2.append(ex2.toString());
            throw new StopRequest(495, sb2.toString(), ex2);
        }
    }
    
    private void setupDestinationFile(final State state, final InnerState innerState) throws StopRequest {
        Label_0222: {
            if (state.mFilename != null) {
                if (!Helpers.isFilenameValid(state.mFilename)) {
                    throw new StopRequest(492, "found invalid internal destination filename");
                }
                final File file = new File(state.mFilename);
                if (file.exists()) {
                    final long length = file.length();
                    if (length != 0L) {
                        if (this.mInfo.mETag != null) {
                            try {
                                state.mStream = new FileOutputStream(state.mFilename, true);
                                innerState.mBytesSoFar = (int)length;
                                if (this.mInfo.mTotalBytes != -1L) {
                                    innerState.mHeaderContentLength = Long.toString(this.mInfo.mTotalBytes);
                                }
                                innerState.mHeaderETag = this.mInfo.mETag;
                                innerState.mContinuingDownload = true;
                                break Label_0222;
                            }
                            catch (FileNotFoundException ex) {
                                final StringBuilder sb = new StringBuilder();
                                sb.append("while opening destination for resuming: ");
                                sb.append(ex.toString());
                                throw new StopRequest(492, sb.toString(), ex);
                            }
                        }
                        file.delete();
                        throw new StopRequest(489, "Trying to resume a download that can't be resumed");
                    }
                    file.delete();
                    state.mFilename = null;
                }
            }
        }
        if (state.mStream != null) {
            this.closeDestination(state);
        }
    }
    
    private void syncDestination(final State p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: astore          4
        //     3: aconst_null    
        //     4: astore          5
        //     6: aconst_null    
        //     7: astore_3       
        //     8: new             Ljava/io/FileOutputStream;
        //    11: dup            
        //    12: aload_1        
        //    13: getfield        com/google/android/vending/expansion/downloader/impl/DownloadThread$State.mFilename:Ljava/lang/String;
        //    16: iconst_1       
        //    17: invokespecial   java/io/FileOutputStream.<init>:(Ljava/lang/String;Z)V
        //    20: astore_2       
        //    21: aload_2        
        //    22: astore_3       
        //    23: aload_2        
        //    24: invokevirtual   java/io/FileOutputStream.getFD:()Ljava/io/FileDescriptor;
        //    27: invokevirtual   java/io/FileDescriptor.sync:()V
        //    30: aload_2        
        //    31: invokevirtual   java/io/FileOutputStream.close:()V
        //    34: return         
        //    35: astore_2       
        //    36: goto            343
        //    39: astore_2       
        //    40: goto            357
        //    43: astore_3       
        //    44: aload_2        
        //    45: astore_1       
        //    46: aload_3        
        //    47: astore_2       
        //    48: goto            74
        //    51: astore          4
        //    53: goto            101
        //    56: astore          4
        //    58: goto            183
        //    61: astore          4
        //    63: goto            265
        //    66: astore_1       
        //    67: goto            366
        //    70: astore_2       
        //    71: aload           4
        //    73: astore_1       
        //    74: aload_1        
        //    75: astore_3       
        //    76: ldc_w           "LVLDL"
        //    79: ldc_w           "exception while syncing file: "
        //    82: aload_2        
        //    83: invokestatic    android/util/Log.w:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //    86: pop            
        //    87: aload_1        
        //    88: ifnull          364
        //    91: aload_1        
        //    92: invokevirtual   java/io/FileOutputStream.close:()V
        //    95: return         
        //    96: astore          4
        //    98: aload           5
        //   100: astore_2       
        //   101: aload_2        
        //   102: astore_3       
        //   103: new             Ljava/lang/StringBuilder;
        //   106: dup            
        //   107: invokespecial   java/lang/StringBuilder.<init>:()V
        //   110: astore          5
        //   112: aload_2        
        //   113: astore_3       
        //   114: aload           5
        //   116: ldc_w           "IOException trying to sync "
        //   119: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   122: pop            
        //   123: aload_2        
        //   124: astore_3       
        //   125: aload           5
        //   127: aload_1        
        //   128: getfield        com/google/android/vending/expansion/downloader/impl/DownloadThread$State.mFilename:Ljava/lang/String;
        //   131: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   134: pop            
        //   135: aload_2        
        //   136: astore_3       
        //   137: aload           5
        //   139: ldc_w           ": "
        //   142: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   145: pop            
        //   146: aload_2        
        //   147: astore_3       
        //   148: aload           5
        //   150: aload           4
        //   152: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //   155: pop            
        //   156: aload_2        
        //   157: astore_3       
        //   158: ldc_w           "LVLDL"
        //   161: aload           5
        //   163: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   166: invokestatic    android/util/Log.w:(Ljava/lang/String;Ljava/lang/String;)I
        //   169: pop            
        //   170: aload_2        
        //   171: ifnull          364
        //   174: aload_2        
        //   175: invokevirtual   java/io/FileOutputStream.close:()V
        //   178: return         
        //   179: astore          4
        //   181: aconst_null    
        //   182: astore_2       
        //   183: aload_2        
        //   184: astore_3       
        //   185: new             Ljava/lang/StringBuilder;
        //   188: dup            
        //   189: invokespecial   java/lang/StringBuilder.<init>:()V
        //   192: astore          5
        //   194: aload_2        
        //   195: astore_3       
        //   196: aload           5
        //   198: ldc_w           "file "
        //   201: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   204: pop            
        //   205: aload_2        
        //   206: astore_3       
        //   207: aload           5
        //   209: aload_1        
        //   210: getfield        com/google/android/vending/expansion/downloader/impl/DownloadThread$State.mFilename:Ljava/lang/String;
        //   213: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   216: pop            
        //   217: aload_2        
        //   218: astore_3       
        //   219: aload           5
        //   221: ldc_w           " sync failed: "
        //   224: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   227: pop            
        //   228: aload_2        
        //   229: astore_3       
        //   230: aload           5
        //   232: aload           4
        //   234: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //   237: pop            
        //   238: aload_2        
        //   239: astore_3       
        //   240: ldc_w           "LVLDL"
        //   243: aload           5
        //   245: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   248: invokestatic    android/util/Log.w:(Ljava/lang/String;Ljava/lang/String;)I
        //   251: pop            
        //   252: aload_2        
        //   253: ifnull          364
        //   256: aload_2        
        //   257: invokevirtual   java/io/FileOutputStream.close:()V
        //   260: return         
        //   261: astore          4
        //   263: aconst_null    
        //   264: astore_2       
        //   265: aload_2        
        //   266: astore_3       
        //   267: new             Ljava/lang/StringBuilder;
        //   270: dup            
        //   271: invokespecial   java/lang/StringBuilder.<init>:()V
        //   274: astore          5
        //   276: aload_2        
        //   277: astore_3       
        //   278: aload           5
        //   280: ldc_w           "file "
        //   283: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   286: pop            
        //   287: aload_2        
        //   288: astore_3       
        //   289: aload           5
        //   291: aload_1        
        //   292: getfield        com/google/android/vending/expansion/downloader/impl/DownloadThread$State.mFilename:Ljava/lang/String;
        //   295: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   298: pop            
        //   299: aload_2        
        //   300: astore_3       
        //   301: aload           5
        //   303: ldc_w           " not found: "
        //   306: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   309: pop            
        //   310: aload_2        
        //   311: astore_3       
        //   312: aload           5
        //   314: aload           4
        //   316: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //   319: pop            
        //   320: aload_2        
        //   321: astore_3       
        //   322: ldc_w           "LVLDL"
        //   325: aload           5
        //   327: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   330: invokestatic    android/util/Log.w:(Ljava/lang/String;Ljava/lang/String;)I
        //   333: pop            
        //   334: aload_2        
        //   335: ifnull          364
        //   338: aload_2        
        //   339: invokevirtual   java/io/FileOutputStream.close:()V
        //   342: return         
        //   343: ldc_w           "exception while closing file: "
        //   346: astore_1       
        //   347: ldc_w           "LVLDL"
        //   350: aload_1        
        //   351: aload_2        
        //   352: invokestatic    android/util/Log.w:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //   355: pop            
        //   356: return         
        //   357: ldc_w           "IOException while closing synced file: "
        //   360: astore_1       
        //   361: goto            347
        //   364: return         
        //   365: astore_1       
        //   366: aload_3        
        //   367: ifnull          399
        //   370: aload_3        
        //   371: invokevirtual   java/io/FileOutputStream.close:()V
        //   374: goto            399
        //   377: astore_2       
        //   378: ldc_w           "exception while closing file: "
        //   381: astore_3       
        //   382: goto            390
        //   385: astore_2       
        //   386: ldc_w           "IOException while closing synced file: "
        //   389: astore_3       
        //   390: ldc_w           "LVLDL"
        //   393: aload_3        
        //   394: aload_2        
        //   395: invokestatic    android/util/Log.w:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //   398: pop            
        //   399: aload_1        
        //   400: athrow         
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                           
        //  -----  -----  -----  -----  -------------------------------
        //  8      21     261    265    Ljava/io/FileNotFoundException;
        //  8      21     179    183    Ljava/io/SyncFailedException;
        //  8      21     96     101    Ljava/io/IOException;
        //  8      21     70     74     Ljava/lang/RuntimeException;
        //  8      21     66     70     Any
        //  23     30     61     66     Ljava/io/FileNotFoundException;
        //  23     30     56     61     Ljava/io/SyncFailedException;
        //  23     30     51     56     Ljava/io/IOException;
        //  23     30     43     51     Ljava/lang/RuntimeException;
        //  23     30     365    366    Any
        //  30     34     39     43     Ljava/io/IOException;
        //  30     34     35     39     Ljava/lang/RuntimeException;
        //  76     87     66     70     Any
        //  91     95     39     43     Ljava/io/IOException;
        //  91     95     35     39     Ljava/lang/RuntimeException;
        //  103    112    66     70     Any
        //  114    123    66     70     Any
        //  125    135    66     70     Any
        //  137    146    66     70     Any
        //  148    156    66     70     Any
        //  158    170    66     70     Any
        //  174    178    39     43     Ljava/io/IOException;
        //  174    178    35     39     Ljava/lang/RuntimeException;
        //  185    194    365    366    Any
        //  196    205    365    366    Any
        //  207    217    365    366    Any
        //  219    228    365    366    Any
        //  230    238    365    366    Any
        //  240    252    365    366    Any
        //  256    260    39     43     Ljava/io/IOException;
        //  256    260    35     39     Ljava/lang/RuntimeException;
        //  267    276    365    366    Any
        //  278    287    365    366    Any
        //  289    299    365    366    Any
        //  301    310    365    366    Any
        //  312    320    365    366    Any
        //  322    334    365    366    Any
        //  338    342    39     43     Ljava/io/IOException;
        //  338    342    35     39     Ljava/lang/RuntimeException;
        //  370    374    385    390    Ljava/io/IOException;
        //  370    374    377    385    Ljava/lang/RuntimeException;
        // 
        // The error that occurred was:
        // 
        // java.lang.UnsupportedOperationException
        //     at java.util.Collections$1.remove(Unknown Source)
        //     at java.util.AbstractCollection.removeAll(Unknown Source)
        //     at com.strobel.decompiler.ast.AstBuilder.convertLocalVariables(AstBuilder.java:2968)
        //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2445)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:108)
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
    
    private void transferData(final State state, final InnerState innerState, final byte[] array, final InputStream inputStream) throws StopRequest {
        while (true) {
            final int fromResponse = this.readFromResponse(state, innerState, array, inputStream);
            if (fromResponse == -1) {
                break;
            }
            state.mGotData = true;
            this.writeDataToDestination(state, array, fromResponse);
            innerState.mBytesSoFar += fromResponse;
            innerState.mBytesThisSession += fromResponse;
            this.reportProgress(state, innerState);
            this.checkPausedOrCanceled(state);
        }
        this.handleEndOfStream(state, innerState);
    }
    
    private void updateDatabaseFromHeaders(final State state, final InnerState innerState) {
        this.mInfo.mETag = innerState.mHeaderETag;
        this.mDB.updateDownload(this.mInfo);
    }
    
    private void updateDownloadDatabase(final int mStatus, final boolean b, final int mRetryAfter, final int mRedirectCount, final boolean b2, final String s) {
        this.mInfo.mStatus = mStatus;
        this.mInfo.mRetryAfter = mRetryAfter;
        this.mInfo.mRedirectCount = mRedirectCount;
        this.mInfo.mLastMod = System.currentTimeMillis();
        if (!b) {
            this.mInfo.mNumFailed = 0;
        }
        else if (b2) {
            this.mInfo.mNumFailed = 1;
        }
        else {
            final DownloadInfo mInfo = this.mInfo;
            ++mInfo.mNumFailed;
        }
        this.mDB.updateDownload(this.mInfo);
    }
    
    private String userAgent() {
        return this.mUserAgent;
    }
    
    private void writeDataToDestination(final State state, final byte[] array, final int n) throws StopRequest {
        try {
            if (state.mStream == null) {
                state.mStream = new FileOutputStream(state.mFilename, true);
            }
            state.mStream.write(array, 0, n);
            this.closeDestination(state);
        }
        catch (IOException ex) {
            if (!Helpers.isExternalMediaMounted()) {
                throw new StopRequest(499, "external media not mounted while writing destination file");
            }
            if (Helpers.getAvailableBytes(Helpers.getFilesystemRoot(state.mFilename)) < n) {
                throw new StopRequest(498, "insufficient space while writing destination file", ex);
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("while writing destination file: ");
            sb.append(ex.toString());
            throw new StopRequest(492, sb.toString(), ex);
        }
    }
    
    public void run() {
        Process.setThreadPriority(10);
        final State state = new State(this.mInfo, this.mService);
        Object o = null;
        Object o2;
        try {
            final PowerManager$WakeLock wakeLock = ((PowerManager)this.mContext.getSystemService("power")).newWakeLock(1, "LVLDL");
            try {
                wakeLock.acquire();
                int mFinalStatus = 0;
            Block_13_Outer:
                while (true) {
                    Label_0125: {
                        if (mFinalStatus != 0) {
                            break Label_0125;
                        }
                        o = new URL(state.mRequestUri).openConnection();
                        ((URLConnection)o).setRequestProperty("User-Agent", this.userAgent());
                        try {
                            this.executeDownload(state, (HttpURLConnection)o);
                            ((HttpURLConnection)o).disconnect();
                            mFinalStatus = 1;
                            continue;
                        }
                        catch (RetryDownload retryDownload) {
                            continue;
                        }
                        finally {
                            ((HttpURLConnection)o).disconnect();
                        }
                        try {
                            continue Block_13_Outer;
                            // iftrue(Label_0141:, wakeLock == null)
                        Label_0141:
                            while (true) {
                                wakeLock.release();
                                break Label_0141;
                                this.finalizeDestinationFile(state);
                                continue;
                            }
                            this.cleanupDestination(state, 200);
                            final boolean b = state.mCountRetry;
                            final int n = state.mRetryAfter;
                            final int n2 = state.mRedirectCount;
                            final boolean b2 = state.mGotData;
                            final String mFilename = state.mFilename;
                            mFinalStatus = 200;
                        }
                        catch (StopRequest stopRequest2) {}
                    }
                }
            }
            catch (StopRequest stopRequest3) {}
        }
        catch (StopRequest stopRequest) {
            o2 = null;
        }
        finally {
            o2 = null;
        }
        try {
            final StringBuilder sb = new StringBuilder();
            o = o2;
            sb.append("Exception for ");
            o = o2;
            sb.append(this.mInfo.mFileName);
            o = o2;
            sb.append(": ");
            o = o2;
            final StopRequest stopRequest;
            sb.append(stopRequest);
            o = o2;
            Log.w("LVLDL", sb.toString());
            if (o2 != null) {
                ((PowerManager$WakeLock)o2).release();
            }
            this.cleanupDestination(state, 491);
            boolean b = state.mCountRetry;
            int n = state.mRetryAfter;
            int n2 = state.mRedirectCount;
            boolean b2 = state.mGotData;
            o2 = state.mFilename;
            int mFinalStatus = 491;
            // iftrue(Label_0480:, o2 == null)
            while (true) {
            Block_21:
                while (true) {
                    this.notifyDownloadCompleted(mFinalStatus, b, n, n2, b2, (String)o2);
                    return;
                    o = o2;
                    final StringBuilder sb2 = new StringBuilder();
                    o = o2;
                    sb2.append("Aborting request for download ");
                    o = o2;
                    sb2.append(this.mInfo.mFileName);
                    o = o2;
                    sb2.append(": ");
                    o = o2;
                    sb2.append(stopRequest.getMessage());
                    o = o2;
                    Log.w("LVLDL", sb2.toString());
                    o = o2;
                    stopRequest.printStackTrace();
                    o = o2;
                    mFinalStatus = stopRequest.mFinalStatus;
                    break Block_21;
                    this.cleanupDestination(state, mFinalStatus);
                    b = state.mCountRetry;
                    n = state.mRetryAfter;
                    n2 = state.mRedirectCount;
                    b2 = state.mGotData;
                    o2 = state.mFilename;
                    continue;
                }
                ((PowerManager$WakeLock)o2).release();
                continue;
            }
        }
        finally {
            if (o != null) {
                ((PowerManager$WakeLock)o).release();
            }
            this.cleanupDestination(state, 491);
            this.notifyDownloadCompleted(491, state.mCountRetry, state.mRetryAfter, state.mRedirectCount, state.mGotData, state.mFilename);
        }
    }
    
    private class RetryDownload extends Throwable
    {
        private static final long serialVersionUID = 6196036036517540229L;
    }
    
    private class StopRequest extends Throwable
    {
        private static final long serialVersionUID = 6338592678988347973L;
        public int mFinalStatus;
        
        public StopRequest(final int mFinalStatus, final String s) {
            super(s);
            this.mFinalStatus = mFinalStatus;
        }
        
        public StopRequest(final int mFinalStatus, final String s, final Throwable t) {
            super(s, t);
            this.mFinalStatus = mFinalStatus;
        }
    }
    
    private static class InnerState
    {
        public int mBytesNotified;
        public int mBytesSoFar;
        public int mBytesThisSession;
        public boolean mContinuingDownload;
        public String mHeaderContentDisposition;
        public String mHeaderContentLength;
        public String mHeaderContentLocation;
        public String mHeaderETag;
        public long mTimeLastNotification;
        
        private InnerState() {
            this.mBytesSoFar = 0;
            this.mBytesThisSession = 0;
            this.mContinuingDownload = false;
            this.mBytesNotified = 0;
            this.mTimeLastNotification = 0L;
        }
    }
    
    private static class State
    {
        public boolean mCountRetry;
        public String mFilename;
        public boolean mGotData;
        public String mNewUri;
        public int mRedirectCount;
        public String mRequestUri;
        public int mRetryAfter;
        public FileOutputStream mStream;
        
        public State(final DownloadInfo downloadInfo, final DownloaderService downloaderService) {
            this.mCountRetry = false;
            this.mRetryAfter = 0;
            this.mRedirectCount = 0;
            this.mGotData = false;
            this.mRedirectCount = downloadInfo.mRedirectCount;
            this.mRequestUri = downloadInfo.mUri;
            this.mFilename = downloaderService.generateTempSaveFileName(downloadInfo.mFileName);
        }
    }
}
