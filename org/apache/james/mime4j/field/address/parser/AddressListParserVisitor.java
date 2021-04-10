package org.apache.james.mime4j.field.address.parser;

public interface AddressListParserVisitor
{
    Object visit(final ASTaddr_spec p0, final Object p1);
    
    Object visit(final ASTaddress p0, final Object p1);
    
    Object visit(final ASTaddress_list p0, final Object p1);
    
    Object visit(final ASTangle_addr p0, final Object p1);
    
    Object visit(final ASTdomain p0, final Object p1);
    
    Object visit(final ASTgroup_body p0, final Object p1);
    
    Object visit(final ASTlocal_part p0, final Object p1);
    
    Object visit(final ASTmailbox p0, final Object p1);
    
    Object visit(final ASTname_addr p0, final Object p1);
    
    Object visit(final ASTphrase p0, final Object p1);
    
    Object visit(final ASTroute p0, final Object p1);
    
    Object visit(final SimpleNode p0, final Object p1);
}
