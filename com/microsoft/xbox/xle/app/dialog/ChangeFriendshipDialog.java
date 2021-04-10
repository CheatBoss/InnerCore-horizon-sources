package com.microsoft.xbox.xle.app.dialog;

import com.microsoft.xbox.xle.viewmodel.*;
import android.content.*;
import android.os.*;
import com.microsoft.xbox.toolkit.ui.*;
import android.view.*;
import com.microsoft.xbox.telemetry.helpers.*;
import com.microsoft.xbox.toolkit.*;
import com.microsoft.xbox.toolkit.network.*;
import com.microsoft.xbox.xle.app.*;
import com.microsoft.xboxtcui.*;
import android.widget.*;

public class ChangeFriendshipDialog extends XLEManagedDialog
{
    private RadioButton addFavorite;
    private RadioButton addFriend;
    private XLEButton cancelButton;
    private SwitchPanel changeFriendshipSwitchPanel;
    private XLEButton confirmButton;
    private CustomTypefaceTextView favoriteIconView;
    private CustomTypefaceTextView gamertag;
    private FastProgressBar overlayLoadingIndicator;
    private ViewModelBase previousVM;
    private TextView profileAccountTier;
    private CustomTypefaceTextView profileGamerScore;
    private XLEUniversalImageView profilePic;
    private CustomTypefaceTextView realName;
    private XLEClickableLayout removeFriendLayout;
    private XLECheckBox shareRealNameCheckbox;
    private ChangeFriendshipDialogViewModel vm;
    
    public ChangeFriendshipDialog(final Context context, final ChangeFriendshipDialogViewModel vm, final ViewModelBase previousVM) {
        super(context, R$style.TcuiDialog);
        this.previousVM = previousVM;
        this.vm = vm;
    }
    
    private void dismissSelf() {
        ((SGProjectSpecificDialogManager)DialogManager.getInstance().getManager()).dismissChangeFriendshipDialog();
    }
    
    private void setDialogLoadingView() {
        XLEUtil.updateVisibilityIfNotNull((View)this.overlayLoadingIndicator, 0);
        final XLEButton confirmButton = this.confirmButton;
        if (confirmButton != null) {
            confirmButton.setEnabled(false);
        }
        final XLEButton cancelButton = this.cancelButton;
        if (cancelButton != null) {
            cancelButton.setEnabled(false);
        }
    }
    
    private void setDialogValidContentView() {
        XLEUtil.updateVisibilityIfNotNull((View)this.overlayLoadingIndicator, 8);
        final XLEButton confirmButton = this.confirmButton;
        if (confirmButton != null) {
            confirmButton.setEnabled(true);
        }
        final XLEButton cancelButton = this.cancelButton;
        if (cancelButton != null) {
            cancelButton.setEnabled(true);
        }
    }
    
    public void closeDialog() {
        this.dismissSelf();
        this.previousVM.load(true);
    }
    
    protected String getActivityName() {
        return "ChangeRelationship Info";
    }
    
    public void onBackPressed() {
        this.dismissSelf();
    }
    
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.requestWindowFeature(1);
        this.getWindow().setLayout(-1, -1);
        this.getWindow().setFlags(1024, 1024);
        this.setContentView(R$layout.change_friendship_dialog);
        this.profilePic = (XLEUniversalImageView)this.findViewById(R$id.change_friendship_profile_pic);
        this.gamertag = (CustomTypefaceTextView)this.findViewById(R$id.gamertag_text);
        this.realName = (CustomTypefaceTextView)this.findViewById(R$id.realname_text);
        this.profileAccountTier = (TextView)this.findViewById(R$id.peoplehub_info_gamerscore_icon);
        this.profileGamerScore = (CustomTypefaceTextView)this.findViewById(R$id.peoplehub_info_gamerscore);
        this.addFriend = (RadioButton)this.findViewById(R$id.add_as_friend);
        this.addFavorite = (RadioButton)this.findViewById(R$id.add_as_favorite);
        this.shareRealNameCheckbox = (XLECheckBox)this.findViewById(R$id.share_real_name_checkbox);
        this.confirmButton = (XLEButton)this.findViewById(R$id.submit_button);
        this.cancelButton = (XLEButton)this.findViewById(R$id.cancel_button);
        this.changeFriendshipSwitchPanel = (SwitchPanel)this.findViewById(R$id.change_friendship_switch_panel);
        this.removeFriendLayout = (XLEClickableLayout)this.findViewById(R$id.remove_friend_btn_layout);
        this.favoriteIconView = (CustomTypefaceTextView)this.findViewById(R$id.people_favorites_icon);
        this.overlayLoadingIndicator = (FastProgressBar)this.findViewById(R$id.overlay_loading_indicator);
        final FrameLayout frameLayout = new FrameLayout(this.getContext());
        final FrameLayout$LayoutParams frameLayout$LayoutParams = new FrameLayout$LayoutParams(-2, -2);
        frameLayout$LayoutParams.gravity = 5;
        final XLEButton xleButton = new XLEButton(this.getContext());
        xleButton.setPadding(60, 0, 0, 0);
        xleButton.setBackgroundResource(R$drawable.common_button_background);
        xleButton.setText(R$string.ic_Close);
        xleButton.setTextColor(-1);
        xleButton.setTextSize(2, 14.0f);
        xleButton.setTypeFace("fonts/SegXboxSymbol.ttf");
        xleButton.setContentDescription((CharSequence)this.getContext().getResources().getString(R$string.TextInput_Confirm));
        xleButton.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                try {
                    ChangeFriendshipDialog.this.dismiss();
                    NavigationManager.getInstance().PopAllScreens();
                }
                catch (XLEException ex) {}
            }
        });
        xleButton.setOnKeyListener((View$OnKeyListener)new View$OnKeyListener() {
            public boolean onKey(final View view, final int n, final KeyEvent keyEvent) {
                if (n == 4 && keyEvent.getAction() == 1) {
                    ChangeFriendshipDialog.this.dismiss();
                    return true;
                }
                return false;
            }
        });
        frameLayout.addView((View)xleButton);
        this.addContentView((View)frameLayout, (ViewGroup$LayoutParams)frameLayout$LayoutParams);
    }
    
    public void onStart() {
        this.vm.load();
        this.updateView();
        this.changeFriendshipSwitchPanel.setBackgroundColor(this.vm.getPreferredColor());
        UTCChangeRelationship.trackChangeRelationshipView(this.getActivityName(), this.vm.getXuid());
    }
    
    public void onStop() {
    }
    
    public void reportAsyncTaskCompleted() {
        if (!this.vm.isBusy() && this.changeFriendshipSwitchPanel.getState() == 1) {
            this.closeDialog();
        }
    }
    
    public void reportAsyncTaskFailed(final String s) {
        if (this.changeFriendshipSwitchPanel.getState() == 1) {
            this.changeFriendshipSwitchPanel.setState(0);
            Toast.makeText((Context)XboxTcuiSdk.getActivity(), (CharSequence)s, 0).show();
        }
        this.updateView();
    }
    
    public void setVm(final ChangeFriendshipDialogViewModel vm) {
        this.vm = vm;
    }
    
    public void updateShareIdentityCheckboxStatus() {
        final String callerShareRealNameStatus = this.vm.getCallerShareRealNameStatus();
        if (callerShareRealNameStatus != null) {
            final boolean equalsIgnoreCase = callerShareRealNameStatus.equalsIgnoreCase("Blocked");
            final XLECheckBox shareRealNameCheckbox = this.shareRealNameCheckbox;
            int visibility;
            if (equalsIgnoreCase) {
                visibility = 8;
            }
            else {
                visibility = 0;
            }
            shareRealNameCheckbox.setVisibility(visibility);
            if (!equalsIgnoreCase) {
                final boolean nullOrEmpty = JavaUtil.isNullOrEmpty(this.vm.getRealName());
                if (callerShareRealNameStatus.compareToIgnoreCase("Everyone") == 0) {
                    this.shareRealNameCheckbox.setChecked(true);
                    this.vm.setInitialRealNameSharingState(true);
                    this.shareRealNameCheckbox.setEnabled(false);
                    this.shareRealNameCheckbox.setSubText(XboxTcuiSdk.getResources().getString(R$string.ChangeRelationship_Checkbox_Subtext_ShareRealName_Everyone));
                }
                if (callerShareRealNameStatus.compareToIgnoreCase("PeopleOnMyList") == 0) {
                    this.shareRealNameCheckbox.setChecked(true);
                    this.vm.setInitialRealNameSharingState(true);
                    this.shareRealNameCheckbox.setEnabled(false);
                    this.shareRealNameCheckbox.setSubText(XboxTcuiSdk.getResources().getString(R$string.ChangeRelationship_Checkbox_Subtext_ShareRealName_Friends));
                }
                if (callerShareRealNameStatus.compareToIgnoreCase("FriendCategoryShareIdentity") == 0) {
                    Label_0257: {
                        if (this.vm.getIsFollowing()) {
                            if (this.vm.getCallerMarkedTargetAsIdentityShared()) {
                                this.shareRealNameCheckbox.setChecked(true);
                                this.vm.setInitialRealNameSharingState(true);
                                break Label_0257;
                            }
                        }
                        else if (nullOrEmpty ^ true) {
                            this.shareRealNameCheckbox.setChecked(true);
                            this.vm.setInitialRealNameSharingState(true);
                            this.vm.setShouldAddUserToShareIdentityList(true);
                            break Label_0257;
                        }
                        this.shareRealNameCheckbox.setChecked(false);
                        this.vm.setInitialRealNameSharingState(false);
                    }
                    this.shareRealNameCheckbox.setSubText(String.format(XboxTcuiSdk.getResources().getString(R$string.ChangeRelationship_Checkbox_Subtext_ShareRealName), this.vm.getGamerTag()));
                    this.shareRealNameCheckbox.setEnabled(true);
                }
            }
        }
    }
    
    public void updateView() {
        if (this.vm.getViewModelState() == ListState.ValidContentState) {
            this.setDialogValidContentView();
            XLEUtil.updateAndShowTextViewUnlessEmpty(this.gamertag, this.vm.getGamerTag());
            final XLEUniversalImageView profilePic = this.profilePic;
            if (profilePic != null) {
                profilePic.setImageURI2(ImageUtil.getMedium(this.vm.getGamerPicUrl()), R$drawable.gamerpic_missing, R$drawable.gamerpic_missing);
            }
            XLEUtil.updateAndShowTextViewUnlessEmpty(this.realName, this.vm.getRealName());
            final CustomTypefaceTextView favoriteIconView = this.favoriteIconView;
            int n;
            if (this.vm.getIsFavorite()) {
                n = 0;
            }
            else {
                n = 4;
            }
            XLEUtil.updateVisibilityIfNotNull((View)favoriteIconView, n);
            if (this.vm.getIsFavorite()) {
                this.favoriteIconView.setTextColor(this.getContext().getResources().getColor(R$color.XboxGreen));
            }
            final String gamerScore = this.vm.getGamerScore();
            if (gamerScore != null && !gamerScore.equalsIgnoreCase("0")) {
                XLEUtil.updateAndShowTextViewUnlessEmpty(this.profileGamerScore, this.vm.getGamerScore());
                XLEUtil.updateVisibilityIfNotNull((View)this.profileAccountTier, 0);
            }
            if (this.addFriend != null) {
                if (!this.vm.getIsFollowing()) {
                    this.vm.setShouldAddUserToFriendList(true);
                }
                this.addFriend.setChecked(true);
                this.addFriend.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                    public void onClick(final View view) {
                        if (!ChangeFriendshipDialog.this.vm.getIsFollowing()) {
                            ChangeFriendshipDialog.this.vm.setShouldAddUserToFriendList(true);
                        }
                        if (ChangeFriendshipDialog.this.vm.getIsFavorite()) {
                            ChangeFriendshipDialog.this.vm.setShouldRemoveUserFromFavoriteList(true);
                        }
                        ChangeFriendshipDialog.this.vm.setShouldAddUserToFavoriteList(false);
                    }
                });
            }
            if (this.addFavorite != null) {
                if (this.vm.getIsFavorite()) {
                    this.addFavorite.setChecked(true);
                }
                this.addFavorite.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                    public void onClick(final View view) {
                        if (!ChangeFriendshipDialog.this.vm.getIsFavorite()) {
                            ChangeFriendshipDialog.this.vm.setShouldAddUserToFavoriteList(true);
                        }
                        ChangeFriendshipDialog.this.vm.setShouldRemoveUserFromFavoriteList(false);
                    }
                });
            }
            final XLEButton confirmButton = this.confirmButton;
            if (confirmButton != null) {
                confirmButton.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                    public void onClick(final View view) {
                        ChangeFriendshipDialog.this.changeFriendshipSwitchPanel.setState(1);
                        ChangeFriendshipDialog.this.vm.onChangeRelationshipCompleted();
                    }
                });
            }
            final XLEButton cancelButton = this.cancelButton;
            if (cancelButton != null) {
                cancelButton.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                    public void onClick(final View view) {
                        ChangeFriendshipDialog.this.dismissSelf();
                        ChangeFriendshipDialog.this.vm.clearChangeFriendshipForm();
                    }
                });
            }
            final XLECheckBox shareRealNameCheckbox = this.shareRealNameCheckbox;
            if (shareRealNameCheckbox != null) {
                shareRealNameCheckbox.setChecked(this.vm.getCallerMarkedTargetAsIdentityShared());
                this.shareRealNameCheckbox.setOnCheckedChangeListener((CompoundButton$OnCheckedChangeListener)new CompoundButton$OnCheckedChangeListener() {
                    public void onCheckedChanged(final CompoundButton compoundButton, final boolean isSharingRealNameEnd) {
                        ChangeFriendshipDialog.this.vm.setIsSharingRealNameEnd(isSharingRealNameEnd);
                        if (isSharingRealNameEnd) {
                            if (!ChangeFriendshipDialog.this.vm.getCallerMarkedTargetAsIdentityShared()) {
                                ChangeFriendshipDialog.this.vm.setShouldAddUserToShareIdentityList(true);
                            }
                            ChangeFriendshipDialog.this.vm.setShouldRemoveUserFroShareIdentityList(false);
                            return;
                        }
                        if (ChangeFriendshipDialog.this.vm.getCallerMarkedTargetAsIdentityShared()) {
                            ChangeFriendshipDialog.this.vm.setShouldRemoveUserFroShareIdentityList(true);
                        }
                        ChangeFriendshipDialog.this.vm.setShouldAddUserToShareIdentityList(false);
                    }
                });
                this.updateShareIdentityCheckboxStatus();
            }
            if (this.removeFriendLayout != null) {
                if (this.vm.getIsFollowing()) {
                    this.removeFriendLayout.setVisibility(0);
                    this.removeFriendLayout.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                        public void onClick(final View view) {
                            UTCChangeRelationship.trackChangeRelationshipRemoveFriend();
                            ChangeFriendshipDialog.this.changeFriendshipSwitchPanel.setState(1);
                            ChangeFriendshipDialog.this.vm.removeFollowingUser();
                        }
                    });
                }
                else {
                    this.removeFriendLayout.setEnabled(false);
                    this.removeFriendLayout.setVisibility(8);
                }
                this.confirmButton.setText((CharSequence)this.vm.getDialogButtonText());
            }
            this.updateShareIdentityCheckboxStatus();
            return;
        }
        if (this.vm.getViewModelState() == ListState.LoadingState) {
            this.setDialogLoadingView();
        }
    }
}
