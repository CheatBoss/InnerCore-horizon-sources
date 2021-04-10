package com.microsoft.xbox.xle.app.adapter;

import com.microsoft.xbox.toolkit.ui.*;
import com.microsoft.xbox.xle.ui.*;
import com.microsoft.xbox.xle.app.activity.Profile.*;
import com.microsoft.xbox.xle.viewmodel.*;
import android.view.*;
import com.microsoft.xbox.telemetry.helpers.*;
import com.microsoft.xboxtcui.*;
import com.microsoft.xbox.toolkit.*;
import com.microsoft.xbox.xle.app.*;
import android.widget.*;

public class ProfileScreenAdapter extends AdapterBase
{
    private IconFontToggleButton blockButton;
    private ScrollView contentScrollView;
    private IconFontToggleButton followButton;
    private XLERoundedUniversalImageView gamerPicImageView;
    private CustomTypefaceTextView gamerscoreIconTextView;
    private CustomTypefaceTextView gamerscoreTextView;
    private CustomTypefaceTextView gamertagTextView;
    private FastProgressBar loadingProgressBar;
    private IconFontToggleButton muteButton;
    private CustomTypefaceTextView realNameTextView;
    private IconFontToggleButton reportButton;
    private XLERootView rootView;
    private IconFontToggleButton viewInXboxAppButton;
    private CustomTypefaceTextView viewInXboxAppSubTextView;
    private ProfileScreenViewModel viewModel;
    
    public ProfileScreenAdapter(final ProfileScreenViewModel viewModel) {
        super(viewModel);
        this.viewModel = viewModel;
        this.rootView = (XLERootView)this.findViewById(R$id.profile_root);
        this.gamerPicImageView = (XLERoundedUniversalImageView)this.findViewById(R$id.profile_gamerpic);
        this.loadingProgressBar = (FastProgressBar)this.findViewById(R$id.profile_screen_loading);
        this.contentScrollView = (ScrollView)this.findViewById(R$id.profile_screen_content_list);
        this.realNameTextView = (CustomTypefaceTextView)this.findViewById(R$id.profile_realname);
        this.gamerscoreIconTextView = (CustomTypefaceTextView)this.findViewById(R$id.profile_gamerscore_icon);
        this.gamerscoreTextView = (CustomTypefaceTextView)this.findViewById(R$id.profile_gamerscore);
        this.gamertagTextView = (CustomTypefaceTextView)this.findViewById(R$id.profile_gamertag);
        this.followButton = (IconFontToggleButton)this.findViewById(R$id.profile_follow);
        this.muteButton = (IconFontToggleButton)this.findViewById(R$id.profile_mute);
        this.blockButton = (IconFontToggleButton)this.findViewById(R$id.profile_block);
        this.reportButton = (IconFontToggleButton)this.findViewById(R$id.profile_report);
        this.viewInXboxAppButton = (IconFontToggleButton)this.findViewById(R$id.profile_view_in_xbox_app);
        this.viewInXboxAppSubTextView = (CustomTypefaceTextView)this.findViewById(R$id.profile_view_in_xbox_app_subtext);
        this.viewInXboxAppButton.setVisibility(0);
        this.viewInXboxAppButton.setEnabled(true);
        this.viewInXboxAppButton.setChecked(true);
        CustomTypefaceTextView customTypefaceTextView;
        int text;
        if (this.viewModel.isMeProfile()) {
            this.followButton.setVisibility(8);
            this.muteButton.setVisibility(8);
            this.blockButton.setVisibility(8);
            this.reportButton.setVisibility(8);
            customTypefaceTextView = this.viewInXboxAppSubTextView;
            text = R$string.Profile_ViewInXboxApp_Details_MeProfile;
        }
        else {
            this.followButton.setVisibility(0);
            this.followButton.setEnabled(true);
            this.muteButton.setVisibility(0);
            this.muteButton.setEnabled(true);
            this.muteButton.setChecked(false);
            this.blockButton.setVisibility(0);
            this.blockButton.setEnabled(false);
            this.reportButton.setVisibility(0);
            this.reportButton.setEnabled(true);
            this.reportButton.setChecked(false);
            customTypefaceTextView = this.viewInXboxAppSubTextView;
            text = R$string.Profile_ViewInXboxApp_Details_YouProfile;
        }
        customTypefaceTextView.setText(text);
    }
    
    @Override
    public void onStart() {
        super.onStart();
        final IconFontToggleButton followButton = this.followButton;
        if (followButton != null) {
            followButton.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                public void onClick(final View view) {
                    ProfileScreenAdapter.this.viewModel.navigateToChangeRelationship();
                }
            });
        }
        final IconFontToggleButton muteButton = this.muteButton;
        if (muteButton != null) {
            muteButton.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                public void onClick(final View view) {
                    ProfileScreenAdapter.this.muteButton.toggle();
                    ProfileScreenAdapter.this.muteButton.setEnabled(false);
                    if (ProfileScreenAdapter.this.muteButton.isChecked()) {
                        UTCPeopleHub.trackMute(true);
                        ProfileScreenAdapter.this.viewModel.muteUser();
                        return;
                    }
                    UTCPeopleHub.trackMute(false);
                    ProfileScreenAdapter.this.viewModel.unmuteUser();
                }
            });
        }
        final IconFontToggleButton blockButton = this.blockButton;
        if (blockButton != null) {
            blockButton.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                public void onClick(final View view) {
                    ProfileScreenAdapter.this.blockButton.toggle();
                    ProfileScreenAdapter.this.blockButton.setEnabled(false);
                    if (ProfileScreenAdapter.this.blockButton.isChecked()) {
                        UTCPeopleHub.trackBlock();
                        ProfileScreenAdapter.this.viewModel.blockUser();
                        return;
                    }
                    UTCPeopleHub.trackUnblock();
                    ProfileScreenAdapter.this.viewModel.unblockUser();
                }
            });
        }
        final IconFontToggleButton reportButton = this.reportButton;
        if (reportButton != null) {
            reportButton.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                public void onClick(final View view) {
                    UTCPeopleHub.trackReport();
                    ProfileScreenAdapter.this.viewModel.showReportDialog();
                }
            });
        }
        if (this.viewInXboxAppButton != null) {
            if (XboxAppDeepLinker.appDeeplinkingSupported()) {
                this.viewInXboxAppButton.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                    public void onClick(final View view) {
                        UTCPeopleHub.trackViewInXboxApp();
                        ProfileScreenAdapter.this.viewModel.launchXboxApp();
                    }
                });
                return;
            }
            this.viewInXboxAppButton.setVisibility(8);
            this.viewInXboxAppSubTextView.setVisibility(8);
        }
    }
    
    @Override
    protected void updateViewOverride() {
        final XLERootView rootView = this.rootView;
        if (rootView != null) {
            rootView.setBackgroundColor(this.viewModel.getPreferredColor());
        }
        final FastProgressBar loadingProgressBar = this.loadingProgressBar;
        final boolean busy = this.viewModel.isBusy();
        final boolean b = false;
        int visibility;
        if (busy) {
            visibility = 0;
        }
        else {
            visibility = 8;
        }
        loadingProgressBar.setVisibility(visibility);
        final ScrollView contentScrollView = this.contentScrollView;
        int visibility2;
        if (this.viewModel.isBusy()) {
            visibility2 = 8;
        }
        else {
            visibility2 = 0;
        }
        contentScrollView.setVisibility(visibility2);
        final XLERoundedUniversalImageView gamerPicImageView = this.gamerPicImageView;
        if (gamerPicImageView != null) {
            gamerPicImageView.setImageURI2(ImageUtil.getMedium(this.viewModel.getGamerPicUrl()), R$drawable.gamerpic_missing, R$drawable.gamerpic_missing);
        }
        if (this.realNameTextView != null) {
            final String realName = this.viewModel.getRealName();
            if (!JavaUtil.isNullOrEmpty(realName)) {
                this.realNameTextView.setText((CharSequence)realName);
                this.realNameTextView.setVisibility(0);
            }
            else {
                this.realNameTextView.setVisibility(8);
            }
        }
        if (this.gamerscoreTextView != null && this.gamerscoreIconTextView != null) {
            final String gamerScore = this.viewModel.getGamerScore();
            if (!JavaUtil.isNullOrEmpty(gamerScore)) {
                XLEUtil.updateTextAndVisibilityIfNotNull(this.gamerscoreTextView, gamerScore, 0);
                XLEUtil.updateVisibilityIfNotNull((View)this.gamerscoreIconTextView, 0);
            }
        }
        if (this.gamertagTextView != null) {
            final String gamerTag = this.viewModel.getGamerTag();
            if (!JavaUtil.isNullOrEmpty(gamerTag)) {
                XLEUtil.updateTextAndVisibilityIfNotNull(this.gamertagTextView, gamerTag, 0);
            }
        }
        if (!this.viewModel.isMeProfile()) {
            final boolean b2 = this.viewModel.getIsAddingUserToBlockList() || this.viewModel.getIsRemovingUserFromBlockList();
            this.followButton.setChecked(this.viewModel.isCallerFollowingTarget());
            this.followButton.setEnabled(!b2 && !this.viewModel.getIsBlocked());
            this.muteButton.setChecked(this.viewModel.getIsMuted());
            final IconFontToggleButton muteButton = this.muteButton;
            boolean enabled = b;
            if (!this.viewModel.getIsAddingUserToMutedList()) {
                enabled = b;
                if (!this.viewModel.getIsRemovingUserFromMutedList()) {
                    enabled = true;
                }
            }
            muteButton.setEnabled(enabled);
            this.blockButton.setChecked(this.viewModel.getIsBlocked());
            this.blockButton.setEnabled(b2 ^ true);
        }
    }
}
