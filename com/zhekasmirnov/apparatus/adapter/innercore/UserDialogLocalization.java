package com.zhekasmirnov.apparatus.adapter.innercore;

import com.zhekasmirnov.horizon.*;
import org.mineprogramming.horizon.innercore.inflater.*;
import android.content.*;
import java.util.regex.*;

public class UserDialogLocalization
{
    public static String getLocalizedString(final String s) {
        return JsonInflater.getString((Context)HorizonApplication.getTopRunningActivity(), s);
    }
    
    public static String localize(final String s) {
        final Matcher matcher = Pattern.compile("\\{\\{loc:\\s*([A-Za-z_]+)\\s*\\}\\}").matcher(s);
        String replace = s;
        while (matcher.find()) {
            replace = replace.replace(s.substring(matcher.start(), matcher.end()), getLocalizedString(matcher.group(1)));
        }
        return replace;
    }
}
