package org.mineprogramming.horizon.innercore.util;

import java.net.*;
import java.io.*;
import android.content.*;
import android.net.*;

public class DownloadHelper
{
    public static void downloadFile(final String s, final File file, final FileDownloadListener fileDownloadListener) throws IOException {
        final URL url = new URL(s);
        final URLConnection openConnection = url.openConnection();
        openConnection.connect();
        final long n = openConnection.getContentLength();
        final BufferedInputStream bufferedInputStream = new BufferedInputStream(url.openStream(), 8192);
        final FileOutputStream fileOutputStream = new FileOutputStream(file);
        final byte[] array = new byte[1024];
        long n2 = 0L;
        while (true) {
            final int read = bufferedInputStream.read(array);
            if (read == -1) {
                break;
            }
            n2 += read;
            fileOutputStream.write(array, 0, read);
            if (fileDownloadListener == null) {
                continue;
            }
            fileDownloadListener.onDownloadProgress(n2, n);
        }
        fileOutputStream.flush();
        fileOutputStream.close();
        bufferedInputStream.close();
    }
    
    public static String downloadString(final String s) throws IOException {
        final HttpURLConnection httpURLConnection = (HttpURLConnection)new URL(s).openConnection();
        final StringBuilder sb = new StringBuilder();
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
        while (true) {
            final String line = bufferedReader.readLine();
            if (line == null) {
                break;
            }
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(line);
            sb2.append("\n");
            sb.append(sb2.toString());
        }
        bufferedReader.close();
        httpURLConnection.disconnect();
        return sb.toString().trim();
    }
    
    public static boolean isOnline(final Context context) {
        final NetworkInfo activeNetworkInfo = ((ConnectivityManager)context.getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
    
    public interface FileDownloadListener
    {
        void onDownloadProgress(final long p0, final long p1);
    }
}
