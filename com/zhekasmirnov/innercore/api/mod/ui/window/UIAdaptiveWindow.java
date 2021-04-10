package com.zhekasmirnov.innercore.api.mod.ui.window;

import com.zhekasmirnov.innercore.api.mod.*;
import android.graphics.*;
import com.zhekasmirnov.innercore.api.runtime.other.*;
import com.zhekasmirnov.innercore.api.mod.ui.types.*;
import android.annotation.*;
import com.zhekasmirnov.innercore.api.mod.ui.container.*;
import com.zhekasmirnov.innercore.api.*;
import org.mozilla.javascript.*;
import android.util.*;
import java.util.*;

public class UIAdaptiveWindow extends UIWindowGroup
{
    private UIProfile classicProfile;
    private ScriptableObject content;
    private UIProfile defaultProfile;
    private int forcedProfile;
    
    public UIAdaptiveWindow(final ScriptableObject content) {
        this.content = null;
        this.forcedProfile = -1;
        this.setContent(content);
    }
    
    @SuppressLint({ "DefaultLocale" })
    private void initializeClassicProfile(final ScriptableObjectWrapper scriptableObjectWrapper) {
        final WindowOptions windowOptions = new WindowOptions(scriptableObjectWrapper.getScriptableWrapper("options"));
        final float max = Math.max(windowOptions.height, 150.0f);
        final float n = max + 500.0f + 100.0f;
        final UIWindowLocation uiWindowLocation = new UIWindowLocation();
        final float windowToGlobal = uiWindowLocation.windowToGlobal((float)uiWindowLocation.getWindowWidth());
        final float windowToGlobal2 = uiWindowLocation.windowToGlobal((float)uiWindowLocation.getWindowHeight());
        final float n2 = 0.28f * windowToGlobal;
        uiWindowLocation.setPadding(0.0f, 0.0f, n2, n2);
        final float n3 = (windowToGlobal2 - uiWindowLocation.windowToGlobal(n)) / 2.0f;
        uiWindowLocation.setPadding(n3, n3, n2, n2);
        final UIWindowLocation uiWindowLocation2 = new UIWindowLocation();
        uiWindowLocation2.setPadding(uiWindowLocation.windowToGlobal(100.0f) + n3, n3 + uiWindowLocation.windowToGlobal(500.0f), n2, n2);
        final ScriptableObjectWrapper scriptableObjectWrapper2 = new ScriptableObjectWrapper("{\"elements\": {}, \"drawing\":[]}");
        final ScriptableObjectWrapper scriptableWrapper = scriptableObjectWrapper.getScriptableWrapper("style");
        if (scriptableWrapper != null) {
            scriptableObjectWrapper2.put("style", scriptableWrapper);
        }
        final ScriptableObjectWrapper scriptableWrapper2 = scriptableObjectWrapper2.getScriptableWrapper("drawing");
        scriptableWrapper2.insert(0, new ScriptableObjectWrapper("{\"type\": \"color\", \"color\": 0}"));
        scriptableWrapper2.insert(1, new ScriptableObjectWrapper(String.format("{\"type\": \"frame\", \"bitmap\": \"style:frame_background_border\", \"x\": 0, \"y\": 0, \"scale\": 5.555, \"width\": %f, \"height\": %f}", 1000.0f, n)));
        ScriptableObjectWrapper scriptableWrapper3;
        if ((scriptableWrapper3 = scriptableObjectWrapper2.getScriptableWrapper("elements")) == null) {
            scriptableWrapper3 = new ScriptableObjectWrapper("{}");
            scriptableObjectWrapper2.put("elements", scriptableWrapper3);
        }
        if (windowOptions.isCloseButtonShown) {
            scriptableWrapper3.put("_adapted_close_button", new ScriptableObjectWrapper("{\"type\":\"close_button\", \"bitmap\":\"style:close_button_up\", \"bitmap2\":\"style:close_button_down\", \"scale\": 8, \"x\": 910, \"y\": 50}"));
        }
        Font access$200 = windowOptions.titleFont;
        if (access$200 == null) {
            access$200 = new Font(Color.parseColor("#383838"), 45.0f, 0.0f);
        }
        if (windowOptions.titleText != null) {
            final ScriptableObjectWrapper scriptableObjectWrapper3 = new ScriptableObjectWrapper(String.format("{\"type\": \"text\", \"x\": 36, \"y\": 40, \"text\": \"%s\"}", windowOptions.titleText));
            scriptableObjectWrapper3.put("font", access$200.asScriptable());
            scriptableWrapper3.put("_adapted_window_title", scriptableObjectWrapper3);
        }
        final ScriptableObjectWrapper scriptableObjectWrapper4 = new ScriptableObjectWrapper(String.format("{\"type\": \"text\", \"x\": 36, \"y\": %f, \"text\": \"%s\"}", max + 100.0f - 28.0f, NameTranslation.translate("Inventory")));
        scriptableObjectWrapper4.put("font", access$200.asScriptable());
        scriptableWrapper3.put("_adapted_inv_label", scriptableObjectWrapper4);
        int n4 = 0;
        for (int i = 0; i < 4; ++i) {
            for (int j = 0; j < 9; ++j) {
                final StringBuilder sb = new StringBuilder();
                sb.append("_adapted_inv_slot");
                sb.append(n4);
                final String string = sb.toString();
                final float n5 = (float)j;
                float n6;
                if (i == 3) {
                    n6 = n - 136.0f;
                }
                else {
                    n6 = n - 256.0f - i * 104;
                }
                int n7;
                if (i == 3) {
                    n7 = n4 - 18;
                }
                else {
                    n7 = n4 % 9 + 18 + (2 - n4 / 9) * 9;
                }
                scriptableWrapper3.put(string, new ScriptableObjectWrapper(String.format("{\"type\": \"invslot\", \"x\": %f, \"y\": %f, \"size\": 104, \"index\": %d}", n5 * 104.0f + 32.0f, n6, n7)));
                ++n4;
            }
        }
        this.classicProfile = new UIProfile(UIStyle.CLASSIC);
        final UIWindow access$201 = this.classicProfile.addWindow("background", scriptableObjectWrapper2);
        access$201.getLocation().set(uiWindowLocation);
        access$201.setBlockingBackground(true);
        final UIWindow access$202 = this.classicProfile.addWindow("main", scriptableObjectWrapper);
        access$202.getLocation().set(uiWindowLocation2);
        access$202.setInventoryNeeded(true);
    }
    
    @SuppressLint({ "DefaultLocale" })
    private void initializeDefaultProfile(final ScriptableObjectWrapper scriptableObjectWrapper) {
        final WindowOptions windowOptions = new WindowOptions(scriptableObjectWrapper.getScriptableWrapper("options"));
        final UIWindowLocation uiWindowLocation = new UIWindowLocation();
        final UIWindowLocation uiWindowLocation2 = new UIWindowLocation();
        uiWindowLocation2.setPadding(110.0f, 30.0f, 30.0f, 690.0f);
        uiWindowLocation2.setScroll(0, (int)uiWindowLocation2.windowToGlobal(2250.0f));
        final UIWindowLocation uiWindowLocation3 = new UIWindowLocation();
        uiWindowLocation3.setPadding(100.0f, 20.0f, 320.0f, 0.0f);
        uiWindowLocation3.setScroll(0, (int)uiWindowLocation3.windowToGlobal(windowOptions.height));
        final ScriptableObjectWrapper scriptableWrapper = scriptableObjectWrapper.getScriptableWrapper("style");
        final ScriptableObjectWrapper scriptableObjectWrapper2 = new ScriptableObjectWrapper("{\"elements\": {}, \"drawing\":[]}");
        if (scriptableWrapper != null) {
            scriptableObjectWrapper2.put("style", scriptableWrapper);
        }
        final ScriptableObjectWrapper scriptableWrapper2 = scriptableObjectWrapper2.getScriptableWrapper("drawing");
        scriptableWrapper2.insert(0, new ScriptableObjectWrapper("{\"type\": \"color\", \"color\": 0}"));
        scriptableWrapper2.insert(1, new ScriptableObjectWrapper(String.format("{\"type\": \"frame\", \"bitmap\": \"style:frame_background_border\", \"x\": 0, \"y\": 0, \"scale\": 3, \"width\": %d, \"height\": %d}", 1000, uiWindowLocation.getWindowHeight())));
        scriptableWrapper2.insert(2, new ScriptableObjectWrapper("{\"type\": \"image\", \"bitmap\": \"_standart_header_shadow\", \"scale\": 2, \"x\": 0, \"y\": 75}"));
        scriptableWrapper2.insert(3, new ScriptableObjectWrapper("{\"type\": \"frame\", \"bitmap\": \"style:frame_header\", \"x\": 0, \"y\": 0, \"scale\": 3, \"width\": 1000, \"height\": 80}"));
        scriptableWrapper2.insert(4, new ScriptableObjectWrapper(String.format("{\"type\": \"frame\", \"bitmap\": \"style:frame_container\", \"x\": 20, \"y\": 100, \"scale\": 3, \"width\": %d, \"height\": %d}", 300, uiWindowLocation.height - 120)));
        final ScriptableObjectWrapper scriptableWrapper3 = scriptableObjectWrapper2.getScriptableWrapper("elements");
        scriptableWrapper3.put("_adapted_close_button", new ScriptableObjectWrapper("{\"type\":\"close_button\", \"bitmap\":\"style:close_button_up\", \"bitmap2\":\"style:close_button_down\", \"scale\": 3, \"x\": 933, \"y\": 13}"));
        Font access$200;
        if ((access$200 = windowOptions.titleFont) == null) {
            access$200 = new Font(-1, 22.0f, 0.65f);
        }
        access$200.alignment = 1;
        if (windowOptions.titleText != null) {
            final ScriptableObjectWrapper scriptableObjectWrapper3 = new ScriptableObjectWrapper(String.format("{\"type\": \"text\", \"x\": 500, \"y\": 20, \"text\": \"%s\"}", windowOptions.titleText));
            scriptableObjectWrapper3.put("font", access$200.asScriptable());
            scriptableWrapper3.put("_adapted_window_title", scriptableObjectWrapper3);
        }
        final ScriptableObjectWrapper scriptableObjectWrapper4 = new ScriptableObjectWrapper("{\"elements\": {}, \"drawing\":[]}");
        if (scriptableWrapper != null) {
            scriptableObjectWrapper4.put("style", scriptableWrapper);
        }
        scriptableObjectWrapper4.getScriptableWrapper("drawing").insert(0, new ScriptableObjectWrapper("{\"type\": \"color\", \"color\": 0}"));
        final ScriptableObjectWrapper scriptableWrapper4 = scriptableObjectWrapper4.getScriptableWrapper("elements");
        int n = 0;
        for (int i = 0; i < 9; ++i) {
            for (int j = 0; j < 4; ++j) {
                final StringBuilder sb = new StringBuilder();
                sb.append("_adapted_inv_slot");
                sb.append(n);
                scriptableWrapper4.put(sb.toString(), new ScriptableObjectWrapper(String.format("{\"type\": \"invslot\", \"x\": %f, \"y\": %f, \"size\": 250, \"index\": %d}", j * 250.0f, i * 250.0f, n + 9)));
                ++n;
            }
        }
        this.defaultProfile = new UIProfile(UIStyle.DEFAULT);
        this.defaultProfile.addWindow("background", scriptableObjectWrapper2).getLocation().set(uiWindowLocation);
        this.defaultProfile.addWindow("inventory", scriptableObjectWrapper4).getLocation().set(uiWindowLocation2);
        final UIWindow access$201 = this.defaultProfile.addWindow("main", scriptableObjectWrapper);
        access$201.getLocation().set(uiWindowLocation3);
        access$201.setInventoryNeeded(true);
    }
    
    private void initializeTransparentBackground(final ScriptableObjectWrapper scriptableObjectWrapper) {
        final ScriptableObjectWrapper scriptableWrapper = scriptableObjectWrapper.getScriptableWrapper("drawing");
        ScriptableObjectWrapper scriptableObjectWrapper2 = null;
        Label_0038: {
            if (scriptableWrapper != null) {
                scriptableObjectWrapper2 = scriptableWrapper;
                if (scriptableWrapper.isArray()) {
                    break Label_0038;
                }
            }
            scriptableObjectWrapper2 = new ScriptableObjectWrapper("[]");
            scriptableObjectWrapper.put("drawing", scriptableObjectWrapper2);
        }
        scriptableObjectWrapper2.insert(0, new ScriptableObjectWrapper("{\"type\": \"color\", \"color\": 0}"));
    }
    
    private void setProfile(final UIProfile uiProfile) {
        if (uiProfile == null) {
            return;
        }
        final boolean opened = this.isOpened();
        final UiAbstractContainer container = this.getContainer();
        if (opened) {
            this.close();
        }
        final Collection<String> windowNames = this.getWindowNames();
        while (windowNames.size() > 0) {
            this.removeWindow(windowNames.iterator().next());
        }
        uiProfile.setProfileTo(this);
        if (opened) {
            if (container != null) {
                container.openAs(this);
                return;
            }
            this.open();
        }
    }
    
    @Override
    public void open() {
        if (!this.isOpened()) {
            int forcedProfile;
            if (this.forcedProfile != -1) {
                forcedProfile = this.forcedProfile;
            }
            else {
                forcedProfile = (NativeAPI.getUiProfile() & 0x1);
            }
            this.setProfile(forcedProfile);
        }
        super.open();
    }
    
    public void setContent(final ScriptableObject content) {
        this.content = content;
        final ScriptableObjectWrapper scriptableObjectWrapper = new ScriptableObjectWrapper((Scriptable)content);
        this.initializeTransparentBackground(scriptableObjectWrapper);
        this.initializeDefaultProfile(scriptableObjectWrapper);
        this.initializeClassicProfile(scriptableObjectWrapper);
    }
    
    public void setForcedProfile(final int forcedProfile) {
        this.forcedProfile = forcedProfile;
    }
    
    public void setProfile(final int n) {
        UIProfile profile;
        if (n == 0) {
            profile = this.classicProfile;
        }
        else {
            profile = this.defaultProfile;
        }
        this.setProfile(profile);
    }
    
    private class UIProfile
    {
        UIStyle style;
        ArrayList<Pair<String, UIWindow>> windows;
        
        private UIProfile(final UIStyle style) {
            this.windows = new ArrayList<Pair<String, UIWindow>>();
            this.style = style;
        }
        
        private UIWindow addWindow(final String s, final ScriptableObjectWrapper scriptableObjectWrapper) {
            final UIWindow uiWindow = new UIWindow(scriptableObjectWrapper.getWrappedObject());
            this.addWindow(s, uiWindow);
            uiWindow.setDynamic(true);
            return uiWindow;
        }
        
        private void addWindow(final String s, final UIWindow uiWindow) {
            this.windows.add((Pair<String, UIWindow>)new Pair((Object)s, (Object)uiWindow));
        }
        
        private void setProfileTo(final UIWindowGroup uiWindowGroup) {
            for (final Pair<String, UIWindow> pair : this.windows) {
                uiWindowGroup.addWindowInstance((String)pair.first, (IWindow)pair.second);
            }
            uiWindowGroup.setStyle(this.style);
        }
    }
    
    private class WindowOptions
    {
        private float height;
        private boolean isCloseButtonShown;
        private Font titleFont;
        private String titleText;
        
        private WindowOptions(final UIAdaptiveWindow uiAdaptiveWindow, final ScriptableObjectWrapper scriptableObjectWrapper) {
            Scriptable wrapped;
            if (scriptableObjectWrapper != null) {
                wrapped = scriptableObjectWrapper.getWrapped();
            }
            else {
                wrapped = null;
            }
            this(uiAdaptiveWindow, wrapped);
        }
        
        private WindowOptions(final Scriptable scriptable) {
            this.titleText = null;
            this.titleFont = null;
            this.isCloseButtonShown = true;
            this.height = 500.0f;
            if (scriptable != null) {
                final ScriptableObjectWrapper scriptableObjectWrapper = new ScriptableObjectWrapper(scriptable);
                this.isCloseButtonShown = scriptableObjectWrapper.getBoolean("close_button", true);
                final ScriptableObjectWrapper scriptableWrapper = scriptableObjectWrapper.getScriptableWrapper("header");
                if (scriptableWrapper != null) {
                    this.titleText = scriptableWrapper.getString("text");
                    final ScriptableObjectWrapper scriptableWrapper2 = scriptableWrapper.getScriptableWrapper("font");
                    if (scriptableWrapper2 != null) {
                        this.titleFont = new Font(scriptableWrapper2.getWrappedObject());
                    }
                }
                this.height = scriptableObjectWrapper.getFloat("height", 500.0f);
            }
        }
    }
}
