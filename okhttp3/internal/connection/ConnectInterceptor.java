package okhttp3.internal.connection;

import okhttp3.internal.http.*;
import okhttp3.*;
import java.io.*;

public final class ConnectInterceptor implements Interceptor
{
    public final OkHttpClient client;
    
    public ConnectInterceptor(final OkHttpClient client) {
        this.client = client;
    }
    
    @Override
    public Response intercept(final Chain chain) throws IOException {
        final RealInterceptorChain realInterceptorChain = (RealInterceptorChain)chain;
        final Request request = realInterceptorChain.request();
        final StreamAllocation streamAllocation = realInterceptorChain.streamAllocation();
        return realInterceptorChain.proceed(request, streamAllocation, streamAllocation.newStream(this.client, chain, request.method().equals("GET") ^ true), streamAllocation.connection());
    }
}
