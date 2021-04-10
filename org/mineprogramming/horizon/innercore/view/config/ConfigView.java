package org.mineprogramming.horizon.innercore.view.config;

import org.mineprogramming.horizon.innercore.*;
import android.content.*;
import com.zhekasmirnov.innercore.utils.*;
import org.json.*;
import java.io.*;
import org.mineprogramming.horizon.innercore.util.*;
import java.util.*;
import android.widget.*;
import android.view.*;
import org.mineprogramming.horizon.innercore.inflater.*;
import org.mineprogramming.horizon.innercore.inflater.layout.*;

public class ConfigView extends Displayable
{
    private JSONObject configInfo;
    private final String header;
    private CustomExpandableListAdapter listAdapter;
    private JSONObject object;
    private final String path;
    
    public ConfigView(final Context context, final String header, final String path) {
        super(context);
        this.configInfo = null;
        this.path = path;
        this.header = header;
        try {
            this.object = FileTools.readJSON(path);
        }
        catch (IOException | JSONException ex2) {
            Toast.makeText(context, (CharSequence)JsonInflater.getString(context, "config_failed"), 0).show();
            this.object = new JSONObject();
            try {
                this.object.put("enabled", true);
            }
            catch (JSONException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    @Override
    public void display(final ViewGroup viewGroup) {
        if (this.configInfo == null) {
            this.configInfo = new JSONObject();
        }
        try {
            final InflatedView inflateLayout = JsonInflater.inflateLayout(this.context, viewGroup, ResourceReader.readLayout(this.context, "config"));
            ((TextView)inflateLayout.getViewByJsonId("header")).setText((CharSequence)this.header);
            JSONObject optJSONObject;
            if ((optJSONObject = this.configInfo.optJSONObject("properties")) == null) {
                optJSONObject = new JSONObject();
            }
            this.listAdapter = new CustomExpandableListAdapter(this.context, this.path, optJSONObject);
            final Object opt = this.configInfo.opt("description");
            if (opt != null) {
                final TextView textView = inflateLayout.getViewByJsonId("description");
                if (opt instanceof String) {
                    textView.setText((CharSequence)opt);
                }
                else if (opt instanceof JSONObject) {
                    String text;
                    if ((text = ((JSONObject)opt).optString(Locale.getDefault().getLanguage(), (String)null)) == null) {
                        text = ((JSONObject)opt).optString("en", (String)null);
                    }
                    textView.setText((CharSequence)text);
                }
            }
            final ExpandableListView expandableListView = inflateLayout.getViewByJsonId("settings");
            expandableListView.setAdapter((ExpandableListAdapter)this.listAdapter);
            expandableListView.setOnGroupClickListener((ExpandableListView$OnGroupClickListener)new ExpandableListView$OnGroupClickListener() {
                public boolean onGroupClick(final ExpandableListView expandableListView, final View view, final int n, final long n2) {
                    return ConfigView.this.listAdapter.isCollapsible(n) ^ true;
                }
            });
            for (int i = 0; i < this.listAdapter.getGroupCount(); ++i) {
                if (this.listAdapter.isExpanded(i)) {
                    expandableListView.expandGroup(i);
                }
            }
        }
        catch (JSONException | JsonInflaterException ex) {
            final Object o;
            throw new RuntimeException((Throwable)o);
        }
    }
    
    public void loadInfo(final JSONObject configInfo) {
        this.configInfo = configInfo;
    }
}
