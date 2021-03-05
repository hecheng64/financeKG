package com.cc.janus.temp;

import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.JanusGraphFactory;
import org.janusgraph.core.PropertyKey;
import org.janusgraph.core.VertexLabel;
import org.janusgraph.core.schema.JanusGraphManagement;
import org.janusgraph.core.schema.SchemaStatus;
import org.janusgraph.graphdb.database.management.ManagementSystem;


public class sanguo {
    public static void main(String[] args) throws InterruptedException {

        JanusGraph graph = JanusGraphFactory.open("conf/graph.properties");

        JanusGraphManagement mgmt = graph.openManagement();

        //创建顶点Label
        VertexLabel luser = mgmt.makeVertexLabel("PERSON").make();
        VertexLabel lphone = mgmt.makeVertexLabel("ITEM").make();
        //创建边Label
        mgmt.makeEdgeLabel("BUY").make();

        //创建属性
        PropertyKey pUid = mgmt.makePropertyKey("uid").dataType(String.class).make();
        PropertyKey pName = mgmt.makePropertyKey("name").dataType(String.class).make();
        PropertyKey pAge = mgmt.makePropertyKey("age").dataType(Integer.class).make();
//        PropertyKey pPhone = mgmt.makePropertyKey("").dataType(String.class).make();

        //创建索引
        String uidIndex = "uidIndex";

        mgmt.buildIndex(uidIndex, Vertex.class)
                .addKey(pUid)
                .indexOnly(luser)
                .unique()
                .buildCompositeIndex();

        mgmt.commit();

        //注册索引
        ManagementSystem
                .awaitGraphIndexStatus(graph, uidIndex)
                .status(SchemaStatus.REGISTERED)
                .call();

        //等待索引ok
        ManagementSystem.awaitGraphIndexStatus(graph, uidIndex).status(SchemaStatus.ENABLED).call();

        graph.close();

    }
}