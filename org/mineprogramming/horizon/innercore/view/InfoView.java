package org.mineprogramming.horizon.innercore.view;

import org.mineprogramming.horizon.innercore.*;
import android.content.*;
import android.view.*;
import org.mineprogramming.horizon.innercore.util.*;
import android.widget.*;
import android.os.*;
import android.text.*;
import org.json.*;
import org.mineprogramming.horizon.innercore.inflater.*;
import org.mineprogramming.horizon.innercore.inflater.layout.*;

public class InfoView extends Displayable
{
    private final String content;
    private final String header;
    
    protected InfoView(final Context context, final String header, final String content) {
        super(context);
        this.header = header;
        this.content = content;
    }
    
    @Override
    public void display(final ViewGroup viewGroup) {
        try {
            final InflatedView inflateLayout = JsonInflater.inflateLayout(this.context, viewGroup, ResourceReader.readLayout(this.context, "info"));
            ((TextView)inflateLayout.getViewByJsonId("header")).setText((CharSequence)this.header);
            final TextView textView = inflateLayout.getViewByJsonId("content");
            if (Build$VERSION.SDK_INT >= 24) {
                textView.setText((CharSequence)Html.fromHtml(this.content, 63));
                return;
            }
            textView.setText((CharSequence)Html.fromHtml(this.content));
        }
        catch (JSONException | JsonInflaterException ex) {
            final Object o;
            throw new RuntimeException((Throwable)o);
        }
    }
}
