package org.spongycastle.asn1.icao;

import org.spongycastle.asn1.x509.*;
import java.util.*;
import org.spongycastle.asn1.*;

public class LDSSecurityObject extends ASN1Object implements ICAOObjectIdentifiers
{
    public static final int ub_DataGroups = 16;
    private DataGroupHash[] datagroupHash;
    private AlgorithmIdentifier digestAlgorithmIdentifier;
    private ASN1Integer version;
    private LDSVersionInfo versionInfo;
    
    private LDSSecurityObject(final ASN1Sequence asn1Sequence) {
        this.version = new ASN1Integer(0L);
        if (asn1Sequence != null && asn1Sequence.size() != 0) {
            final Enumeration objects = asn1Sequence.getObjects();
            this.version = ASN1Integer.getInstance(objects.nextElement());
            this.digestAlgorithmIdentifier = AlgorithmIdentifier.getInstance(objects.nextElement());
            final ASN1Sequence instance = ASN1Sequence.getInstance(objects.nextElement());
            if (this.version.getValue().intValue() == 1) {
                this.versionInfo = LDSVersionInfo.getInstance(objects.nextElement());
            }
            this.checkDatagroupHashSeqSize(instance.size());
            this.datagroupHash = new DataGroupHash[instance.size()];
            for (int i = 0; i < instance.size(); ++i) {
                this.datagroupHash[i] = DataGroupHash.getInstance(instance.getObjectAt(i));
            }
            return;
        }
        throw new IllegalArgumentException("null or empty sequence passed.");
    }
    
    public LDSSecurityObject(final AlgorithmIdentifier digestAlgorithmIdentifier, final DataGroupHash[] datagroupHash) {
        this.version = new ASN1Integer(0L);
        this.version = new ASN1Integer(0L);
        this.digestAlgorithmIdentifier = digestAlgorithmIdentifier;
        this.datagroupHash = datagroupHash;
        this.checkDatagroupHashSeqSize(datagroupHash.length);
    }
    
    public LDSSecurityObject(final AlgorithmIdentifier digestAlgorithmIdentifier, final DataGroupHash[] datagroupHash, final LDSVersionInfo versionInfo) {
        this.version = new ASN1Integer(0L);
        this.version = new ASN1Integer(1L);
        this.digestAlgorithmIdentifier = digestAlgorithmIdentifier;
        this.datagroupHash = datagroupHash;
        this.versionInfo = versionInfo;
        this.checkDatagroupHashSeqSize(datagroupHash.length);
    }
    
    private void checkDatagroupHashSeqSize(final int n) {
        if (n >= 2 && n <= 16) {
            return;
        }
        throw new IllegalArgumentException("wrong size in DataGroupHashValues : not in (2..16)");
    }
    
    public static LDSSecurityObject getInstance(final Object o) {
        if (o instanceof LDSSecurityObject) {
            return (LDSSecurityObject)o;
        }
        if (o != null) {
            return new LDSSecurityObject(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public DataGroupHash[] getDatagroupHash() {
        return this.datagroupHash;
    }
    
    public AlgorithmIdentifier getDigestAlgorithmIdentifier() {
        return this.digestAlgorithmIdentifier;
    }
    
    public int getVersion() {
        return this.version.getValue().intValue();
    }
    
    public LDSVersionInfo getVersionInfo() {
        return this.versionInfo;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.version);
        asn1EncodableVector.add(this.digestAlgorithmIdentifier);
        final ASN1EncodableVector asn1EncodableVector2 = new ASN1EncodableVector();
        int n = 0;
        while (true) {
            final DataGroupHash[] datagroupHash = this.datagroupHash;
            if (n >= datagroupHash.length) {
                break;
            }
            asn1EncodableVector2.add(datagroupHash[n]);
            ++n;
        }
        asn1EncodableVector.add(new DERSequence(asn1EncodableVector2));
        final LDSVersionInfo versionInfo = this.versionInfo;
        if (versionInfo != null) {
            asn1EncodableVector.add(versionInfo);
        }
        return new DERSequence(asn1EncodableVector);
    }
}
