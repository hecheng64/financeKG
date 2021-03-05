package com.cc.janus.dataImport;

import com.cc.janus.core.GraphFactory;
import com.cc.janus.dataSource.read.Read;
import com.cc.janus.dataSource.read.ReadFactory;
import com.cc.janus.task.Task;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.JanusGraphTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class ImportEdge implements Task {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImportVertex.class);
    @Override
    public void execute(Map<String, Object> options) {
        Read reader = ReadFactory.getRead(options).init();

        if(!reader.check()){
            LOGGER.info("文件异常");
            System.exit(0);
        }

        AtomicInteger total = new AtomicInteger(0);//计数器
        final int thread = Integer.parseInt(options.getOrDefault("thread",1).toString());//并发度
        CountDownLatch countDownLatch = new CountDownLatch(thread);//闭锁

        boolean checkedge = Boolean.parseBoolean((String) options.
                getOrDefault("checkedge", "false")); // 检查关系信息是否存在,初次导入可以忽略

        boolean setvertexid = Boolean.parseBoolean((String) options.
                getOrDefault("setvertexid", "false")); // 自定义id

        String[] keys = (String[]) options.
                getOrDefault("keys", new String[] {}); // 如果不自定义id,则通过这些字段判断关系信息是否存在

        String[] fkeys = (String[]) options.
                getOrDefault("fkeys", new String[] {}); // 如果不自定义id,则通过这些字段判断开始节点信息是否存在

        String[] tkeys = (String[]) options.
                getOrDefault("tkeys", new String[] {}); // 如果不自定义id,则通过这些字段判断结束节点信息是否存在

        @SuppressWarnings("unchecked")
        Map<String, String> adds = (Map<String, String>)options.getOrDefault("adds",new HashMap<String,String>(){
            private static final long serialVersionUID = 4017657304973949326L;
        });

        Set<String> filters = new HashSet<>(Arrays.asList("~inVertexId", "~outVertexId",
                "~typeId", "~relationId", "~label", "label"));
        filters.addAll(Arrays.asList(fkeys));
        filters.addAll(Arrays.asList(tkeys));

        try{
            GraphFactory graphFactory = new GraphFactory((String) options.getOrDefault("conf", null));

            JanusGraphTransaction tx = graphFactory.getTx();

          for(int i = 0;i<thread; ++i){
              new Thread(new Runnable() {
                  @Override
                  public void run() {
                      AtomicInteger t_total = new AtomicInteger(0);
                      GraphTraversalSource g = graphFactory.getG();
                      String tempString = null;
                      try{
                          while ((tempString =reader.readLine()) != null){
                              t_total.addAndGet(1);
                              total.addAndGet(1);
                              if(tempString.length() > 20){
                                  net.sf.json.JSONObject content = null;

                                  try{
                                      content = JSONObject.fromObject(tempString);
                                  }catch (JSONException e){
                                      LOGGER.info("Current Position >> " + total.get() + " >> tempString :: " +
                                              tempString + " >> " + e.getMessage());
                                      continue;
                                  }finally {
                                      if (null == content) {
                                          LOGGER.info("Current Position >> " + total.get() + " >> tempString :: " +
                                                  tempString + " >> Json文件转换异常");
                                          continue;
                                      }
                                  }

                                  if (content.containsKey("label1") &&
                                          content.containsKey("label2") &&
                                          content.containsKey("name1") &&
                                          content.containsKey("name2")){
                                      String label = (content.containsKey("~label") ? content.getString("~label") :
                                              (content.containsKey("label") ? content.getString("label") :
                                                      null)); // 获取label信息

                                      if (null == label || "".equals(label.trim())) { // 如果label内容为空,则忽略本条记录
                                          System.out.println("Current Position >> " + total.get() +
                                                  " >> label :: " + label + " >> ignore.");
                                          continue;
                                      }
                                      Long outVertexId = content.containsKey("~outVertexId") ?
                                              content.getLong("~outVertexId") : null; // 获取outVertexId信息
                                      if (setvertexid && (null == outVertexId || 1 > outVertexId)) {
                                          // 如果outVertexId内容为空或小于1,则忽略本条记录
                                          System.out.println("Current Position >> " + total.get() +
                                                  " >> outVertexId :: " + outVertexId + " >> ignore.");
                                          continue;
                                      }
                                      Long inVertexId = content.containsKey("~inVertexId") ?
                                              content.getLong("~inVertexId") : null; // 获取inVertexId信息
                                      if (setvertexid && (null == inVertexId || 1 > inVertexId)) {
                                          // 如果inVertexId内容为空或小于1,则忽略本条记录
                                          System.out.println("Current Position >> " +
                                                  total.get() + " >> inVertexId :: " + inVertexId + " >> ignore.");
                                          continue;
                                      }

                                      // 判断关系是否存在
                                      if (checkedge) {
                                         HashMap<String, Object> kvs = new java.util.HashMap<String, Object>();
                                          // 临时判定条件
                                          for (String key : keys) { // 判断字段集合
                                              if (content.containsKey(key)) { // 判断字段是否存在,存在则参与计算
                                                  kvs.put(key.toString(), content.get(key));
                                              }
                                          }
                                          GraphTraversal<Edge, Edge> e = g.E();
                                          for (Map.Entry<String, Object> kv : kvs.entrySet()) {
                                              e = e.has(kv.getKey(), kv.getValue());
                                          }
                                          if (e.limit(1).hasNext()) {
                                              continue;
                                          }
                                      }

                                      Vertex startV = null,endV = null;
                                      if(setvertexid){
                                          GraphTraversal<Vertex, Vertex> starts = g.V(outVertexId);
                                          GraphTraversal<Vertex,Vertex> endVs = g.V(inVertexId);
                                          if(starts.hasNext()){
                                              startV =starts.next();
                                          }
                                          if(endVs.hasNext()){
                                              endV = endVs.next();
                                          }
                                      }else {
                                          GraphTraversal<Vertex,Vertex> starts = g.V();
                                          for(String key:fkeys){
                                              if(content.containsKey(key)){
                                                  starts = starts.has(key.toString().
                                                                  substring(0, key.toString().length() - 1).
                                                                  replaceAll("label", "type"),
                                                          content.get(key));
                                              }
                                          }
                                          GraphTraversal<Vertex,Vertex> endVs = g.V();
                                          for(String key :tkeys){
                                              if(content.containsKey(key)){
                                                  endVs = endVs.has(key.toString().
                                                          substring(0, key.toString().length() - 1).
                                                          replaceAll("label", "type"),
                                                          content.get(key));
                                              }
                                          }
                                          if(starts.hasNext()){
                                              startV = starts.next();
                                          }
                                          if(endVs.hasNext()){
                                              endV = endVs.next();
                                          }
                                      }

                                      // 起始点和结束点不为null时添加关系
                                      if (null != startV && null != endV) {
                                          Edge e = startV.addEdge(label, endV);
                                          // 将数据中的其他字段添加到Edge
                                          for (Object key : content.keySet()) {
                                              if (filters.contains(key.toString()))
                                                  // 忽略inVertexId & outVertexId & label
                                                  continue;
                                              e.property(key.toString(), content.get(key));
                                          }
                                          // e.property("type", "Statement");
                                          e.property("fvid", startV.id());
                                          e.property("tvid", endV.id());
                                          // 追加的属性
                                          if (null != adds && !adds.isEmpty()) {
                                              for (Map.Entry<String, String> entry : adds.entrySet()) {
                                                  e.property(entry.getKey(), entry.getValue());
                                              }
                                          }
                                      }else {
                                          System.out.println("Current Position >> " + "起始点::" + startV
                                                  + " >> 结束点::" + endV + " >> 关系::" + label + tempString);
                                      }

                                  }

                              }
                              if(t_total.get() % 1000 <2){
                                  try{
                                      g.tx().commit();
                                      g.tx().open();
                                  }catch (Exception e){
                                      e.printStackTrace();
                                  }
                                  LOGGER.info(Thread.currentThread().getName() + "    Current Position >> " +
                                          total.get() + "    Thread Position >> "
                                          + t_total.get());
                              }
                          }
                          g.tx().commit();
                          g.tx().open();
                      }catch (Exception e){
                          e.printStackTrace();
                          LOGGER.info("Current Position >> " + total.get() + " :: 线程出现异常 :: " + tempString);
                      }
                      countDownLatch.countDown();

                  }
              }).start();
          }
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                graphFactory.close();
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            reader.close();
        }
        LOGGER.info("I'm File Thread, I'm Over!");
    }
}
