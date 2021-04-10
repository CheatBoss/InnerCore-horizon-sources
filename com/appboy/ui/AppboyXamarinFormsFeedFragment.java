package com.appboy.ui;

import android.support.v4.widget.*;
import com.appboy.ui.adapters.*;
import com.appboy.*;
import com.appboy.enums.*;
import com.appboy.events.*;
import android.support.v4.view.*;
import com.appboy.support.*;
import android.os.*;
import android.content.*;
import android.app.*;
import android.widget.*;
import com.appboy.models.cards.*;
import java.util.*;
import android.view.*;

public class AppboyXamarinFormsFeedFragment extends ListFragment implements OnRefreshListener
{
    private static final long AUTO_HIDE_REFRESH_INDICATOR_DELAY_MS = 2500L;
    private static final int MAX_FEED_TTL_SECONDS = 60;
    private static final int NETWORK_PROBLEM_WARNING_MS = 5000;
    private static final String TAG;
    private int currentCardIndexAtBottomOfScreen;
    private AppboyListAdapter mAdapter;
    private Appboy mAppboy;
    private EnumSet<CardCategory> mCategories;
    private LinearLayout mEmptyFeedLayout;
    private RelativeLayout mFeedRootLayout;
    private SwipeRefreshLayout mFeedSwipeLayout;
    private IEventSubscriber<FeedUpdatedEvent> mFeedUpdatedSubscriber;
    private GestureDetectorCompat mGestureDetector;
    private ProgressBar mLoadingSpinner;
    private final Handler mMainThreadLooper;
    private LinearLayout mNetworkErrorLayout;
    private final Runnable mShowNetworkError;
    private boolean mSkipCardImpressionsReset;
    private View mTransparentFullBoundsContainerView;
    private int previousVisibleHeadCardIndex;
    
    static {
        TAG = AppboyLogger.getAppboyLogTag(AppboyXamarinFormsFeedFragment.class);
    }
    
    public AppboyXamarinFormsFeedFragment() {
        this.mMainThreadLooper = new Handler(Looper.getMainLooper());
        this.mShowNetworkError = new Runnable() {
            @Override
            public void run() {
                if (AppboyXamarinFormsFeedFragment.this.mLoadingSpinner != null) {
                    AppboyXamarinFormsFeedFragment.this.mLoadingSpinner.setVisibility(8);
                }
                if (AppboyXamarinFormsFeedFragment.this.mNetworkErrorLayout != null) {
                    AppboyXamarinFormsFeedFragment.this.mNetworkErrorLayout.setVisibility(0);
                }
            }
        };
    }
    
    private void setOnScreenCardsToRead() {
        this.mAdapter.batchSetCardsToRead(this.previousVisibleHeadCardIndex, this.currentCardIndexAtBottomOfScreen);
    }
    
    public EnumSet<CardCategory> getCategories() {
        return this.mCategories;
    }
    
    public void onActivityCreated(final Bundle bundle) {
        super.onActivityCreated(bundle);
        if (this.mSkipCardImpressionsReset) {
            this.mSkipCardImpressionsReset = false;
        }
        else {
            this.mAdapter.resetCardImpressionTracker();
            AppboyLogger.d(AppboyXamarinFormsFeedFragment.TAG, "Resetting card impressions.");
        }
        final LayoutInflater from = LayoutInflater.from((Context)this.getActivity());
        final ListView listView = this.getListView();
        listView.addHeaderView(from.inflate(R$layout.com_appboy_feed_header, (ViewGroup)null));
        listView.addFooterView(from.inflate(R$layout.com_appboy_feed_footer, (ViewGroup)null));
        this.mFeedRootLayout.setOnTouchListener((View$OnTouchListener)new View$OnTouchListener() {
            public boolean onTouch(final View view, final MotionEvent motionEvent) {
                return AppboyXamarinFormsFeedFragment.this.mGestureDetector.onTouchEvent(motionEvent);
            }
        });
        listView.setOnScrollListener((AbsListView$OnScrollListener)new AbsListView$OnScrollListener() {
            public void onScroll(final AbsListView absListView, final int n, final int n2, int n3) {
                AppboyXamarinFormsFeedFragment.this.mFeedSwipeLayout.setEnabled(n == 0);
                if (n2 == 0) {
                    return;
                }
                n3 = n - 1;
                if (n3 > AppboyXamarinFormsFeedFragment.this.previousVisibleHeadCardIndex) {
                    AppboyXamarinFormsFeedFragment.this.mAdapter.batchSetCardsToRead(AppboyXamarinFormsFeedFragment.this.previousVisibleHeadCardIndex, n3);
                }
                AppboyXamarinFormsFeedFragment.this.previousVisibleHeadCardIndex = n3;
                AppboyXamarinFormsFeedFragment.this.currentCardIndexAtBottomOfScreen = n + n2;
            }
            
            public void onScrollStateChanged(final AbsListView absListView, final int n) {
            }
        });
        this.mTransparentFullBoundsContainerView.setOnTouchListener((View$OnTouchListener)new View$OnTouchListener() {
            public boolean onTouch(final View view, final MotionEvent motionEvent) {
                return view.getVisibility() == 0;
            }
        });
        this.mAppboy.removeSingleSubscription(this.mFeedUpdatedSubscriber, FeedUpdatedEvent.class);
        final IEventSubscriber<FeedUpdatedEvent> mFeedUpdatedSubscriber = new IEventSubscriber<FeedUpdatedEvent>() {
            @Override
            public void trigger(final FeedUpdatedEvent feedUpdatedEvent) {
                final Activity activity = AppboyXamarinFormsFeedFragment.this.getActivity();
                if (activity == null) {
                    return;
                }
                activity.runOnUiThread((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        final String access$700 = AppboyXamarinFormsFeedFragment.TAG;
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Updating feed views in response to FeedUpdatedEvent: ");
                        sb.append(feedUpdatedEvent);
                        AppboyLogger.d(access$700, sb.toString());
                        AppboyXamarinFormsFeedFragment.this.mMainThreadLooper.removeCallbacks(AppboyXamarinFormsFeedFragment.this.mShowNetworkError);
                        AppboyXamarinFormsFeedFragment.this.mNetworkErrorLayout.setVisibility(8);
                        if (feedUpdatedEvent.getCardCount(AppboyXamarinFormsFeedFragment.this.mCategories) == 0) {
                            listView.setVisibility(8);
                            AppboyXamarinFormsFeedFragment.this.mAdapter.clear();
                        }
                        else {
                            AppboyXamarinFormsFeedFragment.this.mEmptyFeedLayout.setVisibility(8);
                            AppboyXamarinFormsFeedFragment.this.mLoadingSpinner.setVisibility(8);
                            AppboyXamarinFormsFeedFragment.this.mTransparentFullBoundsContainerView.setVisibility(8);
                        }
                        if (feedUpdatedEvent.isFromOfflineStorage() && (feedUpdatedEvent.lastUpdatedInSecondsFromEpoch() + 60L) * 1000L < System.currentTimeMillis()) {
                            AppboyLogger.i(AppboyXamarinFormsFeedFragment.TAG, "Feed received was older than the max time to live of 60 seconds, displaying it for now, but requesting an updated view from the server.");
                            AppboyXamarinFormsFeedFragment.this.mAppboy.requestFeedRefresh();
                            if (feedUpdatedEvent.getCardCount(AppboyXamarinFormsFeedFragment.this.mCategories) == 0) {
                                AppboyLogger.d(AppboyXamarinFormsFeedFragment.TAG, "Old feed was empty, putting up a network spinner and registering the network error message on a delay of 5000ms.");
                                AppboyXamarinFormsFeedFragment.this.mEmptyFeedLayout.setVisibility(8);
                                AppboyXamarinFormsFeedFragment.this.mLoadingSpinner.setVisibility(0);
                                AppboyXamarinFormsFeedFragment.this.mTransparentFullBoundsContainerView.setVisibility(0);
                                AppboyXamarinFormsFeedFragment.this.mMainThreadLooper.postDelayed(AppboyXamarinFormsFeedFragment.this.mShowNetworkError, 5000L);
                                return;
                            }
                        }
                        if (feedUpdatedEvent.getCardCount(AppboyXamarinFormsFeedFragment.this.mCategories) == 0) {
                            AppboyXamarinFormsFeedFragment.this.mLoadingSpinner.setVisibility(8);
                            AppboyXamarinFormsFeedFragment.this.mEmptyFeedLayout.setVisibility(0);
                            AppboyXamarinFormsFeedFragment.this.mTransparentFullBoundsContainerView.setVisibility(0);
                        }
                        else {
                            AppboyXamarinFormsFeedFragment.this.mAdapter.replaceFeed(feedUpdatedEvent.getFeedCards(AppboyXamarinFormsFeedFragment.this.mCategories));
                            listView.setVisibility(0);
                        }
                        AppboyXamarinFormsFeedFragment.this.mFeedSwipeLayout.setRefreshing(false);
                    }
                });
            }
        };
        this.mFeedUpdatedSubscriber = mFeedUpdatedSubscriber;
        this.mAppboy.subscribeToFeedUpdates(mFeedUpdatedSubscriber);
        listView.setAdapter((ListAdapter)this.mAdapter);
        this.mAppboy.requestFeedRefreshFromCache();
    }
    
    public void onAttach(final Context context) {
        super.onAttach(context);
        this.mAppboy = Appboy.getInstance(context);
        if (this.mAdapter == null) {
            this.mAdapter = new AppboyListAdapter(context, R$id.tag, new ArrayList<Card>());
            this.mCategories = CardCategory.getAllCategories();
        }
        this.setRetainInstance(true);
        this.mGestureDetector = new GestureDetectorCompat(context, (GestureDetector$OnGestureListener)new FeedGestureListener());
    }
    
    public View onCreateView(final LayoutInflater layoutInflater, final ViewGroup viewGroup, final Bundle bundle) {
        final View inflate = layoutInflater.inflate(R$layout.com_appboy_feed, viewGroup, false);
        this.mNetworkErrorLayout = (LinearLayout)inflate.findViewById(R$id.com_appboy_feed_network_error);
        this.mLoadingSpinner = (ProgressBar)inflate.findViewById(R$id.com_appboy_feed_loading_spinner);
        this.mEmptyFeedLayout = (LinearLayout)inflate.findViewById(R$id.com_appboy_feed_empty_feed);
        this.mFeedRootLayout = (RelativeLayout)inflate.findViewById(R$id.com_appboy_feed_root);
        (this.mFeedSwipeLayout = (SwipeRefreshLayout)inflate.findViewById(R$id.appboy_feed_swipe_container)).setOnRefreshListener((SwipeRefreshLayout.OnRefreshListener)this);
        this.mFeedSwipeLayout.setEnabled(false);
        this.mFeedSwipeLayout.setColorSchemeResources(R$color.com_appboy_newsfeed_swipe_refresh_color_1, R$color.com_appboy_newsfeed_swipe_refresh_color_2, R$color.com_appboy_newsfeed_swipe_refresh_color_3, R$color.com_appboy_newsfeed_swipe_refresh_color_4);
        this.mTransparentFullBoundsContainerView = inflate.findViewById(R$id.com_appboy_feed_transparent_full_bounds_container_view);
        return inflate;
    }
    
    public void onDestroyView() {
        super.onDestroyView();
        this.mAppboy.removeSingleSubscription(this.mFeedUpdatedSubscriber, FeedUpdatedEvent.class);
        this.setOnScreenCardsToRead();
    }
    
    public void onDetach() {
        super.onDetach();
        this.setListAdapter((ListAdapter)null);
    }
    
    public void onPause() {
        super.onPause();
        this.setOnScreenCardsToRead();
    }
    
    public void onRefresh() {
        this.mAppboy.requestFeedRefresh();
        this.mMainThreadLooper.postDelayed((Runnable)new Runnable() {
            @Override
            public void run() {
                AppboyXamarinFormsFeedFragment.this.mFeedSwipeLayout.setRefreshing(false);
            }
        }, 2500L);
    }
    
    public void onResume() {
        super.onResume();
        Appboy.getInstance((Context)this.getActivity()).logFeedDisplayed();
    }
    
    public void onSaveInstanceState(final Bundle bundle) {
        super.onSaveInstanceState(bundle);
        if (this.isVisible()) {
            this.mSkipCardImpressionsReset = true;
        }
    }
    
    public void setCategories(final EnumSet<CardCategory> set) {
        EnumSet<CardCategory> allCategories;
        if (set == null) {
            AppboyLogger.i(AppboyXamarinFormsFeedFragment.TAG, "The categories passed into setCategories are null, AppboyFeedFragment is going to display all the cards in cache.");
            allCategories = CardCategory.getAllCategories();
        }
        else {
            if (set.isEmpty()) {
                AppboyLogger.w(AppboyXamarinFormsFeedFragment.TAG, "The categories set had no elements and have been ignored. Please pass a valid EnumSet of CardCategory.");
                return;
            }
            allCategories = set;
            if (set.equals(this.mCategories)) {
                return;
            }
        }
        this.mCategories = allCategories;
        final Appboy mAppboy = this.mAppboy;
        if (mAppboy != null) {
            mAppboy.requestFeedRefreshFromCache();
        }
    }
    
    public void setCategory(final CardCategory cardCategory) {
        this.setCategories(EnumSet.of(cardCategory));
    }
    
    public class FeedGestureListener extends GestureDetector$SimpleOnGestureListener
    {
        public boolean onDown(final MotionEvent motionEvent) {
            return true;
        }
        
        public boolean onFling(final MotionEvent motionEvent, final MotionEvent motionEvent2, final float n, final float n2) {
            final long n3 = (motionEvent2.getEventTime() - motionEvent.getEventTime()) * 2L;
            AppboyXamarinFormsFeedFragment.this.getListView().smoothScrollBy(-(int)(n2 * n3 / 1000.0f), (int)(n3 * 2L));
            return true;
        }
        
        public boolean onScroll(final MotionEvent motionEvent, final MotionEvent motionEvent2, final float n, final float n2) {
            AppboyXamarinFormsFeedFragment.this.getListView().smoothScrollBy((int)n2, 0);
            return true;
        }
    }
}
