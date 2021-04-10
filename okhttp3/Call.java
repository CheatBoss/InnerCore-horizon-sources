package okhttp3;

import java.io.*;

public interface Call extends Cloneable
{
    void cancel();
    
    Call clone();
    
    void enqueue(final Callback p0);
    
    Response execute() throws IOException;
    
    boolean isCanceled();
    
    boolean isExecuted();
    
    Request request();
    
    public interface Factory
    {
        Call newCall(final Request p0);
    }
}
