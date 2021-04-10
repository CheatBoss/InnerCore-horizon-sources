package com.microsoft.xbox.service.network.managers;

import com.google.gson.annotations.*;

public class ProfilePreferredColor
{
    private int primary;
    @SerializedName("primaryColor")
    private String primaryColorString;
    private int secondary;
    @SerializedName("secondaryColor")
    private String secondaryColorString;
    private int tertiary;
    @SerializedName("tertiaryColor")
    private String tertiaryColorString;
    
    public ProfilePreferredColor() {
        this.primary = -1;
        this.secondary = -1;
        this.tertiary = -1;
    }
    
    public static int convertColorFromString(final String s) {
        if (s == null) {
            return 0;
        }
        String substring = s;
        if (s.startsWith("#")) {
            substring = s.substring(1);
        }
        int int1;
        final int n = int1 = Integer.parseInt(substring, 16);
        if (n >> 24 == 0) {
            int1 = (n | 0xFF000000);
        }
        return int1;
    }
    
    public int getPrimaryColor() {
        if (this.primary < 0) {
            this.primary = convertColorFromString(this.primaryColorString);
        }
        return this.primary;
    }
    
    public int getSecondaryColor() {
        if (this.secondary < 0) {
            this.secondary = convertColorFromString(this.secondaryColorString);
        }
        return this.secondary;
    }
    
    public int getTertiaryColor() {
        if (this.tertiary < 0) {
            this.tertiary = convertColorFromString(this.tertiaryColorString);
        }
        return this.tertiary;
    }
}
