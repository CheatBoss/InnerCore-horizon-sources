package com.microsoft.aad.adal;

import java.util.concurrent.*;
import android.content.*;
import java.net.*;
import android.os.*;
import java.io.*;
import java.util.regex.*;
import java.util.*;

public class AuthenticationParameters
{
    public static final String AUTHENTICATE_HEADER = "WWW-Authenticate";
    public static final String AUTHORITY_KEY = "authorization_uri";
    public static final String AUTH_HEADER_INVALID_FORMAT = "Invalid authentication header format";
    public static final String AUTH_HEADER_MISSING = "WWW-Authenticate header was expected in the response";
    public static final String AUTH_HEADER_MISSING_AUTHORITY = "WWW-Authenticate header is missing authorization_uri.";
    public static final String AUTH_HEADER_WRONG_STATUS = "Unauthorized http response (status code 401) was expected";
    public static final String BEARER = "bearer";
    public static final String RESOURCE_KEY = "resource_id";
    private static final String TAG = "AuthenticationParameters";
    private static ExecutorService sThreadExecutor;
    private static IWebRequestHandler sWebRequest;
    private String mAuthority;
    private String mResource;
    
    static {
        AuthenticationParameters.sWebRequest = new WebRequestHandler();
        AuthenticationParameters.sThreadExecutor = Executors.newSingleThreadExecutor();
    }
    
    public AuthenticationParameters() {
    }
    
    AuthenticationParameters(final String mAuthority, final String mResource) {
        this.mAuthority = mAuthority;
        this.mResource = mResource;
    }
    
    public static void createFromResourceUrl(final Context context, final URL url, final AuthenticationParamCallback authenticationParamCallback) {
        if (authenticationParamCallback != null) {
            Logger.v("AuthenticationParameters", "createFromResourceUrl");
            AuthenticationParameters.sThreadExecutor.submit(new Runnable() {
                final /* synthetic */ Handler val$handler = new Handler(context.getMainLooper());
                
                void onCompleted(final Exception ex, final AuthenticationParameters authenticationParameters) {
                    this.val$handler.post((Runnable)new Runnable() {
                        @Override
                        public void run() {
                            authenticationParamCallback.onCompleted(ex, authenticationParameters);
                        }
                    });
                }
                
                @Override
                public void run() {
                    final HashMap<String, String> hashMap = new HashMap<String, String>();
                    hashMap.put("Accept", "application/json");
                    try {
                        final HttpWebResponse sendGet = AuthenticationParameters.sWebRequest.sendGet(url, hashMap);
                        try {
                            this.onCompleted(null, parseResponse(sendGet));
                        }
                        catch (ResourceAuthenticationChallengeException ex) {
                            this.onCompleted(ex, null);
                        }
                    }
                    catch (IOException ex2) {
                        this.onCompleted(ex2, null);
                    }
                }
            });
            return;
        }
        throw new IllegalArgumentException("callback");
    }
    
    public static AuthenticationParameters createFromResponseAuthenticateHeader(String replaceAll) throws ResourceAuthenticationChallengeException {
        if (!StringExtensions.isNullOrBlank(replaceAll)) {
            Logger.v("AuthenticationParameters:createFromResponseAuthenticateHeader", "Parsing challenges - BEGIN");
            final List<Challenge> challenges = Challenge.parseChallenges(replaceAll);
            Logger.v("AuthenticationParameters:createFromResponseAuthenticateHeader", "Parsing challenge - END");
            final Challenge challenge = null;
            Logger.v("AuthenticationParameters:createFromResponseAuthenticateHeader", "Looking for Bearer challenge.");
            final Iterator<Challenge> iterator = challenges.iterator();
            while (true) {
                Challenge challenge2;
                do {
                    challenge2 = challenge;
                    if (iterator.hasNext()) {
                        challenge2 = iterator.next();
                    }
                    else {
                        if (challenge2 == null) {
                            Logger.w("AuthenticationParameters:createFromResponseAuthenticateHeader", "Did not locate Bearer challenge.");
                            throw new ResourceAuthenticationChallengeException("Invalid authentication header format");
                        }
                        final Map<String, String> parameters = challenge2.getParameters();
                        replaceAll = parameters.get("authorization_uri");
                        final String s = parameters.get("resource_id");
                        final StringBuilder sb = new StringBuilder();
                        sb.append("[");
                        sb.append(replaceAll);
                        sb.append("]");
                        Logger.i("AuthenticationParameters:createFromResponseAuthenticateHeader", "Bearer authority", sb.toString());
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append("[");
                        sb2.append(s);
                        sb2.append("]");
                        Logger.i("AuthenticationParameters:createFromResponseAuthenticateHeader", "Bearer resource", sb2.toString());
                        if (StringExtensions.isNullOrBlank(replaceAll)) {
                            Logger.w("AuthenticationParameters:createFromResponseAuthenticateHeader", "Null/empty authority.");
                            throw new ResourceAuthenticationChallengeException("WWW-Authenticate header is missing authorization_uri.");
                        }
                        Logger.v("AuthenticationParameters:createFromResponseAuthenticateHeader", "Parsing leading/trailing \"\"'s (authority)");
                        final String replaceAll2 = replaceAll.replaceAll("^\"|\"$", "");
                        final StringBuilder sb3 = new StringBuilder();
                        sb3.append("[");
                        sb3.append(replaceAll2);
                        sb3.append("]");
                        Logger.i("AuthenticationParameters:createFromResponseAuthenticateHeader", "Sanitized authority value", sb3.toString());
                        if (!StringExtensions.isNullOrBlank(replaceAll2)) {
                            replaceAll = s;
                            if (!StringExtensions.isNullOrBlank(s)) {
                                Logger.v("AuthenticationParameters:createFromResponseAuthenticateHeader", "Parsing leading/trailing \"\"'s (resource)");
                                replaceAll = s.replaceAll("^\"|\"$", "");
                                final StringBuilder sb4 = new StringBuilder();
                                sb4.append("[");
                                sb4.append(replaceAll2);
                                sb4.append("]");
                                Logger.i("AuthenticationParameters:createFromResponseAuthenticateHeader", "Sanitized resource value", sb4.toString());
                            }
                            return new AuthenticationParameters(replaceAll2, replaceAll);
                        }
                        Logger.w("AuthenticationParameters:createFromResponseAuthenticateHeader", "Sanitized authority is null/empty.");
                        throw new ResourceAuthenticationChallengeException("WWW-Authenticate header is missing authorization_uri.");
                    }
                } while (!"bearer".equalsIgnoreCase(challenge2.getScheme()));
                Logger.v("AuthenticationParameters:createFromResponseAuthenticateHeader", "Found Bearer challenge.");
                continue;
            }
        }
        Logger.w("AuthenticationParameters:createFromResponseAuthenticateHeader", "authenticateHeader was null/empty.");
        throw new ResourceAuthenticationChallengeException("WWW-Authenticate header was expected in the response");
    }
    
    private static AuthenticationParameters parseResponse(final HttpWebResponse httpWebResponse) throws ResourceAuthenticationChallengeException {
        if (httpWebResponse.getStatusCode() == 401) {
            final Map<String, List<String>> responseHeaders = httpWebResponse.getResponseHeaders();
            if (responseHeaders != null && responseHeaders.containsKey("WWW-Authenticate")) {
                final List<String> list = responseHeaders.get("WWW-Authenticate");
                if (list != null && list.size() > 0) {
                    return createFromResponseAuthenticateHeader(list.get(0));
                }
            }
            throw new ResourceAuthenticationChallengeException("WWW-Authenticate header was expected in the response");
        }
        throw new ResourceAuthenticationChallengeException("Unauthorized http response (status code 401) was expected");
    }
    
    public String getAuthority() {
        return this.mAuthority;
    }
    
    public String getResource() {
        return this.mResource;
    }
    
    public interface AuthenticationParamCallback
    {
        void onCompleted(final Exception p0, final AuthenticationParameters p1);
    }
    
    private static class Challenge
    {
        private static final String REGEX_SPLIT_UNQUOTED_COMMA = ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";
        private static final String REGEX_SPLIT_UNQUOTED_EQUALS = "=(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";
        private static final String REGEX_STRING_TOKEN_WITH_SCHEME = "^([^\\s|^=]+)[\\s|\\t]+([^=]*=[^=]*)+$";
        private static final String REGEX_UNQUOTED_LOOKAHEAD = "(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";
        private static final String SUFFIX_COMMA = ", ";
        private Map<String, String> mParameters;
        private String mScheme;
        
        private Challenge(final String mScheme, final Map<String, String> mParameters) {
            this.mScheme = mScheme;
            this.mParameters = mParameters;
        }
        
        private static boolean containsScheme(final String s) throws ResourceAuthenticationChallengeException {
            if (!StringExtensions.isNullOrBlank(s)) {
                final StringBuilder sb = new StringBuilder();
                sb.append("input[");
                sb.append(s);
                sb.append("]");
                Logger.i("AuthenticationParameters:containsScheme", "Testing token contains scheme", sb.toString());
                final boolean matches = Pattern.compile("^([^\\s|^=]+)[\\s|\\t]+([^=]*=[^=]*)+$").matcher(s).matches();
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Matches? [");
                sb2.append(matches);
                sb2.append("]");
                Logger.i("AuthenticationParameters:containsScheme", "Testing String contains scheme", sb2.toString());
                return matches;
            }
            Logger.w("AuthenticationParameters:containsScheme", "Null/blank potential scheme token");
            throw new ResourceAuthenticationChallengeException("Invalid authentication header format");
        }
        
        private static List<String> extractTokensContainingScheme(final String[] array) throws ResourceAuthenticationChallengeException {
            final ArrayList<String> list = new ArrayList<String>();
            for (int length = array.length, i = 0; i < length; ++i) {
                final String s = array[i];
                if (containsScheme(s)) {
                    list.add(s);
                }
            }
            return list;
        }
        
        static Challenge parseChallenge(final String s) throws ResourceAuthenticationChallengeException {
            if (!StringExtensions.isNullOrBlank(s)) {
                final String scheme = parseScheme(s);
                final StringBuilder sb = new StringBuilder();
                sb.append("Scheme value [");
                sb.append(scheme);
                sb.append("]");
                Logger.i("AuthenticationParameters:parseChallenge", "Parsing scheme", sb.toString());
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("[");
                sb2.append(s);
                sb2.append("]");
                Logger.i("AuthenticationParameters:parseChallenge", "Removing scheme from source challenge", sb2.toString());
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("Parsing challenge substr. Total length: ");
                sb3.append(s.length());
                sb3.append(" Scheme index: ");
                sb3.append(scheme.length());
                sb3.append(1);
                Logger.v("AuthenticationParameters:parseChallenge", sb3.toString());
                return new Challenge(scheme, parseParams(s.substring(scheme.length() + 1)));
            }
            Logger.w("AuthenticationParameters:parseChallenge", "Cannot parse null/empty challenge.");
            throw new ResourceAuthenticationChallengeException("WWW-Authenticate header was expected in the response");
        }
        
        static List<Challenge> parseChallenges(final String s) throws ResourceAuthenticationChallengeException {
            if (!StringExtensions.isNullOrBlank(s)) {
                final ArrayList<Challenge> list = new ArrayList<Challenge>();
                try {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("input[");
                    sb.append(s);
                    sb.append("]");
                    Logger.i("AuthenticationParameters:parseChallenges", "Separating challenges...", sb.toString());
                    final Iterator<String> iterator = separateChallenges(s).iterator();
                    while (iterator.hasNext()) {
                        list.add(parseChallenge(iterator.next()));
                    }
                    return list;
                }
                catch (Exception ex) {
                    Logger.w("AuthenticationParameters:parseChallenges", "Encountered error during parsing...", ex.getMessage(), null);
                    throw new ResourceAuthenticationChallengeException("Invalid authentication header format");
                }
                catch (ResourceAuthenticationChallengeException ex2) {
                    Logger.w("AuthenticationParameters:parseChallenges", "Encountered error during parsing...", ex2.getMessage(), null);
                    throw ex2;
                }
            }
            Logger.w("AuthenticationParameters:parseChallenges", "Cannot parse empty/blank challenges.");
            throw new ResourceAuthenticationChallengeException("WWW-Authenticate header was expected in the response");
        }
        
        private static Map<String, String> parseParams(final String s) throws ResourceAuthenticationChallengeException {
            if (StringExtensions.isNullOrBlank(s)) {
                Logger.w("AuthenticationParameters:parseParams", "ChallengeSansScheme was null/empty");
                throw new ResourceAuthenticationChallengeException("Invalid authentication header format");
            }
            final HashMap<String, String> hashMap = new HashMap<String, String>();
            final StringBuilder sb = new StringBuilder();
            sb.append("in-value [");
            sb.append(s);
            sb.append("]");
            Logger.i("AuthenticationParameters:parseParams", "Splitting on unquoted commas...", sb.toString());
            final String[] split = s.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("out-value [");
            sb2.append(Arrays.toString(split));
            sb2.append("]");
            Logger.i("AuthenticationParameters:parseParams", "Splitting on unquoted commas...", sb2.toString());
            for (int length = split.length, i = 0; i < length; ++i) {
                final String s2 = split[i];
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("in-value [");
                sb3.append(s2);
                sb3.append("]");
                Logger.i("AuthenticationParameters:parseParams", "Splitting on unquoted equals...", sb3.toString());
                final String[] split2 = s2.split("=(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                final StringBuilder sb4 = new StringBuilder();
                sb4.append("out-value [");
                sb4.append(Arrays.toString(split2));
                sb4.append("]");
                Logger.i("AuthenticationParameters:parseParams", "Splitting on unquoted equals...", sb4.toString());
                if (split2.length != 2) {
                    Logger.w("AuthenticationParameters:parseParams", "Splitting on equals yielded mismatched key/value.");
                    throw new ResourceAuthenticationChallengeException("Invalid authentication header format");
                }
                Logger.v("AuthenticationParameters:parseParams", "Trimming split-string whitespace");
                final String trim = split2[0].trim();
                final String trim2 = split2[1].trim();
                final StringBuilder sb5 = new StringBuilder();
                sb5.append("key[");
                sb5.append(trim);
                sb5.append("]");
                Logger.i("AuthenticationParameters:parseParams", "", sb5.toString());
                final StringBuilder sb6 = new StringBuilder();
                sb6.append("value[");
                sb6.append(trim2);
                sb6.append("]");
                Logger.i("AuthenticationParameters:parseParams", "", sb6.toString());
                if (hashMap.containsKey(trim)) {
                    final StringBuilder sb7 = new StringBuilder();
                    sb7.append("Redundant key: ");
                    sb7.append(trim);
                    Logger.w("AuthenticationParameters", "Key/value pair list contains redundant key. ", sb7.toString(), ADALError.DEVELOPER_BEARER_HEADER_MULTIPLE_ITEMS);
                }
                final StringBuilder sb8 = new StringBuilder();
                sb8.append("put(");
                sb8.append(trim);
                sb8.append(", ");
                sb8.append(trim2);
                sb8.append(")");
                Logger.i("AuthenticationParameters:parseParams", "", sb8.toString());
                hashMap.put(trim, trim2);
            }
            if (!hashMap.isEmpty()) {
                return hashMap;
            }
            Logger.w("AuthenticationParameters:parseParams", "Parsed params were empty.");
            throw new ResourceAuthenticationChallengeException("Invalid authentication header format");
        }
        
        private static String parseScheme(final String s) throws ResourceAuthenticationChallengeException {
            if (StringExtensions.isNullOrBlank(s)) {
                Logger.w("AuthenticationParameters:parseScheme", "Cannot parse an empty/blank challenge");
                throw new ResourceAuthenticationChallengeException("WWW-Authenticate header was expected in the response");
            }
            final int index = s.indexOf(32);
            final int index2 = s.indexOf(9);
            if (index < 0 && index2 < 0) {
                Logger.w("AuthenticationParameters:parseScheme", "Couldn't locate space/tab char - returning input String");
                return s;
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("Parsing scheme with indices: indexOfFirstSpace[");
            sb.append(index);
            sb.append("] indexOfFirstTab[");
            sb.append(index2);
            sb.append("]");
            Logger.v("AuthenticationParameters:parseScheme", sb.toString());
            if (index > -1 && (index < index2 || index2 < 0)) {
                return s.substring(0, index);
            }
            if (index2 > -1 && (index2 < index || index < 0)) {
                return s.substring(0, index2);
            }
            Logger.w("AuthenticationParameters:parseScheme", "Unexpected/malformed/missing scheme.");
            throw new ResourceAuthenticationChallengeException("Invalid authentication header format");
        }
        
        private static void sanitizeParsedSuffixes(final String[] array) {
            for (int i = 0; i < array.length; ++i) {
                if (array[i].endsWith(", ")) {
                    array[i] = array[i].substring(0, array[i].length() - 2);
                }
            }
        }
        
        private static void sanitizeWhitespace(final String[] array) {
            Logger.v("AuthenticationParameters:sanitizeWhitespace", "Sanitizing whitespace");
            for (int i = 0; i < array.length; ++i) {
                array[i] = array[i].trim();
            }
        }
        
        private static List<String> separateChallenges(final String s) throws ResourceAuthenticationChallengeException {
            if (!StringExtensions.isNullOrBlank(s)) {
                final StringBuilder sb = new StringBuilder();
                sb.append("input[");
                sb.append(s);
                sb.append("]");
                Logger.i("AuthenticationParameters:separateChallenges", "Splitting input String on unquoted commas", sb.toString());
                final String[] split = s.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("output[");
                sb2.append(Arrays.toString(split));
                sb2.append("]");
                Logger.i("AuthenticationParameters:separateChallenges", "Splitting input String on unquoted commas", sb2.toString());
                sanitizeWhitespace(split);
                final List<String> tokensContainingScheme = extractTokensContainingScheme(split);
                final int size = tokensContainingScheme.size();
                final String[] array = new String[size];
                for (int i = 0; i < size; ++i) {
                    array[i] = "";
                }
                writeParsedChallenges(split, tokensContainingScheme, array);
                sanitizeParsedSuffixes(array);
                return Arrays.asList(array);
            }
            Logger.w("AuthenticationParameters:separateChallenges", "Input String was null");
            throw new ResourceAuthenticationChallengeException("Invalid authentication header format");
        }
        
        private static void writeParsedChallenges(final String[] array, final List<String> list, final String[] array2) {
            final int length = array.length;
            int n = -1;
            for (int i = 0; i < length; ++i) {
                final String s = array[i];
                if (list.contains(s)) {
                    ++n;
                    final StringBuilder sb = new StringBuilder();
                    sb.append(s);
                    sb.append(", ");
                    array2[n] = sb.toString();
                }
                else {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append(array2[n]);
                    sb2.append(s);
                    sb2.append(", ");
                    array2[n] = sb2.toString();
                }
            }
        }
        
        public Map<String, String> getParameters() {
            return this.mParameters;
        }
        
        public String getScheme() {
            return this.mScheme;
        }
    }
}
