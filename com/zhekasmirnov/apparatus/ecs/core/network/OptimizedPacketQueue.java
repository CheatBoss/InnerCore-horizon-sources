package com.zhekasmirnov.apparatus.ecs.core.network;

import java.util.function.*;
import java.util.*;
import com.zhekasmirnov.apparatus.multiplayer.server.*;

public class OptimizedPacketQueue
{
    private final LinkedList<Packet> packets;
    private final Consumer<Packet> sender;
    
    public OptimizedPacketQueue(final Consumer<Packet> sender) {
        this.packets = new LinkedList<Packet>();
        this.sender = sender;
    }
    
    public void add(Packet packet) {
        while (true) {
            while (true) {
                Object o = null;
                Label_0141: {
                    synchronized (this.packets) {
                        o = this.packets;
                        final int size = this.packets.size();
                        int n = 1;
                        final ListIterator<Packet> listIterator = ((LinkedList<Packet>)o).listIterator(size - 1);
                        final int n2 = n;
                        if (!listIterator.hasPrevious()) {
                            this.packets.add(packet);
                            return;
                        }
                        final Packet packet2 = listIterator.previous();
                        if (packet.overrides(packet2)) {
                            listIterator.remove();
                            n = n2;
                            o = packet;
                            break Label_0141;
                        }
                        n = n2;
                        o = packet;
                        if (n2 == 0) {
                            break Label_0141;
                        }
                        o = packet.merge(packet2);
                        if (o != null) {
                            listIterator.remove();
                            n = n2;
                            break Label_0141;
                        }
                    }
                    int n = 0;
                    final LinkedList<Packet> list;
                    o = list;
                }
                packet = (Packet)o;
                continue;
            }
        }
    }
    
    public void flush() {
        synchronized (this.packets) {
            final Iterator<Packet> iterator = this.packets.iterator();
            while (iterator.hasNext()) {
                this.sender.accept(iterator.next());
            }
            this.packets.clear();
        }
    }
    
    public int size() {
        return this.packets.size();
    }
    
    public abstract static class Packet
    {
        private final ConnectedClient client;
        private final String type;
        
        protected Packet(final ConnectedClient client, final String type) {
            this.type = type;
            this.client = client;
        }
        
        public ConnectedClient getClient() {
            return this.client;
        }
        
        public Object getData() {
            return "";
        }
        
        public String getType() {
            return this.type;
        }
        
        public Packet merge(final Packet packet) {
            return null;
        }
        
        public boolean overrides(final Packet packet) {
            return false;
        }
    }
}
