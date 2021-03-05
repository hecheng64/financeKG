package com.cc.janus.schema.entity;

import com.cc.janus.schema.enuminfo.DataType;
import com.cc.janus.schema.enuminfo.Mapping;

import java.io.Serializable;

public class IndexPropertyKey implements Serializable {
    private static final long serialVersionUID = 7796735172803954936L;
    private String name;
    private DataType dataType;
    private Mapping mapping;
    private String description;

    public IndexPropertyKey() {
    }

    public IndexPropertyKey(String name, DataType dataType, Mapping mapping, String description) {
        this.name = name;
        this.dataType = dataType;
        this.mapping = mapping;
        this.description = description;
    }

    public IndexPropertyKey(DataType dataType, Mapping mapping) {
        this.dataType = dataType;
        this.mapping = mapping;
    }

    public IndexPropertyKey(String name, DataType dataType) {
        this.name = name;
        this.dataType = dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public void setMapping(Mapping mapping) {
        this.mapping = mapping;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public DataType getDataType() {
        return dataType;
    }

    public Mapping getMapping() {
        return mapping;
    }

    public String getDescription() {
        return description;
    }
}
