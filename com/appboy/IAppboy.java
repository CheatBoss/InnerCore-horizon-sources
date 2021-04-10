package com.appboy;

import android.app.*;
import com.appboy.models.outgoing.*;
import java.math.*;
import android.content.*;
import com.appboy.events.*;

public interface IAppboy
{
    void changeUser(final String p0);
    
    void closeSession(final Activity p0);
    
    IAppboyImageLoader getAppboyImageLoader();
    
    String getAppboyPushMessageRegistrationId();
    
    int getContentCardCount();
    
    int getContentCardUnviewedCount();
    
    long getContentCardsLastUpdatedInSecondsFromEpoch();
    
    AppboyUser getCurrentUser();
    
    String getInstallTrackingId();
    
    void logContentCardsDisplayed();
    
    void logCustomEvent(final String p0);
    
    void logCustomEvent(final String p0, final AppboyProperties p1);
    
    @Deprecated
    void logFeedCardClick(final String p0);
    
    @Deprecated
    void logFeedCardImpression(final String p0);
    
    void logFeedDisplayed();
    
    void logFeedbackDisplayed();
    
    void logPurchase(final String p0, final String p1, final BigDecimal p2);
    
    void logPurchase(final String p0, final String p1, final BigDecimal p2, final int p3);
    
    void logPurchase(final String p0, final String p1, final BigDecimal p2, final int p3, final AppboyProperties p4);
    
    void logPurchase(final String p0, final String p1, final BigDecimal p2, final AppboyProperties p3);
    
    void logPushDeliveryEvent(final String p0);
    
    void logPushNotificationActionClicked(final String p0, final String p1);
    
    void logPushNotificationOpened(final Intent p0);
    
    void logPushNotificationOpened(final String p0);
    
    void logPushStoryPageClicked(final String p0, final String p1);
    
    void openSession(final Activity p0);
    
    void registerAppboyPushMessages(final String p0);
    
     <T> void removeSingleSubscription(final IEventSubscriber<T> p0, final Class<T> p1);
    
    void requestContentCardsRefresh(final boolean p0);
    
    void requestFeedRefresh();
    
    void requestFeedRefreshFromCache();
    
    void requestImmediateDataFlush();
    
    void setAppboyImageLoader(final IAppboyImageLoader p0);
    
    void submitFeedback(final String p0, final String p1, final boolean p2);
    
    void subscribeToContentCardsUpdates(final IEventSubscriber<ContentCardsUpdatedEvent> p0);
    
    void subscribeToFeedUpdates(final IEventSubscriber<FeedUpdatedEvent> p0);
    
    void subscribeToFeedbackRequestEvents(final IEventSubscriber<SubmitFeedbackSucceeded> p0, final IEventSubscriber<SubmitFeedbackFailed> p1);
    
    void subscribeToNewInAppMessages(final IEventSubscriber<InAppMessageEvent> p0);
}
