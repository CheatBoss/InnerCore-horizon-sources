package org.mozilla.javascript;

class BeanProperty
{
    MemberBox getter;
    MemberBox setter;
    NativeJavaMethod setters;
    
    BeanProperty(final MemberBox getter, final MemberBox setter, final NativeJavaMethod setters) {
        this.getter = getter;
        this.setter = setter;
        this.setters = setters;
    }
}
