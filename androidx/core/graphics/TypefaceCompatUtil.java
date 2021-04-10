package androidx.core.graphics;

import android.content.res.*;
import androidx.annotation.*;
import android.util.*;
import android.net.*;
import java.io.*;
import java.nio.channels.*;
import android.content.*;
import android.os.*;
import java.nio.*;

@RestrictTo({ RestrictTo$Scope.LIBRARY_GROUP_PREFIX })
public class TypefaceCompatUtil
{
    private static final String CACHE_FILE_PREFIX = ".font";
    private static final String TAG = "TypefaceCompatUtil";
    
    private TypefaceCompatUtil() {
    }
    
    public static void closeQuietly(final Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            }
            catch (IOException ex) {}
        }
    }
    
    @Nullable
    @RequiresApi(19)
    public static ByteBuffer copyToDirectBuffer(Context tempFile, final Resources resources, final int n) {
        tempFile = (Context)getTempFile(tempFile);
        if (tempFile == null) {
            return null;
        }
        try {
            if (!copyToFile((File)tempFile, resources, n)) {
                return null;
            }
            return mmap((File)tempFile);
        }
        finally {
            ((File)tempFile).delete();
        }
    }
    
    public static boolean copyToFile(final File file, final Resources resources, final int n) {
        Closeable openRawResource = null;
        try {
            return copyToFile(file, (InputStream)(openRawResource = resources.openRawResource(n)));
        }
        finally {
            closeQuietly(openRawResource);
        }
    }
    
    public static boolean copyToFile(final File file, final InputStream inputStream) {
        Closeable closeable = null;
        Closeable closeable2 = null;
        final StrictMode$ThreadPolicy allowThreadDiskWrites = StrictMode.allowThreadDiskWrites();
        try {
            try {
                final Closeable closeable3 = closeable = (closeable2 = new FileOutputStream(file, (boolean)(0 != 0)));
                final byte[] array = new byte[1024];
                while (true) {
                    closeable2 = closeable3;
                    closeable = closeable3;
                    final int read = inputStream.read(array);
                    if (read == -1) {
                        break;
                    }
                    closeable2 = closeable3;
                    closeable = closeable3;
                    ((FileOutputStream)closeable3).write(array, 0, read);
                }
                closeQuietly(closeable3);
                StrictMode.setThreadPolicy(allowThreadDiskWrites);
                return true;
            }
            finally {}
        }
        catch (IOException ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Error copying resource contents to temp file: ");
            sb.append(ex.getMessage());
            Log.e("TypefaceCompatUtil", sb.toString());
            closeQuietly(closeable);
            StrictMode.setThreadPolicy(allowThreadDiskWrites);
            return false;
        }
        closeQuietly(closeable2);
        StrictMode.setThreadPolicy(allowThreadDiskWrites);
    }
    
    @Nullable
    public static File getTempFile(Context cacheDir) {
        cacheDir = (Context)cacheDir.getCacheDir();
        if (cacheDir == null) {
            return null;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(".font");
        sb.append(Process.myPid());
        sb.append("-");
        sb.append(Process.myTid());
        sb.append("-");
        final String string = sb.toString();
        for (int i = 0; i < 100; ++i) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(string);
            sb2.append(i);
            final File file = new File((File)cacheDir, sb2.toString());
            try {
                if (file.createNewFile()) {
                    return file;
                }
            }
            catch (IOException ex) {}
        }
        return null;
    }
    
    @Nullable
    @RequiresApi(19)
    public static ByteBuffer mmap(Context t, final CancellationSignal cancellationSignal, final Uri uri) {
        final ContentResolver contentResolver = ((Context)t).getContentResolver();
        try {
            final ParcelFileDescriptor openFileDescriptor = contentResolver.openFileDescriptor(uri, "r", cancellationSignal);
            if (openFileDescriptor == null) {
                if (openFileDescriptor != null) {
                    openFileDescriptor.close();
                }
                return null;
            }
            Throwable t4;
            try {
                final FileInputStream fileInputStream = new FileInputStream(openFileDescriptor.getFileDescriptor());
                Throwable t2;
                try {
                    final FileChannel channel = fileInputStream.getChannel();
                    final MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_ONLY, 0L, channel.size());
                    if (fileInputStream != null) {
                        fileInputStream.close();
                    }
                    if (openFileDescriptor != null) {
                        openFileDescriptor.close();
                    }
                    return map;
                }
                catch (Throwable t) {
                    try {
                        throw t;
                    }
                    finally {
                        t2 = t;
                        final Throwable t3;
                        t = t3;
                    }
                }
                finally {
                    t2 = null;
                }
                if (fileInputStream != null) {
                    if (t2 != null) {
                        try {
                            fileInputStream.close();
                        }
                        catch (Throwable t6) {}
                    }
                    else {
                        fileInputStream.close();
                    }
                }
                throw t;
            }
            catch (Throwable t) {
                try {
                    throw t;
                }
                finally {
                    t4 = t;
                    final Throwable t5;
                    t = t5;
                }
            }
            finally {
                t4 = null;
            }
            if (openFileDescriptor != null) {
                if (t4 != null) {
                    try {
                        openFileDescriptor.close();
                    }
                    catch (Throwable t7) {}
                }
                else {
                    openFileDescriptor.close();
                }
            }
            throw t;
        }
        catch (IOException ex) {
            return null;
        }
    }
    
    @Nullable
    @RequiresApi(19)
    private static ByteBuffer mmap(File t) {
        try {
            final FileInputStream fileInputStream = new FileInputStream((File)t);
            Throwable t2;
            try {
                final FileChannel channel = fileInputStream.getChannel();
                final MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_ONLY, 0L, channel.size());
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                return map;
            }
            catch (Throwable t) {
                try {
                    throw t;
                }
                finally {
                    t2 = t;
                    final Throwable t3;
                    t = t3;
                }
            }
            finally {
                t2 = null;
            }
            if (fileInputStream != null) {
                if (t2 != null) {
                    try {
                        fileInputStream.close();
                    }
                    catch (Throwable t4) {}
                }
                else {
                    fileInputStream.close();
                }
            }
            throw t;
        }
        catch (IOException ex) {
            return null;
        }
    }
}
