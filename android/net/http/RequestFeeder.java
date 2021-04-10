package android.net.http;

import org.apache.http.*;

interface RequestFeeder
{
    Request getRequest();
    
    Request getRequest(final HttpHost p0);
    
    boolean haveRequest(final HttpHost p0);
    
    void requeueRequest(final Request p0);
}
