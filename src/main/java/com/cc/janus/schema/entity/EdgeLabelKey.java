package com.cc.janus.schema.entity;

import org.janusgraph.core.Multiplicity;
import org.omg.CORBA.PRIVATE_MEMBER;

import java.io.Serializable;

public class EdgeLabelKey implements Serializable {
    private static final long serialVersionUID = 3000814998539843496L;
    private String name;
    private String signature;
    private String description;
    private Multiplicity multiplicity;

    public EdgeLabelKey(String name, String signature, String description, Multiplicity multiplicity) {
        this.name = name;
        this.signature = signature;
        this.description = description;
        this.multiplicity = multiplicity;
    }

    public EdgeLabelKey() {
    }

    public EdgeLabelKey(String name, String signature, Multiplicity multiplicity) {
        this.name = name;
        this.signature = signature;
        this.multiplicity = multiplicity;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setMultiplicity(Multiplicity multiplicity) {
        this.multiplicity = multiplicity;
    }

    public String getName() {
        return name;
    }

    public String getSignature() {
        return signature;
    }

    public String getDescription() {
        return description;
    }

    public Multiplicity getMultiplicity() {
        return multiplicity;
    }
}
