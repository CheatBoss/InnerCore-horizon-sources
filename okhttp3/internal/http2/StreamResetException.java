package okhttp3.internal.http2;

import java.io.*;

public final class StreamResetException extends IOException
{
    public final ErrorCode errorCode;
    
    public StreamResetException(final ErrorCode errorCode) {
        final StringBuilder sb = new StringBuilder();
        sb.append("stream was reset: ");
        sb.append(errorCode);
        super(sb.toString());
        this.errorCode = errorCode;
    }
}
