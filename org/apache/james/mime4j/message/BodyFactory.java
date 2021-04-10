package org.apache.james.mime4j.message;

import java.nio.charset.*;
import org.apache.commons.logging.*;
import org.apache.james.mime4j.util.*;
import java.io.*;
import org.apache.james.mime4j.storage.*;

public class BodyFactory
{
    private static final Charset FALLBACK_CHARSET;
    private static Log log;
    private StorageProvider storageProvider;
    
    static {
        BodyFactory.log = LogFactory.getLog((Class)BodyFactory.class);
        FALLBACK_CHARSET = CharsetUtil.DEFAULT_CHARSET;
    }
    
    public BodyFactory() {
        this.storageProvider = DefaultStorageProvider.getInstance();
    }
    
    public BodyFactory(final StorageProvider storageProvider) {
        StorageProvider instance = storageProvider;
        if (storageProvider == null) {
            instance = DefaultStorageProvider.getInstance();
        }
        this.storageProvider = instance;
    }
    
    private static Charset toJavaCharset(final String s, final boolean b) {
        final String javaCharset = CharsetUtil.toJavaCharset(s);
        if (javaCharset == null) {
            if (BodyFactory.log.isWarnEnabled()) {
                final Log log = BodyFactory.log;
                final StringBuilder sb = new StringBuilder();
                sb.append("MIME charset '");
                sb.append(s);
                sb.append("' has no ");
                sb.append("corresponding Java charset. Using ");
                sb.append(BodyFactory.FALLBACK_CHARSET);
                sb.append(" instead.");
                log.warn((Object)sb.toString());
            }
            return BodyFactory.FALLBACK_CHARSET;
        }
        if (b && !CharsetUtil.isEncodingSupported(javaCharset)) {
            if (BodyFactory.log.isWarnEnabled()) {
                final Log log2 = BodyFactory.log;
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("MIME charset '");
                sb2.append(s);
                sb2.append("' does not support encoding. Using ");
                sb2.append(BodyFactory.FALLBACK_CHARSET);
                sb2.append(" instead.");
                log2.warn((Object)sb2.toString());
            }
            return BodyFactory.FALLBACK_CHARSET;
        }
        if (!b && !CharsetUtil.isDecodingSupported(javaCharset)) {
            if (BodyFactory.log.isWarnEnabled()) {
                final Log log3 = BodyFactory.log;
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("MIME charset '");
                sb3.append(s);
                sb3.append("' does not support decoding. Using ");
                sb3.append(BodyFactory.FALLBACK_CHARSET);
                sb3.append(" instead.");
                log3.warn((Object)sb3.toString());
            }
            return BodyFactory.FALLBACK_CHARSET;
        }
        return Charset.forName(javaCharset);
    }
    
    public BinaryBody binaryBody(final InputStream inputStream) throws IOException {
        if (inputStream != null) {
            return new StorageBinaryBody(new MultiReferenceStorage(this.storageProvider.store(inputStream)));
        }
        throw new IllegalArgumentException();
    }
    
    public BinaryBody binaryBody(final Storage storage) throws IOException {
        if (storage != null) {
            return new StorageBinaryBody(new MultiReferenceStorage(storage));
        }
        throw new IllegalArgumentException();
    }
    
    public StorageProvider getStorageProvider() {
        return this.storageProvider;
    }
    
    public TextBody textBody(final InputStream inputStream) throws IOException {
        if (inputStream != null) {
            return new StorageTextBody(new MultiReferenceStorage(this.storageProvider.store(inputStream)), CharsetUtil.DEFAULT_CHARSET);
        }
        throw new IllegalArgumentException();
    }
    
    public TextBody textBody(final InputStream inputStream, final String s) throws IOException {
        if (inputStream == null) {
            throw new IllegalArgumentException();
        }
        if (s != null) {
            return new StorageTextBody(new MultiReferenceStorage(this.storageProvider.store(inputStream)), toJavaCharset(s, false));
        }
        throw new IllegalArgumentException();
    }
    
    public TextBody textBody(final String s) {
        if (s != null) {
            return new StringTextBody(s, CharsetUtil.DEFAULT_CHARSET);
        }
        throw new IllegalArgumentException();
    }
    
    public TextBody textBody(final String s, final String s2) {
        if (s == null) {
            throw new IllegalArgumentException();
        }
        if (s2 != null) {
            return new StringTextBody(s, toJavaCharset(s2, true));
        }
        throw new IllegalArgumentException();
    }
    
    public TextBody textBody(final Storage storage) throws IOException {
        if (storage != null) {
            return new StorageTextBody(new MultiReferenceStorage(storage), CharsetUtil.DEFAULT_CHARSET);
        }
        throw new IllegalArgumentException();
    }
    
    public TextBody textBody(final Storage storage, final String s) throws IOException {
        if (storage == null) {
            throw new IllegalArgumentException();
        }
        if (s != null) {
            return new StorageTextBody(new MultiReferenceStorage(storage), toJavaCharset(s, false));
        }
        throw new IllegalArgumentException();
    }
}
