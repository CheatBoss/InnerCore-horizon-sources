package org.spongycastle.asn1.x509;

import org.spongycastle.asn1.x500.*;
import org.spongycastle.asn1.*;
import org.spongycastle.util.*;
import java.util.*;

public class PKIXNameConstraintValidator implements NameConstraintValidator
{
    private Set excludedSubtreesDN;
    private Set excludedSubtreesDNS;
    private Set excludedSubtreesEmail;
    private Set excludedSubtreesIP;
    private Set excludedSubtreesURI;
    private Set permittedSubtreesDN;
    private Set permittedSubtreesDNS;
    private Set permittedSubtreesEmail;
    private Set permittedSubtreesIP;
    private Set permittedSubtreesURI;
    
    public PKIXNameConstraintValidator() {
        this.excludedSubtreesDN = new HashSet();
        this.excludedSubtreesDNS = new HashSet();
        this.excludedSubtreesEmail = new HashSet();
        this.excludedSubtreesURI = new HashSet();
        this.excludedSubtreesIP = new HashSet();
    }
    
    private void checkExcludedDN(final Set set, final ASN1Sequence asn1Sequence) throws NameConstraintValidatorException {
        if (set.isEmpty()) {
            return;
        }
        final Iterator<ASN1Sequence> iterator = set.iterator();
        while (iterator.hasNext()) {
            if (!withinDNSubtree(asn1Sequence, iterator.next())) {
                continue;
            }
            throw new NameConstraintValidatorException("Subject distinguished name is from an excluded subtree");
        }
    }
    
    private void checkExcludedDN(final X500Name x500Name) throws NameConstraintValidatorException {
        this.checkExcludedDN(this.excludedSubtreesDN, ASN1Sequence.getInstance(x500Name));
    }
    
    private void checkExcludedDNS(final Set set, final String s) throws NameConstraintValidatorException {
        if (set.isEmpty()) {
            return;
        }
        for (final String s2 : set) {
            if (!this.withinDomain(s, s2) && !s.equalsIgnoreCase(s2)) {
                continue;
            }
            throw new NameConstraintValidatorException("DNS is from an excluded subtree.");
        }
    }
    
    private void checkExcludedEmail(final Set set, final String s) throws NameConstraintValidatorException {
        if (set.isEmpty()) {
            return;
        }
        final Iterator<String> iterator = set.iterator();
        while (iterator.hasNext()) {
            if (!this.emailIsConstrained(s, iterator.next())) {
                continue;
            }
            throw new NameConstraintValidatorException("Email address is from an excluded subtree.");
        }
    }
    
    private void checkExcludedIP(final Set set, final byte[] array) throws NameConstraintValidatorException {
        if (set.isEmpty()) {
            return;
        }
        final Iterator<byte[]> iterator = set.iterator();
        while (iterator.hasNext()) {
            if (!this.isIPConstrained(array, iterator.next())) {
                continue;
            }
            throw new NameConstraintValidatorException("IP is from an excluded subtree.");
        }
    }
    
    private void checkExcludedURI(final Set set, final String s) throws NameConstraintValidatorException {
        if (set.isEmpty()) {
            return;
        }
        final Iterator<String> iterator = set.iterator();
        while (iterator.hasNext()) {
            if (!this.isUriConstrained(s, iterator.next())) {
                continue;
            }
            throw new NameConstraintValidatorException("URI is from an excluded subtree.");
        }
    }
    
    private void checkPermittedDN(final Set set, final ASN1Sequence asn1Sequence) throws NameConstraintValidatorException {
        if (set == null) {
            return;
        }
        if (set.isEmpty() && asn1Sequence.size() == 0) {
            return;
        }
        final Iterator<ASN1Sequence> iterator = set.iterator();
        while (iterator.hasNext()) {
            if (withinDNSubtree(asn1Sequence, iterator.next())) {
                return;
            }
        }
        throw new NameConstraintValidatorException("Subject distinguished name is not from a permitted subtree");
    }
    
    private void checkPermittedDN(final X500Name x500Name) throws NameConstraintValidatorException {
        this.checkPermittedDN(this.permittedSubtreesDN, ASN1Sequence.getInstance(x500Name.toASN1Primitive()));
    }
    
    private void checkPermittedDNS(final Set set, final String s) throws NameConstraintValidatorException {
        if (set == null) {
            return;
        }
        for (final String s2 : set) {
            if (this.withinDomain(s, s2) || s.equalsIgnoreCase(s2)) {
                return;
            }
        }
        if (s.length() == 0 && set.size() == 0) {
            return;
        }
        throw new NameConstraintValidatorException("DNS is not from a permitted subtree.");
    }
    
    private void checkPermittedEmail(final Set set, final String s) throws NameConstraintValidatorException {
        if (set == null) {
            return;
        }
        final Iterator<String> iterator = set.iterator();
        while (iterator.hasNext()) {
            if (this.emailIsConstrained(s, iterator.next())) {
                return;
            }
        }
        if (s.length() == 0 && set.size() == 0) {
            return;
        }
        throw new NameConstraintValidatorException("Subject email address is not from a permitted subtree.");
    }
    
    private void checkPermittedIP(final Set set, final byte[] array) throws NameConstraintValidatorException {
        if (set == null) {
            return;
        }
        final Iterator<byte[]> iterator = set.iterator();
        while (iterator.hasNext()) {
            if (this.isIPConstrained(array, iterator.next())) {
                return;
            }
        }
        if (array.length == 0 && set.size() == 0) {
            return;
        }
        throw new NameConstraintValidatorException("IP is not from a permitted subtree.");
    }
    
    private void checkPermittedURI(final Set set, final String s) throws NameConstraintValidatorException {
        if (set == null) {
            return;
        }
        final Iterator<String> iterator = set.iterator();
        while (iterator.hasNext()) {
            if (this.isUriConstrained(s, iterator.next())) {
                return;
            }
        }
        if (s.length() == 0 && set.size() == 0) {
            return;
        }
        throw new NameConstraintValidatorException("URI is not from a permitted subtree.");
    }
    
    private boolean collectionsAreEqual(final Collection collection, final Collection collection2) {
        if (collection == collection2) {
            return true;
        }
        if (collection == null) {
            return false;
        }
        if (collection2 == null) {
            return false;
        }
        if (collection.size() != collection2.size()) {
            return false;
        }
    Label_0041:
        for (final Object next : collection) {
            final Iterator<Object> iterator2 = collection2.iterator();
            while (true) {
                while (iterator2.hasNext()) {
                    if (this.equals(next, iterator2.next())) {
                        final boolean b = true;
                        if (!b) {
                            return false;
                        }
                        continue Label_0041;
                    }
                }
                final boolean b = false;
                continue;
            }
        }
        return true;
    }
    
    private static int compareTo(final byte[] array, final byte[] array2) {
        if (Arrays.areEqual(array, array2)) {
            return 0;
        }
        if (Arrays.areEqual(max(array, array2), array)) {
            return 1;
        }
        return -1;
    }
    
    private boolean emailIsConstrained(final String s, final String s2) {
        final String substring = s.substring(s.indexOf(64) + 1);
        if (s2.indexOf(64) != -1) {
            if (s.equalsIgnoreCase(s2)) {
                return true;
            }
        }
        else if (s2.charAt(0) != '.') {
            if (substring.equalsIgnoreCase(s2)) {
                return true;
            }
        }
        else if (this.withinDomain(substring, s2)) {
            return true;
        }
        return false;
    }
    
    private boolean equals(final Object o, final Object o2) {
        if (o == o2) {
            return true;
        }
        if (o == null || o2 == null) {
            return false;
        }
        if (o instanceof byte[] && o2 instanceof byte[]) {
            return Arrays.areEqual((byte[])o, (byte[])o2);
        }
        return o.equals(o2);
    }
    
    private static String extractHostFromURL(String s) {
        final String s2 = s = s.substring(s.indexOf(58) + 1);
        if (s2.indexOf("//") != -1) {
            s = s2.substring(s2.indexOf("//") + 2);
        }
        String substring = s;
        if (s.lastIndexOf(58) != -1) {
            substring = s.substring(0, s.lastIndexOf(58));
        }
        s = substring.substring(substring.indexOf(58) + 1);
        final String s3 = s = s.substring(s.indexOf(64) + 1);
        if (s3.indexOf(47) != -1) {
            s = s3.substring(0, s3.indexOf(47));
        }
        return s;
    }
    
    private byte[][] extractIPsAndSubnetMasks(byte[] array, final byte[] array2) {
        final int n = array.length / 2;
        final byte[] array3 = new byte[n];
        final byte[] array4 = new byte[n];
        System.arraycopy(array, 0, array3, 0, n);
        System.arraycopy(array, n, array4, 0, n);
        array = new byte[n];
        final byte[] array5 = new byte[n];
        System.arraycopy(array2, 0, array, 0, n);
        System.arraycopy(array2, n, array5, 0, n);
        return new byte[][] { array3, array4, array, array5 };
    }
    
    private String extractNameAsString(final GeneralName generalName) {
        return DERIA5String.getInstance(generalName.getName()).getString();
    }
    
    private int hashCollection(final Collection collection) {
        int n = 0;
        if (collection == null) {
            return 0;
        }
        for (final byte[] next : collection) {
            int n2;
            if (next instanceof byte[]) {
                n2 = Arrays.hashCode(next);
            }
            else {
                n2 = next.hashCode();
            }
            n += n2;
        }
        return n;
    }
    
    private Set intersectDN(final Set set, final Set set2) {
        final HashSet<ASN1Sequence> set3 = new HashSet<ASN1Sequence>();
        final Iterator<GeneralSubtree> iterator = set2.iterator();
        while (iterator.hasNext()) {
            final ASN1Sequence instance = ASN1Sequence.getInstance(iterator.next().getBase().getName().toASN1Primitive());
            if (set == null) {
                if (instance == null) {
                    continue;
                }
                set3.add(instance);
            }
            else {
                for (final ASN1Sequence asn1Sequence : set) {
                    if (withinDNSubtree(instance, asn1Sequence)) {
                        set3.add(instance);
                    }
                    else {
                        if (!withinDNSubtree(asn1Sequence, instance)) {
                            continue;
                        }
                        set3.add(asn1Sequence);
                    }
                }
            }
        }
        return set3;
    }
    
    private Set intersectDNS(final Set set, final Set set2) {
        final HashSet<String> set3 = new HashSet<String>();
        final Iterator<GeneralSubtree> iterator = set2.iterator();
        while (iterator.hasNext()) {
            final String nameAsString = this.extractNameAsString(iterator.next().getBase());
            if (set == null) {
                if (nameAsString == null) {
                    continue;
                }
                set3.add(nameAsString);
            }
            else {
                for (final String s : set) {
                    if (this.withinDomain(s, nameAsString)) {
                        set3.add(s);
                    }
                    else {
                        if (!this.withinDomain(nameAsString, s)) {
                            continue;
                        }
                        set3.add(nameAsString);
                    }
                }
            }
        }
        return set3;
    }
    
    private Set intersectEmail(final Set set, final Set set2) {
        final HashSet<String> set3 = new HashSet<String>();
        final Iterator<GeneralSubtree> iterator = set2.iterator();
        while (iterator.hasNext()) {
            final String nameAsString = this.extractNameAsString(iterator.next().getBase());
            if (set == null) {
                if (nameAsString == null) {
                    continue;
                }
                set3.add(nameAsString);
            }
            else {
                final Iterator<String> iterator2 = set.iterator();
                while (iterator2.hasNext()) {
                    this.intersectEmail(nameAsString, iterator2.next(), set3);
                }
            }
        }
        return set3;
    }
    
    private void intersectEmail(final String s, final String s2, final Set set) {
        if (s.indexOf(64) != -1) {
            final String substring = s.substring(s.indexOf(64) + 1);
            if (s2.indexOf(64) != -1) {
                if (s.equalsIgnoreCase(s2)) {
                    set.add(s);
                }
            }
            else if (s2.startsWith(".")) {
                if (this.withinDomain(substring, s2)) {
                    set.add(s);
                }
            }
            else if (substring.equalsIgnoreCase(s2)) {
                set.add(s);
            }
        }
        else if (s.startsWith(".")) {
            if (s2.indexOf(64) != -1) {
                if (this.withinDomain(s2.substring(s.indexOf(64) + 1), s)) {
                    set.add(s2);
                }
            }
            else if (s2.startsWith(".")) {
                if (this.withinDomain(s, s2) || s.equalsIgnoreCase(s2)) {
                    set.add(s);
                    return;
                }
                if (this.withinDomain(s2, s)) {
                    set.add(s2);
                }
            }
            else if (this.withinDomain(s2, s)) {
                set.add(s2);
            }
        }
        else if (s2.indexOf(64) != -1) {
            if (s2.substring(s2.indexOf(64) + 1).equalsIgnoreCase(s)) {
                set.add(s2);
            }
        }
        else if (s2.startsWith(".")) {
            if (this.withinDomain(s, s2)) {
                set.add(s);
            }
        }
        else if (s.equalsIgnoreCase(s2)) {
            set.add(s);
        }
    }
    
    private Set intersectIP(final Set set, final Set set2) {
        final HashSet<Object> set3 = new HashSet<Object>();
        final Iterator<GeneralSubtree> iterator = set2.iterator();
        while (iterator.hasNext()) {
            final byte[] octets = ASN1OctetString.getInstance(iterator.next().getBase().getName()).getOctets();
            if (set == null) {
                if (octets == null) {
                    continue;
                }
                set3.add(octets);
            }
            else {
                final Iterator<byte[]> iterator2 = set.iterator();
                while (iterator2.hasNext()) {
                    set3.addAll(this.intersectIPRange(iterator2.next(), octets));
                }
            }
        }
        return set3;
    }
    
    private Set intersectIPRange(byte[] array, byte[] array2) {
        if (array.length != array2.length) {
            return Collections.EMPTY_SET;
        }
        final byte[][] iPsAndSubnetMasks = this.extractIPsAndSubnetMasks(array, array2);
        final byte[] array3 = iPsAndSubnetMasks[0];
        array = iPsAndSubnetMasks[1];
        final byte[] array4 = iPsAndSubnetMasks[2];
        array2 = iPsAndSubnetMasks[3];
        final byte[][] minMaxIPs = this.minMaxIPs(array3, array, array4, array2);
        if (compareTo(max(minMaxIPs[0], minMaxIPs[2]), min(minMaxIPs[1], minMaxIPs[3])) == 1) {
            return Collections.EMPTY_SET;
        }
        return Collections.singleton(this.ipWithSubnetMask(or(minMaxIPs[0], minMaxIPs[2]), or(array, array2)));
    }
    
    private Set intersectURI(final Set set, final Set set2) {
        final HashSet<String> set3 = new HashSet<String>();
        final Iterator<GeneralSubtree> iterator = set2.iterator();
        while (iterator.hasNext()) {
            final String nameAsString = this.extractNameAsString(iterator.next().getBase());
            if (set == null) {
                if (nameAsString == null) {
                    continue;
                }
                set3.add(nameAsString);
            }
            else {
                final Iterator<String> iterator2 = set.iterator();
                while (iterator2.hasNext()) {
                    this.intersectURI(iterator2.next(), nameAsString, set3);
                }
            }
        }
        return set3;
    }
    
    private void intersectURI(final String s, final String s2, final Set set) {
        if (s.indexOf(64) != -1) {
            final String substring = s.substring(s.indexOf(64) + 1);
            if (s2.indexOf(64) != -1) {
                if (s.equalsIgnoreCase(s2)) {
                    set.add(s);
                }
            }
            else if (s2.startsWith(".")) {
                if (this.withinDomain(substring, s2)) {
                    set.add(s);
                }
            }
            else if (substring.equalsIgnoreCase(s2)) {
                set.add(s);
            }
        }
        else if (s.startsWith(".")) {
            if (s2.indexOf(64) != -1) {
                if (this.withinDomain(s2.substring(s.indexOf(64) + 1), s)) {
                    set.add(s2);
                }
            }
            else if (s2.startsWith(".")) {
                if (this.withinDomain(s, s2) || s.equalsIgnoreCase(s2)) {
                    set.add(s);
                    return;
                }
                if (this.withinDomain(s2, s)) {
                    set.add(s2);
                }
            }
            else if (this.withinDomain(s2, s)) {
                set.add(s2);
            }
        }
        else if (s2.indexOf(64) != -1) {
            if (s2.substring(s2.indexOf(64) + 1).equalsIgnoreCase(s)) {
                set.add(s2);
            }
        }
        else if (s2.startsWith(".")) {
            if (this.withinDomain(s, s2)) {
                set.add(s);
            }
        }
        else if (s.equalsIgnoreCase(s2)) {
            set.add(s);
        }
    }
    
    private byte[] ipWithSubnetMask(final byte[] array, final byte[] array2) {
        final int length = array.length;
        final byte[] array3 = new byte[length * 2];
        System.arraycopy(array, 0, array3, 0, length);
        System.arraycopy(array2, 0, array3, length, length);
        return array3;
    }
    
    private boolean isIPConstrained(final byte[] array, final byte[] array2) {
        final int length = array.length;
        final int n = array2.length / 2;
        int i = 0;
        if (length != n) {
            return false;
        }
        final byte[] array3 = new byte[length];
        System.arraycopy(array2, length, array3, 0, length);
        final byte[] array4 = new byte[length];
        final byte[] array5 = new byte[length];
        while (i < length) {
            array4[i] = (byte)(array2[i] & array3[i]);
            array5[i] = (byte)(array[i] & array3[i]);
            ++i;
        }
        return Arrays.areEqual(array4, array5);
    }
    
    private boolean isUriConstrained(String hostFromURL, final String s) {
        hostFromURL = extractHostFromURL(hostFromURL);
        if (!s.startsWith(".")) {
            if (hostFromURL.equalsIgnoreCase(s)) {
                return true;
            }
        }
        else if (this.withinDomain(hostFromURL, s)) {
            return true;
        }
        return false;
    }
    
    private static byte[] max(final byte[] array, final byte[] array2) {
        for (int i = 0; i < array.length; ++i) {
            if ((array[i] & 0xFFFF) > (0xFFFF & array2[i])) {
                return array;
            }
        }
        return array2;
    }
    
    private static byte[] min(final byte[] array, final byte[] array2) {
        for (int i = 0; i < array.length; ++i) {
            if ((array[i] & 0xFFFF) < (0xFFFF & array2[i])) {
                return array;
            }
        }
        return array2;
    }
    
    private byte[][] minMaxIPs(final byte[] array, final byte[] array2, final byte[] array3, final byte[] array4) {
        final int length = array.length;
        final byte[] array5 = new byte[length];
        final byte[] array6 = new byte[length];
        final byte[] array7 = new byte[length];
        final byte[] array8 = new byte[length];
        for (int i = 0; i < length; ++i) {
            array5[i] = (byte)(array[i] & array2[i]);
            array6[i] = (byte)((array[i] & array2[i]) | ~array2[i]);
            array7[i] = (byte)(array3[i] & array4[i]);
            array8[i] = (byte)((array3[i] & array4[i]) | ~array4[i]);
        }
        return new byte[][] { array5, array6, array7, array8 };
    }
    
    private static byte[] or(final byte[] array, final byte[] array2) {
        final byte[] array3 = new byte[array.length];
        for (int i = 0; i < array.length; ++i) {
            array3[i] = (byte)(array[i] | array2[i]);
        }
        return array3;
    }
    
    private String stringifyIP(final byte[] array) {
        String string = "";
        for (int i = 0; i < array.length / 2; ++i) {
            final StringBuilder sb = new StringBuilder();
            sb.append(string);
            sb.append(Integer.toString(array[i] & 0xFF));
            sb.append(".");
            string = sb.toString();
        }
        final String substring = string.substring(0, string.length() - 1);
        final StringBuilder sb2 = new StringBuilder();
        sb2.append(substring);
        sb2.append("/");
        String s = sb2.toString();
        for (int j = array.length / 2; j < array.length; ++j) {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append(s);
            sb3.append(Integer.toString(array[j] & 0xFF));
            sb3.append(".");
            s = sb3.toString();
        }
        return s.substring(0, s.length() - 1);
    }
    
    private String stringifyIPCollection(final Set set) {
        final StringBuilder sb = new StringBuilder();
        sb.append("");
        sb.append("[");
        final String string = sb.toString();
        final Iterator<byte[]> iterator = set.iterator();
        String string2 = string;
        while (iterator.hasNext()) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(string2);
            sb2.append(this.stringifyIP(iterator.next()));
            sb2.append(",");
            string2 = sb2.toString();
        }
        String substring = string2;
        if (string2.length() > 1) {
            substring = string2.substring(0, string2.length() - 1);
        }
        final StringBuilder sb3 = new StringBuilder();
        sb3.append(substring);
        sb3.append("]");
        return sb3.toString();
    }
    
    private Set unionDN(final Set set, final ASN1Sequence asn1Sequence) {
        if (!set.isEmpty()) {
            final HashSet<ASN1Sequence> set2 = new HashSet<ASN1Sequence>();
            for (final ASN1Sequence asn1Sequence2 : set) {
                if (withinDNSubtree(asn1Sequence, asn1Sequence2)) {
                    set2.add(asn1Sequence2);
                }
                else {
                    if (!withinDNSubtree(asn1Sequence2, asn1Sequence)) {
                        set2.add(asn1Sequence2);
                    }
                    set2.add(asn1Sequence);
                }
            }
            return set2;
        }
        if (asn1Sequence == null) {
            return set;
        }
        set.add(asn1Sequence);
        return set;
    }
    
    private Set unionDNS(final Set set, final String s) {
        if (!set.isEmpty()) {
            final HashSet<String> set2 = new HashSet<String>();
            for (final String s2 : set) {
                if (!this.withinDomain(s2, s)) {
                    if (this.withinDomain(s, s2)) {
                        set2.add(s2);
                        continue;
                    }
                    set2.add(s2);
                }
                set2.add(s);
            }
            return set2;
        }
        if (s == null) {
            return set;
        }
        set.add(s);
        return set;
    }
    
    private Set unionEmail(final Set set, final String s) {
        if (!set.isEmpty()) {
            final HashSet set2 = new HashSet();
            final Iterator<String> iterator = set.iterator();
            while (iterator.hasNext()) {
                this.unionEmail(iterator.next(), s, set2);
            }
            return set2;
        }
        if (s == null) {
            return set;
        }
        set.add(s);
        return set;
    }
    
    private void unionEmail(final String s, final String s2, final Set set) {
        if (s.indexOf(64) != -1) {
            final String substring = s.substring(s.indexOf(64) + 1);
            if (s2.indexOf(64) != -1) {
                if (s.equalsIgnoreCase(s2)) {
                    set.add(s);
                    return;
                }
            }
            else if (s2.startsWith(".")) {
                if (this.withinDomain(substring, s2)) {
                    set.add(s2);
                    return;
                }
            }
            else if (substring.equalsIgnoreCase(s2)) {
                set.add(s2);
                return;
            }
        }
        else if (s.startsWith(".")) {
            if (s2.indexOf(64) != -1) {
                if (this.withinDomain(s2.substring(s.indexOf(64) + 1), s)) {
                    set.add(s);
                    return;
                }
            }
            else if (s2.startsWith(".")) {
                if (this.withinDomain(s, s2) || s.equalsIgnoreCase(s2)) {
                    set.add(s2);
                    return;
                }
                if (this.withinDomain(s2, s)) {
                    set.add(s);
                    return;
                }
            }
            else if (this.withinDomain(s2, s)) {
                set.add(s);
                return;
            }
        }
        else if (s2.indexOf(64) != -1) {
            if (s2.substring(s.indexOf(64) + 1).equalsIgnoreCase(s)) {
                set.add(s);
                return;
            }
        }
        else if (s2.startsWith(".")) {
            if (this.withinDomain(s, s2)) {
                set.add(s2);
                return;
            }
        }
        else if (s.equalsIgnoreCase(s2)) {
            set.add(s);
            return;
        }
        set.add(s);
        set.add(s2);
    }
    
    private Set unionIP(final Set set, final byte[] array) {
        if (!set.isEmpty()) {
            final HashSet set2 = new HashSet();
            final Iterator<byte[]> iterator = set.iterator();
            while (iterator.hasNext()) {
                set2.addAll(this.unionIPRange(iterator.next(), array));
            }
            return set2;
        }
        if (array == null) {
            return set;
        }
        set.add(array);
        return set;
    }
    
    private Set unionIPRange(final byte[] array, final byte[] array2) {
        final HashSet<byte[]> set = new HashSet<byte[]>();
        if (Arrays.areEqual(array, array2)) {
            set.add(array);
            return set;
        }
        set.add(array);
        set.add(array2);
        return set;
    }
    
    private Set unionURI(final Set set, final String s) {
        if (!set.isEmpty()) {
            final HashSet set2 = new HashSet();
            final Iterator<String> iterator = set.iterator();
            while (iterator.hasNext()) {
                this.unionURI(iterator.next(), s, set2);
            }
            return set2;
        }
        if (s == null) {
            return set;
        }
        set.add(s);
        return set;
    }
    
    private void unionURI(final String s, final String s2, final Set set) {
        if (s.indexOf(64) != -1) {
            final String substring = s.substring(s.indexOf(64) + 1);
            if (s2.indexOf(64) != -1) {
                if (s.equalsIgnoreCase(s2)) {
                    set.add(s);
                    return;
                }
            }
            else if (s2.startsWith(".")) {
                if (this.withinDomain(substring, s2)) {
                    set.add(s2);
                    return;
                }
            }
            else if (substring.equalsIgnoreCase(s2)) {
                set.add(s2);
                return;
            }
        }
        else if (s.startsWith(".")) {
            if (s2.indexOf(64) != -1) {
                if (this.withinDomain(s2.substring(s.indexOf(64) + 1), s)) {
                    set.add(s);
                    return;
                }
            }
            else if (s2.startsWith(".")) {
                if (this.withinDomain(s, s2) || s.equalsIgnoreCase(s2)) {
                    set.add(s2);
                    return;
                }
                if (this.withinDomain(s2, s)) {
                    set.add(s);
                    return;
                }
            }
            else if (this.withinDomain(s2, s)) {
                set.add(s);
                return;
            }
        }
        else if (s2.indexOf(64) != -1) {
            if (s2.substring(s.indexOf(64) + 1).equalsIgnoreCase(s)) {
                set.add(s);
                return;
            }
        }
        else if (s2.startsWith(".")) {
            if (this.withinDomain(s, s2)) {
                set.add(s2);
                return;
            }
        }
        else if (s.equalsIgnoreCase(s2)) {
            set.add(s);
            return;
        }
        set.add(s);
        set.add(s2);
    }
    
    private static boolean withinDNSubtree(final ASN1Sequence asn1Sequence, final ASN1Sequence asn1Sequence2) {
        if (asn1Sequence2.size() < 1) {
            return false;
        }
        if (asn1Sequence2.size() > asn1Sequence.size()) {
            return false;
        }
        for (int i = asn1Sequence2.size() - 1; i >= 0; --i) {
            if (!asn1Sequence2.getObjectAt(i).equals(asn1Sequence.getObjectAt(i))) {
                return false;
            }
        }
        return true;
    }
    
    private boolean withinDomain(final String s, final String s2) {
        String substring = s2;
        if (s2.startsWith(".")) {
            substring = s2.substring(1);
        }
        final String[] split = Strings.split(substring, '.');
        final String[] split2 = Strings.split(s, '.');
        if (split2.length <= split.length) {
            return false;
        }
        final int n = split2.length - split.length;
        for (int i = -1; i < split.length; ++i) {
            if (i == -1) {
                if (split2[i + n].equals("")) {
                    return false;
                }
            }
            else if (!split[i].equalsIgnoreCase(split2[i + n])) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public void addExcludedSubtree(final GeneralSubtree generalSubtree) {
        final GeneralName base = generalSubtree.getBase();
        final int tagNo = base.getTagNo();
        if (tagNo == 1) {
            this.excludedSubtreesEmail = this.unionEmail(this.excludedSubtreesEmail, this.extractNameAsString(base));
            return;
        }
        if (tagNo == 2) {
            this.excludedSubtreesDNS = this.unionDNS(this.excludedSubtreesDNS, this.extractNameAsString(base));
            return;
        }
        if (tagNo == 4) {
            this.excludedSubtreesDN = this.unionDN(this.excludedSubtreesDN, (ASN1Sequence)base.getName().toASN1Primitive());
            return;
        }
        if (tagNo == 6) {
            this.excludedSubtreesURI = this.unionURI(this.excludedSubtreesURI, this.extractNameAsString(base));
            return;
        }
        if (tagNo != 7) {
            return;
        }
        this.excludedSubtreesIP = this.unionIP(this.excludedSubtreesIP, ASN1OctetString.getInstance(base.getName()).getOctets());
    }
    
    @Override
    public void checkExcluded(final GeneralName generalName) throws NameConstraintValidatorException {
        final int tagNo = generalName.getTagNo();
        if (tagNo == 1) {
            this.checkExcludedEmail(this.excludedSubtreesEmail, this.extractNameAsString(generalName));
            return;
        }
        if (tagNo == 2) {
            this.checkExcludedDNS(this.excludedSubtreesDNS, DERIA5String.getInstance(generalName.getName()).getString());
            return;
        }
        if (tagNo == 4) {
            this.checkExcludedDN(X500Name.getInstance(generalName.getName()));
            return;
        }
        if (tagNo == 6) {
            this.checkExcludedURI(this.excludedSubtreesURI, DERIA5String.getInstance(generalName.getName()).getString());
            return;
        }
        if (tagNo != 7) {
            return;
        }
        this.checkExcludedIP(this.excludedSubtreesIP, ASN1OctetString.getInstance(generalName.getName()).getOctets());
    }
    
    @Override
    public void checkPermitted(final GeneralName generalName) throws NameConstraintValidatorException {
        final int tagNo = generalName.getTagNo();
        if (tagNo == 1) {
            this.checkPermittedEmail(this.permittedSubtreesEmail, this.extractNameAsString(generalName));
            return;
        }
        if (tagNo == 2) {
            this.checkPermittedDNS(this.permittedSubtreesDNS, DERIA5String.getInstance(generalName.getName()).getString());
            return;
        }
        if (tagNo == 4) {
            this.checkPermittedDN(X500Name.getInstance(generalName.getName()));
            return;
        }
        if (tagNo == 6) {
            this.checkPermittedURI(this.permittedSubtreesURI, DERIA5String.getInstance(generalName.getName()).getString());
            return;
        }
        if (tagNo != 7) {
            return;
        }
        this.checkPermittedIP(this.permittedSubtreesIP, ASN1OctetString.getInstance(generalName.getName()).getOctets());
    }
    
    @Override
    public boolean equals(final Object o) {
        final boolean b = o instanceof PKIXNameConstraintValidator;
        final boolean b2 = false;
        if (!b) {
            return false;
        }
        final PKIXNameConstraintValidator pkixNameConstraintValidator = (PKIXNameConstraintValidator)o;
        boolean b3 = b2;
        if (this.collectionsAreEqual(pkixNameConstraintValidator.excludedSubtreesDN, this.excludedSubtreesDN)) {
            b3 = b2;
            if (this.collectionsAreEqual(pkixNameConstraintValidator.excludedSubtreesDNS, this.excludedSubtreesDNS)) {
                b3 = b2;
                if (this.collectionsAreEqual(pkixNameConstraintValidator.excludedSubtreesEmail, this.excludedSubtreesEmail)) {
                    b3 = b2;
                    if (this.collectionsAreEqual(pkixNameConstraintValidator.excludedSubtreesIP, this.excludedSubtreesIP)) {
                        b3 = b2;
                        if (this.collectionsAreEqual(pkixNameConstraintValidator.excludedSubtreesURI, this.excludedSubtreesURI)) {
                            b3 = b2;
                            if (this.collectionsAreEqual(pkixNameConstraintValidator.permittedSubtreesDN, this.permittedSubtreesDN)) {
                                b3 = b2;
                                if (this.collectionsAreEqual(pkixNameConstraintValidator.permittedSubtreesDNS, this.permittedSubtreesDNS)) {
                                    b3 = b2;
                                    if (this.collectionsAreEqual(pkixNameConstraintValidator.permittedSubtreesEmail, this.permittedSubtreesEmail)) {
                                        b3 = b2;
                                        if (this.collectionsAreEqual(pkixNameConstraintValidator.permittedSubtreesIP, this.permittedSubtreesIP)) {
                                            b3 = b2;
                                            if (this.collectionsAreEqual(pkixNameConstraintValidator.permittedSubtreesURI, this.permittedSubtreesURI)) {
                                                b3 = true;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return b3;
    }
    
    @Override
    public int hashCode() {
        return this.hashCollection(this.excludedSubtreesDN) + this.hashCollection(this.excludedSubtreesDNS) + this.hashCollection(this.excludedSubtreesEmail) + this.hashCollection(this.excludedSubtreesIP) + this.hashCollection(this.excludedSubtreesURI) + this.hashCollection(this.permittedSubtreesDN) + this.hashCollection(this.permittedSubtreesDNS) + this.hashCollection(this.permittedSubtreesEmail) + this.hashCollection(this.permittedSubtreesIP) + this.hashCollection(this.permittedSubtreesURI);
    }
    
    @Override
    public void intersectEmptyPermittedSubtree(final int n) {
        if (n == 1) {
            this.permittedSubtreesEmail = new HashSet();
            return;
        }
        if (n == 2) {
            this.permittedSubtreesDNS = new HashSet();
            return;
        }
        if (n == 4) {
            this.permittedSubtreesDN = new HashSet();
            return;
        }
        if (n == 6) {
            this.permittedSubtreesURI = new HashSet();
            return;
        }
        if (n != 7) {
            return;
        }
        this.permittedSubtreesIP = new HashSet();
    }
    
    @Override
    public void intersectPermittedSubtree(final GeneralSubtree generalSubtree) {
        this.intersectPermittedSubtree(new GeneralSubtree[] { generalSubtree });
    }
    
    @Override
    public void intersectPermittedSubtree(final GeneralSubtree[] array) {
        final HashMap<Object, HashSet<GeneralSubtree>> hashMap = new HashMap<Object, HashSet<GeneralSubtree>>();
        for (int i = 0; i != array.length; ++i) {
            final GeneralSubtree generalSubtree = array[i];
            final Integer value = Integers.valueOf(generalSubtree.getBase().getTagNo());
            if (hashMap.get(value) == null) {
                hashMap.put(value, new HashSet<GeneralSubtree>());
            }
            hashMap.get(value).add(generalSubtree);
        }
        for (final Map.Entry<Integer, HashSet<GeneralSubtree>> entry : hashMap.entrySet()) {
            final int intValue = entry.getKey();
            if (intValue != 1) {
                if (intValue != 2) {
                    if (intValue != 4) {
                        if (intValue != 6) {
                            if (intValue != 7) {
                                continue;
                            }
                            this.permittedSubtreesIP = this.intersectIP(this.permittedSubtreesIP, entry.getValue());
                        }
                        else {
                            this.permittedSubtreesURI = this.intersectURI(this.permittedSubtreesURI, entry.getValue());
                        }
                    }
                    else {
                        this.permittedSubtreesDN = this.intersectDN(this.permittedSubtreesDN, entry.getValue());
                    }
                }
                else {
                    this.permittedSubtreesDNS = this.intersectDNS(this.permittedSubtreesDNS, entry.getValue());
                }
            }
            else {
                this.permittedSubtreesEmail = this.intersectEmail(this.permittedSubtreesEmail, entry.getValue());
            }
        }
    }
    
    @Override
    public String toString() {
        final Set permittedSubtreesDN = this.permittedSubtreesDN;
        String string = "permitted:\n";
        if (permittedSubtreesDN != null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("permitted:\n");
            sb.append("DN:\n");
            final String string2 = sb.toString();
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(string2);
            sb2.append(this.permittedSubtreesDN.toString());
            sb2.append("\n");
            string = sb2.toString();
        }
        String string3 = string;
        if (this.permittedSubtreesDNS != null) {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append(string);
            sb3.append("DNS:\n");
            final String string4 = sb3.toString();
            final StringBuilder sb4 = new StringBuilder();
            sb4.append(string4);
            sb4.append(this.permittedSubtreesDNS.toString());
            sb4.append("\n");
            string3 = sb4.toString();
        }
        String string5 = string3;
        if (this.permittedSubtreesEmail != null) {
            final StringBuilder sb5 = new StringBuilder();
            sb5.append(string3);
            sb5.append("Email:\n");
            final String string6 = sb5.toString();
            final StringBuilder sb6 = new StringBuilder();
            sb6.append(string6);
            sb6.append(this.permittedSubtreesEmail.toString());
            sb6.append("\n");
            string5 = sb6.toString();
        }
        String string7 = string5;
        if (this.permittedSubtreesURI != null) {
            final StringBuilder sb7 = new StringBuilder();
            sb7.append(string5);
            sb7.append("URI:\n");
            final String string8 = sb7.toString();
            final StringBuilder sb8 = new StringBuilder();
            sb8.append(string8);
            sb8.append(this.permittedSubtreesURI.toString());
            sb8.append("\n");
            string7 = sb8.toString();
        }
        String string9 = string7;
        if (this.permittedSubtreesIP != null) {
            final StringBuilder sb9 = new StringBuilder();
            sb9.append(string7);
            sb9.append("IP:\n");
            final String string10 = sb9.toString();
            final StringBuilder sb10 = new StringBuilder();
            sb10.append(string10);
            sb10.append(this.stringifyIPCollection(this.permittedSubtreesIP));
            sb10.append("\n");
            string9 = sb10.toString();
        }
        final StringBuilder sb11 = new StringBuilder();
        sb11.append(string9);
        sb11.append("excluded:\n");
        String s2;
        final String s = s2 = sb11.toString();
        if (!this.excludedSubtreesDN.isEmpty()) {
            final StringBuilder sb12 = new StringBuilder();
            sb12.append(s);
            sb12.append("DN:\n");
            final String string11 = sb12.toString();
            final StringBuilder sb13 = new StringBuilder();
            sb13.append(string11);
            sb13.append(this.excludedSubtreesDN.toString());
            sb13.append("\n");
            s2 = sb13.toString();
        }
        String string12 = s2;
        if (!this.excludedSubtreesDNS.isEmpty()) {
            final StringBuilder sb14 = new StringBuilder();
            sb14.append(s2);
            sb14.append("DNS:\n");
            final String string13 = sb14.toString();
            final StringBuilder sb15 = new StringBuilder();
            sb15.append(string13);
            sb15.append(this.excludedSubtreesDNS.toString());
            sb15.append("\n");
            string12 = sb15.toString();
        }
        String string14 = string12;
        if (!this.excludedSubtreesEmail.isEmpty()) {
            final StringBuilder sb16 = new StringBuilder();
            sb16.append(string12);
            sb16.append("Email:\n");
            final String string15 = sb16.toString();
            final StringBuilder sb17 = new StringBuilder();
            sb17.append(string15);
            sb17.append(this.excludedSubtreesEmail.toString());
            sb17.append("\n");
            string14 = sb17.toString();
        }
        String string16 = string14;
        if (!this.excludedSubtreesURI.isEmpty()) {
            final StringBuilder sb18 = new StringBuilder();
            sb18.append(string14);
            sb18.append("URI:\n");
            final String string17 = sb18.toString();
            final StringBuilder sb19 = new StringBuilder();
            sb19.append(string17);
            sb19.append(this.excludedSubtreesURI.toString());
            sb19.append("\n");
            string16 = sb19.toString();
        }
        String string18 = string16;
        if (!this.excludedSubtreesIP.isEmpty()) {
            final StringBuilder sb20 = new StringBuilder();
            sb20.append(string16);
            sb20.append("IP:\n");
            final String string19 = sb20.toString();
            final StringBuilder sb21 = new StringBuilder();
            sb21.append(string19);
            sb21.append(this.stringifyIPCollection(this.excludedSubtreesIP));
            sb21.append("\n");
            string18 = sb21.toString();
        }
        return string18;
    }
}
