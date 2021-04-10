package com.zhekasmirnov.apparatus.minecraft.enums;

import java.util.*;
import com.zhekasmirnov.apparatus.minecraft.version.*;
import com.zhekasmirnov.innercore.api.mod.*;
import org.mozilla.javascript.*;

public class EnumsJsInjector
{
    private boolean convertToUpperCase;
    private final Scriptable jsScope;
    
    public EnumsJsInjector(final Scriptable jsScope, final boolean convertToUpperCase) {
        this.convertToUpperCase = false;
        this.jsScope = jsScope;
        this.convertToUpperCase = convertToUpperCase;
    }
    
    public void injectAllEnumScopes(final String s) {
        final Iterator<String> iterator = EnumsScopeInfo.getAllScopesWithInfo().iterator();
        while (iterator.hasNext()) {
            this.injectEnumScopeUsingGlobalInfo(iterator.next(), s);
        }
    }
    
    public void injectEnumScope(final MinecraftVersion minecraftVersion, String s, final String s2) {
        final ScriptableObject empty = ScriptableObjectHelper.createEmpty();
        final EnumsContainer.Scope scope = GameEnums.getSingleton().getOrAddContainerForVersion(minecraftVersion).getScope(s);
        if (scope != null) {
            final Iterator<String> iterator = scope.getAllEnumNames().iterator();
            while (iterator.hasNext()) {
                s = iterator.next();
                String upperCase;
                if (this.convertToUpperCase) {
                    upperCase = s.toUpperCase();
                }
                else {
                    upperCase = s;
                }
                empty.put(upperCase, (Scriptable)empty, scope.getEnum(s));
            }
        }
        this.jsScope.put(s2, this.jsScope, (Object)empty);
    }
    
    public void injectEnumScopeUsingGlobalInfo(final String s, final String s2) {
        final EnumsScopeInfo forScope = EnumsScopeInfo.getForScope(s);
        if (forScope != null) {
            final MinecraftVersion jsScopeVersion = forScope.getJsScopeVersion();
            final StringBuilder sb = new StringBuilder();
            sb.append(s2);
            sb.append(forScope.getTypeName());
            this.injectEnumScope(jsScopeVersion, s, sb.toString());
        }
    }
}
