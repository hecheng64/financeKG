package com.cc.janus.dataSource.read;

import java.io.Serializable;

public interface Read extends Serializable {
    /**退出flag*/
    public  static final  String EXIT = "EXIT";

    /**
     * 初始化
     */
    Read init();

    /**
     * 按行读取，EXIT结束
     */
    String readLine();

    /**
     * 提交偏移量
     */
    <T> T commitOffser();

    /**
     * 关闭数据源
     */
    void close();

    /**
     * 检测数据源
     */
    boolean check();
}
