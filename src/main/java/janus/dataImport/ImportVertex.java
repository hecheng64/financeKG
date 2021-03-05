package com.cc.janus.dataImport;
import com.cc.janus.core.GraphFactory;
import com.cc.janus.dataSource.read.Read;
import com.cc.janus.dataSource.read.ReadFactory;
import com.cc.janus.task.Task;
import net.sf.json.JSONObject;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;

import org.apache.tinkerpop.gremlin.structure.T;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.JanusGraphTransaction;
import org.janusgraph.core.JanusGraphVertex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class ImportVertex implements Task {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImportVertex.class);

    @Override
    public void execute(Map<String, Object> options) {
        Read reader = ReadFactory.getRead(options).init();

        if(!reader.check()){
            LOGGER.info("文件异常");
            System.exit(0);
        }
//        定义计数器
        AtomicInteger total =new AtomicInteger(0);
        boolean checkvertex = Boolean.parseBoolean((String) options.
                getOrDefault("checkvertex", "false")); // 检查节点信息是否存在,初次导入可以忽略

        boolean setvertexid = Boolean.parseBoolean((String) options.
                getOrDefault("setvertexid", "false")); // 自定义id

        String []keys = (String[]) options.getOrDefault("keys",new String[]{});

        @SuppressWarnings("unchecked")
        Map<String,String> adds = (Map<String, String>) options.getOrDefault("adds",new HashMap<String,String>(){
            private static final long serialVersionUID = 5231954346316566400L;
        });//追加属性

        try {
            GraphFactory graphFactory = new GraphFactory(
                    (String) options.getOrDefault("conf", null));
            JanusGraphTransaction tx =graphFactory.getTx();
            GraphTraversalSource g = graphFactory.getG();

            String tempString =null;
            while ((tempString =reader.readLine())!=null){
                total.addAndGet(1);
                if(tempString.length() > 20){
                    net.sf.json.JSONObject content = null;
                    try{
                        content = JSONObject.fromObject(tempString);
                    }catch (net.sf.json.JSONException e){
                        LOGGER.info("Current Position >> " + total.get() + " >> tempString :: " +
                                tempString + " >> " + e.getMessage());
                        continue;
                    }finally {
                        if(content == null){
                            LOGGER.info("Current Position >> " + total.get() + " >> tempString :: " +
                                    tempString + " >> Json文件转换异常");
                            continue;
                        }
                    }
                    Long id = null;
                    if(setvertexid){
                        id = (content.containsKey("~id") ? content.getLong("~id") :
                                (content.containsKey("id") ? content.getLong("id") : null));
//                        id = (content.containsKey("~id")?content.getLong("~id"):
//                                (content.containsKey("id")?content.getLong("id"):null));
                        if (null == id || 1 > id){
                            LOGGER.info("Current Position >> " + total.get() + " >> id :: " + id + " >> ignore.");
                            continue;
                        }
                    }

                    String label = (content.containsKey("~label")?content.getString("~label"):
                            (content.containsKey("label")?content.getString("label"):null));
                    if(label==null || "".equals(label.trim())){// 如果label内容为空,则忽略本条记录
                        LOGGER.info("Current Position >> " + total.get() + " >> label :: " + label + " >> ignore.");
                        continue;
                    }
                    /**
                     * 当checvertex==true时,判断节点信息.
                     * 判断节点信息的方式,取决于setvertexid的取值方式.
                     * 当setvertexid==true时,可通过id进行判断,反之只能通过判断字段进行判断.
                     * 逻辑如下:
                     * checvertex--------false------>跳出判断.
                     *    |
                     *   true
                     *    |
                     * setvertexid------true----->通过id检查节点信息是否存在
                     *    |
                     *   false
                     *    |
                     * 通过判定条件检查
                     */
                    if(checkvertex && setvertexid && g.V(id).hasNext()){// 检查节点信息是否存在,通过id检查
                        LOGGER.info("Current Position >> " + total.get() + " >> id :: " + id + " >> existed.");
                        continue;
                    }else if(checkvertex && !setvertexid){
                        HashMap<String ,Object> kvs =new HashMap<>();
                        for(String key :keys){
                            if(content.containsKey(key)){
                                kvs.put(key,content.get(key));
                            }
                        }
                        if(has(g,kvs)){
                            LOGGER.info("Current Position >> " + total.get() + " >> keys :: " + kvs.toString() + " >> existed.");
                            continue;
                        }
                    }

                    JanusGraphVertex v = null;
                    try{
                        if(setvertexid){
                            v = tx.addVertex(T.label,label,T.id,id);
                        }else {
                            v = tx.addVertex(T.label,label);
                        }

                    }catch (IllegalArgumentException e){
                        LOGGER.info("Current Position >> " + total.get() +
                                " >> tempString :: " + tempString + " >> " + e.getMessage());
                        LOGGER.info("请检查JanusGraph配置项: graph.set-vertex-id 是否正常配置. 此值在初始化backend即以成定局, 后期修改均无效.");
                        LOGGER.info("请选择合适的校验方式.");
                        System.exit(1);

                    }

                    if(null == v){
                        LOGGER.info("Current Position >> " + total.get() +
                                " >> tempString :: " + tempString + " >> 生成新Vertex异常");
                        continue;
                    }
                    // 将数据中的其他字段添加到Vertex
                    for(Object key : content.keySet()){
                        if (key.equals("~id") || key.equals("~label") ||
                                key.equals("id") || key.equals("label")) { // 忽略~id & ~label & id & label
                            continue;
                        }
                        v.property(key.toString(), content.get(key));
                    }
                    // 追加的属性
                    if (null != adds && !adds.isEmpty()) {
                        for (Map.Entry<String, String> entry : adds.entrySet()) {
                            v.property(entry.getKey(), entry.getValue());
                        }
                    }
                    // 分批次提交,每次提交事务都会关闭,提交后需要重新创建事务
                    if (total.get() % 1000 == 0) {
                        try {
                            tx.commit();
                        } catch (java.lang.Exception e) {
                            e.printStackTrace();
                        } finally {
                            tx.close();
                        }
                        tx = graphFactory.getTx();
                        System.out.println("Current Position >> " + total.get());
                    }



                }
            }
            try{
                tx.commit();
            }catch (java.lang.Exception e){
                e.printStackTrace();
            }finally {
                tx.close();
            }
            graphFactory.close();
            LOGGER.info("Current Position >> " + total.get() + ", over!");


        }catch (Exception e){
            e.printStackTrace();
        }finally {
            reader.close();
        }
        LOGGER.info("Main Thread,I'm OK");


    }

    public static boolean has(GraphTraversalSource g,HashMap<String,Object>kvs){
        GraphTraversal<Vertex,Vertex> r =null;
        r = g.V();
        for(Map.Entry<String,Object>kv : kvs.entrySet()){
            r =r.has(kv.getKey(),kv.getValue());
        }
        return r != null && r.hasNext();
    }
}
