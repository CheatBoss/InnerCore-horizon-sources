package com.zhekasmirnov.innercore.api.mod.ui.window;

import com.zhekasmirnov.innercore.api.mod.*;
import org.mozilla.javascript.*;
import android.graphics.*;
import com.zhekasmirnov.innercore.api.mod.ui.*;
import com.zhekasmirnov.innercore.api.mod.ui.types.*;

public abstract class UIWindowStandard extends UIWindowGroup
{
    private ScriptableObject content;
    private int paddingLeft;
    private int paddingTop;
    private ScriptableObject standardContent;
    
    public UIWindowStandard(final ScriptableObject content) {
        this.paddingTop = 0;
        this.paddingLeft = 0;
        this.setContent(content);
        this.setCloseOnBackPressed(true);
    }
    
    private void setupHeader() {
        this.paddingTop = 0;
        this.removeWindow("header");
        if (this.standardContent == null) {
            return;
        }
        final ScriptableObject scriptableObjectProperty = ScriptableObjectHelper.getScriptableObjectProperty(this.standardContent, "header", null);
        if (scriptableObjectProperty == null) {
            return;
        }
        final WindowContentAdapter windowContentAdapter = new WindowContentAdapter();
        final int intProperty = ScriptableObjectHelper.getIntProperty(scriptableObjectProperty, "height", ScriptableObjectHelper.getIntProperty(scriptableObjectProperty, "width", 80));
        this.paddingTop = intProperty - 20;
        final UIWindowLocation location = new UIWindowLocation();
        location.set(0, 0, 1000, intProperty);
        windowContentAdapter.setLocation(location);
        final String stringProperty = ScriptableObjectHelper.getStringProperty(scriptableObjectProperty, "frame", this.getStyleSafe().getBinding("headerFrame", "style:frame_background_border"));
        final int intProperty2 = ScriptableObjectHelper.getIntProperty(scriptableObjectProperty, "color", this.getStyleSafe().getIntProperty("header_background", 0));
        final ScriptableObject empty = ScriptableObjectHelper.createEmpty();
        empty.put("type", (Scriptable)empty, (Object)"color");
        empty.put("color", (Scriptable)empty, (Object)Color.argb(0, 0, 0, 0));
        windowContentAdapter.addDrawing(empty);
        final ScriptableObject empty2 = ScriptableObjectHelper.createEmpty();
        empty2.put("type", (Scriptable)empty2, (Object)"frame");
        empty2.put("x", (Scriptable)empty2, (Object)0);
        empty2.put("y", (Scriptable)empty2, (Object)0);
        empty2.put("width", (Scriptable)empty2, (Object)1000);
        empty2.put("height", (Scriptable)empty2, (Object)intProperty);
        empty2.put("bitmap", (Scriptable)empty2, (Object)stringProperty);
        empty2.put("color", (Scriptable)empty2, (Object)intProperty2);
        empty2.put("scale", (Scriptable)empty2, (Object)3);
        windowContentAdapter.addDrawing(empty2);
        final ScriptableObject scriptableObjectProperty2 = ScriptableObjectHelper.getScriptableObjectProperty(scriptableObjectProperty, "text", null);
        if (scriptableObjectProperty2 != null) {
            final ScriptableObject empty3 = ScriptableObjectHelper.createEmpty();
            empty3.put("type", (Scriptable)empty3, (Object)"text");
            empty3.put("x", (Scriptable)empty3, (Object)500);
            empty3.put("y", (Scriptable)empty3, (Object)(intProperty * 0.5f));
            empty3.put("text", (Scriptable)empty3, (Object)ScriptableObjectHelper.getStringProperty(scriptableObjectProperty2, "text", "No Title"));
            final ScriptableObject scriptableObjectProperty3 = ScriptableObjectHelper.getScriptableObjectProperty(scriptableObjectProperty2, "font", scriptableObjectProperty2);
            scriptableObjectProperty3.put("align", (Scriptable)scriptableObjectProperty3, (Object)1);
            scriptableObjectProperty3.put("size", (Scriptable)scriptableObjectProperty3, ScriptableObjectHelper.getProperty(scriptableObjectProperty3, "size", intProperty * 0.25f));
            scriptableObjectProperty3.put("color", (Scriptable)scriptableObjectProperty3, ScriptableObjectHelper.getProperty(scriptableObjectProperty3, "color", this.getStyleSafe().getIntProperty("default_font_color", -16777216)));
            scriptableObjectProperty3.put("shadow", (Scriptable)scriptableObjectProperty3, ScriptableObjectHelper.getProperty(scriptableObjectProperty3, "shadow", this.getStyleSafe().getFloatProperty("default_font_shadow", 0.0f)));
            empty3.put("font", (Scriptable)empty3, (Object)scriptableObjectProperty3);
            windowContentAdapter.addDrawing(empty3);
        }
        if (!ScriptableObjectHelper.getBooleanProperty(scriptableObjectProperty, "hideButton", false)) {
            ScriptableObjectHelper.getScriptableObjectProperty(scriptableObjectProperty, "closeButton", null);
            final ScriptableObject empty4 = ScriptableObjectHelper.createEmpty();
            empty4.put("type", (Scriptable)empty4, (Object)"closeButton");
            empty4.put("x", (Scriptable)empty4, (Object)(994.0 - intProperty * 0.75));
            empty4.put("y", (Scriptable)empty4, (Object)15);
            empty4.put("scale", (Scriptable)empty4, (Object)(intProperty / 18 * 0.75));
            empty4.put("bitmap", (Scriptable)empty4, (Object)this.getStyleSafe().getBitmapName("style:close_button_up"));
            empty4.put("bitmap2", (Scriptable)empty4, (Object)this.getStyleSafe().getBitmapName("style:close_button_down"));
            windowContentAdapter.addElement("default-close-button", empty4);
        }
        this.addWindow("header", windowContentAdapter.getContent()).setDynamic(false);
    }
    
    private void setupInventory() {
        this.paddingLeft = 0;
        this.removeWindow("inventory");
        if (this.standardContent == null) {
            return;
        }
        final ScriptableObject scriptableObjectProperty = ScriptableObjectHelper.getScriptableObjectProperty(this.standardContent, "inventory", null);
        if (scriptableObjectProperty == null) {
            return;
        }
        final int intProperty = ScriptableObjectHelper.getIntProperty(scriptableObjectProperty, "width", 300);
        final int intProperty2 = ScriptableObjectHelper.getIntProperty(scriptableObjectProperty, "padding", 20);
        final WindowContentAdapter windowContentAdapter = new WindowContentAdapter();
        final UIWindowLocation location = new UIWindowLocation();
        location.setPadding(2, this.paddingLeft + intProperty2);
        location.setPadding(0, this.paddingTop + intProperty2 + 20);
        location.setPadding(1, intProperty2);
        location.scrollX = intProperty;
        location.width = intProperty;
        location.scrollY = (int)Math.max((float)location.height, intProperty * 2.25f);
        windowContentAdapter.setLocation(location);
        final ScriptableObject empty = ScriptableObjectHelper.createEmpty();
        empty.put("type", (Scriptable)empty, (Object)"color");
        empty.put("color", (Scriptable)empty, (Object)(-16777216));
        windowContentAdapter.addDrawing(empty);
        for (int i = 0; i < 36; ++i) {
            final ScriptableObject empty2 = ScriptableObjectHelper.createEmpty();
            empty2.put("type", (Scriptable)empty2, (Object)"invSlot");
            empty2.put("x", (Scriptable)empty2, (Object)(i % 4 * 250));
            empty2.put("y", (Scriptable)empty2, (Object)(i / 4 * 250));
            empty2.put("size", (Scriptable)empty2, (Object)(250 + 1));
            empty2.put("index", (Scriptable)empty2, (Object)i);
            final StringBuilder sb = new StringBuilder();
            sb.append("__invSlot");
            sb.append(i);
            windowContentAdapter.addElement(sb.toString(), empty2);
        }
        final UIWindow addWindow = this.addWindow("inventory", windowContentAdapter.getContent());
        addWindow.setDynamic(false);
        addWindow.setInventoryNeeded(true);
    }
    
    private void setupMainWindow() {
        this.removeWindow("main");
        this.removeWindow("content");
        final WindowContentAdapter windowContentAdapter = new WindowContentAdapter(this.content);
        final UIWindowLocation location = new UIWindowLocation(ScriptableObjectHelper.getScriptableObjectProperty(this.content, "location", null));
        location.setPadding(0, this.paddingTop);
        WindowContentAdapter windowContentAdapter2 = windowContentAdapter;
        if (this.standardContent != null) {
            final int intProperty = ScriptableObjectHelper.getIntProperty(this.standardContent, "minHeight", 0);
            final ScriptableObject standardContent = this.standardContent;
            ScriptableObject empty;
            if (!this.isLegacyFormat()) {
                empty = ScriptableObjectHelper.createEmpty();
            }
            else {
                empty = null;
            }
            final ScriptableObject scriptableObjectProperty = ScriptableObjectHelper.getScriptableObjectProperty(standardContent, "contentWindow", empty);
            WindowContentAdapter windowContentAdapter3;
            if (scriptableObjectProperty != null) {
                windowContentAdapter3 = new WindowContentAdapter();
                final int intProperty2 = ScriptableObjectHelper.getIntProperty(scriptableObjectProperty, "padding", 20);
                final int intProperty3 = ScriptableObjectHelper.getIntProperty(scriptableObjectProperty, "width", 640);
                final UIWindowLocation location2 = new UIWindowLocation();
                location2.setPadding(2, 1000 - intProperty3 - intProperty2);
                location2.setPadding(3, intProperty2);
                location2.setPadding(0, this.paddingTop + intProperty2 + 20);
                location2.setPadding(1, intProperty2);
                location.scrollX = intProperty3;
                location2.width = intProperty3;
                if (intProperty > location2.height) {
                    location2.scrollY = (int)Math.max((float)location2.height, intProperty3 * intProperty / 1000.0f);
                }
                windowContentAdapter.setLocation(location2);
                this.addWindow("content", windowContentAdapter.getContent()).setBackgroundColor(0);
            }
            else {
                windowContentAdapter3 = windowContentAdapter;
                if (intProperty > location.height) {
                    location.scrollY = intProperty;
                    windowContentAdapter3 = windowContentAdapter;
                }
            }
            final ScriptableObject scriptableObjectProperty2 = ScriptableObjectHelper.getScriptableObjectProperty(this.standardContent, "background", null);
            if (scriptableObjectProperty2 != null) {
                int n = ScriptableObjectHelper.getIntProperty(scriptableObjectProperty2, "color", -1);
                final String stringProperty = ScriptableObjectHelper.getStringProperty(scriptableObjectProperty2, "bitmap", null);
                ScriptableObject scriptableObject = ScriptableObjectHelper.getScriptableObjectProperty(scriptableObjectProperty2, "frame", null);
                if (ScriptableObjectHelper.getBooleanProperty(scriptableObjectProperty2, "standard", ScriptableObjectHelper.getBooleanProperty(scriptableObjectProperty2, "standart", false))) {
                    n = this.getStyleSafe().getIntProperty("window_background", 0);
                    scriptableObject = ScriptableObjectHelper.createEmpty();
                }
                if (scriptableObject != null) {
                    final ScriptableObject empty2 = ScriptableObjectHelper.createEmpty();
                    empty2.put("type", (Scriptable)empty2, (Object)"frame");
                    empty2.put("x", (Scriptable)empty2, (Object)0);
                    empty2.put("y", (Scriptable)empty2, (Object)0);
                    empty2.put("width", (Scriptable)empty2, (Object)location.getWindowWidth());
                    empty2.put("height", (Scriptable)empty2, (Object)location.getWindowHeight());
                    empty2.put("scale", (Scriptable)empty2, (Object)ScriptableObjectHelper.getFloatProperty(scriptableObject, "scale", 3.0f));
                    empty2.put("bitmap", (Scriptable)empty2, (Object)ScriptableObjectHelper.getStringProperty(scriptableObject, "bitmap", this.getStyleSafe().getBinding("frame", "style:frame_background_border")));
                    windowContentAdapter3.insertDrawing(empty2);
                }
                if (stringProperty != null) {
                    final float n2 = (float)(1000 / TextureSource.instance.getSafe(stringProperty).getWidth());
                    final ScriptableObject empty3 = ScriptableObjectHelper.createEmpty();
                    empty3.put("type", (Scriptable)empty3, (Object)"bitmap");
                    empty3.put("x", (Scriptable)empty3, (Object)0);
                    empty3.put("y", (Scriptable)empty3, (Object)0);
                    empty3.put("scale", (Scriptable)empty3, (Object)n2);
                    empty3.put("bitmap", (Scriptable)empty3, (Object)stringProperty);
                    windowContentAdapter3.insertDrawing(empty3);
                }
                final ScriptableObject empty4 = ScriptableObjectHelper.createEmpty();
                empty4.put("type", (Scriptable)empty4, (Object)"color");
                empty4.put("color", (Scriptable)empty4, (Object)n);
                windowContentAdapter3.insertDrawing(empty4);
            }
            final ScriptableObject scriptableObjectProperty3 = ScriptableObjectHelper.getScriptableObjectProperty(this.standardContent, "header", null);
            windowContentAdapter2 = windowContentAdapter3;
            if (scriptableObjectProperty3 != null) {
                windowContentAdapter2 = windowContentAdapter3;
                if (!ScriptableObjectHelper.getBooleanProperty(scriptableObjectProperty3, "hideShadow", false)) {
                    final ScriptableObject empty5 = ScriptableObjectHelper.createEmpty();
                    empty5.put("type", (Scriptable)empty5, (Object)"bitmap");
                    empty5.put("x", (Scriptable)empty5, (Object)0);
                    empty5.put("y", (Scriptable)empty5, (Object)15);
                    empty5.put("scale", (Scriptable)empty5, (Object)2);
                    empty5.put("bitmap", (Scriptable)empty5, (Object)"_standart_header_shadow");
                    windowContentAdapter3.addDrawing(empty5);
                    windowContentAdapter2 = windowContentAdapter3;
                }
            }
        }
        windowContentAdapter2.setLocation(location);
        this.addWindow("main", windowContentAdapter2.getContent());
        this.moveOnTop("inventory");
        this.moveOnTop("content");
        this.moveOnTop("header");
    }
    
    @Override
    public ScriptableObject getContent() {
        return this.content;
    }
    
    public UIStyle getStyleSafe() {
        final UIStyle style = super.getStyle();
        if (style != null) {
            return style;
        }
        return UIStyle.DEFAULT;
    }
    
    protected abstract boolean isLegacyFormat();
    
    public void setContent(ScriptableObject scriptableObjectProperty) {
        this.content = scriptableObjectProperty;
        this.standardContent = ScriptableObjectHelper.getScriptableObjectProperty(scriptableObjectProperty, "standard", ScriptableObjectHelper.getScriptableObjectProperty(scriptableObjectProperty, "standart", null));
        scriptableObjectProperty = ScriptableObjectHelper.getScriptableObjectProperty(scriptableObjectProperty, "style", ScriptableObjectHelper.getScriptableObjectProperty(scriptableObjectProperty, "params", null));
        if (scriptableObjectProperty != null) {
            this.setStyle(new UIStyle(scriptableObjectProperty));
        }
        this.setupHeader();
        this.setupInventory();
        this.setupMainWindow();
    }
}
