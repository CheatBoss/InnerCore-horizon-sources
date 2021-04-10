package org.spongycastle.asn1.x500.style;

import java.util.*;
import org.spongycastle.asn1.x509.*;
import org.spongycastle.asn1.pkcs.*;
import org.spongycastle.asn1.*;
import org.spongycastle.asn1.x500.*;

public class BCStyle extends AbstractX500NameStyle
{
    public static final ASN1ObjectIdentifier BUSINESS_CATEGORY;
    public static final ASN1ObjectIdentifier C;
    public static final ASN1ObjectIdentifier CN;
    public static final ASN1ObjectIdentifier COUNTRY_OF_CITIZENSHIP;
    public static final ASN1ObjectIdentifier COUNTRY_OF_RESIDENCE;
    public static final ASN1ObjectIdentifier DATE_OF_BIRTH;
    public static final ASN1ObjectIdentifier DC;
    public static final ASN1ObjectIdentifier DMD_NAME;
    public static final ASN1ObjectIdentifier DN_QUALIFIER;
    private static final Hashtable DefaultLookUp;
    private static final Hashtable DefaultSymbols;
    public static final ASN1ObjectIdentifier E;
    public static final ASN1ObjectIdentifier EmailAddress;
    public static final ASN1ObjectIdentifier GENDER;
    public static final ASN1ObjectIdentifier GENERATION;
    public static final ASN1ObjectIdentifier GIVENNAME;
    public static final ASN1ObjectIdentifier INITIALS;
    public static final X500NameStyle INSTANCE;
    public static final ASN1ObjectIdentifier L;
    public static final ASN1ObjectIdentifier NAME;
    public static final ASN1ObjectIdentifier NAME_AT_BIRTH;
    public static final ASN1ObjectIdentifier O;
    public static final ASN1ObjectIdentifier ORGANIZATION_IDENTIFIER;
    public static final ASN1ObjectIdentifier OU;
    public static final ASN1ObjectIdentifier PLACE_OF_BIRTH;
    public static final ASN1ObjectIdentifier POSTAL_ADDRESS;
    public static final ASN1ObjectIdentifier POSTAL_CODE;
    public static final ASN1ObjectIdentifier PSEUDONYM;
    public static final ASN1ObjectIdentifier SERIALNUMBER;
    public static final ASN1ObjectIdentifier SN;
    public static final ASN1ObjectIdentifier ST;
    public static final ASN1ObjectIdentifier STREET;
    public static final ASN1ObjectIdentifier SURNAME;
    public static final ASN1ObjectIdentifier T;
    public static final ASN1ObjectIdentifier TELEPHONE_NUMBER;
    public static final ASN1ObjectIdentifier UID;
    public static final ASN1ObjectIdentifier UNIQUE_IDENTIFIER;
    public static final ASN1ObjectIdentifier UnstructuredAddress;
    public static final ASN1ObjectIdentifier UnstructuredName;
    protected final Hashtable defaultLookUp;
    protected final Hashtable defaultSymbols;
    
    static {
        C = new ASN1ObjectIdentifier("2.5.4.6").intern();
        O = new ASN1ObjectIdentifier("2.5.4.10").intern();
        OU = new ASN1ObjectIdentifier("2.5.4.11").intern();
        T = new ASN1ObjectIdentifier("2.5.4.12").intern();
        CN = new ASN1ObjectIdentifier("2.5.4.3").intern();
        SN = new ASN1ObjectIdentifier("2.5.4.5").intern();
        STREET = new ASN1ObjectIdentifier("2.5.4.9").intern();
        SERIALNUMBER = BCStyle.SN;
        L = new ASN1ObjectIdentifier("2.5.4.7").intern();
        ST = new ASN1ObjectIdentifier("2.5.4.8").intern();
        SURNAME = new ASN1ObjectIdentifier("2.5.4.4").intern();
        GIVENNAME = new ASN1ObjectIdentifier("2.5.4.42").intern();
        INITIALS = new ASN1ObjectIdentifier("2.5.4.43").intern();
        GENERATION = new ASN1ObjectIdentifier("2.5.4.44").intern();
        UNIQUE_IDENTIFIER = new ASN1ObjectIdentifier("2.5.4.45").intern();
        BUSINESS_CATEGORY = new ASN1ObjectIdentifier("2.5.4.15").intern();
        POSTAL_CODE = new ASN1ObjectIdentifier("2.5.4.17").intern();
        DN_QUALIFIER = new ASN1ObjectIdentifier("2.5.4.46").intern();
        PSEUDONYM = new ASN1ObjectIdentifier("2.5.4.65").intern();
        DATE_OF_BIRTH = new ASN1ObjectIdentifier("1.3.6.1.5.5.7.9.1").intern();
        PLACE_OF_BIRTH = new ASN1ObjectIdentifier("1.3.6.1.5.5.7.9.2").intern();
        GENDER = new ASN1ObjectIdentifier("1.3.6.1.5.5.7.9.3").intern();
        COUNTRY_OF_CITIZENSHIP = new ASN1ObjectIdentifier("1.3.6.1.5.5.7.9.4").intern();
        COUNTRY_OF_RESIDENCE = new ASN1ObjectIdentifier("1.3.6.1.5.5.7.9.5").intern();
        NAME_AT_BIRTH = new ASN1ObjectIdentifier("1.3.36.8.3.14").intern();
        POSTAL_ADDRESS = new ASN1ObjectIdentifier("2.5.4.16").intern();
        DMD_NAME = new ASN1ObjectIdentifier("2.5.4.54").intern();
        TELEPHONE_NUMBER = X509ObjectIdentifiers.id_at_telephoneNumber;
        NAME = X509ObjectIdentifiers.id_at_name;
        ORGANIZATION_IDENTIFIER = X509ObjectIdentifiers.id_at_organizationIdentifier;
        EmailAddress = PKCSObjectIdentifiers.pkcs_9_at_emailAddress;
        UnstructuredName = PKCSObjectIdentifiers.pkcs_9_at_unstructuredName;
        UnstructuredAddress = PKCSObjectIdentifiers.pkcs_9_at_unstructuredAddress;
        E = BCStyle.EmailAddress;
        DC = new ASN1ObjectIdentifier("0.9.2342.19200300.100.1.25");
        UID = new ASN1ObjectIdentifier("0.9.2342.19200300.100.1.1");
        DefaultSymbols = new Hashtable();
        DefaultLookUp = new Hashtable();
        BCStyle.DefaultSymbols.put(BCStyle.C, "C");
        BCStyle.DefaultSymbols.put(BCStyle.O, "O");
        BCStyle.DefaultSymbols.put(BCStyle.T, "T");
        BCStyle.DefaultSymbols.put(BCStyle.OU, "OU");
        BCStyle.DefaultSymbols.put(BCStyle.CN, "CN");
        BCStyle.DefaultSymbols.put(BCStyle.L, "L");
        BCStyle.DefaultSymbols.put(BCStyle.ST, "ST");
        BCStyle.DefaultSymbols.put(BCStyle.SN, "SERIALNUMBER");
        BCStyle.DefaultSymbols.put(BCStyle.EmailAddress, "E");
        BCStyle.DefaultSymbols.put(BCStyle.DC, "DC");
        BCStyle.DefaultSymbols.put(BCStyle.UID, "UID");
        BCStyle.DefaultSymbols.put(BCStyle.STREET, "STREET");
        BCStyle.DefaultSymbols.put(BCStyle.SURNAME, "SURNAME");
        BCStyle.DefaultSymbols.put(BCStyle.GIVENNAME, "GIVENNAME");
        BCStyle.DefaultSymbols.put(BCStyle.INITIALS, "INITIALS");
        BCStyle.DefaultSymbols.put(BCStyle.GENERATION, "GENERATION");
        BCStyle.DefaultSymbols.put(BCStyle.UnstructuredAddress, "unstructuredAddress");
        BCStyle.DefaultSymbols.put(BCStyle.UnstructuredName, "unstructuredName");
        BCStyle.DefaultSymbols.put(BCStyle.UNIQUE_IDENTIFIER, "UniqueIdentifier");
        BCStyle.DefaultSymbols.put(BCStyle.DN_QUALIFIER, "DN");
        BCStyle.DefaultSymbols.put(BCStyle.PSEUDONYM, "Pseudonym");
        BCStyle.DefaultSymbols.put(BCStyle.POSTAL_ADDRESS, "PostalAddress");
        BCStyle.DefaultSymbols.put(BCStyle.NAME_AT_BIRTH, "NameAtBirth");
        BCStyle.DefaultSymbols.put(BCStyle.COUNTRY_OF_CITIZENSHIP, "CountryOfCitizenship");
        BCStyle.DefaultSymbols.put(BCStyle.COUNTRY_OF_RESIDENCE, "CountryOfResidence");
        BCStyle.DefaultSymbols.put(BCStyle.GENDER, "Gender");
        BCStyle.DefaultSymbols.put(BCStyle.PLACE_OF_BIRTH, "PlaceOfBirth");
        BCStyle.DefaultSymbols.put(BCStyle.DATE_OF_BIRTH, "DateOfBirth");
        BCStyle.DefaultSymbols.put(BCStyle.POSTAL_CODE, "PostalCode");
        BCStyle.DefaultSymbols.put(BCStyle.BUSINESS_CATEGORY, "BusinessCategory");
        BCStyle.DefaultSymbols.put(BCStyle.TELEPHONE_NUMBER, "TelephoneNumber");
        BCStyle.DefaultSymbols.put(BCStyle.NAME, "Name");
        BCStyle.DefaultSymbols.put(BCStyle.ORGANIZATION_IDENTIFIER, "organizationIdentifier");
        BCStyle.DefaultLookUp.put("c", BCStyle.C);
        BCStyle.DefaultLookUp.put("o", BCStyle.O);
        BCStyle.DefaultLookUp.put("t", BCStyle.T);
        BCStyle.DefaultLookUp.put("ou", BCStyle.OU);
        BCStyle.DefaultLookUp.put("cn", BCStyle.CN);
        BCStyle.DefaultLookUp.put("l", BCStyle.L);
        BCStyle.DefaultLookUp.put("st", BCStyle.ST);
        BCStyle.DefaultLookUp.put("sn", BCStyle.SN);
        BCStyle.DefaultLookUp.put("serialnumber", BCStyle.SN);
        BCStyle.DefaultLookUp.put("street", BCStyle.STREET);
        BCStyle.DefaultLookUp.put("emailaddress", BCStyle.E);
        BCStyle.DefaultLookUp.put("dc", BCStyle.DC);
        BCStyle.DefaultLookUp.put("e", BCStyle.E);
        BCStyle.DefaultLookUp.put("uid", BCStyle.UID);
        BCStyle.DefaultLookUp.put("surname", BCStyle.SURNAME);
        BCStyle.DefaultLookUp.put("givenname", BCStyle.GIVENNAME);
        BCStyle.DefaultLookUp.put("initials", BCStyle.INITIALS);
        BCStyle.DefaultLookUp.put("generation", BCStyle.GENERATION);
        BCStyle.DefaultLookUp.put("unstructuredaddress", BCStyle.UnstructuredAddress);
        BCStyle.DefaultLookUp.put("unstructuredname", BCStyle.UnstructuredName);
        BCStyle.DefaultLookUp.put("uniqueidentifier", BCStyle.UNIQUE_IDENTIFIER);
        BCStyle.DefaultLookUp.put("dn", BCStyle.DN_QUALIFIER);
        BCStyle.DefaultLookUp.put("pseudonym", BCStyle.PSEUDONYM);
        BCStyle.DefaultLookUp.put("postaladdress", BCStyle.POSTAL_ADDRESS);
        BCStyle.DefaultLookUp.put("nameofbirth", BCStyle.NAME_AT_BIRTH);
        BCStyle.DefaultLookUp.put("countryofcitizenship", BCStyle.COUNTRY_OF_CITIZENSHIP);
        BCStyle.DefaultLookUp.put("countryofresidence", BCStyle.COUNTRY_OF_RESIDENCE);
        BCStyle.DefaultLookUp.put("gender", BCStyle.GENDER);
        BCStyle.DefaultLookUp.put("placeofbirth", BCStyle.PLACE_OF_BIRTH);
        BCStyle.DefaultLookUp.put("dateofbirth", BCStyle.DATE_OF_BIRTH);
        BCStyle.DefaultLookUp.put("postalcode", BCStyle.POSTAL_CODE);
        BCStyle.DefaultLookUp.put("businesscategory", BCStyle.BUSINESS_CATEGORY);
        BCStyle.DefaultLookUp.put("telephonenumber", BCStyle.TELEPHONE_NUMBER);
        BCStyle.DefaultLookUp.put("name", BCStyle.NAME);
        BCStyle.DefaultLookUp.put("organizationidentifier", BCStyle.ORGANIZATION_IDENTIFIER);
        INSTANCE = new BCStyle();
    }
    
    protected BCStyle() {
        this.defaultSymbols = AbstractX500NameStyle.copyHashTable(BCStyle.DefaultSymbols);
        this.defaultLookUp = AbstractX500NameStyle.copyHashTable(BCStyle.DefaultLookUp);
    }
    
    @Override
    public ASN1ObjectIdentifier attrNameToOID(final String s) {
        return IETFUtils.decodeAttrName(s, this.defaultLookUp);
    }
    
    @Override
    protected ASN1Encodable encodeStringValue(final ASN1ObjectIdentifier asn1ObjectIdentifier, final String s) {
        if (asn1ObjectIdentifier.equals(BCStyle.EmailAddress) || asn1ObjectIdentifier.equals(BCStyle.DC)) {
            return new DERIA5String(s);
        }
        if (asn1ObjectIdentifier.equals(BCStyle.DATE_OF_BIRTH)) {
            return new ASN1GeneralizedTime(s);
        }
        if (!asn1ObjectIdentifier.equals(BCStyle.C) && !asn1ObjectIdentifier.equals(BCStyle.SN) && !asn1ObjectIdentifier.equals(BCStyle.DN_QUALIFIER) && !asn1ObjectIdentifier.equals(BCStyle.TELEPHONE_NUMBER)) {
            return super.encodeStringValue(asn1ObjectIdentifier, s);
        }
        return new DERPrintableString(s);
    }
    
    @Override
    public RDN[] fromString(final String s) {
        return IETFUtils.rDNsFromString(s, this);
    }
    
    @Override
    public String[] oidToAttrNames(final ASN1ObjectIdentifier asn1ObjectIdentifier) {
        return IETFUtils.findAttrNamesForOID(asn1ObjectIdentifier, this.defaultLookUp);
    }
    
    @Override
    public String oidToDisplayName(final ASN1ObjectIdentifier asn1ObjectIdentifier) {
        return BCStyle.DefaultSymbols.get(asn1ObjectIdentifier);
    }
    
    @Override
    public String toString(final X500Name x500Name) {
        final StringBuffer sb = new StringBuffer();
        final RDN[] rdNs = x500Name.getRDNs();
        int i = 0;
        int n = 1;
        while (i < rdNs.length) {
            if (n != 0) {
                n = 0;
            }
            else {
                sb.append(',');
            }
            IETFUtils.appendRDN(sb, rdNs[i], this.defaultSymbols);
            ++i;
        }
        return sb.toString();
    }
}
