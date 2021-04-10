package com.mojang.minecraftpe;

import java.util.concurrent.*;
import android.util.*;
import java.text.*;
import org.json.*;
import org.apache.http.impl.client.*;
import org.apache.http.entity.mime.*;
import org.apache.http.entity.mime.content.*;
import org.apache.http.*;
import org.apache.http.client.methods.*;
import java.io.*;
import org.apache.http.entity.*;
import java.util.*;

public class CrashManager
{
    private static final int MAX_CONCURRENT_UPLOADS = 4;
    private String mCrashUploadURI;
    private SessionInfo mCurrentSession;
    private ConcurrentLinkedQueue<String> mDumpFileQueue;
    private String mDumpFilesPath;
    private String mExceptionUploadURI;
    private CrashManagerOwner mOwner;
    private Thread.UncaughtExceptionHandler mPreviousUncaughtExceptionHandler;
    private SentryEndpointConfig mSentryEndpointConfig;
    private String mSentrySessionParameters;
    private String mUserId;
    
    public CrashManager(final CrashManagerOwner mOwner, final String mDumpFilesPath, final String mUserId, final SentryEndpointConfig mSentryEndpointConfig, final SessionInfo mCurrentSession) {
        this.mDumpFileQueue = null;
        this.mOwner = null;
        this.mDumpFilesPath = null;
        this.mCrashUploadURI = null;
        this.mExceptionUploadURI = null;
        this.mUserId = null;
        this.mSentrySessionParameters = null;
        this.mSentryEndpointConfig = null;
        this.mCurrentSession = null;
        this.mPreviousUncaughtExceptionHandler = null;
        this.mOwner = mOwner;
        this.mDumpFilesPath = mDumpFilesPath;
        this.mUserId = mUserId;
        this.mSentryEndpointConfig = mSentryEndpointConfig;
        this.mCurrentSession = mCurrentSession;
        this.mDumpFileQueue = new ConcurrentLinkedQueue<String>();
        this.mSentrySessionParameters = this.getSentryParametersJSON(this.mCurrentSession);
        final StringBuilder sb = new StringBuilder();
        sb.append(this.mSentryEndpointConfig.url);
        sb.append("/api/");
        sb.append(this.mSentryEndpointConfig.projectId);
        sb.append("/minidump/?sentry_key=");
        sb.append(this.mSentryEndpointConfig.publicKey);
        this.mCrashUploadURI = sb.toString();
        final StringBuilder sb2 = new StringBuilder();
        sb2.append(this.mSentryEndpointConfig.url);
        sb2.append("/api/");
        sb2.append(this.mSentryEndpointConfig.projectId);
        sb2.append("/store/?sentry_version=7&sentry_key=");
        sb2.append(this.mSentryEndpointConfig.publicKey);
        this.mExceptionUploadURI = sb2.toString();
    }
    
    public static String createLogFile(String s, final String s2, final String s3, final String s4) {
        final Date date = new Date();
        try {
            final String string = UUID.randomUUID().toString();
            final StringBuilder sb = new StringBuilder();
            sb.append(s);
            sb.append("/");
            sb.append(string);
            sb.append(".faketrace");
            s = sb.toString();
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("CrashManager: Writing unhandled exception information to: ");
            sb2.append(s);
            Log.d("MCPE", sb2.toString());
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("CrashManager: Dump timestamp: ");
            sb3.append(s2);
            Log.d("MCPE", sb3.toString());
            final BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(s));
            final StringBuilder sb4 = new StringBuilder();
            sb4.append("Package: ");
            sb4.append(AppConstants.APP_PACKAGE);
            sb4.append("\n");
            bufferedWriter.write(sb4.toString());
            final StringBuilder sb5 = new StringBuilder();
            sb5.append("Version Code: ");
            sb5.append(String.valueOf(AppConstants.APP_VERSION));
            sb5.append("\n");
            bufferedWriter.write(sb5.toString());
            final StringBuilder sb6 = new StringBuilder();
            sb6.append("Version Name: ");
            sb6.append(AppConstants.APP_VERSION_NAME);
            sb6.append("\n");
            bufferedWriter.write(sb6.toString());
            final StringBuilder sb7 = new StringBuilder();
            sb7.append("Android: ");
            sb7.append(AppConstants.ANDROID_VERSION);
            sb7.append("\n");
            bufferedWriter.write(sb7.toString());
            final StringBuilder sb8 = new StringBuilder();
            sb8.append("Manufacturer: ");
            sb8.append(AppConstants.PHONE_MANUFACTURER);
            sb8.append("\n");
            bufferedWriter.write(sb8.toString());
            final StringBuilder sb9 = new StringBuilder();
            sb9.append("Model: ");
            sb9.append(AppConstants.PHONE_MODEL);
            sb9.append("\n");
            bufferedWriter.write(sb9.toString());
            final StringBuilder sb10 = new StringBuilder();
            sb10.append("DeviceId: ");
            sb10.append(s3);
            sb10.append("\n");
            bufferedWriter.write(sb10.toString());
            final StringBuilder sb11 = new StringBuilder();
            sb11.append("DeviceSessionId: ");
            sb11.append(s4);
            sb11.append("\n");
            bufferedWriter.write(sb11.toString());
            final StringBuilder sb12 = new StringBuilder();
            sb12.append("Dmp timestamp: ");
            sb12.append(s2);
            sb12.append("\n");
            bufferedWriter.write(sb12.toString());
            final StringBuilder sb13 = new StringBuilder();
            sb13.append("Upload Date: ");
            sb13.append(date);
            sb13.append("\n");
            bufferedWriter.write(sb13.toString());
            bufferedWriter.write("\n");
            bufferedWriter.write("MinidumpContainer");
            bufferedWriter.flush();
            bufferedWriter.close();
            final StringBuilder sb14 = new StringBuilder();
            sb14.append(string);
            sb14.append(".faketrace");
            s = sb14.toString();
            return s;
        }
        catch (Exception ex) {
            Log.w("MCPE", "CrashManager: failed to create accompanying log file");
            return null;
        }
    }
    
    private static void deleteWithLogging(final File file) {
        if (file.delete()) {
            final StringBuilder sb = new StringBuilder();
            sb.append("CrashManager: Deleted file ");
            sb.append(file.getName());
            Log.d("MCPE", sb.toString());
            return;
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("CrashManager: Couldn't delete file");
        sb2.append(file.getName());
        Log.w("MCPE", sb2.toString());
    }
    
    public static String formatTimestamp(final Date date) {
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return simpleDateFormat.format(date);
    }
    
    private static Date getFileTimestamp(final String s, final String s2) {
        final Date date = new Date();
        try {
            final StringBuilder sb = new StringBuilder();
            sb.append(s);
            sb.append("/");
            sb.append(s2);
            return new Date(new File(sb.toString()).lastModified());
        }
        catch (Exception ex) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("CrashManager: Error getting dump timestamp: ");
            sb2.append(s2);
            Log.w("MCPE", sb2.toString());
            ex.printStackTrace();
            return date;
        }
    }
    
    private static native String getSentryParameters(final String p0, final String p1, final String p2, final String p3, final String p4, final String p5, final int p6);
    
    private String getSentryParametersJSON(final SessionInfo sessionInfo) {
        return getSentryParameters(this.mOwner.getCachedDeviceId(this), sessionInfo.sessionId, sessionInfo.buildId, sessionInfo.commitId, sessionInfo.branchId, sessionInfo.flavor, sessionInfo.appVersion);
    }
    
    private void handlePreviousDumpsWorkerThread() {
        while (true) {
            final String s = this.mDumpFileQueue.poll();
            if (s == null) {
                break;
            }
            final File file = new File(this.mDumpFilesPath, s);
            boolean b = true;
            HttpResponse httpResponse;
            if (s.endsWith(".dmp")) {
                httpResponse = this.uploadMinidump(file, s);
            }
            else {
                httpResponse = this.uploadException(file, s);
            }
            Label_0389: {
                String s2;
                if (httpResponse != null) {
                    final int statusCode = httpResponse.getStatusLine().getStatusCode();
                    if (statusCode == 200) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Successfully uploaded dump file ");
                        sb.append(s);
                        Log.i("MCPE", sb.toString());
                        break Label_0389;
                    }
                    if (statusCode == 429) {
                        final Header firstHeader = httpResponse.getFirstHeader("Retry-After");
                        if (firstHeader != null) {
                            final int int1 = Integer.parseInt(firstHeader.getValue());
                            final StringBuilder sb2 = new StringBuilder();
                            sb2.append("Received Too Many Requests response, retrying after ");
                            sb2.append(int1);
                            sb2.append("s");
                            Log.w("MCPE", sb2.toString());
                            final long n = int1 * 1000;
                            try {
                                Thread.sleep(n);
                                this.mDumpFileQueue.add(s);
                            }
                            catch (InterruptedException ex) {}
                            b = false;
                            break Label_0389;
                        }
                        final StringBuilder sb3 = new StringBuilder();
                        sb3.append("Received Too Many Requests response with no Retry-After header, so dropping event ");
                        sb3.append(s);
                        Log.w("MCPE", sb3.toString());
                        break Label_0389;
                    }
                    else {
                        final StringBuilder sb4 = new StringBuilder();
                        sb4.append("Unrecognied HTTP response: \"");
                        sb4.append(httpResponse.getStatusLine());
                        sb4.append("\", dropping event ");
                        sb4.append(s);
                        s2 = sb4.toString();
                    }
                }
                else {
                    final StringBuilder sb5 = new StringBuilder();
                    sb5.append("An error occurred uploading an event; dropping event ");
                    sb5.append(s);
                    s2 = sb5.toString();
                }
                Log.e("MCPE", s2);
            }
            if (!b) {
                continue;
            }
            deleteWithLogging(file);
        }
    }
    
    private void handleUncaughtException(final Thread thread, final Throwable t) {
        Thread.setDefaultUncaughtExceptionHandler(this.mPreviousUncaughtExceptionHandler);
        Log.e("MCPE", "In handleUncaughtException()");
        Label_0553: {
            StringBuilder sb3;
            String s;
            try {
                final JSONObject jsonObject = new JSONObject(this.mSentrySessionParameters);
                final String replaceAll = UUID.randomUUID().toString().toLowerCase().replaceAll("-", "");
                final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                final String format = simpleDateFormat.format(new Date());
                jsonObject.put("event_id", (Object)replaceAll);
                jsonObject.put("timestamp", (Object)format);
                jsonObject.put("logger", (Object)"na");
                jsonObject.put("platform", (Object)"java");
                final JSONObject jsonObject2 = new JSONObject();
                jsonObject2.put("type", (Object)t.getClass().getName());
                jsonObject2.put("value", (Object)t.getMessage());
                final JSONObject jsonObject3 = new JSONObject();
                final JSONArray jsonArray = new JSONArray();
                final StackTraceElement[] stackTrace = t.getStackTrace();
                int length = stackTrace.length;
                while (true) {
                    --length;
                    if (length < 0) {
                        break;
                    }
                    final StackTraceElement stackTraceElement = stackTrace[length];
                    final JSONObject jsonObject4 = new JSONObject();
                    jsonObject4.put("filename", (Object)stackTraceElement.getFileName());
                    jsonObject4.put("function", (Object)stackTraceElement.getMethodName());
                    jsonObject4.put("module", (Object)stackTraceElement.getClassName());
                    jsonObject4.put("in_app", stackTraceElement.getClassName().startsWith("com.mojang"));
                    if (stackTraceElement.getLineNumber() > 0) {
                        jsonObject4.put("lineno", stackTraceElement.getLineNumber());
                    }
                    jsonArray.put((Object)jsonObject4);
                }
                jsonObject3.put("frames", (Object)jsonArray);
                jsonObject2.put("stacktrace", (Object)jsonObject3);
                jsonObject.put("exception", (Object)jsonObject2);
                final StringBuilder sb = new StringBuilder();
                sb.append(this.mDumpFilesPath);
                sb.append("/");
                sb.append(this.mCurrentSession.sessionId);
                sb.append(".except");
                final String string = sb.toString();
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("CrashManager: Writing unhandled exception information to: ");
                sb2.append(string);
                Log.d("MCPE", sb2.toString());
                final OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(string));
                outputStreamWriter.write(jsonObject.toString(4));
                outputStreamWriter.close();
                break Label_0553;
            }
            catch (IOException ex) {
                sb3 = new StringBuilder();
                sb3.append("IO exception: ");
                s = ex.toString();
            }
            catch (JSONException ex2) {
                sb3 = new StringBuilder();
                sb3.append("JSON exception: ");
                s = ex2.toString();
            }
            sb3.append(s);
            Log.e("MCPE", sb3.toString());
        }
        this.mPreviousUncaughtExceptionHandler.uncaughtException(thread, t);
    }
    
    private static String[] searchForDumpFiles(final String s, final String s2) {
        if (s == null) {
            Log.e("MCPE", "CrashManager: Can't search for exception as file path is null.");
            return new String[0];
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("CrashManager: Searching for dump files in ");
        sb.append(s);
        Log.d("MCPE", sb.toString());
        final StringBuilder sb2 = new StringBuilder();
        sb2.append(s);
        sb2.append("/");
        final File file = new File(sb2.toString());
        if (!file.mkdir() && !file.exists()) {
            return new String[0];
        }
        return file.list(new FilenameFilter() {
            @Override
            public boolean accept(final File file, final String s) {
                return s.endsWith(s2);
            }
        });
    }
    
    public static HttpResponse uploadDumpAndLog(final File file, String ex, String s, final String s2, final String s3, final String s4, final String s5, final String s6, final String s7) {
        s = (String)new File(s, s3);
        HttpResponse httpResponse;
        try {
            try {
                final StringBuilder sb = new StringBuilder();
                sb.append("CrashManager: uploading ");
                sb.append(s2);
                Log.i("MCPE", sb.toString());
                final DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
                ex = (Exception)new HttpPost((String)ex);
                final MultipartEntity entity = new MultipartEntity();
                entity.addPart("upload_file_minidump", (ContentBody)new FileBody(file));
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("CrashManager: sentry parameters: ");
                sb2.append(s7);
                Log.d("MCPE", sb2.toString());
                entity.addPart("sentry", (ContentBody)new StringBody(s7));
                entity.addPart("log", (ContentBody)new FileBody((File)s));
                ((HttpPost)ex).setEntity((HttpEntity)entity);
                defaultHttpClient.execute((HttpUriRequest)ex);
                try {
                    ex = (Exception)new StringBuilder();
                    ((StringBuilder)ex).append("CrashManager: Executed dump file upload with no exception: ");
                    ((StringBuilder)ex).append(s2);
                    Log.d("MCPE", ((StringBuilder)ex).toString());
                }
                catch (Exception ex) {}
            }
            finally {}
        }
        catch (Exception ex) {
            httpResponse = null;
        }
        final StringBuilder sb3 = new StringBuilder();
        sb3.append("CrashManager: Error uploading dump file: ");
        sb3.append(s2);
        Log.w("MCPE", sb3.toString());
        ex.printStackTrace();
        deleteWithLogging((File)s);
        return httpResponse;
        deleteWithLogging((File)s);
    }
    
    private HttpResponse uploadException(final File file, String line) {
        try {
            final StringBuilder sb = new StringBuilder();
            sb.append("CrashManager: reading exception file at ");
            sb.append(line);
            Log.i("MCPE", sb.toString());
            line = "";
            final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String string = line;
            while (true) {
                line = bufferedReader.readLine();
                if (line == null) {
                    break;
                }
                final StringBuilder sb2 = new StringBuilder();
                sb2.append(string);
                sb2.append(line);
                sb2.append("\n");
                string = sb2.toString();
            }
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("Sending exception by HTTP to ");
            sb3.append(this.mExceptionUploadURI);
            Log.i("MCPE", sb3.toString());
            final DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
            final HttpPost httpPost = new HttpPost(this.mExceptionUploadURI);
            httpPost.setEntity((HttpEntity)new StringEntity(string));
            return defaultHttpClient.execute((HttpUriRequest)httpPost);
        }
        catch (Exception ex) {
            final StringBuilder sb4 = new StringBuilder();
            sb4.append("CrashManager: Error uploading exception: ");
            sb4.append(ex.toString());
            Log.w("MCPE", sb4.toString());
            ex.printStackTrace();
            return null;
        }
    }
    
    private HttpResponse uploadMinidump(final File file, final String s) {
        final StringBuilder sb = new StringBuilder();
        sb.append("CrashManager: Located this dump file: ");
        sb.append(s);
        Log.d("MCPE", sb.toString());
        final String replace = s.replace(".dmp", "");
        final Date fileTimestamp = getFileTimestamp(this.mDumpFilesPath, s);
        final SessionInfo sessionInfoForCrash = this.mOwner.findSessionInfoForCrash(this, replace);
        final HttpResponse httpResponse = null;
        if (sessionInfoForCrash != null) {
            final String logFile = createLogFile(this.mDumpFilesPath, formatTimestamp(fileTimestamp), this.mUserId, sessionInfoForCrash.sessionId);
            HttpResponse uploadDumpAndLog;
            if (logFile != null) {
                uploadDumpAndLog = uploadDumpAndLog(file, this.mCrashUploadURI, this.mDumpFilesPath, s, logFile, sessionInfoForCrash.gameVersionName, this.mUserId, sessionInfoForCrash.sessionId, this.getSentryParametersJSON(sessionInfoForCrash));
            }
            else {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("CrashManager: Could not generate log file for previously crashed session ");
                sb2.append(replace);
                Log.e("MCPE", sb2.toString());
                uploadDumpAndLog = httpResponse;
            }
            sessionInfoForCrash.crashTimestamp = fileTimestamp;
            this.mOwner.notifyCrashUploadCompleted(this, sessionInfoForCrash);
            return uploadDumpAndLog;
        }
        final StringBuilder sb3 = new StringBuilder();
        sb3.append("CrashManager: Could not locate session information for previously crashed session ");
        sb3.append(replace);
        Log.e("MCPE", sb3.toString());
        return null;
    }
    
    public void handlePreviousDumps() {
        final StringBuilder sb = new StringBuilder();
        sb.append("CrashManager: handlePreviousDumps: Device ID: ");
        sb.append(this.mUserId);
        Log.d("MCPE", sb.toString());
        this.mDumpFileQueue.addAll(Arrays.asList(searchForDumpFiles(this.mDumpFilesPath, ".dmp")));
        this.mDumpFileQueue.addAll(Arrays.asList(searchForDumpFiles(this.mDumpFilesPath, ".except")));
        for (int min = Math.min(this.mDumpFileQueue.size(), 4), i = 0; i < min; ++i) {
            new Thread() {
                @Override
                public void run() {
                    CrashManager.this.handlePreviousDumpsWorkerThread();
                }
            }.start();
        }
    }
    
    public void installGlobalExceptionHandler() {
        this.mPreviousUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler((Thread.UncaughtExceptionHandler)new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(final Thread thread, final Throwable t) {
                CrashManager.this.handleUncaughtException(thread, t);
            }
        });
    }
}
