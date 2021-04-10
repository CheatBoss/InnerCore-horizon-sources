package com.zhekasmirnov.horizon.activity.util;

import android.app.*;
import android.content.*;
import com.zhekasmirnov.horizon.*;
import android.widget.*;
import android.view.*;

public class DialogHelper
{
    public static boolean awaitDecision(final Activity context, final int title, final Object message, final int positive, final int negative) {
        final DecisionStatus status = new DecisionStatus();
        context.runOnUiThread((Runnable)new Runnable() {
            @Override
            public void run() {
                final AlertDialog.Builder builder = new AlertDialog.Builder((Context)context, 2131689480);
                builder.setTitle(title);
                if (message instanceof CharSequence) {
                    builder.setMessage((CharSequence)message);
                }
                else {
                    builder.setMessage((int)message);
                }
                builder.setCancelable(false);
                builder.setNegativeButton(negative, (DialogInterface.OnClickListener)new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int which) {
                        status.decision = false;
                        status.complete = true;
                    }
                });
                builder.setPositiveButton(positive, (DialogInterface.OnClickListener)new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int which) {
                        status.decision = true;
                        status.complete = true;
                    }
                });
                builder.show();
            }
        });
        while (!status.complete) {
            Thread.yield();
        }
        return status.decision;
    }
    
    public static boolean awaitDecision(final int title, final Object message, final int positive, final int negative) {
        return awaitDecision(HorizonApplication.getTopRunningActivity(), title, message, positive, negative);
    }
    
    public static void showTipDialog(final Activity context, final Object title, final Object tip) {
        context.runOnUiThread((Runnable)new Runnable() {
            @Override
            public void run() {
                final AlertDialog.Builder builder = new AlertDialog.Builder((Context)context, 2131689480);
                if (title instanceof CharSequence) {
                    builder.setTitle((CharSequence)title);
                }
                else {
                    builder.setTitle((int)title);
                }
                if (tip instanceof CharSequence) {
                    builder.setMessage((CharSequence)tip);
                }
                else {
                    builder.setMessage((int)tip);
                }
                builder.setPositiveButton(17039370, (DialogInterface.OnClickListener)null);
                builder.show();
            }
        });
    }
    
    public static void showTipDialog(final int title, final int tip) {
        showTipDialog(HorizonApplication.getTopRunningActivity(), title, tip);
    }
    
    public static class DecisionStatus
    {
        boolean complete;
        boolean decision;
        
        public DecisionStatus() {
            this.complete = false;
            this.decision = false;
        }
    }
    
    public static class ProgressDialogHolder implements ProgressInterface
    {
        private final Activity context;
        private final AlertDialog.Builder builder;
        private AlertDialog dialog;
        private TextView message;
        private TextView info;
        private ProgressBar progressBar;
        private boolean isPrepared;
        private boolean isOpened;
        private final int cancelWarnStr;
        private boolean isTerminated;
        
        public ProgressDialogHolder(final int titleStr, final int cancelWarnStr) {
            this(HorizonApplication.getTopRunningActivity(), titleStr, cancelWarnStr);
        }
        
        public ProgressDialogHolder(final Activity context, final int titleStr, final int cancelWarnStr) {
            this.isPrepared = false;
            this.isOpened = false;
            this.isTerminated = false;
            this.context = context;
            this.builder = new AlertDialog.Builder((Context)context, 2131689480);
            this.cancelWarnStr = cancelWarnStr;
            context.runOnUiThread((Runnable)new Runnable() {
                @Override
                public void run() {
                    final RelativeLayout layout = (RelativeLayout)LayoutInflater.from((Context)context).inflate(2131427368, (ViewGroup)null, false);
                    ProgressDialogHolder.this.message = (TextView)layout.findViewById(2131230777);
                    ProgressDialogHolder.this.info = (TextView)layout.findViewById(2131230776);
                    ProgressDialogHolder.this.progressBar = (ProgressBar)layout.findViewById(2131230778);
                    ProgressDialogHolder.this.builder.setTitle(titleStr);
                    ProgressDialogHolder.this.builder.setView((View)layout);
                    ProgressDialogHolder.this.builder.setCancelable(false);
                    ProgressDialogHolder.this.builder.setNegativeButton(17039360, (DialogInterface.OnClickListener)null);
                    ProgressDialogHolder.this.isPrepared = true;
                    if (ProgressDialogHolder.this.isOpened) {
                        ProgressDialogHolder.this.show();
                    }
                }
            });
            while (!this.isPrepared) {
                Thread.yield();
            }
        }
        
        private AlertDialog show() {
            final AlertDialog dialog = this.builder.show();
            final Button button = dialog.getButton(-2);
            button.setOnClickListener((View.OnClickListener)new View.OnClickListener() {
                public void onClick(final View v) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder((Context)ProgressDialogHolder.this.context, 2131689480);
                    builder.setMessage(ProgressDialogHolder.this.cancelWarnStr);
                    builder.setNegativeButton(2131624062, (DialogInterface.OnClickListener)null);
                    builder.setPositiveButton(2131624122, (DialogInterface.OnClickListener)new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, final int which) {
                            ProgressDialogHolder.this.isTerminated = true;
                        }
                    });
                    builder.show();
                }
            });
            return dialog;
        }
        
        public void open() {
            this.isOpened = true;
            if (this.isPrepared) {
                this.context.runOnUiThread((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        ProgressDialogHolder.this.dialog = ProgressDialogHolder.this.show();
                    }
                });
            }
        }
        
        public void close() {
            if (this.isOpened) {
                this.context.runOnUiThread((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        ProgressDialogHolder.this.dialog.cancel();
                    }
                });
            }
        }
        
        public void setText(final int id) {
            this.context.runOnUiThread((Runnable)new Runnable() {
                @Override
                public void run() {
                    ProgressDialogHolder.this.message.setText(id);
                }
            });
        }
        
        public void setText(final String text) {
            this.context.runOnUiThread((Runnable)new Runnable() {
                @Override
                public void run() {
                    ProgressDialogHolder.this.message.setText((CharSequence)text);
                }
            });
        }
        
        @Override
        public boolean isTerminated() {
            return this.isTerminated;
        }
        
        @Override
        public void onProgress(final double progress) {
            this.context.runOnUiThread((Runnable)new Runnable() {
                @Override
                public void run() {
                    ProgressDialogHolder.this.progressBar.setProgress((int)(progress * 1000.0));
                }
            });
        }
        
        public void onDownloadMessage(final String message) {
            this.context.runOnUiThread((Runnable)new Runnable() {
                @Override
                public void run() {
                    ProgressDialogHolder.this.info.setText((CharSequence)message);
                }
            });
        }
        
        public void onComplete() {
        }
    }
    
    public static class EditStringDialog
    {
        public final Activity context;
        private final AlertDialog.Builder builder;
        private AlertDialog dialog;
        private ResultListener listener;
        private TextView titleView;
        private TextView headingView;
        private TextView textView;
        private TextView valueNameView;
        private EditText editTextView;
        private String defaultValue;
        private int dialogWidth;
        private int dialogHeight;
        private boolean isPrepared;
        private boolean isOpened;
        private boolean isResultAvailable;
        private String currentResult;
        
        public EditStringDialog(final Activity context) {
            this.listener = null;
            this.defaultValue = "";
            this.dialogWidth = -1;
            this.dialogHeight = -1;
            this.isPrepared = false;
            this.isOpened = false;
            this.isResultAvailable = false;
            this.currentResult = null;
            this.context = context;
            this.builder = new AlertDialog.Builder((Context)context, 2131689480);
            context.runOnUiThread((Runnable)new Runnable() {
                @Override
                public void run() {
                    final RelativeLayout layout = (RelativeLayout)LayoutInflater.from((Context)context).inflate(2131427383, (ViewGroup)null, false);
                    EditStringDialog.this.titleView = (TextView)layout.findViewById(2131230802);
                    EditStringDialog.this.headingView = (TextView)layout.findViewById(2131230800);
                    EditStringDialog.this.textView = (TextView)layout.findViewById(2131230801);
                    EditStringDialog.this.valueNameView = (TextView)layout.findViewById(2131230804);
                    EditStringDialog.this.editTextView = (EditText)layout.findViewById(2131230803);
                    EditStringDialog.this.builder.setView((View)layout);
                    EditStringDialog.this.builder.setNegativeButton(17039360, (DialogInterface.OnClickListener)new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, final int which) {
                            if (EditStringDialog.this.listener != null) {
                                EditStringDialog.this.listener.onCancel();
                            }
                            EditStringDialog.this.currentResult = null;
                            EditStringDialog.this.isResultAvailable = true;
                        }
                    });
                    EditStringDialog.this.builder.setOnCancelListener((DialogInterface.OnCancelListener)new DialogInterface.OnCancelListener() {
                        public void onCancel(final DialogInterface dialog) {
                            if (EditStringDialog.this.listener != null) {
                                EditStringDialog.this.listener.onCancel();
                            }
                            EditStringDialog.this.currentResult = null;
                            EditStringDialog.this.isResultAvailable = true;
                        }
                    });
                    EditStringDialog.this.isPrepared = true;
                    if (EditStringDialog.this.isOpened) {
                        EditStringDialog.this.show();
                    }
                }
            });
            while (!this.isPrepared) {
                Thread.yield();
            }
        }
        
        public EditStringDialog setLabels(final int title, final int valueName) {
            if (this.isPrepared) {
                this.context.runOnUiThread((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        EditStringDialog.this.titleView.setText(title);
                        EditStringDialog.this.valueNameView.setText(valueName);
                    }
                });
            }
            return this;
        }
        
        public EditStringDialog setDescription(final String description) {
            if (this.isPrepared) {
                this.context.runOnUiThread((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        EditStringDialog.this.textView.setText((CharSequence)description);
                    }
                });
            }
            return this;
        }
        
        public EditStringDialog setDescription(final int description) {
            if (this.isPrepared) {
                this.context.runOnUiThread((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        EditStringDialog.this.textView.setText(description);
                    }
                });
            }
            return this;
        }
        
        public EditStringDialog setHeading(final String heading) {
            if (this.isPrepared) {
                this.context.runOnUiThread((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        EditStringDialog.this.headingView.setText((CharSequence)heading);
                    }
                });
            }
            return this;
        }
        
        public EditStringDialog setDefaultValue(final String value) {
            this.defaultValue = value;
            if (this.isPrepared) {
                this.context.runOnUiThread((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        EditStringDialog.this.editTextView.setText((CharSequence)value);
                        EditStringDialog.this.editTextView.setHint((CharSequence)value);
                    }
                });
            }
            return this;
        }
        
        public EditStringDialog setSize(final int width, final int height) {
            this.dialogWidth = width;
            this.dialogHeight = height;
            return this;
        }
        
        private AlertDialog show() {
            final AlertDialog dialog = this.builder.show();
            this.editTextView.requestFocus();
            final Window window = dialog.getWindow();
            if (window != null && this.dialogWidth != -1 && this.dialogHeight != -1) {
                window.setLayout(this.dialogWidth, this.dialogHeight);
            }
            this.isResultAvailable = false;
            return dialog;
        }
        
        public void open() {
            this.isOpened = true;
            if (this.isPrepared) {
                this.context.runOnUiThread((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        EditStringDialog.this.dialog = EditStringDialog.this.show();
                    }
                });
            }
        }
        
        public void close() {
            if (this.isOpened) {
                this.isOpened = false;
                this.context.runOnUiThread((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        EditStringDialog.this.dialog.cancel();
                    }
                });
            }
        }
        
        public EditStringDialog setListener(final int positiveButton, final ResultListener listener) {
            this.listener = listener;
            if (this.isPrepared) {
                this.context.runOnUiThread((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        EditStringDialog.this.builder.setPositiveButton(positiveButton, (DialogInterface.OnClickListener)new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog, final int which) {
                                final String text = EditStringDialog.this.editTextView.getText() + "";
                                EditStringDialog.this.currentResult = ((EditStringDialog.this.editTextView.getText() != null && text.length() > 0) ? text : EditStringDialog.this.defaultValue);
                                if (listener != null) {
                                    listener.onConfirm(EditStringDialog.this.currentResult);
                                }
                                EditStringDialog.this.isResultAvailable = true;
                            }
                        });
                    }
                });
            }
            return this;
        }
        
        public String awaitResult() {
            while (!this.isResultAvailable) {
                Thread.yield();
            }
            return this.currentResult;
        }
        
        public interface ResultListener
        {
            void onConfirm(final String p0);
            
            void onCancel();
        }
    }
    
    public interface ProgressInterface
    {
        boolean isTerminated();
        
        void onProgress(final double p0);
    }
}
