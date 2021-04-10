package org.apache.james.mime4j.storage;

import org.apache.commons.logging.*;

public class DefaultStorageProvider
{
    public static final String DEFAULT_STORAGE_PROVIDER_PROPERTY = "org.apache.james.mime4j.defaultStorageProvider";
    private static volatile StorageProvider instance;
    private static Log log;
    
    static {
        DefaultStorageProvider.log = LogFactory.getLog((Class)DefaultStorageProvider.class);
        DefaultStorageProvider.instance = null;
        initialize();
    }
    
    private DefaultStorageProvider() {
    }
    
    public static StorageProvider getInstance() {
        return DefaultStorageProvider.instance;
    }
    
    private static void initialize() {
        final String property = System.getProperty("org.apache.james.mime4j.defaultStorageProvider");
        if (property != null) {
            try {
                DefaultStorageProvider.instance = (StorageProvider)Class.forName(property).newInstance();
            }
            catch (Exception ex) {
                final Log log = DefaultStorageProvider.log;
                final StringBuilder sb = new StringBuilder();
                sb.append("Unable to create or instantiate StorageProvider class '");
                sb.append(property);
                sb.append("'. Using default instead.");
                log.warn((Object)sb.toString(), (Throwable)ex);
            }
        }
        if (DefaultStorageProvider.instance == null) {
            DefaultStorageProvider.instance = new ThresholdStorageProvider(new TempFileStorageProvider(), 1024);
        }
    }
    
    static void reset() {
        DefaultStorageProvider.instance = null;
        initialize();
    }
    
    public static void setInstance(final StorageProvider instance) {
        if (instance != null) {
            DefaultStorageProvider.instance = instance;
            return;
        }
        throw new IllegalArgumentException();
    }
}
