package org.spongycastle.i18n;

import java.nio.charset.*;
import java.io.*;
import java.text.*;
import java.util.*;
import org.spongycastle.i18n.filter.*;

public class LocalizedMessage
{
    public static final String DEFAULT_ENCODING = "ISO-8859-1";
    protected FilteredArguments arguments;
    protected String encoding;
    protected FilteredArguments extraArgs;
    protected Filter filter;
    protected final String id;
    protected ClassLoader loader;
    protected final String resource;
    
    public LocalizedMessage(final String resource, final String id) throws NullPointerException {
        this.encoding = "ISO-8859-1";
        this.extraArgs = null;
        this.filter = null;
        this.loader = null;
        if (resource != null && id != null) {
            this.id = id;
            this.resource = resource;
            this.arguments = new FilteredArguments();
            return;
        }
        throw null;
    }
    
    public LocalizedMessage(final String resource, final String id, final String encoding) throws NullPointerException, UnsupportedEncodingException {
        this.encoding = "ISO-8859-1";
        this.extraArgs = null;
        this.filter = null;
        this.loader = null;
        if (resource == null || id == null) {
            throw null;
        }
        this.id = id;
        this.resource = resource;
        this.arguments = new FilteredArguments();
        if (Charset.isSupported(encoding)) {
            this.encoding = encoding;
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("The encoding \"");
        sb.append(encoding);
        sb.append("\" is not supported.");
        throw new UnsupportedEncodingException(sb.toString());
    }
    
    public LocalizedMessage(final String resource, final String id, final String encoding, final Object[] array) throws NullPointerException, UnsupportedEncodingException {
        this.encoding = "ISO-8859-1";
        this.extraArgs = null;
        this.filter = null;
        this.loader = null;
        if (resource == null || id == null || array == null) {
            throw null;
        }
        this.id = id;
        this.resource = resource;
        this.arguments = new FilteredArguments(array);
        if (Charset.isSupported(encoding)) {
            this.encoding = encoding;
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("The encoding \"");
        sb.append(encoding);
        sb.append("\" is not supported.");
        throw new UnsupportedEncodingException(sb.toString());
    }
    
    public LocalizedMessage(final String resource, final String id, final Object[] array) throws NullPointerException {
        this.encoding = "ISO-8859-1";
        this.extraArgs = null;
        this.filter = null;
        this.loader = null;
        if (resource != null && id != null && array != null) {
            this.id = id;
            this.resource = resource;
            this.arguments = new FilteredArguments(array);
            return;
        }
        throw null;
    }
    
    protected String addExtraArgs(final String s, final Locale locale) {
        String string = s;
        if (this.extraArgs != null) {
            final StringBuffer sb = new StringBuffer(s);
            final Object[] filteredArgs = this.extraArgs.getFilteredArgs(locale);
            for (int i = 0; i < filteredArgs.length; ++i) {
                sb.append(filteredArgs[i]);
            }
            string = sb.toString();
        }
        return string;
    }
    
    protected String formatWithTimeZone(final String s, final Object[] array, final Locale locale, final TimeZone timeZone) {
        final MessageFormat messageFormat = new MessageFormat(" ");
        messageFormat.setLocale(locale);
        messageFormat.applyPattern(s);
        if (!timeZone.equals(TimeZone.getDefault())) {
            final Format[] formats = messageFormat.getFormats();
            for (int i = 0; i < formats.length; ++i) {
                if (formats[i] instanceof DateFormat) {
                    final DateFormat dateFormat = (DateFormat)formats[i];
                    dateFormat.setTimeZone(timeZone);
                    messageFormat.setFormat(i, dateFormat);
                }
            }
        }
        return messageFormat.format(array);
    }
    
    public Object[] getArguments() {
        return this.arguments.getArguments();
    }
    
    public ClassLoader getClassLoader() {
        return this.loader;
    }
    
    public String getEntry(String o, final Locale locale, final TimeZone timeZone) throws MissingEntryException {
        String s2;
        final String s = s2 = this.id;
        if (o != null) {
            final StringBuilder sb = new StringBuilder();
            sb.append(s);
            sb.append(".");
            sb.append((String)o);
            s2 = sb.toString();
        }
        try {
            ResourceBundle resourceBundle;
            if (this.loader == null) {
                resourceBundle = ResourceBundle.getBundle(this.resource, locale);
            }
            else {
                resourceBundle = ResourceBundle.getBundle(this.resource, locale, this.loader);
            }
            final String s3 = (String)(o = resourceBundle.getString(s2));
            if (!this.encoding.equals("ISO-8859-1")) {
                o = new String(s3.getBytes("ISO-8859-1"), this.encoding);
            }
            Object formatWithTimeZone = o;
            if (!this.arguments.isEmpty()) {
                formatWithTimeZone = this.formatWithTimeZone((String)o, this.arguments.getFilteredArgs(locale), locale, timeZone);
            }
            o = this.addExtraArgs((String)formatWithTimeZone, locale);
            return (String)o;
        }
        catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
        catch (MissingResourceException ex2) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Can't find entry ");
            sb2.append(s2);
            sb2.append(" in resource file ");
            sb2.append(this.resource);
            sb2.append(".");
            final String string = sb2.toString();
            final String resource = this.resource;
            ClassLoader classLoader = this.loader;
            if (classLoader == null) {
                classLoader = this.getClassLoader();
            }
            throw new MissingEntryException(string, resource, s2, locale, classLoader);
        }
    }
    
    public Object[] getExtraArgs() {
        final FilteredArguments extraArgs = this.extraArgs;
        if (extraArgs == null) {
            return null;
        }
        return extraArgs.getArguments();
    }
    
    public Filter getFilter() {
        return this.filter;
    }
    
    public String getId() {
        return this.id;
    }
    
    public String getResource() {
        return this.resource;
    }
    
    public void setClassLoader(final ClassLoader loader) {
        this.loader = loader;
    }
    
    public void setExtraArgument(final Object o) {
        this.setExtraArguments(new Object[] { o });
    }
    
    public void setExtraArguments(final Object[] array) {
        if (array != null) {
            (this.extraArgs = new FilteredArguments(array)).setFilter(this.filter);
            return;
        }
        this.extraArgs = null;
    }
    
    public void setFilter(final Filter filter) {
        this.arguments.setFilter(filter);
        final FilteredArguments extraArgs = this.extraArgs;
        if (extraArgs != null) {
            extraArgs.setFilter(filter);
        }
        this.filter = filter;
    }
    
    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append("Resource: \"");
        sb.append(this.resource);
        sb.append("\" Id: \"");
        sb.append(this.id);
        sb.append("\"");
        sb.append(" Arguments: ");
        sb.append(this.arguments.getArguments().length);
        sb.append(" normal");
        final FilteredArguments extraArgs = this.extraArgs;
        if (extraArgs != null && extraArgs.getArguments().length > 0) {
            sb.append(", ");
            sb.append(this.extraArgs.getArguments().length);
            sb.append(" extra");
        }
        sb.append(" Encoding: ");
        sb.append(this.encoding);
        sb.append(" ClassLoader: ");
        sb.append(this.loader);
        return sb.toString();
    }
    
    protected class FilteredArguments
    {
        protected static final int FILTER = 1;
        protected static final int FILTER_URL = 2;
        protected static final int NO_FILTER = 0;
        protected int[] argFilterType;
        protected Object[] arguments;
        protected Filter filter;
        protected Object[] filteredArgs;
        protected boolean[] isLocaleSpecific;
        protected Object[] unpackedArgs;
        
        FilteredArguments(final LocalizedMessage localizedMessage) {
            this(localizedMessage, new Object[0]);
        }
        
        FilteredArguments(final Object[] arguments) {
            this.filter = null;
            this.arguments = arguments;
            this.unpackedArgs = new Object[arguments.length];
            this.filteredArgs = new Object[arguments.length];
            this.isLocaleSpecific = new boolean[arguments.length];
            this.argFilterType = new int[arguments.length];
            for (int i = 0; i < arguments.length; ++i) {
                if (arguments[i] instanceof TrustedInput) {
                    this.unpackedArgs[i] = ((TrustedInput)arguments[i]).getInput();
                    this.argFilterType[i] = 0;
                }
                else if (arguments[i] instanceof UntrustedInput) {
                    this.unpackedArgs[i] = ((UntrustedInput)arguments[i]).getInput();
                    if (arguments[i] instanceof UntrustedUrlInput) {
                        this.argFilterType[i] = 2;
                    }
                    else {
                        this.argFilterType[i] = 1;
                    }
                }
                else {
                    this.unpackedArgs[i] = arguments[i];
                    this.argFilterType[i] = 1;
                }
                this.isLocaleSpecific[i] = (this.unpackedArgs[i] instanceof LocaleString);
            }
        }
        
        private Object filter(final int n, final Object o) {
            Object o2 = o;
            if (this.filter != null) {
                Object o3;
                if ((o3 = o) == null) {
                    o3 = "null";
                }
                o2 = o3;
                if (n != 0) {
                    if (n == 1) {
                        return this.filter.doFilter(o3.toString());
                    }
                    if (n != 2) {
                        return null;
                    }
                    return this.filter.doFilterUrl(o3.toString());
                }
            }
            return o2;
        }
        
        public Object[] getArguments() {
            return this.arguments;
        }
        
        public Filter getFilter() {
            return this.filter;
        }
        
        public Object[] getFilteredArgs(final Locale locale) {
            final Object[] array = new Object[this.unpackedArgs.length];
            int n = 0;
            while (true) {
                final Object[] unpackedArgs = this.unpackedArgs;
                if (n >= unpackedArgs.length) {
                    break;
                }
                final Object[] filteredArgs = this.filteredArgs;
                Object o;
                if (filteredArgs[n] != null) {
                    o = filteredArgs[n];
                }
                else {
                    final Object o2 = unpackedArgs[n];
                    if (this.isLocaleSpecific[n]) {
                        o = this.filter(this.argFilterType[n], ((LocaleString)o2).getLocaleString(locale));
                    }
                    else {
                        o = this.filter(this.argFilterType[n], o2);
                        this.filteredArgs[n] = o;
                    }
                }
                array[n] = o;
                ++n;
            }
            return array;
        }
        
        public boolean isEmpty() {
            return this.unpackedArgs.length == 0;
        }
        
        public void setFilter(final Filter filter) {
            if (filter != this.filter) {
                for (int i = 0; i < this.unpackedArgs.length; ++i) {
                    this.filteredArgs[i] = null;
                }
            }
            this.filter = filter;
        }
    }
}
