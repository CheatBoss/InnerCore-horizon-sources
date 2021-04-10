package androidx.core.util;

import androidx.annotation.*;
import android.util.*;
import java.io.*;

public class AtomicFile
{
    private final File mBackupName;
    private final File mBaseName;
    
    public AtomicFile(@NonNull final File mBaseName) {
        this.mBaseName = mBaseName;
        final StringBuilder sb = new StringBuilder();
        sb.append(mBaseName.getPath());
        sb.append(".bak");
        this.mBackupName = new File(sb.toString());
    }
    
    private static boolean sync(@NonNull final FileOutputStream fileOutputStream) {
        try {
            fileOutputStream.getFD().sync();
            return true;
        }
        catch (IOException ex) {
            return false;
        }
    }
    
    public void delete() {
        this.mBaseName.delete();
        this.mBackupName.delete();
    }
    
    public void failWrite(@Nullable final FileOutputStream fileOutputStream) {
        if (fileOutputStream != null) {
            sync(fileOutputStream);
            try {
                fileOutputStream.close();
                this.mBaseName.delete();
                this.mBackupName.renameTo(this.mBaseName);
            }
            catch (IOException ex) {
                Log.w("AtomicFile", "failWrite: Got exception:", (Throwable)ex);
            }
        }
    }
    
    public void finishWrite(@Nullable final FileOutputStream fileOutputStream) {
        if (fileOutputStream != null) {
            sync(fileOutputStream);
            try {
                fileOutputStream.close();
                this.mBackupName.delete();
            }
            catch (IOException ex) {
                Log.w("AtomicFile", "finishWrite: Got exception:", (Throwable)ex);
            }
        }
    }
    
    @NonNull
    public File getBaseFile() {
        return this.mBaseName;
    }
    
    @NonNull
    public FileInputStream openRead() throws FileNotFoundException {
        if (this.mBackupName.exists()) {
            this.mBaseName.delete();
            this.mBackupName.renameTo(this.mBaseName);
        }
        return new FileInputStream(this.mBaseName);
    }
    
    @NonNull
    public byte[] readFully() throws IOException {
        final FileInputStream openRead = this.openRead();
        int n = 0;
        try {
            byte[] array = new byte[openRead.available()];
            while (true) {
                final int read = openRead.read(array, n, array.length - n);
                if (read <= 0) {
                    break;
                }
                n += read;
                final int available = openRead.available();
                byte[] array2 = array;
                if (available > array.length - n) {
                    array2 = new byte[n + available];
                    System.arraycopy(array, 0, array2, 0, n);
                }
                array = array2;
            }
            return array;
        }
        finally {
            openRead.close();
        }
    }
    
    @NonNull
    public FileOutputStream startWrite() throws IOException {
        if (this.mBaseName.exists()) {
            if (!this.mBackupName.exists()) {
                if (!this.mBaseName.renameTo(this.mBackupName)) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Couldn't rename file ");
                    sb.append(this.mBaseName);
                    sb.append(" to backup file ");
                    sb.append(this.mBackupName);
                    Log.w("AtomicFile", sb.toString());
                }
            }
            else {
                this.mBaseName.delete();
            }
        }
        try {
            return new FileOutputStream(this.mBaseName);
        }
        catch (FileNotFoundException ex) {
            if (!this.mBaseName.getParentFile().mkdirs()) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Couldn't create directory ");
                sb2.append(this.mBaseName);
                throw new IOException(sb2.toString());
            }
            try {
                return new FileOutputStream(this.mBaseName);
            }
            catch (FileNotFoundException ex2) {
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("Couldn't create ");
                sb3.append(this.mBaseName);
                throw new IOException(sb3.toString());
            }
        }
    }
}
