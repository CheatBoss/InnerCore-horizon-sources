package org.mineprogramming.horizon.innercore.view.modpack;

import org.mineprogramming.horizon.innercore.view.item.*;
import android.view.*;
import android.widget.*;
import android.content.*;
import org.mineprogramming.horizon.innercore.inflater.layout.*;
import android.graphics.*;
import org.mineprogramming.horizon.innercore.model.*;
import org.mineprogramming.horizon.innercore.util.*;
import org.json.*;
import org.mineprogramming.horizon.innercore.inflater.*;
import java.util.*;

public class ModPackViewHolder extends ItemViewHolder
{
    private final ViewGroup card;
    private final TextView modsCountTag;
    private final View selectedTag;
    private final LinearLayout tagsLayout;
    
    public ModPackViewHolder(final Context context, final InflatedView inflatedView) {
        super(context, inflatedView);
        this.card = inflatedView.getViewByJsonId("card");
        this.selectedTag = inflatedView.getViewByJsonId("selected");
        this.modsCountTag = inflatedView.getViewByJsonId("mod_count");
        this.tagsLayout = inflatedView.getViewByJsonId("tags");
    }
    
    public void setModsCount(final int n) {
        if (n > 0) {
            this.modsCountTag.setVisibility(0);
            this.modsCountTag.setText((CharSequence)String.format(JsonInflater.getString(this.context, "mods_count"), n));
            return;
        }
        this.modsCountTag.setVisibility(8);
    }
    
    public void setSelected(final boolean b) {
        final View selectedTag = this.selectedTag;
        int visibility;
        if (b) {
            visibility = 0;
        }
        else {
            visibility = 8;
        }
        selectedTag.setVisibility(visibility);
        final ViewGroup card = this.card;
        int rgb;
        if (b) {
            rgb = Color.rgb(204, 221, 255);
        }
        else {
            rgb = -1;
        }
        card.setBackgroundColor(rgb);
    }
    
    public void setTags(final List<Tag> list) {
        this.tagsLayout.removeViews(2, this.tagsLayout.getChildCount() - 2);
        for (final Tag tag : list) {
            try {
                final InflatedView inflateLayout = JsonInflater.inflateLayout(this.context, null, ResourceReader.readLayout(this.context, "tag"));
                ((TextView)inflateLayout.getViewByJsonId("tag")).setText((CharSequence)tag.toString());
                this.tagsLayout.addView(inflateLayout.getView());
                continue;
            }
            catch (JSONException | JsonInflaterException ex) {
                final Object o;
                throw new RuntimeException((Throwable)o);
            }
            break;
        }
    }
}
