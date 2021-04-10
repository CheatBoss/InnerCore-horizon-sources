package org.spongycastle.asn1.x509;

public interface NameConstraintValidator
{
    void addExcludedSubtree(final GeneralSubtree p0);
    
    void checkExcluded(final GeneralName p0) throws NameConstraintValidatorException;
    
    void checkPermitted(final GeneralName p0) throws NameConstraintValidatorException;
    
    void intersectEmptyPermittedSubtree(final int p0);
    
    void intersectPermittedSubtree(final GeneralSubtree p0);
    
    void intersectPermittedSubtree(final GeneralSubtree[] p0);
}
