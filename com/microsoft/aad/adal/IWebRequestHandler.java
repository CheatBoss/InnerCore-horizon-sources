package com.microsoft.aad.adal;

import java.net.*;
import java.io.*;
import java.util.*;

public interface IWebRequestHandler
{
    HttpWebResponse sendGet(final URL p0, final Map<String, String> p1) throws IOException;
    
    HttpWebResponse sendPost(final URL p0, final Map<String, String> p1, final byte[] p2, final String p3) throws IOException;
    
    void setRequestCorrelationId(final UUID p0);
}
