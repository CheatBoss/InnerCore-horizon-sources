package com.appboy.ui.widget;

import android.widget.*;
import com.appboy.support.*;
import android.content.*;
import android.util.*;
import java.util.*;
import android.view.*;
import com.appboy.ui.*;

public class StarRatingView extends LinearLayout
{
    private static final int MAX_NUMBER_OF_STARS = 5;
    private static final String TAG;
    private float mRating;
    private List<ImageView> mStarRating;
    
    static {
        TAG = AppboyLogger.getAppboyLogTag(StarRatingView.class);
    }
    
    public StarRatingView(final Context context, final AttributeSet set) {
        super(context, set);
        this.mRating = 0.0f;
        this.setOrientation(0);
        this.mStarRating = new ArrayList<ImageView>(5);
        for (int i = 0; i < 5; ++i) {
            final ImageView imageView = new ImageView(context);
            imageView.setTag((Object)0);
            this.addView((View)imageView, new ViewGroup$LayoutParams(-2, -2));
            this.mStarRating.add(imageView);
        }
        this.setRating(this.mRating);
    }
    
    List<ImageView> getImageViewList() {
        return this.mStarRating;
    }
    
    public float getRating() {
        return this.mRating;
    }
    
    public boolean setRating(float mRating) {
        int n = 0;
        if (mRating >= 0.0f && mRating <= 5.0f) {
            this.mRating = mRating;
            final int n2 = (int)Math.floor(mRating);
            final int n3 = (int)Math.ceil(this.mRating);
            int i;
            while (true) {
                i = n3;
                if (n >= n2) {
                    break;
                }
                final ImageView imageView = this.mStarRating.get(n);
                imageView.setTag((Object)R$drawable.com_appboy_rating_full_star);
                imageView.setImageResource(R$drawable.com_appboy_rating_full_star);
                ++n;
            }
            while (i < this.mStarRating.size()) {
                final ImageView imageView2 = this.mStarRating.get(i);
                imageView2.setTag((Object)R$drawable.com_appboy_rating_empty_star);
                imageView2.setImageResource(R$drawable.com_appboy_rating_empty_star);
                ++i;
            }
            mRating -= n2;
            if (mRating > 0.0f) {
                final ImageView imageView3 = this.mStarRating.get(n2);
                int imageResource;
                if (mRating < 0.25f) {
                    imageView3.setTag((Object)R$drawable.com_appboy_rating_empty_star);
                    imageResource = R$drawable.com_appboy_rating_empty_star;
                }
                else if (mRating < 0.75f) {
                    imageView3.setTag((Object)R$drawable.com_appboy_rating_half_star);
                    imageResource = R$drawable.com_appboy_rating_half_star;
                }
                else {
                    imageView3.setTag((Object)R$drawable.com_appboy_rating_full_star);
                    imageResource = R$drawable.com_appboy_rating_full_star;
                }
                imageView3.setImageResource(imageResource);
            }
            return true;
        }
        final String tag = StarRatingView.TAG;
        final StringBuilder sb = new StringBuilder();
        sb.append("Unable to set rating to ");
        sb.append(mRating);
        sb.append(". Rating must be between 0 and ");
        sb.append(5);
        AppboyLogger.e(tag, sb.toString());
        return false;
    }
}
