package org.mineprogramming.horizon.innercore.view.config;

import org.mineprogramming.horizon.innercore.view.page.*;
import com.zhekasmirnov.innercore.utils.*;
import org.json.*;
import java.io.*;
import org.mineprogramming.horizon.innercore.model.*;
import org.mineprogramming.horizon.innercore.util.*;
import java.util.*;
import android.widget.*;
import android.view.*;
import org.mineprogramming.horizon.innercore.inflater.*;
import org.mineprogramming.horizon.innercore.inflater.layout.*;

public class ConfigPage extends Page
{
    private JSONObject configInfo;
    private final String header;
    private CustomExpandableListAdapter listAdapter;
    private final String path;
    
    public ConfigPage(final PagesManager pagesManager, final String header, final String path) {
        super(pagesManager);
        this.configInfo = null;
        this.path = path;
        this.header = header;
        if (!FileTools.exists(path)) {
            Toast.makeText(this.context, (CharSequence)JsonInflater.getString(this.context, "config_failed"), 0).show();
            final JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("enabled", true);
                FileTools.writeJSON(path, jsonObject);
            }
            catch (JSONException | IOException ex) {
                final Throwable t;
                t.printStackTrace();
            }
        }
    }
    
    public static ConfigPage forMod(PagesManager pagesManager, final ModItem modItem) {
        final String absolutePath = modItem.getDirectory().getAbsolutePath();
        pagesManager = (PagesManager)new ConfigPage(pagesManager, modItem.getTitle(), modItem.getConfigPath());
        try {
            final StringBuilder sb = new StringBuilder();
            sb.append(absolutePath);
            sb.append("/config.info.json");
            final String string = sb.toString();
            if (FileTools.exists(string)) {
                ((ConfigPage)pagesManager).loadInfo(FileTools.readJSON(string));
            }
            return (ConfigPage)pagesManager;
        }
        catch (IOException | JSONException ex) {
            final Throwable t;
            t.printStackTrace();
            return (ConfigPage)pagesManager;
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
                    return ConfigPage.this.listAdapter.isCollapsible(n) ^ true;
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
