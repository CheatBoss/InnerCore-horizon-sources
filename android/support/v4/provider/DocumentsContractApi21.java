package android.support.v4.provider;

import android.net.*;
import android.provider.*;
import java.util.*;
import android.util.*;
import android.content.*;
import android.database.*;

class DocumentsContractApi21
{
    private static final String TAG = "DocumentFile";
    
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
    
    public static Uri createDirectory(final Context context, final Uri uri, final String s) {
        return createFile(context, uri, "vnd.android.document/directory", s);
    }
    
    public static Uri createFile(final Context context, final Uri uri, final String s, final String s2) {
        return DocumentsContract.createDocument(context.getContentResolver(), uri, s, s2);
    }
    
    public static Uri[] listFiles(Context ex, final Uri uri) {
        Object o = ((Context)ex).getContentResolver();
        final Uri buildChildDocumentsUriUsingTree = DocumentsContract.buildChildDocumentsUriUsingTree(uri, DocumentsContract.getDocumentId(uri));
        final ArrayList<Uri> list = new ArrayList<Uri>();
        final AutoCloseable autoCloseable = null;
        ex = null;
        AutoCloseable autoCloseable2;
        try {
            try {
                o = ((ContentResolver)o).query(buildChildDocumentsUriUsingTree, new String[] { "document_id" }, (String)null, (String[])null, (String)null);
                try {
                    while (((Cursor)o).moveToNext()) {
                        list.add(DocumentsContract.buildDocumentUriUsingTree(uri, ((Cursor)o).getString(0)));
                    }
                    closeQuietly((AutoCloseable)o);
                }
                catch (Exception ex) {}
                finally {
                    ex = (Exception)o;
                }
            }
            finally {}
        }
        catch (Exception o) {
            autoCloseable2 = autoCloseable;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Failed query: ");
        sb.append(o);
        Log.w("DocumentFile", sb.toString());
        closeQuietly(autoCloseable2);
        return list.toArray(new Uri[list.size()]);
        closeQuietly((AutoCloseable)ex);
    }
    
    public static Uri prepareTreeUri(final Uri uri) {
        return DocumentsContract.buildDocumentUriUsingTree(uri, DocumentsContract.getTreeDocumentId(uri));
    }
    
    public static Uri renameTo(final Context context, final Uri uri, final String s) {
        return DocumentsContract.renameDocument(context.getContentResolver(), uri, s);
    }
}
