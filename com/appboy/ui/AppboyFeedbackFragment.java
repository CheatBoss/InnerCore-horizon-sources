package com.appboy.ui;

import android.widget.*;
import android.view.inputmethod.*;
import android.support.v4.app.*;
import com.appboy.support.*;
import android.text.*;
import android.content.*;
import com.appboy.*;
import android.os.*;
import android.view.*;

public class AppboyFeedbackFragment extends Fragment
{
    private static final String TAG;
    private Button mCancelButton;
    private View$OnClickListener mCancelListener;
    private EditText mEmailEditText;
    private boolean mErrorMessageShown;
    private IFeedbackFinishedListener mFeedbackFinishedListener;
    private CheckBox mIsBugCheckBox;
    private EditText mMessageEditText;
    private int mOriginalSoftInputMode;
    private Button mSendButton;
    private TextWatcher mSendButtonWatcher;
    private View$OnClickListener mSendListener;
    
    static {
        TAG = AppboyLogger.getAppboyLogTag(AppboyFeedbackFragment.class);
    }
    
    private void clearData() {
        this.mEmailEditText.setText((CharSequence)"");
        this.mMessageEditText.setText((CharSequence)"");
        this.mIsBugCheckBox.setChecked(false);
        this.mErrorMessageShown = false;
        this.mEmailEditText.setError((CharSequence)null);
        this.mMessageEditText.setError((CharSequence)null);
    }
    
    private void displayEmailTextError(final int n) {
        if (this.getActivity() != null) {
            this.mEmailEditText.setError((CharSequence)this.getResources().getString(n));
            return;
        }
        AppboyLogger.w(AppboyFeedbackFragment.TAG, "Activity is null. Cannot set feedback form email error message");
    }
    
    private void displayMessageTextError(final int n) {
        if (this.getActivity() != null) {
            this.mMessageEditText.setError((CharSequence)this.getResources().getString(n));
            return;
        }
        AppboyLogger.w(AppboyFeedbackFragment.TAG, "Activity is null. Cannot set feedback form message error.");
    }
    
    private boolean ensureSendButton() {
        return this.validatedMessage() & this.validatedEmail();
    }
    
    private void hideSoftKeyboard() {
        final FragmentActivity activity = this.getActivity();
        activity.getWindow().setSoftInputMode(this.mOriginalSoftInputMode);
        if (activity.getCurrentFocus() != null) {
            ((InputMethodManager)activity.getSystemService("input_method")).hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }
    
    private boolean validatedEmail() {
        final Editable text = this.mEmailEditText.getText();
        final boolean b = false;
        final boolean b2 = text != null && !StringUtils.isNullOrBlank(this.mEmailEditText.getText().toString()) && ValidationUtils.isValidEmailAddress(this.mEmailEditText.getText().toString());
        boolean b3 = b;
        if (this.mEmailEditText.getText() != null) {
            b3 = b;
            if (StringUtils.isNullOrBlank(this.mEmailEditText.getText().toString())) {
                b3 = true;
            }
        }
        if (b2) {
            this.mEmailEditText.setError((CharSequence)null);
            return b2;
        }
        if (b3) {
            this.displayEmailTextError(R$string.com_appboy_feedback_form_empty_email);
            return b2;
        }
        this.displayEmailTextError(R$string.com_appboy_feedback_form_invalid_email);
        return b2;
    }
    
    private boolean validatedMessage() {
        final boolean b = this.mMessageEditText.getText() != null && !StringUtils.isNullOrBlank(this.mMessageEditText.getText().toString());
        if (b) {
            this.mMessageEditText.setError((CharSequence)null);
            return b;
        }
        this.displayMessageTextError(R$string.com_appboy_feedback_form_invalid_message);
        return b;
    }
    
    public EditText getEmailEditText() {
        return this.mEmailEditText;
    }
    
    public EditText getMessageEditText() {
        return this.mMessageEditText;
    }
    
    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);
        this.mSendButtonWatcher = (TextWatcher)new TextWatcher() {
            public void afterTextChanged(final Editable editable) {
                if (AppboyFeedbackFragment.this.mErrorMessageShown) {
                    AppboyFeedbackFragment.this.ensureSendButton();
                }
            }
            
            public void beforeTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
            }
            
            public void onTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
            }
        };
        this.mCancelListener = (View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                AppboyFeedbackFragment.this.hideSoftKeyboard();
                if (AppboyFeedbackFragment.this.mFeedbackFinishedListener != null) {
                    AppboyFeedbackFragment.this.mFeedbackFinishedListener.onFeedbackFinished(FeedbackResult.CANCELLED);
                }
                AppboyFeedbackFragment.this.clearData();
            }
        };
        this.mSendListener = (View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                if (AppboyFeedbackFragment.this.ensureSendButton()) {
                    AppboyFeedbackFragment.this.hideSoftKeyboard();
                    final boolean checked = AppboyFeedbackFragment.this.mIsBugCheckBox.isChecked();
                    final String string = AppboyFeedbackFragment.this.mMessageEditText.getText().toString();
                    final String string2 = AppboyFeedbackFragment.this.mEmailEditText.getText().toString();
                    String beforeFeedbackSubmitted = string;
                    if (AppboyFeedbackFragment.this.mFeedbackFinishedListener != null) {
                        beforeFeedbackSubmitted = AppboyFeedbackFragment.this.mFeedbackFinishedListener.beforeFeedbackSubmitted(string);
                    }
                    Appboy.getInstance((Context)AppboyFeedbackFragment.this.getActivity()).submitFeedback(string2, beforeFeedbackSubmitted, checked);
                    if (AppboyFeedbackFragment.this.mFeedbackFinishedListener != null) {
                        AppboyFeedbackFragment.this.mFeedbackFinishedListener.onFeedbackFinished(FeedbackResult.SUBMITTED);
                    }
                    AppboyFeedbackFragment.this.clearData();
                    return;
                }
                AppboyFeedbackFragment.this.mErrorMessageShown = true;
            }
        };
        this.setRetainInstance(true);
    }
    
    @Override
    public View onCreateView(final LayoutInflater layoutInflater, final ViewGroup viewGroup, final Bundle bundle) {
        final View inflate = layoutInflater.inflate(R$layout.com_appboy_feedback, viewGroup, false);
        this.mCancelButton = (Button)inflate.findViewById(R$id.com_appboy_feedback_cancel);
        this.mSendButton = (Button)inflate.findViewById(R$id.com_appboy_feedback_send);
        this.mIsBugCheckBox = (CheckBox)inflate.findViewById(R$id.com_appboy_feedback_is_bug);
        this.mMessageEditText = (EditText)inflate.findViewById(R$id.com_appboy_feedback_message);
        this.mEmailEditText = (EditText)inflate.findViewById(R$id.com_appboy_feedback_email);
        this.mMessageEditText.addTextChangedListener(this.mSendButtonWatcher);
        this.mEmailEditText.addTextChangedListener(this.mSendButtonWatcher);
        this.mCancelButton.setOnClickListener(this.mCancelListener);
        this.mSendButton.setOnClickListener(this.mSendListener);
        return inflate;
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        this.mMessageEditText.removeTextChangedListener(this.mSendButtonWatcher);
        this.mEmailEditText.removeTextChangedListener(this.mSendButtonWatcher);
    }
    
    @Override
    public void onResume() {
        super.onResume();
        Appboy.getInstance((Context)this.getActivity()).logFeedbackDisplayed();
        final FragmentActivity activity = this.getActivity();
        final Window window = activity.getWindow();
        this.mOriginalSoftInputMode = window.getAttributes().softInputMode;
        window.setSoftInputMode(16);
        Appboy.getInstance((Context)activity).logFeedbackDisplayed();
    }
    
    public void setFeedbackFinishedListener(final IFeedbackFinishedListener mFeedbackFinishedListener) {
        this.mFeedbackFinishedListener = mFeedbackFinishedListener;
    }
    
    public enum FeedbackResult
    {
        CANCELLED, 
        SUBMITTED;
    }
    
    public interface IFeedbackFinishedListener
    {
        String beforeFeedbackSubmitted(final String p0);
        
        void onFeedbackFinished(final FeedbackResult p0);
    }
}
