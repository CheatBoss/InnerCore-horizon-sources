package com.microsoft.xbox.idp.ui;

import android.util.*;
import com.microsoft.xbox.telemetry.helpers.*;
import com.microsoft.xbox.idp.model.*;
import android.content.*;
import android.os.*;
import com.microsoft.xbox.idp.compat.*;
import com.microsoft.xbox.idp.interop.*;
import com.microsoft.xboxtcui.*;

public class ErrorActivity extends BaseActivity implements HeaderFragment.Callbacks, ErrorButtonsFragment.Callbacks
{
    public static final String ARG_ERROR_TYPE = "ARG_ERROR_TYPE";
    public static final String ARG_GAMER_TAG = "ARG_GAMER_TAG";
    public static final int RESULT_TRY_AGAIN = 1;
    private static final String TAG;
    private int activityResult;
    
    static {
        TAG = ErrorActivity.class.getSimpleName();
    }
    
    public ErrorActivity() {
        this.activityResult = 0;
    }
    
    public void finish() {
        UTCPageView.removePage();
        super.finish();
    }
    
    @Override
    public void onClickCloseHeader() {
        Log.d(ErrorActivity.TAG, "onClickCloseHeader");
        UTCError.trackClose(ErrorScreen.fromId(this.getIntent().getIntExtra("ARG_ERROR_TYPE", -1)), this.getTitle());
        this.finish();
    }
    
    @Override
    public void onClickedLeftButton() {
        Log.d(ErrorActivity.TAG, "onClickedLeftButton");
        final ErrorScreen fromId = ErrorScreen.fromId(this.getIntent().getIntExtra("ARG_ERROR_TYPE", -1));
        if (fromId == ErrorScreen.BAN) {
            UTCError.trackGoToEnforcement(fromId, this.getTitle());
            try {
                this.startActivity(new Intent("android.intent.action.VIEW", Const.URL_ENFORCEMENT_XBOX_COM));
                return;
            }
            catch (ActivityNotFoundException ex) {
                Log.e(ErrorActivity.TAG, ex.getMessage());
                return;
            }
        }
        UTCError.trackTryAgain(fromId, this.getTitle());
        this.setResult(this.activityResult = 1);
        this.finish();
    }
    
    @Override
    public void onClickedRightButton() {
        Log.d(ErrorActivity.TAG, "onClickedRightButton");
        UTCError.trackRightButton(ErrorScreen.fromId(this.getIntent().getIntExtra("ARG_ERROR_TYPE", -1)), this.getTitle());
        this.finish();
    }
    
    @Override
    protected void onCreate(final Bundle bundle) {
        Log.d(ErrorActivity.TAG, "onCreate");
        super.onCreate(bundle);
        this.setContentView(R$layout.xbid_activity_error);
        final Intent intent = this.getIntent();
        UiUtil.ensureHeaderFragment(this, R$id.xbid_header_fragment, intent.getExtras());
        if (!intent.hasExtra("ARG_ERROR_TYPE")) {
            Log.e(ErrorActivity.TAG, "No error type was provided");
            return;
        }
        final ErrorScreen fromId = ErrorScreen.fromId(intent.getIntExtra("ARG_ERROR_TYPE", -1));
        if (fromId != null) {
            UiUtil.ensureErrorFragment(this, fromId);
            UiUtil.ensureErrorButtonsFragment(this, fromId);
            UTCError.trackPageView(fromId, this.getTitle());
            return;
        }
        Log.e(ErrorActivity.TAG, "Incorrect error type was provided");
    }
    
    public enum ErrorScreen
    {
        BAN(Interop.ErrorType.BAN, (Class<? extends BaseFragment>)BanErrorFragment.class, R$string.xbid_more_info), 
        CATCHALL(Interop.ErrorType.CATCHALL, (Class<? extends BaseFragment>)CatchAllErrorFragment.class, R$string.xbid_try_again), 
        CREATION(Interop.ErrorType.CREATION, (Class<? extends BaseFragment>)CreationErrorFragment.class, R$string.xbid_try_again), 
        OFFLINE(Interop.ErrorType.OFFLINE, (Class<? extends BaseFragment>)OfflineErrorFragment.class, R$string.xbid_try_again);
        
        public final Class<? extends BaseFragment> errorFragmentClass;
        public final int leftButtonTextId;
        public final Interop.ErrorType type;
        
        private ErrorScreen(final Interop.ErrorType type, final Class<? extends BaseFragment> errorFragmentClass, final int leftButtonTextId) {
            this.type = type;
            this.errorFragmentClass = errorFragmentClass;
            this.leftButtonTextId = leftButtonTextId;
        }
        
        public static ErrorScreen fromId(final int n) {
            final ErrorScreen[] values = values();
            for (int length = values.length, i = 0; i < length; ++i) {
                final ErrorScreen errorScreen = values[i];
                if (errorScreen.type.getId() == n) {
                    return errorScreen;
                }
            }
            return null;
        }
    }
}
