package com.cc.janus.schema;
import com.cc.janus.core.GraphFactory;
import com.cc.janus.schema.entity.IndexPropertyKey;
import com.cc.janus.temp.Schema;
import com.google.gson.GsonBuilder;

import com.cc.janus.schema.enuminfo.Mapping;
import com.cc.janus.task.Task;

import org.janusgraph.core.*;
import org.janusgraph.core.schema.ConsistencyModifier;
import org.janusgraph.core.schema.JanusGraphIndex;
import org.janusgraph.core.schema.JanusGraphManagement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Map;

public class finalSchema implements Task {
    private static final Logger LOGGER = LoggerFactory.getLogger(Schema.class);
    @Override
    public void execute(Map<String, Object> options) {
        GraphFactory graphFactory = new GraphFactory();
        JanusGraphManagement mgmt = graphFactory.getGraph().openManagement();
        try{
            com.cc.janus.schema.entity.Schema schema = new GsonBuilder().disableHtmlEscaping().create().
                    fromJson(new FileReader(String.class.cast(options.get("schemaFile"))), com.cc.janus.schema.entity.Schema.class);
            LOGGER.info("init properties...");
            schema.getPropertyKeys().forEach((p)->{
                if(mgmt.containsGraphIndex(p.getName())){
                    PropertyKey oldProperties = mgmt.getPropertyKey(p.getName());
                    LOGGER.info("propertise已存在"+oldProperties.name()+"---"+oldProperties.dataType());
                    LOGGER.info("---"+p.getName()+"---"+p.getDataType().getClazz());
                }else {
                    mgmt.makePropertyKey(p.getName()).dataType(p.getDataType().getClazz()).make();
                    LOGGER.info("已添加属性"+p.getName()+"---"+p.getDataType().getClazz());
                }
            });
            LOGGER.info("属性创建完毕");
            System.out.println();

            LOGGER.info("添加节点类型");
            schema.getVertices().forEach((v)->{
                if(mgmt.containsVertexLabel(v.getName())){
                    VertexLabel oldVerLabel = mgmt.getVertexLabel(v.getName());
                    LOGGER.info("已存在---"+ oldVerLabel.name());
                    LOGGER.info("---"+v.getName());
                }else {
                    mgmt.makeVertexLabel(v.getName()).make();
                    LOGGER.info("已添加节点类型---"+v.getName());
                }
            });
            LOGGER.info("节点类型创建完毕");
            System.out.println();

            LOGGER.info("添加边类型");
            schema.getEdges().forEach((e)->{
                if(mgmt.containsEdgeLabel(e.getName())){
                    EdgeLabel oldEdgeLabel = mgmt.getEdgeLabel(e.getName());
                    LOGGER.info("已存在--"+oldEdgeLabel.name());
                }else {
                    mgmt.makeEdgeLabel(e.getName()).make();
                    LOGGER.info("已添加边类型--"+e.getName());
                }
            });
            LOGGER.info("边类型创建完毕");
            System.out.println();

            LOGGER.info("添加索引");
            schema.getIndexKeys().forEach((i)->{
                if(mgmt.containsGraphIndex(i.getName())){
                    JanusGraphIndex oldIndex = mgmt.getGraphIndex(i.getName());
                    LOGGER.info("已存在该索引--"+oldIndex.name()+"--isUnique="+oldIndex.isUnique()+
                            "--isCompositeIndex="+oldIndex.isCompositeIndex()+
                            "--isMixedIndex="+oldIndex.isMixedIndex());
                    LOGGER.info("本次--"+i.getName()+"--isUnique"+i.getUniqueIndex()+
                            "--isCompositeIndex="+i.getCompositeIndex()+
                            "--isMixedIndex="+i.getMixIndexName());
                }else {
                    JanusGraphManagement.IndexBuilder index =null;
                    if(i.getIndexType()==null){
                        LOGGER.info("未指定索引类型，跳过本次索引创建"+i.getName());
                        return;
                    }else{
                        index =mgmt.buildIndex(i.getName(),i.getIndexType().getClazz());
                    }
//                    处理索引字段
                    for(IndexPropertyKey p :i.getPropertyKeys()){
                        if(mgmt.containsPropertyKey(p.getName())){
                            PropertyKey exesitKey = mgmt.getPropertyKey(p.getName());
                            if(p.getMapping()==null || p.getMapping() == Mapping.NULL){
                                index.addKey(exesitKey);
                            }else{
                                index.addKey(exesitKey,p.getMapping().getMapping().asParameter());
                            }
                        }else{
                            LOGGER.info("不存在该属性--"+p.getName()+"索引"+i.getName()+"忽略此属性");
                        }
                    }
//                    创建唯一索引
                    if(i.getUniqueIndex()){
                        index.unique();
                    }
//                    创建复合索引
                    if(i.getCompositeIndex()){
                        mgmt.setConsistency(index.buildCompositeIndex(),
                                null == i.getConsistencyModifier() ? ConsistencyModifier.LOCK : i.getConsistencyModifier());

                    }
//                    创建混合索引
                    if(i.getMixedIndex()){
                        if(i.getMixIndexName()==null || "".equals(i.getMixIndexName().trim())){
                            LOGGER.info("未发现索引后端"+i.getName()+"跳过本次索引创建");
                        }else{
                            index.buildMixedIndex(i.getMixIndexName());
                        }
                    }else{
                        LOGGER.info("未发现索引方式"+i.getName()+"跳过本次索引创建");
                    }
                    LOGGER.info("已添加"+i.getName());
                }
            });
            LOGGER.info("初始化索引创建完毕");
            mgmt.commit();
            LOGGER.info("schema 创建完毕");



        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            graphFactory.close();
        }


    }
//    private JanusGraphManagement mgmt;
//    private JanusGraph graph;
//    private String configFile = conUtils.CONFIGFILE;
//    private static final Logger LOGGER = LoggerFactory.getLogger(finalSchema.class);
//
//
//    public finalSchema() {
//        LOGGER.info("采用java-jar嵌入连接图");
//        graph = JanusGraphFactory.open(configFile);
//        mgmt = graph.openManagement();
//    }
//    public void createSchema(){
//
//    }
//    public void close(){
//        graph.close();
//    }

}
