package com.zhekasmirnov.innercore.mod.resource.types;

import java.io.*;
import com.zhekasmirnov.innercore.mod.resource.pack.*;
import com.zhekasmirnov.innercore.mod.resource.types.enums.*;
import android.support.annotation.*;

public class ResourceFile extends File
{
    private AnimationType animationType;
    private String packDir;
    protected ParseError parseError;
    private IResourcePack resourcePack;
    private TextureType textureType;
    private FileType type;
    
    public ResourceFile(final IResourcePack resourcePack, final File file) {
        this(file);
        this.setResourcePack(resourcePack);
    }
    
    public ResourceFile(final File file) {
        this(file.getAbsolutePath());
    }
    
    public ResourceFile(@NonNull final String s) {
        super(s);
        this.parseError = ParseError.NONE;
        final String name = this.getName();
        if (name.contains(".anim.")) {
            this.type = FileType.ANIMATION;
            if (s.endsWith(".png")) {
                this.animationType = AnimationType.TEXTURE;
                return;
            }
            if (s.endsWith(".json")) {
                this.animationType = AnimationType.DESCRIPTOR;
                return;
            }
            this.type = FileType.INVALID;
            this.parseError = ParseError.ANIMATION_INVALID_NAME;
        }
        else if (!name.endsWith(".png") && !name.endsWith(".tga")) {
            if (name.endsWith(".json")) {
                if (name.equals("pack_manifest.json")) {
                    this.type = FileType.MANIFEST;
                    return;
                }
                this.type = FileType.JSON;
            }
            else {
                if (name.endsWith(".js")) {
                    this.type = FileType.EXECUTABLE;
                    return;
                }
                this.type = FileType.RAW;
            }
        }
        else {
            this.type = FileType.TEXTURE;
            if (s.contains("items-opaque/")) {
                this.textureType = TextureType.ITEM;
                return;
            }
            if (s.contains("terrain-atlas/")) {
                this.textureType = TextureType.BLOCK;
                return;
            }
            if (s.contains("particle-atlas/")) {
                this.textureType = TextureType.PARTICLE;
                return;
            }
            this.textureType = TextureType.DEFAULT;
        }
    }
    
    public AnimationType getAnimationType() {
        return this.animationType;
    }
    
    public String getLocalDir() {
        final String localPath = this.getLocalPath();
        return localPath.substring(0, localPath.lastIndexOf(47) + 1);
    }
    
    public String getLocalPath() {
        final String absolutePath = this.getAbsolutePath();
        if (this.resourcePack == null) {
            return absolutePath;
        }
        return absolutePath.substring(this.resourcePack.getAbsolutePath().length());
    }
    
    public ParseError getParseError() {
        return this.parseError;
    }
    
    public IResourcePack getResourcePack() {
        return this.resourcePack;
    }
    
    public TextureType getTextureType() {
        return this.textureType;
    }
    
    public FileType getType() {
        return this.type;
    }
    
    public void setResourcePack(final IResourcePack resourcePack) {
        this.resourcePack = resourcePack;
    }
}
