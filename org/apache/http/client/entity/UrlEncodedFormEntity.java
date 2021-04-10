package org.apache.http.client.entity;

import org.apache.http.entity.*;
import java.util.*;
import org.apache.http.*;
import java.io.*;

@Deprecated
public class UrlEncodedFormEntity extends StringEntity
{
    public UrlEncodedFormEntity(final List<? extends NameValuePair> list) throws UnsupportedEncodingException {
        super(null);
        throw new RuntimeException("Stub!");
    }
    
    public UrlEncodedFormEntity(final List<? extends NameValuePair> list, final String s) throws UnsupportedEncodingException {
        super(null);
        throw new RuntimeException("Stub!");
    }
}
