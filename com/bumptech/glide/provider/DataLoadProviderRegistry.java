package com.bumptech.glide.provider;

import com.bumptech.glide.util.*;
import java.util.*;

public class DataLoadProviderRegistry
{
    private static final MultiClassKey GET_KEY;
    private final Map<MultiClassKey, DataLoadProvider<?, ?>> providers;
    
    static {
        GET_KEY = new MultiClassKey();
    }
    
    public DataLoadProviderRegistry() {
        this.providers = new HashMap<MultiClassKey, DataLoadProvider<?, ?>>();
    }
    
    public <T, Z> DataLoadProvider<T, Z> get(final Class<T> clazz, final Class<Z> clazz2) {
        synchronized (DataLoadProviderRegistry.GET_KEY) {
            DataLoadProviderRegistry.GET_KEY.set(clazz, clazz2);
            final DataLoadProvider<?, ?> dataLoadProvider = this.providers.get(DataLoadProviderRegistry.GET_KEY);
            // monitorexit(DataLoadProviderRegistry.GET_KEY)
            DataLoadProvider<T, Z> value = (DataLoadProvider<T, Z>)dataLoadProvider;
            if (dataLoadProvider == null) {
                value = EmptyDataLoadProvider.get();
            }
            return value;
        }
    }
    
    public <T, Z> void register(final Class<T> clazz, final Class<Z> clazz2, final DataLoadProvider<T, Z> dataLoadProvider) {
        this.providers.put(new MultiClassKey(clazz, clazz2), dataLoadProvider);
    }
}
