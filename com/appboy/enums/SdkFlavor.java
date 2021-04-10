package com.appboy.enums;

import com.appboy.models.*;

public enum SdkFlavor implements IPutIntoJson<String>
{
    CORDOVA, 
    MPARTICLE, 
    REACT, 
    SEGMENT, 
    UNITY, 
    XAMARIN;
    
    @Override
    public String forJsonPut() {
        switch (SdkFlavor$1.a[this.ordinal()]) {
            default: {
                return null;
            }
            case 6: {
                return "mparticle";
            }
            case 5: {
                return "segment";
            }
            case 4: {
                return "xamarin";
            }
            case 3: {
                return "cordova";
            }
            case 2: {
                return "react";
            }
            case 1: {
                return "unity";
            }
        }
    }
}
