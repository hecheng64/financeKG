package com.cc.janus.utils;

public interface conUtils {
    /**
     * 连接jannusgraph配置文件
     */
    String CONFIGFILE = "/graph.properties";
    String SERVERCONFIGFILE = "conf/remote-graph.properties";
    String CON = "gremlin.remote.driver.clusterFile";
    /**
     * 定义schema的常量
     */
      String COMPANY = "company";
      String STOCK = "stock";
//      String MONY = "mony";
//      String INDUSTRY = "industry";
//      String NAME = "name";
      String STOCKCONNECT = "stockConnect";
      String CONCEPT = "concept";
      String SHAREHOLDER = "shareholder";
      String HOLD = "hold";
      String BELONG_TO = "belong_to";
      String PUBLISH = "publish";
      String BELONG_TO_CONNECT = "belong_to_connect";


}
