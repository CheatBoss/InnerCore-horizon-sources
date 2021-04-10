package com.microsoft.xbox.xle.viewmodel;

import com.microsoft.xbox.service.model.sls.*;
import com.microsoft.xbox.service.model.*;
import com.microsoft.xbox.toolkit.ui.*;
import com.microsoft.xbox.xle.app.adapter.*;
import java.util.*;
import com.microsoft.xboxtcui.*;
import com.microsoft.xbox.telemetry.helpers.*;
import com.microsoft.xbox.toolkit.*;

public class ReportUserScreenViewModel extends ViewModelBase
{
    private FeedbackType[] feedbackReasons;
    private boolean isSubmittingReport;
    private ProfileModel model;
    private FeedbackType selectedReason;
    private SubmitReportAsyncTask submitReportAsyncTask;
    
    public ReportUserScreenViewModel(final ScreenLayout screenLayout) {
        super(screenLayout);
        final String selectedProfile = NavigationManager.getInstance().getActivityParameters().getSelectedProfile();
        XLEAssert.assertTrue(JavaUtil.isNullOrEmpty(selectedProfile) ^ true);
        if (JavaUtil.isNullOrEmpty(selectedProfile)) {
            this.popScreenWithXuidError();
        }
        final ProfileModel profileModel = ProfileModel.getProfileModel(selectedProfile);
        this.model = profileModel;
        XLEAssert.assertTrue(JavaUtil.isNullOrEmpty(profileModel.getGamerTag()) ^ true);
        this.adapter = new ReportUserScreenAdapter(this);
        final FeedbackType userContentPersonalInfo = FeedbackType.UserContentPersonalInfo;
        final FeedbackType fairPlayCheater = FeedbackType.FairPlayCheater;
        FeedbackType feedbackType;
        if (JavaUtil.isNullOrEmpty(this.model.getRealName())) {
            feedbackType = FeedbackType.UserContentGamertag;
        }
        else {
            feedbackType = FeedbackType.UserContentRealName;
        }
        this.feedbackReasons = new FeedbackType[] { userContentPersonalInfo, fairPlayCheater, feedbackType, FeedbackType.UserContentGamerpic, FeedbackType.FairPlayQuitter, FeedbackType.FairplayUnsporting, FeedbackType.CommsAbusiveVoice };
    }
    
    private void onSubmitReportCompleted(final AsyncActionStatus asyncActionStatus) {
        final int n = ReportUserScreenViewModel$1.$SwitchMap$com$microsoft$xbox$toolkit$AsyncActionStatus[asyncActionStatus.ordinal()];
        if (n == 1 || n == 2 || n == 3) {
            DialogManager.getInstance().showToast(R$string.ProfileCard_Report_SuccessSubtext);
            this.onBackButtonPressed();
            return;
        }
        if (n != 4 && n != 5) {
            return;
        }
        this.showError(R$string.ProfileCard_Report_Error);
    }
    
    private void popScreenWithXuidError() {
        try {
            this.showError(R$string.Service_ErrorText);
            NavigationManager.getInstance().PopScreen();
        }
        catch (XLEException ex) {}
    }
    
    public int getPreferredColor() {
        return this.model.getPreferedColor();
    }
    
    public FeedbackType getReason() {
        return this.selectedReason;
    }
    
    public ArrayList<String> getReasonTitles() {
        final ArrayList<String> list = new ArrayList<String>(this.feedbackReasons.length);
        list.add(XboxTcuiSdk.getResources().getString(R$string.ProfileCard_Report_SelectReason));
        final FeedbackType[] feedbackReasons = this.feedbackReasons;
        for (int length = feedbackReasons.length, i = 0; i < length; ++i) {
            list.add(feedbackReasons[i].getTitle());
        }
        return list;
    }
    
    public String getTitle() {
        return String.format(XboxTcuiSdk.getResources().getString(R$string.ProfileCard_Report_InfoString_Android), this.model.getGamerTag());
    }
    
    public String getXUID() {
        return this.model.getXuid();
    }
    
    @Override
    public boolean isBusy() {
        return this.isSubmittingReport;
    }
    
    @Override
    public void load(final boolean b) {
    }
    
    @Override
    public boolean onBackButtonPressed() {
        UTCPageView.removePage();
        try {
            NavigationManager.getInstance().PopScreensAndReplace(1, null, false, false, false, NavigationManager.getInstance().getActivityParameters());
            return true;
        }
        catch (XLEException ex) {
            return false;
        }
    }
    
    @Override
    public void onRehydrate() {
    }
    
    @Override
    protected void onStartOverride() {
    }
    
    @Override
    protected void onStopOverride() {
        final SubmitReportAsyncTask submitReportAsyncTask = this.submitReportAsyncTask;
        if (submitReportAsyncTask != null) {
            submitReportAsyncTask.cancel();
        }
    }
    
    public void setReason(final int n) {
        FeedbackType selectedReason;
        if (n != 0 && n - 1 < this.feedbackReasons.length) {
            selectedReason = this.feedbackReasons[n - 1];
        }
        else {
            selectedReason = null;
        }
        this.selectedReason = selectedReason;
        this.updateAdapter();
    }
    
    public void submitReport(final String s) {
        final SubmitReportAsyncTask submitReportAsyncTask = this.submitReportAsyncTask;
        if (submitReportAsyncTask != null) {
            submitReportAsyncTask.cancel();
        }
        if (this.selectedReason != null) {
            (this.submitReportAsyncTask = new SubmitReportAsyncTask(this.model, this.selectedReason, s)).load(true);
        }
    }
    
    public boolean validReasonSelected() {
        return this.selectedReason != null;
    }
    
    private class SubmitReportAsyncTask extends NetworkAsyncTask<AsyncActionStatus>
    {
        private FeedbackType feedbackType;
        private ProfileModel model;
        private String textReason;
        
        private SubmitReportAsyncTask(final ProfileModel model, final FeedbackType feedbackType, final String textReason) {
            this.model = model;
            this.feedbackType = feedbackType;
            this.textReason = textReason;
        }
        
        @Override
        protected boolean checkShouldExecute() {
            XLEAssert.assertIsUIThread();
            return true;
        }
        
        @Override
        protected AsyncActionStatus loadDataInBackground() {
            XLEAssert.assertNotNull(this.model);
            return this.model.submitFeedbackForUser(this.forceLoad, this.feedbackType, this.textReason).getStatus();
        }
        
        @Override
        protected AsyncActionStatus onError() {
            return AsyncActionStatus.FAIL;
        }
        
        @Override
        protected void onNoAction() {
            XLEAssert.assertIsUIThread();
            ReportUserScreenViewModel.this.onSubmitReportCompleted(AsyncActionStatus.NO_CHANGE);
        }
        
        @Override
        protected void onPostExecute(final AsyncActionStatus asyncActionStatus) {
            ReportUserScreenViewModel.this.onSubmitReportCompleted(asyncActionStatus);
        }
        
        @Override
        protected void onPreExecute() {
            XLEAssert.assertIsUIThread();
            ReportUserScreenViewModel.this.isSubmittingReport = true;
            ReportUserScreenViewModel.this.updateAdapter();
        }
    }
}
