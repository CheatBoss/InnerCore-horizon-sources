package com.microsoft.xbox.xle.app.activity;

import com.microsoft.xbox.xle.viewmodel.*;
import com.microsoft.xbox.toolkit.ui.*;
import com.microsoft.xbox.telemetry.helpers.*;
import com.microsoft.xboxtcui.*;

public class ReportUserScreen extends ActivityBase
{
    private ReportUserScreenViewModel reportUserScreenViewModel;
    
    @Override
    protected String getActivityName() {
        return "Report user";
    }
    
    @Override
    public void onCreate() {
        super.onCreate();
        this.onCreateContentView();
        this.viewModel = new ReportUserScreenViewModel(this);
        this.reportUserScreenViewModel = (ReportUserScreenViewModel)this.viewModel;
        UTCReportUser.trackReportView(this.getName(), this.reportUserScreenViewModel.getXUID());
    }
    
    @Override
    public void onCreateContentView() {
        this.setContentView(R$layout.report_user_screen);
    }
    
    @Override
    public void onStart() {
        super.onStart();
        this.setBackgroundColor(this.reportUserScreenViewModel.getPreferredColor());
    }
}
