package com.microsoft.aad.adal;

import android.widget.*;
import android.app.*;
import android.content.*;
import android.view.*;

class HttpAuthDialog
{
    private CancelListener mCancelListener;
    private final Context mContext;
    private AlertDialog mDialog;
    private final String mHost;
    private OkListener mOkListener;
    private EditText mPasswordView;
    private final String mRealm;
    private EditText mUsernameView;
    
    public HttpAuthDialog(final Context mContext, final String mHost, final String mRealm) {
        this.mContext = mContext;
        this.mHost = mHost;
        this.mRealm = mRealm;
        this.mDialog = null;
        this.createDialog();
    }
    
    private void createDialog() {
        final View inflate = LayoutInflater.from(this.mContext).inflate(R$layout.http_auth_dialog, (ViewGroup)null);
        this.mUsernameView = (EditText)inflate.findViewById(R$id.editUserName);
        (this.mPasswordView = (EditText)inflate.findViewById(R$id.editPassword)).setOnEditorActionListener((TextView$OnEditorActionListener)new TextView$OnEditorActionListener() {
            public boolean onEditorAction(final TextView textView, final int n, final KeyEvent keyEvent) {
                if (n == 6) {
                    HttpAuthDialog.this.mDialog.getButton(-1).performClick();
                    return true;
                }
                return false;
            }
        });
        this.mDialog = new AlertDialog$Builder(this.mContext).setTitle((CharSequence)this.mContext.getText(R$string.http_auth_dialog_title).toString()).setView(inflate).setPositiveButton(R$string.http_auth_dialog_login, (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
            public void onClick(final DialogInterface dialogInterface, final int n) {
                if (HttpAuthDialog.this.mOkListener != null) {
                    HttpAuthDialog.this.mOkListener.onOk(HttpAuthDialog.this.mHost, HttpAuthDialog.this.mRealm, HttpAuthDialog.this.mUsernameView.getText().toString(), HttpAuthDialog.this.mPasswordView.getText().toString());
                }
            }
        }).setNegativeButton(R$string.http_auth_dialog_cancel, (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
            public void onClick(final DialogInterface dialogInterface, final int n) {
                if (HttpAuthDialog.this.mCancelListener != null) {
                    HttpAuthDialog.this.mCancelListener.onCancel();
                }
            }
        }).setOnCancelListener((DialogInterface$OnCancelListener)new DialogInterface$OnCancelListener() {
            public void onCancel(final DialogInterface dialogInterface) {
                if (HttpAuthDialog.this.mCancelListener != null) {
                    HttpAuthDialog.this.mCancelListener.onCancel();
                }
            }
        }).create();
    }
    
    public void setCancelListener(final CancelListener mCancelListener) {
        this.mCancelListener = mCancelListener;
    }
    
    public void setOkListener(final OkListener mOkListener) {
        this.mOkListener = mOkListener;
    }
    
    public void show() {
        this.mDialog.show();
        this.mUsernameView.requestFocus();
    }
    
    public interface CancelListener
    {
        void onCancel();
    }
    
    public interface OkListener
    {
        void onOk(final String p0, final String p1, final String p2, final String p3);
    }
}
