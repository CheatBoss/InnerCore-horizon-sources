package org.apache.james.mime4j.message;

import java.io.*;

public abstract class TextBody extends SingleBody
{
    protected TextBody() {
    }
    
    public abstract String getMimeCharset();
    
    public abstract Reader getReader() throws IOException;
}
