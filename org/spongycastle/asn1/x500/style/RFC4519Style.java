package org.spongycastle.asn1.x500.style;

import java.util.*;
import org.spongycastle.asn1.*;
import org.spongycastle.asn1.x500.*;

public class RFC4519Style extends AbstractX500NameStyle
{
    private static final Hashtable DefaultLookUp;
    private static final Hashtable DefaultSymbols;
    public static final X500NameStyle INSTANCE;
    public static final ASN1ObjectIdentifier businessCategory;
    public static final ASN1ObjectIdentifier c;
    public static final ASN1ObjectIdentifier cn;
    public static final ASN1ObjectIdentifier dc;
    public static final ASN1ObjectIdentifier description;
    public static final ASN1ObjectIdentifier destinationIndicator;
    public static final ASN1ObjectIdentifier distinguishedName;
    public static final ASN1ObjectIdentifier dnQualifier;
    public static final ASN1ObjectIdentifier enhancedSearchGuide;
    public static final ASN1ObjectIdentifier facsimileTelephoneNumber;
    public static final ASN1ObjectIdentifier generationQualifier;
    public static final ASN1ObjectIdentifier givenName;
    public static final ASN1ObjectIdentifier houseIdentifier;
    public static final ASN1ObjectIdentifier initials;
    public static final ASN1ObjectIdentifier internationalISDNNumber;
    public static final ASN1ObjectIdentifier l;
    public static final ASN1ObjectIdentifier member;
    public static final ASN1ObjectIdentifier name;
    public static final ASN1ObjectIdentifier o;
    public static final ASN1ObjectIdentifier ou;
    public static final ASN1ObjectIdentifier owner;
    public static final ASN1ObjectIdentifier physicalDeliveryOfficeName;
    public static final ASN1ObjectIdentifier postOfficeBox;
    public static final ASN1ObjectIdentifier postalAddress;
    public static final ASN1ObjectIdentifier postalCode;
    public static final ASN1ObjectIdentifier preferredDeliveryMethod;
    public static final ASN1ObjectIdentifier registeredAddress;
    public static final ASN1ObjectIdentifier roleOccupant;
    public static final ASN1ObjectIdentifier searchGuide;
    public static final ASN1ObjectIdentifier seeAlso;
    public static final ASN1ObjectIdentifier serialNumber;
    public static final ASN1ObjectIdentifier sn;
    public static final ASN1ObjectIdentifier st;
    public static final ASN1ObjectIdentifier street;
    public static final ASN1ObjectIdentifier telephoneNumber;
    public static final ASN1ObjectIdentifier teletexTerminalIdentifier;
    public static final ASN1ObjectIdentifier telexNumber;
    public static final ASN1ObjectIdentifier title;
    public static final ASN1ObjectIdentifier uid;
    public static final ASN1ObjectIdentifier uniqueMember;
    public static final ASN1ObjectIdentifier userPassword;
    public static final ASN1ObjectIdentifier x121Address;
    public static final ASN1ObjectIdentifier x500UniqueIdentifier;
    protected final Hashtable defaultLookUp;
    protected final Hashtable defaultSymbols;
    
    static {
        businessCategory = new ASN1ObjectIdentifier("2.5.4.15").intern();
        c = new ASN1ObjectIdentifier("2.5.4.6").intern();
        cn = new ASN1ObjectIdentifier("2.5.4.3").intern();
        dc = new ASN1ObjectIdentifier("0.9.2342.19200300.100.1.25").intern();
        description = new ASN1ObjectIdentifier("2.5.4.13").intern();
        destinationIndicator = new ASN1ObjectIdentifier("2.5.4.27").intern();
        distinguishedName = new ASN1ObjectIdentifier("2.5.4.49").intern();
        dnQualifier = new ASN1ObjectIdentifier("2.5.4.46").intern();
        enhancedSearchGuide = new ASN1ObjectIdentifier("2.5.4.47").intern();
        facsimileTelephoneNumber = new ASN1ObjectIdentifier("2.5.4.23").intern();
        generationQualifier = new ASN1ObjectIdentifier("2.5.4.44").intern();
        givenName = new ASN1ObjectIdentifier("2.5.4.42").intern();
        houseIdentifier = new ASN1ObjectIdentifier("2.5.4.51").intern();
        initials = new ASN1ObjectIdentifier("2.5.4.43").intern();
        internationalISDNNumber = new ASN1ObjectIdentifier("2.5.4.25").intern();
        l = new ASN1ObjectIdentifier("2.5.4.7").intern();
        member = new ASN1ObjectIdentifier("2.5.4.31").intern();
        name = new ASN1ObjectIdentifier("2.5.4.41").intern();
        o = new ASN1ObjectIdentifier("2.5.4.10").intern();
        ou = new ASN1ObjectIdentifier("2.5.4.11").intern();
        owner = new ASN1ObjectIdentifier("2.5.4.32").intern();
        physicalDeliveryOfficeName = new ASN1ObjectIdentifier("2.5.4.19").intern();
        postalAddress = new ASN1ObjectIdentifier("2.5.4.16").intern();
        postalCode = new ASN1ObjectIdentifier("2.5.4.17").intern();
        postOfficeBox = new ASN1ObjectIdentifier("2.5.4.18").intern();
        preferredDeliveryMethod = new ASN1ObjectIdentifier("2.5.4.28").intern();
        registeredAddress = new ASN1ObjectIdentifier("2.5.4.26").intern();
        roleOccupant = new ASN1ObjectIdentifier("2.5.4.33").intern();
        searchGuide = new ASN1ObjectIdentifier("2.5.4.14").intern();
        seeAlso = new ASN1ObjectIdentifier("2.5.4.34").intern();
        serialNumber = new ASN1ObjectIdentifier("2.5.4.5").intern();
        sn = new ASN1ObjectIdentifier("2.5.4.4").intern();
        st = new ASN1ObjectIdentifier("2.5.4.8").intern();
        street = new ASN1ObjectIdentifier("2.5.4.9").intern();
        telephoneNumber = new ASN1ObjectIdentifier("2.5.4.20").intern();
        teletexTerminalIdentifier = new ASN1ObjectIdentifier("2.5.4.22").intern();
        telexNumber = new ASN1ObjectIdentifier("2.5.4.21").intern();
        title = new ASN1ObjectIdentifier("2.5.4.12").intern();
        uid = new ASN1ObjectIdentifier("0.9.2342.19200300.100.1.1").intern();
        uniqueMember = new ASN1ObjectIdentifier("2.5.4.50").intern();
        userPassword = new ASN1ObjectIdentifier("2.5.4.35").intern();
        x121Address = new ASN1ObjectIdentifier("2.5.4.24").intern();
        x500UniqueIdentifier = new ASN1ObjectIdentifier("2.5.4.45").intern();
        DefaultSymbols = new Hashtable();
        DefaultLookUp = new Hashtable();
        RFC4519Style.DefaultSymbols.put(RFC4519Style.businessCategory, "businessCategory");
        RFC4519Style.DefaultSymbols.put(RFC4519Style.c, "c");
        RFC4519Style.DefaultSymbols.put(RFC4519Style.cn, "cn");
        RFC4519Style.DefaultSymbols.put(RFC4519Style.dc, "dc");
        RFC4519Style.DefaultSymbols.put(RFC4519Style.description, "description");
        RFC4519Style.DefaultSymbols.put(RFC4519Style.destinationIndicator, "destinationIndicator");
        RFC4519Style.DefaultSymbols.put(RFC4519Style.distinguishedName, "distinguishedName");
        RFC4519Style.DefaultSymbols.put(RFC4519Style.dnQualifier, "dnQualifier");
        RFC4519Style.DefaultSymbols.put(RFC4519Style.enhancedSearchGuide, "enhancedSearchGuide");
        RFC4519Style.DefaultSymbols.put(RFC4519Style.facsimileTelephoneNumber, "facsimileTelephoneNumber");
        RFC4519Style.DefaultSymbols.put(RFC4519Style.generationQualifier, "generationQualifier");
        RFC4519Style.DefaultSymbols.put(RFC4519Style.givenName, "givenName");
        RFC4519Style.DefaultSymbols.put(RFC4519Style.houseIdentifier, "houseIdentifier");
        RFC4519Style.DefaultSymbols.put(RFC4519Style.initials, "initials");
        RFC4519Style.DefaultSymbols.put(RFC4519Style.internationalISDNNumber, "internationalISDNNumber");
        RFC4519Style.DefaultSymbols.put(RFC4519Style.l, "l");
        RFC4519Style.DefaultSymbols.put(RFC4519Style.member, "member");
        RFC4519Style.DefaultSymbols.put(RFC4519Style.name, "name");
        RFC4519Style.DefaultSymbols.put(RFC4519Style.o, "o");
        RFC4519Style.DefaultSymbols.put(RFC4519Style.ou, "ou");
        RFC4519Style.DefaultSymbols.put(RFC4519Style.owner, "owner");
        RFC4519Style.DefaultSymbols.put(RFC4519Style.physicalDeliveryOfficeName, "physicalDeliveryOfficeName");
        RFC4519Style.DefaultSymbols.put(RFC4519Style.postalAddress, "postalAddress");
        RFC4519Style.DefaultSymbols.put(RFC4519Style.postalCode, "postalCode");
        RFC4519Style.DefaultSymbols.put(RFC4519Style.postOfficeBox, "postOfficeBox");
        RFC4519Style.DefaultSymbols.put(RFC4519Style.preferredDeliveryMethod, "preferredDeliveryMethod");
        RFC4519Style.DefaultSymbols.put(RFC4519Style.registeredAddress, "registeredAddress");
        RFC4519Style.DefaultSymbols.put(RFC4519Style.roleOccupant, "roleOccupant");
        RFC4519Style.DefaultSymbols.put(RFC4519Style.searchGuide, "searchGuide");
        RFC4519Style.DefaultSymbols.put(RFC4519Style.seeAlso, "seeAlso");
        RFC4519Style.DefaultSymbols.put(RFC4519Style.serialNumber, "serialNumber");
        RFC4519Style.DefaultSymbols.put(RFC4519Style.sn, "sn");
        RFC4519Style.DefaultSymbols.put(RFC4519Style.st, "st");
        RFC4519Style.DefaultSymbols.put(RFC4519Style.street, "street");
        RFC4519Style.DefaultSymbols.put(RFC4519Style.telephoneNumber, "telephoneNumber");
        RFC4519Style.DefaultSymbols.put(RFC4519Style.teletexTerminalIdentifier, "teletexTerminalIdentifier");
        RFC4519Style.DefaultSymbols.put(RFC4519Style.telexNumber, "telexNumber");
        RFC4519Style.DefaultSymbols.put(RFC4519Style.title, "title");
        RFC4519Style.DefaultSymbols.put(RFC4519Style.uid, "uid");
        RFC4519Style.DefaultSymbols.put(RFC4519Style.uniqueMember, "uniqueMember");
        RFC4519Style.DefaultSymbols.put(RFC4519Style.userPassword, "userPassword");
        RFC4519Style.DefaultSymbols.put(RFC4519Style.x121Address, "x121Address");
        RFC4519Style.DefaultSymbols.put(RFC4519Style.x500UniqueIdentifier, "x500UniqueIdentifier");
        RFC4519Style.DefaultLookUp.put("businesscategory", RFC4519Style.businessCategory);
        RFC4519Style.DefaultLookUp.put("c", RFC4519Style.c);
        RFC4519Style.DefaultLookUp.put("cn", RFC4519Style.cn);
        RFC4519Style.DefaultLookUp.put("dc", RFC4519Style.dc);
        RFC4519Style.DefaultLookUp.put("description", RFC4519Style.description);
        RFC4519Style.DefaultLookUp.put("destinationindicator", RFC4519Style.destinationIndicator);
        RFC4519Style.DefaultLookUp.put("distinguishedname", RFC4519Style.distinguishedName);
        RFC4519Style.DefaultLookUp.put("dnqualifier", RFC4519Style.dnQualifier);
        RFC4519Style.DefaultLookUp.put("enhancedsearchguide", RFC4519Style.enhancedSearchGuide);
        RFC4519Style.DefaultLookUp.put("facsimiletelephonenumber", RFC4519Style.facsimileTelephoneNumber);
        RFC4519Style.DefaultLookUp.put("generationqualifier", RFC4519Style.generationQualifier);
        RFC4519Style.DefaultLookUp.put("givenname", RFC4519Style.givenName);
        RFC4519Style.DefaultLookUp.put("houseidentifier", RFC4519Style.houseIdentifier);
        RFC4519Style.DefaultLookUp.put("initials", RFC4519Style.initials);
        RFC4519Style.DefaultLookUp.put("internationalisdnnumber", RFC4519Style.internationalISDNNumber);
        RFC4519Style.DefaultLookUp.put("l", RFC4519Style.l);
        RFC4519Style.DefaultLookUp.put("member", RFC4519Style.member);
        RFC4519Style.DefaultLookUp.put("name", RFC4519Style.name);
        RFC4519Style.DefaultLookUp.put("o", RFC4519Style.o);
        RFC4519Style.DefaultLookUp.put("ou", RFC4519Style.ou);
        RFC4519Style.DefaultLookUp.put("owner", RFC4519Style.owner);
        RFC4519Style.DefaultLookUp.put("physicaldeliveryofficename", RFC4519Style.physicalDeliveryOfficeName);
        RFC4519Style.DefaultLookUp.put("postaladdress", RFC4519Style.postalAddress);
        RFC4519Style.DefaultLookUp.put("postalcode", RFC4519Style.postalCode);
        RFC4519Style.DefaultLookUp.put("postofficebox", RFC4519Style.postOfficeBox);
        RFC4519Style.DefaultLookUp.put("preferreddeliverymethod", RFC4519Style.preferredDeliveryMethod);
        RFC4519Style.DefaultLookUp.put("registeredaddress", RFC4519Style.registeredAddress);
        RFC4519Style.DefaultLookUp.put("roleoccupant", RFC4519Style.roleOccupant);
        RFC4519Style.DefaultLookUp.put("searchguide", RFC4519Style.searchGuide);
        RFC4519Style.DefaultLookUp.put("seealso", RFC4519Style.seeAlso);
        RFC4519Style.DefaultLookUp.put("serialnumber", RFC4519Style.serialNumber);
        RFC4519Style.DefaultLookUp.put("sn", RFC4519Style.sn);
        RFC4519Style.DefaultLookUp.put("st", RFC4519Style.st);
        RFC4519Style.DefaultLookUp.put("street", RFC4519Style.street);
        RFC4519Style.DefaultLookUp.put("telephonenumber", RFC4519Style.telephoneNumber);
        RFC4519Style.DefaultLookUp.put("teletexterminalidentifier", RFC4519Style.teletexTerminalIdentifier);
        RFC4519Style.DefaultLookUp.put("telexnumber", RFC4519Style.telexNumber);
        RFC4519Style.DefaultLookUp.put("title", RFC4519Style.title);
        RFC4519Style.DefaultLookUp.put("uid", RFC4519Style.uid);
        RFC4519Style.DefaultLookUp.put("uniquemember", RFC4519Style.uniqueMember);
        RFC4519Style.DefaultLookUp.put("userpassword", RFC4519Style.userPassword);
        RFC4519Style.DefaultLookUp.put("x121address", RFC4519Style.x121Address);
        RFC4519Style.DefaultLookUp.put("x500uniqueidentifier", RFC4519Style.x500UniqueIdentifier);
        INSTANCE = new RFC4519Style();
    }
    
    protected RFC4519Style() {
        this.defaultSymbols = AbstractX500NameStyle.copyHashTable(RFC4519Style.DefaultSymbols);
        this.defaultLookUp = AbstractX500NameStyle.copyHashTable(RFC4519Style.DefaultLookUp);
    }
    
    @Override
    public ASN1ObjectIdentifier attrNameToOID(final String s) {
        return IETFUtils.decodeAttrName(s, this.defaultLookUp);
    }
    
    @Override
    protected ASN1Encodable encodeStringValue(final ASN1ObjectIdentifier asn1ObjectIdentifier, final String s) {
        if (asn1ObjectIdentifier.equals(RFC4519Style.dc)) {
            return new DERIA5String(s);
        }
        if (!asn1ObjectIdentifier.equals(RFC4519Style.c) && !asn1ObjectIdentifier.equals(RFC4519Style.serialNumber) && !asn1ObjectIdentifier.equals(RFC4519Style.dnQualifier) && !asn1ObjectIdentifier.equals(RFC4519Style.telephoneNumber)) {
            return super.encodeStringValue(asn1ObjectIdentifier, s);
        }
        return new DERPrintableString(s);
    }
    
    @Override
    public RDN[] fromString(final String s) {
        final RDN[] rdNsFromString = IETFUtils.rDNsFromString(s, this);
        final int length = rdNsFromString.length;
        final RDN[] array = new RDN[length];
        for (int i = 0; i != rdNsFromString.length; ++i) {
            array[length - i - 1] = rdNsFromString[i];
        }
        return array;
    }
    
    @Override
    public String[] oidToAttrNames(final ASN1ObjectIdentifier asn1ObjectIdentifier) {
        return IETFUtils.findAttrNamesForOID(asn1ObjectIdentifier, this.defaultLookUp);
    }
    
    @Override
    public String oidToDisplayName(final ASN1ObjectIdentifier asn1ObjectIdentifier) {
        return RFC4519Style.DefaultSymbols.get(asn1ObjectIdentifier);
    }
    
    @Override
    public String toString(final X500Name x500Name) {
        final StringBuffer sb = new StringBuffer();
        final RDN[] rdNs = x500Name.getRDNs();
        final int length = rdNs.length;
        int n = 1;
        for (int i = length - 1; i >= 0; --i) {
            if (n != 0) {
                n = 0;
            }
            else {
                sb.append(',');
            }
            IETFUtils.appendRDN(sb, rdNs[i], this.defaultSymbols);
        }
        return sb.toString();
    }
}
