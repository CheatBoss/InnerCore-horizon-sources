package android.support.v4.os;

import java.io.*;
import android.os.*;

class EnvironmentCompatKitKat
{
    public static String getStorageState(final File file) {
        return Environment.getStorageState(file);
    }
}
