package com.bumptech.glide.load.model;

import com.bumptech.glide.load.*;
import com.bumptech.glide.util.*;
import android.util.*;
import java.io.*;

public class StreamEncoder implements Encoder<InputStream>
{
    private static final String TAG = "StreamEncoder";
    
    @Override
    public boolean encode(final InputStream inputStream, final OutputStream outputStream) {
        final byte[] bytes = ByteArrayPool.get().getBytes();
        try {
            try {
                while (true) {
                    final int read = inputStream.read(bytes);
                    if (read == -1) {
                        break;
                    }
                    outputStream.write(bytes, 0, read);
                }
                ByteArrayPool.get().releaseBytes(bytes);
                return true;
            }
            finally {}
        }
        catch (IOException ex) {
            if (Log.isLoggable("StreamEncoder", 3)) {
                Log.d("StreamEncoder", "Failed to encode data onto the OutputStream", (Throwable)ex);
            }
            ByteArrayPool.get().releaseBytes(bytes);
            return false;
        }
        ByteArrayPool.get().releaseBytes(bytes);
    }
    
    @Override
    public String getId() {
        return "";
    }
}
