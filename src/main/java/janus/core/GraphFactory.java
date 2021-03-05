package com.cc.janus.core;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.JanusGraphFactory;
import org.janusgraph.core.JanusGraphFactory.Builder;
import org.janusgraph.core.JanusGraphTransaction;
import com.cc.janus.utils.conUtils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


public class GraphFactory implements Serializable {

    private static final long serialVersionUID = -2896460364799339589L;
    private Builder config = null;
    private JanusGraph graph = null;

    public GraphFactory() {
        buildConfigByFile(null);
    }
    public GraphFactory(String filepath){
        buildConfigByFile(filepath);

    }
    public Builder getConfig(){
        return config;
    }
    public JanusGraph getGraph(){
        return graph;
    }
    public GraphTraversalSource getG(){
        return this.graph.traversal();
    }
    public JanusGraphTransaction getTx(){
        return this.graph.newTransaction();
    }
    public void close(){
        if(this.graph != null){
            try{
                graph.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    private void buildConfigByFile(String congFile){
        InputStream file = null;
        try{
            file = (congFile == null )?this.getClass().getResourceAsStream(conUtils.CONFIGFILE):
                    new FileInputStream((congFile));
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
        Map<String,Object> properties =new HashMap<>();
        try{
            Properties p =new Properties();
            p.load(file);
            p.forEach((key,value)->{
                properties.put((String)key,value);
                System.out.println(key+"---"+value);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        buildConfigByProperties(properties);
    }
    private void buildConfigByProperties(Map<String,Object>properties){
        this.config = JanusGraphFactory.build();
        if(properties != null){
            properties.forEach((key,value)->this.config.set(key,value));
        }
        this.graph = this.config.open();
    }

}
