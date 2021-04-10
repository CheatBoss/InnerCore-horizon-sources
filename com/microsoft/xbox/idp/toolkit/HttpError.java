package com.microsoft.xbox.idp.toolkit;

import java.io.*;
import java.util.*;

public class HttpError
{
    private static final String INPUT_START_TOKEN = "\\A";
    private final int errorCode;
    private final String errorMessage;
    private final int httpStatus;
    
    public HttpError(final int errorCode, final int httpStatus, final InputStream inputStream) {
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        final Scanner useDelimiter = new Scanner(inputStream).useDelimiter("\\A");
        String next;
        if (useDelimiter.hasNext()) {
            next = useDelimiter.next();
        }
        else {
            next = "";
        }
        this.errorMessage = next;
    }
    
    public HttpError(final int errorCode, final int httpStatus, final String errorMessage) {
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
    }
    
    public int getErrorCode() {
        return this.errorCode;
    }
    
    public String getErrorMessage() {
        return this.errorMessage;
    }
    
    public int getHttpStatus() {
        return this.httpStatus;
    }
    
    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append("errorCode: ");
        sb.append(this.errorCode);
        sb.append(", httpStatus: ");
        sb.append(this.httpStatus);
        sb.append(", errorMessage: ");
        sb.append(this.errorMessage);
        return sb.toString();
    }
}
