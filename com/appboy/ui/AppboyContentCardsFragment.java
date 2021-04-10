package com.appboy.ui;

import android.support.v4.app.*;
import android.support.v4.widget.*;
import com.appboy.events.*;
import com.appboy.ui.contentcards.*;
import com.appboy.support.*;
import com.appboy.ui.contentcards.handlers.*;
import android.content.*;
import com.appboy.models.cards.*;
import java.util.*;
import android.support.v7.widget.helper.*;
import com.appboy.ui.contentcards.recycler.*;
import android.support.v7.widget.*;
import android.view.*;
import com.appboy.*;
import android.os.*;
import android.widget.*;

public class AppboyContentCardsFragment extends Fragment implements OnRefreshListener
{
    private static final long AUTO_HIDE_REFRESH_INDICATOR_DELAY_MS = 2500L;
    private static final String KNOWN_CARD_IMPRESSIONS_SAVED_INSTANCE_STATE_KEY = "KNOWN_CARD_IMPRESSIONS_SAVED_INSTANCE_STATE_KEY";
    private static final String LAYOUT_MANAGER_SAVED_INSTANCE_STATE_KEY = "LAYOUT_MANAGER_SAVED_INSTANCE_STATE_KEY";
    private static final int MAX_CONTENT_CARDS_TTL_SECONDS = 60;
    private static final long NETWORK_PROBLEM_WARNING_MS = 5000L;
    private static final String TAG;
    private AppboyCardAdapter mCardAdapter;
    private SwipeRefreshLayout mContentCardsSwipeLayout;
    private IEventSubscriber<ContentCardsUpdatedEvent> mContentCardsUpdatedSubscriber;
    private IContentCardsUpdateHandler mCustomContentCardUpdateHandler;
    private IContentCardsViewBindingHandler mCustomContentCardsViewBindingHandler;
    private IContentCardsUpdateHandler mDefaultContentCardUpdateHandler;
    private IContentCardsViewBindingHandler mDefaultContentCardsViewBindingHandler;
    private AppboyEmptyContentCardsAdapter mEmptyContentCardsAdapter;
    private final Handler mMainThreadLooper;
    private RecyclerView mRecyclerView;
    private Runnable mShowNetworkUnavailableRunnable;
    
    static {
        TAG = AppboyLogger.getAppboyLogTag(AppboyContentCardsFragment.class);
    }
    
    public AppboyContentCardsFragment() {
        this.mMainThreadLooper = new Handler(Looper.getMainLooper());
        this.mDefaultContentCardUpdateHandler = new DefaultContentCardsUpdateHandler();
        this.mDefaultContentCardsViewBindingHandler = new DefaultContentCardsViewBindingHandler();
    }
    
    private void swapRecyclerViewAdapter(final RecyclerView$Adapter adapter) {
        final RecyclerView mRecyclerView = this.mRecyclerView;
        if (mRecyclerView != null && mRecyclerView.getAdapter() != adapter) {
            this.mRecyclerView.setAdapter(adapter);
        }
    }
    
    public IContentCardsUpdateHandler getContentCardUpdateHandler() {
        final IContentCardsUpdateHandler mCustomContentCardUpdateHandler = this.mCustomContentCardUpdateHandler;
        if (mCustomContentCardUpdateHandler != null) {
            return mCustomContentCardUpdateHandler;
        }
        return this.mDefaultContentCardUpdateHandler;
    }
    
    public IContentCardsViewBindingHandler getContentCardsViewBindingHandler() {
        final IContentCardsViewBindingHandler mCustomContentCardsViewBindingHandler = this.mCustomContentCardsViewBindingHandler;
        if (mCustomContentCardsViewBindingHandler != null) {
            return mCustomContentCardsViewBindingHandler;
        }
        return this.mDefaultContentCardsViewBindingHandler;
    }
    
    public void initializeRecyclerView() {
        final LinearLayoutManager layoutManager = new LinearLayoutManager((Context)this.getActivity());
        final AppboyCardAdapter appboyCardAdapter = new AppboyCardAdapter(this.getContext(), layoutManager, new ArrayList<Card>(), this.getContentCardsViewBindingHandler());
        this.mCardAdapter = appboyCardAdapter;
        this.mRecyclerView.setAdapter((RecyclerView$Adapter)appboyCardAdapter);
        this.mRecyclerView.setLayoutManager((RecyclerView$LayoutManager)layoutManager);
        new ItemTouchHelper((ItemTouchHelper$Callback)new SimpleItemTouchHelperCallback(this.mCardAdapter)).attachToRecyclerView(this.mRecyclerView);
        final RecyclerView$ItemAnimator itemAnimator = this.mRecyclerView.getItemAnimator();
        if (itemAnimator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator)itemAnimator).setSupportsChangeAnimations(false);
        }
        this.mRecyclerView.addItemDecoration((RecyclerView$ItemDecoration)new ContentCardsDividerItemDecoration(this.getContext()));
        this.mEmptyContentCardsAdapter = new AppboyEmptyContentCardsAdapter();
    }
    
    @Override
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.mShowNetworkUnavailableRunnable = new NetworkUnavailableRunnable(this.getContext());
    }
    
    @Override
    public View onCreateView(final LayoutInflater layoutInflater, final ViewGroup viewGroup, final Bundle bundle) {
        final View inflate = layoutInflater.inflate(R$layout.com_appboy_content_cards, viewGroup, false);
        this.mRecyclerView = (RecyclerView)inflate.findViewById(R$id.com_appboy_content_cards_recycler);
        this.initializeRecyclerView();
        (this.mContentCardsSwipeLayout = (SwipeRefreshLayout)inflate.findViewById(R$id.appboy_content_cards_swipe_container)).setOnRefreshListener((SwipeRefreshLayout.OnRefreshListener)this);
        this.mContentCardsSwipeLayout.setColorSchemeResources(R$color.com_appboy_content_cards_swipe_refresh_color_1, R$color.com_appboy_content_cards_swipe_refresh_color_2, R$color.com_appboy_content_cards_swipe_refresh_color_3, R$color.com_appboy_content_cards_swipe_refresh_color_4);
        return inflate;
    }
    
    @Override
    public void onPause() {
        super.onPause();
        Appboy.getInstance(this.getContext()).removeSingleSubscription(this.mContentCardsUpdatedSubscriber, ContentCardsUpdatedEvent.class);
        this.mMainThreadLooper.removeCallbacks(this.mShowNetworkUnavailableRunnable);
        this.mCardAdapter.markOnScreenCardsAsRead();
    }
    
    @Override
    public void onRefresh() {
        Appboy.getInstance(this.getContext()).requestContentCardsRefresh(false);
        this.mMainThreadLooper.postDelayed((Runnable)new Runnable() {
            @Override
            public void run() {
                AppboyContentCardsFragment.this.mContentCardsSwipeLayout.setRefreshing(false);
            }
        }, 2500L);
    }
    
    @Override
    public void onResume() {
        super.onResume();
        Appboy.getInstance(this.getContext()).removeSingleSubscription(this.mContentCardsUpdatedSubscriber, ContentCardsUpdatedEvent.class);
        if (this.mContentCardsUpdatedSubscriber == null) {
            this.mContentCardsUpdatedSubscriber = new IEventSubscriber<ContentCardsUpdatedEvent>() {
                @Override
                public void trigger(final ContentCardsUpdatedEvent contentCardsUpdatedEvent) {
                    AppboyContentCardsFragment.this.mMainThreadLooper.post((Runnable)new ContentCardsUpdateRunnable(contentCardsUpdatedEvent));
                }
            };
        }
        Appboy.getInstance(this.getContext()).subscribeToContentCardsUpdates(this.mContentCardsUpdatedSubscriber);
        Appboy.getInstance(this.getContext()).requestContentCardsRefresh(true);
        Appboy.getInstance(this.getContext()).logContentCardsDisplayed();
    }
    
    @Override
    public void onSaveInstanceState(final Bundle bundle) {
        super.onSaveInstanceState(bundle);
        final RecyclerView$LayoutManager layoutManager = this.mRecyclerView.getLayoutManager();
        if (layoutManager != null) {
            bundle.putParcelable("LAYOUT_MANAGER_SAVED_INSTANCE_STATE_KEY", layoutManager.onSaveInstanceState());
        }
        bundle.putStringArrayList("KNOWN_CARD_IMPRESSIONS_SAVED_INSTANCE_STATE_KEY", (ArrayList)this.mCardAdapter.getImpressedCardIds());
    }
    
    @Override
    public void onViewStateRestored(final Bundle bundle) {
        super.onViewStateRestored(bundle);
        if (bundle == null) {
            return;
        }
        this.mMainThreadLooper.post((Runnable)new Runnable() {
            @Override
            public void run() {
                final Parcelable parcelable = bundle.getParcelable("LAYOUT_MANAGER_SAVED_INSTANCE_STATE_KEY");
                final RecyclerView$LayoutManager layoutManager = AppboyContentCardsFragment.this.mRecyclerView.getLayoutManager();
                if (parcelable != null && layoutManager != null) {
                    layoutManager.onRestoreInstanceState(parcelable);
                }
                final ArrayList stringArrayList = bundle.getStringArrayList("KNOWN_CARD_IMPRESSIONS_SAVED_INSTANCE_STATE_KEY");
                if (stringArrayList != null) {
                    AppboyContentCardsFragment.this.mCardAdapter.setImpressedCardIds(stringArrayList);
                }
            }
        });
    }
    
    public void setContentCardUpdateHandler(final IContentCardsUpdateHandler mCustomContentCardUpdateHandler) {
        this.mCustomContentCardUpdateHandler = mCustomContentCardUpdateHandler;
    }
    
    public void setContentCardsViewBindingHandler(final IContentCardsViewBindingHandler mCustomContentCardsViewBindingHandler) {
        this.mCustomContentCardsViewBindingHandler = mCustomContentCardsViewBindingHandler;
    }
    
    private class ContentCardsUpdateRunnable implements Runnable
    {
        private final ContentCardsUpdatedEvent mEvent;
        
        ContentCardsUpdateRunnable(final ContentCardsUpdatedEvent mEvent) {
            this.mEvent = mEvent;
        }
        
        @Override
        public void run() {
            final String access$300 = AppboyContentCardsFragment.TAG;
            final StringBuilder sb = new StringBuilder();
            sb.append("Updating Content Cards views in response to ContentCardsUpdatedEvent: ");
            sb.append(this.mEvent);
            AppboyLogger.v(access$300, sb.toString());
            AppboyContentCardsFragment.this.mCardAdapter.replaceCards(AppboyContentCardsFragment.this.getContentCardUpdateHandler().handleCardUpdate(this.mEvent));
            AppboyContentCardsFragment.this.mMainThreadLooper.removeCallbacks(AppboyContentCardsFragment.this.mShowNetworkUnavailableRunnable);
            if (this.mEvent.isFromOfflineStorage() && (this.mEvent.getLastUpdatedInSecondsFromEpoch() + 60L) * 1000L < System.currentTimeMillis()) {
                AppboyLogger.i(AppboyContentCardsFragment.TAG, "ContentCards received was older than the max time to live of 60 seconds, displaying it for now, but requesting an updated view from the server.");
                Appboy.getInstance(AppboyContentCardsFragment.this.getContext()).requestContentCardsRefresh(false);
                if (this.mEvent.isEmpty()) {
                    AppboyContentCardsFragment.this.mContentCardsSwipeLayout.setRefreshing(true);
                    AppboyLogger.d(AppboyContentCardsFragment.TAG, "Old Content Cards was empty, putting up a network spinner and registering the network error message on a delay of 5000ms.");
                    AppboyContentCardsFragment.this.mMainThreadLooper.postDelayed(AppboyContentCardsFragment.this.mShowNetworkUnavailableRunnable, 5000L);
                    return;
                }
            }
            AppboyContentCardsFragment appboyContentCardsFragment;
            Object o;
            if (!this.mEvent.isEmpty()) {
                appboyContentCardsFragment = AppboyContentCardsFragment.this;
                o = appboyContentCardsFragment.mCardAdapter;
            }
            else {
                appboyContentCardsFragment = AppboyContentCardsFragment.this;
                o = appboyContentCardsFragment.mEmptyContentCardsAdapter;
            }
            appboyContentCardsFragment.swapRecyclerViewAdapter((RecyclerView$Adapter)o);
            AppboyContentCardsFragment.this.mContentCardsSwipeLayout.setRefreshing(false);
        }
    }
    
    private class NetworkUnavailableRunnable implements Runnable
    {
        private final Context mApplicationContext;
        
        private NetworkUnavailableRunnable(final Context mApplicationContext) {
            this.mApplicationContext = mApplicationContext;
        }
        
        @Override
        public void run() {
            AppboyLogger.v(AppboyContentCardsFragment.TAG, "Displaying network unavailable toast.");
            final Context mApplicationContext = this.mApplicationContext;
            Toast.makeText(mApplicationContext, (CharSequence)mApplicationContext.getString(R$string.com_appboy_feed_connection_error_title), 1).show();
            final AppboyContentCardsFragment this$0 = AppboyContentCardsFragment.this;
            this$0.swapRecyclerViewAdapter(this$0.mEmptyContentCardsAdapter);
            AppboyContentCardsFragment.this.mContentCardsSwipeLayout.setRefreshing(false);
        }
    }
}
