package org.reflections.serializers;

import java.lang.reflect.*;
import com.google.common.base.*;
import com.google.common.collect.*;
import java.util.*;
import com.google.gson.*;
import org.reflections.*;
import org.reflections.util.*;
import java.nio.charset.*;
import com.google.common.io.*;
import java.io.*;

public class JsonSerializer implements Serializer
{
    private Gson gson;
    
    private Gson getGson() {
        if (this.gson == null) {
            this.gson = new GsonBuilder().registerTypeAdapter((Type)Multimap.class, (Object)new com.google.gson.JsonSerializer<Multimap>() {
                public JsonElement serialize(final Multimap multimap, final Type type, final JsonSerializationContext jsonSerializationContext) {
                    return jsonSerializationContext.serialize((Object)multimap.asMap());
                }
            }).registerTypeAdapter((Type)Multimap.class, (Object)new JsonDeserializer<Multimap>() {
                public Multimap deserialize(final JsonElement jsonElement, final Type type, final JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
                    final SetMultimap setMultimap = Multimaps.newSetMultimap((Map)new HashMap(), (Supplier)new Supplier<Set<String>>() {
                        public Set<String> get() {
                            return (Set<String>)Sets.newHashSet();
                        }
                    });
                    for (final Map.Entry<K, JsonArray> entry : ((JsonObject)jsonElement).entrySet()) {
                        final Iterator iterator2 = entry.getValue().iterator();
                        while (iterator2.hasNext()) {
                            setMultimap.get((Object)entry.getKey()).add(iterator2.next().getAsString());
                        }
                    }
                    return (Multimap)setMultimap;
                }
            }).setPrettyPrinting().create();
        }
        return this.gson;
    }
    
    @Override
    public Reflections read(final InputStream inputStream) {
        return (Reflections)this.getGson().fromJson((Reader)new InputStreamReader(inputStream), (Class)Reflections.class);
    }
    
    @Override
    public File save(final Reflections reflections, final String s) {
        try {
            final File prepareFile = Utils.prepareFile(s);
            Files.write((CharSequence)this.toString(reflections), prepareFile, Charset.defaultCharset());
            return prepareFile;
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    @Override
    public String toString(final Reflections reflections) {
        return this.getGson().toJson((Object)reflections);
    }
}
