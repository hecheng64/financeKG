package com.cc.janus.temp;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.tinkerpop.gremlin.driver.Client;
import org.apache.tinkerpop.gremlin.driver.Cluster;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.*;
import org.janusgraph.core.schema.JanusGraphManagement;
import org.janusgraph.core.schema.Mapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Schema {
    private static final  String JANUSGRAPH = "janusgraph01";
    private static final Logger LOGGER = LoggerFactory.getLogger(Schema.class);
    public static final String CONFIG_FILE = "conf/graph.properties";

    public static final String BACKING_INDEX = "search";
    public static final String USER = "user";
    public static final String USER_NAME = "janus.username";
    public static final String STATUS_UPDATE = "statusupdate";
    public static final String CONTENT ="janus.content";
    public static final String CREATED_AT = "janus.createdAt";
    public static final String POSTS = "posts";
    public static final String FOllOWS = "follows";

    private  JanusGraph graph;
    private  JanusGraphManagement mgt;
    private Configuration conf;
    private Cluster cluster;
    private Client client;
    private  GraphTraversalSource g;

    public Schema(String configFile) throws ConfigurationException {
        LOGGER.info("connect graph");
//        conf = new PropertiesConfiguration(configFile);
//        try {
//            cluster = Cluster.open(conf.getString("gremlin.remote.driver.clusterFile"));
//            client = cluster.connect();
//        }catch (Exception e){
//            throw new ConfigurationException(e);
//        }
//        graph = EmptyGraph.instance();
//        g = graph.traversal().withRemote(conf);
        graph = JanusGraphFactory.open(configFile);
        LOGGER.info("connect management");
        mgt = graph.openManagement();

    }
    private void createUserSchema(){
        LOGGER.info("create{}schema", USER);
        VertexLabel user = mgt.makeVertexLabel(USER).make();
        PropertyKey username = mgt.makePropertyKey(USER_NAME).dataType(String.class).make();

        mgt.buildIndex(indexName(USER,USER_NAME), Vertex.class).
                addKey(username, Mapping.STRING.asParameter()).
                indexOnly(user).
                buildMixedIndex(BACKING_INDEX);

    }
    private void createEdgeSchema(){
        LOGGER.info("create edge schema");
        EdgeLabel posts = mgt.makeEdgeLabel(POSTS).make();
        EdgeLabel follows =mgt.makeEdgeLabel(FOllOWS).make();
        PropertyKey createAt = mgt.makePropertyKey(CREATED_AT).dataType(Long.class).make();

        mgt.buildIndex(indexName(POSTS,CREATED_AT), Edge.class).
                addKey(createAt).
                indexOnly(posts).
                buildMixedIndex(BACKING_INDEX);

        mgt.buildIndex(indexName(FOllOWS,CREATED_AT),Edge.class).
                addKey(createAt).
                indexOnly(follows).
                buildMixedIndex(BACKING_INDEX);

    }
    private void createStatusUpdateSchema(){
        LOGGER.info("create{}schema",STATUS_UPDATE);
        VertexLabel statusUpate = mgt.makeVertexLabel(STATUS_UPDATE).make();
        PropertyKey content =mgt.makePropertyKey(CONTENT).dataType(String.class).make();

        mgt.buildIndex(indexName(STATUS_UPDATE,CONTENT),Vertex.class).
                addKey(content,Mapping.TEXTSTRING.asParameter()).
                indexOnly(statusUpate).
                buildMixedIndex(BACKING_INDEX);

    }
    private void close(){
        mgt.commit();
        graph.tx().commit();
        graph.close();

    }
    public static String indexName(String label,String propertyKey){
        return label + ":by:" + propertyKey;
    }

    public  static void main(String args[]) throws ConfigurationException {
//        JanusGraph graph = JanusGraphFactory.build().set("storage.backend", "hbase")
//                                                    .set("storage.hostname","node1,node2,node3")
//                                                    .open();
//
//        try {
////            GraphTraversalSource g = graph.traversal().withRemote("conf/remote-graph.properties");
////            Object herculesAge = g.V().has("name", "hercules").values("age").next();
////            System.out.println("Hercules is " + herculesAge + " years old.");
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        Schema schema =new Schema(CONFIG_FILE);
        schema.createUserSchema();
        schema.createStatusUpdateSchema();
        schema.createEdgeSchema();
        schema.close();


    }

}
