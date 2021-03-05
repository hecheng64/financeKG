package com.cc.janus.schema.entity;

import com.cc.janus.schema.enuminfo.DataType;
import com.cc.janus.schema.enuminfo.Mapping;

import java.io.Serializable;

public class PropertyKey implements Serializable {
    private static final long serialVersionUID = -7721763136021009013L;
    private String name;
    private String description;
    private DataType dataType;
    private Mapping mapping;


    public PropertyKey() {
    }

    public PropertyKey(String name, String description, DataType dataType, Mapping mapping) {
        this.name = name;
        this.description = description;
        this.dataType = dataType;
        this.mapping = mapping;
    }

    public PropertyKey(String name, DataType dataType, Mapping mapping) {
        this.name = name;
        this.dataType = dataType;
        this.mapping = mapping;
    }

    public PropertyKey(String name, DataType dataType) {
        this.name = name;
        this.dataType = dataType;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public void setMapping(Mapping mapping) {
        this.mapping = mapping;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public DataType getDataType() {
        return dataType;
    }

    public Mapping getMapping() {
        return mapping;
    }
}
