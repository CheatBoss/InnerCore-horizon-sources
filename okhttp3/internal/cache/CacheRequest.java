package okhttp3.internal.cache;

import okio.*;
import java.io.*;

public interface CacheRequest
{
    void abort();
    
    Sink body() throws IOException;
}
