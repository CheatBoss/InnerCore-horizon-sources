package org.mineprogramming.horizon.innercore.view.item;

import android.content.*;
import org.mineprogramming.horizon.innercore.model.*;
import java.util.*;
import android.support.v7.widget.*;
import android.view.*;
import org.mineprogramming.horizon.innercore.util.*;
import android.widget.*;
import org.json.*;
import org.mineprogramming.horizon.innercore.inflater.*;
import org.mineprogramming.horizon.innercore.inflater.layout.*;

public class TagsAdapter extends RecyclerView$Adapter<TagViewHolder>
{
    private final Context context;
    private final SearchHandler searchHandler;
    private final List<Tag> tags;
    
    public TagsAdapter(final Context context, final SearchHandler searchHandler) {
        this.tags = new ArrayList<Tag>();
        this.context = context;
        this.searchHandler = searchHandler;
    }
    
    public void add(final Tag tag) {
        this.tags.add(tag);
    }
    
    public int getItemCount() {
        return this.tags.size();
    }
    
    public void onBindViewHolder(final TagViewHolder tagViewHolder, final int n) {
        tagViewHolder.setText(this.tags.get(n).tag);
        tagViewHolder.itemView.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                TagsAdapter.this.searchHandler.searchTag(TagsAdapter.this.tags.get(n).tag);
            }
        });
    }
    
    public TagViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int n) {
        try {
            final InflatedView inflateLayout = JsonInflater.inflateLayout(this.context, null, ResourceReader.readLayout(this.context, "tag"));
            return new TagViewHolder(inflateLayout.getView(), (TextView)inflateLayout.getViewByJsonId("tag"));
        }
        catch (JSONException | JsonInflaterException ex) {
            final Object o;
            throw new RuntimeException((Throwable)o);
        }
    }
    
    public class TagViewHolder extends RecyclerView$ViewHolder
    {
        private TextView textView;
        
        public TagViewHolder(final View view, final TextView textView) {
            super(view);
            this.textView = textView;
        }
        
        public void setText(final String text) {
            this.textView.setText((CharSequence)text);
        }
    }
}
