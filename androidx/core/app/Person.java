package androidx.core.app;

import androidx.core.graphics.drawable.*;
import androidx.annotation.*;
import android.os.*;
import android.app.*;
import android.graphics.drawable.*;

public class Person
{
    private static final String ICON_KEY = "icon";
    private static final String IS_BOT_KEY = "isBot";
    private static final String IS_IMPORTANT_KEY = "isImportant";
    private static final String KEY_KEY = "key";
    private static final String NAME_KEY = "name";
    private static final String URI_KEY = "uri";
    @Nullable
    IconCompat mIcon;
    boolean mIsBot;
    boolean mIsImportant;
    @Nullable
    String mKey;
    @Nullable
    CharSequence mName;
    @Nullable
    String mUri;
    
    Person(final Builder builder) {
        this.mName = builder.mName;
        this.mIcon = builder.mIcon;
        this.mUri = builder.mUri;
        this.mKey = builder.mKey;
        this.mIsBot = builder.mIsBot;
        this.mIsImportant = builder.mIsImportant;
    }
    
    @NonNull
    @RequiresApi(28)
    @RestrictTo({ RestrictTo$Scope.LIBRARY_GROUP_PREFIX })
    public static Person fromAndroidPerson(@NonNull final android.app.Person person) {
        final Builder setName = new Builder().setName(person.getName());
        IconCompat fromIcon;
        if (person.getIcon() != null) {
            fromIcon = IconCompat.createFromIcon(person.getIcon());
        }
        else {
            fromIcon = null;
        }
        return setName.setIcon(fromIcon).setUri(person.getUri()).setKey(person.getKey()).setBot(person.isBot()).setImportant(person.isImportant()).build();
    }
    
    @NonNull
    public static Person fromBundle(@NonNull final Bundle bundle) {
        final Bundle bundle2 = bundle.getBundle("icon");
        final Builder setName = new Builder().setName(bundle.getCharSequence("name"));
        IconCompat fromBundle;
        if (bundle2 != null) {
            fromBundle = IconCompat.createFromBundle(bundle2);
        }
        else {
            fromBundle = null;
        }
        return setName.setIcon(fromBundle).setUri(bundle.getString("uri")).setKey(bundle.getString("key")).setBot(bundle.getBoolean("isBot")).setImportant(bundle.getBoolean("isImportant")).build();
    }
    
    @NonNull
    @RequiresApi(22)
    @RestrictTo({ RestrictTo$Scope.LIBRARY_GROUP_PREFIX })
    public static Person fromPersistableBundle(@NonNull final PersistableBundle persistableBundle) {
        return new Builder().setName(persistableBundle.getString("name")).setUri(persistableBundle.getString("uri")).setKey(persistableBundle.getString("key")).setBot(persistableBundle.getBoolean("isBot")).setImportant(persistableBundle.getBoolean("isImportant")).build();
    }
    
    @Nullable
    public IconCompat getIcon() {
        return this.mIcon;
    }
    
    @Nullable
    public String getKey() {
        return this.mKey;
    }
    
    @Nullable
    public CharSequence getName() {
        return this.mName;
    }
    
    @Nullable
    public String getUri() {
        return this.mUri;
    }
    
    public boolean isBot() {
        return this.mIsBot;
    }
    
    public boolean isImportant() {
        return this.mIsImportant;
    }
    
    @NonNull
    @RequiresApi(28)
    @RestrictTo({ RestrictTo$Scope.LIBRARY_GROUP_PREFIX })
    public android.app.Person toAndroidPerson() {
        final Person$Builder setName = new Person$Builder().setName(this.getName());
        Icon icon;
        if (this.getIcon() != null) {
            icon = this.getIcon().toIcon();
        }
        else {
            icon = null;
        }
        return setName.setIcon(icon).setUri(this.getUri()).setKey(this.getKey()).setBot(this.isBot()).setImportant(this.isImportant()).build();
    }
    
    @NonNull
    public Builder toBuilder() {
        return new Builder(this);
    }
    
    @NonNull
    public Bundle toBundle() {
        final Bundle bundle = new Bundle();
        bundle.putCharSequence("name", this.mName);
        Bundle bundle2;
        if (this.mIcon != null) {
            bundle2 = this.mIcon.toBundle();
        }
        else {
            bundle2 = null;
        }
        bundle.putBundle("icon", bundle2);
        bundle.putString("uri", this.mUri);
        bundle.putString("key", this.mKey);
        bundle.putBoolean("isBot", this.mIsBot);
        bundle.putBoolean("isImportant", this.mIsImportant);
        return bundle;
    }
    
    @NonNull
    @RequiresApi(22)
    @RestrictTo({ RestrictTo$Scope.LIBRARY_GROUP_PREFIX })
    public PersistableBundle toPersistableBundle() {
        final PersistableBundle persistableBundle = new PersistableBundle();
        String string;
        if (this.mName != null) {
            string = this.mName.toString();
        }
        else {
            string = null;
        }
        persistableBundle.putString("name", string);
        persistableBundle.putString("uri", this.mUri);
        persistableBundle.putString("key", this.mKey);
        persistableBundle.putBoolean("isBot", this.mIsBot);
        persistableBundle.putBoolean("isImportant", this.mIsImportant);
        return persistableBundle;
    }
    
    public static class Builder
    {
        @Nullable
        IconCompat mIcon;
        boolean mIsBot;
        boolean mIsImportant;
        @Nullable
        String mKey;
        @Nullable
        CharSequence mName;
        @Nullable
        String mUri;
        
        public Builder() {
        }
        
        Builder(final Person person) {
            this.mName = person.mName;
            this.mIcon = person.mIcon;
            this.mUri = person.mUri;
            this.mKey = person.mKey;
            this.mIsBot = person.mIsBot;
            this.mIsImportant = person.mIsImportant;
        }
        
        @NonNull
        public Person build() {
            return new Person(this);
        }
        
        @NonNull
        public Builder setBot(final boolean mIsBot) {
            this.mIsBot = mIsBot;
            return this;
        }
        
        @NonNull
        public Builder setIcon(@Nullable final IconCompat mIcon) {
            this.mIcon = mIcon;
            return this;
        }
        
        @NonNull
        public Builder setImportant(final boolean mIsImportant) {
            this.mIsImportant = mIsImportant;
            return this;
        }
        
        @NonNull
        public Builder setKey(@Nullable final String mKey) {
            this.mKey = mKey;
            return this;
        }
        
        @NonNull
        public Builder setName(@Nullable final CharSequence mName) {
            this.mName = mName;
            return this;
        }
        
        @NonNull
        public Builder setUri(@Nullable final String mUri) {
            this.mUri = mUri;
            return this;
        }
    }
}
