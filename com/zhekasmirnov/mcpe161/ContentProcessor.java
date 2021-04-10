package com.zhekasmirnov.mcpe161;

import com.zhekasmirnov.horizon.modloader.resource.processor.*;
import org.json.*;
import com.zhekasmirnov.horizon.modloader.resource.runtime.*;
import com.zhekasmirnov.horizon.util.*;
import java.io.*;
import com.zhekasmirnov.horizon.modloader.resource.directory.*;
import com.zhekasmirnov.horizon.modloader.resource.*;
import java.util.*;

public class ContentProcessor implements ResourceProcessor, RuntimeResourceHandler
{
    private JSONArray content;
    private JSONObject json;
    private final HashSet<String> paths;
    
    public ContentProcessor() {
        this.paths = new HashSet<String>();
    }
    
    private void addPath(final String s) throws JSONException {
        if (!this.paths.contains(s)) {
            this.paths.add(s);
            this.content.put((Object)new JSONObject().put("path", (Object)s));
        }
    }
    
    public String getResourceName() {
        return "contents.json";
    }
    
    public String getResourcePath() {
        return "contents.json";
    }
    
    public void handle(final RuntimeResource runtimeResource) {
        try {
            runtimeResource.getFile().getParentFile().mkdirs();
            FileUtils.writeJSON(runtimeResource.getFile(), this.json);
        }
        catch (IOException ex) {
            throw new RuntimeException("unexpected exception inside ContentProcessor.handle()", ex);
        }
    }
    
    public void initialize(final ResourceManager resourceManager) {
        try {
            this.paths.clear();
            this.json = FileUtils.readJSONFromAssets(resourceManager.getAssets(), "resource_packs/vanilla/contents.json");
            this.content = this.json.getJSONArray("content");
        }
        catch (JSONException ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("failed to read contents.json: ");
            sb.append(ex);
            throw new RuntimeException(sb.toString(), (Throwable)ex);
        }
        catch (IOException ex2) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("failed to read contents.json: ");
            sb2.append(ex2);
            throw new RuntimeException(sb2.toString(), ex2);
        }
    }
    
    public void process(final Resource resource, final Collection<Resource> collection) {
        collection.add(resource);
        final String path = resource.getPath();
        if (!path.endsWith(".png")) {
            path.endsWith(".tga");
        }
        try {
            final ArrayList<ResourceOverride> list = (ArrayList<ResourceOverride>)new ArrayList<Object>();
            resource.addOverrides((List)list);
            final Iterator<Object> iterator = list.iterator();
            while (iterator.hasNext()) {
                this.addPath(iterator.next().path);
            }
        }
        catch (JSONException ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("unexpected error in ContentProcessor.process(): ");
            sb.append(ex);
            throw new RuntimeException(sb.toString(), (Throwable)ex);
        }
    }
}
