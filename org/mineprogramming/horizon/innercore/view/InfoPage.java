package org.mineprogramming.horizon.innercore.view;

import org.mineprogramming.horizon.innercore.view.page.*;
import android.view.*;
import org.mineprogramming.horizon.innercore.util.*;
import android.widget.*;
import android.text.method.*;
import org.json.*;
import org.mineprogramming.horizon.innercore.inflater.*;
import org.mineprogramming.horizon.innercore.inflater.layout.*;

public class InfoPage extends Page
{
    private final String content;
    private final String header;
    
    protected InfoPage(final PagesManager pagesManager, final String header, final String content) {
        super(pagesManager);
        this.header = header;
        this.content = content;
    }
    
    @Override
    public void display(final ViewGroup viewGroup) {
        try {
            final InflatedView inflateLayout = JsonInflater.inflateLayout(this.context, viewGroup, ResourceReader.readLayout(this.context, "info"));
            ((TextView)inflateLayout.getViewByJsonId("header")).setText((CharSequence)this.header);
            final TextView textView = inflateLayout.getViewByJsonId("content");
            textView.setText((CharSequence)InflaterUtils.fromHtml(this.content));
            textView.setMovementMethod(LinkMovementMethod.getInstance());
        }
        catch (JSONException | JsonInflaterException ex) {
            final Object o;
            throw new RuntimeException((Throwable)o);
        }
    }
}
