package com.microsoft.xbox.service.network.managers;

import java.util.*;

public final class NeverListResultContainer
{
    public static class NeverListResult
    {
        public ArrayList<NeverUser> users;
        
        public NeverListResult() {
            this.users = new ArrayList<NeverUser>();
        }
        
        public void add(final String s) {
            this.users.add(new NeverUser(s));
        }
        
        public boolean contains(final String s) {
            final Iterator<NeverUser> iterator = this.users.iterator();
            while (iterator.hasNext()) {
                if (iterator.next().xuid.equalsIgnoreCase(s)) {
                    return true;
                }
            }
            return false;
        }
        
        public NeverUser remove(final String s) {
            for (final NeverUser neverUser : this.users) {
                if (neverUser.xuid.equalsIgnoreCase(s)) {
                    this.users.remove(neverUser);
                    return neverUser;
                }
            }
            return null;
        }
    }
    
    public static class NeverUser
    {
        public String xuid;
        
        public NeverUser(final String xuid) {
            this.xuid = xuid;
        }
    }
}
