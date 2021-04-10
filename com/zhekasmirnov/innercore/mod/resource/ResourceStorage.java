package com.zhekasmirnov.innercore.mod.resource;

import com.zhekasmirnov.innercore.mod.resource.pack.*;
import com.zhekasmirnov.innercore.mod.resource.types.*;
import org.json.*;
import java.util.*;
import com.zhekasmirnov.innercore.mod.resource.types.enums.*;
import com.zhekasmirnov.horizon.modloader.resource.directory.*;
import com.zhekasmirnov.innercore.api.log.*;
import com.zhekasmirnov.innercore.utils.*;
import java.io.*;

public class ResourceStorage implements IResourcePack
{
    public static final String VANILLA_RESOURCE = "resource_packs/vanilla/";
    private static ArrayList<String> textureLoadQueue;
    JSONArray animationList;
    TextureAtlasDescription blockTextureDescriptor;
    TextureAtlasDescription itemTextureDescriptor;
    private HashMap<String, String> resourceLinks;
    JSONArray textureList;
    
    static {
        ResourceStorage.textureLoadQueue = new ArrayList<String>();
    }
    
    public ResourceStorage() {
        this.resourceLinks = new HashMap<String, String>();
    }
    
    private void addAsAnimation(final ResourceFile resourceFile) {
        final TextureAnimationFile textureAnimationFile = new TextureAnimationFile(resourceFile);
        if (textureAnimationFile.isValid()) {
            try {
                this.animationList.put((Object)textureAnimationFile.constructAnimation());
            }
            catch (JSONException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    public static void addTextureToLoad(final String s) {
        ResourceStorage.textureLoadQueue.add(s);
        if (ResourceStorage.textureLoadQueue.size() < 2) {
            ResourceStorage.textureLoadQueue.add(s);
        }
    }
    
    public static void loadAllTextures() {
        final Iterator<String> iterator = ResourceStorage.textureLoadQueue.iterator();
        while (iterator.hasNext()) {
            nativeAddTextureToLoad(iterator.next());
        }
    }
    
    public static native void nativeAddTextureToLoad(final String p0);
    
    public void addResourceFile(final TextureType textureType, final Resource resource) {
        try {
            switch (textureType) {
                case BLOCK: {
                    this.blockTextureDescriptor.addTexturePath(resource.getNameWithoutExtension(), resource.getIndex(), resource.getPath());
                    break;
                }
                case ITEM: {
                    this.itemTextureDescriptor.addTexturePath(resource.getNameWithoutExtension(), resource.getIndex(), resource.getPath());
                    break;
                }
                default: {}
            }
        }
        catch (JSONException ex) {
            ICLog.e("INNERCORE-RESOURCES", "Cannot add texture path", (Throwable)ex);
        }
    }
    
    public void build() throws IOException, JSONException {
        this.itemTextureDescriptor = new TextureAtlasDescription("textures/item_texture.json");
        this.blockTextureDescriptor = new TextureAtlasDescription("textures/terrain_texture.json");
        this.animationList = FileTools.getAssetAsJSONArray("resource_packs/vanilla/textures/flipbook_textures.json");
        this.textureList = FileTools.getAssetAsJSONArray("resource_packs/vanilla/textures/textures_list.json");
    }
    
    @Override
    public String getAbsolutePath() {
        return "/";
    }
    
    public String getId() {
        return "innercore-resource-main";
    }
    
    public String getLinkedFilePath(final String s) {
        final String s2 = this.resourceLinks.get(s);
        if (s2 != null) {
            return s2;
        }
        return s;
    }
    
    @Override
    public String getPackName() {
        return "Inner Core Resource Storage";
    }
}
