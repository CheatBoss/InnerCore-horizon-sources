package android.support.v4.app;

import android.view.*;
import android.app.*;
import java.util.*;
import android.content.*;
import android.net.*;
import android.os.*;
import android.support.annotation.*;
import android.graphics.drawable.*;
import android.util.*;
import android.content.pm.*;
import android.text.*;

public final class ShareCompat
{
    public static final String EXTRA_CALLING_ACTIVITY = "android.support.v4.app.EXTRA_CALLING_ACTIVITY";
    public static final String EXTRA_CALLING_PACKAGE = "android.support.v4.app.EXTRA_CALLING_PACKAGE";
    private static ShareCompatImpl IMPL;
    
    static {
        ShareCompatImpl impl;
        if (Build$VERSION.SDK_INT >= 16) {
            impl = new ShareCompatImplJB();
        }
        else if (Build$VERSION.SDK_INT >= 14) {
            impl = new ShareCompatImplICS();
        }
        else {
            impl = new ShareCompatImplBase();
        }
        ShareCompat.IMPL = impl;
    }
    
    private ShareCompat() {
    }
    
    public static void configureMenuItem(final Menu menu, final int n, final IntentBuilder intentBuilder) {
        final MenuItem item = menu.findItem(n);
        if (item == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Could not find menu item with id ");
            sb.append(n);
            sb.append(" in the supplied menu");
            throw new IllegalArgumentException(sb.toString());
        }
        configureMenuItem(item, intentBuilder);
    }
    
    public static void configureMenuItem(final MenuItem menuItem, final IntentBuilder intentBuilder) {
        ShareCompat.IMPL.configureMenuItem(menuItem, intentBuilder);
    }
    
    public static ComponentName getCallingActivity(final Activity activity) {
        ComponentName callingActivity;
        if ((callingActivity = activity.getCallingActivity()) == null) {
            callingActivity = (ComponentName)activity.getIntent().getParcelableExtra("android.support.v4.app.EXTRA_CALLING_ACTIVITY");
        }
        return callingActivity;
    }
    
    public static String getCallingPackage(final Activity activity) {
        String s;
        if ((s = activity.getCallingPackage()) == null) {
            s = activity.getIntent().getStringExtra("android.support.v4.app.EXTRA_CALLING_PACKAGE");
        }
        return s;
    }
    
    public static class IntentBuilder
    {
        private Activity mActivity;
        private ArrayList<String> mBccAddresses;
        private ArrayList<String> mCcAddresses;
        private CharSequence mChooserTitle;
        private Intent mIntent;
        private ArrayList<Uri> mStreams;
        private ArrayList<String> mToAddresses;
        
        private IntentBuilder(final Activity mActivity) {
            this.mActivity = mActivity;
            (this.mIntent = new Intent().setAction("android.intent.action.SEND")).putExtra("android.support.v4.app.EXTRA_CALLING_PACKAGE", mActivity.getPackageName());
            this.mIntent.putExtra("android.support.v4.app.EXTRA_CALLING_ACTIVITY", (Parcelable)mActivity.getComponentName());
            this.mIntent.addFlags(524288);
        }
        
        private void combineArrayExtra(final String s, final ArrayList<String> list) {
            final String[] stringArrayExtra = this.mIntent.getStringArrayExtra(s);
            int length;
            if (stringArrayExtra != null) {
                length = stringArrayExtra.length;
            }
            else {
                length = 0;
            }
            final String[] array = new String[list.size() + length];
            list.toArray(array);
            if (stringArrayExtra != null) {
                System.arraycopy(stringArrayExtra, 0, array, list.size(), length);
            }
            this.mIntent.putExtra(s, array);
        }
        
        private void combineArrayExtra(final String s, final String[] array) {
            final Intent intent = this.getIntent();
            final String[] stringArrayExtra = intent.getStringArrayExtra(s);
            int length;
            if (stringArrayExtra != null) {
                length = stringArrayExtra.length;
            }
            else {
                length = 0;
            }
            final String[] array2 = new String[array.length + length];
            if (stringArrayExtra != null) {
                System.arraycopy(stringArrayExtra, 0, array2, 0, length);
            }
            System.arraycopy(array, 0, array2, length, array.length);
            intent.putExtra(s, array2);
        }
        
        public static IntentBuilder from(final Activity activity) {
            return new IntentBuilder(activity);
        }
        
        public IntentBuilder addEmailBcc(final String s) {
            if (this.mBccAddresses == null) {
                this.mBccAddresses = new ArrayList<String>();
            }
            this.mBccAddresses.add(s);
            return this;
        }
        
        public IntentBuilder addEmailBcc(final String[] array) {
            this.combineArrayExtra("android.intent.extra.BCC", array);
            return this;
        }
        
        public IntentBuilder addEmailCc(final String s) {
            if (this.mCcAddresses == null) {
                this.mCcAddresses = new ArrayList<String>();
            }
            this.mCcAddresses.add(s);
            return this;
        }
        
        public IntentBuilder addEmailCc(final String[] array) {
            this.combineArrayExtra("android.intent.extra.CC", array);
            return this;
        }
        
        public IntentBuilder addEmailTo(final String s) {
            if (this.mToAddresses == null) {
                this.mToAddresses = new ArrayList<String>();
            }
            this.mToAddresses.add(s);
            return this;
        }
        
        public IntentBuilder addEmailTo(final String[] array) {
            this.combineArrayExtra("android.intent.extra.EMAIL", array);
            return this;
        }
        
        public IntentBuilder addStream(final Uri stream) {
            final Uri uri = (Uri)this.mIntent.getParcelableExtra("android.intent.extra.STREAM");
            if (uri == null) {
                return this.setStream(stream);
            }
            if (this.mStreams == null) {
                this.mStreams = new ArrayList<Uri>();
            }
            if (uri != null) {
                this.mIntent.removeExtra("android.intent.extra.STREAM");
                this.mStreams.add(uri);
            }
            this.mStreams.add(stream);
            return this;
        }
        
        public Intent createChooserIntent() {
            return Intent.createChooser(this.getIntent(), this.mChooserTitle);
        }
        
        Activity getActivity() {
            return this.mActivity;
        }
        
        public Intent getIntent() {
            if (this.mToAddresses != null) {
                this.combineArrayExtra("android.intent.extra.EMAIL", this.mToAddresses);
                this.mToAddresses = null;
            }
            if (this.mCcAddresses != null) {
                this.combineArrayExtra("android.intent.extra.CC", this.mCcAddresses);
                this.mCcAddresses = null;
            }
            if (this.mBccAddresses != null) {
                this.combineArrayExtra("android.intent.extra.BCC", this.mBccAddresses);
                this.mBccAddresses = null;
            }
            final ArrayList<Uri> mStreams = this.mStreams;
            boolean b = true;
            if (mStreams == null || this.mStreams.size() <= 1) {
                b = false;
            }
            final boolean equals = this.mIntent.getAction().equals("android.intent.action.SEND_MULTIPLE");
            if (!b && equals) {
                this.mIntent.setAction("android.intent.action.SEND");
                if (this.mStreams != null && !this.mStreams.isEmpty()) {
                    this.mIntent.putExtra("android.intent.extra.STREAM", (Parcelable)this.mStreams.get(0));
                }
                else {
                    this.mIntent.removeExtra("android.intent.extra.STREAM");
                }
                this.mStreams = null;
            }
            if (b && !equals) {
                this.mIntent.setAction("android.intent.action.SEND_MULTIPLE");
                if (this.mStreams != null && !this.mStreams.isEmpty()) {
                    this.mIntent.putParcelableArrayListExtra("android.intent.extra.STREAM", (ArrayList)this.mStreams);
                }
                else {
                    this.mIntent.removeExtra("android.intent.extra.STREAM");
                }
            }
            return this.mIntent;
        }
        
        public IntentBuilder setChooserTitle(@StringRes final int n) {
            return this.setChooserTitle(this.mActivity.getText(n));
        }
        
        public IntentBuilder setChooserTitle(final CharSequence mChooserTitle) {
            this.mChooserTitle = mChooserTitle;
            return this;
        }
        
        public IntentBuilder setEmailBcc(final String[] array) {
            this.mIntent.putExtra("android.intent.extra.BCC", array);
            return this;
        }
        
        public IntentBuilder setEmailCc(final String[] array) {
            this.mIntent.putExtra("android.intent.extra.CC", array);
            return this;
        }
        
        public IntentBuilder setEmailTo(final String[] array) {
            if (this.mToAddresses != null) {
                this.mToAddresses = null;
            }
            this.mIntent.putExtra("android.intent.extra.EMAIL", array);
            return this;
        }
        
        public IntentBuilder setHtmlText(final String s) {
            this.mIntent.putExtra("android.intent.extra.HTML_TEXT", s);
            if (!this.mIntent.hasExtra("android.intent.extra.TEXT")) {
                this.setText((CharSequence)Html.fromHtml(s));
            }
            return this;
        }
        
        public IntentBuilder setStream(final Uri uri) {
            if (!this.mIntent.getAction().equals("android.intent.action.SEND")) {
                this.mIntent.setAction("android.intent.action.SEND");
            }
            this.mStreams = null;
            this.mIntent.putExtra("android.intent.extra.STREAM", (Parcelable)uri);
            return this;
        }
        
        public IntentBuilder setSubject(final String s) {
            this.mIntent.putExtra("android.intent.extra.SUBJECT", s);
            return this;
        }
        
        public IntentBuilder setText(final CharSequence charSequence) {
            this.mIntent.putExtra("android.intent.extra.TEXT", charSequence);
            return this;
        }
        
        public IntentBuilder setType(final String type) {
            this.mIntent.setType(type);
            return this;
        }
        
        public void startChooser() {
            this.mActivity.startActivity(this.createChooserIntent());
        }
    }
    
    public static class IntentReader
    {
        private static final String TAG = "IntentReader";
        private Activity mActivity;
        private ComponentName mCallingActivity;
        private String mCallingPackage;
        private Intent mIntent;
        private ArrayList<Uri> mStreams;
        
        private IntentReader(final Activity mActivity) {
            this.mActivity = mActivity;
            this.mIntent = mActivity.getIntent();
            this.mCallingPackage = ShareCompat.getCallingPackage(mActivity);
            this.mCallingActivity = ShareCompat.getCallingActivity(mActivity);
        }
        
        public static IntentReader from(final Activity activity) {
            return new IntentReader(activity);
        }
        
        public ComponentName getCallingActivity() {
            return this.mCallingActivity;
        }
        
        public Drawable getCallingActivityIcon() {
            if (this.mCallingActivity == null) {
                return null;
            }
            final PackageManager packageManager = this.mActivity.getPackageManager();
            try {
                return packageManager.getActivityIcon(this.mCallingActivity);
            }
            catch (PackageManager$NameNotFoundException ex) {
                Log.e("IntentReader", "Could not retrieve icon for calling activity", (Throwable)ex);
                return null;
            }
        }
        
        public Drawable getCallingApplicationIcon() {
            if (this.mCallingPackage == null) {
                return null;
            }
            final PackageManager packageManager = this.mActivity.getPackageManager();
            try {
                return packageManager.getApplicationIcon(this.mCallingPackage);
            }
            catch (PackageManager$NameNotFoundException ex) {
                Log.e("IntentReader", "Could not retrieve icon for calling application", (Throwable)ex);
                return null;
            }
        }
        
        public CharSequence getCallingApplicationLabel() {
            if (this.mCallingPackage == null) {
                return null;
            }
            final PackageManager packageManager = this.mActivity.getPackageManager();
            try {
                return packageManager.getApplicationLabel(packageManager.getApplicationInfo(this.mCallingPackage, 0));
            }
            catch (PackageManager$NameNotFoundException ex) {
                Log.e("IntentReader", "Could not retrieve label for calling application", (Throwable)ex);
                return null;
            }
        }
        
        public String getCallingPackage() {
            return this.mCallingPackage;
        }
        
        public String[] getEmailBcc() {
            return this.mIntent.getStringArrayExtra("android.intent.extra.BCC");
        }
        
        public String[] getEmailCc() {
            return this.mIntent.getStringArrayExtra("android.intent.extra.CC");
        }
        
        public String[] getEmailTo() {
            return this.mIntent.getStringArrayExtra("android.intent.extra.EMAIL");
        }
        
        public String getHtmlText() {
            String s2;
            final String s = s2 = this.mIntent.getStringExtra("android.intent.extra.HTML_TEXT");
            if (s == null) {
                final CharSequence text = this.getText();
                if (text instanceof Spanned) {
                    return Html.toHtml((Spanned)text);
                }
                s2 = s;
                if (text != null) {
                    s2 = ShareCompat.IMPL.escapeHtml(text);
                }
            }
            return s2;
        }
        
        public Uri getStream() {
            return (Uri)this.mIntent.getParcelableExtra("android.intent.extra.STREAM");
        }
        
        public Uri getStream(final int n) {
            if (this.mStreams == null && this.isMultipleShare()) {
                this.mStreams = (ArrayList<Uri>)this.mIntent.getParcelableArrayListExtra("android.intent.extra.STREAM");
            }
            Object o;
            if (this.mStreams != null) {
                o = this.mStreams.get(n);
            }
            else {
                if (n != 0) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Stream items available: ");
                    sb.append(this.getStreamCount());
                    sb.append(" index requested: ");
                    sb.append(n);
                    throw new IndexOutOfBoundsException(sb.toString());
                }
                o = this.mIntent.getParcelableExtra("android.intent.extra.STREAM");
            }
            return (Uri)o;
        }
        
        public int getStreamCount() {
            if (this.mStreams == null && this.isMultipleShare()) {
                this.mStreams = (ArrayList<Uri>)this.mIntent.getParcelableArrayListExtra("android.intent.extra.STREAM");
            }
            if (this.mStreams != null) {
                return this.mStreams.size();
            }
            if (this.mIntent.hasExtra("android.intent.extra.STREAM")) {
                return 1;
            }
            return 0;
        }
        
        public String getSubject() {
            return this.mIntent.getStringExtra("android.intent.extra.SUBJECT");
        }
        
        public CharSequence getText() {
            return this.mIntent.getCharSequenceExtra("android.intent.extra.TEXT");
        }
        
        public String getType() {
            return this.mIntent.getType();
        }
        
        public boolean isMultipleShare() {
            return "android.intent.action.SEND_MULTIPLE".equals(this.mIntent.getAction());
        }
        
        public boolean isShareIntent() {
            final String action = this.mIntent.getAction();
            return "android.intent.action.SEND".equals(action) || "android.intent.action.SEND_MULTIPLE".equals(action);
        }
        
        public boolean isSingleShare() {
            return "android.intent.action.SEND".equals(this.mIntent.getAction());
        }
    }
    
    interface ShareCompatImpl
    {
        void configureMenuItem(final MenuItem p0, final IntentBuilder p1);
        
        String escapeHtml(final CharSequence p0);
    }
    
    static class ShareCompatImplBase implements ShareCompatImpl
    {
        private static void withinStyle(final StringBuilder sb, final CharSequence charSequence, int i, final int n) {
            while (i < n) {
                final char char1 = charSequence.charAt(i);
                Label_0187: {
                    String string;
                    if (char1 == '<') {
                        string = "&lt;";
                    }
                    else if (char1 == '>') {
                        string = "&gt;";
                    }
                    else if (char1 == '&') {
                        string = "&amp;";
                    }
                    else if (char1 <= '~' && char1 >= ' ') {
                        if (char1 == ' ') {
                            while (true) {
                                final int n2 = i + 1;
                                if (n2 >= n || charSequence.charAt(n2) != ' ') {
                                    break;
                                }
                                sb.append("&nbsp;");
                                i = n2;
                            }
                            sb.append(' ');
                            break Label_0187;
                        }
                        sb.append(char1);
                        break Label_0187;
                    }
                    else {
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append("&#");
                        sb2.append((int)char1);
                        sb2.append(";");
                        string = sb2.toString();
                    }
                    sb.append(string);
                }
                ++i;
            }
        }
        
        @Override
        public void configureMenuItem(final MenuItem menuItem, final IntentBuilder intentBuilder) {
            menuItem.setIntent(intentBuilder.createChooserIntent());
        }
        
        @Override
        public String escapeHtml(final CharSequence charSequence) {
            final StringBuilder sb = new StringBuilder();
            withinStyle(sb, charSequence, 0, charSequence.length());
            return sb.toString();
        }
    }
    
    static class ShareCompatImplICS extends ShareCompatImplBase
    {
        @Override
        public void configureMenuItem(final MenuItem menuItem, final IntentBuilder intentBuilder) {
            ShareCompatICS.configureMenuItem(menuItem, intentBuilder.getActivity(), intentBuilder.getIntent());
            if (this.shouldAddChooserIntent(menuItem)) {
                menuItem.setIntent(intentBuilder.createChooserIntent());
            }
        }
        
        boolean shouldAddChooserIntent(final MenuItem menuItem) {
            return !menuItem.hasSubMenu();
        }
    }
    
    static class ShareCompatImplJB extends ShareCompatImplICS
    {
        @Override
        public String escapeHtml(final CharSequence charSequence) {
            return ShareCompatJB.escapeHtml(charSequence);
        }
        
        @Override
        boolean shouldAddChooserIntent(final MenuItem menuItem) {
            return false;
        }
    }
}
