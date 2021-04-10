package com.appboy;

import com.appboy.support.*;
import java.io.*;
import com.appboy.enums.*;
import bo.app.*;
import com.appboy.models.outgoing.*;

public class AppboyUser
{
    private static final String a;
    private final dt b;
    private final dr c;
    private final Object d;
    private final bu e;
    private volatile String f;
    private final br g;
    
    static {
        a = AppboyLogger.getAppboyLogTag(AppboyUser.class);
    }
    
    AppboyUser(final dt b, final br g, final String f, final bu e, final dr c) {
        this.d = new Object();
        this.f = f;
        this.b = b;
        this.e = e;
        this.c = c;
        this.g = g;
    }
    
    void a(final String f) {
        synchronized (this.d) {
            if (!this.f.equals("") && !this.f.equals(f)) {
                final StringBuilder sb = new StringBuilder();
                sb.append("setExternalId can not be used to change the external ID of a UserCache from a non-empty value to a new value. Was: [");
                sb.append(this.f);
                sb.append("], tried to change to: [");
                sb.append(f);
                sb.append("]");
                throw new IllegalArgumentException(sb.toString());
            }
            this.f = f;
            this.b.a(f);
        }
    }
    
    public boolean addAlias(final String s, final String s2) {
        if (StringUtils.isNullOrBlank(s)) {
            AppboyLogger.w(AppboyUser.a, "Invalid alias parameter: alias is required to be non-null and non-empty. Not adding alias.");
            return false;
        }
        if (StringUtils.isNullOrBlank(s2)) {
            AppboyLogger.w(AppboyUser.a, "Invalid label parameter: label is required to be non-null and non-empty. Not adding alias.");
            return false;
        }
        try {
            return this.g.a(cg.a(s, s2));
        }
        catch (Exception ex) {
            final String a = AppboyUser.a;
            final StringBuilder sb = new StringBuilder();
            sb.append("Failed to set alias: ");
            sb.append(s);
            AppboyLogger.e(a, sb.toString(), ex);
            return false;
        }
    }
    
    public boolean addToCustomAttributeArray(String e, final String ex) {
        try {
            if (!CustomAttributeValidationUtils.isValidCustomAttributeKey(e, this.c.m())) {
                AppboyLogger.w(AppboyUser.a, "Custom attribute key was invalid. Not adding to attribute array.");
                return false;
            }
            if (!CustomAttributeValidationUtils.isValidCustomAttributeValue((String)ex)) {
                return false;
            }
            final String ensureAppboyFieldLength = ValidationUtils.ensureAppboyFieldLength(e);
            try {
                e = (String)cg.e(ensureAppboyFieldLength, ValidationUtils.ensureAppboyFieldLength((String)ex));
                return this.g.a((ca)e);
            }
            catch (Exception ex) {
                e = ensureAppboyFieldLength;
            }
        }
        catch (Exception ex2) {}
        final String a = AppboyUser.a;
        final StringBuilder sb = new StringBuilder();
        sb.append("Failed to add custom attribute with key '");
        sb.append(e);
        sb.append("'.");
        AppboyLogger.w(a, sb.toString(), ex);
        return false;
    }
    
    public String getUserId() {
        synchronized (this.d) {
            return this.f;
        }
    }
    
    public boolean incrementCustomUserAttribute(final String s) {
        return this.incrementCustomUserAttribute(s, 1);
    }
    
    public boolean incrementCustomUserAttribute(String ex, final int n) {
        Serializable ensureAppboyFieldLength;
        try {
            if (!CustomAttributeValidationUtils.isValidCustomAttributeKey((String)ex, this.c.m())) {
                return false;
            }
            ensureAppboyFieldLength = ValidationUtils.ensureAppboyFieldLength((String)ex);
            try {
                return this.g.a(cg.a((String)ensureAppboyFieldLength, n));
            }
            catch (Exception ex) {}
        }
        catch (Exception ex2) {
            ensureAppboyFieldLength = ex;
            ex = ex2;
        }
        final String a = AppboyUser.a;
        final StringBuilder sb = new StringBuilder();
        sb.append("Failed to increment custom attribute ");
        sb.append((String)ensureAppboyFieldLength);
        sb.append(" by ");
        sb.append(n);
        sb.append(".");
        AppboyLogger.w(a, sb.toString(), ex);
        return false;
    }
    
    public boolean removeFromCustomAttributeArray(String f, final String ex) {
        try {
            if (!CustomAttributeValidationUtils.isValidCustomAttributeKey(f, this.c.m())) {
                AppboyLogger.w(AppboyUser.a, "Custom attribute key was invalid. Not removing from attribute array.");
                return false;
            }
            if (!CustomAttributeValidationUtils.isValidCustomAttributeValue((String)ex)) {
                return false;
            }
            final String ensureAppboyFieldLength = ValidationUtils.ensureAppboyFieldLength(f);
            try {
                f = (String)cg.f(ensureAppboyFieldLength, ValidationUtils.ensureAppboyFieldLength((String)ex));
                return this.g.a((ca)f);
            }
            catch (Exception ex) {
                f = ensureAppboyFieldLength;
            }
        }
        catch (Exception ex2) {}
        final String a = AppboyUser.a;
        final StringBuilder sb = new StringBuilder();
        sb.append("Failed to remove custom attribute with key '");
        sb.append(f);
        sb.append("'.");
        AppboyLogger.w(a, sb.toString(), ex);
        return false;
    }
    
    public boolean setAttributionData(final AttributionData attributionData) {
        try {
            this.b.a(attributionData);
            return true;
        }
        catch (Exception ex) {
            AppboyLogger.w(AppboyUser.a, "Failed to set attribution data.", ex);
            return false;
        }
    }
    
    public boolean setAvatarImageUrl(final String s) {
        try {
            this.b.i(s);
            return true;
        }
        catch (Exception ex) {
            AppboyLogger.w(AppboyUser.a, "Failed to set the avatar image URL.", ex);
            return false;
        }
    }
    
    public boolean setCountry(final String s) {
        try {
            this.b.e(s);
            return true;
        }
        catch (Exception ex) {
            final String a = AppboyUser.a;
            final StringBuilder sb = new StringBuilder();
            sb.append("Failed to set country to: ");
            sb.append(s);
            AppboyLogger.w(a, sb.toString(), ex);
            return false;
        }
    }
    
    public boolean setCustomAttributeArray(final String p0, final String[] p1) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: aload_0        
        //     2: getfield        com/appboy/AppboyUser.c:Lbo/app/dr;
        //     5: invokevirtual   bo/app/dr.m:()Ljava/util/Set;
        //     8: invokestatic    com/appboy/support/CustomAttributeValidationUtils.isValidCustomAttributeKey:(Ljava/lang/String;Ljava/util/Set;)Z
        //    11: ifne            16
        //    14: iconst_0       
        //    15: ireturn        
        //    16: aload_1        
        //    17: invokestatic    com/appboy/support/ValidationUtils.ensureAppboyFieldLength:(Ljava/lang/String;)Ljava/lang/String;
        //    20: astore          4
        //    22: aload_2        
        //    23: astore_1       
        //    24: aload_2        
        //    25: ifnull          36
        //    28: aload_2        
        //    29: invokestatic    com/appboy/support/CustomAttributeValidationUtils.ensureCustomAttributeArrayValues:([Ljava/lang/String;)[Ljava/lang/String;
        //    32: astore_1       
        //    33: goto            36
        //    36: aload           4
        //    38: aload_1        
        //    39: invokestatic    bo/app/cg.a:(Ljava/lang/String;[Ljava/lang/String;)Lbo/app/cg;
        //    42: astore_1       
        //    43: aload_0        
        //    44: getfield        com/appboy/AppboyUser.g:Lbo/app/br;
        //    47: aload_1        
        //    48: invokeinterface bo/app/br.a:(Lbo/app/ca;)Z
        //    53: istore_3       
        //    54: iload_3        
        //    55: ireturn        
        //    56: astore_2       
        //    57: getstatic       com/appboy/AppboyUser.a:Ljava/lang/String;
        //    60: astore_2       
        //    61: new             Ljava/lang/StringBuilder;
        //    64: dup            
        //    65: invokespecial   java/lang/StringBuilder.<init>:()V
        //    68: astore          4
        //    70: aload           4
        //    72: ldc             "Failed to set custom attribute array with key: '"
        //    74: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    77: pop            
        //    78: aload           4
        //    80: aload_1        
        //    81: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    84: pop            
        //    85: aload           4
        //    87: ldc             "'."
        //    89: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    92: pop            
        //    93: aload_2        
        //    94: aload           4
        //    96: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //    99: invokestatic    com/appboy/support/AppboyLogger.w:(Ljava/lang/String;Ljava/lang/String;)I
        //   102: pop            
        //   103: iconst_0       
        //   104: ireturn        
        //   105: astore_1       
        //   106: aload           4
        //   108: astore_1       
        //   109: goto            57
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  0      14     56     57     Ljava/lang/Exception;
        //  16     22     56     57     Ljava/lang/Exception;
        //  28     33     105    112    Ljava/lang/Exception;
        //  36     54     105    112    Ljava/lang/Exception;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0036:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2596)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at us.deathmarine.luyten.FileSaver.doSaveJarDecompiled(FileSaver.java:192)
        //     at us.deathmarine.luyten.FileSaver.access$300(FileSaver.java:45)
        //     at us.deathmarine.luyten.FileSaver$4.run(FileSaver.java:112)
        //     at java.lang.Thread.run(Unknown Source)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public boolean setCustomUserAttribute(final String s, final double n) {
        try {
            return this.b.a(s, n);
        }
        catch (Exception ex) {
            final String a = AppboyUser.a;
            final StringBuilder sb = new StringBuilder();
            sb.append("Failed to set custom double attribute ");
            sb.append(s);
            sb.append(".");
            AppboyLogger.w(a, sb.toString(), ex);
            return false;
        }
    }
    
    public boolean setCustomUserAttribute(final String s, final float n) {
        try {
            return this.b.a(s, n);
        }
        catch (Exception ex) {
            final String a = AppboyUser.a;
            final StringBuilder sb = new StringBuilder();
            sb.append("Failed to set custom float attribute ");
            sb.append(s);
            sb.append(".");
            AppboyLogger.w(a, sb.toString(), ex);
            return false;
        }
    }
    
    public boolean setCustomUserAttribute(final String s, final int n) {
        try {
            return this.b.a(s, (Object)n);
        }
        catch (Exception ex) {
            final String a = AppboyUser.a;
            final StringBuilder sb = new StringBuilder();
            sb.append("Failed to set custom integer attribute ");
            sb.append(s);
            sb.append(".");
            AppboyLogger.w(a, sb.toString(), ex);
            return false;
        }
    }
    
    public boolean setCustomUserAttribute(final String s, final long n) {
        try {
            return this.b.a(s, (Object)n);
        }
        catch (Exception ex) {
            final String a = AppboyUser.a;
            final StringBuilder sb = new StringBuilder();
            sb.append("Failed to set custom long attribute ");
            sb.append(s);
            sb.append(".");
            AppboyLogger.w(a, sb.toString(), ex);
            return false;
        }
    }
    
    public boolean setCustomUserAttribute(final String s, final String s2) {
        try {
            return this.b.a(s, s2);
        }
        catch (Exception ex) {
            final String a = AppboyUser.a;
            final StringBuilder sb = new StringBuilder();
            sb.append("Failed to set custom string attribute ");
            sb.append(s);
            sb.append(".");
            AppboyLogger.w(a, sb.toString(), ex);
            return false;
        }
    }
    
    public boolean setCustomUserAttribute(final String s, final boolean b) {
        try {
            return this.b.a(s, (Object)b);
        }
        catch (Exception ex) {
            final String a = AppboyUser.a;
            final StringBuilder sb = new StringBuilder();
            sb.append("Failed to set custom boolean attribute ");
            sb.append(s);
            sb.append(".");
            AppboyLogger.w(a, sb.toString(), ex);
            return false;
        }
    }
    
    public boolean setCustomUserAttributeToNow(final String s) {
        try {
            return this.b.a(s, du.a());
        }
        catch (Exception ex) {
            final String a = AppboyUser.a;
            final StringBuilder sb = new StringBuilder();
            sb.append("Failed to set custom attribute ");
            sb.append(s);
            sb.append(" to now.");
            AppboyLogger.w(a, sb.toString(), ex);
            return false;
        }
    }
    
    public boolean setCustomUserAttributeToSecondsFromEpoch(final String s, final long n) {
        try {
            return this.b.a(s, n);
        }
        catch (Exception ex) {
            final String a = AppboyUser.a;
            final StringBuilder sb = new StringBuilder();
            sb.append("Failed to set custom attribute ");
            sb.append(s);
            sb.append(" to ");
            sb.append(n);
            sb.append(" seconds from epoch.");
            AppboyLogger.w(a, sb.toString(), ex);
            return false;
        }
    }
    
    public boolean setDateOfBirth(final int n, final Month month, final int n2) {
        try {
            return this.b.a(n, month, n2);
        }
        catch (Exception ex) {
            final String a = AppboyUser.a;
            final StringBuilder sb = new StringBuilder();
            sb.append("Failed to set date of birth to: ");
            sb.append(n);
            sb.append("-");
            sb.append(month.getValue());
            sb.append("-");
            sb.append(n2);
            AppboyLogger.w(a, sb.toString(), ex);
            return false;
        }
    }
    
    public boolean setEmail(final String s) {
        try {
            return this.b.d(s);
        }
        catch (Exception ex) {
            final String a = AppboyUser.a;
            final StringBuilder sb = new StringBuilder();
            sb.append("Failed to set email to: ");
            sb.append(s);
            AppboyLogger.w(a, sb.toString(), ex);
            return false;
        }
    }
    
    public boolean setEmailNotificationSubscriptionType(final NotificationSubscriptionType notificationSubscriptionType) {
        try {
            this.b.a(notificationSubscriptionType);
            return true;
        }
        catch (Exception ex) {
            final String a = AppboyUser.a;
            final StringBuilder sb = new StringBuilder();
            sb.append("Failed to set email notification subscription to: ");
            sb.append(notificationSubscriptionType);
            AppboyLogger.w(a, sb.toString(), ex);
            return false;
        }
    }
    
    public boolean setFacebookData(final FacebookUser facebookUser) {
        try {
            this.b.a(facebookUser);
            return true;
        }
        catch (Exception ex) {
            AppboyLogger.w(AppboyUser.a, "Failed to set Facebook user data.", ex);
            return false;
        }
    }
    
    public boolean setFirstName(final String s) {
        try {
            this.b.b(s);
            return true;
        }
        catch (Exception ex) {
            final String a = AppboyUser.a;
            final StringBuilder sb = new StringBuilder();
            sb.append("Failed to set first name to: ");
            sb.append(s);
            AppboyLogger.w(a, sb.toString(), ex);
            return false;
        }
    }
    
    public boolean setGender(final Gender gender) {
        try {
            this.b.a(gender);
            return true;
        }
        catch (Exception ex) {
            final String a = AppboyUser.a;
            final StringBuilder sb = new StringBuilder();
            sb.append("Failed to set gender to: ");
            sb.append(gender);
            AppboyLogger.w(a, sb.toString(), ex);
            return false;
        }
    }
    
    public boolean setHomeCity(final String s) {
        try {
            this.b.f(s);
            return true;
        }
        catch (Exception ex) {
            final String a = AppboyUser.a;
            final StringBuilder sb = new StringBuilder();
            sb.append("Failed to set home city to: ");
            sb.append(s);
            AppboyLogger.w(a, sb.toString(), ex);
            return false;
        }
    }
    
    public boolean setLanguage(final String s) {
        try {
            this.b.g(s);
            return true;
        }
        catch (Exception ex) {
            final String a = AppboyUser.a;
            final StringBuilder sb = new StringBuilder();
            sb.append("Failed to set language to: ");
            sb.append(s);
            AppboyLogger.w(a, sb.toString(), ex);
            return false;
        }
    }
    
    public void setLastKnownLocation(final double n, final double n2, final Double n3, final Double n4) {
        try {
            this.e.a(new ch(n, n2, n3, n4));
        }
        catch (Exception ex) {
            AppboyLogger.w(AppboyUser.a, "Failed to manually set location.", ex);
        }
    }
    
    public boolean setLastName(final String s) {
        try {
            this.b.c(s);
            return true;
        }
        catch (Exception ex) {
            final String a = AppboyUser.a;
            final StringBuilder sb = new StringBuilder();
            sb.append("Failed to set last name to: ");
            sb.append(s);
            AppboyLogger.w(a, sb.toString(), ex);
            return false;
        }
    }
    
    public void setLocationCustomAttribute(String ex, final double n, final double n2) {
        Serializable ensureAppboyFieldLength;
        try {
            if (!CustomAttributeValidationUtils.isValidCustomAttributeKey((String)ex, this.c.m())) {
                AppboyLogger.w(AppboyUser.a, "Custom location attribute key was invalid. Not setting attribute.");
                return;
            }
            if (!ValidationUtils.isValidLocation(n, n2)) {
                final String a = AppboyUser.a;
                final StringBuilder sb = new StringBuilder();
                sb.append("Cannot set custom location attribute due with invalid latitude '");
                sb.append(n);
                sb.append(" and longitude '");
                sb.append(n2);
                sb.append("'");
                AppboyLogger.w(a, sb.toString());
                return;
            }
            ensureAppboyFieldLength = ValidationUtils.ensureAppboyFieldLength((String)ex);
            try {
                this.g.a(cg.a((String)ensureAppboyFieldLength, n, n2));
                return;
            }
            catch (Exception ex) {}
        }
        catch (Exception ex2) {
            ensureAppboyFieldLength = ex;
            ex = ex2;
        }
        final String a2 = AppboyUser.a;
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("Failed to set custom location attribute with key '");
        sb2.append((String)ensureAppboyFieldLength);
        sb2.append("' and latitude '");
        sb2.append(n);
        sb2.append("' and longitude '");
        sb2.append(n2);
        sb2.append("'");
        AppboyLogger.w(a2, sb2.toString(), ex);
    }
    
    public boolean setPhoneNumber(final String s) {
        try {
            return this.b.h(s);
        }
        catch (Exception ex) {
            final String a = AppboyUser.a;
            final StringBuilder sb = new StringBuilder();
            sb.append("Failed to set phone number to: ");
            sb.append(s);
            AppboyLogger.w(a, sb.toString(), ex);
            return false;
        }
    }
    
    public boolean setPushNotificationSubscriptionType(final NotificationSubscriptionType notificationSubscriptionType) {
        try {
            this.b.b(notificationSubscriptionType);
            return true;
        }
        catch (Exception ex) {
            final String a = AppboyUser.a;
            final StringBuilder sb = new StringBuilder();
            sb.append("Failed to set push notification subscription to: ");
            sb.append(notificationSubscriptionType);
            AppboyLogger.w(a, sb.toString(), ex);
            return false;
        }
    }
    
    public boolean setTwitterData(final TwitterUser twitterUser) {
        try {
            this.b.a(twitterUser);
            return true;
        }
        catch (Exception ex) {
            AppboyLogger.w(AppboyUser.a, "Failed to set Twitter user data.", ex);
            return false;
        }
    }
    
    public boolean unsetCustomUserAttribute(final String s) {
        try {
            return this.b.j(s);
        }
        catch (Exception ex) {
            final String a = AppboyUser.a;
            final StringBuilder sb = new StringBuilder();
            sb.append("Failed to unset custom attribute ");
            sb.append(s);
            sb.append(".");
            AppboyLogger.w(a, sb.toString(), ex);
            return false;
        }
    }
    
    public void unsetLocationCustomAttribute(String ex) {
        Serializable ensureAppboyFieldLength;
        try {
            if (!CustomAttributeValidationUtils.isValidCustomAttributeKey((String)ex, this.c.m())) {
                AppboyLogger.w(AppboyUser.a, "Custom location attribute key was invalid. Not setting attribute.");
                return;
            }
            ensureAppboyFieldLength = ValidationUtils.ensureAppboyFieldLength((String)ex);
            try {
                this.g.a(cg.k((String)ensureAppboyFieldLength));
                return;
            }
            catch (Exception ex) {}
        }
        catch (Exception ex2) {
            ensureAppboyFieldLength = ex;
            ex = ex2;
        }
        final String a = AppboyUser.a;
        final StringBuilder sb = new StringBuilder();
        sb.append("Failed to unset custom location attribute with key '");
        sb.append((String)ensureAppboyFieldLength);
        sb.append("'");
        AppboyLogger.w(a, sb.toString(), ex);
    }
}
