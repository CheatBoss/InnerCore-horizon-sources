package okhttp3;

import java.io.*;

public interface Interceptor
{
    Response intercept(final Chain p0) throws IOException;
    
    public interface Chain
    {
        int connectTimeoutMillis();
        
        Response proceed(final Request p0) throws IOException;
        
        int readTimeoutMillis();
        
        Request request();
        
        int writeTimeoutMillis();
    }
}
