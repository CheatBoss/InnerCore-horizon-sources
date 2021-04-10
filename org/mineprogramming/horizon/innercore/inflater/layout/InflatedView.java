package org.mineprogramming.horizon.innercore.inflater.layout;

import android.view.*;
import java.util.*;
import org.mineprogramming.horizon.innercore.inflater.*;

public class InflatedView
{
    private final Map<String, View> jsonIdMap;
    private final Map<String, Integer> realIdMap;
    private View view;
    
    InflatedView() {
        this.jsonIdMap = new HashMap<String, View>();
        this.realIdMap = new HashMap<String, Integer>();
    }
    
    void addJsonId(final String s, final View view) throws JsonInflaterException {
        if (this.jsonIdMap.containsKey(s)) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Duplicate string ID found in layout: ");
            sb.append(s);
            throw new JsonInflaterException(sb.toString());
        }
        this.jsonIdMap.put(s, view);
        final int generateViewId = View.generateViewId();
        view.setId(generateViewId);
        this.realIdMap.put(s, generateViewId);
    }
    
    public int getRealId(final String s) {
        return this.realIdMap.get(s);
    }
    
    public View getView() {
        return this.view;
    }
    
    public <T extends View> T getViewByJsonId(final String s) {
        return (T)this.jsonIdMap.get(s);
    }
    
    void setView(final View view) {
        this.view = view;
    }
}
