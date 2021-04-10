package org.mineprogramming.horizon.innercore.view.item;

import android.support.v4.view.*;
import android.content.*;
import java.util.*;
import org.json.*;
import android.view.*;
import org.mineprogramming.horizon.innercore.util.*;
import android.widget.*;
import com.bumptech.glide.*;
import org.mineprogramming.horizon.innercore.inflater.*;
import org.mineprogramming.horizon.innercore.inflater.layout.*;

public class ScreenshotsAdapter extends PagerAdapter
{
    private final Context context;
    private final ArrayList<String> images;
    
    public ScreenshotsAdapter(final Context context, final String s) throws JSONException {
        this.context = context;
        if (s.startsWith("{")) {
            final JSONObject jsonObject = new JSONObject(s);
            this.images = new ArrayList<String>(jsonObject.names().length());
            final Iterator keys = jsonObject.keys();
            while (keys.hasNext()) {
                this.images.add(jsonObject.optString((String)keys.next()));
            }
            return;
        }
        final JSONArray jsonArray = new JSONArray(s);
        this.images = new ArrayList<String>(jsonArray.length());
        for (int i = 0; i < jsonArray.length(); ++i) {
            this.images.add(jsonArray.optString(i));
        }
    }
    
    public void destroyItem(final ViewGroup viewGroup, final int n, final Object o) {
        viewGroup.removeView((View)o);
    }
    
    public int getCount() {
        return this.images.size();
    }
    
    public Object instantiateItem(final ViewGroup viewGroup, final int n) {
        try {
            final InflatedView inflateLayout = JsonInflater.inflateLayout(this.context, null, ResourceReader.readLayout(this.context, "screenshot"));
            final ImageView imageView = inflateLayout.getViewByJsonId("image");
            final StringBuilder sb = new StringBuilder();
            sb.append("https://icmods.mineprogramming.org/api/img/");
            sb.append(this.images.get(n));
            Glide.with(this.context).load(sb.toString()).into(imageView);
            viewGroup.addView(inflateLayout.getView());
            return inflateLayout.getView();
        }
        catch (JSONException | JsonInflaterException ex) {
            final Object o;
            throw new RuntimeException((Throwable)o);
        }
    }
    
    public boolean isViewFromObject(final View view, final Object o) {
        return view == o;
    }
}
