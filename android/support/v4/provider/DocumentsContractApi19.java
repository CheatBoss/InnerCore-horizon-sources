package android.support.v4.provider;

import android.net.*;
import android.text.*;
import android.provider.*;
import android.util.*;
import android.content.*;
import android.database.*;

class DocumentsContractApi19
{
    private static final String TAG = "DocumentFile";
    
    public static boolean canRead(final Context context, final Uri uri) {
        if (context.checkCallingOrSelfUriPermission(uri, 1) == 0) {
            if (!TextUtils.isEmpty((CharSequence)getRawType(context, uri))) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean canWrite(final Context context, final Uri uri) {
        if (context.checkCallingOrSelfUriPermission(uri, 2) != 0) {
            return false;
        }
        final String rawType = getRawType(context, uri);
        final int queryForInt = queryForInt(context, uri, "flags", 0);
        if (!TextUtils.isEmpty((CharSequence)rawType)) {
            if ((queryForInt & 0x4) != 0x0) {
                return true;
            }
            if ("vnd.android.document/directory".equals(rawType) && (queryForInt & 0x8) != 0x0) {
                return true;
            }
            if (!TextUtils.isEmpty((CharSequence)rawType) && (queryForInt & 0x2) != 0x0) {
                return true;
            }
        }
        return false;
    }
    
    private static void closeQuietly(final AutoCloseable autoCloseable) {
        if (autoCloseable != null) {
            try {
                autoCloseable.close();
            }
            catch (Exception ex2) {}
            catch (RuntimeException ex) {
                throw ex;
            }
        }
    }
    
    public static boolean delete(final Context context, final Uri uri) {
        return DocumentsContract.deleteDocument(context.getContentResolver(), uri);
    }
    
    public static boolean exists(Context context, Uri query) {
        final ContentResolver contentResolver = context.getContentResolver();
        boolean b = true;
        final AutoCloseable autoCloseable = null;
        context = null;
        AutoCloseable autoCloseable2;
        try {
            try {
                query = (Uri)contentResolver.query(query, new String[] { "document_id" }, (String)null, (String[])null, (String)null);
                try {
                    if (((Cursor)query).getCount() <= 0) {
                        b = false;
                    }
                    closeQuietly((AutoCloseable)query);
                    return b;
                }
                catch (Exception contentResolver) {}
                finally {
                    context = (Context)query;
                }
            }
            finally {}
        }
        catch (Exception contentResolver) {
            autoCloseable2 = autoCloseable;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Failed query: ");
        sb.append(contentResolver);
        Log.w("DocumentFile", sb.toString());
        closeQuietly(autoCloseable2);
        return false;
        closeQuietly((AutoCloseable)context);
    }
    
    public static String getName(final Context context, final Uri uri) {
        return queryForString(context, uri, "_display_name", null);
    }
    
    private static String getRawType(final Context context, final Uri uri) {
        return queryForString(context, uri, "mime_type", null);
    }
    
    public static String getType(final Context context, final Uri uri) {
        String rawType;
        if ("vnd.android.document/directory".equals(rawType = getRawType(context, uri))) {
            rawType = null;
        }
        return rawType;
    }
    
    public static boolean isDirectory(final Context context, final Uri uri) {
        return "vnd.android.document/directory".equals(getRawType(context, uri));
    }
    
    public static boolean isDocumentUri(final Context context, final Uri uri) {
        return DocumentsContract.isDocumentUri(context, uri);
    }
    
    public static boolean isFile(final Context context, final Uri uri) {
        final String rawType = getRawType(context, uri);
        return !"vnd.android.document/directory".equals(rawType) && !TextUtils.isEmpty((CharSequence)rawType);
    }
    
    public static long lastModified(final Context context, final Uri uri) {
        return queryForLong(context, uri, "last_modified", 0L);
    }
    
    public static long length(final Context context, final Uri uri) {
        return queryForLong(context, uri, "_size", 0L);
    }
    
    private static int queryForInt(final Context context, final Uri uri, final String s, final int n) {
        return (int)queryForLong(context, uri, s, n);
    }
    
    private static long queryForLong(Context context, Uri query, final String ex, final long n) {
        final ContentResolver contentResolver = context.getContentResolver();
        final AutoCloseable autoCloseable = null;
        context = null;
        AutoCloseable autoCloseable2;
        try {
            try {
                query = (Uri)contentResolver.query(query, new String[] { (String)ex }, (String)null, (String[])null, (String)null);
                try {
                    if (((Cursor)query).moveToFirst() && !((Cursor)query).isNull(0)) {
                        final long long1 = ((Cursor)query).getLong(0);
                        closeQuietly((AutoCloseable)query);
                        return long1;
                    }
                    closeQuietly((AutoCloseable)query);
                    return n;
                }
                catch (Exception ex) {}
                finally {
                    context = (Context)query;
                }
            }
            finally {}
        }
        catch (Exception ex) {
            autoCloseable2 = autoCloseable;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Failed query: ");
        sb.append(ex);
        Log.w("DocumentFile", sb.toString());
        closeQuietly(autoCloseable2);
        return n;
        closeQuietly((AutoCloseable)context);
    }
    
    private static String queryForString(Context string, Uri query, final String ex, final String s) {
        final ContentResolver contentResolver = string.getContentResolver();
        final AutoCloseable autoCloseable = null;
        string = null;
        AutoCloseable autoCloseable2;
        try {
            try {
                query = (Uri)contentResolver.query(query, new String[] { (String)ex }, (String)null, (String[])null, (String)null);
                try {
                    if (((Cursor)query).moveToFirst() && !((Cursor)query).isNull(0)) {
                        string = (Context)((Cursor)query).getString(0);
                        closeQuietly((AutoCloseable)query);
                        return (String)string;
                    }
                    closeQuietly((AutoCloseable)query);
                    return s;
                }
                catch (Exception ex) {}
                finally {
                    string = (Context)query;
                }
            }
            finally {}
        }
        catch (Exception ex) {
            autoCloseable2 = autoCloseable;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Failed query: ");
        sb.append(ex);
        Log.w("DocumentFile", sb.toString());
        closeQuietly(autoCloseable2);
        return s;
        closeQuietly((AutoCloseable)string);
    }
}
