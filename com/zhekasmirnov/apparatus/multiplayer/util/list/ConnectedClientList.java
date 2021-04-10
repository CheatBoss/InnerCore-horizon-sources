package com.zhekasmirnov.apparatus.multiplayer.util.list;

import com.android.tools.r8.annotations.*;
import java.lang.ref.*;
import com.zhekasmirnov.apparatus.multiplayer.*;
import com.zhekasmirnov.apparatus.multiplayer.server.*;
import com.zhekasmirnov.apparatus.util.*;
import java.util.*;
import com.zhekasmirnov.apparatus.adapter.innercore.game.entity.*;
import com.zhekasmirnov.apparatus.adapter.innercore.game.common.*;
import com.zhekasmirnov.apparatus.job.*;
import java.util.function.*;

@SynthesizedClassMap({ -$$Lambda$ConnectedClientList$pIg8Q5_oZoS-gJyb-OytOd5EdF4.class, -$$Lambda$ConnectedClientList$VhreSUX0KRZ7DeimWe1grVvNSAE.class, -$$Lambda$ConnectedClientList$tv5Ime92CjTbbtjOc91DWTjxy3s.class, -$$Lambda$ConnectedClientList$bnZ3_-_29kSBgTYFPOAQekK4Tyo.class, -$$Lambda$ConnectedClientList$ZdxY_Y65A70VTrF5yGjbtfHJKLc.class, -$$Lambda$ConnectedClientList$CLOA71t0LK6poZ6eOWk5FHhU6oc.class, -$$Lambda$ConnectedClientList$u82Gm9syvOcz44LYy-Ty5KlvQBk.class, -$$Lambda$ConnectedClientList$oqqlAMcrhrem1u5gogR_rTRmTJM.class, -$$Lambda$ConnectedClientList$Gy0Hpn5rfXkbLLbTPVZi2GverSA.class, -$$Lambda$ConnectedClientList$4e7bYL-aW8YALy3Z7uX6qOAP89Q.class, -$$Lambda$ConnectedClientList$BIGxkrgrhO0-7lvZkGIIKLIVKPo.class, -$$Lambda$ConnectedClientList$cYlDDqOM91mKLAzP57GXj0vM-pk.class, -$$Lambda$ConnectedClientList$_Tejrc0L_EDCEFG69hnX-3PljGs.class, -$$Lambda$ConnectedClientList$vHTkwv71W7B9nzFZ0Bz06oilLGE.class })
public class ConnectedClientList implements Iterable<ConnectedClient>
{
    private static final List<WeakReference<ConnectedClientList>> allClientLists;
    private Policy addPolicy;
    private long addPolicyNextRefresh;
    private int addPolicyTimeout;
    private final Set<ConnectedClient> clients;
    private final List<Listener> listeners;
    private Policy removePolicy;
    private long removePolicyNextRefresh;
    private int removePolicyTimeout;
    
    static {
        allClientLists = new ArrayList<WeakReference<ConnectedClientList>>();
        final JobExecutor serverThreadJobExecutor = Network.getSingleton().getServerThreadJobExecutor();
        Network.getSingleton().getServer().addOnClientConnectedListener((ModdedServer.OnClientConnectedListener)new -$$Lambda$ConnectedClientList$oqqlAMcrhrem1u5gogR_rTRmTJM(serverThreadJobExecutor));
        Network.getSingleton().getServer().addOnClientDisconnectedListener((ModdedServer.OnClientDisconnectedListener)new -$$Lambda$ConnectedClientList$4e7bYL-aW8YALy3Z7uX6qOAP89Q(serverThreadJobExecutor));
        Network.getSingleton().getServer().addShutdownListener((ModdedServer.OnShutdownListener)-$$Lambda$ConnectedClientList$vHTkwv71W7B9nzFZ0Bz06oilLGE.INSTANCE);
    }
    
    public ConnectedClientList() {
        this(true);
    }
    
    public ConnectedClientList(final boolean b) {
        this.clients = new HashSet<ConnectedClient>();
        this.listeners = new ArrayList<Listener>();
        this.addPolicy = null;
        this.addPolicyTimeout = 200;
        this.addPolicyNextRefresh = 0L;
        this.removePolicy = null;
        this.removePolicyTimeout = 10;
        this.removePolicyNextRefresh = 0L;
        if (b) {
            synchronized (ConnectedClientList.allClientLists) {
                ConnectedClientList.allClientLists.add(new WeakReference<ConnectedClientList>(this));
            }
        }
    }
    
    private static void clearOnShutdown() {
        synchronized (ConnectedClientList.allClientLists) {
            ConnectedClientList.allClientLists.clear();
        }
    }
    
    private static void globalRefresh() {
        final ArrayList<ConnectedClientList> list = (ArrayList<ConnectedClientList>)new ArrayList<Object>();
        Object o = ConnectedClientList.allClientLists;
        synchronized (o) {
            Java8BackComp.removeIf(ConnectedClientList.allClientLists, new -$$Lambda$ConnectedClientList$VhreSUX0KRZ7DeimWe1grVvNSAE(list));
            // monitorexit(o)
            o = list.iterator();
            while (((Iterator)o).hasNext()) {
                ((Iterator<ConnectedClientList>)o).next().forcedRefresh();
            }
        }
    }
    
    public void add(final ConnectedClient connectedClient) {
        if (connectedClient != null && !connectedClient.isClosed()) {
            synchronized (this) {
                if (this.removePolicy == null || !this.removePolicy.check(connectedClient)) {
                    this.clients.add(connectedClient);
                    final Iterator<Listener> iterator = this.listeners.iterator();
                    while (iterator.hasNext()) {
                        iterator.next().onAdd(connectedClient);
                    }
                }
            }
        }
    }
    
    public void addListener(final Listener listener) {
        this.listeners.add(listener);
    }
    
    public void clear() {
        if (!this.listeners.isEmpty()) {
            this.forEach(new -$$Lambda$ConnectedClientList$Gy0Hpn5rfXkbLLbTPVZi2GverSA(this));
        }
        this.clients.clear();
    }
    
    public boolean contains(final ConnectedClient connectedClient) {
        return this.clients.contains(connectedClient);
    }
    
    public void dropPoliciesAndClear() {
        this.setAddPolicy(null);
        this.setRemovePolicy(null);
        this.clear();
    }
    
    @Override
    public void forEach(final Consumer<? super ConnectedClient> consumer) {
        this.refresh();
        synchronized (this) {
            final Iterator<ConnectedClient> iterator = this.clients.iterator();
            while (iterator.hasNext()) {
                consumer.accept(iterator.next());
            }
        }
    }
    
    public void forEachClient(final ClientConsumer clientConsumer) {
        this.refresh();
        synchronized (this) {
            final Iterator<ConnectedClient> iterator = this.clients.iterator();
            while (iterator.hasNext()) {
                clientConsumer.accept(iterator.next());
            }
        }
    }
    
    public void forcedRefresh() {
        this.addPolicyNextRefresh = 0L;
        this.removePolicyNextRefresh = 0L;
        this.refresh();
    }
    
    public Set<ConnectedClient> getClientCollection() {
        this.refresh();
        return this.clients;
    }
    
    @Override
    public Iterator<ConnectedClient> iterator() {
        this.refresh();
        return this.clients.iterator();
    }
    
    public void refresh() {
        while (true) {
            while (true) {
                Label_0193: {
                    synchronized (this) {
                        final long currentTimeMillis = System.currentTimeMillis();
                        if (this.removePolicyNextRefresh <= currentTimeMillis) {
                            Java8BackComp.removeIf(this.clients, new -$$Lambda$ConnectedClientList$CLOA71t0LK6poZ6eOWk5FHhU6oc(this));
                            this.removePolicyNextRefresh = currentTimeMillis + this.removePolicyTimeout;
                        }
                        if (this.addPolicy != null && this.addPolicyNextRefresh <= currentTimeMillis) {
                            final Iterator<ConnectedClient> iterator = Network.getSingleton().getServer().getConnectedClients().iterator();
                            if (iterator.hasNext()) {
                                final ConnectedClient connectedClient = iterator.next();
                                if (!this.clients.contains(connectedClient) && this.addPolicy.check(connectedClient)) {
                                    this.clients.add(connectedClient);
                                    final Iterator<Listener> iterator2 = this.listeners.iterator();
                                    while (iterator2.hasNext()) {
                                        iterator2.next().onAdd(connectedClient);
                                    }
                                    break Label_0193;
                                }
                                break Label_0193;
                            }
                            else {
                                this.addPolicyNextRefresh = currentTimeMillis + this.addPolicyTimeout;
                            }
                        }
                        return;
                    }
                }
                continue;
            }
        }
    }
    
    public void remove(final ConnectedClient connectedClient) {
        synchronized (this) {
            if (this.clients.remove(connectedClient)) {
                final Iterator<Listener> iterator = this.listeners.iterator();
                while (iterator.hasNext()) {
                    iterator.next().onRemove(connectedClient);
                }
            }
        }
    }
    
    public void removeListener(final Listener listener) {
        this.listeners.remove(listener);
    }
    
    public void send(final String s, final Object o) {
        this.forEach(new -$$Lambda$ConnectedClientList$bnZ3_-_29kSBgTYFPOAQekK4Tyo(s, o));
    }
    
    public <T> void send(final String s, final T t, final Class<T> clazz) {
        this.forEach(new -$$Lambda$ConnectedClientList$ZdxY_Y65A70VTrF5yGjbtfHJKLc(s, t, clazz));
    }
    
    public void setAddPolicy(final Policy policy) {
        this.setAddPolicy(policy, this.addPolicyTimeout, true);
    }
    
    public void setAddPolicy(final Policy policy, final int n) {
        this.setAddPolicy(policy, n, true);
    }
    
    public void setAddPolicy(final Policy addPolicy, final int addPolicyTimeout, final boolean b) {
        this.addPolicy = addPolicy;
        this.addPolicyTimeout = addPolicyTimeout;
        if (b) {
            this.forcedRefresh();
        }
    }
    
    public void setAddPolicyTimeout(final int addPolicyTimeout) {
        this.addPolicyTimeout = addPolicyTimeout;
    }
    
    public void setRemovePolicy(final Policy policy) {
        this.setRemovePolicy(policy, this.removePolicyTimeout, true);
    }
    
    public void setRemovePolicy(final Policy policy, final int n) {
        this.setRemovePolicy(policy, n, true);
    }
    
    public void setRemovePolicy(final Policy removePolicy, final int removePolicyTimeout, final boolean b) {
        this.removePolicy = removePolicy;
        this.removePolicyTimeout = removePolicyTimeout;
        if (b) {
            this.forcedRefresh();
        }
    }
    
    public void setRemovePolicyTimeout(final int removePolicyTimeout) {
        this.removePolicyTimeout = removePolicyTimeout;
    }
    
    public ConnectedClientList setupAllInDimensionPolicy(final int n) {
        return this.setupAllInDimensionPolicy(n, 1000);
    }
    
    public ConnectedClientList setupAllInDimensionPolicy(final int n, final int n2) {
        this.setAddPolicy((Policy)new -$$Lambda$ConnectedClientList$pIg8Q5_oZoS-gJyb-OytOd5EdF4(n), n2, false);
        this.setRemovePolicy((Policy)new -$$Lambda$ConnectedClientList$tv5Ime92CjTbbtjOc91DWTjxy3s(n), n2, true);
        return this;
    }
    
    public ConnectedClientList setupAllPlayersPolicy() {
        return this.setupAllPlayersPolicy(5000);
    }
    
    public ConnectedClientList setupAllPlayersPolicy(final int n) {
        this.setAddPolicy((Policy)-$$Lambda$ConnectedClientList$u82Gm9syvOcz44LYy-Ty5KlvQBk.INSTANCE, n, false);
        this.setRemovePolicy(null);
        return this;
    }
    
    public ConnectedClientList setupDistancePolicy(final float n, final float n2, final float n3, final int n4, final float n5) {
        return this.setupDistancePolicy(n, n2, n3, n4, n5, n5, 1000);
    }
    
    public ConnectedClientList setupDistancePolicy(final float n, final float n2, final float n3, final int n4, final float n5, final float n6, final int n7) {
        return this.setupDistancePolicy(new Vector3(n, n2, n3), n4, n5, n6, n7);
    }
    
    public ConnectedClientList setupDistancePolicy(final Vector3 vector3, final int n, final float n2) {
        return this.setupDistancePolicy(vector3, n, n2, n2, 1000);
    }
    
    public ConnectedClientList setupDistancePolicy(final Vector3 vector3, final int n, final float n2, final float n3, final int n4) {
        this.setAddPolicy((Policy)new -$$Lambda$ConnectedClientList$_Tejrc0L_EDCEFG69hnX-3PljGs(n, vector3, n2), n4, true);
        this.setRemovePolicy((Policy)new -$$Lambda$ConnectedClientList$cYlDDqOM91mKLAzP57GXj0vM-pk(n, vector3, n3), n4, false);
        return this;
    }
    
    public interface ClientConsumer
    {
        void accept(final ConnectedClient p0);
    }
    
    public interface Listener
    {
        void onAdd(final ConnectedClient p0);
        
        void onRemove(final ConnectedClient p0);
    }
    
    public interface Policy
    {
        boolean check(final ConnectedClient p0);
    }
}
