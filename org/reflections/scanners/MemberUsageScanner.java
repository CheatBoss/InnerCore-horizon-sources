package org.reflections.scanners;

import org.reflections.util.*;
import com.google.common.collect.*;
import javassist.bytecode.*;
import com.google.common.base.*;
import org.reflections.*;
import javassist.*;
import javassist.expr.*;

public class MemberUsageScanner extends AbstractScanner
{
    private ClassPool classPool;
    
    private ClassPool getClassPool() {
        if (this.classPool == null) {
            synchronized (this) {
                this.classPool = new ClassPool();
                final ClassLoader[] classLoaders = this.getConfiguration().getClassLoaders();
                int i = 0;
                ClassLoader[] classLoaders2 = classLoaders;
                if (classLoaders == null) {
                    classLoaders2 = ClasspathHelper.classLoaders(new ClassLoader[0]);
                }
                while (i < classLoaders2.length) {
                    this.classPool.appendClassPath((ClassPath)new LoaderClassPath(classLoaders2[i]));
                    ++i;
                }
            }
        }
        return this.classPool;
    }
    
    private void put(final String s, final int n, final String s2) {
        if (this.acceptResult(s)) {
            final Multimap<String, String> store = this.getStore();
            final StringBuilder sb = new StringBuilder();
            sb.append(s2);
            sb.append(" #");
            sb.append(n);
            store.put((Object)s, (Object)sb.toString());
        }
    }
    
    String parameterNames(final MethodInfo methodInfo) {
        return Joiner.on(", ").join((Iterable)this.getMetadataAdapter().getParameterNames(methodInfo));
    }
    
    @Override
    public void scan(final Object o) {
        try {
            final CtClass value = this.getClassPool().get(this.getMetadataAdapter().getClassName(o));
            final CtConstructor[] declaredConstructors = value.getDeclaredConstructors();
            final int length = declaredConstructors.length;
            final int n = 0;
            for (int i = 0; i < length; ++i) {
                this.scanMember((CtBehavior)declaredConstructors[i]);
            }
            final CtMethod[] declaredMethods = value.getDeclaredMethods();
            for (int length2 = declaredMethods.length, j = n; j < length2; ++j) {
                this.scanMember((CtBehavior)declaredMethods[j]);
            }
            value.detach();
        }
        catch (Exception ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Could not scan method usage for ");
            sb.append(this.getMetadataAdapter().getClassName(o));
            throw new ReflectionsException(sb.toString(), ex);
        }
    }
    
    void scanMember(final CtBehavior ctBehavior) throws CannotCompileException {
        final StringBuilder sb = new StringBuilder();
        sb.append(ctBehavior.getDeclaringClass().getName());
        sb.append(".");
        sb.append(ctBehavior.getMethodInfo().getName());
        sb.append("(");
        sb.append(this.parameterNames(ctBehavior.getMethodInfo()));
        sb.append(")");
        ctBehavior.instrument((ExprEditor)new ExprEditor() {
            final /* synthetic */ String val$key = sb.toString();
            
            public void edit(final ConstructorCall constructorCall) throws CannotCompileException {
                try {
                    final MemberUsageScanner this$0 = MemberUsageScanner.this;
                    final StringBuilder sb = new StringBuilder();
                    sb.append(constructorCall.getConstructor().getDeclaringClass().getName());
                    sb.append(".");
                    sb.append("<init>");
                    sb.append("(");
                    sb.append(MemberUsageScanner.this.parameterNames(constructorCall.getConstructor().getMethodInfo()));
                    sb.append(")");
                    this$0.put(sb.toString(), constructorCall.getLineNumber(), this.val$key);
                }
                catch (NotFoundException ex) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("Could not find member ");
                    sb2.append(constructorCall.getClassName());
                    sb2.append(" in ");
                    sb2.append(this.val$key);
                    throw new ReflectionsException(sb2.toString(), (Throwable)ex);
                }
            }
            
            public void edit(final FieldAccess fieldAccess) throws CannotCompileException {
                try {
                    final MemberUsageScanner this$0 = MemberUsageScanner.this;
                    final StringBuilder sb = new StringBuilder();
                    sb.append(fieldAccess.getField().getDeclaringClass().getName());
                    sb.append(".");
                    sb.append(fieldAccess.getFieldName());
                    this$0.put(sb.toString(), fieldAccess.getLineNumber(), this.val$key);
                }
                catch (NotFoundException ex) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("Could not find member ");
                    sb2.append(fieldAccess.getFieldName());
                    sb2.append(" in ");
                    sb2.append(this.val$key);
                    throw new ReflectionsException(sb2.toString(), (Throwable)ex);
                }
            }
            
            public void edit(final MethodCall methodCall) throws CannotCompileException {
                try {
                    final MemberUsageScanner this$0 = MemberUsageScanner.this;
                    final StringBuilder sb = new StringBuilder();
                    sb.append(methodCall.getMethod().getDeclaringClass().getName());
                    sb.append(".");
                    sb.append(methodCall.getMethodName());
                    sb.append("(");
                    sb.append(MemberUsageScanner.this.parameterNames(methodCall.getMethod().getMethodInfo()));
                    sb.append(")");
                    this$0.put(sb.toString(), methodCall.getLineNumber(), this.val$key);
                }
                catch (NotFoundException ex) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("Could not find member ");
                    sb2.append(methodCall.getClassName());
                    sb2.append(" in ");
                    sb2.append(this.val$key);
                    throw new ReflectionsException(sb2.toString(), (Throwable)ex);
                }
            }
            
            public void edit(final NewExpr newExpr) throws CannotCompileException {
                try {
                    final MemberUsageScanner this$0 = MemberUsageScanner.this;
                    final StringBuilder sb = new StringBuilder();
                    sb.append(newExpr.getConstructor().getDeclaringClass().getName());
                    sb.append(".");
                    sb.append("<init>");
                    sb.append("(");
                    sb.append(MemberUsageScanner.this.parameterNames(newExpr.getConstructor().getMethodInfo()));
                    sb.append(")");
                    this$0.put(sb.toString(), newExpr.getLineNumber(), this.val$key);
                }
                catch (NotFoundException ex) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("Could not find new instance usage in ");
                    sb2.append(this.val$key);
                    throw new ReflectionsException(sb2.toString(), (Throwable)ex);
                }
            }
        });
    }
}
