package com.zhekasmirnov.innercore.api.mod.ui.types;

import android.graphics.*;
import org.mozilla.javascript.*;
import com.zhekasmirnov.innercore.api.mod.ui.memory.*;
import java.util.*;

public class UIStyle
{
    public static final UIStyle CLASSIC;
    public static final UIStyle DEFAULT;
    public static final UIStyle LEGACY;
    private ArrayList<UIStyle> additionalStyles;
    private UIStyle parent;
    private final HashMap<String, Object> properties;
    private HashMap<String, String> styleBindings;
    
    static {
        final UIStyle uiStyle = null;
        DEFAULT = new UIStyle(uiStyle);
        LEGACY = new UIStyle(uiStyle);
        CLASSIC = UIStyle.DEFAULT;
        UIStyle.LEGACY.setProperty("window_background", Color.rgb(198, 198, 198));
        UIStyle.LEGACY.setProperty("header_background", Color.rgb(198, 198, 198));
        UIStyle.LEGACY.setProperty("tab_background", Color.rgb(140, 140, 140));
        UIStyle.LEGACY.setProperty("tab_background_selected", Color.rgb(198, 198, 198));
        UIStyle.LEGACY.setProperty("default_font_color", Color.rgb(77, 77, 77));
        UIStyle.LEGACY.setProperty("default_font_shadow", 0.0f);
        UIStyle.LEGACY.setProperty("close_button_size", 18);
        UIStyle.LEGACY.setProperty("tab_frame_width", 2);
        UIStyle.LEGACY.addBinding("none", "_default_slot_empty");
        UIStyle.LEGACY.addBinding("slot", "_default_slot_light");
        UIStyle.LEGACY.addBinding("inv_slot", "_default_slot");
        UIStyle.LEGACY.addBinding("close_button_up", "close_button_up");
        UIStyle.LEGACY.addBinding("close_button_down", "close_button_down");
        UIStyle.LEGACY.addBinding("frame_background", "default_frame_bg");
        UIStyle.LEGACY.addBinding("frame_background_border", "default_frame_bg_border");
        UIStyle.LEGACY.addBinding("frame_background_light", "default_frame_bg_light");
        UIStyle.LEGACY.addBinding("frame_background_dark", "default_frame_bg_dark");
        UIStyle.LEGACY.addBinding("frame_tin", "default_frame_tin");
        UIStyle.LEGACY.addBinding("frame_tin_border", "default_frame_tin_border");
        UIStyle.LEGACY.addBinding("frame_tin_inverse", "default_frame_tin_inverse");
        UIStyle.LEGACY.addBinding("frame_slot", "default_frame_slot");
        UIStyle.LEGACY.addBinding("frame_slot_dark", "default_frame_slot_dark");
        UIStyle.LEGACY.addBinding("frame_input", "classic_frame_input");
        UIStyle.LEGACY.addBinding("frame_header", "default_header_frame");
        UIStyle.LEGACY.addBinding("frame_container", "default_container_frame");
        UIStyle.LEGACY.addBinding("frame_tab_left", "default_frame_tab_left");
        UIStyle.LEGACY.addBinding("frame_tab_right", "default_frame_tab_right");
        UIStyle.LEGACY.addBinding("button_up", "default_button_up");
        UIStyle.LEGACY.addBinding("button_hover", "default_button_up");
        UIStyle.LEGACY.addBinding("button_down", "default_button_down");
        UIStyle.LEGACY.addBinding("border_button_up", "default_border_button_up");
        UIStyle.LEGACY.addBinding("border_button_hover", "default_border_button_up");
        UIStyle.LEGACY.addBinding("border_button_down", "default_border_button_down");
        UIStyle.LEGACY.addBinding("selection", "_selection");
        UIStyle.DEFAULT.setProperty("window_background", Color.rgb(198, 198, 198));
        UIStyle.DEFAULT.setProperty("header_background", Color.rgb(198, 198, 198));
        UIStyle.DEFAULT.setProperty("tab_background", Color.rgb(140, 140, 140));
        UIStyle.DEFAULT.setProperty("tab_background_selected", Color.rgb(198, 198, 198));
        UIStyle.DEFAULT.setProperty("default_font_color", Color.rgb(77, 77, 77));
        UIStyle.DEFAULT.setProperty("default_font_shadow", 0.0f);
        UIStyle.DEFAULT.setProperty("close_button_size", 15);
        UIStyle.DEFAULT.setProperty("tab_frame_width", 3);
        UIStyle.DEFAULT.addBinding("slot", "classic_slot");
        UIStyle.DEFAULT.addBinding("inv_slot", "classic_slot");
        UIStyle.DEFAULT.addBinding("close_button_up", "classic_close_button");
        UIStyle.DEFAULT.addBinding("close_button_down", "classic_close_button_down");
        UIStyle.DEFAULT.addBinding("frame_background", "classic_frame_bg");
        UIStyle.DEFAULT.addBinding("frame_background_border", "classic_frame_bg_light_border");
        UIStyle.DEFAULT.addBinding("frame_background_light", "classic_frame_bg_light");
        UIStyle.DEFAULT.addBinding("frame_background_dark", "classic_frame_bg_dark");
        UIStyle.DEFAULT.addBinding("frame_tin", "classic_frame_tin");
        UIStyle.DEFAULT.addBinding("frame_tin_border", "classic_frame_tin_border");
        UIStyle.DEFAULT.addBinding("frame_tin_inverse", "classic_frame_tin_inverse");
        UIStyle.DEFAULT.addBinding("frame_slot", "classic_frame_slot");
        UIStyle.DEFAULT.addBinding("frame_slot_dark", "classic_frame_slot");
        UIStyle.DEFAULT.addBinding("frame_input", "classic_frame_input");
        UIStyle.DEFAULT.addBinding("frame_header", "classic_frame_bg_light");
        UIStyle.DEFAULT.addBinding("frame_container", "default_container_frame");
        UIStyle.DEFAULT.addBinding("frame_tab_left", "classic_frame_tab_left");
        UIStyle.DEFAULT.addBinding("frame_tab_right", "classic_frame_tab_right");
        UIStyle.DEFAULT.addBinding("button_up", "classic_button_up");
        UIStyle.DEFAULT.addBinding("button_hover", "classic_button_hover");
        UIStyle.DEFAULT.addBinding("button_down", "classic_button_down");
        UIStyle.DEFAULT.addBinding("border_button_up", "classic_border_button_up");
        UIStyle.DEFAULT.addBinding("border_button_hover", "classic_border_button_hover");
        UIStyle.DEFAULT.addBinding("selection", "_selection");
    }
    
    public UIStyle() {
        this(UIStyle.DEFAULT);
    }
    
    private UIStyle(final UIStyle uiStyle) {
        this.parent = null;
        this.styleBindings = new HashMap<String, String>();
        this.additionalStyles = new ArrayList<UIStyle>();
        this.properties = new HashMap<String, Object>();
        this.inherit(uiStyle);
    }
    
    public UIStyle(final ScriptableObject scriptableObject) {
        this();
        this.addAllBindings(scriptableObject);
    }
    
    public static BitmapWrap getBitmapByDescription(UIStyle uiStyle, String resolveParameterString) {
        if (uiStyle == null) {
            uiStyle = UIStyle.DEFAULT;
        }
        final int index = resolveParameterString.indexOf(91);
        int n = -1;
        if (index != -1) {
            final int lastIndex = resolveParameterString.lastIndexOf(93);
            if (lastIndex != -1) {
                final String substring = resolveParameterString.substring(0, index);
                resolveParameterString = (String)resolveParameterString(resolveParameterString.substring(index + 1, lastIndex));
                if (substring.hashCode() == 97692013) {
                    if (substring.equals("frame")) {
                        n = 0;
                    }
                }
                if (n == 0) {
                    if (((HashMap)resolveParameterString).containsKey("name")) {
                        uiStyle = (UIStyle)FrameTextureSource.getFrameTexture(uiStyle.getBitmapName(((HashMap<K, ArrayList<String>>)resolveParameterString).get("name").get(0)));
                        final ArrayList<String> list = ((HashMap<K, ArrayList<String>>)resolveParameterString).get("size");
                        if (list != null && list.size() == 2) {
                            try {
                                final int int1 = Integer.parseInt(list.get(0));
                                final int int2 = Integer.parseInt(list.get(1));
                                int n2 = ((FrameTexture)uiStyle).getCentralColor();
                                if (((HashMap)resolveParameterString).containsKey("color")) {
                                    final String s = ((HashMap<K, ArrayList<String>>)resolveParameterString).get("color").get(0);
                                    try {
                                        n2 = Color.parseColor(s);
                                    }
                                    catch (IllegalArgumentException ex) {
                                        n2 = n2;
                                    }
                                }
                                final boolean[] array2;
                                final boolean[] array = array2 = new boolean[4];
                                array2[1] = (array2[0] = true);
                                array2[3] = (array2[2] = true);
                                if (((HashMap)resolveParameterString).containsKey("sides")) {
                                    final ArrayList list2 = ((HashMap<K, ArrayList>)resolveParameterString).get("sides");
                                    array[2] = list2.contains("up");
                                    array[3] = list2.contains("down");
                                    array[0] = list2.contains("left");
                                    array[1] = list2.contains("right");
                                }
                                return BitmapWrap.wrap(((FrameTexture)uiStyle).expand(int1, int2, n2, array));
                            }
                            catch (NumberFormatException ex2) {}
                        }
                    }
                }
            }
            return BitmapWrap.wrap("missing_bitmap");
        }
        return BitmapWrap.wrap(uiStyle.getBitmapName(resolveParameterString));
    }
    
    private static HashMap<String, ArrayList<String>> resolveParameterString(final String s) {
        final String[] split = s.split("\\s");
        final HashMap<String, ArrayList<String>> hashMap = new HashMap<String, ArrayList<String>>();
        for (int length = split.length, i = 0; i < length; ++i) {
            final String s2 = split[i];
            if (s2.length() > 0) {
                final String[] split2 = s2.split("=");
                if (split2.length == 2) {
                    final String s3 = split2[0];
                    final String[] split3 = split2[1].split(",");
                    final ArrayList<String> list = new ArrayList<String>();
                    for (int length2 = split3.length, j = 0; j < length2; ++j) {
                        final String s4 = split3[j];
                        if (s4.length() > 0) {
                            list.add(s4);
                        }
                    }
                    if (list.size() > 0) {
                        hashMap.put(s3, list);
                    }
                }
            }
        }
        return hashMap;
    }
    
    public void addAllBindings(final ScriptableObject scriptableObject) {
        final Object[] allIds = scriptableObject.getAllIds();
        for (int length = allIds.length, i = 0; i < length; ++i) {
            final Object o = allIds[i];
            final String string = o.toString();
            final Object value = scriptableObject.get(o);
            if (value instanceof CharSequence) {
                this.addBinding(string, value.toString());
            }
        }
    }
    
    public void addBinding(final String s, final String s2) {
        this.styleBindings.put(s, s2);
    }
    
    public void addStyle(final UIStyle uiStyle) {
        if (uiStyle != null) {
            this.additionalStyles.add(uiStyle);
        }
    }
    
    public UIStyle copy() {
        final UIStyle uiStyle = new UIStyle(this.parent);
        for (final String s : this.styleBindings.keySet()) {
            uiStyle.addBinding(s, this.styleBindings.get(s));
        }
        return uiStyle;
    }
    
    public Collection<String> getAllBindingNames() {
        return this.styleBindings.keySet();
    }
    
    public String getBinding(final String s, final String s2) {
        for (final UIStyle uiStyle : this.additionalStyles) {
            if (uiStyle.styleBindings.containsKey(s)) {
                return uiStyle.styleBindings.get(s);
            }
        }
        if (this.styleBindings.containsKey(s)) {
            return this.styleBindings.get(s);
        }
        if (this.parent != null) {
            return this.parent.getBinding(s, s2);
        }
        return s2;
    }
    
    public String getBitmapName(String substring) {
        if (substring.startsWith("style:")) {
            substring = substring.substring(6);
            return this.getBinding(substring, substring);
        }
        return substring;
    }
    
    public boolean getBooleanProperty(final String s, final boolean b) {
        if (this.properties.containsKey(s)) {
            return this.properties.get(s);
        }
        if (this.parent != null) {
            return this.parent.getBooleanProperty(s, b);
        }
        return b;
    }
    
    public double getDoubleProperty(final String s, final double n) {
        if (this.properties.containsKey(s)) {
            return this.properties.get(s).doubleValue();
        }
        if (this.parent != null) {
            return this.parent.getDoubleProperty(s, n);
        }
        return n;
    }
    
    public float getFloatProperty(final String s, final float n) {
        if (this.properties.containsKey(s)) {
            return this.properties.get(s).floatValue();
        }
        if (this.parent != null) {
            return this.parent.getFloatProperty(s, n);
        }
        return n;
    }
    
    public int getIntProperty(final String s, final int n) {
        if (this.properties.containsKey(s)) {
            return this.properties.get(s).intValue();
        }
        if (this.parent != null) {
            return this.parent.getIntProperty(s, n);
        }
        return n;
    }
    
    public String getStringProperty(final String s, final String s2) {
        if (this.properties.containsKey(s)) {
            final StringBuilder sb = new StringBuilder();
            sb.append(this.properties.get(s));
            sb.append("");
            return sb.toString();
        }
        if (this.parent != null) {
            return this.parent.getStringProperty(s, s2);
        }
        return s2;
    }
    
    public void inherit(final UIStyle parent) {
        if (parent != null) {
            this.parent = parent;
        }
    }
    
    public void setProperty(final String s, final Object o) {
        this.properties.put(s, o);
    }
}
