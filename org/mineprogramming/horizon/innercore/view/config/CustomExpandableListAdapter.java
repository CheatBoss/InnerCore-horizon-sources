package org.mineprogramming.horizon.innercore.view.config;

import com.zhekasmirnov.innercore.mod.build.*;
import android.content.*;
import android.graphics.drawable.*;
import org.mineprogramming.horizon.innercore.inflater.drawable.*;
import android.util.*;
import org.mineprogramming.horizon.innercore.inflater.*;
import android.view.*;
import org.mineprogramming.horizon.innercore.util.*;
import android.text.*;
import org.json.*;
import android.widget.*;
import java.util.*;

class CustomExpandableListAdapter extends BaseExpandableListAdapter
{
    private final Config config;
    private final Context context;
    private Drawable indicatorCollapsed;
    private Drawable indicatorExpanded;
    private final JSONObject info;
    private ChangeListener listener;
    private JSONObject settings;
    
    public CustomExpandableListAdapter(final Context context, final String s, final JSONObject info) {
        this.context = context;
        this.config = new Config(s);
        this.info = info;
        this.updateSettings();
        try {
            this.indicatorExpanded = DrawableInflater.getDrawable(context, "arrow_up");
            this.indicatorCollapsed = DrawableInflater.getDrawable(context, "arrow_down");
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private void applyValue(final String s, final Object o) {
        try {
            final int index = s.indexOf(".");
            if (index == -1) {
                this.settings.put(s, o);
            }
            else {
                this.settings.getJSONObject(s.substring(0, index)).put(s.substring(index + 1), o);
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        this.config.set(s, o);
        this.config.save();
        if (this.listener != null) {
            this.listener.onChange(s, o);
        }
        if (o instanceof Boolean) {
            this.updateSettings();
            this.notifyDataSetChanged();
        }
    }
    
    private View getChildView(final Pair<String, Object> pair, View view, final boolean b) {
        if (view == null) {
            try {
                view = JsonInflater.inflateLayout(this.context, null, ResourceReader.readLayout(this.context, "settings_child")).getView();
            }
            catch (JSONException | JsonInflaterException ex) {
                final Object o;
                throw new RuntimeException((Throwable)o);
            }
        }
        final TextView textView = (TextView)view.findViewWithTag((Object)"description");
        final String localizedInfoParam = this.getLocalizedInfoParam((String)pair.first, "description");
        if (localizedInfoParam == null) {
            textView.setVisibility(8);
        }
        else {
            final StringBuilder sb = new StringBuilder();
            sb.append("\n");
            sb.append(localizedInfoParam);
            textView.setText((CharSequence)sb.toString());
            textView.setVisibility(0);
        }
        final TextView textView2 = (TextView)view.findViewWithTag((Object)"name");
        final String localizedInfoParam2 = this.getLocalizedInfoParam((String)pair.first, "name");
        if (localizedInfoParam2 == null) {
            textView2.setText((CharSequence)this.getReadableName((String)pair.first));
        }
        else {
            textView2.setText((CharSequence)localizedInfoParam2);
        }
        final FrameLayout frameLayout = (FrameLayout)view.findViewWithTag((Object)"editor");
        frameLayout.removeAllViews();
        final FrameLayout frameLayout2 = (FrameLayout)view.findViewWithTag((Object)"bottom");
        frameLayout2.removeAllViews();
        final String infoParam = this.getInfoParam((String)pair.first, "type");
        if (pair.second instanceof Boolean) {
            final Switch switch1 = new Switch(this.context);
            switch1.setChecked((boolean)pair.second);
            frameLayout.addView((View)switch1);
            switch1.setOnCheckedChangeListener((CompoundButton$OnCheckedChangeListener)new CompoundButton$OnCheckedChangeListener() {
                public void onCheckedChanged(final CompoundButton compoundButton, final boolean b) {
                    CustomExpandableListAdapter.this.applyValue((String)pair.first, b);
                }
            });
            return view;
        }
        if (infoParam != null && infoParam.equals("SeekBar")) {
            final SeekBar seekBar = new SeekBar(this.context);
            final String infoParam2 = this.getInfoParam((String)pair.first, "min");
            final String infoParam3 = this.getInfoParam((String)pair.first, "max");
            int int1;
            int int2;
            try {
                int1 = Integer.parseInt(infoParam2);
                int2 = Integer.parseInt(infoParam3);
            }
            catch (Exception ex2) {
                int1 = 0;
                int2 = 100;
            }
            seekBar.setMax(int2 - int1);
            seekBar.setProgress(0);
            seekBar.setProgress((int)pair.second);
            final TextView textView3 = new TextView(this.context);
            textView3.setTextColor(-16777216);
            textView3.setTextSize(18.0f);
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("");
            sb2.append(pair.second);
            textView3.setText((CharSequence)sb2.toString());
            frameLayout.addView((View)textView3);
            frameLayout2.addView((View)seekBar);
            seekBar.setLayoutParams((ViewGroup$LayoutParams)new FrameLayout$LayoutParams(-1, -2));
            seekBar.setOnSeekBarChangeListener((SeekBar$OnSeekBarChangeListener)new SeekBar$OnSeekBarChangeListener() {
                public void onProgressChanged(final SeekBar seekBar, final int n, final boolean b) {
                    final TextView val$tvValue = textView3;
                    final StringBuilder sb = new StringBuilder();
                    sb.append("");
                    sb.append(int1 + n);
                    val$tvValue.setText((CharSequence)sb.toString());
                    CustomExpandableListAdapter.this.applyValue((String)pair.first, int1 + n);
                }
                
                public void onStartTrackingTouch(final SeekBar seekBar) {
                }
                
                public void onStopTrackingTouch(final SeekBar seekBar) {
                }
            });
            return view;
        }
        final EditText editText = new EditText(this.context);
        editText.setTextColor(-16777216);
        editText.setImeOptions(6);
        if (pair.second instanceof Number) {
            double doubleValue;
            if (pair.second instanceof Double) {
                doubleValue = (double)pair.second;
            }
            else {
                doubleValue = (int)pair.second;
            }
            editText.setInputType(8194);
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("");
            sb3.append(doubleValue);
            editText.setText((CharSequence)sb3.toString());
            editText.addTextChangedListener((TextWatcher)new SimpleTextWatcher() {
                public void onTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
                    final Double value = 0.0;
                    Double value2;
                    try {
                        value2 = Double.valueOf(charSequence.toString());
                    }
                    catch (Exception ex) {
                        value2 = value;
                    }
                    CustomExpandableListAdapter.this.applyValue((String)pair.first, value2);
                }
            });
        }
        else if (pair.second instanceof String) {
            editText.setInputType(1);
            final StringBuilder sb4 = new StringBuilder();
            sb4.append("");
            sb4.append(pair.second);
            editText.setText((CharSequence)sb4.toString());
            editText.addTextChangedListener((TextWatcher)new SimpleTextWatcher() {
                public void onTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
                    CustomExpandableListAdapter.this.applyValue((String)pair.first, charSequence.toString());
                }
            });
        }
        frameLayout.addView((View)editText);
        return view;
    }
    
    private int getIndex(final String s) {
        final JSONObject optJSONObject = this.info.optJSONObject(s);
        if (optJSONObject == null) {
            return Integer.MAX_VALUE;
        }
        return optJSONObject.optInt("index", Integer.MAX_VALUE);
    }
    
    private String getInfoParam(final String s, final String s2) {
        final JSONObject optJSONObject = this.info.optJSONObject(s);
        if (optJSONObject == null) {
            return null;
        }
        return optJSONObject.optString(s2, (String)null);
    }
    
    private String getReadableName(String s) {
        final String[] split = s.split("\\.");
        final String s2 = s = split[split.length - 1].replace((char)95, (char)32);
        if (s2.length() > 1) {
            final StringBuilder sb = new StringBuilder();
            sb.append(Character.toUpperCase(s2.charAt(0)));
            sb.append(s2.substring(1));
            s = sb.toString();
        }
        return s;
    }
    
    private Object getValue(final String s) {
        return this.config.get(s);
    }
    
    private boolean shouldDisplay(String infoParam) {
        final String infoParam2 = this.getInfoParam(infoParam, "display");
        infoParam = this.getInfoParam(infoParam, "displayIf");
        if (!(boolean)(infoParam2 == null || Boolean.parseBoolean(infoParam2))) {
            return false;
        }
        if (infoParam != null) {
            final Object value = this.getValue(infoParam.replace("!", ""));
            return (value instanceof Boolean && (boolean)value) != infoParam.startsWith("!");
        }
        return true;
    }
    
    private void updateSettings() {
        this.settings = new JSONObject();
        this.updateSettingsRecursively(this.config, this.settings, 0, "");
    }
    
    private void updateSettingsRecursively(Config iterator, final JSONObject jsonObject, int i, final String s) {
        final ArrayList names = iterator.getNames();
        final ArrayList<Pair> list = new ArrayList<Pair>();
        final HashMap<Object, JSONObject> hashMap = new HashMap<Object, JSONObject>();
        for (final String s2 : names) {
            final StringBuilder sb = new StringBuilder();
            sb.append(s);
            sb.append(s2);
            if (this.shouldDisplay(sb.toString())) {
                final Object value = iterator.get(s2);
                if (value instanceof Config) {
                    final JSONObject jsonObject2 = new JSONObject();
                    list.add(new Pair((Object)s2, (Object)jsonObject2));
                    if (i != 0) {
                        hashMap.put(s2, jsonObject2);
                    }
                    final Config config = (Config)value;
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append(s);
                    sb2.append(s2);
                    sb2.append(".");
                    this.updateSettingsRecursively(config, jsonObject2, i + 1, sb2.toString());
                }
                else {
                    list.add(new Pair((Object)s2, value));
                }
            }
        }
        Collections.sort((List<Object>)list, (Comparator<? super Object>)new Comparator<Pair<String, Object>>() {
            @Override
            public int compare(final Pair<String, Object> pair, final Pair<String, Object> pair2) {
                final CustomExpandableListAdapter this$0 = CustomExpandableListAdapter.this;
                final StringBuilder sb = new StringBuilder();
                sb.append(s);
                sb.append((String)pair.first);
                final int access$100 = this$0.getIndex(sb.toString());
                final CustomExpandableListAdapter this$2 = CustomExpandableListAdapter.this;
                final StringBuilder sb2 = new StringBuilder();
                sb2.append(s);
                sb2.append((String)pair2.first);
                return access$100 - this$2.getIndex(sb2.toString());
            }
        });
        iterator = (Config)list.iterator();
        while (((Iterator)iterator).hasNext()) {
            final Pair pair = ((Iterator<Pair>)iterator).next();
            try {
                jsonObject.put((String)pair.first, pair.second);
                final JSONObject jsonObject3 = hashMap.get(pair.first);
                if (jsonObject3 == null) {
                    continue;
                }
                JSONArray names2;
                String string;
                StringBuilder sb3;
                for (names2 = jsonObject3.names(), i = 0; i < names2.length(); ++i) {
                    string = names2.getString(i);
                    sb3 = new StringBuilder();
                    sb3.append((String)pair.first);
                    sb3.append(".");
                    sb3.append(string);
                    jsonObject.put(sb3.toString(), jsonObject3.get(string));
                }
            }
            catch (JSONException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    public Object getChild(final int n, final int n2) {
        final Pair pair = (Pair)this.getGroup(n);
        if (pair.second instanceof JSONObject) {
            final JSONObject jsonObject = (JSONObject)pair.second;
            final String optString = jsonObject.names().optString(n2);
            final StringBuilder sb = new StringBuilder();
            sb.append((String)pair.first);
            sb.append(".");
            sb.append(optString);
            return new Pair((Object)sb.toString(), jsonObject.opt(optString));
        }
        return null;
    }
    
    public long getChildId(final int n, final int n2) {
        return n2;
    }
    
    public View getChildView(final int n, final int n2, final boolean b, final View view, final ViewGroup viewGroup) {
        return this.getChildView((Pair<String, Object>)this.getChild(n, n2), view, false);
    }
    
    public int getChildrenCount(final int n) {
        final Pair pair = (Pair)this.getGroup(n);
        if (pair.second instanceof JSONObject) {
            return ((JSONObject)pair.second).names().length();
        }
        return 0;
    }
    
    public Object getGroup(final int n) {
        final String optString = this.settings.names().optString(n);
        return new Pair((Object)optString, this.settings.opt(optString));
    }
    
    public int getGroupCount() {
        final JSONArray names = this.settings.names();
        if (names == null) {
            return 0;
        }
        return names.length();
    }
    
    public long getGroupId(final int n) {
        return n;
    }
    
    public View getGroupView(final int n, final boolean b, final View view, final ViewGroup viewGroup) {
        final Pair pair = (Pair)this.getGroup(n);
        if (pair.second instanceof JSONObject) {
            try {
                final View view2 = JsonInflater.inflateLayout(this.context, null, ResourceReader.readLayout(this.context, "settings_group")).getView();
                final ImageView imageView = (ImageView)view2.findViewWithTag((Object)"indicator");
                Drawable imageDrawable;
                if (b) {
                    imageDrawable = this.indicatorExpanded;
                }
                else {
                    imageDrawable = this.indicatorCollapsed;
                }
                imageView.setImageDrawable(imageDrawable);
                if (this.isCollapsible(n)) {
                    imageView.setVisibility(0);
                }
                else {
                    imageView.setVisibility(4);
                }
                ((TextView)view2.findViewWithTag((Object)"name")).setText((CharSequence)this.getReadableName((String)pair.first));
                return view2;
            }
            catch (JSONException | JsonInflaterException ex) {
                final Object o;
                throw new RuntimeException((Throwable)o);
            }
        }
        return this.getChildView((Pair<String, Object>)pair, null, true);
    }
    
    public String getLocalizedInfoParam(final String s, String optString) {
        final JSONObject optJSONObject = this.info.optJSONObject(s);
        if (optJSONObject == null) {
            return null;
        }
        final Object opt = optJSONObject.opt(optString);
        if (opt == null) {
            return null;
        }
        if (opt instanceof String) {
            return (String)opt;
        }
        if (!(opt instanceof JSONObject)) {
            return null;
        }
        optString = ((JSONObject)opt).optString(Locale.getDefault().getLanguage(), (String)null);
        if (optString != null) {
            return optString;
        }
        return ((JSONObject)opt).optString("en", (String)null);
    }
    
    public JSONObject getSettings() {
        return this.settings;
    }
    
    public boolean hasStableIds() {
        return false;
    }
    
    public boolean isChildSelectable(final int n, final int n2) {
        return true;
    }
    
    public boolean isCollapsible(final int n) {
        final String infoParam = this.getInfoParam((String)((Pair)this.getGroup(n)).first, "collapsible");
        return infoParam == null || Boolean.parseBoolean(infoParam);
    }
    
    public boolean isExpanded(final int n) {
        return this.isCollapsible(n) ^ true;
    }
    
    public void setChangeListener(final ChangeListener listener) {
        this.listener = listener;
    }
    
    public interface ChangeListener
    {
        void onChange(final String p0, final Object p1);
    }
}
