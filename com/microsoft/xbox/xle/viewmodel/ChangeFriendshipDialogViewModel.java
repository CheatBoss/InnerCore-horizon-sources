package com.microsoft.xbox.xle.viewmodel;

import com.microsoft.xbox.xle.app.activity.Profile.*;
import com.microsoft.xbox.toolkit.network.*;
import com.microsoft.xbox.xle.app.*;
import com.microsoft.xboxtcui.*;
import android.content.res.*;
import com.microsoft.xbox.telemetry.helpers.*;
import com.microsoft.xbox.toolkit.*;
import com.microsoft.xbox.service.model.*;
import java.util.*;
import com.microsoft.xbox.service.network.managers.*;

public class ChangeFriendshipDialogViewModel
{
    private static final String TAG;
    private AddUserToFavoriteListAsyncTask addUserToFavoriteListAsyncTask;
    private AddUserToFollowingListAsyncTask addUserToFollowingListAsyncTask;
    private AddUserToShareIdentityListAsyncTask addUserToShareIdentityListAsyncTask;
    private HashSet<ProfileScreenViewModel.ChangeFriendshipFormOptions> changeFriendshipForm;
    private boolean isAddingUserToFavoriteList;
    private boolean isAddingUserToFollowingList;
    private boolean isAddingUserToShareIdentityList;
    private boolean isFavorite;
    private boolean isFollowing;
    private boolean isLoadingUserProfile;
    private boolean isRemovingUserFromFavoriteList;
    private boolean isRemovingUserFromFollowingList;
    private boolean isRemovingUserFromShareIdentityList;
    private boolean isSharingRealNameEnd;
    private boolean isSharingRealNameStart;
    private LoadPersonDataAsyncTask loadProfileAsyncTask;
    private ProfileModel model;
    private RemoveUserFromFavoriteListAsyncTask removeUserFromFavoriteListAsyncTask;
    private RemoveUserFromFollowingListAsyncTask removeUserFromFollowingListAsyncTask;
    private RemoveUserFromShareIdentityListAsyncTask removeUserFromShareIdentityListAsyncTask;
    private ListState viewModelState;
    
    static {
        TAG = ChangeFriendshipDialogViewModel.class.getSimpleName();
    }
    
    public ChangeFriendshipDialogViewModel(final ProfileModel model) {
        this.changeFriendshipForm = new HashSet<ProfileScreenViewModel.ChangeFriendshipFormOptions>();
        this.isFollowing = false;
        this.isFavorite = false;
        this.viewModelState = ListState.LoadingState;
        XLEAssert.assertTrue(ProfileModel.isMeXuid(model.getXuid()) ^ true);
        this.model = model;
    }
    
    private void notifyDialogAsyncTaskCompleted() {
        ((SGProjectSpecificDialogManager)DialogManager.getInstance().getManager()).notifyChangeFriendshipDialogAsyncTaskCompleted();
    }
    
    private void notifyDialogAsyncTaskFailed(final String s) {
        ((SGProjectSpecificDialogManager)DialogManager.getInstance().getManager()).notifyChangeFriendshipDialogAsyncTaskFailed(s);
    }
    
    private void notifyDialogUpdateView() {
        ((SGProjectSpecificDialogManager)DialogManager.getInstance().getManager()).notifyChangeFriendshipDialogUpdateView();
    }
    
    private void onAddUseToShareIdentityListCompleted(final AsyncActionStatus asyncActionStatus) {
        this.isAddingUserToShareIdentityList = false;
        final int n = ChangeFriendshipDialogViewModel$1.$SwitchMap$com$microsoft$xbox$toolkit$AsyncActionStatus[asyncActionStatus.ordinal()];
        if (n == 1 || n == 2 || n == 3) {
            this.notifyDialogAsyncTaskCompleted();
            return;
        }
        if (n != 4 && n != 5) {
            return;
        }
        this.notifyDialogAsyncTaskFailed(XboxTcuiSdk.getResources().getString(R$string.RealNameSharing_ErrorChangeRemove));
    }
    
    private void onAddUserToFavoriteListCompleted(final AsyncActionStatus asyncActionStatus, final boolean isFavorite) {
        this.isAddingUserToFavoriteList = false;
        final int n = ChangeFriendshipDialogViewModel$1.$SwitchMap$com$microsoft$xbox$toolkit$AsyncActionStatus[asyncActionStatus.ordinal()];
        if (n == 1 || n == 2 || n == 3) {
            this.isFavorite = isFavorite;
            this.notifyDialogAsyncTaskCompleted();
            return;
        }
        if (n != 4 && n != 5) {
            return;
        }
        this.notifyDialogAsyncTaskFailed(XboxTcuiSdk.getResources().getString(R$string.RealNameSharing_ErrorChangeRemove));
    }
    
    private void onAddUserToFollowingListCompleted(final AsyncActionStatus asyncActionStatus, final boolean isFollowing) {
        this.isAddingUserToFollowingList = false;
        final int n = ChangeFriendshipDialogViewModel$1.$SwitchMap$com$microsoft$xbox$toolkit$AsyncActionStatus[asyncActionStatus.ordinal()];
        if (n == 1 || n == 2 || n == 3) {
            this.isFollowing = isFollowing;
            this.notifyDialogAsyncTaskCompleted();
            return;
        }
        if (n != 4 && n != 5) {
            return;
        }
        Object addUserToFollowingResult = null;
        final ProfileModel meProfileModel = ProfileModel.getMeProfileModel();
        if (meProfileModel != null) {
            addUserToFollowingResult = meProfileModel.getAddUserToFollowingResult();
        }
        if (addUserToFollowingResult != null && !((AddFollowingUserResponseContainer.AddFollowingUserResponse)addUserToFollowingResult).getAddFollowingRequestStatus() && ((AddFollowingUserResponseContainer.AddFollowingUserResponse)addUserToFollowingResult).code == 1028) {
            this.notifyDialogAsyncTaskFailed(((AddFollowingUserResponseContainer.AddFollowingUserResponse)addUserToFollowingResult).description);
            return;
        }
        this.notifyDialogAsyncTaskFailed(XboxTcuiSdk.getResources().getString(R$string.RealNameSharing_ErrorAddingFriend));
    }
    
    private void onLoadPersonDataCompleted(final AsyncActionStatus asyncActionStatus) {
        this.isLoadingUserProfile = false;
        final int n = ChangeFriendshipDialogViewModel$1.$SwitchMap$com$microsoft$xbox$toolkit$AsyncActionStatus[asyncActionStatus.ordinal()];
        Label_0071: {
            while (true) {
                Label_0064: {
                    ListState viewModelState;
                    if (n != 1 && n != 2 && n != 3) {
                        if (n != 4 && n != 5) {
                            break Label_0071;
                        }
                        break Label_0064;
                    }
                    else {
                        if (this.model.getProfileSummaryData() == null) {
                            break Label_0064;
                        }
                        viewModelState = ListState.ValidContentState;
                    }
                    this.viewModelState = viewModelState;
                    break Label_0071;
                }
                ListState viewModelState = ListState.ErrorState;
                continue;
            }
        }
        this.notifyDialogUpdateView();
    }
    
    private void onRemoveUserFromFavoriteListCompleted(final AsyncActionStatus asyncActionStatus, final boolean isFavorite) {
        this.isRemovingUserFromFavoriteList = false;
        final int n = ChangeFriendshipDialogViewModel$1.$SwitchMap$com$microsoft$xbox$toolkit$AsyncActionStatus[asyncActionStatus.ordinal()];
        if (n == 1 || n == 2 || n == 3) {
            this.isFavorite = isFavorite;
            this.notifyDialogAsyncTaskCompleted();
            return;
        }
        if (n != 4 && n != 5) {
            return;
        }
        this.notifyDialogAsyncTaskFailed(XboxTcuiSdk.getResources().getString(R$string.RealNameSharing_ErrorChangeRemove));
    }
    
    private void onRemoveUserFromFollowingListCompleted(final AsyncActionStatus asyncActionStatus, final boolean isFollowing) {
        this.isRemovingUserFromFollowingList = false;
        final int n = ChangeFriendshipDialogViewModel$1.$SwitchMap$com$microsoft$xbox$toolkit$AsyncActionStatus[asyncActionStatus.ordinal()];
        if (n == 1 || n == 2 || n == 3) {
            this.isFollowing = isFollowing;
            if (this.isFavorite && !isFollowing) {
                this.isFavorite = false;
            }
            this.notifyDialogAsyncTaskCompleted();
            return;
        }
        if (n != 4 && n != 5) {
            return;
        }
        this.notifyDialogAsyncTaskFailed(XboxTcuiSdk.getResources().getString(R$string.RealNameSharing_ErrorChangeRemove));
    }
    
    private void onRemoveUserFromShareIdentityListCompleted(final AsyncActionStatus asyncActionStatus) {
        this.isRemovingUserFromShareIdentityList = false;
        final int n = ChangeFriendshipDialogViewModel$1.$SwitchMap$com$microsoft$xbox$toolkit$AsyncActionStatus[asyncActionStatus.ordinal()];
        if (n == 1 || n == 2 || n == 3) {
            this.notifyDialogAsyncTaskCompleted();
            return;
        }
        if (n != 4 && n != 5) {
            return;
        }
        this.notifyDialogAsyncTaskFailed(XboxTcuiSdk.getResources().getString(R$string.RealNameSharing_ErrorChangeRemove));
    }
    
    private void showError(final int n) {
        DialogManager.getInstance().showToast(n);
    }
    
    public void addFavoriteUser() {
        final AddUserToFavoriteListAsyncTask addUserToFavoriteListAsyncTask = this.addUserToFavoriteListAsyncTask;
        if (addUserToFavoriteListAsyncTask != null) {
            addUserToFavoriteListAsyncTask.cancel();
        }
        (this.addUserToFavoriteListAsyncTask = new AddUserToFavoriteListAsyncTask(this.model.getXuid())).load(true);
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
    
    public void clearChangeFriendshipForm() {
        this.changeFriendshipForm.clear();
    }
    
    public String getCallerGamerTag() {
        final ProfileModel meProfileModel = ProfileModel.getMeProfileModel();
        if (meProfileModel != null) {
            return meProfileModel.getGamerTag();
        }
        return "";
    }
    
    public boolean getCallerMarkedTargetAsIdentityShared() {
        return this.model.hasCallerMarkedTargetAsIdentityShared();
    }
    
    public String getCallerShareRealNameStatus() {
        final ProfileModel meProfileModel = ProfileModel.getMeProfileModel();
        if (meProfileModel != null) {
            return meProfileModel.getShareRealNameStatus();
        }
        return "";
    }
    
    public String getDialogButtonText() {
        Resources resources;
        int n;
        if (this.isFollowing) {
            resources = XboxTcuiSdk.getResources();
            n = R$string.TextInput_Confirm;
        }
        else {
            resources = XboxTcuiSdk.getResources();
            n = R$string.OK_Text;
        }
        return resources.getString(n);
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
    
    public boolean getIsFavorite() {
        return this.model.hasCallerMarkedTargetAsFavorite();
    }
    
    public boolean getIsFollowing() {
        return this.model.isCallerFollowingTarget();
    }
    
    public boolean getIsSharingRealNameEnd() {
        return this.isSharingRealNameEnd;
    }
    
    public boolean getIsSharingRealNameStart() {
        return this.isSharingRealNameStart;
    }
    
    public int getPreferredColor() {
        return this.model.getPreferedColor();
    }
    
    public String getRealName() {
        return this.model.getRealName();
    }
    
    public ListState getViewModelState() {
        return this.viewModelState;
    }
    
    public String getXuid() {
        return this.model.getXuid();
    }
    
    public boolean isBusy() {
        return this.isLoadingUserProfile || this.isAddingUserToFavoriteList || this.isRemovingUserFromFavoriteList || this.isAddingUserToFollowingList || this.isRemovingUserFromFollowingList || this.isAddingUserToShareIdentityList || this.isRemovingUserFromShareIdentityList;
    }
    
    public void load() {
        final LoadPersonDataAsyncTask loadProfileAsyncTask = this.loadProfileAsyncTask;
        if (loadProfileAsyncTask != null) {
            loadProfileAsyncTask.cancel();
        }
        (this.loadProfileAsyncTask = new LoadPersonDataAsyncTask()).load(true);
    }
    
    public void onChangeRelationshipCompleted() {
        UTCChangeRelationship.Relationship relationship;
        if (this.model.isCallerFollowingTarget()) {
            relationship = UTCChangeRelationship.Relationship.EXISTINGFRIEND;
        }
        else {
            relationship = UTCChangeRelationship.Relationship.NOTCHANGED;
        }
        UTCChangeRelationship.FavoriteStatus favoriteStatus;
        if (this.model.hasCallerMarkedTargetAsFavorite()) {
            favoriteStatus = UTCChangeRelationship.FavoriteStatus.EXISTINGFAVORITE;
        }
        else {
            favoriteStatus = UTCChangeRelationship.FavoriteStatus.EXISTINGNOTFAVORITED;
        }
        UTCChangeRelationship.RealNameStatus realNameStatus;
        if (this.model.hasCallerMarkedTargetAsIdentityShared()) {
            realNameStatus = UTCChangeRelationship.RealNameStatus.EXISTINGSHARED;
        }
        else {
            realNameStatus = UTCChangeRelationship.RealNameStatus.EXISTINGNOTSHARED;
        }
        final UTCChangeRelationship.GamerType normal = UTCChangeRelationship.GamerType.NORMAL;
        boolean b;
        if (this.changeFriendshipForm.contains(ProfileScreenViewModel.ChangeFriendshipFormOptions.ShouldAddUserToFriendList)) {
            relationship = UTCChangeRelationship.Relationship.ADDFRIEND;
            this.addFollowingUser();
            b = true;
        }
        else {
            b = false;
        }
        if (this.changeFriendshipForm.contains(ProfileScreenViewModel.ChangeFriendshipFormOptions.ShouldRemoveUserFromFriendList)) {
            relationship = UTCChangeRelationship.Relationship.REMOVEFRIEND;
            this.removeFollowingUser();
            b = true;
        }
        if (this.changeFriendshipForm.contains(ProfileScreenViewModel.ChangeFriendshipFormOptions.ShouldAddUserToFavoriteList)) {
            favoriteStatus = UTCChangeRelationship.FavoriteStatus.FAVORITED;
            this.addFavoriteUser();
            b = true;
        }
        if (this.changeFriendshipForm.contains(ProfileScreenViewModel.ChangeFriendshipFormOptions.ShouldRemoveUserFromFavoriteList)) {
            favoriteStatus = UTCChangeRelationship.FavoriteStatus.UNFAVORITED;
            this.removeFavoriteUser();
            b = true;
        }
        if (this.changeFriendshipForm.contains(ProfileScreenViewModel.ChangeFriendshipFormOptions.ShouldAddUserToShareIdentityList)) {
            realNameStatus = UTCChangeRelationship.RealNameStatus.SHARINGON;
            this.addUserToShareIdentityList();
            b = true;
        }
        if (this.changeFriendshipForm.contains(ProfileScreenViewModel.ChangeFriendshipFormOptions.ShouldRemoveUserFromShareIdentityList)) {
            realNameStatus = UTCChangeRelationship.RealNameStatus.SHARINGOFF;
            this.removeUserFromShareIdentityList();
            b = true;
        }
        if (!b) {
            this.notifyDialogAsyncTaskCompleted();
            return;
        }
        UTCChangeRelationship.trackChangeRelationshipDone(relationship, realNameStatus, favoriteStatus, normal);
    }
    
    public void removeFavoriteUser() {
        final RemoveUserFromFavoriteListAsyncTask removeUserFromFavoriteListAsyncTask = this.removeUserFromFavoriteListAsyncTask;
        if (removeUserFromFavoriteListAsyncTask != null) {
            removeUserFromFavoriteListAsyncTask.cancel();
        }
        (this.removeUserFromFavoriteListAsyncTask = new RemoveUserFromFavoriteListAsyncTask(this.model.getXuid())).load(true);
    }
    
    public void removeFollowingUser() {
        final RemoveUserFromFollowingListAsyncTask removeUserFromFollowingListAsyncTask = this.removeUserFromFollowingListAsyncTask;
        if (removeUserFromFollowingListAsyncTask != null) {
            removeUserFromFollowingListAsyncTask.cancel();
        }
        (this.removeUserFromFollowingListAsyncTask = new RemoveUserFromFollowingListAsyncTask(this.model.getXuid())).load(true);
    }
    
    public void removeUserFromShareIdentityList() {
        if (this.removeUserFromFollowingListAsyncTask != null) {
            this.removeUserFromFavoriteListAsyncTask.cancel();
        }
        final ArrayList<String> list = new ArrayList<String>();
        list.add(this.model.getXuid());
        (this.removeUserFromShareIdentityListAsyncTask = new RemoveUserFromShareIdentityListAsyncTask(list)).load(true);
    }
    
    public void setInitialRealNameSharingState(final boolean b) {
        this.isSharingRealNameStart = b;
        this.isSharingRealNameEnd = b;
    }
    
    public void setIsSharingRealNameEnd(final boolean isSharingRealNameEnd) {
        this.isSharingRealNameEnd = isSharingRealNameEnd;
    }
    
    public void setShouldAddUserToFavoriteList(final boolean b) {
        if (b) {
            this.changeFriendshipForm.add(ProfileScreenViewModel.ChangeFriendshipFormOptions.ShouldAddUserToFavoriteList);
            return;
        }
        this.changeFriendshipForm.remove(ProfileScreenViewModel.ChangeFriendshipFormOptions.ShouldAddUserToFavoriteList);
    }
    
    public void setShouldAddUserToFriendList(final boolean b) {
        if (b) {
            this.changeFriendshipForm.add(ProfileScreenViewModel.ChangeFriendshipFormOptions.ShouldAddUserToFriendList);
            return;
        }
        this.changeFriendshipForm.remove(ProfileScreenViewModel.ChangeFriendshipFormOptions.ShouldAddUserToFriendList);
    }
    
    public void setShouldAddUserToShareIdentityList(final boolean b) {
        if (b) {
            this.changeFriendshipForm.add(ProfileScreenViewModel.ChangeFriendshipFormOptions.ShouldAddUserToShareIdentityList);
            return;
        }
        this.changeFriendshipForm.remove(ProfileScreenViewModel.ChangeFriendshipFormOptions.ShouldAddUserToShareIdentityList);
    }
    
    public void setShouldRemoveUserFroShareIdentityList(final boolean b) {
        if (b) {
            this.changeFriendshipForm.add(ProfileScreenViewModel.ChangeFriendshipFormOptions.ShouldRemoveUserFromShareIdentityList);
            return;
        }
        this.changeFriendshipForm.remove(ProfileScreenViewModel.ChangeFriendshipFormOptions.ShouldRemoveUserFromShareIdentityList);
    }
    
    public void setShouldRemoveUserFromFavoriteList(final boolean b) {
        if (b) {
            this.changeFriendshipForm.add(ProfileScreenViewModel.ChangeFriendshipFormOptions.ShouldRemoveUserFromFavoriteList);
            return;
        }
        this.changeFriendshipForm.remove(ProfileScreenViewModel.ChangeFriendshipFormOptions.ShouldRemoveUserFromFavoriteList);
    }
    
    private class AddUserToFavoriteListAsyncTask extends NetworkAsyncTask<AsyncActionStatus>
    {
        private boolean favoriteUser;
        private String favoriteUserXuid;
        
        public AddUserToFavoriteListAsyncTask(final String favoriteUserXuid) {
            this.favoriteUser = false;
            this.favoriteUserXuid = favoriteUserXuid;
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
                final AsyncActionStatus status = meProfileModel.addUserToFavoriteList(this.forceLoad, this.favoriteUserXuid).getStatus();
                if (status == AsyncActionStatus.SUCCESS || status == AsyncActionStatus.NO_CHANGE || status == AsyncActionStatus.NO_OP_SUCCESS) {
                    final ArrayList<FollowersData> favorites = meProfileModel.getFavorites();
                    if (favorites != null) {
                        for (final FollowersData followersData : favorites) {
                            if (followersData.xuid.equals(this.favoriteUserXuid)) {
                                this.favoriteUser = followersData.isFavorite;
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
            ChangeFriendshipDialogViewModel.this.onAddUserToFavoriteListCompleted(AsyncActionStatus.NO_CHANGE, this.favoriteUser);
        }
        
        @Override
        protected void onPostExecute(final AsyncActionStatus asyncActionStatus) {
            ChangeFriendshipDialogViewModel.this.onAddUserToFavoriteListCompleted(asyncActionStatus, this.favoriteUser);
        }
        
        @Override
        protected void onPreExecute() {
            XLEAssert.assertIsUIThread();
            ChangeFriendshipDialogViewModel.this.isAddingUserToFavoriteList = true;
        }
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
                    ChangeFriendshipDialogViewModel.this.model.loadProfileSummary(true);
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
            ChangeFriendshipDialogViewModel.this.onAddUserToFollowingListCompleted(AsyncActionStatus.NO_CHANGE, this.isFollowingUser);
        }
        
        @Override
        protected void onPostExecute(final AsyncActionStatus asyncActionStatus) {
            ChangeFriendshipDialogViewModel.this.onAddUserToFollowingListCompleted(asyncActionStatus, this.isFollowingUser);
        }
        
        @Override
        protected void onPreExecute() {
            XLEAssert.assertIsUIThread();
            ChangeFriendshipDialogViewModel.this.isAddingUserToFollowingList = true;
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
            ChangeFriendshipDialogViewModel.this.onAddUseToShareIdentityListCompleted(asyncActionStatus);
        }
        
        @Override
        protected void onPreExecute() {
            XLEAssert.assertIsUIThread();
            ChangeFriendshipDialogViewModel.this.isAddingUserToShareIdentityList = true;
        }
    }
    
    private class LoadPersonDataAsyncTask extends NetworkAsyncTask<AsyncActionStatus>
    {
        @Override
        protected boolean checkShouldExecute() {
            XLEAssert.assertIsUIThread();
            return false;
        }
        
        @Override
        protected AsyncActionStatus loadDataInBackground() {
            XLEAssert.assertNotNull(ChangeFriendshipDialogViewModel.this.model);
            return ChangeFriendshipDialogViewModel.this.model.loadProfileSummary(this.forceLoad).getStatus();
        }
        
        @Override
        protected AsyncActionStatus onError() {
            return AsyncActionStatus.FAIL;
        }
        
        @Override
        protected void onNoAction() {
            XLEAssert.assertIsUIThread();
            ChangeFriendshipDialogViewModel.this.onLoadPersonDataCompleted(AsyncActionStatus.NO_CHANGE);
        }
        
        @Override
        protected void onPostExecute(final AsyncActionStatus asyncActionStatus) {
            ChangeFriendshipDialogViewModel.this.onLoadPersonDataCompleted(asyncActionStatus);
        }
        
        @Override
        protected void onPreExecute() {
            XLEAssert.assertIsUIThread();
            ChangeFriendshipDialogViewModel.this.isLoadingUserProfile = true;
        }
    }
    
    private class RemoveUserFromFavoriteListAsyncTask extends NetworkAsyncTask<AsyncActionStatus>
    {
        private boolean favoriteUser;
        private String favoriteUserXuid;
        
        public RemoveUserFromFavoriteListAsyncTask(final String favoriteUserXuid) {
            this.favoriteUser = false;
            this.favoriteUserXuid = favoriteUserXuid;
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
                final AsyncActionStatus status = meProfileModel.removeUserFromFavoriteList(this.forceLoad, this.favoriteUserXuid).getStatus();
                if (status == AsyncActionStatus.SUCCESS || status == AsyncActionStatus.NO_CHANGE || status == AsyncActionStatus.NO_OP_SUCCESS) {
                    final ArrayList<FollowersData> favorites = meProfileModel.getFavorites();
                    if (favorites != null) {
                        for (final FollowersData followersData : favorites) {
                            if (followersData.xuid.equals(this.favoriteUserXuid)) {
                                this.favoriteUser = followersData.isFavorite;
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
            ChangeFriendshipDialogViewModel.this.onRemoveUserFromFavoriteListCompleted(AsyncActionStatus.NO_CHANGE, this.favoriteUser);
        }
        
        @Override
        protected void onPostExecute(final AsyncActionStatus asyncActionStatus) {
            ChangeFriendshipDialogViewModel.this.onRemoveUserFromFavoriteListCompleted(asyncActionStatus, this.favoriteUser);
        }
        
        @Override
        protected void onPreExecute() {
            XLEAssert.assertIsUIThread();
            ChangeFriendshipDialogViewModel.this.isRemovingUserFromFavoriteList = true;
        }
    }
    
    private class RemoveUserFromFollowingListAsyncTask extends NetworkAsyncTask<AsyncActionStatus>
    {
        private String followingUserXuid;
        private boolean isFollowingUser;
        
        public RemoveUserFromFollowingListAsyncTask(final String followingUserXuid) {
            this.isFollowingUser = true;
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
                final AsyncActionStatus status = meProfileModel.removeUserFromFollowingList(this.forceLoad, this.followingUserXuid).getStatus();
                if (!AsyncActionStatus.getIsFail(status)) {
                    ChangeFriendshipDialogViewModel.this.model.loadProfileSummary(true);
                    meProfileModel.loadProfileSummary(true);
                    this.isFollowingUser = false;
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
            ChangeFriendshipDialogViewModel.this.onRemoveUserFromFollowingListCompleted(AsyncActionStatus.NO_CHANGE, this.isFollowingUser);
        }
        
        @Override
        protected void onPostExecute(final AsyncActionStatus asyncActionStatus) {
            ChangeFriendshipDialogViewModel.this.onRemoveUserFromFollowingListCompleted(asyncActionStatus, this.isFollowingUser);
        }
        
        @Override
        protected void onPreExecute() {
            XLEAssert.assertIsUIThread();
            ChangeFriendshipDialogViewModel.this.isRemovingUserFromFollowingList = true;
        }
    }
    
    private class RemoveUserFromShareIdentityListAsyncTask extends NetworkAsyncTask<AsyncActionStatus>
    {
        private ArrayList<String> usersToAdd;
        
        public RemoveUserFromShareIdentityListAsyncTask(final ArrayList<String> usersToAdd) {
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
                return meProfileModel.removeUserFromShareIdentity(this.forceLoad, this.usersToAdd).getStatus();
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
            ChangeFriendshipDialogViewModel.this.onRemoveUserFromShareIdentityListCompleted(asyncActionStatus);
        }
        
        @Override
        protected void onPreExecute() {
            XLEAssert.assertIsUIThread();
            ChangeFriendshipDialogViewModel.this.isRemovingUserFromShareIdentityList = true;
        }
    }
}
