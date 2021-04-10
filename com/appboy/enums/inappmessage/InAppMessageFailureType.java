package com.appboy.enums.inappmessage;

import com.appboy.models.*;

public enum InAppMessageFailureType implements IPutIntoJson<String>
{
    DISPLAY_VIEW_GENERATION, 
    IMAGE_DOWNLOAD, 
    TEMPLATE_REQUEST, 
    ZIP_ASSET_DOWNLOAD;
    
    @Override
    public String forJsonPut() {
        final int n = InAppMessageFailureType$1.a[this.ordinal()];
        if (n == 1) {
            return "if";
        }
        if (n == 2) {
            return "tf";
        }
        if (n == 3) {
            return "zf";
        }
        if (n != 4) {
            return null;
        }
        return "vf";
    }
}
