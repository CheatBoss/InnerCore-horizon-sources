package com.microsoft.xbox.toolkit;

public enum AsyncActionStatus
{
    FAIL;
    
    private static final AsyncActionStatus[][] MERGE_MATRIX;
    
    NO_CHANGE, 
    NO_OP_FAIL, 
    NO_OP_SUCCESS, 
    SUCCESS;
    
    static {
        final AsyncActionStatus success = AsyncActionStatus.SUCCESS;
        final AsyncActionStatus fail = AsyncActionStatus.FAIL;
        final AsyncActionStatus no_CHANGE = AsyncActionStatus.NO_CHANGE;
        final AsyncActionStatus no_OP_SUCCESS = AsyncActionStatus.NO_OP_SUCCESS;
        final AsyncActionStatus asyncActionStatus;
        MERGE_MATRIX = new AsyncActionStatus[][] { { success, fail, success, success, fail }, { fail, fail, fail, fail, fail }, { success, fail, no_CHANGE, no_OP_SUCCESS, asyncActionStatus }, { success, fail, no_OP_SUCCESS, no_OP_SUCCESS, asyncActionStatus }, { fail, fail, asyncActionStatus, asyncActionStatus, asyncActionStatus } };
    }
    
    public static boolean getIsFail(final AsyncActionStatus asyncActionStatus) {
        return asyncActionStatus == AsyncActionStatus.FAIL || asyncActionStatus == AsyncActionStatus.NO_OP_FAIL;
    }
    
    public static AsyncActionStatus merge(AsyncActionStatus asyncActionStatus, final AsyncActionStatus... array) {
        for (int length = array.length, i = 0; i < length; ++i) {
            asyncActionStatus = AsyncActionStatus.MERGE_MATRIX[asyncActionStatus.ordinal()][array[i].ordinal()];
        }
        return asyncActionStatus;
    }
}
