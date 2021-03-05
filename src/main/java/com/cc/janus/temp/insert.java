package com.cc.janus.temp;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.tinkerpop.gremlin.driver.Client;
import org.apache.tinkerpop.gremlin.driver.Cluster;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.util.empty.EmptyGraph;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.schema.JanusGraphManagement;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class insert {
    private Graph graph;
    private JanusGraph janusGraph;
    private JanusGraphManagement mgt;
    private Configuration conf;
    private Cluster cluster;
    private Client client;
    private GraphTraversalSource g;
    public insert(String configFile) throws ConfigurationException {
        conf = new PropertiesConfiguration(configFile);
        try {
            cluster = Cluster.open(conf.getString("gremlin.remote.driver.clusterFile"));
            client = cluster.connect();
        }catch (Exception e){
            throw new ConfigurationException(e);
        }
        graph = EmptyGraph.instance();
//        janusGraph =EmptyGraph.instance();

        g = graph.traversal().withRemote(conf);


    }

    public insert() {
    }

    //    public void close() throws Exception {
//        graph.close();
//    }
    public void getP() throws IOException {

        InputStream file =null;
        file = this.getClass().getResourceAsStream("/graph.properties");
        Map<String,Object> properties = new HashMap<>();
        Properties p =new Properties();
        p.load(file);
        p.forEach((key, value) -> {
            properties.put((String) key, value);
            System.out.println(key + ":::" + value);
        });



    }
    public static void main(String[] args) throws Exception {

//        insert i =new insert("conf/remote-graph.properties");
//        Object age= i.g.V().has("name","张三").values("age").next();
//        System.out.println("姓名姓名"+age);
//        i.close();
        insert i =new insert();
        i.getP();







    }

}
