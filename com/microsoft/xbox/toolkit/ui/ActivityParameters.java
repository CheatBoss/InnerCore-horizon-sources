package com.microsoft.xbox.toolkit.ui;

import java.util.*;

public class ActivityParameters extends HashMap<String, Object>
{
    private static final String FF_INFO_TYPE = "InfoType";
    private static final String FORCE_RELOAD = "ForceReload";
    private static final String FROM_SCREEN = "FromScreen";
    private static final String ME_XUID = "MeXuid";
    private static final String ORIGINATING_PAGE = "OriginatingPage";
    private static final String PRIVILEGES = "Privileges";
    private static final String SELECTED_PROFILE = "SelectedProfile";
    
    public String getMeXuid() {
        return ((HashMap<K, String>)this).get("MeXuid");
    }
    
    public String getPrivileges() {
        return ((HashMap<K, String>)this).get("Privileges");
    }
    
    public String getSelectedProfile() {
        return ((HashMap<K, String>)this).get("SelectedProfile");
    }
    
    public boolean isForceReload() {
        return this.containsKey("ForceReload") && ((HashMap<K, Boolean>)this).get("ForceReload");
    }
    
    public void putFromScreen(final ScreenLayout screenLayout) {
        ((HashMap<String, ScreenLayout>)this).put("FromScreen", screenLayout);
    }
    
    public void putMeXuid(final String s) {
        ((HashMap<String, String>)this).put("MeXuid", s);
    }
    
    public void putPrivileges(final String s) {
        ((HashMap<String, String>)this).put("Privileges", s);
    }
    
    public void putSelectedProfile(final String s) {
        ((HashMap<String, String>)this).put("SelectedProfile", s);
    }
    
    public void putSourcePage(final String s) {
        ((HashMap<String, String>)this).put("OriginatingPage", s);
    }
}
