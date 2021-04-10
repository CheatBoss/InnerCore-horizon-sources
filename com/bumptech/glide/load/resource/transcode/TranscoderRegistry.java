package com.bumptech.glide.load.resource.transcode;

import com.bumptech.glide.util.*;
import java.util.*;

public class TranscoderRegistry
{
    private static final MultiClassKey GET_KEY;
    private final Map<MultiClassKey, ResourceTranscoder<?, ?>> factories;
    
    static {
        GET_KEY = new MultiClassKey();
    }
    
    public TranscoderRegistry() {
        this.factories = new HashMap<MultiClassKey, ResourceTranscoder<?, ?>>();
    }
    
    public <Z, R> ResourceTranscoder<Z, R> get(final Class<Z> clazz, final Class<R> clazz2) {
        if (clazz.equals(clazz2)) {
            return (ResourceTranscoder<Z, R>)UnitTranscoder.get();
        }
        Object get_KEY = TranscoderRegistry.GET_KEY;
        synchronized (get_KEY) {
            TranscoderRegistry.GET_KEY.set(clazz, clazz2);
            final ResourceTranscoder<?, ?> resourceTranscoder = this.factories.get(TranscoderRegistry.GET_KEY);
            // monitorexit(get_KEY)
            if (resourceTranscoder == null) {
                get_KEY = new StringBuilder();
                ((StringBuilder)get_KEY).append("No transcoder registered for ");
                ((StringBuilder)get_KEY).append(clazz);
                ((StringBuilder)get_KEY).append(" and ");
                ((StringBuilder)get_KEY).append(clazz2);
                throw new IllegalArgumentException(((StringBuilder)get_KEY).toString());
            }
            return (ResourceTranscoder<Z, R>)resourceTranscoder;
        }
    }
    
    public <Z, R> void register(final Class<Z> clazz, final Class<R> clazz2, final ResourceTranscoder<Z, R> resourceTranscoder) {
        this.factories.put(new MultiClassKey(clazz, clazz2), resourceTranscoder);
    }
}
