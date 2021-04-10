package okhttp3.internal.http;

import okio.*;
import java.io.*;
import okhttp3.*;

public interface HttpCodec
{
    void cancel();
    
    Sink createRequestBody(final Request p0, final long p1);
    
    void finishRequest() throws IOException;
    
    void flushRequest() throws IOException;
    
    ResponseBody openResponseBody(final Response p0) throws IOException;
    
    Response.Builder readResponseHeaders(final boolean p0) throws IOException;
    
    void writeRequestHeaders(final Request p0) throws IOException;
}
