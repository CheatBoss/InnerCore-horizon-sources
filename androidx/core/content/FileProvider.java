package androidx.core.content;

import org.xmlpull.v1.*;
import android.content.pm.*;
import android.content.res.*;
import androidx.annotation.*;
import android.webkit.*;
import android.content.*;
import android.os.*;
import java.io.*;
import android.database.*;
import android.text.*;
import android.net.*;
import java.util.*;

public class FileProvider extends ContentProvider
{
    private static final String ATTR_NAME = "name";
    private static final String ATTR_PATH = "path";
    private static final String[] COLUMNS;
    private static final File DEVICE_ROOT;
    private static final String META_DATA_FILE_PROVIDER_PATHS = "android.support.FILE_PROVIDER_PATHS";
    private static final String TAG_CACHE_PATH = "cache-path";
    private static final String TAG_EXTERNAL = "external-path";
    private static final String TAG_EXTERNAL_CACHE = "external-cache-path";
    private static final String TAG_EXTERNAL_FILES = "external-files-path";
    private static final String TAG_EXTERNAL_MEDIA = "external-media-path";
    private static final String TAG_FILES_PATH = "files-path";
    private static final String TAG_ROOT_PATH = "root-path";
    @GuardedBy("sCache")
    private static HashMap<String, PathStrategy> sCache;
    private PathStrategy mStrategy;
    
    static {
        COLUMNS = new String[] { "_display_name", "_size" };
        DEVICE_ROOT = new File("/");
        FileProvider.sCache = new HashMap<String, PathStrategy>();
    }
    
    private static File buildPath(File file, final String... array) {
        File file2;
        for (int length = array.length, i = 0; i < length; ++i, file = file2) {
            final String s = array[i];
            file2 = file;
            if (s != null) {
                file2 = new File(file, s);
            }
        }
        return file;
    }
    
    private static Object[] copyOf(final Object[] array, final int n) {
        final Object[] array2 = new Object[n];
        System.arraycopy(array, 0, array2, 0, n);
        return array2;
    }
    
    private static String[] copyOf(final String[] array, final int n) {
        final String[] array2 = new String[n];
        System.arraycopy(array, 0, array2, 0, n);
        return array2;
    }
    
    private static PathStrategy getPathStrategy(final Context context, final String s) {
        synchronized (FileProvider.sCache) {
            PathStrategy pathStrategy;
            if ((pathStrategy = FileProvider.sCache.get(s)) == null) {
                try {
                    pathStrategy = parsePathStrategy(context, s);
                    FileProvider.sCache.put(s, pathStrategy);
                }
                catch (XmlPullParserException ex) {
                    throw new IllegalArgumentException("Failed to parse android.support.FILE_PROVIDER_PATHS meta-data", (Throwable)ex);
                }
                catch (IOException ex2) {
                    throw new IllegalArgumentException("Failed to parse android.support.FILE_PROVIDER_PATHS meta-data", ex2);
                }
            }
            return pathStrategy;
        }
    }
    
    public static Uri getUriForFile(@NonNull final Context context, @NonNull final String s, @NonNull final File file) {
        return getPathStrategy(context, s).getUriForFile(file);
    }
    
    private static int modeToMode(final String s) {
        if ("r".equals(s)) {
            return 268435456;
        }
        if ("w".equals(s) || "wt".equals(s)) {
            return 738197504;
        }
        if ("wa".equals(s)) {
            return 704643072;
        }
        if ("rw".equals(s)) {
            return 939524096;
        }
        if ("rwt".equals(s)) {
            return 1006632960;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Invalid mode: ");
        sb.append(s);
        throw new IllegalArgumentException(sb.toString());
    }
    
    private static PathStrategy parsePathStrategy(final Context context, final String s) throws IOException, XmlPullParserException {
        final SimplePathStrategy simplePathStrategy = new SimplePathStrategy(s);
        final ProviderInfo resolveContentProvider = context.getPackageManager().resolveContentProvider(s, 128);
        if (resolveContentProvider == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Couldn't find meta-data for provider with authority ");
            sb.append(s);
            throw new IllegalArgumentException(sb.toString());
        }
        final XmlResourceParser loadXmlMetaData = resolveContentProvider.loadXmlMetaData(context.getPackageManager(), "android.support.FILE_PROVIDER_PATHS");
        if (loadXmlMetaData == null) {
            throw new IllegalArgumentException("Missing android.support.FILE_PROVIDER_PATHS meta-data");
        }
        while (true) {
            final int next = loadXmlMetaData.next();
            if (next == 1) {
                break;
            }
            if (next != 2) {
                continue;
            }
            final String name = loadXmlMetaData.getName();
            final String attributeValue = loadXmlMetaData.getAttributeValue((String)null, "name");
            final String attributeValue2 = loadXmlMetaData.getAttributeValue((String)null, "path");
            final File file = null;
            final File file2 = null;
            File file3 = null;
            if ("root-path".equals(name)) {
                file3 = FileProvider.DEVICE_ROOT;
            }
            else if ("files-path".equals(name)) {
                file3 = context.getFilesDir();
            }
            else if ("cache-path".equals(name)) {
                file3 = context.getCacheDir();
            }
            else if ("external-path".equals(name)) {
                file3 = Environment.getExternalStorageDirectory();
            }
            else if ("external-files-path".equals(name)) {
                final File[] externalFilesDirs = ContextCompat.getExternalFilesDirs(context, null);
                if (externalFilesDirs.length > 0) {
                    file3 = externalFilesDirs[0];
                }
            }
            else if ("external-cache-path".equals(name)) {
                final File[] externalCacheDirs = ContextCompat.getExternalCacheDirs(context);
                file3 = file;
                if (externalCacheDirs.length > 0) {
                    file3 = externalCacheDirs[0];
                }
            }
            else {
                file3 = file2;
                if (Build$VERSION.SDK_INT >= 21) {
                    file3 = file2;
                    if ("external-media-path".equals(name)) {
                        final File[] externalMediaDirs = context.getExternalMediaDirs();
                        file3 = file2;
                        if (externalMediaDirs.length > 0) {
                            file3 = externalMediaDirs[0];
                        }
                    }
                }
            }
            if (file3 == null) {
                continue;
            }
            simplePathStrategy.addRoot(attributeValue, buildPath(file3, attributeValue2));
        }
        return (PathStrategy)simplePathStrategy;
    }
    
    public void attachInfo(@NonNull final Context context, @NonNull final ProviderInfo providerInfo) {
        super.attachInfo(context, providerInfo);
        if (providerInfo.exported) {
            throw new SecurityException("Provider must not be exported");
        }
        if (!providerInfo.grantUriPermissions) {
            throw new SecurityException("Provider must grant uri permissions");
        }
        this.mStrategy = getPathStrategy(context, providerInfo.authority);
    }
    
    public int delete(@NonNull final Uri uri, @Nullable final String s, @Nullable final String[] array) {
        throw new Runtime("d2j fail translate: java.lang.RuntimeException: can not merge I and Z\r\n\tat com.googlecode.dex2jar.ir.TypeClass.merge(TypeClass.java:100)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeRef.updateTypeClass(TypeTransformer.java:174)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.provideAs(TypeTransformer.java:780)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.enexpr(TypeTransformer.java:659)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:719)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.s1stmt(TypeTransformer.java:810)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.sxStmt(TypeTransformer.java:840)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.analyze(TypeTransformer.java:206)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer.transform(TypeTransformer.java:44)\r\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:162)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\r\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\r\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\r\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\r\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\r\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\r\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\r\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\r\n");
    }
    
    public String getType(@NonNull final Uri uri) {
        final File fileForUri = this.mStrategy.getFileForUri(uri);
        final int lastIndex = fileForUri.getName().lastIndexOf(46);
        if (lastIndex >= 0) {
            final String mimeTypeFromExtension = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileForUri.getName().substring(lastIndex + 1));
            if (mimeTypeFromExtension != null) {
                return mimeTypeFromExtension;
            }
        }
        return "application/octet-stream";
    }
    
    public Uri insert(@NonNull final Uri uri, final ContentValues contentValues) {
        throw new UnsupportedOperationException("No external inserts");
    }
    
    public boolean onCreate() {
        return true;
    }
    
    public ParcelFileDescriptor openFile(@NonNull final Uri uri, @NonNull final String s) throws FileNotFoundException {
        return ParcelFileDescriptor.open(this.mStrategy.getFileForUri(uri), modeToMode(s));
    }
    
    public Cursor query(@NonNull final Uri uri, @Nullable final String[] array, @Nullable final String s, @Nullable String[] array2, @Nullable String s2) {
        final File fileForUri = this.mStrategy.getFileForUri(uri);
        String[] columns = array;
        if (array == null) {
            columns = FileProvider.COLUMNS;
        }
        array2 = new String[columns.length];
        final Object[] array3 = new Object[columns.length];
        int n = 0;
        int n4;
        for (int length = columns.length, i = 0; i < length; ++i, n = n4) {
            s2 = columns[i];
            int n3;
            if ("_display_name".equals(s2)) {
                array2[n] = "_display_name";
                final int n2 = n + 1;
                array3[n] = fileForUri.getName();
                n3 = n2;
            }
            else {
                n4 = n;
                if (!"_size".equals(s2)) {
                    continue;
                }
                array2[n] = "_size";
                final int n5 = n + 1;
                array3[n] = fileForUri.length();
                n3 = n5;
            }
            n4 = n3;
        }
        final String[] copy = copyOf(array2, n);
        final Object[] copy2 = copyOf(array3, n);
        final MatrixCursor matrixCursor = new MatrixCursor(copy, 1);
        matrixCursor.addRow(copy2);
        return (Cursor)matrixCursor;
    }
    
    public int update(@NonNull final Uri uri, final ContentValues contentValues, @Nullable final String s, @Nullable final String[] array) {
        throw new UnsupportedOperationException("No external updates");
    }
    
    interface PathStrategy
    {
        File getFileForUri(final Uri p0);
        
        Uri getUriForFile(final File p0);
    }
    
    static class SimplePathStrategy implements PathStrategy
    {
        private final String mAuthority;
        private final HashMap<String, File> mRoots;
        
        SimplePathStrategy(final String mAuthority) {
            this.mRoots = new HashMap<String, File>();
            this.mAuthority = mAuthority;
        }
        
        void addRoot(final String s, final File file) {
            if (TextUtils.isEmpty((CharSequence)s)) {
                throw new IllegalArgumentException("Name must not be empty");
            }
            try {
                this.mRoots.put(s, file.getCanonicalFile());
            }
            catch (IOException ex) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Failed to resolve canonical path for ");
                sb.append(file);
                throw new IllegalArgumentException(sb.toString(), ex);
            }
        }
        
        @Override
        public File getFileForUri(Uri uri) {
            final String encodedPath = uri.getEncodedPath();
            final int index = encodedPath.indexOf(47, 1);
            final String decode = Uri.decode(encodedPath.substring(1, index));
            final String decode2 = Uri.decode(encodedPath.substring(index + 1));
            final File file = this.mRoots.get(decode);
            if (file == null) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Unable to find configured root for ");
                sb.append(uri);
                throw new IllegalArgumentException(sb.toString());
            }
            uri = (Uri)new File(file, decode2);
            try {
                final File canonicalFile = ((File)uri).getCanonicalFile();
                if (!canonicalFile.getPath().startsWith(file.getPath())) {
                    throw new SecurityException("Resolved path jumped beyond configured root");
                }
                return canonicalFile;
            }
            catch (IOException ex) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Failed to resolve canonical path for ");
                sb2.append(uri);
                throw new IllegalArgumentException(sb2.toString());
            }
        }
        
        @Override
        public Uri getUriForFile(File string) {
            try {
                final String canonicalPath = string.getCanonicalPath();
                string = null;
                for (final Map.Entry<String, File> entry : this.mRoots.entrySet()) {
                    final String path = entry.getValue().getPath();
                    Object o = string;
                    Label_0100: {
                        if (canonicalPath.startsWith(path)) {
                            if (string != null) {
                                o = string;
                                if (path.length() <= ((Map.Entry<K, File>)string).getValue().getPath().length()) {
                                    break Label_0100;
                                }
                            }
                            o = entry;
                        }
                    }
                    string = (File)o;
                }
                if (string == null) {
                    string = (File)new StringBuilder();
                    ((StringBuilder)string).append("Failed to find configured root that contains ");
                    ((StringBuilder)string).append(canonicalPath);
                    throw new IllegalArgumentException(((StringBuilder)string).toString());
                }
                final String path2 = ((Map.Entry<K, File>)string).getValue().getPath();
                String s;
                if (path2.endsWith("/")) {
                    s = canonicalPath.substring(path2.length());
                }
                else {
                    s = canonicalPath.substring(path2.length() + 1);
                }
                final StringBuilder sb = new StringBuilder();
                sb.append(Uri.encode((String)((Map.Entry<String, V>)string).getKey()));
                sb.append('/');
                sb.append(Uri.encode(s, "/"));
                string = (File)sb.toString();
                return new Uri$Builder().scheme("content").authority(this.mAuthority).encodedPath((String)string).build();
            }
            catch (IOException ex) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Failed to resolve canonical path for ");
                sb2.append(string);
                throw new IllegalArgumentException(sb2.toString());
            }
        }
    }
}
