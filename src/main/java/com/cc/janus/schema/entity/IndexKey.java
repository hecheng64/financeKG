package com.cc.janus.schema.entity;

import com.cc.janus.schema.enuminfo.IndexType;
import org.janusgraph.core.schema.ConsistencyModifier;

import java.io.Serializable;
import java.util.List;

public class IndexKey implements Serializable {
    private static final long serialVersionUID = -1249670488277876224L;
    private List<IndexPropertyKey> propertyKeys;
    private String name;
    private IndexType indexType;
    private ConsistencyModifier consistencyModifier = ConsistencyModifier.DEFAULT;
    private Boolean uniqueIndex = false;
    private Boolean compositeIndex = false;
    private  Boolean mixedIndex = false;
    private String mixIndexName;

    public IndexKey(List<IndexPropertyKey> propertyKeys, String name, IndexType indexType,
                    ConsistencyModifier consistencyModifier, Boolean uniqueIndex, Boolean compositeIndex,
                    Boolean mixedIndex, String mixIndexName) {
        this.propertyKeys = propertyKeys;
        this.name = name;
        this.indexType = indexType;
        this.consistencyModifier = consistencyModifier;
        this.uniqueIndex = uniqueIndex;
        this.compositeIndex = compositeIndex;
        this.mixedIndex = mixedIndex;
        this.mixIndexName = mixIndexName;
    }

    public IndexKey() {
    }

    public void setPropertyKeys(List<IndexPropertyKey> propertyKeys) {
        this.propertyKeys = propertyKeys;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIndexType(IndexType indexType) {
        this.indexType = indexType;
    }

    public void setConsistencyModifier(ConsistencyModifier consistencyModifier) {
        this.consistencyModifier = consistencyModifier;
    }

    public void setUniqueIndex(Boolean uniqueIndex) {
        this.uniqueIndex = uniqueIndex;
    }

    public void setCompositeIndex(Boolean compositeIndex) {
        this.compositeIndex = compositeIndex;
    }

    public void setMixedIndex(Boolean mixedIndex) {
        this.mixedIndex = mixedIndex;
    }

    public void setMixIndexName(String mixIndexName) {
        this.mixIndexName = mixIndexName;
    }

    public List<IndexPropertyKey> getPropertyKeys() {
        return propertyKeys;
    }

    public String getName() {
        return name;
    }

    public IndexType getIndexType() {
        return indexType;
    }

    public ConsistencyModifier getConsistencyModifier() {
        return consistencyModifier;
    }

    public Boolean getUniqueIndex() {
        return uniqueIndex;
    }

    public Boolean getCompositeIndex() {
        return compositeIndex;
    }

    public Boolean getMixedIndex() {
        return mixedIndex;
    }

    public String getMixIndexName() {
        return mixIndexName;
    }
}
