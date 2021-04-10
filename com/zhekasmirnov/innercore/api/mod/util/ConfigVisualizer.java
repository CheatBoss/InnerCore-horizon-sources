package com.zhekasmirnov.innercore.api.mod.util;

import com.zhekasmirnov.innercore.api.mod.ui.types.*;
import com.zhekasmirnov.innercore.mod.build.*;
import java.util.*;
import com.zhekasmirnov.innercore.api.mod.*;
import org.mozilla.javascript.*;

public class ConfigVisualizer
{
    private static Font font;
    private static Font font2;
    private static int uniqueIndex;
    private Config config;
    private float currentYOffset;
    private String prefix;
    
    static {
        ConfigVisualizer.font = new Font(-1, 30.0f, 0.45f);
        ConfigVisualizer.font2 = new Font(-1, 30.0f, 0.45f);
        ConfigVisualizer.font2.alignment = 2;
        ConfigVisualizer.uniqueIndex = 0;
    }
    
    public ConfigVisualizer(final Config config) {
        this(config, "config_vis");
    }
    
    public ConfigVisualizer(final Config config, final String prefix) {
        this.currentYOffset = 0.0f;
        this.config = config;
        this.prefix = prefix;
    }
    
    private void createVisualContent(final ScriptableObject scriptableObject, final Config config, final float n, final float n2, final float n3) {
        for (final String s : config.getNames()) {
            final Object value = config.get(s);
            if (value instanceof Config) {
                final float currentYOffset = this.currentYOffset;
                final StringBuilder sb = new StringBuilder();
                sb.append(s);
                sb.append(": ");
                this.visualizeName(n, n2 + currentYOffset, n3, scriptableObject, sb.toString());
                this.currentYOffset += 75.0f;
                this.createVisualContent(scriptableObject, (Config)value, n + 75.0f, n2, n3);
            }
            else {
                this.visualizeElement(n, n2 + this.currentYOffset, n3, scriptableObject, config, s);
                this.currentYOffset += 75.0f;
            }
        }
    }
    
    private String getElementName() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.prefix);
        sb.append("_");
        final int uniqueIndex = ConfigVisualizer.uniqueIndex;
        ConfigVisualizer.uniqueIndex = uniqueIndex + 1;
        sb.append(uniqueIndex);
        return sb.toString();
    }
    
    private void visualizeElement(final float n, final float n2, final float n3, final ScriptableObject scriptableObject, final Config config, String s) {
        this.visualizeName(n, n2, n3, scriptableObject, s);
        final float textWidth = ConfigVisualizer.font.getTextWidth(s, 1.0f);
        final Object value = config.get(s);
        float n4;
        if (value instanceof Boolean) {
            n4 = 120.0f;
            final ScriptableObject empty = ScriptableObjectHelper.createEmpty();
            empty.put("type", (Scriptable)empty, (Object)"switch");
            empty.put("x", (Scriptable)empty, (Object)860);
            empty.put("y", (Scriptable)empty, (Object)(n2 + 4.5));
            empty.put("z", (Scriptable)empty, (Object)n3);
            empty.put("state", (Scriptable)empty, value);
            empty.put("configValue", (Scriptable)empty, (Object)config.getValue(s));
            scriptableObject.put(this.getElementName(), (Scriptable)scriptableObject, (Object)empty);
        }
        else {
            final StringBuilder sb = new StringBuilder();
            sb.append(value);
            sb.append("");
            String s2;
            s = (s2 = sb.toString());
            if (value instanceof CharSequence) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("\"");
                sb2.append(s);
                sb2.append("\"");
                s2 = sb2.toString();
            }
            n4 = ConfigVisualizer.font.getTextWidth(s2, 1.0f) + 20.0f;
            final ScriptableObject empty2 = ScriptableObjectHelper.createEmpty();
            empty2.put("type", (Scriptable)empty2, (Object)"text");
            empty2.put("x", (Scriptable)empty2, (Object)960);
            empty2.put("y", (Scriptable)empty2, (Object)(n2 + 22.5));
            empty2.put("z", (Scriptable)empty2, (Object)n3);
            empty2.put("font", (Scriptable)empty2, (Object)ConfigVisualizer.font2.asScriptable());
            empty2.put("text", (Scriptable)empty2, (Object)s2);
            scriptableObject.put(this.getElementName(), (Scriptable)scriptableObject, (Object)empty2);
        }
        final ScriptableObject empty3 = ScriptableObjectHelper.createEmpty();
        empty3.put("type", (Scriptable)empty3, (Object)"image");
        empty3.put("bitmap", (Scriptable)empty3, (Object)"default_horizontal_line_template");
        empty3.put("x", (Scriptable)empty3, (Object)(n + textWidth + 20.0f));
        empty3.put("y", (Scriptable)empty3, (Object)(n2 + 32.5));
        empty3.put("z", (Scriptable)empty3, (Object)n3);
        empty3.put("width", (Scriptable)empty3, (Object)(940.0f - textWidth - n4 - n));
        empty3.put("height", (Scriptable)empty3, (Object)10);
        scriptableObject.put(this.getElementName(), (Scriptable)scriptableObject, (Object)empty3);
    }
    
    private void visualizeName(final float n, final float n2, final float n3, final ScriptableObject scriptableObject, final String s) {
        final ScriptableObject empty = ScriptableObjectHelper.createEmpty();
        empty.put("type", (Scriptable)empty, (Object)"text");
        empty.put("x", (Scriptable)empty, (Object)n);
        empty.put("y", (Scriptable)empty, (Object)(n2 + 22.5));
        empty.put("z", (Scriptable)empty, (Object)n3);
        empty.put("text", (Scriptable)empty, (Object)s);
        empty.put("font", (Scriptable)empty, (Object)ConfigVisualizer.font.asScriptable());
        scriptableObject.put(this.getElementName(), (Scriptable)scriptableObject, (Object)empty);
    }
    
    public void clearVisualContent(final ScriptableObject scriptableObject) {
        final Object[] allIds = scriptableObject.getAllIds();
        for (int length = allIds.length, i = 0; i < length; ++i) {
            final Object o = allIds[i];
            final StringBuilder sb = new StringBuilder();
            sb.append(o);
            sb.append("");
            final String string = sb.toString();
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(this.prefix);
            sb2.append("_");
            if (string.contains(sb2.toString())) {
                final StringBuilder sb3 = new StringBuilder();
                sb3.append(o);
                sb3.append("");
                scriptableObject.put(sb3.toString(), (Scriptable)scriptableObject, (Object)null);
            }
        }
    }
    
    public void createVisualContent(final ScriptableObject scriptableObject, final ScriptableObject scriptableObject2) {
        ScriptableObject empty = scriptableObject2;
        if (scriptableObject2 == null) {
            empty = ScriptableObjectHelper.createEmpty();
        }
        final float floatProperty = ScriptableObjectHelper.getFloatProperty(empty, "x", 0.0f);
        final float floatProperty2 = ScriptableObjectHelper.getFloatProperty(empty, "y", 0.0f);
        final float floatProperty3 = ScriptableObjectHelper.getFloatProperty(empty, "z", 0.0f);
        this.currentYOffset = 0.0f;
        this.createVisualContent(scriptableObject, this.config, floatProperty, floatProperty2, floatProperty3);
    }
}
