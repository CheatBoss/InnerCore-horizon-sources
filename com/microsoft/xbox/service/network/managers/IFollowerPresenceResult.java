package com.microsoft.xbox.service.network.managers;

import java.io.*;
import com.microsoft.xbox.service.model.serialization.*;
import com.microsoft.xbox.toolkit.*;
import java.lang.reflect.*;
import java.util.*;

public interface IFollowerPresenceResult
{
    public static class ActivityRecord
    {
        public BroadcastRecord broadcast;
        public String richPresence;
    }
    
    public static class BroadcastRecord
    {
        public String id;
        public String provider;
        public String session;
        public int viewers;
    }
    
    public static class DeviceRecord
    {
        public ArrayList<TitleRecord> titles;
        public String type;
        
        public boolean isXbox360() {
            return "Xbox360".equalsIgnoreCase(this.type);
        }
        
        public boolean isXboxOne() {
            return "XboxOne".equalsIgnoreCase(this.type);
        }
    }
    
    public static class FollowersPresenceResult
    {
        public ArrayList<UserPresence> userPresence;
        
        public static FollowersPresenceResult deserialize(final InputStream inputStream) {
            final UserPresence[] array = GsonUtil.deserializeJson(inputStream, UserPresence[].class, Date.class, new UTCDateConverterGson.UTCDateConverterJSONDeserializer());
            if (array != null) {
                final FollowersPresenceResult followersPresenceResult = new FollowersPresenceResult();
                followersPresenceResult.userPresence = new ArrayList<UserPresence>(Arrays.asList(array));
                return followersPresenceResult;
            }
            return null;
        }
    }
    
    public static class LastSeenRecord
    {
        public String deviceType;
        public String titleName;
    }
    
    public static class TitleRecord
    {
        public ActivityRecord activity;
        public long id;
        public Date lastModified;
        public String name;
        public String placement;
        
        public boolean isDash() {
            return this.id == 4294838225L;
        }
        
        public boolean isRunningInFullOrFill() {
            return "Full".equalsIgnoreCase(this.placement) || "Fill".equalsIgnoreCase(this.placement);
        }
    }
    
    public static class UserPresence
    {
        private BroadcastRecord broadcastRecord;
        private boolean broadcastRecordSet;
        public ArrayList<DeviceRecord> devices;
        public LastSeenRecord lastSeen;
        public String state;
        public String xuid;
        
        public BroadcastRecord getBroadcastRecord(final long n) {
            if (!this.broadcastRecordSet) {
                Label_0136: {
                    if ("Online".equalsIgnoreCase(this.state)) {
                        for (final DeviceRecord deviceRecord : this.devices) {
                            if (deviceRecord.isXboxOne()) {
                                for (final TitleRecord titleRecord : deviceRecord.titles) {
                                    if (titleRecord.id == n && titleRecord.isRunningInFullOrFill() && titleRecord.activity != null && titleRecord.activity.broadcast != null) {
                                        this.broadcastRecord = titleRecord.activity.broadcast;
                                        break Label_0136;
                                    }
                                }
                            }
                        }
                    }
                }
                this.broadcastRecordSet = true;
            }
            return this.broadcastRecord;
        }
        
        public int getBroadcastingViewerCount(final long n) {
            final BroadcastRecord broadcastRecord = this.getBroadcastRecord(n);
            if (broadcastRecord == null) {
                return 0;
            }
            return broadcastRecord.viewers;
        }
        
        public Date getXboxOneNowPlayingDate() {
            final boolean equalsIgnoreCase = "Online".equalsIgnoreCase(this.state);
            Date date = null;
            Date lastModified = null;
            if (equalsIgnoreCase) {
                final Iterator<DeviceRecord> iterator = this.devices.iterator();
                while (true) {
                    date = lastModified;
                    if (!iterator.hasNext()) {
                        break;
                    }
                    final DeviceRecord deviceRecord = iterator.next();
                    if (!deviceRecord.isXboxOne()) {
                        continue;
                    }
                    for (final TitleRecord titleRecord : deviceRecord.titles) {
                        if (titleRecord.isRunningInFullOrFill()) {
                            lastModified = titleRecord.lastModified;
                            break;
                        }
                    }
                }
            }
            return date;
        }
        
        public long getXboxOneNowPlayingTitleId() {
            final boolean equalsIgnoreCase = "Online".equalsIgnoreCase(this.state);
            long n;
            long id = n = -1L;
            if (equalsIgnoreCase) {
                final Iterator<DeviceRecord> iterator = this.devices.iterator();
                while (true) {
                    n = id;
                    if (!iterator.hasNext()) {
                        break;
                    }
                    final DeviceRecord deviceRecord = iterator.next();
                    if (!deviceRecord.isXboxOne()) {
                        continue;
                    }
                    for (final TitleRecord titleRecord : deviceRecord.titles) {
                        if (titleRecord.isRunningInFullOrFill()) {
                            id = titleRecord.id;
                            break;
                        }
                    }
                }
            }
            return n;
        }
    }
}
