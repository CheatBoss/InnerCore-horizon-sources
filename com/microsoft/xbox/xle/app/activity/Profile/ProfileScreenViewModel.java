package com.microsoft.xbox.xle.app.activity.Profile;

import com.microsoft.xbox.service.model.*;
import com.microsoft.xbox.toolkit.ui.*;
import com.microsoft.xbox.xle.app.adapter.*;
import com.microsoft.xbox.xle.app.*;
import com.microsoft.xbox.xle.viewmodel.*;
import com.microsoft.xboxtcui.*;
import com.microsoft.xbox.telemetry.helpers.*;
import android.app.*;
import android.content.*;
import com.microsoft.xbox.xle.app.activity.*;
import com.microsoft.xbox.toolkit.*;
import com.microsoft.xbox.service.network.managers.*;
import java.util.*;

public class ProfileScreenViewModel extends ViewModelBase
{
    private static final String TAG;
    private AddUserToFollowingListAsyncTask addUserToFollowingListAsyncTask;
    private AddUserToMutedListAsyncTask addUserToMutedListAsyncTask;
    private AddUserToNeverListAsyncTask addUserToNeverListAsyncTask;
    private AddUserToShareIdentityListAsyncTask addUserToShareIdentityListAsyncTask;
    private FollowersData basicData;
    private ChangeFriendshipDialogViewModel changeFriendshipDialogViewModel;
    private HashSet<ChangeFriendshipFormOptions> changeFriendshipForm;
    private boolean isAddingUserToBlockList;
    private boolean isAddingUserToFollowingList;
    private boolean isAddingUserToMutedList;
    private boolean isAddingUserToShareIdentityList;
    private boolean isBlocked;
    private boolean isFavorite;
    private boolean isFollowing;
    private boolean isLoadingUserMutedList;
    private boolean isLoadingUserNeverList;
    private boolean isLoadingUserProfile;
    private boolean isMuted;
    private boolean isRemovingUserFromBlockList;
    private boolean isRemovingUserFromMutedList;
    private boolean isShowingFailureDialog;
    private LoadUserProfileAsyncTask loadMeProfileTask;
    private LoadUserMutedListAsyncTask loadUserMutedListTask;
    private LoadUserNeverListAsyncTask loadUserNeverListTask;
    private LoadUserProfileAsyncTask loadUserProfileTask;
    protected ProfileModel model;
    private RemoveUserFromMutedListAsyncTask removeUserFromMutedListAsyncTask;
    private RemoveUserToNeverListAsyncTask removeUserToNeverListAsyncTask;
    
    static {
        TAG = ProfileScreenViewModel.class.getSimpleName();
    }
    
    public ProfileScreenViewModel(final ScreenLayout screenLayout) {
        super(screenLayout);
        this.changeFriendshipForm = new HashSet<ChangeFriendshipFormOptions>();
        this.isFollowing = false;
        this.isFavorite = false;
        this.isBlocked = false;
        this.isMuted = false;
        this.model = ProfileModel.getProfileModel(NavigationManager.getInstance().getActivityParameters().getSelectedProfile());
        this.adapter = new ProfileScreenAdapter(this);
    }
    
    private void notifyDialogAsyncTaskCompleted() {
        ((SGProjectSpecificDialogManager)DialogManager.getInstance().getManager()).notifyChangeFriendshipDialogAsyncTaskCompleted();
    }
    
    private void notifyDialogAsyncTaskFailed(final String s) {
        ((SGProjectSpecificDialogManager)DialogManager.getInstance().getManager()).notifyChangeFriendshipDialogAsyncTaskFailed(s);
    }
    
    private void onAddUseToShareIdentityListCompleted(final AsyncActionStatus asyncActionStatus) {
        this.isAddingUserToShareIdentityList = false;
        final int n = ProfileScreenViewModel$4.$SwitchMap$com$microsoft$xbox$toolkit$AsyncActionStatus[asyncActionStatus.ordinal()];
        if (n != 1 && n != 2 && n != 3) {
            if (n == 4 || n == 5) {
                this.notifyDialogAsyncTaskFailed(XboxTcuiSdk.getResources().getString(R$string.RealNameSharing_ErrorChangeRemove));
            }
        }
        else {
            this.notifyDialogAsyncTaskCompleted();
        }
        this.updateAdapter();
    }
    
    private void onAddUserToBlockListCompleted(final AsyncActionStatus asyncActionStatus) {
        this.isAddingUserToBlockList = false;
        final int n = ProfileScreenViewModel$4.$SwitchMap$com$microsoft$xbox$toolkit$AsyncActionStatus[asyncActionStatus.ordinal()];
        if (n != 1 && n != 2 && n != 3) {
            if (n == 4 || n == 5) {
                this.showError(R$string.Messages_Error_FailedToBlockUser);
            }
        }
        else {
            final ProfileModel meProfileModel = ProfileModel.getMeProfileModel();
            if (meProfileModel != null) {
                this.isBlocked = false;
                final NeverListResultContainer.NeverListResult neverListData = meProfileModel.getNeverListData();
                if (neverListData != null) {
                    this.isBlocked = neverListData.contains(this.model.getXuid());
                }
                this.isFollowing = false;
            }
        }
        this.updateAdapter();
    }
    
    private void onAddUserToFollowingListCompleted(final AsyncActionStatus asyncActionStatus, final boolean isFollowing) {
        this.isAddingUserToFollowingList = false;
        final int n = ProfileScreenViewModel$4.$SwitchMap$com$microsoft$xbox$toolkit$AsyncActionStatus[asyncActionStatus.ordinal()];
        if (n != 1 && n != 2 && n != 3) {
            if (n == 4 || n == 5) {
                Object addUserToFollowingResult = null;
                final ProfileModel meProfileModel = ProfileModel.getMeProfileModel();
                if (meProfileModel != null) {
                    addUserToFollowingResult = meProfileModel.getAddUserToFollowingResult();
                }
                String s;
                if (addUserToFollowingResult != null && !((AddFollowingUserResponseContainer.AddFollowingUserResponse)addUserToFollowingResult).getAddFollowingRequestStatus() && ((AddFollowingUserResponseContainer.AddFollowingUserResponse)addUserToFollowingResult).code == 1028) {
                    s = ((AddFollowingUserResponseContainer.AddFollowingUserResponse)addUserToFollowingResult).description;
                }
                else {
                    s = XboxTcuiSdk.getResources().getString(R$string.RealNameSharing_ErrorAddingFriend);
                }
                this.notifyDialogAsyncTaskFailed(s);
            }
        }
        else {
            this.isFollowing = isFollowing;
            XLEGlobalData.getInstance().AddForceRefresh(ProfileScreenViewModel.class);
            this.notifyDialogAsyncTaskCompleted();
        }
        this.updateAdapter();
    }
    
    private void onAddUserToMutedListCompleted(final AsyncActionStatus asyncActionStatus) {
        this.isAddingUserToMutedList = false;
        final int n = ProfileScreenViewModel$4.$SwitchMap$com$microsoft$xbox$toolkit$AsyncActionStatus[asyncActionStatus.ordinal()];
        if (n != 1 && n != 2 && n != 3) {
            if (n == 4 || n == 5) {
                this.showError(R$string.Messages_Error_FailedToMuteUser);
            }
        }
        else if (ProfileModel.getMeProfileModel() != null) {
            this.isMuted = true;
        }
        this.updateAdapter();
    }
    
    private void onRemoveUserFromBlockListCompleted(final AsyncActionStatus asyncActionStatus) {
        this.isRemovingUserFromBlockList = false;
        final int n = ProfileScreenViewModel$4.$SwitchMap$com$microsoft$xbox$toolkit$AsyncActionStatus[asyncActionStatus.ordinal()];
        if (n != 1 && n != 2 && n != 3) {
            if (n == 4 || n == 5) {
                this.showError(R$string.Messages_Error_FailedToUnblockUser);
            }
        }
        else {
            final ProfileModel meProfileModel = ProfileModel.getMeProfileModel();
            if (meProfileModel != null) {
                this.isBlocked = false;
                final NeverListResultContainer.NeverListResult neverListData = meProfileModel.getNeverListData();
                if (neverListData != null) {
                    this.isBlocked = neverListData.contains(this.model.getXuid());
                }
            }
        }
        this.updateAdapter();
    }
    
    private void onRemoveUserFromMutedListCompleted(final AsyncActionStatus asyncActionStatus) {
        this.isRemovingUserFromMutedList = false;
        final int n = ProfileScreenViewModel$4.$SwitchMap$com$microsoft$xbox$toolkit$AsyncActionStatus[asyncActionStatus.ordinal()];
        if (n != 1 && n != 2 && n != 3) {
            if (n == 4 || n == 5) {
                this.showError(R$string.Messages_Error_FailedToUnmuteUser);
            }
        }
        else if (ProfileModel.getMeProfileModel() != null) {
            this.isMuted = false;
        }
        this.updateAdapter();
    }
    
    public void addFollowingUser() {
        if (ProfileModel.hasPrivilegeToAddFriend()) {
            final AddUserToFollowingListAsyncTask addUserToFollowingListAsyncTask = this.addUserToFollowingListAsyncTask;
            if (addUserToFollowingListAsyncTask != null) {
                addUserToFollowingListAsyncTask.cancel();
            }
            (this.addUserToFollowingListAsyncTask = new AddUserToFollowingListAsyncTask(this.model.getXuid())).load(true);
            return;
        }
        this.showError(R$string.Global_MissingPrivilegeError_DialogBody);
    }
    
    public void addUserToShareIdentityList() {
        final AddUserToShareIdentityListAsyncTask addUserToShareIdentityListAsyncTask = this.addUserToShareIdentityListAsyncTask;
        if (addUserToShareIdentityListAsyncTask != null) {
            addUserToShareIdentityListAsyncTask.cancel();
        }
        final ArrayList<String> list = new ArrayList<String>();
        list.add(this.model.getXuid());
        (this.addUserToShareIdentityListAsyncTask = new AddUserToShareIdentityListAsyncTask(list)).load(true);
    }
    
    public void blockUser() {
        XLEAssert.assertTrue(Thread.currentThread() == ThreadManager.UIThread);
        this.showOkCancelDialog(XboxTcuiSdk.getResources().getString(R$string.Messages_BlockUserConfirmation_DialogTitle), XboxTcuiSdk.getResources().getString(R$string.Messages_BlockUserConfirmation_DialogBody), XboxTcuiSdk.getResources().getString(R$string.OK_Text), new Runnable() {
            @Override
            public void run() {
                ProfileScreenViewModel.this.blockUserInternal();
            }
        }, XboxTcuiSdk.getResources().getString(R$string.MessageDialog_Cancel), null);
        this.updateAdapter();
    }
    
    public void blockUserInternal() {
        UTCPeopleHub.trackBlockDialogComplete();
        final AddUserToNeverListAsyncTask addUserToNeverListAsyncTask = this.addUserToNeverListAsyncTask;
        if (addUserToNeverListAsyncTask != null) {
            addUserToNeverListAsyncTask.cancel();
        }
        (this.addUserToNeverListAsyncTask = new AddUserToNeverListAsyncTask(this.model.getXuid())).load(true);
    }
    
    public String getGamerPicUrl() {
        return this.model.getGamerPicImageUrl();
    }
    
    public String getGamerScore() {
        return this.model.getGamerScore();
    }
    
    public String getGamerTag() {
        return this.model.getGamerTag();
    }
    
    public boolean getIsAddingUserToBlockList() {
        return this.isAddingUserToBlockList;
    }
    
    public boolean getIsAddingUserToMutedList() {
        return this.isAddingUserToMutedList;
    }
    
    public boolean getIsBlocked() {
        return this.isBlocked;
    }
    
    public boolean getIsFavorite() {
        return this.isFavorite;
    }
    
    public boolean getIsMuted() {
        return this.isMuted;
    }
    
    public boolean getIsRemovingUserFromBlockList() {
        return this.isRemovingUserFromBlockList;
    }
    
    public boolean getIsRemovingUserFromMutedList() {
        return this.isRemovingUserFromMutedList;
    }
    
    public int getPreferredColor() {
        return this.model.getPreferedColor();
    }
    
    public String getRealName() {
        return this.model.getRealName();
    }
    
    public String getXuid() {
        return this.model.getXuid();
    }
    
    @Override
    public boolean isBusy() {
        return this.isLoadingUserProfile || this.isLoadingUserNeverList || this.isLoadingUserMutedList || this.isAddingUserToFollowingList || this.isAddingUserToShareIdentityList || this.isRemovingUserFromBlockList || this.isAddingUserToBlockList || this.isAddingUserToMutedList || this.isRemovingUserFromMutedList;
    }
    
    public boolean isCallerFollowingTarget() {
        return this.isFollowing;
    }
    
    public boolean isFacebookFriend() {
        return false;
    }
    
    public boolean isMeProfile() {
        return this.model.isMeProfile();
    }
    
    public void launchXboxApp() {
        XLEAssert.assertTrue(Thread.currentThread() == ThreadManager.UIThread);
        this.showOkCancelDialog(XboxTcuiSdk.getResources().getString(R$string.Messages_BlockUserConfirmation_DialogTitle), XboxTcuiSdk.getResources().getString(R$string.Messages_ViewInXboxApp_DialogBody), XboxTcuiSdk.getResources().getString(R$string.ConnectDialog_ContinueAsGuest), new Runnable() {
            @Override
            public void run() {
                UTCPeopleHub.trackViewInXboxAppDialogComplete();
                XboxAppDeepLinker.showUserProfile((Context)XboxTcuiSdk.getActivity(), ProfileScreenViewModel.this.model.getXuid());
            }
        }, XboxTcuiSdk.getResources().getString(R$string.MessageDialog_Cancel), null);
        this.updateAdapter();
    }
    
    @Override
    public void load(final boolean b) {
        final LoadUserProfileAsyncTask loadUserProfileTask = this.loadUserProfileTask;
        if (loadUserProfileTask != null) {
            loadUserProfileTask.cancel();
        }
        (this.loadMeProfileTask = new LoadUserProfileAsyncTask(ProfileModel.getMeProfileModel())).load(true);
        if (!this.isMeProfile()) {
            final LoadUserNeverListAsyncTask loadUserNeverListTask = this.loadUserNeverListTask;
            if (loadUserNeverListTask != null) {
                loadUserNeverListTask.cancel();
            }
            (this.loadUserNeverListTask = new LoadUserNeverListAsyncTask(ProfileModel.getMeProfileModel())).load(true);
            final LoadUserMutedListAsyncTask loadUserMutedListTask = this.loadUserMutedListTask;
            if (loadUserMutedListTask != null) {
                loadUserMutedListTask.cancel();
            }
            (this.loadUserMutedListTask = new LoadUserMutedListAsyncTask(ProfileModel.getMeProfileModel())).load(true);
            (this.loadUserProfileTask = new LoadUserProfileAsyncTask(this.model)).load(true);
        }
    }
    
    public void muteUser() {
        final AddUserToMutedListAsyncTask addUserToMutedListAsyncTask = this.addUserToMutedListAsyncTask;
        if (addUserToMutedListAsyncTask != null) {
            addUserToMutedListAsyncTask.cancel();
        }
        (this.addUserToMutedListAsyncTask = new AddUserToMutedListAsyncTask(this.model.getXuid())).load(true);
    }
    
    public void navigateToChangeRelationship() {
        if (ProfileModel.hasPrivilegeToAddFriend()) {
            UTCChangeRelationship.trackChangeRelationshipAction(this.getScreen().getName(), this.getXuid(), this.isCallerFollowingTarget(), this.isFacebookFriend());
            this.showChangeFriendshipDialog();
            return;
        }
        this.showError(R$string.Global_MissingPrivilegeError_DialogBody);
    }
    
    public void onLoadUserMutedListCompleted(final AsyncActionStatus asyncActionStatus) {
        this.isLoadingUserMutedList = false;
        final int n = ProfileScreenViewModel$4.$SwitchMap$com$microsoft$xbox$toolkit$AsyncActionStatus[asyncActionStatus.ordinal()];
        if (n == 1 || n == 2 || n == 3) {
            final ProfileModel meProfileModel = ProfileModel.getMeProfileModel();
            if (!this.isMeProfile() && meProfileModel != null) {
                this.isMuted = false;
                final MutedListResultContainer.MutedListResult mutedList = meProfileModel.getMutedList();
                if (mutedList != null) {
                    this.isMuted = mutedList.contains(this.model.getXuid());
                }
            }
        }
        this.updateAdapter();
    }
    
    public void onLoadUserNeverListCompleted(final AsyncActionStatus asyncActionStatus) {
        this.isLoadingUserNeverList = false;
        final int n = ProfileScreenViewModel$4.$SwitchMap$com$microsoft$xbox$toolkit$AsyncActionStatus[asyncActionStatus.ordinal()];
        if (n == 1 || n == 2 || n == 3) {
            final ProfileModel meProfileModel = ProfileModel.getMeProfileModel();
            if (!this.isMeProfile() && meProfileModel != null) {
                this.isBlocked = false;
                final NeverListResultContainer.NeverListResult neverListData = meProfileModel.getNeverListData();
                if (neverListData != null) {
                    this.isBlocked = neverListData.contains(this.model.getXuid());
                }
            }
        }
        this.updateAdapter();
    }
    
    public void onLoadUserProfileCompleted(final AsyncActionStatus asyncActionStatus) {
        this.isLoadingUserProfile = false;
        final int n = ProfileScreenViewModel$4.$SwitchMap$com$microsoft$xbox$toolkit$AsyncActionStatus[asyncActionStatus.ordinal()];
        if (n != 1 && n != 2 && n != 3) {
            if (n == 4 || n == 5) {
                if (!this.isShowingFailureDialog) {
                    this.isShowingFailureDialog = true;
                    final AlertDialog$Builder alertDialog$Builder = new AlertDialog$Builder((Context)XboxTcuiSdk.getActivity());
                    alertDialog$Builder.setMessage(R$string.Service_ErrorText);
                    alertDialog$Builder.setCancelable(false);
                    alertDialog$Builder.setPositiveButton(R$string.OK_Text, (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
                        public void onClick(final DialogInterface dialogInterface, final int n) {
                            try {
                                NavigationManager.getInstance().PopAllScreens();
                            }
                            catch (XLEException ex) {}
                        }
                    });
                    alertDialog$Builder.create().show();
                }
            }
        }
        else if (!this.isMeProfile() && ProfileModel.getMeProfileModel() != null) {
            this.isFollowing = this.model.isCallerFollowingTarget();
        }
        this.updateAdapter();
    }
    
    @Override
    public void onRehydrate() {
        this.adapter = new ProfileScreenAdapter(this);
    }
    
    @Override
    protected void onStartOverride() {
        this.isShowingFailureDialog = false;
    }
    
    @Override
    protected void onStopOverride() {
        final LoadUserProfileAsyncTask loadMeProfileTask = this.loadMeProfileTask;
        if (loadMeProfileTask != null) {
            loadMeProfileTask.cancel();
        }
        final LoadUserNeverListAsyncTask loadUserNeverListTask = this.loadUserNeverListTask;
        if (loadUserNeverListTask != null) {
            loadUserNeverListTask.cancel();
        }
        final LoadUserMutedListAsyncTask loadUserMutedListTask = this.loadUserMutedListTask;
        if (loadUserMutedListTask != null) {
            loadUserMutedListTask.cancel();
        }
        final LoadUserProfileAsyncTask loadUserProfileTask = this.loadUserProfileTask;
        if (loadUserProfileTask != null) {
            loadUserProfileTask.cancel();
        }
        final AddUserToFollowingListAsyncTask addUserToFollowingListAsyncTask = this.addUserToFollowingListAsyncTask;
        if (addUserToFollowingListAsyncTask != null) {
            addUserToFollowingListAsyncTask.cancel();
        }
        final AddUserToShareIdentityListAsyncTask addUserToShareIdentityListAsyncTask = this.addUserToShareIdentityListAsyncTask;
        if (addUserToShareIdentityListAsyncTask != null) {
            addUserToShareIdentityListAsyncTask.cancel();
        }
        final AddUserToNeverListAsyncTask addUserToNeverListAsyncTask = this.addUserToNeverListAsyncTask;
        if (addUserToNeverListAsyncTask != null) {
            addUserToNeverListAsyncTask.cancel();
        }
        final RemoveUserToNeverListAsyncTask removeUserToNeverListAsyncTask = this.removeUserToNeverListAsyncTask;
        if (removeUserToNeverListAsyncTask != null) {
            removeUserToNeverListAsyncTask.cancel();
        }
        final AddUserToMutedListAsyncTask addUserToMutedListAsyncTask = this.addUserToMutedListAsyncTask;
        if (addUserToMutedListAsyncTask != null) {
            addUserToMutedListAsyncTask.cancel();
        }
        final RemoveUserFromMutedListAsyncTask removeUserFromMutedListAsyncTask = this.removeUserFromMutedListAsyncTask;
        if (removeUserFromMutedListAsyncTask != null) {
            removeUserFromMutedListAsyncTask.cancel();
        }
    }
    
    public void showChangeFriendshipDialog() {
        if (this.changeFriendshipDialogViewModel == null) {
            this.changeFriendshipDialogViewModel = new ChangeFriendshipDialogViewModel(this.model);
        }
        ((SGProjectSpecificDialogManager)DialogManager.getInstance().getManager()).showChangeFriendshipDialog(this.changeFriendshipDialogViewModel, this);
    }
    
    public void showReportDialog() {
        try {
            NavigationManager.getInstance().PopScreensAndReplace(0, ReportUserScreen.class, false, false, false, NavigationManager.getInstance().getActivityParameters());
        }
        catch (XLEException ex) {}
    }
    
    public void unblockUser() {
        final RemoveUserToNeverListAsyncTask removeUserToNeverListAsyncTask = this.removeUserToNeverListAsyncTask;
        if (removeUserToNeverListAsyncTask != null) {
            removeUserToNeverListAsyncTask.cancel();
        }
        (this.removeUserToNeverListAsyncTask = new RemoveUserToNeverListAsyncTask(this.model.getXuid())).load(true);
    }
    
    public void unmuteUser() {
        final RemoveUserFromMutedListAsyncTask removeUserFromMutedListAsyncTask = this.removeUserFromMutedListAsyncTask;
        if (removeUserFromMutedListAsyncTask != null) {
            removeUserFromMutedListAsyncTask.cancel();
        }
        (this.removeUserFromMutedListAsyncTask = new RemoveUserFromMutedListAsyncTask(this.model.getXuid())).load(true);
    }
    
    private class AddUserToFollowingListAsyncTask extends NetworkAsyncTask<AsyncActionStatus>
    {
        private String followingUserXuid;
        private boolean isFollowingUser;
        
        public AddUserToFollowingListAsyncTask(final String followingUserXuid) {
            this.isFollowingUser = false;
            this.followingUserXuid = followingUserXuid;
        }
        
        @Override
        protected boolean checkShouldExecute() {
            XLEAssert.assertIsUIThread();
            return true;
        }
        
        @Override
        protected AsyncActionStatus loadDataInBackground() {
            final ProfileModel meProfileModel = ProfileModel.getMeProfileModel();
            if (meProfileModel != null) {
                final AsyncActionStatus status = meProfileModel.addUserToFollowingList(this.forceLoad, this.followingUserXuid).getStatus();
                if (!AsyncActionStatus.getIsFail(status)) {
                    final AddFollowingUserResponseContainer.AddFollowingUserResponse addUserToFollowingResult = meProfileModel.getAddUserToFollowingResult();
                    if (addUserToFollowingResult != null && !addUserToFollowingResult.getAddFollowingRequestStatus() && addUserToFollowingResult.code == 1028) {
                        return AsyncActionStatus.FAIL;
                    }
                    ProfileScreenViewModel.this.model.loadProfileSummary(true);
                    meProfileModel.loadProfileSummary(true);
                    final ArrayList<FollowersData> followingData = meProfileModel.getFollowingData();
                    if (followingData != null) {
                        final Iterator<FollowersData> iterator = followingData.iterator();
                        while (iterator.hasNext()) {
                            if (iterator.next().xuid.equals(this.followingUserXuid)) {
                                this.isFollowingUser = true;
                                break;
                            }
                        }
                    }
                }
                return status;
            }
            return AsyncActionStatus.FAIL;
        }
        
        @Override
        protected AsyncActionStatus onError() {
            return AsyncActionStatus.FAIL;
        }
        
        @Override
        protected void onNoAction() {
            XLEAssert.assertIsUIThread();
            ProfileScreenViewModel.this.onAddUserToFollowingListCompleted(AsyncActionStatus.NO_CHANGE, this.isFollowingUser);
        }
        
        @Override
        protected void onPostExecute(final AsyncActionStatus asyncActionStatus) {
            ProfileScreenViewModel.this.onAddUserToFollowingListCompleted(asyncActionStatus, this.isFollowingUser);
        }
        
        @Override
        protected void onPreExecute() {
            XLEAssert.assertIsUIThread();
            ProfileScreenViewModel.this.isAddingUserToFollowingList = true;
            ViewModelBase.this.updateAdapter();
        }
    }
    
    private class AddUserToMutedListAsyncTask extends NetworkAsyncTask<AsyncActionStatus>
    {
        private String mutedUserXuid;
        
        public AddUserToMutedListAsyncTask(final String mutedUserXuid) {
            this.mutedUserXuid = mutedUserXuid;
        }
        
        @Override
        protected boolean checkShouldExecute() {
            XLEAssert.assertIsUIThread();
            return true;
        }
        
        @Override
        protected AsyncActionStatus loadDataInBackground() {
            final ProfileModel meProfileModel = ProfileModel.getMeProfileModel();
            if (meProfileModel != null) {
                return meProfileModel.addUserToMutedList(this.forceLoad, this.mutedUserXuid).getStatus();
            }
            return AsyncActionStatus.FAIL;
        }
        
        @Override
        protected AsyncActionStatus onError() {
            return AsyncActionStatus.FAIL;
        }
        
        @Override
        protected void onNoAction() {
            XLEAssert.assertIsUIThread();
            ProfileScreenViewModel.this.onAddUserToMutedListCompleted(AsyncActionStatus.NO_CHANGE);
        }
        
        @Override
        protected void onPostExecute(final AsyncActionStatus asyncActionStatus) {
            ProfileScreenViewModel.this.onAddUserToMutedListCompleted(asyncActionStatus);
        }
        
        @Override
        protected void onPreExecute() {
            XLEAssert.assertIsUIThread();
            ProfileScreenViewModel.this.isAddingUserToMutedList = true;
            ViewModelBase.this.updateAdapter();
        }
    }
    
    private class AddUserToNeverListAsyncTask extends NetworkAsyncTask<AsyncActionStatus>
    {
        private String blockUserXuid;
        
        public AddUserToNeverListAsyncTask(final String blockUserXuid) {
            this.blockUserXuid = blockUserXuid;
        }
        
        @Override
        protected boolean checkShouldExecute() {
            XLEAssert.assertIsUIThread();
            return true;
        }
        
        @Override
        protected AsyncActionStatus loadDataInBackground() {
            final ProfileModel meProfileModel = ProfileModel.getMeProfileModel();
            if (meProfileModel != null) {
                return meProfileModel.addUserToNeverList(this.forceLoad, this.blockUserXuid).getStatus();
            }
            return AsyncActionStatus.FAIL;
        }
        
        @Override
        protected AsyncActionStatus onError() {
            return AsyncActionStatus.FAIL;
        }
        
        @Override
        protected void onNoAction() {
            XLEAssert.assertIsUIThread();
            ProfileScreenViewModel.this.onAddUserToBlockListCompleted(AsyncActionStatus.NO_CHANGE);
        }
        
        @Override
        protected void onPostExecute(final AsyncActionStatus asyncActionStatus) {
            ProfileScreenViewModel.this.onAddUserToBlockListCompleted(asyncActionStatus);
        }
        
        @Override
        protected void onPreExecute() {
            XLEAssert.assertIsUIThread();
            ProfileScreenViewModel.this.isAddingUserToBlockList = true;
            ViewModelBase.this.updateAdapter();
        }
    }
    
    private class AddUserToShareIdentityListAsyncTask extends NetworkAsyncTask<AsyncActionStatus>
    {
        private ArrayList<String> usersToAdd;
        
        public AddUserToShareIdentityListAsyncTask(final ArrayList<String> usersToAdd) {
            this.usersToAdd = usersToAdd;
        }
        
        @Override
        protected boolean checkShouldExecute() {
            return true;
        }
        
        @Override
        protected AsyncActionStatus loadDataInBackground() {
            final ProfileModel meProfileModel = ProfileModel.getMeProfileModel();
            if (meProfileModel != null) {
                return meProfileModel.addUserToShareIdentity(this.forceLoad, this.usersToAdd).getStatus();
            }
            return AsyncActionStatus.FAIL;
        }
        
        @Override
        protected AsyncActionStatus onError() {
            return AsyncActionStatus.FAIL;
        }
        
        @Override
        protected void onNoAction() {
        }
        
        @Override
        protected void onPostExecute(final AsyncActionStatus asyncActionStatus) {
            ProfileScreenViewModel.this.onAddUseToShareIdentityListCompleted(asyncActionStatus);
        }
        
        @Override
        protected void onPreExecute() {
            XLEAssert.assertIsUIThread();
            ProfileScreenViewModel.this.isAddingUserToShareIdentityList = true;
            ViewModelBase.this.updateAdapter();
        }
    }
    
    public enum ChangeFriendshipFormOptions
    {
        ShouldAddUserToFavoriteList, 
        ShouldAddUserToFriendList, 
        ShouldAddUserToShareIdentityList, 
        ShouldRemoveUserFromFavoriteList, 
        ShouldRemoveUserFromFriendList, 
        ShouldRemoveUserFromShareIdentityList;
    }
    
    private class LoadUserMutedListAsyncTask extends NetworkAsyncTask<AsyncActionStatus>
    {
        private ProfileModel model;
        
        private LoadUserMutedListAsyncTask(final ProfileModel model) {
            this.model = model;
        }
        
        @Override
        protected boolean checkShouldExecute() {
            XLEAssert.assertIsUIThread();
            return this.model.shouldRefresh();
        }
        
        @Override
        protected AsyncActionStatus loadDataInBackground() {
            XLEAssert.assertNotNull(this.model);
            final AsyncActionStatus status = this.model.loadSync(this.forceLoad).getStatus();
            if (status != AsyncActionStatus.SUCCESS && status != AsyncActionStatus.NO_CHANGE && status != AsyncActionStatus.NO_OP_SUCCESS) {
                return status;
            }
            return this.model.loadUserMutedList(true).getStatus();
        }
        
        @Override
        protected AsyncActionStatus onError() {
            return AsyncActionStatus.FAIL;
        }
        
        @Override
        protected void onNoAction() {
            XLEAssert.assertIsUIThread();
            ProfileScreenViewModel.this.onLoadUserProfileCompleted(AsyncActionStatus.NO_CHANGE);
        }
        
        @Override
        protected void onPostExecute(final AsyncActionStatus asyncActionStatus) {
            ProfileScreenViewModel.this.onLoadUserMutedListCompleted(asyncActionStatus);
        }
        
        @Override
        protected void onPreExecute() {
            XLEAssert.assertIsUIThread();
            ProfileScreenViewModel.this.isLoadingUserMutedList = true;
            ViewModelBase.this.updateAdapter();
        }
    }
    
    private class LoadUserNeverListAsyncTask extends NetworkAsyncTask<AsyncActionStatus>
    {
        private ProfileModel model;
        
        private LoadUserNeverListAsyncTask(final ProfileModel model) {
            this.model = model;
        }
        
        @Override
        protected boolean checkShouldExecute() {
            XLEAssert.assertIsUIThread();
            return this.model.shouldRefresh();
        }
        
        @Override
        protected AsyncActionStatus loadDataInBackground() {
            XLEAssert.assertNotNull(this.model);
            final AsyncActionStatus status = this.model.loadSync(this.forceLoad).getStatus();
            if (status != AsyncActionStatus.SUCCESS && status != AsyncActionStatus.NO_CHANGE && status != AsyncActionStatus.NO_OP_SUCCESS) {
                return status;
            }
            return this.model.loadUserNeverList(true).getStatus();
        }
        
        @Override
        protected AsyncActionStatus onError() {
            return AsyncActionStatus.FAIL;
        }
        
        @Override
        protected void onNoAction() {
            XLEAssert.assertIsUIThread();
            ProfileScreenViewModel.this.onLoadUserProfileCompleted(AsyncActionStatus.NO_CHANGE);
        }
        
        @Override
        protected void onPostExecute(final AsyncActionStatus asyncActionStatus) {
            ProfileScreenViewModel.this.onLoadUserNeverListCompleted(asyncActionStatus);
        }
        
        @Override
        protected void onPreExecute() {
            XLEAssert.assertIsUIThread();
            ProfileScreenViewModel.this.isLoadingUserNeverList = true;
            ViewModelBase.this.updateAdapter();
        }
    }
    
    private class LoadUserProfileAsyncTask extends NetworkAsyncTask<AsyncActionStatus>
    {
        private ProfileModel model;
        
        private LoadUserProfileAsyncTask(final ProfileModel model) {
            this.model = model;
        }
        
        @Override
        protected boolean checkShouldExecute() {
            XLEAssert.assertIsUIThread();
            return this.model.shouldRefresh() || this.model.shouldRefreshProfileSummary();
        }
        
        @Override
        protected AsyncActionStatus loadDataInBackground() {
            XLEAssert.assertNotNull(this.model);
            final AsyncActionStatus status = this.model.loadSync(this.forceLoad).getStatus();
            if (status != AsyncActionStatus.SUCCESS && status != AsyncActionStatus.NO_CHANGE && status != AsyncActionStatus.NO_OP_SUCCESS) {
                return status;
            }
            return this.model.loadProfileSummary(this.forceLoad).getStatus();
        }
        
        @Override
        protected AsyncActionStatus onError() {
            return AsyncActionStatus.FAIL;
        }
        
        @Override
        protected void onNoAction() {
            XLEAssert.assertIsUIThread();
            ProfileScreenViewModel.this.onLoadUserProfileCompleted(AsyncActionStatus.NO_CHANGE);
        }
        
        @Override
        protected void onPostExecute(final AsyncActionStatus asyncActionStatus) {
            ProfileScreenViewModel.this.onLoadUserProfileCompleted(asyncActionStatus);
        }
        
        @Override
        protected void onPreExecute() {
            XLEAssert.assertIsUIThread();
            ProfileScreenViewModel.this.isLoadingUserProfile = true;
            ViewModelBase.this.updateAdapter();
        }
    }
    
    private class RemoveUserFromMutedListAsyncTask extends NetworkAsyncTask<AsyncActionStatus>
    {
        private String mutedUserXuid;
        
        public RemoveUserFromMutedListAsyncTask(final String mutedUserXuid) {
            this.mutedUserXuid = mutedUserXuid;
        }
        
        @Override
        protected boolean checkShouldExecute() {
            XLEAssert.assertIsUIThread();
            return true;
        }
        
        @Override
        protected AsyncActionStatus loadDataInBackground() {
            final ProfileModel meProfileModel = ProfileModel.getMeProfileModel();
            if (meProfileModel != null) {
                return meProfileModel.removeUserFromMutedList(this.forceLoad, this.mutedUserXuid).getStatus();
            }
            return AsyncActionStatus.FAIL;
        }
        
        @Override
        protected AsyncActionStatus onError() {
            return AsyncActionStatus.FAIL;
        }
        
        @Override
        protected void onNoAction() {
            XLEAssert.assertIsUIThread();
            ProfileScreenViewModel.this.onRemoveUserFromMutedListCompleted(AsyncActionStatus.NO_CHANGE);
        }
        
        @Override
        protected void onPostExecute(final AsyncActionStatus asyncActionStatus) {
            ProfileScreenViewModel.this.onRemoveUserFromMutedListCompleted(asyncActionStatus);
        }
        
        @Override
        protected void onPreExecute() {
            XLEAssert.assertIsUIThread();
            ProfileScreenViewModel.this.isRemovingUserFromMutedList = true;
            ViewModelBase.this.updateAdapter();
        }
    }
    
    private class RemoveUserToNeverListAsyncTask extends NetworkAsyncTask<AsyncActionStatus>
    {
        private String unblockUserXuid;
        
        public RemoveUserToNeverListAsyncTask(final String unblockUserXuid) {
            this.unblockUserXuid = unblockUserXuid;
        }
        
        @Override
        protected boolean checkShouldExecute() {
            XLEAssert.assertIsUIThread();
            return true;
        }
        
        @Override
        protected AsyncActionStatus loadDataInBackground() {
            final ProfileModel meProfileModel = ProfileModel.getMeProfileModel();
            if (meProfileModel != null) {
                return meProfileModel.removeUserFromNeverList(this.forceLoad, this.unblockUserXuid).getStatus();
            }
            return AsyncActionStatus.FAIL;
        }
        
        @Override
        protected AsyncActionStatus onError() {
            return AsyncActionStatus.FAIL;
        }
        
        @Override
        protected void onNoAction() {
            XLEAssert.assertIsUIThread();
            ProfileScreenViewModel.this.onRemoveUserFromBlockListCompleted(AsyncActionStatus.NO_CHANGE);
        }
        
        @Override
        protected void onPostExecute(final AsyncActionStatus asyncActionStatus) {
            ProfileScreenViewModel.this.onRemoveUserFromBlockListCompleted(asyncActionStatus);
        }
        
        @Override
        protected void onPreExecute() {
            XLEAssert.assertIsUIThread();
            ProfileScreenViewModel.this.isRemovingUserFromBlockList = true;
            ViewModelBase.this.updateAdapter();
        }
    }
}
