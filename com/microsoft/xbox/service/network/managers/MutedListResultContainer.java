package com.microsoft.xbox.service.network.managers;

import java.util.*;

public final class MutedListResultContainer
{
    public static class MutedListResult
    {
        public ArrayList<MutedUser> users;
        
        public MutedListResult() {
            this.users = new ArrayList<MutedUser>();
        }
        
        public void add(final String s) {
            this.users.add(new MutedUser(s));
        }
        
        public boolean contains(final String s) {
            final Iterator<MutedUser> iterator = this.users.iterator();
            while (iterator.hasNext()) {
                if (iterator.next().xuid.equalsIgnoreCase(s)) {
                    return true;
                }
            }
            return false;
        }
        
        public MutedUser remove(final String s) {
            for (final MutedUser mutedUser : this.users) {
                if (mutedUser.xuid.equalsIgnoreCase(s)) {
                    this.users.remove(mutedUser);
                    return mutedUser;
                }
            }
            return null;
        }
    }
    
    public static class MutedUser
    {
        public String xuid;
        
        public MutedUser(final String xuid) {
            this.xuid = xuid;
        }
    }
}
