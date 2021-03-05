package com.cc.janus.schema.entity;

import com.google.gson.Gson;

import java.util.List;

public class Schema {
    private List<VertexLabel> vertices;
    private  List<EdgeLabelKey> edges;
    private List<PropertyKey> propertyKeys;
    private List<IndexKey> indexKeys;

    public Schema(List<VertexLabel> vertices, List<EdgeLabelKey> edges, List<PropertyKey> propertyKeys, List<IndexKey> indexKeys) {
        this.vertices = vertices;
        this.edges = edges;
        this.propertyKeys = propertyKeys;
        this.indexKeys = indexKeys;
    }

    public void setVertices(List<VertexLabel> vertices) {
        this.vertices = vertices;
    }

    public void setEdges(List<EdgeLabelKey> edges) {
        this.edges = edges;
    }

    public void setPropertyKeys(List<PropertyKey> propertyKeys) {
        this.propertyKeys = propertyKeys;
    }

    public void setIndexKeys(List<IndexKey> indexKeys) {
        this.indexKeys = indexKeys;
    }

    public List<VertexLabel> getVertices() {
        return vertices;
    }

    public List<EdgeLabelKey> getEdges() {
        return edges;
    }

    public List<PropertyKey> getPropertyKeys() {
        return propertyKeys;
    }

    public List<IndexKey> getIndexKeys() {
        return indexKeys;
    }
    public synchronized static Schema apply(String content) {
        return new Gson().fromJson(content, Schema.class);
    }
}
