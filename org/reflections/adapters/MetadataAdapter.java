package org.reflections.adapters;

import java.util.*;
import org.reflections.vfs.*;

public interface MetadataAdapter<C, F, M>
{
    boolean acceptsInput(final String p0);
    
    List<String> getClassAnnotationNames(final C p0);
    
    String getClassName(final C p0);
    
    List<String> getFieldAnnotationNames(final F p0);
    
    String getFieldName(final F p0);
    
    List<F> getFields(final C p0);
    
    List<String> getInterfacesNames(final C p0);
    
    List<String> getMethodAnnotationNames(final M p0);
    
    String getMethodFullKey(final C p0, final M p1);
    
    String getMethodKey(final C p0, final M p1);
    
    String getMethodModifier(final M p0);
    
    String getMethodName(final M p0);
    
    List<M> getMethods(final C p0);
    
    C getOfCreateClassObject(final Vfs.File p0) throws Exception;
    
    List<String> getParameterAnnotationNames(final M p0, final int p1);
    
    List<String> getParameterNames(final M p0);
    
    String getReturnTypeName(final M p0);
    
    String getSuperclassName(final C p0);
    
    boolean isPublic(final Object p0);
}
