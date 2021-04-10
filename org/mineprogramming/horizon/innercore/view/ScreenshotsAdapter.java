package org.mineprogramming.horizon.innercore.view;

import android.support.v4.view.*;
import android.content.*;
import java.util.*;
import android.view.*;
import org.mineprogramming.horizon.innercore.util.*;
import android.widget.*;
import com.bumptech.glide.*;
import org.json.*;
import org.mineprogramming.horizon.innercore.inflater.*;
import org.mineprogramming.horizon.innercore.inflater.layout.*;

public class ScreenshotsAdapter extends PagerAdapter
{
    private final Context context;
    private final ArrayList<String> images;
    
    public ScreenshotsAdapter(final Context context, final ArrayList<String> images) {
        this.context = context;
        this.images = images;
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
