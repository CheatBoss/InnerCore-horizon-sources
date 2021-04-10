package org.mozilla.javascript;

public class NativeJavaConstructor extends BaseFunction
{
    static final long serialVersionUID = -8149253217482668463L;
    MemberBox ctor;
    
    public NativeJavaConstructor(final MemberBox ctor) {
        this.ctor = ctor;
    }
    
    @Override
    public Object call(final Context context, final Scriptable scriptable, final Scriptable scriptable2, final Object[] array) {
        return NativeJavaClass.constructSpecific(context, scriptable, array, this.ctor);
    }
    
    @Override
    public String getFunctionName() {
        return "<init>".concat(JavaMembers.liveConnectSignature(this.ctor.argTypes));
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("[JavaConstructor ");
        sb.append(this.ctor.getName());
        sb.append("]");
        return sb.toString();
    }
}
