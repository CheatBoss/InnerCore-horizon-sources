package com.microsoft.xbox.toolkit.network;

import org.apache.http.client.methods.*;
import org.apache.http.client.*;
import java.util.zip.*;
import org.apache.http.util.*;
import java.io.*;
import com.microsoft.xbox.toolkit.*;
import org.apache.http.*;

public abstract class AbstractXLEHttpClient
{
    protected abstract HttpResponse execute(final HttpUriRequest p0) throws ClientProtocolException, IOException;
    
    public XLEHttpStatusAndStream getHttpStatusAndStreamInternal(final HttpUriRequest httpUriRequest, final boolean b) throws XLEException {
        XLEHttpStatusAndStream xleHttpStatusAndStream;
        HttpResponse execute = null;
        HttpEntity entity;
        Header firstHeader;
        Block_9_Outer:Label_0119_Outer:
        while (true) {
            xleHttpStatusAndStream = new XLEHttpStatusAndStream();
        Label_0127_Outer:
            while (true) {
                while (true) {
                    Label_0233: {
                        try {
                            execute = this.execute(httpUriRequest);
                            if (execute != null && execute.getStatusLine() != null) {
                                xleHttpStatusAndStream.statusLine = execute.getStatusLine().toString();
                                xleHttpStatusAndStream.statusCode = execute.getStatusLine().getStatusCode();
                            }
                            if (execute != null && execute.getLastHeader("Location") != null) {
                                xleHttpStatusAndStream.redirectUrl = execute.getLastHeader("Location").getValue();
                            }
                            if (execute != null) {
                                xleHttpStatusAndStream.headers = execute.getAllHeaders();
                            }
                            break Label_0233;
                            // iftrue(Label_0198:, firstHeader == null || !firstHeader.getValue().equalsIgnoreCase("gzip"))
                            while (true) {
                                while (true) {
                                    xleHttpStatusAndStream.stream = new GZIPInputStream(xleHttpStatusAndStream.stream);
                                    return xleHttpStatusAndStream;
                                    xleHttpStatusAndStream.stream = new ByteArrayInputStream(EntityUtils.toByteArray(entity));
                                    entity.consumeContent();
                                    firstHeader = execute.getFirstHeader("Content-Encoding");
                                    continue Block_9_Outer;
                                }
                                Label_0198: {
                                    return xleHttpStatusAndStream;
                                }
                                entity = execute.getEntity();
                                continue Label_0119_Outer;
                            }
                        }
                        // iftrue(Label_0198:, entity == null)
                        catch (Exception ex) {
                            httpUriRequest.abort();
                            if (xleHttpStatusAndStream.stream != null) {
                                xleHttpStatusAndStream.close();
                            }
                            throw new XLEException(4L, ex);
                        }
                    }
                    if (execute == null) {
                        entity = null;
                        continue;
                    }
                    break;
                }
                continue Label_0127_Outer;
            }
        }
    }
}
