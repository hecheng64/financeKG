package com.cc.janus.schema.enuminfo;

public enum IndexType {
    Vertex(org.apache.tinkerpop.gremlin.structure.Vertex.class),

    Edge(org.apache.tinkerpop.gremlin.structure.Edge.class);

    private Class<? extends org.apache.tinkerpop.gremlin.structure.Element> clazz;

    private IndexType(Class<? extends org.apache.tinkerpop.gremlin.structure.Element> clazz) {
        this.clazz = clazz;
    }

    public Class<? extends org.apache.tinkerpop.gremlin.structure.Element> getClazz() {
        return clazz;
    }
}
