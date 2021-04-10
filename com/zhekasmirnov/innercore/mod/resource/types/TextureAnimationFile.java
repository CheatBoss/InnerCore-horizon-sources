package com.zhekasmirnov.innercore.mod.resource.types;

import java.io.*;
import android.support.annotation.*;
import com.zhekasmirnov.innercore.mod.resource.types.enums.*;
import com.zhekasmirnov.innercore.utils.*;
import org.json.*;

public class TextureAnimationFile extends ResourceFile
{
    private int delay;
    private boolean isValid;
    private String textureName;
    private String tileToAnimate;
    
    public TextureAnimationFile(final ResourceFile resourceFile) {
        super(resourceFile.getResourcePack(), resourceFile);
        this.isValid = false;
        this.read();
    }
    
    public TextureAnimationFile(@NonNull final String s) {
        super(s);
        this.isValid = false;
        this.read();
    }
    
    private void read() {
        if (this.getType() != FileType.ANIMATION) {
            this.parseError = ParseError.ANIMATION_INVALID_FILE;
            return;
        }
        switch (this.getAnimationType()) {
            default: {
                return;
            }
            case TEXTURE: {
                final String[] split = this.getName().split("\\.");
                Label_0143: {
                    if (split.length > 2 && split[split.length - 2].equals("anim")) {
                        this.delay = 1;
                        break Label_0143;
                    }
                    if (split.length <= 3 || !split[split.length - 3].equals("anim")) {
                        break Label_0143;
                    }
                    try {
                        this.delay = Integer.valueOf(split[split.length - 2]);
                        if (this.delay < 1) {
                            this.parseError = ParseError.ANIMATION_INVALID_DELAY;
                            return;
                        }
                        this.tileToAnimate = split[0];
                        this.textureName = this.getLocalPath();
                        break;
                    }
                    catch (Exception ex2) {
                        this.parseError = ParseError.ANIMATION_INVALID_DELAY;
                        return;
                    }
                }
                this.parseError = ParseError.ANIMATION_INVALID_NAME;
                return;
            }
            case DESCRIPTOR: {
                try {
                    final JSONObject json = FileTools.readJSON(this.getAbsolutePath());
                    if (!json.has("name")) {
                        this.parseError = ParseError.ANIMATION_NAME_MISSING;
                        return;
                    }
                    this.textureName = json.getString("name");
                    if (!json.has("tile")) {
                        this.parseError = ParseError.ANIMATION_TILE_MISSING;
                        return;
                    }
                    this.tileToAnimate = json.getString("tile");
                    if (json.has("delay")) {
                        this.delay = json.optInt("delay");
                        if (this.delay < 1) {
                            this.parseError = ParseError.ANIMATION_INVALID_DELAY;
                        }
                    }
                    else {
                        this.delay = 1;
                    }
                }
                catch (Exception ex) {
                    this.parseError = ParseError.ANIMATION_INVALID_JSON;
                    ex.printStackTrace();
                }
                break;
            }
        }
        this.isValid = true;
    }
    
    public JSONObject constructAnimation() throws JSONException {
        if (!this.isValid) {
            return null;
        }
        final JSONObject jsonObject = new JSONObject();
        final StringBuilder sb = new StringBuilder();
        sb.append(this.tileToAnimate);
        sb.append("");
        jsonObject.put("atlas_tile", (Object)sb.toString());
        final StringBuilder sb2 = new StringBuilder();
        sb2.append(this.textureName);
        sb2.append("");
        jsonObject.put("flipbook_texture", (Object)sb2.toString());
        jsonObject.put("ticks_per_frame", this.delay);
        return jsonObject;
    }
    
    public boolean isValid() {
        return this.isValid;
    }
}
