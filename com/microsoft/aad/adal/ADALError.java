package com.microsoft.aad.adal;

import android.content.*;
import android.content.res.*;

public enum ADALError
{
    ACTIVITY_REQUEST_INTENT_DATA_IS_NULL("Request object is null"), 
    ANDROIDKEYSTORE_FAILED("Failed to use AndroidKeyStore"), 
    ANDROIDKEYSTORE_KEYPAIR_GENERATOR_FAILED("Failed to use KeyPairGeneratorSpec"), 
    APP_PACKAGE_NAME_NOT_FOUND("App package name is not found in the package manager"), 
    ARGUMENT_EXCEPTION("Invalid argument"), 
    AUTHORIZATION_CODE_NOT_EXCHANGED_FOR_TOKEN("Authorization code not exchanged for token"), 
    AUTH_FAILED("Authorization failed"), 
    AUTH_FAILED_BAD_STATE("The authorization server response has no encoded state"), 
    AUTH_FAILED_CANCELLED("The user cancelled the authorization request"), 
    AUTH_FAILED_INTERNAL_ERROR("Invalid parameters for authorization operation"), 
    AUTH_FAILED_NO_RESOURCES("The required resource bundle could not be loaded"), 
    AUTH_FAILED_NO_STATE("The authorization server response has incorrectly encoded state"), 
    AUTH_FAILED_NO_TOKEN("The requested access token could not be found"), 
    AUTH_FAILED_SERVER_ERROR("The Authorization Server returned an unrecognized response"), 
    AUTH_FAILED_USER_MISMATCH("User returned by service does not match the one in the request"), 
    AUTH_REFRESH_FAILED("Refresh token request failed"), 
    AUTH_REFRESH_FAILED_PROMPT_NOT_ALLOWED("Refresh token is failed and prompt is not allowed"), 
    BROADCAST_CANCEL_NOT_SUCCESSFUL("Cancel message is not successfully delivered to broadcast receiver."), 
    BROADCAST_RECEIVER_ERROR("Broadcast receiver has an error"), 
    BROKER_ACCOUNT_DOES_NOT_EXIST("Broker account does not exist"), 
    BROKER_ACCOUNT_SAVE_FAILED("Broker could not save the new account"), 
    BROKER_ACTIVITY_INFO_NOT_FOUND("Activity information is not retrieved"), 
    BROKER_ACTIVITY_INVALID_REQUEST("Invalid request parameters"), 
    BROKER_ACTIVITY_IS_NOT_RESOLVED("Broker activity is not resolved"), 
    BROKER_APP_INSTALLATION_STARTED("Broker app installation started"), 
    BROKER_APP_VERIFICATION_FAILED("Calling app could not be verified"), 
    BROKER_AUTHENTICATION_REQUEST_IS_NULL("Authentication request is null"), 
    BROKER_AUTHENTICATOR_BAD_ARGUMENTS("Invalid arguments for Authenticator request"), 
    BROKER_AUTHENTICATOR_BAD_AUTHENTICATION("Authentication request failed"), 
    BROKER_AUTHENTICATOR_ERROR_GETAUTHTOKEN("Authenticator error"), 
    BROKER_AUTHENTICATOR_EXCEPTION("Authenticator has an Exception"), 
    BROKER_AUTHENTICATOR_IO_EXCEPTION("Authenticator has IO Exception"), 
    BROKER_AUTHENTICATOR_NOT_RESPONDING("Authenticator is not responding"), 
    BROKER_AUTHENTICATOR_UNSUPPORTED_OPERATION("Authenticator is not supporting this operation"), 
    BROKER_BIND_SERVICE_FAILED("Failed to bind the service in broker app"), 
    BROKER_PACKAGE_NAME_NOT_FOUND("Broker is not installed in your system"), 
    BROKER_SIGNATURE_NOT_SAVED("Signature is not saved"), 
    BROKER_SINGLE_USER_EXPECTED("Single user is expected"), 
    BROKER_VERIFICATION_FAILED("Signature could not be verified"), 
    CALLBACK_IS_NOT_FOUND("Active callback is not found"), 
    CERTIFICATE_ENCODING_ERROR("Certificate encoding is not generated"), 
    CORRELATION_ID_FORMAT("Correlationid is not in UUID format"), 
    CORRELATION_ID_NOT_MATCHING_REQUEST_RESPONSE("Correlationid provided in request is not matching the response"), 
    DATE_PARSING_FAILURE("Cannot parse date"), 
    DECRYPTION_FAILED("Decryption failed"), 
    DEVELOPER_ACTIVITY_IS_NOT_RESOLVED("Activity is not resolved. Verify the activity name in your manifest file"), 
    DEVELOPER_ASYNC_TASK_REUSED("Async tasks can only be executed one time. They are not supposed to be reused."), 
    DEVELOPER_AUTHORITY_CAN_NOT_BE_VALIDED("Authority validation returned an error"), 
    DEVELOPER_AUTHORITY_IS_EMPTY("Authority is empty"), 
    DEVELOPER_AUTHORITY_IS_NOT_VALID_INSTANCE("Authority is not a valid instance"), 
    DEVELOPER_AUTHORITY_IS_NOT_VALID_URL("Authority url is not valid"), 
    DEVELOPER_BEARER_HEADER_MULTIPLE_ITEMS("Key/value pair list contains redundant items in the header"), 
    DEVELOPER_BROKER_PERMISSIONS_MISSING("GET_ACCOUNTS, MANAGE_ACCOUNTS, USE_CREDENTIALS are not added to AndroidManifest file"), 
    DEVELOPER_CALLING_ON_MAIN_THREAD("Calling from main thread for background operation"), 
    DEVELOPER_CONTEXT_IS_NOT_PROVIDED("Context is not provided"), 
    DEVELOPER_DIALOG_LAYOUT_INVALID("dialog_authentication.xml file has invalid elements"), 
    DEVELOPER_INTERNET_PERMISSION_MISSING("android.permission.INTERNET is not added to AndroidManifest file"), 
    DEVELOPER_REDIRECTURI_INVALID("The redirectUri for broker is invalid"), 
    DEVELOPER_RESOURCE_IS_EMPTY("Resource is empty"), 
    DEVICE_ALGORITHM_PADDING_EXCEPTION("Requested padding is not available"), 
    DEVICE_CACHE_IS_NOT_WORKING("Cache is not saving the changes."), 
    DEVICE_CERTIFICATE_API_EXCEPTION("Device certificate API has exception"), 
    DEVICE_CERTIFICATE_REQUEST_INVALID("Device certificate request is valid"), 
    DEVICE_CERTIFICATE_RESPONSE_FAILED("It is failed to create device certificate response"), 
    DEVICE_CHALLENGE_FAILURE("Device challenge failure"), 
    DEVICE_CONNECTION_IS_NOT_AVAILABLE("Connection is not available"), 
    DEVICE_FILE_CACHE_FORMAT_IS_WRONG("Wrong cache file format"), 
    DEVICE_FILE_CACHE_IS_NOT_LOADED_FROM_FILE("Cache is not loaded from File"), 
    DEVICE_FILE_CACHE_IS_NOT_WRITING_TO_FILE("FileCache could not write to the File"), 
    DEVICE_INTERNET_IS_NOT_AVAILABLE("Internet permissions are not set for the app"), 
    DEVICE_NO_SUCH_ALGORITHM("Device does not support the algorithm"), 
    DEVICE_PRNG_FIX_ERROR("PRNG fixes are not applied"), 
    DEVICE_SHARED_PREF_IS_NOT_AVAILABLE("Shared preferences are not available"), 
    DIGEST_ERROR("Error in generating hash with MessageDigest"), 
    DISCOVERY_NOT_SUPPORTED("Authority validation is not supported for ADFS authority."), 
    DRS_DISCOVERY_FAILED_UNKNOWN_HOST("DRS discovery failed: unknown host"), 
    DRS_FAILED_SERVER_ERROR("Enrollment server returned an unrecognized response"), 
    DRS_METADATA_URL_INVALID("Malformed DRS metadata URL"), 
    ENCODING_IS_NOT_SUPPORTED("Encoding format is not supported"), 
    ENCRYPTION_ERROR("Encryption related error"), 
    ENCRYPTION_FAILED("Encryption failed"), 
    ERROR_FAILED_SSL_HANDSHAKE("Webview returned error for SSL"), 
    ERROR_SILENT_REQUEST("Error in silent token request"), 
    ERROR_WEBVIEW("Webview returned an error"), 
    FAIL_TO_EXPORT("Fail to export"), 
    FAIL_TO_IMPORT("Fail to import"), 
    IDTOKEN_PARSING_FAILURE("Cannot parse IdToken"), 
    INCOMPATIBLE_BLOB_VERSION("Fail to deserialize because the blob version is incompatible"), 
    INVALID_TOKEN_CACHE_ITEM("Invalid token cache item"), 
    IO_EXCEPTION("I/O exception"), 
    JSON_PARSE_ERROR("Fail to parse JSON"), 
    KEY_CHAIN_PRIVATE_KEY_EXCEPTION("Key Chain private key exception"), 
    NO_NETWORK_CONNECTION_POWER_OPTIMIZATION("Unable to access the network due to power optimizations"), 
    ON_ACTIVITY_RESULT_CALLBACK_NOT_FOUND("onActivityResult is called, but callback is not found"), 
    ON_ACTIVITY_RESULT_INTENT_NULL("onActivityResult is called with null intent data"), 
    PACKAGE_NAME_NOT_FOUND("Package name is not resolved"), 
    RESOURCE_AUTHENTICATION_CHALLENGE_FAILURE("Resource authentication challenge failure"), 
    RESOURCE_NOT_FOUND("Resource is not found in your project. Please include resource files."), 
    SERVER_ERROR("Server returned an error"), 
    SERVER_INVALID_JSON_RESPONSE("Server returned invalid JSON response"), 
    SERVER_INVALID_REQUEST("Invalid request to server"), 
    SIGNATURE_EXCEPTION("Signature exception"), 
    TOKEN_CACHE_ITEM_NOT_FOUND("Token cache item is not found"), 
    WEBVIEW_REDIRECTURL_NOT_SSL_PROTECTED("The webview was redirected to an unsafe URL"), 
    WEBVIEW_RETURNED_AUTHENTICATION_EXCEPTION("Webview returned Authentication Exception"), 
    WEBVIEW_RETURNED_EMPTY_REDIRECT_URL("Webview returned empty redirect url"), 
    WEBVIEW_RETURNED_INVALID_AUTHENTICATION_EXCEPTION("Webview returned invalid or null Authentication Exception"), 
    X_MS_CLITELEM_MALFORMED("Malformed x-ms-clitelem header"), 
    X_MS_CLITELEM_VERSION_UNRECOGNIZED("Unrecognized x-ms-clitelem header version");
    
    private String mDescription;
    
    private ADALError(final String mDescription) {
        this.mDescription = mDescription;
    }
    
    public String getDescription() {
        return this.mDescription;
    }
    
    public String getLocalizedDescription(final Context context) {
        if (context != null) {
            final Resources resources = new Resources(context.getAssets(), context.getResources().getDisplayMetrics(), context.getResources().getConfiguration());
            return resources.getString(resources.getIdentifier(this.name(), "string", context.getPackageName()));
        }
        return this.mDescription;
    }
}
