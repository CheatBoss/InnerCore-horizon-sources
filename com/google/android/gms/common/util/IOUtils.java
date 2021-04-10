package com.google.android.gms.common.util;

import javax.annotation.*;
import java.io.*;

public final class IOUtils
{
    public static void closeQuietly(@Nullable final Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            }
            catch (IOException ex) {}
        }
    }
}
