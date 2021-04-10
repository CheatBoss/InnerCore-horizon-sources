package com.appboy.ui;

import android.support.v4.widget.*;
import com.appboy.ui.adapters.*;
import com.appboy.enums.*;
import com.appboy.events.*;
import android.support.v4.view.*;
import com.appboy.support.*;
import android.os.*;
import android.content.*;
import com.appboy.*;
import com.appboy.models.cards.*;
import android.support.v4.app.*;
import android.widget.*;
import java.util.function.*;
import java.util.*;
import android.view.*;

public class AppboyFeedFragment extends ListFragment implements OnRefreshListener
{
    private static final long AUTO_HIDE_REFRESH_INDICATOR_DELAY_MS = 2500L;
    private static final int MAX_FEED_TTL_SECONDS = 60;
    private static final int NETWORK_PROBLEM_WARNING_MS = 5000;
    static final String SAVED_INSTANCE_STATE_KEY_CARD_CATEGORY = "CARD_CATEGORY";
    static final String SAVED_INSTANCE_STATE_KEY_CURRENT_CARD_INDEX_AT_BOTTOM_OF_SCREEN = "CURRENT_CARD_INDEX_AT_BOTTOM_OF_SCREEN";
    static final String SAVED_INSTANCE_STATE_KEY_PREVIOUS_VISIBLE_HEAD_CARD_INDEX = "PREVIOUS_VISIBLE_HEAD_CARD_INDEX";
    static final String SAVED_INSTANCE_STATE_KEY_SKIP_CARD_IMPRESSIONS_RESET = "SKIP_CARD_IMPRESSIONS_RESET";
    private static final String TAG;
    private AppboyListAdapter mAdapter;
    private EnumSet<CardCategory> mCategories;
    int mCurrentCardIndexAtBottomOfScreen;
    private LinearLayout mEmptyFeedLayout;
    private RelativeLayout mFeedRootLayout;
    private SwipeRefreshLayout mFeedSwipeLayout;
    private IEventSubscriber<FeedUpdatedEvent> mFeedUpdatedSubscriber;
    private GestureDetectorCompat mGestureDetector;
    private ProgressBar mLoadingSpinner;
    private final Handler mMainThreadLooper;
    private LinearLayout mNetworkErrorLayout;
    int mPreviousVisibleHeadCardIndex;
    private final Runnable mShowNetworkError;
    boolean mSkipCardImpressionsReset;
    private boolean mSortEnabled;
    private View mTransparentFullBoundsContainerView;
    
    static {
        TAG = AppboyLogger.getAppboyLogTag(AppboyFeedFragment.class);
    }
    
    public AppboyFeedFragment() {
        this.mMainThreadLooper = new Handler(Looper.getMainLooper());
        this.mShowNetworkError = new Runnable() {
            @Override
            public void run() {
                if (AppboyFeedFragment.this.mLoadingSpinner != null) {
                    AppboyFeedFragment.this.mLoadingSpinner.setVisibility(8);
                }
                if (AppboyFeedFragment.this.mNetworkErrorLayout != null) {
                    AppboyFeedFragment.this.mNetworkErrorLayout.setVisibility(0);
                }
            }
        };
        this.mSortEnabled = false;
        this.mSkipCardImpressionsReset = false;
        this.mPreviousVisibleHeadCardIndex = 0;
        this.mCurrentCardIndexAtBottomOfScreen = 0;
    }
    
    private void setOnScreenCardsToRead() {
        this.mAdapter.batchSetCardsToRead(this.mPreviousVisibleHeadCardIndex, this.mCurrentCardIndexAtBottomOfScreen);
    }
    
    public EnumSet<CardCategory> getCategories() {
        return this.mCategories;
    }
    
    public boolean getSortEnabled() {
        return this.mSortEnabled;
    }
    
    void loadFragmentStateFromSavedInstanceState(final Bundle bundle) {
        if (bundle == null) {
            return;
        }
        if (this.mCategories == null) {
            this.mCategories = CardCategory.getAllCategories();
        }
        this.mPreviousVisibleHeadCardIndex = bundle.getInt("PREVIOUS_VISIBLE_HEAD_CARD_INDEX", 0);
        this.mCurrentCardIndexAtBottomOfScreen = bundle.getInt("CURRENT_CARD_INDEX_AT_BOTTOM_OF_SCREEN", 0);
        this.mSkipCardImpressionsReset = bundle.getBoolean("SKIP_CARD_IMPRESSIONS_RESET", false);
        final ArrayList stringArrayList = bundle.getStringArrayList("CARD_CATEGORY");
        if (stringArrayList != null) {
            this.mCategories.clear();
            final Iterator<String> iterator = stringArrayList.iterator();
            while (iterator.hasNext()) {
                this.mCategories.add(CardCategory.valueOf(iterator.next()));
            }
        }
    }
    
    @Override
    public void onActivityCreated(final Bundle bundle) {
        super.onActivityCreated(bundle);
        this.loadFragmentStateFromSavedInstanceState(bundle);
        if (this.mSkipCardImpressionsReset) {
            this.mSkipCardImpressionsReset = false;
        }
        else {
            this.mAdapter.resetCardImpressionTracker();
            AppboyLogger.d(AppboyFeedFragment.TAG, "Resetting card impressions.");
        }
        final LayoutInflater from = LayoutInflater.from((Context)this.getActivity());
        final ListView listView = this.getListView();
        listView.addHeaderView(from.inflate(R$layout.com_appboy_feed_header, (ViewGroup)null));
        listView.addFooterView(from.inflate(R$layout.com_appboy_feed_footer, (ViewGroup)null));
        this.mFeedRootLayout.setOnTouchListener((View$OnTouchListener)new View$OnTouchListener() {
            public boolean onTouch(final View view, final MotionEvent motionEvent) {
                return AppboyFeedFragment.this.mGestureDetector.onTouchEvent(motionEvent);
            }
        });
        listView.setOnScrollListener((AbsListView$OnScrollListener)new AbsListView$OnScrollListener() {
            public void onScroll(final AbsListView absListView, final int n, final int n2, int mPreviousVisibleHeadCardIndex) {
                AppboyFeedFragment.this.mFeedSwipeLayout.setEnabled(n == 0);
                if (n2 == 0) {
                    return;
                }
                mPreviousVisibleHeadCardIndex = n - 1;
                if (mPreviousVisibleHeadCardIndex > AppboyFeedFragment.this.mPreviousVisibleHeadCardIndex) {
                    AppboyFeedFragment.this.mAdapter.batchSetCardsToRead(AppboyFeedFragment.this.mPreviousVisibleHeadCardIndex, mPreviousVisibleHeadCardIndex);
                }
                AppboyFeedFragment.this.mPreviousVisibleHeadCardIndex = mPreviousVisibleHeadCardIndex;
                AppboyFeedFragment.this.mCurrentCardIndexAtBottomOfScreen = n + n2;
            }
            
            public void onScrollStateChanged(final AbsListView absListView, final int n) {
            }
        });
        this.mTransparentFullBoundsContainerView.setOnTouchListener((View$OnTouchListener)new View$OnTouchListener() {
            public boolean onTouch(final View view, final MotionEvent motionEvent) {
                return view.getVisibility() == 0;
            }
        });
        Appboy.getInstance(this.getContext()).removeSingleSubscription(this.mFeedUpdatedSubscriber, FeedUpdatedEvent.class);
        this.mFeedUpdatedSubscriber = new IEventSubscriber<FeedUpdatedEvent>() {
            @Override
            public void trigger(final FeedUpdatedEvent feedUpdatedEvent) {
                final FragmentActivity activity = AppboyFeedFragment.this.getActivity();
                if (activity == null) {
                    return;
                }
                activity.runOnUiThread((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        final String access$500 = AppboyFeedFragment.TAG;
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Updating feed views in response to FeedUpdatedEvent: ");
                        sb.append(feedUpdatedEvent);
                        AppboyLogger.v(access$500, sb.toString());
                        AppboyFeedFragment.this.mMainThreadLooper.removeCallbacks(AppboyFeedFragment.this.mShowNetworkError);
                        AppboyFeedFragment.this.mNetworkErrorLayout.setVisibility(8);
                        if (feedUpdatedEvent.getCardCount(AppboyFeedFragment.this.mCategories) == 0) {
                            listView.setVisibility(8);
                            AppboyFeedFragment.this.mAdapter.clear();
                        }
                        else {
                            AppboyFeedFragment.this.mEmptyFeedLayout.setVisibility(8);
                            AppboyFeedFragment.this.mLoadingSpinner.setVisibility(8);
                            AppboyFeedFragment.this.mTransparentFullBoundsContainerView.setVisibility(8);
                        }
                        if (feedUpdatedEvent.isFromOfflineStorage() && (feedUpdatedEvent.lastUpdatedInSecondsFromEpoch() + 60L) * 1000L < System.currentTimeMillis()) {
                            AppboyLogger.i(AppboyFeedFragment.TAG, "Feed received was older than the max time to live of 60 seconds, displaying it for now, but requesting an updated view from the server.");
                            Appboy.getInstance(AppboyFeedFragment.this.getContext()).requestFeedRefresh();
                            if (feedUpdatedEvent.getCardCount(AppboyFeedFragment.this.mCategories) == 0) {
                                AppboyLogger.d(AppboyFeedFragment.TAG, "Old feed was empty, putting up a network spinner and registering the network error message with a delay of 5000ms.");
                                AppboyFeedFragment.this.mEmptyFeedLayout.setVisibility(8);
                                AppboyFeedFragment.this.mLoadingSpinner.setVisibility(0);
                                AppboyFeedFragment.this.mTransparentFullBoundsContainerView.setVisibility(0);
                                AppboyFeedFragment.this.mMainThreadLooper.postDelayed(AppboyFeedFragment.this.mShowNetworkError, 5000L);
                                return;
                            }
                        }
                        if (feedUpdatedEvent.getCardCount(AppboyFeedFragment.this.mCategories) == 0) {
                            AppboyFeedFragment.this.mLoadingSpinner.setVisibility(8);
                            AppboyFeedFragment.this.mEmptyFeedLayout.setVisibility(0);
                            AppboyFeedFragment.this.mTransparentFullBoundsContainerView.setVisibility(0);
                        }
                        else {
                            AppboyListAdapter appboyListAdapter;
                            List<Card> list;
                            if (AppboyFeedFragment.this.mSortEnabled && feedUpdatedEvent.getCardCount(AppboyFeedFragment.this.mCategories) != feedUpdatedEvent.getUnreadCardCount(AppboyFeedFragment.this.mCategories)) {
                                appboyListAdapter = AppboyFeedFragment.this.mAdapter;
                                list = AppboyFeedFragment.this.sortFeedCards(feedUpdatedEvent.getFeedCards(AppboyFeedFragment.this.mCategories));
                            }
                            else {
                                appboyListAdapter = AppboyFeedFragment.this.mAdapter;
                                list = feedUpdatedEvent.getFeedCards(AppboyFeedFragment.this.mCategories);
                            }
                            appboyListAdapter.replaceFeed(list);
                            listView.setVisibility(0);
                        }
                        AppboyFeedFragment.this.mFeedSwipeLayout.setRefreshing(false);
                    }
                });
            }
        };
        Appboy.getInstance(this.getContext()).subscribeToFeedUpdates(this.mFeedUpdatedSubscriber);
        listView.setAdapter((ListAdapter)this.mAdapter);
        Appboy.getInstance(this.getContext()).requestFeedRefreshFromCache();
    }
    
    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);
        if (this.mAdapter == null) {
            this.mAdapter = new AppboyListAdapter(context, R$id.tag, new ArrayList<Card>());
            this.mCategories = CardCategory.getAllCategories();
        }
        this.mGestureDetector = new GestureDetectorCompat(context, (GestureDetector$OnGestureListener)new FeedGestureListener());
    }
    
    @Override
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
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Appboy.getInstance(this.getContext()).removeSingleSubscription(this.mFeedUpdatedSubscriber, FeedUpdatedEvent.class);
        this.setOnScreenCardsToRead();
    }
    
    @Override
    public void onDetach() {
        super.onDetach();
        this.setListAdapter(null);
    }
    
    @Override
    public void onPause() {
        super.onPause();
        this.setOnScreenCardsToRead();
    }
    
    @Override
    public void onRefresh() {
        Appboy.getInstance(this.getContext()).requestFeedRefresh();
        this.mMainThreadLooper.postDelayed((Runnable)new Runnable() {
            @Override
            public void run() {
                AppboyFeedFragment.this.mFeedSwipeLayout.setRefreshing(false);
            }
        }, 2500L);
    }
    
    @Override
    public void onResume() {
        super.onResume();
        Appboy.getInstance(this.getContext()).logFeedDisplayed();
    }
    
    @Override
    public void onSaveInstanceState(final Bundle bundle) {
        bundle.putInt("PREVIOUS_VISIBLE_HEAD_CARD_INDEX", this.mPreviousVisibleHeadCardIndex);
        bundle.putInt("CURRENT_CARD_INDEX_AT_BOTTOM_OF_SCREEN", this.mCurrentCardIndexAtBottomOfScreen);
        bundle.putBoolean("SKIP_CARD_IMPRESSIONS_RESET", this.mSkipCardImpressionsReset);
        if (this.mCategories == null) {
            this.mCategories = CardCategory.getAllCategories();
        }
        final ArrayList<String> list = new ArrayList<String>(this.mCategories.size());
        final Iterator<CardCategory> iterator = this.mCategories.iterator();
        while (iterator.hasNext()) {
            list.add(iterator.next().name());
        }
        bundle.putStringArrayList("CARD_CATEGORY", (ArrayList)list);
        super.onSaveInstanceState(bundle);
        if (this.isVisible()) {
            this.mSkipCardImpressionsReset = true;
        }
    }
    
    public void setCategories(final EnumSet<CardCategory> set) {
        EnumSet<CardCategory> allCategories;
        if (set == null) {
            AppboyLogger.i(AppboyFeedFragment.TAG, "The categories passed into setCategories are null, AppboyFeedFragment is going to display all the cards in cache.");
            allCategories = CardCategory.getAllCategories();
        }
        else {
            if (set.isEmpty()) {
                AppboyLogger.w(AppboyFeedFragment.TAG, "The categories set had no elements and have been ignored. Please pass a valid EnumSet of CardCategory.");
                return;
            }
            allCategories = set;
            if (set.equals(this.mCategories)) {
                return;
            }
        }
        this.mCategories = allCategories;
        if (Appboy.getInstance(this.getContext()) != null) {
            Appboy.getInstance(this.getContext()).requestFeedRefreshFromCache();
        }
    }
    
    public void setCategory(final CardCategory cardCategory) {
        this.setCategories(EnumSet.of(cardCategory));
    }
    
    public void setSortEnabled(final boolean mSortEnabled) {
        this.mSortEnabled = mSortEnabled;
    }
    
    public List<Card> sortFeedCards(final List<Card> list) {
        Collections.sort((List<Object>)list, (Comparator<? super Object>)new Comparator<Card>() {
            @Override
            public int compare(final Card card, final Card card2) {
                if (card.isRead() == card2.isRead()) {
                    return 0;
                }
                if (card.isRead()) {
                    return 1;
                }
                return -1;
            }
            
            @Override
            public Comparator<Object> reversed() {
                return Comparator-CC.$default$reversed();
            }
            
            @Override
            public Comparator<Object> thenComparing(final Comparator<?> p0) {
                // 
                // This method could not be decompiled.
                // 
                // Could not show original bytecode, likely due to the same error.
                // 
                // The error that occurred was:
                // 
                // java.lang.NullPointerException
                // 
                throw new IllegalStateException("An error occurred while decompiling this method.");
            }
            
            @Override
            public <U extends Comparable<? super U>> Comparator<Object> thenComparing(final Function<?, ? extends U> p0) {
                // 
                // This method could not be decompiled.
                // 
                // Could not show original bytecode, likely due to the same error.
                // 
                // The error that occurred was:
                // 
                // java.lang.NullPointerException
                //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
                //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:21)
                //     at com.strobel.assembler.metadata.MetadataHelper$5.visitWildcard(MetadataHelper.java:1793)
                //     at com.strobel.assembler.metadata.MetadataHelper$5.visitWildcard(MetadataHelper.java:1790)
                //     at com.strobel.assembler.metadata.WildcardType.accept(WildcardType.java:83)
                //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
                //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:21)
                //     at com.strobel.assembler.metadata.MetadataHelper.getLowerBound(MetadataHelper.java:1240)
                //     at com.strobel.assembler.metadata.MetadataHelper$Adapter.visitWildcard(MetadataHelper.java:2278)
                //     at com.strobel.assembler.metadata.MetadataHelper$Adapter.visitWildcard(MetadataHelper.java:2222)
                //     at com.strobel.assembler.metadata.WildcardType.accept(WildcardType.java:83)
                //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
                //     at com.strobel.assembler.metadata.MetadataHelper$Adapter.adaptRecursive(MetadataHelper.java:2256)
                //     at com.strobel.assembler.metadata.MetadataHelper$Adapter.adaptRecursive(MetadataHelper.java:2233)
                //     at com.strobel.assembler.metadata.MetadataHelper$Adapter.visitParameterizedType(MetadataHelper.java:2246)
                //     at com.strobel.assembler.metadata.MetadataHelper$Adapter.visitParameterizedType(MetadataHelper.java:2222)
                //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedGenericType.accept(CoreMetadataFactory.java:653)
                //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
                //     at com.strobel.assembler.metadata.MetadataHelper.adapt(MetadataHelper.java:1312)
                //     at com.strobel.assembler.metadata.MetadataHelper$11.visitClassType(MetadataHelper.java:2708)
                //     at com.strobel.assembler.metadata.MetadataHelper$11.visitClassType(MetadataHelper.java:2692)
                //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visitParameterizedType(DefaultTypeVisitor.java:65)
                //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedGenericType.accept(CoreMetadataFactory.java:653)
                //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
                //     at com.strobel.assembler.metadata.MetadataHelper.asSubType(MetadataHelper.java:720)
                //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:926)
                //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
                //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
                //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
                //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2515)
                //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
                //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
                //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:778)
                //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1656)
                //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
                //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:672)
                //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:655)
                //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:365)
                //     at com.strobel.decompiler.ast.TypeAnalysis.run(TypeAnalysis.java:96)
                //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:109)
                //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformCall(AstMethodBodyBuilder.java:1162)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:1009)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:554)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformNode(AstMethodBodyBuilder.java:392)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformBlock(AstMethodBodyBuilder.java:333)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:294)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
                //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
                //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
                //     at us.deathmarine.luyten.FileSaver.doSaveJarDecompiled(FileSaver.java:192)
                //     at us.deathmarine.luyten.FileSaver.access$300(FileSaver.java:45)
                //     at us.deathmarine.luyten.FileSaver$4.run(FileSaver.java:112)
                //     at java.lang.Thread.run(Unknown Source)
                // 
                throw new IllegalStateException("An error occurred while decompiling this method.");
            }
            
            @Override
            public <U> Comparator<Object> thenComparing(final Function<?, ? extends U> p0, final Comparator<? super U> p1) {
                // 
                // This method could not be decompiled.
                // 
                // Could not show original bytecode, likely due to the same error.
                // 
                // The error that occurred was:
                // 
                // java.lang.NullPointerException
                //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
                //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:21)
                //     at com.strobel.assembler.metadata.MetadataHelper$5.visitWildcard(MetadataHelper.java:1793)
                //     at com.strobel.assembler.metadata.MetadataHelper$5.visitWildcard(MetadataHelper.java:1790)
                //     at com.strobel.assembler.metadata.WildcardType.accept(WildcardType.java:83)
                //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
                //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:21)
                //     at com.strobel.assembler.metadata.MetadataHelper.getLowerBound(MetadataHelper.java:1240)
                //     at com.strobel.assembler.metadata.MetadataHelper$Adapter.visitWildcard(MetadataHelper.java:2278)
                //     at com.strobel.assembler.metadata.MetadataHelper$Adapter.visitWildcard(MetadataHelper.java:2222)
                //     at com.strobel.assembler.metadata.WildcardType.accept(WildcardType.java:83)
                //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
                //     at com.strobel.assembler.metadata.MetadataHelper$Adapter.adaptRecursive(MetadataHelper.java:2256)
                //     at com.strobel.assembler.metadata.MetadataHelper$Adapter.adaptRecursive(MetadataHelper.java:2233)
                //     at com.strobel.assembler.metadata.MetadataHelper$Adapter.visitParameterizedType(MetadataHelper.java:2246)
                //     at com.strobel.assembler.metadata.MetadataHelper$Adapter.visitParameterizedType(MetadataHelper.java:2222)
                //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedGenericType.accept(CoreMetadataFactory.java:653)
                //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
                //     at com.strobel.assembler.metadata.MetadataHelper.adapt(MetadataHelper.java:1312)
                //     at com.strobel.assembler.metadata.MetadataHelper$11.visitClassType(MetadataHelper.java:2708)
                //     at com.strobel.assembler.metadata.MetadataHelper$11.visitClassType(MetadataHelper.java:2692)
                //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visitParameterizedType(DefaultTypeVisitor.java:65)
                //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedGenericType.accept(CoreMetadataFactory.java:653)
                //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
                //     at com.strobel.assembler.metadata.MetadataHelper.asSubType(MetadataHelper.java:720)
                //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:926)
                //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
                //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
                //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
                //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2515)
                //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
                //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
                //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:778)
                //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1656)
                //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
                //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:672)
                //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:655)
                //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:365)
                //     at com.strobel.decompiler.ast.TypeAnalysis.run(TypeAnalysis.java:96)
                //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:109)
                //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformCall(AstMethodBodyBuilder.java:1162)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:1009)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:554)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformNode(AstMethodBodyBuilder.java:392)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformBlock(AstMethodBodyBuilder.java:333)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:294)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
                //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
                //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
                //     at us.deathmarine.luyten.FileSaver.doSaveJarDecompiled(FileSaver.java:192)
                //     at us.deathmarine.luyten.FileSaver.access$300(FileSaver.java:45)
                //     at us.deathmarine.luyten.FileSaver$4.run(FileSaver.java:112)
                //     at java.lang.Thread.run(Unknown Source)
                // 
                throw new IllegalStateException("An error occurred while decompiling this method.");
            }
            
            @Override
            public Comparator<Object> thenComparingDouble(final ToDoubleFunction<?> p0) {
                // 
                // This method could not be decompiled.
                // 
                // Could not show original bytecode, likely due to the same error.
                // 
                // The error that occurred was:
                // 
                // java.lang.NullPointerException
                // 
                throw new IllegalStateException("An error occurred while decompiling this method.");
            }
            
            @Override
            public Comparator<Object> thenComparingInt(final ToIntFunction<?> p0) {
                // 
                // This method could not be decompiled.
                // 
                // Could not show original bytecode, likely due to the same error.
                // 
                // The error that occurred was:
                // 
                // java.lang.NullPointerException
                // 
                throw new IllegalStateException("An error occurred while decompiling this method.");
            }
            
            @Override
            public Comparator<Object> thenComparingLong(final ToLongFunction<?> p0) {
                // 
                // This method could not be decompiled.
                // 
                // Could not show original bytecode, likely due to the same error.
                // 
                // The error that occurred was:
                // 
                // java.lang.NullPointerException
                // 
                throw new IllegalStateException("An error occurred while decompiling this method.");
            }
        });
        return list;
    }
    
    public class FeedGestureListener extends GestureDetector$SimpleOnGestureListener
    {
        public boolean onDown(final MotionEvent motionEvent) {
            return true;
        }
        
        public boolean onFling(final MotionEvent motionEvent, final MotionEvent motionEvent2, final float n, final float n2) {
            final long n3 = (motionEvent2.getEventTime() - motionEvent.getEventTime()) * 2L;
            AppboyFeedFragment.this.getListView().smoothScrollBy(-(int)(n2 * n3 / 1000.0f), (int)(n3 * 2L));
            return true;
        }
        
        public boolean onScroll(final MotionEvent motionEvent, final MotionEvent motionEvent2, final float n, final float n2) {
            AppboyFeedFragment.this.getListView().smoothScrollBy((int)n2, 0);
            return true;
        }
    }
}
