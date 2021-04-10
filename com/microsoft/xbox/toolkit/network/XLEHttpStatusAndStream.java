package com.microsoft.xbox.toolkit.network;

import org.apache.http.*;
import java.io.*;

public class XLEHttpStatusAndStream
{
    public Header[] headers;
    public String redirectUrl;
    public int statusCode;
    public String statusLine;
    public InputStream stream;
    
    public XLEHttpStatusAndStream() {
        this.stream = null;
        this.statusCode = -1;
        this.statusLine = null;
        this.redirectUrl = null;
        this.headers = new Header[0];
    }
    
    public void close() {
        final InputStream stream = this.stream;
        if (stream != null) {
            try {
                stream.close();
                this.stream = null;
            }
            catch (Exception ex) {}
        }
    }
}
