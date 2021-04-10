package android.support.v4.content;

import android.os.*;
import android.content.*;
import android.util.*;
import android.net.*;
import java.util.*;
import java.io.*;

public final class LocalBroadcastManager
{
    private static final boolean DEBUG = false;
    static final int MSG_EXEC_PENDING_BROADCASTS = 1;
    private static final String TAG = "LocalBroadcastManager";
    private static LocalBroadcastManager mInstance;
    private static final Object mLock;
    private final HashMap<String, ArrayList<ReceiverRecord>> mActions;
    private final Context mAppContext;
    private final Handler mHandler;
    private final ArrayList<BroadcastRecord> mPendingBroadcasts;
    private final HashMap<BroadcastReceiver, ArrayList<IntentFilter>> mReceivers;
    
    static {
        mLock = new Object();
    }
    
    private LocalBroadcastManager(final Context mAppContext) {
        this.mReceivers = new HashMap<BroadcastReceiver, ArrayList<IntentFilter>>();
        this.mActions = new HashMap<String, ArrayList<ReceiverRecord>>();
        this.mPendingBroadcasts = new ArrayList<BroadcastRecord>();
        this.mAppContext = mAppContext;
        this.mHandler = new Handler(mAppContext.getMainLooper()) {
            public void handleMessage(final Message message) {
                if (message.what != 1) {
                    super.handleMessage(message);
                    return;
                }
                LocalBroadcastManager.this.executePendingBroadcasts();
            }
        };
    }
    
    private void executePendingBroadcasts() {
        while (true) {
            Object mReceivers = this.mReceivers;
            synchronized (mReceivers) {
                final int size = this.mPendingBroadcasts.size();
                if (size <= 0) {
                    return;
                }
                final BroadcastRecord[] array = new BroadcastRecord[size];
                this.mPendingBroadcasts.toArray(array);
                this.mPendingBroadcasts.clear();
                // monitorexit(mReceivers)
                for (int i = 0; i < array.length; ++i) {
                    mReceivers = array[i];
                    for (int j = 0; j < ((BroadcastRecord)mReceivers).receivers.size(); ++j) {
                        ((ReceiverRecord)((BroadcastRecord)mReceivers).receivers.get(j)).receiver.onReceive(this.mAppContext, ((BroadcastRecord)mReceivers).intent);
                    }
                }
            }
        }
    }
    
    public static LocalBroadcastManager getInstance(final Context context) {
        synchronized (LocalBroadcastManager.mLock) {
            if (LocalBroadcastManager.mInstance == null) {
                LocalBroadcastManager.mInstance = new LocalBroadcastManager(context.getApplicationContext());
            }
            return LocalBroadcastManager.mInstance;
        }
    }
    
    public void registerReceiver(final BroadcastReceiver broadcastReceiver, final IntentFilter intentFilter) {
        synchronized (this.mReceivers) {
            final ReceiverRecord receiverRecord = new ReceiverRecord(intentFilter, broadcastReceiver);
            ArrayList<IntentFilter> list;
            if ((list = this.mReceivers.get(broadcastReceiver)) == null) {
                list = new ArrayList<IntentFilter>(1);
                this.mReceivers.put(broadcastReceiver, list);
            }
            list.add(intentFilter);
            for (int i = 0; i < intentFilter.countActions(); ++i) {
                final String action = intentFilter.getAction(i);
                ArrayList<ReceiverRecord> list2;
                if ((list2 = this.mActions.get(action)) == null) {
                    list2 = new ArrayList<ReceiverRecord>(1);
                    this.mActions.put(action, list2);
                }
                list2.add(receiverRecord);
            }
        }
    }
    
    public boolean sendBroadcast(final Intent intent) {
        String action;
        String resolveTypeIfNeeded;
        Uri data;
        String scheme;
        Set categories;
        int n;
        Serializable s;
        ArrayList<ReceiverRecord> list;
        Object o;
        Serializable s2 = null;
        IntentFilter filter;
        int match;
        int n2 = 0;
        Label_0377_Outer:Label_0196_Outer:
        while (true) {
        Label_0503:
            while (true) {
            Label_0196:
                while (true) {
                Label_0377:
                    while (true) {
                        Label_0599: {
                            Label_0591:Label_0464_Outer:
                            while (true) {
                                Label_0586: {
                                    synchronized (this.mReceivers) {
                                        action = intent.getAction();
                                        resolveTypeIfNeeded = intent.resolveTypeIfNeeded(this.mAppContext.getContentResolver());
                                        data = intent.getData();
                                        scheme = intent.getScheme();
                                        categories = intent.getCategories();
                                        if ((intent.getFlags() & 0x8) == 0x0) {
                                            break Label_0586;
                                        }
                                        n = 1;
                                        if (n != 0) {
                                            s = new StringBuilder();
                                            ((StringBuilder)s).append("Resolving type ");
                                            ((StringBuilder)s).append(resolveTypeIfNeeded);
                                            ((StringBuilder)s).append(" scheme ");
                                            ((StringBuilder)s).append(scheme);
                                            ((StringBuilder)s).append(" of intent ");
                                            ((StringBuilder)s).append(intent);
                                            Log.v("LocalBroadcastManager", ((StringBuilder)s).toString());
                                        }
                                        list = this.mActions.get(intent.getAction());
                                        if (list == null) {
                                            return false;
                                        }
                                        if (n != 0) {
                                            s = new StringBuilder();
                                            ((StringBuilder)s).append("Action list: ");
                                            ((StringBuilder)s).append(list);
                                            Log.v("LocalBroadcastManager", ((StringBuilder)s).toString());
                                        }
                                        break Label_0591;
                                        // iftrue(Label_0602:, s != null)
                                        // iftrue(Label_0599:, n == 0)
                                        // iftrue(Label_0282:, !o.broadcasting)
                                        // iftrue(Label_0394:, match < 0)
                                        // iftrue(Label_0259:, n == 0)
                                        // iftrue(Label_0360:, n == 0)
                                        // iftrue(Label_0609:, n == 0)
                                    Label_0464:
                                        while (true) {
                                        Block_8_Outer:
                                            while (true) {
                                                Block_15: {
                                                    while (true) {
                                                        while (true) {
                                                            Block_13: {
                                                                Block_11: {
                                                                Block_10_Outer:
                                                                    while (true) {
                                                                        while (true) {
                                                                            Label_0259: {
                                                                                Block_14: {
                                                                                    break Block_14;
                                                                                    Label_0460: {
                                                                                        s = "category";
                                                                                    }
                                                                                    break Label_0464;
                                                                                    s = new StringBuilder();
                                                                                    ((StringBuilder)s).append("Matching against filter ");
                                                                                    ((StringBuilder)s).append(((ReceiverRecord)o).filter);
                                                                                    Log.v("LocalBroadcastManager", ((StringBuilder)s).toString());
                                                                                    break Label_0259;
                                                                                    break Block_11;
                                                                                }
                                                                                s2 = new ArrayList<Object>();
                                                                                break Label_0377;
                                                                                Label_0432: {
                                                                                    s = "unknown reason";
                                                                                }
                                                                                break Label_0464;
                                                                                ((ArrayList<StringBuilder>)s2).add((StringBuilder)o);
                                                                                ((ReceiverRecord)o).broadcasting = true;
                                                                                break Label_0377;
                                                                            }
                                                                            continue Label_0377_Outer;
                                                                        }
                                                                        Label_0282: {
                                                                            filter = ((ReceiverRecord)o).filter;
                                                                        }
                                                                        s = s2;
                                                                        match = filter.match(action, resolveTypeIfNeeded, scheme, data, categories, "LocalBroadcastManager");
                                                                        break Block_10_Outer;
                                                                        Label_0570:
                                                                        return true;
                                                                        this.mHandler.sendEmptyMessage(1);
                                                                        return true;
                                                                        o = list.get(n2);
                                                                        continue Block_10_Outer;
                                                                    }
                                                                    break Block_13;
                                                                    Label_0453: {
                                                                        s = "action";
                                                                    }
                                                                    break Label_0464;
                                                                }
                                                                Log.v("LocalBroadcastManager", "  Filter's target already added");
                                                                break Label_0599;
                                                                Label_0394: {
                                                                    break Block_15;
                                                                }
                                                                o = new StringBuilder();
                                                                ((StringBuilder)o).append("  Filter did not match: ");
                                                                ((StringBuilder)o).append((String)s);
                                                                Log.v("LocalBroadcastManager", ((StringBuilder)o).toString());
                                                                break Label_0377;
                                                            }
                                                            s2 = new StringBuilder();
                                                            ((StringBuilder)s2).append("  Filter matched!  match=0x");
                                                            ((StringBuilder)s2).append(Integer.toHexString(match));
                                                            Log.v("LocalBroadcastManager", ((StringBuilder)s2).toString());
                                                            continue Label_0464_Outer;
                                                        }
                                                        continue Label_0196_Outer;
                                                    }
                                                    ((ReceiverRecord)((ArrayList<ReceiverRecord>)s2).get(n)).broadcasting = false;
                                                    ++n;
                                                    continue Label_0503;
                                                }
                                                Label_0446: {
                                                    s = "data";
                                                }
                                                continue Label_0464;
                                                Label_0532:
                                                this.mPendingBroadcasts.add(new BroadcastRecord(intent, (ArrayList<ReceiverRecord>)s2));
                                                continue Block_8_Outer;
                                            }
                                            Label_0439: {
                                                s = "type";
                                            }
                                            continue Label_0464;
                                        }
                                    }
                                    // iftrue(Label_0616:, n2 >= list.size())
                                    // iftrue(Label_0532:, n >= s2.size())
                                    // switch([Lcom.strobel.decompiler.ast.Label;@6ac4ea0, match)
                                    // iftrue(Label_0570:, this.mHandler.hasMessages(1))
                                }
                                n = 0;
                                continue Label_0377_Outer;
                            }
                            s2 = null;
                            n2 = 0;
                            continue Label_0196;
                        }
                        break Label_0377;
                        Label_0602: {
                            s2 = s;
                        }
                        continue Label_0377;
                    }
                    ++n2;
                    continue Label_0196;
                }
                Label_0616: {
                    if (s2 != null) {
                        n = 0;
                        continue Label_0503;
                    }
                }
                break;
            }
            return false;
        }
    }
    
    public void sendBroadcastSync(final Intent intent) {
        if (this.sendBroadcast(intent)) {
            this.executePendingBroadcasts();
        }
    }
    
    public void unregisterReceiver(final BroadcastReceiver broadcastReceiver) {
        ArrayList<IntentFilter> list;
        int n;
        ArrayList<ReceiverRecord> list2;
        int n2 = 0;
        IntentFilter intentFilter;
        int n3 = 0;
        String action;
        int n4 = 0;
        Label_0054_Outer:Label_0031_Outer:
        while (true) {
        Label_0031:
            while (true) {
            Label_0054:
                while (true) {
                    Block_4_Outer:Block_5_Outer:
                    while (true) {
                        Label_0173: {
                            Label_0168: {
                                synchronized (this.mReceivers) {
                                    list = this.mReceivers.remove(broadcastReceiver);
                                    if (list == null) {
                                        return;
                                    }
                                    break Label_0168;
                                    // iftrue(Label_0137:, n >= list2.size())
                                    // iftrue(Label_0181:, list2.size() > 0)
                                    // iftrue(Label_0181:, list2 == null)
                                    // iftrue(Label_0158:, n2 >= list.size())
                                    // iftrue(Label_0190:, n3 >= intentFilter.countActions())
                                    while (true) {
                                        Block_7: {
                                            break Block_7;
                                        Block_6:
                                            while (true) {
                                                while (true) {
                                                    intentFilter = list.get(n2);
                                                    n3 = 0;
                                                    break Label_0054;
                                                    while (true) {
                                                        this.mActions.remove(action);
                                                        break Block_4_Outer;
                                                        Label_0137: {
                                                            continue Block_5_Outer;
                                                        }
                                                    }
                                                    action = intentFilter.getAction(n3);
                                                    list2 = this.mActions.get(action);
                                                    break Block_6;
                                                    Label_0158: {
                                                        return;
                                                    }
                                                    continue Label_0054_Outer;
                                                }
                                                continue Label_0031_Outer;
                                            }
                                            n = 0;
                                            continue Block_4_Outer;
                                            list2.remove(n);
                                            n4 = n - 1;
                                            break Label_0173;
                                        }
                                        n4 = n;
                                        continue;
                                    }
                                }
                                // iftrue(Label_0173:, (ReceiverRecord)list2.get(n).receiver != broadcastReceiver2)
                            }
                            n2 = 0;
                            continue Label_0031;
                        }
                        n = n4 + 1;
                        continue Label_0054_Outer;
                    }
                    ++n3;
                    continue Label_0054;
                }
                Label_0190: {
                    ++n2;
                }
                continue Label_0031;
            }
        }
    }
    
    private static class BroadcastRecord
    {
        final Intent intent;
        final ArrayList<ReceiverRecord> receivers;
        
        BroadcastRecord(final Intent intent, final ArrayList<ReceiverRecord> receivers) {
            this.intent = intent;
            this.receivers = receivers;
        }
    }
    
    private static class ReceiverRecord
    {
        boolean broadcasting;
        final IntentFilter filter;
        final BroadcastReceiver receiver;
        
        ReceiverRecord(final IntentFilter filter, final BroadcastReceiver receiver) {
            this.filter = filter;
            this.receiver = receiver;
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder(128);
            sb.append("Receiver{");
            sb.append(this.receiver);
            sb.append(" filter=");
            sb.append(this.filter);
            sb.append("}");
            return sb.toString();
        }
    }
}
