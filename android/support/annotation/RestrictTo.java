package android.support.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.CLASS)
@Target({ ElementType.ANNOTATION_TYPE, ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.FIELD, ElementType.PACKAGE })
public @interface RestrictTo {
    Scope[] value();
    
    public enum Scope
    {
        @Deprecated
        GROUP_ID, 
        LIBRARY, 
        LIBRARY_GROUP, 
        SUBCLASSES, 
        TESTS;
    }
}
