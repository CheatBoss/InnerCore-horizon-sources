package com.appboy.ui.contentcards.view;

import com.appboy.models.cards.*;
import android.content.*;
import android.view.*;
import com.appboy.ui.*;

public class DefaultContentCardView extends BaseContentCardView<Card>
{
    public DefaultContentCardView(final Context context) {
        super(context);
    }
    
    @Override
    public void bindViewHolder(final ContentCardViewHolder contentCardViewHolder, final Card card) {
    }
    
    @Override
    public ContentCardViewHolder createViewHolder(final ViewGroup viewGroup) {
        return new ContentCardViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R$layout.com_appboy_default_content_card, viewGroup, false), false);
    }
}
