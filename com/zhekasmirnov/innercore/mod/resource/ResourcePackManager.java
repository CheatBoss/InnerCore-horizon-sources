package com.zhekasmirnov.innercore.mod.resource;

import com.zhekasmirnov.innercore.utils.*;
import com.zhekasmirnov.innercore.ui.*;
import org.json.*;
import java.util.*;

public class ResourcePackManager
{
    public static final String LOGGER_TAG = "INNERCORE-RESOURCES";
    public static ResourcePackManager instance;
    public String resourcePackDefinition;
    public String resourcePackList;
    public ResourceStorage resourceStorage;
    
    static {
        final StringBuilder sb = new StringBuilder();
        sb.append(FileTools.DIR_MINECRAFT);
        sb.append("minecraftpe");
        FileTools.assureDir(sb.toString());
        final StringBuilder sb2 = new StringBuilder();
        sb2.append(FileTools.DIR_MINECRAFT);
        sb2.append("resource_packs");
        FileTools.assureDir(sb2.toString());
        ResourcePackManager.instance = new ResourcePackManager();
    }
    
    public ResourcePackManager() {
        final StringBuilder sb = new StringBuilder();
        sb.append(FileTools.DIR_MINECRAFT);
        sb.append("minecraftpe/valid_known_packs.json");
        this.resourcePackDefinition = sb.toString();
        final StringBuilder sb2 = new StringBuilder();
        sb2.append(FileTools.DIR_MINECRAFT);
        sb2.append("minecraftpe/global_resource_packs.json");
        this.resourcePackList = sb2.toString();
        this.resourceStorage = new ResourceStorage();
    }
    
    public static String getBlockTextureName(final String s, final int n) {
        if (ResourcePackManager.instance.resourceStorage != null) {
            return ResourcePackManager.instance.resourceStorage.blockTextureDescriptor.getTextureName(s, n);
        }
        return null;
    }
    
    public static String getItemTextureName(final String s, final int n) {
        if (ResourcePackManager.instance.resourceStorage != null) {
            return ResourcePackManager.instance.resourceStorage.itemTextureDescriptor.getTextureName(s, n);
        }
        return null;
    }
    
    public static String getSourcePath() {
        final StringBuilder sb = new StringBuilder();
        sb.append(FileTools.DIR_PACK);
        sb.append("assets/resource_packs/vanilla/");
        return sb.toString();
    }
    
    public static boolean isValidBlockTexture(final String s, final int n) {
        return getBlockTextureName(s, n) != null;
    }
    
    public static boolean isValidItemTexture(final String s, final int n) {
        return getItemTextureName(s, n) != null;
    }
    
    public void initializeResources() {
        try {
            this.resourceStorage.build();
            LoadingUI.setProgress(0.05f);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public class DefinitionListWatcher extends ListWatcher
    {
        public DefinitionListWatcher(final String s) {
            super(s);
        }
        
        public void add(final String s) throws JSONException {
            final JSONObject jsonObject = new JSONObject();
            jsonObject.put("file_system", (Object)"RawPath");
            jsonObject.put("path", (Object)s);
            super.add(jsonObject);
        }
    }
    
    public class ListWatcher
    {
        JSONArray list;
        String path;
        
        public ListWatcher(final String path) {
            this.path = path;
            try {
                this.list = FileTools.readJSONArray(path);
            }
            catch (Exception ex2) {
                try {
                    this.list = new JSONArray(new JSONTokener("[]"));
                }
                catch (JSONException ex) {
                    ex.printStackTrace();
                }
            }
        }
        
        public void add(final JSONObject jsonObject) throws JSONException {
            for (int i = 0; i < this.list.length(); ++i) {
                final JSONObject jsonObject2 = this.list.getJSONObject(i);
                final Iterator keys = jsonObject2.keys();
                final boolean b = true;
                boolean b2;
                while (true) {
                    b2 = b;
                    if (!keys.hasNext()) {
                        break;
                    }
                    final String s = keys.next();
                    if (jsonObject.has(s) && jsonObject.getString(s).equals(jsonObject2.getString(s))) {
                        continue;
                    }
                    b2 = false;
                    break;
                }
                if (b2) {
                    return;
                }
            }
            this.list.put(this.list.length(), (Object)jsonObject);
        }
        
        public void clear() {
            this.list = new JSONArray();
        }
        
        public void save() {
            try {
                FileTools.writeJSON(this.path, this.list);
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    
    public class ResourceListWatcher extends ListWatcher
    {
        public ResourceListWatcher(final String s) {
            super(s);
        }
        
        public void add(final String s) throws JSONException {
            final JSONObject jsonObject = new JSONObject();
            jsonObject.put("pack_id", (Object)s);
            jsonObject.put("version", (Object)"1.0.0");
            super.add(jsonObject);
        }
    }
}
