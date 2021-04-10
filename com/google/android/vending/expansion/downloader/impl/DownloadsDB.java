package com.google.android.vending.expansion.downloader.impl;

import android.database.*;
import android.content.*;
import android.provider.*;
import android.database.sqlite.*;
import android.util.*;

public class DownloadsDB
{
    private static final int CONTROL_IDX = 7;
    private static final int CURRENTBYTES_IDX = 4;
    private static final String DATABASE_NAME = "DownloadsDB";
    private static final int DATABASE_VERSION = 7;
    private static final String[] DC_PROJECTION;
    private static final int ETAG_IDX = 2;
    private static final int FILENAME_IDX = 0;
    private static final int INDEX_IDX = 11;
    private static final int LASTMOD_IDX = 5;
    public static final String LOG_TAG;
    private static final int NUM_FAILED_IDX = 8;
    private static final int REDIRECT_COUNT_IDX = 10;
    private static final int RETRY_AFTER_IDX = 9;
    private static final int STATUS_IDX = 6;
    private static final int TOTALBYTES_IDX = 3;
    private static final int URI_IDX = 1;
    private static DownloadsDB mDownloadsDB;
    int mFlags;
    SQLiteStatement mGetDownloadByIndex;
    final SQLiteOpenHelper mHelper;
    long mMetadataRowID;
    int mStatus;
    SQLiteStatement mUpdateCurrentBytes;
    int mVersionCode;
    
    static {
        LOG_TAG = DownloadsDB.class.getName();
        DC_PROJECTION = new String[] { "FN", "URI", "ETAG", "TOTALBYTES", "CURRENTBYTES", "LASTMOD", "STATUS", "CONTROL", "FAILCOUNT", "RETRYAFTER", "REDIRECTCOUNT", "FILEIDX" };
    }
    
    private DownloadsDB(final Context context) {
        this.mMetadataRowID = -1L;
        this.mVersionCode = -1;
        this.mStatus = -1;
        final DownloadsContentDBHelper mHelper = new DownloadsContentDBHelper(context);
        this.mHelper = mHelper;
        final Cursor rawQuery = mHelper.getReadableDatabase().rawQuery("SELECT APKVERSION,_id,DOWNLOADSTATUS,DOWNLOADFLAGS FROM MetadataColumns LIMIT 1", (String[])null);
        if (rawQuery != null && rawQuery.moveToFirst()) {
            this.mVersionCode = rawQuery.getInt(0);
            this.mMetadataRowID = rawQuery.getLong(1);
            this.mStatus = rawQuery.getInt(2);
            this.mFlags = rawQuery.getInt(3);
            rawQuery.close();
        }
        DownloadsDB.mDownloadsDB = this;
    }
    
    public static DownloadsDB getDB(final Context context) {
        synchronized (DownloadsDB.class) {
            if (DownloadsDB.mDownloadsDB == null) {
                return new DownloadsDB(context);
            }
            return DownloadsDB.mDownloadsDB;
        }
    }
    
    private SQLiteStatement getDownloadByIndexStatement() {
        if (this.mGetDownloadByIndex == null) {
            this.mGetDownloadByIndex = this.mHelper.getReadableDatabase().compileStatement("SELECT _id FROM DownloadColumns WHERE FILEIDX = ?");
        }
        return this.mGetDownloadByIndex;
    }
    
    private SQLiteStatement getUpdateCurrentBytesStatement() {
        if (this.mUpdateCurrentBytes == null) {
            this.mUpdateCurrentBytes = this.mHelper.getReadableDatabase().compileStatement("UPDATE DownloadColumns SET CURRENTBYTES = ? WHERE FILEIDX = ?");
        }
        return this.mUpdateCurrentBytes;
    }
    
    public void close() {
        this.mHelper.close();
    }
    
    protected DownloadInfo getDownloadInfoByFileName(final String s) {
        final SQLiteDatabase readableDatabase = this.mHelper.getReadableDatabase();
        Cursor cursor = null;
        Label_0081: {
            try {
                final Cursor query = readableDatabase.query("DownloadColumns", DownloadsDB.DC_PROJECTION, "FN = ?", new String[] { s }, (String)null, (String)null, (String)null);
                if (query != null) {
                    try {
                        if (query.moveToFirst()) {
                            final DownloadInfo downloadInfoFromCursor = this.getDownloadInfoFromCursor(query);
                            if (query != null) {
                                query.close();
                            }
                            return downloadInfoFromCursor;
                        }
                    }
                    finally {
                        break Label_0081;
                    }
                }
                if (query != null) {
                    query.close();
                }
                return null;
            }
            finally {
                cursor = null;
            }
        }
        if (cursor != null) {
            cursor.close();
        }
    }
    
    public DownloadInfo getDownloadInfoFromCursor(final Cursor cursor) {
        final DownloadInfo downloadInfo = new DownloadInfo(cursor.getInt(11), cursor.getString(0), this.getClass().getPackage().getName());
        this.setDownloadInfoFromCursor(downloadInfo, cursor);
        return downloadInfo;
    }
    
    public DownloadInfo[] getDownloads() {
        final SQLiteDatabase readableDatabase = this.mHelper.getReadableDatabase();
        Cursor cursor = null;
        try {
            final Cursor query = readableDatabase.query("DownloadColumns", DownloadsDB.DC_PROJECTION, (String)null, (String[])null, (String)null, (String)null, (String)null);
            if (query != null) {
                try {
                    if (query.moveToFirst()) {
                        final DownloadInfo[] array = new DownloadInfo[query.getCount()];
                        int n = 0;
                        while (true) {
                            array[n] = this.getDownloadInfoFromCursor(query);
                            if (!query.moveToNext()) {
                                break;
                            }
                            ++n;
                        }
                        if (query != null) {
                            query.close();
                        }
                        return array;
                    }
                }
                finally {
                    cursor = query;
                }
            }
            if (query != null) {
                query.close();
            }
            return null;
        }
        finally {}
        if (cursor != null) {
            cursor.close();
        }
    }
    
    public int getFlags() {
        return this.mFlags;
    }
    
    public long getIDByIndex(final int n) {
        final SQLiteStatement downloadByIndexStatement = this.getDownloadByIndexStatement();
        downloadByIndexStatement.clearBindings();
        downloadByIndexStatement.bindLong(1, (long)n);
        try {
            return downloadByIndexStatement.simpleQueryForLong();
        }
        catch (SQLiteDoneException ex) {
            return -1L;
        }
    }
    
    public long getIDForDownloadInfo(final DownloadInfo downloadInfo) {
        return this.getIDByIndex(downloadInfo.mIndex);
    }
    
    public int getLastCheckedVersionCode() {
        return this.mVersionCode;
    }
    
    public boolean isDownloadRequired() {
        final Cursor rawQuery = this.mHelper.getReadableDatabase().rawQuery("SELECT Count(*) FROM DownloadColumns WHERE STATUS <> 0", (String[])null);
        boolean b = true;
        if (rawQuery != null) {
            try {
                if (rawQuery.moveToFirst()) {
                    if (rawQuery.getInt(0) != 0) {
                        b = false;
                    }
                    return b;
                }
            }
            finally {
                if (rawQuery != null) {
                    rawQuery.close();
                }
            }
        }
        if (rawQuery != null) {
            rawQuery.close();
        }
        return true;
    }
    
    public void setDownloadInfoFromCursor(final DownloadInfo downloadInfo, final Cursor cursor) {
        downloadInfo.mUri = cursor.getString(1);
        downloadInfo.mETag = cursor.getString(2);
        downloadInfo.mTotalBytes = cursor.getLong(3);
        downloadInfo.mCurrentBytes = cursor.getLong(4);
        downloadInfo.mLastMod = cursor.getLong(5);
        downloadInfo.mStatus = cursor.getInt(6);
        downloadInfo.mControl = cursor.getInt(7);
        downloadInfo.mNumFailed = cursor.getInt(8);
        downloadInfo.mRetryAfter = cursor.getInt(9);
        downloadInfo.mRedirectCount = cursor.getInt(10);
    }
    
    public boolean updateDownload(final DownloadInfo downloadInfo) {
        final ContentValues contentValues = new ContentValues();
        contentValues.put("FILEIDX", Integer.valueOf(downloadInfo.mIndex));
        contentValues.put("FN", downloadInfo.mFileName);
        contentValues.put("URI", downloadInfo.mUri);
        contentValues.put("ETAG", downloadInfo.mETag);
        contentValues.put("TOTALBYTES", Long.valueOf(downloadInfo.mTotalBytes));
        contentValues.put("CURRENTBYTES", Long.valueOf(downloadInfo.mCurrentBytes));
        contentValues.put("LASTMOD", Long.valueOf(downloadInfo.mLastMod));
        contentValues.put("STATUS", Integer.valueOf(downloadInfo.mStatus));
        contentValues.put("CONTROL", Integer.valueOf(downloadInfo.mControl));
        contentValues.put("FAILCOUNT", Integer.valueOf(downloadInfo.mNumFailed));
        contentValues.put("RETRYAFTER", Integer.valueOf(downloadInfo.mRetryAfter));
        contentValues.put("REDIRECTCOUNT", Integer.valueOf(downloadInfo.mRedirectCount));
        return this.updateDownload(downloadInfo, contentValues);
    }
    
    public boolean updateDownload(final DownloadInfo downloadInfo, final ContentValues contentValues) {
        long idForDownloadInfo;
        if (downloadInfo == null) {
            idForDownloadInfo = -1L;
        }
        else {
            idForDownloadInfo = this.getIDForDownloadInfo(downloadInfo);
        }
        boolean b = false;
        try {
            final SQLiteDatabase writableDatabase = this.mHelper.getWritableDatabase();
            if (idForDownloadInfo == -1L) {
                if (-1L != writableDatabase.insert("DownloadColumns", "URI", contentValues)) {
                    b = true;
                }
                return b;
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("DownloadColumns._id = ");
            sb.append(idForDownloadInfo);
            if (1 != writableDatabase.update("DownloadColumns", contentValues, sb.toString(), (String[])null)) {
                return false;
            }
        }
        catch (SQLiteException ex) {
            ex.printStackTrace();
        }
        return false;
    }
    
    public void updateDownloadCurrentBytes(final DownloadInfo downloadInfo) {
        final SQLiteStatement updateCurrentBytesStatement = this.getUpdateCurrentBytesStatement();
        updateCurrentBytesStatement.clearBindings();
        updateCurrentBytesStatement.bindLong(1, downloadInfo.mCurrentBytes);
        updateCurrentBytesStatement.bindLong(2, (long)downloadInfo.mIndex);
        updateCurrentBytesStatement.execute();
    }
    
    public boolean updateFlags(final int mFlags) {
        if (this.mFlags == mFlags) {
            return true;
        }
        final ContentValues contentValues = new ContentValues();
        contentValues.put("DOWNLOADFLAGS", Integer.valueOf(mFlags));
        if (this.updateMetadata(contentValues)) {
            this.mFlags = mFlags;
            return true;
        }
        return false;
    }
    
    public boolean updateFromDb(final DownloadInfo downloadInfo) {
        final SQLiteDatabase readableDatabase = this.mHelper.getReadableDatabase();
        final Cursor cursor = null;
        Cursor cursor2 = null;
        Label_0091: {
            try {
                final Cursor query = readableDatabase.query("DownloadColumns", DownloadsDB.DC_PROJECTION, "FN= ?", new String[] { downloadInfo.mFileName }, (String)null, (String)null, (String)null);
                if (query != null) {
                    try {
                        if (query.moveToFirst()) {
                            this.setDownloadInfoFromCursor(downloadInfo, query);
                            if (query != null) {
                                query.close();
                            }
                            return true;
                        }
                    }
                    finally {
                        break Label_0091;
                    }
                }
                if (query != null) {
                    query.close();
                }
                return false;
            }
            finally {
                cursor2 = cursor;
            }
        }
        if (cursor2 != null) {
            cursor2.close();
        }
    }
    
    public boolean updateMetadata(final int mVersionCode, final int mStatus) {
        final ContentValues contentValues = new ContentValues();
        contentValues.put("APKVERSION", Integer.valueOf(mVersionCode));
        contentValues.put("DOWNLOADSTATUS", Integer.valueOf(mStatus));
        if (this.updateMetadata(contentValues)) {
            this.mVersionCode = mVersionCode;
            this.mStatus = mStatus;
            return true;
        }
        return false;
    }
    
    public boolean updateMetadata(final ContentValues contentValues) {
        final SQLiteDatabase writableDatabase = this.mHelper.getWritableDatabase();
        if (-1L == this.mMetadataRowID) {
            final long insert = writableDatabase.insert("MetadataColumns", "APKVERSION", contentValues);
            if (-1L == insert) {
                return false;
            }
            this.mMetadataRowID = insert;
        }
        else {
            final StringBuilder sb = new StringBuilder();
            sb.append("_id = ");
            sb.append(this.mMetadataRowID);
            if (writableDatabase.update("MetadataColumns", contentValues, sb.toString(), (String[])null) == 0) {
                return false;
            }
        }
        return true;
    }
    
    public boolean updateStatus(final int mStatus) {
        if (this.mStatus == mStatus) {
            return true;
        }
        final ContentValues contentValues = new ContentValues();
        contentValues.put("DOWNLOADSTATUS", Integer.valueOf(mStatus));
        if (this.updateMetadata(contentValues)) {
            this.mStatus = mStatus;
            return true;
        }
        return false;
    }
    
    public static class DownloadColumns implements BaseColumns
    {
        public static final String CONTROL = "CONTROL";
        public static final String CURRENTBYTES = "CURRENTBYTES";
        public static final String ETAG = "ETAG";
        public static final String FILENAME = "FN";
        public static final String INDEX = "FILEIDX";
        public static final String LASTMOD = "LASTMOD";
        public static final String NUM_FAILED = "FAILCOUNT";
        public static final String REDIRECT_COUNT = "REDIRECTCOUNT";
        public static final String RETRY_AFTER = "RETRYAFTER";
        public static final String[][] SCHEMA;
        public static final String STATUS = "STATUS";
        public static final String TABLE_NAME = "DownloadColumns";
        public static final String TOTALBYTES = "TOTALBYTES";
        public static final String URI = "URI";
        public static final String _ID = "DownloadColumns._id";
        
        static {
            SCHEMA = new String[][] { { "_id", "INTEGER PRIMARY KEY" }, { "FILEIDX", "INTEGER UNIQUE" }, { "URI", "TEXT" }, { "FN", "TEXT UNIQUE" }, { "ETAG", "TEXT" }, { "TOTALBYTES", "INTEGER" }, { "CURRENTBYTES", "INTEGER" }, { "LASTMOD", "INTEGER" }, { "STATUS", "INTEGER" }, { "CONTROL", "INTEGER" }, { "FAILCOUNT", "INTEGER" }, { "RETRYAFTER", "INTEGER" }, { "REDIRECTCOUNT", "INTEGER" } };
        }
    }
    
    protected static class DownloadsContentDBHelper extends SQLiteOpenHelper
    {
        private static final String[][][] sSchemas;
        private static final String[] sTables;
        
        static {
            sSchemas = new String[][][] { DownloadColumns.SCHEMA, MetadataColumns.SCHEMA };
            sTables = new String[] { "DownloadColumns", "MetadataColumns" };
        }
        
        DownloadsContentDBHelper(final Context context) {
            super(context, "DownloadsDB", (SQLiteDatabase$CursorFactory)null, 7);
        }
        
        private String createTableQueryFromArray(final String s, final String[][] array) {
            final StringBuilder sb = new StringBuilder();
            sb.append("CREATE TABLE ");
            sb.append(s);
            sb.append(" (");
            for (int length = array.length, i = 0; i < length; ++i) {
                final String[] array2 = array[i];
                sb.append(' ');
                sb.append(array2[0]);
                sb.append(' ');
                sb.append(array2[1]);
                sb.append(',');
            }
            sb.setLength(sb.length() - 1);
            sb.append(");");
            return sb.toString();
        }
        
        private void dropTables(final SQLiteDatabase sqLiteDatabase) {
            final String[] sTables = DownloadsContentDBHelper.sTables;
            for (int length = sTables.length, i = 0; i < length; ++i) {
                final String s = sTables[i];
                try {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("DROP TABLE IF EXISTS ");
                    sb.append(s);
                    sqLiteDatabase.execSQL(sb.toString());
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        
        public void onCreate(final SQLiteDatabase sqLiteDatabase) {
            final int length = DownloadsContentDBHelper.sSchemas.length;
            int i = 0;
            while (i < length) {
                try {
                    sqLiteDatabase.execSQL(this.createTableQueryFromArray(DownloadsContentDBHelper.sTables[i], DownloadsContentDBHelper.sSchemas[i]));
                    ++i;
                    continue;
                }
                catch (Exception ex) {
                    while (true) {
                        ex.printStackTrace();
                    }
                }
                break;
            }
        }
        
        public void onUpgrade(final SQLiteDatabase sqLiteDatabase, final int n, final int n2) {
            final String name = DownloadsContentDBHelper.class.getName();
            final StringBuilder sb = new StringBuilder();
            sb.append("Upgrading database from version ");
            sb.append(n);
            sb.append(" to ");
            sb.append(n2);
            sb.append(", which will destroy all old data");
            Log.w(name, sb.toString());
            this.dropTables(sqLiteDatabase);
            this.onCreate(sqLiteDatabase);
        }
    }
    
    public static class MetadataColumns implements BaseColumns
    {
        public static final String APKVERSION = "APKVERSION";
        public static final String DOWNLOAD_STATUS = "DOWNLOADSTATUS";
        public static final String FLAGS = "DOWNLOADFLAGS";
        public static final String[][] SCHEMA;
        public static final String TABLE_NAME = "MetadataColumns";
        public static final String _ID = "MetadataColumns._id";
        
        static {
            SCHEMA = new String[][] { { "_id", "INTEGER PRIMARY KEY" }, { "APKVERSION", "INTEGER" }, { "DOWNLOADSTATUS", "INTEGER" }, { "DOWNLOADFLAGS", "INTEGER" } };
        }
    }
}
