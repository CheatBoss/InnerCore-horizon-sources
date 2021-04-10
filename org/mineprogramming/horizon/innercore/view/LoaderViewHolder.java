package org.mineprogramming.horizon.innercore.view;

import android.support.v7.widget.*;
import android.widget.*;
import org.mineprogramming.horizon.innercore.inflater.layout.*;
import android.view.*;

public class LoaderViewHolder extends RecyclerView$ViewHolder
{
    private final LinearLayout llLayout;
    private final ProgressBar pbProgress;
    private final TextView tvNoInternet;
    private final TextView tvRetry;
    
    public LoaderViewHolder(final InflatedView inflatedView) {
        super(inflatedView.getView());
        this.tvRetry = inflatedView.getViewByJsonId("retry");
        this.tvNoInternet = inflatedView.getViewByJsonId("no_internet");
        this.pbProgress = inflatedView.getViewByJsonId("loader");
        this.llLayout = inflatedView.getViewByJsonId("progress_layout");
    }
    
    public void onDownloadFailed() {
        this.llLayout.setVisibility(0);
        this.pbProgress.setVisibility(8);
        this.tvRetry.setVisibility(0);
        this.tvNoInternet.setVisibility(0);
    }
    
    public void onDownloadFinished() {
        this.llLayout.setVisibility(8);
    }
    
    public void onDownloadInProgress() {
        this.llLayout.setVisibility(0);
        this.pbProgress.setVisibility(0);
        this.tvRetry.setVisibility(8);
        this.tvNoInternet.setVisibility(8);
    }
    
    public void setOnRetryClickListener(final View$OnClickListener onClickListener) {
        this.tvRetry.setOnClickListener(onClickListener);
    }
    
    public void setOnTryAgainClickListener(final View$OnClickListener onClickListener) {
        this.tvRetry.setOnClickListener(onClickListener);
    }
}
