package com.cc.janus.schema.entity;

import java.io.Serializable;

public class VertexLabel implements Serializable {

    private static final long serialVersionUID = 3294333125663500288L;
    private String name;
    private String description;

    public VertexLabel() {
    }

    public VertexLabel(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public VertexLabel(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
