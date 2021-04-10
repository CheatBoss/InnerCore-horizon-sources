package com.google.android.gms.common.util;

import javax.annotation.*;
import java.io.*;
import android.os.*;

public class ProcessUtils
{
    private static String zzaai;
    private static int zzaaj;
    
    @Nullable
    public static String getMyProcessName() {
        if (ProcessUtils.zzaai == null) {
            ProcessUtils.zzaai = zzl(zzde());
        }
        return ProcessUtils.zzaai;
    }
    
    private static int zzde() {
        if (ProcessUtils.zzaaj == 0) {
            ProcessUtils.zzaaj = Process.myPid();
        }
        return ProcessUtils.zzaaj;
    }
    
    @Nullable
    private static String zzl(final int n) {
        String s = null;
        if (n <= 0) {
            return null;
        }
        String s2 = null;
        try {
            final StringBuilder sb = new StringBuilder(25);
            sb.append("/proc/");
            sb.append(n);
            sb.append("/cmdline");
            final BufferedReader zzm = zzm(sb.toString());
            try {
                zzm.readLine().trim();
            }
            catch (IOException ex) {}
        }
        catch (IOException ex2) {
            s2 = null;
        }
        finally {
            s = s2;
        }
        IOUtils.closeQuietly((Closeable)s2);
        return s;
    }
    
    private static BufferedReader zzm(final String s) throws IOException {
        final StrictMode$ThreadPolicy allowThreadDiskReads = StrictMode.allowThreadDiskReads();
        try {
            return new BufferedReader(new FileReader(s));
        }
        finally {
            StrictMode.setThreadPolicy(allowThreadDiskReads);
        }
    }
}
